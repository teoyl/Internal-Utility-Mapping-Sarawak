package com.sains.framework.sam.web;

import com.opensymphony.xwork2.ActionContext;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.LogFunction;
import java.util.ArrayList;
import java.util.List;
import com.sains.common.util.Validator;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.Debug;
import com.sains.framework.lookup.DataRetriever;
import com.sains.framework.model.Application;
import com.sains.framework.model.ApplicationRights;
import com.sains.framework.model.GroupApplication;
import com.sains.framework.model.GroupApplicationRights;
import com.sains.framework.model.SetupGroup;
import com.sample.ParentModel;
import java.sql.PreparedStatement;
import java.util.Map;
import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

public class ApplicationAction extends BaseActionSupport<Application> implements ModelDriven<Application> {

    private static final long serialVersionUID = -6659925652584240535L;
    private final String DELETED_RIGHTS_LIST = "deletedRights";
    private final String DYNAMIC_ACTION_USERGROUP = "UserGroupToApplication";
//	private Application model = new Application();
    private List<Application> applicationList = new ArrayList<Application>();
    private List<Options> applicationTypeOption = null;
    private String module_code;
    private String module_desc;
    private String[] rights_selected;
    private String[] app_rights_id;
    private String[] app_rights_code;
    private String[] app_rights_description;
    private String[] app_rights_methods;
    private String curGroupId_ = "";
    private String[] userGroupToApplication_selected = null;  // ThoTH @ 23-Apr-2015

    public ApplicationAction() {
        setPageTitle_("Application");
        model = new Application();
        getRequiredParam().put("application_code", "application.code");
        getRequiredParam().put("application_name", "application.name");
        getRequiredParam().put("action_name", "application.actionName");
        getRequiredParam().put("action_class", "application.actionClass");
        getRequiredParam().put("attached_module_id", "application.attachedTo");
    }

    @Override
    public Application getModel() {
        return model;
    }

