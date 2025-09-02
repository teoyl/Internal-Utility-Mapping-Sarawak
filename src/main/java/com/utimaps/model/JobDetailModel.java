/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.model;

import com.lxg.common.model.SetupCodeModel;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.ModelBase;
import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "US_JOBDETAIL")
public class JobDetailModel extends ModelBase implements java.io.Serializable {

    private String job_id;
    private String case_id;
    private String usj_job_type;
    private String usj_classification;
    private String usj_div;
    private String usj_seq;
    private String usj_year;
    private String us_so_id;
    private String land_desc;
    private java.sql.Timestamp usj_date;
    private String usj_request;
    private String usj_file_id;
    private String usj_status;
    private String usj_no;
    private String qual_level;
    private String usj_desc;
    private java.sql.Timestamp usj_submission_date;
    private java.sql.Timestamp usj_received_date;
    private java.sql.Timestamp hardcopy_received_date;
    private String ack_sub_hardcopy;
    private String ack_sub_certify;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private String wf_status;
    private String wf_status_2;
    private String case_ref;
    private String control_sv_flag;
    private java.sql.Timestamp date_issue;
    private java.sql.Timestamp approval_date;
    private String related_file_ref;
    private String requestor_branch;
    private String plan_no;
    private String precheck_stage;
    private String precheck_notif_sent = "N";
    private Double ht_datum;
    private String ht_reference;
    private java.sql.Timestamp survey_date_start;
    private java.sql.Timestamp survey_date_end;
    private java.sql.Timestamp comp_approved_date;
    private java.sql.Timestamp trav_pc_start_date;
    private java.sql.Timestamp trav_pc_completed_date;
    private String comp_completed;
    private String fim_user_id;
    private String co_id;

    private String ism_rec_id;
    private String ism_wf_started = "N";
    private String ism_wf_inprogress = "N";

    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{"job_id"};

    public String[] columnLength = new String[]{};

    public static final class OPERATION {

        public static final String PROCESS_UPDATE = "processUpdate";
        public static final String PROCESS_CHECK_CHECKLIST = "processCheckChecklist";
        public static final String PROCESS_COMPLETE = "processComplete";
        public static final String PROCESS_REJECT = "processReject";
        public static final String PROCESS_ROUTE_BACK = "processRouteBack";
        public static final String PROCESS_ROUTE_BACK_2 = "processRouteBack2";
        public static final String PROCESS_START_HARDCOPY = "processStartHardcopy";
        public static final String PROCESS_UPDATE_HARDCOPY = "processUpdateHardcopy";
        public static final String PROCESS_COMPLETE_SUBMISSION = "processCompleteSubmission";
        public static final String PROCESS_UPDATE_U20 = "processUpdateU20";
        public static final String PROCESS_UPDATE_U30 = "processUpdateU30";
        public static final String PROCESS_UPDATE_U40 = "processUpdateU40";
        public static final String PROCESS_UPDATE_U50 = "processUpdateU50";
        public static final String PROCESS_UPDATE_U60 = "processUpdateU60";
        public static final String PROCESS_QUERY_PRECHECK = "processQueryPrecheck";
        public static final String PROCESS_CLEAR_CHECKLIST = "processClearPrecheck";

        public static final String PB_UPDATE_NEW = "pbUpdateNew";
        public static final String PB_UPDATE_LIST = "pbUpdateList";
        public static final String PB_UPDATE_CONTROL = "pbUpdateControl";
        public static final String PB_UPDATE_PRECHECK_STAGE = "pbUpdatePcStage";
        public static final String PB_UPDATE_TRAV_START = "pbUpdateTravStart";
        public static final String PB_UPDATE_TRAV_COMPLETE = "pbUpdateTravComplete";
        public static final String PB_UPDATE_HC_ACK = "pbUpdateHcAck";

    }

    @Id
    @Column(name = "job_id")
    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    @Transient
    public String getID() {
        return job_id;
    }

    public void setID(String pk_id) {
        this.job_id = pk_id;
    }

    @Column(name = "case_id")
    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    @Column(name = "usj_job_type")
    public String getUsj_job_type() {
        return usj_job_type;
    }

    public void setUsj_job_type(String usj_job_type) {
        this.usj_job_type = usj_job_type;
    }

    @Column(name = "usj_classification")
    public String getUsj_classification() {
        return usj_classification;
    }

    public void setUsj_classification(String usj_classification) {
        this.usj_classification = usj_classification;
    }

    @Column(name = "usj_div")
    public String getUsj_div() {
        return usj_div;
    }

    public void setUsj_div(String usj_div) {
        this.usj_div = usj_div;
    }

    @Column(name = "usj_seq")
    public String getUsj_seq() {
        return usj_seq;
    }

    public void setUsj_seq(String usj_seq) {
        this.usj_seq = usj_seq;
    }

