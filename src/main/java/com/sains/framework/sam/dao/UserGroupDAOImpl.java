package com.sains.framework.sam.dao;

import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.Validator;
import com.sains.framework.model.ApplicationRights;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.model.GroupApplication;
import com.sains.framework.model.GroupApplicationRights;
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.User;
import java.lang.reflect.Method;
import org.hibernate.Query;

public class UserGroupDAOImpl extends BaseDAOImpl<SetupGroup> implements UserGroupDAO {

    @Override
    public synchronized void insert(SetupGroup model) throws Exception {
        try {
            beginBatchTransaction();
            model.defaultAddProperties();
            getSession().save(model);
            for (GroupApplication groupApp : model.getGroupApplication()) {
                groupApp.defaultAddProperties();
                groupApp.setUg_id(model.getID());
                groupApp.setApplication_id(groupApp.getApplication().getApplication_id());
                getSession().save(groupApp);
                if (groupApp.getApplication().getRightsList() != null && groupApp.getApplication().getRightsList().size() > 0) {
                    for (ApplicationRights appRights : groupApp.getApplication().getRightsList()) {
                        GroupApplicationRights gaRights = new GroupApplicationRights();
                        gaRights.defaultAddProperties();
                        gaRights.setUg_app_id(groupApp.getID());
                        if (Validator.isEmpty(appRights.getGaRights_id())) {
                            gaRights.setApp_rights_id(appRights.getID());
                        } else {
                            gaRights.setApp_rights_id(appRights.getGaRights_id());
                        }
                        gaRights.setHasRight(appRights.getGaRightChecked());
                        getSession().save(gaRights);
                    }
                }
            }
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }

    public synchronized SetupGroup update(SetupGroup model) throws Exception {
        try {
            autoDuplicationCheck(model, InsertUpdate.UPDATE);
            model.defaultUpdateProperties();
            beginBatchTransaction();
            BaseDAOImpl baseDAO = new BaseDAOImpl();
            baseDAO.setSession(getSession());

//            for (GroupApplication item : model.getGroupApplication()) {
//                if (Validator.isEmpty(item.getID())) {
//                    //baseDAO.autoDuplicationCheck(appRights, InsertUpdate.INSERT); // no need duplication check in this case
//                    BaseActionSupport.defaultAddProperties(item);
//                    item.setApplication_id(model.getID());
//                    getSession().save(item);
//                } else {
//                    //baseDAO.autoDuplicationCheck(appRights, InsertUpdate.UPDATE); // no need duplication check in this case
//                    BaseActionSupport.defaultUpdateProperties(item);
//                    item = (GroupApplication) setObjectUpdateProperties(item, getSession());
//                    item.setApplication_id(model.getID());
//                    getSession().update(item);
//                }
//            }
            for (GroupApplication groupApp : model.getGroupApplication()) {
                if (Validator.isEmpty(groupApp.getID())) {
                    groupApp.defaultAddProperties();
                    groupApp.setUg_id(model.getID());
                    groupApp.setApplication_id(groupApp.getApplication().getApplication_id());
                    getSession().save(groupApp);
                    if (groupApp.getApplication().getRightsList() != null && groupApp.getApplication().getRightsList().size() > 0) {
                        for (ApplicationRights appRights : groupApp.getApplication().getRightsList()) {
                            GroupApplicationRights gaRights = new GroupApplicationRights();
                            gaRights.defaultAddProperties();
                            gaRights.setUg_app_id(groupApp.getID());
                            if (Validator.isEmpty(appRights.getGaRights_id())) {
                                gaRights.setApp_rights_id(appRights.getID());
                            } else {
                                gaRights.setApp_rights_id(appRights.getGaRights_id());
                            }
                            gaRights.setHasRight(appRights.getGaRightChecked());
                            getSession().save(gaRights);
                        }
                    }
                } else {
                    groupApp.defaultUpdateProperties();
                    groupApp.setUg_id(model.getID());
                    groupApp.setApplication_id(groupApp.getApplication().getApplication_id());

                    if (groupApp.getApplication().getRightsList() != null && groupApp.getApplication().getRightsList().size() > 0) {
                        for (ApplicationRights appRights : groupApp.getApplication().getRightsList()) {
                            if (Validator.isEmpty(appRights.getGaRights_id())) {
                                GroupApplicationRights gaRights = new GroupApplicationRights();
                                gaRights.defaultAddProperties();
                                gaRights.setUg_app_id(groupApp.getID());
                                if (Validator.isEmpty(appRights.getGaRights_id())) {
                                    gaRights.setApp_rights_id(appRights.getID());
                                } else {
                                    gaRights.setApp_rights_id(appRights.getGaRights_id());
                                }
                                gaRights.setHasRight(appRights.getGaRightChecked());
                                getSession().save(gaRights);
                            } else {
                                GroupApplicationRights gaRights = (GroupApplicationRights)baseDAO.getModelById(appRights.getGaRights_id(), GroupApplicationRights.class);
                                gaRights.defaultUpdateProperties();
                                gaRights.setHasRight(appRights.getGaRightChecked());
                                getSession().update(gaRights);
                            }
                        }
                    }
                    groupApp = (GroupApplication) baseDAO.setUpdateProperties(groupApp, getSession());
                    getSession().update(groupApp);
                }
            }

            model = setUpdateProperties(model, getSession());
            getSession().update(model);
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            throw e;
        }finally{
            closeSession();
        }
        return model;
    }

    @Override
    public synchronized void create(SetupGroup userGroup) throws Exception {
        Transaction tx = getSession().beginTransaction();
        try {

            getSession().save(userGroup);
            for (Object obj : userGroup.getGroupApplication()) {
                GroupApplication groupApp = (GroupApplication) obj;
                groupApp.setUg_id(userGroup.getUg_id());
                groupApp.setID(CommonFunction.getId(20));
                getSession().save(groupApp);
                if (groupApp.getGroupAppRights() != null) {
                    for (GroupApplicationRights gaRights : groupApp.getGroupAppRights()) {
                        gaRights.setUg_app_id(groupApp.getUg_app_id());
                        getSession().save(gaRights);
                    }
                }
            }
            for (Object obj : userGroup.getGroupUser()) {
                GroupUser groupUser = (GroupUser) obj;
                groupUser.setUg_id(userGroup.getUg_id());
                groupUser.setID(CommonFunction.getId(20));
                getSession().save(groupUser);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            closeSession();
        }
    }

    @Override
    public synchronized void update(SetupGroup userGroup, List<GroupApplication> deletedGroupApplication, List<GroupUser> deletedGroupUser) throws Exception {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        BaseDAOImpl<GroupApplication> groupRightDAO = new BaseDAOImpl();
        groupRightDAO.setSession(session);
        try {
            SetupGroup ug = getModelById(userGroup.getUg_id(), SetupGroup.class);
            ug.setGroup_code(userGroup.getGroup_code());
            ug.setGroup_name(userGroup.getGroup_name());
            ug.setUpdated_by(userGroup.getUpdated_by());
            ug.setUpdated_date(userGroup.getUpdated_date());
            session.saveOrUpdate(ug);
            for (Object obj : userGroup.getGroupApplication()) {
                GroupApplication groupApp = (GroupApplication) obj;
                if (groupApp.getUg_app_id() != null && !groupApp.getUg_app_id().equals("")) { //
                    groupApp = new BaseDAOImpl<GroupApplication>().setUpdateProperties((GroupApplication) obj, session);
                } else { // new Group Application
                    groupApp.setUg_id(ug.getUg_id());
                    groupApp.setID(CommonFunction.getId(20));
                }
                session.saveOrUpdate(groupApp);
                if (((GroupApplication) obj).getGroupAppRights() != null) {
                    for (GroupApplicationRights gaRights : ((GroupApplication) obj).getGroupAppRights()) {
                        gaRights.setUg_app_id(groupApp.getUg_app_id());
                        session.saveOrUpdate(gaRights);
                    }
                }
            }
            for (GroupUser groupUser : userGroup.getGroupUser()) {
                groupUser.setUg_id(userGroup.getUg_id());
                if (groupUser.getID().equals("")) {
                    groupUser.setID(CommonFunction.getId(20));
                }
                session.saveOrUpdate(groupUser);
            }
            for (GroupApplication ga : deletedGroupApplication) {
                GroupApplication delGA = groupRightDAO.getModelById(ga.getUg_app_id(), GroupApplication.class);
                //delGA.setUg_app_id(ga.getUg_app_id());
                session.delete(delGA);
                    for (GroupApplicationRights right : delGA.getGroupAppRights()){
                    session.delete(right);
                }
            }
            for (GroupUser gu : deletedGroupUser) {
                session.delete(gu);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public synchronized void groupDelete(String[] deletedUserGroupList, SetupGroup setupGroup) throws Exception {
        try {
            beginBatchTransaction();
            SetupGroup userGroup = null;
            for (String groupId : deletedUserGroupList) {
                userGroup = getModelById(groupId, SetupGroup.class);
                if (userGroup != null) {
                    if (userGroup.getGroupApplication() != null) {
                        for (GroupApplication groupApp : userGroup.getGroupApplication()) {
                            if (groupApp.getGroupAppRights() != null) {
                                for (GroupApplicationRights gAppRight : groupApp.getGroupAppRights()) {
                                    getSession().delete(gAppRight);
                                }
                            }
                            getSession().delete(groupApp);
                        }
                    }
                    if (userGroup.getGroupUser() != null) {
                        for (GroupUser groupUser : userGroup.getGroupUser()) {
                            getSession().delete(groupUser);
                        }
                    }
                    getSession().delete(userGroup);
                }
            }
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }

    public SetupGroup getUserGroupByCode(String groupCode, SetupGroup userGroup) {
        Map param = new HashMap();
        param.put("group_code", groupCode);
        List list = list(param, SetupGroup.class, false);
        if (list != null && list.size() > 0) {
            return (SetupGroup) list.get(0);
        }
        return null;
    }

    @Override
    public synchronized void addGroupUser(SetupGroup group, String[] userIds) throws Exception {
        try {
            beginBatchTransaction();
            for (String id : userIds) {
                GroupUser gu = new GroupUser();
                Method m = gu.getClass().getMethod("getUs_id");
                if (m.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                    m = gu.getClass().getMethod("setUs_id", Integer.class);
                    m.invoke(gu, new Integer(id));
                } else {
                    m = gu.getClass().getMethod("setUs_id", String.class);
                    m.invoke(gu, id);
                } 
                gu.setUg_id(group.getID());
                gu.defaultAddProperties();
                getSession().save(gu);
            }
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            throw e;
        }
    }

    @Override
    public synchronized void deleteGroupUser(SetupGroup group, String[] userIds) throws Exception {
        try {
            beginBatchTransaction();
            String strUpdatedBy = (String) ActionContext.getContext().getSession().get("loginId");  // For Audit
            for (String id : userIds) {
                GroupUser gu = (GroupUser)getObjectById(id, GroupUser.class);
                auditDelete(gu, strUpdatedBy);
            }
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            throw e;
        }
    }

    @Override
    public synchronized void delete(String deletedIds, SetupGroup model) throws Exception {
        if (model.get_operation().equals("processDeleteApplication")) {
            try {
                beginBatchTransaction();
                String[] strArr = model.get_deletedItem().split(",");
                SetupGroup app = getModelById(model.getID(), SetupGroup.class);
                List deletingList = new ArrayList();
                for (String id : strArr) {
                    for (GroupApplication groupApp : app.getGroupApplication()) {
                        if (groupApp.getID().equals(id)) {
                            deletingList.add(groupApp);
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
    public List<SetupGroup> getWorkflowGroupByUserId(String userId) {
        String stmt = "select distinct setupGroup from SetupGroup setupGroup "
                + "join setupGroup.groupUser groupUser "
                + "join groupUser.groupUser user "
                + "where setupGroup.group_type = 'W' and user.us_user_id = :us_user_id ";
        Query query = null;
        query = getSession().createQuery(stmt);
        query.setString("us_user_id", userId);
        
        return query.list();
    }
    
    public synchronized Map getGroupInfo(String groupCode, String userIdInGroup, Session session) throws Exception {
        setSession(session);
        Map jsonMap = new HashMap();
        List userList = new ArrayList();
        String stmt = "select distinct user from SetupGroup setupGroup "
                + "join setupGroup.groupUser groupUser "
                + "join groupUser.groupUser user "
                + "where setupGroup.group_code "+ ((groupCode.indexOf(",")>0)?"in (:group_code)":"= :group_code ");
//                + "where setupGroup.group_type = 'W' and setupGroup.group_code "+ ((groupCode.indexOf(",")>0)?"in (:group_code)":"= :group_code ");
        if (!Validator.isEmpty(userIdInGroup)) {
            stmt += " and user.us_user_id = :us_user_id";
        }
        
        stmt += " order by user";
        System.out.println("stmt ===== " + stmt);
        Query query = getSession().createQuery(stmt);
        if (groupCode.indexOf(",")>0) {
            query.setParameterList("group_code", groupCode.split(","));
        } else {
            query.setString("group_code", groupCode);
        }
        if (!Validator.isEmpty(userIdInGroup)) {
            query.setString("us_user_id", userIdInGroup);
        }
        
        List tempList = query.list();
        if (tempList.isEmpty()) {
            jsonMap.put("status", "fail");
            jsonMap.put("user", userList);
        } else {
            for (User user : (List<User>)tempList) {
                Map dataMap = new HashMap();
                dataMap.put("email", user.getUs_email());
                dataMap.put("userName", user.getUs_user_name());
                dataMap.put("userId", user.getUs_user_id());
                userList.add(dataMap);
            }
            jsonMap.put("status", "success");
            jsonMap.put("user", userList);
        }
        return jsonMap;
    }
    
    public List<SetupGroup> getWorkflowGroupByUserId(String userId, String sysId) {
        String stmt = "select distinct setupGroup from SetupGroup setupGroup "
                + "join setupGroup.groupUser groupUser "
                + "join groupUser.groupUser user "
//                + "where user.us_user_id = :us_user_id ";
                + "where user.us_user_id = :us_user_id and (setupGroup.system_id = :system_id or setupGroup.system_id is null)";
//                + "where setupGroup.group_type = 'W' and user.us_user_id = :us_user_id ";
        Query query = null;
        query = getSession().createQuery(stmt);
        query.setString("us_user_id", userId);
        query.setString("system_id", sysId);
        
        return query.list();
    }
}
