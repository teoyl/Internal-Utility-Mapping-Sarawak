package com.sains.workflow.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.Debug;
import com.sains.framework.base.LogFunction;
import com.sains.framework.model.SetupSystemModel;
import com.sains.framework.model.User;
import com.sains.framework.sam.dao.UserGroupDAOImpl;
import com.sains.workflow.model.SetupSubSystemModel;
import com.sains.workflow.util.RouteUtil;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.JobDetailModel;
import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;
//import org.apache.log4j.Logger;
//import org.codehaus.stax2.validation.Validatable;
import org.hibernate.Query;
import org.json.simple.JSONObject;
//import route.webservice.WebServiceWorkflow;
//import route.webservice.WebServiceWorkflow_Service;

public class RouteJobMainAction extends BaseActionSupport<String> {

    private static final long serialVersionUID = -6659925652584240539L;
    private List listUserSystem = new ArrayList();
    private List listJobtobeGrab = new ArrayList();
    private List listJobOnHand = new ArrayList();
    private List listJobProcessed = new ArrayList();
    private List listJobBySystem = new ArrayList();
//    protected final static Logger logger = Logger.getLogger(RouteJobMainAction.class);
    private String us_id;
    private String istrSystemId;
    //private String en_id;
    //private String jc_id;
    private int intError;
    private String sysId_ = ""; // ThoTH @ 13-Aug-2014

    private String search_start_date_, search_end_date_, search_wfId_, search_job_submit_by_, search_job_post_desc_, search_job_post_name_id_;//serenechye@ 8/7/2015
    private String jobShow;
    private static String LOAD_SEARCH_PAGE = "load_search_page";
    private static String SEARCH = "search";
    private static String JOB_UNASSIGN = "job_unassign";
    private Integer mainPageSize;
    private Boolean isSUT = Boolean.FALSE;

    private String selected_sys_id = "";
    private String strSystemName = "";

//     public static final class showType {
//        public static final String TRUE = "T";
//        public static final String FALSE = "F";
//    }
    public static final class system {

        public static final String PM = "2";
    }

    public RouteJobMainAction() {
        setAction("JobMainRoute"); //Added by Delvene @ 26-Dec-2013 :: To support breadcrumb display
        // set the required field for common check.
        //getRequiredParam().put("module_code", "module.code");
        //getRequiredParam().put("module_name", "module.name");
    }

