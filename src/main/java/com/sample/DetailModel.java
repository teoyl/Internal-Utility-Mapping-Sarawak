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
import com.sains.common.util.Validator;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.ModelBase;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.hibernate.Session;

@Entity
@Table(name="t_detail")
public class DetailModel extends ModelBase implements java.io.Serializable {
    private Integer detail_id = null;
    private Double amount;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String detail_desc;
    private String detail_status;
    private Integer master_id;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Detail_id","Amount","Detail_desc","Detail_status","Master_id"};


    private final String[] columnLength = new String[] {"detail_id:20","detail_desc:30","detail_status:5","master_id:20"};
    private Map columnLengthMap = null;

    public DetailModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


//    @Id
//    @Column(name="detail_id")
//    public String getDetail_id() {
//    	return detail_id;
//    }
//
//    public void setDetail_id(String detail_id) {
//    	this.detail_id = detail_id;
//    }
//
//    @Transient 
//    public String getID() { 
//        return detail_id;
//    } 
//
//    public void setID(String pk_id) {
//        this.detail_id = pk_id;
//    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="detail_id")
    public Integer getDetail_id() {
    	return detail_id;
    }

    public void setDetail_id(Integer detail_id) {
    	this.detail_id = detail_id;
    }

    @Transient 
    public String getID() { 
        if (detail_id == null) {
            return null;
        }
        return detail_id.toString(); 
    } 

    public void setID(String pk_id) {
        if (Validator.isEmpty(pk_id)) {
            detail_id = null;
        } else {
            this.detail_id = Integer.parseInt(pk_id);
        }
    }

    @Column(name="amount")
    public Double getAmount() {
    	return amount;
    }

    public void setAmount(Double amount) {
    	this.amount = amount;
    }

    @Transient
    public String getAmount_str() {
        return amount==null?"":Formatter.formatDecimal(amount.toString(), Formatter.CURRENCY_PATTERN_PLAIN);
    }

    public void setAmount_str(String amount) {
        try {
            this.amount = Double.parseDouble(amount.replaceAll(",", ""));
        } catch (Exception e){}
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

    @Column(name="detail_desc")
    public String getDetail_desc() {
    	return detail_desc;
    }

    public void setDetail_desc(String detail_desc) {
    	this.detail_desc = detail_desc;
    }

    @Column(name="detail_status")
    public String getDetail_status() {
    	return detail_status;
    }

    public void setDetail_status(String detail_status) {
    	this.detail_status = detail_status;
    }

    @Column(name="master_id")
    public Integer getMaster_id() {
    	return master_id;
    }

    public void setMaster_id(Integer master_id) {
    	this.master_id = master_id;
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
//    @Override
//    public void preInsert(Session session) throws Exception {
//        throw new CustomBaseException("Some error happen");
//    }
}
