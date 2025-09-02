/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web.dao;

import com.lxg.common.model.PublicUserModel;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.common.util.FileOperationUtil;
import com.sains.common.util.FtpInterface;
import com.sains.common.util.SFTPBean;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.EmailTrigger;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.model.User;
import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import com.sains.workflow.web.WorkflowApiAction;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.ChecklistItemModel;
import com.utimaps.model.ChecklistItemSetupModel;
import com.utimaps.model.ChecklistModel;
import com.utimaps.model.ChecklistSetupModel;
import com.utimaps.model.FileModel;
import com.utimaps.model.JobDetailModel;
import com.utimaps.model.NotificationPModel;
import com.utimaps.model.PaymentItemModel;
import com.utimaps.model.PaymentModel;
import com.utimaps.model.SurveyFirmPModel;
import com.utimaps.web.SurveyJobAction;
import com.utimaps.web.UtimapsAction;
import com.utimaps.web.UtimapsAction.CL_DECISION;
import com.utimaps.web.UtimapsAction.USER_GROUP;
import com.utimaps.web.UtimapsAction.WF_STATUS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyApplicationDAOImpl extends BaseDAOImpl<ApplicationPModel> implements SurveyApplicationDAO {
    
    @Override
    public synchronized ApplicationPModel updateApplication(ApplicationPModel model) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        try {
            Debug.printDebug("runnning update daoimpl~~~~~");
            dao.setSession(getSession());
            beginBatchTransaction();
            
            if(model.getChecklistModel().getCheck_id() == "" || Validator.isEmpty(model.getChecklistModel().getCheck_id())) {
                Debug.printDebug("empty check id " + model.getChecklistModel());
                
                ChecklistModel caseChecklistModel = new ChecklistModel();
                caseChecklistModel = model.getChecklistModel();
                caseChecklistModel.defaultAddProperties();
                String check_id = com.sains.framework.base.CommonFunction.getId(20);
                caseChecklistModel.setID(check_id);
                
                Map sessionMap = ActionContext.getContext().getSession();
                User user = new User();
                user = (User) dao.getModelById(sessionMap.get("userId").toString(), User.class);

                caseChecklistModel.setCheck_by_oic(user.getUs_id());
                caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());
                
                getSession().save(caseChecklistModel);
                
                model.getChecklistSetupCaseModel().getChecklistItemList().forEach((ChecklistItemSetupModel cism) -> {
//                    Debug.printDebug("csi.getChecklistResultList().size() " + cism.getChecklistResultList().size());

                    for(ChecklistItemModel cim : cism.getChecklistResultList()) {
                        try {
                            if(Validator.isEmpty(cim.getCl_id())) {
                                cim.defaultAddProperties();
                                cim.setCl_id(com.sains.framework.base.CommonFunction.getId(20));
                                cim.setCheck_id(check_id);
                                cim.setCl_status("Y");
                                
//                                if (model.getWf_status().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
//                                    if(!Validator.isEmpty(cim.getCl_result())) {
//                                        if(cim.getCl_result().equals("C")) {
//                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
//                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
//                                                if(updateFile != null) {
//                                                    updateFile.updatableColumns = new String[] {"file_id","file_status"};
//                                                    updateFile.setFile_status("Y");
//                                                    updateFile.defaultUpdateProperties();
//                                                    getSession().update(updateFile);
//                                                }
//                                            });
//                                        } else if(cim.getCl_result().equals("E")) {
//                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
//                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
//                                                if(updateFile != null) {
//                                                    updateFile.updatableColumns = new String[] {"file_id","file_status"};
//                                                    updateFile.setFile_status("N");
//                                                    updateFile.defaultUpdateProperties();
//                                                    getSession().update(updateFile);
//                                                }
//                                            });
//                                        } else if(cim.getCl_result().equals("N")) {
//                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
//                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
//                                                if(updateFile != null) {
//                                                    updateFile.updatableColumns = new String[] {"file_id","file_status"};
//                                                    updateFile.setFile_status("Y");
//                                                    updateFile.defaultUpdateProperties();
//                                                    getSession().update(updateFile);
//                                                }
//                                            });
//                                        }
//                                    }
//                                }
                                
                                getSession().save(cim);
                            } else {
                                cim.updatableColumns = new String[]{"cl_id","case_id","check_id","file_id","ci_id","cl_result","cl_status","cl_remarks","cl_file_id"};
                                cim = (ChecklistItemModel) dao.setUpdateProperties(cim, getSession());
                                cim.setCheck_id(check_id);
                                cim.setCl_status("Y");
                                
//                                if (model.getWf_status().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
//                                    if(!Validator.isEmpty(cim.getCl_result())) {
//                                        if(cim.getCl_result().equals("C")) {
//                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
//                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
//                                                if(updateFile != null) {
//                                                    updateFile.updatableColumns = new String[] {"file_id","file_status"};
//                                                    updateFile.setFile_status("Y");
//                                                    updateFile.defaultUpdateProperties();
//                                                    getSession().update(updateFile);
//                                                }
//                                            });
//                                        } else if(cim.getCl_result().equals("E")) {
//                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
//                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
//                                                if(updateFile != null) {
//                                                    updateFile.updatableColumns = new String[] {"file_id","file_status"};
//                                                    updateFile.setFile_status("N");
//                                                    updateFile.defaultUpdateProperties();
//                                                    getSession().update(updateFile);
//                                                }
//                                            });
//                                        } else if(cim.getCl_result().equals("N")) {
//                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
//                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
//                                                if(updateFile != null) {
//                                                    updateFile.updatableColumns = new String[] {"file_id","file_status"};
//                                                    updateFile.setFile_status("Y");
//                                                    updateFile.defaultUpdateProperties();
//                                                    getSession().update(updateFile);
//                                                }
//                                            });
//                                        }
//                                    }
//                                }
                                
                                getSession().update(cim);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "updateApplication");
                        }
                        
                    }
                });
            } else {
                Debug.printDebug("model.getChecklistModel() " + model.getChecklistModel().getCheck_id());

                ChecklistModel caseChecklistModel = new ChecklistModel();
                caseChecklistModel = model.getChecklistModel();
                caseChecklistModel.updatableColumns = new String[]{"check_id","case_id","check_status","check_comment_oic"};
                caseChecklistModel = (ChecklistModel) dao.setUpdateProperties(caseChecklistModel, getSession());

                Map sessionMap = ActionContext.getContext().getSession();
                User user = new User();
                user = (User) dao.getModelById(sessionMap.get("userId").toString(), User.class);

                if(model.getWf_status().equals(WF_STATUS.PENDING_FOR_TA_CHECK)) {
                    caseChecklistModel.setCheck_by_oic(user.getUs_id());
                    caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());
                } else if(model.getWf_status().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                    caseChecklistModel.setVerify_by_oic(user.getUs_id());
                    caseChecklistModel.setVerify_date_oic(DateUtil.getCurrentTimestamp());
                }
                    
                getSession().update(caseChecklistModel);

                model.getChecklistSetupCaseModel().getChecklistItemList().forEach((ChecklistItemSetupModel cism) -> {
                    for(ChecklistItemModel cim : cism.getChecklistResultList()) {
                        try {
                            if(Validator.isEmpty(cim.getCl_id())) {
                                cim.defaultAddProperties();
                                cim.setCl_id(com.sains.framework.base.CommonFunction.getId(20));
                                cim.setCl_status("Y");
                                
//                                if (model.getWf_status().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
//                                    if(!Validator.isEmpty(cim.getCl_result())) {
//                                        if(cim.getCl_result().equals("C")) {
//                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
//                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
//                                                if(updateFile != null) {
//                                                    updateFile.updatableColumns = new String[] {"file_id","file_status"};
//                                                    updateFile.setFile_status("Y");
//                                                    updateFile.defaultUpdateProperties();
//                                                    getSession().update(updateFile);
//                                                }
//                                            });
//                                        } else if(cim.getCl_result().equals("E")) {
//                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
//                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
//                                                if(updateFile != null) {
//                                                    updateFile.updatableColumns = new String[] {"file_id","file_status"};
//                                                    updateFile.setFile_status("N");
//                                                    updateFile.defaultUpdateProperties();
//                                                    getSession().update(updateFile);
//                                                }
//                                            });
//                                        } else if(cim.getCl_result().equals("N")) {
//                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
//                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
//                                                if(updateFile != null) {
//                                                    updateFile.updatableColumns = new String[] {"file_id","file_status"};
//                                                    updateFile.setFile_status("Y");
//                                                    updateFile.defaultUpdateProperties();
//                                                    getSession().update(updateFile);
//                                                }
//                                            });
//                                        }
//                                    }
//                                }
                                getSession().save(cim);
                            } else {
                                cim.updatableColumns = new String[]{"cl_id","case_id","check_id","file_id","ci_id","cl_result","cl_status","cl_remarks","cl_file_id"};
                                cim = (ChecklistItemModel) dao.setUpdateProperties(cim, getSession());
                                cim.setCl_status("Y");
                                
//                                if (model.getWf_status().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
//                                    if(!Validator.isEmpty(cim.getCl_result())) {
//                                        if(cim.getCl_result().equals("C")) {
//                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
//                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
//                                                if(updateFile != null) {
//                                                    updateFile.updatableColumns = new String[] {"file_id","file_status"};
//                                                    updateFile.setFile_status("Y");
//                                                    updateFile.defaultUpdateProperties();
//                                                    getSession().update(updateFile);
//                                                }
//                                            });
//                                        } else if(cim.getCl_result().equals("E")) {
//                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
//                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
//                                                if(updateFile != null) {
//                                                    updateFile.updatableColumns = new String[] {"file_id","file_status"};
//                                                    updateFile.setFile_status("N");
//                                                    updateFile.defaultUpdateProperties();
//                                                    getSession().update(updateFile);
//                                                }
//                                            });
//                                        } else if(cim.getCl_result().equals("N")) {
//                                            cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
//                                                FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
//                                                if(updateFile != null) {
//                                                    updateFile.updatableColumns = new String[] {"file_id","file_status"};
//                                                    updateFile.setFile_status("Y");
//                                                    updateFile.defaultUpdateProperties();
//                                                    getSession().update(updateFile);
//                                                }
//                                            });
//                                        }
//                                    }
//                                }
                                getSession().update(cim);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "updateApplication");
                        }
                        
                    }
                });
            } 
            
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "updateApplication");
        } finally {
            dao.closeSession();
            closeSession();
        }
        return model;
    }

    @Override
    public String completeApplication(ApplicationPModel model) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        String completingResult = "success";
        Map sessionMap = ActionContext.getContext().getSession();
        try {
            Debug.printDebug("runnning complete task daoimpl~~~~~");
            dao.setSession(getSession());
            beginBatchTransaction();
            if(model.getWf_status().equals(WF_STATUS.PENDING_FOR_TA_CHECK) || model.getWf_status().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                if(model.getChecklistModel().getCheck_id() == "" || Validator.isEmpty(model.getChecklistModel().getCheck_id())) {
                    Debug.printDebug("empty check id " + model.getChecklistModel());

                    ChecklistModel caseChecklistModel = new ChecklistModel();
                    caseChecklistModel = model.getChecklistModel();
                    caseChecklistModel.defaultAddProperties();
                    String check_id = com.sains.framework.base.CommonFunction.getId(20);
                    caseChecklistModel.setID(check_id);

                    User user = new User();
                    user = (User) dao.getModelById(sessionMap.get("userId").toString(), User.class);

                    caseChecklistModel.setCheck_by_oic(user.getUs_id());
                    caseChecklistModel.setCheck_date_oic(DateUtil.getCurrentTimestamp());

                    getSession().save(caseChecklistModel);

                    model.getChecklistSetupCaseModel().getChecklistItemList().forEach((ChecklistItemSetupModel cism) -> {

                        for(ChecklistItemModel cim : cism.getChecklistResultList()) {
                            try {
                                if(Validator.isEmpty(cim.getCl_id())) {
                                    cim.defaultAddProperties();
                                    cim.setCl_id(com.sains.framework.base.CommonFunction.getId(20));
                                    cim.setCl_status("Y");
                                    cim.setCheck_id(check_id);

                                    if (model.getWf_status().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                                        if(!Validator.isEmpty(cim.getCl_result())) {
                                            if(cim.getCl_result().equals("C")) {
                                                cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                    FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                    if(updateFile != null) {
                                                        updateFile.updatableColumns = new String[] {"file_id","file_status"};
                                                        updateFile.setFile_status("Y");
                                                        updateFile.defaultUpdateProperties();
                                                        getSession().update(updateFile);
                                                    }
                                                });
                                            } else if(cim.getCl_result().equals("E")) {
                                                cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                    FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                    if(updateFile != null) {
                                                        updateFile.updatableColumns = new String[] {"file_id","file_status"};
                                                        updateFile.setFile_status("N");
                                                        updateFile.defaultUpdateProperties();
                                                        getSession().update(updateFile);
                                                    }
                                                });
                                            } else if(cim.getCl_result().equals("N")) {
                                                cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                    FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                    if(updateFile != null) {
                                                        updateFile.updatableColumns = new String[] {"file_id","file_status"};
                                                        updateFile.setFile_status("Y");
                                                        updateFile.defaultUpdateProperties();
                                                        getSession().update(updateFile);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                    getSession().save(cim);
                                } else {
                                    cim.updatableColumns = new String[]{"clau_id","case_id","check_id","file_id","ci_id","cl_result","cl_status","cl_remarks","cl_file_id"};
                                    cim = (ChecklistItemModel) dao.setUpdateProperties(cim, getSession());
                                    cim.setCl_status("Y");
                                    cim.setCheck_id(check_id);

                                    if (model.getWf_status().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                                        if(!Validator.isEmpty(cim.getCl_result())) {
                                            if(cim.getCl_result().equals("C")) {
                                                cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                    FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                    if(updateFile != null) {
                                                        updateFile.updatableColumns = new String[] {"file_id","file_status"};
                                                        updateFile.setFile_status("Y");
                                                        updateFile.defaultUpdateProperties();
                                                        getSession().update(updateFile);
                                                    }
                                                });
                                            } else if(cim.getCl_result().equals("E")) {
                                                cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                    FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                    if(updateFile != null) {
                                                        updateFile.updatableColumns = new String[] {"file_id","file_status"};
                                                        updateFile.setFile_status("N");
                                                        updateFile.defaultUpdateProperties();
                                                        getSession().update(updateFile);
                                                    }
                                                });
                                            } else if(cim.getCl_result().equals("N")) {
                                                cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                    FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                    if(updateFile != null) {
                                                        updateFile.updatableColumns = new String[] {"file_id","file_status"};
                                                        updateFile.setFile_status("Y");
                                                        updateFile.defaultUpdateProperties();
                                                        getSession().update(updateFile);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                    getSession().update(cim);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "completeApplication");
                            }

                        }
                    });
                } else {
                    Debug.printDebug("model.getChecklistModel() " + model.getChecklistModel().getCheck_id());

                    ChecklistModel caseChecklistModel = new ChecklistModel();
                    caseChecklistModel = model.getChecklistModel();
                    caseChecklistModel.updatableColumns = new String[]{"check_id","case_id","check_status","check_comment_oic"};
                    caseChecklistModel = (ChecklistModel) dao.setUpdateProperties(caseChecklistModel, getSession());

                    User user = new User();
                    user = (User) dao.getModelById(sessionMap.get("userId").toString(), User.class);

                    if(model.getWf_status().equals(WF_STATUS.PENDING_FOR_TA_CHECK)) {
                        caseChecklistModel.setCheck_by_oic(caseChecklistModel.getCheck_by_oic());
                        caseChecklistModel.setCheck_date_oic(caseChecklistModel.getCheck_date_oic());
                    } else if(model.getWf_status().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                        caseChecklistModel.setVerify_by_oic(user.getUs_id());
                        caseChecklistModel.setVerify_date_oic(DateUtil.getCurrentTimestamp());
                    }
                    getSession().update(caseChecklistModel);

                    model.getChecklistSetupCaseModel().getChecklistItemList().forEach((ChecklistItemSetupModel cism) -> {

                        for(ChecklistItemModel cim : cism.getChecklistResultList()) {
                            try {
                                if(Validator.isEmpty(cim.getCl_id())) {
                                    cim.defaultAddProperties();
                                    cim.setCl_id(com.sains.framework.base.CommonFunction.getId(20));
                                    cim.setCl_status("Y");

                                    if (model.getWf_status().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                                        if(!Validator.isEmpty(cim.getCl_result())) {
                                            if(cim.getCl_result().equals("C")) {
                                                cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                    FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                    if(updateFile != null) {
                                                        updateFile.updatableColumns = new String[] {"file_id","file_status"};
                                                        updateFile.setFile_status("Y");
                                                        updateFile.defaultUpdateProperties();
                                                        getSession().update(updateFile);
                                                    }
                                                });
                                            } else if(cim.getCl_result().equals("E")) {
                                                cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                    FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                    if(updateFile != null) {
                                                        updateFile.updatableColumns = new String[] {"file_id","file_status"};
                                                        updateFile.setFile_status("N");
                                                        updateFile.defaultUpdateProperties();
                                                        getSession().update(updateFile);
                                                    }
                                                });
                                            } else if(cim.getCl_result().equals("N")) {
                                                cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                    FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                    if(updateFile != null) {
                                                        updateFile.updatableColumns = new String[] {"file_id","file_status"};
                                                        updateFile.setFile_status("Y");
                                                        updateFile.defaultUpdateProperties();
                                                        getSession().update(updateFile);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                    getSession().save(cim);
                                } else {
                                    cim.updatableColumns = new String[]{"cl_id","case_id","check_id","file_id","ci_id","cl_result","cl_status","cl_remarks","cl_file_id"};
                                    cim = (ChecklistItemModel) dao.setUpdateProperties(cim, getSession());
                                    cim.setCl_status("Y");

                                    if (model.getWf_status().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                                        if(!Validator.isEmpty(cim.getCl_result())) {
                                            if(cim.getCl_result().equals("C")) {
                                                cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                    FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                    if(updateFile != null) {
                                                        updateFile.updatableColumns = new String[] {"file_id","file_status"};
                                                        updateFile.setFile_status("Y");
                                                        updateFile.defaultUpdateProperties();
                                                        getSession().update(updateFile);
                                                    }
                                                });
                                            } else if(cim.getCl_result().equals("E")) {
                                                cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                    FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                    if(updateFile != null) {
                                                        updateFile.updatableColumns = new String[] {"file_id","file_status"};
                                                        updateFile.setFile_status("N");
                                                        updateFile.defaultUpdateProperties();
                                                        getSession().update(updateFile);
                                                    }
                                                });
                                            } else if(cim.getCl_result().equals("N")) {
                                                cism.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                                                    FileModel updateFile = (FileModel) dao.getModelById(fileModel.getFile_id(), FileModel.class);
                                                    if(updateFile != null) {
                                                        updateFile.updatableColumns = new String[] {"file_id","file_status"};
                                                        updateFile.setFile_status("Y");
                                                        updateFile.defaultUpdateProperties();
                                                        getSession().update(updateFile);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                    getSession().update(cim);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "completeApplication");
                            }

                        }
                    });
                }
            }
            
            commitBatchTransaction();
            
            if(model.get_operation().equals(ApplicationPModel.OPERATION.PROCESS_COMPLETE)) {
                if(model.getWf_status().equals(WF_STATUS.PENDING_FOR_TA_CHECK)) {
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("model.get_taskId() " + model.get_taskId());
                    Map jsonMap = wfApi.completeTask_APP1(getSession(),model.get_taskId(), model.getCase_id(), "", WF_STATUS.PENDING_FOR_TA_CHECK, WF_STATUS.TA_CHECKED_NEW_CASE ,WF_STATUS.PENDING_FOR_SS_VERIFY, "", UtimapsAction.USER_GROUP.SS);
                    String result = jsonMap.get("status").toString();
                    Debug.printDebug("result " + result);
                    completingResult = result;
                } else if (model.getWf_status().equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                    
                    ChecklistModel caseChecklistModel = new ChecklistModel();
                    caseChecklistModel = model.getChecklistModel();
                    Debug.printDebug("checklist decision " + caseChecklistModel.getCheck_status());
                    
                    SurveyJobAction sja = null;
                    sja = new SurveyJobAction();
                    sja.generateUPS10(model.getCase_id());
                    
                    if(caseChecklistModel.getCheck_status().equals(CL_DECISION.ACCEPT)) {
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Debug.printDebug("model.get_taskId() APPLICATION_PAYMENT_PENDING " + model.get_taskId());
                        
                        Map jsonMap = new HashMap();
                        System.out.println("model.getInternal_case() " + model.getInternal_case());
                        //To check if in-house application
                        if(model.getInternal_case().equals("Y")) {
                            System.out.println("run 1");
                            jsonMap = wfApi.completeTask_APP1(getSession(),model.get_taskId(), model.getCase_id(), "", WF_STATUS.PENDING_FOR_SS_VERIFY, WF_STATUS.APPLICATION_FOR_USJ_APPROVED ,WF_STATUS.APPLICATION_PAYMENT_COMPLETED,"","");
                        } else {
                            System.out.println("run 2");
                            jsonMap = wfApi.completeTask_APP1(getSession(),model.get_taskId(), model.getCase_id(), "", WF_STATUS.PENDING_FOR_SS_VERIFY, WF_STATUS.APPLICATION_FOR_USJ_APPROVED ,WF_STATUS.APPLICATION_PAYMENT_PENDING,"","");
                        }
                        
                        String result = jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        if(result.equalsIgnoreCase("success")) {
                            //to send email to surveyor after success
                            Map mailJsonMap = new HashMap();
                            AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
                            autoEmailDAO.setSession(dao.getSession());
                            AutoEmail autoEmail = null;
                            try {
                                Debug.printDebug("send submission email to public user");
                                
                                String publicUserName = "";
                                String publicUserId = "";
                                String publicUserEmail = "default@utimaps.com.my";
                                String surveyFirmUserName = "";
                                String surveyFirmEmail = "";
                                
                                ApplicationPModel appModel =  (ApplicationPModel) dao.getModelById(model.getCase_id(), ApplicationPModel.class);
                                PublicUserModel publicUser = (PublicUserModel) dao.getModelByCode("us_user_id", appModel.getApp_submit_by(), new PublicUserModel());
                                
                                if(publicUser != null) {
                                    publicUserName = publicUser.getUs_user_name();
                                    publicUserId = publicUser.getUs_user_id();
                                    publicUserEmail = publicUser.getUs_email();
                                }
                                
                                SurveyFirmPModel surveyFirm = (SurveyFirmPModel) dao.getModelByCode("case_id", appModel.getCase_id(), new SurveyFirmPModel());
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

                                NotificationPModel insertNot = new UtimapsAction().insertNotification2(dao.getSession(), model.getCase_id(), not_id, not_sender, publicUserId, not_subject, not_content, "U001", "", model.get_taskId(), "S");

                                if(insertNot == null) {
                                    Debug.printDebug("Error when insert notification, no notification is inserted." + model.getCase_id() + " - " + not_id + " - " + not_sender + " - " + publicUserId + " - " + not_subject + " - " + not_content + " - " + "U001" + " - " + " " + " - " + model.get_taskId() + " - " + "S");
                                } else {
                                    new UtimapsAction().auditAction(model.getCase_id(),dao.getSession(),not_sender,"Send Notification : " + model.getCase_id());
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
                        
                    } else if (caseChecklistModel.getCheck_status().equals(CL_DECISION.REJECT)) {
                        
                        WorkflowApiAction wfApi = new WorkflowApiAction();
                        Map jsonMap = wfApi.completeTask_APP2(getSession(),model.get_taskId(), model.getCase_id(), "", WF_STATUS.PENDING_FOR_SS_VERIFY, WF_STATUS.APPLICATION_FOR_USJ_REJECTED, WF_STATUS.ISSUE_LETTER_REJECTION,"","");
                        String result =  jsonMap.get("status").toString();
                        Debug.printDebug("result " + result);
                        completingResult = result;
                        
                        if(result.equalsIgnoreCase("success")) {
                            //to send email to surveyor after success
                            Map mailJsonMap = new HashMap();
                            AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
                            autoEmailDAO.setSession(dao.getSession());
                            FtpInterface ftp = FileOperationUtil.getFtpInterface();
                            AutoEmail autoEmail = null;
                            try {
                                Debug.printDebug("send submission email to public user");

                                String publicUserName = "";
                                String publicUserId = "";
                                String publicUserEmail = "default@utimaps.com.my";
                                String surveyFirmUserName = "";
                                String surveyFirmEmail = "";
                                
                                ApplicationPModel appModel =  (ApplicationPModel) dao.getModelById(model.getCase_id(), ApplicationPModel.class);
                                PublicUserModel publicUser = (PublicUserModel) dao.getModelByCode("us_user_id", appModel.getApp_submit_by(), new PublicUserModel());
                                
                                if(publicUser != null) {
                                    publicUserName = publicUser.getUs_user_name();
                                    publicUserId = publicUser.getUs_user_id();
                                    publicUserEmail = publicUser.getUs_email();
                                }
                                
                                SurveyFirmPModel surveyFirm = (SurveyFirmPModel) dao.getModelByCode("case_id", appModel.getCase_id(), new SurveyFirmPModel());
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
                                
                                FileModel ups10 = (FileModel) dao.getModelByCode("case_id,file_type",model.getCase_id()+",UPS10", new FileModel());
                                Debug.printDebug("ups10 " + ups10);
                                if(ups10 != null){
                                    String filePath = (ups10 != null) ? ups10.getFile_path() : ""; ;
                                    String fileExt = (ups10 != null) ? ups10.getFile_ext() : "pdf"; ;
                                    List attList = new ArrayList<Map>();

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

                                NotificationPModel insertNot = new UtimapsAction().insertNotification2(dao.getSession(), model.getCase_id(), not_id, not_sender, publicUserId, not_subject, not_content, "U001", "", model.get_taskId(), "S");

                                if(insertNot == null) {
                                    Debug.printDebug("Error when insert notification, no notification is inserted." + model.getCase_id() + " - " + not_id + " - " + not_sender + " - " + publicUserId + " - " + not_subject + " - " + not_content + " - " + "U001" + " - " + " " + " - " + model.get_taskId() + " - " + "S");
                                } else {
                                    new UtimapsAction().auditAction(model.getCase_id(),dao.getSession(),not_sender,"Send Notification : " + model.getCase_id());
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
                    
                    updateLetterVersion(model, SystemConstants.FILE_TYPE.UPS10);
                }
            } else if(model.get_operation().equals(ApplicationPModel.OPERATION.PROCESS_ROUTE_BACK_APPLICATION)) {
                WorkflowApiAction wfApi = new WorkflowApiAction();
                Map jsonMap = wfApi.completeTask_APP4(getSession(),model.get_taskId(), model.getCase_id(), "", WF_STATUS.PENDING_FOR_SS_VERIFY, WF_STATUS.APPLICATION_SS_ROUTE_BACK, WF_STATUS.PENDING_FOR_TA_CHECK,"",USER_GROUP.AS);
                String result = jsonMap.get("status").toString();
                Debug.printDebug("result " + result);
                completingResult = result;
            }

        } catch (Exception e) {
            rollbackBatchTransaction();
            completingResult = "failed";
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "completeApplication");
        } finally {
            dao.closeSession();
            closeSession();
        }
        
        return completingResult;
    }
    
    @Override
    public String deleteUPS10Letter(ApplicationPModel model) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        String completingResult = "success";
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        
        try {
            Debug.printDebug("runnning deleteUPS10Letter task daoimpl~~~~~ " + model.get_operation());
            dao.setSession(getSession());
            beginBatchTransaction();
            
            if(model.get_operation().equals(ApplicationPModel.OPERATION.DELETE_UPS10_FILE)) {
                List<FileModel> existingFileList = new ArrayList();
                Map param = new HashMap();
                param.put("case_id", model.getCase_id());
                param.put("file_type", SystemConstants.FILE_TYPE.UPS10);
                existingFileList = dao.list_order(param, FileModel.class, "");

                if(existingFileList.size() > 0) {
                    for(FileModel existingFile : existingFileList) {
                        if(existingFile != null) {
                            if (ftp.isFileExists(existingFile.getFile_path(), Boolean.FALSE)) {
                                ftp.deleteFile(existingFile.getFile_path());
                            }
                            dao.getSession().delete(existingFile);
                        }
                    }
                }
            }
            
            commitBatchTransaction();

        } catch (Exception e) {
            rollbackBatchTransaction();
            completingResult = "failed";
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "deleteUPS10Letter");
        } finally {
            dao.closeSession();
            closeSession();
        }

        return completingResult;
    }
    
    @Override
    public synchronized String clearApplicationChecklist(ApplicationPModel model) throws Exception {
        
        BaseDAOImpl dao = new BaseDAOImpl();
        try {
            Debug.printDebug("runnning checkApplicationChecklist daoimpl~~~~~");
            dao.setSession(getSession());
            beginBatchTransaction();
            
            ChecklistSetupModel checklistSetupModel = new ChecklistSetupModel();
            checklistSetupModel = model.getChecklistSetupCaseModel();

            checklistSetupModel.getChecklistItemList().forEach((ChecklistItemSetupModel csi) -> {
                Debug.printDebug("=============================================" + "size " + csi.getChecklistFileModelList().size());
                if(!csi.getChecklistFileModelList().isEmpty()) {
                    csi.getChecklistFileModelList().forEach((FileModel fileModel) -> {
                    });
                    
                    FileModel attFile = csi.getChecklistFileModelList().get(0);
                    Debug.printDebug("attFile name " + attFile.getFile_name() + " || " + attFile.getFile_status());

                    String fileStatus = attFile.getFile_status();

                    for(ChecklistItemModel cim : csi.getChecklistResultList()) {
                        try {
                            if(fileStatus.equals("P")) {
                                Debug.printDebug("fileStatus " + fileStatus);
                                cim.updatableColumns = new String[]{"cl_id","cl_result","cl_remarks"};
                                cim = (ChecklistItemModel) dao.setUpdateProperties(cim, dao.getSession());
                                cim.setCl_result("");
                                cim.setCl_remarks("");
                                getSession().update(cim);
                            }
                        } catch (Exception e) {

                        }
                    }
                }
            });
            
            if(model.getChecklistModel() != null) {
                ChecklistModel checklistModel = model.getChecklistModel();
                checklistModel.updatableColumns = new String[]{"check_id","check_status","check_comment_oic","check_date_oic","check_by_oic","comment_ss","check_by_ss","check_date_ss"};
                checklistModel.setCheck_status("");
                checklistModel.setCheck_comment_oic("");
                checklistModel.setCheck_by_oic("");
                checklistModel.setCheck_date_oic(null);
                checklistModel.setComment_ss("");
                checklistModel.setCheck_by_ss("");
                checklistModel.setCheck_date_ss(null);
                getSession().update(checklistModel);
            }
        
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "checkApplicationChecklist");
        } finally {
            dao.closeSession();
            closeSession();
        }
        
        return "";
    }
    
    public void updateLetterVersion(ApplicationPModel appModel, String fileType) throws Exception {
        
        BaseDAOImpl dao = new BaseDAOImpl();
        
        try {
            dao.setSession(getSession());
            beginBatchTransaction();
            List<FileModel> existingFileList = new ArrayList();
            Map param = new HashMap();
            param.put("case_id", appModel.getCase_id());
            param.put("file_type", fileType);
            existingFileList = dao.list_order(param, FileModel.class, "");

            if(existingFileList.size() > 0) {
                for(FileModel existingFile : existingFileList) {
                    existingFile.updatableColumns = new String[]{"job_id, file_status"};
                    existingFile = (FileModel) dao.setUpdateProperties(existingFile, dao.getSession());
                    existingFile.setFile_status("N");
                    dao.getSession().update(existingFile);
                }
            }
            
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SurveyJobAction", "SurveyApplicationDAOImpl", "updateLetterVersion");
        } finally {
            dao.closeAllSession();
        }
    }
}
