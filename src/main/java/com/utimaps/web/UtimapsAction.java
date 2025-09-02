/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web;

import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import static com.sains.framework.base.CommonFunction.getId;
import com.sains.framework.base.Debug;
import com.sains.framework.base.LogFunction;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.AuditActionModel;
import com.utimaps.model.NotificationPModel;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

public class UtimapsAction extends BaseActionSupport<ApplicationPModel>{
    BaseDAO retrivalDAO = baseDAO;
    private CommonFunction cf = new CommonFunction();

    public static final class WF_STATUS {

        public static final String NEW = "100";
        public static final String PENDING_FOR_TA_CHECK = "101";
        public static final String TA_CHECKED_NEW_CASE = "102";
        public static final String PENDING_FOR_SS_VERIFY = "103";
        public static final String APPLICATION_FOR_USJ_APPROVED = "104";
        public static final String APPLICATION_FOR_USJ_REJECTED = "105";
        public static final String APPLICATION_SS_ROUTE_BACK = "106";
        public static final String ISSUE_LETTER_REJECTION = "107";
        public static final String ISSUE_LETTER_REJECTION_COMPLETED = "108";
        public static final String APPLICATION_PAYMENT_PENDING = "109";
        public static final String APPLICATION_PAYMENT_COMPLETED = "110";
        public static final String PREPARE_SJI_PENDING = "111";
        public static final String PREPARE_SJI_COMPLETED = "112";
        public static final String ISSUE_SJI_PENDING = "113";
        public static final String ISSUE_SJI_ROUTE_BACK = "114";
        public static final String ISSUE_SJI_COMPLETED = "115";
        public static final String APPLICATION_COMPLETED = "119";
        public static final String SUBMISSION_DRAFT = "116";
        public static final String APPLICATION_SUBMITTED = "117";
        public static final String SUBMISSION_SUBMITTED = "118";

        public static final String CHECK_U10_PENDING = "130";
        public static final String CHECK_U10_APPROVED = "131";

        public static final String VERIFY_U10_PENDING = "133";
        public static final String VERIFY_U10_ROUTE_BACK = "134";
        public static final String VERIFY_U10_APPROVED = "135";
        public static final String VERIFY_U10_QUERY = "136";

        public static final String CHECK_U20_PENDING = "140";
        public static final String CHECK_U20_APPROVED = "141";

        public static final String VERIFY_U20_PENDING = "143";
        public static final String VERIFY_U20_ROUTE_BACK = "144";
        public static final String VERIFY_U20_APPROVED = "145";
        public static final String VERIFY_U20_QUERY = "146";

        public static final String ISSUE_USCS10_PENDING = "150";
        public static final String ISSUE_USCS10_COMPLETED = "151";

        public static final String ISSUE_USCS20_PENDING = "152";
        public static final String ISSUE_USCS20_COMPLETED = "153";

        public static final String CHECK_HARDCOPY_SUBMISSION_READY = "160";
        public static final String CHECK_HARDCOPY_SUBMISSION_START = "161";
        public static final String CHECK_HARDCOPY_SUBMISSION_PENDING = "162";
        public static final String CHECK_HARDCOPY_SUBMISSION_APPROVED = "163";
        public static final String CHECK_HARDCOPY_SUBMISSION_QUERY = "164";

        public static final String PREPARE_USCS10_HARDCOPY_PENDING = "170";
        public static final String PREPARE_USCS10_HARDCOPY_COMPLETED = "171";

        public static final String ISSUE_USCS10_HARDCOPY_PENDING = "172";
        public static final String ISSUE_USCS10_HARDCOPY_ROUTE_BACK = "173";
        public static final String ISSUE_USCS10_HARDCOPY_COMPLETED = "174";

//        public static final String PREPARE_USCS30_PENDING = "180";
//        public static final String PREPARE_USCS30_COMPLETED = "181";
        
        public static final String ISSUE_USCS30_PENDING = "182";
        public static final String ISSUE_USCS30_ROUTE_BACK = "183";
        public static final String ISSUE_USCS30_COMPLETED = "184";

