package com.sains.framework.base;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.ApiUtil;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Validator;
import com.sains.framework.model.ApiAuthenticationModel;
import com.sains.framework.model.ApiTokenModel;
import com.sains.framework.model.User;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;

public abstract class BaseActionSupport_API extends BaseActionSupport {
    
    public BaseActionSupport_API() {
        super();
    }

    public BaseActionSupport_API(org.hibernate.Session session) {
        super(session);
    }
    
    private Map currentApiMap = null;
    public Map getCurrentApiMap() {
        return currentApiMap;
    }
    
    private String accessToken = null;
    public String getAccessToken() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String headerAuthorization = request.getHeader("Authorization");
        if (ApiUtil.authorizationData_headerOrParamOrBoth.equals("H") || !Validator.isEmpty(headerAuthorization)) {
            accessToken = headerAuthorization.substring(7);
        } else {
            accessToken = request.getParameter("accessToken");
        }
        return accessToken;
    }
    
    private String refreshToken = null;
    public String getRefreshToken() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String headerAuthorization = request.getHeader("Authorization");
        if (ApiUtil.authorizationData_headerOrParamOrBoth.equals("H") || !Validator.isEmpty(headerAuthorization)) {
            refreshToken = headerAuthorization.substring(7);
        } else {
            refreshToken = request.getParameter("refreshToken");
        }
        return refreshToken;
    }
    
    private String apiKey = null;
    public String getApiKey() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String headerAuthorization = request.getHeader("Authorization");
        if (ApiUtil.authorizationData_headerOrParamOrBoth.equals("H") || !Validator.isEmpty(headerAuthorization)) {
            apiKey = headerAuthorization.substring(6).split(":")[0];
        } else {
            apiKey = request.getParameter("apiKey");
        }
        return apiKey;
    }

    private String apiSecret = null;
    public String getApiSecret() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String headerAuthorization = request.getHeader("Authorization");
        System.out.println("getApiSecret, headerAuthorization = " + headerAuthorization);
        if (ApiUtil.authorizationData_headerOrParamOrBoth.equals("H") || !Validator.isEmpty(headerAuthorization)) {
            apiSecret = headerAuthorization.substring(6).split(":")[1];
        } else {
            apiSecret = request.getParameter("apiSecret");
        }
        return apiSecret;
    }
    
    public abstract String apiApp();
    public abstract Boolean validateRights();
    
    private void validateTime(Map authMap) throws Exception {
        Long timeCreated = (Long)authMap.get("calendar");
        Long now = DateUtil.getCalendar().getTime().getTime();
        now -= 1000*60*60;
        if (now > timeCreated) {
            removeTokens(authMap, null);
            throw new CustomBaseException("API.authentication.token_expired");
        }
    }
    protected void generateTokens(Map authMap, ApiAuthenticationModel apiAuthModel) throws Exception{
        authMap.put("accessToken", CommonFunction.getId(30));
        authMap.put("refreshToken", CommonFunction.getId(30));
        Timestamp current = DateUtil.getCurrentTimestamp();
        authMap.put("expires_in", ""+(DateUtil.getCurrentTimestamp().getTime() + (1000*60*60)) );//convert to String; expired in 1 hour
        if (!authMap.containsKey("apiApp")) {
            authMap.put("apiApp", apiAuthModel.getApi_app());
        }
        if (!authMap.containsKey("authId")) {
            authMap.put("authId", apiAuthModel.getID());
        }
//        if (!authMap.containsKey("userId")) {
//            BaseDAO dao = baseDAO;
//            User user = (User)dao.getSession().getNamedQuery("User.findByUsUserId").setParameter("us_user_id", apiAuthModel.getApi_user_id()).uniqueResult();
//            if (user == null) {
//                throw new CustomBaseException(getText("API.authentication.apiUserNotExists", new String[] {apiAuthModel.getApi_user_id()}));
//            }
//            authMap.put("userId", user.getID());
//        }
        authMap.put("calendar", current.getTime());
//        ApiUtil.apiAuthMap.put((String)authMap.get("accessToken"), authMap);
//        ApiUtil.apiRefreshMap.put((String)authMap.get("refreshToken"), authMap);
        BaseDAO dao = new BaseDAOImpl();
        try {
            System.out.println("authMap.get(\"accessToken\") " + authMap.get("accessToken"));
            ApiTokenModel tokenModel = new ApiTokenModel();
            tokenModel.defaultAddProperties();
            dao.beginBatchTransaction();
            tokenModel.setAccess_token((String)authMap.get("accessToken"));
            tokenModel.setRefresh_token((String)authMap.get("refreshToken"));
            tokenModel.setApi_auth_id(apiAuthModel.getID());
            tokenModel.setApiapp(apiAuthModel.getApi_app());
            tokenModel.setCreated_long((Long)authMap.get("calendar"));
            tokenModel.setExpires_in(Long.parseLong((String)authMap.get("expires_in")));
            dao.getSession().save(tokenModel);
            dao.commitBatchTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollbackBatchTransaction();
        } finally {
            dao.closeSession();
        }
    }
    
    protected void removeTokens(Map authMap, BaseDAO dao) {
        System.out.println("removeTokens utimaps............");
        Boolean null_dao = Boolean.FALSE;
        if (dao == null) {
            dao = new BaseDAOImpl();
            null_dao = Boolean.TRUE;
        }
        try {
            dao.beginBatchTransaction();
            ApiTokenModel apiTokenModel = (ApiTokenModel) dao.getSession().getNamedQuery("ApiTokenModel.findBy_accessToken").setParameter("accessToken", accessToken).uniqueResult();
            dao.getSession().delete(apiTokenModel);
            dao.commitBatchTransaction();
        } catch (Exception e) {
            dao.rollbackBatchTransaction();
        } finally {
            if (null_dao) {
                dao.closeSession();
            }
        }
//        ApiUtil.apiRefreshMap.remove((String)authMap.get("refreshToken"));
//        ApiUtil.apiAuthMap.remove((String)authMap.get("accessToken"));
    }
    public void responseFail(String msg) throws Exception {
        Map returnMap = new HashMap();
        returnMap.put("status", "fail");
        returnMap.put("message", msg);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.setContentType("application/json");
        response.setStatus(400);
        response.getWriter().append(new Gson().toJson(returnMap));
        response.flushBuffer();
    }
    public void authenticate(String method) throws Exception {
//        System.out.println("ApiUtil.apiAuthMap = " + ApiUtil.apiAuthMap);
//        Map<String, Object> authMap = ApiUtil.apiAuthMap.get(getAccessToken());
        System.out.println("authenticate EIS");
        System.out.println("getAccessToken() " +getAccessToken());
        System.out.println("djfdhgfjshf");
        Map<String, Object> authMap = ApiUtil.getApiByAccessToken(getAccessToken(), baseDAO);
        if (authMap == null) {
            System.out.println("null authmap");
            throw new CustomBaseException("API.authentication.fail");
        }
        validateTime(authMap);
        if (validateRights()) {
//            System.out.println("getCurrentApiMap() = " + getCurrentApiMap());
//            if (!has_right_byAppRightCode(this.getClass().getSimpleName(), "ALL,"+(method==null?"":method), authMap.get("userId"))) {
//                throw new CustomBaseException("API.authentication.success_noRights");
//            }
        }
        currentApiMap = authMap;
        System.out.println("getCurrentApiMap() = " + getCurrentApiMap());
//        if (ApiUtil.EnableApiAuthentication) {
//            
//            if (refreshToken!=null) {
//                if (ApiUtil.apiAuthMap.containsKey(refreshToken)) {
//                    Map<String, Object> authMap = ApiUtil.apiRefreshMap.get(refreshToken);
//                    validateTime(authMap);
//                    generateTokens(authMap, null);
//                } else {
//                    throw new CustomBaseException("API.authentication.invalidRefreshToken");
//                }
//            }
//            if (!ApiUtil.apiAuthMap.containsKey(accessToken)) { //no accessToken
//                System.out.println("apiSecret = " + getApiSecret());
//                System.out.println("apiKey = " + getApiKey());
//                if (Validator.isEmpty(getApiKey()) || Validator.isEmpty(getApiSecret())) { //no apiKey or apiSecret
//                    throw new CustomBaseException("API.authentication.fail");
//                }
//                BaseDAO dao = baseDAO;
//                ApiAuthenticationModel apiAuthModel = (ApiAuthenticationModel) dao.getSession().getNamedQuery("")
//                        .setParameter("apiApp", apiApp())
//                        .setParameter("apiKey", getApiKey())
//                        .setParameter("apiSecret", getApiSecret())
//                        .uniqueResult();
//                if (apiAuthModel == null) {
//                    throw new CustomBaseException("API.authentication.invalidKeySecret");
//                }
//                Map<String, Object> authMap = new HashMap();
//                generateTokens(authMap, apiAuthModel);
////                authMap.put("accessToken", CommonFunction.getId(30));
////                authMap.put("refreshToken", CommonFunction.getId(30));
////                authMap.put("apiApp", apiApp());
////                authMap.put("authId", apiAuthModel.getID());
////                authMap.put("calendar", DateUtil.getCalendar());
////                ApiUtil.apiAuthMap.put((String)authMap.get("accessToken"), authMap);
////                ApiUtil.apiRefreshMap.put((String)authMap.get("refreshToken"), authMap);
//                responseCall(authMap);
//            } else { //has AccessToken
//                Map<String, Object> authMap = ApiUtil.apiAuthMap.get(accessToken);
//                validateTime(authMap);
//            }
//        }
//        return null;
    }
    
    public void responseCall(Map returnMap) throws Exception {//status:success/fail, message, data
        Map statusMap = new HashMap();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        if (returnMap.containsKey("responseCode")) {
            response.setStatus((Integer)returnMap.remove("responseCode"));
            if (!returnMap.containsKey("status")) {
                if (response.getStatus() >= 400) {
                    statusMap.put("status", "fail");
                } else if (response.getStatus() < 300) {
                    statusMap.put("status", "success");
                }
            }
        } else {
            if (returnMap.containsKey("status")) {
                if (((String)returnMap.get("status")).equalsIgnoreCase("fail")) {
                    response.setStatus(400);
                }
            } else {
                statusMap.put("status", "success");
            }
        }
        if (!statusMap.containsKey("status") && returnMap.containsKey("status")) {
            statusMap.put("status", returnMap.remove("status"));
        }
        if (!returnMap.isEmpty()) {
            Map cpMap = new HashMap();
            cpMap.putAll(returnMap);
            cpMap.remove("authId");
            cpMap.remove("calendar");
            cpMap.remove("userId");
            statusMap.put("message", cpMap.remove("message"));
            if (!cpMap.isEmpty()) {
                statusMap.put("data", cpMap);
            }
        }
        response.setContentType("application/json");
        response.getWriter().append(new Gson().toJson(statusMap));
        response.flushBuffer();
    }
}
