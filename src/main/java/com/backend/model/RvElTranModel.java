/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend.model;

import javax.persistence.IdClass;
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

// rename RVEBPPTRANTMP to RVELTRAN @24.11.2011 ai min
@Entity
//@IdClass(RvElTranPkModel.class)
@Table(name="RVELTRAN01")
//@Table(name="RVEBPPTRANTMP01")
public class RvElTranModel extends ModelBase implements java.io.Serializable {

    //AiMin @15.11.2017
    public static final class SOURCE {;
         public static final String LXG_COMMON = "LXG"; //to confirm again
    }
    
    //AiMin @15.11.2017
    public static final class PAY_TYPE {
         public static final String CASH_TRANSACTION="C";
    }
     
    private String ebpp_voucher_no;
    private String ebpp_receipt_no;
    private String trn_div;
    private String trn_type;
    private String trn_dist;
    private String trn_bs;
    private String trn_lot;
    private String trn_ver;
    private java.sql.Timestamp pay_date;
    private Double pay_amount;
    private java.sql.Timestamp create_date;
    private String imp_flag;
    private String elasis_payid;
    private String pay_type;
    private String trancode;
    private String subcode;
    private String pay_method;
    private String doc_no;
    private String div_no = "01";
    private String source = "";  //added by ahmadni 20/1/2012  
    private String ecase_ref = "";  //added by ahmadni 29/3/2012
    
    //AiMIn @4.11.2017 [start]
    private String job_no = "";  
    private String id_type = "";  
    private String id_num = "";  
    private String name = "";  
    private String laas_payid = "";  
    private String remarks = "";  
    //AiMIn @4.11.2017 [end]
    
    private final String[] updatableColumns = new String[] {"Ebpp_voucher_no", "Ebpp_receipt_no", "Trn_div", "Trn_type"};

    private static String insertColumns = "Ebpp_voucher_no,Ebpp_receipt_no,Trn_div,Trn_type,Trn_dist,Trn_bs,Trn_lot,Trn_ver,Pay_date,Pay_amount,"
            + "Create_date,Imp_flag,Elasis_payid,Pay_type,Trancode,Subcode,Pay_method,Doc_no,Div_no,Source,"
            + "Ecase_ref,Job_no,Id_type,Id_num,Remarks,Name,Laas_payid";
    
    //AiMin @11.8.2020. Remark: To cater counter spay.
    private static String insertColumns_extra4Remit = ",Imp_remit_flag,Remittance_ref,Remittance_date";

