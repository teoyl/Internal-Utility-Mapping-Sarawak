package com.sains.framework.base.web;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.SystemConstants;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;

public class HandShakeAction extends ActionSupport {

    private static final long serialVersionUID = 8123321627650960678L;

    public String execute() throws Exception {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.getWriter().append(SystemConstants.RedirectPattern.ALIVE);
//        response.getWriter().append("a");
        response.flushBuffer();
        return null;
    }
}
