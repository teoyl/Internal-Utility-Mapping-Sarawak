package com.sains.framework.base.web;

import com.backend.SetupSchedularModel;
import com.backend.TimerJob;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CommonList;
import com.sains.framework.base.TimerBase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

public class StaticVarAction extends BaseActionSupport{// implements ModelDriven<String> {

    private static final long serialVersionUID = -6659925652584240540L;

    public StaticVarAction() {
        // change the DAO to the correct service.
        //baseDAO = new BaseDAOImpl();
        model = new String();
        setInsertUpdateSuccessCode(SystemConstants.ACTION_Status.LOAD_EDIT_PAGE);
        // set the required field for common check.
        //getRequiredParam().put("column_name1", "column.name1");
        //getRequiredParam().put("column_name2", "column.name2");


    }

    public void specificValidation(String validationType) {
    }

    public String delete() {
        return SUCCESS;
    }

    //** for static var management :: START **//
    public Map getStaticVarMap() {
        return SystemConstants.staticVarMap;
    }
    
    @Override
    public String loadEditPage() {
        return "staticVarList";
    }
    
    @Override
    public String processUpdate() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        for (String key : SystemConstants.staticVarMap.keySet()) {
            SystemConstants.staticVarMap.put(key, request.getParameter(key));
            switch (key) {
                case "triggerTimer" :
                    TimerBase.triggerTimer = request.getParameter(key).equalsIgnoreCase("true"); break;
                case "SMS_Queue" :
                    TimerBase.manageableJob.get(key).setJobSwitch_on(request.getParameter(key).equalsIgnoreCase("true")); break;
                case "EMAIL_Queue" :
                    TimerBase.manageableJob.get(key).setJobSwitch_on(request.getParameter(key).equalsIgnoreCase("true")); break;
                    
            }
        }
        CommonFunction.saveStaticVar();
        return loadEditPage();
    }
    //** for static var management :: END **//
}
