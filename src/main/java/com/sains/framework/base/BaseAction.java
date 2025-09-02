package com.sains.framework.base;

import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Options;
import static com.sains.framework.base.CommonFunction.constructAJP_PORT_inChar;
import com.sains.framework.model.Application;
import com.sains.framework.model.Module;
import com.sains.framework.model.User;
import com.sample.ParentModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.simple.JSONObject;

public abstract class BaseAction<T> extends ActionSupport {

    public BaseAction() {
        super();
    }

    public BaseAction(org.hibernate.Session session) {
        super();
        baseDAO.setSession(session);
    }
    private static final long serialVersionUID = -1477947567038101484L;
    protected String systemType_ = SystemConstants.SYSTEM_TYPE.DEFAULT;
    protected BaseDAO<T> baseDAO = new BaseDAOImpl<T>();
    BaseDAO lasisDAO = null;
    protected String commStr_ = null; //used as a common string
    private String mobile_text_ = "";

    protected Boolean editMode_ = Boolean.FALSE; // ThoTH @ 26-Feb-2014
//    protected Boolean disabledMode_ = Boolean.TRUE; // amywyp 01-08-2018
    private Object jsonData;  // ThoTH @ 23-Jun-2014

    //Added by Delvene @ 24-Dec-2013 :: To support breadcrumb display in breadcrumb.jsp. Direct getter and setter from AuthorizationInterceptor.java doesn't work
//    private Module breadMod = null;
//    private Application breadApp = null;
//    private String myBreadCrumb_ = null; // ThoTH @ 20-Jan-2014
    private String definedBreadCrumb = null;
    private String breadAppCode_ = null; //  ThoTH @ 20-Jan-2014 :: let Developer to set when multiple Applications using same Action
    //Added by Delvene @ 24-Dec-2013 :: To support breadcrumb display in breadcrumb.jsp. Direct getter and setter from AuthorizationInterceptor.java doesn't work - END

    private String secuLevel_ = "";  // ThoTH @ 23-Mar-2015 :: For the purpose of setEditMode_ in beforeResult

    protected String refererUrl_ = "";  // ThoTH @ 15-Feb-2016 : remember the URL before login, and direct to this URL once login

    public Session hibernateSession() {
        return baseDAO.getSession();
    }

    public void session_(org.hibernate.Session session) {
        baseDAO.setSession(session);
    }

    private List otherActionList = null;

    public synchronized List getOtherActionList() {
        if (otherActionList == null) {
            otherActionList = new ArrayList();
        }
        return otherActionList;
    }

    public void setOtherActionList(List otherActionList) {
        this.otherActionList = otherActionList;
    }

    public void closeSession() {
        baseDAO.closeSession();
        if (lasisDAO != null) {
            Debug.printFrameworkInfo("lasis closed count = " + ++lasisCloseCount);
            lasisDAO.closeSession();
            lasisDAO = null;
        }
        if (otherActionList != null) {
            for (Object otherAction : otherActionList) {
                try {
                    ((BaseAction) otherAction).closeSession();
                } catch (Exception e) {
                }
            }
        }
    }

    private static Integer lasisOpenCount = 0, lasisCloseCount = 0;

//    protected synchronized BaseDAO getLasisDAO() {
//        if (lasisDAO == null) {
//            lasisDAO = new BaseDAOImpl();
//            lasisDAO.setSession(SessionFactoryImpl.getSession_lns());
//            System.out.println("lasis opened count = " + ++lasisOpenCount);
//        }
//        return lasisDAO;
//    }

    public String getSystemType_() {
        return systemType_;
    }

    public void setSystemType_(String systemType_) {
        this.systemType_ = systemType_;
    }

    StringBuffer strBuf = null;
    public String getMyBreadCrumb_() {
        if (strBuf == null) {
            strBuf = new StringBuffer();
            BaseDAO retDAO = new BaseDAOImpl();
            try {
                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                Application selectedApp = null;
                Module selectedModule = null;

                String strLevel = "";
                retDAO.setSession(baseDAO.getSession());

                String actionClass = this.getClass().getSimpleName();
                String str = "";
                // ***** GET APPLICATION ***** //
                // Check if is DynamicAction
                if (actionClass.equals("DynamicAction")) {  // Special Case for Dynamic-config
                    str = request.getParameter("action");
                    selectedApp = (Application) retDAO.getModelByCode("application_code", str, new Application());

                } else if (actionClass.equals("LoginAction") || actionClass.equals("LoginEssAction")) {  // Special Case when click on Module
                    // By Pass
                    if (definedBreadCrumb != null) {
                        return definedBreadCrumb;
                    }
                } else if (actionClass.equals("JobDetailAction") || !Validator.isEmpty(request.getParameter("dType"))) {  // Special Case for WF
                    selectedApp = (Application) retDAO.getModelByCode("action_class", "JobMainAction", new Application());

                } else {
                    str = getBreadAppCode_();
                    //System.out.println("str ==== " + str);
                    if (!Validator.isEmpty(str)) {
                        selectedApp = (Application) retDAO.getModelByCode("application_code", str, new Application());
                    } else {
                        // May return multiple if using ActionName, then shall override the getBreadAppCode_ method in your action to get the desired Application
                        selectedApp = (Application) retDAO.getModelByCode("action_class", actionClass, new Application());
                    }
                }

                if (selectedApp == null) {  // either a Main or Module Level
                    // ***** GET MODULE ***** //
                    strLevel = "MODULE";
                    str = request.getParameter("moduleCode");  // This can always get from URL, the rest won't
                    if (!Validator.isEmpty(str)) {
                        selectedModule = (Module) retDAO.getModelByCode("module_code", str, new Module());
                    }

                    if (selectedModule == null) {  // at Main Page
                        strLevel = "MAIN";
                    }
                }

                StringBuffer strBufBottom = new StringBuffer();
                if (strLevel.equals("MAIN")) {
                    // Main Page
                    //Added by Delvene @ 13-FEb-2014 :: Differentiate ESS and Default breadcrumb
                    // Commented by Delvene @ 01-Aug-2014 :: Kho requested to remove Laman Utama from main page
    //                if(systemType_.equals(SystemConstants.SYSTEM_TYPE.ESS)) {
    //                    strBuf.append("<span id='bread_selected'><a href='initLoginESS'>Laman Utama</a></span>");
    //                } else {
    //                    strBuf.append("<span id='bread_selected'><a href='initLogin'>Laman Utama</a></span>");
    //                }
                    // Commented by Delvene @ 01-Aug-2014 :: Kho requested to remove Laman Utama from main page - END
                    //Added by Delvene @ 13-FEb-2014 :: Differentiate ESS and Default breadcrumb - END

                } else {
                    //Added by Delvene @ 13-FEb-2014 :: Differentiate ESS and Default breadcrumb
                    //Change to EQP 29-Aug-2016
    //                if(systemType_.equals(SystemConstants.SYSTEM_TYPE.ESS)) {
    //                    strBuf.append("<span><a href='initLoginESS'>Home</a></span>&nbsp;&nbsp;&gt;&nbsp;&nbsp;");
    //                }
    //                System.out.println("systemType_ " +systemType_);
                    if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_3)) {
                        strBuf.append("<li><a href='internal'>Home</a></li>");
                    } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_4)) {
                        strBuf.append("<a href='welcome' class='kt-subheader__breadcrumbs-home'><i class='flaticon2-shelter'></i></a><span class='kt-subheader__breadcrumbs-separator'></span><a href='welcome' class='kt-subheader__breadcrumbs-link'>Home</a>");
                    } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_5)) {
                        strBuf.append("<li class='breadcrumb-item'><a href='internal'>Home</a></li>");
                    }
                    //Added by Delvene @ 13-FEb-2014 :: Differentiate ESS and Default breadcrumb - END

                    if (strLevel.equals("MODULE")) {
                        //System.out.println("module");
                        // Loop selectedModule parents
                        if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_3)) {
                            strBufBottom.insert(0, "<li><a href='loadMainPageLogin?moduleCode=" + selectedModule.getModule_code() + "'>" + selectedModule.getModule_name() + "</a></li>");
                        } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_4)) {
                            strBufBottom.insert(0, "<span class='kt-subheader__breadcrumbs-separator'></span><a href='loadMainPageLogin?moduleCode=" + selectedModule.getModule_code() + "' class='kt-subheader__breadcrumbs-link'>" + selectedModule.getModule_name() + "</a>");
                        } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_5)) {
                            strBufBottom.insert(0, "<li class='breadcrumb-item'><a href='loadMainPageLogin?moduleCode=" + selectedModule.getModule_code() + "'>" + selectedModule.getModule_name() + "</a></li>");
                        }
                        Module tempModule = selectedModule.getParentModule();
                        while (tempModule != null) {
                            //Added by Delvene @ 13-FEb-2014 :: Differentiate ESS and Default breadcrumb
    //                        if(systemType_.equals(SystemConstants.SYSTEM_TYPE.ESS)) {//Change to EQP 29-Aug-2016
                            if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_3)) {
                                strBufBottom.insert(0, "<li><a href='loadMainPageLogin?moduleCode=" + tempModule.getModule_code() + "'>" + tempModule.getModule_name() + "</a></li>");
                            } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_4)) {
                                strBufBottom.insert(0, "<span class='kt-subheader__breadcrumbs-separator'></span><a href='loadMainPageLogin?moduleCode=" + tempModule.getModule_code() + "' class='kt-subheader__breadcrumbs-link'>" + tempModule.getModule_name() + "</a>");
                            } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_5)) {
                                strBufBottom.insert(0, "<li class='breadcrumb-item'><a href='loadMainPageLogin?moduleCode=" + tempModule.getModule_code() + "'>" + tempModule.getModule_name() + "</a></li>");
                            }
                            //Added by Delvene @ 13-FEb-2014 :: Differentiate ESS and Default breadcrumb - END

                            tempModule = tempModule.getParentModule();
                        }

                        // Current Module
                        //Added by Delvene @ 13-FEb-2014 :: Differentiate ESS and Default breadcrumb
    //                    if(systemType_.equals(SystemConstants.SYSTEM_TYPE.ESS)) {//Change to EQP 29-Aug-2016
