package com.sains.framework.base;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.Transaction;

public interface BaseDAO<T> extends BaseCUDDAO<T>{
    public Long nativeSqlCountRecord(String sql);
    public Long countRecord(Map param, Class modelClass, boolean advanceSearch);
    public List getListFromSql(String strSql, Map<Integer,String> param) throws Exception;  // ThoTH @ 20-Aug-2014
    public Integer executeSql(String strSql) throws Exception;  // ThoTH @ 15-Aug-2014
    public String getSingleValue(String strSql) throws Exception;  // ThoTH @ 15-Aug-2014
    public String getSingleValue(String strSql, Map<Integer,String> param) throws Exception; // ThoTH @ 11-Nov-2014
    public Integer getDateDiff(Timestamp start, Timestamp end) throws Exception; // ThoTH @ 16-Nov-2015
    public List<T> list(Map param, Class modelClass);
    public List<T> list_order(Map param, Class modelClass, String orderBy);
    public List<T> list(Map param, Class modelClass, boolean advanceSearch, String pWildSearch);
    public List<T> list_order(Map param, Class modelClass, boolean advanceSearch, String pWildSearch, String orderBy);
    public List<T> list(Map param, Class modelClass, boolean advanceSearch);
    public List<T> list_order(Map param, Class modelClass, boolean advanceSearch, String orderBy);
    //public List<T> list2(Map param, T model, boolean advanceSearch);
    public List<T> list(Class modelClass);
//    @Deprecated
    /**
     * use getModelById2() or session.get(Object.class, object_ID) to get the model
     */
    public T getModelById(Integer id, Class modelClass);
//    @Deprecated
    /**
     * use getModelById2() or session.get(Object.class, object_ID) to get the model
     */
//    public T getModelById(String id, T model);
    
    public T getModelById(String id, Class objectClass);

    public T getModelByCode(String codeName, String code, T model);
    public Boolean isSessionNull();  // ThOTH @ 19-Jan-2016
    public Session getSession();
    public void setSession(Session session);
    public void closeSession();
    public Transaction beginBatchTransaction();
    public void commitBatchTransaction() throws Exception;
    public void rollbackBatchTransaction();
    public boolean isDuplicate(String fieldName, T model);
    public List<String> checkDuplicateFields(String[] fieldNames, T model, Boolean uniqueByColumns) throws Exception;
    public boolean isRecursive(String parentIDField, String parentModelMethod, T model);
    //added dev 2
    public void checkForReferenced(Object model)throws Exception;
    public void checkData(Object model, Session session)throws Exception;
//    public void checkData(Object model)throws Exception;
    public Object getObjectByCode(String codeName, String code, Object model);
    public Object getObjectById(String id, Class objectClass);
    public Long sqlCountRecord(String sql, Map<String, Object> param) throws Exception;
    public void daoUpdate(Object updatingModel) throws Exception;    
    
    public String getRunningSeq(String strType, String strYear, String prefix, String postfix, Boolean appendZero, String strFormat) throws Exception;
    
    public String getUserId();  // ThoTH @ 9-Nov-2015
    public String getLoginId();  // ThoTH @ 29-Oct-2015
    
    public String getRunningSeq2(String strType, String strYear, String prefix, String postfix, Boolean appendZero, String strFormat) throws Exception ;
    public void setIsPaging(Boolean isPaging);
    public void setMaxSize(Integer maxSize);
    public void setFirstResult(Integer firstResult);
}

//package com.sains.framework.base;
//
//import java.util.List;
//import java.util.Map;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//
//public interface BaseDAO<T> extends BaseCUDDAO<T>{
//    public List<T> list(Map param, T model);
//    public List<T> list_order(Map param, T model, String orderBy);
//    public List<T> list(Map param, T model, boolean advanceSearch, String pWildSearch);
//    public List<T> list_order(Map param, T model, boolean advanceSearch, String pWildSearch, String orderBy);
//    public List<T> list(Map param, T model, boolean advanceSearch);
//    public List<T> list_order(Map param, T model, boolean advanceSearch, String orderBy);
//    //public List<T> list2(Map param, T model, boolean advanceSearch);
//    public List<T> list(T model);
//    public T getModelById(String id, T model);
//    public T getModelByCode(String codeName, String code, T model);
//    public Object getObjectByCode(String codeName, String code, Object model);
//    public Session getSession();
//    public void setSession(Session session);
//    public void closeSession();
//    public Transaction beginBatchTransaction();
//    public void commitBatchTransaction() throws Exception;
//    public void rollbackBatchTransaction();
//    public boolean isDuplicate(String fieldName, T model);
//    public List<String> checkDuplicateFields(String[] fieldNames, T model, Boolean uniqueByColumns);
//    public boolean isRecursive(String parentIDField, String parentModelMethod, T model);
//    public Long sqlCountRecord(String sql) throws Exception;
//    public void checkForReferenced(Object model)throws Exception;
//}
