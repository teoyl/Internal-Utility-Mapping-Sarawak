package com.sains.framework.sam.web;

import com.sains.framework.base.BaseException;
import com.sains.framework.base.LogFunction;
import com.sains.framework.model.NullModel;
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

public class NullAction extends BaseActionSupport<NullModel> implements ModelDriven<NullModel> {

    private static final long serialVersionUID = -6659925652584240539L;

    public NullAction() {
        // change the DAO to the correct service.
        //baseDAO = new BaseDAOImpl();
        model = new NullModel();
        // set the required field for common check.
        //getRequiredParam().put("column_name1", "column.name1");
        //getRequiredParam().put("column_name2", "column.name2");


    }

    @Override // change the "String" and return value to the
    public NullModel getModel() {
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
}
