/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sains.framework.base;

import com.PropertyGetter;
import com.sains.framework.model.NotificationSetup;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.ClsMailQueue;
import com.sains.common.util.SystemConstants;
import static com.sains.framework.base.EmailTrigger.domainName;
import static com.sains.framework.base.EmailTrigger.feedbackEmail;
import static com.sains.framework.base.EmailTrigger.portal;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.model.AutoEmailParam;
import com.sains.framework.model.EmailQueueAttachmentModel;
import com.sains.framework.model.EmailQueueModel;
import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;


/**
 *
 * @author swthen
 */
public class EmailQueueTrigger {
    public static final String MAIL_FROM = "from";
    public static final String MAIL_TO = "to";
    public static final String SMS_TO = "sms_to";
    public static final String MAIL_CCTO = "ccto";
    public static final String MAIL_BCCTO = "bccto";
    public static final String MAIL_SUBJECT = "subject";
    public static final String MAIL_BODY = "body";
    public static String domainName = "";
    public static String feedbackEmail = "";
    public static String portal = "";//ahmadni @ 25-Jul-2017
    public static String systemNameForEmail = "";

    public EmailQueueTrigger() {
        try {
            domainName = SystemConstants.DOMAIN.SERVER_URL[SystemConstants.ENV];
            feedbackEmail = SystemConstants.DOMAIN.email_feedback;
            portal = SystemConstants.DOMAIN.PORTAL_URL[SystemConstants.ENV];
            systemNameForEmail = new PropertyGetter().getPropertyText("system.nameForEmail");
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private Map populateParam(Object actionObject, Map<String, String> param) {
        Map paramMap = new HashMap();
        Method m;
        String keyValue = "";
        String paramData = "";
        try {
            for (String key : param.keySet()) {
                keyValue = key;
                paramData = param.get(key);
                if (paramData == null) {
                    paramMap.put(keyValue, "");
                    continue;
                }
                StringTokenizer st = new StringTokenizer(param.get(key), ".");
                String token = "";
                Object obj = null;
                int count = 0;
                while (st.hasMoreTokens()) {
                    count++;
                    token = st.nextToken().trim();
                    if (obj == null) {
                        m = actionObject.getClass().getMethod(token);
                        obj = m.invoke(actionObject);
                    } else {
                        m = obj.getClass().getMethod(token);
                        obj = m.invoke(obj);
                    }
                }
                paramMap.put(key, obj);
            }
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
            if (!keyValue.equals("")) {
                paramMap.put(keyValue, "");
            }
        }
        return paramMap;
    }

    public String generateMailBody(String templateBody, Map<String, Object> param) {
        String mailBody = templateBody;
        for (String key : param.keySet()) {
//            System.out.println("param.get(key).toString() = " + param.get(key).toString());
            // Edited by Delvene @ 07-Oct-2014 :: Will hit NullPointerException if record is null and make it toString()
            if(param.get(key) != null) {
                mailBody = mailBody.replaceAll("<param>" + key + "</param>", param.get(key).toString());
            } else {
                mailBody = mailBody.replaceAll("<param>" + key + "</param>", "");
            }
            
        }
        if (mailBody.indexOf("<domain.textualFull>") > 0){
            mailBody = mailBody.replace("<domain.textualFull>", domainName);
        }
        if (mailBody.indexOf("<system.nameForEmail>") > 0){
            mailBody = mailBody.replace("<system.nameForEmail>", systemNameForEmail);
        }
        if (mailBody.indexOf("<domain.portal>") > 0){
            mailBody = mailBody.replace("<domain.portal>", portal);
        }
        if (mailBody.indexOf("<email.feedback>") > 0){
            mailBody = mailBody.replace("<email.feedback>", feedbackEmail);
        }
        if (mailBody.indexOf("<system.nameForEmail>") > 0){
            mailBody = mailBody.replaceAll("<system.nameForEmail>", systemNameForEmail);
        }
        return mailBody;
    }

    public void sendEMail(Map mailParam, AutoEmail autoEmailSetup) throws Exception {
//        System.out.println("autoEmailSetup.getEnable() :"+autoEmailSetup.getEnable());
        if (autoEmailSetup.getEnable().equals("Y")) {
            String mailTo = "";
            try {
                mailTo = mailParam.remove(MAIL_TO).toString();
                NotificationSetup notificationSetup = autoEmailSetup.getNotificationSetup();

                Map paramNew = new HashMap();//commented by ahmadni @ 9-Aug-2017
                for (AutoEmailParam autoEmailParam : autoEmailSetup.getAutoEmailParam()) {
                    paramNew.put(autoEmailParam.getParam_name(), autoEmailParam.getRetrieve_from());
                }
                BaseDAO dao = new BaseDAOImpl();
                notificationSetup = getOtherLanguageNotification(mailParam, notificationSetup, dao);
                if (notificationSetup.getNo_email().equals("Y")) {
                    for (Address toAdr : InternetAddress.parse(mailTo)) {
                        EmailQueueModel queue = new EmailQueueModel();
                        queue.setSmtp(notificationSetup.getSmtp());
                        queue.setEmail_sender(notificationSetup.getNo_default_sender());
                        queue.setBcc((String)mailParam.get(MAIL_BCCTO));
                        queue.setCc((String)mailParam.get(MAIL_CCTO));
                        queue.setEmail_subject(generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
                        queue.setEmail_content(generateMailBody(notificationSetup.getNo_template_body(), mailParam));
                        queue.setSend_status("N"); //N=New, S=Sent, R=Retry
                        queue.setQueue_type("N"); //N=Normal (), U=Urgent (every 10 seconds?)
                        queue.setFail_count(0);
                        queue.setRecipient(toAdr.toString());
                        queue.setNotification_type("E");//Email

    //                    EmailQueueRecipientModel recipient = new EmailQueueRecipientModel();
    //                    recipient.setRecipient_email(toAdr.toString());
    //                    recipient.setSend_status("N"); //N=New, S=Sent, R=Retry
    //
    //                    queue.getRecipientList().add(recipient);
                        dao.insert(queue);
                    }
                }
                
                if (notificationSetup.getNo_sms().equals("Y")) {
                    String smsTo = mailParam.remove(SMS_TO).toString();
                    for (String phoneNo : smsTo.split(",")) {
                        EmailQueueModel queue = new EmailQueueModel();
                        queue.setSmtp(notificationSetup.getSmtp());
                        queue.setEmail_sender(notificationSetup.getNo_default_sender());
                        queue.setEmail_subject(generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
                        queue.setEmail_content(generateMailBody(notificationSetup.getSms_content(), mailParam));
                        queue.setSend_status("N"); //N=New, S=Sent, R=Retry
                        queue.setQueue_type("N"); //N=Normal (), U=Urgent (every 10 seconds?)
                        queue.setFail_count(0);
                        queue.setRecipient(phoneNo.trim());
                        queue.setNotification_type("S");//SMS

    //                    EmailQueueRecipientModel recipient = new EmailQueueRecipientModel();
    //                    recipient.setRecipient_email(toAdr.toString());
    //                    recipient.setSend_status("N"); //N=New, S=Sent, R=Retry
    //
    //                    queue.getRecipientList().add(recipient);
                        dao.insert(queue);
                    }
                }
                
                
//                ClsMailQueue mail = new ClsMailQueue();
//                mail.setSendFrom(notificationSetup.getNo_default_sender());
//                mail.setOutMailServer(SystemConstants.DOMAIN.smtpServer);
//                mail.setInMailServer(SystemConstants.DOMAIN.smtpServer);
//
//
//                mail.setTo(mailTo);
//                mail.setCc(mailParam.get(MAIL_CCTO) == null ? "" : mailParam.get(MAIL_CCTO).toString());  // ThoTH @ 27-Nov-2015
//                mail.setBcc(mailParam.get(MAIL_BCCTO) == null ? "" : mailParam.get(MAIL_BCCTO).toString());  // ThoTH @ 27-Nov-2015
////                mail.setCc("");
////                mail.setBcc("");
//
//                mail.setSubject(generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
//                //mail.setSubject(notificationSetup.getNo_template_subject());
//                mail.setMailType("H"); // HTML Style;
//
//                mail.setBody(generateMailBody(notificationSetup.getNo_template_body(), mailParam));
//                mail.sendMsg();
////                writeSentLogFile("Email sent to '" + mailTo + "', Subject: " + generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
//                mail = null;
            } catch (Exception e) {
//                writeLogFile("Fail to send to '" + mailTo + "', error log: " + e.toString());
                throw e;
            }
        }
    }
    public void sendEMail(Map mailParam, NotificationSetup notificationSetup) throws Exception {
//        System.out.println("autoEmailSetup.getEnable() :"+autoEmailSetup.getEnable());
        String mailTo = "";
        try {
            mailTo = mailParam.remove(MAIL_TO).toString();
            
            BaseDAO dao = new BaseDAOImpl();
            notificationSetup = getOtherLanguageNotification(mailParam, notificationSetup, dao);
            if (notificationSetup.getNo_email().equals("Y")) {
                for (Address toAdr : InternetAddress.parse(mailTo)) {
                    EmailQueueModel queue = new EmailQueueModel();
                    queue.setEmail_sender(notificationSetup.getNo_default_sender());
                    queue.setBcc((String)mailParam.get(MAIL_BCCTO));
                    queue.setCc((String)mailParam.get(MAIL_CCTO));
                    queue.setEmail_subject(generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
                    queue.setEmail_content(generateMailBody(notificationSetup.getNo_template_body(), mailParam));
                    queue.setSend_status("N"); //N=New, S=Sent, R=Retry
                    queue.setQueue_type("N"); //N=Normal (), U=Urgent (every 10 seconds?)
                    queue.setFail_count(0);
                    queue.setRecipient(toAdr.toString());
                    queue.setNotification_type("E");//Email

    //                EmailQueueRecipientModel recipient = new EmailQueueRecipientModel();
    //                recipient.setRecipient_email(toAdr.toString());
    //                recipient.setSend_status("N"); //N=New, S=Sent, R=Retry
    //
    //                queue.getRecipientList().add(recipient);
                    dao.insert(queue);
                }
            }
            if (notificationSetup.getNo_sms().equals("Y")) {
                String smsTo = mailParam.remove(SMS_TO).toString();
                for (String phoneNo : smsTo.split(",")) {
                    EmailQueueModel queue = new EmailQueueModel();
                    queue.setEmail_sender(notificationSetup.getNo_default_sender());
                    queue.setEmail_subject(generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
                    queue.setEmail_content(generateMailBody(notificationSetup.getSms_content(), mailParam));
                    queue.setSend_status("N"); //N=New, S=Sent, R=Retry
                    queue.setQueue_type("N"); //N=Normal (), U=Urgent (every 10 seconds?)
                    queue.setFail_count(0);
                    queue.setRecipient(phoneNo.trim());
                    queue.setNotification_type("S");//SMS

    //                EmailQueueRecipientModel recipient = new EmailQueueRecipientModel();
    //                recipient.setRecipient_email(toAdr.toString());
    //                recipient.setSend_status("N"); //N=New, S=Sent, R=Retry
    //
    //                queue.getRecipientList().add(recipient);
                    dao.insert(queue);
                }
            }
            
//            ClsMailQueue mail = new ClsMailQueue();
//            mail.setSendFrom(notificationSetup.getNo_default_sender());
//            mail.setOutMailServer(SystemConstants.DOMAIN.smtpServer);
//            mail.setInMailServer(SystemConstants.DOMAIN.smtpServer);
//
//
//            mail.setTo(mailTo);
//            mail.setCc(mailParam.get(MAIL_CCTO) == null ? "" : mailParam.get(MAIL_CCTO).toString());  // ThoTH @ 27-Nov-2015
//            mail.setBcc(mailParam.get(MAIL_BCCTO) == null ? "" : mailParam.get(MAIL_BCCTO).toString());  // ThoTH @ 27-Nov-2015
////                mail.setCc("");
////                mail.setBcc("");
//
//            mail.setSubject(generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
//            //mail.setSubject(notificationSetup.getNo_template_subject());
//            mail.setMailType("H"); // HTML Style;
//
//            mail.setBody(generateMailBody(notificationSetup.getNo_template_body(), mailParam));
//            mail.sendMsg();
//            mail = null;
//            writeSentLogFile("Email sent to '" + mailTo + "', Subject: " + generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
        } catch (Exception e) {
//            writeLogFile("Fail to send to '" + mailTo + "', error log: " + e.toString());
            throw e;
        }
    }

    public void sendEMail(Map mailParam, String autoEmailCode) throws Exception {
        AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
        try {
            AutoEmail autoEmailSetup = autoEmailDAO.getAutoEmailByCode(autoEmailCode, new AutoEmail());
            if (autoEmailSetup == null) {
                Debug.printError("autoEmailSetup is null...");
                throw new Exception();
            }
            sendEMail(mailParam, autoEmailSetup);
        } finally {
            autoEmailDAO.closeSession();
        }
    }
    //wongkk 10/08/2010- file attachment
    public void sendEMail(Map mailParam, AutoEmail autoEmailSetup, List<String> attFile) throws Exception {
        if (autoEmailSetup.getEnable().equals("Y")) {
            String mailTo = "";
            try {
                mailTo = mailParam.remove(MAIL_TO).toString();
                NotificationSetup notificationSetup = autoEmailSetup.getNotificationSetup();

                Map paramNew = new HashMap();//commented by ahmadni @ 9-Aug-2017
                for (AutoEmailParam autoEmailParam : autoEmailSetup.getAutoEmailParam()) {
                    paramNew.put(autoEmailParam.getParam_name(), autoEmailParam.getRetrieve_from());
                }
                BaseDAO dao = new BaseDAOImpl();
                notificationSetup = getOtherLanguageNotification(mailParam, notificationSetup, dao);
                
                if (autoEmailSetup.getNotificationSetup().getNo_email().equals("Y")) {
                    for (Address toAdr : InternetAddress.parse(mailTo)) {
                        EmailQueueModel queue = new EmailQueueModel();
                        queue.setEmail_sender(notificationSetup.getNo_default_sender());
                        queue.setBcc((String)mailParam.get(MAIL_BCCTO));
                        queue.setCc((String)mailParam.get(MAIL_CCTO));
                        queue.setEmail_subject(generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
                        queue.setEmail_content(generateMailBody(notificationSetup.getNo_template_body(), mailParam));
                        queue.setSend_status("N"); //N=New, S=Sent, R=Retry
                        queue.setQueue_type("N"); //N=Normal (), U=Urgent (every 10 seconds?)
                        queue.setFail_count(0);
                        queue.setRecipient(toAdr.toString());
                        queue.setNotification_type("E");//Email
    //                    EmailQueueRecipientModel recipient = new EmailQueueRecipientModel();
    //                    recipient.setRecipient_email(toAdr.toString());
    //                    recipient.setSend_status("N"); //N=New, S=Sent, R=Retry
    //
    //                    queue.getRecipientList().add(recipient);
                        for (String attachmentStr : attFile) {
                            EmailQueueAttachmentModel attachment = new EmailQueueAttachmentModel();
                            attachment.setDr_id(attachmentStr);
                            queue.getAttachmentList().add(attachment);
                        }
                        dao.insert(queue);
                    }
                }
                if (autoEmailSetup.getNotificationSetup().getNo_sms().equals("Y")) {
                    String smsTo = mailParam.remove(SMS_TO).toString();
                    for (String phoneNo : smsTo.split(",")) {
                        EmailQueueModel queue = new EmailQueueModel();
                        queue.setEmail_sender(notificationSetup.getNo_default_sender());
                        queue.setEmail_subject(generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
                        queue.setEmail_content(generateMailBody(notificationSetup.getSms_content(), mailParam));
                        queue.setSend_status("N"); //N=New, S=Sent, R=Retry
                        queue.setQueue_type("N"); //N=Normal (), U=Urgent (every 10 seconds?)
                        queue.setFail_count(0);
                        queue.setRecipient(phoneNo.trim());
                        queue.setNotification_type("S");//SMS
    //                    EmailQueueRecipientModel recipient = new EmailQueueRecipientModel();
    //                    recipient.setRecipient_email(toAdr.toString());
    //                    recipient.setSend_status("N"); //N=New, S=Sent, R=Retry
    //
    //                    queue.getRecipientList().add(recipient);
                        dao.insert(queue);
                    }
                }
                
//                ClsMailQueue mail = new ClsMailQueue();
//                mail.setSendFrom(notificationSetup.getNo_default_sender());
//                mail.setOutMailServer(SystemConstants.DOMAIN.smtpServer);
//                mail.setInMailServer(SystemConstants.DOMAIN.smtpServer);
//
//
//                mail.setTo(mailTo);
//                mail.setCc("");
//                mail.setBcc("");
//
//                mail.setSubject(notificationSetup.getNo_template_subject());
//                mail.setMailType("H"); // HTML Style;
//
//                mail.setAttfilename(attFile);
//
//                mail.setBody(generateMailBody(notificationSetup.getNo_template_body(), mailParam));
//                mail.sendMsg();
////                mail = null;
//
//                //delete attachment file after sent
//                boolean successDelete = false;
//                for (int i=0;i< attFile.size();i++){
//                    successDelete = (new File(attFile.get(i).toString())).delete();
//                    if (!successDelete) {
//                      writeLogFile("Fail to delete attachment ('" + attFile.get(i).toString()+')' );
//                    }
//                }
//        
//            writeSentLogFile("Email sent to '" + mailTo + "', Subject: " + notificationSetup.getNo_template_subject());
//            mail = null;
            } catch (Exception e) {
//                writeLogFile("Fail to send to '" + mailTo + "', error log: " + e.toString());
                throw e;
            }
        }
    }
    //wongkk 10/08/2010- file attachment
    public void sendEMail(Map mailParam, String autoEmailCode, List<String> attFile) throws Exception {
        //System.out.println("sendEmail size " + attFile.size() );
        AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
        try {
            AutoEmail autoEmailSetup = autoEmailDAO.getAutoEmailByCode(autoEmailCode, new AutoEmail());
            sendEMail(mailParam, autoEmailSetup,attFile);
        } finally {
            autoEmailDAO.closeSession();
        }
    }
//end
    private void writeLogFile(String data) {
        CommonFunction.writeFile("emailError", data);
    }
     private void writeSentLogFile(String data) {
        CommonFunction.writeFile("emailSent", data);
    }
     
    //added by amywyp 25-06-2018
    public void sendEMail_definedContent(Map mailParam, AutoEmail autoEmailSetup, String messageContent) throws Exception {
        if (autoEmailSetup.getEnable().equals("Y")) {
            String mailTo = "";
            try {
                mailTo = mailParam.remove(MAIL_TO).toString();
                NotificationSetup notificationSetup = autoEmailSetup.getNotificationSetup();
                Map paramNew = new HashMap();//commented by ahmadni @ 9-Aug-2017
                for (AutoEmailParam autoEmailParam : autoEmailSetup.getAutoEmailParam()) {
                    paramNew.put(autoEmailParam.getParam_name(), autoEmailParam.getRetrieve_from());
                }
                BaseDAO dao = new BaseDAOImpl();
                notificationSetup = getOtherLanguageNotification(mailParam, notificationSetup, dao);
                
                if (autoEmailSetup.getNotificationSetup().getNo_email().equals("Y")) {
                    for (Address toAdr : InternetAddress.parse(mailTo)) {
                        EmailQueueModel queue = new EmailQueueModel();
                        queue.setEmail_sender(notificationSetup.getNo_default_sender());
                        queue.setBcc((String)mailParam.get(MAIL_BCCTO));
                        queue.setCc((String)mailParam.get(MAIL_CCTO));
                        queue.setEmail_subject(generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
                        queue.setEmail_content(messageContent);
                        queue.setSend_status("N"); //N=New, S=Sent, R=Retry
                        queue.setQueue_type("N"); //N=Normal (), U=Urgent (every 10 seconds?)
                        queue.setFail_count(0);
                        queue.setRecipient(toAdr.toString());
                        queue.setNotification_type("E");//Email

    //                    EmailQueueRecipientModel recipient = new EmailQueueRecipientModel();
    //                    recipient.setRecipient_email(toAdr.toString());
    //                    recipient.setSend_status("N"); //N=New, S=Sent, R=Retry
    //
    //                    queue.getRecipientList().add(recipient);
                        dao.insert(queue);
                    }
                }
                
                if (autoEmailSetup.getNotificationSetup().getNo_sms().equals("Y")) {
                    String smsTo = mailParam.remove(SMS_TO).toString();
                    for (String phoneNo : smsTo.split(",")) {
                        EmailQueueModel queue = new EmailQueueModel();
                        queue.setEmail_sender(notificationSetup.getNo_default_sender());
                        queue.setEmail_subject(generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
                        queue.setEmail_content(messageContent);
                        queue.setSend_status("N"); //N=New, S=Sent, R=Retry
                        queue.setQueue_type("N"); //N=Normal (), U=Urgent (every 10 seconds?)
                        queue.setFail_count(0);
                        queue.setRecipient(phoneNo.trim());
                        queue.setNotification_type("S");//SMS

    //                    EmailQueueRecipientModel recipient = new EmailQueueRecipientModel();
    //                    recipient.setRecipient_email(toAdr.toString());
    //                    recipient.setSend_status("N"); //N=New, S=Sent, R=Retry
    //
    //                    queue.getRecipientList().add(recipient);
                        dao.insert(queue);
                    }
                }
                
            } catch (Exception e) {
                writeLogFile("Fail to send to '" + mailTo + "', error log: " + e.toString());
                throw e;
            }
        }
    }
    
    private NotificationSetup getOtherLanguageNotification(Map mailParam, NotificationSetup notificationSetup, BaseDAO dao) {
        if (mailParam.get("language")!=null) {
            NotificationSetup tempNotification = (NotificationSetup)dao.getSession().getNamedQuery("Notification.findByNoType")
                    .setParameter("noType", notificationSetup.getNo_type()+"_"+mailParam.get("language")).uniqueResult();
            if (tempNotification != null) {
                notificationSetup = tempNotification;
            }
        }
        return notificationSetup;
    }
}
