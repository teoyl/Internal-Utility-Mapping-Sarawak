package com.sains.framework.base.web;

import com.sains.common.util.Formatter;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.LogFunction;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.sains.common.util.Validator;
import com.sains.common.util.DateUtil;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.lookup.DataRetriever;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.DynamicDelete;
import com.sains.framework.base.DynamicPreSearch;
import java.net.URLEncoder;
import java.util.Arrays;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.WordUtils;

public class DynamicAction extends BaseActionSupport {

    private static final long serialVersionUID = -6659925652584240539L;
    private final String LOAD = "load";
    private final String UPDATE = "update";
    private final String INSERT = "insert";
    public final String DEFAULT_DATE_POPUP_FORMAT_java = getText("date_default_date_dr"); //default date used in JAVA, use date_default_date_popup if at jsp
    private final String DEFAULT_DATE_POPUP_FORMAT_jsp = getText("date_default_date_popup"); //default date used in JAVA, use date_default_date_popup if at jsp
    private Map<String, Object> searchParam = new HashMap();
    private Map<String, String> rightsList = new HashMap();
    Map<String, String> modelColumnLengthMap = null;
    /**
     * Variables for Dynamic Add / Edit
     */
    private List<String> pageLabels = new ArrayList();
    private List<String> pageLabelsDesc = new ArrayList();
    private List<String> pageFields = new ArrayList();
    private Map<String, String> pageFieldsData = new HashMap();
    private Map pageFieldDD = new HashMap();
    private Map pageFieldJavaScript = new HashMap();
    private Map pageFieldColName = new HashMap();
    private String pageJavascript = null;
    private String pageLoadedJavascript = null;
    private String localValidateFormJavascript = null;
    private String pageTitle = null;
    private Map configMap = null;
    //private BaseDAO objDAO = new BaseDAOImpl();
    private Object modelObject = null;
    private String addPageRequired = "";
    private String editPageRequired = "";
    private Boolean popupCalanderUsed = false;
    private Boolean loadDescsUsed = false;
    private String labelWidth = "150";
    private Boolean manualParam = Boolean.FALSE;
    
    private Map<String, String> manualParamMap = null;
    public Map getManualParamMap() {
        return manualParamMap;
    }
    public void setManualParamMap(Map manualParamMap) {
        this.manualParamMap = manualParamMap;
    }
    
    public Boolean getManualParam() {
        return manualParam;
    }
    
    public DynamicAction(BaseDAO dao, Boolean manualParam) {
        this.manualParam = manualParam;
        baseDAO = dao;
    }
    public DynamicAction() {
//        baseDAO = new BaseDAOImpl();
    }

