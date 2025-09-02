package com.sains.framework.base.web;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.sains.framework.base.BaseReportActionSupport;
import com.sains.framework.base.CommonList;
import com.sains.framework.base.LogFunction;
import com.sains.framework.base.ReportGenerator;
import com.sains.framework.base.ReportPreGenerate;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.sains.common.util.Validator;
import com.sains.common.util.DateUtil;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseDAO;

import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.RptBaseDAOImpl;
import com.sains.framework.lookup.DataRetriever;
import com.sains.framework.lookup.LookupAction;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.text.WordUtils;

public class DynamicRptAction extends BaseReportActionSupport {

    private static final long serialVersionUID = -6659925652584240539L;
    private final String UPDATE = "update";
    private Map<String, Object> searchParam = new HashMap();
    private Map<String, String> rightsList = new HashMap();
    /**
     * Variables for Dynamic Add / Edit
     */
    private List<String> pageLabels = new ArrayList();
    private List<String> pageLabelsDesc = new ArrayList();
    private List<String> pageFields = new ArrayList();
    private List<String> searchFields_with_dateFromTo = null;   // Delvene @ 11-Dec-2014
    private List<String> searchFields_with_yearFromTo = null;   // Zhafari @ 13-May-2015
    private Map<String, String> pageFieldsData = new HashMap();
    private Map pageFieldDD = new HashMap();
    private Map pageFieldJavaScript = new HashMap();
    private Map pageFieldColName = new HashMap();
    private String pageJavascript = null;
    private String localValidateFormJavascript = null;
    private String pageTitle = null;
    private Map configMap = null;
    private List<String> searchFields = new ArrayList();
    private String pageRequired = "";
    private String pageRequiredWhen = "";
    private String rptCode = "";
    private String contentType = "";
    private InputStream inputStream = null;
    private String contentDisposition = "";
    private String jasperReport;
    private String searchDescription;
    
    private Map searchFieldLookup = new HashMap();
    private Map searchFieldDD = new HashMap();
    private Map reportParam = new HashMap();
    private List<String> searchFieldsLabel = new ArrayList();
    private Boolean printToExcel = false;
    private String printTo_ = "pdf";
//    private String searchPage = ""; //Added by delvene @ 09-Dec-2013 :: To support custom search page
    private String searchPage = "/indexNew";//give a default searchPage 
    private List<String> hiddenFields = new ArrayList();
    private String defaultoToday ="N";
    private String controlDurationPick ="";
    //sereneC @ 2/1/2015 :: support mixConfig
    //start
    private String mcf_action = "";
    private List<String> mcf_actionList = new ArrayList();
    private Map mixedConfig_map = new HashMap();
    private String searchCode = null;
    private Boolean isMixConfig = Boolean.FALSE;
    private List<String> displayFields = new ArrayList();
    private Map searchFieldFormat = new HashMap();
    private Map searchFieldStyleFormat = new HashMap();
    private String dynamicSortBy = "";
    private String dynamicSortOrder = "A";
    private Map<String, Object> searchFieldsHelperText = new HashMap();
    private String mcf_Action_SearchData = "";
    //end
    
    private Map searchFieldsDataMap = new HashMap();

    public Map getSearchFieldsDataMap() {
        return searchFieldsDataMap;
    }

    public void setSearchFieldsDataMap(Map searchFieldsDataMap) {
        this.searchFieldsDataMap = searchFieldsDataMap;
    }
    
    public DynamicRptAction() {
    }

    public String dynamicViewPage() {
        if (!printTo_.equals("pdf")) {
            generateDynamicRpt();
            return SUCCESS + "_pdf";
        } else {
            if (has_right("exportToExcel") || has_right("exportToWord")) {
                getDynamicRptSearchData();
                return SUCCESS;
            } else {
                generateDynamicRpt();
                return SUCCESS + "_pdf";
            }
        }
    }

