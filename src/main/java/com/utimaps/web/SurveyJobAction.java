/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web;

import com.SysConf;
import com.ism.web.ISMWorkflowBase;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.FileOperationUtil;
import com.sains.common.util.FtpInterface;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.LogFunction;
import com.sains.framework.base.ModelBase;
import com.sains.framework.base.ReportGenerator;
import com.sains.workflow.util.RouteUtil;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.ChecklistItemModel;
import com.utimaps.model.ChecklistItemSetupModel;
import com.utimaps.model.ChecklistModel;
import com.utimaps.model.ChecklistSetupModel;
import java.io.InputStream;
import com.utimaps.model.FileModel;
import com.utimaps.model.JobDetailModel;
import com.utimaps.model.NotificationPModel;
import static com.utimaps.web.IssuanceJobAction.FILE_TYPE_MAP;
import com.utimaps.web.UtimapsAction.WF_STATUS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.hibernate.Session;
import org.json.simple.JSONObject;

/**
 *
 * @author yonglai
 */
public class SurveyJobAction extends BaseActionSupport<ApplicationPModel> implements ModelDriven<ApplicationPModel>{
 
    ChecklistSetupModel checklistSetupModel;
    public List swiperList = new ArrayList();
    public Integer swiperStep = 0;
    HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    
    public SurveyJobAction() {
        model = new ApplicationPModel();
    }
    
    @Override
    public ApplicationPModel getModel() {
        return model;
    }
    
    public String loadMainPage() {
        setPageTitle_("");
        
        return "load_main_page";
    }

    public ChecklistSetupModel getChecklistSetupModel() {
        return checklistSetupModel;
    }

    public void setChecklistSetupModel(ChecklistSetupModel checklistSetupModel) {
        this.checklistSetupModel = checklistSetupModel;
    }
    
    @Override
    public String loadEditPage() {
        Debug.printDebug("loadEditPage");
        try {
            setPageTitle_("USJ Application");
            setPageSubTitle_("Edit");
            model = super.processEdit(getModel());
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "loadEditPage");
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
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "processInsert");
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
//            model.set_operation(ChecklistSetupModel.OPERATION.PROCESS_UPDATE);
//            serviceFactory.getChecklistService().update(getModel());

            addActionMessage(getText("updateSuccess"));
//        uncomment the lines below to catch BaseException
//        } catch (BaseException be){
//            addActionError(be.getMessage());
//            return SystemConstants.ACTION_Status.INSERT_FAIL;
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "processUpdate");
                return "edit_fail";
