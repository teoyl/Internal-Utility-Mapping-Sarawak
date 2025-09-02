/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.LogFunction;
import com.sains.framework.model.User;
import com.sains.workflow.web.WorkflowApiAction;
import com.utimaps.model.JobDetailModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.json.simple.JSONObject;

/**
 *
 * @author yonglai
 */
public class HardcopyJobAction extends BaseActionSupport<JobDetailModel> implements ModelDriven<JobDetailModel>{
    private Map<String, Object> jsonMap = new HashMap();
    HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);

    public HardcopyJobAction() {
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
    
    @Override
    public String loadEditPage() {
        Debug.printDebug("loadEditPage");
        try {
            setPageTitle_("USJ Application");
            setPageSubTitle_("Edit");
            model = super.processEdit(getModel());
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "HardcopyJobAction", "HardcopyJobAction", "loadEditPage");
        }
        
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }
    
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
            CommonFunction.writeLogFile(e.getStackTrace(), "HardcopyJobAction", "HardcopyJobAction", "processInsert");
            addActionMessage(getText("createFail"));
            return SystemConstants.ACTION_Status.INSERT_FAIL;
        } finally {
            surveyjobDAO.closeSession();
        }
        return returnStr;
    }
    
    public String processUpdate() {
        try {
            
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }

            addActionMessage(getText("updateSuccess"));
//        uncomment the lines below to catch BaseException
//        } catch (BaseException be){
//            addActionError(be.getMessage());
//            return SystemConstants.ACTION_Status.INSERT_FAIL;
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "HardcopyJobAction", "HardcopyJobAction", "processUpdate");
                return "edit_fail";
