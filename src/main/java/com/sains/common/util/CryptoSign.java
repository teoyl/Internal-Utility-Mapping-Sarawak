/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sains.common.util;

import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.SystemConstants;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Map;
import javax.crypto.Cipher;

/**
 *
 * @author thoth @ 3-Aug-2018 
 * a place for you to generate Key and do Encryption / Decryption easily.
 * 
 * Idea from http://niels.nu/blog/2016/java-rsa.html
 */
public class CryptoSign {
    private static final String UTF8 = "UTF-8";
    private static final String SES_PRIVATE_KEY = "privateKey";
    private static final String SES_PUBLIC_KEY = "publicKey";
    protected KeyStore keyStore;

    public KeyStore getKeyStore() {return keyStore;}
    
    public void initKeyStore(String keystore, String ks_pass) throws GeneralSecurityException, IOException {
		keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//		keyStore.load(new FileInputStream(getClass().getResource(keystore).getFile()), ks_pass.toCharArray());
		keyStore.load(new FileInputStream(keystore), ks_pass.toCharArray());
	}
 
	public X509Certificate getCertificate(String alias) throws KeyStoreException {
		return (X509Certificate) keyStore.getCertificate(alias);
	}
 
    public PublicKey getPublicKey(String alias) throws GeneralSecurityException, IOException {
		return getCertificate(alias).getPublicKey();
	}
 
	public PrivateKey getPrivateKey(String alias, String pk_pass) throws GeneralSecurityException, IOException {
        KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(pk_pass.toCharArray());
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, keyPassword );
		return privateKeyEntry.getPrivateKey();
	}
    
    public static KeyPair generateKeyPair(Boolean saveToSession) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        
        if (saveToSession) {
            Map sessionMap = ActionContext.getContext().getSession();
            sessionMap.put(SES_PRIVATE_KEY,pair.getPrivate());
            sessionMap.put(SES_PUBLIC_KEY,pair.getPublic());
        }
        return pair;
    }
    
    /* this is very useful when building API’s, is sign a message with our private key and verify the signature with the public key. 
       This allows us to make sure that a message indeed comes from the creator of our public key (the private key holder) 
       and that it wasn’t tampered with in transit*/
    public static String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF8));

        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }
    
    public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes(UTF8));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }
 
    
    /* !!! While theoretically possible to do the reverse (encrypt with private key, decrypt with public) 
           this is not secure at all and most libraries (including java.security) won’t let you. */
    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF8));
        return Base64.getEncoder().encodeToString(cipherText);
    }
    
    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);
        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(decriptCipher.doFinal(bytes), UTF8);
    }
}