    @Column(name = "usj_year")
    public String getUsj_year() {
        return usj_year;
    }

    public void setUsj_year(String usj_year) {
        this.usj_year = usj_year;
    }

    @Column(name = "us_so_id")
    public String getUs_so_id() {
        return us_so_id;
    }

    public void setUs_so_id(String us_so_id) {
        this.us_so_id = us_so_id;
    }

    @Column(name = "land_desc")
    public String getLand_desc() {
        return land_desc;
    }

    public void setLand_desc(String land_desc) {
        this.land_desc = land_desc;
    }

    @Column(name = "usj_date")
    public Timestamp getUsj_date() {
        return usj_date;
    }

    public void setUsj_date(Timestamp usj_date) {
        this.usj_date = usj_date;
    }

    @Column(name = "usj_request")
    public String getUsj_request() {
        return usj_request;
    }

    public void setUsj_request(String usj_request) {
        this.usj_request = usj_request;
    }

    @Column(name = "usj_file_id")
    public String getUsj_file_id() {
        return usj_file_id;
    }

    public void setUsj_file_id(String usj_file_id) {
        this.usj_file_id = usj_file_id;
    }

    @Column(name = "usj_status")
    public String getUsj_status() {
        return usj_status;
    }

    public void setUsj_status(String usj_status) {
        this.usj_status = usj_status;
    }

    @Column(name = "usj_no")
    public String getUsj_no() {
        return usj_no;
    }

    public void setUsj_no(String usj_no) {
        this.usj_no = usj_no;
    }

    @Column(name = "qual_level")
    public String getQual_level() {
        return qual_level;
    }

    public void setQual_level(String qual_level) {
        this.qual_level = qual_level;
    }

    @Column(name = "usj_desc")
    public String getUsj_desc() {
        return usj_desc;
    }

    public void setUsj_desc(String usj_desc) {
        this.usj_desc = usj_desc;
    }

    @Column(name = "usj_submission_date")
    public Timestamp getUsj_submission_date() {
        return usj_submission_date;
    }

    public void setUsj_submission_date(Timestamp usj_submission_date) {
        this.usj_submission_date = usj_submission_date;
    }

    @Column(name = "usj_received_date")
    public Timestamp getUsj_received_date() {
        return usj_received_date;
    }

    public void setUsj_received_date(Timestamp usj_received_date) {
        this.usj_received_date = usj_received_date;
    }

    @Column(name = "hardcopy_received_date")
    public Timestamp getHardcopy_received_date() {
        return hardcopy_received_date;
    }

    public void setHardcopy_received_date(Timestamp hardcopy_received_date) {
        this.hardcopy_received_date = hardcopy_received_date;
    }

    @Column(name = "ack_sub_hardcopy")
    public String getAck_sub_hardcopy() {
        return ack_sub_hardcopy;
    }

    public void setAck_sub_hardcopy(String ack_sub_hardcopy) {
        this.ack_sub_hardcopy = ack_sub_hardcopy;
    }

    @Column(name = "ack_sub_certify")
    public String getAck_sub_certify() {
        return ack_sub_certify;
    }

    public void setAck_sub_certify(String ack_sub_certify) {
        this.ack_sub_certify = ack_sub_certify;
    }

    @Column(name = "wf_status")
    public String getWf_status() {
        return wf_status;
    }

    public void setWf_status(String wf_status) {
        this.wf_status = wf_status;
    }

    @Column(name = "wf_status_2")
    public String getWf_status_2() {
        return wf_status_2;
    }

    public void setWf_status_2(String wf_status_2) {
        this.wf_status_2 = wf_status_2;
    }

    @Column(name = "case_ref")
    public String getCase_ref() {
        return case_ref;
    }

    public void setCase_ref(String case_ref) {
        this.case_ref = case_ref;
    }

    @Column(name = "control_sv_flag")
    public String getControl_sv_flag() {
        return control_sv_flag;
    }

    public void setControl_sv_flag(String control_sv_flag) {
        this.control_sv_flag = control_sv_flag;
    }

    @Column(name = "date_issue")
    public Timestamp getDate_issue() {
        return date_issue;
    }

    public void setDate_issue(Timestamp date_issue) {
        this.date_issue = date_issue;
    }

    @Column(name = "approval_date")
    public Timestamp getApproval_date() {
        return approval_date;
    }

    public void setApproval_date(Timestamp approval_date) {
        this.approval_date = approval_date;
    }

//    private String date_issue_str;
    @Transient
    public String getDate_issue_str() {
        return Formatter.formatDate(date_issue, SystemConstants.DATE.dataEntryFormat3);
    }

