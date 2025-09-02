package com.sains.framework.base;


import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.Validator;
import com.sains.framework.model.User;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import com.sains.common.util.CriteriaConverter;
import com.sains.common.util.DateUtil;
import com.sains.common.util.SeqModel;
import com.sains.common.util.SystemConstants;
import static com.sains.framework.base.ModelBase.isAutoNumberPk;
import static com.sains.framework.base.ModelBase.isIntegerPk;
import com.sample.UppyModel;
import com.sample.UppyParentModel;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.text.WordUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.LockOptions;
import org.hibernate.TransactionException;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
//import com.elodgement.deal.model.ElDealStatusHistoryModel;

public class BaseDAOImpl<T> implements BaseDAO<T> {
    public static final class InsertUpdate {
        public static final String INSERT = "insert";
        public static final String UPDATE = "update";
    }
    protected Boolean manualUpdate = Boolean.FALSE;
    protected static Integer openCount = 0;
    protected static Integer closeCount = 0;
    private int noChangeLength = SystemConstants.CRITERIA.NO_CHANGE.length();
    protected boolean loadCount = Boolean.TRUE;
    protected boolean traceCount = Boolean.TRUE;
    private boolean noChange = false;
    private Boolean isPaging = Boolean.FALSE;
    public static Map<String, String> sessionIdHashCode = new HashMap();
    private Integer firstResult = 0;
    private Integer maxSize = 10;
//    private boolean isOracle = SystemConstants.DOMAIN.dbServer.equalsIgnoreCase("Oracle");

    private final String rowchangedbetweenretrieveupdate="Row Changed between Retrieve and Update. Please reload the page to get the latest data.";

    public BaseDAOImpl() {
    }

