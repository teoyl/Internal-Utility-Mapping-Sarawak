package com.sains.framework.base.web;

import com.backend.SetupSchedularModel;
import com.backend.TimerJob;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CommonList;
import com.sains.framework.base.TimerBase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

public class SchedularMgmtAction extends BaseActionSupport{// implements ModelDriven<String> {

    private static final long serialVersionUID = -6659925652584240540L;

    public SchedularMgmtAction() {
        // change the DAO to the correct service.
        //baseDAO = new BaseDAOImpl();
        model = new String();
        setInsertUpdateSuccessCode(SystemConstants.ACTION_Status.LOAD_EDIT_PAGE);
        // set the required field for common check.
        //getRequiredParam().put("column_name1", "column.name1");
        //getRequiredParam().put("column_name2", "column.name2");


    }

    public void specificValidation(String validationType) {
    }

    public String delete() {
        return SUCCESS;
    }

    //** for schedular maintenance :: START **//st
    private Map editTimerJobMap = null;
    public Map getEditTimerJobMap() {
        if (editTimerJobMap == null) {
            editTimerJobMap = new HashMap();
        }
        return editTimerJobMap;
    }
    public void setEditTimerJobMap(Map editTimerJobMap) {
        this.editTimerJobMap = editTimerJobMap;
    }
    
    public List getJobTypeList() {
        try {
            CommonList.getJobTypeList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonList.getJobTypeList();
    }
    private List timerJobList = null;
    public List<TimerJob> getTimerJobList() {
        if (timerJobList == null) {
            timerJobList = new ArrayList();
        }
        return timerJobList;
    }
    public void setTimerJobList(List timerJobList) {
        this.timerJobList = timerJobList;
    }
    
    public List getTimerJobMap() {
        return new ArrayList(TimerBase.manageableJob.values());
    }
    
    @Override
    public String loadEditPage() {
        return "timerJobList";
    }
    
    @Override
    public String processUpdate() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        if (TimerBase.manageableJob != null && !TimerBase.manageableJob.isEmpty()) {
            Boolean foundUpdateJob = Boolean.FALSE;
            BaseDAO dao = baseDAO;
            List<SetupSchedularModel> schedularSetupList = dao.list(SetupSchedularModel.class);
            try {
                dao.beginBatchTransaction();
                for (String key : TimerBase.manageableJob.keySet()) {
                    TimerJob theJob = TimerBase.manageableJob.get(key);
                    for (TimerJob newJobData : getTimerJobList()) {
//                        System.out.println("newJobData.getJobSwitch_on() = " + newJobData.getJobSwitch_on());
                        if (newJobData.getJobCode().equals(theJob.getJobCode())) {
                            //only update the configurable info if is not Default
                            if (!theJob.getJobRepeat().equals(TimerJob.REPEAT.Default)) {
                                theJob.setAlwaysTriggerOnce(newJobData.getAlwaysTriggerOnce());
                                if (newJobData.getAlwaysTriggerOnce()) {
                                    theJob.jobTriggered = Boolean.FALSE;
                                    theJob.setNextTrigger(null);
                                }
                                theJob.setTriggerAfterRestart(newJobData.getTriggerAfterRestart());
                                theJob.setJobRepeat(newJobData.getJobRepeat());
                            }
                            theJob.setJobRunEvery(newJobData.getJobRunEvery());
                            theJob.setJobSwitch_on(newJobData.getJobSwitch_on());
                            for (SetupSchedularModel schedularSetup : schedularSetupList) {
                                if (schedularSetup.getJob_code().equals(theJob.getJobCode())) {
                                    foundUpdateJob = Boolean.TRUE;
                                    if (SystemConstants.staticVarMap.containsKey(schedularSetup.getJob_code())) {
                                        SystemConstants.staticVarMap.put(schedularSetup.getJob_code(), newJobData.getJobSwitch_on()?"true":"false");
                                    }
                                    schedularSetup.setJob_switch_on_boo(newJobData.getJobSwitch_on());//still save to DB
                                    schedularSetup.setJob_repeat_every(theJob.getJobRunEvery());
                                    if (!theJob.getJobRepeat().equals(TimerJob.REPEAT.Default)){
                                        schedularSetup.setTrigger_once_boo(theJob.getTriggerAfterRestart());
                                        schedularSetup.setJob_repeat(theJob.getJobRepeat());
                                    } else {
                                        if (schedularSetup.getJob_switch_on_boo()) {//true
                                            if (theJob.getMyScheduledFuture() != null) {
                                                if (theJob.getMyScheduledFuture().getDelay(TimeUnit.MILLISECONDS) > 0) {
                                                    theJob.jobScheduler.shutdown();
                                                    theJob.jobScheduler = null;
                                                    theJob.jobTriggered = Boolean.FALSE;
                                                } else {
                                                    System.out.println("theJob.getJobCode() + \" is running, will not reset the scheduler for this job until it has complete running");
                                                    addActionMessage(theJob.getJobCode() + " is running, will not reset the scheduler for this job until it has complete running");
                                                }
                                            } else {
                                                if (theJob.jobScheduler != null) {
                                                    theJob.jobScheduler.shutdown();
                                                    theJob.jobScheduler = null;
                                                    theJob.jobTriggered = Boolean.FALSE;
                                                }
                                            }
                                        }
                                    }
                                    theJob.setNextTrigger(null);
                                    dao.getSession().update(schedularSetup);
                                }
                            }
                        }
                    }
                }
                if (foundUpdateJob) {
                    dao.commitBatchTransaction();
                    CommonFunction.saveStaticVar();
                } else {
                    dao.rollbackBatchTransaction();
                }
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
            }
            r = "rmsg;"+getText("schedular.updated");
        }
            
        return SUCCESS;
    }
    
    public String getMainSwitchValue() {
        if (SystemConstants.staticVarMap.containsKey("triggerTimer") && SystemConstants.staticVarMap.get("triggerTimer").equalsIgnoreCase("true")) {
            return "ON";
        }
        return "OFF";
    }
    //** for schedular maintenance :: END **//
}
