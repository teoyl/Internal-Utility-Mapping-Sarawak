/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web.dao;

import com.SysConf;
import com.digicert.PDFSign;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxg.common.model.PublicUserModel;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.common.util.FileOperationUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.FtpInterface;
import com.sains.common.util.HttpsUrlUtil;
import com.sains.common.util.OBSUtil;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.SystemConstants.FILE_TYPE;
import com.sains.common.util.SystemConstants.SCS_JP3_PUBCODE;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.EmailTrigger;
import com.sains.framework.base.ModelBase;
import com.sains.framework.base.ReportGenerator;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.model.User;
import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import com.sains.workflow.web.WorkflowApiAction;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.ChecklistItemModel;
import com.utimaps.model.ChecklistItemSetupModel;
import com.utimaps.model.ChecklistModel;
import com.utimaps.model.ChecklistSetupModel;
import com.utimaps.model.FileModel;
import com.utimaps.model.JobDetailModel;
import com.utimaps.model.NotificationPModel;
import com.utimaps.model.SurveyFirmPModel;
import com.utimaps.web.UtimapsAction;
import com.utimaps.web.UtimapsAction.CL_DECISION;
import com.utimaps.web.UtimapsAction.EMAIL_CODE;
import com.utimaps.web.UtimapsAction.PB_STATUS;
import com.utimaps.web.UtimapsAction.USER_GROUP;
import com.utimaps.web.UtimapsAction.WF_STATUS;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author yonglai
 */
public class SubmissionJobDAOImpl extends BaseDAOImpl<JobDetailModel> implements SubmissionJobDAO {
    
