package com.sains.framework.model;


import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.ModelBase;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.Session;

@Entity
@Table(name="t_setup_application")
public class Application extends ModelBase implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String application_id = "";
    private String application_code = "";
    private String application_name = "";
    private String application_name_code = "";
    private String app_icon = "";
    private String application_type = "A";
    private Integer application_order = 0;
    private String action_name = "";
    private String retrieve_right = "N";
    private String update_right = "N";
    private String delete_right = "N";
    private String create_right = "N";
    private String print_right = "N";
    private String extra_right1 = "N";
    private String extra_right2 = "N";
    private String system_app = "N";
    private String attached_module_id;
    private String action_class = "";
    private String hidden = "N";
    private Integer show_in_main_order;
    private String show_in_main = "N";
    private String remark;
    private String system_type = SystemConstants.SYSTEM_TYPE.DEFAULT;
//    private String show_in_main = "N";
//    private Integer show_in_main_order = 999;
    private final String[] updatableColumns = new String[] {"Application_id", "Application_code", "Application_name", "Application_name_code", "App_icon","Application_type",
        "Action_name", "Create_right", "Retrieve_right", "Update_right", "Delete_right", "Print_right", "Attached_module_id", "Action_class", "Hidden", "show_in_main", "show_in_main_order", "Remark", "System_type","application_order"};
    private Module attachedModule;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private List<ApplicationRights> rightsList = new ArrayList();
    private List<GroupApplication> groupAppList = new ArrayList();
    private final String[] columnLength = new String[]{"parent_gender:1", "parent_id:16", "application_code:20", "parent_own_car:1", "parent_state:3"};
    private Map columnLengthMap = null;

    public Application() {
        userDefined_autoValidation(Boolean.TRUE);
        userDefined_insertNoDuplicate("application_code;application.code");
    }

    @Id
    @Column(name = "application_id")
    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String applicationId) {
        this.application_id = applicationId;
    }

    @Transient
    public String getID() {
        return getApplication_id();
    }

    public void setID(String applicationId) {
        setApplication_id(applicationId);
    }

    @Column(name = "application_code")
    public String getApplication_code() {
        return application_code;
    }

    public void setApplication_code(String applicationCode) {
        this.application_code = applicationCode;
    }

    @Column(name = "application_name")
    public String getApplication_name() {
        return application_name;
    }

    public void setApplication_name(String application_name) {
        this.application_name = application_name;
    }

    @Column(name = "application_name_code")
    public String getApplication_name_code() {
        return application_name_code;
    }

    public void setApplication_name_code(String application_name_code) {
        this.application_name_code = application_name_code;
    }

    @Column(name = "app_icon")
    public String getApp_icon() {
        return app_icon;
    }

    public void setApp_icon(String app_icon) {
        this.app_icon = app_icon;
    }

    @Column(name = "application_type")
    public String getApplication_type() {
        return application_type;
    }

    public void setApplication_type(String application_type) {
        this.application_type = application_type;
    }

    @Column(name = "application_order")
    public Integer getApplication_order() {
        return application_order;
    }
    @Transient
    public String getApplication_order_str() {
        try {
            return application_order.toString();
        } catch (Exception e) {
        }
        return "";
    }

    public void setApplication_order(Integer application_order) {
        this.application_order = application_order;
    }

    @Column(name = "action_name")
    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public void setModule_level(String action_name) {
        this.action_name = action_name;
    }

    @Column(name = "retrieve_right")
    public String getRetrieve_right() {
        return retrieve_right;
    }

    public void setRetrieve_right(String retrieve_right) {
        if (retrieve_right == null || retrieve_right.equalsIgnoreCase("false")) {
            this.retrieve_right = "N";
        } else {
            this.retrieve_right = retrieve_right;
        }
    }

    @Column(name = "update_right")
    public String getUpdate_right() {
        return update_right;
    }

    public void setUpdate_right(String updateRight) {
        if (updateRight == null || updateRight.equalsIgnoreCase("false")) {
            this.update_right = "N";
        } else {
            this.update_right = updateRight;
        }
    }

    @Column(name = "delete_right")
    public String getDelete_right() {
        return delete_right;
    }

    public void setDelete_right(String deleteRight) {
        if (deleteRight == null || deleteRight.equalsIgnoreCase("false")) {
            delete_right = "N";
        } else {
            delete_right = deleteRight;
        }
    }

    @Column(name = "create_right")
    public String getCreate_right() {
        return create_right;
    }

    public void setCreate_right(String createRight) {
        if (createRight == null || createRight.equalsIgnoreCase("false")) {
            create_right = "N";
        } else {
            create_right = createRight;
        }
    }

    @Column(name = "print_right")
    public String getPrint_right() {
        return print_right;
    }

    public void setPrint_right(String printRight) {
        if (printRight == null || printRight.equalsIgnoreCase("false")) {
            print_right = "N";
        } else {
            print_right = printRight;
        }
    }

    @Column(name = "extra_right1")
    public String getExtra_right1() {
        return extra_right1;
    }

    public void setExtra_right1(String extraRight1) {
        if (extraRight1 == null || extraRight1.equalsIgnoreCase("false")) {
            extra_right1 = "N";
        } else {
            extra_right1 = extraRight1;
        }
    }

    @Column(name = "extra_right2")
    public String getExtra_right2() {
        return extra_right2;
    }

    public void setExtra_right2(String extraRight2) {
        if (extraRight2 == null || extraRight2.equalsIgnoreCase("false")) {
            extra_right2 = "N";
        } else {
            extra_right2 = extraRight2;
        }
    }

    @Column(name = "system_app")
    public String getSystem_app() {
        return system_app;
    }

    public void setSystem_app(String system_app) {
        if (system_app == null || system_app.equalsIgnoreCase("false")) {
            system_app = "N";
        } else {
            this.system_app = system_app;
        }
    }

    @Column(name = "attached_module_id")
    public String getAttached_module_id() {
        return attached_module_id;
    }

    public void setAttached_module_id(String attachedModuleId) {
        attached_module_id = attachedModuleId;
    }

    @ManyToOne(targetEntity = Module.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "attached_module_id", insertable = false, updatable = false, nullable = true)
    public Module getAttachedModule() {
        return attachedModule;
    }

    public void setAttachedModule(Module attachedModule) {
        this.attachedModule = attachedModule;
    }

    @OneToMany(targetEntity = ApplicationRights.class, fetch = FetchType.LAZY, mappedBy = "application_id")
    public List<ApplicationRights> getRightsList() {
        return rightsList;
    }

    public void setRightsList(List rightsList) {
        this.rightsList = rightsList;
    }

    @OneToMany(targetEntity = GroupApplication.class, fetch = FetchType.LAZY, mappedBy = "application_id")
    public List<GroupApplication> getGroupAppList() {
        return groupAppList;
    }

    public void setGroupAppList(List groupAppList) {
        this.groupAppList = groupAppList;
    }

    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String createdBy) {
        created_by = createdBy;
    }

    @Column(name = "created_date")
    public java.util.Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(java.sql.Timestamp createdDate) {
        created_date = createdDate;
    }

    @Column(name = "updated_by")
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updatedBy) {
        updated_by = updatedBy;
    }

    @Column(name = "updated_date")
    public java.util.Date getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(java.sql.Timestamp updatedDate) {
        updated_date = updatedDate;
    }

    public boolean equals(Object obj) {
        if (obj.getClass().getSimpleName().equals(this.getClass().getSimpleName())) {
            return getApplication_id().equals(((Application) obj).getApplication_id());
        }
        return false;
    }

    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }

    @Column(name = "action_class")
    public String getAction_class() {
        return action_class;
    }

    public void setAction_class(String action_class) {
        this.action_class = action_class;
    }

    @Column(name = "hidden")
    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        if (!hidden.equalsIgnoreCase("Y")) {
            this.hidden = "N";
        } else {
            this.hidden = hidden;
        }
    }

    @Column(name = "show_in_main_order")
    public Integer getShow_in_main_order() {
        return show_in_main_order;
    }

    public void setShow_in_main_order(Integer show_in_main_order) {
        this.show_in_main_order = show_in_main_order;
    }

    @Column(name = "show_in_main")
    public String getShow_in_main() {
        return show_in_main;
    }

    public void setShow_in_main(String show_in_main) {
        this.show_in_main = show_in_main;
    }
    
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    @Column(name = "system_type")
    public String getSystem_type() {
        return system_type;
    }

    public void setSystem_type(String system_type) {
        this.system_type = system_type;
    }
