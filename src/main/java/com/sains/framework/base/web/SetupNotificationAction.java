package com.sains.framework.base.web;

import com.sains.framework.base.BaseDAOImpl;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.sains.common.util.Validator;
import com.sains.common.util.DateUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;

import com.sains.framework.sam.dao.ModuleDAO;
import com.sains.framework.sam.dao.ModuleDAOImpl;
import com.sains.framework.lookup.DataRetriever;
import com.sains.framework.model.NotificationSetup;
import com.sains.framework.base.CommonFunction;

public class SetupNotificationAction extends BaseActionSupport<NotificationSetup> implements ModelDriven<NotificationSetup> {

    private final String action = "null";
    private static final long serialVersionUID = -6659925652584240539L;
    private NotificationSetup model = new NotificationSetup();
    //private ModuleDAO moduleDAO = new ModuleDAOImpl();
    private List<Options> SmsList = null;

    public SetupNotificationAction() {
        setPageTitle_("Notification");
//        baseDAO = new BaseDAOImpl();
        // set the required field for common check.
        getRequiredParam().put("no_type", "notification.no_type");
        getRequiredParam().put("no_desc", "notification.no_desc");
        getRequiredParam().put("no_sms", "notification.no_sms");
        getRequiredParam().put("no_email", "notification.no_email");
        getRequiredParam().put("no_default_sender", "notification.no_default_sender");
        getRequiredParam().put("no_template_subject", "notification.no_template_subject");
        getRequiredParam().put("no_template_body", "notification.no_template_body");

    }

    @Override // change the "String" and return value to the
    public NotificationSetup getModel() {
        return model;
    }

    public void specificValidation(String validationType) {
        if (baseDAO.isDuplicate("no_type", model)) {
            addActionError(getText("field.duplicated", new String[]{getText("notification.no_type"), model.getNo_type()}));
        }
//        if (model.getNo_system().equalsIgnoreCase("N") && !Validator.isEmpty(model.getFile_id())) {
//            addActionError(getText("notification.select.file"));
//        }
    }

    public String processInsert() {
        try {
            setPageSubTitle_("Add");
            validateRequired();
            specificValidation("insert");
            if (getActionErrors().size() > 0) {
                return SystemConstants.ACTION_Status.INSERT_FAIL;
            }

            return super.insert(getModel());
        } catch (Exception e) {
            return SystemConstants.ACTION_Status.INSERT_FAIL;
        } finally {
            super.closeSession();
        }
    }

    public String add() {
        setPageSubTitle_("Add");
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    //retrieve data for editing.
    public String loadEditPage() {
        try {
            setPageSubTitle_("Edit");
            model = super.processEdit(getModel());
        } catch (Exception e) {
        } finally {
            super.closeSession();
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String processUpdate() {
        String rtnStr = SUCCESS;
        try {
            validateRequired();
            specificValidation("update");
            setPageSubTitle_("Edit");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
            rtnStr = super.processUpdate(model);
        } catch (Exception e) {
            return SystemConstants.ACTION_Status.UPDATE_FAIL;
        } finally {
            super.closeSession();
        }
        return rtnStr;
    }

    public String delete() {
        for (String deleteId : getSelected()) {
            // do deletion checking here.
            super.delete(deleteId, getModel());
        }
        return SUCCESS;
    }

    public List<Options> getSmsList() {
        if (SmsList == null) {
            SmsList = new ArrayList();
            SmsList.add(new Options("Y", getText("yes")));
            SmsList.add(new Options("N", getText("no")));
        }
        return SmsList;
    }

    public List<Options> getSearchSmsList() {
        List<Options> searchSmsList = new ArrayList();
        searchSmsList.add(new Options("", getText("all")));
        searchSmsList.addAll(getSmsList());
        return searchSmsList;
    }

    public void setSmsList(List<Options> SmsList) {
        this.SmsList = SmsList;
    }
}