//                        strBufBottom.append("<li id='bread_selected'><a href='loadMainPageLogin?moduleCode=" + selectedModule.getModule_code() + "'>" + selectedModule.getModule_name() + "</a></li>");
                        //Added by Delvene @ 13-FEb-2014 :: Differentiate ESS and Default breadcrumb - END

                    } else {
                        //System.out.println("not module");
                        // Loop selectedApp parent's parent
                        Module tempModule = selectedApp.getAttachedModule();
                        Module appModule = tempModule;
//                        strBufBottom.insert(0, "<li><a href='loadMainPageLogin?moduleCode=" + appModule.getModule_code() + "'>" + appModule.getModule_name() + "</a></li>");
                        while (tempModule.getParentModule() != null) {
                            //Added by Delvene @ 13-FEb-2014 :: Differentiate ESS and Default breadcrumb
    //                        if(systemType_.equals(SystemConstants.SYSTEM_TYPE.ESS)) {//Change to EQP 29-Aug-2016
                            //System.out.println("system type ="+"<li><a href='loadMainPageLoginSPA?moduleCode="+tempModule.getParentModule().getModule_code()+"'>"+tempModule.getParentModule().getModule_name()+"</a></li>" );
                            if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_3)) {
                                strBufBottom.insert(0, "<li><a href='loadMainPageLogin?moduleCode=" + tempModule.getParentModule().getModule_code() + "'>" + tempModule.getParentModule().getModule_name() + "</a></li>");
                            } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_4)) {
                                strBufBottom.insert(0, "<span class='kt-subheader__breadcrumbs-separator'></span><a href='loadMainPageLogin?moduleCode=" + tempModule.getParentModule().getModule_code() + "' class='kt-subheader__breadcrumbs-link'>" + tempModule.getParentModule().getModule_name() + "</a>");
                            } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_5)) {
                                strBufBottom.insert(0, "<li class='breadcrumb-item'><a href='loadMainPageLogin?moduleCode=" + tempModule.getParentModule().getModule_code() + "'>" + tempModule.getParentModule().getModule_name() + "</a></li>");
                            }
                            //Added by Delvene @ 13-FEb-2014 :: Differentiate ESS and Default breadcrumb - END

                            tempModule = tempModule.getParentModule();
                        }
                        // Current Module
                        // Added by Delvene @ 08-Aug-2014 :: Don't show module in breadcrumb if only 1 application
                        String strSql = "select app.APPLICATION_ID from t_setup_user us "
                                + "inner join t_setup_group_user gu on us.US_ID = gu.US_ID "
                                + "inner join t_setup_group_app ga on gu.UG_ID = ga.UG_ID "
                                + "inner join t_setup_application app on ga.APPLICATION_ID = app.APPLICATION_ID "
                                + "where app.ATTACHED_MODULE_ID = '" + selectedApp.getAttachedModule().getID() + "' and "
                                + (((ActionContext.getContext().getSession().get("userId")) instanceof Integer)?"us.US_ID = " + ActionContext.getContext().getSession().get("userId") + " ":"us.US_ID = '" + (String) ActionContext.getContext().getSession().get("userId") + "'")
                                + " and app.HIDDEN = 'N'";
                        //System.out.println("breadcrumb sql "+strSql);
                        List resultList = new CommonFunction().getListFromSqlWithSession(retDAO.getSession(), strSql, null);
    //                    System.out.println("resultList.size(): " + resultList.size());
                        // Added by Delvene @ 08-Aug-2014 :: Don't show module in breadcrumb if only 1 application
                        if (resultList.size() >= 1) {
                            //Added by Delvene @ 13-FEb-2014 :: Differentiate ESS and Default breadcrumb
    //                        if(systemType_.equals(SystemConstants.SYSTEM_TYPE.ESS)) {//Change to EQP 29-Aug-2016
                            //System.out.println("current system type ="+"<li><a href='loadMainPageLoginSPA?moduleCode="+selectedApp.getAttachedModule().getModule_code()+"'>"+selectedApp.getAttachedModule().getModule_name()+"</a></li>" );
                            if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_3)) {
                                strBufBottom.append("<li><a href='loadMainPageLogin?moduleCode=" + selectedApp.getAttachedModule().getModule_code() + "'>" + selectedApp.getAttachedModule().getModule_name() + "</a></li>");
                            } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_4)) {
                                strBufBottom.append("<span class='kt-subheader__breadcrumbs-separator'></span><a href='loadMainPageLogin?moduleCode=" + selectedApp.getAttachedModule().getModule_code() + "' class='kt-subheader__breadcrumbs-link'>" + selectedApp.getAttachedModule().getModule_name() + "</a>");
                            } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_5)) {
                                strBufBottom.append("<li class='breadcrumb-item'><a href='loadMainPageLogin?moduleCode=" + selectedApp.getAttachedModule().getModule_code() + "'>" + selectedApp.getAttachedModule().getModule_name() + "</a></li>");
                            }
                            //Added by Delvene @ 13-FEb-2014 :: Differentiate ESS and Default breadcrumb - END
                        }

                        // Added by Delvene @ 21-Apr-2015 :: Provide additional link to report list if application is report
                        if (actionClass.equals("DynamicRptAction")) {
                            String rptAppCode = request.getParameter("rptApp");
                            if (!Validator.isEmpty(rptAppCode)) {
                                Application reportApp = (Application) retDAO.getModelByCode("application_code", rptAppCode, new Application());
                                if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_3)) {
                                    strBufBottom.append("<li><a href='" + reportApp.getAction_name() + "'>" + getAppName_final(reportApp) + "</a></li>");
                                } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_4)) {
                                    strBufBottom.append("<span class='kt-subheader__breadcrumbs-separator'></span><a href='" + reportApp.getAction_name() + "' class='kt-subheader__breadcrumbs-link'>" + getAppName_final(reportApp) + "</a>");
                                } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_5)) {
                                    strBufBottom.append("<li class='breadcrumb-item'><a href='" + reportApp.getAction_name() + "'>" + getAppName_final(reportApp) + "</a></li>");
                                }
                            }
                        }
                        // Added by Delvene @ 21-Apr-2015 :: Provide additional link to report list if application is report - END

                        // Current Application
                        //System.out.println("current app = "+"<li id='bread_selected'><a href='"+selectedApp.getAction_name()+"'  class='active'>"+selectedApp.getApplication_name()+"</a></li>");
                        if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_3)) {
                            strBufBottom.append("<li id='bread_selected'><a href='" + selectedApp.getAction_name() + "'  class='active'>" + getAppName_final(selectedApp) + "</a></li>");
                        } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_4)) {
                            strBufBottom.append("<span class='kt-subheader__breadcrumbs-separator'></span><a id='bread_selected' href='" + selectedApp.getAction_name() + "'  class='kt-subheader__breadcrumbs-link active'>" + getAppName_final(selectedApp) + "</a>");
                        } else if (SystemConstants.SYSTEM_SETUP.UI_FRAMEWORK.equals(SystemConstants.UI_FRAMEWORK_TYPE.BOOTSTRAP_5)) {
                            strBufBottom.append("<li class='breadcrumb-item active' id='bread_selected'><a href='" + selectedApp.getAction_name() + "'>" + getAppName_final(selectedApp) + "</a></li>");
                        }
                    }
                }

                strBuf.append(strBufBottom);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cs_ != null) {
                    retDAO.closeSession();
                }
            }
        }
        return strBuf.toString();
    }