    public String prepareAdd() {
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    @Override // change the "String" and return value to the 
    public String getModel() {
        return "";
    }

    public void specificValidation(String validationType) {
    }

    public String add() {
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    public int genListBySystem(String strSystemId) {
        Debug.printDebug("genListBySystem called, strSysmteId = " + strSystemId);
        String strId = "";

        listJobBySystem.clear();
        for (Map<String, Object> mapJob : (List<Map>) getListJobOnHand()) {
            strId = (String) mapJob.get("wf_subsystem");
            if (strId.equals(strSystemId)) {
                listJobBySystem.add(mapJob);
            }
        }
        Debug.printDebug("genListBySystem listJobBySystem.size() :"+listJobBySystem.size());
        return listJobBySystem.size();
    }

    public String processUpdate() {
        validateRequired();
        specificValidation("update");
        if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
            return SystemConstants.ACTION_Status.UPDATE_FAIL;
        }

        return SUCCESS;
    }

    public String delete() {
        for (String deleteId : getSelected()) {
            // do deletion checking here.
            super.delete(deleteId, getModel());
        }
        return SUCCESS;
    }

    // ThoTH @ 28-Jun-2013 :: Impian request no FIFO to get Job, let them select.
    public String showUnassignedJob() {
        String strUsId = ActionContext.getContext().getSession().get("loginId").toString();
        return "load_unassignJob";
    }

    Boolean useRoute = Boolean.FALSE;

    public Boolean getUseRoute() {
        return useRoute;
    }

    public void setUseRoute(Boolean useRoute) {
        this.useRoute = useRoute;
    }

    public String getUs_id() {
        return us_id;
    }

    public void setUs_id(String us_id) {
        this.us_id = us_id;
    }

    public String getIstrSystemId() {
        return istrSystemId;
    }

    public void setIstrSystemId(String istrSystemId) {
        this.istrSystemId = istrSystemId;
    }

    public List getListUserSystem() {
        return listUserSystem;
    }

    public void setListUserSystem(List listUserSystem) {
        this.listUserSystem = listUserSystem;
    }

    public List getListJobtobeGrab() {
        return listJobtobeGrab;
    }

    public void setListJobtobeGrab(List listJobtobeGrab) {
        this.listJobtobeGrab = listJobtobeGrab;
    }

    public List getListJobOnHand() {
        return listJobOnHand;
    }

    public void setListJobOnHand(List listJobOnHand) {
        this.listJobOnHand = listJobOnHand;
    }

    public List getListJobBySystem() {
        return listJobBySystem;
    }

    public void setListJobBySystem(List listJobBySystem) {
        this.listJobBySystem = listJobBySystem;
    }

    public List getListJobProcessed() {
        return listJobProcessed;
    }

    // ThoTH @ 13-Aug-2014 :: No more group by System, always return one record
    public Map getListJobProcessed_total() {
        String strId = "";

        return (Map) getListJobProcessed().get(0);
    }

    public void setListJobProcessed(List listJobProcessed) {
        this.listJobProcessed = listJobProcessed;
    }

    public String getSysId_() {
        return sysId_;
    }

    public void setSysId_(String sysId_) {
        this.sysId_ = sysId_;
    }
//start-- : sereneChye @ 8/7/2015 :: add search for job Unassign

    public String getSearch_start_date_() {
        return search_start_date_;
    }

    public void setSearch_start_date_(String search_start_date_) {
        this.search_start_date_ = search_start_date_;
    }

    public String getSearch_end_date_() {
        return search_end_date_;
    }

    public void setSearch_end_date_(String search_end_date_) {
        this.search_end_date_ = search_end_date_;
    }

    public String getSearch_wfId_() {
        return search_wfId_;
    }

    public void setSearch_wfId_(String search_wfId_) {
        this.search_wfId_ = search_wfId_;
    }

    public String getSearch_job_submit_by_() {
        return search_job_submit_by_;
    }

    public void setSearch_job_submit_by_(String search_job_submit_by_) {
        this.search_job_submit_by_ = search_job_submit_by_;
    }

    public Integer getMainPageSize() {
        return mainPageSize;
    }

    public void setMainPageSize(Integer mainPageSize) {
        this.mainPageSize = mainPageSize;
    }

    public String getSearch_job_post_desc_() {
        return search_job_post_desc_;
    }

    public void setSearch_job_post_desc_(String search_job_post_desc_) {
        this.search_job_post_desc_ = search_job_post_desc_;
    }

    public String getSearch_job_post_name_id_() {
        return search_job_post_name_id_;
    }

    public void setSearch_job_post_name_id_(String search_job_post_name_id_) {
        this.search_job_post_name_id_ = search_job_post_name_id_;
    }
    private Boolean isHOP = Boolean.FALSE;
    private String isFrom = "";

    public Boolean isIsSUT() {
        return isSUT;
    }

    public void setIsSUT(Boolean isSUT) {
        this.isSUT = isSUT;
    }

    private String[] Sign_selected;

    public String[] getSign_selected() {
        return Sign_selected;
    }

    public void setSign_selected(String[] Sign_selected) {
        this.Sign_selected = Sign_selected;
    }

    File certFile = null;
    String dcPass = "";

    public File getCertFile() {
        return certFile;
    }

    public void setCertFile(File certFile) {
        this.certFile = certFile;
    }

    public String getDcPass() {
        return dcPass;
    }

    public void setDcPass(String dcPass) {
        this.dcPass = dcPass;
    }

    public String getIsFrom() {
        return isFrom;
    }

    public void setIsFrom(String isFrom) {
        this.isFrom = isFrom;
    }

    //sw testing on route: start
    private RouteUtil routeUtil = new RouteUtil();
    Boolean fromLoadEditPageRoute = Boolean.FALSE;

    private String completedToday = "0";

    public String getCompletedToday() {
        return completedToday;
    }

    public void setCompletedToday(String completedToday) {
        this.completedToday = completedToday;
    }

    private String completed7days = "0";

    public String getCompleted7days() {
        return completed7days;
    }

    public void setCompleted7days(String completed7days) {
        this.completed7days = completed7days;
    }

    private int totalCompleted7Days = 0;

    public int getTotalCompleted7Days() {
        totalCompleted7Days = Integer.parseInt(completedToday) + Integer.parseInt(completed7days);
        return totalCompleted7Days;
    }

    public void setTotalCompleted7Days(int totalCompleted7Days) {
        this.totalCompleted7Days = totalCompleted7Days;
    }

    private String selected_system = "";

    public String getSelected_system() {
        return selected_system;
    }

    public void setSelected_system(String selected_system) {
        this.selected_system = selected_system;
    }
//serene @ 05-10-2021
    private String selected_tab = "tab2";

    public String getSelected_tab() {
        return selected_tab;
    }

    public void setSelected_tab(String selected_tab) {
        this.selected_tab = selected_tab;
    }
    
//--------------------------------------------------------------------------------- Common RouteJobMainAction : Start
    private List<Map> nextCompleteTaskList = null;

    public List<Map> getNextCompleteTaskList() {
        return nextCompleteTaskList;
    }

    public void setNextCompleteTaskList(List<Map> nextCompleteTaskList) {
        this.nextCompleteTaskList = nextCompleteTaskList;
    }

    private String currentTaskID_ = null;

    public String getCurrentTaskID_() {
        return currentTaskID_;
    }

    public void setCurrentTaskID_(String currentTaskID_) {
        this.currentTaskID_ = currentTaskID_;
    }

    private String currentTaskDesc_ = null;

    public String getCurrentTaskDesc_() {
        return currentTaskDesc_;
    }

    public void setCurrentTaskDesc_(String currentTaskDesc_) {
        this.currentTaskDesc_ = currentTaskDesc_;
    }

    private List<Map> workloadList = null;

    public List<Map> getWorkloadList() {
        if (workloadList == null) {
            workloadList = new ArrayList();
        }
        return workloadList;
    }

    public void setWorkloadList(List<Map> workloadList) {
        this.workloadList = workloadList;
    }

    private Boolean isRA_job = Boolean.FALSE;

    public Boolean getIsRA_job() {
        return isRA_job;
    }

    public String loadAssignPage() throws Exception {
        if (!isFromLoadEdit) {
            setPageTitle_("Assign Processing Officer");
        }
        workloadList = new ArrayList();
        nextCompleteTaskList = new ArrayList();
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        RouteUtil routeUtil = new RouteUtil();
        if (request.getParameter("nc") != null) {
            UserGroupDAOImpl userGroupDAO = new UserGroupDAOImpl();
            for (String ncValue : request.getParameter("nc").split(",")) {
                if (ncValue.equals("RA")) { //Return Assign
                    setPageTitle_("Assign Processing Officer (Return Job)");
                    isRA_job = Boolean.TRUE;
                } else if (ncValue.equals("CQ")) { //Cancel Query
                    setPageTitle_("Assign Processing Officer (Cancel Query)");
                }
                List nextTaskList = new ArrayList();
                String ncDesc = getText("TaskMgmt.manage.A");
                if (ncValue.indexOf(":") > 0) {
                    ncDesc = ncValue.substring(ncValue.indexOf(":") + 1);
                    ncValue = ncValue.substring(0, ncValue.indexOf(":"));
                }
                Map nextTaskMap = routeUtil.getEstimatedNextTask(request.getParameter("taskId_"), ncValue);
                if (nextTaskMap.get("status").equals("success")) {
                    currentTaskDesc_ = (String) nextTaskMap.get("currentTaskDesc");
                    currentTaskID_ = (String) nextTaskMap.get("currentTaskID");
                    for (Map nextTask : (List<Map>) nextTaskMap.get("result")) {
                        queryId_ = (String) nextTask.get("queryId");
                        String tempGroupCode = null;
                        for (String groupCode : (List<String>) nextTask.get("task_group")) {
                            if (tempGroupCode == null) {
                                tempGroupCode = groupCode;
                            } else {
                                tempGroupCode += "," + groupCode;
                            }
                        }
                        Map userGroupMap = userGroupDAO.getGroupInfo(tempGroupCode, null, baseDAO.getSession());
                        List<Map> userList = (List) userGroupMap.get("user");
                        if (!Validator.isEmpty((String) nextTask.get("task_previous_doer"))) {
                            Boolean foundPrevious = Boolean.FALSE;
                            int idx = 0;
                            for (Map user : userList) {
                                if (user.get("userId").equals((String) nextTask.get("task_previous_doer"))) {
                                    user.put("userName", (user.get("userName") + " (Previous User)"));
                                    nextTask.put("assigneeList_value", user.get("userId"));
                                    foundPrevious = Boolean.TRUE;
                                    break;
                                }
                                idx++;
                            }
                            if (foundPrevious) {
                                Map user = userList.get(idx);
                                userList.remove(idx);
                                userList.add(0, user);
                            }
                        }
                        if (!nextTask.containsKey("assigneeList_value")) {
                            nextTask.put("assigneeList_value", userList.get(0).get("user_id"));
                        }

                        if(userList.size()<21){
                            setGraphHeight("600");                            
                        }else{
                            int divideNum = userList.size()/10;
                            int multiplyNum = divideNum *300;
                            setGraphHeight(String.valueOf(multiplyNum));
                        }
                        for (Map user : userList) {
                            Map userWorkloadMap = new HashMap();
                            userWorkloadMap.put("us_user_id", user.get("userId"));
                            userWorkloadMap.put("us_user_name", user.get("userName"));
                            Map completedTaskCountMap = routeUtil.getProcessedTaskCount(SystemCode, "1", (String) user.get("userId"), "populateTaskDetail::=N", "byMonth::=Y");
                            String completedCount = completedTaskCountMap.get("1") + "";
                            Integer completedInt = 0;
                            if (!Validator.isEmpty(completedCount)) {
                                completedInt = Integer.parseInt(completedCount);
                            }
                            userWorkloadMap.put("completed", completedInt);
                            Map pendingTaskMap = routeUtil.getPendingTask(SystemCode, "doer", (String) user.get("userId"));
                            List<Map<String, Object>> pendingTaskList = ((List) pendingTaskMap.get("result"));
                            if (pendingTaskList != null && !pendingTaskList.isEmpty()) {
                                routeUtil.prepareDateValue(((List) pendingTaskMap.get("result")));
                            }
                            Integer outstanding = 0, inProgress = 0;
                            for (Map inProgressTask : (List<Map>) pendingTaskMap.get("result")) {
                                if (inProgressTask.get("due_date") != null
                                        && ((Timestamp) inProgressTask.get("due_date")).after((Timestamp) inProgressTask.get("today"))) {
                                    outstanding++;
                                } else {
                                    inProgress++;
                                }
                            }
                            userWorkloadMap.put("outstanding", outstanding);
                            userWorkloadMap.put("inProgress", inProgress);
                            workloadList.add(userWorkloadMap);
                        }
                        nextTask.put("assigneeList", userGroupMap.get("user")); //User(s) of the task group
                        nextTaskList.add(nextTask);
                    }
                }
                Map map = new HashMap();
                map.put("nextTaskList", nextTaskList);
                map.put("ncDesc", ncDesc);
                map.put("nc", ncValue);

                String a = "";
                int i = 0;
                for (Map workload : workloadList) {
                    a = "{\"Officer\":\"" + workload.get("us_user_name") + "\",\"Completed\":" + workload.get("completed") + ",\"In Progress\":" + workload.get("inProgress") + ",\"Outstanding\":" + workload.get("outstanding") + "}";
                    if (i > 0) {
                        jobStatistic += "," + a;
                    } else {
                        jobStatistic = a;
                    }
                    i++;
                }
                nextCompleteTaskList.add(map);
            }
        }
        return "load_assign_page";
    }

    private String task_id__;

    public String getTask_id__() {
        return task_id__;
    }

    public void setTask_id__(String task_id__) {
        this.task_id__ = task_id__;
    }

    private String activity_id__;

    public String getActivity_id__() {
        return activity_id__;
    }

    public void setActivity_id__(String activity_id__) {
        this.activity_id__ = activity_id__;
    }

    private String assignTo__;

    public String getAssignTo__() {
        return assignTo__;
    }

    public void setAssignTo__(String assignTo__) {
        this.assignTo__ = assignTo__;
    }

    private String assignNextWorkerCompleteValue__;

    public String getAssignNextWorkerCompleteValue__() {
        return assignNextWorkerCompleteValue__;
    }

    public void setAssignNextWorkerCompleteValue__(String assignNextWorkerCompleteValue__) {
        this.assignNextWorkerCompleteValue__ = assignNextWorkerCompleteValue__;
    }

    private String assignmentSelected;

    public String getAssignmentSelected() {
        return assignmentSelected;
    }

    public void setAssignmentSelected(String assignmentSelected) {
        this.assignmentSelected = assignmentSelected;
    }

    private String[] returnJob_returnQuery = new String[]{""};

    public String doAssign() throws Exception {
        BaseDAO dao = baseDAO;
        String loginId = ActionContext.getContext().getSession().get("loginId").toString();
        String[] assignmentVar = assignmentSelected.split(";");
        String[] nextTaskWorkerArr = assignmentVar[1].split(",");
        List<String> paramList = new ArrayList();
        paramList.add("_completeValue::=" + assignmentVar[0]);
        if (assignmentVar[0].equals("CQ")) { //Cancel Query
            //update the query's message_status=D (Deleted)
            try {
                Query query = dao.getSession().createSQLQuery("update t_system_message set message_status='D' where message_id = :msgId");
                query.setString("msgId", queryId_);
                dao.beginBatchTransaction();
                query.executeUpdate();
                dao.commitBatchTransaction();
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
                new LogFunction().logError(this.getClass(), "", e);
            }
        }

        for (String nextTaskActivityWorker : assignmentVar[1].split(",")) {
            String[] activityWorker = nextTaskActivityWorker.split("::");
            paramList.add(activityWorker[0] + "_assignTo::=" + activityWorker[1]);
            paramList.add(activityWorker[0] + "_assignBy::=" + loginId);
            if (activityWorker.length > 3) {
                paramList.add(activityWorker[0] + "_wfJobRemark::=" + activityWorker[3]);
            }
            for (String activityCode : returnJob_returnQuery) {
                if (activityCode.equals(activityWorker[2])) {
                    paramList.add("isReturnOrQueryTask::=Y");
                }
            }
            if (activityWorker[2].equals("hqAssignOic")) {
                paramList.add("caseStatus::=STS-0600-0650");
            }
        }

        String[] paramArr = new String[paramList.size()];
        int idx = 0;
        for (String param : paramList) {
            paramArr[idx++] = param;
        }
        Map wfMap = routeUtil.completeTask(task_id__, paramArr);
        if (!wfMap.get("status").equals("success")) {
            addActionError("Workflow: " + wfMap.get("result"));
        }
        return loadEditPage();
    }

    private String jobStatistic; //@edmund 10102018

    public String getJobStatistic() {
        return jobStatistic;
    }

    public void setJobStatistic(String jobStatistic) {
        this.jobStatistic = jobStatistic;
    }

    private String pageFrom = "";

    public String getPageFrom() {
        return pageFrom;
    }

    public void setPageFrom(String pageFrom) {
        this.pageFrom = pageFrom;
    }

    //amywyp added 13-03-2019 (for show/hide espa&eqp side button)
    List<SetupSubSystemModel> userSubsystemList = null;
    public synchronized List<SetupSubSystemModel> getUserSubsystemList() {
        if (userSubsystemList == null) {
            String strUsId = ActionContext.getContext().getSession().get("userId").toString();
//            Debug.printDebug("strUsId :"+strUsId);
            try {
                userSubsystemList = baseDAO.getSession().createQuery("select distinct subSystem from SetupSystemModel system "
                    + "join system.setupSubSystem subSystem "
                    + "join system.setupGroupList setupGroupList "
                    + "join setupGroupList.groupUser groupUser "
                    + "where groupUser.us_id='" + strUsId + "'").list();
            } catch (Exception e) {
                e.printStackTrace();
                new LogFunction().logError(this.getClass(), "", e);
            }
            if (userSubsystemList == null) {
                userSubsystemList = new ArrayList();
            }
        }
        return userSubsystemList;
    }
    
    public int getCalcTotalSystem() {
        return getUserSubsystemList().size();
    }

    //sereneChye@ 10/7/2015 :: search criteria to grab job
    public String processSearch() throws Exception {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        setIstrSystemId(request.getParameter("istrSystemId"));
        String strAction = request.getParameter("jobSearchAction");
        String strDefaultPageSize = request.getParameter("defaultPageSize");
        String paramDateData = "";
        String paramDateData2 = "";

        setAction("job_unassign");
        setSearched(Boolean.TRUE);
        setPaging(Boolean.TRUE);
        setPagingURL("processSearchRouteJobMain");

        super.populateDynamicActionSetup();
        int intDefaultPageSize = Integer.parseInt(strDefaultPageSize);
        if (intDefaultPageSize > 0) {
            setPageSize(intDefaultPageSize);
        } else if (getMainPageSize() != null) {
            setPageSize(getMainPageSize());
        } else {
            setPageSize(getPageSize());
        }
        setMainPageSize(getPageSize());

        int tempPageNo = 1;
        if (getPageNo() != null) {
            tempPageNo = getPageNo();
        }
        super.setPageNo(tempPageNo);

        List params = new ArrayList();
        if (strAction.equals(SEARCH)) {

            if (!(Validator.isEmpty(getSearch_start_date_()) && Validator.isEmpty(getSearch_end_date_()))) {
                Calendar cal = Calendar.getInstance();
                if (Validator.isEmpty(getSearch_start_date_())) { //end_date not empty
                    cal.setTime(DateUtil.getDate(getSearch_end_date_(), getText("date_default_date_dr")));
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    paramDateData2 = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd");
                    params.add("endDate::=" + paramDateData2);
                } else if (Validator.isEmpty(getSearch_end_date_())) { //start_date not empty
                    cal.setTime(DateUtil.getDate(getSearch_start_date_(), getText("date_default_date_dr")));
                    paramDateData = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd");
                    params.add("startDate::=" + paramDateData);
                } else { //both date not null
                    cal.setTime(DateUtil.getDate(getSearch_start_date_(), getText("date_default_date_dr")));
                    paramDateData = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd");
                    params.add("startDate::=" + paramDateData);
                    cal.setTime(DateUtil.getDate(getSearch_end_date_(), getText("date_default_date_dr")));
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    paramDateData2 = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd");
                    params.add("endDate::=" + paramDateData2);
                }
            }

            String paramUrl = "";
            if (!Validator.isEmpty(paramDateData)) {
                paramUrl += "&search_start_date_=" + getSearch_start_date_();
            }
            if (!Validator.isEmpty(paramDateData2)) {
                paramUrl += "&search_end_date_=" + getSearch_end_date_();
            }

            if (!Validator.isEmpty(getSearch_wfId_())) {
                paramUrl += "&search_wfId_=" + getSearch_wfId_();
                params.add("wfCode::=" + getSearch_wfId_());
            }

            if (!Validator.isEmpty(getSearch_job_submit_by_())) {
                paramUrl += "&search_job_submit_by_=" + getSearch_job_submit_by_();
            }

            if (!Validator.isEmpty(getSearch_job_post_desc_())) {
                paramUrl += "&search_job_post_desc_=" + getSearch_job_post_desc_();
            }

            if (!Validator.isEmpty(getSearch_job_post_desc_())) {
                paramUrl += "&search_job_post_name_id_=" + getSearch_job_post_name_id_();
            }

            BaseDAO dao = baseDAO;
            String strUsId = ActionContext.getContext().getSession().get("loginId").toString();
            if (Validator.isEmpty(request.getParameter("sys_name"))) {
                selected_system = "USJ";
                selected_sys_id = "U001";//default=SPA
            } else {
                selected_system = request.getParameter("sys_name");
                if (selected_system.equals("USJ")) {
                    selected_sys_id = "U001";
                } else {
                    selected_sys_id = "2";
                }

            }

            String workflowGroups = routeUtil.getWorkflowGroup(baseDAO.getSession(), strUsId);

            //-- get Task In Pool --//
            params.add("pageNo::=" + getPageNo());
            params.add("pageSize::=" + getMainPageSize());
            Map taskInPoolMap = routeUtil.getTaskInPool(SystemCode, getIstrSystemId(), "role", workflowGroups, params);

            listJobtobeGrab = (List) taskInPoolMap.get("result");
            routeUtil.prepareDateValue(listJobtobeGrab);
            //this for-loop is to populate the additional Job-Case's detail
            for (Map inProgressTask : (List<Map>) listJobtobeGrab) {
                ApplicationPModel appModel = (ApplicationPModel) dao.getModelById((String)inProgressTask.get("caseId"), ApplicationPModel.class);
                JobDetailModel jobDetailModel = (JobDetailModel) dao.getModelById((String)inProgressTask.get("caseId"), JobDetailModel.class);

                if(appModel!=null){
                    String surveyJobApplication = (Validator.isEmpty(appModel.getPj_name())?"":appModel.getPj_name());
                    String aplsub = appModel.getCase_ref();
                    inProgressTask.put("case_ref", "<b>Application Case Ref. </b>: "+ aplsub + "<br/>" +surveyJobApplication);
                    inProgressTask.put("caseDetailLink", "loadEditApplicationPageUSJ?caseId=" + appModel.getID());                        
                }else if (jobDetailModel!=null){
                    String surveyJobApplication = (Validator.isEmpty(jobDetailModel.getApplicationModel().getPj_name())?"":jobDetailModel.getApplicationModel().getPj_name());
                    String aplsub = jobDetailModel.getCase_ref();
                    String usjno = !Validator.isEmpty(jobDetailModel.getUsj_no()) ? jobDetailModel.getUsj_no() : "N/A";
                    inProgressTask.put("case_ref", "<b>Application Case Ref. </b>: "+ aplsub + "<br/>" + "<b>USJ No. </b>: " + usjno + "<br/>" +surveyJobApplication);
                    inProgressTask.put("caseDetailLink", "loadEditApplicationPageUSJ?caseId=" + jobDetailModel.getID());      

                }
            }
            setNumberOfRows(Integer.parseInt((String) taskInPoolMap.get("totalRecord")));
//            setSearchCondition("jobSearchAction=" + SEARCH + "&istrSystemId=" + istrSystemId + "&defaultPageSize=" + getPageSize() + paramUrl);
        }

        return LOAD_SEARCH_PAGE;
    }

    public String processInsert() throws Exception {
        intError = 0;

        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String taskId = request.getParameter("selectJobId_");
        String loginId = ActionContext.getContext().getSession().get("loginId").toString();
        Map grabTaskMap = routeUtil.grabTask(taskId, loginId);
        if (!grabTaskMap.get("status").equals("success")) {
            addActionError((String) grabTaskMap.get("errMsg"));
            return loadEditPage();
        }
        if (grabTaskMap.containsKey("subSystem")) {
            sysId_ = (String) grabTaskMap.get("subSystem");
        }
        addActionMessage((String) grabTaskMap.get("result"));
        return SystemConstants.ACTION_Status.REFRESH_EDIT;

    }
    
    private String strDurationValue = "1,30";
    public String getStrDurationValue() {
        return strDurationValue;
    }
    
    public String loadJobContent() throws Exception {
//            2019.01.09 Edited by IvyLee to load past 30Days instead of 7 days
        useRoute = Boolean.TRUE;
        Map session = ActionContext.getContext().getSession();
        String USER_ID = (String) session.get("loginId");
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        setIstrSystemId(request.getParameter("istrSystemId"));
        if (getIstrSystemId().equals("COMPLETED")) {
            if (!Validator.isEmpty(request.getParameter("pDuration"))) {
                if (request.getParameter("pDuration").equals("today")) {
                    strDurationValue = "1";
                }
            }
            
//            Debug.printDebug("SystemCode " + SystemCode);
//            Debug.printDebug("strDurationValue " + strDurationValue);
//            Debug.printDebug("USER_ID " + USER_ID);
            Map completedTaskCountMap = routeUtil.getProcessedTaskCount(SystemCode, strDurationValue, USER_ID, "populateTaskDetail::=Y");
            if (completedTaskCountMap != null) {
                BaseDAO dao = baseDAO;
                if (completedTaskCountMap.containsKey("1")) {
                    listJobBySystem.addAll((List) completedTaskCountMap.get("1_taskDetail"));
                }
                if (completedTaskCountMap.containsKey("30")) {
                    listJobBySystem.addAll((List) completedTaskCountMap.get("30_taskDetail"));
                    strDurationValue = "30";
                }
                routeUtil.prepareDateValue(listJobBySystem);
                //this for-loop is to populate the additional Job-Case's detail
                for (Map completedJobMap : (List<Map>) listJobBySystem) {
                    ApplicationPModel appModel = (ApplicationPModel) dao.getModelById((String)completedJobMap.get("caseId"), ApplicationPModel.class);
                    JobDetailModel jobDetailModel = (JobDetailModel) dao.getModelById((String)completedJobMap.get("caseId"), JobDetailModel.class);
                    
                    if(appModel!=null){
                        String surveyJobApplication = (Validator.isEmpty(appModel.getPj_name())?"":appModel.getPj_name());
                        String aplsub = appModel.getCase_ref();
                        completedJobMap.put("case_ref", "<b>Application Case Ref. </b>: "+ aplsub + "<br/>" +surveyJobApplication);
                        completedJobMap.put("caseDetailLink", "loadEditApplicationPageUSJ?caseId=" + appModel.getID());                        
                    }else if (jobDetailModel!=null){
                        String surveyJobApplication = (Validator.isEmpty(jobDetailModel.getApplicationModel().getPj_name())?"":jobDetailModel.getApplicationModel().getPj_name());
                        String aplsub = jobDetailModel.getCase_ref();
                        String usjno = !Validator.isEmpty(jobDetailModel.getUsj_no()) ? jobDetailModel.getUsj_no() : "N/A";
                        completedJobMap.put("case_ref", "<b>Application Case Ref. </b>: "+ aplsub + "<br/>" + "<b>USJ No. </b>: " + usjno + "<br/>" +surveyJobApplication);
                        completedJobMap.put("caseDetailLink", "loadEditApplicationPageUSJ?caseId=" + jobDetailModel.getID());      
                    }
                }
            }
        } else {
            loadEditPage();
        }
        return "load_job_content";
    }

    public String loadDiagramPage() throws Exception {
        try {
            HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter()
                    .append(routeUtil.getTaskWorkflowDiagram(getId()));
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private final String SystemCode = "USJ";
    public String loadEditPage() {
        BaseDAO dao = baseDAO;
        setPageTitle_("My Job");
        setPageSubTitle_(getText("jb.detail"));
        fromLoadEditPageRoute = Boolean.TRUE;
        // ThoTH @ 13-Aug-2014 :: Remain in the current Tab
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        if (!Validator.isEmpty(request.getParameter("msg"))) {//added by amywyp @ 11-03-2019
            String msg = request.getParameter("msg");
            if (msg.startsWith("err::")) {
                for (String err : msg.substring(5).split("::")) {
                    Debug.printDebug("***********************result "+request.getParameter("result"));
                    addActionError(err);
                }
            } else {
                addActionMessage(msg);
            }
        }

        if (!Validator.isEmpty(request.getParameter("page"))) {
            pageFrom = request.getParameter("page");
        }
        
        if (Validator.isEmpty(request.getParameter("sys_name"))) {
            List<SetupSubSystemModel> subSystemList = getUserSubsystemList();
            
            Debug.printDebug("subSystemList " + subSystemList);
            strSystemName = subSystemList.get(0).getSystem_name();
            selected_system = subSystemList.get(0).getSystem_name();
            selected_sys_id = subSystemList.get(0).getSystem_code();
        } else {
            selected_system = request.getParameter("sys_name");
            if (selected_system.equals("EIS")) {
                selected_sys_id = "E001";
                strSystemName = getText("system.name");
            } else {
                selected_sys_id = "2";
                strSystemName = SystemCode;
            }

        }
        setPageSubTitle_(strSystemName);

        Map session = ActionContext.getContext().getSession();
        String USER_ID = (String) session.get("loginId");
        Debug.printDebug("strSystemName = " + strSystemName + " || USER_ID " + USER_ID);
        try {
            String workflowGroups = routeUtil.getWorkflowGroup(baseDAO.getSession(), USER_ID);
            List<Map> listOfMap = null;
            //-- get Task In Pool --//
            Map taskInPoolMap = routeUtil.getTaskInPool(SystemCode, selected_sys_id, "role", workflowGroups, null);
            listOfMap = (List<Map>)taskInPoolMap.get("result");
            Map completedTaskCountMap = routeUtil.getProcessedTaskCount(SystemCode, "1,30", USER_ID, "populateTaskDetail::=N");
            if (completedTaskCountMap != null) {
                if (completedTaskCountMap.containsKey("1")) {
                    completedToday = completedTaskCountMap.get("1") + "";
                }
//              2019.01.09 Edited by IvyL to change from 7 to 30 days
                if (completedTaskCountMap.containsKey("30")) {
                    completed7days = completedTaskCountMap.get("30") + "";
                }
            }
            //-- get Pending Task --//
            Map pendingTaskMap = routeUtil.getPendingTask(SystemCode, "doer", USER_ID);
            List<Map<String, Object>> pendingTaskList = ((List) pendingTaskMap.get("result"));
            if (pendingTaskList != null && !pendingTaskList.isEmpty()) {
                routeUtil.prepareDateValue(((List) pendingTaskMap.get("result")));
            }
            for (Map inProgressTask : (List<Map>) pendingTaskMap.get("result")) {
                listJobOnHand.add(inProgressTask);
            }
            //-- get Sub System --//
            Map subSystemMap = routeUtil.getSubSystem(SystemCode, workflowGroups);
            listOfMap = (List<Map>) subSystemMap.get("result");
            if (listOfMap == null) {
                listOfMap = new ArrayList();
            }                
            for (Map subSystem : listOfMap) {
                System.out.println("subSystem " + subSystem);
                if (!subSystem.get("wf_subsystem").equals(selected_sys_id)) {
//                    continue;
                }
                SetupSubSystemModel subSystemModel = (SetupSubSystemModel) dao.getObjectByCode("system_code", (String) subSystem.get("wf_subsystem"), new SetupSubSystemModel());
                SetupSystemModel systemModel = (SetupSystemModel) dao.getObjectByCode("system_id", subSystemModel.getSystem_id(), new SetupSystemModel());
                Map mapItem = new HashMap();
                mapItem.put("wf_subsystem", subSystem.get("wf_subsystem"));
                mapItem.put("systemname", systemModel.getSystem_name());
                poolCount = 0;
                for (Map inPoolTask : (List<Map>) taskInPoolMap.get("result")) {
                    if (inPoolTask.containsKey("wf_subsystem")) {
                        poolCount++;
                    }
                }
                mapItem.put("longcount", poolCount);

                inProgressCount = 0;
                //this for-loop is to populate the additional Job-Case's detail & count the inProgressCount
                for (Map inProgressTask : (List<Map>) pendingTaskMap.get("result")) {
                    if (!inProgressTask.get("wf_subsystem").equals(selected_sys_id)) {
                        continue;
                    }
                    //serene @ 8/4/2022 
                    if(!Validator.isEmpty((String)inProgressTask.get("previousTaskDoer"))){
                        User  user = (User) dao.getModelByCode("us_user_id",(String)inProgressTask.get("previousTaskDoer"), new User());
                        if(user==null){
                            inProgressTask.put("previousTaskDoerName", "System");
                        }else{
                            inProgressTask.put("previousTaskDoerName", user.getUs_user_name());
                        }
                    }
                    
                    ApplicationPModel appModel = (ApplicationPModel) dao.getModelById((String)inProgressTask.get("caseId"), ApplicationPModel.class);
                    JobDetailModel jobDetailModel = (JobDetailModel) dao.getModelById((String)inProgressTask.get("caseId"), JobDetailModel.class);

                    if(appModel!=null){
                        String surveyJobApplication = (Validator.isEmpty(appModel.getPj_name())?"":appModel.getPj_name());
                        String aplsub = appModel.getCase_ref();
                        inProgressTask.put("case_ref", "<b>Application Case Ref. </b>: "+ aplsub + "<br/>" +surveyJobApplication);
                        inProgressTask.put("caseDetailLink", "loadEditApplicationPageUSJ?caseId=" + appModel.getID());                        
                    }else if (jobDetailModel!=null){
                        String surveyJobApplication = (Validator.isEmpty(jobDetailModel.getApplicationModel().getPj_name())?"":jobDetailModel.getApplicationModel().getPj_name());
                        String aplsub = jobDetailModel.getCase_ref();
                        String usjno = !Validator.isEmpty(jobDetailModel.getUsj_no()) ? jobDetailModel.getUsj_no() : "N/A";
                        inProgressTask.put("case_ref", "<b>Application Case Ref. </b>: "+ aplsub + "<br/>" + "<b>USJ No. </b>: " + usjno + "<br/>" +surveyJobApplication);
                        inProgressTask.put("caseDetailLink", "loadEditApplicationPageUSJ?caseId=" + jobDetailModel.getID());      
                    }
                    
                    if (inProgressTask.containsKey("wf_subsystem")) {
                        inProgressCount++;
                    }
                    
                }
                mapItem.put("ipcount", inProgressCount);
                setIsFromLoadEdit(Boolean.TRUE);
                loadAssignPage();
                for (int i = 0; i < workloadList.size(); i++) {
                    Debug.printDebug("123 listJobOnHand = " + workloadList.get(i));
                }
                listJobtobeGrab.add(0, mapItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        setBreadAppCode_("JobMainRoute");
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    private Boolean isFromLoadEdit = Boolean.FALSE;

    public Boolean getIsFromLoadEdit() {
        return isFromLoadEdit;
    }

    public void setIsFromLoadEdit(Boolean isFromLoadEdit) {
        this.isFromLoadEdit = isFromLoadEdit;
    }

    //added by Edmund
    public String processAssignBatch() throws Exception {

        BaseDAOImpl dao = new BaseDAOImpl();
        String loginId = ActionContext.getContext().getSession().get("loginId").toString();
        List<String> paramList = new ArrayList();
        UserGroupDAOImpl userGroupDAO = new UserGroupDAOImpl();
        try {

            for (String s : job_selected) {
                String taskId = "";
                String activityCode = "";
                String caseId = "";
                int cont = 0;
                for (String sub_s : s.split("::")) {
                    if (cont == 0) {
                        taskId = sub_s;
                    } else {
                        caseId = sub_s;
                    }
                    cont++;
                }
            }

        } catch (Exception e) {
        }
        setHidden_batch_group("grp");
        return loadEditPage();
    }

    private String[] job_selected;

    public String[] getJob_selected() {
        return job_selected;
    }

    public void setJob_selected(String[] job_selected) {
        this.job_selected = job_selected;
    }

    private String batchJobRemark;

    public String getBatchJobRemark() {
        return batchJobRemark;
    }

    public void setBatchJobRemark(String batchJobRemark) {
        this.batchJobRemark = batchJobRemark;
    }

    private String hidden_batch_group;

    public String getHidden_batch_group() {
        return hidden_batch_group;
    }

    public void setHidden_batch_group(String hidden_batch_group) {
        this.hidden_batch_group = hidden_batch_group;
    }

    //added by edmund 07052019
    public String formatBdaRef(String ref) {
        String formattedRef = "";
        try {
            if (!Validator.isEmpty(ref)) {
                if (ref.contains("BDAPLD")) { //new bda reference follow epass
                    //Eg:BDAPLDDCBTU0118B01P03S02
                    //Eg:BDAPLD/DC/BTU-01/18-B01.P03.S02
                    String strAgency = ref.substring(0, 6);
                    String strType = ref.substring(6, 8);
                    String strDistrict = ref.substring(8, 11);
                    String strSeq = ref.substring(11, 13);
                    String strYear = ref.substring(13, 15);
                    String strBlock = ref.substring(15, 18);
                    String strPhase = ref.substring(18, 21);
                    String strSubSeq = ref.substring(21, 24);
                    if (strSubSeq.equals("0")) {
                        if (strType.equals("DC")) {
                            formattedRef = strAgency + "/" + strType + "/" + strDistrict + "-" + strSeq + "/" + strYear + "-" + strBlock + "." + strPhase;
                        } else {
                            formattedRef = strAgency + "/" + strType + "/" + strDistrict + "-" + strSeq + "/" + strYear;
                        }
                    } else {
                        if (strType.equals("DC")) {
                            formattedRef = strAgency + "/" + strType + "/" + strDistrict + "-" + strSeq + "/" + strYear + "-" + strBlock + "." + strPhase + "." + strSubSeq;
                        } else {
                            formattedRef = strAgency + "/" + strType + "/" + strDistrict + "-" + strSeq + "/" + strYear + "-" + strSubSeq;
                        }
                    }
                } else { //existing bda reference (old format)
                    //ivy said for existing bda reference just display as it is , not need format 15-11-2018
                    formattedRef = ref;
                }
            }
        } catch (Exception e) {
            formattedRef = "** " + ref;
        }
        return formattedRef;

    }

    public String formatSubmissionRef(String submissionRef) {

        String formattedSubRef = "";
        if (!Validator.isEmpty(submissionRef)) {
            String strSpaType = submissionRef.substring(0, 3);
            String strFinalSpaType = "";
            if (strSpaType.equals("SPA")) {
                strFinalSpaType = strSpaType;
            } else {
                strFinalSpaType = strSpaType.trim(); //trim trailing empty space
            }
            String strSpaDiv = submissionRef.substring(3, 5);
            strSpaDiv = strSpaDiv.replaceFirst("^0+(?!$)", ""); //trim leading zeroes
            String strSpaYear = submissionRef.substring(5, 9);
            String strSpaSeq = submissionRef.substring(9, 15).trim();
            strSpaSeq = strSpaSeq.replaceFirst("^0+(?!$)", ""); //trim leading zeroes
            String strSpaSubSeq = submissionRef.substring(15, 19).trim();
            strSpaSubSeq = strSpaSubSeq.replaceFirst("^0+(?!$)", ""); //trim leading zeroes
            if (strSpaSubSeq.equals("0")) {
                formattedSubRef = strFinalSpaType + "/" + strSpaDiv + "D/" + strSpaSeq + "/" + strSpaYear;
            } else {
                formattedSubRef = strFinalSpaType + "/" + strSpaDiv + "D/" + strSpaSeq + "/" + strSpaYear + "-" + strSpaSubSeq;
            }

        }
        return formattedSubRef;

    }

    public String formatLnsRef(String ref) {

        String formattedRef = "";
        if (!Validator.isEmpty(ref)) {
            //Eg:TP 0120170000030000
            //Eg:TP/1D/3/2017
            String strType = ref.substring(0, 2);
            String strDiv = ref.substring(2, 4);
            strDiv = strDiv.replaceFirst("^0+(?!$)", ""); //trim leading zeroes
            String strYear = ref.substring(4, 8);
            String strSeq = ref.substring(8, 14).trim();
            strSeq = strSeq.replaceFirst("^0+(?!$)", ""); //trim leading zeroes
            String strSubSeq = ref.substring(14, 18).trim();
            strSubSeq = strSubSeq.replaceFirst("^0+(?!$)", ""); //trim leading zeroes
            if (strSubSeq.equals("0")) {
                formattedRef = strType + "/" + strDiv + "D/" + strSeq + "/" + strYear;
            } else {
                formattedRef = strType + "/" + strDiv + "D/" + strSeq + "/" + strYear + "-" + strSubSeq;
            }
        }
        return formattedRef;
    }

    private String graphHeight;

    public String getGraphHeight() {
        return graphHeight;
    }

    public void setGraphHeight(String graphHeight) {
        this.graphHeight = graphHeight;
    }

    private String queryId_ = null;

    public String getQueryId_() {
        return queryId_;
    }

    public void setQueryId_(String queryId_) {
        this.queryId_ = queryId_;
    }

    //added by ed 5/4/2019
    private String groupDescTitle;

    public String getGroupDescTitle() {
        return groupDescTitle;
    }

    public void setGroupDescTitle(String groupDescTitle) {
        this.groupDescTitle = groupDescTitle;
    }

    //set app id for return job
    private String strAppId;

    public String getStrAppId() {
        return strAppId;
    }

    public void setStrAppId(String strAppId) {
        this.strAppId = strAppId;
    }

    //added by edmund 16042019
    private List userTaskHistoryList = new ArrayList();
    private List userTaskHistoryJobPoolList = new ArrayList();

    public List getUserTaskHistoryList() {
        return userTaskHistoryList;
    }

    public void setUserTaskHistoryList(List userTaskHistoryList) {
        this.userTaskHistoryList = userTaskHistoryList;
    }

    public List getUserTaskHistoryJobPoolList() {
        return userTaskHistoryJobPoolList;
    }

    public void setUserTaskHistoryJobPoolList(List userTaskHistoryJobPoolList) {
        this.userTaskHistoryJobPoolList = userTaskHistoryJobPoolList;
    }

    public String loadUserTaskHistory() throws Exception {
        //amend and added by edmund 08052019
        BaseDAO dao = baseDAO;
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Map session = ActionContext.getContext().getSession();
        String USER_ID = (String) session.get("loginId");
        String cid = request.getParameter("cid");
        //get list from job pool -- start
        String workflowGroups = routeUtil.getWorkflowGroup(baseDAO.getSession(), USER_ID);
        Map taskInPoolMap = routeUtil.getTaskInPool(SystemCode, "SPA", "role", workflowGroups, null);
        List<Map<String, Object>> taskInPoolList = ((List) taskInPoolMap.get("result"));
        if (taskInPoolList != null && !taskInPoolList.isEmpty()) {
            routeUtil.prepareDateValue(((List) taskInPoolMap.get("result")));
        }
        //-----end get list from job pool
        //list of in progress task- amend and added by edmund 08052019
        Map pendingTaskMap = routeUtil.getPendingTask(SystemCode, "doer", USER_ID);
        List<Map<String, Object>> pendingTaskList = ((List) pendingTaskMap.get("result"));
        if (pendingTaskList != null && !pendingTaskList.isEmpty()) {
            routeUtil.prepareDateValue(((List) pendingTaskMap.get("result")));
        }
        //========= set oicHistory into a list===========
        //in progress list --  amend and added by edmund 08052019
        for (Map inProgressTask : (List<Map>) pendingTaskMap.get("result")) {
            if (inProgressTask.get("caseId").equals(cid)) {
                for (Map oicHistoryFilter : (List<Map>) inProgressTask.get("oicHistory")) {
                    if (!Validator.isEmpty((String) oicHistoryFilter.get("task_by"))) {
                        User usermodel = (User) dao.getModelByCode("us_user_id", (String) oicHistoryFilter.get("task_by"), new User());
                        oicHistoryFilter.put("userName", usermodel.getUs_user_name());
                        userTaskHistoryList.add(oicHistoryFilter);
                    }
                }
            }
        }
        //----task in pool list --- amend and added by edmund 08052019
        for (Map unAssignTask : (List<Map>) taskInPoolMap.get("result")) {
            if (unAssignTask.get("caseId").equals(cid)) {
                for (Map oicHistoryFilter : (List<Map>) unAssignTask.get("oicHistory")) {
                    if (!Validator.isEmpty((String) oicHistoryFilter.get("task_by"))) {
                        User usermodeljobpool = (User) dao.getModelByCode("us_user_id", (String) oicHistoryFilter.get("task_by"), new User());
                        oicHistoryFilter.put("userName", usermodeljobpool.getUs_user_name());
                        userTaskHistoryJobPoolList.add(oicHistoryFilter);
                    }
                }
            }
        }

        //============ end here =========================
        return "load_user_task_history";
    }

    public String getSelected_sys_id() {
        return selected_sys_id;
    }
    public void setSelected_sys_id(String selected_sys_id) {
        this.selected_sys_id = selected_sys_id;
    }
    
    private Integer poolCount = 0, inProgressCount=0;

    public Integer getPoolCount() {
        return poolCount;
    }

    public void setPoolCount(Integer poolCount) {
        this.poolCount = poolCount;
    }

    public Integer getInProgressCount() {
        return inProgressCount;
    }

    public void setInProgressCount(Integer inProgressCount) {
        this.inProgressCount = inProgressCount;
    }
    
    HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    public void getTaskJobMap() throws Exception {
        BaseDAO dao = new BaseDAOImpl();
        String initResult = "Y";
        String submitMsg = "Success";
        JSONObject json = new JSONObject();
        
        fromLoadEditPageRoute = Boolean.TRUE;
        // ThoTH @ 13-Aug-2014 :: Remain in the current Tab
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        if (!Validator.isEmpty(request.getParameter("msg"))) {//added by amywyp @ 11-03-2019
            String msg = request.getParameter("msg");
            if (msg.startsWith("err::")) {
                for (String err : msg.substring(5).split("::")) {
                    Debug.printDebug("***********************result "+request.getParameter("result"));
                    addActionError(err);
                }
            } else {
                addActionMessage(msg);
            }
        }

        if (!Validator.isEmpty(request.getParameter("page"))) {
            pageFrom = request.getParameter("page");
        }
        
        if (Validator.isEmpty(request.getParameter("sys_name"))) {
            List<SetupSubSystemModel> subSystemList = getUserSubsystemList();
            
            strSystemName = subSystemList.get(0).getSystem_name();
            selected_system = subSystemList.get(0).getSystem_name();
            selected_sys_id = subSystemList.get(0).getSystem_code();
        } else {
            selected_system = request.getParameter("sys_name");
            if (selected_system.equals("EIS")) {
                selected_sys_id = "E001";
                strSystemName = getText("system.name");
            } else {
                selected_sys_id = "2";
                strSystemName = SystemCode;
            }

        }
        setPageSubTitle_(strSystemName);

        Map session = ActionContext.getContext().getSession();
        String USER_ID = (String) session.get("loginId");
        try {
            String workflowGroups = routeUtil.getWorkflowGroup(baseDAO.getSession(), USER_ID);
            List<Map> listOfMap = null;
            //-- get Task In Pool --//
            Map taskInPoolMap = routeUtil.getTaskInPool(SystemCode, selected_sys_id, "role", workflowGroups, null);
            listOfMap = (List<Map>)taskInPoolMap.get("result");
            Map completedTaskCountMap = routeUtil.getProcessedTaskCount(SystemCode, "1,30", USER_ID, "populateTaskDetail::=N");
            if (completedTaskCountMap != null) {
                if (completedTaskCountMap.containsKey("1")) {
                    completedToday = completedTaskCountMap.get("1") + "";
                }
//              2019.01.09 Edited by IvyL to change from 7 to 30 days
                if (completedTaskCountMap.containsKey("30")) {
                    completed7days = completedTaskCountMap.get("30") + "";
                }
            }
            //-- get Pending Task --//
            Map pendingTaskMap = routeUtil.getPendingTask(SystemCode, "doer", USER_ID);
            List<Map<String, Object>> pendingTaskList = ((List) pendingTaskMap.get("result"));
            if (pendingTaskList != null && !pendingTaskList.isEmpty()) {
                routeUtil.prepareDateValue(((List) pendingTaskMap.get("result")));
            }
            for (Map inProgressTask : (List<Map>) pendingTaskMap.get("result")) {
                listJobOnHand.add(inProgressTask);
            }
            //-- get Sub System --//
            Map subSystemMap = routeUtil.getSubSystem(SystemCode, workflowGroups);
            listOfMap = (List<Map>) subSystemMap.get("result");
            if (listOfMap == null) {
                listOfMap = new ArrayList();
            }
            for (Map subSystem : listOfMap) {
                if (!subSystem.get("wf_subsystem").equals(selected_sys_id)) {
//                    continue;
                }
                SetupSubSystemModel subSystemModel = (SetupSubSystemModel) dao.getObjectByCode("system_code", (String) subSystem.get("wf_subsystem"), new SetupSubSystemModel());
                SetupSystemModel systemModel = (SetupSystemModel) dao.getObjectByCode("system_id", subSystemModel.getSystem_id(), new SetupSystemModel());
                Map mapItem = new HashMap();
                mapItem.put("wf_subsystem", subSystem.get("wf_subsystem"));
                mapItem.put("systemname", systemModel.getSystem_name());
                poolCount = 0;
                for (Map inPoolTask : (List<Map>) taskInPoolMap.get("result")) {
                    if (inPoolTask.containsKey("wf_subsystem")) {
                        poolCount++;
                    }
                }
                mapItem.put("longcount", poolCount);

                inProgressCount = 0;
                //this for-loop is to populate the additional Job-Case's detail & count the inProgressCount
                for (Map inProgressTask : (List<Map>) pendingTaskMap.get("result")) {
                    if (!inProgressTask.get("wf_subsystem").equals(selected_sys_id)) {
                        continue;
                    }
                    //serene @ 8/4/2022 
                    if(!Validator.isEmpty((String)inProgressTask.get("previousTaskDoer"))){
                        User  user = (User) dao.getModelByCode("us_user_id",(String)inProgressTask.get("previousTaskDoer"), new User());
                        if(user==null){
                            inProgressTask.put("previousTaskDoerName", "System");
                        }else{
                            inProgressTask.put("previousTaskDoerName", user.getUs_user_name());
                        }
                    }
                    
                    ApplicationPModel appModel = (ApplicationPModel) dao.getModelById((String)inProgressTask.get("caseId"), ApplicationPModel.class);
                    JobDetailModel jobDetailModel = (JobDetailModel) dao.getModelById((String)inProgressTask.get("caseId"), JobDetailModel.class);
                    if(appModel!=null){
                        String surveyJobApplication = (Validator.isEmpty(appModel.getPj_name())?"":appModel.getPj_name());
                        String aplsub = appModel.getCase_ref();
                        inProgressTask.put("case_ref", "<b>Application Case Ref. </b>: "+ aplsub + "<br/>" +surveyJobApplication);
                        inProgressTask.put("caseDetailLink", "loadEditApplicationPageUSJ?caseId=" + appModel.getID());                        
                    }else if (jobDetailModel!=null){
                        String surveyJobApplication = (Validator.isEmpty(jobDetailModel.getApplicationModel().getPj_name())?"":jobDetailModel.getApplicationModel().getPj_name());
                        String aplsub = jobDetailModel.getCase_ref();
                        String usjno = !Validator.isEmpty(jobDetailModel.getUsj_no()) ? jobDetailModel.getUsj_no() : "N/A";
                        inProgressTask.put("case_ref", "<b>Application Case Ref. </b>: "+ aplsub + "<br/>" + "<b>USJ No. </b>: " + usjno + "<br/>" +surveyJobApplication);
                        inProgressTask.put("caseDetailLink", "loadEditApplicationPageUSJ?caseId=" + jobDetailModel.getID());      

                    }
                    
                    if (inProgressTask.containsKey("wf_subsystem")) {
                        inProgressCount++;
                    }
                    
                }
                mapItem.put("ipcount", inProgressCount);
                setIsFromLoadEdit(Boolean.TRUE);
                loadAssignPage();
                for (int i = 0; i < workloadList.size(); i++) {
                    Debug.printDebug("123 listJobOnHand = " + workloadList.get(i));
                }
                listJobtobeGrab.add(0, mapItem);     
            }
            
//            Debug.printDebug("inProgressCount inmidleel " + inProgressCount);
//            Debug.printDebug("completedToday inmidleel " + completedToday);
//            Debug.printDebug("totalCompleted7Days inmidleel " + totalCompleted7Days);
//            Debug.printDebug("completedToday inmidleel " + completedToday);
//            Debug.printDebug("completed7days inmidleel " + completed7days);
//            Debug.printDebug("poolCount inmidleel " + poolCount);
            
        } catch (Exception e) {
            initResult = "N";
            submitMsg = "e: " + e ;
            e.printStackTrace();
        } finally {
            dao.closeSession();
        }
        
        json.put("status", initResult);
        json.put("poolCount", poolCount);
        json.put("inProgressCount", inProgressCount);
        json.put("completedToday", completedToday);
        json.put("totalCompleted7Days", getTotalCompleted7Days());
        json.put("message", submitMsg);
        
        response.setContentType("application/json");
        response.getWriter().write(json.toString());
        
    }
    
    public void getDashboardData() throws Exception{
        BaseDAO dao = new BaseDAOImpl();
        String initResult = "Y";
        String submitMsg = "Success";
        JSONObject json = new JSONObject();
        Integer appCount = 0, jobCount=0, completedAppCount = 0, completedJobCount = 0;

        try {
            if (Validator.isEmpty(request.getParameter("sys_name"))) {
                List<SetupSubSystemModel> subSystemList = getUserSubsystemList();

                strSystemName = subSystemList.get(0).getSystem_name();
                selected_system = subSystemList.get(0).getSystem_name();
                selected_sys_id = subSystemList.get(0).getSystem_code();
            } else {
                selected_system = request.getParameter("sys_name");
                if (selected_system.equals("EIS")) {
                    selected_sys_id = "E001";
                    strSystemName = getText("system.name");
                } else {
                    selected_sys_id = "2";
                    strSystemName = SystemCode;
                }
            }
            
            Map session = ActionContext.getContext().getSession();
            String USER_ID = (String) session.get("loginId");

            String workflowGroups = routeUtil.getWorkflowGroup(baseDAO.getSession(), USER_ID);
            List<Map> listOfMap = null;
            
            //-- get Task In Pool --//
            Map taskInPoolMap = routeUtil.getTaskInPool(SystemCode, selected_sys_id, "role", workflowGroups, null);
            
            Map pendingTaskMap = routeUtil.getPendingTask(SystemCode, "doer", USER_ID);
            
            List<Map<String, Object>> pendingTaskList = ((List) pendingTaskMap.get("result"));
            if (pendingTaskList != null && !pendingTaskList.isEmpty()) {
                routeUtil.prepareDateValue(((List) pendingTaskMap.get("result")));
            }
            for (Map inProgressTask : (List<Map>) pendingTaskMap.get("result")) {
                listJobOnHand.add(inProgressTask);
            }
            //-- get Sub System --//
            Map subSystemMap = routeUtil.getSubSystem(SystemCode, workflowGroups);
            listOfMap = (List<Map>) subSystemMap.get("result");
            if (listOfMap == null) {
                listOfMap = new ArrayList();
            }
            for (Map subSystem : listOfMap) {
                if (!subSystem.get("wf_subsystem").equals(selected_sys_id)) {
//                    continue;
                }
                SetupSubSystemModel subSystemModel = (SetupSubSystemModel) dao.getObjectByCode("system_code", (String) subSystem.get("wf_subsystem"), new SetupSubSystemModel());
                SetupSystemModel systemModel = (SetupSystemModel) dao.getObjectByCode("system_id", subSystemModel.getSystem_id(), new SetupSystemModel());
                Map mapItem = new HashMap();
                mapItem.put("wf_subsystem", subSystem.get("wf_subsystem"));
                mapItem.put("systemname", systemModel.getSystem_name());
                poolCount = 0;
                for (Map inPoolTask : (List<Map>) taskInPoolMap.get("result")) {
                    if (inPoolTask.containsKey("wf_subsystem")) {
                        poolCount++;
                    }
                }
                mapItem.put("longcount", poolCount);

                inProgressCount = 0;
                //this for-loop is to populate the additional Job-Case's detail & count the inProgressCount
                for (Map inProgressTask : (List<Map>) pendingTaskMap.get("result")) {
                    if (!inProgressTask.get("wf_subsystem").equals(selected_sys_id)) {
//                        continue;
                    }
                    //serene @ 8/4/2022 
                    if(!Validator.isEmpty((String)inProgressTask.get("previousTaskDoer"))){
                        User  user = (User) dao.getModelByCode("us_user_id",(String)inProgressTask.get("previousTaskDoer"), new User());
                        if(user==null){
                            inProgressTask.put("previousTaskDoerName", "System");
                        }else{
                            inProgressTask.put("previousTaskDoerName", user.getUs_user_name());
                        }
                    }
                    
                    ApplicationPModel appModel = (ApplicationPModel) dao.getModelById((String)inProgressTask.get("caseId"), ApplicationPModel.class);
                    JobDetailModel jobDetailModel = (JobDetailModel) dao.getModelById((String)inProgressTask.get("caseId"), JobDetailModel.class);
                    if(appModel!=null){
                        appCount++;
                    }else if (jobDetailModel!=null){
                        jobCount++;
                    }
                    
                    if (inProgressTask.containsKey("wf_subsystem")) {
                        inProgressCount++;
                    }
                    
                }
                mapItem.put("ipcount", inProgressCount);
                setIsFromLoadEdit(Boolean.TRUE);
                loadAssignPage();
                for (int i = 0; i < workloadList.size(); i++) {
                    Debug.printDebug("123 listJobOnHand = " + workloadList.get(i));
                }
                listJobtobeGrab.add(0, mapItem);     
            }
            
            Map completedTaskCountMap = routeUtil.getProcessedTaskCount(SystemCode, strDurationValue, USER_ID, "populateTaskDetail::=Y");
            if (completedTaskCountMap != null) {
                if (completedTaskCountMap.containsKey("1")) {
                    listJobBySystem.addAll((List) completedTaskCountMap.get("1_taskDetail"));
                }
                if (completedTaskCountMap.containsKey("30")) {
                    listJobBySystem.addAll((List) completedTaskCountMap.get("30_taskDetail"));
                    strDurationValue = "30";
                }
                routeUtil.prepareDateValue(listJobBySystem);
                //this for-loop is to populate the additional Job-Case's detail
                for (Map completedJobMap : (List<Map>) listJobBySystem) {
                    ApplicationPModel appModel = (ApplicationPModel) dao.getModelById((String)completedJobMap.get("caseId"), ApplicationPModel.class);
                    JobDetailModel jobDetailModel = (JobDetailModel) dao.getModelById((String)completedJobMap.get("caseId"), JobDetailModel.class);
                    
                    if(appModel!=null){
                        completedAppCount++;
                    }else if (jobDetailModel!=null){
                        completedJobCount++;
                    }
                }
            }
        } catch (Exception e) {
            initResult = "N";
            submitMsg = "e: " + e ;
            e.printStackTrace();
        } finally {
            dao.closeSession();
        }
        
        json.put("status", initResult);
        json.put("appCount", appCount);
        json.put("jobCount", jobCount);
        json.put("completedAppCount", completedAppCount);
        json.put("completedJobCount", completedJobCount);
        json.put("message", submitMsg);
        
        response.setContentType("application/json");
        response.getWriter().write(json.toString());
    }

}
