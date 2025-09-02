package com.sains.framework.lookup;

/*
import java.util.Date;import java.util.Map;

import javax.servlet.http.HttpSession;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionSupport;

public class LoginAction extends ActionSupport {

 */
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.sains.common.util.DateUtil;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonList;
import com.sains.framework.base.LogFunction;
import com.sains.framework.base.LookupCheckTrigger;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.sains.common.util.PageUtil;
import com.sains.common.util.Validator;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.Formatter;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseAction;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.DynamicPreSearch;
import com.sains.framework.base.web.DynamicAction;


public class LookupAction extends BaseAction {

    private static final long serialVersionUID = 5925200641575065502L;
    private Map<String, Object> searchParam = new HashMap();
    private String action = "";
    private String lookup;
    private String lookupDesc;
    private String query;
    private String lookFor;
    private String filterBy;
    private String filter;
    private String writeTo;
    private String displayedColumns;
    private String focusOn;
    private String retrieveOnLoad = "N";
    private String lookupParentFormId = "";
    private Integer pageNo;
    private Integer pageSize;
    private Integer numberOfRows;
    private boolean searched = false;
    private Map criterias = new HashMap();
    private Map criteriasData = new HashMap();
    private List fields = new ArrayList();
    private List result = new ArrayList();
    private List columns = new ArrayList();
    private List<String> displayingColumns = new ArrayList();
    private List goodDisplayingColumns = new ArrayList();
    private String searchCondition = "";
    private Map<String, String> lookupFilters = new HashMap<String, String>();
    private Map<String, String> lookupFiltersOperator = new HashMap<String, String>();
    private String showHideLookupMoreField_ = "H";//hide, S=Show;
    private Boolean hasMoreSearchField = Boolean.FALSE;
    private List<String> moreSearchFieldsDbName = new ArrayList();
    private List<String> moreSearchFields = new ArrayList();
    private Map searchFieldsDataMap = new HashMap();
    private Map<String, String> searchFieldsMap = new HashMap();
    private List<String> searchFields = new ArrayList();
    private List<String> searchFieldsLabel = new ArrayList();
    private boolean paging = true;
    private String retrievingColumns = "";
    private Map retrievingColumnsLabel = new HashMap();
    private String sqlTables = "";
    private Map setupMap = new HashMap();
    private Map searchedParam = new HashMap();
    private Map searchFieldDD = new HashMap();
    private List searchFieldsDbName = new ArrayList();
    private List searchFieldsData = new ArrayList();
    private String sqlOrderBy = "";
    private String dcGroupBy = "";
    private List writeToList = new ArrayList();
    private List writeToDataList = new ArrayList();
    private String lookupSearchFieldData_ = "";
    private String lookupSearchFieldId_ = "";
    private String invokedMsg_ = "";
    private boolean dynamicSearchLoaded = false;
    private String usePopupCalander = "N";
    private Map searchFieldFormat = new HashMap();
    private Map searchFieldStyleFormat = new HashMap();
    private Map searchFieldsDateData = new HashMap();
    private String teaC_ = "";
    private String LAN_ = "";
    private String dynamicSortBy = "";
    private String dynamicSortOrder = "";
    private List<String> sortingFields = new ArrayList();
    private String firstSearchFieldName = null;
    private Boolean noErrorMsg = Boolean.FALSE;
    private Boolean noClearData = Boolean.FALSE;
    private Boolean serverSideCheck = Boolean.FALSE;
    private String serverSideCheckCode = null;
    private String entityTree;
    private String hierarchyLookupOrderBy = null;
    private String hierarchyLookupDisplayColumn = null;
    private String hierarchyLookupModel = null;
    private String hierarchyLookupColumns = null;
    private Map hierarchyLookupFilter = new HashMap();
    private Map ifEmpty_lookupFilters = new HashMap();
    private String hierarchyLookupGetChildMethod = null;
    private BaseDAO baseDAO = new BaseDAOImpl();
    private List pageSizeOption = new ArrayList();
    private Boolean showPageSize = Boolean.FALSE; //default lookup to not show page size?
    private String preLookupSearchMethod = null;
    private Boolean lookupRequireLogin = Boolean.FALSE;
    private Boolean requireRelogin = Boolean.FALSE;
    
    public List getPageSizeOption() {
        return pageSizeOption;
    }

    public void setPageSizeOption(List pageSizeOption) {
        this.pageSizeOption = pageSizeOption;
    }

    public Boolean getShowPageSize() {
        return showPageSize;
    }

    public void setShowPageSize(Boolean showPageSize) {
        this.showPageSize = showPageSize;
    }

    public String getPreLookupSearchMethod() {
        return preLookupSearchMethod;
    }

    public void setPreLookupSearchMethod(String preLookupSearchMethod) {
        this.preLookupSearchMethod = preLookupSearchMethod;
    }

    private void populatePageSizeIfEmpty() {
        if (pageSizeOption.isEmpty()) {
            pageSizeOption.add(new Options("10", "10"));
            pageSizeOption.add(new Options("20", "20"));
            pageSizeOption.add(new Options("30", "30"));
            pageSizeOption.add(new Options("40", "40"));
            pageSizeOption.add(new Options("50", "50"));
        }
    }
    
