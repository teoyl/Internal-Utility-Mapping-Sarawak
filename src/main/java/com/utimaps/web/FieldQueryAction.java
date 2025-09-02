/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web;

import com.Decoder;
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
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.model.User;
import com.sains.framework.sam.dao.UserGroupDAOImpl;
import com.sains.workflow.web.WorkflowApiAction;
import com.utimaps.model.JobDetailModel;
import com.utimaps.web.UtimapsAction.USER_GROUP;
import com.utimaps.web.UtimapsAction.WF_STATUS;
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
public class FieldQueryAction extends BaseActionSupport<JobDetailModel> implements ModelDriven<JobDetailModel>{
    private Map<String, Object> jsonMap = new HashMap();
    HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    
    public FieldQueryAction() {
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
            CommonFunction.writeLogFile(e.getStackTrace(), "FieldQueryAction", "FieldQueryAction", "loadEditPage");
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
            CommonFunction.writeLogFile(e.getStackTrace(), "FieldQueryAction", "FieldQueryAction", "processInsert");
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
            CommonFunction.writeLogFile(e.getStackTrace(), "FieldQueryAction", "FieldQueryAction", "processUpdate");
                return "edit_fail";
//        } finally {
//            closeSession();
        }

        return returnStr;
    }
    
    public String loadInitPage() {
        System.out.println(" " + Decoder.encode("mudbuser"));
        try {
            jobSearch();
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "FieldQueryAction", "FieldQueryAction", "loadInitPage");
        } finally {
        }
        
        return "init_query_page";
    }
    
    public String loadDetailPage() {
        BaseDAO dao = new BaseDAOImpl();
        
        try {
            dao.setSession(baseDAO.getSession());
            model = (JobDetailModel)dao.getModelById(id, JobDetailModel.class);
            
            Map sessionMap = ActionContext.getContext().getSession();
            String userLoginId = sessionMap.get("loginId").toString();
            
            if(model != null) {
                getAssigneeList(model.getUsj_div(), userLoginId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "FieldQueryAction", "FieldQueryAction", "loadDetailPage");
        }
        
        return "detail_query_page";
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
                    "WHERE uj.WF_STATUS_2 IN ('204','205','220','221')" +
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
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "FieldQueryAction", "FieldQueryAction", "jobSearch");
        } finally {
            dao.closeSession();
        }
        
        
        return "init_query_page";
    }
    
    public void initWorkflow() throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        String initResult = "Y";
        String submitMsg = "Success";
        JSONObject json = new JSONObject();
        
        try {
            String job_id = request.getParameter("job_id");
            
            Debug.printDebug("job_id " +job_id);
            Debug.printDebug("assignTo__ " +assignTo__);
            Map sessionMap = ActionContext.getContext().getSession();
            User user = new User();
            user = (User) dao.getModelById(sessionMap.get("userId").toString(), User.class);
            String assignedGroup = "";
            String assignedUser = "";
            boolean canAssign = true;
            
            if(!Validator.isEmpty(assignTo__)) {
                GroupUser gu = (GroupUser) dao.getModelById(assignTo__, GroupUser.class);
                String ug_id = gu.getUg_id();
                String us_id = gu.getUs_id();
                
                SetupGroup ug = (SetupGroup) dao.getModelById(ug_id, SetupGroup.class);
                User us = (User) dao.getModelById(us_id, User.class);
                if(ug != null) {
                    assignedUser = us.getUs_user_id();
                    if(ug.getGroup_code().startsWith("USJ_AS2_")) {
                        assignedGroup = USER_GROUP.AS;
                    } else if(ug.getGroup_code().startsWith("USJ_TA_")) {
                        assignedGroup = USER_GROUP.TA;
                    } else {
                        canAssign = false;
                        initResult = "N";
                        submitMsg = "Failed to start Field Query Job start! Invalid user found.";
                    }
                }
            } else {
                canAssign = false;
                initResult = "N";
                submitMsg = "Failed to start Field Query Job start! No user found.";
            }
            
            Debug.printDebug("assignedUser " + assignedUser);
            Debug.printDebug("assignedGroup " + assignedGroup);
            
            if(canAssign) {
                if(!Validator.isEmpty(job_id)) {
                    JobDetailModel jobModel = (JobDetailModel) dao.getModelByCode("job_id", job_id, new JobDetailModel());
                    String result = "";
                    String wf_rtn = "";
                    WorkflowApiAction wfApi = new WorkflowApiAction();
                    Debug.printDebug("calling workflow - usj job id: " + jobModel.getJob_id() + " at " + new java.sql.Timestamp(System.currentTimeMillis()) );
                    Map wf_rtn_map = new HashMap();
                    
                    if(jobModel.getWf_status_2().equals(WF_STATUS.CHECK_U40_PENDING)) {
                        wf_rtn_map = wfApi.completeTask_SUB3(dao.getSession(), "USJ003_04_02", jobModel.getJob_id(), WF_STATUS.CHECK_U40_APPROVED, "");
                        Debug.printDebug("wf_rtn_map " + wf_rtn_map);
                        result = wf_rtn_map.get("status").toString();
                    } else {
                        result = "success";
                    }
                        
                    if (result.equalsIgnoreCase("success")) {
                        
                        wf_rtn = wfApi.startUSJ003_03(jobModel.getJob_id(), user.getUs_id(), assignedGroup, assignedUser);
                        Debug.printDebug("wf_rtn " + wf_rtn);

                        if (wf_rtn.equals("success")) {
                            initResult = "Y";
                            submitMsg = "Successfully start Field Query Job!";
                        } else {
                            initResult = "N";
                            submitMsg = "Field Query Job start Failed, please contact administrator. Thank you";
                        }
                    } else {
                        initResult = "N";
                        submitMsg = wf_rtn_map.get("errMsg").toString();
                    }
                } else {
                    initResult = "N";
                    submitMsg = "Survey Job not found.";
                }
            }
            
        } catch (Exception e) {
            initResult = "N";
            submitMsg = "e: " + e ;
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "FieldQueryAction", "FieldQueryAction", "initWorkflow");
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

    public List<Options> get_DivisionList() {
        return commList.getAllDivisionList();
    }
    
    private List assigneeList;
    public List getAssigneeList() {
        return assigneeList;
    }

    public void setAssigneeList(List assigneeList) {
        this.assigneeList = assigneeList;
    }
    
    public List getAssigneeList(String div, String userLoginId) {
        BaseDAO dao = new BaseDAOImpl();
        List<Map<String, Object>> list = new ArrayList();
        List<Map<String, Object>> list2 = new ArrayList();

        try {
            org.hibernate.SQLQuery query = null;
            org.hibernate.SQLQuery query2 = null;
            String strSql = "";
            String strSql2 = "";
            boolean isTA = false;
            
            dao.setSession(baseDAO.getSession());
            strSql = "SELECT tsgu.UG_USER_ID, tsu.US_ID, tsu.US_USER_ID, tsu.US_USER_NAME, tsu.US_EMAIL, tsg.GROUP_CODE, tsg.GROUP_NAME from T_SETUP_GROUP tsg " +
                "INNER JOIN T_SETUP_GROUP_USER tsgu ON tsg.UG_ID = tsgu.UG_ID " +
                "INNER JOIN T_SETUP_USER tsu ON tsu.US_ID = tsgu.US_ID " +
                "WHERE tsg.group_code IN (:group_code)" + 
                "ORDER BY tsg.GROUP_NAME, tsu.US_USER_NAME";
            query = dao.getSession().createSQLQuery(strSql);
            String groupCode = "USJ_STA_"+div+",USJ_TA_"+div;
            query.setParameterList("group_code", groupCode.split(","));
            
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            list = query.list();
            
            strSql2 = "SELECT tsgu.UG_USER_ID, tsu.US_ID, tsu.US_USER_ID, tsu.US_USER_NAME, tsu.US_EMAIL, tsg.GROUP_CODE, tsg.GROUP_NAME from T_SETUP_GROUP tsg " +
                "INNER JOIN T_SETUP_GROUP_USER tsgu ON tsg.UG_ID = tsgu.UG_ID " +
                "INNER JOIN T_SETUP_USER tsu ON tsu.US_ID = tsgu.US_ID " +
                "WHERE tsu.US_USER_ID IN (:us_user_id)" + 
                "ORDER BY tsg.GROUP_NAME, tsu.US_USER_NAME";
            
            query2 = dao.getSession().createSQLQuery(strSql2);
            query2.setParameter("us_user_id", userLoginId);
            
            query2.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            list2 = query2.list();
            
            Map taMap = new HashMap();
            for (Map<String, Object> map : list2) {
                String ugUserId = (String) map.get("UG_USER_ID");
                String usUserName = (String) map.get("US_USER_NAME");
                String usEmail = (String) map.get("US_EMAIL");
                String ugName = (String) map.get("GROUP_NAME");
                String ugCode = (String) map.get("GROUP_CODE");

                if(ugCode.startsWith("USJ_TA_")) {
                    isTA = true;
                    taMap.put("email", usEmail);
                    taMap.put("userName", ugName + " - " + usUserName);
                    taMap.put("userId", ugUserId);
                    break;
                }
            }
            
            assigneeList = new ArrayList();
            Map defaultMap = new HashMap();
            if(isTA) {
                assigneeList.add(taMap);
            } else {
                defaultMap.put("email", "");
                defaultMap.put("userName", "--- Please Select ---");
                defaultMap.put("userId", "");
                assigneeList.add(defaultMap);
                for (Map<String, Object> map : list) {
                    String ugUserId = (String) map.get("UG_USER_ID");
                    String usUserName = (String) map.get("US_USER_NAME");
                    String usEmail = (String) map.get("US_EMAIL");
                    String ugName = (String) map.get("GROUP_NAME");

                    Map dataMap = new HashMap();
                    map.get("a");
                    dataMap.put("email", usEmail);
                    dataMap.put("userName", ugName + " - " + usUserName);
                    dataMap.put("userId", ugUserId);
                    assigneeList.add(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "FieldQueryAction", "FieldQueryAction", "getAssigneeList");
        }
        return assigneeList;
    }
    
    private String id = "";
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    private String assignTo__;
    public String getAssignTo__() {
        return assignTo__;
    }
    public void setAssignTo__(String assignTo__) {
        this.assignTo__ = assignTo__;
    }
    
    public void checkWorkflowBefore() throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        String initResult = "N";
        String submitMsg = "Success";
        JSONObject json = new JSONObject();
        
        try {
            String job_id = request.getParameter("job_id");
            
            Map sessionMap = ActionContext.getContext().getSession();
            User user = new User();
            user = (User) dao.getModelById(sessionMap.get("userId").toString(), User.class);
            String assignedGroup = "";
            String assignedUser = "";
            boolean canAssign = true;
            JobDetailModel jobModel = (JobDetailModel) dao.getModelByCode("job_id", job_id, new JobDetailModel());

            if(jobModel.getWf_status_2().equals(WF_STATUS.CHECK_U40_PENDING)) {
                initResult = "Y";
                submitMsg = getText("utimaps.alert.FieldQuery");
            }
            
        } catch (Exception e) {
            initResult = "N";
            submitMsg = "e: " + e ;
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "FieldQueryAction", "FieldQueryAction", "initWorkflow");
        } finally {
            dao.closeSession();
        }
        
        json.put("status", initResult);
        json.put("message", submitMsg);
        
        response.setContentType("application/json");
        response.getWriter().write(json.toString());
    }
}
