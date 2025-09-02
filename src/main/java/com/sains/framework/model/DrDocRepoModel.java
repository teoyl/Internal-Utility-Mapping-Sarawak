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
import com.sains.common.util.SFTPBean;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.ModelBase;
import org.hibernate.Session;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name="t_dr_doc_repo")
@NamedQueries({
    @NamedQuery(name = "DocRepo.findBy_dr_doc_id",query = "from DrDocRepoModel where dr_doc_id = :dr_doc_id"),
    @NamedQuery(name = "DocRepo.findBy_dr_doc_id_notTemp",query = "from DrDocRepoModel where dr_doc_id = :dr_doc_id and dr_doc_temp = 'N'"),
    @NamedQuery(name = "DocRepo.findBy_drDocApp_createdUser",query = "from DrDocRepoModel where dr_doc_application = :drDocApp and created_by = :createdBy"),
    @NamedQuery(name = "DocRepo.findBy_dr_doc_id_application",query = "from DrDocRepoModel where dr_doc_id = :dr_doc_id  and dr_doc_application = :dr_doc_application") //serene @ 11/1/2022
        
})
public class DrDocRepoModel extends ModelBase implements java.io.Serializable {
    private String dr_id = "";
    private String created_by;
    private java.sql.Timestamp created_date;
    private String dr_doc_id;
    private String dr_doc_name;
    private String dr_doc_path;
    private String dr_doc_type;
    private String mime_type;
    private String dr_doc_application;
    private String dr_doc_temp = "N"; //Y=Temp Document,N=Not Temp
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Dr_id","Dr_doc_id","Dr_doc_name","Dr_doc_path","Dr_doc_type", "Dr_doc_temp"};


    private final String[] columnLength = new String[] {"dr_id:20","dr_doc_id:20","dr_doc_name:50","dr_doc_path:200","dr_doc_type:10"};
    private Map columnLengthMap = null;

    public DrDocRepoModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="dr_id")
    public String getDr_id() {
    	return dr_id;
    }

    public void setDr_id(String dr_id) {
    	this.dr_id = dr_id;
    }

    @Transient 
    public String getID() { 
        return dr_id; 
    } 

    public void setID(String pk_id) {
        this.dr_id = pk_id;
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

    @Column(name="dr_doc_id")
    public String getDr_doc_id() {
    	return dr_doc_id;
    }

    public void setDr_doc_id(String dr_doc_id) {
    	this.dr_doc_id = dr_doc_id;
    }

    @Column(name="dr_doc_name")
    public String getDr_doc_name() {
    	return dr_doc_name;
    }

    public void setDr_doc_name(String dr_doc_name) {
    	this.dr_doc_name = dr_doc_name;
    }

    @Column(name="dr_doc_path")
    public String getDr_doc_path() {
    	return dr_doc_path;
    }

    public void setDr_doc_path(String dr_doc_path) {
    	this.dr_doc_path = dr_doc_path;
    }

    @Column(name="dr_doc_type")
    public String getDr_doc_type() {
    	return dr_doc_type;
    }

    public void setDr_doc_type(String dr_doc_type) {
    	this.dr_doc_type = dr_doc_type;
    }

    @Column(name="mime_type")
    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    @Column(name="dr_doc_application")
    public String getDr_doc_application() {
        return dr_doc_application;
    }

    public void setDr_doc_application(String dr_doc_application) {
        this.dr_doc_application = dr_doc_application;
    }
    
    @Column(name="dr_doc_temp")
    public String getDr_doc_temp() {
        return dr_doc_temp;
    }

    public void setDr_doc_temp(String dr_doc_temp) {
        this.dr_doc_temp = dr_doc_temp;
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
        } catch (Exception e){
            e.printStackTrace();
        }
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

//    @Override
//    public void preDelete(Session session, Object deletingObject) throws Exception {
//        System.out.println("in DrDocRepoModel preDelete");
//        new SFTPBean().deleteFile(this.getDr_doc_path());
//    }
    
}
