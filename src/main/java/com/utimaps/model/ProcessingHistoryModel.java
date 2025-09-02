/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.model;

import com.lxg.common.model.SetupCodeModel;
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
import javax.servlet.jsp.tagext.TryCatchFinally;

@Entity
@Table(name = "US_PROCESSING_HISTORY")
public class ProcessingHistoryModel extends ModelBase implements java.io.Serializable{
    private String history_id;
    private String case_id;
    private String process_code;
    private String process_by;
    private java.sql.Timestamp process_date;
    private Integer process_seq = 0;
    private String process_remarks;
    private String task_assign_to;
    private String cur_status;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private String process_code_str;//serene @ 24/6/2024
    private String process_date_str;//serene @ 24/6/2024

    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{};
    
    public String[] columnLength = new String[]{};

    @Id
    @Column(name = "history_id")
    public String getHistory_id() {
        return history_id;
    }

    public void setHistory_id(String history_id) {
        this.history_id = history_id;
    }

    @Column(name = "case_id")
    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    @Column(name = "process_code")
    public String getProcess_code() {
        return process_code;
    }

    public void setProcess_code(String process_code) {
        this.process_code = process_code;
    }

    @Column(name = "process_by")
    public String getProcess_by() {
        return process_by;
    }

    public void setProcess_by(String process_by) {
        this.process_by = process_by;
    }

    @Column(name = "process_date")
    public Timestamp getProcess_date() {
        return process_date;
    }

    public void setProcess_date(Timestamp process_date) {
        this.process_date = process_date;
    }

    @Column(name = "process_seq")
    public Integer getProcess_seq() {
        return process_seq;
    }

    public void setProcess_seq(Integer process_seq) {
        this.process_seq = process_seq;
    }

    @Column(name = "process_remarks")
    public String getProcess_remarks() {
        return process_remarks;
    }

    public void setProcess_remarks(String process_remarks) {
        this.process_remarks = process_remarks;
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
    
    @Transient
    public String getProcess_code_str() {
        BaseDAOImpl retrivalDAO = new BaseDAOImpl();
        try {
//            SetupCodeModel codeModel =(SetupCodeModel) retrivalDAO.getModelByCode("code_type,code_1", "JBS,"+this.process_code, new SetupCodeModel() );
            SetupCodeModel codeModel =(SetupCodeModel) retrivalDAO.getModelByCode("code_type,code_1", "JWS,"+this.process_code, new SetupCodeModel() );
            if(codeModel!=null){
                process_code_str = codeModel.getCode_desc();
            }
            
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "ProcessingHistoryModel", "ProcessingHistoryModel", "getProcess_code_str");
        }finally{
            retrivalDAO.closeSession();
        }
        return process_code_str;
    }

    public void setProcess_code_str(String process_code_str) {
        this.process_code_str = process_code_str;
    }

    @Transient
    public String getProcess_date_str() {
           return Formatter.formatDate(process_date, SystemConstants.DATE.dataTimeAmPmEntryFormat); 
    }

    public void setProcess_date_str(String process_date_str) {
        this.process_date_str = process_date_str;
    }
  
    
}