//    @Column(name = "show_in_main")
//    public String getShow_in_main() {
//        return show_in_main;
//    }
//
//    public void setShow_in_main(String show_in_main) {
//        if (!show_in_main.equalsIgnoreCase("Y")) {
//            this.show_in_main = "N";
//        } else {
//            this.show_in_main = show_in_main;
//        }
//    }
//
//    @Column(name = "show_in_main_order")
//    public Integer getShow_in_main_order() {
//        return show_in_main_order;
//    }
//
//    public void setShow_in_main_order(Integer show_in_main_order) {
//        this.show_in_main_order = show_in_main_order;
//    }
//
//
//    @Column(name = "system_type")
//    public String getSystem_type() {
//        return system_type;
//    }
//
//    public void setSystem_type(String system_type) {
//        this.system_type = system_type;
//    }

    @Override
    public List<List> myItemsLists(){
        List list = new ArrayList();
        getRightsList().size();
        list.add(getRightsList());
        list.add(getGroupAppList());
        return list;
    }
    // Added by ThoTH @ 17-Mar-2011 -- TESTING Purpose on TthAction
//    private List<ApplicationRights> applicationRightsList;
//    @Transient
//    public List<ApplicationRights> getApplicationRightsList() {
//        return applicationRightsList;
//    }
//
//    public void setApplicationRightsList(List<ApplicationRights> applicationRightsList) {
//        this.applicationRightsList = applicationRightsList;
//    }
//    private String FKID = "";
//    @Transient
//    public String getFKID() {
//        return FKID;
//    }
//    public void setFKID(String FKID) {
//        this.FKID = FKID;
//    }
    // END
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

    @Override
    public void postUpdate(Session session, Object updatingObject) throws Exception {
        System.out.println(" in Application's post update");
        Application app = (Application) updatingObject;
        System.out.println(""+ app.getRightsList().size());
    }
    
    
}
