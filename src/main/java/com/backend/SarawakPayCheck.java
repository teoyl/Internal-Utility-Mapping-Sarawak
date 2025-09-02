/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend;

import com.alibaba.fastjson.JSON;
import com.rrtx.security.domain.PostParams;
import com.rrtx.security.domain.SecurityParams;
import com.rrtx.security.service.ISecurity;
import com.rrtx.security.service.impl.SecurityImpl;
import com.sains.common.util.DateUtil;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.LogFunction;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.PaymentModel;
import com.utimaps.web.UtimapsAction;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author user
 */
public class SarawakPayCheck extends TimerJob {
    private BaseDAO retrieverDAO = new BaseDAOImpl();
    
    CommonFunction cf = new CommonFunction();
    
    private String orderInquiryResult ="";
    public String getOrderInquiryResult() {
        return orderInquiryResult;
    }

    public void setOrderInquiryResult(String orderInquiryResult) {
        this.orderInquiryResult = orderInquiryResult;
    }

    public SarawakPayCheck() {
        super();
        setJobSwitch_on(Boolean.FALSE);
        alwaysTriggerOnce = Boolean.FALSE;
        jobRepeat = REPEAT.Default;
        jobRunEvery = "5"; //HH:MM (Day of the month in 2 digits)
    }
    
    @Override
    public void run() {
        try {
            String strURL = getInquiryURL();
            System.out.println("*******SarawakPayCheck*****************");
            String strUSJ_SQL = "select * from us_payment "
                    + "where payment_method = '" + SystemConstants.PAYMENT_OPTION.SARAWAK_PAY + "' and "
                    + "payment_status = '"+ PaymentModel.PAYMENT_STATUS.PAYMENT_IN_PROGRESS +"' and "
                    + "to_char(payment_status_date,'yyyy-mm-dd') >= '2020-10-24' and "
                    + "payment_status_date <= (sysdate - interval '15' minute) "
                    + "order by payment_status_date desc ";
            
            List paymentListUSJ = (new CommonFunction().getListFromSqlWithSession(retrieverDAO.getSession(), strUSJ_SQL, null));
            
            System.out.println("SarawakPayCheck >> RLL - paymentList size: " + paymentListUSJ.size());
	    new LogFunction().logInfo(this.getClass(),"SarawakPayCheck >> RLL - paymentList size: " + paymentListUSJ.size());
            if (paymentListUSJ.size() > 0 && !Validator.isEmpty(strURL)) {
                String merOrderNo;
                for (Map map : (List<Map>) paymentListUSJ) { //TOL payment list
                    merOrderNo = (String) map.get("BILL_REF_NO");
                    this.callInquiry(strURL, merOrderNo, SystemConstants.PAYMENT_CATEGORY.UTIMAPS);
                }
            }
            
        } catch (Exception e) {
            System.out.println("*** SarawakPayCheck running in exception. *** \n" + e);
            new LogFunction().logError(this.getClass(), "*** SarawakPayCheck running in exception. *** \n", e);
        } finally {
            retrieverDAO.closeSession();
        }
    }
    
    private String getInquiryURL() {
        String strURL = SystemConstants.DOMAIN.sarawakPayQueryOrderQRUrl;

        return strURL;
    }
    
    private void callInquiry(String strURL, String merOrderNo, String paymentCategory) {
        try {
//            if (strURL.indexOf("spayolp") > 0) {
//                sendPost2(retrieverDAO, strURL, merOrderNo, paymentCategory);
//            } else {
                // Message
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("merchantId", SystemConstants.SPAY_MERCHANT_ID.ID);
                map.put("merOrderNo", merOrderNo);

                // Formatting message to Json
                String jsonString = JSON.toJSONString(map);
                System.out.println("Calling post > jsonString : " + jsonString);
		new LogFunction().logInfo(this.getClass(),"Calling post > jsonString : " + jsonString);
                sendPost(retrieverDAO, jsonString, strURL, paymentCategory);
//            }
        } catch (Exception e) {
            System.out.println("*** Failed to inquiry for " + merOrderNo + ". ***\n" + e);
	    new LogFunction().logError(this.getClass(),"*** Failed to inquiry for " + merOrderNo + ". ***\n" , e);
        }
    }
    
