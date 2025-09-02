/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sains.workflow.util;

import com.SysConf;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Validator;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.sam.dao.UserGroupDAOImpl;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.hibernate.Session;

/**
 *
 * @author thensw
 */
public class RouteUtil {
    private final Boolean DEBUG_MODE = Boolean.FALSE;
    private final String SYSTEM_CODE = "USJ";
//    private final String WorkflowURL = "https://workflow-tnt.sarawak.gov.my/workflow/WfService_";
    private final String WorkflowURL = SysConf.get("route.url");
    
    public String workflowRESTCall(String function, List params)  {
//        String theUrl = "https://servicetraining.sarawak.gov.my/web/web/api/payment_history/?agency=MAJLIS+PERBANDARAN+PADAWAN&bill_type=Assessment+Bill&bill_ref_no=2234539&bill_amt=37.20&sid=A123456&pymt_channel=epaynow&pymt_datetime=2005-08-12+11:30:05&pymt_trans_no=U19BJYY0100011&object_id=1574150327537CXZSpn0&applicant_name=Test&token=08e277d4a428cf0e7e1e7a7ae9d87297";
//        String theUrl = "https://10.18.4.65/projects/appsuite/branches/dash/api/request_token?client_id=sifbas&secret=ssds7654x4z56iqmcal3ssdszqkc9t160820193d&ldap_id=anthonyckk";
//        String theUrl = "https://www.google.com";

        String theUrl = WorkflowURL + function;
        Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            try {
                
                SSLContext sslContext = new SSLContextBuilder()
                        .loadTrustMaterial(null, (certificate, authType) -> true).build();
                org.apache.http.impl.client.CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
                        .build();
//                HttpGet httpGet = new HttpGet(theUrl);
//                CloseableHttpResponse resp = httpClient.execute(httpGet);
                HttpPost httpPost= new HttpPost(theUrl);
//                HttpGet httpPost= new HttpGet(theUrl);
                List<NameValuePair> arguments = new ArrayList();
                Map map = new HashMap();
                map.put("params", params);
                arguments.add(new BasicNameValuePair("jsonData", gson.toJson(map)));
                httpPost.setEntity(new UrlEncodedFormEntity(arguments));
                CloseableHttpResponse resp = httpClient.execute(httpPost);
                String result;
                String overall="";
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(resp.getEntity().getContent()));
                while ((result = br.readLine()) != null){
                    overall = overall + "\n" + result;
                }
//                Debug.printDebug("overall = " + overall);
                resp.close();
                return overall;
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }
    
    public void validateWebserviceReturn(Map workflowMap, String triggerFrom) throws Exception {
        if (DEBUG_MODE) Debug.printDebug("workflowMap = " + workflowMap);
        if (!(workflowMap.get("status") != null && workflowMap.get("status").equals("success"))) {
            if (workflowMap.get("status") != null) { //got return from Route Webservice but status != success (fail)
                throw new CustomBaseException("Workflow Error >> " + workflowMap.get("errMsg"));
            } else {
                throw new CustomBaseException("Fail to connect to workflow server. ["+ triggerFrom +"]");
            }
        }
    }
    
