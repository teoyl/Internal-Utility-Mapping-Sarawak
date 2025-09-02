package com.sains.framework.sam.web;

import com.sains.framework.base.CommonList;
import com.sains.framework.base.LogFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.sains.common.util.Validator;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.model.GroupUser;
import com.sains.framework.sam.dao.UserDAO;
import com.sains.framework.sam.dao.UserDAOImpl;
import com.sains.framework.model.User;

public class UserAction extends BaseActionSupport<User> implements ModelDriven<User> {

    private static final long serialVersionUID = -5699629435615680208L;
    private User model = new User();
    private UserDAO userDAO = new UserDAOImpl();
    private List<Options> statusList = null;
    private List<Options> adminList = null;
    private List divisionList = null;
    private List ptkOfficeList = null;
    private List ldapList = null;
    private Map<String, Object> searchParam = new HashMap();
    private List nationalityList = new ArrayList();
    private List stateList = new ArrayList();

    private String confirmPassword = "";

    public UserAction() {
        setPageTitle_("User");
        searchParam.put("us_user_id", model.getUs_user_id());
        searchParam.put("us_user_name", model.getUs_user_name());
        searchParam.put("us_email", model.getUs_email());
        searchParam.put("us_status", model.getUs_status());
        searchParam.put("us_admin", model.getUs_admin());
    }

    @Override
    public User getModel() {
        return model;
    }

    public void specificValidation(String validationType) {
        //System.out.println("specificValidation ="+ validationType);
        if (validationType.equalsIgnoreCase("APPROVE") || validationType.equalsIgnoreCase("REJECT")||validationType.equalsIgnoreCase("SAVEPUBLIC")) {
            getRequiredParam().put("us_user_id", "lbl.username");
            //getRequiredParam().put("us_password", "lbl.password");
            //getRequiredParam().put("confirmPassword", "lbl.confirm.password");
            getRequiredParam().put("us_user_name", "lbl.user.full.name");
            getRequiredParam().put("us_id_number", "lbl.ic.no");
            getRequiredParam().put("us_email", "lbl.email.address");
            getRequiredParam().put("us_hp_number", "lbl.handphone");
            getRequiredParam().put("userCompany.co_name", "msen.registeredName");
            getRequiredParam().put("userCompany.co_reg_num", "msen.registeredNo");
            getRequiredParam().put("userCompany.co_registered_address1", "user.coAddress");
            getRequiredParam().put("userCompany.co_phone_no1", "user.coPhone1");
            getRequiredParam().put("userCompany.paid_up_capital_str", "msen.paidCapital");
            getRequiredParam().put("userCompany.business_type", "msen.businessType");
            validateRequired();
            //check password tally with confirm password

//            if (!Validator.isEmpty(model.getUs_password())) { // if password is not empty
//                String validPassword = "true";
//                String legalChars = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//                for (int i = 0; i < model.getUs_password().length(); i++) {
//                    if (legalChars.indexOf(model.getUs_password().charAt(i)) < 0) { // check invalid characters
//                        validPassword = "false";
//                        break;
//                    }
//                }
//                if (validPassword.equals("false")) {
//                    addActionError(getText("errors.passwordIsNotAlphanumeric"));
//                }
//                if(!model.getUs_password().equals(getConfirmPassword()) ){
//                    addActionError(getText("errors.passwordNotMatch"));
//
//                }
//            }
        } else {
            getRequiredParam().put("us_user_id", "user.id");
            getRequiredParam().put("us_user_name", "user.fullName");
            getRequiredParam().put("us_email", "user.emailAddress");
            getRequiredParam().put("us_division", "user.division");
            getRequiredParam().put("ptk_office_name", "user.ptkOffice");
        }
    }

