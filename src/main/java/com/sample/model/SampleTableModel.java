package com.sample.model;

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
@Table(name="t_sample_table")
public class SampleTableModel extends ModelBase implements java.io.Serializable {
    private String sample_table_id = "";
    private String created_by;
    private java.sql.Timestamp created_date;
    private java.sql.Timestamp date_column;
    private java.sql.Timestamp datetime_column;
    private Double double_column;
    private Integer integer_column;
    private String string_column;
    private String long_description;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Sample_table_id","Date_column","Datetime_column","Double_column","Integer_column","String_column","Long_description"};


    private final String[] columnLength = new String[] {"sample_table_id:20","string_column:50", "long_description:100"};
    private Map columnLengthMap = null;

    public SampleTableModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="sample_table_id")
    public String getSample_table_id() {
    	return sample_table_id;
    }

    public void setSample_table_id(String sample_table_id) {
    	this.sample_table_id = sample_table_id;
    }

    @Transient 
    public String getID() { 
        return sample_table_id; 
    } 

    public void setID(String pk_id) {
        this.sample_table_id = pk_id;
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

    @Column(name="date_column")
    public java.sql.Timestamp getDate_column() {
    	return date_column;
    }

    public void setDate_column(java.sql.Timestamp date_column) {
    	this.date_column = date_column;
    }

    @Transient
    public String getDate_column_str() {
        return Formatter.formatDate(date_column, SystemConstants.DATE.dataEntryFormat);    }

    public void setDate_column_str(String date_column) {
        try {
            this.date_column = DateUtil.getTimestampFromDate(DateUtil.getDate(date_column, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

    @Column(name="datetime_column")
    public java.sql.Timestamp getDatetime_column() {
    	return datetime_column;
    }

    public void setDatetime_column(java.sql.Timestamp datetime_column) {
    	this.datetime_column = datetime_column;
    }

    @Transient
    public String getDatetime_column_str() {
        return Formatter.formatDate(datetime_column, SystemConstants.DATE.dataEntryFormat);    }

    public void setDatetime_column_str(String datetime_column) {
        try {
            this.datetime_column = DateUtil.getTimestampFromDate(DateUtil.getDate(datetime_column, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

    @Column(name="double_column")
    public Double getDouble_column() {
    	return double_column;
    }

    public void setDouble_column(Double double_column) {
    	this.double_column = double_column;
    }

    @Transient
    public String getDouble_column_str() {
        return double_column==null?"":Formatter.formatDecimal(double_column.toString(), Formatter.CURRENCY_PATTERN_PLAIN);
    }

    public void setDouble_column_str(String double_column) {
        try {
            this.double_column = Double.parseDouble(double_column.replaceAll(",", ""));
        } catch (Exception e){}
    }

    @Column(name="integer_column")
    public Integer getInteger_column() {
    	return integer_column;
    }

    public void setInteger_column(Integer integer_column) {
    	this.integer_column = integer_column;
    }

    @Transient
    public String getInteger_column_str() {
        return integer_column==null?"":Formatter.formatDecimal(integer_column.toString(), Formatter.CURRENCY_PATTERN_PLAIN2);
    }

    public void setInteger_column_str(String integer_column) {
        try {
            this.integer_column = Integer.parseInt(integer_column.replaceAll(",", ""));
        } catch (Exception e){}
    }

    @Column(name="string_column")
    public String getString_column() {
    	return string_column;
    }

    public void setString_column(String string_column) {
    	this.string_column = string_column;
    }

    @Column(name="long_description")
    public String getLong_description() {
        return long_description;
    }

    public void setLong_description(String long_description) {
        this.long_description = long_description;
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
