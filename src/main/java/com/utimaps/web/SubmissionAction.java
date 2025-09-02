/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web;

import com.SysConf;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.DateUtil;
import com.sains.common.util.FileOperationUtil;
import com.sains.common.util.FtpInterface;
import com.sains.common.util.OBSUtil;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ReportGenerator;
import com.sains.framework.sam.dao.UserGroupDAOImpl;
import com.sains.workflow.util.RouteUtil;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.ChecklistItemModel;
import com.utimaps.model.ChecklistItemSetupModel;
import com.utimaps.model.ChecklistModel;
import com.utimaps.model.ChecklistSetupModel;
import com.utimaps.model.FileModel;
import java.io.InputStream;
import com.utimaps.model.JobDetailModel;
import java.io.ByteArrayOutputStream;
import com.utimaps.model.NotificationPModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.simple.JSONObject;

/**
 *
 * @author yonglai
 */
public class SubmissionAction extends BaseActionSupport<JobDetailModel> implements ModelDriven<JobDetailModel> {

//    ChecklistSetupModel checklistSetupModel;
//    ChecklistSetupModel checklistSetupModel2;
    public List swiperList = new ArrayList();
    public Integer swiperStep = 0;
    private CommonFunction cf = new CommonFunction();

    public SubmissionAction() {
        model = new JobDetailModel();
    }

    @Override
    public JobDetailModel getModel() {
        return model;
    }

    public String loadMainPage() {
        setPageTitle_("");

        return "load_main_page";
    }

