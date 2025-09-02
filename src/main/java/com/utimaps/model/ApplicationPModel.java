/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.model;

import com.lxg.common.model.PublicUserModel;
import com.lxg.common.model.SetupCodeModel;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.common.util.FileOperationUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.FtpInterface;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.EmailTrigger;
import com.sains.framework.base.LnsBaseDAOImpl;
import com.sains.framework.base.ModelBase;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.model.User;
import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import com.sains.workflow.web.WorkflowApiAction;
import com.utimaps.web.SurveyJobAction;
import com.utimaps.web.UtimapsAction;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
//import org.apache.commons.lang.WordUtils;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.Session;
import org.hibernate.annotations.Where;

@Entity
@FilterDefs({
    @FilterDef(name = "caseFilter")
    ,
    @FilterDef(name = "fileFilter")
})
@Table(name = "US_APPLICATION_P")
public class ApplicationPModel extends ModelBase implements java.io.Serializable {

    private String case_id;
    private String job_id;
    private String case_type;
    private String case_div;
    private String case_seq;
    private String case_year;
    private String usj_div;
    private String usj_seq;
    private String usj_year;
    private String pj_div;
    private String pj_name;
    private String land_dist;
    private String land_desc;
    private String client_name;
    private String app_acknowledge;
    private String app_submit_by;
    private java.sql.Timestamp app_submit_date;
    private String app_status;
    private String wf_status;
    private String case_ref;
    private String usj_no;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private String qual_level;
    private String fim_user_id;
    private String co_id;
    private String usj_providers;
    
    // batch 2 enhancement - in house application
    private String internal_case;
    private String lns_in_house_flag;

    private String ism_rec_id;
    private String ism_wf_started = "N";
    private String ism_wf_inprogress = "N";
    
    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{"Case_id", "Job_id", "Case_type", "Case_div", "Case_seq", "Case_year", "Usj_div", "Usj_seq", "Usj_year", "Pj_div", "Pj_name", "Land_dist", "Land_desc", "Client_name", "App_acknowledge", "qual_level", "App_submit_by", "App_submit_date", "App_status", "usj_providers", "internal_case", "created_by", "created_date", "updated_by", "updated_date", "ism_rec_id", "ism_wf_started", "ism_wf_inprogress"};

    public String[] columnLength = new String[]{"case_id:20:", "job_id:20:", "case_type:20:", "case_div:2", "case_seq:6", "case_year:4", "usj_div:2", "usj_seq:4", "usj_year:4", "pj_div:2", "pj_name:10000", "land_dist:3", "land_desc:500", "client_name:500", "app_acknowledge:1", "qual_level:20", "app_submit_by:20", "app_status:20", "created_by:20", "updated_by:20", "usj_providers:200"};

    public static final class OPERATION {

        public static final String PROCESS_DELETE_LIST = "processDeleteList";
        public static final String PROCESS_UPDATE = "processUpdate";
        public static final String PROCESS_CHECK_CHECKLIST = "processCheckChecklist";
        public static final String UPDATE_STATUS = "processUpdateStatus";
        public static final String SAVE_CHECKLIST = "saveChecklist";
        public static final String UPLOAD_SUPPORTING_FILE = "uploadSupportingFile";
        public static final String UPDATE_UPLOAD_SUPPORTING_FILE = "updateUploadSupportingFile";
        public static final String PROCESS_COMPLETE = "processComplete";
        public static final String PROCESS_REJECT = "processReject";
        public static final String PROCESS_ROUTE_BACK_APPLICATION = "processRouteBackApplication";
        public static final String PROCESS_UPDATE_ISSUANCE = "processUpdateIssuance";
        public static final String PROCESS_ROUTE_BACK_ISSUANCE = "processRouteBackIssuance";
        public static final String PROCESS_COMPLETE_ISSUANCE = "processCompleteIssuance";
        
        public static final String DELETE_UPS10_FILE = "deleteUPS10File";
        public static final String DELETE_USJ_FILE = "deleteUSJFile";
        public static final String SIGN_USJ_LETTER = "signUSJLetter";
        public static final String REVOKE_USJ_LETTER = "revokeUSJLetter";
        public static final String INSERT_SIGNED_USJ = "insertSignedUSJLetter";
        public static final String DELETE_USCS_LETTER = "deleteUSCSLetter";
        public static final String SIGN_USCS_LETTER = "signUSCSLetter";
        public static final String INSERT_SIGNED_USCS = "insertSignedUSCSLetter";
        public static final String REVOKE_USCS_LETTER = "revokeUSCSLetter";
    }

    @Id
    @Column(name = "case_id")
    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    @Transient
    public String getID() {
        return case_id;
    }

    public void setID(String pk_id) {
        this.case_id = pk_id;
    }

    @Column(name = "job_id")
    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    @Column(name = "case_type")
    public String getCase_type() {
        return case_type;
    }

    public void setCase_type(String case_type) {
        this.case_type = case_type;
    }

    @Column(name = "case_div")
    public String getCase_div() {
        return case_div;
    }

    public void setCase_div(String case_div) {
        this.case_div = case_div;
    }