    public String generateDynamicRpt() {
        String preGenerateMethod = null;
        getDynamicRptSearchData();
        //added by sereneChye @ 2015/1/14 :: to support dropdownLink. Pass link into report,
//        String hyperlink = (String) configMap.get("dropDownLink");
//         if (hyperlink != null){
//             reportParam.put("pHyperLink", hyperlink);
//         }
        
        // Added by ThoTH @ 2011-Feb-9
        invoke();
        preGenerateMethod = (String) configMap.get("preGenerateMethod");
        if (preGenerateMethod != null) {
            boolean methodExists = false;
            Method m = null;
            ReportPreGenerate preGenerateTrigger = new ReportPreGenerate();
            try {
                m = preGenerateTrigger.getClass().getMethod(preGenerateMethod, DynamicRptAction.class, Map.class);
                Debug.printFrameworkDebug(" get m == " + m);
                methodExists = true;
            } catch (Exception e) {
            }

            Debug.printFrameworkDebug("method Exists == " + methodExists);
            if (methodExists) {
                try {
                    m.invoke(preGenerateTrigger, this, reportParam);
                } catch (Exception e) {
                }
            } else {
                Debug.printFrameworkDebug("Method \"" + preGenerateMethod + "\" do not exists");
            }
        }

        if (getActionErrors().size() <= 0 && getActionMessages().size() <= 0) {
            java.sql.Timestamp startTime = DateUtil.getCurrentTimestamp();
            String successStatus = BaseReportActionSupport.SUCCESS_STATUS.SUCCESS;
            String rptType = null;
            try {
                InputStream jasperRptStream = null;
                BaseDAO rptDAO = new RptBaseDAOImpl();
                if (!printTo_.equals("pdf")) {
                    String extension = "";
                    rptType = SystemConstants.ReportType.SpreadSheet;
                    contentType = "application/pdf"; //unknown type will print as pdf
                    try {
                        if (printTo_.equals("excel")) {
                            extension = "xls";
                            contentType = "application/xls";
                            rptType = SystemConstants.ReportType.SpreadSheet;
                        } else if (printTo_.equals("word")) {
                            extension = "docx";
                            contentType = "application/msword";
                            rptType = SystemConstants.ReportType.MsWord;
                        }
                        jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperReport.substring(0, jasperReport.length()-7) + "_"+extension+".jasper");
                        if (jasperRptStream == null) {
                            throw new Exception("no specific extension ("+extension+")");
                        }
                    } catch (Exception e) {
                        jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperReport);
                    }
//                    System.out.println("inside print to "+printTo_+"...");
//                    System.out.println("report param: " + reportParam);
//                    System.out.println("report code:= " + rptCode);
                    contentDisposition = "attachment; filename=\"" + rptCode + "."+extension+"\"";
//                    contentType = "application/xls";
                    try {
                        System.out.println(reportParam);
                        if (printTo_.equals("excel")) {
                            inputStream = ReportGenerator.printXlsBuffer(jasperRptStream, reportParam, rptDAO);
                        } else if (printTo_.equals("word")) {
                            inputStream = ReportGenerator.printDocxBuffer(jasperRptStream, reportParam, rptDAO);
                        } else { //unknown type will print as pdf
                            contentDisposition = "filename=\"" + rptCode + ".pdf\"";
                            inputStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, rptDAO);
                        }
                    } finally {
                        rptDAO.closeSession();
                    }
                } else { // default to print to PDF
                    Debug.printFrameworkDebug("reportParams: " + reportParams);
                    jasperRptStream = SystemConstants.globalServletContext.getResourceAsStream(jasperReport);
                    //REPORT_RESOURCE_BUNDLE
                    //reportParam.put(JRParameter.REPORT_LOCALE, new Locale("bm", "MY"));
                    //System.out.println("reportParam 2 = " + reportParam);
                    rptType = SystemConstants.ReportType.PDF;
                    contentDisposition = "filename=\"" + rptCode + ".pdf\"";
                    contentType = "application/pdf";                    
                    try {
                        inputStream = ReportGenerator.printPdfBuffer(jasperRptStream, reportParam, rptDAO);
//                        StringBuffer sb = ReportGenerator.printHtmlBuffer(jasperRptStream, reportParam);
//                        System.out.println("sb = " + sb);
                    } finally {
                        rptDAO.closeSession();
                    }
                }
            } catch (Exception e) {
                successStatus = BaseReportActionSupport.SUCCESS_STATUS.FAIL;
                e.printStackTrace();
            }
            auditReport(baseDAO, rptType, startTime, successStatus, jasperReport);
            
            // ThoTH @ 15-Jan-2016
            try {
//                if (printToExcel) {
//                    AuditActivityModel.insert(null,rptCode, null, "", "", AuditActivityModel.Action.ExportToExcel);
//                } else {
//                    AuditActivityModel.insert(null,rptCode, null, "", "", AuditActivityModel.Action.GeneratePDF);
//                }
            } catch (Exception e) {e.printStackTrace();}
        } else {
            return "fail";
        }
        if ((has_right("exportToExcel") && printTo_.equals("excel")) || (has_right("exportToWord") && printTo_.equals("word"))) {
            return SUCCESS;
        } else {
            return SUCCESS + "_pdf";
        }
    }

    private void getDynamicRptSearchData() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try {
            populateConfigMap();
            String[] paramArr = null;
            String paramData = null;
            //sereneC @ 6/1/2015 :: support mixConfig 
            //start
            List searchFieldsList = new ArrayList();
            if(isMixConfig) {
                mcf_Action_SearchData = request.getParameter("mcfActionCode").toString();//sereneC @ 6/1/2015 :: to get the dynamic action from dynamicReportViewer.jsp 
                searchFieldsList = (List) mixedConfig_map.get(mcf_Action_SearchData+"searchFields");
                jasperReport = (String) mixedConfig_map.get(mcf_Action_SearchData+"jasperReport");
                
//                System.out.println("mcf_Action_SearchData" + mcf_Action_SearchData);
//                for(Object strTest : searchFieldsList) {
//                    System.out.println("strTest: " + strTest);
//                }
            } else {
                searchFieldsList = getSearchFields();
            }
            //end
            if (getHiddenFields().size() > 0) {
                for (String hiddenField : getHiddenFields()) {
                    int idxOfName = hiddenField.indexOf("name='");
                    String fieldName = hiddenField.substring(idxOfName+13, hiddenField.indexOf("'", idxOfName+7));
                    Debug.printFrameworkDebug("---------------------- fieldName = " + fieldName);
                        searchFieldsList.add(fieldName);
//                        reportParams += fieldName+"=" + paramData + "&";
//                        auditCriteria += fieldName+"=" + paramData + "&" ;
//                        reportParam.put(fieldName, paramData);
                }
            }

            //for (Object str : getSearchFields()) { //sereneC @ 6/1/2014 :: change to searchFieldsList
            for (Object str : searchFieldsList) {
//                System.out.println("str: " + str);
                if (((String) str).startsWith("_date_")) {
                    if (((String) str).endsWith("_fromTo")) {
                        str = extractSearchFieldForDate((String) str);
                        paramData = request.getParameter("search_" + (String) str + "From");
                        if (!Validator.isEmpty(paramData)) {
                            reportParams += "search_" + str + "From=" + paramData + "&";
                            auditCriteria += "search_" + str + "From=" + paramData + "&" ;
                            reportParam.put(str + "From", DateUtil.getDate(paramData, getText("date_default_date_dr")));
                        }
                        paramData = request.getParameter("search_" + (String) str + "To");
                        
                        if (!Validator.isEmpty(paramData)) {
                            reportParams += "search_" + str + "To=" + paramData + "&";
                            auditCriteria += "search_" + str + "To=" + paramData + "&" ;
                            reportParam.put(str + "To", DateUtil.getDate(paramData, getText("date_default_date_dr")));
                        }
                    } else {
                        str = extractSearchFieldForDate((String) str);
                        paramData = request.getParameter("search_" + (String) str);
                        if (!Validator.isEmpty(paramData)) {
                            reportParams += "search_" + str + "=" + paramData + "&";
                            auditCriteria += "search_" + str + "=" + paramData + "&" ;
                            reportParam.put(str, DateUtil.getDate(paramData, getText("date_default_date_dr")));
                        }
                    }
                    continue;
                }
                
                //Zhafari @ 15-May-2015 :: _yearFromTo
                if (((String) str).endsWith("_yearFromTo")) {
                    str = extractSearchFieldForDate((String) str);
                    paramData = request.getParameter("search_" + (String) str + "From");
                    if (!Validator.isEmpty(paramData)) {
                        reportParams += "search_" + str + "From=" + paramData + "&";
                        auditCriteria += "search_" + str + "From=" + paramData + "&" ;
                        reportParam.put(str + "From", paramData);
                    }
                    paramData = request.getParameter("search_" + (String) str + "To");
                    if (!Validator.isEmpty(paramData)) {
                        reportParams += "search_" + str + "To=" + paramData + "&";
                        auditCriteria += "search_" + str + "To=" + paramData + "&" ;
                        reportParam.put(str + "To", paramData);
                    }
                    
                    continue;
                }
                //Zhafari @ 15-May-2015 :: _yearFromTo - END
                
                paramData = request.getParameter("search_" + (String) str);

                // Added by delvene @ 17-Nov-2014 :: Copied from DynamicAction to support multiple checkbox selection
                if (paramData != null) {
                    paramArr = request.getParameterValues("search_" + (String) str);
                } else {
                    paramArr = null;
                }

                if (paramArr != null) {
                    if (paramArr.length > 1) {
                        paramData = null;
                        for (String data : paramArr) {
//                            System.out.println("data: " + data);
                            if (paramData == null) {
                                paramData = data;
                                
                            } else {
                                paramData += "," + data;
                            }
                        }
                    }
                }
                // Added by delvene @ 17-Nov-2014 :: Copied from DynamicAction to support multiple checkbox selection - END
                
                if (!Validator.isEmpty(paramData)) {
                    //paramData = java.net.URLEncoder.encode(paramData, "UTF-8");  // ThoTH @ 20-Mar-2015
                    reportParams += "search_" + str + "=" + java.net.URLEncoder.encode(paramData, "UTF-8") + "&";
                    auditCriteria += "search_" + str + "=" + java.net.URLEncoder.encode(paramData, "UTF-8") + "&" ;
                    reportParam.put(str, new String(paramData));
                    Debug.printFrameworkDebug("put.... " + str);
//                    if (searchCondition.equals("")) {
//                        searchCondition = "search_" + (String) str + "=" + paramData.toString();
//                    } else {
//                        searchCondition += "&search_" + (String) str + "=" + paramData.toString();
//                    }
                }
            }
            // Added by ThoTH @ 9-Mar-2011 - To pass in User Login ID into Report
            String strSessionVar = (String) configMap.get("pass_Session_Variable");
            if (!Validator.isEmpty(strSessionVar)) {
                if (strSessionVar.contains("loginId")) {
                    paramData = (String) ActionContext.getContext().getSession().get("loginId");
                    if (!Validator.isEmpty(paramData)) {
                        paramData = java.net.URLEncoder.encode(paramData, "UTF-8");  // ThoTH @ 20-Mar-2015
                        reportParams += "pUsLoginId=" + paramData + "&";
                        reportParam.put("pUsLoginId", new String(paramData));
                    }
                }

                if (strSessionVar.contains("userName")) {
                    paramData = (String) ActionContext.getContext().getSession().get("userName");
                    if (!Validator.isEmpty(paramData)) {
                        paramData = java.net.URLEncoder.encode(paramData, "UTF-8");  // ThoTH @ 20-Mar-2015
                        reportParams += "pUsUserName=" + paramData + "&";
                        reportParam.put("pUsUserName", new String(paramData));
                    }
                }
            }
            
            Debug.printFrameworkDebug("reportParams: " + reportParams);
            
            //if (!Validator.isEmpty(request.getParameter("pageNo"))){
            //    searchCondition += "&pageNo="+request.getParameter("pageNo");
            //}
            //keepObjectToSession(searchCondition+"&pageNo="+request.getParameter("pageNo"), getAction() + "_searchCondition");
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            new LogFunction().logError(this.getClass(), "", e1);
        }

        //auditReport(SystemConstants.ReportType.PDF);

