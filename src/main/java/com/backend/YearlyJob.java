package com.backend;

import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import java.util.concurrent.TimeUnit;

public class YearlyJob extends TimerJob {
    public YearlyJob() {
        super();
        setJobSwitch_on(Boolean.FALSE);
        alwaysTriggerOnce = Boolean.FALSE;
        jobRepeat = REPEAT.Yearly;
        jobRunEvery = "03-11"; //(MM-dd of the year)
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
        Debug.printInfo("YearlyJob " + jobRunEvery + " runMe() is called");
    }

    public Integer firstDelay() {
        return null;
    }
}