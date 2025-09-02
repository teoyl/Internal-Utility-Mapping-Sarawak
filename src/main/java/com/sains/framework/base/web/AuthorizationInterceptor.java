package com.sains.framework.base.web;

import com.SysConf;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.sains.common.util.DateUtil;
import com.sains.common.util.SystemConstants;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.PreResultListener;
import com.sains.common.util.Formatter;
import com.sains.common.util.Validator;
import com.sains.framework.base.*;
import java.lang.reflect.Method;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.I18nInterceptor;
import org.hibernate.Query;
//import static sun.security.x509.CertificateAlgorithmId.ALGORITHM;

public class AuthorizationInterceptor extends AbstractInterceptor {
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Cannot simply create Instance Variable in AI, it may be override because there are only 1 instance of AI will be created //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final Boolean debugMode = Boolean.TRUE;
    private static final Integer leavingSecond = 50;
    private static Long triggerCount = 0L;
    private static final long serialVersionUID = 4033782781698316495L;
    private static Map<String, List<String>> byPassMap = null;
//    private String notAuthorisedMessage = null;
//    private User user = new User();
//    Application application = new Application();
    //added by ai min
    private final String LOGIN_FROM = "loginFrom";
    private String USER_KEY = "userId";
    private String EXPIRED_KEY = "expired";
    private String LAST_VISIT_KEY = "lastVisit";
    Logger log = LogManager.getLogger(this.getClass());
    private final CommonFunction commonFunction = new CommonFunction();

    private static final List<String> directRunActionClass = Arrays.asList("HandShakeAction", "HibernateInformationAction", "LoginAction", "LookupAction", "LoginEssAction", "UnauthorisedAction", "FileAction" ,"SampleAction","ItemChangeAction","UppyUploadAction","UtilityMessageAction", "AttachmentUploadAction", "DbAction","LxgAction");
    
    private static Boolean checkTriggerEmail = Boolean.FALSE;
    private static Boolean startUpCheck = null;
    private static Boolean startupEmailSent = null;
//    public synchronized static void startupEmail() {
//        if (startUpCheck == null) {
//            startUpCheck = Boolean.TRUE;
//        } else {
//            if (startUpCheck && checkTriggerEmail && startupEmailSent == null) {
//                startupEmailSent = Boolean.TRUE;
//                System.out.println("------------ sentEmail start up email -----------");
//                try {
//    //                String MAIL_TO = "to";
//    //                String MAIL_CCTO = "ccto";
//    //                String MAIL_BCCTO = "bccto";
//                    ClsMail mail = new ClsMail();
//                    mail.setSendFrom("tomcamStart@impian2.gov.my");
//                    mail.setOutMailServer("smtp.sarawaknet.gov.my");
//                    mail.setInMailServer("smtp.sarawaknet.gov.my");
//    //                Map param = new HashMap();
//    //                param.put(MAIL_TO, "kiasuwee@gmail.com");
//    //                param.put(MAIL_CCTO, "");
//    //                param.put(MAIL_BCCTO, "");
//                    mail.setTo("kiasuwee@gmail.com");
//                    mail.setCc("");
//                    mail.setBcc("");
//
//                    mail.setSubject("I'm started :: WHAT IP???");
//                    mail.setMailType("H"); // HTML Style;
//                    mail.setBody("TomCat has been started/restarted.");
//                    mail.sendMsg();
//                    mail = null;
//                    System.out.println("startWorkFlow here too");
//                    new CallWorkflow().start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    public static Boolean ChangeDefaultLocale = Boolean.FALSE;
    public static Boolean enableCountdown = null;
    public static Integer warningSecond = null;
    public static Integer timeoutMaxSecond = null;
    public static Boolean maintenanceMode = Boolean.TRUE;
    public static Calendar maintenanceStart = null;
    public static Calendar maintenanceEnd = null;
    public static List<String> maintenanceRemark = null;
    public static List<String> allowedIp = null;
    