    public void loadDynamicSearch() throws Exception {
        if (dynamicSearchLoaded) return;
        dynamicSearchLoaded = true;
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String setupName = lookup.substring(9);
        setupMap = getDynamicActionSetup(setupName);
        setupMap = checkConfig(setupMap);
        setSetupMap(setupMap);
        if (setupMap.get("preLookupSearchMethod") != null) {
                setPreLookupSearchMethod(setupMap.get("preLookupSearchMethod").toString());
            }
        if (setupMap.get("lookupShowRecordPerPage") != null) {
            setShowPageSize(Boolean.parseBoolean(setupMap.get("lookupShowRecordPerPage").toString()));
        } else {
            setShowPageSize(Boolean.TRUE); // always show!!
        }
        if (setupMap.get("recordPerPage") != null) {
            String[] pagingSetup = setupMap.get("recordPerPage").toString().split(";");
            if (pageSize == null) {
                pageSize = Integer.parseInt(pagingSetup[0]);
            }
            
            if (pagingSetup.length > 1) {
                for (String pageSizeItem : pagingSetup[1].split(",")) {
                    pageSizeOption.add(new Options(pageSizeItem, pageSizeItem));
                }
            }
            
        }
        populatePageSizeIfEmpty();
        setRetrievingColumnsLabel((String)setupMap.get("lookupRetrievingColumnsLabel"));
        StringTokenizer st = new StringTokenizer(setupMap.get("searchFields").toString(), ",");
        StringTokenizer st2 = null;
        StringTokenizer st3 = null;
        if (!setupMap.get("searchFields").toString().contains(";")) {
            st2 = new StringTokenizer(setupMap.get("searchFieldsLabel").toString(), ",");
            st3 = new StringTokenizer(setupMap.get("searchFieldsDbName").toString(), ",");
        }
        String searchField = null;
        firstSearchFieldName = null;
        String[] fieldArr = null;
        while (st.hasMoreTokens()) {
            if (st2 == null) {
                fieldArr = st.nextToken().trim().split(";");
                searchField = fieldArr[0];
                if (Validator.isEmpty(fieldArr[1])) {
                    getSearchFieldsLabel().add(getText(getDynamicActionSetup(getAction())+"."+fieldArr[0]));
                } else {
                    getSearchFieldsLabel().add(getText(fieldArr[1].trim()));
                }
                if (Validator.isEmpty(fieldArr[2])) {
                    getSearchFieldsDbName().add(fieldArr[0].trim());
                } else {
                    getSearchFieldsDbName().add(fieldArr[2].trim());
                }
            } else {
                searchField = st.nextToken().trim();
                getSearchFieldsLabel().add(getText(st2.nextToken().trim()));
                getSearchFieldsDbName().add(st3.nextToken().trim());
            }
            if (searchField.startsWith("_date_")) {
                if (searchField.endsWith("_fromTo")) {
                    if (firstSearchFieldName == null){
                        firstSearchFieldName = "search_"+searchField+"From";
                    }
                }
                setUsePopupCalander("Y");
            }
            if (firstSearchFieldName == null){
                firstSearchFieldName = "search_"+searchField;
            }

            getSearchFields().add(searchField);
            getSearchFieldsData().add("");
            if (setupMap.get(searchField+"_dd") != null){
                String searchFieldDD_setup = setupMap.get(searchField+"_dd").toString();
                searchFieldDD.put(searchField+"_dd", getListFromSetup(searchFieldDD_setup));
                searchFieldDD.put(searchField+"_dd_key", searchFieldDD_setup.split(";")[3]);
                searchFieldDD.put(searchField+"_dd_value", searchFieldDD_setup.split(";")[4]);
//                if (searchFieldDD_setup.split(";")[2].equalsIgnoreCase("pubcode")){
//                    if (!((String)searchFieldDD.get(searchField+"_dd_key")).equalsIgnoreCase("code_1")){
//                        for (Object obj : (List)searchFieldDD.get(searchField+"_dd") ){
//                            ((Pubcode)obj).setKeyCode((String)searchFieldDD.get(searchField+"_dd_key"));
//                        }
//                    }
//                    if (!((String)searchFieldDD.get(searchField+"_dd_value")).equalsIgnoreCase("code_desc")){
//                        for (Object obj : (List)searchFieldDD.get(searchField+"_dd") ){
//                            ((Pubcode)obj).setKeyCode((String)searchFieldDD.get(searchField+"_dd_value"));
//                        }
//                    }
//                }
            }
        }
        if (setupMap.get("moreSearchFields") != null) {
        hasMoreSearchField = Boolean.TRUE;
        st = new StringTokenizer(setupMap.get("moreSearchFields").toString(), ",");
        int index = -1;
        while (st.hasMoreTokens()) {
            searchField = st.nextToken().trim();
            if (searchField.endsWith(";;")) searchField = searchField.substring(0, searchField.length()-2) + "; ; ";
            fieldArr = searchField.split(";", 0);
            searchField = fieldArr[0];
            if (Validator.isEmpty(fieldArr[1])) {
                searchFieldsMap.put(searchField+"_label", getText(getAction()+"."+fieldArr[0]));
                getSearchFieldsLabel().add(getText(getAction()+"."+fieldArr[0]));
            } else {
                searchFieldsMap.put(searchField+"_label", getText(fieldArr[1].trim()));
                getSearchFieldsLabel().add(getText(fieldArr[1].trim()));
            }
            if (Validator.isEmpty(fieldArr[2])) {
                searchFieldsMap.put(searchField+"_dbName", fieldArr[0].trim());
                getMoreSearchFieldsDbName().add(fieldArr[0].trim());
            } else {
                searchFieldsMap.put(searchField+"_dbName", fieldArr[2].trim());
                getMoreSearchFieldsDbName().add(fieldArr[2].trim());
            }
            index++;
            if (searchField.startsWith("_date_")) {
//                if (!isMixConfig) {
//                    if (searchFields_with_dateFromTo == null) {
//                        searchFields_with_dateFromTo = new ArrayList();
//                    }
//                    if (searchField.endsWith("_fromTo")) {
//                        searchFields_with_dateFromTo.add(searchField.substring(6, (searchField.length() - 7)));
//                    }
//                }
//                setUsePopupCalander("Y");
            }
            getMoreSearchFields().add(searchField);
            if (setupMap.get(searchField + "_dd") != null) {
                String searchFieldDD_setup = setupMap.get(searchField+"_dd").toString();
                String[] listSetup = searchFieldDD_setup.split(";");
                searchFieldDD.put(searchField + "_dd", getListFromSetup(searchFieldDD_setup));
                searchFieldDD.put(searchField + "_dd_list", listSetup[2]);   // Added by Delvene @ 29-Aug-2013 :: To identify list is from setup code table or normal option
                searchFieldDD.put(searchField + "_dd_key", listSetup[3]);
                searchFieldDD.put(searchField + "_dd_value", listSetup[4]);
                if (setupMap.get(searchField + "_dd_type") != null) {
                    getSearchFieldDD().put(searchField + "_dd_type", setupMap.get(searchField + "_dd_type"));
                }
            }
            searchFieldsMap.put("search_" + searchField, "");
            getSearchFieldsData().add("");
            getSearchFieldsDataMap().put("search_" + searchField, "");
        }
    }
        //Group By
        setDcGroupBy(setupMap.get("groupBy")==null? "" : setupMap.get("groupBy").toString());
        
        if (setupMap.get("lookupFilters") != null){
            String[] splitArr = null;
            st = new StringTokenizer(setupMap.get("lookupFilters").toString(), ",");
            while (st.hasMoreTokens()) {
                splitArr = st.nextToken().trim().split(";");
                getLookupFilters().put(splitArr[0].trim(), splitArr[1].trim());
                if (splitArr.length == 3) {
                    lookupFiltersOperator.put(splitArr[0].trim(), splitArr[2].trim());
                }
            }
        }
        if (setupMap.get("retrieveOnLoad") != null){
            retrieveOnLoad = setupMap.get("retrieveOnLoad").toString();
        }

        // for Hierarchy Lookup:
        if (setupMap.get("hierarchyLookupModel") != null){
            hierarchyLookupModel = setupMap.get("hierarchyLookupModel").toString();
            hierarchyLookupColumns = setupMap.get("hierarchyLookupColumns").toString();
            String[] paramArr = null;
            if (!Validator.isEmpty((String)setupMap.get("hierarchyLookupFilter"))){
                for (String str : setupMap.get("hierarchyLookupFilter").toString().split(",")){
                    paramArr = str.split(";");
                    hierarchyLookupFilter.put(paramArr[0].trim(), paramArr[1].trim());
                }
            }
            hierarchyLookupGetChildMethod = setupMap.get("hierarchyLookupGetChildMethod").toString();
            if (setupMap.get("hierarchyLookupOrderBy") != null){
                hierarchyLookupOrderBy = "order by " + setupMap.get("hierarchyLookupOrderBy");
            }

            if (setupMap.get("hierarchyLookupDisplayColumn") != null){
                hierarchyLookupDisplayColumn = setupMap.get("hierarchyLookupDisplayColumn").toString();
            }
        }

        if (!Validator.isEmpty(request.getParameter("dynamicSortBy"))){
            if (searchCondition.equals("")){
                searchCondition = "dynamicSortBy=" + request.getParameter("dynamicSortBy");
            } else {
                searchCondition += "&dynamicSortBy=" + request.getParameter("dynamicSortBy");
            }
            searchCondition += "&dynamicSortOrder=" + request.getParameter("dynamicSortOrder");
            setSqlOrderBy(getDynamicSortBy_condition() + " " + (request.getParameter("dynamicSortOrder").equals("A")?"asc":"desc"));
            setDynamicSortBy((String)request.getParameter("dynamicSortBy"));
            setDynamicSortOrder((String)request.getParameter("dynamicSortOrder"));
        }
        //setSearchCondition(searchCondition);
        if (setupMap.get("sortingFields") != null) {
            if (setupMap.get("sortingFields").equals("asDisplayFields")) {
                st = new StringTokenizer(displayedColumns.toString(), ",");
            } else {
                st = new StringTokenizer(setupMap.get("sortingFields").toString(), ",");
            }
            while (st.hasMoreTokens()) {
                sortingFields.add(st.nextToken().trim());
            }
            if (Validator.isEmpty(dynamicSortBy)) {
                dynamicSortBy = sortingFields.get(0);
            }
        }
        try {
            String strColumn = "";
            for (String displayCol : displayedColumns.split(",")) {
                Boolean found = Boolean.FALSE;
                for (String lookupRegrievingCol : ((String) setupMap.get("lookupRetrievingColumns")).split(",")) {
                    if (lookupRegrievingCol.trim().equals(displayCol.trim())) {
                        found = Boolean.TRUE;
                        break;
                    } else {
                        strColumn = lookupRegrievingCol.trim().toLowerCase();
                        if (strColumn.lastIndexOf("as ") > 0) {
                            if (lookupRegrievingCol.trim().substring(strColumn.lastIndexOf("as ") + 3).equals(displayCol.trim())) {
                                found = Boolean.TRUE;
                                break;
                            }
                        } else if (strColumn.lastIndexOf(".") > 0) {
                            if (lookupRegrievingCol.trim().substring(strColumn.lastIndexOf(".") + 1).equals(displayCol.trim())) {
                                found = Boolean.TRUE;
                                break;
                            }
                        }
                    }
                }
                if (!found) {
                    throw new CustomBaseException("lookup.invalidDisplayCol");
                }
            }
        } catch (BaseException be) {
            throw be;
        }

        setRetrievingColumns(setupMap.get("lookupRetrievingColumns").toString());
        setSqlTables(setupMap.get("sqlTables").toString());
    }
    
