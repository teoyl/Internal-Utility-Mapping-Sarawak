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
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.ModelBase;
import java.sql.Timestamp;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.hibernate.annotations.Type;

@Entity
@Table(name="t_moal")
public class MoalModel extends ModelBase implements java.io.Serializable {
    private String moal_id;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String log_msg;
    private String log_cate;
    private java.sql.Timestamp start_date;
    private java.sql.Timestamp end_date;
    private String log_status;
    private String log_type;
    private String log_remark;
    private String err_log_id;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Moal_id","Log_msg","Log_cate","Start_date","End_date","Log_type","Log_remark", "Err_log_id"};


    private final String[] columnLength = new String[] {"log_msg:65535","log_type:20","log_remark:200"};
    private Map columnLengthMap = null;

    public MoalModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="moal_id")
    public String getMoal_id() {
    	return moal_id;
    }

    public void setMoal_id(String moal_id) {
    	this.moal_id = moal_id;
    }

    @Transient 
    public String getID() { 
        try {
            return moal_id;
        } catch (Exception e) {
        }
        return ""; 
    } 

    public void setID(String pk_id) {
        this.moal_id = pk_id;
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

    @Column(name="log_msg")
    public String getLog_msg() {
    	return log_msg;
    }

    public void setLog_msg(String log_msg) {
    	this.log_msg = log_msg;
    }
    
    @Column(name="log_cate")
    public String getLog_cate() {
    	return log_cate;
    }

    public void setLog_cate(String log_cate) {
    	this.log_cate    = log_cate;
    }

    @Column(name="start_date")
    public Timestamp getStart_date() {
        return start_date;
    }

    public void setStart_date(Timestamp start_date) {
        this.start_date = start_date;
    }

    @Column(name="end_date")
    public Timestamp getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Timestamp end_date) {
        this.end_date = end_date;
    }

    @Column(name="log_status")
    public String getLog_status() {
        return log_status;
    }

    public void setLog_status(String log_status) {
        this.log_status = log_status;
    }

    @Column(name="log_type")
    public String getLog_type() {
    	return log_type;
    }

    public void setLog_type(String log_type) {
    	this.log_type = log_type;
    }

    @Column(name="log_remark")
    public String getLog_remark() {
    	return log_remark;
    }

    public void setLog_remark(String log_remark) {
    	this.log_remark = log_remark;
    }

    @Column(name="err_log_id")
    public String getErr_log_id() {
        return err_log_id;
    }

    public void setErr_log_id(String err_log_id) {
        this.err_log_id = err_log_id;
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
    public void logMe(Timestamp processStartDate, String logType, String logCate, BaseDAO dao, String logMsg, String status, String errLogId) {
        try {
            dao.beginBatchTransaction();
            this.defaultAddProperties();
            this.setLog_type(logType);
            this.setLog_cate(logCate);
            this.setStart_date(processStartDate);
            this.setEnd_date(DateUtil.getCurrentTimestamp());
            this.setLog_msg(logMsg);
            this.setLog_status(status);
            this.setErr_log_id(errLogId);//not mandatory
            dao.getSession().save(this);
            dao.commitBatchTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollbackBatchTransaction();
        }
    }
}
