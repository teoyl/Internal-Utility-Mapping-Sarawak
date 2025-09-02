/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webservice;

import com.lxg.common.model.PublicUserModel;
import com.opensymphony.xwork2.ActionContext;
import com.pb.web.AttachmentUploadAction;
import com.pb.web.PbUtimapsApiAction;
import com.sains.common.util.FileOperationUtil;
import com.sains.common.util.FtpInterface;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.utimaps.model.ApplicationPModel;
import com.sains.framework.base.ModelBase;
import com.sains.framework.base.Debug;
import com.sains.framework.base.EmailTrigger;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import com.utimaps.model.FileModel;
import com.utimaps.model.JobDetailModel;
import com.utimaps.model.NotificationPModel;
import com.utimaps.model.PrecheckHistoryModel;
import com.utimaps.model.SurveyFirmPModel;
import com.utimaps.web.UtimapsAction;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.text.WordUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author Aiman
 */
public class PrecheckServiceAction extends BaseActionSupport<JobDetailModel> {

    public File getFileById(String jobId, String fileType) {
        File rtnFile = null;
        FileModel returnModel = new FileModel();
        BaseDAO retrieverDAO = new BaseDAOImpl();
        String strParam = jobId + "," + fileType + "," + "P";

        returnModel = (FileModel) retrieverDAO.getModelByCode("case_id,file_type,file_status", strParam, new FileModel());

        System.out.println("returnModel " + returnModel.getFile_id());

        try {
            AttachmentUploadAction uploadAction = new AttachmentUploadAction();
            uploadAction.setUploadID(returnModel.getFile_id());
            rtnFile = uploadAction.downloadTempFile();
//            rtnFile = new File(file.getAbsolutePath() + "." + returnModel.getFile_ext());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();
            baseDAO.closeSession();
        }

        System.out.println("rtnFile " + rtnFile.getAbsolutePath());
        return rtnFile;
    }

    public String insertTraverseHistory(String usjSeq, String usjYear, String divNo, String progMsg, String progDate, String progSignal, int progStep) {
//        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//        request.setAttribute("ignoreCsrfCheck", "true");

        Debug.printDebug("insert traverse history ---");
        String rtnString = "fail_insert";
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());

        JobDetailModel detModel = (JobDetailModel) retrieverDAO.getModelByCode("usj_seq,usj_year,usj_div", usjSeq + "," + usjYear + "," + divNo, new JobDetailModel());

        if (detModel == null) {
            return "fail_no_job";
        }

        try {
            PrecheckHistoryModel insertHistory = new PrecheckHistoryModel();
            insertHistory.setTask_id(com.sains.framework.base.CommonFunction.getId(20));
            insertHistory.setJob_id(detModel.getJob_id());
            insertHistory.setPrecheck_type("T");
            insertHistory.setTask_seq(progStep);
            insertHistory.setTask_desc(progMsg);
            insertHistory.setTraverse_signal(progSignal);
            insertHistory.setCreated_by("PRECHECK_API");
            insertHistory.setCreated_date(new java.sql.Timestamp(System.currentTimeMillis()));
            serviceFactory.getPrecheckHistoryService().histInsert(insertHistory);

            rtnString = "success";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();
        }

