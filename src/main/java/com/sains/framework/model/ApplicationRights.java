package com.sains.framework.model;

import com.sains.framework.base.ModelBase;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "t_setup_app_right")
public class ApplicationRights extends ModelBase implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String app_rights_id = "";
    private String app_rights_code = "";
    private String app_rights_description = "";
    private String app_rights_methods = null;
    private String application_id;
    private Application application;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private List<GroupApplicationRights> groupApplicationRightsList = new ArrayList();
    private final String[] updatableColumns = new String[]{"App_rights_id","App_rights_code", "App_rights_description", "App_rights_methods"};
    
    @Id
    @Column(name = "app_rights_id")
    public String getApp_rights_id() {
        return app_rights_id;
    }

    public void setApp_rights_id(String appRightsId) {
        app_rights_id = appRightsId;
    }

    @Transient
    public String getID() {
        return getApp_rights_id();
    }

    public void setID(String appRightsId) {
        setApp_rights_id(appRightsId);
    }

    @Column(name = "app_rights_code")
    public String getApp_rights_code() {
        return app_rights_code;
    }

    public void setApp_rights_code(String appRightsCode) {
        app_rights_code = appRightsCode;
    }

    @Column(name = "app_rights_description")
    public String getApp_rights_description() {
        return app_rights_description;
    }

    public void setApp_rights_description(String appRightsDescription) {
        app_rights_description = appRightsDescription;
    }

    @Transient
    public String getApp_rights_methods() {
        if (app_rights_methods == null) {
            if (getRightMethodList() != null && getRightMethodList().size() > 0) {
                for (RightMethod rightMethod : getRightMethodList()) {
                    if (app_rights_methods == null) {
                        app_rights_methods = rightMethod.getMethod_str();
                    } else {
                        app_rights_methods += ", "+rightMethod.getMethod_str();
                    }
                }
            }
        }
        if (app_rights_methods == null) app_rights_methods = "";
        return app_rights_methods;
    }

    public void setApp_rights_methods(String appRightsMethods) {
        app_rights_methods = appRightsMethods;
    }

    @Column(name = "application_id")
    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String applicationId) {
        application_id = applicationId;
    }

    @OneToOne(targetEntity = Application.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", insertable = false, updatable = false, nullable = true)
    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
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

    @OneToMany(targetEntity = GroupApplicationRights.class, fetch = FetchType.LAZY, mappedBy = "app_rights_id")
    public List<GroupApplicationRights> getGroupApplicationRightsList() {
        return groupApplicationRightsList;
    }

    public void setGroupApplicationRightsList(
        List<GroupApplicationRights> groupApplicationRightsList) {
        this.groupApplicationRightsList = groupApplicationRightsList;
    }

    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }

    private String gaRights_id;
    @Transient
    public String getGaRights_id() {
        return gaRights_id;
    }

    public void setGaRights_id(String gaRights_id) {
        this.gaRights_id = gaRights_id;
    }

    private String gaRightChecked;
    @Transient
    public String getGaRightChecked() {
        return gaRightChecked;
    }

    public void setGaRightChecked(String gaRightChecked) {
        if (gaRightChecked.equalsIgnoreCase("Y") || gaRightChecked.equalsIgnoreCase("true")) {
            this.gaRightChecked = "Y";
        } else {
            this.gaRightChecked = "N";
        }
    }

    List<RightMethod> rightMethodList = new ArrayList();
    @OneToMany(targetEntity = RightMethod.class, fetch = FetchType.LAZY, mappedBy = "app_rights_id")
    @javax.persistence.OrderBy("method_str")
    public List<RightMethod> getRightMethodList() {
        return rightMethodList;
    }

    public void setRightMethodList(List rightMethodList) {
        this.rightMethodList = rightMethodList;
    }

    @Override
    public List<List> myItemsLists() throws Exception {
//        System.out.println(" ApplicationRights myItemsLists");
        List list = new ArrayList();
//        System.out.println("getGroupApplicationRightsList().size()"+ getGroupApplicationRightsList().size()) ;
        getGroupApplicationRightsList().size();
        getRightMethodList().size();
        list.add(getGroupApplicationRightsList());
        list.add(getRightMethodList());
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

    // Added by ThoTH @ 17-Mar-2011 -- TESTING Purpose on TthAction
//    private String FKID = "";
//    @Transient
//    public String getFKID() {
//        return FKID;
//    }
//
//    public void setFKID(String FKID) {
//        this.FKID = FKID;
//    }
    // END
}