    public BaseDAOImpl(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public BaseDAOImpl(Session session) {
        this.session = session;
    }
    private boolean autoCommit = true;
    Session session;
    Transaction transaction = null;
    private CriteriaConverter criteriaConverter = new CriteriaConverter();

    public List<T> list(Class modelClass) {
        return list(null, modelClass);
    }

    public List<T> list(Map param, Class modelClass) {
        return list(param, modelClass, false);
    }

    public List<T> list(Map param, Class modelClass, boolean advanceSearch, String pWildSearch) {
        if (pWildSearch.equalsIgnoreCase("y")) {
            criteriaConverter.setWildSearch("Y");
        } else {
            criteriaConverter.setWildSearch("N");
        }
        return list(param, modelClass, advanceSearch);
    }

    @SuppressWarnings("unchecked")
    public List<T> list(Map param, Class modelClass, boolean advanceSearch) {
        List<T> objs = null;

        //param.put("us_user_id", "thensw");
        //param.put("us_id", new Integer(10));
        List paramValue = new ArrayList();
        try {
            String sql = "select _self from " + modelClass.getName() + " _self ";
            String join = "";

            Query query;
            if (param != null) {
                String condition = "";
                if (advanceSearch) {
                    for (Object obj : param.keySet()) {
                        if (obj.toString().equals("customSQL")) {
                            condition += (condition.length() <= 0) ? "where " + param.get(obj) : " and " + param.get(obj);
                            continue;
                        }
                        if (obj.toString().startsWith(SystemConstants.QUERY.JOIN)){
                            join += param.get(obj) + " ";
                            continue;
                        }
                        if (param.get(obj).toString().indexOf(SystemConstants.CRITERIA.NESTEDSQL) >= 0) {
                            String strNestedSql = (String) obj + " " + param.get(obj).toString().substring(9);

                            condition += (condition.length() <= 0) ? "where " + strNestedSql : " and " + strNestedSql;
                            continue;
                        }

                        if (condition.length() <= 0) {
                            condition += "where " + criteriaConverter.strCriteria("", (String) obj, param.get(obj).toString());
                        } else {
                            condition += " " + criteriaConverter.strCriteria("and", (String) obj, param.get(obj).toString());
                        }
                        paramValue.add(param.get(obj));
                    }
                    sql += join + condition;
//                    System.out.println("sql = " + sql);
                    query = getSession().createQuery(sql);
                } else {
                    for (Object obj : param.keySet()) {
                        if (obj.toString().equals("customSQL")) {
                            condition += (condition.length() <= 0) ? "where " + param.get(obj) : " and " + param.get(obj);
                            continue;
                        }
                        if (obj.toString().startsWith(SystemConstants.QUERY.JOIN)){
                            join += param.get(obj) + " ";
                            continue;
                        }
                        if (((String) obj).startsWith(SystemConstants.CRITERIA.NO_CHANGE)) {
                            obj = ((String) obj).substring(noChangeLength);
                            noChange = true;
                        } else {
                            noChange = false;
                        }
                        String criteriaName = (String)obj;
                        if (criteriaName.indexOf(".") >= 0) {
                            criteriaName = criteriaName.replace(".", "_");
                        }
                        if (condition.length() <= 0) {
                            if (noChange) {
                                condition += " where ltrim(rtrim(" + obj + ")) = ltrim(rtrim(:" + criteriaName + "))";
                            } else {
                                condition += " where lower(ltrim(rtrim(" + obj + "))) = lower(ltrim(rtrim(:" + criteriaName + ")))";
                            }
                        } else {
                            if (noChange) {
                                condition += " and ltrim(rtrim(" + obj + ")) = ltrim(rtrim(:" + criteriaName + "))";
                            } else {
                                condition += " and lower(ltrim(rtrim(" + obj + "))) = lower(ltrim(rtrim(:" + criteriaName + ")))";
                            }
                        }
                        paramValue.add(param.get(obj));
                    }
                    sql += join + condition;
//                    System.out.println("sql = " + sql);

                    query = getSession().createQuery(sql);

                    for (Object obj : param.keySet()) {
                        if (obj.toString().equals("customSQL")) {
                            continue;
                        }
                        if (obj.toString().startsWith(SystemConstants.QUERY.JOIN)){
                            continue;
                        }
                        String criteriaName = (String)obj;
                        if (((String) obj).startsWith(SystemConstants.CRITERIA.NO_CHANGE)) {
                            criteriaName = ((String) obj).substring(noChangeLength);
                        }
                        if (criteriaName.indexOf(".") >= 0) {
                            criteriaName = criteriaName.replace(".", "_");
                        }
                        String paramType = param.get(obj).getClass().getSimpleName();
                        if (paramType.equals("String")) {
                            query.setString((String) criteriaName, (String) param.get(obj));
                        } else if (paramType.equals("Double")) {
                            query.setDouble((String) criteriaName, (Double) param.get(obj));
                        } else if (paramType.equals("Long")) {
                            query.setLong((String) criteriaName, (Long) param.get(obj));
                        } else if (paramType.equals("Integer")) {
                            query.setInteger((String) criteriaName, (Integer) param.get(obj));
                        } else if (paramType.equals("Date")) {
                            query.setTimestamp((String) criteriaName, (java.util.Date) param.get(obj));
                        }
                    }
                }
            } else {
                query = getSession().createQuery(sql);
            }
            
            objs = query.list();
        } catch (Exception e) {
            checkException(e);
            new LogFunction().logError(this.getClass(), "", e);
        }
        return objs;
    }

    @SuppressWarnings("unchecked")
    public Object getSingleObject(Map param, Object model, boolean advanceSearch) {
        Object rtnObj = null;

        //param.put("us_user_id", "thensw");
        //param.put("us_id", new Integer(10));
        List paramValue = new ArrayList();
        try {
            String sql = "select _self from " + model.getClass().getSimpleName() + " _self ";
            String join = "";

            Query query;
            if (param != null) {
                String condition = "";
                if (advanceSearch) {
                    for (Object obj : param.keySet()) {
                        if (obj.toString().equals("customSQL")) {
                            condition += (condition.length() <= 0) ? "where " + param.get(obj) : " and " + param.get(obj);
                            continue;
                        }
                        if (obj.toString().startsWith(SystemConstants.QUERY.JOIN)){
                            join += param.get(obj) + " ";
                            continue;
                        }
                        if (param.get(obj).toString().indexOf("NESTEDSQL") >= 0) {
                            String strNestedSql = (String) obj + " " + param.get(obj).toString().substring(9);

                            condition += (condition.length() <= 0) ? "where " + strNestedSql : " and " + strNestedSql;
                            continue;
                        }

                        if (condition.length() <= 0) {
                            condition += "where " + criteriaConverter.strCriteria("", (String) obj, param.get(obj).toString());
                        } else {
                            condition += " " + criteriaConverter.strCriteria("and", (String) obj, param.get(obj).toString());
                        }
                        paramValue.add(param.get(obj));
                    }
                    sql += join + condition;
//                    System.out.println("sql = " + sql);
                    query = getSession().createQuery(sql);
                } else {
                    for (Object obj : param.keySet()) {
                        if (obj.toString().equals("customSQL")) {
                            condition += (condition.length() <= 0) ? "where " + param.get(obj) : " and " + param.get(obj);
                            continue;
                        }
                        if (obj.toString().startsWith(SystemConstants.QUERY.JOIN)){
                            join += param.get(obj) + " ";
                            continue;
                        }
                        if (((String) obj).startsWith(SystemConstants.CRITERIA.NO_CHANGE)) {
                            obj = ((String) obj).substring(noChangeLength);
                            noChange = true;
                        } else {
                            noChange = false;
                        }
                        String criteriaName = (String)obj;
                        if (((String)criteriaName).indexOf(".") >= 0) {
                            criteriaName = ((String) criteriaName).replace(".", "_");
                        }
                        if (condition.length() <= 0) {
                            if (noChange) {
                                condition += " where ltrim(rtrim(" + obj + ")) = ltrim(rtrim(:" + criteriaName + "))";
                            } else {
                                condition += " where lower(ltrim(rtrim(" + obj + "))) = lower(ltrim(rtrim(:" + criteriaName + ")))";
                            }
                        } else {
                            if (noChange) {
                                condition += " and ltrim(rtrim(" + obj + ")) = ltrim(rtrim(:" + criteriaName + "))";
                            } else {
                                condition += " and lower(ltrim(rtrim(" + obj + "))) = lower(ltrim(rtrim(:" + criteriaName + ")))";
                            }
                        }
//                        System.out.println( criteriaName + ":" + param.get(obj));
//                        paramValue.add(param.get(obj));
                        // ThoTH @ 6-Nov-2015 :: Add no change to the paramName
                        String strTemp = (String)obj;
                        if (noChange)   strTemp = SystemConstants.CRITERIA.NO_CHANGE + strTemp;
                        paramValue.add(param.get(strTemp));
                    }
                    sql += join + condition;
//                   System.out.println("sql = " + sql);

                    query = getSession().createQuery(sql);

                    for (Object obj : param.keySet()) {
                        if (obj.toString().equals("customSQL")) {
                            continue;
                        }
                        if (obj.toString().startsWith(SystemConstants.QUERY.JOIN)){
                            continue;
                        }
                        String criteriaName = (String)obj;
                        if (((String) obj).startsWith(SystemConstants.CRITERIA.NO_CHANGE)) {
                            criteriaName = ((String) obj).substring(noChangeLength);
                        }
                        if (((String)criteriaName).indexOf(".") >= 0) {
                            criteriaName = ((String) criteriaName).replace(".", "_");
                        }
                        String paramType = param.get(obj).getClass().getSimpleName();
                        if (paramType.equals("String")) {
                            query.setString((String) criteriaName, (String) param.get(obj));
                        } else if (paramType.equals("Double")) {
                            query.setDouble((String) criteriaName, (Double) param.get(obj));
                        } else if (paramType.equals("Long")) {
                            query.setLong((String) criteriaName, (Long) param.get(obj));
                        } else if (paramType.equals("Integer")) {
                            query.setInteger((String) criteriaName, (Integer) param.get(obj));
                        } else if (paramType.equals("Date")) {
                            query.setTimestamp((String) criteriaName, (java.util.Date) param.get(obj));
                        }
                    }
                }
            } else {
                query = getSession().createQuery(sql);
            }

            rtnObj = query.uniqueResult();
        } catch (Exception e) {
            checkException(e);
            new LogFunction().logError(this.getClass(), "", e);
        }
        return rtnObj;
    }

    protected void checkException(Exception e) {
        try {
//            if (e.getCause().getMessage().indexOf("The connection is closed") > 0) {
//                System.out.println("Connection is closed...");
//            }
            if (e.getCause().getMessage().indexOf("Connection aborted by peer") >= 0 ||
                e.getCause().getMessage().indexOf("Connection reset by peer") >= 0) {
                CommonFunction.writeFile("Connection", e.getCause().getMessage());
                SessionFactoryImpl.resetSF();
                closeSession();
                session = null;
            }
        } catch (Exception e2) {
//            e2.printStackTrace();
        }
    }

    public List<T> list2(Map param, Class modelClass, boolean advanceSearch) {
        List<T> objs = null;

        //param.put("us_user_id", "thensw");
        //param.put("us_id", new Integer(10));
        List paramValue = new ArrayList();
        try {
            String sql = "select _self from " + modelClass.getClass().getSimpleName() + " _self ";
            String join = "";

            Query query;
            if (param != null) {
                String condition = "";
                if (advanceSearch) {
                    for (Object obj : param.keySet()) {
                        if (obj.toString().equals("customSQL")) {
                            condition += (condition.length() <= 0) ? "where " + param.get(obj) : " and " + param.get(obj);
                            continue;
                        }
                        if (obj.toString().startsWith(SystemConstants.QUERY.JOIN)){
                            join += param.get(obj) + " ";
                            continue;
                        }
                        if (param.get(obj).toString().indexOf("NESTEDSQL") >= 0) {
                            String strNestedSql = (String) obj + " " + param.get(obj).toString().substring(9);

                            condition += (condition.length() <= 0) ? "where " + strNestedSql : " and " + strNestedSql;
                            continue;
                        }

                        if (condition.length() <= 0) {
                            condition += "where " + criteriaConverter.strCriteria("", (String) obj, param.get(obj).toString());
                        } else {
                            condition += " " + criteriaConverter.strCriteria("and", (String) obj, param.get(obj).toString());
                        }
                        paramValue.add(param.get(obj));
                    }
                    sql += join + condition;
                    query = getSession().createQuery(sql);
                } else {
                    for (Object obj : param.keySet()) {
                        if (obj.toString().equals("customSQL")) {
                            condition += (condition.length() <= 0) ? "where " + param.get(obj) : " and " + param.get(obj);
                            continue;
                        }
                        if (obj.toString().startsWith(SystemConstants.QUERY.JOIN)){
                            join += param.get(obj) + " ";
                            continue;
                        }
                        if (((String) obj).startsWith(SystemConstants.CRITERIA.NO_CHANGE)) {
                            obj = ((String) obj).substring(noChangeLength);
                            noChange = true;
                        } else {
                            noChange = false;
                        }
                        String criteriaName = (String)obj;
                        if (((String)criteriaName).indexOf(".") >= 0) {
                            criteriaName = ((String) criteriaName).replace(".", "_");
                        }
                        if (condition.length() <= 0) {
                            if (noChange) {
                                condition += " where ltrim(rtrim(" + obj + ")) = ltrim(rtrim(:" + criteriaName + "))";
                            } else {
                                condition += " where lower(ltrim(rtrim(" + obj + "))) = lower(ltrim(rtrim(:" + criteriaName + ")))";
                            }
                        } else {
                            if (noChange) {
                                condition += " and ltrim(rtrim(" + obj + ")) = ltrim(rtrim(:" + criteriaName + "))";
                            } else {
                                condition += " and lower(ltrim(rtrim(" + obj + "))) = lower(ltrim(rtrim(:" + criteriaName + ")))";
                            }
                        }
                        paramValue.add(param.get(obj));
                    }
                    sql += join + condition;
                    query = getSession().createQuery(sql);

                    for (Object obj : param.keySet()) {
                        if (obj.toString().equals("customSQL")) {
                            continue;
                        }
                        if (obj.toString().startsWith(SystemConstants.QUERY.JOIN)){
                            continue;
                        }
                        String criteriaName = (String)obj;
                        if (((String) obj).startsWith(SystemConstants.CRITERIA.NO_CHANGE)) {
                            criteriaName = ((String) obj).substring(noChangeLength);
                        }
                        if (((String)criteriaName).indexOf(".") >= 0) {
                            criteriaName = ((String) criteriaName).replace(".", "_");
                        }
                        String paramType = param.get(obj).getClass().getSimpleName();
                        if (paramType.equals("String")) {
                            query.setString((String) criteriaName, (String) param.get(obj));
                        } else if (paramType.equals("Double")) {
                            query.setDouble((String) criteriaName, (Double) param.get(obj));
                        } else if (paramType.equals("Long")) {
                            query.setLong((String) criteriaName, (Long) param.get(obj));
                        } else if (paramType.equals("Integer")) {
                            query.setInteger((String) criteriaName, (Integer) param.get(obj));
                        } else if (paramType.equals("Date")) {
                            query.setTimestamp((String) criteriaName, (java.util.Date) param.get(obj));
                        }
                    }
                }
            } else {
                query = getSession().createQuery(sql);
            }

            objs = query.list();
        } catch (Exception e) {
            checkException(e);
            new LogFunction().logError(this.getClass(), "", e);
        }
        return objs;
    }

    @SuppressWarnings("unchecked")
    public List<T> list() {
        return list(null);
    }

    private void tryRemoveDeletedModel() {
        List<List<ModelBase>> insertUpdateList = (List)ModelBase.currentRequestObject("insertUpdateList");
        if (insertUpdateList != null) {
            for (Object listOfObject : insertUpdateList) {
                if (listOfObject instanceof List) {
                    List<ModelBase> listOfModelBase = (List)listOfObject;
                    if (listOfModelBase != null) {
                        for (int count=listOfModelBase.size()-1; count >=0 ; count--) {
                            if (listOfModelBase.get(count).get_markedAsDel() != null && listOfModelBase.get(count).get_markedAsDel().equalsIgnoreCase("Y")) {
                                listOfModelBase.remove(count);
                            }
                        }
                    }
                }
            }
        }
    }
    @Override
    public synchronized void insert(T model) throws Exception {
        try {
            csrfCheck();
            autoDuplicationCheck(model, "insert");
            beginBatchTransaction();
            defaultAddProperties(model);
            preInsert(model);
            getSession().save(model);            
            postInsert(model);
            commitBatchTransaction();
            tryRemoveDeletedModel();
//            getSession().disconnect();
        } catch (Exception ex) {
//            Method m = model.getClass().getMethod("setID", String.class);
//            m.invoke(model, "");
            rollbackBatchTransaction();
            ((ModelBase)model).clearNewModelID();
            checkException(ex);
            ex.printStackTrace();
            throw ex;
        } finally {
            ((ModelBase)model).printOperationList();
            closeSession();
        }
    }

    @Override
    public synchronized void insertWithSession(Session pSession, T model) throws Exception {
        try {
            csrfCheck();
            setSession(pSession);
            autoDuplicationCheck(model, "insert");
//            beginBatchTransaction();
            defaultAddProperties(model); //rearranged by Zhafari @ 11-Jul-2014
            preInsert(model);            //rearranged by Zhafari @ 11-Jul-2014
            getSession().save(model);
            postInsert(model);
//            commitBatchTransaction();
//            getSession().disconnect();
        } catch (Exception ex) {
//            rollbackBatchTransaction();
            checkException(ex);
            throw ex;
//        } finally {
//            closeSession();
        }
    }

    public synchronized T update(T model) throws Exception {
//        System.out.println("BDAO UPDATE:: 1111");
        //public T update(T model) throws Exception {
//        for (int count = 1; count < 3000; count++) {
//            for (int count2 = 1; count2 < 1000; count2++) {
//                System.out.print("");
//            }
//            System.out.print("Update count = ");
//            System.out.println(count);
//        }
        session = getSession();
        try {
            csrfCheck();
            autoDuplicationCheck(model, "update");
            Object localModel = null;
            // to get the data from DB and update back.
            defaultUpdateProperties(model);
            if (model instanceof UppyParentModel) {
                System.out.println("localmodel created_by = " + ((UppyParentModel)model).getCreated_by());
            }
            beginBatchTransaction();
            String temp_operation = ((ModelBase)model).get_operation();
            Object inModel = model;
            if (((ModelBase)model).isUseSQLUpdate()) {
//                System.out.println("--- in sql update model ---");
                ((ModelBase)model).sqlUpdateModel_preparedStmt(((ModelBase)model).getUpdatableColumnsByOperation(), this, Boolean.FALSE);
            } else {
                localModel = setUpdateProperties(model, session);
                if (localModel != null) {
    //                getSession().update(localModel);
                    model = (T)localModel;
    //            } else {
    //                getSession().update(model);
                }
                if (!((ModelBase)model).get_ignoreUpdate()) {
                    daoUpdate(model);
                }
            }
            ((ModelBase)model).set_operation(temp_operation);
            postUpdate(model, inModel);
            commitBatchTransaction();
            tryRemoveDeletedModel();
        } catch (Exception e) {
            rollbackBatchTransaction();
            ((ModelBase)model).clearNewModelID();
            checkException(e);
            e.printStackTrace();
            throw e;
        } finally {
//            ((ModelBase)model).printOperationList();
            closeSession();
        }
//        System.out.println("---------------------------------------------------------------------------------------------------------");
//        System.out.println("---------------------------------------------------------------------------------------------------------");
        for (Map sqlMap : ((ModelBase)model).getReversalList()) {
            System.out.println("sqlMap = " + sqlMap);
//        System.out.println("---------------------------------------------------------------------------------------------------------");
//        System.out.println("---------------------------------------------------------------------------------------------------------");
        }
        return model;
    }

    public synchronized T updateWithSession(Session pSession, T model) throws Exception {
        session = pSession;
        try {
            csrfCheck();
            autoDuplicationCheck(model, "update");
            Object localModel = null;
            // to get the data from DB and update back.
            defaultUpdateProperties(model);
            String temp_operation = ((ModelBase)model).get_operation();
            Object inModel = model;
            if (((ModelBase)model).isUseSQLUpdate()) {
                ((ModelBase)model).sqlUpdateModel_preparedStmt(((ModelBase)model).getUpdatableColumnsByOperation(), this, Boolean.FALSE);
            } else {
                localModel = setUpdateProperties(model, session);

                if (localModel != null) {
    //                getSession().update(localModel);
                    model = (T)localModel;
    //            } else {
    //                getSession().update(model);
                }
                if (!((ModelBase)model).get_ignoreUpdate()) {
                    daoUpdate(model);
                }
            }
            ((ModelBase)model).set_operation(temp_operation);
            postUpdate(model, inModel);
//            commitBatchTransaction();
        } catch (Exception e) {
//            rollbackBatchTransaction();
            checkException(e);
            throw e;
//        } finally {
//            closeSession();
        }
        return model;
    }

    /**
     * For update Single Record which is retrieve by getModelById()
    Set all Data in Model to DB
     * @param model
     * @throws Exception
     */
    public synchronized void directUpdate(T model) throws Exception {
//        session = getSession();
        try {
            beginBatchTransaction();
            getSession().update(model);
            commitBatchTransaction();
            tryRemoveDeletedModel();
        } catch (Exception e) {
            rollbackBatchTransaction();
            ((ModelBase)model).clearNewModelID();
            checkException(e);
            throw e;
        } //finally {
//            closeSession();
//        }
    }

    // ThoTH @ 11-Jun-2013
    public synchronized void directUpdateWithSession(org.hibernate.Session session, T model) throws Exception {
//        session = getSession();
        try {
//            beginBatchTransaction();
            session.update(model);
//            commitBatchTransaction();
        } catch (Exception e) {
//            rollbackBatchTransaction();
            checkException(e);
            throw e;
        } //finally {
//            closeSession();
//        }
    }

//    public synchronized void delete(String id, T model) throws Exception {
//        beginBatchTransaction();
//        try {
//            System.out.println("delete id " + id);
//            model = (T) session.get(model.getClass(), id);
//
//            // For Audit
//            String strUpdatedBy = (String) ActionContext.getContext().getSession().get("loginId");
//            System.out.println("baseDAOImpl bstrUpdatedBy =" + strUpdatedBy);
//            //Method m = model.getClass().getMethod("setUpdated_by", String.class);
//            Method getterObject = model.getClass().getMethod("getUpdated_by");
//            Method m = model.getClass().getMethod("setUpdated_by", getterObject.getClass());
//            System.out.println("baseDAOImpl  before invoke");
//            m.invoke(model, strUpdatedBy);
//            checkForReferenced(model);
//            //auditDelete(model, strUpdatedBy);
//            session.update(model);
//            session.flush();
////            // For Audit - End
////
//            session.delete(model);
//            commitBatchTransaction();
//        } catch (Exception e) {
//            rollbackBatchTransaction();
//            checkException(e);
//            throw e;
//        } finally {
//            closeSession();
//        }
//    }
//
//    public synchronized void groupDelete(String[] ids, T model) throws Exception {
//        session = getSession();
//        beginBatchTransaction();
//        try {
//            String strUpdatedBy = (String) ActionContext.getContext().getSession().get("loginId");  // For Audit
//
//            for (String deleteId : ids) {
//                model = (T) session.get(model.getClass(), deleteId);
//
//                // For Audit
//                Method m = model.getClass().getMethod("setUpdated_by", String.class);
//                m.invoke(model, strUpdatedBy);
//                checkForReferenced(model);
//                session.update(model);
//                session.flush();
//                // For Audit - End
//
//                session.delete(model);
//            }
//            commitBatchTransaction();
//        } catch (Exception e) {
//            rollbackBatchTransaction();
//            checkException(e);
//            throw e;
//        } finally {
//            closeSession();
//        }
//    }

    public synchronized void manualUpdate(T model) throws Exception {
        session = getSession();
        try {
            beginBatchTransaction();
            csrfCheck();
            manualOperation(model);
            commitBatchTransaction();
            tryRemoveDeletedModel();
        } catch (Exception e) {
            rollbackBatchTransaction();
            ((ModelBase)model).clearNewModelID();
            checkException(e);
            throw e;
        } finally {
            closeSession();
        }
    }
    
    // ThoTH @ 10-Aug-2014
    public synchronized void manualUpdateWithSession(org.hibernate.Session session, T model) throws Exception {
        this.session = session;
        try {
            manualOperation(model);
        } catch (Exception e) {
//            checkException(e);  // ThoTH:: Commented because not using and inside got CloseSession, may need to write a new one if needed
            throw e;
        }
    }

    @Override
    public void deleteModelWithPrePost(T model) throws Exception {
        System.out.println("---------------------------- deleteModelWithPrePost ----------------------------");
        csrfCheck();
        if (((ModelBase)model).getModelService((ModelBase)model) != null) {
            ((ModelBase)model).getModelService((ModelBase)model).preDelete(getSession(), model);
        } else {
            ((ModelBase)model).preDelete(getSession(), model);
        }
        ((ModelBase)model).deleteModel(getSession(), model);
        if (((ModelBase)model).getModelService((ModelBase)model) != null) {
            ((ModelBase)model).getModelService((ModelBase)model).postDelete(getSession(), model);
        } else {
            ((ModelBase)model).postDelete(getSession(), model);
        }
    }
    @Override
    public synchronized void deleteModel(T model) throws Exception {
        csrfCheck();
        ((ModelBase)model).deleteModel(getSession(), model);
    }
    @Override
    public synchronized void delete(String id, T model) throws Exception {
//        try {
//            Integer intUpdatedBy = (Integer) ActionContext.getContext().getSession().get("userId");
//            System.out.println("id = " + id);
//            if (model == null){
//                System.out.println("model is null");
//            }
//            model = (T) getSession().get(model.getClass(), new Integer(id));
//            if (!BaseActionSupport.checkEntId(model)){
//                throw new InvalidCompanyDataException();
//            }
//            checkForReferenced(model);
            // For Audit
//            Integer intUpdatedBy = (Integer) ActionContext.getContext().getSession().get("userId");
//            model = (T) session.get(model.getClass(), new Integer(id));
//            if (!BaseActionSupport.checkEntId(model)) {
//                throw new InvalidCompanyDataException();
//            }
//            // For Audit
////            Integer intUpdatedBy = (Integer) ActionContext.getContext().getSession().get("userId");
////            Method m = model.getClass().getMethod(SystemConstants.METHOD.SET_MODIFIED_BY, Integer.class);
////            m.invoke(model, intUpdatedBy);
////
////            session.update(model);
////            session.flush();
////            // For Audit - End
////
////            session.delete(model);
//            auditDelete(model, intUpdatedBy);
//            commitBatchTransaction();
//        } catch (Exception e) {
//            rollbackBatchTransaction();
//            throw e;
//        } finally {
//            closeSession();
//        }
        groupDelete(new String[]{id}, model);
    }

    @Override
    public synchronized void groupDelete(String[] ids, T model) throws Exception {
        session = getSession();
        beginBatchTransaction();
        try {
            csrfCheck();
            Object strUpdatedBy = null;
            if(((ModelBase)model).createUpdate_useUserPK()) {
                strUpdatedBy = ((ModelBase)model).getLoginUserId();
            } else {
                strUpdatedBy = ((ModelBase)model).getLoginId();
            }  // For Audit
            String strOper = ((ModelBase)model).get_operation();  // ThoTH @ 26-Jul-2013
            for (String deleteId : ids) {
                if (ModelBase.isAutoNumberPk(model.getClass())) {
                    model = (T) session.get(model.getClass(), new Integer(deleteId));
                } else {
                    model = (T) session.get(model.getClass(), deleteId);
                }
                ((ModelBase)model).set_operation(strOper);  // ThoTH @ 26-Jul-2013
                if (((ModelBase)model).getModelService((ModelBase)model) != null) {
                    Boolean noSuchMethod = Boolean.FALSE;
                    try {
                        Method m = ((ModelBase)model).getModelService().getClass().getDeclaredMethod("preDelete", Session.class, Object.class);
                    } catch (Exception e) {
                        noSuchMethod = Boolean.TRUE;
                    }
//                    ((ModelBase)model).clearMyChildMap();
//                    ((ModelBase)model).getModelService().getMyChildList_byOperation();
//
//                    ((ModelBase)model).getMyChildMap().putAll(((ModelBase)model).getModelService().getMyChildMap());
//                    ((ModelBase)model).getMyChildMapSetup().putAll(((ModelBase)model).getModelService().getMyChildMapSetup());
                    if (!noSuchMethod) {
                        ((ModelBase)model).traceModelOperation("preDelete", ((ModelBase)model).getModelService().getClass().getSimpleName(), 1);
                        ((ModelBase)model).getModelService().preDelete(session, model);
                    } else {
                        ((ModelBase)model).traceModelOperation("preDelete", ((ModelBase)model).getClass().getSimpleName(), 1);
                        ((ModelBase)model).preDelete(session, model);  // ThoTH @ 26-Jul-2013
                    }
                } else {
                    ((ModelBase)model).traceModelOperation("preDelete", ((ModelBase)model).getClass().getSimpleName(), 1);
                    ((ModelBase)model).preDelete(session, model);  // ThoTH @ 26-Jul-2013
                }
                checkForReferenced(model);
                deleteMyItemsLists(model);
                // For Audit
                auditDelete(model, strUpdatedBy);
                // For Audit - End
            }
            commitBatchTransaction();
            tryRemoveDeletedModel();
        } catch (BaseException be) {
            rollbackBatchTransaction();
            ((ModelBase)model).clearNewModelID();
            throw be;
        } catch (Exception e) {
            rollbackBatchTransaction();
            ((ModelBase)model).clearNewModelID();
            throw e;
        } finally {
            closeSession();
        }
    }

    public void auditDelete(Object model) throws Exception {
//    protected void auditDelete(Object model) throws Exception {  // ThoTH @ 14-May-2013 :: Change to Public
        auditDelete(model, null);
    }

    /**
     * Update the modifiedBy and dateModified for audit purpose, and by default, call session.delete(model);
     * If want to manually delete the model, use auditDelete(model, false);
     * @param model
     * @throws Exception
     */
    protected void auditDelete(Object model, Object strUpdatedBy) throws Exception {
        auditDelete(model, strUpdatedBy, Boolean.TRUE);
//        Method m = model.getClass().getMethod(SystemConstants.METHOD.SET_MODIFIED_BY, Integer.class);
//        m.invoke(model, intUpdatedBy);
//
//        session.update(model);
//        session.flush();
//        session.delete(model);
    }

    protected void auditDelete(Object model, Object strUpdatedBy, boolean deleteModel) throws Exception {
        if (strUpdatedBy == null) {
            if(((ModelBase)model).createUpdate_useUserPK()) {
                strUpdatedBy = ((ModelBase)model).getLoginUserId();
            } else {
                strUpdatedBy = ((ModelBase)model).getLoginId();
            }
        }
        // For Audit
        if (deleteModel) session.evict(model);
        updateUpdatedBy(session, model, strUpdatedBy, deleteModel);
//        Method m = model.getClass().getMethod(SystemConstants.METHOD.SET_UPDATED_BY, String.class);
//        m.invoke(model, strUpdatedBy);
//
//            getSession().update(model);
//            if (deleteModel){
////                getSession().flush();
//                getSession().delete(model);
////                getSession().clear();
//            }
            // For Audit - End
    }

    // ThoTH @ 24-Jul-2013
    public void auditDeleteWithSession(org.hibernate.Session session, Object model, Object strUpdatedBy, boolean deleteModel) throws Exception {
        try {
            if (strUpdatedBy == null) {
                if(ModelBase.GLOBAL_USE_USER_PK_FOR_CREATE_UPDATE_BY) {
                    strUpdatedBy = ((ModelBase)model).getLoginUserId();
                } else {
                    if(((ModelBase)model).createUpdate_useUserPK()) {
                        strUpdatedBy = ((ModelBase)model).getLoginUserId();
                    } else {
                        strUpdatedBy = ((ModelBase)model).getLoginId();
                    }
                }
            }
            // For Audit
            if (strUpdatedBy != null) {
                Method m = model.getClass().getMethod(SystemConstants.METHOD.SET_UPDATED_BY, strUpdatedBy.getClass());
                m.invoke(model, strUpdatedBy);
            }

            String tableName = ((ModelBase)model).modelTableName(model.getClass());
            String tablePK = ((ModelBase)model).model_PK(model.getClass());
            if (strUpdatedBy instanceof Integer) {
                if (ModelBase.isIntegerPk(model.getClass())) {
                    session.createNativeQuery("update " + tableName + " set "+ ((ModelBase)model).defaultUpdatedBy() +" = "+strUpdatedBy+" where " +
                        tablePK + " = "+ ((ModelBase)model).getID()).executeUpdate();
                } else {
                    session.createNativeQuery("update " + tableName + " set "+ ((ModelBase)model).defaultUpdatedBy() +" = "+strUpdatedBy+" where " +
                        tablePK + " = '"+ ((ModelBase)model).getID()+"'").executeUpdate();
                }
            } else {
                if (ModelBase.isIntegerPk(model.getClass())) {
                    session.createNativeQuery("update " + tableName + " set "+ ((ModelBase)model).defaultUpdatedBy() +" = '"+strUpdatedBy+"' where " +
                        tablePK + " = "+ ((ModelBase)model).getID()).executeUpdate();
                } else {
                    session.createNativeQuery("update " + tableName + " set "+ ((ModelBase)model).defaultUpdatedBy() +" = '"+strUpdatedBy+"' where " +
                        tablePK + " = '"+ ((ModelBase)model).getID()+"'").executeUpdate();
                }
            }
            
//            session.update(model);
            if (deleteModel) {
                ((ModelBase)model).genAndKeepReverseSql(Boolean.FALSE, ModelBase.ReversalOpt.DELETE, session);
                session.flush();
                if (ModelBase.isIntegerPk(model.getClass())) {
                    session.createNativeQuery("delete from " + tableName + " where " +
                        tablePK + " = "+ ((ModelBase)model).getID()).executeUpdate();
                } else {
                    session.createNativeQuery("delete from " + tableName + " where " +
                        tablePK + " = '"+ ((ModelBase)model).getID() +"'").executeUpdate();
                }
//                session.delete(model);
//                session.clear();
            }
            // For Audit - End
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void updateUpdatedBy(org.hibernate.Session session, Object model, Object strUpdatedBy, Boolean deleteModel) throws Exception {
        try {
            String tableName = ((ModelBase)model).modelTableName(model.getClass());
            String tablePK = ((ModelBase)model).model_PK(model.getClass());
//            Class<?> c = model.getClass();
//            String fullClassName = null;
//            if (model.getClass().getSimpleName().indexOf("_$$") >= 0) {
//                fullClassName = model.getClass().getPackage().getName()+"."+model.getClass().getSimpleName().substring(0, model.getClass().getSimpleName().indexOf("_$$"));
//            } else if (model.getClass().getSimpleName().indexOf("$$") >= 0) {
//                fullClassName = model.getClass().getPackage().getName()+"."+model.getClass().getSimpleName().substring(0, model.getClass().getSimpleName().indexOf("$$"));
//            } else {
//                fullClassName = model.getClass().getPackage().getName()+"."+model.getClass().getSimpleName();
//            }
//            Class<?> cc = Class.forName(fullClassName);
//            Table table = cc.getAnnotation(Table.class);
//            Table table = c.getAnnotation(Table.class);
//            Query q = session.createNativeQuery("update " + table.name() + " set updated_by = '" + strUpdatedBy + "' where module_id = '" + getMethodValueFromObject(model, "getID")+"'");
            NativeQuery q = null;
            if (strUpdatedBy instanceof Integer) {
                if (ModelBase.isIntegerPk((model.getClass()))) {
                    q = session.createNativeQuery("update " + tableName + " set "+ ((ModelBase)model).defaultUpdatedBy() +" = " + strUpdatedBy + " where "+ tablePK + " = " + getMethodValueFromObject(model, "getID"));
                } else {
                    q = session.createNativeQuery("update " + tableName + " set "+ ((ModelBase)model).defaultUpdatedBy() +" = " + strUpdatedBy + " where "+ tablePK + " = '" + getMethodValueFromObject(model, "getID")+"'");
                }
            } else {
                if (ModelBase.isIntegerPk((model.getClass()))) {
                    q = session.createNativeQuery("update " + tableName + " set "+ ((ModelBase)model).defaultUpdatedBy() +" = '" + strUpdatedBy + "' where "+ tablePK + " = " + getMethodValueFromObject(model, "getID"));
                } else {
                    q = session.createNativeQuery("update " + tableName + " set "+ ((ModelBase)model).defaultUpdatedBy() +" = '" + strUpdatedBy + "' where "+ tablePK + " = '" + getMethodValueFromObject(model, "getID")+"'");
                }
            }

            q.executeUpdate();
            if (deleteModel) {
                ((ModelBase)model).genAndKeepReverseSql(Boolean.FALSE, ModelBase.ReversalOpt.DELETE, session);
                if (ModelBase.isIntegerPk((model.getClass()))) {
                    q = session.createNativeQuery("delete from " + tableName + " where "+ tablePK + " = " + getMethodValueFromObject(model, "getID"));
                } else {
                    q = session.createNativeQuery("delete from " + tableName + " where "+ tablePK + " = '" + getMethodValueFromObject(model, "getID")+"'");
                }
                q.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

//    @Deprecated
    /**
     * use getModelById2() or session.get(Object.class, object_ID) to get the model
     */
//    public T getModelById(String id, T model) {
//        try {
//            model = (T) getSession().get(model.getClass(), id);
//        } catch (Exception e) {
//            checkException(e);
//            new LogFunction().logError(this.getClass(), "", e);
//        }
//        return model;
//    }
    
    public T getModelById(String id, Class objectClass) {
        T obj = null;
        try {
            if (Validator.isEmpty(id)) {
                throw new CustomBaseException("Empty ID in getModelById");
            }
            if(ModelBase.isAutoNumberPk(objectClass)) {
                obj = (T) getSession().get(objectClass, new Integer(id));
            } else {
                obj = (T) getSession().get(objectClass, id);
            }
        } catch (BaseException be) {
            new LogFunction().logError(this.getClass(), "", be);
        } catch (Exception e) {
//            checkException(e);
            e.printStackTrace();
            new LogFunction().logError(this.getClass(), "", e);
        }
        return obj;
    }

    /**
     * Created By TTH @ 4-May-2011
     * To get a Model by using PkModel
     *
     * @param PkModel
     * @param model
     * @return model
     */
//    public T getModelByModel(Object ck, T model) {
//        try {
//            model = (T) getSession().get(model.getClass(), ck);
//        } catch (Exception e) {
//            checkException(e);
//            new LogFunction().logError(this.getClass(), "", e);
//        }
//        return model;
//    }
    public Object getObjectById(String id, Class modelClass) {
        Object model = null;
        try {
            if(ModelBase.isAutoNumberPk(modelClass)) {
                model = (T) getSession().get(modelClass, new Integer(id));
            } else {
                model = (T) getSession().get(modelClass, id);
            }
        } catch (Exception e) {
            checkException(e);
            new LogFunction().logError(this.getClass(), "", e);
        }
        return model;
    }

    public T getModelById_closeSession(String id, Class modelClass) {
        T model = null;
        try {
            if(ModelBase.isAutoNumberPk(modelClass)) {
                model = (T) getSession().get(modelClass, new Integer(id));
            } else {
                model = (T) getSession().get(modelClass, id);
            }
        } catch (Exception e) {
            checkException(e);
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
            closeSession();
        }
        return model;
    }

    // ThoTH @ 15-Jan-2014 :: To cater Network failure cause Connection Time-out
    public static Boolean checked=Boolean.FALSE;
    private Boolean getSession_live() {
        session = SessionFactoryImpl.getSession();
        try {
            Transaction tx = session.beginTransaction();
            tx.rollback();

        } catch (TransactionException te) {  // ThoTH @ 17-Sep-2014
            return Boolean.FALSE;
            
        } catch (Exception e) {
            if (!checked) {
                checked = Boolean.TRUE;
                new LogFunction().logError(this.getClass(), "", e);
            }
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    // ThoTH @ 19-Jan-2016
    public Boolean isSessionNull() {
        if (session == null || !session.isOpen()) return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public Session getSession_forceNew() {
        if (session != null) {
            closeSession();
        }
        return getSession();
    }
    public Session getSession() {
        if (session == null || !session.isOpen()) {
            // ThoTH @ 15-Jan-2014 :: To cater Network failure cause Connection Time-out
            session = SessionFactoryImpl.getSession();
//            try {  // ThoTH @ 17-Sep-2014 :: Catch the Exception, if is Connection Failure, direct end the Loop
//                int getConnCount = 0;  // ThoTH @ 21-Jul-2014 :: Stop after 1000, to avoid connection with Wrong Pwd coz endless loop
//                while (getConnCount++ < 100 && ! getSession_live()) {/* Loop till true */System.out.println("         ---------------- reConnecting...............");}
//            } catch (Exception e) {
//                new LogFunction().logError(this.getClass(), "", e);
//            }
            // ThoTH @ 15-Jan-2014 <END>
            if (loadCount) {
                openCount++;
//                System.out.println("openCount = " + openCount);
                String sessionHashCode = ""+System.identityHashCode(session);
                String traceStr = null;
                if (traceCount) {
                    traceStr = traceCaller();
//                    System.out.println(">>> +++ : " + traceCaller() + " OPENED - (" + (openCount) +")");
                } else {
                    traceStr = ">>> +++ : " + this.getClass().getName() + " OPENED";
//                    System.out.println(">>> +++ : " + this.getClass().getName() + " OPENED - (" + (openCount) +")");
                }
                sessionIdHashCode.put(sessionHashCode, traceStr);
            }
        }
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Transaction beginBatchTransaction() {
        if (transaction == null && autoCommit) {
            transaction = getSession().beginTransaction();
        }
        return transaction;
    }

    public void commitBatchTransaction() throws Exception {
        if (transaction != null && autoCommit) {
            if (ModelBase.currentRequestObject("__prepareStmt__list") != null) {
                for (PreparedStatement pStmt : (List<PreparedStatement>) ModelBase.currentRequestObject("__prepareStmt__list")) {
                    pStmt.executeBatch();
                }
            }
            if (ModelBase.currentRequestObject("__prepareStmt__rollback") != null) {
                PreparedStatement pStmt = (PreparedStatement) ModelBase.currentRequestObject("__prepareStmt__rollback");
                pStmt.executeBatch();
            }
            //System.out.println("transaction commit");
            transaction.commit();
            transaction = null;
            
        }
        transaction = null;
    }

    public void rollbackBatchTransaction() {
        try {
            if (transaction != null && autoCommit) {
                transaction.rollback();
            }
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
            transaction = null;
        }
    }

    public void closeAllSession() {
        List<Session> sessionList = (List) ModelBase.currentRequestObject("hibernate_session_list");
        if (sessionList != null) {
            for (Session session : sessionList) {
                if (session != null) {
                    try {
                        if (pStmt != null && pStmt.isClosed()) {
                            pStmt.close();
                        }
                        if (session.isOpen()) {
                            session.clear();
        //                    session.flush();
                            session.close();
                            if (loadCount) {
                                ++closeCount;
                                String sessionHashCode = ""+System.identityHashCode(session);
                                if (sessionIdHashCode.keySet().contains(sessionHashCode)) {
                                    sessionIdHashCode.remove(sessionHashCode);
                                }
                            }
                        }
                        session = null;
                    } catch (Exception ex) {
                        new LogFunction().logError(this.getClass(), "", ex);
                    }
                }
            }
        }
    }
    
    public void closeSession() {
        if (session != null) {
            try {
                if (pStmt != null && pStmt.isClosed()) {
                    pStmt.close();
                }
//                if (session.isConnected()) {
//                    session.disconnect();
//                }

                if (session.isOpen()) {
                    session.clear();
//                    session.flush();
                    session.close();
                    if (loadCount) {
                        ++closeCount;
//                        System.out.println("closeCount = " + closeCount);
                        String sessionHashCode = ""+System.identityHashCode(session);
                        if (sessionIdHashCode.keySet().contains(sessionHashCode)) {
                            sessionIdHashCode.remove(sessionHashCode);
                        }
                    }

                }
//                    if (!isOracle) { //only call this line if it is not Oracle Database.
//                        session.getSessionFactory().close();
//                    }
//                if (session.getSessionFactory().getCurrentSession().isOpen()){
//                    try {
//                        session.getSessionFactory().getCurrentSession().disconnect();
//                    } catch (Exception ex){
//
//                    }
//                    session.getSessionFactory().getCurrentSession().close();
//                }
                //session.getSessionFactory().close();

                // Added @ 26-Nov-2010 to Handle Concurrent Modification Exception

                session = null;
            } catch (Exception ex) {
                new LogFunction().logError(this.getClass(), "", ex);
            }
        }
    }

    protected String traceCaller(){
        String trace = "";
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int i = 0;
        for (StackTraceElement element : stackTraceElements) {
            i++;
            if (i > 1000) {
                Debug.printInfo("trace caller terminated because looping more than 1000 times");
                break;
            }
            if (checkTraceFolder(element.getClassName()) ) {
                if (element.getClassName().equals("com.sains.framework.base.BaseDAOImpl")){
                    if (element.getMethodName().equals("traceCaller") ||
                        element.getMethodName().equals("getSession") ||
                        element.getMethodName().equals("closeSession") ) {
                        continue;
                    }
                }
                trace = element.getClassName() + "." + element.getMethodName()+ "(),LINE:"+ element.getLineNumber() +":: " + trace;
            }
            if (element.getClassName().endsWith("AuthorizationInterceptor")) break;
        }
        try {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            return request.getAttribute("theRequest_")+">>"+trace;
        } catch (Exception e) {
            return trace;
        }
    }

    private Boolean checkTraceFolder(String className) {
        for (String path : SystemConstants.TRACE_FOLDER) {
            if (className.startsWith(path)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /*
     * from : Model from Form (Data Entry)
     */
//    public T setUpdateProperties(T from, Session session) throws Exception {
//        T dbObject = null;  // Record from DB
//        try {
//            Object getterObject;  //
//            Method m = from.getClass().getMethod("getUpdatableColumns");
//            Object returnObj = m.invoke(from);
//            if (returnObj != null) {
//                // Get Primary Key (Always the First Field from UpdatableColumns)
//                m = from.getClass().getMethod("get" + WordUtils.capitalize(((String[]) returnObj)[0]));
//                String id = (String) m.invoke(from);
//                // Retrieve Record from DB using Primary Key
//                dbObject = (T) session.get(from.getClass(), id);
//                if (dbObject == null) {
//                    throw new ValidationException(); // Own Exception
//                }				//dbObject = listById(id, from);
//
//                // Check Version - ThoTH @ 7-Jul-2011
//                try {
//                    m = from.getClass().getMethod("getHibernate_version");
//                    getterObject = m.invoke(from);
//                    String myVersion = getterObject.toString();
////                    System.out.println("myVersion:"+myVersion);
//
//                    m = dbObject.getClass().getMethod("getHibernate_version");
//                    getterObject = m.invoke(dbObject);
//                    String dbVersion = getterObject.toString();
////                    System.out.println("dbVersion:"+dbVersion);
//
//                    if (! dbVersion.equals(myVersion)) {
////                        System.out.println("not same");
//                        throw new CustomBaseException(rowchangedbetweenretrieveupdate);
//                    }
//
//                } catch (BaseException be) {
//                    throw be;
//                } catch (Exception x) {
//                    // do nothing- To Handle method not found
//                }
//                // Check Version - END
//
//                // Loop UpdatableColumns Starting with Column #2
//                for (int idx = 1; idx < ((String[]) returnObj).length; idx++) {
//                    //System.out.println("((String[])returnObj)[idx] = " + ((String[])returnObj)[idx]);
//                    // Get Data from Form (Data Entry)
//                    m = from.getClass().getMethod("get" + WordUtils.capitalize(((String[]) returnObj)[idx]));
//                    getterObject = m.invoke(from);
//                    if (getterObject != null) {
//                        m = dbObject.getClass().getMethod("set" + WordUtils.capitalize(((String[]) returnObj)[idx]), getterObject.getClass());
//                        m.invoke(dbObject, getterObject);
//                    } else {
//                        Class objClass = null;
//                        if (m.getGenericReturnType().toString().equals("class java.lang.String")) {
//                            objClass = String.class;
//                            if ( ((String)returnObj).equalsIgnoreCase("Updated_by") || ((String)returnObj).equalsIgnoreCase("Updated_date")) {
//                                continue;
//                            }
//                        } else if (m.getGenericReturnType().toString().equals("class java.lang.Double")) {
//                            objClass = Double.class;
//                        } else if (m.getGenericReturnType().toString().equals("class java.lang.Long")) {
//                            objClass = Long.class;
//                        } else if (m.getGenericReturnType().toString().equals("class java.lang.Integer")) {
//                            objClass = Integer.class;
//                        } else if (m.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
//                            objClass = Timestamp.class;
//                        } else if (m.getGenericReturnType().toString().equals("class java.lang.Byte")) {
//                            objClass = Byte.class;
//                        }
//                        m = dbObject.getClass().getMethod("set" + WordUtils.capitalize(((String[]) returnObj)[idx]), objClass);
//                        m.invoke(dbObject, (Object) null);
//                    }
//                }
//
//                // Hardcode updated_by and updated_date column here,
//                // so they can be excluded in the Updatable Column in Model Class
//                m = from.getClass().getMethod("getUpdated_by");
//                getterObject = m.invoke(from);
////                if (getterObject == null) System.out.println("a");
////                if (getterObject.getClass() == null) System.out.println("b");
//                m = dbObject.getClass().getMethod("setUpdated_by", getterObject.getClass());
//                m.invoke(dbObject, getterObject);
//
//                m = from.getClass().getMethod("getUpdated_date");
//                getterObject = m.invoke(from);
//                m = dbObject.getClass().getMethod("setUpdated_date", getterObject.getClass());
//                m.invoke(dbObject, getterObject);
//            }
//        } catch (ValidationException vex) {
//
//            dbObject = from;
////            vnew LogFunction().logError(this.getClass(), "", ex);
//        } catch (NoSuchMethodException noMethod) {
//            new LogFunction().logError(this.getClass(), "", noMethod);
//            //do nothing, no defined updatable columns.
//        } catch (BaseException be) {
//            throw be;
//        } catch (Exception ex) {
//            new LogFunction().logError(this.getClass(), "", ex);
//        }
//        return dbObject;
//    }

    public T setUpdateProperties(T from, Session session) throws Exception{
        return (T) setObjectUpdateProperties(from, session);
    }

    public Object setObjectUpdateProperties(Object from, Session session) throws Exception {
        Object dbObject = null;  // Record from DB
        Object dbGetterObject = null;
        Boolean checkModified = Boolean.FALSE;
        Boolean checkAging = Boolean.FALSE;
        String columnName = null;
        String modelPK=((ModelBase)from).model_PK(from.getClass());
        
        try {
            try {
                checkModified = ((ModelBase)from).getCheckIsDataModified_();
            } catch (Exception e) {}
            
            checkData(from, session);  // ThoTH @ 11-Apr-2014 :: pass in session to avoid unClose Session  
//            checkData(from);
            Object getterObject;  //
            Method m = from.getClass().getMethod("getUpdatableColumnsByOperation");
            Object returnObj = null;
            returnObj = ModelBase.currentRequestObject(from.getClass().getSimpleName()+((ModelBase)from).get_operation()+"_uc");
            if (returnObj == null) {
                returnObj = m.invoke(from);
            }
            if (returnObj != null) {
                // Get Primary Key (Always the First Field from UpdatableColumns)
                m = from.getClass().getMethod("get" + WordUtils.capitalize(modelPK));
                if (m.getGenericReturnType().toString().equals("class java.lang.String")) {
                    String id = (String) m.invoke(from);
                    // Retrieve Record from DB using Primary Key
                    if (!Validator.isEmpty(id)) {
                        if (id.contains(",")) {
                            throw new CustomBaseException("ID with comma found ["+ id +"], probably there are more than 1 model.ID in the form.");
                        }
                    }
                    
                    //to resolve _$$_javassist_ issue
                    //ahmadni @ 8-Sept-2016
                    String fullClassName = null;
                    if (from.getClass().getSimpleName().indexOf("_$$") >= 0) {
                        fullClassName = from.getClass().getPackage().getName() + "." + from.getClass().getSimpleName().substring(0, from.getClass().getSimpleName().indexOf("_$$"));
                    } else if (from.getClass().getSimpleName().indexOf("$$") >= 0) {
                        fullClassName = from.getClass().getPackage().getName() + "." + from.getClass().getSimpleName().substring(0, from.getClass().getSimpleName().indexOf("$$"));
                    } else {
                        fullClassName = from.getClass().getPackage().getName() + "." + from.getClass().getSimpleName();
                    }
//                    System.out.println("fullcalssname = " + fullClassName);
                    
                    Class<?> cc = Class.forName(fullClassName);
//                    Table table = cc.getAnnotation(Table.class);
//                    System.out.println("table = " + table);
//                    System.out.println("id = " + id);
//                    System.out.println("cc = " + cc);
                    try {
//                        BaseDAO dao = new BaseDAOImpl();
//                        dao.setSession(session);
//                        dbObject = dao.getModelById(id, cc);
                        dbObject = (Object) session.get(cc, id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    dbObject = (Object) session.get(from.getClass(), id); //original commented to cater for _$$_javaassist issue : ahmadni @ 8-Sept-2016
                    
                } else {
                    Integer intId = (Integer) m.invoke(from);
                    String fullClassName = null;
                    if (from.getClass().getSimpleName().indexOf("_$$") >= 0) {
                        fullClassName = from.getClass().getPackage().getName() + "." + from.getClass().getSimpleName().substring(0, from.getClass().getSimpleName().indexOf("_$$"));
                    } else if (from.getClass().getSimpleName().indexOf("$$") >= 0) {
                        fullClassName = from.getClass().getPackage().getName() + "." + from.getClass().getSimpleName().substring(0, from.getClass().getSimpleName().indexOf("$$"));
                    } else {
                        fullClassName = from.getClass().getPackage().getName() + "." + from.getClass().getSimpleName();
                    }
                    Class<?> cc = Class.forName(fullClassName);
                    dbObject = (Object) session.get(cc, intId);
                }
                if (dbObject == null) {
                    throw new ValidationException(); // Own Exception
                }				//dbObject = listById(id, from);

                preUpdate(dbObject, from, session);
                //retrieve the updatableColumns again after preUpdate method is triggered:
                m = from.getClass().getMethod("getUpdatableColumnsByOperation");
                returnObj = ModelBase.currentRequestObject(from.getClass().getSimpleName()+((ModelBase)from).get_operation()+"_uc");
                if (returnObj == null) {
                    returnObj = m.invoke(from);
                }

                // Check Version - ThoTH @ 7-Jul-2011
                try {
                    m = from.getClass().getMethod("getHibernate_version");
                    getterObject = m.invoke(from);
                    String myVersion = getterObject.toString();
//                    System.out.println("myVersion:"+myVersion);

                    m = dbObject.getClass().getMethod("getHibernate_version");
                    getterObject = m.invoke(dbObject);
                    String dbVersion = getterObject.toString();
//                    System.out.println("dbVersion:"+dbVersion);

                    if (! dbVersion.equals(myVersion)) {
//                        System.out.println("not same");
                        throw new CustomBaseException(rowchangedbetweenretrieveupdate);
                    }

                } catch (BaseException be) {
                    throw be;
                } catch (Exception x) {
                    // do nothing- To Handle method not found
                }
                // Check Version - END

                // Loop UpdatableColumns Starting with Column #2
                int startIdx = 0; //to support updatable not starting with PK
                if ( ((String[])returnObj)[0].equalsIgnoreCase(((ModelBase)from).model_PK(from.getClass())) ) {
                    startIdx = 1;
                }
                for (int idx = startIdx; idx < ((String[]) returnObj).length; idx++) {
                    //System.out.println("((String[])returnObj)[idx] = " + ((String[])returnObj)[idx]);
                    // Get Data from Form (Data Entry)
                    columnName = WordUtils.capitalize(((String[]) returnObj)[idx]);
                    m = from.getClass().getMethod("get" + columnName);
                    getterObject = m.invoke(from);

//                    if (checkModified) {
                    m = dbObject.getClass().getMethod("get" + columnName);
                    dbGetterObject = m.invoke(dbObject);
//                    }
                    if (getterObject != null) {                        
                        if (checkModified) {
                            if (!getterObject.equals(dbGetterObject)) {
                                try {
                                    ((ModelBase)dbObject).setIsDataModified_(Boolean.TRUE);
                                    checkModified = Boolean.FALSE;
                                } catch (Exception e) {}
                            }
                        }
                        m = dbObject.getClass().getMethod("set" + columnName, getterObject.getClass());
                        m.invoke(dbObject, getterObject);
                    } else {
                        Class objClass = null;
                        if (m.getGenericReturnType().toString().equals("class java.lang.String")) {
                            objClass = String.class;
                        } else if (m.getGenericReturnType().toString().equals("class java.lang.Double")) {
                            objClass = Double.class;
                        } else if (m.getGenericReturnType().toString().equals("class java.lang.Long")) {
                            objClass = Long.class;
                        } else if (m.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                            objClass = Integer.class;
                        } else if (m.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
                            objClass = Timestamp.class;
                        } else if (m.getGenericReturnType().toString().equals("class java.lang.Byte")) {
                            objClass = Byte.class;
                        } else if (m.getGenericReturnType().toString().equals("class java.lang.Character")) {
                            objClass = Character.class;
                        } else if (m.getGenericReturnType().toString().equals("class java.util.Date")) {
                            objClass = Date.class;
                        } else if (m.getGenericReturnType().toString().equals("class java.sql.Date")) {
                            objClass = java.sql.Date.class;
                        } else if (m.getGenericReturnType().toString().equals("class java.math.BigDecimal")) {
                            objClass = BigDecimal.class;
                        } else if (m.getGenericReturnType().toString().equals("class java.io.File")) {
                            objClass = File.class;
                        }
                        
                        if (checkModified) {
                            if (dbGetterObject != null) {
                                try {
                                    ((ModelBase)dbObject).setIsDataModified_(Boolean.TRUE);
                                    checkModified = Boolean.FALSE;
                                } catch (Exception e) {}
                            }
                        }
                        m = dbObject.getClass().getMethod("set" + columnName, objClass);
                        m.invoke(dbObject, (Object) null);
                    }
                }

                // Hardcode updated_by and updated_date column here,
                // so they can be excluded in the Updatable Column in Model Class
                m = from.getClass().getMethod("getUpdated_by");
                getterObject = m.invoke(from);
//                if (getterObject == null) System.out.println("a");
//                if (getterObject.getClass() == null) System.out.println("b");
                if (getterObject != null) {
                    m = dbObject.getClass().getMethod("setUpdated_by", getterObject.getClass());
                    m.invoke(dbObject, getterObject);
                }

                m = from.getClass().getMethod("getUpdated_date");
                getterObject = m.invoke(from);
                if (getterObject != null) {
                    m = dbObject.getClass().getMethod("setUpdated_date", getterObject.getClass());
                    m.invoke(dbObject, getterObject);
                }
            }
        } catch (ValidationException vex) {

            dbObject = from;
//            vnew LogFunction().logError(this.getClass(), "", ex);
        } catch (NoSuchMethodException noMethod) {
            new LogFunction().logError(this.getClass(), "", noMethod);
            //do nothing, no defined updatable columns.
        } catch (BaseException be) {
            throw be;
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
            throw ex;
        }
        return dbObject;
    }

    public List<T> list_order(Map param, Class modelClass, boolean advanceSearch, String orderBy) {
        List<T> objs = null;

        orderBy = " " + orderBy;

        //param.put("us_user_id", "thensw");
        //param.put("us_id", new Integer(10));
        List paramValue = new ArrayList();
        try {
            String sql = "select _self from " + modelClass.getName() + " _self ";
            String join = "";

            Query query;
            if (param != null) {
                String condition = "";
                if (advanceSearch) {
                    for (Object obj : param.keySet()) {
                        if (obj.toString().equals("customSQL")) {
                            condition += (condition.length() <= 0) ? "where " + param.get(obj) : " and " + param.get(obj);
                            continue;
                        }
                        if (obj.toString().startsWith(SystemConstants.QUERY.JOIN)){
                            join += param.get(obj) + " ";
                            continue;
                        }
                        if (param.get(obj).toString().indexOf("NESTEDSQL") >= 0) {
                            String strNestedSql = (String) obj + " " + param.get(obj).toString().substring(9);

                            condition += (condition.length() <= 0) ? "where " + strNestedSql : " and " + strNestedSql;
                            continue;
                        }

                        if (condition.length() <= 0) {
                            condition += "where " + criteriaConverter.strCriteria("", (String) obj, param.get(obj).toString().toLowerCase());
                        } else {
                            condition += " " + criteriaConverter.strCriteria("and", (String) obj, param.get(obj).toString().toLowerCase());
                        }
                        paramValue.add(param.get(obj));
                    }
                    sql += join + condition;
//                    System.out.println("condition1="+condition);
//                    System.out.println("sql = " + sql + orderBy);
                    query = getSession().createQuery(sql + orderBy);
                } else {
                    for (Object obj : param.keySet()) {
                        if (obj.toString().equals("customSQL")) {
                            condition += (condition.length() <= 0) ? "where " + param.get(obj) : " and " + param.get(obj);
                            continue;
                        }
                        if (obj.toString().startsWith(SystemConstants.QUERY.JOIN)){
                            join += param.get(obj) + " ";
                            continue;
                        }
                        if (((String) obj).startsWith(SystemConstants.CRITERIA.NO_CHANGE)) {
                            obj = ((String) obj).substring(noChangeLength);
                            noChange = true;
                        } else {
                            noChange = false;
                        }
                        String criteriaName = (String) obj;
                        if (criteriaName.indexOf(".") >= 0) {
                            criteriaName = criteriaName.replace(".", "_");
                        }
                        if (condition.length() <= 0) {
                            if (noChange) {
                                condition += " where ltrim(rtrim(" + obj + ")) = ltrim(rtrim(:" + criteriaName + "))";
                            } else {
                                condition += " where lower(ltrim(rtrim(" + obj + "))) = lower(ltrim(rtrim(:" + criteriaName + ")))";
                            }
                        } else {
                            if (noChange) {
                                condition += " and ltrim(rtrim(" + obj + ")) = ltrim(rtrim(:" + criteriaName + "))";
                            } else {
                                condition += " and lower(ltrim(rtrim(" + obj + "))) = lower(ltrim(rtrim(:" + criteriaName + ")))";
                            }
                        }
                        paramValue.add(param.get(obj));
                    }
//                    System.out.println("condition2="+condition);
                    sql += join + condition;
//                    System.out.println("List order sql " +sql);
                    query = getSession().createQuery(sql + orderBy);

                    for (Object obj : param.keySet()) {
                        if (obj.toString().equals("customSQL")) {
                            continue;
                        }
                        if (obj.toString().startsWith(SystemConstants.QUERY.JOIN)){
                            continue;
                        }
                        String criteriaName = (String) obj;
                        if (((String) obj).startsWith(SystemConstants.CRITERIA.NO_CHANGE)) {
                            criteriaName = ((String) obj).substring(noChangeLength);
                        }
                        if (criteriaName.indexOf(".") >= 0) {
                            criteriaName = criteriaName.replace(".", "_");
                        }
                        String paramType = param.get(obj).getClass().getSimpleName();
                        if (paramType.equals("String")) {
                            query.setString((String) criteriaName, (String) param.get(obj));
                        } else if (paramType.equals("Double")) {
                            query.setDouble((String) criteriaName, (Double) param.get(obj));
                        } else if (paramType.equals("Long")) {
                            query.setLong((String) criteriaName, (Long) param.get(obj));
                        } else if (paramType.equals("Integer")) {
                            query.setInteger((String) criteriaName, (Integer) param.get(obj));
                        } else if (paramType.equals("Date")) {
                            query.setTimestamp((String) criteriaName, (java.util.Date) param.get(obj));
                        }
                    }
                }
            } else {
                query = getSession().createQuery(sql + orderBy);
            }
            if (isPaging) {
                query.setFirstResult(firstResult);
                query.setMaxResults(maxSize);
            }
            objs = query.list();
        } catch (Exception e) {
            checkException(e);
            new LogFunction().logError(this.getClass(), "", e);
        }
        return objs;
    }

    public List<T> list_order(Map param, Class modelClass, boolean advanceSearch, String pWildSearch, String orderBy) {
        if (pWildSearch.equalsIgnoreCase("y")) {
            criteriaConverter.setWildSearch("Y");
        } else {
            criteriaConverter.setWildSearch("N");
        }
        return list_order(param, modelClass, advanceSearch, orderBy);
    }

    public List<T> list_order(Map param, Class modelClass, String orderBy) {
        return list_order(param, modelClass, false, orderBy);
    }

    public T getModelByCode(String codeName, String code, T model) {
        String[] temp_codeName;
        String[] temp_code;

        temp_codeName = codeName.split(",");
        temp_code = code.split(",");

        if (temp_codeName.length != temp_code.length)   return null;

        Map param = new HashMap();
        for (int i = 0; i < temp_codeName.length; i++) {

            if (!temp_codeName[i].startsWith(SystemConstants.CRITERIA.NO_CHANGE)) {
                temp_codeName[i] = SystemConstants.CRITERIA.NO_CHANGE + temp_codeName[i];
            }
            param.put(temp_codeName[i], temp_code[i]);
        }

        List<T> list = list(param, model.getClass(), false);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }

        return null;

        //-----------original code--------------
//        Map param = new HashMap();
//        if (!codeName.startsWith(SystemConstants.CRITERIA.NO_CHANGE)) {
//            codeName = SystemConstants.CRITERIA.NO_CHANGE + codeName;
//        }
//        param.put(codeName, code);
//        List<T> list = list(param, model, false);
//        if (list != null && list.size() > 0) {
//            return list.get(0);
//        }
//        return null;
        //----------- end original code --------------
    }

    // Created by ThoTH @ 15-Jul-2011
    @Override
    public Object getObjectByCode(String codeName, String code, Object model) {
//        System.out.println("GOBC here");
        String[] temp_codeName;
        String[] temp_code;

        temp_codeName = codeName.split(",");
        temp_code = code.split(",");

        if (temp_codeName.length != temp_code.length)   return null;

        Map param = new HashMap();
        for (int i = 0; i < temp_codeName.length; i++) {

            if (!temp_codeName[i].startsWith(SystemConstants.CRITERIA.NO_CHANGE)) {
                temp_codeName[i] = SystemConstants.CRITERIA.NO_CHANGE + temp_codeName[i];
            }
            param.put(temp_codeName[i], temp_code[i]);
        }

        return getSingleObject(param, model, Boolean.FALSE);
    }


    public boolean isDuplicate(String fieldName, T model) {
        WordUtils.capitalize(fieldName);
        Method m = null;
        Object returnObj = null;
        T dbModel = null;
        try {
            m = model.getClass().getMethod("get" + WordUtils.capitalize(fieldName));
            returnObj = m.invoke(model);
//            fieldData = returnObj.toString();
            String sql = "select _self from " + model.getClass().getSimpleName() + " _self where " + fieldName + " = :fieldData";
            dbModel = (T) getSession().createQuery(sql).setParameter("fieldData", returnObj).uniqueResult();
//            dbModel = getModelByCode(fieldName, fieldData, model);
            if (dbModel != null) {
                m = model.getClass().getMethod("getID");
                returnObj = m.invoke(model);
                if (returnObj == null)  return true;

                String modelID = returnObj.toString();

                if (modelID.equals("")) {
                    return true;
                } else { //existing record.
                    m = dbModel.getClass().getMethod("getID");
                    returnObj = m.invoke(dbModel);
                    String dbID = returnObj.toString();
                    if (!dbID.equals(modelID)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            checkException(e);
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
            returnObj = null;
            m = null;
            dbModel = null;
        }

        return false;
    }

    public void autoDuplicationCheck(T model, String insertOrUpdate) throws Exception {
        try {
            ModelBase mb = (ModelBase) model;
            if (mb.getManualDuplicationCheck()) {
//                mb.manualDuplicationCheck(session, model);
                mb.manualDuplicationCheck(getSession(), model);// serene @ 12/7/2018 ::unable get session
                return;
            }
            if (mb.autoValidation()) {
                String duplicateFields = null;
                String duplicateFieldsRange = null;
                if (insertOrUpdate.equals("insert")) {
                    duplicateFields = mb.insertNoDuplicate();
                    duplicateFieldsRange = mb.insertNoDuplicateRange();
                } else { // update
                    if (Validator.isEmpty(mb.updateNoDuplicate())) { //use insertNoDuplicate if updateNotDuplicate is empty
                        duplicateFields = mb.insertNoDuplicate();
                    } else {
                        duplicateFields = mb.updateNoDuplicate();
                    }
                    if (Validator.isEmpty(mb.updateNoDuplicateRange())) { //use insertNoDuplicateRange if updateNotDuplicateRange is empty
                        duplicateFieldsRange = mb.insertNoDuplicateRange();
                    } else {
                        duplicateFieldsRange = mb.updateNoDuplicateRange();
                    }
                }
                String[] duplicateFieldArr = null;
                if (!Validator.isEmpty(duplicateFields)) {
                    for (String field : duplicateFields.split(",")) {
                        duplicateFieldArr = field.trim().split(";");
                        if (isDuplicate(duplicateFieldArr[0].trim(), model)) {
                            throw new DuplicatedFieldException(duplicateFieldArr[1].trim(), getMethodValueFromObject(model, duplicateFieldArr[0].trim()));
                        }
                    }
                }
                if (!Validator.isEmpty(duplicateFieldsRange)) {
                    List list;
                    int count = 0;
//                    System.out.println("duplicateFieldsRange" + duplicateFieldsRange);
                    for (String fieldsRange : duplicateFieldsRange.split(";")) {
                        list = checkDuplicateFields(fieldsRange.split(","), model, true);
                        if (list != null && list.size() > 0) {
//                            System.out.println("list = " + list);
                            for (Object obj : list) {
                                throw new DuplicationKeyException(mb.duplicateFieldsRangeKeyDescription().split(";")[count].trim());
//                                if (!((ModelBase) obj).getID().equals(mb.getID()))  {
//                                }
                            }
                        }
                        count++;
                    }

//                if (!Validator.isEmpty(duplicateFieldsRange)) {
//                    System.out.println("duplicateFieldsRange" + duplicateFieldsRange);
//                    List list = checkDuplicateFields(duplicateFieldsRange.split(","), model, true);
//                    if (list != null && list.size() > 0) {
//                        System.out.println("list = " + list);
//                        throw new DuplicationKeyException(mb.duplicateFieldsRangeKeyDescription());
//                    }
//                    for (String field : duplicateFields.split(",")){
//                        duplicateFieldArr = field.trim().split(";");
//                        if (isDuplicate(duplicateFieldArr[0].trim(), model)){
//                            throw new DuplicatedFieldException(duplicateFieldArr[1].trim(), getMethodValueFromObject(model, duplicateFieldArr[0].trim()));
//                        }
//                    }

                }
                if (mb.validateRecursive()) {
                    if (!Validator.isEmpty(mb.noRecursive())) {
                        String[] recursiveCheckArr = mb.noRecursive().split(",");
                        if (recursiveCheckArr[0].indexOf(":") > 0) { //only do checking base on specific value.
                            String startCheckValue = getMethodValueFromObject(model, "get" + WordUtils.capitalize(recursiveCheckArr[0].split(":")[0].trim()));
                            if (startCheckValue.equalsIgnoreCase(recursiveCheckArr[0].split(":")[1].trim())) {
                                if (isRecursive(recursiveCheckArr[2].trim(), recursiveCheckArr[1].trim(), model)) {
                                    //addActionError(getText("errors.recursiveRelation", new String[]{getText(recursiveCheckArr[3].trim())}));
                                    throw new RecursiveRelationException(recursiveCheckArr[3].trim());
                                }
                            }
                        } else { //do checking if no checking value is provided.
                            if (isRecursive(recursiveCheckArr[2].trim(), recursiveCheckArr[1].trim(), model)) {
                                //addActionError(getText("errors.recursiveRelation", new String[]{getText(recursiveCheckArr[3].trim())}));
                                throw new RecursiveRelationException(recursiveCheckArr[3].trim());
                            }
                        }
                    }
                }
            }
        } catch (BaseException be) {
            throw be;
        } catch (Exception e) {
            throw e;
        }
    }
    public List<String> checkDuplicateFields(String[] fieldNames, T model, Boolean uniqueByColumns) throws Exception {
        String condition = "";
        String sql = "select _self from " + model.getClass().getSimpleName() + " _self ";
        String joinStr = "";
        String tempCondition = "";
        String fieldData = "";
        Method m = null;
        Object returnObj = null;

        List<String> fieldDataList = new ArrayList();
        List fieldFoundList = new ArrayList();
        String joinOperator = "or";
        if (uniqueByColumns) {
            joinOperator = "and";
        }
        Boolean isDate = Boolean.FALSE;
        try {
            for (String fieldName : fieldNames) {
                isDate = Boolean.FALSE;
                if (fieldName.startsWith(SystemConstants.HQL.JOIN)){
                    if (fieldName.indexOf("|") < 0) { //no compare data
                        joinStr = fieldName.substring(SystemConstants.HQL.JOIN.length());
                        continue;
                    }
                    joinStr = fieldName.substring(SystemConstants.HQL.JOIN.length(), fieldName.indexOf("|")-1);
                    fieldName = fieldName.substring(fieldName.indexOf("|")+1);
                }
                if (fieldName.startsWith(SystemConstants.HQL.DEFAULT_CONDITION)){
                    tempCondition = fieldName.substring(SystemConstants.HQL.DEFAULT_CONDITION.length());
                    if (condition.length() <= 0) {
                        condition = "where " + tempCondition;
                    } else {
                        condition += " and " + tempCondition;
                    }
                    continue;
                }
                if (fieldName.indexOf("_date_") >= 0) {
                    isDate = Boolean.TRUE;
                }
                returnObj = getMethodObjectFromObject(model, fieldName);
                if (isDate) {
                    fieldName = fieldName.substring(fieldName.indexOf("_date_") + 6);
                }
//                System.out.println("fieldName = " + fieldName);
                //m = model.getClass().getMethod("get" + WordUtils.capitalize(fieldName.trim()));
                //returnObj = m.invoke(model);

                if (returnObj == null) {
                    returnObj = "";
                }
                if (returnObj != null) {
//                    System.out.println("returnObj = " + returnObj);
                    fieldData = criteriaConverter.convertSQLChar(returnObj.toString());
                    if (!Validator.isEmpty(fieldData)) {
                        fieldDataList.add(fieldData);
//                        System.out.println("fieldData = " + fieldData);
                        if (condition.length() <= 0) {
                            if (isDate) {
                                condition += "where " + fieldName.trim() + " = '" + fieldData + "'";
                            } else {
                                condition += "where ltrim(rtrim(" + fieldName.trim() + ")) = '" + fieldData + "'";
                            }
                        } else {
                            if (isDate) {
                                condition += " " + joinOperator + " " + fieldName.trim() + " = '" + fieldData + "'";
                            } else {
                                condition += " " + joinOperator + " ltrim(rtrim(" + fieldName.trim() + ")) = '" + fieldData + "'";
                            }
                        }
                    } else {
                        //fieldDataList.add("is null");
                        fieldDataList.add(null);

                        if (condition.length() <= 0) {
                            condition += "where (" + fieldName.trim() + " is null or " + fieldName.trim() + " = '')";
                        } else {
                            condition += " " + joinOperator + " (" + fieldName.trim() + " is null or " + fieldName.trim() + " = '')";
                        }
                    }
                } else {
                    fieldDataList.add(null);
                }
            }

            sql += joinStr + " " + condition;
//            System.out.println("sql in checkDuplicateFields "+ sql);
            Query query = getSession().createQuery(sql);
            List resultList = query.list();
//            if (uniqueByColumns){
//                if (resultList.size() > 0){
//                    System.out.println("uniqueByColumns  return fieldFoundList");
//                    fieldFoundList.add("found");
//                    return fieldFoundList;
//                }
//            }
            m = model.getClass().getMethod(SystemConstants.METHOD.GET_ID);
            returnObj = m.invoke(model);
            String modelID = "";
            if (returnObj != null) {
                modelID = returnObj.toString();
            }
            int idx = 0;
            for (String fieldName : fieldNames) {
                String foundName = "";
                if (fieldName.startsWith(SystemConstants.HQL.JOIN)){
                    //joinStr = fieldName.substring(SystemConstants.HQL_JOIN.JOIN.length(), fieldName.indexOf("|")-1);
                    if (fieldName.indexOf("|") < 0) { //no compare data
                        continue;
                    }
                    fieldName = fieldName.substring(fieldName.indexOf("|")+1);
                }
                if (fieldName.startsWith(SystemConstants.HQL.DEFAULT_CONDITION)){
                    tempCondition = fieldName.substring(SystemConstants.HQL.DEFAULT_CONDITION.length());
                    if (condition.length() <= 0) {
                        condition = "where " + tempCondition;
                    } else {
                        condition += " and " + tempCondition;
                    }
                    continue;
                }
                for (Object dbModel : resultList) {
                    //m = dbModel.getClass().getMethod("get" + WordUtils.capitalize(fieldName.trim()));
                    //returnObj = m.invoke(dbModel);


                    returnObj = getMethodObjectFromObject(model, fieldName);
                    String dbFieldData = null;
                    if (returnObj != null) {
                        dbFieldData = returnObj.toString();
                    }

                    if (modelID.equals("") || modelID.equals("0")) { // new record
                        Debug.printFrameworkDebug("duplicate new");
                        if (fieldDataList.get(idx) == null) {
                            idx++;
                            if (dbFieldData == null) {
                                foundName = fieldName.trim();
                                break;
                            }
                        } else if (fieldDataList.get(idx++).equalsIgnoreCase(dbFieldData)) {
                            foundName = fieldName.trim();
                            break;
                        }
                    } else { //existing record.
//                        System.out.println("duplicate existing");
                        m = dbModel.getClass().getMethod(SystemConstants.METHOD.GET_ID);
                        returnObj = m.invoke(dbModel);
                        String dbID = returnObj.toString();
                        if (!dbID.equals(modelID)) {
                            if (fieldDataList.get(idx) != null && fieldDataList.get(idx).equalsIgnoreCase(dbFieldData)) {
                                foundName = fieldName.trim();
                                idx++;
                                break;
                            } else {
                                if (dbFieldData == null) {
                                    dbFieldData = "";
                                }
                                if (Validator.isEmpty(fieldDataList.get(idx)) && Validator.isEmpty(dbFieldData)) {
                                    foundName = fieldName.trim();
                                    idx++;
                                    break;
                                }
                            }
                        }
                    }
                }
//                if (uniqueByColumns && !foundName.equals("")){
//                    foundName = ""+idx;
//                }
                if (!foundName.equals("")) {
                    fieldFoundList.add(foundName);
                }
                if (uniqueByColumns && !foundName.equals("")) {
                    break;
                }
            }
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
            throw e;
        } finally {
            returnObj = null;
            m = null;
        }

        return fieldFoundList;
    }
//    public List<String> checkDuplicateFields(String[] fieldNames, T model, Boolean uniqueByColumns) {
//        String condition = "";
//        String sql = "select _self from " + model.getClass().getSimpleName() + " _self ";
//        String fieldData = "";
//        Method m = null;
//        Object returnObj = null;
//        List<String> fieldDataList = new ArrayList();
//        List fieldFoundList = new ArrayList();
//        String joinOperator = "or";
//        if (uniqueByColumns) {
//            joinOperator = "and";
//        }
//        try {
//            for (String fieldName : fieldNames) {
//                m = model.getClass().getMethod("get" + WordUtils.capitalize(fieldName.trim()));
//                returnObj = m.invoke(model);
//                if (returnObj != null) {
//                    fieldData = returnObj.toString();
//                    if (!Validator.isEmpty(fieldData)) {
//                        fieldDataList.add(fieldData);
//
//                        if (condition.length() <= 0) {
//                            condition += "where lower(trim(" + fieldName.trim() + ")) = '" + fieldData.toLowerCase() + "'";
//                        } else {
//                            condition += " " + joinOperator + " lower(trim(" + fieldName.trim() + ")) = '" + fieldData.toLowerCase() + "'";
//                        }
//                    } else {
//                        fieldDataList.add(null);
//                    }
//                } else {
//                    fieldDataList.add(null);
//                }
//            }
//
//            sql += condition;
////            System.out.println("duplication check sql = "+ sql);
//            Query query = getSession().createQuery(sql);
//            List resultList = query.list();
//            //disable by wongkk, not working
////            if (uniqueByColumns) {
////                System.out.println("uniqueByColumns");
////                if (resultList.size() > 0) {
////                    fieldFoundList.add("found");
////                    return fieldFoundList;
////                }
////            }
//            m = model.getClass().getMethod("getID");
//            returnObj = m.invoke(model);
//            if (returnObj == null) returnObj = "";
//            String modelID = returnObj.toString();
//            int idx = 0;
//            for (String fieldName : fieldNames) {
//                String foundName = "";
//                for (Object dbModel : resultList) {
//                    m = dbModel.getClass().getMethod("get" + WordUtils.capitalize(fieldName.trim()));
//                    returnObj = m.invoke(dbModel);
//                    String dbFieldData = null;
//                    if (returnObj != null) {
//                        dbFieldData = returnObj.toString();
//                    }
//
//                    if (modelID.equals("")) {
//                        if (fieldDataList.get(idx) == null) {
//                            idx++;
//                            if (dbFieldData == null) {
//                                foundName = fieldName.trim();
//                                break;
//                            }
//                        } else if (fieldDataList.get(idx++).equalsIgnoreCase(dbFieldData)) {
//                            foundName = fieldName.trim();
//                            break;
//                        }
//                    } else { //existing record.
//                        m = dbModel.getClass().getMethod("getID");
//                        returnObj = m.invoke(dbModel);
//                        String dbID = returnObj.toString();
//                        if (!dbID.equals(modelID)) {
//                            if (fieldDataList.get(idx++).equalsIgnoreCase(dbFieldData)) {
//                                foundName = fieldName.trim();
//                                break;
//                            }
//                        }
//                    }
//                }
//                if (!foundName.equals("")) {
//                    fieldFoundList.add(foundName);
//                }
//                if (uniqueByColumns && !foundName.equals("")) {
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            checkException(e);
//            new LogFunction().logError(this.getClass(), "", e);
//        } finally {
//            returnObj = null;
//            m = null;
//        }
//
//        return fieldFoundList;
//    }

    public boolean isRecursive(String parentIDField, String parentModelMethod, T model) {
        Method m = null;
        Object returnObj = null;
        T parentModel = null;
        T tempParentModel = null;
        try {
            String modelId = getMethodValueFromObject(model, "getID");
            m = model.getClass().getMethod("get" + WordUtils.capitalize(parentIDField));
            returnObj = m.invoke(model);
            String parentIdValue = null;
            if (returnObj != null) {
                parentIdValue = returnObj.toString();
            }
            parentModel = getModelById(parentIdValue, model.getClass());
            if (parentModel != null) {
                if (getMethodValueFromObject(parentModel, "getID").equals(modelId)) {
                    return true;
                }
                while (parentModel != null) {
                    m = parentModel.getClass().getMethod(parentModelMethod);
                    tempParentModel = (T) m.invoke(parentModel);
                    if (tempParentModel != null) {
                        if (getMethodValueFromObject(tempParentModel, "getID") != null) {
                            if (getMethodValueFromObject(tempParentModel, "getID").equals(modelId)) {
                                return true;
                            }
                        }
                    }

                    parentModel = tempParentModel;
                }
            }
        } catch (Exception e) {
            checkException(e);
            e.printStackTrace();
        } finally {
            m = null;
            returnObj = null;
            parentModel = null;
        }

        return false;
    }

    private String getMethodValueFromObject(Object model, String method) throws Exception {
        if (!method.startsWith("get")) {
            method = "get" + WordUtils.capitalize(method);
        }
        Method m = model.getClass().getMethod(method);
        Object returnObj = m.invoke(model);
        if (returnObj != null) {
            return returnObj.toString();
        }
        return null;
    }

    // ThoTH @ 22-Mar-2011
    // Move from CommonFunction to solve Syn Problem
//    public synchronized String getRunningSeq2(String strType, String strYear, String prefix, String postfix, Boolean appendZero, String strFormat) throws Exception {
//        DecimalFormat numDigits = new DecimalFormat(strFormat);
//        //String strNewSeq = "";
//        Integer liSeq = 0;
//        try {
//            SeqModel seq = (SeqModel) getObjectById(strType + strYear, new SeqModel());
//
//            if (seq == null) {
//                //System.out.println("NewSeq");
//                liSeq = 1;
//                seq = new SeqModel();
//                seq.setSeq_id(strType + strYear);
//                seq.setSeq_last_no(liSeq);
//
//                getSession().save(seq);
//
////                seqDAO.insert(seq);
//            } else {
//                //System.out.println("Before seq = " + seq.getSeq_last_no() );
//                liSeq = seq.getSeq_last_no() + 1;
//                seq.setSeq_last_no(liSeq);
//
//                getSession().update(seq);
//                //try{
//                //seqDAO.update(seq);
////                seqDAO.directUpdate(seq);
//                //}catch(Exception ex){
//                //}
//            }
//            // System.out.println("After seq = " + llSeq );
//            //aeDAO.update(ae);
//        } catch (Exception e) {
//            throw e;
//        } finally {
//        }
//        if (appendZero) {
////            System.out.println("Append"+prefix+sixDigits.format(llSeq)+postfix);
//            return prefix + numDigits.format(liSeq) + postfix;
//        } else {
////            System.out.println("NO Append"+prefix+llSeq+postfix);
//            return prefix + liSeq + postfix;
//        }
//    }

//    /**
//     * Update the modifiedBy and dateModified for audit purpose, and by default, call session.delete(model);
//     * If want to manually delete the model, use auditDelete(model, false);
//     * @param model
//     * @param strUpdatedBy
//     * @throws Exception
//     */
//    protected void auditDelete(Object model, String strUpdatedBy) throws Exception {
//        auditDelete(model, strUpdatedBy, true);
//    }
//
//    protected void auditDelete(Object model, String strUpdatedBy, boolean deleteModel) throws Exception {
//        // For Audit
//        Method m = model.getClass().getMethod("setUpdated_by", String.class);
//        m.invoke(model, strUpdatedBy);
//
//        session.update(model);
//        session.flush();
//        if (deleteModel) {
//            session.delete(model);
//        }
//        // For Audit - End
//    }

    public void resetSessionCount() {
        openCount  = 0;
        closeCount = 0;
    }

    public void setIsPaging(Boolean isPaging) {
        this.isPaging = isPaging;
    }

    public Integer getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }


    /**
     *
     * Must only use for Select count(*) From xxxx
     *
     * @param sql
     * @return
     * @throws Exception
     *
     * Created by ThenSW
     * Modified by ThoTH @ 22-Sept-2011
     */
//    @Override
//    public Long sqlCountRecord(String sql) throws Exception {
//        Query query = getSession().createNativeQuery(sql);
//        Object from = query.uniqueResult();
//
//        Method m = from.getClass().getMethod("longValue");
//        Long value = (Long)m.invoke(from);
//
//        return value;
////        return ((BigDecimal) obj).longValue();
//    }
    @Override
    public Long sqlCountRecord(String sql, Map<String, Object> param) throws Exception {
        NativeQuery query = getSession().createNativeQuery(sql);
        if (param != null) {
            for (String key : param.keySet()) {
                if (param.get(key) instanceof Object[]) {
                    query.setParameterList(key, (Object[])param.get(key));
                } else if (param.get(key) instanceof List) {
                    query.setParameterList(key, (List)param.get(key));
                } else {
                    query.setParameter(key, param.get(key));
                }
            }
        }
        Object from = query.uniqueResult();

        Method m = from.getClass().getMethod("longValue");
        Long value = (Long)m.invoke(from);

        return value;
//        return ((BigDecimal) obj).longValue();
    }
    
    // ThoTH @ 15-Aug-2014 :: Copied from CommonFunction
    @Override
    public List getListFromSql(String strSql, Map param) throws Exception {
        List listResult = new ArrayList();

        NativeQuery query = null;
        try {
            query = getSession().createNativeQuery(strSql);
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            if (param != null) {  // ThoTH @ 21-Mar-2014
                for (Object key : param.keySet()) {
                    query.setParameter((String)key, param.get(key));
                }
            }
            listResult = query.list();
        } catch (Exception e) {
//            new LogFunction().logError(this.getClass(), "", e);
            throw e;
        }
        return listResult;
    }
    
    @Override
    public Integer executeSql(String strSql) throws Exception {
        NativeQuery query = getSession().createNativeQuery(strSql);
        return query.executeUpdate();
    }
    
    // ThoTH @ 15-Aug-2014 :: Copied from CommonFunction
    @Override
    public String getSingleValue(String strSql) throws Exception {
        NativeQuery query = getSession().createNativeQuery(strSql);
        Object obj = query.uniqueResult();
        if (obj == null) return "";
        return obj.toString();
    }
    
    // ThoTH @ 16-Nov-2015
    public Integer getDateDiff(Timestamp start, Timestamp end) throws Exception {
        String sql = "select datediff(day, convert(date,'"+start+"',120),convert(date,'"+end+"',120)) +1";
        NativeQuery query = getSession().createNativeQuery(sql);
        Object obj = query.uniqueResult();
        if (obj == null) return null;
        return (Integer)obj;
    }
    
    // ThoTH @ 11-Nov-2014
    public String getSingleValue(String strSql, Map<Integer,String> param) throws Exception {
        NativeQuery query = getSession().createNativeQuery(strSql);
        if (param != null) {  
            for (int i = 0;  i < param.size(); i++) {
                query.setString(i, param.get(i).toString());
            }
        }
        Object obj = query.uniqueResult();
        if (obj == null) return "";
        return obj.toString();
    }

    public static String sessionOpenCloseCount(){
        return ",Opened Count="+openCount+",Closed Count="+closeCount;
    }

    private void processNextInsert(Object currentObj, String level, Integer item, String xml, BaseDAOImpl checkingDAO) throws Exception{
        String openPattern = "<" + level + item + ">";
        String closePattern = "</" + level + item + ">";
        String tmpXml = null;
        String strListArg = null;
        Method m = null;
        int idx = -1;
        idx = xml.indexOf(openPattern);
        if (idx <0) return;
        tmpXml = xml.substring(idx+openPattern.length(), xml.indexOf(closePattern));
        if (idx >= 0) {
            if(tmpXml.indexOf("<L"+item+"_") >= 0) {//yes, got sub
                strListArg = tmpXml.substring(0, tmpXml.indexOf("<L"+item+"_"));
                tmpXml = tmpXml.substring(strListArg.length());
            } else { //no sub
                strListArg = tmpXml;
            }
            String[] strArr = strListArg.split(";");
            m = currentObj.getClass().getMethod("get" + WordUtils.capitalize(strArr[0]));
            Object returnObj = m.invoke(currentObj);
            if (returnObj instanceof List) {
                if (returnObj == null || ((List)returnObj).size() <= 0 ) {
                    Debug.printFrameworkDebug("list is null/empty for " + currentObj);
                    return;
                }
                Debug.printFrameworkDebug("it is instance of List :: " + level);
                m = currentObj.getClass().getMethod("getID");
                String id = (String) m.invoke(currentObj);
                for (Object obj : (List)returnObj){
                    defaultAddProperties(obj);
                    m = obj.getClass().getMethod("set" + WordUtils.capitalize(strArr[2]), String.class);
                    m.invoke(obj, id);
                    try {
                        m = obj.getClass().getMethod("set" + WordUtils.capitalize(strArr[1]), currentObj.getClass());
                        m.invoke(obj, currentObj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    checkingDAO.autoDuplicationCheck(obj, ModelBase.INSERT);
                    getSession().save(obj);
                    processNextInsert(obj, level + item+"_", 1, tmpXml, checkingDAO);
                }
//                System.out.println("LIST processing " + currentObj);
                processNextInsert(currentObj, level, item+1, tmpXml, checkingDAO);
            } else {
                if (returnObj == null) {
                    Debug.printFrameworkDebug("returnObj is null for : " + currentObj);
                } else {
                    m = currentObj.getClass().getMethod("getID");
                    String id = (String) m.invoke(currentObj);
                    m = returnObj.getClass().getMethod("set" + WordUtils.capitalize(strArr[2]), String.class);
                    m.invoke(returnObj, id);
                    try {
                        m = returnObj.getClass().getMethod("set" + WordUtils.capitalize(strArr[1]), currentObj.getClass());
                        m.invoke(returnObj, currentObj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    defaultAddProperties(returnObj);
                    checkingDAO.autoDuplicationCheck(returnObj, ModelBase.INSERT);
                    getSession().save(returnObj);
                    processNextInsert(returnObj, level + item+"_", 1, tmpXml, checkingDAO);
                    processNextInsert(currentObj, level, item+1, tmpXml, checkingDAO);
                }
            }
        }
    }

//    private void processNextUpdate(Object currentObj, String level, Integer item, String xml, BaseDAOImpl checkingDAO) throws Exception{
//        String openPattern = "<" + level + item + ">";
//        String closePattern = "</" + level + item + ">";
//        String tmpXml = null;
//        String strListArg = null;
//        Method m = null;
//        int idx = -1;
//        idx = xml.indexOf(openPattern);
//        if (idx <0) return;
//        tmpXml = xml.substring(idx+openPattern.length(), xml.indexOf(closePattern));
//        if (idx >= 0) {
//            if(tmpXml.indexOf("<L"+item+"_") >= 0) {//yes, got sub
//                strListArg = tmpXml.substring(0, tmpXml.indexOf("<L"+item+"_"));
//                tmpXml = tmpXml.substring(strListArg.length());
//            } else { //no sub
//                strListArg = tmpXml;
//            }
//            String[] strArr = strListArg.split(";");
//            m = currentObj.getClass().getMethod("get" + WordUtils.capitalize(strArr[0]));
//            Object returnObj = m.invoke(currentObj);
//            if (returnObj instanceof List) {
//                if (returnObj == null || ((List)returnObj).size() <= 0 ) {
//                    System.out.println("list is null/empty for " + currentObj);
//                    return;
//                }
//                System.out.println("it is instance of List :: " + level);
//
//                //check the deletedItem : START
//                String deletedItem = ((ModelBase)currentObj).getDeletedItem();
//                if (!Validator.isEmpty(deletedItem)){
//                    Object itemModel = null;
//                    String strUpdatedBy = (String) ActionContext.getContext().getSession().get("loginId");  // For Audit
//                    for (String itemId : deletedItem.split(",")){
//                        itemModel = checkingDAO.getModelById(itemId, ((ModelBase)currentObj).getDeletingItemMap().get(strArr[3]));
//                        m = currentObj.getClass().getMethod("getID");
//                        String item_Id = (String) m.invoke(currentObj);
//                        if (!Validator.isEmpty(item_Id)){
//                            checkingDAO.auditDelete(itemModel, strUpdatedBy);
//                        }
//                    }
//                }
//                //check the deletedItem : END
//
//                m = currentObj.getClass().getMethod("getID");
//                String id = (String) m.invoke(currentObj);
//                for (Object obj : (List)returnObj){
//                    m = obj.getClass().getMethod("set" + WordUtils.capitalize(strArr[2]), String.class);
//                    m.invoke(obj, id);
//                    try {
//                        m = obj.getClass().getMethod("set" + WordUtils.capitalize(strArr[1]), currentObj.getClass());
//                        m.invoke(obj, currentObj);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    if (Validator.isEmpty(checkingDAO.getMethodValueFromObject(obj, "ID"))){
//                        defaultAddProperties(obj);
//                        checkingDAO.autoDuplicationCheck(obj, ModelBase.INSERT);
//                        getSession().save(obj);
//                        processNextInsert(obj, level + item+"_", 1, tmpXml, checkingDAO);
//                    } else {
//                        defaultUpdateProperties(obj);
//                        processNextUpdate(obj, level + item+"_", 1, tmpXml, checkingDAO);
//                        checkingDAO.autoDuplicationCheck(obj, ModelBase.UPDATE);
//                        obj = checkingDAO.setUpdateProperties(obj, session);
//                        getSession().update(obj);
//                    }
//
//                }
////                System.out.println("LIST processing " + currentObj);
//                processNextUpdate(currentObj, level, item+1, tmpXml, checkingDAO);
//            } else {
//                if (returnObj == null) {
//                    System.out.println("returnObj is null for : " + currentObj);
//                } else {
//                    m = currentObj.getClass().getMethod("getID");
//                    String id = (String) m.invoke(currentObj);
//                    m = returnObj.getClass().getMethod("set" + WordUtils.capitalize(strArr[2]), String.class);
//                    m.invoke(returnObj, id);
//                    try {
//                        m = returnObj.getClass().getMethod("set" + WordUtils.capitalize(strArr[1]), currentObj.getClass());
//                        m.invoke(returnObj, currentObj);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    defaultUpdateProperties(returnObj);
//                    processNextUpdate(returnObj, level + item+"_", 1, tmpXml, checkingDAO);
//                    processNextUpdate(currentObj, level, item+1, tmpXml, checkingDAO);
//                    checkingDAO.autoDuplicationCheck(returnObj, ModelBase.UPDATE);
//                    returnObj = checkingDAO.setUpdateProperties(returnObj, session);
//                    getSession().update(returnObj);
//                }
//            }
//        }
//    }

    @Override
    public synchronized void frameworkInsert(T model) throws Exception {
        try {
            BaseDAOImpl checkingDAO = new BaseDAOImpl();
            checkingDAO.setSession(getSession());
            beginBatchTransaction();
            defaultAddProperties(model);
            autoDuplicationCheck(model, "insert");
            getSession().save(model);
            processNextInsert(model, "L", 1, ((ModelBase)model).insertControl(), checkingDAO);
            commitBatchTransaction();
            tryRemoveDeletedModel();
        } catch (Exception ex) {
            rollbackBatchTransaction();
            ((ModelBase)model).clearNewModelID();
            checkException(ex);
            throw ex;
        } finally {
            closeSession();
        }
    }

    @Override
    public synchronized T frameworkUpdate(T model) throws Exception {
        try {
            BaseDAOImpl checkingDAO = new BaseDAOImpl();
            checkingDAO.setSession(getSession());
            beginBatchTransaction();
            defaultUpdateProperties(model);
            autoDuplicationCheck(model, ModelBase.UPDATE);
//            processNextUpdate(model, "L", 1, ((ModelBase)model).updateControl(), checkingDAO);
            model = setUpdateProperties(model, session);
            getSession().save(model);
            commitBatchTransaction();
            tryRemoveDeletedModel();
        } catch (Exception ex) {
            rollbackBatchTransaction();
            ((ModelBase)model).clearNewModelID();
            checkException(ex);
            throw ex;
        } finally {
            closeSession();
        }
        return model;
    }

    @Override
    public synchronized Object frameworkDelete(String[] ids, Class modelClass) throws Exception {
        beginBatchTransaction();
        ModelBase model = null;
        try {
            csrfCheck();
            Object strUpdatedBy = null;
            for (String deleteId : ids) {
                if (ModelBase.isAutoNumberPk(modelClass)) {
                    model = (ModelBase) getSession().get(modelClass, new Integer(deleteId));
                } else {
                    model = (ModelBase) getSession().get(modelClass, deleteId);
                }

                if (strUpdatedBy == null) {
                    if(model.createUpdate_useUserPK()) {
                        strUpdatedBy = model.getLoginUserId();
                    } else {
                        strUpdatedBy = model.getLoginId();
                    }
                }
                // For Audit
                model.callSetter_method("updated_by", strUpdatedBy.toString());
//                if ( model.updateByIsInteger() ) {
//                    model.setUpdated_by((Integer)strUpdatedBy);
//                } else {
//                    model.setUpdated_by((String)strUpdatedBy);
//                }
                if (model.getModelService() != null) {
                    Boolean noSuchMethod = Boolean.FALSE;
                    try {
                        Method m = model.getModelService().getClass().getDeclaredMethod("preDelete", Session.class, Object.class);
                    } catch (Exception e) {
                        noSuchMethod = Boolean.TRUE;
                    }
//                    modelBase.clearMyChildMap();
//                    modelBase.getModelService().getMyChildList_byOperation();
//
//                    modelBase.getMyChildMap().putAll(modelBase.getModelService().getMyChildMap());
//                    modelBase.getMyChildMapSetup().putAll(modelBase.getModelService().getMyChildMapSetup());
                    if (!noSuchMethod) {
                        ((ModelBase)model).traceModelOperation("preDelete", ((ModelBase)model).getModelService().getClass().getSimpleName(), 1);
                        model.getModelService().preDelete(session, model);
                    }
                    
                    model.getModelService().deleteModel(session, model);
                    
                    noSuchMethod = Boolean.FALSE;
                    try {
                        Method m = model.getModelService().getClass().getDeclaredMethod("postDelete", Session.class, Object.class);
                    } catch (Exception e) {
                        noSuchMethod = Boolean.TRUE;
                    }
                    if (!noSuchMethod) {
                        ((ModelBase)model).traceModelOperation("postDelete", ((ModelBase)model).getModelService().getClass().getSimpleName(), -1);
                        model.getModelService().postDelete(session, model);
                    }
                } else {
                    ((ModelBase)model).traceModelOperation("preDelete", ((ModelBase)model).getClass().getSimpleName(), 1);
                    model.preDelete(session, model);
                    model.deleteModel(session, model);
                    ((ModelBase)model).traceModelOperation("postDelete", ((ModelBase)model).getClass().getSimpleName(), -1);
                    model.postDelete(session, model);
                }
                //getSession().update(model);
                //getSession().flush();
                // For Audit - End

                //getSession().delete(model);
            }
            commitBatchTransaction();
            tryRemoveDeletedModel();
        } catch (Exception e) {
            e.printStackTrace();
            rollbackBatchTransaction();
            ((ModelBase)model).clearNewModelID();
            checkException(e);
            throw e;
        } finally {
            ((ModelBase)model).printOperationList();
            closeSession();
        }
        return model;
    }
    //change from protected to public, need to use from action
    @Override
    public void checkForReferenced(Object model) throws Exception{
        try {
            ModelBase modelBase = (ModelBase) model;
            modelBase.checkForReferenced(getSession());
        } catch (Exception e) {
            if (e instanceof BaseException){
                throw e;
            }
        }
    }
    public void checkData(Object model, Session session) throws Exception{
        try {
            ModelBase modelBase = (ModelBase) model;
            modelBase.checkData(session);  // ThoTH @ 11-Apr-2014 :: Avoid unClose Session
//            modelBase.checkData(getSession());
        } catch (Exception e) {
            if (e instanceof BaseException){
                throw e;
            }
        }
    }


    @Override
    public Long nativeSqlCountRecord(String sql) {
        NativeQuery query = getSession().createNativeQuery(sql);

        Object obj = query.uniqueResult();
        return ((BigInteger) obj).longValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Long countRecord(Map param, Class modelClass, boolean advanceSearch) {
        Object countObj = null;

        //param.put("us_user_id", "thensw");
        //param.put("us_id", new Integer(10));
        List paramValue = new ArrayList();
        try {
            String sql = "select count(*) from " + modelClass.getName() + " _self ";
            String join = "";
            Query query;
            if (param != null) {
                String condition = "";
                if (advanceSearch) { Debug.printFrameworkDebug("advanceSearch");
                    for (Object obj : param.keySet()) {
                        if (obj.toString().startsWith(SystemConstants.HQL.JOIN)){
                            join += param.get(obj).toString() + " ";
                            continue;
                        }
                        if (param.get(obj).toString().indexOf("NESTEDSQL") >= 0) {
                            String strNestedSql = (String)obj + " " + param.get(obj).toString().substring(9);

                            condition += (condition.length() <= 0) ? "where " + strNestedSql: " and " + strNestedSql;
                            continue;
                        }

                        if (condition.length() <= 0) {
                            condition += "where " + criteriaConverter.strCriteria("", (String) obj, param.get(obj).toString().toLowerCase());
                        } else {
                            condition += " " + criteriaConverter.strCriteria("and", (String) obj, param.get(obj).toString().toLowerCase());
                        }
                        paramValue.add(param.get(obj));
                    }
                    sql += join + condition;
                    query = getSession().createQuery(sql);
                } else {
                    for (Object obj : param.keySet()) {
                        if (obj.toString().startsWith(SystemConstants.HQL.JOIN)){
                            join += param.get(obj).toString() + " ";
                            continue;
                        }
                        String criteriaName = (String) obj;
                        if (criteriaName.indexOf(".") >= 0) {
                            criteriaName = criteriaName.replace(".", "_");
                        }
                        if (condition.length() <= 0) {
                            condition += " where " + obj + " = :" + criteriaName;
                        } else {
                            condition += " and " + obj + " = :" + criteriaName;
                        }
                        paramValue.add(param.get(obj));
                    }
                    sql += join + condition;
                    query = getSession().createQuery(sql);

                    for (Object obj : param.keySet()) {
                        if (obj.toString().startsWith(SystemConstants.HQL.JOIN)){
                            continue;
                        }
                        String paramType = param.get(obj).getClass().getSimpleName();
                        String criteriaName = (String) obj;
                        if (criteriaName.indexOf(".") >= 0) {
                            criteriaName = criteriaName.replace(".", "_");
                        }
                        if (paramType.equals("String")) {
                            query.setString(criteriaName, (String) param.get(obj));
                        } else if (paramType.equals("Double")) {
                            query.setDouble(criteriaName, (Double) param.get(obj));
                        } else if (paramType.equals("Long")) {
                            query.setLong(criteriaName, (Long) param.get(obj));
                        } else if (paramType.equals("Integer")) {
                            query.setInteger(criteriaName, (Integer) param.get(obj));
                        } else if (paramType.equals("Date")) {
                            query.setTimestamp(criteriaName, (java.util.Date) param.get(obj));
                        }
                    }
                }
            } else {
                query = getSession().createQuery(sql);
            }
//            System.out.println("sql "+sql);
            countObj = query.uniqueResult();
            //objs = query.list();
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }
        return (Long) countObj;
    }



//    protected void auditDelete(Object model) throws Exception {
//        auditDelete(model, null);
//    }
//
//
//    protected void auditDelete(Object model, Integer intUpdatedBy, boolean deleteModel) throws Exception {
//        if (intUpdatedBy == null) {
//            intUpdatedBy = (Integer) ActionContext.getContext().getSession().get("userId");
//        }
//        // For Audit
//        Method m = model.getClass().getMethod(SystemConstants.METHOD.SET_MODIFIED_BY, Integer.class);
//        m.invoke(model, intUpdatedBy);
//
//            getSession().update(model);
//            if (deleteModel){
//                getSession().delete(model);
//                getSession().flush();
//                getSession().clear();
//            }
//            // For Audit - End
//    }

    @Override
    public T getModelById(Integer id, Class modelClass) {
        T model = null;
        try {
            model = (T) getSession().get(modelClass, id);
            //getSession().refresh(model, LockMode.NONE);
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }
        return model;
    }


    public Object getMethodObjectFromObject(T model, String method) throws Exception {
        Boolean isDate = Boolean.FALSE;
        if (method.indexOf("|") > 0){
            method = method.substring(method.indexOf("|") + 1);
        }
        if (method.indexOf("_date_") >= 0) {
            method = method.substring(method.indexOf("_date_") + 6);
            isDate = Boolean.TRUE;
        }

        StringTokenizer st = new StringTokenizer(method, ".");
        String token = "";
        Object obj = null;
        Method m;
        while (st.hasMoreTokens()) {
            token = st.nextToken().trim();
            if (token.startsWith("_self")) {
                //token = token.substring(6);
                continue;
            }
            if (!token.startsWith("get")) {
                token = "get" + WordUtils.capitalize(token);
            }
            if (obj == null) {
                m = model.getClass().getMethod(token);
                obj = m.invoke(model);
            } else {
                m = obj.getClass().getMethod(token);
                obj = m.invoke(obj);
            }
        }
        if (obj != null && isDate) {
            return obj.toString().substring(0, 10);
        }
        return obj;
    }

    //added dev24 jun2011
    public synchronized String getRunningSeq(String strType, String strYear, String prefix, String postfix, Boolean appendZero, String strFormat) throws Exception{
        DecimalFormat numDigits = new DecimalFormat(strFormat);
        //String strNewSeq = "";
        long llSeq = 0;
        try {
            SeqModel seq = (SeqModel)getObjectById( strType+strYear, SeqModel.class);

            if(seq == null){
//                System.out.println("NewSeq");
                llSeq = 1;
                seq = new SeqModel();
                seq.setSeq_id(strType+strYear);
                seq.setSeq_last_no(llSeq);

                getSession().save(seq);

//                seqDAO.insert(seq);
            }else{
//                System.out.println("Before seq = " + seq.getSeq_last_no() );
                llSeq = seq.getSeq_last_no() + 1;
//                if(llSeq > 999){
////
//                    //Maximum running number reached
//                    return strFormat;
//
//                }
                seq.setSeq_last_no(llSeq);

                getSession().update(seq);
                //try{
                //seqDAO.update(seq);
//                seqDAO.directUpdate(seq);
                //}catch(Exception ex){
                //}
            }
//            System.out.println("After seq = " + llSeq );
            //aeDAO.update(ae);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
        }
        if(appendZero){
//            System.out.println("Append"+prefix+sixDigits.format(llSeq)+postfix);
            return prefix+numDigits.format(llSeq)+postfix ;
        }else{
//            System.out.println("NO Append"+prefix+llSeq+postfix);
            return prefix+llSeq+postfix ;
        }
    }


    public void preInsert(Object model) throws Exception{
        if (manualUpdate) return;
        ((ModelBase)model).genAndKeepReverseSql(Boolean.FALSE, ModelBase.ReversalOpt.INSERT, getSession());
        ModelBase modelBase = null;
        try {
            modelBase = (ModelBase) model;
        } catch (Exception e) {
            modelBase = null;
        }
        if (modelBase != null) {
//            System.out.println("modelBase.getClass() = " + modelBase.getClass());
            if (modelBase.getModelService(modelBase) != null) {
                modelBase.traceModelOperation("preInsert", modelBase.getModelService().getClass().getSimpleName(), 1);
                modelBase.getModelService().preInsert(getSession());
            } else {
                modelBase.traceModelOperation("preInsert", modelBase.getClass().getSimpleName(), 1);
                modelBase.preInsert(getSession());
            }
        }
    }

    private void manualOperation(Object model) throws Exception{
        if (manualUpdate) return;
        ModelBase modelBase = null;
        try {
            modelBase = (ModelBase) model;
        } catch (Exception e) {
            modelBase = null;
        }
        if (modelBase != null) {
            if (modelBase.getModelService() != null) {
                modelBase.traceModelOperation("manualOperation", modelBase.getModelService().getClass().getSimpleName(), 0);
                modelBase.getModelService().manualOperation(getSession());
            } else {
                modelBase.traceModelOperation("manualOperation", modelBase.getClass().getSimpleName(), 0);
                modelBase.manualOperation(getSession());
            }
        }
    }

    public void preUpdate(Object model, Object dataEntryModel, Session session) throws Exception{
        if (manualUpdate) return;
        ((ModelBase)model).genAndKeepReverseSql(Boolean.TRUE, ModelBase.ReversalOpt.UPDATE, session);
        try {
            ModelBase modelBase = null;
            try {
                modelBase = (ModelBase) model;
            } catch (Exception e) {
                modelBase = null;
            }
            if (modelBase != null) {   
                if (modelBase.getModelService(modelBase) != null) {
                    Boolean noSuchMethod = Boolean.FALSE;
                    try {
                        Method m = modelBase.getModelService().getClass().getDeclaredMethod("preUpdate", Session.class, Object.class);
                    } catch (Exception e) {
                        noSuchMethod = Boolean.TRUE;
                    }
//                    modelBase.clearMyChildMap();
//                    modelBase.getModelService().getMyChildList_byOperation();
//
//                    modelBase.getMyChildMap().putAll(modelBase.getModelService().getMyChildMap());
//                    modelBase.getMyChildMapSetup().putAll(modelBase.getModelService().getMyChildMapSetup());
                    if (!noSuchMethod) {
                        modelBase.traceModelOperation("preUpdate", modelBase.getModelService().getClass().getSimpleName(), 1);
                        modelBase.getModelService().preUpdate(session, dataEntryModel);
                    } else {
                        modelBase.traceModelOperation("preUpdate", modelBase.getClass().getSimpleName(), 1);
                    }
                    modelBase.preUpdate(session, dataEntryModel);
                } else {
                    modelBase.traceModelOperation("preUpdate", modelBase.getClass().getSimpleName(), 1);
                    modelBase.preUpdate(session, dataEntryModel);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void postInsert(Object model) throws Exception{
        if (manualUpdate) return;
        ModelBase modelBase = null;
        try {
            modelBase = (ModelBase) model;
        } catch (Exception e) {}
        if (modelBase != null) {
            if (modelBase.getModelService() != null) {
                Boolean noSuchMethod = Boolean.FALSE;
                try {
                    Method m = modelBase.getModelService().getClass().getDeclaredMethod("postInsert", Session.class);
                } catch (Exception e) {
                    noSuchMethod = Boolean.TRUE;
                }
                if (!noSuchMethod) {
                    modelBase.traceModelOperation("postInsert", modelBase.getModelService().getClass().getSimpleName(), 0);
                    ModelBase.TraceModelOperation_level--;
                    modelBase.getModelService().postInsert(session);
                } else {
                    modelBase.traceModelOperation("postInsert", modelBase.getClass().getSimpleName(), 0);
                    ModelBase.TraceModelOperation_level--;
                }
                modelBase.postInsert(getSession());
            } else {
                modelBase.traceModelOperation("postInsert", modelBase.getClass().getSimpleName(), 0);
                ModelBase.TraceModelOperation_level--;
                modelBase.postInsert(getSession());
            }
        }
    }
    public void postUpdate(Object model, Object updatingModel) throws Exception{
        if (manualUpdate) return;
        ModelBase modelBase = null;
        try {
            modelBase = (ModelBase) model;
        } catch (Exception e) {}
        if (modelBase != null) {
            if (modelBase.getModelService((ModelBase)updatingModel) != null) {
                Boolean noSuchMethod = Boolean.FALSE;
                try {
                    Method m = modelBase.getModelService().getClass().getDeclaredMethod("postUpdate", Session.class, Object.class);
                } catch (Exception e) {
                    noSuchMethod = Boolean.TRUE;
                }
                if (!noSuchMethod) {
                    modelBase.traceModelOperation("postUpdate", modelBase.getModelService().getClass().getSimpleName(), 0);
                    ModelBase.TraceModelOperation_level--;
                    modelBase.getModelService().postUpdate(getSession(), updatingModel);
                }
            } else {
                modelBase.postUpdate(getSession(), updatingModel);
            }
        }
    }

    public void deleteItems(List list) throws Exception {
        Object strUpdatedBy = null;
        for (Object obj : list) {
            if (strUpdatedBy == null) {
                if (((ModelBase)obj).createUpdate_useUserPK()) {
                    strUpdatedBy = ((ModelBase)obj).getLoginUserId();
                } else {
                    strUpdatedBy = ((ModelBase)obj).getLoginId();
                }
            }
            
            checkForReferenced(obj); //code the Delete checking at Model to throw error if the deleting model is referred by other record.
            deleteMyItemsLists(obj); //if the checking pass, it
            // For Audit
            auditDelete(obj, strUpdatedBy);
            // For Audit - End
        }
    }

    public void deleteMyItemsLists(Object obj) throws Exception { //delete all the model's items(children of the model)
        try {
            ModelBase modelBase = (ModelBase) obj;
            if (modelBase.myItemsLists() != null) {
                for (List list : modelBase.myItemsLists()) {
                    deleteItems(list);
                }
            }
        } catch (Exception e) {
            if (e instanceof BaseException){
                throw e;
            }
        }
    }

    public void daoUpdate(Object updatingModel) throws Exception {
        try {
            if ( ((ModelBase)updatingModel).getCheckIsDataModified_()) {
                if ( ((ModelBase)updatingModel).isDataModified_()) {
                    session.update(updatingModel);
                } else {
                    session.evict(updatingModel);
                }
            } else {
                session.update(updatingModel);
            }
        } catch (ClassCastException cce) {
            session.update(updatingModel);
        }
    }
    
    // ThoTH @ 9-Nov-2015 :: For ACNL
    // Shall scan your own code, to replace "ActionContext.getContext().getSession().get("userId")" to this 
    public String getUserId() {
        String str = "";
        try {
            if (ActionContext.getContext().getSession().get("acting_user_id") != null) {
                str = (String) ActionContext.getContext().getSession().get("acting_user_id");
            } else {
                str = (String) ActionContext.getContext().getSession().get("userId");
            }
        } catch (Exception e) {
            str = SystemConstants.BACKEND.DEFAULT_ID;
        }
        return str;
    }
    
    // ThoTH @ 29-Oct-2015 :: For ACNL
    // Shall scan your own code, to replace "ActionContext.getContext().getSession().get("loginId")" to this 
    public String getLoginId() {
        String str = "";
        try {
            if (ActionContext.getContext().getSession().get("acting_login_id") != null) {
                str = (String) ActionContext.getContext().getSession().get("acting_login_id");
            } else {
                str = (String) ActionContext.getContext().getSession().get("loginId");
            }
        } catch (Exception e) {
            str = SystemConstants.BACKEND.DEFAULT_ID;
        }
        return str;
    }

    protected static final int primaryKeyLength = 20;
    public static void defaultAddProperties(Object obj) throws Exception {
        HttpServletRequest request = null;
        try {
            request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        } catch (Exception e) {
        }
        if (ModelBase.class.isAssignableFrom(obj.getClass())) {
            if (!((ModelBase)obj).getDoNotAssignID__()) {
                if (request != null && request.getAttribute("submitPersonUserId") != null){
                    ((ModelBase)obj).defaultAddProperties((String) request.getAttribute("submitPersonUserId"));
                }else{
                    ((ModelBase)obj).defaultAddProperties();
                }
            }
        } else {
            try {
                Method m;
                if (ActionContext.getContext().getSession().get("acting_login_id") != null) {
                    
                } else {
                    if (((ModelBase)obj).createUpdate_useUserPK()) {
                        ((ModelBase)obj).callSetter_method("created_by", ActionContext.getContext().getSession().get("userId").toString());
//                        if ( ((ModelBase)obj).updateByIsInteger() ) {
//                            ((ModelBase)obj).setCreated_by(new Integer(ActionContext.getContext().getSession().get("userId").toString()));
//                        } else {
//                            ((ModelBase)obj).setCreated_by((String)ActionContext.getContext().getSession().get("userId"));
//                        }
                    } else {
                        ((ModelBase)obj).callSetter_method("created_by", (String)ActionContext.getContext().getSession().get("loginId"));
//                        ((ModelBase)obj).setCreated_by((String)ActionContext.getContext().getSession().get("loginId"));
                    }
                }
                
//                try {
//                    if ( ((ModelBase)obj).updateByIsInteger() ) {
//                        m.invoke(obj, new Integer( ((ModelBase)obj).getLoginUserId() ));
//                    } else {
//                        if (ActionContext.getContext().getSession().get("acting_login_id") != null) {
//                            m.invoke(obj, (String) ActionContext.getContext().getSession().get("acting_login_id"));
//                        } else {
//                            if (ModelBase.GLOBAL_USE_USER_PK) {
//                                if ( ((ModelBase)obj).updateByIsInteger() ) {
//                                    m.invoke(obj, (String) ActionContext.getContext().getSession().get("userId"));
//                                } else {
//                                    m.invoke(obj, (String) ActionContext.getContext().getSession().get("userId"));
//                                }
//                            } else {
//                                if (((ModelBase)obj).createUpdate_useUserPK()) {
//                                    if ( ((ModelBase)obj).updateByIsInteger() ) {
//                                        m.invoke(obj, new Integer(ActionContext.getContext().getSession().get("userId").toString()));
//                                    } else {
//                                        m.invoke(obj, (String) ActionContext.getContext().getSession().get("userId"));
//                                    }
//                                } else {
//                                    m.invoke(obj, (String) ActionContext.getContext().getSession().get("loginId"));
//                                }
//                            }
//                        }
//                    }
//                } catch (Exception noCreatedBy) {
//                    m.invoke(obj, SystemConstants.BACKEND.DEFAULT_ID);
//                }
                m = obj.getClass().getMethod("setCreated_date", java.sql.Timestamp.class);
                m.invoke(obj, DateUtil.getCurrentTimestamp());
                try {
                    if (!(((ModelBase)obj).isAutoNumberPk(obj.getClass()))) {
                        m = obj.getClass().getMethod("setID", String.class);
                        if (ModelBase.isIntegerPk(obj.getClass())) {
                            m.invoke(obj, com.sains.framework.base.CommonFunction.getIntId_milisec_plus_4_runningDigit().toString());
                        } else {
                            m.invoke(obj, com.sains.framework.base.CommonFunction.getId(primaryKeyLength));
                        }
                    }
                } catch (Exception e) {}
                defaultUpdateProperties(obj);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void defaultUpdateProperties(Object obj) {
        if (ModelBase.class.isAssignableFrom(obj.getClass())) {
            try {
                ((ModelBase)obj).defaultUpdateProperties();
            } catch (Exception e) {
            }
        } else {
            try {
                Method m;
//                if ( ((ModelBase)obj).updateByIsInteger() ) {
//                    m = obj.getClass().getMethod("setUpdated_by", Integer.class);
//                } else {
//                    m = obj.getClass().getMethod("setUpdated_by", String.class);
//                }
                if (((ModelBase)obj).createUpdate_useUserPK()) {
                    ((ModelBase)obj).callSetter_method("updated_by", ActionContext.getContext().getSession().get("userId").toString());
//                    if ( ((ModelBase)obj).updateByIsInteger() ) {
//                        ((ModelBase)obj).setUpdated_by(new Integer(ActionContext.getContext().getSession().get("userId").toString()));
//                    } else {
//                        ((ModelBase)obj).setUpdated_by((String)ActionContext.getContext().getSession().get("userId"));
//                    }
                } else {
                    ((ModelBase)obj).callSetter_method("updated_by", (String)ActionContext.getContext().getSession().get("loginId"));
//                    ((ModelBase)obj).setUpdated_by((String)ActionContext.getContext().getSession().get("loginId"));
                }
//                try {
//                    if ( ModelBase.isAutoNumberPk(obj.getClass()) && ((ModelBase)obj).createUpdate_useUserPK() ) {
//                        m.invoke(obj, new Integer( ((ModelBase)obj).getLoginUserId() ));
//                    } else {
//                        if (ActionContext.getContext().getSession().get("acting_login_id") != null) {
//                            m.invoke(obj, (String) ActionContext.getContext().getSession().get("acting_login_id"));
//                        } else {
//                            if (ModelBase.GLOBAL_USE_USER_PK) {
//                                if ( ((ModelBase)obj).updateByIsInteger() ) {
//                                    m.invoke(obj, new Integer (ActionContext.getContext().getSession().get("userId")));
//                                } else {
//                                    m.invoke(obj, (String) ActionContext.getContext().getSession().get("userId"));
//                                }
//                            } else {
//                                if (((ModelBase)obj).createUpdate_useUserPK()) {
//                                    if ( ((ModelBase)obj).updateByIsInteger() ) {
//                                        m.invoke(obj, new Integer(ActionContext.getContext().getSession().get("userId")));
//                                    } else {
//                                        m.invoke(obj, (String) ActionContext.getContext().getSession().get("userId"));
//                                    }
//                                } else {
//                                    m.invoke(obj, (String) ActionContext.getContext().getSession().get("loginId"));
//                                }
//                            }
//                        }
//                    }
//                } catch (Exception noUpdatedBy){
//                    //set the default updated id to "BACKEND" if fail to get id from session.
//                    m.invoke(obj, SystemConstants.BACKEND.DEFAULT_ID);
//                }
                m = obj.getClass().getMethod("setUpdated_date", java.sql.Timestamp.class);
                m.invoke(obj, DateUtil.getCurrentTimestamp());

                try {
                    if (!(((ModelBase)obj).isAutoNumberPk(obj.getClass()))) {
                        m = obj.getClass().getMethod("getID");
                        String id = (String) m.invoke(obj);
                        if (id == null || id.equals("")) {
                            m = obj.getClass().getMethod("setID", String.class);
                            m.invoke(obj, com.sains.framework.base.CommonFunction.getId(primaryKeyLength));
                        }
                    }
                } catch (Exception e){}
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    // Move from CommonFunction to solve Syn Problem
    public synchronized String getRunningSeq2(String strType, String strYear, String prefix, String postfix, Boolean appendZero, String strFormat) throws Exception {
        DecimalFormat numDigits = new DecimalFormat(strFormat);
        //String strNewSeq = "";
        long liSeq = 0;
        try {
            //begin and commit commented by ahmadni because nested sql issue @ 1-dec-2016
//             beginBatchTransaction();
//            System.out.println("seqid "+strType +strYear);
            SeqModel seq = (SeqModel) getObjectById(strType + strYear, SeqModel.class);
            if (seq == null) {
                Debug.printFrameworkDebug("NewSeq");
                if (strType.startsWith("RVSReceiptNo") || strType.startsWith("TempLNSNo")){
                    liSeq = 900000;
                }else{
                    liSeq = 0;  // ThoTH @ 8-Mar-2012 : RVS Receipt No will start from 700,000
                }
                seq = new SeqModel();
                seq.setSeq_id(strType + strYear);                

                seq.setSeq_last_no(liSeq); // use back 16/3/2012
//                seq.setSeq_last_no(Integer.parseInt(prefix + numDigits.format(liSeq) + postfix)); // modified by ahmadni during TRAINING @SNT
                liSeq = seq.getSeq_last_no(); //added by ahmadni 12/3/2012

                getSession().save(seq);

//                seqDAO.insert(seq);
            } else {
                Debug.printFrameworkDebug("Before seq = " + seq.getSeq_last_no() );                

                liSeq = seq.getSeq_last_no() + 1;
                seq.setSeq_last_no(liSeq);                
                getSession().update(seq);
                //try{
                //seqDAO.update(seq);
//                seqDAO.directUpdate(seq);
                //}catch(Exception ex){
                //}
            }
             Debug.printFrameworkDebug("After seq = " + liSeq );
            //aeDAO.update(ae);
//             commitBatchTransaction();
        } catch (Exception e) {
//             rollbackBatchTransaction();
            throw e;
        } finally {
        }
        if (appendZero) {
//            System.out.println("Append"+prefix+sixDigits.format(llSeq)+postfix);
            return prefix + numDigits.format(liSeq) + postfix;
//            return "" + liSeq + ""; // modified by ahmadni 12/3/2012
        } else {//post2rvs will be using this one
//            System.out.println("NO Append"+prefix+llSeq+postfix);
//            return prefix + liSeq + postfix;
            return "" + liSeq + ""; // modified by ahmadni 12/3/2012
        }
    }
    
    public void csrfCheck() throws Exception {
        HttpServletRequest request = null;
        try {
            try {
                request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            } catch (Exception e) {
            }
            if (request == null) {
                if (ModelBase.currentRequestObject("antiCSRF") != null &&
                        ModelBase.currentRequestObject("antiCSRF").equals("isFromBackend") ||
                        ModelBase.currentRequestObject("antiCSRF").equals("ignoreCSRF")) {
                    return;
                }
            }
            if (request.getAttribute("isFromBackend") != null && request.getAttribute("isFromBackend").equals("true")) {
                Debug.printFrameworkDebug("is from backend");
            } else {
                if (request.getAttribute("ignoreCsrfCheck") != null && request.getAttribute("ignoreCsrfCheck").equals("true")) {
                    return;
                }
                if (request.getMethod().equalsIgnoreCase("get")) {
                    if (request.getAttribute("csrfAllowGet") == null || request.getAttribute("csrfAllowGet").equals("false")) {
                        throw new CustomBaseException("CSRF Failed : Must use POST");
                    }
                }
                Map session = ActionContext.getContext().getSession();
                
                System.out.println("get - " + session.get("antiCsrf") + " req - " + request.getParameter("antiCsrf"));
                
                if (session.containsKey("antiCsrf")) {
                    if (request.getParameter("antiCsrf") == null) {
                        throw new CustomBaseException("CSRF Failed : No anti CSRF Token");
                    } else if (!(session.get("antiCsrf").equals(request.getParameter("antiCsrf")))) {
                        throw new CustomBaseException("CSRF Failed : Invalid anti CSRF Token");
                    }
                } else {
                    Debug.printDebug("No csrf token in session, no csrf checking");
                }
            }
        } catch (BaseException be) {
            throw be;
        } catch (Exception e) {
            e.printStackTrace();
            Debug.printDebug("request is null, call from backend?");
        }
    }
    
    private PreparedStatement pStmt=null;
    public PreparedStatement getpStmt(final String sql) {
        getSession().doWork(new Work() {
            @Override
            public void execute(java.sql.Connection conn) throws SQLException {
                pStmt = conn.prepareStatement(sql);
//                    try {
//                        //System.out.println("jasperRpt = " + jasperRpt);
//                        //System.out.println("outFileName = " + outFileName);
//                        // Fill the report using an empty data source
//                        //JasperPrint print = JasperFillManager.fillReport(file.getPath(), hm, new JREmptyDataSource());
//                        JasperPrint print = JasperFillManager.fillReport(jasperRpt, param, conn);
//                        // Create a PDF exporter
//                        JRExporter exporter = new JRPdfExporter();
//                        // Configure the exporter (set output file name and print object)
//                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
//                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
//                        // Export the PDF file
//                        exporter.exportReport();
//                    } catch (JRException e) {
//                        new LogFunction().logError(ReportGenerator.class, "in JREException", e);
//                    } catch (Exception e) {
//                    }
            }
        });
        return pStmt;
    }
    private Statement cStmt=null;
    public Statement getcStmt() {
        getSession().doWork(new Work() {
            @Override
            public void execute(java.sql.Connection conn) throws SQLException {
                cStmt = conn.createStatement();
//                    try {
//                        //System.out.println("jasperRpt = " + jasperRpt);
//                        //System.out.println("outFileName = " + outFileName);
//                        // Fill the report using an empty data source
//                        //JasperPrint print = JasperFillManager.fillReport(file.getPath(), hm, new JREmptyDataSource());
//                        JasperPrint print = JasperFillManager.fillReport(jasperRpt, param, conn);
//                        // Create a PDF exporter
//                        JRExporter exporter = new JRPdfExporter();
//                        // Configure the exporter (set output file name and print object)
//                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
//                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
//                        // Export the PDF file
//                        exporter.exportReport();
//                    } catch (JRException e) {
//                        new LogFunction().logError(ReportGenerator.class, "in JREException", e);
//                    } catch (Exception e) {
//                    }
            }
        });
        return cStmt;
    }
}
