package com.sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.CommonComparator;
import com.sains.common.util.CriteriaConverter;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CommonList;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import com.sains.framework.base.SessionFactoryImpl;
import com.sains.framework.lookup.ItemChangeAction;
import com.sains.framework.model.Application;
import com.sains.framework.model.ApplicationRights;
import com.sains.framework.model.AuditTrailModel;
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.Module;
import com.sains.framework.model.User;
import com.sains.framework.model.UserPreferenceModel;
import com.sains.framework.sam.dao.ApplicationDAOImpl;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.Pattern;
import javax.net.ssl.SSLContext;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.WordUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.struts2.ServletActionContext;
import org.hibernate.query.Query;
import org.hibernate.jpa.spi.NativeQueryTupleTransformer;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.ToListResultTransformer;

public class GenerateEntryAction extends BaseActionSupport{// implements ModelDriven<String> {

    private static final long serialVersionUID = -6659925652584240539L;
    private String useServiceFactory_ = "N";

    public String getUseServiceFactory_() {
        return useServiceFactory_;
    }

    public void setUseServiceFactory_(String useServiceFactory_) {
        this.useServiceFactory_ = useServiceFactory_;
    }


    public GenerateEntryAction() {
        // change the DAO to the correct service.
        //baseDAO = new BaseDAOImpl();
        setInsertUpdateSuccessCode(SystemConstants.ACTION_Status.LOAD_EDIT_PAGE);
        // set the required field for common check.
        //getRequiredParam().put("column_name1", "column.name1");
        //getRequiredParam().put("column_name2", "column.name2");


    }

    public void specificValidation(String validationType) {
    }

