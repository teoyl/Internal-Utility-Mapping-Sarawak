package com.backend;

import com.backend.TimerJob;
import com.sains.common.util.DateUtil;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.sains.workflow.web.WorkflowApiAction;
import com.utimaps.model.ApplicationPModel;
import com.webservice.PrecheckServiceAction;
import com.utimaps.model.PrecheckLog;
import com.utimaps.model.JobDetailModel;
import com.utimaps.model.PrecheckHistoryModel;
import com.sains.common.util.SystemConstants;
import com.utimaps.web.UtimapsAction;
import com.utimaps.web.UtimapsAction.PB_STATUS;
import com.utimaps.web.UtimapsAction.SURVEY_JOB_TYPE;
import com.utimaps.web.UtimapsAction.WF_STATUS;
import com.utimaps.web.UtimapsAction;
import static java.lang.Thread.sleep;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrecheckAction extends TimerJob {

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
//    private BaseDAO baseDAO = new BaseDAOImpl();

    public PrecheckAction() {
        super();
        setJobSwitch_on(Boolean.FALSE);
        alwaysTriggerOnce = Boolean.FALSE;
        jobRepeat = TimerJob.REPEAT.Default;
        jobRunEvery = "5"; //HH:MM (Day of the month in 2 digits)
//        setInsertUpdateSuccessCode(SystemConstants.ACTION_Status.LOAD_EDIT_PAGE);

    }

//    public void runAll(){
    @Override
    public void run() {
        PrecheckServiceAction psAction = new PrecheckServiceAction();

        try {
            while (keepChecking) {
//                System.out.println("USJBackendAction check");
                if (debugMode) {
//                    System.out.println("DEBUG >>>>> Opened::" + debugOpenCount + "<<>>Closed::" + debugCloseCount);
                }

                try {
                    Timestamp timeStart = DateUtil.getCurrentTimestamp();
//                    System.out.println("\n ===================== start Precheck Backend Action =====================  " + timeStart + "\n");
                    cf.writeFile("USJPrecheckLog", "============================= [start] PrecheckAction ============================= ");
                    ModelBase.updateCurrentRequestObject("antiCSRF", "ignoreCSRF");
                    String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    paramMap.clear();
                    paramMap.put("precheck_stage", UtimapsAction.PRECHECK_STAGE.TRAV_STARTED); //checking for waiting prechecks
                    List<JobDetailModel> runningTravPrecheckJobList = retrieverDAO.list_order(paramMap, JobDetailModel.class, true, " order by created_date");

                    List resultList;
                    Map paramSQL = new HashMap();
                    String strSQL = "SELECT * FROM US_JOBDETAIL "
                            + "WHERE PRECHECK_STAGE IN ('" + UtimapsAction.PRECHECK_STAGE.TRAV_STARTED + "','" + UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_STARTED + "','" + UtimapsAction.PRECHECK_STAGE.UTIL_STARTED + "','" + UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_STARTED + "') ";
                    paramSQL.clear();
                    resultList = cf.getListFromSqlWithSession(retrieverDAO.getSession(), strSQL, paramSQL);

                    if (!resultList.isEmpty()) {
//                        System.out.println("Got Running Prechecks Found");
                        for (Object item : resultList) {
                            Map<String, String> element = (Map) item;
//                            System.out.println("running id - " + element.get("JOB_ID"));
                            JobDetailModel runningJob = (JobDetailModel) retrieverDAO.getModelByCode("job_id", element.get("JOB_ID"), new JobDetailModel());
//                            JobDetailModel runningJob = new JobDetailModel();

                            if (runningJob.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.TRAV_STARTED) || runningJob.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_STARTED)) {

                                List travHistoryList = new ArrayList();
                                Map travHistoryMap = new HashMap();
                                Map pcHistMap = new HashMap();
                                pcHistMap.put("job_id", runningJob.getJob_id());
                                pcHistMap.put("precheck_type", "T");
                                List<PrecheckHistoryModel> travHistList = new ArrayList();
                                travHistList = retrieverDAO.list_order(pcHistMap, PrecheckHistoryModel.class, "order by created_date");

                                String[] precheckHistoryFields = new String[]{"task_id", "job_id", "task_seq", "task_desc", "created_date", "created_by"};
                                for (PrecheckHistoryModel hist : travHistList) {
                                    PrecheckHistoryModel history = (PrecheckHistoryModel) hist;
                                    travHistoryMap = psAction.getDataMap(history, precheckHistoryFields);
                                    travHistoryList.add(travHistoryMap);
                                }

                                if (!travHistList.isEmpty()) {
                                    PrecheckHistoryModel lastEntry = (PrecheckHistoryModel) travHistList.get(travHistoryList.size() - 1);
                                    cf.writeFile("USJPrecheckLog", "last trav hist - " + lastEntry.getTask_seq() + "---" + lastEntry.getTask_desc());
                                    String travSignal = lastEntry.getTraverse_signal();

                                    if (!travSignal.equals("I")) {
                                        runningJob.updatableColumns = new String[]{"job_id", "precheck_stage"};

                                        if (travSignal.equals("F")) {
                                            if (runningJob.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40)) {
                                                runningJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_FAIL);
                                            } else {
                                                runningJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.FAIL);
                                            }

                                            retrieverDAO.update(runningJob);
                                        } else {

                                            String usjNo = runningJob.getUsj_seq() + runningJob.getUsj_year();
                                            String divNo = runningJob.getUsj_div();

                                            String responseBody = psAction.getTraverseStatus(usjNo, divNo);
                                            cf.writeFile("USJPrecheckLog", responseBody);

                                            if (!responseBody.isEmpty()) {
                                                JSONObject statusJson = (JSONObject) new JSONParser().parse(responseBody);
                                                cf.writeFile("USJPrecheckLog", "status json - " + statusJson.toString());

                                                if (statusJson.containsKey("status")) {
                                                    String precheckStatus = (String) statusJson.get("status");
                                                    cf.writeFile("USJPrecheckLog", "precheck status - " + precheckStatus);

                                                    JSONObject result = (JSONObject) statusJson.get("result");
                                                    JSONObject status = (JSONObject) result.get("status");
                                                    String resultStatus = (String) status.get("Status");

                                                    if ("P".equals(resultStatus)) {
                                                        // runningJob.updatableColumns = new String[]{"job_id", "precheck_notif_sent"};
                                                        // runningJob.setPrecheck_notif_sent("Y");
                                                        if (runningJob.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40)) {
                                                            runningJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_PASS);
                                                        } else {
                                                            runningJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.TRAV_COMPLETE);
                                                        }

                                                        retrieverDAO.update(runningJob);
                                                    } else {
                                                        if (runningJob.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40)) {
                                                            runningJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV_FAIL);
                                                        } else {
                                                            runningJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.FAIL);
                                                        }

                                                        retrieverDAO.update(runningJob);
                                                    }
                                                }
                                            }

                                        }

                                    } else {
//                                        System.out.println("trav job running");
                                    }
                                }

                            } else if (runningJob.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.UTIL_STARTED) || runningJob.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_STARTED)) {
//                                System.out.println("Running Util Prechecks");
                                PrecheckLog utilPcLog = (PrecheckLog) retrieverDAO.getModelByCode("job_id", runningJob.getJob_id(), new PrecheckLog());

                                if (utilPcLog != null) {
//                                    System.out.println("utilPcLog got in backend");
//                                    String[] precheckFields = new String[]{"precheck_job_id", "job_id", "job_status", "usj_no", "no_of_errors", "tot_files", "tot_gislayer", "created_date", "created_by", "updated_date", "updated_by", "precheck_passed", "precheck_log"};
//                                    Map utilPcMap = getDataMap(utilPcLog, precheckFields);
//                                    subCaseMap.put("util_pc", utilPcMap);
                                    runningJob.updatableColumns = new String[]{"job_id", "precheck_stage"};
                                    switch (utilPcLog.getJob_status()) {
                                        case UtimapsAction.UTIL_PRECHECK_JOB_STATUS.SUBMITTED:
                                        case UtimapsAction.UTIL_PRECHECK_JOB_STATUS.EXECUTING:
                                            try {
                                                runningJob.set_operation(JobDetailModel.OPERATION.PB_UPDATE_PRECHECK_STAGE);

                                                if (runningJob.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK)) {
                                                    runningJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_STARTED);
                                                } else {
                                                    runningJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.UTIL_STARTED);
                                                }

                                                retrieverDAO.update(runningJob);

                                                if (!runningJob.getPrecheck_notif_sent().equals("N")) {
                                                    cf.writeFile("USJPrecheckLog", "set not sent precheck notif");
                                                    runningJob.updatableColumns = new String[]{"job_id", "precheck_notif_sent"};
                                                    runningJob.setPrecheck_notif_sent("N");
                                                    retrieverDAO.update(runningJob);
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                cf.writeFile("USJPrecheckLog", "Error in SUBMITTED/EXECUTING case: " + e.getMessage());
                                            }
//                            }
                                            break;
                                        case UtimapsAction.UTIL_PRECHECK_JOB_STATUS.SUCCESS:
                                            try {

                                                if (utilPcLog.getPrecheck_passed().equals("Y")) {
                                                    if (runningJob.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK)) {
                                                        runningJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_PASS);
                                                    } else {
                                                        runningJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.SUCCESS);
                                                    }
                                                } else {
                                                    if (runningJob.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK)) {
                                                        runningJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_FAIL);
                                                    } else {
                                                        runningJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.FAIL);
                                                    }
                                                }

                                                retrieverDAO.update(runningJob);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                cf.writeFile("USJPrecheckLog", "Error in SUCCESS case: " + e.getMessage());
                                            }
                                            break;
                                        case UtimapsAction.UTIL_PRECHECK_JOB_STATUS.FAIL:
                                            try {
                                                runningJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.FAIL);
                                                retrieverDAO.update(runningJob);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                cf.writeFile("USJPrecheckLog", "Error in FAIL case: " + e.getMessage());
                                            }
                                            break;
                                    }
                                }
                            }

                            psAction.handlePrecheckCompletedEmail(runningJob, retrieverDAO);
                        }

                    } else {
//                        System.out.println("No Running Prechecks Found");
                        cf.writeFile("USJPrecheckLog", "============================= No Running Prechecks Found ========================= ");

                    }

                    List doneList;
                    String doneSQL = "SELECT * FROM US_JOBDETAIL "
                            + "WHERE PRECHECK_STAGE IN ('" + UtimapsAction.PRECHECK_STAGE.TRAV_COMPLETE + "') ";
                    paramSQL.clear();
                    doneList = cf.getListFromSqlWithSession(retrieverDAO.getSession(), doneSQL, paramSQL);

                    if (!doneList.isEmpty()) {
//                        Debug.printDebug("Found Pending to Util Prechecks");

                        for (Object item : doneList) {
                            Map<String, String> element = (Map) item;
//                            System.out.println("pending id - " + element.get("JOB_ID"));
                            JobDetailModel pendingJob = (JobDetailModel) retrieverDAO.getModelByCode("job_id", element.get("JOB_ID"), new JobDetailModel());

                            String utilInitStr = psAction.initUtilPrecheck(pendingJob);
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

//                                System.out.println("==========================================");
//                                System.out.println("UTIL PRECHECK MESSAGE - " + message);
//                                System.out.println("==========================================");

                                try {

                                    pendingJob.updatableColumns = new String[]{"job_id", "precheck_stage"};

                                    if (pendingJob.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK)) {
                                        pendingJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL_STARTED);
                                    } else {
                                        pendingJob.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.UTIL_STARTED);
                                    }

                                    retrieverDAO.update(pendingJob);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

