/* ----------------------------------------------
   NAME   : AuditLoginModel.java 
   CREATED BY  : Java Model Generator                   
   CREATED Date: 21-DECEMBER -2010                   
   UPDATED BY  :                                
   UPDATED Date:                    
 ------------------------------------------------*/



package com.sains.framework.model;

import com.sains.common.util.Validator;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_audit_login")
public class AuditLoginModel implements java.io.Serializable {
    private String auditlogin_id = "";
//    private Integer auditlogin_id = null;
    private String loginaction;
    private String logintype;
    private String loginstatus;
    private String loginip;
    private java.sql.Timestamp logintime;
    private String loginid;
    private String system_id;

    private final String[] updatableColumns = new String[] {"Auditlogin_id","Logintype","Loginstatus","Loginip","Logintime","Loginid","System_id"};


    private final String[] columnLength = new String[] {"auditlogin_id:16","logintype:1","loginstatus:1","loginip:30","loginid:20","system_id:15"};
    private Map columnLengthMap = null;


    //varchar(20) as PK : START//
    @Id
    @Column(name="auditlogin_id")
    public String getAuditlogin_id() {
    	return auditlogin_id;
    }

    public void setAuditlogin_id(String auditlogin_id) {
    	this.auditlogin_id = auditlogin_id;
    }

    @Transient 
    public String getID() { 
        return auditlogin_id; 
    } 

    public void setID(String pk_id) {
        this.auditlogin_id = pk_id;
    }
    //varchar(20) as pk : END//
    
    //Integer as pk : START//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "auditlogin_id")
//    public Integer getAuditlogin_id() {
//        return auditlogin_id;
//    }
//
//    public void setAuditlogin_id(Integer auditlogin_id) {
//        this.auditlogin_id = auditlogin_id;
//    }
//
//    @Transient
//    public String getID() {
//        if (auditlogin_id == null) {
//            return null;
//        }
//        return auditlogin_id.toString(); 
//    }
//
//    public void setID(String auditlogin_id) {
//        if (Validator.isEmpty(auditlogin_id)) {
//            setAuditlogin_id(null);
//        } else {
//            try {
//                this.auditlogin_id = Integer.parseInt(auditlogin_id);
//            } catch (Exception e) {
//                setAuditlogin_id(null);
//            }
//        }
//    }
    //Integer as pk : END//

    @Column(name="logintype")
    public String getLogintype() {
    	return logintype;
    }

    public void setLogintype(String logintype) {
    	this.logintype = logintype;
    }

    @Column(name="loginstatus")
    public String getLoginstatus() {
    	return loginstatus;
    }

    public void setLoginstatus(String loginstatus) {
    	this.loginstatus = loginstatus;
    }

    @Column(name="loginip")
    public String getLoginip() {
    	return loginip;
    }

    public void setLoginip(String loginip) {
    	this.loginip = loginip;
    }

    @Column(name="logintime")
    public java.sql.Timestamp getLogintime() {
    	return logintime;
    }

    public void setLogintime(java.sql.Timestamp logintime) {
    	this.logintime = logintime;
    }

    @Column(name="loginid")
    public String getLoginid() {
    	return loginid;
    }

    public void setLoginid(String loginid) {
    	this.loginid = loginid;
    }
    @Column(name="loginaction")
    public String getLoginaction() {
        return loginaction;
    }

    public void setLoginaction(String loginaction) {
        this.loginaction = loginaction;
    }

    @Column(name="system_id")
    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
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

}
