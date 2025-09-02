package com.backend;

import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DayOfWeekJob extends TimerJob {
    public DayOfWeekJob() {
        super();
        setJobSwitch_on(Boolean.FALSE);
        alwaysTriggerOnce = Boolean.FALSE;
        jobRepeat = TimerJob.REPEAT.DayOfWeek;
        jobRunEvery = "11:08 " + Calendar.THURSDAY; //HH:MM _ (_ = Calendar.DAY_OF_WEEK)
    }
    @Override
    public void run() {
        if (triggerJob()) {
            ModelBase.threadVar.remove(Thread.currentThread().getId()); //remove everytime
//            BaseDAO dao = new BaseDAOImpl();
            try {
                runMe();
            } catch (Exception e) {
                Debug.printError("Error when running runMe() ");
                e.printStackTrace();
            } finally {
                jobScheduler.shutdown();
//                dao.closeSession();
            }
        }
    }
    public void runMe() {
        System.out.println("DaiOfWeekJob "+ jobRunEvery +" runMe() is called ");
    }

    public Integer firstDelay() {
        return null;
    }
}