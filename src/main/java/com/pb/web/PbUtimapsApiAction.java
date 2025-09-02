/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pb.web;

import com.backend.SarawakPayCheck;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lxg.common.model.Pubcode;
import com.lxg.common.model.PublicUserModel;
import static com.opensymphony.xwork2.Action.ERROR;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.DateUtil;
import com.sains.common.util.OBSUtil;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import static com.sains.framework.base.CommonFunction.as;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.LogFunction;
import com.sains.framework.base.ModelBase;
import com.sains.framework.base.ReportGenerator;
import com.sains.framework.base.ServiceFactory;
import com.sains.framework.model.User;
import com.sains.framework.base.web.LoginAction;
import com.utimaps.model.AppLocalityModel;
import com.sains.framework.model.DrDocRepoModel;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.ChecklistModel;
import com.utimaps.model.ChecklistItemModel;
import com.utimaps.model.FileModel;
import com.utimaps.model.JobDetailModel;
import com.utimaps.model.PaymentItemModel;
import com.utimaps.model.PaymentModel;
import com.utimaps.model.ProcessingHistoryModel;
import com.utimaps.model.PrecheckHistoryModel;
import com.utimaps.model.SurveyFirmPModel;
import com.utimaps.model.PrecheckLog;
import com.lxg.prepayment.dao.PrepaymentDAO;
import com.lxg.prepayment.model.PpBalanceModel;
import com.sains.common.util.Crypto;
import com.utimaps.model.NotificationPModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.WordUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.query.NativeQuery;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.utimaps.web.UtimapsAction;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import com.digicert.PDFSign;
import com.ism.web.ISMWorkflowBase;
import com.utimaps.web.ISMServicesAction;
import com.lxg.common.model.CustCompanyModel;
import com.lxg.common.model.UserCompanyModel;
import digicert.validation.X509DCVerification;
import com.sains.framework.base.Debug;
import com.sains.framework.base.EmailTrigger;
import com.sains.framework.model.AutoEmail;
import com.utimaps.model.ChecklistItemSetupModel;
import com.utimaps.model.ChecklistSetupModel;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContextBuilder;
import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import com.sains.common.util.FileOperationUtil;
import com.sains.common.util.FtpInterface;
import com.sains.framework.base.EmailQueueTrigger;
import com.webservice.PrecheckServiceAction;
import org.hibernate.Query;

/**
 *
 * @author Aiman
 */
public class PbUtimapsApiAction extends BaseActionSupport<ApplicationPModel> {

    private String pbUserName, pbLoginId, appId, codeType, code1, code_acr, hValue, hashKey, accessKey, pbUserId, selectStep, caseDiv;
    private String pFimUserId, pCoId, ssoCorpId, pCoEmail;
    private String caseOwnerId;
    private String reqCaseId;
    private String reqJobId;
    private String reqMsgId;
    private String reqPymtId;
    private String pymtState;
    private Map<String, Object> jsonMap = new HashMap();
    private Map<String, Object> appCaseMap = new HashMap();
    private Map<String, Object> submitDataMap = new HashMap();
    private Map<String, Object> subCaseMap = new HashMap();
    private Map<String, Object> precheckMap = new HashMap();
    private Map<String, Object> dataMap = new HashMap();
    private Map<String, Object> ddListMap = new HashMap();

    private String loginStatus = "";

    private String cert_pwd;
    private File cert_file;

    //payment api
    private String billRefNo = "";
    private String hashTotal = "";
    private String paymentId = "";
    private String caseId = "";
    private String paymentOptionId = "";

    //spaycheck
    private String orderId = "";
    private String orderNo = "";
    private String merOrderNo = "";
    private String paymentCategory = "";
    private String tranDate = "";
    private String activity = "";

    //For prepayment account verification screen.
    private String loginId;
    private String loginPasswd;
    private String dType = "";
    private String dCoId = "";
    private String usUserid = "";
    private String usid = "";
    private boolean blPaymentSuccess = false;
    private Double bal_amount_;

    private String ssoUserId;

    String[] docTypeList = new String[]{""};

