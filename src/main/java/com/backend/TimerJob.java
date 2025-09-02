package com.backend;

import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.Options;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.Debug;
import com.sains.framework.base.TimerBase;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimerJob implements Runnable {
    public static final class REPEAT {
        public static final String Yearly = "Y";
        public static final String Monthly = "M";
        public static final String Daily = "D";
        public static final String DayOfWeek = "W";
        public static final String Default = "F";
    }
    
    private Boolean jobSwitch_on = Boolean.FALSE;
    public Boolean alwaysTriggerOnce = Boolean.FALSE;
    public Boolean jobTriggered = Boolean.FALSE;
    public ScheduledExecutorService jobScheduler = null;
    

    public String jobRepeat = REPEAT.Default;
    private static List dayOfWeekList = null;

    private ScheduledFuture myScheduledFuture = null;
    public ScheduledFuture getMyScheduledFuture() {
        return myScheduledFuture;
    }
    public void setMyScheduledFuture(ScheduledFuture myScheduledFuture) {
        this.myScheduledFuture = myScheduledFuture;
    }
    
    //** for DayOfWeek list **//
    public List<Options> getDayOfWeekList() {
        if (dayOfWeekList == null) {
            dayOfWeekList = new ArrayList();
            dayOfWeekList.add(new Options(Calendar.MONDAY+"", "Monday"));
            dayOfWeekList.add(new Options(Calendar.TUESDAY+"", "Tuesday"));
            dayOfWeekList.add(new Options(Calendar.WEDNESDAY+"", "Wednesday"));
            dayOfWeekList.add(new Options(Calendar.THURSDAY+"", "Thursday"));
            dayOfWeekList.add(new Options(Calendar.FRIDAY+"", "Friday"));
            dayOfWeekList.add(new Options(Calendar.SATURDAY+"", "Saturday"));
            dayOfWeekList.add(new Options(Calendar.SUNDAY+"", "Sunday"));
        }
        return dayOfWeekList;
    }
    
    //** for jobRepeat = REPEAT.Default  : START **//
    private static List defaultUnitList = null;
    public List getDefaultUnitList() {
        if (defaultUnitList == null) {
            defaultUnitList = new ArrayList();
            defaultUnitList.add(new Options("H", "Hour"));
            defaultUnitList.add(new Options("M", "Minute"));
        }
        return defaultUnitList;
    }
    
    //unit for Hour/Minute when REPEAT.Default is used
    private String repeatUnit = "H"; //default to "H" Hourly
    public String getRepeatUnit() {
        return repeatUnit;
    }
    public void setRepeatUnit(String repeatUnit) {
        this.repeatUnit = repeatUnit;
    }
    public TimeUnit getRepeatTimeUnit() {
        if (repeatUnit.equals("M")) {
            return TimeUnit.MINUTES;
        }
        return TimeUnit.HOURS;
    }
    
    private Integer repeatEvery = 10;
    public Integer getRepeatEvery() {
        return repeatEvery;
    }
    public void setRepeatEvery(Integer repeatEvery) {
        this.repeatEvery = repeatEvery;
    }
    //** for jobRepeat = REPEAT.Default  : END **//
    
    public Boolean triggerJob() {
        return jobSwitch_on && TimerBase.triggerTimer;
    }
    public Boolean getJobSwitch_on() {
        return jobSwitch_on;
    }

    public void setJobSwitch_on(Boolean jobSwitch_on) {
        this.jobSwitch_on = jobSwitch_on;
    }

    public Boolean getAlwaysTriggerOnce() {
        return alwaysTriggerOnce;
    }

    public void setAlwaysTriggerOnce(Boolean alwaysTriggerOnce) {
        this.alwaysTriggerOnce = alwaysTriggerOnce;
    }

    public String getJobRepeat_disp() {
        if (jobRepeat.equals("W")) {
            String dayOfWeek = jobRunEvery.substring(6);
            for (Options opt : getDayOfWeekList()) {
                if (opt.getKeyData().equals(dayOfWeek)) {
                    return "Every " + opt.getValueData() + " " + jobRunEvery.substring(0, 6);
                }
            }
        } else if (jobRepeat.equals("D")) {
            return "Everyday " + jobRunEvery;
        } else if (jobRepeat.equals("M")) {
            return "Every " + jobRunEvery + " of the month";
        } else if (jobRepeat.equals("Y")) {
            return "Every " + jobRunEvery + " of the year";
        }
        return jobRepeat;
    }
    public String getJobRepeat() {
        return jobRepeat;
    }

    public void setJobRepeat(String jobRepeat) {
        this.jobRepeat = jobRepeat;
    }

    public String getNextTrigger() {
        return nextTrigger;
    }

    public void setNextTrigger(String nextTrigger) {
        this.nextTrigger = nextTrigger;
    }
    
    public String jobRunEvery = null; //delay before first trigger:subsequence trigger:TimeUnit
    public String getJobRunEvery() {
        return jobRunEvery;
    }
    public void setJobRunEvery(String jobRunEvery) {
        this.jobRunEvery = jobRunEvery;
    }
    
    public String nextTrigger = null;
    
    public TimerJob() {
//** Yearly Setup **//
//    jobRepeat = REPEAT.Yearly;
//    jobRunEvery = "01-01"; //MM-DD (Month-Day)
    
    //** Monthly Setup**//
//    jobRepeat = REPEAT.Monthly;
//    jobRunEvery = "01"; //DD (Day of the month in 2 digits)
    
    //** Daily Setup**//
//    jobRepeat = REPEAT.Daily; //for midnight(12am), the setup is 00:00
//    jobRunEvery = "00:00"; //HH:MM (Day of the month in 2 digits)

    //** DayOfWeek Setup**//
//    jobRepeat = REPEAT.DayOfWeek; //for midnight(12am), the setup is 00:00
//    jobRunEvery = "00:00 "; //HH:MM (Day of the month in 2 digits)
    
    //** Default's Setup**//
//    jobRepeat = REPEAT.Default;
// setup the repeatUnit and repeatEvery
//    repeatUnit="H", repeatEvery=1 >> Repeat every 1 hour
        super();
    }
    @Override
    public void run() {
        if (jobSwitch_on) {
            BaseDAO dao = new BaseDAOImpl();
            try {
                Debug.printInfo("calling runMe() by scheduler");
                runMe();
            } catch (Exception e) {
                Debug.printError("Error when running runMe() ");
                e.printStackTrace();
            } finally {
                dao.closeSession();
            }
        }
    }
    public void runMe() {
        Debug.printInfo("runMe() is called");
    }

    public Integer firstDelay() {
        return null;
    }
    
    public Boolean jobStarted = Boolean.FALSE;
    public void stopMyJob() {
        jobScheduler.shutdown();
        jobStarted = Boolean.FALSE;
    }
    
    private String runOnceAfterUpdate = "N";
    public String getRunOnceAfterUpdate() {
        return runOnceAfterUpdate;
    }
    public void setRunOnceAfterUpdate(String runOnceAfterUpdate) {
        this.runOnceAfterUpdate = runOnceAfterUpdate;
    }
    public Boolean getRunOnceAfterUpdate_boo() {
        return runOnceAfterUpdate.equals("Y");
    }
    
    private String jobCode = null;
    public String getJobCode() {
        if (jobCode == null) {
            jobCode = this.getClass().getSimpleName();
        }
        return jobCode;
    }
    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }
    
    public Boolean triggerAfterRestart = Boolean.FALSE;
    public Boolean getTriggerAfterRestart() {
        return triggerAfterRestart;
    }

    public void setTriggerAfterRestart(Boolean triggerAfterRestart) {
        this.triggerAfterRestart = triggerAfterRestart;
    }
    
    private static Map<String, List<String>> lastTriggeredMap = new HashMap();
    public String getLastTriggered() {
        List<String> triggeredList = getLastTriggeredList();
        if (triggeredList != null) {
            return triggeredList.get(0);
        }
        return "";
    }
    public List getLastTriggeredList() {
        return lastTriggeredMap.get(jobCode);
    }
    public void updateLastTriggered() {
        String lastTriggered = Formatter.formatDate(DateUtil.getCurrentTimestamp(), "yyyy-MM-dd HH:mm:ss");
        List<String> triggeredList = getLastTriggeredList();
        if (triggeredList == null) {
            triggeredList = new ArrayList();
            triggeredList.add(lastTriggered);
            lastTriggeredMap.put(jobCode, triggeredList);
        } else {
            if (triggeredList.size() >= 10) {
                triggeredList.remove(triggeredList.size() - 1);
            }
            triggeredList.add(0, lastTriggered);
        }
        
//        String tempTriggered = lastTriggeredMap.get(jobCode);
//        if (Validator.isEmpty(tempTriggered)) {
//            lastTriggeredMap.put(jobCode, lastTriggered);
//        } else {
//            if (tempTriggered.contains(",")) {
//                lastTriggeredMap.put(jobCode, lastTriggered + ", " + tempTriggered.substring(0, tempTriggered.indexOf(",")));
//            } else {
//                lastTriggeredMap.put(jobCode, lastTriggered + ", " + tempTriggered);
//            }
//        }
    }
    
}