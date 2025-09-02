package com.sains.framework.base.web;

import com.google.gson.Gson;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.sains.common.util.CommonComparator;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseAction;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.LogFunction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.AesUtil;
import com.sains.common.util.ApiUtil;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Fim2Api;
import com.sains.common.util.Formatter;
import com.sains.common.util.FtpInterface;
import com.sains.common.util.SFTPBean;
import com.sains.common.util.SwkIdApi;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.EmailTrigger;
import com.sains.framework.base.ModelBase;
import static com.sains.framework.base.web.AuthorizationInterceptor.maintenanceEnd;
import static com.sains.framework.base.web.AuthorizationInterceptor.maintenanceStart;
import com.sains.framework.model.Module;
import com.sains.framework.model.ModuleNameComparator;
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.GroupApplication;
import com.sains.framework.sam.dao.UserDAOImpl;
import com.sains.framework.model.User;
import com.sains.framework.model.Application;
import com.sains.framework.model.ApplicationNameComparator;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.sam.dao.ApplicationDAOImpl;
import com.sains.framework.sam.dao.ModuleDAOImpl;
import com.sains.framework.model.ParameterModel;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.model.SetupSystemModel;
import com.sains.framework.model.UserPreferenceModel;
import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import com.sains.framework.sam.dao.UserGroupDAOImpl;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Calendar;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.I18nInterceptor;
import org.hibernate.Query;

public class LoginAction extends BaseAction {

    private final String action = "null";
    private String userId = "";
    private String userIcNo = "";
    private String userEmail = "";
    private String passwd = "";
    //private List menuList = new ArrayList();
    private List<Application> applicationList = new ArrayList();
    private List moduleList = new ArrayList();
    private String menuList = "";
    private String menuTitle = "";  // Zhafari @ 16-Apr-2013 - additional atts
    // ThoTH @ 20-May-2013  :: For ESS

    //Added by Delvene @ 10-Dec-2013 :: To support breadcrumb
    private String moduleTile = "";
    private String moduleCode = "";
    private String appTile = "";
    //Added by Delvene @ 10-Dec-2013 :: To support breadcrumb - END

    private String itemId_ = "";
//    private String endSize_ = "1";  // Default to 1
//    private String endSizeESS_ = "1";  // Default to 1
    private String archive_ = "N";   // Flag whether announcement content is directed from Archive or Login Page :: Default to N (No)
    private String myProfileList = "";  //Added by ChangMH @ 07-Aug-2014 :: To support mega menu

//    private String version_ = SystemConstants.DOMAIN.version_SCSHRA;
    private String strCaptcha_ = ""; //Added by ChangMH @ 30-Sep-2014 :: Activation Account(PBT/BBN)
    private String confirm_passwd_ = ""; //Added by ChangMH @ 01-Oct-2014 :: Change password
    private String strTitle_ = ""; //Added by ChangMH @ 08-Oct-2014 :: Screen Title

    private String finalLookupDesc = "";
    public String getFinalLookupDesc() {
        return finalLookupDesc;
    }
    public void setFinalLookupDesc(String finalLookupDesc) {
        this.finalLookupDesc = finalLookupDesc;
    }
    
    private static final Boolean usePubPrivateKey = Boolean.FALSE;
    public static Boolean getUsePubPrivateKey() {
        return usePubPrivateKey;
    }
    
    private List divList = new ArrayList(); //yonglai @23/10/2024 set user divison from user group assigned 
    
//    protected String systemType_ = SystemConstants.SYSTEM_TYPE.DEFAULT;
    // ThoTH @ 20-May-2013 - END
    //private Module module = new Module();
    //private ModuleDAO moduleDAO = new ModuleDAOImpl();
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
        Module parentModule = null;
        try {
            parentModule = module.getParentModule();
        } catch (Exception e) {
        }
        if (parentModule == null) {
            if (moduleList.size() < 1) { // no first level module yet
                List list = new ArrayList();
                list.add(module);
                moduleList.add(list);
            } else {
                if (module != null) {
                    if (!Validator.isEmpty(module.getModule_id())) {
                        if (!containsModule(module, (List) moduleList.get(0))) {
                            ((List) moduleList.get(0)).add(module);
                        }
                    }
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

    private String getIconString(String iconClass) {
        if (Validator.isEmpty(iconClass)) {
//            iconClass = "";
            return "&nbsp;&nbsp;&nbsp;";
        }
        if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_3)) {
            return "<span class='nav-menu-icon kt-menu__link-text'><i class='"+StringEscapeUtils.escapeHtml4(iconClass)+"'></i></span>";
        } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_4)) {
            return "<span class='accordion-heading nav-menu-icon'><i class='menu-icon "+StringEscapeUtils.escapeHtml4(iconClass)+"'></i></span>";
        } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_5)) {
            return "<span class='nav-link-icon'><i class='" + StringEscapeUtils.escapeHtml4(iconClass) + "'></i></span>";
        }
        return "";
    }
    private String sanitiseName(String nameCode, String name) {
        if (!Validator.isEmpty(nameCode)) {
            return StringEscapeUtils.escapeHtml4(getText(nameCode));
        }
        return StringEscapeUtils.escapeHtml4(name);
    }
    private String loopModules2(int level, List moduleList, Module currentModule, String indent, List<Application> appList, String systemType) {
        String localMenu = "";
        int intWrite = 0;  // ThoTH @ 21-Jan-2014
        Boolean added = Boolean.FALSE;
        String strTemp = "";  // ThoTH @ 21-Jan-2014 :: To hide the Module with no Application
        if (level == 1 && currentModule.getAttachedApplication().size() == 1) {
            for (Application app : appList) {
                if (app.getAttached_module_id().equals(currentModule.getModule_id())) {
                    if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_3)) {
                        localMenu += "<li><a href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "'>"+getIconString(app.getApp_icon()) + sanitiseName(app.getApplication_name_code(), app.getApplication_name()) + "</a></li>";
                    } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_4)) {
                        localMenu += strTemp += indent + "  <li class='kt-menu__item ' aria-haspopup='true'><a href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "' class='kt-menu__link '>"+getIconString(app.getApp_icon()) +"<span class='kt-menu__link-text'>" + sanitiseName(app.getApplication_name_code(), app.getApplication_name()) + "</span></a></li>\r\n";
                    } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_5)) {
                        localMenu += "<li class='nav-item'><a class='nav-link' href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "' role='button'><div class='d-flex align-items-center'>"+getIconString(app.getApp_icon()) + "<span class='nav-link-text ps-1'>" + sanitiseName(app.getApplication_name_code(), app.getApplication_name()) + "<span></div></a></li>";
                    }
                }

            }
