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
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.ModelBase;
import com.sample.ChildModel;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OrderBy;

@Entity
@Table(name="t_dq_template")
public class DqTemplateModel extends ModelBase implements java.io.Serializable {
    private Integer tmpl_id;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String tmpl_cond;
    private String tmpl_desc;
    private Integer tmpl_ds_id;
    private String tmpl_name;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Tmpl_id","Tmpl_cond","Tmpl_desc","Tmpl_ds_id","Tmpl_name"};


    private final String[] columnLength = new String[] {"tmpl_cond:500","tmpl_desc:500","tmpl_name:100"};
    private Map columnLengthMap = null;

    public DqTemplateModel() {
        userDefined_autoValidation(Boolean.TRUE);
        userDefined_insertNoDuplicate("tmpl_name;DqTemplate.tmpl_name");
        userDefined_insertRequired("tmpl_name;DqTemplate.tmpl_name,tmpl_desc;DqTemplate.tmpl_desc");
        userDefined_updateRequired("tmpl_name;DqTemplate.tmpl_name,tmpl_desc;DqTemplate.tmpl_desc");
        setupMyChildList("getTemplateFieldList", DqTemplateFieldModel.class, "fieldDeleted", Boolean.TRUE);
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }

@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tmpl_id")
    public Integer getTmpl_id() {
        return tmpl_id;
    }

    public void setTmpl_id(Integer tmpl_id) {
        this.tmpl_id = tmpl_id;
    }

    @Transient
    public String getID() {
        if (tmpl_id == null) {
            return null;
        }
        return tmpl_id.toString(); 
    }

    public void setID(String tmpl_id) {
        if (Validator.isEmpty(tmpl_id)) {
            setTmpl_id(null);
        } else {
            try {
                this.tmpl_id = Integer.parseInt(tmpl_id);
            } catch (Exception e) {
                setTmpl_id(null);
            }
        }
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

    @Column(name="tmpl_cond")
    public String getTmpl_cond() {
    	return tmpl_cond;
    }

    public void setTmpl_cond(String tmpl_cond) {
    	this.tmpl_cond = tmpl_cond;
    }

    @Column(name="tmpl_desc")
    public String getTmpl_desc() {
    	return tmpl_desc;
    }

    public void setTmpl_desc(String tmpl_desc) {
    	this.tmpl_desc = tmpl_desc;
    }

    @Column(name="tmpl_ds_id")
    public Integer getTmpl_ds_id() {
    	return tmpl_ds_id;
    }

    public void setTmpl_ds_id(Integer tmpl_ds_id) {
    	this.tmpl_ds_id = tmpl_ds_id;
    }

    @Transient
    public String getTmpl_ds_id_str() {
        return tmpl_ds_id==null?"":Formatter.formatDecimal(tmpl_ds_id.toString(), Formatter.CURRENCY_PATTERN_PLAIN2);
    }

    public void setTmpl_ds_id_str(String tmpl_ds_id) {
        try {
            this.tmpl_ds_id = Integer.parseInt(tmpl_ds_id.replaceAll(",", ""));
        } catch (Exception e){}
    }

    @Column(name="tmpl_name")
    public String getTmpl_name() {
    	return tmpl_name;
    }

    public void setTmpl_name(String tmpl_name) {
    	this.tmpl_name = tmpl_name;
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
    private List templateFieldList;
    @OneToMany(targetEntity = DqTemplateFieldModel.class, fetch = FetchType.LAZY, mappedBy = "fld_tmpl_id")
    @OrderBy(value = "fld_order asc")
    public List<DqTemplateFieldModel> getTemplateFieldList() {
        return templateFieldList;
    }
    public void setTemplateFieldList(List templateFieldList) {
        this.templateFieldList = templateFieldList;
    }
}