    public void sendPost(BaseDAO retrieverDAO, String jsonString, String inquiryURL, String paymentCategory) throws Exception {
        ISecurity security;
        try {
            security = spayCertInitialise();
        } catch (Exception e) {
	    new LogFunction().logError(this.getClass(),"*** sendPost():: Certificate Initialisation Failed. *** " , e);
            throw new Exception("*** sendPost():: Certificate Initialisation Failed. *** " + e);
        }

        try {
            // Sending message to SP ServerName
            PostParams postParams = new PostParams();
            postParams.setRequestData(jsonString);

            orderInquiryResult = security.post(inquiryURL, postParams);

            if (Validator.isEmpty(orderInquiryResult)) {
		new LogFunction().logInfo(this.getClass(),"*** sendPost():: Order inquiry result is EMPTY. ***");
                throw new Exception("*** sendPost():: Order inquiry result is EMPTY. ***");
            } else {
		new LogFunction().logInfo(this.getClass(),"sendPost():: Order inquiry result: " + orderInquiryResult);
                System.out.println("sendPost():: Order inquiry result: " + orderInquiryResult);
            }
        } catch (Exception e) {
	    new LogFunction().logError(this.getClass(),"*** sendPost():: security.post failed. ***", e);
            throw new Exception("*** sendPost():: security.post failed. ***" + e.getMessage());
        }

        try {
            decrypt(retrieverDAO, security, paymentCategory);
        } catch (Exception e) {
	    new LogFunction().logError(this.getClass(),"*** sendPost():: decrypt failed. *** \n" ,e);
            throw new Exception("*** sendPost():: decrypt failed. *** \n" + e.getMessage());
        }
    }
    
    private ISecurity spayCertInitialise() throws Exception {
        // Certificate Initialization
        ISecurity security = new SecurityImpl();
        SecurityParams securityParams = new SecurityParams();
        securityParams.setRsaPrivateKeyFromType(1);
        securityParams.setRsaPublicKeyFromType(1);
        securityParams.setRsaPrivateKey(new CommonFunction().readMLasisPrivateKey());
        securityParams.setRsaPublicKey(new CommonFunction().readPublicKey());
        security.init(securityParams);

        return security;
    }
    
