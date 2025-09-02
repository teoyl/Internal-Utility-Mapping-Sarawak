package com.webservice.util;

import com.sains.common.util.CryptoSign;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.webservice.WSConstants;
//import com.webservice.WebServiceAuditor;
import com.sains.framework.base.LogFunction;
import com.webservice.model.WsClientModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.PublicKey;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.message.internal.ReaderWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */
@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter {

    // ======================================
    // =          Injection Points          =
    // ======================================
//    @Inject
//    private Logger logger;
    private LogFunction logger = new LogFunction();

//    @Inject
//    private KeyGenerator keyGenerator = new SimpleKeyGenerator();
//    private WebServiceAuditor auditor = new WebServiceAuditor();

    @Context
    private HttpServletRequest request;

    // ======================================
    // =          Business methods          =
    // ======================================
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        System.out.println("#### authorizationHeader : " + authorizationHeader);
//        logger.info("#### authorizationHeader : " + authorizationHeader);
//        logger.logInfo(this.getClass(), "#### authorizationHeader : " + authorizationHeader);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            logger.severe("#### invalid authorizationHeader : " + authorizationHeader);
//            logger.logError(this.getClass(), "#### invalid authorizationHeader : " + authorizationHeader, null);
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {

            // Validate the token (HS512)
//            Key key = keyGenerator.generateKey();
            // ThoTH @ 18-Dec-2018 : RS512   
//            PublicKey key = null;
//            BaseDAO dao = new BaseDAOImpl();
//            try {
//                String iam = request.getParameter(WSConstants.Key.Login);
//                WsClient2Model client = (WsClient2Model)dao.getObjectByCode("client_login",iam, new WsClient2Model());
            String ks = SystemConstants.DOMAIN.KeyStorePath + SystemConstants.DOMAIN.CryptoSignCert_KeyStore;

            CryptoSign cs = new CryptoSign();
            cs.initKeyStore(ks, SystemConstants.DOMAIN.CryptoSignCert_Password);
            PublicKey key = cs.getPublicKey(SystemConstants.DOMAIN.CryptoSignCert_Alias);

//            } catch (Exception e) {
//                throw e;
//            } finally {
//                dao.closeSession();
//            }
            // ThoTH @ 18-Dec-2018 : RS512 <END>
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);

            // Temp
            System.out.println("Expired at: " + Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getExpiration());

            // ThoTH @ 10-Jul-2017
//            System.out.println(requestContext.getUriInfo().getBaseUri());
//            System.out.println(requestContext.getUriInfo().getPath());
            // 31/07/2024 Aiman - commented scope cheking
//            validateScope(requestContext.getUriInfo().getPath(), key,token);
//            auditor.audit(request.getAttribute(WSConstants.ClientId), request.getRemoteAddr(), requestContext.getUriInfo().getPath(), WebServiceAuditor.AuditStatus.Request);
//            logger.info("#### valid token : " + token);
//            logger.logInfo(this.getClass(), "#### valid token : " + token);
        } catch (Exception e) {
//            logger.severe("#### invalid token : " + token);
            /*  The OAuth 2.0 Authorization Framework: Bearer Token Usage", https://tools.ietf.org/html/rfc6750, p.8, section 3.1, 
            resource server should return 401: >
            invalid_token The access token provided is expired, revoked, malformed, or invalid for other reasons. 
            The resource SHOULD respond with the HTTP 401 (Unauthorized) status code. 
            The client MAY request a new access token and retry the protected resource request.*/
            logger.logError(this.getClass(), "#### invalid token : " + token, e);
//            auditor.audit(request.getAttribute(WSConstants.ClientId), request.getRemoteAddr(), requestContext.getUriInfo().getPath(), WebServiceAuditor.AuditStatus.NoAccess);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    // ThoTH @ 10-Jul-2017
//    private void validateScope(String path, Key key, String token) throws Exception {
//        String clientId = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
//        request.setAttribute(WSConstants.ClientId, clientId); // Audit Purpose
//        
//        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
//        JSONObject jData = (JSONObject)new JSONParser().parse((String)claims.get("scopes"));
//        JSONObject jSub = null;
////        Map<String,String> scopeMap = (Map)claims.get("scopes") ;
////        System.out.println(scopeMap);
//        
//        String[] str = getPath(path);
//        
//        if (str[0].equals(WSConstants.Path.CodeService.Main)) {
//            jSub = (JSONObject)jData.get(WSConstants.Subscribe.Code);
//            if (jSub.get(WSConstants.Subscribe.Code).equals("Y")) {
//                if (str[1].equals(WSConstants.Path.CodeService.Get)) {
//                    if (! jSub.get(WSConstants.Subscribe.CodeGet).equals("Y")) {
//                        throw new Exception(WSConstants.Error.Unauthorized);
//                    }
//                } else if (str[1].equals(WSConstants.Path.CodeService.Update)) {
//                    if (! jSub.get(WSConstants.Subscribe.CodeUpdate).equals("Y")) {
//                        throw new Exception(WSConstants.Error.Unauthorized);
//                    }
//                } else if (str[1].equals(WSConstants.Path.CodeService.Coa)) {
//                    if (! jSub.get(WSConstants.Subscribe.CodeCoa).equals("Y")) {
//                        throw new Exception(WSConstants.Error.Unauthorized);
//                    }   
//                 } else if (str[1].equals(WSConstants.Path.CodeService.Division)) {
//                    if (! jSub.get(WSConstants.Subscribe.CodeDivision).equals("Y")) {
//                        throw new Exception(WSConstants.Error.Unauthorized);
//                    }     
//                } else {
//                    throw new Exception(WSConstants.Error.Unauthorized);
//                }
//                
//            } else {
//                throw new Exception(WSConstants.Error.Unauthorized);
//            }
//        }else if (str[0].equals(WSConstants.Path.PaymentService.Main)) {
//            jSub = (JSONObject)jData.get(WSConstants.Subscribe.Payment);
//            
//            if (jSub.get(WSConstants.Subscribe.Payment).equals("Y")) {
//                if (str[1].equals(WSConstants.Path.PaymentService.Get)) {
//                    if (! jSub.get(WSConstants.Subscribe.PaymentGet).equals("Y")) {
//                        throw new Exception(WSConstants.Error.Unauthorized);
//                    }
//                } else if (str[1].equals(WSConstants.Path.PaymentService.Update)) {
//                    if (! jSub.get(WSConstants.Subscribe.PaymentUpdate).equals("Y")) {
//                        throw new Exception(WSConstants.Error.Unauthorized);
//                    }
//                } else if (str[1].equals(WSConstants.Path.PaymentService.Cancel)) {
//                    if (! jSub.get(WSConstants.Subscribe.CancelBill).equals("Y")) {
//                        throw new Exception(WSConstants.Error.Unauthorized);
//                    }    
//                } else {
//                    throw new Exception(WSConstants.Error.Unauthorized);
//                }
//                
//            } else {
//                throw new Exception(WSConstants.Error.Unauthorized);
//            }
//        }else if (str[0].equals(WSConstants.Path.ProductService.Main)) {
//            jSub = (JSONObject)jData.get(WSConstants.Subscribe.Product);
//            
//            if (jSub.get(WSConstants.Subscribe.Product).equals("Y")) {
//                if (str[1].equals(WSConstants.Path.ProductService.Get)) {
//                    if (! jSub.get(WSConstants.Subscribe.ProductSearch).equals("Y")) {
//                        throw new Exception(WSConstants.Error.Unauthorized);
//                    }
//                
//                } else {
//                    throw new Exception(WSConstants.Error.Unauthorized);
//                }
//                
//            } else {
//                throw new Exception(WSConstants.Error.Unauthorized);
//            }    
//        }else if (str[0].equals(WSConstants.Path.CertificateService.Main)) {
//            jSub = (JSONObject)jData.get(WSConstants.Subscribe.Certificate);
//            
//            if (jSub.get(WSConstants.Subscribe.Certificate).equals("Y")) {
//                if (str[1].equals(WSConstants.Path.CertificateService.Get)) {
//                    if (! jSub.get(WSConstants.Subscribe.CertificateSearch).equals("Y")) {
//                        throw new Exception(WSConstants.Error.Unauthorized);
//                    }
//                
//                } else {
//                    throw new Exception(WSConstants.Error.Unauthorized);
//                }
//                
//            } else {
//                throw new Exception(WSConstants.Error.Unauthorized);
//            }       
//        } else {
//            throw new Exception(WSConstants.Error.Unauthorized);    
//        }
//    }
    // Sample: /ls/calendar
    private String[] getPath(String path) throws Exception {
//        System.out.println("path: " + path);
        if (path.length() > 0 && !path.substring(0, 1).equals("/")) {
            path = "/" + path;  // append / to path if not, to standardize
        }//        System.out.println("path2: " + path);
        String[] str = new String[3];
        int i = 0;
        while (StringUtils.ordinalIndexOf(path, "/", i + 2) >= 0) {
            str[i] = path.substring(StringUtils.ordinalIndexOf(path, "/", i + 1), StringUtils.ordinalIndexOf(path, "/", i + 2));
            i++;
        }
        // Last Path
        str[i] = path.substring(StringUtils.ordinalIndexOf(path, "/", i + 1));

        System.out.println("Service: " + str[0]);
        System.out.println("Type   : " + str[1]);
        System.out.println("3      : " + str[2]);  // for future, now only 2 Level

        return str;
    }

}
