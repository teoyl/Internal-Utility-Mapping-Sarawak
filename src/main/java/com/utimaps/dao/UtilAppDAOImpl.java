/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.dao;

import com.sains.common.util.DateUtil;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.JobDetailModel;
import com.utimaps.model.SurveyFirmPModel;
import com.utimaps.web.UtimapsAction;

/**
 *
 * @author Aiman
 */
public class UtilAppDAOImpl extends BaseDAOImpl<ApplicationPModel> implements UtilAppDAO {

    public synchronized void manualInsert(ApplicationPModel model, String pbUserId) {
        CommonFunction.writeFile("UtilAppDAO", "MANUAL INSERT DAO - " + new java.sql.Timestamp(System.currentTimeMillis()));
        BaseDAOImpl dao = new BaseDAOImpl();
        try {
            beginBatchTransaction();
            dao.setSession(getSession());
            CommonFunction.writeFile("UtilAppDAO", model.getCase_id());
//            model.setCase_id(model.getCase_id());
            model.setCreated_date(DateUtil.getCurrentTimestamp());
            model.setCreated_by(pbUserId);
            model.setUpdated_date(DateUtil.getCurrentTimestamp());
            model.setUpdated_by(pbUserId);
            model.setApp_submit_by(pbUserId);
            model.setApp_status(UtimapsAction.PB_STATUS.APPLICATION_SAVE);
            model.setWf_status(UtimapsAction.WF_STATUS.NEW);
            getSession().save(model);

//            SurveyFirmPModel newFirm = new SurveyFirmPModel();
//            newFirm.setSo_app_id(com.sains.framework.base.CommonFunction.getId(20));
//            newFirm.setCase_id(model.getCase_id());
//            newFirm.setCreated_date(DateUtil.getCurrentTimestamp());
//            newFirm.setCreated_by(pbUserId);
//            newFirm.setUpdated_date(DateUtil.getCurrentTimestamp());
//            newFirm.setUpdated_by(pbUserId);
//            getSession().save(newFirm);
            commitBatchTransaction();

        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "manualInsert");
        } finally {
            dao.closeSession();
            closeSession();
        }
    }

    public synchronized void updateForWorkflow(ApplicationPModel model, String subType) {
        CommonFunction.writeFile("UtilAppDAO", "WF UPDATE DAO - " + new java.sql.Timestamp(System.currentTimeMillis()));
        BaseDAOImpl dao = new BaseDAOImpl();
        try {
            beginBatchTransaction();
            dao.setSession(getSession());

            ApplicationPModel updateModel = (ApplicationPModel) dao.getModelByCode("case_id", model.getCase_id(), new ApplicationPModel());

            switch (subType) {
                case UtimapsAction.DOC_TYPE_CODE_1.APP:
                    updateModel.setApp_acknowledge("Y");
                    updateModel.setApp_submit_date(DateUtil.getCurrentTimestamp());
                    updateModel.setWf_status(UtimapsAction.WF_STATUS.PENDING_FOR_TA_CHECK);
                    updateModel.setApp_status(UtimapsAction.PB_STATUS.APPLICATION_SUB);
                    break;
                case UtimapsAction.DOC_TYPE_CODE_1.USJ:
                    //comment as no update applicationPModel 
//                    updateModel.setApp_status(UtimapsAction.PB_STATUS.SUBMISSION_SUB);
//                    updateModel.setWf_status(UtimapsAction.WF_STATUS.CHECK_U10_PENDING);
//                    updateModel.setUpdated_date(DateUtil.getCurrentTimestamp());

                    JobDetailModel updateJobModel = (JobDetailModel) dao.getModelByCode("case_id", model.getCase_id(), new JobDetailModel());

                    if (updateJobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS10) || updateJobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40) || updateJobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK)) {
                        updateJobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_RESUBMIT_HARDCOPY);
                    } else {
                        updateJobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_PENDING_HARDCOPY);
                    }

                    updateJobModel.setUpdated_date(DateUtil.getCurrentTimestamp());
                    updateJobModel.setUsj_submission_date(DateUtil.getCurrentTimestamp());
                    updateJobModel.setWf_status(UtimapsAction.WF_STATUS.CHECK_U10_PENDING);
                    updateJobModel.setWf_status_2("");
                    getSession().update(updateJobModel);
//                    updateJobModel.setUsj_status("");
                    break;
            }

            getSession().update(updateModel);
            commitBatchTransaction();

        } catch (Exception e) {
            rollbackBatchTransaction();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "updateForWorkflow");
            e.printStackTrace();
        } finally {
            dao.closeSession();
            closeSession();
        }
    }

    public synchronized void updateForPayment(ApplicationPModel model) {
        CommonFunction.writeFile("UtilAppDAO", "PYMT UPDATE DAO - " + new java.sql.Timestamp(System.currentTimeMillis()));
        BaseDAOImpl dao = new BaseDAOImpl();
        try {
            beginBatchTransaction();
            dao.setSession(getSession());

            ApplicationPModel updateModel = (ApplicationPModel) dao.getModelByCode("case_id", model.getCase_id(), new ApplicationPModel());
//            updateModel.setApp_acknowledge("Y");
//            updateModel.setApp_submit_date(DateUtil.getCurrentTimestamp());
            updateModel.setWf_status(UtimapsAction.WF_STATUS.APPLICATION_PAYMENT_COMPLETED);
            updateModel.setApp_status(UtimapsAction.PB_STATUS.APP_PAYMENT_COMPLETED);
            updateModel.setUpdated_date(DateUtil.getCurrentTimestamp());
            getSession().update(updateModel);
            commitBatchTransaction();

        } catch (Exception e) {
            rollbackBatchTransaction();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "updateForPayment");
            e.printStackTrace();
        } finally {
            dao.closeSession();
            closeSession();
        }
    }

//    public synchronized ApplicationPModel updateForApplication(ApplicationPModel model) throws Exception {
//        ApplicationPModel updateModel = new ApplicationPModel();
//        return updateModel;
//    }
}