    private CommonFunction cf = new CommonFunction();

    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);

    String[] appFields = new String[]{"case_id", "job_id", "usj_no", "case_ref", "case_type", "case_div", "case_seq", "case_year", "usj_div", "usj_seq", "usj_year", "pj_div", "pj_name", "land_dist", "land_desc", "client_name", "app_acknowledge", "app_submit_by", "app_submit_date", "app_status", "created_by", "created_date", "updated_by", "updated_date", "qual_level", "usj_providers"};
    String[] jobFields = new String[]{"case_id", "job_id", "usj_no", "usj_job_type", "usj_div", "usj_seq", "usj_year", "usj_date", "usj_status", "usj_request", "usj_desc", "case_ref", "created_by", "created_date", "updated_by", "updated_date", "usj_submission_date", "date_issue", "survey_date_start", "survey_date_end", "control_sv_flag", "plan_no", "ht_datum", "precheck_stage", "hardcopy_received_date"};
    String[] firmFields = new String[]{"so_app_id", "case_id", "firm_soc", "firm_oic", "firm_email", "firm_country_code", "firm_contact_num", "firm_fax_country_code", "firm_fax_num", "created_by", "created_date", "updated_by", "updated_date"};
    String[] docFields = new String[]{"file_id", "file_type", "file_name", "file_ext", "file_path", "case_id", "usj_no", "total_files", "total_gislayer", "total_features", "description", "created_date", "ci_id", "original_file_name"};
    String[] pymtFields = new String[]{"payment_id", "case_id", "payment_status", "payment_status_date", "payment_remarks", "payment_amount", "payment_method", "platform", "pay_ref_no", "bill_ref_no"};
    String[] pymtListFields = new String[]{"payment_id", "payitem_id", "item_ref_no", "item_desc", "item_amount", "rvs_receiptno", "rvs_depositno", "rvs_posted", "rvr_code", "remark", "subcode", "rvr_code_str"};
    String[] pbUserField = new String[]{"us_id", "us_user_name", "us_id_number", "us_email"};
    String[] historyFields = new String[]{"history_id", "case_id", "process_code", "process_date", "process_remarks", "cur_status"};

    String[] checklistFields = new String[]{"check_id", "case_id", "check_type", "check_status", "check_by_oic", "check_date_oic", "check_comment_oic", "comment_ss", "check_by_ss", "check_counter", "check_date_oic_str2", "check_by_oic_str", "check_date_ss_str", "check_by_ss_str"};
    String[] checklistSetupItemFields = new String[]{"ci_id", "checklist_id", "ci_desc", "ci_notes", "ci_datatype", "ci_sequence"};
    String[] checklistItemFields = new String[]{"cl_id", "ci_id", "check_id", "case_id", "cl_result", "cl_status", "cl_remarks", "ci_desc"};
    String[] processingHistoryFields = new String[]{"history_id", "case_id", "process_code", "process_by", "process_date_str", "process_seq", "process_remarks", "task_assign_to", "cur_status", "process_code_str"};
    String[] precheckHistoryFields = new String[]{"task_id", "job_id", "task_seq", "task_desc", "created_date", "created_by"};
    String[] notificationFields = new String[]{"message_id", "case_id", "no_id", "message_sender", "message_recipient", "message_subject", "message_content", "message_status", "message_status_date_str", "system_id", "file_id", "task_id", "message_type"};

    //joveni @ 17.3.2022 
    public static final class SPAY_DECRPYT_ACTIVITY_CODE {

        public static final String SPAY_UPDATE_PENDING = "SPY_PP";
        public static final String SPAY_UPDATE_COMPLETE = "SPY_CP";
    }

    public static final class USJ_STEP {

        public static final String STEP_NEW = "1";
        public static final String STEP_LIST = "2";
        public static final String STEP_CONTROL = "3";
        public static final String STEP_PRECHECK = "4";
        public static final String STEP_ACK = "5";
    }

    public static final class TRAV_HISTORY_TASK {

        public static final class TaskEntry {

            public final String task_desc;
            public final int task_seq;

            public TaskEntry(String task_desc, int task_seq) {
                this.task_desc = task_desc;
                this.task_seq = task_seq;
            }
        }

        public static final TaskEntry INIT = new TaskEntry("Initiated Traverse Pre-Check", 1);
        public static final TaskEntry PASSED = new TaskEntry("Passed Traverse Pre-Check", 2);
        public static final TaskEntry FAIL = new TaskEntry("Failed Traverse Pre-Check", 3);
    }

    /*
        INSERT / CREATE / NEW APPLICATION
     */
    public void newApplication() throws IOException {
        System.out.println("NEW APPLICATION API - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "Server Not Responding.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());

        Map returnAppMap = new HashMap();
        Map returnFirmMap = new HashMap();

        try {
            if (Validator.isEmpty(getPbUserName()) || Validator.isEmpty(this.hValue)) {
                resStatus = 401;
                resMsg = "Required param cannot be empty.";

                try {
                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonMap));
                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }

            } else {
                this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
                if (this.hValue.equals(this.hashKey)) {
                    resStatus = 200;
                    resMsg = "OK";
                    ApplicationPModel newModel = new ApplicationPModel();
                    SurveyFirmPModel newFirmModel = new SurveyFirmPModel();

                    try {
                        //check if there is an existing draft and divert to that if exists
//                        NativeQuery query = retrieverDAO.getSession().createNativeQuery("select * from us_application_p where app_submit_by = :app_submit_by and app_status = :app_status", ApplicationPModel.class);
//                        query.setParameter("app_submit_by", pbUserId);
//                        query.setParameter("app_status", "001");
//                        newModel = (ApplicationPModel) query.getSisngleResult();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (getReqCaseId() == null || Validator.isEmpty(getReqCaseId())) {
//                    if (newModel.getCase_id() == null || Validator.isEmpty(newModel.getCase_id())) {
                        newModel.setCase_id(com.sains.framework.base.CommonFunction.getId(20));

                        //reference setting
//                        Calendar cal = Calendar.getInstance();
//                        String caseYear = Integer.toString(cal.get(Calendar.YEAR));
//                        String sqlWhere = "case_year = " + "'" + caseYear + "'";
//                        String maxCaseSeq = cf.getSingleValueWithSession(baseDAO.getSession(), "us_application_p", "max(case_seq)", sqlWhere);
//                        String caseType = "UAP";
//                        Integer intNewCaseSeq = 0;
//
//                        if (Validator.isEmpty(maxCaseSeq)) {
//                            intNewCaseSeq = 1;
//                        } else {
//                            intNewCaseSeq = Integer.parseInt(maxCaseSeq) + 1;
//                        }
//
//                        //System.out.println("new seq - " + intNewCaseSeq);
//                        String newCaseSeq = String.format("%06d", intNewCaseSeq);
//
//                        newModel.setCase_type(caseType);
//                        newModel.setCase_year(caseYear);
//                        newModel.setCase_seq(newCaseSeq);
//                        newModel.setCase_ref(caseType + "/" + newCaseSeq + "/" + caseYear);
                        newModel = setAppReference(newModel);
                        newModel.setApp_status(UtimapsAction.PB_STATUS.APPLICATION_SAVE);
                        newModel.setWf_status(UtimapsAction.WF_STATUS.NEW);

                        newFirmModel.setFirm_soc(getFirmSoc());
                        appCaseMap.put("firm_name", getFirmSocName(getFirmSoc()));

                        newFirmModel.setSo_app_id(com.sains.framework.base.CommonFunction.getId(20));
                        newFirmModel.setCase_id(newModel.getCase_id());
                        newFirmModel.setCreated_date(DateUtil.getCurrentTimestamp());
                        newFirmModel.setCreated_by(pbLoginId);
                        newFirmModel.setUpdated_date(DateUtil.getCurrentTimestamp());
                        newFirmModel.setUpdated_by(pbLoginId);

//                        serviceFactory.getUtilAppService().manualInsert(newModel, pbLoginId);
//                        serviceFactory.getSurveyFirmService().insert(newFirmModel);
                        returnAppMap = getDataMap(newModel, appFields);
                        returnFirmMap = getDataMap(newFirmModel, firmFields);
                    } else {
                        System.out.println("EXISTING DRAFT UTIL - " + getReqCaseId());
                        newModel.setCase_id(getReqCaseId());
                        ApplicationPModel returnAppModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", getReqCaseId(), new ApplicationPModel());
                        SurveyFirmPModel returnFirmModel = (SurveyFirmPModel) retrieverDAO.getModelByCode("case_id", getReqCaseId(), new SurveyFirmPModel());

                        Map suppDocMap = new HashMap();
                        Map miscDocMap = new HashMap();

                        switch (selectStep) {
                            case "1":
                                appCaseMap.put("firm_name", getFirmSocName(getFirmSoc()));
                                break;
                            case "2":
                                //System.out.println("fetching ahead - supp docs n any remarks/checklist");

                                if (returnAppModel.getApp_status().equals(UtimapsAction.PB_STATUS.APPLICATION_SAVE)) {
                                    suppDocMap = checkSuppDoc(UtimapsAction.DOC_TYPE_CODE_1.APP, returnAppModel, false);
                                    miscDocMap = checkSuppDoc("SUP", returnAppModel, false);
                                } else {
                                    suppDocMap = checkSuppDoc(UtimapsAction.DOC_TYPE_CODE_1.APP, returnAppModel, true);
                                    miscDocMap = checkSuppDoc("SUP", returnAppModel, true);
//                                    System.out.println("app suppDocMap - " + suppDocMap);
                                    if (suppDocMap.containsKey("check_list")) {
                                        appCaseMap.put("check_list", suppDocMap.get("check_list"));
                                    }
                                }

//                                miscDocMap = checkMiscDoc(returnAppModel, "SUP");
                                appCaseMap.put("supp_doc", suppDocMap);
                                appCaseMap.put("misc_doc", miscDocMap);

                                break;
                            case "3":
                                jsonMap.put("public_user", fetchPbUser(this.pbUserId, retrieverDAO));
                                int docPass = verifySuppDocs(returnAppModel);

                                if (docPass > 0) {
                                    jsonMap.put("unsub_docs", submitDataMap.get("unsub_docs"));

                                    if (returnAppModel.getApp_status().equals(UtimapsAction.PB_STATUS.APPLICATION_SAVE)) {
                                        suppDocMap = checkSuppDoc(UtimapsAction.DOC_TYPE_CODE_1.APP, returnAppModel, false);
                                        miscDocMap = checkSuppDoc("SUP", returnAppModel, false);
                                    } else {
                                        suppDocMap = checkSuppDoc(UtimapsAction.DOC_TYPE_CODE_1.APP, returnAppModel, true);
//                                        System.out.println("app suppDocMap - " + suppDocMap);
                                        if (suppDocMap.containsKey("check_list")) {
                                            appCaseMap.put("check_list", suppDocMap.get("check_list"));
                                        }

                                        miscDocMap = checkSuppDoc("SUP", returnAppModel, true);
                                    }

                                    appCaseMap.put("supp_doc", suppDocMap);
                                    appCaseMap.put("misc_doc", miscDocMap);
                                }
                            default:
                                break;
                        }

                        if (returnAppModel.getChecklistModel() != null) {
                            //System.out.println("checklist retrv");
//                            ChecklistModel caseChecklist = returnAppModel.getChecklistModel();
                            ChecklistModel caseChecklist = (ChecklistModel) retrieverDAO.getModelByCode("case_id", returnAppModel.getCase_id(), new ChecklistModel());
                            Map checklistMap = getDataMap(caseChecklist, checklistFields);
                            appCaseMap.put("checklist", checklistMap);
                        } else {
                        }

                        returnAppMap = getDataMap(returnAppModel, appFields);
                        returnFirmMap = getDataMap(returnFirmModel, firmFields);
                    }

                    appCaseMap.put("util_app", returnAppMap);
                    appCaseMap.put("survey_firm", returnFirmMap);
                    //System.out.println("appCase - " + appCaseMap);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            jsonMap.put("dropdown", fetchDropDowns());
            jsonMap.put("docList", fetchDocList("APP"));
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("appCase", appCaseMap);

            retrieverDAO.closeSession();
            baseDAO.closeSession();

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }
    }

    public ApplicationPModel setAppReference(ApplicationPModel appModel) {
        try {
            Calendar cal = Calendar.getInstance();
            String caseYear = Integer.toString(cal.get(Calendar.YEAR));
            String sqlWhere = "case_year = " + "'" + caseYear + "'";
            String maxCaseSeq = cf.getSingleValueWithSession(baseDAO.getSession(), "us_application_p", "max(case_seq)", sqlWhere);
            String caseType = "UAP";
            Integer intNewCaseSeq = 0;

            if (Validator.isEmpty(maxCaseSeq)) {
                intNewCaseSeq = 1;
            } else {
                intNewCaseSeq = Integer.parseInt(maxCaseSeq) + 1;
            }

            //System.out.println("new seq - " + intNewCaseSeq);
            String newCaseSeq = String.format("%06d", intNewCaseSeq);

            appModel.setCase_type(caseType);
            appModel.setCase_year(caseYear);
            appModel.setCase_seq(newCaseSeq);
            appModel.setCase_ref(caseType + "/" + newCaseSeq + "/" + caseYear);

            System.out.println("new app case type " + appModel.getCase_type());
            System.out.println("new app case yr " + appModel.getCase_year());
            System.out.println("new app case seq " + appModel.getCase_seq());
            System.out.println("new app case ref " + appModel.getCase_ref());

        } catch (Exception e) {

        } finally {
            // not yet close
        }

        return appModel;
    }

    /*
        GET DETAILS OF UTIL APPLICATION (AFTER HAS BEEN SUBMITTED/IN PROCESSING) - PRIMARILY SUMMARY PAGE
     */
    public void getApplication() throws IOException {
        System.out.println("GET APPLICATION API - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "Server Not Responding.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());

        try {
            if (Validator.isEmpty(getPbUserName()) || Validator.isEmpty(this.hValue)) {
                resStatus = 401;
                resMsg = "Required param cannot be empty.";

                try {
                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonMap));
                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }

            } else {
                this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
                if (this.hValue.equals(this.hashKey)) {
                    resStatus = 200;
                    resMsg = "OK";

//                    System.out.println("reqCaseId - " + reqCaseId);
                    ApplicationPModel returnAppModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", reqCaseId, new ApplicationPModel());
                    SurveyFirmPModel returnFirmModel = (SurveyFirmPModel) retrieverDAO.getModelByCode("case_id", reqCaseId, new SurveyFirmPModel());

                    if (returnAppModel.getApp_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_SAVE) || returnAppModel.getApp_status().equals(UtimapsAction.PB_STATUS.ISSUANCE_PENDING) || returnAppModel.getApp_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_SUB)) {
//                        System.out.println("take job det");
                        JobDetailModel returnJobModel = (JobDetailModel) retrieverDAO.getModelByCode("case_id", reqCaseId, new JobDetailModel());
                        Map returnJobDetailMap = getDataMap(returnJobModel, jobFields);
                        appCaseMap.put("job_det", returnJobDetailMap);

                        if (returnAppModel.getApp_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_SAVE)
                                || returnJobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_SAVE)
                                || returnJobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS10)
                                || returnJobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40)
                                || returnJobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS50)
                                || returnJobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS80)
                                || returnJobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK)) {
                            try {
                                FileModel usjIssuance = (FileModel) retrieverDAO.getModelByCode("case_id,file_type", returnJobModel.getJob_id() + "," + "SISJL", new FileModel());
//                                Debug.printDebug("got? " + usjIssuance.getFile_id());
                                appCaseMap.put("sji_id", usjIssuance.getFile_id());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

//                    if (returnAppModel.getUSJL() != null && !returnAppModel.getUSJL().isEmpty()) {
//                        FileModel usjIssuance = (FileModel) retrieverDAO.getModelByCode("job_id", jobModel.getJob_id(), new FileModel());
//                        FileModel usjIssuance = returnAppModel.getUSJL().get(0);
//                        System.out.println("sji id - " + usjIssuance.getFile_id());
//                    }
                    Map suppDocMap = new HashMap();

                    Boolean getCl = false;

                    switch (returnAppModel.getApp_status()) {
                        case UtimapsAction.PB_STATUS.APPLICATION_SUB:
                        case UtimapsAction.PB_STATUS.APPLICATION_SAVE:
                            getCl = false;
                            break;
                        default:
                            getCl = true;
                            break;
                    }
////                        FileModel usjIssuance = (FileModel) retrieverDAO.getModelByCode("job_id", jobModel.getJob_id(), new FileModel());
//                        FileModel usjIssuance = returnAppModel.getUSJL().get(0);
//                        appCaseMap.put("sji_id", usjIssuance.getFile_id());
//                    }

                    suppDocMap = checkSuppDoc("APP", returnAppModel, getCl);
                    appCaseMap.put("supp_doc", suppDocMap);

                    if (suppDocMap.containsKey("check_list")) {
                        appCaseMap.put("check_list", suppDocMap.get("check_list"));
                    }

                    Map miscDocMap = new HashMap();
                    miscDocMap = checkSuppDoc("SUP", returnAppModel, true);
                    appCaseMap.put("misc_doc", miscDocMap);
//
                    if (returnAppModel.getProcessingHistory() != null) {
//                        appCaseMap.put("history", checkHistory(returnAppModel));
                        //serene @ 24/6/2024 :: add history status 
                        Map historyMap = new HashMap();
                        Map rtnHistMap = new HashMap();
                        List historyList = new ArrayList();
                        historyMap.put("case_id", reqCaseId);
                        historyMap.put("cur_status", "Y");
                        List<ProcessingHistoryModel> historyModelList = retrieverDAO.list_order(historyMap, ProcessingHistoryModel.class, "order by process_date");

                        if (historyModelList.size() > 0) {
                            historyMap.clear();
                            for (ProcessingHistoryModel hist : historyModelList) {
                                ProcessingHistoryModel history = (ProcessingHistoryModel) hist;
                                historyMap = getDataMap(history, processingHistoryFields);
                                historyList.add(historyMap);
                            }
                            rtnHistMap.put("history_list", historyList);
                        }
                        appCaseMap.put("history", rtnHistMap);

                    }

                    Map returnAppMap = getDataMap(returnAppModel, appFields);
                    Map returnFirmMap = getDataMap(returnFirmModel, firmFields);
                    appCaseMap.put("util_app", returnAppMap);
                    appCaseMap.put("survey_firm", returnFirmMap);

                    appCaseMap.put("firm_name", getFirmSocName(getFirmSoc()));

                    ChecklistModel returnCheckList = new ChecklistModel();

                    returnCheckList = (ChecklistModel) retrieverDAO.getModelByCode("case_id", reqCaseId, new ChecklistModel());

                    if (returnCheckList.getCheck_id() != null) {
                        appCaseMap.put("checklist", getDataMap(returnCheckList, checklistFields));
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();

            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("docList", fetchDocList("APP"));
            jsonMap.put("appCase", appCaseMap);

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }
    }

    public void getSubmission() throws IOException {
        System.out.println("GET SJ SUBMISSION API - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "Server Not Responding.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
//        Debug.printDebug("--- setting session ---");
        Map<String, Object> histListMap = new HashMap();
        Map<String, Object> commentListMap = new HashMap();
        Map<String, Object> noticeListMap = new HashMap();
        try {
            if (Validator.isEmpty(getPbUserName()) || Validator.isEmpty(this.hValue)) {
                resStatus = 401;
                resMsg = "Required param cannot be empty.";

                try {
                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonMap));
                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }

            } else {
//                System.out.println("this userid - " + this.pbUserId);
                this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
                if (this.hValue.equals(this.hashKey)) {
                    resStatus = 200;
                    resMsg = "OK";

//                    Debug.printDebug("reqCaseId - " + reqCaseId);
                    ApplicationPModel returnAppModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", reqCaseId, new ApplicationPModel());
                    SurveyFirmPModel returnFirmModel = (SurveyFirmPModel) retrieverDAO.getModelByCode("case_id", reqCaseId, new SurveyFirmPModel());
                    JobDetailModel returnJobModel = (JobDetailModel) retrieverDAO.getModelByCode("case_id", getReqCaseId(), new JobDetailModel());

//                    if (returnAppModel.getUSJL() != null && !returnAppModel.getUSJL().isEmpty()) {
//                        FileModel usjIssuance = returnAppModel.getUSJL().get(0);
//                        subCaseMap.put("sji_id", usjIssuance.getFile_id());
                    try {
                        FileModel usjIssuance = (FileModel) retrieverDAO.getModelByCode("case_id,file_type", returnJobModel.getJob_id() + "," + "SISJL", new FileModel());
                        if (usjIssuance != null) {
                            Debug.printDebug("got? " + usjIssuance.getFile_id());
                            subCaseMap.put("sji_id", usjIssuance.getFile_id());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    }
                    Map returnLetterMap = new HashMap();
                    returnLetterMap = checkReturnLetter(retrieverDAO, returnJobModel);

//                    if(returnLetterMap)
                    Map suppDocMap = new HashMap();
                    setSwiperStep(" ");
                    suppDocMap = checkSuppDoc("USJ", returnAppModel, true);
                    subCaseMap.put("supp_doc", suppDocMap);

                    Map checkListMap = new HashMap();
                    checkListMap = (HashMap) suppDocMap.get("check_list");
                    subCaseMap.put("check_list", checkListMap);

                    Map miscDocMap = new HashMap();
                    miscDocMap = checkSuppDoc("SUP", returnAppModel, false);
                    subCaseMap.put("misc_doc", miscDocMap);

                    Map returnAppMap = getDataMap(returnAppModel, appFields);
                    Map returnFirmMap = getDataMap(returnFirmModel, firmFields);
                    subCaseMap.put("util_app", returnAppMap);
                    subCaseMap.put("survey_firm", returnFirmMap);
                    subCaseMap.put("firm_name", getFirmSocName(getFirmSoc()));

                    Map returnJobDetailMap = getDataMap(returnJobModel, jobFields);
                    subCaseMap.put("job_det", returnJobDetailMap);

                    Map travReports = checkSuppDoc("TRAV", returnAppModel, false);
                    subCaseMap.put("trav_report_docs", travReports);

                    Map hcMap = new HashMap();
                    if (returnJobModel.getControl_sv_flag().equals("Y")) {
                        if (returnJobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS50)) {
                            hcMap.put("us41", fetchHardcopyList("HCUS41"));
                        } else {
                            hcMap.put("us11", fetchHardcopyList("HCUS11"));
                            hcMap.put("us21", fetchHardcopyList("HCUS21"));
                        }
                    } else {
                        hcMap.put("us21", fetchHardcopyList("HCUS21"));
                    }

                    subCaseMap.put("hardcopy", hcMap);

                    Map localityMap = new HashMap();
                    localityMap.put("case_id", reqCaseId);
                    List<AppLocalityModel> distList = retrieverDAO.list(localityMap, AppLocalityModel.class);

                    List adList = new ArrayList();
                    List subList = new ArrayList();

                    List<AppLocalityModel> adMList = returnAppModel.getAdminDistrictList();
                    List<AppLocalityModel> subMList = returnAppModel.getSubDistrictList();
                    if (distList.size() > 0) {
                        for (AppLocalityModel alm : distList) {
                            if (alm.getAdmin_dist() != null) {
                                adList.add(getDistDesc("AD", returnAppModel.getCase_div(), alm.getAdmin_dist()));
                            } else if (alm.getSub_dist() != null) {
                                subList.add(getDistDesc("SD", returnAppModel.getCase_div(), alm.getSub_dist()));
                            }
                        }
                    }

                    subCaseMap.put("adList", adList);
                    subCaseMap.put("subList", subList);

                    ChecklistModel returnCheckList = new ChecklistModel();
                    returnCheckList = (ChecklistModel) retrieverDAO.getModelByCode("case_id", returnJobModel.getJob_id(), new ChecklistModel());
                    if (returnCheckList != null) {
//                        System.out.println("checklist added on");
                        subCaseMap.put("checklist", getDataMap(returnCheckList, checklistFields));
                    }

                    PaymentModel jobPayment = (PaymentModel) retrieverDAO.getModelByCode("case_id", getReqCaseId(), new PaymentModel());

                    if (jobPayment != null) {

                        Map pymtMap = getDataMap(jobPayment, pymtFields);
                        subCaseMap.put("job_payment", pymtMap);

                    } else {
                        System.out.println("no job payment model");
                    }

                    //serene @ 24/6/2024 :: add history status 
                    Map historyMap = new HashMap();
                    List historyList = new ArrayList();
//                    historyMap.put("case_id", reqCaseId);
                    historyMap.put("case_id", returnJobModel.getJob_id());
                    historyMap.put("cur_status", "Y");
                    List<ProcessingHistoryModel> historyModelList = retrieverDAO.list_order(historyMap, ProcessingHistoryModel.class, "order by process_date");

                    if (historyModelList.size() > 0) {
                        historyMap.clear();
                        for (ProcessingHistoryModel hist : historyModelList) {
                            ProcessingHistoryModel history = (ProcessingHistoryModel) hist;
                            historyMap = getDataMap(history, processingHistoryFields);
                            historyList.add(historyMap);
                        }
                    }
                    histListMap.put("historyList", historyList);

                    //serene @ 24/6/2024 :: load comment
                    ApplicationPModel appModel = baseDAO.getModelById(reqCaseId, ApplicationPModel.class);
                    Map commentMap = new HashMap();
                    Map commentEntry = new HashMap();
                    List commentList = new ArrayList();
                    commentMap.put("case_id", reqCaseId);
                    List<ChecklistModel> checklistModelList = retrieverDAO.list_order(commentMap, ChecklistModel.class, "order by updated_date");
                    commentMap.clear();

                    System.out.println("checking comments");
                    if (appModel != null) {
                        commentMap.put("case_id", appModel.getJob_id());
                        List<ChecklistModel> checklistJobModelList = retrieverDAO.list_order(commentMap, ChecklistModel.class, "order by updated_date");
                        if (checklistJobModelList.size() > 0) {
                            checklistModelList.addAll(checklistJobModelList);
                        }
                    }

                    if (checklistModelList.size() > 0) {
                        commentMap.clear();
                        for (ChecklistModel check : checklistModelList) {
                            ChecklistModel checklist = (ChecklistModel) check;
                            if (checklist.getCheck_by_oic() != null) {
                                String userSql = "select us_user_name from t_setup_user"
                                        + " where us_id = '" + checklist.getCheck_by_oic() + "'";
                                String userName = retrieverDAO.getSingleValue(userSql);
                                checklist.setCheck_by_oic_str(userName);
                            }

                            if (checklist.getComment_ss() != null) {
                                String userSql = "select us_user_name from t_setup_user"
                                        + " where us_id = '" + checklist.getCheck_by_ss() + "'";
                                String userName = retrieverDAO.getSingleValue(userSql);

//                                System.out.println("userName - " + userName);
                                checklist.setCheck_by_ss_str(userName);
                            }

//                            System.out.println("========================================");
                            commentMap = getDataMap(checklist, checklistFields);
                            commentList.add(commentMap);
                        }
                    }
//                    System.out.println("comment list - " + commentList);
                    commentListMap.put("commentList", commentList);

                    //serene @ 24/6/2024 :: load notification
                    Map map = new HashMap();
                    List notificationList = new ArrayList();
                    map.clear();
                    map.put("case_id", reqCaseId);

                    List<NotificationPModel> notificationPModelList = retrieverDAO.list_order(map, NotificationPModel.class, "order by updated_date");
                    System.out.println("NotificationModel :: " + notificationPModelList.size());
                    map.clear();

                    if (appModel != null) {
                        map.put("case_id", appModel.getJob_id());
                        List<NotificationPModel> notificationPJobModelList = retrieverDAO.list_order(map, NotificationPModel.class, "order by message_status_date");
                        if (notificationPJobModelList != null) {
                            notificationPModelList.addAll(notificationPJobModelList);
                        }
                    }

                    if (notificationPModelList.size() > 0) {
                        map.clear();
                        for (NotificationPModel item : notificationPModelList) {
                            NotificationPModel noticeList = (NotificationPModel) item;
                            map = getDataMap(noticeList, notificationFields);
                            notificationList.add(map);
                        }
                    }
                    noticeListMap.put("notificationList", notificationList);

                    PrecheckLog utilPcLog = (PrecheckLog) retrieverDAO.getModelByCode("job_id", returnJobModel.getJob_id(), new PrecheckLog());

                    if (utilPcLog != null) {
                        System.out.println("utilPcLog gottt");
                        String[] precheckFields = new String[]{"precheck_job_id", "job_id", "job_status", "usj_no", "no_of_errors", "tot_files", "tot_gislayer", "created_date", "created_by", "updated_date", "updated_by", "precheck_passed", "precheck_log"};
                        Map utilPcMap = getDataMap(utilPcLog, precheckFields);
                        subCaseMap.put("util_pc", utilPcMap);
                    }

                    switch (returnJobModel.getUsj_status()) {
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS10:
                            break;
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40:
                            subCaseMap.put("USCS40_QUERY", getQueryChecklist(returnJobModel.getJob_id(), "U30", "USCS40", retrieverDAO));
                            break;
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS50:
                            subCaseMap.put("USCS50_QUERY", getQueryChecklist(returnJobModel.getJob_id(), "U40", "USCS50", retrieverDAO));
                            break;
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS80:
                            subCaseMap.put("USCS80_QUERY", getQueryChecklist(returnJobModel.getJob_id(), "U50", "USCS80", retrieverDAO));
                            System.out.println("uscs80 query - " + subCaseMap.get("USCS80_QUERY").toString());
                            break;
                        default:
                            break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            jsonMap.put("public_user", fetchPbUser(this.pbUserId, retrieverDAO));
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("docList", fetchDocList("USJ"));
            jsonMap.put("travRepList", fetchDocList("TRAV"));
            jsonMap.put("histList", histListMap);
            jsonMap.put("commList", commentListMap);
            jsonMap.put("noticeList", noticeListMap);
            jsonMap.put("subCase", subCaseMap);

            retrieverDAO.closeSession();
            baseDAO.closeSession();
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }
    }

    public Map checkReturnLetter(BaseDAO rDao, JobDetailModel jobModel) {
        Map returnLetterMap = new HashMap();

        try {
            String[] uscsLetters = new String[]{"USCS10", "USCS10H", "USCS40", "USCS50", "USCS80"};

            for (String letter : uscsLetters) {
                String uscs_sql = "select file_id from us_file "
                        + "where file_type = '" + letter + "' "
                        + "and case_id = '" + jobModel.getJob_id() + "' "
                        + "AND LOWER(file_name) LIKE '%signed%' "
                        + "order by created_date desc "
                        + "FETCH FIRST 1 ROW ONLY";
                String uscs_id = rDao.getSingleValue(uscs_sql);

                if (uscs_id == null || Validator.isEmpty(uscs_id)) {
                    System.out.println("not found for - " + letter);
                } else {
                    returnLetterMap.put(letter, uscs_id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Debug.printDebug("returnLMap " + returnLetterMap);

        return returnLetterMap;
    }

    /*
        SUBMIT UTIL APPLICATION
        !!!! DEPRECATED - USING UtimapsSurveyJobApiAction for SUBMIT APPLICATION AS OF AUG 2025
     */
    public void submitApplication() throws IOException {
        System.out.println("SUBMIT APPLICATION API - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "Server Not Responding.";
        String submitMsg = "Application Submission Failed, please try again later. Thank you";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        BufferedReader rd = request.getReader();
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            content.append(line).append("\n");
        }
        rd.close();

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(content.toString());
            JSONObject jsonData = (JSONObject) jsonObject.get("upd_data");
//            System.out.println("json - " + jsonObject);

            this.pbUserId = (String) jsonObject.get("pbUserId");
            this.hValue = (String) jsonObject.get("hValue");
            this.selectStep = (String) jsonObject.get("selectStep");

            this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
            if (this.hValue.equals(this.hashKey)) {
                resStatus = 200;
                resMsg = "OK";
//                ApplicationPModel updateModel = new ApplicationPModel();
                JSONObject jsonApp = (JSONObject) jsonData.get("app");
                ApplicationPModel updateModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", jsonApp.get("case_id").toString(), new ApplicationPModel());

                //updateModel.updatableColumns = new String[]{"app_acknowl"};
//                String[] dataFields = {"case_id", "job_id", "case_type", "case_div", "case_seq", "case_year", "case_div", "usj_div", "usj_seq", "usj_year", "pj_div", "pj_name", "land_dist", "land_desc", "client_name"};
//                String[] dataFields = {"app_ackowledge"};
//                setModelData((JSONObject) jsonData.get("app"), dataFields, updateModel);
                updateModel.setApp_submit_date(DateUtil.getCurrentTimestamp());
                updateModel.setApp_acknowledge("Y");
                updateModel.setApp_status(UtimapsAction.PB_STATUS.APPLICATION_SUB);
                serviceFactory.getUtilAppService().update(updateModel);

                //############################################## Trigger ISM [Start] ##############################################
                new LogFunction().logInfo(this.getClass(), "ATTEMPT APP ISM SUBMIT WORKFLOW CALL FOR -> " + updateModel.getID());
                org.apache.logging.log4j.LogManager.getLogger(this.getClass().getSimpleName()).info("ATTEMPT APP ISM SUBMIT WORKFLOW CALL FOR -> " + updateModel.getID());
                new ISMServicesAction().triggerISMWorkflowApp(updateModel, ISMWorkflowBase.ISM_WF_STEP.SUBMIT);
                //############################################## End Tr. ISM [ End ] ##############################################
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();

            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("submit_data", submitDataMap);

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }
    }

    public void initTraversePrecheck() throws IOException {
        System.out.println("TRAVERSE PRECHECK API - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "Server Not Responding.";
        String resDesc = "";
        JSONObject jsonResponse = new JSONObject();

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
//        Debug.printDebug("--- setting session ---");
        BufferedReader rd = request.getReader();
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            content.append(line).append("\n");
        }
        rd.close();

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(content.toString());
            JSONObject jsonData = (JSONObject) jsonObject.get("upd_data");
            System.out.println("json - " + jsonObject);

            this.pbUserId = (String) jsonObject.get("pbUserId");
            this.hValue = (String) jsonObject.get("hValue");
            this.selectStep = (String) jsonObject.get("utilityStep");
            this.reqCaseId = (String) jsonObject.get("reqCaseId"); // JobDetailModel - job_id
            this.reqJobId = (String) jsonObject.get("reqJobId"); // JobDetailModel - job_id

            String securityToken = (String) jsonObject.get("security_token");
            String attachmentType = (String) jsonObject.get("attachment_type");
            JSONObject fileUpload = (JSONObject) jsonObject.get("file_upload");

            JobDetailModel jobDModel = (JobDetailModel) retrieverDAO.getModelByCode("job_id", this.reqJobId, new JobDetailModel());
//            FileModel rsoModel = (FileModel) retrieverDAO.getModelByCode("file_type,case_id,file_status", "RSO" + "," + jobDModel.getJob_id() + "," + "P", new FileModel());

//            String usjNo = setupTraverseUsjNo(jobDModel.getUsj_no());
            String usjNo = jobDModel.getUsj_seq() + jobDModel.getUsj_year();
            String divNo = jobDModel.getUsj_div();

            File rsoFile = getFileById(jobDModel.getJob_id(), "RSO", retrieverDAO);
            File fblFile = getFileById(jobDModel.getJob_id(), "FBL", retrieverDAO);
            File ps3File = getFileById(jobDModel.getJob_id(), "PS3", retrieverDAO);

            if (rsoFile != null) {
                System.out.println("rso file fetched");
                //rename file name format is divNo_usjNo_type.current file extension
//                String newFileName = divNo + "_" + usjNo + "_RSO." + rsoFile.getName().substring(rsoFile.getName().lastIndexOf('.') + 1);
//                rsoFile.renameTo(new File(rsoFile.getParent(), newFileName));
            }
            if (fblFile != null) {
                System.out.println("fbl file fetched");
                //rename file name format is divNo_usjNo_type.current file extension
//                String newFileName = divNo + "_" + usjNo + "_FBL." + fblFile.getName().substring(rsoFile.getName().lastIndexOf('.') + 1);
//                fblFile.renameTo(new File(fblFile.getParent(), newFileName));
            }
            if (ps3File != null) {
                System.out.println("ps3 file fetched");
                //rename file name format is divNo_usjNo_type.current file extension
//                String newFileName = divNo + "_" + usjNo + "_PS3." + ps3File.getName().substring(rsoFile.getName().lastIndexOf('.') + 1);
//                ps3File.renameTo(new File(ps3File.getParent(), newFileName));
            }

            File[] postFileSet = {
                rsoFile, fblFile, ps3File
            };

            //before postFormData, need to ensure that the bearer token exists in the ActionContext
            // String postReturn = postFormData(usjNo, divNo, postFileSet);
            String token = "";
            if (ActionContext.getContext().getSession().containsKey("bearer_token")) {
//                Debug.printDebug("token in session");
                token = ActionContext.getContext().getSession().get("bearer_token").toString();
            } else {
//                Debug.printDebug("no token in session");
                token = getBearerToken();

                if (token.equals("CONN_FAIL") || token.equals("CONN_TIMEOUT")) {
                    resStatus = 500;
                    resMsg = "CATCH_FAIL";
                    resDesc = "Connection to authentication server failed, please try again later.";

                    retrieverDAO.closeSession();

                    jsonResponse.put("res_status", resStatus);
                    jsonResponse.put("res_message", resMsg);
                    jsonResponse.put("res_desc", resDesc);

                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonResponse));
                    response.flushBuffer();

                    return;
                }

                ActionContext.getContext().getSession().put("bearer_token", token);
            }

            if (token != null && !Validator.isEmpty(token)) {
                Debug.printDebug("session check - " + ActionContext.getContext().getSession());
            }

            String postReturn = postFormData(usjNo, divNo, fblFile, rsoFile, ps3File, token);

            if (postReturn != null && !Validator.isEmpty(postReturn)) {
                // json response
                JSONObject pcJsonResponse = (JSONObject) new JSONParser().parse(postReturn);
                Debug.printDebug("json response - " + pcJsonResponse);
                String initStatus = (String) pcJsonResponse.get("status");

                if ("success".equals(initStatus)) {
                    System.out.println(" all ok ");
                    resStatus = 200;
                    resMsg = "OK";

                    try {
                        jobDModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_PRECHECK_STAGE);

                        if (jobDModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40)
                                || jobDModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV)) {
                            jobDModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_STARTED);
                        } else {
                            jobDModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.TRAV_STARTED);
                        }

                        serviceFactory.getSubmissionJobService().updateSubmission(jobDModel);

                        jobDModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_TRAV_START);
                        jobDModel.setTrav_pc_start_date(new java.sql.Timestamp(System.currentTimeMillis()));
                        serviceFactory.getSubmissionJobService().updateSubmission(jobDModel);

                        // ON INITIATION OF TRAVERSE PRECHECK -> DELETE OLD HISTORY RECORDS
                        Map oldHistMap = new HashMap();
                        oldHistMap.put("job_id", jobDModel.getJob_id());
                        oldHistMap.put("precheck_type", "T");
                        List<PrecheckHistoryModel> oldHistList = retrieverDAO.list(oldHistMap, PrecheckHistoryModel.class);

                        if (oldHistList.size() > 0) {
                            for (PrecheckHistoryModel hist : oldHistList) {
                                serviceFactory.getPrecheckHistoryService().delete(hist.getTask_id(), hist);
                            }
                        }

//                        PrecheckHistoryModel insertHistory = new PrecheckHistoryModel();
//                        insertHistory.setTask_id(com.sains.framework.base.CommonFunction.getId(20));
//                        insertHistory.setJob_id(jobDModel.getJob_id());
//                        insertHistory.setPrecheck_type("T");
//                        insertHistory.setTask_seq(TRAV_HISTORY_TASK.INIT.task_seq);
//                        insertHistory.setTask_desc(TRAV_HISTORY_TASK.INIT.task_desc);
//                        insertHistory.setCreated_by("BACKEND");
//                        insertHistory.setCreated_date(new java.sql.Timestamp(System.currentTimeMillis()));
//
//                        serviceFactory.getPrecheckHistoryService().insert(insertHistory);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resStatus = 500;
                    resMsg = "CATCH_FAIL";

                    String initError = (String) pcJsonResponse.get("message");
                    resDesc = initError;
                }

            } else {
                // Trigger catch handling for null or empty postReturn
                throw new Exception("Post return is null or empty");
            }

            jsonResponse.put("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            resStatus = 500;
            resMsg = "CATCH_FAIL";
        } finally {
            retrieverDAO.closeSession();

            jsonResponse.put("res_status", resStatus);
            jsonResponse.put("res_message", resMsg);
            jsonResponse.put("res_desc", resDesc);

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonResponse));
            response.flushBuffer();
        }
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

    // option 1 for setting up traverse usj no
    public String setupTraverseUsjNo(String input) {
        // Extract only the job number and year
        String[] parts = input.split("/");
        if (parts.length < 4) {
            return ""; // Return empty string if input format is incorrect
        }

        String jobNo = parts[2];
        String year = parts[3];

        // Ensure job number is 4 digits by padding with zeros
        jobNo = String.format("%04d", Integer.parseInt(jobNo));

        return jobNo + year;
    }

    public String getBearerToken() throws IOException {
        String token = (String) ActionContext.getContext().get("security_token");

        if (token == null || Validator.isEmpty(token)) {
//            //call the login API
//            String loginUrl = "https://localhost:8080/esub/api/auth"; // Placeholder URL
//            HttpClient httpClient = HttpClients.createDefault();
//            HttpPost httpPost = new HttpPost(loginUrl);
//
//            // Set up form data
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("LOGIN", "utimaps_api_2024"));  // Leave value empty for now
//            params.add(new BasicNameValuePair("PWD", "UTiMAPS@2024"));    // Leave value empty for now
//            httpPost.setEntity(new UrlEncodedFormEntity(params));
//
//            // Execute the request
//            HttpResponse httpResponse = httpClient.execute(httpPost);
//
//            // Process the response
//            if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                HttpEntity entity = httpResponse.getEntity();
//                String responseString = EntityUtils.toString(entity, "UTF-8");
//                // Parse the response to get the token
//                // This is a placeholder - you'll need to implement the actual parsing logic
//                // token = parseTokenFromResponse(responseString);
//                //token is from the response header
//                token = httpResponse.getFirstHeader("Authorization").getValue();
//            } else {
//                System.out.println("Failed to obtain token. Status code: " + httpResponse.getStatusLine().getStatusCode());
//            }

            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost post = new HttpPost(SystemConstants.DOMAIN.domain + "scs/esub/api/auth"); // SCS AUTH API

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("LOGIN", "utimaps_api_2024"));
                params.add(new BasicNameValuePair("PWD", "UTiMAPS@2024"));
                post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

                try (CloseableHttpResponse response = client.execute(post)) {
                    HttpEntity responseEntity = response.getEntity();
                    // get token from response header
                    if (responseEntity != null) {
                        token = response.getFirstHeader("Authorization").getValue();
                    } else {
                        return "CONN_FAIL";
                    }
                } catch (IOException e) {
                    Debug.printDebug("Connection failed or timed out: " + e.getMessage());
                    return "CONN_TIMEOUT";
                }
            } catch (Exception e) {
                Debug.printDebug("Error creating HTTP client: " + e.getMessage());
                return "CONN_FAIL";
            }
        }

        return token;
    }

    public String postFormData(String usj_no, String div_no, File FBL, File RSO, File PS3, String token) throws IOException {
        CloseableHttpClient client = null;
        try {
            client = HttpClients.createDefault();
//            HttpPost post = new HttpPost("http://localhost:8080/esub/api/precheck/init"); // DUMMY TRAVERSE PC API
            HttpPost post = new HttpPost(SystemConstants.DOMAIN.domain + "scs/utilityControlPrecheck"); // SCS TNT

//            Debug.printDebug("token? in post form data - " + token);
            post.setHeader(HttpHeaders.AUTHORIZATION, token);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("usj_no", new StringBody(usj_no, ContentType.TEXT_PLAIN));
            builder.addPart("div_no", new StringBody(div_no, ContentType.TEXT_PLAIN));

            File renamedFBL = null, renamedPS3 = null, renamedRSO = null;
            try {
                renamedFBL = renameFile(FBL, div_no + "_" + usj_no + "_FBL." + getFileExtension(FBL));
                builder.addPart("FBL", new FileBody(renamedFBL, ContentType.DEFAULT_BINARY));

                renamedPS3 = renameFile(PS3, div_no + "_" + usj_no + "_PS3." + getFileExtension(PS3));
                builder.addPart("PS3", new FileBody(renamedPS3, ContentType.DEFAULT_BINARY));

                renamedRSO = renameFile(RSO, div_no + "_" + usj_no + "_RSO." + getFileExtension(RSO));
                builder.addPart("Survey_Job", new FileBody(renamedRSO, ContentType.DEFAULT_BINARY));

                HttpEntity multipart = builder.build();
                post.setEntity(multipart);

                try (CloseableHttpResponse response = client.execute(post)) {
//                    Debug.printDebug("resp 1 " + response.getStatusLine().getStatusCode());
                    HttpEntity responseEntity = response.getEntity();
                    if (responseEntity != null) {
                        String responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
//                        Debug.printDebug("res str - " + responseString);
                        return responseString;
                    }
                    return "";
                }
            } catch (IOException e) {
                Debug.printDebug("Error during file operations or HTTP request: " + e.getMessage());
                throw e;
            } finally {
                // Delete the renamed files
                deleteFileIfExists(renamedFBL);
                deleteFileIfExists(renamedPS3);
                deleteFileIfExists(renamedRSO);
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

    public String getTraverseStatus(String usjNo, String divNo) {
//        Debug.printDebug("GET TRAVERSE STATUS SCS CALL");
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

    private void deleteFileIfExists(File file) {
        if (file != null && file.exists()) {
            if (!file.delete()) {
                Debug.printDebug("Failed to delete file: " + file.getAbsolutePath());
            }
        }
    }

    private File renameFile(File originalFile, String newName) throws IOException {
        File newFile = new File(originalFile.getParent(), newName);
        Files.copy(originalFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return newFile;
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // Empty extension
        }
        return name.substring(lastIndexOf + 1);
    }

    // Get [PRECHECK] Status
    public void getPrecheckStatus() throws IOException {
        System.out.println("GET PRECHECK STATUS API - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "Server Not Responding.";
        JSONObject jsonResponse = new JSONObject();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
//        Debug.printDebug("--- setting session ---");
        BufferedReader rd = request.getReader();
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            content.append(line).append("\n");
        }
        rd.close();
        dataMap.clear();

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(content.toString());
            JSONObject jsonData = (JSONObject) jsonObject.get("upd_data");
            //System.out.println("json - " + jsonObject);

            this.pbUserId = (String) jsonObject.get("pbUserId");
            this.hValue = (String) jsonObject.get("hValue");
            this.selectStep = (String) jsonObject.get("utilityStep");
            this.reqCaseId = (String) jsonObject.get("reqCaseId"); // JobDetailModel - case_id
            this.reqJobId = (String) jsonObject.get("reqJobId"); // JobDetailModel - job_id

            this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
            //System.out.println("this hashkey - " + this.hashKey);
            if (this.hValue.equals(this.hashKey)) {
                //System.out.println("OK!");
                resStatus = 200;
                resMsg = "OK";

                //here - request body for calling SCS API                
                //waiting for SCS precheck result API
                //System.out.println("reqJobId - " + getReqJobId());
                PrecheckLog utilPcLog = (PrecheckLog) retrieverDAO.getModelByCode("job_id", this.reqJobId, new PrecheckLog());
                JobDetailModel jobDetailModel = (JobDetailModel) retrieverDAO.getModelByCode("job_id", this.reqJobId, new JobDetailModel());
                String[] precheckFields = new String[]{"precheck_job_id", "job_id", "job_status", "usj_no", "no_of_errors", "tot_files", "tot_gislayer", "created_date", "created_by", "updated_date", "updated_by", "precheck_passed", "precheck_log"};

                //System.out.println("job detail stage - " + jobDetailModel.getPrecheck_stage());
                // NO TRAVERSE CASE - FETCH UTIL PC STATUS ONLY
                if (jobDetailModel.getControl_sv_flag().equals("N") || jobDetailModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK)) {

                    if (utilPcLog != null) {
                        //System.out.println("util id " + utilPcLog.getJob_id());
                        Map utilPcMap = getDataMap(utilPcLog, precheckFields);
                        dataMap.put("util_status", utilPcMap);

                        switch (utilPcLog.getJob_status()) {
                            case UtimapsAction.UTIL_PRECHECK_JOB_STATUS.SUBMITTED:
                            case UtimapsAction.UTIL_PRECHECK_JOB_STATUS.EXECUTING:
//                            if (jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.TRAV_COMPLETE)) {
                                //System.out.println("switching off util pc need run flag");
                                try {
                                    jobDetailModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_PRECHECK_STAGE);

                                    if (jobDetailModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK)) {
                                        jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_STARTED);
                                    } else {
                                        jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.UTIL_STARTED);
                                    }

                                    serviceFactory.getSubmissionJobService().updateSubmission(jobDetailModel);

                                    if (!jobDetailModel.getPrecheck_notif_sent().equals("N")) {
                                        Debug.printDebug("set not sent precheck notif");
                                        jobDetailModel.updatableColumns = new String[]{"job_id", "precheck_notif_sent"};
                                        jobDetailModel.setPrecheck_notif_sent("N");
                                        retrieverDAO.update(jobDetailModel);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                            }
                                break;
                            case UtimapsAction.UTIL_PRECHECK_JOB_STATUS.SUCCESS:
                                try {
                                    jobDetailModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_PRECHECK_STAGE);

                                    if (utilPcLog.getPrecheck_passed().equals("Y")) {
                                        if (jobDetailModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK)) {
                                            jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_PASS);
                                        } else {
                                            jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.SUCCESS);
                                        }
                                    } else {
                                        if (jobDetailModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK)) {
                                            jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_FAIL);
                                        } else {
                                            jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.FAIL);
                                        }
                                    }

                                    serviceFactory.getSubmissionJobService().updateSubmission(jobDetailModel);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case UtimapsAction.UTIL_PRECHECK_JOB_STATUS.FAIL:
                                try {
                                    jobDetailModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_PRECHECK_STAGE);
                                    jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.FAIL);
                                    serviceFactory.getSubmissionJobService().updateSubmission(jobDetailModel);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }

                        Map historyMap = new HashMap();
                        Map rtnHistMap = new HashMap();
                        List historyList = new ArrayList();

                        Map pcHistMap = new HashMap();
                        pcHistMap.put("job_id", utilPcLog.getJob_id());
                        pcHistMap.put("precheck_type", "U");
                        List<PrecheckHistoryModel> pcHistList = new ArrayList();
                        pcHistList = retrieverDAO.list_order(pcHistMap, PrecheckHistoryModel.class, "");

                        if (!pcHistList.isEmpty()) {
                            historyMap.clear();
                            for (PrecheckHistoryModel hist : pcHistList) {
                                PrecheckHistoryModel history = (PrecheckHistoryModel) hist;
                                historyMap = getDataMap(history, precheckHistoryFields);
//                                System.out.println("hist map - " + historyMap);
                                historyList.add(historyMap);
                            }
//                        rtnHistMap.put("history_list", historyList);
                            dataMap.put("util_history", historyList);
                        }
                    } else {
                        System.out.println("pclog null");
                    }

                    // fetch details on [TRAVERSE] [PRECHECK] for RO Failure Query
                    if (jobDetailModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK)) {
                        Map travStatusMap = new HashMap();
                        //do scs precheck status check
                        String token = getBearerToken();
                        String usjNo = jobDetailModel.getUsj_seq() + jobDetailModel.getUsj_year();
                        String divNo = jobDetailModel.getUsj_div();

                        String statusCheckUrl = SystemConstants.DOMAIN.domain + "scs/UtilityControlPrecheckResult?usj_no=" + usjNo + "&div_no=" + divNo;

                        try (CloseableHttpClient client = HttpClients.createDefault()) {
                            HttpGet httpGet = new HttpGet(statusCheckUrl);
                            httpGet.setHeader(HttpHeaders.AUTHORIZATION, token);

                            try (CloseableHttpResponse statusHttpRes = client.execute(httpGet)) {
                                int statusCode = statusHttpRes.getStatusLine().getStatusCode();
                                String responseBody = EntityUtils.toString(statusHttpRes.getEntity());

                                if (statusCode == 200) {
                                    JSONObject statusJson = (JSONObject) new JSONParser().parse(responseBody);
//                                    Debug.printDebug("status json - " + statusJson.toString());

                                    if (statusJson.containsKey("status")) {
                                        String precheckStatus = (String) statusJson.get("status");
//                                        Debug.printDebug("precheck status - " + precheckStatus);

                                        JSONObject result = (JSONObject) statusJson.get("result");
                                        JSONObject status = (JSONObject) result.get("status");
                                        String resultStatus = (String) status.get("Status");
                                        travStatusMap.put("pc_status", precheckStatus);
                                        travStatusMap.put("pc_task", resultStatus);
                                        travStatusMap.put("pc_start_date", jobDetailModel.getTrav_pc_start_date());
                                        travStatusMap.put("pc_last_date", jobDetailModel.getTrav_pc_completed_date());
                                        travStatusMap.put("pc_status_json", status);

//                                        if (jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_PASS)
//                                                || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_PASS)) {
                                        ApplicationPModel returnAppModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", jobDetailModel.getCase_id(), new ApplicationPModel());
                                        Map travReports = checkSuppDoc("TRAV", returnAppModel, false);
                                        travStatusMap.put("pc_report_files", travReports);

//                                        }
                                        List travHistoryList = new ArrayList();
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

//                                            Debug.printDebug("trav hist - " + travHistoryList);
                                            dataMap.put("trav_history", travHistoryList);
                                        }
                                    } else {
                                        Debug.printDebug("No status found in response body?");
                                    }

                                    dataMap.put("trav_status", travStatusMap);
                                } else {
//                                    Debug.printDebug("Failed to get precheck status. Status code: " + statusCode);

                                    List travHistoryList = new ArrayList();
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

                                        Debug.printDebug("trav hist - " + travHistoryList);
                                        dataMap.put("trav_history", travHistoryList);
                                    } else {
                                        Debug.printDebug("no trav history?");
                                    }

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Debug.printDebug("Error checking precheck status: " + e.getMessage());
                        }
                    }

                } else { // FETCH TRAVERSE AND UTIL PC STATUS
                    // precheck traverse running and handling start
                    if (jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.TRAV_STARTED)
                            || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_STARTED)) {
                        Map travStatusMap = new HashMap();
                        //do scs precheck status check
                        String token = getBearerToken();
                        String usjNo = jobDetailModel.getUsj_seq() + jobDetailModel.getUsj_year();
                        String divNo = jobDetailModel.getUsj_div();
                        Boolean travPass = false;
                        String statusCheckUrl = SystemConstants.DOMAIN.domain + "scs/UtilityControlPrecheckResult?usj_no=" + usjNo + "&div_no=" + divNo;

                        List travHistoryList = new ArrayList();
                        Map travHistoryMap = new HashMap();
                        Map pcHistMap = new HashMap();
                        pcHistMap.put("job_id", jobDetailModel.getJob_id());
                        pcHistMap.put("precheck_type", "T");
                        List<PrecheckHistoryModel> travHistList = new ArrayList();
                        travHistList = retrieverDAO.list_order(pcHistMap, PrecheckHistoryModel.class, "order by created_date");

                        // switch over - now hinges on last entry for Traverse PrecheckHistoryModel list
                        if (!travHistList.isEmpty()) {
                            travHistoryMap.clear();
                            for (PrecheckHistoryModel hist : travHistList) {
                                PrecheckHistoryModel history = (PrecheckHistoryModel) hist;
                                travHistoryMap = getDataMap(history, precheckHistoryFields);
                                travHistoryList.add(travHistoryMap);
                            }

                            //get last entry of travHistoryList
                            PrecheckHistoryModel lastEntry = (PrecheckHistoryModel) travHistList.get(travHistoryList.size() - 1);
//                            Debug.printDebug("last trav hist - " + lastEntry.getTask_seq() + "---" + lastEntry.getTask_desc());

                            // check traverse signal from last entry
                            String travSignal = lastEntry.getTraverse_signal();
                            // Debug.printDebug("trav signal - " + travSignal);

                            if (!travSignal.equals("I")) {
//                                Debug.printDebug("trav signal is not I");
                                // FETCH TRAV PRECHECK STATUS/RESULT
                                try (CloseableHttpClient client = HttpClients.createDefault()) {
                                    HttpGet httpGet = new HttpGet(statusCheckUrl);
                                    httpGet.setHeader(HttpHeaders.AUTHORIZATION, token);

                                    try (CloseableHttpResponse statusHttpRes = client.execute(httpGet)) { // <-- RESULT API CALL ATTEMPT
                                        int statusCode = statusHttpRes.getStatusLine().getStatusCode();
                                        String responseBody = EntityUtils.toString(statusHttpRes.getEntity());

                                        if (statusCode == 200) {
                                            JSONObject statusJson = (JSONObject) new JSONParser().parse(responseBody);
//                                            Debug.printDebug("status json - " + statusJson.toString());

                                            if (statusJson.containsKey("status")) {
                                                String precheckStatus = (String) statusJson.get("status");
                                                if ("success".equals(precheckStatus)) {
//                                                    Debug.printDebug("precheck status - " + precheckStatus);

                                                    JSONObject result = (JSONObject) statusJson.get("result");

                                                    JSONObject status = (JSONObject) result.get("status");
                                                    travStatusMap.put("pc_status_json", status);

                                                    String resultStatus = (String) status.get("Status");
                                                    if ("P".equals(resultStatus)) {

                                                        jobDetailModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_PRECHECK_STAGE);
                                                        if (jobDetailModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40)) {
                                                            jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_PASS);
                                                        } else {
                                                            jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.TRAV_COMPLETE);
                                                        }

                                                        serviceFactory.getSubmissionJobService().updateSubmission(jobDetailModel);
                                                        travStatusMap.put("pc_status", precheckStatus);
                                                        travStatusMap.put("pc_task", resultStatus);
//                                                        Debug.printDebug("Precheck status - " + resultStatus + ". Updated to TRAV_COMPLETE.");

                                                        jobDetailModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_TRAV_COMPLETE);
                                                        jobDetailModel.setTrav_pc_completed_date(new java.sql.Timestamp(System.currentTimeMillis()));
                                                        serviceFactory.getSubmissionJobService().updateSubmission(jobDetailModel);

                                                        travPass = true;

                                                        travStatusMap.put("pc_start_date", jobDetailModel.getTrav_pc_start_date());
                                                        travStatusMap.put("pc_last_date", jobDetailModel.getTrav_pc_completed_date());
                                                    } else {
                                                        jobDetailModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_PRECHECK_STAGE);
                                                        if (jobDetailModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40)) {
//                                                            Debug.printDebug("USCS40 SET TO TRAV QUERY FAIL");
                                                            jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_FAIL);
                                                        } else {
//                                                            Debug.printDebug("ELSE SET TO REGULAR FAIL");
                                                            jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.FAIL);
                                                        }

                                                        serviceFactory.getSubmissionJobService().updateSubmission(jobDetailModel);
                                                        travStatusMap.put("pc_status", precheckStatus);
                                                        travStatusMap.put("pc_task", resultStatus);
//                                                        Debug.printDebug("Precheck status - " + resultStatus + ". Updated to FAIL.");

                                                        jobDetailModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_TRAV_COMPLETE);
                                                        jobDetailModel.setTrav_pc_completed_date(new java.sql.Timestamp(System.currentTimeMillis()));
                                                        serviceFactory.getSubmissionJobService().updateSubmission(jobDetailModel);

                                                        travStatusMap.put("pc_start_date", jobDetailModel.getTrav_pc_start_date());
                                                        travStatusMap.put("pc_last_date", jobDetailModel.getTrav_pc_completed_date());
                                                    }

                                                }

                                                ApplicationPModel returnAppModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", jobDetailModel.getCase_id(), new ApplicationPModel());
                                                Map travReports = checkSuppDoc("TRAV", returnAppModel, false);
                                                travStatusMap.put("pc_report_files", travReports);
                                            } else {
//                                                Debug.printDebug("NO STATUS YET");
                                            }

                                            dataMap.put("trav_status", travStatusMap);
                                        } else {
//                                            Debug.printDebug("Failed to get precheck status. Status code: " + statusCode);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
//                                    Debug.printDebug("Error checking precheck status: " + e.getMessage());
                                }
                            } else {
//                                Debug.printDebug("message says go on");
                            }

