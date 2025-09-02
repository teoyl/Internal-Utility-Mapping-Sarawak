
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package com.sains.workflow.web;

import com.google.gson.Gson;
import com.ism.web.ISMWorkflowBase;
import com.lxg.common.model.Pubcode;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseAction;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.LogFunction;
import com.sains.framework.base.ServiceFactory;
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.model.User;
import com.sains.framework.sam.dao.UserGroupDAOImpl;
import com.sains.workflow.util.RouteUtil;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.ChecklistModel;
import com.utimaps.model.ChecklistSetupModel;
import com.utimaps.model.PaymentItemModel;
import com.utimaps.model.PaymentModel;
import com.utimaps.model.JobDetailModel;
import com.utimaps.model.JobStatusModel;
import com.utimaps.model.ProcessingHistoryModel;
import com.utimaps.model.SurveyFirmPModel;
import com.utimaps.web.ISMServicesAction;
import com.utimaps.web.SubmissionAction;
import com.utimaps.web.UtimapsAction;
import com.utimaps.web.UtimapsAction.PB_STATUS;
import com.utimaps.web.UtimapsAction.USER_GROUP;
import com.utimaps.web.UtimapsAction.WF_STATUS;
import com.utimaps.web.UtimapsAction.WF_STEP;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author 
 */
public class WorkflowApiAction extends BaseAction{
    private static final long serialVersionUID = -6659925652584240539L;
    public List<Pubcode> divList = new ArrayList();
    BaseDAO retrivalDAO = baseDAO;
    protected Boolean appendEmpty = Boolean.FALSE;
    protected Boolean appendSelect = Boolean.FALSE;
    private String code1Filter = "";
    private List publicCodeList = new ArrayList();
    public String divDisString = "";
    public String antiCSRF = "";
    public List<Pubcode> disList = new ArrayList();
    public Map paramMap = new HashMap();
    protected ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private CommonFunction cf = new CommonFunction();

    String wfGroupId = null;
    public String getWfGroupId() {
        return wfGroupId;
    }
    public void setWfGroupId(String wfGroupId) {
        this.wfGroupId = wfGroupId;
    }

    private String userIdInGroup = null;
    public String getUserIdInGroup() {
        return userIdInGroup;
    }
    public void setUserIdInGroup(String userIdInGroup) {
        this.userIdInGroup = userIdInGroup;
    }

    /**
     * This method will return the user group's user list base on the wfGroupId, it will filter to specific user in the group if 
     * userIdInGroup is not null.
     * @return
     * @throws Exception 
     */
    
    public String getGroupInfo() throws Exception {
        Debug.printDebug("To get user infor for wfGroupId=" + wfGroupId + ", userIdInGroup=" + userIdInGroup); 
        CommonFunction.writeFile("WorkflowApiLog", "To get user infor for wfGroupId=" + wfGroupId + ", userIdInGroup=" + userIdInGroup); 
        
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String rrUserGroup = request.getParameter("rrUserGroup");
        String strCaseId = request.getParameter("caseId");
        Debug.printDebug("@@!!## rrUserGroup :"+rrUserGroup +" || caseId :"+strCaseId);
        CommonFunction.writeFile("WorkflowApiLog", "@@!!## rrUserGroup :"+rrUserGroup +" || caseId :"+strCaseId);
        UserGroupDAOImpl userGroupDAO = new UserGroupDAOImpl();
        Map jsonMap = userGroupDAO.getGroupInfo(wfGroupId, userIdInGroup, baseDAO.getSession());
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.setContentType("application/json");
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
        return null;
    }
    
    String dataName = null;
    public String getDataName() {
        return dataName;
    }
    
    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
    
