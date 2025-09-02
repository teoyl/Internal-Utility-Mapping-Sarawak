package com.sample;


import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SFTPBean;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.ModelBase;
import com.sains.framework.model.DrDocRepoModel;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.hibernate.Session;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name="t_uppy")
@NamedQueries({
    @NamedQuery(name = "UppyModel.findBy_createdBy",query = "from UppyModel where created_by = :createdBy"),
    @NamedQuery(name = "UppyModel.findBy_drDocId",query = "from UppyModel where dr_doc_id = :drDocId"),
    @NamedQuery(name = "UppyModel.findBy_uppyFileDesc",query = "from UppyModel where uppy_file_desc = :uppyFileDesc")
})
public class UppyModel extends ModelBase implements java.io.Serializable {
    private String uppy_id = "";
    private String uppy_parent_id = null;
    private String dr_doc_id;
    private String dr_doc_id_2;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private String uppy_file_desc;

    private final String[] updatableColumns = new String[] {"Uppy_id","Uppy_file_desc"};


    private final String[] columnLength = new String[] {"uppy_id:20","uppy_file_desc:50"};
    private Map columnLengthMap = null;

    public UppyModel() {
//        getUppyUpload_appCodeSetup().put("directModel_file", "dr_doc_id;uppyDirectModel_actualFolder");
//        getUppyUpload_appCodeSetup().put("uppySample2_file1", "dr_doc_id;uppySample2_actualFolder");
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="uppy_id")
    public String getUppy_id() {
    	return uppy_id;
    }

    public void setUppy_id(String uppy_id) {
    	this.uppy_id = uppy_id;
    }

    @Transient 
    public String getID() { 
        return uppy_id; 
    } 

    public void setID(String pk_id) {
        this.uppy_id = pk_id;
    }

    @Column(name="uppy_parent_id")
    public String getUppy_parent_id() {
        return uppy_parent_id;
    }

    public void setUppy_parent_id(String uppy_parent_id) {
        if (Validator.isEmpty(uppy_parent_id)) {
            uppy_parent_id = null;
        }
        this.uppy_parent_id = uppy_parent_id;
    }

    @Column(name="dr_doc_id")
    public String getDr_doc_id() {
        return dr_doc_id;
    }

    public void setDr_doc_id(String dr_doc_id) {
        if (Validator.notEmpty(dr_doc_id)) {
            if (get_operation()!=null && get_operation().equals("directModel")) {
                this.set_uppyUploadFile_drDocPath("uppyDirectModel_actualFolder");
            } else {
                this.set_uppyUploadFile_drDocPath("uppySample2ActualFolder");
            }
            this.set_uppyUploadFile_drDocId(dr_doc_id);
        }
        this.dr_doc_id = dr_doc_id;
    }
    
    @Column(name="dr_doc_id_2")
    public String getDr_doc_id_2() {
        return dr_doc_id_2;
    }

    public void setDr_doc_id_2(String dr_doc_id_2) {
        this.dr_doc_id_2 = dr_doc_id_2;
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
        } catch (Exception e){}
    }

    @Column(name="uppy_file_desc")
    public String getUppy_file_desc() {
    	return uppy_file_desc;
    }

    public void setUppy_file_desc(String uppy_file_desc) {
    	this.uppy_file_desc = uppy_file_desc;
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
    DrDocRepoModel drDocRepoModel = null;
    @OneToOne(targetEntity=DrDocRepoModel.class, optional=true, fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="dr_doc_id", name="dr_doc_id", insertable=false, updatable=false, nullable = true)
    public DrDocRepoModel getDrDocRepoModel() {
            return drDocRepoModel;
    }
    public void setDrDocRepoModel(DrDocRepoModel drDocRepoModel) {
            this.drDocRepoModel = drDocRepoModel;
    }
    
    DrDocRepoModel drDocRepoModel2 = null;
    @OneToOne(targetEntity=DrDocRepoModel.class, optional=true, fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="dr_doc_id", name="dr_doc_id_2", insertable=false, updatable=false, nullable = true)
    public DrDocRepoModel getDrDocRepoModel2() {
            return drDocRepoModel2;
    }
    public void setDrDocRepoModel2(DrDocRepoModel drDocRepoModel) {
            this.drDocRepoModel2 = drDocRepoModel;
    }
}