    public void specificValidation(String validationType) {
//        if (!Validator.isEmpty(model.getApplication_code())) {
//            //Duplication check. (application_id = "" means it is a new record.
//            if (model.getApplication_id().equals("")) {
//                if (applicationDAO.getApplicationByCode(model.getApplication_code(), model) != null) {
//                    addActionError(getText("field.duplicated", new String[]{getText("application.code"), model.getApplication_code()}));
//                }
//            } else { //existing record.
//                if (applicationDAO.getApplicationByCode(model.getApplication_code(), model) != null
//                    && !(applicationDAO.getApplicationByCode(model.getApplication_code(), model).getApplication_id().equals(model.getApplication_id()))) {
//                    addActionError(getText("field.duplicated", new String[]{getText("application.code"), model.getApplication_code()}));
//                }
//            }
//        }

        int index = 1, sameCount;
        boolean hasError = false;
        for (ApplicationRights appRights : model.getRightsList()) {
            if (Validator.isEmpty(appRights.getApp_rights_code())) {
                addActionError(getText("errors.itemized", new String[]{"" + index, getText("field.required", new String[]{getText("application.rightsCode")})}));
//				addActionError(getText("errors.itemized", ""+index, getText("errors.required", "<s:text name='application.rightsCode' />")));
                hasError = true;
            }
            if (Validator.isEmpty(appRights.getApp_rights_description())) {
                addActionError(getText("errors.itemized", new String[]{"" + index, getText("field.required", new String[]{getText("application.rightDesc")})}));
//				addActionError(getText("errors.itemized", ""+index, getText("errors.required", "<s:text name='application.rightDesc' />")));
                hasError = true;
            }
            if (Validator.isEmpty(appRights.getApp_rights_methods())) {
                addActionError(getText("errors.itemized", new String[]{"" + index, getText("field.required", new String[]{getText("application.methodInvoked")})}));
//				addActionError(getText("errors.itemized", ""+index, getText("errors.required", "<s:text name='application.methodInvoked' />")));
                hasError = true;
            }

            if (hasError) {
                break;
            } else {
                sameCount = 0;
                for (ApplicationRights appRights2 : model.getRightsList()) {
                    if (appRights2.getApp_rights_code().equalsIgnoreCase(appRights.getApp_rights_code())) {
                        sameCount++;
                    }
                    if (sameCount >= 2) {
                        addActionError(getText("field.duplicated", new String[]{getText("application.rightsCode"), appRights2.getApp_rights_code()}));
                        break;
                    }
                }
            }
            index++;
        }
    }

//    @Override
    public String loadAddPage() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        System.out.println("search_abp_aName = " + request.getParameter("search_abp_aName"));
        return super.loadAddPage();
    }

    public String processInsert() {
        try {
            specificValidation("insert");
            if (getActionErrors().size() > 0) {
                return SystemConstants.ACTION_Status.INSERT_FAIL;
            }
            
            System.out.println("getModel().getAttached_module_id() " + getModel().getAttached_module_id());
//                defaultAddProperties(getModel());
//                getModel().setRightsList(getAppRightsList());
//                for (ApplicationRights appRights : getModel().getRightsList()){
//                    defaultAddProperties(appRights);
//                }
            //applicationDAO.create(getModel());
            serviceFactory.getApplicationService().insert(getModel());
        } catch (Exception ex) {
            ex.printStackTrace();
            new LogFunction().logError(this.getClass(), "", ex);
            returnStr = SystemConstants.ACTION_Status.INSERT_FAIL;
        }// finally {
        //applicationDAO.closeSession();
        //}
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("createSuccess"));
        } else {
            addActionError(getText("createFail"));
        }
        return returnStr;
    }

    public String loadEditPage() {
        try {
        setPageSubTitle_("Edit");
            model = super.processEdit(getModel());
//            if (model != null) {
//                setModule_code(model.getAttachedModule().getModule_code());
//                setModule_desc(model.getAttachedModule().getModule_name());
//            }
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
        }
//                super.populateLastVisit("Edit Application - " + model.getApplication_name());
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String processUpdate() {
        try {
            setPageSubTitle_("Edit");
            specificValidation("update");
            if (getActionErrors().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
            serviceFactory.getApplicationService().update(getModel());
//            baseDAO.update(getModel());
/*            com.sains.framework.base.BaseDAOImpl dao = new BaseDAOImpl();
            dao.setSession(baseDAO.getSession());
            ParentModel parent = (ParentModel)dao.getModelById("1561012992253U83APK0", ParentModel.class);
            parent.setParent_name(parent.getParent_name() + " | updated");
            parent.setParent_age(parent.getParent_age() + 1);
            dao.getSession().evict(parent);
            ParentModel.updateCurrentRequestObject(parent.getClass()+"__operation", "update_parent_age");
            dao.update(parent);
*/
/*
//            ParentModel.updateCurrentRequestObject(parent.getClass()+"__operation", "update_parent_name");
            com.sains.framework.base.BaseDAOImpl dao = new BaseDAOImpl();
            dao.setSession(baseDAO.getSession());
            dao.setIsPaging(Boolean.TRUE);
//            dao.setMaxSize(10);
            dao.setMaxSize(12000);
            List<com.sains.framework.model.AuditTrailModel> list = dao.list_order(null, com.sains.framework.model.AuditTrailModel.class, "order by audit_id desc");
                try {
                    System.out.println("Start : " + DateUtil.getCurrentTimestamp() + ", size = "+list.size());
                    dao.beginBatchTransaction();
                    int count = 0;
//                    PreparedStatement ps = dao.getpStmt("update t_audit_trail set date_time = ? where audit_id = ?");
            for (com.sains.framework.model.AuditTrailModel audit : list) {
                dao.getSession().evict(audit);
//                if (count++ > 100) {
//                    count=0;
//                    dao.getSession().flush();
//                }
                java.sql.Timestamp current = DateUtil.getCurrentTimestamp();
                current.setTime((audit.getDate_time().getTime()+1000));
                audit.setDate_time(current);
                String[] updatableColumns = new String[] {"Date_time"};
//                ps.setTimestamp(1, audit.getDate_time());
//                ps.setInt(2, audit.getAudit_id());
//                ps.addBatch();
//                audit.sqlUpdateModel(updatableColumns, dao, Boolean.FALSE);
                dao.updateWithSession(dao.getSession(), audit);
//                new String[] {"Auditlogin_id","Logintype","Loginstatus","Loginip","Logintime","Loginid"}
            }
//            ps.executeBatch();
                    dao.commitBatchTransaction();
                    System.out.println("End : " + DateUtil.getCurrentTimestamp());
                } catch (Exception e) {
                    e.printStackTrace();
                    dao.rollbackBatchTransaction();
                }
*/
//            returnStr = super.processUpdate(model);
//                System.out.println("after model.getCreated_by = " + model.getCreated_by());
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
            returnStr = SystemConstants.ACTION_Status.UPDATE_FAIL;
        } finally {
            closeSession();
        }
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("updateSuccess"));
        } else {
            addActionError(getText("updateFail"));
        }
        return SUCCESS;
    }

