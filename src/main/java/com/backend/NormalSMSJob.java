package com.backend;

import com.SysConf;
import com.sains.common.util.ClsMail;
import com.sains.common.util.DateUtil;
import com.sains.common.util.HttpsUrlUtil;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import com.sains.framework.model.EmailQueueModel;
import com.sains.framework.model.ParameterModel;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NormalSMSJob extends TimerJob {
    public NormalSMSJob() {
        super();
        setJobCode("SMS_Queue");
        setJobSwitch_on(Boolean.FALSE);
        alwaysTriggerOnce = Boolean.FALSE;
        jobRepeat = REPEAT.Default;
        jobRunEvery = "20"; //Default 1 M = 1 Minute, 1 H = 1 hour, else = second
    }
    @Override
    public void run() {
        if (triggerJob()) {
            ModelBase.threadVar.remove(Thread.currentThread().getId()); //remove everytime
            try {
                updateLastTriggered();
                runMe();
            } catch (Exception e) {
                Debug.printError("Error when running runMe() ");
            } finally {
//                jobScheduler.shutdown();
//                dao.closeSession();
            }
        }
    }
    public void runMe() {
        BaseDAO dao = new BaseDAOImpl();
        BaseDAO updateDao = new BaseDAOImpl();
        try {
            ModelBase.updateCurrentRequestObject("antiCSRF", "ignoreCSRF");
            Map param = new HashMap();
            param.put(SystemConstants.CRITERIA.NO_CHANGE+"notification_type", "S");
            param.put(SystemConstants.CRITERIA.NO_CHANGE+"send_status", "N");
            param.put(SystemConstants.CRITERIA.NO_CHANGE+"fail_count", "<= 3");
            ParameterModel paramModel = (ParameterModel) dao.getSession().getNamedQuery("Parameter.getByParameter_code")
                .setParameter("system_code", "DEF")
                .setParameter("parameter_code", "smsSize")
                .uniqueResult();
            String limit = "";
            if (paramModel != null) {
                limit = paramModel.getParameter_value();
                try {
                    dao.setMaxSize(Integer.parseInt(limit));
                    dao.setIsPaging(Boolean.TRUE);
                } catch (Exception e) {
                    limit = "";//if error then it is not an integer. set to empty string
                }
                dao.getSession().evict(paramModel);
            }
            List<EmailQueueModel> emailQueueList = dao.list_order(param, EmailQueueModel.class, true, "order by created_date");
            for (EmailQueueModel emailQueue : emailQueueList) {
                try {
                    String smsUrl = SysConf.get("sms.url")
                            .replace("<sms_phoneNo>", emailQueue.getRecipient())
                            .replace("<sms_msg>", URLEncoder.encode(emailQueue.getEmail_content(), "UTF-8"));

                    String rtnBody = null;
                    if (smsUrl.startsWith("https")) {
                        rtnBody = HttpsUrlUtil.https_selfSignCallUrlGet(smsUrl);
                    } else {
                        URL obj = new URL(smsUrl);
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                        //request GET
                        con.setRequestMethod("GET");

                        InputStream inputStream;
                        if (200 <= con.getResponseCode() && con.getResponseCode() <= 299) {
                            inputStream = con.getInputStream();
                            BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    inputStream));
                            StringBuilder response = new StringBuilder();
                            String currentLine;

                            while ((currentLine = in.readLine()) != null) 
                                response.append(currentLine);
                            in.close();
                            rtnBody = response.toString();
                            
                        } else {
                            throw new CustomBaseException("not 200, cannot access sms server??");
                        }
                    }
                    emailQueue.setSms_result(rtnBody);
                    if (!rtnBody.startsWith("-WELCOME-Queued")) {
                        throw new CustomBaseException("not success");
                    }
                    emailQueue.setSms_message_id(rtnBody.substring(15));
                    emailQueue.setSent_date(DateUtil.getCurrentTimestamp());
                    emailQueue.setSend_status("Y");
                    updateDao.update(emailQueue);
                } catch (Exception e) {
                    emailQueue.setFail_count(emailQueue.getFail_count()+1);
                    updateDao.update(emailQueue);
                }
            }
        } catch (Exception e) {
        } finally {
            dao.closeSession();
        }
        Debug.printInfo(getJobCode() + " job "+jobRunEvery+"seconds runMe() is called");
    }

    public Integer firstDelay() {
        return null;
    }
}