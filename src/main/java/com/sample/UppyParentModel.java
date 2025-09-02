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
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.ModelBase;
import com.sains.framework.model.DrDocRepoModel;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name="t_uppy_parent")
@NamedQueries({
    @NamedQuery(name = "UppyParentModel.findBy_createdBy",query = "from UppyParentModel where created_by = :createdBy"),
})
public class UppyParentModel extends ModelBase implements java.io.Serializable {
    private String uppy_parent_id = "";
    private String uppy_parent_desc = null;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Uppy_parent_id", "Uppy_parent_desc", "Uppy_parent_id"};


    private final String[] columnLength = new String[] {"uppy_parent_desc:20","uppy_parent_desc:30"};
    private Map columnLengthMap = null;

    public UppyParentModel() {
        setupMyChildList("getChildList", UppyModel.class, "childDeleted", Boolean.FALSE);
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="uppy_parent_id")
    public String getUppy_parent_id() {
    	return uppy_parent_id;
    }

    public void setUppy_parent_id(String uppy_id) {
    	this.uppy_parent_id = uppy_id;
    }

    @Transient 
    public String getID() { 
        return uppy_parent_id; 
    } 

    public void setID(String pk_id) {
        this.uppy_parent_id = pk_id;
    }

    @Column(name="uppy_parent_desc")
    public String getUppy_parent_desc() {
        return uppy_parent_desc;
    }

    public void setUppy_parent_desc(String uppy_parent_desc) {
        this.uppy_parent_desc = uppy_parent_desc;
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

    private List<UppyModel> childList = new ArrayList();
    @OneToMany(targetEntity=UppyModel.class, fetch=FetchType.LAZY, mappedBy="uppy_parent_id")
    public List<UppyModel> getChildList() {
            return childList;
    }
    public void setChildList(List<UppyModel> childList) {
            this.childList = childList;
    }
    
    private List<DrDocRepoModel> docRepoList = new ArrayList();
    @Transient
    public List<DrDocRepoModel> getDocRepoList() {
        return docRepoList;
    }
    public void setDocRepoList(List<DrDocRepoModel> docRepoList) {
        this.docRepoList = docRepoList;
    }
    
}
