package com.sains.workflow.web;

import com.lxg.common.model.Pubcode;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.sains.framework.base.LogFunction;
import java.util.ArrayList;
import java.util.List;
import com.sains.common.util.DateUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.EmailTrigger;
import com.sains.framework.base.ModelBase;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.model.User;
import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import com.sains.framework.sam.dao.UserGroupDAOImpl;
import com.sains.workflow.util.RouteUtil;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.JobDetailModel;
import com.utimaps.model.JobStatusModel;
import com.utimaps.web.UtimapsAction;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

public class RouteJobAdminAction extends BaseActionSupport{

    private static final long serialVersionUID = -6659925652584240539L;
    private String pType = "";
    private String pPlId = "";
    private String pJpId = "";

    private String assignTo = "";

    private List listEntityUser = new ArrayList();

    private Integer intActiveJobProgCount = 0;
    private String jobDesc = "";
    Map mailParam = new HashMap();
    Integer intError = 0;
    private String jobStatistic; //@edmund 10102018
    private List<Map> workloadList = null;

    public RouteJobAdminAction() {
		// set the required field for common check.
        //getRequiredParam().put("module_code", "module.code");
        //getRequiredParam().put("module_name", "module.name");

    }

    public void specificValidation(String validationType) {

    }

    public String processInsert() {
        validateRequired();
        specificValidation("insert");
        if (getActionErrors().size() > 0) {
            return SystemConstants.ACTION_Status.INSERT_FAIL;
        }

        return super.insert(getModel());
    }

    public String loadAddPage() {
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    //retrieve data for editing.
//    public String loadEditPage() {
////        PurchaseDetailDAO poddao = new PurchaseDetailDAOImpl();
//
//        try {
//            setAction("JobAdminRoute");
//            model = super.processEdit(getModel());
//
//            // Load the Job Progress List
//            jobProgList = model.getJobProgressList();
//
//            // Load Entity User
//            JobEngine je = new JobEngine();
////            Debug.printDebug("EN_ID : " + model.getEn_id());
//            listEntityUser = je.getEntityUserList(model);
////            listEntityUser = je.getEntityUserList(model.getEn_id());
////            Debug.printDebug("size: " + listEntityUser.size());
//
//            // PreLoad
//            intActiveJobProgCount = 0;
//
//            jobProgList.size();
//            String strData = "";
//            for (JobProgressModel jpLoop : jobProgList) {
//                try {
//                    if (jpLoop.getJp_status().equals("A") || jpLoop.getJp_status().equals("B")) {
//                        intActiveJobProgCount++;
//                    }
//                    if (jpLoop.getJp_assign_from().equals("SYSTEM")) {//sereneChye @8/10/2014 change to malay.
//                        jpLoop.setJp_assign_from("SISTEM");
//                    }
//
//                    strData = jpLoop.getUserAssignFrom().getUs_user_name();
//                    jpLoop.setJp_assign_from(strData);
//
//                } catch (org.hibernate.ObjectNotFoundException onfe) {
//                    continue;
//                }
//            }
//
//            // Purposely Redundant, else if no From Name... To Name also won't display
//            for (JobProgressModel jpLoop : jobProgList) {
//                try {
//                    strData = jpLoop.getUserAssignTo().getUs_user_name();
//                    jpLoop.setJp_assign_to(strData);
//
//                } catch (org.hibernate.ObjectNotFoundException onfe) {
//                    continue;
//                }
//            }
//
//            // Prod Desc
////            jobDesc = poddao.getPODDesc(model.getPurchaseDetail().getPod_id());
//            jobDesc = new JobEngine().buildJobDesc(baseDAO.getSession(), model);
////            jobDesc= jobDesc.replaceAll("\r\n", "<br/>");
//            // PreLoad - End
//
//        } catch (Exception e) {
//            new LogFunction().logError(this.getClass(), "", e);
//        } finally {
////            poddao.closeSession();
////            closeSession();
////            baseDAO.closeSession();
//        }
//
//        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
//    }
    private RouteUtil routeUtil = new RouteUtil();

    private Map jobAdminMap = null;
    public Map getJobAdminMap() {
        return jobAdminMap;
    }
    public void setJobAdminMap(Map jobAdminMap) {
        this.jobAdminMap = jobAdminMap;
    }
    
    private List<Map> assigneeList = null;
    public List getAssigneeList() {
        return assigneeList;
    }
    public void setAssigneeList(List assigneeList) {
        this.assigneeList = assigneeList;
    }
    
    private String assignTo__;
    public String getAssignTo__() {
        return assignTo__;
    }
    public void setAssignTo__(String assignTo__) {
        this.assignTo__ = assignTo__;
    }
    
    private String jobId;
    private String caseId;
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }
    
    
    public String assign() {
        String rtnStr = SUCCESS;
        try {
            List list = new ArrayList();
            list.add("taskId::=" +  getId()); 
            list.add("assignTo::=" + assignTo__); 
            list.add("assignedBy::=" + ModelBase.get_updatedBy());
            
            Map assignJobMap = routeUtil.assignTask(list);
            if (assignJobMap.get("status") != null && !((String)assignJobMap.get("status")).equals("success")) {
                throw new CustomBaseException((String)assignJobMap.get("errMsg"));
            }
            
            //update job_status 
//            setInternalStatusBackend(jobId,caseId);
            //insert processing history
            
            addActionMessage(this.getText("TaskMgmt.msg.assignSuccessfully", new String[]{assignTo__}));
//        } catch (BaseException be) {
//            addActionError(be.getMessage());
//            return loadEditPage();
        } catch (Exception e) {
            if (e.getCause() != null) {
                addActionError(e.getCause().getMessage());
            } else {
                addActionError(e.getMessage());
            }
            new LogFunction().logError(this.getClass(), "", e);
            return SystemConstants.ACTION_Status.UPDATE_FAIL;
        } 
        return rtnStr;
    }
    
