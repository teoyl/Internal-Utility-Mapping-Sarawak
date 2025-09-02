package com.sains.framework.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.ModelBase;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author johnnuk
 */
@Entity
@Table(name = "t_audit_trail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuditLog.findAll", query = "SELECT a FROM AuditLog a")
    , @NamedQuery(name = "AuditLog.findByLogId", query = "SELECT a FROM AuditLog a WHERE a.logId = :logId")
    , @NamedQuery(name = "AuditLog.findByLogUserId", query = "SELECT a FROM AuditLog a WHERE a.logUserId = :logUserId")
    , @NamedQuery(name = "AuditLog.findByLogApplicantId", query = "SELECT a FROM AuditLog a WHERE a.logApplicantId = :logApplicantId")
    , @NamedQuery(name = "AuditLog.findByLogDateTime", query = "SELECT a FROM AuditLog a WHERE a.logDateTime = :logDateTime")
    , @NamedQuery(name = "AuditLog.findByLogTableName", query = "SELECT a FROM AuditLog a WHERE a.logTableName = :logTableName")
    , @NamedQuery(name = "AuditLog.findByLogAuditAction", query = "SELECT a FROM AuditLog a WHERE a.logAuditAction = :logAuditAction")
    , @NamedQuery(name = "AuditLog.findByLogRecordId", query = "SELECT a FROM AuditLog a WHERE a.logRecordId = :logRecordId")})
public class AuditLog extends ModelBase implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer logId;
    private Integer logUserId;
    private Integer logApplicantId;
    private Date logDateTime;
    private String logTableName;
    private String logAuditAction;
    private Integer logRecordId;
    private String logOldData;
    private String logNewData;

    private final String[] updatableColumns = new String[] {"Log_id","Log_applicant_id","Log_audit_action","Log_date_time","Log_new_data","Log_old_data","Log_record_id","Log_table_name","Log_user_id"};


    private final String[] columnLength = new String[] {"log_audit_action:10","log_new_data:65535","log_old_data:65535","log_table_name:40"};
    
    private Map columnLengthMap = null;

    
    public AuditLog() {
    }

    public AuditLog(Integer logId) {
        this.logId = logId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "log_id")
    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }
    
    @Transient
    public String getLog_id_str() {
        return logId==null?"":Formatter.formatDecimal(logId.toString(), Formatter.CURRENCY_PATTERN_PLAIN2);
    }

    public void setLog_id_str(String log_id) {
        try {
            this.logId = Integer.parseInt(log_id.replaceAll(",", ""));
        } catch (Exception e){}
    }

    @Transient 
    public String getID() { 
        if (logId == null) {
            return null;
        }
        return logId.toString(); 
    } 

    public void setID(Integer pk_id) {
        this.logId = pk_id;
    }

    @Column(name = "log_user_id")
    public Integer getLogUserId() {
        return logUserId;
    }

    public void setLogUserId(Integer logUserId) {
        this.logUserId = logUserId;
    }
    
    @Transient
    public String getLog_user_id_str() {
        return logUserId==null?"":Formatter.formatDecimal(logUserId.toString(), Formatter.CURRENCY_PATTERN_PLAIN2);
    }

    public void setLog_user_id_str(String log_user_id) {
        try {
            this.logUserId = Integer.parseInt(log_user_id.replaceAll(",", ""));
        } catch (Exception e){}
    }


    @Column(name = "log_applicant_id")
    public Integer getLogApplicantId() {
        return logApplicantId;
    }

    public void setLogApplicantId(Integer logApplicantId) {
        this.logApplicantId = logApplicantId;
    }
    
    @Transient
    public String getLog_applicant_id_str() {
        return logApplicantId==null?"":Formatter.formatDecimal(logApplicantId.toString(), Formatter.CURRENCY_PATTERN_PLAIN2);
    }

    public void setLog_applicant_id_str(String log_applicant_id) {
        try {
            this.logApplicantId = Integer.parseInt(log_applicant_id.replaceAll(",", ""));
        } catch (Exception e){}
    }


    @Column(name = "log_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLogDateTime() {
        return logDateTime;
    }

    public void setLogDateTime(Date logDateTime) {
        this.logDateTime = logDateTime;
    }
    
    @Transient
    public String getLog_date_time_str() {
        return Formatter.formatDate(logDateTime, SystemConstants.DATE.dataEntryFormat);    }

    public void setLog_date_time_str(String log_date_time) {
        try {
            this.logDateTime = DateUtil.getTimestampFromDate(DateUtil.getDate(log_date_time, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

    @Column(name = "log_table_name")
    public String getLogTableName() {
        return logTableName;
    }

    public void setLogTableName(String logTableName) {
        this.logTableName = logTableName;
    }

    @Column(name = "log_audit_action")
    public String getLogAuditAction() {
        return logAuditAction;
    }

    public void setLogAuditAction(String logAuditAction) {
        this.logAuditAction = logAuditAction;
    }

    @Column(name = "log_record_id")
    public Integer getLogRecordId() {
        return logRecordId;
    }

    public void setLogRecordId(Integer logRecordId) {
        this.logRecordId = logRecordId;
    }
    
    @Transient
    public String getLog_record_id_str() {
        return logRecordId==null?"":Formatter.formatDecimal(logRecordId.toString(), Formatter.CURRENCY_PATTERN_PLAIN2);
    }

    public void setLog_record_id_str(String log_record_id) {
        try {
            this.logRecordId = Integer.parseInt(log_record_id.replaceAll(",", ""));
        } catch (Exception e){}
    }

    @Lob
    @Column(name = "log_old_data")
    public String getLogOldData() {
        return logOldData;
    }

    public void setLogOldData(String logOldData) {
        this.logOldData = logOldData;
    }

    @Lob
    @Column(name = "log_new_data")
    public String getLogNewData() {
        return logNewData;
    }

    public void setLogNewData(String logNewData) {
        this.logNewData = logNewData;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logId != null ? logId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuditLog)) {
            return false;
        }
        AuditLog other = (AuditLog) object;
        if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sains.es.model.AuditLog[ logId=" + logId + " ]";
    }

    @Override
    public void setCreated_date(Timestamp createdDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUpdated_date(Timestamp updatedDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
