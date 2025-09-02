package com.sains.framework.sam.web;

import com.SysConf;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.LogFunction;
import com.sains.framework.model.EmailQueueModel;
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
import com.sains.common.util.ClsMail;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;

public class EmailQueueAction extends BaseActionSupport<EmailQueueModel> implements ModelDriven<EmailQueueModel> {

    private static final long serialVersionUID = -6659925652584240539L;

    public EmailQueueAction() {
        // change the DAO to the correct service.
        //baseDAO = new BaseDAOImpl();
        model = new EmailQueueModel();
        // set the required field for common check.
        //getRequiredParam().put("column_name1", "column.name1");
        //getRequiredParam().put("column_name2", "column.name2");


    }

    @Override // change the "String" and return value to the
    public EmailQueueModel getModel() {
        return model;
    }

    public void specificValidation(String validationType) {
    }

    public String processInsert() {
        try {
            validateRequired();
            specificValidation("insert");
            if (getActionErrors().size() > 0) {
                return SystemConstants.ACTION_Status.INSERT_FAIL;
            }

            //serviceFactory.getXXXService().insert(getModel());
            addActionMessage(getText("createSuccess"));
//        uncomment the lines below to catch BaseException
//        } catch (BaseException be){
//            addActionError(be.getMessage());
//            return SystemConstants.ACTION_Status.INSERT_FAIL;
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
            addActionMessage(getText("createFail"));
            return SystemConstants.ACTION_Status.INSERT_FAIL;
//        } finally {
//            closeSession();
        }
        return returnStr;
    }

    public String loadAddPage() {
        try {
            // You Code Here
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);

//        } finally {
//            closeSession();
        }

        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    //retrieve data for editing.
    public String loadEditPage() {
        try {
            model = super.processEdit(getModel());

        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);

//        } finally {
//            closeSession();
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String processUpdate() {
        try {
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
            returnStr = super.processUpdate(model);
            //serviceFactory.getXXXService().update(getModel());
            addActionMessage(getText("updateSuccess"));
//        uncomment the lines below to catch BaseException
//        } catch (BaseException be){
//            addActionError(be.getMessage());
//            return SystemConstants.ACTION_Status.INSERT_FAIL;
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
            addActionError(getText("updateFail"));
            return SystemConstants.ACTION_Status.INSERT_FAIL;
//        } finally {
//            closeSession();
        }

        return returnStr;
    }

    public String delete() {
        if (getSelected() != null) {
            if (getSelected().length == 1) {
                super.delete(getSelected()[0], getModel());
            } else {
                super.groupDelete(getSelected(), model);
            }
        }
        return SUCCESS;
    }
    
    public String resend() {
        try {
            EmailQueueModel emailQueue = baseDAO.getModelById(getId(), EmailQueueModel.class);
            ClsMail mail = new ClsMail();
            mail.setSendFrom(emailQueue.getEmail_sender());
            if (!Validator.isEmpty(emailQueue.getBcc())) {
                mail.setBcc(emailQueue.getBcc());
            } else {
                mail.setBcc("");
            }
            if (Validator.isEmpty(emailQueue.getSmtp()) ) {
                mail.setOutMailServer(SysConf.get("smtp.default"));
                mail.setInMailServer(SysConf.get("smtp.default"));
            } else {
                mail.setOutMailServer(emailQueue.getSmtp());
                mail.setInMailServer(emailQueue.getSmtp());
            }


            mail.setTo(emailQueue.getRecipient());
            if (!Validator.isEmpty(emailQueue.getCc())) {
                mail.setCc(emailQueue.getCc());
            } else {
                mail.setCc("");
            }

            mail.setSubject(emailQueue.getEmail_subject());
            mail.setMailType("H"); // HTML Style;

//            mail.setBody(emailQueue.getEmail_content());
//            if (emailQueue.getAttachmentList() != null && !emailQueue.getAttachmentList().isEmpty()) {
//                mail.setAttfilename(emailQueue.getAttachmentList());
//            }
            mail.sendMail();
            if (emailQueue.getSend_status() != null && emailQueue.getSend_status().equals("Y")) {
                emailQueue.setResend_date(DateUtil.getCurrentTimestamp());
            } else {
                emailQueue.setSent_date(DateUtil.getCurrentTimestamp());
                emailQueue.setSend_status("Y");
            }
            try {
                baseDAO.beginBatchTransaction();
                baseDAO.getSession().update(emailQueue);
                baseDAO.commitBatchTransaction();
            } catch (Exception e) {
                baseDAO.rollbackBatchTransaction();
            }
            r = "rmsg;"+getText("emailSent");
        } catch (Exception e) {
            e.printStackTrace();
            r = "rmsg;"+getText("emailFail");
        }
        return "divSubmitForm";
    }
}
