
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package com.lxg.common.web;

import com.lxg.common.model.Pubcode;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.ApiUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CommonList;
import com.sains.framework.base.Debug;
import com.utimaps.model.ApplicationPModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;

/**
 *
 * @author 
 */
public class LxgAction extends BaseActionSupport<ApplicationPModel>{
    private static final long serialVersionUID = -6659925652584240539L;
    public List<Pubcode> divList = new ArrayList();
    BaseDAO retrivalDAO = baseDAO;
    protected BaseDAO retrieverDAO = new BaseDAOImpl();
    protected Boolean appendEmpty = Boolean.FALSE;
    protected Boolean appendSelect = Boolean.FALSE;
    private String code1Filter = "";
    private List publicCodeList = new ArrayList();
    public String divDisString = "";
//    public String antiCSRF;
    public List<Pubcode> disList = new ArrayList();
    public Map paramMap = new HashMap();
    public String currentPage_ = "";
    public Integer currentStep_ = 1;
    public String currentTitle_ = "";
    public String formId = "";
    public String formAction = "";
    private String reg = "^0+(?!$)";//remove leading zero
    private String lotDesc_;
    
    public String strApiSystem = "";
    public String strApiUrl = "";
    
    private CommonFunction cf = new CommonFunction();
//*******getter setter
    private String codeType = "";
    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public List getPublicCodeList() {
        return publicCodeList;
    }

    public void setPublicCodeList(List publicCodeList) {
        this.publicCodeList = publicCodeList;
    }
     
    public String getCode1Filter() {
        return code1Filter;
    }


    public void setCode1Filter(String code1Filter) {
        this.code1Filter = code1Filter;
    }
    

    public String getDivDisString() {
        return divDisString;
    }

    public void setDivDisString(String divDisString) {
        this.divDisString = divDisString;
    }
   
    public List<Pubcode> getDisList() {
        return disList;
    }

    public void setDisList(List<Pubcode> disList) {
        this.disList = disList;
    }

//    public String getAntiCSRF() {
//        return antiCSRF;
//    }
//
//    public void setAntiCSRF(String antiCSRF) {
//        this.antiCSRF = antiCSRF;
//    }

    public String getCurrentPage_() {
        return currentPage_;
    }

    public void setCurrentPage_(String currentPage_) {
        this.currentPage_ = currentPage_;
    }

    public Integer getCurrentStep_() {
        return currentStep_;
    }

    public void setCurrentStep_(Integer currentStep_) {
        this.currentStep_ = currentStep_;
    }

  
    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormAction() {
        return formAction;
    }

    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    public String getStrApiSystem() {
        return strApiSystem;
    }

    public void setStrApiSystem(String strApiSystem) {
        this.strApiSystem = strApiSystem;
    }

    public String getStrApiUrl() {
        return strApiUrl;
    }

    public void setStrApiUrl(String strApiUrl) {
        this.strApiUrl = strApiUrl;
    }
    
    //yonglai @ 27/10/2024 :: to redirect back to lxg common dashboard
    public void ssoCommon() throws Exception {
        Debug.printDebug("##############ssoCommon#################");
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();
        Map param = new HashMap();
        
        param.put("userid", sessionMap.get("loginId"));
        
        Map jsonMap = new HashMap();
        
        strApiSystem = SystemConstants.SYSTEM_CODE.LXG_MAIN;
        
        String strSqlWhere = "";
        strSqlWhere = "SYSTEM_CODE = '" + strApiSystem + "'";
        strApiUrl = cf.getSingleValueWithSession(baseDAO.getSession(), "T_SETUP_SYSTEM", "SYSTEM_API_URL", strSqlWhere);
        Debug.printDebug("strApiUrl :: " +strApiUrl);
        
        jsonMap = ApiUtil.callApi_aquila(strApiSystem, strApiUrl+"/method1LxgApi", param);
        Debug.printDebug("jsonMap  callSys_ApiAuthentication "+ jsonMap);
        
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        
        response.sendRedirect(strApiUrl + "/ssoLogin?usrid="+sessionMap.get("loginId"));
        clearSessionMap(sessionMap);
    }
    
    private void clearSessionMap(Map sessionMap) {
        String pubkey = (String)sessionMap.get("pubkey");
        String prikey = (String)sessionMap.get("prikey");
        sessionMap.clear();
        sessionMap.put("pubkey", pubkey);
        sessionMap.put("prikey", prikey);
    }
}
