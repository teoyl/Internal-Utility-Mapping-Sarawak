/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend;

import com.alibaba.fastjson.JSON;
import com.lxg.common.model.PublicUserModel;
import com.rrtx.security.domain.PostParams;
import com.rrtx.security.domain.SecurityParams;
import com.rrtx.security.service.ISecurity;
import com.rrtx.security.service.impl.SecurityImpl;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.SystemConstants.DOMAIN;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.EmailTrigger;
import com.sains.framework.base.LogFunction;
import com.sains.framework.base.ModelBase;
import com.sains.framework.base.ServiceFactory;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.PaymentModel;
import static java.lang.Thread.sleep;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author joveni.h
 */
public class PaymentCheck extends TimerJob {
//    private oracle.jdbc.OracleDriver od = new oracle.jdbc.OracleDriver();

    private static Connection conn = null;

    private long waitingTime = 60000; // 1 min 
    public Boolean keepChecking = true;

    //email
    private PublicUserModel publicUser = new PublicUserModel();

    private PaymentModel paymentModel = new PaymentModel();

    private String orderNo = "";
//    private Integer orderNo;
    private String created_by = "";
    private String updated_by = "";
    private Double payment_amount;
    private String refNo = "";
    private String payment_date = "";
    private String remark = "";
    private Map mailParam = new HashMap();
    private Boolean isUsesp = Boolean.FALSE;

    private String strEbppSql = "";

    private String strdivision = "";

    public String getStrdivision() {
        return strdivision;
    }

    public void setStrdivision(String strdivision) {
        this.strdivision = strdivision;
    }
    // ThoTH @ 5-Oct-2012 :: DEBUG ORACLE CURSOR
    private Boolean debugMode = Boolean.FALSE; // default is FALSE;
    private Integer debugOpenCount = 0;
    private Integer debugCloseCount = 0;

    private PreparedStatement stmt = null;
    private org.hibernate.SQLQuery query = null;
    private BaseDAO retrieverDAO = new BaseDAOImpl();
    private BaseDAO baseDAO = new BaseDAOImpl();
    
    private BaseDAO lasisDAO = new BaseDAOImpl();
    
    CommonFunction cf = new CommonFunction();

    ApplicationPModel appModel = new ApplicationPModel();
    
    private ResultSet rs = null;
    // ThoTH @ 5-Oct-2012 :: DEBUG ORACLE CURSOR - END

    public PaymentCheck() {
//        setName("PaymentCheck");
        super();
        setJobSwitch_on(Boolean.FALSE);
        alwaysTriggerOnce = Boolean.FALSE;
        jobRepeat = TimerJob.REPEAT.Default;
        jobRunEvery = "5"; //HH:MM (Day of the month in 2 digits)
    }

