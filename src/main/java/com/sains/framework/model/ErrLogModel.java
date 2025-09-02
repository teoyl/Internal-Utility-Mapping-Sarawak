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
@Table(name="t_err_log")
public class ErrLogModel extends ModelBase implements java.io.Serializable {
    private String err_log_id;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String err_msg;
    private String err_type;
    private String solution_remark;
    private java.sql.Timestamp process_start_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Err_log_id","Err_msg","Err_type","Solution_remark"};


    private final String[] columnLength = new String[] {"err_msg:65535","err_type:20","solution_remark:200"};
    private Map columnLengthMap = null;

    public ErrLogModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="err_log_id")
    public String getErr_log_id() {
    	return err_log_id;
    }

    public void setErr_log_id(String err_log_id) {
    	this.err_log_id = err_log_id;
    }

    @Transient 
    public String getID() { 
        try {
            return err_log_id;
        } catch (Exception e) {
        }
        return ""; 
    } 

    public void setID(String pk_id) {
        this.err_log_id = pk_id;
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

    @Column(name="err_msg")
    @Type(type="text")
    public String getErr_msg() {
    	return err_msg;
    }

    public void setErr_msg(String err_msg) {
    	this.err_msg = err_msg;
    }

    @Column(name="err_type")
    public String getErr_type() {
    	return err_type;
    }

    public void setErr_type(String err_type) {
    	this.err_type = err_type;
    }

    @Column(name="solution_remark")
    public String getSolution_remark() {
    	return solution_remark;
    }

    public void setSolution_remark(String solution_remark) {
    	this.solution_remark = solution_remark;
    }

    @Column(name="process_start_date")
    public Timestamp getProcess_start_date() {
        return process_start_date;
    }

    public void setProcess_start_date(Timestamp process_start_date) {
        this.process_start_date = process_start_date;
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
    public void logMe(Timestamp processStartDate, String errType, BaseDAO dao, Exception exToLog) {
        try {
            dao.beginBatchTransaction();
            this.defaultAddProperties();
            this.setErr_type(errType);
            this.setProcess_start_date(processStartDate);
            StringBuffer buff = new StringBuffer();
            for (StackTraceElement element : exToLog.getStackTrace()) {
                buff.append(element.toString()).append("\n");
            }
            this.setErr_msg(exToLog.toString()+"\r\n"+buff.toString());
//            if (exToLog.getMessage() != null) {
//                System.out.println("1---------");
//                if (exToLog.getCause()!=null) {
//                    this.setErr_msg(exToLog.getMessage() + "/n" + "CAUSE: " + exToLog.getCause().getMessage());
//                } else {
//                    this.setErr_msg(exToLog.getMessage());
//                }
//            } else if (exToLog.getCause() != null) {
//                System.out.println("2---------");
//                this.setErr_msg("CAUSE: " + exToLog.getCause().getMessage());
//            }
            dao.getSession().save(this);
            dao.commitBatchTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollbackBatchTransaction();
        }
    }
}
