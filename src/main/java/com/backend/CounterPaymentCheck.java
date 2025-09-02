/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend;

import com.sains.common.util.DateUtil;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.LogFunction;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import com.sains.framework.base.BaseDAO;

import com.sains.common.util.Validator;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ServiceFactory;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.PaymentItemModel;
import com.utimaps.model.PaymentModel;
import static java.lang.Thread.sleep;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @ 14.2.2022. Remark: To cater UTiMAPS Counter Payment. (follow eRLL)
 * @author stephnieia
 */
public class CounterPaymentCheck extends TimerJob {
    private long waitingTime = 60000; // 1 min // added @16Jan2015
    public Boolean keepChecking = true;
    private PaymentModel paymentModel = null;
    private PaymentItemModel paymentItemModel = null;
    private String created_by = "";
    private String payment_date = "";
    String payment_date_ = "";
    private Map mailParam = new HashMap();

    PreparedStatement stmt = null;
    private String backend = "USJ CounterPayment";
    CommonFunction cf = new CommonFunction();
    private Boolean debugMode = Boolean.FALSE; // default is FALSE;
    private Integer debugOpenCount = 0;
    private Integer debugCloseCount = 0;
    long sleepTime = 0;

    public CounterPaymentCheck() {
        super();
        setJobSwitch_on(Boolean.FALSE);
        alwaysTriggerOnce = Boolean.FALSE;
        jobRepeat = TimerJob.REPEAT.Default;
        jobRunEvery = "5"; //HH:MM (Day of the month in 2 digits)
    }

