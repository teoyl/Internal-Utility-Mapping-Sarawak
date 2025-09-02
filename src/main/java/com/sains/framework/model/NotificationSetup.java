/* ----------------------------------------------
   NAME   : NotificationSetup.java 
   CREATED BY  : Java Model Generator                   
   CREATED Date: 29-JUNE     -2010                   
   UPDATED BY  :                                
   UPDATED Date:                    
 ------------------------------------------------*/



package com.sains.framework.model;

import com.sains.framework.base.ModelBase;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name="t_setup_notification")
@NamedQueries({
    @NamedQuery(name = "Notification.findByNoType",query = "from NotificationSetup where no_type = :noType")
})
public class NotificationSetup extends ModelBase implements java.io.Serializable {
    private String no_id;
    private String no_type;
    private String no_desc;
    private String no_sms;
    private String no_email;
    private String no_default_sender;
    private String no_template_subject;
    private String no_template_body;
    private String sms_content;
    private String smtp;
    private java.sql.Timestamp created_date;
    private String created_by;
    private java.sql.Timestamp updated_date;
    private String updated_by;

    private final String[] updatableColumns = new String[] {"No_id","No_type","No_desc","No_sms","No_email","No_default_sender","No_template_subject","No_template_body","Sms_content","Smtp"};


    @Id
    @Column(name="no_id")
    public String getNo_id() {
    	return no_id;
    }

    public void setNo_id(String no_id) {
    	this.no_id = no_id;
    }

    @Transient 
    public String getID() { 
        return no_id; 
    } 

    public void setID(String pk_id) {
        this.no_id = pk_id;
    }

    @Column(name="no_type")
    public String getNo_type() {
    	return no_type;
    }

    public void setNo_type(String no_type) {
    	this.no_type = no_type;
    }

    @Column(name="no_desc")
    public String getNo_desc() {
    	return no_desc;
    }

    public void setNo_desc(String no_desc) {
    	this.no_desc = no_desc;
    }

    @Column(name="no_sms")
    public String getNo_sms() {
    	return no_sms;
    }

    public void setNo_sms(String no_sms) {
    	this.no_sms = no_sms;
    }

    @Column(name="no_email")
    public String getNo_email() {
    	return no_email;
    }

    public void setNo_email(String no_email) {
    	this.no_email = no_email;
    }

    @Column(name="no_default_sender")
    public String getNo_default_sender() {
    	return no_default_sender;
    }

    public void setNo_default_sender(String no_default_sender) {
    	this.no_default_sender = no_default_sender;
    }

    @Column(name="no_template_subject")
    public String getNo_template_subject() {
    	return no_template_subject;
    }

    public void setNo_template_subject(String no_template_subject) {
    	this.no_template_subject = no_template_subject;
    }

    @Column(name="no_template_body")
    public String getNo_template_body() {
    	return no_template_body;
    }

    public void setNo_template_body(String no_template_body) {
    	this.no_template_body = no_template_body;
    }

    @Column(name="sms_content")
    public String getSms_content() {
        return sms_content;
    }

    public void setSms_content(String sms_content) {
        this.sms_content = sms_content;
    }
    
    @Column(name="smtp")
    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }
    
    @Column(name="created_date")
    public java.sql.Timestamp getCreated_date() {
    	return created_date;
    }

    public void setCreated_date(java.sql.Timestamp created_date) {
    	this.created_date = created_date;
    }

    @Column(name="created_by")
    public String getCreated_by() {
    	return created_by;
    }

    public void setCreated_by(String created_by) {
    	this.created_by = created_by;
    }

    @Column(name="updated_date")
    public java.sql.Timestamp getUpdated_date() {
    	return updated_date;
    }

    public void setUpdated_date(java.sql.Timestamp updated_date) {
    	this.updated_date = updated_date;
    }

    @Column(name="updated_by")
    public String getUpdated_by() {
    	return updated_by;
    }

    public void setUpdated_by(String updated_by) {
    	this.updated_by = updated_by;
    }

    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }
    
    private final String[] columnLength = new String[]{};
    private Map columnLengthMap = null;
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
//    @Override
//    public void preInsert(org.hibernate.Session session) throws Exception {
//
//    }

}