//    public void setMyBreadCrumb_(String myBreadCrumb_) {
//        this.myBreadCrumb_ = myBreadCrumb_;
//    }
    private String cs_;

    public void setCs_(String cs) {
        this.cs_ = cs;
    }

    public String getCs_() {
        return cs_;
    }

    public String getBreadAppCode_() {
        return breadAppCode_;
    }
    public void setBreadAppCode_(String breadAppCode_) {
        this.breadAppCode_ = breadAppCode_;
    }

    public String getDefinedBreadCrumb() {
        return definedBreadCrumb;
    }
    public void setDefinedBreadCrumb(String definedBreadCrumb) {
        this.definedBreadCrumb = definedBreadCrumb;
    }

    
    // Modified by ThoTH @  28-Jan-2014 :: Use own DAO, to avoid saving other unuse Model during Commit, e.g. EmployeeModel which will then connect to FTPS
    // ThoTH @ 22-Apr-2014 :: Support LoginAction
    protected void auditLogin(String loginAction, String loginType, String loginStatus, String loginId) {
        new CommonFunction().auditLogin(loginAction, loginType, loginStatus, loginId);
        //thensw@28Apr2012: move the method to CommonFunction (for SessionTimeoutListener to share the function)
        /*
//    protected void auditLogin(String loginType, String loginStatus, String loginId, BaseDAO basedao) {
        
        // Not need log if the loginId is empty
        
        BaseDAO<AuditLoginModel> basedao = new BaseDAOImpl();
        try {
            if (loginAction.equals(SystemConstants.AUDIT_LOGIN.ACTION.LOGOUT)) {
                loginId = (String) ActionContext.getContext().getSession().get("loginId");
                if (Validator.isEmpty(loginId)) return;
            }
            
            AuditLoginModel auditLogin = new AuditLoginModel();

            auditLogin.setID(CommonFunction.getId(20));
            auditLogin.setLoginaction(loginAction);
            auditLogin.setLogintype(loginType);
            auditLogin.setLoginstatus(loginStatus);
            auditLogin.setLoginid(loginId);
            auditLogin.setLogintime(DateUtil.getCurrentTimestamp());


            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            //ThOTH @ 17-Feb-2014  :: is client behind something?
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            auditLogin.setLoginip(ipAddress);
//            auditLogin.setLoginip((String)request.getRemoteAddr());

            //basedao.insert(auditLogin);
            auditLogin.setID(com.sains.framework.base.CommonFunction.getId(CommonFunction.PK_LENGTH));
            basedao.beginBatchTransaction();
            basedao.getSession().save(auditLogin);
            basedao.commitBatchTransaction();
        } catch (Exception e) {
            basedao.rollbackBatchTransaction();
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
            basedao.closeSession();
        }*/
    }