//                                resStatus = 200;
//                                resMsg = sMsg;
                            } else {
//                                resStatus = 500;
//                                resMsg = sMsg;
                            }
                        }

                    }

//                    System.out.println("\n\n=========================================================================\n");

                    Timestamp timeEnd = DateUtil.getCurrentTimestamp();
                    if (waitingTime - (timeEnd.getTime() - timeStart.getTime()) > 0) {
                        sleepTime = waitingTime - (timeEnd.getTime() - timeStart.getTime());
                        try {
                            sleep(sleepTime);
                        } catch (InterruptedException e) {
                            CommonFunction.writeFile("interruptError", "USJBackendAction: " + e.getMessage());
                            sleep(waitingTime);
                            break;
                        }
                    }
                } catch (Exception ex) {
//                    System.out.println("try begin. catch:" + ex);
                    ex.printStackTrace();
                    retrieverDAO.closeSession();
//                    baseDAO.closeSession();
                    cf.writeFile("USJPrecheckLog", "Precheck Action Running with Exception: " + ex);

                    sleep(waitingTime * 3);//do nothing.

                } finally {
                }
            }//[end while]
        } catch (Exception e) {
            e.printStackTrace();
//            new LogFunction().logError(this.getClass(), "PC Running with Exception", e);
            retrieverDAO.closeSession();
            cf.writeFile("USJPrecheckLog", "USJPrecheckRunning with Exception: " + e);
        } finally {
            try {
                retrieverDAO.closeSession();
            } catch (Exception ex) {
                Logger.getLogger(PrecheckAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
