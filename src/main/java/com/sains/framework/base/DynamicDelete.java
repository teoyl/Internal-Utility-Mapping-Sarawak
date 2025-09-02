package com.sains.framework.base;

import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.web.DynamicAction;
import com.sains.framework.model.Module;
import com.sains.framework.sam.dao.ModuleDAO;
import com.sains.framework.sam.dao.ModuleDAOImpl;

public class DynamicDelete{
    public String delete_Module_dynamic(DynamicAction dynamicAction, String[] selected){
        ModuleDAO moduleDAO = new ModuleDAOImpl();
        if (selected == null) {
            dynamicAction.setRedirectMessage(dynamicAction.getText("deleteFail"));
            return SystemConstants.ACTION_Status.DELETE_FAIL;
        }
        Module dbModule;
        for (String deleteId : selected) {
            if (deleteId.equalsIgnoreCase("false")) {
                dynamicAction.setRedirectMessage(dynamicAction.getText("deleteFail"));
                return SystemConstants.ACTION_Status.DELETE_FAIL;
            }
            dbModule = moduleDAO.getModelById(deleteId, Module.class);
            //if the module is referred by other sub module.
            if (dbModule.getChildModule().size() > 0) {
                dynamicAction.setRedirectMessage(dynamicAction.getText("module.deleteFail.referedByModule", new String[]{dbModule.getModule_code()}));
            } else if (dbModule.getAttachedApplication().size() > 0) {
                dynamicAction.setRedirectMessage(dynamicAction.getText("module.deleteFail.referedByApp", new String[]{dbModule.getModule_code()}));
            }
            if (!Validator.isEmpty(dynamicAction.getRedirectMessage())) {
                return SystemConstants.ACTION_Status.DELETE_FAIL;
            }
        }

        try {
            moduleDAO.groupDelete(selected, new Module());
        } catch (Exception e){
            new LogFunction().logError(this.getClass(), "", e);
            dynamicAction.setRedirectMessage(dynamicAction.getText("deleteFail"));
            return SystemConstants.ACTION_Status.DELETE_FAIL;
        } finally {
            moduleDAO.closeSession();
        }
        dynamicAction.setRedirectMessage(dynamicAction.getText("deleteSuccess"));
        return DynamicAction.SUCCESS;
    }
}