//        } finally {
//            closeSession();
        }

        return returnStr;
    }
    
    public String loadEditApplicationPage() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();
        
        try {
            dao.setSession(baseDAO.getSession());
            String case_id_=request.getParameter("caseId");
            Debug.printDebug("case_id_ " + case_id_);
            dao.getSession().enableFilter("caseFilter").setParameter("caseNo", case_id_);
            dao.getSession().enableFilter("typeFilter").setParameter("fileType", "CM");
            dao.getSession().enableFilter("fileFilter");
            dao.getSession().enableFilter("statusFilter");
            
            model = (ApplicationPModel)dao.getModelById(case_id_, ApplicationPModel.class);
            
            ChecklistModel checklistModel = new ChecklistModel();
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "APP01,"+model.getCase_id(), new ChecklistModel());
            model.setChecklistModel(checklistModel);
            
            checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type", "APP01", new ChecklistSetupModel());
            
            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                for(ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });
            
            if(model.getChecklistModel() != null) {
                if(model.getChecklistModel().getUser() != null) {
                    checker_name = model.getChecklistModel().getUser().getUs_user_name();
                }
                if(model.getChecklistModel().getVerifyUser() != null) {
                    verifier_name = model.getChecklistModel().getVerifyUser().getUs_user_name();
                }
            }
            
            Debug.printDebug("getApplicantPublicUser " + model.getApplicantPublicUser());
            model.setChecklistSetupCaseModel(checklistSetupModel);
            model.getUPS10List().size();
            
            wf_step = "0";
            checklist_step = "UPS10";
            wf_step = (Validator.isEmpty(model.getWf_status())) ? "1" : getText("utimaps.submission." + model.getWf_status());
            setSwiperStep(wf_step);
            setupSwiper("sjApplication");
            
            if(model.getWf_status().equals(UtimapsAction.WF_STATUS.PENDING_FOR_TA_CHECK) || model.getWf_status().equals(UtimapsAction.WF_STATUS.PENDING_FOR_SS_VERIFY) ) {
                rightToUpdate = true;
            }

            if(model.getWf_status().equals(UtimapsAction.WF_STATUS.PENDING_FOR_TA_CHECK)) {
                noDecisionMade = true;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "loadEditApplicationPage");
        } finally {
            dao.closeSession();
        }
        
        return "load_application_page";
    }
    
    public String loadRejectApplicationPage() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();
        
        try {
            dao.setSession(baseDAO.getSession());
            String case_id_=request.getParameter("caseId");
            Debug.printDebug("case_id_ " + case_id_);
            
            model = (ApplicationPModel)baseDAO.getModelById(case_id_, ApplicationPModel.class);
            
            dao.getSession().enableFilter("caseFilter").setParameter("caseNo", case_id_);
            dao.getSession().enableFilter("typeFilter").setParameter("fileType", "CM");
            
            ChecklistModel checklistModel = new ChecklistModel();
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "APP01,"+model.getCase_id(), new ChecklistModel());
            model.setChecklistModel(checklistModel);
            
            Map param = new HashMap();
            param.put("check_type", "APP01");
            List<ChecklistModel> list = dao.list_order(param, ChecklistModel.class, "");
            Debug.printDebug("list " + list.size());
            
            checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type", "APP01", new ChecklistSetupModel());
            
            Debug.printDebug("model.checklistModel " + model.getChecklistModel());
            Debug.printDebug("ChecklistSetup " + checklistSetupModel);
            Debug.printDebug("ChecklistSetup " + checklistSetupModel.getChecklistItemList());
            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList();
                for(ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList();
                }
            });
            
            if(model.getChecklistModel() != null) {
                if(model.getChecklistModel().getUser() != null) {
                    checker_name = model.getChecklistModel().getUser().getUs_user_name();
                }
            }
            model.setChecklistSetupCaseModel(checklistSetupModel);
            
            wf_step = "0";
            wf_step = (Validator.isEmpty(model.getWf_status())) ? "1" : getText("utimaps.submission." + model.getWf_status());
            Debug.printDebug("wf_step " + wf_step);
            setSwiperStep(wf_step);
            setupSwiper("sjRejection");
            
            if(model.getWf_status().equals(UtimapsAction.WF_STATUS.PENDING_FOR_TA_CHECK) || model.getWf_status().equals(UtimapsAction.WF_STATUS.PENDING_FOR_SS_VERIFY) ) {
                rightToUpdate = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "loadRejectApplicationPage");
        } finally {
            dao.closeSession();
        }
        
        return "load_reject_application_page";
    }
    
    public String processUpdateApplication() {
        try {
            Debug.printDebug("processUpdateApplication");
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
            
            actionName = "loadEditApplicationPageUSJ";
            id = model.getID();
            caseId_ = model.getCase_id();

            model.set_operation(ApplicationPModel.OPERATION.PROCESS_UPDATE);
            serviceFactory.getSurveyApplicationService().manualUpdate(model);
            
            addActionMessage(getText("updateSuccess"));
        } catch (Exception e) {
            e.printStackTrace();
            addActionMessage(e.getLocalizedMessage());
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "processUpdateApplication");
            return "edit_fail";
        }

        return "return_to_edit_page";
    }
    
    public String processCompleteApplication() {
        System.out.println("complete app?");
        String returnString = "redirect_job";
        try {
            Debug.printDebug("processCompleteApplication");
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
            
            actionName = "loadEditApplicationPageUSJ";
            id = model.getID();
            caseId_ = model.getCase_id();
            model.set_taskId(taskId_);
            Debug.printDebug("taskId_ " + taskId_);
            Debug.printDebug("model " + model.getChecklistModel());
            
//            ChecklistModel checklistModel = new ChecklistModel();
//            checklistModel = model.getChecklistModel();
//            
//            model.getChecklistSetupCaseModel().getChecklistItemList().forEach((ChecklistItemSetupModel cism) -> {
//            });

            // move the function into model instead of DAOImpl
            model.set_operation(ApplicationPModel.OPERATION.PROCESS_COMPLETE);
            serviceFactory.getSurveyApplicationService().manualUpdate(model);
            
//            //############################################## Trigger ISM [Start] ##############################################
//            new LogFunction().logInfo(this.getClass(), "ATTEMPT APP ISM IN PROGRESS WORKFLOW CALL FOR -> " + model.getID());
//            System.out.println("ATTEMPT UPDATE APP ISM ");
//            org.apache.logging.log4j.LogManager.getLogger(this.getClass().getSimpleName()).info("ATTEMPT APP ISM IN PROGRESS WORKFLOW CALL FOR -> " + model.getID());
//            new ISMServicesAction().triggerISMWorkflowApp(model, ISMWorkflowBase.ISM_WF_STEP.UPD_INPROGRESS);
//            //############################################## Trigger ISM [End] ##############################################
            
            Debug.printDebug("model.getWf_status() ==== " + model.getWf_status());
            if(model.getWf_status().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                returnString = "return_to_edit_page";
            }

            addActionMessage(getText("updateSuccess"));
        } catch (Exception e) {
            e.printStackTrace();
            addActionMessage(e.getLocalizedMessage());
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "processCompleteApplication");
            return returnString;
        }
        
        return returnString;
    }
    
    public String processRouteBackApplication() {
        String returnString = "redirect_job";
        try {
            Debug.printDebug("processRouteBackApplication");
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
            
            actionName = "loadEditApplicationPageUSJ";
            id = model.getID();
            caseId_ = model.getCase_id();
            model.set_taskId(taskId_);
            Debug.printDebug("taskId_ " + taskId_);
            Debug.printDebug("model " + model.getChecklistModel());
            
            model.set_operation(ApplicationPModel.OPERATION.PROCESS_ROUTE_BACK_APPLICATION);
            serviceFactory.getSurveyApplicationService().manualUpdate(model);

//            //############################################## Trigger ISM [Start] ##############################################
//            new LogFunction().logInfo(this.getClass(), "ATTEMPT APP ISM IN PROGRESS WORKFLOW CALL FOR -> " + model.getID());
//            org.apache.logging.log4j.LogManager.getLogger(this.getClass().getSimpleName()).info("ATTEMPT APP ISM IN PROGRESS WORKFLOW CALL FOR -> " + model.getID());
//            new ISMServicesAction().triggerISMWorkflowApp(model, ISMWorkflowBase.ISM_WF_STEP.UPD_QUERIED);
//            //############################################## Trigger ISM [End] ##############################################
            
            addActionMessage(getText("updateSuccess"));
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "processRouteBackApplication");
            new LogFunction().logError(this.getClass(), "", e);
            return returnString;
        }
        
        return returnString;
    }
    
    public String pdfViewer() {
        
        try {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            InputStream jasperRptStream = null;
            Map reportParam = new HashMap();
            String idCase = request.getParameter("caseId");
            String rptType = request.getParameter("type");
            contentType = "application/pdf";
            String jasperFileName = "";
            String fileName = idCase +".pdf";
            if (rptType.equals("viewAppSummary")) {
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/ApplicationSummary.jasper";
                fileName = "ApplicationSummary_"+idCase+".pdf";
            } else if (rptType.equals("checklistUPS10")) {
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_IssueUPS10.jasper";
//                jasperFileName = "/WEB-INF/classes/com/utimaps/report/PsChecklist_IssueUPS10.jasper";
                fileName = "ChecklistUPS10"+idCase+".pdf";
            } else if (rptType.equals("checklistUPS20")) {
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_IssueUPS20.jasper";
                fileName = "ChecklistUPS20"+idCase+".pdf";
            } else if (rptType.equals("checklistUPS21")) {
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_IssueUPS21.jasper";
                fileName = "ChecklistUPS21"+idCase+".pdf";
            } else if (rptType.equals("checklistUPS30")) {
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_IssueUPS30.jasper";
                fileName = "ChecklistUPS30"+idCase+".pdf";
            } else if (rptType.equals("checklistUPS31")) {
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_IssueUPS31.jasper";
                fileName = "ChecklistUPS31"+idCase+".pdf";
            }
            contentDisposition = "filename="+fileName;
            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
            reportParam.put("pCaseId", idCase.trim());
            inputStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "pdfViewer");
        }
   
        return "pdfViewer";
    }
    
    public void generateUPS10(String idCase) throws Exception {
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        InputStream jasperRptStream = null;
        Map reportParam = new HashMap();
        BaseDAO dao = baseDAO;
        dao.setSession(baseDAO.getSession());
        String generateResult = "Y";
        String generateMsg = "Success";
        JSONObject json = new JSONObject();
        
        try {
            String sftpUploadPath = null; 
            String newDrDocId = CommonFunction.getId(20);
            String jasperFileName = "";
            String fileName = "sampple.pdf";
            FileModel uploadingModel = null;
            
            String deleteResult = "";
                        
            ApplicationPModel caseModel = (ApplicationPModel)dao.getModelById(idCase, ApplicationPModel.class);
//            caseModel.set_operation(ApplicationPModel.OPERATION.DELETE_UPS10_FILE);
//            deleteResult = serviceFactory.getSurveyApplicationService().deleteUPS10Letter(caseModel);
            
//            if(!Validator.isEmpty(deleteResult) && deleteResult.equals("success")) {
                
                sftpUploadPath = "UPS10";
                fileName = "UPS10_"+caseModel.getCase_ref().replace("/", "_")+".pdf";
                contentType = "application/pdf";
                contentDisposition = "filename="+fileName;
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/Checklist_IssueUPS10.jasper";
//                jasperFileName = "/WEB-INF/classes/com/utimaps/report/PsChecklist_IssueUPS10.jasper";
                jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                reportParam.put("pCaseId", idCase.trim());

                ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
                if (ftp.isDirExists(SysConf.get("pathPrefix") + sftpUploadPath, Boolean.FALSE)) {
                    ftp.createDirIfNotExists(sftpUploadPath + "/" + idCase, Boolean.FALSE);
                    ftp.createFile(sftpUploadPath + "/" + idCase + "/" + newDrDocId, ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO));// use File as parameter
                    ftp.insertToFileDirectory(sftpUploadPath + "/" + idCase + "/" + newDrDocId, fileName, "application/pdf", newDrDocId, baseDAO, SystemConstants.FILE_TYPE.USJL, Boolean.FALSE);
                    Debug.printDebug("file insert directory " + sftpUploadPath + "/" + idCase + "/" );
                    if (uploadingModel != null) {
                    } else {
                        uploadingModel = new FileModel();
                        uploadingModel.setFile_id(newDrDocId);
                        uploadingModel.setFile_type(SystemConstants.FILE_TYPE.UPS10);
                        uploadingModel.setFile_name(fileName);
                        uploadingModel.setOriginal_file_name(fileName);
                        uploadingModel.setFile_ext("PDF");
                        uploadingModel.setCase_id(idCase);
                        uploadingModel.setCi_id("");
                        uploadingModel.setFile_path(sftpUploadPath + "/" + idCase + "/" + newDrDocId);
                        uploadingModel.setUsj_no("-");
                        uploadingModel.setTotal_files(1);
                        uploadingModel.setTotal_gislayer(0);
                        uploadingModel.setTotal_features(0);
                        uploadingModel.defaultAddProperties();

                        try {
                            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                            request.setAttribute("ignoreCsrfCheck", "true");
                            ModelBase.updateCurrentRequestObject("antiCSRF", "ignoreCSRF");//serene @ 10/8/2022
                            dao.beginBatchTransaction();
                            dao.insert(uploadingModel);
                            dao.commitBatchTransaction();
                        } catch (Exception e) {
                            dao.rollbackBatchTransaction();
                            try {
                                ftp.deleteFile(sftpUploadPath + "/" + newDrDocId); //try to delete if the file is uploaded but record not updated.
                            } catch (Exception delEx) {
                            }
                            throw e;
                        }
                    }
                }
//            }
            
        } catch (BaseException be) {
            generateResult = "N";
            generateMsg = "be: " + be ;
            be.printStackTrace();
            CommonFunction.writeLogFile(be.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "generateUPS10");
        } catch (Exception e) {
            generateResult = "N";
            generateMsg = "e: " + e ;
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "generateUPS10");
        } finally {
            ftp.disconnect();
            dao.closeSession();
        }
        
        json.put("status", generateResult);
        json.put("message", generateMsg);

        response.setContentType("application/json");
        response.getWriter().write(json.toString());
    }
    
    public String viewTempFile() throws Exception {
        Debug.printDebug("fileID = " + fileID);
        
        try {
            FileModel fileModel = (FileModel) baseDAO.getSession().getNamedQuery("FileModel.findBy_fileId").setParameter("file_id", fileID).uniqueResult();
            FtpInterface ftp = FileOperationUtil.getFtpInterface();

            inputStream = ftp.getFile(fileModel.getFile_path());
            contentDisposition = "filename=\""+StringEscapeUtils.escapeHtml4(fileModel.getFile_name())+"\"";
            contentType = "application/" + fileModel.getFile_ext();
            if (Validator.isEmpty(fileModel.getFile_ext())) {
                contentType = FILE_TYPE_MAP.getContentType(fileModel.getFile_name());
                if (contentType.startsWith("application/")) {
                    contentType = "application/" + fileModel.getFile_name().substring(fileModel.getFile_name().lastIndexOf(".")+1);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "viewTempFile");
        }

        return "fileDownload";
    }
    
    public String fileID;
    public String getFileID() {
        return fileID;
    }
    public void setFileID(String fileID) {
        this.fileID = fileID;
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
            case "sjApplication":
                String checkingAS[] = {"Checking by AS", "loadEditApplicationPageUSJ", "selectStep", "1", "caseId", model.getCase_id()};
                String verificationSS[] = {"Verification by SS", "loadEditApplicationPageUSJ", "selectStep", "2", "caseId", model.getCase_id()};
                String payFee[] = {"Pay Application Fees", "loadEditApplicationPageUSJ", "selectStep", "3", "caseId", model.getCase_id()};
                String prepareUSJ[] = {"Prepare USJ Instruction", "loadEditApplicationPageUSJ", "selectStep", "4", "caseId", model.getCase_id()};
                String issueUSJ[] = {"Issue USJ Instruction", "loadEditApplicationPageUSJ", "selectStep", "5", "caseId", model.getCase_id()};

                swiperList.add(checkingAS);
                swiperList.add(verificationSS);
                swiperList.add(payFee);
                swiperList.add(prepareUSJ);
                swiperList.add(issueUSJ);
                
                break;
            case "sjRejection":
                String checkingAS2[] = {"Checking by AS", "loadEditApplicationPageUSJ", "selectStep", "1", "caseId", model.getCase_id()};
                String verificationSS2[] = {"Verification by SS", "loadEditApplicationPageUSJ", "selectStep", "2", "caseId", model.getCase_id()};
                String rejectJob[] = {"Endorse Rejection", "loadRejectApplicationPage", "selectStep", "3", "caseId", model.getCase_id()};
                
                swiperList.add(checkingAS2);
                swiperList.add(verificationSS2);
                swiperList.add(rejectJob);
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
    
    private Boolean noDecisionMade = false;

    public Boolean getNoDecisionMade() {
        return noDecisionMade;
    }

    public void setNoDecisionMade(Boolean noDecisionMade) {
        this.noDecisionMade = noDecisionMade;
    }
    
    String wf_step = "0";
    public String getWf_step() {
        return wf_step;
    }

    public void setWf_step(String wf_step) {
        this.wf_step = wf_step;
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
    
    String checklist_step = "0";
    public String getChecklist_step() {
        return checklist_step;
    }

    public void setChecklist_step(String checklist_step) {
        this.checklist_step = checklist_step;
    }
    
    public void clearChecklistResult(Session session, ApplicationPModel appModel) {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        dao.getSession().enableFilter("caseFilter").setParameter("caseNo", appModel.getCase_id());
        dao.getSession().enableFilter("typeFilter").setParameter("fileType", "CM");
        dao.getSession().enableFilter("fileFilter");
        
        try {
            ApplicationPModel appModel2 = new ApplicationPModel();
            appModel2 = (ApplicationPModel) dao.getModelById(appModel.getCase_id(), ApplicationPModel.class);
            ChecklistSetupModel checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type", "APP01", new ChecklistSetupModel());
            ChecklistModel checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id","APP01," + appModel2.getCase_id() ,new ChecklistModel());
            
            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                csi.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                });
                for(ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });
            
            appModel2.setChecklistSetupCaseModel(checklistSetupModel);
            appModel2.setChecklistModel(checklistModel);

            appModel2.set_operation(ApplicationPModel.OPERATION.PROCESS_CHECK_CHECKLIST);
//            serviceFactory.getSurveyApplicationService().clearApplicationChecklist(appModel2);
            
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "clearChecklistResult");
        }
    }
    
    public boolean checkHasChecklist(String caseId, String checklist) {
        Boolean hasChecklist = true;
        Map param = new HashMap();
        param.put("case_id", caseId);
        param.put("file_type", checklist);

        if(baseDAO.list_order(param, FileModel.class, "order by created_date desc").size()==0) {
            hasChecklist = false;
        }
        
        return hasChecklist;
    }
    
    private String noDecoPage = null;
    public String getNoDecoPage() {
        return noDecoPage;
    }
    
    private List fileList = new ArrayList();

    public List getFileList() {
        return fileList;
    }

    public void setFileList(List fileList) {
        this.fileList = fileList;
    }
    
    public String viewPastChecklist() {
        BaseDAO dao = new BaseDAOImpl();
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try {
            noDecoPage = "/utimaps/job_listing/modalPastChecklist.jsp";
            String caseId = request.getParameter("caseId");
            String checklist = request.getParameter("checklist");
            dao.setSession(baseDAO.getSession());
            Map param = new HashMap();
            param.put("case_id", caseId);
            param.put("file_type", checklist);
            fileList = dao.list_order(param, FileModel.class, "order by created_date desc");
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "viewPastChecklist");
        } finally {
//            dao.closeSession();
        }

        return "no_deco";
    }
    
    public String moreInfo() {
        model = super.processEdit(getModel());
        Debug.printDebug("history size" + model.getJobHistoryList().size());
        return super.moreInfo();
    }
    
    public String loadViewJob() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();

        try {
            dao.setSession(baseDAO.getSession());
            actionName = ActionContext.getContext().getName();
            caseId_ = request.getParameter("id");

            model = (ApplicationPModel) dao.getModelById(caseId_, ApplicationPModel.class);
            Map map = new HashMap();
            map.put("case_id", caseId_);
            Debug.printDebug("history size" + model.getJobHistoryList().size());

            notifList = dao.list_order(map, NotificationPModel.class, "order by updated_date desc");
            CommonFunction.writeFile("SurveyJobAction", "notif size" + notifList.size());
            String strSQL = getFileListSQL(model.getCase_id());
            fileList = baseDAO.getListFromSql(strSQL, null);
            
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyJobAction", "loadViewJob");
        } finally {
            dao.closeSession();
        }

        return "load_case_summary_page";
    }
    
    private List<NotificationPModel> notifList = new ArrayList();

    public List<NotificationPModel> getNotifList() {
        return notifList;
    }

    public void setNotifList(List<NotificationPModel> notifList) {
        this.notifList = notifList;
    }
    
    private String getFileListSQL(String caseId) {
        String strSQL_fileList = "SELECT * FROM ( "
                + //                List of file submitted by Survey Firm 
                "SELECT a.* FROM (SELECT (tsc.PROCESS_TYPE||'_SF') AS file_group, tsc2.CODE_DESC AS file_desc, uci.CL_RESULT AS status, "
                + "uf.FILE_ID, uf.FILE_NAME, uf.FILE_PATH, uf.ORIGINAL_FILE_NAME, uf.CREATED_DATE FROM US_FILE uf "
                + "inner JOIN T_SETUP_CHECKLIST_ITEM tsci ON tsci.CI_ID = uf.CI_ID AND tsci.CI_DATATYPE = uf.FILE_TYPE "
                + "LEFT JOIN T_SETUP_CHECKLIST tsc ON tsc.CHECKLIST_ID = tsci.CHECKLIST_ID "
                + "LEFT JOIN T_SETUP_CODE tsc2 ON tsc2.code_2 = tsci.CI_DATATYPE "
                + "LEFT JOIN US_CHECKLIST_ITEM uci ON uci.CASE_ID = uf.CASE_ID AND uci.CI_ID = tsci.CI_ID "
                + "WHERE uf.CASE_ID IN  ('" + caseId + "') "
                + "ORDER BY DECODE (tsc.PROCESS_TYPE, 'APP01',1, 'U10',2, 'U20',3), tsci.CI_SEQUENCE, uf.CREATED_DATE)a "
                + "UNION ALL "
                + //                List of file uploaded by L&S 
                "SELECT b.* FROM (SELECT (tsc.PROCESS_TYPE||'_LS') AS file_group, tsc2.CODE_DESC AS file_desc, uci.CL_RESULT AS status, "
                + "uf.FILE_ID, uf.FILE_NAME, uf.FILE_PATH, uf.ORIGINAL_FILE_NAME, uf.CREATED_DATE FROM US_FILE uf "
                + "inner JOIN T_SETUP_CHECKLIST_ITEM tsci ON tsci.CI_ID = uf.CI_ID "
                + "LEFT JOIN T_SETUP_CHECKLIST tsc ON tsc.CHECKLIST_ID = tsci.CHECKLIST_ID "
                + "LEFT JOIN T_SETUP_CODE tsc2 ON tsc2.code_2 = tsci.CI_DATATYPE "
                + "LEFT JOIN US_CHECKLIST_ITEM uci ON uci.CASE_ID = uf.CASE_ID AND uci.CI_ID = tsci.CI_ID "
                + "WHERE uf.CASE_ID IN  ('" + caseId + "') AND uf.FILE_TYPE = 'CM' "
                + "ORDER BY DECODE (tsc.PROCESS_TYPE, 'APP01',1, 'U10',2, 'U20',3), tsci.CI_SEQUENCE, uf.CREATED_DATE)b "
                + "UNION ALL "
                + //                List of file signed by Survey Firm
                "SELECT c.* FROM (SELECT ('APP01_SF') AS process_type, uf.FILE_TYPE, ('') AS status, "
                + "uf.FILE_ID, uf.FILE_NAME, uf.FILE_PATH, uf.ORIGINAL_FILE_NAME, uf.CREATED_DATE "
                + "FROM US_FILE uf WHERE uf.CASE_ID IN  ('" + caseId + "') AND CI_ID IS NULL "
                + "AND FILE_TYPE NOT LIKE '%SJL' AND FILE_TYPE NOT LIKE 'USCS%' AND file_type NOT in ('MINUTE', 'SSDSP'))c "
                + "UNION ALL "
                + //                List of file signed by L&S during Approval
                "SELECT d.* FROM (SELECT ('Approval_LS') AS process_type, uf.FILE_TYPE, ('') AS status, "
                + "uf.FILE_ID, uf.FILE_NAME, uf.FILE_PATH, uf.ORIGINAL_FILE_NAME, uf.CREATED_DATE "
                + "FROM US_FILE uf WHERE uf.CASE_ID IN  ('" + caseId + "') AND CI_ID IS NULL "
                + "AND (((FILE_TYPE LIKE 'USCS%' OR file_type in ('MINUTE', 'SSDSP')) AND DESCRIPTION LIKE '%_SIGNED') OR file_type = 'SISJL') ORDER BY CREATED_DATE)d) "
                + "ORDER BY DECODE (file_group, 'APP01_SF',1, 'APP01_LS',2, 'U10_SF',3, 'U10_LS',4, 'U20_SF', 5, 'U20_LS', 6, 'Approval', 7, 'Approval_LS', 8), "
                + "created_date asc";
        
        System.out.println("strSQL_fileList " + strSQL_fileList);
        return strSQL_fileList;
        
        
    }
}
