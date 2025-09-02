package com.sains.framework.sam.web;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.LogFunction;
import com.sains.framework.sam.dao.UserGroupDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.sains.common.util.Validator;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.CommonComparator;
import com.sains.common.util.CriteriaConverter;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.sam.dao.UserGroupDAOImpl;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CommonList;
import com.sains.framework.base.Debug;
import com.sains.framework.lookup.DataRetriever;
import com.sains.framework.model.User;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.model.GroupApplication;
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.Application;
import com.sains.framework.model.ApplicationRights;
import com.sains.framework.model.GroupApplicationRights;
import java.net.URLEncoder;
import java.util.Collections;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public class UserGroupAction extends BaseActionSupport<SetupGroup> implements ModelDriven<SetupGroup> {

    private static final long serialVersionUID = -6659925652584240539L;
    private final String APP_LIST = "applicationList", USER_LIST = "userList", DELETED_APP_LIST = "deletedApplicationList", DELETED_USER_LIST = "deletedUserList";
    private final String DYNAMIC_ACTION_APPLICATION = "Application";
    private final String DYNAMIC_ACTION_USER = "User";
    private final String ADDITIONAL_RIGHTS_LIST = "AdditionalRightsList";
//    private SetupGroup userGroup = new SetupGroup();
    private UserGroupDAO userGroupDAO = new UserGroupDAOImpl();
    private BaseDAO<User> userDAO = new BaseDAOImpl<User>();
    private BaseDAO<Application> applicationDAO = new BaseDAOImpl<Application>();
    private String[] applicationSelected = null;
    private String[] userSelected = null;
    private List<String> accessRight_retrieveList = null;
    private String sortURL_ = "";
    private String sort_ = "id";
    private String order_ = "A";

    private void removeList() {
        ActionContext.getContext().getSession().remove(APP_LIST);
        ActionContext.getContext().getSession().remove(DELETED_APP_LIST);
        ActionContext.getContext().getSession().remove(USER_LIST);
        ActionContext.getContext().getSession().remove(DELETED_USER_LIST);
        ActionContext.getContext().getSession().remove(ADDITIONAL_RIGHTS_LIST);
        setAction("UserGroup");
    }

    public void setAdditionalRightsList(List list) {
        if (list == null) {
            list = new ArrayList();
        }
        ActionContext.getContext().getSession().remove(ADDITIONAL_RIGHTS_LIST);
        ActionContext.getContext().getSession().put(ADDITIONAL_RIGHTS_LIST, list);
    }

    public List<String> getAdditionalRightsList() {
        List list = (List) ActionContext.getContext().getSession().get(ADDITIONAL_RIGHTS_LIST);
        if (list == null) {
            list = new ArrayList();
            setAdditionalRightsList(list);
        }

        return list;
    }

    public List<GroupApplication> getApplicationList() {
        List applicationList = (List) ActionContext.getContext().getSession().get(APP_LIST);
        if (applicationList == null) {
            applicationList = new ArrayList();
        }

        return applicationList;
    }

    public List<GroupUser> getUserList() {
        List userList = (List) ActionContext.getContext().getSession().get(USER_LIST);
        if (userList == null) {
            userList = new ArrayList();
        }

        return userList;
    }

    public List getDeletedApplicationList() {
        List deletedAppList = (List) ActionContext.getContext().getSession().get(DELETED_APP_LIST);
        if (deletedAppList == null) {
            deletedAppList = new ArrayList();
            ActionContext.getContext().getSession().put(DELETED_APP_LIST, deletedAppList);
        }

        return deletedAppList;
    }

    public List getDeletedUserList() {
        List deletedUserList = (List) ActionContext.getContext().getSession().get(DELETED_USER_LIST);
        if (deletedUserList == null) {
            deletedUserList = new ArrayList();
            ActionContext.getContext().getSession().put(DELETED_USER_LIST, deletedUserList);
        }

        return deletedUserList;
    }

    public void setApplicationList(List applicationList) {
        if (applicationList == null) {
            applicationList = new ArrayList();
        }
        ActionContext.getContext().getSession().remove(APP_LIST);
        ActionContext.getContext().getSession().put(APP_LIST, applicationList);
    }

    public void setUserList(List userList) {
        if (userList == null) {
            userList = new ArrayList();
        }
        
        System.out.println("userList " + userList);
        ActionContext.getContext().getSession().remove(USER_LIST);
        ActionContext.getContext().getSession().put(USER_LIST, userList);
    }

    public UserGroupAction() {
        // set the required field for common check.
        getRequiredParam().put("group_code", "group.code");
        getRequiredParam().put("group_name", "group.name");
        model = new SetupGroup();
        /*dynamicSearchApplicationField.add("application_code");
         dynamicSearchApplicationField.add("application_name");
         dynamicSearchApplicationLabel.add("application.code");
         dynamicSearchApplicationLabel.add("application.name");*/
    }

    @Override // change the "String" and return value to the
    public SetupGroup getModel() {
        return model;
    }

    public void specificValidation(String validationType) {
        if (getApplicationList().size() <= 0) {
            //decide to add min. application checking or not.
            //addActionError("Please add at least 1 application");
        }
//        if (!Validator.isEmpty(getModel().getGroup_code())) {
//            for (String duplicateName : userGroupDAO.checkDuplicateFields(new String[]{"group_code", "group_name"}, getModel(), false)){
//                if (duplicateName.equals("group_code")){
//                    addActionError(getText("field.duplicated", new String[]{getText("group.code"), getModel().getGroup_code()}));
//                } else if (duplicateName.equals("group_name")){
//                    addActionError(getText("field.duplicated", new String[]{getText("group.name"), getModel().getGroup_name()}));
//                }
//            }
//            //Duplication check. (ug_id = "" means it is a new record.
//            /*if (getModel().getUg_id().equals("")) {
//                if (userGroupDAO.getUserGroupByCode(getModel().getGroup_code(), getModel()) != null) {
//                    addActionError(getText("field.duplicated", new String[]{getText("group.code"), getModel().getGroup_code()}));
//                }
//            } else { //existing record.
//                if (userGroupDAO.getUserGroupByCode(getModel().getGroup_code(), getModel()) != null
//                    && !userGroupDAO.getUserGroupByCode(getModel().getGroup_code(), getModel()).getUg_id().equals(getModel().getUg_id())) {
//                    addActionError(getText("field.duplicated", new String[]{getText("group.code"), getModel().getGroup_code()}));
//                }
//            }*/
//        }
    }

    public String processInsert() {
        try {
            populateCheckBoxValue();
            validateRequired();
            specificValidation("insert");
            if (getActionErrors().size() > 0) {
                return SystemConstants.ACTION_Status.INSERT_FAIL;
            }
            //List applicationList = new ArrayList();
//            defaultAddProperties(getModel());
//            boolean foundGARight = false;
//            String rightsName = "";
//            for (GroupApplication groupAppObj : getApplicationList()) {
//                defaultAddProperties(groupAppObj);
//                rightsName = groupAppObj.getApplication().getApplication_code() + "_";
//                for (ApplicationRights appRights : groupAppObj.getApplication().getRightsList()) {
//                    foundGARight = false;
//                    if (groupAppObj.getGroupAppRights() != null) {
//                        for (GroupApplicationRights groupAppRights : groupAppObj.getGroupAppRights()) {
//                            if (groupAppRights.getApp_rights_id().equals(appRights.getApp_rights_id())) {
//                                if (getAdditionalRightsList().contains(rightsName + appRights.getApp_rights_code())) {
//                                    groupAppRights.setHasRight("Y");
//                                } else {
//                                    groupAppRights.setHasRight("N");
//                                }
//                                defaultUpdateProperties(groupAppRights);
//                                foundGARight = true;
//                                break;
//                            }
//                        }
//                    } else {
//                        groupAppObj.setGroupAppRights(new ArrayList());
//                    }
//                    if (!foundGARight) {
//                        GroupApplicationRights gaRights = new GroupApplicationRights();
//                        gaRights.setApp_rights_id(appRights.getApp_rights_id());
//                        gaRights.setUg_app_id(groupAppObj.getUg_app_id());
//                        defaultAddProperties(gaRights);
//                        if (getAdditionalRightsList().contains(rightsName + appRights.getApp_rights_code())) {
//                            gaRights.setHasRight("Y");
//                        } else {
//                            gaRights.setHasRight("N");
//                        }
//                        groupAppObj.getGroupAppRights().add(gaRights);
//                    }
//                }
//            }
//            //getModel().setGroupApplication(applicationList);
//            getModel().setGroupApplication(getApplicationList());
//            getModel().setGroupUser(getUserList());
            serviceFactory.getInternalUserGroupService().insert(getModel());
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
            returnStr = SystemConstants.ACTION_Status.INSERT_FAIL;
        }
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("createSuccess"));
        } else {
            addActionError(getText("createFail"));
        }
        return SUCCESS;
    }

    public String loadAddPage() {
        //clear the items in application listing if any.
        setPageTitle_(getText("userGroup"));
        setPageSubTitle_(getText("button.add"));
        removeList();

        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    public String goEditPage() {
        setPageTitle_(getText("userGroup"));
        setPageSubTitle_(getText("button.update"));
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    // ThoTH @ 25-Sep-2014 :: WF Group :: Start
    List wfGroupList_ = new ArrayList();

    public List getWfGroupList_() {
        return wfGroupList_;
    }

    public void setWfGroupList_(List wfGroupList_) {
        this.wfGroupList_ = wfGroupList_;
    }

    private String ug_search_user_id;
    public String getUg_search_user_id() {
        return ug_search_user_id;
    }
    public void setUg_search_user_id(String ug_search_user_id) {
        this.ug_search_user_id = ug_search_user_id;
    }

    private String ug_search_user_name;
    public String getUg_search_user_name() {
        return ug_search_user_name;
    }
    public void setUg_search_user_name(String ug_search_user_name) {
        this.ug_search_user_name = ug_search_user_name;
    }
    
    public String processEditUser() throws Exception {
        setPageTitle_(getText("userGroup"));
        setPageSubTitle_(getText("actionType.searchUser"));
        Map param = new HashMap();
        BaseDAOImpl userListDAO = new BaseDAOImpl();
        userListDAO.setSession(baseDAO.getSession());
        userListDAO.setIsPaging(Boolean.TRUE);
        userListDAO.setMaxSize(15);
        setPageSize(15);
        param.clear();
        param.put("ug_id", model.getUg_id());
        Long size = null;
        if (!Validator.isEmpty(ug_search_user_name) || !Validator.isEmpty(ug_search_user_id)) {
            String countSql = "select count(*) from t_setup_group_user inner join t_setup_user on t_setup_user.us_id = t_setup_group_user.us_id where ug_id = :ug_id ";
            CriteriaConverter cc = new CriteriaConverter();
            if (!Validator.isEmpty(ug_search_user_name)) {
                countSql += cc.strCriteria("and", "t_setup_user.us_user_name", ug_search_user_name);
            }
            if (!Validator.isEmpty(ug_search_user_id)) {
                StringBuffer moreConditions = new StringBuffer();
                moreConditions.append("and (");
                moreConditions.append(cc.strCriteria("", "t_setup_user.us_user_id", ug_search_user_id));
                moreConditions.append(cc.strCriteria("or", "t_setup_user.sso_login_id", ug_search_user_id));
                moreConditions.append(")");
                countSql += moreConditions.toString();
            }
            size = userListDAO.sqlCountRecord(countSql, param);
        } else {
            size = userListDAO.sqlCountRecord("select count(*) from t_setup_group_user where ug_id = :ug_id", param);
        }
        param.clear();
        param.put(SystemConstants.QUERY.JOIN, SystemConstants.QUERY.INNER_JOIN + "_self.groupUser groupUser");
        param.put(SystemConstants.CRITERIA.NO_CHANGE_LOWER + "ug_id", model.getID());
        String pagingExtraCond = "";
        if (!Validator.isEmpty(ug_search_user_name)) {
            param.put("groupUser.us_user_name", ug_search_user_name);
            pagingExtraCond += "&ug_search_user_name="+URLEncoder.encode(ug_search_user_name, "UTF-8");
        }
        if (!Validator.isEmpty(ug_search_user_id)) {
            StringBuffer moreConditions = new StringBuffer();
            moreConditions.append("(");
            CriteriaConverter cc = new CriteriaConverter();
            moreConditions.append(cc.strCriteria("", "groupUser.us_user_id", ug_search_user_id));
            moreConditions.append(cc.strCriteria("or", "groupUser.sso_login_id", ug_search_user_id));
            param.put("customSQL", moreConditions.append(")").toString());
            pagingExtraCond += "&ug_search_user_id="+URLEncoder.encode(ug_search_user_id, "UTF-8");
        }
        Debug.printDebug("size " + size);
        if (size <= 15) {
            userListDAO.setFirstResult(0);
            setPageNo(1);
        } else {
            if (getPageNo() == null) {
                setPageNo(1);
            }
            userListDAO.setFirstResult(((getPageNo() - 1) * 15));
        }
        if (size == null) {
            setNumberOfRows(0);
        } else {
            setNumberOfRows(size.intValue());
        }

//        setPagingURL("loadEditPageUserGroup?id="+model.getID()+"&action=UserGroup");
        setPagingURL("processEditUserUserGroup?ID=" + model.getID() + "&sort_=" + sort_ + "&order_=" + order_+pagingExtraCond);
        setSortURL_("processEditUserUserGroup?ID=" + model.getID()+pagingExtraCond);
        //preload the data for Group Users DONE
//        System.out.println("param = " + param);
        Debug.printDebug("sort_" + sort_);
        if (sort_.equals("id")) {
            System.out.println("sort_ " + param);
            setUserList(userListDAO.list_order(param, GroupUser.class, true, "order by groupUser.us_user_id " + (order_.equals("A") ? "asc" : "desc")));
        } else {
            setUserList(userListDAO.list_order(param, GroupUser.class, true, "order by groupUser.us_user_name " + (order_.equals("A") ? "asc" : "desc")));
        }
        
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE + "_user";
    }

    //retrieve data for editing.
    public String loadEditPage() {
        try {
            setPageTitle_(getText("userGroup"));
            setPageSubTitle_(getText("button.update"));
            model = super.processEdit(getModel());

            //Collections.sort(model.getGroupApplication(), new CommonComparator(new String[]{"getApplication.getApplication_name;desc", "getApplication.getApplication_code;desc"}));
            Collections.sort(model.getGroupApplication(), new CommonComparator(new String[]{"getApplication.getApplication_name", "getApplication.getApplication_code"}));

//            Collections.sort(model.getGroupApplication(), new CommonComparator(new String[]{"getApplication().getApplication_name"}));
//            Collections.sort(model.getGroupApplication(), new CommonComparator(new String[]{"getShow_in_main_order"}));
            for (GroupApplication ga : model.getGroupApplication()) {
                //use the existing looping to prepopulate the application rights list for each application:

                //use the existing looping to prepopulate the application rights list for each application: DONE
                for (GroupApplicationRights gaRights : ga.getGroupAppRights()) {
                    for (ApplicationRights appRights : ga.getApplication().getRightsList()) {

                        if (gaRights.getApp_rights_id().equals(appRights.getID())) {
                            appRights.setGaRights_id(gaRights.getID()); // set the Group Application Right's ID to Application Right
                            if (gaRights.getHasRight().equalsIgnoreCase("Y")) {
                                appRights.setGaRightChecked("Y");
                            }
                            break;
                        }
                    }
                }
            }
            //preload the data for Group Application
            //for (GroupApplication groupAppObj : userGroup.getGroupApplication()){
            //for (ApplicationRights appRights : groupAppObj.getApplication().getRightsList());
            //groupAppObj.getApplication().getRightsList().size();

            //for (GroupApplicationRights gaRights : groupAppObj.getGroupAppRights());
            //groupAppObj.getGroupAppRights();
            //}
            //finish preload the list needed
//            setApplicationList(userGroup.getGroupApplication());
//            for (GroupApplication ga : userGroup.getGroupApplication()) {
//                //use the existing looping to prepopulate the application rights list for each application:
//                ga.getApplication().getRightsList().size();
//                //use the existing looping to prepopulate the application rights list for each application: DONE
//                for (GroupApplicationRights gaRights : ga.getGroupAppRights()) {
//                    if (gaRights.getHasRight().equalsIgnoreCase("Y")) {
//                        getAdditionalRightsList().add(gaRights.getApplicationRights().getApplication().getApplication_code()+ "_" + gaRights.getApplicationRights().getApp_rights_code());
//                    }
//                }
//            }
            //preload the data for Group Users
            //for (GroupUser gu : userGroup.getGroupUser());
//            try {
//                userGroup.getGroupUser().size();
//            } catch (Exception e) {
//                new LogFunction().logError(this.getClass(), "", e);
//                userGroup.setGroupUser(new ArrayList());
//            }
            //preload the data for Group Users DONE
//            setUserList(userGroup.getGroupUser());
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String processUpdate() {
        returnStr = SUCCESS;
        try {
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }

//            List applicationList = new ArrayList();
//            String rightsName;
//            defaultUpdateProperties(getModel());
//            boolean foundGARight;
//            for (Object obj : getApplicationList()) {
//                GroupApplication groupAppObj = (GroupApplication) obj;
//                rightsName = groupAppObj.getApplication().getApplication_code() + "_";
//
//                for (ApplicationRights appRights : groupAppObj.getApplication().getRightsList()) {
//                    foundGARight = false;
//                    if (groupAppObj.getGroupAppRights() != null) {
//                        for (GroupApplicationRights groupAppRights : groupAppObj.getGroupAppRights()) {
//                            if (groupAppRights.getApp_rights_id().equals(appRights.getApp_rights_id())) {
//                                if (getAdditionalRightsList().contains(rightsName + appRights.getApp_rights_code())) {
//                                    groupAppRights.setHasRight("Y");
//                                } else {
//                                    groupAppRights.setHasRight("N");
//                                }
//                                defaultUpdateProperties(groupAppRights);
//                                foundGARight = true;
//                                break;
//                            }
//                        }
//                    } else {
//                        groupAppObj.setGroupAppRights(new ArrayList());
//                    }
//                    if (!foundGARight) {
//                        GroupApplicationRights gaRights = new GroupApplicationRights();
//                        gaRights.setApp_rights_id(appRights.getApp_rights_id());
//                        gaRights.setUg_app_id(groupAppObj.getUg_app_id());
//                        defaultAddProperties(gaRights);
//                        if (getAdditionalRightsList().contains(rightsName + appRights.getApp_rights_code())) {
//                            gaRights.setHasRight("Y");
//                        } else {
//                            gaRights.setHasRight("N");
//                        }
//                        groupAppObj.getGroupAppRights().add(gaRights);
//                    }
//                }
//                defaultUpdateProperties(groupAppObj);
//                groupAppObj.setUg_id(getModel().getUg_id());
//                applicationList.add(groupAppObj);
//            }
//            getModel().setGroupApplication(applicationList);
//            getModel().setGroupUser(getUserList());
//            userGroupDAO.update(getModel(), getDeletedApplicationList(), getDeletedUserList());
            getModel().set_operation(SystemConstants.COMM_OPERATION.UPDATE);//ahmadni @ 19-Jul-2016
            serviceFactory.getInternalUserGroupService().update(getModel());
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
            returnStr = SystemConstants.ACTION_Status.UPDATE_FAIL;
        } finally {
            userGroupDAO.closeSession();
        }
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("updateSuccess"));
        } else {
            addActionError(getText("updateFail"));
        }
        return returnStr;
    }

    //when application added from Edit Page.
    public String addApplicationEdit() {
        //just need to call the addApplication method, but return page set to end with "_edit";
        addApplication();
        setPageTitle_(getText("userGroup"));
        setPageSubTitle_(getText("button.update"));
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }
    //when user added from Edit Page.

    public String addUserEdit() throws Exception {
        //just need to call the addApplication method, but return page set to end with "_edit";
        addUser();
        return processEditUser();
    }

    public String addApplication() {
        try {
            setPageTitle_(getText("userGroup"));
            setPageSubTitle_(getText("button.add"));
            //List moduleList = new ArrayList();
            populateDynamicActionSetup(DYNAMIC_ACTION_APPLICATION);
            setList_param(DYNAMIC_ACTION_APPLICATION);
            if (getApplication_selected() != null) {
                List appList = model.getGroupApplication();
                GroupApplication groupApp = null;
                Application app = null;
                for (String appId : getApplication_selected()) {
                    if (!containsApplication(appId, appList)) {
                        groupApp = new GroupApplication();
                        applicationDAO.setSession(baseDAO.getSession());
                        app = applicationDAO.getModelById(appId, Application.class);
                        //preload the application rights.
                        app.getRightsList().size();
                        //preload the application rights DONE
                        groupApp.setApplication(app);
                        groupApp.setApplication_id(appId);
                        appList.add(groupApp);
                        //Module module = groupApp.getGroupApplication().getAttachedModule();
                        //getModules(module, moduleList);
                    }
                }
            }

//            for (GroupApplication ga : model.getGroupApplication()) {
//                System.out.println("ga.getApplication().getApplication_code() = " + ga.getApplication().getApplication_code());
//                for (ApplicationRights rights : ga.getApplication().getRightsList()) {
//                    System.out.println("getApp_rights_code = " + rights.getApp_rights_code());
//                    System.out.println("gaRightChecked = " + rights.getGaRightChecked());
//                }
//            }

            /*int level = 1;
             for (Object obj : moduleList){
             List list = (List) obj;
             for (Object moduleObj : list){
             System.out.println(level + ". " + ((Module)moduleObj).getModule_code());
             }
             level++;
             }*/
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
        }

        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    public String addUser() {
        try {
            SetupGroup ug = (SetupGroup) getAndRemoveObjectFromSession(model);
            if (ug != null) {
                model = ug;
            }
            //List moduleList = new ArrayList();
            populateDynamicActionSetup(DYNAMIC_ACTION_USER);
            setList_param(DYNAMIC_ACTION_USER);
            List deletedUserList = getDeletedUserList();
            if (getUser_selected() != null) {
                userGroupDAO.addGroupUser(model, getUser_selected());
//                List userList = getUserList();
//                GroupUser groupUser = null;
//                User user = new User();
//                for (String userId : getUser_selected()) {
//                    if (containsUser(userId, deletedUserList)) {
//                        userList.add(removeUser(userId, deletedUserList));
//                    } else {
//                        if (!containsUser(userId, userList)) {
//                            groupUser = new GroupUser();
//                            groupUser.setGroupUser((User) userDAO.getModelById(userId, user));
//                            groupUser.setUs_id(userId);
//                            userList.add(groupUser);
//                        }
//                    }
//                }
            }

            /*int level = 1;
             for (Object obj : moduleList){
             List list = (List) obj;
             for (Object moduleObj : list){
             System.out.println(level + ". " + ((Module)moduleObj).getModule_code());
             }
             level++;
             }*/
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
            addActionError(getText("group.error.failToAddUser"));
        } finally {
            userDAO.closeSession();
            userGroupDAO.closeSession();
        }

        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    private boolean containsApplication(String application_id, List<GroupApplication> appList) {
        if (appList != null) {
            for (GroupApplication groupApp : appList) {
                if (groupApp.getApplication_id().equals(application_id)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean containsUser(String user_id, List<GroupUser> userList) {
        for (GroupUser groupUser : userList) {
            if (groupUser.getUs_id().equals(user_id)) {
                return true;
            }
        }
        return false;
    }

    private GroupApplication removeApplication(String application_id, List<GroupApplication> appList) {
        int index = 0;
        for (GroupApplication groupApp : appList) {
            if (groupApp.getApplication_id().equals(application_id)) {
                return appList.remove(index);
            }
            index++;
        }
        return null;
    }

    private GroupUser removeUser(String user_id, List<GroupUser> userList) {
        int index = 0;
        for (GroupUser groupUser : userList) {
            if (groupUser.getUs_id().equals(user_id)) {
                return userList.remove(index);
            }
            index++;
        }
        return null;
    }

    public String loadAddApplicationPage() {
        //List appList = getApplicationList();
        setPageTitle_(getText("userGroup"));
        setPageSubTitle_(getText("actionType.assignApplication"));
        populateDynamicActionSetup(DYNAMIC_ACTION_APPLICATION);
        setList_param(DYNAMIC_ACTION_APPLICATION);
        setSearchDescription((String)((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_APPLICATION)).get("searchDescription"));
        ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_APPLICATION)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_SEARCH_DESCRIPTION, getSearchDescription());
        ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_APPLICATION)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_SEARCH_ACTION_TYPE, "actionType.searchApplication");
        Debug.printDebug("getDynamicDisplayFieldHeader(DYNAMIC_ACTION_APPLICATION).size() = " + getDynamicDisplayFieldHeader(DYNAMIC_ACTION_APPLICATION).size());

        return "load_add_application_page";
    }

    public String loadAddUserPage() {
        setPageTitle_(getText("userGroup"));
        setPageSubTitle_(getText("actionType.searchUser"));
        //List appList = getApplicationList();
        populateDynamicActionSetup(DYNAMIC_ACTION_USER);
        setList_param(DYNAMIC_ACTION_USER);
        ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_USER)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_SEARCH_DESCRIPTION, getSearchDescription());
        ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_USER)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_SEARCH_ACTION_TYPE, "actionType.searchApplication");
        populateCheckBoxValue();

        //sample to keep data to object so that no need to hold them in hidden fields.
        keepObjectToSession(getModel());

        return "load_add_user_page";
    }

    private void populateCheckBoxValue() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        int idx = 0;
        String addRightsName;
        setAdditionalRightsList(null);
        for (Object obj : getApplicationList()) {
            ((GroupApplication) obj).setCreate_right(request.getParameter("accessRight_create_" + idx));
            ((GroupApplication) obj).setRetrieve_right(request.getParameter("accessRight_retrieve_" + idx));
            ((GroupApplication) obj).setUpdate_right(request.getParameter("accessRight_update_" + idx));
            ((GroupApplication) obj).setDelete_right(request.getParameter("accessRight_delete_" + idx));
            ((GroupApplication) obj).setPrint_right(request.getParameter("accessRight_print_" + idx++));
            List<ApplicationRights> list = ((GroupApplication) obj).getApplication().getRightsList();
            addRightsName = ((GroupApplication) obj).getApplication().getApplication_code() + "_";
            if (list != null && list.size() > 0) {
                for (ApplicationRights appRights : list) {
                    if (request.getParameter(addRightsName + appRights.getApp_rights_code()) != null && request.getParameter(addRightsName + appRights.getApp_rights_code()).equalsIgnoreCase("Y")) {
                        if (!getAdditionalRightsList().contains(addRightsName + appRights.getApp_rights_code())) {
                            getAdditionalRightsList().add(addRightsName + appRights.getApp_rights_code());
                        }
                    }
                }
            }
        }
    }

    private GroupApplication deleteAppFromList(List<GroupApplication> list, String appId) {
        int index = 0;
        for (GroupApplication groupApp : list) {
            if (groupApp.getApplication_id().equals(appId)) {
                return (GroupApplication) list.remove(index);
            }
            index++;
        }
        return null;
    }

    private GroupUser deleteUserFromList(List<GroupUser> list, String userId) {
        int index = 0;
        for (GroupUser groupUser : list) {
            if (groupUser.getUs_id().equals(userId)) {
                return (GroupUser) list.remove(index);
            }
            index++;
        }
        return null;
    }

    public String processDeleteApplication() {
        setPageTitle_(getText("userGroup"));
        if (getModel().getID() != null && !getModel().getID().equals("")) {
            setPageSubTitle_(getText("button.update"));
        }else{
             setPageSubTitle_(getText("button.add"));
        }
        GroupApplication item = null;
        for (int i = getSelected().length; i > 0; i--) {
            item = model.getGroupApplication().remove(Integer.parseInt(getSelected()[i - 1]));
            if (item.getID() != null) {
                if (Validator.isEmpty(model.get_deletedItem())) {
                    model.set_deletedItem("" + item.getID());
                } else {
                    model.set_deletedItem(model.get_deletedItem() + "," + item.getID());
                }
            }
        }
        Debug.printDebug("get_deletedItem = " + model.get_deletedItem());
        try {
            if (!Validator.isEmpty(model.get_deletedItem())) {
                getModel().set_operation("processDeleteApplication");
                serviceFactory.getInternalUserGroupService().delete(getModel().getID(), getModel());
            }
        } catch (BaseException be) {
            addActionMessage(be.getMessage());
        } catch (Exception e) {
            addActionError(e.getMessage());
        }

        if (getModel().getID() != null && !getModel().getID().equals("")) {
            return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
        }
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }
//    public String processDeleteApplication() {
//        //List appList = getApplicationList();
//        GroupApplication groupApp;
//        populateCheckBoxValue();
//        populateDynamicActionSetup(DYNAMIC_ACTION_APPLICATION);
//        List list = getApplicationList();
//        if (getSelected() != null && list.size() > 0) {
//            for (String appId : getSelected()) {
//                groupApp = deleteAppFromList(list, appId);
//                //if it is an existing Group Application.
//                if (groupApp != null && !groupApp.getUg_id().equals("")) {
//                    getDeletedApplicationList().add(groupApp);
//                }
//            }
//            setSelected(null);
//            setApplicationList(list);
//        }
//
//        //goto Edit Page if GroupId > 0, existing records
//        if (!getModel().getUg_id().equals("")) {
//            return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
//        }
//        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
//    }

    public String processDeleteUser() throws Exception {
//        GroupUser groupUser;
//        populateCheckBoxValue();
//        populateDynamicActionSetup(DYNAMIC_ACTION_USER);
//        List list = getUserList();
//        if (getUser_selected() != null && list.size() > 0) {
//            for (String appId : getUser_selected()) {
//                groupUser = deleteUserFromList(list, appId);
//                //if it is an existing Group Application.
//                if (groupUser != null && !groupUser.getUg_id().equals("")) {
//                    getDeletedUserList().add(groupUser);
//                }
//            }
//            setUser_selected(null);
//            setUserList(list);
//        }
//
//        //goto Edit Page if GroupId > 0, existing records
//        if (!getModel().getUg_id().equals("")) {
//            return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
//        }
        try {
            if (getUser_selected() != null) {
                userGroupDAO.deleteGroupUser(model, getUser_selected());
            }
        } catch (Exception e) {
            e.printStackTrace();
            addActionError(getText("group.error.failToDeleteUser"));
        } finally {
            userGroupDAO.closeSession();
        }

        return processEditUser();
    }

    public String processSearchApplication() {
        try {
            setPageTitle_(getText("userGroup"));
            setPageSubTitle_(getText("actionType.assignApplication"));
            SetupGroup ug = (SetupGroup) getObjectFromSession(model);
            if (ug != null) {
                model = ug;
            }

            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            int index = 0;
            populateDynamicActionSetup(DYNAMIC_ACTION_APPLICATION);
            setList_param(DYNAMIC_ACTION_APPLICATION);
            //Map setupMap = getDynamicActionSetup(DYNAMIC_ACTION_APPLICATION);
            Map searchParam = getDynamicSearchParam(DYNAMIC_ACTION_APPLICATION);
            //add any extra parameter here if necessary.
            String extraCondition = "";
            for (GroupApplication ga : model.getGroupApplication()) {
                if (extraCondition.equals("")) {
                    extraCondition = ga.getApplication().getID().toString();
                } else {
                    extraCondition += "|" + ga.getApplication().getID();
                }
            }
            if (!extraCondition.equals("")) {
                // to filter those already selected applications
                searchParam.put("t_setup_application.application_id", "!IN " + extraCondition);
            }
            Map setupMap = getDynamicActionSetup(DYNAMIC_ACTION_APPLICATION);
            this.setPaging(true);
            new DataRetriever().retrieveDataDynamic(this, DYNAMIC_ACTION_APPLICATION, setupMap,
                    searchParam, true, getDynamicColumns(DYNAMIC_ACTION_APPLICATION), getDynamicResult(DYNAMIC_ACTION_APPLICATION),
                    request, request.getSession().getServletContext());
            ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_APPLICATION)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO, setupMap.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO));
            ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_APPLICATION)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_SIZE, setupMap.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_SIZE));
            ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_APPLICATION)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO_OF_ROWS, setupMap.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO_OF_ROWS));
            ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_APPLICATION)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGING_URL, "processSearchApplicationUserGroup");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            new LogFunction().logError(this.getClass(), "", e);
        }
        return "load_add_application_page";
    }

    public String processSearchUser() {
        try {
            setPageTitle_(getText("userGroup"));
            setPageSubTitle_(getText("actionType.searchUser"));
            SetupGroup ug = (SetupGroup) getObjectFromSession(model);
            if (ug != null) {
                model = ug;
            }

            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            int index = 0;
            populateDynamicActionSetup(DYNAMIC_ACTION_USER);
            setList_param(DYNAMIC_ACTION_USER);
            //Map setupMap = getDynamicActionSetup(DYNAMIC_ACTION_APPLICATION);
            Map searchParam = getDynamicSearchParam(DYNAMIC_ACTION_USER);
            //add any extra parameter here if necessary.
            String extraCondition = "";
            for (GroupUser gu : getUserList()) {
                if (extraCondition.equals("")) {
                    extraCondition = gu.getUs_id().toString();
                } else {
                    extraCondition += "|" + gu.getUs_id();
                }
            }
            if (!extraCondition.equals("")) {
                // to filter those already selected applications
                searchParam.put("t_setup_user.us_id", "!IN " + extraCondition);
            }
            Map setupMap = getDynamicActionSetup(DYNAMIC_ACTION_USER);
//            System.out.println("impianSecurityCheck = " + setupMap.get("impianSecurityCheck"));
            this.setPaging(true);
            new DataRetriever().retrieveDataDynamic(this, DYNAMIC_ACTION_USER, setupMap,
                    searchParam, true, getDynamicColumns(DYNAMIC_ACTION_USER), getDynamicResult(DYNAMIC_ACTION_USER),
                    request, request.getSession().getServletContext());
            ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_USER)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO, setupMap.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO));
            ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_USER)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_SIZE, setupMap.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_SIZE));
            ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_USER)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO_OF_ROWS, setupMap.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO_OF_ROWS));
            ((Map) getDynamicSetupMap().get(DYNAMIC_ACTION_USER)).put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGING_URL, "processSearchUserUserGroup");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            new LogFunction().logError(this.getClass(), "", e);
        }
        return "load_add_user_page";
    }

    public String delete() {
        try {
            if (getSelected() != null) {
                userGroupDAO.groupDelete(getSelected(), new SetupGroup());
            }
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
            returnStr = SystemConstants.ACTION_Status.DELETE_FAIL;
        } finally {
            userGroupDAO.closeSession();
        }
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("deleteSuccess"));
        } else {
            addActionError(getText("deleteFail"));
        }
        return SUCCESS;
    }

    public String[] getApplication_selected() {
        return applicationSelected;
    }

    public void setApplication_selected(String[] selected) {
        applicationSelected = selected;
    }

    public String[] getUser_selected() {
        return userSelected;
    }

    public void setUser_selected(String[] userSelected) {
        this.userSelected = userSelected;
    }

    public List<String> getAccessRight_retrieveList() {
        return accessRight_retrieveList;
    }

    public void setAccessRight_retrieveList(List<String> accessRightRetrieveList) {
        accessRight_retrieveList = accessRightRetrieveList;
    }

    /*public List<String> getAdditionalRightsList() {
     return additionalRightsList;
     }

     public void setAdditionalRightsList(List<String> additionalRightsList) {
     this.additionalRightsList = additionalRightsList;
     }*/
    public String checkSelected(String name) {
        if (getAdditionalRightsList().contains(name)) {
            return "True";
        }
        return "False";
    }

    public String getSortURL_() {
        return sortURL_;
    }

    public void setSortURL_(String sortURL_) {
        this.sortURL_ = sortURL_;
    }

    public String getSort_() {
        return sort_;
    }

    public void setSort_(String sort_) {
        this.sort_ = sort_;
    }

    public String getOrder_() {
        return order_;
    }

    public void setOrder_(String order_) {
        this.order_ = order_;
    }

    public String loadClonePage() {
        setPageTitle_("User Group");
        setPageSubTitle_("Clone");
        return "load_clone_page";
    }

    public String processClone() {
        try {
            setPageTitle_(getText("userGroup"));
            setPageSubTitle_(getText("group.clone"));
            getModel().set_operation("clone");
            serviceFactory.getInternalUserGroupService().manualUpdate(getModel());
            addActionMessage(getText("group.cloneSuccess"));
        } catch (BaseException be) {
            addActionError(be.getMessage());
            return "load_clone_page";
        } catch (Exception e) {
            addActionError(e.getMessage());
            return "load_clone_page";
        }
        setId(model.getTempSetupGroup().getID());
        return loadEditPage();
    }
    
    public List getGroupTypeOption() {
        return getCommList().getGroupTypeOption();
    }
    
    public String processEditUser2() throws Exception {
        setPageTitle_("User Group");
        setPageSubTitle_("Search User");
        Map param = new HashMap();
        model = super.processEdit(getModel());
        model = baseDAO.getModelById(model.getUg_id(), SetupGroup.class);
        System.out.println("1 user list :"+model.getGroupUser().size());

        param.put(SystemConstants.QUERY.JOIN, SystemConstants.QUERY.INNER_JOIN + "_self.groupUser groupUser");
        param.put(SystemConstants.CRITERIA.NO_CHANGE_LOWER + "UG_ID", model.getID());
        BaseDAOImpl userListDAO = new BaseDAOImpl();
        userListDAO.setSession(baseDAO.getSession());
        userListDAO.setIsPaging(Boolean.TRUE);
        userListDAO.setMaxSize(15);
        setPageSize(15);
        Long size = userListDAO.sqlCountRecord("select count(*) from t_setup_group_user where ug_id = '" + model.getUg_id() + "'", null);
        System.out.println("size " + size);
        if (size <= 15) {
            userListDAO.setFirstResult(0);
            setPageNo(1);
        } else {
            if (getPageNo() == null) {
                setPageNo(1);
            }
            userListDAO.setFirstResult(((getPageNo() - 1) * 15));
        }
        if (size == null) {
            setNumberOfRows(0);
        } else {
            setNumberOfRows(size.intValue());
        }
        System.out.println("2 user list :"+model.getGroupUser().size());

        setPagingURL("processEditUserUserGroup?ID=" + model.getID() + "&sort_=" + sort_ + "&order_=" + order_);
        setSortURL_("processEditUserUserGroup?ID=" + model.getID());
        //preload the data for Group Users DONE
//        System.out.println("param = " + param);
        System.out.println("sort_" + sort_);
        System.out.println("3 user list :"+model.getGroupUser().size());
        if (sort_.equals("id")) {
//            setUserList(userListDAO.list_order(param, GroupUser.class, true, "order by groupUser.us_user_id " + (order_.equals("A") ? "asc" : "desc")));
            setUserList(model.getGroupUser());
        } else {
            setUserList(userListDAO.list_order(param, GroupUser.class, true, "order by groupUser.us_user_name " + (order_.equals("A") ? "asc" : "desc")));
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE + "_user2";
    }
    
    public String processUpdateRR() {
        returnStr = SUCCESS;
        try {
            System.out.println("getNext_action_officer_ " + getNext_action_officer_());
            getModel().set_nextActionOfficer(getNext_action_officer_());
            if(getModel().getGroupUser().size() > 0){
                for(GroupUser gs : getModel().getGroupUser()){
                    System.out.println("gs 1 :"+gs.getUs_id());
                    System.out.println("gs 2 :"+gs.getNext_action_officer());
                    System.out.println("gs 3 :"+gs.getOn_duty());
                }
            }

            getModel().set_operation(SetupGroup.OPERATION.UPDATE_RR);//added by ChangMH @ 08-Jul-2019
            serviceFactory.getInternalUserGroupService().update(getModel());
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
            returnStr = SystemConstants.ACTION_Status.UPDATE_FAIL;
        } finally {
            userGroupDAO.closeSession();
        }
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("updateSuccess"));
        } else {
            addActionError(getText("updateFail"));
        }
        return returnStr;
    }
    
    private String next_action_officer_;

    public String getNext_action_officer_() {
        return next_action_officer_;
    }

    public void setNext_action_officer_(String next_action_officer_) {
        this.next_action_officer_ = next_action_officer_;
    }
}