//            return -100;
            return "only1app"+localMenu;
        }
        if (level <= moduleList.size() - 1) {
            Boolean blnCurrentModuleOpened = Boolean.FALSE;  // ThoTH @ 8-Sep-2014 :: Only Open CurrentModule Once
            Boolean blnOpenTagAdded = Boolean.FALSE;
            for (Module module : ((List<Module>) moduleList.get(level))) {
                blnOpenTagAdded = Boolean.FALSE;
                strTemp = "";// sereneC @ 23/3/2015 :: avoid the application add to different module
                if (module.getParentModule().getModule_id().equals(currentModule.getModule_id())) {
                    String rtnMenu = loopModules2(level + 1, moduleList, module, indent + "  ", appList, systemType);
                    if (rtnMenu.length() > 0) {
                        blnOpenTagAdded = Boolean.TRUE;
                        intWrite++;
                    }
                    
                    for (Application app : appList) {
                        if (app.getAttached_module_id().equals(module.getModule_id())) {
                            if (app.getHidden().equals("Y")) {
                                continue;
                            }
                            intWrite++;
                            if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_3)) {
                                strTemp += indent + "  <li><a href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "'>"+ getIconString(app.getApp_icon()) + sanitiseName(app.getApplication_name_code(), app.getApplication_name()) + "</a></li>";
                            } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_4)) {
                                strTemp += indent + "  <li class='kt-menu__item ' aria-haspopup='true'><a href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "' class='kt-menu__link '>"+getIconString(app.getApp_icon()) +"<span class='kt-menu__link-text'>" + sanitiseName(app.getApplication_name_code(), app.getApplication_name()) + "</span></a></li>";
                            } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_5)) {
                                strTemp += indent + "  <li class='nav-item'><a class='nav-link' href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "' role='button'><div class='d-flex align-items-center'>" + getIconString(app.getApp_icon()) + "<span class='nav-link-text ps-1'>" + sanitiseName(app.getApplication_name_code(), app.getApplication_name()) + "<span></div></a></li>";
                            }
                        }
                    }

                    if (intWrite > 0) {  // ThoTH @ 21-Jan-2014
                        if (systemType.equals(SystemConstants.SYSTEM_TYPE.DEFAULT)) {
                            if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_3)) {
                                localMenu += "<li>" +
                                          "   <a class='accordion-heading' data-toggle='collapse' data-target='#"+module.getID()+"'>" +
                                          "      <span class='nav-header-primary'>"+getIconString(module.getModule_icon())+sanitiseName(module.getModule_name_code(), module.getModule_name())+"<span class='pull-right'><b class='caret'></b></span></span>" +
                                          "   </a>" +
                                          "      <ul class='nav-list collapse' id='"+module.getID()+"'>";
                            } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_4)) {
                                localMenu += "<li class='menu-text kt-menu__item  kt-menu__item--submenu' aria-haspopup='true' data-ktmenu-submenu-toggle='hover'>\n" +
                                        "	<a class='accordion-heading kt-menu__link' data-toggle='collapse' data-target='#_"+module.getID()+"'>\n" +
                                        "       "+getIconString(module.getModule_icon())+sanitiseName(module.getModule_name_code(), module.getModule_name())+"<span class='pull-right'><b class='caret'></b></span>\n" +
                                        "	</a>\n" +
                                        "	<ul class='collapse' id='_"+module.getID()+"'>";
                            } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_5)) {
                                localMenu += "<li class='nav-item'>" +
                                          "   <a class='nav-link dropdown-indicator' href='#"+module.getID()+"' role='button' data-bs-toggle='collapse' aria-expanded='false' aria-controls='" + module.getID() + "'>" +
                                          "      <div class=\"d-flex align-items-center\">"+getIconString(module.getModule_icon())+ "<span class=\"nav-link-text ps-1\">" + sanitiseName(module.getModule_name_code(), module.getModule_name())+"</span></div>" +
                                          "   </a>" +
                                          "      <ul class='nav collapse' id='"+module.getID()+"'>";
                            }
                            localMenu += rtnMenu + strTemp;
                            localMenu += indent + "</ul></li>";//sereneC @ 27/7/2014 ::add </div> to close <div class='sf-mega'>
                        }
                    }
                }
            }
        }
        return localMenu;
    }
    
    // ThoTH @ 29-Aug-2014 :: Change systemType_ from using Param, since now User can switch between ESS And HRA
    private int loopModules(int level, List moduleList, Module currentModule, String indent, List<Application> appList, String systemType) {
        Debug.printFrameworkDebug("loopModules");
        int intWrite = 0;  // ThoTH @ 21-Jan-2014
        Boolean added = Boolean.FALSE;
        String strTemp = "";  // ThoTH @ 21-Jan-2014 :: To hide the Module with no Application
        if (level == 1 && currentModule.getAttachedApplication().size() == 1) {
            for (Application app : appList) {
                if (app.getAttached_module_id().equals(currentModule.getModule_id())) {
                    menuList += 
                "<li><a href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "'>" + StringEscapeUtils.escapeHtml4(app.getApplication_name()) + "</a></li>";
                }

            }
            return -100;
        }
        if (level <= moduleList.size() - 1) {
            Boolean blnCurrentModuleOpened = Boolean.FALSE;  // ThoTH @ 8-Sep-2014 :: Only Open CurrentModule Once
            Boolean blnOpenTagAdded = Boolean.FALSE;
            for (Module module : ((List<Module>) moduleList.get(level))) {
                blnOpenTagAdded = Boolean.FALSE;
                strTemp = "";// sereneC @ 23/3/2015 :: avoid the application add to different module
                if (module.getParentModule().getModule_id().equals(currentModule.getModule_id())) {
                    if (systemType.equals(SystemConstants.SYSTEM_TYPE.DEFAULT)) {  // ThoTH @ 16-Apr-2014

                    } else {
                    }
                    if (loopModules(level + 1, moduleList, module, indent + "  ", appList, systemType) > 0) {
                        blnOpenTagAdded = Boolean.TRUE;
                        intWrite++;
                    }

                    for (Application app : appList) {
                        if (app.getAttached_module_id().equals(module.getModule_id())) {
                            if (app.getHidden().equals("Y")) {
                                continue;
                            }
                            intWrite++;
                            strTemp += indent + "  <li><a href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "'>" + StringEscapeUtils.escapeHtml4(app.getApplication_name()) + "</a></li>";
//                            menuList += indent + "  <li><a class='menuTree' href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "'>" + StringEscapeUtils.escapeHtml4(app.getApplication_name()) + "</a></li>";
                        }

                    }

                    if (intWrite > 0) {  // ThoTH @ 21-Jan-2014
                        if (systemType.equals(SystemConstants.SYSTEM_TYPE.DEFAULT)) {
                            if (!blnCurrentModuleOpened) {
                                blnCurrentModuleOpened = Boolean.TRUE;
                                menuList += "<li>" +
                                            "   <a class='accordion-heading' data-toggle='collapse' data-target='#"+currentModule.getID()+"'>" +
                                            "      <span class='nav-header-primary'>"+currentModule.getModule_name()+"<span class='pull-right'><b class='caret'></b></span></span>" +
                                            "   </a>" +
                                            "      <ul class='nav-list collapse' id='"+currentModule.getID()+"'>";
                            }

                            if (!blnOpenTagAdded) {
                                  menuList += "<li>" +
                                            "   <a class='accordion-heading' data-toggle='collapse' data-target='#"+module.getID()+"'>" +
                                            "      <span class='nav-header-primary'>"+module.getModule_name()+"<span class='pull-right'><b class='caret'></b></span></span>" +
                                            "   </a>" +
                                            "      <ul class='nav-list collapse' id='"+module.getID()+"'>";
                            }
                            menuList += strTemp;
                            menuList += indent + "</ul></li>";//sereneC @ 27/7/2014 ::add </div> to close <div class='sf-mega'>
                        }
//                        Debug.printFrameworkDebug("menuList_ESS :"+menuList_ESS);
                    }
                }
            }
        }
        return intWrite;
    }

    private String refererFrom = "";
    public LoginAction() {
        try {
            Map sessionMap = ActionContext.getContext().getSession();
            if (sessionMap.containsKey("lookupRefererUrl_")) {
                refererUrl_ = (String)sessionMap.get("lookupRefererUrl_");
                finalLookupDesc = (String)sessionMap.get("finalLookupDesc");
                refererFrom = "Lookup";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // set the required field for common check.
        //getRequiredParam().put("user_id", "user.id");
        //getRequiredParam().put("password", "user.password");
    }

    public void specificValidation(String validationType) {
    }

    public String add() {
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    //retrieve data for editing.
    public String loadEditPage() {

        // retrieve the data out for updating.
		/* sample as below...
         module = super.processEdit(getModel());
         if (module.getParentModule() != null){
         setParent_code(module.getParentModule().getModule_code());
         setParent_desc(module.getParentModule().getModule_name());
         }*/
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }


    public String loadActivationPage() {
        return "load_activation_page_PBT_BBN";
    }

    public String showNeedHelp() {
        return "load_NeedHelp";
    }

    public String processUpdate() {
        return SUCCESS;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
//Added by SereneC @ 06-Aug-2014 ::  To support current date in login page

    public String getLocalDate_str() {
        return Formatter.formatDate(DateUtil.getCurrentTimestamp(), "EEEE, dd MMMM yyyy h:m:s a");
    }

    private String defaultReloginDiv = "lookupModal";
    public String getDefaultReloginDiv() {
        Map sessionMap = ActionContext.getContext().getSession();
        if (sessionMap.containsKey("notFromLookup")) {
            defaultReloginDiv = "moreInfoDiv";
        }
        return defaultReloginDiv;
    }
    
    public String init() {
        Map sessionMap = ActionContext.getContext().getSession();
        
        if(sessionMap.get("userDivisionId") != null) {
            userDivisionId = sessionMap.get("userDivisionId").toString();
        }
        if (sessionMap.containsKey("notFromLookup")) {
            defaultReloginDiv = "moreInfoDiv";
        }
        if (sessionMap.containsKey("map_sso_uid") && !isFromProcessMapSSO) { //in case ppl bypass mapping/direct access login
            addActionMessage(getText("errors.needToMapSwkId"));
            return "map_sso_uid";
        }
        if (sessionMap.get("expired") != null) {
            addActionError(getText("errors.sesionExpired"));
            sessionMap.remove("expired");
        } else if (sessionMap.get("logined") != null) {
            if (((String) sessionMap.get("logined")).equals("true")) {
                prepareRightPanel();
                if (systemType_.equals(sessionMap.get("loginSystemType_"))) {
                    setPageTitle_("Home");
                    if (!Validator.isEmpty(refererUrl_)) {
                        return "refererUrl";
                    } else {
                        return SUCCESS;
                    }
                } else {
                    if (sessionMap.get("loginSystemType_").equals(SystemConstants.SYSTEM_TYPE.DEFAULT)) {
                        return "redirectLoginInternal";
                    }

                    return "redirectLoginSPA";
                }
            }
        }

        // ThoTH @ 15-Feb-2015
        if (refererUrl_ != null && refererUrl_.equals("mrpe")) {
            refererUrl_ = "";
        }

        if (!Validator.isEmpty(refererUrl_)) {
            addActionError(getText("qp.referer.error.login"));
        }
        return "loadLoginPage";
    }

    public String cancel() {
        return init();
    }

    public List getRecentView() {
        Map sessionMap = ActionContext.getContext().getSession();
        //return (List)sessionMap.get("recentViewed");
        List<String> list = new ArrayList();
        list.add("view 2");
        list.add("view 1");
        list.add("view 3");
        return list;
    }

    public String processlogout() {
        Debug.printFrameworkDebug("....................................................................................................");
        Debug.printFrameworkDebug(".................................................. LoginAction - processlogout ..................................................");
        Debug.printFrameworkDebug("....................................................................................................");
        
        auditLogin(SystemConstants.AUDIT_LOGIN.ACTION.LOGOUT, systemType_, SystemConstants.AUDIT_LOGIN.STATUS.SUCCESS, getUserId());
        SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();
        SessionListener.activeSessionMap.remove(sessionMap.get("loginId"));

        //[START] activity log summary - ahmadni @ 29-Sept-2016
        User model = new User();
//        BaseDAO<ActivityLogModel> activityLogDAO = new BaseDAOImpl<ActivityLogModel>();
//        activityLogDAO.setSession(baseDAO.getSession());
        if (sessionMap.get("logined") != null && !sessionMap.get("logined").equals("")) {
            //activityLogDAO.setSession(baseDAO.getSession());
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.DATE.activityLogDateTimeFormat);
//                Debug.printFrameworkDebug("session loginid"+sessionMap.get("loginId").toString());
                if (systemType_.equals(SystemConstants.SYSTEM_TYPE.PUBLIC)) {
                    model = (User) baseDAO.getModelByCode("us_id_number", sessionMap.get("loginId").toString(), new User());
                } else {
                    model = (User) baseDAO.getModelByCode("us_user_id", sessionMap.get("loginId").toString(), new User());
                }
//                model = (User) baseDAO.getModelByCode("us_user_id",sessionMap.get("loginId").toString(), new User());
                timeLoggedIn = Formatter.formatDate(model.getUs_last_login_date(), SystemConstants.DATE.activityLogDateTimeFormat);
                timeLoggedOut = Formatter.formatDate(DateUtil.getCurrentTimestamp(), SystemConstants.DATE.activityLogDateTimeFormat);
                long startMillis = 0;
                long endMillis = 0;
                if (!Validator.isEmpty(timeLoggedIn)) {
                    startMillis = sdf.parse(timeLoggedIn).getTime();
                }
                if (!Validator.isEmpty(timeLoggedOut)) {
                    endMillis = sdf.parse(timeLoggedOut).getTime();
                }
                if (!Validator.isEmpty(timeLoggedIn) && !Validator.isEmpty(timeLoggedOut)) {
                    logonDuration = DurationFormatUtils.formatPeriod(startMillis, endMillis, "HH:mm:ss");
                }
//                Map param = new HashMap();
//                param.put("us_id", sessionMap.get("userId").toString());
//                param.put(SystemConstants.CRITERIA.DATE_CRITERIA + "created_date",
//                        ">= to_timestamp('" + Formatter.formatTimestamp(new java.sql.Timestamp(startMillis), "YYYY-MM-dd HH:mm:ss") + "'_comma_ 'yyyy-mm-dd hh24:mi:ss'), "
//                        + "< to_timestamp('" + Formatter.formatTimestamp(new java.sql.Timestamp(endMillis), "YYYY-MM-dd HH:mm:ss") + "'_comma_ 'yyyy-mm-dd hh24:mi:ss')");

//                Set<String> setLogDesc = new HashSet<String>();
//                for (ActivityLogModel activityLog : activityLogDAO.list_order(param, ActivityLogModel.class, true, "order by system_id,created_date")) {
//                    if (setLogDesc.add(activityLog.getCreated_date() + activityLog.getLog_desc())) {
//                        activityLogList.add(activityLog);
//                    }
//                }
            } catch (Exception e) {
                new LogFunction().logError(this.getClass(), "", e);
            }
        }
        //[END] activity log summary - ahmadni @ 29-Sept-2016
        Object multilingualDD = sessionMap.get("multilingualDD");
        String defaultLanguage = (String)sessionMap.get("defaultLanguage");
        Object multilingualSupport = sessionMap.get("multilingualSupport");
        clearSessionMap(sessionMap);
        //invalidate
        sessionMap.invalidate();
        sessionMap.put("multilingualDD", multilingualDD);
        sessionMap.put("defaultLanguage", defaultLanguage);
        sessionMap.put("multilingualSupport", multilingualSupport);
        
        BaseDAO dao = baseDAO;
        dao.setSession(baseDAO.getSession());
        SetupSystemModel ss= (SetupSystemModel) dao.getModelByCode("system_code",SystemConstants.SYSTEM_CODE.LXG_MAIN, new SetupSystemModel());
        
        refererUrl_ = ss.getSystem_api_url()+"/welcome";
//        refererUrl_ = "http://localhost:8080/utimaps_internal/welcome";
        Debug.printFrameworkDebug("logout  to be remove: tt: " + refererUrl_);

        return "refererUrl";
    }
    
    private void clearSessionMap(Map sessionMap) {
        String pubkey = (String)sessionMap.get("pubkey");
        String prikey = (String)sessionMap.get("prikey");
        sessionMap.clear();
        sessionMap.put("pubkey", pubkey);
        sessionMap.put("prikey", prikey);
    }

    private Boolean isSwitch = Boolean.FALSE; //no need gatter/setter for this variable

    public String processSwitch() throws Exception {
        Map sessionMap = ActionContext.getContext().getSession();
        isSwitch = Boolean.TRUE;
        if (sessionMap.get("acting_user_id") == null) {
            userId = (String) sessionMap.get("userId");
        } else {
            userId = (String) sessionMap.get("acting_user_id");
        }
        try {
            return processlogin();
        } catch (CustomBaseException cbe) {
            addActionError(cbe.getMessage());
            return "switchFail";
        }
    }

    public String withdrawActing() throws Exception {
        Map sessionMap = ActionContext.getContext().getSession();
        try {
            UserDAOImpl userDAO = new UserDAOImpl();
            userDAO.setSession(baseDAO.getSession());
//            userDAO.withdrawActing((String) sessionMap.get("userId"));
            sessionMap.remove("acting_user_id");
            sessionMap.remove("actingUserName");
            sessionMap.remove("act_end_date");
            sessionMap.remove("act_start_date");
            sessionMap.remove("hasActiveActingRecord_actedBy");
        } catch (Exception e) {
            addActionError(e.getMessage());
        }
        return SUCCESS;
    }

    public byte[] decrypt(byte[] privateKey, byte[] inputData)
            throws Exception {

        PrivateKey key = KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(privateKey));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decryptedBytes = cipher.doFinal(inputData);

        return decryptedBytes;
    }
    
    private Boolean isFromProcessMapSSO = Boolean.FALSE;
    public String processMapSSO() throws Exception {
        try {
            isFromProcessMapSSO = Boolean.TRUE;
            processlogin();
            Map sessionMap = ActionContext.getContext().getSession();
            if (sessionMap.get("logined") != null) {
                sessionMap.remove("map_sso_uid");
                addActionMessage(getTextProvider().getText("swkId.mapped", new String[]{(String)sessionMap.get("loginId"), (String)sessionMap.get("userName")}));
            } else {
                addActionError("errors.mappedSwkIdFail");
                return "map_sso_uid";
            }
        } catch (Exception e) {
            return "map_sso_uid";
        }
        return SUCCESS;
    }
    public String processlogin() throws Exception {
        Debug.printFrameworkDebug("process Login.*******************************" +userId);
        SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();
        if (sessionMap.containsKey("map_sso_uid") && !isFromProcessMapSSO) { //in case ppl bypass mapping/direct access login
            addActionMessage(getText("errors.needToMapSwkId"));
            return "map_sso_uid";
        }
        userIcNo = userIcNo.replaceAll("\\D", "");//remove hyphen for user 
        java.sql.Timestamp timeBefore = DateUtil.getCurrentTimestamp();
        Debug.printFrameworkDebug("refererurl = "+refererUrl_);
        Debug.printFrameworkDebug("isSwitch " + isSwitch);
        if (!isSwitch) {
            if (sessionMap.get("logined") != null) {
//                Debug.printFrameworkDebug("here proces slogins");
                if (systemType_.equals(sessionMap.get("loginSystemType_"))) {
                    if (((String) sessionMap.get("logined")).equals("true")) {
//                        populateMenu();
//                    getNotificationList_(); //if this method is first called from jsp, it will cause some performance issue during stresstest
                        return SUCCESS;
                    }
                } else {
                    sessionMap.remove("logined");
                }
            }
        }

        String strLoginStatus = SystemConstants.AUDIT_LOGIN.STATUS.FAIL;
        try {
            Boolean hasActiveActingRecord_actedBy = Boolean.FALSE;
            UserDAOImpl userDAO = new UserDAOImpl();
            ApplicationDAOImpl applicationDAO = new ApplicationDAOImpl();
            User user = null;
            userDAO.setSession(baseDAO.getSession());
            applicationDAO.setSession(baseDAO.getSession());
            Object multilingualDD = sessionMap.get("multilingualDD");
            Object defaultLanguage = sessionMap.get("defaultLanguage");
            Object multilingualSupport = sessionMap.get("multilingualSupport");
            sessionMap.remove("crypto_salt");
            sessionMap.remove("crypto_iv");
            //                Debug.printFrameworkDebug("AFTER : " + passwd);
            // ThoTH @ 2-Apr-2014 :: Decrypt Password <END>
//                }                                                                   // << remove this line after testing
            try {
                Debug.printFrameworkDebug("usePubPrivateKey " + usePubPrivateKey);
                if (usePubPrivateKey) {
                    passwd = new String(decrypt(Base64.decodeBase64((String)"MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIHx+ID7pzs4v7zZQ61zQ0Db0K4y1WHsySDH0YJUxf0ZHkDR8Ao4/upY6wJhIr9DUqS3fdvF+xFIa5N/4qXNThAKyfvsC4Omg9fQCzVdnOW1cKuX74pp933da0wmyZRizF9jD+e9uk+39RjpMFd+SHDJR6ISR3N+h6jP8gFYbaXPAgMBAAECgYAFzDH2L0Qq1EFZv6fRWU8q2aPZ4dK7Z8EhgUjLs9SRkklTQwqeqiRVeFoj5QmT1wEc8ELhR1gIUV7bWd1W4G4XIHcX/P7zOkWkaWyJhqXKbJ/6vxyonSaeBHCdFQU764rUrvW+ksMoH8kjH4fWXf2xtgT6QwKsFeh8xBAtvRaGAQJBAOOX3ms9C8SNdQC1XFUfU4Mlam4REOyzzlhHjhWffiefVQ0e/8k6H6ioVQXAABRQwwuk8ZO5nqP9NMsxjSfsCY8CQQCSKgVUxgs2SSZOYHIk6r3Ct9+JLUt0OfPkg+pmaWRCShZvpMb9zgfg9lwrUmhp9k3PQrVl9TicgR0elZIyO//BAkB1Qdho/cGjWZIPdGEGxiX7qnpEDOzgVt8X4n3UrCCN/2wNX4w/O3/2IAv1827XCKkExc1k47xVFu5tRbFJATYHAkBn/OFiGKwtaQmJxXAv95oeCHdOvxzpibmNIJUJW33q847WyyKPu587W8rjP3Ptv0BIbqtj6HzoS6uN8NfcSXeBAkBtxJxpC5F3qv63M7UGpv10cSfi/7IJvBMQS3aViW1jlSX1nvLmZhHMl4hHJUdwuSqt9eQ5xYxiCj23yAJEhghJ"), Base64.decodeBase64(passwd)));
                }
                Debug.printFrameworkDebug("userIcNo " + userIcNo);
                //Debug.printFrameworkDebug("password " +passwd);
                setPageTitle_("Home");
                if (!Validator.isEmpty(userIcNo) && !userIcNo.equals("000")) {
                    Debug.printFrameworkDebug("Login IC");
                    Debug.printFrameworkDebug("Login IC");
                    user = userDAO.processLogin(userIcNo, passwd, systemType_, "IC");
                } else if (!Validator.isEmpty(userEmail)) {
                    Debug.printFrameworkDebug("Login EMAIL");
                    Debug.printFrameworkDebug("Login EMAIL");
                    user = userDAO.processLogin(userEmail, passwd, systemType_, "EMAIL");
                } else {
                    Debug.printFrameworkDebug("Login USERID");
                    Debug.printFrameworkDebug("Login USERID");
                    user = userDAO.processLogin(userId, passwd, systemType_, "USERID");
                }
            } catch (Exception e) {
                throw e;
            }
//            }     
            if (user != null) {
                
                //invalidate
                sessionMap.invalidate();
                sessionMap.remove("lookupRefererUrl_"); //remove if login successfully
                sessionMap.remove("finalLookupDesc"); //remove if login successfully
                sessionMap.remove("notFromLookup"); //remove if login successfully
                sessionMap.put("multilingualDD", multilingualDD);
                sessionMap.put("defaultLanguage", defaultLanguage);
                sessionMap.put("multilingualSupport", multilingualSupport);
                Debug.printFrameworkDebug("User not Empty");

                strLoginStatus = userDAO.getAuditLoginStatus();
                auditLogin(SystemConstants.AUDIT_LOGIN.ACTION.LOGIN, systemType_, strLoginStatus, user.getUs_user_id());
                
                sessionMap.put("ihLogin", ihLogin);  // ThoTh @ 18-Jan-2016
                sessionMap.put("loginSystemType_", systemType_);
                sessionMap.put("logined", "true");
                sessionMap.remove("sSysssoUserObjId"); //remove sso login pk
                sessionMap.put("antiCsrf", CommonFunction.getId(30));
                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                SessionListener.activeSessionMap.put(request.getSession().getId(), null);
                sessionMap.put("user_ip", CommonFunction.getUserIp(request));
                sessionMap.put("user_agent", CommonFunction.getUserAgent(request));
                sessionMap.put("context", new Date());
                sessionMap.put("loginTime", DateUtil.getCurrentTimestamp());
                sessionMap.put("div_assigned", divList); //yonglai :: put div code follow user group assigned
                //Debug.printFrameworkDebug("session user id " +user.getUs_id());
                sessionMap.put("userId", user.getUs_id());
//                sessionMap.put("empId", user.getEmp_id());

//                Debug.printFrameworkDebug("systemType_ = " + systemType_);
                if (systemType_.equals(SystemConstants.SYSTEM_TYPE.PUBLIC)) {
                    sessionMap.put("loginId", user.getUs_id_number());
                } else {
                    sessionMap.put("loginId", user.getUs_user_id());
                }
                sessionMap.put("userName", user.getUs_user_name());
//                sessionMap.put("userDivisionId", user.getUs_division());
                sessionMap.put("version", SystemConstants.DOMAIN.version);
                sessionMap.put("inbox", 0);//amywyp @ 13-10-2017
                sessionMap.put("todaysDateTime", getLocalDate_str());//amywyp @ 13-10-2017
                sessionMap.put("userEmail", user.getUs_email()); //amywyp @ 10-05-2018
                loadGlobalPreference();
                List<UserPreferenceModel> newPrefList = null;
                for (ParameterModel param : modelList) {
                    if (param.getField_indicator().equals("input")) {
                        Boolean found = Boolean.FALSE;
                        for (UserPreferenceModel userPref : user.getUserPreferenceList()) {
                            if (userPref.getPref_code().equals(param.getParameter_code())) {
                                sessionMap.put(userPref.getPref_code(), userPref.getPref_value());
                                found = Boolean.TRUE;
                                break;
                            }
                        }
                        if (!found) {
                            sessionMap.put(param.getParameter_code(), param.getParameter_value());
                            if (newPrefList == null) newPrefList = new ArrayList();
                            UserPreferenceModel newUserPref = new UserPreferenceModel();
                            newUserPref.setUs_id(user.getUs_id().toString());
                            newUserPref.setPref_code(param.getParameter_code());
                            newUserPref.setPref_value(param.getParameter_value());
                            newPrefList.add(newUserPref);
                        }
                    }
                }
                if (newPrefList != null) {
                    BaseDAO insertPrefDAO = new BaseDAOImpl();
                    try {
                        insertPrefDAO.beginBatchTransaction();
                        for (UserPreferenceModel userPref : newPrefList) {
                            insertPrefDAO.insertWithSession(insertPrefDAO.getSession(), userPref);
                        }
                        insertPrefDAO.commitBatchTransaction();
                    } catch (Exception e) {
                        insertPrefDAO.rollbackBatchTransaction();
                    } finally {
                        insertPrefDAO.closeSession();
                    }
                }
                //set co_id to session - ahmadni @ 4-Jul-2016
                //problem: user might have 2 active company
                //solution: will get the latest active user company
                Map userCoParam = new HashMap();
                userCoParam.put("us_id", user.getUs_id());
                userCoParam.put("usco_status", "Y");

                request.getSession().setAttribute("loginId", (String)sessionMap.get("loginId"));
                request.getSession().setAttribute("loginIp", request.getRemoteAddr());
                SessionListener.activeSessionMap.put((String)sessionMap.get("loginId"), ((HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST)).getSession());
                if (user.getUs_admin().equalsIgnoreCase("Y")) {
                    applicationList = applicationDAO.getAdminApplications();
                }

                populateMenu2(user);
                
                /*
                if (systemType_.equals(SystemConstants.SYSTEM_TYPE.DEFAULT)) {
                    if (user.getWfGroupUserList().size() > 0) {
                        myProfileList += "<div style='padding:10px 15px;'>" + getText("userAccess.navStep.KumpulanPengesah");
                        myProfileList += "<div class=\"ul_image\">";
                        for (WorkflowGroupUserModel wfUserGroup : user.getWfGroupUserList()) {
                            myProfileList += "<p>" + wfUserGroup.getWorkflowgroup().getWg_name() + "</p>";
                        }
                        myProfileList += "<br style=\"clear: left\" /></div>";
                        myProfileList += "</div>";
                    }
                }*/
                ActionContext.getContext().getSession().remove("myProfileList");
                ActionContext.getContext().getSession().put("myProfileList", myProfileList);
                //Added by ChangMH @ 01-Aug-2014 :: To display my profile details in mega menu - END

                //Debug.printFrameworkDebug("user" + ((Application)((UserGroup)((GroupUser)user.getGroupUserList()).getUserGroupList()).getGroupApplication()).getApplication_name());
//                if (sessionMap.get("lastVisit") != null) {
//                    HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
//                    response.sendRedirect(sessionMap.get("lastVisit").toString());
//                }
                //check disciplinary status - ahmadni 29-Sept-2015   
                //  Long count = baseDAO.sqlCountRecord("select count(*) from t_tata_disciplinary where active='Y' and work_status != '1' and employee_id = '"+ user.getEmp_id() +"' and effective_date+punish_duration > CURRENT_TIMESTAMP");
//                if (count>0){
//                    //sessionMap.put("access_mode", SystemConstants.ACCESS_MODE.READONLY);
//                    Debug.printFrameworkDebug("@@@@@ Currently readonly access mode disable for development @@@@@");
//                    sessionMap.put("access_mode", "DEVELOPMENT");//for temporary only , no need put at SC
//                }else{
//                    sessionMap.put("access_mode",SystemConstants.ACCESS_MODE.NORMAL);            
//                }    
                // Commented by ThoTH @ 3-Nov-2015
//                if (hasActiveActingRecord_actedBy) {
//                    return "actingOption";//ERROR
//                }
//            Debug.printDebug(" " + sessionMap);
                // ThoTH @ 15-Feb-2016
                if (!Validator.isEmpty(refererUrl_)) {
                    //Debug.printFrameworkDebug("redirectUrl_1: " + refererUrl_);
//                    redirectUrl_ = URLDecoder.decode(redirectUrl_, "UTF-8");
//                    Debug.printFrameworkDebug("redirectUrl_2: " + redirectUrl_);
//                    redirectUrl_ = "loadEditPageLeave?dType=LMS_CutiRehat&dCode=14555032029846s6JfH0&dCode2=1455503186843YGydML0";
//                    redirectUrl_ = URLEncoder.encode("loadEditPageLeave?dType=LMS_CutiRehat&dCode=14555032029846s6JfH0&dCode2=1455503186843YGydML0", "UTF8");
                    return "refererUrl";
                }

                // ThoTH @ 28-Aug-2014
                prepareRightPanel();
//                getNotificationList_(); //if this method is first called from jsp, it will cause some performance issue during stresstest
                return SUCCESS;
            } else {
                Debug.printFrameworkDebug("--- 555");
                Debug.printFrameworkDebug("User Empty");
                if (sessionMap.get("logined") != null) {
                    if (((String) sessionMap.get("logined")).equals("true")) {
//                        getNotificationList_(); //if this method is first called from jsp, it will cause some performance issue during stresstest
                        return SUCCESS;
                    }
                }
            }
        } catch (CustomBaseException cbe) {
            cbe.printStackTrace();
            addActionError(cbe.getMessage());
            clearSessionMap(sessionMap);
        } catch (Exception ex) {
            ex.printStackTrace();
            clearSessionMap(sessionMap);
            new LogFunction().logError(this.getClass(), "", ex);
//        } finally {
            /*if (userDAO.getSession().isOpen()){
             userDAO.getSession().clear();
             userDAO.getSession().flush();
             userDAO.getSession().close();
             Debug.printFrameworkDebug("userDAO Session Closed----------");
             }
             if (userDAO.getSession().isConnected()){
             userDAO.getSession().disconnect();
             }
             if (applicationDAO.getSession().isOpen()){
             applicationDAO.getSession().clear();
             applicationDAO.getSession().flush();
             applicationDAO.getSession().close();
             Debug.printFrameworkDebug("application Session Closed----------");
             }
             if (applicationDAO.getSession().isConnected()){
             applicationDAO.getSession().disconnect();
             }*/
//            userDAO.closeSession();
//            applicationDAO.closeSession();
        }
//        if (!isSwitch) {
//            if (systemType_.equals(SystemConstants.SYSTEM_TYPE.ESS)) {
//                auditLogin(SystemConstants.AUDIT_LOGIN.ACTION.LOGIN, SystemConstants.AUDIT_LOGIN.TYPE.ESS, strLoginStatus, getUserId());  // ThoTH @ 7-Jan-2014
//            } else {
//                auditLogin(SystemConstants.AUDIT_LOGIN.ACTION.LOGIN, SystemConstants.AUDIT_LOGIN.TYPE.INTERNAL, strLoginStatus, getUserId());  // ThoTH @ 7-Jan-2014
//            }
//        }
        if (getActionErrors().size() == 0) {
            addActionError(getText("invalidLogin"));
        }

//        String sqlQuery = "select top 2 * from t_setup_announcement order by created_date desc";
//         Query query = baseDAO.getSession().createSQLQuery(sqlQuery).setResultTransformer(Transformers.aliasToBean(SetupAnnouncementModel.class));
//        announceList = query.list();
//        if (announceList.size() < 2) {
//        endSize_ = String.valueOf(announceList.size() - 1);
//    }
        Debug.printFrameworkDebug("----- Init Login -------");
//        Debug.printFrameworkDebug("AnnounceList.size(): " + announceList.size());
//        Debug.printFrameworkDebug("AnnounceListESS.size(): " + announceListESS.size());
        //sereneChye @ 29/9/2014 
//        prepareAnnouncement();
        if (!sessionMap.containsKey("lookupRefererUrl_") && !Validator.isEmpty(refererUrl_) && !refererFrom.isEmpty()) {
            sessionMap.put("lookupRefererUrl_", refererUrl_);
            sessionMap.put("lookupDescription", finalLookupDesc);
            Debug.printFrameworkDebug("--- 666");
            return ERROR+refererFrom+"RefererUrl";
        }
        if (sessionMap.containsKey("lookupRefererUrl_")) {
            Debug.printFrameworkDebug("actionError = " + getActionErrors());
            Debug.printFrameworkDebug("--- 777");
            return "login_modal";
        }
        return ERROR;

        //Module module = groupApp.getGroupApplication().getAttachedModule();
        //getModules(module, moduleList);
    }

    //Added by Delvene @ 10-Dec-2013 :: To support main screen navigation
    //Edited by Delvene @ 11-Sep-2014 :: Add systemType parameter, to determine what type of Module Tile (DEFAULT/ESS) to populate
    private String populateModuleTile(Module moduleObj, int showModule, Application appObj, String systemType) {
        String tileString = "";
        setPageTitle_("Home");
//        Debug.printFrameworkDebug("showModule :" + showModule + ":: modulecode :" + moduleObj.getModule_code() + ":: modulename: " + moduleObj.getModule_name());

        if (showModule == 1) {
            tileString += "<div class=\"col-lg-3 col-md-3 col-sm-6 col-xs-12\"><div class=\"icon-box\">";
            if (appObj == null) {
                if (systemType.equals(SystemConstants.SYSTEM_TYPE.PUBLIC)) {
                    tileString += "<a class='nb-btn-circle' href='loadMainPageLoginESS?moduleCode=" + moduleObj.getModule_code() + "'>";
                    tileString += "<i class='fa fa-gears fa-2x text-theme'></i><br><br></a>";
                    tileString += " <h3 class='title-sm text-theme-sm text-theme'>" + moduleObj.getModule_name() + "</h3>";
                    tileString += "<p class='text-theme-sm'></p>";
                } else {
                    tileString += "<a class='nb-btn-circle' href='loadMainPageLogin?moduleCode=" + moduleObj.getModule_code() + "'>";
                    if (moduleObj.getModule_code().equals("SAM")) {
                        tileString += "<i class='fa fa-user fa-2x text-theme'></i><br><br></a>";
                    } else {
                        tileString += "<i class='fa fa-gears fa-2x text-theme'></i><br><br></a>";
                    }
                    tileString += " <h3 class='title-sm text-theme-sm text-theme'>" + moduleObj.getModule_name() + "</h3>";
                    tileString += "<p class='text-theme-sm'></p>";
                }

            } else {
                tileString += "<a href='" + appObj.getAction_name() + "'>"; //anchor open tag
//                Debug.printFrameworkDebug("appObj 111 :"+appObj.getApplication_code());
                tileString += "<a class='nb-btn-circle' href='" + appObj.getAction_name() + "'>"; 
                if (moduleObj.getModule_code().equals("SitingApplication")) {
                    tileString += "<i class='fa fa-map-marker fa-2x text-theme'></i><br><br></a>";
//                } else if (moduleObj.getModule_code().equals("eQP")) {
//                    tileString += "<i class='fa fa-user fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("Message")) {
                    tileString += "<i class='fa fa-envelope fa-2x text-theme'></i><span class=\"indicator-dot\">" + 0 + "</span><br><br></a>";
                } else if (moduleObj.getModule_code().equals("CaseAcessRight")) {
                    tileString += "<i class='fa fa-key fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("publicApplication")) {
                    tileString += "<i class='fa fa-file-text-o fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("MeetingFacilitation")) {
                    tileString += "<i class='fa fa-users fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("AppEnquiry")) {
                    tileString += "<i class='fa fa-building-o fa-2x text-theme'></i><br><br></a>";
//                    tileString += "<i class='fa fa-keyboard-o fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("SitingInternal")) {
                    tileString += "<i class='fa fa-map-marker fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("Payment")) {
                    tileString += "<i class='fa fa-dollar fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("QP")) {
                    tileString += "<i class='fa fa fa-drivers-license-o fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("MP")) {
                    tileString += "<i class='fa fa-id-badge fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("publicLicense")) {
                    tileString += "<i class='fa fa-newspaper-o fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("BatchEndorse")) {
                    tileString += "<i class='fa fa-gavel fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("EndorseDecision")) {
                    tileString += "<i class='fa fa-check fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("SpaForms")) {
                    tileString += "<i class='fa fa-file-text-o fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("WFM")) {
                    tileString += "<i class='fa fa-list-alt fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("DCApplication")) {
                    tileString += "<i class='fa fa-building  fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("UserGuide")) {
                    tileString += "<i class='fa fa-info fa-2x text-theme'></i><br><br></a>";    
                } else {
                    tileString += "<i class='fa fa-gears fa-2x text-theme'></i><br><br></a>";
                }

                tileString += " <h3 class='title-sm text-theme-sm text-theme'>" + moduleObj.getModule_name() + "</h3>";
                if (moduleObj.getModule_code().equals("SitingApplication") || moduleObj.getModule_code().equals("SitingInternal")) {
                    tileString += "<p class='text-theme-sm'>for Government Projects / NGOS</p>";
                } else if (moduleObj.getModule_code().equals("DCApplication") || moduleObj.getModule_code().equals("AppEnquiry")) {
                    tileString += "<p class='text-theme-sm'>for Development of Lands or Buildings</p>";
                } else if (moduleObj.getModule_code().equals("CaseAcessRight")) {
                    tileString += "<p class='text-theme-sm'>for Project Proponents</p>";
                } else if (moduleObj.getModule_code().equals("UserGuide")) {
                    tileString += "<p class='text-theme-sm'>Videos, Manuals, Guidelines, Installers </p>";
                }
                
            }
            tileString += "</div></div>";
        } else if (showModule > 1) {
            tileString += "<div class=\"col-lg-3 col-md-3 col-sm-6 col-xs-12\"><div class=\"icon-box\">";
            if (systemType.equals(SystemConstants.SYSTEM_TYPE.PUBLIC)) {
                Debug.printFrameworkDebug("is public!");
                tileString += "<a class='nb-btn-circle' href='loadMainPageLoginESS?moduleCode=" + moduleObj.getModule_code() + "'>";
                tileString += "<i class='fa fa-gears fa-2x text-theme'></i><br><br></a>";
                tileString += " <h3 class='title-sm text-theme-sm text-theme'>" + moduleObj.getModule_name() + "</h3>";
                tileString += "<p class='text-theme-sm'></p>";
            } else {
                tileString += "<a class='nb-btn-circle' href='loadMainPageLogin?moduleCode=" + moduleObj.getModule_code() + "'>";
                if (moduleObj.getModule_code().equals("WFM")) {
                    tileString += "<i class='fa fa-list-alt fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("WFS")) {
                    tileString += "<i class='fa fa-gear fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("publicApplication")) {
                    tileString += "<i class='fa fa-file-text-o fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("publicLicense")) {
                    tileString += "<i class='fa fa-newspaper-o fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("BatchApproval")) {
                    tileString += "<i class='fa fa-check fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("myApplication")) {
                    tileString += "<i class='fa fa-building fa-2x text-theme'></i><br><br></a>";
                } else if (moduleObj.getModule_code().equals("report")) {
                    tileString += "<i class='fa fa-file-text-o fa-2x text-theme'></i><br><br></a>";    
                } else if (moduleObj.getModule_code().equals("SAM")) {
                    tileString += "<i class='fa fa-user fa-2x text-theme'></i><br><br></a>";
                } else {
                    tileString += "<i class='fa fa-gears fa-2x text-theme'></i><br><br></a>";
                }
                tileString += " <h3 class='title-sm text-theme-sm text-theme'>" + moduleObj.getModule_name() + "</h3>";
                tileString += "<p class='text-theme-sm'></p>";
            }
            tileString += "</div></div>";
        }
        return tileString;
    }

    private final String hasAppSql = "SELECT COUNT(*) AS ccCount FROM t_setup_group_user gu, t_setup_group_app ga, t_setup_application app " +
                     " WHERE gu.ug_id = ga.ug_id " +
                     "   AND ga.application_id = app.application_id  AND app.hidden = 'N' " +
                     "   AND us_id = :userId AND app.attached_module_id = :moduleId";
    
    private Boolean subModulesHasApp(List<Module> childModules, String userId) throws Exception {
        if (childModules !=null) {
            Map param = new HashMap();
            for (Module childModule : childModules) {
                param.put("userId", userId);
                param.put("moduleId", childModule.getID());
                if (baseDAO.sqlCountRecord(hasAppSql, param) > 0) {
                    return Boolean.TRUE;
                } else {
                    return subModulesHasApp(childModule.getChildModule(), userId);
                }
            }
        }
        return Boolean.FALSE;
    }
    public String loadMainPage() {
        ModuleDAOImpl moduleDAO = new ModuleDAOImpl();
        moduleDAO.setSession(baseDAO.getSession());
        ApplicationDAOImpl applicationDAO = new ApplicationDAOImpl();
        applicationDAO.setSession(baseDAO.getSession());
        User user = null;
        try {
            if (ActionContext.getContext().getSession().get("userId") instanceof Integer) {
                user = (User) baseDAO.getModelById((Integer) ActionContext.getContext().getSession().get("userId"), User.class);
            } else {
                user = (User) baseDAO.getModelById((String) ActionContext.getContext().getSession().get("userId"), User.class);
            }
            if (user == null) return init();
        } catch (Exception e) {
            return init();
        }
        List<Application> appList = new ArrayList();
        List<Application> appList_ESS = new ArrayList();
        int appSize = 0;
        int subModSize = 0; //Delvene @ 12-Feb-2014
        String strTempTile = ""; // Delvene @ 11-Feb-2014

        if (user.getUs_admin().equalsIgnoreCase("Y")) {
            appList = applicationDAO.getAdminApplications();
        }

        //ESS
        if (systemType_.equals(SystemConstants.SYSTEM_TYPE.PUBLIC)) {
            for (GroupUser gu : user.getGroupUserList()) {
                for (GroupApplication ga : gu.getUserGroup().getGroupApplication()) {
                    if (!containsApplication(ga.getApplication(), appList)) {
                        if (ga.getApplication().getSystem_type().equals(SystemConstants.SYSTEM_TYPE.PUBLIC)) {
                            appList.add(ga.getApplication());
                        }
                    }
                }
            }

        } else {    //Default
            for (GroupUser gu : user.getGroupUserList()) {
                for (GroupApplication ga : gu.getUserGroup().getGroupApplication()) {
                    if (!containsApplication(ga.getApplication(), appList)) {
                        if (!ga.getApplication().getSystem_type().equals(SystemConstants.SYSTEM_TYPE.PUBLIC)) {
                            appList.add(ga.getApplication());
                        }
                    }
                }
            }
        }

//        for (GroupUser gu : user.getGroupUserList()) {
//            for (GroupApplication ga : gu.getUserGroup().getGroupApplication()) {
//                if (!containsApplication(ga.getApplication(), appList)) {
//                    // ThoTH @ 20-May-2013A
//                    if (ga.getApplication().getSystem_type().equals(SystemConstants.SYSTEM_TYPE.ESS)) {
//                        appList_ESS.add(ga.getApplication());
//                    } else {
//                        appList.add(ga.getApplication());
//                    }
//                    // ThoTH @ 20-May-2013 -- END
////                  applicationList.add(ga.getApplication());
//              }
//            }
//        }
//        Debug.printFrameworkDebug("moduleCode :" + moduleCode);
        if (moduleCode.equals("SAM")) {
            setPageTitle_("System Administration");
        } else if (moduleCode.equals("WFS")) {
            setPageTitle_("Workflow Configuration");
        } else if (moduleCode.equals("WFM")) {
            setPageTitle_("Assignment");
        }
        Module moduleObj = moduleDAO.getModuleByCode(moduleCode, new Module());

        List<Module> moduleList = new ArrayList();
        /*
        // ThoTH @ 21-Jan-2014 :: Get SubModule for AppTile purpose //MSSQL
        String strSql = "WITH RECURSIVE n(module_id) AS "
                + "    (SELECT module_id FROM t_setup_module WHERE module_id = ? "
                + "       UNION ALL "
                + "     SELECT nplus1.module_id FROM t_setup_module AS nplus1, n "
                + "      WHERE n.module_id = nplus1.parent_module_id) "
                + "SELECT count(*) as ccCount FROM t_setup_group_user gu, t_setup_group_app ga, t_setup_application app, n "
                + " WHERE gu.ug_id = ga.ug_id "
                + "   AND ga.application_id = app.application_id  and app.hidden = 'N' "
                + "   AND us_id = ? and app.attached_module_id = n.module_id";

        Map<Integer, String> param = new HashMap();
        CommonFunction cf = new CommonFunction();

        try {
            for (Module module : moduleObj.getChildModule()) {
                param.clear();
                param.put(1, module.getID());
                param.put(2, user.getID());
                Debug.printFrameworkDebug("module.getID() = " + module.getID());
                Debug.printFrameworkDebug("user.getID() = " + user.getID());
                for (Map<String, BigInteger> myMap : (List<Map>) cf.getListFromSqlWithSession(baseDAO.getSession(), strSql, param)) {
                    if (myMap.get("cccount").longValue() > 0) {
                        moduleList.add(module);
                        break;
                    }
                }
            }

        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }
        // ThoTH @ 21-Jan-2014 <END>
        */
        Map param = new HashMap();
        param.put("userId", user.getID());
        for (Module module : moduleObj.getChildModule()) {
            try {
                param.put("moduleId", module.getID());
                if (baseDAO.sqlCountRecord(hasAppSql, param) > 0) {
                    moduleList.add(module);
                } else if (subModulesHasApp(module.getChildModule(), user.getID())){
                    moduleList.add(module);
                }
            } catch (Exception e) {
            }
        }

        //Default
//        Collections.sort(appList, new CommonComparator(new String[]{"getShow_in_main_order"}));
        Collections.sort(appList, new ApplicationNameComparator());
        if (moduleObj != null) {
//            appTile += "<tr valign='top'>";
            //For back button
//                appTile += "<td id=\"back\" class=\"menu-tile-name\" align=\"center\" width=\"120px\" height=\"120px\" style=\"background-image: url(images/menu/back_main.png)\" onmouseover='changeImage(this)' onmouseout='changeImageBack(this)'>";
//                    appTile += "<a href='initLogin'>"; //anchor open tag
//                        appTile += "<table border='0' cellpadding='0' cellspacing='0'>";
//                            appTile += "<tr valign='bottom'>";
//                                appTile += "<td align='center' height=105px' width='110px'>KEMBALI KE MENU UTAMA</td>";
//                            appTile += "</tr>";
//                        appTile += "</table>";
//                    appTile += "</a>"; //anchor end tag
//                appTile += "</td>";
            //For back button - END
            // ThoTH @ 21-Jan-2014 :: Display Module for AppTile
            for (Module module : moduleList) {
                appSize++;
                subModSize++;
                strTempTile = "";
//                if(appSize % 6 == 0) {
//                    appTile += "</tr>";
//                    appTile += "<tr>";
//                    appSize ++;
//                    appTile += "<td></td>";
//                }

                //Delvene @ 12-Feb-2014
                for (Application app : appList) {
                    if (app.getAttached_module_id().equals(module.getModule_id()) && !app.getHidden().equals("Y")) {
//                        strTempTile += "<a  href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "'>" + StringEscapeUtils.escapeHtml4(app.getApplication_name()) + "</a> <br>";
                        strTempTile += "<ul class='dMenu'><li><a  href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "'>" + StringEscapeUtils.escapeHtml4(app.getApplication_name()) + "</a></li></ul>";
                    }
                }
//                Debug.printFrameworkDebug("module.getModule_name() :"+module.getModule_name());

//                appTile += "<td>";
//                appTile += "<table border='0' cellpadding='2' cellspacing='2' class='moduleTitle'>";
                appTile += "<div class='col-md-4'><div class='item'><div class='icon-box espaBox bordered'>";
                if (moduleObj.getParentModule() != null) {
//                    appTile += "<td  class=\"menu-tile-name \" align=\"center\" height=\"60px\" style=\"background-image: url(images/menu/" + moduleObj.getParentModule().getModule_code() + "_sub.png); background-repeat: no-repeat;\">";
                    appTile += "<i class=\"fa fa-gears fa-2x text-theme text-green\"></i>";
                } else {
//                    appTile += "<td class=\"menu-tile-name\" align=\"center\" height=\"60px\" style=\"background-image: url(images/menu/" + moduleObj.getModule_code() + "_sub.png); background-repeat: no-repeat;\">";
//                    appTile += "<i class=\"fa fa-gears fa-3x text-theme text-green\"></i>";
                    if (module.getModule_name().equals("System Security")) {
                        appTile += "<i class=\"fa fa-shield fa-2x text-theme text-green\"></i>";
                    } else if (module.getModule_name().equals("Technical Configuration")) {
                        appTile += "<i class=\"fa fa fa-wrench fa-2x text-theme text-green\"></i>";
                    } else if (module.getModule_name().equals("Business Paramaters")) {
                        appTile += "<i class=\"fa fa fa-suitcase fa-2x text-theme text-green\"></i>";
                    } else if (module.getModule_name().equals("BSC Report")) {
                        appTile += "<i class=\"fa fa fa-file-text-o fa-2x text-theme text-green\"></i>";
                    } else if (module.getModule_code().equals("SAM")) {
                        appTile += "<i class=\"fa fa-user fa-2x text-theme text-green\"></i>";
                    } else {
                        appTile += "<i class=\"fa fa-gears fa-2x text-theme text-green\"></i>";
                    }
                }
//                appTile += "<td class='moduleName' >" + module.getModule_name() + "</td>";
                appTile += "<a href=loadMainPageLogin?moduleCode="+StringEscapeUtils.escapeEcmaScript(module.getModule_code())+"><h3 class=\"title-sm text-theme\">" + module.getModule_name() + "</h3></a><br>";
                appTile += strTempTile;
                // appTile += "</td>";
                // appTile += "</tr>";
                appTile += "</div></div></div>";
//                appTile += "</table></div>";
//                appTile += "</td>";

//                appTile += "<td id=\"" + moduleObj.getModule_code() + "\" class=\"menu-tile-name\" align=\"center\" width=\"120px\" height=\"120px\" style=\"background-image: url(images/menu/" + moduleObj.getModule_code() + "_main.png); background-repeat:no-repeat;\" onmouseover='changeImage(this)' onmouseout='changeImageBack(this)'>";
//                    appTile += "<a href='loadMainPageLogin?moduleCode=" + module.getModule_code() + "'>"; //anchor open tag
//                        appTile += "<table border='0' cellpadding='0' cellspacing='0'>";
//                            appTile += "<tr valign='bottom'>";
//                                appTile += "<td align='center' height='105px' width='110px'>" + module.getModule_name() + "</td>";
//                            appTile += "</tr>";
//                        appTile += "</table>";
//                    appTile += "</a>"; //anchor end tag
//                appTile += "</td>";
//                if (subModSize % 3 == 0) {
//                    appTile += "</tr>";
//                    appTile += "<tr valign='top'>";
//                } else {
//                    appTile += "<td width='30px'></td>";
//                }
            }
            // ThoTH @ 21-Jan-2014 :: Display Module for AppTile <END>
            strTempTile = "";
            String temp = "";
            for (Application app : appList) {
                if (app.getAttached_module_id().equals(moduleObj.getModule_id()) && !app.getHidden().equals("Y")) {
                    appSize++;
//                    if(appSize % 6 == 0) {
//                        appTile += "</tr>";
//                        appTile += "<tr>";
//                        appSize ++;
//                        appTile += "<td></td>";
//                    }
//                    strTempTile += "<a href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "'>" + StringEscapeUtils.escapeHtml4(app.getApplication_name()) + "</a> <br>";
                    strTempTile += "<ul class='dMenu'><li><a href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "'>" + StringEscapeUtils.escapeHtml4(app.getApplication_name()) + "</a></li></ul>";
//                    appTile += "<td id=\"" + moduleObj.getModule_code() + "\" class=\"menu-tile-name\" align=\"center\" width=\"120px\" height=\"120px\" style=\"background-image: url(images/menu/" + moduleObj.getModule_code() + "_main.png); background-repeat:no-repeat;\" onmouseover='changeImage(this)' onmouseout='changeImageBack(this)'>";
//                        appTile += "<a href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "'>"; //anchor open tag
//                            appTile += "<table border='0' cellpadding='0' cellspacing='0'>";
//                                appTile += "<tr valign='bottom'>";
//                                    appTile += "<td align='center' height='105px' width='110px'>" + StringEscapeUtils.escapeHtml4(app.getApplication_name()) + "</td>";
//                                appTile += "</tr>";
//                            appTile += "</table>";
//                        appTile += "</a>"; //anchor end tag
//                    appTile += "</td>";
                }
            }
            if (!Validator.isEmpty(strTempTile)) {
//                appTile += "<td>";
                temp += "<div class='col-md-4'><div class='item'><div class='icon-box espaBox bordered'>";
                if (moduleObj.getParentModule() != null) {
//                    appTile += "<td class=\"menu-tile-name\" align=\"center\" height=\"60px\" style=\"background-image: url(images/menu/" + moduleObj.getParentModule().getModule_code() + "_sub.png); background-repeat: no-repeat;\">";
                    if (moduleObj.getModule_code().equals("SamSecurity")) {
                        temp += "<i class=\"fa fa-shield fa-2x text-theme text-green\"></i>";
                    } else if (moduleObj.getModule_code().equals("SamBusiness")) {
                        temp += "<i class=\"fa fa-suitcase fa-2x text-theme text-green\"></i>";
                    } else if (moduleObj.getModule_code().equals("SamTechnical")) {
                        temp += "<i class=\"fa fa-wrench fa-2x text-theme text-green\"></i>";
                    } else {
                        temp += "<i class=\"fa fa-gears fa-2x text-theme text-green\"></i>";
                    }
                } else {
                    setPageTitle_(moduleObj.getModule_name());
//                    appTile += "<td class=\"menu-tile-name\" align=\"center\" height=\"60px\"style=\"background-image: url(images/menu/" + moduleObj.getModule_code() + "_sub.png); background-repeat: no-repeat;\">";
//                    appTile += "<i class=\"fa fa-gears fa-3x text-theme text-green\"></i>";
                    if (moduleObj.getModule_code().equals("myApplication")) {
                        temp += "<i class=\"fa fa-user fa-2x text-theme text-green\"></i>";
                    } else if (moduleObj.getModule_code().equals("WFM")) {
                        temp += "<i class=\"fa fa-list-alt fa-2x text-theme text-green\"></i>";
                    } else if (moduleObj.getModule_code().equals("WFS")) {
                        temp += "<i class=\"fa fa-gear fa-2x text-theme text-green\"></i>";
                    } else if (moduleObj.getModule_code().equals("publicApplication")) {
                        temp += "<i class=\"fa fa-file-text-o fa-2x text-theme text-green\"></i>";
                    } else if (moduleObj.getModule_code().equals("publicLicense")) {
                        temp += "<i class=\"fa fa-newspaper-o fa-2x text-theme text-green\"></i>";
                    } else if (moduleObj.getModule_code().equals("BatchApproval")) {
                        temp += "<i class=\"fa fa-check fa-2x text-theme text-green\"></i>";
                    } else if (moduleObj.getModule_code().equals("MeetingFacilitation")) {
                        temp += "<i class=\"fa fa-users fa-2x text-theme text-green\"></i>";
                    } else if (moduleObj.getModule_code().equals("AppEnquiry")) {
                        temp += "<i class=\"fa fa-building fa-2x text-theme text-green\"></i>";
//                        appTile += "<i class=\"fa fa fa-keyboard fa-3x text-theme text-green\"></i>";
                    } else if (moduleObj.getModule_code().equals("SitingInternal")) {
                        temp += "<i class=\"fa fa-map-marker fa-2x text-theme text-green\"></i>";
                    } else if (moduleObj.getModule_code().equals("SAM")) {
                        temp += "<i class=\"fa fa-user fa-2x text-theme text-green\"></i>";
                    } else if (moduleObj.getModule_code().equals("report")) {
                        temp += "<i class=\"fa fa-file-text fa-2x text-theme text-green\"></i>";
                    } else {
                        temp += "<i class=\"fa fa-gears fa-2x text-theme text-green\"></i>";
                    }
                }
                temp += "<h3 class=\"title-sm text-theme\">" + moduleObj.getModule_name() + "</h3><br>";
                temp += strTempTile;
                temp += "</div></div></div>";
//                appTile += "</td>";
//                appTile += "</tr>";
            }
            if (!temp.equals("")) {
                appTile = temp + appTile;
            }
            //Delvene @ 12-Feb-2014

//            super.setBreadMod(moduleObj);   //Added by Delvene @ 24-Dec-2013 :: To show breadcrumb module
        }

        return SUCCESS;
    }
    //Added by Delvene @ 10-Dec-2013 :: To support main screen navigation - END

    public void sortMenu() {
        for (int index = 0; index < moduleList.size(); index++) {
            Collections.sort((List) moduleList.get(index), new ModuleNameComparator());
        }
        Collections.sort((List) applicationList, new ApplicationNameComparator());

    }

    public String getSystemType_() {
        return systemType_;
    }

    public void setSystemType_(String systemType_) {
        this.systemType_ = systemType_;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getAppTile() {
        return appTile;
    }

    public void setAppTile(String appTile) {
        this.appTile = appTile;
    }

//    public List getApplicationList() {
//        ApplicationDAOImpl applicationDAO = new ApplicationDAOImpl();
//        List list = null;
//        try {
//            list = applicationDAO.list(new Application());
//        } finally {
//            applicationDAO.closeSession();
//        }
//        return list;
//    }
//
//    public void setApplicationList(List applicationList) {
//        this.applicationList = applicationList;
//    }
//    protected BaseDAO baseDAO = new BaseDAOImpl();
//    public void closeSession(){
//        baseDAO.closeSession();
//    }
//    public Session hibernateSession(){
//        return baseDAO.getSession();
//    }
    private Boolean debugUserSecurity = Boolean.FALSE;

    public Boolean getDebugUserSecurity() {
        return debugUserSecurity;
    }

    public void setDebugUserSecurity(Boolean debugUserSecurity) {
        this.debugUserSecurity = debugUserSecurity;
    }

    // ThoTH @ 1-Apr-2014 :: Encrypt Password :: called by Ajax
    private Object json_key;

    public Object getJson_key() {
        return json_key;
    }

    public void setJson_key(Object json_key) {
        this.json_key = json_key;
    }

    public String getItemId_() {
        return itemId_;
    }

    public void setItemId_(String itemId_) {
        this.itemId_ = itemId_;
    }

//    public String getEndSize_() {
//        return endSize_;
//    }
//
//    public void setEndSize_(String endSize_) {
//        this.endSize_ = endSize_;
//    }
//    public String getEndSizeESS_() {
//        return endSizeESS_;
//    }
//
//    public void setEndSizeESS_(String endSizeESS_) {
//        this.endSizeESS_ = endSizeESS_;
//    }
    public String getArchive_() {
        return archive_;
    }

    public void setArchive_(String archive_) {
        this.archive_ = archive_;
    }

    public String getVersion_() {
        return SystemConstants.DOMAIN.version;
    }

    public String obtainKeyData() {
        Map sessionMap = ActionContext.getContext().getSession();
//        if (sessionMap.get("crypto_salt") == null) {
        sessionMap.put("crypto_salt", CommonFunction.getHexId(64));
        sessionMap.put("crypto_iv", CommonFunction.getHexId(32));
//        }

        Map keymap = new HashMap();
        keymap.put("passPhrase", SystemConstants.Crypto.PassPhrase);
        keymap.put("keySize", SystemConstants.Crypto.KeySize);
        keymap.put("iterationCount", SystemConstants.Crypto.IterationCount);
        keymap.put("salt", sessionMap.get("crypto_salt"));
        keymap.put("iv", sessionMap.get("crypto_iv"));

//        Debug.printFrameworkDebug(sessionMap.get("crypto_salt") + " :: " + sessionMap.get("crypto_iv"));
        setJson_key(keymap);

        return "loadKeySuccess";

    }
    // ThoTH @ 1-Apr-2014 :: Encrypt Password :: called by Ajax <END>

    //thensw @ 505-2014 :: to flag user leaving the page...
    public String leavePage() throws Exception {
        return null;
    }

//    private List getNotificationList_ = null;
//    private Boolean checked_ = Boolean.FALSE;
    // ThoTH @ 14-Aug-2014 :: No more using :: Replace by RIGHT Panel
//    public List getNotificationList_() throws Exception {
//        if (checked_) {
//            return getNotificationList_;
//        }
//        checked_ = Boolean.TRUE;
//        List<Map> leaveApprovalList_ = null;
//        try {
//            leaveApprovalList_ = new CommonList(baseDAO.getSession()).populateLeaveApprovalList(null, Boolean.FALSE, Boolean.FALSE, (String) ActionContext.getContext().getSession().get("userId"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (leaveApprovalList_ != null && leaveApprovalList_.size() > 0) {
//            getNotificationList_ = new ArrayList();
//            if (leaveApprovalList_.size() == 1) {
//                getNotificationList_.add(new NotificationSetup("loadApprovalPageDetailEssLeave?transId_=" + leaveApprovalList_.get(0).get("lt_id"), getText("LEAVE.approval.xLeavePendingApproval", new String[]{"1"})));
//            } else {
//                getNotificationList_.add(new NotificationSetup("loadApprovalPageEssLeave", getText("LEAVE.approval.xLeavePendingApproval", new String[]{"" + leaveApprovalList_.size()})));
//            }
//        }
//        return getNotificationList_;
//    }
//    public class NotificationSetup {
//
//        private String url;
//        private String label;
//
//        public NotificationSetup(String url, String label) {
//            this.url = url;
//            this.label = label;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//
//        public String getLabel() {
//            return label;
//        }
//
//        public void setLabel(String label) {
//            this.label = label;
//        }
//
//    }
    // ThoTH @ 28-Aug-2014 :: Right Panel
    private List pendingSubmitList_;
    private Map jobMap_ = new HashMap();
    private Boolean showBackFromLeave_ = Boolean.FALSE;  // ThoTH @ 5-Jun-2015
    private Boolean outstandingReportDuty_ = Boolean.FALSE;  // ThoTH @ 18-Jun-2015
//    private Boolean showLeaveMsg_ = Boolean.FALSE;  // ThoTH @ 5-Jun-2015
    private String leaveMsg_ = "";  // ThoTH @ 5-Jun-2015

    public void prepareRightPanel() {
//        Debug.printFrameworkDebug("prepareRightPanel");

        try {
            //Debug.printFrameworkDebug("us id = "+ ActionContext.getContext().getSession().get("userId").toString());
            // Pending Submission List
//            RightPanelAction rightPanel = new RightPanelAction();
//            //pendingSubmitList_ = rightPanel.preparePendingSubmissionList(baseDAO.getSession(), strUsId);
//            // JOB
//            List<Map<String, Object>> jobList = new JobEngine().getJobPoolList(strUsId);
//            Integer intPool = 0;
//            Integer intHand = 0;
//            for (Map<String, Object> item : jobList) {
//                Object intObj = null;
//                intObj = item.get("longcount");
//                //cater for big integer - ahmadni @ 27-Jul-2016
//                if (intObj instanceof Integer) {
//                    intPool += (Integer) item.get("longcount");
//                } else if (intObj instanceof BigInteger) {
//                    intPool += ((BigInteger) item.get("longcount")).intValue();
//                }
//
//                intObj = item.get("ipcount");
//                if (intObj instanceof Integer) {
//                    intHand += (Integer) item.get("ipcount");
//                } else if (intObj instanceof BigInteger) {
//                    intHand += ((BigInteger) item.get("ipcount")).intValue();
//                }
//
//                //commented by ahmadni @ 27-Jul-2016
////                intPool += (Integer) item.get("longcount");
////                intHand += (Integer) item.get("ipcount");
//            }
            // ThoTH @ 5-Jun-2015 ::  Leave
            //rightPanel.prepareLeaveInfo(baseDAO.getSession());
//            showBackFromLeave_ = rightPanel.getShowBackFromLeave_();
            //outstandingReportDuty_ = rightPanel.getOutstandingReportDuty_();
            //Debug.printFrameworkDebug("outstandingReportDuty_" + outstandingReportDuty_);
//            showLeaveMsg_ = rightPanel.getOutstandingReportDuty_();
            // leaveMsg_ = rightPanel.getLeaveMsg_();
//            User user = (User)baseDAO.getObjectById((String)ActionContext.getContext().getSession().get("userId"), User.class);
//            String sql = "select count(*) from t_lm_trans trans inner join t_lm_trans_detail det on trans.lt_id = det.lt_id" +
//                        "   and trans.lt_app_status_leave = '"+LmTransModel.APP_STATUS.APPROVED+"'" +
//                        "   and ltd_report_duty_date_act is null" +
//                        "   and convert(date,ltd_report_duty_date_est) = convert(date,getdate())" +
//                        "   and trans.employee_id = '"+user.getEmp_id()+"'";
//            Long llCount = baseDAO.sqlCountRecord(sql);
//            if (llCount > 0 )  showBackFromLeave_ = Boolean.TRUE;
//            
//            sql = "select count(*) from t_lm_trans trans inner join t_lm_trans_detail det on trans.lt_id = det.lt_id" +
//                        "   and trans.lt_app_status_leave = '"+LmTransModel.APP_STATUS.APPROVED+"'" +
//                        "   and ltd_report_duty_date_act is null" +
//                        "   and convert(date,ltd_report_duty_date_est) < convert(date,getdate())" +
//                        "   and trans.employee_id = '"+user.getEmp_id()+"'";
//            llCount = baseDAO.sqlCountRecord(sql);
//            if (llCount > 0 )  {
//                showLeaveMsg_ = Boolean.TRUE;
//                leaveMsg_ = getText("LEAVE.backFromLeave.noReportDuty");
//            }
            // ThoTH @ 5-Jun-2015 ::  Leave <END>
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }

    }

    public List getPendingSubmitList_() {
        return pendingSubmitList_;
    }

//    public synchronized Map getJobMap_() {
//        if (jobMap_.isEmpty()) {
//            String strUsId = ActionContext.getContext().getSession().get("loginId").toString();
//            RouteUtil routeUtil = new RouteUtil();
//            String workflowGroups = routeUtil.getWorkflowGroup(baseDAO.getSession(), strUsId);
//
//            //-- get Task In Pool --//
//            Map taskInPoolMap = routeUtil.getTaskInPool("eQP", "", "role", workflowGroups, null);
//            List inPoolList = (List)taskInPoolMap.get("result");
//            jobMap_.put("longcount", inPoolList==null?"0":inPoolList.size()+"");
//
//            Map pendingTaskMap = routeUtil.getPendingTask("eQP", "doer", strUsId);
//            List pendingTaskList = (List)pendingTaskMap.get("result");
//            jobMap_.put("ipcount", pendingTaskList==null?"0":pendingTaskList.size()+"");
//        }
//        return jobMap_;
//    }

    // ThoTH @ 5-Jun-2015
    public Boolean getShowBackFromLeave_() {
        return showBackFromLeave_;
    }

    // ThoTH @ 18-Jun-2015
    public Boolean getOutstandingReportDuty_() {
        return outstandingReportDuty_;
    }

    // ThoTH @ 5-Jun-2015
//    public Boolean getShowLeaveMsg_() {
//        return showLeaveMsg_;
//    }
    // ThoTH @ 5-Jun-2015
    public String getLeaveMsg_() {
        return leaveMsg_;
    }

    //  ChangMH @ 30-Sep-2014 :: activation for PBT/BBN user
//    public String processCheckActivate() {
//        setStrTitle_(User.OPERATION.ACTIVATE_ACCOUNT);
//        Map sessionMap = ActionContext.getContext().getSession();
//        if (strCaptcha_.equals(sessionMap.get("sSysCaptcha"))) {
//
//            UserDAOImpl userDAO = new UserDAOImpl();
//            User user = null;
//            userDAO.setSession(baseDAO.getSession());
//
//            user = userDAO.getModelByCode("us_user_id", userId, new User());
////            Debug.printFrameworkDebug("user :"+user.getUs_id());
//            if (user != null) {
//                if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.ACTIVE)) {
////                    if (user.getEmployee().getEmp_type().equals(SystemConstants.EMP_TYPE.PAN)) {
////                        Debug.printFrameworkDebug("IC exists in PAN list");
////                        addActionError(getText("accountActivate.error.PANActive", new String[]{userId}));
////                    } else {
////                        Debug.printFrameworkDebug("IC exists in PBT/BBN list");
////                        addActionError(getText("accountActivate.error.PBTBBNActive"));
////                    }
//                } else {
//                    //Send activation email
//                    try {
//                        if (!Validator.isEmpty(user.getUs_email())) {
//                            String strEmail = getCensoredEmail(user.getUs_email());
//                            String strActivationCode = "";
//                            
//                            if(user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.INACTIVE)){
//                                String strCurrentDateTime = DateUtil.getCurrentTimestamp().toString();
//                                strActivationCode = getActivationCode(userId + strCurrentDateTime);
//                            }else if(user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.LOCKED)){
//                                strActivationCode = user.getUs_activation_code();
//                            }
//                            
//                            Map mailParam = new HashMap();
//                            mailParam.put(EmailTrigger.MAIL_TO, user.getUs_email());  // Compulsary
//                            mailParam.put("userName", user.getUs_user_name().toUpperCase());
//                            mailParam.put("userId", user.getUs_user_id());
//                            mailParam.put("encData", getActivationCode(userId));
//                            mailParam.put("activationCode", strActivationCode);
//                            mailParam.put("encOperation", getActivationCode(User.OPERATION.ACTIVATE_ACCOUNT));
//                            new EmailTrigger().sendEMail(mailParam, "AccountActivation");
//
//                            user.setUs_activation_code(strActivationCode);
//                            userDAO.updateActivationCode(user);
//
//                            addActionError(getText("accountActivate.success", new String[]{strEmail}));
//                        }
//                    } catch (Exception e) {
//                        addActionError(e.getMessage());
//                    }
//                }
//            } else {
//                addActionError(getText("accountActivate.error.IdNotExist"));
//            }
//            return "activate_success";
//        } else {
//            addActionError("Captcha tidak sepadan, sila cuba lagi");
//            return "activate_fail";
//        }
////        return "activate_success";
//    }
    public String getStrCaptcha_() {
        return strCaptcha_;
    }

    public void setStrCaptcha_(String strCaptcha_) {
        this.strCaptcha_ = strCaptcha_;
    }

//    public String sifbas() throws Exception {
//        Debug.printFrameworkDebug("--- login SIFBAS start time: " + DateUtil.getCurrentTimestamp());
//        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
//        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//        String keyId = null;
//        Debug.printFrameworkDebug("--- 111 : " + DateUtil.getCurrentTimestamp());
//        Map sessionMap = ActionContext.getContext().getSession();
//        if (sessionMap.get("logined") != null) {
//            if (((String) sessionMap.get("logined")).equals("true")) {
//                Debug.printFrameworkDebug("--- 222 : " + DateUtil.getCurrentTimestamp());
//                if (sessionMap.get("acting_user_id") == null) {
//                    BaseDAO dao_setSifbasSession = new BaseDAOImpl();
//                    try {
//                        dao_setSifbasSession.setSession(SessionFactoryImpl.getSession_sifbas_ih());
//                        Debug.printFrameworkDebug("--- 333 : " + DateUtil.getCurrentTimestamp());
//                        keyId = CommonFunction.getId(20);
//                        Debug.printFrameworkDebug("--- 444 : " + DateUtil.getCurrentTimestamp());
////                        conn = getSifbasSSO_connection();
//                        Debug.printFrameworkDebug("--- 555 : " + DateUtil.getCurrentTimestamp());
//                        //t_sys_ih_login_control (loginid, keyid, appid, remoteip, modified_date
//                        
//                        dao_setSifbasSession.beginBatchTransaction();
//                        Query query = dao_setSifbasSession.getSession().createSQLQuery("insert into t_sys_ih_login_control values (?, ?, ?, ?, getdate())");
//                        Debug.printFrameworkDebug("--- 666 : " + DateUtil.getCurrentTimestamp());
//                        query.setString(0, (String) sessionMap.get("loginId"));
//                        query.setString(1, keyId);
//                        query.setString(2, SystemConstants.DOMAIN.sso_sifbas_appid);
//                        query.setString(3, request.getRemoteAddr());
//                        Debug.printFrameworkDebug("--- 777(b4 execute) : " + DateUtil.getCurrentTimestamp());
//                        query.executeUpdate();
//                        dao_setSifbasSession.commitBatchTransaction();
//                        Debug.printFrameworkDebug("--- 888(after execute) : " + DateUtil.getCurrentTimestamp());
//                        Debug.printFrameworkDebug("--- 999(commit)  : " + DateUtil.getCurrentTimestamp());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        dao_setSifbasSession.rollbackBatchTransaction();
//                        response.getWriter().append("{\"status\":\"" + getText("rightPanel.login.SIFBAS.ssoFail") + "\"}");
//                        response.flushBuffer();
//                        return null;
//                    } finally {
//                        Debug.printFrameworkDebug("--- (b4 close conn) : " + DateUtil.getCurrentTimestamp());
//                        dao_setSifbasSession.closeSession();
//                        Debug.printFrameworkDebug("--- (after close conn) : " + DateUtil.getCurrentTimestamp());
//                    }
//                    Debug.printFrameworkDebug("--- 10 (b4 append url) : " + DateUtil.getCurrentTimestamp());
//                    response.getWriter().append("{\"status\":\"success\",\"url\":\"" + SystemConstants.DOMAIN.sso_sifbas_url + keyId + "\"}");
//                    Debug.printFrameworkDebug("--- 10 (after append url) : " + DateUtil.getCurrentTimestamp());
//                } else {
//                    response.getWriter().append("{\"status\":\"" + getText("rightPanel.login.SIFBAS.notAllow") + "\"}");
//                }
//                Debug.printFrameworkDebug("--- 11 (b4 flush) : " + DateUtil.getCurrentTimestamp());
//                response.flushBuffer();
//                Debug.printFrameworkDebug("--- 11 (after flush) : " + DateUtil.getCurrentTimestamp());
//                return null;
//            }
//        }
//        return null;
//    }
//    private Connection getSifbasSSO_connection() throws Exception {
//        Debug.printFrameworkDebug("------ 1. getSifbasConn : " + DateUtil.getCurrentTimestamp());
//        String url = "jdbc:sqlserver://" + SystemConstants.DOMAIN.sso_sifbas_server + ";databaseName=" + SystemConstants.DOMAIN.sso_sifbas_db;
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        Debug.printFrameworkDebug("------ 2. getSifbasConn : " + DateUtil.getCurrentTimestamp());        
//        Connection conn = DriverManager.getConnection(url, SystemConstants.DOMAIN.sso_sifbas_user, SessionFactoryImpl.str(SystemConstants.DOMAIN.sso_sifbas_pass));
//        Debug.printFrameworkDebug("------ 3. getSifbasConn : " + DateUtil.getCurrentTimestamp());
//        return conn;
//    }
    public String getActivationCode(String strActivationCode) {
        String temp = new CommonFunction().encData(strActivationCode);
        return temp;
    }

    private String decodeActicationCode(String strActivationCode) {
        return new CommonFunction().decryptData(strActivationCode);
    }

//    public String loadActivationLink() {
//        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//        String strUserId = request.getParameter("encData");
//        String strActivationCode = request.getParameter("activationCode");
//        String strOperation = request.getParameter("encOperation");
//        Debug.printFrameworkDebug("strOperation :"+decodeActicationCode(strOperation));
//        UserDAOImpl userDAO = new UserDAOImpl();
//        userDAO.setSession(baseDAO.getSession());
//
//        User activatingUser = userDAO.getModelByCode("us_user_id", decodeActicationCode(strUserId), new User());
//        if (activatingUser != null) {
//            if (activatingUser.getUs_activation_code().equals(strActivationCode)) {
//                java.sql.Timestamp timeNow = DateUtil.getCurrentTimestamp();
//                if (decodeActicationCode(strOperation).equals(User.OPERATION.ACTIVATE_ACCOUNT)) {
//                    setStrTitle_(User.OPERATION.ACTIVATE_ACCOUNT);
//                    if (activatingUser.getUs_status().equals(SystemConstants.USER_ACC_STATUS.NEW) || activatingUser.getUs_status().equals(SystemConstants.USER_ACC_STATUS.LOCKED)) {
//                        setUserId(activatingUser.getUs_user_id());
//                        return "change_password";
//                    } else {
//                        addActionError(getText("accountActivate.error.PBTBBNActive"));
//                        return init();
//                    }
//                }else if(decodeActicationCode(strOperation).equals(User.OPERATION.FORGOT_PASSWORD)){
//                    setStrTitle_(User.OPERATION.FORGOT_PASSWORD);
//                    if (activatingUser.getUs_status().equals(SystemConstants.USER_ACC_STATUS.ACTIVE)) {
//                        setUserId(activatingUser.getUs_user_id());
//                        return "change_password";
//                    } else if (activatingUser.getUs_status().equals(SystemConstants.USER_ACC_STATUS.LOCKED)) {
//                        addActionError(getText("account.error.locked"));
//                        return init();
//                    } else if (activatingUser.getUs_status().equals(SystemConstants.USER_ACC_STATUS.INACTIVE)) {
//                        addActionError(getText("account.error.inactive"));
//                        return init();
//                    } else {
//                        addActionError(getText("accountActivate.error.PBTBBNActive"));
//                        return init();
//                    }
//                }else{
//                    return init();
//                }
//            } else {
//                addActionError(getText("accountActivate.error.invalidLink"));
//                return init();
//            }
//        } else {
//            return "error";
//        }
//    }
    public String getCensoredEmail(String strEmail) {
        String temp = CommonFunction.censoredEmail(strEmail);
        return temp;
    }

    public String getConfirm_passwd_() {
        return confirm_passwd_;
    }

    public void setConfirm_passwd_(String confirm_passwd_) {
        this.confirm_passwd_ = confirm_passwd_;
    }

    public String processActivate() {

        UserDAOImpl userDAO = new UserDAOImpl();
        userDAO.setSession(baseDAO.getSession());
        Map sessionMap = ActionContext.getContext().getSession();
        String strTempPasswd = passwd;

        try {
            User user = null;
            AesUtil au = new AesUtil(SystemConstants.Crypto.KeySize, SystemConstants.Crypto.IterationCount);
            passwd = au.decrypt(sessionMap.get("crypto_salt").toString(), sessionMap.get("crypto_iv").toString(), SystemConstants.Crypto.PassPhrase, passwd);
            confirm_passwd_ = au.decrypt(sessionMap.get("crypto_salt").toString(), sessionMap.get("crypto_iv").toString(), SystemConstants.Crypto.PassPhrase, confirm_passwd_);

            if (!Validator.isEmpty(passwd) && !Validator.isEmpty(confirm_passwd_)) {
                if (passwd.equals(confirm_passwd_)) {
                    if (!passwd.equals(userId)) {
                        userDAO.activateUser(userId, passwd, SystemConstants.COMM_OPERATION.ACTIVATE_ACCOUNT);
                        passwd = strTempPasswd;
                        return processlogin();
                    } else {
                        addActionError(getText("accountActivate.errors.passwordSameAsUserID"));
                        return "change_password";
                    }
                } else {
                    addActionError(getText("accountActivate.error.comfirmPasswordX"));
                    return "change_password";
                }
            } else {
                String strRequiredFrield = "";
                if (Validator.isEmpty(au.decrypt(sessionMap.get("crypto_salt").toString(), sessionMap.get("crypto_iv").toString(), SystemConstants.Crypto.PassPhrase, passwd))) {
                    if (Validator.isEmpty(strRequiredFrield)) {
                        strRequiredFrield = getText("user.newPassword");
                    } else {
                        strRequiredFrield = strRequiredFrield + "," + getText("user.newPassword");
                    }
                }
                if (Validator.isEmpty(au.decrypt(sessionMap.get("crypto_salt").toString(), sessionMap.get("crypto_iv").toString(), SystemConstants.Crypto.PassPhrase, confirm_passwd_))) {
                    if (Validator.isEmpty(strRequiredFrield)) {
                        strRequiredFrield = getText("accountActivate.confirmNewPassword");
                    } else {
                        strRequiredFrield = strRequiredFrield + "," + getText("accountActivate.confirmNewPassword");
                    }
                }
                addActionError(getText("field.required", new String[]{strRequiredFrield}));
                return "change_password";
            }
        } catch (CustomBaseException cbe) {
            addActionError(cbe.getMessage());
            return init();
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
        }
        return init();
    }

    public String loadForgotPassword() {
        return "load_forgot_password";
    }

    //  ChangMH @ 08-Oct-2014 :: forgot password
    public String processCheckForgotPass() {
        setStrTitle_(SystemConstants.COMM_OPERATION.FORGOT_PASSWORD);
        Map sessionMap = ActionContext.getContext().getSession();
        if (strCaptcha_.equals(sessionMap.get("sSysCaptcha"))) {

            UserDAOImpl userDAO = new UserDAOImpl();
            User user = null;
            userDAO.setSession(baseDAO.getSession());

            user = userDAO.getModelByCode("us_user_id", userId, new User());

            if (user != null) {
                if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.ACTIVE)) {
//                    if (user.getEmployee().getEmp_type().equals(SystemConstants.EMP_TYPE.PAN)) {
//                        Debug.printFrameworkDebug("IC exists in PAN list");
//                        writeLogFile("FAIL: "+getText("accountActivate.error.PANActive", new String[]{userId}));
//                        addActionError(getText("accountActivate.error.PANActive", new String[]{userId}));
//                    } else {
                    //Send activation email
                    try {
                        if (!Validator.isEmpty(user.getUs_email())) {
                            String strEmail = getCensoredEmail(user.getUs_email());
                            String strActivationCode = "";

                            if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.INACTIVE)) {
                                String strCurrentDateTime = DateUtil.getCurrentTimestamp().toString();
                                strActivationCode = getActivationCode(userId + strCurrentDateTime);
                            } else if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.LOCKED)) {
                                strActivationCode = user.getUs_activation_code();
                            }

                            Map mailParam = new HashMap();
                            mailParam.put(EmailTrigger.MAIL_TO, user.getUs_email());  // Compulsary
                            mailParam.put("userName", user.getUs_user_name());
                            mailParam.put("userId", user.getUs_user_id());
                            mailParam.put("encData", getActivationCode(userId));
                            mailParam.put("activationCode", strActivationCode);
                            mailParam.put("encOperation", getActivationCode(SystemConstants.COMM_OPERATION.FORGOT_PASSWORD));
                            new EmailTrigger().sendEMail(mailParam, "ForgotPassword");

                            user.setUs_activation_code(strActivationCode);
                            userDAO.updateActivationCode(user);

                            writeLogFile("SUCCEED: " + getText("accountActivate.success", new String[]{strEmail}));
                            addActionError(getText("accountActivate.success", new String[]{strEmail}));
                        }
                    } catch (Exception e) {
                        writeLogFile("Failed: " + e.getMessage());
                        addActionError(e.getMessage());
                    }
                    //}
                } else if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.LOCKED)) {
                        writeLogFile("Failed: " + getText("account.error.locked"));
                        addActionError(getText("account.error.locked"));
                } else if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.INACTIVE)) {
                    writeLogFile("Failed: " + getText("account.error.inactive"));
                    addActionError(getText("account.error.inactive"));
                }
            } else {
                writeLogFile("Failed: " + getText("accountActivate.error.IdNotExist"));
                addActionError(getText("accountActivate.error.IdNotExist"));
            }
            return "activate_success";
        } else {
            writeLogFile("Failed: " + getText("account.error.captchaXmatch"));
            addActionError(getText("account.error.captchaXmatch"));
            return "check_forgot_password_fail";
        }
