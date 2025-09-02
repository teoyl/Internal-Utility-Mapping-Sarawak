/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.model;

import com.sains.common.util.Formatter;
import com.sains.common.util.Validator;
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

@Entity
@Table(name = "US_PAYMENT_ITEM")
public class PaymentItemModel extends ModelBase implements java.io.Serializable{
    private String payitem_id;
    private String payment_id;
    private String item_ref_no;
    private String item_desc;
    private Double item_amount = 0.00;
    private Integer rvs_receiptno;
    private Integer rvs_depositno;
    private String rvs_posted = "N";
    private String rvr_code;
    private String rvr_code_str;
    private String remark;
    private String subcode;
    private String subcode_str;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{};
    
    public String[] columnLength = new String[]{};
    
    public static final class RVS_POSTED {
        public static final String NOT_FOR_POSTING_TO_RVS = "X"; //counter payment
    }

    @Id
    @Column(name = "payitem_id")
    public String getPayitem_id() {
        return payitem_id;
    }

    public void setPayitem_id(String payitem_id) {
        this.payitem_id = payitem_id;
    }

    @Column(name = "payment_id")
    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    @Column(name = "item_ref_no")
    public String getItem_ref_no() {
        return item_ref_no;
    }

    public void setItem_ref_no(String item_ref_no) {
        this.item_ref_no = item_ref_no;
    }

    @Column(name = "item_desc")
    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    @Column(name = "item_amount")
    public Double getItem_amount() {
        return item_amount;
    }

    public void setItem_amount(Double item_amount) {
        this.item_amount = item_amount;
    }
    
    @Transient
    public String getitemAmount() {
        return Formatter.formatCurrency(item_amount);
    }

    @Column(name = "rvs_receiptno")
    public Integer getRvs_receiptno() {
        return rvs_receiptno;
    }

    public void setRvs_receiptno(Integer rvs_receiptno) {
        this.rvs_receiptno = rvs_receiptno;
    }

    @Column(name = "rvs_depositno")
    public Integer getRvs_depositno() {
        return rvs_depositno;
    }

    public void setRvs_depositno(Integer rvs_depositno) {
        this.rvs_depositno = rvs_depositno;
    }

    @Column(name = "rvs_posted")
    public String getRvs_posted() {
        return rvs_posted;
    }

    public void setRvs_posted(String rvs_posted) {
        this.rvs_posted = rvs_posted;
    }

    @Column(name = "rvr_code")
    public String getRvr_code() {
        return rvr_code;
    }

    public void setRvr_code(String rvr_code) {
        this.rvr_code = rvr_code;
    }
    
    @Transient
    public String getRvr_code_str() {
        CommonFunction cf = new CommonFunction();
        if(Validator.isEmpty(subcode)) {
            rvr_code_str = cf.getRvrcodeDesc(rvr_code);
        } else {
            rvr_code_str = cf.getRvrSubcodeDesc(rvr_code, subcode);
        }
        return rvr_code_str;
    }

    @Column(name = "subcode")
    public String getSubcode() {
        return subcode;
    }

    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }
    
    @Transient
    public String getSubcode_str() {
        CommonFunction cf = new CommonFunction();
        subcode_str = cf.getRvrSubcodeDesc(rvr_code, subcode);
        return subcode_str;
    }

    public void setSubcode_str(String subcode_str) {
        this.subcode_str = subcode_str;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public void setColumnLength(String[] columnLength) {
        this.columnLength = columnLength;
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
    
    
}
