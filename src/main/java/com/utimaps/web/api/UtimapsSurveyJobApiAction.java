/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web.api;

import com.google.gson.Gson;
import com.ism.web.ISMWorkflowBase;
import com.lxg.common.model.PublicUserModel;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.workflow.web.WorkflowApiAction;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.JobDetailModel;
import com.utimaps.web.UtimapsAction;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.pb.web.PbUtimapsApiAction;
import com.sains.common.util.FileOperationUtil;
import com.sains.common.util.FtpInterface;
import com.utimaps.web.SubmissionAction;
import com.utimaps.web.SurveyJobAction;

import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.EmailTrigger;
import com.sains.framework.base.LogFunction;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import com.utimaps.model.ChecklistItemModel;
import com.utimaps.model.ChecklistItemSetupModel;
import com.utimaps.model.ChecklistModel;
import com.utimaps.model.ChecklistSetupModel;
import com.utimaps.model.FileModel;
import com.utimaps.model.NotificationPModel;
import com.utimaps.model.SurveyFirmPModel;
import com.utimaps.web.ISMServicesAction;
import com.utimaps.web.UtimapsAction.PB_STATUS;
import com.utimaps.web.UtimapsAction.WF_STATUS;
import org.hibernate.Session;

public class UtimapsSurveyJobApiAction extends BaseActionSupport<ApplicationPModel> {

