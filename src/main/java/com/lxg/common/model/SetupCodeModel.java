/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lxg.common.model;

import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.ModelBase;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 *
 * @author seren
 */
@Entity
@Table(name="T_SETUP_CODE")
@NamedQueries({
    @NamedQuery(name = "SetupCodeModel.filterCode1_orderCodeDesc",query = "from SetupCodeModel where code_type = :type and code_1 = :code1 and active = 'Y' order by code_desc ")
})
public class SetupCodeModel extends ModelBase implements java.io.Serializable {
    private String code_id = "";
    private String code_type;
    private String code_1;
    private String code_2;
    private String code_3;
    private String code_4;
    private String code_acr;
    private String code_desc;
    private String code_map;
    private String active = "Y";
    private String code_reject_remark;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    public String[] updatableColumns = new String[] {"Code_id","Code_type","Code_1","Code_2","Code_3","Code_4","Code_acr","Code_desc","Active"};


    private final String[] columnLength = new String[] {"code_id:16","code_type:3","code_1:16","code_2:16","code_3:16","code_4:10","code_acr:80","code_desc:150","active:1"};
    private Map columnLengthMap = null;

    public SetupCodeModel() {
//        userDefined_autoValidation(Boolean.TRUE);
//        if(this.get_operation().equals("code_1")){
//            userDefined_insertNoDuplicate("code_type;module.code,code_1;setupcode.parentCode");
//        }else{
//            userDefined_insertNoDuplicate("code_type;module.code,code_1;setupcode.parentCode,code_2;setupcode.parentCode");
//        }
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="code_id")
    public String getCode_id() {
    	return code_id;
    }

    public void setCode_id(String code_id) {
    	this.code_id = code_id;
    }

    @Transient 
    public String getID() { 
        return code_id; 
    } 

    public void setID(String pk_id) {
        this.code_id = pk_id;
    }

    @Column(name="code_type")
    public String getCode_type() {
    	return code_type;
    }

    public void setCode_type(String code_type) {
    	this.code_type = code_type;
    }

    @Column(name="code_1")
    public String getCode_1() {
    	return code_1;
    }

    public void setCode_1(String code_1) {
    	this.code_1 = code_1;
    }

    @Column(name="code_2")
    public String getCode_2() {
    	return code_2;
    }

    public void setCode_2(String code_2) {
    	this.code_2 = code_2;
    }

    @Column(name="code_3")
    public String getCode_3() {
    	return code_3;
    }

    public void setCode_3(String code_3) {
    	this.code_3 = code_3;
    }

    @Column(name="code_4")
    public String getCode_4() {
    	return code_4;
    }

    public void setCode_4(String code_4) {
    	this.code_4 = code_4;
    }

    @Column(name="code_acr")
    public String getCode_acr() {
    	return code_acr;
    }

    public void setCode_acr(String code_acr) {
    	this.code_acr = code_acr;
    }

    @Column(name="code_desc")
    public String getCode_desc() {
    	return code_desc;
    }

    public void setCode_desc(String code_desc) {
    	this.code_desc = code_desc;
    }

    @Column(name="code_map")
    public String getCode_map() {
        return code_map;
    }

    public void setCode_map(String code_map) {
        this.code_map = code_map;
    }

    @Column(name="active")
    public String getActive() {
    	return active;
    }

    public void setActive(String active) {
    	this.active = active;
    }

    @Column(name="code_reject_remark")
    public String getCode_reject_remark() {
        return code_reject_remark;
    }

    public void setCode_reject_remark(String code_reject_remark) {
        this.code_reject_remark = code_reject_remark;
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

    @Transient
    public String getCreated_date_str() {
        return Formatter.formatDate(created_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setCreated_date_str(String created_date) {
        try {
            this.created_date = DateUtil.getTimestampFromDate(DateUtil.getDate(created_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
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
    public String getUpdated_date_str() {
        return Formatter.formatDate(updated_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setUpdated_date_str(String updated_date) {
        try {
            this.updated_date = DateUtil.getTimestampFromDate(DateUtil.getDate(updated_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
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
