package com.sains.framework.model;

import com.sains.framework.base.ModelBase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_setup_group_app")
public class GroupApplication extends ModelBase implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private String ug_app_id = "";
    private String ug_id = "";
    private String application_id = "";
    private String retrieve_right = "N";
    private String update_right = "N";
    private String delete_right = "N";
    private String create_right = "N";
    private String print_right = "N";
    private String extra_right1 = "N";
    private String extra_right2 = "N";
    private final String[] updatableColumns = new String[] {"Ug_app_id", "Ug_id", "Application_id", "Retrieve_right", "Update_right",
                                                                                                                                    "Delete_right", "Create_right", "Print_right", "Extra_right1", "Extra_right2",
                                                                                                                                    "Updated_by", "Updated_date"}; 

    //private Menu attachedMenu;
    private Application application;
    private List<GroupApplicationRights> groupAppRights = new ArrayList() ;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;



    @Id
    @Column(name="ug_app_id")
    public String getUg_app_id() {
            return ug_app_id;
    }
    public void setUg_app_id(String ugAppId) {
            ug_app_id = ugAppId;
    }

    @Transient
    public String getID() {
            return getUg_app_id();
    }

    public void setID(String ugAppId) {
            setUg_app_id(ugAppId);
    }

    @Column(name="ug_id")
    public String getUg_id() {
            return ug_id;
    }
    public void setUg_id(String ugId) {
            ug_id = ugId;
    }

    @Column(name="application_id")
    public String getApplication_id() {
            return application_id;
    }
    public void setApplication_id(String applicationId) {
            this.application_id = applicationId;
    }

    @Column(name="retrieve_right")
    public String getRetrieve_right() {
            return retrieve_right;
    }
    public void setRetrieve_right(String retrieve_right) {
            if (retrieve_right == null || retrieve_right.equalsIgnoreCase("false")){
                    this.retrieve_right = "N";
            } else {
                    this.retrieve_right = retrieve_right;
            }
    }

    @Column(name="update_right")
    public String getUpdate_right() {
            return update_right;
    }
    public void setUpdate_right(String updateRight) {
            if (updateRight == null || updateRight.equalsIgnoreCase("false")){
                    this.update_right = "N";
            } else {
                    this.update_right = updateRight;
            }
    }

    @Column(name="delete_right")
    public String getDelete_right() {
            return delete_right;
    }
    public void setDelete_right(String deleteRight) {
            if (deleteRight == null || deleteRight.equalsIgnoreCase("false")){
                    delete_right = "N";
            } else {
                    delete_right = deleteRight;
            }
    }

    @Column(name="create_right")
    public String getCreate_right() {
            return create_right;
    }
    public void setCreate_right(String createRight) {
            if (createRight == null || createRight.equalsIgnoreCase("false")){
                    create_right = "N";
            } else {
                    create_right = createRight;
            }
    }

    @Column(name="print_right")
    public String getPrint_right() {
            return print_right;
    }
    public void setPrint_right(String printRight) {
            if (printRight == null || printRight.equalsIgnoreCase("false")){
                    print_right = "N";
            } else {
                    print_right = printRight;
            }
    }

    @Column(name="extra_right1")
    public String getExtra_right1() {
            return extra_right1;
    }
    public void setExtra_right1(String extraRight1) {
            if (extraRight1.equalsIgnoreCase("false")){
                    extra_right1 = "N";
            } else {
                    extra_right1 = extraRight1;
            }
    }

    @Column(name="extra_right2")
    public String getExtra_right2() {
            return extra_right2;
    }
    public void setExtra_right2(String extraRight2) {
            if (extraRight2.equalsIgnoreCase("false")){
                    extra_right2 = "N";
            } else {
                    extra_right2 = extraRight2;
            }
    }

    /*@ManyToOne(targetEntity=Module.class, optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="attached_module_id", insertable=false, updatable=false, nullable = true)
    public Module getAttachedModule() {
            return attachedModule;
    }
    public void setAttachedModule(Module attachedModule) {
            this.attachedModule = attachedModule;
    }*/

    @OneToOne(targetEntity=Application.class, optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="application_id", insertable=false, updatable=false, nullable = true)
    public Application getApplication() {
            return application;
    }
    public void setApplication(Application application) {
            this.application = application;
    }

    SetupGroup group = new SetupGroup();
    @OneToOne(targetEntity=SetupGroup.class, optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="ug_id", insertable=false, updatable=false, nullable = true)
    public SetupGroup getGroup() {
        return group;
    }

    public void setGroup(SetupGroup group) {
        this.group = group;
    }

    @OneToMany(targetEntity=GroupApplicationRights.class, fetch=FetchType.LAZY, mappedBy="ug_app_id")
    public List<GroupApplicationRights> getGroupAppRights() {
            return groupAppRights;
    }
    public void setGroupAppRights(List<GroupApplicationRights> rightsList) {
            this.groupAppRights = rightsList;
    }

    @Column(name="created_by")
    public String getCreated_by() {
            return created_by;
    }
    public void setCreated_by(String createdBy) {
            created_by = createdBy;
    }

    @Column(name="created_date")
    public java.util.Date getCreated_date() {
            return created_date;
    }
    public void setCreated_date(java.sql.Timestamp createdDate) {
            created_date = createdDate;
    }

    @Column(name="updated_by")
    public String getUpdated_by() {
            return updated_by;
    }
    public void setUpdated_by(String updatedBy) {
            updated_by = updatedBy;
    }

    @Column(name="updated_date")
    public java.sql.Timestamp getUpdated_date() {
            return updated_date;
    }
    public void setUpdated_date(java.sql.Timestamp updatedDate) {
            updated_date = updatedDate;
    }

    @Transient
    public String[] getUpdatableColumns() {
            return updatableColumns;
    }

    @Override
    public List<List> myItemsLists(){
        List list = new ArrayList();
        list.add(getGroupAppRights());
        return list;
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
}