    private Map<String, Object> jsonMap = new HashMap();
    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);

    private Map<String, Object> submitDataMap = new HashMap();

    public void submitApplication() throws IOException {
        BaseDAO retrieverDAO = new BaseDAOImpl();
        BufferedReader rd = request.getReader();
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            content.append(line).append("\n");
        }
        rd.close();

        int resStatus = 200;
        String resMessage = "OK";
        String rtnPage = "";
        String submitMsg = "";
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(content.toString());
            JSONObject jsonData = (JSONObject) jsonObject.get("upd_data");
            JSONObject jsonApp = (JSONObject) jsonData.get("app");
            CommonFunction.writeFile("UtimapsJobApi", "jsonObj: " + jsonObject.toString());
            String caseId = jsonApp.get("case_id").toString();

            ApplicationPModel applicationModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", caseId, new ApplicationPModel());
            WorkflowApiAction wfApi = new WorkflowApiAction();

            int docPass = 0;
            docPass = verifySuppDocs(applicationModel);

            if (docPass > 0) {
                submitDataMap.put("submit_message", "Application Failed to Submit!");
                submitDataMap.put("submitted", "failed");
            } else {
                CommonFunction.writeFile("UtimapsJobApi", "calling workflow - usj application id: " + applicationModel.getCase_id() + " at " + new java.sql.Timestamp(System.currentTimeMillis()));
                String wf_rtn = wfApi.startUSJ001(applicationModel.getCase_id(), jsonObject.get("pbUserId").toString());
                CommonFunction.writeFile("UtimapsJobApi", "wf_rtn " + wf_rtn);

                if (wf_rtn.equals("success")) {
                    // to update application status here
                    // your code here

                    //to clear checklist result according to file status
//                    SurveyJobAction sjAction = new SurveyJobAction();
//                    sjAction.clearChecklistResult(retrieverDAO.getSession(), applicationModel);
                    // updated 03-10-2024 :: change to use function within same class due to DEBUG STACK TRACE
                    clearChecklistResult(retrieverDAO.getSession(), applicationModel);

                    // use a self defined updating method bcs the base update doesn't update wf_status for unknown reason 16/04/2024
                    serviceFactory.getUtilAppService().updateForWorkflow(applicationModel, UtimapsAction.DOC_TYPE_CODE_1.APP);

                    //############################################## Trigger ISM [Start] ##############################################
                    new LogFunction().logInfo(this.getClass(), "ATTEMPT APP ISM SUBMIT WORKFLOW CALL FOR -> " + applicationModel.getID());
                    org.apache.logging.log4j.LogManager.getLogger(this.getClass().getSimpleName()).info("ATTEMPT APP ISM SUBMIT WORKFLOW CALL FOR -> " + applicationModel.getID());
                    
                    // if ism record exists then it means this is a resubmission, must be from queried
                    if(applicationModel.getIsm_wf_started().equals("Y")) {
                        new ISMServicesAction().triggerISMWorkflowApp(applicationModel, ISMWorkflowBase.ISM_WF_STEP.UPD_RESUBMIT);
                    } else {
                        new ISMServicesAction().triggerISMWorkflowApp(applicationModel, ISMWorkflowBase.ISM_WF_STEP.SUBMIT);
                    }
                    
                    //############################################## End Tr. ISM [ End ] ##############################################

                    submitMsg = "Successfully submitted Utliity Application! Your Submission Reference is " + applicationModel.getCase_ref();
                    submitDataMap.put("submit_message", submitMsg);
                    submitDataMap.put("submitted", "success");
                } else {
                    submitDataMap.put("submitted", "failed");
                    submitMsg = "Workflow API connection error, please notify system administrators and try again later. Thank you.";
                    submitDataMap.put("submit_message", submitMsg);
                }
            }
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);
            jsonMap.put("submit_data", submitDataMap);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "UtimapsJobApi", "UtimapsSurveyJobApiAction", "submitApplication");
            resStatus = 500;
            resMessage = "Internal Server Error.";
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);

            submitDataMap.put("submitted", "failed");
            submitMsg = "Workflow API Connection Error, please try again later. Thank you";
            submitDataMap.put("submit_message", submitMsg);

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } finally {
            retrieverDAO.closeSession();
        }
    }

    public void submitSJSubmission() throws IOException {
        CommonFunction.writeFile("UtimapsJobApi", "SUBMIT SJ - UtimapsSUrveyJobApiAction- " + new java.sql.Timestamp(System.currentTimeMillis()));

        BaseDAO retrieverDAO = new BaseDAOImpl();
        BufferedReader rd = request.getReader();
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            content.append(line).append("\n");
        }
        rd.close();

        int resStatus = 200;
        String resMessage = "OK";
        String rtnPage = "";
        String submitMsg = "";
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(content.toString());
            JSONObject jsonData = (JSONObject) jsonObject.get("upd_data");
            JSONObject jsonApp = (JSONObject) jsonData.get("app");
            CommonFunction.writeFile("UtimapsJobApi", "jsonObj: " + jsonObject.toString());

            String caseId = jsonApp.get("case_id").toString();
            String jsonJobId = jsonApp.get("job_id").toString();
            CommonFunction.writeFile("UtimapsJobApi", "job id - " + jsonJobId);

            ApplicationPModel applicationModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", caseId, new ApplicationPModel());
            JobDetailModel jobModel = (JobDetailModel) retrieverDAO.getModelByCode("job_id", jsonJobId, new JobDetailModel());

            WorkflowApiAction wfApi = new WorkflowApiAction();
            CommonFunction.writeFile("UtimapsJobApi", "calling workflow - usj application id: " + applicationModel.getCase_id() + " at " + new java.sql.Timestamp(System.currentTimeMillis()));
            String wf_rtn = "";

            //to check for control survey flag and go to dedicated workflow
            if (!Validator.isEmpty(jobModel.getControl_sv_flag())) {
                if (jobModel.getControl_sv_flag().equals("Y")) {
                    switch (jobModel.getUsj_status()) {
                        case PB_STATUS.SUBMISSION_QUERY_USCS40:
                            wf_rtn = wfApi.startUSJ003_03(jsonJobId, jsonObject.get("pbUserId").toString(), UtimapsAction.USER_GROUP.TA, "");
                            break;
                        case PB_STATUS.SUBMISSION_QUERY_USCS80:
                            wf_rtn = wfApi.startUSJ003_07(jsonJobId, jsonObject.get("pbUserId").toString());
                            break;
                        case PB_STATUS.SUBMISSION_QUERY_PRECHECK:
                            wf_rtn = wfApi.startUSJ004_01(jsonJobId, jsonObject.get("pbUserId").toString());
                            break;
                        default:
                            if (jobModel.getComp_completed() != null) {
                                if (jobModel.getComp_completed().equals("E")) {
                                    wf_rtn = wfApi.startUSJ004_01(jsonJobId, jsonObject.get("pbUserId").toString());
                                } else {
                                    wf_rtn = wfApi.startUSJ003(jsonJobId, jsonObject.get("pbUserId").toString());
                                }
                            } else {
                                wf_rtn = wfApi.startUSJ003(jsonJobId, jsonObject.get("pbUserId").toString());
                            }
                            break;
                    }

                } else {
                    switch (jobModel.getUsj_status()) {
                        case PB_STATUS.SUBMISSION_QUERY_USCS80:
                            CommonFunction.writeFile("UtimapsJobApi", "submit uscs80 non traverse");
                            // 06.09.2024 :: Use the same workflow as Traverse Case
                            wf_rtn = wfApi.startUSJ003_07(jsonJobId, jsonObject.get("pbUserId").toString());
                            break;
                        default:
                            wf_rtn = wfApi.startUSJ004_01(jsonJobId, jsonObject.get("pbUserId").toString());
                            break;
                    }
                }
            } else {
                switch (jobModel.getUsj_status()) {
                    case PB_STATUS.SUBMISSION_QUERY_USCS80:
                        CommonFunction.writeFile("UtimapsJobApi", "submit uscs80 non traverse");
                        // 06.09.2024 :: Use the same workflow as Traverse Case
                        wf_rtn = wfApi.startUSJ003_07(jsonJobId, jsonObject.get("pbUserId").toString());
                        break;
                    default:
                        wf_rtn = wfApi.startUSJ004_01(jsonJobId, jsonObject.get("pbUserId").toString());
                        break;
                }
            }

            CommonFunction.writeFile("UtimapsJobApi", "wf_rtn " + wf_rtn);

            if (wf_rtn.equals("success")) {
//                SubmissionAction subAction = new SubmissionAction();

                // to update application/job based on usj_status
                if (jobModel.getControl_sv_flag().equals("Y")) {
                    if (jobModel.getUsj_status().equals(PB_STATUS.SUBMISSION_QUERY_USCS40) || jobModel.getUsj_status().equals(PB_STATUS.SUBMISSION_QUERY_USCS50)) {
                    } else if (jobModel.getUsj_status().equals(PB_STATUS.SUBMISSION_QUERY_USCS80)) {
                    } else if (jobModel.getUsj_status().equals(PB_STATUS.SUBMISSION_QUERY_PRECHECK)) {
                    } else {
                        // updated 03-10-2024 :: change to use function within same class due to DEBUG STACK TRACE
                        clearChecklistResult(retrieverDAO.getSession(), jobModel, "U10");
                        clearChecklistResult(retrieverDAO.getSession(), jobModel, "U20");

                        // use a self defined updating method bcs the base update doesn't update wf_status for unknown reason 16/04/2024
                        serviceFactory.getUtilAppService().updateForWorkflow(applicationModel, UtimapsAction.DOC_TYPE_CODE_1.USJ);

                    }
                } else {
                    //to clear checklist result according to file status and if submission include control survey or not
                    // updated 03-10-2024 :: change to use function within same class due to DEBUG STACK TRACE
                    clearChecklistResult(retrieverDAO.getSession(), jobModel, "U20");
                }

                sendSubmissionEmailNotification(retrieverDAO, jobModel);
                
                //############################################## Trigger ISM [Start] ##############################################
                new LogFunction().logInfo(this.getClass(), "ATTEMPT SUB ISM SUBMIT WORKFLOW CALL FOR -> " + jobModel.getID());
                org.apache.logging.log4j.LogManager.getLogger(this.getClass().getSimpleName()).info("ATTEMPT SUB ISM SUBMIT WORKFLOW CALL FOR -> " + jobModel.getID());
                new ISMServicesAction().triggerISMWorkflowSub(jobModel, ISMWorkflowBase.ISM_WF_STEP.SUBMIT);
                //############################################## End Tr. ISM [ End ] ##############################################

                submitMsg = "Survey Job Submission Successful! Your Survey Job No. is " + jobModel.getUsj_no();
                submitDataMap.put("submit_message", submitMsg);
                submitDataMap.put("submitted", "success");
            } else {
                submitMsg = "Survey Job Submission Failed, please try again later. Thank you";
                submitDataMap.put("submit_message", submitMsg);
                submitDataMap.put("submitted", "failed");
            }
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);
            jsonMap.put("submit_data", submitDataMap);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "UtimapsJobApi", "UtimapsSurveyJobApiAction", "submitSJSubmission");
            resStatus = 500;
            resMessage = "Internal Server Error.";
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } finally {
            retrieverDAO.closeSession();
        }
    }

    public void sendSubmissionEmailNotification(BaseDAO retrieverDAO, JobDetailModel jobModel) {

        //send email & notification for hardcopy needed to be submitted
        Map mailJsonMap = new HashMap();
        AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
        autoEmailDAO.setSession(retrieverDAO.getSession());
        AutoEmail autoEmail = null;
        AutoEmail autoEmailHc = null;
        FtpInterface ftp = FileOperationUtil.getFtpInterface();

        try {
            CommonFunction.writeFile("UtimapsJobApi", "send submission email to public user");

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

            if (jobModel.getUsj_status().equals(PB_STATUS.SUBMISSION_RESUBMIT_HARDCOPY) || jobModel.getUsj_status().equals(PB_STATUS.SUBMISSION_QUERY_USCS10) || jobModel.getUsj_status().equals(PB_STATUS.SUBMISSION_QUERY_USCS80) || jobModel.getUsj_status().equals(PB_STATUS.SUBMISSION_QUERY_USCS40) || jobModel.getUsj_status().equals(PB_STATUS.SUBMISSION_QUERY_USCS50)) {
                autoEmail = autoEmailDAO.getAutoEmailByCode("USJResubmitted", new AutoEmail());
                autoEmailHc = autoEmailDAO.getAutoEmailByCode("USJResubHardcopy", new AutoEmail());
            } else {
                autoEmail = autoEmailDAO.getAutoEmailByCode("USJNewSubmission", new AutoEmail());
                autoEmailHc = autoEmailDAO.getAutoEmailByCode("USJNotifyNewHardcopy", new AutoEmail());
            }

            CommonFunction.writeFile("UtimapsJobApi", "autoEmail " + autoEmail);
            mailParam.put(EmailTrigger.MAIL_TO, publicUserEmail); // For Public User
            mailParam.put(EmailTrigger.MAIL_CCTO, SystemConstants.email_landsurveyboard);

            CommonFunction.writeFile("UtimapsJobApi", "userName - " + publicUserName);
            CommonFunction.writeFile("UtimapsJobApi", "jobModel - " + jobModel.getUsj_no());

            mailParam.put("userName", publicUserName); // For Public User
            mailParam.put("strUsjNo", jobModel.getUsj_no());

            new EmailTrigger().sendEMail(mailParam, autoEmail);

            Map mailParamHc = new HashMap();
            mailParamHc.put(EmailTrigger.MAIL_TO, publicUserEmail); // For Public User
            mailParamHc.put(EmailTrigger.MAIL_CCTO, SystemConstants.email_landsurveyboard);
            mailParamHc.put("userName", publicUserName); // For Public User
            mailParamHc.put("strUsjNo", jobModel.getUsj_no());
            mailParamHc.put("strDivision", returnStrDivision(jobModel.getUsj_div()));
            mailParamHc.put("strHardcopyDocList", setupHardcopyList(retrieverDAO, jobModel));

            new EmailTrigger().sendEMail(mailParamHc, autoEmailHc);

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

            //Insert Notification
            NotificationPModel insertNot = new UtimapsAction().insertNotification2(retrieverDAO.getSession(), jobModel.getJob_id(), not_id, not_sender, publicUserId, not_subject, not_content, "U001", "", jobModel.get_taskId(), "S");

            if (insertNot == null) {
                CommonFunction.writeFile("UtimapsJobApi", "Error when insert notification, no notification is inserted." + jobModel.getJob_id() + " - " + not_id + " - " + not_sender + " - " + publicUserId + " - " + not_subject + " - " + not_content + " - " + "U001" + " - " + "" + " - " + jobModel.get_taskId() + " - " + "S");
            } else {
                new UtimapsAction().auditAction(jobModel.getJob_id(), retrieverDAO.getSession(), not_sender, "Send Notification : " + jobModel.getJob_id());
            }

            String not_id_hc = "";
            String not_sender_hc = "";
            String not_subject_hc = "";
            String not_content_hc = "";
            if (autoEmailHc.getNotificationSetup() != null) {
//                                not_sender = ActionContext.getContext().getSession().get("userId").toString();
                not_sender_hc = "UTiMAPS BACKEND";
                not_subject_hc = autoEmailHc.getNotificationSetup().getNo_id();
                not_subject_hc = new EmailTrigger().generateMailBody(autoEmailHc.getNotificationSetup().getNo_template_subject(), mailParamHc);
                not_content_hc = new EmailTrigger().generateMailBody(autoEmailHc.getNotificationSetup().getNo_template_body(), mailParamHc);
            }

            //Insert Notification
            NotificationPModel insertNotHc = new UtimapsAction().insertNotification2(retrieverDAO.getSession(), jobModel.getJob_id(), not_id_hc, not_sender_hc, publicUserId, not_subject_hc, not_content_hc, "U001", "", jobModel.get_taskId(), "S");

            if (insertNotHc == null) {
                CommonFunction.writeFile("UtimapsJobApi", "Error when insert notification, no notification is inserted." + jobModel.getJob_id() + " - " + not_id_hc + " - " + not_sender_hc + " - " + publicUserId + " - " + not_subject_hc + " - " + not_content_hc + " - " + "U001" + " - " + "" + " - " + jobModel.get_taskId() + " - " + "S");
            } else {
                new UtimapsAction().auditAction(jobModel.getJob_id(), retrieverDAO.getSession(), not_sender_hc, "Send Notification : " + jobModel.getJob_id());
            }

            CommonFunction.writeFile("UtimapsJobApi", "### Public email sent ");

            mailJsonMap.put("status", "success");
            mailJsonMap.put("errMsg", "");
        } catch (Exception e) {
            mailJsonMap.put("errMsg", "Fail to update case status");
            mailJsonMap.put("status", "fail");
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "UtimapsJobApi", "UtimapsSurveyJobApiAction", "sendSubmissionEmailNotification");
        }

    }

    public String returnStrDivision(String usj_no) {
        String strDivision = "KUCHING";

        // to do fetch division string
        return strDivision;
    }

    public String setupHardcopyList(BaseDAO dao, JobDetailModel jModel) {
        String hcList = "";

        PbUtimapsApiAction pbApiAction = new PbUtimapsApiAction();

        Map ups21 = pbApiAction.fetchHardcopyList("HCUS11"); //trav
        Map ups31 = pbApiAction.fetchHardcopyList("HCUS21"); //util

        String ups21Title = "<p>Part 1: Submission of Traverse Survey</p>";
        String ups31Title = "<p>Part 2: USJ Submission</p>";

        List us11List = (List) ups21.get("us11List");
        List us21List = (List) ups31.get("us21List");

        if (jModel.getControl_sv_flag().equals("Y")) {
            hcList += ups21Title;
            hcList += "<ol>";

            //iterate ups21 as <li>
            hcList += "<li>Traverse Survey Plan Including:";
            hcList += "<ol type='I'>";
            for (Object value : us11List) {
                if (!value.toString().toLowerCase().contains("field books")) {
                    hcList += "<li>" + value.toString().replaceAll(".*=(.*)}", "$1") + "</li>";
                }
            }
            hcList += "</li></ol>";
            hcList += "<li>Field Books</li>";

            hcList += "</ol>";

            hcList += ups31Title;
            hcList += "<ol>";

            //iterate ups31 as <li>
            for (Object value : us21List) {
                hcList += "<li>" + value.toString().replaceAll(".*=(.*)}", "$1") + "</li>";
            }

            hcList += "</ol>";
        } else {
            hcList += ups31Title;
            hcList += "<ol>";

            //iterate ups31 as <li>
            for (Object value : us21List) {
                hcList += "<li>" + value.toString().replaceAll(".*=(.*)}", "$1") + "</li>";
            }

            hcList += "</ol>";
        }

        CommonFunction.writeFile("UtimapsJobApi", "hcList - " + hcList);
        return hcList;
    }

    public void acknowledgeHardcopySubmit() throws IOException {
        CommonFunction.writeFile("UtimapsJobApi", "ACK HC - UtimapsSurveyJobApiAction- " + new java.sql.Timestamp(System.currentTimeMillis()));

        BaseDAO retrieverDAO = new BaseDAOImpl();
        BufferedReader rd = request.getReader();
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            content.append(line).append("\n");
        }
        rd.close();

        int resStatus = 200;
        String resMessage = "OK";
        String rtnPage = "";
        String submitMsg = "";
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(content.toString());
            JSONObject jsonData = (JSONObject) jsonObject.get("upd_data");
            JSONObject jsonApp = (JSONObject) jsonData.get("app");
            CommonFunction.writeFile("UtimapsJobApi", "jsonObj: " + jsonObject.toString());

            String caseId = jsonApp.get("case_id").toString();
            String jsonJobId = jsonApp.get("job_id").toString();
            CommonFunction.writeFile("UtimapsJobApi", "job id - " + jsonJobId);

            ApplicationPModel applicationModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", caseId, new ApplicationPModel());
            JobDetailModel jobModel = (JobDetailModel) retrieverDAO.getModelByCode("job_id", jsonJobId, new JobDetailModel());

            WorkflowApiAction wfApi = new WorkflowApiAction();