    public String getInfo() throws Exception {
        Debug.printDebug("calling getInfo()@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        CommonFunction.writeFile("WorkflowApiLog", "calling getInfo()@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Map jsonMap = new HashMap();
        Debug.printDebug("dataName in getInfo " + dataName);
        CommonFunction.writeFile("WorkflowApiLog", "dataName in getInfo " + dataName);
        if (dataName == null) {
            jsonMap.put("status", "fail");
            jsonMap.put("errMsg", "dataName is required");
        } else {
            jsonMap.put("status", "success"); //set status to success first;
            if (dataName.equals("extraReceipient")) {
                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                String userId = request.getParameter("userId");
                if (request.getParameter("ccList") != null) { //this line shows that the respective system can (try to) get any additional parameters needed
                    List ccList = new ArrayList();
                    if (request.getParameter("ccList").equals("supervisor")) {
                        ccList.add("Supervisor of "+ userId +" <thensw@sains.com.my>");
                        jsonMap.put("ccList", ccList);
                    } else if (request.getParameter("ccList").equals("sectionHead")) {
                        ccList.add("SectionHead of "+ userId +" <thensw@sains.com.my>");
                        jsonMap.put("ccList", ccList);
                    }
                }
            } else if (dataName.equals("checkSomething2")) {
                jsonMap.put("callBackStatus", "Fail");
                jsonMap.put("msg", "Something not completed");
            } else if (dataName.equals("checkSomething")) {
                jsonMap.put("callBackStatus", "Pass");
                jsonMap.put("status", "Everything completed");
            } else if (dataName.equals("checkFileUploaded_no")) {
                jsonMap.put("status", "no");
            } else if (dataName.equals("checkFileUploaded_yes")) {
                jsonMap.put("status", "yes");
            } else if (dataName.equals("test2Pass")) {
                jsonMap.put("subject", "Test 2 had passed");
                jsonMap.put("content", "This is the content of PASS Email");
            } else if (dataName.equals("test2Fail")) {
                jsonMap.put("subject", "Test 2 had failed");
                jsonMap.put("content", "This is the content of FAIL Email");
            } else if (dataName.equals("requester")) {
                jsonMap.put("email", "thensw@sains.com.my");
                jsonMap.put("userName", "Requester Name");
            } else if (dataName.equals("holiday")) {
                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                jsonMap.put("holiday", new CommonFunction().getHoliday(baseDAO.getSession(), 
                        dateFormatter.format(formatter.parse(request.getParameter("startDate"))), 
                        dateFormatter.format(formatter.parse(request.getParameter("endDate"))) ));
            } else if (dataName.equals("dueDate")) {
                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                Debug.printDebug("request.getParameter(\"activityDuration\") = " + request.getParameter("activityDuration"));
                CommonFunction.writeFile("WorkflowApiLog", "request.getParameter(\"activityDuration\") = " + request.getParameter("activityDuration"));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                jsonMap.put("dueDate", formatter.format(new CommonFunction().getJobNextWorkingDay(baseDAO.getSession(), 
                        DateUtil.getTimestampFromDate(formatter.parse(request.getParameter("startDate"))), 
                        request.getParameter("activityDuration"))));
            } else if(dataName.equals("rrUser")){ 
                Debug.printDebug("*****rrUser*****");
                CommonFunction.writeFile("WorkflowApiLog", "*****rrUser*****");
                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                String rrUserGroup = request.getParameter("rrUserGroup");
                String strCaseId = request.getParameter("caseId");
                Debug.printDebug("!!!!!!!!!!!!!!!!!!!!!!!!rrUserGroup :"+rrUserGroup +" || caseId :"+strCaseId);
                CommonFunction.writeFile("WorkflowApiLog", "!!!!!!!!!!!!!!!!!!!!!!!!rrUserGroup :"+rrUserGroup +" || caseId :"+strCaseId);
                String assignTo = "";
                Map dataMap = new HashMap();
                boolean checkRrUser = true;
                
                if(checkRrUser){    
                
                if(rrUserGroup != null){
                    request.setAttribute("ignoreCsrfCheck", "true");
                    SetupGroup sg = new SetupGroup();
                    sg = (SetupGroup)baseDAO.getObjectByCode("group_code", rrUserGroup, new SetupGroup());
                    
                    if(sg != null){
                        if(sg.getGroupUser().size() > 0){
                            Debug.printDebug("sg.getGroupUser().size() :"+sg.getGroupUser().size());
                            CommonFunction.writeFile("WorkflowApiLog", "sg.getGroupUser().size() :"+sg.getGroupUser().size());
                            if(sg.getGroupUser().size()==1){
                                for (GroupUser gu : sg.getGroupUser()) {
                                    User user = (User) baseDAO.getObjectById(gu.getUs_id(), User.class);
                                    assignTo = user.getUs_id();
                                    dataMap.put("email", user.getUs_email());
                                    dataMap.put("userName", user.getUs_user_name());
                                    dataMap.put("userId", user.getUs_user_id());
                                    jsonMap.put("status", "success");
                                    jsonMap.put("user", dataMap);
                                }
                            }else{ // More than one user in the userGroup
                                int rrUserSeq = 1;
                                int nextSeq = 1;
                                boolean blnNotAvailable = false;
                                for (GroupUser gu : sg.getGroupUser()) {
                                    if (gu.getNext_action_officer().equals("Y")) {
                                        if(gu.getOn_duty().equals("Y")){
                                            Debug.printDebug("next action officer is available");
                                            CommonFunction.writeFile("WorkflowApiLog", "next action officer is available");
                                            rrUserSeq = gu.getSeq();
                                            User user = (User) baseDAO.getObjectById(gu.getUs_id(), User.class);
                                            assignTo = user.getUs_id();
                                            dataMap.put("email", user.getUs_email());
                                            dataMap.put("userName", user.getUs_user_name());
                                            dataMap.put("userId", user.getUs_user_id());
                                            jsonMap.put("status", "success");
                                            jsonMap.put("user", dataMap);

                                            gu.setNext_action_officer("N");
                                            gu.defaultUpdateProperties();
                                            baseDAO.update(gu);
                                            Debug.printDebug("set this oic to N");
                                            CommonFunction.writeFile("WorkflowApiLog", "set this oic to N");
                                        }else{ // next action officer is not available
                                            blnNotAvailable = true;
                                            rrUserSeq = gu.getSeq();
                                        }
                                 
                                    }
                                }
                                if(blnNotAvailable){
                                    //get available officer list
                                    Map param = new HashMap();
                                    param.put("ug_id", sg.getUg_id());
                                    param.put("on_duty", "Y");
//                                    param.put("seq", rrUserSeq+1);
                                    List availableList = (List<GroupUser>) baseDAO.list_order(param, GroupUser.class, "order by seq asc");
                                     
                                    if(availableList.size() > 0){
                                        if(availableList.size()==1){
                                            for(GroupUser gUser : (List<GroupUser>) baseDAO.list_order(param, GroupUser.class, "order by seq asc")){
                                                rrUserSeq = gUser.getSeq();
                                                User user = (User) baseDAO.getObjectById(gUser.getUs_id(), User.class);
                                                assignTo = user.getUs_id();
                                                dataMap.put("email", user.getUs_email());
                                                dataMap.put("userName", user.getUs_user_name());
                                                dataMap.put("userId", user.getUs_user_id());
                                                jsonMap.put("status", "success");
                                                jsonMap.put("user", dataMap);

                                                gUser.setNext_action_officer("N");
                                                gUser.defaultUpdateProperties();
                                                baseDAO.update(gUser);
                                            }
                                        }else{
                                            for(GroupUser gUser : (List<GroupUser>) baseDAO.list_order(param, GroupUser.class, "order by seq asc")){
                                                    rrUserSeq = gUser.getSeq();
                                                    User user = (User) baseDAO.getObjectById(gUser.getUs_id(), User.class);
                                                    assignTo = user.getUs_id();
                                                    dataMap.put("email", user.getUs_email());
                                                    dataMap.put("userName", user.getUs_user_name());
                                                    dataMap.put("userId", user.getUs_user_id());
                                                    jsonMap.put("status", "success");
                                                    jsonMap.put("user", dataMap);

                                                    gUser.setNext_action_officer("N");
                                                    gUser.defaultUpdateProperties();
                                                    baseDAO.update(gUser);
//                                                }
                                            }
                                        }
                                    }else{ //all not available
                                        for (GroupUser gu : sg.getGroupUser()) {
                                            if (gu.getNext_action_officer().equals("Y")) {
                                                rrUserSeq = gu.getSeq();
                                                User user = (User) baseDAO.getObjectById(gu.getUs_id(), User.class);
                                                assignTo = user.getUs_id();
                                                dataMap.put("email", user.getUs_email());
                                                dataMap.put("userName", user.getUs_user_name());
                                                dataMap.put("userId", user.getUs_user_id());
                                                jsonMap.put("status", "success");
                                                jsonMap.put("user", dataMap);

                                                gu.setNext_action_officer("N");
                                                gu.defaultUpdateProperties();
                                                baseDAO.update(gu);

                                            }
                                        }
                                    }
                                }
                                GroupUser nextUser = (GroupUser)baseDAO.getObjectByCode("seq,ug_id", rrUserSeq+1+","+sg.getUg_id(), new GroupUser());
                                boolean checkLatestNext = false;
                                if(nextUser != null){
                                    if(nextUser.getOn_duty().equals("N")){ // Not available
                                        checkLatestNext = true;
                                    }else{
                                        nextUser.setNext_action_officer("Y");
                                        nextUser.defaultUpdateProperties();
                                        baseDAO.update(nextUser);
                                    }
                                }else{
                                    rrUserSeq = 1;
                                    nextUser = (GroupUser)baseDAO.getObjectByCode("seq,ug_id", rrUserSeq+","+sg.getUg_id(), new GroupUser());
                                    if(nextUser.getOn_duty().equals("N")){ 
                                        checkLatestNext = true;
                                    }else{
                                        nextUser.setNext_action_officer("Y");
                                        nextUser.defaultUpdateProperties();
                                        baseDAO.update(nextUser);
                                    }                                    
                                }
                                
                                if(checkLatestNext){
                                    Debug.printDebug("check latest next oic");
                                    CommonFunction.writeFile("WorkflowApiLog", "check latest next oic");
                                   int nextNextSeq = rrUserSeq+1;
                                   //get available officer list
                                    Map param = new HashMap();
                                    param.put("ug_id", sg.getUg_id());
                                    param.put("on_duty", "Y");
//                                    param.put("seq", rrUserSeq+1);
                                    List availableList = (List<GroupUser>) baseDAO.list_order(param, GroupUser.class, "order by seq asc");
                                     
                                    if(availableList.size() > 0){
                                        if(availableList.size()==1){
                                            for(GroupUser gUser : (List<GroupUser>) baseDAO.list_order(param, GroupUser.class, "order by seq asc")){
                                                gUser.setNext_action_officer("Y");
                                                gUser.defaultUpdateProperties();
                                                baseDAO.update(gUser);
                                            }
                                        }else{
//                                            boolean checkMinNo = false;
                                            if(availableList.size() > nextNextSeq){
                                                for(GroupUser gUser : (List<GroupUser>) baseDAO.list_order(param, GroupUser.class, "order by seq asc ")){
                                                    int loop = 1;
                                                    if(loop==1){
                                                        gUser.setNext_action_officer("Y");
                                                        gUser.defaultUpdateProperties();
                                                        baseDAO.update(gUser);
                                                    }
                                                    loop++;
                                                }
                                            }else{
                                                boolean checkNext = true;
                                                for (GroupUser gUser : (List<GroupUser>) baseDAO.list_order(param, GroupUser.class, "order by seq asc")) {
                                                    if(nextNextSeq == gUser.getSeq()){
                                                        gUser.setNext_action_officer("Y");
                                                        gUser.defaultUpdateProperties();
                                                        baseDAO.update(gUser);
                                                        checkNext = false;
                                                    }else{
//                                                        checkNext = true;
                                                    }                                               
                                                }
                                                if(checkNext){
                                                    int loop = 1;
                                                    for (GroupUser gUser : (List<GroupUser>) baseDAO.list_order(param, GroupUser.class, "order by seq asc ")) {
                                                        if (loop == 1) {
                                                            gUser.setNext_action_officer("Y");
                                                            gUser.defaultUpdateProperties();
                                                            baseDAO.update(gUser);
                                                        }
                                                        loop++;
                                                    }
                                                }
                                                
                                            }
                                        }
                                    }else{ //all not available
                                        
                                    }     
                                }
                            }
                        }
                    }else{
                        jsonMap.put("status", "fail");
                        jsonMap.put("errMsg", "rrUserGroup ["+rrUserGroup+"] does not exist");
                    }
                }
            }
            }else {
                jsonMap.put("status", "fail"); //if reach here.... meaning the extra infor to get not yet coded or supported
                jsonMap.put("errMsg", "Fail to get info for dataName = ["+ dataName +"]");
            }
        }
        response.setContentType("application/json");
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
        return null;
    }
    
    String wfUserId = null;
    public String getWfUserId() {
        return wfUserId;
    }
    public void setWfUserId(String wfUserId) {
        this.wfUserId = wfUserId;
    }
    
    public String getUserInfo() throws Exception {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Map jsonMap = new HashMap();
        Map dataMap = new HashMap();
        Debug.printDebug("wfUserId "+ wfUserId);
        CommonFunction.writeFile("WorkflowApiLog", "wfUserId "+ wfUserId);
        if (!Validator.isEmpty(wfUserId)) {
            Query query = baseDAO.getSession().createQuery("select user from User user where user.us_status = 'Y' and user.us_user_id = :us_user_id")
                    .setString("us_user_id", wfUserId);
            jsonMap.put("status", "success");
            List tempList = query.list();
            if (tempList.isEmpty()) {
                jsonMap.put("errMsg", "User not found for wfUserId = ["+ wfUserId +"]");
                jsonMap.put("status", "fail");
                jsonMap.put("user", "");
            } else {
                User user = (User) tempList.get(0);
                dataMap.put("email", user.getUs_email());
                dataMap.put("userName", user.getUs_user_name());
                dataMap.put("userId", user.getUs_user_id());
                jsonMap.put("status", "success");
                jsonMap.put("user", dataMap);
            }
        } else {
            jsonMap.put("errMsg", "Please supply wfUserId");
            jsonMap.put("status", "fail");
            jsonMap.put("user", "");
        }
        response.setContentType("application/json");
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
        return null;
    }

    private String isPublic_ = "N"; //Added by ChangMH @ 01-Jul-2019 :: For public login
    public String getIsPublic_() {
        return isPublic_;
    }
    public void setIsPublic_(String isPublic_) {
        this.isPublic_ = isPublic_;
    }
    
    public void setInternalStatusBackend(Session session, ApplicationPModel appModel, JobDetailModel jobModel) {
        try {
            Debug.printDebug("setInternalStatusBackend ^@@@@@@@@@^");
            CommonFunction.writeFile("WorkflowApiLog", "setInternalStatusBackend ^@@@@@@@@@^");

            paramMap.clear();
            paramMap.put("case_id", jobModel.getJob_id());
            List<JobStatusModel> statusOverallList = new ArrayList();
            Debug.printDebug("paramMap1 " + paramMap);
            CommonFunction.writeFile("WorkflowApiLog", "paramMap1 " + paramMap);
            statusOverallList = baseDAO.list_order(paramMap, JobStatusModel.class, true, "order by status_date desc" );
            Integer totalSize = 0;
            if(!statusOverallList.isEmpty()) {
                totalSize = statusOverallList.size();
            }
            
            paramMap.clear();
            paramMap.put("case_id", jobModel.getJob_id());
            paramMap.put("status_code", UtimapsAction.WF_STATUS.PREPARE_SJI_PENDING);
            List<JobStatusModel> statusList = new ArrayList();
            statusList = baseDAO.list_order(paramMap, JobStatusModel.class, true, "order by status_date desc" );
            Debug.printDebug("paramMap2 " + paramMap);
            CommonFunction.writeFile("WorkflowApiLog", "paramMap2 " + paramMap);
  
            if(statusList.size() > 0) {
                for(JobStatusModel status: statusList){
                    status.updatableColumns=new String[]{"case_id","cur_status"};
                    status.setCur_status("Y");
                    getDaoService_().updateWithSession(session, status);
                }
            }
            
            //            arine@04/04/2025::insert task_assign_to for report purpose
            RouteUtil routeUtil = new RouteUtil();
            Map currentOicMap = routeUtil.getCurrentOIC(appModel.getCase_id());
            List<Map<String, Object>> currentTaskOicList = ((List) currentOicMap.get("taskList"));
            
            JobStatusModel statusModel = new JobStatusModel();
            statusModel.setStatus_id(com.sains.framework.base.CommonFunction.getId(20));
            statusModel.setCase_id(jobModel.getJob_id());
            statusModel.setStatus_code(UtimapsAction.WF_STATUS.PREPARE_SJI_PENDING);
            
            statusModel.setStatus_date(DateUtil.getCurrentTimestamp());
            statusModel.setCreated_date(DateUtil.getCurrentTimestamp());
            statusModel.setUpdated_date(DateUtil.getCurrentTimestamp());
            
            statusModel.setStatus_by("BACKEND");
            statusModel.setCreated_by("BACKEND");
            statusModel.setUpdated_by("BACKEND");
            
            if (currentTaskOicList != null && !currentTaskOicList.isEmpty()) {
                statusModel.setTask_assign_to(currentTaskOicList.get(0).get("taskDoer").toString());
            } else {
                statusModel.setTask_assign_to("");
            }
            
            statusModel.setCur_status("N");
            statusModel.setStatus_seq(totalSize+1);
            
            getDaoService_().insertWithSession(session, statusModel);
            Debug.printDebug("setInternalStatusBackend ^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + statusModel.getStatus_code());
            CommonFunction.writeFile("WorkflowApiLog", "setInternalStatusBackend ^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + statusModel.getStatus_code());
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "setInternalStatusBackend");
        }
    }
    
    public void setInternalStatus(Session session, ApplicationPModel appModel ,String strStatus, String nxtStatus, String strWf_status_remark) {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();  
        BaseDAO dao = baseDAO;
        try {
            Debug.printDebug("setInternalStatus ^@@@@@@@@@^");
            CommonFunction.writeFile("WorkflowApiLog", "setInternalStatus ^@@@@@@@@@^");
            request.setAttribute("ignoreCsrfCheck", "true");
            
            paramMap.clear();
            paramMap.put("case_id", appModel.getCase_id());
            List<JobStatusModel> statusOverallList = new ArrayList();
            Debug.printDebug("paramMap1 " + paramMap);
            CommonFunction.writeFile("WorkflowApiLog", "paramMap1 " + paramMap);
            statusOverallList = baseDAO.list_order(paramMap, JobStatusModel.class, true, "order by status_date desc" );
            Integer totalSize = 0;
            if(!statusOverallList.isEmpty()) {
                totalSize = statusOverallList.size();
            }
            
            paramMap.clear();
            paramMap.put("case_id", appModel.getCase_id());
            paramMap.put("status_code", strStatus);
            List<JobStatusModel> statusList = new ArrayList();
            statusList = baseDAO.list_order(paramMap, JobStatusModel.class, true, "order by status_date desc" );
            Debug.printDebug("paramMap2 " + paramMap);
            CommonFunction.writeFile("WorkflowApiLog", "paramMap2 " + paramMap);
  
            if(statusList.size() > 0) {
                for(JobStatusModel status: statusList){
                    status.updatableColumns=new String[]{"case_id","cur_status"};
                    status.setCur_status("Y");
                    getDaoService_().updateWithSession(session, status);
                }
            }

//            arine@04/04/2025::insert task_assign_to for report purpose
            RouteUtil routeUtil = new RouteUtil();
            Map currentOicMap = routeUtil.getCurrentOIC(appModel.getCase_id());
            List<Map<String, Object>> currentTaskOicList = ((List) currentOicMap.get("taskList"));
            
            JobStatusModel statusModel = new JobStatusModel();
            statusModel.setStatus_id(com.sains.framework.base.CommonFunction.getId(20));
            statusModel.setCase_id(appModel.getCase_id());
            statusModel.setStatus_code(nxtStatus);
            
            statusModel.setStatus_date(DateUtil.getCurrentTimestamp());
            statusModel.setCreated_date(DateUtil.getCurrentTimestamp());
            statusModel.setUpdated_date(DateUtil.getCurrentTimestamp());
            
            if(sessionMap.isEmpty()) {
                statusModel.setStatus_by("BACKEND");
                statusModel.setCreated_by("BACKEND");
                statusModel.setUpdated_by("BACKEND");
            } else {
                statusModel.setStatus_by(sessionMap.get("loginId").toString());
                statusModel.setCreated_by(sessionMap.get("loginId").toString());
                statusModel.setUpdated_by(sessionMap.get("loginId").toString());
            }
            
            if (currentTaskOicList != null && !currentTaskOicList.isEmpty()) {
                statusModel.setTask_assign_to(currentTaskOicList.get(0).get("taskDoer").toString());
            } else {
                statusModel.setTask_assign_to("");
            }
            statusModel.setCur_status("N");
            
            statusModel.setStatus_seq(totalSize+1);
            
            getDaoService_().insertWithSession(session, statusModel);
            Debug.printDebug("setInternalStatus ^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + statusModel.getStatus_code());
            CommonFunction.writeFile("WorkflowApiLog", "setInternalStatus ^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + statusModel.getStatus_code());
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "setInternalStatus");
        }
    }

    public void setInternalSubStatus(Session session, JobDetailModel appModel ,String strStatus, String nxtStatus, String strWf_status_remark) {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();  

        try {
            Debug.printDebug("setInternalStatus ^@@@@@@@@@^");
            CommonFunction.writeFile("WorkflowApiLog", "setInternalStatus ^@@@@@@@@@^");
            request.setAttribute("ignoreCsrfCheck", "true");
            
            String loginId = "BACKEND";
            if(sessionMap.get("loginId") != null) {
                loginId = sessionMap.get("loginId").toString();
            }
            
            paramMap.clear();
            paramMap.put("case_id", appModel.getJob_id());
            List<JobStatusModel> statusOverallList = new ArrayList();
            Debug.printDebug("paramMap1 " + paramMap);
            CommonFunction.writeFile("WorkflowApiLog", "paramMap1 " + paramMap);
            statusOverallList = baseDAO.list_order(paramMap, JobStatusModel.class, true, "order by status_date desc" );
            
            Integer totalSize = 0;
            if(!statusOverallList.isEmpty()) {
                totalSize = statusOverallList.size();
            }
            
            paramMap.clear();
            paramMap.put("case_id", appModel.getJob_id());
            paramMap.put("status_code", strStatus);
            List<JobStatusModel> statusList = new ArrayList();
            statusList = baseDAO.list_order(paramMap, JobStatusModel.class, true, "order by status_date desc" );
            Debug.printDebug("paramMap2 " + paramMap);
            CommonFunction.writeFile("WorkflowApiLog", "paramMap2 " + paramMap);
  
            if(statusList.size() > 0) {
                for(JobStatusModel status: statusList){
                    status.updatableColumns=new String[]{"case_id","cur_status","status_by"};
                    status.setCur_status("Y");
                    status.setStatus_by(loginId);
                    getDaoService_().updateWithSession(session, status);
                }
            }
            
            Debug.printDebug("nxtStatus === " + nxtStatus);
            CommonFunction.writeFile("WorkflowApiLog", "nxtStatus === " + nxtStatus);
            if(!Validator.isEmpty(nxtStatus)) {
                //            arine@04/04/2025::insert task_assign_to for report purpose
                RouteUtil routeUtil = new RouteUtil();
                Map currentOicMap = routeUtil.getCurrentOIC(appModel.getCase_id());
                List<Map<String, Object>> currentTaskOicList = ((List) currentOicMap.get("taskList"));
            
                JobStatusModel statusModel = new JobStatusModel();
                statusModel.setStatus_id(com.sains.framework.base.CommonFunction.getId(20));
                statusModel.setCase_id(appModel.getJob_id());
                statusModel.setStatus_code(nxtStatus);

                statusModel.setStatus_date(DateUtil.getCurrentTimestamp());
                statusModel.setCreated_date(DateUtil.getCurrentTimestamp());
                statusModel.setUpdated_date(DateUtil.getCurrentTimestamp());

                statusModel.setStatus_by(loginId);
                statusModel.setCreated_by(loginId);
                statusModel.setUpdated_by(loginId);
                
                if (currentTaskOicList != null && !currentTaskOicList.isEmpty()) {
                    statusModel.setTask_assign_to(currentTaskOicList.get(0).get("taskDoer").toString());
                } else {
                    statusModel.setTask_assign_to("");
                }
                
                statusModel.setCur_status("N");
                statusModel.setStatus_seq(totalSize+1);

                getDaoService_().insertWithSession(session, statusModel);
                Debug.printDebug("setInternalSubStatus ^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + statusModel.getStatus_id() + " || " + statusModel.getStatus_code());
                CommonFunction.writeFile("WorkflowApiLog", "setInternalSubStatus ^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + statusModel.getStatus_id() + " || " + statusModel.getStatus_code());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "setInternalSubStatus");
        }
    }
    
    public void setInternalSubStatusBackend(Session session, JobDetailModel appModel ,String strStatus, String nxtStatus, String strWf_status_remark) {
        try {
            Debug.printDebug("setInternalSubStatusBackend ^@@@@@@@@@^");
            CommonFunction.writeFile("WorkflowApiLog", "setInternalSubStatusBackend ^@@@@@@@@@^");

            paramMap.clear();
            paramMap.put("case_id", appModel.getJob_id());
            List<JobStatusModel> statusOverallList = new ArrayList();
            Debug.printDebug("paramMap1 " + paramMap);
            CommonFunction.writeFile("WorkflowApiLog", "paramMap1 " + paramMap);
            statusOverallList = baseDAO.list_order(paramMap, JobStatusModel.class, true, "order by status_date desc" );

            Integer totalSize = 0;
            if(!statusOverallList.isEmpty()) {
                totalSize = statusOverallList.size();
            }
            
            paramMap.clear();
            paramMap.put("case_id", appModel.getJob_id());
            paramMap.put("status_code", strStatus);
            List<JobStatusModel> statusList = new ArrayList();
            statusList = baseDAO.list_order(paramMap, JobStatusModel.class, true, "order by status_date desc" );
            Debug.printDebug("paramMap2 " + paramMap);
            CommonFunction.writeFile("WorkflowApiLog", "paramMap2 " + paramMap);
            
            if(statusList.size() > 0) {
                for(JobStatusModel status: statusList){
                    status.updatableColumns=new String[]{"case_id","cur_status","status_by"};
                    status.setCur_status("Y");
                    status.setStatus_by("BACKEND");
                    getDaoService_().updateWithSession(session, status);
                }
            }
            
            Debug.printDebug("nxtStatus === " + nxtStatus);
            CommonFunction.writeFile("WorkflowApiLog", "nxtStatus === " + nxtStatus);
            if(!Validator.isEmpty(nxtStatus)) {
                //            arine@04/04/2025::insert task_assign_to for report purpose
                RouteUtil routeUtil = new RouteUtil();
                Map currentOicMap = routeUtil.getCurrentOIC(appModel.getCase_id());
                List<Map<String, Object>> currentTaskOicList = ((List) currentOicMap.get("taskList"));
            
                JobStatusModel statusModel = new JobStatusModel();
                statusModel.setStatus_id(com.sains.framework.base.CommonFunction.getId(20));
                statusModel.setCase_id(appModel.getJob_id());
                statusModel.setStatus_code(nxtStatus);

                statusModel.setStatus_date(DateUtil.getCurrentTimestamp());
                statusModel.setCreated_date(DateUtil.getCurrentTimestamp());
                statusModel.setUpdated_date(DateUtil.getCurrentTimestamp());

                statusModel.setStatus_by("BACKEND");
                statusModel.setCreated_by("BACKEND");
                statusModel.setUpdated_by("BACKEND");
                
                if (currentTaskOicList != null && !currentTaskOicList.isEmpty()) {
                    statusModel.setTask_assign_to(currentTaskOicList.get(0).get("taskDoer").toString());
                } else {
                    statusModel.setTask_assign_to("");
                }
                
                statusModel.setCur_status("N");
                statusModel.setStatus_seq(totalSize+1);

                getDaoService_().insertWithSession(session, statusModel);
                Debug.printDebug("setInternalSubStatusBackend ^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + statusModel.getStatus_id() + " || " + statusModel.getStatus_code());
                CommonFunction.writeFile("WorkflowApiLog", "setInternalSubStatusBackend ^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + statusModel.getStatus_id() + " || " + statusModel.getStatus_code());
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "setInternalSubStatusBackend");
        }
    }
    
    public void insertProcessingHistory(Session session, String caseId ,String strStatus, String nxtStatus, String strWf_status_remark) {
        SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();  
        try {
            
            String loginId = "BACKEND";
            if(sessionMap.get("loginId") != null) {
                loginId = sessionMap.get("loginId").toString();
            }
            Debug.printDebug("loginId insertProcessingHistory " + loginId);
            CommonFunction.writeFile("WorkflowApiLog", "loginId insertProcessingHistory " + loginId);
            
            paramMap.clear();
            paramMap.put("case_id", caseId);
            List<ProcessingHistoryModel> statusOverallList = new ArrayList();
            Debug.printDebug("paramMap1 " + paramMap);
            CommonFunction.writeFile("WorkflowApiLog", "paramMap1 " + paramMap);
            statusOverallList = baseDAO.list_order(paramMap, ProcessingHistoryModel.class, true, "order by process_date desc" );
            
            Integer totalSize = 0;
            if(!statusOverallList.isEmpty()) {
                totalSize = statusOverallList.size();
            }
            
            ProcessingHistoryModel historyModel = new ProcessingHistoryModel();
            historyModel.setHistory_id(com.sains.framework.base.CommonFunction.getId(20));
            historyModel.setCase_id(caseId);
            historyModel.setProcess_code(nxtStatus);
             
           historyModel.setProcess_date(DateUtil.getCurrentTimestamp());
            historyModel.setCreated_date(DateUtil.getCurrentTimestamp());
            historyModel.setUpdated_date(DateUtil.getCurrentTimestamp());
            
            if(sessionMap == null) {
                historyModel.setProcess_by("BACKEND");
                historyModel.setCreated_by("BACKEND");
                historyModel.setUpdated_by("BACKEND");
            } else {
                historyModel.setProcess_by(loginId);
                historyModel.setCreated_by(loginId);
                historyModel.setUpdated_by(loginId);
            }
            
            historyModel.setTask_assign_to("");
            historyModel.setCur_status("Y");
            historyModel.setProcess_seq(totalSize+1);
            
            getDaoService_().insertWithSession(session, historyModel);
            Debug.printDebug("insertProcessingHistory ^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + historyModel.getHistory_id());
            CommonFunction.writeFile("WorkflowApiLog", "insertProcessingHistory ^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + historyModel.getHistory_id());
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "insertProcessingHistory");
        }
    }
    
    public void insertProcessingHistoryBackend(Session session, String caseId ,String strStatus, String nxtStatus, String strWf_status_remark) {
        try {
            
            paramMap.clear();
            paramMap.put("case_id", caseId);
            List<ProcessingHistoryModel> statusOverallList = new ArrayList();
            Debug.printDebug("paramMap1 " + paramMap);
            CommonFunction.writeFile("WorkflowApiLog", "paramMap1 " + paramMap);
            statusOverallList = baseDAO.list_order(paramMap, ProcessingHistoryModel.class, true, "order by process_date desc" );
            
            Integer totalSize = 0;
            if(!statusOverallList.isEmpty()) {
                totalSize = statusOverallList.size();
            }
            
            ProcessingHistoryModel historyModel = new ProcessingHistoryModel();
            historyModel.setHistory_id(com.sains.framework.base.CommonFunction.getId(20));
            historyModel.setCase_id(caseId);
            historyModel.setProcess_code(nxtStatus);
            
            historyModel.setProcess_date(DateUtil.getCurrentTimestamp());
            historyModel.setCreated_date(DateUtil.getCurrentTimestamp());
            historyModel.setUpdated_date(DateUtil.getCurrentTimestamp());
            
            historyModel.setProcess_by("BACKEND");
            historyModel.setCreated_by("BACKEND");
            historyModel.setUpdated_by("BACKEND");
            
            historyModel.setTask_assign_to("");
            historyModel.setCur_status("Y");
            historyModel.setProcess_seq(totalSize+1);
            
            getDaoService_().insertWithSession(session, historyModel);
            Debug.printDebug("insertProcessingHistory ^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + historyModel.getHistory_id());
            CommonFunction.writeFile("WorkflowApiLog", "insertProcessingHistory ^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + historyModel.getHistory_id());
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "insertProcessingHistoryBackend");
        }
    }
    
    public String startUSJ001(String strCaseId, String triggerBy) throws Exception {
        Debug.printDebug("*******@@@@@@@@@@@@*******WFI.startUSJ001 called from public submission***********************************");
        CommonFunction.writeFile("WorkflowApiLog", "*******@@@@@@@@@@@@*******WFI.startUSJ001 called from public submission***********************************");
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        RouteUtil routeUtil = new RouteUtil();
        BaseDAO dao = baseDAO;
        ApplicationPModel appPbModel = (ApplicationPModel) dao.getModelById(strCaseId, ApplicationPModel.class);
        Debug.printDebug("strCaseId:: " + strCaseId + ", appPbModel.getID() " + appPbModel.getID());
        CommonFunction.writeFile("WorkflowApiLog", "strCaseId:: " + strCaseId + ", appPbModel.getID() " + appPbModel.getID());
        Map jsonMap = null;
        String taskAssignTo = checkPreviousTaskDoer(appPbModel.getCase_id(), appPbModel.getJob_id(), USER_GROUP.AS);
        
        jsonMap = routeUtil.startUSJ001(appPbModel.getCase_id(), appPbModel.getCase_div(), appPbModel.getPj_name(), "-", appPbModel.getCase_type(), appPbModel.getCase_ref(), triggerBy, taskAssignTo, triggerBy);

        Debug.printDebug("jsonMap " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap " + jsonMap);
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            Debug.printDebug("sucess startUSJ001");
            CommonFunction.writeFile("WorkflowApiLog", "sucess startUSJ001");
            appPbModel.setWf_status(UtimapsAction.WF_STATUS.PENDING_FOR_TA_CHECK);
            try {
                dao.beginBatchTransaction();
                dao.getSession().update(appPbModel);
                
                setInternalStatus(dao.getSession(), appPbModel , "", UtimapsAction.WF_STATUS.PENDING_FOR_TA_CHECK, "");
                insertProcessingHistory(dao.getSession(), appPbModel.getCase_id() ,"", appPbModel.getWf_status(), "");
                dao.commitBatchTransaction();
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
                e.printStackTrace();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> startUSJ001 :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "startUSJ001");
            }finally{
                dao.closeSession();
            }
            return jsonMap.get("status").toString();
        }else{
            return jsonMap.get("errMsg").toString();
        }
    }

    public Map completeTask_APP1(Session session, String strTaskId, String strCaseId, String strCode, String strCaseStatus, String currentCaseStatus, String strNextStatus, String strAssignTo, String userGroup) throws Exception {
        SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();
        RouteUtil routeUtil = new RouteUtil();
        Debug.printDebug("strTaskId " + strTaskId);
        CommonFunction.writeFile("WorkflowApiLog", "strTaskId " + strTaskId);
        BaseDAO dao = baseDAO;
        dao.setSession(session);
        String wfCode = "";
        String taskAssignTo = "";
        
        Map jsonMap = new HashMap();
        if(!Validator.isEmpty(userGroup)) {
            taskAssignTo = checkPreviousTaskDoer(strCaseId, "", userGroup);
            if(Validator.isEmpty(strAssignTo)) {
                strAssignTo = taskAssignTo;
            }
            
            wfCode = getWfCode(strNextStatus, WF_STEP.APPLICATION);
        }
        Debug.printDebug("taskAssignTo " + taskAssignTo);
        Debug.printDebug("wfCode " + wfCode);
        CommonFunction.writeFile("WorkflowApiLog", "taskAssignTo " + taskAssignTo);
        CommonFunction.writeFile("WorkflowApiLog", "wfCode " + wfCode);
        
        if(!Validator.isEmpty(strAssignTo)) {
            jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=Y", wfCode + "_assignTo::=" + strAssignTo, "caseStatus::=" + strCaseStatus);
        } else {
            jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=Y", "caseStatus::=" + strCaseStatus);
        }
                
        Debug.printDebug("jsonMap  " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap  " + jsonMap);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        request.setAttribute("ignoreCsrfCheck", "true");
        
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
//            dao.beginBatchTransaction();
             try {
                ApplicationPModel appModel = (ApplicationPModel) dao.getModelById(strCaseId, ApplicationPModel.class);
                 System.out.println("n workflowAPI appModel.strNextStatus " + strNextStatus);
                if(strNextStatus.equals(WF_STATUS.APPLICATION_PAYMENT_PENDING)) {
                    appModel.updatableColumns= new String[]{"case_id","wf_status","app_status"};
                    appModel.setApp_status(PB_STATUS.PENDING_APP_PAYMENT);
                    appModel.setWf_status(strNextStatus);

                    PaymentModel paymentModel = new PaymentModel();
                    paymentModel.setPayment_id(com.sains.framework.base.CommonFunction.getId(20));
                    paymentModel.setCase_id(appModel.getCase_id());
                    paymentModel.setPayment_amount(100.00); //fixed amount RM100.00
                    paymentModel.setPayment_status(PaymentModel.PAYMENT_STATUS.PENDING_PAYMENT); //initial status
                    paymentModel.setPlatform("W"); //default value
                    paymentModel.setDiv_code(appModel.getCase_div()); //default value
                    paymentModel.defaultAddProperties();

                    getDaoService_().insertWithSession(dao.getSession(), paymentModel);

                    PaymentItemModel paymentItemModel = new PaymentItemModel();
                    paymentItemModel.setPayitem_id(com.sains.framework.base.CommonFunction.getId(20));
                    paymentItemModel.setPayment_id(paymentModel.getPayment_id());
                    paymentItemModel.setItem_amount(paymentModel.getPayment_amount()); //initial amount
                    paymentItemModel.setItem_ref_no(appModel.getCase_ref());
                    paymentItemModel.setRvr_code(SystemConstants.RVS_TRAN_CODE.RVS_TRANCODE);
                    paymentItemModel.setSubcode(SystemConstants.RVS_TRAN_CODE.RVS_SUBCODE);

                    paymentItemModel.defaultAddProperties();

                    Debug.printDebug("paymentItemModel had inserted !!!!!!! " + paymentItemModel);
                    CommonFunction.writeFile("WorkflowApiLog", "paymentItemModel had inserted !!!!!!! " + paymentItemModel);
                    getDaoService_().insertWithSession(dao.getSession(), paymentItemModel);
                } else {
                    appModel.updatableColumns= new String[]{"case_id","wf_status"};
                    System.out.println("n workflowAPI appModel.getInternal_case() " + appModel.getInternal_case());
                    System.out.println("n workflowAPI appModel.strNextStatus() " + strNextStatus);
                    if(!Validator.isEmpty(appModel.getInternal_case())) {
                        if(appModel.getInternal_case().equals("Y") && strNextStatus.equals(WF_STATUS.APPLICATION_PAYMENT_COMPLETED)) {
                            appModel.setApp_status(PB_STATUS.APP_PAYMENT_COMPLETED);
                        }
                    }
                    appModel.setWf_status(strNextStatus);
                }
                
//                getDaoService_().updateWithSession(dao.getSession(), appModel);
                
                //to insert processing history record
                setInternalStatus(dao.getSession(), appModel ,strCaseStatus, strNextStatus, "");
                insertProcessingHistory(dao.getSession(), appModel.getCase_id() ,"", currentCaseStatus, "");
                
//                dao.commitBatchTransaction();

                // to change -> to include payment pending
                //############################################## Trigger ISM [Start] ##############################################
                new LogFunction().logInfo(this.getClass(), "ATTEMPT APP ISM IN PROGRESS WORKFLOW CALL FOR -> " + appModel.getID());
                System.out.println("ATTEMPT UPDATE APP ISM ");
                org.apache.logging.log4j.LogManager.getLogger(this.getClass().getSimpleName()).info("ATTEMPT APP ISM IN PROGRESS WORKFLOW CALL FOR -> " + appModel.getID());
                new ISMServicesAction().triggerISMWorkflowApp(appModel, ISMWorkflowBase.ISM_WF_STEP.UPD_INPROGRESS);
                //############################################## Trigger ISM [End] ################################################

             } catch (Exception e) {
//                dao.rollbackBatchTransaction();
                e.printStackTrace();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> completeTask_APP1 :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "completeTask_APP1");
             }finally{
//                 dao.closeSession();
             }
            return jsonMap;
        }else{
            return jsonMap;
        }
    }
    
    public Map completeTask_APP2(Session session, String strTaskId, String strCaseId, String strCode, String strCaseStatus, String currentCaseStatus, String strNextStatus, String strAssignTo, String userGroup) throws Exception {
        SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();
        RouteUtil routeUtil = new RouteUtil();
        Debug.printDebug("strTaskId " + strTaskId);
        CommonFunction.writeFile("WorkflowApiLog", "strTaskId " + strTaskId);
        BaseDAO dao = baseDAO;
        dao.setSession(session);
        String wfCode = "";
        String taskAssignTo = "";
        
        Map jsonMap = new HashMap();
        if(!Validator.isEmpty(userGroup)) {
            taskAssignTo = checkPreviousTaskDoer(strCaseId, "", userGroup);
            if(Validator.isEmpty(strAssignTo)) {
                strAssignTo = taskAssignTo;
            }
            
            wfCode = getWfCode(strNextStatus, WF_STEP.APPLICATION);
        }
        Debug.printDebug("taskAssignTo " + taskAssignTo);
        Debug.printDebug("wfCode " + wfCode);
        CommonFunction.writeFile("WorkflowApiLog", "taskAssignTo " + taskAssignTo);
        CommonFunction.writeFile("WorkflowApiLog", "wfCode " + wfCode);
        
        if(!Validator.isEmpty(strAssignTo)) {
            jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=R", wfCode + "_assignTo::=" + strAssignTo, "caseStatus::=" + strCaseStatus);
        } else {
            jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=R", "caseStatus::=" + strCaseStatus);
        }
                
        Debug.printDebug("jsonMap  " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap  " + jsonMap);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        request.setAttribute("ignoreCsrfCheck", "true");
        
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            dao.beginBatchTransaction();
             try {
                ApplicationPModel appModel = (ApplicationPModel) dao.getModelById(strCaseId, ApplicationPModel.class);
                
                if(strNextStatus.equals(WF_STATUS.ISSUE_LETTER_REJECTION)) {
                    appModel.updatableColumns= new String[]{"case_id","wf_status","app_status"};
                    appModel.setApp_status(PB_STATUS.APPLICATION_QUERY);
                    appModel.setWf_status(strNextStatus);
                } else {
                    appModel.updatableColumns= new String[]{"case_id","wf_status"};
                    appModel.setWf_status(strNextStatus);
                }
                
                getDaoService_().updateWithSession(dao.getSession(), appModel);

                //to insert processing history record
                setInternalStatus(dao.getSession(), appModel ,strCaseStatus, strNextStatus, "");
                insertProcessingHistory(dao.getSession(), appModel.getCase_id() ,"", currentCaseStatus, "");
                
                dao.commitBatchTransaction();
                
                //############################################## Trigger ISM [Start] ##############################################
                new LogFunction().logInfo(this.getClass(), "ATTEMPT APP ISM IN PROGRESS WORKFLOW CALL FOR -> " + appModel.getID());
                Debug.printDebug("ATTEMPT UPDATE APP ISM ");
                org.apache.logging.log4j.LogManager.getLogger(this.getClass().getSimpleName()).info("ATTEMPT APP ISM IN PROGRESS (QUERY) WORKFLOW CALL FOR -> " + appModel.getID());
                new ISMServicesAction().triggerISMWorkflowApp(appModel, ISMWorkflowBase.ISM_WF_STEP.UPD_QUERIED);
                //############################################## Trigger ISM [End] ##############################################
             } catch (Exception e) {
                dao.rollbackBatchTransaction();
                e.printStackTrace();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> completeTask_APP2 :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "completeTask_APP2");
             }finally{
//                 dao.closeSession();
             }
            return jsonMap;
        }else{
            return jsonMap;
        }
    }
    
    public String completeTask_APP3(Session session, String strTaskId, String strCaseId, String strCode, String strCaseStatus, String strCurrentStatus, String strNextStatus, String strAssignTo, String userGroup) throws Exception {
        SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();
        RouteUtil routeUtil = new RouteUtil();
        Debug.printDebug("strTaskId " + strTaskId);
        CommonFunction.writeFile("WorkflowApiLog", "strTaskId " + strTaskId);
        
        BaseDAO dao = baseDAO;
        dao.setSession(session);
        String wfCode = "";
        String taskAssignTo = "";
        
        Map jsonMap = new HashMap();
        if(!Validator.isEmpty(userGroup)) {
            taskAssignTo = checkPreviousTaskDoer(strCaseId, "", userGroup);
            if(Validator.isEmpty(strAssignTo)) {
                strAssignTo = taskAssignTo;
            }
            
            wfCode = getWfCode(strNextStatus, WF_STEP.APPLICATION);
        }
        Debug.printDebug("taskAssignTo " + taskAssignTo);
        Debug.printDebug("wfCode " + wfCode);
        CommonFunction.writeFile("WorkflowApiLog", "taskAssignTo " + taskAssignTo);
        CommonFunction.writeFile("WorkflowApiLog", "wfCode " + wfCode);
        
        if(!Validator.isEmpty(strAssignTo)) {
            jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=Y", wfCode + "_assignTo::=" + strAssignTo, "caseStatus::=" + strCaseStatus);
        } else {
            jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=Y", "caseStatus::=" + strCaseStatus);
        }
                
        Debug.printDebug("jsonMap  " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap  " + jsonMap);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        request.setAttribute("ignoreCsrfCheck", "true");
        
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            dao.beginBatchTransaction();
             try {
                ApplicationPModel appModel = (ApplicationPModel) dao.getModelById(strCaseId, ApplicationPModel.class);
                
                appModel.updatableColumns= new String[]{"case_id","wf_status","app_status"};
                appModel.setApp_status(PB_STATUS.APPLICATION_QUERY);
                appModel.setWf_status(strNextStatus);
                
                getDaoService_().updateWithSession(dao.getSession(), appModel);
                
                //to insert processing history record
                setInternalStatus(dao.getSession(), appModel ,strCaseStatus, strNextStatus, "");
                insertProcessingHistory(dao.getSession(), appModel.getCase_id() ,"", strCurrentStatus, "");
                
                dao.commitBatchTransaction();
             } catch (Exception e) {
                dao.rollbackBatchTransaction();
                e.printStackTrace();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> completeTask_APP3 :: " + e);                
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "completeTask_APP3");
             }finally{
//                 dao.closeSession();
             }
            return jsonMap.get("result").toString();
        }else{
            return jsonMap.get("errMsg").toString();
        }
    }
    
    public Map completeTask_APP4(Session session, String strTaskId, String strCaseId, String strCode, String strCaseStatus, String strCurrentStatus, String strNextStatus, String strAssignTo, String userGroup) throws Exception {
        SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();
        RouteUtil routeUtil = new RouteUtil();
        Debug.printDebug("strTaskId " + strTaskId);
        CommonFunction.writeFile("WorkflowApiLog", "strTaskId " + strTaskId);
        
        BaseDAO dao = baseDAO;
        dao.setSession(session);
        String wfCode = "";
        String taskAssignTo = "";
        
        Map jsonMap = new HashMap();
        if(!Validator.isEmpty(userGroup)) {
            taskAssignTo = checkPreviousTaskDoer(strCaseId, "", userGroup);
            if(Validator.isEmpty(strAssignTo)) {
                strAssignTo = taskAssignTo;
            }
            
            wfCode = getWfCode(strNextStatus, WF_STEP.APPLICATION);
        }
        Debug.printDebug("taskAssignTo " + taskAssignTo);
        Debug.printDebug("wfCode " + wfCode);
        CommonFunction.writeFile("WorkflowApiLog", "taskAssignTo " + taskAssignTo);
        CommonFunction.writeFile("WorkflowApiLog", "wfCode " + wfCode);
        
        if(!Validator.isEmpty(strAssignTo)) {
            jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=N", wfCode + "_assignTo::=" + strAssignTo, "caseStatus::=" + strCaseStatus);
        } else {
            jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=N", "caseStatus::=" + strCaseStatus);
        }
                
        Debug.printDebug("jsonMap  " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap  " + jsonMap);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        request.setAttribute("ignoreCsrfCheck", "true");
        
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            dao.beginBatchTransaction();
             try {
                ApplicationPModel appModel = (ApplicationPModel) dao.getModelById(strCaseId, ApplicationPModel.class);
                
                if(strCaseStatus.equals(WF_STATUS.PENDING_FOR_SS_VERIFY)) {
                    appModel.updatableColumns= new String[]{"case_id","wf_status","app_status"};
                    appModel.setWf_status(strNextStatus);
                }
                
                getDaoService_().updateWithSession(dao.getSession(), appModel);
                
                //to insert processing history record
                setInternalStatus(dao.getSession(), appModel ,strCaseStatus, strNextStatus, "");
                insertProcessingHistory(dao.getSession(), appModel.getCase_id() ,"", strCurrentStatus, "");
                
                dao.commitBatchTransaction();
             } catch (Exception e) {
                dao.rollbackBatchTransaction();
                e.printStackTrace();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> completeTask_APP4 :: " + e);                
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "completeTask_APP4");
             }finally{
//                 dao.closeSession();
             }
            return jsonMap;
        }else{
            return jsonMap;
        }
    }
   
    public String startUSJ002(String strCaseId) throws Exception {
        Debug.printDebug("*******@@@@@@@@@@@@*******WFI.startUSJ02 called from backend***********************************");
        CommonFunction.writeFile("WorkflowApiLog", "*******@@@@@@@@@@@@*******WFI.startUSJ02 called from backend***********************************");
        cf.writeFile("WorkflowAPILog", "startUSJ002*****************strCaseId:: " + strCaseId);

        RouteUtil routeUtil = new RouteUtil();
        BaseDAO dao = baseDAO;
        Debug.printDebug("strCaseId:: " + strCaseId);
        CommonFunction.writeFile("WorkflowApiLog", "strCaseId:: " + strCaseId);
        ApplicationPModel appPbModel = (ApplicationPModel) dao.getModelById(strCaseId, ApplicationPModel.class);
        JobDetailModel surveyJob = new JobDetailModel();

        Debug.printDebug("appPbModel.getID() " + appPbModel.getID());
        CommonFunction.writeFile("WorkflowApiLog", "appPbModel.getID() " + appPbModel.getID());
        Map jsonMap = null;
        
        if(appPbModel != null) {
            
            SurveyFirmPModel firmModel = new SurveyFirmPModel();
            firmModel = (SurveyFirmPModel) dao.getModelByCode("case_id", appPbModel.getCase_id(), new SurveyFirmPModel());
            
            appPbModel.setWf_status(UtimapsAction.WF_STATUS.PREPARE_SJI_PENDING);
            appPbModel.setApp_status(UtimapsAction.PB_STATUS.ISSUANCE_PENDING);
            
            surveyJob.defaultAddProperties();
            surveyJob.setJob_id(com.sains.framework.base.CommonFunction.getId(20));

            surveyJob.setCase_id(appPbModel.getCase_id());
            surveyJob.setUsj_job_type(UtimapsAction.SURVEY_JOB_TYPE.USJ);
            surveyJob.setUsj_classification("M");
            surveyJob.setUsj_div(appPbModel.getCase_div());
            Debug.printDebug("firmModel " + firmModel);
            Debug.printDebug("firmModel getFirm_soc " + firmModel.getFirm_soc());
            CommonFunction.writeFile("WorkflowApiLog", "firmModel " + firmModel);
            CommonFunction.writeFile("WorkflowApiLog", "firmModel getFirm_soc " + firmModel.getFirm_soc());
            surveyJob.setUs_so_id(firmModel.getFirm_soc());
            Debug.printDebug("appPbModel.getLand_desc check if null " + appPbModel.getLand_desc());
            CommonFunction.writeFile("WorkflowApiLog", "appPbModel.getLand_desc check if null " + appPbModel.getLand_desc());
            surveyJob.setLand_desc(appPbModel.getLand_desc());
            surveyJob.setUsj_status(PB_STATUS.ISSUANCE_PENDING);
            surveyJob.setWf_status(WF_STATUS.PREPARE_SJI_PENDING);
            surveyJob.setCase_ref(appPbModel.getCase_ref());
            surveyJob.setQual_level(appPbModel.getQual_level());
            surveyJob.setRequestor_branch("90");
            
            // 22/08/2025 add for ISM workflow use
            surveyJob.setFim_user_id(appPbModel.getFim_user_id());
            surveyJob.setCo_id(appPbModel.getCo_id());
            
            appPbModel.setJob_id(surveyJob.getJob_id());
            String taskAssignTo = checkPreviousTaskDoer(surveyJob.getCase_id(), surveyJob.getJob_id(), USER_GROUP.AS);
            
            jsonMap = routeUtil.startUSJ002(surveyJob.getJob_id(), surveyJob.getUsj_div(), appPbModel.getPj_name(), surveyJob.getUsj_no(), surveyJob.getUsj_job_type(), surveyJob.getCase_ref(), "BACKEND", taskAssignTo, "BACKEND");
        }

        Debug.printDebug("jsonMap " + jsonMap);
        Debug.printDebug("done start at routeutil startUSJ002 for" + strCaseId);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "done start at routeutil startUSJ002 for" + strCaseId);
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            Debug.printDebug("sucess startUSJ002");
            CommonFunction.writeFile("WorkflowApiLog", "sucess startUSJ002");
            try {
                dao.beginBatchTransaction();
                dao.getSession().update(appPbModel);
                dao.getSession().save(surveyJob);
                
                insertProcessingHistoryBackend(dao.getSession(), surveyJob.getJob_id() ,"", surveyJob.getWf_status(), "");
                setInternalStatusBackend(dao.getSession(), appPbModel, surveyJob);
                dao.commitBatchTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> startUSJ002 :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "startUSJ002");
                dao.rollbackBatchTransaction();
            }finally{
                dao.closeSession();
            }
            return jsonMap.get("status").toString();
        }else{
            return jsonMap.get("errMsg").toString();
        }
    }
    
    public Map completeTask_ISS(Session session, String strTaskId, String strJobId, String strCaseId, String completeValue, String strCaseStatus, String strCompleteStatus, String strNextStatus, String strAssignTo, String userGroup) throws Exception {
        SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();
        RouteUtil routeUtil = new RouteUtil();
        Debug.printDebug("strTaskId " + strTaskId);
        CommonFunction.writeFile("WorkflowApiLog", "strTaskId " + strTaskId);
        
        BaseDAO dao = baseDAO;
        dao.setSession(session);
        String wfCode = "";
        String taskAssignTo = "";
        
        Map jsonMap = new HashMap();
        if(!Validator.isEmpty(userGroup)) {
            taskAssignTo = checkPreviousTaskDoer(strCaseId, "", userGroup);
            if(Validator.isEmpty(strAssignTo)) {
                strAssignTo = taskAssignTo;
            }
            
            wfCode = getWfCode(strNextStatus, WF_STEP.APPLICATION);
        }
        Debug.printDebug("taskAssignTo " + taskAssignTo);
        Debug.printDebug("wfCode " + wfCode);
        CommonFunction.writeFile("WorkflowApiLog", "taskAssignTo " + taskAssignTo);
        CommonFunction.writeFile("WorkflowApiLog", "wfCode " + wfCode);
        
        if(!Validator.isEmpty(strAssignTo)) {
            jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=" + completeValue, wfCode + "_assignTo::=" + strAssignTo, "caseStatus::=" + strCaseStatus);
        } else {
            jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=" + completeValue, "caseStatus::=" + strCaseStatus);
        }
                
        Debug.printDebug("jsonMap  " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap  " + jsonMap);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        request.setAttribute("ignoreCsrfCheck", "true");
        
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            try {
                ApplicationPModel appModel = (ApplicationPModel) dao.getModelById(strCaseId, ApplicationPModel.class);
                JobDetailModel jobModel  = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
                if(strCaseStatus.equals(WF_STATUS.PREPARE_SJI_PENDING)) {
                    appModel.updatableColumns= new String[]{"case_id","wf_status"};
                    appModel.setWf_status(strNextStatus);
                    
                    jobModel.updatableColumns= new String[]{"job_id","wf_status"};
                    jobModel.setWf_status(strNextStatus);
                    
                } else if(strCaseStatus.equals(WF_STATUS.ISSUE_SJI_PENDING)){
                    if(completeValue.equals("Y")) {
                        appModel.updatableColumns= new String[]{"case_id","wf_status","app_status"};
                        appModel.setApp_status(PB_STATUS.SUBMISSION_SAVE);
                        appModel.setWf_status(strNextStatus);
                        
                        jobModel.updatableColumns= new String[]{"job_id","usj_status","wf_status","date_issue","usj_date"};
                        jobModel.setUsj_status(PB_STATUS.SUBMISSION_SAVE);
                        jobModel.setWf_status(strNextStatus);
                        jobModel.setDate_issue(DateUtil.getCurrentTimestamp());
                        jobModel.setUsj_date(DateUtil.getCurrentTimestamp());
                        
                    } else if(completeValue.equals("B")) {
                        appModel.updatableColumns= new String[]{"case_id","wf_status"};
                        appModel.setWf_status(strNextStatus);
                        
                        jobModel.updatableColumns= new String[]{"job_id","wf_status"};
                        jobModel.setWf_status(strNextStatus);
                    }
                }
                
                getDaoService_().updateWithSession(dao.getSession(), appModel);
                getDaoService_().updateWithSession(dao.getSession(), jobModel);
                
                //############################################## Trigger ISM [Start] ##############################################
                if(appModel.getApp_status().equals(PB_STATUS.SUBMISSION_SAVE)) { // <- after finish sign SJI
                    Debug.printDebug("ISM COMPLETE UPDATE!");
                    new LogFunction().logInfo(this.getClass(), "ATTEMPT APP ISM COMPLETE WORKFLOW CALL FOR -> " + appModel.getID());
                    new ISMServicesAction().triggerISMWorkflowApp(appModel, ISMWorkflowBase.ISM_WF_STEP.COMPLETED);
                    new LogFunction().logInfo(this.getClass(), "ATTEMPT SUB ISM SAVE AS DRAFT WORKFLOW CALL FOR -> " + jobModel.getID());
                    new ISMServicesAction().triggerISMWorkflowSub(jobModel, ISMWorkflowBase.ISM_WF_STEP.SAVE_DRAFT);
                }
                //############################################## End Tr. ISM [ End ] ##############################################

                //to insert processing history record
                setInternalSubStatus(dao.getSession(), jobModel ,strCaseStatus, strNextStatus, "");
                insertProcessingHistory(dao.getSession(), jobModel.getJob_id() ,"", strCompleteStatus, "");
                
             } catch (Exception e) {
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> completeTask_ISS :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "completeTask_ISS");
                 e.printStackTrace();
             }finally{
             }
            return jsonMap;
        }else{
            return jsonMap;
        }
    }
    
    public String startUSJ003(String strJobId, String triggerBy) throws Exception {
        Debug.printDebug("*******@@@@@@@@@@@@*******WFI.startUSJ003 called from public submission********************* strJobId:: " + strJobId);
        CommonFunction.writeFile("WorkflowApiLog", "*******@@@@@@@@@@@@*******WFI.startUSJ003 called from public submission********************* strJobId:: " + strJobId);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        RouteUtil routeUtil = new RouteUtil();
        BaseDAO dao = baseDAO;
        JobDetailModel jobModel = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
        Map jsonMap = null;
        String taskAssignTo = checkPreviousTaskDoer(jobModel.getCase_id(), jobModel.getJob_id(), USER_GROUP.AS);
        
        jsonMap = routeUtil.startUSJ003_1(jobModel.getJob_id(), jobModel.getUsj_div(), jobModel.getApplicationModel().getPj_name(), jobModel.getUsj_no(), jobModel.getUsj_job_type(), jobModel.getCase_ref(), triggerBy, taskAssignTo, triggerBy);

        Debug.printDebug("jsonMap " + jsonMap);
        Debug.printDebug("done start at routeutil startUSJ003 for" + strJobId);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "done start at routeutil startUSJ003 for" + strJobId);
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            Debug.printDebug("sucess startUSJ003");
            CommonFunction.writeFile("WorkflowApiLog", "sucess startUSJ003");
            
            if(jobModel.getUsj_status().equals(PB_STATUS.SUBMISSION_QUERY_USCS10)) {
                Debug.printDebug("USCS10 SKIP HC");
                CommonFunction.writeFile("WorkflowApiLog", "USCS10 SKIP HC");
                jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_SUB);
            } else {
                Debug.printDebug("OTHER GO TO HC");
                CommonFunction.writeFile("WorkflowApiLog", "OTHER GO TO HC");
                jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_PENDING_HARDCOPY);
            }
            
            jobModel.setWf_status(UtimapsAction.WF_STATUS.CHECK_U10_PENDING);
            try {
                dao.beginBatchTransaction();
                dao.getSession().update(jobModel);
                
                setInternalSubStatus(dao.getSession(), jobModel ,"", jobModel.getWf_status(), "");
                insertProcessingHistory(dao.getSession(), jobModel.getJob_id() ,"", jobModel.getWf_status(), "");
                
                dao.commitBatchTransaction();
                
                //############################################## Trigger ISM [Start] ##############################################
                new LogFunction().logInfo(this.getClass(), "ATTEMPT APP ISM SUBMIT WORKFLOW CALL FOR -> " + jobModel.getID());
                new ISMServicesAction().triggerISMWorkflowSub(jobModel, ISMWorkflowBase.ISM_WF_STEP.SUBMIT);
                //############################################## Trigger ISM [ End ] ##############################################
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> startUSJ003 :: " + e);                
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "startUSJ003");
                e.printStackTrace();
            }finally{
                dao.closeSession();
            }
            return jsonMap.get("status").toString();
        }else{
            return jsonMap.get("errMsg").toString();
        }
    }
    
    public Map completeTask_SUB(Session session, String strTaskId, String strJobId, String strCaseId, String completeValue, String strCaseStatus, String strCompleteStatus, String strNextStatus, String strAssignTo) throws Exception {
        RouteUtil routeUtil = new RouteUtil();
        BaseDAO dao = baseDAO;
        dao.setSession(session);
        Debug.printDebug("strTaskId " + strTaskId);
        CommonFunction.writeFile("WorkflowApiLog", "strTaskId " + strTaskId);
        Map jsonMap = new HashMap();
        
        JobDetailModel jobDetailModel  = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
        String taskAssignTo = checkPreviousTaskDoer(strCaseId, strJobId, USER_GROUP.SS);
        if(Validator.isEmpty(strAssignTo)) {
            strAssignTo = taskAssignTo;
        }
        
        String wfStep = "";
        Debug.printDebug("taskAssignTo " + taskAssignTo);
        CommonFunction.writeFile("WorkflowApiLog", "taskAssignTo " + taskAssignTo);
        
        if(Validator.isEmpty(jobDetailModel.getControl_sv_flag()) || jobDetailModel.getControl_sv_flag().equals("N")) {
            wfStep = "NONTRAVERSE";
        } else {
            if(Validator.isEmpty(jobDetailModel.getComp_completed()) || jobDetailModel.getComp_completed().equals("N")) {
                wfStep = "TRAVERSE";
            } else {
                wfStep = "NONTRAVERSE";
            }
        }
        
        String wfCode = getWfCode(strNextStatus, wfStep);
        Debug.printDebug("wfCode " + wfCode);
        Debug.printDebug("strAssignTo " + strAssignTo);
        CommonFunction.writeFile("WorkflowApiLog", "wfCode " + wfCode);
        CommonFunction.writeFile("WorkflowApiLog", "strAssignTo " + strAssignTo);
        
        if(!Validator.isEmpty(strAssignTo)) {
            jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=" + completeValue, wfCode + "_assignTo::=" + strAssignTo, "caseStatus::=" + strCaseStatus);
        } else {
            jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=" + completeValue, "caseStatus::=" + strCaseStatus);
        }

        Debug.printDebug("jsonMap  " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap  " + jsonMap);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        request.setAttribute("ignoreCsrfCheck", "true");
        
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            dao.beginBatchTransaction();
             try {
                JobDetailModel jobModel  = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
                Debug.printDebug("strJobId " + strJobId);
                Debug.printDebug("strCaseStatus " + strCaseStatus);
                Debug.printDebug("strNextStatus " + strNextStatus);
                CommonFunction.writeFile("WorkflowApiLog", "strJobId " + strJobId);
                CommonFunction.writeFile("WorkflowApiLog", "strCaseStatus " + strCaseStatus);
                CommonFunction.writeFile("WorkflowApiLog", "strNextStatus " + strNextStatus);
                
                String ismUpdateStatus = ISMWorkflowBase.ISM_WF_STEP.UPD_INPROGRESS;
                
                if(completeValue.equals("Y")) {
                    if(strCaseStatus.equals(UtimapsAction.WF_STATUS.CHECK_U10_PENDING)) {
                        jobModel.updatableColumns= new String[]{"job_id","wf_status"};
                        jobModel.setWf_status(strNextStatus);
                        
                    } else if(strCaseStatus.equals(UtimapsAction.WF_STATUS.VERIFY_U10_PENDING)) {
                        jobModel.updatableColumns= new String[]{"job_id","wf_status"};
                        jobModel.setWf_status(UtimapsAction.WF_STATUS.ISSUE_USCS20_PENDING);
                        
                    } else if(strCaseStatus.equals(UtimapsAction.WF_STATUS.ISSUE_USCS10_PENDING)) {
                        jobModel.updatableColumns= new String[]{"job_id","wf_status","usj_status"};
                        jobModel.setWf_status(strNextStatus);
                        jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS10);
                        
                        ismUpdateStatus = ISMWorkflowBase.ISM_WF_STEP.UPD_QUERIED;
                    
                    } else if(strCaseStatus.equals(UtimapsAction.WF_STATUS.ISSUE_USCS20_PENDING)) {
                        jobModel.updatableColumns= new String[]{"job_id","wf_status"};
                        jobModel.setWf_status(strNextStatus);
                    } else {
                        jobModel.updatableColumns= new String[]{"job_id","wf_status"};
                        jobModel.setWf_status(strNextStatus);
                    }
                     
                } else if (completeValue.equals("N")) {
                    if(strCaseStatus.equals(UtimapsAction.WF_STATUS.VERIFY_U10_PENDING)) {
                        jobModel.updatableColumns= new String[]{"job_id","wf_status"};
                        jobModel.setWf_status(strNextStatus);
                    } else if(strCaseStatus.equals(UtimapsAction.WF_STATUS.VERIFY_U20_PENDING)) {
                        jobModel.updatableColumns= new String[]{"job_id","wf_status"};
                        jobModel.setWf_status(strNextStatus);
                    }
                } else if (completeValue.equals("B")) {
                    if(strCaseStatus.equals(UtimapsAction.WF_STATUS.ISSUE_USCS20_PENDING)) {
                        jobModel.updatableColumns= new String[]{"job_id","wf_status"};
                        jobModel.setWf_status(strNextStatus);
                    } else {
                        jobModel.updatableColumns= new String[]{"job_id","wf_status"};
                        jobModel.setWf_status(strNextStatus);
                    }
                }
                
                getDaoService_().updateWithSession(dao.getSession(), jobModel);
                
                if(!strNextStatus.equals(WF_STATUS.SUBMISSION_DRAFT)) {
                    //to insert processing history record
                    setInternalSubStatus(dao.getSession(), jobModel ,strCaseStatus, strNextStatus, "");
                }
                
                //to insert processing history record
                insertProcessingHistory(dao.getSession(), jobModel.getJob_id() ,"", strCompleteStatus, "");
                
                dao.commitBatchTransaction();
                
                //############################################## End Tr. ISM [ Trigger ] ##############################################
                new LogFunction().logInfo(this.getClass(), "ATTEMPT SUB ISM UPDATE PROGRESS WORKFLOW CALL FOR -> " + jobModel.getID());
                new ISMServicesAction().triggerISMWorkflowSub(jobModel, ismUpdateStatus);
                //############################################## End Tr. ISM [ End ] ##############################################
                
             } catch (Exception e) {
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> completeTask_SUB :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "completeTask_SUB");
                e.printStackTrace();
             }finally{
//                 dao.closeSession();
             }
            return jsonMap;
        }else{
            return jsonMap;
        }
    }
    
    public Map completeTask_SUB2(Session session, String strTaskId, String strJobId, String strCaseId, String completeValue, String strCaseStatus, String strCompleteStatus, String strNextStatus, String strAssignTo, String userGroup) throws Exception {
        RouteUtil routeUtil = new RouteUtil();
        Debug.printDebug("strTaskId " + strTaskId);
        CommonFunction.writeFile("WorkflowApiLog", "strTaskId " + strTaskId);
        BaseDAO dao = baseDAO;
        dao.setSession(session);
        String wfCode = "";
        String taskAssignTo = "";
        
        Map jsonMap = new HashMap();
        if(!Validator.isEmpty(userGroup)) {
            JobDetailModel jobDetailModel  = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
            taskAssignTo = checkPreviousTaskDoer(strCaseId, strJobId, userGroup);
            if(Validator.isEmpty(strAssignTo)) {
                strAssignTo = taskAssignTo;
            }
            String wfStep = "";
            Debug.printDebug("taskAssignTo " + taskAssignTo);
            CommonFunction.writeFile("WorkflowApiLog", "taskAssignTo " + taskAssignTo);

            if(Validator.isEmpty(jobDetailModel.getControl_sv_flag()) || jobDetailModel.getControl_sv_flag().equals("N")) {
                wfStep = "NONTRAVERSE";
            } else {
                
                // Temporary Commented :: will check if need to checking or not
//                if(Validator.isEmpty(jobDetailModel.getComp_completed()) || jobDetailModel.getComp_completed().equals("N")) {
//                    wfStep = "TRAVERSE";
//                } else {
//                    wfStep = "NONTRAVERSE";
//                }
                
                wfStep = "TRAVERSE";
            }
            wfCode = getWfCode(strNextStatus, wfStep);
        }
        
        Debug.printDebug("wfCode " + wfCode);
        CommonFunction.writeFile("WorkflowApiLog", "wfCode " + wfCode);
        
        if(!Validator.isEmpty(strAssignTo)) {
            jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=" + completeValue, wfCode + "_assignTo::=" + strAssignTo, "caseStatus::=" + strCaseStatus);
        } else {
            jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=" + completeValue, "caseStatus::=" + strCaseStatus);
        }
        Debug.printDebug("jsonMap  " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap  " + jsonMap);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        request.setAttribute("ignoreCsrfCheck", "true");
        
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            dao.beginBatchTransaction();
            try {
                JobDetailModel jobModel  = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
                Debug.printDebug("strCaseStatus" + strCaseStatus);
                Debug.printDebug("strNextStatus" + strNextStatus);
                CommonFunction.writeFile("WorkflowApiLog", "strCaseStatus" + strCaseStatus);
                CommonFunction.writeFile("WorkflowApiLog", "strNextStatus" + strNextStatus);
                
                if(strCaseStatus.equals(WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING)) {
                    jobModel.updatableColumns= new String[]{"job_id","wf_status_2","usj_status","approval_date"};
                    jobModel.setWf_status_2(strNextStatus);
                    jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_COMPLETED);
                    jobModel.setApproval_date(DateUtil.getCurrentTimestamp());
                } else if(strCaseStatus.equals(WF_STATUS.ISSUE_USCS30_PENDING)) {
                    jobModel.updatableColumns= new String[]{"job_id","wf_status_2"};
                    jobModel.setWf_status_2(strNextStatus);
                } else if(strCaseStatus.equals(WF_STATUS.ISSUE_USCS40_PENDING) || strCaseStatus.equals(WF_STATUS.ISSUE_USCS50_PENDING) || strCaseStatus.equals(WF_STATUS.ISSUE_USCS80_PENDING)) {

                    switch (strCaseStatus) {
                        case WF_STATUS.ISSUE_USCS40_PENDING:
                            jobModel.updatableColumns= new String[]{"job_id","wf_status_2","usj_status", "precheck_stage"};
                            jobModel.setWf_status_2(strNextStatus);
                            jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40);
                            jobModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV); // 25092024 Aiman - [USCS40] Include reset [PRECHECK] stage to query run traverse only
                            break;
                        case WF_STATUS.ISSUE_USCS50_PENDING:
                            jobModel.updatableColumns= new String[]{"job_id","wf_status_2","usj_status"};
                            jobModel.setWf_status_2(strNextStatus);
                            jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS50);
                            break;
                        case WF_STATUS.ISSUE_USCS80_PENDING:
                            jobModel.updatableColumns= new String[]{"job_id","wf_status_2","usj_status"};
                            jobModel.setWf_status_2(strNextStatus);
                            jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS80);
