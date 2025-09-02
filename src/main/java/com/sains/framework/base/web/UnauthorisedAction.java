package com.sains.framework.base.web;

import java.util.Map;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.Validator;

public class UnauthorisedAction extends ActionSupport {

    private static final long serialVersionUID = 8159031627650960678L;

    public String notAuthorised = null;

    public String getNotAuthorised() {
        return notAuthorised;
    }

    public void setNotAuthorised(String notAuthorised) {
        this.notAuthorised = notAuthorised;
    }
    
    private String pageErrorMsg = null;
    public String getPageErrorMsg() {
        return pageErrorMsg;
    }
    
    public void setPageErrorMsg(String pageErrorMsg) {
        this.pageErrorMsg = pageErrorMsg;
    }

    public String execute() {        
        if (!Validator.isEmpty(notAuthorised) && !notAuthorised.equalsIgnoreCase("null")) {
            addActionError(notAuthorised);
        }
        return SUCCESS;
    }
    
    public String nullRedirect() {
        if (!Validator.isEmpty(pageErrorMsg) && !pageErrorMsg.equalsIgnoreCase("null")) {
            addActionError(pageErrorMsg);
        }
        return "null";
    }
    
    public String nullRedirectESS() {
        nullRedirect();
        return "nullESS";
    }
}
