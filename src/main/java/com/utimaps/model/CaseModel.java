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
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "US_CASE")
public class CaseModel extends ModelBase implements java.io.Serializable{
    private String case_id;
    private String usj_no;
    private String qual_level;
    private java.sql.Timestamp usj_date;
    private String so_id;
    private String div_id;
    private String ht_datum;
    private String land_desc;
    private java.sql.Timestamp usj_start_date;
    private java.sql.Timestamp usj_end_date;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    
    

    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{"case_id","usj_no","qual_level","usj_date","so_id","div_id","ht_datum","land_desc","usj_start_date","usj_end_date"};
    
    public String[] columnLength = new String[]{"case_id:20:","usj_no:20","qual_level:3","so_id:20","div_id:22","ht_datum:38","land_desc:256"};
    
    @Id
    @Column(name = "case_id")
    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    @Column(name = "usj_no")
    public String getUsj_no() {
        return usj_no;
    }

    public void setUsj_no(String usj_no) {
        this.usj_no = usj_no;
    }

    @Column(name = "qual_level")
    public String getQual_level() {
        return qual_level;
    }

    public void setQual_level(String qual_level) {
        this.qual_level = qual_level;
    }

    @Column(name = "usj_date")
    public Timestamp getUsj_date() {
        return usj_date;
    }

    public void setUsj_date(Timestamp usj_date) {
        this.usj_date = usj_date;
    }

    @Column(name = "so_id")
    public String getSo_id() {
        return so_id;
    }

    public void setSo_id(String so_id) {
        this.so_id = so_id;
    }

    @Column(name = "div_id")
    public String getDiv_id() {
        return div_id;
    }

    public void setDiv_id(String div_id) {
        this.div_id = div_id;
    }

    @Column(name = "ht_datum")
    public String getHt_datum() {
        return ht_datum;
    }

    public void setHt_datum(String ht_datum) {
        this.ht_datum = ht_datum;
    }

    @Column(name = "land_desc")
    public String getLand_desc() {
        return land_desc;
    }

    public void setLand_desc(String land_desc) {
        this.land_desc = land_desc;
    }

    @Column(name = "usj_start_date")
    public Timestamp getUsj_start_date() {
        return usj_start_date;
    }

    public void setUsj_start_date(Timestamp usj_start_date) {
        this.usj_start_date = usj_start_date;
    }

    @Column(name = "usj_end_date")
    public Timestamp getUsj_end_date() {
        return usj_end_date;
    }

    public void setUsj_end_date(Timestamp usj_end_date) {
        this.usj_end_date = usj_end_date;
    }
    
    public void setColumnLength(String[] columnLength) {
        this.columnLength = columnLength;
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
    
    private String _taskId;

    @Transient
    public String get_taskId() {
        return _taskId;
    }
    
    public void set_taskId(String _taskId) {
        this._taskId = _taskId;
    }
}
