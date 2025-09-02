/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend;

import com.sains.common.util.DateUtil;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.LogFunction;
import com.sains.framework.base.ServiceFactory;
import static java.lang.Thread.sleep;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joveni.h
 */
public class Post2RVS extends TimerJob {
    
    private long waitingTime ; 
    public Boolean keepChecking = true;
    
    private BaseDAO retrieverDAO = new BaseDAOImpl();
    
    public static final class SQL_TYPE {
        public static final String USJ_PAYMENT_FESS = "USJ_PAYMENT_FESS";
    }

    public Post2RVS() {
//        setName("Post2RVS");
        super();
        setJobSwitch_on(Boolean.FALSE);
        alwaysTriggerOnce = Boolean.FALSE;
        jobRepeat = TimerJob.REPEAT.Default;
        jobRunEvery = "15"; //HH:MM (Day of the month in 2 digits)     
    }

    @Override
    public void run() {
    if (triggerJob()) { //serene @ 29/6/2022 :: for control job on or not from schedular
        waitingTime = 900000;
        int intError = 0;
        long sleepTime = 0;

        CommonFunction cf = new CommonFunction();
        try {
//            while (keepChecking) {
                  setStrQuery(SQL_TYPE.USJ_PAYMENT_FESS);
                  cf.writeFile("Post2RVS", "[start] Post2RVSJob**********************");
                try {
//                    CommonFunction.backend_checkIn(SystemConstants.BACKENDSERVICENAME.POST2RVS);
                    Timestamp timeStart = DateUtil.getCurrentTimestamp();
                    /*************     Data from eLodgement payment (Lodgement Fess)     **********************/
                    
                    setStrQuery(SQL_TYPE.USJ_PAYMENT_FESS);
                    List usjPaymentList = retrieverDAO.getListFromSql(strQuery, null);
                    cf.writeFile("Post2RVS", "Post2RVS >>> usj list size :: " +usjPaymentList.size());
                    
                    try {
                        ServiceFactory.getInstance().getPost2RVSService().post2RVS(usjPaymentList);
                    } catch (Exception e) {
                        retrieverDAO.closeSession();
                        new LogFunction().logError(this.getClass(), "Post2RVSService Running with Exception", e);
                    }
                    
                    if (intError != 0) {
                        CommonFunction.writeFile("Post2RVS", "[ERROR] >>> Failed to Post to RVS.");
                    }
                    Timestamp timeEnd = DateUtil.getCurrentTimestamp();
                    retrieverDAO.closeSession();
                    if (waitingTime - (timeEnd.getTime() - timeStart.getTime()) > 0) {
                        sleepTime = waitingTime - (timeEnd.getTime() - timeStart.getTime());
                        try {
                            sleep(sleepTime);
                        } catch (InterruptedException e) {
                            CommonFunction.writeFile("Post2RVS", "[ERROR] >>> interruptError :: " +e.getMessage());
                            sleep(waitingTime);
                        }
                    }
                } catch (Exception ex){
                    retrieverDAO.closeSession();
                    CommonFunction.writeFile("Post2RVS", "[ERROR] >>> Post2RVS Running with Exception :: " +ex.getMessage());
                    sleep(waitingTime);//do nothing.
                } 
//            }
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "Post2RVS Main Try", e);
            CommonFunction.writeFile("Post2RVS", "[ERROR] >>> Post2RVS Main Try :: " +e.getMessage());
        } finally {
                retrieverDAO.closeSession();
        }
    }
    }
    
    private String strQuery = "";
    public void setStrQuery(String sqlType) {
        if (sqlType.equals(SQL_TYPE.USJ_PAYMENT_FESS)) {
              //String rvrCodeFee = "'214','215'";
            
            strQuery = "SELECT uap.CASE_REF AS jobno, uap.CASE_DIV, up.PAYMENT_ID AS payid, upi.PAYITEM_ID AS itemid,to_char(up.payment_date, 'yyyy-mm-dd hh24:mi:ss') as paymentdate, " +
                    "uap.CASE_REF AS ECASEREF, " +
                    "to_char(up.payment_status_date, 'yyyy-mm-dd hh24:mi:ss') as statusdate, upi.item_amount as podprice, upi.rvr_code as rvrcode, " +
                    "upi.subcode as subcode, up.payment_method as paymentmethod, up.bill_ref_no as billrefno, " +
                    "regexp_replace(upi.ITEM_REF_NO , '/', '') AS laaspayid "+
                    "FROM US_PAYMENT up, US_PAYMENT_ITEM upi, US_APPLICATION_P uap " +
                    "WHERE up.PAYMENT_ID = upi.PAYMENT_ID " +
                    "AND up.CASE_ID = uap.CASE_ID " +
                    "AND up.PAYMENT_STATUS = '"+ SystemConstants.PodStatus_Type.PAYMENT_COMPLETED + "' "   + 
                    "AND upi.RVS_POSTED = 'N' " +
                    "ORDER BY up.PAYMENT_DATE ";
            
            new LogFunction().logDebug(this.getClass(), "*** USJ PAYMENT FEES SQL: " + strQuery, null);
        }
    }
}
