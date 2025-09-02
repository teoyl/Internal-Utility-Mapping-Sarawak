/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.model;

import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.ModelBase;
import com.sains.framework.base.RunningSeqFunction;
import com.utimaps.web.UtimapsAction;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "US_PAYMENT")
public class PaymentModel extends ModelBase implements java.io.Serializable{
    private String payment_id;
    private String case_id;
    private java.sql.Timestamp payment_date;
    private String payment_status;
    private java.sql.Timestamp payment_status_date;
    private String bill_ref_no;
    private String payment_remarks;
    private Double payment_amount = 0.00;
    private String payment_method;
    private String platform;
    private String pay_ref_no;
    private String div_code;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    
    private String ism_payhist_id;

    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{"Payment_id", "Case_id", "Bill_ref_no", "Payment_date", "Payment_status", "Payment_status_date", "Payment_amount", "Payment_remarks", "Payment_method", "Pay_ref_no"};

    public String[] columnLength = new String[]{};
    
    //@3.7.2018
    public static final class PLATFORM {

        public static final String MOBILE = "M"; //From mobile
        public static final String WEB = "W"; //From web
        public static final String COUNTER = "C"; //From Web
    }
    
    public static final class PAYMENT_STATUS {

        public static final String PAYMENT_DRAFT = "PD";
        public static final String PENDING_PAYMENT = "PP";
        public static final String PAYMENT_IN_PROGRESS = "PI";
        public static final String PAYMENT_COMPLETED = "PC";
        public static final String PAYMENT_EXPIRED = "PE";
        public static final String PAYMENT_REJECTED = "PX";
    }
    
    //@29.3.2019
    public static final class PAYMENT_METHOD {

        public static final String SWK_PAY = "SPY";
        public static final String EBPP = "EBP";
    }
    
    public static final class OPERATION {

        public static final String UPD_BILL_REF_NO = "update_bill_ref_no";
        public static final String UPD_PAY_BYPREPAYMENT = "update_pay_bypreparement";
        public static final String UPD_PAY_PENDING = "update_pay_pending";
        public static final String UPD_PAY_COMPLETED = "update_payment_completed";
        public static final String UPD_PAY_REJECTED = "update_payment_rejected";
        public static final String UPD_PAY_EXPIRED = "update_payment_expired";
        
        //bill generation
        public static final String UPDATE_BILL_FEE = "updateBillFee";
        public static final String UPDATE_BILL_TRN = "updateBillTrn";
        public static final String UPDATE_BILL = "updateBill";

    }
    