    public void initialMaintenanceModeAfterDeployment() throws Exception {
        if (allowedIp == null) {
            allowedIp = new ArrayList();
            maintenanceMode = Boolean.TRUE;
            
            //add the IPs that allow to run in maintenance mode
//            allowedIp.add("0:0:0:0:0:0:0:1"); //localhost
            allowedIp.add("10.17.101.219");
            
            //setup the maintenance start and end date, date format is yyyyMMddHHmm (LoginAction.MaintenanceDatetimeFormat)
            maintenanceStart = DateUtil.getCalendar(DateUtil.getDate("201908220815", LoginAction.MaintenanceDatetimeFormat));
            maintenanceEnd = DateUtil.getCalendar(DateUtil.getDate("201908220920", LoginAction.MaintenanceDatetimeFormat));
            
            //if want to add maintenance remark.
            maintenanceRemark = new ArrayList();
            maintenanceRemark.add("remark 1");
            maintenanceRemark.add("remark 2");
        }
    }
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        try {
            BaseActionSupport_API apiAction = (BaseActionSupport_API)invocation.getAction(); //if extends BaseActionSupport_API
            try {
                if (invocation.getAction().getClass().getSimpleName().equals("ApiTokenAction")) {
                    invocation.invoke();
                    return null;
                }
            } catch (Exception e) {
                apiAction.responseFail(e.getMessage());
                return null;
            }
            try {
                apiAction.authenticate(invocation.getProxy().getMethod());
            } catch (Exception e) {
                e.printStackTrace();
                apiAction.responseFail(e.getMessage());
                return null;
            }
            return invocation.invoke();
        } catch (Exception e) { //not extending BaseActionSupport_API
//            e.printStackTrace();
        }
        Map session = ActionContext.getContext().getSession();
        //** Load and set default language : START **//
        if (session.get("multilingualSupport")==null) {
            String defaultLanguage = ((ActionSupport)invocation.getAction()).getText("system.defaultLanguage");
            String multilingualList = ((ActionSupport)invocation.getAction()).getText("system.multilingualList");
            session.put("multilingualSupport", !Validator.isEmpty(multilingualList) && multilingualList.contains(","));
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
            session.put("defaultLocaleSet", "y");
        }
        //** Load and set default language : END **//
        //** Set other locale if don't want to default with English : START **//
//        if (ChangeDefaultLocale) {
//            if (session.get("defaultLocaleSet")==null) {
//                Locale locale = new Locale("bm", "bm");
//                ActionContext.getContext().setLocale(locale);
//                session.put(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, locale);
//                session.put("defaultLocaleSet", "y");
//            }
//        }
        // if want to set back to default locale
        //Locale locale = new Locale("", "");
        //** Set other locale if don't want to default with English : END **//
        
        //** to enable maintenance mode after deployment : uncomment below **//
//        initialMaintenanceModeAfterDeployment();
            
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        if (enableCountdown == null) {
            enableCountdown = SysConf.get("countdown.enable").equalsIgnoreCase("true");
            if (enableCountdown) {
                warningSecond = 120;
                try {
                    warningSecond = Integer.parseInt(SysConf.get("countdown.warningSecond"));
                } catch (Exception e) {
                }
                warningSecond = warningSecond * 1000;
                timeoutMaxSecond = request.getSession().getMaxInactiveInterval() * 1000;
            }
        }
        if (enableCountdown) {
            session.put("enableCountdown", enableCountdown);
            session.put("countdown_redirAfter", timeoutMaxSecond);
            session.put("countdown_warnAfter", timeoutMaxSecond - warningSecond);
            session.put("countdown_warnAfter", timeoutMaxSecond - warningSecond);
            session.put("countdown_stayAfterTimeout", SysConf.get("countdown.stayAfterTimeout").equalsIgnoreCase("true"));
            
        }
        
        if (maintenanceMode) {
            Calendar cal = DateUtil.getCalendar();
            if (cal.after(maintenanceStart) && cal.before(maintenanceEnd)) {
                String userIp = CommonFunction.getUserIp(request);
                if (allowedIp!=null && allowedIp.contains(userIp)) {
                    session.put("maintenanceStart_time", Formatter.formatDate(maintenanceStart.getTime(), "dd MMM yyyy HH:mm:ss"));
                    session.put("maintenanceEnd_time", Formatter.formatDate(maintenanceEnd.getTime(), "dd MMM yyyy HH:mm:ss"));
                    session.put("maintenanceRemark", maintenanceRemark);
                } else {
                    session.put("maintenanceStart", maintenanceStart);
                    session.put("maintenanceStart_time", Formatter.formatDate(maintenanceStart.getTime(), "dd MMM yyyy HH:mm:ss"));
                    session.put("maintenanceEnd", maintenanceEnd);
                    session.put("maintenanceEnd_time", Formatter.formatDate(maintenanceEnd.getTime(), "dd MMM yyyy HH:mm:ss"));
                    session.put("maintenanceRemark", maintenanceRemark);
                    return "underMaintenance";
                }
            } else {
                maintenanceMode = Boolean.FALSE;
            }
        }
        Long localCount = triggerCount++;
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        //the frame-src and frame-ancestors is sample of how to allow the project to include or be included in iframe
//        response.setHeader("Content-Security-Policy", "default-src 'self' http://localhost:8080/  https://fim2.lab.sains.com.my/ https://ss-intmobile.tnt.sarawak.gov.my/ https://fonts.googleapis.com/ https://fonts.gstatic.com https://polyfill.io https://sarawakid-tnt.sarawak.gov.my https://sarawakid.sarawak.gov.my https://utilitysurvey-tnt.sains.com.my ; style-src 'self' 'nonce-EuTVqS192VKl' https://fonts.googleapis.com https://polyfill.io;  script-src 'self' 'nonce-r4DjhKbfO5ry' https://polyfill.io; img-src 'self' https://utilitysurvey-tnt.sains.com.my http://localhost:8080 data: blob:; font-src 'self' http://localhost:8080 https://utilitysurvey-tnt.sains.com.my https://fonts.gstatic.com data:; frame-src 'self' http://10.17.101.219:8080 http://localhost:8113 https://utilitysurvey-tnt.sains.com.my; frame-ancestors http://localhost:8080 http://localhost:8113 https://utilitysurvey-tnt.sains.com.my https://polyfill.io");
        response.setHeader("Content-Security-Policy", "default-src 'self' http://localhost:8080/  https://fim2.lab.sains.com.my/ https://ss-intmobile.tnt.sarawak.gov.my/ https://fonts.googleapis.com/ https://fonts.gstatic.com https://polyfill.io https://sarawakid-tnt.sarawak.gov.my https://sarawakid.sarawak.gov.my https://utilitysurvey-tnt.sarawak.gov.my https://cdn.ckeditor.com 'unsafe-inline'; img-src 'self' https://utilitysurvey-tnt.sarawak.gov.my http://localhost:8080 data: blob:; font-src 'self' http://localhost:8080 https://utilitysurvey-tnt.sarawak.gov.my https://fonts.gstatic.com data:; frame-src 'self' http://10.17.101.219:8080 http://localhost:8113 https://utilitysurvey-tnt.sarawak.gov.my; frame-ancestors http://localhost:8080 http://localhost:8113 https://utilitysurvey-tnt.sarawak.gov.my");
        //added to control back button
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("x-frame-options", "SAMEORIGIN");
        response.setHeader("Strict-Transport-Security", "max-age=31536000");
        
