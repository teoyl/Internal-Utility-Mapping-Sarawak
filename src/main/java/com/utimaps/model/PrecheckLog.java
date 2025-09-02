/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.model;

import com.sains.framework.base.ModelBase;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Table;

@Entity
@Table(name = "US_PRECHECK_LOG")
public class PrecheckLog extends ModelBase implements java.io.Serializable {

//    private String case_id;
    private String precheck_job_id;
    private String job_id;
    private String job_status;
    private String usj_no;
    private Integer no_of_errors;
    private Integer tot_files;
    private Integer tot_gislayer;
    private Integer tot_features;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private String precheck_passed;
    private String precheck_log;

    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{};
    public String[] columnLength = new String[]{};

    @Id
    @Column(name = "precheck_job_id")
    public String getPrecheck_job_id() {
        return precheck_job_id;
    }

    public void setPrecheck_job_id(String precheck_job_id) {
        this.precheck_job_id = precheck_job_id;
    }

    @Transient
    public String getID() {
        return precheck_job_id;
    }

    public void setID(String pk_id) {
        this.precheck_job_id = pk_id;
    }

    @Column(name = "job_id")
    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
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

//    @Column(name = "case_id")
//    public String getCase_id() {
//        return case_id;
//    }
//
//    public void setCase_id(String case_id) {
//        this.case_id = case_id;
//    }
//
//    @Transient
//    public String getID() {
//        return job_id;
//    }
//    
//    public void setID(String job_id) {
//        this.job_id = job_id;
//    }
//
//    @Id
//    @Column(name = "job_id")
//    public String getJob_id() {
//        return job_id;
//    }
//    
//    public void setJob_id(String job_id) {
//        this.job_id = job_id;
//    }
//
    @Column(name = "job_status")
    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    @Column(name = "usj_no")
    public String getUsj_no() {
        return usj_no;
    }

    public void setUsj_no(String usj_no) {
        this.usj_no = usj_no;
    }

    @Column(name = "no_of_errors")
    public Integer getNo_of_errors() {
        return no_of_errors;
    }

    public void setNo_of_errors(Integer no_of_errors) {
        this.no_of_errors = no_of_errors;
    }

    @Column(name = "tot_files")
    public Integer getTot_files() {
        return tot_files;
    }

    public void setTot_files(Integer tot_files) {
        this.tot_files = tot_files;
    }

    @Column(name = "tot_gislayer")
    public Integer getTot_gislayer() {
        return tot_gislayer;
    }

    public void setTot_gislayer(Integer tot_gislayer) {
        this.tot_gislayer = tot_gislayer;
    }

    @Column(name = "tot_features")
    public Integer getTot_features() {
        return tot_features;
    }

    public void setTot_features(Integer tot_features) {
        this.tot_features = tot_features;
    }
//
//    @Column(name = "created_by")
//    public String getCreated_by() {
//        return created_by;
//    }
//
//    public void setCreated_by(String created_by) {
//        this.created_by = created_by;
//    }
//
//    @Column(name = "created_date")
//    public Timestamp getCreated_date() {
//        return created_date;
//    }
//
//    public void setCreated_date(Timestamp created_date) {
//        this.created_date = created_date;
//    }
//
//    @Column(name = "updated_by")
//    public String getUpdated_by() {
//        return updated_by;
//    }
//
//    public void setUpdated_by(String updated_by) {
//        this.updated_by = updated_by;
//    }
//
//    @Column(name = "updated_date")
//    public Timestamp getUpdated_date() {
//        return updated_date;
//    }
//
//    public void setUpdated_date(Timestamp updated_date) {
//        this.updated_date = updated_date;
//    }
//

    @Column(name = "precheck_passed")
    public String getPrecheck_passed() {
        return precheck_passed;
    }

    public void setPrecheck_passed(String precheck_passed) {
        this.precheck_passed = precheck_passed;
    }

    @Column(name = "precheck_log")
    public String getPrecheck_log() {
        return precheck_log;
    }

    public void setPrecheck_log(String precheck_log) {
        this.precheck_log = precheck_log;
    }

    @Transient
    public String[] getColumnLength() {
        return columnLength;
    }

    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }

    public void setUpdatableColumns(String[] updatableColumns) {
        this.updatableColumns = updatableColumns;
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