        public static final String ASSESS_RESUBMISSION_PENDING = "190";
        public static final String ASSESS_RESUBMISSION_COMPLETED = "191";
        public static final String ASSESS_RESUBMISSION_QUERY = "192";

        public static final String TA_CHECK_U30_PENDING = "200";
        public static final String TA_CHECK_U30_APPROVED = "201";
        public static final String TA_CHECK_U30_QUERY = "202";

        public static final String STA_CHECK_U30_PENDING = "203";
        public static final String STA_CHECK_U30_APPROVED = "204";
        public static final String STA_CHECK_U30_QUERY = "205";

        public static final String VERIFY_USCS40_PENDING = "210";
        public static final String VERIFY_USCS40_COMPLETED = "211";

        public static final String ISSUE_USCS40_PENDING = "212";
        public static final String ISSUE_USCS40_ROUTE_BACK = "213";
        public static final String ISSUE_USCS40_COMPLETED = "214";

        public static final String CHECK_U40_PENDING = "220";
        public static final String CHECK_U40_APPROVED = "221";
        public static final String CHECK_U40_QUERY = "222";

        public static final String VERIFY_U40_PENDING = "223";
        public static final String VERIFY_U40_ROUTE_BACK = "224";
        public static final String VERIFY_U40_APPROVED = "225";
        public static final String VERIFY_U40_QUERY = "226";

        public static final String VERIFY_USCS50_PENDING = "230";
        public static final String VERIFY_USCS50_COMPLETED = "231";

        public static final String ISSUE_USCS50_PENDING = "232";
        public static final String ISSUE_USCS50_ROUTE_BACK = "233";
        public static final String ISSUE_USCS50_COMPLETED = "234";

//        public static final String COMPLETE_COMPUTATION_PENDING = "240";
//        public static final String COMPLETE_COMPUTATION_COMPLETED = "241";
//
//        public static final String JOB_REGISTRATION_PENDING = "242";
//        public static final String JOB_REGISTRATION_COMPLETED = "243";
//
        public static final String ISSUE_USCS60_PENDING = "250";
        public static final String ISSUE_USCS60_COMPLETED = "251";
//
//        public static final String JOB_CHECKING_PENDING = "260";
//        public static final String JOB_CHECKING_COMPLETED = "261";
//
//        public static final String JOB_POSTING_PENDING = "262";
//        public static final String JOB_POSTING_COMPLETED = "263";

        public static final String ISSUE_USCS70_PENDING = "270";
        public static final String ISSUE_USCS70_COMPLETED = "271";

        public static final String RO_PRECHECK_PENDING = "275";
        public static final String RO_PRECHECK_PASSED = "276";
        public static final String RO_PRECHECK_FAILED = "277";

        public static final String SUBMISSION_QUERY_PRECHECK = "278";

        public static final String CHECK_U50_PENDING = "280";
        public static final String CHECK_U50_APPROVED = "281";
        public static final String CHECK_U50_QUERY = "282";

//        public static final String PREPARE_USCS80_PENDING = "290";
//        public static final String PREPARE_USCS80_ROUTE_BACK = "291";
//        public static final String PREPARE_USCS80_COMPLETED = "292";
        
        public static final String ISSUE_USCS80_PENDING = "290";
        public static final String ISSUE_USCS80_ROUTE_BACK = "291";
        public static final String ISSUE_USCS80_COMPLETED = "292";

        public static final String ASSIGN_STA_FOR_CHECKING = "300";
        public static final String ASSIGN_STA_CHECKING_COMPLETED = "301";
        
//        public static final String VERIFY_PLAN_CHECK_PENDING = "310";
//        public static final String VERIFY_PLAN_CHECK_COMPLETED = "311";
        
        public static final String CHECK_U60_PENDING = "310";
        public static final String CHECK_U60_COMPLETED = "311";
        
        public static final String CHECK_U60_SD_PENDING = "320";
        public static final String CHECK_U60_SD_ROUTE_BACK = "321";
        public static final String CHECK_U60_SD_COMPLETED = "322";
        
//        public static final String ISSUE_USCS80_STA_PENDING = "340";
//        public static final String ISSUE_USCS80_STA_COMPLETED = "341";
        