//    public void checkEditMode_system(ModelBase recordModel) throws Exception {
//        try {
//            PtEstablishmentModel est = null;
//            String strRecordDeptId = "";
//
//            String strSecuLevel   = (String) ActionContext.getContext().getSession().get("secuLevel");
//            if (strSecuLevel.equals(SystemConstants.ACCESS_TYPE.SAINS) || strSecuLevel.equals(SystemConstants.ACCESS_TYPE.Sarawak)) return;
//
//            if (recordModel instanceof PtApplicationModel){
//                strRecordDeptId = ((PtApplicationModel)recordModel).getDept_id();
//
//            } else if (recordModel instanceof RmApplicationModel){
//                strRecordDeptId = ((RmApplicationModel)recordModel).getDept_id();
//
//            } else if (PostHolderBase.class.isAssignableFrom(recordModel.getClass()) || (this instanceof PostHolderAction)){
//                if (recordModel instanceof EmployeeModel)   return;
//
//                est = ((PostBase)recordModel).getRecordEst();
//                if (recordModel instanceof PtPostLantikModel) {
//                    strRecordDeptId = est.getEst_dept_id();
//                } else {
//                    strRecordDeptId = est.getEst_dept_id();
//                }
//
//            } else if (PRBase.class.isAssignableFrom(recordModel.getClass())){
//                est = ((PRBase)recordModel).getEmployeeModel().getPersonalPostList().get(0).getPostOperation().getEstablishment_bu();
//                strRecordDeptId = est.getEst_dept_id();
//
//            } else if (PostInfoBase.class.isAssignableFrom(recordModel.getClass())){
//                est = ((PostBase)recordModel).getRecordEst();
//                strRecordDeptId = est.getEst_dept_id();
//
//            } else if (PostOperationBase.class.isAssignableFrom(recordModel.getClass())){
//                est = ((PostBase)recordModel).getRecordEst();
//                strRecordDeptId = est.getEst_dept_id();
//
//            } else {
//                System.out.println("NOT NEED CHECK");
//                return;
//            }
//
//            if (! ((String)ActionContext.getContext().getSession().get("secuDefaultDept")).equals(strRecordDeptId)) {
//                editMode_ = Boolean.FALSE;
//            }
//        } catch (Exception e) {
//        }
//    }
    public Boolean getEditMode_() {
        return editMode_;
    }

    public void setEditMode_(Boolean editMode_) {
        this.editMode_ = editMode_;
    }

    // **************** SECURITY CHECK <START> ****************** //
    // ThoTH @ 25-Feb-2014
    public Boolean canView(ModelBase recordModel) throws Exception {
//        if (!checkCanView_) {
//            return Boolean.TRUE;
//        }
        Debug.printFrameworkDebug("inside CanView....");
        CommonFunction cf = new CommonFunction();
        String sql = null;
        String strSecureType = "";
        String strViewingRecord = "";

        Map secuMap = new HashMap();
//        String strSecuLevel   = (String) ActionContext.getContext().getSession().get("secuLevel");
//        if (strSecuLevel.equals(SystemConstants.ACCESS_TYPE.SAINS) || strSecuLevel.equals(SystemConstants.ACCESS_TYPE.Sarawak)) return Boolean.TRUE;

        Boolean byPassCanViewIfNoSecurityBeenSetup = Boolean.FALSE;

        if (recordModel instanceof ParentModel){
            if (Validator.isEmpty(((ParentModel)recordModel).getID())) { //empty ID = new records = create
                return Boolean.TRUE;
            }
            Map param = new HashMap();
            param.put("parentId", ((ParentModel)recordModel).getID());
            param.put("createdBy", ((ParentModel)recordModel).getLoginId());
            if (baseDAO.sqlCountRecord("select count(*) from t_parent where parent_id = :parentId and created_by = :createdBy", param) <= 0) {
                return Boolean.FALSE;
            }
        }

//        if (recordModel instanceof SpaApplicationModel){
//            if (this instanceof DcAppPublicAction) {
//                
//                sql = "select count(*) from spa_app_user appUser "
//                       + "where appUser.app_id = '"+ recordModel.getID() +"' and us_id = '"+ recordModel.getLoginUserId() +"'";
//                System.out.println("temporary allow view");
//                return Boolean.TRUE;
//           }
//        }
        if (sql != null) {
            Long recordCount = baseDAO.sqlCountRecord(sql, null);
            if (recordCount <= 0) {
                return Boolean.FALSE;
            }
        }

//            // PostInfoTemp and PostOperationTemp shall check agains their Applications Dept_id_rec.
//            if (recordModel instanceof PtPostInfoTempModel) {
//                est.setEst_dept_id(((PtPostInfoTempModel)recordModel).getPtApplication().getDept_id_rec());
//            } else if (recordModel instanceof PtPostOperationTempModel) {
//                est.setEst_dept_id(((PtPostOperationTempModel)recordModel).getPtApplication().getDept_id_rec());
//            } else {
//                est.setEst_dept_id(((PtApplicationModel)recordModel).getDept_id_rec());
//            }
//            if (Validator.isEmpty(est.getEst_dept_id()))    return Boolean.TRUE;  // this happen when is Add New Record
//            strSecureType = "MOHONAN";
//            sql = "SELECT count(*) FROM T_PT_APPLICATION app WHERE ";
//            strViewingRecord = " app.dept_id_rec = '" + est.getEst_dept_id() + "' ";
//            
//
//        } else if (recordModel instanceof RmApplicationModel){
//            est.setEst_dept_id(((RmApplicationModel)recordModel).getDept_id_rec());
//            if (Validator.isEmpty(est.getEst_dept_id()))    return Boolean.TRUE;  // this happen when is Add New Record
//            strSecureType = "MOHONAN";
//            sql = "SELECT count(*) FROM T_RM_APPLICATION app WHERE ";
//            strViewingRecord = " app.dept_id_rec = '" + est.getEst_dept_id() + "' ";
//
//        } else if (PostHolderBase.class.isAssignableFrom(recordModel.getClass()) || (this instanceof PostHolderAction)){
//            // Because PostHolderActions's model is EmployeeModel
//            if (recordModel instanceof EmployeeModel)   return true;
//
//            strSecureType = "PostHolder";
//            sql = "SELECT count(*) FROM t_employee emp " +
//                  "       left outer join t_pt_post_lantik lantik on emp.employee_id = lantik.employee_id     " +
//                  "       left outer join t_pt_post_info postInfo on lantik.post_info_id = postInfo.post_info_id " +
//                  "       left outer join t_pt_establishment ba on postInfo.est_id = ba.est_id    " +
//                  "       left outer join t_cm_personal_post pp on emp.employee_id = pp.employee_id  " +
//                  "       left outer join t_pt_post_operation postOper on pp.post_oper_id = postOper.post_oper_id " +
//                  "       left outer join t_pt_establishment bu on postOper.est_id = bu.est_id WHERE ";
//            est = ((PostBase)recordModel).getRecordEst();
//
//            if (recordModel instanceof PtPostLantikModel) {
//                strViewingRecord = " ba.est_dept_id = '" + est.getEst_dept_id() + "' ";
//                blnUseBA = Boolean.TRUE;
//            } else {
//                strViewingRecord = " bu.est_dept_id = '" + est.getEst_dept_id() + "' ";
//            }
//
//        } else if (PRBase.class.isAssignableFrom(recordModel.getClass())){
//            try {
//                if (((PRBase)recordModel).getEmployeeModel().getPr_sub_status().startsWith("5")) {
//                    System.out.println("New Employee :: SKIP");
//                    return Boolean.TRUE; // ThoTH @ 5-Mar-2014 :: Skip when is New Employee
//                }
//                est = ((PRBase)recordModel).getEmployeeModel().getPersonalPostList().get(0).getPostOperation().getEstablishment_bu();
//            } catch (Exception e) {
//                try {
//                    EmployeeModel empLoad = (EmployeeModel)baseDAO.getObjectById(((PRBase)recordModel).getEmployee_id(), EmployeeModel.class);
//                    est = empLoad.getPersonalPostList().get(0).getPostOperation().getEstablishment_bu();
//                } catch (Exception e2) {
//                    System.out.println("$$$$$ Employee Doesn't have BU $$$$$$$ ");
//                    return Boolean.FALSE;
//                }
//
//            }
////        if (strModel.equals("PRBase")) {
//            strSecureType = "PRHR";
//
//            sql = "SELECT count(*) FROM T_EMPLOYEE emp " + 
//                  "  left outer join t_pt_post_lantik lantik on emp.employee_id = lantik.employee_id " +
//                  "  left outer join t_pt_post_info postInfo on lantik.post_info_id = postInfo.post_info_id " +
//                  "  left outer join t_pt_establishment ba on postInfo.est_id = ba.est_id " +
//                  "  left outer join t_cm_personal_post pp on emp.employee_id = pp.employee_id " +
//                  "  left outer join t_pt_post_operation po on pp.post_oper_id = po.post_oper_id " +
//                  "  left outer join t_pt_establishment bu on po.est_id = bu.est_id " + 
//                  " WHERE ";
//
//            CommonFunction.getSecuCondMap(baseDAO.getSession(), 
//                    CommonFunction.retrieveGroupSecurityList(baseDAO.getSession(), "action_class", this.getClass().getSimpleName(), (String)ActionContext.getContext().getSession().get("userId")), 
//                    (String)ActionContext.getContext().getSession().get("secuDefaultDept"), 
//                    secuMap);
////            CommonFunction.getSecuCondMap(baseDAO.getSession(), 
////                    CommonFunction.retrieveGroupSecurityList(baseDAO.getSession(), "application_code", strSecureType, (String)ActionContext.getContext().getSession().get("userId")), 
////                    (String)ActionContext.getContext().getSession().get("secuDefaultDept"), 
////                    secuMap);
//            secuMap.put("loaded", "loaded"); //for checking below, no need to reload this map if already loaded here.
//            String strSecuLevel = (String)secuMap.get("secuLevel");
//            if (strSecuLevel.equals(SystemConstants.ACCESS_TYPE.Department)) {
//                strViewingRecord = " innerEst.est_dept_id = '" + est.getEst_dept_id() + "' ";
//            } else {
//                strViewingRecord = " innerEst.est_id = '" + est.getEst_id() + "' ";
//            }
//
//        } else if (PostInfoBase.class.isAssignableFrom(recordModel.getClass())){
//            blnUseBA = Boolean.TRUE;
//            
//            strSecureType = "PostInfo";
//            sql = "SELECT count(*) FROM T_PT_POST_INFO post WHERE ";
//
//            est = ((PostBase)recordModel).getRecordEst();
//            if (est == null) {
//                // These tables are shared by PostName and PostInfo, when it is null, means it is link to PostName, so
//                // Not Need to do Security Check, just return TRUE
//                if (recordModel instanceof PtPostTaskModel || recordModel instanceof PtPostAuthScopeModel ||
//                    recordModel instanceof PtPostCompetencyModel || recordModel instanceof PtPostLogisticalModel ||
//                    recordModel instanceof PtPostKraModel || recordModel instanceof PtPostKpiModel ||
//                    recordModel instanceof PtPostCriteriaModel) {
//
//                    return Boolean.TRUE;
//                }
//            }
//
//            strViewingRecord = " innerEst.est_dept_id = '" + est.getEst_dept_id() + "' ";
//
//        } else if (PostOperationBase.class.isAssignableFrom(recordModel.getClass())){
//            strSecureType = "PostOperation";
//            sql = "SELECT count(*) FROM T_PT_POST_OPERATION post WHERE ";
//            est = ((PostBase)recordModel).getRecordEst();
//            strViewingRecord = " innerEst.est_dept_id = '" + est.getEst_dept_id() + "' ";
//
//        } else if (PostOrgBase.class.isAssignableFrom(recordModel.getClass())){
//            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");
//            strSecureType = "Department";
//            sql = "SELECT count(*) FROM T_PT_Department dept WHERE ";
//            
//            // ThoTH @ 21-Oct-2014
//            // Edited by Delvene @ 13-May-2015 :: est.getDepartment() hit NullPointerException, retrieve PtEstablishmentModel again in catch to solve it.
//            try {
//                est = ((PostBase)recordModel).getRecordEst();
//                strViewingRecord = " dept_id = '" + est.getEst_dept_id() + "' ";
//            
//                if (est.getDepartment().getDept_set().equalsIgnoreCase("a")) {
//                    strSecureType = "DepartmentBA";
//                    blnUseBA = Boolean.TRUE;
//                }
//            } catch (Exception e) {
//                try {
//                    PtEstablishmentModel estLoad = (PtEstablishmentModel)baseDAO.getObjectById(((PostBase)recordModel).getID(), PtEstablishmentModel.class);
//                    strViewingRecord = " dept_id = '" + estLoad.getEst_dept_id() + "' ";
//                
//                    if (estLoad.getDepartment().getDept_set().equalsIgnoreCase("a")) {
//                        strSecureType = "DepartmentBA";
//                        blnUseBA = Boolean.TRUE;
//                    }
//                } catch (Exception e2) {
//                    System.out.println("$$$$$ NOT ABLE TO LOAD est.getDepartment() $$$$$$$ ");
//                    return Boolean.FALSE;
//                }
//            }
//            // Edited by Delvene @ 13-May-2015 :: est.getDepartment() hit NullPointerException, retrieve PtEstablishmentModel again in catch to solve it. - END
//
//        } else if (RecruitBase.class.isAssignableFrom(recordModel.getClass())){
//            est.setEst_dept_id(((RecruitBase)recordModel).getRecordApp().getDept_id_rec());
////            if (Validator.isEmpty(est.getEst_dept_id()))    return Boolean.TRUE;  // this happen when is Add New Record
//            strSecureType = "MOHONAN";
//            sql = "SELECT count(*) FROM T_RM_APPLICATION app WHERE ";
//            strViewingRecord = " app.dept_id = '" + est.getEst_dept_id() + "' ";
//
//        } else if (TOSBase.class.isAssignableFrom(recordModel.getClass())){ // ChangMH @ 02-Apr-2015 :: TOS
//            CommonFunction.getSecuCondMap(baseDAO.getSession(), 
//                    CommonFunction.retrieveGroupSecurityList(baseDAO.getSession(), "action_class", this.getClass().getSimpleName(), (String)ActionContext.getContext().getSession().get("userId")), 
//                    (String)ActionContext.getContext().getSession().get("secuDefaultDept"), 
//                    secuMap);
//            secuMap.put("loaded", "loaded"); 
//            String strSecuLevel = (String)secuMap.get("secuLevel");
//            if (strSecuLevel.equals(SystemConstants.ACCESS_TYPE.Ministry)) {
//                return Boolean.TRUE; 
//            }
//            
//            try {
//                if (((TOSBase)recordModel).getTravelModel().getTr_sub_status().startsWith(TravelModel.TRAVEL_STATUS.A1_New)) {
//                    return Boolean.TRUE; 
//                }
//                est = ((TOSBase)recordModel).getTravelModel().getEmployee().getPersonalPostList().get(0).getPostOperation().getEstablishment_bu();
//            } catch (Exception e) {
//                try {
//                    EmployeeModel empLoad = (EmployeeModel)baseDAO.getObjectById(((TOSBase)recordModel).getTravelModel().getEmployee_id(), EmployeeModel.class);
//                    est = empLoad.getPersonalPostList().get(0).getPostOperation().getEstablishment_bu();
//                } catch (Exception e2) {
//                    System.out.println("$$$$$ Employee Doesn't have BU $$$$$$$ ");
//                    return Boolean.FALSE;
//                }
//
//            }
//            
//            strSecureType = "PRHR";
//
//            sql = "SELECT count(*) FROM T_TRAVEL tos " + 
//                  "  inner join t_employee emp on emp.employee_id = tos.employee_id " +
//                  "  left outer join t_pt_post_lantik lantik on emp.employee_id = lantik.employee_id " +
//                  "  left outer join t_pt_post_info postInfo on lantik.post_info_id = postInfo.post_info_id " +
//                  "  left outer join t_pt_establishment ba on postInfo.est_id = ba.est_id " +
//                  "  left outer join t_cm_personal_post pp on emp.employee_id = pp.employee_id " +
//                  "  left outer join t_pt_post_operation po on pp.post_oper_id = po.post_oper_id " +
//                  "  left outer join t_pt_establishment bu on po.est_id = bu.est_id " + 
//                  " WHERE ";
//
//            strViewingRecord = " innerEst.est_dept_id = '" + est.getEst_dept_id() + "' ";
//            
//        } else if (LeaveBase.class.isAssignableFrom(recordModel.getClass())){ // ThoTH @ 23-Sep-2015 :: Leave
//            // ThoTH @ 6-Oct-2015 :: Since Leave_Verify is assigned to SelfService Group, and cannot give Sarawak Access to it,
//            //                       so need to do some specific checking here
//            //                       Means if user has the job, then he can View it
//            // ThoTH @ 2-Dec-2015 :: Cater for affected Trans, and enhance on the Job checking (compare the pod_id to lt_id
//            System.out.println("###### " + recordModel.getClass());
//            
//            if (((LeaveBase)recordModel).get_byPassCanView())  return Boolean.TRUE;
//            
//            LmTransModel viewTrans = new LmTransModel();
//            if (recordModel instanceof LmMainModel) {
//                viewTrans = ((LmMainModel)recordModel).getTransList().get(0);
//            } else if (recordModel instanceof LmTransModel) {
//                viewTrans = (LmTransModel)recordModel;
//            }
//            if (viewTrans.getID() != null) {    
//                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//                String dCode = request.getParameter("dCode");
//                if (Validator.isEmpty(dCode)) {  // ThoTH @ 1-Mar-2016 :: To handle open from Email
//                    String str = request.getParameter("emailID");
//                    if (! Validator.isEmpty(str)) {
//                        JobPoolModel pool = (JobPoolModel) baseDAO.getObjectById(str, JobPoolModel.class);
//                        dCode = pool.getJobProgressList().get(0).getID();
//                    }
//                }
//                if (! Validator.isEmpty(dCode)) {
//                    JobProgressModel jp = (JobProgressModel) baseDAO.getObjectById(dCode, JobProgressModel.class);
//                    if (jp != null) {
//                        String sesUserId = (String)ActionContext.getContext().getSession().get("userId");
//                        if (sesUserId.equals(jp.getJp_assign_to())) {
//                            if (jp.getJobpool().getPod_id().equals(viewTrans.getID())) return Boolean.TRUE;
//                        }
//                    }
//                }
//            }
//            
//            CommonFunction.getSecuCondMap(baseDAO.getSession(), 
//                    CommonFunction.retrieveGroupSecurityList(baseDAO.getSession(), "action_class", this.getClass().getSimpleName(), (String)ActionContext.getContext().getSession().get("userId")), 
//                    (String)ActionContext.getContext().getSession().get("secuDefaultDept"), 
//                    secuMap);
//            secuMap.put("loaded", "loaded"); 
//            
//            try {
//                est = ((LeaveBase)recordModel).getLmMain().getEmployee().getPersonalPostList().get(0).getPostOperation().getEstablishment_bu();
//            } catch (Exception e) {
//                try {
//                    if (((LeaveBase)recordModel).getLmMain() == null) System.out.println("lm main is null");
//                    System.out.println(" hohohoho " + ((LeaveBase)recordModel).getLmMain().getEmployee_id());
//                    EmployeeModel empLoad = (EmployeeModel)baseDAO.getObjectById(((LeaveBase)recordModel).getLmMain().getEmployee_id(), EmployeeModel.class);
//                    est = empLoad.getPersonalPostList().get(0).getPostOperation().getEstablishment_bu();
//                } catch (Exception e2) {
//                    System.out.println("$$$$$ Employee Doesn't have BU $$$$$$$ ");
//                    return Boolean.FALSE;
//                }
//            }
//            
//            strSecureType = "PRHR";
//
//            sql = "SELECT count(*) FROM T_LM_MAIN lm " + 
//                  "  inner join t_employee emp on emp.employee_id = lm.employee_id " +
//                  "  left outer join t_pt_post_lantik lantik on emp.employee_id = lantik.employee_id " +
//                  "  left outer join t_pt_post_info postInfo on lantik.post_info_id = postInfo.post_info_id " +
//                  "  left outer join t_pt_establishment ba on postInfo.est_id = ba.est_id " +
//                  "  left outer join t_cm_personal_post pp on emp.employee_id = pp.employee_id " +
//                  "  left outer join t_pt_post_operation po on pp.post_oper_id = po.post_oper_id " +
//                  "  left outer join t_pt_establishment bu on po.est_id = bu.est_id " + 
//                  " WHERE ";
//
//            String strSecuLevel = (String)secuMap.get("secuLevel");
//            if (strSecuLevel.equals(SystemConstants.ACCESS_TYPE.Ministry)) {
//                strViewingRecord = " (innerEst.est_dept_id = '" + est.getEst_dept_id() + "' " +
//                                   " or innerEst.est_dept_id in (select deptMin.dept_id from t_pt_department deptMin where ministry_id = '"+est.getEst_dept_id()+"')) ";
//            } else if (strSecuLevel.equals(SystemConstants.ACCESS_TYPE.Department)) {
//                strViewingRecord = " innerEst.est_dept_id = '" + est.getEst_dept_id() + "' ";
//            } else {
//                strViewingRecord = " innerEst.est_id = '" + est.getEst_id() + "' ";
//            }
//            System.out.println("strViewingRecord : " + strViewingRecord);
//       } else if (HartaBase.class.isAssignableFrom(recordModel.getClass())){ // Bernard @ 18-JUNE-2015 :: HARTA
//            try {
//                System.out.println("HARTA  BASE ACTION");
//                if (((PRBase)recordModel).getEmployeeModel().getPr_sub_status().startsWith("5")) {
//                    System.out.println("HARTA New Employee :: SKIP");
//                    return Boolean.TRUE; // ThoTH @ 5-Mar-2014 :: Skip when is New Employee
//                }
//                est = ((PRBase)recordModel).getEmployeeModel().getPersonalPostList().get(0).getPostOperation().getEstablishment_bu();
//            } catch (Exception e) {
//                try {
//                    EmployeeModel empLoad = (EmployeeModel)baseDAO.getObjectById(((PRBase)recordModel).getEmployee_id(), EmployeeModel.class);
//                    est = empLoad.getPersonalPostList().get(0).getPostOperation().getEstablishment_bu();
//                } catch (Exception e2) {
//                    System.out.println("$$$$$ HARTA Employee Doesn't have BU $$$$$$$ ");
//                    return Boolean.FALSE;
//                }
//
//            }
////        if (strModel.equals("PRBase")) {
//            strSecureType = "PRHR";
//
//            sql = "SELECT count(*) FROM T_EMPLOYEE emp " + 
//                  "  left outer join t_pt_post_lantik lantik on emp.employee_id = lantik.employee_id " +
//                  "  left outer join t_pt_post_info postInfo on lantik.post_info_id = postInfo.post_info_id " +
//                  "  left outer join t_pt_establishment ba on postInfo.est_id = ba.est_id " +
//                  "  left outer join t_cm_personal_post pp on emp.employee_id = pp.employee_id " +
//                  "  left outer join t_pt_post_operation po on pp.post_oper_id = po.post_oper_id " +
//                  "  left outer join t_pt_establishment bu on po.est_id = bu.est_id " + 
//                  " WHERE ";
//
//            CommonFunction.getSecuCondMap(baseDAO.getSession(), 
//                    CommonFunction.retrieveGroupSecurityList(baseDAO.getSession(), "action_class", this.getClass().getSimpleName(), (String)ActionContext.getContext().getSession().get("userId")), 
//                    (String)ActionContext.getContext().getSession().get("secuDefaultDept"), 
//                    secuMap);
////            CommonFunction.getSecuCondMap(baseDAO.getSession(), 
////                    CommonFunction.retrieveGroupSecurityList(baseDAO.getSession(), "application_code", strSecureType, (String)ActionContext.getContext().getSession().get("userId")), 
////                    (String)ActionContext.getContext().getSession().get("secuDefaultDept"), 
////                    secuMap);
//            secuMap.put("loaded", "loaded"); //for checking below, no need to reload this map if already loaded here.
//            String strSecuLevel = (String)secuMap.get("secuLevel");
//            if (strSecuLevel.equals(SystemConstants.ACCESS_TYPE.Department)) {
//                strViewingRecord = " innerEst.est_dept_id = '" + est.getEst_dept_id() + "' ";
//            } else {
//                strViewingRecord = " innerEst.est_id = '" + est.getEst_id() + "' ";
//            }
//       } else {
//            System.out.println("NOT NEED CHECK");
//            return Boolean.TRUE;
//        }
        // ThoTH @ 27-Feb-2014 :: Override the EditMode here
//        System.out.println("$$$$$$$$$$$$ Original editMode_ :" + editMode_);
//        if (blnUseBA) {  // ThoTH @ 21-Oct-2014
//            if (! ((String)ActionContext.getContext().getSession().get("secuDeptBA")).equals(est.getEst_dept_id())) {
//                System.out.println(((String)ActionContext.getContext().getSession().get("secuDeptBA")) + " vs " + est.getEst_dept_id());
//                editMode_ = Boolean.FALSE;
//            }
//            
//        } else {
//            if (! ((String)ActionContext.getContext().getSession().get("secuDefaultDept")).equals(est.getEst_dept_id())) {
//                System.out.println(((String)ActionContext.getContext().getSession().get("secuDefaultDept")) + " vs " + est.getEst_dept_id());
//                editMode_ = Boolean.FALSE;
//            }
//        }
        // Check View Access Right
        /*SereneC @ 30/6/2016 ---start comment
        String strSecureSql = CommonFunction.appendSecurityCondition(baseDAO.getSession(), this.getClass().getSimpleName(), strSecureType, "", CommonFunction.SecurityConditionLocation.AIRetrieve, strViewingRecord, secuMap);
        sql += strSecureSql;
        
        String strSecuLevel = (String)secuMap.get("secuLevel");
        if (strSecuLevel.equals(SystemConstants.ACCESS_TYPE.Unit)) sql = (String) secuMap.get("secuSqlHead") + sql;
        sql = CommonFunction.insertNoLock(sql); // ThoTH @ 26-May-2015
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$ sql : " + sql);

        String strResult = cf.getSingleValueWithSession(baseDAO.getSession(), "", sql, "");
        if (Integer.parseInt(strResult) > 0) return Boolean.TRUE;
//
        return Boolean.FALSE;
        // ---end comment*/
        return Boolean.TRUE;//SereneC @ 30/6/2016 ---change to TRUE
    }

    private ModelBase currentViewModel_ = null;
    public Object getCurrentViewModel_() {
        if (currentViewModel_ == null) {
            return getModel();
        }
        return currentViewModel_;
    }
    public void setCurrentViewModel_(ModelBase currentViewModel_) {
        this.currentViewModel_ = currentViewModel_;
    }
    
    public Object getModel() {
        return null;
    }

    public String getModelPackage_() {
        return getModel().getClass().getPackage().toString().substring(8);
    }
    // **************** SECURITY CHECK <END> ****************** //

    // ThoTH @ 16-Apr-2014
    @Override
    public Collection getActionErrors() {
        if (super.getActionErrors().size() > 0) {
            return super.getActionErrors();
        }

        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        //check the validation message pass over. if got message, show it.
        if (!Validator.isEmpty(request.getParameter("rMsg_"))) {
            String abc = request.getParameter("rMsg_");
            for (String str : abc.split("::")) {
                Debug.printFrameworkInfo(str);
                addActionError(str);
            }
        }

        return super.getActionErrors();
    }

    public Object getJsonData() {
        return jsonData;
    }

    public void setJsonData(Object jsonData) {
        this.jsonData = jsonData;
    }