    public String processInsert() {
        try {
            if (checkSessionTimeOut()) {
                return SystemConstants.ACTION_Status.EXPIRED;
            }
            setPageSubTitle_("Add");
            model.set_operation("insert");
            specificValidation(model.get_operation());
            if (getActionErrors().size() > 0) {
                return SystemConstants.ACTION_Status.INSERT_FAIL;
            }
            getModel().updatableColumns = new String[]{"Us_id", "Us_user_id", "Us_user_name","Us_email","Us_division", "us_status","us_ptk_office"};
            serviceFactory.getInternalUserService().insert(model);
            addActionMessage(getText("createSuccess"));
            return "new";
        } catch (Exception e) {
            if (e.getCause() != null) {
                addActionError(e.getCause().getMessage());
            } else {
                addActionError(e.getMessage());
            }
            new LogFunction().logError(this.getClass(), "", e);
            return SystemConstants.ACTION_Status.INSERT_FAIL;
//        } finally {
//            closeSession();
        }
    }


    public String loadEditPage() {
        try {
            setPageSubTitle_("Edit");
            model = super.processEdit(getModel());
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
//        } finally {
//            super.closeSession();
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String processUpdate() {
        try {
            if (checkSessionTimeOut()) {
                return SystemConstants.ACTION_Status.EXPIRED;
            }
            setPageSubTitle_("Edit");
            model.set_operation("update");
            specificValidation(model.get_operation());
            if (getActionErrors().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
            getModel().updatableColumns = new String[]{"Us_id", "Us_user_id", "Us_user_name", "Us_email", "Us_status", "us_ldap"};
            serviceFactory.getInternalUserService().update(model);
            addActionMessage(getText("updateSuccess"));
            return SUCCESS;
        } catch (Exception e) {
            if (e.getCause() != null) {
                addActionError(e.getCause().getMessage());
            } else {
                addActionError(e.getMessage());
            }
            new LogFunction().logError(this.getClass(), "", e);
            return SystemConstants.ACTION_Status.UPDATE_FAIL;
//        } finally {
//            closeSession();
        }
    }

    public String delete() {
        try {
            userDAO.groupDeleteUser(getSelected(), model);
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
            returnStr = SystemConstants.ACTION_Status.DELETE_FAIL;
        } finally {
            userDAO.closeSession();
        }
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("deleteSuccess"));
        } else {
            addActionError(getText("deleteFail"));
        }
        return returnStr;
        //return groupDelete(getSelected(), getModel());
    }

    public void setStatusList(List<Options> statusList) {
        this.statusList = statusList;
    }

    public String getActivationCode() {
        String temp = new CommonFunction().encData(model.getID());
        return temp;
    }
    public String changeLocale() {
        if (!Validator.isEmpty(((HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST)).getParameter("request_locale"))) {
            return SUCCESS;
        }
        return "changeLocale";
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public List getLdapList() {
        return CommonList.getLdapOptions();
    }
    public void setLdapList(List ldapList) {
        this.ldapList = ldapList;
    }
    
    public List<Options> getStatusList() {
        if (statusList == null) {
            statusList = new ArrayList();
            statusList.add(new Options("Y", getText("active")));
            statusList.add(new Options("N", getText("inactive")));
            statusList.add(new Options("L", getText("locked")));
        }
        return statusList;
    }
    
    private String remove_gu_id = null;

    public void setRemove_gu_id(String remove_gu_id) {
        this.remove_gu_id = remove_gu_id;
    }
    
    public String processRemove() {
        setId(model.getID());
        try {
            BaseDAO dao = baseDAO;
            dao.frameworkDelete(new String[]{remove_gu_id}, GroupUser.class);
            for (int idx = model.getGroupUserList().size()-1; idx>=0; idx--) {
                if (model.getGroupUserList().get(idx).getID().equals(remove_gu_id)) {
                    GroupUser gu = model.getGroupUserList().remove(idx);
                    addActionMessage(getText("user.removedFromGroup", new String[]{gu.getUserGroup().getGroup_name()}));
                    break;
                }
            }
        } catch (BaseException e) {
            addActionError(e.getMessage());
        } catch (Exception e) {
            addActionError("errors.unexpectedError");
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }
}
