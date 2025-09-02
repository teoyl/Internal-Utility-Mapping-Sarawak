/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend;

import com.lxg.common.model.PublicUserModel;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.EmailTrigger;
import com.sains.framework.base.LogFunction;
import com.sains.framework.base.ModelBase;
import com.sains.framework.base.TimerBase;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.NotificationPModel;
import com.utimaps.model.PaymentModel;
import com.utimaps.web.UtimapsAction;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

/**
 *
 * @author serene
 */
public class USJPaymentReminder extends TimerJob {
    private Map paramMap = new HashMap();
    
  //  PaymentModel paymentModel = new PaymentModel();
       public USJPaymentReminder() {
        super();
        setJobSwitch_on(Boolean.TRUE);
        alwaysTriggerOnce = Boolean.FALSE;
        jobRepeat = REPEAT.Daily;
        jobRunEvery = "15:55"; //HH:MM (Day of the month in 2 digits)
        
    }
    @Override
    public void run() {
        TimerBase.triggerTimer =Boolean.TRUE;
        if (triggerJob()) {
            ModelBase.threadVar.remove(Thread.currentThread().getId()); //remove everytime
            try {
                updateLastTriggered();
                runMe();
            } catch (Exception e) {
                CommonFunction.writeLogFile(e.getStackTrace(), "USJPaymentReminder", "USJPaymentReminder", "run");
            } finally {
                jobScheduler.shutdown();
            }
        }
    }

    public Integer firstDelay() {
        return null;
    }
    
    public void runMe() {
        CommonFunction.writeFile("USJPaymentReminder", "-------------------------------start run USJPaymentReminder----------------------------------------");
        BaseDAO baseDAO = new BaseDAOImpl();
        ApplicationPModel appModel = new ApplicationPModel();
        PublicUserModel publicUser = new PublicUserModel();
        CommonFunction cf = new CommonFunction();

        AutoEmail autoEmail =null;
        AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
        Map mailParam = new HashMap();
        
        String publicUserEmail;
        String publicUserName;
        String publicUserId;
        
        long days_difference = 0;
        String today = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
        String endDate;
        
        paramMap.put("PAYMENT_STATUS", PaymentModel.PAYMENT_STATUS.PENDING_PAYMENT);
        List<PaymentModel> paymentList = baseDAO.list(paramMap, PaymentModel.class);
        CommonFunction.writeFile("USJPaymentReminder", "paymentList " + paymentList.size());

        if(paymentList.size() >0){
            try {
                for(PaymentModel payment : paymentList){
                    appModel = (ApplicationPModel) baseDAO.getModelById(payment.getCase_id(), ApplicationPModel.class);
                    publicUser= (PublicUserModel) baseDAO.getModelByCode("us_user_id", appModel.getApp_submit_by(), new PublicUserModel());

                    endDate = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(payment.getCreated_date());
                    days_difference = cf.find_different_days(endDate, today, SystemConstants.TIME_UNIT.days);
                    CommonFunction.writeFile("USJPaymentReminder", "days_difference " + days_difference);
                    CommonFunction.writeFile("USJPaymentReminder", "getApp_submit_by " + appModel.getApp_submit_by());

                    if(days_difference == 31 ||days_difference == 61){
                        if(publicUser != null) {
                            publicUserName = publicUser.getUs_user_name();
                            publicUserId = publicUser.getUs_user_id();
                            publicUserEmail = publicUser.getUs_email();

        //                  email to applicant after 60 or 30 days
                            autoEmail = autoEmailDAO.getAutoEmailByCode("USJPaymentReminder", new AutoEmail());
                            mailParam.put(EmailTrigger.MAIL_TO, publicUserEmail); // For Public User
                            mailParam.put(EmailTrigger.MAIL_CCTO, "chyes@sains.com.my");
                            mailParam.put("userName", publicUserName); // For Public User
                            mailParam.put("strCaseNo", appModel.getCase_ref());
                            new EmailTrigger().sendEMail(mailParam, autoEmail);

                            String not_id = "";
                            String not_sender = "";
                            String not_subject = "";
                            String not_content = "";
                            if(autoEmail.getNotificationSetup() != null) {
                                not_sender = "USJPaymentReminder";
                                not_id = autoEmail.getNotificationSetup().getNo_id();
                                not_subject = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_subject(), mailParam);
                                not_content = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_body(), mailParam);
                            }

                            NotificationPModel insertNot = new UtimapsAction().insertNotificationBackend(baseDAO.getSession(), appModel.getCase_id(), not_id, not_sender, publicUserId, not_subject, not_content, "U001", "", appModel.get_taskId(), "S");

                            if(insertNot == null) {
                                CommonFunction.writeFile("USJPaymentReminder", "Error when insert notification, no notification is inserted." + appModel.getJob_id() + " - " + not_id + " - " + not_sender + " - " + publicUserId + " - " + not_subject + " - " + not_content + " - " + "U001" + " - " + " " + " - " + appModel.get_taskId() + " - " + "S");
                            } else {
                                new UtimapsAction().auditAction(appModel.getJob_id(), baseDAO.getSession(),not_sender,"Send Notification : " + appModel.getJob_id());
                            }
                        }else{
                           CommonFunction.writeFile("USJPaymentReminder", " No public info found for  " + appModel.getApp_submit_by());
                        }
                    }
                 }
            } catch (Exception e) {
                CommonFunction.writeLogFile(e.getStackTrace(), "USJPaymentReminder", "USJPaymentReminder", "runMe");
            } finally{
                baseDAO.closeSession();
            }
        }
   }
}

