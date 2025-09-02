package com.sains.framework.model;

import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.Session;

@Entity
@Table(name="t_setup_group")
public class SetupGroup extends ModelBase implements java.io.Serializable{
    private static final long serialVersionUID = -665992565258121239L;
    private String ug_id = "";
    private String group_code = "";
    private String group_name = "";
    private String group_type = "";
    private String system_id = "";
    private String group_div = "";
    private String group_div_str = "";
    
    //private Group parentGroup;
    private List<GroupApplication> groupApplication = new ArrayList();
    private List<GroupUser> groupUser;
    //private List<Group> attachedApplication;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
//    private final String[] updatableColumns = new String[] {"Ug_id", "group_code", "group_name"};

// if required to have system_id        
    private final String[] updatableColumns = new String[] {"Ug_id", "group_code", "group_name", "group_div", "system_id"};
    
    //added by TeoYL @ 06-03-2024
    public static final class OPERATION {

        public static final String DELETE = "delete";
        public static final String UPDATE = "update";
        public static final String UPDATE_RR = "update_rr"; 
    }


    public static final class GROUP_TYPE {
        public static final String Application = "A";
        public static final String Workflow = "W";
    }

    public static final class GROUP_LEVEL {
        public static final String Master = "4";
        public static final String Central = "3";
        public static final String Dept = "2";
        public static final String Normal = "1";
    }
    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }

    @Id
    @Column(name="ug_id")
    public String getUg_id() {
            return ug_id;
    }
    public void setUg_id(String ug_id) {
            this.ug_id = ug_id;
    }

    @Transient
    public String getID() {
            return getUg_id();
    }

    public void setID(String ug_id) {
            setUg_id(ug_id);
    }

    @Column(name="group_code")
    public String getGroup_code() {
            return group_code;
    }
    public void setGroup_code(String groupCode) {
            group_code = groupCode;
    }

    @Column(name="group_name")
    public String getGroup_name() {
            return group_name;
    }
    public void setGroup_name(String groupName) {
            group_name = groupName;
    }

    @Column(name="group_type")
    public String getGroup_type() {
            return group_type;
    }
    public void setGroup_type(String groupType) {
            group_type = groupType;
    }

    @OneToMany(targetEntity=GroupApplication.class, fetch=FetchType.LAZY, mappedBy="ug_id")
    public List<GroupApplication> getGroupApplication() {
            return groupApplication;
    }
    public void setGroupApplication(List<GroupApplication> groupApplication) {
            this.groupApplication = groupApplication;
    }

    @OneToMany(targetEntity=GroupUser.class, fetch=FetchType.LAZY, mappedBy="ug_id")
    public List<GroupUser> getGroupUser() {
            return groupUser;
    }
    public void setGroupUser(List<GroupUser> groupUser) {
            this.groupUser = groupUser;
    }

    /*@ManyToOne(targetEntity=Group.class, optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="parent_group_id", insertable=false, updatable=false, nullable = true)
    public Group getParentGroup() {
            return parentGroup;
    }
    public void setParentGroup(Group parentGroup) {
            this.parentGroup = parentGroup;
    }

    @OneToMany(targetEntity=Group.class, fetch=FetchType.LAZY, mappedBy="parent_group_id")
    public List<Group> getChildGroup() {
            return childGroup;
    }
    public void setChildGroup(List<Group> childGroup) {
            this.childGroup = childGroup;
    }

    @OneToMany(targetEntity=Application.class, fetch=FetchType.LAZY, mappedBy="attached_group_id")
    public List<Group> getAttachedApplication() {
            return attachedApplication;
    }
    public void setAttachedApplication(List<Group> attachedApplication) {
            this.attachedApplication = attachedApplication;
    }*/

    @Column(name="created_by")
    public String getCreated_by() {
            return created_by;
    }
    public void setCreated_by(String createdBy) {
            created_by = createdBy;
    }

    @Column(name="created_date")
    public java.sql.Timestamp getCreated_date() {
            return created_date;
    }
    public void setCreated_date(java.sql.Timestamp createdDate) {
            created_date = createdDate;
    }

    @Column(name="updated_date")
    public java.sql.Timestamp getUpdated_date() {
            return updated_date;
    }
    public void setUpdated_date(java.sql.Timestamp updatedDate) {
            updated_date = updatedDate;
    }

    @Column(name="updated_by")
    public String getUpdated_by() {
            return updated_by;
    }
    public void setUpdated_by(String updatedBy) {
            updated_by = updatedBy;
    }
    
    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }
    
    @Column(name = "group_div")
    public String getGroup_div() {
        return group_div;
    }

    public void setGroup_div(String group_div) {
        this.group_div = group_div;
    }

