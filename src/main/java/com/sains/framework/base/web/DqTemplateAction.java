package com.sains.framework.base.web;

import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.CommonComparator;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.Debug;
import com.sains.framework.model.DqDatasourceModel;
import com.sains.framework.model.DqTemplateFieldModel;
import com.sains.framework.model.DqTemplateModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public class DqTemplateAction extends BaseActionSupport<DqTemplateModel> implements ModelDriven<DqTemplateModel> {

    public DqTemplateAction() {
        // change the DAO to the correct service.
        //baseDAO = new BaseDAOImpl();
        setAction("DqTemplate");
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
        if (model.getTemplateFieldList() != null && !model.getTemplateFieldList().isEmpty()) {
            Boolean foundAtLeastOne = Boolean.FALSE;
            for (DqTemplateFieldModel templateField : model.getTemplateFieldList()) {
                if (templateField.getFld_showMe_boo()) {
                    foundAtLeastOne = Boolean.TRUE;
                    break;
                }
            }
            if (!foundAtLeastOne) {
                addActionError(getText("DqTemplate.error.atLeastOne"));
            }
        }
    }
    
    private String theStatus = null;

    public void setTheStatus(String theStatus) {
        this.theStatus = theStatus;
    }
    public String getTheStatus() {
        return theStatus;
    }
    
    @Override
    public String loadAddPage() {
        if (model.getTemplateFieldList() != null && !model.getTemplateFieldList().isEmpty()) {
            sortTemplateField();
        }
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }
    
    private List<Options> columnList = null;
    public List<Options> getColumnList() {
        return columnList;
    }
    public void setColumnList(List<Options> columnList) {
        this.columnList = columnList;
    }
    
    List<Map<String, Object>> listOfMap = null;
    public String showColumns() {
        if (columnList == null) {
            columnList = new ArrayList();
        }
        BaseDAO retrivingDAO = baseDAO;
        DqDatasourceModel dsModel = (DqDatasourceModel)retrivingDAO.getModelById(model.getTmpl_ds_id(), DqDatasourceModel.class);
        
        NativeQuery query = baseDAO.getSession().createNativeQuery("select * from " + dsModel.getDs_view_name() + " order by 1 limit 1");
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        listOfMap = query.list();
        for (String key : listOfMap.get(0).keySet()) {
            columnList.add(new Options(key, org.apache.commons.text.WordUtils.capitalize(key.toLowerCase().replaceAll("_", " "))));
        }
        Collections.sort(columnList, new CommonComparator(new String[]{"getValueData"}));
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
            if (getActionErrors().size() > 0) {
                return "add_step2";
            }
        } catch (Exception e) {
        return "add_step2";
    }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }
    @Override
    public String processUpdate() {
        try {
            super.processUpdate();
            if (getActionErrors().size() > 0) {
                return "edit_step2";
            }
        } catch (Exception e) {
            return "edit_step2";
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }
    
    private void sortTemplateField() {
        Collections.sort(model.getTemplateFieldList(), new CommonComparator(new String[]{"getFld_order"}));
    }
    public String goEditPage() {
        sortTemplateField();
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }
    public String edit_step2() {
        sortTemplateField();
        if (add_step2().equals(SystemConstants.ACTION_Status.LOAD_ADD_PAGE)) {
            return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
        }
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
                addActionMessage(getText("DqTemplate.noRecord"));
            }
        } catch (Exception e) {
            addActionError(getText("DqTemplate.failToRetrieveData"));
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
                    Debug.printFrameworkDebug(" not found and removed ");
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
                    Debug.printFrameworkDebug(" not found and added ");
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
}
