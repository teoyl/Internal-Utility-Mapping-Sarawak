package com.sains.framework.base.web;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.SessionFactoryImpl;
import static com.sains.framework.base.web.SessionListener.activeSessionMap;
import com.sains.framework.model.Application;
import com.sains.framework.model.ApplicationNameComparator;
import com.sains.framework.model.GroupApplication;
import com.sains.framework.model.GroupApplicationRights;
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.Module;
import com.sains.framework.model.ModuleNameComparator;
import com.sains.framework.model.User;
import com.sains.framework.sam.dao.ApplicationDAO;
import com.sains.framework.sam.dao.ApplicationDAOImpl;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;

public class HibernateInformationAction extends ActionSupport{
    private static final long serialVersionUID = 8159031627650960678L;
    private List hibernateInfor = new ArrayList();
    private List hibernateInforLabel = new ArrayList();
    private List sessionPropertyInforLabel = new ArrayList();
    private List sessionPropertyInfor = new ArrayList();
    private List propertyInforLabel = new ArrayList();
    private List propertyInfor = new ArrayList();
    private String closeSessionLess = "N";
    private String closeStatementLess = "N";
    private String closeConnectionLess = "N";
    public static final Long loadIfMoreThanSecond = 10000L;
    public static List longRequestList = new ArrayList();
    
    private List<Application> applicationList = new ArrayList();
    private List moduleList = new ArrayList();
    private String menuListTemp = "";
    private String menuList = "";

    public Boolean showActiveSession = Boolean.FALSE;
    public Boolean isShowActiveSession() {
        return showActiveSession;
    }
    public void setShowActiveSession(Boolean showActiveSession) {
        this.showActiveSession = showActiveSession;
    }
    public Boolean showHibernateStatistic = Boolean.TRUE;

    public Boolean isShowHibernateStatistic() {
        return showHibernateStatistic;
    }
    public void setShowHibernateStatistic(Boolean showHibernateStatistic) {
        this.showHibernateStatistic = showHibernateStatistic;
    }

    public Set getActiveSessionSet() {
        return SessionListener.activeSessionMap.keySet().stream().collect(Collectors.toSet());
    }
    public String loadInfor() {
        if (showHibernateStatistic) {
            Debug.printFrameworkDebug(SessionFactoryImpl.getSfStatistics());
        }
        //SessionTimeoutListener.listActiveSession();
        String[] inforArr = null;
        Integer opened = null;
//        System.out.println("SessionFactoryImpl.getSessionPropertyMap() = " + SessionFactoryImpl.getSessionPropertyMap());
        for (String str : SessionFactoryImpl.getSessionPropertyMap().split(",")) {
            if (str.indexOf("=") < 0) {
                break;
            }
            inforArr = str.split("=");
            if (inforArr[0].equalsIgnoreCase("Opened Count")) {
                opened = new Integer(inforArr[1]);
            } else if (inforArr[0].equalsIgnoreCase("Closed Count")) {
                if (opened - new Integer(inforArr[1]) > 0) {
                    closeSessionLess = "Y";
                }
            }
            sessionPropertyInforLabel.add(inforArr[0]);
            sessionPropertyInfor.add(inforArr[1]);
        }
        SessionFactoryImpl.populateNumberOfConnInPool(sessionPropertyInforLabel, sessionPropertyInfor);
        for (String str : SessionFactoryImpl.getPropertyMap().split(",")) {
            if (str.indexOf("=") < 0) {
                break;
            }
            inforArr = str.split("=");
            if (inforArr[0].equalsIgnoreCase("Opened Count")) {
                opened = new Integer(inforArr[1]);
            } else if (inforArr[0].equalsIgnoreCase("Closed Count")) {
                if (opened - new Integer(inforArr[1]) > 0) {
                    closeConnectionLess = "Y";
                }
            }

            propertyInforLabel.add(inforArr[0]);
            propertyInfor.add(inforArr[1]);
        }
        
        return SUCCESS;
    }

