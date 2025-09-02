/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utimaps.web;

import com.ism.web.ISMWorkflowBase;
import com.ism.web.IsmWorkflowApiAction;
import com.lxg.common.model.CustCompanyModel;
import com.lxg.common.model.PublicUserModel;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.Debug;
import com.sains.framework.base.LogFunction;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.JobDetailModel;
import java.util.Map;

/**
 *
 * @author Aiman
 */
public class ISMServicesAction {

    public void triggerISMWorkflowApp(ApplicationPModel appModel, String ismStep) throws Exception {
        BaseDAO baseDAO = new BaseDAOImpl();
        Map sessionMap = ActionContext.getContext().getSession();
        new LogFunction().logDebug(this.getClass(), "\nCaseID: " + appModel.getID() + " >>> triggerISMWorkflow (" + ismStep + ") >>> sessionMap: " + sessionMap.toString() + "\n", null);

        try {
            IsmWorkflowApiAction oIsmWfApi = new IsmWorkflowApiAction();
            String returnedStatus = "";

            ApplicationPModel updateModel = (ApplicationPModel) baseDAO.getObjectById(appModel.getID(), ApplicationPModel.class);

            String ismWFCode = ISMWorkflowBase.ISM_WF_CODE.USJ_APPLY;
            String ismServiceId = ISMWorkflowBase.ISM_SERVICE_ID.USJ_APPLY;
            String ismActivityCode = ISMWorkflowBase.ISM_ACTIVITY_CODE.USJ_APPLY;

            String updateStatus = "";
            String remarksEn = "";
            String remarksBm = "";

            switch (ismStep) {
                case ISMWorkflowBase.ISM_WF_STEP.SAVE_DRAFT:
                    returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_SaveAsDraft_Workflow(ismWFCode, updateModel.getFim_user_id(), updateModel.getID(), updateModel.getCase_ref(), "submit::in_progress::complete|reject", ismServiceId, "", getCorpSwkId(updateModel.getCo_id()));
                    break;
                case ISMWorkflowBase.ISM_WF_STEP.SUBMIT:
                    if (Validator.isEmpty(appModel.getIsm_rec_id())) {
                        new LogFunction().logError(this.getClass(), "CaseID: " + appModel.getID() + " [NO ISM RECORD ID] ", null);
                        return;
                    }
                    returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_Start_Workflow(ismWFCode, updateModel.getFim_user_id(), updateModel.getID(), updateModel.getCase_ref(), updateModel.getIsm_rec_id(), ismServiceId, "", getCorpSwkId(updateModel.getCo_id()));
                    break;
                case ISMWorkflowBase.ISM_WF_STEP.UPD_QUERIED:
                    updateStatus = "in_progress";
                    remarksEn = ISMWorkflowBase.ISM_EN_REMARKS.APP_QUERIED;
                    remarksBm = "";
                    returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_Update_Progress(updateModel.getIsm_rec_id(), updateStatus, remarksEn, remarksBm);
                    break;
                case ISMWorkflowBase.ISM_WF_STEP.UPD_RESUBMIT:
                    updateStatus = "in_progress";
                    remarksEn = ISMWorkflowBase.ISM_EN_REMARKS.APP_QUERIED;
                    remarksBm = "";
                    returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_Update_Progress(updateModel.getIsm_rec_id(), updateStatus, remarksEn, remarksBm);
                    break;
                case ISMWorkflowBase.ISM_WF_STEP.UPD_INPROGRESS:
                    if (Validator.isEmpty(appModel.getIsm_rec_id())) {
                        new LogFunction().logError(this.getClass(), "CaseID: " + appModel.getID() + " [NO ISM RECORD ID] ", null);
                        return;
                    }
//
//                    PublicUserModel mWhoCreatedCase = (PublicUserModel) baseDAO.getObjectByCode("us_user_id", appModel.getCreated_by(), new PublicUserModel());
//                    if (mWhoCreatedCase == null || Validator.isEmpty(mWhoCreatedCase.getFim_user_id())) {
//                        return;
//                    }
//
//                    returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_Select("", mWhoCreatedCase.getFim_user_id(), ismServiceId, appModel.getCase_ref(), appModel.getID(), Formatter.formatDate(appModel.getCreated_date(), "yyyy-MM-dd HH:mm:ss"), "");
//
//                    new LogFunction().logDebug(this.getClass(), "CaseID: " + appModel.getID() + " >>> triggerISMWorkflow (" + ismStep + ") >>> returnedStatus #1: " + returnedStatus, null);
//
//                    if (returnedStatus.contains("success::submit")) {
//                        returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_Get_Task(appModel.getID(), ismActivityCode, Formatter.formatDate(DateUtil.getCurrentTimestamp(), "yyyy-MM-dd HH:mm:ss"));
//                    }

                    if (updateModel.getIsm_wf_inprogress().equals("Y")) {
                        updateStatus = "in_progress";
                        remarksEn = ISMWorkflowBase.ISM_EN_REMARKS.APP_PROCESSING;
                        remarksBm = "";
                        returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_Update_Progress(updateModel.getIsm_rec_id(), updateStatus, remarksEn, remarksBm);
                    } else {
                        returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_Get_Task(updateModel.getID(), ismActivityCode, Formatter.formatDate(DateUtil.getCurrentTimestamp(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    break;
                case ISMWorkflowBase.ISM_WF_STEP.COMPLETED:
                    String pCompleteRemarkEn = "Completed";
                    String pCompleteRemarkBm = "Selesai";
                    returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_Complete_Task(updateModel.getID(), ismActivityCode, "Y", "", "", pCompleteRemarkEn, pCompleteRemarkBm, Formatter.formatDate(DateUtil.getCurrentTimestamp(), "yyyy-MM-dd HH:mm:ss"));
                    break;
                default:
                    break;
            }

            new LogFunction().logDebug(this.getClass(), "CaseID: " + appModel.getID() + " >>> triggerISMWorkflow (" + ismStep + ") >>> returnedStatus: " + returnedStatus, null);
            System.out.println("returnStatus -> " + returnedStatus);
            
            // ######################################### DONE TRIGGER ISM API FOR APP -> UPDATE IN MUDB ###################################################

            if (returnedStatus.contains("success")) {
                baseDAO.beginBatchTransaction();

                if (ismStep.equals(ISMWorkflowBase.ISM_WF_STEP.SAVE_DRAFT)) {
                    String ism_rec_id = returnedStatus.substring(returnedStatus.lastIndexOf("::") + 2);
                    System.out.println("REC ID TO BE UPDATED TO APP -> " + ism_rec_id);
                    new LogFunction().logDebug(this.getClass(), "CaseID: " + appModel.getID() + " >>> triggerISMWorkflow (" + ismStep + ") >>> ism_rec_id: " + ism_rec_id, null);
                    updateModel.setIsm_rec_id(ism_rec_id);
                    System.out.println(">> UPDATED REC ID >> " + updateModel.getIsm_rec_id());
                    updateModel.updatableColumns = new String[]{"case_id", "ism_rec_id", "updated_by", "updated_date"};

                } else if (ismStep.equals(ISMWorkflowBase.ISM_WF_STEP.SUBMIT)) {
                    String ism_rec_id = returnedStatus.substring(returnedStatus.lastIndexOf("::") + 2);
                    new LogFunction().logDebug(this.getClass(), "CaseID: " + appModel.getID() + " >>> triggerISMWorkflow (" + ismStep + ") >>> ism_rec_id: " + ism_rec_id, null);
//                    appModel.setIsm_rec_id(ism_rec_id);
//                    appModel.updatableColumns = new String[]{"Case_id", "Ism_rec_id", "Ism_wf_started", "Updated_by", "Updated_date"};
                    updateModel.setIsm_wf_started("Y");
                    updateModel.updatableColumns = new String[]{"Case_id", "Ism_wf_started", "Updated_by", "Updated_date"};
                } else if (ismStep.equals(ISMWorkflowBase.ISM_WF_STEP.UPD_INPROGRESS)) {
                    if (!updateModel.getIsm_wf_inprogress().equals("Y")) {
                        Debug.printDebug("Updating inprogress -> Y");
                        updateModel.setIsm_wf_inprogress("Y");
                    }
                    updateModel.updatableColumns = new String[]{"Case_id", "Ism_wf_inprogress", "Updated_by", "Updated_date"};
                } else if (ismStep.equals(ISMWorkflowBase.ISM_WF_STEP.DEL_DRAFT)) {
//                    mCase.setCase_status(CsCaseModel.CASE_STATUS.APPLICATION_CLOSED);
//                    appModel.updatableColumns = new String[]{"Case_id", "Case_status", "Updated_by", "Updated_date"};
                }

                updateModel.setCase_id(appModel.getCase_id());
                updateModel.defaultUpdateProperties();
                baseDAO.getSession().update(updateModel);
                baseDAO.commitBatchTransaction();
            }

            new LogFunction().logDebug(this.getClass(), "CaseID: " + appModel.getID() + " >>> triggerISMWorkflow (" + ismStep + ") >>> Updated ism_rec_id...", null);

        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "CaseID: " + appModel.getID() + " >>> triggerISMWorkflow (" + ismStep + ") >>> [EXCEPTION] ", e);
            e.printStackTrace();
            throw e;
        } finally {
            baseDAO.closeSession();
        }
    }

    public void triggerISMWorkflowSub(JobDetailModel jobModel, String ismStep) throws Exception {
        BaseDAO baseDAO = new BaseDAOImpl();
        Map sessionMap = ActionContext.getContext().getSession();
        new LogFunction().logDebug(this.getClass(), "\nCaseID: " + jobModel.getID() + " >>> triggerISMWorkflow (" + ismStep + ") >>> sessionMap: " + sessionMap.toString() + "\n", null);

        try {
            IsmWorkflowApiAction oIsmWfApi = new IsmWorkflowApiAction();
            String returnedStatus = "";
            JobDetailModel updateModel = (JobDetailModel) baseDAO.getObjectById(jobModel.getID(), JobDetailModel.class);

            String ismWFCode = ISMWorkflowBase.ISM_WF_CODE.USJ_SUBMIT;
            String ismServiceId = ISMWorkflowBase.ISM_SERVICE_ID.USJ_SUBMIT;
            String ismActivityCode = ISMWorkflowBase.ISM_ACTIVITY_CODE.USJ_SUBMIT;

            String updateStatus = "";
            String remarksEn = "";
            String remarksBm = "";

            switch (ismStep) {
                case ISMWorkflowBase.ISM_WF_STEP.SAVE_DRAFT:
                    returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_SaveAsDraft_Workflow(ismWFCode, jobModel.getFim_user_id(), jobModel.getID(), jobModel.getUsj_no(), "submit::in_progress::complete|reject", ismServiceId, "", getCorpSwkId(jobModel.getCo_id()));
                    break;
                case ISMWorkflowBase.ISM_WF_STEP.SUBMIT:
                    if (Validator.isEmpty(jobModel.getIsm_rec_id())) {
                        new LogFunction().logError(this.getClass(), "JobID: " + jobModel.getID() + " [NO ISM RECORD ID] ", null);
                        return;
                    }
                    returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_Start_Workflow(ismWFCode, jobModel.getFim_user_id(), jobModel.getID(), jobModel.getUsj_no(), jobModel.getIsm_rec_id(), ismServiceId, "", getCorpSwkId(jobModel.getCo_id()));
                    break;
                case ISMWorkflowBase.ISM_WF_STEP.UPD_RESUBMIT:
                    updateStatus = "in_progress";
                    remarksEn = ISMWorkflowBase.ISM_EN_REMARKS.SUB_RESUBMITTED;
                    remarksBm = "";
                    returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_Update_Progress(jobModel.getIsm_rec_id(), updateStatus, remarksEn, remarksBm);
                    break;
                case ISMWorkflowBase.ISM_WF_STEP.UPD_QUERIED:
                    updateStatus = "in_progress";
                    remarksEn = getSubQueryEnRemark(jobModel.getUsj_status());
                    remarksBm = "";
                    returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_Update_Progress(jobModel.getIsm_rec_id(), updateStatus, remarksEn, remarksBm);
                    break;
                case ISMWorkflowBase.ISM_WF_STEP.UPD_INPROGRESS:
                    if (updateModel.getIsm_wf_inprogress().equals("Y")) {
                        updateStatus = "in_progress";
                        remarksEn = ISMWorkflowBase.ISM_EN_REMARKS.SUB_PROCESSING;
                        remarksBm = "";
                        returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_Update_Progress(updateModel.getIsm_rec_id(), updateStatus, remarksEn, remarksBm);
                    } else {
                        returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_Get_Task(jobModel.getID(), ismActivityCode, Formatter.formatDate(DateUtil.getCurrentTimestamp(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    break;
                case ISMWorkflowBase.ISM_WF_STEP.COMPLETED:
                    String pCompleteRemarkEn = "Completed";
                    String pCompleteRemarkBm = "Selesai";
                    returnedStatus = oIsmWfApi.processSXC_ISMWorkflow_Complete_Task(jobModel.getID(), ismActivityCode, "Y", "", "", pCompleteRemarkEn, pCompleteRemarkBm, Formatter.formatDate(DateUtil.getCurrentTimestamp(), "yyyy-MM-dd HH:mm:ss"));
                    break;
                default:
                    break;
            }

            new LogFunction().logDebug(this.getClass(), "JobID: " + updateModel.getID() + " >>> triggerISMWorkflow (" + ismStep + ") >>> returnedStatus: " + returnedStatus, null);

            // ######################################### DONE TRIGGER ISM API FOR JOB -> UPDATE IN MUDB ################################################### 
            
            
            if (returnedStatus.contains("success")) {
                baseDAO.beginBatchTransaction();

                if (ismStep.equals(ISMWorkflowBase.ISM_WF_STEP.SAVE_DRAFT)) {
                    String ism_rec_id = returnedStatus.substring(returnedStatus.lastIndexOf("::") + 2);
                    new LogFunction().logDebug(this.getClass(), "JobID: " + updateModel.getID() + " >>> triggerISMWorkflow (" + ismStep + ") >>> ism_rec_id: " + ism_rec_id, null);
                    System.out.println("REC ID TO BE UPDATED TO JOB -> " + ism_rec_id);
                    updateModel.setIsm_rec_id(ism_rec_id);
                    System.out.println(">> UPDATED REC ID FOR JOB >> " + updateModel.getIsm_rec_id());
                    updateModel.updatableColumns = new String[]{"Job_id", "Ism_rec_id", "Updated_by", "Updated_date"};

                } else if (ismStep.equals(ISMWorkflowBase.ISM_WF_STEP.SUBMIT)) {
                    String ism_rec_id = returnedStatus.substring(returnedStatus.lastIndexOf("::") + 2);
                    new LogFunction().logDebug(this.getClass(), "CaseID: " + updateModel.getID() + " >>> triggerISMWorkflow (" + ismStep + ") >>> ism_rec_id: " + ism_rec_id, null);
//                    updateModel.setIsm_rec_id(ism_rec_id);
//                    updateModel.updatableColumns = new String[]{"Case_id", "Ism_rec_id", "Ism_wf_started", "Updated_by", "Updated_date"};
                    updateModel.setIsm_wf_started("Y");
                    updateModel.updatableColumns = new String[]{"Job_id", "Ism_wf_started", "Updated_by", "Updated_date"};
                } else if (ismStep.equals(ISMWorkflowBase.ISM_WF_STEP.UPD_INPROGRESS)) {
//                    if (!updateModel.getIsm_wf_inprogress().equals("Y")) {
//                        Debug.printDebug("Updating inprogress -> Y");
//                    }
                    updateModel.setIsm_wf_inprogress("Y");
                    updateModel.updatableColumns = new String[]{"Job_id", "Ism_wf_inprogress", "Updated_by", "Updated_date"};
                } else if (ismStep.equals(ISMWorkflowBase.ISM_WF_STEP.DEL_DRAFT)) {
//                    mCase.setCase_status(CsCaseModel.CASE_STATUS.APPLICATION_CLOSED);
//                    updateModel.updatableColumns = new String[]{"Case_id", "Case_status", "Updated_by", "Updated_date"};
                }

                updateModel.setJob_id(updateModel.getID());
                updateModel.defaultUpdateProperties();
                baseDAO.getSession().update(updateModel);
                baseDAO.commitBatchTransaction();
            }

            new LogFunction().logDebug(this.getClass(), "JobID: " + jobModel.getID() + " >>> triggerISMWorkflow (" + ismStep + ") >>> Updated ism_rec_id...", null);

        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "JobID: " + jobModel.getID() + " >>> triggerISMWorkflow (" + ismStep + ") >>> [EXCEPTION] ", e);
            throw e;
        } finally {
            baseDAO.closeSession();
        }
    }

    // workaround since swkid corp id is not in session, 
    // use the applicant(corp)'s data from T_CUST_COMPANY
    // to retrieve the SwkID Corp ID
    public String getCorpSwkId(String co_id) throws Exception {
        BaseDAO baseDAO = new BaseDAOImpl();
        String swkId = "";
        try {
            CustCompanyModel ccModel = (CustCompanyModel) baseDAO.getObjectById(co_id, CustCompanyModel.class);
            swkId = ccModel.getSwkid_corp();

        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), " getCorpSwkId [EXCEPTION] ", e);
            throw e;
        } finally {
            baseDAO.closeSession();
        }

        return swkId;
    }

    public String getUserSwkId(String us_user_id) throws Exception {
        BaseDAO baseDAO = new BaseDAOImpl();
        String swkId = "";
        try {
//            CustCompanyModel ccModel = (CustCompanyModel) baseDAO.getObjectById(co_id, CustCompanyModel.class);
//            swkId = ccModel.getSwkid_corp();
            PublicUserModel mWhoCreatedCase = (PublicUserModel) baseDAO.getObjectByCode("us_user_id", us_user_id, new PublicUserModel());
            swkId = mWhoCreatedCase.getFim_id();

        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), " getCorpSwkId [EXCEPTION] ", e);
            throw e;
        } finally {
            baseDAO.closeSession();
        }

        return swkId;
    }

    public String getSubQueryEnRemark(String status) {
        String remark = ISMWorkflowBase.ISM_EN_REMARKS.APP_QUERIED;

        switch (status) {
            case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS10:
                remark = ISMWorkflowBase.ISM_EN_REMARKS.SUBMISSION_QUERY_USCS10;
                break;
            case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40:
                remark = ISMWorkflowBase.ISM_EN_REMARKS.SUBMISSION_QUERY_USCS40;
                break;
            case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS50:
                remark = ISMWorkflowBase.ISM_EN_REMARKS.SUBMISSION_QUERY_USCS50;
                break;
            case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS80:
                remark = ISMWorkflowBase.ISM_EN_REMARKS.SUBMISSION_QUERY_USCS80;
                break;
            case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK:
                remark = ISMWorkflowBase.ISM_EN_REMARKS.SUBMISSION_QUERY_PRECHECK;
                break;
            default:
                break;
        }

        return remark;
    }
}