        public static final String CHECK_U60_SS_PENDING = "330";
        public static final String CHECK_U60_SS_ROUTE_BACK = "331";
        public static final String CHECK_U60_SS_QUERY = "332";
        public static final String CHECK_U60_SS_COMPLETED = "333";
        
        public static final String ISSUE_USCS80_U60_PENDING = "340";
        public static final String ISSUE_USCS80_U60_COMPLETED = "341";
        
//        public static final String SPATIAL_CHECKING_PENDING = "350";
//        public static final String SPATIAL_CHECKING_COMPLETED = "351";
//        
//        public static final String JOB_SCREENING_PENDING = "361";
//        public static final String JOB_SCREENING_ROUTE_BACK = "362";
//        public static final String JOB_SCREENING_COMPLETED = "363";
//        
//        public static final String JOB_VERIFICATION_PENDING = "370";
//        public static final String JOB_VERIFICATION_ROUTE_BACK = "371";
//        public static final String JOB_VERIFICATION_COMPLETED = "372";
//        
//        public static final String JOB_COMPLETION_CLOSING_PENDING = "380";
//        public static final String JOB_COMPLETION_CLOSING_COMPLETED = "381";
        
        public static final String ENDORSE_USP_ISSUE_USCS90_PENDING = "350";
        public static final String ENDORSE_USP_ISSUE_USCS90_COMPLETED = "351";

        public static final String COMPLETED = "500";
    }

    public static final class PB_STATUS {
        public static final String APPLICATION_SAVE = "001";
        public static final String APPLICATION_SUB = "002";
        public static final String APPLICATION_APP = "003";
        public static final String APPLICATION_QUERY = "004";
        public static final String PENDING_APP_PAYMENT = "005";
        public static final String APP_PAYMENT_COMPLETED = "006";
        public static final String ISSUANCE_PENDING = "007";
        public static final String SUBMISSION_SAVE = "008"; // shared between UAP and USJ (UAP = Application Completed, USJ = New Survey Job Submission)

//        public static final String SUBMISSION_SUB = "009";
        // SJ include pending hardcopy submission 19/08/2024
        public static final String SUBMISSION_PENDING_HARDCOPY = "009";
        public static final String SUBMISSION_SUB = "010";

        // SJ query return to PS status 27/06/2024 
        // SJ updated query status 19/08/2024
        public static final String SUBMISSION_QUERY_USCS10 = "011";
        public static final String SUBMISSION_QUERY_USCS40 = "012";
        public static final String SUBMISSION_QUERY_USCS50 = "013";
        public static final String SUBMISSION_QUERY_USCS80 = "014";
        public static final String SUBMISSION_QUERY_PRECHECK = "015";
        public static final String SUBMISSION_RESUBMIT_HARDCOPY = "016";

        public static final String SUBMISSION_SUBMITTED_NO_HC = "017";

        //SJ Completed and approved
        public static final String SUBMISSION_COMPLETED = "020";
    }

    public static final class PRECHECK_STAGE {
        public static final String NEW = "001"; // Fresh Record - No Precheck before (no log record)
        public static final String TRAV_STARTED = "002"; // Traverse Start (if fail -> NEW)
        public static final String TRAV_COMPLETE = "003"; // Traverse Complete -> Status to Proceed Call Util Precheck on Backend
        public static final String UTIL_STARTED = "004"; // Util Start (if cannot start/return error -> FAIL)
//        public static final String UTIL_COMPLETE = "005"; // Util PreCheck Done (Fail/Success)
        public static final String SUCCESS = "005"; // PreCheck Passed All
        public static final String FAIL = "006"; // PC Util or Trav fail -> Open to Restart of Precheck

        public static final String UTIL_HOLD = "031"; // Util PC in init call - in situation calling takes time, avoid double calling
        public static final String UTIL_RERUN_PENDING = "032"; // Util PC in init call - in situation calling takes time, avoid double calling
        
        public static final String QUERY_TRAV = "041"; // [USCS40]
        public static final String QUERY_TRAV_STARTED = "042"; // [USCS40]
        public static final String QUERY_TRAV_COMPLETED = "043"; // [USCS40]
        public static final String QUERY_TRAV_PASS = "045"; // [USCS40]
        public static final String QUERY_TRAV_FAIL = "046"; // [USCS40]
    
