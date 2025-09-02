/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web.api;

import com.google.gson.Gson;
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
import com.utimaps.web.SubmissionAction;
import com.utimaps.web.SurveyJobAction;
import java.util.Base64;

import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.model.User;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Base64;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public class UtimapsTestApiAction extends BaseActionSupport<ApplicationPModel> {

    private Map<String, Object> jsonMap = new HashMap();
    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
    private CommonFunction cf = new CommonFunction();
    private Map<String, Object> submitDataMap = new HashMap();
    
    public void submitApplicationTest() throws IOException {
        BaseDAO retrieverDAO = new BaseDAOImpl();
        int resStatus = 200;
        String resMessage = "OK";
        String rtnPage = "";
        String submitMsg = "";
        try {

            String caseId = request.getParameter("caseId");

            ApplicationPModel applicationModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", caseId, new ApplicationPModel());
            WorkflowApiAction wfApi = new WorkflowApiAction();
            Debug.printDebug("calling workflow - usj application id: " + applicationModel.getCase_id() + " at " + new java.sql.Timestamp(System.currentTimeMillis()));
            String wf_rtn = wfApi.startUSJ001(applicationModel.getCase_id(), "Te3pDRrRm3d43Afe");
            Debug.printDebug("wf_rtn " + wf_rtn);

            if (wf_rtn.equals("success")) {
                // to update application status here
                // your code here
                
                //to clear checklist result according to file status
                SurveyJobAction sjAction = new SurveyJobAction();
                sjAction.clearChecklistResult(retrieverDAO.getSession(), applicationModel);

                // use a self defined updating method bcs the base update doesn't update wf_status for unknown reason 16/04/2024
                serviceFactory.getUtilAppService().updateForWorkflow(applicationModel, UtimapsAction.DOC_TYPE_CODE_1.APP);
                submitMsg = "Successfully submitted Utliity Application! Your Submission Reference is " + applicationModel.getCase_ref();
                jsonMap.put("submitted", "success");
            } else {
                jsonMap.put("submitted", "failed");
                submitMsg = "Application Submission Failed, please try again later. Thank you";
            }
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);
            jsonMap.put("submit_message", submitMsg);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } catch (Exception e) {
            Debug.printDebug("EXCEPTION SUBMIT UTIL APP - " + e);
            e.printStackTrace();
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

    public void submitApplicationTestAfterPayment() throws IOException {
        BaseDAO retrieverDAO = new BaseDAOImpl();
        int resStatus = 200;
        String resMessage = "OK";
        String rtnPage = "";
        String submitMsg = "";
        try {
            String caseId = request.getParameter("caseId");

            ApplicationPModel applicationModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", caseId, new ApplicationPModel());
            WorkflowApiAction wfApi = new WorkflowApiAction();
            Debug.printDebug("calling workflow - usj application id: " + applicationModel.getCase_id() + " at " + new java.sql.Timestamp(System.currentTimeMillis()));
            String wf_rtn = wfApi.startUSJ002(applicationModel.getCase_id());
            Debug.printDebug("wf_rtn " + wf_rtn);

            if (wf_rtn.equals("success")) {
                // to update application status here
                // your code here

                // use a self defined updating method bcs the base update doesn't update wf_status for unknown reason 16/04/2024
                serviceFactory.getUtilAppService().updateForWorkflow(applicationModel, UtimapsAction.DOC_TYPE_CODE_1.USJ);
                submitMsg = "Successfully submitted Issuance USJ!";
                jsonMap.put("submitted", "success");
            } else {
                jsonMap.put("submitted", "failed");
                submitMsg = "Application Submission Failed, please try again later. Thank you";
            }
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);
            jsonMap.put("submit_message", submitMsg);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } catch (Exception e) {
            Debug.printDebug("EXCEPTION SUBMIT UTIL APP - " + e);
            e.printStackTrace();
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

    public void submitUSJSubTest() throws IOException {
        BaseDAO retrieverDAO = new BaseDAOImpl();
        int resStatus = 200;
        String resMessage = "OK";
        String rtnPage = "";
        String submitMsg = "";
        try {
//            String jobId = "1714723080971pxx99c0";
            String jobId = request.getParameter("jobId");
            Debug.printDebug("jobId " + jobId);
            JobDetailModel jobModel = (JobDetailModel) retrieverDAO.getModelByCode("job_id", jobId, new JobDetailModel());
            ApplicationPModel applicationModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", jobModel.getCase_id(), new ApplicationPModel());
            WorkflowApiAction wfApi = new WorkflowApiAction();
            Debug.printDebug("calling workflow - usj application id: " + jobModel.getJob_id() + " at " + new java.sql.Timestamp(System.currentTimeMillis()));
            String wf_rtn = "";

            //to check for control survey flag and go to dedicated workflow
            if(jobModel.getControl_sv_flag().equals("Y")) {
                wf_rtn = wfApi.startUSJ003(jobId, applicationModel.getApp_submit_by());
            } else {
                wf_rtn = wfApi.startUSJ004_01(jobId, applicationModel.getApp_submit_by());
            }
            Debug.printDebug("wf_rtn " + wf_rtn);

            if (wf_rtn.equals("success")) {
                // to update application status here
                // your code here
                SubmissionAction subAction = new SubmissionAction();
                if(jobModel.getControl_sv_flag().equals("Y")) {
                    subAction.clearChecklistResult(retrieverDAO.getSession(), jobModel, "U10");
                    subAction.clearChecklistResult(retrieverDAO.getSession(), jobModel, "U20");
                } else {
                    subAction.clearChecklistResult(retrieverDAO.getSession(), jobModel, "U20");
                }

                // use a self defined updating method bcs the base update doesn't update wf_status for unknown reason 16/04/2024
//                serviceFactory.getUtilAppService().updateForWorkflow(jobModel);
                submitMsg = "Successfully submitted U10 or U20!";
                jsonMap.put("submitted", "success");
            } else {
                jsonMap.put("submitted", "failed");
                submitMsg = "Application Submission Failed, please try again later. Thank you";
            }
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);
            jsonMap.put("submit_message", submitMsg);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } catch (Exception e) {
            Debug.printDebug("EXCEPTION SUBMIT UTIL APP - " + e);
            e.printStackTrace();
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

    public void submitUSJSub2Test() throws IOException {
        BaseDAO retrieverDAO = new BaseDAOImpl();
        int resStatus = 200;
        String resMessage = "OK";
        String rtnPage = "";
        String submitMsg = "";
        try {
            String jobId = request.getParameter("jobId");
            String usjNo = request.getParameter("usj_no");
            String wfId = request.getParameter("wf_id");
            JobDetailModel jobModel = null;
            
            Debug.printDebug("jobId " + jobId);
            
            if(!Validator.isEmpty(usjNo)) {
                jobModel = (JobDetailModel) retrieverDAO.getModelByCode("usj_no", usjNo, new JobDetailModel());
            } else if(!Validator.isEmpty(jobId)) {
                jobModel = (JobDetailModel) retrieverDAO.getModelByCode("job_id", jobId, new JobDetailModel());
            }
            
            if(jobModel != null) {
                if(!Validator.isEmpty(wfId)) {
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("calling workflow - usj application id: " + jobModel.getJob_id() + " at " + new java.sql.Timestamp(System.currentTimeMillis()) );

                    String wf_rtn = "";
                    if(wfId.equals("3")) {
                        wf_rtn = wfApi.startUSJ003_03(jobModel.getJob_id(), "BACKEND", UtimapsAction.USER_GROUP.TA, "");
                        submitMsg = "Successfully submitted U30!";
                    } else if(wfId.equals("4")) {
                        wf_rtn = wfApi.startUSJ003_04(jobModel.getJob_id(), "BACKEND", "");

                        submitMsg = "Successfully submitted U40!";
                    } else if(wfId.equals("5")) {
                        wf_rtn = wfApi.startUSJ003_05(jobModel.getJob_id(), "BACKEND", "");
                        submitMsg = "Successfully submitted USCS60!";
                    } else if(wfId.equals("6")) {
                        jobModel.setComp_approved_date(DateUtil.getCurrentTimestamp());
                        retrieverDAO.beginBatchTransaction();
                        retrieverDAO.getSession().update(jobModel);
                        retrieverDAO.commitBatchTransaction();
                        wf_rtn = wfApi.startUSJ003_06(jobModel.getJob_id(), "BACKEND", "");

                        submitMsg = "Successfully submitted USCS70!";
                    } else if(wfId.equals("7")) {
                        wf_rtn = wfApi.startUSJ003_07(jobModel.getJob_id(), "BACKEND");
                        submitMsg = "Successfully submitted U50!";
                    }

                    Debug.printDebug("wf_rtn " + wf_rtn);

                    if (wf_rtn.equals("success")) {
                        jsonMap.put("submitted", "success");
                    } else {
                        jsonMap.put("submitted", "failed");
                        submitMsg = "Application Submission Failed, please try again later.";
                    }
                } else {
                    jsonMap.put("submitted", "failed");
                    submitMsg = "Application Submission Failed, please select a task to start.";
                }
            } else {
                jsonMap.put("submitted", "failed");
                submitMsg = "Application Submission Failed, no case with this Survey Job No. found.";
            }
            
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);
            jsonMap.put("submit_message", submitMsg);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } catch (Exception e) {
            retrieverDAO.rollbackBatchTransaction();
            Debug.printDebug("EXCEPTION SUBMIT SUBMISSION - " + e);
            e.printStackTrace();
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
    
    public void submitUSJNC() throws IOException {
        BaseDAO retrieverDAO = new BaseDAOImpl();
        int resStatus = 200;
        String resMessage = "OK";
        String rtnPage = "";
        String submitMsg = "";
        try {
            String jobId = request.getParameter("jobId");
            String wfId = request.getParameter("wf_id");
            String usjNo = request.getParameter("usj_no");
            JobDetailModel jobModel = null;
            
            Debug.printDebug("jobId " + jobId);

            if(!Validator.isEmpty(usjNo)) {
                jobModel = (JobDetailModel) retrieverDAO.getModelByCode("usj_no", usjNo, new JobDetailModel());
            } else if(!Validator.isEmpty(jobId)) {
                jobModel = (JobDetailModel) retrieverDAO.getModelByCode("job_id", jobId, new JobDetailModel());
            }
            
            if(jobModel != null) {
                if(!Validator.isEmpty(wfId)) {
            
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("calling workflow - usj job id: " + jobModel.getJob_id() + " at " + new java.sql.Timestamp(System.currentTimeMillis()) );

                    String wf_rtn = "";
                    if(wfId.equals("1")) {
                        wf_rtn = wfApi.startUSJ004_01(jobModel.getJob_id(), "BACKEND");
                        submitMsg = "Successfully submitted U20!";
                    } else if(wfId.equals("2")) {
                        wf_rtn = wfApi.startUSJ004_02(jobModel.getJob_id(), "BACKEND");
                        submitMsg = "Successfully submitted Hardcopy!";
                    } else if(wfId.equals("3")) {
                        wf_rtn = wfApi.startUSJ004_03(jobModel.getJob_id(), "BACKEND");
                        submitMsg = "Successfully submitted USCS50!";
                    }

                    Debug.printDebug("wf_rtn " + wf_rtn);

                    if (wf_rtn.equals("success")) {
                        jsonMap.put("submitted", "success");
                    } else {
                        jsonMap.put("submitted", "failed");
                        submitMsg = "Application Submission Failed, please try again later. Thank you";
                    }
                    } else {
                    jsonMap.put("submitted", "failed");
                    submitMsg = "Application Submission Failed, please select a task to start.";
                }
            } else {
                jsonMap.put("submitted", "failed");
                submitMsg = "Application Submission Failed, no case with this Survey Job No. found.";
            }
            
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);
            jsonMap.put("submit_message", submitMsg);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } catch (Exception e) {
            Debug.printDebug("EXCEPTION SUBMIT UTIL APP - " + e);
            e.printStackTrace();
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
    
    public String checkUtiJobProg() throws Exception {
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        try {
            List<Map<String, Object>> lsJobProg = null;
            String jobProgSQL = "SELECT JOB_NUMBER FROM UTIJOBPROG WHERE JOB_NUMBER = ? ";
            Query query = baseDAO.getSession().createSQLQuery(jobProgSQL);
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            query.setString(1, "0");
            lsJobProg = query.list();
            cf.writeFile("UtimapsTestApi", "caseList.size()***************************************" + lsJobProg.size());

            if (lsJobProg.size()>0){
                WorkflowApiAction wfApi = new WorkflowApiAction();
                String wf_rtn = "";
                for (Map jobProg : lsJobProg) {
                    String jobNumber = jobProg.get("JOB_NUMBER").toString();
                    String assignFrom = jobProg.get("ASSIGN_FROM").toString();
                    String assignTo = jobProg.get("ASSIGN_TO").toString();
                    String jobAction = jobProg.get("JOB_ACTION").toString();
                    String divId = jobProg.get("DIV_ID").toString();
                    String userIdFrom = "DEFAULT";
                    String userIdTo = "DEFAULT";
                    String usUserIdFrom = "DEFAULT";
                    String usUserIdTo = "DEFAULT";
                    User userFrom = null;
                    User userTo = null;

                    String strSQL = "SELECT tsuo.US_ID FROM T_SETUP_ORACLE_USER tsou " +
                        "LEFT JOIN T_SETUP_USER_ORACLE tsuo ON tsuo.ORACLE_USER_ID = tsou.ORACLE_USER_ID " +
                        "WHERE tsou.ORACLE_ID = '" + assignFrom +"'";

                    String strSQL2 = "SELECT tsuo.US_ID FROM T_SETUP_ORACLE_USER tsou " +
                        "LEFT JOIN T_SETUP_USER_ORACLE tsuo ON tsuo.ORACLE_USER_ID = tsou.ORACLE_USER_ID " +
                        "WHERE tsou.ORACLE_ID = '" + assignTo +"'";

                    List lasisUserList = new ArrayList();
                    lasisUserList = retrieverDAO.getListFromSql(strSQL,null);

                    List lasisUserList2 = new ArrayList();
                    lasisUserList2 = retrieverDAO.getListFromSql(strSQL2,null);

                    if(lasisUserList.size() > 0){
                        for (Map lasisUser : (List<Map>) lasisUserList) {
                            if(lasisUser.get("US_ID")!= null){
                                userIdFrom =  lasisUser.get("US_ID").toString();
                            }
                        }

                        userFrom = (User) retrieverDAO.getModelById(userIdFrom, User.class);

                        if(userFrom != null) {
                            usUserIdFrom = userFrom.getUs_user_id();
                        }
                    }

                    if(lasisUserList2.size() > 0){
                        for (Map lasisUser : (List<Map>) lasisUserList2) {
                            if(lasisUser.get("US_ID")!= null){
                                userIdTo =  lasisUser.get("US_ID").toString();
                            }
                        }
                        userTo = (User) retrieverDAO.getModelById(userIdTo, User.class);
                        if(userTo != null) {
                            usUserIdTo = userTo.getUs_user_id();
                        }
                    }

                    JobDetailModel jobModel = new JobDetailModel();
                    jobModel = (JobDetailModel) retrieverDAO.getModelByCode("USJ_DIV || USJ_YEAR || USJ_SEQ", divId + jobNumber, new JobDetailModel());

                    if(jobModel != null) {
                        switch (jobAction) {
                            case SystemConstants.SCS_JP3_PUBCODE.ASSIGN_PLAN_EXAMINATION:
//                                wf_rtn = wfApi.startUSJ003_03(jobModel.getJob_id(), usUserIdFrom, UtimapsAction.USER_GROUP.TA, usUserIdTo);
                                wf_rtn = "success";
                                break;
                            case SystemConstants.SCS_JP3_PUBCODE.JOB_REGISTERED:
//                                wf_rtn = wfApi.startUSJ003_05(jobModel.getJob_id(), usUserIdFrom, "");
                                wf_rtn = "success";
                                break;
                            case SystemConstants.SCS_JP3_PUBCODE.COMPUTATION_APPROVED:
//                                wf_rtn = wfApi.startUSJ003_06(jobModel.getJob_id(), usUserIdFrom, "");
                                wf_rtn = "success";
                                break;
                            default:
                                wf_rtn = "failed";
                                break;
                        }

                        if (wf_rtn.equals("success")) {
                            Transaction transaction = retrieverDAO.getSession().beginTransaction();

                            try {
                                String sql2 = "UPDATE UTIJOBPROG SET STATUS = 'Y' WHERE JOB_NUMBER = ? and DIV_ID = ?";
                                Query query2 = retrieverDAO.getSession().createSQLQuery(sql2);
                                query2.setString(1, jobNumber);
                                query2.setString(2, divId);
                                query2.executeUpdate();
                                transaction.commit();

                            } catch (Exception e) {
                                transaction.rollback();
                                e.printStackTrace();
                                CommonFunction.writeLogFile(e.getStackTrace(), "UtimapsTestApi", "UtimapsTestApi", "updateUTIJOBPROG"); // Re-throw the exception for handling
                            } finally {

                            }
                            new UtimapsAction().auditAction(jobModel.getJob_id(), retrieverDAO.getSession(), usUserIdFrom, "UTIJOBPROG : Job Number " + jobNumber + " For Action Code : (" + jobAction + ") success");
                        } else {
                            new UtimapsAction().auditAction(jobModel.getJob_id(), retrieverDAO.getSession(), usUserIdFrom, "UTIJOBPROG : Job Number " + jobNumber + " For Action Code : (" + jobAction + ") failed");
                        }
                    }
                }
            }
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "UtimapsTestApi", "UtimapsTestApi", "checkUtiJobProg");
        } finally {
            retrieverDAO.closeSession();
        }
        
        return "";
    }
}