//    public void jspCheckTime(String location){
//        if ( (Boolean)ActionContext.getContext().getSession().get("isStressTesting") )  {
//            System.out.println(location+ " took                                                        triggerCount::" + ((HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST)).getAttribute("triggerCount") + "                           %%%%%% " + (DateUtil.getCurrentDate().getTime() - ((java.util.Date)((HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST)).getAttribute("startDatetime")).getTime()) );
//        }
//    }
    public String getNotAuthorised() {
        HttpServletRequest req = ((HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST));
        if (req.getAttribute("not_authorised") != null) {
            return req.getAttribute("not_authorised").toString();
        }
        return "";
    }

    protected String errorAtJSP_ = null; // thensw @ 12-Aug-2014

    public String getErrorAtJSP_() {
        return errorAtJSP_;
    }

    public void setErrorAtJSP_(String errorAtJSP_) {
        this.errorAtJSP_ = errorAtJSP_;
    }

    public String getCommStr_() {
        return commStr_;
    }

    public void setCommStr_(String commStr_) {
        this.commStr_ = commStr_;
    }

    public String getMobile_text_() {
        return mobile_text_;
    }

    public void setMobile_text_(String mobile_text_) {
        this.mobile_text_ = mobile_text_;
    }

    List actionInfo = null;

    public List getActionInfo() {
        return actionInfo;
    }

    public void addActionInfo(String icon, String message) {
        if (actionInfo == null) {
            actionInfo = new ArrayList();
        }
        actionInfo.add(new Options(icon, message)); //reuse Options object to capture the actionInfo's message.
    }

    public void addActionInfo(String message) { //use default icon (information)
        addActionInfo("images//action_icon//info.png", message);
    }

    public String getSecuLevel_() {
        return secuLevel_;
    }

    public void setSecuLevel_(String secuLevel_) {
        this.secuLevel_ = secuLevel_;
    }

    public String getWorkerNum_() {
        if (SystemConstants.AJP_PORT == null) {
            constructAJP_PORT_inChar();
        }
        return SystemConstants.AJP_PORT;
    }

    public void mobile_status(String value) throws Exception {
//        String charset = "utf-8";
//        String contentType = "text/plain";
//        
//        byte[] b = value.getBytes(charset);
//        HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
//
//	res.setContentType(contentType + "; charset=" + charset);
//	res.setContentLength(b.length);
//	try {
//            res.getOutputStream().write(b);
//            res.getOutputStream().flush();
//	} finally {
//            res.getOutputStream().close();	
//	}
        Map m = new HashMap();
        m.put("status", value);
        setJsonData(m);
    }

