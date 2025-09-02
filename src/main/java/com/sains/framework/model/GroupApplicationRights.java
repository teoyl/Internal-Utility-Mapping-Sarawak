package com.sains.framework.model;

import com.sains.common.util.Validator;
import com.sains.framework.base.ModelBase;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_setup_group_app_right")
public class GroupApplicationRights extends ModelBase implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private String ug_app_right_id = "";
    private String ug_app_id = "";
    private String app_rights_id = "";
    private String hasRight = "N";
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private ApplicationRights applicationRights;
    private final String[] updatableColumns = new String[] {"Ug_app_right_id", "Ug_app_id", "App_rights_id", "HasRight", 
                                                                                                                                            "Updated_by", "Updated_date"};
    @Id
    @Column(name="ug_app_right_id")
    public String getUg_app_right_id() {
            return ug_app_right_id;
    }
    public void setUg_app_right_id(String ugAppRightId) {
            ug_app_right_id = ugAppRightId;
    }

    @Transient
    public String getID() {
            return getUg_app_right_id();
    }

    public void setID(String ugAppRightId) {
            setUg_app_right_id(ugAppRightId);
    }

    @Column(name="ug_app_id")
    public String getUg_app_id() {
            return ug_app_id;
    }
    public void setUg_app_id(String ugAppId) {
            ug_app_id = ugAppId;
    }

    @Column(name="app_rights_id")
    public String getApp_rights_id() {
            return app_rights_id;
    }
    public void setApp_rights_id(String appRightsId) {
            app_rights_id = appRightsId;
    }

    @Column(name="hasRight")
    public String getHasRight() {
            return hasRight;
    }
    public void setHasRight(String hasRight) {
        if (Validator.isEmpty(hasRight) || hasRight.equalsIgnoreCase("false")){
            this.hasRight = "N";
        } else {
            this.hasRight = hasRight;
        }
    }

    @OneToOne(targetEntity=ApplicationRights.class, optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="app_rights_id", insertable=false, updatable=false, nullable = true)
    public ApplicationRights getApplicationRights() {
            return applicationRights;
    }
    public void setApplicationRights(ApplicationRights applicationRights) {
            this.applicationRights = applicationRights;
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
    public java.util.Date getUpdated_date() {
            return updated_date;
    }
    public void setUpdated_date(java.sql.Timestamp updatedDate) {
            updated_date = updatedDate;
    }

    @Transient
    public String[] getUpdatableColumns() {
            return updatableColumns;
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