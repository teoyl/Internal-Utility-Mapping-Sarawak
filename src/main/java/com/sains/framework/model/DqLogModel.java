package com.sains.framework.model;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.ModelBase;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name="t_dq_log")
public class DqLogModel extends ModelBase implements java.io.Serializable {
    private Integer dq_log_id;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String dq_condition;
    private Integer tmpl_id;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Dq_log_id","Dq_condition","Tmpl_id"};


    private final String[] columnLength = new String[] {"dq_condition:500","qry_name:100"};
    private Map columnLengthMap = null;

    public DqLogModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dq_log_id")
    public Integer getDq_log_id() {
        return dq_log_id;
    }

    public void setDq_log_id(Integer dq_log_id) {
        this.dq_log_id = dq_log_id;
    }

    @Transient
    public String getID() {
        if (dq_log_id == null) {
            return null;
        }
        return dq_log_id.toString(); 
    }

    public void setID(String dq_log_id) {
        if (Validator.isEmpty(dq_log_id)) {
            setDq_log_id(null);
        } else {
            try {
                this.dq_log_id = Integer.parseInt(dq_log_id);
            } catch (Exception e) {
                setDq_log_id(null);
            }
        }
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

    @Column(name="dq_condition")
    public String getDq_condition() {
    	return dq_condition;
    }

    public void setDq_condition(String dq_condition) {
    	this.dq_condition = dq_condition;
    }

    @Column(name="tmpl_id")
    public Integer getTmpl_id() {
    	return tmpl_id;
    }

    public void setTmpl_id(Integer tmpl_id) {
    	this.tmpl_id = tmpl_id;
    }

    @Transient
    public String getTmpl_id_str() {
        return tmpl_id==null?"":Formatter.formatDecimal(tmpl_id.toString(), Formatter.CURRENCY_PATTERN_PLAIN2);
    }

    public void setTmpl_id_str(String tmpl_id) {
        try {
            this.tmpl_id = Integer.parseInt(tmpl_id.replaceAll(",", ""));
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

    /* ******** Write your code after this line ****** */

}
