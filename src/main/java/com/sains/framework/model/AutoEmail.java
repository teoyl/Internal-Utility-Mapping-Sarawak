package com.sains.framework.model;

import com.sains.framework.base.ModelBase;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_setup_autoemail")
public class AutoEmail extends ModelBase implements java.io.Serializable {
    private String auto_email_id = "";
    private String code;
    private String descs;
    private String action_name;
    private String method_name;
    private String action_status;
    private String auto_type;
    private java.sql.Timestamp created_date;
    private String created_by;
    private java.sql.Timestamp updated_date;
    private String updated_by;
    private String no_id;
    private String mail_to;
    private String mail_ccto;
    private String mail_bccto;
    private String enable = "Y";

    private NotificationSetup notificationSetup;
    private List<AutoEmailParam> autoEmailParamList = new ArrayList();

    private final String[] updatableColumns = new String[] {"Auto_email_id","Code","Descs","Action_name",
    "Method_name","Action_status", "Auto_type", "No_id", "Mail_to", "Mail_ccto", "Mail_bccto", "Enable"};


    @Id
    @Column(name="auto_email_id")
    public String getAuto_email_id() {
    	return auto_email_id;
    }

    public void setAuto_email_id(String auto_email_id) {
    	this.auto_email_id = auto_email_id;
    }

    @Transient 
    public String getID() { 
        return auto_email_id; 
    } 

    public void setID(String pk_id) {
        this.auto_email_id = pk_id;
    }

    @Column(name="code")
    public String getCode() {
    	return code;
    }

    public void setCode(String code) {
    	this.code = code;
    }

    @Column(name="descs")
    public String getDescs() {
    	return descs;
    }

    public void setDescs(String descs) {
    	this.descs = descs;
    }

    @Column(name="action_name")
    public String getAction_name() {
    	return action_name;
    }

    public void setAction_name(String action_name) {
    	this.action_name = action_name;
    }

    @Column(name="method_name")
    public String getMethod_name() {
    	return method_name;
    }

    public void setMethod_name(String method_name) {
    	this.method_name = method_name;
    }

    @Column(name="action_status")
    public String getAction_status() {
    	return action_status;
    }

    public void setAction_status(String action_status) {
    	this.action_status = action_status;
    }

    @Column(name="auto_type")
    public String getAuto_type() {
        return auto_type;
    }

    public void setAuto_type(String auto_type) {
        this.auto_type = auto_type;
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

    @Column(name="no_id")
    public String getNo_id() {
        return no_id;
    }

    public void setNo_id(String no_id) {
        this.no_id = no_id;
    }

    @Column(name="mail_bccto")
    public String getMail_bccto() {
        return mail_bccto;
    }

    public void setMail_bccto(String mail_bccto) {
        this.mail_bccto = mail_bccto;
    }

    @Column(name="mail_ccto")
    public String getMail_ccto() {
        return mail_ccto;
    }

    public void setMail_ccto(String mail_ccto) {
        this.mail_ccto = mail_ccto;
    }

    @Column(name="mail_to")
    public String getMail_to() {
        return mail_to;
    }

    public void setMail_to(String mail_to) {
        this.mail_to = mail_to;
    }

    @Column(name="enable")
    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        if (! enable.equalsIgnoreCase("Y")){
            this.enable = "N";
        } else {
            this.enable = enable;
        }
    }



    /* ******** Write your code after this line ****** */

    @OneToOne(targetEntity=NotificationSetup.class, optional=true, fetch=FetchType.EAGER)
    @JoinColumn(name="no_id", insertable=false, updatable=false, nullable = true)
    public NotificationSetup getNotificationSetup() {
        return notificationSetup;
    }
    public void setNotificationSetup(NotificationSetup ns) {
        this.notificationSetup = ns;
    }

    @OneToMany(targetEntity=AutoEmailParam.class, fetch=FetchType.LAZY, mappedBy="auto_email_id")
    public List<AutoEmailParam> getAutoEmailParam() {
            return autoEmailParamList;
    }
    public void setAutoEmailParam(List autoEmailParamList) {
            this.autoEmailParamList = autoEmailParamList;
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
}
