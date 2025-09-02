/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web.dao;

import com.digicert.PDFSign;
import com.lxg.common.model.PublicUserModel;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.common.util.FileOperationUtil;
import com.sains.common.util.FtpInterface;
import com.sains.common.util.OBSUtil;
import com.sains.common.util.SFTPBean;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.EmailTrigger;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.model.AutoEmailParam;
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.model.User;
import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import com.sains.workflow.web.WorkflowApiAction;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.FileModel;
import com.utimaps.model.JobDetailModel;
import com.utimaps.model.JobStatusModel;
import com.utimaps.model.NotificationPModel;
import com.utimaps.model.ProcessingHistoryModel;
import com.utimaps.model.SurveyFirmPModel;
import com.utimaps.web.UtimapsAction;
import com.utimaps.web.UtimapsAction.USER_GROUP;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author yonglai
 */
public class IssuanceJobDAOImpl extends BaseDAOImpl<JobDetailModel> implements IssuanceJobDAO {
    @Override
    public synchronized JobDetailModel updateIssuance(JobDetailModel model) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        try {
            Debug.printDebug("runnning update daoimpl~~~~~");
            dao.setSession(getSession());
            beginBatchTransaction();
            Map sessionMap = ActionContext.getContext().getSession();
            
            if(model.get_operation().equals(ApplicationPModel.OPERATION.PROCESS_UPDATE_ISSUANCE)) {
                String ori_sj_no = model.getUsj_no();
                model.updatableColumns = new String[]{"job_id", "usj_seq", "usj_year", "usj_request", "survey_date_start","survey_date_end","usj_no","usj_classification","requestor_branch"};
                String formatted_seq = prefixZeros(model.getUsj_seq(),4,false,"");
                
                model.setUsj_seq(formatted_seq);
                model.setUsj_no("USJ/"+model.getApplication_div_display()+"/"+model.getUsj_seq()+"/"+model.getUsj_year());
                model = (JobDetailModel) dao.setUpdateProperties(model, getSession());
                dao.getSession().update(model);
                
                String new_sj_no = model.getUsj_no();
                
                // Check if Survey Job No. updated or not
                if(ori_sj_no.equals(new_sj_no)) {
                    
                    String sql = "SELECT JOB_NUMBER FROM SCS_JOB" + model.getUsj_div() + " WHERE JOB_NUMBER = ? ";
                    Query query = dao.getSession().createSQLQuery(sql);
                    query.setString(1, model.getUsj_year() + model.getUsj_seq());
                    List list = query.list();
                    
                    if(list.size() > 0) {
                        //If Survey Job No. no updated, proceed to update
                        String sql2 = "UPDATE SCS_JOB" + model.getUsj_div() + " SET DIVISION_NO = ?, CLASSIFICATION = ?, DATE_SURVEY_START = ?, DATE_SURVEY_FINISH = ?, "
                                + "DIV_ID = ?, REQUESTER = ? WHERE JOB_NUMBER = ?";
                        Query query2 = dao.getSession().createSQLQuery(sql2);
                        query2.setString(1, model.getUsj_div());
                        query2.setString(2, model.getUsj_classification());
//                        query2.setTimestamp(3, model.getSurvey_date_start());
//                        query2.setTimestamp(4, model.getSurvey_date_end());
                        // To remove survey duration as requsted in external preview, but date cannot be null, therefore set to current datetime
                        query2.setTimestamp(3, DateUtil.getCurrentTimestamp());
                        query2.setTimestamp(4, DateUtil.getCurrentTimestamp());
                        query2.setString(5, model.getUsj_div());
                        query2.setString(6, model.getRequestor_branch());
                        query2.setString(7, model.getUsj_year() + model.getUsj_seq());
                        query2.executeUpdate();
                        dao.commitBatchTransaction();

                        new UtimapsAction().auditAction(model.getJob_id(),dao.getSession(),sessionMap.get("userId").toString(),"Update SCS_JOB" + model.getUsj_div() + " : " + model.getUsj_year() + model.getUsj_seq());
                    }
                    
                } else {
                    //If Survey Job No. got updated, proceed to insert 
                    String surveyOrg = "NA";
                    
                    if(!Validator.isEmpty(model.getApplicationModel().getSurveyFirmModel().getFirm_soc())) {
                        surveyOrg = model.getApplicationModel().getSurveyFirmModel().getFirm_soc();
                    }
                    
                    // 05-03-2025 :: To bypass this case as requested by LNS 
                    String sjNoToExcluded = "USJ/1D/0042/2025";
                    if(new_sj_no.equals(sjNoToExcluded)) {
                        // Do nothing
                    } else {
                        String sql = "INSERT INTO SCS_JOB" + model.getUsj_div() + " (JOB_NUMBER, JOB_OPER, DIVISION_NO, MEASURE_UNIT, SURVEY_ORG, SURVEY_METHOD, PROJECTION, CLASSIFICATION, JOB_DESC, SURVEYOR, DATE_SURVEY_START, DATE_SURVEY_FINISH, APPROV_DATE, INST_THEO_PC, INST_EDM, INST_CHAIN, INST_METRE_BAND, SURVEY_PLAN_TYPE, SURVEY_PLAN_NUM, TOL_DIST, TOL_COORD, TOL_THEOD, TOL_PC, SURVEY_JOB_STATUS, FILE_REF, JOB_TYPE, JOB_STATUS, DIV_ID, TIMESTAMP, REQUESTER) VALUES "
                                + "(?, ' ', ?, ' ', ?, ' ', ' ', ?, ?, ' ', ?, ?, sysdate, ' ', ' ', ' ', ' ', ' ', ' ', 0, 0, 0, 0, ' ', ?, ' ', ' ', ?, sysdate, ?)";
                        Query query = dao.getSession().createSQLQuery(sql);
                        query.setString(1, model.getUsj_year() + model.getUsj_seq());
                        query.setString(2, model.getUsj_div());
                        query.setString(3, surveyOrg);
                        query.setString(4, model.getUsj_classification());
                        query.setString(5, model.getApplicationModel().getPj_name());
    //                    query.setTimestamp(6, model.getSurvey_date_start());
    //                    query.setTimestamp(7, model.getSurvey_date_end());
                        query.setTimestamp(6, DateUtil.getCurrentTimestamp());
                        query.setTimestamp(7, DateUtil.getCurrentTimestamp());
                        query.setString(8, model.getCase_ref());
                        query.setString(9, model.getUsj_div());
                        query.setString(10, model.getRequestor_branch());
                        query.executeUpdate();
                        dao.commitBatchTransaction();

                        new UtimapsAction().auditAction(model.getJob_id(),dao.getSession(),sessionMap.get("userId").toString(),"Insert SCS_JOB" + model.getUsj_div() + " : " + model.getUsj_year() + model.getUsj_seq());
                    }
                        
                }
            }
            
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobDAOImpl", "updateIssuance");
        } finally {
            dao.closeAllSession();
            closeSession();
        }
        return model;
    }
    
    @Override
    public String completeIssuance(JobDetailModel model) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        String completingResult = "success";
        Map sessionMap = ActionContext.getContext().getSession();
        
        try {
            Debug.printDebug("runnning completeIssuance task daoimpl~~~~~");
            List<String> fileArrayList = new ArrayList(); // 17.09.2024 :: List to store all files to be sent with email
            dao.setSession(getSession());
            beginBatchTransaction();
            if(model.get_operation().equals(ApplicationPModel.OPERATION.PROCESS_COMPLETE_ISSUANCE)) {
                if(model.getWf_status().equals(UtimapsAction.WF_STATUS.PREPARE_SJI_PENDING)) {
                    
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Map jsonMap = wfApi.completeTask_ISS(dao.getSession(),model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", UtimapsAction.WF_STATUS.PREPARE_SJI_PENDING,UtimapsAction.WF_STATUS.PREPARE_SJI_COMPLETED, UtimapsAction.WF_STATUS.ISSUE_SJI_PENDING,"",USER_GROUP.SS);
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                } else if (model.getWf_status().equals(UtimapsAction.WF_STATUS.ISSUE_SJI_PENDING)) {
                    
                    model.updatableColumns = new String[]{"job_id"};
                    
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Map jsonMap = wfApi.completeTask_ISS(getSession(),model.get_taskId(), model.getJob_id(), model.getCase_id(), "Y", UtimapsAction.WF_STATUS.ISSUE_SJI_PENDING,UtimapsAction.WF_STATUS.ISSUE_SJI_COMPLETED ,UtimapsAction.WF_STATUS.ISSUE_SJI_COMPLETED,"","");
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;

                    if(result.equalsIgnoreCase("success")) {
                        //to send email to surveyor after success
                        Map mailJsonMap = new HashMap();
                        AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
                        autoEmailDAO.setSession(dao.getSession());
                        AutoEmail autoEmail = null;
                        AutoEmail autoEmail2 = null;
                        FtpInterface ftp = FileOperationUtil.getFtpInterface();
                        
                        try {
                            Debug.printDebug("send submission email to public user");

                            String publicUserName = "";
                            String publicUserId = "";
                            String publicUserEmail = "default@utimaps.com.my";
                            String surveyFirmUserName = "";
                            String surveyFirmEmail = "";

                            ApplicationPModel appModel =  (ApplicationPModel) dao.getModelById(model.getCase_id(), ApplicationPModel.class);
                            PublicUserModel publicUser = (PublicUserModel) dao.getModelByCode("us_user_id", appModel.getApp_submit_by(), new PublicUserModel());

                            if(publicUser != null) {
                                publicUserName = publicUser.getUs_user_name();
                                publicUserId = publicUser.getUs_user_id();
                                publicUserEmail = publicUser.getUs_email();
                            }

                            SurveyFirmPModel surveyFirm = (SurveyFirmPModel) dao.getModelByCode("case_id", appModel.getCase_id(), new SurveyFirmPModel());
                            if(surveyFirm != null) {
                                surveyFirmUserName = surveyFirm.getFirm_oic();
                                surveyFirmEmail = surveyFirm.getFirm_email();

                                publicUserName = Validator.isEmpty(surveyFirmUserName) ? publicUserName : publicUserName + ", " + surveyFirmUserName;
                                publicUserEmail = Validator.isEmpty(surveyFirmEmail) ? publicUserEmail : publicUserEmail + "," + surveyFirmEmail;
                            }
                            
                            Map mailParam = new HashMap();
                            autoEmail = autoEmailDAO.getAutoEmailByCode("USJNotifyJobNo", new AutoEmail());
                            
                            Debug.printDebug("autoEmail " + autoEmail);
                            mailParam.put(EmailTrigger.MAIL_TO, publicUserEmail); // For Public User
                            mailParam.put(EmailTrigger.MAIL_CCTO, SystemConstants.email_landsurveyboard);
                            mailParam.put("userName", publicUserName); // For Public User
                            mailParam.put("strUsjNo", model.getUsj_no());
                            mailParam.put("strPlanNo", "USP-" + model.getUsj_div() + "-" + model.getPlan_no());
                            
                            FileModel usji = (FileModel) dao.getModelByCode("case_id,file_type",model.getJob_id()+",SISJL", new FileModel());
                            Debug.printDebug("usji " + usji);
                            if(usji != null){
                                String filePath = (usji != null) ? usji.getFile_path() : ""; ;
                                List attList = new ArrayList<Map>();

                                Map aa = new HashMap();
                                String fileName = usji.getFile_name();
                                aa.put(EmailTrigger.ATTACH.AttInputStream, ftp.getFile(filePath));
                                aa.put(EmailTrigger.ATTACH.AttFileName, fileName);
                                attList.add(aa);
                                fileArrayList.add(usji.getFile_id());
                                Debug.printDebug("filePath " + filePath);
                                Debug.printDebug("fileName " + fileName);

                                //add attachment
                                mailParam.put(EmailTrigger.ATTACH.AttList, attList);
                            }
                            
                            new EmailTrigger().sendEMail(mailParam, autoEmail);
                            
                            String not_id = "";
                            String not_sender = "";
                            String not_subject = "";
                            String not_content = "";
                            if(autoEmail.getNotificationSetup() != null) {
                                not_sender = sessionMap.get("userId").toString();
                                not_id = autoEmail.getNotificationSetup().getNo_id();
                                not_subject = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_subject(), mailParam);
                                not_content = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_body(), mailParam);
                            }
                            
                            String fileList = "";
                            if(fileArrayList.size() > 0) {
                                fileList = fileArrayList.toString();
                            }
                            Debug.printDebug("fileList before insert notification: " + fileList);

                            //Insert Notification
                            NotificationPModel insertNot = new UtimapsAction().insertNotification(dao.getSession(), model.getJob_id(), not_id, not_sender, publicUserId, not_subject, not_content, "U001", fileList, model.get_taskId(), "S");
                            
                            if(insertNot == null) {
                                Debug.printDebug("Error when insert notification, no notification is inserted." + model.getJob_id() + " - " + not_id + " - " + not_sender + " - " + publicUserId + " - " + not_subject + " - " + not_content + " - " + "U001" + " - " + usji.getFile_id() + " - " + model.get_taskId() + " - " + "S");
                            } else {
                                new UtimapsAction().auditAction(model.getJob_id(),dao.getSession(),not_sender,"Send Notification : " + model.getJob_id());
                            }
                            
                            Debug.printDebug("### Public email sent ");

                            mailJsonMap.put("status", "success");
                            mailJsonMap.put("errMsg", "");
                        } catch (Exception e) {
                            mailJsonMap.put("errMsg", "Fail to update case status");
                            mailJsonMap.put("status", "fail");
                            Debug.printDebug("Error when send email " + e);
                            e.printStackTrace();
                            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobDAOImpl", "completeIssuance");
                        }
                    }
                }
            } else if(model.get_operation().equals(ApplicationPModel.OPERATION.PROCESS_ROUTE_BACK_ISSUANCE)) {
                Debug.printDebug("model.getWf_status() " + model.getWf_status());
                if (model.getWf_status().equals(UtimapsAction.WF_STATUS.ISSUE_SJI_PENDING)) {
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Map jsonMap = wfApi.completeTask_ISS(getSession(),model.get_taskId(), model.getJob_id(), model.getCase_id(), "B", UtimapsAction.WF_STATUS.ISSUE_SJI_PENDING, UtimapsAction.WF_STATUS.ISSUE_SJI_ROUTE_BACK ,UtimapsAction.WF_STATUS.PREPARE_SJI_PENDING,"",USER_GROUP.AS);
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                }
            }
            
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            completingResult = "failed";
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobDAOImpl", "completeIssuance");
        } finally {
            dao.closeSession();
            closeSession();
        }
        
        return completingResult;
    }
    
    @Override
    public String deleteUSJFile(JobDetailModel model) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        String completingResult = "success";
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        
        try {
            Debug.printDebug("runnning deleteUSJFile task daoimpl~~~~~ " + model.get_operation());
            dao.setSession(getSession());
            beginBatchTransaction();
            
            if(model.get_operation().equals(ApplicationPModel.OPERATION.DELETE_USJ_FILE)) {
                List<FileModel> existingFileList = new ArrayList();
                Map param = new HashMap();
                param.put("case_id", model.getJob_id());
                param.put("file_type", SystemConstants.FILE_TYPE.USJL);
                existingFileList = dao.list_order(param, FileModel.class, "");

                if(existingFileList.size() > 0) {
                    for(FileModel existingFile : existingFileList) {
                        if(existingFile != null) {
//                            Debug.printDebug("Filepath " + existingFile.getFile_path() + "/" + existingFile.getFile_id());
//                            Debug.printDebug("isFileexist? " + ftp.isFileExists(SysConf.get("pathPrefix") + existingFile.getFile_path() + "/" + existingFile.getFile_id(), Boolean.FALSE));
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
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobDAOImpl", "deleteUSJFile");
        } finally {
            dao.closeSession();
            closeSession();
        }

        return completingResult;
    }

    @Override
    public Integer signUSJLetter(JobDetailModel model) throws Exception {
        
        Debug.printDebug("getCert_File " + model.getCert_File());
        Debug.printDebug("getCert_Password " + model.getCert_Password());

        BaseDAOImpl dao = new BaseDAOImpl();
        Integer intError = 1;
        String filePath = "";
        String fileName;
        String fileId;
        PDFSign pdfsign = new PDFSign();
        Map sessionMap = ActionContext.getContext().getSession();
        
        try {
            dao.setSession(getSession());
            FileModel usjLetter = new FileModel();
            usjLetter = (FileModel) dao.getModelByCode("file_type,case_id",SystemConstants.FILE_TYPE.USJL+","+model.getJob_id(),new FileModel());
            fileId = usjLetter.getFile_id();
            filePath = usjLetter.getFile_path();
            fileName = usjLetter.getFile_name();
            intError = pdfsign.selfSignMode(fileId, filePath, fileName, model.getCert_File() , model.getCert_Password(), model.getJob_id(), model.getUsj_div(), model.getWf_status());
            
            if(intError < 0) {
                
            } else {
                String ssUserId = sessionMap.get("userId").toString();
                String asUserId = "";
                String asLasisId = "";
                String ssLasisId = "";
                
                Map paramMap = new HashMap();
                paramMap.put("case_id", model.getJob_id());
                paramMap.put("process_code", UtimapsAction.WF_STATUS.PREPARE_SJI_COMPLETED);
                List<ProcessingHistoryModel> processList = new ArrayList();
                processList = dao.list_order(paramMap, ProcessingHistoryModel.class, true, "order by process_date asc" );
                
                if(processList.size() > 0) {
                    String asUsUserId = "";
                    for(ProcessingHistoryModel process: processList){
                        asUsUserId = process.getProcess_by();
                    }
                    
                    User user = (User) dao.getModelByCode("us_user_id", asUsUserId, new User());
                    if(user != null) {
                        asUserId = user.getUs_id();
                    }
                }
                
                String strSQL = "SELECT tsou.ORACLE_ID FROM T_SETUP_ORACLE_USER tsou " +
                                "LEFT JOIN T_SETUP_USER_ORACLE tsuo ON tsuo.ORACLE_USER_ID = tsou.ORACLE_USER_ID " +
                                "WHERE tsuo.US_ID = '" + asUserId +"'";
                
                String strSQL2 = "SELECT tsou.ORACLE_ID FROM T_SETUP_ORACLE_USER tsou " +
                                "LEFT JOIN T_SETUP_USER_ORACLE tsuo ON tsuo.ORACLE_USER_ID = tsou.ORACLE_USER_ID " +
                                "WHERE tsuo.US_ID = '" + ssUserId +"'";
                
                List lasisUserList = new ArrayList();
                lasisUserList = dao.getListFromSql(strSQL,null);
                
                List lasisUserList2 = new ArrayList();
                lasisUserList2 = dao.getListFromSql(strSQL2,null);
                
                if(lasisUserList.size() > 0){
                    for (Map lasisUser : (List<Map>) lasisUserList) {
                        if(lasisUser.get("ORACLE_ID")!= null){
                            asLasisId =  lasisUser.get("ORACLE_ID").toString();
                        }
                    }
                }
                
                if(lasisUserList2.size() > 0){
                    for (Map lasisUser : (List<Map>) lasisUserList2) {
                        if(lasisUser.get("ORACLE_ID")!= null){
                            ssLasisId =  lasisUser.get("ORACLE_ID").toString();
                        }
                    }
                }
                
                // Allocate Plan No upon signing
                if(Validator.isEmpty(model.getPlan_no())) {
                    beginBatchTransaction();
                    Calendar cal = Calendar.getInstance();
                    String strYear = Integer.toString(cal.get(Calendar.YEAR));
                    CommonFunction cf = new CommonFunction();
                    String planNo = cf.getRunningSeq("USJPlanNo",strYear,"","");

                    model.updatableColumns = new String[]{"job_id","plan_no"};
                    model.setPlan_no(planNo);
                    model = (JobDetailModel) dao.setUpdateProperties(model, dao.getSession());
                    dao.getSession().update(model);
                    commitBatchTransaction();
                }
                
                insertSCJOBPROG(dao.getSession(), model, asLasisId, ssLasisId, ssUserId);
                updateSCSJOB(dao.getSession(), model, ssUserId);
            }
            
            Debug.printDebug("usjLetter " + usjLetter);
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobDAOImpl", "signUSJLetter");
        } finally {
            dao.closeSession();
            closeSession();
        }
        
        return intError;
    }
    
    public void updateSCSJOB(Session pSession, JobDetailModel model, String userId) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(pSession);
        Transaction transaction = dao.getSession().beginTransaction();
        
        try {
            String sql = "SELECT JOB_NUMBER FROM SCS_JOB" + model.getUsj_div() + " WHERE JOB_NUMBER = ? ";
            Query query = dao.getSession().createSQLQuery(sql);
            query.setString(1, model.getUsj_year() + model.getUsj_seq());
            List list = query.list();

            if(list.size() > 0) {
                //If Survey Job No. no updated, proceed to update
                String sql2 = "UPDATE SCS_JOB" + model.getUsj_div() + " SET DATE_SIGNED = sysdate WHERE JOB_NUMBER = ?";
                Query query2 = dao.getSession().createSQLQuery(sql2);
                query2.setString(1, model.getUsj_year() + model.getUsj_seq());
                query2.executeUpdate();
                transaction.commit();

                new UtimapsAction().auditAction(model.getJob_id(),dao.getSession(),userId,"Update SCS_JOB" + model.getUsj_div() + " : " + model.getUsj_year() + model.getUsj_seq());
            }
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobDAOImpl", "updateSCSJOB"); // Re-throw the exception for handling
        } finally {
            transaction = null;
            dao.closeAllSession();
        }
    }

    public void insertSCJOBPROG(Session pSession, JobDetailModel model, String asLasisId, String ssLasisId, String userId) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(pSession);
        Transaction transaction = dao.getSession().beginTransaction();
        try {
            String sql = "SELECT JOB_NUMBER FROM SCJOBPROG" + model.getUsj_div() + " WHERE JOB_NUMBER = ? ";
            Query query = dao.getSession().createSQLQuery(sql);
            query.setString(1, model.getUsj_year() + model.getUsj_seq());
            List list = query.list();
            
            Debug.printDebug("asLasisId: " + asLasisId + " || ssLasisId: " + ssLasisId);
            
            if(list.isEmpty()) {
                String sql2 = "INSERT INTO SCJOBPROG" + model.getUsj_div() + " (JOB_NUMBER, ASSIGN_FROM, ASSIGN_TO, DATE_ASSIGN, JOB_ACTION, BATCH_NO, REMARKS, STATUS, DIV_ID, TIMESTAMP) VALUES "
                        + "(?, ?, ?, sysdate, ?, ?, ?, ?, ?, sysdate)";
                Query query2 = dao.getSession().createSQLQuery(sql2);
                query2.setString(1, model.getUsj_year() + model.getUsj_seq());
                query2.setString(2, "SYSTEM"); //22-10-2024 :: change to use "SYSTEM" as requested by user
                query2.setString(3, ssLasisId);
                query2.setString(4, "170");
                query2.setString(5, "  ");
                query2.setString(6, "Job Created By SS");
                query2.setString(7, "1");
                query2.setString(8, model.getUsj_div());
                query2.executeUpdate();
                
                new UtimapsAction().auditAction(model.getJob_id(),dao.getSession(),userId,"Insert SCJOBPROG " + model.getUsj_div() + " : " + model.getUsj_year() + model.getUsj_seq());
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobDAOImpl", "insertSCJOBPROG"); // Re-throw the exception for handling
        } finally {
            transaction = null;
            dao.closeAllSession();
        }
    }
    
    @Override
    public Integer insertSignedUSJLetter(JobDetailModel model, String fileId, String filePath, String fileName, String contentType, String fileType) throws Exception {
        
        BaseDAOImpl dao = new BaseDAOImpl();
        OBSUtil ftp = new OBSUtil();
        FileModel fileModel = new FileModel();
        
        try {
            dao.setSession(getSession());
            beginBatchTransaction();
            Debug.printDebug("insertSignedUSJLetter daoImpl");
                    
            if(model.get_operation().equals(ApplicationPModel.OPERATION.INSERT_SIGNED_USJ)) {
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
                Debug.printDebug("fileModel created " + fileModel);
                
                dao.getSession().save(fileModel);
            }

            commitBatchTransaction();

        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobDAOImpl", "insertSignedUSJLetter");
        } finally {
            dao.closeSession();
            closeSession();
            ftp.insertToFileDirectory(filePath+"/"+fileModel.getFile_id(), fileName, "application/pdf", fileModel.getFile_id(), dao, SystemConstants.FILE_TYPE.SISJL, Boolean.FALSE);
        }
        
        return 0;
    }

    @Override
    public String revokeUSJLetter(JobDetailModel model) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        OBSUtil ftp = new OBSUtil();
        FileModel fileModel = new FileModel();
        FileModel fileModel2 = new FileModel();
        
        try {
            dao.setSession(getSession());
            beginBatchTransaction();
            Debug.printDebug("revokeUSJLetter daoImpl");
                    
            if(model.get_operation().equals(ApplicationPModel.OPERATION.REVOKE_USJ_LETTER)) {
                fileModel = (FileModel) dao.getModelByCode("case_id,file_type",model.getJob_id()+","+SystemConstants.FILE_TYPE.SISJL , new FileModel());
                if(ftp.isFileExists(fileModel.getFile_path(), Boolean.FALSE)){
                    ftp.deleteFile(fileModel.getFile_path());
                }
                
                fileModel2 = (FileModel) dao.getModelByCode("case_id,file_type",model.getJob_id()+","+SystemConstants.FILE_TYPE.STSJL , new FileModel());
                if(ftp.isFileExists(fileModel2.getFile_path(), Boolean.FALSE)){
                    ftp.deleteFile(fileModel2.getFile_path());
                }
                
                dao.getSession().delete(fileModel);
                dao.getSession().delete(fileModel2);
            }   
   
            commitBatchTransaction();

        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobDAOImpl", "revokeUSJLetter");
        } finally {
            dao.closeSession();
            closeSession();
        }
        
        return "";
    }
    
    public String prefixZeros(String value, int len, boolean prefixAlphabet, String prefixAl) {
        char[] t = new char[len];
        int l = value.length();
        int k = len - l;
        for (int i = 0; i < k; i++) {
            t[i] = '0';
        }
        value.getChars(0, l, t, k);
        
        if(prefixAlphabet){
            return prefixAl + new String(t);
        }else{
            return new String(t);
        }        
    }
}