//                            jobModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL); 
                            break;
                        default:
                            break;
                    }
                } else if(strCaseStatus.equals(WF_STATUS.ISSUE_USCS60_PENDING)) {
                    // if uscs60, do nothing
                } else if(strCaseStatus.equals(WF_STATUS.ISSUE_USCS70_PENDING)) {
                    jobModel.updatableColumns= new String[]{"job_id","wf_status_2","comp_completed"};
                    jobModel.setWf_status_2(strNextStatus);
                    jobModel.setComp_completed("Y");
                } else if(strCaseStatus.equals(WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING)) {
                    jobModel.updatableColumns= new String[]{"job_id","wf_status_2","usj_status"};
                    jobModel.setWf_status_2(WF_STATUS.ISSUE_USCS10_HARDCOPY_COMPLETED); 
                    jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_RESUBMIT_HARDCOPY);
                } else if(strCaseStatus.equals(WF_STATUS.ASSIGN_STA_FOR_CHECKING)) {
                    jobModel.updatableColumns= new String[]{"job_id","wf_status_2"};
                    jobModel.setWf_status_2(strNextStatus); 
                } else {
                    jobModel.updatableColumns= new String[]{"job_id","wf_status_2"};
                    jobModel.setWf_status_2(strNextStatus);
                }
                
                Debug.printDebug("jobModel getWf_status_2 @@@ " + jobModel.getWf_status_2());
                CommonFunction.writeFile("WorkflowApiLog", "jobModel getWf_status_2 @@@ " + jobModel.getWf_status_2());
                
                getDaoService_().updateWithSession(dao.getSession(), jobModel);
                
                if(!strNextStatus.equals(WF_STATUS.SUBMISSION_DRAFT)) {
                    setInternalSubStatus(dao.getSession(), jobModel ,strCaseStatus, strNextStatus, "");
                }
                //to insert processing history record
                insertProcessingHistory(dao.getSession(), jobModel.getJob_id() ,"", strCompleteStatus, "");
                
                dao.commitBatchTransaction();
                
                if(UtimapsAction.PB_STATUS.SUBMISSION_COMPLETED.equals(jobModel.getUsj_status())) {
                    //############################################## Trigger ISM [Start] ##############################################
                    new LogFunction().logInfo(this.getClass(), "ATTEMPT SUB ISM COMPLETE WORKFLOW CALL FOR -> " + jobModel.getID());
                    Debug.printDebug("ATTEMPT SUB ISM COMPLETE WORKFLOW CALL FOR -> " + jobModel.getID());
                    new ISMServicesAction().triggerISMWorkflowSub(jobModel, ISMWorkflowBase.ISM_WF_STEP.COMPLETED);
                    //############################################## Tr. ISM [ End ] ##############################################
                } else {
                    switch(jobModel.getUsj_status()) {
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS10:
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40:
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS50:
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS80:
                        case UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK:
                            Debug.printDebug("ATTEMPT SUB ISM QUERY WORKFLOW CALL FOR -> " + jobModel.getID());
                            new ISMServicesAction().triggerISMWorkflowSub(jobModel, ISMWorkflowBase.ISM_WF_STEP.UPD_QUERIED);
                            break;
                    
                    }
                }
                
                
             } catch (Exception e) {
                Debug.printDebug("ada error in workflwo!!!!!!!!!!!!!!");
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> completeTask_SUB :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "completeTask_SUB");
                e.printStackTrace();
             }finally{
//                 dao.closeSession();
             }
            return jsonMap;
        }else{
            return jsonMap;
        }
    }
    
    public String startUSJ003_02(String strJobId, String triggerBy) throws Exception {
        Debug.printDebug("*******@@@@@@@@@@@@*******WFI.startUSJ003_02 called from internal********************* strJobId:: " + strJobId);
        CommonFunction.writeFile("WorkflowApiLog", "*******@@@@@@@@@@@@*******WFI.startUSJ003_02 called from internal********************* strJobId:: " + strJobId);
        RouteUtil routeUtil = new RouteUtil();
        BaseDAO dao = baseDAO;
        JobDetailModel jobModel = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
        String taskAssignTo = checkPreviousTaskDoer(jobModel.getCase_id(), jobModel.getJob_id(), USER_GROUP.AS);
        
        Map jsonMap = null;
        jsonMap = routeUtil.startUSJ003_02(jobModel.getJob_id(), jobModel.getUsj_div(), jobModel.getApplicationModel().getPj_name(), jobModel.getUsj_no(), jobModel.getUsj_job_type(), jobModel.getCase_ref(), triggerBy, taskAssignTo, triggerBy);

        Debug.printDebug("jsonMap " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap " + jsonMap);
        //if declared & submitted
        Debug.printDebug("done start at routeutil startUSJ003_02 for" + strJobId);
        CommonFunction.writeFile("WorkflowApiLog", "done start at routeutil startUSJ003_02 for" + strJobId);
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            Debug.printDebug("sucess startUSJ003_02");
            CommonFunction.writeFile("WorkflowApiLog", "sucess startUSJ003_02");
            jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_SUB);
            jobModel.setWf_status_2(UtimapsAction.WF_STATUS.CHECK_HARDCOPY_SUBMISSION_START);
            try {
                dao.beginBatchTransaction();
                dao.getSession().update(jobModel);
                
                //to clear checklist result according to checklist status
                clearRejectedChecklist(dao.getSession(), jobModel, "U21");
                
                setInternalSubStatus(dao.getSession(), jobModel ,"", jobModel.getWf_status_2(), "");
                insertProcessingHistory(dao.getSession(), jobModel.getJob_id() ,"", jobModel.getWf_status_2(), "");
                
                dao.commitBatchTransaction();
                
                //############################################## Trigger ISM [Start] ##############################################
                new LogFunction().logInfo(this.getClass(), "ATTEMPT SUB ISM IN SUBMIT WORKFLOW CALL FOR -> " + jobModel.getID());
                new ISMServicesAction().triggerISMWorkflowSub(jobModel, ISMWorkflowBase.ISM_WF_STEP.SUBMIT);
                //############################################## End Tr. ISM [ End ] ##############################################
                
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> startUSJ003_02 :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "startUSJ003_02");
                e.printStackTrace();
            }finally{
                dao.closeSession();
            }
            return jsonMap.get("status").toString();
        }else{
            return jsonMap.get("errMsg").toString();
        }
    }
    
    public Map completeTask_AC(Session session, String strJobId, String strCode, String strCaseStatus, String strCompleteStatus, String strNextStatus, String strRemarks) throws Exception {
        RouteUtil routeUtil = new RouteUtil();
        Map jsonMap = routeUtil.completeTaskByActivityCode(strJobId, strCode, "_completeValue::=Y");
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            BaseDAO dao = baseDAO;
            dao.setSession(session);
            dao.beginBatchTransaction();
             try {
                 
                JobDetailModel jobModel  = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
                jobModel.updatableColumns= new String[]{"job_id","wf_status_2"};
                jobModel.setWf_status_2(strNextStatus);
                request.setAttribute("ignoreCsrfCheck", "true");
                getDaoService_().updateWithSession(dao.getSession(), jobModel);
                 
                setInternalSubStatus(dao.getSession(), jobModel ,strCaseStatus, strNextStatus, "");
                insertProcessingHistory(dao.getSession(), jobModel.getJob_id() ,"", strCompleteStatus, "");

                dao.commitBatchTransaction();
             } catch (Exception e) {
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> completeTask_AC :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "completeTask_AC");
                e.printStackTrace();
             }finally{
                 dao.closeSession();
             }
            return jsonMap;
        }else{
            return jsonMap;
        }
