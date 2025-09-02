package com.backend;

import com.SysConf;
import com.sains.common.util.ClsMail;
import com.sains.common.util.DateUtil;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import com.sains.framework.model.EmailQueueAttachmentModel;
import com.sains.framework.model.EmailQueueModel;
import com.sains.framework.model.ErrLogModel;
import com.sains.framework.model.ParameterModel;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NormalMailJob extends TimerJob {
    public NormalMailJob() {
        super();
        setJobCode("EMAIL_Queue");
        setJobSwitch_on(Boolean.FALSE);
        alwaysTriggerOnce = Boolean.FALSE;
        jobRepeat = TimerJob.REPEAT.Default;
        jobRunEvery = "20"; //Default 1 M = 1 Minute, 1 H = 1 hour, else = second
    }
    @Override
    public void run() {
        if (triggerJob()) {
            ModelBase.threadVar.remove(Thread.currentThread().getId()); //remove everytime
            try {
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
        Timestamp processStartDate = null;
        try {
            ModelBase.updateCurrentRequestObject("antiCSRF", "ignoreCSRF");
            Map param = new HashMap();
            param.put(SystemConstants.CRITERIA.NO_CHANGE+"notification_type", "E");
            param.put(SystemConstants.CRITERIA.NO_CHANGE+"send_status", "N");
            param.put(SystemConstants.CRITERIA.NO_CHANGE+"fail_count", "<= 3");
            ParameterModel paramModel = (ParameterModel) dao.getSession().getNamedQuery("Parameter.getByParameter_code")
                .setParameter("system_code", "DEF")
                .setParameter("parameter_code", "emailSize")
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
//            System.out.println("before for loop, size = " + emailQueueList.size());
            for (EmailQueueModel emailQueue : emailQueueList) {
                try {
                    processStartDate = DateUtil.getCurrentTimestamp();
                    ClsMail mail = new ClsMail();
                    mail.setSendFrom(emailQueue.getEmail_sender());
                    if (!Validator.isEmpty(emailQueue.getBcc())) {
                        mail.setBcc(emailQueue.getBcc());
                    } else {
                        mail.setBcc("");
                    }
                    if (Validator.isEmpty(emailQueue.getSmtp()) ) {
                        mail.setOutMailServer(SysConf.get("smtp.default"));
                        mail.setInMailServer(SysConf.get("smtp.default"));
                    } else {
                        mail.setOutMailServer(emailQueue.getSmtp());
                        mail.setInMailServer(emailQueue.getSmtp());
                    }


                    mail.setTo(emailQueue.getRecipient());
                    if (!Validator.isEmpty(emailQueue.getCc())) {
                        mail.setCc(emailQueue.getCc());
                    } else {
                        mail.setCc("");
                    }

                    mail.setSubject(emailQueue.getEmail_subject());
                    mail.setMailType("H"); // HTML Style;

                    mail.setBody(emailQueue.getEmail_content());
//                    if (emailQueue.getAttachmentList() != null && !emailQueue.getAttachmentList().isEmpty()) {
//                        mail.setAttfilename(emailQueue.getAttachmentList());
//                    }
                    mail.sendMail();
//                    emailQueue.setSent_date(DateUtil.getCurrentTimestamp());
//                    emailQueue.setSend_status("Y");
                    updateDao.beginBatchTransaction();
                    updateDao.getSession().createNativeQuery("update t_email_queue "
                            + "set send_status = 'Y', sent_date = :sentDate, process_start_date = :processStartDate "
                            + "where email_queue_id = :emailQueueId")
                            .setParameter("emailQueueId", emailQueue.getID())
                            .setParameter("sentDate", DateUtil.getCurrentTimestamp())
                            .setParameter("processStartDate", processStartDate)
                            .executeUpdate();
                    updateDao.commitBatchTransaction();
//                    updateDao.update(emailQueue);
                } catch (Exception e) {
                    updateDao.beginBatchTransaction();
                    updateDao.getSession().createNativeQuery("update t_email_queue set fail_count = :failCount where email_queue_id = :emailQueueId")
                            .setParameter("failCount", emailQueue.getFail_count()+1)
                            .setParameter("emailQueueId", emailQueue.getID())
                            .executeUpdate();
                    updateDao.commitBatchTransaction();
//                    emailQueue.setFail_count(emailQueue.getFail_count()+1);
//                    updateDao.update(emailQueue);
                    new ErrLogModel().logMe(processStartDate, "EmailQueue", updateDao, e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "NormalMailJob", "NormalMailJob", "run");
        } finally {
            dao.closeSession();
            updateDao.closeSession();
        }
        Debug.printInfo(getJobCode() + " job "+jobRunEvery+"seconds runMe() is called");
    }

    public Integer firstDelay() {
        return null;
    }
}