        public static final String QUERY_UTIL = "081"; // [RO Query]
        public static final String QUERY_UTIL_STARTED = "082"; // [RO Query]
        public static final String QUERY_UTIL_COMPLETE = "083"; // [RO Query]
        public static final String QUERY_UTIL_PASS = "085"; // [RO Query]
        public static final String QUERY_UTIL_FAIL = "086"; // [RO Query]
    }

    public static final class UTIL_PRECHECK_JOB_STATUS {
        public static final String SUBMITTED = "SUBMITTED";
        public static final String EXECUTING = "EXECUTING";
        public static final String SUCCESS = "SUCCEEDED";
        public static final String FAIL = "FAILED";
    }

    public static final class STATIC_FILE_ID {
        public static final String SJ_DETAILED_INSTRUCTIONS = "172474848203552X5fN0";
    }

    public static final class CL_DECISION {
        public static final String ACCEPT = "A";
        public static final String REJECT = "R";
    }

    public static final class SURVEY_JOB_TYPE {
        public static final String USJ = "USJ";
    }

    public static final class FILE_PATH {
//        public static final String EIS = "EIS";       
        public static final String USJ = "Signed";
        public static final String USJ_LETTER_SIGNED = "Signed/USJLetter";
        public static final String USJ_LETTER_STAMPED = "Stamped/USJLetter";
        public static final String USCS10_SIGNED = "Signed/USCS10";
        public static final String USCS10_STAMPED = "Stamped/USCS10";
        public static final String USCS10H_SIGNED = "Signed/USCS10H";
        public static final String USCS10H_STAMPED = "Stamped/USCS10H";
        public static final String USCS20_SIGNED = "Signed/USCS20";
        public static final String USJCS20_STAMPED = "Stamped/USCS20";
        public static final String USCS30_SIGNED = "Signed/USCS30";
        public static final String USCS30_STAMPED = "Stamped/USCS30";
        public static final String USCS40_SIGNED = "Signed/USCS40";
        public static final String USCS40_STAMPED = "Stamped/USCS40";
        public static final String USCS50_SIGNED = "Signed/USCS50";
        public static final String USCS50_STAMPED = "Stamped/USCS50";
        public static final String USCS60_SIGNED = "Signed/USCS60";
        public static final String USCS60_STAMPED = "Stamped/USCS60";
        public static final String USCS70_SIGNED = "Signed/USCS70";
        public static final String USCS70_STAMPED = "Stamped/USCS70";
        public static final String USCS80_SIGNED = "Signed/USCS80";
        public static final String USCS80_STAMPED = "Stamped/USCS80";
        public static final String USCS90_SIGNED = "Signed/USCS90";
        public static final String USCS90_STAMPED = "Stamped/USCS90";
        public static final String SSDSP_SIGNED = "Signed/SSDSP";
        public static final String SSDSP_STAMPED = "Stamped/SSDSP";
        public static final String MINUTE_SIGNED = "Signed/Minute";
        public static final String MINUTE_STAMPED = "Stamped/Minute";

        public static final String PS_DSP_SIGNED = "Signed/PSDSP";
    }

    public static final class DOC_APPLICATION {