        //if Chrome, then heed to set HttpOnly;Secure;SameSite=None for the jsessionid so that CORS from other site can use back the session
        //currently only apply if chrome (FF no problem if do no apply this). Maybe can apply for all (After pentest team confirm can apply for all)
//        if (request.getHeader("User-Agent").contains("Chrome")) {
//            String session_id = request.getSession().getId();
//            String SESSION_COOKIE_NAME = "JSESSIONID";
//            String SESSION_PATH_ATTRIBUTE = ";Path=";
//            String ROOT_CONTEXT = "/";
//            String SAME_SITE_ATTRIBUTE_VALUES = ";HttpOnly;Secure;SameSite=None";
//
//            String rawCookie = request.getHeader("Cookie");
//            System.out.println("request.getHeaderNames " + request.getHeaderNames());
//            System.out.println("rawCookie !!@@ "+ rawCookie);
//            String[] rawCookieParams = rawCookie.split(";");
//            
//            Cookie[] cookies = ((HttpServletRequest) request).getCookies();
//            String contextPath = "";
//            Cookie sessionCookie = null;
//
//            if (cookies != null && cookies.length > 0) {
//                List<Cookie> cookieList = Arrays.asList(cookies);
//                sessionCookie = cookieList.stream().filter(cookie -> SESSION_COOKIE_NAME.equals(cookie.getName())).findFirst().orElse(null);
//                if (sessionCookie != null) {
//                    contextPath = request.getServletContext() != null && StringUtils.isNotBlank(request.getServletContext().getContextPath()) ? request.getServletContext().getContextPath() : ROOT_CONTEXT;
//                    response.setHeader("Set-Cookie", sessionCookie.getName() + "=" + sessionCookie.getValue() + SESSION_PATH_ATTRIBUTE + contextPath + SAME_SITE_ATTRIBUTE_VALUES);
//                } else {
//                    response.setHeader("Set-Cookie", SESSION_COOKIE_NAME + "=" + session_id + ";Path=/forNewProject; HttpOnly; Secure; SameSite=None");
//                }
//            }
//        }
        
        Boolean found = Boolean.FALSE;
        if (request.getHeader("ORIGIN") != null ) {
            String refererUrl = request.getHeader("REFERER");
            for (String origin : SysConf.get("accessControlAllowOrigin").split(",")) {
                if (refererUrl == null) {break;}
                if (refererUrl.startsWith(origin.trim())) {
                    found = Boolean.TRUE;
                    response.setHeader("Access-Control-Allow-Origin", origin.trim());
                    break;
                }
            }
        }
        if (!found) {
            response.setHeader("Access-Control-Allow-Origin", SysConf.get("defaultAccessControlAllowOrigin"));
        }
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        response.setHeader("Expires", sdf.format(currentDate));
        response.setDateHeader("Last-Modified", currentDate.getTime());
        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("Vary", "Origin");
        String method = null;
        String actionClass = null;
        String paramAction = null; //for DynamicAction called
        String actionSystemType = "";
//        startupEmail();
        Boolean directRun = Boolean.FALSE;
        method = invocation.getProxy().getMethod();
        actionClass = invocation.getAction().getClass().getSimpleName();
        try {
            if (method.equals("moreInfo") && session.get("logined") == null) {
                String temp = null;
                for (String paramKey : request.getParameterMap().keySet()) {
                    if (temp == null) {
                        temp = "?"+paramKey+"="+request.getParameter(paramKey);
                    } else {
                        temp += "&"+paramKey+"="+request.getParameter(paramKey);
                    }
                }
                session.put("lookupRefererUrl_", request.getRequestURI().substring(request.getRequestURI().indexOf("/", 2))+temp);
                session.put("notFromLookup", Boolean.TRUE);
                return "login_modal";
            }
        } catch (Exception e) {
        }
//        if (request.getRequestURL().toString().startsWith("http://localhost:")) {
//            directRun = Boolean.TRUE;
//        }
        if (method.equals("validate_jsp") && actionClass.equals("LoginAction")) {
            return invocation.invoke();
        }
        if (method.equals("processlogout") && actionClass.equals("LoginAction")) {
            return invocation.invoke();
        }
        
