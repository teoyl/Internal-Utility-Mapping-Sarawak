/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.model;

import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.ModelBase;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;

@Entity
@FilterDefs({
    @FilterDef(name="caseFilter", parameters={
        @ParamDef( name="caseNo", type="string")
    }),
    @FilterDef(name="typeFilter", parameters={
        @ParamDef( name="fileType", type="string")
    }),
    @FilterDef(name="statusFilter")
})
@Table(name = "US_CHECKLIST_ITEM")
public class ChecklistItemModel extends ModelBase implements java.io.Serializable{
    private String cl_id;
    private String case_id;
    private String check_id;
    private String file_id;
    private String ci_id;
    private String cl_result;
    private String cl_status;
    private String cl_remarks;
    private String cl_file_id;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{"cl_id","case_id","check_id","file_id","ci_id","cl_result","cl_status","cl_remarks","cl_file_id"};
    
    public String[] columnLength = new String[]{"cl_id:20","case_id:20","cs_code:20","file_id:20","cl_item_type:20","cl_result:20","cl_status:2","cl_remarks:4000","cl_file_id:20"};

    @Id
    @Column(name = "cl_id")
    public String getCl_id() {
        return cl_id;
    }

    public void setCl_id(String cl_id) {
        this.cl_id = cl_id;
    }

    @Column(name = "case_id")
    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    @Column(name = "check_id")
    public String getCheck_id() {
        return check_id;
    }

    public void setCheck_id(String check_id) {
        this.check_id = check_id;
    }

    @Column(name = "file_id")
    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    @Column(name = "ci_id")
    public String getCi_id() {
        return ci_id;
    }

    public void setCi_id(String ci_id) {
        this.ci_id = ci_id;
    }

    @Column(name = "cl_result")
    public String getCl_result() {
        return cl_result;
    }

    public void setCl_result(String cl_result) {
        this.cl_result = cl_result;
    }

    @Column(name = "cl_status")
    public String getCl_status() {
        return cl_status;
    }

    public void setCl_status(String cl_status) {
        this.cl_status = cl_status;
    }

    @Column(name = "cl_remarks")
    public String getCl_remarks() {
        return cl_remarks;
    }

    public void setCl_remarks(String cl_remarks) {
        this.cl_remarks = cl_remarks;
    }

    @Column(name = "cl_file_id")
    public String getCl_file_id() {
        return cl_file_id;
    }

    public void setCl_file_id(String cl_file_id) {
        this.cl_file_id = cl_file_id;
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
    
    private FileModel ciFileModel;

    @OneToOne(targetEntity = FileModel.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "FILE_ID", name = "FILE_ID", insertable = false, updatable = false, nullable = true)
    public FileModel getCiFileModel() {
        return ciFileModel;
    }

    public void setCiFileModel(FileModel ciFileModel) {
        this.ciFileModel = ciFileModel;
    }
    
    private List<FileModel> checklistFileModelList = new ArrayList();
    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "ci_id", name = "ci_id", insertable = false, updatable = false, nullable = true)
    @Filter(name="caseFilter", condition="case_id = :caseNo")
    @Filter(name="typeFilter", condition="file_type <> :fileType")
    @OrderBy("created_date DESC")
    public List<FileModel> getChecklistFileModelList() {
        return checklistFileModelList;
    }

    public void setChecklistFileModelList(List<FileModel> checklistFileModelList) {
        this.checklistFileModelList = checklistFileModelList;
    }
    
    private List<FileModel> checklistSupportFileList = new ArrayList();
    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "ci_id", name = "ci_id", insertable = false, updatable = false, nullable = true)
    @Filter(name="caseFilter", condition="case_id = :caseNo")
    @Filter(name="typeFilter", condition="file_type = :fileType")
    @Filter(name="statusFilter", condition="file_status = 'Y'")
    @OrderBy("created_date DESC")
    public List<FileModel> getChecklistSupportFileList() {
        return checklistSupportFileList;
    }

    public void setChecklistSupportFileList(List<FileModel> checklistSupportFileList) {
        this.checklistSupportFileList = checklistSupportFileList;
    }
   
    @Transient
    public String getUpdated_date_str() {
        return Formatter.formatDate(updated_date, SystemConstants.DATE.dataEntryFormat);
    }

    public void setUpdated_date_str(String updated_date_str) {
        try {
            this.updated_date = DateUtil.getTimestampFromDate(DateUtil.getDate(updated_date_str, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){
            CommonFunction.writeLogFile(e.getStackTrace(), "ChecklistItemModel", "ChecklistItemModel", "setUpdated_date_str");
        }
    }
}
