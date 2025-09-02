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
import java.sql.Timestamp;

@Entity
@Table(name="t_email_queue")
public class EmailQueueModel extends ModelBase implements java.io.Serializable {
    private String email_queue_id = "";
    private String created_by;
    private java.sql.Timestamp created_date;
    private String email_sender;
    private String bcc;
    private String cc;
    private String sms_result;
    private String sms_message_id;
    private String email_content;
    private String email_subject;
    private Integer fail_count;
    private String recipient;
    private java.sql.Timestamp sent_date;
    private java.sql.Timestamp resend_date;
    private String notification_type;
    private String smtp;
    private String err_log_id;
    private java.sql.Timestamp process_start_date;
    private String queue_type;
    private String send_status;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Email_queue_id","Email_sender","Email_content","Email_subject","Fail_count","Queue_type","Send_status", "sent_date", "resend_date", "Sms_result", "Sms_message_id", "Smtp", "Process_start_date", "Err_log_id"};


    private final String[] columnLength = new String[] {"email_queue_id:20","email_content:2000","email_subject:150","queue_type:2","send_status:2"};
    private Map columnLengthMap = null;

    public EmailQueueModel() {
//        setupMyChildList("getRecipientList", EmailQueueRecipientModel.class, "recipientDeleted", Boolean.TRUE);
        setupMyChildList("getAttachmentList", EmailQueueAttachmentModel.class, "attachmentDeleted", Boolean.TRUE);
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="email_queue_id")
    public String getEmail_queue_id() {
    	return email_queue_id;
    }

    public void setEmail_queue_id(String email_queue_id) {
    	this.email_queue_id = email_queue_id;
    }

    @Transient 
    public String getID() { 
        return email_queue_id; 
    } 

    public void setID(String pk_id) {
        this.email_queue_id = pk_id;
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

    @Column(name="email_sender")
    public String getEmail_sender() {
        return email_sender;
    }

    public void setEmail_sender(String email_sender) {
        this.email_sender = email_sender;
    }

    @Column(name="bcc")
    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }
    
    @Column(name="cc")
    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    @Column(name="sms_result")
    public String getSms_result() {
        return sms_result;
    }

    public void setSms_result(String sms_result) {
        this.sms_result = sms_result;
    }

    @Column(name="sms_message_id")
    public String getSms_message_id() {
        return sms_message_id;
    }

    public void setSms_message_id(String sms_message_id) {
        this.sms_message_id = sms_message_id;
    }

    @Column(name="email_content")
    public String getEmail_content() {
    	return email_content;
    }

    public void setEmail_content(String email_content) {
    	this.email_content = email_content;
    }

    @Column(name="email_subject")
    public String getEmail_subject() {
    	return email_subject;
    }

    public void setEmail_subject(String email_subject) {
    	this.email_subject = email_subject;
    }

    @Column(name="fail_count")
    public Integer getFail_count() {
    	return fail_count;
    }

    public void setFail_count(Integer fail_count) {
    	this.fail_count = fail_count;
    }

    @Transient
    public String getFail_count_str() {
        return fail_count==null?"":Formatter.formatDecimal(fail_count.toString(), Formatter.CURRENCY_PATTERN_PLAIN2);
    }

    public void setFail_count_str(String fail_count) {
        try {
            this.fail_count = Integer.parseInt(fail_count.replaceAll(",", ""));
        } catch (Exception e){}
    }

    @Column(name="recipient")
    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Column(name="sent_date")
    public Timestamp getSent_date() {
        return sent_date;
    }

    public void setSent_date(Timestamp sent_date) {
        this.sent_date = sent_date;
    }
    
    @Transient
    public String getSent_date_str() {
        return Formatter.formatDate(sent_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setSent_date_str(String sent_date) {
        try {
            this.sent_date = DateUtil.getTimestampFromDate(DateUtil.getDate(sent_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

    @Column(name="resend_date")
    public Timestamp getResend_date() {
        return resend_date;
    }

    public void setResend_date(Timestamp resend_date) {
        this.resend_date = resend_date;
    }
    
    @Transient
    public String getResend_date_str() {
        return Formatter.formatDate(resend_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setResend_date_str(String resend_date) {
        try {
            this.resend_date = DateUtil.getTimestampFromDate(DateUtil.getDate(resend_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

    @Column(name="notification_type")
    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }
    
    @Column(name="smtp")
    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    @Column(name="err_log_id")
    public String getErr_log_id() {
        return err_log_id;
    }

    public void setErr_log_id(String err_log_id) {
        this.err_log_id = err_log_id;
    }

    @Column(name="process_start_date")
    public Timestamp getProcess_start_date() {
        return process_start_date;
    }

    public void setProcess_start_date(Timestamp process_start_date) {
        this.process_start_date = process_start_date;
    }
    
    @Column(name="queue_type")
    public String getQueue_type() {
    	return queue_type;
    }

    public void setQueue_type(String queue_type) {
    	this.queue_type = queue_type;
    }

    @Column(name="send_status")
    public String getSend_status() {
    	return send_status;
    }

    public void setSend_status(String send_status) {
    	this.send_status = send_status;
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

    private List<EmailQueueAttachmentModel> attachmentList = new ArrayList();
    @OneToMany(targetEntity = EmailQueueAttachmentModel.class, fetch = FetchType.LAZY, mappedBy = "email_queue_id")
    public List<EmailQueueAttachmentModel> getAttachmentList() {
        return attachmentList;
    }
    public void setAttachmentList(List<EmailQueueAttachmentModel> attachmentList) {
        this.attachmentList = attachmentList;
    }
    
}