    /**
     *
     * @return
     */
    @Override
    public String loadEditPage() {
        try {
            setPageTitle_("USJ Application");
            setPageSubTitle_("Edit");
            model = super.processEdit(getModel());
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadEditPage");
        }

        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    @Override
    public String processInsert() {
        BaseDAO surveyjobDAO = new BaseDAOImpl();
        try {
            validateRequired();
            specificValidation("insert");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.INSERT_FAIL;
            }

            addActionMessage(getText("createSuccess"));

        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "processInsert");
            addActionMessage(getText("createFail"));
            return SystemConstants.ACTION_Status.INSERT_FAIL;
        } finally {
            surveyjobDAO.closeSession();
        }
        return returnStr;
    }

    @Override
    public String processUpdate() {
        try {

            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
            addActionMessage(getText("updateSuccess"));
        } catch (Exception e) {
            return "edit_fail";
        }

        return returnStr;
    }

    public String loadCheckU10() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistControlUtilitySurvey(dao.getSession());

            wf_step = "0";
            checklist_step = "U10U20";
            wf_step = (Validator.isEmpty(model.getWf_status())) ? "1" : getText("utimaps.submission." + model.getWf_status());
            activeAccordion = "1";
            setSwiperStep(wf_step);
            setupSwiper("controlSubmission");

            if (model.getWf_status().equals(UtimapsAction.WF_STATUS.CHECK_U10_PENDING) || model.getWf_status().equals(UtimapsAction.WF_STATUS.VERIFY_U10_PENDING)) {
                rightToUpdate = true;
            }

        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadCheckU10");
            e.printStackTrace();
        } finally {
//            dao.closeSession();
        }

        return "load_digital_verify_page";
    }

    public String loadCheckU20() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistUtilitySurvey(dao.getSession()); //To-Do : new function for U20 only

            wf_step = "0";
            checklist_step = "U20";
            wf_step = (Validator.isEmpty(model.getWf_status())) ? "1" : getText("utimaps.submission." + model.getWf_status());
            activeAccordion = "1";
            setSwiperStep(wf_step);
            setupSwiper("nonControlSubmission");
            if (model.getWf_status().equals(UtimapsAction.WF_STATUS.CHECK_U20_PENDING) || model.getWf_status().equals(UtimapsAction.WF_STATUS.VERIFY_U20_PENDING)) {
                rightToUpdate = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadCheckU20");
        } finally {
            dao.closeSession();
        }

        return "load_digital_verify_page";
    }

    public String loadCheckU30() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistControlUtilitySurvey(dao.getSession());
            checklistControlCompChecking(dao.getSession());

            wf_step = "0";
            checklist_step = "U30";
            wf_step = (Validator.isEmpty(model.getWf_status_2())) ? "1" : getText("utimaps.submission." + model.getWf_status_2());
            activeAccordion = "2";
            activeTab = "1";
            setSwiperStep(wf_step);
            setupSwiper("controlSubmission");

            if (model.getWf_status_2().equals(UtimapsAction.WF_STATUS.TA_CHECK_U30_PENDING) || model.getWf_status_2().equals(UtimapsAction.WF_STATUS.STA_CHECK_U30_PENDING)) {
                rightToUpdate = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadCheckU30");
        } finally {
            dao.closeSession();
        }

        return "load_survey_comp_page";
    }

    public String loadCheckU40() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistControlUtilitySurvey(dao.getSession());
            checklistControlCompChecking(dao.getSession());
            checklistControlPlanChecking(dao.getSession());

            wf_step = "0";
            checklist_step = "U40";
            wf_step = (Validator.isEmpty(model.getWf_status_2())) ? "1" : getText("utimaps.submission." + model.getWf_status_2());
            activeAccordion = "2";
            activeTab = "2";
            setSwiperStep(wf_step);
            setupSwiper("controlSubmission");

            if (model.getWf_status_2().equals(UtimapsAction.WF_STATUS.CHECK_U40_PENDING)) {
                rightToUpdate = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadCheckU40");
        } finally {
            dao.closeSession();
        }

        return "load_survey_comp_page";
    }

    public String loadCheckU50() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistUtilitySurvey(dao.getSession());
            checklistControlUtilitySurvey(dao.getSession());

            checklistControlCompChecking(dao.getSession());
            checklistControlPlanChecking(dao.getSession());
            checklistDetailedPlanChecking(dao.getSession());

            wf_step = "0";
            checklist_step = "U50";
            wf_step = (Validator.isEmpty(model.getWf_status_2())) ? "1" : getText("utimaps.submission." + model.getWf_status_2());
            activeAccordion = "3";
            setSwiperStep(wf_step);

            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                setupSwiper("nonControlSubmission");
            } else {
                setupSwiper("controlSubmission");
            }

            if (model.getWf_status_2().equals(UtimapsAction.WF_STATUS.CHECK_U50_PENDING)) {
                rightToUpdate = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadCheckU50");
        } finally {
            dao.closeSession();
        }

        return "load_plan_check_page";
    }

    public String loadPrepUSCS10() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                checklistUtilitySurvey(dao.getSession());
            } else {
                checklistControlUtilitySurvey(dao.getSession());
            }

            wf_step = "0";
            wf_step = (Validator.isEmpty(model.getWf_status())) ? "1" : getText("utimaps.submission." + model.getWf_status());
            activeAccordion = "2";
            setSwiperStep(wf_step);

            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                setupSwiper("nonControlSubmission");
            } else {
                setupSwiper("controlSubmission");
            }

            if (!Validator.isEmpty(taskId_) && model.getWf_status().equals(UtimapsAction.WF_STATUS.ISSUE_USCS10_PENDING)) {
                rightToUpdate = true;
            }

            Map param = new HashMap();
            param.clear();
            param.put("case_id", model.getJob_id());
            param.put("file_type", SystemConstants.FILE_TYPE.USCS10);
            param.put("file_status", "Y");
            List<FileModel> fileList = dao.list_order(param, FileModel.class, "order by created_date desc");
            if (fileList.size() > 0) {
                fileID = fileList.get(0).getFile_id();
            } else {
                fileID = null;
            }
            
            if(wfActivityCode.equals("USJ003_01_04")) {
                checklistType = "DigiCS";
            } else if(wfActivityCode.equals("USJ004_01_04")) {
                checklistType = "DigiNCS";
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadPrepUSCS10");
        } finally {
            dao.closeSession();
        }

        return "load_uscs10_page";
    }

    public String loadPrepUSCS10H() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistHardcopyChecking(dao.getSession());

            wf_step = "0";
            wf_step = (Validator.isEmpty(model.getWf_status_2())) ? "1" : getText("utimaps.submission." + model.getWf_status_2());
            activeAccordion = "2";
            setSwiperStep(wf_step);

            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                setupSwiper("nonControlSubmission");
            } else {
                setupSwiper("controlSubmission");
            }

            if (!Validator.isEmpty(taskId_) && model.getWf_status_2().equals(UtimapsAction.WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING)) {
                rightToUpdate = true;
            }

            Map param = new HashMap();
            param.clear();
            param.put("case_id", model.getJob_id());
            param.put("file_type", SystemConstants.FILE_TYPE.USCS10H);
            param.put("file_status", "Y");
            List<FileModel> fileList = dao.list_order(param, FileModel.class, "order by created_date desc");
            if (fileList.size() > 0) {
                fileID = fileList.get(0).getFile_id();
            } else {
                fileID = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadPrepUSCS10H");
        } finally {
            dao.closeSession();
        }

        return "load_uscs10H_page";
    }

    public String loadIssueUSCS10() {

        return loadPrepUSCS10();
    }

    public String loadIssueUSCS10H() {

        return loadPrepUSCS10H();
    }

    public String loadPrepUSCS20() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistControlUtilitySurvey(dao.getSession());

            wf_step = "1";
            activeAccordion = "3";
            setSwiperStep(wf_step);

            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                setupSwiper("nonControlSubmission");
            } else {
                setupSwiper("controlSubmission");
            }

            if (!Validator.isEmpty(taskId_) && model.getWf_status().equals(UtimapsAction.WF_STATUS.ISSUE_USCS20_PENDING)) {
                rightToUpdate = true;
            }

            Map param = new HashMap();
            param.clear();
            param.put("case_id", model.getJob_id());
            param.put("file_type", SystemConstants.FILE_TYPE.USCS20);
            param.put("file_status", "Y");
            List<FileModel> fileList = dao.list_order(param, FileModel.class, "order by created_date desc");
            if (fileList.size() > 0) {
                fileID = fileList.get(0).getFile_id();
            } else {
                fileID = null;
            }
            
            System.out.println("wfActivityCode " + wfActivityCode);
            if(wfActivityCode.equals("USJ003_01_03")) {
                checklistType = "DigiCS";
            } else if(wfActivityCode.equals("USJ004_01_05")) {
                checklistType = "DigiNCS";
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadPrepUSCS20");
        } finally {
            dao.closeSession();
        }

        return "load_uscs20_page";
    }

    public String loadIssueUSCS20() {

        return loadPrepUSCS20();
    }

    public String loadVerifyHardcopy() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistControlUtilitySurvey(dao.getSession());
            checklistHardcopyChecking(dao.getSession());

            wf_step = "0";
            wf_step = (Validator.isEmpty(model.getWf_status_2())) ? "1" : getText("utimaps.submission." + model.getWf_status_2());
            activeAccordion = "1";
            checklist_step = "U21";
            setSwiperStep(wf_step);

            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                setupSwiper("nonControlSubmission");
            } else {
                setupSwiper("controlSubmission");
            }

            if (model.getWf_status_2().equals(UtimapsAction.WF_STATUS.CHECK_HARDCOPY_SUBMISSION_START) || model.getWf_status_2().equals(UtimapsAction.WF_STATUS.CHECK_HARDCOPY_SUBMISSION_PENDING)) {
                rightToUpdate = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadVerifyHardcopy");
        } finally {
            dao.closeSession();
        }

        return "load_hardcopy_verify_page";
    }

    public String startHardcopyAssessment() {

        try {
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }

            id = model.getID();
            caseId_ = model.getCase_id();
            jobId_ = model.getJob_id();
            model.set_taskId(taskId_);

            model.set_operation(JobDetailModel.OPERATION.PROCESS_START_HARDCOPY);
            serviceFactory.getSubmissionJobService().updateSubmission(model);

            addActionMessage(getText("updateSuccess"));
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "startHardcopyAssessment");
            return "return_to_edit_page";
        }

        return "return_to_edit_page";
    }

    public String loadPrepUSCS30() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistControlUtilitySurvey(dao.getSession());
            checklistHardcopyChecking(dao.getSession());

            wf_step = "0";
            wf_step = (Validator.isEmpty(model.getWf_status())) ? "1" : getText("utimaps.submission." + model.getWf_status_2());
            activeAccordion = "2";
            setSwiperStep(wf_step);

            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                setupSwiper("nonControlSubmission");
            } else {
                setupSwiper("controlSubmission");
            }

            if (!Validator.isEmpty(taskId_) && model.getWf_status_2().equals(UtimapsAction.WF_STATUS.ISSUE_USCS30_PENDING)) {
                rightToUpdate = true;
            }

            actionName = ActionContext.getContext().getName();
            Map param = new HashMap();
            param.clear();
            param.put("case_id", model.getJob_id());
            param.put("file_type", SystemConstants.FILE_TYPE.USCS30);
            param.put("file_status", "Y");
            List<FileModel> fileList = dao.list_order(param, FileModel.class, "order by created_date desc");
            if (fileList.size() > 0) {
                fileID = fileList.get(0).getFile_id();
            } else {
                fileID = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadPrepUSCS30");
        } finally {
            dao.closeSession();
        }

        return "load_uscs30_page";
    }

    public String loadIssueUSCS30() {

        return loadPrepUSCS30();
    }

    public String loadVerifyUSCS40() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistControlUtilitySurvey(dao.getSession());
            checklistControlCompChecking(dao.getSession());

            wf_step = "0";
            wf_step = (Validator.isEmpty(model.getWf_status())) ? "1" : getText("utimaps.submission." + model.getWf_status_2());
            activeAccordion = "3";
            activeTab = "1";
            setSwiperStep(wf_step);

            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                setupSwiper("nonControlSubmission");
            } else {
                setupSwiper("controlSubmission");
            }

            if (model.getWf_status_2().equals(UtimapsAction.WF_STATUS.VERIFY_USCS40_PENDING) || model.getWf_status_2().equals(UtimapsAction.WF_STATUS.ISSUE_USCS40_PENDING)) {
                rightToUpdate = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadVerifyUSCS40");
        } finally {
            dao.closeSession();
        }

        return "load_uscs40_page";
    }

    public String loadIssueUSCS40() {

        return loadVerifyUSCS40();
    }

    public String loadVerifyUSCS50() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistControlUtilitySurvey(dao.getSession());
            checklistControlCompChecking(dao.getSession());
            checklistControlPlanChecking(dao.getSession());

            wf_step = "0";
            wf_step = (Validator.isEmpty(model.getWf_status_2())) ? "1" : getText("utimaps.submission." + model.getWf_status_2());
            activeAccordion = "3";
            activeTab = "2";
            setSwiperStep(wf_step);

            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                setupSwiper("nonControlSubmission");
            } else {
                setupSwiper("controlSubmission");
            }

            if (model.getWf_status_2().equals(UtimapsAction.WF_STATUS.VERIFY_USCS50_PENDING) || model.getWf_status_2().equals(UtimapsAction.WF_STATUS.ISSUE_USCS50_PENDING)) {
                rightToUpdate = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadVerifyUSCS50");
        } finally {
            dao.closeSession();
        }

        return "load_uscs50_page";
    }

    public String loadIssueUSCS50() {

        return loadVerifyUSCS50();
    }

    public String loadIssueUSCS60() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistControlUtilitySurvey(dao.getSession());
            checklistControlCompChecking(dao.getSession());
            checklistControlPlanChecking(dao.getSession());

            wf_step = "5";
            activeAccordion = "3";
            setSwiperStep(wf_step);

            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                setupSwiper("nonControlSubmission");
            } else {
                setupSwiper("controlSubmission");
            }

            actionName = ActionContext.getContext().getName();
            Map param = new HashMap();
            param.clear();
            param.put("case_id", model.getJob_id());
            param.put("file_type", SystemConstants.FILE_TYPE.USCS60);
            param.put("file_status", "Y");
            List<FileModel> fileList = dao.list_order(param, FileModel.class, "order by created_date desc");
            if (fileList.size() > 0) {
                fileID = fileList.get(0).getFile_id();
            } else {
                fileID = null;
            }

            rightToUpdate = true;
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadIssueUSCS60");
        } finally {
            dao.closeSession();
        }

        return "load_uscs60_page";
    }

    public String loadIssueUSCS70() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistControlUtilitySurvey(dao.getSession());
            checklistControlCompChecking(dao.getSession());
            checklistControlPlanChecking(dao.getSession());

            wf_step = "6";
            activeAccordion = "3";
            setSwiperStep(wf_step);

            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                setupSwiper("nonControlSubmission");
            } else {
                setupSwiper("controlSubmission");
            }

            Map param = new HashMap();
            actionName = ActionContext.getContext().getName();
            param.clear();
            param.put("case_id", model.getJob_id());
            param.put("file_type", SystemConstants.FILE_TYPE.USCS70);
            param.put("file_status", "Y");
            List<FileModel> fileList = dao.list_order(param, FileModel.class, "order by created_date desc");
            if (fileList.size() > 0) {
                fileID = fileList.get(0).getFile_id();
            } else {
                fileID = null;
            }

            if (model.getWf_status_2().equals(UtimapsAction.WF_STATUS.ISSUE_USCS70_PENDING)) {
                rightToUpdate = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadIssueUSCS70");
        } finally {
            dao.closeSession();
        }

        return "load_uscs70_page";
    }

    public String loadIssueUSCS80() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistControlUtilitySurvey(dao.getSession());
            checklistControlCompChecking(dao.getSession());
            checklistControlPlanChecking(dao.getSession());
            checklistDetailedPlanChecking(dao.getSession());

            wf_step = "7";
            activeAccordion = "5";
            setSwiperStep(wf_step);

            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                setupSwiper("nonControlSubmission");
            } else {
                setupSwiper("controlSubmission");
            }

            Map param = new HashMap();
            actionName = ActionContext.getContext().getName();
            param.clear();
            param.put("case_id", model.getJob_id());
            param.put("file_type", SystemConstants.FILE_TYPE.USCS80);
            param.put("file_status", "Y");
            List<FileModel> fileList = dao.list_order(param, FileModel.class, "order by created_date desc");
            if (fileList.size() > 0) {
                fileID = fileList.get(0).getFile_id();
            } else {
                fileID = null;
            }

            if (model.getWf_status_2().equals(UtimapsAction.WF_STATUS.ISSUE_USCS80_PENDING) || model.getWf_status_2().equals(UtimapsAction.WF_STATUS.ISSUE_USCS80_U60_PENDING)) {
                rightToUpdate = true;
            }
            
            if(wfActivityCode.equals("USJ003_07_02")) {
                checklistType = "U50";
            } else if(wfActivityCode.equals("USJ005_01_05")) {
                checklistType = "U60";
                checklistSpatialPlanChecking(dao.getSession());
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadIssueUSCS80");
        } finally {
            dao.closeSession();
        }

        return "load_uscs80_page";
    }

    public String loadIssueUSCS90() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            Map param = new HashMap();
            param.put("case_id", model.getJob_id());
            param.put("file_type", "PSDSP");
            List<FileModel> psdspList = dao.list_order(param, FileModel.class, "order by created_date asc");

            param.clear();
            param.put("case_id", model.getJob_id());
            param.put("file_type", "SSDSP");
            param.put("description", "SSDSP_SIGNED");
            List<FileModel> ssdspList = dao.list_order(param, FileModel.class, "order by created_date asc");

            if (psdspList.size() > ssdspList.size() && ssdspList.size() > 0) {
                dspList = psdspList;
                addActionError("Some drawing plan is not signed by SS. Please recall drawing and resign");
            } else if (psdspList.size() == ssdspList.size() && ssdspList.size() > 0) {
                dspList = ssdspList;
                allDSPSigned = true;
            } else {
                dspList = psdspList;
            }
            checklistControlUtilitySurvey(dao.getSession());
            checklistControlCompChecking(dao.getSession());
            checklistControlPlanChecking(dao.getSession());
            checklistDetailedPlanChecking(dao.getSession());

            wf_step = "8";
            activeAccordion = "5";
            setSwiperStep(wf_step);

            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                setupSwiper("nonControlSubmission");
            } else {
                setupSwiper("controlSubmission");
            }

            actionName = ActionContext.getContext().getName();
            param.clear();
            param.put("case_id", model.getJob_id());
            param.put("file_type", SystemConstants.FILE_TYPE.USCS90);
            param.put("file_status", "Y");
            List<FileModel> fileList = dao.list_order(param, FileModel.class, "order by created_date desc");
            if (fileList.size() > 0) {
                fileID = fileList.get(0).getFile_id();
            } else {
                fileID = null;
            }

            if (model.getWf_status_2().equals(UtimapsAction.WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING)) {
                rightToUpdate = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadIssueUSCS90");
        } finally {
            dao.closeSession();
        }

        return "load_uscs90_page";
    }

    public String processUpdateJob() {
        try {
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }

            id = model.getID();
            caseId_ = model.getCase_id();
            jobId_ = model.getJob_id();
            model.set_taskId(taskId_);

            switch (model.getWf_status()) {
                case UtimapsAction.WF_STATUS.CHECK_HARDCOPY_SUBMISSION_PENDING:
                    model.set_operation(JobDetailModel.OPERATION.PROCESS_UPDATE_HARDCOPY);
                    break;
                case UtimapsAction.WF_STATUS.CHECK_U20_PENDING:
                case UtimapsAction.WF_STATUS.VERIFY_U20_PENDING:
                    model.set_operation(JobDetailModel.OPERATION.PROCESS_UPDATE_U20);
                    break;
                default:
                    model.set_operation(JobDetailModel.OPERATION.PROCESS_UPDATE);
                    break;
            }
            serviceFactory.getSubmissionJobService().updateApplication(model);

            addActionMessage(getText("updateSuccess"));
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "processUpdateJob");
            return "return_to_edit_page";
        }

        return "return_to_edit_page";
    }

    public String processUpdate2Job() {
        try {
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }

            id = model.getID();
            caseId_ = model.getCase_id();
            jobId_ = model.getJob_id();
            model.set_taskId(taskId_);

            switch (model.getWf_status_2()) {
                case UtimapsAction.WF_STATUS.CHECK_HARDCOPY_SUBMISSION_PENDING:
                    model.set_operation(JobDetailModel.OPERATION.PROCESS_UPDATE_HARDCOPY);
                    break;
                case UtimapsAction.WF_STATUS.TA_CHECK_U30_PENDING:
                    model.set_operation(JobDetailModel.OPERATION.PROCESS_UPDATE_U30);
                    break;
                case UtimapsAction.WF_STATUS.STA_CHECK_U30_PENDING:
                    model.set_operation(JobDetailModel.OPERATION.PROCESS_UPDATE_U30);
                    break;
                case UtimapsAction.WF_STATUS.CHECK_U40_PENDING:
                case UtimapsAction.WF_STATUS.VERIFY_U40_PENDING:
                    model.set_operation(JobDetailModel.OPERATION.PROCESS_UPDATE_U40);
                    break;
                case UtimapsAction.WF_STATUS.CHECK_U50_PENDING:
                    model.set_operation(JobDetailModel.OPERATION.PROCESS_UPDATE_U50);
                    break;
                case UtimapsAction.WF_STATUS.CHECK_U60_PENDING:
                case UtimapsAction.WF_STATUS.CHECK_U60_SD_PENDING:
                case UtimapsAction.WF_STATUS.CHECK_U60_SS_PENDING:
                    model.set_operation(JobDetailModel.OPERATION.PROCESS_UPDATE_U60);
                    break;
                default:
                    model.set_operation(JobDetailModel.OPERATION.PROCESS_UPDATE);
                    break;
            }
            serviceFactory.getSubmissionJobService().updateSubmission(model);

            addActionMessage(getText("updateSuccess"));
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "processUpdate2Job");
            return "return_to_edit_page";
        }

        return "return_to_edit_page";
    }

    public String processCompleteJob() {
        String returnString = "redirect_job";
        try {

            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }

            id = model.getID();
            caseId_ = model.getCase_id();
            model.set_taskId(taskId_);

            if (model.getWf_status().equals(UtimapsAction.WF_STATUS.CHECK_U10_PENDING) || model.getWf_status().equals(UtimapsAction.WF_STATUS.VERIFY_U10_PENDING)
                    || model.getWf_status().equals(UtimapsAction.WF_STATUS.CHECK_U20_PENDING) || model.getWf_status().equals(UtimapsAction.WF_STATUS.VERIFY_U20_PENDING)
                    || model.getWf_status().equals(UtimapsAction.WF_STATUS.ISSUE_USCS10_PENDING) || model.getWf_status().equals(UtimapsAction.WF_STATUS.ISSUE_USCS20_PENDING)) {
                model.set_operation(JobDetailModel.OPERATION.PROCESS_COMPLETE);
            }

            serviceFactory.getSubmissionJobService().completeApplication(model);

            addActionMessage(getText("updateSuccess"));
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "processCompleteJob");
            return returnString;
        }

        return returnString;
    }

    public String processComplete2Job() {
        String returnString = "redirect_job";
        try {

            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }

            id = model.getID();
            caseId_ = model.getCase_id();
            model.set_taskId(taskId_);
            model.setProcess_type(processType);
            model.setAssignTo(assignTo__);

            model.set_operation(JobDetailModel.OPERATION.PROCESS_COMPLETE_SUBMISSION);
            String completingResult = serviceFactory.getSubmissionJobService().completeApplication(model);
            
            if(completingResult.equals("success")) {
            } else if(completingResult.equals("postfail")) {
                returnString = "return_to_edit_page";
                addActionMessage("Unexpected error occurred when posting to SCS. Please contact administrator.");
            } else if(completingResult.equals("assignfail")) {
                returnString = "return_to_edit_page";
                addActionMessage("Unexpected error occurred when getting round robin user. Please contact administrator.");
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "processComplete2Job");
            return returnString;
        }

        return returnString;
    }

    public String processRouteBackJob() {
        String returnString = "redirect_job";
        try {

            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }

            id = model.getID();
            caseId_ = model.getCase_id();
            model.set_taskId(taskId_);
            model.setProcess_type(processType);

            if(processType.equals("310")) {
                model.set_operation(JobDetailModel.OPERATION.PROCESS_ROUTE_BACK_2);
            } else {
                model.set_operation(JobDetailModel.OPERATION.PROCESS_ROUTE_BACK);
            }

            serviceFactory.getSubmissionJobService().completeApplication(model);

            addActionMessage(getText("updateSuccess"));
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "processRouteBackJob");
            return returnString;
        }

        return returnString;
    }

    public String pdfViewer() throws Exception {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        InputStream jasperRptStream = null;
        Map reportParam = new HashMap();
        String jobId = request.getParameter("pJobId");
        String rptType = request.getParameter("pType");
        String chkType = request.getParameter("cType");
        System.out.println("chkType " + chkType);
        contentType = "application/pdf";
        String jasperFileName = "";
        String fileName = jobId + "_" + rptType + ".pdf";
        try {
            FileModel fileModel = (FileModel) baseDAO.getSession().getNamedQuery("FileModel.findBy_USCS")
                    .setParameter("case_id", jobId)
                    .setParameter("file_type", rptType)
                    .setParameter("description", rptType + "_SIGNED")
                    .uniqueResult();
            
            // List of input streams to merge
            List<InputStream> pdfsToMerge = new ArrayList<>();
            
            if (fileModel == null) {
                if (rptType.equals("U10")||rptType.equals("U20")||rptType.equals("U30")||rptType.equals("U40")||rptType.equals("U50")||rptType.equals("U21")||rptType.equals("U60")) {
                    if (rptType.equals("U10")) {
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U10.jasper";
                    } else if (rptType.equals("U20")) {
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U20.jasper";
                    } else if (rptType.equals("U30")) {
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U30.jasper";
                    } else if (rptType.equals("U40")) {
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U40.jasper";
                    } else if (rptType.equals("U50")) {
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U50.jasper";
                    } else if (rptType.equals("U21")) {
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U21.jasper";
                    } else if (rptType.equals("U60")) {
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U60.jasper";
                    }

                    reportParam.put("pCaseId", jobId.trim());
                    contentDisposition = "filename=" + fileName;
                    jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                    inputStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                    return "pdfViewer";
                } else if (rptType.startsWith("USCS")) {
                    
                    if (rptType.equals("USCS10")) {
                        if(chkType.equals("DigiCS")) {
                            // Digital : CS
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U10.jasper";
                            reportParam.put("pCaseId", jobId.trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream);
                            
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U20.jasper";
                            reportParam.put("pCaseId", jobId.trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream2 = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream2);
                            
                        } else if(chkType.equals("DigiNCS")) {
                            // Digital : Non-CS
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U20.jasper";
                            reportParam.put("pCaseId", jobId.trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream);
                        }
                    } if (rptType.equals("USCS10H")) {
                        // Hardcopy
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U21.jasper";
                        reportParam.put("pCaseId", jobId.trim());
                        jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                        InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                        reportParam.clear();

                        pdfsToMerge.add(checklistReportStream);
                        
                    } else if (rptType.equals("USCS20")) {
                        if(chkType.equals("DigiCS")) {
                            // Digital : CS
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U10.jasper";
                            reportParam.put("pCaseId", jobId.trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream);
                            
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U20.jasper";
                            reportParam.put("pCaseId", jobId.trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream2 = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream2);
                            
                        } else if(chkType.equals("DigiNCS")) {
                            // Digital : Non-CS
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U20.jasper";
                            reportParam.put("pCaseId", jobId.trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream);
                        }
                        
                    } else if (rptType.equals("USCS30")) {
                        // CS : Hardcopy
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U21.jasper";
                        reportParam.put("pCaseId", jobId.trim());
                        jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                        InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                        reportParam.clear();
                        
                        pdfsToMerge.add(checklistReportStream);
                        
                    } else if (rptType.equals("USCS40")) {
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U30.jasper";
                        reportParam.put("pCaseId", jobId.trim());
                        jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                        InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                        reportParam.clear();
                        
                        pdfsToMerge.add(checklistReportStream);
                    } else if (rptType.equals("USCS50")) {
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U40.jasper";
                        reportParam.put("pCaseId", jobId.trim());
                        jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                        InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                        reportParam.clear();
                        
                        pdfsToMerge.add(checklistReportStream);
                    } else if (rptType.equals("USCS60")) {
                    } else if (rptType.equals("USCS70")) {
                    } else if (rptType.equals("USCS80")) {
                        if(chkType.equals("U50")) {
                            // Query U50
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U50.jasper";
                            reportParam.put("pCaseId", jobId.trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream);
                            
                        } else if(chkType.equals("U60")) {
                             // Query U60
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U60.jasper";
                            reportParam.put("pCaseId", jobId.trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream);
                        }
                       
                    } else if (rptType.equals("USCS90")) {
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U50.jasper";
                        reportParam.put("pCaseId", jobId.trim());
                        jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                        InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                        reportParam.clear();

                        pdfsToMerge.add(checklistReportStream);
                        
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U60.jasper";
                        reportParam.put("pCaseId", jobId.trim());
                        jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                        InputStream checklistReportStream2 = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                        reportParam.clear();

                        pdfsToMerge.add(checklistReportStream2);
                    }
                    
                    jasperFileName = "/WEB-INF/classes/com/utimaps/report/IssueUSCS.jasper";
                    reportParam.put("pJobId", jobId.trim());
                    reportParam.put("pType", rptType);
                } else if (rptType.equals("MINUTE")) {
                    jasperFileName = "/WEB-INF/classes/com/utimaps/report/Minute.jasper";
                    reportParam.put("pJobId", jobId.trim());
                    reportParam.put("pType", rptType);
                } else if (rptType.equals("DSP") || rptType.equals("SSDSP")) {
                    fileModel = (FileModel) baseDAO.getSession().getNamedQuery("FileModel.findBy_DSP")
                            .setParameter("case_id", jobId)
                            .setParameter("file_type", "PSDSP")
                            .uniqueResult();
                    FtpInterface ftp = FileOperationUtil.getFtpInterface();
                    inputStream = ftp.getFile(fileModel.getFile_path());
                    contentDisposition = "filename=\"" + StringEscapeUtils.escapeHtml4(fileModel.getFile_name()) + "\"";
                    contentType = "application/" + fileModel.getFile_ext();
                    if (Validator.isEmpty(fileModel.getFile_ext())) {
                        contentType = FILE_TYPE_MAP.getContentType(fileModel.getFile_name());
                        if (contentType.startsWith("application/")) {
                            contentType = "application/" + fileModel.getFile_name().substring(fileModel.getFile_name().lastIndexOf(".") + 1);
                        }
                    }
                    return "pdfViewer";
                }
                
                contentDisposition = "filename=" + fileName;
                jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                inputStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                pdfsToMerge.add(inputStream);

                // Merge the PDFs
                ByteArrayOutputStream mergedOutputStream = new ByteArrayOutputStream();
                PDFMergerUtility pdfMerger = new PDFMergerUtility();
                for (InputStream pdfStream : pdfsToMerge) {
                    pdfMerger.addSource(pdfStream);
                }
                pdfMerger.setDestinationStream(mergedOutputStream);
                pdfMerger.mergeDocuments(null);

                // Use the merged PDF as the input stream
                inputStream = new ByteArrayInputStream(mergedOutputStream.toByteArray());
            } else {
                FtpInterface ftp = FileOperationUtil.getFtpInterface();

                inputStream = ftp.getFile(fileModel.getFile_path());
                contentDisposition = "filename=\"" + StringEscapeUtils.escapeHtml4(fileModel.getFile_name()) + "\"";
                contentType = "application/" + fileModel.getFile_ext();
                if (Validator.isEmpty(fileModel.getFile_ext())) {
                    contentType = FILE_TYPE_MAP.getContentType(fileModel.getFile_name());
                    if (contentType.startsWith("application/")) {
                        contentType = "application/" + fileModel.getFile_name().substring(fileModel.getFile_name().lastIndexOf(".") + 1);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "pdfViewer");
        }
        return "pdfViewer";
    }

    final static MimetypesFileTypeMap FILE_TYPE_MAP = new MimetypesFileTypeMap();

    public String viewTempFile() throws Exception {

        try {
            FileModel fileModel = (FileModel) baseDAO.getSession().getNamedQuery("FileModel.findBy_fileId").setParameter("file_id", fileID).uniqueResult();
            FtpInterface ftp = FileOperationUtil.getFtpInterface();

            inputStream = ftp.getFile(fileModel.getFile_path());
            contentDisposition = "filename=\"" + StringEscapeUtils.escapeHtml4(fileModel.getFile_name()) + "\"";
            if (!fileModel.getFile_ext().toLowerCase().equals("pdf")) {
                contentDisposition = "attachment; filename=\"" + StringEscapeUtils.escapeHtml4(fileModel.getFile_name()) + "\"";
            }
            contentType = "application/" + fileModel.getFile_ext();
            if (Validator.isEmpty(fileModel.getFile_ext())) {
                contentType = FILE_TYPE_MAP.getContentType(fileModel.getFile_name());
                if (contentType.startsWith("application/")) {
                    contentType = "application/" + fileModel.getFile_name().substring(fileModel.getFile_name().lastIndexOf(".") + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "viewTempFile");
        }

        return "pdfViewer";
    }

    public void generateLetter() throws Exception {
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        InputStream jasperRptStream = null;
        Map reportParam = new HashMap();
        BaseDAO dao = baseDAO;
        dao.setSession(baseDAO.getSession());

        try {
            String sftpUploadPath = null;
            String newDrDocId = CommonFunction.getId(20);
            String jasperFileName = "";
            String fileName = "sampple.pdf";
            FileModel uploadingModel = null;

            String deleteResult = "";

            switch (processType) {
                case UtimapsAction.WF_STATUS.ISSUE_USCS10_PENDING:
                    sftpUploadPath = SystemConstants.FILE_TYPE.USCS10;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING:
                    sftpUploadPath = SystemConstants.FILE_TYPE.USCS10H;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS20_PENDING:
                    sftpUploadPath = SystemConstants.FILE_TYPE.USCS20;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS30_PENDING:
                    sftpUploadPath = SystemConstants.FILE_TYPE.USCS30;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS40_PENDING:
                    sftpUploadPath = SystemConstants.FILE_TYPE.USCS40;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS50_PENDING:
                    sftpUploadPath = SystemConstants.FILE_TYPE.USCS50;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS60_PENDING:
                    sftpUploadPath = SystemConstants.FILE_TYPE.USCS60;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS70_PENDING:
                    sftpUploadPath = SystemConstants.FILE_TYPE.USCS70;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS80_PENDING:
                    sftpUploadPath = SystemConstants.FILE_TYPE.USCS80;
                    break;
                case UtimapsAction.WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING:
                    sftpUploadPath = SystemConstants.FILE_TYPE.USCS90;
                    break;
                case "MINUTE":
                    sftpUploadPath = SystemConstants.FILE_TYPE.MINUTE;
                    break;
                default:
                    break;
            }

            JobDetailModel jobModel = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            jobModel.set_operation(ApplicationPModel.OPERATION.DELETE_USCS_LETTER);
            deleteResult = serviceFactory.getSubmissionJobService().deleteUSCSLetter(jobModel, sftpUploadPath, sftpUploadPath);

            if (!Validator.isEmpty(deleteResult) && deleteResult.equals("success")) {
                String rptType = sftpUploadPath;
                fileName = rptType + "_" + jobModel.getCase_ref().replace("/", "_") + ".pdf";
                contentType = "application/pdf";
                System.out.println("rptType " + rptType);
                System.out.println("checklistType " + checklistType);
                // List of input streams to merge
                List<InputStream> pdfsToMerge = new ArrayList<>();
                
                if (processType.equals("MINUTE")) {
                    jasperFileName = "/WEB-INF/classes/com/utimaps/report/Minute.jasper";
                    reportParam.put("pJobId", jobModel.getJob_id().trim());
                    reportParam.put("pType", rptType);
                } else {
//                    jasperFileName = "/WEB-INF/classes/com/utimaps/report/IssueUSCS.jasper";
                    
                    if (rptType.equals("USCS10")) {
                        if(checklistType.equals("DigiCS")) {
                            // Digital : CS
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U10.jasper";
                            reportParam.put("pCaseId", jobModel.getJob_id().trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream);
                            
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U20.jasper";
                            reportParam.put("pCaseId", jobModel.getJob_id().trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream2 = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream2);
                            
                        } else if(checklistType.equals("DigiNCS")) {
                            // Digital : Non-CS
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U20.jasper";
                            reportParam.put("pCaseId", jobModel.getJob_id().trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream);
                        } else if(checklistType.equals("Hard")) {
                            // Hardcopy
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U21.jasper";
                            reportParam.put("pCaseId", jobModel.getJob_id().trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream);
                        }
                        
                    } else if (rptType.equals("USCS20")) {
                        if(checklistType.equals("DigiCS")) {
                            // Digital : CS
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U10.jasper";
                            reportParam.put("pCaseId", jobModel.getJob_id().trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream);
                            
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U20.jasper";
                            reportParam.put("pCaseId", jobModel.getJob_id().trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream2 = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream2);
                            
                        } else if(checklistType.equals("DigiNCS")) {
                            // Digital : Non-CS
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U20.jasper";
                            reportParam.put("pCaseId", jobModel.getJob_id().trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream);
                        } 
                    } else if (rptType.equals("USCS30")) {
                        // CS : Hardcopy
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U21.jasper";
                        reportParam.put("pCaseId", jobModel.getJob_id().trim());
                        jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                        InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                        reportParam.clear();
                        
                        pdfsToMerge.add(checklistReportStream);
                        
                    } else if (rptType.equals("USCS40")) {
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U30.jasper";
                        reportParam.put("pCaseId", jobModel.getJob_id().trim());
                        jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                        InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                        reportParam.clear();
                        
                        pdfsToMerge.add(checklistReportStream);
                    } else if (rptType.equals("USCS50")) {
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U40.jasper";
                        reportParam.put("pCaseId", jobModel.getJob_id().trim());
                        jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                        InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                        reportParam.clear();
                        
                        pdfsToMerge.add(checklistReportStream);
                    } else if (rptType.equals("USCS60")) {
                    } else if (rptType.equals("USCS70")) {
                    } else if (rptType.equals("USCS80")) {
                        if(checklistType.equals("")) {
                            // Query U50
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U50.jasper";
                            reportParam.put("pCaseId", jobModel.getJob_id().trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream);
                            
                        } else if(checklistType.equals("")) {
                             // Query U60
                            jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U60.jasper";
                            reportParam.put("pCaseId", jobModel.getJob_id().trim());
                            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                            InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                            reportParam.clear();

                            pdfsToMerge.add(checklistReportStream);
                        }
                       
                    } else if (rptType.equals("USCS90")) {
                        jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_U50.jasper";
                        reportParam.put("pCaseId", jobModel.getJob_id().trim());
                        jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                        InputStream checklistReportStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                        reportParam.clear();

                        pdfsToMerge.add(checklistReportStream);
                    }
                    
                    jasperFileName = "/WEB-INF/classes/com/utimaps/report/IssueUSCS.jasper";
                    reportParam.put("pJobId", jobModel.getJob_id().trim());
                    reportParam.put("pType", rptType);
                }
//                jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
//                reportParam.put("pJobId", jobModel.getJob_id().trim());
//                reportParam.put("pType", rptType);
                
                contentDisposition = "filename=" + fileName;
                jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                inputStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
                pdfsToMerge.add(inputStream);

                // Merge the PDFs
                ByteArrayOutputStream mergedOutputStream = new ByteArrayOutputStream();
                PDFMergerUtility pdfMerger = new PDFMergerUtility();
                for (InputStream pdfStream : pdfsToMerge) {
                    pdfMerger.addSource(pdfStream);
                }
                pdfMerger.setDestinationStream(mergedOutputStream);
                pdfMerger.mergeDocuments(null);

                // Use the merged PDF as the input stream
                inputStream = new ByteArrayInputStream(mergedOutputStream.toByteArray());

                ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
                if (ftp.isDirExists(SysConf.get("pathPrefix") + sftpUploadPath, Boolean.FALSE)) {
                    ftp.createDirIfNotExists(sftpUploadPath + "/" + jobId_, Boolean.FALSE);
//                    ftp.createFile(sftpUploadPath + "/" + jobId_ + "/" + newDrDocId, ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO));// use File as parameter
                    ftp.createFile(sftpUploadPath + "/" + jobId_ + "/" + newDrDocId, inputStream);// use File as parameter
                    ftp.insertToFileDirectory(sftpUploadPath + "/" + jobId_ + "/" + newDrDocId, fileName, "application/pdf", newDrDocId, baseDAO, sftpUploadPath, Boolean.FALSE);
                    if (uploadingModel != null) {
                    } else {
                        String letterType = "";
                        switch (processType) {
                            case UtimapsAction.WF_STATUS.ISSUE_USCS10_PENDING:
                                letterType = SystemConstants.FILE_TYPE.USCS10;
                                break;
                            case UtimapsAction.WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING:
                                letterType = SystemConstants.FILE_TYPE.USCS10H;
                                break;
                            case UtimapsAction.WF_STATUS.ISSUE_USCS20_PENDING:
                                letterType = SystemConstants.FILE_TYPE.USCS20;
                                break;
                            case UtimapsAction.WF_STATUS.ISSUE_USCS30_PENDING:
                                letterType = SystemConstants.FILE_TYPE.USCS30;
                                break;
                            case UtimapsAction.WF_STATUS.ISSUE_USCS40_PENDING:
                                letterType = SystemConstants.FILE_TYPE.USCS40;
                                break;
                            case UtimapsAction.WF_STATUS.ISSUE_USCS50_PENDING:
                                letterType = SystemConstants.FILE_TYPE.USCS50;
                                break;
                            case UtimapsAction.WF_STATUS.ISSUE_USCS60_PENDING:
                                letterType = SystemConstants.FILE_TYPE.USCS60;
                                break;
                            case UtimapsAction.WF_STATUS.ISSUE_USCS70_PENDING:
                                letterType = SystemConstants.FILE_TYPE.USCS70;
                                break;
                            case UtimapsAction.WF_STATUS.ISSUE_USCS80_PENDING:
                                letterType = SystemConstants.FILE_TYPE.USCS80;
                                break;
                            case UtimapsAction.WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING:
                                letterType = SystemConstants.FILE_TYPE.USCS90;
                                break;
                            case "MINUTE":
                                letterType = SystemConstants.FILE_TYPE.MINUTE;
                                break;
                            default:
                                break;
                        }

                        request.setAttribute("ignoreCsrfCheck", "true");
                        uploadingModel = new FileModel();
                        uploadingModel.setFile_id(newDrDocId);
                        uploadingModel.setFile_type(letterType);
                        uploadingModel.setFile_name(fileName);
                        uploadingModel.setOriginal_file_name(fileName);
                        uploadingModel.setFile_ext("pdf");
                        uploadingModel.setCase_id(jobModel.getJob_id());
                        uploadingModel.setCi_id("");
                        uploadingModel.setFile_path(sftpUploadPath + "/" + jobId_ + "/" + newDrDocId);
                        uploadingModel.setUsj_no("-");
                        uploadingModel.setTotal_files(1);
                        uploadingModel.setTotal_gislayer(0);
                        uploadingModel.setTotal_features(0);
                        uploadingModel.setDescription(letterType);
                        uploadingModel.setFile_status("Y");
                        uploadingModel.defaultAddProperties();
                        getDaoService_().insert(uploadingModel);
                    }
                }
            }

        } catch (BaseException be) {
            be.printStackTrace();
            CommonFunction.writeLogFile(be.getStackTrace(), "SubmissionAction", "SubmissionAction", "generateLetter");
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "generateLetter");
        } finally {
            ftp.disconnect();
            dao.closeSession();
        }
    }

    public String pdfSign() throws Exception {
        model = (JobDetailModel) baseDAO.getModelById(jobId_, JobDetailModel.class);
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        Integer intError = 0;
        String letterType = "";
        try {
            if (certFile == null) {
                cf.writeFile("SubmissionAction", "$$$$$$$$$$$$$$ no valid cert ");
                Debug.printDebug("$$$$$$$$$$$$$$ no valid cert ");
            }
            
            model.setCert_File(certFile);
            model.setCert_Password(certPassword);
            model.set_operation(ApplicationPModel.OPERATION.SIGN_USCS_LETTER);
            switch (processType) {
                case UtimapsAction.WF_STATUS.ISSUE_USCS10_PENDING:
                    letterType = SystemConstants.FILE_TYPE.USCS10;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING:
                    letterType = SystemConstants.FILE_TYPE.USCS10H;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS20_PENDING:
                    letterType = SystemConstants.FILE_TYPE.USCS20;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS30_PENDING:
                    letterType = SystemConstants.FILE_TYPE.USCS30;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS40_PENDING:
                    letterType = SystemConstants.FILE_TYPE.USCS40;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS50_PENDING:
                    letterType = SystemConstants.FILE_TYPE.USCS50;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS60_PENDING:
                    letterType = SystemConstants.FILE_TYPE.USCS60;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS70_PENDING:
                    letterType = SystemConstants.FILE_TYPE.USCS70;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS80_PENDING:
                    letterType = SystemConstants.FILE_TYPE.USCS80;
                    break;
                case UtimapsAction.WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING:
                    return signBatch();
                default:
                    break;
            }
            generateLetter();
            intError = serviceFactory.getSubmissionJobService().signUSCSLetter(model, processType, letterType);

            cf.writeFile("SubmissionAction", "---Sign batch cert intError " + intError);
            Debug.printDebug("---Sign batch cert intError " + intError);
            if (intError < 0) {
                if (intError == -1) {
                    addActionMessage("Sign Failed: Certificate has Expired.");
                } else if (intError == -2) {
                    addActionMessage("Sign Failed: Certificate has been Revoked.");
                } else if (intError == -3) {
                    addActionMessage("Sign Failed: Certificate is not from Valid Issuer");
                } else if (intError == -4) {
                    addActionMessage("Sign Failed: Wrong Password.");
                }
            } else {
                addActionMessage(getText("utimaps.sign.cert.success"));
            }

            id = model.getID();
            caseId_ = model.getCase_id();
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "pdfSign");
        } finally {
            dao.closeSession();
        }
        return "return_to_edit_page";
    }

    public String signBatch() throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        Integer intError = 0;
        String letterType = "";
        try {
            if (certFile == null) {
                cf.writeFile("SubmissionAction", "$$$$$$$$$$$$$$ no valid cert ");
                Debug.printDebug("$$$$$$$$$$$$$$ no valid cert ");
            }

            for (String docType : checkbox_selected_) {
                model.setCert_File(certFile);
                model.setCert_Password(certPassword);
                model.set_operation(ApplicationPModel.OPERATION.SIGN_USCS_LETTER);
                switch (docType) {
                    case "DSP":
                        processType = "DSP";
                        letterType = SystemConstants.FILE_TYPE.SSDSP;
                        break;
                    case "USCS90":
                        processType = "350";
                        generateLetter();
                        letterType = SystemConstants.FILE_TYPE.USCS90;
                        break;
                    case "MINUTE":
                        processType = "MINUTE";
                        generateLetter();
                        letterType = SystemConstants.FILE_TYPE.MINUTE;
                        break;
                    default:
                        break;
                }

                intError = serviceFactory.getSubmissionJobService().signUSCSLetter(model, processType, letterType);
                cf.writeFile("Submi1ssionAction", "---Sign batch cert intError " + intError);
                Debug.printDebug("---Sign batch cert intError " + intError);
                if (intError < 0) {
                    if (intError == -1) {
                        clearMessages();
                        addActionMessage("Sign Failed: Certificate has Expired.");
                    } else if (intError == -2) {
                        clearMessages();
                        addActionMessage("Sign Failed: Certificate has been Revoked.");
                    } else if (intError == -3) {
                        clearMessages();
                        addActionMessage("Sign Failed: Certificate is not from Valid Issuer");
                    } else if (intError == -4) {
                        clearMessages();
                        addActionMessage("Sign Failed: Wrong Password.");
                    }
                } else {
                    clearMessages();
                    addActionMessage(getText("utimaps.sign.cert.success"));
                }
            }

            id = model.getID();
            caseId_ = model.getCase_id();
        } catch (Exception e) {

            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "signBatch");
        } finally {
            dao.closeSession();
        }
        return "return_to_edit_page";
    }

    public String revokeSelected() throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());

        try {
            for (String docType : revoke_checkbox_selected_) {
                cf.writeFile("Submi1ssionAction", "proceed to revoke " + docType);
                serviceFactory.getSubmissionJobService().revokeUSCSLetter(model, docType);
            }
            id = model.getID();
            caseId_ = model.getCase_id();
        } catch (Exception e) {

            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "revokeSelected");
        } finally {
            dao.closeSession();
        }

        return "return_to_edit_page";
    }

    public Integer uploadSignedFile(InputStream inputFileStream, String fileid, String filename, String filePath, String fileType, String appId, File inputFile, String fileSubType) {
        OBSUtil ftp = new OBSUtil();
        BaseDAO dao = new BaseDAOImpl();
        String deleteResult = "";
        try {
            JobDetailModel jobDetailModel = new JobDetailModel();
            jobDetailModel = (JobDetailModel) dao.getModelById(appId, JobDetailModel.class);

            if (fileType.equals("SSDSP") || fileType.equals("PSDSP")) {
                deleteResult = "success";
            } else {
                jobDetailModel.set_operation(ApplicationPModel.OPERATION.DELETE_USCS_LETTER);
                deleteResult = serviceFactory.getSubmissionJobService().deleteUSCSLetter(jobDetailModel, fileType, fileSubType);
            }
            if (!Validator.isEmpty(deleteResult) && deleteResult.equals("success")) {
                dao.setSession(baseDAO.getSession());

                ftp.createDirIfNotExists(filePath, Boolean.FALSE);
                String fileMimeType = ServletActionContext.getServletContext().getMimeType(filename);
                if (inputFileStream == null) {

                }

                if (filename != null) {
                    if (inputFile != null) {
                        if (ftp.isFileExists(filePath + "/" + fileid, Boolean.FALSE)) {
                            ftp.deleteFile(filePath + "/" + fileid);
                        }
                        ftp.createFile(filePath + "/" + fileid, inputFile);

                    } else if (inputFileStream != null) {
                        try {
                            if (ftp.isFileExists(filePath + "/" + fileid, Boolean.FALSE)) {
                                ftp.deleteFile(filePath + "/" + fileid);
                            }
                            ftp.createFile(filePath + "/" + fileid, inputFileStream);
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "uploadSignedFile");
                        }

                    } else {
                        return 1;
                    }

                    if (ftp.isFileExists(filePath + "/" + fileid, editMode_)) {
                        //to create fileModel
                        jobDetailModel.set_operation(ApplicationPModel.OPERATION.INSERT_SIGNED_USCS);
                        serviceFactory.getSubmissionJobService().insertSignedUSCSLetter(jobDetailModel, fileid, filePath, filename, "application/pdf", fileType, fileSubType);
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "uploadSignedFile");
            return 1;
        }

        return 0;
    }

    public boolean isSigned(String jobId, String letter) {
        Boolean isSigned = true;
        if (letter.equals("SSDSP")) {
            Map param = new HashMap();
            param.put("case_id", jobId);
            param.put("file_type", "SSDSP");
            param.put("description", "SSDSP_SIGNED");
            if (baseDAO.list_order(param, FileModel.class, "order by created_date asc").size() == 0) {
                isSigned = false;
            }
        } else {
            FileModel fileModel = (FileModel) baseDAO.getSession().getNamedQuery("FileModel.findBy_USCS")
                    .setParameter("case_id", jobId)
                    .setParameter("file_type", letter)
                    .setParameter("description", letter + "_SIGNED")
                    //                    .setParameter("file_status", "Y")
                    .uniqueResult();
            if (fileModel == null) {
                isSigned = false;
            }
        }

        return isSigned;
    }

    public String pdfRevoke() {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());

        try {
            model.setJob_id(jobId_);
            model.set_operation(ApplicationPModel.OPERATION.REVOKE_USCS_LETTER);
            String fileType = "";
            switch (processType) {
                case UtimapsAction.WF_STATUS.ISSUE_USCS10_PENDING:
                    fileType = SystemConstants.FILE_TYPE.USCS10;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING:
                    fileType = SystemConstants.FILE_TYPE.USCS10H;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS20_PENDING:
                    fileType = SystemConstants.FILE_TYPE.USCS20;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS30_PENDING:
                    fileType = SystemConstants.FILE_TYPE.USCS30;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS40_PENDING:
                    fileType = SystemConstants.FILE_TYPE.USCS40;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS50_PENDING:
                    fileType = SystemConstants.FILE_TYPE.USCS50;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS60_PENDING:
                    fileType = SystemConstants.FILE_TYPE.USCS60;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS70_PENDING:
                    fileType = SystemConstants.FILE_TYPE.USCS70;
                    break;
                case UtimapsAction.WF_STATUS.ISSUE_USCS80_PENDING:
                    fileType = SystemConstants.FILE_TYPE.USCS80;
                    break;
                case UtimapsAction.WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING:
                    return revokeSelected();
                default:
                    break;
            }
            serviceFactory.getSubmissionJobService().revokeUSCSLetter(model, fileType);

            //for endorse USCS90, need revoke DSP and Minute too
            if (processType.equals(UtimapsAction.WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING)) {
                serviceFactory.getSubmissionJobService().revokeUSCSLetter(model, SystemConstants.FILE_TYPE.DSP);
                serviceFactory.getSubmissionJobService().revokeUSCSLetter(model, SystemConstants.FILE_TYPE.MINUTE);
            }
            id = model.getID();
            caseId_ = model.getCase_id();
        } catch (Exception e) {

            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "pdfRevoke");
        } finally {
            dao.closeSession();
        }

        return "return_to_edit_page";
    }

    private InputStream inputStream = null;
    private String contentType = "application/pdf";
    private String contentDisposition = "";

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private String actionName = "";

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    private String caseId_;

    public String getCaseId_() {
        return caseId_;
    }

    public void setCaseId_(String caseId_) {
        this.caseId_ = caseId_;
    }

    private String jobId_;

    public String getJobId_() {
        return jobId_;
    }

    public void setJobId_(String jobId_) {
        this.jobId_ = jobId_;
    }

    private String processType = "";

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }
    
    private String checklistType = "";
    public String getChecklistType() {
        return checklistType;
    }

    public void setChecklistType(String checklistType) {
        this.checklistType = checklistType;
    }

    private String taskId_;

    public String getTaskId_() {
        return taskId_;
    }

    public void setTaskId_(String taskId_) {
        this.taskId_ = taskId_;
    }

    private String wfActivityCode;

    public String getWfActivityCode() {
        return wfActivityCode;
    }

    public void setWfActivityCode(String wfActivityCode) {
        this.wfActivityCode = wfActivityCode;
    }

    private String letterType_;

    public String getLetterType_() {
        return letterType_;
    }

    public void setLetterType_(String letterType_) {
        this.letterType_ = letterType_;
    }

    private String id = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List getSwiperList() {
        return swiperList;
    }

    public void setSwiperList(List swiperList) {
        this.swiperList = swiperList;
    }

    public Integer getSwiperStep() {
        return swiperStep;
    }

    public void setSwiperStep(String swiperStep) {
        this.swiperStep = Integer.valueOf(swiperStep);
    }

    public void setupSwiper(String stepType) {

        switch (stepType) {
            case "nonControlSubmission":
                String cStepper1[] = {getText("utimaps.process.submission.checkDoc"), "", "selectStep", "1", "jobId", model.getJob_id()};
                String cStepper2[] = {getText("utimaps.process.submission.verifyHardcopy"), "", "selectStep", "2", "jobId", model.getJob_id()};
                String cStepper3[] = {getText("utimaps.process.submission.checkDetailedPlan"), "", "selectStep", "3", "jobId", model.getJob_id()};
                String cStepper4[] = {getText("utimaps.process.submission.checkSpatialDataset"), "", "selectStep", "4", "jobId", model.getJob_id()};
                String cStepper5[] = {getText("utimaps.process.submission.endorseUSP"), "", "selectStep", "5", "jobId", model.getJob_id()};
                String cStepper6[] = {getText("utimaps.process.submission.notifyApplicant"), "", "selectStep", "5", "jobId", model.getJob_id()};

                swiperList.add(cStepper1);
                swiperList.add(cStepper2);
                swiperList.add(cStepper3);
                swiperList.add(cStepper4);
                swiperList.add(cStepper5);
                swiperList.add(cStepper6);

                break;
            case "controlSubmission":
                String nStepper1[] = {getText("utimaps.process.submission.checkDoc"), "", "selectStep", "1", "jobId", model.getJob_id()};
                String nStepper2[] = {getText("utimaps.process.submission.verifyHardcopy"), "", "selectStep", "2", "jobId", model.getJob_id()};
                String nStepper3[] = {getText("utimaps.process.submission.checkSurveyComp"), "", "selectStep", "3", "jobId", model.getJob_id()};
                String nStepper4[] = {getText("utimaps.process.submission.checkUtilityTPlan"), "", "selectStep", "4", "jobId", model.getJob_id()};
                String nStepper5[] = {getText("utimaps.process.submission.compCompleted"), "", "selectStep", "5", "jobId", model.getJob_id()};
                String nStepper6[] = {getText("utimaps.process.submission.compApproved"), "", "selectStep", "6", "jobId", model.getJob_id()};
                String nStepper7[] = {getText("utimaps.process.submission.checkSpatialDataset"), "", "selectStep", "7", "jobId", model.getJob_id()};
                String nStepper8[] = {getText("utimaps.process.submission.checkDetailedPlan"), "", "selectStep", "8", "jobId", model.getJob_id()};
                String nStepper9[] = {getText("utimaps.process.submission.endorseUSP"), "", "selectStep", "9", "jobId", model.getJob_id()};

                swiperList.add(nStepper1);
                swiperList.add(nStepper2);
                swiperList.add(nStepper3);
                swiperList.add(nStepper4);
                swiperList.add(nStepper5);
                swiperList.add(nStepper6);
                swiperList.add(nStepper7);
                swiperList.add(nStepper8);
                swiperList.add(nStepper9);

                break;
            default:
                break;
        }
    }

    private Boolean rightToUpdate = false;

    public Boolean getRightToUpdate() {
        return rightToUpdate;
    }

    public void setRightToUpdate(Boolean rightToUpdate) {
        this.rightToUpdate = rightToUpdate;
    }

    String wf_step = "0";

    public String getWf_step() {
        return wf_step;
    }

    public void setWf_step(String wf_step) {
        this.wf_step = wf_step;
    }

    String activeAccordion = "0";

    public String getActiveAccordion() {
        return activeAccordion;
    }

    public void setActiveAccordion(String activeAccordion) {
        this.activeAccordion = activeAccordion;
    }

    String activeTab = "1";

    public String getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
    }

    String checklist_step = "0";

    public String getChecklist_step() {
        return checklist_step;
    }

    public void setChecklist_step(String checklist_step) {
        this.checklist_step = checklist_step;
    }

    String checker_name = "-";

    public String getChecker_name() {
        return checker_name;
    }

    public void setChecker_name(String checker_name) {
        this.checker_name = checker_name;
    }

    String verifier_name = "-";

    public String getVerifier_name() {
        return verifier_name;
    }

    public void setVerifier_name(String verifier_name) {
        this.verifier_name = verifier_name;
    }

    private File certFile = null;
    private String certPassword;

    public File getCertFile() {
        return certFile;
    }

    public void setCertFile(File certFile) {
        this.certFile = certFile;
    }

    public String getCertPassword() {
        return certPassword;
    }

    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    public String fileID;

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    private String[] checkbox_selected_;

    public String[] getCheckbox_selected_() {
        return checkbox_selected_;
    }

    public void setCheckbox_selected_(String[] checkbox_selected_) {
        this.checkbox_selected_ = checkbox_selected_;
    }

    private String[] revoke_checkbox_selected_;

    public String[] getRevoke_checkbox_selected_() {
        return revoke_checkbox_selected_;
    }

    public void setRevoke_checkbox_selected_(String[] revoke_checkbox_selected_) {
        this.revoke_checkbox_selected_ = revoke_checkbox_selected_;
    }

    private List<FileModel> dspList = new ArrayList();

    public List getDspList() {
        return dspList;
    }

    public void setDspList(List dspList) {
        this.dspList = dspList;
    }

    private Boolean allDSPSigned = false;

    public Boolean getAllDSPSigned() {
        return allDSPSigned;
    }

    public void setAllDSPSigned(Boolean allDSPSigned) {
        this.allDSPSigned = allDSPSigned;
    }

    private List fileList = new ArrayList();

    public List getFileList() {
        return fileList;
    }

    public void setFileList(List fileList) {
        this.fileList = fileList;
    }

    private List<NotificationPModel> notifList = new ArrayList();

    public List<NotificationPModel> getNotifList() {
        return notifList;
    }

    public void setNotifList(List<NotificationPModel> notifList) {
        this.notifList = notifList;
    }

    private String paymentRemarks;

    public String getPaymentRemarks() {
        return paymentRemarks;
    }

    public void setPaymentRemarks(String paymentRemarks) {
        this.paymentRemarks = paymentRemarks;
    }

    private String detailLink;

    public String getDetailLink() {
        return detailLink;
    }

    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }

    public void enableFilterSub(Session session) {
        session.enableFilter("caseFilter").setParameter("caseNo", jobId_);
        session.enableFilter("typeFilter").setParameter("fileType", "CM");
        session.enableFilter("fileFilter");
        session.enableFilter("statusFilter");
    }

    public void checklistControlUtilitySurvey(Session session) throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        enableFilterSub(dao.getSession());

        try {

            ChecklistModel checklistModel = new ChecklistModel();
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U10," + model.getJob_id(), new ChecklistModel());
            model.setU10ChecklistModel(checklistModel);
            
            // 09.09.2024 :: Cater for Checklist Versioning
            Map param = new HashMap();
            param.put("process_type", "U10");
            List<ChecklistSetupModel> list = dao.list_order(param, ChecklistSetupModel.class, "");
            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
            
            for(ChecklistSetupModel checklist : list){
                for(ChecklistItemSetupModel checklistItem : checklist.getChecklistItemList()){
                    if(checklistItem.getChecklistResultList().size() > 0){
                        checklistSetupModel = checklist;
                        break;
                    }
                }
            }
            
            if(checklistSetupModel.getID() == null || checklistSetupModel.getID().equals("")){
                for(ChecklistSetupModel checklist : list){
                    if(checklist.getChecklist_status().equals("Y")){
                        checklistSetupModel = checklist;  
                        break;
                    }
                }
            }

            Map param2 = new HashMap();
            param2.put("process_type", "U20");
            List<ChecklistSetupModel> list2 = dao.list_order(param2, ChecklistSetupModel.class, "");
            ChecklistSetupModel checklistSetupModel2 = new ChecklistSetupModel();
            
            for(ChecklistSetupModel checklist : list2){
                for(ChecklistItemSetupModel checklistItem : checklist.getChecklistItemList()){
                    if(checklistItem.getChecklistResultList().size() > 0){
                        checklistSetupModel2 = checklist;
                        break;
                    }
                }
            }
            
            if(checklistSetupModel2.getID() == null || checklistSetupModel2.getID().equals("")){
                for(ChecklistSetupModel checklist : list2){
                    if(checklist.getChecklist_status().equals("Y")){
                        checklistSetupModel2 = checklist;  
                        break;
                    }
                }
            }

//            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
//            checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type,checklist_status", "U10,Y", new ChecklistSetupModel());
//            ChecklistSetupModel checklistSetupModel2 = new ChecklistSetupModel();
//            checklistSetupModel2 = (ChecklistSetupModel) dao.getModelByCode("process_type,checklist_status", "U20,Y", new ChecklistSetupModel());

            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });
            checklistSetupModel2.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });

            model.setU10ChecklistSetupModel(checklistSetupModel);
            model.setU20ChecklistSetupModel(checklistSetupModel2);

            if (model.getU10ChecklistModel() != null) {
                Debug.printDebug("checkUser " + model.getU10ChecklistModel().getCheckUser());
                model.getU10ChecklistModel().getVerifyUser();
            }
            model.getSMS().size();

            model.getJobPaymentModel();

            String paymentMethod = model.getJobPaymentModel().getPayment_method();
            String paymentDate = model.getJobPaymentModel().getPayment_status_date_str();
            String payRefNo = model.getJobPaymentModel().getPay_ref_no();
            Integer rvsReceiptNo = model.getJobPaymentModel().getPaymentItemList().get(0).getRvs_receiptno();
            String paymentMethodStr = "";
            String transactionStr = "";
            String receiptNoStr = "";
            paymentMethodStr = Validator.isEmpty(paymentMethod) ? "" : getText("utimaps.paymentChannel." + paymentMethod);
            transactionStr = Validator.isEmpty(payRefNo) ? "" : " (Transaction No. " + payRefNo + ")";
            receiptNoStr = (rvsReceiptNo == null) ? "" : " via Receipt No. " + rvsReceiptNo + "";

            paymentRemarks = "Payment made through " + paymentMethodStr + transactionStr + " on " + paymentDate + receiptNoStr;
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "checklistControlUtilitySurvey");
        } finally {
            dao.closeSession();
        }
    }

    public void checklistUtilitySurvey(Session session) throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        enableFilterSub(dao.getSession());

        try {
            ChecklistModel checklistModel = new ChecklistModel();
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U20," + model.getJob_id(), new ChecklistModel());
            model.setU20ChecklistModel(checklistModel);
            
            // 09.09.2024 :: Cater for Checklist Versioning
            Map param = new HashMap();
            param.put("process_type", "U20");
            List<ChecklistSetupModel> list = dao.list_order(param, ChecklistSetupModel.class, "");
            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
            
            for(ChecklistSetupModel checklist : list){
                for(ChecklistItemSetupModel checklistItem : checklist.getChecklistItemList()){
                    if(checklistItem.getChecklistResultList().size() > 0){
                        checklistSetupModel = checklist;
                        break;
                    }
                }
            }
            
            if(checklistSetupModel.getID() == null || checklistSetupModel.getID().equals("")){
                for(ChecklistSetupModel checklist : list){
                    if(checklist.getChecklist_status().equals("Y")){
                        checklistSetupModel = checklist;  
                        break;
                    }
                }
            }

//            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
//            checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type,checklist_status", "U20,Y", new ChecklistSetupModel());

            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });
            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });

            model.setU20ChecklistSetupModel(checklistSetupModel);

            if (model.getU20ChecklistModel() != null) {
                Debug.printDebug("" + model.getU20ChecklistModel().getCheckUser());
                model.getU20ChecklistModel().getVerifyUser();
            }
            model.getSMS().size();
            model.getJobPaymentModel();

            String paymentMethod = model.getJobPaymentModel().getPayment_method();
            String paymentDate = model.getJobPaymentModel().getPayment_status_date_str();
            String payRefNo = model.getJobPaymentModel().getPay_ref_no();
            Integer rvsReceiptNo = model.getJobPaymentModel().getPaymentItemList().get(0).getRvs_receiptno();
            String paymentMethodStr = "";
            String transactionStr = "";
            String receiptNoStr = "";
            paymentMethodStr = Validator.isEmpty(paymentMethod) ? "" : getText("utimaps.paymentChannel." + paymentMethod);
            transactionStr = Validator.isEmpty(payRefNo) ? "" : " (Transaction No. " + payRefNo + ")";
            receiptNoStr = (rvsReceiptNo == null) ? "" : " via Receipt No. " + rvsReceiptNo + "";

            paymentRemarks = "Payment made through " + paymentMethodStr + transactionStr + " on " + paymentDate + receiptNoStr;

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "checklistUtilitySurvey");
        } finally {
            dao.closeSession();
        }
    }

    public void checklistHardcopyChecking(Session session) throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        enableFilterSub(dao.getSession());

        try {
            ChecklistModel checklistModel = new ChecklistModel();
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U21," + model.getJob_id(), new ChecklistModel());
            model.setU21ChecklistModel(checklistModel);
            
            // 09.09.2024 :: Cater for Checklist Versioning
            Map param = new HashMap();
            param.put("process_type", "U11");
            List<ChecklistSetupModel> list = dao.list_order(param, ChecklistSetupModel.class, "");
            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
            
            for(ChecklistSetupModel checklist : list){
                for(ChecklistItemSetupModel checklistItem : checklist.getChecklistItemList()){
                    if(checklistItem.getChecklistResultList().size() > 0){
                        checklistSetupModel = checklist;
                        break;
                    }
                }
            }
            
            if(checklistSetupModel.getID() == null || checklistSetupModel.getID().equals("")){
                for(ChecklistSetupModel checklist : list){
                    if(checklist.getChecklist_status().equals("Y")){
                        checklistSetupModel = checklist;  
                        break;
                    }
                }
            }

            Map param2 = new HashMap();
            param2.put("process_type", "U21");
            List<ChecklistSetupModel> list2 = dao.list_order(param2, ChecklistSetupModel.class, "");
            ChecklistSetupModel checklistSetupModel2 = new ChecklistSetupModel();
            
            for(ChecklistSetupModel checklist : list2){
                for(ChecklistItemSetupModel checklistItem : checklist.getChecklistItemList()){
                    if(checklistItem.getChecklistResultList().size() > 0){
                        checklistSetupModel2 = checklist;
                        break;
                    }
                }
            }
            
            if(checklistSetupModel2.getID() == null || checklistSetupModel2.getID().equals("")){
                for(ChecklistSetupModel checklist : list2){
                    if(checklist.getChecklist_status().equals("Y")){
                        checklistSetupModel2 = checklist;  
                        break;
                    }
                }
            }

//            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
//            checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type,checklist_status", "U11,Y", new ChecklistSetupModel());
//            ChecklistSetupModel checklistSetupModel2 = new ChecklistSetupModel();
//            checklistSetupModel2 = (ChecklistSetupModel) dao.getModelByCode("process_type,checklist_status", "U21,Y", new ChecklistSetupModel());

            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                csi.getChecklistFileModelList2().size();
                for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });

            checklistSetupModel2.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                csi.getChecklistFileModelList2().size();
                for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });

            model.setU11ChecklistSetupModel(checklistSetupModel);
            model.setU21ChecklistSetupModel(checklistSetupModel2);

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "checklistHardcopyChecking");
        } finally {
            dao.closeSession();
        }
    }

    public void checklistControlCompChecking(Session session) throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        enableFilterSub(dao.getSession());

        try {
            ChecklistModel checklistModel3 = new ChecklistModel();
            checklistModel3 = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U30," + model.getJob_id(), new ChecklistModel());
            model.setU30ChecklistModel(checklistModel3);
            
            // 09.09.2024 :: Cater for Checklist Versioning
            Map param = new HashMap();
            param.put("process_type", "U30");
            List<ChecklistSetupModel> list = dao.list_order(param, ChecklistSetupModel.class, "");
            ChecklistSetupModel checklistSetupModel3 = new ChecklistSetupModel();
            
            for(ChecklistSetupModel checklist : list){
                for(ChecklistItemSetupModel checklistItem : checklist.getChecklistItemList()){
                    if(checklistItem.getChecklistResultList().size() > 0){
                        checklistSetupModel3 = checklist;
                        break;
                    }
                }
            }
            
            if(checklistSetupModel3.getID() == null || checklistSetupModel3.getID().equals("")){
                for(ChecklistSetupModel checklist : list){
                    if(checklist.getChecklist_status().equals("Y")){
                        checklistSetupModel3 = checklist;  
                        break;
                    }
                }
            }

//            ChecklistSetupModel checklistSetupModel3 = new ChecklistSetupModel();
//            checklistSetupModel3 = (ChecklistSetupModel) dao.getModelByCode("process_type,checklist_status", "U30,Y", new ChecklistSetupModel());

            checklistSetupModel3.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });

            model.setU30ChecklistSetupModel(checklistSetupModel3);

            if (model.getU30ChecklistModel() != null) {
                Debug.printDebug("" + model.getU30ChecklistModel().getCheckUser());
                model.getU30ChecklistModel().getVerifyUser();
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "checklistControlCompChecking");
        } finally {
            dao.closeSession();
        }
    }

    public void checklistControlPlanChecking(Session session) throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        enableFilterSub(dao.getSession());

        try {
            ChecklistModel checklistModel4 = new ChecklistModel();
            checklistModel4 = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U40," + model.getJob_id(), new ChecklistModel());
            model.setU40ChecklistModel(checklistModel4);
            
            // 09.09.2024 :: Cater for Checklist Versioning
            Map param = new HashMap();
            param.put("process_type", "U40");
            List<ChecklistSetupModel> list = dao.list_order(param, ChecklistSetupModel.class, "");
            ChecklistSetupModel checklistSetupModel4 = new ChecklistSetupModel();
            
            for(ChecklistSetupModel checklist : list){
                for(ChecklistItemSetupModel checklistItem : checklist.getChecklistItemList()){
                    if(checklistItem.getChecklistResultList().size() > 0){
                        checklistSetupModel4 = checklist;
                        break;
                    }
                }
            }
            
            if(checklistSetupModel4.getID() == null || checklistSetupModel4.getID().equals("")){
                for(ChecklistSetupModel checklist : list){
                    if(checklist.getChecklist_status().equals("Y")){
                        checklistSetupModel4 = checklist;  
                        break;
                    }
                }
            }

//            ChecklistSetupModel checklistSetupModel4 = new ChecklistSetupModel();
//            checklistSetupModel4 = (ChecklistSetupModel) dao.getModelByCode("process_type,checklist_status", "U40,Y", new ChecklistSetupModel());

            checklistSetupModel4.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });

            model.setU40ChecklistSetupModel(checklistSetupModel4);

            if (model.getU40ChecklistModel() != null) {
                Debug.printDebug("" + model.getU40ChecklistModel().getCheckUser());
                model.getU40ChecklistModel().getVerifyUser();
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "checklistControlPlanChecking");
        } finally {
            dao.closeSession();
        }
    }

    public void checklistDetailedPlanChecking(Session session) throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        enableFilterSub(dao.getSession());

        try {
            ChecklistModel checklistModel = new ChecklistModel();
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U50," + model.getJob_id(), new ChecklistModel());
            model.setU50ChecklistModel(checklistModel);
            
            // 09.09.2024 :: Cater for Checklist Versioning
            Map param = new HashMap();
            param.put("process_type", "U50");
            List<ChecklistSetupModel> list = dao.list_order(param, ChecklistSetupModel.class, "");
            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
            
            for(ChecklistSetupModel checklist : list){
                for(ChecklistItemSetupModel checklistItem : checklist.getChecklistItemList()){
                    if(checklistItem.getChecklistResultList().size() > 0){
                        checklistSetupModel = checklist;
                        break;
                    }
                }
            }
            
            if(checklistSetupModel.getID() == null || checklistSetupModel.getID().equals("")){
                for(ChecklistSetupModel checklist : list){
                    if(checklist.getChecklist_status().equals("Y")){
                        checklistSetupModel = checklist;  
                        break;
                    }
                }
            }

//            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
//            checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type,checklist_status", "U50,Y", new ChecklistSetupModel());

            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });

            model.setU50ChecklistSetupModel(checklistSetupModel);

            if (model.getU50ChecklistModel() != null) {
                Debug.printDebug("" + model.getU50ChecklistModel().getCheckUser());
                model.getU50ChecklistModel().getVerifyUser();
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "checklistDetailedPlanChecking");
        } finally {
            dao.closeSession();
        }
    }
    
    public void checklistSpatialPlanChecking(Session session) throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        enableFilterSub(dao.getSession());

        try {
            ChecklistModel checklistModel = new ChecklistModel();
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U60," + model.getJob_id(), new ChecklistModel());
            model.setU60ChecklistModel(checklistModel);
            
            // 09.09.2024 :: Cater for Checklist Versioning
            Map param = new HashMap();
            param.put("process_type", "U60");
            List<ChecklistSetupModel> list = dao.list_order(param, ChecklistSetupModel.class, "");
            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
            
            for(ChecklistSetupModel checklist : list){
                for(ChecklistItemSetupModel checklistItem : checklist.getChecklistItemList()){
                    if(checklistItem.getChecklistResultList().size() > 0){
                        checklistSetupModel = checklist;
                        break;
                    }
                }
            }
            
            if(checklistSetupModel.getID() == null || checklistSetupModel.getID().equals("")){
                for(ChecklistSetupModel checklist : list){
                    if(checklist.getChecklist_status().equals("Y")){
                        checklistSetupModel = checklist;  
                        break;
                    }
                }
            }

//            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
//            checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type,checklist_status", "U60,Y", new ChecklistSetupModel());

            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });

            model.setU60ChecklistSetupModel(checklistSetupModel);

            if (model.getU60ChecklistModel() != null) {
                Debug.printDebug("" + model.getU60ChecklistModel().getCheckUser());
                model.getU60ChecklistModel().getVerifyUser();
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "checklistSpatialPlanChecking");
        } finally {
            dao.closeSession();
        }
    }

    public String moreInfo() {
        model = super.processEdit(getModel());
        cf.writeFile("SubmissionAction", "history size" + model.getJobHistoryList().size());
        Debug.printDebug("history size" + model.getJobHistoryList().size());
        return super.moreInfo();
    }

    public String loadViewJob() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("id");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            Map map = new HashMap();
            map.put("case_id", jobId_);

            notifList = dao.list_order(map, NotificationPModel.class, "order by updated_date desc");
            CommonFunction.writeFile("SubmissionAction", "notif size" + notifList.size());
            CommonFunction.writeFile("SubmissionAction", "history size" + model.getJobHistoryList().size());
            CommonFunction.writeFile("SubmissionAction", "usjletter size" + model.getSignedUsjLetterList().size());
            String strSQL = getFileListSQL(model.getCase_id(), model.getJob_id());
            fileList = baseDAO.getListFromSql(strSQL, null);
            ChecklistModel checklistModel = new ChecklistModel();
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "APP01," + model.getCase_id(), new ChecklistModel());
            model.setChecklistModel(checklistModel);

            checklistModel = new ChecklistModel();
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U10," + model.getJob_id(), new ChecklistModel());
            model.setU10ChecklistModel(checklistModel);

            checklistModel = new ChecklistModel();
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U20," + model.getJob_id(), new ChecklistModel());
            model.setU20ChecklistModel(checklistModel);

            checklistModel = new ChecklistModel();
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U30," + model.getJob_id(), new ChecklistModel());
            model.setU30ChecklistModel(checklistModel);

            checklistModel = new ChecklistModel();
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U40," + model.getJob_id(), new ChecklistModel());
            model.setU40ChecklistModel(checklistModel);

            checklistModel = new ChecklistModel();
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U50," + model.getJob_id(), new ChecklistModel());
            model.setU50ChecklistModel(checklistModel);

            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U21," + model.getJob_id(), new ChecklistModel());
            model.setU21ChecklistModel(checklistModel);
            
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "U60," + model.getJob_id(), new ChecklistModel());
            model.setU60ChecklistModel(checklistModel);
            
            CommonFunction.writeFile("SubmissionAction", "strSQL " + strSQL);
            String task_doer = null;
            RouteUtil routeUtil = new RouteUtil();
            Map taskMap = routeUtil.getCurrentOIC(model.getJob_id());
            if(taskMap.get("status").toString().equals("fail")) {
                
            } else {
                for (Map inProgressTask : (List<Map>) taskMap.get("taskList")) {
                    task_doer = inProgressTask.get("taskDoer").toString();
                }
                Map pendingTaskMap = routeUtil.getPendingTask("USJ", "doer", task_doer);
                for (Map inProgressTask : (List<Map>) pendingTaskMap.get("result")) {
                    if (inProgressTask.get("caseId").toString().equals(model.getJob_id())) {
                        for (Map taskDetail : (List<Map>) inProgressTask.get("taskAction")) {
                            detailLink = taskDetail.get("actionUrl").toString();
                            detailLink = detailLink.split("&taskId_")[0];
                        }
                        break;
                    }
                }
            }
            
            disableUtilityProvider = true;
            Debug.printDebug("detailLink " + detailLink);
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadViewJob");
        } finally {
            dao.closeSession();
        }

        return "load_job_summary_page";
    }

    public void clearChecklistResult(Session session, JobDetailModel jobModel, String processType) {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        dao.getSession().enableFilter("caseFilter").setParameter("caseNo", jobModel.getJob_id());
        dao.getSession().enableFilter("typeFilter").setParameter("fileType", "CM");
        dao.getSession().enableFilter("fileFilter");
        dao.getSession().enableFilter("statusFilter");

        try {
            JobDetailModel jobModel2 = new JobDetailModel();
            jobModel2 = (JobDetailModel) dao.getModelById(jobModel.getJob_id(), JobDetailModel.class);
            ChecklistSetupModel checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type", processType, new ChecklistSetupModel());
            ChecklistModel checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", processType + "," + jobModel2.getJob_id(), new ChecklistModel());

            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                csi.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                });
                for (ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });

            jobModel2.setChecklistSetupCaseModel(checklistSetupModel);
            jobModel2.setU10ChecklistModel(checklistModel);

            jobModel2.set_operation(JobDetailModel.OPERATION.PROCESS_CLEAR_CHECKLIST);
            serviceFactory.getSubmissionJobService().clearSubmissionChecklist(jobModel2);

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "clearChecklistResult");
        }
    }

    public void clearAllChecklistResult(Session session, JobDetailModel jobModel, String processType) {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        dao.getSession().enableFilter("caseFilter").setParameter("caseNo", jobModel.getJob_id());
        dao.getSession().enableFilter("typeFilter").setParameter("fileType", "CM");
        dao.getSession().enableFilter("fileFilter");
        dao.getSession().enableFilter("statusFilter");

        try {
            JobDetailModel jobModel2 = new JobDetailModel();
            jobModel2 = (JobDetailModel) dao.getModelById(jobModel.getJob_id(), JobDetailModel.class);
            ChecklistSetupModel checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type", processType, new ChecklistSetupModel());
            ChecklistModel checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", processType + "," + jobModel2.getJob_id(), new ChecklistModel());

            jobModel2.setChecklistSetupCaseModel(checklistSetupModel);
            jobModel2.setU10ChecklistModel(checklistModel);

            jobModel2.set_operation(JobDetailModel.OPERATION.PROCESS_CLEAR_CHECKLIST);
            serviceFactory.getSubmissionJobService().clearAllSubmissionChecklist(jobModel2);

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "clearAllChecklistResult");
        }
    }

    public boolean checkHasChecklist(String jobId, String checklist) {
        Boolean hasChecklist = true;
        Map param = new HashMap();
        param.put("case_id", jobId);
        param.put("file_type", checklist);

        if (baseDAO.list_order(param, FileModel.class, "order by created_date desc").size() == 0) {
            hasChecklist = false;
        }

        return hasChecklist;
    }

    private String noDecoPage = null;

    public String getNoDecoPage() {
        return noDecoPage;
    }

    public String viewPastChecklist() {
        BaseDAO dao = new BaseDAOImpl();
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try {
            noDecoPage = "/utimaps/job_listing/modalPastChecklist.jsp";
            String jobId = request.getParameter("jobId");
            String checklist = request.getParameter("checklist");
            dao.setSession(baseDAO.getSession());
            Map param = new HashMap();
            param.put("case_id", jobId);
            param.put("file_type", checklist);
            fileList = dao.list_order(param, FileModel.class, "order by created_date desc");
            checklist_step = checklist;
            Debug.printDebug("checklist " + checklist);
            Debug.printDebug("fileList size " + fileList.size());
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "viewPastChecklist");
        } finally {
//            dao.closeSession();
        }

        return "no_deco";
    }

