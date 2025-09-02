/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lxg.prepayment.model;

/**
 *
 * @author joveni.h
 */
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
import java.sql.Timestamp;

@Entity
@Table(name="PP_TRAN")
public class PpTranModel extends ModelBase implements java.io.Serializable {
    private String tran_id = "";
    private String bal_id;
    private java.sql.Timestamp tran_date;
    private Double tran_amount;
    private String tran_type;
    private String tran_desc;
    private String tran_payment_id;
    private java.sql.Timestamp created_date;
    private String created_by;
    private java.sql.Timestamp updated_date;
    private String updated_by;
    private String rvs_posted;
    private Integer rvs_receipt_no; // modified @6.10.2011 ai min. Reason : Change String to Integer
    private java.sql.Timestamp status_date; // Mike 2020-09-04:: ELASIS CASHLESS
    private String pay_ref_no; // Mike 2020-09-04 :: ELASIS CASHLESS


    public String[] updatableColumns = new String[] {"Tran_id","Bal_id","Tran_date","Tran_amount","Tran_type","Tran_desc","Tran_payment_id","Rvs_receipt_no"};


    private final String[] columnLength = new String[] {"tran_id:16","bal_id:16","tran_type:3","tran_desc:20","Tran_payment_id:16","Rvs_receipt_no:6"};
    private Map columnLengthMap = null;

//    public PpTranModel() {
//        //userDefined_autoValidation(Boolean.TRUE);
//        //userDefined_insertNoDuplicate("module_code;module.code");
//        //userDefined_validateRecursive(Boolean.TRUE);
//        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
//    }


    @Id
    @Column(name="tran_id")
    public String getTran_id() {
    	return tran_id;
    }

    public void setTran_id(String tran_id) {
    	this.tran_id = tran_id;
    }

    @Transient 
    public String getID() { 
        return tran_id; 
    } 

    public void setID(String pk_id) {
        this.tran_id = pk_id;
    }

    @Column(name="bal_id")
    public String getBal_id() {
    	return bal_id;
    }

    public void setBal_id(String bal_id) {
    	this.bal_id = bal_id;
    }

    @Column(name="tran_date")
    public java.sql.Timestamp getTran_date() {
    	return tran_date;
    }

    public void setTran_date(java.sql.Timestamp tran_date) {
    	this.tran_date = tran_date;
    }

    @Transient
    public String getTran_date_str() {
        return Formatter.formatDate(tran_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setTran_date_str(String tran_date) {
        try {
            this.tran_date = DateUtil.getTimestampFromDate(DateUtil.getDate(tran_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

    @Column(name="tran_amount")
    public Double getTran_amount() {
    	return tran_amount;
    }

    public void setTran_amount(Double tran_amount) {
    	this.tran_amount = tran_amount;
    }

    @Column(name="tran_type")
    public String getTran_type() {
    	return tran_type;
    }

    public void setTran_type(String tran_type) {
    	this.tran_type = tran_type;
    }

    @Column(name="tran_desc")
    public String getTran_desc() {
    	return tran_desc;
    }

    public void setTran_desc(String tran_desc) {
    	this.tran_desc = tran_desc;
    }

    @Column(name="tran_payment_id")
    public String getTran_payment_id() {
        return tran_payment_id;
    }

    public void setTran_payment_id(String tran_payment_id) {
        this.tran_payment_id = tran_payment_id;
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

    @Column(name="created_by")
    public String getCreated_by() {
    	return created_by;
    }

    public void setCreated_by(String created_by) {
    	this.created_by = created_by;
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

    @Column(name="updated_by")
    public String getUpdated_by() {
    	return updated_by;
    }

    public void setUpdated_by(String updated_by) {
    	this.updated_by = updated_by;
    }

    @Column(name="rvs_posted")
    public String getRvs_posted() {
        return rvs_posted;
    }

    public void setRvs_posted(String rvs_posted) {
        this.rvs_posted = rvs_posted;
    }

    @Column(name = "rvs_receipt_no")
    public Integer getRvs_receipt_no() {
        return rvs_receipt_no;
    }

    public void setRvs_receipt_no(Integer rvs_receipt_no) {
        this.rvs_receipt_no = rvs_receipt_no;
    }

    @Column(name="status_date")
    public Timestamp getStatus_date() {
        return status_date;
    }

    public void setStatus_date(Timestamp status_date) {
        this.status_date = status_date;
    }
    
    @Column(name="pay_ref_no")
    public String getPay_ref_no() {
        return pay_ref_no;
    }

    public void setPay_ref_no(String pay_ref_no) {
        this.pay_ref_no = pay_ref_no;
    }

    @Transient
    public String getStatus_date_str() {
        return Formatter.formatDate(status_date, SystemConstants.DATE.dataEntryFormat);
    }

    public void setStatus_date_str(String status_date) {
        try {
            this.status_date = DateUtil.getTimestampFromDate(DateUtil.getDate(status_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e) {
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
    private PpBalanceModel balanceModel;
    @OneToOne(targetEntity = PpBalanceModel.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "bal_id", name = "bal_id", insertable = false, updatable = false, nullable = true)
    public PpBalanceModel getBalanceModel() {
        return balanceModel;
    }

    public void setBalanceModel(PpBalanceModel balanceModel) {
        this.balanceModel = balanceModel;
    }
}
