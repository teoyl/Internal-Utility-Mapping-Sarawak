package com.sains.framework.base.web;

import com.sains.framework.base.LogFunction;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.ServletActionContext;

import com.sains.common.util.Validator;
import com.sains.common.util.DateUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;

import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.model.AutoEmailParam;

public class AutoEmailAction extends BaseActionSupport<AutoEmail> implements ModelDriven<AutoEmail> {

    private static final long serialVersionUID = -6659925652584240539L;
    private final String DELETED_PARAM_LIST = "deletedParams";
    private AutoEmail autoEmail = new AutoEmail();
    private List<AutoEmail> AutoEmailList = new ArrayList<AutoEmail>();
    private AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
    private List<Options> AutoEmailTypeOption = null;
    private List<AutoEmailParam> emailParamList = new ArrayList();
    private List<AutoEmailParam> deletedemailParamList = new ArrayList();
    private String notification_code;
    private String notification_desc;
    private String[] param_selected;
    private String[] param_id;
    private String[] param_name;
    private String[] retrieve_from;

    // for JSP Form
    private List autoTypeList = null;

    public AutoEmailAction() {
        /*getRequiredParam().put("AutoEmail_code", "AutoEmail.code");
         getRequiredParam().put("AutoEmail_name", "AutoEmail.name");
         getRequiredParam().put("action_name", "AutoEmail.actionName");
         getRequiredParam().put("attached_module_id", "AutoEmail.attachedTo");*/
    }

    @Override
    public AutoEmail getModel() {
        return autoEmail;
    }

    public void specificValidation(String validationType) {
        try {
            copyParamToList();

            if (!Validator.isEmpty(autoEmail.getCode())) {
                //Duplication check. (AutoEmail_id = "" means it is a new record.
                if (autoEmail.getAuto_email_id().equals("")) {
                    if (autoEmailDAO.getAutoEmailByCode(autoEmail.getCode(), autoEmail) != null) {
                        addActionError(getText("field.duplicated", new String[]{getText("autoEmailSetup.code"), autoEmail.getCode()}));
                    }
                } else { //existing record.
                    if (autoEmailDAO.getAutoEmailByCode(autoEmail.getCode(), autoEmail) != null
                            && !(autoEmailDAO.getAutoEmailByCode(autoEmail.getCode(), autoEmail).getAuto_email_id().equals(autoEmail.getAuto_email_id()))) {
                        addActionError(getText("field.duplicated", new String[]{getText("autoEmailSetup.code"), autoEmail.getCode()}));
                    }
                }
            }

            int index = 1, sameCount, innerLoop = 0;
            boolean hasError = false;
            Set<String> set = new HashSet();
            for (AutoEmailParam emailParam : emailParamList) {
                if (Validator.isEmpty(emailParam.getParam_name())) {
                    addActionError(getText("errors.itemized", "" + index, getText("errors.required", "<s:text name='autoEmailSetup.paramName' />")));
                    hasError = true;
                }
                if (Validator.isEmpty(emailParam.getRetrieve_from())) {
                    addActionError(getText("errors.itemized", "" + index, getText("errors.required", "<s:text name='autoEmailSetup.paramFrom' />")));
                    hasError = true;
                }

                if (hasError) {
                    break;
                } else {
                    sameCount = 0;
                    //for (AutoEmailParam emailParam2 : emailParamList) {
                    for (int inner = index - 1; inner < emailParamList.size(); inner++) {
                        AutoEmailParam emailParam2 = emailParamList.get(inner);
                        if (emailParam2.getParam_name().equalsIgnoreCase(emailParam.getParam_name())) {
                            sameCount++;
                        }
                        if (sameCount >= 2) {
                            set.add(emailParam2.getParam_name());
                            break;
                        }
                    }
                }
                index++;
            }
            if (set.size() > 0) {
                String strSame = "";
                for (String str : set) {
                    if (strSame.equals("")) {
                        strSame = str;
                    } else {
                        strSame += "," + str;
                    }
                }
                addActionError(getText("field.duplicated", new String[]{getText("autoEmailSetup.paramName"), strSame}));
            }
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
            autoEmailDAO.closeSession();
        }
    }

