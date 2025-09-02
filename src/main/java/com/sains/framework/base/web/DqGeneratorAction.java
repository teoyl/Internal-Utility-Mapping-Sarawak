package com.sains.framework.base.web;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.CommonComparator;
import com.sains.common.util.CriteriaConverter;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.model.DqDatasourceModel;
import com.sains.framework.model.DqLogModel;
import com.sains.framework.model.DqTemplateFieldModel;
import com.sains.framework.model.DqTemplateModel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public class DqGeneratorAction extends BaseActionSupport<DqTemplateModel> implements ModelDriven<DqTemplateModel> {

    public DqGeneratorAction() {
        // change the DAO to the correct service.
        //baseDAO = new BaseDAOImpl();
        setAction("DqRptGen");
        model = new DqTemplateModel();
        // set the required field for common check.
        //getRequiredParam().put("column_name1", "column.name1");
        //getRequiredParam().put("column_name2", "column.name2");


    }

    @Override // change the "String" and return value to the
    public DqTemplateModel getModel() {
        return model;
    }

    @Override
    public void specificValidation(String validationType) {
    }
    
    @Override
    public String loadAddPage() {
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }
    
    private List<Options> columnList = null;
    public List<Options> getColumnList() {
        return columnList;
    }
    public void setColumnList(List<Options> columnList) {
        this.columnList = columnList;
    }
    
    private Map<String, String> columnTypeMap = null;
    
    List<Map<String, Object>> listOfMap = null;
    public String showColumns() {
        if (columnTypeMap == null) {
            columnTypeMap = new HashMap();
        }
        if (columnList == null) {
            columnList = new ArrayList();
            BaseDAO retrivingDAO = baseDAO;
            DqDatasourceModel dsModel = (DqDatasourceModel)retrivingDAO.getModelById(model.getTmpl_ds_id(), DqDatasourceModel.class);

            NativeQuery query = baseDAO.getSession().createNativeQuery("select * from " + dsModel.getDs_view_name() + " order by 1 ");
            query.setMaxResults(1);
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            listOfMap = query.list();
            Object maxFieldObj = null;
            for (String key : listOfMap.get(0).keySet()) {
                columnList.add(new Options(key, org.apache.commons.text.WordUtils.capitalize(key.toLowerCase().replaceAll("_", " "))));
                if (listOfMap.get(0).get(key) == null) {
                    query = baseDAO.getSession().createNativeQuery("select max("+key+") from " + dsModel.getDs_view_name());
                    maxFieldObj = query.uniqueResult();
                } else {
                    maxFieldObj = listOfMap.get(0).get(key);
                }
                if (maxFieldObj == null) {
                    columnTypeMap.put(key, "String");
                } else {
                    if (maxFieldObj instanceof Timestamp) { 
                        columnTypeMap.put(key, "Timestamp");
                    } else if (maxFieldObj instanceof java.util.Date) { 
                        columnTypeMap.put(key, "Date");
                    } else if (maxFieldObj instanceof String) { 
                        columnTypeMap.put(key, "String");
                    } else {
                        columnTypeMap.put(key, "Numeric");
                    }
                }
            }
            Collections.sort(columnList, new CommonComparator(new String[]{"getValueData"}));
        }
        return "showColumns";
    }
    
//    private List fieldIdxList = new ArrayList();
//    public List getFieldIdxList() {
//        return fieldIdxList;
//    }
//    public void setFieldIdxList(List fieldIdxList) {
//        this.fieldIdxList = fieldIdxList;
//    }
    private Long templateCount = null;
    public Long getTemplateCount() {
        return templateCount;
    }
    public void setTemplateCount(Long templateCount) {
        this.templateCount = templateCount;
    }
    
    @Override
    public String processInsert() {
        try {
            super.processInsert();
        } catch (Exception e) {
            return "add_step2";
        }
        return SUCCESS;
    }
    
    private List<DqTemplateFieldModel> searchFieldList = new ArrayList();
    public List<DqTemplateFieldModel> getSearchFieldList() {
        return searchFieldList;
    }
    public void setSearchFieldList(List<DqTemplateFieldModel> searchFieldList) {
        this.searchFieldList = searchFieldList;
    }
    
    private List<DqTemplateFieldModel> displayFieldList = null;
    public List<DqTemplateFieldModel> getDisplayFieldList() {
        if (displayFieldList == null) displayFieldList = new ArrayList();
        return displayFieldList;
    }
    public void setDisplayFieldList(List<DqTemplateFieldModel> displayFieldList) {
        this.displayFieldList = displayFieldList;
    }
    
    
    public String saveRptQuery() {
        return goGeneratorPage();
    }
    
    private String getSearchFieldCol(Integer searchFieldId) {
        for (DqTemplateFieldModel searchField : model.getTemplateFieldList()) {
            if (searchField.getFld_id().equals(searchFieldId)) {
                return searchField.getFld_col();
            }
        }
        return "";
    }
    
    private List resultList = null;
    public List getResultList() {
        if (resultList == null) {
            resultList = new ArrayList();
        }
        return resultList;
    }
    public void setResultList(List resultList) {
        this.resultList = resultList;
    }
    
    private List criteriaList = null;
    public List getCriteriaList() {
        if (criteriaList == null) {
            criteriaList = new ArrayList();
        }
        return criteriaList;
    }
    public void setCriteriaList(List criteriaList) {
        this.criteriaList = criteriaList;
    }
    
    public String generateDqRpt() {
        CriteriaConverter cc = null;
        StringBuffer buffer = new StringBuffer();
        BaseDAO retrievalDAO = baseDAO;
        model = (DqTemplateModel)retrievalDAO.getModelById(model.getTmpl_id(), DqTemplateModel.class);
        showColumns();
        DqDatasourceModel dsModel = (DqDatasourceModel) retrievalDAO.getModelById(model.getTmpl_ds_id(), DqDatasourceModel.class);
        for (DqTemplateFieldModel searchField : model.getTemplateFieldList()) {
            if (searchField.getFld_showMe_boo()) {
                if (listOfMap.get(0).keySet().contains(searchField.getFld_col())) {
                    if (buffer.length() <= 0) {
                        buffer.append(searchField.getFld_col());
                    } else {
                        buffer.append(", ").append(searchField.getFld_col());
                    }
                }
            }
        }
        buffer.insert(0, "SELECT ").append(" FROM ").append(dsModel.getDs_view_name())
                .append( (Validator.isEmpty(model.getTmpl_cond())?"":" WHERE (" + model.getTmpl_cond() + ")" ) );
        StringBuffer condition = new StringBuffer();
        Map criteriaMap = new TreeMap();
        Map criteriaMap_log = new TreeMap();
        Map criteriaHeader = new TreeMap();
        Boolean addedLbl = Boolean.FALSE;
        for (DqTemplateFieldModel searchField : searchFieldList) {
            
            if (!addedLbl) {
                criteriaHeader.put(getSearchFieldCol(searchField.getFld_id()), getText("DqRptGen.criteria"));
                addedLbl = Boolean.TRUE;
            } else {
                criteriaHeader.put(getSearchFieldCol(searchField.getFld_id()), "");
            }
            DqTemplateFieldModel displayField = new DqTemplateFieldModel();
            displayField.setFld_col(getSearchFieldCol(searchField.getFld_id()));
            getDisplayFieldList().add(displayField);
            if (!Validator.isEmpty(searchField.getFld_col())) {
                if (cc == null) {
                    cc = new CriteriaConverter(Boolean.TRUE);
                }
                if (condition.length() > 0) {
                    if (getFieldType(getSearchFieldCol(searchField.getFld_id())).equals("Timestamp") || getFieldType(getSearchFieldCol(searchField.getFld_id())).equals("Date")) {
//                    if (listOfMap.get(0).get(getSearchFieldCol(searchField.getFld_id())) instanceof Timestamp) {
                        condition.append(cc.dateCriteria("and", getSearchFieldCol(searchField.getFld_id()), searchField.getFld_col()));
                    } else if (getFieldType(getSearchFieldCol(searchField.getFld_id())).equals("String")) {
//                    } else if (listOfMap.get(0).get(getSearchFieldCol(searchField.getFld_id())) instanceof String) {
                        condition.append(cc.strCriteria("and", getSearchFieldCol(searchField.getFld_id()), searchField.getFld_col()));
                    } else {
                        condition.append(cc.numericCriteria("and", getSearchFieldCol(searchField.getFld_id()), searchField.getFld_col()));
                    }
                } else {
                    if (getFieldType(getSearchFieldCol(searchField.getFld_id())).equals("Timestamp") || getFieldType(getSearchFieldCol(searchField.getFld_id())).equals("Date")) {
//                    if (listOfMap.get(0).get(getSearchFieldCol(searchField.getFld_id())) instanceof Timestamp) {
                        condition.append(cc.dateCriteria("", getSearchFieldCol(searchField.getFld_id()), searchField.getFld_col()));
                    } else if (getFieldType(getSearchFieldCol(searchField.getFld_id())).equals("String")) {
//                    } else if (listOfMap.get(0).get(getSearchFieldCol(searchField.getFld_id())) instanceof String) {
                        condition.append(cc.strCriteria("", getSearchFieldCol(searchField.getFld_id()), searchField.getFld_col()));
                    } else {
                        condition.append(cc.numericCriteria("", getSearchFieldCol(searchField.getFld_id()), searchField.getFld_col()));
                    }
                }
                if (criteriaMap.size() == 0) {
                    criteriaMap.put(getSearchFieldCol(searchField.getFld_id()), "Criteria\r\n"+searchField.getFld_col());
                    criteriaMap_log.put(getSearchFieldCol(searchField.getFld_id()), "Criteria\r\n"+searchField.getFld_col());
                } else {
                    criteriaMap.put(getSearchFieldCol(searchField.getFld_id()), "\r\n"+searchField.getFld_col());
                    criteriaMap_log.put(getSearchFieldCol(searchField.getFld_id()), "\r\n"+searchField.getFld_col());
                    
                }
            } else {
                if (criteriaMap.size() == 0) {
                    criteriaMap.put(getSearchFieldCol(searchField.getFld_id()), "Criteria\r\n~nil~");
//                    criteriaMap_log.put(getSearchFieldCol(searchField.getFld_id()), "Criteria\r\n~nil~");
                } else {
                    criteriaMap.put(getSearchFieldCol(searchField.getFld_id()), "\r\n~nil~");
//                    criteriaMap_log.put(getSearchFieldCol(searchField.getFld_id()), "\r\n~nil~");
                }
            }
        }
//        getCriteriaList().add(criteriaHeader);
        getCriteriaList().add(criteriaMap);
        
        NativeQuery query = null;
        if (condition.length() > 0) {
            if (Validator.isEmpty(model.getTmpl_cond())) {
                buffer.append(" where ");
            } else {
                buffer.append(" and ");
            }
            buffer.append(condition.toString());
            query = baseDAO.getSession().createSQLQuery(buffer.toString());
            for (String key : cc.criteriaMap.keySet()) {
                if (cc.criteriaMap.get(key)!=null && cc.criteriaMap.get(key).getClass().isArray()) {
                    query.setParameterList(key, (Object[])cc.criteriaMap.get(key));
                } else {
                    query.setParameter(key, cc.criteriaMap.get(key));
                }
            }
        } else {
            query = baseDAO.getSession().createSQLQuery(buffer.toString());
        }
//        System.out.println("buffer.toString = " + buffer.toString());
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        resultList = query.list();
        try {
            baseDAO.beginBatchTransaction();
            DqLogModel logModel = new DqLogModel();
            logModel.defaultAddProperties();
            logModel.setTmpl_id(model.getTmpl_id());
            logModel.setDq_condition(criteriaMap_log.toString().substring(1, criteriaMap_log.toString().length() - 1));
            baseDAO.getSession().save(logModel);
            baseDAO.commitBatchTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            addActionError(getText("DqRptGen.error.failToLog"));
            resultList.clear();
            baseDAO.rollbackBatchTransaction();
        }
//        System.out.println("resultList.size = " + resultList.size());
        return "resultPage";
    }
    public String goGeneratorPage() {
        if (Validator.isEmpty(getId())) {
            setId(model.getID());
        }
        model = super.processEdit(model);
        if (searchFieldList.isEmpty()) {
            for (DqTemplateFieldModel searchField : model.getTemplateFieldList()) {
                if (searchField.getFld_showMe_boo()) {
                    searchField.setFld_col("");
                    searchFieldList.add(searchField);
                }
            }
        }
        return "generatorPage";
    }
    
    public String edit_step2() {
        add_step2();
        return "edit_step2";
    }
    public String add_step2() {
        showColumns();
        Integer colListIdx = 1;
        try {
            BaseDAO retrievalDAO = baseDAO;
            DqDatasourceModel dsModel = (DqDatasourceModel) retrievalDAO.getModelById(model.getTmpl_ds_id(), DqDatasourceModel.class);
            String sql = "select count(*) from " +dsModel.getDs_view_name() + (Validator.isEmpty(model.getTmpl_cond())?"":" where "+model.getTmpl_cond());
            templateCount = retrievalDAO.sqlCountRecord(sql, null);
            if (templateCount <= 0) {
                addActionMessage("DqTemplate.noRecord");
            }
        } catch (Exception e) {
            addActionError("DqTemplate.failToRetrieveData");
            return loadAddPage();
        }
        if (model.getTemplateFieldList()==null || model.getTemplateFieldList().isEmpty()) {
            model.setTemplateFieldList(new ArrayList());
            for (Options option : columnList) {
                DqTemplateFieldModel templateField = new DqTemplateFieldModel();
                templateField.setFld_col(option.getKeyData());
                templateField.setFld_lbl(option.getValueData());
//                fieldIdxList.add(colListIdx);
                templateField.setFld_order(10 * colListIdx++);
                templateField.setFld_showMe("Y");//default to show
                model.getTemplateFieldList().add(templateField);
            }
        } else {
            for (int idx = model.getTemplateFieldList().size()-1; idx >= 0; idx--) {
                Boolean foundColumn = Boolean.FALSE;
                DqTemplateFieldModel templateField  = model.getTemplateFieldList().get(idx);
                for (Options option : columnList) {
                    if (templateField.getFld_col().equals(option.getKeyData())) { //found 
                        foundColumn = Boolean.TRUE;
                    }
                }
                if (!foundColumn) {
                    System.out.println(" not found and removed ");
                    model.getTemplateFieldList().remove(idx);
                }
            }
            colListIdx = 0;
            for (Options option : columnList) {
                Boolean foundColumn = Boolean.FALSE;
                for (int idx = 0; idx < model.getTemplateFieldList().size(); idx++) {
                    DqTemplateFieldModel templateField  = model.getTemplateFieldList().get(idx);
                    if (templateField.getFld_col().equals(option.getKeyData())) { //found 
                        foundColumn = Boolean.TRUE;
                    }
                }
                if (!foundColumn) {
                    System.out.println(" not found and added ");
                    DqTemplateFieldModel templateField = new DqTemplateFieldModel();
                    templateField.setFld_col(option.getKeyData());
                    templateField.setFld_lbl(option.getValueData());
                    templateField.setFld_showMe("Y");//default to show
                    model.getTemplateFieldList().add(templateField);
                }
            }
            
            colListIdx = 1;
            for (DqTemplateFieldModel templateField : model.getTemplateFieldList()) {
//                fieldIdxList.add(colListIdx);
                templateField.setFld_order(10 * colListIdx++);
            }
        }
        return "add_step2";
    }
    
    public String getFieldType(String fieldName) {
        if (columnTypeMap == null) {
            showColumns();
        }
        return columnTypeMap.get(fieldName);
    }
}