//        return "activate_success";
    }

    public String getStrTitle_() {
        return strTitle_;
    }

    public void setStrTitle_(String strTitle_) {
        this.strTitle_ = strTitle_;
    }

    private void writeLogFile(String data) {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        data = "IP Address='" + ipAddress + "' us_user_id='" + userId + "' " + data;

        CommonFunction.writeFile("requestForgotPassword", data);
    }

    private Boolean ihLogin = Boolean.FALSE;
    private String keyId = null;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
//    public String ih() throws Exception { //Integration Hub
//        Map sessionMap = ActionContext.getContext().getSession();
//        if (sessionMap.get("logined") != null) {
//            return init();
//        }
//        Connection conn = null;
//        try {
//            conn = getSifbasSSO_connection();
//            PreparedStatement statement = conn.prepareStatement("select loginid, appid from t_sys_ih_login_control where keyid = ?");
//            statement.setString(1, keyId);
//            ResultSet rs = statement.executeQuery();
//            while (rs!=null && rs.next()) {
//                userId = rs.getString("loginid");
//            }
//            ihLogin = Boolean.TRUE; 
//            return processlogin();
//        } catch (Exception e) {
//        } finally {
//            if (conn != null) {
//                conn.close();
//            }
//        }
//        return init();
//    }

    public String admin() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Map session = ActionContext.getContext().getSession();
        if (session.get("lock") == null) {
            String code = null;
            if (request.getParameter("lock") == null) {
                code = DateUtil.getCurrentTimestamp_nano().toString();
                code = code.substring(code.length() - 6);
                addActionError("Page Not Found... : ErrorCode (" + code + ")");
                session.put("dba", code);
                return "pageNotFound";
            } else { //checking for the lock
                code = (String) session.get("dba");
                if (code == null) {
                    code = DateUtil.getCurrentTimestamp_nano().toString();
                    code = code.substring(code.length() - 6);
                    addActionError("Page Not Found... : ErrorCode (" + code + ")");
                    session.put("dba", code);
                    return "pageNotFound";
                } else {
                    code.replaceFirst("^0+(?!$)", "");
                    if (!request.getParameter("lock").equals((9876543 - Integer.parseInt(code)) + "")) {
                        code = DateUtil.getCurrentTimestamp_nano().toString();
                        code = code.substring(code.length() - 6);
                        addActionError("Page Not Found... : ErrorCode (" + code + ")");
                        session.put("dba", code);
                        return "pageNotFound";
                    } else {
                        session.put("lock", DateUtil.getCurrentTimestamp());
                    }
                }
            }
        }
        Map sessionMap = ActionContext.getContext().getSession();

        if (sessionMap.get("loginId") != null && ((String) sessionMap.get("loginId")).equalsIgnoreCase("sainsadmin")) {
            if (Validator.isEmpty(userId)) {
                return "admin_page";
            }
            try {
                processlogout();
                ihLogin = Boolean.TRUE;
                passwd = "";
                return processlogin();
            } catch (Exception e) {
            }
        }
        return "success";
    }

    public String signup() {
//        populateDropdownData();
//        if (model.getUserCompanyList().size() == 0) {
//            model.getUserCompanyList().add(usercompanymodel);
//        }
        return "signup";
    }

    public void populateMenu() {
        User user = null;
        if (ActionContext.getContext().getSession().get("userId") instanceof Integer) {
            user = (User) baseDAO.getModelById((Integer) ActionContext.getContext().getSession().get("userId"), User.class);
        } else {
            user = (User) baseDAO.getModelById((String) ActionContext.getContext().getSession().get("userId"), User.class);
        }
        for (GroupUser gu : user.getGroupUserList()) {
//                    Debug.printFrameworkDebug("gu.getUserGroup().getUg_id()" + gu.getUserGroup().getUg_id() +"  " + gu.getUs_id());
            for (GroupApplication ga : gu.getUserGroup().getGroupApplication()) {
                if (ga.getApplication().getSystem_type().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)) {
                    if (!containsApplication(ga.getApplication(), applicationList)) {
                        applicationList.add(ga.getApplication());
                    }
                    // ThoTH @ 20-May-2013 -- END
//                            applicationList.add(ga.getApplication());
                } else { // ThoTH @ 29-Aug-2014 :: Split the list
//                    if (!containsApplication(ga.getApplication(), applicationList_ESS)) {
//                        applicationList_ESS.add(ga.getApplication());
//                    }
                }
            }
        }
        if (applicationList == null) {
            Debug.printFrameworkDebug("applicationList is null");
        }

        // ThoTH @ 5-Sept-2014 :: Solve the issue of Comparison method violates, when too many Order with same value exist
        int intApp = 0;
        while (intApp < applicationList.size()) {
            if (applicationList.get(intApp).getHidden().equals("Y")) {
                applicationList.remove(intApp);
            } else {
                intApp++;
            }
        }

        // ThoTH @ 20-May-2013
        // Default
        // ThenSW @ 09-Jul-2015 //somehow the sorting with just getShow_in_main_order will hit error, added getApplication_code solve the issue
        Collections.sort(applicationList, new CommonComparator(new String[]{"getShow_in_main_order", "getApplication_code"}));

        // Default
        for (Application app : applicationList) {
            Module module = app.getAttachedModule();
            getModules(module, moduleList);
        }