//    get the list of files 
    private String getFileListSQL(String caseId, String jobId) {
        String strSQL_fileList = "SELECT * FROM ( "
                + //                List of file submitted by Survey Firm 
                "SELECT a.* FROM (SELECT (tsc.PROCESS_TYPE||'_SF') AS file_group, tsc2.CODE_DESC AS file_desc, uci.CL_RESULT AS status, "
                + "uf.FILE_ID, uf.FILE_NAME, uf.FILE_PATH, uf.ORIGINAL_FILE_NAME, uf.CREATED_DATE FROM US_FILE uf "
                + "inner JOIN T_SETUP_CHECKLIST_ITEM tsci ON tsci.CI_ID = uf.CI_ID AND tsci.CI_DATATYPE = uf.FILE_TYPE "
                + "LEFT JOIN T_SETUP_CHECKLIST tsc ON tsc.CHECKLIST_ID = tsci.CHECKLIST_ID "
                + "LEFT JOIN T_SETUP_CODE tsc2 ON tsc2.code_2 = tsci.CI_DATATYPE "
                + "LEFT JOIN US_CHECKLIST_ITEM uci ON uci.CASE_ID = uf.CASE_ID AND uci.CI_ID = tsci.CI_ID "
                + "WHERE uf.CASE_ID IN  ('" + caseId + "','" + jobId + "') "
                + "ORDER BY DECODE (tsc.PROCESS_TYPE, 'APP01',1, 'U10',2, 'U20',3), tsci.CI_SEQUENCE, uf.CREATED_DATE)a "
                + "UNION ALL "
                + //                List of file uploaded by L&S 
                "SELECT b.* FROM (SELECT (tsc.PROCESS_TYPE||'_LS') AS file_group, tsc2.CODE_DESC AS file_desc, uci.CL_RESULT AS status, "
                + "uf.FILE_ID, uf.FILE_NAME, uf.FILE_PATH, uf.ORIGINAL_FILE_NAME, uf.CREATED_DATE FROM US_FILE uf "
                + "inner JOIN T_SETUP_CHECKLIST_ITEM tsci ON tsci.CI_ID = uf.CI_ID "
                + "LEFT JOIN T_SETUP_CHECKLIST tsc ON tsc.CHECKLIST_ID = tsci.CHECKLIST_ID "
                + "LEFT JOIN T_SETUP_CODE tsc2 ON tsc2.code_2 = tsci.CI_DATATYPE "
                + "LEFT JOIN US_CHECKLIST_ITEM uci ON uci.CASE_ID = uf.CASE_ID AND uci.CI_ID = tsci.CI_ID "
                + "WHERE uf.CASE_ID IN  ('" + caseId + "','" + jobId + "') AND uf.FILE_TYPE = 'CM' "
                + "ORDER BY DECODE (tsc.PROCESS_TYPE, 'APP01',1, 'U10',2, 'U20',3), tsci.CI_SEQUENCE, uf.CREATED_DATE)b "
                + "UNION ALL "
                + //                List of file signed by Survey Firm
                "SELECT c.* FROM (SELECT ('APP01_SF') AS process_type, uf.FILE_TYPE, ('') AS status, "
                + "uf.FILE_ID, uf.FILE_NAME, uf.FILE_PATH, uf.ORIGINAL_FILE_NAME, uf.CREATED_DATE "
                + "FROM US_FILE uf WHERE uf.CASE_ID IN  ('" + caseId + "','" + jobId + "') AND CI_ID IS NULL "
                + "AND FILE_TYPE NOT LIKE '%SJL' AND FILE_TYPE NOT LIKE 'USCS%' AND file_type NOT in ('MINUTE', 'SSDSP'))c "
                + "UNION ALL "
                + //                List of file signed by L&S during Approval
                "SELECT d.* FROM (SELECT ('Approval_LS') AS process_type, uf.FILE_TYPE, ('') AS status, "
                + "uf.FILE_ID, uf.FILE_NAME, uf.FILE_PATH, uf.ORIGINAL_FILE_NAME, uf.CREATED_DATE "
                + "FROM US_FILE uf WHERE uf.CASE_ID IN  ('" + caseId + "','" + jobId + "') AND CI_ID IS NULL "
                + "AND (((FILE_TYPE LIKE 'USCS%' OR file_type in ('MINUTE', 'SSDSP')) AND DESCRIPTION LIKE '%_SIGNED') OR file_type = 'SISJL') ORDER BY CREATED_DATE)d) "
                + "ORDER BY DECODE (file_group, 'APP01_SF',1, 'APP01_LS',2, 'U10_SF',3, 'U10_LS',4, 'U20_SF', 5, 'U20_LS', 6, 'Approval', 7, 'Approval_LS', 8), "
                + "created_date asc";
        return strSQL_fileList;
    }

    private static final int BUFFER_SIZE = 4096;

    public void downloadSignedPlan() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);

            if (model != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream out = new ZipOutputStream(baos);
                FtpInterface ftp = FileOperationUtil.getFtpInterface();

                Map param = new HashMap();

                param.clear();
                param.put("case_id", model.getJob_id());
                param.put("file_type", "SSDSP");
                param.put("description", "SSDSP_SIGNED");
                List<FileModel> ssdspList = dao.list_order(param, FileModel.class, "order by created_date asc");

                if (ssdspList.size() > 0) {
                    for (FileModel fileModel : ssdspList) {
                        String filenames = fileModel.getFile_name();

                        InputStream inputStream = ftp.getFile(fileModel.getFile_path());
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int bytesRead = -1;

                        // Add ZIP entry to output stream.
                        File file = new File(fileModel.getFile_path() + "." + fileModel.getFile_ext());
                        String entryname = file.getName();
                        out.putNextEntry(new ZipEntry(entryname));

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }
                }

                out.flush();
                baos.flush();
                out.close();
                baos.close();

                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=\"Drawing Utility Survey Plan.zip\"");
                ServletOutputStream sos = response.getOutputStream();
                sos.write(baos.toByteArray());
                out.flush();
                out.close();
                sos.flush();
            } else {
                throw new Exception("Survey Job not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "downloadSignedPlan");
        } finally {
            dao.closeSession();
        }
    }

    public void updateApprovalDate() throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        JSONObject json = new JSONObject();
        int flag = 0;

        try {
            dao.setSession(baseDAO.getSession());
            jobId_ = request.getParameter("jobId");

            JobDetailModel jobModel = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            jobModel.updatableColumns = new String[]{"job_id", "approval_date"};
            jobModel.setApproval_date(DateUtil.getCurrentTimestamp());
            getDaoService_().update(jobModel);

            flag = 1;
            json.put("status", flag);
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "updateApprovalDate");
            json.put("status", flag);
        } finally {
            dao.closeSession();
        }

        response.setContentType("application/json");
        response.getWriter().write(json.toString());
    }

    public String apiPage() {

        return "api_page";
    }

    public List<Options> getTaskListForApiList() {
        List taskListForApiList = null;
        try {
            taskListForApiList = new ArrayList();
            taskListForApiList.add(new Options("", "Please Select"));

            taskListForApiList.add(new Options("3", "Checklist U30"));
            taskListForApiList.add(new Options("4", "Checklist U40"));
            taskListForApiList.add(new Options("5", "USCS60"));
            taskListForApiList.add(new Options("6", "USCS70"));
            taskListForApiList.add(new Options("7", "U50"));

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "getTaskListForApiList");
        } finally {

        }
        return taskListForApiList;
    }

    public List<Options> getTaskListForApiList2() {
        List taskListForApiList = null;
        try {
            taskListForApiList = new ArrayList();
            taskListForApiList.add(new Options("", "Please Select"));

            taskListForApiList.add(new Options("1", "Checklist U20"));
            taskListForApiList.add(new Options("2", "Checklist U21"));
            taskListForApiList.add(new Options("3", "Checklist U50"));

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "getTaskListForApiList2");
        } finally {

        }
        return taskListForApiList;
    }
    
    //24-03-2025 :: Spatial Checking Process
    private RouteUtil routeUtil = new RouteUtil();
    private List<Map> workloadList = null;
    
    public String loadAssignSpatial() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistUtilitySurvey(dao.getSession());
            checklistControlUtilitySurvey(dao.getSession());

            checklistControlCompChecking(dao.getSession());
            checklistControlPlanChecking(dao.getSession());
            checklistDetailedPlanChecking(dao.getSession());
            checklistSpatialPlanChecking(dao.getSession());
            
            wf_step = "0";
            checklist_step = "U60";
            wf_step = (Validator.isEmpty(model.getWf_status_2())) ? "1" : getText("utimaps.submission." + model.getWf_status_2());
            activeAccordion = "4";
            setSwiperStep(wf_step);

            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                setupSwiper("nonControlSubmission");
            } else {
                setupSwiper("controlSubmission");
            }
            
            // Get Assignee List for STA
            assigneeList = new ArrayList();
            Map defaultMap = new HashMap();
            defaultMap.put("email", "");
            defaultMap.put("userName", "--- Please Select ---");
            defaultMap.put("userId", "");
            assigneeList.add(defaultMap);
            UserGroupDAOImpl userGroupDAO = new UserGroupDAOImpl();
            Map userGroupMap = userGroupDAO.getGroupInfo("USJ_STA_" + model.getUsj_div(), null, baseDAO.getSession());
            assigneeList.addAll((List) userGroupMap.get("user"));
            
            // Get Workload List based on user group
            workloadList = new ArrayList();
            for (Map user : assigneeList) {
                if(!Validator.isEmpty((String) user.get("userId"))) {
                    Map userWorkloadMap = new HashMap();
                    userWorkloadMap.put("us_user_id", user.get("userId"));
                    userWorkloadMap.put("us_user_name", user.get("userName"));
                    Map completedTaskCountMap = routeUtil.getProcessedTaskCount("USJ", "1", (String)user.get("userId"), "populateTaskDetail::=N", "byMonth::=Y");
                    String completedCount = completedTaskCountMap.get("1")+"";
                    Integer completedInt = 0;
                    if (!Validator.isEmpty(completedCount)) {
                        completedInt = Integer.parseInt(completedCount);
                    }
                    userWorkloadMap.put("completed", completedInt);
                    Map pendingTaskMap = routeUtil.getPendingTask("USJ", "doer", (String)user.get("userId"));
                    List<Map<String, Object>> pendingTaskList =  ((List)pendingTaskMap.get("result"));
                    if (pendingTaskList != null && !pendingTaskList.isEmpty()) {
                        routeUtil.prepareDateValue(((List)pendingTaskMap.get("result")));
                    }
                    Integer outstanding = 0, inProgress = 0;
                    for (Map inProgressTask : (List<Map>)pendingTaskMap.get("result")) {
                        if (inProgressTask.get("due_date")!=null && 
                            ((Timestamp) inProgressTask.get("due_date")).after((Timestamp) inProgressTask.get("today"))) {
                            outstanding++;
                        } else {
                            inProgress++;
                        }
                    }
                    userWorkloadMap.put("outstanding", outstanding);
                    userWorkloadMap.put("inProgress", inProgress);
                    workloadList.add(userWorkloadMap);
                }
            }
            String a = "";
            int i=0;
            for (Map workload : workloadList) {
                a = "{\"Officer\":\""+workload.get("us_user_name")+"\",\"Completed\":"+workload.get("completed")+",\"In Progress\":"+workload.get("inProgress")+",\"Outstanding\":"+workload.get("outstanding")+"}";
                if(i>0){
                    jobStatistic += ","+a;
                }else{
                    jobStatistic = a;
                }
                i++;
            }

            if (model.getWf_status_2().equals(UtimapsAction.WF_STATUS.ASSIGN_STA_FOR_CHECKING)) {
                rightToUpdate = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadAssignSpatial");
        } finally {
            dao.closeSession();
        }
        return "load_assign_spatial";
    }
    
    public String loadCheckU60() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            jobId_ = request.getParameter("jobId");
            enableFilterSub(dao.getSession());

            model = (JobDetailModel) dao.getModelById(jobId_, JobDetailModel.class);
            checklistUtilitySurvey(dao.getSession());
            checklistControlUtilitySurvey(dao.getSession());

            checklistControlCompChecking(dao.getSession());
            checklistControlPlanChecking(dao.getSession());
            checklistDetailedPlanChecking(dao.getSession());
            checklistSpatialPlanChecking(dao.getSession());

            wf_step = "0";
            checklist_step = "U60";
            wf_step = (Validator.isEmpty(model.getWf_status_2())) ? "1" : getText("utimaps.submission." + model.getWf_status_2());
            activeAccordion = "4";
            setSwiperStep(wf_step);

            if (Validator.isEmpty(model.getControl_sv_flag()) || model.getControl_sv_flag().equals("N")) {
                wf_step = "4"; 
                setupSwiper("nonControlSubmission");
            } else {
                setupSwiper("controlSubmission");
            }

            if (model.getWf_status_2().equals(UtimapsAction.WF_STATUS.CHECK_U60_PENDING) || model.getWf_status_2().equals(UtimapsAction.WF_STATUS.CHECK_U60_SD_PENDING)
                || model.getWf_status_2().equals(UtimapsAction.WF_STATUS.CHECK_U60_SS_PENDING)) {
                rightToUpdate = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadCheckU60");
        } finally {
            dao.closeSession();
        }

        return "load_spatial_check_page";
    }
    
    private List<Map> assigneeList = null;
    public List getAssigneeList() {
        return assigneeList;
    }
    public void setAssigneeList(List assigneeList) {
        this.assigneeList = assigneeList;
    }
    
    private String jobStatistic;
    public String getJobStatistic() {
        return jobStatistic;
    }

    public void setJobStatistic(String jobStatistic) {
        this.jobStatistic = jobStatistic;
    }
    
    private String assignTo__;
    public String getAssignTo__() {
        return assignTo__;
    }

    public void setAssignTo__(String assignTo__) {
        this.assignTo__ = assignTo__;
    }
    
    public List utilityProvidersList = new ArrayList();
    public List getUtilityProvidersList() {
        utilityProvidersList = getCommList().getUtilityProvidersList();
        return utilityProvidersList;
    }

    public void setUtilityProvidersList(List utilityProvidersList) {
        this.utilityProvidersList = utilityProvidersList;
    }
    
    private boolean disableUtilityProvider = false;
    public boolean isDisableUtilityProvider() {
        return disableUtilityProvider;
    }

    public void setDisableUtilityProvider(boolean disableUtilityProvider) {
        this.disableUtilityProvider = disableUtilityProvider;
    }
    
}
