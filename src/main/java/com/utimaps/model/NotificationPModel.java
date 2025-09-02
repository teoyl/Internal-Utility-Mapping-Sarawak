/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.model;

import com.lxg.common.model.PublicUserModel;
import com.sains.common.util.Validator;
import com.sains.framework.base.ModelBase;
import com.sains.framework.model.User;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "US_NOTIFICATION_P")
public class NotificationPModel extends ModelBase implements java.io.Serializable{
    private String message_id;
    private String case_id;
    private String no_id;
    private String message_sender;
    private String message_recipient;
    private String message_subject;
    private String message_content;
    private String message_status;
    private java.sql.Timestamp message_status_date;
    private String message_status_date_str;
    private String system_id;
    private String file_id;
    private String task_id;
    private String message_type;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{"message_id","case_id","no_id","message_sender","message_recipient","message_subject","message_content","message_status","message_status_date","system_id","file_id","task_id","message_type"};
    
    public String[] columnLength = new String[]{"message_id:20","case_id:20","no_id:20","message_sender:20","message_recipient:20","message_subject:255","message_content:4000","message_status:20","system_id:20","file_id:20","task_id:20","message_type:1"};

    @Id
    @Column(name = "message_id")
    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    @Column(name = "case_id")
    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    @Column(name = "no_id")
    public String getNo_id() {
        return no_id;
    }

    public void setNo_id(String no_id) {
        this.no_id = no_id;
    }

    @Column(name = "message_sender")
    public String getMessage_sender() {
        return message_sender;
    }

    public void setMessage_sender(String message_sender) {
        this.message_sender = message_sender;
    }

    @Column(name = "message_recipient")
    public String getMessage_recipient() {
        return message_recipient;
    }

    public void setMessage_recipient(String message_recipient) {
        this.message_recipient = message_recipient;
    }

    @Column(name = "message_subject")
    public String getMessage_subject() {
        return message_subject;
    }

    public void setMessage_subject(String message_subject) {
        this.message_subject = message_subject;
    }

    @Column(name = "message_content")
    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    @Column(name = "message_status")
    public String getMessage_status() {
        return message_status;
    }

    public void setMessage_status(String message_status) {
        this.message_status = message_status;
    }

    @Column(name = "message_status_date")
    public Timestamp getMessage_status_date() {
        return message_status_date;
    }

    public void setMessage_status_date(Timestamp message_status_date) {
        this.message_status_date = message_status_date;
    }

    @Column(name = "system_id")
    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    @Column(name = "file_id")
    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    @Column(name = "task_id")
    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    @Column(name = "message_type")
    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
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
    public String getMessage_status_date_str() {
        if (message_status_date != null) {
            return message_status_date.toString();
        }
        return " ";
    }

    public void setMessage_status_date_str(String message_status_date_str) {
        this.message_status_date_str = message_status_date_str;
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
    
    public String[] fileIdList;
    @Transient
    public String[] getFileIdList() {
        
        String tempFileId = "";
        
        try {
            if(!Validator.isEmpty(file_id)) {
                tempFileId = file_id.substring(1,file_id.length()-1);
                fileIdList = tempFileId.split(",");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return fileIdList;
    }
    
    private User messageSenderModel;

    @OneToOne(targetEntity = User.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "us_user_id", name = "message_sender", insertable = false, updatable = false, nullable = true)
    public User getMessageSenderModel() {
        return messageSenderModel;
    }

    public void setMessageSenderModel(User messageSenderModel) {
        this.messageSenderModel = messageSenderModel;
    }
}
