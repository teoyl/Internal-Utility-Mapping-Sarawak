package com.sains.framework.base;

import com.PropertyGetter;
import com.SysConf;
import com.backend.CompJobCheck;
import com.backend.CounterPaymentCheck;
import com.backend.DailyJob;
import com.backend.DailyJob2;
import com.backend.DayOfWeekJob;
import com.backend.JobStarter;
import com.backend.MonthlyJob;
import com.backend.NormalMailJob;
import com.backend.NormalSMSJob;
import com.backend.PaymentCheck;
import com.backend.Post2RVS;
import com.backend.SetupSchedularModel;
import com.backend.TimerJob;
import com.backend.TokenSessionJob;
import com.backend.YearlyJob;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.SystemConstants;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.backend.USJBackendAction;
import com.backend.PrecheckAction;
import com.backend.USJPaymentReminder;
//import javax.servlet.annotation.WebListener;//cannot use if JDK1.7 and below

//@WebListener;//cannot use if JDK1.7 and below
public class TimerBase implements ServletContextListener {

    private ScheduledExecutorService scheduler = null;
//    private List list = new ArrayList();
    public static Boolean triggerTimer = Boolean.TRUE; //only set to true for those who need to run/text schedule job/Backend
    public static Map<String, TimerJob> customisableJob = null;
    public static Map<String, TimerJob> manageableJob = null;
    ScheduledFuture jobStarter = null;
    public static final String SystemShortName = PropertyGetter.getPropertyText_package("system.code");
//    private Boolean triggerTimer = Boolean.FALSE; //only set to true for those who need to run/text schedule job/Backend
//    private Boolean triggerPendingTimer = Boolean.FALSE;
//    private BaseDAO dao = new BaseDAOImpl();
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        
        System.out.println("event.getSource().toString() " + event.getSource().toString());
        System.out.println("maxAge = " + event.getServletContext().getSessionCookieConfig().getMaxAge());
        
