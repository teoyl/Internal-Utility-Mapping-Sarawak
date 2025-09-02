/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend;

import com.sains.common.util.DateUtil;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.SystemConstants.SCS_JP3_PUBCODE;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import com.sains.framework.model.User;
import com.sains.workflow.web.WorkflowApiAction;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.FileModel;
import com.utimaps.model.JobDetailModel;
import com.utimaps.web.UtimapsAction;
import static java.lang.Thread.sleep;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

/**
 *
 * @author yonglai
 */
public class CompJobCheck extends TimerJob {
    private String useServiceFactory_ = "N";
    private String backend = "Backend";
    private CommonFunction cf = new CommonFunction();
    private Map<Integer, String> paramSQL = new HashMap();
    private Map paramMap = new HashMap();

    private long waitingTime = 60000; // **TNT for UAT/training test run every 1 min
//    private long waitingTime = 900000; // 15 min // in milisecond
    public Boolean keepChecking = true;
    long sleepTime = 0;
    private Boolean debugMode = Boolean.FALSE; // default is FALSE;
    private Integer debugOpenCount = 0;
    private Integer debugCloseCount = 0;
    
    public String getUseServiceFactory_() {
        return useServiceFactory_;
    }

    public void setUseServiceFactory_(String useServiceFactory_) {
        this.useServiceFactory_ = useServiceFactory_;
    }
    
    private BaseDAO retrieverDAO = new BaseDAOImpl();
    private BaseDAO baseDAO = new BaseDAOImpl();
    