//                            Debug.printDebug("trav hist - " + travHistoryList);
                            dataMap.put("trav_history", travHistoryList);
                        }

                        // fetch previous UTIL PRECHECK RESULTS FOR 042
                        if (jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_STARTED)) {
                            if (utilPcLog != null) {
                                System.out.println("util id " + utilPcLog.getJob_id());
                                Map utilPcMap = getDataMap(utilPcLog, precheckFields);
                                dataMap.put("util_status", utilPcMap);

                                Map historyMap = new HashMap();
                                List historyList = new ArrayList();

                                pcHistMap = new HashMap();
                                pcHistMap.put("job_id", utilPcLog.getJob_id());
                                pcHistMap.put("precheck_type", "U");
                                List<PrecheckHistoryModel> pcHistList = new ArrayList();
                                pcHistList = retrieverDAO.list_order(pcHistMap, PrecheckHistoryModel.class, "");

                                if (!pcHistList.isEmpty()) {
                                    historyMap.clear();
                                    for (PrecheckHistoryModel hist : pcHistList) {
                                        PrecheckHistoryModel history = (PrecheckHistoryModel) hist;
                                        historyMap = getDataMap(history, precheckHistoryFields);
                                        historyList.add(historyMap);
                                    }
                                    dataMap.put("util_history", historyList);
                                }
                            } else {
                                System.out.println("pclog null");
                            }

                        }

                        // precheck traverse running and handling end
                    } else if (jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.UTIL_STARTED)
                            || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.UTIL_HOLD)
                            || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.UTIL_RERUN_PENDING)
                            || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.FAIL)
                            || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.SUCCESS)
                            || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV)
                            || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_FAIL)
                            || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_PASS)) {
                        Boolean travPass = false;

                        if (jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV)) {
                            //blank trav status - querying back traverse
                        } else {
                            Map travStatusMap = new HashMap();
                            //do scs precheck status check
                            // String token = getBearerToken();
                            // String usjNo = jobDetailModel.getUsj_seq() + jobDetailModel.getUsj_year();
                            // String divNo = jobDetailModel.getUsj_div();

                            // String statusCheckUrl = SystemConstants.DOMAIN.domain + "scs/UtilityControlPrecheckResult?usj_no=" + usjNo + "&div_no=" + divNo;
                            try {
                                String usjNo = jobDetailModel.getUsj_seq() + jobDetailModel.getUsj_year();
                                String divNo = jobDetailModel.getUsj_div();
                                String responseBody = getTraverseStatus(usjNo, divNo);

                                if (!responseBody.isEmpty()) {
                                    JSONObject statusJson = (JSONObject) new JSONParser().parse(responseBody);
//                                    Debug.printDebug("status json - " + statusJson.toString());

                                    if (statusJson.containsKey("status")) {
                                        String precheckStatus = (String) statusJson.get("status");
//                                        Debug.printDebug("precheck status - " + precheckStatus);

                                        JSONObject result = (JSONObject) statusJson.get("result");
                                        JSONObject status = (JSONObject) result.get("status");
                                        String resultStatus = (String) status.get("Status");
                                        travStatusMap.put("pc_status", precheckStatus);
                                        travStatusMap.put("pc_task", resultStatus);
                                        travStatusMap.put("pc_start_date", jobDetailModel.getTrav_pc_start_date());
                                        travStatusMap.put("pc_last_date", jobDetailModel.getTrav_pc_completed_date());
                                        travStatusMap.put("pc_status_json", status);

                                        if ("P".equals(resultStatus)) {
                                            travPass = true;
                                        }

                                        if (jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_PASS)
                                                || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_FAIL)
                                                || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.UTIL_STARTED)
                                                || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.FAIL)
                                                || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.SUCCESS)) {
                                            //FETCH REPORTS

                                            Debug.printDebug("FETCH REPORTS");
                                            ApplicationPModel returnAppModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", jobDetailModel.getCase_id(), new ApplicationPModel());
                                            Map travReports = checkSuppDoc("TRAV", returnAppModel, false);

                                            System.out.println("reports - " + travReports);
                                            travStatusMap.put("pc_report_files", travReports);

                                        }

                                        List travHistoryList = new ArrayList();
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

//                                            Debug.printDebug("trav hist - " + travHistoryList);
                                            dataMap.put("trav_history", travHistoryList);
                                        }

                                    } else {
//                                        Debug.printDebug("Status key not found in response");
                                    }

                                    dataMap.put("trav_status", travStatusMap);
                                } else {
//                                    Debug.printDebug("Failed to get precheck status. Empty response.");

                                    List travHistoryList = new ArrayList();
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

//                                        Debug.printDebug("trav hist - " + travHistoryList);
                                        dataMap.put("trav_history", travHistoryList);
                                    } else {
//                                        Debug.printDebug("no trav history?");
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
//                                Debug.printDebug("Error checking precheck status: " + e.getMessage());
                            }

                        }

                        // FETCH UTIL PRECHECK STATUS/RESULT
                        if (!jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.UTIL_HOLD)) {
                            if (utilPcLog != null) {
                                System.out.println("util id " + utilPcLog.getJob_id());
                                Map utilPcMap = getDataMap(utilPcLog, precheckFields);
                                dataMap.put("util_status", utilPcMap);

                                if (jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV)
                                        || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_STARTED)
                                        || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_FAIL)
                                        || jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_PASS)) {
//                                    Debug.printDebug("USCS40 TRAV QUERY / PASS / FAIL SCENARIO");
                                    //USCS40 don't change the precheck stage when retrieving utility pc status
                                } else {
                                    switch (utilPcLog.getJob_status()) {
                                        case UtimapsAction.UTIL_PRECHECK_JOB_STATUS.SUBMITTED:
                                        case UtimapsAction.UTIL_PRECHECK_JOB_STATUS.EXECUTING:
//                            if (jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.TRAV_COMPLETE)) {
                                            System.out.println("switching off util pc need run flag");
                                            try {
                                                jobDetailModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_PRECHECK_STAGE);
                                                jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.UTIL_STARTED);
                                                serviceFactory.getSubmissionJobService().updateSubmission(jobDetailModel);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
//                            }
                                            break;
                                        case UtimapsAction.UTIL_PRECHECK_JOB_STATUS.SUCCESS:
                                            try {
                                                jobDetailModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_PRECHECK_STAGE);

                                                if (utilPcLog.getPrecheck_passed().equals("Y") && travPass) {
//                                                    Debug.printDebug("full pass");
                                                    jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.SUCCESS);
                                                } else {
                                                    if (utilPcLog.getPrecheck_passed().equals("Y")) {
                                                        dataMap.remove("util_status");
                                                    }

                                                    if (travPass) {
//                                                        Debug.printDebug("FOUND TRAV PASS + UTIL FAIL = FAIL TO RERUN");
                                                        jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.UTIL_RERUN_PENDING);
                                                    } else {
                                                        jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.FAIL);
                                                    }
                                                }

                                                serviceFactory.getSubmissionJobService().updateSubmission(jobDetailModel);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case UtimapsAction.UTIL_PRECHECK_JOB_STATUS.FAIL:
                                            try {
                                                jobDetailModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_PRECHECK_STAGE);

                                                if (travPass) {
//                                                    Debug.printDebug("FOUND TRAV PASS + UTIL FAIL = FAIL TO RERUN");
                                                    jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.UTIL_RERUN_PENDING);
                                                } else {
                                                    jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.FAIL);
                                                }

                                                serviceFactory.getSubmissionJobService().updateSubmission(jobDetailModel);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                    }
                                }

                                Map historyMap = new HashMap();
                                Map rtnHistMap = new HashMap();
                                List historyList = new ArrayList();

                                Map pcHistMap = new HashMap();
                                pcHistMap.put("job_id", utilPcLog.getJob_id());
                                pcHistMap.put("precheck_type", "U");
                                List<PrecheckHistoryModel> pcHistList = new ArrayList();
                                pcHistList = retrieverDAO.list_order(pcHistMap, PrecheckHistoryModel.class, "");

                                if (!pcHistList.isEmpty()) {
                                    historyMap.clear();
                                    for (PrecheckHistoryModel hist : pcHistList) {
                                        PrecheckHistoryModel history = (PrecheckHistoryModel) hist;
                                        historyMap = getDataMap(history, precheckHistoryFields);
//                                System.out.println("hist map - " + historyMap);
                                        historyList.add(historyMap);
                                    }
//                        rtnHistMap.put("history_list", historyList);
                                    dataMap.put("util_history", historyList);
                                }
                            } else {
                                System.out.println("pclog null");
                            }
                        }
                    } else if (jobDetailModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.TRAV_COMPLETE)) {
                        if (!jobDetailModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40)) {
                            try {
                                jobDetailModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_PRECHECK_STAGE);
                                jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.UTIL_HOLD);
                                serviceFactory.getSubmissionJobService().updateSubmission(jobDetailModel);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            System.out.println("COMPLETE TRAVERSE - INITIATE UTIL PRECHECK");
                            Map utilInitMap = new HashMap();
                            String utilInitStr = initUtilPrecheck(jobDetailModel);
                            JSONObject initObj = (JSONObject) new JSONParser().parse(utilInitStr);

                            String sMsg = "";

                            if (initObj.containsKey("message")) {
                                sMsg = "OK";
                            } else {
                                sMsg = "Precheck Connection Failed! Please Try Again Later.";
                            }

                            Boolean sStatus = false;
                            if (initObj.containsKey("success")) {
                                sStatus = Boolean.valueOf(initObj.get("success").toString());
                            }

                            if (sStatus) {
                                JSONObject jobResponse = (JSONObject) initObj.get("jobResponse");
                                String message = "Precheck Connection OK!";

                                System.out.println("==========================================");
                                System.out.println("UTIL PRECHECK MESSAGE - " + message);
                                System.out.println("==========================================");

                                try {
                                    jobDetailModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_PRECHECK_STAGE);
                                    jobDetailModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.UTIL_STARTED);
                                    serviceFactory.getSubmissionJobService().updateSubmission(jobDetailModel);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                resStatus = 200;
                                resMsg = sMsg;
                            } else {
                                resStatus = 500;
                                resMsg = sMsg;
                            }

                            utilInitMap.put("res", resStatus);
                            utilInitMap.put("msg", resMsg);
                            dataMap.put("initiated_util", utilInitMap);
                        }
                    }
                }

                dataMap.put("precheck_stage", jobDetailModel.getPrecheck_stage());
                dataMap.put("usj_no", jobDetailModel.getUsj_no());
                precheckMap.put("data", dataMap);

                PrecheckServiceAction psAction = new PrecheckServiceAction();
                psAction.handlePrecheckCompletedEmail(jobDetailModel, retrieverDAO);
                psAction.closeSession();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();

            precheckMap.put("res_status", resStatus);
            precheckMap.put("res_message", resMsg);

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(precheckMap));
            response.flushBuffer();
        }
    }

//    public void handlePrecheckCompletedEmail(JobDetailModel jobModel, BaseDAO retrieverDAO) {
//        Map mailJsonMap = new HashMap();
//        AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
//        autoEmailDAO.setSession(retrieverDAO.getSession());
//        AutoEmail autoEmail = null;
//        AutoEmail autoEmailHc = null;
//        FtpInterface ftp = FileOperationUtil.getFtpInterface();
//
//        Boolean passed = true;
//
//        if (jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.FAIL)
//                || jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_FAIL)
//                || jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_FAIL)) {
//            passed = false;
//        }
//
//        if (jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.FAIL)
//                || jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.SUCCESS)
//                || jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_FAIL)
//                || jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_PASS)
//                || jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_FAIL)
//                || jobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_PASS)) {
//
//            if (jobModel.getPrecheck_notif_sent().equals("N")) {
//                Debug.printDebug("will send precheck notif");
//
//                try {
//                    CommonFunction.writeFile("UtimapsJobApi", "send precheck completed email to public user");
//
//                    String publicUserName = "";
//                    String publicUserId = "";
//                    String publicUserEmail = "default@utimaps.sains.com.my";
//                    String surveyFirmUserName = "";
//                    String surveyFirmEmail = "";
//
//                    PublicUserModel publicUser = (PublicUserModel) retrieverDAO.getModelByCode("us_user_id", jobModel.getApplicationModel().getApp_submit_by(), new PublicUserModel());
//
//                    if (publicUser != null) {
//                        publicUserName = publicUser.getUs_user_name();
//                        publicUserId = publicUser.getUs_user_id();
//                        publicUserEmail = publicUser.getUs_email();
//                        CommonFunction.writeFile("UtimapsJobApi", publicUserEmail);
////                publicUserEmail = "mhdaiman@sains.com.my";
//                    }
//
//                    SurveyFirmPModel surveyFirm = (SurveyFirmPModel) retrieverDAO.getModelByCode("case_id", jobModel.getCase_id(), new SurveyFirmPModel());
//                    if (surveyFirm != null) {
//                        surveyFirmUserName = surveyFirm.getFirm_oic();
//                        surveyFirmEmail = surveyFirm.getFirm_email();
//
//                        publicUserName = Validator.isEmpty(surveyFirmUserName) ? publicUserName : publicUserName + ", " + surveyFirmUserName;
//                        publicUserEmail = Validator.isEmpty(surveyFirmEmail) ? publicUserEmail : publicUserEmail + "," + surveyFirmEmail;
//                    }
//
//                    Map mailParam = new HashMap();
//
//                    if (passed) {
//                        autoEmail = autoEmailDAO.getAutoEmailByCode("USJPrecheckSuccess", new AutoEmail());
//                    } else {
//                        autoEmail = autoEmailDAO.getAutoEmailByCode("USJPrecheckFailed", new AutoEmail());
//                    }
//
//                    CommonFunction.writeFile("UtimapsJobApi", "autoEmail " + autoEmail);
//                    mailParam.put(EmailTrigger.MAIL_TO, publicUserEmail); // For Public User
//                    mailParam.put(EmailTrigger.MAIL_CCTO, SystemConstants.email_landsurveyboard);
//
//                    CommonFunction.writeFile("UtimapsJobApi", "userName - " + publicUserName);
//                    CommonFunction.writeFile("UtimapsJobApi", "jobModel - " + jobModel.getUsj_no());
//
//                    mailParam.put("userName", publicUserName); // For Public User
//                    mailParam.put("strUsjNo", jobModel.getUsj_no());
//
//                    new EmailTrigger().sendEMail(mailParam, autoEmail);
//
//                    String not_id = "";
//                    String not_sender = "";
//                    String not_subject = "";
//                    String not_content = "";
//                    if (autoEmail.getNotificationSetup() != null) {
////                                not_sender = ActionContext.getContext().getSession().get("userId").toString();
//                        not_sender = "UTiMAPS BACKEND";
//                        not_id = autoEmail.getNotificationSetup().getNo_id();
//                        not_subject = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_subject(), mailParam);
//                        not_content = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_body(), mailParam);
//                    }
//
//                    NotificationPModel insertNot = new UtimapsAction().insertNotification2(retrieverDAO.getSession(), jobModel.getJob_id(), not_id, not_sender, publicUserId, not_subject, not_content, "U001", "", jobModel.get_taskId(), "S");
//
//                    if (insertNot == null) {
//                        CommonFunction.writeFile("UtimapsJobApi", "Error when insert notification, no notification is inserted." + jobModel.getJob_id() + " - " + not_id + " - " + not_sender + " - " + publicUserId + " - " + not_subject + " - " + not_content + " - " + "U001" + " - " + "" + " - " + jobModel.get_taskId() + " - " + "S");
//                    } else {
//                        new UtimapsAction().auditAction(jobModel.getJob_id(), retrieverDAO.getSession(), not_sender, "Send Notification : " + jobModel.getJob_id());
//                    }
//
//                    jobModel.updatableColumns = new String[]{"job_id", "precheck_notif_sent"};
//                    jobModel.setPrecheck_notif_sent("Y");
//                    retrieverDAO.update(jobModel);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                Debug.printDebug("not sending precheck notif");
//            }
//
//        }
//
//    }
//    public void 
    public ApplicationPModel loadOrCreateAppModel(BaseDAO retrieverDAO, String reqCaseId, String pbLoginId) throws IOException {
        System.out.println("LOAD OR CREATE APP MODEL @@");
        ApplicationPModel returnAppModel = new ApplicationPModel();
        try {
            returnAppModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", this.reqCaseId, new ApplicationPModel());

            if (returnAppModel != null) {
                System.out.println("found model");
            } else {
                System.out.println("null model, create new");

                returnAppModel = new ApplicationPModel();
                returnAppModel.setCase_id(reqCaseId);

                //reference setting
                Calendar cal = Calendar.getInstance();
                String caseYear = Integer.toString(cal.get(Calendar.YEAR));
                String sqlWhere = "case_year = " + "'" + caseYear + "'";
                String maxCaseSeq = cf.getSingleValueWithSession(baseDAO.getSession(), "us_application_p", "max(case_seq)", sqlWhere);
                String caseType = "UAP";
                Integer intNewCaseSeq = 0;

                if (Validator.isEmpty(maxCaseSeq)) {
                    intNewCaseSeq = 1;
                } else {
                    intNewCaseSeq = Integer.parseInt(maxCaseSeq) + 1;
                }

                //System.out.println("new seq - " + intNewCaseSeq);
                String newCaseSeq = String.format("%06d", intNewCaseSeq);

                returnAppModel.setCase_type(caseType);
                returnAppModel.setCase_year(caseYear);
                returnAppModel.setCase_seq(newCaseSeq);
                returnAppModel.setCase_ref(caseType + "/" + newCaseSeq + "/" + caseYear);
                returnAppModel.setApp_status(UtimapsAction.PB_STATUS.APPLICATION_SAVE);
                System.out.println("this.pFimUserId in creating app " + this.pFimUserId);
                returnAppModel.setFim_user_id(this.pFimUserId);
                returnAppModel.setCo_id(this.pCoId);

                SurveyFirmPModel newFirmModel = new SurveyFirmPModel();
                newFirmModel.setFirm_soc(getFirmSoc());
                appCaseMap.put("firm_name", getFirmSocName(getFirmSoc()));

                // L&S CATER FOR IN HOUSE APPLICATION 05.05.2025
                // whitelist for PS_ID values that correspond to trigger application as the in-house type.
                String[] ps_whitelist = {"01"};

                for (String ps : ps_whitelist) {
                    if (newFirmModel.getFirm_soc().equals(ps)) {
                        returnAppModel.setInternal_case("Y");
                        break;
                    }
                }

                newFirmModel.setSo_app_id(com.sains.framework.base.CommonFunction.getId(20));
                newFirmModel.setCase_id(returnAppModel.getCase_id());
                newFirmModel.setCreated_date(DateUtil.getCurrentTimestamp());
                newFirmModel.setCreated_by(pbLoginId);
                newFirmModel.setUpdated_date(DateUtil.getCurrentTimestamp());
                newFirmModel.setUpdated_by(pbLoginId);

                serviceFactory.getUtilAppService().manualInsert(returnAppModel, pbLoginId);
                serviceFactory.getSurveyFirmService().insert(newFirmModel);

                System.out.println("created new and returning model");

                //############################################## Trigger ISM [Start] ##############################################
//                new LogFunction().logInfo(this.getClass(), "ATTEMPT APP ISM SAVE AS DRAFT WORKFLOW CALL FOR -> " + returnAppModel.getID());
                org.apache.logging.log4j.LogManager.getLogger(this.getClass().getSimpleName()).info("ATTEMPT APP ISM SAVE AS DRAFT WORKFLOW CALL FOR -> " + returnAppModel.getID());
                ApplicationPModel appModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", reqCaseId, new ApplicationPModel());
                new ISMServicesAction().triggerISMWorkflowApp(appModel, ISMWorkflowBase.ISM_WF_STEP.SAVE_DRAFT);
                //############################################## End Tr. ISM [ End ] ##############################################

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("returning App Model");
        returnAppModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", this.reqCaseId, new ApplicationPModel());
        return returnAppModel;
    }

    /*
        UPDATE UTIL APPLICATION
     */
    public void updateApplication() throws IOException {
        org.apache.logging.log4j.LogManager.getLogger(this.getClass().getSimpleName()).info("UPDATE APPLICATION API - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "Server Not Responding.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
//        Debug.printDebug("--- setting session ---");
        BufferedReader rd = request.getReader();
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            content.append(line).append("\n");
        }
        rd.close();

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(content.toString());
            JSONObject jsonData = (JSONObject) jsonObject.get("upd_data");
            System.out.println("json - " + jsonObject);

            this.pbUserId = (String) jsonObject.get("pbUserId");
            this.pbLoginId = (String) jsonObject.get("pbLoginId");
            this.hValue = (String) jsonObject.get("hValue");
            this.selectStep = (String) jsonObject.get("selectStep");
            this.pCoId = (String) jsonObject.get("pCoId");
            this.pFimUserId = (String) jsonObject.get("pFimUserId");

            JSONObject jsonUpd = (JSONObject) jsonData.get("app");
//            System.out.println("jsonUpd - " + jsonUpd);
            this.reqCaseId = jsonUpd.get("case_id").toString();
            System.out.println("case id " + this.reqCaseId);

//            System.out.println("pb user login - " + pbLoginId);
//            System.out.println("select step - " + selectStep);
            this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbLoginId, "UTiMAPS");
            if (this.hValue.equals(this.hashKey)) {
                resStatus = 200;
                resMsg = "OK";
                ApplicationPModel updateModel = new ApplicationPModel();

                switch (selectStep) {
                    case "1":
                        ApplicationPModel appModel = loadOrCreateAppModel(retrieverDAO, this.reqCaseId, pbLoginId);

                        updateModel.updatableColumns = new String[]{"pj_div", "case_div", "pj_name", "land_dist", "land_desc", "qual_level", "client_name", "usj_providers"};
                        String[] dataFields = {"case_id", "job_id", "case_type", "case_div", "case_seq", "case_year", "case_div", "qual_level", "usj_div", "usj_seq", "usj_year", "pj_div", "pj_name", "land_dist", "land_desc", "client_name", "usj_providers"};
//                        setModelData((JSONObject) jsonData.get("app"), dataFields, updateModel);
                        setModelData((JSONObject) jsonData.get("app"), dataFields, updateModel);

                        JSONObject app = (JSONObject) jsonData.get("app");
//                        System.out.println("app save - " + app);

                        Map localityMap = new HashMap();
                        localityMap.put("case_id", updateModel.getCase_id());
                        List<AppLocalityModel> distList = retrieverDAO.list(localityMap, AppLocalityModel.class);

                        if (distList.size() > 0) {
                            for (AppLocalityModel alm : distList) {
//                                System.out.println("to del - " + alm.getLocality_id());
                                serviceFactory.getAppLocalityService().delete(alm.getLocality_id(), alm);
                            }
                        }

                        if (app.containsKey("selectedAD")) {
                            String[] selectedAd = app.get("selectedAD").toString().split(",");

                            for (String ad : selectedAd) {
//                                System.out.println("selected " + ad);
                                Map paramMap = new HashMap();
                                paramMap.put("code_type", "ADS");
                                paramMap.put("code_desc", ad);
                                paramMap.put("code_acr", "AD");
                                List<Pubcode> pbList = retrieverDAO.list(paramMap, Pubcode.class);

                                if (pbList.size() > 0) {
                                    for (Pubcode pb : pbList) {
                                        AppLocalityModel newLocality = new AppLocalityModel();
                                        newLocality.setLocality_id(com.sains.framework.base.CommonFunction.getId(20));
                                        newLocality.setCase_id(updateModel.getCase_id());
                                        newLocality.setAdmin_dist(pb.getCode_2());
                                        serviceFactory.getAppLocalityService().insert(newLocality);
                                    }
                                }
                            }
                        }

                        if (app.containsKey("selectedSUB")) {
                            String[] selectedSub = app.get("selectedSUB").toString().split(",");

                            for (String sd : selectedSub) {
//                                System.out.println("selected " + sd);
                                Map paramMap = new HashMap();
                                paramMap.put("code_type", "ADS");
                                paramMap.put("code_desc", sd);
                                paramMap.put("code_acr", "SD");
                                List<Pubcode> pbList = retrieverDAO.list(paramMap, Pubcode.class);

                                if (pbList.size() > 0) {
                                    for (Pubcode pb : pbList) {
                                        AppLocalityModel newLocality = new AppLocalityModel();
                                        newLocality.setLocality_id(com.sains.framework.base.CommonFunction.getId(20));
                                        newLocality.setCase_id(updateModel.getCase_id());
                                        newLocality.setSub_dist(pb.getCode_2());
                                        serviceFactory.getAppLocalityService().insert(newLocality);
                                    }
                                }
                            }
                        }

                        SurveyFirmPModel updateFirm = (SurveyFirmPModel) retrieverDAO.getModelByCode("case_id", updateModel.getCase_id(), new SurveyFirmPModel());
                        updateFirm.updatableColumns = new String[]{"firm_oic", "firm_email", "firm_country_code", "firm_contact_num", "firm_fax_country_code", "firm_fax_num"};
                        String[] updateFirmFields = new String[]{"case_id", "firm_soc", "firm_oic", "firm_email", "firm_country_code", "firm_contact_num", "firm_fax_country_code", "firm_fax_num", "created_by", "created_date", "updated_by", "updated_date"};
                        setModelData((JSONObject) jsonData.get("firm"), updateFirmFields, updateFirm);
                        serviceFactory.getSurveyFirmService().update(updateFirm);

                        jsonMap.put("returnStep", "2");

                        break;
                    default:
                        break;
                }

//                System.out.println("factory call");
                serviceFactory.getUtilAppService().update(updateModel);

                ApplicationPModel returnAppModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", updateModel.getCase_id(), new ApplicationPModel());
                SurveyFirmPModel returnFirmModel = (SurveyFirmPModel) retrieverDAO.getModelByCode("case_id", updateModel.getCase_id(), new SurveyFirmPModel());

                Map suppDocMap = new HashMap();
                if (returnAppModel.getChecklistModel() != null) {
//                    System.out.println("checklist retrv");

                    ChecklistModel caseChecklist = (ChecklistModel) retrieverDAO.getModelByCode("case_id", returnAppModel.getCase_id(), new ChecklistModel());
                    Map checklistMap = getDataMap(caseChecklist, checklistFields);
                    appCaseMap.put("checklist", checklistMap);

                    //since checklist exists - fetch along with checklist items n remarks
                    suppDocMap = checkSuppDoc("APP", returnAppModel, true);
                    appCaseMap.put("supp_doc", suppDocMap);

                    if (suppDocMap.containsKey("check_list")) {
                        appCaseMap.put("check_list", suppDocMap.get("check_list"));
                    }

                    Map miscDocMap = new HashMap();
                    miscDocMap = checkSuppDoc("SUP", returnAppModel, true);
                    appCaseMap.put("misc_doc", miscDocMap);
                } else {
//                    System.out.println("not fetching CL");
                    suppDocMap = checkSuppDoc("APP", returnAppModel, false);
                    appCaseMap.put("supp_doc", suppDocMap);

                    Map miscDocMap = new HashMap();
                    miscDocMap = checkSuppDoc("SUP", returnAppModel, false);
                    appCaseMap.put("misc_doc", miscDocMap);
                }

                Map returnMap = getDataMap(returnAppModel, appFields);
                appCaseMap.put("util_app", returnMap);

                Map rtnFirmMap = getDataMap(returnFirmModel, firmFields);
                appCaseMap.put("survey_firm", rtnFirmMap);

                appCaseMap.put("firm_name", getFirmSocName(getFirmSoc()));

            }

        } catch (Exception e) {
            e.printStackTrace();

            jsonMap.put("returnStep", selectStep);
            jsonMap.put("dropdown", fetchDropDowns());
            jsonMap.put("rtnMsg", "An error has occurred, please try again.");
            jsonMap.put("firm_name", getFirmSocName(getFirmSoc()));
            resMsg = "CATCH_FAIL";

        } finally {

            jsonMap.put("dropdown", fetchDropDowns());
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("appCase", appCaseMap);
            jsonMap.put("docList", fetchDocList("APP"));

            retrieverDAO.closeSession();
            baseDAO.closeSession();
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }
    }

    public void updateSJSubmission() throws IOException {
        System.out.println("UPDATE SUBMISSION API - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "Server Not Responding.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        BufferedReader rd = request.getReader();
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            content.append(line).append("\n");
        }
        rd.close();

        Map returnAppMap = new HashMap();
        Map returnJobDetailMap = new HashMap();
        String returnStep = "STEP_LIST";

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(content.toString());
            JSONObject jsonData = (JSONObject) jsonObject.get("upd_data");
            JSONObject jsonSubData = (JSONObject) jsonData.get("job");

            this.pbUserId = (String) jsonObject.get("pbUserId");
            this.hValue = (String) jsonObject.get("hValue");
            this.swiperStep = (String) jsonObject.get("utilityStep");
            this.selectStep = (String) jsonObject.get("selectStep");

            this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
            if (this.hValue.equals(this.hashKey)) {
                resStatus = 200;
                resMsg = "OK";
                JobDetailModel updateModel = (JobDetailModel) retrieverDAO.getModelByCode("job_id", jsonSubData.get("job_id").toString(), new JobDetailModel());
                String[] dataFields = {""};
                switch (swiperStep) {
                    case "2":
                        updateModel.updatableColumns = new String[]{"ht_datum"};
                        dataFields = new String[]{"case_id", "job_id", "ht_datum"};
                        setModelData(jsonSubData, dataFields, updateModel);

                        try {
                            updateModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_LIST);
                            serviceFactory.getSubmissionJobService().updateSubmission(updateModel);

                            returnStep = "STEP_CONTROL";
                        } catch (Exception e) {
                            e.printStackTrace();
                            returnStep = "STEP_LIST";
                        }

                        break;
                    case "3":
                        updateModel.updatableColumns = new String[]{"control_sv_flag"};
                        dataFields = new String[]{"case_id", "job_id", "control_sv_flag"};

                        if (!jsonSubData.get("control_sv_flag").equals(updateModel.getControl_sv_flag())) {
                            updateModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.NEW);
                        }

                        setModelData(jsonSubData, dataFields, updateModel);

                        try {
                            updateModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_CONTROL);
                            serviceFactory.getSubmissionJobService().updateSubmission(updateModel);

                            switch (this.selectStep) {
                                case USJ_STEP.STEP_NEW:
                                    returnStep = "STEP_NEW";
                                    break;
                                case USJ_STEP.STEP_LIST:
                                    returnStep = "STEP_LIST";
                                    break;
                                case USJ_STEP.STEP_CONTROL:
                                    returnStep = "STEP_CONTROL";
                                    break;
                                case USJ_STEP.STEP_PRECHECK:
                                    returnStep = "STEP_PRECHECK";
                                    break;
                                case USJ_STEP.STEP_ACK:
                                    returnStep = "STEP_ACK";
                                    break;
                                default:
                                    returnStep = "STEP_PRECHECK";
                                    break;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            returnStep = "STEP_CONTROL";
                        }
                        break;
                    default:
                        break;
                }

