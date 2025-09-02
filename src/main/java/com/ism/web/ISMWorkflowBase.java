/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ism.web;

import com.SysConf;

/**
 *
 * @author User
 */
public class ISMWorkflowBase {

    public static final class ISM_AGENCY_CODE {
        public static final String AGENCY_CODE = "24";
    }

    public static final class ISM_WF_CODE {
        public static final String USJ_APPLY = "ELASISSKID_ANUSJ";
        public static final String USJ_SUBMIT = "ELASISSKID_SUSJ";
    }

    public static final class ISM_SERVICE_ID {
        public static final String USJ_APPLY = "662";
        public static final String USJ_SUBMIT = "663";
    }

    public static final class ISM_ACTIVITY_CODE {
        public static final String USJ_APPLY = "ELASISSKID_ANUSJ_002";
        public static final String USJ_SUBMIT = "ELASISSKID_SUSJ_002";
    }

    public static final class ISM_EN_REMARKS {
        public static final String APP_PROCESSING = "Application in Processing by L&S";
        public static final String APP_QUERIED = "Application Queried to Applicant";
        public static final String SUB_PROCESSING = "Submission in Processing by L&S";
        public static final String SUB_RESUBMITTED = "Resubmitted";
        public static final String SUBMISSION_QUERY_USCS10 = "USCS10 Checklist Query";
        public static final String SUBMISSION_QUERY_USCS40 = "USCS40 Checklist Query";
        public static final String SUBMISSION_QUERY_USCS50 = "USCS50 Checklist Query";
        public static final String SUBMISSION_QUERY_USCS80 = "USCS80 Checklist Query";
        public static final String SUBMISSION_QUERY_PRECHECK = "Precheck Query";
    }
    
    public static final class ISM_WF_STEP {

        public static final String SAVE_DRAFT = "01";
        public static final String SUBMIT = "02";
        public static final String UPD_INPROGRESS = "03";
        
        public static final String UPD_RESUBMIT = "021";
        public static final String UPD_QUERIED = "031";
        
        public static final String DEL_DRAFT = "04";
        public static final String COMPLETED = "05";
        
        //[Start] Remark: This group do not involve ISM Workflow Setup.
        public static final String PYMT_PENDING = "06";
        public static final String PYMT_IN_PROGRESS = "07";
        public static final String PYMT_COMPLETED = "08";
        public static final String PYMT_REJECTED = "09";
        public static final String PYMT_CANCELLED = "10";
        public static final String PYMT_COMPLETED_JVP = "11";
        //[Ebd] Remark: This group do not involve ISM Workflow Setup.
    }

    public static final class ISM_PAYMENT {

        public static final String TOKEN = SysConf.get("ism.payment.history.token");

        //Remark: For api workflow/update_application
        public static final class PAY_STATUS {

            public static final String PENDING_EXTERNAL = "pending_ext"; //Remark: external payment will be open payment gateway in new tab such as using bank payment gateway
            public static final String PENDING_INTERNAL = "pending_int"; //Remark: internal payment will be open payment gateway in iframe, such as swkpay or open payment
            public static final String DONE = "done";
        }

        //Remark: For api workflow/payment_history_update
        public static final class HISTORY_STATUS {

            public static final String PENDING = "PENDING";
            public static final String APPROVED = "APPROVED";
            public static final String REJECTED = "REJECTED";
            public static final String CANCELLED = "CANCELLED";
        }
    }
}