//    public Boolean checkIsExpiredForApps_() throws Exception {
//        Map sessionMap = ActionContext.getContext().getSession();
//        Boolean exp = Boolean.FALSE;
//        if (sessionMap.get("acting_user_id") != null) {
//            BaseDAO baseDAO = new BaseDAOImpl();
//            try {
//                Query query = baseDAO.getSession().createQuery("select _self from ActMainModel _self where act_status = '" + ActMainModel.ACTING_STATUS.IN_PROGRESS +"' " +
//                    "and act_by_id = '"+ sessionMap.get("acting_user_id") +"'");
//                ActMainModel actMain = (ActMainModel)query.uniqueResult();
//                if (actMain == null) {
//                    sessionMap.clear();
//                    return Boolean.TRUE;
//                }
//            } catch (Exception e) {
//                sessionMap.clear();
//                exp = Boolean.TRUE;
//            } finally {
//                baseDAO.closeSession();
//            }
//        }
//        if (sessionMap.get("userId") == null) {
//            exp = Boolean.TRUE;
//        }
//        if (exp) {
//            mobile_status("expired");
//        }
//        return exp;
//    }
    Boolean isViewerFromRequest = null;

    public Boolean getIsViewerFromRequest() { //isViewer flag from ValidateRights_sql, to identify the current user have only Viewing rights.
        if (isViewerFromRequest == null) {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            isViewerFromRequest = (request.getAttribute("isViewer") != null);
        }
        return isViewerFromRequest;
    }

    public String getRefererUrl_() {
        return refererUrl_;
    }

    public void setRefererUrl_(String refererUrl_) {
        this.refererUrl_ = refererUrl_;
    }

    private String pageTitle_ = null;
    private String pageSubTitle_ = null;
    private String pageTitleNum_ = null;

    public String getPageTitle_() {
        return pageTitle_;
    }

    public void setPageTitle_(String pageTitle_) {
        this.pageTitle_ = pageTitle_;
    }

    public String getPageTitleNum_() {
        return pageTitleNum_;
    }

    public void setPageTitleNum_(String pageTitleNum_) {
        this.pageTitleNum_ = pageTitleNum_;
    }

    public String getPageSubTitle_() {
        return pageSubTitle_;
    }

    public void setPageSubTitle_(String pageSubTitle_) {
        this.pageSubTitle_ = pageSubTitle_;
    }

    private String dialogWidth = "";

    public String getDialogWidth() {
        return dialogWidth;
    }

    public void setDialogWidth(String dialogWidth) {
        this.dialogWidth = dialogWidth;
    }

    private String loadDiv_id;

    public String getLoadDiv_id() {
        return loadDiv_id;
    }

    public void setLoadDiv_id(String loadDiv_id) {
        this.loadDiv_id = loadDiv_id;
    }

    private String loadDiv_name;

    public String getLoadDiv_name() {
        return loadDiv_name;
    }

    public void setLoadDiv_name(String loadDiv_name) {
        this.loadDiv_name = loadDiv_name;
    }

    private String loadDiv_value;

    public String getLoadDiv_value() {
        return loadDiv_value;
    }

    public void setLoadDiv_value(String loadDiv_value) {
        this.loadDiv_value = loadDiv_value;
    }

    public List loadDiv_list = null;

    public List getLoadDiv_list() {
        return loadDiv_list;
    }

    public void setLoadDiv_list(List loadDiv_list) {
        this.loadDiv_list = loadDiv_list;
    }

    private String sBy_ = null; //search value

    public String getsBy_() {
        return sBy_;
    }

    public void setsBy_(String sBy_) {
        this.sBy_ = sBy_;
    }

    private String sValue_ = null; //search value

    public String getsValue_() {
        return sValue_;
    }

    public void setsValue_(String sValue_) {
        this.sValue_ = sValue_;
    }
    private String sNo_ = null; //search value
    private String sYear_ = null; //search value
    private String sName_ = null; //search value

    public String getsNo_() {
        return sNo_;
    }

    public void setsNo_(String sNo_) {
        this.sNo_ = sNo_;
    }

    public String getsYear_() {
        return sYear_;
    }

    public void setsYear_(String sYear_) {
        this.sYear_ = sYear_;
    }

    public String getsName_() {
        return sName_;
    }

    public void setsName_(String sName_) {
        this.sName_ = sName_;
    }

    private String scrPos = null; //search value

    public String getScrPos() {
        Debug.printFrameworkDebug("getting scrPos = " + scrPos);
        return scrPos;
    }

    public void setScrPos(String scrPos) {
        this.scrPos = scrPos;
    }

    public void clearNewModelID() {
        HttpServletRequest currentRequest = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        if (currentRequest.getAttribute("newlyAddedModel") != null) {
            for (ModelBase newModel : (List<ModelBase>) currentRequest.getAttribute("newlyAddedModel")) {
                newModel.setID("");
            }
        }
    }
    
    private Boolean checkCanView_ = Boolean.FALSE;
    public Boolean getCheckCanView_() {
        return checkCanView_;
    }
    public void setCheckCanView_(Boolean checkCanView_) {
        this.checkCanView_ = checkCanView_;
    }

    
    