//                ApplicationPModel returnAppModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", updateModel.getCase_id(), new ApplicationPModel());
//                JobDetailModel returnJobModel = (JobDetailModel) retrieverDAO.getModelByCode("case_id", updateModel.getCase_id(), new JobDetailModel());
//
//                returnAppMap = getDataMap(returnAppModel, appFields);
//                returnJobDetailMap = getDataMap(returnJobModel, jobFields);
//                subCaseMap.put("util_app", returnAppMap);
//                System.out.println("rtn job map - " + returnJobDetailMap);
//                subCaseMap.put("job_det", returnJobDetailMap);
//                Map suppDocMap = new HashMap();
//
//                switch (returnJobModel.getUsj_status()) {
//                    case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS10:
//                    case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40:
//                    case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS50:
//                    case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS80:
//                        suppDocMap = checkSuppDoc(UtimapsAction.SURVEY_JOB_TYPE.USJ, returnAppModel, true);
//                        break;
//                    default:
//                        suppDocMap = checkSuppDoc(UtimapsAction.SURVEY_JOB_TYPE.USJ, returnAppModel, false);
//                        break;
//                }
//
//                subCaseMap.put("supp_doc", suppDocMap);
//
//                Map miscDocMap = new HashMap();
//                miscDocMap = checkSuppDoc("SMS", returnAppModel, false);
//                subCaseMap.put("misc_doc", miscDocMap);
            }

        } catch (Exception e) {
            e.printStackTrace();

            jsonMap.put("returnStep", swiperStep);
            jsonMap.put("dropdown", fetchDropDowns());
            jsonMap.put("rtnMsg", "An error has occurred, please try again.");
            jsonMap.put("firm_name", getFirmSocName(getFirmSoc()));
            resMsg = "CATCH_FAIL";

        } finally {
            retrieverDAO.closeSession();

            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("subCase", subCaseMap);
            jsonMap.put("docList", fetchDocList("USJ"));
            jsonMap.put("rtnStep", returnStep);

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }
    }

    public void updateMiscDoc() throws IOException {
        System.out.println("UPDATE MISC DOC API - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "Server Not Responding.";
        String subType = "";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        BufferedReader rd = request.getReader();
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            content.append(line).append("\n");
        }
        rd.close();

        String fileId = "";

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(content.toString());
            JSONObject jsonData = (JSONObject) jsonObject.get("upd_data");
            System.out.println("json - " + jsonObject);

            this.reqCaseId = (String) jsonObject.get("reqCaseId");
            this.pbUserId = (String) jsonObject.get("pbUserId");
            this.hValue = (String) jsonObject.get("hValue");
            this.swiperStep = (String) jsonObject.get("swiperStep");

            subType = (String) jsonObject.get("subType");

            this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
            if (this.hValue.equals(this.hashKey)) {
                resStatus = 200;
                resMsg = "OK";
//                ApplicationPModel updateModel = new ApplicationPModel();
                FileModel updateFile = new FileModel();
                updateFile.updatableColumns = new String[]{"description"};

                if (subType.equals(UtimapsAction.DOC_TYPE_CODE_1.APP)) {
                    setModelData((JSONObject) jsonData.get("app"), docFields, updateFile);

                } else if (subType.equals(UtimapsAction.DOC_TYPE_CODE_1.USJ)) {
                    setModelData((JSONObject) jsonData.get("sub"), docFields, updateFile);

                    JSONObject jsonSubData = (JSONObject) jsonData.get("job");
                    JobDetailModel updateModel = (JobDetailModel) retrieverDAO.getModelByCode("job_id", jsonSubData.get("job_id").toString(), new JobDetailModel());
                    String upd_ht_datum = "";
                    upd_ht_datum = jsonSubData.get("ht_datum").toString();
                    if (!Validator.isEmpty(upd_ht_datum) && upd_ht_datum != null) {
                        if (updateModel.getHt_datum() != Double.parseDouble(upd_ht_datum)) {
                            Debug.printDebug("misc uploading - updating ht datum");
                            updateModel.setHt_datum(Double.parseDouble(upd_ht_datum));
//                            serviceFactory.get
                            updateModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_LIST);
                            serviceFactory.getSubmissionJobService().updateSubmission(updateModel);
                        }
                    }
                }
                serviceFactory.getFileModelService().update(updateFile);

//                System.out.println("factory call");
//                serviceFactory.getUtilAppService().update(updateModel);
                ApplicationPModel returnAppModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", this.reqCaseId, new ApplicationPModel());
                SurveyFirmPModel returnFirmModel = (SurveyFirmPModel) retrieverDAO.getModelByCode("case_id", this.reqCaseId, new SurveyFirmPModel());
                Map suppDocMap = new HashMap();
                Map miscDocMap = new HashMap();
                Map returnMap = getDataMap(returnAppModel, appFields);
                Map rtnFirmMap = getDataMap(returnFirmModel, firmFields);

                switch (subType) {
                    case UtimapsAction.DOC_TYPE_CODE_1.APP:
                        suppDocMap = checkSuppDoc("APP", returnAppModel, false);
                        appCaseMap.put("supp_doc", suppDocMap);

                        miscDocMap = checkSuppDoc("SUP", returnAppModel, false);
                        appCaseMap.put("misc_doc", miscDocMap);
                        appCaseMap.put("util_app", returnMap);
                        appCaseMap.put("survey_firm", rtnFirmMap);
                        appCaseMap.put("firm_name", getFirmSocName(getFirmSoc()));

                        jsonMap.put("appCase", appCaseMap);

                        break;
                    case UtimapsAction.DOC_TYPE_CODE_1.USJ:
                        JobDetailModel returnJobModel = (JobDetailModel) retrieverDAO.getModelByCode("case_id", getReqCaseId(), new JobDetailModel());
                        Map returnJobDetailMap = getDataMap(returnJobModel, jobFields);

                        suppDocMap = checkSuppDoc("USJ", returnAppModel, false);
                        subCaseMap.put("supp_doc", suppDocMap);
                        subCaseMap.put("job_det", returnJobDetailMap);
//                        miscDocMap = checkMiscDoc(returnAppModel, "SMS");
                        miscDocMap = checkSuppDoc("SMS", returnAppModel, false);
                        subCaseMap.put("misc_doc", miscDocMap);
                        subCaseMap.put("util_app", returnMap);
                        subCaseMap.put("survey_firm", rtnFirmMap);
                        subCaseMap.put("firm_name", getFirmSocName(getFirmSoc()));

                        jsonMap.put("subCase", subCaseMap);

                        break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();

            jsonMap.put("dropdown", fetchDropDowns());
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("docList", fetchDocList("APP"));

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }
    }

    public void newJobSubmission() throws IOException {
        System.out.println("NEW OR RESUBMIT SJ SUBMISSION API - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "Server Not Responding.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
//        Debug.printDebug("--- setting session ---");

        Map returnAppMap = new HashMap();
        Map returnJobDetailMap = new HashMap();

//        System.out.println("swiper step? - " + swiperStep);
//        Debug.printDebug("session info - " + ActionContext.getContext().getSession());
//        cf.writeFile("SessionLog", ActionContext.getContext().getSession().toString());
        try {
            if (Validator.isEmpty(getPbUserName()) || Validator.isEmpty(this.hValue)) {
                resStatus = 401;
                resMsg = "Required param cannot be empty.";

                try {
                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonMap));
                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }

            } else {
                this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
                if (this.hValue.equals(this.hashKey)) {
                    resStatus = 200;
                    resMsg = "OK";
                    ApplicationPModel returnAppModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", getReqCaseId(), new ApplicationPModel());
                    JobDetailModel returnJobModel = (JobDetailModel) retrieverDAO.getModelByCode("case_id", getReqCaseId(), new JobDetailModel());

                    returnAppMap = getDataMap(returnAppModel, appFields);
                    returnJobDetailMap = getDataMap(returnJobModel, jobFields);

                    if (returnJobModel.getIsm_rec_id() == null) {
                        //############################################## Trigger ISM [Start] ##############################################
                        org.apache.logging.log4j.LogManager.getLogger(this.getClass().getSimpleName()).info("ATTEMPT SUB ISM SAVE AS DRAFT WORKFLOW CALL FOR -> " + returnJobModel.getID());
                        new ISMServicesAction().triggerISMWorkflowSub(returnJobModel, ISMWorkflowBase.ISM_WF_STEP.SAVE_DRAFT);
                        //############################################## End Tr. ISM [ End ] ##############################################
                    }

                    try {
                        FileModel usjIssuance = (FileModel) retrieverDAO.getModelByCode("case_id,file_type", returnJobModel.getJob_id() + "," + "SISJL", new FileModel());
                        if (usjIssuance != null) {
                            subCaseMap.put("sji_id", usjIssuance.getFile_id());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    switch(returnAppModel.getCase_div()) {
                    String divSQL = "select code_desc from pubcode where code_type = 'DIV' and code_1 = '" + returnAppModel.getCase_div() + "'";
                    String divStr = retrieverDAO.getSingleValue(divSQL);
                    returnAppMap.put("div_str", divStr);

//                    }
                    subCaseMap.put("util_app", returnAppMap);
//                    System.out.println("rtn job map - " + returnJobDetailMap);
                    subCaseMap.put("job_det", returnJobDetailMap);

                    Map suppDocMap = new HashMap();
                    Map checkListMap = new HashMap();

                    // [CHECKLIST RETRIEVE BY FILE]
                    switch (returnJobModel.getUsj_status()) {
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS10:
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40:
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS50:
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS80:
                            suppDocMap = checkSuppDoc(UtimapsAction.SURVEY_JOB_TYPE.USJ, returnAppModel, true);
                            checkListMap = (HashMap) suppDocMap.get("check_list");
//                            Debug.printDebug(checkListMap.toString());
                            break;
                        default:
                            suppDocMap = checkSuppDoc(UtimapsAction.SURVEY_JOB_TYPE.USJ, returnAppModel, false);
                            break;
                    }

                    // [CHECKLIST RETRIEVE BY QUERY] USCS40 / USCS50 / USCS80
                    switch (returnJobModel.getUsj_status()) {
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS10:
                            break;
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40:
//                            List USCS40_Checklist = new ArrayList();
//                            ChecklistModel queryModel = new ChecklistModel();
//                            queryModel = (ChecklistModel) retrieverDAO.getModelByCode("check_type,case_id", "U30," + returnJobModel.getJob_id(), new ChecklistModel());
//
//                            if (queryModel.getCheck_comment_oic() != null) {
//                                subCaseMap.put("USCS40_OIC", queryModel.getCheck_comment_oic());
//                            }
//
//                            if (queryModel.getComment_ss() != null) {
//                                subCaseMap.put("USCS40_SS", queryModel.getComment_ss());
//                            }
//
//                            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
//                            checklistSetupModel = (ChecklistSetupModel) retrieverDAO.getModelByCode("process_type,checklist_status", "U30,Y", new ChecklistSetupModel());
//
//                            // get each checklist setup item
//                            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
//
//                                Map clItemMap = getDataMap(csi, checklistSetupItemFields);
////                                System.out.println("csi - " + clItemMap);
//
//                                ChecklistItemModel cim = (ChecklistItemModel) retrieverDAO.getModelByCode("ci_id,case_id", csi.getCi_id() + "," + returnJobModel.getJob_id(), new ChecklistItemModel());
//                                if (cim != null) {
////                                    System.out.println("remarks - " + cim.getCl_remarks());
//
//                                    clItemMap.put("cl_remarks", cim.getCl_remarks());
//                                    clItemMap.put("cl_result", cim.getCl_result());
//                                }
//
//                                USCS40_Checklist.add(clItemMap);
//                            });

//                            subCaseMap.put("USCS40_CL", USCS40_Checklist); // <- call function here
                            subCaseMap.put("USCS40_QUERY", getQueryChecklist(returnJobModel.getJob_id(), "U30", "USCS40", retrieverDAO)); // <- call function here
                            break;
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS50:
//                            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U40," + model.getJob_id(), new ChecklistModel());
                            break;
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS80:
                            subCaseMap.put("USCS80_QUERY", getQueryChecklist(returnJobModel.getJob_id(), "U50", "USCS80", retrieverDAO));

//                            System.out.println("uscs80 query - " + subCaseMap.get("USCS80_QUERY").toString());
//                            suppDocMap = checkSuppDoc(UtimapsAction.SURVEY_JOB_TYPE.USJ, returnAppModel, true);
//                            checkListMap = (HashMap) suppDocMap.get("check_list");
//                            Debug.printDebug(checkListMap.toString());
                            break;
                        default:
//                            suppDocMap = checkSuppDoc(UtimapsAction.SURVEY_JOB_TYPE.USJ, returnAppModel, false);
                            break;
                    }

                    subCaseMap.put("supp_doc", suppDocMap);
                    subCaseMap.put("check_list", checkListMap);

                    Map miscDocMap = new HashMap();
                    miscDocMap = checkSuppDoc("SMS", returnAppModel, false);
                    subCaseMap.put("misc_doc", miscDocMap);

                    PaymentModel jobPayment = (PaymentModel) retrieverDAO.getModelByCode("case_id", getReqCaseId(), new PaymentModel());

                    if (jobPayment != null) {
//                        String payRefNo = jobPayment.getPay_ref_no();
//                        System.out.println("pyrfeno - " + payRefNo);
//                        if (payRefNo != null) {
//                            subCaseMap.put("pay_ref_no", payRefNo);
//                        } else {
//                            subCaseMap.put("pay_ref_no", "");
//                        }

                        Map pymtMap = getDataMap(jobPayment, pymtFields);
                        subCaseMap.put("job_payment", pymtMap);

                    } else {
                        System.out.println("no job payment model");
                    }

                    switch (swiperStep) {
                        case USJ_STEP.STEP_NEW:
                        case USJ_STEP.STEP_LIST:
//                            System.out.println("STEP LIST RETRIEVING");
                            try {
                                returnJobModel.set_operation(JobDetailModel.OPERATION.PB_UPDATE_NEW);
//                                returnJobModel.setprecheck_stage(SystemConstants.FILE_STATUS.NO);
                                String firmSoc = getFirmSoc();
                                if (firmSoc != null) {
                                    returnJobModel.setUs_so_id(firmSoc);
                                } else {
                                    // Handle the case where getFirmSoc() returns null
                                    Debug.printDebug("Warning: getFirmSoc() returned null");
                                }

                                if (returnJobModel.getDate_issue() != null) {
                                    returnJobModel.setUsj_date(returnJobModel.getDate_issue());
                                }

//                                System.out.println("UPDATE SOC ID AND USJ DATE");
                                serviceFactory.getSubmissionJobService().updateSubmission(returnJobModel);

                                // 2024-10-23 PREVENT NULL CONTROL SV FLAG AS USER IS PROCEEDING TO DIFFERENT PAGES
                                if (returnJobModel.getControl_sv_flag() == null) {
                                    returnJobModel.updatableColumns = new String[]{"job_id", "control_sv_flag"};
                                    returnJobModel.setControl_sv_flag("N");
                                    retrieverDAO.update(returnJobModel);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case USJ_STEP.STEP_ACK:
                            Debug.printDebug("STEP_ACK retrieval");
                            Map psDspMap = checkSuppDoc(SystemConstants.FILE_TYPE.PSDSP, returnAppModel, false);
                            subCaseMap.put("ps_dsp", psDspMap);

                            Map hcMap = new HashMap();
                            if (returnJobModel.getControl_sv_flag().equals("Y")) {
                                if (returnJobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS50)) {
                                    hcMap.put("us41", fetchHardcopyList("HCUS41"));

                                } else {
                                    hcMap.put("us11", fetchHardcopyList("HCUS11"));
                                    hcMap.put("us21", fetchHardcopyList("HCUS21"));
                                }
                            } else {
                                hcMap.put("us21", fetchHardcopyList("HCUS21"));
                            }

                            String hcList = "";

                            Map ups21 = fetchHardcopyList("HCUS11"); //trav
                            Map ups31 = fetchHardcopyList("HCUS21"); //util

                            Map u40Map = fetchHardcopyList("HCUS41");

                            String ups21Title = "<p>Part 1: Submission of Traverse Survey</p>";
                            String ups31Title = "<p>Part 2: USJ Submission</p>";
                            String ups41Title = "<p>Traverse Survey Plan</p>";

                            List us11List = (List) ups21.get("us11List");
                            List us21List = (List) ups31.get("us21List");
                            List us41List = (List) u40Map.get("us41List");

                            if (returnJobModel.getControl_sv_flag().equals("Y")) {

                                if (returnJobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS50)) {
                                    hcList += ups41Title;
                                    hcList += "<ol>";
                                    for (Object value : us41List) {
                                        if (!value.toString().toLowerCase().contains("field books")) {
                                            hcList += "<li>" + value.toString().replaceAll(".*=(.*)}", "$1") + "</li>";
                                        }
                                    }

                                } else {
                                    hcList += ups21Title;
                                    hcList += "<ol>";
                                    //iterate ups21 as <li>
                                    hcList += "<li>Traverse Survey Plan Including:</li>";
                                    hcList += "<ol type='I'>";
                                    for (Object value : us11List) {
//                                    hcList += "<li>" + value.toString().replaceAll(".*=(.*)}", "$1") + "</li>";
//                                    if (value.toString().toLowerCase().contains("field books")) {
//                                    }
                                        if (!value.toString().toLowerCase().contains("field books")) {
                                            hcList += "<li>" + value.toString().replaceAll(".*=(.*)}", "$1") + "</li>";
                                        }
                                    }
                                    hcList += "</ol>";
                                    hcList += "<li>Field Books</li>";

                                    hcList += "</ol>";

                                    hcList += ups31Title;
                                    hcList += "<ol>";

                                    //iterate ups31 as <li>
                                    for (Object value : us21List) {
                                        hcList += "<li>" + value.toString().replaceAll(".*=(.*)}", "$1") + "</li>";
                                    }
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

//                            Debug.printDebug("hcList - " + hcList);
                            subCaseMap.put("hardcopy", hcMap);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

//            jsonMap.put("dropdown", fetchDropDowns());
            jsonMap.put("public_user", fetchPbUser(this.pbUserId, retrieverDAO));
            jsonMap.put("docList", fetchDocList("USJ"));
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("subCase", subCaseMap);

            retrieverDAO.closeSession();
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();

        }
    }

    public Map getQueryChecklist(String job_id, String checklist, String query, BaseDAO retrieverDAO) {
        Map queryMap = new HashMap();

        try {
            List queryChecklist = new ArrayList();
            ChecklistModel queryModel = new ChecklistModel();
            queryModel = (ChecklistModel) retrieverDAO.getModelByCode("check_type,case_id", checklist + "," + job_id, new ChecklistModel());

            if (queryModel.getCheck_comment_oic() != null) {
                queryMap.put(query + "_OIC", queryModel.getCheck_comment_oic());

                if (queryModel.getCheck_by_oic() != null) {
                    String userSql = "select us_user_name from t_setup_user"
                            + " where us_id = '" + queryModel.getCheck_by_oic() + "'";
                    String userName = retrieverDAO.getSingleValue(userSql);

                    queryMap.put(query + "_OIC_USER", userName);
                } else {
                    queryMap.put(query + "_OIC_USER", "AS");
                }

            }

            if (queryModel.getComment_ss() != null) {
                queryMap.put(query + "_SS", queryModel.getComment_ss());

                if (queryModel.getCheck_by_ss() != null) {
                    String userSql = "select us_user_name from t_setup_user"
                            + " where us_id = '" + queryModel.getCheck_by_ss() + "'";
                    String userName = retrieverDAO.getSingleValue(userSql);

                    queryMap.put(query + "_SS_USER", userName);
                } else {
                    queryMap.put(query + "_SS_USER", "SS");
                }
            }

            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
            checklistSetupModel = (ChecklistSetupModel) retrieverDAO.getModelByCode("process_type,checklist_status", checklist + ",Y", new ChecklistSetupModel());

            // get each checklist setup item
            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {

                Map clItemMap = getDataMap(csi, checklistSetupItemFields);
//                System.out.println("csi - " + clItemMap);

                ChecklistItemModel cim = (ChecklistItemModel) retrieverDAO.getModelByCode("ci_id,case_id", csi.getCi_id() + "," + job_id, new ChecklistItemModel());
                if (cim != null) {
//                    System.out.println("remarks - " + cim.getCl_remarks());

                    clItemMap.put("cl_remarks", cim.getCl_remarks());
                    clItemMap.put("cl_result", cim.getCl_result());
                }

                queryChecklist.add(clItemMap);
            });

            if (!queryChecklist.isEmpty()) {
                queryMap.put(query + "_CL", queryChecklist);
            }

        } catch (Exception e) {

        }

        return queryMap;
    }

    public int verifySuppDocs(ApplicationPModel model) {
        int pass = 0;
        String[] requiredDocs = new String[]{"LOA", "LOC", "PUP", "LAW"};

        List unsubmittedDocs = new ArrayList();

        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
//        Debug.printDebug("--- setting session ---");

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
//                            + "and CODE_1 = 'USJ' "
                    unsubmittedDocs.add(retrieverDAO.getSingleValue(docSql));
                    pass++;
                }
            }

            submitDataMap.put("unsub_docs", unsubmittedDocs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pass;
//        return unsubmittedDocs;
    }

//=========================================================================================================
//          FETCHING METHODS        
    public Map<String, Object> fetchDropDowns() {
        Map<String, Object> dropDownMap = new HashMap();
        dropDownMap.put("prefixPhoneList", getCommList().getCountryCodeListWithDesc());
        dropDownMap.put("stateList", getCommList().getStateList());
        dropDownMap.put("divisionList", getCommList().getDivisionList());
        dropDownMap.put("allDisList", getCommList().getAllDisList());
        dropDownMap.put("qualitySurveyList", getCommList().getQualitySurveyList());
        dropDownMap.put("utilityProvidersList", getCommList().getUtilityProvidersList());
        return dropDownMap;
    }

    public Map<String, Object> fetchHardcopyList(String type) {
        Map<String, Object> hcListMap = new HashMap();
        try {
            List resultList;
            String strSQL = "";
            Map paramSQL = new HashMap();
            paramSQL.clear();

            switch (type) {
                case "HCUS11":
                    strSQL = "SELECT CI_DESC FROM T_SETUP_CHECKLIST_ITEM "
                            + "WHERE CHECKLIST_ID = '1724316153377c5pzDn0'";
//                            + "AND CODE_1 = '" + type + "' "
//                            + "AND NOT CODE_2 = 'AMS' "
//                            + "AND NOT CODE_2 = 'SUP'";
                    resultList = cf.getListFromSqlWithSession(baseDAO.getSession(), strSQL, paramSQL);
                    hcListMap.put("us11List", resultList);
                    break;
                case "HCUS21":
                    strSQL = "SELECT CI_DESC FROM T_SETUP_CHECKLIST_ITEM "
                            + "WHERE CHECKLIST_ID = '1718163616755pTdaBw0'";
                    resultList = cf.getListFromSqlWithSession(baseDAO.getSession(), strSQL, paramSQL);
                    hcListMap.put("us21List", resultList);
                    break;
                case "HCUS41":
                    strSQL = "SELECT CI_DESC FROM T_SETUP_CHECKLIST_ITEM "
                            + "WHERE CHECKLIST_ID = '1725589524995u5vTDX0'";
                    resultList = cf.getListFromSqlWithSession(baseDAO.getSession(), strSQL, paramSQL);
                    hcListMap.put("us41List", resultList);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hcListMap;
    }

    public Map<String, Object> fetchDocList(String type) {
        Map<String, Object> docListMap = new HashMap();
        //BaseDAO dao = new BaseDAOImpl();
        //dao.setSession(baseDAO.getSession());
        try {
            List resultList;
            String strSQL = "";
            Map paramSQL = new HashMap();
            paramSQL.clear();

            switch (type) {
                case "TRAV":
                    strSQL = "SELECT CODE_2, CODE_3, CODE_DESC, CODE_ACR FROM T_SETUP_CODE "
                            + "WHERE CODE_TYPE = 'DOC' "
                            + "AND CODE_1 = 'USJ' "
                            + "AND CODE_3 = 'TR' ";
                    resultList = cf.getListFromSqlWithSession(baseDAO.getSession(), strSQL, paramSQL);
                    System.out.println("trav res list - " + resultList);
                    docListMap.put("docList", resultList);
                    break;
                case "APP":
                    strSQL = "SELECT CODE_2, CODE_3, CODE_DESC, CODE_ACR FROM T_SETUP_CODE "
                            + "WHERE CODE_TYPE = 'DOC' "
                            + "AND CODE_1 = '" + type + "' "
                            + "AND NOT CODE_2 = 'AMS' "
                            + "AND NOT CODE_2 = 'SUP'";
                    resultList = cf.getListFromSqlWithSession(baseDAO.getSession(), strSQL, paramSQL);
                    docListMap.put("docList", resultList);
                    break;
                case "USJ":
                    List drawingList,
                     surveyInfoList,
                     suppDocList,
                     controlList;

                    String drawingSQL = "SELECT CODE_2, CODE_DESC, CODE_ACR FROM T_SETUP_CODE "
                            + "WHERE CODE_TYPE = 'DOC' "
                            + "AND CODE_1 = '" + type + "' "
                            + "AND CODE_3 = 'DR'";

                    String surveyInfoSQL = "SELECT CODE_2, CODE_DESC, CODE_ACR FROM T_SETUP_CODE "
                            + "WHERE CODE_TYPE = 'DOC' "
                            + "AND CODE_1 = '" + type + "' "
                            + "AND CODE_3 = 'SI'";

                    String suppDocSQL = "SELECT CODE_2, CODE_DESC, CODE_ACR FROM T_SETUP_CODE "
                            + "WHERE CODE_TYPE = 'DOC' "
                            + "AND CODE_1 = '" + type + "' "
                            + "AND CODE_3 = 'SD' "
                            + "AND NOT CODE_2 = 'SMS' "
                            + "AND NOT CODE_2 = 'FLB'";

                    String controlSQL = "SELECT CODE_2, CODE_DESC, nvl(CODE_ACR, ' ') as CODE_ACR FROM T_SETUP_CODE "
                            + "WHERE CODE_TYPE = 'DOC' "
                            + "AND CODE_1 = '" + type + "' "
                            + "AND CODE_3 = 'CL'"
                            + "AND NOT CODE_2 = 'FBH' "
                            + "AND NOT CODE_2 = 'SRE' " // 20.8.24 exclude unused sv report
                            + "AND NOT CODE_2 = 'DS6' " // 27.8.24 exclude redundant sv plan
                            + "AND NOT CODE_2 = 'USH' ";

                    drawingList = cf.getListFromSqlWithSession(baseDAO.getSession(), drawingSQL, paramSQL);
                    surveyInfoList = cf.getListFromSqlWithSession(baseDAO.getSession(), surveyInfoSQL, paramSQL);
                    suppDocList = cf.getListFromSqlWithSession(baseDAO.getSession(), suppDocSQL, paramSQL);
                    controlList = cf.getListFromSqlWithSession(baseDAO.getSession(), controlSQL, paramSQL);

                    docListMap.put("drawingList", drawingList);
                    docListMap.put("surveyInfoList", surveyInfoList);
                    docListMap.put("suppDocList", suppDocList);
                    docListMap.put("controlDocsList", controlList);

                    break;
            }

//            System.out.println("resList - " + resultList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //dao.closeSession();
        }

        return docListMap;
    }

    public Map<String, Object> fetchPbUser(String userId, BaseDAO dao) {
//        System.out.println("pb user id - " + userId);
        Map<String, Object> pbUserMap = new HashMap();
//        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        try {
            PublicUserModel pbUserModel = (PublicUserModel) dao.getModelByCode("us_id", userId, new PublicUserModel());
            pbUserMap = getDataMap(pbUserModel, pbUserField);
//            System.out.println("resList - " + resultList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //dao.closeSession();
        }

        return pbUserMap;
    }

    public File getFileById(String jobId, String fileType, BaseDAO retrieverDAO) {
        File rtnFile = null;
        FileModel returnModel = new FileModel();
//        BaseDAO retrieverDAO = new BaseDAOImpl();
        // todo - open for accepting Y

//        String strParam = jobId + "," + fileType + "," + "P";
//        returnModel = (FileModel) retrieverDAO.getModelByCode("case_id,file_type,file_status", strParam, new FileModel());
        String fileSql = "select nvl(file_id, '') from us_file uf "
                + "where uf.case_id = '" + jobId + "'"
                + "and uf.file_type = '" + fileType + "'"
                + "and uf.file_status not in ('D', 'N') "
                + "and rownum = 1 "
                + "order by uf.created_date desc";

        try {
//            Map paramSQL = new HashMap();
//            paramSQL.clear();
//            paramSQL.put(1, jobId);
//            paramSQL.put(2, fileType);
//            System.out.println(paramSQL);
//            List resultList = cf.getListFromSqlWithSession(baseDAO.getSession(), fileSql, paramSQL);

            if (retrieverDAO.getSingleValue(fileSql) != null) {
                String fileSqlId = retrieverDAO.getSingleValue(fileSql);
                returnModel = (FileModel) retrieverDAO.getModelByCode("file_id", fileSqlId, new FileModel());

                if (returnModel != null) {
                    try {
                        AttachmentUploadAction uploadAction = new AttachmentUploadAction();
                        uploadAction.setUploadID(returnModel.getFile_id());
                        File file = uploadAction.downloadTempFile();
                        rtnFile = new File(file.getAbsolutePath());
                        System.out.println("rtnFile " + rtnFile.getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtnFile;
    }

    private Map checkSuppDoc(String type, ApplicationPModel model, Boolean fetchCL) {
        Debug.printDebug("\n=========== " + model.getCase_id() + " checkSuppDoc " + type + " @ " + new java.sql.Timestamp(System.currentTimeMillis()) + " ================");
        Map docMap = new HashMap();
        Map clMap = new HashMap();
        String case_id = model.getCase_id();
        BaseDAO retrieverDAO = new BaseDAOImpl();
//        retrieverDAO.setSession(baseDAO.getSession());

        try {
            switch (type) {
                case UtimapsAction.DOC_TYPE_CODE_1.APP:
//                    System.out.println("APP DOC SEEK");
                    String[] suppDocsField = new String[]{"LOA", "LOC", "PUP", "POT", "LAW"};

                    for (String doc : suppDocsField) {
//                        System.out.println("doc " + doc);
                        Method appM = model.getClass().getMethod("get" + doc);
                        List fieldObj = (List) appM.invoke(model);
                        if (fieldObj.size() > 0) {

                            for (int i = 0; i < fieldObj.size(); i++) {
                                FileModel attachment = (FileModel) fieldObj.get(i);

                                if (!attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.SOFT_DELETED)) {
                                    Debug.printDebug("doc put into map - " + doc);
                                    Map attachmentModelMap = getDataMap(attachment, docFields);
                                    docMap.put(doc, attachmentModelMap);
                                }
                            }

                            if (fetchCL) {
//                                System.out.println("fetching CL here");
//                                System.out.println("===================");
//                                System.out.println("attachment ci_id " + attachment.getCi_id());
//                                System.out.println("attachment ci_id " + attachment.getCase_id());
//                                System.out.println("===================");
//                                String clSQL = "select cl_remarks from US_CHECKLIST_ITEM uci, US_FILE uf "
//                                String clSQL = "select cl_remarks from US_CHECKLIST_ITEM uci "
//                                        //                                        + "where uf.case_id = '" + reqCaseId +"' "
//                                        + "where uci.case_id = '" + reqCaseId + "' "
//                                        + "and uci.ci_id ='" + attachment.getCi_id() + "'";
//                                String clresSQL = "select cl_result from US_CHECKLIST_ITEM uci "
//                                        //                                        + "where uf.case_id = '" + reqCaseId +"' "
//                                        + "where uci.case_id = '" + reqCaseId + "' "
//                                        + "and uci.ci_id ='" + attachment.getCi_id() + "'";

                                String fileCaseId = model.getCase_id();
                                String ci_id = fetchCi_id(retrieverDAO, "APP01", doc);
                                Map clItemMap = new HashMap();

//                                System.out.println("--- ci id " + ci_id + "---- fileCaseId " + fileCaseId + "-----");
                                String cmSQL = "select nvl(file_id, '') from us_file uf "
                                        + "where uf.ci_id = '" + ci_id + "' "
                                        + "and uf.case_id = '" + fileCaseId + "' "
                                        + "and uf.file_type = 'CM'";

                                String cm_file_id = "";
                                if (retrieverDAO.getSingleValue(cmSQL) != null) {
                                    cm_file_id = retrieverDAO.getSingleValue(cmSQL);
                                    FileModel cmFile = (FileModel) retrieverDAO.getModelByCode("file_id", cm_file_id, new FileModel());

                                    if (cmFile != null) {
                                        Map cmFileMap = getDataMap(cmFile, docFields);
//                                        attachmentModelMap.put("cm_file", cmFileMap);
                                        clItemMap.put("cm_file", cmFileMap);
                                    }
                                }

                                ChecklistItemModel cItem = (ChecklistItemModel) retrieverDAO.getModelByCode("ci_id,case_id", ci_id + "," + fileCaseId, new ChecklistItemModel());

                                if (cItem != null) {
                                    String cl_remark = cItem.getCl_remarks();
                                    String cl_result = cItem.getCl_result();

//                                String cl_remark = retrieverDAO.getSingleValue(clSQL);
//                                String cl_result = retrieverDAO.getSingleValue(clresSQL);
//                                System.out.println("==========================");
//                                System.out.println("cl remark - " + cl_remark);
//                                System.out.println("cl result - " + cl_result);
//                                System.out.println("cm_file_id - " + cm_file_id);
//                                System.out.println("==========================\n");
                                    clItemMap.put("agency_remarks", cl_remark);
                                    clItemMap.put("cl_result", cl_result);
                                    clItemMap.put("cm_file_id", cm_file_id);

                                    clMap.put(doc, clItemMap);
                                }
                            }

                        } else {
//                            System.out.println("none found for " + doc);
                        }
                    }

                    if (fetchCL) {
//                        Debug.printDebug("fetchCl - " + fetchCL);
//                        Debug.printDebug("check_list - " + clMap);
                        docMap.put("check_list", clMap);
                    }
                    break;
                case "SUP":

                    Method appMtd = model.getClass().getMethod("getSUP");
                    List amsObj = (List) appMtd.invoke(model);
                    List miscDocs = new ArrayList();
                    if (amsObj.size() > 0) {
//                        System.out.println("found some misc files");
                        for (Object obj : amsObj) {
                            Map miscMap = new HashMap();
                            FileModel attachment = (FileModel) obj;

                            if (!attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.SOFT_DELETED)) {
                                miscMap = getDataMap(attachment, docFields);
                            }

                            if (fetchCL) {
//                                System.out.println("fetching CL here");
//                                System.out.println("===================");
//                                System.out.println("attachment ci_id " + attachment.getCi_id());
//                                System.out.println("attachment ci_id " + attachment.getCase_id());
//                                System.out.println("===================");

                                String fileCaseId = model.getCase_id();
                                String ci_id = fetchCi_id(retrieverDAO, "APP01", "SUP");

                                String cmSQL = "select nvl(file_id, '') from us_file uf "
                                        + "where uf.ci_id = '" + ci_id + "' "
                                        + "and uf.case_id = '" + fileCaseId + "' "
                                        + "and uf.file_type = 'CM'";

                                String cm_file_id = "";
                                if (retrieverDAO.getSingleValue(cmSQL) != null) {
                                    cm_file_id = retrieverDAO.getSingleValue(cmSQL);
                                    FileModel cmFile = (FileModel) retrieverDAO.getModelByCode("file_id", cm_file_id, new FileModel());

                                    if (cmFile != null) {
                                        Map cmFileMap = getDataMap(cmFile, docFields);
                                        miscMap.put("cm_file", cmFileMap);
                                    }
                                }

                                ChecklistItemModel cItem = (ChecklistItemModel) retrieverDAO.getModelByCode("ci_id,case_id", ci_id + "," + fileCaseId, new ChecklistItemModel());
                                String cl_remark = cItem.getCl_remarks();
                                String cl_result = cItem.getCl_result();

                                miscMap.put("agency_remarks", cl_remark);
                                miscMap.put("cl_result", cl_result);
                                miscMap.put("cm_file_id", cm_file_id);
                            }

                            miscDocs.add(miscMap);

                        }
                        docMap.put("SUP", miscDocs);
                    } else {
//                        System.out.println("none");
                        cf.writeFile("PbCheckSuppDocLog", "SUP - none " + model.getCase_id());
                    }

                    break;
                case "SMS":
//                case "AMS":

//                    Method usjMtd = model.getClass().getMethod("getSMS");
//                    List smsObj = (List) usjMtd.invoke(model);
                    JobDetailModel jModel = (JobDetailModel) retrieverDAO.getModelByCode("case_id", model.getCase_id(), new JobDetailModel());
                    Method usjMtd = jModel.getClass().getMethod("getSMS");
                    List smsObj = (List) usjMtd.invoke(jModel);

                    List miscUsjDocs = new ArrayList();
                    if (smsObj.size() > 0) {
                        for (Object obj : smsObj) {
                            Map miscMap = new HashMap();
                            FileModel attachment = (FileModel) obj;

                            miscMap = getDataMap(attachment, docFields);
                            miscUsjDocs.add(miscMap);
                        }
                        docMap.put("SMS", miscUsjDocs);
//                        System.out.println("UTIL SUB SMS - " + miscUsjDocs);
                    } else {
                        cf.writeFile("PbCheckSuppDocLog", "SMS - none " + model.getCase_id());
//                        System.out.println("none");
                    }

                    break;
                case SystemConstants.FILE_TYPE.PSDSP:
//                    System.out.println("PSDSP SEEK");
//                case "AMS":

//                    Method usjMtd = model.getClass().getMethod("getSMS");
//                    List smsObj = (List) usjMtd.invoke(model);
                    JobDetailModel jobDModel = (JobDetailModel) retrieverDAO.getModelByCode("case_id", model.getCase_id(), new JobDetailModel());
//                    Method usjMtd = jModel.getClass().getMethod("getSMS");
                    List psDspList = jobDModel.getPbSignedDSPList();

                    List psDspDocs = new ArrayList();
                    if (!psDspList.isEmpty()) {
                        for (Object obj : psDspList) {
                            Map psDpsMap = new HashMap();
                            FileModel attachment = (FileModel) obj;

                            psDpsMap = getDataMap(attachment, docFields);
                            psDspDocs.add(psDpsMap);
                        }
                        docMap.put("ps_dsp", psDspDocs);
//                        System.out.println("UTIL SUB PSDSP - " + psDspDocs);
                    } else {
//                        System.out.println("none");
                        cf.writeFile("PbCheckSuppDocLog", "PS_DSP - none " + model.getCase_id());
                    }

                    break;
                case SystemConstants.FILE_TYPE.TRAVERSE_PC: // TRAVERSE [PRECHECK] OUTPUT FILES
                    String[] travReportsField = new String[]{"STA", "ERR", "RPN", "RPO", "RPR", "RPG"};
                    for (String doc : travReportsField) {
                        JobDetailModel jobModel = (JobDetailModel) retrieverDAO.getModelByCode("case_id", model.getCase_id(), new JobDetailModel());
                        Method appM = jobModel.getClass().getMethod("get" + doc);
                        List fieldObj = (List) appM.invoke(jobModel);

                        if (fieldObj.size() > 0) {
                            FileModel attachment = new FileModel();
                            Map attachmentModelMap = new HashMap();
                            attachment = (FileModel) fieldObj.get(0);
                            attachmentModelMap = getDataMap(attachment, docFields);

                            docMap.put(doc, attachmentModelMap);
                        }

                    }
                    break;
                case UtimapsAction.DOC_TYPE_CODE_1.USJ:
//                    System.out.println("USJ SEEK");
//                    System.out.println("step " + getSwiperStep());
                    String[] usjDocsField = new String[]{"DSP"};

                    if (getSwiperStep().equals("2")) {
                        usjDocsField = new String[]{"DSP", "DSD", "CPR", "PFR",
                            "CPU", "RDL", "RTS", "RTK", "RIN", "GPR", "SPH", "CLR", "SVR", "FLB"};

                    } else if (getSwiperStep().equals("3")) {
                        usjDocsField = new String[]{"SJI", "EDM", "WOP", "FBL", "RSO", "DS6", "PS3", "FBS"};
                    } else {
                        usjDocsField = new String[]{"DSP", "DSD",
                            "CPU", "RDL", "RTS", "RTK", "RIN", "GPR", "SPH", "CLR", "SVR", "FLB", "FBS",
                            "SJI", "EDM", "WOP", "FBL", "RSO", "DS6", "PS3", "SRE", "CPR", "PFR"};
                    }

                    for (String doc : usjDocsField) {
                        JobDetailModel jobModel = (JobDetailModel) retrieverDAO.getModelByCode("case_id", model.getCase_id(), new JobDetailModel());
                        Method appM = jobModel.getClass().getMethod("get" + doc);
                        List fieldObj = (List) appM.invoke(jobModel);
                        if (fieldObj.size() > 0) {
                            FileModel attachment = new FileModel();
                            FileModel currentAttachment = new FileModel();
                            Map attachmentModelMap = new HashMap();
                            List historyAttachmentList = new ArrayList();

                            String ci_id = "";
                            String fileCaseId = "";

                            if (doc.equals("DSP")) {
                                if (fieldObj.size() == 1) {
                                    attachment = (FileModel) fieldObj.get(0);

                                    if (!attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.SOFT_DELETED)) {
                                        attachmentModelMap = getDataMap(attachment, docFields);

                                        if (attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.INCORRECT)) {
                                            if (!jobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40)) {
//                                            System.out.println("delete psdsp if current dsp is N");
                                                setReqJobId(jobModel.getJob_id());
                                                pbRevokeDigitalPlansNoRes();
                                            }
                                        }
                                    }
                                } else {
                                    FileModel dspToDisplay = new FileModel();
                                    for (int i = 0; i < fieldObj.size(); i++) {
                                        // get the first file with the extension as zip
                                        attachment = (FileModel) fieldObj.get(i);

                                        if (attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.INCORRECT)
                                                || attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.CORRECT)
                                                || attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.SOFT_DELETED)) {
                                            historyAttachmentList.add(getDataMap(attachment, docFields));
                                            docMap.put(doc + "_past", historyAttachmentList);
                                        }

                                        if (attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.INCORRECT)
                                                || attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.CORRECT)
                                                || attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.PENDING)) {
                                            if (attachment.getFile_ext().equals("zip")) {
                                                dspToDisplay = attachment;
                                                break;
                                            } else {
                                                dspToDisplay = attachment;
                                            }

                                        }
                                    }

                                    // see if getting file status is null
                                    if (dspToDisplay.getFile_status() != null) {
                                        if (dspToDisplay.getFile_status().equals(UtimapsAction.DOC_STATUS.INCORRECT)) {
//                                        System.out.println("delete psdsp if current dsp is N");
                                            setReqJobId(jobModel.getJob_id());
                                            pbRevokeDigitalPlansNoRes();
                                        }
                                    }

                                    attachmentModelMap = getDataMap(dspToDisplay, docFields);
                                }

                            } else {
                                for (int i = 0; i < fieldObj.size(); i++) {
                                    attachment = (FileModel) fieldObj.get(i);

                                    if (attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.INCORRECT) || attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.SOFT_DELETED) || attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.CORRECT)) {
                                        if (attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.INCORRECT) || attachment.getFile_status().equals(UtimapsAction.DOC_STATUS.CORRECT)) {
//                                            if (doc.equals("DSP")) {
//                                                if (fieldObj.size() == 1) {
//                                                    attachmentModelMap = getDataMap(attachment, docFields);
//                                                } else {
//                                                    if (attachment.getFile_ext().equals("zip")) {
//                                                        attachmentModelMap = getDataMap(attachment, docFields);
//                                                    }
//                                                }
//                                            } else {
//                                                attachmentModelMap = getDataMap(attachment, docFields);
//                                            }
                                            attachmentModelMap = getDataMap(attachment, docFields);
                                        }

                                        historyAttachmentList.add(getDataMap(attachment, docFields));
                                        docMap.put(doc + "_past", historyAttachmentList);

                                    } else {

//                                        if (doc.equals("DSP")) {
////                                        if (attachment.getFile_ext().equals("zip") || fieldObj.size() == 1) {
////                                            attachmentModelMap = getDataMap(attachment, docFields);
////                                        } else {
////                                            attachmentModelMap = getDataMap(attachment, docFields);
////                                        }
//                                            if (fieldObj.size() == 1) {
//                                                attachmentModelMap = getDataMap(attachment, docFields);
//                                            } else {
//                                                if (attachment.getFile_ext().equals("zip")) {
//                                                    attachmentModelMap = getDataMap(attachment, docFields);
//                                                }
//                                            }
//                                        } else {
//                                        }
                                        attachmentModelMap = getDataMap(attachment, docFields);
                                        currentAttachment = attachment;
                                    }

                                    ci_id = attachment.getCi_id();
                                    fileCaseId = attachment.getCase_id();
                                }
                            }

                            if (fieldObj.size() > 1) {
//                                System.out.println("TYPE " + doc + " has more than 1!");
                            }

                            docMap.put(doc, attachmentModelMap);

                        } else {

                            if (doc.equals("SJI")) {
                                FileModel attachment = jobModel.getSISJL().get(0);

                                if (attachment != null) {
//                                    System.out.println("found signed SJI");
                                    AttachmentUploadAction uploadAction = new AttachmentUploadAction();
                                    uploadAction.copyFiles("U10", doc, attachment);

                                }

                            }
                        }

                        if (fetchCL) {
                            /*
                                    if checklist is independently fetched then
                                    - fileCaseId = jobModel.getJob_id();
                                    - ci_id = fetchCi_id(retrieverDAO ,doc)
                             */

                            String fileCaseId = jobModel.getJob_id();
                            String ci_id = fetchCi_id(retrieverDAO, setupChecklistCode(doc), doc);
                            Map clItemMap = new HashMap();

                            String clSQL = "select cl_remarks from US_CHECKLIST_ITEM uci "
                                    + "where uci.case_id = '" + fileCaseId + "' "
                                    + "and uci.ci_id ='" + ci_id + "'";
                            String clresSQL = "select cl_result from US_CHECKLIST_ITEM uci "
                                    + "where uci.case_id = '" + fileCaseId + "' "
                                    + "and uci.ci_id ='" + ci_id + "'";
                            String cmSQL = "select nvl(file_id, '') from us_file uf "
                                    + "where uf.ci_id = '" + ci_id + "' "
                                    + "and uf.case_id = '" + fileCaseId + "' "
                                    + "and uf.file_type = 'CM' "
                                    + "and rownum = 1 "
                                    + "order by uf.created_date desc";

                            String cm_file_id = "";
                            if (retrieverDAO.getSingleValue(cmSQL) != null) {
                                cm_file_id = retrieverDAO.getSingleValue(cmSQL);
                                FileModel cmFile = (FileModel) retrieverDAO.getModelByCode("file_id", cm_file_id, new FileModel());

                                if (cmFile != null) {
                                    Map cmFileMap = getDataMap(cmFile, docFields);
                                    clItemMap.put("cm_file", cmFileMap);
                                }
                            }

                            String cl_remark = retrieverDAO.getSingleValue(clSQL);
                            String cl_result = retrieverDAO.getSingleValue(clresSQL);

                            clItemMap.put("agency_remarks", cl_remark);
                            clItemMap.put("cl_result", cl_result);
                            clItemMap.put("cm_file_id", cm_file_id);

                            clMap.put(doc, clItemMap);
                        }
                    }

                    if (fetchCL) {
//                        Debug.printDebug("fetchCl - " + fetchCL);
                        docMap.put("check_list", clMap);
                    }

                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //commented out closeSession as session will be closed further down after this function had been called.
            retrieverDAO.closeSession();
        }

