package com.sains.framework.base;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.sains.common.util.ObjectCloner;
import com.sains.common.util.Options;
import com.sains.framework.base.web.DynamicAction;
import com.sains.framework.lookup.DataRetriever;
import com.sains.framework.lookup.LookupAction;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.FtpInterface;
import com.sains.common.util.FtpsUtil;
import com.sains.common.util.PageUtil;
import com.sains.common.util.SFTPBean;
import com.sains.common.util.Sftp2Util;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sample.ParentModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.text.WordUtils;
import org.hibernate.Query;
import org.hibernate.jdbc.Work;

public abstract class BaseActionSupport<T> extends BaseAction<T> {
    private String deleteSuccessCode = SUCCESS;
    public String getDeleteSuccessCode() {
        return deleteSuccessCode;
    }
    public void setDeleteSuccessCode(String deleteSuccessCode) {
        this.deleteSuccessCode = deleteSuccessCode;
    }
    
    private String insertUpdateSuccessCode = SUCCESS;
    public String getInsertUpdateSuccessCode() {
        return insertUpdateSuccessCode;
    }
    protected void setInsertUpdateSuccessCode(String insertUpdateSuccessCode) {
        this.insertUpdateSuccessCode = insertUpdateSuccessCode;
    }
    
    public BaseActionSupport() {
        super();
    }

    public BaseActionSupport(org.hibernate.Session session) {
        super(session);
    }
    private static final long serialVersionUID = -1477947567038101484L;
    protected ServiceFactory serviceFactory = ServiceFactory.getInstance();
    protected T model;
    private String action = "";
    private String retrievingColumns = "";
    private String sqlTables = "";
    private String sqlCondition = "";
    private String sqlOrderBy = "";
    private List<String> displayFields = new ArrayList();
    private List<String> displayFieldsHeader = new ArrayList();
    private Integer pageNo;
    private Integer pageSize;
    private List pageSizeOption = new ArrayList();
    private Boolean showPageSize = Boolean.TRUE;
    private Integer numberOfRows;
    private List result = new ArrayList();
    private List columns = new ArrayList();
    private List<String> searchFields = new ArrayList();
    private Map<String, String> searchFieldsMap = new HashMap();
    private Boolean hasMoreSearchField = Boolean.FALSE;
    private Boolean noPaging = Boolean.FALSE;
    private String showHideMoreField_ = "H";//hide, S=Show;
    private List<String> searchFieldsDbName = new ArrayList();
    private List<String> searchFields_with_dateFromTo = null;
    private List<String> searchFields_dd_validateCriteria = new ArrayList();
    private List<String> searchFieldsData = new ArrayList();
    private List<String> searchFieldsDefaultData = new ArrayList();
    private List<String> searchFieldsLabel = new ArrayList();
    
    private List<String> moreSearchFieldsDbName = new ArrayList();
    private List<String> moreSearchFields = new ArrayList();
    private List<String> moreSearchFields_with_dateFromTo = null;
    private List<String> moreSearchFields_dd_validateCriteria = new ArrayList();
    private List<String> moreSearchFieldsDefaultData = new ArrayList();
    private List<String> moreSearchFieldsLabel = new ArrayList();
    private Map searchFieldsDataMap = new HashMap();
    private List<String> searchingParameter = new ArrayList();
    private List<String> editLinkColumn = new ArrayList();
    private String editLinkAtNewPage = "";
    private Map<String, String> searchedParam = new HashMap();
    private Map<String, Object> searchFieldsHelperText = new HashMap();
    private Map setupMap = new HashMap();
    private boolean searched;
    private boolean paging;
    private String retrieve = "n";
    private String searchCondition = "";
    private String pagingURL = "";
    private String dcGroupBy = "";
    private String primaryKeyColumn = "";
    private String searchPage = "dynamicSearch"; //give a default searchPage
    private String specialSearch = null;
    private String listPage = "dynamicList";
    private String editPage = "";
    private String addPage = "";
    private String editPageURL = "";
    private String addPageURL = "";
    private String deleteURL = "";
    private String searchDescription;
    private Map<String, String> requiredParam = new HashMap();
    private String[] selected;
    private String redirectMessage;
    private String redirectError;
    private Map dynamicSetupMap = new HashMap();
    private Map dynamicSortByIndex = null;
    private String dynamicSortBy = "";
    private String dynamicSortOrder = "A";
    private String previousSearch = "";
    private List<String> sortingFields = new ArrayList();
    private final static int primaryKeyLength = 20;
    protected String returnStr = SUCCESS;
    protected String SUCCESS_EL;
    private String customisedMsg = "";
    private String callerSuccessPage = "";
    private Map searchFieldLookup = new HashMap();
    private Map searchFieldDD = new HashMap();
    private Map searchFieldFormat = new HashMap();
    private Map searchFieldStyleFormat = new HashMap();
    private Map searchFieldsDateData = new HashMap();
    private Map extraSetupMap = new HashMap();
    private Boolean useDataTable = Boolean.FALSE;
    private String hideAddButton = "N";
    private String hideDeleteButton = "N";
    private Boolean dl_showCheckbox = Boolean.TRUE;
    private String defaultSearchValue = "";
    private String impianSecurityCheck = "NIL";  // ThoTH @ 26-Jun-2013 :: For Impian Security
    private String usePopupCalander = "N";
    private Map<String, String> rightsList = new HashMap();
    private Map<String, String> appRightsList = new HashMap();
    private String dbMsg = null;
    private String session_us_id_filter = null;
    private String session_us_div_filter = null;
    private String session_userpk_filter = null;
    private String preSearchMethod = null;
    private String prePopulateMethod = null;
    private String pageRequired = "";
    private String scrollTo_ID = null;
    private String id = null;
    private String editLinkStyle = "";
//    private CommonList commList = null;
    protected CommonList commList = new CommonList(baseDAO.getSession());
    private Map<String, String> displayColumnLink = new HashMap();
    private List<String> hiddenFields = new ArrayList();
    private Map additionalButtonMap = new HashMap();
    private List additionalButton = new ArrayList();
    private List additionalButtonLabel = new ArrayList();
    private List additionalButtonAction = new ArrayList();
    private List additionalButtonIcon = new ArrayList();
    private List additionalButtonChecked = new ArrayList();
    private Boolean isMixConfig = Boolean.FALSE;
    private Boolean isAppendNoLock = Boolean.TRUE;
    private String mcf_action = "";
    private List<String> mcf_actionList = new ArrayList();
    private Map mixedConfig_map = new HashMap();
    private String searchCode = null;
    private Boolean showNumbering = Boolean.FALSE;
    public String noDeco_include = "";
    private String moreInfoWidth = null;
    private String moreInfoType = null;
    private String moreInfoJsp = null;
    private String moreInfoModel = null;
    private String moreInfoTitle = null;
    private Boolean moreInfoReloadOnClose = Boolean.FALSE;
    private String customHeaderEnd = null;
    private String customListEnd = null;
    private String jsInclude = null;