    @Override
    public void run() {
        
        ModelBase.updateCurrentRequestObject("antiCSRF", "ignoreCSRF");//serene @ 10/8/2022
        PaymentModel paymentModel = new PaymentModel();

        List<PaymentModel> paymentList = new ArrayList(); 
//        List<PaymentModel> paymentRVMList = new ArrayList();

        Map usjParam = new HashMap();
//        Map tolParam2 = new HashMap();

        usjParam.put("payment_status", SystemConstants.PodStatus_Type.PAYMENT_IN_PROGRESS);
//        tolParam2.put("payment_status", SystemConstants.PodStatus_Type.PENDING_PAYMENT);

        boolean ebbpFound = false;

        long sleepTime = 0;
        String status = "";
        //setEbppSql();        

        try {
            while (keepChecking) {
                cf.writeFile("PaymentCheckJob", "[start] PaymentCheckJob***************************************");
                if (debugMode) {
                    cf.writeFile("PaymentCheckJob", "DEBUG >>>>> Opened::" + debugOpenCount + "<<>>Closed::" + debugCloseCount);
                }
                try {
                    baseDAO.beginBatchTransaction();
                    Timestamp timeStart = DateUtil.getCurrentTimestamp();
                    paymentList = retrieverDAO.list(usjParam, PaymentModel.class);

                    cf.writeFile("PaymentCheckJob", "UTiMAPS PaymentCheck START....list size : " + paymentList.size() );

                    //START EIS Payment
                    if (paymentList != null) {
                        for (PaymentModel paymentObj : paymentList) {
                            paymentModel = paymentObj;
                            ebbpFound = false;
                            
                            cf.writeFile("PaymentCheckJob", "PaymentCheck >> " + paymentObj.getPayment_id() + " >> " + paymentObj.getPayment_amount().toString() + " >> " + paymentObj.getBill_ref_no());
                            String app_id = paymentObj.getCase_id(); //application

                            cf.writeFile("PaymentCheckJob", "PaymentCheck >> " + "app_id :" +app_id);

                            String strStatus = "";
                            created_by = paymentObj.getCreated_by();
                            updated_by = paymentObj.getUpdated_by();
                            refNo = paymentObj.getBill_ref_no();
                            payment_amount = paymentObj.getPayment_amount();
                            payment_date = Formatter.formatDate(paymentObj.getPayment_date());
                            String biller = "";
                            String svc = "";
                            String merchant_id = "";
                            String receiptNo = "";
                            String statusDate = "";
                            String amt_paid = String.valueOf(payment_amount);
                            String strDivision= "";
                            
                            biller = SystemConstants.DOMAIN.ebppBillerId;
                            svc = SystemConstants.DOMAIN.ebppSrvId;
                            merchant_id = SystemConstants.DOMAIN.ebppMerchantId;
                            
                            if(!Validator.isEmpty(app_id)){
                                appModel = (ApplicationPModel)baseDAO.getModelById(app_id, ApplicationPModel.class);
                                cf.writeFile("PaymentCheckJob", "Application payment :: "+appModel.getCase_id()); 
                                if (appModel != null) { 
                                    strDivision =appModel.getCase_div();
                                    updated_by = appModel.getApp_submit_by();
                                    paymentModel.setAppCaseModel(appModel);
                                }
                            } else {
                                cf.writeFile("PaymentCheckJob", "[ERROR] >>>>>>> unable to retrieve Application model. Application model is empty"); 
                                throw new Exception("Application Model is empty!");  
                            }
                            
                            //for EBPP payment
                            if (paymentObj.getPayment_method().equals("EBP")) {
                                USJService usjServ = new USJService();
                                String ret = "";
                                try {
//                                    cf.writeFile("PaymentCheckJob", "biller: " +biller);
//                                    cf.writeFile("PaymentCheckJob", "svc: " +svc);
//                                    cf.writeFile("PaymentCheckJob", "refNo: " +refNo);
//                                    cf.writeFile("PaymentCheckJob", "updated_by: " +updated_by);
//                                    cf.writeFile("PaymentCheckJob", "amt_paid: " +amt_paid);
                                    ret = usjServ.ePayNowEnquiry(biller, svc, refNo, updated_by, amt_paid, merchant_id); //fot tol used updated_by
                                    cf.writeFile("PaymentCheckJob", "ret: " +ret);
                                } finally {
                                    usjServ.closeSession();
                                }

                                int cont = 0;
                                for (String string_result : ret.split(",")) {
                                    if (cont == 0) {
                                        strStatus = string_result;
                                    } else if (cont == 1) {
                                        receiptNo = string_result;
                                        cf.writeFile("PaymentCheckJob", "receiptNo "+receiptNo);
                                    } else {
                                        statusDate = string_result;
                                    }
                                    cont++;
                                }
                              
                                ebbpFound = true;

                                SimpleDateFormat sdfOri = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"); 
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
                                cf.writeFile("PaymentCheckJob", "strStatus: " +strStatus);

                                if (!Validator.isEmpty(strStatus)) {
                                    if (strStatus.equals("APPROVED")) {
                                        status = strStatus;
//                                        cf.writeFile("PaymentCheckJob", "approved status found... doing payment receipt here...");
                                        try {
//                                            cf.writeFile("PaymentCheckJob", "get statuDate from ebpp in paymentCheck == " + statusDate);
                                            Timestamp convertStatusDateOri = DateUtil.getTimestampFromDate(sdfOri.parse((statusDate)));
                                            String convertStatusDateFormat = sdf.format(convertStatusDateOri);
                                            Timestamp convertStatusDate = DateUtil.getTimestampFromDate(sdf.parse((convertStatusDateFormat)));
//                                            cf.writeFile("PaymentCheckJob", "get converted statuDate from ebpp in paymentCheck == " + convertStatusDate);
                                            paymentModel.setPayment_status_date(convertStatusDate);
                                            paymentModel.setPay_ref_no(receiptNo);
                                            paymentModel.set_operation(PaymentModel.OPERATION.UPD_PAY_COMPLETED);
                                            ServiceFactory.getInstance().getPaymentService().update(paymentModel);
                                            
                                            logAppActivity(baseDAO.getSession(), "paymentCompleted",
                                            "Payment using EBPP success/approved : " + paymentObj.getBill_ref_no(), paymentObj.getPayment_id(), "us_payment", "Backend");
                                            cf.writeFile("PaymentCheckJob", "Payment using EBPP success/approved : " + paymentObj.getBill_ref_no()); 
                                            
                                            usjPaymentEmail(status, paymentModel);

                                        } catch (Exception e) {
                                            new LogFunction().logError(this.getClass(), "", e);
                                        } finally {

                                        }

                                    } else if (strStatus.equals("REJECTED")) {
                                        cf.writeFile("PaymentCheckJob", "---Payment Rejected :: payment id = " + paymentObj.getPayment_id());
                                        status = strStatus;

                                        try {

                                            paymentModel.set_operation(PaymentModel.OPERATION.UPD_PAY_REJECTED);
                                            ServiceFactory.getInstance().getPaymentService().update(paymentModel);
                                            
                                            logAppActivity(baseDAO.getSession(), "paymentRejected",
                                            "Payment using EBPP rejected : " + paymentModel.getBill_ref_no(), paymentModel.getPayment_id(), "us_payment", "Backend");
                                            cf.writeFile("PaymentCheckJob", "Payment using EBPP rejected : " + paymentModel.getBill_ref_no()); 
                                            
                                            usjPaymentEmail(status, paymentModel);
                                        } catch (Exception e) {
                                            new LogFunction().logError(this.getClass(), "", e);
                                        } finally {
                                            //                                        pDAO.closeSession();
                                        }
                                    } else if (strStatus.equals("CANCELLED") || strStatus.equals("NOT_FOUND")) { //added @27Oct2014
                                        cf.writeFile("PaymentCheckJob", "Payment using EBPP not found / cancelled : " + paymentModel.getBill_ref_no()); 
                                        ebbpFound = false;
//                                         break;
                                    } else if (strStatus.equals("PENDING")) { //added @27Oct2014
                                        cf.writeFile("PaymentCheckJob", "Payment using EBPP pending : " + paymentModel.getBill_ref_no());
                                        break;
                                    }
                                } else {
                                    cf.writeFile("PaymentCheckJob", "No proper result fetched from EBPP " + paymentModel.getBill_ref_no());
//                                    cf.writeFile("PaymentCheckJob", "no proper result fetched from ebpp");
                                }

                                cf.writeFile("PaymentCheckJob", "ebbpFound " + ebbpFound);
                                
                                if (!ebbpFound) {
                                    status = "EXPIRED";
//                                cf.writeFile("PaymentCheckJob", "EBPP Record not found for BillRefNo = " + paymentObj.getBill_ref_no());
                                    long paymentTime = paymentObj.getPayment_date().getTime();
                                    long currentTime = DateUtil.getCurrentTimestamp().getTime();
                                    if ((currentTime - paymentTime) / 3600000 >= 2) { // set payment status to Expired if no ebbp record found after 2 hours.
//                                    PaymentDAOImpl pDAO = new PaymentDAOImpl();
                                        try {
                                            cf.writeFile("PaymentCheckJob", "SET EXPIRED");
                                            paymentObj.setPayment_status(SystemConstants.PodStatus_Type.EXPIRED);
                                            paymentObj.set_operation(PaymentModel.OPERATION.UPD_PAY_EXPIRED);
                                            ServiceFactory.getInstance().getPaymentService().update(paymentObj);
                                            
                                            logAppActivity(baseDAO.getSession(), "paymentExpired",
                                            "Payment using EBPP expired : " + paymentObj.getBill_ref_no(), paymentObj.getPayment_id(), "us_payment", "Backend");
//                                        paymentDAO.processPaymentRejected_or_expired(paymentObj);
                                            
                                            cf.writeFile("PaymentCheckJob", "Payment using EBPP expired : " + paymentModel.getBill_ref_no());
                                            
                                            usjPaymentEmail(status, paymentModel);//
                                        } catch (Exception e) {
                                            new LogFunction().logError(this.getClass(), "", e);
                                        } finally {
//                                        pDAO.closeSession();
                                        }
                                    }
                                }
                            } else if (paymentObj.getPayment_method().equals("SPY")) {
                                String strURL = SystemConstants.DOMAIN.sarawakPayQueryOrderQRUrl;
                                callInquiry(strURL, paymentObj.getBill_ref_no());
                            }

                        } // end of paymentList Loop
                    }

                    Timestamp timeEnd = DateUtil.getCurrentTimestamp();

                    baseDAO.commitBatchTransaction();
                    retrieverDAO.closeSession();

                    if (waitingTime - (timeEnd.getTime() - timeStart.getTime()) > 0) {
                        sleepTime = waitingTime - (timeEnd.getTime() - timeStart.getTime());
                        try {
//                            cf.writeFile("PaymentCheckJob", "PaymentCheck: TimeStart = " + timeStart);
//                            cf.writeFile("PaymentCheckJob", "PaymentCheck: TimeEnd   = " + timeEnd);
                            sleep(sleepTime);
                        } catch (InterruptedException e) {
                            CommonFunction.writeFile("interruptError", e.getMessage());
                            sleep(waitingTime);
                            break;
                        }
                    }
                } catch (Exception ex) {
                    baseDAO.rollbackBatchTransaction(); 
                    

                    new LogFunction().logError(this.getClass(), "PC Running with Exception", ex);

                    sleep(waitingTime * 3);//do nothing.

                } finally {
                    retrieverDAO.closeSession();
                    baseDAO.closeSession(); //joveni @ 21/2/2024 :: resolve session not close
                }
            }
        } catch (Exception e) {
            cf.writeFile("PaymentCheckJob", "Throw Exception : " + e);
            new LogFunction().logError(this.getClass(), "Payment Check Main Try", e);
        } finally {
            try {

                retrieverDAO.closeSession();
                baseDAO.closeSession(); //joveni @ 21/2/2024 :: resolve session not close
            } catch (Exception ex) {
                Logger.getLogger(PaymentCheck.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }
    
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(Double payment_amount) {
        this.payment_amount = payment_amount;
    }

    public void usjPaymentEmail(String status, PaymentModel paymentModel) {
        AutoEmail autoEmail = new AutoEmail();
        AutoEmail autoEmail_rejected = new AutoEmail();
        AutoEmail emailType = null;
        String smsType = "";
        String preferContact = "";
        AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
//        BaseDAO baseDAO = new BaseDAOImpl();
        autoEmailDAO.setSession(retrieverDAO.getSession());
//        autoEmailDAO.setSession(baseDAO.getSession());
        ApplicationPModel appModel = null;
        String strAmount = "0.00";

        try {
            publicUser = (PublicUserModel) retrieverDAO.getObjectByCode("us_user_id", updated_by, new PublicUserModel());
            
            if (publicUser != null) {
                preferContact = publicUser.getUs_preferred_contact();
            }

            if (!Validator.isEmpty(status)) {
                if (status.equalsIgnoreCase("Approved")) {
                    smsType = "SmsELPaymentReceived";
                    autoEmail= autoEmailDAO.getAutoEmailByCode("TolPaymentReceipt", autoEmail);
//                    autoEmail= autoEmailDAO.getAutoEmailByCode("PaymentReceipt", autoEmail);
                    emailType = autoEmail;
                    if (autoEmail != null) cf.writeFile("PaymentCheckJob", "auto email not null"); else cf.writeFile("PaymentCheckJob", " auto email nulll");
                } else if (status.equalsIgnoreCase("Rejected")) {
                    smsType = "SmsELPaymentRejected";
                    autoEmail_rejected = autoEmailDAO.getAutoEmailByCode("TolPaymentRejected", autoEmail_rejected);
                    emailType = autoEmail_rejected;
                } else {
                    smsType = "";
                    autoEmail = autoEmailDAO.getAutoEmailByCode("ELPaymentCancelled", autoEmail);
                    emailType= autoEmail;
                }
            }

            // Email
            //cf.writeFile("PaymentCheckJob", "prefer contact " +preferContact);
            if (!Validator.isEmpty(preferContact) && preferContact.equals("M") || preferContact.equals("B")) {
                // Commented @ 13-10-2010 as Changing the Template will required to Stop and Start the Backend Service
                appModel = (ApplicationPModel) retrieverDAO.getModelByCode("payment_id",paymentModel.getPayment_id(), ApplicationPModel.class);
                mailParam.clear();
                mailParam.put(EmailTrigger.MAIL_TO, publicUser.getUs_email());
//                mailParam.put("paymentAmount", payment_amount);
                strAmount = Formatter.formatCurrency(payment_amount);//added by ahmadni 8/10/2012
                mailParam.put("paymentAmount", strAmount);
                mailParam.put("billRefNo", refNo);
//                mailParam.put("orderNo", orderNo);
                mailParam.put("paymentDate", payment_date);
                mailParam.put("userName", publicUser.getUs_user_name());
                mailParam.put("paymentAccount", "Direct Debit"); //added by ahmadni - 6/10/2011
//                mailParam.put("digitalReference", appModel.getDigital_reference_display());
                if (paymentModel.getPayment_remarks().contains("Submission")) {
                    mailParam.put("paymentType", "Submission");
                } else {
                    mailParam.put("paymentType", "Approval");
                }
//                mailParam.put("digitalReference",appModel.getDigital_reference());
//                mailParam.put("appCategory", appModel.getApp_categoryModel().getCode_desc() +": "+appModel.getApp_professionModel().getCode_desc());
//                new EmailTrigger().sendEMail(mailParam, emailType); // original code commented @5.12.201
                cf.writeFile("PaymentCheckJob", "---payment check send email" + mailParam);
                new EmailTrigger().sendEMail(mailParam, emailType);
            }
            //SMS
//            if (!Validator.isEmpty(smsType)) {
//                if (preferContact.equals("S") || preferContact.equals("B")) {
//                    spaSendSMS(smsType, publicUser, paymentModel);
//                }
//            }

        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
//            autoEmailDAO = null;
        }
    }

    public void sendPost(BaseDAO retrieverDAO, String jsonString, String inquiryURL) throws Exception {
        ISecurity security;
        try {
            security = spayCertInitialise();
        } catch (Exception e) {
            cf.writeFile("PaymentCheckJob", "[ERROR] *** sendPost():: Certificate Initialisation Failed. ***"); 
	    new LogFunction().logError(this.getClass(),"*** sendPost():: Certificate Initialisation Failed. *** " , e);
            throw new Exception("*** sendPost():: Certificate Initialisation Failed. *** " + e);
        }

        try {
            // Sending message to SP ServerName
            PostParams postParams = new PostParams();;
            postParams.setRequestData(jsonString);

            orderInquiryResult = security.post(inquiryURL, postParams);

            if (Validator.isEmpty(orderInquiryResult)) {
                cf.writeFile("PaymentCheckJob", "[ERROR] *** sendPost():: Order inquiry result is EMPTY. ***"); 
		new LogFunction().logInfo(this.getClass(),"*** sendPost():: Order inquiry result is EMPTY. ***");
                throw new Exception("*** sendPost():: Order inquiry result is EMPTY. ***");
            } else {
                cf.writeFile("PaymentCheckJob", "[ERROR] *** sendPost():: Order inquiry result: " + orderInquiryResult+"  ****"); 
		new LogFunction().logInfo(this.getClass(),"sendPost():: Order inquiry result: " + orderInquiryResult);
                cf.writeFile("PaymentCheckJob", "sendPost():: Order inquiry result: " + orderInquiryResult);
            }
        } catch (Exception e) {
            cf.writeFile("PaymentCheckJob", "[ERROR] *** sendPost():: security.post failed. ***"); 
	    new LogFunction().logError(this.getClass(),"*** sendPost():: security.post failed. ***", e);
            throw new Exception("*** sendPost():: security.post failed. ***" + e.getMessage());
        }

        try {
            decrypt(retrieverDAO, security);
        } catch (Exception e) {
	    new LogFunction().logError(this.getClass(),"*** sendPost():: decrypt failed. *** \n" ,e);
            throw new Exception("*** sendPost():: decrypt failed. *** \n" + e.getMessage());
        }
    }
    
    public void decrypt(BaseDAO retrieverDAO, ISecurity security) throws Exception {
        try {
//            cf.writeFile("PaymentCheckJob", "------------- Decrypted Data -------------");
	    new LogFunction().logInfo(this.getClass(),"------------- Decrypted Data -------------");
//            String jsonString = security.decrypt(orderInquiryResult);
            String jsonString = orderInquiryResult;
            
//            cf.writeFile("PaymentCheckJob", "Order inquiry decrypted data: " + jsonString);
            new LogFunction().logInfo(this.getClass(),"Order inquiry decrypted data: " + jsonString);
            if (Validator.isEmpty(jsonString)) {
		cf.writeFile("PaymentCheckJob", "SPAY >>>> Order inquiry decrypt an empty output."); 
		new LogFunction().logInfo(this.getClass(),"Order inquiry decrypt an empty output.");
                throw new Exception("Order inquiry decrypt an empty output.");
            }

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = new HashMap<String, Object>();

            // convert JSON string to Map
            map = mapper.readValue(jsonString, new TypeReference<Map<String, String>>() {
            });

//            cf.writeFile("PaymentCheckJob", "map : " + map);
            new LogFunction().logInfo(this.getClass(),"map : " + map);
            String billRefNo = map.get("merOrderNo").toString();
            
            PaymentModel payment = checkPaymentRecord(retrieverDAO, billRefNo);
            if (!Validator.isEmpty(appModel.getCase_id())) {
                payment.setAppCaseModel(appModel);
            } 
            // for Cart SarawakPay payment
                    //Remark: Not enough money in acc.
                    if (map.get("ResCode") != null && map.get("ResCode").equals(SystemConstants.SPAY_RESPONSE_CODE.Order_Not_Exist)) {
                        
                        if (payment != null) {
                            String status = "Order Not Exist";
                            payment.setPayment_status(SystemConstants.PodStatus_Type.EXPIRED);
                            payment.set_operation(PaymentModel.OPERATION.UPD_PAY_EXPIRED);
                            ServiceFactory.getInstance().getPaymentService().update(payment);
                            
                            logAppActivity(baseDAO.getSession(), "paymentExpired",
                                            "Payment using SPay expired : " + payment.getBill_ref_no(), payment.getPayment_id(), "us_payment", "Backend");
                            cf.writeFile("PaymentCheckJob", "Payment using SPay expired : " + payment.getBill_ref_no()); 
//                            usjPaymentEmail(status, payment);
                        }
                    } else if (map.get("orderStatus") != null
                            && (map.get("orderStatus").toString().equals(SystemConstants.SPAY_ORDER_STATUS.Failed) || map.get("orderStatus").toString().equals(SystemConstants.SPAY_ORDER_STATUS.Order_Closed))) {
                        if (payment != null) {
                            String status = "";
                            if (map.get("orderStatus").toString().equals(SystemConstants.SPAY_ORDER_STATUS.Failed)) {
                                status = "Failed";
                            } else if (map.get("orderStatus").toString().equals(SystemConstants.SPAY_ORDER_STATUS.Order_Closed)) {
                                status = "Order Closed";
                            }
                            payment.setPayment_status(SystemConstants.PodStatus_Type.EXPIRED);
                            payment.set_operation(PaymentModel.OPERATION.UPD_PAY_EXPIRED);
                            ServiceFactory.getInstance().getPaymentService().update(payment);
                            
                            logAppActivity(baseDAO.getSession(), "paymentExpired",
                                            "Payment using SPay expired : " + payment.getBill_ref_no(), payment.getPayment_id(), "us_payment", "Backend");
                            cf.writeFile("PaymentCheckJob", "Payment using SPay expired : " + payment.getBill_ref_no()); 
//                            usjPaymentEmail(status, payment);
                        }
                    } else if (map.get("orderNo") != null && map.get("tranDate") != null && map.get("orderStatus") != null
                            && map.get("orderStatus").toString().equals(SystemConstants.SPAY_ORDER_STATUS.Order_Paid)) {

                        cf.writeFile("PaymentCheckJob", "orderNo: " + map.get("orderNo").toString() + " tranDate: " + map.get("tranDate").toString()
                                + " merOrderNo: " + map.get("merOrderNo").toString() + " orderStatus: " + map.get("orderStatus").toString());
                        new LogFunction().logInfo(this.getClass(), "orderNo: " + map.get("orderNo").toString() + " tranDate: " + map.get("tranDate").toString()
                                + " merOrderNo: " + map.get("merOrderNo").toString() + " orderStatus: " + map.get("orderStatus").toString());
                        String spayRefNo = map.get("orderNo").toString();
                        if (payment != null) {
                            String status = "Approved";
                            String tranDate = map.get("tranDate") != null ? map.get("tranDate").toString() : "";
                            
                            String postRet="0";

                            updatePaymentCompleted(retrieverDAO, payment, spayRefNo, tranDate, postRet);
                            
                            logAppActivity(baseDAO.getSession(), "paymentCompleted",
                                            "Payment using SPay success/received : " + payment.getBill_ref_no(), payment.getPayment_id(), "us_payment", "Backend");
                            cf.writeFile("PaymentCheckJob", "Payment using SPay success/received : " + payment.getBill_ref_no()); 
                            usjPaymentEmail(status, payment);
                        }
                    } 
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    public PaymentModel checkPaymentRecord(BaseDAO retrieverDAO, String billRefNo) throws Exception {
        PaymentModel payment = (PaymentModel) retrieverDAO.getObjectByCode("bill_ref_no,payment_method", billRefNo + "," + SystemConstants.PAYMENT_METHOD.SPY, new PaymentModel());
        if (payment == null) {
            new LogFunction().logInfo(this.getClass(), "Payment record not found: " + billRefNo);
            cf.writeFile("PaymentCheckJob", "Payment record not found: " + billRefNo); 
            throw new Exception("Payment record not found: " + billRefNo);
            
        } else {
            if (!payment.getPayment_status().equals(SystemConstants.PodStatus_Type.PAYMENT_IN_PROGRESS)) {
                cf.writeFile("PaymentCheckJob", "Invalid payment status: " + billRefNo); 
                new LogFunction().logInfo(this.getClass(), "Invalid payment status: " + billRefNo);
                throw new Exception("Invalid payment status: " + billRefNo);
            }
        }
        return payment;
    }

    public void updatePaymentCompleted(BaseDAO retrieverDAO, PaymentModel payment, String spayRefNo, String tranDate, String postRet) throws Exception {
        try {
            retrieverDAO.beginBatchTransaction();
            //update payment status completed
            payment.setPayment_status_date(DateUtil.getTimestampFromDate(DateUtil.getDate(tranDate, "yyyyMMddhhmmss")));
            payment.setPayment_status(SystemConstants.PodStatus_Type.PAYMENT_COMPLETED);
               
            if (!Validator.isEmpty(appModel.getCase_id())) {
                payment.setAppCaseModel(appModel);
            } 
            
            payment.setPay_ref_no(spayRefNo);
            payment.setUpdated_by("BACKEND");
            payment.setUpdated_date(DateUtil.getCurrentTimestamp());
            payment.set_operation(PaymentModel.OPERATION.UPD_PAY_COMPLETED);
            ServiceFactory.getInstance().getPaymentService().update(payment);

            cf.writeFile("PaymentCheckJob", "spayolp inquiry > commit trans......");
            cf.writeFile("PaymentCheckJob", "spayolp inquiry > commit trans......"); 
            new LogFunction().logInfo(this.getClass(), "spayolp inquiry > commit trans......");
            retrieverDAO.commitBatchTransaction();

        } catch (Exception e) {
            retrieverDAO.rollbackBatchTransaction();
            cf.writeFile("PaymentCheckJob", "Update payment completed failed: "+ e); 
            new LogFunction().logError(this.getClass(), "Update payment completed failed: ", e);
            throw new Exception("Update payment completed failed: " + e.getMessage());
        }
    }

    public void updatePendingPayment(BaseDAO retrieverDAO, PaymentModel payment) {
        try {
            retrieverDAO.beginBatchTransaction();

            payment.setPayment_status(SystemConstants.PodStatus_Type.EXPIRED);
            payment.set_operation(PaymentModel.OPERATION.UPD_PAY_EXPIRED);
            ServiceFactory.getInstance().getPaymentService().update(payment);
            retrieverDAO.commitBatchTransaction();
        } catch (Exception e) {
            cf.writeFile("PaymentCheckJob", "Failed to update pending payment : "+ e); 
            new LogFunction().logError(this.getClass(), "*** Failed to update pending payment. ***", e);
            cf.writeFile("PaymentCheckJob", "*** Failed to update pending payment. ***");
            retrieverDAO.rollbackBatchTransaction();
        } finally {
            retrieverDAO.closeSession(); //joveni @ 21/2/2024 :: resolve session not close
        }
    }

    private void callInquiry(String strURL, String merOrderNo) {
        try {
            // Message
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("merchantId", SystemConstants.SPAY_MERCHANT_ID.ID);
            map.put("merOrderNo", merOrderNo);

            // Formatting message to Json
            String jsonString = JSON.toJSONString(map);
           
            cf.writeFile("PaymentCheckJob", "Calling post > jsonString : " + jsonString);
            new LogFunction().logInfo(this.getClass(), "Calling post > jsonString : " + jsonString);
//            sendPost(retrieverDAO, jsonString, strURL, strDivision);
            sendPost(retrieverDAO, jsonString, strURL);
        } catch (Exception e) {
            cf.writeFile("PaymentCheckJob", "*** Failed to inquiry for " + merOrderNo + ". ***\n" + e);
            new LogFunction().logError(this.getClass(), "*** Failed to inquiry for " + merOrderNo + ". ***\n", e);
        }
    }
    
    private ISecurity spayCertInitialise() throws Exception {
        // Certificate Initialization
        ISecurity security = new SecurityImpl();
        SecurityParams securityParams = new SecurityParams();
        securityParams.setRsaPrivateKeyFromType(1);
        securityParams.setRsaPublicKeyFromType(1);
        securityParams.setRsaPrivateKey(new CommonFunction().readMLasisPrivateKey());
//        securityParams.setRsaPrivateKey(new CommonFunction().readMerchantPrivateKey(strDivision));
        securityParams.setRsaPublicKey(new CommonFunction().readPublicKey());
        security.init(securityParams);

        return security;
    }
    
    private String orderInquiryResult;

    public String getOrderInquiryResult() {
        return orderInquiryResult;
    }

    public void setOrderInquiryResult(String orderInquiryResult) {
        this.orderInquiryResult = orderInquiryResult;
    }
    
    public synchronized void logAppActivity( org.hibernate.Session session, String strActivity, String strRemark, String idTableActivity,String strTableActivity,String strCreatedBy) throws Exception {
        cf.writeFile("PaymentCheckJob", "LOGGING EVENT: " + strActivity);
        //BaseDAO baseDAO = new BaseDAOImpl();
        //baseDAO.setSession(session);
        baseDAO.setSession(session);
//        Map sessionMap = ActionContext.getContext().getSession();
        try {
            //Processing History Table: to be done later
//            EisTlActivityModel activityLog = new EisTlActivityModel();
//            cf.writeFile("PaymentCheckJob", "---userId = " +sessionMap.get("userId").toString());
//            User user = (User) checkingDAO.getObjectById(sessionMap.get("userId").toString(), User.class);
//            
//            activityLog.setActivity(strActivity);
//            activityLog.setRemark(strRemark);
//            activityLog.setId_table_activity(idTableActivity);
//            activityLog.setTable_activity(strTableActivity);
//            activityLog.setCreated_by(strCreatedBy);
//            activityLog.setUpdated_by(strCreatedBy);
//            activityLog.defaultAddProperties();
//            
//            baseDAO.getSession().save(activityLog);
//            cf.writeFile("PaymentCheckJob", "-------------------------------END Activity Log ----------------------------------------");
        } catch (Exception ex) {
            throw ex;
        }
        
    }
    
}