    @Column(name = "case_seq")
    public String getCase_seq() {
        return case_seq;
    }

    public void setCase_seq(String case_seq) {
        this.case_seq = case_seq;
    }

    @Column(name = "case_year")
    public String getCase_year() {
        return case_year;
    }

    public void setCase_year(String case_year) {
        this.case_year = case_year;
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

    @Column(name = "pj_div")
    public String getPj_div() {
        return pj_div;
    }

    public void setPj_div(String pj_div) {
        this.pj_div = pj_div;
    }

    @Column(name = "pj_name")
    public String getPj_name() {
        return pj_name;
    }

    public void setPj_name(String pj_name) {
        this.pj_name = pj_name;
    }

    @Column(name = "land_dist")
    public String getLand_dist() {
        return land_dist;
    }

    public void setLand_dist(String land_dist) {
        this.land_dist = land_dist;
    }

    @Column(name = "land_desc")
    public String getLand_desc() {
        return land_desc;
    }

    public void setLand_desc(String land_desc) {
        this.land_desc = land_desc;
    }

    @Column(name = "client_name")
    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    @Column(name = "app_acknowledge")
    public String getApp_acknowledge() {
        return app_acknowledge;
    }

    public void setApp_acknowledge(String app_acknowledge) {
        this.app_acknowledge = app_acknowledge;
    }

    @Column(name = "app_submit_by")
    public String getApp_submit_by() {
        return app_submit_by;
    }

    public void setApp_submit_by(String app_submit_by) {
        this.app_submit_by = app_submit_by;
    }

    @Column(name = "app_submit_date")
    public Timestamp getApp_submit_date() {
        return app_submit_date;
    }

    public void setApp_submit_date(Timestamp app_submit_date) {
        this.app_submit_date = app_submit_date;
    }

    @Column(name = "app_status")
    public String getApp_status() {
        return app_status;
    }

    public void setApp_status(String app_status) {
        this.app_status = app_status;
    }

    @Column(name = "wf_status")
    public String getWf_status() {
        return wf_status;
    }

    public void setWf_status(String wf_status) {
        this.wf_status = wf_status;
    }

    @Column(name = "case_ref")
    public String getCase_ref() {
        return case_ref;
    }

    public void setCase_ref(String case_ref) {
        this.case_ref = case_ref;
    }

    @Column(name = "usj_no")
    public String getUsj_no() {
        return usj_no;
    }

    public void setUsj_no(String usj_no) {
        this.usj_no = usj_no;
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

    @Transient
    public String[] getColumnLength() {
        return columnLength;
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
    
    private List<ProcessingHistoryModel> processingHistory ;

    @OneToMany(targetEntity = ProcessingHistoryModel.class, fetch = FetchType.LAZY, mappedBy = "case_id")
    @OrderBy("process_date ASC")
    
    public List<ProcessingHistoryModel> getProcessingHistory() {
        return processingHistory;
    }

    public void setProcessingHistory(List<ProcessingHistoryModel> processingHistory) {
        this.processingHistory = processingHistory;
    }
    
    
    private List<FileModel> LOA;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY, mappedBy = "case_id")
    @Where(clause = "file_type  ='LOA'")

    public List<FileModel> getLOA() {
        return LOA;
    }

    public void setLOA(List<FileModel> LOA) {
        this.LOA = LOA;
    }

    private List<FileModel> LOC;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY, mappedBy = "case_id")
    @Where(clause = "file_type  ='LOC'")

    public List<FileModel> getLOC() {
        return LOC;
    }

    public void setLOC(List<FileModel> LOC) {
        this.LOC = LOC;
    }

    private List<FileModel> PUP;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY, mappedBy = "case_id")
    @Where(clause = "file_type  ='PUP'")

    public List<FileModel> getPUP() {
        return PUP;
    }

    public void setPUP(List<FileModel> PUP) {
        this.PUP = PUP;
    }

//    private List<FileModel> LSA;
//
//    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY, mappedBy = "case_id")
//    @Where(clause = "file_type  ='LSA' and rownum=1")
//
//    public List<FileModel> getLSA() {
//        return LSA;
//    }
//
//    public void setLSA(List<FileModel> LSA) {
//        this.LSA = LSA;
//    }
    private List<FileModel> SUP;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY, mappedBy = "case_id")
    @Where(clause = "file_type  ='SUP'")

    public List<FileModel> getSUP() {
        return SUP;
    }

    public void setSUP(List<FileModel> SUP) {
        this.SUP = SUP;
    }