    public Map getWorkflowEstimationBy_caseId(String caseId) {
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List list = new ArrayList();
        list.add("wf_system::="+SYSTEM_CODE); 
        list.add("by_caseId::="+caseId); 
//        String rtn = port.getWorkflowEstimation(list);
        String rtn = workflowRESTCall("getWorkflowEstimation", list);
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map startWithError() {
        List list = new ArrayList();
        String rtn = workflowRESTCall("startWorkflowInstance", list);
        if (DEBUG_MODE) Debug.printDebug("startWasteBinApplicationWorkflow call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map grabTaskByActivityCode(String caseId, String activityCode) {
        List list = new ArrayList();
        list.add("caseId::=" + caseId);
        list.add("wf_system::=" + SYSTEM_CODE);
        String rtn = workflowRESTCall("grabTask", list);
        if (DEBUG_MODE) Debug.printDebug("grabTaskByActivityCode call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map completeTaskByActivityCode(String caseId, String activityCode, String... outputParam) {
        List<String> list = new ArrayList();
        list.add("caseId::="+caseId);
        list.add("activityCode::="+activityCode);
        Boolean completeValueFound = Boolean.FALSE;
        if (outputParam != null) {
            for (String param : outputParam) {
                if (param.startsWith("_completeValue::=")) {
                    completeValueFound = Boolean.TRUE;
                }
                list.add(param); 
            }
        }
        if (!completeValueFound) { //default the completeValue to N if not found
            list.add("_completeValue::=N"); 
        }
        String rtn = workflowRESTCall("completeTask", list);
        if (DEBUG_MODE) Debug.printDebug("completeTaskByActivityCode call return :: " + rtn);
        return getMapFromJson(rtn);
    }

    public Map getSubSystem(String wf_system, String userGroup) {
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List list = new ArrayList();
        list.add("wf_system::="+wf_system);
        list.add("userGroup::="+userGroup);
//        String rtn = port.getSubSystem(list);
        String rtn = workflowRESTCall("getSubSystem", list);
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map getTaskInPool(String wf_system, String subSystem, String searchBy, String searchByData, List params) {
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List list = new ArrayList();
        list.add("wf_system::="+wf_system);
        list.add("subSystem::="+subSystem);
        list.add("searchBy::="+searchBy);
        list.add("searchByData::="+searchByData);
        list.add("req4_case_no::=case_no");//to get parameter passed to workflow when start a workflow
//        list.add("req4_data2::=data2");//to get parameter passed to workflow when start a workflow
        if (params != null && !params.isEmpty()) {
            list.addAll(params);
        }
//        String rtn = port.getTaskInPool(list);
        String rtn = workflowRESTCall("getTaskInPool", list);
//        Debug.printDebug("getTaskInPool string");
//        Debug.printDebug(rtn);
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map checkIsMyPendingTask(String wf_system, String searchByData, String caseId, String activityCode) {
        return checkIsMyPendingTask(wf_system, searchByData, caseId, activityCode, (String[])null);
    }
    
    public Map checkIsMyPendingTask(String wf_system, String searchByData, String caseId, String activityCode, String... requestData) {
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List list = new ArrayList();
        list.add("wf_system::="+wf_system);
        list.add("searchBy::=doer");
        list.add("searchByData::="+searchByData);
        list.add("caseId::="+caseId);
        list.add("activityCode::="+activityCode);
        if (requestData != null) {
            for (String dataRequested : requestData) {
                list.add("req4_"+dataRequested+"::="+dataRequested);
            }
        }
//        String rtn = port.getPendingTask(list);
        String rtn = workflowRESTCall("getPendingTask", list);
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map getPendingTask(String wf_system, String searchBy, String searchByData) {
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List list = new ArrayList();
        list.add("wf_system::="+wf_system);
        list.add("searchBy::="+searchBy);
        list.add("searchByData::="+searchByData);
        
        list.add("req4_case_no::=case_no");//to get parameter passed to workflow when start a workflow
//        String rtn = port.getPendingTask(list);
        String rtn = workflowRESTCall("getPendingTask", list);
//        Debug.printDebug("getTaskInPool string");
//        Debug.printDebug(rtn);
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public String getTaskWorkflowDiagram(String id) {
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List list = new ArrayList();
        list.add("id::="+id);
//        String rtn = port.getTaskWorkflowDiagram(list);
        String rtn = workflowRESTCall("getTaskWorkflowDiagram", list);
        return rtn;
    }
    
    public void prepareDateValue(List<Map> listOfMap, String dateField) throws Exception {
        for (Map<String, Object> mapData : listOfMap) {
            if (mapData.containsKey(dateField)) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                mapData.put(dateField, DateUtil.getTimestampFromDate(formatter.parse((String)mapData.get(dateField))));
            }
        }
    }
    
    public void prepareDateValue(Map theMap, String dateField) throws Exception {
        if (theMap != null && theMap.containsKey(dateField)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            theMap.put(dateField, DateUtil.getTimestampFromDate(formatter.parse((String)theMap.get(dateField))));
        }
    }
    
    List<String> dateFields = Arrays.asList(new String[] {"today", "due_date","grabbed_date","completion_date","created_date", "assigned_date"});
    public void prepareDateValue(List<Map> listOfMap) throws Exception {
        for (Map<String, Object> mapData : listOfMap) {
            for (String key : mapData.keySet()) {
                if (dateFields.contains(key)) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                    mapData.put(key, DateUtil.getTimestampFromDate(formatter.parse((String)mapData.get(key))));
                }
            }
        }
    }
    
    private final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    public Map getMapFromJson(String jsonString) {
        return gson.fromJson(jsonString, Map.class);
    }
    
    
    public String getWorkflowGroup(Session session, String userId, String sysId) {
        UserGroupDAOImpl setupGroupImpl = new UserGroupDAOImpl();
        setupGroupImpl.setSession(session);
//        Debug.printDebug("getWorkflowGroup()");
        List<SetupGroup> workflowGroupList = setupGroupImpl.getWorkflowGroupByUserId(userId, sysId);
        String workflowGroups = null;
        for (SetupGroup workflowGroup : workflowGroupList) {
            if (workflowGroups == null) {
                workflowGroups = workflowGroup.getGroup_code();
            } else {
                workflowGroups += ","+workflowGroup.getGroup_code();
            }
//            Debug.printDebug("workflowGroup.getGroup_code()::   "+workflowGroups);
        }
        return workflowGroups;
    }
    
    public String getWorkflowGroup(Session session, String userId) {
        UserGroupDAOImpl setupGroupImpl = new UserGroupDAOImpl();
        setupGroupImpl.setSession(session);
        List<SetupGroup> workflowGroupList = setupGroupImpl.getWorkflowGroupByUserId(userId);
        String workflowGroups = null;
        for (SetupGroup workflowGroup : workflowGroupList) {
            if (workflowGroups == null) {
                workflowGroups = workflowGroup.getGroup_code();
//                Debug.printDebug("workflowGroup.getGroup_code()::   "+workflowGroup.getGroup_code());
            } else {
                workflowGroups += ","+workflowGroup.getGroup_code();
            }
        }
        return workflowGroups;
    }
    
    public Map grabTask(String taskId, String grabBy) {
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List list = new ArrayList();
        list.add("taskId::="+taskId); 
        list.add("grabBy::="+grabBy); 
//        String rtn = port.grabTask(list);
        String rtn = workflowRESTCall("grabTask", list);
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map getCurrentOIC(String caseId) {
        return getTaskDetail("getCurrentOIC::=Y", "caseId::="+caseId);
    }
    
    public String getAdminTask(List list) {
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
//        return port.getAdminTask(list); 
        return workflowRESTCall("getAdminTask", list);
    }
    
    public Map getTaskDetail(String... taskDetailIdAndParam) {
        List list = new ArrayList();
        for (String str : taskDetailIdAndParam) {
            list.add(str);
        }
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
//        String rtn = port.getTaskDetail(list);
        String rtn = workflowRESTCall("getTaskDetail", list);
//        Debug.printDebug("getTaskDetail string");
        Debug.printDebug("getTaskDetail" + rtn);
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map getTaskDetailWithTaskProgress(Boolean fullList, String... taskDetailIdAndParam) {
        List list = new ArrayList();
        
        for (String str : taskDetailIdAndParam) {
            list.add(str);
        }
        list.add("taskProgressList::=Y");
        if (fullList) {
            list.add("fullList::=Y");
        }
        
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
//        String rtn = port.getTaskDetail(list);
        String rtn = workflowRESTCall("getTaskDetail", list);
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map assignTask(List list) {
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
//        String rtn = port.assignTask(list);
        String rtn = workflowRESTCall("assignTask", list);        
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map completePendingTask(String caseId, String... outputParam) {
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List<String> list = new ArrayList();
        list.add("caseId::="+caseId);
        Boolean completeValueFound = Boolean.FALSE;
        if (outputParam != null) {
            for (String param : outputParam) {
                if (param.startsWith("_completeValue::=")) {
                    completeValueFound = Boolean.TRUE;
                }
                list.add(param); 
            }
        }
        if (!completeValueFound) { //default the completeValue to Y if not found
            list.add("_completeValue::=Y"); 
        }
//        String rtn = port.completeTask(list);
        String rtn = workflowRESTCall("completeTask", list);

//        Debug.printDebug("completePendingTask string");
//        Debug.printDebug(rtn);
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map completeTask(String taskId, String... outputParam) {
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
    Debug.printDebug("taskId completeTask " + taskId);
        List<String> list = new ArrayList();
        list.add("taskId::="+taskId); 
        Boolean completeValueFound = Boolean.FALSE;
        if (outputParam != null) {
            for (String param : outputParam) {
                Debug.printDebug("param !!!!!!!!!!! " + param);
                if (param == null) {
                    continue;
                }
                if (param.startsWith("_completeValue::=")) {
                    completeValueFound = Boolean.TRUE;
                }
                if (!Validator.isEmpty(param)) {
                    list.add(param); 
                }
            }
        }
        if (!completeValueFound) { //default the completeValue to N if not found
            list.add("_completeValue::=N"); 
        }
//        String rtn = port.completeTask(list);
        String rtn = workflowRESTCall("completeTask", list);
        Debug.printDebug("completeTask string" + rtn);
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map batchCompleteTask(String caseIds, String batchAppActivityCode) {
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List<String> list = new ArrayList();
        list.add("caseIds::="+caseIds); 
        list.add("batchAppActivityCode::="+batchAppActivityCode); 
//        String rtn = port.batchCompleteTask(list);
        String rtn = workflowRESTCall("batchCompleteTask", list);
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map getProcessedTaskCount(String wf_system, String dayBefore, String taskDoer, String... otherParam) {
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List<String> list = new ArrayList();
        list.add("wf_system::="+wf_system); 
        list.add("dayBefore::="+dayBefore); 
        list.add("taskDoer::="+taskDoer);
        list.add("req4_case_no::=case_no");//to get parameter passed to workflow when start a workflow
        list.add("populateTaskDetail::=Y");//to get parameter passed to workflow when start a workflow
        if (otherParam != null) {
            for (String param : otherParam) {
                list.add(param); 
            }
        }
//        String rtn = port.getProcessedTaskCount(list);
        String rtn = workflowRESTCall("getProcessedTaskCount", list);
//        Debug.printDebug("getProcessedTaskCount string");
//        Debug.printDebug(rtn);
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map getEstimatedNextTask(String taskId, String completeValue, String... otherParam) {
//        WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//        WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List<String> list = new ArrayList();
        list.add("task_id::="+taskId); 
        list.add("nextWorkflowTask::=Y"); 
        list.add("_completeValue::="+completeValue); 
        if (otherParam != null) {
            for (String param : otherParam) {
                list.add(param); 
            }
        }
//        String rtn = port.getWorkflowEstimation(list);
        String rtn = workflowRESTCall("getWorkflowEstimation", list);
//        Debug.printDebug("getEstimatedNextTask string");
//        Debug.printDebug(rtn);
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map checkIsMyPendingCase(String wf_system, String searchByData, String caseId) {
//       WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//       WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List list = new ArrayList();
        list.add("wf_system::="+wf_system);
        list.add("searchBy::=doer");
        list.add("searchByData::="+searchByData);
        list.add("caseId::="+caseId);
        list.add("activityCode::=isMyPendingCase_");
//       String rtn = port.getPendingTask(list);
        String rtn = workflowRESTCall("getPendingTask", list);

        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
   }
    
    public Map getPendingTaskOfCase(String wf_system, String caseId) {
//       WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();
//       WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List list = new ArrayList();
        list.add("wf_system::="+wf_system);
        list.add("caseId::="+caseId);
        list.add("currentTask::=Y");
//       String rtn = port.getPendingTask(list);
        String rtn = workflowRESTCall("getPendingTask", list);
        if (DEBUG_MODE) Debug.printDebug("call return :: " + rtn);
        return getMapFromJson(rtn);
   }
    
    public Map startUSJ001(String caseID, String app_div, String app_name, String usj_no, String app_type, String case_no, String requester_triggerBy, String strTaskAssignTo, String user_us_id) {
        //WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();        //WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        //WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List list = new ArrayList();
        list.add("wf_code::=USJ001");
        list.add("app_div::="+app_div); 
        list.add("app_name::="+app_name); 
        list.add("usj_no::="+usj_no); 
        list.add("app_type::="+app_type); 
        list.add("userName::=PROCESSING OFFICER"); 
        list.add("case_no::="+case_no); 
        list.add("caseId::="+caseID);
        list.add("triggerBy::="+requester_triggerBy); 
        list.add("systemCode::="+SYSTEM_CODE); 
        
        Debug.printDebug("startUSJ001 strTaskAssignTo :"+strTaskAssignTo);
        if(!Validator.isEmpty(strTaskAssignTo)){
            list.add("USJ001_00_02_assignTo::="+strTaskAssignTo); 
        }
        
        String rtn = workflowRESTCall("startWorkflowInstance", list);
        if (DEBUG_MODE) Debug.printDebug("startUSJ001 call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map startUSJ002(String caseID, String app_div, String app_name, String usj_no, String app_type, String case_no, String requester_triggerBy, String strTaskAssignTo, String user_us_id) {
        //WebServiceWorkflow_Service service = new WebServiceWorkflow_Service();        //WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        //WebServiceWorkflow port = service.getWebServiceWorkflowPort();
        List list = new ArrayList();
        list.add("wf_code::=USJ002");
        list.add("app_div::="+app_div); 
        list.add("app_name::="+app_name); 
        list.add("usj_no::="+usj_no); 
        list.add("app_type::="+app_type); 
        list.add("userName::=PROCESSING OFFICER"); 
        list.add("case_no::="+case_no); 
        list.add("caseId::="+caseID);
        list.add("triggerBy::="+requester_triggerBy); 
        list.add("systemCode::="+SYSTEM_CODE); 
        
        Debug.printDebug("startUSJ002 strTaskAssignTo :"+strTaskAssignTo);
        if(!Validator.isEmpty(strTaskAssignTo)){
            list.add("USJ001_01_02_assignTo::="+strTaskAssignTo); 
        }
        
        String rtn = workflowRESTCall("startWorkflowInstance", list);
        if (DEBUG_MODE) Debug.printDebug("startUSJ002 call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map startUSJ003_1(String jobID, String app_div, String app_name, String usj_no, String app_type, String case_no, String requester_triggerBy, String strTaskAssignTo, String user_us_id) {
        List list = new ArrayList();
        list.add("wf_code::=USJ003_01");
        list.add("app_div::="+app_div); 
        list.add("app_name::="+app_name); 
        list.add("usj_no::="+usj_no); 
        list.add("app_type::="+app_type); 
        list.add("userName::=PROCESSING OFFICER"); 
        list.add("case_no::="+case_no); 
        list.add("caseId::="+jobID);
        list.add("triggerBy::="+requester_triggerBy); 
        list.add("systemCode::="+SYSTEM_CODE); 
        
        Debug.printDebug("startUSJ003_1 strTaskAssignTo :"+strTaskAssignTo);
        if(!Validator.isEmpty(strTaskAssignTo)){
            list.add("USJ003_01_01_assignTo::="+strTaskAssignTo); 
        }
        
        String rtn = workflowRESTCall("startWorkflowInstance", list);
        if (DEBUG_MODE) Debug.printDebug("startUSJ003_1 call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map startUSJ003_02(String jobID, String app_div, String app_name, String usj_no, String app_type, String case_no, String requester_triggerBy, String strTaskAssignTo, String user_us_id) {
        List list = new ArrayList();
        list.add("wf_code::=USJ003_02");
        list.add("app_div::="+app_div); 
        list.add("app_name::="+app_name); 
        list.add("usj_no::="+usj_no); 
        list.add("app_type::="+app_type); 
        list.add("userName::=PROCESSING OFFICER"); 
        list.add("case_no::="+case_no); 
        list.add("caseId::="+jobID);
        list.add("triggerBy::="+requester_triggerBy); 
        list.add("systemCode::="+SYSTEM_CODE); 
        
        Debug.printDebug("startUSJ003_02 strTaskAssignTo :"+strTaskAssignTo);
        if(!Validator.isEmpty(strTaskAssignTo)){
            list.add("USJ003_02_02_assignTo::="+strTaskAssignTo); 
        }
        
        String rtn = workflowRESTCall("startWorkflowInstance", list);
        if (DEBUG_MODE) Debug.printDebug("startUSJ003_02 call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map startUSJ003_03(String jobID, String app_div, String app_name, String usj_no, String app_type, String case_no, String requester_triggerBy, String strTaskAssignTo, String user_us_id, String complete_value) {
        List list = new ArrayList();
        list.add("wf_code::=USJ003_03");
        list.add("app_div::="+app_div); 
        list.add("app_name::="+app_name); 
        list.add("usj_no::="+usj_no); 
        list.add("app_type::="+app_type); 
        list.add("userName::=PROCESSING OFFICER"); 
        list.add("case_no::="+case_no); 
        list.add("caseId::="+jobID);
        list.add("triggerBy::="+requester_triggerBy); 
        list.add("systemCode::="+SYSTEM_CODE); 
        
        Debug.printDebug("startUSJ003_03 complete_value :"+complete_value);
        if(!Validator.isEmpty(complete_value)){
            list.add("_completeValue::=B");  //to include for resubmission
        }
        Debug.printDebug("startUSJ003_03 strTaskAssignTo :"+strTaskAssignTo);
        if(!Validator.isEmpty(strTaskAssignTo)){
            list.add("USJ003_03_02_assignTo::="+strTaskAssignTo); 
            list.add("USJ003_03_03_assignTo::="+strTaskAssignTo); 
        }
        
        String rtn = workflowRESTCall("startWorkflowInstance", list);
        if (DEBUG_MODE) Debug.printDebug("startUSJ003_03 call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map startUSJ003_04(String jobID, String app_div, String app_name, String usj_no, String app_type, String case_no, String requester_triggerBy, String strTaskAssignTo, String user_us_id) {
        List list = new ArrayList();
        list.add("wf_code::=USJ003_04");
        list.add("app_div::="+app_div); 
        list.add("app_name::="+app_name); 
        list.add("usj_no::="+usj_no); 
        list.add("app_type::="+app_type); 
        list.add("userName::=PROCESSING OFFICER"); 
        list.add("case_no::="+case_no); 
        list.add("caseId::="+jobID);
        list.add("triggerBy::="+requester_triggerBy); 
        list.add("systemCode::="+SYSTEM_CODE); 
//        list.add("_completeValue::=R"); 
        
        Debug.printDebug("startUSJ003_04 strTaskAssignTo :"+strTaskAssignTo);
        if(!Validator.isEmpty(strTaskAssignTo)){
            list.add("USJ003_04_02_assignTo::="+strTaskAssignTo); 
        }
        
        String rtn = workflowRESTCall("startWorkflowInstance", list);
        if (DEBUG_MODE) Debug.printDebug("startUSJ003_04 call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map startUSJ003_05(String jobID, String app_div, String app_name, String usj_no, String app_type, String case_no, String requester_triggerBy, String strTaskAssignTo, String user_us_id) {
        List list = new ArrayList();
        list.add("wf_code::=USJ003_05");
        list.add("app_div::="+app_div); 
        list.add("app_name::="+app_name); 
        list.add("usj_no::="+usj_no); 
        list.add("app_type::="+app_type); 
        list.add("userName::=PROCESSING OFFICER"); 
        list.add("case_no::="+case_no); 
        list.add("caseId::="+jobID);
        list.add("triggerBy::="+requester_triggerBy); 
        list.add("systemCode::="+SYSTEM_CODE); 
//        list.add("_completeValue::=R"); 
        
        Debug.printDebug("startUSJ003_05 strTaskAssignTo :"+strTaskAssignTo);
        if(!Validator.isEmpty(strTaskAssignTo)){
            list.add("USJ003_05_02_assignTo::="+strTaskAssignTo); 
        }
        
        String rtn = workflowRESTCall("startWorkflowInstance", list);
        if (DEBUG_MODE) Debug.printDebug("startUSJ003_05 call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map startUSJ003_06(String jobID, String app_div, String app_name, String usj_no, String app_type, String case_no, String requester_triggerBy, String strTaskAssignTo, String user_us_id) {
        List list = new ArrayList();
        list.add("wf_code::=USJ003_06");
        list.add("app_div::="+app_div); 
        list.add("app_name::="+app_name); 
        list.add("usj_no::="+usj_no); 
        list.add("app_type::="+app_type); 
        list.add("userName::=PROCESSING OFFICER"); 
        list.add("case_no::="+case_no); 
        list.add("caseId::="+jobID);
        list.add("triggerBy::="+requester_triggerBy); 
        list.add("systemCode::="+SYSTEM_CODE); 
//        list.add("_completeValue::=R"); 
        
        Debug.printDebug("startUSJ003_06 strTaskAssignTo :"+strTaskAssignTo);
        if(!Validator.isEmpty(strTaskAssignTo)){
            list.add("USJ003_06_02_assignTo::="+strTaskAssignTo); 
        }
        
        String rtn = workflowRESTCall("startWorkflowInstance", list);
        if (DEBUG_MODE) Debug.printDebug("startUSJ003_06 call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map startUSJ003_07(String jobID, String app_div, String app_name, String usj_no, String app_type, String case_no, String requester_triggerBy, String strTaskAssignTo, String user_us_id) {
        List list = new ArrayList();
        list.add("wf_code::=USJ003_07");
        list.add("app_div::="+app_div); 
        list.add("app_name::="+app_name); 
        list.add("usj_no::="+usj_no); 
        list.add("app_type::="+app_type); 
        list.add("userName::=PROCESSING OFFICER"); 
        list.add("case_no::="+case_no); 
        list.add("caseId::="+jobID);
        list.add("triggerBy::="+requester_triggerBy); 
        list.add("systemCode::="+SYSTEM_CODE); 
//        list.add("_completeValue::=R"); 
        
        Debug.printDebug("startUSJ003_07 strTaskAssignTo :"+strTaskAssignTo);
        if(!Validator.isEmpty(strTaskAssignTo)){
            list.add("USJ003_07_02_assignTo::="+strTaskAssignTo); 
        }
        
        String rtn = workflowRESTCall("startWorkflowInstance", list);
        if (DEBUG_MODE) Debug.printDebug("startUSJ003_07 call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map startUSJ004_01(String jobID, String app_div, String app_name, String usj_no, String app_type, String case_no, String requester_triggerBy, String strTaskAssignTo, String user_us_id) {
        List list = new ArrayList();
        list.add("wf_code::=USJ004_01");
        list.add("app_div::="+app_div); 
        list.add("app_name::="+app_name); 
        list.add("usj_no::="+usj_no); 
        list.add("app_type::="+app_type); 
        list.add("userName::=PROCESSING OFFICER"); 
        list.add("case_no::="+case_no); 
        list.add("caseId::="+jobID);
        list.add("triggerBy::="+requester_triggerBy); 
        list.add("systemCode::="+SYSTEM_CODE); 
        
        Debug.printDebug("startUSJ004_01 strTaskAssignTo :"+strTaskAssignTo);
        if(!Validator.isEmpty(strTaskAssignTo)){
            list.add("USJ004_01_02_assignTo::="+strTaskAssignTo); 
        }
        
        String rtn = workflowRESTCall("startWorkflowInstance", list);
        if (DEBUG_MODE) Debug.printDebug("startUSJ004_01 call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map startUSJ004_02(String jobID, String app_div, String app_name, String usj_no, String app_type, String case_no, String requester_triggerBy, String strTaskAssignTo, String user_us_id) {
        List list = new ArrayList();
        list.add("wf_code::=USJ004_02");
        list.add("app_div::="+app_div); 
        list.add("app_name::="+app_name); 
        list.add("usj_no::="+usj_no); 
        list.add("app_type::="+app_type); 
        list.add("userName::=PROCESSING OFFICER"); 
        list.add("case_no::="+case_no); 
        list.add("caseId::="+jobID);
        list.add("triggerBy::="+requester_triggerBy); 
        list.add("systemCode::="+SYSTEM_CODE); 
        
        Debug.printDebug("startUSJ004_02 strTaskAssignTo :"+strTaskAssignTo);
        if(!Validator.isEmpty(strTaskAssignTo)){
            list.add("USJ004_02_02_assignTo::="+strTaskAssignTo); 
        }
        
        String rtn = workflowRESTCall("startWorkflowInstance", list);
        if (DEBUG_MODE) Debug.printDebug("startUSJ004_02 call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map startUSJ004_03(String jobID, String app_div, String app_name, String usj_no, String app_type, String case_no, String requester_triggerBy, String strTaskAssignTo, String user_us_id) {
        List list = new ArrayList();
        list.add("wf_code::=USJ004_03");
        list.add("app_div::="+app_div); 
        list.add("app_name::="+app_name); 
        list.add("usj_no::="+usj_no); 
        list.add("app_type::="+app_type); 
        list.add("userName::=PROCESSING OFFICER"); 
        list.add("case_no::="+case_no); 
        list.add("caseId::="+jobID);
        list.add("triggerBy::="+requester_triggerBy); 
        list.add("systemCode::="+SYSTEM_CODE); 
        
        Debug.printDebug("startUSJ004_03 strTaskAssignTo :"+strTaskAssignTo);
        if(!Validator.isEmpty(strTaskAssignTo)){
            list.add("USJ004_03_02_assignTo::="+strTaskAssignTo); 
        }
        
        String rtn = workflowRESTCall("startWorkflowInstance", list);
        if (DEBUG_MODE) Debug.printDebug("startUSJ004_03 call return :: " + rtn);
        return getMapFromJson(rtn);
    }
    
    public Map startUSJ005_01(String jobID, String app_div, String app_name, String usj_no, String app_type, String case_no, String requester_triggerBy, String strTaskAssignTo, String user_us_id) {
        List list = new ArrayList();
        list.add("wf_code::=USJ005_01");
        list.add("app_div::="+app_div); 
        list.add("app_name::="+app_name); 
        list.add("usj_no::="+usj_no); 
        list.add("app_type::="+app_type); 
        list.add("userName::=PROCESSING OFFICER"); 
        list.add("case_no::="+case_no); 
        list.add("caseId::="+jobID);
        list.add("triggerBy::="+requester_triggerBy); 
        list.add("systemCode::="+SYSTEM_CODE); 
        
        Debug.printDebug("startUSJ005_01 strTaskAssignTo :"+strTaskAssignTo);
        if(!Validator.isEmpty(strTaskAssignTo)){
            list.add("USJ005_01_01_assignTo::="+strTaskAssignTo); 
        }
        
        String rtn = workflowRESTCall("startWorkflowInstance", list);
        if (DEBUG_MODE) Debug.printDebug("startUSJ005_01 call return :: " + rtn);
        return getMapFromJson(rtn);
    }
}
