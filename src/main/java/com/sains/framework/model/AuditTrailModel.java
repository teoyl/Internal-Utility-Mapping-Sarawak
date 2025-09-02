package com.sains.framework.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.sql.Timestamp;
import java.util.Collections;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name="t_audit_trail")
@NamedQueries({
    @NamedQuery(name = "AuditTrailModel.findBy_tableName",query = "from AuditTrailModel where table_name = :tableName order by record_id, date_time desc")
})
public class AuditTrailModel extends ModelBase implements java.io.Serializable {
    private Integer audit_id;
    private String audit_action;
    private String column_name;
    private java.sql.Timestamp date_time;
    private String field_desc;
    private String new_data;
    private String old_data;
    private String record_id;
    private String table_name;
    private String user_id;

//    private final String[] updatableColumns = new String[] {"Audit_id","Audit_action","Column_name","Date_time","Field_desc","New_data","Old_data","Record_id","Table_name","User_id"};
    private final String[] updatableColumns = new String[] {"Date_time"};


    private final String[] columnLength = new String[] {"audit_action:10","column_name:30","field_desc:5000","new_data:4000","old_data:4000","record_id:20","table_name:50","user_id:65"};
    private Map columnLengthMap = null;

    public AuditTrailModel() {
//        useSQLUpdate(Boolean.TRUE);
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="audit_id")
    public Integer getAudit_id() {
    	return audit_id;
    }

    public void setAudit_id(Integer audit_id) {
    	this.audit_id = audit_id;
    }

    @Transient
    public String getAudit_id_str() {
        return audit_id==null?"":Formatter.formatDecimal(audit_id.toString(), Formatter.CURRENCY_PATTERN_PLAIN2);
    }

    public void setAudit_id_str(String audit_id) {
        try {
            this.audit_id = Integer.parseInt(audit_id.replaceAll(",", ""));
        } catch (Exception e){}
    }

    @Transient 
    public String getID() { 
        return audit_id.toString(); 
    } 

    public void setID(String pk_id) {
        this.audit_id = Integer.parseInt(pk_id);
    }

    @Column(name="audit_action")
    public String getAudit_action() {
    	return audit_action;
    }

    public void setAudit_action(String audit_action) {
    	this.audit_action = audit_action;
    }

    @Column(name="column_name")
    public String getColumn_name() {
    	return column_name;
    }

    public void setColumn_name(String column_name) {
    	this.column_name = column_name;
    }

    @Column(name="date_time")
    public java.sql.Timestamp getDate_time() {
    	return date_time;
    }

    public void setDate_time(java.sql.Timestamp date_time) {
    	this.date_time = date_time;
    }

    @Transient
    public String getDate_time_str() {
        return Formatter.formatDate(date_time, SystemConstants.DATE.dataTimeEntryFormat);    
    }

    public void setDate_time_str(String date_time) {
        try {
            this.date_time = DateUtil.getTimestampFromDate(DateUtil.getDate(date_time, SystemConstants.DATE.dataTimeEntryFormat));
        } catch (Exception e){}
    }

    @Column(name="field_desc")
    public String getField_desc() {
    	return field_desc;
    }

    public void setField_desc(String field_desc) {
    	this.field_desc = field_desc;
    }

    @Column(name="new_data")
    public String getNew_data() {
    	return new_data;
    }

    public void setNew_data(String new_data) {
    	this.new_data = new_data;
    }

    @Column(name="old_data")
    public String getOld_data() {
    	return old_data;
    }

    public void setOld_data(String old_data) {
    	this.old_data = old_data;
    }

    @Column(name="record_id")
    public String getRecord_id() {
    	return record_id;
    }

    public void setRecord_id(String record_id) {
    	this.record_id = record_id;
    }

    @Column(name="table_name")
    public String getTable_name() {
    	return table_name;
    }

    public void setTable_name(String table_name) {
    	this.table_name = table_name;
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

    @Override
    public void setCreated_date(Timestamp createdDate) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUpdated_date(Timestamp updatedDate) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Map newDataMap = null;
    private Map oldDataMap = null;
    private static Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    public Map newDataMap() {
        if (newDataMap == null) {
            newDataMap = gson.fromJson(getNew_data(), Map.class);
        }
        return newDataMap;
    }
    public void setNewDataMap(Map map) {
        newDataMap = map;
    }
    public Map oldDataMap() {
        if (oldDataMap == null) {
            oldDataMap = gson.fromJson(getOld_data(), Map.class);
        }
        return oldDataMap;
    }
    
    public Object getNewData(String colName) {
        if (newDataMap()==null) return null;
        Object obj = newDataMap().get(colName);
        if (obj == null) {
            return "";
        }
        return obj;
    }
    public Object getOldData(String colName) {
        if (oldDataMap()==null) return null;
        Object obj = oldDataMap().get(colName);
        if (obj == null) {
            return "";
        }
        return obj;
    }
    
    private Boolean matchCriteria = Boolean.FALSE;
    @Transient
    public Boolean getMatchCriteria() {
        return matchCriteria;
    }
    public void setMatchCriteria(Boolean matchCriteria) {
        this.matchCriteria = matchCriteria;
    }
    @Transient
    public String getMatchClass() {
        if (getDate_time() == null) {
            return "current";
        }
        if (matchCriteria) {
            return "matched";
        } else {
            return "notMatched";
        }
    }
    
    public String defaultUpdatedBy() {
        return "";
    }
    public String defaultUpdatedDate() {
        return "";
    }
}
