/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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

/**
 *
 * @author Aiman
 */
@Entity
@Table(name = "US_PRECHECK_HISTORY")
public class PrecheckHistoryModel extends ModelBase implements java.io.Serializable {

    private String task_id;
    private String job_id;
    private String precheck_type;
    private Integer task_seq;
    private String task_desc;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private String traverse_signal;

    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{};
    public String[] columnLength = new String[]{};

    @Id
    @Column(name = "task_id")
    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    @Transient
    public String getID() {
        return task_id;
    }

    public void setID(String pk_id) {
        this.task_id = pk_id;
    }

    @Column(name = "job_id")
    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    @Column(name = "precheck_type")
    public String getPrecheck_type() {
        return precheck_type;
    }

    public void setPrecheck_type(String precheck_type) {
        this.precheck_type = precheck_type;
    }

    @Column(name = "task_seq")
    public Integer getTask_seq() {
        return task_seq;
    }

    public void setTask_seq(Integer task_seq) {
        this.task_seq = task_seq;
    }

    @Column(name = "task_desc")
    public String getTask_desc() {
        return task_desc;
    }

    public void setTask_desc(String task_desc) {
        this.task_desc = task_desc;
    }

    @Column(name = "traverse_signal")
    public String getTraverse_signal() {
        return traverse_signal;
    }

    public void setTraverse_signal(String traverse_signal) {
        this.traverse_signal = traverse_signal;
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
