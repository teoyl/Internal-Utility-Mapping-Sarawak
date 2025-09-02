package com.sains.framework.base;

import com.sains.framework.base.CommonList;
import com.sains.framework.lookup.DataRetriever;
import com.sains.framework.model.AuditReportModel;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.PageUtil;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;

public abstract class BaseReportActionSupport<T> extends BaseAction{
    private static final long serialVersionUID = -1477947567038101484L;
    protected String jasper = "";
    private String action = "";
    private List result = new ArrayList();
    private List columns = new ArrayList();
    private Map setupMap = new HashMap();
    private boolean searched;
    private String searchCondition= "";
    private String redirectMessage;
    private String redirectError;
    protected String found = "found";
    protected String notFound = "notFound";
    private String customisedMsg = "";
    private String callerSuccessPage = "";
    private String defaultSearchValue = "";
    private Map<String, String> searchFieldsMap = new HashMap();
    private String usePopupCalander = "N";
    private Map<String, String> rightsList = new HashMap();
    public static final class SUCCESS_STATUS {
        public static final String SUCCESS = "Y";
        public static final String FAIL = "N";
    }
    protected String reportParams = "";
    protected String auditCriteria = "";

    public abstract String loadSearchPage();
	public abstract String processSearch();
	
	public List getResult() {
		return result;
	}
	public void setResult(List result) {
		this.result = result;
	}
	public List getColumns() {
		return columns;
	}
	public void setColumns(List columns) {
		this.columns = columns;
	}
	
	public Map getSetupMap() {
		return setupMap;
	}
	public void setSetupMap(Map setupMap) {
		this.setupMap = setupMap;
	}
	public boolean isSearched() {
		return searched;
	}
	public void setSearched(boolean searched) {
		this.searched = searched;
	}

    public String getSearchCondition() {
		return searchCondition;
	}
	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getRedirectMessage() {
		return redirectMessage;
	}
	public void setRedirectMessage(String redirectMessage) {
		this.redirectMessage = redirectMessage;
	}

    public String getRedirectError() {
        return redirectError;
    }

    public void setRedirectError(String redirectError) {
        this.redirectError = redirectError;
    }
	
	public void keepObjectToSession(Object obj, String name){
		ActionContext.getContext().getSession().remove(name);
		ActionContext.getContext().getSession().put(name, obj);
	}
	
	public Object getObjectFromSession(String name){
		Object sessionObj = ActionContext.getContext().getSession().get(name);
		return  sessionObj;
	}
	
	public Object getAndRemoveObjectFromSession(String name){
		return ActionContext.getContext().getSession().remove(name);
	}
	
	public void keepObjectToSession(Object obj){
		//remove first before add.
		ActionContext.getContext().getSession().remove(obj.getClass().getSimpleName()+"_");
		ActionContext.getContext().getSession().put(obj.getClass().getSimpleName()+"_", obj);
	}
	
	public Object getObjectFromSession(Object obj){
		return ActionContext.getContext().getSession().get(obj.getClass().getSimpleName()+"_");
	}

	//will remove the Object from Session once get from session.
	public Object getAndRemoveObjectFromSession(Object obj){
		return ActionContext.getContext().getSession().remove(obj.getClass().getSimpleName()+"_");
	}

//	public void closeSession(){
//            baseDAO.closeSession();
//	}
	
    public String cancel(){
        return "cancel";
    }

    public String getCallerSuccessPage() {
        return callerSuccessPage;
    }

    public void setCallerSuccessPage(String callerSuccessPage) {
        this.callerSuccessPage = callerSuccessPage;
    }

    public String getCustomisedMsg() {
        return customisedMsg;
    }

    public void setCustomisedMsg(String customisedMsg) {
        this.customisedMsg = customisedMsg;
    }

    public String getDefaultSearchValue() {
        return defaultSearchValue;
    }

    public void setDefaultSearchValue(String defaultSearchValue) {
        this.defaultSearchValue = defaultSearchValue;
    }

    public Map<String, String> getSearchFieldsMap() {
        return searchFieldsMap;
    }

    public void setSearchFieldsMap(Map<String, String> searchFieldsMap) {
        this.searchFieldsMap = searchFieldsMap;
    }

