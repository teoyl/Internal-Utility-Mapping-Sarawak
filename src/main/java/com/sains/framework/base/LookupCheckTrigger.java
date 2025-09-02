package com.sains.framework.base;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.framework.lookup.LookupAction;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

public class LookupCheckTrigger{
    private Map<String, String> serverSideCheckMethod = new HashMap();
    private HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    public LookupCheckTrigger() {
        serverSideCheckMethod.put("1", "calculate1");
        serverSideCheckMethod.put("2", "abc");
    }

    public String calculate1(LookupAction lookupAction){
        String[] writeToArray = request.getParameter("_writeTo").split(",");
        int i = 100;
        for (String str : writeToArray){
            lookupAction.getWriteToList().add(str.trim());
            lookupAction.getWriteToDataList().add(""+i++);
        }
//        if (true) { //to return error to lookup check
//            actionSupport.addActionError("da da da da....");
//        }
        return ActionSupport.SUCCESS;
    }
    public String abc(LookupAction lookupAction){
        String[] writeToArray = request.getParameter("_writeTo").split(",");
        int i = 200;
        for (String str : writeToArray){
            lookupAction.getWriteToList().add(str.trim());
            lookupAction.getWriteToDataList().add(""+i++);
        }
//        if (true) { //to return error to lookup check
//            actionSupport.addActionError("da da da da....");
//        }
        return ActionSupport.SUCCESS;
    }

    public String getLookupMethod(String key) {
        return serverSideCheckMethod.get(key);
    }
}