    @Override
    public JobDetailModel updateApplication(JobDetailModel model) throws Exception {
        
        BaseDAOImpl dao = new BaseDAOImpl();
        try {
            Debug.printDebug("runnning updateApplication daoimpl~~~~~");
            dao.setSession(getSession());
            beginBatchTransaction();
            if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_UPDATE) ||
                model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_UPDATE_U20)) {
                ApplicationPModel appModel = new ApplicationPModel();
                appModel = (ApplicationPModel) dao.getModelById(model.getCase_id(), ApplicationPModel.class);
                appModel.updatableColumns = new String[]{"case_id", "usj_providers"};
                appModel.setUsj_providers(model.getApplicationModel().getUsj_providers());
                getSession().update(appModel);
            }
            
            if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_UPDATE)) {
                saveOrUpdateChecklist(model, model.getU10ChecklistModel(), model.getU10ChecklistSetupModel(), "1", "N");
                saveOrUpdateChecklist(model, model.getU10ChecklistModel(), model.getU20ChecklistSetupModel(), "1", "N");
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_UPDATE_U20)) {
                saveOrUpdateChecklist(model, model.getU20ChecklistModel(), model.getU20ChecklistSetupModel(), "1", "N");
            }
            
            
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "updateApplication");
        } finally {
            dao.closeAllSession();
            closeSession();
        }
        return model;
    }
    
    @Override
    public JobDetailModel updateSubmission(JobDetailModel model) throws Exception {
        
        BaseDAOImpl dao = new BaseDAOImpl();
        try {
            Debug.printDebug("runnning updateSubmission daoimpl~~~~~ " + model.get_operation());
            if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_UPDATE)) {
                saveOrUpdateChecklist(model, model.getU10ChecklistModel(), model.getU10ChecklistSetupModel(), "2", "N");
                saveOrUpdateChecklist(model, model.getU10ChecklistModel(), model.getU20ChecklistSetupModel(), "2", "N");
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_UPDATE_HARDCOPY)) {
                Debug.printDebug("model.getU11ChecklistSetupModel() " + model.getU11ChecklistSetupModel());
                Debug.printDebug("model.getU21ChecklistSetupModel() " + model.getU21ChecklistSetupModel());
                saveOrUpdateChecklist(model, model.getU21ChecklistModel(), model.getU11ChecklistSetupModel(), "2", "N");
                saveOrUpdateChecklist(model, model.getU21ChecklistModel(), model.getU21ChecklistSetupModel(), "2", "N");
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_UPDATE_U30)) {
                saveOrUpdateChecklist(model, model.getU30ChecklistModel(), model.getU30ChecklistSetupModel(), "2", "N");
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_UPDATE_U40)) {
                Debug.printDebug("model.getU40ChecklistSetupModel() " + model.getU40ChecklistSetupModel());
                Debug.printDebug("model.getU40ChecklistModel() " + model.getU40ChecklistModel());
                saveOrUpdateChecklist(model, model.getU40ChecklistModel(), model.getU40ChecklistSetupModel(), "2", "N");
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_UPDATE_U50)) {
                saveOrUpdateChecklist(model, model.getU50ChecklistModel(), model.getU50ChecklistSetupModel(), "2", "N");
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_UPDATE_U60)) {
                saveOrUpdateChecklist(model, model.getU60ChecklistModel(), model.getU60ChecklistSetupModel(), "2", "N");
            }
            
            dao.setSession(getSession());
            beginBatchTransaction();
                
            if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_START_HARDCOPY)) {
                model.updatableColumns = new String[]{"job_id, wf_status"};
                model = (JobDetailModel) dao.setUpdateProperties(model, dao.getSession());
                model.setWf_status_2(WF_STATUS.CHECK_HARDCOPY_SUBMISSION_PENDING);
                dao.getSession().update(model);
                
                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                request.setAttribute("ignoreCsrfCheck", "true");
                new WorkflowApiAction().insertProcessingHistory(dao.getSession(), model.getJob_id(), "", model.getWf_status_2(), "");
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_UPDATE_HARDCOPY)) {
                model.updatableColumns = new String[]{"job_id", "hardcopy_received_date"};
                model = (JobDetailModel) dao.setUpdateProperties(model, dao.getSession());
                dao.getSession().update(model);
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_UPDATE)) {
                
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PB_UPDATE_NEW)) {                
                model.updatableColumns = new String[]{"job_id", "usj_date", "us_so_id"};
                model = (JobDetailModel) dao.setUpdateProperties(model, dao.getSession());
                dao.getSession().update(model);
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PB_UPDATE_LIST)) {
                model.updatableColumns = new String[]{"job_id", "ht_datum", "ht_reference"};
                model = (JobDetailModel) dao.setUpdateProperties(model, dao.getSession());
                dao.getSession().update(model);
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PB_UPDATE_CONTROL)) {
                model.updatableColumns = new String[]{"job_id", "control_sv_flag", "precheck_stage"};
                model = (JobDetailModel) dao.setUpdateProperties(model, dao.getSession());
                dao.getSession().update(model);
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PB_UPDATE_HC_ACK)) {
                model.updatableColumns = new String[]{"job_id", "usj_status", "ack_sub_hardcopy", "hardcopy_received_date"};
                model = (JobDetailModel) dao.setUpdateProperties(model, dao.getSession());
                dao.getSession().update(model);
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PB_UPDATE_PRECHECK_STAGE)) {
                model.updatableColumns = new String[]{"job_id", "precheck_stage"};
                model = (JobDetailModel) dao.setUpdateProperties(model, dao.getSession());
                dao.getSession().update(model);
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PB_UPDATE_TRAV_START)) {
                model.updatableColumns = new String[]{"job_id", "trav_pc_start_date"};
                model = (JobDetailModel) dao.setUpdateProperties(model, dao.getSession());
                dao.getSession().update(model);
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PB_UPDATE_TRAV_COMPLETE)) {
                model.updatableColumns = new String[]{"job_id", "trav_pc_completed_date"};
                model = (JobDetailModel) dao.setUpdateProperties(model, dao.getSession());
                dao.getSession().update(model);
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_QUERY_PRECHECK)) {
                new WorkflowApiAction().insertProcessingHistoryBackend(dao.getSession(), model.getJob_id(), "", WF_STATUS.RO_PRECHECK_FAILED, "");
                
                model.updatableColumns = new String[]{"job_id", "usj_status", "wf_status_2", "precheck_stage"};
                model.setUsj_status(PB_STATUS.SUBMISSION_QUERY_PRECHECK);
                model.setWf_status_2(WF_STATUS.RO_PRECHECK_FAILED);
                model.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL); // [PRECHECK] 26092024 RO CHECKING FAILURE - SET PRECHECK TO RUN UTIL(GIS) PRECHECK ONLY
                model = (JobDetailModel) dao.setUpdateProperties(model, dao.getSession());
                dao.getSession().update(model);
                
                sendEmailROQuery(dao.getSession(), model);
                
                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                request.setAttribute("ignoreCsrfCheck", "true");
                new WorkflowApiAction().insertProcessingHistoryBackend(dao.getSession(), model.getJob_id(), "", WF_STATUS.SUBMISSION_QUERY_PRECHECK, "");
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_UPDATE_U60)) {
                ApplicationPModel appModel = new ApplicationPModel();
                appModel = (ApplicationPModel) dao.getModelById(model.getCase_id(), ApplicationPModel.class);
                appModel.updatableColumns = new String[]{"case_id", "usj_providers"};
                appModel.setUsj_providers(model.getApplicationModel().getUsj_providers());
                dao.getSession().update(appModel);
            }
            
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "updateSubmission");
        } finally {
            dao.closeAllSession();
            closeSession();
        }
        return model;
    }
    
    @Override
    public String completeApplication(JobDetailModel model) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        String completingResult = "success";
        
        try {
            Debug.printDebug("runnning complete task daoimpl~~~~~ " + model.getWf_status() + " :: " + model.get_operation());
            Map sessionMap = ActionContext.getContext().getSession();
            
            if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_COMPLETE)) {
                if (model.getWf_status().equals(WF_STATUS.CHECK_U10_PENDING)) {
                    saveOrUpdateChecklist(model, model.getU10ChecklistModel(), model.getU10ChecklistSetupModel(), "1", "N");
                    saveOrUpdateChecklist(model, model.getU10ChecklistModel(), model.getU20ChecklistSetupModel(), "1", "N");
                    
                    ChecklistModel caseChecklistModel = new ChecklistModel();
                    caseChecklistModel = model.getU10ChecklistModel();
                    Debug.printDebug("checklist decision " + caseChecklistModel.getCheck_status());
                    
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.CHECK_U10_PENDING, WF_STATUS.CHECK_U10_APPROVED, WF_STATUS.VERIFY_U10_PENDING, "");
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    if (result.equalsIgnoreCase("success")) {
                        
                    }
                } else if (model.getWf_status().equals(WF_STATUS.VERIFY_U10_PENDING)) {
                    saveOrUpdateChecklist(model, model.getU10ChecklistModel(), model.getU10ChecklistSetupModel(), "1", "Y");
                    saveOrUpdateChecklist(model, model.getU10ChecklistModel(), model.getU20ChecklistSetupModel(), "1", "Y");
                    
                    ChecklistModel caseChecklistModel = new ChecklistModel();
                    caseChecklistModel = model.getU10ChecklistModel();
                    Debug.printDebug("checklist decision " + caseChecklistModel.getCheck_status());
                    User user = new User();
                    user = (User) dao.getModelById(sessionMap.get("userId").toString(), User.class);
                    
                    if (caseChecklistModel.getCheck_status().equals(CL_DECISION.ACCEPT)) {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Map jsonMap = wfApi.completeTask_SUB(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.VERIFY_U10_PENDING, WF_STATUS.VERIFY_U10_APPROVED, WF_STATUS.ISSUE_USCS20_PENDING, user.getUs_user_id());
                        
                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        if (result.equalsIgnoreCase("success")) {
                            
                        }
                    } else if (caseChecklistModel.getCheck_status().equals(CL_DECISION.REJECT)) {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Map jsonMap = wfApi.completeTask_SUB(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "N", WF_STATUS.VERIFY_U10_PENDING, WF_STATUS.VERIFY_U10_QUERY, WF_STATUS.ISSUE_USCS10_PENDING, user.getUs_user_id());
                        
                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        if (result.equalsIgnoreCase("success")) {
                            
                        }
                    }
                } else if (model.getWf_status().equals(WF_STATUS.CHECK_U20_PENDING)) {
                    saveOrUpdateChecklist(model, model.getU20ChecklistModel(), model.getU20ChecklistSetupModel(), "1", "N");
                    
                    ChecklistModel caseChecklistModel = new ChecklistModel();
                    caseChecklistModel = model.getU20ChecklistModel();
                    Debug.printDebug("checklist decision " + caseChecklistModel.getCheck_status());
                    
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.CHECK_U20_PENDING, WF_STATUS.CHECK_U20_APPROVED, WF_STATUS.VERIFY_U20_PENDING, "");
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    if (result.equalsIgnoreCase("success")) {
                        
                    }
                } else if (model.getWf_status().equals(WF_STATUS.VERIFY_U20_PENDING)) {
                    saveOrUpdateChecklist(model, model.getU20ChecklistModel(), model.getU20ChecklistSetupModel(), "1", "Y");
                    
                    ChecklistModel caseChecklistModel = new ChecklistModel();
                    caseChecklistModel = model.getU20ChecklistModel();
                    Debug.printDebug("checklist decision " + caseChecklistModel.getCheck_status());
                    User user = new User();
                    user = (User) dao.getModelById(sessionMap.get("userId").toString(), User.class);
                    
                    if (caseChecklistModel.getCheck_status().equals(CL_DECISION.ACCEPT)) {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Map jsonMap = wfApi.completeTask_SUB(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.VERIFY_U20_PENDING, WF_STATUS.VERIFY_U20_APPROVED, WF_STATUS.ISSUE_USCS20_PENDING, user.getUs_user_id());
                        
                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        if (result.equalsIgnoreCase("success")) {
                            
                        }
                    } else if (caseChecklistModel.getCheck_status().equals(CL_DECISION.REJECT)) {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Map jsonMap = wfApi.completeTask_SUB(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "N", WF_STATUS.VERIFY_U20_PENDING, WF_STATUS.VERIFY_U20_QUERY, WF_STATUS.ISSUE_USCS10_PENDING, user.getUs_user_id());
                        
                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        if (result.equalsIgnoreCase("success")) {
                            
                        }
                    }
                } else if (model.getWf_status().equals(WF_STATUS.ISSUE_USCS10_PENDING)) {
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Debug.printDebug("model.getJob_id() " + model.getJob_id());
                    Map jsonMap = wfApi.completeTask_SUB(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.ISSUE_USCS10_PENDING, WF_STATUS.ISSUE_USCS10_COMPLETED, WF_STATUS.ISSUE_USCS10_COMPLETED, "");
                    
                    String result = jsonMap.get("status").toString();
                    completingResult = result;
                    
                    if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                        generateChecklistFile(model.getJob_id(), FILE_TYPE.U20);
                    } else {
                        generateChecklistFile(model.getJob_id(), FILE_TYPE.U10);
                        generateChecklistFile(model.getJob_id(), FILE_TYPE.U20);
                    }
                    
                    if (result.equalsIgnoreCase("success")) {
                        // To send email upon complete job
                        Map param = new HashMap();
                        JobDetailModel jobmodel = (JobDetailModel) dao.getModelById(model.getJob_id(), JobDetailModel.class);
                        param.put("strUsjNo", jobmodel.getUsj_no());
                        param.put("submitDueDate", getLetterSignedDate(dao.getSession(), jobmodel.getJob_id(), FILE_TYPE.USCS10));
                        sendEmailUponCompletion(dao.getSession(), model, EMAIL_CODE.USCS10, FILE_TYPE.USCS10, param);
                        
                        updateLetterVersion(model, FILE_TYPE.USCS10);
                    }
                    
                } else if (model.getWf_status().equals(WF_STATUS.ISSUE_USCS20_PENDING)) {
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.ISSUE_USCS20_PENDING, WF_STATUS.ISSUE_USCS20_COMPLETED, WF_STATUS.ISSUE_USCS20_COMPLETED, "");
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                        generateChecklistFile(model.getJob_id(), FILE_TYPE.U20);
                    } else {
                        generateChecklistFile(model.getJob_id(), FILE_TYPE.U10);
                        generateChecklistFile(model.getJob_id(), FILE_TYPE.U20);
                    }
                    
                    if (result.equalsIgnoreCase("success")) {
                        // To send email upon complete job
                        Map param = new HashMap();
                        JobDetailModel jobmodel = (JobDetailModel) dao.getModelById(model.getJob_id(), JobDetailModel.class);
                        param.put("strUsjNo", jobmodel.getUsj_no());
                        sendEmailUponCompletion(dao.getSession(), model, EMAIL_CODE.USCS20, FILE_TYPE.USCS20, param);
                        
                        updateLetterVersion(model, FILE_TYPE.USCS20);
                    }
                }
            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_ROUTE_BACK)) {
            
                if (model.getWf_status().equals(WF_STATUS.VERIFY_U10_PENDING)) {
                    saveOrUpdateChecklist(model, model.getU10ChecklistModel(), model.getU10ChecklistSetupModel(), "1", "Y");
                    saveOrUpdateChecklist(model, model.getU10ChecklistModel(), model.getU20ChecklistSetupModel(), "1", "Y");
                    
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "B", WF_STATUS.VERIFY_U10_PENDING, WF_STATUS.VERIFY_U10_ROUTE_BACK, WF_STATUS.CHECK_U10_PENDING, "");
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    if (result.equalsIgnoreCase("success")) {
                        
                    }
                } else if (model.getWf_status().equals(WF_STATUS.VERIFY_U20_PENDING)) {
                    saveOrUpdateChecklist(model, model.getU20ChecklistModel(), model.getU20ChecklistSetupModel(), "1", "Y");
                    
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "B", WF_STATUS.VERIFY_U20_PENDING, WF_STATUS.VERIFY_U20_ROUTE_BACK, WF_STATUS.CHECK_U20_PENDING, "");
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    if (result.equalsIgnoreCase("success")) {
                        
                    }
                }

            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_ROUTE_BACK_2)) {
                if (model.getWf_status_2().equals(WF_STATUS.CHECK_U60_SD_PENDING)) {
                    saveOrUpdateChecklist(model, model.getU60ChecklistModel(), model.getU60ChecklistSetupModel(), "2", "Y");

                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "B", WF_STATUS.CHECK_U60_SD_PENDING, WF_STATUS.CHECK_U60_SD_ROUTE_BACK, WF_STATUS.CHECK_U60_PENDING, "", "");

                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;

                    if (result.equalsIgnoreCase("success")) {

                    }
                } else if (model.getWf_status_2().equals(WF_STATUS.CHECK_U60_SS_PENDING)) {
                    saveOrUpdateChecklist(model, model.getU60ChecklistModel(), model.getU60ChecklistSetupModel(), "2", "Y");

                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "B", WF_STATUS.CHECK_U60_SS_PENDING, WF_STATUS.CHECK_U60_SS_ROUTE_BACK, WF_STATUS.CHECK_U60_SD_PENDING, "", "");

                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;

                    if (result.equalsIgnoreCase("success")) {

                    }
                }

            } else if (model.get_operation().equals(JobDetailModel.OPERATION.PROCESS_COMPLETE_SUBMISSION)) {
                Debug.printDebug("model.getWf_status_2() " + model.getWf_status_2());
                if (model.getWf_status_2().equals(WF_STATUS.CHECK_HARDCOPY_SUBMISSION_PENDING)) {
                    Debug.printDebug("model.getCheck_status() " + model.getCheck_status());
                    saveOrUpdateChecklist(model, model.getU21ChecklistModel(), model.getU11ChecklistSetupModel(), "2", "Y");
                    saveOrUpdateChecklist(model, model.getU21ChecklistModel(), model.getU21ChecklistSetupModel(), "2", "Y");
                    
                    ChecklistModel caseChecklistModel = new ChecklistModel();
                    caseChecklistModel = model.getU21ChecklistModel();
                    Debug.printDebug("checklist decision " + caseChecklistModel.getCheck_status());
                    
                    if (caseChecklistModel.getCheck_status().equals("A")) {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Debug.printDebug("model.get_taskId() " + model.get_taskId());
                        Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.CHECK_HARDCOPY_SUBMISSION_START, WF_STATUS.CHECK_HARDCOPY_SUBMISSION_APPROVED, WF_STATUS.ISSUE_USCS30_PENDING, "", USER_GROUP.SS);
                        
                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        if (result.equalsIgnoreCase("success")) {
                            try {
                                dao.setSession(getSession());
                                beginBatchTransaction();
                                
                                model.updatableColumns = new String[]{"job_id", "hardcopy_received_date"};
                                model = (JobDetailModel) dao.setUpdateProperties(model, dao.getSession());
                                dao.getSession().update(model);
                                
                                commitBatchTransaction();
                                
                            } catch (Exception e) {
                                rollbackBatchTransaction();
                                e.printStackTrace();
                                CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "completeApplication");
                            } finally {
                                dao.closeSession();
                            }
                        }
                    } else {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Debug.printDebug("model.get_taskId() " + model.get_taskId());
                        Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "N", WF_STATUS.CHECK_HARDCOPY_SUBMISSION_START, WF_STATUS.CHECK_HARDCOPY_SUBMISSION_QUERY, WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING, "", USER_GROUP.SS);
                        
                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        if (result.equalsIgnoreCase("success")) {
                            
                        }
                    }
                    
                } else if (model.getWf_status_2().equals(WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING)) {
                    
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING, WF_STATUS.ISSUE_USCS10_HARDCOPY_COMPLETED, WF_STATUS.SUBMISSION_DRAFT, "", "");
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    generateChecklistFile(model.getJob_id(), FILE_TYPE.U21);
                    
                    if (result.equalsIgnoreCase("success")) {
                        // To send email upon complete job
                        Map param = new HashMap();
                        JobDetailModel jobmodel = (JobDetailModel) dao.getModelById(model.getJob_id(), JobDetailModel.class);
                        param.put("strUsjNo", jobmodel.getUsj_no());
                        param.put("submitDueDate", getLetterSignedDate(dao.getSession(), jobmodel.getJob_id(), FILE_TYPE.USCS10H));
                        sendEmailUponCompletion(dao.getSession(), model, EMAIL_CODE.USCS10, FILE_TYPE.USCS10H, param);
                        
                        updateLetterVersion(model, FILE_TYPE.USCS10H);
                    }
                    
                } else if (model.getWf_status_2().equals(WF_STATUS.ISSUE_USCS30_PENDING)) {
                    
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    String wf_rtn = "";
                    Debug.printDebug("model.get_taskId() " + model.get_taskId() + "model.getControl_sv_flag " + model.getControl_sv_flag());
                    Map jsonMap = new HashMap();
                    String passresult = "" ;
                    String taskAssignTo = "";
                    String idAssignTo = "";
                    
                    if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                        passresult = "postsuccess";
                    } else {
                        if (Validator.isEmpty(model.getComp_completed()) || model.getComp_completed().equals("N") || model.getComp_completed().equals("E")) {
                            taskAssignTo = wfApi.checkPreviousTaskDoer(model.getCase_id(), model.getJob_id(), USER_GROUP.TA);
                            if(Validator.isEmpty(taskAssignTo)) {
                                Map getInforMap = getInfo(model.getJob_id(), "rrUser", "USJ_TA_" + model.getUsj_div());
                                Map ifOnlyOneUserGroupMap = (Map) getInforMap.get("user");
                                String userId = (String) ifOnlyOneUserGroupMap.get("userId");

                                User user = (User) dao.getModelByCode("us_user_id", userId, new User());
                                if(user != null) {
                                    idAssignTo = user.getUs_id();
                                }
                            } 

                            if(!Validator.isEmpty(idAssignTo)) {
                                if(SystemConstants.ENV.equals("1")) {
                                    passresult = startCompJob(getSession(), model, sessionMap, idAssignTo);
                                } else {
                                    passresult = "postsuccess";
                                }
                            } else {
                                passresult = "assignfail";
                            }
                        } else {
                            passresult = "postsuccess";
                        }
                    }
                    
                    Debug.printDebug("passresult " + passresult);
                    
                    if(passresult.equals("postsuccess")) {
                        if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                            jsonMap = wfApi.completeTask_AC(getSession(), model.getJob_id(), "USJ004_02_04", WF_STATUS.ISSUE_USCS30_PENDING, WF_STATUS.ISSUE_USCS30_COMPLETED, WF_STATUS.CHECK_U50_PENDING, "_completeValue::=Y");
                        } else {
                            if (Validator.isEmpty(model.getComp_completed()) || model.getComp_completed().equals("N")) {
                                jsonMap = wfApi.completeTask_AC(getSession(), model.getJob_id(), "USJ003_02_06", WF_STATUS.ISSUE_USCS30_PENDING, WF_STATUS.ISSUE_USCS30_COMPLETED, WF_STATUS.TA_CHECK_U30_PENDING, "_completeValue::=Y");
                            } else if (model.getComp_completed().equals("E")) {
                                jsonMap = wfApi.completeTask_AC(getSession(), model.getJob_id(), "USJ003_02_06", WF_STATUS.ISSUE_USCS30_PENDING, WF_STATUS.ISSUE_USCS30_COMPLETED, WF_STATUS.TA_CHECK_U30_PENDING, "_completeValue::=Y");
                            } else {
                                jsonMap = wfApi.completeTask_AC(getSession(), model.getJob_id(), "USJ004_02_04", WF_STATUS.ISSUE_USCS30_PENDING, WF_STATUS.ISSUE_USCS30_COMPLETED, WF_STATUS.CHECK_U50_PENDING, "_completeValue::=Y");
                            }
                        }

                        Debug.printDebug("jsonMap " + jsonMap);
                        String result = jsonMap.get("status").toString();
                        completingResult = result;
                        
                        generateChecklistFile(model.getJob_id(), FILE_TYPE.U21);

                        if (result.equalsIgnoreCase("success")) {
                            // To send email upon complete job
                            Map param = new HashMap();
                            JobDetailModel jobmodel = (JobDetailModel) dao.getModelById(model.getJob_id(), JobDetailModel.class);
                            param.put("strUsjNo", jobmodel.getUsj_no());
                            param.put("hardcopyDate", jobmodel.getHardcopy_received_date_str());
                            sendEmailUponCompletion(dao.getSession(), model, EMAIL_CODE.USCS30, FILE_TYPE.USCS30, param);

                            updateLetterVersion(model, FILE_TYPE.USCS30);

                            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {

                                // 3-09-2024 :: To start next workflow here as using workflow script method cannot assign user
                                wf_rtn = wfApi.startUSJ003_07(model.getJob_id(), "BACKEND");
                                String msg = "";
                                if (wf_rtn.equals("success")) {
                                    msg = "Successfully trigger workflow startUSJ003_07! Job Id is " + model.getJob_id() + ", job No is " + jobmodel.getUsj_no();
                                } else {
                                    msg = "failed";
                                }

                                Debug.printDebug("msg " + msg);
                            } else {
                                String msg = "";
                                if (Validator.isEmpty(model.getComp_completed()) || model.getComp_completed().equals("N")) {
                                    // 3-09-2024 :: To start next workflow here as using workflow script method cannot assign user
                                    wf_rtn = wfApi.startUSJ003_03(model.getJob_id(), "BACKEND", USER_GROUP.TA, taskAssignTo);
                                    if (wf_rtn.equals("success")) {
                                        msg = "Successfully trigger workflow USJ003_03! Job Id is " + model.getJob_id() + ", job No is " + jobmodel.getUsj_no();
                                    } else {
                                        msg = "failed";
                                    }
                                } else {
                                    // 3-09-2024 :: To start next workflow here as using workflow script method cannot assign user
                                    wf_rtn = wfApi.startUSJ003_07(model.getJob_id(), "BACKEND");
                                    if (wf_rtn.equals("success")) {
                                        msg = "Successfully trigger workflow startUSJ003_07! Job Id is " + model.getJob_id() + ", job No is " + jobmodel.getUsj_no();
                                    } else {
                                        msg = "failed";
                                    }
                                }

                                Debug.printDebug("msg " + msg);
                            }
                        }
                    } else if (passresult.equals("postfail")){
                        completingResult = "postfail";
                    }
                    
                } else if (model.getWf_status_2().equals(WF_STATUS.TA_CHECK_U30_PENDING)) {
                    saveOrUpdateChecklist(model, model.getU30ChecklistModel(), model.getU30ChecklistSetupModel(), "2", "Y");
                    
                    ChecklistModel caseChecklistModel = new ChecklistModel();
                    caseChecklistModel = model.getU30ChecklistModel();
                    Debug.printDebug("checklist decision " + caseChecklistModel.getCheck_status());
                    
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB2(getSession(),model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.TA_CHECK_U30_PENDING, WF_STATUS.TA_CHECK_U30_APPROVED, WF_STATUS.STA_CHECK_U30_PENDING, "",USER_GROUP.STA);

                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    if (result.equalsIgnoreCase("success")) {
                        
                    }
                    
                } else if (model.getWf_status_2().equals(WF_STATUS.STA_CHECK_U30_PENDING)) {
                    saveOrUpdateChecklist(model, model.getU30ChecklistModel(), model.getU30ChecklistSetupModel(), "2", "Y");
                    
                    ChecklistModel caseChecklistModel = new ChecklistModel();
                    caseChecklistModel = model.getU30ChecklistModel();
                    Debug.printDebug("checklist decision " + caseChecklistModel.getCheck_status());
                    
                    if (caseChecklistModel.getCheck_status().equals("A")) {
                        generateChecklistFile(model.getJob_id(), FILE_TYPE.U30);
                        
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Debug.printDebug("model.get_taskId() " + model.get_taskId());
                        Map jsonMap = wfApi.completeTask_SUB2(getSession(),model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.STA_CHECK_U30_PENDING, WF_STATUS.STA_CHECK_U30_APPROVED, WF_STATUS.STA_CHECK_U30_APPROVED, "",USER_GROUP.STA);

                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        if (result.equalsIgnoreCase("success")) {
                            
                        }
                    } else if (caseChecklistModel.getCheck_status().equals("R")) {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Debug.printDebug("model.get_taskId() " + model.get_taskId());
                        Map jsonMap = wfApi.completeTask_SUB2(getSession(),model.get_taskId(), model.getJob_id(), model.getCase_id(), "N", WF_STATUS.STA_CHECK_U30_PENDING, WF_STATUS.STA_CHECK_U30_QUERY, WF_STATUS.ISSUE_USCS40_PENDING, "",USER_GROUP.SS);

                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        if (result.equalsIgnoreCase("success")) {
                            
                        }
                    }
                    
                } else if (model.getWf_status_2().equals(WF_STATUS.ISSUE_USCS40_PENDING)) {
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.ISSUE_USCS40_PENDING, WF_STATUS.ISSUE_USCS40_COMPLETED, WF_STATUS.SUBMISSION_DRAFT, "", "");
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    generateChecklistFile(model.getJob_id(), FILE_TYPE.U30);
                    
                    if (result.equalsIgnoreCase("success")) {
                        // To send email upon complete job
                        Map param = new HashMap();
                        JobDetailModel jobmodel = (JobDetailModel) dao.getModelById(model.getJob_id(), JobDetailModel.class);
                        param.put("strUsjNo", jobmodel.getUsj_no());
                        param.put("submitDueDate", getLetterSignedDate(dao.getSession(), jobmodel.getJob_id(), FILE_TYPE.USCS40));
                        sendEmailUponCompletion(dao.getSession(), model, EMAIL_CODE.USCS40, FILE_TYPE.USCS40, param);
                        
                        updateLetterVersion(model, FILE_TYPE.USCS40);

                        // 09092024 aiman - revoke all psdsp on query
                        // revert - BASA mention no need dsp to re-sing for USCS40
//                        try {
//                            PbUtimapsApiAction pbAction = new PbUtimapsApiAction();
//                            pbAction.setReqCaseId(jobmodel.getJob_id());
//                            pbAction.pbRevokeDigitalPlans();
//                            pbAction.setReqJobId(jobmodel.getJob_id());
//                            pbAction.pbRevokeDigitalPlansNoRes();
//                        } catch (Exception e) {
//                            e.printStackTrace();                           
//                            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "completeApplication");
//                        }
                    }
                } else if (model.getWf_status_2().equals(WF_STATUS.CHECK_U40_PENDING)) {
                    
                    saveOrUpdateChecklist(model, model.getU40ChecklistModel(), model.getU40ChecklistSetupModel(), "2", "Y");
                    
                    ChecklistModel caseChecklistModel = new ChecklistModel();
                    caseChecklistModel = model.getU40ChecklistModel();
                    Debug.printDebug("checklist decision " + caseChecklistModel.getCheck_status());
                    
                    if (caseChecklistModel.getCheck_status().equals("A")) {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Debug.printDebug("model.get_taskId() " + model.get_taskId());
                        Map jsonMap = wfApi.completeTask_SUB2(getSession(),model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.CHECK_U40_PENDING, WF_STATUS.CHECK_U40_APPROVED, WF_STATUS.ISSUE_USCS70_PENDING, "",USER_GROUP.STA);

                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        generateChecklistFile(model.getJob_id(), FILE_TYPE.U40);
                        
                        if (result.equalsIgnoreCase("success")) {
                            
                        }
                    } else {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Debug.printDebug("model.get_taskId() " + model.get_taskId());
                        Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "N", WF_STATUS.CHECK_U40_PENDING, WF_STATUS.CHECK_U40_QUERY, WF_STATUS.ISSUE_USCS50_PENDING, "", USER_GROUP.SS);
                        
                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        if (result.equalsIgnoreCase("success")) {
                            
                        }
                    }
                } else if (model.getWf_status_2().equals(WF_STATUS.ISSUE_USCS50_PENDING)) {
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "N", WF_STATUS.ISSUE_USCS50_PENDING, WF_STATUS.ISSUE_USCS50_COMPLETED, WF_STATUS.SUBMISSION_DRAFT, "", "");
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    generateChecklistFile(model.getJob_id(), FILE_TYPE.U40);
                    
                    if (result.equalsIgnoreCase("success")) {
                        // To send email upon complete job
                        Map param = new HashMap();
                        JobDetailModel jobmodel = (JobDetailModel) dao.getModelById(model.getJob_id(), JobDetailModel.class);
                        param.put("strUsjNo", jobmodel.getUsj_no());
                        param.put("submitDueDate", getLetterSignedDate(dao.getSession(), jobmodel.getJob_id(), FILE_TYPE.USCS50));
                        sendEmailUponCompletion(dao.getSession(), model, EMAIL_CODE.USCS50, FILE_TYPE.USCS50, param);
                        
                        updateLetterVersion(model, FILE_TYPE.USCS50);
                    }
                    
                } else if (model.getWf_status_2().equals(WF_STATUS.ISSUE_USCS60_PENDING)) {
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "N", WF_STATUS.ISSUE_USCS60_PENDING, WF_STATUS.ISSUE_USCS60_COMPLETED, "", "", "");
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    if (result.equalsIgnoreCase("success")) {
                        // To send email upon complete job
                        Map param = new HashMap();
                        JobDetailModel jobmodel = (JobDetailModel) dao.getModelById(model.getJob_id(), JobDetailModel.class);
                        param.put("strUsjNo", jobmodel.getUsj_no());
                        sendEmailUponCompletion(dao.getSession(), model, EMAIL_CODE.USCS60, FILE_TYPE.USCS60, param);
                        
                        updateLetterVersion(model, FILE_TYPE.USCS60);
                    }
                    
                } else if (model.getWf_status_2().equals(WF_STATUS.ISSUE_USCS70_PENDING)) {
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.ISSUE_USCS70_PENDING, WF_STATUS.ISSUE_USCS70_COMPLETED, WF_STATUS.RO_PRECHECK_PENDING, "", "");
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    String wf_rtn = "";
                    
                    if (result.equalsIgnoreCase("success")) {
                        // To send email upon complete job
                        Map param = new HashMap();
                        JobDetailModel jobmodel = (JobDetailModel) dao.getModelById(model.getJob_id(), JobDetailModel.class);
                        param.put("strUsjNo", jobmodel.getUsj_no());
                        param.put("approvedDate", jobmodel.getComp_approved_date_str());
                        sendEmailUponCompletion(dao.getSession(), model, EMAIL_CODE.USCS70, FILE_TYPE.USCS70, param);
                        
                        updateLetterVersion(model, FILE_TYPE.USCS70);

                        // to call ro precheck upon completion
                        try {
                            String strReturn = "";
                            String usjNoFormatted = setupUsjNoFormattedString(jobmodel.getUsj_no());
                            String paramHashKey = setupHashParamKey();
                            Debug.printDebug("param key - " + paramHashKey);
                            String pcCallUrl = SystemConstants.DOMAIN.domain_gis_tnt + "uti_mapi/api/CallAndCheckPrecheck";
                            pcCallUrl += "?jobId=" + jobmodel.getJob_id();
                            pcCallUrl += "&usjNo=" + usjNoFormatted;
                            pcCallUrl += "&parameters=" + paramHashKey;
                            pcCallUrl += "&RO_status=" + "ORO";
                            strReturn = callPublicApiGet(pcCallUrl);
                            Debug.printDebug("strReturn " + strReturn);
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "completeApplication");
                        }

                        // 26-08-2024 :: Commented as moved to call workflow after precheck on RO done