    public CompJobCheck() {
        super();
        setJobSwitch_on(Boolean.FALSE);
        alwaysTriggerOnce = Boolean.FALSE;
        jobRepeat = TimerJob.REPEAT.Default;
        jobRunEvery = "1"; //HH:MM (Day of the month in 2 digits)
//        setInsertUpdateSuccessCode(SystemConstants.ACTION_Status.LOAD_EDIT_PAGE);

    }
    
//    public void runAll(){
    @Override
    public void run() {
        try {
            while (keepChecking) {
                if (debugMode) {
                    Debug.printDebug("DEBUG >>>>> Opened::" + debugOpenCount + "<<>>Closed::" + debugCloseCount);
                    cf.writeFile("CompJobCheck", "DEBUG >>>>> Opened::" + debugOpenCount + "<<>>Closed::" + debugCloseCount);
                }

                try {
                    Timestamp timeStart = DateUtil.getCurrentTimestamp();
                    cf.writeFile("CompJobCheck", "**********[start] USJ CompJobCheck*** " + timeStart);
                    ModelBase.updateCurrentRequestObject("antiCSRF", "ignoreCSRF");
                    
                    checkUtiJobProg();
                    
                    Timestamp timeEnd = DateUtil.getCurrentTimestamp();
                    if (waitingTime - (timeEnd.getTime() - timeStart.getTime()) > 0) {
                        sleepTime = waitingTime - (timeEnd.getTime() - timeStart.getTime());
                        try {
                            sleep(sleepTime);
                        } catch (InterruptedException e) {
                            CommonFunction.writeLogFile(e.getStackTrace(), "interruptError", "CompJobCheck", "run");
                            sleep(waitingTime);
                            break;
                        }
                    }
                } catch (Exception ex) {;
                    retrieverDAO.closeSession();
                    CommonFunction.writeLogFile(ex.getStackTrace(), "CompJobCheck", "CompJobCheck", "run");
                    sleep(waitingTime * 3);//do nothing.

                } finally {
                }
            }
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "CompJobCheck", "CompJobCheck", "run");
        } finally {
            try {
                retrieverDAO.closeSession();
            } catch (Exception ex) {
                CommonFunction.writeLogFile(ex.getStackTrace(), "CompJobCheck", "CompJobCheck", "run");
            }
        }
        
    }
    
    public String checkUtiJobProg() throws Exception {
        
        try {
            List<Map<String, Object>> lsJobProg = null;
            String jobProgSQL = "SELECT * FROM UTIJOBPROG WHERE STATUS = ? ";
            Query query = baseDAO.getSession().createSQLQuery(jobProgSQL);
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            query.setString(1, "N");
            lsJobProg = query.list();
            cf.writeFile("CompJobCheck", "lsJobProg.size()***************************************" + lsJobProg.size());

            if (lsJobProg.size()>0){
                WorkflowApiAction wfApi = new WorkflowApiAction();
                String wf_rtn = "";
                cf.writeFile("CompJobCheck", "lsJobProg == " + lsJobProg);

                for (Map jobProg : lsJobProg) {
                    cf.writeFile("CompJobCheck", "jobProg == " + jobProg);
                    cf.writeFile("CompJobCheck", "jobProg " + jobProg);
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
                    lasisUserList = baseDAO.getListFromSql(strSQL,null);

                    List lasisUserList2 = new ArrayList();
                    lasisUserList2 = baseDAO.getListFromSql(strSQL2,null);

                    cf.writeFile("CompJobCheck", "lasisUserList " + lasisUserList);
                    cf.writeFile("CompJobCheck", "lasisUserList2 " + lasisUserList2);
                    if(lasisUserList.size() > 0){
                        for (Map lasisUser : (List<Map>) lasisUserList) {
                            if(lasisUser.get("US_ID")!= null){
                                userIdFrom =  lasisUser.get("US_ID").toString();
                            }
                        }

                        userFrom = (User) baseDAO.getModelById(userIdFrom, User.class);

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
                        userTo = (User) baseDAO.getModelById(userIdTo, User.class);
                        if(userTo != null) {
                            usUserIdTo = userTo.getUs_user_id();
                        }
                    }

                    List<JobDetailModel> jobDetailModelList = new ArrayList();
                    Map param = new HashMap();
                    param.put("USJ_DIV||USJ_YEAR||USJ_SEQ", divId + jobNumber);
                    jobDetailModelList = baseDAO.list_order(param, JobDetailModel.class, true, "");
                    JobDetailModel jobModel = new JobDetailModel();
                    if (jobDetailModelList != null && jobDetailModelList.size() > 0) {
                        jobModel = jobDetailModelList.get(0);
                    }

                    cf.writeFile("CompJobCheck", "usUserIdFrom: " + usUserIdFrom + " | usUserIdTo: " + usUserIdTo);
                    cf.writeFile("CompJobCheck", "jobModel for " + divId + jobNumber + " is " + jobModel);

                    if(jobModel != null) {
                        switch (jobAction) {
                            case SCS_JP3_PUBCODE.ASSIGN_PLAN_EXAMINATION:
                                wf_rtn = wfApi.startUSJ003_04(jobModel.getJob_id(), usUserIdFrom, "");
                                break;
                            case SCS_JP3_PUBCODE.JOB_REGISTERED:
                                wf_rtn = wfApi.startUSJ003_05(jobModel.getJob_id(), usUserIdFrom, "");
                                break;
                            case SCS_JP3_PUBCODE.COMPUTATION_APPROVED:
                                wf_rtn = wfApi.startUSJ003_06(jobModel.getJob_id(), usUserIdFrom, "");
                                break;
                            default:
                                wf_rtn = "failed";
                                break;
                        }
                        
                        cf.writeFile("CompJobCheck", "wf_rtn after start workflow " + wf_rtn);
                        if (wf_rtn.equals("success")) {
                            Transaction transaction = baseDAO.getSession().beginTransaction();

                            try {
                                String sql2 = "UPDATE UTIJOBPROG" + divId + " SET STATUS = 'Y' WHERE JOB_NUMBER = ? AND DIV_ID = ? AND JOB_ACTION = ?";
                                Query query2 = baseDAO.getSession().createSQLQuery(sql2);
                                query2.setString(1, jobNumber);
                                query2.setString(2, divId);
                                query2.setString(3, jobAction);
                                query2.executeUpdate();
                                transaction.commit();

                            } catch (Exception e) {
                                transaction.rollback();
                                e.printStackTrace();
                                CommonFunction.writeLogFile(e.getStackTrace(), "CompJobCheck", "CompJobCheck", "updateUTIJOBPROG"); // Re-throw the exception for handling
                                new UtimapsAction().auditAction2(jobModel.getJob_id(), baseDAO.getSession(), usUserIdFrom, "UTIJOBPROG : Job Number " + jobNumber + " For Action Code : (" + jobAction + ") failed with exception.");
                            } finally {
                                cf.writeFile("CompJobCheck", "jobProg for jobNumber " + jobNumber + " divId " + divId + " jobAction " + jobAction + " triggered ");
                                new UtimapsAction().auditAction2(jobModel.getJob_id(), baseDAO.getSession(), usUserIdFrom, "UTIJOBPROG : Job Number " + jobNumber + " For Action Code : (" + jobAction + ") success.");
                            }

                        } else {
                            new UtimapsAction().auditAction2(jobModel.getJob_id(), baseDAO.getSession(), usUserIdFrom, "UTIJOBPROG : Job Number " + jobNumber + " For Action Code : (" + jobAction + ") failed. Job not found.");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "CompJobCheck", "CompJobCheck", "checkUtiJobProg");
        } finally {
            
        }
        
        return "";
    }
}