//    private List<FileModel> CCQ;
//
//    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY, mappedBy = "case_id")
//    @Where(clause = "file_type  = 'CCQ' and rownum=1")
//
//    public List<FileModel> getCCQ() {
//        return CCQ;
//    }
//
//    public void setCCQ(List<FileModel> CCQ) {
//        this.CCQ = CCQ;
//    }
    private List<FileModel> POT;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY, mappedBy = "case_id")
    @Where(clause = "file_type  = 'POT'")

    public List<FileModel> getPOT() {
        return POT;
    }

    public void setPOT(List<FileModel> POT) {
        this.POT = POT;
    }

    private List<FileModel> LAW;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY, mappedBy = "case_id")
    @Where(clause = "file_type  = 'LAW'")

    public List<FileModel> getLAW() {
        return LAW;
    }

    public void setLAW(List<FileModel> LAW) {
        this.LAW = LAW;
    }

    private List<FileModel> DSP;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type ='DSP'")

    public List<FileModel> getDSP() {
        return DSP;
    }

    public void setDSP(List<FileModel> DSP) {
        this.DSP = DSP;
    }
    
    private List<FileModel> USJL;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type ='USJL'")

    public List<FileModel> getUSJL() {
        return USJL;
    }

    public void setUSJL(List<FileModel> USJL) {
        this.USJL = USJL;
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
    
    private List<FileModel> ONS;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @Where(clause = "file_type  ='ONS'")
    public List<FileModel> getONS() {
        return ONS;
    }

    public void setONS(List<FileModel> ONS) {
        this.ONS = ONS;
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

    @Transient
    public String getFormatedRef() {

        String refno = "";
        String caseYear = case_year;
        String caseSeq = case_seq;

        refno = "UAP/" + caseSeq + "/" + caseYear;

        return refno;
    }

    private String division_name = "";

    @Transient
    public String getDivision_name() {

        CommonFunction cf = new CommonFunction();
        division_name = cf.getPubcodeDesc("DIV", case_div);

        return division_name;
    }

    @Transient
    public String getCase_createddate_str() {
        return Formatter.formatDate(created_date, SystemConstants.DATE.dataEntryFormat3);
    }

    public void setCase_createddate_str(String case_submitteddate) {
        try {
            this.created_date = DateUtil.getTimestampFromDate(DateUtil.getDate(case_submitteddate, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "ApplicationPModel", "ApplicationPModel", "setCase_createddate_str");
        }
    }
    
    @Transient
    public String getApp_submit_date_str() {
        return Formatter.formatDate(app_submit_date, SystemConstants.DATE.dataEntryFormat3);
    }

    public void setApp_submit_date(String app_submit_date) {
        try {
            this.app_submit_date = DateUtil.getTimestampFromDate(DateUtil.getDate(app_submit_date, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "ApplicationPModel", "ApplicationPModel", "setApp_submit_date");
        }
    }

    private SurveyFirmPModel surveyFirmModel;

    @OneToOne(targetEntity = SurveyFirmPModel.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "CASE_ID", name = "CASE_ID", insertable = false, updatable = false, nullable = true)
    public SurveyFirmPModel getSurveyFirmModel() {
        return surveyFirmModel;
    }

    public void setSurveyFirmModel(SurveyFirmPModel surveyFirmModel) {
        this.surveyFirmModel = surveyFirmModel;
    }

    private ChecklistModel checklistModel;
    private ChecklistSetupModel checklistSetupCaseModel;

    @OneToOne(targetEntity = ChecklistModel.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "CASE_ID", name = "CASE_ID", insertable = false, updatable = false, nullable = true)
    @Filter(name = "caseFilter", condition = "check_type = :checkType")
//    @Transient
    public ChecklistModel getChecklistModel() {
        return checklistModel;
    }

    public void setChecklistModel(ChecklistModel checklistModel) {
        this.checklistModel = checklistModel;
    }

    @Transient
    public ChecklistSetupModel getChecklistSetupCaseModel() {
        return checklistSetupCaseModel;
    }

    public void setChecklistSetupCaseModel(ChecklistSetupModel checklistSetupCaseModel) {
        this.checklistSetupCaseModel = checklistSetupCaseModel;
    }

    public void enableFilterSub(org.hibernate.Session session) {
        session.enableFilter("caseFilter");
    }

    private SurveyFirmPModel appFirmPModel;

    @OneToOne(targetEntity = SurveyFirmPModel.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "case_id", name = "case_id", insertable = false, updatable = false, nullable = false)
    public SurveyFirmPModel getAppFirmPModel() {
        return appFirmPModel;
    }

    public void setAppFirmPModel(SurveyFirmPModel appFirmPModel) {
        this.appFirmPModel = appFirmPModel;
    }

    private String _taskId;

    @Transient
    public String get_taskId() {
        return _taskId;
    }

    public void set_taskId(String _taskId) {
        this._taskId = _taskId;
    }

    private List<PaymentModel> paymentModelList = new ArrayList();

    @OneToMany(targetEntity = PaymentModel.class, fetch = FetchType.LAZY, mappedBy = "case_id")
    public List<PaymentModel> getPaymentModelList() {
        return paymentModelList;
    }

    public void setPaymentModelList(List<PaymentModel> paymentModelList) {
        this.paymentModelList = paymentModelList;
    }

    private PaymentModel paymentModel;

    @OneToOne(targetEntity = PaymentModel.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "CASE_ID", name = "CASE_ID", insertable = false, updatable = false, nullable = false)
    public PaymentModel getPaymentModel() {
        return paymentModel;
    }

    public void setPaymentModel(PaymentModel paymentModel) {
        this.paymentModel = paymentModel;
    }

    private List<FileModel> UPS10List;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY, mappedBy = "case_id")
//    @Filter(name="fileFilter", condition="file_type = 'UPS10'")
    @Where(clause = "file_type  ='" + SystemConstants.FILE_TYPE.UPS10 + "'")
    @OrderBy("created_date DESC")
    public List<FileModel> getUPS10List() {
        return UPS10List;
    }

    public void setUPS10List(List<FileModel> UPS10List) {
        this.UPS10List = UPS10List;
    }

    private List<AppLocalityModel> adminDistrictList = new ArrayList();

    @OneToMany(targetEntity = AppLocalityModel.class, fetch = FetchType.LAZY, mappedBy = "case_id")
    @Where(clause = "ADMIN_DIST IS NOT NULL")
    public List<AppLocalityModel> getAdminDistrictList() {
        return adminDistrictList;
    }

    public void setAdminDistrictList(List<AppLocalityModel> adminDistrictList) {
        this.adminDistrictList = adminDistrictList;
    }

    private List<AppLocalityModel> subDistrictList = new ArrayList();

    @OneToMany(targetEntity = AppLocalityModel.class, fetch = FetchType.LAZY, mappedBy = "case_id")
    @Where(clause = "SUB_DIST IS NOT NULL")
    public List<AppLocalityModel> getSubDistrictList() {
        return subDistrictList;
    }

    public void setSubDistrictList(List<AppLocalityModel> subDistrictList) {
        this.subDistrictList = subDistrictList;
    }

//    private PublicUserModel applicantPublicUser;
//
//    @OneToOne(targetEntity = PublicUserModel.class, optional = true, fetch = FetchType.LAZY)
//    @JoinColumn(referencedColumnName = "us_user_id", name = "app_submit_by", insertable = false, updatable = false, nullable = true)
//    public PublicUserModel getApplicantPublicUser() {
//        return applicantPublicUser;
//    }
//
//    public void setApplicantPublicUser(PublicUserModel applicantPublicUser) {
//        this.applicantPublicUser = applicantPublicUser;
//    }
    
    private User applicantPublicUser;

    @OneToOne(targetEntity = User.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "us_user_id", name = "app_submit_by", insertable = false, updatable = false, nullable = true)
    public User getApplicantPublicUser() {
        return applicantPublicUser;
    }

    public void setApplicantPublicUser(User applicantPublicUser) {
        this.applicantPublicUser = applicantPublicUser;
    }
    
    @Column(name = "qual_level")
    public String getQual_level() {
        return qual_level;
    }

    public void setQual_level(String qual_level) {
        this.qual_level = qual_level;
    }

    @Column(name = "usj_providers")
    public String getUsj_providers() {
        return usj_providers;
    }

    public void setUsj_providers(String usj_providers) {
        this.usj_providers = usj_providers;
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
    
    @Column(name = "internal_case")
    public String getInternal_case() {
        return internal_case;
    }

    public void setInternal_case(String internal_case) {
        this.internal_case = internal_case;
    }
    
    private JobDetailModel jobDetailModel;

    @OneToOne(targetEntity = JobDetailModel.class, optional = true, fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "CASE_ID", name = "CASE_ID", insertable = false, updatable = false, nullable = true)
    public JobDetailModel getJobDetailModel() {
        return jobDetailModel;
    }

    public void setJobDetailModel(JobDetailModel jobDetailModel) {
        this.jobDetailModel = jobDetailModel;
    }
    
    private List<JobStatusModel> jobHistoryList;

    @OneToMany(targetEntity = JobStatusModel.class, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "case_id", name = "case_id", insertable = false, updatable = false, nullable = true)
    @OrderBy("STATUS_SEQ DESC")
    public List<JobStatusModel> getJobHistoryList() {
        return jobHistoryList;
    }

    public void setJobHistoryList(List<JobStatusModel> jobHistoryList) {
        this.jobHistoryList = jobHistoryList;
    }
    
    @Transient
    public String getWf_status_str() {
        BaseDAOImpl retrivalDAO = new BaseDAOImpl();
        String status_code_str = "";
        try {
            SetupCodeModel codeModel = (SetupCodeModel) retrivalDAO.getModelByCode("code_type,code_1", "JWS," + this.wf_status, new SetupCodeModel());
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
    
    public String getFim_user_id() {
        return fim_user_id;
    }

    public void setFim_user_id(String fim_user_id) {
        this.fim_user_id = fim_user_id;
    }

    public String getCo_id() {
        return co_id;
    }

    public void setCo_id(String co_id) {
        this.co_id = co_id;
    }
    
//    private User applicantPublicUser;
//
//    @OneToOne(targetEntity = PublicUserModel.class, optional = true, fetch = FetchType.LAZY)
//    @JoinColumn(referencedColumnName = "us_user_id", name = "app_submit_by", insertable = false, updatable = false, nullable = true)
//    public PublicUserModel getApplicantPublicUser() {
//        return applicantPublicUser;
//    }
//
//    public void setApplicantPublicUser(PublicUserModel applicantPublicUser) {
//        this.applicantPublicUser = applicantPublicUser;
//    }
    
    @Override
    public void manualOperation(Session session) throws Exception {
        try {
            if (this.get_operation().equals(ApplicationPModel.OPERATION.PROCESS_UPDATE)) {                
                updateApplication(this, session);
            } else if (this.get_operation().equals(ApplicationPModel.OPERATION.PROCESS_COMPLETE)) {
                completeApplication(this, session);
            }                
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "ApplicationPModel", "manualOperation");
        }
    }
    
    public synchronized ApplicationPModel updateApplication(ApplicationPModel model, Session session) throws Exception {

        try {
            Debug.printDebug("runnning updateApplication in model ~~~~~");
            
            saveOrUpdateChecklist(session, model, "N");
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "updateApplication");
        } finally {
        }
        return model;
    }
    
    public synchronized String completeApplication(ApplicationPModel model, Session session) throws Exception {
        String completingResult = "success";
        Map sessionMap = ActionContext.getContext().getSession();
        
        try {
            Debug.printDebug("runnning completeApplication in model~~~~~");
            
            if(model.getWf_status().equals(UtimapsAction.WF_STATUS.PENDING_FOR_TA_CHECK) || model.getWf_status().equals(UtimapsAction.WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                saveOrUpdateChecklist(session, model, "N");
            }
            
            if(model.get_operation().equals(ApplicationPModel.OPERATION.PROCESS_COMPLETE)) {
                if(model.getWf_status().equals(UtimapsAction.WF_STATUS.PENDING_FOR_TA_CHECK)) {
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_APP1(session,model.get_taskId(), model.getCase_id(), "", UtimapsAction.WF_STATUS.PENDING_FOR_TA_CHECK, UtimapsAction.WF_STATUS.TA_CHECKED_NEW_CASE ,UtimapsAction.WF_STATUS.PENDING_FOR_SS_VERIFY, "", UtimapsAction.USER_GROUP.SS);
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                } else if (model.getWf_status().equals(UtimapsAction.WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                    
                    ChecklistModel caseChecklistModel = new ChecklistModel();
                    caseChecklistModel = model.getChecklistModel();
                    Debug.printDebug("checklist decision " + caseChecklistModel.getCheck_status());
                    
                    SurveyJobAction sja = null;
                    sja = new SurveyJobAction();
                    sja.generateUPS10(model.getCase_id());
                    
                    if(caseChecklistModel.getCheck_status().equals(UtimapsAction.CL_DECISION.ACCEPT)) {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Debug.printDebug("model.get_taskId() " + model.get_taskId());
                        
                        Map jsonMap = new HashMap();
                        //To check if in-house application
                        if(model.getInternal_case().equals("Y")) {
                            System.out.println("run 1");
                            jsonMap = wfApi.completeTask_APP1(session,model.get_taskId(), model.getCase_id(), "", UtimapsAction.WF_STATUS.PENDING_FOR_SS_VERIFY, UtimapsAction.WF_STATUS.APPLICATION_FOR_USJ_APPROVED ,UtimapsAction.WF_STATUS.APPLICATION_PAYMENT_COMPLETED,"","");
                        } else {
                            System.out.println("run 2");
                            jsonMap = wfApi.completeTask_APP1(session,model.get_taskId(), model.getCase_id(), "", UtimapsAction.WF_STATUS.PENDING_FOR_SS_VERIFY, UtimapsAction.WF_STATUS.APPLICATION_FOR_USJ_APPROVED ,UtimapsAction.WF_STATUS.APPLICATION_PAYMENT_PENDING,"","");
                        }
                        
                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        if(result.equalsIgnoreCase("success")) {
                            //to send email to surveyor after success
                            Map mailJsonMap = new HashMap();
                            AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
                            autoEmailDAO.setSession(session);
                            AutoEmail autoEmail = null;
                            try {
                                Debug.printDebug("send submission email to public user");
                                
                                String publicUserName = "";
                                String publicUserId = "";
                                String publicUserEmail = "default@utimaps.com.my";
                                String surveyFirmUserName = "";
                                String surveyFirmEmail = "";
                                
                                ApplicationPModel appModel =  (ApplicationPModel) checkingDAO.getModelById(model.getCase_id(), ApplicationPModel.class);
                                PublicUserModel publicUser = (PublicUserModel) checkingDAO.getModelByCode("us_user_id", appModel.getApp_submit_by(), new PublicUserModel());
                                
                                if(publicUser != null) {
                                    publicUserName = publicUser.getUs_user_name();
                                    publicUserId = publicUser.getUs_user_id();
                                    publicUserEmail = publicUser.getUs_email();
                                }
                                
                                SurveyFirmPModel surveyFirm = (SurveyFirmPModel) checkingDAO.getModelByCode("case_id", appModel.getCase_id(), new SurveyFirmPModel());
                                if(surveyFirm != null) {
                                    surveyFirmUserName = surveyFirm.getFirm_oic();
                                    surveyFirmEmail = surveyFirm.getFirm_email();

                                    publicUserName = Validator.isEmpty(surveyFirmUserName) ? publicUserName : publicUserName + ", " + surveyFirmUserName;
                                    publicUserEmail = Validator.isEmpty(surveyFirmEmail) ? publicUserEmail : publicUserEmail + "," + surveyFirmEmail;
                                }

                                Map mailParam = new HashMap();
                                autoEmail = autoEmailDAO.getAutoEmailByCode("USJAppAccepted", new AutoEmail());
                                Debug.printDebug("autoEmail " + autoEmail);
                                mailParam.put(EmailTrigger.MAIL_TO, publicUserEmail); // For Public User
                                mailParam.put("userName", publicUserName); // For Public User
                                mailParam.put("strCaseNo", model.getCase_ref());
                                new EmailTrigger().sendEMail(mailParam, autoEmail);
                                
                                String not_id = "";
                                String not_sender = "";
                                String not_subject = "";
                                String not_content = "";
                                if(autoEmail.getNotificationSetup() != null) {
                                    not_sender = sessionMap.get("userId").toString();
                                    not_id = autoEmail.getNotificationSetup().getNo_id();
                                    not_subject = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_subject(), mailParam);
                                    not_content = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_body(), mailParam);
                                }

                                NotificationPModel insertNot = new UtimapsAction().insertNotification(session, model.getCase_id(), not_id, not_sender, publicUserId, not_subject, not_content, "U001", "", model.get_taskId(), "S");

                                if(insertNot == null) {
                                    Debug.printDebug("Error when insert notification, no notification is inserted." + model.getCase_id() + " - " + not_id + " - " + not_sender + " - " + publicUserId + " - " + not_subject + " - " + not_content + " - " + "U001" + " - " + " " + " - " + model.get_taskId() + " - " + "S");
                                } else {
                                    new UtimapsAction().auditAction(model.getCase_id(),session,not_sender,"Send Notification : " + model.getCase_id());
                                }

                                Debug.printDebug("### Public email sent");

                                mailJsonMap.put("status", "success");
                                mailJsonMap.put("errMsg", "");
                            } catch (Exception e) {
                                mailJsonMap.put("errMsg", "Fail to update case status");
                                mailJsonMap.put("status", "fail");
                                Debug.printDebug("Error when send email " + e);
                                e.printStackTrace();
                                CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "completeApplication");
                            }
                        }
                        
                    } else if (caseChecklistModel.getCheck_status().equals(UtimapsAction.CL_DECISION.REJECT)) {
                        
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Debug.printDebug("model.get_taskId() " + model.get_taskId());
                        Map jsonMap = wfApi.completeTask_APP2(session, model.get_taskId(), model.getCase_id(), "", UtimapsAction.WF_STATUS.PENDING_FOR_SS_VERIFY, UtimapsAction.WF_STATUS.APPLICATION_FOR_USJ_REJECTED, UtimapsAction.WF_STATUS.ISSUE_LETTER_REJECTION,"","");
                        String result =  jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        if(result.equalsIgnoreCase("success")) {
                            //to send email to surveyor after success
                            Map mailJsonMap = new HashMap();
                            AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
                            autoEmailDAO.setSession(session);
                            FtpInterface ftp = FileOperationUtil.getFtpInterface();
                            AutoEmail autoEmail = null;
                            try {
                                Debug.printDebug("send submission email to public user");

                                String publicUserName = "";
                                String publicUserId = "";
                                String publicUserEmail = "default@utimaps.com.my";
                                String surveyFirmUserName = "";
                                String surveyFirmEmail = "";
                                
                                ApplicationPModel appModel =  (ApplicationPModel) checkingDAO.getModelById(model.getCase_id(), ApplicationPModel.class);
                                PublicUserModel publicUser = (PublicUserModel) checkingDAO.getModelByCode("us_user_id", appModel.getApp_submit_by(), new PublicUserModel());
                                
                                if(publicUser != null) {
                                    publicUserName = publicUser.getUs_user_name();
                                    publicUserId = publicUser.getUs_user_id();
                                    publicUserEmail = publicUser.getUs_email();
                                }
                                
                                SurveyFirmPModel surveyFirm = (SurveyFirmPModel) checkingDAO.getModelByCode("case_id", appModel.getCase_id(), new SurveyFirmPModel());
                                if(surveyFirm != null) {
                                    surveyFirmUserName = surveyFirm.getFirm_oic();
                                    surveyFirmEmail = surveyFirm.getFirm_email();

                                    publicUserName = Validator.isEmpty(surveyFirmUserName) ? publicUserName : publicUserName + ", " + surveyFirmUserName;
                                    publicUserEmail = Validator.isEmpty(surveyFirmEmail) ? publicUserEmail : publicUserEmail + "," + surveyFirmEmail;
                                }

                                Map mailParam = new HashMap();
                                autoEmail = autoEmailDAO.getAutoEmailByCode("USJAppRejected", new AutoEmail());
                                Debug.printDebug("autoEmail " + autoEmail);
                                mailParam.put(EmailTrigger.MAIL_TO, publicUserEmail); // For Public User
                                mailParam.put("userName", publicUserName); // For Public User
                                mailParam.put("strCaseNo", model.getCase_ref());
                                
                                FileModel ups10 = (FileModel) checkingDAO.getModelByCode("case_id,file_type",model.getCase_id()+",UPS10", new FileModel());
                                Debug.printDebug("ups10 " + ups10);
                                if(ups10 != null){
                                    String filePath = (ups10 != null) ? ups10.getFile_path() : ""; ;
                                    String fileExt = (ups10 != null) ? ups10.getFile_ext() : "pdf"; ;
                                    List attList = new ArrayList();

                                    Map aa = new HashMap();
                                    String fileName = ups10.getFile_name();
                                    aa.put(EmailTrigger.ATTACH.AttInputStream, ftp.getFile(filePath));
                                    aa.put(EmailTrigger.ATTACH.AttFileName, fileName);
                                    attList.add(aa);
                                    
                                    Debug.printDebug("testPath " + filePath);
                                    Debug.printDebug("fileName " + fileName);

                                    //add attachment
                                    mailParam.put(EmailTrigger.ATTACH.AttList, attList);
                                }

                                new EmailTrigger().sendEMail(mailParam, autoEmail);
                            
                                String not_id = "";
                                String not_sender = "";
                                String not_subject = "";
                                String not_content = "";
                                if(autoEmail.getNotificationSetup() != null) {
                                    not_sender = sessionMap.get("userId").toString();
                                    not_id = autoEmail.getNotificationSetup().getNo_id();
                                    not_subject = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_subject(), mailParam);
                                    not_content = new EmailTrigger().generateMailBody(autoEmail.getNotificationSetup().getNo_template_body(), mailParam);
                                }

                                NotificationPModel insertNot = new UtimapsAction().insertNotification(session, model.getCase_id(), not_id, not_sender, publicUserId, not_subject, not_content, "U001", "", model.get_taskId(), "S");

                                if(insertNot == null) {
                                    Debug.printDebug("Error when insert notification, no notification is inserted." + model.getCase_id() + " - " + not_id + " - " + not_sender + " - " + publicUserId + " - " + not_subject + " - " + not_content + " - " + "U001" + " - " + " " + " - " + model.get_taskId() + " - " + "S");
                                } else {
                                    new UtimapsAction().auditAction(model.getCase_id(),session,not_sender,"Send Notification : " + model.getCase_id());
                                }
                                
                                Debug.printDebug("### Public email sent");

                                mailJsonMap.put("status", "success");
                                mailJsonMap.put("errMsg", "");
                            } catch (Exception e) {
                                mailJsonMap.put("errMsg", "Fail to update case status");
                                mailJsonMap.put("status", "fail");
                                Debug.printDebug("Error when send email " + e);
                                e.printStackTrace();
                                CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "completeApplication");
                            }
                        }
                    } else {
                        Debug.printDebug("Invalid check status ::: " + caseChecklistModel.getCheck_status());
                    }
                    
                    updateLetterVersion(session, model, SystemConstants.FILE_TYPE.UPS10);
                }
            } else if(model.get_operation().equals(ApplicationPModel.OPERATION.PROCESS_ROUTE_BACK_APPLICATION)) {
                WorkflowApiAction wfApi = new WorkflowApiAction();
                Map jsonMap = wfApi.completeTask_APP4(session,model.get_taskId(), model.getCase_id(), "", UtimapsAction.WF_STATUS.PENDING_FOR_SS_VERIFY, UtimapsAction.WF_STATUS.APPLICATION_SS_ROUTE_BACK, UtimapsAction.WF_STATUS.PENDING_FOR_TA_CHECK,"",UtimapsAction.USER_GROUP.AS);
                String result = jsonMap.get("status").toString();
                Debug.printDebug("result " + result);
                completingResult = result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            completingResult = "failed";
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "updateApplication");
        } finally {
        }
        
        return completingResult;
    }
    
    public ChecklistModel saveOrUpdateChecklist(Session session, ApplicationPModel model, String updateFileStatus) throws Exception {
        ChecklistModel caseChecklistModel = new ChecklistModel();
        checkingDAO.setSession(session);

        if(model.getChecklistModel().getCheck_id() == "" || Validator.isEmpty(model.getChecklistModel().getCheck_id())) {
            Debug.printDebug("empty check id " + model.getChecklistModel());

            caseChecklistModel = model.getChecklistModel();
            caseChecklistModel.defaultAddProperties();
            String check_id = com.sains.framework.base.CommonFunction.getId(20);
            caseChecklistModel.setID(check_id);

            Map sessionMap = ActionContext.getContext().getSession();
            User user = new User();
            user = (User) checkingDAO.getModelById(sessionMap.get("userId").toString(), User.class);

            caseChecklistModel.setCheck_by_oic(user.getUs_id());
            caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());

            session.save(caseChecklistModel);

            model.getChecklistSetupCaseModel().getChecklistItemList().forEach((ChecklistItemSetupModel cism) -> {
                for(ChecklistItemModel cim : cism.getChecklistResultList()) {
                    try {
                        if(Validator.isEmpty(cim.getCl_id())) {
                            cim.defaultAddProperties();
                            cim.setCl_id(com.sains.framework.base.CommonFunction.getId(20));
                            cim.setCheck_id(check_id);
                            cim.setCl_status("Y");
                            session.save(cim);
                            
                            if (updateFileStatus.equals("Y")) {
                                updateFileStatus(session, model, cism, cim);
                            }
                        } else {
                            cim.updatableColumns = new String[]{"cl_id","case_id","check_id","file_id","ci_id","cl_result","cl_status","cl_remarks","cl_file_id"};
                            cim.defaultUpdateProperties();
                            cim.setCheck_id(check_id);
                            cim.setCl_status("Y");
                            session.update(cim);
                            
                            if (updateFileStatus.equals("Y")) {
                                updateFileStatus(session, model, cism, cim);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "updateApplication");
                    }

                }
            });
        } else {
            Debug.printDebug("model.getChecklistModel() " + model.getChecklistModel().getCheck_id());

            caseChecklistModel = model.getChecklistModel();
            caseChecklistModel.updatableColumns = new String[]{"check_id","case_id","check_status","check_comment_oic"};
            caseChecklistModel.defaultUpdateProperties();

            Map sessionMap = ActionContext.getContext().getSession();
            User user = new User();
            user = (User) checkingDAO.getModelById(sessionMap.get("userId").toString(), User.class);

            if(model.getWf_status().equals(UtimapsAction.WF_STATUS.PENDING_FOR_TA_CHECK)) {
                caseChecklistModel.setCheck_by_oic(user.getUs_id());
                caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());
            } else if(model.getWf_status().equals(UtimapsAction.WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                caseChecklistModel.setVerify_by_oic(user.getUs_id());
                caseChecklistModel.setVerify_date_oic(DateUtil.getCurrentTimestamp());
            }

            session.update(caseChecklistModel);

            model.getChecklistSetupCaseModel().getChecklistItemList().forEach((ChecklistItemSetupModel cism) -> {
                for(ChecklistItemModel cim : cism.getChecklistResultList()) {
                    try {
                        if(Validator.isEmpty(cim.getCl_id())) {
                            cim.defaultAddProperties();
                            cim.setCl_id(com.sains.framework.base.CommonFunction.getId(20));
                            cim.setCl_status("Y");
                            session.save(cim);
                            
                            if (updateFileStatus.equals("Y")) {
                                updateFileStatus(session, model, cism, cim);
                            }
                        } else {
                            cim.updatableColumns = new String[]{"cl_id","case_id","check_id","file_id","ci_id","cl_result","cl_status","cl_remarks","cl_file_id"};
                            cim.defaultUpdateProperties();
                            cim.setCl_status("Y");
                            session.update(cim);
                            
                            if (updateFileStatus.equals("Y")) {
                                updateFileStatus(session, model, cism, cim);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "updateApplication");
                    }
                }
            });
        }
        
        return caseChecklistModel;        
    }
    
    public void updateFileStatus(Session session, ApplicationPModel model, ChecklistItemSetupModel cism, ChecklistItemModel cim) {
        
        try {
            if(!Validator.isEmpty(cim.getCl_result())) {
                if(cim.getCl_result().equals("C")) {
                    cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                        FileModel updateFile = (FileModel) checkingDAO.getModelById(fileModel.getFile_id(), FileModel.class);
                        if(updateFile != null) {
                            updateFile.updatableColumns = new String[] {"file_id","file_status"};
                            updateFile.setFile_status("Y");
                            updateFile.defaultUpdateProperties();
                            session.update(updateFile);
                        }
                    });
                } else if(cim.getCl_result().equals("E")) {
                    cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                        FileModel updateFile = (FileModel) checkingDAO.getModelById(fileModel.getFile_id(), FileModel.class);
                        if(updateFile != null) {
                            updateFile.updatableColumns = new String[] {"file_id","file_status"};
                            updateFile.setFile_status("N");
                            updateFile.defaultUpdateProperties();
                            session.update(updateFile);
                        }
                    });
                } else if(cim.getCl_result().equals("N")) {
                    cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                        FileModel updateFile = (FileModel) checkingDAO.getModelById(fileModel.getFile_id(), FileModel.class);
                        if(updateFile != null) {
                            updateFile.updatableColumns = new String[] {"file_id","file_status"};
                            updateFile.setFile_status("Y");
                            updateFile.defaultUpdateProperties();
                            session.update(updateFile);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "updateFileStatus");
        }
    }
    
    public void updateLetterVersion(Session session, ApplicationPModel appModel, String fileType) throws Exception {
        
        try {
            List<FileModel> existingFileList = new ArrayList();
            Map param = new HashMap();
            param.put("case_id", appModel.getCase_id());
            param.put("file_type", fileType);
            existingFileList = checkingDAO.list_order(param, FileModel.class, "");

            if(existingFileList.size() > 0) {
                for(FileModel existingFile : existingFileList) {
                    existingFile.updatableColumns = new String[]{"job_id, file_status"};
                    existingFile.defaultUpdateProperties();
                    existingFile.setFile_status("N");
                    session.update(existingFile);                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "updateLetterVersion");
        }
    }
}
