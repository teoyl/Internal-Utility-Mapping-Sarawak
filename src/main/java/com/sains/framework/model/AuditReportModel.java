/* ----------------------------------------------
   NAME   : AuditReportModel.java 
   CREATED BY  : Java Model Generator                   
   CREATED Date: 20-DECEMBER -2010                   
   UPDATED BY  : thensw
   UPDATED Date: 30-Sept-2013
 ------------------------------------------------*/

package com.sains.framework.model;

import com.sains.common.util.Validator;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_audit_report")
public class AuditReportModel implements java.io.Serializable {
    private String reportname;
    private java.sql.Timestamp starttime;
    private java.sql.Timestamp endtime;
    private String queryby;
    private String criteria;
    private String auditreport_id = "";
//    private Integer auditreport_id = null;
    private String reporttype;
    private String success_status;

    private final String[] updatableColumns = new String[] {"Auditreport_id", "Reportname","Starttime","Endtime","Queryby","Criteria","Reporttype","Success_status"};


    private final String[] columnLength = new String[] {"reportname:50","queryby:65","criteria:4000","auditreport_id:20","reporttype:5"};
    private Map columnLengthMap = null;


    @Column(name="reportname")
    public String getReportname() {
    	return reportname;
    }

    public void setReportname(String reportname) {
    	this.reportname = reportname;
    }

    @Column(name="starttime")
    public java.sql.Timestamp getStarttime() {
    	return starttime;
    }

    public void setStarttime(java.sql.Timestamp starttime) {
    	this.starttime = starttime;
    }

    @Column(name="endtime")
    public Timestamp getEndtime() {
        return endtime;
    }

    public void setEndtime(Timestamp endtime) {
        this.endtime = endtime;
    }

    @Column(name="success_status")
    public String getSuccess_status() {
        return success_status;
    }

    public void setSuccess_status(String success_status) {
        this.success_status = success_status;
    }



    @Column(name="queryby")
    public String getQueryby() {
    	return queryby;
    }

    public void setQueryby(String queryby) {
    	this.queryby = queryby;
    }

    @Column(name="criteria")
    public String getCriteria() {
    	return criteria;
    }

    public void setCriteria(String criteria) {
    	this.criteria = criteria;
    }

    //varchar(20) as pk : START//
    @Id
    @Column(name="auditreport_id")
    public String getAuditreport_id() {
    	return auditreport_id;
    }

    public void setAuditreport_id(String auditreport_id) {
    	this.auditreport_id = auditreport_id;
    }

    @Transient 
    public String getID() { 
        return auditreport_id; 
    } 

    public void setID(String pk_id) {
        this.auditreport_id = pk_id;
    }
    //Integer as pk : END//

    //Integer as pk : START//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "auditreport_id")
//    public Integer getAuditreport_id() {
//        return auditreport_id;
//    }
//
//    public void setAuditreport_id(Integer auditreport_id) {
//        this.auditreport_id = auditreport_id;
//    }
//
//    @Transient
//    public String getID() {
//        if (auditreport_id == null) {
//            return null;
//        }
//        return auditreport_id.toString(); 
//    }
//
//    public void setID(String auditreport_id) {
//        if (Validator.isEmpty(auditreport_id)) {
//            setAuditreport_id(null);
//        } else {
//            try {
//                this.auditreport_id = Integer.parseInt(auditreport_id);
//            } catch (Exception e) {
//                setAuditreport_id(null);
//            }
//        }
//    }
    //Integer as pk : END//
    
    @Column(name="reporttype")
    public String getReporttype() {
    	return reporttype;
    }

    public void setReporttype(String reporttype) {
    	this.reporttype = reporttype;
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
