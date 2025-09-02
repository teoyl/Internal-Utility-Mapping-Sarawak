package com.sains.framework.sam.web;

import com.opensymphony.xwork2.ActionContext;
import com.sains.framework.base.LogFunction;
import com.sains.framework.model.ErrLogModel;

import com.sains.common.util.Validator;
import com.sains.common.util.DateUtil;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.ClsMail;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.model.ErrLogModel;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

public class ErrLogAction extends BaseActionSupport<ErrLogModel> implements ModelDriven<ErrLogModel> {

    private static final long serialVersionUID = -6659925652584240539L;

    public ErrLogAction() {
        // change the DAO to the correct service.
        //baseDAO = new BaseDAOImpl();
        model = new ErrLogModel();
        // set the required field for common check.
        //getRequiredParam().put("column_name1", "column.name1");
        //getRequiredParam().put("column_name2", "column.name2");


    }

    @Override // change the "String" and return value to the
    public ErrLogModel getModel() {
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

    private String[] dcRecordIds;
    public String[] getDcRecordIds() {
        return dcRecordIds;
    }
    public void setDcRecordIds(String[] dcRecordIds) {
        this.dcRecordIds = dcRecordIds;
    }
    
    
    
    public String dynamicUpdate() {
        if (dcRecordIds != null && dcRecordIds.length > 0) {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            ErrLogModel errLog = new ErrLogModel();
            String[] updatingColumn = new String[] {"solution_remark"};
            try {
                baseDAO.beginBatchTransaction();
                for (String recordId : dcRecordIds) {
                    System.out.println("request.getParameter(\"solution_remark\"+recordId) = " + request.getParameter("solution_remark"+recordId));
                    errLog.setID(recordId);
                    errLog.setSolution_remark(request.getParameter("solution_remark"+recordId));
                    errLog.sqlUpdateModel(updatingColumn, baseDAO, Boolean.FALSE);
                }
                baseDAO.commitBatchTransaction();
                r = "rmsg;"+getText("updateSuccess");
            } catch (Exception e) {
                baseDAO.rollbackBatchTransaction();
                r = "rmsg;"+getText("updateFail");
            }
        } else {
            r = "rmsg;"+getText("updateNoRecord");
        }
        return "divSubmitForm";
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
    
}