    public RvElTranModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }

    @Column(name="ebpp_receipt_no")
    @Id public String getEbpp_receipt_no() {
    	return ebpp_receipt_no;
    }

    public void setEbpp_receipt_no(String ebpp_receipt_no) {
    	this.ebpp_receipt_no = ebpp_receipt_no;
    }

    @Column(name="pay_date")
    @Id public java.sql.Timestamp getPay_date() {
    	return pay_date;
    }

    public void setPay_date(java.sql.Timestamp pay_date) {
    	this.pay_date = pay_date;
    }

    @Transient
    public String getPay_date_str() {
        return Formatter.formatDate(pay_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setPay_date_str(String pay_date) {
        try {
            this.pay_date = DateUtil.getTimestampFromDate(DateUtil.getDate(pay_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }


    @Column(name="ebpp_voucher_no")
    public String getEbpp_voucher_no() {
    	return ebpp_voucher_no;
    }

    public void setEbpp_voucher_no(String ebpp_voucher_no) {
    	this.ebpp_voucher_no = ebpp_voucher_no;
    }

    

    @Column(name="trn_div")
    public String getTrn_div() {
    	return trn_div;
    }

    public void setTrn_div(String trn_div) {
    	this.trn_div = trn_div;
    }

    @Column(name="trn_type")
    public String getTrn_type() {
    	return trn_type;
    }

    public void setTrn_type(String trn_type) {
    	this.trn_type = trn_type;
    }

    @Column(name="trn_dist")
    public String getTrn_dist() {
    	return trn_dist;
    }

    public void setTrn_dist(String trn_dist) {
    	this.trn_dist = trn_dist;
    }

    @Column(name="trn_bs")
    public String getTrn_bs() {
    	return trn_bs;
    }

    public void setTrn_bs(String trn_bs) {
    	this.trn_bs = trn_bs;
    }

    @Column(name="trn_lot")
    public String getTrn_lot() {
    	return trn_lot;
    }

    public void setTrn_lot(String trn_lot) {
    	this.trn_lot = trn_lot;
    }

    @Column(name="trn_ver")
    public String getTrn_ver() {
    	return trn_ver;
    }

    public void setTrn_ver(String trn_ver) {
    	this.trn_ver = trn_ver;
    }

    

    @Column(name="pay_amount")
    public Double getPay_amount() {
    	return pay_amount;
    }

    public void setPay_amount(Double pay_amount) {
    	this.pay_amount = pay_amount;
    }

    @Column(name="create_date")
    public java.sql.Timestamp getCreate_date() {
    	return create_date;
    }

    public void setCreate_date(java.sql.Timestamp create_date) {
    	this.create_date = create_date;
    }

    @Transient
    public String getCreate_date_str() {
        return Formatter.formatDate(create_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setCreate_date_str(String create_date) {
        try {
            this.create_date = DateUtil.getTimestampFromDate(DateUtil.getDate(create_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

    @Column(name="imp_flag")
    public String getImp_flag() {
    	return imp_flag;
    }

    public void setImp_flag(String imp_flag) {
    	this.imp_flag = imp_flag;
    }

    @Column(name="elasis_payid")
    @Id public String getElasis_payid() {
    	return elasis_payid;
    }

    public void setElasis_payid(String elasis_payid) {
    	this.elasis_payid = elasis_payid;
    }

    @Column(name="pay_type")
    public String getPay_type() {
    	return pay_type;
    }

    public void setPay_type(String pay_type) {
    	this.pay_type = pay_type;
    }

    @Column(name="trancode")
    public String getTrancode() {
    	return trancode;
    }

    public void setTrancode(String trancode) {
    	this.trancode = trancode;
    }

    @Column(name="subcode")
    public String getSubcode() {
    	return subcode;
    }

    public void setSubcode(String subcode) {
    	this.subcode = subcode;
    }

    @Column(name="pay_method")
    public String getPay_method() {
    	return pay_method;
    }

    public void setPay_method(String pay_method) {
    	this.pay_method = pay_method;
    }

    @Column(name="doc_no")
    public String getDoc_no() {
    	return doc_no;
    }

    public void setDoc_no(String doc_no) {
    	this.doc_no = doc_no;
    }

    @Column(name="div_no")
    public String getDiv_no() {
    	return div_no;
    }

    public void setDiv_no(String div_no) {
    	this.div_no = div_no;
    }

    //[Start] @12.9.2013. Commented. Reason: For eMAP LIVE.
    //added by ahmadni 20/1/2012
    @Column(name="source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    //[End] @12.9.2013.

    /* ******** Write your code after this line ****** */
    //[Start] @12.9.2013. Commented. Reason: For eMAP LIVE.
    //Remarks: To store ecase no for RVS Reporting.
    //added by ahmadni 29/3/2012
    @Column(name="ecase_ref")
    public String getEcase_ref() {
        return ecase_ref;
    }

    public void setEcase_ref(String ecase_ref) {
        this.ecase_ref = ecase_ref;
    }

//[End] @12.9.2013.
    
    @Column(name="job_no")
    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }
    
    @Column(name="id_type")
    public String getId_type() {
        return id_type;
    }

    public void setId_type(String id_type) {
        this.id_type = id_type;
    }

    @Column(name="id_num")
    public String getId_num() {
        return id_num;
    }

    public void setId_num(String id_num) {
        this.id_num = id_num;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    @Column(name="laas_payid")
    public String getLaas_payid() {
        return laas_payid;
    }


    public void setLaas_payid(String laas_payid) {
        this.laas_payid = laas_payid;
    }

    @Column(name="remarks")
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public static String getInsertColumns() {
        return insertColumns;
    }
    
    public static String getInsertColumns_extra4Remit() {
        return insertColumns_extra4Remit;
    }
    

    @Override
    public void setCreated_date(Timestamp createdDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void setUpdated_date(Timestamp updatedDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }
}

