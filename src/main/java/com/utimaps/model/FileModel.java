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
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.ParamDef;

@Entity(name = "US_FILE")
@Table(name = "US_FILE")
@NamedQueries({
        @NamedQuery(name = "FileModel.findBy_fileType",query = "from US_FILE where file_id = :file_id and file_type = :file_type"),
        @NamedQuery(name = "FileModel.findBy_fileId",query = "from US_FILE where file_id = :file_id"),
        @NamedQuery(name = "FileModel.findBy_caseId",query = "from US_FILE where case_id = :case_id and ci_id = :ci_id"),
        @NamedQuery(name = "FileModel.findBy_USCS",query = "from US_FILE where case_id = :case_id and file_type = :file_type and description = :description and file_status = 'Y'"),
        @NamedQuery(name = "FileModel.findBy_DSP",query = "from US_FILE where case_id = :case_id and file_type = :file_type and (description not in ('DSP_SIGNED', 'DSP_STAMPED') or description is null)"),
        @NamedQuery(name = "FileModel.findBy_DRO",query = "from US_FILE where case_id = :case_id and file_type = :file_type"),
    })
@FilterDefs({
    @FilterDef(name="caseFilter", parameters={
        @ParamDef( name="caseNo", type="string")
    })
})
public class FileModel extends ModelBase implements java.io.Serializable{
    private String file_id;
    private String file_type;
    private String file_name;
    private String file_ext;
    private String file_path;
    private String case_id;
    private String usj_no;
    private String file_size;
    private String ci_id;
    private String file_status;
    private Integer total_files;
    private Integer total_gislayer;
    private Integer total_features;
    private String description;
    private String original_file_name;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{"file_id", "file_type", "file_name", "file_ext", "file_path", "case_id", "usj_no", "total_files", "total_gislayer", "total_features", "description"};

    public String[] columnLength = new String[]{"file_id:20", "file_type:3", "file_name:50", "file_ext:3", "file_path:100", "case_id:20", "usj_no:20", "total_files:38", "total_gislayer:38", "total_features:38", "description:100"};

    @Id
    @Column(name = "file_id")
    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    @Column(name = "file_type")
    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    @Column(name = "file_name")
    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    @Column(name = "file_ext")
    public String getFile_ext() {
        return file_ext;
    }

    public void setFile_ext(String file_ext) {
        this.file_ext = file_ext;
    }

    @Column(name = "file_path")
    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
    
    @Column(name = "file_size")
    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    @Column(name = "ci_id")
    public String getCi_id() {
        return ci_id;
    }

    public void setCi_id(String ci_id) {
        this.ci_id = ci_id;
    }
    
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

    @Column(name = "total_files")
    public Integer getTotal_files() {
        return total_files;
    }

    public void setTotal_files(Integer total_files) {
        this.total_files = total_files;
    }

    @Column(name = "total_gislayer")
    public Integer getTotal_gislayer() {
        return total_gislayer;
    }

    public void setTotal_gislayer(Integer total_gislayer) {
        this.total_gislayer = total_gislayer;
    }

    @Column(name = "total_features")
    public Integer getTotal_features() {
        return total_features;
    }

    public void setTotal_features(Integer total_features) {
        this.total_features = total_features;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name = "original_file_name")
    public String getOriginal_file_name() {
        return original_file_name;
    }

    public void setOriginal_file_name(String original_file_name) {
        this.original_file_name = original_file_name;
    }

    @Column(name = "file_status")
    public String getFile_status() {
        return file_status;
    }

    public void setFile_status(String file_status) {
        this.file_status = file_status;
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
    
    public void setColumnLength(String[] columnLength) {
        this.columnLength = columnLength;
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
    
    private String created_date_str;
    @Transient
    public String getCreated_date_str() {
        return Formatter.formatDate(created_date, SystemConstants.DATE.dataEntryFormat3);
    }

    public void setCreated_date_str(String created_date_str) {
        try {
            this.created_date = DateUtil.getTimestampFromDate(DateUtil.getDate(created_date_str, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "FileModel", "FileModel", "setCreated_date_str");
        }
    }
    
    @Transient
    public String getUpdated_date_str() {
        return Formatter.formatDate(updated_date, SystemConstants.DATE.dataEntryFormat);
    }

    public void setUpdated_date_str(String updated_date_str) {
        try {
            this.updated_date = DateUtil.getTimestampFromDate(DateUtil.getDate(updated_date_str, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){
            CommonFunction.writeLogFile(e.getStackTrace(), "FileModel", "FileModel", "setUpdated_date_str");
        }
    }
}