//    public Boolean getDisabledMode_() {
//        return disabledMode_;
//    }
//
//    public void setDisabledMode_(Boolean disabledMode_) {
//        this.disabledMode_ = disabledMode_;
//    }
//    
    
    BaseCUDDAO daoService_ = null;
    public BaseCUDDAO getDaoService_() {
        if (daoService_ == null) {
            daoService_ = new BaseDAOImpl();
        }
        return daoService_;
    }
    public void setDaoService_(BaseCUDDAO daoService_) {
        this.daoService_ = daoService_;
    }
    
    public void specificValidation(String validationType) throws Exception {
    }
    
    private String bs_format_ = "1";
    public String getBs_format_() {
        return bs_format_;
    }
    public void setBs_format_(String bs_format_) {
        this.bs_format_ = bs_format_;
    }
    
    public String getBs_format1_open_() {
        return "<div class=\"col-lg-4 col-md-4 col-sm-4 col-xs-6\">\n" +
"    <div class=\"form-group form-float\">";
    }
    public String getBs_format1_close_() {
        return "</div>\n" +
"</div>";
    }

    public String getBsHor_tf_open_() {
        return "<div class=\"form-horizontal form-group\">\n" +
"                        <label class=\"col-md-4 checkbox-label\"";
    }
    public String getBsHor_tf_close_() {
        return "</div>";
    }
    private String bsHor_tf_b4Input_ = null;
    public String getBsHor_tf_b4Input_() {
        if (bsHor_tf_b4Input_ == null) {
            bsHor_tf_b4Input_ = "<div class=\"col-md-5\">";
        }
        return bsHor_tf_b4Input_;
    }
    
    private String bsHor_cb_open_ = null;
    public String getBsHor_cb_open_() {
        if (bsHor_cb_open_ == null) {
            bsHor_cb_open_ = "<div class=\"form-horizontal form-group\">\n" +
"                        <label class=\"col-md-4 checkbox-label\"";
        }
        return bsHor_cb_open_;
    }
    
    private String bsHor_cb_close_ = null;
    public String getBsHor_cb_close_() {
        if (bsHor_cb_close_ == null) {
            bsHor_cb_close_ = "</div>\n</div>";
        }
        return bsHor_cb_close_;
    }
    private String bsHor_cb_b4Input_ = null;
    public String getBsHor_cb_b4Input_() {
        if (bsHor_cb_b4Input_ == null) {
            bsHor_cb_b4Input_ = "<div class=\"col-md-5 checkbox right check-success\">";
        }
        return bsHor_cb_b4Input_;
    }
    public void setBsHor_cb_b4Input_(String bsHor_cb_b4Input_) {
        this.bsHor_cb_b4Input_ = bsHor_cb_b4Input_;
    }
    
    public void setBsHor_cb_close_(String bsHor_cb_close_) {
        this.bsHor_cb_close_ = bsHor_cb_close_;
    }

    public void setBsHor_cb_open_(String bsHor_cb_open_) {
        this.bsHor_cb_open_ = bsHor_cb_open_;
    }
    
    private String bsHor_rd_open_ = null;
    public String getBsHor_rd_open_() {
        if (bsHor_rd_open_ == null) {
            bsHor_rd_open_ = "<div class=\"form-horizontal form-group\">\n" +
"                        <label class=\"col-md-4 radio-label\"";
        }
        return bsHor_rd_open_;
    }
    private String bsHor_rd_close_ = null;
    public String getBsHor_rd_close_() {
        if (bsHor_rd_close_ == null) {
            bsHor_rd_close_ = "</div>\n</div>";
        }
        return bsHor_rd_close_;
    }

    private String bsHor_rd_b4Input_ = null;
    public String getBsHor_rd_b4Input_() {
        if (bsHor_rd_b4Input_ == null) {
            bsHor_rd_b4Input_ = "<div class=\"col-md-5 radio radio-inline radio-success\">";
        }
        return bsHor_rd_b4Input_;
    }
    public void setBsHor_rd_b4Input_(String bsHor_rd_b4Input_) {
        this.bsHor_rd_b4Input_ = bsHor_rd_b4Input_;
    }

    private String bsHor_dd_open_ = null;
    public String getBsHor_dd_open_() {
        if (bsHor_dd_open_ == null) {
            bsHor_dd_open_ = "<div class=\"form-horizontal form-group\">\n" +
"                        <label class=\"col-md-4 control-label\"";;
        }
        return bsHor_dd_open_;
    }
    private String bsHor_dd_close_ = null;
    public String getBsHor_dd_close_() {
        if (bsHor_dd_close_ == null) {
            bsHor_dd_close_ = "</div>";
        }
        return bsHor_dd_close_;
    }

    private String bsHor_dd_b4Input_ = null;
    public String getBsHor_dd_b4Input_() {
        if (bsHor_dd_b4Input_ == null) {
            bsHor_dd_b4Input_ = "<div class=\"col-md-5\">";
        }
        return bsHor_dd_b4Input_;
    }
    public void setBsHor_dd_b4Input_(String bsHor_dd_b4Input_) {
        this.bsHor_dd_b4Input_ = bsHor_dd_b4Input_;
    }
    
    private String bsHor_ta_open_ = null;
    public String getBsHor_ta_open_() {
        if (bsHor_ta_open_ == null) {
            bsHor_ta_open_ = "<div class=\"form-horizontal form-group\">\n" +
"                        <label class=\"col-md-4 control-label\"";
        }
        return bsHor_ta_open_;
    }
    
    private String bsHor_ta_close_ = null;
    public String getBsHor_ta_close_() {
        if (bsHor_ta_close_ == null) {
            bsHor_ta_close_ = "</div>";
        }
        return bsHor_ta_close_;
    }
    private String bsHor_ta_b4Input_ = null;
    public String getBsHor_ta_b4Input_() {
        if (bsHor_ta_b4Input_ == null) {
            bsHor_ta_b4Input_ = "<div class=\"col-md-5\">";
        }
        return bsHor_ta_b4Input_;
    }
    public void setBsHor_ta_b4Input_(String bsHor_ta_b4Input_) {
        this.bsHor_ta_b4Input_ = bsHor_ta_b4Input_;
    }
    
    public void setBsHor_ta_close_(String bsHor_ta_close_) {
        this.bsHor_ta_close_ = bsHor_ta_close_;
    }

    public void setBsHor_ta_open_(String bsHor_ta_open_) {
        this.bsHor_ta_open_ = bsHor_ta_open_;
    }
    
    public String bs_ud_open_ = "";
    public String getBs_ud_open_() {
        return bs_ud_open_;
    }
    public void setBs_ud_open_(String bs_ud_open_) {
        this.bs_ud_open_ = bs_ud_open_;
    }

    public String bs_ud_close_ = "</div>\n" +
        "</div>";
    public String getBs_ud_close_() {
        return bs_ud_close_;
    }
    public void setBs_ud_close_(String bs_ud_close_) {
        this.bs_ud_close_ = bs_ud_close_;
    }
    
    private String securityPolicy_ = "default-src 'self' 'unsafe-inline' 'unsafe-eval';";
    protected void addSeciryPolicy_(String newPolicy) {
        securityPolicy_ += newPolicy;
        updatePolicy();
    }
    protected void updatePolicy() {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.setHeader("Content-Security-Policy", securityPolicy_);
    }
    protected void defineScurityPolicy_(String policy) {
        securityPolicy_ = policy;
        updatePolicy();
    }
    public String getSecurityPolicy_() {
        return securityPolicy_;
    }    
    /** for BubbleShell : START **/
    private Integer BS_TIMEOUT_MINUTE = 30;
    private String __MOONLIGHT_TOKEN__; //use same variable name as php
    public String get__MOONLIGHT_TOKEN__() {
        if (__MOONLIGHT_TOKEN__ == null) {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            __MOONLIGHT_TOKEN__ = request.getParameter("__MOONLIGHT_TOKEN__");
        }
        return __MOONLIGHT_TOKEN__;
    }
    public void set__MOONLIGHT_TOKEN__(String __MOONLIGHT_TOKEN__) {
        this.__MOONLIGHT_TOKEN__ = __MOONLIGHT_TOKEN__;
    }
    
    private String __MOONLIGHT_REFRESH_TOKEN__; //use same variable name as php
    public String get__MOONLIGHT_REFRESH_TOKEN__() {
        if (__MOONLIGHT_REFRESH_TOKEN__ == null) {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            __MOONLIGHT_REFRESH_TOKEN__ = request.getParameter("__MOONLIGHT_REFRESH_TOKEN__");
        }
        return __MOONLIGHT_REFRESH_TOKEN__;
    }
    public void set__MOONLIGHT_REFRESH_TOKEN__(String __MOONLIGHT_REFRESH_TOKEN__) {
        this.__MOONLIGHT_REFRESH_TOKEN__ = __MOONLIGHT_REFRESH_TOKEN__;
    }
    
    private String bsUserId;
    public String getBsUserId() {
        return bsUserId;
    }
    public void setBsUserId(String bsUserId) {
        this.bsUserId = bsUserId;
    }
    
    private String bsPswd;
    public String getBsPswd() {
        return bsPswd;
    }
    public void setBsPswd(String bsPswd) {
        this.bsPswd = bsPswd;
    }
    
    public Boolean isLogined(){
        if (get__MOONLIGHT_TOKEN__() != null) {
            Map userInfo = (Map)getBsLoginMap().get(get__MOONLIGHT_TOKEN__());
            if (userInfo != null) {
                Calendar cal = (Calendar) userInfo.get("lastTrigger");
                cal.add(Calendar.MINUTE, BS_TIMEOUT_MINUTE);
                Calendar now = DateUtil.getCalendar();
                if (cal.after(now)) {
                    userInfo.put("lastTrigger", now);
                    populateBsLoginSession(userInfo);
                    return Boolean.TRUE;
                }
            }
        } else {
            Map session = ActionContext.getContext().getSession();
            System.out.println("session = " + session);
            return (session.containsKey("logined") && (session.containsKey("bubbleShellLogin")&&session.get("bubbleShellLogin").equals("Y")));
        }
        return Boolean.FALSE;
    }
    public Boolean loginBs(User user) {
        if (!Validator.isEmpty(bsUserId)) {
            Map userInfo = new HashMap();
            __MOONLIGHT_TOKEN__ = CommonFunction.getId(25);
            userInfo.put("__MOONLIGHT_TOKEN__", __MOONLIGHT_TOKEN__);
            userInfo.put("__MOONLIGHT_REFRESH_TOKEN__", CommonFunction.getId(25));
            userInfo.put("user_id", bsUserId);
            userInfo.put("loginId", bsUserId);
            userInfo.put("us_id", user.getID());
            userInfo.put("user_name", user.getUs_user_name());
            userInfo.put("loginAt", DateUtil.getCalendar());
            userInfo.put("lastTrigger", DateUtil.getCalendar());
            userInfo.put("logined", "true");
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            userInfo.put("user_ip", CommonFunction.getUserIp(request));
            userInfo.put("user_agent", CommonFunction.getUserAgent(request));
            userInfo.put("loginSystemType_", getSystemType_());
            userInfo.put("bubbleShellLogin", "Y"); //if login from bubbleShell then will put this flag.
            //check agains db to prevent duplicate token;
            getBsLoginMap().put(__MOONLIGHT_TOKEN__, userInfo);
            populateBsLoginSession(userInfo);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    public void logoutBs(){
        getBsLoginMap().remove(get__MOONLIGHT_TOKEN__());
        ActionContext.getContext().getSession().clear();
    }
    private static Map bsLoginMap = null;
    public static Map getBsLoginMap() {
        if (bsLoginMap == null) {
            bsLoginMap = new HashMap();
        }
        return bsLoginMap;
    }
    
    public Map populateBsLoginSession(Map userInfo) {
        if (userInfo == null) {
            userInfo = (Map)getBsLoginMap().get(get__MOONLIGHT_TOKEN__());
        }
        
        Map currentSession = ActionContext.getContext().getSession();
        currentSession.put("__MOONLIGHT_TOKEN__", get__MOONLIGHT_TOKEN__());
        if (userInfo != null) {
            currentSession.putAll(userInfo);
        }
//        currentSession.put("__MOONLIGHT_REFRESH_TOKEN__", userInfo.get("__MOONLIGHT_REFRESH_TOKEN__"));
//        currentSession.put("user_id", userInfo.get("user_id"));
//        currentSession.put("lastTrigger", userInfo.get("lastTrigger"));
//        currentSession.put("loginAt", userInfo.get("loginAt"));
//        currentSession.put("logined", userInfo.get("logined"));
//        currentSession.put("user_ip", userInfo.get("user_ip"));
//        currentSession.put("user_agent", userInfo.get("user_agent"));
        return userInfo;
    }
    /** for BubbleShell : END **/
    
    public String r = ""; //item to be refreshed, separated by comma(no space)

    public String getR() {
        //System.out.println("r = " + r);
        return r;
    }
    
    protected void printParam() {
        if (Debug.printFrameworkInfo) {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            Enumeration en=request.getParameterNames();

            while(en.hasMoreElements())
            {
                    Object objOri=en.nextElement();
                    String param=(String)objOri;
                    String value=request.getParameter(param);
                    System.out.println("Parameter Name is '"+param+"' and Parameter Value is '"+value+"'");

            }
        }
    }
    
    public String getJspPrefix() {
        return SystemConstants.SYSTEM_SETUP.JSP_PREFIX;
    }
    
    public Map uppyFileMap = null;
    public synchronized Map getUppyFileMap() {
        if (uppyFileMap == null) {
            uppyFileMap = new HashMap();
        }
        return uppyFileMap;
    }
    public void setUppyFileMap(Map uppyFileMap) {
        this.uppyFileMap = uppyFileMap;
    }
    
    public String getAppName_final(Application app) {
        if (Validator.isEmpty(app.getApplication_name_code())) {
            return app.getApplication_name();
        } else {
            return getText(app.getApplication_name_code());
        }
    }
}