    public void setDate_issue_str(String date_issue_str) {
        try {
            this.date_issue = DateUtil.getTimestampFromDate(DateUtil.getDate(date_issue_str, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "JobDetailModel", "JobDetailModel", "setDate_issue_str");
        }
    }

    @Transient
    public String getApproval_date_str() {
        return Formatter.formatDate(approval_date, SystemConstants.DATE.dataEntryFormat3);
    }

    public void setApproval_date_str(String approval_date_str) {
        try {
            this.approval_date = DateUtil.getTimestampFromDate(DateUtil.getDate(approval_date_str, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "JobDetailModel", "JobDetailModel", "setApproval_date_str");
        }
    }

    @Column(name = "related_file_ref")
    public String getRelated_file_ref() {
        return related_file_ref;
    }

    public void setRelated_file_ref(String related_file_ref) {
        this.related_file_ref = related_file_ref;
    }

    @Column(name = "requestor_branch")
    public String getRequestor_branch() {
        return requestor_branch;
    }

    public void setRequestor_branch(String requestor_branch) {
        this.requestor_branch = requestor_branch;
    }

    @Column(name = "plan_no")
    public String getPlan_no() {
        return plan_no;
    }

    public void setPlan_no(String plan_no) {
        this.plan_no = plan_no;
    }

    @Column(name = "precheck_stage")
    public String getPrecheck_stage() {
        return precheck_stage;
    }

    public void setPrecheck_stage(String precheck_stage) {
        this.precheck_stage = precheck_stage;
    }

    @Column(name = "comp_completed")
    public String getComp_completed() {
        return comp_completed;
    }

    public void setComp_completed(String comp_completed) {
        this.comp_completed = comp_completed;
    }

    @Column(name = "survey_date_start")
    public Timestamp getSurvey_date_start() {
        return survey_date_start;
    }

    public void setSurvey_date_start(Timestamp survey_date_start) {
        this.survey_date_start = survey_date_start;
    }

    @Column(name = "precheck_notif_sent")
    public String getPrecheck_notif_sent() {
        return precheck_notif_sent;
    }

    public void setPrecheck_notif_sent(String precheck_notif_sent) {
        this.precheck_notif_sent = precheck_notif_sent;
    }

    @Column(name = "survey_date_end")
    public Timestamp getSurvey_date_end() {
        return survey_date_end;
    }

    public void setSurvey_date_end(Timestamp survey_date_end) {
        this.survey_date_end = survey_date_end;
    }

    @Column(name = "comp_approved_date")
    public Timestamp getComp_approved_date() {
        return comp_approved_date;
    }

    public void setComp_approved_date(Timestamp comp_approved_date) {
        this.comp_approved_date = comp_approved_date;
    }

    @Column(name = "trav_pc_start_date")
    public Timestamp getTrav_pc_start_date() {
        return trav_pc_start_date;
    }

    public void setTrav_pc_start_date(Timestamp trav_pc_start_date) {
        this.trav_pc_start_date = trav_pc_start_date;
    }

    @Column(name = "trav_pc_completed_date")
    public Timestamp getTrav_pc_completed_date() {
        return trav_pc_completed_date;
    }

    public void setTrav_pc_completed_date(Timestamp trav_pc_completed_date) {
        this.trav_pc_completed_date = trav_pc_completed_date;
    }

    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Column(name = "created_date")
    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    @Column(name = "updated_by")
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    @Column(name = "updated_date")
    public Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Timestamp updated_date) {
        this.updated_date = updated_date;
    }

    @Column(name = "ht_datum")
    public Double getHt_datum() {
        return ht_datum;
    }

    public void setHt_datum(Double ht_datum) {
        this.ht_datum = ht_datum;
    }

    @Column(name = "fim_user_id")
    public String getFim_user_id() {
        return fim_user_id;
    }

    public void setFim_user_id(String fim_user_id) {
        this.fim_user_id = fim_user_id;
    }

    @Column(name = "co_id")
    public String getCo_id() {
        return co_id;
    }

    public void setCo_id(String co_id) {
        this.co_id = co_id;
    }

    @Column(name = "ism_rec_id")
    public String getIsm_rec_id() {
        return ism_rec_id;
    }

    public void setIsm_rec_id(String ism_rec_id) {
        this.ism_rec_id = ism_rec_id;
    }

    @Column(name = "ism_wf_started")
    public String getIsm_wf_started() {
        return ism_wf_started;
    }

    public void setIsm_wf_started(String ism_wf_started) {
        this.ism_wf_started = ism_wf_started;
    }

    @Column(name = "ism_wf_inprogress")
    public String getIsm_wf_inprogress() {
        return ism_wf_inprogress;
    }
    
    public void setIsm_wf_inprogress(String ism_wf_inprogress) {
        this.ism_wf_inprogress = ism_wf_inprogress;
    }

    @Column(name = "ht_reference")
    public String getHt_reference() {
        return ht_reference;
    }

    public void setHt_reference(String ht_reference) {
        this.ht_reference = ht_reference;
    }

    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }

    public void setUpdatableColumns(String[] updatableColumns) {
        this.updatableColumns = updatableColumns;
    }

    @Transient
    public Map getColumnLengthMap() {
        if (columnLengthMap == null) {
            columnLengthMap = new HashMap();
            String[] strA = null;

            for (String str : columnLength) {
                strA = str.split(":");
                columnLengthMap.put(strA[0], strA[1]);
            }
        }

        return columnLengthMap;
    }

    private ApplicationPModel applicationModel;

    @OneToOne(targetEntity = ApplicationPModel.class, optional = true, fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "CASE_ID", name = "CASE_ID", insertable = false, updatable = false, nullable = true)
    public ApplicationPModel getApplicationModel() {
        return applicationModel;
    }

    public void setApplicationModel(ApplicationPModel applicationModel) {
        this.applicationModel = applicationModel;
    }

    private PaymentModel jobPaymentModel;

    @OneToOne(targetEntity = PaymentModel.class, optional = true, fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "CASE_ID", name = "CASE_ID", insertable = false, updatable = false, nullable = true)
    public PaymentModel getJobPaymentModel() {
        return jobPaymentModel;
    }

    public void setJobPaymentModel(PaymentModel jobPaymentModel) {
        this.jobPaymentModel = jobPaymentModel;
    }

    private String _taskId;

    @Transient
    public String get_taskId() {
        return _taskId;
    }

    public void set_taskId(String _taskId) {
        this._taskId = _taskId;
    }

    private String application_div_display;

    @Transient
    public String getApplication_div_display() {
        CommonFunction cf = new CommonFunction();
        application_div_display = usj_div.replaceFirst("^0+(?!$)", "") + "D";
        return application_div_display;
    }

    public void setApplication_div_display(String application_div_display) {
        this.application_div_display = application_div_display;
    }

    private File cert_File = null;

    @Transient
    public File getCert_File() {
        return cert_File;
    }

    public void setCert_File(File cert_File) {
        this.cert_File = cert_File;
    }

    private String cert_Password = "";

    @Transient
    public String getCert_Password() {
        return cert_Password;
    }

    public void setCert_Password(String cert_Password) {
        this.cert_Password = cert_Password;
    }