    public String processInsert() {
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    public String processUpdate() {
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String delete() {
        return SUCCESS;
    }

    //** Generate Entry Page from Model : START **//
    private String theApplicationCode_ = null;
    public String getTheApplicationCode_() {
        return theApplicationCode_;
    }
    public void setTheApplicationCode_(String theApplicationCode_) {
        this.theApplicationCode_ = theApplicationCode_;
    }
    private String getFormatterApplicationCode_() {
        return theApplicationCode_.substring(0, 1).toUpperCase() + theApplicationCode_.substring(1);
    }
    private String theApplicationName_ = null;
    public String getTheApplicationName_() {
        return theApplicationName_;
    }
    public void setTheApplicationName_(String theApplicationName_) {
        this.theApplicationName_ = theApplicationName_;
    }
    
    private String filePath = null;
    public String getFilePath() {
        if (Validator.isEmpty(filePath)) {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            String contextPath = request.getContextPath();
            filePath = "C:\\Projects\\" + contextPath.substring(1);
        }
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    private String updateToProject = "N";
    public String getUpdateToProject() {
        return updateToProject;
    }
    public void setUpdateToProject(String updateToProject) {
        this.updateToProject = updateToProject;
    }
    
    private String updateToProjectActionClass = "N";
    public String getUpdateToProjectActionClass() {
        return updateToProjectActionClass;
    }
    public void setUpdateToProjectActionClass(String updateToProjectActionClass) {
        this.updateToProjectActionClass = updateToProjectActionClass;
    }

    private String updateToProjectJsp = "N";
    public String getUpdateToProjectJsp() {
        return updateToProjectJsp;
    }
    public void setUpdateToProjectJsp(String updateToProjectJsp) {
        this.updateToProjectJsp = updateToProjectJsp;
    }
    
    private String insertApplicationRecord = "N";
    public String getInsertApplicationRecord() {
        return insertApplicationRecord;
    }
    public void setInsertApplicationRecord(String insertApplicationRecord) {
        this.insertApplicationRecord = insertApplicationRecord;
    }
    
    private String useTab = "N";
    public String getUseTab() {
        return useTab;
    }
    public void setUseTab(String useTab) {
        this.useTab = useTab;
    }
    
    private String createStrutsXML = "N";
    public String getCreateStrutsXML() {
        return createStrutsXML;
    }
    public void setCreateStrutsXML(String createStrutsXML) {
        this.createStrutsXML = createStrutsXML;
    }
    
    private String createDynamicConfig = "N";
    public String getCreateDynamicConfig() {
        return createDynamicConfig;
    }
    public void setCreateDynamicConfig(String createDynamicConfig) {
        this.createDynamicConfig = createDynamicConfig;
    }
    public Boolean getCreateDynamicConfig_boo() {
        return createDynamicConfig!=null&&createDynamicConfig.equals("Y");
    }
    
    private String jspDir = "";
    public String getJspDir() {
        return jspDir;
    }
    public void setJspDir(String jspDir) {
        try {
            jspDir = jspDir.toLowerCase();
        } catch (Exception e) {
        }
        this.jspDir = jspDir;
    }
    
    private final String uppy_js = "<script src=\"uppy/v1.22.0/uppy.min.js\"></script>";
    private final String uppy_css = "<link rel=\"stylesheet\" href=\"uppy/v1.22.0/uppy.min.css\">";
    private final String uppy_style = "<style>\n" +
"            button .fa-ns {\n" +
"                margin-right: 0px;\n" +
"            }\n" +
"            .uppy-DragDrop-label {\n" +
"                font-size: 0.9em;\n" +
"                max-width: 100%;\n" +
"            }\n" +
"            .uppy-DragDrop-arrow{\n" +
"                width:0px;\n" +
"                height:0px;\n" +
"                margin-bottom:0px;\n" +
"            }\n" +
"            .uppy-DragDrop-inner{\n" +
"                padding: 0px;\n" +
"            }\n" +
"        </style>";
    private Boolean hasUppy = Boolean.FALSE;
    
    private final TemplateClass  CurrentTemplateClass = new COL_MD_47();
    
    public static class TemplateClass {
        public static String mainTextarea_templateStr;
        public String mainTextField_templateStr;
        public String mainUppyUpload_templateStr;
        public String mainCheckbox_templateStr;
        public String mainRadio_templateStr;
        public String mainDropdown_templateStr;
    }
    public static class COL_MD_47 extends TemplateClass {
        public COL_MD_47() {
        mainTextarea_templateStr = 
"            <div class=\"form-horizontal form-group\">\n" +
"                <label class=\"col-md-4 control-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
"                <div class=\"col-md-7\">\n" +
"                    <s:textarea id=\"field__ReplaceID_\" _ReplaceTaSetup_ cssClass=\"form-control _ReplaceCssClass_\" name=\"_ReplaceFieldName_\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_/>\n" +
"                </div>\n" +
"            </div>\r\n";
        mainTextField_templateStr = 
"            <div class=\"form-horizontal form-group\">\n" +
"                <label class=\"col-md-4 control-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
"                <div class=\"col-md-7\">\n" +
"                    <s:textfield id=\"field__ReplaceID_\" _ReplaceMaxLength_ cssClass=\"form-control _ReplaceCssClass_\" name=\"_ReplaceFieldName_\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_/>\n" +
"                </div>\n" +
"            </div>\r\n";
        mainUppyUpload_templateStr = 
"            <div class=\"form-horizontal form-group\">\n" +
"                <label class=\"col-md-4 control-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
"                        <div class=\"col-md-7\">\n" +
"                            <s:hidden id=\"_ReplaceDrDocNAME_ID_\" name=\"_ReplaceDrDocNAME_NAME_\"/>\n" +
"                            <s:hidden id=\"_ReplaceDrDocFK_ID_\" name=\"_ReplaceFieldName_\"/>\n" +
"                            <s:include value=\"/base/uppyIncludeSingleFile_Replace_tus_.jsp\">\n" +
"                                <s:param name=\"uploadUrl_\">_ReplaceUploadURL_</s:param>\n" +
"                                <s:param name=\"uppyFieldName_\">_ReplaceFileCode_NAME_</s:param>\n" +
"                                <s:param name=\"drFileCode_\">_ReplaceFileCode_</s:param>\n" +
"                                <s:param name=\"theModelID\"><s:property value=\"%{_ReplacePkName_}\"/></s:param>\n" +
"                                <s:param name=\"uploadParams\">uploadRecordId_=<s:property value=\"%{_ReplacePkName_}\"/></s:param>\n" +
"                                <s:param name=\"uploadedFileName\"><s:property value=\"_ReplaceDrDocNAME_NAME_\"/></s:param>\n" +
"                                <s:param name=\"uploadedFileId\"><s:property value=\"_ReplaceFieldName_\"/></s:param>\n" +
"                                <s:param name=\"theRecord_id\">_theRecord_id_</s:param>\n" +
"                                <s:param name=\"updateHiddenId\">_ReplaceDrDocFK_ID_</s:param>\n" +
"                                <s:param name=\"updateHiddenName\">_ReplaceDrDocNAME_ID_</s:param>\n" +
"                                <%--<s:param name=\"allowedFileTypes\">'image/*','application/pdf'</s:param>--%>\n" +
"                                <%--<s:param name=\"checkFileFn\">checkFileName</s:param>--%>\n" +
"                            </s:include>\n" +
"                        </div>\n" +
"            </div>\r\n";
        mainCheckbox_templateStr = 
"            <div class=\"form-horizontal form-group\">\n" +
"                <label class=\"col-md-4 checkbox-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
"                <div class=\"col-md-7 checkbox right check-success padTop\">\n" +
"                       <input type=\"checkbox\" class=\"form-control\" value=\"_ReplaceCheckedValue_\" id=\"field__ReplaceID_\" name=\"_ReplaceFieldName_\" _ReplaceInputReq_<s:if test='_ReplaceValueVar_.equals(\"_ReplaceCheckedValue_\")'>checked</s:if>" + " >\n" +
"                       <label for=\"field__ReplaceID_\"></label>\n" +
"                </div>\n" +
"            </div>\r\n";
        mainRadio_templateStr = 
"            <div class=\"form-horizontal form-group\">\n" +
"                <label class=\"col-md-5 radio-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
"                <div class=\"col-md-7 radio radio-inline radio-success\">\n" +
"                    <s:radio id=\"field__ReplaceID_\" list=\"_ReplaceDdList_\" listKey=\"_ReplaceDdKey_\" listValue=\"_ReplaceDdValue_\" name=\"_ReplaceFieldName_\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_ />\n" +        
"                </div>\n" +
"            </div>\r\n";
        mainDropdown_templateStr = 
"            <div class=\"form-horizontal form-group\">\n" +
"                <label class=\"col-md-4 control-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
"                <div class=\"col-md-7\">\n" +
"                    <s:select id=\"field__ReplaceID_\" list=\"_ReplaceDdList_\" listKey=\"_ReplaceDdKey_\" listValue=\"_ReplaceDdValue_\" name=\"_ReplaceFieldName_\" cssClass=\"form-control sds-dropdown\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_ />\n" +        
"                </div>\n"+
"            </div>\n";
        }
    }
    public static class COL_MD_56 extends TemplateClass {
        public COL_MD_56() {
        mainTextarea_templateStr = 
"            <div class=\"form-horizontal form-group\">\n" +
"                <label class=\"col-md-5 control-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
"                <div class=\"col-md-6\">\n" +
"                    <s:textarea id=\"field__ReplaceID_\" _ReplaceTaSetup_ cssClass=\"form-control _ReplaceCssClass_\" name=\"_ReplaceFieldName_\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_/>\n" +
"                </div>\n" +
"            </div>\r\n";
        mainTextField_templateStr = 
"            <div class=\"form-horizontal form-group\">\n" +
"                <label class=\"col-md-5 control-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
"                <div class=\"col-md-6\">\n" +
"                    <s:textfield id=\"field__ReplaceID_\" _ReplaceMaxLength_ cssClass=\"form-control _ReplaceCssClass_\" name=\"_ReplaceFieldName_\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_/>\n" +
"                </div>\n" +
"            </div>\r\n";
        mainCheckbox_templateStr = 
"            <div class=\"form-horizontal form-group\">\n" +
"                <label class=\"col-md-4 checkbox-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
"                <div class=\"col-md-7 checkbox right check-success padTop\">\n" +
"                       <input type=\"checkbox\" class=\"form-control\" value=\"_ReplaceCheckedValue_\" id=\"field__ReplaceID_\" name=\"_ReplaceFieldName_\" _ReplaceInputReq_<s:if test='_ReplaceValueVar_.equals(\"_ReplaceCheckedValue_\")'>checked</s:if>" + " >\n" +
"                       <label for=\"field__ReplaceID_\"></label>\n" +
"                </div>\n" +
"            </div>\r\n";
        mainRadio_templateStr = 
"            <div class=\"form-horizontal form-group\">\n" +
"                <label class=\"col-md-5 radio-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
"                <div class=\"col-md-7 radio radio-inline radio-success\">\n" +
"                    <s:radio id=\"field__ReplaceID_\" list=\"_ReplaceDdList_\" listKey=\"_ReplaceDdKey_\" listValue=\"_ReplaceDdValue_\" name=\"_ReplaceFieldName_\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_ />\n" +        
"                </div>\n" +
"            </div>\r\n";
        mainDropdown_templateStr = 
"            <div class=\"form-horizontal form-group\">\n" +
"                <label class=\"col-md-4 control-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
"                <div class=\"col-md-7\">\n" +
"                    <s:select id=\"field__ReplaceID_\" list=\"_ReplaceDdList_\" listKey=\"_ReplaceDdKey_\" listValue=\"_ReplaceDdValue_\" name=\"_ReplaceFieldName_\" cssClass=\"form-control sds-dropdown\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_ />\n" +        
"                </div>\n"+
"            </div>\n";
        }
    }
//    }
    
/*
//    private String mainTextarea_templateStr = 
//    "            <div class=\"form-group\">\n" +
//    "                <label class=\"control-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
//    "                <div class=\"\">\n" +
//    "                    <s:textarea id=\"field__ReplaceID_\" _ReplaceTaSetup_ cssClass=\"form-control _ReplaceCssClass_\" name=\"_ReplaceFieldName_\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_/>\n" +
//    "                </div>\n" +
//    "            </div>\r\n";
    private String mainTextarea_templateStr = 
    "            <div class=\"form-horizontal form-group\">\n" +
    "                <label class=\"col-md-4 control-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
    "                <div class=\"col-md-7\">\n" +
    "                    <s:textarea id=\"field__ReplaceID_\" _ReplaceTaSetup_ cssClass=\"form-control _ReplaceCssClass_\" name=\"_ReplaceFieldName_\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_/>\n" +
    "                </div>\n" +
    "            </div>\r\n";
//private String mainTextField_templateStr = 
//    "            <div class=\"col-md-6\">\n" +
//    "                <div class=\"form-group\">\n" +
//    "                    <label class=\"control-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
//    "                    <s:textfield id=\"field__ReplaceID_\" _ReplaceMaxLength_ cssClass=\"form-control _ReplaceCssClass_\" name=\"_ReplaceFieldName_\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_/>\n" +
//    "                </div>\r\n";
//    "            </div>\r\n";
            
    private String mainTextField_templateStr = 
"            <div class=\"form-horizontal form-group\">\n" +
"                <label class=\"col-md-4 control-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
"                <div class=\"col-md-7\">\n" +
"                    <s:textfield id=\"field__ReplaceID_\" _ReplaceMaxLength_ cssClass=\"form-control _ReplaceCssClass_\" name=\"_ReplaceFieldName_\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_/>\n" +
"                </div>\n" +
"            </div>\r\n";
//    private String mainCheckbox_templateStr = 
//    "            <div class=\"form-group\">\n" +
//    "                <label class=\"checkbox-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
//    "                <div class=\"checkbox right check-success\">\n" +
//    "                       <input type=\"checkbox\" class=\"form-control\" value=\"_ReplaceCheckedValue_\" id=\"field__ReplaceID_\" name=\"_ReplaceFieldName_\" _ReplaceInputReq_<s:if test='_ReplaceValueVar_.equals(\"_ReplaceCheckedValue_\")'>checked</s:if>" + " >\n" +
//    "                       <label for=\"field__ReplaceID_\"></label>\n" +
//    "                </div>\n" +
//    "            </div>\r\n";
    private String mainCheckbox_templateStr = 
    "            <div class=\"form-horizontal form-group\">\n" +
    "                <label class=\"col-md-4 checkbox-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
    "                <div class=\"col-md-7 checkbox right check-success padTop\">\n" +
    "                       <input type=\"checkbox\" class=\"form-control\" value=\"_ReplaceCheckedValue_\" id=\"field__ReplaceID_\" name=\"_ReplaceFieldName_\" _ReplaceInputReq_<s:if test='_ReplaceValueVar_.equals(\"_ReplaceCheckedValue_\")'>checked</s:if>" + " >\n" +
    "                       <label for=\"field__ReplaceID_\"></label>\n" +
    "                </div>\n" +
    "            </div>\r\n";
//    private String mainRadio_templateStr = 
//    "            <div class=\"form-group\">\n" +
//    "                <label class=\"control-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label><br>\n" +
//    "                <div class=\"radio radio-inline radio-success\">\n" +
//    "                    <s:radio id=\"field__ReplaceID_\" list=\"_ReplaceDdList_\" listKey=\"_ReplaceDdKey_\" listValue=\"_ReplaceDdValue_\" name=\"_ReplaceFieldName_\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_ />\n" +        
//    "                </div>\n" +
//    "            </div>\r\n";
    private String mainRadio_templateStr = 
    "            <div class=\"form-horizontal form-group\">\n" +
"                <label class=\"col-md-5 radio-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
"                <div class=\"col-md-7 radio radio-inline radio-success\">\n" +
"                    <s:radio id=\"field__ReplaceID_\" list=\"_ReplaceDdList_\" listKey=\"_ReplaceDdKey_\" listValue=\"_ReplaceDdValue_\" name=\"_ReplaceFieldName_\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_ />\n" +        
"                </div>\n" +
"            </div>\r\n";
    private String mainDropdown_templateStr = 
    "            <div class=\"form-horizontal form-group\">\n" +
    "                <label class=\"col-md-4 control-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
    "                <div class=\"col-md-7\">\n" +
    "                    <s:select id=\"field__ReplaceID_\" list=\"_ReplaceDdList_\" listKey=\"_ReplaceDdKey_\" listValue=\"_ReplaceDdValue_\" name=\"_ReplaceFieldName_\" cssClass=\"form-control sds-dropdown\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_ />\n" +        
    "                </div>\n"+
    "            </div>\n";
//    private String mainDropdown_templateStr = 
//    "            <div class=\"form-horizontal form-group\">\n" +
//"                <label class=\"col-md-4 control-label\"><s:text name=\"_ReplaceLabel_\" /> _ReplaceReq_</label>\n" +
//"                <div class=\"col-md-5\">\n" +
//"                    <s:select id=\"field__ReplaceID_\" list=\"_ReplaceDdList_\" listKey=\"_ReplaceDdKey_\" listValue=\"_ReplaceDdValue_\" name=\"_ReplaceFieldName_\" cssClass=\"form-control sds-dropdown\" value=\"%{_ReplaceValueVar_}\" _ReplaceInputReq_ />\n" +        
//"                </div>\n" +
//"            </div>\r\n";
*/
    private String createActionClass = "";
    public String getCreateActionClass() {
        return createActionClass;
    }
    public void setCreateActionClass(String createActionClass) {
        if (createActionClass!=null && createActionClass.endsWith(".java")) {
            createActionClass = createActionClass.substring(0, createActionClass.length()-5);
        }
        this.createActionClass = createActionClass;
    }
    
    private List<String> addItemList = new ArrayList();
    private String getListStr = null;
    
    private String mainModel = null;
    private String configSearchField=null;
    private String configRetrievingCol=null;
    private String configDisplayHeader=null;
    private String editLink=null;
    private static final String packagePath = "src\\main\\resource\\";
    private static final String dynamicConfigPath = "web\\WEB-INF\\config\\";
    private Map<String, Map> selectedClassMap = null;
    private Map<String, Map> htmlMap = null;
    private Map<String, String> selectClassModelMap = new HashMap();
    HttpServletRequest globalRequest = null;
    
    private String initLower(String value) {
        return value.substring(0, 1).toLowerCase() + value.substring(1);
    }
    private String initCap(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }
    
    private String jspContent = null;
    public String getJspContent() {
        return jspContent;
    }
    
    private Set<String> fieldNameSet = new HashSet();
    
    private Boolean isView = Boolean.FALSE;
    private void loopSelectedClassMap2(Map<String, Map> loopingMap) throws Exception {
        List<String> classList = new ArrayList();
        for (String key : loopingMap.keySet()) {
            Map<String, Map> valueMap = loopingMap.get(key); 
            if (valueMap.size() > 0) {
                for (String valueKey : valueMap.keySet()) {
                    classList.add(selectClassModelMap.get(valueKey) + ":" + valueKey);
                }
            }
            if (!classList.isEmpty() && !isView) {
                System.out.print(selectClassModelMap.get(key) + "("+key+") : ");
                String temp = null;
                for (String value : classList) {
                    if (temp == null) {
                        temp = "setupMyChildList(\"get"+ initCap(lastFieldName(value.split(":")[1])) +"\", "+value.split(":")[0]+".class, \""+ initLower(value.split(":")[0]).replace("Model", "") +"Deleted\", Boolean.FALSE);";
                    } else {
                        temp += "\r\n                setupMyChildList(\"get"+ initCap(lastFieldName(value.split(":")[1])) +"\", "+value.split(":")[0]+".class, \""+ initLower(value.split(":")[0]).replace("Model", "") +"Deleted\", Boolean.FALSE);";
                    }
                }
                System.out.println("add to " + selectClassModelMap.get(key));
                System.out.println("childList = " + temp);
                FileReader reader = null;
                String line = null;
                String fullLines = "";
                try {
                    System.out.println("++++++++++++++++++++++++++++++++"+filePath+"src\\main\\java\\com\\sample\\ModelService.tmp");
                    reader = new FileReader(filePath+"src\\main\\java\\com\\sample\\ModelService.tmp");
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    while ((line = bufferedReader.readLine()) != null) {
    //                    Debug.printFrameworkDebug(line);
                        fullLines += line+"\n";
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    reader.close();
                }
                BufferedWriter output = null;
                try {
                    Class modelClass = getModelClass(selectClassModelMap.get(key));
                    fullLines = fullLines.replace("__replacePackage__", modelClass.getPackage().getName());
                    fullLines = fullLines.replace("__ReplaceModel__", selectClassModelMap.get(key));
                    fullLines = fullLines.replace("__ReplaceChildList__", temp);
                    File fileDir;
                    fileDir = new File(filePath+"src\\main\\java\\com\\"+modelClass.getPackage().getName().replaceAll("\\.","\\\\"));
//                    if (createActionClass.contains("\\")) {
//                        fullLines = fullLines.replaceAll("NewAction", createActionClass.substring(createActionClass.lastIndexOf("\\")+1));
//                        fileDir = new File(filePath+"src\\main\\java\\com\\"+createActionClass.substring(0, createActionClass.lastIndexOf("\\")));
//                    } else {
//                        fullLines = fullLines.replaceAll("NewAction", createActionClass);
//                        fileDir = new File(filePath+"src\\main\\java\\com");
//                    }
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    output = new BufferedWriter(new FileWriter(new File(filePath+"src\\main\\java\\com\\"+modelClass.getPackage().getName().replaceAll("\\.","\\\\")+"\\"+selectClassModelMap.get(key)+"Service.java")));
                    output.write(fullLines);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (output != null) {
                        output.close();
                    }
                }
            }
            classList.clear();
            loopSelectedClassMap2(loopingMap.get(key));
        }
//        for (String key : loopingMap.keySet()) {
//        }
    }
    
    public String view() throws Exception{
        try {
            isView = Boolean.TRUE;
            File projectDir = new File(filePath);
            if (!projectDir.exists()) {
                throw new CustomBaseException("Invalid project directory");
            }
            if (updateToProjectActionClass != null && updateToProjectActionClass.equalsIgnoreCase("Y")) {
                if (Validator.isEmpty(createActionClass)) {
                    throw new CustomBaseException("Action Class (include package) is required");
                }
            }

            if (Validator.isEmpty(jspDir)) {
//                jspDir = theApplicationCode_.substring(0, 1).toLowerCase() + theApplicationCode_.substring(1);
                jspDir = theApplicationCode_.toLowerCase();
            }

            String moreHiddenStr = null;
            for (String more : moreHidden) {
                if (moreHiddenStr == null) {
                    moreHiddenStr = "    private String "+more+" = null;\n";
                } else {
                    moreHiddenStr += "    private String "+more+" = null;\n";
                }
                moreHiddenStr += 
                        "    public String get"+initCap(more)+"() {\n" +
                        "        return "+more+";\n" + 
                        "    }\n" +
                        "    public void set"+initCap(more)+"(String value) {\n" +
                        "        this."+more+" = value;\n" +
                        "    }\n";
            }

            globalRequest = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            Debug.printFrameworkDebug("selectedClass = " + selectedClass);
            selectedClassMap = new TreeMap();
            Map allMap = new HashMap();
            selectClassModelMap = new HashMap();
    //        entryModels = new String[]{"model;ParentModel", "model.getList1;List1Model", "model.getList2;List2Model", "model.getList1.getList1_1;List1_1Model"};
            Debug.printFrameworkDebug("sout filePath =  " + filePath);
            if (filePath == null) {
                filePath = "C:\\Projects\\forNewProject\\";
            } 
            if (!filePath.endsWith("\\")) {
                filePath += "\\";
            }

            for (String selectedClassSetup : entryModels) {
                if (selectedClassSetup.split(";")[0].equals("theModel")) {
                    if(!Validator.isEmpty(globalRequest.getParameter("theModel_noOfColumn"))) {
                        mainCol = Integer.parseInt(globalRequest.getParameter("theModel_noOfColumn"));
                    }
                } else {
                    String temp = selectedClassSetup.split(";")[0].replaceAll("\\.", "_") + "_noOfColumn";
                    if(!Validator.isEmpty(globalRequest.getParameter(temp))) {
                        miColMap.put(selectedClassSetup.split(";")[0] + "_miCurrentCol", 0);
                        miColMap.put(selectedClassSetup.split(";")[0] + "_miTotalCol", 0);
                        miColMap.put(selectedClassSetup.split(";")[0] + "_miCol", Integer.parseInt(globalRequest.getParameter(temp)));
                    }
                }
                int count = 0;
                String mapKey = null;
                String[] selectedClassSetupArr = selectedClassSetup.split(";");
                Map currentMap = null;
                for (String setup : selectedClassSetupArr[0].split("\\.")) {
                    if (count++ == 0) {
                        mapKey = setup;
                        if (!allMap.containsKey(setup)) {
                            currentMap = new TreeMap();
//                            currentMap.put(setup+"_list", selectedClassSetupArr[0].substring(selectedClassSetupArr[0].lastIndexOf(".")+1));
                            selectedClassMap.put(setup, currentMap);
                            selectClassModelMap.put(setup, selectedClassSetupArr[1]);
                            allMap.put(setup, currentMap);
                        } else {
                            currentMap = (Map)allMap.get(setup);
                        }
                    } else {
                        if (!currentMap.containsKey(mapKey+"."+setup)) {
                            selectClassModelMap.put(mapKey+"."+setup, selectedClassSetupArr[1]);
                            currentMap.put(mapKey+"."+setup, new TreeMap());
                            allMap.put(mapKey+"."+setup, new TreeMap());
                        } else {
                            currentMap = (Map)currentMap.get(mapKey+"."+setup);
                        }
                        mapKey += "."+setup;
                    }
                }
            }

            Debug.printFrameworkDebug("selectedClassMap = " + selectedClassMap);
            Debug.printFrameworkDebug("selectClassModelMap = " + selectClassModelMap);
    //        for (String key : selectedClassMap.keySet()) {
    //
    //            Map map = selectedClassMap.get(key);
    //            
    //        }
            htmlBuilder = new StringBuilder();
            htmlBuilder_th = new StringBuilder();
            htmlBuilder_mi = new StringBuilder();
            htmlMap = new TreeMap();
            javaImportModels = null;
            tabBuilder = new StringBuilder();
            loopSelectedClassMap2(selectedClassMap);
            loopSelectedClassMap(selectedClassMap, 0, null, null);
            finalHtmlBuilder.setLength(0);
            buildHtml(selectedClassMap, 0);
            if (useTab.equalsIgnoreCase("Y")) {
                finalHtmlBuilder.append("\r\n</div>\r\n</div>");
            }
            String javaAddItem = null;
            String appRights = null;
            for (String addItem : addItemList) {
                String[] items = addItem.split(";");
//                String addWhat = items[2].replace("Model", "");
                String addWhat = items[1].substring(items[1].lastIndexOf(".")+1);
                addWhat = initCap(addWhat);
                if (appRights == null) {
//                    appRights = "processAdd"+items[2].replace("Model", "");
                    appRights = "processAdd"+addWhat;
                } else {
//                    appRights += ",processAdd"+items[2].replace("Model", "");
                    appRights += ",processAdd"+addWhat;
                }
                String javaList = null;
                Integer idx = 0;
                String[] listStrArr = items[1].split("\\.");
                Debug.printFrameworkDebug("item[1] = " + items[1]);

                for (String listStr : listStrArr) {
                    Debug.printFrameworkDebug("listStr = " + listStr);
                    idx++;
                    if (listStr.equals("theModel")) {
                        javaList = mainModelVarName;
                    } else {
                        if (listStrArr.length == idx) {
                            String temp = items[2] + " newItem = new "+items[2]+"();";
                            temp += "\n        newItem.set_hideShowMore(\"S\");\n        ";
                            javaList += ".get" + listStr.substring(0, 1).toUpperCase() + listStr.substring(1)+"().add(newItem);";
                            javaList = temp + javaList;
                        } else {
                            javaList += ".get" + listStr.substring(0, 1).toUpperCase() + listStr.substring(1)+"().get(Integer.parseInt(entryParentLvl))";
                        }
                    }
                }
                for (String tabGetListName : tabName_index.keySet()) {
                    if (javaList.startsWith(tabGetListName)) {
                        javaList += "\n        activeTab=\""+tabName_index.get(tabGetListName)+"\";";
                        break;
                    }
                }
                if (javaAddItem == null) {
                    javaAddItem = "public String processAdd"+addWhat+"() {\n"
                            + "        "+javaList+"\n"
                            + "        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;\n"
                            + "    }\n";
                } else {
                    javaAddItem += 
                              "    public String processAdd"+addWhat+"() {\n"
                            + "        "+javaList+"\n"
                            + "        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;\n"
                            + "    }\n";
                }
                javaAddItem += "\n";
            }
            StringBuilder packageStr = new StringBuilder();
            String newLine = "\r\n";
            String addedStartEnd = "#Added @"+ Formatter.formatDate(DateUtil.getCurrentDate(), "yyyyMMMdd") +" : "+ theApplicationCode_ +" : ";
            String addedStartEnd2 = "<!--Added @"+ Formatter.formatDate(DateUtil.getCurrentDate(), "yyyyMMMdd") +" : "+ theApplicationCode_ +" : ";
            packageStr.append(newLine).append(newLine).append(addedStartEnd).append("START").append(newLine);
            Debug.printFrameworkDebug("updateToProject = " + updateToProject);
//            if (updateToProject != null && updateToProject.equalsIgnoreCase("Y")) {
//                writeFile(filePath+packagePath, "package.properties", addedStartEnd+"START");
//                writeFile(filePath+packagePath, "package.properties", getFormatterApplicationCode_()+".appName="+theApplicationName_);
//                for (String addItem : addItemList) {
//                    String[] items = addItem.split(";");
//                    writeFile(filePath+packagePath, "package.properties", getFormatterApplicationCode_()+".add"+items[2].replace("Model", "")+"Title=Add New "+items[2].replace("Model", ""));
//                }
//                loopSelectedClassMap_package(selectedClassMap);
//                writeFile(filePath+packagePath, "package.properties", addedStartEnd+"END");
//            }
            Debug.printFrameworkDebug("tempJava = " + javaAddItem);
            Debug.printFrameworkDebug("finalHtmlBuilder = " + finalHtmlBuilder);
            Debug.printFrameworkDebug("mainModel = " + mainModel);
            Class modelClass = getModelClass(mainModel);
            Table table = (Table)modelClass.getAnnotation(Table.class);
            jspContent = finalHtmlBuilder.toString().replaceAll("theModel", mainModelVarName);
            FileReader reader = null;
            String line = null;
            String fullLines = "";
            File jspTemplate = new File(filePath+"web\\sample\\entry\\generateEntryTemplate.jsp");
            if (!jspTemplate.exists()) {
                throw new CustomBaseException("Path ["+filePath+"web\\sample\\entry\\generateEntryTemplate.jsp"+"] not exists.");
            }
            try {
                reader = new FileReader(filePath+"\\web\\no_deco\\viewTemplate.jsp");
                BufferedReader bufferedReader = new BufferedReader(reader);
                while ((line = bufferedReader.readLine()) != null) {
//                    Debug.printFrameworkDebug(line);
                    fullLines += line+"\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                reader.close();
            }
            BufferedWriter output = null;
            try {
                fullLines = fullLines.replace("__jspContent__", jspContent);
                System.out.println("** DO REPLACE **");
                if (hasUppy) {
                    fullLines = fullLines.replace("_replace_uppy_js_", uppy_js);
                    fullLines = fullLines.replace("_replace_uppy_css_", uppy_css);
                    fullLines = fullLines.replace("_replace_uppy_style_", uppy_style);
                } else {
                    fullLines = fullLines.replace("_replace_uppy_js_", "");
                    fullLines = fullLines.replace("_replace_uppy_css_", "");
                    fullLines = fullLines.replace("_replace_uppy_style_", "");
                }
                output = new BufferedWriter(new FileWriter(new File(filePath+"\\web\\sample\\view.jsp")));
                output.write(fullLines);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (output != null) {
                    output.close();
                }
                Thread.sleep(2500);
            }
            if (1==1) {
                System.out.println("1==1");
                Object currentModel = null;
                Class currentClass = null;
                for (String field_name : fieldNameSet) {
                    System.out.println("field_name = " + field_name);
                    for (String name : field_name.split("\\.")) {
                        System.out.println("name = " + name);
                        if (name.equals("theModel")) {
                            System.out.println("name = theModel...");
                            if (model == null) {
                                System.out.println("model == null");
                                currentClass = getModelClass(mainModel);
                                model = currentClass.newInstance();
                                currentModel = model;
                                System.out.println("set model = currentModel");
                            } else {
                                System.out.println("model != null");
                                currentClass = model.getClass();
                                currentModel = model;
                            }
                        } else {
                            System.out.println("currentClass = " + currentClass);
                            Method m = currentClass.getMethod("get" + initCap(name));
                            List list = (List) m.invoke(currentModel);
                            Annotation annotation = m.getAnnotationsByType(OneToMany.class)[0];
                            Class classNow = ((OneToMany)annotation).targetEntity();
                            System.out.println("name = " + name);
                            if (list != null & !list.isEmpty()) {
                                System.out.println("List Not Empty--");
                                currentClass = classNow;
                                currentModel = list.get(0);
                            } else {
                                System.out.println("List Empty--");
//                            for (int i = 0; i < annotations.length; i++) {
//                                if (annotations[i].annotationType().equals(OneToMany.class)) {
                                    list = new ArrayList();
                                    Object objectNow = classNow.newInstance();
                                    list.add(objectNow);
                                    m = currentClass.getMethod("set"+ initCap(name), List.class);
                                    m.invoke(currentModel, list);
                                    currentClass = classNow;
                                    currentModel = objectNow;
//                                }
//                            }

//                            Annotation[] annotations = m.getAnnotations();
//                            for (int i = 0; i < annotations.length; i++) {
//                                if (annotations[i].annotationType().equals(OneToMany.class)) {
//                                    Class classNow = ((OneToMany)annotations[i]).targetEntity();
//                                    List list = new ArrayList();
//                                    list.add(classNow.newInstance());
//                                    m = currentClass.getMethod("s"+name.substring(1), List.class);
//                                    m.invoke(currentModel, list);
//                                }
//                            }
                            }
                        }
                    }
                }
                return "view";
            }
//            System.out.println("finalHtmlBuilder = " + finalHtmlBuilder.toString().replaceAll("theModel", mainModelVarName));
//            if (createDynamicConfig != null && createDynamicConfig.equalsIgnoreCase("Y")) {
//    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"START");
//                String pk_name = null;
//                for(Method method : modelClass.getDeclaredMethods()){
//        //                String name = method.getName();
//                    if (!method.getName().startsWith("get")) continue;
//                    Annotation[] annotations = method.getAnnotations();
//                    Boolean found = Boolean.FALSE;
//                    for (int i = 0; i < annotations.length; i++) {
//                        if (annotations[i].annotationType().equals(Id.class)) {
//                            found = Boolean.TRUE;
//                        }
//                        if (annotations[i].annotationType().equals(Column.class)) {
//                            pk_name = ((Column)annotations[i]).name();
//                        }
//                    }
//                    if (found) {
//                        break;
//                    }
//                }
//
//    String config = 
//    "        <dynamicAction name=\""+theApplicationCode_+"\">\n" +
//    "            <property name=\"searchDescription\" value=\""+theApplicationCode_+".appName\" />\n" +
//    "            <property name=\"searchFields\" value=\""+configSearchField+"\" />\n" +
//    "            <property name=\"retrievingColumns\" value=\""+pk_name+","+configRetrievingCol+"\" />\n" +
//    "            <property name=\"displayFields\" value=\""+configRetrievingCol+"\" />\n" +
//    "            <property name=\"displayFieldsHeader\" value=\""+configDisplayHeader+"\" />\n" +
//    "            <property name=\"sortingFields\" value=\"asDisplayFields\" />\n" +
//    "            <property name=\"sqlTables\" value=\""+table.name()+" \" />\n" +
//    "            <property name=\"primaryKey\" value=\""+ pk_name +"\" />\n" +
//    "            <property name=\"editLinkColumn\" value=\""+editLink+"\" />\n" +
//    "        </dynamicAction>\n" +
//    "    "+addedStartEnd2+"END-->\n" +
//    "    </dynamics>";
//                FileReader reader = null;
//                String line = null;
//                String fullLines = "";
//                try {
//                    reader = new FileReader(filePath+dynamicConfigPath+"dynamic-config.xml");
//                    BufferedReader bufferedReader = new BufferedReader(reader);
//                    while ((line = bufferedReader.readLine()) != null) {
//    //                    Debug.printFrameworkDebug(line);
//                        fullLines += line+"\n";
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    reader.close();
//                }
//                BufferedWriter output = null;
//                try {
//                    fullLines = fullLines.replace("</dynamics>", addedStartEnd2+"START-->\n"+config);
//                    output = new BufferedWriter(new FileWriter(new File(filePath+dynamicConfigPath+"dynamic-config.xml")));
//                    output.write(fullLines);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (output != null) {
//                        output.close();
//                    }
//                }
//    //            Debug.printFrameworkDebug("config = " + config);
//    //            writeFile(filePath+packagePath, "dynamic-config.xml", config);
//    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"END");
//            }
//            if (createStrutsXML != null && createStrutsXML.equalsIgnoreCase("Y")) {
//    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"START");
//                String pk_name = null;
//                for(Method method : modelClass.getDeclaredMethods()){
//        //                String name = method.getName();
//                    if (!method.getName().startsWith("get")) continue;
//                    Annotation[] annotations = method.getAnnotations();
//                    Boolean found = Boolean.FALSE;
//                    for (int i = 0; i < annotations.length; i++) {
//                        if (annotations[i].annotationType().equals(Id.class)) {
//                            found = Boolean.TRUE;
//                        }
//                        if (annotations[i].annotationType().equals(Column.class)) {
//                            pk_name = ((Column)annotations[i]).name();
//                        }
//                    }
//                    if (found) {
//                        break;
//                    }
//                }
//    String config = 
//    "        <action name=\"*"+theApplicationCode_+"\" method=\"{1}\" class=\"com."+createActionClass.replaceAll("\\\\", ".")+"\">\n" +
//    "            <result name=\"load_add_page\" >/"+jspDir.replaceAll("\\\\", "/")+"/addEdit"+getFormatterApplicationCode_()+".jsp</result>\n" +
//    "            <result name=\"load_edit_page\" >/"+jspDir.replaceAll("\\\\", "/")+"/addEdit"+getFormatterApplicationCode_()+".jsp</result>\n" +
//    "        </action>\n"+
//    "    "+addedStartEnd2+"END-->\n" +
//    "    </package>";
//                FileReader reader = null;
//                String line = null;
//                String fullLines = "";
//                try {
//                    reader = new FileReader(filePath+packagePath+"struts.xml");
//                    BufferedReader bufferedReader = new BufferedReader(reader);
//                    while ((line = bufferedReader.readLine()) != null) {
//    //                    Debug.printFrameworkDebug(line);
//                        fullLines += line+"\n";
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    reader.close();
//                }
//                BufferedWriter output = null;
//                try {
//                    fullLines = fullLines.replace("</package>", addedStartEnd2+"START-->\n"+config);
//                    output = new BufferedWriter(new FileWriter(new File(filePath+packagePath+"struts.xml")));
//                    output.write(fullLines);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (output != null) {
//                        output.close();
//                    }
//                }
//    //            Debug.printFrameworkDebug("config = " + config);
//    //            writeFile(filePath+packagePath, "dynamic-config.xml", config);
//    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"END");
//            }
//            if (updateToProjectJsp != null && updateToProjectJsp.equalsIgnoreCase("Y")) {
//    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"START");
//                FileReader reader = null;
//                String line = null;
//                String fullLines = "";
//                File jspTemplate = new File(filePath+"web\\sample\\entry\\generateEntryTemplate.jsp");
//                if (!jspTemplate.exists()) {
//                    throw new CustomBaseException("Path ["+filePath+"web\\sample\\entry\\generateEntryTemplate.jsp"+"] not exists.");
//                }
//                try {
//                    reader = new FileReader(filePath+"web\\sample\\entry\\generateEntryTemplate.jsp");
//                    BufferedReader bufferedReader = new BufferedReader(reader);
//                    while ((line = bufferedReader.readLine()) != null) {
//    //                    Debug.printFrameworkDebug(line);
//                        fullLines += line+"\n";
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    reader.close();
//                }
//                BufferedWriter output = null;
//                try {
//                    try {
//                        jspDir = jspDir.replaceAll("/", "\\\\"); 
//                    } catch (Exception e) {
//                    }
////                    fullLines = fullLines.replaceAll("__initCapAction__", getInitCapAction());
//                    fullLines = fullLines.replaceAll("__initCapAction__", theApplicationCode_);
//                    fullLines = fullLines.replaceAll("theModel.ID", mainModelVarName+".ID");
//                    fullLines = fullLines.replaceAll("__formId__", theApplicationCode_);
//                    fullLines = fullLines.replaceAll("ReplaceApplicationTitle", "<s:text name=\""+getFormatterApplicationCode_()+".appName\"/>");
//                    fullLines = fullLines.replace("//ReplaceContentHere", finalHtmlBuilder.toString().replaceAll("theModel", mainModelVarName));
//                    File fileDir = new File(filePath+"web\\"+jspDir);
//                    if (!fileDir.exists()) {
//                        fileDir.mkdirs();
//                    }
//                    output = new BufferedWriter(new FileWriter(new File(filePath+"web\\"+jspDir+"\\addEdit"+getFormatterApplicationCode_()+".jsp")));
//                    output.write(fullLines);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (output != null) {
//                        output.close();
//                    }
//                }
//    //            Debug.printFrameworkDebug("config = " + config);
//    //            writeFile(filePath+packagePath, "dynamic-config.xml", config);
//    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"END");
//            }
//            if (insertApplicationRecord != null && insertApplicationRecord.equalsIgnoreCase("Y")) {
//                try {
//                    BaseDAO dao = baseDAO;
//                    Module module = null;
//                    if (Validator.isEmpty(globalRequest.getParameter("appModuleCode"))) {
//                        module = (Module)dao.getSession().getNamedQuery("Module.findByModuleCode").setParameter("module_code", "SAM").uniqueResult();
//                    } else {
//                        module = (Module)dao.getSession().getNamedQuery("Module.findByModuleCode").setParameter("module_code", globalRequest.getParameter("appModuleCode")).uniqueResult();
//                    }
//                    Application application = new Application();
//                    application.setAttached_module_id(module.getModule_id());
//                    dao.getSession().evict(module);
//                    application.setApplication_code(theApplicationCode_);
//                    application.setApplication_name(theApplicationName_);
//                    application.setAction_class(createActionClass.substring(createActionClass.lastIndexOf("\\")+1));
//                    application.setAction_name("dynamicAction?action="+theApplicationCode_+"&retrieve=n");
//                    application.setCreate_right("Y");
//                    application.setRetrieve_right("Y");
//                    application.setUpdate_right("Y");
//                    application.setDelete_right("Y");
//
//                    if (appRights != null) {
//                        ApplicationRights right = new ApplicationRights();
//                        right.setApp_rights_code(theApplicationCode_+"Mgmt");
//                        right.setApp_rights_description(theApplicationName_+" Mgmt.");
//                        right.setApp_rights_methods(appRights);
//                        application.getRightsList().add(right);
//                    }
//                    ApplicationDAOImpl appDAO = new ApplicationDAOImpl();
//                    appDAO.insert(application);
//                } catch (BaseException be) {
//                    r = "rmsg;"+be.getMessage();
//                    return "divSubmitForm";
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    r = "rmsg;"+getText("generateEntry.errorGenerate");
//                    return "divSubmitForm";
//                }
//            }
//            if (updateToProjectActionClass != null && updateToProjectActionClass.equalsIgnoreCase("Y")) {
//    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"START");
//                String actionPackageStr = null;
//                try {
//                    createActionClass = createActionClass.replaceAll("/", "\\\\"); 
//                } catch (Exception e) {
//                }
//                if (createActionClass.contains("\\")) {
//                    actionPackageStr = createActionClass.substring(0, createActionClass.lastIndexOf("\\")).replaceAll("\\\\", ".");
//                }
//                Debug.printFrameworkDebug("actionPackageStr = " + actionPackageStr);
//                FileReader reader = null;
//                String line = null;
//                String fullLines = "";
////                try {
////                    reader = new FileReader(filePath+"src\\main\\java\\com\\sample\\NewAction.java");
////                    BufferedReader bufferedReader = new BufferedReader(reader);
////                    while ((line = bufferedReader.readLine()) != null) {
////    //                    Debug.printFrameworkDebug(line);
////                        fullLines += line+"\n";
////                    }
////                } catch (Exception e) {
////                    throw e;
////                } finally {
////                    reader.close();
////                }
//                BufferedWriter output = null;
////                try {
////                    if (actionPackageStr == null) {
////                        fullLines = fullLines.replace("package com.sample", "package com");
////                    } else {
////                        fullLines = fullLines.replace("package com.sample", "package com."+actionPackageStr);
////                    }
////                    fullLines = fullLines.replace("__ApplicationCode__", theApplicationCode_);
////                    fullLines = fullLines.replace("model = new ParentModel();", "model = new "+ mainModel +"();");
////                    fullLines = fullLines.replace("<ParentModel>", "<"+mainModel+">");
////                    fullLines = fullLines.replace("//OverrideAddItem", (javaAddItem==null?"":javaAddItem));
////                    fullLines = fullLines.replace("//OverrideGetList", (getListStr==null?"":getListStr));
////                    fullLines = fullLines.replace("//ImportModelsHere", javaImportModels);
////                    fullLines = fullLines.replace("public ParentModel getModel()", "public "+ mainModel +" getModel()");
////                    File fileDir;
////                    if (createActionClass.contains("\\")) {
////                        fullLines = fullLines.replaceAll("NewAction", createActionClass.substring(createActionClass.lastIndexOf("\\")+1));
////                        fileDir = new File(filePath+"src\\main\\java\\com\\"+createActionClass.substring(0, createActionClass.lastIndexOf("\\")));
////                    } else {
////                        fullLines = fullLines.replaceAll("NewAction", createActionClass);
////                        fileDir = new File(filePath+"src\\main\\java\\com");
////                    }
////                    if (!fileDir.exists()) {
////                        fileDir.mkdirs();
////                    }
////                    output = new BufferedWriter(new FileWriter(new File(filePath+"src\\main\\java\\com\\"+createActionClass+".java")));
////                    output.write(fullLines);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                } finally {
////                    if (output != null) {
////                        output.close();
////                    }
////                }
//    //            Debug.printFrameworkDebug("config = " + config);
//    //            writeFile(filePath+packagePath, "dynamic-config.xml", config);
//    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"END");
//            }
        } catch (BaseException be) {
            r = "rmsg;"+be.getMessage();
            return "divSubmitForm";
        } catch (Exception e) {
            e.printStackTrace();
            r = "rmsg;"+getText("generateEntry.errorGenerate");
            return "divSubmitForm";
        }
        r = "rmsg;"+getText("generateEntry.generated");
        return "divSubmitForm";
    }
    public String create() throws Exception{
        try {
            File projectDir = new File(filePath);
            if (!projectDir.exists()) {
                throw new CustomBaseException("Invalid project directory");
            }
            if (updateToProjectActionClass != null && updateToProjectActionClass.equalsIgnoreCase("Y")) {
                if (Validator.isEmpty(createActionClass)) {
                    throw new CustomBaseException("Action Class (include package) is required");
                }
            }

            if (Validator.isEmpty(jspDir)) {
//                jspDir = theApplicationCode_.substring(0, 1).toLowerCase() + theApplicationCode_.substring(1);
                jspDir = theApplicationCode_.toLowerCase();
            }

            String moreHiddenStr = null;
            for (String more : moreHidden) {
                if (moreHiddenStr == null) {
                    moreHiddenStr = "    private String "+more+" = null;\n";
                } else {
                    moreHiddenStr += "    private String "+more+" = null;\n";
                }
                moreHiddenStr += 
                        "    public String get"+initCap(more)+"() {\n" +
                        "        return "+more+";\n" + 
                        "    }\n" +
                        "    public void set"+initCap(more)+"(String value) {\n" +
                        "        this."+more+" = value;\n" +
                        "    }\n";
            }

            globalRequest = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            Debug.printFrameworkDebug("selectedClass = " + selectedClass);
            selectedClassMap = new TreeMap();
            Map allMap = new HashMap();
            selectClassModelMap = new HashMap();
    //        entryModels = new String[]{"model;ParentModel", "model.getList1;List1Model", "model.getList2;List2Model", "model.getList1.getList1_1;List1_1Model"};
            Debug.printFrameworkDebug("sout filePath =  " + filePath);
            if (filePath == null) {
                filePath = "C:\\Projects\\forNewProject\\";
            } 
            if (!filePath.endsWith("\\")) {
                filePath += "\\";
            }

            for (String selectedClassSetup : entryModels) {
                if (selectedClassSetup.split(";")[0].equals("theModel")) {
                    if(!Validator.isEmpty(globalRequest.getParameter("theModel_noOfColumn"))) {
                        mainCol = Integer.parseInt(globalRequest.getParameter("theModel_noOfColumn"));
                    }
                } else {
                    String temp = selectedClassSetup.split(";")[0].replaceAll("\\.", "_") + "_noOfColumn";
                    if(!Validator.isEmpty(globalRequest.getParameter(temp))) {
                        miColMap.put(selectedClassSetup.split(";")[0] + "_miCurrentCol", 0);
                        miColMap.put(selectedClassSetup.split(";")[0] + "_miTotalCol", 0);
                        miColMap.put(selectedClassSetup.split(";")[0] + "_miCol", Integer.parseInt(globalRequest.getParameter(temp)));
                    }
                }
                int count = 0;
                String mapKey = null;
                String[] selectedClassSetupArr = selectedClassSetup.split(";");
                Map currentMap = null;
                for (String setup : selectedClassSetupArr[0].split("\\.")) {
                    if (count++ == 0) {
                        mapKey = setup;
                        if (!allMap.containsKey(setup)) {
                            currentMap = new TreeMap();
//                            currentMap.put(setup+"_list", selectedClassSetupArr[0].substring(selectedClassSetupArr[0].lastIndexOf(".")+1));
                            selectedClassMap.put(setup, currentMap);
                            selectClassModelMap.put(setup, selectedClassSetupArr[1]);
                            allMap.put(setup, currentMap);
                        } else {
                            currentMap = (Map)allMap.get(setup);
                        }
                    } else {
                        if (!currentMap.containsKey(mapKey+"."+setup)) {
                            selectClassModelMap.put(mapKey+"."+setup, selectedClassSetupArr[1]);
                            currentMap.put(mapKey+"."+setup, new TreeMap());
                            allMap.put(mapKey+"."+setup, new TreeMap());
                        } else {
                            currentMap = (Map)currentMap.get(mapKey+"."+setup);
                        }
                        mapKey += "."+setup;
                    }
                }
            }

            Debug.printFrameworkDebug("selectedClassMap = " + selectedClassMap);
            Debug.printFrameworkDebug("selectClassModelMap = " + selectClassModelMap);
    //        for (String key : selectedClassMap.keySet()) {
    //
    //            Map map = selectedClassMap.get(key);
    //            
    //        }
            htmlBuilder = new StringBuilder();
            htmlBuilder_th = new StringBuilder();
            htmlBuilder_mi = new StringBuilder();
            htmlMap = new TreeMap();
            javaImportModels = null;
            tabBuilder = new StringBuilder();
            loopSelectedClassMap(selectedClassMap, 0, null, null);
            finalHtmlBuilder.setLength(0);
            buildHtml(selectedClassMap, 0);
            if (useTab.equalsIgnoreCase("Y")) {
                finalHtmlBuilder.append("\r\n</div>\r\n</div>");
            }
            String javaAddItem = null;
            String appRights = null;
            for (String addItem : addItemList) {
                String[] items = addItem.split(";");
//                String addWhat = items[2].replace("Model", "");
                String addWhat = items[1].substring(items[1].lastIndexOf(".")+1);
                addWhat = initCap(addWhat);
                if (appRights == null) {
//                    appRights = "processAdd"+items[2].replace("Model", "");
                    appRights = "processAdd"+addWhat;
                } else {
//                    appRights += ",processAdd"+items[2].replace("Model", "");
                    appRights += ",processAdd"+addWhat;
                }
                String javaList = null;
                Integer idx = 0;
                String[] listStrArr = items[1].split("\\.");
                Debug.printFrameworkDebug("item[1] = " + items[1]);

                for (String listStr : listStrArr) {
                    Debug.printFrameworkDebug("listStr = " + listStr);
                    idx++;
                    if (listStr.equals("theModel")) {
                        javaList = mainModelVarName;
                    } else {
                        if (listStrArr.length == idx) {
                            String temp = items[2] + " newItem = new "+items[2]+"();";
                            temp += "\n        newItem.set_hideShowMore(\"S\");\n        ";
                            javaList += ".get" + listStr.substring(0, 1).toUpperCase() + listStr.substring(1)+"().add(newItem);";
                            javaList = temp + javaList;
                        } else {
                            javaList += ".get" + listStr.substring(0, 1).toUpperCase() + listStr.substring(1)+"().get(Integer.parseInt(entryParentLvl))";
                        }
                    }
                }
                for (String tabGetListName : tabName_index.keySet()) {
                    if (javaList.startsWith(tabGetListName)) {
                        javaList += "\n        activeTab=\""+tabName_index.get(tabGetListName)+"\";";
                        break;
                    }
                }
                if (javaAddItem == null) {
                    javaAddItem = "public String processAdd"+addWhat+"() {\n"
                            + "        "+javaList+"\n"
                            + "        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;\n"
                            + "    }\n";
                } else {
                    javaAddItem += 
                              "    public String processAdd"+addWhat+"() {\n"
                            + "        "+javaList+"\n"
                            + "        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;\n"
                            + "    }\n";
                }
                javaAddItem += "\n";
            }
            StringBuilder packageStr = new StringBuilder();
            String newLine = "\r\n";
            String addedStartEnd = "#Added @"+ Formatter.formatDate(DateUtil.getCurrentDate(), "yyyyMMMdd") +" : "+ theApplicationCode_ +" : ";
            String addedStartEnd2 = "<!--Added @"+ Formatter.formatDate(DateUtil.getCurrentDate(), "yyyyMMMdd") +" : "+ theApplicationCode_ +" : ";
            packageStr.append(newLine).append(newLine).append(addedStartEnd).append("START").append(newLine);
            Debug.printFrameworkDebug("updateToProject = " + updateToProject);
            if (updateToProject != null && updateToProject.equalsIgnoreCase("Y")) {
                writeFile(filePath+packagePath, "package.properties", addedStartEnd+"START");
                writeFile(filePath+packagePath, "package.properties", getFormatterApplicationCode_()+".appName="+theApplicationName_);
                for (String addItem : addItemList) {
                    String[] items = addItem.split(";");
                    writeFile(filePath+packagePath, "package.properties", getFormatterApplicationCode_()+".add"+items[2].replace("Model", "")+"Title=Add New "+items[2].replace("Model", ""));
                }
                loopSelectedClassMap_package(selectedClassMap);
                writeFile(filePath+packagePath, "package.properties", addedStartEnd+"END");
            }
            Debug.printFrameworkDebug("tempJava = " + javaAddItem);
            Debug.printFrameworkDebug("finalHtmlBuilder = " + finalHtmlBuilder);
            Debug.printFrameworkDebug("mainModel = " + mainModel);
            Class modelClass = getModelClass(mainModel);
            Table table = (Table)modelClass.getAnnotation(Table.class);
            if (createDynamicConfig != null && createDynamicConfig.equalsIgnoreCase("Y")) {
    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"START");
                String pk_name = null;
                for(Method method : modelClass.getDeclaredMethods()){
        //                String name = method.getName();
                    if (!method.getName().startsWith("get")) continue;
                    Annotation[] annotations = method.getAnnotations();
                    Boolean found = Boolean.FALSE;
                    for (int i = 0; i < annotations.length; i++) {
                        if (annotations[i].annotationType().equals(Id.class)) {
                            found = Boolean.TRUE;
                        }
                        if (annotations[i].annotationType().equals(Column.class)) {
                            pk_name = ((Column)annotations[i]).name();
                        }
                    }
                    if (found) {
                        break;
                    }
                }

    String config = 
    "        <dynamicAction name=\""+theApplicationCode_+"\">\n" +
    "            <property name=\"searchDescription\" value=\""+theApplicationCode_+".appName\" />\n" +
    "            <property name=\"searchFields\" value=\""+configSearchField+"\" />\n" +
    "            <property name=\"retrievingColumns\" value=\""+pk_name+","+configRetrievingCol+"\" />\n" +
    "            <property name=\"displayFields\" value=\""+configRetrievingCol+"\" />\n" +
    "            <property name=\"displayFieldsHeader\" value=\""+configDisplayHeader+"\" />\n" +
    "            <property name=\"sortingFields\" value=\"asDisplayFields\" />\n" +
    "            <property name=\"sqlTables\" value=\""+table.name()+" \" />\n" +
    "            <property name=\"primaryKey\" value=\""+ pk_name +"\" />\n" +
    "            <property name=\"editLinkColumn\" value=\""+editLink+"\" />\n" +
    "        </dynamicAction>\n" +
    "    "+addedStartEnd2+"END-->\n" +
    "    </dynamics>";
                FileReader reader = null;
                String line = null;
                String fullLines = "";
                try {
                    reader = new FileReader(filePath+dynamicConfigPath+"dynamic-config.xml");
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    while ((line = bufferedReader.readLine()) != null) {
    //                    Debug.printFrameworkDebug(line);
                        fullLines += line+"\n";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    reader.close();
                }
                BufferedWriter output = null;
                try {
                    fullLines = fullLines.replace("</dynamics>", addedStartEnd2+"START-->\n"+config);
                    output = new BufferedWriter(new FileWriter(new File(filePath+dynamicConfigPath+"dynamic-config.xml")));
                    output.write(fullLines);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (output != null) {
                        output.close();
                    }
                }
    //            Debug.printFrameworkDebug("config = " + config);
    //            writeFile(filePath+packagePath, "dynamic-config.xml", config);
    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"END");
            }
            if (createStrutsXML != null && createStrutsXML.equalsIgnoreCase("Y")) {
    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"START");
                String pk_name = null;
                for(Method method : modelClass.getDeclaredMethods()){
        //                String name = method.getName();
                    if (!method.getName().startsWith("get")) continue;
                    Annotation[] annotations = method.getAnnotations();
                    Boolean found = Boolean.FALSE;
                    for (int i = 0; i < annotations.length; i++) {
                        if (annotations[i].annotationType().equals(Id.class)) {
                            found = Boolean.TRUE;
                        }
                        if (annotations[i].annotationType().equals(Column.class)) {
                            pk_name = ((Column)annotations[i]).name();
                        }
                    }
                    if (found) {
                        break;
                    }
                }
    String config = 
    "        <action name=\"*"+theApplicationCode_+"\" method=\"{1}\" class=\"com."+createActionClass.replaceAll("\\\\", ".")+"\">\n" +
    "            <result name=\"load_add_page\" >/"+jspDir.replaceAll("\\\\", "/")+"/addEdit"+getFormatterApplicationCode_()+".jsp</result>\n" +
    "            <result name=\"load_edit_page\" >/"+jspDir.replaceAll("\\\\", "/")+"/addEdit"+getFormatterApplicationCode_()+".jsp</result>\n" +
    "        </action>\n"+
    "    "+addedStartEnd2+"END-->\n" +
    "    </package>";
                FileReader reader = null;
                String line = null;
                String fullLines = "";
                try {
                    reader = new FileReader(filePath+packagePath+"struts.xml");
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    while ((line = bufferedReader.readLine()) != null) {
    //                    Debug.printFrameworkDebug(line);
                        fullLines += line+"\n";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    reader.close();
                }
                BufferedWriter output = null;
                try {
                    fullLines = fullLines.replace("</package>", addedStartEnd2+"START-->\n"+config);
                    output = new BufferedWriter(new FileWriter(new File(filePath+packagePath+"struts.xml")));
                    output.write(fullLines);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (output != null) {
                        output.close();
                    }
                }
    //            Debug.printFrameworkDebug("config = " + config);
    //            writeFile(filePath+packagePath, "dynamic-config.xml", config);
    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"END");
            }
            if (updateToProjectJsp != null && updateToProjectJsp.equalsIgnoreCase("Y")) {
    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"START");
                FileReader reader = null;
                String line = null;
                String fullLines = "";
                File jspTemplate = new File(filePath+"web\\sample\\entry\\generateEntryTemplate.jsp");
                if (!jspTemplate.exists()) {
                    throw new CustomBaseException("Path ["+filePath+"web\\sample\\entry\\generateEntryTemplate.jsp"+"] not exists.");
                }
                try {
                    reader = new FileReader(filePath+"web\\sample\\entry\\generateEntryTemplate.jsp");
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    while ((line = bufferedReader.readLine()) != null) {
    //                    Debug.printFrameworkDebug(line);
                        fullLines += line+"\n";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    reader.close();
                }
                BufferedWriter output = null;
                try {
                    try {
                        jspDir = jspDir.replaceAll("/", "\\\\"); 
                    } catch (Exception e) {
                    }
//                    fullLines = fullLines.replaceAll("__initCapAction__", getInitCapAction());
                    fullLines = fullLines.replaceAll("__initCapAction__", theApplicationCode_);
                    fullLines = fullLines.replaceAll("theModel.ID", mainModelVarName+".ID");
                    fullLines = fullLines.replaceAll("__formId__", theApplicationCode_);
                    if (hasUppy) {
                        fullLines = fullLines.replaceAll("_replace_uppy_js_", uppy_js);
                        fullLines = fullLines.replaceAll("_replace_uppy_css_", uppy_css);
                        fullLines = fullLines.replaceAll("_replace_uppy_style_", uppy_style);
                    } else {
                        fullLines = fullLines.replaceAll("_replace_uppy_js_", "");
                        fullLines = fullLines.replaceAll("_replace_uppy_css_", "");
                        fullLines = fullLines.replaceAll("_replace_uppy_style_", "");
                    }
                    fullLines = fullLines.replaceAll("ReplaceApplicationTitle", "<s:text name=\""+getFormatterApplicationCode_()+".appName\"/>");
                    String temp = finalHtmlBuilder.toString().replaceAll("theModel", mainModelVarName);
                    System.out.println("---------------------- jspContent ---------------------");
                System.out.println(temp);
                System.out.println("---------------------- jspContent ---------------------");
                    fullLines = fullLines.replace("//ReplaceContentHere", temp);
                    File fileDir = new File(filePath+"web\\"+jspDir);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    output = new BufferedWriter(new FileWriter(new File(filePath+"web\\"+jspDir+"\\addEdit"+getFormatterApplicationCode_()+".jsp")));
                    output.write(fullLines);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (output != null) {
                        output.close();
                    }
                }
    //            Debug.printFrameworkDebug("config = " + config);
    //            writeFile(filePath+packagePath, "dynamic-config.xml", config);
    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"END");
            }
            if (insertApplicationRecord != null && insertApplicationRecord.equalsIgnoreCase("Y")) {
                try {
                    BaseDAO dao = baseDAO;
                    Module module = null;
                    if (Validator.isEmpty(globalRequest.getParameter("appModuleCode"))) {
                        module = (Module)dao.getSession().getNamedQuery("Module.findByModuleCode").setParameter("module_code", "SAM").uniqueResult();
                    } else {
                        module = (Module)dao.getSession().getNamedQuery("Module.findByModuleCode").setParameter("module_code", globalRequest.getParameter("appModuleCode")).uniqueResult();
                    }
                    Application application = new Application();
                    application.setAttached_module_id(module.getModule_id());
                    dao.getSession().evict(module);
                    application.setApplication_code(theApplicationCode_);
                    application.setApplication_name(theApplicationName_);
                    application.setAction_class(createActionClass.substring(createActionClass.lastIndexOf("\\")+1));
                    application.setAction_name("dynamicAction?action="+theApplicationCode_+"&retrieve=n");
                    application.setCreate_right("Y");
                    application.setRetrieve_right("Y");
                    application.setUpdate_right("Y");
                    application.setDelete_right("Y");

                    if (appRights != null) {
                        ApplicationRights right = new ApplicationRights();
                        right.setApp_rights_code(theApplicationCode_+"Mgmt");
                        right.setApp_rights_description(theApplicationName_+" Mgmt.");
                        right.setApp_rights_methods(appRights);
                        application.getRightsList().add(right);
                    }
                    ApplicationDAOImpl appDAO = new ApplicationDAOImpl();
                    appDAO.insert(application);
                } catch (BaseException be) {
                    r = "rmsg;"+be.getMessage();
                    return "divSubmitForm";
                } catch (Exception e) {
                    e.printStackTrace();
                    r = "rmsg;"+getText("generateEntry.errorGenerate");
                    return "divSubmitForm";
                }
            }
            if (updateToProjectActionClass != null && updateToProjectActionClass.equalsIgnoreCase("Y")) {
    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"START");
                String actionPackageStr = null;
                try {
                    createActionClass = createActionClass.replaceAll("/", "\\\\"); 
                } catch (Exception e) {
                }
                if (createActionClass.contains("\\")) {
                    actionPackageStr = createActionClass.substring(0, createActionClass.lastIndexOf("\\")).replaceAll("\\\\", ".");
                }
                Debug.printFrameworkDebug("actionPackageStr = " + actionPackageStr);
                FileReader reader = null;
                String line = null;
                String fullLines = "";
                try {
                    reader = new FileReader(filePath+"src\\main\\java\\com\\sample\\NewAction.java");
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    while ((line = bufferedReader.readLine()) != null) {
    //                    Debug.printFrameworkDebug(line);
                        fullLines += line+"\n";
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    reader.close();
                }
                BufferedWriter output = null;
                try {
                    if (actionPackageStr == null) {
                        fullLines = fullLines.replace("package com.sample", "package com");
                    } else {
                        fullLines = fullLines.replace("package com.sample", "package com."+actionPackageStr);
                    }
                    fullLines = fullLines.replace("__ApplicationCode__", theApplicationCode_);
                    fullLines = fullLines.replace("model = new ParentModel();", "model = new "+ mainModel +"();");
                    fullLines = fullLines.replace("<ParentModel>", "<"+mainModel+">");
                    fullLines = fullLines.replace("//OverrideAddItem", (javaAddItem==null?"":javaAddItem));
                    fullLines = fullLines.replace("//OverrideGetList", (getListStr==null?"":getListStr));
                    fullLines = fullLines.replace("//ImportModelsHere", javaImportModels);
                    fullLines = fullLines.replace("public ParentModel getModel()", "public "+ mainModel +" getModel()");
                    File fileDir;
                    if (createActionClass.contains("\\")) {
                        fullLines = fullLines.replaceAll("NewAction", createActionClass.substring(createActionClass.lastIndexOf("\\")+1));
                        fileDir = new File(filePath+"src\\main\\java\\com\\"+createActionClass.substring(0, createActionClass.lastIndexOf("\\")));
                    } else {
                        fullLines = fullLines.replaceAll("NewAction", createActionClass);
                        fileDir = new File(filePath+"src\\main\\java\\com");
                    }
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    output = new BufferedWriter(new FileWriter(new File(filePath+"src\\main\\java\\com\\"+createActionClass+".java")));
                    output.write(fullLines);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (output != null) {
                        output.close();
                    }
                }
    //            Debug.printFrameworkDebug("config = " + config);
    //            writeFile(filePath+packagePath, "dynamic-config.xml", config);
    //            writeFile(filePath+packagePath, "dynamic-config.xml", addedStartEnd+"END");
            }
        } catch (BaseException be) {
            r = "rmsg;"+be.getMessage();
            return "divSubmitForm";
        } catch (Exception e) {
            e.printStackTrace();
            r = "rmsg;"+getText("generateEntry.errorGenerate");
            return "divSubmitForm";
        }
        r = "rmsg;"+getText("generateEntry.generated");
        return "divSubmitForm";
    }
    
    private StringBuilder htmlBuilder_th = null;
    private StringBuilder htmlBuilder_mi = null;
    private StringBuilder htmlBuilder = null;
    private StringBuilder finalHtmlBuilder = null;
    private StringBuilder tabBuilder = null;
    private Map<String, Integer> tabName_index = new HashMap();
    private List<String> moreHidden = new ArrayList();
    public StringBuilder getHtmlBuilder() {
        return htmlBuilder;
    }
    public void setHtmlBuilder(StringBuilder htmlBuilder) {
        this.htmlBuilder = htmlBuilder;
    }
    
    private String indentSpace(Integer howMany) {
        String temp = "";
        for (int i = 0; i < howMany; i++) {
            temp += indent;
        }
        return "            " + temp;
    }
    private void buildHtml(Map<String, Map> loopingMap, Integer level) {
        for (String key : loopingMap.keySet()) {
            Map map = htmlMap.get(key);
            Debug.printFrameworkDebug("buildHtml key = " + key);
            Debug.printFrameworkDebug("currentLevel = " + map.get("currentLevel"));
            
            if (map.containsKey("td_count")) { //table;not main
                finalHtmlBuilder.append(map.get("content"));
                if ( !((Map)loopingMap.get(key)).isEmpty()) {
                    Map<String, Object> nextMap = (Map)loopingMap.get(key);
                        String nextLevel = null;
                        String nextKey = null;
                        try {
                            for (String next : nextMap.keySet()) {
                                nextKey = next;
                                nextMap = (Map)htmlMap.get(next);
                                nextLevel = (String)nextMap.get("currentLevel");
                            }
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    if (map.containsKey("htmlBuilder_mi")) {
                        map.put("added_ifSize>0", "added");
                        finalHtmlBuilder.append("</tr>\r\n<tr class=\"<s:if test='#var_"+map.get("currentLevel")+"._hideShowMore.equals(\"H\")'>hidden</s:if> main_"+recUniqueIdx((String)map.get("currentLevel"))+" main_"+recUniqueIdx((String)map.get("currentLevel"))+"-other\" id=\"main_"+recUniqueIdx((String)map.get("currentLevel"))+"-more\">\n" +
"                        <td colspan=\""+((Integer)map.get("td_count")+1)+"\">")
                                .append(map.get("htmlBuilder_mi")).append("</td>\r\n");
//                        finalHtmlBuilder.append("</tr>\r\n<s:if test=\""+formatIteratorValue(key, (String)map.get("currentLevel"))+".size() > 0\"><tr class=\"main_"+recUniqueIdx((String)map.get("currentLevel"))+"-other\" id=\"detailItem_"+recUniqueIdx((String)map.get("currentLevel"))+"\"><td></td><td colspan=\""+map.get("td_count")+"\">");
                        finalHtmlBuilder.append("</tr>\r\n<s:if test=\""+formatIteratorValue(nextKey, nextLevel )+".size() > 0\">\n<tr class=\"main_"+recUniqueIdx((String)map.get("currentLevel"))+"-other\" id=\"detailItem_"+recUniqueIdx((String)map.get("currentLevel"))+"\"><td></td>\n<td colspan=\""+map.get("td_count")+"\">");
                        buildHtml(loopingMap.get(key), level+1);
                        finalHtmlBuilder.append("\r\n</td>");
                    } else {
//                        Map<String, Object> nextMap = (Map)loopingMap.get(key);
//                        String nextLevel = null;
//                        String nextKey = null;
//                        try {
//                            for (String next : nextMap.keySet()) {
//                                nextKey = next;
//                                nextMap = (Map)htmlMap.get(next);
//                                nextLevel = (String)nextMap.get("currentLevel");
//                            }
//                            
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        
                        map.put("added_ifSize>0", "added");
                        finalHtmlBuilder.append(indentSpace(level)).append("</tr>\r\n<s:if test=\""+formatIteratorValue(nextKey, nextLevel )+".size() > 0\">\n")
                                .append(indentSpace(level)).append("<tr id=\"detailItem_"+recUniqueIdx((String)map.get("currentLevel"))+"\">\n")
                                .append(indentSpace(level+1)).append("<td></td>\n")
                                .append(indentSpace(level+1)).append("<td colspan=\""+map.get("td_count")+"\">\n");
                        buildHtml(loopingMap.get(key), level+1);
                        finalHtmlBuilder.append("\r\n</td>\n");
                    }
                } else {
                    if (map.containsKey("htmlBuilder_mi")) {
                        finalHtmlBuilder.append(indentSpace(level)).append("</tr>\r\n")
                        .append(indentSpace(level)).append("<tr class=\"<s:if test='#var_"+map.get("currentLevel")+"._hideShowMore.equals(\"H\")'>hidden</s:if> main_"+recUniqueIdx((String)map.get("currentLevel"))+"-other\" id=\"main_"+recUniqueIdx((String)map.get("currentLevel"))+"-more\" >\n")
                        .append("<td colspan=\""+((Integer)map.get("td_count")+1)+"\">")
                                .append(map.get("htmlBuilder_mi")).append("</td>\r\n");
                        finalHtmlBuilder.append("</tr>\n</td>");
                    }
                }
                Debug.printFrameworkDebug("(String)map.get(\"currentLevel\") = " + map.get("currentLevel"));
//                if (((String)map.get("currentLevel")).contains("_") || !((String)map.get("currentLevel")).equals("0")) {
//                    finalHtmlBuilder.append("</tr></s:iterator>\r\n</table>\r\n");
//                } else {
//                    finalHtmlBuilder.append("</tr></s:if></s:iterator>\r\n</table>\r\n");
//                }
                if (map.containsKey("added_ifSize>0")) {
                    finalHtmlBuilder.append("</tr>\r\n</s:if>\r\n</s:iterator>\r\n</table>\r\n");
                } else {
                    finalHtmlBuilder.append("</tr>\r\n</s:iterator>\r\n</table>\r\n");
                }
                if (useTab.equalsIgnoreCase("Y") && level==1) {
                    finalHtmlBuilder.append(indent).append("</div>").append("\r\n").append("</div>\r\n");
                }
            } else {
                finalHtmlBuilder.append(map.get("content"));
                if (useTab.equalsIgnoreCase("Y")) {
                    finalHtmlBuilder.append("<br>\r\n<div class=\"row col-md-12\">\r\n").append(tabBuilder).append("</ul>\r\n<div class=\"tab-content\">\r\n");
                }
                buildHtml(loopingMap.get(key), level+1);
            }
        }
    }
    
    private String recUniqueIdx_sTag(String currentLevel) {
        try {
            if (Validator.isEmpty(currentLevel)) {
                return "";
            }
            String temp = recUniqueIdx(currentLevel);
            return temp.substring(currentLevel.length()+1).replaceAll("\\$\\{", "\\%\\{\\#");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    private String recUniqueIdx(String currentLevel) {
        if (currentLevel.contains("_")) {
            Debug.printFrameworkDebug("currentLevel.contains _ " + currentLevel);
            String temp = null;
            String tempLevel = null;
            for (String level : currentLevel.split("_")) {
                if (tempLevel == null) {
                    tempLevel = level;
                } else {
                    tempLevel += "_"+level;
                }
                if (temp == null) {
                    temp = "${status_"+tempLevel+".index}";
                } else {
                    temp += "_${status_"+tempLevel+".index}";
                }
            }
            return currentLevel+"-"+temp;
        } else {
            return currentLevel+"-${status_"+currentLevel+".index}";
        }
    }
    private String currentIdx(String currentLevel, Boolean previousLevel) {
        if (currentLevel.contains("_")) {
            if (previousLevel) {
                currentLevel = currentLevel.substring(0, currentLevel.lastIndexOf("_") );
            }
            String temp = null;
            String tempLevel = null;
            for (String level : currentLevel.split("_")) {
                if (tempLevel == null) {
                    tempLevel = level;
                } else {
                    tempLevel += "_"+level;
                }
                if (temp == null) {
                    temp = "${status_"+tempLevel+".index}";
                } else {
                    temp += "_${status_"+tempLevel+".index}";
                }
            }
            return temp;
        } else {
            return "${status_"+currentLevel+".index}";
        }
    }
    private String getAllClass() {
        StringBuilder temp = new StringBuilder();
        for (String level : levelStack) {
            temp.append(level).append(" ");
        }
        return temp.toString();
    }
    private Stack<String> levelStack = new Stack<String>();
    private void loopSelectedClassMap_package(Map<String, Map> loopingMap) throws Exception {
        for (String key : loopingMap.keySet()) {
            selectedClass = selectClassModelMap.get(key);
            Class modelClass = getModelClass();
            for(Method method : modelClass.getDeclaredMethods()){
                if (!method.getName().startsWith("get")) continue;
                Annotation[] annotations = method.getAnnotations();
                Boolean isPK = Boolean.FALSE;
                String name = null;
                for (int i = 0; i < annotations.length; i++) {
                    if (annotations[i].annotationType().equals(Id.class)) {
                        isPK = Boolean.TRUE;
                    }
                    if (annotations[i].annotationType().equals(Column.class)) {
                        name = ((Column)annotations[i]).name();
                    }
                }
                if (name != null) {
                    if (name.equalsIgnoreCase("created_date") || name.equalsIgnoreCase("created_by") ||
                        name.equalsIgnoreCase("updated_date") || name.equalsIgnoreCase("updated_by")) {
                        continue; //do not generate the input fields for these columns
                    }
                    if (method.getGenericReturnType().toString().equals("class java.lang.String")) {
                    } else if (method.getGenericReturnType().toString().equals("class java.lang.Double")) {
                        name += "_str";
                    } else if (method.getGenericReturnType().toString().equals("class java.lang.Long")) {
                        name += "_str";
                    } else if (method.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                        name += "_str";
                    } else if (method.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
                        name += "_str";
                    }
                    if (!isPK) {
                        if (!globalRequest.getParameter(key+"."+name+"_type").equals("ig")) {
                            if (updateToProject != null && updateToProject.equalsIgnoreCase("Y")) {
                                writeFile(filePath+packagePath, "package.properties", globalRequest.getParameter(key+"."+name+"_packageName")+"="+globalRequest.getParameter(key+"."+name+"_label"));
                            }
                        }
                    }
                }
            }
            loopSelectedClassMap_package(loopingMap.get(key));
        }
    }
    
    private String checkDisableButton_parent(String nextLevel) {
        if (nextLevel.contains("_")) {
            return "<s:if test='#var_"+nextLevel.substring(0, nextLevel.lastIndexOf("_"))+"._markedAsDel.equals(\"Y\")'>disabled </s:if>";
        }
        return "";
    }
    private String checkDeleted_current(String nextLevel) {
        return "<s:if test='#var_"+nextLevel+"._markedAsDel.equals(\"Y\")'>deleted </s:if>";
    }
    
    private String addItemTitle(String modelName) {
        return initCap(modelName.replace("Model", ""));
    }
    
    private Boolean getIsRequired(String name) {
        if (globalRequest.getParameter(name+"_required")!=null && 
                globalRequest.getParameter(name+"_required").equals("Y")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    private Set<String> getListSet = null;
    private void populateGetList(String value, String appendWhat) throws Exception {
        if (value.startsWith("get")) {
            value = value.substring(3);
            String firstChar = value.substring(0, 1);
            value = firstChar.toLowerCase() + value.substring(1);
        }
        System.out.println("value = " + value +  ", appendWhat = " + appendWhat);
        if (getListSet == null) {
            getListSet = new HashSet();
        }
        
        if (!getListSet.add(value+(Validator.isEmpty(appendWhat)?"NE":""))) {
            return;
        }
String temp = 
"    public List get"+initCap(value)+(Validator.isEmpty(appendWhat)?"NE":"")+"() {\n" + 
"        return getListFromCommonList(\"get"+ initCap(value) +"\", \""+appendWhat+"\");\n" + 
"    }\n\n";
        if (getListStr == null) {
            getListStr = temp;
        } else {
            getListStr += temp;
        }
        Debug.printFrameworkDebug("getListStr = " + getListStr);
    }
    
    String javaImportModels = null;
    private Integer tabCounter = 0;
    private void loopSelectedClassMap(Map<String, Map> loopingMap, Integer level, String levelSetup, String space) throws Exception {
        Integer subCount = -1;
        Debug.printFrameworkDebug("starting.levelSetup = " + levelSetup);
        Debug.printFrameworkDebug("loopingMap.size() = " + loopingMap.size());
        Debug.printFrameworkDebug("loopingMap = " + loopingMap);
        String nextLevel = null;
        Integer td_count = 0;
        Map currentHtmlMap = null;
        String currentStackLevel = "";
        System.out.println("level = " + level);
        System.out.println("loopingMap.size = " + loopingMap.size());
        for (String key : loopingMap.keySet()) {
            System.out.println("loopingMap.value = " + loopingMap.get(key));
            System.out.println("level = "+ level + ", key= " + key);
            Debug.printFrameworkDebug("level = "+ level + ", key= " + key);
            currentHtmlMap = new TreeMap();
            Boolean startBuild = Boolean.FALSE;
            subCount++;
            if (levelSetup ==null) {
                nextLevel = "";
            } else {
                if (Validator.isEmpty(levelSetup)) {
                    nextLevel = ""+subCount;
                } else {
                    nextLevel = levelSetup+"_"+subCount;
                }
            }
            System.out.println("------------------ nextLevel == " + nextLevel);
            Debug.printFrameworkDebug("");
            Debug.printFrameworkDebug("nextLevel = " + nextLevel);
            Debug.printFrameworkDebug("key = " + key);
            Debug.printFrameworkDebug("formatListName(key+\".ID\", nextLevel) = " + formatListName(key+".ID", nextLevel));
            Debug.printFrameworkDebug("");
            Debug.printFrameworkDebug("");
            String hiddenPK = "<s:hidden id=\"field_"+recUniqueIdx_sTag(nextLevel)+"_ID\" name=\""+formatListName(key+".ID", nextLevel)+"\" /><s:hidden id=\"field_"+recUniqueIdx_sTag(nextLevel)+"_markedAsDel\" name=\""+formatListName(key+"._markedAsDel", nextLevel)+"\" /><s:hidden id=\"field_"+recUniqueIdx_sTag(nextLevel)+"_hideShowMore\" name=\""+formatListName(key+"._hideShowMore", nextLevel)+"\" />";
            Debug.printFrameworkDebug("nextLevel = " + nextLevel);
            selectedClass = selectClassModelMap.get(key);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ selectedClass = " + selectedClass);
            if (mainModel == null) {
                mainModel = selectedClass;
            }
            Class modelClass = getModelClass();
            if (javaImportModels == null) {
                javaImportModels = "import " + modelClass.getPackage().getName() + "." + modelClass.getSimpleName()+";\n";
            } else {
                javaImportModels += "import " + modelClass.getPackage().getName() + "." + modelClass.getSimpleName()+";\n";
            }
            Map<String, Map> columnMap = new HashMap();
            List<Options> list = new ArrayList();
            for(Method method : modelClass.getDeclaredMethods()){
                if (!method.getName().startsWith("get")) continue;
                Annotation[] annotations = method.getAnnotations();
                Boolean isPK = Boolean.FALSE;
                String name = null;
                for (int i = 0; i < annotations.length; i++) {
                    if (annotations[i].annotationType().equals(Id.class)) {
                        isPK = Boolean.TRUE;
                    }
                    if (annotations[i].annotationType().equals(Column.class)) {
                        name = ((Column)annotations[i]).name();
                    }
                }
                if (name != null) {
                    String oldName = name;
                    if (name.equalsIgnoreCase("created_date") || name.equalsIgnoreCase("created_by") ||
                        name.equalsIgnoreCase("updated_date") || name.equalsIgnoreCase("updated_by")) {
                        continue; //do not generate the input fields for these columns
                    }
                    Map mapContent = new HashMap();
                    String date_numeric_float_class = "";
                    if (method.getGenericReturnType().toString().equals("class java.lang.String")) {
                    } else if (method.getGenericReturnType().toString().equals("class java.lang.Double")) {
                        date_numeric_float_class = "Float";
                        name += "_str";
                    } else if (method.getGenericReturnType().toString().equals("class java.lang.Long")) {
                        date_numeric_float_class = "Numeric";
                        name += "_str";
                    } else if (method.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                        date_numeric_float_class = "Numeric";
                        name += "_str";
                    } else if (method.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
                        date_numeric_float_class = "Date_Picker";
                        name += "_str";
                    }
                    mapContent.put("isPK", isPK);
                    mapContent.put("date_numeric_float_class", date_numeric_float_class);
                    columnMap.put(name, mapContent);
                    list.add(new Options(globalRequest.getParameter(key+"."+name+"_sorting"), name));
                    if (level == 0) {
                        mapContent.put("oldName", oldName);
                    }
                }
            }
            
            System.out.println("");
//            System.out.println("before sorting");
//            for (Options ops : list) {
//                System.out.println("ops.getValue = " + ops.getValueData());
//            }
            Collections.sort(list, new CommonComparator(new String[]{"getKeyData_int"}));
            if (level == 0) {
                for (Options ops : list) {
                    if (!(Boolean)columnMap.get(ops.getValueData()).get("isPK")) {
                        String oldName = (String) columnMap.get(ops.getValueData()).get("oldName");
                        if (configRetrievingCol == null) {
                            configRetrievingCol = oldName;
                        } else {
                            configRetrievingCol += ","+oldName;
                        }
                    }
                }
            }
//            System.out.println("");
//            System.out.println("after sorting");
//            for (Options ops : list) {
//                System.out.println("ops.getValue = " + ops.getValueData());
//            }
            
            
            for(Options nameOption : list){
                String name = nameOption.getValueData();
                System.out.println("generate entry for " + nameOption.getValueData());
//                if (!method.getName().startsWith("get")) continue;
//                Annotation[] annotations = method.getAnnotations();
System.out.println("columnMap.get(name) = " + columnMap.get(name));
                Boolean isPK = (Boolean) columnMap.get(name).get("isPK");
//                String name = null;
//                for (int i = 0; i < annotations.length; i++) {
//                    if (annotations[i].annotationType().equals(Id.class)) {
//                        isPK = Boolean.TRUE;
//                    }
//                    if (annotations[i].annotationType().equals(Column.class)) {
//                        name = ((Column)annotations[i]).name();
//                    }
//                }
                if (name != null) {
                    String labelName = name;

                    if (name.equalsIgnoreCase("created_date") || name.equalsIgnoreCase("created_by") ||
                        name.equalsIgnoreCase("updated_date") || name.equalsIgnoreCase("updated_by")) {
                        continue; //do not generate the input fields for these columns
                    }
                    String oldName = name;
                    String date_numeric_float_class = (String) columnMap.get(name).get("date_numeric_float_class");
//                    String date_numeric_float_class = "";
//                    if (method.getGenericReturnType().toString().equals("class java.lang.String")) {
//                    } else if (method.getGenericReturnType().toString().equals("class java.lang.Double")) {
//                        date_numeric_float_class = "Float";
//                        name += "_str";
//                    } else if (method.getGenericReturnType().toString().equals("class java.lang.Long")) {
//                        date_numeric_float_class = "Numeric";
//                        name += "_str";
//                    } else if (method.getGenericReturnType().toString().equals("class java.lang.Integer")) {
//                        date_numeric_float_class = "Numeric";
//                        name += "_str";
//                    } else if (method.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
//                        date_numeric_float_class = "Date_Picker";
//                        name += "_str";
//                    }
                    if (!isPK) {
                        if (level == 0) {
                            if (editLink == null) {
                                editLink = oldName;
                            }
                            if (configSearchField == null) {
                                configSearchField = oldName+";"+globalRequest.getParameter(key+"."+name+"_packageName")+";"+oldName;
                            } else {
                                configSearchField += ","+oldName+";"+globalRequest.getParameter(key+"."+name+"_packageName")+";"+oldName;
                            }
//                            if (configRetrievingCol == null) {
//                                configRetrievingCol = oldName;
//                            } else {
//                                configRetrievingCol += ","+oldName;
//                            }
                            if (configDisplayHeader == null) {
                                configDisplayHeader = globalRequest.getParameter(key+"."+name+"_packageName");
                            } else {
                                configDisplayHeader += ","+globalRequest.getParameter(key+"."+name+"_packageName");
                            }
                        }
                        Debug.printFrameworkDebug("levelSetup =  "+nextLevel+ ", " +key+"."+name+"_packageName"+ " = " + globalRequest.getParameter(key+"."+name+"_packageName"));
                        if (Validator.isEmpty(nextLevel)) { //first level (model/theModel)
                            if (!startBuild) {
                                startBuild = Boolean.TRUE;
                            }
                            if (hiddenPK != null) {
                                htmlBuilder.append("\r\n").append(hiddenPK).append("\r\n");
                                hiddenPK = null;
                            }
                            if (globalRequest.getParameter(key+"."+name+"_type").equals("ta")) {
                                String taSetup = "maxlength='%{"+key+".columnLengthMap[\""+lastFieldName(name)+"\"]}' ";
                                if (Validator.isEmpty(globalRequest.getParameter(key+"."+name+"_setup"))) {
                                    taSetup+= " cols='5' rows='3' "; //if no setup then default to this.
                                } else {
                                    String[] colRow = globalRequest.getParameter(key+"."+name+"_setup").split(";");
                                    taSetup+= " cols='"+colRow[0]+"' rows='"+colRow[1]+"' ";
                                }
                                appendTextArea(htmlBuilder, key+"."+name, key+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name), "taCount", taSetup);
                            } else if (globalRequest.getParameter(key+"."+name+"_type").equals("tf")) {
                                String maxLength = "maxlength='%{"+key+".columnLengthMap[\""+lastFieldName(name)+"\"]}' ";
                                appendTextField(htmlBuilder, key+"."+name, key+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name), date_numeric_float_class, maxLength);
                            } else if (globalRequest.getParameter(key+"."+name+"_type").equals("dd")) {
                                appendDropdown(htmlBuilder, key+"."+name, key+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name) );
                            } else if (globalRequest.getParameter(key+"."+name+"_type").equals("rd")) {
                                appendRadio(htmlBuilder, key+"."+name, key+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name) );
                            } else if (globalRequest.getParameter(key+"."+name+"_type").equals("cb")) {
                                System.out.println("checkbox...............................................................");
                                appendCheckbox(htmlBuilder, key+"."+name, key+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name) );
                            } else if (globalRequest.getParameter(key+"."+name+"_type").equals("uu")) {//uppyUpload
                                System.out.println("UppyUpload...............................................................");
                                appendUppyUpload(htmlBuilder, key+"."+name, key+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name) );
                            }
                        } else { //not main level
                            checkClosingForMain(htmlBuilder);
                            if (!startBuild) {
                                Debug.printFrameworkDebug("not main - not start build");
                                startBuild = Boolean.TRUE;
                                currentStackLevel = "main_"+recUniqueIdx(nextLevel);
                                htmlBuilder.append("<tr id=\"main_"+recUniqueIdx(nextLevel)+"\" class=\""+checkDeleted_current(nextLevel)+getAllClass()+"\">\n");
                                levelStack.push(currentStackLevel);
                                if (nextLevel.contains("_")) {
                                    htmlBuilder_th.append("<th width=\"20px\"></th>");
                                } else { // add addItem icon for 2nd Lvl only
                                    Debug.printFrameworkDebug("1. --------------- adding " + nextLevel+";"+(key)+";"+modelClass.getSimpleName());
                                    addItemList.add(nextLevel+";"+(key)+";"+modelClass.getSimpleName());
                                    String addWhat = initCap(key.substring(key.lastIndexOf(".")+1));
                                    htmlBuilder_th.append("<th width=\"20px\">"+
                                            "<i class=\"fa fa-plus-circle\" title=\"<s:text name='"+getFormatterApplicationCode_()+".add"+addItemTitle(modelClass.getSimpleName())+"Title'/>\" onclick=\"processAddItem('"+theApplicationCode_+"FormID', '"+nextLevel+"','"+currentIdx(nextLevel, Boolean.TRUE)+"', 'processAdd"+ addWhat+ theApplicationCode_ +"')\"></i>"+
                                            "</th>");
                                }
                                htmlBuilder.append(space).append(indent).append(indent).append("<td>\r\n").append(space).append(indent).append(indent).append(indent).append("<div class=\"btn-group\">\n" +
space+indent+indent+indent+indent+"<button type=\"button\" class=\""+checkDisableButton_parent(nextLevel)+"btn btn-default block-xs dropdown-toggle btn-dropdown-sds ge-more\" data-toggle=\"dropdown\" aria-expanded=\"false\" aria-haspopup=\"true\"><i class=\"fa fa-caret-down noMargin\"></i></button>\n" +
"                                <ul class=\"dropdown-menu sds-dropdown-menu\">\n");
//** this one is to add icon to control show/hide list : not ready : START **//
//                                if (loopingMap.get(key).size() > 0) {
//htmlBuilder.append("                                    <li onclick=\"processAddItem('"+nextLevel+"','"+currentIdx(nextLevel, Boolean.FALSE)+"')\"><a href=\"#\"><i class=\"fa fa-toggle-down\"></i><s:text name=\"Details\"/></a></li>\n");
//                                }
//** this one is to add icon to control show/hide list : not ready : END **//
//                                "field_"+nextLevel+lastFieldName(name)
//var_"+nextLevel+"."+lastFieldName(name)
htmlBuilder.append("APPEND_MI"); //replace with more info string OR replace with empty later
htmlBuilder.append(space).append(indent).append(indent).append(indent).append(indent).append("<s:if test='#var_"+nextLevel+"._markedAsDel.equals(\"Y\")'>"
        + "<li onclick=\"return deleteMe(this, '"+nextLevel+"','"+currentIdx(nextLevel, Boolean.FALSE)+"');\"><a href=\"#\"><i class=\"fa far fa-trash-alt\"></i><s:text name=\"button.undoDelete\"/></a></li>"
                + "</s:if><s:else>"
                + "<li onclick=\"return deleteMe(this, '"+nextLevel+"','"+currentIdx(nextLevel, Boolean.FALSE)+"');\"><a href=\"#\"><i class=\"fa far fa-trash-alt\"></i><s:text name=\"button.markDelete\"/></a></li>"
                + "</s:else>\n");
//htmlBuilder.append("                                    <li onclick=\"return deleteMe(this, '"+nextLevel+"','"+currentIdx(nextLevel, Boolean.FALSE)+"')\"><a href=\"#\"><i class=\"fa far fa-trash-alt\"></i><s:text name=\"button.markDelete\"/></a></li>\n");
                                if (loopingMap.get(key).size() > 0) {
                                    String tempKey = null;
                                    
                                    for (String nextKey : ((Map<String, Object>)loopingMap.get(key)).keySet()) {
                                        tempKey = nextKey;
                                    }
                                    String nextSelectedClass = selectClassModelMap.get(tempKey);
                                    Class nextClass = getModelClass(nextSelectedClass);
                                    Debug.printFrameworkDebug("2. --------------- adding " + nextLevel+";"+(tempKey)+";"+nextClass.getSimpleName());
                                    addItemList.add(nextLevel+";"+(tempKey)+";"+nextClass.getSimpleName());
                                    String addWhat = initCap(tempKey.substring(tempKey.lastIndexOf(".")+1));
htmlBuilder.append("                                    <li class=\"add"+nextLevel+"-"+currentIdx(nextLevel, Boolean.FALSE)+"\" onclick=\"processAddItem('"+theApplicationCode_+"FormID', '"+nextLevel+"','"+currentIdx(nextLevel, Boolean.FALSE)+"', 'processAdd"+ addWhat +theApplicationCode_+"')\"><a href=\"#\"><i class=\"fa fa-plus-square-o\"></i><s:text name=\"button.add\"/></a></li>\n");
                                }
htmlBuilder.append("                                </ul>\n" +
space+indent+indent+indent+"</div>\n"+space+indent+indent+"</td>\n");
                            } else {
                                Debug.printFrameworkDebug("not main - start build");
//                                hiddenPK = null;
                            }
                            Debug.printFrameworkDebug("globalRequest.getParameter(key+\".\"+name+\"_type\") = " + globalRequest.getParameter(key+"."+name+"_type"));
                            System.out.println("before more infor ====================================- key="+key + ", name="+name);
                            System.out.println("globalRequest.getParameter(key+\".\"+name+\"_mi\") = " + globalRequest.getParameter(key+"."+name+"_mi"));
                            if (globalRequest.getParameter(key+"."+name+"_mi")!=null && globalRequest.getParameter(key+"."+name+"_mi").equals("Y")) { //More Info
                                System.out.println("in more infor ====================================-");
                                if (globalRequest.getParameter(key+"."+name+"_type").equals("tf")) {
                                    String maxLength = "maxlength='%{"+key+".columnLengthMap[\""+lastFieldName(name)+"\"]}' ";
                                    appendTextField_mi(htmlBuilder_mi, key+"."+name, "#var_"+nextLevel+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name), date_numeric_float_class, maxLength);
                                } else if (globalRequest.getParameter(key+"."+name+"_type").equals("ta")) {
                                    String taSetup = "maxlength='%{"+"#var_"+nextLevel+".columnLengthMap[\""+lastFieldName(name)+"\"]}' ";
                                    if (Validator.isEmpty(globalRequest.getParameter(key+"."+name+"_setup"))) {
                                        taSetup+= " cols='5' rows='3' "; //if no setup then default to this.
                                    } else {
                                        String[] colRow = globalRequest.getParameter(key+"."+name+"_setup").split(";");
                                        taSetup+= " cols='"+colRow[0]+"' rows='"+colRow[1]+"' ";
                                    }
                                    appendTextArea_mi(htmlBuilder_mi, key+"."+name, "#var_"+nextLevel+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name), "taCount", taSetup);
                                } else if (globalRequest.getParameter(key+"."+name+"_type").equals("cb")) {
                                    appendCheckbox_mi(htmlBuilder_mi, key+"."+name, "#var_"+nextLevel+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name) );
                                } else if (globalRequest.getParameter(key+"."+name+"_type").equals("rd")) {
                                    appendRadio_mi(htmlBuilder_mi, key+"."+name, "#var_"+nextLevel+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name) );
                                } else if (globalRequest.getParameter(key+"."+name+"_type").equals("dd")) {
                                    appendDropdown_mi(htmlBuilder_mi, key+"."+name, "#var_"+nextLevel+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name) );
                                }
                            } else if (globalRequest.getParameter(key+"."+name+"_type").equals("ta")) {
                                String taSetup = "maxlength='%{#var_"+nextLevel+".columnLengthMap[\""+lastFieldName(name)+"\"]}' ";
                                if (Validator.isEmpty(globalRequest.getParameter(key+"."+name+"_setup"))) {
                                    taSetup+= " cols='5' rows='3' "; //if no setup then default to this.
                                } else {
                                    String[] colRow = globalRequest.getParameter(key+"."+name+"_setup").split(";");
                                    taSetup+= " cols='"+colRow[0]+"' rows='"+colRow[1]+"' ";
                                }
                                htmlBuilder_th.append("<th><s:text name=\"").append(globalRequest.getParameter(key+"."+name+ (isView?"_label":"_packageName") )).append("\"/>"+(getIsRequired(key+"."+name)?"&nbsp;<font class=\"asterisk\">*</font></label>":"")+"</th>");
                                td_count++;
                                appendTextField_ta(key+"."+name, "#var_"+nextLevel+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name), hiddenPK, "taCount", taSetup);
                                if (hiddenPK != null) {
                                    hiddenPK = null;
                                }
                            } else if (globalRequest.getParameter(key+"."+name+"_type").equals("tf")) {
                                String maxLength = "maxlength='%{"+key+".columnLengthMap[\""+lastFieldName(name)+"\"]}' ";
                                htmlBuilder_th.append("<th><s:text name=\"").append(globalRequest.getParameter(key+"."+name+ (isView?"_label":"_packageName") )).append("\"/>"+(getIsRequired(key+"."+name)?"&nbsp;<font class=\"asterisk\">*</font></label>":"")+"</th>");
                                td_count++;
                                appendTextField_td(key+"."+name, "#var_"+nextLevel+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name), hiddenPK, date_numeric_float_class, maxLength);
                                if (hiddenPK != null) {
                                    hiddenPK = null;
                                }
                            } else if (globalRequest.getParameter(key+"."+name+"_type").equals("dd")) {
                                htmlBuilder_th.append("<th><s:text name=\"").append(globalRequest.getParameter(key+"."+name+ (isView?"_label":"_packageName") )).append("\"/>"+(getIsRequired(key+"."+name)?"&nbsp;<font class=\"asterisk\">*</font></label>":"")+"</th>");
                                td_count++;
                                appendDropdown_td(key+"."+name, "#var_"+nextLevel+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name), hiddenPK);
                                if (hiddenPK != null) {
                                    hiddenPK = null;
                                }
                            } else if (globalRequest.getParameter(key+"."+name+"_type").equals("rd")) {
                                htmlBuilder_th.append("<th><s:text name=\"").append(globalRequest.getParameter(key+"."+name+ (isView?"_label":"_packageName") )).append("\"/>"+(getIsRequired(key+"."+name)?"&nbsp;<font class=\"asterisk\">*</font></label>":"")+"</th>");
                                td_count++;
                                appendRadio_td(key+"."+name, "#var_"+nextLevel+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name), hiddenPK);
                                if (hiddenPK != null) {
                                    hiddenPK = null;
                                }
                            } else if (globalRequest.getParameter(key+"."+name+"_type").equals("cb")) {
                                htmlBuilder_th.append("<th><s:text name=\"").append(globalRequest.getParameter(key+"."+name+ (isView?"_label":"_packageName") )).append("\"/>"+(getIsRequired(key+"."+name)?"&nbsp;<font class=\"asterisk\">*</font></label>":"")+"</th>");
                                td_count++;
                                appendCheckbox_td(key+"."+name, "#var_"+nextLevel+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name), hiddenPK);
                                if (hiddenPK != null) {
                                    hiddenPK = null;
                                }
