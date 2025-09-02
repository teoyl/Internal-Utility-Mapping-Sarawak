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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name="t_dq_template_field")
public class DqTemplateFieldModel extends ModelBase implements java.io.Serializable {
    private Integer fld_id;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String fld_col;
    private String fld_lbl;
    private Integer fld_order;
    private String fld_showMe;
    private String fld_helperText;
    private Integer fld_tmpl_id;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Fld_id","Fld_col","Fld_lbl","Fld_order","Fld_showMe","Fld_helperText"};


    private final String[] columnLength = new String[] {"fld_col:100","fld_lbl:250"};
    private Map columnLengthMap = null;

    public DqTemplateFieldModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fld_id")
    public Integer getFld_id() {
        return fld_id;
    }

    public void setFld_id(Integer fld_id) {
        this.fld_id = fld_id;
    }

    @Transient
    public String getID() {
        if (fld_id == null) {
            return null;
        }
        return fld_id.toString(); 
    }

    public void setID(String fld_id) {
        if (Validator.isEmpty(fld_id)) {
            setFld_id(null);
        } else {
            try {
                this.fld_id = Integer.parseInt(fld_id);
            } catch (Exception e) {
                setFld_id(null);
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

    @Column(name="fld_col")
    public String getFld_col() {
    	return fld_col;
    }

    public void setFld_col(String fld_col) {
    	this.fld_col = fld_col;
    }

    @Column(name="fld_lbl")
    public String getFld_lbl() {
    	return fld_lbl;
    }

    public void setFld_lbl(String fld_lbl) {
    	this.fld_lbl = fld_lbl;
    }

    @Column(name="fld_order")
    public Integer getFld_order() {
        return fld_order;
    }

    public void setFld_order(Integer fld_order) {
        this.fld_order = fld_order;
    }
    
    @Transient
    public String getFld_order_str() {
        return fld_order==null?"":Formatter.formatDecimal(fld_order.toString(), Formatter.CURRENCY_PATTERN_PLAIN2);
    }

    public void setFld_order_str(String fld_order) {
        try {
            this.fld_order = Integer.parseInt(fld_order.replaceAll(",", ""));
        } catch (Exception e){}
    }

    @Column(name="fld_showMe")
    public String getFld_showMe() {
        return fld_showMe;
    }

    public void setFld_showMe(String fld_showMe) {
        this.fld_showMe = fld_showMe;
    }
    
    @Transient
    public Boolean getFld_showMe_boo() {
        try {
            return fld_showMe.equals("Y");
        } catch (Exception e) {}
        return Boolean.FALSE;
    }

    @Column(name="fld_helperText")
    public String getFld_helperText() {
        return fld_helperText;
    }

    public void setFld_helperText(String fld_helperText) {
        this.fld_helperText = fld_helperText;
    }

    @Column(name="fld_tmpl_id")
    public Integer getFld_tmpl_id() {
    	return fld_tmpl_id;
    }

    public void setFld_tmpl_id(Integer fld_tmpl_id) {
    	this.fld_tmpl_id = fld_tmpl_id;
    }

    @Transient
    public String getFld_tmpl_id_str() {
        return fld_tmpl_id==null?"":Formatter.formatDecimal(fld_tmpl_id.toString(), Formatter.CURRENCY_PATTERN_PLAIN2);
    }

    public void setFld_tmpl_id_str(String fld_tmpl_id) {
        try {
            this.fld_tmpl_id = Integer.parseInt(fld_tmpl_id.replaceAll(",", ""));
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

}
