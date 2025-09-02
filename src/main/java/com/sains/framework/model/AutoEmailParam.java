/* ----------------------------------------------
   NAME   : AutoEmailParam.java 
   CREATED BY  : Java Model Generator                   
   CREATED Date: 02-JULY     -2010                   
   UPDATED BY  :                                
   UPDATED Date:                    
 ------------------------------------------------*/



package com.sains.framework.model;

import com.sains.framework.base.ModelBase;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
@Table(name="t_setup_autoemail_param")
public class AutoEmailParam extends ModelBase implements java.io.Serializable {
    private String param_id = "";
    private String param_name;
    private String retrieve_from;
    private java.sql.Timestamp created_date;
    private String created_by;
    private java.sql.Timestamp updated_date;
    private String updated_by;
    private String auto_email_id;

    private final String[] updatableColumns = new String[] {"Param_id","Param_name","Retrieve_from","Auto_email_id"};


    @Id
    @Column(name="param_id")
    public String getParam_id() {
    	return param_id;
    }

    public void setParam_id(String param_id) {
    	this.param_id = param_id;
    }

    @Transient 
    public String getID() { 
        return param_id; 
    } 

    public void setID(String pk_id) {
        this.param_id = pk_id;
    }

    @Column(name="param_name")
    public String getParam_name() {
    	return param_name;
    }

    public void setParam_name(String param_name) {
    	this.param_name = param_name;
    }

    @Column(name="retrieve_from")
    public String getRetrieve_from() {
    	return retrieve_from;
    }

    public void setRetrieve_from(String retrieve_from) {
    	this.retrieve_from = retrieve_from;
    }

    @Column(name="created_date")
    public java.sql.Timestamp getCreated_date() {
    	return created_date;
    }

    public void setCreated_date(java.sql.Timestamp created_date) {
    	this.created_date = created_date;
    }

    @Column(name="created_by")
    public String getCreated_by() {
    	return created_by;
    }

    public void setCreated_by(String created_by) {
    	this.created_by = created_by;
    }

    @Column(name="updated_date")
    public java.sql.Timestamp getUpdated_date() {
    	return updated_date;
    }

    public void setUpdated_date(java.sql.Timestamp updated_date) {
    	this.updated_date = updated_date;
    }

    @Column(name="updated_by")
    public String getUpdated_by() {
    	return updated_by;
    }

    public void setUpdated_by(String updated_by) {
    	this.updated_by = updated_by;
    }

    @Column(name="auto_email_id")
    public String getAuto_email_id() {
    	return auto_email_id;
    }

    public void setAuto_email_id(String auto_email_id) {
    	this.auto_email_id = auto_email_id;
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

    /* ******** Write your code after this line ****** */

}