//            CommonFunction.writeFile("UtimapsJobApi", "calling workflow - usj application id: " + applicationModel.getCase_id() + " at " + new java.sql.Timestamp(System.currentTimeMillis()));
            String wf_rtn = "skipped";
            //to check for control survey flag and go to dedicated workflow
            try {
                if (jobModel.getControl_sv_flag().equals("Y")) {
                    if (jobModel.getUsj_status().equals(PB_STATUS.SUBMISSION_QUERY_USCS40) || jobModel.getUsj_status().equals(PB_STATUS.SUBMISSION_QUERY_USCS50)) {
                        wf_rtn = wfApi.startUSJ003_04(jobModel.getJob_id(), jsonObject.get("pbUserId").toString(), "");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                CommonFunction.writeLogFile(e.getStackTrace(), "UtimapsJobApi", "UtimapsSurveyJobApiAction", "submitApplication");
            }

            if (wf_rtn.equals("success") || wf_rtn.equals("skipped")) {
                try {
                    jobModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_HC_ACK);
//                jobModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.TRAV_STARTED);
                    jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_SUB);
                    jobModel.setHardcopy_received_date(DateUtil.getCurrentTimestamp());
                    jobModel.setAck_sub_hardcopy("Y");
                    serviceFactory.getSubmissionJobService().updateSubmission(jobModel);

                    submitMsg = "Hardcopy Acknowledgement Successful! Thank you.";
                    submitDataMap.put("submit_message", submitMsg);
                    submitDataMap.put("submitted", "success");
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonFunction.writeLogFile(e.getStackTrace(), "UtimapsJobApi", "UtimapsSurveyJobApiAction", "submitApplication");

                    submitMsg = "Hardcopy Acknowledgement Failed, please try again later. Thank you";
                    submitDataMap.put("submit_message", submitMsg);
                    submitDataMap.put("submitted", "failed");
                }
            } else {
                submitMsg = "Hardcopy Acknowledgement Failed, please try again later. Thank you";
                submitDataMap.put("submit_message", submitMsg);
                submitDataMap.put("submitted", "failed");
            }

            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);
            jsonMap.put("submit_data", submitDataMap);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "UtimapsJobApi", "UtimapsSurveyJobApiAction", "acknowledgeHardcopySubmit");
            resStatus = 500;
            resMessage = "Internal Server Error.";
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } finally {
            retrieverDAO.closeSession();
        }
    }

    public void submitApplicationAfterPayment() throws IOException {
        BaseDAO retrieverDAO = new BaseDAOImpl();
        BufferedReader rd = request.getReader();
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            content.append(line).append("\n");
        }
        rd.close();

        int resStatus = 200;
        String resMessage = "OK";
        String rtnPage = "";
        String submitMsg = "";
        try {
            String caseId = "171106880RB80C03n44Q";
            ApplicationPModel applicationModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", caseId, new ApplicationPModel());
            WorkflowApiAction wfApi = new WorkflowApiAction();
            CommonFunction.writeFile("UtimapsJobApi", "calling workflow - usj application id: " + applicationModel.getCase_id() + " at " + new java.sql.Timestamp(System.currentTimeMillis()));
            String wf_rtn = wfApi.startUSJ002(applicationModel.getCase_id());
            CommonFunction.writeFile("UtimapsJobApi", "wf_rtn " + wf_rtn);

            if (wf_rtn.equals("success")) {
                // to update application status here
                // your code here

                // use a self defined updating method bcs the base update doesn't update wf_status for unknown reason 16/04/2024
                serviceFactory.getUtilAppService().updateForWorkflow(applicationModel, UtimapsAction.DOC_TYPE_CODE_1.APP);
                submitMsg = "Successfully submitted Issuance USJ!";
                jsonMap.put("submitted", "success");
            } else {
                submitDataMap.put("submitted", "failed");
                submitMsg = "Application Submission Failed, please try again later. Thank you";
            }
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);
            jsonMap.put("submit_message", submitMsg);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "UtimapsJobApi", "UtimapsSurveyJobApiAction", "submitApplicationAfterPayment");
            resStatus = 500;
            resMessage = "Internal Server Error.";
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } finally {
            retrieverDAO.closeSession();
        }
    }

    public int verifySuppDocs(ApplicationPModel model) {
        int pass = 0;
        String[] requiredDocs = new String[]{"LOA", "LOC", "PUP", "LAW"};

        List unsubmittedDocs = new ArrayList();

        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());

        try {
            for (String doc : requiredDocs) {
                Method appM = model.getClass().getMethod("get" + doc);
                List fieldObj = (List) appM.invoke(model);
                if (fieldObj.size() > 0) {

                } else {
                    String docSql = "select code_desc from t_setup_code "
                            + "where code_type = 'DOC' "
                            + "and CODE_1 = 'APP' "
                            + "and CODE_2 = '" + doc + "'";
                    unsubmittedDocs.add(retrieverDAO.getSingleValue(docSql));
                    pass++;
                }
            }

            submitDataMap.put("unsub_docs", unsubmittedDocs);

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "UtimapsJobApi", "UtimapsSurveyJobApiAction", "verifySuppDocs");
        }

        return pass;
    }

    public void callWorkflowAfterPrecheck() throws IOException {
        BaseDAO retrieverDAO = new BaseDAOImpl();
        int resStatus = 200;
        String resMessage = "Success";
        String rtnPage = "";
        String resultMsg = "";

        BufferedReader rd = request.getReader();
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            content.append(line).append("\n");
        }
        rd.close();

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(content.toString());
            String jobId = (String) jsonObject.get("jobId");
            String strResult = (String) jsonObject.get("strResult");
            String strMessage = (String) jsonObject.get("strMsg");