    private List<FileModel> usjLetterList;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "job_id", name = "case_id", insertable = false, updatable = false, nullable = true)
    @Where(clause = "file_type  ='" + SystemConstants.FILE_TYPE.USJL + "'")
    public List<FileModel> getUsjLetterList() {
        return usjLetterList;
    }

    public void setUsjLetterList(List<FileModel> usjLetterList) {
        this.usjLetterList = usjLetterList;
    }

    private List<FileModel> signedUsjLetterList;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "job_id", name = "case_id", insertable = false, updatable = false, nullable = true)
    @Where(clause = "file_type  ='" + SystemConstants.FILE_TYPE.SISJL + "'")
    public List<FileModel> getSignedUsjLetterList() {
        return signedUsjLetterList;
    }

    public void setSignedUsjLetterList(List<FileModel> signedUsjLetterList) {
        this.signedUsjLetterList = signedUsjLetterList;
    }

    private List<FileModel> pbSignedDSPList;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "job_id", name = "case_id", insertable = false, updatable = false, nullable = true)
    @Where(clause = "file_type  ='" + SystemConstants.FILE_TYPE.PSDSP + "'")
    public List<FileModel> getPbSignedDSPList() {
        return pbSignedDSPList;
    }

    public void setPbSignedDSPList(List<FileModel> pbSignedDSPList) {
        this.pbSignedDSPList = pbSignedDSPList;
    }

    private ChecklistModel checklistModel;

    @OneToOne(targetEntity = ChecklistModel.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "CASE_ID", name = "CASE_ID", insertable = false, updatable = false, nullable = true)
    @Filter(name = "caseFilter", condition = "check_type = 'APP01'")
    @Transient
    public ChecklistModel getChecklistModel() {
        return checklistModel;
    }

    public void setChecklistModel(ChecklistModel checklistModel) {
        this.checklistModel = checklistModel;
    }

    private ChecklistSetupModel checklistSetupCaseModel;

    @Transient
    public ChecklistSetupModel getChecklistSetupCaseModel() {
        return checklistSetupCaseModel;
    }

    public void setChecklistSetupCaseModel(ChecklistSetupModel checklistSetupCaseModel) {
        this.checklistSetupCaseModel = checklistSetupCaseModel;
    }

    @Transient
    public String getCase_createddate_str() {
        return Formatter.formatDate(created_date, SystemConstants.DATE.dataEntryFormat3);
    }

    public void setCase_createddate_str(String case_submitteddate) {
        try {
            this.created_date = DateUtil.getTimestampFromDate(DateUtil.getDate(case_submitteddate, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "JobDetailModel", "JobDetailModel", "setCase_createddate_str");
        }
    }

    @Transient
    public String getUsj_received_date_str() {
        return Formatter.formatDate(usj_received_date, SystemConstants.DATE.dataEntryFormat3);
    }

    public void setUsj_received_date_str(String usj_received_date_str) {
        try {
            this.usj_received_date = DateUtil.getTimestampFromDate(DateUtil.getDate(usj_received_date_str, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "JobDetailModel", "JobDetailModel", "setUsj_received_date_str");
        }
    }

    @Transient
    public String getUsj_submission_date_str() {
        return Formatter.formatDate(usj_submission_date, SystemConstants.DATE.dataEntryFormat3);
    }

    public void setUsj_submission_date_str(String usj_submission_date_str) {
        try {
            this.usj_submission_date = DateUtil.getTimestampFromDate(DateUtil.getDate(usj_submission_date_str, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "JobDetailModel", "JobDetailModel", "setUsj_submission_date_str");
        }
    }

    @Transient
    public String getHardcopy_received_date_str() {
        return Formatter.formatDate(hardcopy_received_date, SystemConstants.DATE.dataEntryFormat3);
    }

    public void setHardcopy_received_date_str(String hardcopy_received_date_str) {
        try {
            this.hardcopy_received_date = DateUtil.getTimestampFromDate(DateUtil.getDate(hardcopy_received_date_str, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "JobDetailModel", "JobDetailModel", "setHardcopy_received_date_str");
        }
    }

    @Transient
    public String getSurvey_date_start_str() {
        return Formatter.formatDate(survey_date_start, SystemConstants.DATE.dataEntryFormat);
    }

    public void setSurvey_date_start_str(String survey_date_start) {
        try {
            this.survey_date_start = DateUtil.getTimestampFromDate(DateUtil.getDate(survey_date_start, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "JobDetailModel", "JobDetailModel", "setSurvey_date_start_str");
        }
    }

    @Transient
    public String getSurvey_date_end_str() {
        return Formatter.formatDate(survey_date_end, SystemConstants.DATE.dataEntryFormat);
    }

    public void setSurvey_date_end_str(String survey_date_end) {
        try {
            this.survey_date_end = DateUtil.getTimestampFromDate(DateUtil.getDate(survey_date_end, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "JobDetailModel", "JobDetailModel", "setSurvey_date_end_str");
        }
    }

    @Transient
    public String getComp_approved_date_str() {
        return Formatter.formatDate(comp_approved_date, SystemConstants.DATE.dataEntryFormat3);
    }

    public void setComp_approved_date_str(String comp_approved_date) {
        try {
            this.comp_approved_date = DateUtil.getTimestampFromDate(DateUtil.getDate(comp_approved_date, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "JobDetailModel", "JobDetailModel", "setComp_approved_date_str");
        }
    }

    @Transient
    public String getSurvey_date_str() {
        return getSurvey_date_start_str() + " - " + getSurvey_date_end_str();
    }

    private String division_name = "";

    @Transient
    public String getDivision_name() {

        CommonFunction cf = new CommonFunction();
        division_name = cf.getPubcodeDesc("DIV", usj_div);

        return division_name;
    }

    private ChecklistModel u10ChecklistModel;
    private ChecklistModel u20ChecklistModel;
    private ChecklistModel u21ChecklistModel;
    private ChecklistModel u30ChecklistModel;
    private ChecklistModel u40ChecklistModel;
    private ChecklistModel u50ChecklistModel;
    private ChecklistModel u60ChecklistModel;
    private ChecklistModel hardChecklistModel;

    @Transient
    public ChecklistModel getU10ChecklistModel() {
        return u10ChecklistModel;
    }

    public void setU10ChecklistModel(ChecklistModel u10ChecklistModel) {
        this.u10ChecklistModel = u10ChecklistModel;
    }

    @Transient
    public ChecklistModel getU20ChecklistModel() {
        return u20ChecklistModel;
    }

    public void setU20ChecklistModel(ChecklistModel u20ChecklistModel) {
        this.u20ChecklistModel = u20ChecklistModel;
    }

    @Transient
    public ChecklistModel getU30ChecklistModel() {
        return u30ChecklistModel;
    }

    public void setU30ChecklistModel(ChecklistModel u30ChecklistModel) {
        this.u30ChecklistModel = u30ChecklistModel;
    }

    @Transient
    public ChecklistModel getU40ChecklistModel() {
        return u40ChecklistModel;
    }

    public void setU40ChecklistModel(ChecklistModel u40ChecklistModel) {
        this.u40ChecklistModel = u40ChecklistModel;
    }

    @Transient
    public ChecklistModel getU50ChecklistModel() {
        return u50ChecklistModel;
    }

    public void setU50ChecklistModel(ChecklistModel u50ChecklistModel) {
        this.u50ChecklistModel = u50ChecklistModel;
    }

    @Transient
    public ChecklistModel getU60ChecklistModel() {
        return u60ChecklistModel;
    }

    public void setU60ChecklistModel(ChecklistModel u60ChecklistModel) {
        this.u60ChecklistModel = u60ChecklistModel;
    }

    @Transient
    public ChecklistModel getU21ChecklistModel() {
        return u21ChecklistModel;
    }

    public void setU21ChecklistModel(ChecklistModel u21ChecklistModel) {
        this.u21ChecklistModel = u21ChecklistModel;
    }

//    @Transient
//    public ChecklistModel getHardChecklistModel() {
//        return hardChecklistModel;
//    }
//
//    public void setHardChecklistModel(ChecklistModel hardChecklistModel) {
//        this.hardChecklistModel = hardChecklistModel;
//    }
    private ChecklistSetupModel checklistSetupModel;
    private ChecklistSetupModel u10ChecklistSetupModel;
    private ChecklistSetupModel u11ChecklistSetupModel;
    private ChecklistSetupModel u20ChecklistSetupModel;
    private ChecklistSetupModel u21ChecklistSetupModel;
    private ChecklistSetupModel u30ChecklistSetupModel;
    private ChecklistSetupModel u40ChecklistSetupModel;
    private ChecklistSetupModel u50ChecklistSetupModel;
    private ChecklistSetupModel u60ChecklistSetupModel;
//    private ChecklistSetupModel hardChecklistSetupModel;

    @Transient
    public ChecklistSetupModel getChecklistSetupModel() {
        return checklistSetupModel;
    }

    public void setChecklistSetupModel(ChecklistSetupModel checklistSetupModel) {
        this.checklistSetupModel = checklistSetupModel;
    }

    @Transient
    public ChecklistSetupModel getU10ChecklistSetupModel() {
        return u10ChecklistSetupModel;
    }

    public void setU10ChecklistSetupModel(ChecklistSetupModel u10ChecklistSetupModel) {
        this.u10ChecklistSetupModel = u10ChecklistSetupModel;
    }

    @Transient
    public ChecklistSetupModel getU11ChecklistSetupModel() {
        return u11ChecklistSetupModel;
    }

    public void setU11ChecklistSetupModel(ChecklistSetupModel u11ChecklistSetupModel) {
        this.u11ChecklistSetupModel = u11ChecklistSetupModel;
    }

    @Transient
    public ChecklistSetupModel getU20ChecklistSetupModel() {
        return u20ChecklistSetupModel;
    }

    public void setU20ChecklistSetupModel(ChecklistSetupModel u20ChecklistSetupModel) {
        this.u20ChecklistSetupModel = u20ChecklistSetupModel;
    }

    @Transient
    public ChecklistSetupModel getU21ChecklistSetupModel() {
        return u21ChecklistSetupModel;
    }

    public void setU21ChecklistSetupModel(ChecklistSetupModel u21ChecklistSetupModel) {
        this.u21ChecklistSetupModel = u21ChecklistSetupModel;
    }

    @Transient
    public ChecklistSetupModel getU30ChecklistSetupModel() {
        return u30ChecklistSetupModel;
    }

    public void setU30ChecklistSetupModel(ChecklistSetupModel u30ChecklistSetupModel) {
        this.u30ChecklistSetupModel = u30ChecklistSetupModel;
    }

    @Transient
    public ChecklistSetupModel getU40ChecklistSetupModel() {
        return u40ChecklistSetupModel;
    }

    public void setU40ChecklistSetupModel(ChecklistSetupModel u40ChecklistSetupModel) {
        this.u40ChecklistSetupModel = u40ChecklistSetupModel;
    }

    @Transient
    public ChecklistSetupModel getU50ChecklistSetupModel() {
        return u50ChecklistSetupModel;
    }

    public void setU50ChecklistSetupModel(ChecklistSetupModel u50ChecklistSetupModel) {
        this.u50ChecklistSetupModel = u50ChecklistSetupModel;
    }

    @Transient
    public ChecklistSetupModel getU60ChecklistSetupModel() {
        return u60ChecklistSetupModel;
    }

    public void setU60ChecklistSetupModel(ChecklistSetupModel u60ChecklistSetupModel) {
        this.u60ChecklistSetupModel = u60ChecklistSetupModel;
    }

//    @Transient
//    public ChecklistSetupModel getHardChecklistSetupModel() {
//        return hardChecklistSetupModel;
//    }
//
//    public void setHardChecklistSetupModel(ChecklistSetupModel hardChecklistSetupModel) {
//        this.hardChecklistSetupModel = hardChecklistSetupModel;
//    }
    private String process_type;

    @Transient
    public String getProcess_type() {
        return process_type;
    }

    public void setProcess_type(String process_type) {
        this.process_type = process_type;
    }

    private String check_status;

    @Transient
    public String getCheck_status() {
        return check_status;
    }

    public void setCheck_status(String check_status) {
        this.check_status = check_status;
    }

    private List<FileModel> DSD;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='DSD'")

    public List<FileModel> getDSD() {
        return DSD;
    }

    public void setDSD(List<FileModel> DSD) {
        this.DSD = DSD;
    }

    private List<FileModel> CPU;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='CPU'")

    public List<FileModel> getCPU() {
        return CPU;
    }

    public void setCPU(List<FileModel> CPU) {
        this.CPU = CPU;
    }

    private List<FileModel> RPO;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='RPO'")

    public List<FileModel> getRPO() {
        return RPO;
    }

    public void setRPO(List<FileModel> RPO) {
        this.RPO = RPO;
    }

    private List<FileModel> RPN;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='RPN'")

    public List<FileModel> getRPN() {
        return RPN;
    }

    public void setRPN(List<FileModel> RPN) {
        this.RPN = RPN;
    }

    private List<FileModel> RPR;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='RPR'")

    public List<FileModel> getRPR() {
        return RPR;
    }

    public void setRPR(List<FileModel> RPR) {
        this.RPR = RPR;
    }

    private List<FileModel> RPG;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='RPG'")

    public List<FileModel> getRPG() {
        return RPG;
    }

    public void setRPG(List<FileModel> RPG) {
        this.RPG = RPG;
    }

    private List<FileModel> ERR;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='ERR'")

    public List<FileModel> getERR() {
        return ERR;
    }

    public void setERR(List<FileModel> ERR) {
        this.ERR = ERR;
    }

    private List<FileModel> STA;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='STA'")

    public List<FileModel> getSTA() {
        return STA;
    }

    public void setSTA(List<FileModel> STA) {
        this.STA = STA;
    }

    private List<FileModel> RDL;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='RDL'")

    public List<FileModel> getRDL() {
        return RDL;
    }

    public void setRDL(List<FileModel> RDL) {
        this.RDL = RDL;
    }

    private List<FileModel> RTS;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='RTS'")

    public List<FileModel> getRTS() {
        return RTS;
    }

    public void setRTS(List<FileModel> RTS) {
        this.RTS = RTS;
    }

    private List<FileModel> RTK;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='RTK'")

    public List<FileModel> getRTK() {
        return RTK;
    }

    public void setRTK(List<FileModel> RTK) {
        this.RTK = RTK;
    }

    private List<FileModel> RIN;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='RIN'")

    public List<FileModel> getRIN() {
        return RIN;
    }

    public void setRIN(List<FileModel> RIN) {
        this.RIN = RIN;
    }

    private List<FileModel> GPR;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='GPR'")

    public List<FileModel> getGPR() {
        return GPR;
    }

    public void setGPR(List<FileModel> GPR) {
        this.GPR = GPR;
    }

    private List<FileModel> SPH;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='SPH'")

    public List<FileModel> getSPH() {
        return SPH;
    }

    public void setSPH(List<FileModel> SPH) {
        this.SPH = SPH;
    }

    private List<FileModel> CLR;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='CLR'")

    public List<FileModel> getCLR() {
        return CLR;
    }

    public void setCLR(List<FileModel> CLR) {
        this.CLR = CLR;
    }

    private List<FileModel> SVR;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='SVR'")

    public List<FileModel> getSVR() {
        return SVR;
    }

    public void setSVR(List<FileModel> SVR) {
        this.SVR = SVR;
    }

    private List<FileModel> FLB;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='FLB'")

    public List<FileModel> getFLB() {
        return FLB;
    }

    public void setFLB(List<FileModel> FLB) {
        this.FLB = FLB;
    }

    private List<FileModel> SJI;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='SJI' and rownum=1")

    public List<FileModel> getSJI() {
        return SJI;
    }

    public void setSJI(List<FileModel> SJI) {
        this.SJI = SJI;
    }

    private List<FileModel> SISJL;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='SISJL' and rownum=1")

    public List<FileModel> getSISJL() {
        return SISJL;
    }

    public void setSISJL(List<FileModel> SISJL) {
        this.SISJL = SISJL;
    }

    private List<FileModel> EDM;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='EDM'")

    public List<FileModel> getEDM() {
        return EDM;
    }

    public void setEDM(List<FileModel> EDM) {
        this.EDM = EDM;
    }

    private List<FileModel> WOP;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='WOP'")
    public List<FileModel> getWOP() {
        return WOP;
    }

    public void setWOP(List<FileModel> WOP) {
        this.WOP = WOP;
    }

    private List<FileModel> FBL;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='FBL'")
    public List<FileModel> getFBL() {
        return FBL;
    }

    public void setFBL(List<FileModel> FBL) {
        this.FBL = FBL;
    }

    private List<FileModel> FBS;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='FBS'")
    public List<FileModel> getFBS() {
        return FBS;
    }

    public void setFBS(List<FileModel> FBS) {
        this.FBS = FBS;
    }

    private List<FileModel> FBH;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='FBH' and rownum=1")
    public List<FileModel> getFBH() {
        return FBH;
    }

    public void setFBH(List<FileModel> FBH) {
        this.FBH = FBH;
    }

    private List<FileModel> SRE;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='SRE'")
    public List<FileModel> getSRE() {
        return SRE;
    }

    public void setSRE(List<FileModel> SRE) {
        this.SRE = SRE;
    }

    private List<FileModel> PS3;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='PS3'")
    public List<FileModel> getPS3() {
        return PS3;
    }

    public void setPS3(List<FileModel> PS3) {
        this.PS3 = PS3;
    }

    private List<FileModel> DS6;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='DS6'")
    public List<FileModel> getDS6() {
        return DS6;
    }

    public void setDS6(List<FileModel> DS6) {
        this.DS6 = DS6;
    }

    private List<FileModel> RSO;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='RSO'")
    public List<FileModel> getRSO() {
        return RSO;
    }

    public void setRSO(List<FileModel> RSO) {
        this.RSO = RSO;
    }

    private List<FileModel> CPR;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='CPR'")
    public List<FileModel> getCPR() {
        return CPR;
    }

    public void setCPR(List<FileModel> CPR) {
        this.CPR = CPR;
    }

    private List<FileModel> PFR;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='PFR'")
    public List<FileModel> getPFR() {
        return PFR;
    }

    public void setPFR(List<FileModel> PFR) {
        this.PFR = PFR;
    }

    private List<FileModel> SMS;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='SMS'")
    public List<FileModel> getSMS() {
        return SMS;
    }

    public void setSMS(List<FileModel> SMS) {
        this.SMS = SMS;
    }

    private List<FileModel> DSP;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type ='DSP'")
    @OrderBy("CREATED_DATE DESC")
    public List<FileModel> getDSP() {
        return DSP;
    }

    public void setDSP(List<FileModel> DSP) {
        this.DSP = DSP;
    }

    private List<JobStatusModel> jobHistoryList;

    @OneToMany(targetEntity = JobStatusModel.class, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "job_id", name = "case_id", insertable = false, updatable = false, nullable = true)
    @OrderBy("STATUS_SEQ DESC")
    public List<JobStatusModel> getJobHistoryList() {
        return jobHistoryList;
    }

    public void setJobHistoryList(List<JobStatusModel> jobHistoryList) {
        this.jobHistoryList = jobHistoryList;
    }

    private List<FileModel> signedPSDSPList;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "job_id", name = "case_id", insertable = false, updatable = false, nullable = true)
    @Where(clause = "file_type  ='PSDSP'")

    public List<FileModel> getSignedPSDSPList() {
        return signedPSDSPList;
    }

    public void setSignedPSDSPList(List<FileModel> signedPSDSPList) {
        this.signedPSDSPList = signedPSDSPList;
    }

    private List<FileModel> signedSSDSPList;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "job_id", name = "case_id", insertable = false, updatable = false, nullable = true)
    @Where(clause = "file_type  ='SSDSP' and description = 'SSDSP_SIGNED'")

    public List<FileModel> getSignedSSDSPList() {
        return signedSSDSPList;
    }

    public void setSignedSSDSPList(List<FileModel> signedSSDSPList) {
        this.signedSSDSPList = signedSSDSPList;
    }

    private String qual_level_str;

    @Transient
    public String getQual_level_str() {
        CommonFunction cf = new CommonFunction();
        qual_level_str = cf.getPubcodeDesc("UST", qual_level);

        return qual_level_str;
    }

    public void setQual_level_str(String qual_level_str) {
        this.qual_level_str = qual_level_str;
    }

    @Transient
    public String getWf_status_2_str() {
        BaseDAOImpl retrivalDAO = new BaseDAOImpl();
        String status_code_str = "";
        try {
            SetupCodeModel codeModel = (SetupCodeModel) retrivalDAO.getModelByCode("code_type,code_1", "JWS," + this.wf_status_2, new SetupCodeModel());
            if (codeModel != null) {
                status_code_str = codeModel.getCode_desc();
            } else {
                status_code_str = "-";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retrivalDAO.closeSession();
        }
        return status_code_str;
    }

    private String _assignTo;

    @Transient
    public String getAssignTo() {
        return _assignTo;
    }

    public void setAssignTo(String _assignTo) {
        this._assignTo = _assignTo;
    }

}