        //stop timer first
//        /* stop timer
        if (SystemConstants.staticVarMap == null) {
            SystemConstants.staticVarMap = new TreeMap();
            SystemConstants.staticVarMap.put("EMAIL_Queue", null); //set Email_Queue and SMS_Queue switch to use SystemConstants.staticVarMap (read from local file)
            SystemConstants.staticVarMap.put("SMS_Queue", null);
            SystemConstants.staticVarMap.put("triggerTimer", triggerTimer?"true":"false");
            try {
                CommonFunction.loadStaticVar();
            } catch (Exception e) {
            }
        }
        if (SystemConstants.staticVarMap.containsKey("triggerTimer")) {
            triggerTimer = SystemConstants.staticVarMap.get("triggerTimer").equalsIgnoreCase("true");
        }
        if (scheduler == null) {
            manageableJob = new HashMap();
            scheduler = Executors.newSingleThreadScheduledExecutor();
        }
        if (customisableJob == null) {
            customisableJob = new HashMap();
//            customisableJob.put("DayOfWeekJob", new DayOfWeekJob());
//            customisableJob.put("DailyJob", new DailyJob());
//            customisableJob.put("MonthlyJob", new MonthlyJob());
//            customisableJob.put("YearlyJob", new YearlyJob());
//            customisableJob.put("EMAIL_Queue", new NormalMailJob());
//            customisableJob.put("SMS_Queue", new NormalSMSJob());
//            customisableJob.put("TokenCleaner", new TokenSessionJob());
            customisableJob.put("PaymentCheck", new PaymentCheck());
            customisableJob.put("Post2RVS", new Post2RVS()); //yonglai @ 26/4/2024
            customisableJob.put("CounterPaymentCheck", new CounterPaymentCheck()); //yonglai @ 26/4/2024
            customisableJob.put("USJBackendAction", new USJBackendAction());
            customisableJob.put("USJPaymentReminder", new USJPaymentReminder()); //serene@27/6/2024
            customisableJob.put("PrecheckAction", new PrecheckAction());
            customisableJob.put("CompJobCheck", new CompJobCheck()); //yonglai@13/10/2024

            //add your new schedular job here...
        }
        //Do not remove this : start
        if (customisableJob != null) {
            manageableJob.putAll(customisableJob);
            BaseDAO dao = new BaseDAOImpl();
            List<SetupSchedularModel> schedularSetupList = dao.list(SetupSchedularModel.class);
//            for (SetupSchedularModel schedularSetup : schedularSetupList) {
//                if (SystemConstants.staticVarMap.containsKey(schedularSetup.getJob_code()) && SystemConstants.staticVarMap.get(schedularSetup.getJob_code())==null) {
//                    SystemConstants.staticVarMap.put(schedularSetup.getJob_code(), schedularSetup.getJob_switch_on_boo()?"true":"false");
//                }
//            }
            try {
                dao.beginBatchTransaction();
                for (String key : TimerBase.manageableJob.keySet()) {
                    TimerJob theJob = TimerBase.manageableJob.get(key);
                    Boolean foundDBSetup = Boolean.FALSE;
                    for (SetupSchedularModel schedularSetup : schedularSetupList) {
                        if (schedularSetup.getJob_code().equals(theJob.getJobCode())) {
                            foundDBSetup = Boolean.TRUE;
                            if (SystemConstants.staticVarMap.containsKey(schedularSetup.getJob_code())) {
                                theJob.setJobSwitch_on(SystemConstants.staticVarMap.get(schedularSetup.getJob_code()).equalsIgnoreCase("true"));
                            } else {
                                theJob.setJobSwitch_on(schedularSetup.getJob_switch_on_boo());
                            }
                            theJob.setJobRunEvery(schedularSetup.getJob_repeat_every());
                            if (!theJob.getJobRepeat().equals(TimerJob.REPEAT.Default)){
                                theJob.setAlwaysTriggerOnce(schedularSetup.getTrigger_once_boo());
                                theJob.setJobRepeat(schedularSetup.getJob_repeat());
                            }
                            break;
                        }
                    }
                    if (!foundDBSetup) {
                        SetupSchedularModel newSetup = new SetupSchedularModel();
                        newSetup.defaultAddProperties(SystemConstants.BACKEND.ADMIN_ID);
                        newSetup.setJob_code(key);
//                        newSetup.setJob_code(theJob.getJobCode());
                        if (SystemConstants.staticVarMap.containsKey(theJob.getJobCode())) {
                            SystemConstants.staticVarMap.put(theJob.getJobCode(), theJob.getJobSwitch_on()?"true":"false");
                        }
                        newSetup.setJob_switch_on_boo(theJob.getJobSwitch_on());
                        newSetup.setTrigger_once_boo(theJob.getAlwaysTriggerOnce());
                        newSetup.setJob_repeat(theJob.getJobRepeat());
                        newSetup.setJob_repeat_every(theJob.getJobRunEvery());
                        dao.getSession().save(newSetup);
                    }
                }
                dao.commitBatchTransaction();
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
            } finally {
                dao.closeSession();
            }
        }
        CommonFunction.saveStaticVar();
        Integer startDelay = 60;
        if (SysConf.get("jobStarter.startValue") != null) {
            startDelay = Integer.parseInt(SysConf.get("jobStarter.startValue"));
        }
        scheduler.scheduleAtFixedRate(new JobStarter(), (startDelay - Calendar.getInstance().get(Calendar.SECOND)), 15, TimeUnit.SECONDS); //base scheduler that keep checking at every 15 seconds interval
//            scheduler.scheduleAtFixedRate(new JobStarter(), 0, 15, TimeUnit.SECONDS); //base scheduler that keep checking at every 15 seconds interval
            //Do not remove this : end
        
//        if (triggerPendingTimer) {
//            AbcJob abcJob = new AbcJob();
//            scheduler.scheduleAtFixedRate(abcJob, (60 - Calendar.getInstance().get(Calendar.SECOND)), RepeatEvery?, TimeUnit.SECONDS);
//            managableJob.put("abcJob", abcJob);//add this job to managableJob if u want to manage it at run time later
//        }
//        stop timer */ 
        
        
//        scheduler.scheduleAtFixedRate(null, 0, 1, TimeUnit.HOURS);
//        scheduler.scheduleAtFixedRate(new SomeQuarterlyJob(), 0, 15, TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
//        System.out.println("scheduler.shutdownNow is called!!");
//        if (triggerTimer || triggerPendingTimer) {
    Enumeration<java .sql.Driver> drivers = java.sql.DriverManager.getDrivers();
    while (drivers.hasMoreElements()) {
        java.sql.Driver driver = drivers.nextElement();
        try {
            java.sql.DriverManager.deregisterDriver(driver);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
   try { Thread.sleep(2000L); } catch (Exception e) {}
        try {
            SessionFactoryImpl.closeSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            Debug.printError(SystemShortName + " close session factory got ERROR");
        } 
        if (scheduler != null) {
            Debug.printFrameworkDebug(SystemShortName + " scheduler shutdown");
            scheduler.shutdownNow();
        }
        if (customisableJob != null) {
            for (String key : customisableJob.keySet()) {
                if (customisableJob.get(key).jobScheduler != null) {
                    customisableJob.get(key).jobScheduler.shutdownNow();
                }
            }
        }
//        dao.closeSession();
    }

}