//                            } else if (globalRequest.getParameter(key+"."+name+"_type").equals("mi")) { //More Info
//                                appendTextField_mi(htmlBuilder_mi, key+"."+name, "#var_"+nextLevel+"."+lastFieldName(name), nextLevel, getIsRequired(key+"."+name));
                            }
                        }
                    }
                }
            }
            checkClosingForMain(htmlBuilder);
                finalHtmlBuilder = new StringBuilder();
//            if (finalHtmlBuilder == null) {
//                finalHtmlBuilder = new StringBuilder();
//            }
            if (!Validator.isEmpty(nextLevel)) {
                //** table structure inserted **//
//                finalHtmlBuilder.append("<table><tr>\r\n").append(htmlBuilder_th).append("</tr>\r\n")
//                    .append("<s:iterator value=\""+formatIteratorValue(key, nextLevel)+"\" status=\"status_"+nextLevel+"\" var=\"var_"+nextLevel+"\">")
//                    .append(htmlBuilder).append("\r\n").append("</tr></s:iterator>\r\n</table>\r\n");
                String htmlBuilder_str = null;
                    htmlBuilder_str = htmlBuilder.toString();
                Debug.printFrameworkDebug("htmlBuilder_mi.length() = " + htmlBuilder_mi.length());
                if (htmlBuilder_mi.length() > 0) {
                    try {
                        checkClosingForMi(htmlBuilder_mi, key+".");
                        String temp1 = htmlBuilder_str.substring(0, htmlBuilder_str.indexOf("APPEND_MI"));
                        String temp2 = htmlBuilder_str.substring(htmlBuilder_str.indexOf("APPEND_MI")+9);
                        htmlBuilder_str = temp1 + space + indent + indent + indent+ indent + indent + indent+"<s:if test='#var_"+nextLevel+"._hideShowMore.equals(\"S\")'>\n" +
    "                                        <li onclick=\"return moreInfo('"+nextLevel+"','"+currentIdx(nextLevel, Boolean.FALSE)+"');\"><a href=\"#\"><i class=\"fa fa-toggle-down\"></i><span id=\"main_"+recUniqueIdx(nextLevel)+"-ico\"><s:text name=\"button.lessInfo\"/></span></a></li>\n" +
    "                                    </s:if><s:else>\n" +
    "                                        <li onclick=\"return moreInfo('"+nextLevel+"','"+currentIdx(nextLevel, Boolean.FALSE)+"');\"><a href=\"#\"><i class=\"fa fa-toggle-down\"></i><span id=\"main_"+recUniqueIdx(nextLevel)+"-ico\"><s:text name=\"button.moreInfo\"/></span></a></li>\n" +
    "                                    </s:else>" + temp2;
                        currentHtmlMap.put("htmlBuilder_mi", htmlBuilder_mi);
                        Debug.printFrameworkDebug("htmlBuilder_str = " + htmlBuilder_str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                    htmlBuilder_str = htmlBuilder.toString();
                    htmlBuilder_str = htmlBuilder_str.replaceAll("APPEND_MI", "");
                }
                if (useTab.equalsIgnoreCase("Y") && level==1) {
                    tabCounter++;
//                    finalHtmlBuilder.append("<div id=\"menu").append(tabCounter).append("\" class=\"tab-pane fade"+(tabCounter==1?" in active":"")+" \">").append("\r\n")
                    finalHtmlBuilder.append("<div id=\"menu").append(tabCounter).append("\" class=\"tab-pane fade <s:property value='%{getActiveTabDiv("+tabCounter+")}'/>" +" \">").append("\r\n")
                            .append(indent).append("<div class=\"table-responsive\">");
                    if (tabBuilder.length() == 0) {
                        tabBuilder.append("<ul class=\"nav nav-tabs\" id=\"myTab\">\r\n")
                                .append(indent).append("<li class=\"<s:property value='%{getActiveTabLi("+tabCounter+")}'/>\"><a class=\"tabToggle\" data-toggle=\"tab\" id=\"tab_menu"+tabCounter+"\" href=\"#menu"+tabCounter+"\">"+ formatIteratorValue_forTabName(key, nextLevel) +"</a></li>\r\n");
                    } else {
                        tabBuilder.append(indent).append("<li class=\"<s:property value='%{getActiveTabLi("+tabCounter+")}'/>\"><a class=\"tabToggle\" data-toggle=\"tab\" id=\"tab_menu"+tabCounter+"\" href=\"#menu"+tabCounter+"\">"+ formatIteratorValue_forTabName(key, nextLevel) +"</a></li>\r\n");
                    }
                    String getListName = formatIteratorValue(key, nextLevel);
                    String temp = getListName.substring(getListName.indexOf(".")+1);
                    getListName = getListName.substring(0, getListName.indexOf(".")+1) + "get" + temp.substring(0, 1).toUpperCase() + temp.substring(1) + "()";
                    System.out.println("getListName = " + getListName.replaceAll("theModel\\.", mainModelVarName+"\\."));
                    tabName_index.put(getListName.replaceAll("theModel\\.", mainModelVarName+"\\."), tabCounter);
                }
                System.out.println("s:iterator here.... value = " + formatIteratorValue(key, nextLevel));
                finalHtmlBuilder.append(indentSpace(level>0?level+1:0)).append("\r\n<table class=\"table-condensed tb"+nextLevel+"_"+currentIdx(nextLevel, Boolean.FALSE)+"\" width=\"100%\">\r\n")
                    .append(space).append(indent).append("<tr>\r\n")
                    .append(space).append(indent).append(indent).append(htmlBuilder_th).append("\r\n")
                    .append(space).append(indent).append("</tr>\r\n")
                        .append(space).append("<s:iterator value=\""+formatIteratorValue(key, nextLevel)+"\" status=\"status_"+nextLevel+"\" var=\"var_"+nextLevel+"\">").append("\r\n")
                    .append(space).append(indent).append(htmlBuilder_str);
                currentHtmlMap.put("content", finalHtmlBuilder.toString());
                currentHtmlMap.put("currentLevel", nextLevel);
                currentHtmlMap.put("td_count", td_count);td_count = 0;
            } else {
                finalHtmlBuilder.append(htmlBuilder);
                currentHtmlMap.put("currentLevel", nextLevel);
                currentHtmlMap.put("content", finalHtmlBuilder.toString());
            }
//            finalHtmlBuilder.append("\r\n");
            htmlMap.put(key, currentHtmlMap);
            htmlBuilder.setLength(0);
            htmlBuilder_th.setLength(0);
            htmlBuilder_mi = new StringBuilder();
            loopSelectedClassMap(loopingMap.get(key), level+1, nextLevel, (space==null?"            ":space+indent));
        }
    }
    
    private String removeGet(String methodName) {
        String firstChar = methodName.substring(3, 4);
        methodName = methodName.substring(4);
        return firstChar.toLowerCase()+methodName;
    }
    private String lastFieldName(String fullName) {
        return fullName.substring(fullName.lastIndexOf(".")+1);
    }
    
    private String formatIteratorValue_build(String fieldName, String nextLevel) {
        if (nextLevel.equals("")) {
            return fieldName;
        } else {
            return "#var_"+nextLevel+"."+lastFieldName(fieldName);
        }
    }
    
    private String formatName(String inName) {
        if (inName.contains(".")) {
            inName = inName.substring(inName.lastIndexOf(".")+1);
        }
        String charsNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int count = -1;
        StringBuilder newName = new StringBuilder();
        while (true) {
            if (++count == inName.length()) {
                break;
            }
            if (count == 0) {
                newName.append( (inName.charAt(count)+"").toUpperCase() );
                continue;
            }
            if (charsNum.contains(inName.charAt(count)+"")) {
                newName.append( " " );
            }
            newName.append( inName.charAt(count) );
        }
        return newName.toString();
    }
    
    private String formatIteratorValue_forTabName(String fieldName, String nextLevel) {
        String temp = formatIteratorValue(fieldName, nextLevel);
        temp = temp.substring(temp.lastIndexOf(".")+1);
        return formatName(temp);
    }
    private String formatIteratorValue(String fieldName, String nextLevel) {
        if (nextLevel.contains("_")) {
            return "#var_"+nextLevel.substring(0, nextLevel.length()-2)+"."+lastFieldName(fieldName);
        }
        return fieldName;
    }
    private String formatListName(String fieldName, String nextLevel) {
        if (Validator.isEmpty(nextLevel)) {
            if (fieldName.equals("theModel.ID") || fieldName.equals("theModel._markedAsDel")) {
                return fieldName.replace("theModel", mainModelVarName);
            }
            return fieldName.replaceAll(".", "");
        }
        try {
            int count = 1;
            String currentLevel = null;
            for (String theLevel : nextLevel.split("_")) {
                count++;
                if (currentLevel == null) {
                    currentLevel = theLevel;
                } else {
                    currentLevel += "_"+theLevel;
                }
                int foundIdx = -1;
                for (int idx = 0; idx < count; idx++) {
                    foundIdx = fieldName.indexOf(".", foundIdx+1);
                }
                fieldName = fieldName.substring(0, foundIdx)+"[%{#status_"+currentLevel+"_DOT_index}]"+fieldName.substring(foundIdx);
            }
        } catch (Exception e) {
            Debug.printFrameworkDebug("error for field_name = " + fieldName + " , nextlevel = " + nextLevel);
//            e.printStackTrace();
        }
        return fieldName.replaceAll("_DOT_", ".");
    }
    private String formatListName_normalInput(String fieldName, String nextLevel) {
        System.out.println("fieldName = " + fieldName);
        if (Validator.isEmpty(nextLevel)) {
            if (fieldName.equals("theModel.ID") || fieldName.equals("theModel._markedAsDel")) {
                return fieldName.replace("theModel", mainModelVarName);
            }
            return fieldName.replaceAll(".", "");
        }
        try {
            int count = 1;
            String currentLevel = null;
            for (String theLevel : nextLevel.split("_")) {
                count++;
                if (currentLevel == null) {
                    currentLevel = theLevel;
                } else {
                    currentLevel += "_"+theLevel;
                }
                int foundIdx = -1;
                for (int idx = 0; idx < count; idx++) {
                    foundIdx = fieldName.indexOf(".", foundIdx+1);
                }
                fieldName = fieldName.substring(0, foundIdx)+"[\\${status_"+currentLevel+"_DOT_index}]"+fieldName.substring(foundIdx);
            }
        } catch (Exception e) {
            Debug.printFrameworkDebug("error for field_name = " + fieldName + " , nextlevel = " + nextLevel);
//            e.printStackTrace();
        }
        return fieldName.replaceAll("_DOT_", ".");
    }
    private String formatListID_nst(String fieldName, String nextLevel) {
        String temp = formatListID(fieldName, nextLevel);
        return temp.replace("%{#", "${");
    }
    private String formatListID(String fieldName, String nextLevel) {
        if (Validator.isEmpty(nextLevel)) {
            if (fieldName.equals("theModel.ID") || fieldName.equals("theModel._markedAsDel")) {
                return fieldName.replace("theModel", mainModelVarName);
            }
            return fieldName.replaceAll(".", "");
        }
        try {
            int count = 1;
            String currentLevel = null;
            for (String theLevel : nextLevel.split("_")) {
                count++;
                if (currentLevel == null) {
                    currentLevel = theLevel;
                } else {
                    currentLevel += "_"+theLevel;
                }
                int foundIdx = -1;
                for (int idx = 0; idx < count; idx++) {
                    foundIdx = fieldName.indexOf(".", foundIdx+1);
                }
                fieldName = fieldName.substring(0, foundIdx)+"%{#status_"+currentLevel+"_DOT_index}"+fieldName.substring(foundIdx);
            }
        } catch (Exception e) {
            Debug.printFrameworkDebug("error for field_name = " + fieldName + " , nextlevel = " + nextLevel);
//            e.printStackTrace();
        }
        fieldName = fieldName.replaceAll("\\.", "_");
        return fieldName.replaceAll("_DOT_", ".");
    }
    private void appendTextField_ta(String fieldName, String valueVar, String nextLevel, Boolean req, String hiddenPK, String taClass, String taSetup) {
        fieldNameSet.add(fieldName.substring(0, fieldName.lastIndexOf(".")));
//        String label = globalRequest.getParameter(fieldName+"_packageName");
        htmlBuilder.append(
"                    <td>\n"+
(hiddenPK==null?"":"                       "+hiddenPK+"\n")+
"                       <div id=\""+formatListID_nst(fieldName, nextLevel)+"_div\" >\n" +
"                          <s:textarea id='"+formatListID(fieldName, nextLevel)+"' "+ taSetup +" cssClass=\"form-control "+taClass+"\" name=\""+formatListName(fieldName, nextLevel)+"\" "+(req?"required=\"required\"":"")+"/>\n" +
"                       </div>" +
"                    </td>\r\n");
    }
    private void appendTextField_td(String fieldName, String valueVar, String nextLevel, Boolean req, String hiddenPK, String date_numeric_float_class, String maxLength) {
        fieldNameSet.add(fieldName.substring(0, fieldName.lastIndexOf(".")));
//        String label = globalRequest.getParameter(fieldName+"_packageName");
        htmlBuilder.append(
"                    <td>\n"+
(hiddenPK==null?"":"                       "+hiddenPK+"\n")+
"                       <s:textfield id='"+formatListID(fieldName, nextLevel)+"' "+ maxLength +" cssClass=\"form-control "+date_numeric_float_class+"\" name=\""+formatListName(fieldName, nextLevel)+"\" value=\"%{"+valueVar+"}\" "+(req?"required=\"required\"":"")+"/>\n" +
"                    </td>\r\n");
    }
    private void appendCheckbox_td(String fieldName, String valueVar, String nextLevel, Boolean req, String hiddenPK) throws Exception {
        fieldNameSet.add(fieldName.substring(0, fieldName.lastIndexOf(".")));
//        String label = globalRequest.getParameter(fieldName+"_packageName");
        String packageLabel = globalRequest.getParameter(fieldName+"_label");
        if (Validator.isEmpty(globalRequest.getParameter(fieldName+"_setup"))) {
            throw new CustomBaseException("Pls enter dropdown''s setup value for ["+packageLabel+"]");
        }
        htmlBuilder.append(
"                    <td>\n"+
(hiddenPK==null?"":"                       "+hiddenPK+"\n")+
"                <div class=\"checkbox right check-success\">\n" +
"                       <input onclick=\"return cbReadOnly(this);\" type=\"checkbox\" class=\"form-control\" value=\""+globalRequest.getParameter(fieldName+"_setup")+"\" id=\""+formatListID(fieldName, nextLevel)+"\" name=\""+formatListName(fieldName, nextLevel).replace("%", "$").replace("#", "") +"\" "+(req?"required=\"\"":"")+" >\n" +
"                       <label for=\"field_"+nextLevel+lastFieldName(fieldName)+"\"></label>\n" +
"                </div>\n" +
"                    </td>\r\n");
    }
    private void appendDropdown_td(String fieldName, String valueVar, String nextLevel, Boolean req, String hiddenPK) throws Exception {
        fieldNameSet.add(fieldName.substring(0, fieldName.lastIndexOf(".")));
        String ddList = null, ddKey = "keyData", ddValue = "valueData";
        String packageLabel = globalRequest.getParameter(fieldName+"_label");
        if (Validator.isEmpty(globalRequest.getParameter(fieldName+"_setup"))) {
            throw new CustomBaseException("Pls enter dropdown''s setup value for ["+packageLabel+"]");
        } else {
            String[] setupArr = globalRequest.getParameter(fieldName+"_setup").split(";");
            if (setupArr.length == 1 || setupArr.length == 3) {
                if (setupArr.length == 3) {
                    ddKey =  setupArr[1];
                    ddValue =  setupArr[2];
                }
                ddList = initLower(setupArr[0]);
                populateGetList(initCap(ddList), "");
                ddList += "NE";
            } else {
                ddList = initLower(setupArr[0]);
                ddKey =  setupArr[1];
                ddValue =  setupArr[2];
                if (setupArr.length == 4) {
                    populateGetList(initCap(ddList), setupArr[3]);
                }
            }
        }
        htmlBuilder.append(
"                    <td>\n"+
(hiddenPK==null?"":"                       "+hiddenPK+"\n")+
"                       <s:select style=\"width:100%;\" id=\""+formatListID(fieldName, nextLevel)+"\" list=\""+(isView?"sampleList":ddList)+"\" listKey=\""+(isView?"keyData":ddList)+"\" listValue=\""+(isView?"valueData":ddValue)+"\" name=\""+formatListName(fieldName, nextLevel)+"\" cssClass=\"form-control sds-dropdown\" value=\"%{"+valueVar+"}\" "+(req?"required=\"required\"":"")+"/>\n" +
"                    </td>\r\n");
    }
    private void appendRadio_td(String fieldName, String valueVar, String nextLevel, Boolean req, String hiddenPK) throws Exception {
        fieldNameSet.add(fieldName.substring(0, fieldName.lastIndexOf(".")));
        String ddList = null, ddKey = "keyData", ddValue = "valueData";
        String label = globalRequest.getParameter(fieldName+"_packageName");
        if (isView) {
            label = globalRequest.getParameter(fieldName+"_label");
        }
        String packageLabel = globalRequest.getParameter(fieldName+"_label");
        if (Validator.isEmpty(globalRequest.getParameter(fieldName+"_setup"))) {
            throw new CustomBaseException("Pls enter dropdown''s setup value for ["+packageLabel+"]");
        } else {
            String[] setupArr = globalRequest.getParameter(fieldName+"_setup").split(";");
            if (setupArr.length == 1 || setupArr.length == 3) {
                if (setupArr.length == 3) {
                    ddKey =  setupArr[1];
                    ddValue =  setupArr[2];
                }
                ddList = initLower(setupArr[0]);
                populateGetList(initCap(ddList), "");
                ddList += "NE";
            } else {
                ddList = initLower(setupArr[0]);
                ddKey =  setupArr[1];
                ddValue =  setupArr[2];
                if (setupArr.length == 4) {
                    populateGetList(initCap(ddList), setupArr[3]);
                }
            }
        }
        htmlBuilder.append(
"                    <td>\n"+
"                        <div class=\"radio radio-inline radio-success\">\n"+
(hiddenPK==null?"":"                       "+hiddenPK+"\n")+
"                            <s:radio id=\""+formatListID(fieldName, nextLevel)+"\" list=\""+ddList+"\" listKey=\""+ddKey+"\" listValue=\""+ddValue+"\" name=\""+formatListName(fieldName, nextLevel)+"\" value=\"%{"+valueVar+"}\" "+(req?"required=\"required\"":"")+"/>\n" +
"                        </div>\n"+
"                    </td>\r\n");
    }
    private void appendTextField_mi(StringBuilder htmlBuilder, String fieldName, String valueVar, String nextLevel, Boolean req, String date_numeric_float_class, String maxLength) {
        checkMiColStart(htmlBuilder, fieldName);
        String label = globalRequest.getParameter(fieldName+"_packageName");
        if (isView) {
            label = globalRequest.getParameter(fieldName+"_label");
        }
        if (CurrentTemplateClass.mainTextField_templateStr != null) {
            htmlBuilder.append(CurrentTemplateClass.mainTextField_templateStr.replaceAll("_ReplaceLabel_", label).replaceAll("_ReplaceReq_", (req?"<font class=\"asterisk\">*</font>":""))
            .replaceAll("_ReplaceID_", formatListID(fieldName, nextLevel)).replaceAll("_ReplaceMaxLength_", maxLength)
            .replaceAll("_ReplaceCssClass_", date_numeric_float_class).replaceAll("_ReplaceFieldName_", formatListName(fieldName, nextLevel))
            .replaceAll("_ReplaceValueVar_", valueVar).replaceAll("_ReplaceInputReq_", (req?"required=\"required\"":"")));
        } else {
            htmlBuilder.append(
    "            <div class=\"form-horizontal form-group\">\n" +
    "                <label class=\"col-md-4 control-label\"><s:text name=\""+label+"\" /> "+ (req?"<font class=\"asterisk\">*</font>":"") +"</label>\n" +
    "                <div class=\"col-md-5\">\n" +
    "                    <s:textfield id=\"field_"+nextLevel+lastFieldName(fieldName)+"\" cssClass=\"form-control\" name=\""+formatListName(fieldName, nextLevel)+"\" value=\"%{"+valueVar+"}\" "+(req?"required=\"required\"":"")+"/>\n" +
    "                </div>\n" +
    "            </div>\r\n");
        }
        checkMiColEnd(htmlBuilder, fieldName);
    }
    
    private void appendTextArea_mi(StringBuilder htmlBuilder, String fieldName, String valueVar, String nextLevel, Boolean req, String date_numeric_float_class, String taSetup) throws Exception {
        checkMiColStart(htmlBuilder, fieldName);
        String label = globalRequest.getParameter(fieldName+"_packageName");
        if (isView) {
            label = globalRequest.getParameter(fieldName+"_label");
        }
        if (CurrentTemplateClass.mainTextarea_templateStr != null) {
            htmlBuilder.append(CurrentTemplateClass.mainTextarea_templateStr.replaceAll("_ReplaceLabel_", label).replaceAll("_ReplaceReq_", (req?"<font class=\"asterisk\">*</font>":""))
            .replaceAll("_ReplaceID_", formatListID(fieldName, nextLevel)).replaceAll("_ReplaceTaSetup_", taSetup)
            .replaceAll("_ReplaceCssClass_", date_numeric_float_class).replaceAll("_ReplaceFieldName_", formatListName(fieldName, nextLevel))
            .replaceAll("_ReplaceValueVar_", valueVar).replaceAll("_ReplaceInputReq_", (req?"required=\"required\"":"")));
        } else {
            htmlBuilder.append(
    "            <div class=\"form-horizontal form-group\">\n" +
    "                <label class=\"col-md-4 control-label\"><s:text name=\""+label+"\" /> "+ (req?"<font class=\"asterisk\">*</font>":"") +"</label>\n" +
    "                <div class=\"col-md-5\" id=\"field_"+nextLevel+lastFieldName(fieldName)+"_div\" >\n" +
    "                    <s:textarea id=\"field_"+nextLevel+lastFieldName(fieldName)+"\" cssClass=\"form-control "+date_numeric_float_class+"\" name=\""+fieldName+"\" "+(req?"required=\"required\"":"")+" "+ taSetup +"/>\n" +
    "                </div>\n" +
    "            </div>\r\n");
        }
        checkMiColEnd(htmlBuilder, fieldName);
    }
    
    private void appendCheckbox_mi(StringBuilder htmlBuilder, String fieldName, String valueVar, String nextLevel, Boolean req) throws Exception {
        checkMiColStart(htmlBuilder, fieldName);
        String label = globalRequest.getParameter(fieldName+"_packageName");
        if (isView) {
            label = globalRequest.getParameter(fieldName+"_label");
        }
        String packageLabel = globalRequest.getParameter(fieldName+"_label");
        if (Validator.isEmpty(globalRequest.getParameter(fieldName+"_setup"))) {
            throw new CustomBaseException("Pls enter dropdown''s setup value for ["+packageLabel+"]");
        }
        if (CurrentTemplateClass.mainCheckbox_templateStr != null) {
            htmlBuilder.append(CurrentTemplateClass.mainCheckbox_templateStr.replaceAll("_ReplaceLabel_", label).replaceAll("_ReplaceReq_", (req?"<font class=\"asterisk\">*</font>":""))
            .replaceAll("_ReplaceID_", formatListID(fieldName, nextLevel)).replaceAll("_ReplaceFieldName_", formatListName_normalInput(fieldName, nextLevel) )
            .replaceAll("_ReplaceValueVar_", valueVar).replaceAll("_ReplaceInputReq_", (req?"required=\"required\"":""))
            .replaceAll("_ReplaceCheckedValue_", globalRequest.getParameter(fieldName+"_setup")));
        } else {
            htmlBuilder.append(
    "            <div class=\"form-horizontal form-group\">\n" +
    "                <label class=\"col-md-4 checkbox-label\"><s:text name=\""+label+"\" /> "+ (req?"<font class=\"asterisk\">*</font>":"") +"</label>\n" +
    "                <div class=\"col-md-5 checkbox right check-success\">\n" +
    "                       <input type=\"checkbox\" class=\"form-control\" value=\""+globalRequest.getParameter(fieldName+"_setup")+"\" id=\"field_"+nextLevel+lastFieldName(fieldName)+"\" name=\""+fieldName+"\" "+(req?"required=\"\"":"")+ "<s:if test='"+valueVar+".equals(\""+globalRequest.getParameter(fieldName+"_setup")+"\")'>checked</s:if>" + " >\n" +
    "                       <label for=\"field_"+nextLevel+lastFieldName(fieldName)+"\"></label>\n" +
    "                </div>\n" +
    "            </div>\r\n");
        }
        checkMiColEnd(htmlBuilder, fieldName);
    }
    
    private void appendRadio_mi(StringBuilder htmlBuilder, String fieldName, String valueVar, String nextLevel, Boolean req) throws Exception {
        checkMiColStart(htmlBuilder, fieldName);
        String label = globalRequest.getParameter(fieldName+"_packageName");
        if (isView) {
            label = globalRequest.getParameter(fieldName+"_label");
        }
        String packageLabel = globalRequest.getParameter(fieldName+"_label");
        String ddList = null, ddKey = "keyData", ddValue = "valueData";
        if (Validator.isEmpty(globalRequest.getParameter(fieldName+"_setup"))) {
            throw new CustomBaseException("Pls enter dropdown''s setup value for ["+packageLabel+"]");
        } else {
            String[] setupArr = globalRequest.getParameter(fieldName+"_setup").split(";");
            if (setupArr.length == 1 || setupArr.length == 3) {
                if (setupArr.length == 3) {
                    ddKey =  setupArr[1];
                    ddValue =  setupArr[2];
                }
                try {
                    ddList = initLower(setupArr[0]);
                    populateGetList(initCap(ddList), "");
                    ddList += "NE";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ddList = initLower(setupArr[0]);
                ddKey =  setupArr[1];
                ddValue =  setupArr[2];
                if (setupArr.length == 4) {
                    populateGetList(initCap(ddList), setupArr[3]);
                }
            }
        }
        if (CurrentTemplateClass.mainRadio_templateStr != null) {
            htmlBuilder.append(CurrentTemplateClass.mainRadio_templateStr.replaceAll("_ReplaceLabel_", label).replaceAll("_ReplaceReq_", (req?"<font class=\"asterisk\">*</font>":""))
            .replaceAll("_ReplaceID_", formatListID(fieldName, nextLevel)).replaceAll("_ReplaceFieldName_", formatListName(fieldName, nextLevel))
            .replaceAll("_ReplaceValueVar_", valueVar).replaceAll("_ReplaceInputReq_", (req?"required=\"required\"":""))
            .replaceAll("_ReplaceDdList_", ddList).replaceAll("_ReplaceDdKey_", ddKey).replaceAll("_ReplaceDdValue_", ddValue));
        } else {
            htmlBuilder.append(
    "            <div class=\"form-horizontal form-group\">\n" +
    "                <label class=\"col-md-4 radio-label\"><s:text name=\""+label+"\" /> "+ (req?"<font class=\"asterisk\">*</font>":"") +"</label>\n" +
    "                <div class=\"col-md-5 radio radio-inline radio-success\">\n" +
    "                    <s:radio id=\""+formatListID(fieldName, nextLevel)+"\" list=\""+ddList+"\" listKey=\""+ddKey+"\" listValue=\""+ddValue+"\" name=\""+fieldName+"\" value=\"%{"+valueVar+"}\" "+(req?"required=\"required\"":"")+" />\n" +        
    "                </div>\n" +
    "            </div>\r\n");
        }
        checkMiColEnd(htmlBuilder, fieldName);
    }
    
    private void appendDropdown_mi(StringBuilder htmlBuilder, String fieldName, String valueVar, String nextLevel, Boolean req) throws Exception {
        checkMiColStart(htmlBuilder, fieldName);
        String label = globalRequest.getParameter(fieldName+"_packageName");
        if (isView) {
            label = globalRequest.getParameter(fieldName+"_label");
        }
        String packageLabel = globalRequest.getParameter(fieldName+"_label");
        String ddList = null, ddKey = "keyData", ddValue = "valueData";
        if (Validator.isEmpty(globalRequest.getParameter(fieldName+"_setup"))) {
            throw new CustomBaseException("Pls enter dropdown''s setup value for ["+packageLabel+"]");
        } else {
            String[] setupArr = globalRequest.getParameter(fieldName+"_setup").split(";");
            if (setupArr.length == 1 || setupArr.length == 3) {
                if (setupArr.length == 3) {
                    ddKey =  setupArr[1];
                    ddValue =  setupArr[2];
                }
                ddList = initLower(setupArr[0]);
                populateGetList(initCap(ddList), "");
                ddList += "NE";
            } else {
                ddList = initLower(setupArr[0]);
                ddKey =  setupArr[1];
                ddValue =  setupArr[2];
                if (setupArr.length == 4) {
                    populateGetList(initCap(ddList), setupArr[3]);
                }
            }
        }
        if (CurrentTemplateClass.mainDropdown_templateStr != null) {
            if (isView) {
                htmlBuilder.append(CurrentTemplateClass.mainDropdown_templateStr.replaceAll("_ReplaceLabel_", label).replaceAll("_ReplaceReq_", (req?"<font class=\"asterisk\">*</font>":""))
                .replaceAll("_ReplaceID_", formatListID(fieldName, nextLevel)).replaceAll("_ReplaceFieldName_", formatListName(fieldName, nextLevel))
                .replaceAll("_ReplaceValueVar_", valueVar).replaceAll("_ReplaceInputReq_", (req?"required=\"required\"":""))
                .replaceAll("_ReplaceDdList_", "sampleList").replaceAll("_ReplaceDdKey_", "keyData").replaceAll("_ReplaceDdValue_", "valueData"));
            } else {
                htmlBuilder.append(CurrentTemplateClass.mainDropdown_templateStr.replaceAll("_ReplaceLabel_", label).replaceAll("_ReplaceReq_", (req?"<font class=\"asterisk\">*</font>":""))
                .replaceAll("_ReplaceID_", formatListID(fieldName, nextLevel)).replaceAll("_ReplaceFieldName_", formatListName(fieldName, nextLevel))
                .replaceAll("_ReplaceValueVar_", valueVar).replaceAll("_ReplaceInputReq_", (req?"required=\"required\"":""))
                .replaceAll("_ReplaceDdList_", ddList).replaceAll("_ReplaceDdKey_", ddKey).replaceAll("_ReplaceDdValue_", ddValue));
            }
        } else {
            htmlBuilder.append(
    "            <div class=\"form-horizontal form-group\">\n" +
    "                <label class=\"col-md-4 control-label\"><s:text name=\""+label+"\" /> "+ (req?"<font class=\"asterisk\">*</font>":"") +"</label>\n" +
    "                <div class=\"col-md-5\">\n" +
    "                    <s:select id=\"field_"+nextLevel+lastFieldName(fieldName)+"\" list=\""+(isView?"sampleList":ddList)+"\" listKey=\""+(isView?"keydata":ddKey)+"\" listValue=\""+(isView?"valueData":ddValue)+"\" name=\""+fieldName+"\" cssClass=\"form-control sds-dropdown\" value=\"%{"+valueVar+"}\" "+(req?"required=\"required\"":"")+" />\n" +        
    "                </div>\n" +
    "            </div>\r\n");
        }
        checkMiColEnd(htmlBuilder, fieldName);
    }
    
//    private void appendDropdown_mi(StringBuilder htmlBuilder, String fieldName, String valueVar, String nextLevel, Boolean req) throws Exception {
//        String label = globalRequest.getParameter(fieldName+"_packageName");
//        String packageLabel = globalRequest.getParameter(fieldName+"_label");
//        String ddList = null, ddKey = "keyData", ddValue = "valueData";
//        if (Validator.isEmpty(globalRequest.getParameter(fieldName+"_setup"))) {
//            throw new CustomBaseException("Pls enter dropdown''s setup value for ["+packageLabel+"]");
//        } else {
//            String[] setupArr = globalRequest.getParameter(fieldName+"_setup").split(";");
//            if (setupArr.length == 1 || setupArr.length == 3) {
//                if (setupArr.length == 3) {
//                    ddKey =  setupArr[1];
//                    ddValue =  setupArr[2];
//                }
//                ddList = initLower(setupArr[0]);
//                populateGetList(initCap(ddList), "");
//                ddList += "NE";
//            } else {
//                ddList = initLower(setupArr[0]);
//                ddKey =  setupArr[1];
//                ddValue =  setupArr[2];
//                if (setupArr.length == 4) {
//                    populateGetList(initCap(ddList), setupArr[3]);
//                }
//            }
//        }
//        htmlBuilder.append(
//"            <div class=\"form-horizontal form-group\">\n" +
//"                <label class=\"col-md-4 control-label\"><s:text name=\""+label+"\" /> "+ (req?"<font class=\"asterisk\">*</font>":"") +"</label>\n" +
//"                <div class=\"col-md-5\">\n" +
//"                    <s:select id=\"field_"+nextLevel+lastFieldName(fieldName)+"\" list=\""+ddList+"\" listKey=\""+ddKey+"\" listValue=\""+ddValue+"\" name=\""+formatListName(fieldName, nextLevel)+"\" cssClass=\"form-control sds-dropdown\" value=\"%{"+valueVar+"}\" "+(req?"required=\"required\"":"")+"/>\n" +
//"                </div>\n" +
//"            </div>\r\n");
//    }
    private Boolean autoSplitCol = Boolean.TRUE;
    private Integer mainCol = 2;
    private Integer mainTotalCol = 0;
    private Integer currentCol = 0;
    
    private Boolean miAutoSplitCol = Boolean.TRUE;
    private Integer miCol = 2;
    
    private Map<String, Integer> miColMap = new HashMap();
    
    
    private void checkMiColStart(StringBuilder htmlBuilder, String fieldName) {
        String temp = fieldName.substring(0, fieldName.lastIndexOf("."));
        Integer miTotalCol = miColMap.get(temp+"_miTotalCol");
        Integer miCurrentCol = miColMap.get(temp+"_miCurrentCol");
        Integer miCol = miColMap.get(temp+"_miCol");
        if (miCol == null) {
            miCol = this.miCol;
        }
        if (miTotalCol == null){
            miTotalCol = 0;
            miCurrentCol = 0;
        }
        if (miCol > 1) {
            miTotalCol++;
            miCurrentCol++;
            miColMap.put(temp+"_miTotalCol", miTotalCol);
            miColMap.put(temp+"_miCurrentCol", miCurrentCol);
            
            if (miCurrentCol == 1) {
                htmlBuilder.append("<div class=\"row ge-mi\">\n");
            }
            if (miAutoSplitCol) {
                htmlBuilder.append("   <div class=\"col-md-"+(12/miCol)+"\">\n");
            }
        } else {
            htmlBuilder.append("<div class=\"row ge-mi\">\n");
            htmlBuilder.append("   <div class=\"col-md-"+(12/miCol)+"\">\n");
        }
    }
    private void checkMiColEnd(StringBuilder htmlBuilder, String fieldName) {
        String temp = fieldName.substring(0, fieldName.lastIndexOf("."));
        Integer miTotalCol = miColMap.get(temp+"_miTotalCol");
        Integer miCurrentCol = miColMap.get(temp+"_miCurrentCol");
        Integer miCol = miColMap.get(temp+"_miCol");
        if (miCol == null) {
            miCol = this.miCol;
        }
        if (miTotalCol == null){
            miTotalCol = 0;
            miCurrentCol = 0;
        }
        if (miCol > 1) {
            if (miCurrentCol.equals(miCol)) {
                miCurrentCol = 0;
                htmlBuilder.append("</div>\n");
            }
            if (miAutoSplitCol) {
                htmlBuilder.append("   </div>\n");
            }
            miColMap.put(temp+"_miTotalCol", miTotalCol);
            miColMap.put(temp+"_miCurrentCol", miCurrentCol);
        } else {
            htmlBuilder.append("</div>\n");
            htmlBuilder.append("   </div>\n");
        }
    }
    
    private void checkMainModelColStart(StringBuilder htmlBuilder, String fieldName) {
        fieldNameSet.add(fieldName.substring(0, fieldName.lastIndexOf(".")));
        
        if (mainCol > 1) {
            mainTotalCol++;
            currentCol++;
            
            if (currentCol == 1) {
                htmlBuilder.append("<div class=\"row\">\n");
            }
            if (autoSplitCol) {
                htmlBuilder.append("   <div class=\"col-md-"+(12/mainCol)+"\">\n");
            }
        }
    }
    private void checkMainModelColEnd(StringBuilder htmlBuilder) {
        if (mainCol > 1) {
            if (currentCol.equals(mainCol)) {
                currentCol = 0;
                htmlBuilder.append("</div>\n");
            }
            if (autoSplitCol) {
                htmlBuilder.append("   </div>\n");
            }
        }
    }
    private Boolean closingForMain_checked = Boolean.FALSE;
    private void checkClosingForMain(StringBuilder htmlBuilder) {
        if (closingForMain_checked) return;
        closingForMain_checked = Boolean.TRUE;
        if (currentCol<mainCol && mainCol > 1) {
            htmlBuilder.append("</div>\n");
        }
    }
    private Boolean closingForMi_checked = Boolean.FALSE;
    private void checkClosingForMi(StringBuilder htmlBuilder, String fieldName) {
        if (closingForMi_checked) return;
        closingForMi_checked = Boolean.TRUE;
        String temp = fieldName.substring(0, fieldName.lastIndexOf("."));
        Integer miTotalCol = miColMap.get(temp+"_miTotalCol");
        Integer miCurrentCol = miColMap.get(temp+"_miCurrentCol");
        if (miTotalCol == null){
            miTotalCol = 0;
            miCurrentCol = 0;
        }
        if (miCurrentCol<miCol) {
            htmlBuilder.append("</div>\n");
        }
    }
    private void appendTextArea(StringBuilder htmlBuilder, String fieldName, String valueVar, String nextLevel, Boolean req, String date_numeric_float_class, String taSetup) throws Exception {
        checkMainModelColStart(htmlBuilder, fieldName);
        String label = globalRequest.getParameter(fieldName+"_packageName");
        if (isView) {
            label = globalRequest.getParameter(fieldName+"_label");
        }
        if (CurrentTemplateClass.mainTextarea_templateStr != null) {
            htmlBuilder.append(CurrentTemplateClass.mainTextarea_templateStr.replaceAll("_ReplaceLabel_", label).replaceAll("_ReplaceReq_", (req?"<font class=\"asterisk\">*</font>":""))
            .replaceAll("_ReplaceID_", nextLevel+lastFieldName(fieldName)).replaceAll("_ReplaceTaSetup_", taSetup)
            .replaceAll("_ReplaceCssClass_", date_numeric_float_class).replaceAll("_ReplaceFieldName_", fieldName)
            .replaceAll("_ReplaceValueVar_", valueVar).replaceAll("_ReplaceInputReq_", (req?"required=\"required\"":"")));
        } else {
            htmlBuilder.append(
    "            <div class=\"form-horizontal form-group\">\n" +
    "                <label class=\"col-md-4 control-label\"><s:text name=\""+label+"\" /> "+ (req?"<font class=\"asterisk\">*</font>":"") +"</label>\n" +
    "                <div class=\"col-md-5\" id=\"field_"+nextLevel+lastFieldName(fieldName)+"_div\" >\n" +
    "                    <s:textarea id=\"field_"+nextLevel+lastFieldName(fieldName)+"\" cssClass=\"form-control "+date_numeric_float_class+"\" name=\""+fieldName+"\" "+(req?"required=\"required\"":"")+" "+ taSetup +"/>\n" +
    "                </div>\n" +
    "            </div>\r\n");
        }
        checkMainModelColEnd(htmlBuilder);
    }
    
    private void appendTextField(StringBuilder htmlBuilder, String fieldName, String valueVar, String nextLevel, Boolean req, String date_numeric_float_class, String maxLength) throws Exception {
        checkMainModelColStart(htmlBuilder, fieldName);
        String label = globalRequest.getParameter(fieldName+"_packageName");
        if (isView) {
            label = globalRequest.getParameter(fieldName+"_label");
        }
        if (CurrentTemplateClass.mainTextField_templateStr != null) {
            htmlBuilder.append(CurrentTemplateClass.mainTextField_templateStr.replaceAll("_ReplaceLabel_", label).replaceAll("_ReplaceReq_", (req?"<font class=\"asterisk\">*</font>":""))
            .replaceAll("_ReplaceID_", nextLevel+lastFieldName(fieldName)).replaceAll("_ReplaceMaxLength_", maxLength)
            .replaceAll("_ReplaceCssClass_", date_numeric_float_class).replaceAll("_ReplaceFieldName_", fieldName)
            .replaceAll("_ReplaceValueVar_", valueVar).replaceAll("_ReplaceInputReq_", (req?"required=\"required\"":"")));
        } else {
            htmlBuilder.append(
    "            <div class=\"form-horizontal form-group\">\n" +
    "                <label class=\"col-md-4 control-label\"><s:text name=\""+label+"\" /> "+ (req?"<font class=\"asterisk\">*</font>":"") +"</label>\n" +
    "                <div class=\"col-md-5\">\n" +
    "                    <s:textfield id=\"field_"+nextLevel+lastFieldName(fieldName)+"\" "+maxLength+" cssClass=\"form-control "+date_numeric_float_class+"\" name=\""+fieldName+"\" value=\"%{"+valueVar+"}\" "+(req?"required=\"required\"":"")+"/>\n" +
    "                </div>\n" +
    "            </div>\r\n");
        }
        checkMainModelColEnd(htmlBuilder);
    }
    private void appendUppyUpload(StringBuilder htmlBuilder, String fieldName, String valueVar, String nextLevel, Boolean req) throws Exception {
        hasUppy = Boolean.TRUE;
        checkMainModelColStart(htmlBuilder, fieldName);
        String label = globalRequest.getParameter(fieldName+"_packageName");
        if (isView) {
            label = globalRequest.getParameter(fieldName+"_label");
        }
        String packageLabel = globalRequest.getParameter(fieldName+"_label");
        if (Validator.isEmpty(globalRequest.getParameter(fieldName+"_setup"))) {
            throw new CustomBaseException("Pls enter Uppy Upload''s setup value for ["+packageLabel+"]");
        }
        System.out.println("CurrentTemplateClass.mainUppyUpload_templateStr = " + CurrentTemplateClass.mainUppyUpload_templateStr);
        if (CurrentTemplateClass.mainUppyUpload_templateStr != null) {
            System.out.println("in appendUppyUpload using template");
            String[] setupArr = globalRequest.getParameter(fieldName+"_setup").split(";");
            String getModelFunction = setupArr[0];
            String _ReplaceFileCode_ = setupArr[1];
            String uploadURL = null;
            String tus = "";
            String _Replace_tus_ = "";
            if (setupArr.length > 2) {
                for (int i = 2; i < setupArr.length; i++) {
                    System.out.println("setupArr[i] = " + setupArr[i]);
                    switch(setupArr[i].split("=")[0].toLowerCase()) {
                        case "uploadurl":
                            uploadURL = setupArr[i].split("=")[1]; break;
                        case "tus":
                            tus = setupArr[i].split("=")[1]; break;
                    }
                }
            } 
            if (tus.equalsIgnoreCase("true") || tus.equalsIgnoreCase("y")) {
                _Replace_tus_ = "_tus";
                if (uploadURL == null) {
                    uploadURL = "upload_tus";
                }
            }
            if (uploadURL == null) {
                uploadURL = "uploadUppy";
            }
            
            String fieldNameToPkName = fieldName.substring(0, fieldName.lastIndexOf("."))+".ID";
            String fieldNameToPkID = fieldName.substring(0, fieldName.lastIndexOf(".")+1)+"_ID";
            String methodNoGet_lower = getModelFunction.substring(3);
            methodNoGet_lower = methodNoGet_lower.substring(0, 1).toLowerCase() + methodNoGet_lower.substring(1);
            String _ReplaceDrDocNAME_NAME_ = fieldName.substring(0, fieldName.lastIndexOf(".")+1)+methodNoGet_lower+".dr_doc_name";
//            String fk_id
//            String _ReplaceDrDocFK_XX_ = 
            htmlBuilder.append(CurrentTemplateClass.mainUppyUpload_templateStr.replaceAll("_ReplaceLabel_", label).replaceAll("_ReplaceReq_", (req?"<font class=\"asterisk\">*</font>":""))
            .replaceAll("_ReplaceID_", nextLevel+lastFieldName(fieldName)).replaceAll("_ReplaceFieldName_", fieldName)
            .replaceAll("_ReplaceValueVar_", valueVar).replaceAll("_ReplaceInputReq_", (req?"required=\"required\"":""))
            .replaceAll("_ReplaceFileCode_", _ReplaceFileCode_)
            .replaceAll("_ReplaceFileCode_NAME_", _ReplaceFileCode_+nextLevel+lastFieldName(fieldName))
            .replaceAll("_ReplaceUploadURL_", uploadURL)
            .replaceAll("_ReplacePkName_", fieldNameToPkName)
            .replaceAll("_ReplaceDrDocPK_ID_", "field_"+recUniqueIdx_sTag(nextLevel)+"_ID")
            .replaceAll("_ReplaceDrDocFK_ID_", "field_"+nextLevel+lastFieldName(fieldName))
            .replaceAll("_ReplaceDrDocFK_NAME_", fieldName)
            .replaceAll("_theRecord_id_", "field_"+recUniqueIdx_sTag(nextLevel)+"_ID")
            .replaceAll("_ReplaceDrDocNAME_NAME_", _ReplaceDrDocNAME_NAME_)
            .replaceAll("_Replace_tus_", _Replace_tus_)
            .replaceAll("_ReplaceDrDocNAME_ID_", mainModelVarName+"_"+formatListID(fieldName, nextLevel)+"_drDocName")
            );
        } else {
            System.out.println("in else not using template");
            htmlBuilder.append(
    "            <div class=\"form-horizontal form-group\">\n" +
    "                <label class=\"col-md-4 checkbox-label\"><s:text name=\""+label+"\" /> "+ (req?"<font class=\"asterisk\">*</font>":"") +"</label>\n" +
    "                <div class=\"col-md-5 checkbox right check-success\">\n" +
    "                       <input type=\"checkbox\" class=\"form-control\" value=\""+globalRequest.getParameter(fieldName+"_setup")+"\" id=\"field_"+nextLevel+lastFieldName(fieldName)+"\" name=\""+fieldName+"\" "+(req?"required=\"\"":"")+ "<s:if test='"+valueVar+".equals(\""+globalRequest.getParameter(fieldName+"_setup")+"\")'>checked</s:if>" + " >\n" +
    "                       <label for=\"field_"+nextLevel+lastFieldName(fieldName)+"\"></label>\n" +
    "                </div>\n" +
    "            </div>\r\n");
        }
        checkMainModelColEnd(htmlBuilder);
    }
    private void appendCheckbox(StringBuilder htmlBuilder, String fieldName, String valueVar, String nextLevel, Boolean req) throws Exception {
        checkMainModelColStart(htmlBuilder, fieldName);
        String label = globalRequest.getParameter(fieldName+"_packageName");
        if (isView) {
            label = globalRequest.getParameter(fieldName+"_label");
        }
        String packageLabel = globalRequest.getParameter(fieldName+"_label");
        if (Validator.isEmpty(globalRequest.getParameter(fieldName+"_setup"))) {
            throw new CustomBaseException("Pls enter dropdown''s setup value for ["+packageLabel+"]");
        }
        if (CurrentTemplateClass.mainCheckbox_templateStr != null) {
            htmlBuilder.append(CurrentTemplateClass.mainCheckbox_templateStr.replaceAll("_ReplaceLabel_", label).replaceAll("_ReplaceReq_", (req?"<font class=\"asterisk\">*</font>":""))
            .replaceAll("_ReplaceID_", nextLevel+lastFieldName(fieldName)).replaceAll("_ReplaceFieldName_", fieldName)
            .replaceAll("_ReplaceValueVar_", valueVar).replaceAll("_ReplaceInputReq_", (req?"required=\"required\"":""))
            .replaceAll("_ReplaceCheckedValue_", globalRequest.getParameter(fieldName+"_setup")));
        } else {
            htmlBuilder.append(
    "            <div class=\"form-horizontal form-group\">\n" +
    "                <label class=\"col-md-4 checkbox-label\"><s:text name=\""+label+"\" /> "+ (req?"<font class=\"asterisk\">*</font>":"") +"</label>\n" +
    "                <div class=\"col-md-5 checkbox right check-success\">\n" +
    "                       <input type=\"checkbox\" class=\"form-control\" value=\""+globalRequest.getParameter(fieldName+"_setup")+"\" id=\"field_"+nextLevel+lastFieldName(fieldName)+"\" name=\""+fieldName+"\" "+(req?"required=\"\"":"")+ "<s:if test='"+valueVar+".equals(\""+globalRequest.getParameter(fieldName+"_setup")+"\")'>checked</s:if>" + " >\n" +
    "                       <label for=\"field_"+nextLevel+lastFieldName(fieldName)+"\"></label>\n" +
    "                </div>\n" +
    "            </div>\r\n");
        }
        checkMainModelColEnd(htmlBuilder);
    }
    private void appendDropdown(StringBuilder htmlBuilder, String fieldName, String valueVar, String nextLevel, Boolean req) throws Exception {
        checkMainModelColStart(htmlBuilder, fieldName);
        String label = globalRequest.getParameter(fieldName+"_packageName");
        if (isView) {
            label = globalRequest.getParameter(fieldName+"_label");
        }
        String packageLabel = globalRequest.getParameter(fieldName+"_label");
        String ddList = null, ddKey = "keyData", ddValue = "valueData";
        if (Validator.isEmpty(globalRequest.getParameter(fieldName+"_setup"))) {
            throw new CustomBaseException("Pls enter dropdown''s setup value for ["+packageLabel+"]");
        } else {
            String[] setupArr = globalRequest.getParameter(fieldName+"_setup").split(";");
            if (setupArr.length == 1 || setupArr.length == 3) {
                if (setupArr.length == 3) {
                    ddKey =  setupArr[1];
                    ddValue =  setupArr[2];
                }
                ddList = initLower(setupArr[0]);
                populateGetList(initCap(ddList), "");
                ddList += "NE";
            } else {
                ddList = initLower(setupArr[0]);
                ddKey =  setupArr[1];
                ddValue =  setupArr[2];
                if (setupArr.length == 4) {
                    populateGetList(initCap(ddList), setupArr[3]);
                }
            }
        }
        if (CurrentTemplateClass.mainDropdown_templateStr != null) {
            if (isView) {
                htmlBuilder.append(CurrentTemplateClass.mainDropdown_templateStr.replaceAll("_ReplaceLabel_", label).replaceAll("_ReplaceReq_", (req?"<font class=\"asterisk\">*</font>":""))
                .replaceAll("_ReplaceID_", nextLevel+lastFieldName(fieldName)).replaceAll("_ReplaceFieldName_", fieldName)
                .replaceAll("_ReplaceValueVar_", valueVar).replaceAll("_ReplaceInputReq_", (req?"required=\"required\"":""))
                .replaceAll("_ReplaceDdList_", "sampleList").replaceAll("_ReplaceDdKey_", "keyData").replaceAll("_ReplaceDdValue_", "valueData"));
            } else {
                htmlBuilder.append(CurrentTemplateClass.mainDropdown_templateStr.replaceAll("_ReplaceLabel_", label).replaceAll("_ReplaceReq_", (req?"<font class=\"asterisk\">*</font>":""))
                .replaceAll("_ReplaceID_", nextLevel+lastFieldName(fieldName)).replaceAll("_ReplaceFieldName_", fieldName)
                .replaceAll("_ReplaceValueVar_", valueVar).replaceAll("_ReplaceInputReq_", (req?"required=\"required\"":""))
                .replaceAll("_ReplaceDdList_", ddList).replaceAll("_ReplaceDdKey_", ddKey).replaceAll("_ReplaceDdValue_", ddValue));
            }
        } else {
            htmlBuilder.append(
    "            <div class=\"form-horizontal form-group\">\n" +
    "                <label class=\"col-md-4 control-label\"><s:text name=\""+label+"\" /> "+ (req?"<font class=\"asterisk\">*</font>":"") +"</label>\n" +
    "                <div class=\"col-md-5\">\n" +
    "                    <s:select id=\"field_"+nextLevel+lastFieldName(fieldName)+"\" list=\""+(isView?"sampleList":ddList)+"\" listKey=\""+(isView?"keyData":ddKey)+"\" listValue=\""+(isView?"valueData":ddValue)+"\" name=\""+fieldName+"\" cssClass=\"form-control sds-dropdown\" value=\"%{"+valueVar+"}\" "+(req?"required=\"required\"":"")+" />\n" +        
    "                </div>\n" +
    "            </div>\r\n");
        }
        checkMainModelColEnd(htmlBuilder);
    }
    private void appendRadio(StringBuilder htmlBuilder, String fieldName, String valueVar, String nextLevel, Boolean req) throws Exception {
        checkMainModelColStart(htmlBuilder, fieldName);
        String label = globalRequest.getParameter(fieldName+"_packageName");
        if (isView) {
            label = globalRequest.getParameter(fieldName+"_label");
        }
        String packageLabel = globalRequest.getParameter(fieldName+"_label");
        String ddList = null, ddKey = "keyData", ddValue = "valueData";
        if (Validator.isEmpty(globalRequest.getParameter(fieldName+"_setup"))) {
            throw new CustomBaseException("Pls enter dropdown''s setup value for ["+packageLabel+"]");
        } else {
            String[] setupArr = globalRequest.getParameter(fieldName+"_setup").split(";");
            if (setupArr.length == 1 || setupArr.length == 3) {
                if (setupArr.length == 3) {
                    ddKey =  setupArr[1];
                    ddValue =  setupArr[2];
                }
                try {
                    ddList = initLower(setupArr[0]);
                    populateGetList(initCap(ddList), "");
                    ddList += "NE";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ddList = initLower(setupArr[0]);
                ddKey =  setupArr[1];
                ddValue =  setupArr[2];
                if (setupArr.length == 4) {
                    populateGetList(initCap(ddList), setupArr[3]);
                }
            }
        }
        if (CurrentTemplateClass.mainRadio_templateStr != null) {
            if (isView) {
                htmlBuilder.append(CurrentTemplateClass.mainRadio_templateStr.replaceAll("_ReplaceLabel_", label).replaceAll("_ReplaceReq_", (req?"<font class=\"asterisk\">*</font>":""))
                .replaceAll("_ReplaceID_", formatListID(fieldName, nextLevel)).replaceAll("_ReplaceFieldName_", formatListName(fieldName, nextLevel))
                .replaceAll("_ReplaceValueVar_", valueVar).replaceAll("_ReplaceInputReq_", (req?"required=\"required\"":""))
                .replaceAll("_ReplaceDdList_", "sampleList").replaceAll("_ReplaceDdKey_", "keyData").replaceAll("_ReplaceDdValue_", "valueData"));
            } else {
                htmlBuilder.append(CurrentTemplateClass.mainRadio_templateStr.replaceAll("_ReplaceLabel_", label).replaceAll("_ReplaceReq_", (req?"<font class=\"asterisk\">*</font>":""))
                .replaceAll("_ReplaceID_", nextLevel+lastFieldName(fieldName)).replaceAll("_ReplaceFieldName_", fieldName)
                .replaceAll("_ReplaceValueVar_", valueVar).replaceAll("_ReplaceInputReq_", (req?"required=\"required\"":""))
                .replaceAll("_ReplaceDdList_", ddList).replaceAll("_ReplaceDdKey_", ddKey).replaceAll("_ReplaceDdValue_", ddValue));
            }
        } else {
            htmlBuilder.append(
    "            <div class=\"form-horizontal form-group\">\n" +
    "                <label class=\"col-md-4 radio-label\"><s:text name=\""+label+"\" /> "+ (req?"<font class=\"asterisk\">*</font>":"") +"</label>\n" +
    "                <div class=\"col-md-5 radio radio-inline radio-success\">\n" +
    "                    <s:radio id=\"field_"+nextLevel+lastFieldName(fieldName)+"\" list=\""+ddList+"\" listKey=\""+ddKey+"\" listValue=\""+ddValue+"\" name=\""+fieldName+"\" value=\"%{"+valueVar+"}\" "+(req?"required=\"required\"":"")+" />\n" +        
    "                </div>\n" +
    "            </div>\r\n");
        }
        checkMainModelColEnd(htmlBuilder);
    }
    
    public String generate() {
        return "generate";
    }
    public static List registeredClassList = null;
    public List getRegisteredClassList() {
        if (registeredClassList == null) {
            registeredClassList = new ArrayList();
            if (SessionFactoryImpl.registeredClass == null) {
                baseDAO.getSession();
            }
            for (Object obj : SessionFactoryImpl.registeredClass) {
                registeredClassList.add(new Options(((Class)obj).getSimpleName(), ((Class)obj).getSimpleName()));
            }
            CommonList.addBlankOption(registeredClassList, "", getText("pleaseSelect"));
        }
        return registeredClassList;
    }
    
    private String selectedClass = null;
    public String getSelectedClass() {
        return selectedClass;
    }
    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }
    
    private Class getModelClass() {
        if (SessionFactoryImpl.registeredClass == null) {
            baseDAO.getSession();
        }
        for (Object obj : SessionFactoryImpl.registeredClass) {
            if (((Class)obj).getSimpleName().equals(selectedClass)) {
                return (Class)obj;
            }
        }
        return null;
    }
    private Class getModelClass(String selectedClass) throws Exception {
        if (SessionFactoryImpl.registeredClass == null) {
            baseDAO.getSession();
        }
        for (Object obj : SessionFactoryImpl.registeredClass) {
            if (((Class)obj).getSimpleName().equals(selectedClass)) {
                return (Class)obj;
            }
        }
        return null;
    }
    public Integer childLevel = 1;
    public Integer getChildLevel() {
        return childLevel;
    }
    public void setChildLevel(Integer childLevel) {
        this.childLevel = childLevel;
    }
    
    private String parentModelClass = "theModel";
    public String getParentModelClass() {
        return parentModelClass;
    }
    public void setParentModelClass(String parentModelClass) {
        this.parentModelClass = parentModelClass;
    }
    
    private String parentModelName = "theModel";
    public String getParentModelName() {
        return parentModelName;
    }
    public void setParentModelName(String parentModelName) {
        this.parentModelName = parentModelName;
    }

    private String mainModelVarName = "model";
    public String getMainModelVarName() {
        return mainModelVarName;
    }
    public void setMainModelVarName(String mainModelVarName) {
        this.mainModelVarName = mainModelVarName;
    }
    
    
    private String childModelName = null;
    public String getChildModelName() {
        return childModelName;
    }
    public void setChildModelName(String childModelName) {
        this.childModelName = childModelName;
    }
    
    public String addChildList() throws Exception {
        modelChanged();
        return null;
    }
    
    private String[] entryModels = null;
    public String[] getEntryModels() {
        return entryModels;
    }
    public void setEntryModels(String[] entryModels) {
        this.entryModels = entryModels;
    }
    
    private final String indent = "    ";
    private String whiteSpace = "";
    private Integer padding = 0;
//    private String extraTd = null;
//    private String extraTh = null;
    private String extraTd = "";
    private String extraTh = "";
    private Integer colSpan = 5;
    public String modelChanged() throws Exception {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Class modelClass = getModelClass();
//        if (parentModelName.equals("theModel") && !Validator.isEmpty(mainModelVarName)) {
//            parentModelName = mainModelVarName;
//        }
        if (parentModelName == null) parentModelName = "theModel";
        if (parentModelName.split("\\.").length > 0) {
            for (String split : parentModelName.split("\\.")) {
                padding++;
//                if(extraTd ==null) {
//                    extraTd = "";
//                    extraTh = "";
//                    whiteSpace = "";
//                } else {
//                    colSpan++;
//                    extraTd += "<td/>";
//                    extraTh += "<th width='20px'/>";
//                    whiteSpace += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
//                }
            }
        }
        String html = "";
        List<Options> oneToManyList = new ArrayList();
        for(Method method : modelClass.getDeclaredMethods()){
            if (!method.getName().startsWith("get")) continue;
            Annotation[] annotations = method.getAnnotations();
            Boolean isPK = Boolean.FALSE;
            String name = null;
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i].annotationType().equals(Id.class)) {
                    isPK = Boolean.TRUE;
                }
                if (annotations[i].annotationType().equals(Column.class)) {
                    name = ((Column)annotations[i]).name();
                }
                if (annotations[i].annotationType().equals(OneToMany.class)) {
                    oneToManyList.add(new Options(parentModelName+"."+removeGet(method.getName()), ((OneToMany)annotations[i]).targetEntity().getSimpleName()));
                }
            }
            if (name != null) {
                String labelName = name;
                
                if (name.equalsIgnoreCase("created_date") || name.equalsIgnoreCase("created_by") ||
                    name.equalsIgnoreCase("updated_date") || name.equalsIgnoreCase("updated_by")) {
                    continue; //do not generate the input fields for these columns
                }
                if (method.getGenericReturnType().toString().equals("class java.lang.String")) {
                } else if (method.getGenericReturnType().toString().equals("class java.lang.Double")) {
                    name += "_str";
                } else if (method.getGenericReturnType().toString().equals("class java.lang.Long")) {
                    name += "_str";
                } else if (method.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                    name += "_str";
                } else if (method.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
                    name += "_str";
                }
                if (isPK) {
                    html = getHtmlInput("ID", parentModelName+".ID", modelClass.getSimpleName(), Boolean.TRUE) + html;
                } else {
                    html += getHtmlInput(labelName, parentModelName+"."+name, modelClass.getSimpleName(), Boolean.FALSE);
                }
            }
        }
        
        String colSetup = 
        "<div class=\"col-md-8 row\">\n" + 
        "    <label class=\"col-md-5 col-form-label col-form-label-sm\">"+ (parentModelName.equals("theModel")?"Main Model":formatName(parentModelName)) +": Column/Field per row"+(parentModelName.equals("theModel")?"":" (for MI)")+"</label>\n" +
        "    <div class='col-md-7'><input type='text' class='form-control form-control-sm' name='"+parentModelName.replaceAll("\\.", "_")+"_noOfColumn'></div>\n" +
        "</div>";
