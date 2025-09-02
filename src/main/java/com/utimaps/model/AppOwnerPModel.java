/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.model;

import com.sains.framework.base.ModelBase;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "US_APP_OWNER_P")
public class AppOwnerPModel extends ModelBase implements java.io.Serializable{
    private String app_owner_id;
    private String case_id;
    private String utility_owner;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{"case_id","app_owner_id","utility_owner","created_by","created_date","updated_by","updated_date"};
    
    public String[] columnLength = new String[]{"case_id:20:","app_owner_id:20:","utility_owner:20:","created_by:20","updated_by:20"};
    
    @Id
    @Column(name = "app_owner_id")
    public String getApp_owner_id() {
        return app_owner_id;
    }

    public void setApp_owner_id(String app_owner_id) {
        this.app_owner_id = app_owner_id;
    }
    
    @Column(name = "case_id")
    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    @Column(name = "utility_owner")
    public String getUtility_owner() {
        return utility_owner;
    }

    public void setUtility_owner(String utility_owner) {
        this.utility_owner = utility_owner;
    }

    public void setColumnLength(String[] columnLength) {
        this.columnLength = columnLength;
    }
    
    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Column(name = "created_date")
    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    @Column(name = "updated_by")
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    @Column(name = "updated_date")
    public Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Timestamp updated_date) {
        this.updated_date = updated_date;
    }
      
    public String[] getColumnLength() {
        return columnLength;
    }

    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }
    
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