    @Override
    public String loadEditPage() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String taskId = request.getParameter("id");
        Debug.printDebug("getId = " + getId());
        setPageTitle_("Job Administration");
        setPageSubTitle_("Job Assignment");
        try {
            BaseDAO dao = baseDAO;
            jobAdminMap = routeUtil.getTaskDetail("task_id::=" + taskId, "populateRoleOrGroup::=Y", "populateProgressList::=Y");
            
            ApplicationPModel appModel = (ApplicationPModel) dao.getModelById((String)jobAdminMap.get("caseId"), ApplicationPModel.class);
            JobDetailModel jobModel = (JobDetailModel) dao.getModelById((String)jobAdminMap.get("caseId"), JobDetailModel.class);

            if(jobModel !=null){
                (jobAdminMap).put("case_desc", jobModel.getApplicationModel().getPj_name());
                String aplsub = jobModel.getCase_ref();
                String usjno = !Validator.isEmpty(jobModel.getUsj_no()) ? jobModel.getUsj_no() : "N/A";
                (jobAdminMap).put("case_ref", aplsub);
                (jobAdminMap).put("usj_no", usjno);
                (jobAdminMap).put("job_id", jobModel.getJob_id());
                (jobAdminMap).put("case_id", jobModel.getCase_id());

            }else{
                (jobAdminMap).put("case_desc", appModel.getPj_name());
                (jobAdminMap).put("case_id", appModel.getCase_id());
                String aplsub = appModel.getCase_ref();
                (jobAdminMap).put("case_ref", aplsub);
            }
            
            // Added by teoYL: To display full name of taskDoer
            String taskDoer_id = jobAdminMap.get("taskDoer").toString();
            Debug.printDebug("taskDoer_id " + taskDoer_id);
            User taskDoer_user = new User();
            taskDoer_user = (User) dao.getModelByCode("us_user_id",taskDoer_id, taskDoer_user);
            Debug.printDebug("taskDoer_user " + taskDoer_user);
            String taskDoer_name = "";
            
            if(taskDoer_user != null) {
                taskDoer_name = taskDoer_user.getUs_user_name();                
            }
            
            jobAdminMap.put("taskDoer_name", taskDoer_name);
            // End
            
//            jobAdminMap.put("task_description", jobAdminMap.get("task_description") + " ("+pubCode.getCode_desc()+")");
//            jobAdminMap.put("wf_name", jobAdminMap.get("wf_name")+ "("+ appModel.getSetupCodeModel().getCode_1().substring(4) +")");
            routeUtil.prepareDateValue(jobAdminMap, "created_date");
            UserGroupDAOImpl userGroupDAO = new UserGroupDAOImpl();
            Map userGroupMap = userGroupDAO.getGroupInfo((String)jobAdminMap.get("roleOrGroup"), null, baseDAO.getSession());

            assigneeList = new ArrayList();
            Map defaultMap = new HashMap();
            defaultMap.put("email", "");
            defaultMap.put("userName", "--- Please Select ---");
            defaultMap.put("userId", "");
            assigneeList.add(defaultMap);
            assigneeList.addAll((List) userGroupMap.get("user"));
//            assigneeList = (List) userGroupMap.get("user");
            if ( ((String)jobAdminMap.get("task_status")).equals("20") ) {
                for (int idx = assigneeList.size()-1; idx >= 0; idx--) {
                    if ( ((String)assigneeList.get(idx).get("userId")).equals(jobAdminMap.get("taskDoer"))) {
                        assigneeList.remove(idx);
                        break;
                    }
                }
            }
            workloadList = new ArrayList();
            for (Map user : assigneeList) {
                if(!Validator.isEmpty((String) user.get("userId"))) {
                    Map userWorkloadMap = new HashMap();
                    userWorkloadMap.put("us_user_id", user.get("userId"));
                    userWorkloadMap.put("us_user_name", user.get("userName"));
                    Map completedTaskCountMap = routeUtil.getProcessedTaskCount("USJ", "1", (String)user.get("userId"), "populateTaskDetail::=N", "byMonth::=Y");
//                    Debug.printDebug(" " + completedTaskCountMap);
                    String completedCount = completedTaskCountMap.get("1")+"";
                    Integer completedInt = 0;
                    if (!Validator.isEmpty(completedCount)) {
                        completedInt = Integer.parseInt(completedCount);
                    }
                    userWorkloadMap.put("completed", completedInt);
                    Map pendingTaskMap = routeUtil.getPendingTask("USJ", "doer", (String)user.get("userId"));
                    List<Map<String, Object>> pendingTaskList =  ((List)pendingTaskMap.get("result"));
                    if (pendingTaskList != null && !pendingTaskList.isEmpty()) {
                        routeUtil.prepareDateValue(((List)pendingTaskMap.get("result")));
                    }
                    Integer outstanding = 0, inProgress = 0;
                    for (Map inProgressTask : (List<Map>)pendingTaskMap.get("result")) {
                        if (inProgressTask.get("due_date")!=null && 
                            ((Timestamp) inProgressTask.get("due_date")).after((Timestamp) inProgressTask.get("today"))) {
                            outstanding++;
                        } else {
                            inProgress++;
                        }
                    }
                    userWorkloadMap.put("outstanding", outstanding);
                    userWorkloadMap.put("inProgress", inProgress);
                    workloadList.add(userWorkloadMap);
                }
            }
            String a = "";
            int i=0;
            for (Map workload : workloadList) {
                a = "{\"Officer\":\""+workload.get("us_user_name")+"\",\"Completed\":"+workload.get("completed")+",\"In Progress\":"+workload.get("inProgress")+",\"Outstanding\":"+workload.get("outstanding")+"}";
                if(i>0){
                    jobStatistic += ","+a;
                }else{
                    jobStatistic = a;
                }
                i++;
//                Debug.printDebug("workloadlist = " + workload);
            }
//            Debug.printDebug("jobAdminMap = " + jobAdminMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

//	public String processUpdate(){
    public String processUpdate() throws Exception {
//		validateRequired();
//		specificValidation("update");
//		if (getActionErrors().size() > 0 || getActionMessages().size() > 0){
//			return SystemConstants.ACTION_Status.UPDATE_FAIL;
//		}

		//retrieve the modal out for updating back to database.
		/* sample as below...
         Module dbModule = moduleDAO.listById(module.getModule_id(), module);
         Debug.printDebug("module.getModule_code()" + module.getModule_code());
         Debug.printDebug("dbModule.getModule_code()" + dbModule.getModule_code());
		
         dbModule.setModule_code(module.getModule_code());
         dbModule.setModule_name(module.getModule_name());
         dbModule.setModule_type(module.getModule_type());
         dbModule.setParent_module_id(module.getParent_module_id());
         super.processUpdate(dbModule);*/
        //super.processUpdate(model);
//        if (pType.equals("NEW")) {
//            return doNew();
//        } else if (pType.equals("REASSIGN")) {
////            doEmailTrigger(pPlId);
//            return doReassignProg();
//        }

        return SUCCESS;
    }

    public String delete() {
        for (String deleteId : getSelected()) {
            // do deletion checking here.
            super.delete(deleteId, getModel());
        }
        return SUCCESS;
    }


    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getpPlId() {
        return pPlId;
    }

    public void setpPlId(String pPlId) {
        this.pPlId = pPlId;
    }

    public String getpJpId() {
        return pJpId;
    }

    public void setpJpId(String pJpId) {
        this.pJpId = pJpId;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public Integer getIntError() {
        return intError;
    }

    public void setIntError(Integer intError) {
        this.intError = intError;
    }

    public List getListEntityUser() {
        return listEntityUser;
    }

    public void setListEntityUser(List listEntityUser) {
        this.listEntityUser = listEntityUser;
    }

    public Integer getIntActiveJobProgCount() {
        return intActiveJobProgCount;
    }

    public void setIntActiveJobProgCount(Integer intActiveJobProgCount) {
        this.intActiveJobProgCount = intActiveJobProgCount;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public List<Map> getWorkloadList() {
        return workloadList;
    }

    public void setWorkloadList(List<Map> workloadList) {
        this.workloadList = workloadList;
    }

    public String getJobStatistic() {
        return jobStatistic;
    }

    public void setJobStatistic(String jobStatistic) {
        this.jobStatistic = jobStatistic;
    }
       
    public void setInternalStatusBackend(String jobId, String caseId) {
        try {
//            Debug.printDebug("setInternalStatus ^@@@@@@@@@^");
//            Map paramMap = new HashMap();
//            paramMap.clear();
//            paramMap.put("case_id", jobId);
//            List<JobStatusModel> statusOverallList = new ArrayList();
//            statusOverallList = baseDAO.list_order(paramMap, JobStatusModel.class, true, "order by status_date desc" );
//            Integer totalSize = statusOverallList.size();
//            
//            paramMap.clear();
//            paramMap.put("case_id", caseId);
//            paramMap.put("status_code", UtimapsAction.WF_STATUS.PREPARE_SJI_PENDING);
//            List<JobStatusModel> statusList = new ArrayList();
//            statusList = baseDAO.list_order(paramMap, JobStatusModel.class, true, "order by status_date desc" );
//            Debug.printDebug("paramMap " + paramMap);
//  
//            if(statusList.size() > 0) {
//                for(JobStatusModel status: statusList){
//                    status.updatableColumns=new String[]{"case_id","cur_status"};
//                    status.setCur_status("Y");
//                    getDaoService_().updateWithSession(session, status);
//                }
//            }
//            
//            JobStatusModel statusModel = new JobStatusModel();
//            statusModel.setStatus_id(com.sains.framework.base.CommonFunction.getId(20));
//            statusModel.setCase_id(jobModel.getJob_id());
//            statusModel.setStatus_code(UtimapsAction.WF_STATUS.PREPARE_SJI_PENDING);
//            
//            statusModel.setStatus_date(DateUtil.getCurrentTimestamp());
//            statusModel.setCreated_date(DateUtil.getCurrentTimestamp());
//            statusModel.setUpdated_date(DateUtil.getCurrentTimestamp());
//            
//            statusModel.setStatus_by("BACKEND");
//            statusModel.setCreated_by("BACKEND");
//            statusModel.setUpdated_by("BACKEND");
//            
//            statusModel.setTask_assign_to("");
//            statusModel.setCur_status("N");
//            statusModel.setStatus_seq(totalSize+1);
//            
//            getDaoService_().insertWithSession(session, statusModel);
//            Debug.printDebug("setInternalStatus ^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + statusModel.getStatus_code());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}