//        } finally {
//            closeSession();
        }

        return returnStr;
    }
    
    public String loadInitPage() {
        
        try {
            jobSearch();
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "HardcopyJobAction", "HardcopyJobAction", "loadInitPage");
        } finally {
        }
        
        return "init_hardcopy_page";
    }
    
    public String jobSearch() {
        BaseDAO dao = new BaseDAOImpl();
        org.hibernate.SQLQuery query = null;
        String strSql = "";
        
        try {
            Map sessionMap = ActionContext.getContext().getSession();
            String divAssignedList = sessionMap.get("div_assigned").toString();
            String formattedDivList = divAssignedList.replace("[", "").replace("]", "");
            formattedDivList = formattedDivList.replace(" ", "").replace("]", "");

            String[] stringArray = formattedDivList.split(",");
            List<String> divList = new ArrayList<>();
            for (String str : stringArray) {
                divList.add(str);
            }
            userDiv_ = request.getParameter("division");
            
            String usjDiv = Validator.isEmpty(request.getParameter("division")) ? divList.toString() : request.getParameter("division");
            String usjSeq = Validator.isEmpty(request.getParameter("usj_seq")) ? "" : request.getParameter("usj_seq");
            String usjYear = Validator.isEmpty(request.getParameter("usj_year")) ? "" : request.getParameter("usj_year");
            String surveyOrg = Validator.isEmpty(request.getParameter("survey_org")) ? "" : request.getParameter("survey_org");
            String locality = Validator.isEmpty(request.getParameter("location")) ? "" : request.getParameter("location");
            
            String formattedUsjDiv = usjDiv.replace("[", "").replace("]", "");
            formattedUsjDiv = formattedUsjDiv.replace(" ", "");
            Debug.printDebug("usjDiv " + formattedUsjDiv + " || usjSeq " + usjSeq + " || usjYear " + usjYear + " || surveyOrg " + surveyOrg + " || locality " + locality);
            
            List<JobDetailModel> list = new ArrayList();
            dao.setSession(baseDAO.getSession());
            strSql = "SELECT job_id, case_ref, usj_no, land_desc, pb.code_desc as co_name, TO_CHAR(date_issue, 'DD-MM-YYYY') as date_issue," +
                    " wf_status, wf_status_2, pb2.code_desc as wf_status_str, pb3.code_desc as wf_status_str_2 " +
                    "FROM US_JOBDETAIL uj " +
                    "INNER JOIN US_SURVEY_FIRM_P usfp ON uj.CASE_ID = usfp.CASE_ID " +
                    "LEFT JOIN pubcode pb ON usfp.FIRM_SOC = pb.CODE_1 AND pb.code_type = 'SOC' " +
                    "LEFT JOIN pubcode pb2 ON uj.wf_status = pb2.CODE_1 AND pb2.code_type = 'JWS' " +
                    "LEFT JOIN pubcode pb3 ON uj.wf_status_2 = pb3.CODE_1 AND pb3.code_type = 'JWS' " +
                    "WHERE uj.USJ_STATUS IN ('009', '010', '017') AND uj.WF_STATUS >= 130 AND (uj.WF_STATUS_2 IS null OR uj.WF_STATUS_2 in ('174','277') )" +
                    "AND usj_div IN (:usjDiv) " +
                    "AND usj_seq like (:usjSeq) " +
                    "AND usj_year like (:usjYear) " +
                    "AND land_desc LIKE (:locality) "+
                    "AND pb.code_desc LIKE (:surveyOrg) ";
            query = dao.getSession().createSQLQuery(strSql);
            query.setParameterList("usjDiv", formattedUsjDiv.split(","));
            query.setParameter("usjSeq", "%"+usjSeq+"%");
            query.setParameter("usjYear", "%"+usjYear+"%");
            query.setParameter("locality", "%"+locality+"%");
            query.setParameter("surveyOrg", "%"+surveyOrg+"%");
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
//            Debug.printDebug("query " + query.getQueryString());
            list = query.list();
            surveyJobList = list;
            Debug.printDebug("list " + list);
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "HardcopyJobAction", "HardcopyJobAction", "jobSearch");
            e.printStackTrace();
        } finally {
            dao.closeSession();
        }
        
        
        return "init_hardcopy_page";
    }
    
    public List<Options> get_DivisionList() {
        return commList.getAllDivisionList();
    }
    
    public void initHardcopyWorkflow() throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        String initResult = "Y";
        String submitMsg = "Success";
        JSONObject json = new JSONObject();
        
        try {
            String job_id = request.getParameter("job_id");
            Debug.printDebug("job_id " +job_id);
            Map sessionMap = ActionContext.getContext().getSession();
            User user = new User();
            user = (User) dao.getModelById(sessionMap.get("userId").toString(), User.class);
            
            if(!Validator.isEmpty(job_id)) {
                JobDetailModel jobModel = (JobDetailModel) dao.getModelByCode("job_id", job_id, new JobDetailModel());
                WorkflowApiAction wfApi = new WorkflowApiAction();
                Debug.printDebug("calling workflow - usj application id: " + jobModel.getJob_id() + " at " + new java.sql.Timestamp(System.currentTimeMillis()) );
                String wf_rtn = "";
                
                if(Validator.isEmpty(jobModel.getControl_sv_flag()) || jobModel.getControl_sv_flag().equals("N")) {
                    wf_rtn = wfApi.startUSJ004_02(jobModel.getJob_id(), user.getUs_id());
                } else {
                    if(Validator.isEmpty(jobModel.getComp_completed()) || jobModel.getComp_completed().equals("N")) {
                        wf_rtn = wfApi.startUSJ003_02(jobModel.getJob_id(), user.getUs_id());
                    } else {
                        wf_rtn = wfApi.startUSJ004_02(jobModel.getJob_id(), user.getUs_id());
                    }
                }
                Debug.printDebug("wf_rtn " + wf_rtn);
                
                if (wf_rtn.equals("success")) {
                // to update application status here
                // your code here
                
                // use a self defined updating method bcs the base update doesn't update wf_status for unknown reason 16/04/2024
//                serviceFactory.getUtilAppService().updateForWorkflow(jobModel);
                    initResult = "Y";
                    submitMsg = "Successfully start Hardcopy Submission Job!";
                } else {
                    initResult = "N";
                    submitMsg = "Hardcopy Submission Job start Failed, please try again later. Thank you";
                }

            } else {
                initResult = "N";
                submitMsg = "Survey Job not found.";
            }                    
            
        } catch (Exception e) {
            initResult = "N";
            submitMsg = "e: " + e ;
            CommonFunction.writeLogFile(e.getStackTrace(), "HardcopyJobAction", "HardcopyJobAction", "initHardcopyWorkflow");
            e.printStackTrace();
        } finally {
            dao.closeSession();
        }
        
        json.put("status", initResult);
        json.put("message", submitMsg);
        
        response.setContentType("application/json");
        response.getWriter().write(json.toString());
        
    }
    
    private List<JobDetailModel> surveyJobList;
    public List<JobDetailModel> getSurveyJobList() {
        return surveyJobList;
    }

    public void setSurveyJobList(List<JobDetailModel> surveyJobList) {
        this.surveyJobList = surveyJobList;
    }
    
    private String userDiv_;
    public String getUserDiv_() {
        return userDiv_;
    }

    public void setUserDiv_(String userDiv_) {
        this.userDiv_ = userDiv_;
    }
    
}