    public boolean has_right(String theRight){
        if (rightsList.containsKey(theRight)){
            return rightsList.get(theRight).equalsIgnoreCase("Y");
        } else {
            Map sessionMap = ActionContext.getContext().getSession();
            if (new CommonFunction().validateRight(this.getClass().getSimpleName(), theRight, sessionMap, "", baseDAO.getSession())){
                rightsList.put(theRight, "Y");
                return true;
            }
            rightsList.put(theRight, "N");
            return false;
        }
    }
    
    public boolean has_right2(String actionClass, String theRight, String paramAction) throws Exception{
        if (rightsList.containsKey(actionClass+"|"+theRight+"|"+paramAction)){
            return rightsList.get(actionClass+"|"+theRight+"|"+paramAction).equalsIgnoreCase("Y");
        } else {
            Map sessionMap = ActionContext.getContext().getSession();

//            if (sessionMap.get("login_user_type").equals(SystemConstants.SF_SYSTEM_TYPE.PUBLIC)) {
////                System.out.println("has right public......");
//                if (new CommonFunction().validatePublicRight(this.getClass().getSimpleName(), theRight, sessionMap, getAction(), baseDAO.getSession())) {
//                    rightsList.put(theRight, "Y");
//                    return true;
//                }
//            } else {
                if (new CommonFunction().validateRight_sql(actionClass, theRight, sessionMap, paramAction, baseDAO.getSession())){
                    rightsList.put(actionClass+"|"+theRight+"|"+paramAction, "Y");
                    return true;
                }
//            }
            rightsList.put(actionClass+"|"+theRight+"|"+paramAction, "N");
            return false;
        }
    }

    public String checkNull(String strParam) {
        if (strParam == null)   strParam = "";
        return strParam;
    }


    public String getMethodValueFromObject(Object model, String method){
        try {
            Method m = model.getClass().getMethod(method);
            Object returnObj = m.invoke(model);
            if (returnObj != null){
                return returnObj.toString();
            }
        } catch (Exception e){

        }
        return null;
    }

    protected void addErrorsFromList(List<String> list){
        for (String error : list){
            addActionError(error);
        }
    }

    public String getJasper() {
        return jasper;
    }

    public void setJasper(String jasper) {
        this.jasper = jasper;
    }

    protected void auditReport(BaseDAO basedao, String reportType, java.sql.Timestamp startTime, String status, String jasperReport) {
//        BaseDAO<AuditReportModel> basedao = new BaseDAOImpl();
        try {
            AuditReportModel auditReport = new AuditReportModel();

            auditReport.setID(CommonFunction.getId(20));
            if (jasperReport.indexOf("/com/") > 0) {
                jasperReport = jasperReport.substring(jasperReport.indexOf("/com/")+1);
            }
            auditReport.setReportname(jasperReport);
            auditReport.setReporttype(reportType);
            auditReport.setQueryby(basedao.getLoginId());  // ThoTH @ 29-Oct-2015
//            auditReport.setQueryby((String) ActionContext.getContext().getSession().get("loginId"));
            auditReport.setStarttime(startTime);
            auditReport.setEndtime(DateUtil.getCurrentTimestamp());
            auditReport.setSuccess_status(status);
            auditReport.setCriteria(auditCriteria);
            basedao.beginBatchTransaction();
            basedao.getSession().save(auditReport);
            basedao.commitBatchTransaction();
        } catch (Exception e) {
            basedao.rollbackBatchTransaction();
            new LogFunction().logError(this.getClass(), "", e);
//        } finally {
//            basedao.closeSession();
        }
    }

    public String getUsePopupCalander() {
        return usePopupCalander;
    }

    public void setUsePopupCalander(String usePopupCalander) {
        this.usePopupCalander = usePopupCalander;
    }

    public String getReportParams() {
//        System.out.println("here here here, reportParams = " + reportParams);
        return reportParams;
    }

    public void addReportParams(String newParam) {
        if (Validator.isEmpty(reportParams)) {
            reportParams = newParam;
        } else {
            reportParams += "&"+newParam;
        }
        if (Validator.isEmpty(auditCriteria)) {
            auditCriteria = newParam;
        } else {
            auditCriteria += "&"+newParam;
        }
    }
}

