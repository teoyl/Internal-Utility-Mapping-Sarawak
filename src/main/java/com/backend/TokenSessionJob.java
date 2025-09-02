package com.backend;

import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import com.sains.framework.model.ParameterModel;
import com.sains.framework.model.TokenSessionModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TokenSessionJob extends TimerJob {
    public TokenSessionJob() {
        super();
        setJobSwitch_on(Boolean.FALSE);
        alwaysTriggerOnce = Boolean.FALSE;
        jobRepeat = REPEAT.Default;
        jobRunEvery = "60"; //HH:MM (Day of the month in 2 digits)
    }
    @Override
    public void run() {
        if (triggerJob()) {
            ModelBase.threadVar.remove(Thread.currentThread().getId()); //remove everytime
            ModelBase.updateCurrentRequestObject("councilCode", "B2");
            BaseDAO dao = new BaseDAOImpl();
            try {
                ParameterModel paramModel = (ParameterModel) dao.getSession().getNamedQuery("Parameter.getByParameter_code")
                .setParameter("system_code", "DEF")
                .setParameter("parameter_code", "cleanHour")
                .uniqueResult();
                Integer hour = null;
                try {
                    hour = Integer.parseInt(paramModel.getParameter_value());
                } catch (Exception e) { //if error then always default to 1
                    hour = 1;
                }
                if (hour < 1) hour = 1; //in case the system parameter there set to 0
                
                List<TokenSessionModel> tokenList = dao.list(TokenSessionModel.class);
                Calendar cal = DateUtil.getCalendar();
                List<TokenSessionModel> deleteList = new ArrayList();
                for (TokenSessionModel tokenSession : tokenList) {
                    Calendar lastCal = null;
                    if (tokenSession.getToken_last_active_datetime() == null) {
                        lastCal = DateUtil.getCalendar(tokenSession.getCreated_date());
                    } else {
                        lastCal = DateUtil.getCalendar(tokenSession.getToken_last_active_datetime());
                    }
                    
                    cal.add(Calendar.HOUR, hour*-1);
                    if (cal.after(lastCal)) {
                        deleteList.add(tokenSession);
                    }
                }
                if (!deleteList.isEmpty()) {
                    try {
                        dao.beginBatchTransaction();
                        for (TokenSessionModel tokenSession : deleteList) {
                            dao.getSession().delete(tokenSession);
                        }
                        dao.commitBatchTransaction();
                        System.out.println(deleteList.size() + " token session deleted");
                    } catch (Exception e) {
                        dao.rollbackBatchTransaction();
                        throw e;
                    }
                }
            } catch (Exception e) {
                Debug.printError("TokenSessionJob error");
                e.printStackTrace();
            } finally {
                dao.closeSession();
            }
        }
    }
    public void runMe() {
        Debug.printInfo("TokenSessionJob "+jobRunEvery+" runMe() is called");
    }

    public Integer firstDelay() {
        return null;
    }
}