        public static final String USJ_LETTER_SIGNED = "Signed/USJLetter";
        public static final String USJ_LETTER_STAMPED = "Stamped/USJLetter";
        public static final String USCS10_SIGNED = "USCS10_SIGNED";
        public static final String USCS10_STAMPED = "USCS10_STAMPED";
        public static final String USCS10H_SIGNED = "USCS10H_SIGNED";
        public static final String USCS10H_STAMPED = "USCS10H_STAMPED";
        public static final String USCS20_SIGNED = "USCS20_SIGNED";
        public static final String USJCS20_STAMPED = "USCS20_STAMPED";
        public static final String USCS30_SIGNED = "USCS30_SIGNED";
        public static final String USCS30_STAMPED = "USCS30_STAMPED";
        public static final String USCS40_SIGNED = "USCS40_SIGNED";
        public static final String USCS40_STAMPED = "USCS40_STAMPED";
        public static final String USCS50_SIGNED = "USCS50_SIGNED";
        public static final String USCS50_STAMPED = "USCS50_STAMPED";
        public static final String USCS60_SIGNED = "USCS60_SIGNED";
        public static final String USCS60_STAMPED = "USCS60_STAMPED";
        public static final String USCS70_SIGNED = "USCS70_SIGNED";
        public static final String USCS70_STAMPED = "USCS70_STAMPED";
        public static final String USCS80_SIGNED = "USCS80_SIGNED";
        public static final String USCS80_STAMPED = "USCS80_STAMPED";
        public static final String USCS90_SIGNED = "USCS90_SIGNED";
        public static final String USCS90_STAMPED = "USCS90_STAMPED";
        public static final String SSDSP_SIGNED = "SSDSP_SIGNED";
        public static final String SSDSP_STAMPED = "SSDSP_STAMPED";
        public static final String MINUTE_SIGNED = "MINUTE_SIGNED";
        public static final String MINUTE_STAMPED = "MINUTE_STAMPED";
        public static final String PS_DSP_SIGNED = "PS_DSP_SIGNED";

    }

    public static final class EMAIL {//email setup code in admin
        public static final String FORMAL_OFFER = "EisLcOfferLetter";
    }

    public static final class DOC_TYPE_CODE_1 {
        public static final String APP = "APP";
        public static final String USJ = "USJ";
    }
    
    public static final class NOTIF_STATUS {
        public static final String UNREAD = "N";
        public static final String READ = "R";
    }

    public static final class DOC_STATUS {
        public static final String PENDING = "P";
        public static final String CORRECT = "Y";
        public static final String INCORRECT = "N";
        public static final String SOFT_DELETED = "D"; //soft-deleted
    }

    public static final class EMAIL_CODE {
        public static final String USCS10 = "USJ_USCS10_QUERY";
        public static final String USCS20 = "USJ_USCS20_ACCEPT";
        public static final String USCS30 = "USJ_USCS30_ACCEPT";
        public static final String USCS40 = "USJ_USCS40_QUERY";
        public static final String USCS50 = "USJ_USCS50_QUERY";
        public static final String USCS60 = "USJ_USCS60_COMPLETED";
        public static final String USCS70 = "USJ_USCS70_APPROVED";
        public static final String USCS80 = "USJ_USCS80_QUERY";
        public static final String USCS90 = "USJ_USCS90_APPROVED";
        public static final String USCS90_PLANNING = "USJ_USCS90_PLANNING";
    }

    public static final class PAYMENT_METHOD {
        public static final String JVP = "Prepayment Account";
        public static final String SPY = "SPay Global";
        public static final String EBP = "ePayNow";
    }

    public static final class USER_GROUP {
        public static final String AS = "AS";
        public static final String SS = "SS";
        public static final String STA = "STA";
        public static final String TA = "TA";
        public static final String SD = "SD";
    }

    public static final class WF_STEP {
        public static final String APPLICATION = "APPLICATION";
        public static final String TRAVERSE = "TRAVERSE";
        public static final String NONTRAVERSE = "NONTRAVERSE";
    }

    public void auditAction(String caseId, Session session, String actionBy, String actionRemark) {
        new CommonFunction().auditAction(caseId, session, actionBy, actionRemark);
    }

    public void auditAction2(String caseId, Session session, String actionBy, String actionRemark) {
        new CommonFunction().auditAction2(caseId, session, actionBy, actionRemark);
    }

