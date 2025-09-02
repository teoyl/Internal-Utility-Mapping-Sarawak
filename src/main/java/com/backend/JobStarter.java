package com.backend;

import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import com.sains.framework.base.TimerBase;
import static com.sains.framework.base.TimerBase.customisableJob;
import com.sains.framework.model.User;
import com.sample.ParentModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class JobStarter implements Runnable {
    
    public JobStarter() {
        super();
    }
    @Override
    public void run() {
//        Calendar cal = Calendar.getInstance();
//        System.out.println("cal.getTime = " + cal.getTime());
//        System.out.println("cal.getTime.getTime() = " + cal.getTime().getTime());
//        System.out.println("created = " + DateUtil.getCurrentTimestamp().getTime());
//        System.out.println("expired_in = " + (DateUtil.getCurrentTimestamp().getTime() + (1000*60*60)));
//        BaseDAO dao = new BaseDAOImpl();
        try {
            ModelBase.threadVar.remove(Thread.currentThread().getId()); //remove everytime
            runMe();
        } catch (Exception e) {
            Debug.printError("Error when running "+ TimerBase.SystemShortName +" JobStarter.runMe() ");
            e.printStackTrace();
        } finally {
//            dao.closeSession();
        }
    }
    public void runMe() {
        Date currentDate = DateUtil.getCurrentDate();
        if (customisableJob != null) {
            for (String key : customisableJob.keySet()) {
                TimerJob job = customisableJob.get(key);
                if (job.jobRepeat.equals(TimerJob.REPEAT.DayOfWeek)) {
                    String hourMinute = Formatter.formatDate(DateUtil.getCurrentDate(), "kk:mm");
                    if (hourMinute.startsWith("24")) {
                        hourMinute = "00" + hourMinute.substring(2);
                    }
                    if (job.nextTrigger == null) {
                        String jobHourMinute = job.jobRunEvery.substring(0, 5);
                        Calendar cal = DateUtil.getCalendar();
                        int dayOfWeek = Integer.parseInt(job.jobRunEvery.substring(6));
                        while (true) {
                            if (cal.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
                                break;
                            }
                            cal.add(Calendar.DAY_OF_MONTH, 1);
                        }
//                        if (cal.get(Calendar.DAY_OF_WEEK))
                        job.nextTrigger = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd") + " " + jobHourMinute;
    //                    System.out.println("Formatter.formatDate(DateUtil.getCurrentDate(), \"kk:mm\") = " + Formatter.formatDate(DateUtil.getCurrentDate(), "kk:mm"));
                    }
                    if (job.nextTrigger.compareTo(Formatter.formatDate(currentDate, "yyyy-MM-dd") + " " + hourMinute) <= 0) { //trigger
                        job.jobTriggered = Boolean.TRUE;
                        if (job.getMyScheduledFuture() != null) {
                            if (job.getMyScheduledFuture().getDelay(TimeUnit.MILLISECONDS) > 0) {
                                job.jobScheduler.shutdownNow();
                            } else {
                                continue;
                            }
                        }
                        job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
                        job.setMyScheduledFuture(job.jobScheduler.scheduleAtFixedRate(job, 0, 8, TimeUnit.DAYS));
//                        if (job.jobScheduler == null) {
//                            job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
////                            job.jobScheduler = Executors.newScheduledThreadPool(4);
//                            job.jobScheduler.scheduleAtFixedRate(job, 0, 7, TimeUnit.DAYS);
//                        }
                        
                        String jobHourMinute = job.jobRunEvery.substring(0, 5);
                        Calendar cal = DateUtil.getCalendar();
                        cal.add(Calendar.DAY_OF_MONTH, 7);
                        int dayOfWeek = Integer.parseInt(job.jobRunEvery.substring(6));
                        while (true) {
                            if (cal.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
                                break;
                            }
                            cal.add(Calendar.DAY_OF_MONTH, 1);
                        }
//                        if (cal.get(Calendar.DAY_OF_WEEK))
                        job.nextTrigger = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd") + " " + jobHourMinute;
                        
                        
                    } else if (job.triggerAfterRestart || (job.alwaysTriggerOnce && !job.jobTriggered)) {
                        job.alwaysTriggerOnce = Boolean.FALSE;
                        job.triggerAfterRestart = Boolean.FALSE;
                        job.jobTriggered = Boolean.TRUE;
                        if (job.getMyScheduledFuture() != null) {
                            if (job.getMyScheduledFuture().getDelay(TimeUnit.MILLISECONDS) > 0) {
                                job.jobScheduler.shutdownNow();
                            } else {
                                continue;
                            }
                        }
                        job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
                        job.setMyScheduledFuture(job.jobScheduler.scheduleAtFixedRate(job, 0, 8, TimeUnit.DAYS));
//                        if (job.jobScheduler == null) {
//                            job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
////                            job.jobScheduler = Executors.newScheduledThreadPool(4);
//                            job.jobScheduler.scheduleAtFixedRate(job, 0, 7, TimeUnit.DAYS);
//                        } else {
//                            job.jobScheduler.shutdownNow();
//                            job.jobScheduler.scheduleAtFixedRate(job, 0, 7, TimeUnit.DAYS);
//                        }
                    }
                } else if (job.jobRepeat.equals(TimerJob.REPEAT.Daily)) {
                    String hourMinute = Formatter.formatDate(DateUtil.getCurrentDate(), "kk:mm");
                    if (hourMinute.startsWith("24")) {
                        hourMinute = "00" + hourMinute.substring(2);
                    }
                    if (job.nextTrigger == null) {
                        job.nextTrigger = Formatter.formatDate(currentDate, "yyyy-MM-dd") + " " + job.jobRunEvery;
                        if (job.nextTrigger.compareTo(Formatter.formatDate(currentDate, "yyyy-MM-dd") + " " + hourMinute) <= 0) { //trigger
                            Calendar cal = DateUtil.getCalendar();
                            cal.add(Calendar.DAY_OF_MONTH, 1);
                            job.nextTrigger = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd") + " " + job.jobRunEvery;
                        }
    //                    System.out.println("Formatter.formatDate(DateUtil.getCurrentDate(), \"kk:mm\") = " + Formatter.formatDate(DateUtil.getCurrentDate(), "kk:mm"));
                    }
    //                System.out.println("------------Daily Job ------------");
    //                System.out.println("job.nextTrigger = " + job.nextTrigger);
    //                System.out.println("job.nextTrigger.compareTo(Formatter.formatDate(currentDate, \"yyyy-MM-DD\") + \" \" + hourMinute) = " + job.nextTrigger.compareTo(Formatter.formatDate(currentDate, "yyyy-MM-dd") + " " + hourMinute));
                    if (job.nextTrigger.compareTo(Formatter.formatDate(currentDate, "yyyy-MM-dd") + " " + hourMinute) <= 0) { //trigger
                        job.jobTriggered = Boolean.TRUE;
                        
                        if (job.getMyScheduledFuture() != null) {
                            if (job.getMyScheduledFuture().getDelay(TimeUnit.MILLISECONDS) > 0) {
                                job.jobScheduler.shutdownNow();
                            } else {
                                continue;
                            }
                        }
                        job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
                        job.setMyScheduledFuture(job.jobScheduler.scheduleAtFixedRate(job, 0, 2, TimeUnit.DAYS));
//                        if (job.jobScheduler == null) {
//                            job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
////                            job.jobScheduler = Executors.newScheduledThreadPool(4);
//                            job.jobScheduler.scheduleAtFixedRate(job, 0, 1, TimeUnit.DAYS);
//                        }
                        Calendar cal = DateUtil.getCalendar();
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        job.nextTrigger = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd") + " " + job.jobRunEvery;
                    } else if (job.triggerAfterRestart || (job.alwaysTriggerOnce && !job.jobTriggered)) {
                        job.alwaysTriggerOnce = Boolean.FALSE;
                        job.triggerAfterRestart = Boolean.FALSE;
                        job.jobTriggered = Boolean.TRUE;
                        
                        if (job.getMyScheduledFuture() != null) {
                            if (job.getMyScheduledFuture().getDelay(TimeUnit.MILLISECONDS) > 0) {
                                job.jobScheduler.shutdownNow();
                            } else {
                                continue;
                            }
                        }
                        job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
                        job.setMyScheduledFuture(job.jobScheduler.scheduleAtFixedRate(job, 0, 2, TimeUnit.DAYS));
                        
//                        if (job.jobScheduler == null) {
//                            job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
////                            job.jobScheduler = Executors.newScheduledThreadPool(4);
//                            job.jobScheduler.scheduleAtFixedRate(job, 0, 1, TimeUnit.DAYS);
//                        } else {
//                            job.jobScheduler.shutdownNow();
//                            job.jobScheduler.scheduleAtFixedRate(job, 0, 1, TimeUnit.DAYS);
//                        }
                    }
                } else if (job.jobRepeat.equals(TimerJob.REPEAT.Monthly)) {
                    Date monthlyCurrentDate = DateUtil.getCurrentDate();
                    String day = Formatter.formatDate(monthlyCurrentDate, "d");
                    if (day.length() == 1) {
                        day = "0"+day;
                    }
                    if (job.jobRunEvery.contains(":")) {
                        day += " " +Formatter.formatDate(currentDate, "kk:mm");
                    }
                    if (job.nextTrigger == null) {
                        job.nextTrigger = Formatter.formatDate(currentDate, "yyyy-MM") + "-" + job.jobRunEvery;
                        if (job.nextTrigger.compareTo(Formatter.formatDate(currentDate, "yyyy-MM") + "-" + day) <= 0) { //trigger
                            Calendar cal = DateUtil.getCalendar();
                            cal.add(Calendar.MONTH, 1);
                            job.nextTrigger = Formatter.formatDate(cal.getTime(), "yyyy-MM") + "-" + job.jobRunEvery;
                        }
    //                    System.out.println("Formatter.formatDate(DateUtil.getCurrentDate(), \"kk:mm\") = " + Formatter.formatDate(DateUtil.getCurrentDate(), "kk:mm"));
                    }
                    if (job.nextTrigger.compareTo(Formatter.formatDate(currentDate, "yyyy-MM") + "-" + day) <= 0) { //trigger
//                        job.alwaysTriggerOnce = Boolean.FALSE;
                        job.jobTriggered = Boolean.TRUE;
                        if (job.getMyScheduledFuture() != null) {
                            if (job.getMyScheduledFuture().getDelay(TimeUnit.MILLISECONDS) > 0) {
                                job.jobScheduler.shutdownNow();
                            } else {
                                continue;
                            }
                        }
                        job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
                        job.setMyScheduledFuture(job.jobScheduler.scheduleAtFixedRate(job, 0, 40, TimeUnit.DAYS));
//                        if (job.jobScheduler == null) {
//                            job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
////                            job.jobScheduler = Executors.newScheduledThreadPool(4);
//                            job.jobScheduler.scheduleAtFixedRate(job, 0, 40, TimeUnit.DAYS);
//                        } else {
//                            job.jobScheduler.shutdownNow();
//                            job.jobScheduler.scheduleAtFixedRate(job, 0, 40, TimeUnit.DAYS);
//                        }
                        Calendar cal = DateUtil.getCalendar();
                        cal.add(Calendar.MONTH, 1);
                        job.nextTrigger = Formatter.formatDate(cal.getTime(), "yyyy-MM") + "-" + job.jobRunEvery;
                    } else if (job.triggerAfterRestart || (job.alwaysTriggerOnce && !job.jobTriggered)) {
                        job.jobTriggered = Boolean.TRUE;
                        job.triggerAfterRestart = Boolean.FALSE;
                        job.alwaysTriggerOnce = Boolean.FALSE;
                        if (job.getMyScheduledFuture() != null) {
                            if (job.getMyScheduledFuture().getDelay(TimeUnit.MILLISECONDS) > 0) {
                                job.jobScheduler.shutdownNow();
                            } else {
                                continue;
                            }
                        }
                        job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
                        job.setMyScheduledFuture(job.jobScheduler.scheduleAtFixedRate(job, 0, 40, TimeUnit.DAYS));
//                        if (job.jobScheduler == null) {
//                            job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
////                            job.jobScheduler = Executors.newScheduledThreadPool(4);
//                            job.jobScheduler.scheduleAtFixedRate(job, 0, 40, TimeUnit.DAYS);
//                        } else {
//                            job.jobScheduler.shutdownNow();
//                            job.jobScheduler.scheduleAtFixedRate(job, 0, 40, TimeUnit.DAYS);
//                        }
                    }
                } else if (job.jobRepeat.equals(TimerJob.REPEAT.Yearly)) {
                    String mmdd = Formatter.formatDate(DateUtil.getCurrentDate(), "MM-dd");
                    if (job.jobRunEvery.contains(":")) {
                        mmdd += " " +Formatter.formatDate(currentDate, "kk:mm");
                    }
                    if (job.nextTrigger == null) {
                        job.nextTrigger = Formatter.formatDate(currentDate, "yyyy") + "-" + job.jobRunEvery;
                        if (job.nextTrigger.compareTo(Formatter.formatDate(currentDate, "yyyy") + "-" + mmdd) <= 0) { //trigger
                            Calendar cal = DateUtil.getCalendar();
                            cal.add(Calendar.YEAR, 1);
                            job.nextTrigger = Formatter.formatDate(cal.getTime(), "yyyy") + "-" + job.jobRunEvery;
                        }
                    }
    //                System.out.println("------------ Yearly Job ------------");
    //                System.out.println("job.nextTrigger = " + job.nextTrigger);
    //                System.out.println("job.nextTrigger.compareTo(Formatter.formatDate(currentDate, \"yyyy\") + \"-\" + mmdd) = " + job.nextTrigger.compareTo(Formatter.formatDate(currentDate, "yyyy") + "-" + mmdd));
                    if (job.nextTrigger.compareTo(Formatter.formatDate(currentDate, "yyyy") + "-" + mmdd) <= 0) { //trigger
                        job.jobTriggered = Boolean.TRUE;
//                        job.alwaysTriggerOnce = Boolean.FALSE;
                        if (job.getMyScheduledFuture() != null) {
                            if (job.getMyScheduledFuture().getDelay(TimeUnit.MILLISECONDS) > 0) {
                                job.jobScheduler.shutdownNow();
                            } else {
                                continue;
                            }
                        }
                        job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
                        job.setMyScheduledFuture(job.jobScheduler.scheduleAtFixedRate(job, 0, 400, TimeUnit.DAYS));
//                        if (job.jobScheduler == null) {
//                            job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
////                            job.jobScheduler = Executors.newScheduledThreadPool(4);
//                            job.jobScheduler.scheduleAtFixedRate(job, 0, 400, TimeUnit.DAYS);
//                        } else {
//                            job.jobScheduler.shutdownNow();
//                            job.jobScheduler.scheduleAtFixedRate(job, 0, 400, TimeUnit.DAYS);
//                        }
                        Calendar cal = DateUtil.getCalendar();
                        cal.add(Calendar.YEAR, 1);
                        job.nextTrigger = Formatter.formatDate(cal.getTime(), "yyyy") + "-" + job.jobRunEvery;
                    } else if (job.triggerAfterRestart || (job.alwaysTriggerOnce && !job.jobTriggered)) {
                        job.triggerAfterRestart = Boolean.FALSE;
                        job.jobTriggered = Boolean.TRUE;
                        job.alwaysTriggerOnce = Boolean.FALSE;
                        if (job.getMyScheduledFuture() != null) {
                            if (job.getMyScheduledFuture().getDelay(TimeUnit.MILLISECONDS) > 0) {
                                job.jobScheduler.shutdownNow();
                            } else {
                                continue;
                            }
                        }
                        job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
                        job.setMyScheduledFuture(job.jobScheduler.scheduleAtFixedRate(job, 0, 400, TimeUnit.DAYS));
//                        if (job.jobScheduler == null) {
//                            job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
////                            job.jobScheduler = Executors.newScheduledThreadPool(4);
//                            job.jobScheduler.scheduleAtFixedRate(job, 0, 400, TimeUnit.DAYS);
//                        } else {
//                            job.jobScheduler.shutdownNow();
//                            job.jobScheduler.scheduleAtFixedRate(job, 0, 400, TimeUnit.DAYS);
//                        }
                    }
                } else if (job.jobRepeat.equals(TimerJob.REPEAT.Default)) {
                    if (!job.jobTriggered) {
                        job.jobTriggered = Boolean.TRUE;
                        TimeUnit tu = TimeUnit.SECONDS;
                        String repEvery = job.jobRunEvery;
                        if (job.jobRunEvery.contains(" ")) {
                            String[] repeatSetting = job.jobRunEvery.split(" ");
                            repEvery = repeatSetting[0];
                            if (repeatSetting[1].equals("M")) {
                                tu = TimeUnit.MINUTES;
                            } else if (repeatSetting[1].equals("H")) {
                                tu = TimeUnit.HOURS;
                            }
                        }
                        if (job.jobScheduler == null) {
                            job.jobScheduler = Executors.newSingleThreadScheduledExecutor();
//                            job.jobScheduler = Executors.newScheduledThreadPool(4);
                            job.jobScheduler.scheduleAtFixedRate(job, 
                                (60 - Calendar.getInstance().get(Calendar.SECOND)), 
                                Long.parseLong(repEvery), tu);
                        }
                    }
                }
            }
        }
    }
}