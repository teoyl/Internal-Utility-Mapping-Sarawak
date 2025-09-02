/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sains.framework.base;

import com.PropertyGetter;
import com.SysConf;
import com.sains.framework.model.NotificationSetup;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.ClsMail;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import static com.sains.framework.base.EmailQueueTrigger.systemNameForEmail;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.model.AutoEmailParam;
import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.io.IOUtils;


/**
 *
 * @author swthen
 */
public class EmailTrigger {
    public static final String MAIL_FROM = "from";
    public static final String MAIL_TO = "to";
    public static final String MAIL_CCTO = "ccto";
    public static final String MAIL_BCCTO = "bccto";
    public static final String MAIL_SUBJECT = "subject";
    public static final String MAIL_BODY = "body";
    public static String domainName = "";
    public static String feedbackEmail = "";
    public static String portal = "";//ahmadni @ 25-Jul-2017
    public static String systemNameForEmail = "";
    
    // Copied from ssts @ 23/4/2024
    public static final class ATTACH {
        public static final String AttList = "attList";  // Store the list of Map, that contains 2 attributes below
        public static final String AttInputStream = "attInputStream";  // Store the File InputStream
        public static final String AttFileName = "attFileName";  // Store the FileName, to be shown in the Email Attachment List
    }

    public EmailTrigger() {
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

    public void check(Object actionObject, String actionName, String methodName, String status) {
        AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
        try {
            AutoEmail autoEmailSetup = new AutoEmail();
            autoEmailSetup = autoEmailDAO.getAutoEmailSetup(actionName, methodName, status, autoEmailSetup);
            if (autoEmailSetup != null) {
                sendEMail(actionObject, autoEmailSetup);
            }
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
            //automation fail... need to create any log?
        } finally {
            autoEmailDAO.closeSession();
        }
    }

    /*
     * Created by TTH @ 9-Jul-2010
     * Used by Workflow
     * */
    public void sendEmail(Object actionObject, AutoEmail autoEmailSetup) throws Exception {
        sendEMail(actionObject, autoEmailSetup);
    }

    private void sendEMail(Object actionObject, AutoEmail autoEmailSetup) throws Exception {
        Map param = new HashMap();
        if (autoEmailSetup.getEnable().equals("Y")) {
            String mailTo = "";
            try {
                NotificationSetup notificationSetup = autoEmailSetup.getNotificationSetup();
                mailTo = autoEmailSetup.getMail_to();

                /*NotificationSetup noticeSetup = new NotificationSetup();
                BaseDAO<NotificationSetup> notificationSetupDAO = new BaseDAOImpl<NotificationSetup>();
                noticeSetup = notificationSetupDAO.listById("basicAccCreated", noticeSetup);
                notificationSetupDAO.closeSession();*/

                ClsMail mail = new ClsMail();
                mail.setSendFrom(notificationSetup.getNo_default_sender());
                if (Validator.isEmpty(notificationSetup.getSmtp()) ) {
                    mail.setOutMailServer(SysConf.get("smtp.default"));
                    mail.setInMailServer(SysConf.get("smtp.default"));
                } else {
                    mail.setOutMailServer(notificationSetup.getSmtp());
                    mail.setInMailServer(notificationSetup.getSmtp());
                }
//                Map param = new HashMap();
                param.put(MAIL_TO, autoEmailSetup.getMail_to());
                param.put(MAIL_CCTO, autoEmailSetup.getMail_ccto());
                param.put(MAIL_BCCTO, autoEmailSetup.getMail_bccto());
                param = populateParam(actionObject, param);
                mail.setTo(param.get(MAIL_TO) == null ? "" : param.get(MAIL_TO).toString());
                mail.setCc(param.get(MAIL_CCTO) == null ? "" : param.get(MAIL_CCTO).toString());
                mail.setBcc(param.get(MAIL_BCCTO) == null ? "" : param.get(MAIL_BCCTO).toString());

                mail.setSubject(notificationSetup.getNo_template_subject());
                mail.setMailType("H"); // HTML Style;
                Map paramNew = new HashMap();//commented by ahmadni @ 9-Aug-2017
                for (AutoEmailParam autoEmailParam : autoEmailSetup.getAutoEmailParam()) {
                    paramNew.put(autoEmailParam.getParam_name(), autoEmailParam.getRetrieve_from());
                }
                mail.setBody(generateMailBody(notificationSetup.getNo_template_body(), populateParam(actionObject, paramNew)));
                mail.sendMsg();
                mail = null;
                writeSentLogFile("Email sent to '" + param.get(MAIL_TO).toString() + "', Subject: " + notificationSetup.getNo_template_subject());
            } catch (Exception e) {
                writeLogFile("Fail to send to '" + autoEmailSetup.getMail_to() + "', error log: " + e.toString());
            }
        }
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
        return mailBody;
    }

    public void sendEMail(Map mailParam, AutoEmail autoEmailSetup) throws Exception {
//        System.out.println("autoEmailSetup.getEnable() :"+autoEmailSetup.getEnable());
        if (autoEmailSetup.getEnable().equals("Y")) {
            String mailTo = "";
            List<File> tempFileList = null;  // YongLai @ 20-06-2024 : Copied from SSTS's Code
            try {
                mailTo = mailParam.remove(MAIL_TO).toString();
                NotificationSetup notificationSetup = autoEmailSetup.getNotificationSetup();

                ClsMail mail = new ClsMail();
                mail.setSendFrom(notificationSetup.getNo_default_sender());
                if (Validator.isEmpty(notificationSetup.getSmtp()) ) {
                    mail.setOutMailServer(SysConf.get("smtp.default"));
                    mail.setInMailServer(SysConf.get("smtp.default"));
                } else {
                    mail.setOutMailServer(notificationSetup.getSmtp());
                    mail.setInMailServer(notificationSetup.getSmtp());
                }


                mail.setTo(mailTo);
                mail.setCc(mailParam.get(MAIL_CCTO) == null ? "" : mailParam.get(MAIL_CCTO).toString());  // ThoTH @ 27-Nov-2015
                mail.setBcc(mailParam.get(MAIL_BCCTO) == null ? "" : mailParam.get(MAIL_BCCTO).toString());  // ThoTH @ 27-Nov-2015
//                mail.setCc("");
//                mail.setBcc("");

                mail.setSubject(generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
                //mail.setSubject(notificationSetup.getNo_template_subject());
                mail.setMailType("H"); // HTML Style;

                mail.setBody(generateMailBody(notificationSetup.getNo_template_body(), mailParam));
                
                // YongLai @ 20-06-2024 : Copied from SSTS's Code :: attach file from ftps
                if (mailParam.containsKey(ATTACH.AttList)) {
                    List<Map> attachList = (List<Map>)mailParam.get(ATTACH.AttList);
                    
                    if (attachList != null && !attachList.isEmpty()) {
//                        System.out.println("attMapList != null");
                        File tempFile;
                        tempFileList = new ArrayList();
                        List<String> fileNameList = new ArrayList();
                        for (Map attMap : attachList){
                            InputStream is = (InputStream) attMap.get(EmailTrigger.ATTACH.AttInputStream);
                            fileNameList.add((String)attMap.get(EmailTrigger.ATTACH.AttFileName));
                            tempFile = File.createTempFile(CommonFunction.getId(20), ".tmp");
                            tempFile.deleteOnExit();
                            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                                IOUtils.copy(is, out);
                            }
                            tempFileList.add(tempFile);
                        }
                        mail.setAttTempFileList(tempFileList);
                        mail.setAttFileNameList(fileNameList);
                    }
                }
                // YongLai @ 20-06-2024 : Copied from SSTS's Code - END
                
                mail.sendMsg();
                writeSentLogFile("Email sent to '" + mailTo + "', Subject: " + generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
                mail = null;
            } catch (Exception e) {
                writeLogFile("Fail to send to '" + mailTo + "', error log: " + e.toString());
                throw e;
            }
        }
    }
    public void sendEMail(Map mailParam, NotificationSetup notificationSetup) throws Exception {
//        System.out.println("autoEmailSetup.getEnable() :"+autoEmailSetup.getEnable());
        String mailTo = "";
        try {
            mailTo = mailParam.remove(MAIL_TO).toString();
            ClsMail mail = new ClsMail();
            mail.setSendFrom(notificationSetup.getNo_default_sender());
            if (Validator.isEmpty(notificationSetup.getSmtp()) ) {
                mail.setOutMailServer(SysConf.get("smtp.default"));
                mail.setInMailServer(SysConf.get("smtp.default"));
            } else {
                mail.setOutMailServer(notificationSetup.getSmtp());
                mail.setInMailServer(notificationSetup.getSmtp());
            }


            mail.setTo(mailTo);
            mail.setCc(mailParam.get(MAIL_CCTO) == null ? "" : mailParam.get(MAIL_CCTO).toString());  // ThoTH @ 27-Nov-2015
            mail.setBcc(mailParam.get(MAIL_BCCTO) == null ? "" : mailParam.get(MAIL_BCCTO).toString());  // ThoTH @ 27-Nov-2015
//                mail.setCc("");
//                mail.setBcc("");

            mail.setSubject(generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
            //mail.setSubject(notificationSetup.getNo_template_subject());
            mail.setMailType("H"); // HTML Style;

            mail.setBody(generateMailBody(notificationSetup.getNo_template_body(), mailParam));
            mail.sendMsg();
            mail = null;
            writeSentLogFile("Email sent to '" + mailTo + "', Subject: " + generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
        } catch (Exception e) {
            writeLogFile("Fail to send to '" + mailTo + "', error log: " + e.toString());
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

                ClsMail mail = new ClsMail();
                mail.setSendFrom(notificationSetup.getNo_default_sender());
                if (Validator.isEmpty(notificationSetup.getSmtp()) ) {
                    mail.setOutMailServer(SysConf.get("smtp.default"));
                    mail.setInMailServer(SysConf.get("smtp.default"));
                } else {
                    mail.setOutMailServer(notificationSetup.getSmtp());
                    mail.setInMailServer(notificationSetup.getSmtp());
                }


                mail.setTo(mailTo);
                mail.setCc("");
                mail.setBcc("");

                mail.setSubject(notificationSetup.getNo_template_subject());
                mail.setMailType("H"); // HTML Style;

                mail.setAttFileNameList(attFile);

                mail.setBody(generateMailBody(notificationSetup.getNo_template_body(), mailParam));
                mail.sendMsg();
//                mail = null;

                //delete attachment file after sent
                boolean successDelete = false;
                for (int i=0;i< attFile.size();i++){
                    successDelete = (new File(attFile.get(i).toString())).delete();
                    if (!successDelete) {
                      writeLogFile("Fail to delete attachment ('" + attFile.get(i).toString()+')' );
                    }
                }
        
            writeSentLogFile("Email sent to '" + mailTo + "', Subject: " + notificationSetup.getNo_template_subject());
            mail = null;
            } catch (Exception e) {
                writeLogFile("Fail to send to '" + mailTo + "', error log: " + e.toString());
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
    public void sendEMailConvey(Map mailParam, AutoEmail autoEmailSetup, String messageContent) throws Exception {
        if (autoEmailSetup.getEnable().equals("Y")) {
            String mailTo = "";
            try {
                mailTo = mailParam.remove(MAIL_TO).toString();
                NotificationSetup notificationSetup = autoEmailSetup.getNotificationSetup();

                ClsMail mail = new ClsMail();
                mail.setSendFrom(notificationSetup.getNo_default_sender());
                if (Validator.isEmpty(notificationSetup.getSmtp()) ) {
                    mail.setOutMailServer(SysConf.get("smtp.default"));
                    mail.setInMailServer(SysConf.get("smtp.default"));
                } else {
                    mail.setOutMailServer(notificationSetup.getSmtp());
                    mail.setInMailServer(notificationSetup.getSmtp());
                }

                mail.setTo(mailTo);
                mail.setCc(mailParam.get(MAIL_CCTO) == null ? "" : mailParam.get(MAIL_CCTO).toString());  // ThoTH @ 27-Nov-2015
                mail.setBcc(mailParam.get(MAIL_BCCTO) == null ? "" : mailParam.get(MAIL_BCCTO).toString());  // ThoTH @ 27-Nov-2015

                mail.setSubject(generateMailBody(notificationSetup.getNo_template_subject(), mailParam));
                mail.setMailType("H"); // HTML Style;

                mail.setBody(messageContent);
                mail.sendMsg();
                writeSentLogFile("Email sent to '" + mailTo + "', Subject: " + notificationSetup.getNo_template_subject());
                mail = null;
            } catch (Exception e) {
                writeLogFile("Fail to send to '" + mailTo + "', error log: " + e.toString());
                throw e;
            }
        }
    }
}