        Date startDatetime = DateUtil.getCurrentDate();
        Method m;
        IBubbleShell bsInterface = null;
        try {
            if (invocation.getAction() instanceof IBubbleShell) {
                bsInterface = (IBubbleShell)invocation.getAction();
                if (session.containsKey("bs_map_sso_uid") && !method.equals("processMapSSO")) { //in case ppl bypass mapping/direct access login
                    ((ActionSupport)invocation.getAction()).addActionMessage(((ActionSupport)invocation.getAction()).getText("errors.needToMapSwkId"));
                    return "bs_map_sso_uid";
                }
                if (bsInterface.isLogined()) {
                    bsInterface.populateBsLoginSession(null);
                } else {
                    if (!session.containsKey("logined")) {
                        if (!(method.equals("logout") || method.equals("init") || method.equals("testCall") || 
                            method.equals("doLogin") || method.equals("processMapSSO") || method.equals("redirect") ||
                            method.equals("landing"))){
                            return "success";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            bsInterface = null;
        }
//        if (!session.containsKey("pubkey")) {
//            CommonFunction.generateKeyPayToSession(session);
//        }
        if (session.get("logined") != null) { //Session hijacking prevention
            if (!session.get("user_ip").equals(CommonFunction.getUserIp(request)) ||
                !session.get("user_agent").equals(CommonFunction.getUserAgent(request))) {
                
                if (session.get(LOGIN_FROM) != null) {
                    return "expired" + session.get(LOGIN_FROM);
                } else {
                    try {
                        return "expired"+((BaseAction)invocation.getAction()).getSystemType_();
                    } catch (Exception e) {
                        return "expired";
                    }
                }
            }
        }
        if (SystemConstants.globalServletContext == null) {
            SystemConstants.globalServletContext = ServletActionContext.getServletContext();
        }
        
        if (actionClass.equals("DynamicAction") || actionClass.equals("LookupAction")) {
            request.setAttribute("request_action", request.getParameter("action"));
        } else {
            request.setAttribute("request_actionClass", actionClass);
        }
        
        if (actionClass.equals("DynamicAction")) {
            try {
                paramAction = invocation.getInvocationContext().getParameters().get("action").getMultipleValues()[0];
            } catch (Exception e) {
            }
        } else if (actionClass.equals("DynamicRptAction")) {
            try {
                paramAction = invocation.getInvocationContext().getParameters().get("rptCode").getMultipleValues()[0];
            } catch (Exception e) {
            }
        }
        if (debugMode) {
//            System.out.println("TIME Start : " + DateUtil.getCurrentDate());
            Debug.printFrameworkInfo("---------- RequestTo:: " + actionClass+"."+method + "(), Total Trigger: "+localCount);
        }
        try {
            m = invocation.getAction().getClass().getMethod("getSystemType_");
            actionSystemType = (String) m.invoke(invocation.getAction());
        } catch (Exception e) {
        }
        
        try { //If the Action = action defined at directRunActionClass, then direcit invoke the action, check the Login to SystemType here
            if (directRunActionClass.contains(actionClass)) {
                if (actionClass.equals("LoginAction")) {
                    if(method.equals("processlogin")){
                        if (SystemConstants.SYSTEM_TYPE.DEFAULT.equals(actionSystemType)) actionSystemType = "";
                        session.put(LOGIN_FROM, actionSystemType);
                    }
                }
                directRun = Boolean.TRUE;
                try {
                    String temp = invokeAndAddSecurityPolicy(invocation);
//                    System.out.println("temp === " + temp);
                    return temp;
//                    return invocation.invoke();
                } catch (Exception e) {
                    System.out.println("Exception here... ");
                    if(!(method.equals("getUserInfo") || method.equals("getGroupInfo")) ){
                        try {
                            e.printStackTrace();
                            if (e.getCause() != null) {
                                ((BaseAction)invocation.getAction()).setErrorAtJSP_(e.getCause().getMessage());
                            } else {
                                ((BaseAction)invocation.getAction()).setErrorAtJSP_(e.getMessage());
                            }
                            m = invocation.getAction().getClass().getMethod("getSystemType_");
                            actionSystemType = (String) m.invoke(invocation.getAction());
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        if (actionSystemType.equals(SystemConstants.SYSTEM_TYPE.DEFAULT)) {
                            return "pageError" + notLoginString();
                        } else {
                            return "pageError"+actionSystemType + notLoginString();
                        }
                    } else {
                        return null;
                    }
                }
            }
            if (byPassMap == null) {
                byPassMap = new HashMap();
                List includeAllList = new ArrayList();
                includeAllList.add("-all-");
                String byPassAction = ((ActionSupport)invocation.getAction()).getText("bypass.action");

                //System.out.println("by pass action = " + byPassAction);
                if (!Validator.isEmpty(byPassAction)) {
                    String[] stActions = byPassAction.split(",");
                    for (String action : stActions) {
                        if (action.indexOf(";") > 0) {
                            List list = new ArrayList();
                            String[] splitedStr = action.split(";");
                            if (splitedStr[1].indexOf(":") > 0) {
                                String[] actions = splitedStr[1].split(":");
                                list.add("-" + actions[0].trim() + "-");
                                StringTokenizer st = new StringTokenizer(actions[1], new String("|"));
                                while (st.hasMoreTokens()) {
                                    list.add(st.nextToken().trim());
                                }
                                byPassMap.put(splitedStr[0], list); // by pass all methods
                            } else {
                                byPassMap.put(splitedStr[0], includeAllList); // by pass all methods
                            }
                        } else {
                            byPassMap.put(action, includeAllList); // by pass all methods
                        }
                    }
                }
            }
            String hc = null;
            String query = null;
            List<String> tempList = null;
            if (actionClass.equals("LookupAction")){
                tempList = byPassMap.get(request.getParameter("LAN_"));
            } else {
                tempList = byPassMap.get(actionClass);
            }
            if (tempList != null) {
                if (actionClass.equals("RegistrationAction")) {
                    directRun = Boolean.TRUE;
                }
                if (tempList.get(0).equals("-all-")
                        || (tempList.get(0).equals("-include-") && tempList.contains(method))
                        || (tempList.get(0).equals("-exclude-") && !tempList.contains(method))) {
                    directRun = Boolean.TRUE;
                }
            }
            if (directRun) {
                return invokeAndAddSecurityPolicy(invocation);
//                return invocation.invoke();
            }
        } catch (Exception e) {
            try {
                if (e.getCause() != null) {
                    ((BaseAction)invocation.getAction()).setErrorAtJSP_(e.getCause().getMessage());
                } else {
                    ((BaseAction)invocation.getAction()).setErrorAtJSP_(e.getMessage());
                }
                m = invocation.getAction().getClass().getMethod("getSystemType_");
                actionSystemType = (String) m.invoke(invocation.getAction());
            } catch (Exception e2) {
            }
            if (actionSystemType.equals(SystemConstants.SYSTEM_TYPE.DEFAULT)) {
                return "pageError" + notLoginString();
            } else {
                return "pageError" + actionSystemType + notLoginString();
            }
        } finally {
            if (directRun) {
                try {
                    m = invocation.getAction().getClass().getMethod("closeSession");
                    m.invoke(invocation.getAction());
                } catch (Exception e) {}
                if (debugMode) {
                    Long timeUsed = DateUtil.getCurrentDate().getTime() - startDatetime.getTime();
                    Debug.printFrameworkInfo("      ==========       "+ actionClass+"."+method+"() Completed using : ___" + timeUsed + "___ miliseconds, trigger=" + (localCount) );
                    if (timeUsed > HibernateInformationAction.loadIfMoreThanSecond) {
                        keepLongProcessing(request, session, timeUsed, startDatetime);
                    }
                }
            }
        }

        //get the HibernateSession from Action's baseDAO to share ard this action's retrieving, and close the session at the finally.
        //Developer no need to manually close it(and preload) anymore (eliminate the lazy-load no session/session is close issue too)
        org.hibernate.Session hibernateSession = null;
        try {
            m = invocation.getAction().getClass().getMethod("hibernateSession");
            hibernateSession = (org.hibernate.Session)m.invoke(invocation.getAction());
        } catch (Exception e) {
            if (debugMode) {
                Debug.printFrameworkInfo("DEBUG: Cannot get hibernateSession from Action.");
            }
        }
        String sessionHashCode = null;
        if (hibernateSession != null) {
            sessionHashCode = ""+System.identityHashCode(hibernateSession);
        }
        try {
            m = invocation.getAction().getClass().getMethod("getSystemType_");
            actionSystemType = (String) m.invoke(invocation.getAction());
        } catch (Exception e) {
        }
//        UserDAOImpl userDAO = new UserDAOImpl();
//        userDAO.setSession(hibernateSession);
//        ApplicationDAOImpl applicationDAO = new ApplicationDAOImpl();
//        applicationDAO.setSession(userDAO.getSession());
        Boolean hibernateIsNull = Boolean.FALSE;
        BaseDAO hibernateIsNullDAO  = null;
        try {
            String checkExpReturn = checkExpired(invocation);
            if (checkExpReturn != null) {
                Debug.printDebug(invocation.getAction().getClass().getSimpleName() + "      ...... Expired found ......");
                if (session.get(LOGIN_FROM) != null) {
                    if (actionClass.equals("LookupAction")){
                        response.getWriter().append("Expired");
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("text/html; charset=UTF-8");
                        response.flushBuffer();
                        return null;
                    }
                    return checkExpReturn + session.get(LOGIN_FROM);
                } else {
                    if (actionClass.equals("LookupAction")){
                        response.getWriter().append("Expired");
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("text/html; charset=UTF-8");
                        response.flushBuffer();
                        System.out.println("return null");
                        return null;
                    }
                    System.out.println("return " + checkExpReturn + actionSystemType);
                    return checkExpReturn + actionSystemType;
                }
            }
            Debug.printFrameworkInfo("actionClass = " + actionClass);
            Debug.printFrameworkInfo("method = " + method);
            Debug.printFrameworkInfo("paramAction = " + paramAction);
            if (commonFunction.validateRight_sql(actionClass, method, session, paramAction, hibernateSession, Boolean.TRUE)) {
                if (actionClass.equals("RegistrationAction") || actionClass.equals("LoginEssAction") || actionClass.equals("LoginEssAction")) {
                    return checkAutomation(invocation);
                }
                //if action = DynamicAction
                if (actionClass.equals("DynamicAction")) {
                    try {
                        String systemType = dynamicAction_systemType(hibernateSession, paramAction);
                        if (!systemType.equals(SystemConstants.SYSTEM_TYPE.DEFAULT)) {
                            ((DynamicAction)invocation.getAction()).setSystemType_(SystemConstants.SYSTEM_TYPE.PUBLIC);
                        }
                    } catch (Exception e) {
                    }
                }
                //end if action = DynamicAction
                
//                populateBreadcrumb(invocation, hibernateSession); //Added by Delvene @ 24-Dec-2013 :: To display breadcrumb
//                populateBreadcrumb(actionClass, invocation, hibernateSession); //Added by Delvene @ 24-Dec-2013 :: To display breadcrumb
                /* ThoTH @ 28-Feb-2014 :: Skip Can View check when:
                                            - Access Level is SAINS & Sarawak
                                            - ESS
                                            - Certain Actions */
                Boolean blnCheckCanView = ((BaseAction)invocation.getAction()).getCheckCanView_();
                
                if (hibernateSession == null) {
                    hibernateIsNullDAO = new BaseDAOImpl();
                    hibernateIsNull = Boolean.TRUE;
                    hibernateSession = hibernateIsNullDAO.getSession();
                }
                
                if (blnCheckCanView) {
                    invocation.addPreResultListener(new PreResultListener() {
                        public void beforeResult(ActionInvocation invocation,
                                String resultCode) {
                            if (debugMode) Debug.printFrameworkDebug("beforeResult: " + resultCode);
                            HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
                            if (!((BaseAction)invocation.getAction()).getSecurityPolicy_().equals("default-src 'self' 'unsafe-inline' 'unsafe-eval';")) {
                                response.setHeader("Content-Security-Policy", ((BaseAction)invocation.getAction()).getSecurityPolicy_());
                            }
                            
                            // ThoTH @ 23-Mar-2015 :: Moved outside the "If (blnCheckCanView) {}", else when secuLevel is 8, this code won't run
                            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                            BaseAction currentAction = (BaseAction) invocation.getAction();
                            if (request.getAttribute("isViewer") != null) { //when request.getAttribute("isViewer") is not null, it must be TRUE;
                                currentAction.setEditMode_(Boolean.FALSE); //set editMode_ to FALSE so that jsp there can do viewer's control (eg, hide button...)
                            }
//                            else if (ActionContext.getContext().getSession().get("access_mode").equals("READONLY")){ //ahmadni 1-Oct-2015
//                                currentAction.setEditMode_(Boolean.FALSE);                                 
//                            }
                            
                            if (currentAction.getSecuLevel_().equals(SystemConstants.ACCESS_TYPE.Sarawak)) return;  // ThoTH 2 23-Mar-2015, sarawak can view all, can skip the rest
                            
                            // Check Can View
                            if (resultCode == null) return;
                            if (SystemConstants.ACTION_Status.REFRESH_EDIT.equals(resultCode) || resultCode.equals("load_list_page") || resultCode.equals("add_grade_page") || // Zhafari @ 27-Mar-2015 - added load_list_page
                                (resultCode.endsWith("_fail"))   ) return; //no need to check can view if the return code is refresh_edit or code endsWith _fail
                            try { //try for the Cast to BaseAction
//                                BaseAction currentAction = (BaseAction) invocation.getAction();
                                Object recordModel = null;

//                                if (Boolean.TRUE) return;

                                try { //try go check can view the retrieved model or not.
                                    Method m;
                                    try {
                                        m = currentAction.getClass().getMethod("getCurrentViewModel_");
                                        recordModel = m.invoke(currentAction);
                                    } catch (Exception e) {}

                                    try {
                                        if (recordModel == null || Validator.isEmpty(((ModelBase)recordModel).getID()) ) {
                                            if (debugMode) Debug.printFrameworkInfo("$$$$$$$$$$$$$$$$$$$ use MODEL $$$$$$$$$$$$$$$$ ");
                                            m = currentAction.getClass().getMethod("getModel");
                                            recordModel = m.invoke(currentAction);
                                        }
                                    } catch (Exception e) {}

                                    // ThoTH @ 7-Mar-2014 :: Special Case to handle Organisation TRee BaEstMgmt & BuEstMgmt
//                                    try {
//                                        if (recordModel != null && Validator.isEmpty(((ModelBase)recordModel).getID())) {
//                                            System.out.println("$$$$ Department Model $$$$");
//                                            recordModel = ((PtEstablishmentModel)recordModel).getDepartment();
//                                        }
//                                    } catch (Exception e) {}
//                                    System.out.println("$$$$$$$$$$$$$$$$$ recordModel.ID : " + ((ModelBase)recordModel).getID()); //commented by Zhafari @ 03-Mar-2014 - can cause NullPointerException
                                    if (recordModel != null && !Validator.isEmpty(((ModelBase)recordModel).getID())) {
                                        if (!currentAction.canView((ModelBase) recordModel)) {
                                            invocation.setResultCode("notAllowToView");
                                        }
                                    } else {
                                        if (debugMode) Debug.printFrameworkInfo("RecordModel is Empty");
                                    }
                                    
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    currentAction.addActionError(e.getMessage());
                                    invocation.setResultCode("notAllowToView");
                                }
                            } catch (Exception e) {}
    //                          invocation.setResultCode("null");
                        }
                    });
                } else {
                    return invokeAndAddSecurityPolicy(invocation);
                }
                
                return invocation.invoke();
            } else {
                if (debugMode) Debug.printFrameworkInfo("************************ NOT Authorised Found ************************");
                if (session.get(LOGIN_FROM) != null) {
                    return SystemConstants.ACTION_Status.NOT_AUTHORISED + session.get(LOGIN_FROM);
                } else {
                    return SystemConstants.ACTION_Status.NOT_AUTHORISED + actionSystemType;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (SystemConstants.ENV == 0) { //only show the exception msg if in development env. (Security req.)
                if (ex.getCause() != null) {
                    ((ActionSupport)invocation.getAction()).addActionError(ex.getCause().toString());
                    ((ActionSupport)invocation.getAction()).addActionError(ex.getCause().getStackTrace()[0].toString());
                } else {
                    ((ActionSupport)invocation.getAction()).addActionError(ex.toString());
                    try {
                        ((ActionSupport)invocation.getAction()).addActionError(ex.getStackTrace()[0].toString());
                    } catch (Exception e) {}
                }
            } else {
                if (!invocation.getResultCode().equals("input")) {
                    ((ActionSupport)invocation.getAction()).addActionError(((ActionSupport)invocation.getAction()).getText("errors.unexpectedError"));
                }
            }
            log.error("Exception at AI", ex);
        } finally {
            try {
                if (SystemConstants.ENV == 0) {
                    if (sessionHashCode != null) {
                        m = invocation.getAction().getClass().getMethod("hibernateSession"); //get the session again.
                        hibernateSession = (org.hibernate.Session)m.invoke(invocation.getAction());
                        if (!sessionHashCode.equals(""+System.identityHashCode(hibernateSession))) {
                            Debug.printFrameworkInfo("!!!!!!!!!!!!!!!!!!!!!!!! ORIGINAL hinerbateSession not same as closeSession's hibernateSession !!!!!!!!!!!!!!!!!!!!!!!!");
                        }
                    }
                } 
                if (hibernateIsNull) {
                    hibernateIsNullDAO.closeSession();
                } else {
                    m = invocation.getAction().getClass().getMethod("closeSession");
                    m.invoke(invocation.getAction());
                }
            } catch (Exception e) {
                if (debugMode) { // ThoTH @ 17-Apr-2014 :: Try to trace canView unCloseSession error
                    new LogFunction().logError(invocation.getAction().getClass(), "", e);
                }
            }
//            try {
//                BaseDAO basedao = new BaseDAOImpl();
//                basedao.setSession(hibernateSession);
//                basedao.closeSession();
//            } catch (Exception e) {}
            if (debugMode) {
                Long timeUsed = DateUtil.getCurrentDate().getTime() - startDatetime.getTime();
                Debug.printFrameworkInfo("      ==========       "+ actionClass+"."+method+"() Completed using : ___" + timeUsed + "___ miliseconds" );
                if (timeUsed > HibernateInformationAction.loadIfMoreThanSecond) {
                    keepLongProcessing(request, session, timeUsed, startDatetime);
                }
            }
//            userDAO.closeSession();
            /*if (userDAO.getSession().isConnected()){
            userDAO.getSession().disconnect();
            System.out.println("userDAO Session disconnected----------");
            }
            if (userDAO.getSession().isOpen()){
            userDAO.getSession().clear();
            userDAO.getSession().flush();
            userDAO.getSession().close();
            System.out.println("userDAO Session Closed----------");

            }
            if (applicationDAO.getSession().isConnected()){
            applicationDAO.getSession().disconnect();
            System.out.println("applicationDAO Session disconnected----------");
            }
            if (applicationDAO.getSession().isOpen()){
            applicationDAO.getSession().clear();
            applicationDAO.getSession().flush();
            applicationDAO.getSession().close();
            System.out.println("application Session Closed----------");
            }*/

            //applicationDAO.closeSession();
//            userDAO = null;
//            applicationDAO = null;
            //closeSession(applicationDAO, userDAO);
            //p_closeSession(p_applicationDAO, p_userDAO);
        }
        try {
            m = invocation.getAction().getClass().getMethod("setCs_", String.class);
            m.invoke(invocation.getAction(), "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }

    private String checkAutomation(ActionInvocation invocation) throws Exception {
        //System.out.println("checkAutomation running.........");
        String returnStr = invocation.invoke();
        new EmailTrigger().check(invocation.getAction(), invocation.getAction().getClass().getSimpleName(), invocation.getProxy().getMethod(), returnStr);
        //add checking here for automation:
        return returnStr;
    }

    private String checkExpired(ActionInvocation invocation){
        Map session = ActionContext.getContext().getSession();
        if (session.get(USER_KEY) == null) {
//            System.out.println("in set expired");
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//        String queryString = request.getQueryString();
//            queryString = (queryString == null ? "" : "?" + queryString);
//            session.put(LAST_VISIT_KEY, request.getRequestURL().toString() + queryString);
            return SystemConstants.ACTION_Status.EXPIRED;
        }
        return null;
    }

    private void keepLongProcessing(HttpServletRequest request, Map session, Long timeUsed, Date startDatetime) {
        Map map = new HashMap();
        map.put("user", session.get("loginId"));
        map.put("processingTime", timeUsed);
        map.put("reqTime", Formatter.formatDate(startDatetime, Formatter.DATE_PATTERN + " hh:mm:ss a"));
        if (Validator.isEmpty(request.getQueryString())) {
            map.put("theRequest", request.getRequestURI());
        } else {
            map.put("theRequest", request.getRequestURI()+ "?" +request.getQueryString());
        }
        
        if (HibernateInformationAction.longRequestList.size() >= 30) {
            HibernateInformationAction.longRequestList.remove(29);
        }
        HibernateInformationAction.longRequestList.add(0, map);
    }
    
    private String dynamicAction_systemType(org.hibernate.Session hibernateSession, String paramAction) {
        Query query = hibernateSession.createSQLQuery("select system_type from t_setup_application where application_code = :app_code");
        query.setString("app_code", paramAction);
        return (String) query.uniqueResult();
    }
    
    private String invokeAndAddSecurityPolicy(ActionInvocation invocation) throws Exception{
//        invocation.addPreResultListener(new PreResultListener() {
//            public void beforeResult(ActionInvocation invocation, String resultCode) {
//                HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
//                if (response.getHeader("Content-Security-Policy") == null) {
//                    System.out.println("Content-Security-Policy is null");
//                    if (invocation.getAction() instanceof UnauthorisedAction) {
//                        response.setHeader("Content-Security-Policy", "default-src 'self' 'unsafe-inline' 'unsafe-eval';");
//                    } else {
//                        if (!(invocation.getAction() instanceof HibernateInformationAction)) {
//                            response.setHeader("Content-Security-Policy", "default-src 'self' 'unsafe-inline' 'unsafe-eval';");
//                        } else {
//                            response.setHeader("Content-Security-Policy", ((BaseAction)invocation.getAction()).getSecurityPolicy_());
//                        }
//                    }
//                } else {
//                    System.out.println("Content-Security-Policy is not null : " + response.getHeader("Content-Security-Policy"));
//                }
//            }
//        });
        
        return invocation.invoke();
    }
    
    public String notLoginString() {
        Map session = ActionContext.getContext().getSession();
        if (session.containsKey("logined")) {
            return "";
        }
        return "_logout";
    }
}	
