/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web;

import com.SysConf;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
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
import com.sains.framework.base.LogFunction;
import com.sains.framework.base.ModelBase;
import com.sains.framework.base.ReportGenerator;
import com.sains.framework.model.DrDocRepoModel;
import com.sains.framework.model.NotificationSetup;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.ChecklistItemModel;
import com.utimaps.model.ChecklistItemSetupModel;
import com.utimaps.model.ChecklistModel;
import com.utimaps.model.ChecklistSetupModel;
import com.utimaps.model.FileModel;
import com.utimaps.model.JobDetailModel;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.json.simple.JSONObject;

/**
 *
 * @author yonglai
 */
public class IssuanceJobAction extends BaseActionSupport<JobDetailModel> implements ModelDriven<JobDetailModel>{
    ChecklistSetupModel checklistSetupModel;
    ApplicationPModel appModel;
    public List swiperList = new ArrayList();
    public Integer swiperStep = 0;
    HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    
    
    public IssuanceJobAction() {
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
    
    public ChecklistSetupModel getChecklistSetupModel() {
        return checklistSetupModel;
    }

    public void setChecklistSetupModel(ChecklistSetupModel checklistSetupModel) {
        this.checklistSetupModel = checklistSetupModel;
    }

    public ApplicationPModel getAppModel() {
        return appModel;
    }

    public void setAppModel(ApplicationPModel appModel) {
        this.appModel = appModel;
    }
    
    @Override
    public String loadEditPage() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        BaseDAO dao = new BaseDAOImpl();
        
        try {
            dao.setSession(baseDAO.getSession());
            String job_id_=request.getParameter("jobId");
            Debug.printDebug("job_id_ " + job_id_);
            noRightUpdate = false;
            rightToUpdate = false;

            model = (JobDetailModel)dao.getModelById(job_id_, JobDetailModel.class);
            ChecklistModel checklistModel = new ChecklistModel();
            checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", "APP01,"+model.getCase_id(), new ChecklistModel());
            model.getApplicationModel().setChecklistModel(checklistModel);
            model.setChecklistModel(checklistModel);
            dao.getSession().enableFilter("caseFilter").setParameter("caseNo", model.getCase_id());
            dao.getSession().enableFilter("typeFilter").setParameter("fileType", "CM");
            dao.getSession().enableFilter("fileFilter");
            dao.getSession().enableFilter("statusFilter");
            
            checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type", "APP01", new ChecklistSetupModel());
            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                csi.getChecklistFileModelList().size();
                for(ChecklistItemModel cim : csi.getChecklistResultList()) {
                    cim.getChecklistSupportFileList().size();
                }
            });
            
            if(model.getApplicationModel().getChecklistModel() != null) {
                if(model.getApplicationModel().getChecklistModel().getUser() != null) {
                    checker_name = model.getApplicationModel().getChecklistModel().getUser().getUs_user_name();
                }
                if(model.getApplicationModel().getChecklistModel().getVerifyUser() != null) {
                    verifier_name = model.getApplicationModel().getChecklistModel().getVerifyUser().getUs_user_name();
                }
            }
            
            model.getApplicationModel().setChecklistSetupCaseModel(checklistSetupModel);
            model.setChecklistSetupCaseModel(checklistSetupModel);
            model.getApplicationModel().getUPS10List().size();
            model.getUsjLetterList().size();
            model.getSignedUsjLetterList().size();
            
            wf_step = "0";
            Debug.printDebug("model.getWf_status() " + model.getWf_status());
            wf_step = (Validator.isEmpty(model.getWf_status())) ? "1" : getText("utimaps.submission." + model.getWf_status());
            Debug.printDebug("wf_step " + wf_step);
            setSwiperStep(wf_step);
            setupSwiper("sjApplication");
            