    public NotificationPModel insertNotification(Session session, String caseId, String noId, String sender, String recipient, String subject, String content, String systemId, String fileId, String taskId, String msgType) throws Exception {
        NotificationPModel notModel = new NotificationPModel();
        BaseDAO<NotificationPModel> basedao = new BaseDAOImpl();
//        basedao.setSession(session);
        try {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            request.setAttribute("ignoreCsrfCheck", "true");

//            basedao.beginBatchTransaction();
            notModel.defaultAddProperties();
            notModel.setMessage_id(com.sains.framework.base.CommonFunction.getId(20));
            notModel.setCase_id(caseId);
            notModel.setNo_id(noId);
            notModel.setMessage_sender(sender);
            notModel.setMessage_recipient(recipient);
            notModel.setMessage_subject(subject);
            notModel.setMessage_content(content);
            notModel.setMessage_status(NOTIF_STATUS.UNREAD);
            notModel.setMessage_status_date(DateUtil.getCurrentTimestamp());
            notModel.setSystem_id(systemId);
            notModel.setFile_id(fileId);
            notModel.setTask_id(taskId);
            notModel.setMessage_type(msgType);

//            session.save(notModel);
            getDaoService_().insertWithSession(session, notModel);
//            basedao.commitBatchTransaction();
            Debug.printDebug("notification inserted ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ " + notModel.getMessage_id());
        } catch (Exception e) {
//            basedao.rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "UtimapsAction", "UtimapsAction", "insertNotification");
        } finally {
//            basedao.closeSession();
        }

        return notModel;

    }

    public NotificationPModel insertNotification2(Session session, String caseId, String noId, String sender, String recipient, String subject, String content, String systemId, String fileId, String taskId, String msgType) throws Exception {
        NotificationPModel notModel = new NotificationPModel();
        BaseDAO<NotificationPModel> basedao = new BaseDAOImpl();
        basedao.setSession(session);
        try {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            request.setAttribute("ignoreCsrfCheck", "true");

            basedao.beginBatchTransaction();
            notModel.defaultAddProperties();
            notModel.setMessage_id(com.sains.framework.base.CommonFunction.getId(20));
            notModel.setCase_id(caseId);
            notModel.setNo_id(noId);
            notModel.setMessage_sender(sender);
            notModel.setMessage_recipient(recipient);
            notModel.setMessage_subject(subject);
            notModel.setMessage_content(content);
            notModel.setMessage_status(NOTIF_STATUS.UNREAD);
            notModel.setMessage_status_date(DateUtil.getCurrentTimestamp());
            notModel.setSystem_id(systemId);
            notModel.setFile_id(fileId);
            notModel.setTask_id(taskId);
            notModel.setMessage_type(msgType);

            getDaoService_().insertWithSession(session, notModel);
            basedao.commitBatchTransaction();
            Debug.printDebug("notification inserted ^^^^^^^^^^^^^^^^^^^^^^^^^^^^ " + notModel.getMessage_id());
        } catch (Exception e) {
            basedao.rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "UtimapsAction", "UtimapsAction", "insertNotification2");
        } finally {
            basedao.closeSession();
        }

        return notModel;

    }

    //serene @ 27/6/2024 :: for insert from backend 
    public NotificationPModel insertNotificationBackend(Session session, String caseId, String noId, String sender, String recipient, String subject, String content, String systemId, String fileId, String taskId, String msgType) throws Exception {
        NotificationPModel notModel = new NotificationPModel();
        try {
//            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//            request.setAttribute("ignoreCsrfCheck", "true");
            notModel.updateCurrentRequestObject("antiCSRF", "ignoreCSRF");
            notModel.defaultAddProperties();
            notModel.setMessage_id(com.sains.framework.base.CommonFunction.getId(20));
            notModel.setCase_id(caseId);
            notModel.setNo_id(noId);
            notModel.setMessage_sender(sender);
            notModel.setMessage_recipient(recipient);
            notModel.setMessage_subject(subject);
            notModel.setMessage_content(content);
            notModel.setMessage_status(NOTIF_STATUS.UNREAD);
            notModel.setMessage_status_date(DateUtil.getCurrentTimestamp());
            notModel.setSystem_id(systemId);
            notModel.setFile_id(fileId);
            notModel.setTask_id(taskId);
            notModel.setMessage_type(msgType);
            if(session ==null ){
                Debug.printDebug("no session found");
            }
            Debug.printDebug("session " + session);
//            getDaoService_().insertWithSession(session, notModel);
            getDaoService_().insert(notModel);
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "UtimapsAction", "UtimapsAction", "insertNotificationBackend");
        }

        return notModel;

    }
}
