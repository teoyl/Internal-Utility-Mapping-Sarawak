package com.sains.framework.sam.dao;

import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.common.util.Validator;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.web.LoginAction;
import com.sains.framework.model.ParameterModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;

public class ParameterDAOImpl extends BaseDAOImpl<List<ParameterModel>> implements BaseDAO<List<ParameterModel>>{
    @Override
    public List update(List<ParameterModel> paramItems) throws Exception{
        try {
            beginBatchTransaction();
            if (paramItems != null) {
//                System.out.println("paramItems.size = " + paramItems.size());
                for (ParameterModel parameterItem : paramItems) {
                    if (parameterItem.getField_indicator().equalsIgnoreCase("input")) { //only update for the indicator = input
                        parameterItem = (ParameterModel) setObjectUpdateProperties(parameterItem, getSession());
                        if (parameterItem.getField_indicator().equalsIgnoreCase("input")) { // Double checking: only update for the indicator = input (DB Model)
                            getSession().update(parameterItem);
                        }
                    }
                }
            }
            commitBatchTransaction();
        } catch (Exception ex) {
            rollbackBatchTransaction();
            throw ex;
        } finally {
            closeSession();
        }
        return null;
    }
    
    private ParameterModel getFromList(String paramId, List<ParameterModel> paramList) {
        Debug.printDebug("paramId = " + paramId);
        for (ParameterModel paramModel : paramList) {
            Debug.printDebug("paramModel.ID = " + paramModel.getID());
            if (paramModel.getID().equals(paramId)) {
                return paramModel;
            }
        }
        return null;
    }
    
    @Override
    public void insert(List<ParameterModel> paramItems) throws Exception{
        try {
            Debug.printDebug("in insert parameter");
            BaseDAO dao = new BaseDAOImpl();
            dao.setSession(getSession());
            if (Validator.isEmpty(paramItems.get(0).getSystem_code())) {
                throw new CustomBaseException("System Code is required");
            }
            if (Validator.isEmpty(paramItems.get(0).getSystem_descs())) {
                throw new CustomBaseException("System Name is required");
            }
            Map param = new HashMap();
            param.put("system_code", paramItems.get(0).getSystem_code());
            if (this.sqlCountRecord("select count(*) from t_system_parameter where system_code = :system_code", param) > 0) {
                throw new CustomBaseException("System Code ["+ paramItems.get(0).getSystem_code() +"] already exists");
            }
            beginBatchTransaction();
            ParameterModel oriParam = (ParameterModel)dao.getModelById(paramItems.get(0).getID(), ParameterModel.class);
            param.clear();
            param.put("system_code", oriParam.getSystem_code());
            getSession().evict(oriParam);
//            BaseActionSupport.defaultEntIdToParam(param);
            List<ParameterModel> sysParamList = dao.list_order(param, ParameterModel.class, false, "order by group_code, group_item_order");
            ParameterModel newParamModel = null;
            for (ParameterModel paramModel : sysParamList) {
                if (paramModel.getField_indicator().equalsIgnoreCase("input")) { //get the new parameter value.
                    newParamModel = getFromList(paramModel.getID(), paramItems);
                    paramModel.setParameter_value(newParamModel.getParameter_value());
                }
                getSession().evict(paramModel);
                paramModel.defaultAddProperties();
                paramModel.setSystem_code(paramItems.get(0).getSystem_code());
                paramModel.setSystem_descs(paramItems.get(0).getSystem_descs());
                getSession().save(paramModel);
            }
//            if (paramItems != null) {
////                System.out.println("paramItems.size = " + paramItems.size());
//                for (ParameterModel parameterItem : paramItems) {
//                    if (parameterItem.getField_indicator().equalsIgnoreCase("input")) { //only update for the indicator = input
//                        parameterItem = (ParameterModel) setObjectUpdateProperties(parameterItem, getSession());
//                        if (parameterItem.getField_indicator().equalsIgnoreCase("input")) { // Double checking: only update for the indicator = input (DB Model)
//                            getSession().save(parameterItem);
//                        }
//                    }
//                }
//            }
            commitBatchTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
            rollbackBatchTransaction();
            throw ex;
        } finally {
            closeSession();
        }
    }
    
    public ParameterModel getParameterModel(String systemCode, String parameterCode) throws Exception{
        Query query;
        query = getSession().createQuery("from ParameterModel where " 
                + " system_code = :systemCode and parameter_code = :parameterCode");
        query.setString("systemCode", systemCode);
        query.setString("parameterCode", parameterCode);
        return (ParameterModel)query.uniqueResult();
    }
}