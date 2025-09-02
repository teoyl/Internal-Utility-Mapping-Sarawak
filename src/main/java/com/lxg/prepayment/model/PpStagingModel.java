/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lxg.prepayment.model;

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
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.ModelBase;

@Entity
@Table(name="PP_STAGING")
public class PpStagingModel extends ModelBase implements java.io.Serializable {
    private String stg_id = "";
    private java.sql.Timestamp stg_date;
    private Double stg_amount;
    private String stg_type;
    private String stg_account_type;
    private String stg_shortkey;
    private String stg_desc;
    private String stg_status;
    private String stg_error;
    private String stg_div;


    private java.sql.Timestamp created_date;
    private String created_by;
    private java.sql.Timestamp updated_date;
    private String updated_by;

    public String[] updatableColumns = new String[] {"Stg_id","Stg_date","Stg_amount","Stg_type","Stg_account_type","Stg_shortkey","Stg_desc","Stg_status","Stg_div"};


    private final String[] columnLength = new String[] {"stg_id:16","stg_type:3","stg_account_type:1","stg_shortkey:20","stg_desc:20","stg_status:1","stg_error:1","stg_div:2"};
    private Map columnLengthMap = null;

    public PpStagingModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="stg_id")
    public String getStg_id() {
    	return stg_id;
    }

    public void setStg_id(String stg_id) {
    	this.stg_id = stg_id;
    }

    @Transient 
    public String getID() { 
        return stg_id; 
    } 

    public void setID(String pk_id) {
        this.stg_id = pk_id;
    }

    @Column(name="stg_date")
    public java.sql.Timestamp getStg_date() {
    	return stg_date;
    }

    public void setStg_date(java.sql.Timestamp stg_date) {
    	this.stg_date = stg_date;
    }

    @Transient
    public String getStg_date_str() {
        return Formatter.formatDate(stg_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setStg_date_str(String stg_date) {
        try {
            this.stg_date = DateUtil.getTimestampFromDate(DateUtil.getDate(stg_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

    @Column(name="stg_amount")
    public Double getStg_amount() {
    	return stg_amount;
    }

    public void setStg_amount(Double stg_amount) {
    	this.stg_amount = stg_amount;
    }

    @Column(name="stg_type")
    public String getStg_type() {
    	return stg_type;
    }

    public void setStg_type(String stg_type) {
    	this.stg_type = stg_type;
    }

    @Column(name="stg_account_type")
    public String getStg_account_type() {
    	return stg_account_type;
    }

    public void setStg_account_type(String stg_account_type) {
    	this.stg_account_type = stg_account_type;
    }

    @Column(name="stg_shortkey")
    public String getStg_shortkey() {
    	return stg_shortkey;
    }

    public void setStg_shortkey(String stg_shortkey) {
    	this.stg_shortkey = stg_shortkey;
    }

    @Column(name="stg_desc")
    public String getStg_desc() {
    	return stg_desc;
    }

    public void setStg_desc(String stg_desc) {
    	this.stg_desc = stg_desc;
    }

    @Column(name="stg_status")
    public String getStg_status() {
    	return stg_status;
    }

    public void setStg_status(String stg_status) {
    	this.stg_status = stg_status;
    }

    // ai min @ 12.7.2011
    @Column(name="stg_error")
    public String getStg_error() {
    	return stg_error;
    }

    public void setStg_error(String stg_error) {
    	this.stg_error = stg_error;
    }

    @Column(name="stg_div")
    public String getStg_div() {
        return stg_div;
    }

    public void setStg_div(String stg_div) {
        this.stg_div = stg_div;
    }

    @Column(name="created_date")
    public java.sql.Timestamp getCreated_date() {
    	return created_date;
    }

    public void setCreated_date(java.sql.Timestamp created_date) {
    	this.created_date = created_date;
    }

    @Transient
    public String getCreated_date_str() {
        return Formatter.formatDate(created_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setCreated_date_str(String created_date) {
        try {
            this.created_date = DateUtil.getTimestampFromDate(DateUtil.getDate(created_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
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

    @Transient
    public String getUpdated_date_str() {
        return Formatter.formatDate(updated_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setUpdated_date_str(String updated_date) {
        try {
            this.updated_date = DateUtil.getTimestampFromDate(DateUtil.getDate(updated_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

    @Column(name="updated_by")
    public String getUpdated_by() {
    	return updated_by;
    }

    public void setUpdated_by(String updated_by) {
    	this.updated_by = updated_by;
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

