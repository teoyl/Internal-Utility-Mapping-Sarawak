/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend;

import com.sains.common.util.DateUtil;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.LogFunction;
import com.sains.framework.base.SessionFactoryImpl;
import com.utimaps.model.PaymentItemModel;
import com.utimaps.model.PaymentModel;
import com.utimaps.web.UtimapsAction;

/**
 *
 * @author joveni.h
 */
public class CounterPaymentEngine extends LRBase {
    CommonFunction cf = new CommonFunction();
    public int updatePayment(String div, java.sql.Timestamp payDate, String rvsReceiptNo, String payMethod, String payAmount, String lsFeeId, String paymentId, boolean isLast) throws Exception{
        String strStage = "";
        boolean commitTrans = false;
        
        try {
            strStage = "Update US_PAYMENT_ITEM";
            update_USPaymentItem(rvsReceiptNo, lsFeeId, paymentId);
            
            if (isLast) {
                System.out.println("Update US_PAYMENT :: " +paymentId);
                strStage = "Update US_PAYMENT";
                update_USPayment(payDate, payMethod, paymentId);
            
                PaymentModel pay = (PaymentModel) retrieverDAO.getObjectById(paymentId, PaymentModel.class);

                System.out.println("Update MAIN TABLE :: " +paymentId);
                strStage = "Update US_APPLICATION_P";
                update_USApplication(pay.getCase_id());
//                
            }    
            
            commitTrans = true;
            
            if (commitTrans) {
                conn.commit();
            }
        
        
         } catch (Exception ex) {
            if (conn != null) {
                conn.rollback();
            }
            cf.writeFile("CounterPaymentCheck", "[ERROR] CounterPaymentEngine >>> Stage: " + strStage + ", Failed update counter payment status." + ex.getMessage());
            new LogFunction().logError(this.getClass(), "Stage: " + strStage + ", Failed update counter payment status.", ex);
            throw ex;
        } finally {
            
            closeAll();
            closeAll_new();

            if (conn != null) {
                SessionFactoryImpl.closeConnection(conn);
            }
            conn = null;

            if (conn_new != null) {
                SessionFactoryImpl.closeConnection(conn_new);
            }
            conn_new = null;
        }
        return 0;
    }
    
    private void update_USPaymentItem(String rvsReceiptNo, String voucherNo, String paymentId) throws Exception{
//        System.out.println("update_USPaymentItem > voucherNo :: " + voucherNo+ ";;" +paymentId);
        cf.writeFile("CounterPaymentCheck", "CounterPaymentEngine >>> update_USPaymentItem voucherNo ::" + voucherNo + " :: " +paymentId);
        new LogFunction().logInfo(this.getClass(), "CounterPaymentEngine....update_USPaymentItem irefno:: " +voucherNo);
        sqlStmt_.setLength(0);
        param_.clear();
     
        sqlStmt_.append("UPDATE US_PAYMENT_ITEM SET ");

        sqlStmt_.append(" RVS_RECEIPTNO = ?, ");
        param_.add(rvsReceiptNo);
        
        sqlStmt_.append(" RVS_POSTED = ?, ");
        param_.add(PaymentItemModel.RVS_POSTED.NOT_FOR_POSTING_TO_RVS);
        
        sqlStmt_.append(" UPDATED_BY = ?, ");
        param_.add(SystemConstants.COUNTER_PAYMENT_CHECK.DEFAULT_USER);
        
        sqlStmt_.append(" UPDATED_DATE = ? ");
        param_.add(DateUtil.getCurrentTimestamp());

        sqlStmt_.append(" WHERE PAYMENT_ID = ? ");
        param_.add(paymentId);
        
        sqlStmt_.append(" AND ITEM_REF_NO = ? ");
        param_.add(voucherNo);

        sqlUpdate(param_, sqlStmt_.toString());
    }
    
    private void update_USPayment(java.sql.Timestamp payDate, String payMethod, String paymentId) throws Exception{
        cf.writeFile("CounterPaymentCheck", "CounterPaymentEngine >>> update_USPayment " + paymentId );
        new LogFunction().logInfo(this.getClass(), "CounterPaymentEngine....update_usPayment");
        sqlStmt_.setLength(0);
        param_.clear();

        sqlStmt_.append("UPDATE US_PAYMENT SET ");

        sqlStmt_.append(" PAYMENT_METHOD = ?, ");
        param_.add(payMethod);

        sqlStmt_.append(" PAYMENT_DATE = ?, ");
        param_.add(payDate);

        sqlStmt_.append(" PAYMENT_STATUS = ?, ");
        param_.add(PaymentModel.PAYMENT_STATUS.PAYMENT_COMPLETED);

        sqlStmt_.append(" PLATFORM = ?, ");
        param_.add(PaymentModel.PLATFORM.COUNTER);
        
        sqlStmt_.append(" PAYMENT_REMARKS = ?, ");
        param_.add("Counter Payment");
        
        sqlStmt_.append(" UPDATED_BY = ?, ");
        param_.add(SystemConstants.COUNTER_PAYMENT_CHECK.DEFAULT_USER);

        sqlStmt_.append(" UPDATED_DATE = ? ");
        param_.add(DateUtil.getCurrentTimestamp());

        sqlStmt_.append(" WHERE PAYMENT_ID = ? ");
        param_.add(paymentId);

        sqlUpdate(param_, sqlStmt_.toString());
    }
    
    private void update_USApplication(String caseId) throws Exception{
        cf.writeFile("CounterPaymentCheck", "CounterPaymentEngine >>> update_usapplication " + caseId );
        new LogFunction().logInfo(this.getClass(), "CounterPaymentEngine....US_APPLICATION_P: " + caseId);
        sqlStmt_.setLength(0);
        param_.clear();

        sqlStmt_.append("UPDATE US_APPLICATION_P SET ");

        sqlStmt_.append(" APP_STATUS = ?, ");
//        param_.add(SystemConstants.PodStatus_Type.PAYMENT_COMPLETED); //PC
        param_.add(UtimapsAction.PB_STATUS.APP_PAYMENT_COMPLETED); //015
        
        sqlStmt_.append(" WF_STATUS = ?, ");
//        param_.add(SystemConstants.PodStatus_Type.PAYMENT_COMPLETED); //PC
        param_.add(UtimapsAction.WF_STATUS.APPLICATION_PAYMENT_COMPLETED); //015

        sqlStmt_.append(" UPDATED_BY = ?, ");
        param_.add(SystemConstants.COUNTER_PAYMENT_CHECK.DEFAULT_USER);

        sqlStmt_.append(" UPDATED_DATE = ? ");
        param_.add(DateUtil.getCurrentTimestamp());
        
//        sqlStmt_.append(" STATUS_REMARKS = ? ");
//        param_.add("Counter Payment");

        sqlStmt_.append(" WHERE CASE_ID = ? ");
        param_.add(caseId);

        sqlUpdate(param_, sqlStmt_.toString());
    }
    
}

