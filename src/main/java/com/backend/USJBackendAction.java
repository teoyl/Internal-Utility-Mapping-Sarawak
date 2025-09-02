package com.backend;

import com.backend.TimerJob;
import com.sains.common.util.DateUtil;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import com.sains.workflow.web.WorkflowApiAction;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.web.UtimapsAction;
import static java.lang.Thread.sleep;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class USJBackendAction extends TimerJob {
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
    
    public USJBackendAction() {
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
                    cf.writeFile("USJBackendLog", "DEBUG >>>>> Opened::" + debugOpenCount + "<<>>Closed::" + debugCloseCount);
                }

                try {
                    Timestamp timeStart = DateUtil.getCurrentTimestamp();
                    cf.writeFile("USJBackendLog", "**********[start] USJ USJBackendAction*** " + timeStart);
                    ModelBase.updateCurrentRequestObject("antiCSRF", "ignoreCSRF");
                    String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    paramMap.clear();
                    paramMap.put("app_status", UtimapsAction.PB_STATUS.APP_PAYMENT_COMPLETED);
                    paramMap.put("wf_status", UtimapsAction.WF_STATUS.APPLICATION_PAYMENT_COMPLETED);
                    List<ApplicationPModel> caseList = baseDAO.list_order(paramMap, ApplicationPModel.class, true, " order by case_ref");
                    cf.writeFile("USJBackendLog", "caseList.size()***************************************" + caseList.size());

                    if (caseList.size()>0){
                        for (ApplicationPModel mapCase : caseList) {
                            triggerWfUSJ();
                        }
                    }
                    
                    Timestamp timeEnd = DateUtil.getCurrentTimestamp();
                    if (waitingTime - (timeEnd.getTime() - timeStart.getTime()) > 0) {
                        sleepTime = waitingTime - (timeEnd.getTime() - timeStart.getTime());
                        try {
                            sleep(sleepTime);
                        } catch (InterruptedException e) {
                            CommonFunction.writeLogFile(e.getStackTrace(), "interruptError", "USJBackendAction", "run");
                            sleep(waitingTime);
                            break;
                        }
                    }
                } catch (Exception ex) {;
                    retrieverDAO.closeSession();
                    CommonFunction.writeLogFile(ex.getStackTrace(), "USJBackendLog", "USJBackendAction", "run");
                    sleep(waitingTime * 3);//do nothing.

                } finally {
                }
            }
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "USJBackendLog", "USJBackendAction", "run");
        } finally {
            try {
                retrieverDAO.closeSession();
            } catch (Exception ex) {
                CommonFunction.writeLogFile(ex.getStackTrace(), "USJBackendLog", "USJBackendAction", "run");
            }
        }
        
    }
    
    public void triggerWfUSJ() {
        try {
            cf.writeFile("USJBackendLog", "triggerWfUSJ***********issuance of usj************");
            WorkflowApiAction wfApi = null;
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            
            //To update application to app
            //TO create submission record
            String strSk2="";
            String strPpSk2="";
            cf.writeFile("USJBackendLog","triggerWfUSJ curDate: " + today);
            paramMap.clear();
            paramMap.put("app_status", UtimapsAction.PB_STATUS.APP_PAYMENT_COMPLETED);
            paramMap.put("wf_status", UtimapsAction.WF_STATUS.APPLICATION_PAYMENT_COMPLETED);
            List<ApplicationPModel> caseList = baseDAO.list_order(paramMap, ApplicationPModel.class, true, " order by case_ref");
            cf.writeFile("USJBackendLog", "caseList: " + caseList.size());

            if (caseList.size() > 0) {
                for (ApplicationPModel appWf : caseList) {
                    try {
                        cf.writeFile("USJBackendLog", "triggerWfUSJ case: " + appWf.getCase_ref());
                        cf.writeFile("USJBackendLog", "appWf.getCase_id() :: "+appWf.getCase_id());
                        
                        wfApi = new WorkflowApiAction();
                        wfApi.startUSJ002(appWf.getCase_id());
                    } catch (Exception e) {
                        CommonFunction.writeLogFile(e.getStackTrace(), "USJBackendLog", "USJBackendAction", "triggerWfUSJ");
                        continue;
                    }

                }

            }
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "USJBackendLog", "USJBackendAction", "triggerWfUSJ");
        }
    }
}