//         return null;
    }
    
    public String startUSJ003_03(String strJobId, String triggerBy, String assignedGroup, String assignTo) throws Exception {
        Debug.printDebug("*******@@@@@@@@@@@@*******WFI.startUSJ003_03 called from internal********************* strJobId:: " + strJobId + " || assignTo:: " + assignTo);
        CommonFunction.writeFile("WorkflowApiLog", "*******@@@@@@@@@@@@*******WFI.startUSJ003_03 called from internal********************* strJobId:: " + strJobId + " || assignTo:: " + assignTo);
        RouteUtil routeUtil = new RouteUtil();
        BaseDAO dao = baseDAO;
        JobDetailModel jobModel = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
        Map jsonMap = null;
        String taskAssignTo = "";
        
        if(assignedGroup.equals(USER_GROUP.TA)) {
            Debug.printDebug("is empty assignTo " + Validator.isEmpty(assignTo));
            if(Validator.isEmpty(assignTo)) {
                taskAssignTo = checkPreviousTaskDoer(jobModel.getCase_id(), jobModel.getJob_id(), USER_GROUP.TA);
            } else {
                taskAssignTo = assignTo;
            }
            jsonMap = routeUtil.startUSJ003_03(jobModel.getJob_id(), jobModel.getUsj_div(), jobModel.getApplicationModel().getPj_name(), jobModel.getUsj_no(), jobModel.getUsj_job_type(), jobModel.getCase_ref(), triggerBy, taskAssignTo, triggerBy, "B");
            
        } else {
            Debug.printDebug("is empty assignTo " + Validator.isEmpty(assignTo));
            if(Validator.isEmpty(assignTo)) {
                taskAssignTo = checkPreviousTaskDoer(jobModel.getCase_id(), jobModel.getJob_id(), USER_GROUP.STA);
            }  else {
                taskAssignTo = assignTo;
            }
            jsonMap = routeUtil.startUSJ003_03(jobModel.getJob_id(), jobModel.getUsj_div(), jobModel.getApplicationModel().getPj_name(), jobModel.getUsj_no(), jobModel.getUsj_job_type(), jobModel.getCase_ref(), triggerBy, taskAssignTo, triggerBy, "");
        }

        Debug.printDebug("jsonMap " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap " + jsonMap);
        //if declared & submitted
        Debug.printDebug("done start at routeutil startUSJ003_03 for" + strJobId);
        CommonFunction.writeFile("WorkflowApiLog", "done start at routeutil startUSJ003_03");
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            Debug.printDebug("sucess startUSJ003_03");
            CommonFunction.writeFile("WorkflowApiLog", "sucess startUSJ003_03");
            
            if(jobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40)) {
                // USCS40 (TRAVERSE) HC ACKNOWLEDGEMENT
                jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_PENDING_HARDCOPY);
            } else {
                jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_SUB);
            }
            
            if(assignedGroup.equals(USER_GROUP.TA)) {
                jobModel.setWf_status_2(UtimapsAction.WF_STATUS.TA_CHECK_U30_PENDING);
            } else {
                jobModel.setWf_status_2(UtimapsAction.WF_STATUS.STA_CHECK_U30_PENDING);
            }
            
            try {
                dao.beginBatchTransaction();
                dao.getSession().update(jobModel);
                
                //to clear checklist result according to file status and if submission include control survey or not
                clearAllChecklistResult(dao.getSession(), jobModel, "U30");
                
                setInternalSubStatus(dao.getSession(), jobModel ,"", jobModel.getWf_status_2(), "");
                insertProcessingHistory(dao.getSession(), jobModel.getJob_id() ,"", jobModel.getWf_status_2(), "");
                
                dao.commitBatchTransaction();
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> startUSJ003_03 :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "startUSJ003_03");
                e.printStackTrace();
            }finally{
                dao.closeSession();
            }
            return jsonMap.get("status").toString();
        }else{
            return jsonMap.get("errMsg").toString();
        }
    }
    
    public String startUSJ003_04(String strJobId, String triggerBy, String assignTo) throws Exception {
        Debug.printDebug("*******@@@@@@@@@@@@*******WFI.startUSJ003_04 called from public submission********************* strJobId:: " + strJobId);
        CommonFunction.writeFile("WorkflowApiLog", "*******@@@@@@@@@@@@*******WFI.startUSJ003_04 called from public submission********************* strJobId:: " + strJobId);
        RouteUtil routeUtil = new RouteUtil();
        BaseDAO dao = baseDAO;
        JobDetailModel jobModel = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
        String taskAssignTo = checkPreviousTaskDoer(jobModel.getCase_id(), jobModel.getJob_id(), USER_GROUP.STA);
        
        Map jsonMap = null;
        jsonMap = routeUtil.startUSJ003_04(jobModel.getJob_id(), jobModel.getUsj_div(), jobModel.getApplicationModel().getPj_name(), jobModel.getUsj_no(), jobModel.getUsj_job_type(), jobModel.getCase_ref(), triggerBy, taskAssignTo, triggerBy);

        Debug.printDebug("jsonMap " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap " + jsonMap);
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            Debug.printDebug("sucess startUSJ003_04");
            CommonFunction.writeFile("WorkflowApiLog", "sucess startUSJ003_04");
//            jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_SUB);
            jobModel.setWf_status_2(UtimapsAction.WF_STATUS.CHECK_U40_PENDING);
            
            try {
                dao.beginBatchTransaction();
                dao.getSession().update(jobModel);
                
                //to clear checklist result according to file status and if submission include control survey or not
                clearAllChecklistResult(dao.getSession(), jobModel, "U40");

                setInternalSubStatusBackend(dao.getSession(), jobModel ,UtimapsAction.WF_STATUS.CHECK_U40_PENDING, UtimapsAction.WF_STATUS.CHECK_U40_PENDING, "");
                insertProcessingHistoryBackend(dao.getSession(), jobModel.getJob_id() ,"", UtimapsAction.WF_STATUS.CHECK_U40_PENDING, "");
                
                dao.commitBatchTransaction();
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> startUSJ003_04 :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "startUSJ003_04");
                e.printStackTrace();
            }finally{
                dao.closeSession();
            }
            return jsonMap.get("status").toString();
        }else{
            return jsonMap.get("errMsg").toString();
        }
    }
    
    public String startUSJ003_05(String strJobId, String triggerBy, String assignTo) throws Exception {
        Debug.printDebug("*******@@@@@@@@@@@@*******WFI.startUSJ003_05 called from public submission********************* strJobId:: " + strJobId);
        CommonFunction.writeFile("WorkflowApiLog", "*******@@@@@@@@@@@@*******WFI.startUSJ003_05 called from public submission********************* strJobId:: " + strJobId);
        RouteUtil routeUtil = new RouteUtil();
        BaseDAO dao = baseDAO;
        JobDetailModel jobModel = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
        String taskAssignTo = checkPreviousTaskDoer(jobModel.getCase_id(), jobModel.getJob_id(), USER_GROUP.SS);
        Map jsonMap = null;
        
        jsonMap = routeUtil.startUSJ003_05(jobModel.getJob_id(), jobModel.getUsj_div(), jobModel.getApplicationModel().getPj_name(), jobModel.getUsj_no(), jobModel.getUsj_job_type(), jobModel.getCase_ref(), triggerBy, taskAssignTo, triggerBy);

        Debug.printDebug("jsonMap " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap " + jsonMap);
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            Debug.printDebug("sucess startUSJ003_05");
            CommonFunction.writeFile("WorkflowApiLog", "sucess startUSJ003_05");
            try {
                dao.beginBatchTransaction();
                dao.getSession().update(jobModel);

                setInternalSubStatusBackend(dao.getSession(), jobModel ,UtimapsAction.WF_STATUS.ISSUE_USCS60_PENDING, UtimapsAction.WF_STATUS.ISSUE_USCS60_PENDING, "");
                insertProcessingHistoryBackend(dao.getSession(), jobModel.getJob_id() ,"", UtimapsAction.WF_STATUS.ISSUE_USCS60_PENDING, "");
                
                dao.commitBatchTransaction();
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> startUSJ003_05 :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "startUSJ003_05");
                e.printStackTrace();
            }finally{
                dao.closeSession();
            }
            return jsonMap.get("status").toString();
        }else{
            return jsonMap.get("errMsg").toString();
        }
    }
    
    public String startUSJ003_06(String strJobId, String triggerBy, String assignTo) throws Exception {
        Debug.printDebug("*******@@@@@@@@@@@@*******WFI.startUSJ003_06 called from public submission********************* strJobId:: " + strJobId);
        CommonFunction.writeFile("WorkflowApiLog", "*******@@@@@@@@@@@@*******WFI.startUSJ003_06 called from public submission********************* strJobId:: " + strJobId);
        RouteUtil routeUtil = new RouteUtil();
        BaseDAO dao = baseDAO;
        JobDetailModel jobModel = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
        String taskAssignTo = checkPreviousTaskDoer(jobModel.getCase_id(), jobModel.getJob_id(), USER_GROUP.SS);
        Map jsonMap = null;
        
        jsonMap = routeUtil.startUSJ003_06(jobModel.getJob_id(), jobModel.getUsj_div(), jobModel.getApplicationModel().getPj_name(), jobModel.getUsj_no(), jobModel.getUsj_job_type(), jobModel.getCase_ref(), triggerBy, taskAssignTo, triggerBy);

        Debug.printDebug("jsonMap " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap " + jsonMap);
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            Debug.printDebug("sucess startUSJ003_06");
            CommonFunction.writeFile("WorkflowApiLog", "sucess startUSJ003_06");

            try {
                dao.beginBatchTransaction();
                if(jobModel.getWf_status_2().equals(WF_STATUS.CHECK_U40_APPROVED)) {
                    jobModel.setWf_status_2(UtimapsAction.WF_STATUS.ISSUE_USCS70_PENDING);
                }
                dao.getSession().update(jobModel);
                
                setInternalSubStatus(dao.getSession(), jobModel ,UtimapsAction.WF_STATUS.ISSUE_USCS70_PENDING, UtimapsAction.WF_STATUS.ISSUE_USCS70_PENDING, "");
                insertProcessingHistory(dao.getSession(), jobModel.getJob_id() ,"", UtimapsAction.WF_STATUS.ISSUE_USCS70_PENDING, "");
                
                dao.commitBatchTransaction();
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> startUSJ003_06 :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "startUSJ003_06");
                e.printStackTrace();
            }finally{
                dao.closeSession();
            }
            return jsonMap.get("status").toString();
        }else{
            return jsonMap.get("errMsg").toString();
        }
    }
    
    public String startUSJ003_07(String strJobId, String triggerBy) throws Exception {
        Debug.printDebug("*******@@@@@@@@@@@@*******WFI.startUSJ003_07 called ********************* strJobId:: " + strJobId);
        CommonFunction.writeFile("WorkflowApiLog", "*******@@@@@@@@@@@@*******WFI.startUSJ003_07 called ********************* strJobId:: " + strJobId);
        RouteUtil routeUtil = new RouteUtil();
        BaseDAO dao = baseDAO;
        JobDetailModel jobModel = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
        String taskAssignTo = checkPreviousTaskDoer(jobModel.getCase_id(), jobModel.getJob_id(), USER_GROUP.AS);
        Map jsonMap = null;
        
        jsonMap = routeUtil.startUSJ003_07(jobModel.getJob_id(), jobModel.getUsj_div(), jobModel.getApplicationModel().getPj_name(), jobModel.getUsj_no(), jobModel.getUsj_job_type(), jobModel.getCase_ref(), triggerBy, taskAssignTo, triggerBy);

        Debug.printDebug("jsonMap " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap " + jsonMap);
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            Debug.printDebug("sucess startUSJ003_07");
            CommonFunction.writeFile("WorkflowApiLog", "sucess startUSJ003_07");
            
            if(jobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS80)) {
                //USCS80 TRAVERSE -> HARDCOPY ACKNOWLEDGEMENT
                jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_PENDING_HARDCOPY);
            }
            
            jobModel.setWf_status_2(UtimapsAction.WF_STATUS.CHECK_U50_PENDING);
            try {
                dao.beginBatchTransaction();
                dao.getSession().update(jobModel);
                
                //to clear checklist result according to file status and if submission include control survey or not
                clearAllChecklistResult(dao.getSession(), jobModel, "U50");
                
                setInternalSubStatus(dao.getSession(), jobModel ,"", jobModel.getWf_status_2(), "");
                insertProcessingHistory(dao.getSession(), jobModel.getJob_id() ,"", jobModel.getWf_status_2(), "");
                
                dao.commitBatchTransaction();
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> startUSJ003_07 :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "startUSJ003_07");
                e.printStackTrace();
            }finally{
                dao.closeSession();
            }
            return jsonMap.get("status").toString();
        }else{
            return jsonMap.get("errMsg").toString();
        }
    }
    
    public String startUSJ004_01(String strJobId, String triggerBy) throws Exception {
        Debug.printDebug("*******@@@@@@@@@@@@*******WFI.startUSJ004_01 called from public submission********************* strJobId:: " + strJobId);
        CommonFunction.writeFile("WorkflowApiLog", "*******@@@@@@@@@@@@*******WFI.startUSJ004_01 called from public submission********************* strJobId:: " + strJobId);
        RouteUtil routeUtil = new RouteUtil();
        BaseDAO dao = baseDAO;
        JobDetailModel jobModel = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
        
        String taskAssignTo = checkPreviousTaskDoer(jobModel.getCase_id(), jobModel.getJob_id(), USER_GROUP.AS);

        Map jsonMap = null;
        jsonMap = routeUtil.startUSJ004_01(jobModel.getJob_id(), jobModel.getUsj_div(), jobModel.getApplicationModel().getPj_name(), jobModel.getUsj_no(), jobModel.getUsj_job_type(), jobModel.getCase_ref(), triggerBy, taskAssignTo, triggerBy);

        Debug.printDebug("jsonMap " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap " + jsonMap);
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            Debug.printDebug("sucess startUSJ004_01");
            CommonFunction.writeFile("WorkflowApiLog", "sucess startUSJ004_01");
            
            if(jobModel.getUsj_status().equals(PB_STATUS.SUBMISSION_QUERY_USCS10)) {
                Debug.printDebug("USCS10 SKIP HC");
                CommonFunction.writeFile("WorkflowApiLog", "USCS10 SKIP HC");
                jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_SUBMITTED_NO_HC);
            } else {
                Debug.printDebug("OTHER GO TO HC");
                CommonFunction.writeFile("WorkflowApiLog", "OTHER GO TO HC");
                jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_PENDING_HARDCOPY);
            }
            
            jobModel.setWf_status(UtimapsAction.WF_STATUS.CHECK_U20_PENDING);
            try {
                dao.beginBatchTransaction();
                dao.getSession().update(jobModel);
                
                setInternalSubStatus(dao.getSession(), jobModel ,"", jobModel.getWf_status(), "");
                insertProcessingHistory(dao.getSession(), jobModel.getJob_id() ,"", jobModel.getWf_status(), "");
                
                dao.commitBatchTransaction();
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> startUSJ004_01 :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "startUSJ004_01");
                e.printStackTrace();
            }finally{
                dao.closeSession();
            }
            return jsonMap.get("status").toString();
        }else{
            return jsonMap.get("errMsg").toString();
        }
    }
    
    public String startUSJ004_02(String strJobId, String triggerBy) throws Exception {
        Debug.printDebug("*******@@@@@@@@@@@@*******WFI.startUSJ004_02 called from public submission********************* strJobId:: " + strJobId);
        CommonFunction.writeFile("WorkflowApiLog", "*******@@@@@@@@@@@@*******WFI.startUSJ004_02 called from public submission********************* strJobId:: " + strJobId);
        RouteUtil routeUtil = new RouteUtil();
        BaseDAO dao = baseDAO;
        JobDetailModel jobModel = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
        String taskAssignTo = checkPreviousTaskDoer(jobModel.getCase_id(), jobModel.getJob_id(), USER_GROUP.AS);
        
        Map jsonMap = null;
        jsonMap = routeUtil.startUSJ004_02(jobModel.getJob_id(), jobModel.getUsj_div(), jobModel.getApplicationModel().getPj_name(), jobModel.getUsj_no(), jobModel.getUsj_job_type(), jobModel.getCase_ref(), triggerBy, taskAssignTo, triggerBy);

        Debug.printDebug("jsonMap " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap " + jsonMap);
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            Debug.printDebug("sucess startUSJ004_02");
            CommonFunction.writeFile("WorkflowApiLog", "sucess startUSJ004_02");
            jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_SUB);
            jobModel.setWf_status_2(UtimapsAction.WF_STATUS.CHECK_HARDCOPY_SUBMISSION_START);
            try {
                dao.beginBatchTransaction();
                dao.getSession().update(jobModel);
                
                //to clear checklist result according to checklist status
                clearRejectedChecklist(dao.getSession(), jobModel, "U21");
                
                setInternalSubStatus(dao.getSession(), jobModel ,"", jobModel.getWf_status_2(), "");
                insertProcessingHistory(dao.getSession(), jobModel.getJob_id() ,"", jobModel.getWf_status_2(), "");
                
                dao.commitBatchTransaction();
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> startUSJ004_02 :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "startUSJ004_02");
                e.printStackTrace();
            }finally{
                dao.closeSession();
            }
            return jsonMap.get("status").toString();
        }else{
            return jsonMap.get("errMsg").toString();
        }
    }
    
    public String startUSJ004_03(String strJobId, String triggerBy) throws Exception {
        Debug.printDebug("*******@@@@@@@@@@@@*******WFI.startUSJ004_03 called from public submission********************* strJobId:: " + strJobId);
        CommonFunction.writeFile("WorkflowApiLog", "*******@@@@@@@@@@@@*******WFI.startUSJ004_03 called from public submission********************* strJobId:: " + strJobId);
        RouteUtil routeUtil = new RouteUtil();
        BaseDAO dao = baseDAO;
        JobDetailModel jobModel = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
        String taskAssignTo = checkPreviousTaskDoer(jobModel.getCase_id(), jobModel.getJob_id(), USER_GROUP.AS);
        
        Map jsonMap = null;
        jsonMap = routeUtil.startUSJ004_03(jobModel.getJob_id(), jobModel.getUsj_div(), jobModel.getApplicationModel().getPj_name(), jobModel.getUsj_no(), jobModel.getUsj_job_type(), jobModel.getCase_ref(), triggerBy, taskAssignTo, triggerBy);

        Debug.printDebug("jsonMap " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap " + jsonMap);
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            Debug.printDebug("sucess startUSJ004_03");
            CommonFunction.writeFile("WorkflowApiLog", "sucess startUSJ004_03");
            
            if(jobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS80)) {
                //USCS80 NON TRAVERSE -> HARDCOPY ACKNOWLEDGEMENT
                jobModel.setUsj_status(UtimapsAction.PB_STATUS.SUBMISSION_PENDING_HARDCOPY);
            }
            
            jobModel.setWf_status_2(UtimapsAction.WF_STATUS.CHECK_U50_PENDING);
            try {
                dao.beginBatchTransaction();
                dao.getSession().update(jobModel);
                
                setInternalSubStatus(dao.getSession(), jobModel ,"", jobModel.getWf_status(), "");
                insertProcessingHistory(dao.getSession(), jobModel.getJob_id() ,"", jobModel.getWf_status(), "");
                
                dao.commitBatchTransaction();
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> startUSJ004_03 :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "startUSJ004_03");
                e.printStackTrace();
            }finally{
                dao.closeSession();
            }
            return jsonMap.get("status").toString();
        }else{
            return jsonMap.get("errMsg").toString();
        }
    }
    
    public String startUSJ005_01(String strJobId, String triggerBy) throws Exception {
        Debug.printDebug("*******@@@@@@@@@@@@*******WFI.startUSJ005_01 called from public submission********************* strJobId:: " + strJobId);
        CommonFunction.writeFile("WorkflowApiLog", "*******@@@@@@@@@@@@*******WFI.startUSJ005_01 called from public submission********************* strJobId:: " + strJobId);
        RouteUtil routeUtil = new RouteUtil();
        BaseDAO dao = baseDAO;
        JobDetailModel jobModel = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
        String taskAssignTo = checkPreviousTaskDoer(jobModel.getCase_id(), jobModel.getJob_id(), USER_GROUP.SS);
        
        Map jsonMap = null;
        jsonMap = routeUtil.startUSJ005_01(jobModel.getJob_id(), jobModel.getUsj_div(), jobModel.getApplicationModel().getPj_name(), jobModel.getUsj_no(), jobModel.getUsj_job_type(), jobModel.getCase_ref(), triggerBy, taskAssignTo, triggerBy);

        Debug.printDebug("jsonMap " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap " + jsonMap);
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            Debug.printDebug("sucess startUSJ005_01");
            CommonFunction.writeFile("WorkflowApiLog", "sucess startUSJ005_01");
            
            jobModel.setWf_status_2(UtimapsAction.WF_STATUS.ASSIGN_STA_FOR_CHECKING);
            try {
                dao.beginBatchTransaction();
                dao.getSession().update(jobModel);
                
                setInternalSubStatus(dao.getSession(), jobModel ,"", jobModel.getWf_status_2(), "");
                insertProcessingHistory(dao.getSession(), jobModel.getJob_id() ,"", jobModel.getWf_status_2(), "");
                
                dao.commitBatchTransaction();
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> startUSJ005_01 :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "startUSJ005_01");
                e.printStackTrace();
            }finally{
                dao.closeSession();
            }
            return jsonMap.get("status").toString();
        }else{
            return jsonMap.get("errMsg").toString();
        }
    }
    
    public String checkPreviousTaskDoer(String caseId, String jobId, String userGroup) {
//        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//        SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();  
        String strAssignTo = "";
        
        try {
//            request.setAttribute("ignoreCsrfCheck", "true");
            Map wfStatus = new HashMap();
            wfStatus = getWfStatus(userGroup, "");
            Debug.printDebug("wfStatus " + wfStatus);
            CommonFunction.writeFile("WorkflowApiLog", "wfStatus " + wfStatus);
            
            paramMap.clear();
            paramMap.put("case_id", caseId);
            paramMap.put("process_code", wfStatus.get("APP"));
            List<ProcessingHistoryModel> statusCaseIdList = new ArrayList();
            statusCaseIdList = baseDAO.list_order(paramMap, ProcessingHistoryModel.class, true, "order by process_date asc" );
            
            List<ProcessingHistoryModel> statusJobIdList = new ArrayList();
            if(!Validator.isEmpty(jobId)) {
                paramMap.clear();
                paramMap.put("case_id", jobId);
                paramMap.put("process_code", wfStatus.get("SUB"));
                statusJobIdList = baseDAO.list_order(paramMap, ProcessingHistoryModel.class, true, "order by process_date asc" );
            }
            
            Debug.printDebug("statusCaseIdList.size() " + statusCaseIdList.size());
            Debug.printDebug("statusJobIdList.size() " + statusJobIdList.size());
            CommonFunction.writeFile("WorkflowApiLog", "statusCaseIdList.size() " + statusCaseIdList.size());
            CommonFunction.writeFile("WorkflowApiLog", "statusJobIdList.size() " + statusJobIdList.size());
            if(statusJobIdList.size() > 0) {
                for(ProcessingHistoryModel processing: statusJobIdList){
                    if(!processing.getProcess_by().equals("BACKEND")) {
                        strAssignTo = processing.getProcess_by();
                        Debug.printDebug("getProcess_code " + processing.getProcess_code() + " getHistory_id " + processing.getHistory_id() + " getProcess_by " + strAssignTo);
                        CommonFunction.writeFile("WorkflowApiLog", "getProcess_code " + processing.getProcess_code() + " getHistory_id " + processing.getHistory_id() + " getProcess_by " + strAssignTo);
                    }
                }
            } else if (statusCaseIdList.size() > 0) {
                for(ProcessingHistoryModel processing: statusCaseIdList){
                    if(!processing.getProcess_by().equals("BACKEND")) {
                        strAssignTo = processing.getProcess_by();
                        Debug.printDebug("getProcess_code " + processing.getProcess_code() + " getHistory_id " + processing.getHistory_id() + " getProcess_by " + strAssignTo);
                        CommonFunction.writeFile("WorkflowApiLog", "getProcess_code " + processing.getProcess_code() + " getHistory_id " + processing.getHistory_id() + " getProcess_by " + strAssignTo);
                    }
                }
            }
            
            Debug.printDebug("strAssignTo " + strAssignTo);
            CommonFunction.writeFile("WorkflowApiLog", "strAssignTo " + strAssignTo);
          
        } catch (Exception e) {
            CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> checkPreviousTaskDoer :: " + e);
            CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "checkPreviousTaskDoer");
            e.printStackTrace();
        }finally{
        }
        return strAssignTo;
    }
    
    public Map getWfStatus(String userGroup, String isCase) {
        
        Map jsonMap = new HashMap();
        try {
            Debug.printDebug("@@@@ userGroup "  + userGroup);
            CommonFunction.writeFile("WorkflowApiLog", "@@@@ userGroup "  + userGroup);
            switch (userGroup) {
                case USER_GROUP.AS:
                    jsonMap.put("APP", "102");
                    jsonMap.put("SUB", "112,131,141,162,163,164,281,282");
                    break;
                case USER_GROUP.SS:
                    jsonMap.put("APP", "104,105");
                    jsonMap.put("SUB", "114,115,134,135,136,144,145,146,151,153,174,184,214,234,251,271,292,332,333,341,351");
                    break;
                case USER_GROUP.TA:
                    jsonMap.put("APP", "000");
                    jsonMap.put("SUB", "201,202");
                    break;
                case USER_GROUP.STA:
                    jsonMap.put("APP", "000");
                    jsonMap.put("SUB", "204,205,221,222,310");
                    break;
                case USER_GROUP.SD:
                    jsonMap.put("APP", "000");
                    jsonMap.put("SUB", "300,320,321,322");
                    break;
                default:
                    break;
            }
          
        } catch (Exception e) {
            CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> getWfStatus :: " + e);
            CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "getWfStatus");
            e.printStackTrace();
        }finally{
        }
            
        return jsonMap;
    }
    
    public String getWfCode(String wfStatus, String wfStep) {
        
        String wf_code = "";
        try {
            if(wfStep.equals(WF_STEP.APPLICATION)) {
                switch (wfStatus) {
                    case WF_STATUS.PENDING_FOR_TA_CHECK:
                        wf_code = "USJ001_00_02";
                        break;
                    case WF_STATUS.PENDING_FOR_SS_VERIFY:
                        wf_code = "USJ001_00_03";
                        break;
                    case WF_STATUS.PREPARE_SJI_PENDING:
                        wf_code = "USJ001_01_02";
                        break;
                    case WF_STATUS.ISSUE_SJI_PENDING:
                        wf_code = "USJ001_01_03";
                        break;
                    default:
                        break;
                }
            } else if(wfStep.equals(WF_STEP.TRAVERSE)) {
                switch (wfStatus) {
                    case WF_STATUS.CHECK_U10_PENDING:
                        wf_code = "USJ003_01_01";
                        break;
                    case WF_STATUS.VERIFY_U10_PENDING:
                        wf_code = "USJ003_01_02";
                        break;
                    case WF_STATUS.ISSUE_USCS10_PENDING:
                        wf_code = "USJ003_01_04";
                        break;
                    case WF_STATUS.ISSUE_USCS20_PENDING:
                        wf_code = "USJ003_01_03";
                        break;
                    case WF_STATUS.CHECK_HARDCOPY_SUBMISSION_PENDING:
                        wf_code = "USJ003_02_02";
                        break;
                    case WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING:
                        wf_code = "USJ003_02_04";
                        break;
                    case WF_STATUS.ISSUE_USCS30_PENDING:
                        wf_code = "USJ003_02_06";
                        break;
                    case WF_STATUS.TA_CHECK_U30_PENDING:
                        wf_code = "USJ003_03_02";
                        break;
                    case WF_STATUS.STA_CHECK_U30_PENDING:
                        wf_code = "USJ003_03_03";
                        break;
                    case WF_STATUS.ISSUE_USCS40_PENDING:
                        wf_code = "USJ003_03_04";
                        break;
                    case WF_STATUS.CHECK_U40_PENDING:
                        wf_code = "USJ003_04_02";
                        break;
                    case WF_STATUS.ISSUE_USCS50_PENDING:
                        wf_code = "USJ003_04_04";
                        break;
                    case WF_STATUS.ISSUE_USCS60_PENDING:
                        wf_code = "USJ003_05_02";
                        break;
                    case WF_STATUS.ISSUE_USCS70_PENDING:
                        wf_code = "USJ003_06_02";
                        break;
                    case WF_STATUS.CHECK_U50_PENDING:
                        wf_code = "USJ003_07_02";
                        break;
                    case WF_STATUS.ISSUE_USCS80_PENDING:
                        wf_code = "USJ003_07_03";
                        break;
                    case WF_STATUS.ASSIGN_STA_FOR_CHECKING:
                        wf_code = "USJ005_01_01";
                        break;
                    case WF_STATUS.CHECK_U60_PENDING:
                        wf_code = "USJ005_01_02";
                        break;
                    case WF_STATUS.CHECK_U60_SD_PENDING:
                        wf_code = "USJ005_01_03";
                        break;
                    case WF_STATUS.CHECK_U60_SS_PENDING:
                        wf_code = "USJ005_01_04";
                        break;
                    case WF_STATUS.ISSUE_USCS80_U60_PENDING:
                        wf_code = "USJ005_01_05";
                        break;
                    case WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING:
                        wf_code = "USJ005_01_06";
                        break;
                    default:
                        break;
                }
            } else if (wfStep.equals(WF_STEP.NONTRAVERSE)) {
                switch (wfStatus) {
                    case WF_STATUS.CHECK_U20_PENDING:
                        wf_code = "USJ004_01_02";
                        break;
                    case WF_STATUS.VERIFY_U20_PENDING:
                        wf_code = "USJ004_01_03";
                        break;
                    case WF_STATUS.ISSUE_USCS10_PENDING:
                        wf_code = "USJ004_01_04";
                        break;
                    case WF_STATUS.ISSUE_USCS20_PENDING:
                        wf_code = "USJ004_01_05";
                        break;
                    case WF_STATUS.CHECK_HARDCOPY_SUBMISSION_PENDING:
                        wf_code = "USJ004_02_02";
                        break;
                    case WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING:
                        wf_code = "USJ004_02_03";
                        break;
                    case WF_STATUS.ISSUE_USCS30_PENDING:
                        wf_code = "USJ004_02_04";
                        break;
                    case WF_STATUS.CHECK_U50_PENDING:
                        wf_code = "USJ003_07_02";
                        break;
                    case WF_STATUS.ISSUE_USCS80_PENDING:
                        wf_code = "USJ003_07_03";
                        break;
                    case WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING:
                        wf_code = "USJ003_07_04";
                        break;
                    default:
                        break;
                }
            }
          
        } catch (Exception e) {
            CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> getWfCode :: " + e);
            CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "getWfCode");
            e.printStackTrace();
        }finally{
        }
            
        return wf_code;
    }

    public void clearRejectedChecklist(Session session, JobDetailModel jobModel, String processType) {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        dao.getSession().enableFilter("caseFilter").setParameter("caseNo", jobModel.getJob_id());
        dao.getSession().enableFilter("typeFilter").setParameter("fileType", "CM");
        dao.getSession().enableFilter("fileFilter");
        
        try {
            JobDetailModel jobModel2 = new JobDetailModel();
            jobModel2 = (JobDetailModel) dao.getModelById(jobModel.getJob_id(), JobDetailModel.class);
            ChecklistSetupModel checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type", processType, new ChecklistSetupModel());
            ChecklistModel checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", processType+","+jobModel2.getJob_id(), new ChecklistModel());
            
            if(processType.equals("U21")) {
                ChecklistSetupModel checklistSetupModel2 = (ChecklistSetupModel) dao.getModelByCode("process_type", "U11", new ChecklistSetupModel());
                jobModel2.setChecklistSetupModel(checklistSetupModel2);
            }
                
            jobModel2.setChecklistSetupCaseModel(checklistSetupModel);
            jobModel2.setU10ChecklistModel(checklistModel);

            jobModel2.set_operation(JobDetailModel.OPERATION.PROCESS_CLEAR_CHECKLIST);
            serviceFactory.getSubmissionJobService().clearRejectedChecklist(jobModel2);
            
        } catch (Exception e) {
            CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> clearRejectedChecklist :: " + e);
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "clearRejectedChecklist");
            e.printStackTrace();
        }
    }
    
    public void clearAllChecklistResult(Session session, JobDetailModel jobModel, String processType) {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        dao.getSession().enableFilter("caseFilter").setParameter("caseNo", jobModel.getJob_id());
        dao.getSession().enableFilter("typeFilter").setParameter("fileType", "CM");
        dao.getSession().enableFilter("fileFilter");

        try {
            JobDetailModel jobModel2 = new JobDetailModel();
            jobModel2 = (JobDetailModel) dao.getModelById(jobModel.getJob_id(), JobDetailModel.class);
            ChecklistSetupModel checklistSetupModel = (ChecklistSetupModel) dao.getModelByCode("process_type", processType, new ChecklistSetupModel());
            ChecklistModel checklistModel = (ChecklistModel) dao.getModelByCode("check_type,case_id", processType+","+jobModel2.getJob_id(), new ChecklistModel());
            jobModel2.setChecklistSetupCaseModel(checklistSetupModel);
            jobModel2.setU10ChecklistModel(checklistModel);

            jobModel2.set_operation(JobDetailModel.OPERATION.PROCESS_CLEAR_CHECKLIST);
            serviceFactory.getSubmissionJobService().clearAllSubmissionChecklist(jobModel2);

        } catch (Exception e) {
            CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> clearAllChecklistResult :: " + e);
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "clearAllChecklistResult");
            e.printStackTrace();
        }
    }
    
    public Map completeTask_SUB3(Session session, String strCode, String strJobId, String strCompleteStatus, String strNextStatus) throws Exception {
        SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();
        RouteUtil routeUtil = new RouteUtil();
//        Debug.printDebug("strTaskId " + strTaskId);
//        CommonFunction.writeFile("WorkflowApiLog", "strTaskId " + strTaskId);
        BaseDAO dao = baseDAO;
        dao.setSession(session);
        Map jsonMap = new HashMap();
        
//        jsonMap = routeUtil.completeTask(strTaskId, "_completeValue::=" + completeValue, "caseStatus::=" + strCaseStatus, "_markComplete::=Y" );  
        jsonMap = routeUtil.completeTaskByActivityCode(strJobId, strCode, "_completeValue::=Y", "_markComplete::=Y");
        Debug.printDebug("jsonMap  " + jsonMap);
        CommonFunction.writeFile("WorkflowApiLog", "jsonMap  " + jsonMap);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        request.setAttribute("ignoreCsrfCheck", "true");
        
        if(jsonMap.get("status").toString().equalsIgnoreCase("success")){
            dao.beginBatchTransaction();
            try {
                JobDetailModel jobModel  = (JobDetailModel) dao.getModelById(strJobId, JobDetailModel.class);
//                getDaoService_().updateWithSession(dao.getSession(), jobModel);
                
                insertProcessingHistory(dao.getSession(), jobModel.getJob_id() ,"", strCompleteStatus, "");
                
                dao.commitBatchTransaction();
             } catch (Exception e) {
                dao.rollbackBatchTransaction();
                CommonFunction.writeFile("WorkflowApiLog", "WorkflowApiAction >>> completeTask_SUB :: " + e);
                CommonFunction.writeLogFile(e.getStackTrace(), "WorkflowApiLog", "WorkflowApiAction", "completeTask_SUB");
                e.printStackTrace();
             }finally{
//                 dao.closeSession();
             }
            return jsonMap;
        }else{
            return jsonMap;
        }
    }
    
}