//        System.out.println("=================================================================\n");
        cf.writeFile("PbCheckSuppDocLog", "docMap " + model.getCase_id() + "\n\n===================\n\n" + docMap + "\n\n=======================\n\n");
//        System.out.println("docmap - " + docMap);
        return docMap;
    }

    private String fetchCi_id(BaseDAO dao, String process_, String docType) throws IOException {
        String checkItemId = "";
        try {
            String setupCheckListId = cf.getSingleValueWithSession(dao.getSession(), "t_setup_checklist", "checklist_id", "process_type ='" + process_ + "' AND checklist_status = 'Y'");
            checkItemId = cf.getSingleValueWithSession(dao.getSession(), "t_setup_checklist_item", "ci_id", "ci_datatype = '" + docType + "' AND checklist_id = '" + setupCheckListId + "'");
//            checkItemId = cf.getSingleValueWithSession(dao.getSession(), "t_setup_checklist_item", "ci_id", "ci_datatype = '" + docType + "'");
        } catch (Exception e) {

        }

//        Debug.printDebug("doc checkItemId - " + docType + " - " + checkItemId);
        return checkItemId;
    }

    public String setupChecklistCode(String docField) {
        String clCode = "";
        switch (docField) {
            case "DS6":
            case "PS3":
//            case "SRE":
            case "SJI":
            case "EDM":
            case "WOP":
            case "FBL":
            case "FBS":
            case "RSO":
                clCode = "U10";
                break;
            case "DSP":
            case "DSD":
            case "CPU":
            case "RDL":
            case "RTS":
            case "RTK":
            case "RIN":
            case "GPR":
            case "SPH":
            case "CLR":
            case "SVR":
            case "CPR":
            case "PFR":
            case "FLB":
                clCode = "U20";
                break;
        }

//        System.out.println("code for " + docField + " = " + clCode);
        return clCode;
    }

    private Map checkMiscDoc(ApplicationPModel model, String type) {
//        System.out.println("\n=========== checkMiscDoc " + new java.sql.Timestamp(System.currentTimeMillis()) + " ================");
        Map miscDocMap = new HashMap();
        String case_id = model.getCase_id();
        BaseDAO retDAO = new BaseDAOImpl();
        retDAO.setSession(baseDAO.getSession());
        String typeGetter = "get" + type;

        try {

            Method appMtd = model.getClass().getMethod(typeGetter);
            List amsObj = (List) appMtd.invoke(model);
            List miscDocs = new ArrayList();
            if (amsObj.size() > 0) {
                for (Object obj : amsObj) {
                    Map miscMap = new HashMap();
                    FileModel attachment = (FileModel) obj;

                    miscMap = getDataMap(attachment, docFields);

                    miscDocs.add(miscMap);
                }
                miscDocMap.put(type, miscDocs);
            } else {
//                System.out.println("none");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retDAO.closeSession();
        }

//        System.out.println("=================================================================\n");
        return miscDocMap;
    }

    private Map checkHistory(ApplicationPModel model) {
//        System.out.println("\n=========== checkMiscDoc " + new java.sql.Timestamp(System.currentTimeMillis()) + " ================");
        Map historyMap = new HashMap();
//        String case_id = model.getCase_id();
        BaseDAO retDAO = new BaseDAOImpl();
        retDAO.setSession(baseDAO.getSession());

        try {
            Method appMtd = model.getClass().getMethod("getProcessingHistory");
            List histObj = (List) appMtd.invoke(model);
            List histEntry = new ArrayList();
            if (histObj.size() > 0) {
                for (Object obj : histObj) {
                    Map histMap = new HashMap();
                    ProcessingHistoryModel attachment = (ProcessingHistoryModel) obj;

                    histMap = getDataMap(attachment, historyFields);

                    histEntry.add(histMap);
                }
                historyMap.put("history_list", histEntry);
            } else {
//                System.out.println("none");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retDAO.closeSession();
        }

//        System.out.println("=================================================================\n");
        return historyMap;
    }

    public void pbSignDigitalPlans() throws IOException {
        System.out.println("PB SIGN DSP API - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 500;
        String resMsg = "Server Not Responding.";
        PDFSign pdfSign = new PDFSign();
        int signResult = -4;

        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());

        try {
            if (getCert_file() == null) {
                resStatus = 401;
                resMsg = "Required param cannot be empty.";

                try {
                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonMap));
                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** pbSignDigitalPlans Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }

            } else {

                KeyStore ks = KeyStore.getInstance("pkcs12");

                try {
                    ks.load(new FileInputStream(getCert_file()), getCert_pwd().toCharArray());

                    resStatus = 200;
                    resMsg = "OK";
//                    signResult = 0;

                    JobDetailModel returnJobModel = (JobDetailModel) retrieverDAO.getModelByCode("job_id", reqCaseId, new JobDetailModel());

                    Map dspMap = new HashMap();
                    dspMap.put("file_status", "P");
                    dspMap.put("file_type", "DSP");
                    dspMap.put("file_ext", "pdf");
                    dspMap.put("case_id", reqCaseId);
                    List<FileModel> dspModelList = retrieverDAO.list_order(dspMap, FileModel.class, "");

                    if (dspModelList.size() > 0) {
                        for (FileModel dspModel : dspModelList) {
                            String filePath = dspModel.getFile_path();
                            String fileId = dspModel.getFile_id();
                            String fileName = dspModel.getFile_name();

                            signResult = pdfSign.selfSignMode(fileId, filePath, fileName, getCert_file(), getCert_pwd(), returnJobModel.getJob_id(), returnJobModel.getUsj_div(), "PSDSP");
                        }
                    }

                    Map psDspMap = new HashMap();
                    psDspMap.put("file_type", "PSDSP");
                    psDspMap.put("case_id", reqCaseId);
                    List<FileModel> psDspModelList = retrieverDAO.list_order(psDspMap, FileModel.class, "");

                    if (psDspModelList.size() > 0) {
                        List psDspDoclList = new ArrayList();
                        for (FileModel psdsp : psDspModelList) {
                            Map miscMap = new HashMap();
                            miscMap = getDataMap(psdsp, docFields);
                            psDspDoclList.add(miscMap);
                        }
                        jsonMap.put("signed_dsp", psDspDoclList);
                        signResult = 0;
                    } else {
                        resMsg = "No files found for signature. Please reupload digital survey plans (DSP).";
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    resStatus = 500;
                    if (e instanceof java.io.IOException) {
                        if (e.getMessage().contains("keystore password was incorrect")) {
                            resMsg = "Incorrect keystore password. \n\nPlease ensure correct password provided.";
                        } else {
                            resMsg = "Certification failed integrity check during file signature, please ensure valid .p12 file is uploaded.";
                        }
                    } else if (e instanceof java.security.KeyStoreException) {
                        e.printStackTrace();
                        resMsg = "Keystore error: Invalid Keystore File, please ensure valid .p12 file is uploaded.";
                    } else if (e instanceof java.security.cert.CertificateException) {
                        e.printStackTrace();
                        resMsg = "Certificate error: Certification Failed to Verify " + e.getMessage() + "\n\nPlease report to system administrator, thank you.";;
                    } else if (e instanceof java.security.NoSuchAlgorithmException) {
                        e.printStackTrace();
                        resMsg = "Cryptographic algorithm not available. Please try again later, thank you.";
                    } else {
                        e.printStackTrace();
                        resMsg = "Unexpected error during file signature: \n\nPlease report to system administrator, thank you.";
                    }

                    signResult = -4;
                    // resMsg = "Error during file signature.";
                }

//                jsonMap.put("code", code);
                jsonMap.put("signedRes", signResult);
                jsonMap.put("status", resStatus);
                jsonMap.put("message", resMsg);

                // 20241023 close session
                retrieverDAO.closeSession();
                baseDAO.closeSession();

                response.setContentType("application/json");
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                response.getWriter().append(gson.toJson(jsonMap));
                response.flushBuffer();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pbRevokeDigitalPlans() throws IOException {
        System.out.println("PB REVOKE DSP API - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 500;
        String resMsg = "Server Not Responding.";
        PDFSign pdfSign = new PDFSign();
//        int signResult = -4;

        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());

        List dspList = new ArrayList();
//        System.out.println("get req case id - " + getReqCaseId());
//        System.out.println("" + getReqCaseId());
//        System.out.println("cert file - " + getCert_file());
//        System.out.println("cert pwd - " + getCert_pwd());

        try {
            if (getReqCaseId() == null) {
                resStatus = 401;
                resMsg = "Required param cannot be empty.";

                try {
                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonMap));
                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** pbRevokeDigitalPlans Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }

            } else {
                resStatus = 200;
                resMsg = "OK";
                String uploadResult = "failed";

                Map psDspMap = new HashMap();
//                    psDspMap.put("file_status", "P");
                psDspMap.put("file_type", "PSDSP");
                psDspMap.put("case_id", reqCaseId);
                List<FileModel> psDspModelList = retrieverDAO.list_order(psDspMap, FileModel.class, "");

                if (!psDspModelList.isEmpty()) {
                    for (FileModel psdsp : psDspModelList) {
                        AttachmentUploadAction uploadAction = new AttachmentUploadAction();
                        uploadAction.setUploadID(psdsp.getFile_id());
                        uploadResult = uploadAction.removeTempFileRtnStr();
                    }
                }

                jsonMap.put("status", resStatus);
                jsonMap.put("message", resMsg);
//                response.setContentType("application/json");
//                Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                response.getWriter().append(gson.toJson(jsonMap));
//                response.flushBuffer();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();
            baseDAO.closeSession();
        }
    }

//=========================================================================================================
    public void pbRevokeDigitalPlansNoRes() throws IOException {
        System.out.println("PB REVOKE DSP FUNCTION - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 500;
        String resMsg = "Server Not Responding.";
        PDFSign pdfSign = new PDFSign();
//        int signResult = -4;

        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());

        List dspList = new ArrayList();
//        System.out.println("get req job id - " + getReqJobId());
//        System.out.println("cert file - " + getCert_file());
//        System.out.println("cert pwd - " + getCert_pwd());

        try {
            if (getReqCaseId() == null) {
                resStatus = 401;
                resMsg = "Required param cannot be empty.";

                try {
//                    response.setContentType("application/json");
//                    response.getWriter().append(new Gson().toJson(jsonMap));
//                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }

            } else {
                resStatus = 200;
                resMsg = "OK";
                String uploadResult = "failed";

                Map psDspMap = new HashMap();
//                    psDspMap.put("file_status", "P");
                psDspMap.put("file_type", "PSDSP");
                psDspMap.put("case_id", reqJobId);
                List<FileModel> psDspModelList = retrieverDAO.list_order(psDspMap, FileModel.class, "");

                if (!psDspModelList.isEmpty()) {
                    for (FileModel psdsp : psDspModelList) {
                        AttachmentUploadAction uploadAction = new AttachmentUploadAction();
                        uploadAction.setUploadID(psdsp.getFile_id());
                        uploadResult = uploadAction.removeTempFileRtnStr();
                    }
                }

                jsonMap.put("status", resStatus);
                jsonMap.put("message", resMsg);
//                response.setContentType("application/json");
//                Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                response.getWriter().append(gson.toJson(jsonMap));
//                response.flushBuffer();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();
        }
    }

    public void updateNotificationStatus() throws Exception {
        System.out.println("UPDATE NOTIFICATION STATUS - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "Failed to update notification status.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        jsonMap.clear();

        try {
            if (Validator.isEmpty(getPbUserName()) || Validator.isEmpty(this.hValue) || Validator.isEmpty(this.reqMsgId)) {
                resStatus = 401;
                resMsg = "Required parameters cannot be empty.";
            } else {
                this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
                if (this.hValue.equals(this.hashKey)) {
                    NotificationPModel notificationModel = (NotificationPModel) retrieverDAO.getModelByCode("message_id", this.reqMsgId, new NotificationPModel());

                    if (notificationModel != null) {
                        notificationModel.setMessage_status(UtimapsAction.NOTIF_STATUS.READ);
                        notificationModel.setUpdated_date(new java.sql.Timestamp(System.currentTimeMillis()));
//                        notificationModel.setUpdated_by(this.pbUserId);

                        retrieverDAO.update(notificationModel);

                        resStatus = 200;
                        resMsg = "OK";

                        List resultList;
//                        String strSQL = "select n.*, u.US_USER_NAME as sender_name from US_NOTIFICATION_P n "
//                                + "left join T_SETUP_USER u on n.MESSAGE_SENDER = u.US_ID "
//                                + "where n.message_recipient = ?";
                        String strSQL = "select n.* from US_NOTIFICATION_P n "
                                + "left join T_SETUP_USER u on n.MESSAGE_SENDER = u.US_ID "
                                + "INNER JOIN US_APPLICATION_P uap ON n.CASE_ID = uap.CASE_ID "
                                + "where uap.CO_ID = ? "
                                + "UNION "
                                + "select n.* from US_NOTIFICATION_P n "
                                + "left join T_SETUP_USER u on n.MESSAGE_SENDER = u.US_ID "
                                + "INNER JOIN US_JOBDETAIL uj ON n.CASE_ID = uj.job_id "
                                + "INNER JOIN US_APPLICATION_P uap2 ON uap2.case_id = uj.case_id "
                                + "where uap2.CO_ID = ? ";
                        Map paramSQL = new HashMap();
                        paramSQL.clear();
                        paramSQL.put(1, getpCoId());
//                        paramSQL.put(1, getPbUserId());
//                    System.out.println(paramSQL);
                        resultList = cf.getListFromSqlWithSession(baseDAO.getSession(), strSQL, paramSQL);
//                    System.out.println("notif resultList - " + resultList);
                        dataMap.put("notificationsList", resultList);

                    } else {
                        resStatus = 404;
                        resMsg = "Notification not found.";
                    }
                } else {
                    resStatus = 401;
                    resMsg = "Invalid hash key.";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resStatus = 500;
            resMsg = "Internal server error occurred.";
        } finally {
            retrieverDAO.closeSession();

            jsonMap.put("data", dataMap);
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }
    }

//=========================================================================================================
    // LIST QUERYING METHODS
    public void getAllCaseCount() throws Exception {
        System.out.println("GET ALL CASE COUNT - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "No result found.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        jsonMap.clear();
        dataMap.clear();

        try {
            if (Validator.isEmpty(getPbUserName()) || Validator.isEmpty(this.hValue)) {
                resStatus = 401;
                resMsg = "Required param cannot be empty.";

                try {
                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonMap));
                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }

            } else {
                this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
                if (this.hValue.equals(this.hashKey)) {
                    resStatus = 200;
                    resMsg = "OK";
                    allCaseListCount(retrieverDAO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();

            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("data", dataMap);
//            System.out.println("json map - " + jsonMap);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }
    }

    public void getAllCaseList() throws Exception {
        System.out.println("GET ALL CASE LIST - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "No result found.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        jsonMap.clear();
        dataMap.clear();

        try {
            if (Validator.isEmpty(getPbUserName()) || Validator.isEmpty(this.hValue)) {
                resStatus = 401;
                resMsg = "Required param cannot be empty.";

                try {
                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonMap));
                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }

            } else {
                this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
                if (this.hValue.equals(this.hashKey)) {
                    resStatus = 200;
                    resMsg = "OK";
                    allCaseListData(retrieverDAO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();

            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("data", dataMap);

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }

    }

    public void getAllNotifList() throws Exception {
        System.out.println("GET ALL NOTIFICATIONS LIST - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "No result found.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        jsonMap.clear();
        dataMap.clear();

        try {
            if (Validator.isEmpty(getPbUserName()) || Validator.isEmpty(this.hValue)) {
                resStatus = 401;
                resMsg = "Required param cannot be empty.";

                try {
                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonMap));
                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }

            } else {
                this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
                if (this.hValue.equals(this.hashKey)) {
                    resStatus = 200;
                    resMsg = "OK";
//                    allCaseListData(retrieverDAO);
                    List resultList;

//                    String strSQL = "select n.*, u.US_USER_NAME as sender_name from US_NOTIFICATION_P n "
//                            + "left join T_SETUP_USER u on n.MESSAGE_SENDER = u.US_ID "
//                            + "where n.message_recipient = ?";
                    String strSQL = "select n.* from US_NOTIFICATION_P n "
                            + "left join T_SETUP_USER u on n.MESSAGE_SENDER = u.US_ID "
                            + "INNER JOIN US_APPLICATION_P uap ON n.CASE_ID = uap.CASE_ID "
                            + "where uap.CO_ID = ? "
                            + "UNION "
                            + "select n.* from US_NOTIFICATION_P n "
                            + "left join T_SETUP_USER u on n.MESSAGE_SENDER = u.US_ID "
                            + "INNER JOIN US_JOBDETAIL uj ON n.CASE_ID = uj.job_id "
                            + "INNER JOIN US_APPLICATION_P uap2 ON uap2.case_id = uj.case_id "
                            + "where uap2.CO_ID = ?";
                    Map paramSQL = new HashMap();
                    paramSQL.clear();
                    paramSQL.put(1, getpCoId());
                    paramSQL.put(2, getpCoId());
                    System.out.println(paramSQL);
                    resultList = cf.getListFromSqlWithSession(baseDAO.getSession(), strSQL, paramSQL);
//                    System.out.println("notif resultList - " + resultList);
                    dataMap.put("notificationsList", resultList);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();

            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("data", dataMap);

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }

    }

    public void allCaseListCount(BaseDAO retrieverDAO) {
        try {
            List appList;
            List subList;
            List notifList;

            String appSQL = "SELECT "
                    + "SUM(CASE WHEN a.CASE_TYPE = 'UAP' AND a.APP_STATUS NOT IN ('" + UtimapsAction.PB_STATUS.SUBMISSION_SAVE + "') THEN 1 ELSE 0 END) AS util_app_count, "
                    //                    + "SUM(CASE WHEN CASE_TYPE = 'CAD' THEN 1 ELSE 0 END) AS cad_app_count, "
                    + "SUM(CASE WHEN a.USJ_NO IS NOT NULL THEN 1 ELSE 0 END) AS util_sub_count, "
                    + "SUM(CASE WHEN a.CASE_TYPE = 'UAP' AND a.APP_STATUS IN ('" + UtimapsAction.PB_STATUS.APPLICATION_SAVE + "', '" + UtimapsAction.PB_STATUS.APPLICATION_SUB + "' , '" + UtimapsAction.PB_STATUS.ISSUANCE_PENDING + "') THEN 1 ELSE 0 END) AS util_app_new_count, "
                    //                    + "SUM(CASE WHEN CASE_TYPE = 'CAD' AND APP_STATUS = '001' THEN 1 ELSE 0 END) AS cad_app_new_count, "
                    + "SUM(CASE WHEN a.CASE_TYPE = 'UAP' AND a.APP_STATUS = '" + UtimapsAction.PB_STATUS.APPLICATION_QUERY + "' THEN 1 ELSE 0 END) AS util_app_resub_count, "
                    //                    + "SUM(CASE WHEN CASE_TYPE = 'CAD' AND APP_STATUS = '004' THEN 1 ELSE 0 END) AS cad_app_resub_count, "
                    + "SUM(CASE WHEN a.CASE_TYPE = 'UAP' AND a.APP_STATUS = '" + UtimapsAction.PB_STATUS.SUBMISSION_SAVE + "' THEN 1 ELSE 0 END) AS util_sub_new_count "
                    + "FROM US_APPLICATION_P a  "
                    + "where a.CO_ID = ?";

            String subSQL = "SELECT "
                    //                    + "COUNT(*) AS util_sub_count, "
                    + "SUM(CASE WHEN b.USJ_STATUS NOT IN ('" + UtimapsAction.PB_STATUS.SUBMISSION_COMPLETED + "', '" + UtimapsAction.PB_STATUS.APP_PAYMENT_COMPLETED + "', '" + UtimapsAction.PB_STATUS.ISSUANCE_PENDING + "', '" + UtimapsAction.PB_STATUS.PENDING_APP_PAYMENT + "') THEN 1 ELSE 0 END) AS util_sub_count, "
                    + "SUM(CASE WHEN b.USJ_STATUS = '" + UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS10 + "' THEN 1 ELSE 0 END) AS util_sub_resub_count, "
                    + "SUM(CASE WHEN b.USJ_STATUS = '" + UtimapsAction.PB_STATUS.SUBMISSION_SAVE + "' THEN 1 ELSE 0 END) AS util_sub_new_count "
                    //                    + "LEFT JOIN US_APPLICATION_P a ON a.CASE_ID = b.CASE_ID "
                    + "FROM US_JOBDETAIL b LEFT JOIN US_APPLICATION_P a ON b.CASE_ID = a.CASE_ID "
                    + "WHERE a.CO_ID = ?";

//            String notifSQL = "SELECT count(*) as total_notif, "
//                    + "sum(CASE WHEN unp.message_status = 'N' THEN 1 ELSE 0 END) AS unread_notif "
//                    + "FROM US_NOTIFICATION_P unp "
//                    + "WHERE MESSAGE_RECIPIENT = ?";
            String notifSQL = "SELECT sum(total_notif) AS total_notif, sum(unread_notif) AS unread_notif FROM ( "
                    + "select count(n.MESSAGE_ID) as total_notif, "
                    + "sum(CASE WHEN n.message_status = 'N' THEN 1 ELSE 0 END) AS unread_notif "
                    + "from US_NOTIFICATION_P n "
                    + "left join T_SETUP_USER u on n.MESSAGE_SENDER = u.US_ID "
                    + "INNER JOIN US_APPLICATION_P uap ON n.CASE_ID = uap.CASE_ID "
                    + "where uap.CO_ID = ? "
                    + "UNION "
                    + "select count(n.MESSAGE_ID) as total_notif, "
                    + "sum(CASE WHEN n.message_status = 'N' THEN 1 ELSE 0 END) AS unread_notif "
                    + "from US_NOTIFICATION_P n "
                    + "left join T_SETUP_USER u on n.MESSAGE_SENDER = u.US_ID "
                    + "INNER JOIN US_JOBDETAIL uj ON n.CASE_ID = uj.job_id "
                    + "INNER JOIN US_APPLICATION_P uap2 ON uap2.case_id = uj.case_id "
                    + "where uap2.CO_ID = ?"
                    + ")";

            Map paramSQL = new HashMap();
            paramSQL.clear();
            paramSQL.put(1, getpCoId());
            Map paramSQL2 = new HashMap();
            paramSQL2.clear();
            paramSQL2.put(1, getpCoId());
            paramSQL2.put(2, getpCoId());
//            System.out.println(paramSQL);
            appList = cf.getListFromSqlWithSession(baseDAO.getSession(), appSQL, paramSQL);
            subList = cf.getListFromSqlWithSession(baseDAO.getSession(), subSQL, paramSQL);
            dataMap.put("appCaseCount", appList);
            dataMap.put("subCaseCount", subList);

            notifList = cf.getListFromSqlWithSession(baseDAO.getSession(), notifSQL, paramSQL2);
            dataMap.put("notifCount", notifList);
            System.out.println("appSQL " + appSQL);
            System.out.println("subSQL " + subSQL);
            System.out.println("notifSQL " + notifSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allCaseListData(BaseDAO retrieverDAO) {
        try {
            List resultList;

            String strSQL = "SELECT 'uta' AS submission_type_code, a.CASE_ID AS ID, a.JOB_ID, a.CASE_REF as ref_no ,a.APP_SUBMIT_BY, a.PJ_NAME, a.LAND_DESC, a.app_status AS status, a.wf_status, a.updated_date, "
                    + "COALESCE(a.usj_no, b.usj_no, 'Unassigned') AS usj_no, "
                    //                    + "CASE WHEN a.usj_no IS NULL THEN 'Unassigned' ELSE a.usj_no END AS usj_no, "
                    + "CASE WHEN a.PJ_DIV IS NULL THEN 'Unassigned' ELSE (SELECT code_desc FROM PUBCODE b WHERE a.PJ_DIV = b.CODE_1 AND CODE_TYPE = 'DIV') END AS APP_DIVISION "
                    + "FROM US_APPLICATION_P a "
                    + "LEFT JOIN US_JOBDETAIL b ON a.CASE_ID = b.CASE_ID "
                    + "WHERE a.co_id = ? "
                    + "UNION ALL "
                    + "SELECT 'uts' AS submission_type_code, b.CASE_ID AS ID, a.JOB_ID, a.CASE_REF as ref_no, a.APP_SUBMIT_BY, a.PJ_NAME, a.land_desc, usj_status AS status, b.wf_status, b.updated_date, "
                    //                    + "CASE WHEN b.usj_no IS NULL THEN 'Unassigned' ELSE b.usj_no END AS usj_no, "
                    + "COALESCE(b.usj_no, 'Unassigned') AS usj_no,"
                    + "CASE WHEN a.PJ_DIV IS NULL THEN 'Unassigned' ELSE (SELECT code_desc FROM PUBCODE b WHERE a.PJ_DIV = b.CODE_1 AND CODE_TYPE = 'DIV') END AS APP_DIVISION "
                    + "FROM US_JOBDETAIL b LEFT JOIN US_APPLICATION_P a "
                    + "ON b.CASE_ID = a.CASE_ID "
                    + "WHERE a.co_id = ? "
                    + "AND (a.app_status NOT IN ('"
                    + UtimapsAction.PB_STATUS.ISSUANCE_PENDING
                    + "', '"
                    + UtimapsAction.PB_STATUS.APP_PAYMENT_COMPLETED
                    + "') OR a.app_status IS NULL) ";
            Map paramSQL = new HashMap();
            paramSQL.clear();
            paramSQL.put(1, getpCoId());
            paramSQL.put(2, getpCoId());
            System.out.println("paramSQL" + paramSQL);
            System.out.println("strSQL " + strSQL);
            resultList = cf.getListFromSqlWithSession(baseDAO.getSession(), strSQL, paramSQL);
            dataMap.put("caseList", resultList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFirmSoc() {
        System.out.println("check pCoId - " + getpCoId());

        String firmSoc = "";
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        try {
            List resultList;
            String strSQL = "SELECT nvl(ps_id, '01') AS CODE FROM T_CUST_COMPANY tcc "
                    + "RIGHT JOIN T_USER_COMPANY tuc ON tcc.CO_ID = tuc.CO_ID "
                    + "WHERE tuc.USCO_STATUS = 'Y' "
                    + "AND tuc.US_ID = ? "
                    + "AND tcc.CO_ID = ?";

            Map paramSQL = new HashMap();
            paramSQL.clear();
            paramSQL.put(1, getPbUserId());
            paramSQL.put(2, getpCoId());
            resultList = cf.getListFromSqlWithSession(baseDAO.getSession(), strSQL, paramSQL);

//            System.out.println("resList - " + resultList);
            if (resultList.size() > 0) {
                Map<String, String> firstElement = (Map) resultList.get(0);
                firmSoc = firstElement.get("CODE");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();
        }

//        System.out.println("firm soc - " + firmSoc);
        return firmSoc;
    }

    public String getFirmSocName(String ps_id) {
        String firmSoc = "";
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        try {
            String psSQL = "select code_desc from pubcode where code_type = 'SOC' and code_1 = '" + ps_id + "' AND ROWNUM = '1'";
            firmSoc = retrieverDAO.getSingleValue(psSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            retrieverDAO.closeSession();
        }

        return firmSoc;
    }

    //loadSelectList
    public void loadSelectList() {
        System.out.println("api loadSelectList************");
        int resStatus = 401;
        String resMessage = "No result found.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());

        try {
            if (Validator.isEmpty(getCodeType())) {
                resStatus = 401;
                resMessage = "Required param cannot be empty.";
                jsonMap.put("res_status", resStatus);
                jsonMap.put("res_message", resMessage);

                try {
                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonMap));
                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }
            } else {
                resStatus = 200;
                resMessage = "OK";

                List pubcodeList = new ArrayList();
                pubcodeList.add(new Options("", as.getText("pleaseSelect")));
                try {
                    Map paramMap = new HashMap();
                    paramMap.put("code_type", getCodeType());
                    if (getCode1() != null) {
                        paramMap.put("code_1", getCode1());
                    } else {
                        paramMap.put("code_1", "!in 000");

                    }

                    if (getCode_acr() != null) {
                        paramMap.put("code_acr", getCode_acr());
                    }

                    for (Pubcode item : (List<Pubcode>) retrieverDAO.list_order(paramMap, Pubcode.class,
                            //                            true, "order by code_desc")) {
                            true, "order by code_2")) {
                        pubcodeList.add(new Options(item.getCode_2(), item.getCode_desc()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jsonMap.put("data", pubcodeList);
                jsonMap.put("res_status", resStatus);
                jsonMap.put("res_message", resMessage);

                response.setContentType("application/json");
                response.getWriter().append(new Gson().toJson(jsonMap));
                response.flushBuffer();
            }
        } catch (Exception e) {
            System.out.println("*** ERROR create new application. *** " + e);

            resStatus = 500;
            resMessage = "Internal Server Error.";

            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);

            try {
                response.setContentType("application/json");
                response.getWriter().append(new Gson().toJson(jsonMap));
                response.flushBuffer();
            } catch (Exception ex) {
                System.out.println("*** Failed to response server error. ***");
                new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
            }
        } finally {
            retrieverDAO.closeSession();
        }
    }

    public void loadDistCheckList() {
//        System.out.println("api loadDistCheckList************");
        int resStatus = 401;
        String resMessage = "No result found.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());

        try {
            if (Validator.isEmpty(getCodeType())) {
                resStatus = 401;
                resMessage = "Required param cannot be empty.";
                jsonMap.put("res_status", resStatus);
                jsonMap.put("res_message", resMessage);

                try {
                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonMap));
                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }
            } else {
                resStatus = 200;
                resMessage = "OK";

                List pubcodeList = new ArrayList();
//                pubcodeList.add(new Options("", as.getText("pleaseSelect")));
                try {
                    Map paramMap = new HashMap();
                    paramMap.put("code_type", getCodeType());
                    if (getCode1() != null) {
                        paramMap.put("code_1", getCode1());
                    } else {
                        paramMap.put("code_1", "!in 000");

                    }

                    if (getCode_acr() != null) {
                        paramMap.put("code_acr", getCode_acr());
                    }

                    for (Pubcode item : (List<Pubcode>) retrieverDAO.list_order(paramMap, Pubcode.class,
                            //                            true, "order by code_desc")) {
                            true, "order by code_2")) {
                        pubcodeList.add(new Options(item.getCode_2(), item.getCode_desc()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jsonMap.put("data", pubcodeList);
                jsonMap.put("res_status", resStatus);
                jsonMap.put("res_message", resMessage);

                response.setContentType("application/json");
                response.getWriter().append(new Gson().toJson(jsonMap));
                response.flushBuffer();
            }
        } catch (Exception e) {
            System.out.println("*** ERROR create new application. *** " + e);

            resStatus = 500;
            resMessage = "Internal Server Error.";

            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);

            try {
                response.setContentType("application/json");
                response.getWriter().append(new Gson().toJson(jsonMap));
                response.flushBuffer();
            } catch (Exception ex) {
                System.out.println("*** Failed to response server error. ***");
                new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
            }
        } finally {
            retrieverDAO.closeSession();
        }
    }

    public String getDistDesc(String type, String div, String code) throws Exception {
        String desc = "";
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());

        try {
            String distSQL = "select code_desc from pubcode where code_type = 'ADS' "
                    + "and code_1 = '" + div + "' "
                    + "and code_2 = '" + code + "' "
                    + "and code_acr = '" + type + "'";

            desc = retrieverDAO.getSingleValue(distSQL);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();
        }

        return desc;
    }

    public void loadSelectedDist() throws Exception {
        System.out.println("GET SELECTED DISTRICTS AD & SD - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "No result found.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());

        ArrayList<String> checkAdVals = new ArrayList<>();
        ArrayList<String> checkSubVals = new ArrayList<>();

        jsonMap.clear();
        dataMap.clear();

        try {
            if (Validator.isEmpty(getPbUserName()) || Validator.isEmpty(this.hValue)) {
                resStatus = 401;
                resMsg = "Required param cannot be empty.";

                try {
                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonMap));
                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }

            } else {
                this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
                if (this.hValue.equals(this.hashKey)) {
                    resStatus = 200;
                    resMsg = "OK";

                    Map paramMap = new HashMap();
//                    System.out.println("req in id - " + getReqCaseId());
                    paramMap.put("case_id", getReqCaseId());

                    ApplicationPModel appModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", getReqCaseId(), new ApplicationPModel());
//                    paramMap.put("code_desc", sd);
//                    paramMap.put("code_acr", "SD");
//                    List<AppLocalityModel> locList = retrieverDAO.list(paramMap, AppLocalityModel.class);

                    List<AppLocalityModel> adList = appModel.getAdminDistrictList();
                    List<AppLocalityModel> subList = appModel.getSubDistrictList();

                    if (adList.size() > 0) {
//                        System.out.println("found adlist");
                        for (AppLocalityModel alm : adList) {
//                            System.out.println("desc");
                            String psSQL = "select code_desc from pubcode where code_type = 'ADS' "
                                    + "and code_1 = '" + caseDiv + "' "
                                    + "and code_2 = '" + alm.getAdmin_dist() + "'";
                            checkAdVals.add(retrieverDAO.getSingleValue(psSQL));
                        }

                        dataMap.put("checkAdVals", checkAdVals);
//                        dataMap.put("checkSubVals", checkSubVals);
                    } else {
//                        System.out.println("ad not found");
                    }

                    if (subList.size() > 0) {
//                        System.out.println("found loclist");
                        for (AppLocalityModel alm : subList) {
//                            System.out.println("desc");
                            String psSQL = "select code_desc from pubcode where code_type = 'ADS' "
                                    + "and code_1 = '" + caseDiv + "' "
                                    + "and code_2 = '" + alm.getSub_dist() + "'";
                            checkSubVals.add(retrieverDAO.getSingleValue(psSQL));
                        }

                        dataMap.put("checkSubVals", checkSubVals);
                    } else {
//                        System.out.println("sub not found");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();

            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("data", dataMap);

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }
    }

    public void getPendingItemsList() throws Exception {
//        System.out.println("GET PYMT CASE LIST - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "No result found.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        jsonMap.clear();
        dataMap.clear();
        System.out.println("getpCoId getPendingItemsList " + getpCoId());

        try {
            if (Validator.isEmpty(getPbUserName()) || Validator.isEmpty(this.hValue)) {
                resStatus = 401;
                resMsg = "Required param cannot be empty.";

                try {
                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonMap));
                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }

            } else {
                this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
                if (this.hValue.equals(this.hashKey)) {
                    resStatus = 200;
                    resMsg = "OK";
                    //get pending payment case list
                    List resultlist;
                    String strSQL = "SELECT usp.*, uap.CASE_REF, uap.PJ_NAME FROM "
                            + "US_PAYMENT usp LEFT JOIN US_APPLICATION_P uap ON usp.CASE_ID = uap.CASE_ID "
                            + "WHERE usp.payment_status != 'PC' "
                            + "AND uap.co_id  = ?";
                    Map paramSQL = new HashMap();
                    paramSQL.clear();
                    paramSQL.put(1, getpCoId());
                    resultlist = cf.getListFromSqlWithSession(baseDAO.getSession(), strSQL, paramSQL);
                    dataMap.put("pendingPaymentCaseList", resultlist);
                    //get payment case list wich is not pending payment 
                    resultlist = null;
                    strSQL = "SELECT usp.*, uap.CASE_REF, uap.PJ_NAME FROM "
                            + "US_PAYMENT usp LEFT JOIN US_APPLICATION_P uap ON usp.CASE_ID = uap.CASE_ID "
                            + "WHERE usp.payment_status = 'PC' "
                            + "AND uap.co_id = ?";
                    paramSQL = new HashMap();
                    paramSQL.clear();
                    paramSQL.put(1, getpCoId());
                    resultlist = cf.getListFromSqlWithSession(baseDAO.getSession(), strSQL, paramSQL);
                    dataMap.put("completePaymentCaseList", resultlist);
                    resultlist = null;

                    strSQL = "select n.* from US_NOTIFICATION_P n "
                            + "left join T_SETUP_USER u on n.MESSAGE_SENDER = u.US_ID "
                            + "INNER JOIN US_APPLICATION_P uap ON n.CASE_ID = uap.CASE_ID "
                            + "where uap.CO_ID = ? and message_status = ? "
                            + "UNION "
                            + "select n.* from US_NOTIFICATION_P n "
                            + "left join T_SETUP_USER u on n.MESSAGE_SENDER = u.US_ID "
                            + "INNER JOIN US_JOBDETAIL uj ON n.CASE_ID = uj.job_id "
                            + "INNER JOIN US_APPLICATION_P uap2 ON uap2.case_id = uj.case_id "
                            + "where uap2.CO_ID = ? and message_status = ? ";
                    paramSQL.clear();
                    paramSQL.put(1, getpCoId());
                    paramSQL.put(2, UtimapsAction.NOTIF_STATUS.UNREAD);
                    paramSQL.put(3, getpCoId());
                    paramSQL.put(4, UtimapsAction.NOTIF_STATUS.UNREAD);
                    resultlist = cf.getListFromSqlWithSession(baseDAO.getSession(), strSQL, paramSQL);
                    dataMap.put("pendingNotifsList", resultlist);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();

            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("data", dataMap);

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }
    }

    //======================================= PAYMENT FUNCTIONS ==================================================
    public void getPaymentCaseList() throws Exception {
//        System.out.println("GET PYMT CASE LIST - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "No result found.";

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        jsonMap.clear();
        dataMap.clear();

        try {
            if (Validator.isEmpty(getPbUserName()) || Validator.isEmpty(this.hValue)) {
                resStatus = 401;
                resMsg = "Required param cannot be empty.";

                try {
                    response.setContentType("application/json");
                    response.getWriter().append(new Gson().toJson(jsonMap));
                    response.flushBuffer();
                } catch (Exception ex) {
                    System.out.println("*** Failed to response server error. ***");
                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
                }

            } else {
                this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
                if (this.hValue.equals(this.hashKey)) {
                    resStatus = 200;
                    resMsg = "OK";
                    //get pending payment case list
                    List resultlist;
//                    String strSQL = "SELECT usp.*, uap.CASE_REF, uap.PJ_NAME FROM "
//                            + "US_PAYMENT usp LEFT JOIN US_APPLICATION_P uap ON usp.CASE_ID = uap.CASE_ID "
//                            + "WHERE usp.payment_status != 'PC' and uap.CREATED_BY = ? "
//                            + "AND uap.APP_SUBMIT_BY = ?";
                    String strSQL = "SELECT usp.*, uap.CASE_REF, uap.PJ_NAME FROM US_PAYMENT usp "
                            + "LEFT JOIN US_APPLICATION_P uap ON usp.CASE_ID = uap.CASE_ID "
                            + "WHERE usp.payment_status != 'PC' "
                            + "AND uap.CO_ID = ?";
                    Map paramSQL = new HashMap();
                    paramSQL.clear();
                    paramSQL.put(1, getpCoId());
//                    paramSQL.put(2, getPbUserId());
                    resultlist = cf.getListFromSqlWithSession(baseDAO.getSession(), strSQL, paramSQL);
                    dataMap.put("pendingPaymentCaseList", resultlist);
                    //get payment case list wich is not pending payment 
                    resultlist = null;
//                    strSQL = "SELECT usp.*, uap.CASE_REF, uap.PJ_NAME FROM "
//                            + "US_PAYMENT usp LEFT JOIN US_APPLICATION_P uap ON usp.CASE_ID = uap.CASE_ID "
//                            + "WHERE usp.payment_status = 'PC' and uap.CREATED_BY = ? "
//                            + "AND uap.APP_SUBMIT_BY = ?";
                    strSQL = "SELECT usp.*, uap.CASE_REF, uap.PJ_NAME FROM US_PAYMENT usp "
                            + "LEFT JOIN US_APPLICATION_P uap ON usp.CASE_ID = uap.CASE_ID "
                            + "WHERE usp.payment_status = 'PC' "
                            + "AND uap.CO_ID = ?";
                    paramSQL = new HashMap();
                    paramSQL.clear();
                    paramSQL.put(1, getpCoId());
//                    paramSQL.put(2, getPbUserId());
                    resultlist = cf.getListFromSqlWithSession(baseDAO.getSession(), strSQL, paramSQL);
                    dataMap.put("completePaymentCaseList", resultlist);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();

            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("data", dataMap);

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }
    }

    public void retrievePayment() throws Exception {
        System.out.println(" GET RETRIEVE PYMT - " + new java.sql.Timestamp(System.currentTimeMillis()));
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//wongkk4@11Aug2021 - for pentest requirement for all API
        int resStatus = 401;
        String resMessage = "No result found.";

//        System.out.println("content type: " + request.getContentType());
//        BufferedReader rd = request.getReader();
//        System.out.println("rd: " + rd);
//        StringBuilder content = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//            content.append(line).append("\n");
//        }
//        rd.close();
        try {
//            JSONObject jsonObject = (JSONObject) new JSONParser().parse(content.toString());
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-store");

//            pbUserId = (String) jsonObject.get("pbUserId");
//            hValue = (String) jsonObject.get("hValue");
//            reqCaseId = (String) jsonObject.get("reqCaseId");
            if (Validator.isEmpty(this.pbUserId) || Validator.isEmpty(this.hValue)) {
                resStatus = 401;
                resMessage = "Required param cannot be empty.";
                jsonMap.put("res_status", resStatus);
                jsonMap.put("res_message", resMessage);

//                try {
//                    response.setContentType("application/json");
//                    response.getWriter().append(new Gson().toJson(jsonMap));
//                    response.flushBuffer();
//                } catch (Exception ex) {
//                    System.out.println("*** Failed to response server error. ***");
//                    new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
//                }
            } else { // VALID PARAMS
                this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
                if (this.hValue.equals(this.hashKey)) {
                    resStatus = 200;
                    resMessage = "OK";

                    String mapCase_ref;
                    String mapTl_name = "";
                    String mapPayment_status = "";
                    String mapBill_ref_no = "";
                    String mapPay_ref_no = "";
                    String mapId_app_case = "";
                    String mapPayment_id = "";
                    String mapPayment_amount = "";
                    String mapPayment_date = "";
                    String mapStatus_date = "";
                    List caseInfoList = new ArrayList();
                    List payItemList = new ArrayList();
                    Map caseInfoMap = new LinkedHashMap();

//                    Map paramMap = new HashMap();
//                    paramMap.clear();
//                    paramMap.put("case_id", this.reqCaseId);
//                    List<ApplicationPModel> appCaseList = retrieverDAO.list(paramMap, ApplicationPModel.class
//                    );
                    ApplicationPModel utilAppCase = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", this.reqCaseId, new ApplicationPModel());
                    PaymentModel pymtCase = (PaymentModel) retrieverDAO.getModelByCode("case_id", this.reqCaseId, new PaymentModel());

                    Map returnPymtMap = getDataMap(pymtCase, pymtFields);
                    jsonMap.put("CaseInfo", returnPymtMap);

                    if (pymtCase != null) {
                        List<PaymentItemModel> paymentItemList = pymtCase.getPaymentItemList();
                        if (paymentItemList.size() > 0) {
                            for (PaymentItemModel paymentItem : paymentItemList) {
                                Map payInfoMap = getDataMap(paymentItem, pymtListFields);
                                payItemList.add(payInfoMap);

                            }

                            jsonMap.put("PymtItemList", payItemList);
                        } else {
                            System.out.println("No Payment Item Found!");
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();
        }

        jsonMap.put("status", resStatus);
        jsonMap.put("message", resMessage);
//        System.out.println("jsonMap " + jsonMap);
        response.setContentType("application/json");
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
    }

    public void retrieveUserPrepaymentDetails() throws IOException {
        BaseDAO transDAO = new BaseDAOImpl();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//wongkk4@11Aug2021 - for pentest requirement for all API
        int resStatus = 401;
        String resMessage = "No result found.";
        System.out.println("****In retrieveUserPrepaymentDetails()****");

        try {

            if (Validator.isEmpty(this.pbUserId)) {
                jsonMap.put("status", resStatus);
                jsonMap.put("message", "User ID is empty.");
//                return "success";
            }
            resStatus = 200;
            resMessage = "OK";
            PublicUserModel userModel = (PublicUserModel) transDAO.getObjectByCode("us_user_id", this.pbUserId, new PublicUserModel());

//            PpBalanceModel balanceModel = new PpBalanceModel();
            if (userModel != null) {
//                System.out.println("check balance model");
//                System.out.println("userModel.getID()" + userModel.getID());
                String usId = userModel.getID();
                PpBalanceModel balanceModel = (PpBalanceModel) transDAO.getObjectByCode("us_id", usId, new PpBalanceModel());
                if (balanceModel == null) {
                    jsonMap.put("status", resStatus);
                    jsonMap.put("message", "Individual Balance Account was not found.");
//                    return "success";
                } else {
                    jsonMap.put("status", resStatus);
                    jsonMap.put("message", resMessage);
                    jsonMap.put("bal_id", balanceModel.getBal_id());
                    jsonMap.put("us_id", balanceModel.getUs_id());
                    jsonMap.put("co_id", balanceModel.getCo_id());
                    jsonMap.put("bal_type", balanceModel.getBal_type());
                    jsonMap.put("bal_status", balanceModel.getBal_status());
                    jsonMap.put("bal_amount", balanceModel.getBal_amount());
//                    return "success";
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve prepayment info. " + e);
            new LogFunction().logError(this.getClass(), " *** Failed to retrieve prepayment info. *** ", e);
        } finally {
            baseDAO.closeSession();
            transDAO.closeSession();
        }
//        return "success";
        response.setContentType("application/json");
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
    }

    public void processPayViaPrepaymentAcc() throws IOException {
        BaseDAO transDAO = new BaseDAOImpl();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//wongkk4@11Aug2021 - for pentest requirement for all API
        int resStatus = 401;
        String resMessage = "No result found.";

        System.out.println("****In processPayViaPrepaymentAcc()****");
        if (Validator.isEmpty(this.ssoUserId)) {
            if (Validator.isEmpty(this.loginId)) {
                jsonMap.put("status", resStatus);
                jsonMap.put("message", "Login ID is empty.");
            }

            if (Validator.isEmpty(this.loginPasswd)) {
                jsonMap.put("status", resStatus);
                jsonMap.put("message", "Login password is empty.");
            }
        }

        if (Validator.isEmpty(this.caseId)) {
            jsonMap.put("status", resStatus);
            jsonMap.put("message", "Application Case Id is empty.");
        }

        if (Validator.isEmpty(this.paymentId)) {
            jsonMap.put("status", resStatus);
            jsonMap.put("message", "Payment Id is empty.");
        }

        //dType: I-Individual; C-Company
        if (Validator.isEmpty(this.dType)) {
            jsonMap.put("status", resStatus);
            jsonMap.put("message", "Prepayment Account Type is empty.");
        }

        if (Validator.isEmpty(this.pbUserId)) {
            jsonMap.put("status", resStatus);
            jsonMap.put("message", "usid is empty.");
        }

//        System.out.println("this.loginPasswd : " + this.loginPasswd);
//        System.out.println("this.loginId : " + this.loginId);
//        System.out.println("this.pbUserId : " + this.pbUserId);
//        System.out.println("Application case ID: " + this.caseId + " Prepayment dType: " + dType);
        PrepaymentDAO ppDAO = ServiceFactory.getInstance().getPrepaymentService();
        try {
            Map sessionMap = ActionContext.getContext().getSession();
            boolean blInsertPayment = false;
// temporary comment
            PaymentModel paymentModel = (PaymentModel) baseDAO.getObjectByCode("payment_id", this.paymentId, new PaymentModel());
            if (Validator.isEmpty(this.caseId)) {
                System.out.println("caseId not in request!");
            }
            ApplicationPModel utilAppCase = (ApplicationPModel) baseDAO.getModelByCode("case_id", this.caseId, new ApplicationPModel());
            if (!verifyPrepaymentAcc()) {
                resStatus = 401;
                resMessage = "Prepayment account verification failed. No payment is made.";
                paymentModel.setAppCaseModel(utilAppCase);
                paymentModel.set_operation(PaymentModel.OPERATION.UPD_PAY_REJECTED);
                ServiceFactory.getInstance().getPaymentService().update(paymentModel);
//                throw new Exception("Prepayment account verification failed. No payment is made.");
            } else {
                if (utilAppCase == null) {
                    throw new Exception("ApplicationPModel not found.");
                }

                //Check Pending Payment status
                if (!utilAppCase.getApp_status().equals(UtimapsAction.PB_STATUS.PENDING_APP_PAYMENT)) {
                    throw new Exception("Wrong Application Status.");
                }
                //************** End validation checking ***************************

                synchronized (ppDAO) {

                    if (paymentModel == null) {
                        blInsertPayment = true;
                        //                    paymentModel = createPayment(applicationModel, SystemConstants.PAYMENT_METHOD.JVP);
                    } else { //could be record existed if user choose PayBills at first
                        blInsertPayment = false;
                    }

                    ppDAO.setSession(baseDAO.getSession()); //must set!!!

                    /* IMPORTANT: startTransaction() will check exist of:-
                     1. PublicUser
                     2. PPBalance - Account Active, Balance Enough

                     If above checking pass, then go processData(). 
                     1. Insert PPTran
                     2. Update PPBalance
                     */
                    baseDAO.beginBatchTransaction();

                    if (!Validator.isEmpty(dType) && dType.equals("I")) {
                        blPaymentSuccess = ppDAO.startTransaction(SystemConstants.PREPAYMENT_CHECK.TRANSACTION.USJ_PYMNT, this.pbUserId, SystemConstants.PAYMENT_OPTION.INDIVIDUAL_DEPOSIT_ACC, paymentModel, null, Boolean.FALSE, this.usUserid);
                    } else if (!Validator.isEmpty(dType) && dType.equals("C") && !Validator.isEmpty(dCoId)) { //added @25.4.2018.
                        blPaymentSuccess = ppDAO.startTransaction(SystemConstants.PREPAYMENT_CHECK.TRANSACTION.USJ_PYMNT, dCoId, SystemConstants.PAYMENT_OPTION.COMPANY_DEPOSIT_ACC, paymentModel, null, Boolean.FALSE, this.usUserid);
                    }
                    System.out.println("blPaymentSuccess API --- " + blPaymentSuccess);
                    if (!blPaymentSuccess) {
                        resStatus = 200;
                        resMessage = "prepayment account balance is insufficient.";
                        throw new Exception("Please make sure your prepayment account balance is sufficient to make payment. Any others issue when using your prepayment account, please contact system administrator.");
                    }

                    System.out.println("blInsertPayment >>>>>>>>> " + blInsertPayment);
                    if (blInsertPayment) {
                        /* IMPORTANT: preInsert() will do:-
                         1. Insert ElsPayment & ElsPaymentItem
                         2. Update application_status='PC'
                         3. Insert status history
                         4. Insert activity log 
                         */
                        //                    ServiceFactory.getInstance().getElsPaymentService().insertWithSession(baseDAO.getSession(), paymentModel);

                    } else {
                        /* IMPORTANT: preUpdate() will do:-
                         1. Update ElsPayment payment_status='PC'
                         2. Update application_status='PC'
                         3. Insert status history
                         4. Insert activity log 
                         */
                        //                    paymentModel.setUpdated_by(usUserid);
                        //                    paymentModel.setUpdated_date(DateUtil.getCurrentTimestamp());
                        System.out.println("here update UPD_PAY_BYPREPAYMENT");
                        paymentModel.setPlatform(PaymentModel.PLATFORM.WEB); //AiMin @23.8.2019. Remark: Must update platform and payment method bcos mobile can pay erll too.
                        paymentModel.set_operation(PaymentModel.OPERATION.UPD_PAY_BYPREPAYMENT);
                        paymentModel.setAppCaseModel(utilAppCase);
                        //                    ServiceFactory.getInstance().getPaymentService().updateWithSession(baseDAO.getSession(), paymentModel);
                        ServiceFactory.getInstance().getPaymentService().update(paymentModel);
                    }

                    baseDAO.commitBatchTransaction();

                    //this part for self log checking only.
                    PpBalanceModel ppBal = null;
                    if (!Validator.isEmpty(dType) && dType.equals("I")) {
                        String usId = this.pbUserId;
                        System.out.println("userId " + usId);
                        ppBal = (PpBalanceModel) transDAO.getObjectByCode("us_id", usId, new PpBalanceModel());
                        bal_amount_ = ppBal.getBal_amount();
                        System.out.println("Individual account balance ::::: " + ppBal.getBal_amount());
                        //                } else if (!Validator.isEmpty(dType) && dType.equals("C") && !Validator.isEmpty(dCoId)) {
                        //                    ppBal = (PpBalanceModel) baseDAO.getObjectByCode("co_id", dCoId, new PpBalanceModel());
                        //                    System.out.println(dCoId + " Company account balance ::::: " + ppBal.getBal_amount());
                        //                    bal_amount_ = ppBal.getBal_amount();
                    }

                }

                //            blPaymentSuccess = true;
                if (blPaymentSuccess) {
                    jsonMap.put("actionMessage", "You have made payment successfully.");
                    jsonMap.put("Bal_amount", bal_amount_);
                    resStatus = 200;
                    resMessage = "success";
                } else {
                    resStatus = 401;
                    resMessage = "Payment error found.";
                }
            }
            jsonMap.put("blPaymentSuccess", blPaymentSuccess);
            jsonMap.put("status", resStatus);
            jsonMap.put("message", resMessage);
//            addActionMessage("You have made payment successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            new LogFunction().logError(this.getClass(), "ERROR processPayViaPrepaymentAcc.", ex);
            addActionError(ex.getMessage());

            baseDAO.rollbackBatchTransaction();
        } finally {
            baseDAO.closeSession();
            transDAO.closeSession();
        }
        response.setContentType("application/json");
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();

    }

    //For prepayment account verification screen.
    public boolean verifyPrepaymentAcc() {
        Map sessionMap = ActionContext.getContext().getSession();
        try {
            if (Validator.isEmpty(this.ssoUserId)) {

                if (Validator.isEmpty(getLoginId()) || Validator.isEmpty(getLoginPasswd())) {
                    throw new Exception("Please key in your User Id and Password to verify prepayment account.");
                }

            }

//                PublicUserModel loginedUser = (PublicUserModel) baseDAO.getObjectByCode("us_id", pbUserId, new PublicUserModel());
            PublicUserModel loginedUser = (PublicUserModel) baseDAO.getObjectByCode("fim_user_id", this.ssoUserId, new PublicUserModel());
            if (loginedUser == null) {
                throw new Exception("User Id not found.");
            }

//                if (!getLoginId().equals(loginedUser.getUs_user_id())) {
//                    throw new Exception("Sorry, you must be the logined user.");
//                }
//                Crypto crypto = new Crypto(SystemConstants.SIGNUP.SECRET_KEY);
//                if (!getLoginPasswd().trim().equals(crypto.decrypt(loginedUser.getUs_password()).trim())) {
//                    throw new Exception("Password is not match.");
//                }
            PpBalanceModel balanceModel = (PpBalanceModel) baseDAO.getObjectByCode("us_id", loginedUser.getUs_id(), new PpBalanceModel());
            if (balanceModel == null) {
                throw new Exception("Sorry, your prepayment account was not found. Please contact system administrator.");
            }

            if (!balanceModel.getBal_status().equals(SystemConstants.PREPAYMENT_CHECK.BALANCE_STATUS.ACTIVE)) {
                throw new Exception("Sorry, your prepayment account was NOT ACTIVE. Please contact system administrator.");
            }
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "ERROR verifyPrepaymentAcc.", e);
            addActionError(e.getMessage());
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String updateBillRefNo() throws IOException {
        System.out.println("****In updateBillRefNo()****" + this.paymentId);
        BaseDAO transDAO = new BaseDAOImpl();
        transDAO.setSession(baseDAO.getSession());
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//wongkk4@11Aug2021 - for pentest requirement for all API
        int resStatus = 401;
        String resMessage = "No result found.";

        System.out.println("****In updateBillRefNo()****");
        Calendar cal = Calendar.getInstance();
        String strYear = Integer.toString(cal.get(Calendar.YEAR));
        System.out.println("year" + strYear);
        try {
            if (Validator.isEmpty(this.paymentId)) {
                resStatus = 401;
                resMessage = "Payment ID is empty.";
                jsonMap.put("res_status", resStatus);
                jsonMap.put("res_message", resMessage);
                return "success";
            }

            if (Validator.isEmpty(this.caseId)) {
                jsonMap.put("res_status", resStatus);
                jsonMap.put("res_message", "Application Case Id is empty.");
                return "success";
            }

            if (Validator.isEmpty(this.paymentOptionId)) {
                jsonMap.put("res_status", resStatus);
                jsonMap.put("res_message", "Payment Option Id is empty.");
                return "success";
            }

            if (Validator.isEmpty(this.pbUserId)) {
                jsonMap.put("res_status", resStatus);
                jsonMap.put("res_message", "pbUserId is empty.");
                return "success";
            }

            if (!Validator.isEmpty(this.caseId) && !Validator.isEmpty(this.paymentId)) {
                System.out.println("here");
                PaymentModel paymentModel = (PaymentModel) transDAO.getModelById(paymentId, PaymentModel.class
                );
                ApplicationPModel utilAppCase = (ApplicationPModel) transDAO.getModelByCode("case_id", this.caseId, new ApplicationPModel());
                paymentModel.setAppCaseModel(utilAppCase);
                if (utilAppCase != null) {

                } else {
                    jsonMap.put("res_status", resStatus);
                    jsonMap.put("res_message", "Application Case not found");
                    return "success";
                }
//                }
                System.out.println("retrieve model done");

                if (paymentModel != null) {

                } else {
                    jsonMap.put("res_status", resStatus);
                    jsonMap.put("res_message", "Payment not found");
                    return "success";
                }

//                paymentModel.setP(pbUserId);
                paymentModel.setUpdated_date(DateUtil.getCurrentTimestamp());
                paymentModel.setPayment_status(PaymentModel.PAYMENT_STATUS.PAYMENT_IN_PROGRESS);
                paymentModel.setPayment_date(DateUtil.getCurrentTimestamp()); //Remark: Solved empty date, cannot post2rvs issue. @20.8.2019
                if (paymentOptionId.equals("EBPP")) {
                    paymentModel.setPayment_method(PaymentModel.PAYMENT_METHOD.EBPP);
                } else {
                    paymentModel.setPayment_method(paymentOptionId);
                }

                paymentModel.set_operation(PaymentModel.OPERATION.UPD_BILL_REF_NO);
                ServiceFactory.getInstance().getPaymentService().update(paymentModel);

                resStatus = 200;
                resMessage = "OK";

            }

        } catch (Exception e) {
            System.out.println("Failed to update payment info. " + e);
            e.printStackTrace();
            new LogFunction().logError(this.getClass(), " *** Failed to update payment info. *** ", e);
        } finally {
            baseDAO.closeSession();
            transDAO.closeSession();
        }

        jsonMap.put("res_status", resStatus);
        jsonMap.put("res_message", resMessage);

        return "success";
    }

    public String paymentAcknowledgement() {
        System.out.println("paymentAcknowledgement");

        try {
            InputStream jasperRptStream = null;
            Map reportParam = new HashMap();

            if (SystemConstants.globalServletContext == null) { //ckp @4/7/2018 mobile null servletcontext
                SystemConstants.globalServletContext = ServletActionContext.getServletContext();
            }

            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream("/WEB-INF/classes/com/utimaps/report/paymentAcknowledgment.jasper");
//            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream("/WEB-INF/classes/com/las/report/tol/paymentReceipt.jasper");
            reportParam.put("pCaseId", this.caseId);

            inputStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
            contentType = "application/pdf";
            contentDisposition = "filename=\"PaymentAcknowledgement.pdf\"";

            System.out.println("inputStream" + inputStream.toString());

        } catch (Exception e) {
            addActionError(e.getMessage());
            new LogFunction().logError(this.getClass(), " *** paymentAcknowledgement. *** ", e);
            return ERROR;
        } finally {
            baseDAO.closeSession();
        }
        return "fileDownload";
    }

    public String processPayViaCounter() throws Exception {
        BaseDAO paymentDAO = new BaseDAOImpl();
        paymentDAO.setSession(baseDAO.getSession());
        try {
            System.out.println("processPayViaCounter");
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            request.setAttribute("ignoreCsrfCheck", "true");
            PaymentModel paymentModel = (PaymentModel) paymentDAO.getModelById(this.paymentId, PaymentModel.class
            );

            ApplicationPModel utilAppCase = (ApplicationPModel) paymentDAO.getModelByCode("case_id", this.caseId, new ApplicationPModel());
            if (utilAppCase == null) {
                throw new Exception("ApplicationModel not found.");
            }
            //Check Pending Payment status
            if (!utilAppCase.getApp_status().equals(UtimapsAction.PB_STATUS.PENDING_APP_PAYMENT)) {
                throw new Exception("Wrong Application Status.");
            }

//            PaymentModel paymentModel = (PaymentModel) .getObjectByCode("payment_id", applicationModel.getPayment_id(), new PaymentModel());
            System.out.println("paymentModel : " + paymentModel.getPayment_id());
            if (paymentModel.getPayment_status().trim().equals(PaymentModel.PAYMENT_STATUS.PENDING_PAYMENT)
                    || paymentModel.getPayment_status().trim().equals(PaymentModel.PAYMENT_STATUS.PAYMENT_EXPIRED)) {
                paymentModel.setPlatform(PaymentModel.PLATFORM.COUNTER);
                paymentModel.setPayment_method(SystemConstants.PAYMENT_METHOD.CSH);
                paymentModel.setPayment_status(PaymentModel.PAYMENT_STATUS.PENDING_PAYMENT);
                paymentModel.setPayment_date(DateUtil.getCurrentTimestamp()); //stephnieia@10/7/2019 create els_payment & els_payment_item records for o9 RLL payment at counter 
//                    paymentModel.set_operation(PaymentModel.OPERATION.UPD_PAY_PENDING);
                ServiceFactory.getInstance().getPaymentService().update(paymentModel);
            }
//            }
        } catch (Exception e) {
            paymentDAO.closeSession();
            baseDAO.closeSession();
            new LogFunction().logError(this.getClass(), "ERROR processPayViaCounter.", e);
            throw e;
        }

        return SUCCESS;
    }

    public String printT126() {
        System.out.println("****In printT126 dd****");
        BaseDAO transDAO = new BaseDAOImpl();
        transDAO.setSession(baseDAO.getSession());

        try {
            PaymentModel paymentModel = (PaymentModel) transDAO.getModelById(this.paymentId, PaymentModel.class
            );
            String filePath = "USJ/BillT126";
            String filename = this.caseId + ".pdf";
            if (Validator.isEmpty(paymentModel.getBill_ref_no()) || !paymentModel.getPayment_method().equals(SystemConstants.PAYMENT_METHOD.CSH)) {
                processPayViaCounter();
                System.out.println(" *** printT126 submission id : " + this.caseId);
                String counterReport = "";
                String strPath = "/com/utimaps/report/";
                counterReport = "/WEB-INF/classes/com/utimaps/report/billT126Main.jasper";

                System.out.println(" *** printT126 strPath: " + strPath);

                Map param = new HashMap();
                param.put("pSubmissionId", this.caseId);
                param.put("SUBREPORT_DIR_MAIN", strPath);

                if (SystemConstants.globalServletContext == null) { //ckp @4/7/2018 mobile null servletcontext
                    SystemConstants.globalServletContext = ServletActionContext.getServletContext();
                }

                String outFileName = filePath + this.caseId + ".pdf";
                System.out.println("--FOA outFileName = " + outFileName);
                InputStream jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(counterReport);
                inputStream = ReportGenerator.printPdfBuffer(jasperRptStream, param, baseDAO);
                contentType = "application/pdf";
                contentDisposition = "attachment; filename=\"BillT126.pdf\"";

                if (uploadFile(inputStream, filename, filePath, "Bil26", this.caseId, null) > 0) {
                    System.out.println("Upload Failed");
                };

            }
            try {
                OBSUtil ftp = new OBSUtil();
                DrDocRepoModel docRepo = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id_application")
                        .setParameter("dr_doc_id", this.caseId).setParameter("dr_doc_application", "Bil26").uniqueResult();
                System.out.println("DrDocRepoModel here ");
                if (docRepo == null) {
                    inputStream = ftp.getFile(filePath + "/" + filename);
                } else {
                    inputStream = ftp.getFile(docRepo.getDr_doc_path() + "." + docRepo.getDr_doc_type());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            addActionError(e.getMessage());
            new LogFunction().logError(this.getClass(), " *** Print T126 failed. *** ", e);
            return ERROR;
        } finally {
            transDAO.closeSession();
            baseDAO.closeSession();
        }

        return SUCCESS;
    }

    public Integer uploadFile(InputStream inputFileStream, String filename, String filePath, String docApplication, String appId, File inputFile) {
        System.out.println("upload counter payment");
        OBSUtil ftp = new OBSUtil();
        Map paramMap = new HashMap();
        BaseDAO transDAO = new BaseDAOImpl();
        transDAO.setSession(baseDAO.getSession());
        try {
            ftp.createDirIfNotExists(filePath, Boolean.FALSE);
            //  String newDrDocId = CommonFunction.getId(20);
            String fileMimeType = ServletActionContext.getServletContext().getMimeType(filename);
            if (inputFileStream == null) {

            }
            if (filename != null) {
                if (inputFile != null) {
                    ftp.createFile(filePath + "/" + filename, inputFile);
                } else if (inputFileStream != null) {
                    ftp.createFile(filePath + "/" + filename, inputFileStream);
                } else {
                    System.out.println("file is null");
                    return 1;
                }

                if (ftp.isFileExists(filePath + "/" + filename, editMode_)) {
                    System.out.println("File Uploaded Successfully");
//                            DrDocRepoModel docRepo  = (DrDocRepoModel)dao.getModelByCode("dr_doc_id", appId, new DrDocRepoModel());
                    paramMap.clear();
                    paramMap.put("dr_doc_id", appId);
                    paramMap.put("dr_doc_application", docApplication);
                    List<DrDocRepoModel> docRepo = transDAO.list(paramMap, DrDocRepoModel.class
                    );
                    if (docRepo.size() == 0) {
                        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                        ftp.insertToFileDirectory(filePath + "/" + appId, filename, fileMimeType, appId, transDAO, docApplication, Boolean.FALSE);
                    }
                } else {
                    System.out.println("Error File Upload");
                    return 1;
                }
            } else {
                return 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        } finally {
            transDAO.closeSession();
        }
        return 0;
    }

    public void updatePayment() throws IOException {
        System.out.println("UPDATE PYMT API - " + new java.sql.Timestamp(System.currentTimeMillis()));
        int resStatus = 401;
        String resMsg = "Server Not Responding.";
        Map pymtMap = new HashMap();

        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        BufferedReader rd = request.getReader();
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            content.append(line).append("\n");
        }
        rd.close();

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(content.toString());
            JSONObject jsonData = (JSONObject) jsonObject.get("upd_data");
            System.out.println("json - " + jsonObject);

            this.pbUserId = (String) jsonObject.get("pbUserId");
            this.hValue = (String) jsonObject.get("hValue");
            this.pymtState = (String) jsonObject.get("pymtState");
            this.reqCaseId = (String) jsonObject.get("reqCaseId");
            this.reqPymtId = (String) jsonObject.get("reqPymtId");
//            this.selectStep = (String) jsonObject.get("selectStep");

            this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
            if (this.hValue.equals(this.hashKey)) {
                resStatus = 200;
                resMsg = "OK";
                ApplicationPModel updateModel = new ApplicationPModel();

                switch (pymtState) {
                    case "forceDemo":

                        System.out.println("UPDATE PYMT FOR DEMO MAY 24");
                        updateModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", getReqCaseId(), new ApplicationPModel());
                        PaymentModel updatePymtModel = (PaymentModel) retrieverDAO.getModelByCode("payment_id", getReqPymtId(), new PaymentModel());

                        System.out.println("case id - " + updateModel.getCase_id());
//                        updateModel.setApp_status(UtimapsAction.PB_STATUS.APP_PAYMENT_COMPLETED);
//                        updateModel.setWf_status(UtimapsAction.WF_STATUS.APPLICATION_PAYMENT_COMPLETED);
                        serviceFactory.getUtilAppService().updateForPayment(updateModel);

                        System.out.println("pymt id - " + updatePymtModel.getPayment_id());
                        updatePymtModel.setPayment_status("PC");
                        updatePymtModel.setPayment_status_date(DateUtil.getCurrentTimestamp());

//                        serviceFactory.getPaymentService().update(updatePymtModel);
//                        updateModel.updatableColumns = new String[]{"pj_div", "case_div", "pj_name", "land_dist", "land_desc", "client_name"};
//                        String[] dataFields = {"case_id", "job_id", "case_type", "case_div", "case_seq", "case_year", "case_div", "usj_div", "usj_seq", "usj_year", "pj_div", "pj_name", "land_dist", "land_desc", "client_name"};
//                        setModelData((JSONObject) jsonData.get("app"), dataFields, updateModel);
//
////                        updateModel.setCase_div(updateModel.getPj_div());
//                        SurveyFirmPModel updateFirm = new SurveyFirmPModel();
//                        updateFirm.updatableColumns = new String[]{"firm_oic", "firm_email", "firm_country_code", "firm_contact_num", "firm_fax_country_code", "firm_fax_num"};
//                        setModelData((JSONObject) jsonData.get("firm"), firmFields, updateFirm);
//
////                        updateModel.setAppFirmPModel(updateFirm);
//                        serviceFactory.getSurveyFirmService().update(updateFirm);
                        break;
                    default:
                        break;
                }

//                System.out.println("factory call");
//
                ApplicationPModel returnAppModel = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", getReqCaseId(), new ApplicationPModel());
                PaymentModel returnPymtModel = (PaymentModel) retrieverDAO.getModelByCode("payment_id", getReqPymtId(), new PaymentModel());
                Map returnMap = getDataMap(returnAppModel, appFields);
                appCaseMap.put("util_app", returnMap);

                pymtMap = getDataMap(returnPymtModel, pymtFields);
//                SurveyFirmPModel returnFirmModel = (SurveyFirmPModel) retrieverDAO.getModelByCode("case_id", updateModel.getCase_id(), new SurveyFirmPModel());
//                suppDocMap = checkSuppDoc(returnAppModel);
//                appCaseMap.put("supp_doc", suppDocMap);
//
//
//                Map rtnFirmMap = getDataMap(returnFirmModel, firmFields);
//                appCaseMap.put("survey_firm", rtnFirmMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrieverDAO.closeSession();

//            jsonMap.put("dropdown", fetchDropDowns());
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMsg);
            jsonMap.put("appCase", appCaseMap);
            jsonMap.put("pymtCase", pymtMap);
//            jsonMap.put("docList", fetchDocList("APP"));

            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        }
    }

    public String sPayCancel() throws IOException {
        BaseDAO transDAO = new BaseDAOImpl();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//wongkk4@11Aug2021 - for pentest requirement for all API
        int code = -1;
        String status = "error";
        String message = "No record found.";

        System.out.println("****In sPayCancel()****");
        Calendar cal = Calendar.getInstance();
        String strYear = Integer.toString(cal.get(Calendar.YEAR));
        System.out.println("year" + strYear);
        try {

            if (Validator.isEmpty(this.orderId)) {
                jsonMap.put("response", code);
                jsonMap.put("status", status);
                jsonMap.put("message", "order id is empty.");
                return "success";
            }
            if (Validator.isEmpty(this.merOrderNo)) {
                jsonMap.put("response", code);
                jsonMap.put("status", status);
                jsonMap.put("message", "merOrderNo is empty.");
                return "success";
            }

            if (Validator.isEmpty(this.paymentCategory)) {
                jsonMap.put("response", code);
                jsonMap.put("status", status);
                jsonMap.put("message", "paymentCategory is empty.");
                return "success";
            }

            SarawakPayCheck spayCheck = new SarawakPayCheck();

            if (paymentCategory.equals(SystemConstants.PAYMENT_CATEGORY.UTIMAPS)) {
                System.out.println("Cancelling TOL Payment");
                PaymentModel lsPayModel = null;

                lsPayModel = spayCheck.checkUsjPaymentRecord(baseDAO, merOrderNo);

                message = "success";
                if (lsPayModel != null) {
                    System.out.println("lsPayModel   " + lsPayModel);
                    spayCheck.updateUsjPendingPayment(baseDAO, lsPayModel, this.orderId);
                }
            }

            code = 0;
            status = "success";

        } catch (Exception e) {
            System.out.println("Failed to check Sarawak Pay status. " + e);
            new LogFunction().logError(this.getClass(), " *** Failed to Sarawak Pay status. *** ", e);
        } finally {
            baseDAO.closeSession();
            transDAO.closeSession();
        }

        jsonMap.put("response", code);
        jsonMap.put("status", status);
        jsonMap.put("message", message);

        return "success";
    }

    public String sPayDecrypt() throws IOException {
        BaseDAO transDAO = new BaseDAOImpl();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//wongkk4@11Aug2021 - for pentest requirement for all API
        int code = -1;
        String status = "error";
        String message = "No record found.";

        System.out.println("****In sPayDecrypt()****");
        Calendar cal = Calendar.getInstance();
        String strYear = Integer.toString(cal.get(Calendar.YEAR));
        System.out.println("merOrderNo " + this.merOrderNo);
        System.out.println("activity " + this.activity);
        System.out.println("trandate " + this.tranDate);
        System.out.println("orderNo " + this.orderNo);
        System.out.println("year" + strYear);
        try {

            if (Validator.isEmpty(this.merOrderNo)) {
                jsonMap.put("response", code);
                jsonMap.put("status", status);
                jsonMap.put("message", "merOrderNo is empty.");
                return "success";
            }

            if (Validator.isEmpty(this.activity)) {
                jsonMap.put("response", code);
                jsonMap.put("status", status);
                jsonMap.put("message", "Activity Code is empty.");
                return "success";
            }

            if (activity.equals(PbUtimapsApiAction.SPAY_DECRPYT_ACTIVITY_CODE.SPAY_UPDATE_PENDING)) {
                //find back the order, set to PP
                PaymentModel payment = (PaymentModel) baseDAO.getObjectByCode("bill_ref_no", this.merOrderNo, new PaymentModel());

                String strSqlWhere = "";
                String app_id = "";

                strSqlWhere = "payment_id ='" + payment.getPayment_id() + "'";

                app_id = payment.getCase_id();
                ApplicationPModel applicationModel = (ApplicationPModel) baseDAO.getModelByCode("case_id", payment.getCase_id(), new ApplicationPModel());

                if (payment == null) {
                    System.out.println("1. Payment record not found: " + this.merOrderNo);
                    throw new Exception("Payment record not found: " + this.merOrderNo);
                } else {
                    if (!payment.getPayment_status().equals(PaymentModel.PAYMENT_STATUS.PAYMENT_IN_PROGRESS)) {
                        System.out.println("1. Invalid payment status: " + this.merOrderNo);
                        throw new Exception("Invalid payment status: " + this.merOrderNo);
                    }
                }

                baseDAO.beginBatchTransaction();
                payment.setPayment_status(PaymentModel.PAYMENT_STATUS.PENDING_PAYMENT);
                payment.setPayment_date(DateUtil.getCurrentTimestamp());
                payment.setPay_ref_no("");
//                    BaseActionSupport.defaultUpdateProperties(payment, "LXGTOL");
                baseDAO.getSession().update(payment);

                applicationModel.setApp_status(UtimapsAction.PB_STATUS.PENDING_APP_PAYMENT);
//                    BaseActionSupport.defaultUpdateProperties(applicationModel, "LXGTOL");
                baseDAO.getSession().update(applicationModel);

                baseDAO.commitBatchTransaction();

            } else if (activity.equals(PbUtimapsApiAction.SPAY_DECRPYT_ACTIVITY_CODE.SPAY_UPDATE_COMPLETE)) {
                PaymentModel payment = (PaymentModel) baseDAO.getObjectByCode("bill_ref_no", this.merOrderNo, new PaymentModel());
//                    LsRAppCaseModel applicationModel = (LsRAppCaseModel) baseDAO.getModelByCode("payment_id",payment.getPayment_id(), LsRAppCaseModel.class);

                String strSqlWhere = "";
                String app_id = "";

                strSqlWhere = "payment_id ='" + payment.getPayment_id() + "'";

                app_id = payment.getCase_id();
                ApplicationPModel applicationModel = (ApplicationPModel) baseDAO.getModelByCode("case_id", payment.getCase_id(), new ApplicationPModel());

                if (payment == null) {
                    System.out.println("2. Payment record not found: " + this.merOrderNo);
                    throw new Exception("Payment record not found: " + this.merOrderNo);
                } else {
                    if (!payment.getPayment_status().equals(PaymentModel.PAYMENT_STATUS.PAYMENT_IN_PROGRESS)) {
                        System.out.println("2. Invalid status for payment: " + this.merOrderNo);
                        throw new Exception("Invalid status for payment: " + this.merOrderNo);
                    }
                }

                baseDAO.beginBatchTransaction();

                //UPDATE PAYMENT_STATUS to 359
                String tranDate = this.tranDate;
                payment.setPayment_status_date(DateUtil.getTimestampFromDate(DateUtil.getDate(tranDate, "yyyyMMddHHmmss"))); //AiMin @7.7.2020. Remark: yyyyMMddhhmmss hh is 12 Hours time format, HH is 24 Hours time format.
                payment.setPayment_status(PaymentModel.PAYMENT_STATUS.PAYMENT_COMPLETED);
                payment.setPay_ref_no(this.orderNo); //sarawakpay ref number
//                    BaseActionSupport.defaultUpdateProperties(payment, "LXGTOL");
                baseDAO.getSession().update(payment);

                applicationModel.setApp_status(UtimapsAction.PB_STATUS.APP_PAYMENT_COMPLETED);
//                    BaseActionSupport.defaultUpdateProperties(applicationModel, "LXGTOL");
                baseDAO.getSession().update(applicationModel);

                baseDAO.commitBatchTransaction();

            }

            code = 0;
            status = "success";
            message = "success";

        } catch (Exception e) {
            System.out.println("Failed to update Sarawak Pay status. " + e);
            new LogFunction().logError(this.getClass(), " *** Failed to update Sarawak Pay status. *** ", e);
        } finally {
            baseDAO.closeSession();
            transDAO.closeSession();
        }

        jsonMap.put("response", code);
        jsonMap.put("code", code);
        jsonMap.put("status", status);
        jsonMap.put("message", message);

        return "success";
    }

    public void searchPaymentCashtran() throws IOException {
        BaseDAO retrieverDAO = new BaseDAOImpl();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//wongkk4@11Aug2021 - for pentest requirement for all API
        int code = -1;
        String status = "error";
        String message = "No record found.";

        try {
            if (Validator.isEmpty(this.billRefNo)) { //item_reference_number
                jsonMap.put("response", code);
                jsonMap.put("status", status);
                jsonMap.put("message", "Bill Reference No. is empty.");
//                return "success";
            }

            if (Validator.isEmpty(this.hashTotal)) {
                jsonMap.put("response", code);
                jsonMap.put("status", status);
                jsonMap.put("message", "Hash total is empty.");
//                return "success";
            }

            String commonHashTotal = new CommonFunction().getHashCashTran(this.billRefNo);
            System.out.println("this.commonHashTotal : " + commonHashTotal);
            System.out.println("this.hshT : " + hashTotal);

            if (this.hashTotal.equals(commonHashTotal)) {
                System.out.println("match hashtotal");
                PaymentItemModel paymentItmodel = (PaymentItemModel) retrieverDAO.getModelByCode("ITEM_REF_NO", billRefNo, new PaymentItemModel());
                System.out.println("paymentItModel " + paymentItmodel.getID());
                System.out.println("usermodel " + paymentItmodel.getPayment_id());
                PaymentModel paymentModel = (PaymentModel) retrieverDAO.getModelById(paymentItmodel.getPayment_id(), PaymentModel.class);

                String app_id = "";

                ApplicationPModel utilAppCase = (ApplicationPModel) retrieverDAO.getModelByCode("case_id", paymentModel.getCase_id(), new ApplicationPModel());

                if (paymentItmodel != null) {
                    jsonMap.put("billRefNo", paymentItmodel.getItem_ref_no());
                    jsonMap.put("paymentAmount", paymentItmodel.getItem_amount());
                    jsonMap.put("revenueCode", paymentItmodel.getRvr_code());
                    jsonMap.put("subCode", paymentItmodel.getSubcode());
                } else {
                    System.out.println("paymentItmodel not found");
                }

                if (paymentModel != null) {
                    jsonMap.put("paymentStatus", paymentModel.getPayment_status());

                } else {
                    System.out.println("paymentModel not found");
                }

                if (utilAppCase != null) {
                    String divCode = utilAppCase.getCase_div();

                    jsonMap.put("divCode", divCode); //test first
                    jsonMap.put("shortkey1", utilAppCase.getCase_ref());
                    jsonMap.put("shortkey1Str", utilAppCase.getCase_ref());
                } else {
                    System.out.println("paymentModel not found");
                }

                code = 0;
                status = "Success";
                message = "Retrieve cashtran payment details successfully.";
                jsonMap.put("response", code);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Retrieve cashtran payment detail unsuccessfully. " + e);
            new LogFunction().logError(this.getClass(), " *** Failed to retrieve cashtran payment detail successfully. *** ", e);
        } finally {
            retrieverDAO.closeSession();
            baseDAO.closeSession();
        }

        response.setContentType("application/json");
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
//        System.out.println(jsonMap.toString());
//        jsonMaps.put("status", status); no need
//        jsonMaps.put("message", message); no need

//       return "success";
    }

    // =========================================== UTITLITY METHODS =========================================================
    private void setModelData(JSONObject jsonData, String[] dataFields, ModelBase webModel) throws IOException {
//        System.out.println("====================setModelData=========================\n");
        String columnName = null;
        String value = "";
        int valueInt = 0;
        Object getterObject;
        for (String field : dataFields) {
            try {
//                System.out.println(field);
                if (jsonData.get(field) == null) {
//                    System.out.println("field - " + field + " is null");
                } else {
//                    System.out.println(jsonData.get(field).getClass());
                    columnName = WordUtils.capitalize(field);
                    Method m = webModel.getClass().getMethod("get" + columnName);

                    if (jsonData.get(field).getClass().toString().equals("class java.lang.String")) {
                        m = webModel.getClass().getMethod("set" + columnName, String.class
                        );
                        value = (String) jsonData.get(field);
                        m.invoke(webModel, value);

                    } else if (jsonData.get(field).getClass().toString().equals("class java.lang.Double")) {
                        m = webModel.getClass().getMethod("set" + columnName, Double.class
                        );
                        double valueL = (Double) jsonData.get(field);
                        m.invoke(webModel, valueL);

                    } else {
                        m = webModel.getClass().getMethod("set" + columnName, Integer.class
                        );
                        long valueL = (Long) jsonData.get(field);
                        valueInt = (int) valueL;
                        m.invoke(webModel, valueInt);
                    }
                }
//                if(value == null || Validator.isEmpty(value.toString())) {
//                    System.out.println("value with no value");
//                } else {
//                    System.out.println("getter objetc" + getterObject.getClass().));
//                }
            } catch (NoSuchMethodException ex) {
                System.out.println("no such method" + ex);
            } catch (SecurityException ex) {
                System.out.println("security exc" + ex);
            } catch (IllegalAccessException ex) {
                System.out.println("illegal access" + ex);
            } catch (IllegalArgumentException ex) {
                System.out.println("illegal arg" + ex);
            } catch (InvocationTargetException ex) {
                System.out.println("illegal " + ex);
            }
        }
//        System.out.println("\n====================END setModelData=========================");
    }

    private Map getDataMap(Object from, String[] dataFields) {
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
                    Logger.getLogger(PbUtimapsApiAction.class
                            .getName()).log(Level.SEVERE, null, ex);

                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(PbUtimapsApiAction.class
                            .getName()).log(Level.SEVERE, null, ex);

                } catch (InvocationTargetException ex) {
                    Logger.getLogger(PbUtimapsApiAction.class
                            .getName()).log(Level.SEVERE, null, ex);

                }
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(PbUtimapsApiAction.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (SecurityException ex) {
                Logger.getLogger(PbUtimapsApiAction.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return modelMap;
    }

    public void retrieveSessionInfo() {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//wongkk4@11Aug2021 - for pentest requirement for all API
        int code = -1;
        String status = "error";
        String message = "No record found.";
        String passwd = "";
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-store");

            byte[] decodedParam = Base64.decodeBase64(request.getParameter("param"));
            String strParam = new String(decodedParam);

            if (Validator.isEmpty(this.pbUserName)) {
                jsonMap.put("response", code);
                jsonMap.put("status", status);
                jsonMap.put("message", "User's ID is empty.");
            }
            if (Validator.isEmpty(this.hValue)) {
                jsonMap.put("response", code);
                jsonMap.put("status", status);
                jsonMap.put("message", "hValue is empty.");
            }

            this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserName, "UTiMAPS");

            if (this.hValue.equals(this.hashKey)) {
                PublicUserModel user = null;
                user = (PublicUserModel) baseDAO.getObjectByCode("us_user_id", this.pbUserName, new PublicUserModel());
                if (user != null) {
//                    Crypto crypto = new Crypto(SystemConstants.SIGNUP.SECRET_KEY); //dynamic key/secret key/public key
//                    passwd = user.getUs_password().trim().concat("^").concat(user.getPubkey());
//                    Crypto crypto = new Crypto(SystemConstants.SIGNUP.SECRET_KEY); //dynamic key/secret key/public key
//                    passwd = crypto.decrypt(user.getUs_password()).trim().concat("^").concat(user.getPubkey());
//                    if (strParam.equals(passwd)) { // if password is match
//                    if(noCheckPswd || user.getUs_password().trim().equals(Encriptor.encode(password).trim())){ // if password is match
                    /* [End] @20Mar2014 */
                    // -------- check account status ------------------
                    if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.ACTIVE)) { // if ACTIVE
//                            Calendar today = Calendar.getInstance();
//                            if (user.getUs_expiry_date() != null) { // expiry date is for internal use only
//                                if (today.getTime().after(user.getUs_expiry_date())) { // check account expiry date
//                                    user.setUs_status("N"); // set to INACTIVE if already expired
//                                    user.updatableColumns = new String[]{"Us_id", "Us_status"};
////                                    beginBatchTransaction();
//                                    baseDAO.getSession().update(user);
////                                    commitBatchTransaction();
//                                    throw new CustomBaseException(new ActionSupport().getText("errors.accExpired"));
//                                }
//                            }
                    } else if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.INACTIVE)) { // if INACTIVE
                        throw new CustomBaseException(new ActionSupport().getText("errors.accINACTIVE"));
                    } else if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.CANCELLED)) { // if CANCELLED
                        throw new CustomBaseException(new ActionSupport().getText("errors.accCANCELLED"));
                    } else if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.LOCKED)) { // if LOCKED account
                        throw new CustomBaseException(new ActionSupport().getText("errors.accLOCKED"));
                    }// ------------- end check account status ---------------------
                    dataMap.clear(); // remove all setting, in case logged in using internal id b4.
                    dataMap.put("p_logined", "true");
                    dataMap.put("p_loginId", user.getUs_user_id());
                    dataMap.put("p_userName", user.getUs_user_name());
                    dataMap.put("p_userId", user.getUs_id());
                    dataMap.put("userId", user.getUs_id());
                    dataMap.put("loginId", user.getUs_user_id());
                    dataMap.put("userName", user.getUs_user_name());
                    dataMap.put("userEmail", user.getUs_email()); //amywyp @ 10-05-2018
//                        dataMap.put("userHpCode", user.getUs_hp_country_code());
//                        dataMap.put("userHpNumber", user.getUs_hp_number());
                    dataMap.put("ssoUserId", user.getFim_user_id());
                    dataMap.put("userHpCode", user.getUs_hp_country_code());
                    dataMap.put("userHpNumber", user.getUs_hp_country_code() + user.getUs_hp_number());

                    // Get Company Id Based on sarawak corp id
                    if (Validator.isEmpty(this.ssoCorpId)) {
                        for (UserCompanyModel ucm : user.getUserCompanyList()) {
                            pCoId = ucm.getCustCompany().getCo_id();
                            pCoEmail = ucm.getCustCompany().getCo_email();
                        }
                    } else {
                        CustCompanyModel custCompany = (CustCompanyModel) baseDAO.getObjectByCode("SWKID_CORP", this.ssoCorpId, new CustCompanyModel());
                        pCoId = custCompany.getCo_id();
                        pCoEmail = custCompany.getCo_email();
                    }
                    System.out.println("pCoId " + pCoId + " pCoEmail " + pCoEmail);
                    dataMap.put("p_coId", pCoId);
                    dataMap.put("p_coEmail", pCoEmail);

//                        dataMap.put("p_loginId", userModel.get("US_USER_ID").toString());
//                        dataMap.put("p_userName", userModel.get("US_USER_NAME").toString());
//                        dataMap.put("p_userId", userModel.get("US_ID").toString());
//                        dataMap.put("userId", userModel.get("US_ID").toString());
//                        dataMap.put("loginId", userModel.get("US_USER_ID").toString());
//                        dataMap.put("userName", userModel.get("US_USER_NAME").toString());
//                        dataMap.put("userEmail", userModel.get("US_EMAIL").toString()); //amywyp @ 10-05-2018
                    //setup title menu
//                        User userModel = null;
//                        LoginAction loginAction = new LoginAction();
//                        loginAction.populateMenu2(userModel);
//                        loginAction.populateMenu2(null,SystemConstants.SYSTEM_TYPE.PUBLIC); //serene @ 31/3/2023 :: add system type
//                        dataMap.put("menuList", (String)ActionContext.getContext().getSession().get("menuList"));
//                        dataMap.put("moduleTile", (String)ActionContext.getContext().getSession().get("moduleTile"));
                    jsonMap.put("data", dataMap);
//                    }
                    code = 0;
                    status = "Success";
                    message = "Retrieve session info successfully.";
//                    } else {
//                        code = -1;
//                        status = "Error";
//                        message = "Password not match!";
//                    }
                } else {
                    message = "Invalid user";
                    status = "Error";
                    System.out.println("no user !!!!");
                }

            } else {
                System.out.println("retrieveSessionInfo different hashkey");
                jsonMap.put("response", code);
                jsonMap.put("status", status);
                jsonMap.put("message", "hValue invalid");
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve session info. " + e);
            e.printStackTrace();
            new LogFunction().logError(this.getClass(), " *** Failed to retrieve session info. *** ", e);
        } finally {
            retrieverDAO.closeSession();
        }

        jsonMap.put("response", code);
        jsonMap.put("status", status);
        jsonMap.put("message", message);
        try {
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } catch (Exception ex) {
            System.out.println("*** Failed to response server error. ***");
            new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
        }
    }

    public String publicLogin() {
        BaseDAO transDAO = new BaseDAOImpl();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//wongkk4@11Aug2021 - for pentest requirement for all API
        int code = -1;
        String status = "error";
        String message = "No record found.";
        if (checkValidation()) {
            code = 0;
            status = "success";
            message = "success";
            new CommonFunction().auditLogin(SystemConstants.AUDIT_LOGIN.ACTION.LOGIN, "PUB", this.loginStatus, this.pbUserId);
        }
        jsonMap.put("response", code);
        jsonMap.put("status", status);
        jsonMap.put("message", message);

        return "success";
    }

    public String publicLogout() {
        BaseDAO transDAO = new BaseDAOImpl();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//wongkk4@11Aug2021 - for pentest requirement for all API
        int code = -1;
        String status = "error";
        String message = "No record found.";
        if (checkValidation()) {
            code = 0;
            status = "success";
            message = "success";
            new CommonFunction().auditLogin(SystemConstants.AUDIT_LOGIN.ACTION.LOGOUT, "PUB", SystemConstants.AUDIT_LOGIN.STATUS.SUCCESS, this.pbUserId);
        }
        jsonMap.put("response", code);
        jsonMap.put("status", status);
        jsonMap.put("message", message);

        return "success";
    }

    public boolean checkValidation() {
        System.out.println("check validation");
        if (Validator.isEmpty(getPbUserId()) || Validator.isEmpty(this.hValue)) {
            jsonMap.put("message", "Required param cannot be empty.");
            return false;
        } else {
            System.out.println("hValue " + this.hValue);
            System.out.println("user " + getPbUserId());
            this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserId, "UTiMAPS");
            System.out.println("hashkey " + this.hashKey);
            if (this.hValue.equals(this.hashKey)) {
                return true;
            }
        }
        jsonMap.put("message", "Invalid user id");
        return false;
    }

    public void tempRetrieveSessionInfo() {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//wongkk4@11Aug2021 - for pentest requirement for all API
        int code = -1;
        String status = "error";
        String message = "No record found.";
        String passwd = "";
        BaseDAO retrieverDAO = new BaseDAOImpl();
        retrieverDAO.setSession(baseDAO.getSession());
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-store");

//            byte[] decodedParam = Base64.decodeBase64(request.getParameter("param"));
//            String strParam = new String(decodedParam);
            if (Validator.isEmpty(this.pbUserName)) {
                jsonMap.put("response", code);
                jsonMap.put("status", status);
                jsonMap.put("message", "User's ID is empty.");
            }
            if (Validator.isEmpty(this.hValue)) {
                jsonMap.put("response", code);
                jsonMap.put("status", status);
                jsonMap.put("message", "hValue is empty.");
            }

            this.hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", this.pbUserName, "UTiMAPS");

            if (this.hValue.equals(this.hashKey)) {
                PublicUserModel user = null;
                user = (PublicUserModel) baseDAO.getObjectByCode("us_user_id", this.pbUserName, new PublicUserModel());
                if (user != null) {
//                    Crypto crypto = new Crypto(SystemConstants.SIGNUP.SECRET_KEY); //dynamic key/secret key/public key
                    passwd = user.getUs_password().trim().concat("^").concat(user.getPubkey());
//                    Crypto crypto = new Crypto(SystemConstants.SIGNUP.SECRET_KEY); //dynamic key/secret key/public key
//                    passwd = crypto.decrypt(user.getUs_password()).trim().concat("^").concat(user.getPubkey());
//                    if (strParam.equals(passwd)) { // if password is match
//                    if(noCheckPswd || user.getUs_password().trim().equals(Encriptor.encode(password).trim())){ // if password is match
                    /* [End] @20Mar2014 */
                    // -------- check account status ------------------
                    if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.ACTIVE)) { // if ACTIVE
//                            Calendar today = Calendar.getInstance();
//                            if (user.getUs_expiry_date() != null) { // expiry date is for internal use only
//                                if (today.getTime().after(user.getUs_expiry_date())) { // check account expiry date
//                                    user.setUs_status("N"); // set to INACTIVE if already expired
//                                    user.updatableColumns = new String[]{"Us_id", "Us_status"};
////                                    beginBatchTransaction();
//                                    baseDAO.getSession().update(user);
////                                    commitBatchTransaction();
//                                    throw new CustomBaseException(new ActionSupport().getText("errors.accExpired"));
//                                }
//                            }
                    } else if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.INACTIVE)) { // if INACTIVE
                        throw new CustomBaseException(new ActionSupport().getText("errors.accINACTIVE"));
                    } else if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.CANCELLED)) { // if CANCELLED
                        throw new CustomBaseException(new ActionSupport().getText("errors.accCANCELLED"));
                    } else if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.LOCKED)) { // if LOCKED account
                        throw new CustomBaseException(new ActionSupport().getText("errors.accLOCKED"));
                    }// ------------- end check account status ---------------------
                    dataMap.clear(); // remove all setting, in case logged in using internal id b4.
                    dataMap.put("p_logined", "true");
                    dataMap.put("p_loginId", user.getUs_user_id());
                    dataMap.put("p_userName", user.getUs_user_name());
                    dataMap.put("p_userId", user.getUs_id());
                    dataMap.put("userId", user.getUs_id());
                    dataMap.put("loginId", user.getUs_user_id());
                    dataMap.put("userName", user.getUs_user_name());
                    dataMap.put("userEmail", user.getUs_email()); //amywyp @ 10-05-2018
//                        dataMap.put("p_loginId", userModel.get("US_USER_ID").toString());
//                        dataMap.put("p_userName", userModel.get("US_USER_NAME").toString());
//                        dataMap.put("p_userId", userModel.get("US_ID").toString());
//                        dataMap.put("userId", userModel.get("US_ID").toString());
//                        dataMap.put("loginId", userModel.get("US_USER_ID").toString());
//                        dataMap.put("userName", userModel.get("US_USER_NAME").toString());
//                        dataMap.put("userEmail", userModel.get("US_EMAIL").toString()); //amywyp @ 10-05-2018
                    //setup title menu
//                        User userModel = null;
//                        LoginAction loginAction = new LoginAction();
//                        loginAction.populateMenu2(userModel);
//                        loginAction.populateMenu2(null,SystemConstants.SYSTEM_TYPE.PUBLIC); //serene @ 31/3/2023 :: add system type
//                        dataMap.put("menuList", (String)ActionContext.getContext().getSession().get("menuList"));
//                        dataMap.put("moduleTile", (String)ActionContext.getContext().getSession().get("moduleTile"));
                    jsonMap.put("data", dataMap);
//                    }
                    code = 0;
                    status = "Success";
                    message = "Retrieve session info successfully.";
//                    } else {
//                        code = -1;
//                        status = "Error";
//                        message = "Password not match!";
//                    }
                } else {
                    message = "Invalid user";
                    status = "Error";
                    System.out.println("no user !!!!");
                }

            } else {
                System.out.println("retrieveSessionInfo different hashkey");
                jsonMap.put("response", code);
                jsonMap.put("status", status);
                jsonMap.put("message", "hValue invalid");
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve session info. " + e);
            new LogFunction().logError(this.getClass(), " *** Failed to retrieve session info. *** ", e);
        } finally {
            retrieverDAO.closeSession();
        }

        jsonMap.put("response", code);
        jsonMap.put("status", status);
        jsonMap.put("message", message);
        try {
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } catch (Exception ex) {
            System.out.println("*** Failed to response server error. ***");
            new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
        }
    }

    public String initUtilPrecheck(JobDetailModel jobModel) {
        String strReturn = "";

        String ROCheck = "nro";

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
        }

        return strReturn;
    }

    //serene @ 21/11/2024 -> eis aiman @ 4/12/2024 -> utimaps aiman @ 2/1/2025
    //setup noification and email in live using LasSmsOTP. 
    public void publicSendNotification() {
        System.out.println("============== publicSendNotification ==============");
        int resStatus = 401;
        String resMessage = "No result found.";
        Map mailParam = new HashMap();
        AutoEmail emailType = null;
        AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
        AutoEmail autoEmail = new AutoEmail();
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-store");

        try {
            BufferedReader rd = request.getReader();
            new LogFunction().logInfo(this.getClass(), "content: " + rd.toString());
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                content.append(line).append("\n");
            }
            rd.close();
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(content.toString());
            request.setAttribute("ignoreCsrfCheck", "true");
            JSONObject jsonData = (JSONObject) jsonObject.get("data");
            String securityCode = (String) jsonData.get("securityCode");
            String userHpNumber = (String) jsonData.get("userHpNumber");
            String otpGenerated = (String) jsonData.get("otpGenerated");
            String strExpTimeOtp = (String) jsonData.get("expTimeOtp");
//            new LogFunction().logInfo(this.getClass(), "jsonData  " + jsonData);
            if (!Validator.isEmpty(userHpNumber)) {
//                userHpNumber = "0168821179";

                mailParam.put(EmailQueueTrigger.SMS_TO, "+" + userHpNumber);
                mailParam.put(EmailQueueTrigger.MAIL_TO, "");
                mailParam.put("pSys", "Elasis");
                mailParam.put("pPurpose", "Please using OTP below  to verify your prepayment account before " + strExpTimeOtp + " ");
                mailParam.put("pSecurityCode", securityCode);
                mailParam.put("pOTP", otpGenerated);
                emailType = autoEmailDAO.getAutoEmailByCode("LasSmsOtp", autoEmail); // 09.01.2025 use same otp sms as LAS
                new EmailQueueTrigger().sendEMail(mailParam, emailType);
                resStatus = 500;
                String maskNum = userHpNumber.substring(0, 7).replaceAll("[0-9]", "x") + userHpNumber.substring(7, userHpNumber.length());
                resMessage = "SMS successfully send to mobile number : " + maskNum;
            }

            System.out.println("============== publicSendNotification response build json ==============");

            jsonMap.clear();
            jsonMap.put("response", 0);
            jsonMap.put("res_status", resStatus);
            jsonMap.put("res_message", resMessage);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
//            new LogFunction().logInfo(this.getClass(), "response ::" + jsonMap);
        } catch (Exception e) {
            try {
                e.printStackTrace();
                jsonMap.put("response", -1);
                jsonMap.put("response", resStatus);
                jsonMap.put("message", e.getMessage());
                response.setContentType("application/json");
                response.getWriter().append(new Gson().toJson(jsonMap));
                response.flushBuffer();
            } catch (Exception ex) {
//                new LogFunction().logInfo(this.getClass(), "*** Failed to response server error. ***");
            }
        }
//        return "success";
    }

    public String setupHashParamKey() {
        System.out.println("hash key param setup");
//        String paramUserName = "usersd01"; //hardcode testing
        String paramUserName = this.pbUserId;
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

    public String getPbUserName() {
        return pbUserName;
    }

    public void setPbUserName(String pbUserName) {
        this.pbUserName = pbUserName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getCode_acr() {
        return code_acr;
    }

    public void setCode_acr(String code_acr) {
        this.code_acr = code_acr;
    }

    public String gethValue() {
        return hValue;
    }

    public void sethValue(String hValue) {
        this.hValue = hValue;
    }

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getPbUserId() {
        return pbUserId;
    }

    public void setPbUserId(String pbUserId) {
        this.pbUserId = pbUserId;
    }

    public Map<String, Object> getJsonMap() {
        return jsonMap;
    }

    public void setJsonMap(Map<String, Object> jsonMap) {
        this.jsonMap = jsonMap;
    }

    public String getCaseOwnerId() {
        return caseOwnerId;
    }

    public void setCaseOwnerId(String caseOwnerId) {
        this.caseOwnerId = caseOwnerId;
    }

    public String getSelectStep() {
        return selectStep;
    }

    public void setSelectStep(String selectStep) {
        this.selectStep = selectStep;
    }

    public String getReqCaseId() {
        return reqCaseId;
    }

    public void setReqCaseId(String reqCaseId) {
        this.reqCaseId = reqCaseId;
    }

    public String getReqJobId() {
        return reqJobId;
    }

    public void setReqJobId(String reqJobId) {
        this.reqJobId = reqJobId;
    }

    public String getReqMsgId() {
        return reqMsgId;
    }

    public void setReqMsgId(String reqMsgId) {
        this.reqMsgId = reqMsgId;
    }

    public String[] getDocTypeList() {
        return docTypeList;
    }

    public void setDocTypeList(String[] docTypeList) {
        this.docTypeList = docTypeList;
    }

    public String getReqPymtId() {
        return reqPymtId;
    }

    public void setReqPymtId(String reqPymtId) {
        this.reqPymtId = reqPymtId;
    }

    public String getPymtState() {
        return pymtState;
    }

    public void setPymtState(String pymtState) {
        this.pymtState = pymtState;
    }

    public String getPbLoginId() {
        return pbLoginId;
    }

    public void setPbLoginId(String pbLoginId) {
        this.pbLoginId = pbLoginId;
    }

    public String getBillRefNo() {
        return billRefNo;
    }

    public void setBillRefNo(String billRefNo) {
        this.billRefNo = billRefNo;
    }

    public String getHashTotal() {
        return hashTotal;
    }

    public void setHashTotal(String hashTotal) {
        this.hashTotal = hashTotal;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getPaymentOptionId() {
        return paymentOptionId;
    }

    public void setPaymentOptionId(String paymentOptionId) {
        this.paymentOptionId = paymentOptionId;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMerOrderNo() {
        return merOrderNo;
    }

    public void setMerOrderNo(String merOrderNo) {
        this.merOrderNo = merOrderNo;
    }

    public String getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(String paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    private String contentDisposition = "";

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    private InputStream inputStream = null;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private String contentType = "";

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPasswd() {
        return loginPasswd;
    }

    public void setLoginPasswd(String loginPasswd) {
        this.loginPasswd = loginPasswd;
    }

    public String getdType() {
        return dType;
    }

    public void setdType(String dType) {
        this.dType = dType;
    }

    public boolean isBlPaymentSuccess() {
        return blPaymentSuccess;
    }

    public void setBlPaymentSuccess(boolean blPaymentSuccess) {
        this.blPaymentSuccess = blPaymentSuccess;
    }

    public String getdCoId() {
        return dCoId;
    }

    public void setdCoId(String dCoId) {
        this.dCoId = dCoId;
    }

    public String getUsUserid() {
        return usUserid;
    }

    public void setUsUserid(String usUserid) {
        this.usUserid = usUserid;
    }

    public String getUsid() {
        return usid;
    }

    public void setUsid(String usid) {
        this.usid = usid;
    }

    public Double getBal_amount_() {
        return bal_amount_;
    }

    public void setBal_amount_(Double bal_amount_) {
        this.bal_amount_ = bal_amount_;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    private String swiperStep;

    public String getSwiperStep() {
        return swiperStep;
    }

    public void setSwiperStep(String swiperStep) {
        this.swiperStep = swiperStep;
    }

    public String getCaseDiv() {
        return caseDiv;
    }

    public void setCaseDiv(String caseDiv) {
        this.caseDiv = caseDiv;
    }

    public String getCert_pwd() {
        return cert_pwd;
    }

    public void setCert_pwd(String cert_pwd) {
        this.cert_pwd = cert_pwd;
    }

    public File getCert_file() {
        return cert_file;
    }

    public void setCert_file(File cert_file) {
        this.cert_file = cert_file;
    }

    private String dsp_id;

    public String getDsp_id() {
        return dsp_id;
    }

    public void setDsp_id(String dsp_id) {
        this.dsp_id = dsp_id;
    }

    public Map<String, Object> getPrecheckMap() {
        return precheckMap;
    }

    public void setPrecheckMap(Map<String, Object> precheckMap) {
        this.precheckMap = precheckMap;
    }

    public String getSsoUserId() {
        return ssoUserId;
    }

    public void setSsoUserId(String ssoUserId) {
        this.ssoUserId = ssoUserId;
    }

    public String getpFimUserId() {
        return pFimUserId;
    }

    public void setpFimUserId(String pFimUserId) {
        this.pFimUserId = pFimUserId;
    }

    public String getpCoId() {
        return pCoId;
    }

    public void setpCoId(String pCoId) {
        this.pCoId = pCoId;
    }

    public String getSsoCorpId() {
        return ssoCorpId;
    }

    public void setSsoCorpId(String ssoCorpId) {
        this.ssoCorpId = ssoCorpId;
    }

    public String getpCoEmail() {
        return pCoEmail;
    }

    public void setpCoEmail(String pCoEmail) {
        this.pCoEmail = pCoEmail;
    }

    public void getElasisUserBySwkId() {
        BaseDAO transDAO = new BaseDAOImpl();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        int code = -1;
        String status = "error";
        String message = "No record found.";
        Map<String, Object> pbUserMap = new HashMap();
        boolean userFound = false;

        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-store");
            transDAO.setSession(baseDAO.getSession());

            String fimId = request.getParameter("strFimId");
            String fimUserId = request.getParameter("strFimUserId");
            String strIc = request.getParameter("strNewICNo");
            String userPassport = request.getParameter("strPassportNo");
            Map param = new HashMap();
            param.put("fim_id", fimId);
            Debug.printDebug("fimId" + fimId + "||" + "fimUserId " + fimUserId + " || " + " strIc " + strIc + " || " + " userPassport " + userPassport);

            List<PublicUserModel> userList = transDAO.list(param, PublicUserModel.class, false);
            Debug.printDebug("userList " + userList);
            if (userList != null && userList.size() > 0) {
                PublicUserModel foundUser = userList.get(0);
                userFound = true;

                String[] pbUserField2 = new String[]{"Us_id", "Us_user_id", "Us_password", "Us_id_type", "Us_id_number", "Us_user_name", "Us_nationality", "Us_preferred_contact", "Us_email", "Us_hp_number", "Us_profession", "Us_mailing_address1", "Us_mailing_address2", "Us_mailing_address3", "Us_mailing_postcode", "Us_mailing_city", "Us_mailing_state", "Us_status", "Us_admin", "Us_last_login_date", "Us_effective_date", "Us_cancel_date", "Created_date", "Created_by", "Updated_date", "Updated_by", "Us_user_type", "Us_verify_by", "Us_verify_date", "Us_fail_attempt_count", "Us_fail_login_date", "Us_division", "Us_upgrade", "Us_internal", "Us_upgrade_date", "Us_upgrade_by", "Us_hp_country_code", "Us_expiry_date", "Us_internal_section", "Us_email_date", "Us_email_by", "Us_tc_accept_version", "Pubkey", "Us_password_expiry", "Us_internal_storefront", "User_type_el", "Fim_id", "Fim_user_id", "Ldap_user_id"};
                pbUserMap = getDataMap(foundUser, pbUserField2);
                Debug.printDebug("pbUserMap " + pbUserMap);
            }

            if (!userFound) {
                if (!Validator.isEmpty(strIc) || !Validator.isEmpty(userPassport)) { //Remark: Foreigner dont have IC.
                    if (!Validator.isEmpty(strIc)) {
                        //Breakdown IC from Sarawak ID.
                        String icFirst = strIc.substring(0, 6);
                        String icSecond = strIc.substring(6, 8);
                        String icThird = strIc.substring(8, 12);
                        strIc = icFirst + "-" + icSecond + "-" + icThird;
                    } else if (!Validator.isEmpty(userPassport)) {
                        strIc = userPassport;
                    }

                    param.clear();
                    param.put("us_id_number", strIc);
                    List<PublicUserModel> userList2 = transDAO.list(param, PublicUserModel.class, false);
                    Debug.printDebug("userList2 " + userList2);
                    if (userList2 != null && userList2.size() > 0) {
                        PublicUserModel foundUser = userList2.get(0);
                        String[] pbUserField2 = new String[]{"Us_id", "Us_user_id", "Us_password", "Us_id_type", "Us_id_number", "Us_user_name", "Us_nationality", "Us_preferred_contact", "Us_email", "Us_hp_number", "Us_profession", "Us_mailing_address1", "Us_mailing_address2", "Us_mailing_address3", "Us_mailing_postcode", "Us_mailing_city", "Us_mailing_state", "Us_status", "Us_admin", "Us_last_login_date", "Us_effective_date", "Us_cancel_date", "Created_date", "Created_by", "Updated_date", "Updated_by", "Us_user_type", "Us_verify_by", "Us_verify_date", "Us_fail_attempt_count", "Us_fail_login_date", "Us_division", "Us_upgrade", "Us_internal", "Us_upgrade_date", "Us_upgrade_by", "Us_hp_country_code", "Us_expiry_date", "Us_internal_section", "Us_email_date", "Us_email_by", "Us_tc_accept_version", "Pubkey", "Us_password_expiry", "Us_internal_storefront", "User_type_el", "Fim_id", "Fim_user_id", "Ldap_user_id"};
                        pbUserMap = getDataMap(foundUser, pbUserField2);
                        Debug.printDebug("pbUserMap " + pbUserMap);
                    }
                }
            }

            code = 0;
            status = "Success";
            message = "Retrieve public user info successfully.";
            jsonMap.put("pbUserMap", pbUserMap);

        } catch (Exception e) {

        } finally {
            transDAO.closeSession();
        }

        jsonMap.put("response", code);
        jsonMap.put("status", status);
        jsonMap.put("message", message);

        try {
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } catch (Exception ex) {
            System.out.println("*** Failed to response server error. ***");
        }

    }

    public void checkValidSurveyor() {
        BaseDAO transDAO = new BaseDAOImpl();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        int code = -1;
        String status = "error";
        String message = "No record found.";
        Map<String, Object> pbUserMap = new HashMap();
        boolean validSurveyor = true;

        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-store");
            transDAO.setSession(baseDAO.getSession());

            String userId = request.getParameter("userId");
            String swkip_corp = request.getParameter("swkip_corp");
            Map param = new HashMap();
            param.put("fim_id", userId);
            Debug.printDebug("userId " + userId + " || " + " swkip_corp " + swkip_corp);

            String sqlQuery = "SELECT tsup.* FROM T_SETUP_USER_P tsup "
                    + "INNER JOIN T_USER_COMPANY tuc ON tsup.US_ID = tuc.us_id "
                    + "INNER JOIN T_CUST_COMPANY tcc ON tcc.CO_ID = tuc.CO_ID "
                    + "WHERE tsup.US_USER_ID = '" + userId + "' AND tcc.SWKID_CORP = '" + swkip_corp + "'";
            Query query = baseDAO.getSession().createSQLQuery(sqlQuery);
            System.out.println("sqlQuery " + sqlQuery);

            List validSurveyorList = query.list();
            System.out.println("validSurveyorList " + validSurveyorList.size());
            if (validSurveyorList.size() < 1) {
                validSurveyor = false;
            }

            code = 0;
            status = "Success";
            message = "Retrieve surveyor info successfully.";
            jsonMap.put("validSurveyor", validSurveyor);

        } catch (Exception e) {

        } finally {
            transDAO.closeSession();
        }

        jsonMap.put("response", code);
        jsonMap.put("status", status);
        jsonMap.put("message", message);

        try {
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } catch (Exception ex) {
            System.out.println("*** Failed to response server error. ***");
        }

    }

}
