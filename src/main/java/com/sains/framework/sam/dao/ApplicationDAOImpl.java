package com.sains.framework.sam.dao;

import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.model.RightMethod;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.Debug;
import com.sains.framework.model.Application;
import com.sains.framework.model.ApplicationRights;
import com.sains.framework.model.GroupApplication;
import com.sains.framework.model.GroupApplicationRights;

public class ApplicationDAOImpl extends BaseDAOImpl<Application> implements ApplicationDAO {

    @Override
    public synchronized void insert(Application application) throws Exception {
        try {
            beginBatchTransaction();
            application.defaultAddProperties();
            autoDuplicationCheck(application, InsertUpdate.INSERT);
            getSession().save(application);
            for (ApplicationRights appRights : application.getRightsList()) {
                appRights.defaultAddProperties();
                appRights.setApplication_id(application.getApplication_id());
                getSession().save(appRights);
                String[] methodArr = appRights.getApp_rights_methods().split(",");
                addRightMethods(methodArr, appRights);
            }
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }

    private void addRightMethods(String[] methodArr, ApplicationRights appRights){
        appRights.setRightMethodList(new ArrayList());
        if (methodArr != null && methodArr.length > 0) {
            for (String method : methodArr) {
                RightMethod rightMethod = new RightMethod();
                rightMethod.setApp_rights_id(appRights.getID());
                rightMethod.defaultAddProperties();
                rightMethod.setMethod_str(method.trim());
                getSession().save(rightMethod);
            }
        }
    }

    @Override
    public synchronized Application update(Application model) throws Exception {
        try {
            autoDuplicationCheck(model, InsertUpdate.UPDATE);
            model.defaultUpdateProperties();
            beginBatchTransaction();
//            BaseDAOImpl baseDAO = new BaseDAOImpl();
//            baseDAO.setSession(getSession());
            Application dbApplication = setUpdateProperties(model, getSession());
            if (dbApplication != null) {
                dbApplication.setRightsList(model.getRightsList());
                model = dbApplication;
            }
            getSession().update(model);
            for (ApplicationRights appRights : model.getRightsList()) {
                if (Validator.isEmpty(appRights.getID())) {
                    //baseDAO.autoDuplicationCheck(appRights, InsertUpdate.INSERT); // no need duplication check in this case
                    appRights.defaultAddProperties();
                    appRights.setApplication_id(model.getID());
                    String[] methodArr = appRights.getApp_rights_methods().split(",");
                    addRightMethods(methodArr, appRights);
                    getSession().save(appRights);
                } else {
                    //baseDAO.autoDuplicationCheck(appRights, InsertUpdate.UPDATE); // no need duplication check in this case
                    appRights.defaultUpdateProperties();
                    String[] methodArr = appRights.getApp_rights_methods().split(",");
                    appRights = (ApplicationRights) setObjectUpdateProperties(appRights, getSession());
                    appRights.setApplication_id(model.getID());
                    for (RightMethod rightMethod : appRights.getRightMethodList()) {
                        getSession().delete(rightMethod);
                    }
                    addRightMethods(methodArr, appRights);
                    getSession().update(appRights);
                }
            }
//                    //need to get the deleted items' id
//                    if (model.get_deletedItem() != null) {
//                        ApplicationRights newItemInstance = new ApplicationRights(); //to create only 1 ApplicationRights instance
//                        ApplicationRights item = null;
//                        List deletingList = new ArrayList();
//                        if (!Validator.isEmpty(model.get_deletedItem())) {
//                            for (String itemId : model.get_deletedItem().split(",")) {
//                                item = (ApplicationRights) baseDAO.getModelById(itemId, newItemInstance);
//                                if (item != null) {
//                                    deletingList.add(item);
//                                }
//                            }
//                            deleteItems(deletingList);
//                        }
//                    }

//            for (ApplicationRights appRights : deletedList) {
//                getSession().refresh(appRights);
//                for (GroupApplicationRights gaRight : appRights.getGroupApplicationRightsList()) {
//                    getSession().delete(gaRight);
//                }
//                getSession().delete(appRights);
//            }
//            Application app = (Application) getSession().get(Application.class, model.getID());
//            System.out.println("application name = " + app.getApplication_name());

            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            throw e;
        } finally {
            closeSession();
        }
        return model;
    }

    @Override
    public synchronized void delete(String deletedIds, Application model) throws Exception {
        if (model.get_operation().equals("processDeleteRights")) {
            try {
                beginBatchTransaction();
                String[] strArr = model.get_deletedItem().split(",");
                Application app = getModelById(model.getID(), Application.class);
                List deletingList = new ArrayList();
                for (String id : strArr) {
                    for (ApplicationRights appRights : app.getRightsList()) {
                        if (appRights.getID().equals(id)) {
                            Debug.printDebug("id = " + id);
                            //System.out.println("appRights.getGroupApplicationRightsList().size() = " + appRights.getGroupApplicationRightsList().size());
                            if (appRights.getRightMethodList() != null && appRights.getRightMethodList().size() > 0) {
                                for (RightMethod method : appRights.getRightMethodList()) {

                                    Debug.printDebug("deleting the method..."+method.getID()+" = " + method.getMethod_str());
                                    getSession().delete(method);
                                }
                                getSession().flush();
                            }
                            deletingList.add(appRights);
                        }
                    }
                }
                if (deletingList.size() > 0) {
                    deleteItems(deletingList);
                    commitBatchTransaction();
                }
                rollbackBatchTransaction();
            } catch (Exception e) {
                rollbackBatchTransaction();
            } finally {
                closeSession();
            }
        } else {
            super.delete(deletedIds, model);
        }
    }

//    @Override
//    public synchronized void groupDelete(String[] ids, Application application) throws Exception {
//        beginBatchTransaction();
//        try {
//
//        } catch (Exception e) {
//            rollbackBatchTransaction();
//            throw e;
//        } finally {
//            closeSession();
//        }
//    }

    public List getAdminApplications() {
        Map param = new HashMap();
        param.put("system_app", "Y");
        return list(param, Application.class);
    }

    public Application getApplicationByCode(String applicationCode, Application application) {
        Map param = new HashMap();
        param.put("application_code", applicationCode);
        List applicationList = list(param, Application.class, false);
        if (applicationList != null && applicationList.size() > 0) {
            return (Application) applicationList.get(0);
        }
        return null;
    }

    public Application getApplicationByActionClass(String actiocClass, Application application) {
        Map param = new HashMap();
        param.put("action_class", actiocClass);
        List applicationList = list(param, Application.class, false);
        if (applicationList != null && applicationList.size() > 0) {
            return (Application) applicationList.get(0);
        }
        return null;
    }
    
    @Override
    public synchronized Application updateGroup(Application model) throws Exception {
        try {
            beginBatchTransaction();
            BaseDAOImpl baseDAO = new BaseDAOImpl();
            baseDAO.setSession(getSession());
            
            Debug.printDebug("model.getRightsList().size(): " + model.getRightsList().size());

            for (GroupApplication groupApp : model.getGroupAppList()) {
                if (Validator.isEmpty(groupApp.getID())) {
                    groupApp.defaultAddProperties();
                    groupApp.setUg_id(groupApp.getGroup().getID());
                    groupApp.setApplication_id(model.getID());
                    getSession().save(groupApp);
                    if (model.getRightsList() != null && model.getRightsList().size() > 0) {
                        for (GroupApplicationRights grAppRights : groupApp.getGroupAppRights()) {
//                        for (ApplicationRights appRights : model.getRightsList()) {
                            GroupApplicationRights gaRights = new GroupApplicationRights();
                            gaRights.defaultAddProperties();
                            gaRights.setUg_app_id(groupApp.getID());
                            gaRights.setApp_rights_id(grAppRights.getApp_rights_id());
                            gaRights.setHasRight(grAppRights.getHasRight());
                            getSession().save(gaRights);
                        }
                    }
                } else {
                    groupApp.defaultUpdateProperties();
                    groupApp.setUg_id(groupApp.getGroup().getID());
                    groupApp.setApplication_id(model.getID());

                    if (model.getRightsList() != null && model.getRightsList().size() > 0) {
                        for (GroupApplicationRights grAppRights : groupApp.getGroupAppRights()) {
//                            System.out.println("#####################");
//                            System.out.println(grAppRights.getApplicationRights().getGaRights_id());
//                            System.out.println(grAppRights.getApplicationRights().getGaRightChecked());
//                        for (ApplicationRights appRights : model.getRightsList()) {
                            if (Validator.isEmpty(grAppRights.getID())) {
                                Debug.printDebug("new");
                                GroupApplicationRights gaRights = new GroupApplicationRights();
                                gaRights.defaultAddProperties();
                                gaRights.setUg_app_id(groupApp.getID());
                                gaRights.setApp_rights_id(grAppRights.getApp_rights_id());
                                gaRights.setHasRight(grAppRights.getHasRight());
                                getSession().save(gaRights);
                            } else {
                                Debug.printDebug("exist");
                                GroupApplicationRights gaRights = (GroupApplicationRights)baseDAO.getModelById(grAppRights.getID(), GroupApplicationRights.class);
                                Debug.printDebug("@@@@@@@@@ $$$$" + gaRights.getHasRight());
                                gaRights.defaultUpdateProperties();
                                gaRights.setHasRight(grAppRights.getHasRight());
                                Debug.printDebug("@@@@@@@@@ ####" + gaRights.getHasRight());
                                getSession().update(gaRights);
                            }
                        }
                    }
                    groupApp = (GroupApplication) baseDAO.setUpdateProperties(groupApp, getSession());
                    getSession().update(groupApp);
                }
            }

//            model = setUpdateProperties(model, getSession());
//            getSession().update(model);
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            throw e;
        } finally {
            closeSession();
        }
        return model;
    }
    
    @Override
    public synchronized void deleteGroup(String[] ids, GroupApplication groupApp) throws Exception {
        beginBatchTransaction();
        try {
            String strUpdatedBy = (String) ActionContext.getContext().getSession().get("loginId");  // For Audit
//            String strOper = ((ModelBase)model).get_operation();  // ThoTH @ 26-Jul-2013
            for (String deleteId : ids) {
                groupApp = (GroupApplication) getObjectById(deleteId, GroupApplication.class);
                
                // Delete Group App Rights
                for (GroupApplicationRights gaRights : groupApp.getGroupAppRights()) {
                    auditDelete(gaRights, strUpdatedBy);  // For Audit
                }
                
                auditDelete(groupApp, strUpdatedBy);  // For Audit
            }
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }
}
