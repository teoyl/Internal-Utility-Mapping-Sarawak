package com.sains.framework.model;

import com.sains.framework.base.ModelBase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_setup_app_right_method")
public class RightMethod extends ModelBase implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String right_method_id = "";
    private String app_rights_id = "";
    private String method_str = "";
    private ApplicationRights applicationRights;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private final String[] updatableColumns = new String[]{"App_rights_id","Method_str"};

    @Id
    @Column(name = "right_method_id")
    public String getRight_method_id() {
        return right_method_id;
    }

    public void setRight_method_id(String right_method_id) {
        this.right_method_id = right_method_id;
    }

    @Transient
    public String getID() {
        return getRight_method_id();
    }

    public void setID(String right_method_id) {
        setRight_method_id(right_method_id);
    }

    @Column(name = "app_rights_id")
    public String getApp_rights_id() {
        return app_rights_id;
    }

    public void setApp_rights_id(String appRightsId) {
        app_rights_id = appRightsId;
    }

    @Column(name = "method_str")
    public String getMethod_str() {
        return method_str;
    }

    public void setMethod_str(String method_str) {
        this.method_str = method_str;
    }

    @ManyToOne(targetEntity = ApplicationRights.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "app_rights_id", insertable = false, updatable = false, nullable = true)
    public ApplicationRights getApplicationRights() {
        return applicationRights;
    }

    public void setApplicationRights(ApplicationRights applicationRights) {
        this.applicationRights = applicationRights;
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
//    @Override
//    public List<List> myItemsLists() throws Exception {
//        System.out.println(" Rights Method myItemsLists");
//        List list = new ArrayList();
//
//        return null;
//    }

    
}
