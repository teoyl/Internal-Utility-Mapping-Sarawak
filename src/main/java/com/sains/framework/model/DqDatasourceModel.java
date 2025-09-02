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
@Table(name="t_dq_datasource")
public class DqDatasourceModel extends ModelBase implements java.io.Serializable {
    private Integer ds_id;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String ds_desc;
    private String ds_view_name;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Ds_id","Ds_desc","Ds_view_name"};


    private final String[] columnLength = new String[] {"ds_desc:200","ds_view_name:50"};
    private Map columnLengthMap = null;

    public DqDatasourceModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ds_id")
    public Integer getDs_id() {
        return ds_id;
    }

    public void setDs_id(Integer ds_id) {
        this.ds_id = ds_id;
    }

    @Transient
    public String getID() {
        if (ds_id == null) {
            return null;
        }
        return ds_id.toString(); 
    }

    public void setID(String ds_id) {
        if (Validator.isEmpty(ds_id)) {
            setDs_id(null);
        } else {
            try {
                this.ds_id = Integer.parseInt(ds_id);
            } catch (Exception e) {
                setDs_id(null);
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

    @Column(name="ds_desc")
    public String getDs_desc() {
    	return ds_desc;
    }

    public void setDs_desc(String ds_desc) {
    	this.ds_desc = ds_desc;
    }

    @Column(name="ds_view_name")
    public String getDs_view_name() {
    	return ds_view_name;
    }

    public void setDs_view_name(String ds_view_name) {
    	this.ds_view_name = ds_view_name;
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
