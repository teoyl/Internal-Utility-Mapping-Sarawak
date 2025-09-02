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

@Entity
@Table(name="t_email_queue_attachment")
public class EmailQueueAttachmentModel extends ModelBase implements java.io.Serializable {
    private String attachment_id = "";
    private String created_by;
    private java.sql.Timestamp created_date;
    private String dr_id;
    private String email_queue_id;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Attachment_id","Dr_id","Email_queue_id"};


    private final String[] columnLength = new String[] {"attachment_id:20","dr_id:20","email_queue_id:20"};
    private Map columnLengthMap = null;

    public EmailQueueAttachmentModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="attachment_id")
    public String getAttachment_id() {
    	return attachment_id;
    }

    public void setAttachment_id(String attachment_id) {
    	this.attachment_id = attachment_id;
    }

    @Transient 
    public String getID() { 
        return attachment_id; 
    } 

    public void setID(String pk_id) {
        this.attachment_id = pk_id;
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

    @Column(name="dr_id")
    public String getDr_id() {
    	return dr_id;
    }

    public void setDr_id(String dr_id) {
    	this.dr_id = dr_id;
    }

    @Column(name="email_queue_id")
    public String getEmail_queue_id() {
    	return email_queue_id;
    }

    public void setEmail_queue_id(String email_queue_id) {
    	this.email_queue_id = email_queue_id;
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
    DrDocRepoModel drDocRepoModel = null;
    @ManyToOne(targetEntity=DrDocRepoModel.class, optional=true, fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="dr_doc_id", name="dr_id", insertable=false, updatable=false, nullable = true)
    public DrDocRepoModel getDrDocRepoModel() {
            return drDocRepoModel;
    }
    public void setDrDocRepoModel(DrDocRepoModel drDocRepoModel) {
            this.drDocRepoModel = drDocRepoModel;
    }
}
