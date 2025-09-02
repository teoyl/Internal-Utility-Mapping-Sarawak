package com.sains.framework.base;

import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.model.SetupSystemModel;
import com.sains.framework.model.User;
import com.sains.workflow.util.RouteUtil;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.JobDetailModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

public class SpecialSearch {
    private final String CASE_REF = "case_no";
    private final String DivCode = "app_div";
    
    private String getUserIds(String nameCriteria, BaseDAO dao) {
        Map param = new HashMap();
        String rtnStr = null;
        param.put("us_user_name", nameCriteria);
        Debug.printDebug("param = " + param);
        List<User> list = dao.list(param, User.class, Boolean.TRUE);
        for (User user : list) {
            if (rtnStr == null) {
                rtnStr = user.getUs_user_id();
            } else {
                rtnStr += "," + user.getUs_user_id();
            }
        }
        return rtnStr;
    }

    public Boolean retrieveFromRoute(BaseActionSupport form, HttpServletRequest request) throws Exception {
        //remember to set the numberOfRows, result
//        Calendar cal = Calendar.getInstance();
        RouteUtil routeUtil = new RouteUtil();
        List<String> routeParamList = new ArrayList();
        BaseDAO dao = new BaseDAOImpl();
        try {
            if (form.getPageNo() == null) {
                form.setPageNo(1);
            }
            routeParamList.add("pageNo::=" + form.getPageNo());
            routeParamList.add("pageSize::=" + form.getPageSize());
            //ChangMH @ 2019-12-27 : filter by division code
            if(form.getAction().contains("Div")){
                String divList = ActionContext.getContext().getSession().get("div_assigned").toString();
                String formattedDivList = divList.replace("[", "").replace("]", "");
                routeParamList.add("taskData_"+DivCode+"::=" +formattedDivList);
            }
            if (!Validator.isEmpty((String)form.getSearchedParam().get("divisionCode"))) {
                //
                    routeParamList.add("taskData_"+DivCode+"::=" + form.getSearchedParam().get("divisionCode"));
            }
            if (form.getSearchedParam().get("_date_pool.pl_assign_date") != null) {
                if (form.getSearchedParam().get("_date_pool.pl_assign_date").toString().indexOf("_comma_") >= 0) {//oracle/postgres??
                    String[] dateArr = form.getSearchedParam().get("_date_pool.pl_assign_date").toString().split(",");
                    int idx = dateArr[0].indexOf("to_date");
                    routeParamList.add("dateFrom::=" + dateArr[0].substring(idx+9, idx+19));
                    idx = dateArr[1].indexOf("to_date");
                    routeParamList.add("dateTo::=" + dateArr[1].substring(idx+9, idx+19));
                } else if (form.getSearchedParam().get("_date_pool.pl_assign_date").toString().indexOf(">=") >= 0) {
                    routeParamList.add("dateFrom::=" + form.getSearchedParam().get("_date_pool.pl_assign_date").toString().substring(3, 13));
                    int idx = form.getSearchedParam().get("_date_pool.pl_assign_date").toString().indexOf("<");
                    if (idx >= 0) {
                        routeParamList.add("dateTo::=" + form.getSearchedParam().get("_date_pool.pl_assign_date").toString().substring(idx+2, idx+12));
                    }
                }
//                if (!Validator.isEmpty(request.getParameter("search_pl_assign_dateFrom"))) { //dateFrom not empty
//                    cal.setTime(DateUtil.getDate(request.getParameter("search_pl_assign_dateFrom"), form.getText("date_default_date_dr")));
//                    
//                }
//                if (!Validator.isEmpty(request.getParameter("search_pl_assign_dateTo"))) { //dateTo not empty
//                    cal.setTime(DateUtil.getDate(request.getParameter("search_pl_assign_dateTo"), form.getText("date_default_date_dr")));
//                    cal.add(Calendar.DAY_OF_YEAR, 1);
//                    routeParamList.add("dateTo::=" + Formatter.formatDate(cal.getTime(), "yyyy-MM-dd"));
//                }
            }
            routeParamList.add("wf_system::=" + SystemConstants.WF_SYSTEM_CODE);
            if (!Validator.isEmpty((String)form.getSearchedParam().get("case_no"))) {
                //
                    routeParamList.add("taskData_"+CASE_REF+"::=" + form.getSearchedParam().get("case_no"));
            }
            if (!Validator.isEmpty((String)form.getSearchedParam().get("usj_no"))) {
                //
                    routeParamList.add("taskData_usj_no"+"::=" + form.getSearchedParam().get("usj_no"));
            }
            if (!Validator.isEmpty((String)form.getSearchedParam().get("workflow.system_id"))) {
                routeParamList.add("wf_subsystem::=" + form.getSearchedParam().get("workflow.system_id"));
            } else {
                List<SetupSystemModel> systemList = new CommonList(dao.getSession()).getSystemListRoute();
                String tempSysId = null;
                for (SetupSystemModel system : systemList) {
                    if (tempSysId == null) {
                        tempSysId = system.getSystem_code();
                    } else {
                        tempSysId += ","+system.getSystem_code();
                    }
                }
//                if (!Validator.isEmpty(tempSysId)) {
//                    routeParamList.add("wf_subsystem::=" + tempSysId);
//                    routeParamList.add("wf_subsystem::=STA");
//                }
            }
            if (!Validator.isEmpty((String)form.getSearchedParam().get("su.us_user_name"))) { //Requester/Creator of the workflow
//                String requesterIds = getUserIds((String)form.getSearchedParam().get("su.us_user_name"), dao);
//                if (requesterIds != null) {
//                    routeParamList.add("requester::=" + requesterIds);
//                }
                routeParamList.add("assigned_by::=" + (String)form.getSearchedParam().get("su.us_user_name"));
            }
//            if (!Validator.isEmpty((String)form.getSearchedParam().get("jpuser.us_user_name"))) { //Task Doer
//                String doerIds = getUserIds((String)form.getSearchedParam().get("jpuser.us_user_name"), dao);
//                if (doerIds != null) {
//                    routeParamList.add("doer::=" + doerIds);
//                }
//            }
            //Search using us_user_id
            if (!Validator.isEmpty((String)form.getSearchedParam().get("jpuser.us_user_name"))) { //Task Doer
                String doerIds = getUserIds((String)form.getSearchedParam().get("jpuser.us_user_name"), dao);
                if (doerIds != null) {
                    routeParamList.add("doer::=" + doerIds);
                }
            }
            if (!Validator.isEmpty((String)form.getSearchedParam().get("pool.pl_status"))) { //Task status
                String status = "";
                switch ((String)form.getSearchedParam().get("pool.pl_status")) {
                    case "B":
                        status = "20";
                        break;
                    case "C":
                        status = "40";
                        break;
                    case "D":
                        status = "25";
                        break;
                    default:
                        status = (String)form.getSearchedParam().get("pool.pl_status");
                }
                routeParamList.add("task_status::=" + status);
            } else {
                routeParamList.add("task_status::=" + "20");
            }
//            if (!Validator.isEmpty(request.getParameter("search_task_desc"))) { //Task Description
//                routeParamList.add("task_desc::=" + request.getParameter("search_task_desc"));
//            }
            if (form.getSqlOrderBy() != null && form.getSqlOrderBy().length() > 0) {
                form.setSqlOrderBy(form.getDynamicSortBy());
            }
            Debug.printFrameworkDebug("form.getSqlOrderBy = " + form.getSqlOrderBy());
            if (form.getSqlOrderBy() != null && form.getSqlOrderBy().length() > 0) {
                if (form.getSqlOrderBy().equals("pl_assign_date_disp") || form.getSqlOrderBy().equals("pl_assign_date")) {
                    routeParamList.add("orderBy::=task.created_date " + (form.getDynamicSortOrder().equals("A") ? "asc " : "desc ") );
                } else if (form.getSqlOrderBy().equals("summaryjob")) {
                    routeParamList.add("orderBy::=t_workflow.wf_name "+ (form.getDynamicSortOrder().equals("A") ? "asc " : "desc ") +", t_workflow_activity.activity_name " + (request.getParameter("dynamicSortOrder").equals("A") ? "asc " : "desc "));
                } else if (form.getSqlOrderBy().equals("assignfrom_disp")) {
                    routeParamList.add("orderBy::=requester " + (form.getDynamicSortOrder().equals("A") ? "asc " : "desc "));
                } else if (form.getSqlOrderBy().equals("assignto_disp")) {
                    routeParamList.add("orderBy::=taskDoer " + (form.getDynamicSortOrder().equals("A") ? "asc " : "desc "));
                } else if (form.getSqlOrderBy().equals("pl_status")) {
                    routeParamList.add("orderBy::=task_status " + (form.getDynamicSortOrder().equals("A") ? "asc " : "desc "));
                } else {
                    routeParamList.add("orderBy::=1 " + (form.getDynamicSortOrder().equals("A") ? "asc " : "desc "));
                }
            } else {
                routeParamList.add("orderBy::=1 " + (form.getDynamicSortOrder().equals("A") ? "asc " : "desc "));
            }
//            Debug.printFrameworkDebug("Time 1 :"+DateUtil.getCurrentTimestamp_nano());
            Debug.printDebug("routeParamList " + routeParamList);
            Map adminTaskMap = routeUtil.getMapFromJson(routeUtil.getAdminTask(routeParamList));
//            Debug.printFrameworkDebug("Time 2 :"+DateUtil.getCurrentTimestamp_nano());    
            if (adminTaskMap.get("status").toString().equals("success")) {
                List<SetupSystemModel> systemList = new CommonList(dao.getSession()).getSystemListRoute();
                
                //for oracle and postgres, need to convert the key to uppercase
                if (adminTaskMap.get("result") != null && ((List)adminTaskMap.get("result")).size() > 0) {
                    for (Map<String, Object> imap : (List<Map<String, Object>>)adminTaskMap.get("result")) {
                        Map<String, Object> newMap = imap.keySet().stream()
                        .collect(Collectors.toMap(key -> key.toUpperCase(), key -> imap.get(key)));
                        form.getResult().add(newMap);
                    }
                }
                //if not oracle or postgres, can use the line below
                //form.setResult((List)adminTaskMap.get("result"));
                form.setNumberOfRows(Integer.parseInt( (String)adminTaskMap.get("totalRecord") ));
                for (Object result : form.getResult() ) {
//                    Debug.printFrameworkDebug("22222222222 " + result);
                    try {
                        String strCaseId = (String)((Map)result).get("CASEID");
//                        Debug.printFrameworkDebug("CaseId :"+strCaseId+" Time 3 :"+DateUtil.getCurrentTimestamp_nano());   
                        User user = (User) dao.getSession().getNamedQuery("User.findByUsUserId").setParameter("us_user_id", ((Map)result).get("TASKDOER")).uniqueResult();
//                        Debug.printFrameworkDebug("Time 4 :"+DateUtil.getCurrentTimestamp_nano());   
                        ((Map)result).put("TASKDOER", ((Map)result).get("TASKDOER"));
                        ((Map)result).put("TASKDOER_USERNAME", user.getUs_user_name());
                        ((Map)result).put("ASSIGNBY_USERNAME", ((Map)result).get("ASSIGNED_BY"));
                        
                        ApplicationPModel appModel = (ApplicationPModel) dao.getModelById((String)((Map)result).get("CASEID"), ApplicationPModel.class);
                        JobDetailModel jobModel = (JobDetailModel) dao.getModelById((String)((Map)result).get("CASEID"), JobDetailModel.class);
                        
                        if(jobModel !=null){
                            ((Map)result).put("CASE_DESC", jobModel.getApplicationModel().getPj_name());
                            String caseRef = "";
                            String aplsub = jobModel.getCase_ref();
                            String usjno = !Validator.isEmpty(jobModel.getUsj_no()) ? jobModel.getUsj_no() : "N/A";
                            caseRef = "<b>App Case Ref. </b>: "+ aplsub + "<br/>" + "<b>USJ No. </b>: " + usjno;
                            ((Map)result).put("CASE_NO", caseRef);
//                            ((Map)result).put("CASE_NO", appModel.getTl_seq()+"/"+appModel.getTl_year());
//                           ((Map)result).put("CASE_URL", "loadViewPageEisLcReg?id="+appModel.getId_licence()+"&actionFrom=RouteJobAdmin&taskid_="+((Map)result).get("TASK_ID"));
//                           ((Map)result).put("CASE_URL", "loadEditPageEisLcReg?id="+appModel.getId_licence()+"&actionFrom=RouteJobAdmin&taskid_="+((Map)result).get("TASK_ID"));
                            
                        }else{
                            ((Map)result).put("CASE_DESC", appModel.getPj_name());
                            String aplsub = appModel.getCase_ref();
                            ((Map)result).put("CASE_NO", "<b>App Case Ref. </b>: "+ aplsub);
                        }
                       
                        ((Map)result).put("system_name", "UTiMAPS System");
                        for (SetupSystemModel system : systemList) {
                            if (system.getSystem_code().equals(((Map)result).get("WF_SUBSYSTEM"))) {
                                ((Map)result).put("system_name", system.getSystem_name());
                                break;
                            }
                        }
                    } catch (Exception e) {
                        
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.closeSession();
        }
    //         routeUtil.getTask
        return Boolean.TRUE ;
    }
}
