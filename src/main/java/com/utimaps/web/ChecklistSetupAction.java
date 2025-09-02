/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.LogFunction;
import com.utimaps.model.ChecklistItemSetupModel;
import com.utimaps.model.ChecklistSetupModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author yonglai
 */
public class ChecklistSetupAction extends BaseActionSupport<ChecklistSetupModel> implements ModelDriven<ChecklistSetupModel> {
    
    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    private String[] list_selected;
    
    public ChecklistSetupAction() {
        model = new ChecklistSetupModel();
    }
    
    @Override
    public ChecklistSetupModel getModel() {
        return model;
    }
    
    public String loadMainPage() {
        setPageTitle_("Checklist Setup");
        
        return "load_main_page";
    }
    
    @Override
    public String loadEditPage() {
        Debug.printDebug("loadEditPage");
        try {
            setPageTitle_("Checklist Setup");
            setPageSubTitle_("Edit");
            model = super.processEdit(getModel());
            Debug.printDebug("checlistItem Size" + model.getChecklistItemList().size());
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "ChecklistSetupAction", "ChecklistSetupAction", "loadEditPage");
            e.printStackTrace();
        }
        
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }
    
    private List<ChecklistSetupModel> listResult = new ArrayList();
    public List<ChecklistSetupModel> getListResult() {
        listResult = baseDAO.list_order(null, ChecklistSetupModel.class, "order by created_date desc");
        Debug.printDebug("listResult " + listResult);
        return listResult;
    }
    
    public String loadAddPage() {
        setPageTitle_("Checklist Setup");
        setPageSubTitle_("Add");
        try {
            // You Code Here
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "ChecklistSetupAction", "ChecklistSetupAction", "loadAddPage");
        } finally {
//            closeSession();
        }
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }
    
    public String processAddListItem() {
        
        try {
            setPageTitle_("Checklist Setup");
            if (getModel().getChecklist_id() != null && !getModel().getChecklist_id().equals("")) {
                setPageSubTitle_("Edit");
            }else{
                setPageSubTitle_("Add");
            }
            model.getChecklistItemList().add(new ChecklistItemSetupModel());
            if (getModel().getChecklist_id() != null && !getModel().getChecklist_id().equals("")) {
                return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
            }
            
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "ChecklistSetupAction", "ChecklistSetupAction", "processAddListItem");
        } finally {
//            closeSession();
        }
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }
    
    public String processInsert() {
        BaseDAO checklistDAO = new BaseDAOImpl();
        try {
            validateRequired();
            specificValidation("insert");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.INSERT_FAIL;
            }
            
            checklistDAO.setSession(baseDAO.getSession());
            
            Debug.printDebug("model " + model);
            Debug.printDebug("model " + model.getChecklist_status());
            Debug.printDebug("model " + model.getProcess_type());
            
            if (model.getChecklist_status().equals("Y")) {
                List<ChecklistSetupModel> checklistList = new ArrayList();
                Map paramD = new HashMap();
                paramD.put("process_type", model.getProcess_type());
                
                checklistList = checklistDAO.list(paramD, ChecklistSetupModel.class);
                Debug.printDebug("changing status of old checklist to N" + checklistList);
                if(checklistList != null) {
                    for(ChecklistSetupModel oldchecklist : checklistList) {
                        Debug.printDebug("checklist id :: " + oldchecklist.getID());
                        oldchecklist.setChecklist_status("N");
                        oldchecklist.set_operation(ChecklistSetupModel.OPERATION.UPDATE_STATUS);
                        checklistDAO.directUpdate(oldchecklist);
                    }
                    
                }
            }
            
            serviceFactory.getChecklistSetupService().insert(getModel());
            addActionMessage(getText("createSuccess"));

        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "ChecklistSetupAction", "ChecklistSetupAction", "processInsert");
            e.printStackTrace();
            addActionMessage(getText("createFail"));
            return SystemConstants.ACTION_Status.INSERT_FAIL;
        } finally {
            checklistDAO.closeSession();
        }
        return returnStr;
    }
    
    public String processUpdate() {
        try {
            
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
            model.set_operation(ChecklistSetupModel.OPERATION.PROCESS_UPDATE);

            //Arine::30/6/21 set update the checklist status to N for old version
            if (model.getChecklist_status().equals("Y")) {
                List<ChecklistSetupModel> checklistList = new ArrayList();
                Map paramD = new HashMap();
                paramD.put("process_type", model.getProcess_type());
                BaseDAO checklistDAO = new BaseDAOImpl();
                checklistList = checklistDAO.list(paramD, ChecklistSetupModel.class);
                
                for(ChecklistSetupModel oldchecklist : checklistList) {
                    oldchecklist.setChecklist_status("N");
                    checklistDAO.directUpdate(oldchecklist);

                }
                checklistDAO.closeSession();
                model.setChecklist_status("Y");
            }
            serviceFactory.getChecklistSetupService().update(getModel());
            addActionMessage(getText("updateSuccess"));
//        uncomment the lines below to catch BaseException
//        } catch (BaseException be){
//            addActionError(be.getMessage());
//            return SystemConstants.ACTION_Status.INSERT_FAIL;
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "ChecklistSetupAction", "ChecklistSetupAction", "processUpdate");
            e.printStackTrace();
//            return SystemConstants.ACTION_Status.INSERT_FAIL;
                return "edit_fail";
        } finally {
//            closeSession();
        }

        return returnStr;
    }
    
    public List<Options> getProcessTypeList() {
        return commList.getProcessTypeList();
    }
    
    public List getStatusOptionList() {
        return commList.getStatusOption();
    }
    
    public List<Options> getDataTypeList() {
        return commList.getDataTypeList();
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
            //applicationDAO.delete(getSelected());
            serviceFactory.getChecklistSetupService().groupDelete(getSelected(), getModel());
        } catch (Exception ex) {
            CommonFunction.writeLogFile(ex.getStackTrace(), "ChecklistSetupAction", "ChecklistSetupAction", "delete");
            returnStr = SystemConstants.ACTION_Status.DELETE_FAIL;
        }
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("deleteSuccess"));
        } else {
            //addActionError(getText("deleteFail"));
            setRedirectMessage(getText("deleteFail"));
        }
        return returnStr;
    }
    
    public String processDeleteChecklistItems() {
        setPageTitle_("Checklist Setup");
        if (getModel().getChecklist_id() != null && !getModel().getChecklist_id().equals("")) {
            setPageSubTitle_("Edit");
        }else{
            setPageSubTitle_("Add");
        }
        
        ChecklistItemSetupModel item;
        
        Debug.printDebug("list_selected " + list_selected);

        for(int i = list_selected.length; i > 0 ; i--) {
            item = model.getChecklistItemList().remove(Integer.parseInt(list_selected[i-1]));
            
            if(item.getID() != null) {
                if(Validator.isEmpty(model.get_deletedItem())) {
                    model.set_deletedItem("" + item.getID());
                } else {
                    model.set_deletedItem(model.get_deletedItem() + "," + item.getID());
                }
            }
        }
        
        try {
            if(!Validator.isEmpty(model.get_deletedItem())) {
                getModel().set_operation(ChecklistSetupModel.OPERATION.PROCESS_DELETE_LIST);
                serviceFactory.getChecklistSetupService().manualUpdate(model);
            }
        } catch (Exception e) {
            addActionError(e.getMessage());
            CommonFunction.writeLogFile(e.getStackTrace(), "ChecklistSetupAction", "ChecklistSetupAction", "processDeleteChecklistItems");
        }
        
        if (getModel().getChecklist_id() != null && !getModel().getChecklist_id().equals("")) {
            return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
        }
        
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    public String[] getList_selected() {
        return list_selected;
    }

    public void setList_selected(String[] list_selected) {
        this.list_selected = list_selected;
    }
    
    
}
