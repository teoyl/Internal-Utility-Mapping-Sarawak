/* ----------------------------------------------
   NAME   : SetupSchedulerModel.java 
   CREATED BY  : Java Model Generator                   
   CREATED Date: 24-JANUARY  -2011                   
   UPDATED BY  :                                
   UPDATED Date:                    
 ------------------------------------------------*/



package com.backend;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_setup_scheduler")
public class SetupSchedulerModel implements java.io.Serializable {
    private String sc_id = "";
    private String sc_code;
    private String sc_desc;
    private Integer sc_interval;
    private String sc_time;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Sc_id","Sc_code","Sc_desc","Sc_interval","Sc_time"};


    private final String[] columnLength = new String[] {"sc_id:16","sc_code:50","sc_desc:100","sc_time:4"};
    private Map columnLengthMap = null;


    @Id
    @Column(name="sc_id")
    public String getSc_id() {
    	return sc_id;
    }

    public void setSc_id(String sc_id) {
    	this.sc_id = sc_id;
    }

    @Transient 
    public String getID() { 
        return sc_id; 
    } 

    public void setID(String pk_id) {
        this.sc_id = pk_id;
    }

    @Column(name="sc_code")
    public String getSc_code() {
    	return sc_code;
    }

    public void setSc_code(String sc_code) {
    	this.sc_code = sc_code;
    }

    @Column(name="sc_desc")
    public String getSc_desc() {
    	return sc_desc;
    }

    public void setSc_desc(String sc_desc) {
    	this.sc_desc = sc_desc;
    }

    @Column(name="sc_interval")
    public Integer getSc_interval() {
    	return sc_interval;
    }

    public void setSc_interval(Integer sc_interval) {
    	this.sc_interval = sc_interval;
    }

    @Column(name="sc_time")
    public String getSc_time() {
    	return sc_time;
    }

    public void setSc_time(String sc_time) {
    	this.sc_time = sc_time;
    }

    @Column(name="created_by")
    public String getCreated_by() {
    	return created_by;
    }

    public void setCreated_by(String created_by) {
    	this.created_by = created_by;
    }

    @Column(name="created_date")
    public java.sql.Timestamp getCreated_date() {
    	return created_date;
    }

    public void setCreated_date(java.sql.Timestamp created_date) {
    	this.created_date = created_date;
    }

    @Column(name="updated_by")
    public String getUpdated_by() {
    	return updated_by;
    }

    public void setUpdated_by(String updated_by) {
    	this.updated_by = updated_by;
    }

    @Column(name="updated_date")
    public java.sql.Timestamp getUpdated_date() {
    	return updated_date;
    }

    public void setUpdated_date(java.sql.Timestamp updated_date) {
    	this.updated_date = updated_date;
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

    /* ******** Write your code after this line ****** */

}
