/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.model;

import com.lxg.common.model.SetupCodeModel;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.ModelBase;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "US_JOB_STATUS")
public class JobStatusModel extends ModelBase implements java.io.Serializable{
    private String status_id;
    private String case_id;
    private String status_code;
    private String status_by;
    private java.sql.Timestamp status_date;
    private Integer status_seq = 0;
    private String status_remarks;
    private String task_assign_to;
    private String cur_status;
    private String created_by;
    private String created_by_str;
    private String status_by_str;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private String status_date_str;
    private String status_date_str2;
    private String status_code_str;
    
    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{};
    
    public String[] columnLength = new String[]{};

    @Id
    @Column(name = "status_id")
    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    @Column(name = "case_id")
    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    @Column(name = "status_code")
    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    @Column(name = "status_by")
    public String getStatus_by() {
        return status_by;
    }

    public void setStatus_by(String status_by) {
        this.status_by = status_by;
    }

    @Column(name = "status_date")
    public Timestamp getStatus_date() {
        return status_date;
    }

    public void setStatus_date(Timestamp status_date) {
        this.status_date = status_date;
    }

    @Column(name = "status_seq")
    public Integer getStatus_seq() {
        return status_seq;
    }

    public void setStatus_seq(Integer status_seq) {
        this.status_seq = status_seq;
    }

    @Column(name = "status_remarks")
    public String getStatus_remarks() {
        return status_remarks;
    }

    public void setStatus_remarks(String status_remarks) {
        this.status_remarks = status_remarks;
    }

    @Column(name = "task_assign_to")
    public String getTask_assign_to() {
        return task_assign_to;
    }

    public void setTask_assign_to(String task_assign_to) {
        this.task_assign_to = task_assign_to;
    }

    @Column(name = "cur_status")
    public String getCur_status() {
        return cur_status;
    }

    public void setCur_status(String cur_status) {
        this.cur_status = cur_status;
    }
    
    public void setColumnLength(String[] columnLength) {
        this.columnLength = columnLength;
    }
    
    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Column(name = "created_date")
    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    @Column(name = "updated_by")
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    @Column(name = "updated_date")
    public Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Timestamp updated_date) {
        this.updated_date = updated_date;
    }
      
    @Transient
    public String[] getColumnLength() {
        return columnLength;
    }
    
    @Transient
    public String getStatus_date_str() {
        return Formatter.formatDate(status_date, SystemConstants.DATE.dataEntryFormat3);
    }

    public void setStatus_date_str(String status_date) {
        try {
            this.status_date = DateUtil.getTimestampFromDate(DateUtil.getDate(status_date, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "JobStatusModel", "JobStatusModel", "setStatus_date_str");
        }
    }
    
    @Transient
    public String getStatus_date_str2() {
        return Formatter.formatDate(status_date, SystemConstants.DATE.activityLogDateTimeFormat);
    }

    public void setStatus_date_str2(String status_date) {
        try {
            this.status_date = DateUtil.getTimestampFromDate(DateUtil.getDate(status_date, SystemConstants.DATE.activityLogDateTimeFormat));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "JobStatusModel", "JobStatusModel", "setStatus_date_str2");
        }
    }

    @Transient
    public String getStatus_code_str() {
        BaseDAOImpl retrivalDAO = new BaseDAOImpl();
        try {
            SetupCodeModel codeModel =(SetupCodeModel) retrivalDAO.getModelByCode("code_type,code_1", "JWS,"+this.status_code, new SetupCodeModel() );
            if(codeModel!=null){
                status_code_str = codeModel.getCode_desc();
            } else {
                status_code_str ="-";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            retrivalDAO.closeSession();
        }
        return status_code_str;
    }

    public void setStatus_code_str(String status_code_str) {
        this.status_code_str = status_code_str;
    }
    
    @Transient
    public String getCreated_by_str() {
        CommonFunction cf = new CommonFunction();
        created_by_str = cf.getUserName(created_by);
        return created_by_str;
    }

    public void setCreated_by_str(String created_by_str) {
        this.created_by_str = created_by_str;
    }

    @Transient
    public String getStatus_by_str() {
        CommonFunction cf = new CommonFunction();
        status_by_str = cf.getUserName(status_by);
        return status_by_str;
    }

    public void setStatus_by_str(String status_by_str) {
        this.status_by_str = status_by_str;
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

