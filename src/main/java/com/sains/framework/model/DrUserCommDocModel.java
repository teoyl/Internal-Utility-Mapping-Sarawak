package com.sains.framework.model;

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
@Table(name="t_dr_user_comm_doc")
public class DrUserCommDocModel extends ModelBase implements java.io.Serializable {
    private String comm_doc_id = "";
    private String created_by;
    private java.sql.Timestamp created_date;
    private String current_dr_id;
    private String doc_code_id;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private String user_id;

    private final String[] updatableColumns = new String[] {"Comm_doc_id","Created_date","Current_dr_id","Doc_code_id","User_id"};


    private final String[] columnLength = new String[] {"comm_doc_id:20","current_dr_id:20","doc_code_id:20","user_id:20"};
    private Map columnLengthMap = null;

    public DrUserCommDocModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="comm_doc_id")
    public String getComm_doc_id() {
    	return comm_doc_id;
    }

    public void setComm_doc_id(String comm_doc_id) {
    	this.comm_doc_id = comm_doc_id;
    }

    @Transient 
    public String getID() { 
        return comm_doc_id; 
    } 

    public void setID(String pk_id) {
        this.comm_doc_id = pk_id;
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

    @Column(name="current_dr_id")
    public String getCurrent_dr_id() {
    	return current_dr_id;
    }

    public void setCurrent_dr_id(String current_dr_id) {
    	this.current_dr_id = current_dr_id;
    }

    @Column(name="doc_code_id")
    public String getDoc_code_id() {
    	return doc_code_id;
    }

    public void setDoc_code_id(String doc_code_id) {
    	this.doc_code_id = doc_code_id;
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

    @Column(name="user_id")
    public String getUser_id() {
    	return user_id;
    }

    public void setUser_id(String user_id) {
    	this.user_id = user_id;
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
