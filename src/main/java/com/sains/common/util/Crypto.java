package com.sains.common.util;

import com.sains.framework.base.Debug;
import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
//iv length should be 16 bytes
//    private String ivBackend  = "B$a@abflRaBc.!Pj"; //<< Private Key.
    private String ivBackend = "fzeudUwEYFj62KSo";//<< Private Key. //ahnadni @ 22-Nov-2017: Standardize across projects
    private String keyBackend = null;
    private Cipher cipher = null;
    private SecretKeySpec keySpec = null;
    private IvParameterSpec ivBackendSpec = null;

    /**
     * Constructor
     *
     * @throws Exception
     */
    public Crypto(String key) throws Exception {
        this.keyBackend = key;

// Make sure the key length should be 16
        int len = this.keyBackend.length();
        if (len < 16) {
            int addSpaces = 16 - len;
            for (int i = 0; i < addSpaces; i++) {
                this.keyBackend = this.keyBackend + " ";
            }
        } else {
            this.keyBackend = this.keyBackend.substring(0, 16);
        }
        this.keySpec = new SecretKeySpec(this.keyBackend.getBytes(), "AES");
        this.ivBackendSpec = new IvParameterSpec(ivBackend.getBytes());
        this.cipher = Cipher.getInstance("AES/CBC/NoPadding");
    }

    public void changePublicKey(String key) throws Exception {
        this.keyBackend = key;

// Make sure the key length should be 16
        int len = this.keyBackend.length();
        if (len < 16) {
            int addSpaces = 16 - len;
            for (int i = 0; i < addSpaces; i++) {
                this.keyBackend = this.keyBackend + " ";
            }
        } else {
            this.keyBackend = this.keyBackend.substring(0, 16);
        }
        this.keySpec = new SecretKeySpec(this.keyBackend.getBytes(), "AES");
        this.ivBackendSpec = new IvParameterSpec(ivBackend.getBytes());
        this.cipher = Cipher.getInstance("AES/CBC/NoPadding");
    }

    /**
     * Bytes to Hexa conversion
     *
     * @param data
     * @return
     */
    public String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        } else {
            int len = data.length;
            String str = "";
            for (int i = 0; i < len; i++) {
                if ((data[i] & 0xFF) < 16) {
                    str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
                } else {
                    str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
                }
            }
            return str;
        }
    }

    /** Encrpt the goven string
     *
     * @param plainData
     * @throws Exception
     */
    public String encrypt(String plainData) throws Exception {

// Make sure the plainData length should be multiple with 16
        int len = plainData.length();
        int q = len / 16;
        int addSpaces = ((q + 1) * 16) - len;
        for (int i = 0; i < addSpaces; i++) {
            plainData = plainData + " ";
        }

        this.cipher.init(Cipher.ENCRYPT_MODE, this.keySpec, this.ivBackendSpec);
        byte[] encrypted = cipher.doFinal(plainData.getBytes());

        return bytesToHex(encrypted);
    }

    /**
     * Hexa to Bytes conversion
     *
     * @param str
     * @return
     */
    public byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    public String decrypt(String encrData) throws Exception {
        this.cipher.init(Cipher.DECRYPT_MODE, this.keySpec, this.ivBackendSpec);
        byte[] outText = this.cipher.doFinal(hexToBytes(encrData));

        String decrData = new String(outText).trim();
        return decrData;
    }

    public static void main(String[] args) throws Exception {
        Crypto c = new Crypto("D4:6E:AC:3F:F0:BE");
        Debug.printDebug("c. Encrypted Str :" + c.encrypt("Hello World"));
    }

    public void testSomething() throws Exception {
        SshClient ssh = new SshClient();
        String host = "10.17.101.118";
        Integer port=22;
        ssh.connect(host, port, new IgnoreHostKeyVerification());
        //Authenticate
        PasswordAuthenticationClient passwordAuthenticationClient = new PasswordAuthenticationClient();
        passwordAuthenticationClient.setUsername("thoth");
        passwordAuthenticationClient.setPassword("password");
        int result = ssh.authenticate(passwordAuthenticationClient);
        if(result != AuthenticationProtocolState.COMPLETE){
             Debug.printDebug("Login to " + host + ":" + port + " thoth/password failed");
        }
        //Open the SFTP channel
        SftpClient client = ssh.openSftpClient();
        //Send the file
        client.cd("impian2");
        client.put("C:\\Users\\thensw\\desktop\\CECILIATANG4");
//        client.rm("my.ini");
        //disconnect
        client.quit();
        ssh.disconnect();
    }

    private InputStream getSftpFile() throws Exception {
        SshClient ssh = new SshClient();
        String host = "10.17.101.118";
        Integer port=22;
        ssh.connect(host, port, new IgnoreHostKeyVerification());
        //Authenticate
        PasswordAuthenticationClient passwordAuthenticationClient = new PasswordAuthenticationClient();
        passwordAuthenticationClient.setUsername("thoth");
        passwordAuthenticationClient.setPassword("password");
        int result = ssh.authenticate(passwordAuthenticationClient);
        if(result != AuthenticationProtocolState.COMPLETE){
             Debug.printDebug("Login to " + host + ":" + port + " thoth/password failed");
        }
        //Open the SFTP channel
        SftpClient client = ssh.openSftpClient();
        //Send the file
//        client.cd("impian2");
//        client.pu
        ByteArrayOutputStream out = null;
        client.get("CECILIATANG4", out);
//        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        InputStream inStream = new ByteArrayInputStream(out.toByteArray());
//        client.rm("my.ini");
        //disconnect
        FileInputStream fis = new FileInputStream(new File(""));
        client.put(fis, "impian2/newname");
        client.quit();
        ssh.disconnect();
        return inStream;
    }
}
