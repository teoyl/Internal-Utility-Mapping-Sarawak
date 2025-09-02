package com.backend;

import com.sains.common.util.DateUtil;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import java.util.concurrent.TimeUnit;

public class DailyJob extends TimerJob {
    public DailyJob() {
        super();
        setJobSwitch_on(Boolean.FALSE);
        alwaysTriggerOnce = Boolean.FALSE;
        jobRepeat = REPEAT.Daily;
        jobRunEvery = "11:50"; //HH:MM (Day of the month in 2 digits)
    }
    @Override
    public void run() {
        if (triggerJob()) {
            ModelBase.threadVar.remove(Thread.currentThread().getId()); //remove everytime
//            BaseDAO dao = new BaseDAOImpl();
            try {
                updateLastTriggered();
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
        Debug.printInfo("DailyJob "+jobRunEvery+" runMe() is called");
    }

    public Integer firstDelay() {
        return null;
    }
}