            if(Validator.isEmpty(model.getUsj_request())) {
                NotificationSetup getRequestTemplate = new NotificationSetup();
                getRequestTemplate = (NotificationSetup) dao.getModelByCode("no_type", "USJ_JOB_INSTRUCTION_V1", new NotificationSetup());

                if(getRequestTemplate != null) {
                    getRequestTemplate.getNo_desc();
                    model.setUsj_request(getRequestTemplate.getNo_template_body());
                }
            }
            
            if(model.getSignedUsjLetterList().size() > 0) {
                noRightUpdate = true;
            }

        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "loadEditPage");
            e.printStackTrace();
        } finally {
            dao.closeSession();
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
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "processInsert");
            e.printStackTrace();
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
            Debug.printDebug("inside processUpdateIssuance");
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
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "processUpdate");
                return "edit_fail";
//        } finally {
//            closeSession();
        }

        return "return_to_edit_page";
    }
    
    public String processUpdateJob() {
        try {
            Debug.printDebug("processUpdateIssuance");
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
            
            actionName = "loadEditPageIssuance";
            id = model.getID();
            caseId_ = model.getCase_id();            
            model.set_operation(ApplicationPModel.OPERATION.PROCESS_UPDATE_ISSUANCE);
            serviceFactory.getIssuanceJobService().updateIssuance(model);
            
            addActionMessage(getText("updateSuccess"));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "processUpdateJob");
            e.printStackTrace();
            addActionError(getText("updateFail"));
            return "return_to_edit_page";
        }

        return "return_to_edit_page";
    }
    
    public String processCompleteJob() {
        String returnString = "redirect_job";
        try {
            Debug.printDebug("processCompleteIssuance");
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
            
            actionName = "loadEditPageIssuance";
            id = model.getID();
            caseId_ = model.getCase_id();
            model.set_taskId(taskId_);
            model.set_operation(ApplicationPModel.OPERATION.PROCESS_COMPLETE_ISSUANCE);
            serviceFactory.getIssuanceJobService().completeIssuance(model);
            
            addActionMessage(getText("updateSuccess"));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "processCompleteJob");
            e.printStackTrace();
            return "edit_fail";
        }

        return returnString;
    }
    
    public String processRouteBackJob() {
        try {
            Debug.printDebug("processRouteBackIssuance");
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
            
            actionName = "loadEditPageIssuance";
            id = model.getID();
            caseId_ = model.getCase_id();
            model.set_taskId(taskId_);

            model.set_operation(ApplicationPModel.OPERATION.PROCESS_ROUTE_BACK_ISSUANCE);
            serviceFactory.getIssuanceJobService().completeIssuance(model);
            
            addActionMessage(getText("updateSuccess"));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "processRouteBackJob");
            e.printStackTrace();
            return "edit_fail";
        }

        return "redirect_job";
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
                fileName = "ChecklistUPS10"+idCase+".pdf";
            } else if (rptType.equals("AppCheck_USJ")) {
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/AppCheck_USJ.jasper";
                fileName = "USJ"+idCase+".pdf";
            }
            contentDisposition = "filename="+fileName;
            jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
            reportParam.put("pCaseId", idCase.trim());
            inputStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO);
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "pdfViewer");
            e.printStackTrace();
        }
        
        return "pdfViewer";
    }
    
    public String generateLetter() throws Exception {
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
//        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        InputStream jasperRptStream = null;
        Map reportParam = new HashMap();
        BaseDAO dao = baseDAO;
        dao.setSession(baseDAO.getSession());
        String generateResult = "Y";
        String generateMsg = "Success";
        JSONObject json = new JSONObject();
        
        try {
            String sftpUploadPath = null; 
            String idCase = request.getParameter("caseId");
            String idJob = request.getParameter("jobId");
            String newDrDocId = CommonFunction.getId(20);
            String jasperFileName = "";
            String fileName = "sampple.pdf";
            FileModel uploadingModel = null;
            
            String deleteResult = "";
                        
            JobDetailModel jobModel = (JobDetailModel)dao.getModelById(idJob, JobDetailModel.class);
            jobModel.set_operation(ApplicationPModel.OPERATION.DELETE_USJ_FILE);
            deleteResult = serviceFactory.getIssuanceJobService().deleteUSJFile(jobModel);
            
            if(!Validator.isEmpty(deleteResult) && deleteResult.equals("success")) {
                
                sftpUploadPath = "USJL";
                fileName = "USJ_"+jobModel.getCase_ref().replace("/", "_")+".pdf";
                contentType = "application/pdf";
                contentDisposition = "filename="+fileName;
                jasperFileName = "/WEB-INF/classes/com/utimaps/report/AppCheck_USJ.jasper";
                jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperFileName);
                reportParam.put("pCaseId", idCase.trim());

                ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
                if (ftp.isDirExists(SysConf.get("pathPrefix") + sftpUploadPath, Boolean.FALSE)) {
                        ftp.createDirIfNotExists(sftpUploadPath + "/" + idJob, Boolean.FALSE);
                        ftp.createFile(sftpUploadPath + "/" + idJob + "/" + newDrDocId, ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, baseDAO));// use File as parameter
                        ftp.insertToFileDirectory(sftpUploadPath + "/" + idJob + "/" + newDrDocId, fileName, "application/pdf", newDrDocId, baseDAO, SystemConstants.FILE_TYPE.USJL, Boolean.FALSE);
                        Debug.printDebug("file insert directory " + sftpUploadPath + "/" + idJob + "/" );
                        if (uploadingModel != null) {
                        } else {
                            uploadingModel = new FileModel();
                            uploadingModel.setFile_id(newDrDocId);
                            uploadingModel.setFile_type(SystemConstants.FILE_TYPE.USJL);
                            uploadingModel.setFile_name(fileName);
                            uploadingModel.setOriginal_file_name(fileName);
                            uploadingModel.setFile_ext("PDF");
                            uploadingModel.setCase_id(idJob);
                            uploadingModel.setCi_id("");
                            uploadingModel.setFile_path(sftpUploadPath + "/" + idJob + "/" + newDrDocId);
                            uploadingModel.setUsj_no("-");
                            uploadingModel.setTotal_files(1);
                            uploadingModel.setTotal_gislayer(0);
                            uploadingModel.setTotal_features(0);
                            uploadingModel.defaultAddProperties();

                            try {
                                ModelBase.updateCurrentRequestObject("antiCSRF", "ignoreCSRF");//serene @ 10/8/2022
                                dao.beginBatchTransaction();
                                dao.insert(uploadingModel);
                                dao.commitBatchTransaction();
                            } catch (Exception e) {
                                dao.rollbackBatchTransaction();
                                e.printStackTrace();
                                try {
                                    ftp.deleteFile(sftpUploadPath + "/" + newDrDocId); //try to delete if the file is uploaded but record not updated.
                                } catch (Exception delEx) {
                                    delEx.printStackTrace();
                                }
                                throw e;
                            }
                            
                                json.put("fileId", uploadingModel.getFile_id());
                        }
                    }
            }
            
        } catch (BaseException be) {
            generateResult = "N";
            generateMsg = "be: " + be ;
            CommonFunction.writeLogFile(be.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "generateLetter");
        } catch (Exception e) {
            generateResult = "N";
            generateMsg = "e: " + e ;
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "generateLetter");
            e.printStackTrace();
            return "return_to_edit_page";
        } finally {
            ftp.disconnect();
            dao.closeSession();
        }
        
        json.put("status", generateResult);
        json.put("message", generateMsg);

        response.setContentType("application/json");
        response.getWriter().write(json.toString());
        
        return null;
    }
    
    public String pdfSign() throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        Integer intError = 0; 
        
        try {
            Debug.printDebug("certFile " + certFile);
            Debug.printDebug("certPassword " + certPassword);

            if(certFile == null){
                Debug.printDebug("$$$$$$$$$$$$$$ no valid cert ");
            }
            
            model.setCert_File(certFile);
            model.setCert_Password(certPassword);
            model.set_operation(ApplicationPModel.OPERATION.SIGN_USJ_LETTER);
            intError = serviceFactory.getIssuanceJobService().signUSJLetter(model);

            Debug.printDebug("---Sign batch cert intError "+intError);
            if (intError < 0) {
                if (intError == -1) {
                    addActionMessage("Sign Failed: Certificate has Expired.");
                } else if (intError == -2) {
                    addActionMessage("Sign Failed: Certificate has been Revoked.");
                } else if (intError == -3) {
                    addActionMessage("Sign Failed: Certificate is not from Valid Issuer");
                } else if (intError == -4) {
                    addActionMessage("Sign Failed: Wrong Password.");
                } else if (intError == -9) {
                    addActionMessage("Sign Failed: Contact admin for assistance.");
                } 
            }else{
                clearMessages();
                addActionMessage(getText("utimaps.sign.cert.success"));
            }
            
            actionName = "loadEditPageIssuance";
            id = model.getID();
            caseId_ = model.getCase_id();
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "pdfSign");
            e.printStackTrace();
        } finally {
            dao.closeSession();
        }
        return "return_to_edit_page";
    }
    
    public String pdfRevoke() {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        
        try {
            model.set_operation(ApplicationPModel.OPERATION.REVOKE_USJ_LETTER);
            serviceFactory.getIssuanceJobService().revokeUSJLetter(model);
            
            actionName = "loadEditPageIssuance";
            id = model.getID();
            caseId_ = model.getCase_id();
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "pdfRevoke");
        } finally {
            dao.closeSession();
        }
        
        return "return_to_edit_page";
    }
    
    public void downloadUSJLetter() throws Exception {
        BaseDAO dao = baseDAO;
        dao.setSession(baseDAO.getSession());
        String generateResult = "Y";
        String generateMsg = "Success";
        JSONObject json = new JSONObject();
        
        try {
            String idCase = request.getParameter("caseId");
            String idJob = request.getParameter("jobId");
            String fileType = request.getParameter("fileType");
            Debug.printDebug("idJob " + idJob);
            Debug.printDebug("fileType " + fileType);
            FileModel fileModel = (FileModel) dao.getModelByCode("case_id,file_type",idJob+","+fileType, new FileModel());
            String fileId = "";
            if(fileModel != null) {
                fileId = fileModel.getFile_id();   
            }
            json.put("fileId", fileId);
        } catch (Exception e) {
            generateResult = "N";
            generateMsg = "e: " + e ;
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "downloadUSJLetter");
            e.printStackTrace();
        } finally {
            dao.closeSession();
        }
        
        json.put("status", generateResult);
        json.put("message", generateMsg);

        response.setContentType("application/json");
        response.getWriter().write(json.toString());
    }
    
    final static MimetypesFileTypeMap FILE_TYPE_MAP = new MimetypesFileTypeMap();
    
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
            Debug.printDebug("contentType = " + contentType);
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "viewTempFile");
            e.printStackTrace();
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
    
    private String jobId_;
    public String getJobId_() {
        return jobId_;
    }
    
    public void setJobId_(String jobId_) {
        this.jobId_ = jobId_;
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
    
    private Boolean noRightUpdate = false;

    public Boolean getNoRightUpdate() {
        return noRightUpdate;
    }

    public void setNoRightUpdate(Boolean noRightUpdate) {
        this.noRightUpdate = noRightUpdate;
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
    
    public List<Options> getSurveyTypeList() {
        return commList.getSurveyTypeOption();
    }
    
    public List<Options> getClassificationList() {
        return commList.getClassificationList();
    }
    
    public List<Options> getBranchList() {
        return commList.getBranchList();
    }
    
    public List<Options> getQualitySurveyList() {
        return commList.getQualitySurveyList();
    }
    
    public List<Options> getRequestInstructionList() {
        return commList.getRequestInstructionOption();
    }
    
    public List<Options> getUSJYearList() {
        return commList.getCurrentYearUpToFiveYear();
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

    public Integer uploadSignedFile(InputStream inputFileStream, String fileid, String filename , String filePath, String fileType, String appId, File inputFile) {
        OBSUtil ftp = new OBSUtil();
        BaseDAO dao = new BaseDAOImpl();
        try{
            dao.setSession(baseDAO.getSession());
            ftp.createDirIfNotExists(filePath, Boolean.FALSE);
          //  String newDrDocId = CommonFunction.getId(20);
            String fileMimeType = ServletActionContext.getServletContext().getMimeType(filename);
            if(inputFileStream == null){

            }
            if(filename != null){
                if(inputFile != null){
                     if(ftp.isFileExists(filePath + "/" + fileid, Boolean.FALSE)){
                        ftp.deleteFile(filePath + "/" + fileid);
                     }
                    ftp.createFile(filePath + "/" + fileid, inputFile);

                }else if(inputFileStream !=null) {
                    try {
                       if(ftp.isFileExists(filePath + "/" + fileid, Boolean.FALSE)){
                         ftp.deleteFile(filePath + "/" + fileid);
                      }
                       ftp.createFile(filePath + "/" + fileid, inputFileStream);
                    } catch (Exception e) {
                        CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "loadEditPage");
                        e.printStackTrace();
                    }

                }else  {
                    return 1;
                }

                if(ftp.isFileExists(filePath + "/" + fileid, editMode_)){
                    //to create fileModel later
                    JobDetailModel jobDetailModel = new JobDetailModel();
                    jobDetailModel = (JobDetailModel) dao.getModelById(appId, JobDetailModel.class);
                    
                    jobDetailModel.set_operation(ApplicationPModel.OPERATION.INSERT_SIGNED_USJ);
                    serviceFactory.getIssuanceJobService().insertSignedUSJLetter(jobDetailModel, fileid, filePath, filename, "application/pdf", fileType);

                }else{
                    return 1;
                }
            }else{
                return 1;
            }

        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "uploadSignedFile");
            e.printStackTrace();
            return 1;
        }
        
        return 0;
    }
    
    public void checkSurveyJobNo() {
        
        org.hibernate.SQLQuery query = null;
        org.hibernate.SQLQuery query2 = null;
        org.hibernate.SQLQuery query3 = null;
        String strUsjSeq = request.getParameter("usj_seq");
        String strUsjYear = request.getParameter("usj_year");
        String strUsjDiv = request.getParameter("usj_div");
        String strJobId = request.getParameter("job_id");
        
        String message = "";
        Boolean flag = true;
        JSONObject json = new JSONObject();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        BaseDAO dao = new BaseDAOImpl();
        
        try{
            dao.setSession(baseDAO.getSession());
            JobDetailModel jobModel = new JobDetailModel();
            jobModel = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
            String formatted_seq = CommonFunction.prefixZeros(model.getUsj_seq(),4,false,"");
            String ori_sj_no = jobModel.getUsj_no();
            String new_sj_no = "USJ/"+jobModel.getApplication_div_display()+"/"+formatted_seq+"/"+strUsjYear;
            
            Debug.printDebug(ori_sj_no + " = " + new_sj_no);
            if(!Validator.isEmpty(new_sj_no)) {
                // Check if Survey Job No. updated or not
                if(new_sj_no.equals(ori_sj_no)) {
                } else {
                    //If Survey Job No. got updated, proceed to check duplicate 
                    if(!Validator.isEmpty(strUsjSeq) && !Validator.isEmpty(strUsjYear)) {
                        String strSql = "SELECT * FROM SCS_JOB" + strUsjDiv + " WHERE JOB_NUMBER = ?";
                        String strSql2 = "SELECT * FROM SCJOBPROG" + strUsjDiv + " WHERE JOB_NUMBER = ?";
                        String strSql3 = "SELECT * FROM T_ESUBPS_JOBDETAIL WHERE JOB_ID = ? AND DIVS = ?";
                        
                        query = baseDAO.getSession().createSQLQuery(strSql);
                        query.setString(1, strUsjYear + formatted_seq);
                        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
                        List list = query.list();
                        Debug.printDebug("result :"+list.size());

                        query2 = baseDAO.getSession().createSQLQuery(strSql2);
                        query2.setString(1, strUsjYear + formatted_seq);
                        query2.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
                        List list2 = query2.list();
                        Debug.printDebug("result2 :"+list2.size());

                        query3 = baseDAO.getSession().createSQLQuery(strSql3);
                        query3.setString(1, strUsjYear + formatted_seq);
                        query3.setString(2, strUsjDiv);
                        query3.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
                        List list3 = query3.list();
                        Debug.printDebug("result3 :"+list3.size());

                        if (list.size() > 0) {
                            flag= false;
                            message = "Failed! Survey Job No. <span class='text-danger'>" + formatted_seq + "/" + strUsjYear + "</span> already exist in LASIS MSDB";
                        }

                        if (list2.size() > 0) {
                            flag= false;
                            message = "Failed! Survey Job No. <span class='text-danger'>" + formatted_seq + "/" + strUsjYear + "</span> already exist in LASIS MSDB";
                        }

                        if (list3.size() > 0) {
                            flag= false;
                            message = "Failed! Survey Job No. <span class='text-danger'>" + formatted_seq + "/" + strUsjYear + "</span> already exist in eLASIS - eSubmission";
                        }
                    } else{
                    }
                }
            }
            
            Debug.printDebug("" + flag);
            json.put("status", flag);
            json.put("message", message);
            response.setContentType("application/json");
            response.getWriter().write(json.toString());
            
        } catch (Exception e) {
            try{
                flag= false;
                message = "Connection Error. Please contact Administrative.";

                json.put("status", flag);
                json.put("message", message);
                response.setContentType("application/json");
                response.getWriter().write(json.toString());
            }catch(Exception ex){
                CommonFunction.writeLogFile(ex.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "checkSurveyJobNo");
                e.printStackTrace();
            }
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "checkSurveyJobNo");
        }
    }
    
    public void getInstructionTemplate() {
        
        String message = "";
        String template = "";
        Boolean flag = true;
        JSONObject json = new JSONObject();
        BaseDAO dao = new BaseDAOImpl();
        
        try{
            dao.setSession(baseDAO.getSession());
            String instruction_type = request.getParameter("instruction_type");
            Debug.printDebug("instruction_type " + instruction_type);
            
            if(!Validator.isEmpty(instruction_type)) {
                NotificationSetup getRequestTemplate = new NotificationSetup();
                getRequestTemplate = (NotificationSetup) dao.getModelByCode("no_type", instruction_type, new NotificationSetup());

                if(getRequestTemplate != null) {
                    template = getRequestTemplate.getNo_template_body();
                    flag= true;
                    message = "Template found.";
                } else {
                    flag= false;
                    message = "Template not found.";
                }
                
            } else{
            }
            
            json.put("status", flag);
            json.put("message", message);
            json.put("template", template);
            response.setContentType("application/json");
            response.getWriter().write(json.toString());
            
        } catch (Exception e) {
            try{
                flag= false;
                message = "Connection Error. Please contact Administrative.";

                json.put("status", flag);
                json.put("message", message);
                json.put("template", template);
                response.setContentType("application/json");
                response.getWriter().write(json.toString());
            }catch(Exception ex){
                CommonFunction.writeLogFile(ex.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "getInstructionTemplate");
                ex.printStackTrace();
            }
            CommonFunction.writeLogFile(e.getStackTrace(), "IssuanceJobAction", "IssuanceJobAction", "getInstructionTemplate");
        }
    }
}