//            String jobId = request.getParameter("jobId");
//            String strResult = request.getParameter("strResult").toString();
//            String strMessage = request.getParameter("strMsg").toString();
            Debug.printDebug("callWorkflowAfterPrecheck jsonObject " + jobId + " " + strResult + " " + strMessage);
            CommonFunction.writeFile("UtimapsJobApi", "jsonObject " + jobId + " " + strResult + " " + strMessage);

            if (!Validator.isEmpty(jobId)) {
                JobDetailModel jobModel = (JobDetailModel) retrieverDAO.getModelByCode("job_id", jobId, new JobDetailModel());
                WorkflowApiAction wfApi = new WorkflowApiAction();
                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                request.setAttribute("ignoreCsrfCheck", "true");

                if (jobModel != null) {
                    if (strResult.equals("0")) {
                        new WorkflowApiAction().insertProcessingHistory(retrieverDAO.getSession(), jobModel.getJob_id(), "", WF_STATUS.RO_PRECHECK_PASSED, "");

                        if (!Validator.isEmpty(jobModel.getWf_status_2())) {
                            if (jobModel.getWf_status_2().equals(WF_STATUS.RO_PRECHECK_PENDING)) {
                                resStatus = 200;
                                resMessage = "Precheck with no discrepancy error(s).";
                                CommonFunction.writeFile("UtimapsJobApi", "calling usj003_07 workflow - usj job id: " + jobModel.getJob_id());
                                String wf_rtn = wfApi.startUSJ003_07(jobModel.getJob_id(), "BACKEND");

                                CommonFunction.writeFile("UtimapsJobApi", "wf_rtn " + wf_rtn);
                            } else {
                                resStatus = 403;
                                resMessage = "Invalid Job Status.";
                            }
                        } else {
                            resStatus = 403;
                            resMessage = "Invalid Job Status.";
                        }

                    } else if (strResult.equals("-1")) {
                        resStatus = 406;
                        resMessage = "Unexpected error. Please contact with admin.";
                    } else {
                        resMessage = "Precheck with " + strResult + " discrepancy error(s).";
                        jobModel.set_operation(JobDetailModel.OPERATION.PROCESS_QUERY_PRECHECK);
                        serviceFactory.getSubmissionJobService().updateSubmission(jobModel);
                    }
                } else {
                    resStatus = 404;
                    resMessage = "Survey Job not found.";
                }
            } else {
                resStatus = 404;
                resMessage = "Utility Survey Job not found.";
            }

            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "UtimapsJobApi", "UtimapsSurveyJobApiAction", "callWorkflowAfterPrecheck");
            resStatus = 500;
            resMessage = "Internal Server Error.";
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);

        } finally {
            retrieverDAO.closeSession();
        }

        response.setContentType("application/json");
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
    }

    public void clearChecklistResult(Session session, ApplicationPModel appModel) {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        dao.getSession().enableFilter("caseFilter").setParameter("caseNo", appModel.getCase_id());
        dao.getSession().enableFilter("typeFilter").setParameter("fileType", "CM");
        dao.getSession().enableFilter("fileFilter");

        try {
            ApplicationPModel appModel2 = new ApplicationPModel();
            appModel2 = (ApplicationPModel) dao.getModelById(appModel.getCase_id(), ApplicationPModel.class);
            ChecklistSetupModel checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type", "APP01", new ChecklistSetupModel());
            ChecklistModel checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "APP01," + appModel2.getCase_id(), new ChecklistModel());

            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                csi.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                });
                for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });

            appModel2.setChecklistSetupCaseModel(checklistSetupModel);
            appModel2.setChecklistModel(checklistModel);

            appModel2.set_operation(ApplicationPModel.OPERATION.PROCESS_CHECK_CHECKLIST);
            serviceFactory.getSurveyApplicationService().clearApplicationChecklist(appModel2);

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "clearChecklistResult");
        }
    }

    public void clearChecklistResult(Session session, JobDetailModel jobModel, String processType) {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        dao.getSession().enableFilter("caseFilter").setParameter("caseNo", jobModel.getJob_id());
        dao.getSession().enableFilter("typeFilter").setParameter("fileType", "CM");
        dao.getSession().enableFilter("fileFilter");
//        dao.getSession().enableFilter("statusFilter");

        try {
            JobDetailModel jobModel2 = new JobDetailModel();
            jobModel2 = (JobDetailModel) dao.getModelById(jobModel.getJob_id(), JobDetailModel.class);
            ChecklistSetupModel checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type", processType, new ChecklistSetupModel());
            ChecklistModel checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", processType + "," + jobModel2.getJob_id(), new ChecklistModel());

            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                csi.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                });
                for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });

            jobModel2.setChecklistSetupCaseModel(checklistSetupModel);
            jobModel2.setU10ChecklistModel(checklistModel);

            jobModel2.set_operation(JobDetailModel.OPERATION.PROCESS_CLEAR_CHECKLIST);
            serviceFactory.getSubmissionJobService().clearSubmissionChecklist(jobModel2);

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "clearChecklistResult");
        }
    }
}