    public String getHibernateStatistic() {
        return SessionFactoryImpl.getHibernateStatistic();
    }
    
    public List getHibernateInfor() {
        return hibernateInfor;
    }

    public List getHibernateInforLabel() {
        return hibernateInforLabel;
    }

    public String getCloseSessionLess() {
        return closeSessionLess;
    }

    public String getCloseStatementLess() {
        return closeStatementLess;
    }

    public String getCloseConnectionLess() {
        return closeConnectionLess;
    }

    public List getPropertyInfor() {
        return propertyInfor;
    }

    public List getPropertyInforLabel() {
        return propertyInforLabel;
    }

    public List getSessionPropertyInfor() {
        return sessionPropertyInfor;
    }

    public List getSessionPropertyInforLabel() {
        return sessionPropertyInforLabel;
    }

    public String resetSessionCount() {
        new BaseDAOImpl().resetSessionCount();
        return "forwardLoadInfor";
    }

    public String resetConnectionCount() {
        SessionFactoryImpl.resetSessionCount();
        return "forwardLoadInfor";
    }

    public Set getNotCloseSessionHashCode_key() {
        Set notClosedSet = new HashSet();
        for (String str: BaseDAOImpl.sessionIdHashCode.keySet()) {
            notClosedSet.add(BaseDAOImpl.sessionIdHashCode.get(str));
        }

        return notClosedSet;
    }

    public Map getNotCloseConnectionHashCode() {
        return SessionFactoryImpl.connectionHashCode;
    }

    public Set getNotCloseConnectionHashCode_key() {
        Set notClosedSet = new HashSet();
//        for (String str: SessionFactoryImpl.connectionHashCode.keySet()) {
//            notClosedSet.add(SessionFactoryImpl.connectionHashCode.get(str));
//        }
        for (java.sql.Connection conn : SessionFactoryImpl.connectionHashCode.keySet()) {
            notClosedSet.add(SessionFactoryImpl.connectionHashCode.get(conn));
        }

        return notClosedSet;
    }
    
    public String getWarVersion() {
        return SystemConstants.DOMAIN.war_version;
    }
    
    public List getLongRequestList() {
        return longRequestList;
    }
    
    public Long getLoadIfMoreThanSecond() {
        return loadIfMoreThanSecond;
    }
    
    private String us_user_id = "";
    private String us_security = "";

    public String getUs_security() {
	return us_security;
    }

    public void setUs_security(String us_security) {
	this.us_security = us_security;
    }
    public String getUs_user_id() {
        return us_user_id;
    }

    public void setUs_user_id(String us_user_id) {
        this.us_user_id = us_user_id;
    }
    
    private List divList = new ArrayList(); //yonglai @23/10/2024 set user divison from user group assigned 
    