//	private void removeList(){
//		ActionContext.getContext().getSession().remove(DELETED_RIGHTS_LIST);
//	}
//    public List<ApplicationRights> getDeletedRightsList() {
//        List deletedRightsList = (List) ActionContext.getContext().getSession().get(DELETED_RIGHTS_LIST);
//        if (deletedRightsList == null) {
//            deletedRightsList = new ArrayList();
//            ActionContext.getContext().getSession().put(DELETED_RIGHTS_LIST, deletedRightsList);
//        }
//
//        return deletedRightsList;
//    }

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
            serviceFactory.getApplicationService().groupDelete(getSelected(), getModel());
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
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

//	private void copyRightsToList(){
//		int index = 0;
//		if (app_rights_code != null) {
//			for (String code: app_rights_code){
//				ApplicationRights ar = new ApplicationRights();
//				ar.setApp_rights_code(code);
//				ar.setApp_rights_description(app_rights_description[index]);
//				ar.setApp_rights_id(app_rights_id[index]);
//				ar.setApp_rights_methods(app_rights_methods[index]);
//				index++;
//				appRightsList.add(ar);
//			}
//		}
//	}
    public String processAddRights() {
//		copyRightsToList();
        model.getRightsList().add(new ApplicationRights());
        if (getModel().getApplication_id() != null && !getModel().getApplication_id().equals("")) {
            setPageSubTitle_("Edit");
            return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
        }
        setPageSubTitle_("Add");
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    public String processDeleteRights() {
        ApplicationRights item = null;

        for (int i = rights_selected.length; i>0; i--) {
            item = model.getRightsList().remove(Integer.parseInt(rights_selected[i-1]));
            if (item.getID() != null) {
                if (Validator.isEmpty(model.get_deletedItem())) {
                    model.set_deletedItem(""+item.getID());
                } else {
                    model.set_deletedItem(model.get_deletedItem()+","+item.getID());
                }
            }
        }
        try {
            if (!Validator.isEmpty(model.get_deletedItem())){
                getModel().set_operation("processDeleteRights");
                serviceFactory.getApplicationService().delete(getModel().getID(), getModel());
            }
        } catch (BaseException be) {
            addActionMessage(be.getMessage());
        } catch (Exception e) {
            addActionError(e.getMessage());
        }

        if (getModel().getApplication_id() != null && !getModel().getApplication_id().equals("")) {
            setPageSubTitle_("Edit");
            return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
        }
        setPageSubTitle_("Add");
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    public List<Application> getApplicationList() {
        return applicationList;
    }

    public void setApplicationList(List<Application> userList) {
        this.applicationList = userList;
    }

    public List<Options> getApplicationTypeOption() {
        if (applicationTypeOption == null) {
            applicationTypeOption = new ArrayList();
            applicationTypeOption.add(new Options("A", getText("application")));
            applicationTypeOption.add(new Options("R", getText("report")));
        }
        return applicationTypeOption;
    }

    public void setApplicationTypeOption(List<Options> applicationTypeOption) {
        this.applicationTypeOption = applicationTypeOption;
    }

    public String getModule_code() {
        return module_code;
    }

    public void setModule_code(String moduleCode) {
        module_code = moduleCode;
    }

    public String getModule_desc() {
        return module_desc;
    }

    public void setModule_desc(String moduleDesc) {
        module_desc = moduleDesc;
    }

    public String[] getApp_rights_code() {
        return app_rights_code;
    }

    public void setApp_rights_code(String[] appRightsCode) {
        app_rights_code = appRightsCode;
    }

    public String[] getApp_rights_description() {
        return app_rights_description;
    }

    public void setApp_rights_description(String[] appRightsDescription) {
        app_rights_description = appRightsDescription;
    }

    public String[] getApp_rights_methods() {
        return app_rights_methods;
    }

    public void setApp_rights_methods(String[] appRightsMehtods) {
        app_rights_methods = appRightsMehtods;
    }

    public String[] getApp_rights_id() {
        return app_rights_id;
    }

    public void setApp_rights_id(String[] appRightsId) {
        app_rights_id = appRightsId;
    }

    public String[] getRights_selected() {
        return rights_selected;
    }

    public void setRights_selected(String[] rightsSelected) {
        rights_selected = rightsSelected;
    }
    
    public List getSystemTypeOption_() {
        return getCommList().getSystemTypeOption();
    }
    
    public String assignToGroup() {
        try {
            setPageSubTitle_("Edit");
            model = baseDAO.getModelById(model.getID(), Application.class);

            for (GroupApplication ga : model.getGroupAppList()) {
                for (GroupApplicationRights gaRights : ga.getGroupAppRights()) {
                    for (ApplicationRights appRights : model.getRightsList()) {
                        if (gaRights.getApp_rights_id().equals(appRights.getID())) {
                            gaRights.setApplicationRights(appRights);
                            break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }

        return "appToGroup";
    }
    
    public String loadAddGroup() {
        //List appList = getApplicationList();
        populateDynamicActionSetup(DYNAMIC_ACTION_USERGROUP);
        setList_param(DYNAMIC_ACTION_USERGROUP);
        ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_USERGROUP)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_SEARCH_DESCRIPTION, getSearchDescription());
        ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_USERGROUP)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_SEARCH_ACTION_TYPE, "actionType.search");

        // ThoTH @ 22-Apr-2015 : Avoid Duplicate Application
        curGroupId_ = "";
        for (GroupApplication groupApp : model.getGroupAppList()) {
            if (curGroupId_.length() > 0) {
                curGroupId_ += ",";
            }
            curGroupId_ += groupApp.getGroup().getID();
        }
        setPageSubTitle_("Edit");
        return "load_add_group_page";
    }

    public String processSearchGroup() {
        try {
            Application app = (Application) getObjectFromSession(model);
            if (app != null) {
                model = app;
            }
            setPageSubTitle_("Edit");
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            int index = 0;
            populateDynamicActionSetup(DYNAMIC_ACTION_USERGROUP);
            setList_param(DYNAMIC_ACTION_USERGROUP);
            //Map setupMap = getDynamicActionSetup(DYNAMIC_ACTION_APPLICATION);
            Map searchParam = getDynamicSearchParam(DYNAMIC_ACTION_USERGROUP);
            //add any extra parameter here if necessary.
            String extraCondition = "";
            // ThoTH @ 22-Apr-2015
            for (String str : getCurGroupId_().split(",")) {
                if (extraCondition.equals("")) {
                    extraCondition = str;
                } else {
                    extraCondition += "|" + str;
                }
            }
            if (!extraCondition.equals("")) {
                // to filter those already selected applications
                searchParam.put("userGroup.ug_id", "!IN " + extraCondition);
            }
            Map setupMap = getDynamicActionSetup(DYNAMIC_ACTION_USERGROUP);
            this.setPaging(true);
            new DataRetriever().retrieveDataDynamic(this, DYNAMIC_ACTION_USERGROUP, setupMap,
                    searchParam, true, getDynamicColumns(DYNAMIC_ACTION_USERGROUP), getDynamicResult(DYNAMIC_ACTION_USERGROUP),
                    request, request.getSession().getServletContext());
            ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_USERGROUP)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO, setupMap.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO));
            ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_USERGROUP)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_SIZE, setupMap.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_SIZE));
            ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_USERGROUP)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO_OF_ROWS, setupMap.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO_OF_ROWS));
            ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_USERGROUP)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGING_URL, "processSearchGroupApplication");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            new LogFunction().logError(this.getClass(), "", e);
        }
        return "load_add_group_page";
    }

    //when application added from Edit Page.
    public String addGroupEdit() {
        //just need to call the addApplication method, but return page set to end with "_edit";
        addGroup();
        setPageSubTitle_("Edit");
        return "appToGroup";
//        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String addGroup() {
        try {
            setPageSubTitle_("Add");
            //List moduleList = new ArrayList();
            populateDynamicActionSetup(DYNAMIC_ACTION_USERGROUP);
            setList_param(DYNAMIC_ACTION_USERGROUP);
            if (getUserGroupToApplication_selected() != null) {
                List groupAppList = model.getGroupAppList();
                GroupApplication groupApp = null;
                SetupGroup group = null;
                for (String ugId : getUserGroupToApplication_selected()) {
                    Debug.printDebug(ugId);
                    if (!containsGroup(ugId, groupAppList)) {
                        groupApp = new GroupApplication();
//                        applicationDAO.setSession(baseDAO.getSession());
                        group = (SetupGroup) baseDAO.getObjectById(ugId, SetupGroup.class);
                        //preload the application rights.
//                        group.getRightsList().size();
                        //preload the application rights DONE
                        groupApp.setGroup(group);
                        groupApp.setUg_id(ugId);

                        groupApp.setGroupAppRights(new ArrayList());
                        for (ApplicationRights appRights : model.getRightsList()) {
//                            appRights.setGaRights_id(appRights.getID());
//                            System.out.println("################### " + appRights.getID() );
                            GroupApplicationRights gaRights = new GroupApplicationRights();
                            gaRights.setApp_rights_id(appRights.getID());
                            gaRights.setApplicationRights(appRights);

                            groupApp.getGroupAppRights().add(gaRights);
                        }

                        groupAppList.add(groupApp);

                        //Module module = groupApp.getGroupApplication().getAttachedModule();
                        //getModules(module, moduleList);
                    }
                }
            }

        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
        }

        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    private boolean containsGroup(String ug_id, List<GroupApplication> appList) {
        if (appList != null) {
            for (GroupApplication groupApp : appList) {
                if (groupApp.getUg_id().equals(ug_id)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public String processUpdateGroup() {
//        returnStr = SUCCESS;
        try {
            setPageSubTitle_("Edit");
//            validateRequired();
//            specificValidation("update");
//            if (getActionErrors().size() > 0) {
//                addActionError(getText("updateFail"));
//                return "appToGroup";
//            }

            serviceFactory.getApplicationService().updateGroup(getModel());
            addActionMessage(getText("updateSuccess"));

        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
            addActionError(getText("updateFail"));
            return "appToGroup";
        } finally {
//            userGroupDAO.closeSession();
        }
        return loadEditPage();
    }

    public String processDeleteGroup() {
        try {
            if (getSelected() != null) {
                serviceFactory.getApplicationService().deleteGroup(getSelected(), new GroupApplication());
                addActionMessage(getText("deleteSuccess"));
            }
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
            addActionError(getText("deleteFail"));
            return "appToGroup";
        }
        return loadEditPage();
    }
    
    public String getCurGroupId_() {
        return curGroupId_;
    }

    public void setCurGroupId_(String curGroupId_) {
        this.curGroupId_ = curGroupId_;
    }
    
    public String[] getUserGroupToApplication_selected() {
        return userGroupToApplication_selected;
    }

    public void setUserGroupToApplication_selected(String[] userGroupToApplication_selected) {
        this.userGroupToApplication_selected = userGroupToApplication_selected;
    }
    
    public String cancelAddToGroup() {
        return loadEditPage();
    }
}