    @Id
    @Column(name = "payment_id")
    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }
    
    @Transient
    public String getID() {
        return payment_id;
    }

    public void setID(String pk_id) {
        this.payment_id = pk_id;
    }
    
    @Column(name = "case_id")
    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    @Column(name = "payment_date")
    public Timestamp getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Timestamp payment_date) {
        this.payment_date = payment_date;
    }
    
    @Transient
    public String getPayment_date_str() {
        return Formatter.formatDate(payment_date, SystemConstants.DATE.dataEntryFormat);
    }

    @Column(name = "payment_status")
    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    @Column(name = "payment_status_date")
    public Timestamp getPayment_status_date() {
        return payment_status_date;
    }

    public void setPayment_status_date(Timestamp payment_status_date) {
        this.payment_status_date = payment_status_date;
    }

    @Column(name = "bill_ref_no")
    public String getBill_ref_no() {
        return bill_ref_no;
    }

    public void setBill_ref_no(String bill_ref_no) {
        this.bill_ref_no = bill_ref_no;
    }

    @Column(name = "payment_remarks")
    public String getPayment_remarks() {
        return payment_remarks;
    }

    public void setPayment_remarks(String payment_remarks) {
        this.payment_remarks = payment_remarks;
    }

    @Column(name = "payment_amount")
    public Double getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(Double payment_amount) {
        this.payment_amount = payment_amount;
    }
    
    @Transient
    public String getpaymentAmount() {
        return Formatter.formatCurrency(payment_amount);
    }

    @Column(name = "payment_method")
    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    @Column(name = "platform")
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Column(name = "pay_ref_no")
    public String getPay_ref_no() {
        return pay_ref_no;
    }

    public void setPay_ref_no(String pay_ref_no) {
        this.pay_ref_no = pay_ref_no;
    }

    @Column(name = "div_code")
    public String getDiv_code() {
        return div_code;
    }

    public void setDiv_code(String div_code) {
        this.div_code = div_code;
    }

    @Column(name = "ism_payhist_id")
    public String getIsm_payhist_id() {
        return ism_payhist_id;
    }

    public void setIsm_payhist_id(String ism_payhist_id) {
        this.ism_payhist_id = ism_payhist_id;
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
    
    private List<PaymentItemModel> paymentItemList = new ArrayList();
    @OneToMany(targetEntity = PaymentItemModel.class, fetch = FetchType.EAGER, mappedBy = "payment_id")

    public List<PaymentItemModel> getPaymentItemList() {
        return paymentItemList;
    }

    public void setPaymentItemList(List<PaymentItemModel> paymentItemList) {
        this.paymentItemList = paymentItemList;
    }
    
    @Transient
    public String getPayment_status_date_str() {
        return Formatter.formatDate(payment_status_date, SystemConstants.DATE.dataEntryFormat3);
    }

    public void setPayment_status_date_str(String payment_status_date_str) {
        try {
            this.payment_status_date = DateUtil.getTimestampFromDate(DateUtil.getDate(payment_status_date_str, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "PaymentModel", "PaymentModel", "setPayment_status_date_str");
        }
    }
    
    private ApplicationPModel appCaseModel;
    
    @Transient
    public ApplicationPModel getAppCaseModel() {
        return appCaseModel;
    }

    public void setAppCaseModel(ApplicationPModel appCaseModel) {
        this.appCaseModel = appCaseModel;
    }
    
    @Override
    public void preUpdate(org.hibernate.Session session, Object dataEntryModel) throws Exception {
        
        BaseDAOImpl dao = new BaseDAOImpl();

        setSession_(session);
        dao.setSession(session);

        PaymentModel updatingModel = (PaymentModel) dataEntryModel;

        System.out.println("## preUpdate PaymentID: " + updatingModel.getID() + " Operation: " + updatingModel.get_operation());
        
        if (updatingModel.get_operation().equals(OPERATION.UPD_PAY_COMPLETED)) { //call by PaymentCheck2
            updatingModel.setPayment_status(PaymentModel.PAYMENT_STATUS.PAYMENT_COMPLETED);

            //update application status
            ApplicationPModel casePbModel = (ApplicationPModel) dao.getModelByCode("case_id", updatingModel.getCase_id(), new ApplicationPModel());
            if (casePbModel == null) {
                    throw new CustomBaseException("app case not found");
                }
                if (casePbModel != null) {
                    System.out.println("appCaseModel not null");
                } else {
                    System.out.println("appCaseModel null");
                }
            casePbModel.setApp_status(UtimapsAction.PB_STATUS.APP_PAYMENT_COMPLETED);
            casePbModel.setWf_status(UtimapsAction.WF_STATUS.APPLICATION_PAYMENT_COMPLETED);
            casePbModel.defaultUpdateProperties();
            session.update(casePbModel);


        } else if (updatingModel.get_operation().equals(OPERATION.UPD_PAY_REJECTED)) { //call by PaymentCheck2 

            updatingModel.setPayment_status(PaymentModel.PAYMENT_STATUS.PENDING_PAYMENT);
           
            //update application status
            ApplicationPModel casePbModel = (ApplicationPModel) dao.getModelByCode("case_id", updatingModel.getAppCaseModel().getCase_id(), new ApplicationPModel());
            casePbModel.setApp_status(UtimapsAction.PB_STATUS.PENDING_APP_PAYMENT);
            casePbModel.defaultUpdateProperties();

        } else if (updatingModel.get_operation().equals(OPERATION.UPD_PAY_EXPIRED)) { //call by PaymentCheck2
//            updatingModel.setPayment_status(PaymentModel.PAYMENT_STATUS.PENDING_PAYMENT);
            updatingModel.setPayment_status(PaymentModel.PAYMENT_STATUS.PAYMENT_EXPIRED);

            //update application status
            ApplicationPModel casePbModel = (ApplicationPModel) dao.getModelByCode("case_id", updatingModel.getAppCaseModel().getCase_id(), new ApplicationPModel());
            casePbModel.setApp_status(UtimapsAction.PB_STATUS.PENDING_APP_PAYMENT);
            casePbModel.defaultUpdateProperties();
            session.update(casePbModel);
        } else if (updatingModel.get_operation().equals(OPERATION.UPD_BILL_REF_NO)) { 
            System.out.println("in UPD_BILL_REF_NO");
            String divCode = "";
            System.out.println("updatingModel " + updatingModel.getPayment_id());
            ApplicationPModel casePbModel = (ApplicationPModel) dao.getModelByCode("case_id", updatingModel.getAppCaseModel().getCase_id(), new ApplicationPModel());
            
                if (casePbModel == null) {
                    throw new CustomBaseException("app case not found");
                }
                if (casePbModel != null) {
                    System.out.println("appCaseModel not null");
                } else {
                    System.out.println("appCaseModel null");
                }
//            }
            divCode = updatingModel.getDiv_code();
//            divCode = "01"; //testing

            Calendar cal = Calendar.getInstance();

            String strYear = Integer.toString(cal.get(Calendar.YEAR));
            System.out.println("year" + strYear);
            System.out.println("divCode " + divCode);
            String billRef = RunningSeqFunction.getRunningSeq("UTIMAPS", strYear, "USJ" + strYear.substring(2, 4) + divCode, "", true, session);
                updatingModel.setBill_ref_no(billRef);

            System.out.println("bill_ref_no" + updatingModel.getBill_ref_no());
//            LsTlActivityModel activityLog = new LsTlActivityModel();
//            activityLog.setActivity(OPERATION.UPD_BILL_REF_NO);
//            activityLog.setRemark("Bill Ref No is updated :: " + billRef);
//            activityLog.setId_table_activity(updatingModel.getPayment_id());
//            activityLog.setTable_activity("ls_payment");
//            activityLog.defaultAddProperties();
//            session.save(activityLog);
        } else if (updatingModel.get_operation().equals(OPERATION.UPD_PAY_PENDING)) { //call by RLLAction when payment rejected by ebpp & expired. @4.4.2019. Remark: To cater RLL Counter Payment.
            updatingModel.setPayment_status(PaymentModel.PAYMENT_STATUS.PENDING_PAYMENT);
            updatingModel.setPayment_date(DateUtil.getCurrentTimestamp()); //@18.7.2018 Remark: Must reset this date each time repay. PaymentCheck2 will check this date for expired.

            String divCode = updatingModel.getDiv_code();

            //added @4.5.2018. Remark: To set new bill reference no. Else PaymentBills will show duplicate Bill Reference No error.
            Calendar cal = Calendar.getInstance();
            String strYear = Integer.toString(cal.get(Calendar.YEAR));
            String billRef = RunningSeqFunction.getRunningSeq("UTIMAPS", strYear, "USJ" + strYear.substring(2, 4) + divCode, "", true, session);
                updatingModel.setBill_ref_no(billRef); //must do here. Not at action.
                
        } else if (updatingModel.get_operation().equals(OPERATION.UPD_PAY_BYPREPAYMENT)) { //when payment made by prepayment account
            System.out.println("UPD_PAY_BYPREPAYMENT");
            updatingModel.setPayment_status(PaymentModel.PAYMENT_STATUS.PAYMENT_COMPLETED);
            updatingModel.setPayment_date(DateUtil.getCurrentTimestamp()); //Remark: Solved empty date, cannot post2rvs issue. @20.8.2019
            updatingModel.setPayment_status_date(DateUtil.getCurrentTimestamp()); //Remark: To cater treasurvy recon. Status_date used to post to rvs. @8.7.2020.
            updatingModel.setPayment_method(SystemConstants.PAYMENT_METHOD.JVP); //must set to JVP

            System.out.println("payment status >>>>>>>> " + updatingModel.getPayment_status());
            ApplicationPModel casePbModel = (ApplicationPModel) dao.getModelByCode("case_id", updatingModel.getCase_id(), new ApplicationPModel());
            casePbModel.setApp_status(UtimapsAction.PB_STATUS.APP_PAYMENT_COMPLETED);
            casePbModel.setWf_status(UtimapsAction.WF_STATUS.APPLICATION_PAYMENT_COMPLETED);
            casePbModel.defaultUpdateProperties();
            session.update(casePbModel);
        }
    }
}