//    // Added by ThoTH @ 17-Mar-2011 -- TESTING Purpose on TthAction
//    private List<Application> applicationList;
//    private List<User> userList;
//    @Transient
//    public List<Application> getApplicationList() {
//        return applicationList;
//    }
//
//    public void setApplicationList(List<Application> applicationList) {
//        this.applicationList = applicationList;
//    }
//
//    @Transient
//    public List<User> getUserList() {
//        return userList;
//    }
//
//    public void setUserList(List<User> userList) {
//        this.userList = userList;
//    }
//    // END
        SetupGroup tempSetupGroup = null;

    @Transient
    public SetupGroup getTempSetupGroup() {
        return tempSetupGroup;
    }

    public void setTempSetupGroup(SetupGroup tempSetupGroup) {
        this.tempSetupGroup = tempSetupGroup;
    }
    
    public void manualOperation(org.hibernate.Session session) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(session);
        try {
            //ahmadni @ 18-Jul-2016 : For delete public user group 
            Debug.printFrameworkDebug("operation " + this.get_operation());
            if (!this.get_operation().equals(SystemConstants.COMM_OPERATION.DELETE)) {
                SetupGroup fromSetupGroup = (SetupGroup) session.get(SetupGroup.class, this.getID());
                SetupGroup newSetupGroup = this.getTempSetupGroup();
                newSetupGroup.defaultAddProperties();;
                session.save(this.getTempSetupGroup());
                for (GroupApplication groupApp : fromSetupGroup.getGroupApplication()) {
                    groupApp.getGroupAppRights().size();
                    session.evict(groupApp);
                    groupApp.defaultAddProperties();
                    groupApp.setUg_id(newSetupGroup.getID());
                    session.save(groupApp);
                    for (GroupApplicationRights groupAppRight : groupApp.getGroupAppRights()) {
                        session.evict(groupAppRight);
                        groupAppRight.defaultAddProperties();
                        groupAppRight.setUg_app_id(groupApp.getID());
                        session.save(groupAppRight);
                    }
                }
            } else {
                Debug.printFrameworkDebug("manual operation delete here");
                SetupGroup userGroup = null;
                for (String groupId : get_selected()) {
                    Debug.printFrameworkDebug("--- ug_id " + groupId);
                    userGroup = (SetupGroup) dao.getObjectById(groupId, SetupGroup.class);
                    if (userGroup != null) {
                        if (userGroup.getGroupApplication() != null) {
                            for (GroupApplication groupApp : userGroup.getGroupApplication()) {
                                if (groupApp.getGroupAppRights() != null) {
                                    for (GroupApplicationRights gAppRight : groupApp.getGroupAppRights()) {
                                        session.delete(gAppRight);
                                    }
                                }
                                session.delete(groupApp);
                            }
                        }
                        if (userGroup.getGroupUser() != null) {
                            for (GroupUser groupUser : userGroup.getGroupUser()) {
                                session.delete(groupUser);
                            }
                        }
                        session.delete(userGroup);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
    private final String[] columnLength = new String[]{};
    private Map columnLengthMap = null;
    @Transient
    public Map getColumnLengthMap() {
        if (columnLengthMap == null) {
            columnLengthMap = new HashMap();
            String[] strA = null;

            for (String str : columnLength) {
                strA = str.split(":");
                columnLengthMap.put(strA[0], strA[1]);
            }
        }

        return columnLengthMap;
    }
    
    //added by TeoYL @ 06-03-2024
    private String _nextActionOfficer = "";

    @Transient
    public String get_nextActionOfficer() {
        return _nextActionOfficer;
    }

    public void set_nextActionOfficer(String _nextActionOfficer) {
        this._nextActionOfficer = _nextActionOfficer;
    }
    
    public void preUpdate(Session session, Object dataEntryModel) throws Exception {
        //Map sessionMap = ActionContext.getContext().getSession();
        BaseDAOImpl dao = new BaseDAOImpl();
        setSession_(session);
        dao.setSession(session);
        SetupGroup updatingModel = (SetupGroup) dataEntryModel;

        if (updatingModel.get_operation().equals(OPERATION.UPDATE_RR)) { //added by ChangMH @ 08-Jul-2019
//            updatingModel.updatableColumns = new String[]{"ug_user_id"};
            if (updatingModel.getGroupUser() != null) {
                for (GroupUser groupUser : updatingModel.getGroupUser()) {
                    if (!Validator.isEmpty(groupUser.getID())) {
                        System.out.println("SetupGroupModel :" + groupUser.getUs_id());
                        if (groupUser.getUs_id().equals(updatingModel.get_nextActionOfficer())) {
                            groupUser.setNext_action_officer("Y");
                        } else {
                            groupUser.setNext_action_officer("N");
                        }
                        System.out.println("groupUser.getOn_duty() :" + groupUser.getOn_duty());
                        if (Validator.isEmpty(groupUser.getOn_duty())) {
                            groupUser.setOn_duty("Y");
                        } else if (groupUser.getOn_duty().equals("on")) {
                            groupUser.setOn_duty("Y");
                        } else {
                            groupUser.setOn_duty("N");
                        }
                        groupUser.defaultUpdateProperties();
                        session.update(groupUser);
                    }
                }
            }
        }

    }
}