         public void decrypt(BaseDAO retrieverDAO, ISecurity security, String paymentCategory) throws Exception {
        try {
            System.out.println("------------- Decrypted Data -------------");
	    new LogFunction().logInfo(this.getClass(),"------------- Decrypted Data -------------");
            String jsonString = security.decrypt(orderInquiryResult);
            
            System.out.println("Order inquiry decrypted data: " + jsonString);
            new LogFunction().logInfo(this.getClass(),"Order inquiry decrypted data: " + jsonString);
            if (Validator.isEmpty(jsonString)) {
		
		new LogFunction().logInfo(this.getClass(),"Order inquiry decrypt an empty output.");
                throw new Exception("Order inquiry decrypt an empty output.");
            }

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = new HashMap<String, Object>();

            // convert JSON string to Map
            map = mapper.readValue(jsonString, new TypeReference<Map<String, String>>() {
            });

            System.out.println("map : " + map);
            new LogFunction().logInfo(this.getClass(),"map : " + map);
            String billRefNo = map.get("merOrderNo").toString();
            
            
//            String appCaseId = map.get("orderId").toString();
            
            String strSqlWhere = "";
            String caseId = "";

            
            
            
            // for SarawakPay payment
            switch (paymentCategory) {
                case SystemConstants.PAYMENT_CATEGORY.UTIMAPS:
                    
                    if (map.get("ResCode") != null && map.get("ResCode").equals(SystemConstants.SPAY_RESPONSE_CODE.Order_Not_Exist)) {
                        PaymentModel paymentUSJ = checkUsjPaymentRecord(retrieverDAO, billRefNo);
                        
                        
                        if (paymentUSJ != null) {
                            strSqlWhere = "payment_id ='" + paymentUSJ.getPayment_id()+ "'";

                            caseId = paymentUSJ.getCase_id();
                            
                            updateUsjPendingPayment(retrieverDAO, paymentUSJ, caseId);
                        }
                    } else if (map.get("orderStatus") != null
                            && (map.get("orderStatus").toString().equals(SystemConstants.SPAY_ORDER_STATUS.Failed) || map.get("orderStatus").toString().equals(SystemConstants.SPAY_ORDER_STATUS.Order_Closed))) {
                        PaymentModel paymentUSJ = checkUsjPaymentRecord(retrieverDAO, billRefNo);
                        
                        if (paymentUSJ != null) {
                            
                            strSqlWhere = "payment_id ='" + paymentUSJ.getPayment_id()+ "'";

                            caseId = paymentUSJ.getCase_id();
                        
                            updateUsjPendingPayment(retrieverDAO, paymentUSJ, caseId);
                        }
                    } else if (map.get("orderNo") != null && map.get("tranDate") != null && map.get("orderStatus") != null
                            && map.get("orderStatus").toString().equals(SystemConstants.SPAY_ORDER_STATUS.Order_Paid)) {
                        
                        System.out.println("orderNo: " + map.get("orderNo").toString() + " tranDate: " + map.get("tranDate").toString()
                                + " merOrderNo: " + map.get("merOrderNo").toString() + " orderStatus: " + map.get("orderStatus").toString());
                        new LogFunction().logInfo(this.getClass(),"orderNo: " + map.get("orderNo").toString() + " tranDate: " + map.get("tranDate").toString()
                                + " merOrderNo: " + map.get("merOrderNo").toString() + " orderStatus: " + map.get("orderStatus").toString());
                        String spayRefNo = map.get("orderNo").toString();
                        PaymentModel paymentUSJ = checkUsjPaymentRecord(retrieverDAO, billRefNo);
                        if (paymentUSJ != null) {
                            
                            updateUsjPaymentCompleted(retrieverDAO, paymentUSJ, spayRefNo, map.get("tranDate").toString());
                        }
                    }  break;
            }
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }
         
    public PaymentModel checkUsjPaymentRecord(BaseDAO retrieverDAO, String billRefNo) throws Exception {
        PaymentModel payment = (PaymentModel) retrieverDAO.getObjectByCode("bill_ref_no,payment_method", billRefNo + "," + SystemConstants.PAYMENT_METHOD.SPY, new PaymentModel());
        if (payment == null) {
	    new LogFunction().logInfo(this.getClass(),"Payment record not found: " + billRefNo);
            throw new Exception("Payment record not found: " + billRefNo);
        } else {
            if (!payment.getPayment_status().equals(PaymentModel.PAYMENT_STATUS.PAYMENT_IN_PROGRESS)) {
		new LogFunction().logInfo(this.getClass(),"Invalid payment status: " + billRefNo);
                throw new Exception("Invalid payment status: " + billRefNo);
            }
        }
        return payment;
    }
    
    public void updateUsjPendingPayment(BaseDAO retrieverDAO, PaymentModel payment, String appCaseId) {
        try {
            retrieverDAO.beginBatchTransaction();
            System.out.println("updating RLL to pending payment");
	    new LogFunction().logInfo(this.getClass(),"updating RLL to pending payment");
            ApplicationPModel appModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", payment.getCase_id(), new ApplicationPModel());
                
            System.out.println("Payment_id " + payment.getPayment_id());
	    new LogFunction().logInfo(this.getClass(),"Payment_id " + payment.getPayment_id());
            payment.setPayment_status(PaymentModel.PAYMENT_STATUS.PENDING_PAYMENT);
            payment.setPayment_status_date(DateUtil.getCurrentTimestamp());
            payment.setPay_ref_no("");
//            BaseActionSupport.defaultUpdateProperties(payment, "LXGTOL");
            retrieverDAO.getSession().saveOrUpdate(payment);

            appModel.setApp_status(UtimapsAction.PB_STATUS.PENDING_APP_PAYMENT);
//            BaseActionSupport.defaultUpdateProperties(tolApp, "LXGTOL");
            retrieverDAO.getSession().saveOrUpdate(appModel);

            retrieverDAO.commitBatchTransaction();
        } catch (Exception e) {
	    new LogFunction().logError(this.getClass(),"*** Failed to update pending payment. ***",e);
            System.out.println("*** Failed to update pending payment. ***");
            retrieverDAO.rollbackBatchTransaction();
        } finally {
            //retrieverDAO.closeSession();
        }
    }
    
    private void updateUsjPaymentCompleted(BaseDAO retrieverDAO, PaymentModel payment, String spayRefNo, String tranDate) throws Exception {
        String strSqlWhere = "";
        String app_id = "";

        strSqlWhere = "payment_id ='" + payment.getPayment_id()+ "'";

        app_id = payment.getCase_id();
        
        ApplicationPModel appModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", app_id, new ApplicationPModel());
                
        try {
            retrieverDAO.beginBatchTransaction();
            
            System.out.println("spayCheck updating tolPayment to completed: " + payment.getBill_ref_no());
	    new LogFunction().logInfo(this.getClass(),"spayCheck updating tolPayment to completed: " + payment.getBill_ref_no());
            //UPDATE PAYMENT_STATUS to 359 (Completed)
            payment.setPayment_status_date(DateUtil.getTimestampFromDate(DateUtil.getDate(tranDate, "yyyyMMddhhmmss")));
            payment.setPayment_status(PaymentModel.PAYMENT_STATUS.PAYMENT_COMPLETED);
            payment.setPay_ref_no(spayRefNo); //sarawakpay ref number
//            BaseActionSupport.defaultUpdateProperties(payment, "LXGTOL");
            retrieverDAO.getSession().update(payment);
            appModel.setWf_status(UtimapsAction.WF_STATUS.APPLICATION_PAYMENT_COMPLETED);
            appModel.setApp_status(UtimapsAction.PB_STATUS.APP_PAYMENT_COMPLETED);
//            BaseActionSupport.defaultUpdateProperties(applicationModel, "LXGTOL");
            retrieverDAO.getSession().update(appModel);
            
            logAppActivity(retrieverDAO.getSession(), "paymentCompleted",
                                            "Payment using SPay success/received : " + payment.getBill_ref_no(), payment.getPayment_id(), "ls_payment", "Backend");
                            

            System.out.println("spayolp inquiry > commit trans......");
	    new LogFunction().logInfo(this.getClass(),"spayolp inquiry > commit trans......");
            retrieverDAO.commitBatchTransaction();
            
        } catch (Exception e) {
            retrieverDAO.rollbackBatchTransaction();
	    new LogFunction().logError(this.getClass(),"Update TOL payment completed failed: " , e);
            throw new Exception("Update TOL payment completed failed: " + e.getMessage());
        } finally {
            retrieverDAO.closeSession();
        }
    }
    
    public synchronized void logAppActivity( org.hibernate.Session session, String strActivity, String strRemark, String idTableActivity,String strTableActivity,String strCreatedBy) throws Exception {
        System.out.println("LOGGING EVENT: " + strActivity);
        //BaseDAO baseDAO = new BaseDAOImpl();
        //baseDAO.setSession(session);
//        retrieverDAO.setSession(session);
//        Map sessionMap = ActionContext.getContext().getSession();
        try {
//            LsTlActivityModel activityLog = new LsTlActivityModel();
////            System.out.println("---userId = " +sessionMap.get("userId").toString());
////            User user = (User) checkingDAO.getObjectById(sessionMap.get("userId").toString(), User.class);
//            
//            activityLog.setActivity(strActivity);
//            activityLog.setRemark(strRemark);
//            activityLog.setId_table_activity(idTableActivity);
//            activityLog.setTable_activity(strTableActivity);
//            activityLog.setCreated_by(strCreatedBy);
//            activityLog.setUpdated_by(strCreatedBy);
//            activityLog.defaultAddProperties();
//            
//            retrieverDAO.getSession().save(activityLog);
//            System.out.println("-------------------------------END Activity Log ----------------------------------------");
        } catch (Exception ex) {
            throw ex;
        }
        
    }
    
    
}
