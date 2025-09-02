package com.utimaps.web.api;

import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport_API;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.LogFunction;
import com.sains.workflow.util.RouteUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import org.apache.struts2.ServletActionContext;

public class UtmApiAction extends BaseActionSupport_API{// implements ModelDriven<String> {

    private static final long serialVersionUID = -6659925652584240539L;
    private String useServiceFactory_ = "N";
    private String userid;
    private String strSQL = "";
    
    //mobile api parameter
    
    //payment api
    private String billRefNo = "";
    private String hashTotal = "";
    private String paymentId = "";
    private String appCaseId = "";
    private String paymentOptionId = "";
    
    private int pageNumber, perPage, total, pageTotal;
    private Map<String, Object> jsonMap = new HashMap();
    private Map<String, Object> jsonMaps = new HashMap();
    private Map<String, Object> metaMap = new HashMap();
    private RouteUtil routeUtil = new RouteUtil();
    
    private CommonFunction cf = new CommonFunction();
    private BaseDAO dao = baseDAO;
    
    public UtmApiAction() {
        model = new String();
        setInsertUpdateSuccessCode(SystemConstants.ACTION_Status.LOAD_EDIT_PAGE);
    }
        
    @Override
    public String apiApp() {
        return "USJ";
    }

    @Override
    public Boolean validateRights() {
        return Boolean.TRUE;
    }
    
    public void tryApiCall() throws Exception {
        Map map = new HashMap();
        map.put("message", "tryApiCall triggered");
        responseCall(map);
    }

    public void method1() throws Exception {
        Map map = new HashMap();
        map.put("message", "This is method1 from EIS. Over...");
        responseCall(map);
    }
    
    public void method2() throws Exception {
        Map map = new HashMap();
        map.put("message", "This is method2. Over...");
        responseCall(map);
    }
    
    public void accessKey() throws Exception {        
        Map map = new HashMap();
        map.put("message", "This is accessKey() from UTiMAPS. Over.");
        responseCall(map);
    }
    
    public String searchPaymentCashtran(){
        CommonFunction.writeFile("UtmApi", "call searchPaymentCashtran");
        BaseDAO retrieverDAO = new BaseDAOImpl();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//wongkk4@11Aug2021 - for pentest requirement for all API
        int code = -1;
        String status = "error";
        String message = "No record found.";
        
        CommonFunction.writeFile("UtmApi", "billRefNo: "+ this.billRefNo);
        CommonFunction.writeFile("UtmApi", "hashTotal: "+ this.hashTotal);
        
        try {
            if(Validator.isEmpty(this.billRefNo)){ //item_reference_number
                jsonMaps.put("response", code);
                jsonMaps.put("status", status);
                jsonMaps.put("message", "Bill Reference No. is empty.");
               
                return "success";
            }
            
            if(Validator.isEmpty(this.hashTotal)){
                jsonMaps.put("response", code);
                jsonMaps.put("status", status);
                jsonMaps.put("message", "Hash total is empty.");
               
                return "success";
            }
            
            String commonHashTotal = new CommonFunction().getHashCashTran(this.billRefNo);
            CommonFunction.writeFile("UtmApi", "this.commonHashTotal : "+ commonHashTotal);
            
            if(this.hashTotal.equals(commonHashTotal) ){
                CommonFunction.writeFile("UtmApi", "match hashtotal");

                code = 0;
                status = "Success";
                message = "Retrieve cashtran payment details successfully.";
            }
        }catch (Exception e) {
            CommonFunction.writeFile("UtmApi", "Retrieve cashtran payment detail unsuccessfully. " + e);
            new LogFunction().logError(this.getClass(), " *** Failed to retrieve cashtran payment detail successfully. *** ", e);
        } finally {
            baseDAO.closeSession();
        }
        jsonMaps.put("response", code);
        
       return "success";
   }
      
    public static String getDivFromShortkey1(String shortkey1) {
        if(Validator.isEmpty(shortkey1)) return "";
        String caseDiv = "";
        caseDiv = String.valueOf(Integer.parseInt(shortkey1.substring(2, 4))) ;
        return caseDiv;
    }
    
    public String decryptUser(String token) {
        String userStr = "";
        String secretKey = "lXGM0bile";
        
        Key signingKey = this.getSigningKey(secretKey);
        CommonFunction.writeFile("UtmApi", "signingKey" +signingKey.toString());
        
        Jws<Claims> jwtClaims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token); //parse token
        userStr = jwtClaims.getBody().getId().toLowerCase(); //get id of token
        
        return userStr;
    }
    
    private Key getSigningKey(String secretKey) {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = null;
        //We will sign our JWT with our ApiKey secret
        apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);

        
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        CommonFunction.writeFile("UtmApi", "signingKey :" +signingKey);
        return signingKey;
    }
}