//        try {
//            new DataRetriever().retrieveData2(this, request, request.getSession().getServletContext());
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            new LogFunction().logError(this.getClass(), "", e);
//        }
    }

    @Override
    public boolean has_right(String theRight) {
        if (rightsList.containsKey(theRight)) {
            return rightsList.get(theRight).equalsIgnoreCase("Y");
        } else {
            Map sessionMap = ActionContext.getContext().getSession();
//            System.out.println("getRptCode() == " + getRptCode());
//            System.out.println("baseDAO.getSession() == " + baseDAO.getSession());
//            System.out.println("new CommonFunction().validateRight= "+ new CommonFunction().validateRight(this.getClass().getSimpleName(), theRight, sessionMap, getRptCode(), baseDAO.getSession()));
            if (new CommonFunction().validateRight(this.getClass().getSimpleName(), theRight, sessionMap, getRptCode(), baseDAO.getSession())) {
                rightsList.put(theRight, "Y");
                return true;
            }
            rightsList.put(theRight, "N");
            return false;
        }
    }

    public List<String> getPageLabels() {
        return pageLabels;
    }

    public void setPageLabels(List<String> pageLabels) {
        this.pageLabels = pageLabels;
    }

    public List<String> getPageLabelsDesc() {
        return pageLabelsDesc;
    }

    public void setPageLabelsDesc(List<String> pageLabelsDesc) {
        this.pageLabelsDesc = pageLabelsDesc;
    }

    private String checkRequired = null;
    public boolean isRequiredField(String fieldName) {
       
        if (checkRequired == null) {
            checkRequired = (String) configMap.get("pageRequired");
        }
//        if (required != null) {
//            String[] requiredArr = required.split(",");
//            for (String str : requiredArr) {
//                if (str.split(";")[0].trim().equals(pageField)) {
//                    return true;
//                }
//            }
//        }
        if (checkRequired != null) {
            for (String requiredField : checkRequired.split(",")) {
                String reqFieldName = requiredField.split(";")[0];
                if (fieldName.startsWith("_date_")) {
                    if (reqFieldName.equals(fieldName.substring(6)) || reqFieldName.equals(fieldName.substring(6)+"From") || reqFieldName.equals(fieldName.substring(6)+"To")) {
                        return Boolean.TRUE;
                    }
                } else if(requiredField.split(";")[0].equals(fieldName)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    public List<String> getPageFields() {
        return pageFields;
    }

    public void setPageFields(List<String> pageFields) {
        this.pageFields = pageFields;
    }

    public List getPageDDList(String pageField) {
        return (List) pageFieldDD.get(pageField);
    }

    public String getNameOfColumn(String pageField) {
        return (String) pageFieldColName.get(pageField);
    }

    public Map getPageFieldColName() {
        return pageFieldColName;
    }

    public void setPageFieldColName(Map pageFieldColName) {
        this.pageFieldColName = pageFieldColName;
    }

    public String getPageJavascript() {
        return pageJavascript;
    }

    public void setPageJavascript(String pageJavascript) {
        this.pageJavascript = pageJavascript;
    }

    public String getLocalValidateFormJavascript() {
        return localValidateFormJavascript;
    }

    public void setLocalValidateFormJavascript(String localValidateFormJavascript) {
        this.localValidateFormJavascript = localValidateFormJavascript;
    }

    public String getFieldData(String fieldName) {
        if (fieldName.startsWith("select:")) {
            return (String) pageFieldsData.get(fieldName.substring(7));
        } else {
            return (String) pageFieldsData.get(fieldName);
        }
    }

    public Map getPageFieldJavaScript() {
        return pageFieldJavaScript;
    }

    public void setPageFieldJavaScript(Map pageFieldJavaScript) {
        this.pageFieldJavaScript = pageFieldJavaScript;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageRequired() {
        return pageRequired;
    }

    public void setPageRequired(String pageRequired) {
        this.pageRequired = pageRequired;
    }

    public String getPageRequiredWhen() {
        return pageRequiredWhen;
    }

    public void setPageRequiredWhen(String pageRequiredWhen) {
        this.pageRequiredWhen = pageRequiredWhen;
    }

    public String getSearchPage() {
        if (searchPage != null) {
            if (searchPage.equals("dynamicRptSearch")) {
                searchPage = SystemConstants.SYSTEM_SETUP.JSP_PREFIX + searchPage;
            }
        }
        return searchPage;
    }

    public void setSearchPage(String searchPage) {
        this.searchPage = searchPage;
    }

    public String getFieldJavaScript(String fieldName) {
        if (fieldName.startsWith("select:")) {
            return (String) pageFieldJavaScript.get(fieldName.substring(7));
        } else {
            return (String) pageFieldJavaScript.get(fieldName);
        }
    }

    private Object getFieldValue(Object obj, String fieldName) {
        try {
            Method m = obj.getClass().getMethod("get" + WordUtils.capitalize(fieldName));
            return m.invoke(obj);
        } catch (Exception e) {
        }
        return null;
    }

    private String replaceJavascriptText(String replaceString) {
        while (replaceString.indexOf("[/setText]") > 0) {
            String strReplace = replaceString.substring(replaceString.indexOf("[setText]"), localValidateFormJavascript.indexOf("[/setText]") + 10);
            String strPattern = strReplace.substring(9, strReplace.length() - 10);
            strPattern = getText(strPattern);
            if (strPattern == null) {
                strPattern = "";
            }
            replaceString = replaceString.replace(strReplace, strPattern);
        }
        return replaceString;
    }

    public String loadSearchPage() {
        
        populateConfigMap();
        return SUCCESS;
    }

    private String populateConfigMap() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//        System.out.println("request.getParameter(\"mcfActionCode\").toString(): " + request.getParameter("mcfActionCode").toString());
        try {
            if (configMap == null) {
                configMap = new CommonFunction().getDynamicReportConfiguration(request.getParameter("rptCode"), request, request.getSession().getServletContext(), "");
            }
//            if (configMap.get("pageJavascript") != null) {
//                pageJavascript = (String) configMap.get("pageJavascript");
//                pageJavascript = replaceJavascriptText(pageJavascript);
//            }
//            setSearchDescription(configMap.get("searchDescription").toString());
//            setSearchPage(configMap.get("searchPage").toString());  //Added by delvene @ 09-Dec-2013 :: To support custom search page

//            String required = (String) configMap.get("pageRequired");
//            int count = 1;
//            if (required != null) {
//                for (String req : required.split(",")) {
//                    String[] reqArr = req.split(";");
//                    pageRequired += "this.a" + count++ + " = new Array('search_" + reqArr[0].trim() + "', '" + getText(reqArr[1]) + "'); " + "\r\n";
//                }
//            }

            String str = (String) getObjectFromSession(getAction() + "_searchCondition");
//            System.out.println("str : " +str);
            //  Added by serene @ 02-01-2015 :: Copied from DynamicAction to support mixconfig 
            String[] listSetup = null;
            if (configMap.get("mixedConfig") != null) {
                Debug.printFrameworkDebug("start mixConfig.........." );
                isMixConfig = Boolean.TRUE;
                
                setSearchDescription(configMap.get("searchDescription").toString());
                searchCode = request.getParameter("searchCode");
                if (Validator.isEmpty(searchCode)) {
                    Debug.printFrameworkDebug("searchCode is empty....");
                    if (str != null){
                        String[] conditionArr = str.split("&");
                        for (String s : conditionArr){
//                          System.out.println("s: " + s);
                            if (s.split("=")[0].equals("searchCode")){
                                searchCode=s.substring(s.indexOf("=")+1);
                                mcf_action = searchCode.split("::")[0];
                                break;
                            }
                        }
                    }
                } else {
                    mcf_action = searchCode.split("::")[0];
                }
                List<Options> list = getListFromSetup((String)configMap.get("mixedDD"));
                searchFieldDD.put("searchCode_dd", list);
                mixedConfig_map.put("mainSearchCodeDD", searchFieldDD); searchFieldDD = new HashMap();  // Added by Delvene @ 27-Aug-2014 :: Mainly for mainSearchCode in dynamicSearch. To support dropdown list in other MixedConfig
//                            System.out.println("searchCode = " + searchCode);
                if (Validator.isEmpty(searchCode)) {//if searchCode still empty? use the List's first data.
                    searchCode = list.get(0).getKeyData();
                    mcf_action = searchCode.split("::")[0];
                }

                if (Validator.isEmpty(searchCode)) {
                    mcf_action = configMap.get("mixedConfig").toString().split(",")[0].trim();
//                                configMap = getDynamicActionSetup(mcf_action);
//                                setSetupMap(configMap);
                }
                for (String mixAction : configMap.get("mixedConfig").toString().split(",")) {
                    mcf_actionList.add(mixAction.trim());
                    configMap = new CommonFunction().getDynamicReportConfiguration(mixAction.trim(), request, request.getSession().getServletContext(), "");
                    String searchField = null, searchFieldDD_setup = null;
//                    jasperReport = configMap.get("jasperReport").toString();
                    if (configMap.get("defaultSorting") != null) {
                        if (Validator.isEmpty(getDynamicSortBy())) {
                            setDynamicSortBy(configMap.get("defaultSorting").toString().split(";")[0]);
                            setDynamicSortOrder(configMap.get("defaultSorting").toString().split(";")[1]);
                        }
                    }
                    StringTokenizer st = new StringTokenizer(configMap.get("searchFields").toString(), ",");
                    StringTokenizer st2 = new StringTokenizer(configMap.get("searchFieldsLabel").toString(), ",");
                    while (st.hasMoreTokens()){
                        if (configMap.get(searchField+"_ac") != null){
                            String[] strArr = ((String)configMap.get(searchField+"_ac")).split(";");
                            CommonList commList = new CommonList(baseDAO.getSession());
                            Method m = commList.getClass().getMethod(strArr[0]);
                            useAc_ = Boolean.TRUE;
                            if (strArr.length > 2) {
                                acSetupList.add(new AutoComplete((List)m.invoke(commList), "search_"+searchField, strArr[1], strArr[1], "search_"+searchField, "", "", strArr[2].equalsIgnoreCase("true"), (strArr.length==4?strArr[3]:null) ));
                            } else {
                                acSetupList.add(new AutoComplete((List)m.invoke(commList), "search_"+searchField, strArr[1], strArr[1], "search_"+searchField, "", ""));
                            }
                        }
                        searchField = st.nextToken().trim();
                        if (searchField.startsWith("_date_")) { //setup the _fromTo
                            if (searchField.endsWith("_fromTo")) { 
                                if (searchFields_with_dateFromTo == null) {
                                    searchFields_with_dateFromTo = new ArrayList();
                                }
                                searchFields_with_dateFromTo.add(mixAction+"_"+searchField.substring(6, (searchField.length() - 7)));
                            }
                            setUsePopupCalander("Y");
                        }
                        //Zhafari @ 15-May-2015 :: _yearFromTo
                        if (searchField.endsWith("_yearFromTo")) {
                            if (searchFields_with_yearFromTo == null) {
                                searchFields_with_yearFromTo = new ArrayList();
                            }
                            searchFields_with_yearFromTo.add(mixAction+"_"+searchField.substring(0, (searchField.length() - 11)));
                        }
                        //Zhafari @ 15-May-2015 :: _yearFromTo - END
                        getSearchFields().add(searchField);
//                        System.out.println("getSearchFields().size(): " + getSearchFields().size());
                        if (configMap.get(searchField+"_lookupSearch") != null){
                            searchFieldLookup.put(searchField+"_lookupSearch", configMap.get(searchField+"_lookupSearch"));
                        }
                        if (configMap.get(searchField+"_lookupSearch_readOnly") != null){
                            searchFieldLookup.put(searchField+"_lookupSearch_readOnly", "true");
                        }
//                        if (configMap.get(searchField+"_defaultValue") != null){
//                            searchFieldsDefaultData.add(searchField+";"+configMap.get(searchField+"_defaultValue"));
//                        }
                        if (configMap.get(searchField+"_helperText") != null){
                            searchFieldsHelperText.put(searchField, configMap.get(searchField+"_helperText"));
                        }
                        if (configMap.get(searchField+"_dd") != null){
                            searchFieldDD_setup = configMap.get(searchField+"_dd").toString();
                            listSetup = searchFieldDD_setup.split(";");
                            searchFieldDD.put(searchField+"_dd", getListFromSetup(searchFieldDD_setup));
                            searchFieldDD.put(searchField+"_dd_list", listSetup[2]);   // Added by Delvene @ 29-Aug-2013 :: To identify list is from setup code table or normal option
                            searchFieldDD.put(searchField+"_dd_key", listSetup[3]);
                            searchFieldDD.put(searchField+"_dd_value", listSetup[4]);
//                            if (listSetup.length == 7) {//listSetup[6] must be the searchField's DB Name
//                                getSearchFields_dd_validateCriteria().add(searchField+";"+listSetup[6]);
//                            }
                            if (configMap.get(searchField+"_dd_type") != null) {
                                getSearchFieldDD().put(searchField+"_dd_type", configMap.get(searchField+"_dd_type"));
                            }
//                                if (searchFieldDD_setup.split(";")[2].equalsIgnoreCase("pubcode")){
//                                    if (!((String)searchFieldDD.get(searchField+"_dd_key")).equalsIgnoreCase("code_1")){
//                                        for (Object obj : (List)searchFieldDD.get(searchField+"_dd") ){
//                                            ((Pubcode)obj).setKeyCode((String)searchFieldDD.get(searchField+"_dd_key"));
//                                        }
//                                    }
//                                    if (!((String)searchFieldDD.get(searchField+"_dd_value")).equalsIgnoreCase("code_desc")){
//                                        for (Object obj : (List)searchFieldDD.get(searchField+"_dd") ){
//                                            ((Pubcode)obj).setKeyCode((String)searchFieldDD.get(searchField+"_dd_value"));
//                                        }
//                                    }
//                                }
                        }
//                        if (str != null){
//                            boolean found = false;
//                            String[] conditionArr = str.split("&");
//                            for (String s : conditionArr){
//                                if (s.split("=")[0].equals("search_" + searchField)){
//                                    found = true;
//                                    getSearchFieldsData().add( replaceSpecialChar_rev(s.substring(s.indexOf("=")+1)) );
//                                    getSearchFieldsDataMap().put("search_" + searchField, replaceSpecialChar_rev(s.substring(s.indexOf("=")+1)) );
//                                    getSearchedParam().put(searchField, replaceSpecialChar_rev(s.substring(s.indexOf("=")+1)) );
//                                    break;
//                                }
//                            }
//                            if (!found){
//                                getSearchFieldsData().add("");
//                                getSearchFieldsDataMap().put("search_" + searchField, "");
//                            }
//                        } else {
//                            getSearchFieldsData().add("");
//                            getSearchFieldsDataMap().put("search_" + searchField, "");
//                        }
                        
                        getSearchFieldsLabel().add(getText(st2.nextToken().trim()));
//                        setJasperReport(configMap.get("jasperReport").toString());
//                        getSearchFieldsDbName().add(st3.nextToken().trim());
                    }
                    st = new StringTokenizer(getDisplayFields().toString(), ",");
//                                st2 = new StringTokenizer(configMap.get("displayFieldsHeader").toString(), ",");
                    
                    
                    String displayField = null;
                    while (st.hasMoreTokens()){
                        displayField = st.nextToken().trim();
                        if (configMap.get("decFormat_"+displayField) != null){
                            searchFieldFormat.put("decFormat_"+displayField, configMap.get("decFormat_"+displayField));
                        }
                        if (configMap.get("propertyText_"+displayField) != null){
                            searchFieldFormat.put("propertyText_"+displayField, configMap.get("propertyText_"+displayField));
                        }
                        if (configMap.get("functionText_"+displayField) != null){
                            searchFieldFormat.put("functionText_"+displayField, configMap.get("functionText_"+displayField));
                        }
                        if (configMap.get("styleFormat_"+displayField) != null){
                            searchFieldStyleFormat.put("styleFormat_"+displayField, configMap.get("styleFormat_"+displayField));
                        }
                        if (configMap.get("searchInputStyleFormat_"+displayField) != null){ //Added by Zhafari @ 11-Nov-2014 - for style at search input
                            searchFieldStyleFormat.put("searchInputStyleFormat_"+displayField, configMap.get("searchInputStyleFormat_"+displayField));
                        }

                        getDisplayFields().add(displayField);
//                                    getDisplayFieldsHeader().add(getText(st2.nextToken().trim()));
                    }
                    mixedConfig_map.put(mixAction.trim()+"displayFields", displayFields);
                    mixedConfig_map.put(mixAction.trim()+"searchFields", searchFields); searchFields = new ArrayList();
                    mixedConfig_map.put(mixAction.trim()+"searchFieldLookup", searchFieldLookup); searchFieldLookup = new HashMap();
//                    mixedConfig_map.put(mixAction.trim()+"searchFieldsDefaultData", searchFieldsDefaultData); searchFieldsDefaultData = new ArrayList();
                    mixedConfig_map.put(mixAction.trim()+"searchFieldsHelperText", searchFieldsHelperText); searchFieldsHelperText = new HashMap();
                    mixedConfig_map.put(mixAction.trim()+"searchFieldDD", searchFieldDD); searchFieldDD = new HashMap();    // Uncommented by Delvene @ 28-Aug-2014 :: To show dropdown list in MixedConfig
//                    mixedConfig_map.put(mixAction.trim()+"searchFieldsData", searchFieldsData); searchFieldsData = new ArrayList();
//                    mixedConfig_map.put(mixAction.trim()+"searchedParam", searchedParam); searchedParam = new HashMap();
//                                mixedConfig_map.put("mixActionsearchFields_dd_validateCriteria", searchFields_dd_validateCriteria);
                    mixedConfig_map.put(mixAction.trim()+"searchFieldsLabel", searchFieldsLabel);searchFieldsLabel = new ArrayList();
//                    mixedConfig_map.put(mixAction.trim()+"searchFieldsDbName", searchFieldsDbName);searchFieldsDbName = new ArrayList();
                    mixedConfig_map.put(mixAction.trim()+"jasperReport", configMap.get("jasperReport").toString());//sereneC @ 6/1/2014 :: get jasper report of different dynamicAction

                    if (configMap.get("hideAddButton") != null){
                        mixedConfig_map.put(mixAction.trim()+"hideAddButton", configMap.get("hideAddButton"));
                    }
                    if (configMap.get("hideDeleteButton") != null){
                        mixedConfig_map.put(mixAction.trim()+"hideDeleteButton", configMap.get("hideDeleteButton"));
                    }
//                    if (configMap.get("sortingFields") != null){
//                        if (configMap.get("sortingFields").equals("asDisplayFields")){
//                            sortingFields = displayFields;
//                        } else {
//                            st = new StringTokenizer(configMap.get("sortingFields").toString(), ",");
//                            while (st.hasMoreTokens()){
//                                sortingFields.add(st.nextToken().trim());
//                            }
//                        }
//                        if (mixedConfig_map.get(mixAction.trim()+"dynamicSortBy") == null) {
//                            mixedConfig_map.put(mixAction.trim()+"dynamicSortBy", sortingFields.get(0));
//                            mixedConfig_map.put(mixAction.trim()+"sortingFields", sortingFields); sortingFields = new ArrayList();
//                        }
//                    }
                    displayFields = new ArrayList();
                    mixedConfig_map.put(mixAction.trim()+"editLinkColumn", configMap.get("editLinkColumn"));
                    mixedConfig_map.put(mixAction.trim()+"editPageURL", configMap.get("editPageURL"));
                    mixedConfig_map.put(mixAction.trim()+"addPageURL", configMap.get("addPageURL"));
                    mixedConfig_map.put(mixAction.trim()+"deleteURL", configMap.get("deleteURL"));
                }
                
                configMap = new CommonFunction().getDynamicReportConfiguration(mcf_action, request, request.getSession().getServletContext(), "");
                //configMap = getDynamicActionSetup(mcf_action);
                setSetupMap(configMap);
            }
            //end  
//            System.out.println("getSearchFields.size(): " + getSearchFields().size());
//            for(String strzx : getSearchFields()) {
//                System.out.println("str ===== : " + strzx);
//            }
            
            
            String searchField = null, searchFieldDD_setup = null;
            StringTokenizer st = new StringTokenizer(configMap.get("searchFields").toString(), ",");
            StringTokenizer st2 = new StringTokenizer(configMap.get("searchFieldsLabel").toString(), ",");
            //StringTokenizer st3 = new StringTokenizer(configMap.get("searchFieldsDbName").toString(), ",");
            
//            System.out.println("jasperReport: " + jasperReport);
//            if(Validator.isEmpty(jasperReport)) {
                jasperReport = configMap.get("jasperReport").toString();
//            }
//            System.out.println("jasperReport non-mix config: " + jasperReport);
            if (configMap.get("localValidateFormJavascript") != null) {
                localValidateFormJavascript = (String) configMap.get("localValidateFormJavascript");
                localValidateFormJavascript = replaceJavascriptText(localValidateFormJavascript);
            }
            if (configMap.get("dynamicDescription") != null) {
                pageTitle = getText((String) configMap.get("dynamicDescription"));
            }
            
            if (configMap.get("pageJavascript") != null) {
                pageJavascript = (String) configMap.get("pageJavascript");
                pageJavascript = replaceJavascriptText(pageJavascript);
            }
            
            String required = (String) configMap.get("pageRequired");
            int count = 1;
            if (required != null) {
                for (String req : required.split(",")) {
                    String[] reqArr = req.split(";");
                    pageRequired += "this.a" + count++ + " = new Array('search_" + reqArr[0].trim() + "', '" + getText(reqArr[1]) + "'); " + "\r\n";
                }
            }
            
            String requiredWhen = (String) configMap.get("pageRequiredWhen");
            if (requiredWhen != null) {
                for (String reqWhen : requiredWhen.split(",")) {
                    String[] reqWhenArr  = reqWhen.split(";");
                    String requiredWhenValue = reqWhenArr[4];
                    if (requiredWhenValue.equals("empty")) {
                        pageRequiredWhen += "if (document.getElementById('search_"+ reqWhenArr[2] +"').value=='') errors[errors.length] = '["+( getText(reqWhenArr[1]) )+"] is required when ["+getText(reqWhenArr[3])+"] is empty';" + "\r\n";
                    } else if (requiredWhenValue.contains("|")) {
                        for (String moreReqWhen : requiredWhenValue.split("\\|")) {
                            if (moreReqWhen.equals("empty")) {
                                pageRequiredWhen += "if (document.getElementById('search_"+ reqWhenArr[2] +"').value=='') errors[errors.length] = '["+( getText(reqWhenArr[1]) )+"] is required when ["+getText(reqWhenArr[3])+"] is empty';" + "\r\n";
                            } else {
                                String requiredValue = null;
                                String requiredDisplay = null;
                                if (moreReqWhen.contains("::")) {
                                    requiredValue = getText(moreReqWhen.split("::")[0]);
                                    requiredDisplay = getText(moreReqWhen.split("::")[1]);
                                } else {
                                    requiredDisplay = moreReqWhen;
                                    requiredValue = moreReqWhen;
                                }
                                pageRequiredWhen += "if (document.getElementById('search_"+ reqWhenArr[2] +"').value=='"+requiredValue+"') errors[errors.length] = '["+( getText(reqWhenArr[1]) )+"] is required when ["+getText(reqWhenArr[3])+"] = "+requiredDisplay+"';" + "\r\n";
                            }
                        }
                    } else {
                        String requiredValue = null;
                        if (reqWhenArr.length == 6) {
                            requiredValue = getText(reqWhenArr[5]);
                        } else {
                            requiredValue = reqWhenArr[4];
                        }
                        pageRequiredWhen += "if (document.getElementById('search_"+ reqWhenArr[2] +"').value=='"+requiredValue+"') errors[errors.length] = '["+( getText(reqWhenArr[1]) )+"] is required when ["+getText(reqWhenArr[3])+"] = "+requiredValue+"';" + "\r\n";
                    }
                }
            }
            
            setSearchDescription(configMap.get("searchDescription").toString());
            if (configMap.get("searchPage") == null) {
                setSearchPage("dynamicRptSearch");
            } else {
                setSearchPage(configMap.get("searchPage").toString());  //Added by delvene @ 09-Dec-2013 :: To support custom search page
            }
            
            while (st.hasMoreTokens()) {
                searchField = st.nextToken().trim();
                if (configMap.get(searchField+"_ac") != null){
                    String[] strArr = ((String)configMap.get(searchField+"_ac")).split(";");
                    CommonList commList = new CommonList(baseDAO.getSession());
                    Method m = commList.getClass().getMethod(strArr[0]);
                    useAc_ = Boolean.TRUE;
                    if (strArr.length > 2) {
                        acSetupList.add(new AutoComplete((List)m.invoke(commList), "search_"+searchField, strArr[1], strArr[1], "search_"+searchField, "", "", strArr[2].equalsIgnoreCase("true"), (strArr.length==4?strArr[3]:null) ));
                    } else {
                        acSetupList.add(new AutoComplete((List)m.invoke(commList), "search_"+searchField, strArr[1], strArr[1], "search_"+searchField, "", ""));
                    }
                }
                if (searchField.startsWith("_date_")) {
                    if (searchFields_with_dateFromTo == null) {
                        searchFields_with_dateFromTo = new ArrayList();
                    }
                    if (searchField.endsWith("_fromTo")) {
                        searchFields_with_dateFromTo.add(searchField.substring(6, (searchField.length() - 7)));
                    }
                    //sereneC @ 15/12/2014
                    if(configMap.get("dateDefault") != null){
                        setDefaultoToday("Y");
                    }
                    //sereneC @  16/1/2015 :: control duration of date
                    if(configMap.get("controlDurationPick") != null){
                        setControlDurationPick((String) configMap.get("controlDurationPick"));
                    }
                    
                    
                    setUsePopupCalander("Y");
                }
                //Zhafari @ 15-May-2015 :: _yearFromTo
                if (searchField.endsWith("_yearFromTo")) {
                    if (searchFields_with_yearFromTo == null) {
                        searchFields_with_yearFromTo = new ArrayList();
                    }
                    searchFields_with_yearFromTo.add(searchField.substring(0, (searchField.length() - 11)));
                }
                //Zhafari @ 15-May-2015 :: _yearFromTo - END
                getSearchFields().add(searchField);
                if (configMap.get(searchField + "_lookupSearch") != null) {
                    getSearchFieldLookup().put(searchField + "_lookupSearch", configMap.get(searchField + "_lookupSearch"));
                }
                if (configMap.get(searchField+"_lookupSearch_readOnly") != null){
                    searchFieldLookup.put(searchField+"_lookupSearch_readOnly", "true");
                }
                if (configMap.get(searchField + "_itemChange") != null) {
                    String[] itemChangeArr = ((String)configMap.get(searchField + "_itemChange")).split(",");
                    getSearchFieldsMap().put(searchField+"_itemChange", (String)configMap.get(searchField + "_itemChange"));
                    getSearchFieldsMap().put(searchField+"_refreshItem", itemChangeArr[0].replaceAll("'", ""));
                }
                if (configMap.get(searchField + "_ddMultiple") != null) {
                    getSearchFieldsMap().put(searchField+"_ddMultiple", "true");
                    System.out.println("arine test");
                }
                if (configMap.get(searchField + "_dd") != null) {
                    searchFieldDD_setup = configMap.get(searchField + "_dd").toString();
                    getSearchFieldDD().put(searchField + "_dd", getListFromSetup(searchFieldDD_setup));
                    getSearchFieldDD().put(searchField + "_dd_list", searchFieldDD_setup.split(";")[2]);   // Added by Delvene @ 10-Nov-2014 :: To identify list is from setup code table or normal option
                    getSearchFieldDD().put(searchField + "_dd_key", searchFieldDD_setup.split(";")[3]);
                    getSearchFieldDD().put(searchField + "_dd_value", searchFieldDD_setup.split(";")[4]);
                    
                    // Added by Delvene @ 10-Nov-2014 :: To support checkbox
                    if (configMap.get(searchField+"_dd_type") != null) {
                        getSearchFieldDD().put(searchField+"_dd_type", configMap.get(searchField+"_dd_type"));
                    }
//                    if (searchFieldDD_setup.split(";")[2].equalsIgnoreCase("pubcode")) {
//                        if (!((String) getSearchFieldDD().get(searchField + "_dd_key")).equalsIgnoreCase("code_1")) {
//                            for (Object obj : (List) getSearchFieldDD().get(searchField + "_dd")) {
//                                ((Pubcode) obj).setKeyCode((String) getSearchFieldDD().get(searchField + "_dd_key"));
//                            }
//                        }
//                        if (!((String) getSearchFieldDD().get(searchField + "_dd_value")).equalsIgnoreCase("code_desc")) {
//                            for (Object obj : (List) getSearchFieldDD().get(searchField + "_dd")) {
//                                ((Pubcode) obj).setKeyCode((String) getSearchFieldDD().get(searchField + "_dd_value"));
//                            }
//                        }
//                    }
                }
//                if (str != null) {
//                    boolean found = false;
//                    String[] conditionArr = str.split("&");
//                    for (String s : conditionArr) {
//                        if (s.split("=")[0].equals("search_" + searchField)) {
//                            found = true;
//                            getSearchFieldsData().add(s.substring(s.indexOf("=") + 1));
//                            break;
//                        }
//                    }
//                    if (!found) {
//                        getSearchFieldsData().add("");
//                    }
//                } else {
//                    getSearchFieldsData().add("");
//                }
                if (st2 != null) {
                    getSearchFieldsLabel().add(getText(st2.nextToken().trim()));
                }
                
                
                //getSearchFieldsDbName().add(st3.nextToken().trim());
            }
            
            if (configMap.get("hiddenFields") != null) {
                st = new StringTokenizer(configMap.get("hiddenFields").toString(), ",");
                while (st.hasMoreTokens()){
                    getHiddenFields().add(st.nextToken().trim());
                }
            }
            
            String preSearchPageMethod = (String)configMap.get("preSearchPageMethod");
            if (configMap.get("preSearchPageMethod") != null) {
                boolean methodExists = false;
                Method m = null;
                ReportPreGenerate preGenerateTrigger = new ReportPreGenerate();
                try {
                    m = preGenerateTrigger.getClass().getMethod(preSearchPageMethod, DynamicRptAction.class, Map.class);
                    methodExists = true;
                } catch (Exception e) {
                }

                if (methodExists) {
                    try {
                        m.invoke(preGenerateTrigger, this, reportParam);
                    } catch (Exception e) {
                    }
                } else {
                    Debug.printFrameworkDebug("PreSearchPage Method \"" + preSearchPageMethod + "\" do not exists");
                }    
            }

//            st = new StringTokenizer(configMap.get("displayFields").toString(), ",");
//            st2 = new StringTokenizer(configMap.get("displayFieldsHeader").toString(), ",");
//            while (st.hasMoreTokens()) {
//                displayField = st.nextToken().trim();
//                if (configMap.get("decFormat_" + displayField) != null) {
//                    searchFieldFormat.put("decFormat_" + displayField, configMap.get("decFormat_" + displayField));
//                }
//                if (configMap.get("propertyText_" + displayField) != null) {
//                    searchFieldFormat.put("propertyText_" + displayField, configMap.get("propertyText_" + displayField));
//                }
//                if (configMap.get("functionText_" + displayField) != null) {
//                    searchFieldFormat.put("functionText_" + displayField, configMap.get("functionText_" + displayField));
//                }
//                if (configMap.get("styleFormat_" + displayField) != null) {
//                    searchFieldStyleFormat.put("styleFormat_" + displayField, configMap.get("styleFormat_" + displayField));
//                }
//
//                getDisplayFields().add(displayField);
//                getDisplayFieldsHeader().add(getText(st2.nextToken().trim()));
//            }
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }
        return SUCCESS;
    }

    /*
     * ThoTH @ 2011-Feb-9
     * For Customized Where Clause
     */
    private int invoke() {
        String strReportName = rptCode;
        String strCallAction = (String) configMap.get("customWhereClause");

        if (Validator.isEmpty(strCallAction)) {
            return 0;
        }

        Debug.printFrameworkDebug("DynamicRptAction:invoke- " + strReportName + "::" + strCallAction);

        String strClass = strCallAction.substring(0, strCallAction.indexOf("::"));
        String strMethod = strCallAction.substring(strCallAction.indexOf("::") + 2);
        Class[] params = new Class[]{String.class};
        Object[] args = new Object[]{new String(strReportName)};
        try {
            Class c = Class.forName(strClass);
            Method m = c.getDeclaredMethod(strMethod, params);
            Object i = c.newInstance();
            Object r = m.invoke(i, args);

            // StrParam Must in the format of <ParameterName>::<Data>
            String strParam = (String) r;
            Debug.printFrameworkDebug("strParam: " + strParam);
            reportParam.put(strParam.substring(0, strParam.indexOf("::")), strParam.substring(strParam.indexOf("::") + 2));

        } catch (InvocationTargetException ie) {
            addActionError(ie.getTargetException().getMessage());
        } catch (Exception e) {
            addActionError(e.getMessage());
            new LogFunction().logError(this.getClass(), "", e);
        }

        return 0;
    }
    
      public String replaceSpecialChar_rev(String data) {//sereneC @ 2/1/2015 :: copy from BaseActionSupport to support mixConfig
        String tmp = data.replace("%26", "&");
        tmp = tmp.replace("%3D", "=");
        tmp = tmp.replace("%25", "%");
        return tmp;
    }
      
    public String getRptCode() {
        return rptCode;
    }

    public void setRptCode(String rptCode) {
        this.rptCode = rptCode;
    }

    public String extractSearchFieldForDate(String theDate) {
        if (theDate.endsWith("_fromTo")) {
            return theDate.substring(6, (theDate.length() - 7));
        } else if (theDate.endsWith("_yearFromTo")) {
            return theDate.substring(0, (theDate.length() - 11));
        }
        return theDate.substring(6);
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<String> getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(List searchFields) {
        this.searchFields = searchFields;
    }
    
    public List<String> getSearchFields_with_dateFromTo() {
        return searchFields_with_dateFromTo;
    }

    public void setSearchFields_with_dateFromTo(List<String> searchFields_with_dateFromTo) {
        this.searchFields_with_dateFromTo = searchFields_with_dateFromTo;
    }

    public List getSearchDDList(String searchField) {
        return (List) getSearchFieldDD().get(searchField);
    }

    public String getSearchDDKey(String searchField) {
        return (String) getSearchFieldDD().get(searchField);
    }

    public String getSearchDDValue(String searchField) {
        return (String) getSearchFieldDD().get(searchField);
    }

    public String processSearch() {
        return SUCCESS;
    }

    public String getSearchDescription() {
        return getText(searchDescription);
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public Map getSearchFieldLookup() {
        return searchFieldLookup;
    }

    public void setSearchFieldLookup(Map searchFieldLookup) {
        this.searchFieldLookup = searchFieldLookup;
    }

    public Map getSearchFieldDD() {
        return searchFieldDD;
    }

    public void setSearchFieldDD(Map searchFieldDD) {
        this.searchFieldDD = searchFieldDD;
    }

    protected List getListFromSetup(String setup) {
        Object returnObject = null;
        String[] setupArr = setup.split(";");
        try {
            Class c = Class.forName(setupArr[0]);
            Method m = c.getDeclaredMethod(setupArr[1]);
            Object i = null;
            try {
                i = c.getDeclaredConstructor(org.hibernate.Session.class).newInstance(baseDAO.getSession());
            } catch (Exception e) {
                i = c.newInstance();
            }
            returnObject = m.invoke(i);
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }

        if (returnObject != null) {
            if (!setupArr[5].equalsIgnoreCase("none")) {
                if (setupArr[2].equalsIgnoreCase("setupcode") ){    //Added by Delvene @ 10-Nov-2014 :: To support drop down list from setup code table
//                    CommonList.addBlankSetupCode((List)returnObject, "", getText(setupArr[5]));
                } else
                if (setupArr[2].equalsIgnoreCase("option")) {
                    CommonList.addBlankOption((List) returnObject, "", getText(setupArr[5]));
                }
            }
        }
        return (List) returnObject;
    }

    public List<String> getSearchFieldsLabel() {
        return searchFieldsLabel;
    }

    public void setSearchFieldsLabel(List searchFieldsLabel) {
        this.searchFieldsLabel = searchFieldsLabel;
    }

    public Map getConfigMap() { // do not generate setConfigMap!!
        return configMap;
    }

    public String exportToExcel() { // almost the same as
        printToExcel = true;
        printTo_ = ";";
        generateDynamicRpt();
        return SUCCESS;
    }

    public String getPrintTo_() {
        return printTo_;
    }

    public void setPrintTo_(String printTo_) throws Exception {
        if (Validator.isEmpty(printTo_)) {
            printTo_ = "pdf";
        } else {
            if (!(printTo_.equals("excel") || printTo_.equals("word"))) {
                throw new CustomBaseException("Invalid Print format");
            }
        }
        this.printTo_ = printTo_;
    }

    public Boolean isPrintToExcel() { // do not generate setPrintToExcel!!
        return printToExcel;
    }

    public void setJasperReport(String newJasperName) {
        jasperReport = newJasperName;
    }
    
    public List<String> getHiddenFields() {
        return hiddenFields;
    }

    public void setHiddenFields(List<String> hiddenFields) {
        this.hiddenFields = hiddenFields;
    }
    
    //sereneChye @ 16/1/2014 
    public String getDefaultoToday() {
        return defaultoToday;
    }

    public void setDefaultoToday(String defaultoToday) {
        this.defaultoToday = defaultoToday;
    }
      //sereneChye @ 16/1/2014 :: set duration of date
    public String getControlDurationPick() {
        return controlDurationPick;
    }

    public void setControlDurationPick(String controlDurationPick) {
        this.controlDurationPick = controlDurationPick;
    }
    
    
     //sereneChye @ 2/1/2014 :: start Getter Setter of mixConfig
     public Map getMixedConfig_map() {
        return mixedConfig_map;
    }

    public void setMixedConfig_map(Map mixedConfig_map) {
        this.mixedConfig_map = mixedConfig_map;
    }
    
    public List<String> getDisplayFields() {
		return displayFields;
	}
    
    public void setDisplayFields(List<String> displayFields) {
            this.displayFields = displayFields;
    }
    
    public String getDynamicSortBy() {
		return dynamicSortBy;
	}
    
    public void setDynamicSortBy(String dynamicSortBy) {
            this.dynamicSortBy = dynamicSortBy;
    }
    
    public String getDynamicSortOrder() {
            return dynamicSortOrder;
    }
    
    public void setDynamicSortOrder(String dynamicSortOrder) {
            this.dynamicSortOrder = dynamicSortOrder;
    }
    
    public Map<String, Object> getSearchFieldsHelperText() {
        return searchFieldsHelperText;
    }

    public void setSearchFieldsHelperText(Map<String, Object> searchFieldsHelperText) {
        this.searchFieldsHelperText = searchFieldsHelperText;
    }

    public Boolean getIsMixConfig() {
        return isMixConfig;
    }

    public void setIsMixConfig(Boolean isMixConfig) {
        this.isMixConfig = isMixConfig;
    }

    public String getMcf_action() {
        return mcf_action;
    }

    public void setMcf_action(String mcf_action) {
        this.mcf_action = mcf_action;
    }

    public List<String> getMcf_actionList() {
        return mcf_actionList;
    }

    public void setMcf_actionList(List<String> mcf_actionList) {
        this.mcf_actionList = mcf_actionList;
    }

    public String getMcf_Action_SearchData() {
        return mcf_Action_SearchData;
    }

    public void setMcf_Action_SearchData(String mcf_Action_SearchData) {
        this.mcf_Action_SearchData = mcf_Action_SearchData;
    }

     //------------ End Getter Setter of mixConfig
    
    
    // ThenSW @ 26-Aug-2013 :: Copied from DR
//    private String appendSecurityCondition(String securityType, String condition) throws Exception {
//        String strSql = "";
//        Integer intSecuAL  = (Integer) ActionContext.getContext().getSession().get("secuAL");
//        String strSecuBA = (String) ActionContext.getContext().getSession().get("secuBA");
//        String strSecuDeptBA = (String) ActionContext.getContext().getSession().get("secuDeptBA");
//        String strSecuDivBA = (String) ActionContext.getContext().getSession().get("secuDivBA");
//        String strSecuBU = (String) ActionContext.getContext().getSession().get("secuBU");
//        String strSecuDeptBU = (String) ActionContext.getContext().getSession().get("secuDeptBU");
//        String strSecuDivBU = (String) ActionContext.getContext().getSession().get("secuDivBU");
//        System.out.println("appendSecurityCondition: securityType: " + securityType + " >>>> intSecuAL:"+intSecuAL );
//
//        String strOperator = "";
////        if (condition == null || condition.indexOf("where") < 0) {
////            strOperator = " where ";
////        } else {
////            strOperator = " and ";
////        }
//
//        if (securityType.equalsIgnoreCase("SetupUser")) {
////        if (sqlTable.equalsIgnoreCase("t_setup_user")) {
//            if (intSecuAL == 8) {
//                strSql = strOperator + "exists (select su.us_id from t_setup_user su    where 8 >= t_setup_user.us_accesslevel) ";
//
//            } else if (intSecuAL < 8) {
//                strSql = strOperator + "exists (  select us_id from t_setup_user su, t_cm_personal_post bu, t_pt_post_operation oper, t_pt_establishment est   " +
//                       "                  where su.emp_id = bu.employee_id and bu.post_oper_id = oper.post_oper_id" +
//                       "                    and oper.est_id = est.est_id     " +
//                       "                    and est.est_dept_id = '"+strSecuDeptBU+"' " +
//                       "                    and t_setup_user.us_id = su.us_id)" ;
//                // Commented by ThoTH @ 25-Apr-2013 :: No more using t_pt_post_lokasi
////                strSql = strOperator + "exists (  select us_id from t_setup_user su, t_pt_post_lokasi bu, t_pt_establishment est   " +
////                       "                  where su.emp_id = bu.emp_id and bu.est_id_bu = est.est_id     " +
////                       "                    and est.est_dept_id = '"+strSecuDept+"' " +
////                       "                    and t_setup_user.us_id = su.us_id)" ;
//            }
//
//        } else if (securityType.equalsIgnoreCase("SetupUserGroup")) {
////        } else if (sqlTable.equalsIgnoreCase("t_setup_group userGroup")) {
//            if (intSecuAL == 8) {
//                strSql = strOperator + "not isnull(dept_id, '') = '' ";
//
//            } else if (intSecuAL < 8) {
//                strSql = strOperator + "dept_id = '"+strSecuDeptBU+"' ";
//            }
//
//        } else if (securityType.equalsIgnoreCase("MOHONAN")) {
//            if (intSecuAL < 8) {
//                strSql = strOperator + "((app.dept_id = '"+strSecuDeptBU+"' and app.app_type = '"+PtApplicationModel.APPLICATION_TYPE.NewPostUnderWarrant+"') OR (app.dept_id = '"+strSecuDeptBU+"' and app.app_type != '"+PtApplicationModel.APPLICATION_TYPE.NewPostUnderWarrant+"')) ";
////                strSql = strOperator + "((app.dept_id = '"+strSecuDeptBA+"' and app.app_type = '"+PtApplicationModel.APPLICATION_TYPE.NewPostUnderWarrant+"') OR (app.dept_id = '"+strSecuDeptBU+"' and app.app_type != '"+PtApplicationModel.APPLICATION_TYPE.NewPostUnderWarrant+"')) ";
//            }
//        } else if (securityType.equalsIgnoreCase("PostInfo")) {
//            System.out.println("intSecuAL = " + intSecuAL);
//            System.out.println("strSecuDeptBU = " + strSecuDeptBU);
//            System.out.println("strSecuBU = " + strSecuBU);
//            if (intSecuAL == 7) {  // HQ
//                strSql = strOperator + "exists (select innerBA.est_dept_id from t_pt_establishment innerBA " +
//                        "                        where innerBA.est_id = post.est_id " +
//                        "                          and innerBA.est_dept_id = '"+strSecuDeptBU+"')" ;
////                        "                          and innerBA.est_dept_id = '"+strSecuDeptBA+"')" ;
//            } else if (intSecuAL == 6) { // Station
//                strSql = strOperator + "exists (select innerBA.est_dept_id from t_pt_establishment innerBA " +
//                        "                        where innerBA.est_id = post.est_id " +
//                        "                          and innerBA.est_dept_id = '"+strSecuDeptBU+"' " +
//                        "                          and innerBA.est_id = '"+strSecuBU+"') " ;
////                        "                          and innerBA.est_dept_id = '"+strSecuDeptBA+"' " +
////                        "                          and innerBA.est_id = '"+strSecuBA+"') " ;
//
//            } else if (intSecuAL == 5) { // Division
//                strSql = strOperator + "exists (select innerBA.est_dept_id from t_pt_establishment innerBA inner join t_pt_station innerStt " +
//                        "                   on innerBA.est_station_id = innerStt.station_id " +
//                        "                where innerBA.est_id = post.est_id" +
//                        "                  and innerBA.est_dept_id = '"+strSecuDeptBU+"' and innerStt.station_div_id = '"+strSecuDivBU+"') " ;
////                        "                  and innerBA.est_dept_id = '"+strSecuDeptBA+"' and innerStt.station_div_id = '"+strSecuDivBA+"') " ;
//            }
//        } else if (securityType.equalsIgnoreCase("PostOperation")) {
//            if (intSecuAL == 7) {  // HQ
//                strSql = strOperator + "exists (select innerBU.est_dept_id from t_pt_establishment innerBU " +
//                        "                        where innerBU.est_id = post.est_id " +
//                        "                          and innerBU.est_dept_id = '"+strSecuDeptBU+"')" ;
//            } else if (intSecuAL == 6) { // Station
//                strSql = strOperator + "exists (select innerBU.est_dept_id from t_pt_establishment innerBU " +
//                        "                        where innerBU.est_id = post.est_id " +
//                        "                          and innerBU.est_dept_id = '"+strSecuDeptBU+"' " +
//                        "                          and innerBU.est_id = '"+strSecuBU+"') " ;
//
//            } else if (intSecuAL == 5) { // Division
//                strSql = strOperator + "exists (select innerBU.est_dept_id from t_pt_establishment innerBU inner join t_pt_station innerStt " +
//                        "                   on innerBU.est_station_id = innerStt.station_id " +
//                        "                where innerBU.est_id = post.est_id" +
//                        "                  and innerBU.est_dept_id = '"+strSecuDeptBU+"' and innerStt.station_div_id = '"+strSecuDivBU+"') " ;
//            }
//        } else if (securityType.equalsIgnoreCase("PostHolder")) {
//            if (intSecuAL == 7) {  // HQ
//                strSql = strOperator + "exists (select innerBU.est_dept_id from t_pt_establishment innerBU " +
//                        "                        where innerBU.est_id = bu.est_id " +
//                        "                          and innerBU.est_dept_id = '"+strSecuDeptBU+"')" ;
//            } else if (intSecuAL == 6) { // Station
//                strSql = strOperator + "exists (select innerBU.est_dept_id from t_pt_establishment innerBU " +
//                        "                        where innerBU.est_id = bu.est_id " +
//                        "                          and innerBU.est_dept_id = '"+strSecuDeptBU+"' " +
//                        "                          and innerBU.est_id = '"+strSecuBU+"') " ;
//
//            } else if (intSecuAL == 5) { // Division
//                strSql = strOperator + "exists (select innerBU.est_dept_id from t_pt_establishment innerBU inner join t_pt_station innerStt " +
//                        "                   on innerBU.est_station_id = innerStt.station_id " +
//                        "                where innerBU.est_id = bu.est_id" +
//                        "                  and innerBU.est_dept_id = '"+strSecuDeptBU+"' and innerStt.station_div_id = '"+strSecuDivBU+"') " ;
//            }
//        }
//
//        return strSql;
//    }

//    public Session hibernateSession(){
//        return baseDAO.getSession();
//    }
    
    @Override
    public String getBreadAppCode_() {
        return rptCode;
    }
    
    public Boolean useAc_ = Boolean.FALSE;
    public Boolean getUseAc_() {
        return useAc_;
    }
    
    public List acSetupList = new ArrayList();
    public List getAcSetupList() {
        return acSetupList;
    }
    public class AutoComplete{
        String dynamicConfig;
        String lookFor = "";
        String writeTo;
        List acList; //manual list
        StringBuilder data = new StringBuilder();
        String postEvent;
        String matchingColumn;
        String inputId;
        Boolean clearSuggestion;
        String placeHolder = null;
        String jsFunction;
        public AutoComplete(String dynamicConfig, String inputId, String matchingColumn, String lookFor, String writeTo, String jsFunction, String postEvent) throws Exception{
            this.dynamicConfig = dynamicConfig;
            this.inputId = inputId;
            this.matchingColumn = matchingColumn;
            this.setLookFor(lookFor);
            this.writeTo = writeTo;
            this.jsFunction = jsFunction;
            this.postEvent = postEvent;
            LookupAction lookupAction = new LookupAction();
            Map localMap = (lookupAction.getDynamicActionSetup(getRptCode()));
            lookupAction.setPageSize(0);
            lookupAction.setPaging(Boolean.FALSE);
            lookupAction.setRetrievingColumns(localMap.get("lookupRetrievingColumns").toString());
            lookupAction.setSqlTables(localMap.get("sqlTables").toString());
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            try {
                new DataRetriever().retrieveData_useSetup(lookupAction, request, request.getSession().getServletContext());
                int size = lookupAction.getResult().size();
                int idx = 1;
                String comma = ",";
                for (Map obj : (List<Map>)lookupAction.getResult()) {
                    if (idx++ == size) {
                        comma = "";
                    }
                    data.append("{");
                    String[] fromToArr = uniqueLookfor.split(",");
                    int innerIdx = 1;
                    int innerSize = fromToArr.length;
                    String innerComma = ",";
                    for (String from : fromToArr) {
                        if (innerIdx++ == innerSize) {
                            innerComma = "";
                        }
                        data.append(from).append(":'").append(obj.get(from.equals("value")?matchingColumn:from)).append("'").append(innerComma);
                    }
                    
//                    fromToArr = writeTo.split(",");
//                    innerIdx = 1;
//                    innerSize=fromToArr.length;
//                    for (String write_to : writeTo.split(",")) {
//                        if (innerIdx++ == innerSize) {
//                            innerComma = "";
//                        }
//                        data.append(write_to.equals(matchingColumn)?"value":write_to).append(":'").append(obj.get(write_to)).append("'").append(innerComma);
//                    }
                    data.append("}").append(comma);
                }
                Debug.printFrameworkDebug("data = " + data);
            } catch (Exception e) {
                
            }
        }
        public AutoComplete(List acList, String inputId, String matchingColumn, String lookFor, String writeTo, String jsFunction, String postEvent, Boolean clearSuggestion, String placeHolder) throws Exception {
            this(acList, inputId, matchingColumn, lookFor, writeTo, jsFunction, postEvent);
            this.clearSuggestion = clearSuggestion;
            this.placeHolder = placeHolder;
        }
        public AutoComplete(List acList, String inputId, String matchingColumn, String lookFor, String writeTo, String jsFunction, String postEvent) throws Exception {
            this.inputId = inputId;
            this.matchingColumn = matchingColumn;
            this.setLookFor(lookFor);
            this.writeTo = writeTo;
            this.jsFunction = jsFunction;
            this.postEvent = postEvent;
            this.clearSuggestion = Boolean.TRUE;
            int size = acList.size();
                int idx = 1;
                String comma = ",";
                for (Object obj : acList) {
                    if (idx++ == size) {
                        comma = "";
                    }
                    data.append("{");
                    String[] fromToArr = uniqueLookfor.split(",");
                    int innerIdx = 1;
                    int innerSize = fromToArr.length;
                    String innerComma = ",";
                    for (String from : fromToArr) {
                        if (innerIdx++ == innerSize) {
                            innerComma = "";
                        }
                        if (obj instanceof Map) {
                            data.append(from).append(":'").append(((Map)obj).get(from.equals("value")?matchingColumn:from).toString().replaceAll("'", "\\\\'")).append("'").append(innerComma);
                        } else {
                            data.append(from).append(":'").append( getMethodValueFromObject(obj, from.equals("value")?matchingColumn:from)).append("'").append(innerComma);
                        }
                    }
                    data.append("}").append(comma);
                }
        }

        public String getLookFor() {
            return lookFor;
        }
        
        String uniqueLookfor = null;
        public void setLookFor(String lookFor) {
            Set<String> lookForSet = new HashSet();
            String[] lookForArr = lookFor.split(",");
            int idx = 1;
            int arrSize = lookForArr.length;
            for (String look4 : lookForArr) {
                if (look4.equals(matchingColumn)) {
                    look4 = "value";
                }
                lookForSet.add(look4.trim());
                if (idx++ == arrSize) {
                    this.lookFor += look4;
                } else {
                    this.lookFor += look4 + ",";
                }
            }
            for (String look4 : lookForSet) {
                if (this.uniqueLookfor == null) {
                    this.uniqueLookfor = look4;
                } else {
                    this.uniqueLookfor += ","+look4;
                }
            }
            Debug.printFrameworkDebug("uniqueLookfor = "+ uniqueLookfor);
        }

        public String getWriteTo() {
            return writeTo;
        }

        public void setWriteTo(String writeTo) {
            this.writeTo = writeTo;
        }

        public String getData() {
            return data.toString();
        }

        public String getPostEvent() {
            return postEvent;
        }

        public void setPostEvent(String postEvent) {
            this.postEvent = postEvent;
        }

        public String getJsFunction() {
            return jsFunction;
        }

        public void setJsFunction(String jsFunction) {
            this.jsFunction = jsFunction;
        }

        public String getInputId() {
            return inputId;
        }

        public void setInputId(String inputId) {
            this.inputId = inputId;
        }

        public Boolean getClearSuggestion() {
            return clearSuggestion;
        }

        public void setClearSuggestion(Boolean clearSuggestion) {
            this.clearSuggestion = clearSuggestion;
        }

        public String getPlaceHolder() {
            return placeHolder;
        }

        public void setPlaceHolder(String placeHolder) {
            this.placeHolder = placeHolder;
        }
    }
}