    public String loadEditPage() {
        setPageSubTitle_("Edit");
        try {
            model = processEdit(model);
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String processUpdate() throws Exception {
        try {
            validateRequired((ModelBase)getModel());
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
            }
            getDaoService_().update(getModel());
            addActionMessage(getText("updateSuccess"));
        } catch (BaseException be){
            clearNewModelID();
            addActionError(be.getMessage());
            return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
        } catch (Exception e) {
            clearNewModelID();
            new LogFunction().logError(this.getClass(), "", e);
            addActionError(getText("updateFail"));
            return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
        }

        return getInsertUpdateSuccessCode();
    }
    
    public String processInsert() throws Exception {
        try {
            validateRequired((ModelBase)getModel());
            specificValidation("insert");
            if (getActionErrors().size() > 0) {
                return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
            }
            getDaoService_().insert(getModel());
            addActionMessage(getText("createSuccess"));
        } catch (BaseException be){
            addActionError(be.getMessage());
            return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
            addActionMessage(getText("createFail"));
            return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
        }
        return insertUpdateSuccessCode;
    }

    public String delete() {
        try {
            if (getSelected() != null) {
                getDaoService_().frameworkDelete(getSelected(), getModel().getClass());
            }
            addActionMessage(getText("deleteSuccess"));
        } catch (BaseException be) {
            setRedirectMessage(be.getMessage());
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
            setRedirectMessage(getText("deleteFail"));
            return SystemConstants.ACTION_Status.INSERT_FAIL;
        }
        return deleteSuccessCode;
    }
    
    private String date_default_datetime;
    private String servHistPreviewTitle_ = ""; // ThoTH @ 1-Dec-2014
    private String servHistPreviewText_ = ""; // ThoTH @ 21-Nov-2014
    private List servHistPreviewList_; // ThoTH @ 20-Jun-2015
    private Map extraConditionMap = new HashMap(); // Zhafari @ 29-May-2015
    private List<String> extraCondition_toExecute = new ArrayList(); // Zhafari @ 29-May-2015
    private Map sqlTablesMap_other = new HashMap(); // Zhafari @ 10-Jun-2015
    private String countDistinctColumns;

    public String loadAddPage() {
        setPageSubTitle_("Add");
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

//    public static void defaultAddProperties(Object obj) {
//        try {
//            Method m;
//            m = obj.getClass().getMethod("setCreated_by", String.class);
//            try {
//                m.invoke(obj, (String) ActionContext.getContext().getSession().get("loginId"));
//            } catch (Exception noCreatedBy) {
//                m.invoke(obj, SystemConstants.BACKEND.DEFAULT_ID);
//            }
//            m = obj.getClass().getMethod("setCreated_date", java.sql.Timestamp.class);
//            m.invoke(obj, DateUtil.getCurrentTimestamp());
//            try {
//                m = obj.getClass().getMethod("setID", String.class);
//                m.invoke(obj, com.sains.framework.base.CommonFunction.getId(primaryKeyLength));
//            } catch (Exception e) {}
//            defaultUpdateProperties(obj);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public static void defaultAddProperties(Object obj, String strCreatedBy){
//		try {
//			Method m = obj.getClass().getMethod("setCreated_by", String.class);
//			m.invoke(obj, strCreatedBy);
//			m = obj.getClass().getMethod("setCreated_date", java.sql.Timestamp.class);
//			m.invoke(obj, DateUtil.getCurrentTimestamp());
//			m = obj.getClass().getMethod("setID", String.class);
//			m.invoke(obj, com.sains.framework.base.CommonFunction.getId(primaryKeyLength));
//			defaultUpdateProperties(obj, strCreatedBy);
//		} catch (Exception ex){
//			ex.printStackTrace();
//		}
//	}
//
//    public static void defaultAddProperties_p(Object obj){
//		try {
//			Method m = obj.getClass().getMethod("setCreated_by", String.class);
//			m.invoke(obj, (String)ActionContext.getContext().getSession().get("p_loginId"));
//			m = obj.getClass().getMethod("setCreated_date", java.sql.Timestamp.class);
//			m.invoke(obj, DateUtil.getCurrentTimestamp());
//			m = obj.getClass().getMethod("setID", String.class);
//			m.invoke(obj, com.sains.framework.base.CommonFunction.getId(primaryKeyLength));
//			defaultUpdateProperties_p(obj);
//		} catch (Exception ex){
//			ex.printStackTrace();
//		}
//	}
//
//    public static void defaultUpdateProperties(Object obj) {
//        try {
//
//            Method m;
//            m = obj.getClass().getMethod("setUpdated_by", String.class);
//            try {
//                m.invoke(obj, (String) ActionContext.getContext().getSession().get("loginId"));
//            } catch (Exception noUpdatedBy){
//                //set the default updated id to "BACKEND" if fail to get id from session.
//                m.invoke(obj, SystemConstants.BACKEND.DEFAULT_ID);
//            }
//            m = obj.getClass().getMethod("setUpdated_date", java.sql.Timestamp.class);
//            m.invoke(obj, DateUtil.getCurrentTimestamp());
//
//            try {
//                m = obj.getClass().getMethod("getID");
//                String id = (String) m.invoke(obj);
//                if (id == null || id.equals("")) {
//                    m = obj.getClass().getMethod("setID", String.class);
//                    m.invoke(obj, com.sains.framework.base.CommonFunction.getId(primaryKeyLength));
//                }
//            } catch (Exception e){}
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public static void defaultUpdateProperties(Object obj, String strUpdatedBy){
//		try {
//			Method m = obj.getClass().getMethod("setUpdated_by", String.class);
//			m.invoke(obj, strUpdatedBy);
//			m = obj.getClass().getMethod("setUpdated_date", java.sql.Timestamp.class);
//			m.invoke(obj, DateUtil.getCurrentTimestamp());
//
//            m = obj.getClass().getMethod("getID");
//            String id = (String)m.invoke(obj);
//            if (id == null || id.equals("")){
//                m = obj.getClass().getMethod("setID", String.class);
//                m.invoke(obj, com.sains.framework.base.CommonFunction.getId(primaryKeyLength));
//            }
//		} catch (Exception ex){
//			ex.printStackTrace();
//		}
//	}
//
//    public static void defaultUpdateProperties_p(Object obj){
//		try {
//			Method m = obj.getClass().getMethod("setUpdated_by", String.class);
//			m.invoke(obj, (String)ActionContext.getContext().getSession().get("p_loginId"));
//			m = obj.getClass().getMethod("setUpdated_date", java.sql.Timestamp.class);
//			m.invoke(obj, DateUtil.getCurrentTimestamp());
//                        m = obj.getClass().getMethod("getID");
//                        String id = (String)m.invoke(obj);
//                        if (id == null || id.equals("")){
//                            m = obj.getClass().getMethod("setID", String.class);
//                            m.invoke(obj, com.sains.framework.base.CommonFunction.getId(primaryKeyLength));
//                        }
//		} catch (Exception ex){
//			ex.printStackTrace();
//		}
//	}
    public T processEdit(T model) {
        if (ModelBase.isAutoNumberPk(model.getClass())) {
            return baseDAO.getModelById(new Integer(getId()), model.getClass());
        } else {
            return baseDAO.getModelById(getId(), model.getClass());
        }
    }

    public String insert(T model) {
        try {
//                defaultAddProperties(model);
            baseDAO.insert(model);
        } catch (BaseException ex) {
            dbMsg = ex.getMessage();
            returnStr = SystemConstants.ACTION_Status.INSERT_FAIL;
        } catch (Exception ex) {
            if (ex.getCause() != null) {
                dbMsg = ex.getCause().getMessage();
            } else {
                dbMsg = ex.getMessage();
            }
            new LogFunction().logError(this.getClass(), "", ex);
            returnStr = SystemConstants.ACTION_Status.INSERT_FAIL;
        } finally {
            closeSession();
        }
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("createSuccess"));
        } else {
            addActionError(getText("createFail"));
            if (dbMsg != null) {
                addActionError(dbMsg);
            }
        }
        return returnStr;
    }

    public String processUpdate(T model) {
        try {
//                defaultUpdateProperties(model);
            this.model = baseDAO.update(model);
        } catch (BaseException baseEx) {
            dbMsg = baseEx.getMessage();
            returnStr = SystemConstants.ACTION_Status.UPDATE_FAIL;
        } catch (Exception ex) {
            if (ex.getCause() != null) {
                dbMsg = ex.getCause().getMessage();
            } else {
                dbMsg = ex.getMessage();
            }
            new LogFunction().logError(this.getClass(), "", ex);
            returnStr = SystemConstants.ACTION_Status.UPDATE_FAIL;
        } finally {
            closeSession();
        }
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("updateSuccess"));
        } else {
            addActionError(getText("updateFail"));
            if (dbMsg != null) {
                addActionError(dbMsg);
            }
        }
        return returnStr;
    }

    public String delete(String id, T model) {
        try {
            baseDAO.delete(id, model);
        } catch (Exception ex) {
            if (ex.getCause() != null) {
                dbMsg = ex.getCause().getMessage();
            } else {
                dbMsg = ex.getMessage();
            }
            new LogFunction().logError(this.getClass(), "", ex);
            returnStr = SystemConstants.ACTION_Status.DELETE_FAIL;
        } finally {
            closeSession();
        }
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("deleteSuccess"));
        } else {
            addActionError(getText("deleteFail"));
            if (dbMsg != null) {
                addActionError(dbMsg);
            }
        }
        return returnStr;
    }

    public String groupDelete(String[] ids, T model) {
        try {
            baseDAO.groupDelete(ids, model);
        } catch (Exception ex) {
            if (ex.getCause() != null) {
                dbMsg = ex.getCause().getMessage();
            } else {
                dbMsg = ex.getMessage();
            }
            new LogFunction().logError(this.getClass(), "", ex);
            returnStr = SystemConstants.ACTION_Status.DELETE_FAIL;
        } finally {
            closeSession();
        }
        if (returnStr.equals(SUCCESS)) {
            addActionMessage(getText("deleteSuccess"));
        } else {
            addActionError(getText("deleteFail"));
            if (dbMsg != null) {
                addActionError(dbMsg);
            }
        }
        return returnStr;
    }

    public Integer getPageNo() {
        if (pageNo == null) {
            pageNo = 1;
        }
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

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

    public Integer getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(Integer numberOfRows) {
        this.numberOfRows = numberOfRows;
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

    public List<String> getSearchFields_dd_validateCriteria() {
        return searchFields_dd_validateCriteria;
    }

    public void setSearchFields_dd_validateCriteria(List<String> searchFields_dd_validateCriteria) {
        this.searchFields_dd_validateCriteria = searchFields_dd_validateCriteria;
    }

    public List<String> getSearchFieldsLabel() {
        return searchFieldsLabel;
    }

    public void setSearchFieldsLabel(List searchFieldsLabel) {
        this.searchFieldsLabel = searchFieldsLabel;
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

    public List<String> getMoreSearchFields_with_dateFromTo() {
        return moreSearchFields_with_dateFromTo;
    }

    public void setMoreSearchFields_with_dateFromTo(List<String> moreSearchFields_with_dateFromTo) {
        this.moreSearchFields_with_dateFromTo = moreSearchFields_with_dateFromTo;
    }

    public List<String> getMoreSearchFields_dd_validateCriteria() {
        return moreSearchFields_dd_validateCriteria;
    }

    public void setMoreSearchFields_dd_validateCriteria(List<String> moreSearchFields_dd_validateCriteria) {
        this.moreSearchFields_dd_validateCriteria = moreSearchFields_dd_validateCriteria;
    }

    public List<String> getMoreSearchFieldsDefaultData() {
        return moreSearchFieldsDefaultData;
    }

    public void setMoreSearchFieldsDefaultData(List<String> moreSearchFieldsDefaultData) {
        this.moreSearchFieldsDefaultData = moreSearchFieldsDefaultData;
    }

    public List<String> getMoreSearchFieldsLabel() {
        return moreSearchFieldsLabel;
    }

    public void setMoreSearchFieldsLabel(List<String> moreSearchFieldsLabel) {
        this.moreSearchFieldsLabel = moreSearchFieldsLabel;
    }

    public String getSearchDescription() {
        return getText(searchDescription);
    }

    public void setSearchDescription(String desc) {
        this.searchDescription = desc;
    }

    public String getRetrievingColumns() {
        return retrievingColumns;
    }

    public void setRetrievingColumns(String retrievingColumns) {
        this.retrievingColumns = retrievingColumns;
    }

    public String getSqlCondition() {
        return sqlCondition;
    }

    public void setSqlCondition(String sqlCondition) {
        this.sqlCondition = sqlCondition;
    }

    public String getSqlOrderBy() {
        return sqlOrderBy;
    }

    public void setSqlOrderBy(String sqlOrderBy) {
        this.sqlOrderBy = sqlOrderBy;
    }

    public List<String> getDisplayFields() {
        return displayFields;
    }

    public void setDisplayFields(List<String> displayFields) {
        this.displayFields = displayFields;
    }

    public List<String> getDisplayFieldsHeader() {
        return displayFieldsHeader;
    }

    public void setDisplayFieldsHeader(List<String> displayFieldsHeader) {
        this.displayFieldsHeader = displayFieldsHeader;
    }

    public String getSqlTables() {
        return sqlTables;
    }

    public void setSqlTables(String sqlTables) {
        this.sqlTables = sqlTables;
    }

    public List<String> getSearchingParameter() {
        return searchingParameter;
    }

    public void setSearchingParameter(List<String> searchingParameter) {
        this.searchingParameter = searchingParameter;
    }

    public Map<String, String> getSearchedParam() {
        if (searchedParam != null) {
            searchedParam.remove("ignore");
        }
        return searchedParam;
    }

    public void setSearchedParam(Map<String, String> searchedParam) {
        this.searchedParam = searchedParam;
    }

    public Map getSetupMap() {
        return setupMap;
    }

    public void setSetupMap(Map setupMap) {
        this.setupMap = setupMap;
    }

    public Map<String, String> getSearchFieldsMap() {
        return searchFieldsMap;
    }

    public void setSearchFieldsMap(Map<String, String> searchFieldsMap) {
        this.searchFieldsMap = searchFieldsMap;
    }

    public Boolean getHasMoreSearchField() {
        return hasMoreSearchField;
    }
    public void setHasMoreSearchField(Boolean hasMoreSearchField) {
        this.hasMoreSearchField = hasMoreSearchField;
    }

    public Boolean getNoPaging() {
        return noPaging;
    }

    public void setNoPaging(Boolean noPaging) {
        this.noPaging = noPaging;
    }

    public String getShowHideMoreField_() {
        return showHideMoreField_;
    }
    public Boolean getShowHideMoreField_boo() {
        return showHideMoreField_.equals("S");
    }

    public void setShowHideMoreField_(String showHideMoreField_) {
        this.showHideMoreField_ = showHideMoreField_;
    }

    public List<String> getSearchFieldsDbName() {
        return searchFieldsDbName;
    }

    public void setSearchFieldsDbName(List<String> searchFieldsDbName) {
        this.searchFieldsDbName = searchFieldsDbName;
    }

    public boolean isSearched() {
        return searched;
    }

    public void setSearched(boolean searched) {
        this.searched = searched;
    }

    public Map getPagingItem() {
        return PageUtil.getPagingItem(getPageNo(), getPageSize(), getNumberOfRows(), 9);
    }

    public Map getDynamicPagingItem(String dynamicParam) {
        return PageUtil.getPagingItem(getDynamicPageNo(dynamicParam), getDynamicPageSize(dynamicParam), getDynamicNumberOfRows(dynamicParam), 10);
    }

    public String getPagingURL() {
        String rtn = null;
        if (pagingURL.indexOf("?") >= 0) {
            rtn = pagingURL + (Validator.isEmpty(searchCondition) ? "" : "&" + searchCondition);
            if (!rtn.contains("dynamicSortBy=")) {
                rtn += "&dynamicSortBy="+getDynamicSortBy()+"&dynamicSortOrder="+dynamicSortOrder;
            }
            return rtn.replaceAll("/", "%2F");
        }
        rtn = pagingURL + "?" + (Validator.isEmpty(searchCondition) ? "" : searchCondition);
        if (!rtn.contains("dynamicSortBy=")) {
            rtn += "&dynamicSortBy="+getDynamicSortBy()+"&dynamicSortOrder="+dynamicSortOrder;
        }
        return rtn.replaceAll("/", "%2F");
    }

    public void setPagingURL(String pagingUrl) {
        pagingURL = pagingUrl;
    }

    public String getDcGroupBy() {
        return dcGroupBy;
    }

    public void setDcGroupBy(String dcGroupBy) {
        this.dcGroupBy = dcGroupBy;
    }

    public String getSearchCondition() {
        return searchCondition;
    }

    public void setSearchCondition(String searchCondition) {
        this.searchCondition = searchCondition;
    }

    public boolean isPaging() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public String getInitCapAction() {
        return action.substring(0, 1).toUpperCase() + action.substring(1);
    }
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, String> getSearchFieldsDateData() {
        return searchFieldsDateData;
    }

    public void setSearchFieldsDateData(Map searchFieldDateMap) {
        searchFieldsDateData = searchFieldDateMap;
    }

    public Map getExtraSetupMap() {
        return extraSetupMap;
    }

    public Boolean getUseDataTable() {
        return useDataTable;
    }

    public List<String> getSearchFieldsData() {
        return searchFieldsData;
    }

    public void setSearchFieldsData(List<String> searchFieldsData) {
        this.searchFieldsData = searchFieldsData;
    }

    public Map getSearchFieldsDataMap() {
        return searchFieldsDataMap;
    }

    public void setSearchFieldsDataMap(Map searchFieldsDataMap) {
        this.searchFieldsDataMap = searchFieldsDataMap;
    }

    public List<String> getSearchFieldsDefaultData() {
        return searchFieldsDefaultData;
    }

    public void setSearchFieldsDefaultData(List<String> searchFieldsDefaultData) {
        this.searchFieldsDefaultData = searchFieldsDefaultData;
    }

    public List<String> getEditLinkColumn() {
        return editLinkColumn;
    }

    public void setEditLinkColumn(List<String> editLinkColumn) {
        this.editLinkColumn = editLinkColumn;
    }

    public String getEditLinkAtNewPage() {
        return editLinkAtNewPage;
    }

    public void setEditLinkAtNewPage(String editLinkAtNewPage) {
        this.editLinkAtNewPage = editLinkAtNewPage;
    }

    public String getOpenLinkAtNewPage(String editLinkColumn) {
        if (editLinkAtNewPage != null) {
            String colNames[] = editLinkAtNewPage.split(",");
            for (String colName : colNames) {
                if (colName.trim().equals(editLinkColumn)) {
                    return "_blank";
                }
            }
        }
        return "_self";
    }

    public String getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    public void setPrimaryKeyColumn(String primaryKeyColumn) {
        this.primaryKeyColumn = primaryKeyColumn;
    }

    public String getSearchPage() {
        if (searchPage != null) {
            if (searchPage.equals("dynamicSearch") || searchPage.equals("dynamicSearch2")) {
                searchPage = SystemConstants.SYSTEM_SETUP.JSP_PREFIX + searchPage;
            }
        }
        return searchPage;
    }

    public void setSearchPage(String searchPage) {
        this.searchPage = searchPage;
    }

    public String getSpecialSearch() {
        return specialSearch;
    }

    public void setSpecialSearch(String specialSearch) {
        this.specialSearch = specialSearch;
    }

    public String getListPage() {
        if (listPage != null) {
            if (listPage.equals("dynamicList") || listPage.equals("dynamicList2")) {
                listPage = SystemConstants.SYSTEM_SETUP.JSP_PREFIX + listPage;
            }
        }
        return listPage;
    }

    public void setListPage(String listPage) {
        this.listPage = listPage;
    }

    public String getEditPage() {
        return editPage;
    }

    public void setEditPage(String editPage) {
        this.editPage = editPage;
    }

    public String getAddPage() {
        return addPage;
    }

    public void setAddPage(String addPage) {
        this.addPage = addPage;
    }

    public String getEditPageURL() {
        return editPageURL;
    }

    public void setEditPageURL(String editPageURL) {
        this.editPageURL = editPageURL;
    }

    public String getAddPageURL() {
        return addPageURL;
    }

    public void setAddPageURL(String addPageURL) {
        this.addPageURL = addPageURL;
    }

    public String getDeleteURL() {
        return deleteURL;
    }

    public void setDeleteURL(String deleteURL) {
        this.deleteURL = deleteURL;
    }

    public Map<String, String> getRequiredParam() {
        return requiredParam;
    }

    public void setRequiredParam(Map<String, String> requiredParam) {
        this.requiredParam = requiredParam;
    }

    public String[] getSelected() {
        return selected;
    }

    public void setSelected(String[] selected) {
        this.selected = selected;
    }

    private void populatePageSizeIfEmpty() {
        if (getShowPageSize() && pageSizeOption.isEmpty()) {
            pageSizeOption.add(new Options("10", "10"));
            pageSizeOption.add(new Options("20", "20"));
            pageSizeOption.add(new Options("30", "30"));
            pageSizeOption.add(new Options("40", "40"));
            pageSizeOption.add(new Options("50", "50"));
        }
    }

    public String populateDynamicActionSetup() {
        Collection<String> col = getActionMessages();
        col.remove("[]");
        setActionMessages(col);
        String[] listSetup = null;

        if (retrieve.equalsIgnoreCase("n")) {
            getAndRemoveObjectFromSession(getAction() + "_searchCondition");
        }
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        //check the validation message pass over. if got message, show it.
        if (!Validator.isEmpty(request.getParameter("errmsg"))) {
            addActionError(request.getParameter("errmsg"));
        }
        if (!Validator.isEmpty(request.getParameter("msg"))) {
            addActionMessage(request.getParameter("msg"));
        }
        try {
//                    Debug.printFrameworkDebug("getAction = " + getAction());
            setSetupMap(getDynamicActionSetup(getAction()));
            Map configMap = getSetupMap();
            setPageTitle_(getText((String) configMap.get("searchDescription")));
            String str = (String) getObjectFromSession(getAction() + "_searchCondition");

            if (configMap.get("jsInclude") != null) {
                jsInclude = (String)configMap.get("jsInclude");
                if (Validator.isEmpty(jsInclude.trim())) {
                    jsInclude = null;
                }
            }
            
            if (configMap.get("customListEnd") != null) {
                customListEnd = (String)configMap.get("customListEnd");
            }

            if (configMap.get("customHeaderEnd") != null) {
                customHeaderEnd = (String)configMap.get("customHeaderEnd");
            }
            
            if (configMap.get("moreInfoWidth") != null) {
                moreInfoWidth = (String)configMap.get("moreInfoWidth");
                if (moreInfoWidth.equals("")) {
                    moreInfoWidth = "60%";
                }
            }
            if (configMap.get("moreInfoType") != null) {
                moreInfoType = (String)configMap.get("moreInfoType");
                if (moreInfoType.equals("")) {
                    moreInfoType = "Auto";
                }
            }
            if (configMap.get("moreInfoReloadOnClose") != null) {
                moreInfoReloadOnClose = configMap.get("moreInfoReloadOnClose").toString().equalsIgnoreCase("true");
            }
            if (configMap.get("moreInfoJsp") != null) {
                moreInfoJsp = (String)configMap.get("moreInfoJsp");
            }
            if (configMap.get("moreInfoModel") != null) {
                moreInfoModel = (String)configMap.get("moreInfoModel");
            }
            if (configMap.get("showNumbering") != null) {
                showNumbering = configMap.get("showNumbering").toString().equalsIgnoreCase("Y");
            }
            
            if (configMap.get("noPaging") != null) {
                noPaging = "true".equalsIgnoreCase((String)configMap.get("noPaging"));
            }
            
            if (configMap.get("appendNoLock") != null) {
                if (((String) configMap.get("appendNoLock")).equalsIgnoreCase("false")) {
                    isAppendNoLock = Boolean.FALSE;
                }
            }
            if (configMap.get("mixedConfig") != null) {
                isMixConfig = Boolean.TRUE;
                setSearchDescription(configMap.get("searchDescription").toString());
                searchCode = request.getParameter("searchCode");
                if (Validator.isEmpty(searchCode)) {
//                                Debug.printFrameworkDebug("searchCode is empty....");
                    if (str != null) {
                        String[] conditionArr = str.split("&");
                        for (String s : conditionArr) {
//                                        Debug.printFrameworkDebug("s: " + s);
                            if (s.split("=")[0].equals("searchCode")) {
                                searchCode = s.substring(s.indexOf("=") + 1);
                                mcf_action = searchCode.split("::")[0];
                                break;
                            }
                        }
                    }
                } else {
                    mcf_action = searchCode.split("::")[0];
                }
                List<Options> list = getListFromSetup((String) configMap.get("mixedDD"));
                searchFieldDD.put("searchCode_dd", list);
                mixedConfig_map.put("mainSearchCodeDD", searchFieldDD);
                searchFieldDD = new HashMap();  // Added by Delvene @ 27-Aug-2014 :: Mainly for mainSearchCode in dynamicSearch. To support dropdown list in other MixedConfig
//                            Debug.printFrameworkDebug("searchCode = " + searchCode);
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
                    configMap = getDynamicActionSetup(mixAction.trim());
                    String searchField = null, searchFieldDD_setup = null;

//                    if (configMap.get("defaultSorting") != null) {
//                        if (Validator.isEmpty(getDynamicSortBy())) {
//                            setDynamicSortBy(configMap.get("defaultSorting").toString().split(";")[0]);
//                            setDynamicSortOrder(configMap.get("defaultSorting").toString().split(";")[1]);
//                        }
//                    }
                    StringTokenizer st = new StringTokenizer(configMap.get("searchFields").toString(), ",");
                    StringTokenizer st2 = null;
                    StringTokenizer st3 = null;
                    if (!configMap.get("searchFields").toString().contains(";")) {
                        st2 = new StringTokenizer(configMap.get("searchFieldsLabel").toString(), ",");
                        st3 = new StringTokenizer(configMap.get("searchFieldsDbName").toString(), ",");
                    }

                    String[] fieldArr = null;
                    while (st.hasMoreTokens()) {
                        if (st2 == null) {
                            searchField = st.nextToken().trim();
                            if (Validator.isEmpty(fieldArr[1])) {
                                searchFieldsMap.put(searchField+"_label", getText(getDynamicActionSetup(getAction())+"."+fieldArr[0]));
                                getSearchFieldsLabel().add(getText(getDynamicActionSetup(getAction())+"."+fieldArr[0]));
                            } else {
                                searchFieldsMap.put(searchField+"_label", getText(fieldArr[1].trim()));
                                getSearchFieldsLabel().add(getText(fieldArr[1].trim()));
                            }
                            if (Validator.isEmpty(fieldArr[2])) {
                                searchFieldsMap.put(searchField+"_dbName", fieldArr[0].trim());
                                getSearchFieldsDbName().add(fieldArr[0].trim());
                            } else {
                                searchFieldsMap.put(searchField+"_dbName", fieldArr[2].trim());
                                getSearchFieldsDbName().add(fieldArr[2].trim());
                            }
                        } else {
                            fieldArr = st.nextToken().trim().split(";");
                            searchField = fieldArr[0];
                            searchFieldsMap.put(searchField+"_label", getText(st2.nextToken().trim()));
                            searchFieldsMap.put(searchField+"_dbName", st3.nextToken().trim());
                            getSearchFieldsLabel().add(searchFieldsMap.get(searchField+"_label"));
                            getSearchFieldsDbName().add(searchFieldsMap.get(searchField+"_dbName"));
                        }
                        if (searchField.startsWith("_date_")) { //setup the _fromTo
                            if (searchField.endsWith("_fromTo")) {
                                if (searchFields_with_dateFromTo == null) {
                                    searchFields_with_dateFromTo = new ArrayList();
                                }
                                searchFields_with_dateFromTo.add(mixAction + "_" + searchField.substring(6, (searchField.length() - 7)));
                            }
                            setUsePopupCalander("Y");
                        }
                        getSearchFields().add(searchField);
                        if (configMap.get(searchField + "_lookupSearch") != null) {
                            searchFieldsMap.put(searchField+"_lookupSearch", (String)configMap.get(searchField + "_lookupSearch"));
                            searchFieldLookup.put(searchField + "_lookupSearch", configMap.get(searchField + "_lookupSearch"));
                        }
                        if (configMap.get(searchField + "_defaultValue") != null) {
                            searchFieldsMap.put(searchField+"_defaultValue", (String)configMap.get(searchField + "_defaultValue"));
                            searchFieldsDefaultData.add(searchField + ";" + configMap.get(searchField + "_defaultValue"));
                        }
                        if (configMap.get(searchField + "_helperText") != null) {
                            searchFieldsMap.put(searchField+"_helperText", (String)configMap.get(searchField + "_helperText"));
                            searchFieldsHelperText.put(searchField, configMap.get(searchField + "_helperText"));
                        }
                        if (configMap.get(searchField + "_dd") != null) {
                            searchFieldDD_setup = configMap.get(searchField + "_dd").toString();
                            listSetup = searchFieldDD_setup.split(";");
                            searchFieldDD.put(searchField + "_dd", getListFromSetup(searchFieldDD_setup));
                            searchFieldDD.put(searchField + "_dd_list", listSetup[2]);   // Added by Delvene @ 29-Aug-2013 :: To identify list is from setup code table or normal option
                            searchFieldDD.put(searchField + "_dd_key", listSetup[3]);
                            searchFieldDD.put(searchField + "_dd_value", listSetup[4]);
                            if (listSetup.length == 7) {//listSetup[6] must be the searchField's DB Name
                                getSearchFields_dd_validateCriteria().add(searchField + ";" + listSetup[6]);
                            }
                            if (configMap.get(searchField + "_dd_type") != null) {
                                getSearchFieldDD().put(searchField + "_dd_type", configMap.get(searchField + "_dd_type"));
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
                        if (str != null) {
                            boolean found = false;
                            String[] conditionArr = str.split("&");
                            for (String s : conditionArr) {
                                if (s.split("=")[0].equals("search_" + searchField)) {
                                    found = true;
                                    searchFieldsMap.put("search_" + searchField, replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                                    getSearchFieldsData().add(replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                                    getSearchFieldsDataMap().put("search_" + searchField, replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                                    getSearchedParam().put(searchField, replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                                    break;
                                }
                            }
                            if (!found) {
                                searchFieldsMap.put("search_" + searchField, "");
                                getSearchFieldsData().add("");
                                getSearchFieldsDataMap().put("search_" + searchField, "");
                            }
                        } else {
                            searchFieldsMap.put("search_" + searchField, "");
                            getSearchFieldsData().add("");
                            getSearchFieldsDataMap().put("search_" + searchField, "");
                        }
                    }
                    if (configMap.get("moreSearchFields") != null) {
                        hasMoreSearchField = Boolean.TRUE;
                        st = new StringTokenizer(configMap.get("moreSearchFields").toString(), ",");
                        while (st.hasMoreTokens()) {
                            searchField = st.nextToken().trim();
                            if (searchField.endsWith(";;")) searchField = searchField.substring(0, searchField.length()-2) + "; ; ";
                            fieldArr = searchField.split(";", 0);
                            searchField = fieldArr[0];
                            if (Validator.isEmpty(fieldArr[1])) {
                                searchFieldsMap.put(searchField+"_label", getText(getAction()+"."+fieldArr[0]));
                            } else {
                                searchFieldsMap.put(searchField+"_label", getText(fieldArr[1].trim()));
                            }
                            if (Validator.isEmpty(fieldArr[2])) {
                                searchFieldsMap.put(searchField+"_dbName", fieldArr[0].trim());
                            } else {
                                searchFieldsMap.put(searchField+"_dbName", fieldArr[2].trim());
                            }
                            getMoreSearchFieldsLabel().add(getText(st2.nextToken().trim()));
                            getMoreSearchFieldsDbName().add(st3.nextToken().trim());
                            if (searchField.startsWith("_date_")) { //setup the _fromTo
                                if (searchField.endsWith("_fromTo")) {
                                    if (searchFields_with_dateFromTo == null) {
                                        searchFields_with_dateFromTo = new ArrayList();
                                    }
                                    searchFields_with_dateFromTo.add(mixAction + "_" + searchField.substring(6, (searchField.length() - 7)));
                                }
                                setUsePopupCalander("Y");
                            }
                            getMoreSearchFields().add(searchField);
                            if (configMap.get(searchField + "_lookupSearch") != null) {
                                searchFieldLookup.put(searchField + "_lookupSearch", configMap.get(searchField + "_lookupSearch"));
                                searchFieldsMap.put(searchField+"_lookupSearch", (String)configMap.get(searchField + "_lookupSearch"));
                            }
                            if (configMap.get(searchField + "_defaultValue") != null) {
                                searchFieldsDefaultData.add(searchField + ";" + configMap.get(searchField + "_defaultValue"));
                                searchFieldsMap.put(searchField+"_defaultValue", (String)configMap.get(searchField + "_defaultValue"));
                            }
                            if (configMap.get(searchField + "_helperText") != null) {
                                searchFieldsHelperText.put(searchField, configMap.get(searchField + "_helperText"));
                                searchFieldsMap.put(searchField+"_helperText", (String)configMap.get(searchField + "_helperText"));
                            }
                            if (configMap.get(searchField + "_dd") != null) {
                                searchFieldDD_setup = configMap.get(searchField + "_dd").toString();
                                listSetup = searchFieldDD_setup.split(";");
                                searchFieldDD.put(searchField + "_dd", getListFromSetup(searchFieldDD_setup));
                                searchFieldDD.put(searchField + "_dd_list", listSetup[2]);   // Added by Delvene @ 29-Aug-2013 :: To identify list is from setup code table or normal option
                                searchFieldDD.put(searchField + "_dd_key", listSetup[3]);
                                searchFieldDD.put(searchField + "_dd_value", listSetup[4]);
                                if (listSetup.length == 7) {//listSetup[6] must be the searchField's DB Name
                                    getSearchFields_dd_validateCriteria().add(searchField + ";" + listSetup[6]);
                                }
                                if (configMap.get(searchField + "_dd_type") != null) {
                                    getSearchFieldDD().put(searchField + "_dd_type", configMap.get(searchField + "_dd_type"));
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
                            if (str != null) {
                                boolean found = false;
                                String[] conditionArr = str.split("&");
                                for (String s : conditionArr) {
                                    if (s.split("=")[0].equals("search_" + searchField)) {
                                        found = true;
                                        searchFieldsMap.put(searchField+"_data", replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                                        getSearchFieldsData().add(replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                                        getSearchFieldsDataMap().put("search_" + searchField, replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                                        getSearchedParam().put(searchField, replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                                        break;
                                    }
                                }
                                if (!found) {
                                    searchFieldsMap.put(searchField+"_data", "");
                                    getSearchFieldsData().add("");
                                    getSearchFieldsDataMap().put("search_" + searchField, "");
                                }
                            } else {
                                searchFieldsMap.put(searchField+"_data", "");
                                getSearchFieldsData().add("");
                                getSearchFieldsDataMap().put("search_" + searchField, "");
                            }
                        }
                    }
                    st = new StringTokenizer(configMap.get("displayFields").toString(), ",");
                    if (configMap.get("displayFields").toString().contains(";")) {
                        st2 = null;
                    } else {
                        st2 = new StringTokenizer(configMap.get("displayFieldsHeader").toString(), ",");
                    }

                    String displayField = null;
                    while (st.hasMoreTokens()) {
                        if (st2 == null) {
                            fieldArr = st.nextToken().trim().split(";");
                            displayField = fieldArr[0].trim();
                            if (Validator.isEmpty(fieldArr[1])) {
                                getDisplayFieldsHeader().add(getText(mixAction.trim()+"."+fieldArr[0]));
                            } else {
                                getDisplayFieldsHeader().add(getText(fieldArr[1]));
                            }
                        } else {
                            displayField = st.nextToken().trim();
                            getDisplayFieldsHeader().add(getText(st2.nextToken().trim()));
                        }
                        getDisplayFields().add(displayField);
                        if (configMap.get("decFormat_" + displayField) != null) {
                            searchFieldFormat.put("decFormat_" + displayField, configMap.get("decFormat_" + displayField));
                        }
                        if (configMap.get("propertyText_" + displayField) != null) {
                            searchFieldFormat.put("propertyText_" + displayField, configMap.get("propertyText_" + displayField));
                        }
                        if (configMap.get("functionText_" + displayField) != null) {
                            searchFieldFormat.put("functionText_" + displayField, configMap.get("functionText_" + displayField));
                        }
                        if (configMap.get("styleFormat_" + displayField) != null) {
                            searchFieldStyleFormat.put("styleFormat_" + displayField, configMap.get("styleFormat_" + displayField));
                        }
                        if (configMap.get("searchInputStyleFormat_" + displayField) != null) { //Added by Zhafari @ 11-Nov-2014 - for style at search input
                            searchFieldStyleFormat.put("searchInputStyleFormat_" + displayField, configMap.get("searchInputStyleFormat_" + displayField));
                        }
//                                    getDisplayFieldsHeader().add(getText(st2.nextToken().trim()));
                    }
                    
                    if (configMap.get("defaultSorting") != null) {
                        if (Validator.isEmpty(getDynamicSortBy())) {
                            setDynamicSortBy(configMap.get("defaultSorting").toString().split(";")[0]);
                            setDynamicSortOrder(configMap.get("defaultSorting").toString().split(";")[1]);
                        }
                    } else {
                        if (Validator.isEmpty(getDynamicSortBy())) {
                            setDynamicSortBy(getDisplayFields().get(0));
                            setDynamicSortOrder("A");
                        }
                    }
                    mixedConfig_map.put(mixAction.trim(), searchFieldsMap); searchFieldsMap=new HashMap();
                    
                    mixedConfig_map.put(mixAction.trim() + "displayFields", displayFields);
                    mixedConfig_map.put(mixAction.trim() + "searchFields", searchFields);
                    searchFields = new ArrayList();
                    mixedConfig_map.put(mixAction.trim() + "searchFieldLookup", searchFieldLookup);
                    searchFieldLookup = new HashMap();
                    mixedConfig_map.put(mixAction.trim() + "searchFieldsDefaultData", searchFieldsDefaultData);
                    searchFieldsDefaultData = new ArrayList();
                    mixedConfig_map.put(mixAction.trim() + "searchFieldsHelperText", searchFieldsHelperText);
                    searchFieldsHelperText = new HashMap();
                    mixedConfig_map.put(mixAction.trim() + "searchFieldDD", searchFieldDD);
                    searchFieldDD = new HashMap();    // Uncommented by Delvene @ 28-Aug-2014 :: To show dropdown list in MixedConfig
                    mixedConfig_map.put(mixAction.trim() + "searchFieldsData", searchFieldsData);
                    searchFieldsData = new ArrayList();
                    mixedConfig_map.put(mixAction.trim() + "searchedParam", searchedParam);
                    searchedParam = new HashMap();
//                                mixedConfig_map.put("mixActionsearchFields_dd_validateCriteria", searchFields_dd_validateCriteria);
                    mixedConfig_map.put(mixAction.trim() + "searchFieldsLabel", searchFieldsLabel);
                    searchFieldsLabel = new ArrayList();
                    mixedConfig_map.put(mixAction.trim() + "searchFieldsDbName", searchFieldsDbName);
                    searchFieldsDbName = new ArrayList();

                    if (configMap.get("hideAddButton") != null) {
                        mixedConfig_map.put(mixAction.trim() + "hideAddButton", configMap.get("hideAddButton"));
                    }
                    if (configMap.get("hideDeleteButton") != null) {
                        mixedConfig_map.put(mixAction.trim() + "hideDeleteButton", configMap.get("hideDeleteButton"));
                    }
                    if (configMap.get("sortingFields") != null) {
                        if (configMap.get("sortingFields").equals("asDisplayFields")) {
                            sortingFields = displayFields;
                        } else {
                            st = new StringTokenizer(configMap.get("sortingFields").toString(), ",");
                            while (st.hasMoreTokens()) {
                                sortingFields.add(st.nextToken().trim());
                            }
                        }
                        if (mixedConfig_map.get(mixAction.trim() + "dynamicSortBy") == null) {
                            mixedConfig_map.put(mixAction.trim() + "dynamicSortBy", sortingFields.get(0));
                            mixedConfig_map.put(mixAction.trim() + "sortingFields", sortingFields);
                            sortingFields = new ArrayList();
                        }
                    }
                    if (configMap.get("sortingByIndex") != null) {
                        if (dynamicSortByIndex == null) {
                            dynamicSortByIndex = new HashMap();
                        }
                        st = new StringTokenizer(configMap.get("sortingByIndex").toString(), ",");
                        while (st.hasMoreTokens()) {
                            String[] strArr = st.nextToken().trim().split(";");
                            dynamicSortByIndex.put(strArr[0], strArr[1]);
                        }
                    }
                    displayFields = new ArrayList();
                    mixedConfig_map.put(mixAction.trim() + "editLinkColumn", configMap.get("editLinkColumn"));
                    mixedConfig_map.put(mixAction.trim() + "editPageURL", configMap.get("editPageURL"));
                    mixedConfig_map.put(mixAction.trim() + "addPageURL", configMap.get("addPageURL"));
                    mixedConfig_map.put(mixAction.trim() + "deleteURL", configMap.get("deleteURL"));
                }
//                            Debug.printFrameworkDebug("mcf_action = " + mcf_action);
                configMap = getDynamicActionSetup(mcf_action);
                setSetupMap(configMap);
            }
//                        if (configMap.get("sf_system_type") != null){
//                            sf_system_type = (String)configMap.get("sf_system_type");
////                            Debug.printFrameworkDebug("BaseActionSupport : populateDynamicActionSetup() sf_system_type = " + sf_system_type);
//                        }
            if (configMap.get("useDataTable") != null) {
                if (((String)configMap.get("useDataTable")).equalsIgnoreCase("true")) {
                    useDataTable = Boolean.TRUE;
                }
            }
            if (configMap.get("StartEndDateField") != null) {
                extraSetupMap.put("StartEndDateField", configMap.get("StartEndDateField"));
            }
            setEditLinkAtNewPage((String) configMap.get("editLinkAtNewPage"));
            if (configMap.get("defaultSorting") != null) {
                if (Validator.isEmpty(getDynamicSortBy())) {
                    setDynamicSortBy(configMap.get("defaultSorting").toString().split(";")[0]);
                    setDynamicSortOrder(configMap.get("defaultSorting").toString().split(";")[1]);
                }
            }
            if (configMap.get("showRecordPerPage") != null) {
                setShowPageSize(Boolean.parseBoolean(configMap.get("showRecordPerPage").toString()));
            } else {
                setShowPageSize(Boolean.TRUE); // always show!!
            }
            if (configMap.get("recordPerPage") != null) {
                String[] pagingSetup = configMap.get("recordPerPage").toString().split(";");
                if (pageSize == null) {
                    pageSize = Integer.parseInt(pagingSetup[0]);
                }
                if (getShowPageSize()) {
                    if (pagingSetup.length > 1) {
                        for (String pageSizeItem : pagingSetup[1].split(",")) {
                            pageSizeOption.add(new Options(pageSizeItem, pageSizeItem));
                        }
                    }
                }
            }
            populatePageSizeIfEmpty();
            if (configMap.get("editLinkStyle") != null) {
                setEditLinkStyle(configMap.get("editLinkStyle").toString());
            }
            if (configMap.get("session_us_id_filter") != null) {
                setSession_us_id_filter(configMap.get("session_us_id_filter").toString());
            }
            if (configMap.get("session_userpk_filter") != null) {
                setSession_userpk_filter(configMap.get("session_userpk_filter").toString());
            }
            if (configMap.get("additionalButton") != null) {
                String[] button_desc = null;
                for (String buttons : configMap.get("additionalButton").toString().split(",")) {
                    button_desc = buttons.split(";");
                    additionalButton.add(button_desc[0]);
                    additionalButtonLabel.add(getText(button_desc[1]));
                    if (button_desc.length >= 4) {
                        if (!Validator.isEmpty(button_desc[3])) {
                            additionalButtonIcon.add(button_desc[3]);
                        } else {
                            additionalButtonIcon.add("");
                        }
                    } else {
                        additionalButtonIcon.add("");
                    }
                    if (button_desc.length >= 3) {
                        if (!Validator.isEmpty(button_desc[2])) {
                            additionalButtonAction.add(getText(button_desc[0] + button_desc[2]));
                        } else {
                            additionalButtonAction.add(button_desc[0] + getAction());
                        }
                    } else {
                        additionalButtonAction.add(button_desc[0] + getAction());
                    }
                    if (button_desc.length >= 5) {
                        additionalButtonChecked.add(button_desc[4].equals("checked"));
                    } else {
                        additionalButtonChecked.add(Boolean.FALSE);
                    }
                    if (button_desc.length >= 6) {
                        if (button_desc[5].equals("divSubmitForm")) {
                            additionalButtonMap.put(button_desc[0]+"divSubmitForm", button_desc[5]);
                        }
                    }
                    if (button_desc.length >= 7) {
                        if (button_desc[6].equalsIgnoreCase("offLoading")) {
                            additionalButtonMap.put(button_desc[0]+"offLoading", "true");
                        }
                    }
                }
            }
            if (configMap.get("preSearchMethod") != null) {
                setPreSearchMethod(configMap.get("preSearchMethod").toString());
            }
            if (configMap.get("prePopulateSearchPage") != null) {
                setPrePopulateMethod(configMap.get("prePopulateSearchPage").toString());
            }

            String required = (String) configMap.get("pageRequired");
            int count = 1;
            if (required != null) {
                for (String req : required.split(",")) {
                    String[] reqArr = req.split(";");
                    pageRequired += "this.a" + count++ + " = new Array('search_" + reqArr[0].trim() + "', '" + getText(reqArr[1]) + "'); " + "\r\n";
                }
            }
            if (configMap.get("hideAddButton") != null) {
                setHideAddButton("" + configMap.get("hideAddButton"));
            }
            if (configMap.get("hideDeleteButton") != null) {
                setHideDeleteButton("" + configMap.get("hideDeleteButton"));
            }
            if (configMap.get("showCheckbox") != null) {
                setDl_showCheckbox("y".equalsIgnoreCase((String)configMap.get("showCheckbox")) || "true".equalsIgnoreCase((String)configMap.get("showCheckbox")));
            }
            
            if (configMap.get("defaultSearchValue") != null) {
                setDefaultSearchValue(configMap.get("defaultSearchValue").toString());
            }
//                        if (configMap.get("impianSecurityCheck") != null){  // ThoTH @ 26-Jun-2013 :: For Impian Security
//                            setImpianSecurityCheck(configMap.get("impianSecurityCheck").toString());
//                        }
            String searchField = null, searchFieldDD_setup = null, displayField = null;
            StringTokenizer st = new StringTokenizer(configMap.get("searchFields").toString(), ",");
            StringTokenizer st2 = null;
            StringTokenizer st3 = null;
            if (!configMap.get("searchFields").toString().contains(";")) {
                st2 = new StringTokenizer(configMap.get("searchFieldsLabel").toString(), ",");
                st3 = new StringTokenizer(configMap.get("searchFieldsDbName").toString(), ",");
            }
//                        Debug.printFrameworkDebug("session search condition = " + str);
            int index = -1;
            String[] fieldArr = null;
            while (st.hasMoreTokens()) {
                if (st2 == null) {
                    fieldArr = st.nextToken().trim().split(";");
                    searchField = fieldArr[0];
                    if (searchField.endsWith("_fromTo_split")) {
                        searchFieldsMap.put(searchField.substring(6, (searchField.length() - 13)) + "_splitDate", "");
                        searchField = searchField.substring(0, (searchField.length() - 6));
                    }
                    if (Validator.isEmpty(fieldArr[1])) {
                        searchFieldsMap.put(searchField+"_label", getText(getDynamicActionSetup(getAction())+"."+fieldArr[0]));
                        getSearchFieldsLabel().add(getText(getDynamicActionSetup(getAction())+"."+fieldArr[0]));
                    } else {
                        searchFieldsMap.put(searchField+"_label", getText(fieldArr[1].trim()));
                        getSearchFieldsLabel().add(getText(fieldArr[1].trim()));
                    }
                    if (Validator.isEmpty(fieldArr[2])) {
                        searchFieldsMap.put(searchField+"_dbName", fieldArr[0].trim());
                        getSearchFieldsDbName().add(fieldArr[0].trim());
                    } else {
                        searchFieldsMap.put(searchField+"_dbName", fieldArr[2].trim());
                        getSearchFieldsDbName().add(fieldArr[2].trim());
                    }
                } else {
                    searchField = st.nextToken().trim();
                    searchFieldsMap.put(searchField+"_label", getText(st2.nextToken().trim()));
                    searchFieldsMap.put(searchField+"_dbName", st3.nextToken().trim());
                    getSearchFieldsLabel().add(searchFieldsMap.get(searchField+"_label"));
                    getSearchFieldsDbName().add(searchFieldsMap.get(searchField+"_dbName"));
                }
                index++;
                if (searchField.startsWith("_date_")) {
                    if (!isMixConfig) {
                        if (searchFields_with_dateFromTo == null) {
                            searchFields_with_dateFromTo = new ArrayList();
                        }
                        if (searchField.endsWith("_fromTo")) {
                            searchFields_with_dateFromTo.add(searchField.substring(6, (searchField.length() - 7)));
                        }
                    }
                    setUsePopupCalander("Y");
                }
                if (configMap.get(searchField + "_ac") != null) {
                    String[] strArr = ((String) configMap.get(searchField + "_ac")).split(";");
                    Method m = getCommList().getClass().getMethod(strArr[0]);
                    useAc_ = Boolean.TRUE;
                    if (strArr.length > 2) {
                        acSetupList.add(new AutoComplete((List) m.invoke(commList), "search_" + searchField, strArr[1], strArr[1], "search_" + searchField, "", "", strArr[2].equalsIgnoreCase("true"), (strArr.length == 4 ? strArr[3] : null)));
                    } else {
                        acSetupList.add(new AutoComplete((List) m.invoke(commList), "search_" + searchField, strArr[1], strArr[1], "search_" + searchField, "", ""));
                    }
                }
                getSearchFields().add(searchField);
                if (configMap.get(searchField + "_lookupSearch") != null) {
                    searchFieldsMap.put(searchField+"_lookupSearch", (String)configMap.get(searchField + "_lookupSearch"));
                    searchFieldLookup.put(searchField + "_lookupSearch", configMap.get(searchField + "_lookupSearch"));
                }
                if (configMap.get(searchField + "_defaultValue") != null) {
                    Debug.printFrameworkDebug("searchField_defaultValue = " + searchField);
                    searchFieldsMap.put(searchField+"_defaultValue", (String)configMap.get(searchField + "_defaultValue"));
                    searchFieldsDefaultData.add(searchField + ";" + configMap.get(searchField + "_defaultValue"));
                }
                if (configMap.get(searchField + "_helperText") != null) {
                    searchFieldsMap.put(searchField+"_helperText", (String)configMap.get(searchField + "_helperText"));
                    searchFieldsHelperText.put(searchField, configMap.get(searchField + "_helperText"));
                }
                if (configMap.get(searchField + "_itemChange") != null) {
                    String[] itemChangeArr = ((String)configMap.get(searchField + "_itemChange")).split(",");
                    searchFieldsMap.put(searchField+"_itemChange", (String)configMap.get(searchField + "_itemChange"));
                    searchFieldsMap.put(searchField+"_refreshItem", itemChangeArr[0].replaceAll("'", ""));
                }
                if (configMap.get(searchField + "_dd") != null) {
                    searchFieldDD_setup = configMap.get(searchField + "_dd").toString();
                    listSetup = searchFieldDD_setup.split(";");
                    searchFieldDD.put(searchField + "_dd", getListFromSetup(searchFieldDD_setup));
                    searchFieldDD.put(searchField + "_dd_list", listSetup[2]);   // Added by Delvene @ 29-Aug-2013 :: To identify list is from setup code table or normal option
                    searchFieldDD.put(searchField + "_dd_key", listSetup[3]);
                    searchFieldDD.put(searchField + "_dd_value", listSetup[4]);
                    if (listSetup.length == 7) {//listSetup[6] must be the searchField's DB Name
                        getSearchFields_dd_validateCriteria().add(searchField + ";" + listSetup[6]);
                    }
                    if (configMap.get(searchField + "_dd_type") != null) {
                        getSearchFieldDD().put(searchField + "_dd_type", configMap.get(searchField + "_dd_type"));
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
                if (str != null) {
                    Boolean found = Boolean.FALSE;
                    Boolean isDate = Boolean.FALSE;
                    String[] conditionArr = str.split("&");
                    String tempDateData = null;
                    String paramData = null;
                    for (String s : conditionArr) {
                        if (searchField.startsWith("_date_")) {
                            isDate = Boolean.TRUE;
                            if (searchField.endsWith("_fromTo")) {
                                if (s.split("=")[0].equals("search_" + extractSearchFieldForDate(searchField) + "From")) {
                                    found = true;
                                    searchFieldsMap.put("search_" + extractSearchFieldForDate(searchField) + "From", s.substring(s.indexOf("=") + 1));
                                    getSearchFieldsDateData().put("search_" + extractSearchFieldForDate(searchField) + "From", s.substring(s.indexOf("=") + 1));
                                    tempDateData = s.substring(s.indexOf("=") + 1);
                                }
                                if (s.split("=")[0].equals("search_" + extractSearchFieldForDate(searchField) + "To")) {
                                    found = true;
                                    searchFieldsMap.put("search_" + extractSearchFieldForDate(searchField) + "To", s.substring(s.indexOf("=") + 1));
                                    getSearchFieldsDateData().put("search_" + extractSearchFieldForDate(searchField) + "To", s.substring(s.indexOf("=") + 1));
                                    paramData = s.substring(s.indexOf("=") + 1);
                                }

                            } else {
                                if (s.split("=")[0].equals("search_" + extractSearchFieldForDate(searchField))) {
                                    found = true;
                                    searchFieldsMap.put("search_"+searchField, s.substring(s.indexOf("=") + 1));
                                    getSearchFieldsDataMap().put("search_" + extractSearchFieldForDate(searchField), s.substring(s.indexOf("=") + 1));
                                    tempDateData = s.substring(s.indexOf("=") + 1);
                                }
                            }
                        } else if (searchField.startsWith("_hidden_")) { // Added by Delvene @ 19-May-2015 :: To support searchable hidden field
                            if (s.split("=")[0].equals("search_" + searchField)) {
                                found = true;
                                searchFieldsMap.put("search_"+searchField, s.substring(s.indexOf("=") + 1));
                                getSearchFieldsDataMap().put("search_" + searchField, s.substring(s.indexOf("=") + 1));
                                tempDateData = s.substring(s.indexOf("=") + 1);
                            }

                        } else if (s.split("=")[0].equals("search_" + searchField)) {
                            found = true;
                            searchFieldsMap.put("search_"+searchField, replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                            getSearchFieldsData().add(replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                            getSearchFieldsDataMap().put("search_" + searchField, replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                            getSearchedParam().put(searchField, replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                            break;
                        } else if (s.split("=")[0].equals("pageSize")) {
                            this.pageSize = new Integer(s.split("=")[1]);
                        }
                    }
                    if (found && isDate) {
                        if (!Validator.isEmpty(tempDateData) || !Validator.isEmpty(paramData)) {
                            if (searchField.endsWith("_fromTo")) {
                                date_fromTo_criteria(getSearchedParam(), paramData, tempDateData, (String) getSearchFieldsDbName().get(index));
                            } else {
                                date_criteria(getSearchedParam(), tempDateData, (String) getSearchFieldsDbName().get(index));
                            }
                        }
                    } else if (!found) {
                        searchFieldsMap.put("search_"+searchField, "");
                        getSearchFieldsData().add("");
                        getSearchFieldsDataMap().put("search_" + searchField, "");
                    }
                } else {
                    if (searchField.startsWith("_date_")) {
                        getSearchFieldsData().add("");
                        getSearchFieldsDataMap().put("search_" + searchField, "");
                        if (searchField.endsWith("_fromTo")) {
                            searchField = extractSearchFieldForDate(searchField);
                            if (configMap.get(searchField + "From_defaultValue") != null) {
                                searchFieldsMap.put(searchField+"From_defaultValue", (String)configMap.get(searchField + "From_defaultValue"));
                                searchFieldsDefaultData.add(searchField + "From;" + configMap.get(searchField + "From_defaultValue"));
                            }
                            if (configMap.get(searchField + "To_defaultValue") != null) {
                                searchFieldsMap.put(searchField+"To_defaultValue", (String)configMap.get(searchField + "To_defaultValue"));
                                searchFieldsDefaultData.add(searchField + "To;" + configMap.get(searchField + "To_defaultValue"));
                            }
                        } else {
                            searchField = extractSearchFieldForDate(searchField);
                            if (configMap.get(searchField + "_defaultValue") != null) {
                                searchFieldsMap.put(searchField+"_defaultValue", (String)configMap.get(searchField + "_defaultValue"));
                                searchFieldsDefaultData.add(searchField + ";" + configMap.get(searchField + "_defaultValue"));
                            }
                        }
                    } else {
                        searchFieldsMap.put("search_"+searchField, "");
                        getSearchFieldsData().add("");
                        getSearchFieldsDataMap().put("search_" + searchField, "");
                    }
                }
            }
            
            if (configMap.get("moreSearchFields") != null) {
                hasMoreSearchField = Boolean.TRUE;
                st = new StringTokenizer(configMap.get("moreSearchFields").toString(), ",");
                while (st.hasMoreTokens()) {
                    searchField = st.nextToken().trim();
                    if (searchField.endsWith(";;")) searchField = searchField.substring(0, searchField.length()-2) + "; ; ";
                    fieldArr = searchField.split(";", 0);
                    searchField = fieldArr[0];
                    if (Validator.isEmpty(fieldArr[1])) {
                        searchFieldsMap.put(searchField+"_label", getText(getAction()+"."+fieldArr[0]));
                        getMoreSearchFieldsLabel().add(getText(getAction()+"."+fieldArr[0]));
                    } else {
                        searchFieldsMap.put(searchField+"_label", getText(fieldArr[1].trim()));
                        getMoreSearchFieldsLabel().add(getText(fieldArr[1].trim()));
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
                        if (!isMixConfig) {
                            if (searchFields_with_dateFromTo == null) {
                                searchFields_with_dateFromTo = new ArrayList();
                            }
                            if (searchField.endsWith("_fromTo")) {
                                searchFields_with_dateFromTo.add(searchField.substring(6, (searchField.length() - 7)));
                            }
                        }
                        setUsePopupCalander("Y");
                    }
                    if (configMap.get(searchField + "_ac") != null) {
                        String[] strArr = ((String) configMap.get(searchField + "_ac")).split(";");
                        Method m = getCommList().getClass().getMethod(strArr[0]);
                        useAc_ = Boolean.TRUE;
                        if (strArr.length > 2) {
                            acSetupList.add(new AutoComplete((List) m.invoke(commList), "search_" + searchField, strArr[1], strArr[1], "search_" + searchField, "", "", strArr[2].equalsIgnoreCase("true"), (strArr.length == 4 ? strArr[3] : null)));
                        } else {
                            acSetupList.add(new AutoComplete((List) m.invoke(commList), "search_" + searchField, strArr[1], strArr[1], "search_" + searchField, "", ""));
                        }
                    }
                    getMoreSearchFields().add(searchField);
                    if (configMap.get(searchField + "_lookupSearch") != null) {
                        searchFieldsMap.put(searchField+"_dbName", (String)configMap.get(searchField + "_lookupSearch"));
                        searchFieldLookup.put(searchField + "_lookupSearch", configMap.get(searchField + "_lookupSearch"));
                    }
                    if (configMap.get(searchField + "_defaultValue") != null) {
                        searchFieldsMap.put(searchField+"_defaultValue", (String)configMap.get(searchField + "_defaultValue"));
                        searchFieldsDefaultData.add(searchField + ";" + configMap.get(searchField + "_defaultValue"));
                    }
                    if (configMap.get(searchField + "_helperText") != null) {
                        searchFieldsMap.put(searchField+"_helperText", (String)configMap.get(searchField + "_helperText"));
                        searchFieldsHelperText.put(searchField, configMap.get(searchField + "_helperText"));
                    }
                    if (configMap.get(searchField + "_dd") != null) {
                        searchFieldDD_setup = configMap.get(searchField + "_dd").toString();
                        listSetup = searchFieldDD_setup.split(";");
                        searchFieldDD.put(searchField + "_dd", getListFromSetup(searchFieldDD_setup));
                        searchFieldDD.put(searchField + "_dd_list", listSetup[2]);   // Added by Delvene @ 29-Aug-2013 :: To identify list is from setup code table or normal option
                        searchFieldDD.put(searchField + "_dd_key", listSetup[3]);
                        searchFieldDD.put(searchField + "_dd_value", listSetup[4]);
                        if (listSetup.length == 7) {//listSetup[6] must be the searchField's DB Name
                            getSearchFields_dd_validateCriteria().add(searchField + ";" + listSetup[6]);
                        }
                        if (configMap.get(searchField + "_dd_type") != null) {
                            getSearchFieldDD().put(searchField + "_dd_type", configMap.get(searchField + "_dd_type"));
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
                    if (str != null) {
                        Boolean found = Boolean.FALSE;
                        Boolean isDate = Boolean.FALSE;
                        String[] conditionArr = str.split("&");
                        String tempDateData = null;
                        String paramData = null;
                        for (String s : conditionArr) {
                            if (searchField.startsWith("_date_")) {
                                isDate = Boolean.TRUE;
                                if (searchField.endsWith("_fromTo")) {
                                    if (s.split("=")[0].equals("search_" + extractSearchFieldForDate(searchField) + "From")) {
                                        found = true;
                                        searchFieldsMap.put("search_" + extractSearchFieldForDate(searchField) + "From", s.substring(s.indexOf("=") + 1));
                                        getSearchFieldsDateData().put("search_" + extractSearchFieldForDate(searchField) + "From", s.substring(s.indexOf("=") + 1));
                                        tempDateData = s.substring(s.indexOf("=") + 1);
                                    }
                                    if (s.split("=")[0].equals("search_" + extractSearchFieldForDate(searchField) + "To")) {
                                        found = true;
                                        searchFieldsMap.put("search_" + extractSearchFieldForDate(searchField) + "To", s.substring(s.indexOf("=") + 1));
                                        getSearchFieldsDateData().put("search_" + extractSearchFieldForDate(searchField) + "To", s.substring(s.indexOf("=") + 1));
                                        paramData = s.substring(s.indexOf("=") + 1);
                                    }

                                } else {
                                    if (s.split("=")[0].equals("search_" + extractSearchFieldForDate(searchField))) {
                                        found = true;
                                        searchFieldsMap.put("search_" + extractSearchFieldForDate(searchField), s.substring(s.indexOf("=") + 1));
                                        getSearchFieldsDataMap().put("search_" + extractSearchFieldForDate(searchField), s.substring(s.indexOf("=") + 1));
                                        tempDateData = s.substring(s.indexOf("=") + 1);
                                    }
                                }
                            } else if (searchField.startsWith("_hidden_")) { // Added by Delvene @ 19-May-2015 :: To support searchable hidden field
                                if (s.split("=")[0].equals("search_" + searchField)) {
                                    found = true;
                                    searchFieldsMap.put("search_" + searchField, s.substring(s.indexOf("=") + 1));
                                    getSearchFieldsDataMap().put("search_" + searchField, s.substring(s.indexOf("=") + 1));
                                    tempDateData = s.substring(s.indexOf("=") + 1);
                                }

                            } else if (s.split("=")[0].equals("search_" + searchField)) {
                                found = true;
                                searchFieldsMap.put("search_" + searchField, replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                                getSearchFieldsData().add(replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                                getSearchFieldsDataMap().put("search_" + searchField, replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                                getSearchedParam().put(searchField, replaceSpecialChar_rev(s.substring(s.indexOf("=") + 1)));
                                break;
                            } else if (s.split("=")[0].equals("pageSize")) {
                                this.pageSize = new Integer(s.split("=")[1]);
                            }
                        }
                        if (found && isDate) {
                            if (!Validator.isEmpty(tempDateData)) {
                                if (searchField.endsWith("_fromTo")) {
                                    date_fromTo_criteria(getSearchedParam(), paramData, tempDateData, (String) getMoreSearchFieldsDbName().get(index));
                                } else {
                                    date_criteria(getSearchedParam(), tempDateData, (String) getMoreSearchFieldsDbName().get(index));
                                }
                            }
                        } else if (!found) {
                            searchFieldsMap.put("search_" + searchField, "");
                            getSearchFieldsData().add("");
                            getSearchFieldsDataMap().put("search_" + searchField, "");
                        }
                    } else {
                        searchFieldsMap.put("search_" + searchField, "");
                        getSearchFieldsData().add("");
                        getSearchFieldsDataMap().put("search_" + searchField, "");
                    }
                }
            }

            if (configMap.get("hiddenFields") != null) {
                st = new StringTokenizer(configMap.get("hiddenFields").toString(), ",");
                while (st.hasMoreTokens()) {
                    Debug.printFrameworkDebug("hidden fields not empty---- ");
                    getHiddenFields().add(st.nextToken().trim());
                }
            }

            //Added by Zhafari @ 29-May-2015 extraCondition
            if (configMap.get("extraCondition_trigger") != null && configMap.get("extraCondition") != null) {
                st = new StringTokenizer(configMap.get("extraCondition_trigger").toString(), ",");
                st2 = new StringTokenizer(configMap.get("extraCondition").toString(), ",");
                while (st.hasMoreTokens()) {
                    getExtraConditionMap().put(st.nextToken().trim(), st2.nextToken().trim());
                }
            }
            //Added by Zhafari @ 29-May-2015 extraCondition - END

            st = new StringTokenizer(configMap.get("displayFields").toString(), ",");
            if (configMap.get("displayFields").toString().contains(";")) {
                st2 = null;
            } else {
                st2 = new StringTokenizer(configMap.get("displayFieldsHeader").toString(), ",");
            }
            while (st.hasMoreTokens()) {
                if (st2 == null) {
                    fieldArr = st.nextToken().trim().split(";");
                    displayField = fieldArr[0].trim();
                    if (Validator.isEmpty(fieldArr[1])) {
                        getDisplayFieldsHeader().add(getText((getAction()+"."+fieldArr[0]).trim()));
                    } else {
                        getDisplayFieldsHeader().add(getText(fieldArr[1].trim()));
                    }
                } else {
                    displayField = st.nextToken().trim();
                    getDisplayFieldsHeader().add(getText(st2.nextToken().trim()));
                }
                getDisplayFields().add(displayField);
                
                if (configMap.get("decFormat_" + displayField) != null) {
                    searchFieldFormat.put("decFormat_" + displayField, configMap.get("decFormat_" + displayField));
                }
                if (configMap.get("propertyText_" + displayField) != null) {
//                                Debug.printFrameworkDebug("aaa = "+configMap.get("propertyText_"+displayField));
//                                Debug.printFrameworkDebug("aaa = "+displayField);
                    searchFieldFormat.put("propertyText_" + displayField, configMap.get("propertyText_" + displayField));
                }
                if (configMap.get("functionText_" + displayField) != null) {
                    searchFieldFormat.put("functionText_" + displayField, configMap.get("functionText_" + displayField));
                }
                if (configMap.get("styleFormat_" + displayField) != null) {
                    searchFieldStyleFormat.put("styleFormat_" + displayField, configMap.get("styleFormat_" + displayField));
                }
                if (configMap.get("searchInputStyleFormat_" + displayField) != null) { //Added by Zhafari @ 11-Nov-2014 - for style at search input
                    searchFieldStyleFormat.put("searchInputStyleFormat_" + displayField, configMap.get("searchInputStyleFormat_" + displayField));
                }
            }
            if (configMap.get("defaultSorting") != null) {
                if (Validator.isEmpty(getDynamicSortBy())) {
                    setDynamicSortBy(configMap.get("defaultSorting").toString().split(";")[0]);
                    setDynamicSortOrder(configMap.get("defaultSorting").toString().split(";")[1]);
                }
            } else {
                if (Validator.isEmpty(getDynamicSortBy())) {
                    setDynamicSortBy(getDisplayFields().get(0));
                    setDynamicSortOrder("A");
                }
            }
            if (configMap.get("sortingFields") != null) {
                if (configMap.get("sortingFields").equals("asDisplayFields")) {
                    sortingFields = displayFields;
                } else {
                    st = new StringTokenizer(configMap.get("sortingFields").toString(), ",");
                    while (st.hasMoreTokens()) {
                        sortingFields.add(st.nextToken().trim());
                    }
                }
                if (Validator.isEmpty(dynamicSortBy)) {
                    dynamicSortBy = sortingFields.get(0);
                }
            }
            if (configMap.get("sortingByIndex") != null) {
                if (dynamicSortByIndex == null) {
                    dynamicSortByIndex = new HashMap();
                }
                st = new StringTokenizer(configMap.get("sortingByIndex").toString(), ",");
                while (st.hasMoreTokens()) {
                    String[] strArr = st.nextToken().trim().split(";");
                    dynamicSortByIndex.put(strArr[0], strArr[1]);
                }
            }
            if (searchFieldsDefaultData.size() > 0){
                String[] searchFieldDefaultDataArr;
                for (String defValue : searchFieldsDefaultData){
                    searchFieldDefaultDataArr = defValue.split(";");
                    String data = null;
                    if (searchFieldDefaultDataArr[1].startsWith("func:")) {
                        data = DynamicPreSearch.getDefaultValue(searchFieldDefaultDataArr[0],searchFieldDefaultDataArr[1], searchFieldsDateData);
                    } else {
                        data = searchFieldDefaultDataArr[1];
                    }
                    if (searchFieldsDataMap.containsKey("search_"+searchFieldDefaultDataArr[0]) && !Validator.isEmpty((String)searchFieldsDataMap.get("search_"+searchFieldDefaultDataArr[0]))) {
                        searchFieldsDataMap.put("search_"+searchFieldDefaultDataArr[0], data);
                    } else {
                        searchFieldsDataMap.put("search_"+searchFieldDefaultDataArr[0], data);
                    }
                    
                }
            }
            setRetrievingColumns(configMap.get("retrievingColumns").toString());
            setSqlTables(configMap.get("sqlTables").toString());
            if (configMap.get("countDistinctColumns") != null) {
                this.countDistinctColumns = "distinct " + (String) configMap.get("countDistinctColumns");
            } else {
                this.countDistinctColumns = "*";
            }
            //Added by Zhafari @ 10-Jun-2015 sqlTables_other
            if (configMap.get("sqlTables_other_trigger") != null && configMap.get("sqlTables_other") != null) {
                st = new StringTokenizer(configMap.get("sqlTables_other_trigger").toString(), ",");
                st2 = new StringTokenizer(configMap.get("sqlTables_other").toString(), ",");
                while (st.hasMoreTokens()) {
                    getSqlTablesMap_other().put(st.nextToken().trim(), st2.nextToken().trim());
                }
            }
            //Added by Zhafari @ 10-Jun-2015 sqlTables_other - END

            setListPage(configMap.get("listPage").toString());
            setSearchPage(configMap.get("searchPage").toString());
            setSpecialSearch((String) configMap.get("specialSearch"));
            //setEditPage(configMap.get("editPage").toString());
            //setAddPage(configMap.get("addPage").toString());
            if (configMap.get("editPageURL") != null) {
                setEditPageURL(configMap.get("editPageURL").toString());
            }
            if (configMap.get("addPageURL") != null) {
                setAddPageURL(configMap.get("addPageURL").toString());
            }
            if (configMap.get("deleteURL") != null) {
                setDeleteURL(configMap.get("deleteURL").toString());
            }
            if (!isMixConfig) {
                setSearchDescription(configMap.get("searchDescription").toString());
            }
            setSqlOrderBy(configMap.get("sqlOrderBy") == null ? "" : configMap.get("sqlOrderBy").toString());
            setPagingURL(configMap.get("pagingURL") == null ? "search2Dynamic" : configMap.get("pagingURL").toString());
            setDcGroupBy(configMap.get("groupBy") == null ? "" : configMap.get("groupBy").toString());
            setPrimaryKeyColumn(configMap.get("primaryKey") == null ? "" : configMap.get("primaryKey").toString());
            st = new StringTokenizer(configMap.get("editLinkColumn") == null ? "" : configMap.get("editLinkColumn").toString(), ",");
            while (st.hasMoreTokens()) {
                getEditLinkColumn().add(st.nextToken().trim());
            }
            st = new StringTokenizer(configMap.get("displayFieldsLink") == null ? "" : configMap.get("displayFieldsLink").toString(), ",");
            String[] strArr = null;
            while (st.hasMoreTokens()) {
                strArr = st.nextToken().trim().split(";;");
                getEditLinkColumn().add(strArr[0].trim() + "_otherLink");
                getDisplayColumnLink().put(strArr[0].trim() + "_otherLink", strArr[1].trim());
                if (strArr.length >= 3) {
                    getDisplayColumnLink().put(strArr[0].trim() + "_style", strArr[2].trim());
                }
            }
            //to get the display field that want to set the style for display column
            st = new StringTokenizer(configMap.get("styleFromPackage") == null ? "" : configMap.get("styleFromPackage").toString(), ",");
            String tokenValue = null;
            while (st.hasMoreTokens()) {
                tokenValue = st.nextToken().trim();
                if (tokenValue.indexOf(";") > 0) {
                    getSearchFieldFormat().put(tokenValue.split(";")[0] + "_style_package", tokenValue.substring(tokenValue.indexOf(";")+1));
                } else {
                    getSearchFieldFormat().put(tokenValue + "_style_package", (String) searchFieldFormat.get("propertyText_" + tokenValue));
                }
            }
            if (retrieve.equalsIgnoreCase("y")) {
                int tempPageNo = 1;
                if (!useDataTable) {
                    setPaging(Boolean.TRUE);
                    setSearched(Boolean.TRUE);
                    populateSearchOrderAndPageNo();
                    if (getPageNo() != null) {
                        tempPageNo = getPageNo();
                    }
                } else {
                    setPaging(Boolean.FALSE);
                }
                try {
                    checkPreSearch(getSearchedParam());
                    check_dd_criteria(getSearchedParam());
                    checkFilterBySessionUsId(getSearchedParam());  // Added by ChangMH @ 09-Jul-2014 :: add in user's login PK in to the param if session_us_id_filter in config map is not null
                    if (getIsMixConfig()) {
                        if (getSetupMap().get("searchCodeDbName") != null) {
                            String[] searchCodeData = searchCode.split("::");
                            if (searchCodeData.length == 2) {
                                getSearchedParam().put(SystemConstants.CRITERIA.NO_CHANGE + getSetupMap().get("searchCodeDbName"), searchCodeData[1]);
                            }
                        }
                    }
//                                Debug.printFrameworkDebug("POPULATE:: dynamicSortBy="+getDynamicSortBy()+ ", dynamicSortOrder="+getDynamicSortOrder());
                    index = 0;
                    for (String checkSearchField : getSearchFields()) {
                        if (getSearchedParam().containsKey(checkSearchField)) {
                            getSearchedParam().put(getSearchFieldsDbName().get(index++), getSearchedParam().remove(checkSearchField));
//                                        param.put(getSearchFieldsDbName().get(index), new String(paramData));
                        } else {
                            index++;
                        }
                    }
                    if (getSpecialSearch() != null) {
                        doSpecialSearch();
                    } else {
                        new DataRetriever().retrieveData2(this, request, request.getSession().getServletContext());
                        if (!useDataTable) {
                            setPageNo(tempPageNo);
                        }
                    }
                    if (str != null && str.indexOf("&pageNo=") >= 0) {
                        setSearchCondition(str.substring(0, str.indexOf("&pageNo=")));
                    } else {
                        setSearchCondition(str);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    new LogFunction().logError(this.getClass(), "", e);
                }
            }
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }

//                String returnStr=null;
//                Map session = ActionContext.getContext().getSession();
//               if(session.get("login_user_type").equals(SystemConstants.SF_SYSTEM_TYPE.PUBLIC))
//               {
//                   returnStr= "success_el";
//               }else if(session.get("login_user_type").equals(SystemConstants.SF_SYSTEM_TYPE.INTERNAL))
//               {
//                   returnStr = SUCCESS;
//               }
        return SUCCESS;

    }

    public void check_dd_criteria(Map param) {
        String[] searchDD_setup;
        Boolean found = null;
        for (String searchFieldDbName : getSearchFields_dd_validateCriteria()) {
            searchDD_setup = searchFieldDbName.split(";");
            if (param.containsKey(searchDD_setup[1])) {
                if (searchFieldDD.get(searchDD_setup[0] + "_dd_list").toString().equals("option")) {
                    List<Options> list = (List) searchFieldDD.get(searchDD_setup[0] + "_dd");
                    found = Boolean.FALSE;
                    String tempCriteria = null;
                    for (Options opt : list) {
                        if (opt.getKeyData().equals(param.get(searchDD_setup[1]))) {
                            found = Boolean.TRUE;
                            break;
                        } else {
                            if (tempCriteria == null) {
                                tempCriteria = opt.getKeyData();
                            } else {
                                tempCriteria += "," + opt.getKeyData();
                            }
                        }
                    }
                    if (!found) {
                        if (Validator.isEmpty((String) param.get(searchDD_setup[1]))) {
                            param.put(searchDD_setup[1], tempCriteria);
                        } else {
                            param.put(searchDD_setup[1], "invalid_" + param.get(searchDD_setup[1]));
                            addActionMessage(getText("mohon.error.invalidCriteria"));
                        }
                    }
                } else {

                }
            } else {
                if (searchFieldDD.get(searchDD_setup[0] + "_dd_list").toString().equals("option")) {
                    List<Options> list = (List) searchFieldDD.get(searchDD_setup[0] + "_dd");
                    found = Boolean.FALSE;
                    String tempCriteria = null;
                    String tempAll = "";
                    for (Options opt : list) {
                        if (Validator.isEmpty(opt.getKeyData())) {
                            tempAll = opt.getValueData();
                            continue;
                        }
                        if (tempCriteria == null) {
                            tempCriteria = opt.getKeyData();
                        } else {
                            tempCriteria += "," + opt.getKeyData();
                        }
                    }
                    if (tempCriteria == null) {
                        addActionError("Drop down for [" + searchDD_setup[0] + "]" + " must not contain '" + tempAll + "' only");
                    }
                    param.put(searchDD_setup[1], tempCriteria);
                }
            }
        }
    }

    private void populateSearchOrderAndPageNo() {
//            Map param = new HashMap();
//            int index = -1;
        try {
//                for (String searchField : getSearchFields()){
//                    index++;
////                    if (searchField.startsWith("_date_")) {
////                        if (searchField.endsWith("_fromTo")) {
////                            if (Validator.isEmpty((String)getSearchFieldsDataMap().get("search_"+extractSearchFieldForDate(searchField)+"From")) ) {
////                                param.put
////                            }
////                            if (Validator.isEmpty((String)getSearchFieldsDataMap().get("search_"+extractSearchFieldForDate(searchField)+"To")) ) {
////                                
////                            }
////                        } else {
////                            
////                        }
////                    } else 
//                    if (! getSearchFieldsData().get(index).equals("") ){
//                        param.put(getSearchFieldsDbName().get(index) , new String(getSearchFieldsData().get(index)));
//                    } else {
//                        String str = (String)getObjectFromSession(getAction()+"_searchCondition");
//                        if (str == null){
//                            break;
//                        }
//                        String[] conditionArr = str.split("&");
//                        for (String s : conditionArr){
//                            if (s.indexOf("dynamicSortBy")==0){
//                                setDynamicSortBy(s.substring(s.indexOf("=")+1));
//                            } else if (s.indexOf("dynamicSortOrder")==0){
//                                setDynamicSortOrder(s.substring(s.indexOf("=")+1));
//                            } else if (s.indexOf("pageNo")==0){
//                                try {
//                                    setPageNo(Integer.parseInt(s.substring(s.indexOf("=")+1)));
//                                } catch (Exception e) {
//                                    setPageNo(1);
//                                }
//                            }
//                        }
//                    }
//                }
            String str = (String) getObjectFromSession(getAction() + "_searchCondition");
            if (str != null) {  // ThoTH @ 1-Mar-2016
                String[] conditionArr = str.split("&");
                for (String s : conditionArr) {
                    if (s.indexOf("dynamicSortBy") == 0) {
                        setDynamicSortBy(s.substring(s.indexOf("=") + 1));
                    } else if (s.indexOf("dynamicSortOrder") == 0) {
                        setDynamicSortOrder(s.substring(s.indexOf("=") + 1));
                    } else if (s.indexOf("pageNo") == 0) {
                        try {
                            setPageNo(Integer.parseInt(s.substring(s.indexOf("=") + 1)));
                        } catch (Exception e) {
                            setPageNo(1);
                        }
                    }
                }
            }
            if (!getDynamicSortOrder().equals("")) {
                setSqlOrderBy(getDynamicSortBy() + " " + (getDynamicSortOrder().equals("A") ? "asc" : "desc"));
            }
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }
//            return param;
    }

    public Map getDynamicActionSetup(String action) {
        try {
//                Debug.printFrameworkDebug("action ======================== " + action);
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            Map<String, String> setupMap = new CommonFunction().getDynamicConfiguration(action, request, request.getSession().getServletContext());
            Map<String, String> extendedMap = null;
            Boolean extendFound = Boolean.TRUE;
            if (setupMap.get("extendsConfig") != null && (setupMap.get("extended") == null)) {
                while (extendFound) {
                    extendedMap = (Map) CommonFunction.getDynamicConfigMap().get(setupMap.get("extendsConfig").toString());
                    setupMap.remove("extendsConfig");
                    //getDynamicActionSetup(setupMap.get("extendsConfig").toString());
                    if (extendedMap.get("extendsConfig") == null) {
                        extendFound = Boolean.FALSE;
                    } else {
                    }
//                        for (String objKey : setupMap.keySet()){
//                            if (!objKey.equals("extendsConfig")){
//                                extendedMap.remove(objKey);
//                                extendedMap.put(objKey, setupMap.get(objKey));
//                            }
//                        }
//                        setupMap = extendedMap;
                    for (String objKey : extendedMap.keySet()) {
                        if (!setupMap.containsKey(objKey)) {
                            setupMap.put(objKey, extendedMap.get(objKey));
                        }
                    }
                }
                setupMap.put("extended", "yes");
            }
            if (getShowPageSize() && pageSizeOption.isEmpty()) {
                if (pageSize == null) {
                    try {
                        pageSize = Integer.parseInt(request.getParameter(action + "_pageSize"));
                    } catch (Exception e) {
                        pageSize = Integer.parseInt(getText("defaultPageSize"));
                    }
                }
                if (setupMap.get("showRecordPerPage") != null) {
                    setShowPageSize(Boolean.parseBoolean(setupMap.get("showRecordPerPage").toString()));
                }
                if (setupMap.get("recordPerPage") != null) {
                    String[] pagingSetup = setupMap.get("recordPerPage").toString().split(";");
                    if (pageSize == null) {
                        pageSize = Integer.parseInt(pagingSetup[0]);
                    }
                    if (getShowPageSize()) {
                        if (pagingSetup.length > 1) {
                            for (String pageSizeItem : pagingSetup[1].split(",")) {
                                pageSizeOption.add(new Options(pageSizeItem, pageSizeItem));
                            }
                        }
                    }
                }
                populatePageSizeIfEmpty();
            }
            if (setupMap.get("editPageURL")==null) setupMap.put("editPageURL", "loadEditPage"+action);
            if (setupMap.get("addPageURL")==null) setupMap.put("addPageURL", "loadAddPage"+action);
            if (setupMap.get("deleteURL")==null) setupMap.put("deleteURL", "delete"+action);
            if (setupMap.get("searchPage")==null) setupMap.put("searchPage", "dynamicSearch");
            if (setupMap.get("listPage")==null) setupMap.put("listPage", "dynamicList");
            return setupMap;
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }
        return null;
    }

    public void validateRequired(ModelBase webModel) throws Exception {
        if (webModel.get_markedAsDel()!=null && webModel.get_markedAsDel().equalsIgnoreCase("Y")) {
            return;
        }
        String[] requiredField = null;
        Method m = null;
        if (Validator.isEmpty(webModel.getID())) { //new=insert
            if (!Validator.isEmpty(webModel.insertRequired())) {
                requiredField = webModel.insertRequired().split(",");
            }
        } else {
            if (!Validator.isEmpty(webModel.updateRequired())) {
                requiredField = webModel.updateRequired().split(",");
            }
        }
        if (requiredField != null) {
            for (String field : requiredField) {
                String[] fieldArr = field.split(";");
                m = webModel.getClass().getMethod("get"+WordUtils.capitalize(fieldArr[0]));
                Object fieldObj = m.invoke(webModel);
                if (fieldObj == null || Validator.isEmpty(fieldObj.toString())) {
                    addActionError(getText("field.required", new String[]{getText(fieldArr[1])}));
                }
            }
        }
        for (String childList_getter : webModel.getMyChildMap().keySet()) {
            
            m = webModel.getClass().getMethod(childList_getter);
            Object listOrModelBase = m.invoke(webModel);
            if (listOrModelBase!=null) {
                if (!(listOrModelBase instanceof ModelBase)) {
                    for (ModelBase child : (List<ModelBase>) listOrModelBase) {
                        validateRequired(child);
                    }
                } else {
                    validateRequired((ModelBase)listOrModelBase);
                }
            }
        }
    }
    public void validateRequired() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        for (String key : requiredParam.keySet()) {
            if (Validator.isEmpty(request.getParameter(key))) {
                Debug.printFrameworkDebug("requiredParam: " + requiredParam.get(key));
                addActionError(getText("field.required", new String[]{getText(requiredParam.get(key))}));
            }
        }
    }

    public void validateRowRequired() {

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

    //DynamicSearch2 methods
    public void populateDynamicActionSetup(String dynamicParam){

            Debug.printFrameworkDebug("BaseActionSupport : populateDynamicActionSetup() dynamicParam = " + dynamicParam);
		//DynamicSearch2
		Map dynamicSearchSetup = new HashMap();
		List dynamicSearchField = new ArrayList();
		List dynamicSortField = new ArrayList();
		List dynamicSearchLabel = new ArrayList();
		List dynamicSearchData = new ArrayList();
		List dynamicSearchFieldsDbName = new ArrayList();
		List dynamicDisplayFieldHeader = new ArrayList();
		List dynamicDisplayField = new ArrayList();
		List dynamicResult = new ArrayList();
		List dynamicColumns = new ArrayList();
                Map dynamicSearchFieldDD = new HashMap();
                Map dynamicSearchFieldLookup = new HashMap();   //Added by Delvene @ 21-Oct-2013 :: To support lookup search
                Map dynamicFieldStyleFormat = new HashMap();   //Added by Delvene @ 15-Oct-2015 :: To support colored status in lookup search
                List dynamicHiddenFields = new ArrayList();     //Added by Delvene @ 21-Oct-2013 :: To support hidden field (E.g. est_type in PtEstablishment)
                List<String> dynamicSearchFieldsDefaultData = new ArrayList();  // Added by Delvene @ 08-Apr-2014 :: To support dropdown default value (E.g post_status in PtPostInfo for PostOperationRecruitment)
		//String dynamicSearcCondition = "";
		String dynamicPrimaryKeyColumn = "";
		String retrievingColumns="";
		dynamicSearchSetup = getDynamicActionSetup(dynamicParam);
                if (dynamicSearchSetup.get("impianSecurityCheck") != null){  // ThenSW @ 06-Mar-2014 :: For Impian Security
                    setImpianSecurityCheck(dynamicSearchSetup.get("impianSecurityCheck").toString());
                }
		StringTokenizer st = new StringTokenizer(dynamicSearchSetup.get("searchFields").toString(), ",");
                StringTokenizer st2 = null;
                StringTokenizer st3 = null;
                if (!dynamicSearchSetup.get("searchFields").toString().contains(";")) {
                    st2 = new StringTokenizer(dynamicSearchSetup.get("searchFieldsLabel").toString(), ",");
                    st3 = new StringTokenizer(dynamicSearchSetup.get("searchFieldsDbName").toString(), ",");
                }
		String[] fieldArr = null;
                String searchField = null;
                    while (st.hasMoreTokens()) {
                        if (st2 == null) {
                            fieldArr = st.nextToken().trim().split(";");
                            searchField = fieldArr[0];
                            if (Validator.isEmpty(fieldArr[1])) {
                                dynamicSearchLabel.add(getText(getDynamicActionSetup(getAction())+"."+fieldArr[0]));
                            } else {
                                dynamicSearchLabel.add(getText(fieldArr[1].trim()));
                            }
                            if (Validator.isEmpty(fieldArr[2])) {
                                dynamicSearchFieldsDbName.add(fieldArr[0].trim());
                            } else {
                                dynamicSearchFieldsDbName.add(fieldArr[2].trim());
                            }
                        } else {
                            searchField = st.nextToken().trim();
                            dynamicSearchLabel.add(getText(st2.nextToken().trim()));
                            dynamicSearchFieldsDbName.add(st3.nextToken().trim());
                        }
                        dynamicSearchField.add(searchField);
			dynamicSearchData.add("");
//			dynamicSearchLabel.add(getText(st2.nextToken().trim()));
//			dynamicSearchFieldsDbName.add(getText(st3.nextToken().trim()));
                        String searchFieldDD_setup = null;

                        //Added by Delvene @ 21-Oct-2013
                        if (dynamicSearchSetup.get(searchField + "_lookupSearch") != null) {
                            dynamicSearchFieldLookup.put(searchField + "_lookupSearch", dynamicSearchSetup.get(searchField + "_lookupSearch"));
                        }
                        //Added by Delvene @ 21-Oct-2013 - END

                        //Added by Delvene @ 08-Apr-2014
                        if (dynamicSearchSetup.get(searchField+"_defaultValue") != null){
                            dynamicSearchFieldsDefaultData.add(searchField+";"+dynamicSearchSetup.get(searchField+"_defaultValue"));
                        }
                        //Added by Delvene @ 08-Apr-2014 - END

                        if (dynamicSearchSetup.get(searchField+"_dd") != null){
                            searchFieldDD_setup = dynamicSearchSetup.get(searchField+"_dd").toString();
                            dynamicSearchFieldDD.put(searchField+"_dd", getListFromSetup(searchFieldDD_setup));
                            dynamicSearchFieldDD.put(searchField+"_dd_list", searchFieldDD_setup.split(";")[2]);
                            dynamicSearchFieldDD.put(searchField+"_dd_key", searchFieldDD_setup.split(";")[3]);
                            dynamicSearchFieldDD.put(searchField+"_dd_value", searchFieldDD_setup.split(";")[4]);
                            if (dynamicSearchSetup.get(searchField+"_dd_type") != null) {
                                dynamicSearchFieldDD.put(searchField+"_dd_type", dynamicSearchSetup.get(searchField+"_dd_type"));
                            }
                        }
		}
                
                //Added by Delvene @ 15-Oct-2015 :: to get the display field that want to set the style for display column
                if (dynamicSearchSetup.get("styleFromPackage") != null) {
                    st = new StringTokenizer(dynamicSearchSetup.get("styleFromPackage").toString(), ",");
                    String tokenValue = null;
                    while (st.hasMoreTokens()){
                        tokenValue = st.nextToken().trim();
                        if (tokenValue.indexOf(";") > 0) {
                            searchFieldFormat.put(tokenValue.split(";")[0]+"_style_package", tokenValue);
                        } else {
                            searchFieldFormat.put(tokenValue+"_style_package", dynamicSearchSetup.get("propertyText_"+tokenValue));
                        }
                    }
                }
                //Added by Delvene @ 15-Oct-2015 :: to get the display field that want to set the style for display column - END

                //Added by Delvene @ 21-Oct-2013
                if (dynamicSearchSetup.get("hiddenFields") != null) {
                    st = new StringTokenizer(dynamicSearchSetup.get("hiddenFields").toString(), ",");
                    while (st.hasMoreTokens()){
                        dynamicHiddenFields.add(st.nextToken().trim());
                    }
                }
                //Added by Delvene @ 21-Oct-2013 - END

                //Added by Delvene @ 08-Apr-2014
                Debug.printFrameworkDebug("111111111111111111111111111111111111111111111");
                if (dynamicSearchFieldsDefaultData.size() > 0){
                    Debug.printFrameworkDebug("222222222222222222222222222222222222222222222");
                    int indexOfSearchField;
                    String[] searchFieldDefaultDataArr;
                    for (String str : (List<String>)dynamicSearchFieldsDefaultData){
                        searchFieldDefaultDataArr = str.split(";");
                        indexOfSearchField = dynamicSearchField.indexOf(searchFieldDefaultDataArr[0]);
                        dynamicSearchData.remove(indexOfSearchField);
                        if (searchFieldDefaultDataArr[1].startsWith("func:")) {
                            dynamicSearchData.add(indexOfSearchField, DynamicPreSearch.getDefaultValue(searchFieldDefaultDataArr[0],searchFieldDefaultDataArr[1], searchFieldsDateData));
                        } else {
                            dynamicSearchData.add(indexOfSearchField, searchFieldDefaultDataArr[1]);
                        }
                    }
                }
                //Added by Delvene @ 08-Apr-2014 - END

                String displayField = null;
		st = new StringTokenizer(dynamicSearchSetup.get("displayFields").toString(), ",");
                if (dynamicSearchSetup.get("displayFields").toString().contains(";")) {
                    st2 = null;
                } else {
                    st2 = new StringTokenizer(dynamicSearchSetup.get("displayFieldsHeader").toString(), ",");
                }
                while (st.hasMoreTokens()){
//                    if (dynamicSearchSetup.get("decFormat_"+displayField) != null){
//                        searchFieldFormat.put("decFormat_"+displayField, dynamicSearchSetup.get("decFormat_"+displayField));
//                    }
//                    if (dynamicSearchSetup.get("propertyText_"+displayField) != null){
//                        searchFieldFormat.put("propertyText_"+displayField, dynamicSearchSetup.get("propertyText_"+displayField));
//                    }
//                    if (dynamicSearchSetup.get("functionText_"+displayField) != null){
//                        searchFieldFormat.put("functionText_"+displayField, dynamicSearchSetup.get("functionText_"+displayField));
//                    }
//                    if (dynamicSearchSetup.get("styleFormat_"+displayField) != null){
//                        searchFieldStyleFormat.put("styleFormat_"+displayField, dynamicSearchSetup.get("styleFormat_"+displayField));
//                    }
                    if (st2 == null) {
                        fieldArr = st.nextToken().trim().split(";");
                        displayField = fieldArr[0];
                        if (Validator.isEmpty(fieldArr[1])) {
                            dynamicDisplayFieldHeader.add(getText(dynamicParam+"."+fieldArr[0]));
                        } else {
                            dynamicDisplayFieldHeader.add(getText(fieldArr[1]));
                        }
                    } else {
                        displayField = st.nextToken().trim();
                        dynamicDisplayFieldHeader.add(getText(st2.nextToken().trim()));
                    }
                    dynamicDisplayField.add(displayField);


                    if (dynamicSearchSetup.get("decFormat_"+displayField) != null){
                        searchFieldFormat.put("decFormat_"+displayField, dynamicSearchSetup.get("decFormat_"+displayField));
                    }
                    if (dynamicSearchSetup.get("propertyText_"+displayField) != null){
                        searchFieldFormat.put("propertyText_"+displayField, dynamicSearchSetup.get("propertyText_"+displayField));
                    }
                    if (dynamicSearchSetup.get("functionText_"+displayField) != null){
                        searchFieldFormat.put("functionText_"+displayField, dynamicSearchSetup.get("functionText_"+displayField));
                    }
                    if (dynamicSearchSetup.get("styleFormat_"+displayField) != null){
                        searchFieldStyleFormat.put("styleFormat_"+displayField, dynamicSearchSetup.get("styleFormat_"+displayField));
                    }
                    

//                    getDisplayFields().add(displayField);
//                    getDisplayFieldsHeader().add(getText(st2.nextToken().trim()));
		}

		if (dynamicSearchSetup.get("sortingFields") != null){
			if (dynamicSearchSetup.get("sortingFields").equals("asDisplayFields")){
				dynamicSortField = dynamicDisplayField;
			} else {
				st = new StringTokenizer(dynamicSearchSetup.get("sortingFields").toString(), ",");
				while (st.hasMoreTokens()){
					dynamicSortField.add(st.nextToken().trim());
				}
			}
                        if (Validator.isEmpty(dynamicSortBy)) {
                            dynamicSortBy = (String)dynamicSortField.get(0);
                        }
		}

		dynamicPrimaryKeyColumn = dynamicSearchSetup.get("primaryKey")==null? "" : dynamicSearchSetup.get("primaryKey").toString();
		retrievingColumns = dynamicSearchSetup.get("retrievingColumns")==null? "" : dynamicSearchSetup.get("retrievingColumns").toString();

		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.SEARCH_LABEL, dynamicSearchLabel);
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.SEARCH_FIELD, dynamicSearchField);
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.SEARCH_FIELD_LOOKUP, dynamicSearchFieldLookup); //Added by Delvene @ 21-Oct-2013
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_HIDDEN_FIELD, dynamicHiddenFields);     //Added by Delvene @ 21-Oct-2013
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.SEARCH_FIELD_DD, dynamicSearchFieldDD);
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.SEARCH_DATA, dynamicSearchData);
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.SEARCH_FIELDS_DB_NAME, dynamicSearchFieldsDbName);
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.DISPLAY_FIELD_HEADER, dynamicDisplayFieldHeader);
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.DISPLAY_FIELD, dynamicDisplayField);
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_RESULT, dynamicResult);
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_COLUMNS, dynamicColumns);
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_SEARCH_CONDITION, "");
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PRIMARY_KEY_COLUMN, dynamicPrimaryKeyColumn);
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.RETRIEVING_COLUMN, retrievingColumns);
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_SORT_FIELD, dynamicSortField);

		/*dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO, null);
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_SIZE, null);
		dynamicSearchSetup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO_OF_ROWS, null);*/
		getDynamicSetupMap().put(dynamicParam, dynamicSearchSetup);
	}

    public List<String> getDynamicSearchField(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (List) setup.get(SystemConstants.DYNAMIC_ATTS.SEARCH_FIELD);
    }

    //Added by Delvene @ 21-Oct-2013
    public Map getDynamicSearchFieldLookup(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (Map) setup.get(SystemConstants.DYNAMIC_ATTS.SEARCH_FIELD_LOOKUP);
    }

    public List<String> getDynamicHiddenFields(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (List) setup.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_HIDDEN_FIELD);
    }
    //Added by Delvene @ 21-Oct-2013 - END

    public Map getDynamicSearchFieldDD(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (Map) setup.get(SystemConstants.DYNAMIC_ATTS.SEARCH_FIELD_DD);
    }

    public List getDynamicSearchLabel(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (List) setup.get(SystemConstants.DYNAMIC_ATTS.SEARCH_LABEL);
    }

    public List getDynamicSearchData(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (List) setup.get(SystemConstants.DYNAMIC_ATTS.SEARCH_DATA);
    }

    public List<String> getDynamicSearchFieldsDbName(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (List) setup.get(SystemConstants.DYNAMIC_ATTS.SEARCH_FIELDS_DB_NAME);
    }

    /*public String getDynamicAddURL(String dynamicParam){
		Map setup = (Map)getDynamicSetupMap().get(dynamicParam);
			return "processSearchApplicationMenu";
		}
		return null;

	}*/
    public List getDynamicDisplayFieldHeader(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (List) setup.get(SystemConstants.DYNAMIC_ATTS.DISPLAY_FIELD_HEADER);
    }

    public List getDynamicDisplayField(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (List) setup.get(SystemConstants.DYNAMIC_ATTS.DISPLAY_FIELD);
    }

    public List getDynamicResult(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (List) setup.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_RESULT);
    }

    public void setDynamicResult(String dynamicParam, List result) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        setup.remove(SystemConstants.DYNAMIC_ATTS.DYNAMIC_RESULT);
        setup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_RESULT, result);
    }

    public List getDynamicColumns(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (List) setup.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_COLUMNS);
    }

    public void setDynamicColumns(String dynamicParam, List columns) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        setup.remove(SystemConstants.DYNAMIC_ATTS.DYNAMIC_COLUMNS);
        setup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_COLUMNS, columns);
    }

    public String getDynamicResultPrimaryKey(String dynamicParam, int row) {
        Map resultMap = (Map) getDynamicResult(dynamicParam).get(row);
        String pk = resultMap.get(getDynamicPrimaryKeyColumn(dynamicParam)).toString();
        return pk;
    }

    public String getDynamicPrimaryKeyColumn(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (String) setup.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PRIMARY_KEY_COLUMN);
    }

    public void setDynamicPrimaryKeyColumn(String dynamicParam, String value) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        setup.remove(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PRIMARY_KEY_COLUMN);
        setup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PRIMARY_KEY_COLUMN, value);
    }

    public void setDynamicSearchCondition(String dynamicParam, String value) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        setup.remove(SystemConstants.DYNAMIC_ATTS.DYNAMIC_SEARCH_CONDITION);
        setup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_SEARCH_CONDITION, value);
    }

    public String getDynamicSearchCondition(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (String) setup.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_SEARCH_CONDITION);
    }

    public Map getDynamicSetupMap() {
        return dynamicSetupMap;
    }

    public Map getDynamicSearchParam(String dynamicParam) {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        Map param = new HashMap();
        int index = 0;
        String searchCondition = "";
        Debug.printFrameworkDebug("dynamicParam = " + dynamicParam);
        for (String str : getDynamicSearchField(dynamicParam)) {
            String paramData = request.getParameter("search_" + str);
            Debug.printFrameworkDebug("search_" + str + " = " + paramData);
            
            if (!Validator.isEmpty(paramData)) {
                param.put(getDynamicSearchFieldsDbName(dynamicParam).get(index), new String(paramData));
                searchCondition += "&search_" + str + "=" + paramData.toString();
            }
            Debug.printFrameworkDebug("getDynamicSearchData(dynamicParam).size() = " + getDynamicSearchData(dynamicParam).size());
            getDynamicSearchData(dynamicParam).set(index, paramData);
            index++;
        }

        if (!Validator.isEmpty(request.getParameter("dynamicSortBy"))) {
            searchCondition += "&dynamicSortBy=" + request.getParameter("dynamicSortBy");
            searchCondition += "&dynamicSortOrder=" + request.getParameter("dynamicSortOrder");
            setSqlOrderBy(request.getParameter("dynamicSortBy") + " " + (request.getParameter("dynamicSortOrder").equals("A") ? "asc" : "desc"));
            setDynamicSortBy(request.getParameter("dynamicSortBy"));
            setDynamicSortOrder(request.getParameter("dynamicSortOrder"));
        }
        setDynamicSearchCondition(dynamicParam, searchCondition);
        return param;
    }

    public Integer getDynamicPageNo(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (Integer) setup.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO);
    }

    public void setDynamicPageNo(String dynamicParam, Integer pageNo) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        setup.remove(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO);
        setup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO, pageNo);
    }

    public Integer getDynamicPageSize(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (Integer) setup.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_SIZE);
    }

    public void setDynamicPageSize(String dynamicParam, Integer value) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        setup.remove(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_SIZE);
        setup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_SIZE, value);
    }

    public Integer getDynamicNumberOfRows(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (Integer) setup.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO_OF_ROWS);
    }

    public void setDynamicNumberOfRows(String dynamicParam, Integer numberOfRows) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        setup.remove(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO_OF_ROWS);
        setup.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO_OF_ROWS, numberOfRows);
    }

    public String getDynamicPagingURL(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (String) setup.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGING_URL);
    }

    public String getDynamicSearchDescription(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (String) setup.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_SEARCH_DESCRIPTION);
    }

    public String getDynamicSearchActionType(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (String) setup.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_SEARCH_ACTION_TYPE);
    }

    public List getDynamicSortField(String dynamicParam) {
        Map setup = (Map) getDynamicSetupMap().get(dynamicParam);
        return (List) setup.get(SystemConstants.DYNAMIC_ATTS.DYNAMIC_SORT_FIELD);
    }

    public void keepObjectToSession(Object obj, String name) {
        ActionContext.getContext().getSession().remove(name);
        ActionContext.getContext().getSession().put(name, obj);
    }

    public Object getObjectFromSession(String name) {
        Object sessionObj = ActionContext.getContext().getSession().get(name);
        return sessionObj;
    }

    public Object getAndRemoveObjectFromSession(String name) {
        return ActionContext.getContext().getSession().remove(name);
    }

    public void keepObjectToSession(Object obj) {
        //remove first before add.
        ActionContext.getContext().getSession().put(obj.getClass().getSimpleName() + "_", obj);
    }

    public Object getObjectFromSession(Object obj) {
        return ActionContext.getContext().getSession().get(obj.getClass().getSimpleName() + "_");
    }

    //will remove the Object from Session once get from session.
    public Object getAndRemoveObjectFromSession(Object obj) {
        return ActionContext.getContext().getSession().remove(obj.getClass().getSimpleName() + "_");
    }

    public String getDisplayFieldsIndex(String disp) {
        if (dynamicSortByIndex != null) {
            if (dynamicSortByIndex.containsKey(disp)) {
                disp = (String) dynamicSortByIndex.get(disp);
            }
        }
        return disp;
    }
    public String getDynamicSortBy_condition() {
        String newSortBy = "1";
        if (Validator.isEmpty(dynamicSortBy)) {
            if (!sortingFields.isEmpty()) {
                newSortBy = sortingFields.get(0);
                if (dynamicSortByIndex != null) {
                    if (dynamicSortByIndex.containsKey(newSortBy)) {
                        return (String) dynamicSortByIndex.get(newSortBy);
                    }
                }
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
                if (dynamicSortByIndex != null) {
                    if (dynamicSortByIndex.containsKey(dynamicSortBy)) {
                        return (String) dynamicSortByIndex.get(dynamicSortBy);
                    }
                }
        }
        return newSortBy;
    }
    
    public String getDynamicSortBy() {
//        Debug.printFrameworkDebug("dynamicSortBy = " + dynamicSortBy);
//        if (Validator.isEmpty(dynamicSortBy)) {
//            if (!sortingFields.isEmpty()) {
//                dynamicSortBy = sortingFields.get(0);
//            }
//        } else {
//            try {
//                Integer.parseInt(dynamicSortBy);//if sorting field is integer/index, direct use
//            } catch (Exception e) {
//                if (!sortingFields.isEmpty()) {
//                    Debug.printFrameworkDebug("1111111111");
//                    if (!sortingFields.contains(dynamicSortBy)) {
//                        Debug.printFrameworkDebug("2222222222222");
//                        dynamicSortBy = sortingFields.get(0);
//                    }
//                } else {
//                    dynamicSortBy = "1";
//                }
//            }
//        }
//        if (dynamicSortByIndex != null) {
//            if (dynamicSortByIndex.containsKey(dynamicSortBy)) {
//                dynamicSortBy = (String) dynamicSortByIndex.get(dynamicSortBy);
//            }
//        }
        return dynamicSortBy;
    }

    public void setDynamicSortBy(String dynamicSortBy) {
        this.dynamicSortBy = dynamicSortBy;
    }

    public String getDynamicSortOrder() {
        if (dynamicSortOrder == null) {
            dynamicSortOrder = "A";
        }
        if (!dynamicSortOrder.equals("D")) {
            dynamicSortOrder = "A";
        }
        return dynamicSortOrder;
    }

    public void setDynamicSortOrder(String dynamicSortOrder) {
        this.dynamicSortOrder = dynamicSortOrder;
    }

    public String getPreviousSearch() {
        return previousSearch;
    }

    public void setPreviousSearch(String previousSearch) {
        this.previousSearch = previousSearch;
    }

    public boolean isSortingField(String fieldName) {
        if (sortingFields.contains(fieldName)) {
            return true;
        }
        return false;
    }

    public boolean isDynamicSortingField(String dynamicParam, String fieldName) {
        if (getDynamicSortField(dynamicParam).contains(fieldName)) {
            return true;
        }
        return false;
    }

    public String cancel() {
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
            Object i;
            try {
                i = c.getDeclaredConstructor(org.hibernate.Session.class).newInstance(baseDAO.getSession());
            } catch (Exception e) {
                i = c.getDeclaredConstructor().newInstance();
            }
            getOtherActionList().add(i);
            //Object i = c.newInstance();
            returnObject = m.invoke(i);
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }

        if (returnObject != null) {
            if (!setupArr[5].equalsIgnoreCase("none")) {
                if (setupArr[2].equalsIgnoreCase("setupcode")) {    //Added by Delvene @ 29-Aug-2013 :: To support drop down list from setup code table
//                    CommonList.addBlankSetupCode((List)returnObject, "", getText(setupArr[5]));
                } else if (setupArr[2].equalsIgnoreCase("option")) {
                    CommonList.addBlankOption((List) returnObject, "", getText(setupArr[5]));
                }
            }
        }
        return (List) returnObject;
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

    public String getHideAddButton() {
        return hideAddButton;
    }

    public void setHideAddButton(String hideAddButton) {
        this.hideAddButton = hideAddButton;
    }

    public String getHideDeleteButton() {
        return hideDeleteButton;
    }

    public void setHideDeleteButton(String hideDeleteButton) {
        this.hideDeleteButton = hideDeleteButton;
    }

    public Boolean getDl_showCheckbox() {
        return dl_showCheckbox;
    }
    public void setDl_showCheckbox(Boolean dl_showCheckbox) {
        this.dl_showCheckbox = dl_showCheckbox;
    }

    public String getDefaultSearchValue() {
        return defaultSearchValue;
    }

    public void setDefaultSearchValue(String defaultSearchValue) {
        this.defaultSearchValue = defaultSearchValue;
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

    public String getRetrieve() {
        return retrieve;
    }

    public void setRetrieve(String retrieve) {
        this.retrieve = retrieve;
    }

    public Map getSearchFieldLookup() {
        return searchFieldLookup;
    }

    public void setSearchFieldLookup(Map searchFieldLookup) {
        this.searchFieldLookup = searchFieldLookup;
    }

    //old has_right(), new one will trigger has_right2()
//    public boolean has_right(String theRight){
////        Debug.printFrameworkDebug("BaseActionSupport : has_right() theRight = " + theRight);
//        if (rightsList.containsKey(theRight)){
//            return rightsList.get(theRight).equalsIgnoreCase("Y");
//        } else {
//            Map sessionMap = ActionContext.getContext().getSession();
//
////            if (sessionMap.get("login_user_type").equals(SystemConstants.SF_SYSTEM_TYPE.PUBLIC)) {
//////                Debug.printFrameworkDebug("has right public......");
////                if (new CommonFunction().validatePublicRight(this.getClass().getSimpleName(), theRight, sessionMap, getAction(), baseDAO.getSession())) {
////                    rightsList.put(theRight, "Y");
////                    return true;
////                }
////            } else {
//                if (new CommonFunction().validateRight(this.getClass().getSimpleName(), theRight, sessionMap, "", baseDAO.getSession())){
//                    rightsList.put(theRight, "Y");
//                    return true;
//                }
////            }
//            rightsList.put(theRight, "N");
//            return false;
//        }
//    }
    public boolean has_right(String theRight) throws Exception {
        if (this.getClass().getSimpleName().equals("DynamicRptAction")) {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            return has_right2(this.getClass().getSimpleName(), theRight, request.getParameter("rptCode"));
        } else {
            return has_right2(this.getClass().getSimpleName(), theRight, getAction());
        }
    }

    public boolean has_right2(String actionClass, String theRight, String paramAction) throws Exception {
//        Debug.printFrameworkDebug("actionClass = " + actionClass);
//        Debug.printFrameworkDebug("theRight = " + theRight);
//        Debug.printFrameworkDebug("paramAction = " + paramAction);
        if (rightsList.containsKey(actionClass + "|" + theRight + "|" + paramAction)) {
            return rightsList.get(actionClass + "|" + theRight + "|" + paramAction).equalsIgnoreCase("Y");
        } else {
            Map sessionMap = ActionContext.getContext().getSession();

//            if (sessionMap.get("login_user_type").equals(SystemConstants.SF_SYSTEM_TYPE.PUBLIC)) {
////                Debug.printFrameworkDebug("has right public......");
//                if (new CommonFunction().validatePublicRight(this.getClass().getSimpleName(), theRight, sessionMap, getAction(), baseDAO.getSession())) {
//                    rightsList.put(theRight, "Y");
//                    return true;
//                }
//            } else {
            if (new CommonFunction().validateRight_sql(actionClass, theRight, sessionMap, paramAction, baseDAO.getSession())) {
                rightsList.put(actionClass + "|" + theRight + "|" + paramAction, "Y");
                return true;
            }
//            }
            rightsList.put(actionClass + "|" + theRight + "|" + paramAction, "N");
            return false;
        }
    }
    
    public boolean has_right_byAppRightCode(String actionClass, String theAppRightCode, Object userId) throws Exception {
//        Debug.printFrameworkDebug("actionClass = " + actionClass);
//        Debug.printFrameworkDebug("theRight = " + theRight);
//        Debug.printFrameworkDebug("paramAction = " + paramAction);
        if (appRightsList.containsKey(actionClass + "|" + theAppRightCode)) {
            return appRightsList.get(actionClass + "|" + theAppRightCode).equalsIgnoreCase("Y");
        } else {
            if (new CommonFunction().validateRight_by_actionClass_appRightCode(actionClass, theAppRightCode, userId, baseDAO)) {
                appRightsList.put(actionClass + "|" + theAppRightCode, "Y");
                return true;
            }
//            }
            appRightsList.put(actionClass + "|" + theAppRightCode, "N");
            return false;
        }
    }

    public String checkNull(String strParam) {
        if (strParam == null) {
            strParam = "";
        }
        return strParam;
    }

    public Object getMethodValueFromObject(Object model, String method) throws Exception {
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
        return obj;
//        try {
//            Method m = model.getClass().getMethod(method);
//            Object returnObj = m.invoke(model);
//            if (returnObj != null){
//                return returnObj.toString();
//            }
//        } catch (Exception e){
//
//        }
    }

    public List<String> checkFieldLength(Object model, String insertOrUpdate, String[] specificColumn) {
        List<String> errorList = new ArrayList();
        try {
            Method m = model.getClass().getMethod("getColumnLengthMap");
            Object returnObj = m.invoke(model);
            if (returnObj != null) {
                Map<String, String> columnLengthMap = (Map) returnObj;
                String[] updatebleColumns = null;
                if (insertOrUpdate.equalsIgnoreCase("update")) {
                    m = model.getClass().getMethod("getUpdatableColumns");
                    updatebleColumns = (String[]) m.invoke(model);
                }
                if (specificColumn != null) {
                    for (String columnName : specificColumn) {
                        if (insertOrUpdate.equalsIgnoreCase("update")) {
                            if (!Arrays.asList(updatebleColumns).contains(WordUtils.capitalize(columnName))) {
                                continue;
                            }
                        }
                        returnObj = getMethodValueFromObject(model, "get" + WordUtils.capitalize(columnName));
                        if (returnObj != null) {
                            if (returnObj.toString().length() > Integer.parseInt(columnLengthMap.get(columnName))) {
                                errorList.add(getText("errors.maxlength", columnName, columnLengthMap.get(columnName)));
                            }
                        }
                    }
                } else {
                    for (String columnName : columnLengthMap.keySet()) {
                        if (insertOrUpdate.equalsIgnoreCase("update")) {
                            if (!Arrays.asList(updatebleColumns).contains(WordUtils.capitalize(columnName))) {
                                continue;
                            }
                        }
                        returnObj = getMethodValueFromObject(model, "get" + WordUtils.capitalize(columnName));
                        if (returnObj != null) {
                            if (returnObj.toString().length() > Integer.parseInt(columnLengthMap.get(columnName))) {
                                errorList.add(getText("errors.maxlength", new String[]{columnName, columnLengthMap.get(columnName)}));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

        return errorList;
    }

    protected void addErrorsFromList(List<String> list) {
        for (String error : list) {
            addActionError(error);
        }
    }

    public String getUsePopupCalander() {
        return usePopupCalander;
    }

    public void setUsePopupCalander(String usePopupCalander) {
        this.usePopupCalander = usePopupCalander;
    }

    public String getSession_us_id_filter() {
        return session_us_id_filter;
    }

    public void setSession_us_id_filter(String session_us_id_filter) {
        this.session_us_id_filter = session_us_id_filter;
    }

    public String getSession_userpk_filter() {
        return session_userpk_filter;
    }

    public void setSession_userpk_filter(String session_userpk_filter) {
        this.session_userpk_filter = session_userpk_filter;
    }

    public String getPreSearchMethod() {
        return preSearchMethod;
    }

    public void setPreSearchMethod(String preSearchMethod) {
        this.preSearchMethod = preSearchMethod;
    }

    public String getPrePopulateMethod() {
        return prePopulateMethod;
    }

    public void setPrePopulateMethod(String prePopulateMethod) {
        this.prePopulateMethod = prePopulateMethod;
    }

    public String getPageRequired() {
        return pageRequired;
    }

//    public void setPageRequired(String pageRequired) {
//        this.pageRequired = pageRequired;
//    }

    public void checkFilterBySessionUsId(Map param) {
        if (getSession_us_id_filter() != null) { //get the t_setup_user's loginId from login session.
            param.put(SystemConstants.CRITERIA.NO_CHANGE + getSession_us_id_filter(), ActionContext.getContext().getSession().get("loginId"));
        }
        if (getSession_userpk_filter() != null) { //get the t_setup_user's PK from login session.
            param.put(SystemConstants.CRITERIA.NO_CHANGE + getSession_userpk_filter(), ActionContext.getContext().getSession().get("userId"));
        }
    }
    
    public void checkFilterBySessionUsDiv(Map param){
        if (getSession_us_div_filter() != null){ 
            String divList = ActionContext.getContext().getSession().get("div_assigned").toString();
            String formattedDivList = divList.replace("[", "").replace("]", "");
            param.put(SystemConstants.CRITERIA.NO_CHANGE+getSession_us_div_filter(), formattedDivList);
        }
    }

    public String getScrollTo_ID() {
        return scrollTo_ID;
    }

    public void setScrollTo_ID(String scrollToID) {
        this.scrollTo_ID = scrollToID;
    }

    public CommonList getCommList() {
        if (commList == null) {
            commList = new CommonList(baseDAO.getSession());
        }
        return commList;
    }
//    public String getSystemType(){
//        return SystemConstants.SF_SYSTEM_TYPE.INTERNAL; //default to Internal System.
//    }
//    protected String getSf_system_type(){
//        return sf_system_type;
//    }
    public void doSpecialSearch() throws Exception {
        Debug.printFrameworkDebug("specialSearch " + specialSearch);
        if (specialSearch != null) {
            boolean methodExists = false;
            Method m = null;
            SpecialSearch specialSearchInstance = new SpecialSearch();
            try {
                m = specialSearchInstance.getClass().getMethod(specialSearch, BaseActionSupport.class, HttpServletRequest.class);
                methodExists = true;
            } catch (Exception e) {
            }

            if (methodExists) {
                try {
                    m.invoke(specialSearchInstance, this, (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST));
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
                Debug.printFrameworkInfo("### For Developer A: \"" + preSearchMethod + "\" defined in dynamic-config by do not implemented in DynamicPreSearch.java ###");
            }
        }
    }

    public void checkPreSearch(Map param) throws Exception {
        String preSearchMethod = getPreSearchMethod();

        if (preSearchMethod != null) {
            boolean methodExists = false;
            Method m = null;
            DynamicPreSearch preSearchTrigger = new DynamicPreSearch();
            try {
                m = preSearchTrigger.getClass().getMethod(preSearchMethod, DynamicAction.class, Map.class);
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

    public void checkPrePopulateSearchPage(Map param) throws Exception {
        String prePopulateMethod = getPrePopulateMethod();

        if (prePopulateMethod != null) {
            boolean methodExists = false;
            Method m = null;
            DynamicPrePopulateSearchPage prePopulateTrigger = new DynamicPrePopulateSearchPage();
            try {
                m = prePopulateTrigger.getClass().getMethod(prePopulateMethod, DynamicAction.class, Map.class);
                methodExists = true;
            } catch (Exception e) {
            }

            if (methodExists) {
                try {
                    m.invoke(prePopulateTrigger, this, param);
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
                Debug.printFrameworkInfo("### For Developer: \"" + prePopulateMethod + "\" defined in dynamic-config do not implemented in DynamicPrePopulateSearchPage.java ###");
            }
        }
    }

    // Created by ThoTH @ 29-Jun-2011
    protected T originalModel = null;

    protected void backupModel() throws Exception {
        originalModel = (T) ObjectCloner.deepCopy(model);
    }

    protected void restoreModel() {
        model = originalModel;
    }

    public String getActionClassName_() {
        return getClass().getSimpleName();
    }

    public String getHc(String actionName, String dynamicConfigName) {
        return new LookupAction().getHc(actionName, dynamicConfigName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    //added by etys for page note
//    public String getPageNote(String pageId) {
//        if (!Validator.isEmpty(pageId)) {
//            try {
//                PageNoteModel pn = (PageNoteModel) baseDAO.getObjectByCode("page_id_str", pageId, new PageNoteModel());
//                if (pn != null) {
//                    if (!pn.getPage_hidden().equalsIgnoreCase("Y")) {
//                        return pn.getPage_note();
//                    }
//                }
//            } catch (Exception e) {
//                new LogFunction().logError(this.getClass(), "", e);
//            }
//        }
//        return "";
//    }
    public void populateLastVisit(String label) {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String url = request.getRequestURI() + "?" + request.getQueryString();
        Map session = ActionContext.getContext().getSession();
        List<String> historyList = (List) session.get("historyList");
        Map<String, String> historyMap = null;
        if (historyList == null) {
            historyList = new ArrayList();
            session.put("historyList", historyList);
            historyMap = new HashMap();
            session.put("historyMap", historyMap);
        } else {
            historyMap = (Map) session.get("historyMap");
        }
        if (historyList.contains(url)) {
            historyList.remove(url);
        } else {
            historyMap.put(url, label);
        }
        historyList.add(0, url);
        while (historyList.size() > 10) {
            historyMap.remove(historyList.remove(10));
        }
    }

    /**
     *
     * @param label
     * @param url
     * @param param: sample "action===Application&&&name2===name2data
     */
    public void populateLastVisit(String label, String url, String param) {
        String criteria = null;
        Map session = ActionContext.getContext().getSession();
        List<String> historyList = (List) session.get("historyList");
        Map<String, String> historyMap = null;
        if (historyList == null) {
            historyList = new ArrayList();
            session.put("historyList", historyList);
            historyMap = new HashMap();
            session.put("historyMap", historyMap);
        } else {
            historyMap = (Map) session.get("historyMap");
        }
//        if (url.indexOf("?") < 0){
//            url += "?";
//        }
        try {
            if (!Validator.isEmpty(param)) {
                String[] dataArr = null;
                for (String paramArr : param.split("&&&")) {
                    dataArr = paramArr.split("===");
                    if (paramArr.indexOf("===") > 0) {
                        if (criteria == null) {
                            criteria = dataArr[0] + "=" + URLEncoder.encode(dataArr[1], "UTF-8");
                        } else {
                            criteria += "&" + dataArr[0] + "=" + URLEncoder.encode(dataArr[1], "UTF-8");
                        }
                        //historyParamMap.put(dataArr[0], dataArr[1]);
                    } else {
                        if (criteria == null) {
                            criteria = paramArr;
                        } else {
                            criteria += "&" + paramArr;
                        }
                        //historyParamMap.put(paramArr, "");
                    }
                }
            }
        } catch (Exception e) {
        }
        criteria = "?" + criteria;
        url += criteria;
        if (historyList.contains(url)) {
            historyList.remove(url);
        } else {
            historyMap.put(url, label);
        }
        historyList.add(0, url);
        while (historyList.size() > 10) {
            historyMap.remove(historyList.remove(10));
        }
    }

    public String getEditLinkStyle() {
        return editLinkStyle;
    }

    public void setEditLinkStyle(String editLinkStyle) {
        this.editLinkStyle = editLinkStyle;
    }

    public Map<String, Object> getSearchFieldsHelperText() {
        return searchFieldsHelperText;
    }

    public void setSearchFieldsHelperText(Map<String, Object> searchFieldsHelperText) {
        this.searchFieldsHelperText = searchFieldsHelperText;
    }

    public String getImpianSecurityCheck() {
        return impianSecurityCheck;
    }

    public void setImpianSecurityCheck(String impianSecurityCheck) {
        this.impianSecurityCheck = impianSecurityCheck;
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

    public String getCountDistinctColumns() {
        return countDistinctColumns;
    }

    public void setCountDistinctColumns(String countDistinctColumns) {
        this.countDistinctColumns = countDistinctColumns;
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
                    paramClassArr = new Class[]{Map.class};
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
                returnObj = m.invoke(instanceObject, resultMap);

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

    public String getFormattedField_dynamic(String param, String value, int row) {
        Object instanceObject = null;
        Class c = null;
        Method m = null;
        Map resultMap = ((Map) getDynamicResult(param).get(row));
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

    public Map<String, String> getDisplayColumnLink() {
        return displayColumnLink;
    }

    public void setDisplayColumnLink(Map<String, String> displayColumnLink) {
        this.displayColumnLink = displayColumnLink;
    }

    public void populateDD(String param) {
        String[] paramArr = param.split("@@");
        if (!getSearchFieldDD().containsKey(paramArr[0])) {
            try {
                Method m = getCommList().getClass().getDeclaredMethod(param.split("@@")[2]);
                List list = (List)m.invoke(getCommList());
                if (paramArr.length > 3) {
                    if (!paramArr[3].equalsIgnoreCase("none")) {
                        CommonList.addBlankOption(list, "", getText(paramArr[3]));
                    }
                }
                getSearchFieldDD().put(paramArr[0], list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //this method will return the link or the style
    public String getDisplayFieldLink(String value, Map<String, Object> result) {
        String url = getDisplayColumnLink().get(value);
        if (value.endsWith("_otherLink")) {
            String temp = null;
            String temp2 = null;
            int startIdx = 0, endIdx = 0;
            while (url.indexOf("{:") >= 0) {
                startIdx = url.indexOf("{:");
                endIdx = url.indexOf("}", startIdx) + 1;
                temp = url.substring(startIdx, endIdx);
                temp2 = temp.substring(2, temp.length() - 1);
                temp2 = result.get(temp2).toString();
                url = url.replace(temp, temp2);
            }
        }
//        if (getDisplayColumnLink().containsKey(value+"_package")) {
//            
//        }
        if (url == null || url.equalsIgnoreCase("null")) {
            url = "";
        }
        if (isMixConfig) {
            url += "&searchCode=" + searchCode;
        }
        return url;
    }

    public List<String> getHiddenFields() {
        return hiddenFields;
    }

    public void setHiddenFields(List<String> hiddenFields) {
        this.hiddenFields = hiddenFields;
    }

    public Map getAdditionalButtonMap() {
        return additionalButtonMap;
    }

    public void setAdditionalButtonMap(Map additionalButtonMap) {
        this.additionalButtonMap = additionalButtonMap;
    }
    
    public List getAdditionalButton() {
        return additionalButton;
    }

    public List getAdditionalButtonLabel() {
        return additionalButtonLabel;
    }

    public List getAdditionalButtonAction() {
        return additionalButtonAction;
    }

    public String getSUCCESS_EL() {
        return SUCCESS_EL;
    }

    public void setSUCCESS_EL(String SUCCESS_EL) {
        this.SUCCESS_EL = SUCCESS_EL;
    }

    public List getAdditionalButtonIcon() {
        return additionalButtonIcon;
    }

    public void setAdditionalButtonIcon(List additionalButtonIcon) {
        this.additionalButtonIcon = additionalButtonIcon;
    }

    public List getAdditionalButtonChecked() {
        return additionalButtonChecked;
    }

    public void setAdditionalButtonChecked(List additionalButtonChecked) {
        this.additionalButtonChecked = additionalButtonChecked;
    }
    
    public Map getExtraConditionMap() {
        return extraConditionMap;
    }

    public void setExtraConditionMap(Map extraConditionMap) {
        this.extraConditionMap = extraConditionMap;
    }

    public List<String> getExtraCondition_toExecute() {
        return extraCondition_toExecute;
    }

    public void setExtraCondition_toExecute(List<String> extraCondition_toExecute) {
        this.extraCondition_toExecute = extraCondition_toExecute;
    }

    public Map getSqlTablesMap_other() {
        return sqlTablesMap_other;
    }

    public void setSqlTablesMap_other(Map sqlTablesMap_other) {
        this.sqlTablesMap_other = sqlTablesMap_other;
    }

    public Boolean getIsMixConfig() {
        return isMixConfig;
    }

    public void setIsMixConfig(Boolean isMixConfig) {
        this.isMixConfig = isMixConfig;
    }

    public Boolean getIsAppendNoLock() {
        return isAppendNoLock;
    }

    public void setIsAppendNoLock(Boolean isAppendNoLock) {
        this.isAppendNoLock = isAppendNoLock;
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

    public Map getMixedConfig_map() {
        return mixedConfig_map;
    }

    public void setMixedConfig_map(Map mixedConfig_map) {
        this.mixedConfig_map = mixedConfig_map;
    }

    public String getSearchCode() {
        return searchCode;
    }

    public void setSearchCode(String searchCode) {
        this.searchCode = searchCode;
    }

    public Boolean getShowNumbering() {
        return showNumbering;
    }

    public void setShowNumbering(Boolean showNumbering) {
        this.showNumbering = showNumbering;
    }

    public String getNoDeco_include() {
        return noDeco_include;
    }

    public String getMoreInfoMarginLeft() {
        return "15%";
    }
    public String getMoreInfoWidth() {
        return moreInfoWidth;
    }
    
    public String getMoreInfoType() {
        return moreInfoType;
    }

    public void setMoreInfoType(String moreInfoType) {
        this.moreInfoType = moreInfoType;
    }

    public String getMoreInfoJsp() {
        return moreInfoJsp;
    }

    public void setMoreInfoJsp(String moreInfoJsp) {
        this.moreInfoJsp = moreInfoJsp;
    }

    public String getMoreInfoModel() {
        return moreInfoModel;
    }

    public void setMoreInfoModel(String moreInfoModel) {
        this.moreInfoModel = moreInfoModel;
    }

    public String getMoreInfoTitle() {
        return moreInfoTitle;
    }

    public void setMoreInfoTitle(String moreInfoTitle) {
        this.moreInfoTitle = moreInfoTitle;
    }

    public Boolean getMoreInfoReloadOnClose() {
        return moreInfoReloadOnClose;
    }

    public void setMoreInfoReloadOnClose(Boolean moreInfoReloadOnClose) {
        this.moreInfoReloadOnClose = moreInfoReloadOnClose;
    }

    public String getCustomHeaderEnd() {
        return customHeaderEnd;
    }

    public String getCustomListEnd() {
        return customListEnd;
    }
    
    public String getJsInclude() {
        return jsInclude;
    }

    // ThoTH @ 21-Jun-2012
    public static String getContentType(String fileUrl) throws Exception {
        URL u = new URL(fileUrl);
        URLConnection uc = u.openConnection();
        String type = uc.getContentType();
        return type;
    }

    public String getDate_default_datetime() {
        return date_default_datetime;
    }

    public void setDate_default_datetime(String date_default_datetime) {
        this.date_default_datetime = date_default_datetime;
    }

    public String getCheckEditLink(String value) {
        if (getEditLinkColumn().contains(value)) {
            return "true";
        }
        return "false";
    }

    public String getResultPrimaryKey(int row) {
        Map resultMap = ((Map) getResult().get(row));
        String pk = resultMap.get(getPrimaryKeyColumn()).toString();
        return pk;
    }

    public String extractSearchFieldForDate(String theDate) {
        if (theDate.endsWith("_fromTo_split")) {
            return theDate.substring(6, (theDate.length() - 13));
        } else if (theDate.endsWith("_fromTo")) {
            return theDate.substring(6, (theDate.length() - 7));
        }
        return theDate.substring(6);
    }

    public void date_criteria(Map param, String tempDateData, String searchFieldDBName) throws Exception {
        Calendar cal = Calendar.getInstance();
        String dateFormat = getText("date_default_date_dr");
        try {
            cal.setTime(DateUtil.getDate(tempDateData, dateFormat));
        } catch (Exception e) {
            try {
                dateFormat = "dd-MM-yyyy";
                cal.setTime(DateUtil.getDate(tempDateData, dateFormat));
            } catch (Exception e2) {
                dateFormat = "dd.MM.yyyy";
                cal.setTime(DateUtil.getDate(tempDateData, dateFormat));
            }
        }
        tempDateData = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd");
        cal.add(Calendar.DAY_OF_MONTH, 1);
        String paramData = Formatter.formatDate(cal.getTime(), "yyyy-MM-dd");

        String start_or_end = null;
        String from_or_to_searchField = null;
        if (getExtraSetupMap().containsKey("StartEndDateField")) {
            for (String startEndDateField : ((String) getExtraSetupMap().get("StartEndDateField")).split(",")) {
                String[] startEnd = startEndDateField.split(";");
                if (startEnd[0].equals(searchFieldDBName)) {
                    start_or_end = "start";
                    break;
                } else if (startEnd[1].equals(searchFieldDBName)) {
                    start_or_end = "end";
                    break;
                }
            }
        }
        if (start_or_end != null) {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            if (start_or_end.equals("start")) {
                if (new CommonFunction().isOracleDB()) {
                    param.put(SystemConstants.CRITERIA.DATE_CRITERIA + searchFieldDBName,
                            ">= to_date('" + Formatter.formatDate(DateUtil.getDate(tempDateData, dateFormat), "yyyy-MM-dd") + "'_comma_ 'YYYY-MM-DD')");
                } else {
                    param.put(SystemConstants.CRITERIA.DATE_CRITERIA + searchFieldDBName,
                            //                            ">= convert(datetime_comma_ '" + Formatter.formatDate(DateUtil.getDate(tempDateData, getText("date_default_date_dr")), "yyyy-MM-dd")+"'_comma_ 126)");
                            //                            ">= convert(datetime_comma_ '" + tempDateData+"'_comma_ 126)");
                            ">= " + tempDateData);
                }
            } else { //with DateTo
                if (new CommonFunction().isOracleDB()) {
                    param.put(SystemConstants.CRITERIA.DATE_CRITERIA + searchFieldDBName,
                            "< to_date('" + paramData + "'_comma_ 'YYYY-MM-DD')");
                } else {
                    param.put(SystemConstants.CRITERIA.DATE_CRITERIA + searchFieldDBName,
                            //                            "< " + "convert(datetime_comma_ '" + paramData+"'_comma_ 126)");
                            "< " + paramData);
                }
            }
            return;
        }

        if (new CommonFunction().isOracleDB()) {
            param.put(SystemConstants.CRITERIA.DATE_CRITERIA + searchFieldDBName,
                    ">= to_date('" + tempDateData + "'_comma_ 'YYYY-MM-DD'), "
                    + "< to_date('" + paramData + "'_comma_ 'YYYY-MM-DD')");
        } else {
            param.put(SystemConstants.CRITERIA.NO_CHANGE + searchFieldDBName,
                    ">= " + tempDateData + ", "
                    + "< " + paramData);
        }
    }

    public void date_fromTo_criteria(Map param, String paramData, String tempDateData, String searchFieldDBName) throws Exception {
        Calendar cal = Calendar.getInstance();
        String dateFormat = getText("date_default_date_dr");
        Boolean found = Boolean.FALSE;
        try {
            cal.setTime(DateUtil.getDate(tempDateData, dateFormat));
            found = Boolean.TRUE;
        } catch (Exception e) {
            try {
                dateFormat = "dd-MM-yyyy";
                cal.setTime(DateUtil.getDate(tempDateData, dateFormat));
                found = Boolean.TRUE;
            } catch (Exception e2) {
                try {
                    dateFormat = "dd.MM.yyyy";
                    cal.setTime(DateUtil.getDate(tempDateData, dateFormat));
                    found = Boolean.TRUE;
                } catch (Exception e3) {
                }
            }
        }
        if (!found) {
            try {
                cal.setTime(DateUtil.getDate(paramData, dateFormat));
                found = Boolean.TRUE;
            } catch (Exception e) {
                try {
                    dateFormat = "dd-MM-yyyy";
                    cal.setTime(DateUtil.getDate(paramData, dateFormat));
                    found = Boolean.TRUE;
                } catch (Exception e2) {
                    try {
                        dateFormat = "dd.MM.yyyy";
                        cal.setTime(DateUtil.getDate(paramData, dateFormat));
                        found = Boolean.TRUE;
                    } catch (Exception e3) {
                        dateFormat = getText("newDateRangePicker_defaultFormat").replaceAll("m", "M");
                    }
                }
            }
        }
        if (!Validator.isEmpty(tempDateData)) {
            //reportParams += "search_" + str + "From=" + paramData + "&" ;
            if (Validator.isEmpty(paramData)) { // no DateTo
                if (new CommonFunction().isOracleDB()) {
                    param.put(SystemConstants.CRITERIA.DATE_CRITERIA + searchFieldDBName,
                            ">= to_date('" + Formatter.formatDate(DateUtil.getDate(tempDateData, dateFormat), "yyyy-MM-dd") + "'_comma_ 'YYYY-MM-DD')");
                } else {
                    param.put(SystemConstants.CRITERIA.DATE_CRITERIA + searchFieldDBName,
                            ">= " + Formatter.formatDate(DateUtil.getDate(tempDateData, dateFormat), "yyyy-MM-dd"));
//                            ">= convert(datetime_comma_ '" + Formatter.formatDate(DateUtil.getDate(tempDateData, getText("date_default_date_dr")), "yyyy-MM-dd")+"'_comma_ 126)");
                }
            } else { //with DateTo
                if (new CommonFunction().isOracleDB()) {
                    param.put(SystemConstants.CRITERIA.DATE_CRITERIA + searchFieldDBName,
                            ">= to_date('" + Formatter.formatDate(DateUtil.getDate(tempDateData, dateFormat), "yyyy-MM-dd") + "'_comma_ 'YYYY-MM-DD'), "
                            + "< to_date('" + paramData + "'_comma_ 'YYYY-MM-DD')");
                } else {
                    Calendar calTo = DateUtil.getCalendar(DateUtil.getDate(paramData, dateFormat));
                    calTo.add(Calendar.DATE, 1);
                    param.put(SystemConstants.CRITERIA.DATE_CRITERIA + searchFieldDBName,
                            //                            ">= " + "convert(datetime_comma_ '" + Formatter.formatDate(DateUtil.getDate(tempDateData, getText("date_default_date_dr")), "yyyy-MM-dd")+"'_comma_ 126)" + ", "
                            ">= " + Formatter.formatDate(DateUtil.getDate(tempDateData, dateFormat), "yyyy-MM-dd") + " , "
                            //                            + "< " + "convert(datetime_comma_ '" + paramData+"'_comma_ 126)");
                            //                                            ">= " + Formatter.formatDate(DateUtil.getDate(tempDateData, getText("date_default_date_dr")), "yyyy-MM-dd") + ", "
                            + "< " + Formatter.formatDate(calTo.getTime(), "yyyy-MM-dd"));
                }
            }
        } else {
            if (!Validator.isEmpty(paramData)) { // only with DateTo
                if (new CommonFunction().isOracleDB()) {
                    param.put(SystemConstants.CRITERIA.DATE_CRITERIA + searchFieldDBName,
                            "< to_date('" + paramData + "'_comma_ 'YYYY-MM-DD')");
                } else {
                    Calendar calTo = DateUtil.getCalendar(DateUtil.getDate(paramData, dateFormat));
                    calTo.add(Calendar.DATE, 1);
                    param.put(SystemConstants.CRITERIA.DATE_CRITERIA + searchFieldDBName,
                            //                            "< " + "convert(datetime_comma_ '" + paramData+"'_comma_ 126)");
                            "< " + Formatter.formatDate(calTo.getTime(), "yyyy-MM-dd") );
                }
            }
        }
    }

    private Boolean acClearSuggestion_ = Boolean.TRUE;

    public Boolean isAcClearSuggestion_() {
        return acClearSuggestion_;
    }

    public void setAcClearSuggestion_(Boolean acClearSuggestion_) {
        this.acClearSuggestion_ = acClearSuggestion_;
    }

    public Boolean useAc_ = Boolean.FALSE;

    public Boolean getUseAc_() {
        return useAc_;
    }

    public List acSetupList = new ArrayList();

    public List getAcSetupList() {
        return acSetupList;
    }

    public class AutoComplete {

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

        public AutoComplete(String dynamicConfig, String inputId, String matchingColumn, String lookFor, String writeTo, String jsFunction, String postEvent) {
            this.dynamicConfig = dynamicConfig;
            this.inputId = inputId;
            this.matchingColumn = matchingColumn;
            this.setLookFor(lookFor);
            this.writeTo = writeTo;
            this.jsFunction = jsFunction;
            this.postEvent = postEvent;
            Map localMap = (getDynamicActionSetup(getAction()));
            LookupAction lookupAction = new LookupAction();
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
                for (Map obj : (List<Map>) lookupAction.getResult()) {
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
                        data.append(from).append(":'").append(obj.get(from.equals("value") ? matchingColumn : from)).append("'").append(innerComma);
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
                        data.append(from).append(":'").append(((Map) obj).get(from.equals("value") ? matchingColumn : from).toString().replaceAll("'", "\\\\'")).append("'").append(innerComma);
                    } else {
                        data.append(from).append(":'").append(getMethodValueFromObject(obj, from.equals("value") ? matchingColumn : from)).append("'").append(innerComma);
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
                    this.uniqueLookfor += "," + look4;
                }
            }
            Debug.printFrameworkDebug("uniqueLookfor = " + uniqueLookfor);
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

    public String getFieldStyleFormat(String fieldName, String type, Object fieldData, Object map) {
        //Added by Zhafari @ 11-Nov-2014
        //Eg. <property name="searchInputStyleFormat_emp_name" value="text-transform: uppercase;"/>
        if (getSearchFieldStyleFormat().containsKey("searchInputStyleFormat_" + fieldName)) {
            if (type.equals("searchField")) {
                return getSearchFieldStyleFormat().get("searchInputStyleFormat_" + fieldName).toString();
            }
        }
        //Added by Zhafari @ 11-Nov-2014 - END
        if (type.equals("detail")) {
            String packageStr = null;
            if (getSearchFieldFormat().containsKey(fieldName + "_style_package")) {
                if (((String) getSearchFieldFormat().get(fieldName + "_style_package")).indexOf(";") > 0) {
//                    String[] strArr = ((String) getSearchFieldFormat().get(fieldName + "_style_package")).split(";");
//                    if (getSearchFieldFormat().containsKey("propertyText_" + fieldName)) {
//                        if (getSearchFieldFormat().get("propertyText_" + fieldName).toString().endsWith(".")) {
//                            packageStr = getSearchFieldFormat().get("propertyText_" + fieldName).toString();
//                        } else {
//                            packageStr = getSearchFieldFormat().get("propertyText_" + fieldName).toString() + ".";
//                        }
//                    }
//                    if (!packageStr.endsWith(".")) {
//                        packageStr += ".";
//                    }
//                    packageStr += ((Map) map).get(strArr[1]);
                    return (String)getSearchFieldFormat().get(fieldName + "_style_package");
                } else {
                    if (getSearchFieldFormat().containsKey("propertyText_" + fieldName)) {
                        if (getSearchFieldFormat().get("propertyText_" + fieldName).toString().endsWith(".")) {
                            packageStr = getSearchFieldFormat().get("propertyText_" + fieldName).toString() + fieldData;
                        } else {
                            packageStr = getSearchFieldFormat().get("propertyText_" + fieldName).toString() + "." + fieldData;
                        }
                    }
                }
                if (packageStr != null) {
                    packageStr += ".style";
                    String getTextData = null;
                    try {
                        getTextData = getText(packageStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!getTextData.equals(packageStr)) {
                        return "text-align: left; " + getTextData;
                    }
                }
            }
        } else if (getSearchFieldStyleFormat().containsKey("styleFormat_" + fieldName)) {
            if (type.equals("header")) {
                return getSearchFieldStyleFormat().get("styleFormat_" + fieldName).toString();
            }
            String packageStr = null;
            if (getSearchFieldFormat().get("propertyText_" + fieldName).toString().endsWith(".")) {
                packageStr = getSearchFieldFormat().get("propertyText_" + fieldName).toString() + fieldData;
            } else {
                packageStr = getSearchFieldFormat().get("propertyText_" + fieldName).toString() + "." + fieldData;
            }
            String getTextData = getText(packageStr);
            if (!getTextData.equals(packageStr)) {
                if (getSearchFieldStyleFormat().get("styleFormat_" + fieldName).toString().endsWith(";")) {
                    return getSearchFieldStyleFormat().get("styleFormat_" + fieldName).toString() + getTextData;
                } else {
                    return getSearchFieldStyleFormat().get("styleFormat_" + fieldName).toString() + "; " + getTextData;
                }
            }
            return getSearchFieldStyleFormat().get("styleFormat_" + fieldName).toString();
        } else if (type.equals("header")) {
            if (((String) getSearchFieldFormat().get(fieldName + "_style_package")).indexOf(";") > 0) {
                return (String)getSearchFieldFormat().get(fieldName + "_style_package");
            }
            String packageStr = null;
            if (getSearchFieldFormat().get("propertyText_" + fieldName).toString().endsWith(".")) {
                packageStr = getSearchFieldFormat().get("propertyText_" + fieldName).toString() + fieldData;
            } else {
                packageStr = getSearchFieldFormat().get("propertyText_" + fieldName).toString() + "." + fieldData;
            }
            packageStr += ".style";
//            Debug.printFrameworkDebug("packageStr = " + packageStr);
            String getTextData = null;
            try {
                getTextData = getText(packageStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            Debug.printFrameworkDebug("getTextData = " + getTextData);
            if (!getTextData.equals(packageStr)) {
                return "text-align: left; " + getTextData;
            }
        }
        return "text-align: left;";
    }

    public String replaceSpecialChar(String data) {
        String tmp = data.replace("%", "%%%%%");
        tmp = tmp.replace("&", "%26");
        tmp = tmp.replace("=", "%3D");
        tmp = tmp.replace("%%%%%", "%25");
        return tmp;
    }

    public String replaceSpecialChar_rev(String data) {
        String tmp = data.replace("%26", "&");
        tmp = tmp.replace("%3D", "=");
        tmp = tmp.replace("%25", "%");
        return tmp;
    }

    public String getServHistPreviewText_() {
        return servHistPreviewText_;
    }

    public void setServHistPreviewText_(String servHistPreviewText_) {
        this.servHistPreviewText_ = servHistPreviewText_;
    }

    public String getServHistPreviewTitle_() {
        return servHistPreviewTitle_;
    }

    public void setServHistPreviewTitle_(String servHistPreviewTitle_) {
        this.servHistPreviewTitle_ = servHistPreviewTitle_;
    }

    public List getServHistPreviewList_() {
        return servHistPreviewList_;
    }

    public void setServHistPreviewList_(List servHistPreviewList_) {
        this.servHistPreviewList_ = servHistPreviewList_;
    }

    public Boolean isRequiredField(String pageField) {
        Boolean isDateFromTo = Boolean.FALSE;
        if (pageField.startsWith("_date_")) {
            pageField = pageField.substring(6);
            if (pageField.endsWith("_fromTo_split")) {
                isDateFromTo = Boolean.TRUE;
                pageField = pageField.substring(0, pageField.length() - 13);
            } else if (pageField.endsWith("_fromTo")) {
                isDateFromTo = Boolean.TRUE;
                pageField = pageField.substring(0, pageField.length() - 7);
            }
        }
//        Debug.printFrameworkDebug("isRequiredField called... pageField = " + pageField);
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

    //FTP Setting     
    public void closeFTP() {
        try {
            if (System.getProperty("os.name").startsWith("Windows")) {
//                new FtpsUtil().disconnect(); //no more using ftpsutil
                new SFTPBean().disconnect();
            } else {
//                new Sftp2Util().disconnectSftp();
                new SFTPBean().disconnect();
            }
        } catch (Exception e) {
        }
    }

    public FtpInterface ftps = getFtps();

    public FtpInterface getFtps() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            ftps = new FtpsUtil();            
        } else {
            ftps = new SFTPBean();
        }
        return ftps;
    }

    public void setFtps(FtpInterface ftps) {
        this.ftps = ftps;
    }

    //Common SPA functions - ahmadni @ 5-Oct-2017
    protected String strutsAction = "";

    public String getStrutsAction() {
        return strutsAction;
    }

    protected void setStrutsAction(String strutsAction) { //Don't allow to be set from <form> or get method
        this.strutsAction = strutsAction;
    }

    public String eotSeq = "";
    public String eotYear = "";
    public String trnDiv_desc = "";
    public String trnDiv = "";
    public String trnType = "";
//    private String trnTypePopTitle = "";
    public String trnDist = "";
    public String trnBs = "";
    public String trnLot = "";
    public String trnLotTo = "";
    public String searchType = "Land Title";
    public String storey = "";
    public String parcel = "";
    public String trn = "";
    public String titVer = "";
    public String trnUpi = "";
    public String instType = "";
    public String instSeq = "";
    public String instYear = "";

    public String constructSQLStml(String sqlType) {
//        Debug.printFrameworkDebug("searchType = " + searchType);
        Formatter format = new Formatter();
        String sqlQuery = "";
        trnDiv = format.appendZero(trnDiv, 2, false);
        param_.clear();
        if (sqlType.equals("C")) {
            if (searchType.equals("Instrument")) {
                sqlQuery = "SELECT COUNT(DISTINCT(TI.ROWID)) AS recordCount ";

            } else {
                sqlQuery = "SELECT COUNT(DISTINCT(TT.ROWID)) AS recordCount ";
            }
        } else {
            if (searchType.equals("Land Title")) {
                sqlQuery = "SELECT TT.ROWID AS strRowId,TT.TRN_DIV AS TrnDiv,TT.TRN_TYPE AS TrnType, "
                        + "TT.TRN_DIST AS TrnDist, TT.TRN_BS AS TrnBs,TT.TRN_LOT AS TrnLot, "
                        + "TT.TRN_UPI AS TrnUpi, "
                        + "TT.TIT_VER AS TitVer, TT.STOREY AS Storey, TT.PARCEL AS Parcel, TT.CUR_STATUS as cur_status, "
                        + "TT.LOCALITY AS LOCALITY , TT.TRN_UPI AS Upi, TT.TIT_VER AS TitVer,"
                        + "(TT.TRN_DIV||SUBSTR(TT.TRN_TYPE||'   ',1,5)||TT.TRN_DIST||TT.TRN_BS||TT.TRN_LOT) AS TRN, "
                        /*                        + //                        "(0) as CcInst ";
                        "(Select count(*) from bywhom" + trnDiv + ", trsinst" + trnDiv + " "
                        + "where BYWHOM" + trnDiv + ".TRN = TT.TRN_DIV||SUBSTR(TT.TRN_TYPE||'   ',1,5)||TT.TRN_DIST||TT.TRN_BS||TT.TRN_LOT "
                        + //"AND BYWHOM"+ trnDiv+".TIT_VER = TT.TIT_VER AND BYWHOM"+ trnDiv+".CUR_STATUS = 'Y' "+
                        "AND BYWHOM" + trnDiv + ".TIT_VER = TT.TIT_VER "
                        + "AND BYWHOM" + trnDiv + ".INST_TYPE = TRSINST" + trnDiv + ".INST_TYPE "
                        + "AND BYWHOM" + trnDiv + ".INST_YEAR = TRSINST" + trnDiv + ".INST_YEAR "
                        + "AND BYWHOM" + trnDiv + ".INST_SEQ = TRSINST" + trnDiv + ".INST_SEQ "
                        + "AND TRSINST" + trnDiv + ".NATURE in ('CA','WC','CH','VC','DC','TC','KC','TR','AT','TL','SL','VS','SS','TS','KS','PA','PB','PC','RP','CO','CR','RW','EE','VO','CN','CP') "
                        + ") as CcInst, "*/
                        + "TO_CHAR(TO_NUMBER(TT.TRN_BS)) as BS_NUMBER, "
                        + "TO_CHAR(TO_NUMBER(TT.TRN_LOT)) as LOT_NUMBER,"
                        + "(select initcap(code_desc) from PUBCODE" + trnDiv + " where (code_type = 'PLT' or code_type = 'LCT')and code_acr = TT.TRN_TYPE) as cc1, "
                        + "(select initcap(code_desc) from PUBCODE" + trnDiv + " where code_type = 'DIS' and code_1 = TT.TRN_DIV and code_2 = TT.TRN_DIST) as cc2, "
                        + "(select count(*) from PUBCODE" + trnDiv + " where code_type = 'DIS' and code_1 = TT.TRN_DIV and code_2 = TT.TRN_DIST and code_3 = 'S') as cc3 ";
            } else if (searchType.equals("Strata Title")) {
                sqlQuery = "SELECT TT.ROWID AS strrowid, TT.TRN_DIV AS TrnDiv,TT.TRN_TYPE AS TrnType, "
                        + "TT.TRN_DIST AS TrnDist, TT.TRN_BS AS TrnBs,TT.TRN_LOT AS TrnLot, "
                        + "(SELECT DISTINCT TS.TRN_UPI FROM TRSTITLE" + trnDiv + " TS WHERE TS.TRN_DIV = TT.TRN_DIV AND TS.TRN_TYPE = TT.TRN_TYPE AND TS.TRN_DIST = TT.TRN_DIST "
                        + "AND TS.TRN_BS = TT.TRN_BS AND TS.TRN_LOT = TT.TRN_LOT AND TS.CUR_STATUS = 'Y' AND TS.STOREY is NULL AND TS.PARCEL is NULL ) AS TrnUpi, "
                        + "TT.TIT_VER AS TitVer, TT.STOREY AS Storey, TT.PARCEL AS Parcel, "
                        + "TT.LOCALITY AS LOCALITY , TT.TRN_UPI AS Upi, TT.TIT_VER AS TitVer,"
                        + "(TT.TRN_DIV||SUBSTR(TT.TRN_TYPE||'   ',1,5)||TT.TRN_DIST||TT.TRN_BS||TT.TRN_LOT) AS TRN, "
                        + "(Select count(*) from bywhom" + trnDiv + ", trsinst" + trnDiv + " "
                        + "where BYWHOM" + trnDiv + ".TRN = TT.TRN_DIV||SUBSTR(TT.TRN_TYPE||'   ',1,5)||TT.TRN_DIST||TT.TRN_BS||TT.TRN_LOT "
                        /*                        + //"AND BYWHOM"+ trnDiv+".TIT_VER = TT.TIT_VER AND BYWHOM"+ trnDiv+".STOREY = TT.STOREY AND  BYWHOM"+ trnDiv+".PARCEL = TT.PARCEL  AND BYWHOM"+ trnDiv+".CUR_STATUS = 'Y' "+
                        "AND BYWHOM" + trnDiv + ".TIT_VER = TT.TIT_VER AND BYWHOM" + trnDiv + ".STOREY = TT.STOREY AND  BYWHOM" + trnDiv + ".PARCEL = TT.PARCEL "
                        + "AND BYWHOM" + trnDiv + ".INST_TYPE = TRSINST" + trnDiv + ".INST_TYPE "
                        + "AND BYWHOM" + trnDiv + ".INST_YEAR = TRSINST" + trnDiv + ".INST_YEAR "
                        + "AND BYWHOM" + trnDiv + ".INST_SEQ = TRSINST" + trnDiv + ".INST_SEQ "
                        + "AND TRSINST" + trnDiv + ".NATURE in ('CA','WC','CH','VC','DC','TC','KC','TR','AT','TL','SL','VS','SS','TS','KS','PA','PB','PC','RP','CO','CR','RW','EE','VO','CN','CP') "
                        + ") as CcInst, "*/
                        + "TO_CHAR(TO_NUMBER(TT.TRN_BS)) as BS_NUMBER, "
                        + "TO_CHAR(TO_NUMBER(TT.TRN_LOT)) as LOT_NUMBER,"
                        + "TO_CHAR(TO_NUMBER(TT.STOREY)) as STOREY_NUMBER, "
                        + "TO_CHAR(TO_NUMBER(TT.PARCEL)) as PARCEL_NUMBER,"
                        + "(select initcap(code_desc) from PUBCODE" + trnDiv + " where (code_type = 'PLT' or code_type = 'LCT')and code_acr = TT.TRN_TYPE) as cc1, "
                        + "(select initcap(code_desc) from PUBCODE" + trnDiv + " where code_type = 'DIS' and code_1 = TT.TRN_DIV and code_2 = TT.TRN_DIST) as cc2, "
                        + "(select count(*) from PUBCODE" + trnDiv + " where code_type = 'DIS' and code_1 = TT.TRN_DIV and code_2 = TT.TRN_DIST and code_3 = 'S') as cc3 ";

            } else if (searchType.equals("Instrument")) {
                //eLodgement
                sqlQuery = "SELECT DISTINCT TI.ROWID AS strrowid, TI.INST_TYPE AS InstType, TI.INST_SEQ AS InstSeq, "
                        + "TI.INST_YEAR AS InstYear, TI.REG_DATE AS RegDate, TI.ENDORSE AS Endorse, "
                        + //                        "B.TIT_VER AS TitVer, B.STOREY AS Storey, B.PARCEL AS Parcel, B.TRN AS TRN, "+
                        "TO_CHAR(TO_NUMBER(TI.INST_SEQ)) AS displaySeq,TI.NATURE AS NATURE, P.CODE_DESC AS NATUREDESC ";

            }

        }
        if (searchType.equals("Land Title")) {
            sqlQuery = sqlQuery + " FROM TRSTITLE" + trnDiv + " TT "
                    + "WHERE " //( (concat(concat(TT.TIT_VER,TT.CUR_STATUS),to_char(TT.reg_date, 'YYYYMMDD'))) = (SELECT concat(max(concat(T1.tit_ver,T1.cur_status)),TO_CHAR(max(T1.reg_date), 'YYYYMMDD')) FROM TRSTITLE"+trnDiv+" T1 WHERE T1.TRN_UPI = TT.TRN_UPI ) ) AND "
                    + "TT.STOREY is NULL AND TT.PARCEL is NULL";
//                    + ") AND TT.CUR_STATUS = 'Y' AND TT.STOREY is NULL AND TT.PARCEL is NULL";
        } else if (searchType.equals("Strata Title")) {
            sqlQuery = sqlQuery + " FROM TRSTITLE" + trnDiv + " TT "
                    + "WHERE (TT.TIT_VER = "
                    + "(SELECT MAX(T1.TIT_VER) FROM TRSTITLE" + trnDiv + " T1 WHERE "
                    + "( T1.TRN_DIV = TT.TRN_DIV) AND "
                    + "( T1.TRN_TYPE = TT.TRN_TYPE ) AND "
                    + "( T1.TRN_DIST =  TT.TRN_DIST ) AND "
                    + "( T1.TRN_BS =  TT.TRN_BS) AND  "
                    + "( T1.TRN_LOT =  TT.TRN_LOT) "
                    + ") "
                    + ") AND NOT TT.STOREY is NULL AND NOT TT.PARCEL is NULL";
//                    + ") AND TT.CUR_STATUS = 'Y' AND NOT TT.STOREY is NULL AND NOT TT.PARCEL is NULL";
        } else if (searchType.equals("Instrument")) {
            //eLodgement
            sqlQuery = sqlQuery + " FROM TRSINST" + trnDiv + " TI, BYWHOM" + trnDiv + " B , PUBCODE" + trnDiv + " P "
                    + "WHERE ( B.INST_YEAR = TI.INST_YEAR ) AND "
                    + "( B.INST_SEQ = TI.INST_SEQ ) AND "
                    + "( B.INST_TYPE = TI.INST_TYPE) AND "
                    + //"( TI.CUR_STATUS = 'Y') AND"+
                    "( NOT TI.INST_TYPE IN ('K','A') ) AND"
                    + "( TI.NATURE in ('CA','WC','CH','VC','DC','TC','KC','TR','AT','TL','SL','VS','SS','TS','KS','PA','PB','PC','RP','CO','CR','RW','EE','VO','CN','CP') ) AND "
                    + "( TI.NATURE = P.CODE_1) AND "
                    + "( P.CODE_TYPE = 'INS') ";

        }

        if (searchType.equals("Land Title") || searchType.equals("Strata Title")) {
            if (!trnDiv.equals("")) {
                //strDiv is a mandatory field
                //09/03/2012 by Wongkk
                //sqlQuery = sqlQuery + " AND TT.TRN_DIV = '" + trnDiv+"'";
                sqlQuery = sqlQuery + " AND TT.TRN_DIV = ? ";
                param_.add(trnDiv);
            }
            if (!trnType.equals("")) {
                //09/03/2012 by Wongkk
                //sqlQuery = sqlQuery + " AND TT.TRN_TYPE = '" + trnType+"'";
                sqlQuery = sqlQuery + " AND TT.TRN_TYPE = ? ";
                param_.add(trnType.toUpperCase());

            }

            if (!trnDist.equals("")) {
                trnDist = format.appendZero(trnDist, 3, false);
                //09/03/2012 by Wongkk
//                    sqlQuery = sqlQuery + " AND TT.TRN_DIST = '" + trnDist+"'";
                sqlQuery = sqlQuery + " AND TT.TRN_DIST = ? ";
                param_.add(trnDist);
            }
            if (!trnBs.equals("")) {
                trnBs = format.appendZero(trnBs, 3, false);
                //09/03/2012 by Wongkk
//                    sqlQuery = sqlQuery + " AND TT.TRN_BS = '" + trnBs+"'";
                sqlQuery = sqlQuery + " AND TT.TRN_BS = ? ";
                param_.add(trnBs);
            }
            if (!trnLot.equals("") && trnLotTo.equals("")) {
                trnLot = format.appendZero(trnLot, 5, false);
                //09/03/2012 by Wongkk
//                    sqlQuery = sqlQuery + " AND TT.TRN_LOT = '" + trnLot+"'";
                sqlQuery = sqlQuery + " AND TT.TRN_LOT = ? ";
                param_.add(trnLot);
            } else if (trnLot.equals("") && !trnLotTo.equals("")) {
                trnLotTo = format.appendZero(trnLotTo, 5, false);
                //09/03/2012 by Wongkk
//                    sqlQuery = sqlQuery + " AND TT.TRN_LOT = '" + trnLotTo+"'";
                sqlQuery = sqlQuery + " AND TT.TRN_LOT = ? ";
                param_.add(trnLotTo);
            } else if (!trnLot.equals("") && !trnLotTo.equals("")) {
                trnLot = format.appendZero(trnLot, 5, false);
                //09/03/2012 by Wongkk
                trnLotTo = format.appendZero(trnLotTo, 5, false);
//                    sqlQuery = sqlQuery + " AND (TT.TRN_LOT between '" + trnLot+"' AND '"+trnLotTo+"') ";
                sqlQuery = sqlQuery + " AND (TT.TRN_LOT between ? AND ? ) ";
                param_.add(trnLot);
                param_.add(trnLotTo);
            }

            if (!storey.equals("")) {
                storey = format.appendZero(storey, 2, false);
                //09/03/2012 by Wongkk
//                    sqlQuery = sqlQuery + " AND TT.STOREY = '" + storey+"'";
                sqlQuery = sqlQuery + " AND TT.STOREY = ? ";
                param_.add(storey);
            }

            if (!parcel.equals("")) {
                parcel = format.appendZero(parcel, 2, false);
                //09/03/2012 by Wongkk
//                    sqlQuery = sqlQuery + " AND TT.PARCEL = '" + parcel+"'";
                sqlQuery = sqlQuery + " AND TT.PARCEL = ? ";
                param_.add(parcel);
            }
            //no upi for strata Title
            if (searchType.equals("Land Title")) {
                if (!trnUpi.equals("")) {
                    //09/03/2012 by Wongkk
                    sqlQuery = sqlQuery + " AND TT.TRN_UPI in (" + trnUpi + ") ";
                    //cannot use parameter coz upi has been formatted into 'upi','upi'
                    //sqlQuery = sqlQuery + " AND TT.TRN_UPI in ( ? ) ";
                    //param_.add(trnUpi);
                }
            }
            //ADDED 25/11/2010
            if (searchType.equals("Strata Title")) {
                if (!trnUpi.equals("")) {
                    sqlQuery = sqlQuery + " AND TT.TRN_DIV||substr(TT.TRN_TYPE||'  ',0,5)||TT.TRN_DIST||TT.TRN_BS||TT.TRN_LOT in ";
                    //09/03/2012 by Wongkk
                    sqlQuery = sqlQuery + " (SELECT DISTINCT TRN_DIV||substr(TRN_TYPE||'  ',0,5)||TRN_DIST||TRN_BS||TRN_LOT FROM TRSTITLE" + trnDiv + " WHERE TRN_UPI IN (" + trnUpi + ") )";
//                    sqlQuery = sqlQuery + " (SELECT DISTINCT TRN_DIV||substr(TRN_TYPE||'  ',0,5)||TRN_DIST||TRN_BS||TRN_LOT FROM TRSTITLE"+ trnDiv+" WHERE TRN_UPI IN (?) )";
//                    param_.add(trnUpi);
                }
            }
            sqlQuery = sqlQuery + " ORDER BY TT.TRN_DIV, TT.TRN_DIST, TT.TRN_BS, TT.TRN_LOT,TT.STOREY,TT.PARCEL ";
        } else if (searchType.equals("Instrument")) {
            if (!instType.equals("")) {
                //09/03/2012 by Wongkk
//                    sqlQuery = sqlQuery + " AND B.INST_TYPE = '" + instType+"'";
                sqlQuery = sqlQuery + " AND B.INST_TYPE = ? ";
                param_.add(instType);
            }
            if (!instYear.equals("")) {
                //09/03/2012 by Wongkk
//                    sqlQuery = sqlQuery + " AND B.INST_YEAR = '" + instYear+"'";
                sqlQuery = sqlQuery + " AND B.INST_YEAR = ? ";
                param_.add(instYear);
            }
            if (!instSeq.equals("")) {
                //09/03/2012 by Wongkk
                instSeq = format.appendZero(instSeq, 6, false);
//                    sqlQuery = sqlQuery + " AND B.INST_SEQ = '" + instSeq+"'";
                sqlQuery = sqlQuery + " AND B.INST_SEQ =  ? ";
                param_.add(instSeq);
            }
            if (!titVer.equals("")) {
                //09/03/2012 by Wongkk
                titVer = format.appendZero(titVer, 2, false);
//                    sqlQuery = sqlQuery + " AND B.TIT_VER = '" + titVer+"'";
                sqlQuery = sqlQuery + " AND B.TIT_VER =  ? ";
                param_.add(titVer);
            }
            if (!storey.equals("")) {
                storey = format.appendZero(storey, 2, false);
                //09/03/2012 by Wongkk
//                    sqlQuery = sqlQuery + " AND ( nvl(B.STOREY,' ') = nvl('" + storey+"',' ') )";
                sqlQuery = sqlQuery + " AND ( nvl(B.STOREY,' ') = nvl( ? ,' ') )";
                param_.add(storey);
            }

            if (!parcel.equals("")) {
                parcel = format.appendZero(parcel, 2, false);
                //09/03/2012 by Wongkk
                //sqlQuery = sqlQuery + " AND ( nvl(B.PARCEL,' ') = nvl('" + parcel+"',' ') )";
                sqlQuery = sqlQuery + " AND ( nvl(B.PARCEL,' ') = nvl( ? ,' ') )";
                param_.add(parcel);
            }

            if (!trn.equals("")) {
                //09/03/2012 by Wongkk
//                    sqlQuery = sqlQuery + " AND B.TRN = '" + trn+"'";
                sqlQuery = sqlQuery + " AND B.TRN = ? ";
                param_.add(trn);
            }
            //2/12/2010 - PB no TRN in BYWHOM
//                else{
//                    if(!trnDiv.equals("")){
//                        trnDiv = format.appendZero(trnDiv, 2, false);
//                        sqlQuery = sqlQuery + " AND SUBSTR(B.TRN,1,2) = '" + trnDiv+"'";
//                    }
//                }
            sqlQuery = sqlQuery + " ORDER BY TI.INST_TYPE, TI.INST_YEAR, TI.INST_SEQ, TI.REG_DATE ";
        }
        //new LogFunction().logDebug(this.getClass(),"Search Type "+searchType+" SQL: " + sqlQuery, null);
        //Debug.printFrameworkDebug("(BAS)eLodgement " + sqlQuery);
        return sqlQuery;
    }

    public String getEotSeq() {
        return eotSeq;
    }

    public void setEotSeq(String eotSeq) {
        this.eotSeq = eotSeq;
    }

    public String getEotYear() {
        return eotYear;
    }

    public void setEotYear(String eotYear) {
        this.eotYear = eotYear;
    }

    public String getTrnDiv_desc() {
        return trnDiv_desc;
    }

    public void setTrnDiv_desc(String trnDiv_desc) {
        this.trnDiv_desc = trnDiv_desc;
    }

    public String getTrnDiv() {
        return trnDiv;
    }

    public void setTrnDiv(String trnDiv) {
        this.trnDiv = trnDiv;
    }

    public String getTrnType() {
        return trnType;
    }

    public void setTrnType(String trnType) {
        this.trnType = trnType;
    }

    public String getTrnDist() {
        return trnDist;
    }

    public void setTrnDist(String trnDist) {
        this.trnDist = trnDist;
    }

    public String getTrnBs() {
        return trnBs;
    }

    public void setTrnBs(String trnBs) {
        this.trnBs = trnBs;
    }

    public String getTrnLot() {
        return trnLot;
    }

    public void setTrnLot(String trnLot) {
        this.trnLot = trnLot;
    }

    public String getTrnLotTo() {
        return trnLotTo;
    }

    public void setTrnLotTo(String trnLotTo) {
        this.trnLotTo = trnLotTo;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getStorey() {
        return storey;
    }

    public void setStorey(String storey) {
        this.storey = storey;
    }

    public String getParcel() {
        return parcel;
    }

    public void setParcel(String parcel) {
        this.parcel = parcel;
    }

    public String getTrn() {
        return trn;
    }

    public void setTrn(String trn) {
        this.trn = trn;
    }

    public String getTitVer() {
        return titVer;
    }

    public void setTitVer(String titVer) {
        this.titVer = titVer;
    }

    public String getTrnUpi() {
        return trnUpi;
    }

    public void setTrnUpi(String trnUpi) {
        this.trnUpi = trnUpi;
    }

    public String getInstType() {
        return instType;
    }

    public void setInstType(String instType) {
        this.instType = instType;
    }

    public String getInstSeq() {
        return instSeq;
    }

    public void setInstSeq(String instSeq) {
        this.instSeq = instSeq;
    }

    public String getInstYear() {
        return instYear;
    }

    public void setInstYear(String instYear) {
        this.instYear = instYear;
    }

    protected List param_ = new ArrayList();

    public List getParam_() {
        return param_;
    }

    public void setParam_(List param_) {
        this.param_ = param_;
    }

    public void setParameter(List param, Query query) throws Exception {
        int i = 0;
        String paramType = "";
        for (Object obj : param) {
            if (obj == null) {
                query.setParameter(i++, obj);  // Not sure this Set Null to parameter working or not... testing
            } else {
                paramType = obj.getClass().getSimpleName();
                if (paramType.equals("String")) {
                    query.setString(i++, (String) obj);
                } else if (paramType.equals("Double")) {
                    query.setDouble(i++, (Double) obj);
                } else if (paramType.equals("Long")) {
                    query.setLong(i++, (Long) obj);
                } else if (paramType.equals("Integer")) {
                    query.setInteger(i++, (Integer) obj);
                } else if (paramType.equals("Timestamp")) {
                    query.setTimestamp(i++, (java.sql.Timestamp) obj);
                }
            }
        }
    }

    public List searchResultList = new ArrayList();

    public List getSearchResultList() {
        return searchResultList;
    }

    public void setSearchResultList(List searchResultList) {
        this.searchResultList = searchResultList;
    }

    public void setParameterStmt(List param, PreparedStatement stmt) throws Exception {
        int i = 1;
        String paramType = "";
        for (Object obj : param) {
            if (obj == null) {
                stmt.setNull(i++, 12);  // Not sure this Set Null to parameter working or not... testing
            } else {
                paramType = obj.getClass().getSimpleName();
                if (paramType.equals("String")) {
                    stmt.setString(i++, (String) obj);
                } else if (paramType.equals("Double")) {
                    stmt.setDouble(i++, (Double) obj);
                } else if (paramType.equals("Long")) {
                    stmt.setLong(i++, (Long) obj);
                } else if (paramType.equals("Integer")) {
                    stmt.setInt(i++, (Integer) obj);
                } else if (paramType.equals("Timestamp")) {
                    stmt.setTimestamp(i++, (java.sql.Timestamp) obj);
                }
            }
        }
    }
    public long intRowCount = 0;
    protected ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public void searchUpiList(List resultList) {
        trnUpi = "";
        try {
            for (Map resultMap : (List<Map>) resultList) {
                if (trnUpi.equals("")) {
                    trnUpi = resultMap.get("trnupi").toString();
                } else {
                    trnUpi = trnUpi + "," + resultMap.get("trnupi").toString();
                }
            }
        } catch (Exception e) {
        }
        //Debug.printFrameworkDebug("Result UPI = " + trnUpi);
        //return strUpi;
    }

    public boolean checkSessionTimeOut() {
        Map sessionMap = ActionContext.getContext().getSession();
        if ((sessionMap.get("logined") == null) || !sessionMap.get("logined").equals("true")) {
            addActionError(getText("errors.sesionExpired"));
            sessionMap.put("expired", true);
            return true;
        }
        return false;
    }
    
    private String list_param = null;
    public String getList_param() {
        return list_param;
    }
    public void setList_param(String list_param) {
        this.list_param = list_param;
    }
    
    protected Boolean isGenerateDynamicRpt = Boolean.FALSE;
    public Boolean getIsGenerateDynamicRpt() {
        return isGenerateDynamicRpt;
    }
    
    private Map listFromCommonListMap = null;
    protected List getListFromCommonList(String name, String appendWhat) {
        List list = null;
        if (listFromCommonListMap == null) {
            listFromCommonListMap = new HashMap();
        }
        if (listFromCommonListMap.containsKey(name+(Validator.isEmpty(appendWhat)?"NE":""))) {
            list = (List)listFromCommonListMap.get(name+(Validator.isEmpty(appendWhat)?"NE":""));
        } else {
            try {
                Method m = CommonList.class.getMethod(name);
                list = (List) m.invoke(getCommList());
                if (!Validator.isEmpty(appendWhat)) {
                    CommonList.addBlankOption(list, "", getText(appendWhat));
                }
                listFromCommonListMap.put(name+(Validator.isEmpty(appendWhat)?"NE":""), list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    
    //** Datatable support :: Start **/
    private Integer dt_pageNo, dt_pageSize, dt_order_by;
    private String dt_order;

    public Integer getDt_pageNo() {
        if (dt_pageNo == null) dt_pageNo = 1;
        return dt_pageNo;
    }

    public void setDt_pageNo(Integer dt_pageNo) {
        setPageNo(dt_pageNo);
        this.dt_pageNo = dt_pageNo;
    }

    public Integer getDt_pageSize() {
        if (dt_pageSize == null) dt_pageSize=10;
        return dt_pageSize;
    }

    public void setDt_pageSize(Integer dt_pageSize) {
        setPageSize(dt_pageSize);
        this.dt_pageSize = dt_pageSize;
    }

    public Integer getDt_order_by() {
        if (dt_order_by==null) dt_order_by = 0;
        return dt_order_by;
    }

    public void setDt_order_by(Integer dt_order_by) {
        this.dt_order_by = dt_order_by;
    }

    public String getDt_order() {
        if (dt_order==null) dt_order = "asc";
        return dt_order;
    }

    public void setDt_order(String dt_order) {
//        if (dt_order!=null && dt_order.equals("asc")) {
//            dt_order = "desc";
//        } else {
//            dt_order = "asc";
//        }
        this.dt_order = dt_order;
    }
    
    protected String dt_defaultOrder = null;
    public String getDt_defaultOrder() {
        return dt_defaultOrder;
    }
    protected String[] dt_columnsArr = null;
    protected String[] dt_columnsIdxArr = null;
    protected String dt_columns;
    public String getDt_columns() {
        if (dt_columnsArr != null) {
            dt_columns = "'columns': [\n";
            for (String columns : dt_columnsArr) {
                if (columns.contains(",")) {
                    dt_columns += "{ "+columns+" },\n";
                } else {
                    dt_columns += "{ 'data': '"+ columns +"' },\n";
                }
            }
            dt_columns += "],";
        }
        return dt_columns;
    }
    public void populateDtServerSideInfo(Map dataMap, Integer defaultLength, Integer replaceSorting_0) {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        if (request.getParameter("start") == null) {
            dataMap.put("start", 0);
        } else {
            dataMap.put("start", Integer.parseInt(request.getParameter("start")));
        }
        
        if (request.getParameter("length") == null) {
            dataMap.put("length", defaultLength);
        } else {
            dataMap.put("length", Integer.parseInt(request.getParameter("length")));
        }
        setDt_pageSize((Integer)dataMap.get("length"));
        
        if (request.getParameter("order[0][column]") == null) {
            dataMap.put("orderIdx", replaceSorting_0);
        } else {
            if (replaceSorting_0!=null && Integer.parseInt(request.getParameter("order[0][column]"))==0) {
                dataMap.put("orderIdx", replaceSorting_0);
            } else {
                dataMap.put("orderIdx", Integer.parseInt(request.getParameter("order[0][column]")));
            }
        }
        dt_order_by = (Integer) dataMap.get("orderIdx");
        Debug.printFrameworkDebug("dt_order_by = " + dt_order_by);
        if (request.getParameter("order[0][dir]") == null) {
            dataMap.put("orderDir", "asc");
        } else {
            dataMap.put("orderDir", request.getParameter("order[0][dir]"));
        }
        dt_order = (String) dataMap.get("orderDir");
        dt_defaultOrder = "order: [["+dataMap.get("orderIdx")+", '"+dataMap.get("orderDir")+"']],";
    }
    //** Datatable support :: End **/

    //** to prevent EXPRESSION LANGUAGE INJECTgetION :: Start **//
    @Override
    public String getText(String key, String[] args) {
        if(Validator.isEmpty(key)) return "";
        return super.getText(key.replaceAll("[\\$\\{\\}]", ""), args); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getText(String aTextName, List<?> args) {
        if(Validator.isEmpty(aTextName)) return "";
        return super.getText(aTextName.replaceAll("[\\$\\{\\}]", ""), args); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getText(String aTextName) {
        if(Validator.isEmpty(aTextName)) return "";
        return super.getText(aTextName.replaceAll("[\\$\\{\\}]", "")); //To change body of generated methods, choose Tools | Templates.
    }
    //** to prevent EXPRESSION LANGUAGE INJECTION :: End **//

    @Override
    public void closeSession() {
        super.closeSession();
        if (commList != null && commList.session != null) {
            baseDAO.setSession(commList.session);
            baseDAO.closeSession();
        }
    }
    
    private String defaultReloginDiv = "lookupModal";
    public String getDefaultReloginDiv() {
        Map sessionMap = ActionContext.getContext().getSession();
        if (sessionMap.containsKey("notFromLookup")) {
            defaultReloginDiv = "moreInfoDiv";
        }
        return defaultReloginDiv;
    }
    
    private Map moreInfoMap = null;
    public Map getMoreInfoMap() {
        return moreInfoMap;
    }
    public void setMoreInfoMap(Map moreInfoMap) {
        this.moreInfoMap = moreInfoMap;
    }

    public String moreInfo() {
        Debug.printFrameworkDebug("getAction() = " + getAction());
        Map map = getDynamicActionSetup(getAction());
        moreInfoModel = (String)map.get("moreInfoModel");
        moreInfoJsp = (String)map.get("moreInfoJsp");
        moreInfoTitle = (String)map.get("moreInfoTitle");
        Debug.printFrameworkDebug("moreInfoModel = " + moreInfoModel);
        Debug.printFrameworkDebug("moreInfoJsp = " + moreInfoJsp);
        Debug.printFrameworkDebug("moreInfoTitle = " + moreInfoTitle);
        Class modelClass = null;
        for (Object obj : SessionFactoryImpl.registeredClass) {
            if (((Class)obj).getSimpleName().equals(moreInfoModel)) {
                modelClass = (Class)obj;
            }
        }
//        model = (T) baseDAO.getModelById(id, modelClass);
        Debug.printFrameworkDebug("model is null ? " + (model==null) );
        moreInfoType = (String)map.get("moreInfoType");
        if ((Validator.isEmpty(moreInfoType)||moreInfoType.equals("Auto"))) {//return the page using dynamic-config
            
        }
        String rtn = "moreInfo" + ( (Validator.isEmpty(moreInfoType)||moreInfoType.equals("Auto"))?"":moreInfoType );
        Debug.printFrameworkDebug("rtn = " + rtn);
        return rtn;
    }
    
    public String getFileToBeID() {
        return CommonFunction.getId(20);
    }
    
    public String getSession_us_div_filter() {
        return session_us_div_filter;
    }

    public void setSession_us_div_filter(String session_us_div_filter) {
        this.session_us_div_filter = session_us_div_filter;
    }
}