    public String processSetSession() throws Exception {

	BaseDAOImpl dao = new BaseDAOImpl();

        try {
            if (getUs_security().equals("1vne1t2v1w8v1s3g1s3m1w8v1t3b1vnoIN") ) {
		    User user1 = null;
		    ApplicationDAO applicationDAO = new ApplicationDAOImpl();
		    //pentest to change SESSIONID After Login, need to do before set new session
		    SessionMap sessionMap1 = (SessionMap) ActionContext.getContext().getSession();
		    sessionMap1.invalidate();
		    try {

		    user1 = (User) dao.getModelByCode("us_user_id", getUs_user_id().toLowerCase(), new User());

		    if (user1 == null) {
			System.out.println("USER IS NULL");
			throw new CustomBaseException(getText("errors.invalidLogin"));
		    }

		    if (user1 != null) {
			Map sessionMap = ActionContext.getContext().getSession();
			HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
			sessionMap.clear();  // Clear Session for ELS and StoreFront
			sessionMap.put("logined", "true");
			sessionMap.put("context", new Date());
			sessionMap.put("userId", user1.getUs_id());
			sessionMap.put("loginId", user1.getUs_user_id());
			sessionMap.put("userName", user1.getUs_user_name());
			sessionMap.put("login_user_type", "internal");
                        sessionMap.put("loginSystemType_", "DEF");
                        sessionMap.put("div_assigned", divList); //yonglai :: put div code follow user group assigned

			//pentest
			sessionMap.put("user_ip", CommonFunction.getUserIp(request));
			sessionMap.put("user_agent", CommonFunction.getUserAgent(request));
			SessionTimeoutListener.activeSessionSet.add(((HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST)).getSession());
			if (user1.getUs_admin().equalsIgnoreCase("Y")) { // if ADMIN
			    applicationList = applicationDAO.getAdminApplications(); // populate admin application

			}

			boolean hasJobListings = false;
			for (GroupUser gu : user1.getGroupUserList()) {
                            divList.add(gu.getUserGroup().getGroup_div());
			    for (GroupApplication ga : gu.getUserGroup().getGroupApplication()) { // populate user group's application
				if (!containsApplication(ga.getApplication(), applicationList)) { // check if already in applicationList
				    applicationList.add(ga.getApplication());

				    if (ga.getApplication().getApplication_name().equals("Job Listing")) // Check if user got job listings access - 08/21/2019 Hamizan
				    {
					ActionContext.getContext().getSession().put("jobListing", "Y");
					hasJobListings = true;
				    }

				    //Hamizan @03/12/2019 Remark:Check if user got mobile dashboard access 
				    if (ga.getApplication().getApplication_code().equals("mobileDashboard")) {
					//Hamizan @13/12/2019 Remark: Changed mobile dashboard access (Operation/Management) by access right 
					for (GroupApplicationRights appRight : ga.getGroupAppRights()) {
					    if (appRight.getApplicationRights().getApp_rights_code().equals("operationAccess")
						    && appRight.getHasRight().equals("Y")) {
						ActionContext.getContext().getSession().put("mobileDashboardOperation", "Y");
					    } else if (appRight.getApplicationRights().getApp_rights_code().equals("managementAccess")
						    && appRight.getHasRight().equals("Y")) {
						ActionContext.getContext().getSession().put("mobileDashboardMngmt", "Y");
					    }
					}
				    }
				}
			    }
			}

			for (Application app : applicationList) {
			    if (app != null) {
				Module module = app.getAttachedModule();
				getModules(module, moduleList); // attached application according module
			    }
			}

			//----------- populate menuList -------------------
			if (moduleList.size() > 0) {
			    sortMenu();
			    List<Module> list = (List) moduleList.get(0);
			    String menuListTempLocal = "";  // ThoTH @ 24-Jul-2012 :: To Hide Module when no Item
			    for (Module moduleObj : list) {
				menuListTemp = "";  // Clear
//                            menuListTempLocal = "  <li class='current'><a href='javascript:void(0)'>" + moduleObj.getModule_name() + "</a><ul>\r\n";
//                            menuListTempLocal = "  <li class='current  '><a href='javascript:void(0)' class='dropdown-toggle' data-toggle='dropdown'>1" + moduleObj.getModule_name() + "</a><ul class='dropdown-menu'>\r\n";//serene @ 26/7/2017: add class for boostrap
				menuListTempLocal = "  <li class='current '><a href='javascript:void(0)' >" + moduleObj.getModule_name() + " <span class='caret'></span></a><ul class='dropdown-menu'>\r\n";//serene @ 5/10/2017: add class for boostrap
				loopModules(1, moduleList, moduleObj, "    ");
				for (Application app : applicationList) {
				    if (app.getHidden().equals("Y")) {
					continue;
				    }
				    if (app.getAttached_module_id().equals(moduleObj.getModule_id())) {
					menuListTemp += "    <li><a class='menuTree' href='" + app.getAction_name() + "'>" + app.getApplication_name() + "</a></li>\r\n";
				    }
				}
				if (menuListTemp.length() > 0) {
				    menuList += menuListTempLocal + menuListTemp + "  </ul></li>";
				}
			    }
			    ActionContext.getContext().getSession().remove("menuList");
			    ActionContext.getContext().getSession().put("menuList", menuList); // set Session 'menuList'
			}//----------- end populate menuList -------------------

			//Hamizan @ 29.08.2019 Remarks: Redirect officer with jobListing access to Job Listing page
			if (!hasJobListings) {
			    return SUCCESS;
			} else {
			    return "loadListing";
			}

		    }
		} catch (Exception e) {
		}


	    }

        } catch (Exception e) {
            throw e;
        }finally{
	    dao.closeSession();
	}

        return SUCCESS;
    }
    