    public String mSearch() throws Exception {
        if (notValidPage()) {
            Map session = ActionContext.getContext().getSession();
            return SystemConstants.ACTION_Status.NOT_AUTHORISED + session.get("loginFrom");
        }
        pageItem = 5; //if lookup modal paging
        return search()+"_modal";
    }
    private Integer pageItem = 19;
    public String modal() throws Exception {
        pageItem = 5; //if lookup modal paging
        if (notValidPage()) {
            Map session = ActionContext.getContext().getSession();
            return SystemConstants.ACTION_Status.NOT_AUTHORISED + session.get("loginFrom");
        }
        return process()+"_modal";
    }
    public Boolean notValidPage() {
        if (!lookup.startsWith("useSetup_")) {
            Object obj = SystemConstants.globalServletContext.getResourceAsStream("pages/lookup/lookup_"+lookup+".jsp");
            if (obj == null) {
                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                request.setAttribute("not_authorised", "Invalid lookup.");
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    public String process() throws Exception {
    
        String setupName = null;
        setSearched(false);
//        System.out.println("lookup = " + lookup);
        try {
            if (lookup.indexOf("useSetup_") == 0) {
                loadDynamicSearch();
            } else {
                if (notValidPage()) {
                    Map session = ActionContext.getContext().getSession();
                    return SystemConstants.ACTION_Status.NOT_AUTHORISED + session.get("loginFrom");
                }
            }
        } catch (BaseException be) {
            addActionError(be.getMessage());
            return "lookupError";
        } catch (Exception e) {
            addActionError("Unexpected Error at Lookup, pls contact system administrator");
            return "lookupError";
        }
        
        if (requireRelogin) {
            Map session = ActionContext.getContext().getSession();
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            String temp = null;
            for (String paramKey : request.getParameterMap().keySet()) {
                if (temp == null) {
                    temp = "?"+paramKey+"="+request.getParameter(paramKey);
                } else {
                    temp += "&"+paramKey+"="+request.getParameter(paramKey);
                }
            }
            session.put("lookupRefererUrl_", request.getRequestURI().substring(request.getRequestURI().indexOf("/", 2))+temp);
            session.put("lookupDescription", getFinalLookupDesc());
            return "login";
        }
        if (retrieveOnLoad.equalsIgnoreCase("Y")){
            return search();
        }

        return SUCCESS;
//        String returnStr = SUCCESS;
//        Map session = ActionContext.getContext().getSession();
//        if (session.get("login_user_type") != null && session.get("login_user_type").equals(SystemConstants.SF_SYSTEM_TYPE.PUBLIC)) {
//            returnStr = "success_el";
//        } else if (session.get("login_user_type") != null && session.get("login_user_type").equals(SystemConstants.SF_SYSTEM_TYPE.INTERNAL)) {
//            returnStr = SUCCESS;
//        }
//        return returnStr;
    }

    public String gotoPage()throws Exception {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        if (request.getParameter("bs") != null && request.getParameter("bs").equals("1")) {
            return mSearch();
        }
        return search();
    }

    public List getHiddenColumns() {
        List<String> hiddenColumns = new ArrayList();
        for (Object col : columns) {
            if (!displayingColumns.contains((String)col)){
                hiddenColumns.add(((String)col).toLowerCase());
            }
        }
        return hiddenColumns;
    }

    public String getHiddenValue(Integer index, String columnName) {
        Object obj = ((Map)result.get(index)).get(columnName);
        if (obj != null) return obj.toString();
        return "";

    }

    protected String populateSearchCondition() {
        searchCondition = "query=" + query;
        searchCondition += "&lookFor=" + lookFor;
        searchCondition += "&lookup=" + lookup;
        searchCondition += "&writeTo=" + writeTo;
        searchCondition += "&displayedColumns=" + displayedColumns;
        searchCondition += "&focusOn=" + focusOn;
        searchCondition += "&filterBy=" + filterBy;
        searchCondition += "&filter=" + filter;
        searchCondition += "&lookupParentFormId=" + lookupParentFormId;
        //added wongkk4@14Aug14 - missing
        searchCondition += "&LAN_=" + LAN_;
        return searchCondition;
    }

    public String search() throws Exception {
        System.out.println("_parentFormId= " + lookupParentFormId);
        if (lookup.indexOf("useSetup_") == 0) {
            try {
                loadDynamicSearch();
            } catch (BaseException be) {
                addActionError(be.getMessage());
                return "lookupError";
            }
        } else {
            if (notValidPage()) {
                Map session = ActionContext.getContext().getSession();
                return SystemConstants.ACTION_Status.NOT_AUTHORISED + session.get("loginFrom");
            }
        }
        setSearched(true);
        populateSearchCondition();
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        if (criterias.size() <= 0) {
            for (Object obj : request.getParameterMap().keySet()) {
                String paramName = ((String) obj);
                if ((paramName).startsWith("search_")) {
                    String paramValue = request.getParameter(paramName);
                    if (!Validator.isEmpty(paramValue)) {
                        criterias.put(paramName.substring(7, paramName.length()), paramValue);
//                        searchCondition += "&" + paramName + "=" + paramValue.replace("-", "&#45;").replaceAll(" ", "&#32;");
                        criteriasData.put(paramName.substring(7, paramName.length()), paramValue);
                    }
                }
            }
        }

        try {
            System.out.println("lookup " + lookup);
            if (lookup.indexOf("useSetup_") == 0) {
                int index = 0;
                Map param = new HashMap();
                String paramData = null;
                System.out.println("getSearchFields() " + getSearchFields());
                for (Object str : getSearchFields()){
                    Boolean isDate = Boolean.FALSE;
                    System.out.println("str " + str);
                    if (((String) str).startsWith("_date_")) {
                        isDate = Boolean.TRUE;
                        String tempDateData = null;
                        if (((String) str).endsWith("_fromTo")) {
                            str = extractSearchFieldForDate((String) str);
                            String ori = request.getParameter("search_" + (String) str);
                            if (ori != null && ori.length() > 10) {
                                tempDateData = ori.substring(0, 10);
                                paramData = ori.substring(13);
                            } else {
                                tempDateData = request.getParameter("search_" + (String) str + "From");
                                paramData = request.getParameter("search_" + (String) str + "To");
                            }
                            
                            if (Validator.isEmpty(tempDateData)) {
                                tempDateData = "";
                            }
                            if (Validator.isEmpty(paramData)) {
                                paramData = "";
                            }

                            //keep the searchData : start
                            getSearchFieldsDateData().put("search_" + str + "From", tempDateData);
                            getSearchFieldsDateData().put("search_" + str + "To", paramData);
                            if (searchCondition.equals("")) {
                                searchCondition = "search_" + (String) str + "From=" + tempDateData.toString();
                                searchCondition += "&search_" + (String) str + "To=" + paramData.toString();
                            } else {
                                searchCondition += "&search_" + (String) str + "From=" + tempDateData.toString();
                                searchCondition += "&search_" + (String) str + "To=" + paramData.toString();
                            }
                            //keep the searchData : end

                            if (!Validator.isEmpty(paramData)) { // format the DateTo here... (1 day after selected date)
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(DateUtil.getDate(paramData, getText("date_default_date_dr")));
                                cal.add(Calendar.DAY_OF_MONTH, 1);
                                paramData = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd");
                            }
                            if (!Validator.isEmpty(tempDateData)) {
                                //reportParams += "search_" + str + "From=" + paramData + "&" ;
                                if (Validator.isEmpty(paramData)) { // no DateTo
                                    if (new CommonFunction().isOracleDB()) {
                                        param.put(SystemConstants.CRITERIA.DATE_CRITERIA + getSearchFieldsDbName().get(index),
                                                ">= to_date('" + Formatter.formatDate(DateUtil.getDate(tempDateData, getText("date_default_date_dr")), "yyyy-MM-dd") + "'_comma_ 'YYYY-MM-DD')");
                                    } else {
                                        param.put(SystemConstants.CRITERIA.DATE_CRITERIA + getSearchFieldsDbName().get(index),
                                                ">= " + Formatter.formatDate(DateUtil.getDate(tempDateData, getText("date_default_date_dr")), "yyyy-MM-dd"));
                                    }
                                } else { //with DateTo
                                    if (new CommonFunction().isOracleDB()) {
                                        param.put(SystemConstants.CRITERIA.DATE_CRITERIA + getSearchFieldsDbName().get(index),
                                                ">= to_date('" + Formatter.formatDate(DateUtil.getDate(tempDateData, getText("date_default_date_dr")), "yyyy-MM-dd") + "'_comma_ 'YYYY-MM-DD'), "
                                                + "< to_date('" + paramData + "'_comma_ 'YYYY-MM-DD')");
                                    } else {
                                        param.put(SystemConstants.CRITERIA.DATE_CRITERIA + getSearchFieldsDbName().get(index),
                                                ">= " + Formatter.formatDate(DateUtil.getDate(tempDateData, getText("date_default_date_dr")), "yyyy-MM-dd") + ", "
                                                + "< " + paramData);
                                    }
                                }
                            } else {
                                if (!Validator.isEmpty(paramData)) { // only with DateTo
                                    if (new CommonFunction().isOracleDB()) {
                                        param.put(SystemConstants.CRITERIA.DATE_CRITERIA + getSearchFieldsDbName().get(index),
                                                "< to_date('" + paramData + "'_comma_ 'YYYY-MM-DD')");
                                    } else {
                                        param.put(SystemConstants.CRITERIA.DATE_CRITERIA + getSearchFieldsDbName().get(index),
                                                "< " + paramData);
                                    }
                                }
                            }
    //                        paramData = request.getParameter("search_" + (String) str + "To");
    //                        if (!Validator.isEmpty(paramData)) {
    //                            //reportParams += "search_" + str + "To=" + paramData + "&" ;
    //                            param.put(str + "To", DateUtil.getDate(paramData, getText("date_default_date_dr")));
    //                        }
                        } else {
                            str = extractSearchFieldForDate((String) str);
                            tempDateData = request.getParameter("search_" + (String) str);
                            if (tempDateData == null) tempDateData = "";
                            //keep the searchDateData : start
                            getSearchFieldsDateData().put("search_" + str, tempDateData);
                            if (searchCondition.equals("")) {
                                searchCondition = "search_" + (String) str + "=" + tempDateData.toString();
                            } else {
                                searchCondition += "&search_" + (String) str + "=" + tempDateData.toString();
                            }
                            //keep the searchDateData : end
                            if (!Validator.isEmpty(tempDateData)) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(DateUtil.getDate(tempDateData, getText("date_default_date_dr")));
                                tempDateData = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd");
                                cal.add(Calendar.DAY_OF_MONTH, 1);
                                paramData = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd");
                                if (new CommonFunction().isOracleDB()) {
                                    param.put(SystemConstants.CRITERIA.DATE_CRITERIA + getSearchFieldsDbName().get(index),
                                            ">= to_date('" + tempDateData + "'_comma_ 'YYYY-MM-DD'), "
                                            + "< to_date('" + paramData + "'_comma_ 'YYYY-MM-DD')");
                                } else {
                                    param.put(SystemConstants.CRITERIA.NO_CHANGE + getSearchFieldsDbName().get(index),
                                            ">= " + tempDateData + ", "
                                            + "< " + paramData);
                                }
                            }
                        }
                        index++;
                        continue;
                    }
                    paramData = request.getParameter("search_"+(String)str);
                    System.out.println("paramData " + paramData);
                    if (!Validator.isEmpty(paramData)){
                        param.put(getSearchFieldsDbName().get(index), new String(paramData));
                        if (!isDate) {
                            System.out.println("searchCondition = " + searchCondition);
                            if (searchCondition.equals("")){
                                searchCondition = "search_" + (String)str +"="+paramData.toString();
                            } else {
                                searchCondition += "&search_" + (String)str +"="+paramData.toString();
                            }
                        }
                    }
                    getSearchFieldsData().set(index, paramData);
                    index++;
                }
                
                System.out.println("showHideLookupMoreField_ " + showHideLookupMoreField_);
                if (showHideLookupMoreField_.equals("S")) {
                    index = 0;
                    for (Object str : getMoreSearchFields()){
                        if (((String) str).startsWith("_date_")) {
                            String tempDateData = null;
                            if (((String) str).endsWith("_fromTo")) {
                                str = extractSearchFieldForDate((String) str);
                                tempDateData = request.getParameter("search_" + (String) str + "From");
                                paramData = request.getParameter("search_" + (String) str + "To");
                                if (Validator.isEmpty(tempDateData)) {
                                    tempDateData = "";
                                }
                                if (Validator.isEmpty(paramData)) {
                                    paramData = "";
                                }

                                //keep the searchData : start
                                getSearchFieldsMap().put("search_" + str + "From", tempDateData);
                                getSearchFieldsDateData().put("search_" + str + "From", tempDateData);
                                getSearchFieldsMap().put("search_" + str + "To", paramData);
                                getSearchFieldsDateData().put("search_" + str + "To", paramData);
                                if (searchCondition.equals("")) {
                                    searchCondition = "search_" + (String) str + "From=" + tempDateData.toString();
                                    searchCondition += "&search_" + (String) str + "To=" + paramData.toString();
                                } else {
                                    searchCondition += "&search_" + (String) str + "From=" + tempDateData.toString();
                                    searchCondition += "&search_" + (String) str + "To=" + paramData.toString();
                                }
                                //keep the searchData : end

                                if (!Validator.isEmpty(paramData)) { // format the DateTo here... (1 day after selected date)
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(DateUtil.getDate(paramData, getText("date_default_date_dr")));
                                    cal.add(Calendar.DAY_OF_MONTH, 1);
                                    paramData = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd");
                                }
                                if (!Validator.isEmpty(tempDateData)) {
                                    //reportParams += "search_" + str + "From=" + paramData + "&" ;
                                    if (Validator.isEmpty(paramData)) { // no DateTo
                                        if (new CommonFunction().isOracleDB()) {
                                            param.put(SystemConstants.CRITERIA.DATE_CRITERIA + getMoreSearchFieldsDbName().get(index),
                                                    ">= to_date('" + Formatter.formatDate(DateUtil.getDate(tempDateData, getText("date_default_date_dr")), "yyyy-MM-dd") + "'_comma_ 'YYYY-MM-DD')");
                                        } else {
                                            param.put(SystemConstants.CRITERIA.DATE_CRITERIA + getMoreSearchFieldsDbName().get(index),
                                                    ">= " + Formatter.formatDate(DateUtil.getDate(tempDateData, getText("date_default_date_dr")), "yyyy-MM-dd"));
                                        }
                                    } else { //with DateTo
                                        if (new CommonFunction().isOracleDB()) {
                                            param.put(SystemConstants.CRITERIA.DATE_CRITERIA + getMoreSearchFieldsDbName().get(index),
                                                    ">= to_date('" + Formatter.formatDate(DateUtil.getDate(tempDateData, getText("date_default_date_dr")), "yyyy-MM-dd") + "'_comma_ 'YYYY-MM-DD'), "
                                                    + "< to_date('" + paramData + "'_comma_ 'YYYY-MM-DD')");
                                        } else {
                                            param.put(SystemConstants.CRITERIA.DATE_CRITERIA + getMoreSearchFieldsDbName().get(index),
                                                    ">= " + Formatter.formatDate(DateUtil.getDate(tempDateData, getText("date_default_date_dr")), "yyyy-MM-dd") + ", "
                                                    + "< " + paramData);
                                        }
                                    }
                                } else {
                                    if (!Validator.isEmpty(paramData)) { // only with DateTo
                                        if (new CommonFunction().isOracleDB()) {
                                            param.put(SystemConstants.CRITERIA.DATE_CRITERIA + getMoreSearchFieldsDbName().get(index),
                                                    "< to_date('" + paramData + "'_comma_ 'YYYY-MM-DD')");
                                        } else {
                                            param.put(SystemConstants.CRITERIA.DATE_CRITERIA + getMoreSearchFieldsDbName().get(index),
                                                    "< " + paramData);
                                        }
                                    }
                                }
        //                        paramData = request.getParameter("search_" + (String) str + "To");
        //                        if (!Validator.isEmpty(paramData)) {
        //                            //reportParams += "search_" + str + "To=" + paramData + "&" ;
        //                            param.put(str + "To", DateUtil.getDate(paramData, getText("date_default_date_dr")));
        //                        }
                            } else {
                                str = extractSearchFieldForDate((String) str);
                                tempDateData = request.getParameter("search_" + (String) str);
                                if (tempDateData == null) tempDateData = "";
                                //keep the searchDateData : start
                                getSearchFieldsMap().put("search_" + str, tempDateData);
                                getSearchFieldsDateData().put("search_" + str, tempDateData);
                                if (searchCondition.equals("")) {
                                    searchCondition = "search_" + (String) str + "=" + tempDateData.toString();
                                } else {
                                    searchCondition += "&search_" + (String) str + "=" + tempDateData.toString();
                                }
                                //keep the searchDateData : end
                                if (!Validator.isEmpty(tempDateData)) {
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(DateUtil.getDate(tempDateData, getText("date_default_date_dr")));
                                    tempDateData = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd");
                                    cal.add(Calendar.DAY_OF_MONTH, 1);
                                    paramData = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd");
                                    if (new CommonFunction().isOracleDB()) {
                                        param.put(SystemConstants.CRITERIA.DATE_CRITERIA + getMoreSearchFieldsDbName().get(index),
                                                ">= to_date('" + tempDateData + "'_comma_ 'YYYY-MM-DD'), "
                                                + "< to_date('" + paramData + "'_comma_ 'YYYY-MM-DD')");
                                    } else {
                                        param.put(SystemConstants.CRITERIA.NO_CHANGE + getMoreSearchFieldsDbName().get(index),
                                                ">= " + tempDateData + ", "
                                                + "< " + paramData);
                                    }
                                }
                            }
                            index++;
                            continue;
                        }
                        paramData = request.getParameter("search_"+(String)str);
                        System.out.println("paramData" + paramData);
                        if (!Validator.isEmpty(paramData)){
                            param.put(getMoreSearchFieldsDbName().get(index), new String(paramData));
                            if (searchCondition.equals("")){
                                searchCondition = "search_" + (String)str +"="+paramData.toString();
                            } else {
                                searchCondition += "&search_" + (String)str +"="+paramData.toString();
                            }
                        }
                        getSearchFieldsMap().put("search_" + getMoreSearchFields().get(index), paramData);
                        getSearchFieldsData().set(index, paramData);
                        index++;
                    }
                }
                System.out.println("request.getParameter(\"dynamicSortBy\") " + request.getParameter("dynamicSortBy"));
                if (!Validator.isEmpty(request.getParameter("dynamicSortBy"))){
                    if (searchCondition.equals("")){
                        searchCondition = "dynamicSortBy=" + request.getParameter("dynamicSortBy");
                    } else {
                        searchCondition += "&dynamicSortBy=" + request.getParameter("dynamicSortBy");
                    }
                    searchCondition += "&dynamicSortOrder=" + request.getParameter("dynamicSortOrder");
                    setSqlOrderBy(getDynamicSortBy_condition() + " " + (request.getParameter("dynamicSortOrder").equals("A")?"asc":"desc"));
                } else {
                    if (sortingFields == null || sortingFields.size() <= 0) {
                        setSqlOrderBy(" 2 asc ");
                    } else {
                        setSqlOrderBy(sortingFields.get(0) + " asc ");
                        setDynamicSortBy(sortingFields.get(0));
                    }
                    setDynamicSortOrder("A");
                }
                if (requireRelogin) {
                    Map session = ActionContext.getContext().getSession();
                    String temp = null;
                    for (String paramKey : request.getParameterMap().keySet()) {
                        if (temp == null) {
                            temp = "?"+paramKey+"="+request.getParameter(paramKey);
                        } else {
                            temp += "&"+paramKey+"="+request.getParameter(paramKey);
                        }
                    }
                    session.put("lookupRefererUrl_", request.getRequestURI().substring(request.getRequestURI().indexOf("/", 2))+temp);
                    session.put("lookupDescription", getFinalLookupDesc());
                    return "login";
                }
                
                System.out.println("param " + param);
                populateLookupFilterParam(param);
                setSearchedParam(param);
                checkPreSearch(param);
                new DataRetriever().retrieveData_useSetup(this, request, request.getSession().getServletContext());
                System.out.println("result " + result);
                if (result.size() > 0) {
                    Map map = (Map)result.get(0);
                    columns.addAll(map.keySet());
                }
            } else {
                
                new DataRetriever().retrieveData(this, request, request.getSession().getServletContext());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            new LogFunction().logError(this.getClass(), "", e);
        }
        return SUCCESS;
//        String returnStr = SUCCESS;
//        Map session = ActionContext.getContext().getSession();
//        if (session.get("login_user_type") != null && session.get("login_user_type").equals(SystemConstants.SF_SYSTEM_TYPE.PUBLIC)) {
//            returnStr = "success_el";
//        } else if (session.get("login_user_type") != null && session.get("login_user_type").equals(SystemConstants.SF_SYSTEM_TYPE.INTERNAL)) {
//            returnStr = SUCCESS;
//        }
//        return returnStr;
    }

    private void populateLookupFilterParam(Map param){
        String newFilter = "";
        String newFilterBy = "";
        try {
            if (filter == null || filter.equals("null")) return;
            String[] filterData = filter.split(",,,");
            if (filterData.length == 1){
                if (!Validator.isEmpty(filter)) {
                    if (!Validator.isEmpty(filterBy.split(",")[0])){
                        if (lookupFiltersOperator.containsKey(getFinalFilterByName(filterBy.split(",")[0]))){
                            param.put(getLookupFilters().get(getFinalFilterByName(filterBy.split(",")[0])), lookupFiltersOperator.get(getFinalFilterByName(filterBy.split(",")[0]))+filterData[0]);
                        } else {
                            param.put(getLookupFilters().get(getFinalFilterByName(filterBy.split(",")[0])), filterData[0]);
                        }
                    }
                    if (filterBy.split(",")[0].indexOf("firstFilter_") == 0){
                        filter = "";
                        filterBy = "";
                    }
                }
            } else {
                int index = 0;
                for (String fData : filterData){
                    if (!Validator.isEmpty(fData)){
                        if (lookupFiltersOperator.containsKey(getFinalFilterByName(filterBy.split(",")[index]))){
                            param.put(getLookupFilters().get(getFinalFilterByName(filterBy.split(",")[index])), lookupFiltersOperator.get(getFinalFilterByName(filterBy.split(",")[index]))+fData);
                        } else {
                            param.put(getLookupFilters().get(getFinalFilterByName(filterBy.split(",")[index])), fData);
                        }
                    }
                    if (filterBy.split(",")[index].indexOf("firstFilter_") < 0){
                        if (newFilter.equals("")){
                            newFilter = fData;
                            newFilterBy = filterBy.split(",")[index];
                        } else {
                            newFilter += ",,," + fData;
                            newFilterBy += ","+filterBy.split(",")[index];
                        }

                    }
                    index++;
                }
                filterBy = newFilterBy;
                filter = newFilter;
            }
        } catch (Exception e){}
    }

    private String getFinalFilterByName(String startName){
        if (startName.indexOf("_as_") > 0) {
            return startName.substring(startName.indexOf("_as_") + 4).trim();
        }
        return startName.trim();
    }

    public String getLookup() {
        if (Validator.isEmpty(lookup)) {
            return null;
        } else {
            return lookup;
        }
    }

    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    public String getFinalLookupDesc() {
        if (Validator.isEmpty(lookupDesc)) {
            return getText("lookup.search") + " " + getText((String)setupMap.get("searchDescription"));
        }
        return lookupDesc;
    }
    
    public String getLookupDesc() {
        return lookupDesc;
    }

    public void setLookupDesc(String lookupDesc) {
        this.lookupDesc = lookupDesc;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLookFor() {
        return lookFor;
    }

    public void setLookFor(String lookFor) {
        this.lookFor = lookFor;
    }

    public String getWriteTo() {
        return writeTo;
    }

    public void setWriteTo(String writeTo) {
        this.writeTo = writeTo;
    }

    public String getDisplayedColumns() {
        return displayedColumns;
    }

    public void setDisplayedColumns(String displayedColumns) {
        this.displayedColumns = displayedColumns;
    }

    public String getFocusOn() {
        return focusOn;
    }

    public void setFocusOn(String focusOn) {
        this.focusOn = focusOn;
    }

    public String getRetrieveOnLoad() {
        return retrieveOnLoad;
    }

    public void setRetrieveOnLoad(String retrieveOnLoad) {
        this.retrieveOnLoad = retrieveOnLoad;
    }

    public Map getCriterias() {
        return criterias;
    }

    public void setCriterias(Map criterias) {
        this.criterias = criterias;
    }

    public List getFields() {
        return fields;
    }

    public void setFields(List fields) {
        this.fields = fields;
    }

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

    public List getDisplayingColumns() {
        return displayingColumns;
    }

    public boolean isSearched() {
        return searched;
    }

    public void setSearched(boolean searched) {
        this.searched = searched;
    }

    public List getGoodDisplayingColumns() {
        return goodDisplayingColumns;
    }

    public void setGoodDisplayingColumns(List goodDisplayingColumns) {
        this.goodDisplayingColumns = goodDisplayingColumns;
    }

    public Integer getPageNo() {
        if (pageNo == null) pageNo = 1;
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(Integer numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public Integer getPageSize() {
        if (pageSize == null) pageSize = 10; //change the default page size here
        return pageSize;
//        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        System.out.println("setting pagesize to " + pageSize);
        this.pageSize = pageSize;
    }

    public Map getPagingItem() {
        return PageUtil.getPagingItem(getPageNo(), getPageSize(), getNumberOfRows(), pageItem);
        //return PageUtil.getPagingItem(getPageNo(), getPageSize(), getNumberOfRows());
    }

    public String getPagingURL() {
        return "gotoPageLookup?" + searchCondition + "&lookupDesc="+lookupDesc + "&showHideLookupMoreField_="+showHideLookupMoreField_+"&pageSize="+pageSize;
    }

    public Map getDynamicActionSetup(String action) throws Exception {
        try {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            Map map = new CommonFunction().getDynamicConfiguration(action, request, request.getSession().getServletContext(), "");
            if (map.get("lookupRequireLogin") != null) {
                lookupRequireLogin = "true".equalsIgnoreCase((String)map.get("lookupRequireLogin"));
            }
            if (lookupRequireLogin) {
                Map session = ActionContext.getContext().getSession();
                if (!session.containsKey("logined")) {
                    requireRelogin = Boolean.TRUE;
                }
            }
            String required = (String) map.get("pageRequired");
            int count = 1;
            if (required != null) {
                for (String req : required.split(",")) {
                    String[] reqArr = req.split(";");
                    pageRequired += "this.a" + count++ + " = new Array('search_" + reqArr[0].trim() + "', '" + getText(reqArr[1]) + "'); " + "\r\n";
                }
            }
            if (map.get("lookupAccessRightChecking") != null) {
                setPreLookupSearchMethod((String)map.get("lookupAccessRightChecking"));
                checkPreSearch(map);
                setPreLookupSearchMethod(null);//
            }
            return map;
        } catch (Exception e) {
            throw e;
            // TODO Auto-generated catch block
//            new LogFunction().logError(this.getClass(), "", e);
        }
    }
    
    private String pageRequired = "";
    public String getPageRequired() {
        return pageRequired;
    }
    
    public Boolean isRequiredField(String pageField) {
        Boolean isDateFromTo = Boolean.FALSE;
        if (pageField.startsWith("_date_")) {
            pageField = pageField.substring(6);
            if (pageField.endsWith("_fromTo")) {
                isDateFromTo = Boolean.TRUE;
                pageField = pageField.substring(0, pageField.length() - 7);
            }
        }
        Debug.printFrameworkDebug("isRequiredField called... pageField = " + pageField);
        String required = null;
        required = (String) getSetupMap().get("pageRequired");
        if (required != null) {
            String[] requiredArr = required.split(",");
            for (String str : requiredArr) {
                if (isDateFromTo) {
                    if (str.split(";")[0].trim().equals(pageField + "From") || str.split(";")[0].trim().equals(pageField + "To")) {
                        return Boolean.TRUE;
                    }
                } else if (str.split(";")[0].trim().equals(pageField)) {
                    return Boolean.TRUE;
                }
            }
        }

        return Boolean.FALSE;
    }

    public String getShowHideLookupMoreField_() {
        return showHideLookupMoreField_;
    }

    public void setShowHideLookupMoreField_(String showHideLookupMoreField_) {
        this.showHideLookupMoreField_ = showHideLookupMoreField_;
    }

    public Boolean getHasMoreSearchField() {
        return hasMoreSearchField;
    }

    public void setHasMoreSearchField(Boolean hasMoreSearchField) {
        this.hasMoreSearchField = hasMoreSearchField;
    }

    public List<String> getMoreSearchFieldsDbName() {
        return moreSearchFieldsDbName;
    }

    public void setMoreSearchFieldsDbName(List<String> moreSearchFieldsDbName) {
        this.moreSearchFieldsDbName = moreSearchFieldsDbName;
    }

    public List<String> getMoreSearchFields() {
        return moreSearchFields;
    }

    public void setMoreSearchFields(List<String> moreSearchFields) {
        this.moreSearchFields = moreSearchFields;
    }

    public Map getSearchFieldsDataMap() {
        return searchFieldsDataMap;
    }

    public void setSearchFieldsDataMap(Map searchFieldsDataMap) {
        this.searchFieldsDataMap = searchFieldsDataMap;
    }

    public Map<String, String> getSearchFieldsMap() {
        return searchFieldsMap;
    }

    public void setSearchFieldsMap(Map<String, String> searchFieldsMap) {
        this.searchFieldsMap = searchFieldsMap;
    }

    public List<String> getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(List searchFields) {
        this.searchFields = searchFields;
    }

    public List<String> getSearchFieldsLabel() {
        return searchFieldsLabel;
    }

    public void setSearchFieldsLabel(List searchFieldsLabel) {
        this.searchFieldsLabel = searchFieldsLabel;
    }

    public Map getCriteriasData() {
        return criteriasData;
    }

    public void setCriteriasData(Map criteriasData) {
        this.criteriasData = criteriasData;
    }

    public String getData(String name) {
        if (criteriasData != null) {
            return (String) criteriasData.get(name);
        }
        return "";
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPropertyText(String prop) {
        return getText(prop);
    }

    public boolean isPaging() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public String getRetrievingColumns() {
        return retrievingColumns;
    }

    public void setRetrievingColumns(String retrievingColumns) {
        this.retrievingColumns = retrievingColumns;
    }

    public Map getRetrievingColumnsLabel() {
        return retrievingColumnsLabel;
    }

    public void setRetrievingColumnsLabel(String retrievingColumnsLabel) {
        if (retrievingColumnsLabel != null){
            String[] columnLabel = null;
            for (String labels : retrievingColumnsLabel.split(",")){
                columnLabel = labels.split(";");
                this.retrievingColumnsLabel.put(columnLabel[0].trim(), getText(columnLabel[1].trim()));
            }
        }
        if (!Validator.isEmpty(displayedColumns)) {
            StringTokenizer st = new StringTokenizer(this.displayedColumns, ",");
            while (st.hasMoreTokens()) {
                String displayedColumn = st.nextToken().trim();

                if (setupMap.get("decFormat_"+displayedColumn) != null){
                    searchFieldFormat.put("decFormat_"+displayedColumn, setupMap.get("decFormat_"+displayedColumn));
                }
                if (setupMap.get("propertyText_"+displayedColumn) != null){
                    searchFieldFormat.put("propertyText_"+displayedColumn, setupMap.get("propertyText_"+displayedColumn));
                }
                if (setupMap.get("functionText_"+displayedColumn) != null){
                    searchFieldFormat.put("functionText_"+displayedColumn, setupMap.get("functionText_"+displayedColumn));
                }
                if (setupMap.get("styleFormat_"+displayedColumn) != null){
                    searchFieldStyleFormat.put("styleFormat_"+displayedColumn, setupMap.get("styleFormat_"+displayedColumn));
                }

                if (getRetrievingColumnsLabel().containsKey(displayedColumn)){
                    displayingColumns.add(displayedColumn.toLowerCase());
                    goodDisplayingColumns.add(getRetrievingColumnsLabel().get(displayedColumn));
                } else {
                    displayingColumns.add(displayedColumn.toLowerCase());
                    goodDisplayingColumns.add(Formatter.initcap(displayedColumn.replaceAll("_", " ")));
                }
            }
        }
        //this.retrievingColumnsLabel = retrievingColumnsLabel;
    }

    public String getSqlTables() {
        return sqlTables;
    }

    public void setSqlTables(String sqlTables) {
        this.sqlTables = sqlTables;
    }

    public Map getSetupMap() {
        return setupMap;
    }

    public void setSetupMap(Map setupMap) {
        this.setupMap = setupMap;
    }

    public Map<String, Object> getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(Map<String, Object> searchParam) {
        this.searchParam = searchParam;
    }

    public List getSearchFieldsDbName() {
        return searchFieldsDbName;
    }

    public void setSearchFieldsDbName(List searchFieldsDbName) {
        this.searchFieldsDbName = searchFieldsDbName;
    }

    public List getSearchFieldsData() {
        return searchFieldsData;
    }

    public void setSearchFieldsData(List searchFieldsData) {
        this.searchFieldsData = searchFieldsData;
    }

    public String getSqlOrderBy() {
        return sqlOrderBy;
    }

    public void setSqlOrderBy(String sqlOrderBy) {
        this.sqlOrderBy = sqlOrderBy;
    }

    public String getDcGroupBy() {
        return dcGroupBy;
    }

    public void setDcGroupBy(String dcGroupBy) {
        this.dcGroupBy = dcGroupBy;
    }

    public Map getSearchedParam() {
        if (searchedParam != null) {
            searchedParam.remove("ignore");
        }
        return searchedParam;
    }

    public void setSearchedParam(Map searchedParam) {
        this.searchedParam = searchedParam;
    }

    public Map getSearchFieldDD() {
        return searchFieldDD;
    }

    public void setSearchFieldDD(Map searchFieldDD) {
        this.searchFieldDD = searchFieldDD;
    }

    protected List getListFromSetup(String setup){
        Object returnObject = null;
        String[] setupArr = setup.split(";");
        try {
            Class c = Class.forName(setupArr[0]);
            Method m = c.getDeclaredMethod(setupArr[1]);
            Object i;
            try {
                i = c.getDeclaredConstructor(org.hibernate.Session.class).newInstance(baseDAO.getSession());
            } catch (Exception e){
                i = c.getDeclaredConstructor().newInstance();
            }
            returnObject = m.invoke(i);
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }
        if (returnObject != null){
//            if (setupArr[2].equalsIgnoreCase("pubcode") ){
//                CommonList.addBlankPubocde((List)returnObject, "", getText("all"));
//            } else 
            if (setupArr[2].equalsIgnoreCase("option") ){
                CommonList.addBlankOption((List)returnObject, "", getText("all"));
            }
        }
        return (List)returnObject;
    }

    public List getSearchDDList(String searchField){
        Debug.printFrameworkDebug("searchField = " + searchField);
        return (List)getSearchFieldDD().get(searchField);
    }

    public String retrieveData(){
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        lookup = request.getParameter("_loadingConfig");
        lookupSearchFieldData_ = request.getParameter("_searchFieldData");
        lookupSearchFieldId_ = request.getParameter("_searchFieldId");
        if (lookup.indexOf(";") > 0) {
            String[] params = lookup.split(";");
            lookup = params[0];
            for (String paramStr : params) {
                if (paramStr.equalsIgnoreCase("ne")) { // No Show Error
                    noErrorMsg = Boolean.TRUE;
                } else if (paramStr.equalsIgnoreCase("nc")) { // No Clear Data
                    noClearData = Boolean.TRUE;
                } else if (paramStr.startsWith("ssc:")) { //server side checking
                    serverSideCheck = Boolean.TRUE;
                    serverSideCheckCode = paramStr.substring(4);
                }
            }
        }
        if (serverSideCheck){
            Boolean methodExists = Boolean.FALSE;
            LookupCheckTrigger lookupCheckTrigger = new LookupCheckTrigger();
            Method m = null;
            try {
                m = lookupCheckTrigger.getClass().getMethod(lookupCheckTrigger.getLookupMethod(serverSideCheckCode), LookupAction.class);
                methodExists = true;
            } catch (Exception e){}

            if (methodExists){
                try {
                    m.invoke(lookupCheckTrigger, this);
                    return "retrieveData";
                } catch (Exception e){
                    invokedMsg_ = "Calculation failed.";
                }
            } else {
                Debug.printFrameworkDebug("Method \""+ lookupCheckTrigger.getLookupMethod(serverSideCheckCode) +"\" do not exists");
            }
        }
        Debug.printFrameworkDebug("serverSideCheckCode = " + serverSideCheckCode);
        try {
            if (lookup.startsWith("useManual_")) {
                String[] manualConfigArr = lookup.substring(10).split(":");
                Class c = Class.forName(getText("ajax_dataRetriever." + manualConfigArr[0]));
                Method m = c.getDeclaredMethod(getText("ajax_dataRetriever." + manualConfigArr[0]+"."+manualConfigArr[1]), LookupAction.class);
                Object i = c.newInstance();
                m.invoke(i, this);
                invokedMsg_ = "Walau ehh...";
            } else {
                loadDynamicSearch_forDataRetriever();
                Map param = new HashMap();
                int index = 0;

//                for (int count = 0; count < 25000; count++){
//                    System.out.println(request.getParameter("_searchFieldId"));
//                }

                for (Object str : getSearchFields()){
                    if (request.getParameter("_searchField").equals(str)) {
                        //String paramData = request.getParameter("_searchFieldData");
                        if (!Validator.isEmpty(lookupSearchFieldData_)){
                            param.put(SystemConstants.CRITERIA.NO_CHANGE_LOWER + getSearchFieldsDbName().get(index), lookupSearchFieldData_);
                            break;
                        }
                    }
                    index++;
                }
                populateLookupFilterParam(param);
                //the code below is to add more filter condition base on other data selected.
    //            if (!Validator.isEmpty(request.getParameter("_filterBy"))){
    //                for (Object str : getSearchFields()){
    //                    if (request.getParameter("_filterBy").equals(str)) {
    //                        String paramData = request.getParameter("_searchFieldData");
    //                        if (!Validator.isEmpty(paramData)){
    //                            param.put(SystemConstants.CRITERIA.NO_CHANGE_LOWER + getSearchFieldsDbName().get(index), request.getParameter("_searchFieldData"));
    //                            break;
    //                        }
    //                    }
    //                    index++;
    //                }
    //            }
                setSearchedParam(param);
                checkPreSearch(getSearchedParam());
                if (sortingFields == null || sortingFields.size() <= 0) {
                    setSqlOrderBy(" 2 asc ");
                } else {
                    setSqlOrderBy(sortingFields.get(0) + " asc ");
                }
                new DataRetriever().retrieveData_useSetup(this, request, request.getSession().getServletContext());
                String[] writeToArray = request.getParameter("_writeTo").split(",");
                if (getResult() != null && getResult().size() == 1){
                    String[] lookForArray = request.getParameter("_lookFor").split(",");
                    Map map = (Map)getResult().get(0);
                    index = 0;
                    for (String str : writeToArray){
                        writeToList.add(str.trim());
                        writeToDataList.add(map.get(lookForArray[index++].trim()));
                    }
                } else { // if not only 1 record found...
                    if (getResult().size() > 1) {
                        invokedMsg_ = "More than 1 record found.";
                    }
                    setResult(new ArrayList());
                    if (noClearData) {
                        writeToDataList = getResult(); //reuse result, empty ArrayList()
                        writeToList = getResult(); //reuse result, empty ArrayList()
                        Debug.printFrameworkDebug("writeToList.size = " + writeToList.size());

                    } else {
                        for (String str : writeToArray){
                            writeToList.add(str.trim());
                            writeToDataList.add("");
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (!noClearData) {
                if (writeToList == null || writeToList.size() <= 0) {
                    String[] writeToArray = request.getParameter("_writeTo").split(",");
                    for (String str : writeToArray){
                        writeToList.add(str.trim());
                        writeToDataList.add("");
                    }
                }
            }
        }
        return "retrieveData";
    }

    public void loadDynamicSearch_forDataRetriever() throws Exception {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String setupName = lookup.substring(9);
        Map setupMap = new HashMap();
        setupMap = getDynamicActionSetup(setupName);
        setupMap = checkConfig(setupMap);
        setSetupMap(setupMap);
        setRetrievingColumnsLabel((String)setupMap.get("lookupRetrievingColumnsLabel"));
        StringTokenizer st = new StringTokenizer(setupMap.get("searchFields").toString(), ",");
        StringTokenizer st2 = new StringTokenizer(setupMap.get("searchFieldsDbName").toString(), ",");
        String searchField = null;
        String[] splitArr = null;
        while (st.hasMoreTokens()) {
            searchField = st.nextToken().trim();
            getSearchFields().add(searchField);
            getSearchFieldsDbName().add(st2.nextToken().trim());
            getSearchFieldsData().add("");
        }
        if (setupMap.get("lookupFilters") != null){
            st = new StringTokenizer(setupMap.get("lookupFilters").toString(), ",");
            while (st.hasMoreTokens()) {
                splitArr = st.nextToken().trim().split(";");
                getLookupFilters().put(splitArr[0].trim(), splitArr[1].trim());
            }
        }
        setRetrievingColumns(setupMap.get("lookupRetrievingColumns").toString());
        setSqlTables(setupMap.get("sqlTables").toString());
    }
    
    private String populateSelectNodeValue(Object object){
        Map map = new HashMap();
        for (String str : hierarchyLookupColumns.split(",")){
            map.put(str.split(";")[0].trim(), str.split(";")[1].trim());
        }
        String temp = "";
        String strTemp = null;
        for (String str : lookFor.split(",")){
            if (map.get(str) == null) {
                strTemp = "";
            } else {
                strTemp = map.get(str).toString();
            }
            if (temp.equals("")){
                Debug.printFrameworkDebug("object = " + object);
                Debug.printFrameworkDebug("strTemp = " + strTemp);
                String value = getMethodValueFromObject(object, strTemp);
                if (value != null) {
                    temp = "\"" + value.replace("\\", "$backslash") +"\"";
                } else {
                    temp = "\"\"";
                }
            } else {
                temp += ", \"" + getMethodValueFromObject(object, strTemp).replace("\\", "$backslash") +"\"";
            }
        }
        return temp;
    }

    private int loopTree2(List pEntityList) {
        if (pEntityList != null)
            for (Object object : pEntityList) {
                entityTree += "<li style='font-size: 8pt;'><a style='text-decoration: none' href='#' onclick='selectNode("+ populateSelectNodeValue(object) +")'>"+ getMethodValueFromObject(object, hierarchyLookupDisplayColumn) +"</a>\n";
                List list = getChildListFromObject(object, hierarchyLookupGetChildMethod);
                if (list != null && list.size() > 0) {
                    entityTree += "<ul>";
                    loopTree2(list);
                    entityTree += "</ul>";
                }
            }
        return 0;
    }

//    public String testLoop(){
//        Map param = new HashMap();
//        param.put("module_type", "M");
//
//        List list = new BaseDAOImpl<Module>().list(param, new Module());
//        entityTree = "<div id='main'>" +
//                "<div id='sidetree'>"+
//                "<div class='treeheader'>&nbsp;</div>"+
//                "<div id='sidetreecontrol'><a href='?#'>Collapse All</a> | <a href='?#'>Expand All</a></div>" +
//                "<ul id='tree'>";
//        loopTree2(list);
//        entityTree += "</ul></div></div>";
//        return SUCCESS + "Tree";
//    }

    private String lookForParam = "";

    public String getLookForParam() {
        return lookForParam;
    }

    public void setLookForParam(String lookForParam) {
        this.lookForParam = lookForParam;
    }
    
    private String updateParentString = "";

    public String getUpdateParentString() {
        return updateParentString;
    }

    public void setUpdateParentString(String updateParentString) {
        this.updateParentString = updateParentString;
    }

    private void revalidateParam(Map<String, String> param) {
        if (param != null) {
            List<String> removeKey = new ArrayList();
            Map newParam = new HashMap();
            if (param.keySet() != null) {
                for (String objKey : param.keySet()){
                    if (objKey != null && objKey.startsWith(SystemConstants.HQL.JOIN)){
                        removeKey.add(objKey);
                        String data = param.get(objKey);
                        String newKey = objKey.substring(6);
                        newParam.put(SystemConstants.HQL.JOIN, newKey.split("___")[0]);
                        newParam.put(newKey.split("___")[1], data);
                        //_join_inner join faClass___faClass.class_code
                    }
                }
                for (String key : removeKey) {
                    param.remove(key);
                }
                param.putAll(newParam);
            }
        }
    }

    public String hierarchy() throws Exception {
        setSearched(false);

        if (lookup.indexOf("useSetup_") == 0) {
            try {
                loadDynamicSearch();
            } catch (BaseException be) {
                addActionError(be.getMessage());
                return "lookupError";
            }
        }
        int i = 1;
        for (String str : lookFor.split(",")){
            lookForParam += lookForParam.equals("")? ("D"+i++) : (",D"+i++);
        }
        i=1;
        for (String str : writeTo.split(",")){
            updateParentString += "updateParentForm('"+ str +("', D"+i++) + ");\n    ";
        }

        try {
        Object obj = Class.forName(hierarchyLookupModel).newInstance();
        List list = null;
        Debug.printFrameworkDebug("hierarchyLookupFilter = " + hierarchyLookupFilter);
        populateLookupFilterParam(hierarchyLookupFilter);
        revalidateParam(hierarchyLookupFilter);
        Debug.printFrameworkDebug("hierarchyLookupFilter = " + hierarchyLookupFilter);
        if (hierarchyLookupOrderBy != null){
            list = baseDAO.list_order(hierarchyLookupFilter, obj.getClass(), true, hierarchyLookupOrderBy);
        } else {
            list = baseDAO.list(hierarchyLookupFilter, obj.getClass(), true);
        }

           entityTree = "<div id='main'>" +
                "<div id='sidetree'>"+
                "<div class='treeheader'>&nbsp;</div>"+
                "<div id='sidetreecontrol'><a href='?#'>Collapse/Expand All</a></div>" +
                "<ul id='tree'>";
        loopTree2(list);
        entityTree += "</ul></div></div>";
        } catch (Exception e){
            e.printStackTrace();
        }
        return SUCCESS+"Tree";
    }

    public List getWriteToDataList() {
        return writeToDataList;
    }

    public void setWriteToDataList(List writeToDataList) {
        this.writeToDataList = writeToDataList;
    }

    public List getWriteToList() {
        return writeToList;
    }

    public void setWriteToList(List writeToList) {
        this.writeToList = writeToList;
    }

    public String getLookupParentFormId() {
        return lookupParentFormId;
    }

    public void setLookupParentFormId(String _parentFormId) {
        this.lookupParentFormId = _parentFormId;
    }

    public String getLookupSearchFieldData_(){
        return lookupSearchFieldData_;
    }

    public String getLookupSearchFieldId_(){
        return lookupSearchFieldId_;
    }

    public void tryToCallMe(LookupAction la){
        Debug.printFrameworkDebug("i'm called");
        Debug.printFrameworkDebug("this is a sample of manual description retrieving + checking to be done");
    }

    public String getInvokedMsg_() {
        return invokedMsg_;
    }

    public void setInvokedMsg_(String invokedMsg_) {
        this.invokedMsg_ = invokedMsg_;
    }

    public String getFilterBy() {
        return filterBy;
    }

    public void setFilterBy(String filterBy) {
        this.filterBy = filterBy;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Map<String, String> getLookupFilters() {
        return lookupFilters;
    }

    public void setLookupFilters(Map<String, String> lookupFilters) {
        this.lookupFilters = lookupFilters;
    }

    public String getUsePopupCalander() {
        return usePopupCalander;
    }

    public void setUsePopupCalander(String usePopupCalander) {
        this.usePopupCalander = usePopupCalander;
    }

    public Map getSearchFieldFormat() {
        return searchFieldFormat;
    }

    public void setSearchFieldFormat(Map searchFieldFormat) {
        this.searchFieldFormat = searchFieldFormat;
    }

    public Map getSearchFieldStyleFormat() {
        return searchFieldStyleFormat;
    }

    public void setSearchFieldStyleFormat(Map searchFieldStyleFormat) {
        this.searchFieldStyleFormat = searchFieldStyleFormat;
    }

    public String extractSearchFieldForDate(String theDate) {
        if (theDate.endsWith("_fromTo")) {
            return theDate.substring(6, (theDate.length() - 7));
        }
        return theDate.substring(6);
    }

    public Map<String, String> getSearchFieldsDateData() {
        return searchFieldsDateData;
    }
    public void setSearchFieldsDateData(Map searchFieldDateMap) {
        searchFieldsDateData = searchFieldDateMap;
    }

    public String getHc(String actionName, String dynamicConfigName) {
        String str = actionName + dynamicConfigName;
        char ch;
        Integer total = 23; //purposely start at 23
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            total += (int)ch;
        }
        total = ((total * 2011) + 12321);
        return total.toString();
    }

    public String getTeaC_() {
        return teaC_;
    }

    public void setTeaC_(String teaC_) {
        this.teaC_ = teaC_;
    }

    public String getLAN_() {
        return LAN_;
    }

    public void setLAN_(String LAN_) {
        this.LAN_ = LAN_;
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

    public boolean isSortingField(String fieldName) {
        if (sortingFields.contains(fieldName)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public String getSearchCondition() {
        return searchCondition;
    }

    public void setSearchCondition(String searchCondition) {
        this.searchCondition = searchCondition;
    }

//    public Map checkConfig(Map<String, String> setupMap){
//        Map<String, String> extendedMap = null;
//        Boolean extendFound = Boolean.TRUE;
//        if (setupMap.get("extendsConfig") != null){
//            while(extendFound) {
//                extendedMap = getDynamicActionSetup(setupMap.get("extendsConfig").toString());
//                if (extendedMap.get("extendsConfig") == null){
//                    extendFound = Boolean.FALSE;
//                } else {
//                }
//                for (String objKey : setupMap.keySet()){
//                    if (!objKey.equals("extendsConfig")){
//                        extendedMap.remove(objKey);
//                        extendedMap.put(objKey, setupMap.get(objKey));
//                    }
//                }
//                setupMap = extendedMap;
//            }
//        }
//        return setupMap;
//    }
    public Map checkConfig(Map<String, String> setupMap) {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Map<String, String> extendedMap = null;
        Boolean extendFound = Boolean.TRUE;
        if (setupMap.get("extendsConfig") != null && (setupMap.get("extended") == null)) {
            while (extendFound) {
                extendedMap = (Map) ((Map) request.getSession().getServletContext().getAttribute("action")).get(setupMap.get("extendsConfig").toString());
                setupMap.remove("extendsConfig");
                if (extendedMap.get("extendsConfig") == null) {
                    extendFound = Boolean.FALSE;
                } else {
                }
                for (String objKey : extendedMap.keySet()) {
                    if (!setupMap.containsKey(objKey)) {
                        setupMap.put(objKey, extendedMap.get(objKey));
                    }
                }
            }
            setupMap.put("extended", "yes");
        }
// if (setupMap.get("extendsConfig") != null){
// while(extendFound) {
// extendedMap = getDynamicActionSetup(setupMap.get("extendsConfig").toString());
// if (extendedMap.get("extendsConfig") == null){
// extendFound = Boolean.FALSE;
// } else {
// }
// for (String objKey : setupMap.keySet()){
// if (!objKey.equals("extendsConfig")){
// extendedMap.remove(objKey);
// extendedMap.put(objKey, setupMap.get(objKey));
// }
// }
// setupMap = extendedMap;
// }
// }
        return setupMap;
    }

    public String getFirstSearchFieldName() {
        if (Validator.isEmpty(firstSearchFieldName)) {
            if (!getSearchFields().isEmpty()) {
                firstSearchFieldName = "search_" + getSearchFields().get(0);
            }
        }
        return firstSearchFieldName;
    }

    public void setFirstSearchFieldName(String firstSearchFieldName) {
        this.firstSearchFieldName = firstSearchFieldName;
    }

    public Boolean getNoClearData() {
        return noClearData;
    }

    public void setNoClearData(Boolean noClearData) {
        this.noClearData = noClearData;
    }

    public Boolean getNoErrorMsg() {
        return noErrorMsg;
    }

    public void setNoErrorMsg(Boolean noErrorMsg) {
        this.noErrorMsg = noErrorMsg;
    }

    private List getChildListFromObject(Object model, String method){
        try {
            Method m = model.getClass().getMethod(method);
            Object returnObj = m.invoke(model);
            if (returnObj != null){
                return (List)returnObj;
            }
        } catch (Exception e){

        }
        return null;
    }

    private String getMethodValueFromObject(Object model, String method){
        try {
            Method m;
            StringTokenizer st = new StringTokenizer(method, ".");
            String token = "";
            Object obj = null;
            int count = 0;
            while (st.hasMoreTokens()) {
                count++;
                token = st.nextToken().trim();
                if (obj == null) {
                    m = model.getClass().getMethod(token);
                    obj = m.invoke(model);
                } else {
                    m = obj.getClass().getMethod(token);
                    obj = m.invoke(obj);
                }
            }
            if (obj != null){
                return obj.toString();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getEntityTree() {
        return entityTree;
    }

    public void setEntityTree(String entityTree) {
        this.entityTree = entityTree;
    }

    public String getCheckFormatField(String value) {
        if (getSearchFieldFormat().containsKey("decFormat_" + value)) {
            return "true";
        }
        if (getSearchFieldFormat().containsKey("propertyText_" + value)) {
            return "true";
        }
        if (getSearchFieldFormat().containsKey("functionText_" + value)) {
            return "true";
        }
        return "false";
    }

    public String getFormattedField(String value, int row) {
        Object instanceObject = null;
        Class c = null;
        Method m = null;
        Map resultMap = ((Map) getResult().get(row));
        Object returnObj = null;
        if (getSearchFieldFormat().containsKey("decFormat_" + value)) {
            return Formatter.formatDecimal(resultMap.get(value).toString(), getSearchFieldFormat().get("decFormat_" + value).toString());
        }
        if (getSearchFieldFormat().containsKey("propertyText_" + value)) {
            if (getSearchFieldFormat().get("propertyText_" + value).toString().endsWith(".")) {
                return getText(getSearchFieldFormat().get("propertyText_" + value).toString() + resultMap.get(value).toString());
            } else {
                return getText(getSearchFieldFormat().get("propertyText_" + value).toString() + "." + resultMap.get(value).toString());
            }
        }
        if (getSearchFieldFormat().containsKey("functionText_" + value)) {
            String[] functionTextArr = getSearchFieldFormat().get("functionText_" + value).toString().split(";");
            Object[] paramObjectArr = null;
            Class[] paramClassArr = null;
            try {
                int i = 1;
                if (functionTextArr.length > 2) {
                    i = functionTextArr[2].split(":").length + 1;
                }
                if (i == 1) {
                    paramObjectArr = new Object[]{resultMap.get(value).toString()};
                    paramClassArr = new Class[]{String.class};
                } else {
                    paramObjectArr = new Object[i];
                    paramObjectArr[0] = resultMap.get(value).toString();
                    paramClassArr = new Class[i];
                    paramClassArr[0] = String.class;
                    String[] paramTypeArr = functionTextArr[2].split(":");
                    String[] paramValueArr = functionTextArr[3].split(":");
                    for (int idx = 1; idx < i; idx++) {
                        if (paramTypeArr[idx - 1].equalsIgnoreCase("Boolean")) {
                            paramClassArr[idx] = Boolean.class;
                            if (paramValueArr[idx - 1].trim().equalsIgnoreCase("true")) {
                                paramObjectArr[idx] = new Boolean(true);
                            } else if (paramValueArr[idx - 1].trim().equalsIgnoreCase("false")) {
                                paramObjectArr[idx] = new Boolean("False");
                            } else {
                                // need to take from method.
                            }
                        } else {
                            paramObjectArr[idx] = "to be done";
                        }
                    }
                }

                c = Class.forName(functionTextArr[0]);
                m = c.getDeclaredMethod(functionTextArr[1], paramClassArr);
                instanceObject = c.newInstance();
                returnObj = m.invoke(instanceObject, paramObjectArr);

                if (returnObj == null) {
                    return "";
                } else {
                    return returnObj.toString().replace("\r\n", "; ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (instanceObject != null) {
                    try { // try to call closeSession to free connection/session if exists.
                        m = instanceObject.getClass().getMethod("closeSession");
                        m.invoke(instanceObject);
                    } catch (Exception ex) {
                    }
                }
            }
        }
        return "###";
    }

    public org.hibernate.Session hibernateSession(){
        return baseDAO.getSession();
    }
    public void closeSession(){
        baseDAO.closeSession();
    }
    
    public String replaceSpecialChar_rev(String data) {
        String tmp = data.replace("%26", "&");
        tmp = tmp.replace("%3D", "=");
        tmp = tmp.replace("%25", "%");
        return tmp;
    }
    
    public String getRequestParam_(String paramName) {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        return request.getParameter(paramName);
    }
    
    public void checkPreSearch(Map param) throws Exception {
        System.out.println("in checkPreSearch");
        String preSearchMethod = getPreLookupSearchMethod();

        if (preSearchMethod != null) {
            boolean methodExists = false;
            Method m = null;
            DynamicPreSearch preSearchTrigger = new DynamicPreSearch();
            try {
                m = preSearchTrigger.getClass().getMethod(preSearchMethod, LookupAction.class, Map.class);
                methodExists = true;
            } catch (Exception e) {
            }

            if (methodExists) {
                try {
                    m.invoke(preSearchTrigger, this, param);
                } catch (Exception e) {
                    try {
                        if (e.getCause() instanceof BaseException) {
                            BaseException be = (BaseException) e.getCause();
                            throw be;
                        }
                    } finally {
                    }
                }
            } else {
                Debug.printFrameworkInfo("### For Developer: \"" + preSearchMethod + "\" defined in dynamic-config by do not implemented in DynamicPreSearch.java ###");
            }
        }
    }
    
    public String getDynamicSortBy_condition() {
        String newSortBy = "1";
        if (Validator.isEmpty(dynamicSortBy)) {
            if (!sortingFields.isEmpty()) {
                newSortBy = sortingFields.get(0);
//                if (dynamicSortByIndex != null) {
//                    if (dynamicSortByIndex.containsKey(newSortBy)) {
//                        return (String) dynamicSortByIndex.get(newSortBy);
//                    }
//                }
            } else {
                newSortBy = "1";
            }
        } else {
                if (!sortingFields.isEmpty()) {
                    if (!sortingFields.contains(dynamicSortBy)) {
                        newSortBy = sortingFields.get(0);
                    } else {
                        newSortBy = dynamicSortBy;
                    }
                } else {
                    newSortBy = "1";
                }
//                if (dynamicSortByIndex != null) {
//                    if (dynamicSortByIndex.containsKey(dynamicSortBy)) {
//                        return (String) dynamicSortByIndex.get(dynamicSortBy);
//                    }
//                }
        }
        return newSortBy;
    }
}