        baseDAO.closeSession();
        return rtnString;
    }

    public String getTraverseStatus(String usjNo, String divNo) {
        Debug.printDebug("GET TRAVERSE STATUS SCS CALL");
        String strResponse = "";

        try {
            String token = getBearerToken();
            String statusCheckUrl = SystemConstants.DOMAIN.domain + "scs/UtilityControlPrecheckResult?usj_no=" + usjNo + "&div_no=" + divNo;

            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpGet httpGet = new HttpGet(statusCheckUrl);
                httpGet.setHeader(HttpHeaders.AUTHORIZATION, token);

                try (CloseableHttpResponse response = client.execute(httpGet)) {
                    int statusCode = response.getStatusLine().getStatusCode();
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        strResponse = EntityUtils.toString(entity);
                        Debug.printDebug("Traverse status response: " + strResponse);
                    }
                }
            }
        } catch (Exception e) {
            Debug.printDebug("Error checking traverse status: " + e.getMessage());
            e.printStackTrace();
        }

        return strResponse;
    }

    public String getBearerToken() throws IOException {
        String token = "";

        if (token == null || Validator.isEmpty(token)) {
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost post = new HttpPost(SystemConstants.DOMAIN.domain + "scs/esub/api/auth"); // SCS AUTH API

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("LOGIN", "utimaps_api_2024"));
                params.add(new BasicNameValuePair("PWD", "UTiMAPS@2024"));
                post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

                try (CloseableHttpResponse response = client.execute(post)) {
                    System.out.println(response.getStatusLine().getStatusCode());
                    HttpEntity responseEntity = response.getEntity();
                    // get token from response header
                    if (responseEntity != null) {
                        token = response.getFirstHeader("Authorization").getValue();
                        Debug.printDebug("token - " + token);
                        return token;
                    }
                    return "";
                }
            }

        } else {

        }

        return token;
    }

    public JobDetailModel updateManualPrecheckStage(JobDetailModel jobModel, String new_status) {
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        JobDetailModel returnJobModel = new JobDetailModel();

        try {
            jobModel.updatableColumns = new String[]{"job_id", "precheck_stage"};
            jobModel.setPrecheck_stage(new_status);
            retrieverDAO.update(jobModel);

            returnJobModel = (JobDetailModel) retrieverDAO.getModelByCode("job_id", jobModel.getJob_id(), new JobDetailModel());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();
            baseDAO.closeSession();
        }

        return returnJobModel;
    }

    public void handlePrecheckCompletedEmail(JobDetailModel jobModel, BaseDAO retrieverDAO) {
        Map mailJsonMap = new HashMap();
        AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
        autoEmailDAO.setSession(retrieverDAO.getSession());
        AutoEmail autoEmail = null;
        AutoEmail autoEmailHc = null;
        FtpInterface ftp = FileOperationUtil.getFtpInterface();

        Boolean passed = true;

        if (jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.FAIL)
                || jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_FAIL)
                || jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_FAIL)) {
            passed = false;
        }

        if (jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.FAIL)
                || jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.SUCCESS)
                || jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_FAIL)
                || jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_PASS)
                || jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_FAIL)
                || jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_PASS)) {

            if (jobModel.getPrecheck_notif_sent() == null) {
                jobModel.setPrecheck_notif_sent("N");
            }

            if (jobModel.getPrecheck_notif_sent().equals("N")) {
                Debug.printDebug("will send precheck notif");

                try {
                    CommonFunction.writeFile("UtimapsJobApi", "send precheck completed email to public user");

                    String publicUserName = "";
                    String publicUserId = "";
                    String publicUserEmail = "default@utimaps.sains.com.my";
                    String surveyFirmUserName = "";
                    String surveyFirmEmail = "";

                    PublicUserModel publicUser = (PublicUserModel) retrieverDAO.getModelByCode("us_user_id", jobModel.getApplicationModel().getApp_submit_by(), new PublicUserModel());

                    if (publicUser != null) {
                        publicUserName = publicUser.getUs_user_name();
                        publicUserId = publicUser.getUs_user_id();
                        publicUserEmail = publicUser.getUs_email();
                        CommonFunction.writeFile("UtimapsJobApi", publicUserEmail);
//                publicUserEmail = "mhdaiman@sains.com.my";
                    }

                    SurveyFirmPModel surveyFirm = (SurveyFirmPModel) retrieverDAO.getModelByCode("case_id", jobModel.getCase_id(), new SurveyFirmPModel());
                    if (surveyFirm != null) {
                        surveyFirmUserName = surveyFirm.getFirm_oic();
                        surveyFirmEmail = surveyFirm.getFirm_email();

                        publicUserName = Validator.isEmpty(surveyFirmUserName) ? publicUserName : publicUserName + ", " + surveyFirmUserName;
                        publicUserEmail = Validator.isEmpty(surveyFirmEmail) ? publicUserEmail : publicUserEmail + "," + surveyFirmEmail;
                    }

                    Map mailParam = new HashMap();

                    if (passed) {
                        autoEmail = autoEmailDAO.getAutoEmailByCode("USJPrecheckSuccess", new AutoEmail());
                    } else {
                        autoEmail = autoEmailDAO.getAutoEmailByCode("USJPrecheckFailed", new AutoEmail());
                    }

                    CommonFunction.writeFile("UtimapsJobApi", "autoEmail " + autoEmail);
                    mailParam.put(EmailTrigger.MAIL_TO, publicUserEmail); // For Public User
                    mailParam.put(EmailTrigger.MAIL_CCTO, SystemConstants.email_landsurveyboard);

                    CommonFunction.writeFile("UtimapsJobApi", "userName - " + publicUserName);
                    CommonFunction.writeFile("UtimapsJobApi", "jobModel - " + jobModel.getUsj_no());

                    mailParam.put("userName", publicUserName); // For Public User
                    mailParam.put("strUsjNo", jobModel.getUsj_no());

                    new EmailTrigger().sendEMail(mailParam, autoEmail);

                    String not_id = "";
                    String not_sender = "";
                    String not_subject = "";
                    String not_content = "";
                    if (autoEmail.getNotificationSetup() != null) {
//                                not_sender = ActionContext.getContext().getSession().get("userId").toString();
                        not_sender = "UTiMAPS BACKEND";
                        not_id = autoEmail.getNotificationSetup().getNo_id();
                        not_subject = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_subject(), mailParam);
                        not_content = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_body(), mailParam);
                    }

                    NotificationPModel insertNot = new UtimapsAction().insertNotificationBackend(retrieverDAO.getSession(), jobModel.getJob_id(), not_id, not_sender, publicUserId, not_subject, not_content, "U001", "", jobModel.get_taskId(), "S");

                    if (insertNot == null) {
//                        CommonFunction.writeFile("UtimapsJobApi", "Error when insert notification, no notification is inserted." + jobModel.getJob_id() + " - " + not_id + " - " + not_sender + " - " + publicUserId + " - " + not_subject + " - " + not_content + " - " + "U001" + " - " + "" + " - " + jobModel.get_taskId() + " - " + "S");
                    } else {
                        new UtimapsAction().auditAction(jobModel.getJob_id(), retrieverDAO.getSession(), not_sender, "Send Notification : " + jobModel.getJob_id());
                    }

                    jobModel.updatableColumns = new String[]{"job_id", "precheck_notif_sent"};
                    jobModel.setPrecheck_notif_sent("Y");
                    retrieverDAO.update(jobModel);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    autoEmailDAO.closeSession();
                    retrieverDAO.closeSession();
                }

            } else {
                Debug.printDebug("not sending precheck notif");
            }

        }

    }

    public String initUtilPrecheck(JobDetailModel jobModel) {
        String strReturn = "";

        String ROCheck = "nro";

        model = jobModel;

        switch (jobModel.getUsj_status()) {
            case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK:
                ROCheck = "full";
                break;
//            case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS10:
            default:
                if (jobModel.getControl_sv_flag().equals("N")) {
                    ROCheck = "full";
                } else {
                    ROCheck = "nro";
                }
                break;
        }

        try {
            String usjNoFormatted = setupUsjNoFormattedString(jobModel.getUsj_no());
            String paramHashKey = setupHashParamKey();
            System.out.println("param key - " + paramHashKey);
            String pcCallUrl = SystemConstants.DOMAIN.domain_gis_tnt + "uti_mapi/api/CallAndCheckPrecheck";
            pcCallUrl += "?jobId=" + jobModel.getJob_id();
            pcCallUrl += "&usjNo=" + usjNoFormatted;
            pcCallUrl += "&parameters=" + paramHashKey;
            pcCallUrl += "&RO_status=" + ROCheck;
            strReturn = callPublicApiGet(pcCallUrl);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            baseDAO.closeSession();
        }

        return strReturn;
    }

    public String findPbUserId(JobDetailModel jobModel) {
        String userId = "";
        PublicUserModel user = null;
        try {
            String pbUserName = jobModel.getApplicationModel().getApp_submit_by();
            user = (PublicUserModel) baseDAO.getObjectByCode("us_user_id", pbUserName, new PublicUserModel());
            userId = user.getUs_id();
        } finally {
            baseDAO.closeSession();
        }
        return userId;
    }

    public String setupHashParamKey() {
        System.out.println("hash key param setup");
//        String paramUserName = "usersd01"; //hardcode testing
        String paramUserName = findPbUserId(model);
        String param = "username='" + paramUserName + "'&hashkey='" + GetMapHashKey(paramUserName) + "'";
        System.out.println("param - " + param);

        return java.util.Base64.getEncoder().encodeToString(param.getBytes());
    }

    public String GetMapHashKey(String userName) {
        try {
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
//            String CurrentMonth = currentTimestamp.Now.Month.ToString("d2");
            String CurrentYear = Integer.toString(currentTimestamp.getYear() + 1900);
            String CurrentDay = String.format("%02d", currentTimestamp.getDate());
            String CurrentMonth = String.format("%02d", currentTimestamp.getMonth() + 1);
            String appKey = "^" + userName + "^UTIMAPS";
            String hashStr = CurrentYear + "^" + CurrentMonth + "^" + CurrentDay + appKey;
//            System.out.println("hashStr - " + hashStr);
            String hValue = calcHmac(hashStr);
            hValue = hValue.toUpperCase();
//            System.out.println("hval - " + hValue);
//            String hvalue = "";

            return hValue;
        } catch (Exception e) {
            return "";
        }

    }

    public String calcHmac(String data) {
        try {
            String AUTH_TOKEN = SystemConstants.DOMAIN.MapViewerKey;
            byte[] key = AUTH_TOKEN.getBytes(StandardCharsets.US_ASCII);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            byte[] byteArray = data.getBytes(StandardCharsets.US_ASCII);
            byte[] resultBytes = mac.doFinal(byteArray);
            return byteArrayToHexString(resultBytes).toUpperCase();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String byteArrayToHexString(byte[] array) {
        Formatter formatter = new Formatter();
        for (byte b : array) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public JobDetailModel getJobDetail(String usjSeq, String usjYear, String divNo) {
        BaseDAO retrieverDAO = new BaseDAOImpl();
        JobDetailModel detModel = new JobDetailModel();

        try {
            detModel = (JobDetailModel) retrieverDAO.getModelByCode("usj_seq,usj_year,usj_div", usjSeq + "," + usjYear + "," + divNo, new JobDetailModel());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();
            baseDAO.closeSession();
        }
        return detModel;
    }

    public String callPublicApiGet(String apiUrl) throws Exception {
        System.out.println(apiUrl);
        SSLContext sslContext = null;
        String strResponse = "";
        try {
            sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (certificate, authType) -> true).build();
        } catch (KeyManagementException ex) {
//            Logger.getLogger(UtilApplicationAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        org.apache.http.impl.client.CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();

        HttpGet httpGet = new HttpGet(apiUrl);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");

        CloseableHttpResponse resp = httpClient.execute(httpGet);
        BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
        String responseString = "";
        while ((responseString = br.readLine()) != null) {
            strResponse = strResponse + responseString;
        }

        return strResponse;
    }

    public List getTraverseHistoryList(JobDetailModel jobDetailModel) {
        String[] precheckHistoryFields = new String[]{"task_id", "job_id", "task_seq", "task_desc", "created_date", "created_by", "traverse_signal"};
        BaseDAO retrieverDAO = new BaseDAOImpl();
        List travHistoryList = new ArrayList();

        try {
            Map travHistoryMap = new HashMap();
            Map pcHistMap = new HashMap();
            pcHistMap.put("job_id", jobDetailModel.getJob_id());
            pcHistMap.put("precheck_type", "T");
            List<PrecheckHistoryModel> travHistList = new ArrayList();
            travHistList = retrieverDAO.list_order(pcHistMap, PrecheckHistoryModel.class, "order by created_date");

            if (!travHistList.isEmpty()) {
                travHistoryMap.clear();
                for (PrecheckHistoryModel hist : travHistList) {
                    PrecheckHistoryModel history = (PrecheckHistoryModel) hist;
                    travHistoryMap = getDataMap(history, precheckHistoryFields);
                    travHistoryList.add(travHistoryMap);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();
            baseDAO.closeSession();
        }

        return travHistoryList;
    }

    public String setupUsjNoFormattedString(String input) {
        // Use regular expression to extract the parts
        String transformed = input
                .replaceAll("USJ/", "") // Remove the initial part
                .replaceAll("D/", "") // Remove the 'D/' part
                .replaceAll("/", "");      // Remove all remaining slashes

        // Format the result by ensuring the first segment is always 2 digits
        // We assume that the first segment might be 1 or 2 digits and we need to pad with '0' if it's a single digit
        String[] parts = transformed.split("(?<=\\d)(?=\\d{4})");
        if (parts[0].length() == 1) {
            parts[0] = "0" + parts[0];
        }

        return String.join("", parts);
    }

    public Map getDataMap(Object from, String[] dataFields) {
        Map modelMap = new HashMap();
        String columnName = null;
        Object getterObject;
        int startIdx = 0;
        for (int idx = startIdx; idx < ((String[]) dataFields).length; idx++) {
            try {
                columnName = WordUtils.capitalize(((String[]) dataFields)[idx]);
                Method m = from.getClass().getMethod("get" + columnName);
                try {
                    getterObject = m.invoke(from);

                    if (getterObject == null) {
                        getterObject = "";
                    }

                    modelMap.put(((String[]) dataFields)[idx], getterObject);

                } catch (IllegalAccessException ex) {
                    Logger.getLogger(PrecheckServiceAction.class
                            .getName()).log(Level.SEVERE, null, ex);

                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(PrecheckServiceAction.class
                            .getName()).log(Level.SEVERE, null, ex);

                } catch (InvocationTargetException ex) {
                    Logger.getLogger(PrecheckServiceAction.class
                            .getName()).log(Level.SEVERE, null, ex);

                }
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(PrecheckServiceAction.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (SecurityException ex) {
                Logger.getLogger(PrecheckServiceAction.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

        baseDAO.closeSession();
        return modelMap;
    }

}
