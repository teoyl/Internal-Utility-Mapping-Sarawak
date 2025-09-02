package com.sains.framework.base.web;

import com.sains.common.util.ApiUtil;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport_API;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.model.ApiAuthenticationModel;
import java.util.HashMap;
import java.util.Map;

public class ApiTokenAction extends BaseActionSupport_API {
    
    public ApiTokenAction() {
        super();
    }

    public ApiTokenAction(org.hibernate.Session session) {
        super(session);
    }

    @Override
    public String apiApp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean validateRights() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void token() throws Exception {
        System.out.println("inside token");
        if (Validator.isEmpty(getApiKey()) || Validator.isEmpty(getApiSecret())) { //no apiKey or apiSecret
            responseFail(getText("API.authentication.fail"));
        } else {
            BaseDAO dao = baseDAO;
            ApiAuthenticationModel apiAuthModel = (ApiAuthenticationModel) dao.getSession().getNamedQuery("ApiAuthenticationModel.findBy_apiKey_apiSecret")
                    .setParameter("apiKey", getApiKey())
                    .setParameter("apiSecret", getApiSecret())
                    .uniqueResult();
            if (apiAuthModel == null) {
                responseFail(getText("API.authentication.invalidKeySecret"));
            } else {
                Map<String, Object> authMap = new HashMap();
                generateTokens(authMap, apiAuthModel);
                responseCall(authMap);
            }
        }
    }
    
    public void refresh() throws Exception {
        if (Validator.isEmpty(getRefreshToken())) { //no refreshToken
            responseFail(getText("API.authentication.invalidRefreshToken"));
        } else {
            Map authMap = (Map)ApiUtil.apiRefreshMap.get(getRefreshToken());
            if (authMap != null) {
                removeTokens(authMap, baseDAO);
                generateTokens(authMap, null);
            } else {
                authMap = new HashMap();
                authMap.put("status", "fail");
                authMap.put("message", getText("API.authentication.invalidRefreshToken"));
            }
            responseCall(authMap);
        }
    }
    
    public void revoke() throws Exception {
        Map authMap = null;
        if (Validator.isEmpty(getAccessToken())) {
            authMap = new HashMap();
            authMap.put("status", "fail");
            authMap.put("message", getText("API.authentication.invalidAccessToken"));
        } else {
            authMap = (Map)ApiUtil.apiAuthMap.get(getAccessToken());
            if (authMap != null) {
                removeTokens(authMap, baseDAO);
            }
            authMap.clear();
            authMap.put("status", "success");
            authMap.put("message", getText("API.authentication.revoked"));
        }
        responseCall(authMap);
    }
}