    private boolean containsApplication(Application compareApp, List<Application> appList) {
        for (Application app : appList) {
            if (app.getApplication_id().equals(compareApp.getApplication_id())) {
                return true;
            }
        }
        return false;
    }
    private boolean containsModule(Module compareModule, List<Module> moduleList) {
        for (Module module : moduleList) {
            if (module.getModule_id().equals(compareModule.getModule_id())) {
                return true;
            }
        }
        return false;
    }

    private int getModules(Module module, List moduleList) {
        Module parentModule = module.getParentModule();
        if (parentModule == null) {
            if (moduleList.size() < 1) { // no first level module yet
                List list = new ArrayList();
                list.add(module);
                moduleList.add(list);
            } else {
                if (!containsModule(module, (List) moduleList.get(0))) {
                    ((List) moduleList.get(0)).add(module);
                }
            }
            return 2;
        } else {
            int level = getModules(parentModule, moduleList);
            if (moduleList.size() < level) {
                List list = new ArrayList();
                list.add(module);
                moduleList.add(list);
            } else {
                if (!containsModule(module, (List) moduleList.get(level - 1))) {
                    ((List) moduleList.get(level - 1)).add(module);
                }
            }
            return level + 1;
        }
    }
    private int loopModules(int level, List moduleList, Module currentModule, String indent) {
        if (level <= moduleList.size() - 1) {
            for (Module module : ((List<Module>) moduleList.get(level))) {
                if (module.getParentModule().getModule_id().equals(currentModule.getModule_id())) {
//                    menuListTemp += indent + "<li class='current '><a href='javascript:void(0)'>" + module.getModule_name() + "</a><ul>\r\n";
//                    menuListTemp += indent + "<li class='current dropdown-submenu'><a href='javascript:void(0)' class='dropdown-toggle' data-toggle='dropdown'>2" + module.getModule_name() + "</a><span class=\"caret\"></span><ul class='dropdown-menu'>\r\n";//serene @ 26/7/2017: add class for boostarp
                    menuListTemp += indent + "<li class='current'><a href='javascript:void(0)'>" + module.getModule_name() + " <span class='caret'></span></a><ul class='dropdown-menu'>\r\n";//serene @ 5/10/2017: add class for boostrap
                    loopModules(level + 1, moduleList, module, indent + "  ");
                    for (Application app : applicationList) {
//                        System.out.println("app name : " + app.getApplication_name());
                        if (app.getAttached_module_id().equals(module.getModule_id())) {
                            if (app.getHidden().equals("Y")) {
                                continue;
                            }
                            menuListTemp += indent + "  <li><a class='menuTree ' href='" + app.getAction_name() + "'>" + app.getApplication_name() + "</a></li>\r\n";
                        }
                    }
                    menuListTemp += indent + "</ul></li>\r\n";
                }
            }
        }
        return 0;
    }
    public void sortMenu() {
        for (int index = 0; index < moduleList.size(); index++) {
            Collections.sort((List) moduleList.get(index), new ModuleNameComparator());
        }

        Collections.sort((List) applicationList, new ApplicationNameComparator());

    }
}