    @Override
    public void run() {

        if (getJobSwitch_on()) { //serene @ 4/7/2022
            BaseDAO baseDAO = new BaseDAOImpl();
            BaseDAO retrieverDAO = new BaseDAOImpl();
            retrieverDAO.setSession(baseDAO.getSession());
            try {
                Map tsParam = new HashMap();
                tsParam.put("payment_status", SystemConstants.PodStatus_Type.PENDING_PAYMENT);
                tsParam.put("platform", PaymentModel.PLATFORM.COUNTER);
                while (keepChecking) {
                    cf.writeFile("CounterPaymentCheck", "[start] CounterPaymentCheck***************************************");
                    if (debugMode) {
                        Debug.printDebug("DEBUG >>>>> Opened::" + debugOpenCount + "<<>>Closed::" + debugCloseCount);
                    }

                    try {
                        Timestamp timeStart = DateUtil.getCurrentTimestamp();
                        List<PaymentModel> paymentList = new ArrayList();
                        paymentList = retrieverDAO.list(tsParam, PaymentModel.class);

                        cf.writeFile("CounterPaymentCheck", "CounterPaymentCheck >>> 1. CP paymentList.size() :: " + paymentList.size());

                        String caseDiv = null;
                        String paymentId = null;
                        if (paymentList != null) {
                            for (PaymentModel paymentObj : paymentList) {
                                paymentModel = paymentObj;
                                created_by = paymentObj.getCreated_by();

                                if (paymentModel == null) {
                                    cf.writeFile("CounterPaymentCheck", "[ERROR] CounterPaymentCheck >>> Payment not found.");
                                    throw new Exception("Payment not found.");
                                }

                                String strSqlWhere = "";
                                String app_id = "";

                                ApplicationPModel appModel = (ApplicationPModel) baseDAO.getModelById(paymentModel.getCase_id(), ApplicationPModel.class);
                                if(appModel != null){
                                    caseDiv = appModel.getCase_div();
                                    paymentObj.setAppCaseModel(appModel);
                                }

                                Map itemParam = new HashMap();
                                itemParam.put("payment_id", paymentModel.getPayment_id());
                                cf.writeFile("CounterPaymentCheck", "CounterPaymentCheck >>> 2. CP itemParam :: " + itemParam);
                                List<PaymentItemModel> paymentItemList = new ArrayList();

                                paymentItemList = retrieverDAO.list(itemParam, PaymentItemModel.class);

                                if (paymentItemList.size() > 0) {
                                    cf.writeFile("CounterPaymentCheck", "CounterPaymentCheck >>> 2. CP paymenttItem.size() :: " + paymentItemList.size());
                                    //get detail from rvmtran records
                                    List<Map<String, Object>> lsRvmTran = null;
                                    String itemrefNo = "";
                                    for (PaymentItemModel payItem : paymentItemList) {
                                        if (Validator.isEmpty(itemrefNo)) {
                                            itemrefNo = "'" + payItem.getItem_ref_no().replace("/", "") + "'";
                                        } else {
                                            itemrefNo += ", '" + payItem.getItem_ref_no().replace("/", "") + "'";
                                        }
                                        cf.writeFile("CounterPaymentCheck", "CounterPaymentCheck >>> 2. CP itemrefNo :: " + itemrefNo);
                                    }

                                    if (itemrefNo == null) {
                                        cf.writeFile("CounterPaymentCheck", "CounterPaymentCheck >>> itemrefNo NULL ");
                                    } else {
                                        try {
                                            String rvmTranSQL = "SELECT PAY_DATE, PAY_METHOD, PAY_AMOUNT, RECEIPT_NO, DOC_NO, ECASE_REF, VOUCHER_NO "
                                                    + "FROM RVMTRAN" + caseDiv + " WHERE VOUCHER_NO IN (" + itemrefNo + ")";
                                            Query query = baseDAO.getSession().createSQLQuery(rvmTranSQL);
                                            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
                                            lsRvmTran = query.list();

                                            if (lsRvmTran.size() > 0) {
                                            } else {
                                            }

                                            if (lsRvmTran.size() == paymentItemList.size()) { //joveni @ 21/7/2021 :: new fee added - serene inform 1 tol payment has 3 payment items 
                                                int index = 0;
                                                boolean isLast = false; //if true, then update lspayment

                                                //get detail from rvmtran*****
                                                for (Map rvmTran : lsRvmTran) {
                                                    cf.writeFile("CounterPaymentCheck", "CounterPaymentCheck >>> 4. start rvmTran....");
                                                    index++;
                                                    String voucherNumber = rvmTran.get("VOUCHER_NO").toString();
                                                    java.sql.Timestamp payDate = (java.sql.Timestamp) rvmTran.get("PAY_DATE");
                                                    String rvsReceiptNo = rvmTran.get("RECEIPT_NO").toString();
                                                    String payMethod = (String) rvmTran.get("PAY_METHOD");
                                                    String paidAmt = (String) rvmTran.get("PAY_AMOUNT").toString();

                                                    if (index == lsRvmTran.size()) { //index==2
                                                        isLast = true;
                                                    }

                                                    ServiceFactory.getInstance().getCounterPaymentService().updatePayment(caseDiv, payDate, rvsReceiptNo, payMethod, paidAmt, voucherNumber, paymentModel.getID(), isLast);
                                                }// End for rvmTran
                                            }
                                        } catch (Exception e) {
                                            cf.writeFile("CounterPaymentCheck", "[ERROR] CounterPaymentCheck >>> 4. rvmtran catch:" + e);
                                        }

                                    }//if itemnull
                                }// End IF elsPaymentList>0

                            }// End FOR LOOP elsPaymentList 
                        }
                        //No sending email to notify applicant in case there is reversal in counter.    

                        Timestamp timeEnd = DateUtil.getCurrentTimestamp();
//                        try {rs.close();} catch (Exception e2){}
//                        try {stmt.close();} catch (Exception e4) {}
//                        retrieverDAO.closeSession();
//                        if (conn != null) {
//    //                        conn.close();
//    //                        SessionFactoryImpl.closeConnection(conn); //ThenSW @ 28 Aug 2013 :: This line will put the conn to connection pool. But is closed next line
//                            SessionFactoryImpl.disconnectConnection(conn); // use SFI.disconnectConnection to make the connection count accurately. //ThoTH @ 29-Aug-2013 :: This line is to solve the Opened Cursor Problem
//    //                        if (! conn.isClosed()) conn.close();  // Commented by ThenSW @ 28-Aug-2013:: conn.close cannot use together with SFI.closeConn()                 ///// ThoTH @ 5-Oct-2012 :: This line is to solve the Opened Cursor Problem
//                            conn = null;
//                            if (debugMode) debugCloseCount++;
//                        }
//                        closeAll();

                        if (waitingTime - (timeEnd.getTime() - timeStart.getTime()) > 0) {
                            sleepTime = waitingTime - (timeEnd.getTime() - timeStart.getTime());
                            try {
                                sleep(sleepTime);
                            } catch (InterruptedException e) {
                                cf.writeFile("CounterPaymentCheck", "[ERROR] CounterPaymentCheck >>> interruptError" + e.getMessage());
                                sleep(waitingTime);
                                break;
                            }
                        }
                    } catch (Exception ex) {
                        cf.writeFile("CounterPaymentCheck", "[ERROR] CounterPaymentCheck >>> Running with Exception :: " + ex.getMessage());
                        retrieverDAO.closeSession();
                        sleep(waitingTime * 3);//do nothing.
                    } finally {
                    }
                }//[end while]
            } catch (Exception e) {
                cf.writeFile("CounterPaymentCheck", "[ERROR] CounterPaymentCheck >>> Payment Check Main Try :: " + e.getMessage());
                new LogFunction().logError(this.getClass(), "Payment Check Main Try", e);
            } finally {
                try {
                    retrieverDAO.closeSession();
                } catch (Exception ex) {
                    cf.writeFile("CounterPaymentCheck", "[ERROR] CounterPaymentCheck >>> " + ex.getMessage());
                    Logger.getLogger(PaymentCheck.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