//                int level = 0;
        int intWrite = 0;  // ThoTH @ 21-Jan-2014
        String strTemp = ""; // ThoTH @ 21-Jan-2014 :: to hide module without application
        Application singleApp = null;  //Delvene @ 12-Feb-2014 :: To direct module to single application
        Boolean blnOpenTagAdded = Boolean.FALSE;  // ThoTH @ 29-Aug-2014
        if (moduleList.size() > 0) {
            sortMenu();
            List<Module> list = (List) moduleList.get(0);
            Collections.sort(list, new CommonComparator(new String[]{"getModule_order", "getModule_name"}));

            // moduleTile = "<tr>";    //Added by Delvene @ 10-Dec-2013 :: To display module tiles in main screen
//                    moduleTile = "<div style=\"position:relative; width='700px;\">";  //sereneC@ 22/8/2014 :: change td to div and add style. To auto drop second line if hv more than 5 system.
            moduleTile += "<div class='row'>";
            
            int i=0;
            for (Module moduleObj : list) {
                blnOpenTagAdded = Boolean.FALSE;
                intWrite = 0;
                strTemp = "";

                int loopModulesRtn = loopModules(1, moduleList, moduleObj, "    ", applicationList, SystemConstants.SYSTEM_TYPE.DEFAULT);
                if (loopModulesRtn > 0) {
                    blnOpenTagAdded = Boolean.TRUE;
                    intWrite++;
                } else if (loopModulesRtn == -100) {
                    continue;
                }
                for (Application app : applicationList) {
                    if (app.getHidden().equals("Y")) {
                        continue;
                    }
                    if (app.getAttached_module_id().equals(moduleObj.getModule_id())) {
                        intWrite++;
                        strTemp += "    <li><a href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "'>" + StringEscapeUtils.escapeHtml4(app.getApplication_name()) + "</a></li>";
                        singleApp = app;    //Delvene @ 12-Feb-2014
                    }
                }
                if (intWrite > 0) { // ThoTH @ 21-Jan-2014
                    if (!blnOpenTagAdded) {
                        menuList += "<li>" +
                                            "   <a class='accordion-heading' data-toggle='collapse' data-target='#"+moduleObj.getID()+"'>" +
                                            "      <span class='nav-header-primary'>"+moduleObj.getModule_name()+"<span class='pull-right'><b class='caret'></b></span></span>" +
                                            "   </a>" +
                                            "      <ul class='nav-list collapse' id='"+moduleObj.getID()+"'>";
                    }
                    menuList += strTemp;
                    menuList += "  </ul></li>";//sereneC @ 27/7/2014 ::add </div> to close <div class='sf-mega'>
                }
                
                
                if(!moduleObj.getModule_name().contains("QP")){ //temp hide the QP from internal view only 15-08-2018
                    moduleTile += populateModuleTile(moduleObj, intWrite, singleApp, SystemConstants.SYSTEM_TYPE.DEFAULT);
                    i++;
                }
                if((i%4)==0)
                {
                   moduleTile += "</div><div class='row'>";
                }
                
                singleApp = null;   // Added by Delvene @ 11-Dec-2014 :: Reset back to null incase all applications are tied to Sub-Module and NONE in Main Module
            }
            moduleTile += "</div>";
            // moduleTile += "</tr>";  //Added by Delvene @ 10-Dec-2013 :: To display module tiles in main screen
//                    moduleTile += "</div>";// sereneChye@ 22/8/2014 :: To wrap module tiles in main screen.
            ActionContext.getContext().getSession().remove("menuList");
            ActionContext.getContext().getSession().put("menuList", menuList);

            //Added by Delvene @ 10-Dec-2013 :: To display module tiles in main screen
            ActionContext.getContext().getSession().remove("moduleTile");
            ActionContext.getContext().getSession().put("moduleTile", moduleTile);
            //Added by Delvene @ 10-Dec-2013 :: To display module tiles in main screen - END
        }

        myProfileList += "<br/>";

        //Added by ChangMH @ 04-Feb-2015 :: As per kho request - Hide the JobDoer Group
        //get the USER_GROUP_HIDE from SystemParamenterModel
        ParameterModel paramModel = (ParameterModel) baseDAO.getSession().getNamedQuery("Parameter.getByParameter_code")
                        .setParameter("system_code", (Validator.isEmpty(systemType_)?"DEF":systemType_))
                        .setParameter("parameter_code", SystemConstants.PARAM_NAME.USER_GROUP_HIDE)
                        .uniqueResult();

        if (user.getGroupUserList().size() > 0) {
            myProfileList += "<div style='padding:10px 15px;'><b>" + getText("user.group") + "</b>";
//                    myProfileList += "<ul class=\"ul_image\">";
            for (GroupUser userGroup : user.getGroupUserList()) {
                if (paramModel.getParameter_value().indexOf('*' + userGroup.getUserGroup().getGroup_code() + '*') < 0) {
                    myProfileList += "<p>" + userGroup.getUserGroup().getGroup_name() + "</p>";
                }
            }
//                    myProfileList += "<br style=\"clear: left\" /></ul>";
//                    myProfileList += "<br />";
            myProfileList += "</div>";
        }

        ActionContext.getContext().getSession().remove("myProfileList");
        ActionContext.getContext().getSession().put("myProfileList", myProfileList);
    }

    public String getUserIcNo() {
        return userIcNo;
    }

    public void setUserIcNo(String userIcNo) {
        this.userIcNo = userIcNo;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    //activity log
    private String timeLoggedIn = "";
    private String timeLoggedOut = "";
    private String logonDuration = "";

    public String getTimeLoggedIn() {
        return timeLoggedIn;
    }

    public void setTimeLoggedIn(String timeLoggedIn) {
        this.timeLoggedIn = timeLoggedIn;
    }

    public String getTimeLoggedOut() {
        return timeLoggedOut;
    }

    public void setTimeLoggedOut(String timeLoggedOut) {
        this.timeLoggedOut = timeLoggedOut;
    }

    public String getLogonDuration() {
        return logonDuration;
    }

    public void setLogonDuration(String logonDuration) {
        this.logonDuration = logonDuration;
    }

    public String template() {
        return "loadTemplate";
    }
    
    public String msb = "S"; //S = show
    public String getMsb() {
        Map sessionMap = ActionContext.getContext().getSession();
        if (sessionMap.get("msb") != null) {
            return (String)sessionMap.get("msb");
        } else {
            return "S"; //
        }
    }
    public void setMsb(String menuSlideBar) {
        this.msb = menuSlideBar;
        Map sessionMap = ActionContext.getContext().getSession();
        sessionMap.put("msb", msb);
    }
    public void menuSideBar() throws Exception {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.getWriter().append("Expired");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.flushBuffer();
    }
    
    //** for user preference :: START **//
    private List<ParameterModel> modelList = null;
    public List<ParameterModel> getModelList() {
        return modelList;
    }
    public void setModelList(List<ParameterModel> modelList) {
        this.modelList = modelList;
    }
    
    private String pageRequired_ = null;
    public String getPageRequired_() {
        return pageRequired_;
    }
    public void setPageRequired_(String pageRequired_) {
        this.pageRequired_ = pageRequired_;
    }
    private final String User_Preference_Code = "userPref";
    
    private List modelGroupList = null;
    public List getModelGroupList() {
        if (modelGroupList == null) modelGroupList = new ArrayList();
        return modelGroupList;
    }
    public void setModelGroupList(List modelGroupList) {
        this.modelGroupList = modelGroupList;
    }
    
    private void loadGlobalPreference() {
        ParameterAction paramAction = new ParameterAction(baseDAO.getSession());
        paramAction.setId(User_Preference_Code);
        paramAction.loadEditPage();
        modelList = paramAction.getModelList();
        modelGroupList = paramAction.getModelGroupList();
        pageRequired_ = paramAction.getPageRequired_();
    }
    public String loadPreference() {
        try {
            Debug.printFrameworkDebug("111");
            loadGlobalPreference();
            Map sessionMap = ActionContext.getContext().getSession();
            User user = null;
            Debug.printFrameworkDebug("222");
            if (ActionContext.getContext().getSession().get("userId") instanceof Integer) {
                user = (User) baseDAO.getModelById((Integer) sessionMap.get("userId"), User.class);
            } else {
                user = (User) baseDAO.getModelById((String) sessionMap.get("userId"), User.class);
            }
            Debug.printFrameworkDebug("333");
            for (ParameterModel param : modelList) {
                if (param.getField_indicator().equals("input")) {
                    for (UserPreferenceModel userPref : user.getUserPreferenceList()) {
                        if (userPref.getPref_code().equals(param.getParameter_code())) {
                            param.setParameter_value(userPref.getPref_value());
                            break;
                        }
                    }
                }
            }
            Debug.printFrameworkDebug("444");
            setDefinedBreadCrumb("<li><a href='internal'>Home</a></li><li><a href='loadPreference'>"+getText("user.myPreference")+"</a></li>");
            Debug.printFrameworkDebug("555");
        } catch (Exception e) {
            e.printStackTrace();
            new LogFunction().logError(this.getClass(), "", e);
            return SUCCESS;
        }
        return SUCCESS;
    }
    
    public String updatePreference() {
        Map sessionMap = ActionContext.getContext().getSession();
        User user = null;
        if (ActionContext.getContext().getSession().get("userId") instanceof Integer) {
            user = (User) baseDAO.getModelById((Integer) sessionMap.get("userId"), User.class);
        } else {
            user = (User) baseDAO.getModelById((String) sessionMap.get("userId"), User.class);
        }
        List<UserPreferenceModel> updatePrefList = null;
        for (ParameterModel param : modelList) {
            if (param.getField_indicator().equals("input")) {
                for (UserPreferenceModel userPref : user.getUserPreferenceList()) {
                    if (userPref.getPref_code().equals(param.getParameter_code())) {
                        if (userPref.getPref_value() == null || !userPref.getPref_value().equals(param.getParameter_value())) {
                            if (updatePrefList == null) updatePrefList = new ArrayList();
                            userPref.setPref_value(param.getParameter_value());
                            updatePrefList.add(userPref);
                            break;
                        }
                    }
                }
            }
        }
        if (updatePrefList != null) {
            BaseDAO updatePrefDAO = new BaseDAOImpl();
            try {
                updatePrefDAO.beginBatchTransaction();
                for (UserPreferenceModel userPref : updatePrefList) {
                    updatePrefDAO.updateWithSession(updatePrefDAO.getSession(), userPref);
                }
                updatePrefDAO.commitBatchTransaction();
            } catch (Exception e) {
                updatePrefDAO.rollbackBatchTransaction();
            } finally {
                updatePrefDAO.closeSession();
            }
        }
        return loadPreference();
    }
    
    //** for user preference :: END **//
    
    //** for maintenance mode :: START **//
    public final static String MaintenanceDatetimeFormat = "yyyyMMddHHmm"; //in 
    private String maintenanceStartTime; //in yyyyMMddhhmm
    private String maintenanceEndTime; //in yyyyMMddhhmm
    private String allowedIp ; //, seperated
    private String maintenanceRemark ; //;; seperated

    public String getMaintenanceStartTime() {
        return maintenanceStartTime;
    }
    public void setMaintenanceStartTime(String maintenanceStartTime) {
        this.maintenanceStartTime = maintenanceStartTime;
    }

    public String getMaintenanceEndTime() {
        return maintenanceEndTime;
    }
    public void setMaintenanceEndTime(String maintenanceEndTime) {
        this.maintenanceEndTime = maintenanceEndTime;
    }

    public List getAllowIpList_AI() {
        return AuthorizationInterceptor.allowedIp;
    }
    
    private String myCurrentIp = null;
    public String getMyCurrentIp() {
        return myCurrentIp;
    }
    public String getAllowedIp_AI() {
        String temp = null;
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        myCurrentIp = CommonFunction.getUserIp(request);
        if (AuthorizationInterceptor.allowedIp != null) {
            if (!AuthorizationInterceptor.allowedIp.contains(myCurrentIp)) {
                AuthorizationInterceptor.allowedIp.add(0, myCurrentIp);
            }
            for (String ip : AuthorizationInterceptor.allowedIp) {
                if (temp == null) {
                    temp = ip;
                } else {
                    temp+=";;"+ip;
                }
            }
        } else {
            AuthorizationInterceptor.allowedIp.add(myCurrentIp);
            temp = myCurrentIp;
        }

        return temp;
    }
    public String getAllowedIp() {
        return allowedIp;
    }
    public void setAllowedIp(String allowedIp) {
        this.allowedIp = allowedIp;
    }

    public String getMaintenanceRemark_AI() {
        String temp = null;
        if (AuthorizationInterceptor.maintenanceRemark != null) {
            for (String remark : AuthorizationInterceptor.maintenanceRemark) {
                if (temp == null) {
                    temp = remark;
                } else {
                    temp+=";;"+remark;
                }
            }
        }
        return temp;
    }
    public String getMaintenanceRemark() {
        return maintenanceRemark;
    }
    public void setMaintenanceRemark(String maintenanceRemark) {
        this.maintenanceRemark = maintenanceRemark;
    }
    
    public String configureMaintenanceMode() {
        Debug.printDebug("AuthorizationInterceptor.maintenanceMode = " + AuthorizationInterceptor.maintenanceMode);
        Map sessionMap = ActionContext.getContext().getSession();
        if (sessionMap.containsKey("logined") && ((String)sessionMap.get("loginId")).equalsIgnoreCase("admin")) {
            try {
                if (AuthorizationInterceptor.allowedIp == null) {
                    AuthorizationInterceptor.allowedIp = new ArrayList();
                }

                if (AuthorizationInterceptor.maintenanceRemark == null) {
                    AuthorizationInterceptor.maintenanceRemark = new ArrayList();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "configureMaintenanceMode";
        }
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        request.setAttribute("not_authorised", "You are not allow to Configure Maintenance Mode");
        return "not_authorised";
    }
    public String startMaintenanceMode() {
        Map sessionMap = ActionContext.getContext().getSession();
        if (sessionMap.containsKey("logined") && ((String)sessionMap.get("loginId")).equalsIgnoreCase("admin")) {
            try {
                AuthorizationInterceptor.maintenanceStart = DateUtil.getCalendar(DateUtil.getDate(maintenanceStartTime, MaintenanceDatetimeFormat));
                AuthorizationInterceptor.maintenanceEnd = DateUtil.getCalendar(DateUtil.getDate(maintenanceEndTime, MaintenanceDatetimeFormat));
                sessionMap.put("maintenanceStart_time", Formatter.formatDate(maintenanceStart.getTime(), "dd MMM yyyy HH:mm:ss"));
                sessionMap.put("maintenanceEnd_time", Formatter.formatDate(maintenanceEnd.getTime(), "dd MMM yyyy HH:mm:ss"));
                if (AuthorizationInterceptor.allowedIp == null) {
                    AuthorizationInterceptor.allowedIp = new ArrayList();
                }
                AuthorizationInterceptor.allowedIp.clear();
                for (String theAllowedIp : allowedIp.split(";;")) {
                    AuthorizationInterceptor.allowedIp.add(theAllowedIp.trim());
                }
                
                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                myCurrentIp = CommonFunction.getUserIp(request);
                if (!AuthorizationInterceptor.allowedIp.contains(myCurrentIp)) {
                    AuthorizationInterceptor.allowedIp.add(myCurrentIp);
                }
                
                if (!Validator.isEmpty(maintenanceRemark)) {
                    if (AuthorizationInterceptor.maintenanceRemark == null) {
                        AuthorizationInterceptor.maintenanceRemark = new ArrayList();
                    }
                    AuthorizationInterceptor.maintenanceRemark.clear();
                    for (String theMsg : maintenanceRemark.split(";;")) {
                        AuthorizationInterceptor.maintenanceRemark.add(theMsg.trim());
                    }
                }
                sessionMap.put("maintenanceRemark", AuthorizationInterceptor.maintenanceRemark);
                AuthorizationInterceptor.maintenanceMode = Boolean.TRUE;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "configureMaintenanceMode";
        }
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        request.setAttribute("not_authorised", "You are not allow to start Maintenance Mode");
        return "not_authorised";
    }
    public String stopMaintenanceMode() {
        if (AuthorizationInterceptor.maintenanceMode) {
            Map sessionMap = ActionContext.getContext().getSession();
            if (sessionMap.containsKey("logined") && ((String)sessionMap.get("loginId")).equalsIgnoreCase("admin")) {
                AuthorizationInterceptor.maintenanceMode = Boolean.FALSE;
                return "configureMaintenanceMode";
            } else {
                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                request.setAttribute("not_authorised", "You are not allow to stop Maintenance Mode");
                return "not_authorised";
            }
        } else {
            return init();
        }
    }
    public Boolean getMaintenanceMode() {
        return AuthorizationInterceptor.maintenanceMode;
    }
    public String error404() {
        return "error404";
    }
    public String error403() {
        return "error403";
    }
    public String error500() {
        return "error500";
    }
    //** for maintenance mode :: END **//
    //** for debug switch :: START **//
    public Integer switchLevel = null;
    public String switchPrintFrameworkInfo = null;
    public String switchPrintFrameworkDebug = null;

    public Integer getSwitchLevel() {
        return switchLevel;
    }

    public void setSwitchLevel(Integer switchLevel) {
        this.switchLevel = switchLevel;
    }

    public String getSwitchPrintFrameworkInfo() {
        return switchPrintFrameworkInfo;
    }

    public void setSwitchPrintFrameworkInfo(String switchPrintFrameworkInfo) {
        this.switchPrintFrameworkInfo = switchPrintFrameworkInfo;
    }

    public String getSwitchPrintFrameworkDebug() {
        return switchPrintFrameworkDebug;
    }

    public void setSwitchPrintFrameworkDebug(String switchPrintFrameworkDebug) {
        this.switchPrintFrameworkDebug = switchPrintFrameworkDebug;
    }
    
    public String switchDebug() {
        Map sessionMap = ActionContext.getContext().getSession();
        if (sessionMap.get("logined") != null) {
            if (((String) sessionMap.get("logined")).equals("true")) {
                User user = null;
                if (ActionContext.getContext().getSession().get("userId") instanceof Integer) {
                    user = (User) baseDAO.getModelById((Integer) ActionContext.getContext().getSession().get("userId"), User.class);
                } else {
                    user = (User) baseDAO.getModelById((String) ActionContext.getContext().getSession().get("userId"), User.class);
                }
                if (user.getGroupUserList().stream()
                        .filter(x -> x.getUserGroup().getGroup_code().equals("AdminGroup"))
                        .count() > 0) {
                    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                    if (switchLevel != null) {
                        Debug.debug_level = switchLevel;
                    }
                    if (switchPrintFrameworkInfo != null) {
                        Debug.printFrameworkInfo = switchPrintFrameworkInfo.equalsIgnoreCase("true");
                    }
                    if (switchPrintFrameworkDebug != null) {
                        Debug.printFrameworkDebug = switchPrintFrameworkDebug.equalsIgnoreCase("true");
                    }
                } else {
                    addActionMessage("common.debug.cannotChangeSwitch");
                }
            }
        }
        return init();
    }
    
    public String switchLog() {
        Map sessionMap = ActionContext.getContext().getSession();
        if (sessionMap.get("logined") != null) {
            if (((String) sessionMap.get("logined")).equals("true")) {
                User user = null;
                if (ActionContext.getContext().getSession().get("userId") instanceof Integer) {
                    user = (User) baseDAO.getModelById((Integer) ActionContext.getContext().getSession().get("userId"), User.class);
                } else {
                    user = (User) baseDAO.getModelById((String) ActionContext.getContext().getSession().get("userId"), User.class);
                }
                if (user.getGroupUserList().stream()
                        .filter(x -> x.getUserGroup().getGroup_code().equals("AdminGroup"))
                        .count() > 0) {
                    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                    if (request.getParameter("TraceModelOperation") != null) {
                        ModelBase.TraceModelOperation = request.getParameter("TraceModelOperation").equalsIgnoreCase("true");
                    }
                    if (request.getParameter("addTraceOperation") != null) {
                        List<String> stringList = (List)ModelBase.currentRequestObject("tracingOperation");
                        if (stringList == null) {
                            stringList = new ArrayList();
                            ModelBase.updateCurrentRequestObject("tracingOperation", stringList);
                        }
                        stringList.add(request.getParameter("addTraceOperation"));
                    }
                    if (request.getParameter("removeTraceOperation") != null) {
                        List<String> stringList = (List)ModelBase.currentRequestObject("tracingOperation");
                        if (stringList == null) {
                            stringList = new ArrayList();
                            ModelBase.updateCurrentRequestObject("tracingOperation", stringList);
                        }
                        stringList.remove(request.getParameter("removeTraceOperation"));
                    }
                } else {
                    addActionMessage("common.debug.cannotChangeSwitch");
                }
            }
        }
        return init();
    }
    //** for debug switch :: END **//
    
    // For Route Workflow : Start //
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
//        Debug.printFrameworkDebug("To get user infor for wfGroupId=" + wfGroupId + ", userIdInGroup=" + userIdInGroup); 
        UserGroupDAOImpl userGroupDAO = new UserGroupDAOImpl();
        Map jsonMap = userGroupDAO.getGroupInfo(wfGroupId, userIdInGroup, baseDAO.getSession());
//        Debug.printFrameworkDebug("getGroupInfo() " + jsonMap);
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
        Debug.printFrameworkDebug("Calling get infom");
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Map jsonMap = new HashMap();
        if (dataName == null) {
            jsonMap.put("status", "fail");
            jsonMap.put("errMsg", "dataName is required");
        }else if (dataName.equals("rrUser")) {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            String strUserGroup = request.getParameter("rrUserGroup");    
            if(Validator.isEmpty(strUserGroup)){
                jsonMap.put("status", "fail");
                jsonMap.put("errMsg", "rrUserGroup is required");
            }else{
//                Debug.printFrameworkDebug("getInfo Round Robin");
//                UserGroupDAOImpl userGroupDAO = new UserGroupDAOImpl();
//                jsonMap = userGroupDAO.getRRGroupUserInfo(strUserGroup, baseDAO.getSession());
//                Debug.printFrameworkDebug("getInfo Return ");
//                Debug.printFrameworkDebug(jsonMap);
            }
        }else {
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
//                Debug.printFrameworkDebug("request.getParameter(\"activityDuration\") = " + request.getParameter("activityDuration"));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                jsonMap.put("dueDate", formatter.format(new CommonFunction().getJobNextWorkingDay(baseDAO.getSession(), 
                        DateUtil.getTimestampFromDate(formatter.parse(request.getParameter("startDate"))), 
                        request.getParameter("activityDuration"))));
            } else {
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
    // For Route Workflow : End //
    // For SwkID Integration : START //
    public String ssoTrigger() {
        Debug.printDebug("ssoTrigger");
        Map sessionMap = ActionContext.getContext().getSession();
        if (sessionMap.get("sSysUserObjId") == null) {
            return "loadLoginPage";
        }
        return SUCCESS;
    }

    public String fim2Verify() throws Exception {
        Map map = Fim2Api.verifyFimLogin((HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST), Boolean.TRUE);
        if (map.get("status").equals("success")) {
            setUserId((String)map.get("fimLoginUserId"));
            return processlogin();
        }
        return "welcome_fim2";
    }
    
    public String ssoVerify() throws Exception {
        SessionMap sessionMap = (SessionMap)ActionContext.getContext().getSession();
        if (sessionMap.containsKey("logined")) {
            return SUCCESS;
        }
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String strOAuthCode = request.getParameter("code");
        String strOAuthStateCode = request.getParameter("state");
        String strOAuthAccessToken = "";
        String strStateCode = (sessionMap.get("sesStateCode") == null ? "" : sessionMap.get("sesStateCode").toString());
        if (!Validator.isEmpty(strOAuthStateCode) && strOAuthStateCode.equals(strStateCode)) {
            try {
                SwkIdApi swkIdApi = new SwkIdApi();
                strOAuthAccessToken = swkIdApi.token_exchange(strOAuthStateCode, strOAuthCode);
                new LogFunction().logInfo(this.getClass(), "access token: " + strOAuthAccessToken);

                if (!Validator.isEmpty(strOAuthAccessToken)) { // sarawak id user verified
                    sessionMap.put("sesAccessToken", strOAuthAccessToken);

                    // get user info from sarawak id
                    Map usr_info = swkIdApi.token_user(strOAuthAccessToken);
                    if (usr_info.containsKey("usr_ldap_id")) { // SwkID-PK
                        sessionMap.put("sSysssoUserObjId", usr_info.get("usr_ldap_id"));
                        if (usr_info.get("login_by") != null && usr_info.get("login_by").equals("sarawaknet")) {
//                            // sarawaknet id
                            sessionMap.put("sSysUserObjId", usr_info.get("usr_ldap_sarawaknet_id"));
                        } else {
//                            // sarawakid
                            sessionMap.put("sSysUserObjId", usr_info.get("usr_short_name"));
                        }
                        userId = (String) usr_info.get("usr_ldap_id");
                        User swkId_user = (User) baseDAO.getSession().getNamedQuery("User.findBySsoUid").setParameter("sso_uid", userId).uniqueResult();
                        if (swkId_user == null) { //not matching swk_id user found
                            sessionMap.put("map_sso_uid", "map_sso_uid"); //to mark this login need to do mapping with SSO ID
                            addActionMessage(getText("errors.needToMapSwkId"));
                            return "map_sso_uid";
                        }
                        return processlogin();
                    }

                }
            } catch (CustomBaseException e) {
                addActionError(e.getMessage());
                return "loadLoginPage";
            }
        }
        return "loadLoginPage";
    }
    // For SwkID Integration : END //
    
    // For Change Locale : start //
    public String changeLocale() {
//        language
        Map session = ActionContext.getContext().getSession();
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String defaultLanguage = request.getParameter("language");
        if (!Validator.isEmpty(defaultLanguage)) {
            String multilingualList = getText("system.multilingualList");
            if (!Validator.isEmpty(multilingualList)) {
                List languageList = new ArrayList();
                for (String language : multilingualList.split(",")) {
                    String languageCode = language.substring(0, language.indexOf(";"));
                    if (languageCode.equals(defaultLanguage)) {
                        session.put("defaultLanguage", language);
                        continue;
                    }
                    languageList.add(language);
                }
                session.put("multilingualDD", languageList);
            }
    //            String languageCode = defaultLanguage.substring(0, defaultLanguage.indexOf(";"));
            Locale locale = new Locale(defaultLanguage, defaultLanguage);
            ActionContext.getContext().setLocale(locale);
            session.put(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, locale);
            populateMenu2(null);
        }
        return init();
    }
    // For Change Locale : END //
    
    public void populateMenu2(User user) {
        BaseDAO<Application>applicationDAO = new BaseDAOImpl(); //joveni @ 21/2/2024 :: resolve session not close
        
        try {
            if (user == null) {
                if (ActionContext.getContext().getSession().get("userId") instanceof Integer) {
                    user = (User) baseDAO.getModelById((Integer) ActionContext.getContext().getSession().get("userId"), User.class);
                } else {
                    user = (User) baseDAO.getModelById((String) ActionContext.getContext().getSession().get("userId"), User.class);
                }
            }
            if (user == null) {
            } else {
                Map paramMap = new HashMap();
                List<SetupGroup> sgList = new ArrayList();
                paramMap.put("SYSTEM_ID", SystemConstants.SYSTEM_ID.USJ);
                sgList = baseDAO.list(paramMap, SetupGroup.class);
                List lxgGroupList = new ArrayList();
                for(SetupGroup sg : sgList){
                    lxgGroupList.add(sg.getUg_id());
                }
            
                for (GroupUser gu : user.getGroupUserList()) {
                    if(lxgGroupList.contains(gu.getUg_id())){ //only add application  assigned to UTiMAPS
                        divList.add(gu.getUserGroup().getGroup_div());
                        
                        for (GroupApplication ga : gu.getUserGroup().getGroupApplication()) {
                            if (ga.getApplication().getSystem_type().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)) {
                              if (!containsApplication(ga.getApplication(), applicationList)) {
                                    applicationList.add(ga.getApplication());
                                }
                            } else { // ThoTH @ 29-Aug-2014 :: Split the list
                            }
                        }
                    }
                }
            }
            
            if (applicationList == null) {
                Debug.printFrameworkDebug("applicationList is null");
            }
            
            if (applicationList.isEmpty()) { //serene @ 18/8/2021 from public SSO which get data from elasis
                Debug.printFrameworkDebug("applicationList is null");
                Debug.printInfo("load Public List");
                Map pMap = new HashMap();
                pMap.put("system_type", SystemConstants.SYSTEM_TYPE.PUBLIC);
                List<Application> appPbList = applicationDAO.list(pMap, Application.class);
                applicationList = appPbList;
            }
            
            // ThoTH @ 5-Sept-2014 :: Solve the issue of Comparison method violates, when too many Order with same value exist
            int intApp = 0;
            while (intApp < applicationList.size()) {
                if (applicationList.get(intApp).getHidden().equals("Y")) {
                    applicationList.remove(intApp);
                } else {
                    intApp++;
                }
            }

            // ThoTH @ 20-May-2013
            // Default
            // ThenSW @ 09-Jul-2015 //somehow the sorting with just getShow_in_main_order will hit error, added getApplication_code solve the issue
            Collections.sort(applicationList, new CommonComparator(new String[]{"getShow_in_main_order", "getApplication_code"}));

            // Default
            for (Application app : applicationList) {
                Module module = app.getAttachedModule();
                getModules(module, moduleList);
            }
    //                int level = 0;
            int intWrite = 0;  // ThoTH @ 21-Jan-2014
            String strTemp = ""; // ThoTH @ 21-Jan-2014 :: to hide module without application
            Application singleApp = null;  //Delvene @ 12-Feb-2014 :: To direct module to single application
            Boolean blnOpenTagAdded = Boolean.FALSE;  // ThoTH @ 29-Aug-2014

            if (moduleList.size() > 0) {
                sortMenu();
                List<Module> list = (List) moduleList.get(0);
                Collections.sort(list, new CommonComparator(new String[]{"getModule_order", "getModule_name"}));
                moduleTile += "<div class='row'>";
                int i=0;
                for (Module moduleObj : list) {
                    blnOpenTagAdded = Boolean.FALSE;
                    Boolean blnHasSubMenu = Boolean.FALSE;
                    intWrite = 0;
                    strTemp = "";

                    String rtnMenu = loopModules2(1, moduleList, moduleObj, "    ", applicationList, SystemConstants.SYSTEM_TYPE.DEFAULT);
                    int loopModulesRtn = rtnMenu.length();
                    if (loopModulesRtn > 0) {
                        if (rtnMenu.startsWith("only1app")) {
                            menuList += rtnMenu.substring(8);
                            continue;
                        }
                        blnOpenTagAdded = Boolean.TRUE;
                        blnHasSubMenu = Boolean.TRUE;
                        intWrite++;
                    }
                    for (Application app : applicationList) {
                        if (app.getHidden().equals("Y")) {
                            continue;
                        }
                        if (app.getAttached_module_id().equals(moduleObj.getModule_id())) {
                            intWrite++;
                            if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_3)) {
                                strTemp += "    <li><a href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "'>"+ getIconString(app.getApp_icon()) + sanitiseName(app.getApplication_name_code(), app.getApplication_name()) + "&nbsp;&nbsp;&nbsp;&nbsp;</a></li>";
                            } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_4)) {
                                strTemp += "    <li class='kt-menu__item ' aria-haspopup='true'><a href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "' class='kt-menu__link '><span class='kt-menu__link-text'>" + getIconString(app.getApp_icon()) + sanitiseName(app.getApplication_name_code(), app.getApplication_name()) + "</span></a></li>\r\n";
                            } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_5)) {
                                strTemp += "    <li class='nav-item'><a class='nav-link' href='" + StringEscapeUtils.escapeEcmaScript(app.getAction_name()) + "'><div class=\"d-flex align-items-center\">"+ getIconString(app.getApp_icon()) + "<span class=\"nav-link-text ps-1\">" + sanitiseName(app.getApplication_name_code(), app.getApplication_name()) + "</span></div></a></li>";
                            }
                            singleApp = app;    //Delvene @ 12-Feb-2014
                        }
                    }
                    if (intWrite > 0) { // ThoTH @ 21-Jan-2014
                            if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_3)) {
                                menuList += "<li>" +
                                    "   <a class='accordion-heading' data-toggle='collapse' data-target='#"+moduleObj.getID()+"'>" +
                                    "      <span class='nav-header-primary'>"+getIconString(moduleObj.getModule_icon())+sanitiseName(moduleObj.getModule_name_code(), moduleObj.getModule_name())+"<span class='pull-right'><b class='caret'></b></span></span>" +
                                    "   </a>" +
                                    "      <ul class='nav-list collapse' id='"+moduleObj.getID()+"'>";
                            } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_4)) {
                                menuList += "<li class='menu-text kt-menu__item  kt-menu__item--submenu' aria-haspopup='true' data-ktmenu-submenu-toggle='hover'>\n" +
                                    "	<a class='accordion-heading kt-menu__link' data-toggle='collapse' data-target='#_"+moduleObj.getID()+"'>\n" +
                                    "       "+getIconString(moduleObj.getModule_icon())+sanitiseName(moduleObj.getModule_name_code(), moduleObj.getModule_name())+"<span class='pull-right'><b class='caret'></b></span>\n" +
                                    "	</a>\n" +
                                    "   <ul class='collapse' id='_"+moduleObj.getID()+"'>";
                            } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_5)) {
                                menuList += "<li class='nav-item '>" +
                                    "   <a class='nav-link dropdown-indicator' href='#"+moduleObj.getID()+"' role='button' data-bs-toggle='collapse' aria-expanded='false' aria-controls='" + moduleObj.getID() + "'>" +
                                    "      <div class=\"d-flex align-items-center\">"+getIconString(moduleObj.getModule_icon())+ "<span class=\"nav-link-text ps-1\">" + sanitiseName(moduleObj.getModule_name_code(), moduleObj.getModule_name())+"<span></div>" +
                                    "   </a>" +
                                    "      <ul class='nav collapse' id='"+moduleObj.getID()+"'>";
                            }
                        menuList += rtnMenu + strTemp;
                        if(!blnOpenTagAdded) {
                        menuList += "  </ul></li>";//sereneC @ 27/7/2014 ::add </div> to close <div class='sf-mega'>
                        }
                    }
                    if (blnHasSubMenu) {
                        menuList += "</ul>";
                    }

                    if((i%4)==0)
                    {
                       moduleTile += "</div><div class='row'>";
                    }

                    singleApp = null;   // Added by Delvene @ 11-Dec-2014 :: Reset back to null incase all applications are tied to Sub-Module and NONE in Main Module
                }

                moduleTile += "</div>";
                ActionContext.getContext().getSession().remove("menuList");
                ActionContext.getContext().getSession().put("menuList", menuList);
                //Added by Delvene @ 10-Dec-2013 :: To display module tiles in main screen
                ActionContext.getContext().getSession().remove("moduleTile");
                ActionContext.getContext().getSession().put("moduleTile", moduleTile);
                //Added by Delvene @ 10-Dec-2013 :: To display module tiles in main screen - END
            }

            ParameterModel paramModel = (ParameterModel) baseDAO.getSession().getNamedQuery("Parameter.getByParameter_code")
                .setParameter("system_code", (Validator.isEmpty(systemType_)?"DEF":systemType_))
                .setParameter("parameter_code", SystemConstants.PARAM_NAME.USER_GROUP_HIDE)
                .uniqueResult();

            if (user.getGroupUserList().size() > 0) {
                myProfileList += "<div style='padding:10px 15px;'><b><ul>" + getText("userAccess.navStep.KumpulanPengguna") + "</ul></b>";
                myProfileList += "<div class=\"ul_image\">";
                for (GroupUser userGroup : user.getGroupUserList()) {
                    if (paramModel.getParameter_value().indexOf('*' + userGroup.getUserGroup().getGroup_code() + '*') < 0) {
                        myProfileList += "<p>" + userGroup.getUserGroup().getGroup_name() + "</p>";
                    }
                }
                myProfileList += "<br style=\"clear: left\" /></div>";
                myProfileList += "</div>";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            applicationDAO.closeSession();
            baseDAO.closeSession();
        }
    }
    
    public String validate_jsp() {
        return "validation";
    }
    
    // For Reset Password : Start //
    private String resetUserId = null;
    public String getResetUserId() {
        return resetUserId;
    }
    public void setResetUserId(String resetUserId) {
        this.resetUserId = resetUserId;
    }

    private String resetEmail = null;
    public String getResetEmail() {
        return resetEmail;
    }
    public void setResetEmail(String resetEmail) {
        this.resetEmail = resetEmail;
    }
    
    User model = null;
    public User getModel() {
        return model;
    }
    public void setModel(User model) {
        this.model = model;
    }
    
    private String confirmPassword = null;
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public String activateNew() {
        try {
            model = (User) baseDAO.getSession().getNamedQuery("User.findByActivationCode")
                    .setParameter("activationCode", getActivationCode()).uniqueResult();
            if (model == null) {
                addActionError(getText("errors.invalidCode"));
                return "forgotPassword";
            }
            
            Boolean transBegin = Boolean.FALSE;
            try {
                validateActicationCode(activationCode, model);
                if (Validator.isEmpty(confirmPassword)) {
                    addActionError(getText("field.required", new String[]{getText("user.newPassword") + " & " + getText("user.confirmPassword")}));
                    return "reset";
                }
                baseDAO.beginBatchTransaction();
                transBegin = Boolean.TRUE;
                baseDAO.getSession().createNativeQuery("update t_setup_user set us_activation_code = null, us_password = :newPassword where us_id = :us_id ")
                    .setParameter("newPassword", Base64.encodeBase64String(UserDAOImpl.encrypt(Base64.decodeBase64(UserDAOImpl.Base64PublicStr), confirmPassword.getBytes())))
                    .setParameter("us_id", model.getID())
                    .executeUpdate();
                baseDAO.commitBatchTransaction();
                addActionMessage(getText("lbl.resetPassword.activated"));
            } catch (CustomBaseException e) {
                if (transBegin) {
                    baseDAO.rollbackBatchTransaction();
                }
                addActionError(e.getMessage());
                return "forgotPassword";
            } catch (Exception e) {
                if (transBegin) {
                    baseDAO.rollbackBatchTransaction();
                }
            }
        } catch (Exception e) {
        }
        return "activated";
    }
    
    private String activationCode = null;
    public String getActivationCode() {
        return activationCode;
    }
    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
    
    private void validateActicationCode(String code, User user) throws Exception {
        String decryptedCode = new String(UserDAOImpl.decrypt(Base64.decodeBase64(UserDAOImpl.Base64PrivateStr), Base64.decodeBase64(code)));
        decryptedCode = decryptedCode.substring(user.getID().length()+1);
        Calendar cal = DateUtil.getCalendar(DateUtil.getDate(decryptedCode, Formatter.DATE_PATTERN+ " " + Formatter.TIME_PATTERN));
        cal.add(Calendar.DATE, 1);
        if (DateUtil.getCalendar().after(cal)) {
            throw new CustomBaseException("ActicationCodeExpired");
        }
    }
    
    public String reset() throws Exception {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        activationCode = request.getParameter("code");
        model = (User) baseDAO.getSession().getNamedQuery("User.findByActivationCode")
                    .setParameter("activationCode", request.getParameter("code")).uniqueResult();
        if (model == null) {
            addActionError(getText("errors.invalidCode"));
            return "forgotPassword";
        }
        try {
            validateActicationCode(activationCode, model);
        } catch (Exception e) {
            addActionError(getText("errors.resetPasswordExpired"));
            return "forgotPassword";
        }
        return "reset";
    }
    public String forgot() throws Exception {
        return "forgotPassword";
    }
    
    private String ajax = null;
    public String getAjax() {
        return ajax;
    }
    public void setAjax(String ajax) {
        this.ajax = ajax;
    }
    
    private Boolean checkReturnAjax() {
        if (!Validator.isEmpty(ajax) && ajax.equals("true")) {
            for (Object object : getActionErrors().toArray()) {
                if (Validator.isEmpty(r)) {
                    r = "rmsg;"+object.toString();
                } else {
                    r += "\r\n"+object.toString();
                }
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    public String processReset() {
//        r = "rmsg;"+getText("generateEntry.generated");
        if (Validator.isEmpty(ajax)) {
            if (Validator.isEmpty(resetUserId) && Validator.isEmpty(resetEmail)) {
                addActionError(getText("field.required", new String[]{getText("lbl.resetPassword.required")}));
            }
            if (!getActionErrors().isEmpty()) {
                return "forgotPassword";
            }
        }
        try {
            User user = null;
            List<User> userList = null;
            if (Validator.isEmpty(ajax)) {
                if (!Validator.isEmpty(resetUserId) && !Validator.isEmpty(resetEmail)) {
                    user = (User) baseDAO.getSession().getNamedQuery("User.findByIdAndEmail")
                            .setParameter("us_user_id", resetUserId)
                            .setParameter("us_email", resetEmail).uniqueResult();
                } else if (Validator.isEmpty(resetUserId)) {//userId is empty means email is not empty
                    userList = baseDAO.getSession().getNamedQuery("User.findByEmail")
                            .setParameter("us_email", resetEmail).list();
                } else {//userid is not empty
                    user = (User) baseDAO.getSession().getNamedQuery("User.findByUsUserId").setParameter("us_user_id", resetUserId).uniqueResult();
                }
            } else {
                BaseDAO dao = baseDAO;
                user = (User) dao.getModelById(resetUserId, User.class); //reused resetUserId as t_setup_user's us_id
            }
            if (user == null && (userList==null||userList.isEmpty()) ) {
                addActionError(getText("errors.resetPasswordIdEmail"));
            }
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            request.setAttribute("ignoreCsrfCheck", "true");
            if (user!=null) {
                user.set_operation("generateActivationCode");
                baseDAO.manualUpdate(user);
            } else {
                for (User moreUser : userList) {
                    moreUser.set_operation("generateActivationCode");
                    baseDAO.manualUpdate(moreUser);
                }
            }
//            String resetCode = user.getID() +"_"+Formatter.formatDate(DateUtil.getCurrentTimestamp(), Formatter.DATE_PATTERN + " " + Formatter.TIME_PATTERN);
//            resetCode = Base64.encodeBase64String(UserDAOImpl.encrypt(Base64.decodeBase64(UserDAOImpl.Base64PublicStr), resetCode.getBytes()));
//            baseDAO.beginBatchTransaction();
//            transBegin = Boolean.TRUE;
//            baseDAO.getSession().createNativeQuery("update t_setup_user set us_activation_code = :activationCode where us_id = :us_id ")
//                    .setParameter("activationCode", resetCode)
//                    .setParameter("us_id", user.getID())
//                    .executeUpdate();
//            EmailQueueTrigger eqTrigger = new EmailQueueTrigger();
//            Map mailParam = new HashMap();
//            mailParam.put(EmailQueueTrigger.MAIL_TO, user.getUs_user_name() + "<"+user.getUs_email()+">");
//            mailParam.put("userId", user.getUs_user_id());
//            mailParam.put("activationCode", URLEncoder.encode(resetCode, StandardCharsets.UTF_8.toString()));
//            NotificationSetup notification = (NotificationSetup)baseDAO.getSession().getNamedQuery("Notification.findByNoType")
//                    .setParameter("noType", "Reset Password").uniqueResult();
//            eqTrigger.sendEMail(mailParam, notification);
//            baseDAO.commitBatchTransaction();
        } catch (Exception e) {
//            if (transBegin) {
//                baseDAO.rollbackBatchTransaction();
//            }
            e.printStackTrace();
        }
        if (!getActionErrors().isEmpty()) {
            if (checkReturnAjax()) {
                return "divSubmitForm";
            }
            return "forgotPassword";
        }
        if (!Validator.isEmpty(ajax) && ajax.equals("true")) {
            r = "rmsg;"+getText("emailSent");
            return "divSubmitForm";
        }
        return "successEmail";
    }
    // For Reset Password : End //
    
    public String sso()  throws Exception{
           SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();
           HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
           Debug.printFrameworkDebug("3 request : "+request.getParameter("usrid"));
            userId = request.getParameter("usrid");
            sessionMap.put("map_api_call","map_api_call");
         
        
           
        return  processlogin();
    }
    
    // For testing API Call : START //
    public void callApi() throws Exception {
        Debug.printFrameworkDebug("call url return :");
        String apiString = ApiUtil.getToken_aquila("API_CALL");
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
//        response.setContentType("application/json");
        response.getWriter().append(apiString);
        response.flushBuffer();
    }
    // For testing API Call : END //
    
    
    public String updateCaseStatus() throws Exception {
        SessionMap sessionMap = (SessionMap) ActionContext.getContext().getSession();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Map jsonMap = new HashMap();
        BaseDAO dao = baseDAO;
        AutoEmailDAO autoEmailDAO = new AutoEmailDAOImpl();
        autoEmailDAO.setSession(dao.getSession());
        AutoEmail autoEmail = null;
        FtpInterface ftps = new SFTPBean();
//        SPAService spaService = null;
        if (request.getParameter("caseId") == null) {
            jsonMap.put("errMsg", "Case ID is required");
            jsonMap.put("status", "fail");
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
            sessionMap.invalidate();
            return null;
        }
        
        if (request.getParameter("caseStatus") == null) {
            jsonMap.put("errMsg", "Case Status is required");
            jsonMap.put("status", "fail");
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
            sessionMap.invalidate();
            return null;
        }
        
        Debug.printFrameworkDebug("####loginAction updateCaseStatus caseId :"+request.getParameter("caseId")+" caseStatus:"+request.getParameter("caseStatus"));
        jsonMap.put("status", "success");
        jsonMap.put("errMsg", "");
                
        response.setContentType("application/json");
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
        sessionMap.invalidate();
        return null;
    }
    
    private String userDivisionId;
    public String getUserDivisionId() {
        return userDivisionId;
    }

    public void setUserDivisionId(String userDivisionId) {
        this.userDivisionId = userDivisionId;
    }
    

}