//        System.out.println("html = " + html);
        html = "<div class='row'/><hr><div class='row mb-1'>"+colSetup+"</div><input type=\"hidden\" name=\"entryModels\" value=\""+parentModelName+";"+modelClass.getSimpleName()+"\">\r\n" +
               "<table class=\"allTables table table-sds table-sm fs--1 table-striped table-hover\" id=\""+parentModelName.replaceAll("\\.", "_")+"_id\" width=\"100%\">\r\n" +
               "    <thead class=\"bg-200 text-900\">" +
               "        <tr>"+extraTh+"<th style='width:1%;' class=\"align-middle white-space-nowrap sort\">No.</th><th class=\"align-middle white-space-nowrap sort\">Package Text</th><th class=\"align-middle white-space-nowrap sort\">Package Label</th><th class=\"align-middle white-space-nowrap sort\">Field Type</th><th class=\"align-middle white-space-nowrap sort\" title=\"Tick for Required Field\">Req.?</th>"+(parentModelName.indexOf(".")>0?"<th class=\"align-middle white-space-nowrap sort\" title=\"More Infor\">MI</th>":"")+"<th>Field Setup</th></tr>" + 
               "    </thead>" + 
                html + "\r\n</table>\r\n";
        if (!oneToManyList.isEmpty()) {
            System.out.println(" list not empty ");
//            html += "<div class=\"row\">";
            for (Options option : oneToManyList) {
                String btnDesc = formatName(option.getKeyData());
                html += "   <div style='padding-left:"+padding*18+"px' id=\""+option.getKeyData().replaceAll("\\.", "_") +"\" >\r\n" +
                        "       "+whiteSpace+"<input type=\"button\" class='btn btn-sm btn-secondary mb-1' onclick=\"addList('"+ option.getKeyData().replaceAll("\\.", "_") +"', '"+ option.getKeyData() +"', '"+modelClass.getSimpleName()+"', '"+option.getValueData()+"')\" value='Add "+ btnDesc +"'>\r\n" + 
                        "   </div>\r\n";
//                html += "<div id=\""+option.getValueData()+"\" class=\"form-horizontal form-group\">\r\n" +
//                        "   <label class=\"col-md-4 control-label\">"+option.getValueData()+"</label>\r\n" +
//                        "   <div class=\"col-md-5\">\r\n" +
//                        "       <input type=\"button\" onclick=\"addList('"+ option.getKeyData() +"', '"+modelClass.getSimpleName()+"', '"+option.getValueData()+"')\" value='Add List'>\r\n" +
//                        "   </div>\r\n" +
//                        "</div>\r\n";
            }
//            html += "</div>"; //closing for <div class=\"row\">
        }
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().append(html);
        response.flushBuffer();
        return null;
    }
    private Integer currentRowIdx = 2;
    private String getHtmlInput(String labelName, String inputName, String modelName, Boolean isHidden) {
        String oriModelName = modelName;
        modelName = modelName.replace("Model", "");
        if (labelName==null) {
            labelName = inputName;
        }
        Integer dotCount = StringUtils.countMatches(inputName, ".");
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String tfChecked="", ddChecked="", cbChecked="", rdChecked="";
        String tfSelected="", taSelected="", ddSelected="", cbSelected="", rdSelected="", igSelected="", hdSelected="", uuSelected="";
        if (request.getParameter(inputName+"_type") == null) {
            tfChecked="checked=\"checked\"";
            tfSelected="selected";
        } else {
            if (request.getParameter(inputName+"_type").equalsIgnoreCase("dd")) {
                ddChecked="checked";
                ddSelected="selected";
            } else if (request.getParameter(inputName+"_type").equalsIgnoreCase("cb")) {
                cbChecked="checked";
                cbSelected="selected";
            } else if (request.getParameter(inputName+"_type").equalsIgnoreCase("rd")) {
                rdChecked="checked";
                rdSelected="selected";
            } else if (request.getParameter(inputName+"_type").equalsIgnoreCase("ig")) {
                igSelected="selected";
            } else if (request.getParameter(inputName+"_type").equalsIgnoreCase("hd")) {
                hdSelected="selected";
            }
        }
        String packageLabel= WordUtils.capitalize(labelName.replace("_", " "));
        Integer currentIdx = currentRowIdx;
        if (labelName.equals("ID")){
            currentIdx = 1;
        } else {
            currentRowIdx++;
        }
        if (packageLabel.endsWith(" Id")) {
            packageLabel = packageLabel.substring(0, packageLabel.length()-2) + "ID";
        }
        if (packageLabel.endsWith(" Dob")) {
            packageLabel = packageLabel.substring(0, packageLabel.length()-3) + "DOB";
        }
        if (packageLabel.contains(" Dob ")) {
            packageLabel = packageLabel.replaceAll(" Dob ", " DOB ");
        }
        if (packageLabel.contains(" Id ")) {
            packageLabel = packageLabel.replaceAll(" Id ", " ID ");
        }
        System.out.println("=====================================================================packageLabel = " + packageLabel);
        return  "<tr id=\"row-"+(currentIdx)+"\" hiddenIdName=\""+inputName.replaceAll("\\.", "-")+"\" class=\"templateFieldRow\">\r\n" + extraTd + "\r\n" + 
                "   <td>"+(currentIdx)+"</td>\r\n" + 
                "   <td>\r\n" + 
                "      <input type=\"text\" name=\""+inputName+"_packageName\" class=\"form-control form-control-sm\" value=\""+modelName+"."+labelName+"\">\r\n" +
                "      <input type=\"hidden\" name=\""+inputName+"_sorting\" id=\""+(inputName.replaceAll("\\.", "-"))+"_sorting\" value=\"\">\r\n"+
                "   </td>\r\n" + 
                "   <td><input type=\"text\" name=\""+inputName+"_label\" class=\"form-control form-control-sm\" value=\""+packageLabel+"\"></td>\r\n" + 
                "   <td>\r\n" + 
                "       <select name=\""+ inputName +"_type\" id=\""+ inputName +"_type\" class=\"form-control form-control-sm\" modelName=\""+oriModelName+"\" onchange=\"fieldTypeChanged(this)\">\r\n" +
                "           <option "+tfSelected+" value=\"tf\">Textfield</option>\r\n" + 
                "           <option "+taSelected+" value=\"ta\">Textarea</option>\r\n" + 
                "           <option "+ddSelected+" value=\"dd\">DropDown</option>\r\n" + 
                "           <option "+cbSelected+" value=\"cb\">Checkbox</option>\r\n" + 
                "           <option "+rdSelected+" value=\"rd\">Radio</option>\r\n" + 
                "           <option "+hdSelected+" value=\"hd\">Hidden</option>\r\n" + 
                "           <option "+igSelected+" value=\"ig\">Ignore This</option>\r\n" + 
                "           <option "+igSelected+" value=\"mi\">More Info</option>\r\n" + 
                "           <option "+uuSelected+" value=\"uu\">Uppy Upload</option>\r\n" + 
                "   </td>\r\n" + 
                "   <td class='text-center'><div class='form-check fs-0 mb-0'><input type=\"checkbox\" name=\""+inputName+"_required\" class=\"form-check-input float-none\" value=\"Y\"></div></td>\r\n" + 
                (dotCount>1?"   <td><div class='form-check fs-0 mb-0'><input type=\"checkbox\" name=\""+inputName+"_mi\" class=\"form-check-input float-none\" value=\"Y\"></div></td>\r\n":"")+
                "   <td class='text-center'><input type=\"text\" name=\""+inputName+"_setup\" id=\""+inputName+"_setup\" class=\"form-control form-control-sm\" value=\"\"></td>\r\n" + 
                "</tr>";
//                "<tr><td colspan=\"4\" id=\""+inputName+"_id\">" +
//                " <div class=\"form-horizontal form-group\">\n" +
//"                        <label class=\"col-md-4 control-label\">Input <font class=\"asterisk\">*</font></label>\n" +
//"                        <div class=\"col-md-5\">\n" +
//"                            <input type=\"text\" id='inputId' class=\"form-control\" name=\"input\" value=\"\" required/>\n" +
//"                        </div>\n" +
//"                    </div>"
//                + "<div class=\"form-horizontal form-group\">\n" +
//"                        <label class=\"col-md-4 control-label\">Input <font class=\"asterisk\">*</font></label>\n" +
//"                        <div class=\"col-md-5\">\n" +
//"                            <input type=\"text\" id='inputId' class=\"form-control\" name=\"input\" value=\"\" required/>\n" +
//"                        </div>\n" +
//"                    </div>" + 
//                "</td></tr>";
        
//                return "<div class=\"form-horizontal form-group\">\r\n" +
//"   <label class=\"col-md-4 control-label\">"+modelName+"."+labelName+"</label>\r\n" +
//"   <div class=\"col-md-5\">\r\n" +
//"       Label\r\n" +
//"       <input type=\"text\" name=\""+inputName+"_label\" value=\""+packageLabel+"\">\r\n" +
//"       Setup\r\n" +
//"       <input type=\"text\" name=\""+inputName+"_setup\" value=\"\">\r\n" +
//"       <div class=\"col-md-5 radio radio-inline radio-success\">\n" +
//"           <input "+tfChecked+" value=\"tf\" name=\""+inputName+"_type\" id=\""+inputName+"tf\" type=\"radio\">\n" +
//"           <label for=\""+inputName+"tf\">Textfield</label>\n" +
//"           <input "+ddChecked+" value=\"dd\" name=\""+inputName+"_type\" id=\""+inputName+"dd\" type=\"radio\">\n" +
//"           <label for=\""+inputName+"dd\">Dropdown</label>\n" +
//"           <input "+cbChecked+" value=\"cb\" name=\""+inputName+"_type\" id=\""+inputName+"cb\" type=\"radio\">\n" +
//"           <label for=\""+inputName+"cb\">Checkbox</label>\n" +
//"           <input "+rdChecked+" value=\"rd\" name=\""+inputName+"_type\" id=\""+inputName+"rd\" type=\"radio\">\n" +
//"           <label for=\""+inputName+"rd\">Radio</label>\n" +
//"           <input value=\"ig\" name=\""+inputName+"_type\" id=\""+inputName+"ig\" type=\"radio\">\n" +
//"           <label for=\""+inputName+"ig\">Ignore</label>\n" +
//"       </div>\r\n" +
//"   </div>\r\n" +
//"</div>\r\n";
                
//        return "<div class=\"form-horizontal form-group\">\r\n" +
//"   <label class=\"col-md-4 control-label\"><s:text name=\""+modelName+"."+inputName+"\"/></label>\r\n" +
//"   <div class=\"col-md-5\">\r\n" +
//"       <s:"+(isHidden?"hidden":"textfield")+" name=\""+inputName+"\" cssClass=\"form-control\" value=\"%{"+inputName+"}\"/>\r\n" +
//    "       </div>\r\n" +
//"</div>\r\n";
    }
    public static void writeFile(String filePath, String fileName, String data) {
        try {
            String enterChar = "\r\n";
            //File dir = new File(logPath + fileName + ".log");
            File dir = new File(filePath);

            //create directory if not exist
            if (dir.exists() == false) {
                dir.mkdirs();
                dir.createNewFile();
                enterChar = "";
            }
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(filePath + fileName, true));
            writer.write(enterChar + data);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
//    private User theModel = null;
//    public User getTheModel() {
//        return theModel;
//    }
//    public String viewGenerated() throws Exception{
//        theModel = new User();
//        GroupUser groupUser = new GroupUser();
//        groupUser.setUg_user_id("userId_1");
//        List groupUserList = new ArrayList();
//        groupUserList.add(groupUser);
//        theModel.setGroupUserList(groupUserList);
//        List userPrefList = new ArrayList();
//        UserPreferenceModel userPref = new UserPreferenceModel();
//        userPref.setPref_code("pre code");
//        userPrefList.add(userPref);
//        theModel.setUserPreferenceList(userPrefList);
//        return "viewGenerated";
//    }
    
    private ParentModel theModel = null;
    public ParentModel getTheModel() {
        return theModel;
    }
    public void setTheModel(ParentModel theModel) {
        this.theModel = theModel;
    }
    public String viewGenerated() {
        theModel = new ParentModel();
        ChildModel child1 = new ChildModel();
        child1.setChild_age(111);
        GrandChildModel gc1 = new GrandChildModel();
        gc1.setGc_name("gc name 1");
        child1.getGrandChildList().add(gc1);
        theModel.getChildList().add(child1);
        ChildModel child2 = new ChildModel();
        child2.setChild_age(222);
        GrandChildModel gc2 = new GrandChildModel();
        child2.getGrandChildList().add(gc2);
        gc2.setGc_name("gc name 2");
        theModel.getChildList().add(child2);
        return "viewGenerated";
    }
    
//    private User theModel = null;
//    public User getTheModel() {
//        return theModel;
//    }
//    public void setTheModel(User theModel) {
//        this.theModel = theModel;
//    }
//    public String viewGenerated() {
//        theModel = new User();
//        GroupUser child1 = new GroupUser();
//        child1.setUs_id("US111");
//        child1.setUg_id("UG111");
//        theModel.setGroupUserList(new ArrayList());
//        theModel.getGroupUserList().add(child1);
//        child1 = new GroupUser();
//        child1.setUs_id("US222");
//        child1.setUg_id("UG333");
//        UserPreferenceModel userPref = new UserPreferenceModel();
//        userPref.setPref_code("pref code 1");
//        theModel.setUserPreferenceList(new ArrayList());
//        theModel.getUserPreferenceList().add(userPref);
//        userPref = new UserPreferenceModel();
//        userPref.setPref_code("pref code 2");
//        theModel.getUserPreferenceList().add(userPref);
//        return "viewGenerated";
//    }
    
    private String entryParent;
    public String getEntryParent() {
        return entryParent;
    }
    public void setEntryParent(String entryParent) {
        this.entryParent = entryParent;
    }

    private String entryParentLvl;
    public String getEntryParentLvl() {
        return entryParentLvl;
    }
    public void setEntryParentLvl(String entryParentLvl) {
        this.entryParentLvl = entryParentLvl;
    }
    
    
//    public String processAddItem() {
//        Debug.printFrameworkDebug("processAddItem: entryParent=" + entryParent + ", entryParentLvl="+entryParentLvl);
//        if (entryParent.equals("0") && Validator.isEmpty(entryParentLvl)) {
//            GroupUser newChild = new GroupUser();
//            newChild.setUs_id("US " + (theModel.getGroupUserList().size()+1) );
//            theModel.getGroupUserList().add(newChild);
//        } else if(entryParent.equals("1")) { //add item for 0
//            UserPreferenceModel userPref = new UserPreferenceModel();
//            userPref.setPref_code("Pref_code " + (theModel.getUserPreferenceList().size()+1) );
//            theModel.getUserPreferenceList().add(userPref);
//        }
//        return "viewGenerated";
//    }
    public String processAddItem() {
        for (ChildModel child : theModel.getChildList()) {
            Debug.printFrameworkDebug("child.get_markedAsDel() = " + child.get_markedAsDel());
        }
        Debug.printFrameworkDebug("processAddItem: entryParent=" + entryParent + ", entryParentLvl="+entryParentLvl);
        if (entryParent.equals("0") && Validator.isEmpty(entryParentLvl)) {
            Debug.printFrameworkDebug("bbb");
            ChildModel newChild = new ChildModel();
            theModel.getChildList().add(newChild);
        } else if(entryParent.equals("0")) { //add item for 0
            Debug.printFrameworkDebug("ccc");
            GrandChildModel grandChildModel = new GrandChildModel();
            Debug.printFrameworkDebug("_markedAsDel = " + theModel.getChildList().get(Integer.parseInt(entryParentLvl)).get_markedAsDel());
            theModel.getChildList().get(Integer.parseInt(entryParentLvl)).getGrandChildList().add(grandChildModel);
        }
        return "viewGenerated";
    }
//    public String processAddItem_0() {
//        ChildModel newChild = new ChildModel();
//        theModel.getChildList().add(newChild);
//        return "viewGenerated";
//    }
//    
//    public String processAddItem_0_0() {
//        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//        GrandChildModel newGrandChildModel = new GrandChildModel();
//        theModel.getChildList().get(Integer.parseInt(request.getParameter("lvl2Idx"))).getGrandChildList().add(newGrandChildModel);
//        return "viewGenerated";
//    }
    
//    public String processAddItem_0_0_0() {
//        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//        GrandChildModel newGrandChildModel = new GrandChildModel();
//        theModel.getChildList().get(Integer.parseInt(request.getParameter("lvl2Idx"))).getGrandChildList().add(newGrandChildModel);
//        return "viewGenerated";
//    }
    //** Generate Entry Page from Model : END **//
    //** Generate Audit Query Page from Model : START **//
    public String auditLog() {
        return "auditLog";
    }
    
    private List columnList = null;
    public List getColumnList() {
        return columnList;
    }
    public void setColumnList(List columnList) {
        this.columnList = columnList;
    }
    
    public String modelSelected() {
        if (columnList == null) columnList = new ArrayList();
        List<Options> oneToManyList = new ArrayList();
        Class modelClass = getModelClass();
        String html = "";
        Integer idx = -1;
        for(Method method : modelClass.getDeclaredMethods()){
            if (!method.getName().startsWith("get")) continue;
            Annotation[] annotations = method.getAnnotations();
            Boolean isPK = Boolean.FALSE;
            String name = null;
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i].annotationType().equals(Id.class)) {
                    isPK = Boolean.TRUE;
                }
                if (annotations[i].annotationType().equals(Column.class)) {
                    name = ((Column)annotations[i]).name();
                }
                if (annotations[i].annotationType().equals(OneToMany.class)) {
                    oneToManyList.add(new Options(parentModelName+"."+removeGet(method.getName()), ((OneToMany)annotations[i]).targetEntity().getSimpleName()));
                }
            }
            if (name != null) {
                idx++;
                String labelName = name;
                labelName= WordUtils.capitalize(labelName.replace("_", " "));
                if (labelName.endsWith(" Id")) {
                    labelName = labelName.substring(0, labelName.length()-2) + "ID";
                }
                if (labelName.endsWith(" Dob")) {
                    labelName = labelName.substring(0, labelName.length()-3) + "DOB";
                }
                if (labelName.contains(" Dob ")) {
                    labelName = labelName.replaceAll(" Dob ", " DOB ");
                }
                if (labelName.contains(" Id ")) {
                    labelName = labelName.replaceAll(" Id ", " ID ");
                }
                System.out.println("=====================================================================labelName = " + labelName);
                if (name.equalsIgnoreCase("created_date") || name.equalsIgnoreCase("created_by") ||
                    name.equalsIgnoreCase("updated_date") || name.equalsIgnoreCase("updated_by")) {
                    continue; //do not generate the input fields for these columns
                }
                if (method.getGenericReturnType().toString().equals("class java.lang.String")) {
                } else if (method.getGenericReturnType().toString().equals("class java.lang.Double")) {
                    name += "_str";
                } else if (method.getGenericReturnType().toString().equals("class java.lang.Long")) {
                    name += "_str";
                } else if (method.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                    name += "_str";
                } else if (method.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
                    name += "_str";
                }
                if (isPK) {
//                    html =  "<div class='form-horizontal form-group'>\n"+
//                            "   <label class='col-md-4 control-label'>"+ WordUtils.capitalize(labelName.replace("_", " ")).replace(" Id", " ID") +"</label>\n"+
//                            "   <div class='col-md-5'>\n"+
//                            "      <input type='text' id='"+name+"' class='form-control' name='"+name+"' value='' />\n"+
//                            "   </div>\n"+
//                            "</div>\n" + html;
                    html =  "<tr id=\"row-"+(idx+1)+"\" class=\"templateFieldRow\"><td class=\"align-middle white-space-nowrap\">"+(idx+1)+"</td>\n"+
                            "<td class=\"align-middle white-space-nowrap\">\n"+
                            "    <div class=\"form-check\">\n"+
                            "        <input class=\"cbShowMe form-check-input\" name=\""+name+"_showMe\" type=\"checkbox\" id=\"_"+idx+"\" onclick=\"toggleSelectAll('cbShowMe')\"/>\n"+
                            "        <label for=\"_"+idx+"\" class=\"form-check-label\"></label>\n"+
                            "    </div>\n"+
                            "</td>\n"+
                            "<td class=\"align-middle white-space-nowrap\">"+labelName+"</td>\n"+
                            "<td class=\"align-middle white-space-nowrap\">\n"+
                            "   <input type='text' id='"+name+"' class='form-control form-control-sm' name='"+name+"' value='' />\n"+
                            "   <input type='hidden' id='order row-"+(idx+1)+"' class='form-control' name='"+name+"_fld_order' value='' />\n"+
                            "</td></tr>\n" + html;
                } else {
//                    html +=  "<div class='form-horizontal form-group'>\n"+
//                            "   <label class='col-md-4 control-label'>"+ WordUtils.capitalize(labelName.replace("_", " ")).replace(" Id", " ID") +"</label>\n"+
//                            "   <div class='col-md-5'>\n"+
//                            "      <input type='text' id='"+name+"' class='form-control' name='"+name+"' value='' />\n"+
//                            "   </div>\n"+
//                            "</div>";
                    html += "<tr id=\"row-"+(idx+1)+"\" class=\"templateFieldRow\"><td class=\"align-middle white-space-nowrap\">"+(idx+1)+"</td>\n"+
                            "<td class=\"align-middle white-space-nowrap\">\n"+
                            "    <div class=\"form-check\">\n"+
                            "        <input class=\"cbShowMe form-check-input\" name=\""+name+"_showMe\" type=\"checkbox\" id=\"_"+idx+"\" onclick=\"toggleSelectAll('cbShowMe')\"/>\n"+
                            "        <label for=\"_"+idx+"\" class=\"form-check-label\"></label>\n"+
                            "    </div>\n"+
                            "</td>\n"+
                            "<td class=\"align-middle white-space-nowrap\">"+labelName+"</td>\n"+
                            "<td class=\"align-middle white-space-nowrap\">\n"+
                            "   <input type='text' id='"+name+"' class='form-control form-control-sm' name='"+name+"' value='' />\n"+
                            "   <input type='hidden' id='order row-"+(idx+1)+"' class='form-control' name='"+name+"_fld_order' value='' />\n"+
                            "</td></tr>";
                }
            }
        }
        
        try {
            HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            response.setContentType("text/html; charset=UTF-8");
            
            /*
            
            <div class="table-responsive">
                            <table id="templateFieldTable" class="table table-sds table-condensed table-striped table-hover" width="100%">
                            <thead>
                                <tr>
                                    <th><s:text name="common.no"/></th>
                                    <th><s:text name="AuditLog.showMe"/><br>
                                        <div class="checkbox check-success">
                                            <input type="checkbox"  id="cbselect" class="selectAll" name="cbselect" onClick="toggleCheckboxByClassName(this,'cbShowMe');">
                                            <label for="cbselect"></label>
                                        </div>
                                    </th>
                                    <th><s:text name="AuditLog.fieldName"/></th>
                                    <th><s:text name="AuditLog.fieldFilter"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <s:iterator value="model.templateFieldList" var="field" status="fieldStatus">
                                    <tr id="row-idx+1}" class="templateFieldRow">
                                        <td>idx+1</td>
                                        <td>
                                            <div class="checkbox check-success">
                                                <input class="cbShowMe" type="checkbox" <s:if test="#field.fld_showMe_boo">checked</s:if> value="Y" name="model.templateFieldList[idx}].fld_showMe" id="_idx}" onclick="toggleSelectAll('cbShowMe')"/>
                                                <label for="_idx}"></label>
                                            </div>
                                    </div>
                                        </td>
                                        <td><s:textfield readonly="true" value="%{#field.fld_col}" name="model.templateFieldList[%{#fieldStatus.index}].fld_col" cssClass="form-control"/></td>
                                        <td>
                                            <s:hidden id="order_row-%{#fieldStatus.index+1}" value="%{#field.fld_order}" name="model.templateFieldList[%{#fieldStatus.index}].fld_order" cssClass="form-control"/>
                                            <s:hidden value="%{#field.ID}" name="model.templateFieldList[%{#fieldStatus.index}].ID"/>
                                            <s:textfield value="%{#field.fld_lbl}" name="model.templateFieldList[%{#fieldStatus.index}].fld_lbl" cssClass="form-control"/>
                                        </td>
                                    </tr>
                                </s:iterator>
                            </tbody>
                            </table>
                        </div> 
            
            */
            html = "<div class=\"table-responsive mt-3\">\n"+
                   "   <table id=\"templateFieldTable\" class=\"table table-sds table-sm fs--1 table-striped table-hover\" width=\"100%\">\n"+
                   "      <thead class=\"bg-200 text-900\">\n"+
                   "         <tr>\n"+
                   "            <th class=\"align-middle white-space-nowrap sort\">"+getText("common.no")+"</th>\n"+
                   "            <th class=\"align-middle white-space-nowrap no-sort\">"+getText("AuditLog.showMe")+"<br>\n"+
                   "               <div class=\"form-check fs-0 d-flex align-items-center\">\n"+
                   "                  <input type=\"checkbox\" id=\"cbselect\" class=\"selectAll form-check-input\" name=\"cbselect\" onClick=\"toggleCheckboxByClassName(this,'cbShowMe');\">\n"+
                   "                  <label for=\"cbselect\" class=\"form-check-label\"></label>\n"+
                   "               </div>\n"+
                   "            </th>\n"+
                   "            <th class=\"align-middle white-space-nowrap sort\">"+getText("AuditLog.fieldName")+"</th>\n"+
                   "            <th class=\"align-middle white-space-nowrap no-sort\">"+getText("AuditLog.fieldFilter")+"</th>\n"+
                   "         </tr>\n"+
                   "      </thead>\n"+
                   "      <tbody>\n" + html + "\n"+
                   "      </tbody>\n"+
                   "   </table>\n"+
                   "</div>" ;
            response.getWriter().append(html+
                    "<div class='row'>\n" +
"                        <div class=\"col text-center\">\n" +
"                            <button type='button' class='btn btn-primary btn-sm' name='auditReportEntry' id='actionName' onclick='auditReport(); return false;' >\n" +
"                                <i class='fa fa-plus'></i><span class=\"ms-1\">Generate Now</span>\n" +
"                            </button>\n" +
"                        </div>\n" +
"                    </div>");
//            response.getWriter().append("<div class='row'><hr></div>"+html+"<br>"+
//            "<div class='form-horizontal form-group'>\n"+
//            "   <label class='col-md-4 control-label'>Action</label>\n"+
//            "      <div class='col-md-5 control-label'>\n"+
//            "         <button type='button' class='btn btn-primary' name='auditReportEntry' id='actionName' onclick='auditReport(); return false;' >\n"+
//            "      <i class='fa fa-plus'></i>Generate Now\n"+
//            "         </button>\n"+
//            "   </div>\n"+
//            "</div>");
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (!oneToManyList.isEmpty()) {
//        }
        return null;
    }
    
    private List<AuditTrailModel> auditList = null;
    public List<AuditTrailModel> getAuditList() {
        return auditList;
    }
    public void setAuditList(List<AuditTrailModel> auditList) {
        this.auditList = auditList;
    }
    
    private Map<String, List> auditTrailMap = null;
    public Map<String, List> getAuditTrailMap() {
        return auditTrailMap;
    }
    public void setAuditTrailMap(Map<String, List> auditTrailMap) {
        this.auditTrailMap = auditTrailMap;
    }
    
    
    private List<String> displayColList = null;
    public List<String> getDisplayColList() {
        if (displayColList == null) {
            displayColList = new ArrayList();
        }
        return displayColList;
    }
    public void setDisplayColList(List<String> displayColList) {
        this.displayColList = displayColList;
    }
    
    public List<String> getSortedDisplayColList() {
        Collections.sort(getDisplayColList());
        return getDisplayColList();
    }
    
    private String logDateFrom;
    public String getLogDateFrom() {
        return logDateFrom;
    }
    public void setLogDateFrom(String logDateFrom) {
        this.logDateFrom = logDateFrom;
    }

    private String logDateTo;
    public String getLogDateTo() {
        return logDateTo;
    }
    public void setLogDateTo(String logDateTo) {
        this.logDateTo = logDateTo;
    }
    
    private String logUserId;
    public String getLogUserId() {
        return logUserId;
    }
    public void setLogUserId(String logUserId) {
        this.logUserId = logUserId;
    }

    private String logRecordId;
    public String getLogRecordId() {
        return logRecordId;
    }
    public void setLogRecordId(String logRecordId) {
        this.logRecordId = logRecordId;
    }
    
    public String auditReport() {
        auditTrailMap = new TreeMap();
        try {
            globalRequest = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            Class modelClass = getModelClass();
            String html = "";
            List<Options> oneToManyList = new ArrayList();
            Table table = (Table)modelClass.getAnnotation(Table.class);
//            auditList = baseDAO.getSession().getNamedQuery("AuditTrailModel.findBy_tableName").setParameter("tableName", table.name()).list();
            String stmt = "select _self from AuditTrailModel _self where table_name = :tableName ";
            CriteriaConverter cc = new CriteriaConverter(Boolean.TRUE);
            if (Validator.notEmpty(logUserId)) {
                stmt += cc.strCriteria("and", "user_id", logUserId.toLowerCase());
            }
            if (Validator.notEmpty(logRecordId)) {
                stmt += cc.strCriteria("and", "record_id", logRecordId.toLowerCase());
            }
            if (Validator.notEmpty(logDateFrom)) {
                String dateFormat = getText("date_default_date_dr");
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(DateUtil.getDate(logDateFrom, dateFormat));
                } catch (Exception e) {
                    try {
                        dateFormat = "dd-MM-yyyy";
                        cal.setTime(DateUtil.getDate(logDateFrom, dateFormat));
                    } catch (Exception e2) {
                        dateFormat = "dd.MM.yyyy";
                        cal.setTime(DateUtil.getDate(logDateFrom, dateFormat));
                    }
                }
                stmt += "and date_time >= :dateFrom ";
                cc.criteriaMap.put("dateFrom", cal.getTime());
            }
            if (Validator.notEmpty(logDateTo)) {
                String dateFormat = getText("date_default_date_dr");
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(DateUtil.getDate(logDateTo, dateFormat));
                } catch (Exception e) {
                    try {
                        dateFormat = "dd-MM-yyyy";
                        cal.setTime(DateUtil.getDate(logDateTo, dateFormat));
                    } catch (Exception e2) {
                        dateFormat = "dd.MM.yyyy";
                        cal.setTime(DateUtil.getDate(logDateTo, dateFormat));
                    }
                }
                cal.add(Calendar.DAY_OF_MONTH, 1);
                stmt += "and date_time < :dateTo ";
                cc.criteriaMap.put("dateTo", cal.getTime());
            }
            
            Query query = baseDAO.getSession().createQuery(stmt);
            for (String key : cc.criteriaMap.keySet()) {
                if (cc.criteriaMap.get(key)!=null && cc.criteriaMap.get(key).getClass().isArray()) {
                    query.setParameterList(key, (Object[])cc.criteriaMap.get(key));
                } else {
                    query.setParameter(key, cc.criteriaMap.get(key));
                }
            }
            query.setParameter("tableName", table.name());
            auditList = query.list();
            System.out.println("auditList.size = " + auditList.size());

            List list = null;
            Boolean addedDisplayColumn = Boolean.FALSE;
            for (AuditTrailModel auditTrail : auditList) {
                if (auditTrailMap.containsKey(auditTrail.getRecord_id()))  {
                    list = auditTrailMap.get(auditTrail.getRecord_id());
                } else {
                    list = new ArrayList();
                    auditTrailMap.put(auditTrail.getRecord_id(), list);
                }
                list.add(auditTrail);
                for(Method method : modelClass.getDeclaredMethods()){
                    if (!method.getName().startsWith("get")) continue;
                    Annotation[] annotations = method.getAnnotations();
                    Boolean isPK = Boolean.FALSE;
                    String name = null;
                    for (int i = 0; i < annotations.length; i++) {
                        if (annotations[i].annotationType().equals(Id.class)) {
                            isPK = Boolean.TRUE;
                        }
                        if (annotations[i].annotationType().equals(Column.class)) {
                            name = ((Column)annotations[i]).name();
                        }
                        if (annotations[i].annotationType().equals(OneToMany.class)) {
                            oneToManyList.add(new Options(parentModelName+"."+removeGet(method.getName()), ((OneToMany)annotations[i]).targetEntity().getSimpleName()));
                        }
                    }
                    if (name != null) {
                        if (name.equalsIgnoreCase("created_date") || name.equalsIgnoreCase("created_by") ||
                            name.equalsIgnoreCase("updated_date") || name.equalsIgnoreCase("updated_by")) {
                            continue; //do not generate the input fields for these columns
                        }
                        if (method.getGenericReturnType().toString().equals("class java.lang.String")) {
                        } else if (method.getGenericReturnType().toString().equals("class java.lang.Double")) {
                            name += "_str";
                        } else if (method.getGenericReturnType().toString().equals("class java.lang.Long")) {
                            name += "_str";
                        } else if (method.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                            name += "_str";
                        } else if (method.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
                            name += "_str";
                        }
                        if (Validator.notEmpty(globalRequest.getParameter(name+"_showMe"))) {//user selected to show this column
                            if (!addedDisplayColumn) {
                                getDisplayColList().add(name);
                            }
                            if (Validator.notEmpty(globalRequest.getParameter(name))) {//with filtering criteria
                                for (String criteria : globalRequest.getParameter(name).split(",")) {
                                    criteria = criteria.trim();
                                    if (criteria.contains("%")) {
                                        Pattern pattern = Pattern.compile(criteria.toLowerCase().replaceAll("%", ".*"), Pattern.DOTALL);
                                        if (pattern.matcher( ((String)auditTrail.getNewData(name)).toLowerCase()).matches()) {
                                            auditTrail.setMatchCriteria(Boolean.TRUE);
                                            auditTrail.newDataMap().put(name+"__matched", Boolean.TRUE);
                                        }
                                    } else if (criteria.startsWith(">=") || criteria.startsWith(">") || criteria.startsWith("<=") || criteria.startsWith("<")) {
                                        Boolean found = null;
                                        if (criteria.startsWith(">=")) {
                                            found = (criteria.substring(2).trim().compareToIgnoreCase((String)auditTrail.getNewData(name))>=0);
                                        } else if (criteria.startsWith(">")) {
                                            found = (criteria.substring(1).trim().compareToIgnoreCase((String)auditTrail.getNewData(name))>0);
                                        } else if (criteria.startsWith("<=")) {
                                            found = (criteria.substring(2).trim().compareToIgnoreCase((String)auditTrail.getNewData(name))<=0);
                                        } else {
                                            found = (criteria.substring(1).trim().compareToIgnoreCase((String)auditTrail.getNewData(name))<0);
                                        }
                                        if (found) {
                                            auditTrail.setMatchCriteria(Boolean.TRUE);
                                            auditTrail.newDataMap().put(name+"__matched", Boolean.TRUE);
                                        }
                                    } else {//equals
                                        if (criteria.trim().equalsIgnoreCase((String)auditTrail.getNewData(name))) {
                                            auditTrail.setMatchCriteria(Boolean.TRUE);
                                            auditTrail.newDataMap().put(name+"__matched", Boolean.TRUE);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                addedDisplayColumn = Boolean.TRUE;
            }
            for (String key : auditTrailMap.keySet()) {
                AuditTrailModel currentRecordModel = new AuditTrailModel();
                AuditTrailModel auditTrail = (AuditTrailModel)auditTrailMap.get(key).get(0);
                currentRecordModel.setRecord_id(auditTrail.getRecord_id());
                ModelBase currentModelInstance = null;
                try {
                    currentModelInstance = (ModelBase)baseDAO.getModelById(auditTrail.getRecord_id(), modelClass);
                } catch (Exception e) {
                    currentModelInstance = (ModelBase)baseDAO.getModelById(new Integer(auditTrail.getRecord_id()), modelClass);
                }
                Map map = new HashMap();
                for (String name : getDisplayColList()) {
                    map.put(name, getMethodValueFromObject(currentModelInstance, "get"+name.substring(0, 1).toUpperCase() + name.substring(1)));
                }
                currentRecordModel.setUser_id((String)getMethodValueFromObject(currentModelInstance, "getUpdated_by"));
                currentRecordModel.setNewDataMap(map);
                auditTrailMap.get(key).add(0, currentRecordModel);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "auditReport";
    }
    
    private List sampleList = null;
    public List getSampleList() {
        if (sampleList == null) {
            sampleList = new ArrayList();
            sampleList.add(new Options("1", "Data 1"));
            sampleList.add(new Options("2", "Data 2"));
            sampleList.add(new Options("3", "Data 3"));
            sampleList.add(new Options("4", "Data 4"));
        }
        return sampleList;
    }
    //** Generate Audit Query Page from Model : END **//
    private Object model=null;
    public Object getModel() {
        return model;
    }
    
    
    public void uuSetup() throws Exception {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String colName = request.getParameter("uuColumn");
        if (colName.endsWith("_str_")) {
            colName = colName.substring(0, colName.length()-5);
        }
        if (colName.endsWith("_")) {
            colName = colName.substring(0, colName.length()-1);
        }
        if (colName.contains(".")) {
            colName = colName.substring(colName.lastIndexOf(".")+1);
        }
        String getDrDocModel_name = null;
        String foundUploadCode = null;
        for (Class regClass : SessionFactoryImpl.registeredClass) {
            if (regClass.getSimpleName().equals(request.getParameter("uuModelName"))) {
                System.out.println("regClass.getSimpleName() = " + regClass.getSimpleName());
                ModelBase modelBase;
                try {
                    Class c = Class.forName(regClass.getPackage().getName()+"."+regClass.getSimpleName()+"Service");
                    modelBase = (ModelBase) c.getDeclaredConstructor(ModelBase.class, ModelBase.class).newInstance(regClass.newInstance(), null);
                    for (String appCodeSetupKey : modelBase.getUppyUpload_appCodeSetup().keySet()) {
                        System.out.println("appCodeSetupKey = " + appCodeSetupKey);
                        System.out.println("modelBase.getUppyUpload_appCodeSetup().get(appCodeSetupKey).split(\";\")[0] = " + modelBase.getUppyUpload_appCodeSetup().get(appCodeSetupKey).split(";")[0]);
                        if (modelBase.getUppyUpload_appCodeSetup().get(appCodeSetupKey).split(";")[0].equals(colName)) {
                            foundUploadCode = appCodeSetupKey;
                            break;
                        }
                    }
                } catch (Exception e) {
                    
                }
//                modelBase = (ModelBase)regClass.newInstance();
                Boolean found = Boolean.FALSE;
                    System.out.println("colName = " + colName);
                for(Method method : regClass.getDeclaredMethods()){
                    System.out.println("method.getName() = " + method.getName());
                    Annotation[] annotations = method.getAnnotations();
                    for (int i = 0; i < annotations.length; i++) {
                        if (annotations[i].annotationType().equals(JoinColumn.class)) {
                            System.out.println("((JoinColumn)annotations[i]).name() = " + ((JoinColumn)annotations[i]).name());
                            if (((JoinColumn)annotations[i]).name().equals(colName)) {
                                getDrDocModel_name = method.getName();
                                found = Boolean.TRUE;
                                break;
                            }
                        }
                    }
                    if (found) {
                        break;
                    }
                }
                break;
            }
        }
        response.setContentType("text/html; charset=UTF-8");
        if (getDrDocModel_name == null) {
            response.getWriter().append("Please setup do getUppyUpload_appCodeSetup().put(\""+colName+"_file"+"\", \""+colName+";"+colName+"_file_actualFolder\")");
        } else {
            response.getWriter().append(getDrDocModel_name+";"+foundUploadCode);
        }
        response.flushBuffer();
    }
}
