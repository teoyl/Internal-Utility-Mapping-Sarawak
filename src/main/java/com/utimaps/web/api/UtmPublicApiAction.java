package com.utimaps.web.api;

import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport_API;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;

public class UtmPublicApiAction extends BaseActionSupport_API{// implements ModelDriven<String> {

    private static final long serialVersionUID = -6659925652584240539L;
    private String useServiceFactory_ = "N";
    private String usId = "";
    private String loginStatus = "";
    private String hValue, hashKey, from;
    private Map<String, Object> jsonMap = new HashMap();
    private Map<String, Object> jsonMaps = new LinkedHashMap();
    CommonFunction cf = new CommonFunction();
    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);

    public UtmPublicApiAction() {
        model = new String();
    }
        
    public String getUsId() {
        return usId;
    }

    public void setUsId(String usId) {
        this.usId = usId;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String gethValue() {
        return hValue;
    }

    public void sethValue(String hValue) {
        this.hValue = hValue;
    }

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Map<String, Object> getJsonMap() {
        return jsonMap;
    }

    public void setJsonMap(Map<String, Object> jsonMap) {
        this.jsonMap = jsonMap;
    }

    public Map<String, Object> getJsonMaps() {
        return jsonMaps;
    }

    public void setJsonMaps(Map<String, Object> jsonMaps) {
        this.jsonMaps = jsonMaps;
    }
    
    @Override
    public String apiApp() {
//        return "API_CALL";
        return "UTIMAPS";
    }

    @Override
    public Boolean validateRights() {
        return Boolean.TRUE;
    }
    
    public boolean checkValidation() {
        System.out.println("check validation");
        if (Validator.isEmpty(getUsId()) || Validator.isEmpty(this.hValue)) {
            jsonMap.put("message", "Required param cannot be empty.");
            return false;
        } else {
            System.out.println("hValue " + this.hValue);
            System.out.println("user " + getUsId());
            this.hashKey = cf.getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.usId, from);
            System.out.println("hashkey " + this.hashKey);
            if (this.hValue.equals(this.hashKey)) {
//                System.out.println("matched");b
                return true;
            }
        }
        jsonMap.put("message", "Invalid user id");
        return false;
    }
    
    public String publicLogin() {
        BaseDAO transDAO = new BaseDAOImpl();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//wongkk4@11Aug2021 - for pentest requirement for all API
        int code = -1;
        String status = "error";
        String message = "No record found.";
        if(checkValidation()){
            code = 0;
            status = "success";
            message = "success";
            new CommonFunction().auditLogin(SystemConstants.AUDIT_LOGIN.ACTION.LOGIN, "PUB", this.loginStatus, this.usId);
        }
        jsonMaps.put("response", code);
        jsonMaps.put("status", status);
        jsonMaps.put("message", message);

        return "success";
    }
    
    public String publicLogout() {
        BaseDAO transDAO = new BaseDAOImpl();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//wongkk4@11Aug2021 - for pentest requirement for all API
        int code = -1;
        String status = "error";
        String message = "No record found.";
        if(checkValidation()){
            code = 0;
            status = "success";
            message = "success";
            new CommonFunction().auditLogin(SystemConstants.AUDIT_LOGIN.ACTION.LOGOUT, "PUB", SystemConstants.AUDIT_LOGIN.STATUS.SUCCESS, this.usId);
        }
        jsonMaps.put("response", code);
        jsonMaps.put("status", status);
        jsonMaps.put("message", message);

        return "success";
    }
}