    public String setup() {

//        Debug.printFrameworkDebug("DynamicAction : setup().");

        super.populateDynamicActionSetup();
//        Debug.printFrameworkDebug("searchFieldsMap = " + getSearchFieldsMap());
        return SUCCESS + (getSystemType_().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)?"":"_"+getSystemType_());
    }

    @Override
    public void specificValidation(String validationType) throws Exception{
        String required = null;
        String requiredWhen = null;
        String noDuplicate = null;
        //BaseDAO checkingDAO = new BaseDAOImpl();
        //addPageRequiredWhen

        try {
            if (validationType.equals(UPDATE)) {
                required = (String) configMap.get("editPageRequired");
                if (required == null || required.equalsIgnoreCase("asAddPage")) {
                    required = (String) configMap.get("addPageRequired");
                }

                requiredWhen = (String) configMap.get("editPageRequiredWhen");
                if (requiredWhen != null && requiredWhen.equalsIgnoreCase("asAddPage")) {
                    requiredWhen = (String) configMap.get("addPageRequiredWhen");
                }

                noDuplicate = (String) configMap.get("editPageNoDuplicate");
                if (noDuplicate == null || noDuplicate.equalsIgnoreCase("asAddPage")) {
                    noDuplicate = (String) configMap.get("addPageNoDuplicate");
                }

                String recursiveCheck = (String) configMap.get("recursiveCheck");
                if (!Validator.isEmpty(recursiveCheck)) {
                    if (recursiveCheck != null) {
                        String[] recursiveCheckArr = recursiveCheck.split(",");
                        if (recursiveCheckArr[0].indexOf(":") > 0) { //only do checking base on specific value.
                            String startCheckValue = (String)getMethodValueFromObject(modelObject, "get" + WordUtils.capitalize(recursiveCheckArr[0].split(":")[0].trim()));
                            if (startCheckValue.equalsIgnoreCase(recursiveCheckArr[0].split(":")[1].trim())) {
                                if (baseDAO.isRecursive(recursiveCheckArr[2].trim(), recursiveCheckArr[1].trim(), modelObject)) {
                                    addActionError(getText("errors.recursiveRelation", new String[]{getText(recursiveCheckArr[3].trim())}));
                                }
                            }
                        } else { //do checking if no checking value is provided.
                            if (baseDAO.isRecursive(recursiveCheckArr[2].trim(), recursiveCheckArr[1].trim(), modelObject)) {
                                addActionError(getText("errors.recursiveRelation", new String[]{getText(recursiveCheckArr[3].trim())}));
                            }
                        }
                    }
                }
                //
            } else { // is insert
                required = (String) configMap.get("addPageRequired");
                requiredWhen = (String) configMap.get("addPageRequiredWhen");
                noDuplicate = (String) configMap.get("addPageNoDuplicate");
            }

            if (requiredWhen != null) {
                StringTokenizer moreReqWhen = new StringTokenizer(requiredWhen, new String("|"));
                while (moreReqWhen.hasMoreTokens()) {
                    //parent_module_id;parent.module reqWhen1
                    //module_type;module.type;module.type.S;S reqWhen2
                    String[] reqWhen = moreReqWhen.nextToken().split(",");
                    String[] reqWhen1 = reqWhen[0].split(";");
                    String[] reqWhen2 = reqWhen[1].split(";");

                    String data = pageFieldsData.get(reqWhen2[0].trim());
                    if (data != null && (reqWhen2[3].equalsIgnoreCase("NotEmpty") || data.equalsIgnoreCase(reqWhen2[3]))) {
                        if (Validator.isEmpty(pageFieldsData.get(reqWhen1[0].trim()))) {
                            addActionError(getText("field.required.when", new String[]{getText(reqWhen1[1]), getText(reqWhen2[1]), getText(reqWhen2[2])}));
                        }
                    }
                }
            }

            if (required != null) {
                for (String req : required.split(",")) {
                    String[] reqArr = req.split(";");
                    String obj = pageFieldsData.get(reqArr[0].trim());
                    if (Validator.isEmpty(obj)) {
                        addActionError(getText("field.required", new String[]{getText(reqArr[1].trim())}));
                    }
                }
            }

            if (noDuplicate != null) {
                for (String dupCol : noDuplicate.split(",")) {
                    String[] dupArr = dupCol.split(";");
                    String obj = pageFieldsData.get(dupArr[0].trim());
                    if (baseDAO.isDuplicate(dupArr[0].trim(), modelObject)) {
                        addActionError(getText("field.duplicated", new String[]{getText(dupArr[1].trim()), obj}));
                    }
                }
            }
        } finally {
            //checkingDAO.closeSession();
            closeSession();
        }
    }

    public String processInsert() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try {
            if (configMap == null) {
                configMap = new CommonFunction().getDynamicEntryConfiguration(request.getParameter("action") + "_dynamic", request, request.getSession().getServletContext(), "");
                configMap = checkDynamicEntryConfig(configMap, request.getParameter("action")+"_dynamic", request, request.getSession().getServletContext(), "");
            }
            StringTokenizer pageColumnsTokenizer = new StringTokenizer(configMap.get("addPageColumns").toString(), ",");
            String column = null;
            while (pageColumnsTokenizer.hasMoreTokens()) {
                column = pageColumnsTokenizer.nextToken().trim();
                StringTokenizer moreColumns = new StringTokenizer(column, new String("|"));
                while (moreColumns.hasMoreTokens()) {
                    String columnName = moreColumns.nextToken();
                    if (columnName.toLowerCase().indexOf(" as ") > 0) {
                        columnName = columnName.substring(columnName.toLowerCase().indexOf(" as ") + 4);
                    }
                    pageFieldsData.put(columnName, request.getParameter(columnName));
                }
            }
            pageFieldsData.put("objId", request.getParameter("objId"));
            Class c = Class.forName((String) configMap.get("modelName"));
            modelObject = c.newInstance();
            Method m = modelObject.getClass().getMethod("setID", String.class);
            m.invoke(modelObject, pageFieldsData.get("objId"));

            Object obj, dataObj = null;
            Class objClass = null;
            for (String col : pageFieldsData.keySet()) {
//                try {
//                    m = modelObject.getClass().getMethod("set" + WordUtils.capitalize(col), String.class);
//                    m.invoke(modelObject, pageFieldsData.get(col));
//                    if (m.getGenericReturnType().toString().equals("class java.lang.String")) {
//                        objClass = String.class;
//                        dataObj = pageFieldsData.get(col);
//                    } else if (m.getGenericReturnType().toString().equals("class java.lang.Double")) {
//                        objClass = Double.class;
//                        dataObj = new Double(pageFieldsData.get(col));
//                    } else if (m.getGenericReturnType().toString().equals("class java.lang.Long")) {
//                        objClass = Long.class;
//                        dataObj = new Long(pageFieldsData.get(col));
//                    } else if (m.getGenericReturnType().toString().equals("class java.lang.Integer")) {
//                        objClass = Integer.class;
//                        dataObj = new Integer(pageFieldsData.get(col));
//                    } else if (m.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
//                        objClass = Timestamp.class;
//                        m = modelObject.getClass().getMethod("set" + WordUtils.capitalize(col), Timestamp.class);
//                        dataObj = DateUtil.getTimestampFromDate(DateUtil.getDate(pageFieldsData.get(col), DEFAULT_DATE_POPUP_FORMAT_java));
//                    }
//                }
                try {
                    m = modelObject.getClass().getMethod("get" + WordUtils.capitalize(col));
                    obj = m.invoke(modelObject);
                    if (m.getGenericReturnType().toString().equals("class java.lang.String")) {
                        objClass = String.class;
                        dataObj = pageFieldsData.get(col);
                    } else if (m.getGenericReturnType().toString().equals("class java.lang.Double")) {
                        objClass = Double.class;
                        dataObj = new Double(pageFieldsData.get(col));
                    } else if (m.getGenericReturnType().toString().equals("class java.lang.Long")) {
                        objClass = Long.class;
                        dataObj = new Long(pageFieldsData.get(col));
                    } else if (m.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                        objClass = Integer.class;
                        dataObj = new Integer(pageFieldsData.get(col));
                    } else if (m.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
                        objClass = Timestamp.class;
                        m = modelObject.getClass().getMethod("set" + WordUtils.capitalize(col), Timestamp.class);
                        dataObj = DateUtil.getTimestampFromDate(DateUtil.getDate(pageFieldsData.get(col), DEFAULT_DATE_POPUP_FORMAT_java));
                    }
                    m = modelObject.getClass().getMethod("set" + WordUtils.capitalize(col), objClass);
                    try {
                        m.invoke(modelObject, dataObj);
                    } catch (Exception e) {
                        m.invoke(modelObject, (Object) null);
                    }
                } catch (Exception ex) {
                    //new LogFunction().logError(this.getClass(), "", ex);
                }
            }
            specificValidation(INSERT);
            if (getActionErrors().size() > 0) {
                populateDynamicContent(INSERT);
                return SystemConstants.ACTION_Status.INSERT_FAIL + (getSystemType_().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)?"":"_"+getSystemType_());
            }
            BaseDAOImpl.defaultAddProperties(modelObject);
            baseDAO.insert(modelObject);
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
            returnStr = SystemConstants.ACTION_Status.INSERT_FAIL;
        } finally {
            baseDAO.closeSession();
            //objDAO.closeSession();
        }

        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("createSuccess"));
        } else {
            addActionError(getText("createFail"));
            populateDynamicContent(INSERT);
        }

        return returnStr + (getSystemType_().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)?"":"_"+getSystemType_());
    }

    public String add() {
        return "add" + (getSystemType_().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)?"":"_"+getSystemType_());
    }

    @Override
    public String loadAddPage() {
        populateDynamicContent(INSERT);
        String required = (String) configMap.get("addPageRequired");
        int count = 1;
        for (String req : required.split(",")) {
            String[] reqArr = req.split(";");
            addPageRequired += "this.a" + count++ + " = new Array('" + reqArr[0].trim() + "', '" + getText(reqArr[1]) + "'); " + "\r\n";
        }
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE + (getSystemType_().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)?"":"_"+getSystemType_());
    }

    public String loadEditPage() {
        populateDynamicContent(LOAD);
        String required = (String) configMap.get("editPageRequired");
        if (required == null || required.equalsIgnoreCase("asAddPage")) {
            required = (String) configMap.get("addPageRequired");
        }
        int count = 1;
        for (String req : required.split(",")) {
            String[] reqArr = req.split(";");
            editPageRequired += "this.a" + count++ + " = new Array('" + reqArr[0].trim() + "', '" + getText(reqArr[1]) + "'); " + "\r\n";
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE + (getSystemType_().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)?"":"_"+getSystemType_());
    }

    public String processUpdate() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try {
            if (configMap == null) {
                configMap = new CommonFunction().getDynamicEntryConfiguration(request.getParameter("action") + "_dynamic", request, request.getSession().getServletContext(), "");
                configMap = checkDynamicEntryConfig(configMap, request.getParameter("action")+"_dynamic", request, request.getSession().getServletContext(), "");
            }
            String editpageColumn = (String) configMap.get("editPageColumns");
            if (editpageColumn == null || editpageColumn.equalsIgnoreCase("asAddPage")) {
                editpageColumn = (String) configMap.get("addPageColumns");
            }
            StringTokenizer pageColumnsTokenizer = new StringTokenizer(editpageColumn, ",");
            String column = null;
            while (pageColumnsTokenizer.hasMoreTokens()) {
                column = pageColumnsTokenizer.nextToken().trim();
                StringTokenizer moreColumns = new StringTokenizer(column, new String("|"));
                while (moreColumns.hasMoreTokens()) {
                    String columnName = moreColumns.nextToken();
                    if (columnName.toLowerCase().indexOf(" as ") > 0) {
                        columnName = columnName.substring(columnName.toLowerCase().indexOf(" as ") + 4);
                    }
                    pageFieldsData.put(columnName, request.getParameter(columnName));
                }
            }
            pageFieldsData.put("objId", request.getParameter("objId"));
            Class c = Class.forName((String) configMap.get("modelName"));
            modelObject = c.newInstance();
            Method m = modelObject.getClass().getMethod("setID", String.class);
            m.invoke(modelObject, pageFieldsData.get("objId"));
            Object obj = null, dataObj = null;
            Class objClass = null;
            for (String col : pageFieldsData.keySet()) {
                try {
                    m = modelObject.getClass().getMethod("get" + WordUtils.capitalize(col));
                    obj = m.invoke(modelObject);
                    if (m.getGenericReturnType().toString().equals("class java.lang.String")) {
                        objClass = String.class;
                        dataObj = pageFieldsData.get(col);
                    } else if (m.getGenericReturnType().toString().equals("class java.lang.Double")) {
                        objClass = Double.class;
                        dataObj = new Double(pageFieldsData.get(col));
                    } else if (m.getGenericReturnType().toString().equals("class java.lang.Long")) {
                        objClass = Long.class;
                        dataObj = new Long(pageFieldsData.get(col));
                    } else if (m.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                        objClass = Integer.class;
                        dataObj = new Integer(pageFieldsData.get(col));
                    } else if (m.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
                        objClass = Timestamp.class;
                        m = modelObject.getClass().getMethod("set" + WordUtils.capitalize(col), Timestamp.class);
                        dataObj = DateUtil.getTimestampFromDate(DateUtil.getDate(pageFieldsData.get(col), DEFAULT_DATE_POPUP_FORMAT_java));
                    }
                    m = modelObject.getClass().getMethod("set" + WordUtils.capitalize(col), objClass);
                    try {
                        m.invoke(modelObject, dataObj);
                    } catch (Exception e) {
                        m.invoke(modelObject, (Object) null);
                    }
                } catch (Exception ex) {
                    //new LogFunction().logError(this.getClass(), "", ex);
                }
            }
            specificValidation(UPDATE);
            if (getActionErrors().size() > 0) {
                populateDynamicContent(UPDATE);
                return SystemConstants.ACTION_Status.UPDATE_FAIL + (getSystemType_().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)?"":"_"+getSystemType_());
            }
            BaseDAOImpl.defaultUpdateProperties(modelObject);
            baseDAO.update(modelObject);
            //Debug.printFrameworkDebug("modelObject.address = " + ((BusinessUnitModel)modelObject).getBu_address());
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
            returnStr = SystemConstants.ACTION_Status.UPDATE_FAIL;
        } finally {
            baseDAO.closeSession();
            //objDAO.closeSession();
        }

        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("updateSuccess"));
        } else {
            addActionError(getText("updateFail"));
            populateDynamicContent(UPDATE);
        }

        return returnStr + (getSystemType_().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)?"":"_"+getSystemType_());
    }

    public String delete() {
        boolean methodExists = false;
        Method m = null;
        DynamicDelete dynamicDelete = new DynamicDelete();
        try {
            m = dynamicDelete.getClass().getMethod("delete_" + getAction() + "_dynamic", this.getClass(), String[].class);
            methodExists = true;
        } catch (Exception e) {
        }

        if (methodExists) {
            try {
                return (String) m.invoke(dynamicDelete, this, getSelected()) + (getSystemType_().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)?"":"_"+getSystemType_());
            } catch (Exception e) {
            }
        } else {
            Debug.printInfo("delete method do not exists");
        }
        return SUCCESS + (getSystemType_().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)?"":"_"+getSystemType_());
    }

    public String search() {
//		ModuleDAOImpl moduleDAO = new ModuleDAOImpl();
//		try {
//			Map param = new HashMap();
//			HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//			for (String str : searchParam.keySet()){
//				String paramData = request.getParameter(str);
//				if (!Validator.isEmpty(paramData)){
//					if (searchParam.get(str).getClass().getSimpleName().equals("String")){
//						param.put(str, new String(paramData));
//					} else if (searchParam.get(str).getClass().getSimpleName().equals("Double")){
//						param.put(str, new Double(paramData));
//					} else if (searchParam.get(str).getClass().getSimpleName().equals("Long")){
//						param.put(str, new Long(paramData));
//					} else if (searchParam.get(str).getClass().getSimpleName().equals("Integer")){
//						param.put(str, new Integer(paramData));
//					} else if (searchParam.get(str).getClass().getSimpleName().equals("Date")){
//						try {
//							param.put(str, DateUtil.getDate(paramData));
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							new LogFunction().logError(this.getClass(), "", e);
//						}
//					}
//				}
//			}
//			moduleList = moduleDAO.list(param, module, true);
//		} catch (Exception ex){
//
//		} finally {
//                    moduleDAO.closeSession();
//                    //if (moduleDAO.getSession().isOpen()){
//                    //        moduleDAO.getSession().close();
//                    //}
//		}

        //added by ai min
        String returnStr = null;
//        Map session = ActionContext.getContext().getSession();
//        if (session.get("login_user_type").equals(SystemConstants.SF_SYSTEM_TYPE.PUBLIC)) {
//            returnStr = "search_el";
//        } else if (session.get("login_user_type").equals(SystemConstants.SF_SYSTEM_TYPE.INTERNAL)) {
            returnStr = SystemConstants.ACTION_Status.SEARCH;
//        }

        return returnStr + (getSystemType_().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)?"":"_"+getSystemType_());
    }

    @Override
    public String populateDynamicActionSetup(){
        String returnStr = super.populateDynamicActionSetup();
        Debug.printFrameworkDebug("returnStr " + returnStr);
        int indexOfSearchField;
        if (getSearchFieldsDefaultData().size() > 0){
            String[] searchFieldDefaultDataArr;
            for (String str : (List<String>)getSearchFieldsDefaultData()){
                searchFieldDefaultDataArr = str.split(";");
//                indexOfSearchField = getSearchFields().indexOf(searchFieldDefaultDataArr[0]);
//                getSearchFieldsData().remove(indexOfSearchField);
//                getSearchFieldsData().add(indexOfSearchField, searchFieldDefaultDataArr[1]);

                if (searchFieldDefaultDataArr[1].startsWith("func:")) {
                    getSearchFieldsDataMap().put("search_" + searchFieldDefaultDataArr[0], DynamicPreSearch.getDefaultValue(searchFieldDefaultDataArr[0],searchFieldDefaultDataArr[1], getSearchFieldsDateData()));
                } else {
                    getSearchFieldsDataMap().put("search_" + searchFieldDefaultDataArr[0], searchFieldDefaultDataArr[1]);
                }
//                getSearchFieldsDataMap().put("search_" + getSearchFields().get(indexOfSearchField), searchFieldDefaultDataArr[1]);
            }
        }
        try {
            checkPrePopulateSearchPage(getSetupMap());
        } catch (BaseException be) {
            addActionError(be.getMessage());
        } catch (Exception e) {}
        return returnStr + (getSystemType_().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)?"":"_"+getSystemType_());
    }

    public String search2() {

//        System.out.println("DynamicAction : search2()");
        setSearched(true);
        if (!isGenerateDynamicRpt) setPaging(true);
        
        //String searchCondition = "action="+getAction();
        String searchCondition = "";
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Map param = new HashMap();
        int index = 0;
        try {
            if(request.getParameter("dynamicSortOrder")!=null){
                Validator.validateDynamicSortOrder(request.getParameter("dynamicSortOrder"));
            }
            super.populateDynamicActionSetup();
            if (getUseDataTable()) {
                setPaging(false);
            }
            checkPreSearch(param);
            String[] paramArr = null;
            String paramData = null;
            for (Object str : getSearchFields()) {
//                        System.out.println("str " + str);
                if (((String) str).startsWith("_date_")) {
                    String tempDateData = null;
                    if (((String) str).endsWith("_fromTo")) {
                        str = extractSearchFieldForDate((String) str);
                        tempDateData = request.getParameter("search_" + (String) str + "From");
                        paramData = request.getParameter("search_" + (String) str + "To");
                        if (Validator.isEmpty(tempDateData)) tempDateData = "";
                        if (Validator.isEmpty(paramData)) paramData = "";
                        //keep the searchData : start
                        
//                        Debug.printFrameworkDebug("tempDateData1 = " + tempDateData);

//                        tempDateData = "zfizz'onclick='alert(5 + 6);'autofocus='fdes017/11/2024";
                        paramData = StringEscapeUtils.escapeHtml4(paramData);
                        tempDateData = StringEscapeUtils.escapeHtml4(tempDateData);
//                        System.out.println("tempDateData1 " + tempDateData);
//                        System.out.println("paramData1 " + paramData);
                        
                        getSearchFieldsMap().put("search_" + str + "From", tempDateData);
                        getSearchFieldsDateData().put("search_" + str + "From", tempDateData);
                        getSearchFieldsDataMap().put("search_" + str + "From", tempDateData);
                        getSearchFieldsMap().put("search_" + str + "To", paramData);
                        getSearchFieldsDateData().put("search_" + str + "To", paramData);
                        getSearchFieldsDataMap().put("search_" + str + "To", paramData);
                        if (searchCondition.equals("")) {
                            searchCondition = "search_" + (String) str + "From=" + tempDateData.toString();
                            searchCondition += "&search_" + (String) str + "To=" + paramData.toString();
                        } else {
                            searchCondition += "&search_" + (String) str + "From=" + tempDateData.toString();
                            searchCondition += "&search_" + (String) str + "To=" + paramData.toString();
                        }
                        Debug.printFrameworkDebug("searchCondition = " + searchCondition);
                        //keep the searchData : end
                        date_fromTo_criteria(param, paramData, tempDateData, (String)getSearchFieldsDbName().get(index));
                        Debug.printFrameworkDebug("param = " + param);
                    } else {
                        str = extractSearchFieldForDate((String) str);
                        tempDateData = request.getParameter("search_" + (String) str);
                        if (Validator.isEmpty(tempDateData)) {
                            tempDateData = "";
                        }
                        
                        Debug.printFrameworkDebug("tempDateData2 = " + tempDateData);
                        
                        paramData = StringEscapeUtils.escapeHtml4(paramData);
                        tempDateData = StringEscapeUtils.escapeHtml4(tempDateData);
                        
//                        System.out.println("tempDateData2 " + tempDateData);
//                        System.out.println("paramData2 " + paramData);
                        
                        //keep the searchDateData : start
                        getSearchFieldsMap().put("search_" + str, tempDateData);
                        getSearchFieldsDateData().put("search_" + str, tempDateData);
                        getSearchFieldsDataMap().put("search_" + str, tempDateData);
                        if (searchCondition.equals("")) {
                            searchCondition = "search_" + (String) str + "=" + tempDateData.toString();
                        } else {
                            searchCondition += "&search_" + (String) str + "=" + tempDateData.toString();
                        }
                        //keep the searchDateData : end
                        if (!Validator.isEmpty(tempDateData)) {
                            date_criteria(param, tempDateData, (String)getSearchFieldsDbName().get(index));

                        }
                    }
                    index++;
                    continue;
                }
//                    System.out.println("will run after continue");
                paramData = request.getParameter("search_" + (String) str);
                if (paramData != null) {
                    paramArr = request.getParameterValues("search_" + (String) str);
                } else {
                    paramArr = null;
                }

                if (paramArr != null) {
                    if (paramArr.length > 1) {
                        paramData = null;
                        for (String data : paramArr) {
                            if (paramData == null) {
                                paramData = data;
                            } else {
                                paramData += "," + data;
                            }
                        }
                    }
                }
                if (!Validator.isEmpty(paramData)) {
                    param.put(getSearchFieldsDbName().get(index), new String(paramData));
                    if (searchCondition.equals("")) {
                        searchCondition = "search_" + (String) str + "=" + replaceSpecialChar(paramData.toString());
                    } else {
                        searchCondition += "&search_" + (String) str + "=" + replaceSpecialChar(paramData.toString());
                    }
                }
                getSearchFieldsData().set(index, paramData);
                getSearchFieldsDataMap().put("search_" + getSearchFields().get(index), paramData);

                getSearchFieldsMap().put("search_" + getSearchFields().get(index), paramData);
                index++;
            }
            if (getShowHideMoreField_boo()) {
                index=0;
                for (Object str : getMoreSearchFields()) {
                    if (((String) str).startsWith("date")) {
                        String tempDateData = null;
                        if (((String) str).endsWith("_fromTo")) {
                            str = extractSearchFieldForDate((String) str);
                            tempDateData = request.getParameter("search_" + (String) str + "From");
                            paramData = request.getParameter("search_" + (String) str + "To");
                            if (Validator.isEmpty(tempDateData)) tempDateData = "";
                            if (Validator.isEmpty(paramData)) paramData = "";
                            //keep the searchData : start
                            getSearchFieldsMap().put("search_" + str + "From", tempDateData);
                            getSearchFieldsDateData().put("search_" + str + "From", tempDateData);
                            getSearchFieldsDataMap().put("search_" + str + "From", tempDateData);
                            getSearchFieldsMap().put("search_" + str + "To", paramData);
                            getSearchFieldsDateData().put("search_" + str + "To", paramData);
                            getSearchFieldsDataMap().put("search_" + str + "To", paramData);
                            if (searchCondition.equals("")) {
                                searchCondition = "search_" + (String) str + "From=" + tempDateData.toString();
                                searchCondition += "&search_" + (String) str + "To=" + paramData.toString();
                            } else {
                                searchCondition += "&search_" + (String) str + "From=" + tempDateData.toString();
                                searchCondition += "&search_" + (String) str + "To=" + paramData.toString();
                            }
                            //keep the searchData : end
                            date_fromTo_criteria(param, paramData, tempDateData, (String)getSearchFieldsDbName().get(index));
                        } else {
                            str = extractSearchFieldForDate((String) str);
                            tempDateData = request.getParameter("search_" + (String) str);
                            if (Validator.isEmpty(tempDateData)) {
                                tempDateData = "";
                            }

                            //keep the searchDateData : start
                            getSearchFieldsMap().put("search_" + str, tempDateData);
                            getSearchFieldsDateData().put("search_" + str, tempDateData);
                            getSearchFieldsDataMap().put("search_" + str, tempDateData);
                            if (searchCondition.equals("")) {
                                searchCondition = "search_" + (String) str + "=" + tempDateData.toString();
                            } else {
                                searchCondition += "&search_" + (String) str + "=" + tempDateData.toString();
                            }
                            //keep the searchDateData : end
                            if (!Validator.isEmpty(tempDateData)) {
                                date_criteria(param, tempDateData, (String)getSearchFieldsDbName().get(index));
                            }
                        }
                        index++;
                        continue;
                    }
                    paramData = request.getParameter("search_" + (String) str);
                    if (paramData != null) {
                        paramArr = request.getParameterValues("search_" + (String) str);
                    } else {
                        paramArr = null;
                    }

                    if (paramArr != null) {
                        if (paramArr.length > 1) {
                            paramData = null;
                            for (String data : paramArr) {
                                if (paramData == null) {
                                    paramData = data;
                                } else {
                                    paramData += "," + data;
                                }
                            }
                        }
                    }
                    if (!Validator.isEmpty(paramData)) {
                        param.put(getSearchFieldsDbName().get(index), new String(paramData));
                        if (searchCondition.equals("")) {
                            searchCondition = "search_" + (String) str + "=" + replaceSpecialChar(paramData.toString());
                        } else {
                            searchCondition += "&search_" + (String) str + "=" + replaceSpecialChar(paramData.toString());
                        }
                    }
                    getSearchFieldsData().set(index, paramData);
//                    getSearchFieldsDataMap().put("search_" + getMoreSearchFields().get(index), paramData);
                    getSearchFieldsMap().put("search_" + getMoreSearchFields().get(index), paramData);
                    index++;
                }
            }
            //Added by Zhafari @ 29-May-2015 extraCondition
            String trigger[]; // trigger[0] = field name, trigger[1] = field data
            for (Object str : getExtraConditionMap().keySet()) {
                
                trigger = ((String) str).split(";"); //eg. trigger[0] = "active_flag", trigger[1] = "I" 
                if (trigger[1].equals("emptyString")) {
                    trigger[1] = "";
                }
                
                if (getSearchFieldsDataMap().containsKey("search_" + trigger[0])) {
                    if (getSearchFieldsDataMap().get("search_" + trigger[0]).equals(trigger[1])) { // if true, will trigger extraCondition
                        Debug.printFrameworkDebug("ExtraCondition append " + getExtraConditionMap().get((String) str));
                        getExtraCondition_toExecute().add(getExtraConditionMap().get((String) str));
                    }
                }
            }
            //Added by Zhafari @ 29-May-2015 extraCondition - END
            
            //Added by Zhafari @ 10-Jun-2015 sqlTables_other
            for (Object str : getSqlTablesMap_other().keySet()) {
                
                trigger = ((String) str).split(";");
                if (trigger[1].equals("emptyString")) {
                    trigger[1] = "";
                }
                
                if (getSearchFieldsDataMap().containsKey("search_" + trigger[0])) {
                    if (getSearchFieldsDataMap().get("search_" + trigger[0]).equals(trigger[1])) { // if true, will switch sqlTables to sqlTables_other
                        setSqlTables(getSqlTablesMap_other().get((String)str).toString());
                    }
                }
            }
            //Added by Zhafari @ 10-Jun-2015 sqlTables_other - END

            if (!Validator.isEmpty(request.getParameter("dynamicSortBy"))) {
                if (searchCondition.equals("")) {
                    searchCondition = "dynamicSortBy=" + request.getParameter("dynamicSortBy");
                } else {
                    searchCondition += "&dynamicSortBy=" + request.getParameter("dynamicSortBy");
                }
                searchCondition += "&dynamicSortOrder=" + request.getParameter("dynamicSortOrder");
                setSqlOrderBy(getDynamicSortBy_condition() + " " + (getDynamicSortOrder().equals("A") ? "asc" : "desc"));
                setSqlOrderBy(request.getParameter("dynamicSortBy") + " " + (request.getParameter("dynamicSortOrder").equals("A") ? "asc" : "desc"));
                setDynamicSortBy(request.getParameter("dynamicSortBy"));
                setDynamicSortOrder(request.getParameter("dynamicSortOrder"));
            }
            //if (!Validator.isEmpty(request.getParameter("pageNo"))){
            //    searchCondition += "&pageNo="+request.getParameter("pageNo");
            //}
            
//            System.out.println("DynamicAction : search2() searchCondition = " + searchCondition);
            if (getIsMixConfig()) {
                searchCondition+="&searchCode="+request.getParameter("searchCode");
            }
            if (request.getParameter("pageSize") != null && !request.getParameter("pageSize").equalsIgnoreCase("null")) {
                searchCondition += "&pageSize=" + request.getParameter("pageSize");
            }
            setSearchCondition(searchCondition);
            keepObjectToSession(searchCondition + "&pageNo=" + request.getParameter("pageNo"), getAction() + "_searchCondition");
        } catch (BaseException be) {
            addActionMessage(be.getMessage());
            return SUCCESS + (getSystemType_().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)?"":"_"+getSystemType_());//goto search page again.
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            new LogFunction().logError(this.getClass(), "", e1);
            if(e1.getMessage().startsWith("Illegal argument of dynamicSortOrder")){
                addActionError(e1.getMessage());
                return "pageError";
            }
        }
        if (getIsMixConfig()) {
            if (getSetupMap().get("searchCodeDbName") !=null) {
                String[] searchCodeData = request.getParameter("searchCode").split("::");
                if (searchCodeData.length == 2) {
                    param.put(SystemConstants.CRITERIA.NO_CHANGE+getSetupMap().get("searchCodeDbName"), searchCodeData[1]);
                }
            }
        }
        //add in user's login PK in to the param if session_us_id_filter in config map is not null
        checkFilterBySessionUsId(param);
        checkFilterBySessionUsDiv(param);
        setSearchedParam(param);
        check_dd_criteria(getSearchedParam());
        try {
            if (getSpecialSearch() != null) {
                doSpecialSearch();
            } else {
                new DataRetriever().retrieveData2(this, request, request.getSession().getServletContext());
            }
        } catch (BaseException be) {
            addActionError(be.getMessage());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            new LogFunction().logError(this.getClass(), "", e);
        }
        Debug.printFrameworkDebug("getSearchFieldsDataMap = " + getSearchFieldsDataMap());
        return "search2" + (getSystemType_().equals(SystemConstants.SYSTEM_TYPE.DEFAULT)?"":"_"+getSystemType_());
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

    public boolean isRequiredField(String operationType, String pageField) {
        String required = null;
        if (operationType.equals(UPDATE)) {
            required = (String) configMap.get("editPageRequired");
            if (required == null || required.equalsIgnoreCase("asAddPage")) {
                required = (String) configMap.get("addPageRequired");
            }
        } else { // is insert
            required = (String) configMap.get("addPageRequired");
        }
        String[] requiredArr = required.split(",");
        for (String str : requiredArr) {
            if (str.split(";")[1].trim().equals(pageField)) {
                return true;
            }
        }

        return false;
    }

    public List<String> getPageFields() {
        return pageFields;
    }

    public void setPageFields(List<String> pageFields) {
        this.pageFields = pageFields;
    }

    public Map getPageFieldDD() {
        return pageFieldDD;
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

    public String getPageLoadedJavascript() {
        return pageLoadedJavascript;
    }

    public void setPageLoadedJavascript(String pageLoadedJavascript) {
        this.pageLoadedJavascript = pageLoadedJavascript;
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

    public String getAddPageRequired() {
        return addPageRequired;
    }

    public void setAddPageRequired(String addPageRequired) {
        this.addPageRequired = addPageRequired;
    }

    public String getEditPageRequired() {
        return editPageRequired;
    }

    public void setEditPageRequired(String editPageRequired) {
        this.editPageRequired = editPageRequired;
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

    private void populateDynamicContent(String insertUpdate_or_load) {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try {
            if (configMap == null) {
                configMap = new CommonFunction().getDynamicEntryConfiguration(request.getParameter("action") + "_dynamic", request, request.getSession().getServletContext(), "");
                configMap = checkDynamicEntryConfig(configMap, request.getParameter("action")+"_dynamic", request, request.getSession().getServletContext(), "");
            }
            if (configMap.get("popupCalanderUsed") != null) {
                setPopupCalanderUsed(true);
            }
            if (configMap.get("loadDescsUsed") != null) {
                setLoadDescsUsed(true);
            }
            if (configMap.get("labelWidth") != null) {
                setLabelWidth(configMap.get("labelWidth").toString());
            }
            
            setPageTitle_(getText((String)configMap.get("searchDescription")));
            
            if (configMap.get("pageJavascript") != null) {
                pageJavascript = (String) configMap.get("pageJavascript");
                pageJavascript = replaceJavascriptText(pageJavascript);
            }
            if (configMap.get("pageLoadedJavascript") != null) {
                pageLoadedJavascript = (String) configMap.get("pageLoadedJavascript");
                pageLoadedJavascript = replaceJavascriptText(pageLoadedJavascript);
            }
            if (configMap.get("localValidateFormJavascript") != null) {
                localValidateFormJavascript = (String) configMap.get("localValidateFormJavascript");
                localValidateFormJavascript = replaceJavascriptText(localValidateFormJavascript);
            }
            if (configMap.get("dynamicDescription") != null) {
                pageTitle = getText((String) configMap.get("dynamicDescription"));
            }
            String dynamicPageLabels = (String) configMap.get("addPageLabels");
            if (insertUpdate_or_load.equals(LOAD) || insertUpdate_or_load.equals(UPDATE)) {
                dynamicPageLabels = (String) configMap.get("editPageLabels");
                if (dynamicPageLabels == null || dynamicPageLabels.equalsIgnoreCase("asAddPage")) {
                    dynamicPageLabels = (String) configMap.get("addPageLabels");
                }
            }
//            Debug.printFrameworkDebug("dynamicPageLabels = " + dynamicPageLabels);
            StringTokenizer st = new StringTokenizer(dynamicPageLabels, ",");

            String dynamicPageColumns = (String) configMap.get("addPageColumns");
            if (insertUpdate_or_load.equals(LOAD) || insertUpdate_or_load.equals(UPDATE)) {
                dynamicPageColumns = (String) configMap.get("editPageColumns");
                if (dynamicPageColumns == null || dynamicPageColumns.equalsIgnoreCase("asAddPage")) {
                    dynamicPageColumns = (String) configMap.get("addPageColumns");
                }
            }
//            Debug.printFrameworkDebug("dynamicPageColumns = " + dynamicPageColumns);
            StringTokenizer st2 = new StringTokenizer(dynamicPageColumns, ",");
            Class c = Class.forName((String) configMap.get("modelName"));
            modelObject = c.newInstance();
            if (insertUpdate_or_load.equals(LOAD)) {
                if (pageFieldsData.get("objId") == null || pageFieldsData.get("objId").equals("")) {
                    modelObject = baseDAO.getModelById(request.getParameter("id"), c);
                    pageFieldsData.put("objId", request.getParameter("id"));
                }
            }
            Method m = null;

            try {
                m = modelObject.getClass().getMethod("getColumnLengthMap");
                modelColumnLengthMap = (Map) m.invoke(modelObject);
            } catch (Exception ex) {
                modelColumnLengthMap = null;
            }

            while (st.hasMoreTokens()) {
                String label = st.nextToken().trim();
                String column = st2.nextToken().trim();
                pageLabels.add(label);
                pageLabelsDesc.add(getText(label));
                String temp = (String) configMap.get(label + "_inputType");
                if (temp == null) {
                    temp = "<^text^/>";
                } else {
                    temp = temp.replace("[", "<");
                    temp = temp.replace("]", ">");
                    temp = temp.replace("<0>", "[0]");
                }
                temp = temp.replace("[", "<");
                temp = temp.replace("]", ">");
                temp = temp.replace("<0>", "[0]");
                temp = temp.replaceAll("%22", "\"");
                String strPattern = "";
                String strReplace = "";
                StringTokenizer moreColumns = new StringTokenizer(column, new String("|"));
                String colName = null;
                String data = null;
                String aliasName = null;
                Object obj = null;
                int idx = 0, nextIdx = 0;
                int count = 0;
                do {
                    colName = moreColumns.nextToken().trim();
                    idx = temp.indexOf("^");
                    if (idx >= 0) {
                        nextIdx = temp.indexOf("^", idx + 1) + 1;
                        strPattern = temp.substring(idx, nextIdx);
                        temp = temp.substring(0, idx) + "????" + temp.substring(nextIdx);
                    }

                    // to get the data
                    StringTokenizer colNameTokenizer = new StringTokenizer(colName, ".");
                    int count2 = 0;
                    while (colNameTokenizer.hasMoreTokens()) {
                        String columnName = colNameTokenizer.nextToken();
                        if (count2++ == 0) {
                            if (insertUpdate_or_load.equals(LOAD)) {
                                m = modelObject.getClass().getMethod("get" + WordUtils.capitalize(columnName));
                                obj = m.invoke(modelObject);
                            }
                        } else {
                            aliasName = columnName;
                            if (columnName.toLowerCase().indexOf(" as ") > 0) {
                                aliasName = columnName.substring(columnName.toLowerCase().indexOf(" as ") + 4);
                                columnName = columnName.substring(0, columnName.toLowerCase().indexOf(" as "));
                            }
                            if (insertUpdate_or_load.equals(LOAD)) {
                                if (obj != null) {
                                    m = obj.getClass().getMethod("get" + WordUtils.capitalize(columnName));
                                    obj = m.invoke(obj);
                                }
                            }
                            columnName = aliasName;
                        }
                        colName = columnName;
                    }

                    //try to get the maxLength of the column
                    if (temp.equals("<????/>") && modelColumnLengthMap != null) {
                        try {
                            if (modelColumnLengthMap.get(colName) != null) {
                                temp = "<???? maxlength='" + modelColumnLengthMap.get(colName) + "'/>";
                            }
                        } catch (Exception e) {
                        }
                    }

                    if (insertUpdate_or_load.equals(LOAD)) {
                        if (obj != null) {
                            if (obj instanceof Timestamp || obj instanceof java.util.Date) {
                                data = Formatter.formatDate((Timestamp) obj, DEFAULT_DATE_POPUP_FORMAT_java);
                            } else {
                                data = obj.toString();
                            }
                        } else {
                            data = "";
                        }
                        pageFieldsData.put(colName, data);
                    } else {
                        data = pageFieldsData.get(colName);
                        if (data == null) {
                            data = "";
                        }
                    }
                    data = StringEscapeUtils.escapeHtml4(data).replaceAll("[^\\x20-\\x7e]", "");
                    data = data.replaceAll("'", "&#39;");
                    // to get the data: finish here.
                    if (strPattern.startsWith("^select")) {
                        strPattern = strPattern.substring(8, strPattern.length() - 1);
                        pageFieldDD.put(label, getListFromSetup(strPattern));
                        pageFieldColName.put(label, colName);
                        if (strPattern.indexOf("onchange=") > 0) {
                            pageFieldJavaScript.put(colName + "_onchange", strPattern.substring(strPattern.indexOf("onchange=") + 9));
                        }
                        temp = "select:" + colName;
                        break;
                        //pageFieldDD_javaScript.put(label, getListFromSetup(temp));
                        //pageFieldAfterDD.put(label, getListFromSetup(temp));
                    } else if (strPattern.startsWith("^checkbox")) {
                        pageFieldColName.put(label, colName);
                        if (strPattern.indexOf("onchange=") > 0) {
                            pageFieldJavaScript.put(colName + "_onchange", strPattern.substring(strPattern.indexOf("onchange=") + 9));
                        }
                        strReplace = "input type='checkbox' name='" + colName + "' value='Y' id='" + colName + "'";
                        if (data.equalsIgnoreCase("Y")) {
                            strReplace += " checked='checked'";
                        }
                    } else {
                        if (strPattern.startsWith("^hidden")) {
                            strReplace = "input type='hidden' name='" + colName + "' value='" + data + "' id='" + colName + "'";
                        } else if (strPattern.startsWith("^textarea")) {
                            strReplace = "textarea name='" + colName + "'";
                            temp += data + "</textarea>";
                        } else if (strPattern.startsWith("^text")) {
                            if (temp.indexOf("maxlength") >= 0 || temp.indexOf("maxLength") >= 0) {
                                strReplace = "input type='text' name='" + colName + "' value='" + data + "' id='" + colName + "'";
                            } else {
                                //add in max length if ModelObject provide the information when the dynamicEntry do not provide it.
                                if (modelColumnLengthMap.get(colName) != null) {
                                    strReplace = "input id='" + colName + "'type='text' name='" + colName + "' value='" + data + "' maxlength='" + modelColumnLengthMap.get(colName) + "'";
                                } else {
                                    strReplace = "input id='" + colName + "'type='text' name='" + colName + "' value='" + data + "'";
                                }
                            }

                        } else if (strPattern.startsWith("^password")) {
                            strReplace = "input type='password' name='" + colName + "' value='" + data + "'";
                        } else if (strPattern.startsWith("^checkbox")) {
                            strReplace = "input type='checkbox' name='" + colName + "' value='" + data + "' id='" + colName + "'";
                        } else if (strPattern.startsWith("^popCalander")) {
                            strReplace = "input type='text' name='" + colName + "' id='" + colName + "' style='text-align:left; width:80;' value='" + data + "' readonly='true'";
                            temp += "<img alt='' src='images/calendar.gif' id='imgCalTo1' style='' onclick=\"popUpCalendar(this, document.getElementById('" + colName + "'), '" + DEFAULT_DATE_POPUP_FORMAT_jsp + "')\"   title='Calendar' align='absmiddle' height='18' width='18'>";
                        }
                    }
                    temp = temp.replace("????", strReplace);
                    count++;
                } while (temp.indexOf("^") >= 0);
                //for lookup's description.
                String columnName = null;
                if (temp.toLowerCase().indexOf("</set") > 0) {
                    while (temp.toLowerCase().indexOf("</set>") > 0) {
                        strReplace = temp.substring(temp.toLowerCase().indexOf("<set>"), temp.toLowerCase().indexOf("</set>") + 6);
                        strPattern = strReplace.substring(5, strReplace.length() - 6);
                        strPattern = pageFieldsData.get(strPattern);
                        if (strPattern == null) {
                            strPattern = "";
                        }
                        temp = temp.replace(strReplace, strPattern);
                    }

                    while (temp.toLowerCase().indexOf("</settext>") > 0) {
                        strReplace = temp.substring(temp.toLowerCase().indexOf("<settext>"), temp.toLowerCase().indexOf("</settext>") + 10);
                        strPattern = strReplace.substring(9, strReplace.length() - 10);
                        strPattern = getText(strPattern);
                        if (strPattern == null) {
                            strPattern = "";
                        }
                        temp = temp.replace(strReplace, strPattern);
                    }
                }
                pageFields.add(temp);
            }
            modelObject = null;
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
            baseDAO.closeSession();
        }
    }

    public Boolean getPopupCalanderUsed() {
        return popupCalanderUsed;
    }

    public void setPopupCalanderUsed(Boolean popupCalanderUsed) {
        this.popupCalanderUsed = popupCalanderUsed;
    }

    public Boolean getLoadDescsUsed() {
        return loadDescsUsed;
    }

    public void setLoadDescsUsed(Boolean loadDescsUsed) {
        this.loadDescsUsed = loadDescsUsed;
    }

    public String getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(String labelWidth) {
        this.labelWidth = labelWidth;
    }

//    @Override
//    public String getSystemType(){
//        return getSf_system_type();
//    }

    public Map checkDynamicEntryConfig(Map<String, String> setupMap, String action, HttpServletRequest request, ServletContext context, String defaultAction){
        Map<String, String> extendedMap = null;
        if (setupMap.get("extendsConfig") != null){
            try {
                extendedMap = new CommonFunction().getDynamicEntryConfiguration(setupMap.get("extendsConfig").toString() + "_dynamic", request, request.getSession().getServletContext(), "");
                //extendedMap = getDynamicActionSetup();
                for (String objKey : setupMap.keySet()){
                    if (!objKey.equals("extendsConfig")){
                        extendedMap.remove(objKey);
                        extendedMap.put(objKey, setupMap.get(objKey));
                    }
                }
            } catch (Exception e){}
            setupMap = extendedMap;
        }
        return setupMap;
    }
    
    
    //Added by ChangMH 15-Jan-15 :: Temporary for testing 
    public String getFormattedStatusField(String value, int row) {
        Object instanceObject = null;
        Class c = null;
        Method m = null;
        Map resultMap = ((Map) getResult().get(row));
        Object returnObj = null;
        String strVal = value;
        if(strVal.contains(":")){
            strVal = value.replace("*", ", ");
            strVal = strVal.substring(0, strVal.length()-2);
        }else{
            strVal = getText("PR.SUB_STATUS.VER."+strVal);
        }
        return strVal;
    }
    
    private String printTo = null;
    public String getPrintTo() {
        return printTo;
    }
    public void setPrintTo(String printTo) {
        this.printTo = printTo;
    }
    
    public String dynamicSearchRpt() {
        if (Validator.isEmpty(printTo)) {
            printTo = "pdf";
        } else {
            if (Arrays.asList(SystemConstants.DYNAMIC_ATTS.AVAILABLE_RPT_FORMAT).contains(printTo)) {
                //GOOD
            } else {
               //hardcode printTo to pdf;
               printTo = "pdf";
            }
        }
        isGenerateDynamicRpt = Boolean.TRUE;
        search2();
        return "rpt";
    }

}
