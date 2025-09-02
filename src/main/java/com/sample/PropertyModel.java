package com.sample;

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
import com.sains.framework.base.ModelBase;

@Entity
@Table(name="t_property")
public class PropertyModel extends ModelBase implements java.io.Serializable {
    private String property_id = "";
    private String created_by;
    private java.sql.Timestamp created_date;
    private String parent_id;
    private String property_desc;
    private Double property_value;
    private java.sql.Timestamp purchased_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Property_id","Parent_id","Property_desc","Property_value","Purchased_date"};


    private final String[] columnLength = new String[] {"property_id:20","parent_id:20","property_desc:30"};
    private Map columnLengthMap = null;

    public PropertyModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="property_id")
    public String getProperty_id() {
    	return property_id;
    }

    public void setProperty_id(String property_id) {
    	this.property_id = property_id;
    }

    @Transient 
    public String getID() { 
        return property_id; 
    } 

    public void setID(String pk_id) {
        this.property_id = pk_id;
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

    @Column(name="parent_id")
    public String getParent_id() {
    	return parent_id;
    }

    public void setParent_id(String parent_id) {
    	this.parent_id = parent_id;
    }

    @Column(name="property_desc")
    public String getProperty_desc() {
    	return property_desc;
    }

    public void setProperty_desc(String property_desc) {
    	this.property_desc = property_desc;
    }

    @Column(name="property_value")
    public Double getProperty_value() {
    	return property_value;
    }

    public void setProperty_value(Double property_value) {
    	this.property_value = property_value;
    }

    @Transient
    public String getProperty_value_str() {
        return property_value==null?"":Formatter.formatDecimal(property_value.toString(), Formatter.CURRENCY_PATTERN_PLAIN);
    }

    public void setProperty_value_str(String property_value) {
        try {
            this.property_value = Double.parseDouble(property_value.replaceAll(",", ""));
        } catch (Exception e){}
    }

    @Column(name="purchased_date")
    public java.sql.Timestamp getPurchased_date() {
    	return purchased_date;
    }

    public void setPurchased_date(java.sql.Timestamp purchased_date) {
    	this.purchased_date = purchased_date;
    }

    @Transient
    public String getPurchased_date_str() {
        return Formatter.formatDate(purchased_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setPurchased_date_str(String purchased_date) {
        try {
            this.purchased_date = DateUtil.getTimestampFromDate(DateUtil.getDate(purchased_date, SystemConstants.DATE.dataEntryFormat));
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