    public String loadAddPage() {
        removeList();
        setPageTitle_("Email Setup");
        setPageSubTitle_("Add");
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    public String processInsert() {
        try {
            specificValidation("insert");
            if (getActionErrors().size() > 0) {
                return SystemConstants.ACTION_Status.INSERT_FAIL;
            }
            getModel().defaultAddProperties();
            getModel().setAutoEmailParam(getEmailParamList());
            for (AutoEmailParam emailParam : getModel().getAutoEmailParam()) {
                emailParam.defaultAddProperties();
            }
            autoEmailDAO.create(getModel());
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
            returnStr = SystemConstants.ACTION_Status.INSERT_FAIL;
        } finally {
            autoEmailDAO.closeSession();
        }
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("createSuccess"));
        } else {
            addActionError(getText("createFail"));
        }
        return returnStr;
    }

    public String loadEditPage() {
        try {
            removeList();
            setPageTitle_("Email Setup");
            setPageSubTitle_("Edit");
            autoEmail = super.processEdit(getModel());
            // preload rightsList: START
            notification_code = autoEmail.getNotificationSetup().getNo_type();
            autoEmail.getAutoEmailParam().size();
            // preload rightsList: DONE
            setEmailParamList(autoEmail.getAutoEmailParam());
            /*for (AutoEmailRights ar : getAppRightsList()){
             System.out.println("checking methods " + ar.getApp_rights_methods());
             }*/
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
        } finally {
            super.closeSession();
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String processUpdate() {
        String strId = "";

        try {
            specificValidation("update");
            if (getActionErrors().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
            getModel().defaultUpdateProperties();
            getModel().setAutoEmailParam(getEmailParamList());

            strId = getModel().getID();
            for (AutoEmailParam emailParam : getModel().getAutoEmailParam()) {
                if (emailParam.getID().equals("")) {
                    emailParam.defaultAddProperties();
                    emailParam.setAuto_email_id(strId);
                } else {
                    emailParam.defaultUpdateProperties();
                }
            }
            autoEmailDAO.update(getModel(), getDeletedParamList());
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
            returnStr = SystemConstants.ACTION_Status.UPDATE_FAIL;
        } finally {
            autoEmailDAO.closeSession();
        }
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("updateSuccess"));
        } else {
            addActionError(getText("updateFail"));
        }
        return SUCCESS;
    }

    private void removeList() {
        ActionContext.getContext().getSession().remove(DELETED_PARAM_LIST);
    }

    public List<AutoEmailParam> getDeletedParamList() {
        List deletedParamList = (List) ActionContext.getContext().getSession().get(DELETED_PARAM_LIST);
        if (deletedParamList == null) {
            deletedParamList = new ArrayList();
            ActionContext.getContext().getSession().put(DELETED_PARAM_LIST, deletedParamList);
        }

        return deletedParamList;
    }

    public String delete() {
        try {
            if (getSelected() == null) {
                setRedirectMessage(getText("deleteFail"));
                return SystemConstants.ACTION_Status.DELETE_FAIL;
            }
            for (String deleteId : getSelected()) {
                if (deleteId.equalsIgnoreCase("false")) {
                    setRedirectMessage(getText("deleteFail"));
                    return SystemConstants.ACTION_Status.DELETE_FAIL;
                }
            }
            //perform deletion one go.
            autoEmailDAO.delete(getSelected());
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
            returnStr = SystemConstants.ACTION_Status.DELETE_FAIL;
        } finally {
            autoEmailDAO.closeSession();
        }
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("deleteSuccess"));
        } else {
            addActionError(getText("deleteFail"));
        }
        return SUCCESS;
    }

    private void copyParamToList() {
        int index = 0;
        if (param_name != null) {
            for (String code : param_name) {
                AutoEmailParam emailParam = new AutoEmailParam();
                emailParam.setParam_name(code);
                emailParam.setRetrieve_from(retrieve_from[index]);
                emailParam.setParam_id(param_id[index]);
                emailParam.setAuto_email_id(getModel().getID());
                index++;
                emailParamList.add(emailParam);
            }
        }
    }

    public String processAddParam() {
        copyParamToList();
        emailParamList.add(new AutoEmailParam());
        setPageTitle_("Email Setup");
        setPageSubTitle_("Add");
        if (getModel().getAuto_email_id() != null && !getModel().getAuto_email_id().equals("")) {
            setPageSubTitle_("Edit");
            return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
        }
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    public String processDeleteParam() {
        copyParamToList();
        setPageTitle_("Email Setup");
        setPageSubTitle_("Add");
        if (param_selected != null) {
            for (int index = param_selected.length - 1; index >= 0; index--) {
                AutoEmailParam emailParam = emailParamList.remove(Integer.parseInt(param_selected[index]));
                if (emailParam.getAuto_email_id() != null && !emailParam.getAuto_email_id().equals("")) {
                    //keep the existing appRights to a list.
                    getDeletedParamList().add(emailParam);
                }
            }
        }
        if (getModel().getAuto_email_id() != null && !getModel().getAuto_email_id().equals("")) {
            setPageSubTitle_("Edit");
            return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
        }
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    public List<AutoEmail> getAutoEmailList() {
        return AutoEmailList;
    }

    public void setAutoEmailList(List<AutoEmail> userList) {
        this.AutoEmailList = userList;
    }

    public List<Options> getAutoEmailTypeOption() {
        if (AutoEmailTypeOption == null) {
            AutoEmailTypeOption = new ArrayList();
            AutoEmailTypeOption.add(new Options("A", getText("AutoEmail")));
            AutoEmailTypeOption.add(new Options("R", getText("report")));
        }
        return AutoEmailTypeOption;
    }

    public void setAutoEmailTypeOption(List<Options> AutoEmailTypeOption) {
        this.AutoEmailTypeOption = AutoEmailTypeOption;
    }

    public String getNotification_code() {
        return notification_code;
    }

    public void setNotification_code(String notification_code) {
        this.notification_code = notification_code;
    }

    public String getNotification_desc() {
        return notification_desc;
    }

    public void setNotification_desc(String notification_desc) {
        this.notification_desc = notification_desc;
    }

    public List<AutoEmailParam> getEmailParamList() {
        return emailParamList;
    }

    public void setEmailParamList(List paramList) {
        this.emailParamList = paramList;
    }

    public String[] getParam_id() {
        return param_id;
    }

    public void setParam_id(String[] param_id) {
        this.param_id = param_id;
    }

    public String[] getParam_name() {
        return param_name;
    }

    public void setParam_name(String[] param_name) {
        this.param_name = param_name;
    }

    public String[] getRetrieve_from() {
        return retrieve_from;
    }

    public void setRetrieve_from(String[] retrieve_from) {
        this.retrieve_from = retrieve_from;
    }

    public String[] getParam_selected() {
        return param_selected;
    }

    public void setParam_selected(String[] paramSelected) {
        param_selected = paramSelected;
    }

    public List getAutoTypeList() {
        if (autoTypeList == null) {
            autoTypeList = new ArrayList();
            autoTypeList.add(new Options(SystemConstants.AutoEmail_Type.AI, getText("autoEmailSetup.autoType.AI")));
            autoTypeList.add(new Options(SystemConstants.AutoEmail_Type.WF, getText("autoEmailSetup.autoType.WF")));
        }
        return autoTypeList;
    }

    public void setAutoTypeList(List autoTypeList) {
        this.autoTypeList = autoTypeList;
    }

}