//                        wf_rtn = wfApi.startUSJ003_07(model.getJob_id(), "BACKEND");
//                        String msg = "";
//                        if (wf_rtn.equals("success")) {
//                            msg = "Successfully trigger workflow USJ003_07! Job Id is " + model.getJob_id() + ", job No is " + jobmodel.getUsj_no();
//                        } else {
//                            msg = "failed";
//                        }
                    }
                } else if (model.getWf_status_2().equals(WF_STATUS.CHECK_U50_PENDING)) {
                    
                    saveOrUpdateChecklist(model, model.getU50ChecklistModel(), model.getU50ChecklistSetupModel(), "2", "Y");
                    
                    ChecklistModel caseChecklistModel = new ChecklistModel();
                    caseChecklistModel = model.getU50ChecklistModel();
                    Debug.printDebug("checklist decision " + caseChecklistModel.getCheck_status());
                    
                    if (caseChecklistModel.getCheck_status().equals("A")) {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Debug.printDebug("model.get_taskId() " + model.get_taskId());
                        Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.CHECK_U50_PENDING, WF_STATUS.CHECK_U50_APPROVED, WF_STATUS.ASSIGN_STA_FOR_CHECKING, "", USER_GROUP.SD);
                        
                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        generateChecklistFile(model.getJob_id(), FILE_TYPE.U50);
                        
                        if (result.equalsIgnoreCase("success")) {
                            // 25-03-2025 :: Spatial Checking after U50 approved
                            String wf_rtn = "";
                            String msg = "";
                            wf_rtn = wfApi.startUSJ005_01(model.getJob_id(), "BACKEND");
                            if (wf_rtn.equals("success")) {
                                msg = "Successfully trigger workflow USJ005_01! Job Id is " + model.getJob_id() + ", job No is " + model.getUsj_no();
                            } else {
                                msg = "failed";
                            }
                            
                        }
                    } else {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Debug.printDebug("model.get_taskId() " + model.get_taskId());
                        Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "N", WF_STATUS.CHECK_U50_PENDING, WF_STATUS.CHECK_U50_QUERY, WF_STATUS.ISSUE_USCS80_PENDING, "", USER_GROUP.SS);
                        
                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        if (result.equalsIgnoreCase("success")) {
                            
                        }
                    }
                } else if (model.getWf_status_2().equals(WF_STATUS.ISSUE_USCS80_PENDING)) {
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.ISSUE_USCS80_PENDING, WF_STATUS.ISSUE_USCS80_COMPLETED, WF_STATUS.SUBMISSION_DRAFT, "", "");
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    generateChecklistFile(model.getJob_id(), FILE_TYPE.U50);
                    
                    if (result.equalsIgnoreCase("success")) {
                        // To send email upon complete job
                        Map param = new HashMap();
                        JobDetailModel jobmodel = (JobDetailModel) dao.getModelById(model.getJob_id(), JobDetailModel.class);
                        param.put("strUsjNo", jobmodel.getUsj_no());
                        param.put("strPlanNo", "USP-" + jobmodel.getUsj_div() + "-" + jobmodel.getPlan_no());
                        param.put("strChecklist", "Checklist U50");
                        param.put("submitDueDate", getLetterSignedDate(dao.getSession(), jobmodel.getJob_id(), FILE_TYPE.USCS80));
                        Debug.printDebug("param " + param);
                        sendEmailUponCompletion(dao.getSession(), model, EMAIL_CODE.USCS80, FILE_TYPE.USCS80, param);
                        
                        updateLetterVersion(model, FILE_TYPE.USCS80);
                    }
    
                } else if (model.getWf_status_2().equals(WF_STATUS.ASSIGN_STA_FOR_CHECKING)) {
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Debug.printDebug("model.getAssignTo() " + model.getAssignTo());
                    Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.ASSIGN_STA_FOR_CHECKING, WF_STATUS.ASSIGN_STA_CHECKING_COMPLETED, WF_STATUS.CHECK_U60_PENDING, model.getAssignTo(), USER_GROUP.STA);
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    if (result.equalsIgnoreCase("success")) {
                        
                    }
                } else if (model.getWf_status_2().equals(WF_STATUS.CHECK_U60_PENDING)) {
                    saveOrUpdateChecklist(model, model.getU60ChecklistModel(), model.getU60ChecklistSetupModel(), "2", "N");

                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.CHECK_U60_PENDING, WF_STATUS.CHECK_U60_COMPLETED, WF_STATUS.CHECK_U60_SD_PENDING, "", USER_GROUP.SD);
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    if (result.equalsIgnoreCase("success")) {
                        generateChecklistFile(model.getJob_id(), FILE_TYPE.U60);
                    }
                } else if (model.getWf_status_2().equals(WF_STATUS.CHECK_U60_SD_PENDING)) {
                    saveOrUpdateChecklist(model, model.getU60ChecklistModel(), model.getU60ChecklistSetupModel(), "2", "N");
                    
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.CHECK_U60_SD_PENDING, WF_STATUS.CHECK_U60_SD_COMPLETED, WF_STATUS.CHECK_U60_SS_PENDING, "", USER_GROUP.SS);
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    if (result.equalsIgnoreCase("success")) {
                        
                    }
                    
                } else if (model.getWf_status_2().equals(WF_STATUS.CHECK_U60_SS_PENDING)) {
                    saveOrUpdateChecklist(model, model.getU60ChecklistModel(), model.getU60ChecklistSetupModel(), "2", "Y");

                    ChecklistModel caseChecklistModel = new ChecklistModel();
                    caseChecklistModel = model.getU60ChecklistModel();
                    Debug.printDebug("checklist decision " + caseChecklistModel.getCheck_status());
                    
                    if (caseChecklistModel.getCheck_status().equals("A")) {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Debug.printDebug("model.get_taskId() " + model.get_taskId());
                        Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.CHECK_U60_SS_PENDING, WF_STATUS.CHECK_U60_SS_COMPLETED, WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING, "", "");

                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;

                        if (result.equalsIgnoreCase("success")) {

                        }
                        
                    } else {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Debug.printDebug("model.get_taskId() " + model.get_taskId());
                        Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "N", WF_STATUS.CHECK_U60_SS_PENDING, WF_STATUS.CHECK_U60_SS_COMPLETED, WF_STATUS.ISSUE_USCS80_U60_PENDING, "", "");

                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;

                        if (result.equalsIgnoreCase("success")) {

                        }
                    }

                } else if (model.getWf_status_2().equals(WF_STATUS.ISSUE_USCS80_U60_PENDING)) {
                    
                } else if (model.getWf_status_2().equals(WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING)) {
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_SUB2(getSession(), model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING, WF_STATUS.ENDORSE_USP_ISSUE_USCS90_COMPLETED, WF_STATUS.COMPLETED, "", "");
                    
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                    
                    generateChecklistFile(model.getJob_id(), FILE_TYPE.U60);
                    
                    if (result.equalsIgnoreCase("success")) {
                        // To send email upon complete job
                        Map param = new HashMap();
                        JobDetailModel jobmodel = (JobDetailModel) dao.getModelById(model.getJob_id(), JobDetailModel.class);
                        param.put("strUsjNo", jobmodel.getUsj_no());
                        param.put("approvedDate", jobmodel.getApproval_date_str());
                        param.put("strPlanNo", "USP-" + jobmodel.getUsj_div() + "-" + jobmodel.getPlan_no());
                        param.put("submitDueDate", getLetterSignedDate(dao.getSession(), jobmodel.getJob_id(), FILE_TYPE.USCS90));
                        sendEmailUponCompletion(dao.getSession(), model, EMAIL_CODE.USCS90, FILE_TYPE.USCS90, param);
                        sendEmailToPlanningBranch(dao.getSession(), jobmodel, EMAIL_CODE.USCS90, FILE_TYPE.USCS90, param);
                        
                        // To zip all signed DSP and upload to elasis sftp (path: /home/elasis/attachment/eLasis/utimaps/usp)
                        Map fileParam = new HashMap();
                        fileParam.put("case_id", model.getJob_id());
                        fileParam.put("file_type", SystemConstants.FILE_TYPE.SSDSP);
                        fileParam.put("description", SystemConstants.FILE_TYPE.SSDSP + "_SIGNED");
                        fileParam.put("file_status", "Y");
                        List<FileModel> ssdspList = dao.list_order(fileParam, FileModel.class, "");

                        FtpInterface ftp = FileOperationUtil.getFtpInterface();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ZipOutputStream out = new ZipOutputStream(baos);
                        String zipFilePath = "UAP_" + jobmodel.getUsj_div() + "_" + jobmodel.getPlan_no() + ".zip";

                        try {
                            for(FileModel ssdsp : ssdspList){
                                InputStream inputStream = null;
                                try {
                                    inputStream = ftp.getFile(ssdsp.getFile_path());
                                    if (inputStream == null) {
                                        System.err.println("Error: Could not retrieve file from FTP: " + ssdsp.getFile_path());
                                        continue; // Skip to the next file
                                    }
                                    byte[] buffer = new byte[1024];
                                    int length;

                                    File file = new File(ssdsp.getFile_path());
                                    String entryname = file.getName();
                                    ZipEntry zipEntry = new ZipEntry(ssdsp.getFile_name());
                                    out.putNextEntry(zipEntry);

                                    while ((length = inputStream.read(buffer)) > 0) {
                                        out.write(buffer, 0, length);
                                    }
                                    out.closeEntry();

                                } catch (IOException e) {
                                    e.printStackTrace(); // Handle the exception appropriately
                                } finally {
                                    if (inputStream != null) {
                                        try {
                                            inputStream.close();
                                        } catch (IOException e) {
                                            e.printStackTrace(); // Handle the close exception
                                        }
                                    }
                                }
                            }
                        } finally {
                            try {
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace(); // Handle the close exception for ZipOutputStream
                            }
                        }

                        java.io.InputStream zipInputStream = new java.io.ByteArrayInputStream(baos.toByteArray());

                        FtpInterface ftps = FileOperationUtil.getFtpInterface("sftp");
                        ftps.createFile(zipFilePath, zipInputStream);
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "completeApplication");
        } finally {
            dao.closeAllSession();
            closeSession();
        }
        return completingResult;
    }

    // Common Function to save/update all types of Checklist and Checklist Item
    public ChecklistModel saveOrUpdateChecklist(JobDetailModel model, ChecklistModel getChecklistModel, ChecklistSetupModel getChecklistSetupModel, String triggerFrom, String updateFileStatus) throws Exception {
        
        BaseDAOImpl dao = new BaseDAOImpl();
        ChecklistModel caseChecklistModel = new ChecklistModel();
        ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
        
        try {
            dao.setSession(getSession());
            beginBatchTransaction();
            
            caseChecklistModel = getChecklistModel;
            checklistSetupModel = getChecklistSetupModel;
            
            if (caseChecklistModel.getCheck_id() == "" || Validator.isEmpty(caseChecklistModel.getCheck_id())) {
                Debug.printDebug("empty check id " + caseChecklistModel);
                
                caseChecklistModel.defaultAddProperties();
                String check_id = com.sains.framework.base.CommonFunction.getId(20);
                caseChecklistModel.setID(check_id);
                
                Map sessionMap = ActionContext.getContext().getSession();
                User user = new User();
                user = (User) dao.getModelById(sessionMap.get("userId").toString(), User.class);
                
                if (triggerFrom.equals("1")) {
                    if (model.getWf_status().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                        caseChecklistModel.setCheck_by_oic(user.getUs_id());
                        caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());
                        caseChecklistModel.setCheck_by_ss(user.getUs_id());
                        caseChecklistModel.setCheck_date_ss(DateUtil.getCurrentTimestamp());
                    } else {
                        caseChecklistModel.setCheck_by_oic(user.getUs_id());
                        caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());
                    }
                } else {
                    if (model.getWf_status_2().equals(WF_STATUS.TA_CHECK_U30_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "comment_ss", "check_by_ss", "check_date_ss"};
                        caseChecklistModel.setCheck_by_ss(user.getUs_id());
                        caseChecklistModel.setCheck_date_ss(DateUtil.getCurrentTimestamp());
                    } else if (model.getWf_status_2().equals(WF_STATUS.STA_CHECK_U30_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "check_comment_oic", "check_by_oic", "check_date_oic"};
                        caseChecklistModel.setCheck_by_oic(user.getUs_id());
                        caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());
                    } else if (model.getWf_status_2().equals(WF_STATUS.CHECK_U60_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "check_comment_oic", "check_by_oic", "check_date_oic"};
                        caseChecklistModel.setCheck_by_oic(user.getUs_id());
                        caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());
                    } else if (model.getWf_status_2().equals(WF_STATUS.CHECK_U60_SD_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "verify_by_oic","verify_date_oic","comment_verify_oic"};
                        caseChecklistModel.setVerify_by_oic(user.getUs_id());
                        caseChecklistModel.setVerify_date_oic(DateUtil.getCurrentTimestamp());
                    } else if (model.getWf_status_2().equals(WF_STATUS.CHECK_U60_SS_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "check_by_ss", "check_date_ss", "comment_ss",};
                        caseChecklistModel.setCheck_by_ss(user.getUs_id());
                        caseChecklistModel.setCheck_date_ss(DateUtil.getCurrentTimestamp());
                    } else {
                        caseChecklistModel.setCheck_by_oic(user.getUs_id());
                        caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());
                    }
                }
                
                getSession().save(caseChecklistModel);
                
                checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel cism) -> {
                    
                    for (ChecklistItemModel cim : cism.getChecklistResultList()) {
                        try {
                            if (Validator.isEmpty(cim.getCl_id())) {
                                cim.defaultAddProperties();
                                cim.setCl_id(com.sains.framework.base.CommonFunction.getId(20));
                                cim.setCheck_id(check_id);
                                cim.setCl_status("Y");
                                
                                if (updateFileStatus.equals("Y")) {
                                    if (!Validator.isEmpty(cim.getCl_result())) {
                                        if (cim.getCl_result().equals("C")) {
                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                if (updateFile != null) {
                                                    updateFile.updatableColumns = new String[]{"file_id", "file_status"};
                                                    updateFile.setFile_status("Y");
                                                    updateFile.defaultUpdateProperties();
                                                    getSession().update(updateFile);
                                                }
                                            });
                                        } else if (cim.getCl_result().equals("E")) {
                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                if (updateFile != null) {
                                                    updateFile.updatableColumns = new String[]{"file_id", "file_status"};
                                                    updateFile.setFile_status("N");
                                                    updateFile.defaultUpdateProperties();
                                                    getSession().update(updateFile);
                                                }
                                            });
                                        } else if (cim.getCl_result().equals("N")) {
                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                if (updateFile != null) {
                                                    updateFile.updatableColumns = new String[]{"file_id", "file_status"};
                                                    updateFile.setFile_status("Y");
                                                    updateFile.defaultUpdateProperties();
                                                    getSession().update(updateFile);
                                                }
                                            });
                                        }
                                    }
                                }
                                getSession().save(cim);
                            } else {
                                cim.updatableColumns = new String[]{"cl_id", "case_id", "check_id", "file_id", "ci_id", "cl_result", "cl_status", "cl_remarks", "cl_file_id"};
                                cim = (ChecklistItemModel) dao.setUpdateProperties(cim, getSession());
                                Debug.printDebug("check_id " + check_id);
                                cim.setCheck_id(check_id);
                                cim.setCl_status("Y");
                                
                                if (updateFileStatus.equals("Y")) {
                                    if (!Validator.isEmpty(cim.getCl_result())) {
                                        if (cim.getCl_result().equals("C")) {
                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                if (updateFile != null) {
                                                    updateFile.updatableColumns = new String[]{"file_id", "file_status"};
                                                    updateFile.setFile_status("Y");
                                                    updateFile.defaultUpdateProperties();
                                                    getSession().update(updateFile);
                                                }
                                            });
                                        } else if (cim.getCl_result().equals("E")) {
                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                if (updateFile != null) {
                                                    updateFile.updatableColumns = new String[]{"file_id", "file_status"};
                                                    updateFile.setFile_status("N");
                                                    updateFile.defaultUpdateProperties();
                                                    getSession().update(updateFile);
                                                }
                                            });
                                        } else if (cim.getCl_result().equals("N")) {
                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                if (updateFile != null) {
                                                    updateFile.updatableColumns = new String[]{"file_id", "file_status"};
                                                    updateFile.setFile_status("Y");
                                                    updateFile.defaultUpdateProperties();
                                                    getSession().update(updateFile);
                                                }
                                            });
                                        }
                                    }
                                }
                                getSession().update(cim);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "saveOrUpdateChecklist");
                        }
                    }
                });
            } else {
                Debug.printDebug("caseChecklistModel.getChecklistModel() " + caseChecklistModel.getCheck_id());
                
                String check_id = caseChecklistModel.getCheck_id();
                
                Map sessionMap = ActionContext.getContext().getSession();
                User user = new User();
                user = (User) dao.getModelById(sessionMap.get("userId").toString(), User.class);
                
                if (triggerFrom.equals("1")) {
                    if (model.getWf_status().equals(WF_STATUS.CHECK_U10_PENDING) || model.getWf_status().equals(WF_STATUS.CHECK_U20_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "rec_status", "check_comment_oic", "check_by_oic", "check_date_oic"};
                        caseChecklistModel.setCheck_by_oic(user.getUs_id());
                        caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());
                    } else if (model.getWf_status().equals(WF_STATUS.VERIFY_U10_PENDING) || model.getWf_status().equals(WF_STATUS.VERIFY_U20_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "check_status", "comment_ss", "check_by_ss", "check_date_ss"};
                        caseChecklistModel.setCheck_by_ss(user.getUs_id());
                        caseChecklistModel.setCheck_date_ss(DateUtil.getCurrentTimestamp());
                    }
                    
                } else {
                    if (model.getWf_status_2().equals(WF_STATUS.CHECK_HARDCOPY_SUBMISSION_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "check_status", "check_comment_oic", "check_by_oic", "check_date_oic"};
                        caseChecklistModel.setCheck_by_oic(user.getUs_id());
                        caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());
                    } else if (model.getWf_status_2().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "check_status", "check_comment_oic", "comment_ss", "check_by_ss", "check_date_ss"};
                        caseChecklistModel.setCheck_by_ss(user.getUs_id());
                        caseChecklistModel.setCheck_date_ss(DateUtil.getCurrentTimestamp());
                    } else if (model.getWf_status_2().equals(WF_STATUS.TA_CHECK_U30_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "check_status", "comment_ss", "check_by_ss", "check_date_ss"};
                        caseChecklistModel.setCheck_by_ss(user.getUs_id());
                        caseChecklistModel.setCheck_date_ss(DateUtil.getCurrentTimestamp());
                    } else if (model.getWf_status_2().equals(WF_STATUS.STA_CHECK_U30_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "check_status", "check_comment_oic", "check_by_oic", "check_date_oic"};
                        caseChecklistModel.setCheck_by_oic(user.getUs_id());
                        caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());
                    } else if (model.getWf_status_2().equals(WF_STATUS.CHECK_U40_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "check_status", "check_comment_oic", "check_by_oic", "check_date_oic"};
                        caseChecklistModel.setCheck_by_oic(user.getUs_id());
                        caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());
                    } else if (model.getWf_status_2().equals(WF_STATUS.CHECK_U50_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "check_status", "check_comment_oic", "check_by_oic", "check_date_oic"};
                        caseChecklistModel.setCheck_by_oic(user.getUs_id());
                        caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());
                    } else if (model.getWf_status_2().equals(WF_STATUS.CHECK_U60_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "rec_status", "check_comment_oic", "check_by_oic", "check_date_oic"};
                        caseChecklistModel.setCheck_by_oic(user.getUs_id());
                        caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());
                    } else if (model.getWf_status_2().equals(WF_STATUS.CHECK_U60_SD_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "verify_status", "verify_by_oic","verify_date_oic","comment_verify_oic"};
                        caseChecklistModel.setVerify_by_oic(user.getUs_id());
                        caseChecklistModel.setVerify_date_oic(DateUtil.getCurrentTimestamp());
                    } else if (model.getWf_status_2().equals(WF_STATUS.CHECK_U60_SS_PENDING)) {
                        caseChecklistModel.updatableColumns = new String[]{"check_id", "case_id", "check_status", "check_by_ss", "check_date_ss", "comment_ss",};
                        caseChecklistModel.setCheck_by_ss(user.getUs_id());
                        caseChecklistModel.setCheck_date_ss(DateUtil.getCurrentTimestamp());
                    }
                }
                
                caseChecklistModel = (ChecklistModel) dao.setUpdateProperties(caseChecklistModel, getSession());
                getSession().update(caseChecklistModel);
                
                checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel cism) -> {
                    
                    for (ChecklistItemModel cim : cism.getChecklistResultList()) {
                        try {
                            if (Validator.isEmpty(cim.getCl_id())) {
                                cim.defaultAddProperties();
                                cim.setCheck_id(check_id);
                                cim.setCl_id(com.sains.framework.base.CommonFunction.getId(20));
                                cim.setCl_status("Y");
                                
                                if (updateFileStatus.equals("Y")) {
                                    if (!Validator.isEmpty(cim.getCl_result())) {
                                        if (cim.getCl_result().equals("C")) {
                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                if (updateFile != null) {
                                                    updateFile.updatableColumns = new String[]{"file_id", "file_status"};
                                                    updateFile.setFile_status("Y");
                                                    updateFile.defaultUpdateProperties();
                                                    getSession().update(updateFile);
                                                }
                                            });
                                        } else if (cim.getCl_result().equals("E")) {
                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                if (updateFile != null) {
                                                    updateFile.updatableColumns = new String[]{"file_id", "file_status"};
                                                    updateFile.setFile_status("N");
                                                    updateFile.defaultUpdateProperties();
                                                    getSession().update(updateFile);
                                                }
                                            });
                                        } else if (cim.getCl_result().equals("N")) {
                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                if (updateFile != null) {
                                                    updateFile.updatableColumns = new String[]{"file_id", "file_status"};
                                                    updateFile.setFile_status("Y");
                                                    updateFile.defaultUpdateProperties();
                                                    getSession().update(updateFile);
                                                }
                                            });
                                        }
                                    }
                                }
                                getSession().save(cim);
                            } else {
                                cim.updatableColumns = new String[]{"cl_id", "case_id", "check_id", "file_id", "ci_id", "cl_result", "cl_status", "cl_remarks", "cl_file_id"};
                                cim = (ChecklistItemModel) dao.setUpdateProperties(cim, getSession());
                                cim.setCl_status("Y");
                                cim.setCheck_id(check_id);
                                
                                if (updateFileStatus.equals("Y")) {
                                    if (!Validator.isEmpty(cim.getCl_result())) {
                                        if (cim.getCl_result().equals("C")) {
                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                if (updateFile != null) {
                                                    updateFile.updatableColumns = new String[]{"file_id", "file_status"};
                                                    updateFile.setFile_status("Y");
                                                    updateFile.defaultUpdateProperties();
                                                    getSession().update(updateFile);
                                                }
                                            });
                                        } else if (cim.getCl_result().equals("E")) {
                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                if (updateFile != null) {
                                                    updateFile.updatableColumns = new String[]{"file_id", "file_status"};
                                                    updateFile.setFile_status("N");
                                                    updateFile.defaultUpdateProperties();
                                                    getSession().update(updateFile);
                                                }
                                            });
                                        } else if (cim.getCl_result().equals("N")) {
                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                if (updateFile != null) {
                                                    updateFile.updatableColumns = new String[]{"file_id", "file_status"};
                                                    updateFile.setFile_status("Y");
                                                    updateFile.defaultUpdateProperties();
                                                    getSession().update(updateFile);
                                                }
                                            });
                                        }
                                    }
                                }
                                getSession().update(cim);
                            }
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "saveOrUpdateChecklist");
                        }
                    }
                });
            }
            
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "saveOrUpdateChecklist");
        } finally {
        }
        
        return caseChecklistModel;
    }
    
    public String deleteUSCSLetter(JobDetailModel model, String letterType, String letterSubType) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        String completingResult = "success";
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        
        try {
            Debug.printDebug("runnning deleteUSJFile task daoimpl~~~~~ " + model.get_operation());
            dao.setSession(getSession());
            beginBatchTransaction();
            
            if (model.get_operation().equals(ApplicationPModel.OPERATION.DELETE_USCS_LETTER)) {
                List<FileModel> existingFileList = new ArrayList();
                Map param = new HashMap();
                Debug.printDebug("letterType " + letterType);
                Debug.printDebug("letterSubType " + letterSubType);
                param.put("case_id", model.getJob_id());
                param.put("file_type", letterType);
                param.put("description", letterSubType);
                param.put("file_status", "Y");
                existingFileList = dao.list_order(param, FileModel.class, "");
                Debug.printDebug("file size " + existingFileList.size());
                if (existingFileList.size() > 0) {
                    for (FileModel existingFile : existingFileList) {
                        if (existingFile != null) {
                            if (ftp.isFileExists(existingFile.getFile_path(), Boolean.FALSE)) {
                                ftp.deleteFile(existingFile.getFile_path());
                            }
                            dao.getSession().delete(existingFile);
                        }
                    }
                }
            }
            
            commitBatchTransaction();
            
        } catch (Exception e) {
            rollbackBatchTransaction();
            completingResult = "failed";
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "deleteUSCSLetter");
        } finally {
            dao.closeSession();
            closeSession();
        }
        
        return completingResult;
    }
    
    public Integer signUSCSLetter(JobDetailModel model, String processType, String letterType) throws Exception {
        
        Debug.printDebug("getCert_File " + model.getCert_File());
        Debug.printDebug("getCert_Password " + model.getCert_Password());
        
        BaseDAOImpl dao = new BaseDAOImpl();
        Integer intError = 1;
        String filePath = "";
        String fileName;
        String fileId;
        PDFSign pdfsign = new PDFSign();
        
        try {
            Debug.printDebug("runnning signUSJLetter task daoimpl~~~~~ " + DateUtil.getCurrentTimestamp());
            Debug.printDebug("runnning signUSJLetter task daoimpl~~~~~ " + model.get_operation() + model.getJob_id());
            dao.setSession(getSession());
            if (processType.equals("DSP")) {
                Map param = new HashMap();
                param.put("case_id", model.getJob_id());
                param.put("file_type", "PSDSP");
                List<FileModel> fileList = dao.list_order(param, FileModel.class, true, "and (description not in ('DSP_SIGNED', 'DSP_STAMPED') or description is null)");
                
                for (FileModel drawing : fileList) {
                    fileId = drawing.getFile_id();
                    filePath = drawing.getFile_path();
                    fileName = drawing.getFile_name();
                    intError = pdfsign.selfSignMode(fileId, filePath, fileName, model.getCert_File(), model.getCert_Password(), model.getJob_id(), model.getUsj_div(), processType);
                }
            } else {
                FileModel uscsLetter = new FileModel();
                uscsLetter = (FileModel) dao.getModelByCode("file_type,case_id,description", letterType + "," + model.getJob_id() + "," + letterType, new FileModel());
                fileId = uscsLetter.getFile_id();
                filePath = uscsLetter.getFile_path();
                fileName = uscsLetter.getFile_name();
                intError = pdfsign.selfSignMode(fileId, filePath, fileName, model.getCert_File(), model.getCert_Password(), model.getJob_id(), model.getUsj_div(), processType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "signUSCSLetter");
        } finally {
            dao.closeSession();
            closeSession();
        }
        
        return intError;
    }
    
    public Integer insertSignedUSCSLetter(JobDetailModel model, String fileId, String filePath, String fileName, String contentType, String fileType, String fileSubType) throws Exception {
        
        BaseDAOImpl dao = new BaseDAOImpl();
        OBSUtil ftp = new OBSUtil();
        FileModel fileModel = new FileModel();
        
        try {
            dao.setSession(getSession());
            beginBatchTransaction();
            Debug.printDebug("insertSignedUSJLetter daoImpl");
            
            if (model.get_operation().equals(ApplicationPModel.OPERATION.INSERT_SIGNED_USCS)) {
                fileModel.defaultAddProperties();
                fileModel.setFile_id(fileId);
                fileModel.setFile_type(fileType);
                fileModel.setFile_name(fileName);
                fileModel.setOriginal_file_name(fileName);
                fileModel.setFile_ext(fileName.substring(fileName.lastIndexOf(".") + 1));
                fileModel.setCase_id(model.getJob_id());
                fileModel.setFile_path(filePath + "/" + fileId);
                fileModel.setUsj_no("-");
                fileModel.setTotal_files(1);
                fileModel.setTotal_gislayer(0);
                fileModel.setTotal_features(0);
                fileModel.setDescription(fileSubType);
//                fileModel.setFile_status("Y");
                
                if (fileType.equals("PSDSP")) {                    
                    fileModel.setFile_status("P"); //put P if PS just signed digital plan on pb
                } else {
                    fileModel.setFile_status("Y");
                }
                
                dao.getSession().save(fileModel);
                
            }
            commitBatchTransaction();
            
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "insertSignedUSCSLetter");
        } finally {
            dao.closeSession();
            closeSession();
            ftp.insertToFileDirectory(filePath + "/" + fileModel.getFile_id(), fileName, "application/pdf", fileModel.getFile_id(), dao, SystemConstants.FILE_TYPE.SISJL, Boolean.FALSE);
        }
        
        return 0;
    }
    
    public String revokeUSCSLetter(JobDetailModel model, String fileType) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        OBSUtil ftp = new OBSUtil();
        FileModel fileModel = new FileModel();
        
        try {
            dao.setSession(getSession());
            beginBatchTransaction();
            Debug.printDebug("revokeUSJLetter daoImpl");
            
            if (model.get_operation().equals(ApplicationPModel.OPERATION.REVOKE_USCS_LETTER)) {
                List<FileModel> existingFileList = new ArrayList();
                Map param = new HashMap();
                param.put("case_id", model.getJob_id());
                param.put("file_type", fileType);
                param.put("file_status", "Y");
                existingFileList = dao.list_order(param, FileModel.class, "");                
                Debug.printDebug("file size " + existingFileList.size());
                if (existingFileList.size() > 0) {
                    for (FileModel existingFile : existingFileList) {
                        if (existingFile != null) {
                            if (ftp.isFileExists(existingFile.getFile_path(), Boolean.FALSE)) {
                                ftp.deleteFile(existingFile.getFile_path());
                            }
                            dao.getSession().delete(existingFile);
                        }
                    }
                }                
                
            }
            commitBatchTransaction();
            
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "revokeUSCSLetter");
        } finally {
            dao.closeSession();
            closeSession();
        }
        
        return "";
    }
    
    public Map sendEmailUponCompletion(Session session, JobDetailModel model, String emailCode, String letterType, Map mapParam) {
        Map mailJsonMap = new HashMap();
        AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
        autoEmailDAO.setSession(session);
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(session);
        AutoEmail autoEmail = null;
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        Map sessionMap = ActionContext.getContext().getSession();
        
        try {
            Debug.printDebug("send submission email to public user");
            
            List<String> fileArrayList = new ArrayList(); // 09.09.2024 :: List to store all files to be sent with email
            String publicUserName = "";
            String surveyFirmUserName = "";
            String publicUserId = "";
            String publicUserEmail = "default@utimaps.sains.com.my";
            String surveyFirmEmail = "";
            
            ApplicationPModel appModel = new ApplicationPModel();
            appModel = (ApplicationPModel) dao.getModelById(model.getCase_id(), ApplicationPModel.class);
            
            PublicUserModel publicUser = (PublicUserModel) dao.getModelByCode("us_user_id", appModel.getApp_submit_by(), new PublicUserModel());
            
            if (publicUser != null) {
                publicUserName = publicUser.getUs_user_name();
                publicUserId = publicUser.getUs_user_id();
                publicUserEmail = publicUser.getUs_email();
            }
            
            SurveyFirmPModel surveyFirm = (SurveyFirmPModel) dao.getModelByCode("case_id", appModel.getCase_id(), new SurveyFirmPModel());
            if (surveyFirm != null) {
                surveyFirmUserName = surveyFirm.getFirm_oic();
                surveyFirmEmail = surveyFirm.getFirm_email();
                
                publicUserName = Validator.isEmpty(surveyFirmUserName) ? publicUserName : publicUserName + ", " + surveyFirmUserName;
                publicUserEmail = Validator.isEmpty(surveyFirmEmail) ? publicUserEmail : publicUserEmail + "," + surveyFirmEmail;
            }
            
            Map mailParam = new HashMap();
            autoEmail = autoEmailDAO.getAutoEmailByCode(emailCode, new AutoEmail());
            
            mailParam.put(EmailTrigger.MAIL_TO, publicUserEmail); // For Public User
            
            if(!appModel.getInternal_case().equals("Y")) {
                mailParam.put(EmailTrigger.MAIL_CCTO, SystemConstants.email_landsurveyboard);
            }
                
            mailParam.put("userName", publicUserName); // For Public User
            mailParam.put("strUsjNo", model.getUsj_no());
            
            if (letterType.equals(FILE_TYPE.USCS10H)) {
                mailParam.put("strChecklist", "Checklist U21");
            } else {
                mailParam.put("strChecklist", "Checklist " + getChecklistNameFromUSCS(letterType));
            }

            //Digital or Hardcopy Documents based on param
            if (emailCode.equals(EMAIL_CODE.USCS10)) {
                if (letterType.equals(FILE_TYPE.USCS10H)) {
                    mailParam.put("strDocType", "hardcopy");
                } else {
                    mailParam.put("strDocType", "digital");
                }
            }
            
            if (mapParam != null) {
                mapParam.keySet().forEach((obj) -> {
                    mailParam.put(obj, mapParam.get(obj));
                });
            }
            
            FileModel attachmentLetter = new FileModel();
            if (!Validator.isEmpty(letterType)) {
                attachmentLetter = (FileModel) dao.getSession().getNamedQuery("FileModel.findBy_USCS")
                        .setParameter("case_id", model.getJob_id())
                        .setParameter("file_type", letterType)
                        .setParameter("description", letterType + "_SIGNED")
                        .uniqueResult();
            }
            
            Debug.printDebug("attachmentLetter " + attachmentLetter);
            List attList = new ArrayList<Map>();
            if (attachmentLetter != null) {
                String filePath = (attachmentLetter != null) ? attachmentLetter.getFile_path() : "";;
                String fileId = attachmentLetter.getFile_id();
                
                Map aa = new HashMap();
                String fileName = attachmentLetter.getFile_name();
                aa.put(EmailTrigger.ATTACH.AttInputStream, ftp.getFile(filePath));
                aa.put(EmailTrigger.ATTACH.AttFileName, fileName);
                attList.add(aa);
                fileArrayList.add(fileId);
                Debug.printDebug("file_id " + fileId + " || filePath " + filePath + " || fileName " + fileName);
            }
            
            //04.09.2024:: Change to list
            List<FileModel> checklistFileModelList = new ArrayList();
            Map param = new HashMap();
            param.put("case_id", model.getJob_id());
            param.put("file_type", getChecklistNameFromUSCS(letterType));
            checklistFileModelList = dao.list_order(param, FileModel.class, "order by created_date asc");
            
            if (checklistFileModelList.size() > 0) {
                String fileName = "";
                String filePath = "";
                String fileId = "";
                for (FileModel checklistFile : checklistFileModelList) {
                    filePath = (checklistFile != null) ? checklistFile.getFile_path() : "";;
                    fileName = checklistFile.getFile_name();
                    fileId = checklistFile.getFile_id();
                }
                
                Map aa = new HashMap();
                aa.put(EmailTrigger.ATTACH.AttInputStream, ftp.getFile(filePath));
                aa.put(EmailTrigger.ATTACH.AttFileName, fileName);
                attList.add(aa);
                fileArrayList.add(fileId);
                Debug.printDebug("file_id " + fileId + " || filePath " + filePath + " || fileName " + fileName);
            }

            // For USCS10, to check if contain traverse, to include checklist U10 together
            if (letterType.equals(FILE_TYPE.USCS10)) {
                if (model.getControl_sv_flag().equals("Y")) {
                    //04.09.2024:: Change to list
                    List<FileModel> checklistFileModelList2 = new ArrayList();
                    Map param2 = new HashMap();
                    param2.put("case_id", model.getJob_id());
                    param2.put("file_type", "U10");
                    checklistFileModelList2 = dao.list_order(param2, FileModel.class, "order by created_date asc");
                    
                    if (checklistFileModelList2.size() > 0) {
                        String filePath = "";
                        String fileName = "";
                        String fileId = attachmentLetter.getFile_id();
                        for (FileModel checklistFile : checklistFileModelList2) {
                            filePath = (checklistFile != null) ? checklistFile.getFile_path() : "";;
                            
                            fileName = checklistFile.getFile_name();
                        }
                        
                        Map aa = new HashMap();
                        aa.put(EmailTrigger.ATTACH.AttInputStream, ftp.getFile(filePath));
                        aa.put(EmailTrigger.ATTACH.AttFileName, fileName);
                        attList.add(aa);
                        fileArrayList.add(fileId);
                        Debug.printDebug("file_id " + fileId + " || filePath " + filePath + " || fileName " + fileName);
                    }
                }
            }
            
            if (!attList.isEmpty()) {
                //add attachment
                mailParam.put(EmailTrigger.ATTACH.AttList, attList);
            }
            
            new EmailTrigger().sendEMail(mailParam, autoEmail);
            
            String not_id = "";
            String not_sender = "";
            String not_subject = "";
            String not_content = "";
            if (autoEmail.getNotificationSetup() != null) {
                not_sender = sessionMap.get("userId").toString();
                not_id = autoEmail.getNotificationSetup().getNo_id();
                not_subject = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_subject(), mailParam);
                not_content = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_body(), mailParam);
            }
            
            String fileList = "";
            if (fileArrayList.size() > 0) {
                fileList = fileArrayList.toString();
            }
            Debug.printDebug("fileList before insert notification: " + fileList);

            //Insert Notification
            NotificationPModel insertNot = new UtimapsAction().insertNotification2(dao.getSession(), model.getJob_id(), not_id, not_sender, publicUserId, not_subject, not_content, "U001", fileList, model.get_taskId(), "S");
            
            if (insertNot == null) {
                Debug.printDebug("Error when insert notification, no notification is inserted." + model.getJob_id() + " - " + not_id + " - " + not_sender + " - " + publicUserId + " - " + not_subject + " - " + not_content + " - " + "U001" + " - " + fileList + " - " + model.get_taskId() + " - " + "S");
            } else {
                new UtimapsAction().auditAction2(model.getJob_id(), dao.getSession(), not_sender, "Send Notification : " + model.getJob_id());
            }
            
            Debug.printDebug("### Public email sent ");
            mailJsonMap.put("status", "success");
            mailJsonMap.put("errMsg", "");
        } catch (Exception e) {
            mailJsonMap.put("errMsg", "Fail to update case status");
            mailJsonMap.put("status", "fail");
            Debug.printDebug("Error when send email " + e);
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "sendEmailUponCompletion");
        } finally {
            autoEmailDAO.closeSession();
        }
        
        return mailJsonMap;
    }
    
    public Map sendEmailToPlanningBranch(Session session, JobDetailModel model, String emailCode, String letterType, Map mapParam) {
        Map mailJsonMap = new HashMap();
        AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
        autoEmailDAO.setSession(session);
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(session);
        AutoEmail autoEmail = null;
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        Map sessionMap = ActionContext.getContext().getSession();
        
        try {
            Debug.printDebug("send submission email to planning branch");
            
            String planningUserName = "";
            String planningUserId = "";
            String planningUserEmail = "default@utimaps.sains.com.my";
            ArrayList<String> planningUserEmailList = new ArrayList<String>();
            
            SetupGroup planningGroup = (SetupGroup) dao.getModelByCode("group_code", "USJ_PLANNING_" + model.getUsj_div(), new SetupGroup());
            if (planningGroup != null) {
                if (planningGroup.getGroupUser().size() > 0) {
                    for (GroupUser gu : planningGroup.getGroupUser()) {
                        if (gu.getUserModel() != null) {
                            planningUserEmailList.add(gu.getUserModel().getUs_email());
                        }
                    }
                }
            }
            
            if (!planningUserEmailList.isEmpty()) {
                planningUserEmail = planningUserEmailList.toString().replace("[", "").replace("]", "");
                Debug.printDebug("planningUserEmail " + planningUserEmail);
            }
            
            Map mailParam = new HashMap();
            autoEmail = autoEmailDAO.getAutoEmailByCode(emailCode, new AutoEmail());
            
            mailParam.put(EmailTrigger.MAIL_TO, planningUserEmail);
            mailParam.put("userName", "Planning Officer");
            mailParam.put("strUsjNo", model.getUsj_no());
            
            FileModel attachmentLetter = new FileModel();
            if (!Validator.isEmpty(letterType)) {
                attachmentLetter = (FileModel) dao.getSession().getNamedQuery("FileModel.findBy_USCS")
                        .setParameter("case_id", model.getJob_id())
                        .setParameter("file_type", letterType)
                        .setParameter("description", letterType + "_SIGNED")
                        .uniqueResult();
            }
            
            List attList = new ArrayList<Map>();
            if (attachmentLetter != null) {
                String filePath = (attachmentLetter != null) ? attachmentLetter.getFile_path() : "";;
                
                Map aa = new HashMap();
                String fileName = attachmentLetter.getFile_name();
                aa.put(EmailTrigger.ATTACH.AttInputStream, ftp.getFile(filePath));
                aa.put(EmailTrigger.ATTACH.AttFileName, fileName);
                attList.add(aa);
                
            }
            
            FileModel attachmentMinute = new FileModel();
            if (letterType.equals("USCS90")) {
                attachmentMinute = (FileModel) dao.getSession().getNamedQuery("FileModel.findBy_USCS")
                        .setParameter("case_id", model.getJob_id())
                        .setParameter("file_type", SystemConstants.FILE_TYPE.MINUTE)
                        .setParameter("description", SystemConstants.FILE_TYPE.MINUTE + "_SIGNED")
                        .uniqueResult();
            }
            
            if (attachmentMinute != null) {
                String filePath = (attachmentMinute != null) ? attachmentMinute.getFile_path() : "";;
                
                Map aa = new HashMap();
                String fileName = attachmentMinute.getFile_name();
                aa.put(EmailTrigger.ATTACH.AttInputStream, ftp.getFile(filePath));
                aa.put(EmailTrigger.ATTACH.AttFileName, fileName);
                attList.add(aa);
            }
            
            if (!attList.isEmpty()) {
                //add attachment
                mailParam.put(EmailTrigger.ATTACH.AttList, attList);
            }
            
            if (mapParam != null) {
                mapParam.keySet().forEach((obj) -> {
                    mailParam.put(obj, mapParam.get(obj));
                });
            }
            
            new EmailTrigger().sendEMail(mailParam, autoEmail);
            
            String not_id = "";
            String not_sender = "";
            String not_subject = "";
            String not_content = "";
            if (autoEmail.getNotificationSetup() != null) {
                not_sender = sessionMap.get("userId").toString();
                not_id = autoEmail.getNotificationSetup().getNo_id();
                not_subject = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_subject(), mailParam);
                not_content = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_body(), mailParam);
            }
            
            Debug.printDebug("### Planning email sent ");
            
            mailJsonMap.put("status", "success");
            mailJsonMap.put("errMsg", "");
        } catch (Exception e) {
            mailJsonMap.put("errMsg", "Fail to update case status");
            mailJsonMap.put("status", "fail");
            Debug.printDebug("Error when send email " + e);
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "sendEmailToPlanningBranch");
        } finally {
            autoEmailDAO.closeSession();
        }
        
        return mailJsonMap;
    }
    
    @Override
    public synchronized String clearSubmissionChecklist(JobDetailModel model) throws Exception {
        
        BaseDAOImpl dao = new BaseDAOImpl();
        try {
            Debug.printDebug("runnning checkSubmissionChecklist daoimpl~~~~~");
            dao.setSession(getSession());
            beginBatchTransaction();
            
            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
            checklistSetupModel = model.getChecklistSetupCaseModel();
            
            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                Debug.printDebug("=============================================" + "size " + csi.getChecklistFileModelList().size());
                if (!csi.getChecklistFileModelList().isEmpty()) {
                    FileModel attFile = csi.getChecklistFileModelList().get(0);
                    Debug.printDebug("attFile name " + attFile.getFile_name() + " || " + attFile.getFile_status());
                    
                    String fileStatus = attFile.getFile_status();
                    
                    for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                        try {
                            if (fileStatus.equals("P")) {
                                Debug.printDebug("fileStatus " + fileStatus);
                                cim.updatableColumns = new String[]{"cl_id", "cl_result", "cl_remarks"};
                                cim = (ChecklistItemModel) dao.setUpdateProperties(cim, dao.getSession());
                                cim.setCl_result("");
                                cim.setCl_remarks("");
                                getSession().update(cim);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "clearSubmissionChecklist");
                        }
                    }
                }
            });
            
            if (model.getU10ChecklistModel() != null) {
                ChecklistModel checklistModel = model.getU10ChecklistModel();
                checklistModel.updatableColumns = new String[]{"check_id", "check_status", "check_comment_oic", "check_date_oic", "check_by_oic", "comment_ss", "check_by_ss", "check_date_ss"};
                checklistModel.setCheck_status("");
                checklistModel.setCheck_comment_oic("");
                checklistModel.setCheck_by_oic("");
                checklistModel.setCheck_date_oic(null);
                checklistModel.setComment_ss("");
                checklistModel.setCheck_by_ss("");
                checklistModel.setCheck_date_ss(null);
                getSession().update(checklistModel);
            }
            
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "clearSubmissionChecklist");
        } finally {
            dao.closeSession();
            closeSession();
        }
        
        return "";
    }
    
    @Override
    public synchronized String clearRejectedChecklist(JobDetailModel model) throws Exception {
        
        BaseDAOImpl dao = new BaseDAOImpl();
        try {
            Debug.printDebug("runnning clearRejectedSubmissionChecklist daoimpl~~~~~");
            dao.setSession(getSession());
            beginBatchTransaction();
            
            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
            checklistSetupModel = model.getChecklistSetupCaseModel();
            
            if (!checklistSetupModel.getChecklistItemList().isEmpty()) {
                checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                    for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                        if (cim.getCl_result().equals("E")) {
                            try {
                                cim.updatableColumns = new String[]{"cl_id", "cl_result", "cl_remarks"};
                                cim = (ChecklistItemModel) dao.setUpdateProperties(cim, dao.getSession());
                                cim.setCl_result("");
                                cim.setCl_remarks("");
                                getSession().update(cim);
                            } catch (Exception e) {
                                e.printStackTrace();
                                CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "clearRejectedChecklist");
                            }
                        }
                    }
                });
            }
            
            ChecklistSetupModel checklistSetupModel2 = new ChecklistSetupModel();
            checklistSetupModel2 = model.getChecklistSetupModel();
            
            if (!checklistSetupModel2.getChecklistItemList().isEmpty()) {
                checklistSetupModel2.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                    for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                        if (cim.getCl_result().equals("E")) {
                            try {
                                cim.updatableColumns = new String[]{"cl_id", "cl_result", "cl_remarks"};
                                cim = (ChecklistItemModel) dao.setUpdateProperties(cim, dao.getSession());
                                cim.setCl_result("");
                                cim.setCl_remarks("");
                                getSession().update(cim);
                            } catch (Exception e) {
                                e.printStackTrace();
                                CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "clearRejectedChecklist");
                            }
                        }
                    }
                });
            }
            
            if (model.getU10ChecklistModel() != null) {
                ChecklistModel checklistModel = model.getU10ChecklistModel();
                checklistModel.updatableColumns = new String[]{"check_id", "check_status", "check_comment_oic", "check_date_oic", "check_by_oic", "comment_ss", "check_by_ss", "check_date_ss"};
                checklistModel.setCheck_status("");
                checklistModel.setCheck_comment_oic("");
                checklistModel.setCheck_by_oic("");
                checklistModel.setCheck_date_oic(null);
                checklistModel.setComment_ss("");
                checklistModel.setCheck_by_ss("");
                checklistModel.setCheck_date_ss(null);
                getSession().update(checklistModel);
            }
            
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "clearRejectedChecklist");
        } finally {
            dao.closeSession();
            closeSession();
        }
        
        return "";
    }

    @Override
    public synchronized String clearAllSubmissionChecklist(JobDetailModel model) throws Exception {
        
        BaseDAOImpl dao = new BaseDAOImpl();
        try {
            Debug.printDebug("runnning clearAllSubmissionChecklist daoimpl~~~~~");
            dao.setSession(getSession());
            beginBatchTransaction();
            
            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
            checklistSetupModel = model.getChecklistSetupCaseModel();
            
            if (!checklistSetupModel.getChecklistItemList().isEmpty()) {
                checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                    for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                        try {
                            cim.updatableColumns = new String[]{"cl_id", "cl_result", "cl_remarks"};
                            cim = (ChecklistItemModel) dao.setUpdateProperties(cim, dao.getSession());
                            cim.setCl_result("");
                            cim.setCl_remarks("");
                            getSession().update(cim);
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "clearAllSubmissionChecklist");
                        }
                    }
                });
            }
            
            if (model.getU10ChecklistModel() != null) {
                ChecklistModel checklistModel = model.getU10ChecklistModel();
                checklistModel.updatableColumns = new String[]{"check_id", "check_status", "check_comment_oic", "check_date_oic", "check_by_oic", "comment_ss", "check_by_ss", "check_date_ss"};
                checklistModel.setCheck_status("");
                checklistModel.setCheck_comment_oic("");
                checklistModel.setCheck_by_oic("");
                checklistModel.setCheck_date_oic(null);
                checklistModel.setComment_ss("");
                checklistModel.setCheck_by_ss("");
                checklistModel.setCheck_date_ss(null);
                getSession().update(checklistModel);
            }
            
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "clearAllSubmissionChecklist");
        } finally {
            dao.closeSession();
            closeSession();
        }
        
        return "";
    }
    
    public void generateChecklistFile(String idJob, String processType) throws Exception {
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        InputStream jasperRptStream = null;
        Map reportParam = new HashMap();
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(getSession());
        
        try {
            String sftpUploadPath = null;            
            String newDrDocId = CommonFunction.getId(20);
            String jasperFileName = "";
            String fileName = "sample.pdf";
            FileModel uploadingModel = null;
            
            JobDetailModel jobModel = (JobDetailModel) dao.getModelById(idJob, JobDetailModel.class);
            sftpUploadPath = processType;
            fileName = processType + "_" + jobModel.getUsj_no().replace("/", "_") + ".pdf";
            jasperFileName = getJasperReportName(processType);
            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
            reportParam.put("pCaseId", idJob.trim());
            
            ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
            if (ftp.isDirExists(SysConf.get("pathPrefix") + sftpUploadPath, Boolean.FALSE)) {
                ftp.createDirIfNotExists(sftpUploadPath + "/" + idJob, Boolean.FALSE);
                ftp.createFile(sftpUploadPath + "/" + idJob + "/" + newDrDocId, ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, dao));// use File as parameter
                ftp.insertToFileDirectory(sftpUploadPath + "/" + idJob + "/" + newDrDocId, fileName, "application/pdf", newDrDocId, dao, SystemConstants.FILE_TYPE.USJL, Boolean.FALSE);
                Debug.printDebug("file insert directory " + sftpUploadPath + "/" + idJob + "/");
                if (uploadingModel != null) {
                } else {
                    uploadingModel = new FileModel();
                    uploadingModel.setFile_id(newDrDocId);
                    uploadingModel.setFile_type(processType);
                    uploadingModel.setFile_name(fileName);
                    uploadingModel.setOriginal_file_name(fileName);
                    uploadingModel.setFile_ext("PDF");
                    uploadingModel.setCase_id(idJob);
                    uploadingModel.setCi_id("");
                    uploadingModel.setFile_path(sftpUploadPath + "/" + idJob + "/" + newDrDocId);
                    uploadingModel.setUsj_no("-");
                    uploadingModel.setTotal_files(1);
                    uploadingModel.setTotal_gislayer(0);
                    uploadingModel.setTotal_features(0);
                    uploadingModel.defaultAddProperties();
                    uploadingModel.setFile_status("Y");
                    
                    try {
                        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                        request.setAttribute("ignoreCsrfCheck", "true");
                        ModelBase.updateCurrentRequestObject("antiCSRF", "ignoreCSRF");
                        dao.beginBatchTransaction();
                        dao.insert(uploadingModel);
                        dao.commitBatchTransaction();
                    } catch (Exception e) {
                        dao.rollbackBatchTransaction();
                        try {
                            ftp.deleteFile(sftpUploadPath + "/" + newDrDocId);
                        } catch (Exception delEx) {
                            e.printStackTrace();
                            CommonFunction.writeLogFile(delEx.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "generateChecklistFile");
                        }
                        e.printStackTrace();
                        CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "generateChecklistFile");
                    }
                }
            }
        } catch (BaseException be) {
            be.printStackTrace();
            CommonFunction.writeLogFile(be.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "generateChecklistFile");
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "generateChecklistFile");
        } finally {
            ftp.disconnect();
            dao.closeSession();
        }
    }
    
    public String deleteChecklistFile(JobDetailModel model, String processType) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        String completingResult = "success";
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        
        try {
            Debug.printDebug("runnning deleteChecklistFile task daoimpl~~~~~ " + processType);
            dao.setSession(getSession());
            beginBatchTransaction();
            
            switch (processType) {
                case "USCS10":
                    removeChecklistFile(model, processType);
                    break;
                case "USCS10H":
                    removeChecklistFile(model, processType);
                    break;
                case "USCS40":
                    removeChecklistFile(model, processType);
                    break;
                case "USCS50":
                    removeChecklistFile(model, processType);
                    break;
                case "USCS80":
                    removeChecklistFile(model, processType);
                    break;
                default:
                    removeChecklistFile(model, processType);
                    break;
            }
            
            commitBatchTransaction();
            
        } catch (Exception e) {
            rollbackBatchTransaction();
            completingResult = "failed";
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "deleteChecklistFile");
        } finally {
            dao.closeSession();
            closeSession();
        }
        
        return completingResult;
    }
    
    public void removeChecklistFile(JobDetailModel model, String fileType) throws Exception {
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        BaseDAOImpl dao = new BaseDAOImpl();
        
        try {
            dao.setSession(getSession());
            List<FileModel> existingFileList = new ArrayList();
            Map param = new HashMap();
            param.put("case_id", model.getJob_id());
            param.put("file_type", fileType);
            existingFileList = dao.list_order(param, FileModel.class, "");
            List<String> fileTypeList = Arrays.asList("U10", "U20", "U21", "U30", "U40", "U50");
            if (fileTypeList.contains(fileType)) {
                if (existingFileList.size() > 0) {
                    for (FileModel existingFile : existingFileList) {
                        existingFile.updatableColumns = new String[]{"file_status"};
                        existingFile = (FileModel) dao.setUpdateProperties(existingFile, dao.getSession());
                        existingFile.setFile_status("N");
                        getSession().update(existingFile);
                    }
                }
            } else {
                if (existingFileList.size() > 0) {
                    for (FileModel existingFile : existingFileList) {
                        if (existingFile != null) {
                            if (ftp.isFileExists(existingFile.getFile_path(), Boolean.FALSE)) {
                                ftp.deleteFile(existingFile.getFile_path());
                            }
                            dao.getSession().delete(existingFile);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "removeChecklistFile");
        }
    }
    
    public String getJasperReportName(String rptType) {
        String jasperFileName = "";
        switch (rptType) {
            case "U10":
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U10.jasper";
                break;
            case "U20":
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U20.jasper";
                break;
            case "U21":
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U21.jasper";
                break;
            case "U30":
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U30.jasper";
                break;
            case "U40":
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U40.jasper";
                break;
            case "U50":
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U50.jasper";
                break;
            default:
                break;
        }
        
        return jasperFileName;
    }
    
    public String getChecklistNameFromUSCS(String letterType) {
        String checklistName = "";
        switch (letterType) {
            case "USCS10":
                checklistName = "U20";
                break;
            case "USCS10H":
                checklistName = "U21";
                break;
            case "USCS40":
                checklistName = "U30";
                break;
            case "USCS50":
                checklistName = "U40";
                break;
            case "USCS80":
                checklistName = "U50";
                break;
            default:
                checklistName = "None";
                break;
        }
        
        return checklistName;
    }
    
    public void updateLetterVersion(JobDetailModel jobModel, String fileType) throws Exception {
        
        BaseDAOImpl dao = new BaseDAOImpl();
        
        try {
            dao.setSession(getSession());
            beginBatchTransaction();
            List<FileModel> existingFileList = new ArrayList();
            Map param = new HashMap();
            param.put("case_id", jobModel.getJob_id());
            param.put("file_type", fileType);
            existingFileList = dao.list_order(param, FileModel.class, "");
            
            if (existingFileList.size() > 0) {
                for (FileModel existingFile : existingFileList) {
                    existingFile.updatableColumns = new String[]{"job_id, file_status"};
                    existingFile = (FileModel) dao.setUpdateProperties(existingFile, dao.getSession());
                    existingFile.setFile_status("N");
                    dao.getSession().update(existingFile);
                }
            }
            
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "updateLetterVersion");
        } finally {
            dao.closeAllSession();
        }
    }
    
    public Integer updateApprovalDate(JobDetailModel model) throws Exception {
        
        BaseDAOImpl dao = new BaseDAOImpl();
        Debug.printDebug("== updateApprovalDate ==");
        try {
            Debug.printDebug("getSession() " + getSession());
            dao.setSession(getSession());
            Debug.printDebug("dao.getSession() " + dao.getSession());
            beginBatchTransaction();
            model.updatableColumns = new String[]{"job_id", "approval_date"};
            model.setApproval_date(DateUtil.getCurrentTimestamp());
            dao.getSession().update(model);
            
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "updateApprovalDate");
            return 0;
        } finally {
            dao.closeSession();
        }
        
        return 1;
    }
    
    public Map updateJobProgress(String usjNo, String divNo, String jobAction, String statusDate) throws Exception {
        String result = "0";
        BaseDAOImpl dao = new BaseDAOImpl();
        Map jsonMap = new HashMap();
        
        try {
            dao.setSession(getSession());
            String usjSeq = usjNo.substring(0, 4);
            String usjYear = usjNo.substring(4, 8);
            String strMsg = "";
            String strStatus = "";
            
            JobDetailModel jobModel = (JobDetailModel) dao.getModelByCode("usj_seq,usj_year,usj_div", usjSeq + "," + usjYear + "," + divNo, new JobDetailModel());
            WorkflowApiAction wfApi = new WorkflowApiAction();
            String wf_rtn = "success";
            
            Debug.printDebug("=== UPDATE JOB PROGRESS :: USJ NO " + usjNo + " || DIV NO " + divNo + " jobAction : " + jobAction + " || statusDate : " + statusDate);
            
            if (jobModel == null) {
            } else {
                if (!Validator.isEmpty(jobAction)) {
                    switch (jobAction) {
                        case "JP3 260":
                            Debug.printDebug("To call wfApi.startUSJ003_05");
//                            wf_rtn = wfApi.startUSJ003_05(jobModel.getJob_id(), "SCS");
                            break;
                        case "JP3 151":
                            jobModel.setComp_approved_date(DateUtil.getCurrentTimestamp());
                            dao.beginBatchTransaction();
                            dao.getSession().update(jobModel);
                            dao.commitBatchTransaction();
                            Debug.printDebug("To call wfApi.startUSJ003_06");
//                            wf_rtn = wfApi.startUSJ003_06(jobModel.getJob_id(), "BACKEND");
                            break;
                        case "JP3 204":
                            Debug.printDebug("To call wfApi.startUSJ003_04");
//                            wf_rtn = wfApi.startUSJ003_04(jobModel.getJob_id(), "BACKEND");
                            break;
                        default:
                            Debug.printDebug("Invalid Job Action Code");
                            wf_rtn = "Invalid Job Action Code";
                            break;
                    }
                }
                
                if (wf_rtn.equals("success")) {
                    strStatus = wf_rtn;
                } else {
                    strStatus = "failed";
                    strMsg = wf_rtn;
                }
                
                jsonMap.put("status", strStatus);
                jsonMap.put("result", "1");
                jsonMap.put("resultMsg", strMsg);
            }
            
            Debug.printDebug("" + jobModel);
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "updateJobProgress");
            jsonMap.put("status", "failed");
            jsonMap.put("result", "-1");
            jsonMap.put("resultMsg", e);
        } finally {
            
        }
        
        return jsonMap;
    }
    
    public Map sendEmailROQuery(Session session, JobDetailModel model) {
        Map mailJsonMap = new HashMap();
        AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
        autoEmailDAO.setSession(session);
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(session);
        AutoEmail autoEmail = null;
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        
        try {
            Debug.printDebug("send query email to public user");
            List<String> fileArrayList = new ArrayList(); // 17.09.2024 :: List to store all files to be sent with email
            String publicUserName = "";
            String publicUserId = "";
            String publicUserEmail = "default@utimaps.sains.com.my";
            String surveyFirmUserName = "";
            String surveyFirmEmail = "";
            
            ApplicationPModel appModel = new ApplicationPModel();
            appModel = (ApplicationPModel) dao.getModelById(model.getCase_id(), ApplicationPModel.class);
            
            PublicUserModel publicUser = (PublicUserModel) dao.getModelByCode("us_user_id", appModel.getApp_submit_by(), new PublicUserModel());
            
            if (publicUser != null) {
                publicUserName = publicUser.getUs_user_name();
                publicUserId = publicUser.getUs_user_id();
                publicUserEmail = publicUser.getUs_email();
            }
            
            SurveyFirmPModel surveyFirm = (SurveyFirmPModel) dao.getModelByCode("case_id", appModel.getCase_id(), new SurveyFirmPModel());
            if (surveyFirm != null) {
                surveyFirmUserName = surveyFirm.getFirm_oic();
                surveyFirmEmail = surveyFirm.getFirm_email();
                
                publicUserName = Validator.isEmpty(surveyFirmUserName) ? publicUserName : publicUserName + ", " + surveyFirmUserName;
                publicUserEmail = Validator.isEmpty(surveyFirmEmail) ? publicUserEmail : publicUserEmail + "," + surveyFirmEmail;
            }
            
            Map mailParam = new HashMap();
            autoEmail = autoEmailDAO.getAutoEmailByCode("USJROQuery", new AutoEmail());
            
            Debug.printDebug("autoEmail " + autoEmail);
            mailParam.put(EmailTrigger.MAIL_TO, publicUserEmail); // For Public User
            mailParam.put("userName", publicUserName); // For Public User
            mailParam.put("strUsjNo", model.getUsj_no());
            mailParam.put("submitDueDate", DateUtil.getCurrentDatePlus7Days());
            
            FileModel attachmentLetter = new FileModel();
            attachmentLetter = (FileModel) dao.getSession().getNamedQuery("FileModel.findBy_DRO")
                    .setParameter("case_id", model.getJob_id())
                    .setParameter("file_type", SystemConstants.FILE_TYPE.DRO)
                    .uniqueResult();
            
            Debug.printDebug("attachmentLetter " + attachmentLetter);
            List attList = new ArrayList<Map>();
            String attachmentId = "";
            if (attachmentLetter != null) {
                String filePath = (attachmentLetter != null) ? attachmentLetter.getFile_path() : "";;
                
                Map aa = new HashMap();
                String fileName = attachmentLetter.getFile_name();
                aa.put(EmailTrigger.ATTACH.AttInputStream, ftp.getFile(filePath));
                aa.put(EmailTrigger.ATTACH.AttFileName, fileName);
                attList.add(aa);
                fileArrayList.add(attachmentLetter.getFile_id());
                Debug.printDebug("filePath " + filePath);
                Debug.printDebug("fileName " + fileName);
                
                attachmentId = attachmentLetter.getFile_id();
            }
            
            if (!attList.isEmpty()) {
                //add attachment
                mailParam.put(EmailTrigger.ATTACH.AttList, attList);
            }
            
            new EmailTrigger().sendEMail(mailParam, autoEmail);
            
            String not_id = "";
            String not_sender = "";
            String not_subject = "";
            String not_content = "";
            if (autoEmail.getNotificationSetup() != null) {
                not_sender = publicUserId;
                not_id = autoEmail.getNotificationSetup().getNo_id();
                not_subject = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_subject(), mailParam);
                not_content = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_body(), mailParam);
            }
            
            String fileList = "";
            if (fileArrayList.size() > 0) {
                fileList = fileArrayList.toString();
            }
            Debug.printDebug("fileList before insert notification: " + fileList);

            //Insert Notification
            NotificationPModel insertNot = new UtimapsAction().insertNotification(dao.getSession(), model.getJob_id(), not_id, not_sender, publicUserId, not_subject, not_content, "U001", fileList, model.get_taskId(), "S");
            
            if (insertNot == null) {
                Debug.printDebug("Error when insert notification, no notification is inserted." + model.getJob_id() + " - " + not_id + " - " + not_sender + " - " + publicUserId + " - " + not_subject + " - " + not_content + " - " + "U001" + " - " + attachmentLetter.getFile_id() + " - " + model.get_taskId() + " - " + "S");
            } else {
                new UtimapsAction().auditAction(model.getJob_id(), dao.getSession(), not_sender, "Send Notification : " + model.getJob_id());
            }
            
            Debug.printDebug("### Public email sent ");
            mailJsonMap.put("status", "success");
            mailJsonMap.put("errMsg", "");
        } catch (Exception e) {
            mailJsonMap.put("errMsg", "Fail to update case status");
            mailJsonMap.put("status", "fail");
            Debug.printDebug("Error when send email " + e);
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "sendEmailROQuery");
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "sendEmailROQuery");
        } finally {
//            autoEmailDAO.closeSession();
        }
        
        return mailJsonMap;
    }
    
    public String setupHashParamKey() {
        String paramUserName = (String) ActionContext.getContext().getSession().get("loginId");
        String param = "username='" + paramUserName + "'&hashkey='" + GetMapHashKey(paramUserName) + "'";
        Debug.printDebug("param - " + param);
        
        return Base64.getEncoder().encodeToString(param.getBytes());
    }
    
    public String GetMapHashKey(String userName) {
        try {
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
            String CurrentYear = Integer.toString(currentTimestamp.getYear() + 1900);
            String CurrentDay = String.format("%02d", currentTimestamp.getDate());
            String CurrentMonth = String.format("%02d", currentTimestamp.getMonth() + 1);
            String appKey = "^" + userName + "^UTIMAPS";
            String hashStr = CurrentYear + "^" + CurrentMonth + "^" + CurrentDay + appKey;
            String hValue = calcHmac(hashStr);
            hValue = hValue.toUpperCase();
            
            return hValue;
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "GetMapHashKey");
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
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "calcHmac");
            return "";
        }
    }
    
    public String byteArrayToHexString(byte[] array) {
        java.util.Formatter formatter = new java.util.Formatter();
        for (byte b : array) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
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
    
    public String callPublicApiGet(String apiUrl) throws Exception {
        Debug.printDebug(apiUrl);
        SSLContext sslContext = null;
        String strResponse = "";
        try {
            sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (certificate, authType) -> true).build();
        } catch (KeyManagementException ex) {
            CommonFunction.writeLogFile(ex.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "calcHmac");
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
    
    public String getLetterSignedDate(Session session, String jobId, String letterType) {
        String letterDateStr = "";
        Timestamp letterDate;
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(session);
        
        try {
            FileModel fileModel = (FileModel) dao.getSession().getNamedQuery("FileModel.findBy_USCS")
                    .setParameter("case_id", jobId)
                    .setParameter("file_type", letterType)
                    .setParameter("description", letterType + "_SIGNED")
                    .uniqueResult();
            
            if (fileModel != null) {
                letterDate = fileModel.getCreated_date();
                letterDateStr = fileModel.getCreated_date_str();

                // Specify the number of days to add
                int daysToAdd = SystemConstants.daysDue;

                // Use Calendar to add days to the Timestamp
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(letterDate);
                calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);

                // Get the updated Timestamp
                Timestamp updatedTimestamp = new Timestamp(calendar.getTimeInMillis());
                letterDateStr = Formatter.formatDate(updatedTimestamp, SystemConstants.DATE.dataEntryFormat3);
                
                Debug.printDebug("letterDateStr " + letterDateStr);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "getLetterSignedDate");
        }
        return letterDateStr;
    }
    
    public String startCompJob(Session session, JobDetailModel model, Map sessionMap, String taskAssignTo) throws Exception {
        String resStatus = "success";
        String resMsg = "Server Not Responding.";
        String resDesc = "";
        JSONObject jsonResponse = new JSONObject();
        Map<String, Object> dataMap = new HashMap();
        
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(session);
         
        try {
            String token = "";
            token = getBearerToken();
            
            String fromUserId = sessionMap.get("userId").toString();
            String toUserId = taskAssignTo;
            Debug.printDebug("fromUserId " + fromUserId + " || toUserId " + toUserId);
            String fromLasisId = "";
            String toLasisId = "";
            
            String strSQL = "SELECT tsou.ORACLE_ID FROM T_SETUP_ORACLE_USER tsou " +
                "LEFT JOIN T_SETUP_USER_ORACLE tsuo ON tsuo.ORACLE_USER_ID = tsou.ORACLE_USER_ID " +
                "WHERE tsuo.US_ID = '" + fromUserId +"'";
                
            String strSQL2 = "SELECT tsou.ORACLE_ID FROM T_SETUP_ORACLE_USER tsou " +
                "LEFT JOIN T_SETUP_USER_ORACLE tsuo ON tsuo.ORACLE_USER_ID = tsou.ORACLE_USER_ID " +
                "WHERE tsuo.US_ID = '" + toUserId +"'";

            List lasisUserList = new ArrayList();
            lasisUserList = retrieverDAO.getListFromSql(strSQL,null);

            List lasisUserList2 = new ArrayList();
            lasisUserList2 = retrieverDAO.getListFromSql(strSQL2,null);

            if(lasisUserList.size() > 0){
                for (Map lasisUser : (List<Map>) lasisUserList) {
                    if(lasisUser.get("ORACLE_ID")!= null){
                        fromLasisId =  lasisUser.get("ORACLE_ID").toString();
                    }
                }
            }

            if(lasisUserList2.size() > 0){
                for (Map lasisUser : (List<Map>) lasisUserList2) {
                    if(lasisUser.get("ORACLE_ID")!= null){
                        toLasisId =  lasisUser.get("ORACLE_ID").toString();
                    }
                }
            }
            
            JobDetailModel jobModel = (JobDetailModel) retrieverDAO.getModelById(model.getJob_id(), JobDetailModel.class);
            String usjNo = jobModel.getUsj_year() + jobModel.getUsj_seq();
            String divNo = jobModel.getUsj_div();
            String assignFrom = fromLasisId; //LASIS User ID
            String assignTo = toLasisId; //LASIS User ID
            String dateAssign = "";
            String jobAction = SCS_JP3_PUBCODE.ASSIGN_COMP;
            String jobStatus = "1"; // Set to 1 : Active
            
            Debug.printDebug(usjNo + " || " + divNo + " || " + assignFrom + " || " + assignTo + " || " + jobAction + " || " + jobStatus);
//            String postReturn = null;
            String postReturn = postFormData(usjNo, divNo, "SYSTEM", assignTo, dateAssign, jobAction, jobStatus, token); //22-10-2024 :: change to use "SYSTEM" as requested by user
            
            if (postReturn != null && !Validator.isEmpty(postReturn)) {
                // json response
                JSONObject pcJsonResponse = (JSONObject) new JSONParser().parse(postReturn);
                Debug.printDebug("json response - " + pcJsonResponse);
                String initStatus = (String) pcJsonResponse.get("status");

                if ("success".equals(initStatus)) {
                    Debug.printDebug(" postsuccess");
                    resStatus = "postsuccess";
                    resMsg = "OK";
                    
                    new UtimapsAction().auditAction2(model.getJob_id(), retrieverDAO.getSession(), fromLasisId, "startCompJob post success : " + model.getJob_id() + " - " + usjNo);
                } else {
                    resStatus = "postfail";
                    resMsg = "CATCH_FAIL";

                    String initError = (String) pcJsonResponse.get("message");
                    resDesc = initError;
                    new UtimapsAction().auditAction2(model.getJob_id(), retrieverDAO.getSession(), fromLasisId, "startCompJob post failed : " + model.getJob_id() + " - " + usjNo);
                }
            } else {
                new UtimapsAction().auditAction2(model.getJob_id(), retrieverDAO.getSession(), fromLasisId, "startCompJob return failed : " + model.getJob_id() + " - " + usjNo);
                // Trigger catch handling for null or empty postReturn
                throw new Exception("Post return is null or empty");
            }

            jsonResponse.put("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            resStatus = "postfail";
            resMsg = "CATCH_FAIL";
        } finally {
            retrieverDAO.closeSession();
          
        }
        
        return resStatus;
    }
    
    public String getBearerToken() throws IOException {
        String token = (String) ActionContext.getContext().get("security_token");

        if (token == null || Validator.isEmpty(token)) {
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost post = new HttpPost(SystemConstants.DOMAIN.domain + "scs/esub/api/auth"); // SCS AUTH API

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("LOGIN", "utimaps_api_2024"));
                params.add(new BasicNameValuePair("PWD", "UTiMAPS@2024"));
                post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

                try (CloseableHttpResponse response = client.execute(post)) {
//                    Debug.printDebug("" + response.getStatusLine().getStatusCode());
                    HttpEntity responseEntity = response.getEntity();
                    // get token from response header
                    if (responseEntity != null) {
                        token = response.getFirstHeader("Authorization").getValue();
//                        Debug.printDebug("token - " + token);
                        return token;
                    }
                    return "";
                }
            }

        } else {

        }

        return token;
    }
    
    public String postFormData(String usj_no, String div_no, String assign_from, String assign_to, String date_assign, String job_action, String status, String token) throws IOException {
        CloseableHttpClient client = null;
        try {
            client = HttpClients.createDefault();
            HttpPost post = new HttpPost(SystemConstants.DOMAIN.domain + "scs/startCompJob");

//            Debug.printDebug("token? in post form data - " + token);
            post.setHeader(HttpHeaders.AUTHORIZATION, token);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("usj_no", usj_no));
            params.add(new BasicNameValuePair("div_no", div_no));
            params.add(new BasicNameValuePair("assign_from", assign_from));
            params.add(new BasicNameValuePair("assign_to", assign_to));
            params.add(new BasicNameValuePair("date_assign", date_assign));
            params.add(new BasicNameValuePair("job_action", job_action));
            params.add(new BasicNameValuePair("status", status));

            try {
                post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

                try (CloseableHttpResponse response = client.execute(post)) {
                    Debug.printDebug("resp 1 " + response.getStatusLine().getStatusCode());
                    HttpEntity responseEntity = response.getEntity();
                    if (responseEntity != null) {
                        String responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                        Debug.printDebug("res str - " + usj_no + " - " + responseString);
                        return responseString;
                    }
                    return "";
                }
            } catch (IOException e) {
                Debug.printDebug("Error during file operations or HTTP request: " + e.getMessage());
                throw e;
            } finally {
            }
        } catch (Exception e) {
            Debug.printDebug("Unexpected error in postFormData: " + e.getMessage());
            throw new IOException("Error in postFormData", e);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    Debug.printDebug("Error closing HTTP client: " + e.getMessage());
                }
            }
        }
    }
    
    public Map getInfo(String jobId, String dataName, String userGroup) {
        StringBuilder sb = new StringBuilder();
        BaseDAO dao = new BaseDAOImpl();
        try {
           
            String theUrl = SystemConstants.DOMAIN.domain + "/esub/getInfoRouteApi?dataName=" +dataName + "&rrUserGroup=" + userGroup;
            URL url = new URL(theUrl);  
            InputStreamReader stream = null;
            if (theUrl.startsWith("https")) {
                String rtnStr = HttpsUrlUtil.https_selfSignCallUrlGet(theUrl);
                Map jSonMap = new Gson().fromJson(rtnStr , new TypeToken<HashMap<String, Object>>() {}.getType() );
                return jSonMap;
            } else {
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(20000);  // 20 seconds
                stream = new InputStreamReader(conn.getInputStream());
                BufferedReader rd = new BufferedReader(stream);
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                Map jSonMap = new Gson().fromJson(sb.toString() , new TypeToken<HashMap<String, Object>>() {}.getType() );
                return jSonMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.closeSession();
        }
        return null;
    }
}
