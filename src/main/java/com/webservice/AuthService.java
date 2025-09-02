/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package com.webservice;

import com.sains.common.util.BCrypt;
import com.sains.common.util.CryptoSign;
import com.webservice.model.WsClientModel;
import com.webservice.util.JWTTokenNeeded;
import com.webservice.util.KeyGenerator;
import com.sains.common.util.Encriptor;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.LogFunction;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.webservice.model.WsClientModel;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.webservice.util.AuthResponse;

/**
 * REST Web Service
 *
 * @author Aiman
 */
@Path("auth")
public class AuthService extends WebServiceBase {

    String currentSvc = "AUTH";

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AuthService
     */
    public AuthService() {
    }

    /**
     * Retrieves representation of an instance of
     * com.webservice.util.AuthService
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        Map jsonMap = new HashMap();

        jsonMap.put("status", "OK");
        jsonMap.put("response", 200);
        jsonMap.put("API", currentSvc);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(jsonMap);
    }

    /**
     * PUT method for updating or creating an instance of AuthService
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authPost(@FormParam(WSConstants.Key.Login) String login,
            @FormParam(WSConstants.Key.Password) String password) {
        System.out.println("authPost");

        String passwordHashed = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("pass hashed " + passwordHashed);

        try {
            // Authenticate the user using the credentials providsed
            WsClientModel client = authenticate(login, password);

            // Issue a token for the user
            String token = issueTokenRSA(client);  // ThoTH @ 18-Dec-2018
            System.out.println("token? - " + token);
//            auditor.audit(login, request.getRemoteAddr(), WebServiceAuditor.AuditAction.Auth, WebServiceAuditor.AuditStatus.Success);
            // Return the token on the response
            return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();

        } catch (Exception e) {
            e.printStackTrace();
//            new LogFunction().logError(this.getClass(), "", e);
//            auditor.audit(login, request.getRemoteAddr(), WebServiceAuditor.AuditAction.Auth, WebServiceAuditor.AuditStatus.Failed);
            return Response.status(UNAUTHORIZED).build();
        }
    }

    private WsClientModel authenticate(String login, String password) throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        try {
            System.out.println("login - " + login);
            System.out.println("pwd - " + password);
            WsClientModel client = (WsClientModel) dao.getObjectByCode("client_login", login, new WsClientModel());

            if (client != null) {
                System.out.println("client not null");
                System.out.println("client name " + client.getClient_name());
            }

            if (BCrypt.checkpw(password, client.getClient_pwd())) {  // ThoTH @ 20-Dec-2018 : Use more secure hashing
                System.out.println("checked pwd ok");
                return client;
            }
            throw new CustomBaseException(WSConstants.Error.Unauthorized);

        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomBaseException(WSConstants.Error.Unauthorized);

        } finally {
            dao.closeSession();
        }
    }

    // ThoTH @ 18-Dec-2018 : RS512
//    private String issueTokenRSA(WsClientModel client) throws Exception {
//        String ks = SystemConstants.DOMAIN.KeyStorePath + SystemConstants.DOMAIN.CryptoSignCert_KeyStore;
//
//        System.out.println("ks - " + ks);
//
//        try {
//
//            CryptoSign cs = new CryptoSign();
//            cs.initKeyStore(ks, SystemConstants.DOMAIN.CryptoSignCert_Password);
//            PrivateKey privateKey = cs.getPrivateKey(SystemConstants.DOMAIN.CryptoSignCert_Alias, SystemConstants.DOMAIN.CryptoSignCert_Password);
//
//            System.out.println("here ady?");
//
//            Claims claims = Jwts.claims().setSubject(client.getClient_login());
////        claims.put("scopes", client.getSubs());
////            claims.put("scopes", "");
//
//            System.out.println("j token create");
//            System.out.println("url " + uriInfo.getAbsolutePath().toString());
//
//            String jwtToken = Jwts.builder()
//                    .setSubject(client.getClient_login())
//                    .setClaims(claims)
//                    .setIssuer(uriInfo.getAbsolutePath().toString())
//                    .setIssuedAt(new Date())
//                    .setExpiration(LocalDateTime.now().plusMinutes(15).toDate())
//                    .signWith(SignatureAlgorithm.RS512, privateKey)
//                    .compact();
//
//            System.out.println("j token - " + jwtToken);
////        new LogFunction().logInfo(this.getClass(), "#### generating token for a key : " + jwtToken + " - " + key);
//            return jwtToken;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
    private String issueTokenRSA(WsClientModel client) throws Exception {
        String ks = SystemConstants.DOMAIN.KeyStorePath + SystemConstants.DOMAIN.CryptoSignCert_KeyStore;
        System.out.println("Keystore path: " + ks);

        try {
            System.out.println("Initializing CryptoSign...");
            CryptoSign cs = new CryptoSign();
            cs.initKeyStore(ks, SystemConstants.DOMAIN.CryptoSignCert_Password);
            System.out.println("KeyStore initialized.");

            System.out.println("Retrieving private key...");
            PrivateKey privateKey = cs.getPrivateKey(SystemConstants.DOMAIN.CryptoSignCert_Alias, SystemConstants.DOMAIN.CryptoSignCert_Password);
            if (privateKey == null) {
                throw new Exception("Private key is null");
            }
            System.out.println("Private key retrieved.");

//            Claims claims = Jwts.claims().setSubject(client.getClient_login());
//            System.out.println("Claims created.");

            System.out.println("Building JWT token...");
            String jwtToken = Jwts.builder()
                    .setSubject(client.getClient_login())
//                    .setClaims(claims)
                    .setIssuer(uriInfo.getAbsolutePath().toString())
                    .setIssuedAt(new Date())
                    .setExpiration(LocalDateTime.now().plusMinutes(30).toDate())
                    .signWith(SignatureAlgorithm.RS512, privateKey)
                    .compact();

            System.out.println("JWT token created:" + jwtToken);

            return jwtToken;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error issuing JWT token", e);
        }
    }

}
