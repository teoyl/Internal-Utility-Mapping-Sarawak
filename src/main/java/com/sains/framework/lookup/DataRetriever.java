package com.sains.framework.lookup;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.common.util.IConstants;
import com.sains.common.util.CriteriaConverter;
import com.sains.framework.base.*;
import java.math.BigDecimal;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

/**
 * Retrieve the data for the query that is specified in the config file.
 *
 * @version 1.0
 * @author ThenSW
 */
// TODO: to change the JDBC handling with hibernate query to eliminate the hard-coding of LIMIT (use setmaxresult instead)
public class DataRetriever {

    private final String QUERY_CONFIG = "queryConfig";

    private final String FIELDS = "fields";

    private final String TABLES = "tables";

    private final String FILTERS = "filters";

    private final String SESSION_FILTERS = "sessionFilters";

    private final String FILTER_FIELDS = "filterFields";

    private final String CRITERIAS = "criterias";

    private final String COLUMNS = "columns";

    private final String RESULT = "result";

    private static final String QUERY = "query";

    private static final String ENTITY_ID = "entityId";
    private static final String COMPANY_ID = "co_id";

    private final String FILTER_BY = "filterBy";

    private final String ORDERS = "orders";

    private String timestampFormat = "dd MMM yyyy HH:mm:ss";
    private int pageNo, pageSize;
    private org.hibernate.SQLQuery query = null;
    private String dbType = "";

    private CriteriaConverter criteriaConverter = new CriteriaConverter(Boolean.TRUE);

    //private java.sql.Connection conn = SessionFactoryImpl.getConnection();
    private void checkDbType(Session session) {
        String driver = (String)session.getSessionFactory().getProperties().get("hibernate.connection.driver_class");
        if (driver.contains("sqlserver")) {
            dbType = "sqlserver";
        } else if (driver.contains("oracle")) {
            dbType = "oracle";
        } else if (driver.contains("mysql")) {
            dbType = "mysql";
        }
        if (criteriaConverter.dbType == null) {
            criteriaConverter.dbType = dbType;
        }
    }
    private boolean isOracleDB = new CommonFunction().isOracleDB();

    public boolean retrieveData(LookupAction form,
            HttpServletRequest request, ServletContext context)
            throws Exception {
//		java.sql.Connection conn = SessionFactoryImpl.getConnection();
        boolean result = false;
        Session session = form.hibernateSession();
        checkDbType(session);
//		PreparedStatement statement = null;
//		ResultSet resultSet = null;

        try {
            //PersistentSession session = EMSPersistentManager.instance().getSession();
            //Map criterias = (Map) form.get

            //Query form.session.createSQLQuery(getSQL(request, context, form.getCriterias(), true, true, false));
//			statement = conn.prepareStatement(getSQL(request, context, form.getCriterias(), true, true, false, false, isOracleDB));
//			//setParameters(statement, form.getCriterias());
//			resultSet = statement.executeQuery();
//			//copyTo(resultSet.getMetaData(), (List) form.get(COLUMNS));
//			copyTo(resultSet.getMetaData(), (List) form.getColumns());
//			//copyTo(resultSet, (List) form.get(RESULT));
//			copyTo(resultSet, (List) form.getResult());
            query = session.createSQLQuery(getSQL(request, context, form.getCriterias(), true, true, false, false, isOracleDB));
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            List list = query.list();
            form.setResult(list);

            if (form.getResult() != null && form.getResult().size() > 0) {
//				statement = conn.prepareStatement(getSQL(request, context, form.getCriterias(), false, true, false, true, isOracleDB));
//				resultSet = statement.executeQuery();
//				resultSet.next();
                query = session.createSQLQuery(getSQL(request, context, form.getCriterias(), false, true, false, true, isOracleDB));
                Object countObj = query.uniqueResult();
                if (countObj instanceof Integer) {
                    form.setNumberOfRows(((Integer) countObj).intValue());
                } else if (countObj instanceof Long) {
                    form.setNumberOfRows(((Long) countObj).intValue());
                } else {
                    form.setNumberOfRows(((BigInteger) countObj).intValue());
                }
//				form.setNumberOfRows(resultSet.getInt(1));
                form.setPageSize(Integer.parseInt(getParameter(request, "pageSize", form.getText("defaultPageSize", "10"))));
                form.setPageNo(Integer.parseInt(getParameter(request, "pageNo", "1")));
            }

            result = true;
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
//			if (resultSet != null) {
//				resultSet.close();
//			}
//			if (statement != null) {
//				statement.close();
//			}
//			if (conn != null){
//                            SessionFactoryImpl.closeConnection(conn);
//			}
        }
        return result;
    }

    public boolean retrieveData_useSetup(LookupAction form,
            HttpServletRequest request, ServletContext context)
            throws Exception {

        boolean result = false;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//        java.sql.Connection conn = SessionFactoryImpl.getConnection();
        Session session = form.hibernateSession();
        checkDbType(session);
        String sql = "";
        List<String> preTableList = new ArrayList();
        //Session session = new SessionFactoryImpl().getSession();
        try {
            if (isOracleDB) {
                criteriaConverter.setIsOracleDB();
            }
            criteriaConverter.criteriaMap.clear();
//            Debug.printFrameworkDebug("form.getRetrievingColumns() " + form.getRetrievingColumns());
            //PersistentSession session = EMSPersistentManager.instance().getSession();
            //Map criterias = (Map) form.get

            //Query form.session.createSQLQuery(getSQL(request, context, form.getCriterias(), true, true, false));
            StringBuffer buffer = new StringBuffer();
            String distinctSelect = "";
            if (form.getRetrievingColumns().startsWith("distinct")) {
                distinctSelect = " group by " + form.getRetrievingColumns().substring(9);
            }
            buffer.append("SELECT ").append(form.getRetrievingColumns()).append(" FROM ").append(form.getSqlTables());
            StringBuffer condition = new StringBuffer();
            Map<String, String> setupMap = form.getSetupMap();

            // Added by ThoTH @ 9-Mar-2011
            if (setupMap.get("defaultLookupSearchValue") != null) {
                if (setupMap.get("defaultLookupSearchValue").toString().length() > 0) {
                    if (condition.indexOf("where") >= 0) {
                        condition.append(" and (" + setupMap.get("defaultLookupSearchValue").toString() + ") ");
                    } else {
                        condition.append(" where (" + setupMap.get("defaultLookupSearchValue").toString() + ") ");
                    }
                }
            }
            //Added Wongkk4 @ 19/3/2014
            if (condition.toString().indexOf(":_sessionFilter_") >= 0) {
                String tmpCondition = replaceCriteriaData(condition.toString());
                condition.setLength(0);
                condition.append(tmpCondition);
            }

            Map param = form.getSearchedParam();
            //Set joinSet = new HashSet();
            String joinTable = "";
            String strColumn = "";
            if (param.keySet() != null && param.keySet().size() > 0) {
                for (Object obj : param.keySet()) {
                    if (obj.equals(SystemConstants.DYNAMIC_ATTS.SPECIAL_CRITERIA)) {
                        if (condition.length() <= 0) {
                            condition.append(" where ").append(param.get(SystemConstants.DYNAMIC_ATTS.SPECIAL_CRITERIA));
                        } else {
                            condition.append(" and ").append(param.get(SystemConstants.DYNAMIC_ATTS.SPECIAL_CRITERIA));
                        }
                        continue;
                    }
                    if (((String) obj).indexOf(new String("|")) > 0) {
                        StringTokenizer moreColumns = new StringTokenizer((String) obj, new String("|"));
                        StringBuffer moreConditions = new StringBuffer();
                        while (moreColumns.hasMoreTokens()) {
                            strColumn = moreColumns.nextToken().trim();
                            strColumn = criteriaConverter.replaceCriteriaComma(strColumn);
                            if (moreConditions.length() <= 0) {
                                moreConditions.append(criteriaConverter.strCriteria("", strColumn, param.get(obj).toString().toLowerCase()));
                            } else {
                                moreConditions.append(criteriaConverter.strCriteria("or", strColumn, param.get(obj).toString().toLowerCase()));
                            }
                            if (setupMap.get(strColumn + "_join") != null) {
                                if (setupMap.get(strColumn + "_join_preTable") != null) {
                                    String preTable = (String) setupMap.get(strColumn + "_join_preTable");
                                    if (setupMap.get(preTable) != null) {
                                        if (!preTableList.contains(preTable)) {
                                            preTableList.add(preTable);
                                            joinTable += " " + setupMap.get(preTable);
                                        }
                                    }
                                }
                                joinTable += " " + setupMap.get(strColumn + "_join").toString();
                            }
                        }
                        String temp = moreConditions.toString();
                        if (setupMap.get(strColumn + "_other_condition") != null) {
                            temp = (String) setupMap.get(strColumn + "_other_condition");
                            temp = temp.replace("$filterCondition", moreConditions);
                        }
                        if (condition.length() <= 0) {
                            condition.append(" where (").append(temp).append(")");
                        } else {
                            condition.append(" and (").append(temp).append(")");
                        }
                    } else { // 1 columns XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                        String filterCondition = criteriaConverter.strCriteria("", criteriaConverter.replaceCriteriaComma((String) obj), param.get(obj).toString().toLowerCase());
                        String temp = filterCondition;
                        if (setupMap.get(obj + "_other_condition") != null) {
                            temp = (String) setupMap.get(obj + "_other_condition");
                            temp = temp.replace("$filterCondition", filterCondition);
                        }
                        if (condition.length() <= 0) {
                            //condition.append(" where ").append(criteriaConverter.strCriteria("", (String) obj, param.get(obj).toString().toLowerCase()));
                            condition.append(" where ").append(temp);
                        } else {
                            condition.append(" and ").append(temp);
                        }
                        if (setupMap.get(obj.toString() + "_join") != null) {
                            if (setupMap.get(obj.toString() + "_join_preTable") != null) {
                                String preTable = (String) setupMap.get(obj.toString() + "_join_preTable");
                                if (setupMap.get(preTable) != null) {
                                    if (!preTableList.contains(preTable)) {
                                        preTableList.add(preTable);
                                        joinTable += " " + setupMap.get(preTable);
                                    }
                                }
                            }
                            joinTable += " " + setupMap.get(obj.toString() + "_join").toString();
                        }
                    }
                }
            }

            sql = buffer.toString() + joinTable + (condition.toString() == null ? "" : condition.toString());
            int startNum = 1;

            //GROUP BY
            if (form.getDcGroupBy() != null && form.getDcGroupBy().length() > 0) {
                sql += " GROUP BY " + form.getDcGroupBy();
            }

            // ORDER
            if (form.getSqlOrderBy() != null && form.getSqlOrderBy().length() > 0) {
                if (isOracleDB) {
                    String tempOrder = " ORDER BY lower(" + form.getSqlOrderBy();
                    tempOrder = tempOrder.replaceAll(" asc", " ) asc");
                    tempOrder = tempOrder.replaceAll(" desc", " ) desc");
                    sql += tempOrder;
                } else {
                    sql += " ORDER BY " + form.getSqlOrderBy();
                }
            }
            // PAGING
//            pageSize = Integer.parseInt(getParameter(request, "pageSize", "10"));
//            pageNo = Integer.parseInt(getParameter(request, "pageNo", "1"));
            pageSize = form.getPageSize();
            pageNo = form.getPageNo();
            if (form.isPaging() && !isOracleDB) {
                sql += getPagingStatement(request);
//                Debug.printFrameworkDebug("sql = " + sql);
            } else if (form.isPaging() && isOracleDB) {
                startNum = ((pageNo - 1) * pageSize) + 1;
                sql = "select * "
                        + "from ( select a.*, rownum rnum "
                        + "from (" + sql + ") a "
                        + "where rownum < " + (startNum + pageSize) + " ) "
                        + "where rnum >= " + startNum;
            }
            Debug.printFrameworkDebug("useSetup lookup SQL = " + sql);
//            statement = conn.prepareStatement(sql);
//            resultSet = statement.executeQuery();
//            copyTo(resultSet.getMetaData(), (List) form.getColumns());
//            copyTo(resultSet, (List) form.getResult());
            query = session.createSQLQuery(sql);
            if (criteriaConverter.populateCriteria) {
                for (String key : criteriaConverter.criteriaMap.keySet()) {
                    if (criteriaConverter.criteriaMap.get(key)!=null && criteriaConverter.criteriaMap.get(key).getClass().isArray()) {
                        query.setParameterList(key, (Object[])criteriaConverter.criteriaMap.get(key));
                    } else {
                        query.setParameter(key, criteriaConverter.criteriaMap.get(key));
                    }
                }
            }
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            List<Map<String, Object>> list = query.list();
            form.setResult(list);
//            Debug.printFrameworkDebug("size = " + form.getResult().size());
            if (form.getResult() != null && form.getResult().size() > 0 && form.isPaging()) {
//                statement.close();
                sql = "select count(*) from (" + buffer.toString() + joinTable + (condition.toString() == null ? "" : condition.toString());
                if (form.getDcGroupBy() != null && form.getDcGroupBy().length() > 0) {
                    sql += " GROUP BY " + form.getDcGroupBy();
                }
                sql += ") n";
//                buffer.setLength(0);
//                if (!Validator.isEmpty(distinctSelect)) {
//                    buffer.append("SELECT count(*) FROM (select ").append(distinctSelect).append(" from ").append(form.getSqlTables())
//                            .append(" ").append(joinTable).append((condition.toString() == null ? "" : condition.toString()));
//                    sql = buffer.toString();
////                    sql = buffer.toString() + joinTable + (condition.toString() == null ? "" : condition.toString());
////                    sql = "select count(*) from (select " + distinctSelect + ") n";
//                } else if (!Validator.isEmpty(form.getDcGroupBy())) {
//                    buffer.append("SELECT count(*) FROM (select '' from ").append(form.getSqlTables())
//                            .append(" ").append(joinTable).append((condition.toString() == null ? "" : condition.toString()))
//                            .append(" GROUP BY ").append(form.getDcGroupBy()).append(") n");
//                    sql = buffer.toString();
//                }
                Debug.printFrameworkDebug("Paging SQL = " + sql);
//                statement = conn.prepareStatement(sql);
//                resultSet = statement.executeQuery();
//                resultSet.next();
//                form.setNumberOfRows(resultSet.getInt(1));
                query = session.createSQLQuery(sql);
                if (criteriaConverter.populateCriteria) {
                    for (String key : criteriaConverter.criteriaMap.keySet()) {
                        if (criteriaConverter.criteriaMap.get(key)!=null && criteriaConverter.criteriaMap.get(key).getClass().isArray()) {
                            query.setParameterList(key, (Object[])criteriaConverter.criteriaMap.get(key));
                        } else {
                            query.setParameter(key, criteriaConverter.criteriaMap.get(key));
                        }
                    }
                }
                Object countObj = query.uniqueResult();
//                if (countObj instanceof Integer) {
//                    form.setNumberOfRows(((Integer) countObj).intValue());
//                } else if (countObj instanceof Long) {
//                    form.setNumberOfRows(((Long) countObj).intValue());
//                } else {
//                    form.setNumberOfRows(((BigInteger) countObj).intValue());
//                }

                if (countObj instanceof Integer) {
//                    System.out.println("INTEGER-----------");
                    form.setNumberOfRows(((Integer)countObj).intValue());
                } else if (countObj instanceof Long) {
//                    System.out.println("LONG-----------");
                    form.setNumberOfRows(((Long)countObj).intValue());
                } else if (countObj instanceof Number) {
//                    System.out.println("NUMBER-----------");
                    form.setNumberOfRows(((Number)countObj).intValue());
                }
//                form.setPageSize(Integer.parseInt(getParameter(request, "pageSize", form.getText("defaultPageSize", "10"))));
                form.setPageNo(Integer.parseInt(getParameter(request, "pageNo", "1")));
            }

            result = true;
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
//        } finally {
//            if (resultSet != null) {
//                resultSet.close();
//            }
//            if (statement != null) {
//                statement.close();
//            }
//
//            if (conn != null) {
//                //conn.close(); //use the connectionProvider to close instead of directly call close()
//                SessionFactoryImpl.closeConnection(conn);
//            }

            /*if (session.isConnected()){
            session.disconnect();
            }
            if (session.isOpen()){
            session.clear();
            session.flush();
            session.close();
            session.getSessionFactory().close();
            }*/
        }
        return result;
    }

    private String remove_as_(String ori) {
        if (ori == null) {
            return ori;
        }
        String temp = ori.toLowerCase();;
        if (temp.indexOf(" as ") >= 0) {
            int idxStart = 0;
            int lastIdx = 0;
            while (temp.indexOf(" as ") >= 0) {
                idxStart = temp.indexOf(" as ");
                lastIdx = temp.indexOf(",", idxStart + 5);
                if (lastIdx < 0) {
                    ori = ori.substring(0, idxStart);
                } else {
                    ori = ori.substring(0, idxStart) + ori.substring(lastIdx);
                }
                temp = ori.toLowerCase();
            }
        }
        return ori;
    }

    public String replaceCriteriaData(String condition) {
        String sFilter = ":_sessionFilter_";
        int indexOf = -1;
        int indexOfQuote = -1;
        int count = 0;
        String filterName = "";
        String paramName = "";
        Map webSession = ActionContext.getContext().getSession();
        while (condition.indexOf(sFilter) >= 0) {
            indexOf = condition.indexOf(sFilter);
            indexOfQuote = condition.indexOf("'", indexOf);
            filterName = condition.substring(indexOf, indexOfQuote);
            paramName = filterName.substring(sFilter.length()); //thensw: original using "16", change to use the .lenght()
            condition = condition.replaceAll(filterName, "" + webSession.get(paramName));
        }
        return condition;
    }

    public boolean retrieveData2(BaseActionSupport form,
            HttpServletRequest request, ServletContext context)
            throws Exception {
        boolean result = false;
        //org.hibernate.Session session = SessionFactoryImpl.getSession();
//        java.sql.Connection conn = SessionFactoryImpl.getConnection();
        Session session = form.hibernateSession();
        checkDbType(session);
        String sql = "";
        List<String> preTableList = new ArrayList();
        //Session session = new SessionFactoryImpl().getSession();
        try {
            criteriaConverter.criteriaMap.clear();
            if (isOracleDB) {
                criteriaConverter.setIsOracleDB();
            }
            //PersistentSession session = EMSPersistentManager.instance().getSession();
            //Map criterias = (Map) form.get

            //Query form.session.createSQLQuery(getSQL(request, context, form.getCriterias(), true, true, false));
            StringBuffer buffer = new StringBuffer();
            String distinctSelect = null;
            if (form.getRetrievingColumns().startsWith("distinct")) {
                distinctSelect = " group by " + form.getRetrievingColumns().substring(9);
            }
            buffer.append("SELECT ").append(form.getRetrievingColumns()).append(" FROM ").append(form.getSqlTables());
            StringBuffer condition = new StringBuffer();
            StringBuilder havingCondition = new StringBuilder();
            Map<String, String> setupMap = form.getSetupMap();
            if (setupMap.get("defaultSearchValue") != null) {
                if (setupMap.get("defaultSearchValue").toString().length() > 0) {
                    condition.append(" where (" + setupMap.get("defaultSearchValue").toString() + ") ");
                }
            }

            if (condition.toString().indexOf(":_sessionFilter_") >= 0) {
                String tmpCondition = replaceCriteriaData(condition.toString());
                condition.setLength(0);
                condition.append(tmpCondition);
            }

//            if (setupMap.get("defaultLookupSearchValue") != null){
//                if (setupMap.get("defaultLookupSearchValue").toString().length() > 0){
//                    if (condition.indexOf("where") >= 0){
//                        condition.append(" and (" + setupMap.get("defaultLookupSearchValue").toString() + ") ");
//                    } else {
//                        condition.append(" where (" + setupMap.get("defaultLookupSearchValue").toString() + ") ");
//                    }
//                }
//            }
            Map param = form.getSearchedParam();
            //Set joinSet = new HashSet();
            Debug.printFrameworkDebug("param = " + param);
            String joinTable = "";
            String strColumn = "";
            if (param.containsKey(SystemConstants.DYNAMIC_ATTS.CUSTOM_SQL)) {
                sql = (String) param.get(SystemConstants.DYNAMIC_ATTS.CUSTOM_SQL);
            } else {
                for (Object obj : param.keySet()) {
                    Debug.printFrameworkDebug("obj = " + obj);
                    if (obj.equals(SystemConstants.DYNAMIC_ATTS.SPECIAL_CRITERIA)) {
                        if (condition.length() <= 0) {
                            condition.append(" where ").append(param.get(SystemConstants.DYNAMIC_ATTS.SPECIAL_CRITERIA));
                        } else {
                            condition.append(" and ").append(param.get(SystemConstants.DYNAMIC_ATTS.SPECIAL_CRITERIA));
                        }
                        continue;
                    }
                    //                Debug.printFrameworkDebug(obj+":"+param.get(obj).toString());
                    if (((String) obj).startsWith("_having_")) {
                        if (((String) obj).indexOf(new String("|")) > 0) {
                            String newObj = ((String) obj).substring(8);
                            StringTokenizer moreColumns = new StringTokenizer((String) newObj, new String("|"));
                            StringBuilder currentHaving = new StringBuilder();
                            while (moreColumns.hasMoreTokens()) {
                                strColumn = moreColumns.nextToken().trim();
                                if (currentHaving.length() <= 0) {
                                    currentHaving.append(" (").append(criteriaConverter.strCriteria("", strColumn, param.get(obj).toString()));
                                } else {
                                    currentHaving.append(" or ").append(criteriaConverter.strCriteria("", strColumn, param.get(obj).toString()));
                                }
                            }
                            if (havingCondition.length() <= 0) {
                                havingCondition.append(" having ").append(currentHaving).append(")");
                            } else {
                                havingCondition.append(" and ").append(currentHaving).append(")");
                            }
                        } else {
                            if (havingCondition.length() <= 0) {
                                havingCondition.append(" having (").append(criteriaConverter.strCriteria("", ((String) obj).substring(8), param.get(obj).toString())).append(")");
                            } else {
                                havingCondition.append(" and (").append(criteriaConverter.strCriteria("", ((String) obj).substring(8), param.get(obj).toString())).append(")");
                            }
                        }
                        continue;
                    }
                    if (((String) obj).indexOf(new String("|")) > 0) {
                        StringTokenizer moreColumns = new StringTokenizer((String) obj, new String("|"));
                        StringBuffer moreConditions = new StringBuffer();
                        while (moreColumns.hasMoreTokens()) {
                            strColumn = moreColumns.nextToken().trim();
                            strColumn = criteriaConverter.replaceCriteriaComma(strColumn);
                            if (moreConditions.length() <= 0) {
                                moreConditions.append(criteriaConverter.strCriteria("", strColumn, param.get(obj).toString()));
                            } else {
                                moreConditions.append(criteriaConverter.strCriteria("or", strColumn, param.get(obj).toString()));
                            }
                            if (setupMap.get(strColumn + "_join") != null) {
                                if (setupMap.get(strColumn + "_join_preTable") != null) {
                                    String preTable = (String) setupMap.get(strColumn + "_join_preTable");
                                    if (setupMap.get(preTable) != null) {
                                        if (!preTableList.contains(preTable)) {
                                            preTableList.add(preTable);
                                            joinTable += " " + setupMap.get(preTable);
                                        }
                                    }
                                }
                                joinTable += " " + setupMap.get(strColumn + "_join").toString();
                            }
                        }
                        String temp = moreConditions.toString();
                        if (setupMap.get(strColumn + "_other_condition") != null) {
                            temp = (String) setupMap.get(strColumn + "_other_condition");
                            temp = temp.replace("$filterCondition", moreConditions);
                        }
                        if (condition.length() <= 0) {
                            condition.append(" where (").append(temp).append(")");
                        } else {
                            condition.append(" and (").append(temp).append(")");
                        }
                    } else { // 1 columns XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

                        Debug.printFrameworkDebug("here...................");
                        String filterCondition = criteriaConverter.strCriteria("", criteriaConverter.replaceCriteriaComma((String) obj), param.get(obj).toString());
//                        String filterCondition = criteriaConverter.strCriteria("", (String) obj, param.get(obj).toString());

                        Debug.printFrameworkDebug("DataRetriever : retrieveData2() filterCondition = " + filterCondition);
                        String temp = filterCondition;
                        if (setupMap.get(obj + "_other_condition") != null) {
                            temp = (String) setupMap.get(obj + "_other_condition");
                            temp = temp.replace("$filterCondition", filterCondition);
                        }
                        if (condition.length() <= 0) {
                            //condition.append(" where ").append(criteriaConverter.strCriteria("", (String) obj, param.get(obj).toString()));
                            condition.append(" where ").append(temp);
                        } else {
                            condition.append(" and ").append(temp);
                        }
                        if (setupMap.get(obj.toString() + "_join") != null) {
                            if (setupMap.get(obj.toString() + "_join_preTable") != null) {
                                String preTable = (String) setupMap.get(obj.toString() + "_join_preTable");
                                if (setupMap.get(preTable) != null) {
                                    if (!preTableList.contains(preTable)) {
                                        preTableList.add(preTable);
                                        joinTable += " " + setupMap.get(preTable);
                                    }
                                }
                            }
                            joinTable += " " + setupMap.get(obj.toString() + "_join").toString();
                        }
                    }
                }
                sql = buffer.toString() + joinTable + (condition.toString() == null ? "" : condition.toString());
            }

            int startNum = 1;
            //GROUP BY
            if (form.getDcGroupBy() != null && form.getDcGroupBy().length() > 0) {
                sql += " GROUP BY " + form.getDcGroupBy();
            }

            if (havingCondition.length() > 0) {
                sql += " " + havingCondition.toString();
            }

            // ORDER
            if (form.getSqlOrderBy() != null && form.getSqlOrderBy().length() > 0) {
                if (isOracleDB) {
                    if (form.getSqlOrderBy().indexOf("date") > 0) {
                        String tempOrder = " ORDER BY " + form.getSqlOrderBy();
                        sql += tempOrder;
                    } else {
                        String tempOrder = " ORDER BY lower(" + form.getSqlOrderBy();
                        tempOrder = tempOrder.replaceAll(" asc", " ) asc");
                        tempOrder = tempOrder.replaceAll(" desc", " ) desc");
                        sql += tempOrder;
                    }
                } else {
                    sql += " ORDER BY " + form.getSqlOrderBy();
                }
            }
//            Debug.printFrameworkDebug("DataRetriever form.getSqlOrderBy() " + form.getSqlOrderBy());
            Debug.printFrameworkDebug("sql: " + sql);
//            System.out.println("sql: " + sql);
//            Debug.printFrameworkDebug("DataRetriever : retrieveData2() sql = " + sql);

            // PAGING
//            pageSize = Integer.parseInt(getParameter(request, "pageSize", "10"));
//            pageNo = getPageNo(form, 1);
            // PAGING
//            pageSize = Integer.parseInt(getParameter(request, "pageSize", "10"));
//            pageNo = getPageNo(form, 1);
            pageSize = form.getPageSize();
            pageNo = form.getPageNo();
//            if (form.isPaging() && !isOracleDB) {System.out.println("###########");
            if (form.isPaging() &&  !dbType.equals("oracle")) {//serene @ 10/3/2022
                sql += getPagingStatement(request);
//            } else if (form.isPaging() && isOracleDB) { //serene @ 10/3/2022
            } else if (form.isPaging() && dbType.equals("oracle")) {
                startNum = ((pageNo - 1) * pageSize) + 1;
                sql = "select * "
                        + "from ( select a.*, rownum rnum "
                        + "from (" + sql + ") a "
                        + "where rownum < " + (startNum + pageSize) + " ) "
                        + "where rnum >= " + startNum;
            }
            //org.hibernate.Query query = session.createSQLQuery(sql);
//            for (Object objArr : query.list()){
//                for (Object obj : (Object[])objArr){
//                    Debug.printFrameworkDebug("class= "+ obj.getClass().getSimpleName() + ", " + obj);
//                }
//            }
//            Debug.printFrameworkDebug("DR2 Paging: " + sql);
            query = session.createSQLQuery(sql);
            if (criteriaConverter.populateCriteria) {
                for (String key : criteriaConverter.criteriaMap.keySet()) {
                    if (criteriaConverter.criteriaMap.get(key)!=null && criteriaConverter.criteriaMap.get(key).getClass().isArray()) {
                        query.setParameterList(key, (Object[])criteriaConverter.criteriaMap.get(key));
                    } else {
                        query.setParameter(key, criteriaConverter.criteriaMap.get(key));
                    }
                }
            }
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            List list = query.list();
//            for (Object obj : list) {
//                Map hmap = (Map) obj;
//                Debug.printFrameworkDebug("hmap = " + hmap);
//            }

//            statement = conn.prepareStatement(sql);
//            resultSet = statement.executeQuery();
//            copyTo(resultSet.getMetaData(), (List) form.getColumns());
//            copyTo(resultSet, (List) form.getResult());
            form.setResult(list);

            //Debug.printFrameworkDebug("size = " + form.getResult().size());
            if (form.getResult() != null && form.getResult().size() > 0 && form.isPaging()) {
                if (param.containsKey(SystemConstants.DYNAMIC_ATTS.CUSTOM_COUNT_SQL) || param.containsKey(SystemConstants.DYNAMIC_ATTS.CUSTOM_SQL)) {
                    if (!param.containsKey(SystemConstants.DYNAMIC_ATTS.CUSTOM_COUNT_SQL)) {
                        sql = "select count(*) from ("+(String) param.get(SystemConstants.DYNAMIC_ATTS.CUSTOM_SQL) + ") aa ";
                    } else {
                        sql = (String) param.get(SystemConstants.DYNAMIC_ATTS.CUSTOM_COUNT_SQL);
                    }
                } else {
                    buffer.setLength(0);
//                    buffer.append("SELECT count(*) FROM ").append(form.getSqlTables());
                    //wongkk4@24Feb2015
                    if (!Validator.isEmpty(form.getDcGroupBy())) {
                        buffer.append("SELECT " + form.getDcGroupBy() + " FROM ").append(form.getSqlTables());
                    } else {
                        buffer.append("SELECT count(" + form.getCountDistinctColumns() + ") FROM ").append(form.getSqlTables());
                    }
                    sql = buffer.toString() + joinTable + (condition.toString() == null ? "" : condition.toString());

                    if (distinctSelect != null) {
                        distinctSelect = remove_as_(distinctSelect);
                        sql = "select count(*) from (" + sql + (distinctSelect == null ? "" : distinctSelect)
                                + ((havingCondition.length() > 0) ? havingCondition.toString() : "")
                                + ") a";
                    }
                    if (!Validator.isEmpty(form.getDcGroupBy())) {
                        sql = "select count(*) from (" + sql + " GROUP BY " + form.getDcGroupBy()
                                + ((havingCondition.length() > 0) ? havingCondition.toString() : "")
                                + ") a";
                    }
                }
                Debug.printFrameworkDebug("countsql = " + sql);
                query = session.createSQLQuery(sql);
                if (criteriaConverter.populateCriteria) {
                    for (String key : criteriaConverter.criteriaMap.keySet()) {
                        if (criteriaConverter.criteriaMap.get(key)!=null && criteriaConverter.criteriaMap.get(key).getClass().isArray()) {
                            query.setParameterList(key, (Object[])criteriaConverter.criteriaMap.get(key));
                        } else {
                            query.setParameter(key, criteriaConverter.criteriaMap.get(key));
                        }
                    }
                }
                Object countObj = query.uniqueResult();
//                statement = conn.prepareStatement(sql);
//                Debug.printFrameworkDebug("Paging SQL = " + sql);
//                resultSet = statement.executeQuery();
//                resultSet.next();
//                form.setNumberOfRows(resultSet.getInt(1));
                if (countObj instanceof Integer) {
                    form.setNumberOfRows(((Integer) countObj).intValue());
                } else if (countObj instanceof Long) {
                    form.setNumberOfRows(((Long) countObj).intValue());
                } else {
//                    form.setNumberOfRows(((BigInteger) countObj).intValue());
                    form.setNumberOfRows(((BigDecimal) countObj).intValue()); // change to big decimal @ 27/2/2024
                }
                form.setPageSize(Integer.parseInt(getParameter(request, "pageSize", form.getText("defaultPageSize", "10"))));
                form.setPageNo(Integer.parseInt(getParameter(request, "pageNo", "1")));
            }

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            new LogFunction().logError(this.getClass(), "", e);
            throw new CustomBaseException("SQL Error");
        }
        return result;
    }

    public boolean retrieveDataDynamic(BaseActionSupport form, String dynamicAction, Map setupMap, Map param, boolean isPaging, List listColumns, List listResult,
            HttpServletRequest request, ServletContext context)
            throws Exception {
//                java.sql.Connection conn = SessionFactoryImpl.getConnection();
        Session session = form.hibernateSession();
        checkDbType(session);
        boolean result = false;
//		PreparedStatement statement = null;
//		ResultSet resultSet = null;
        String sql = "";
        List<String> preTableList = new ArrayList();
        String retrievingColumns = setupMap.get("retrievingColumns").toString();
        try {
            if (isOracleDB) {
                criteriaConverter.setIsOracleDB();
            }
            //PersistentSession session = EMSPersistentManager.instance().getSession();
            //Map criterias = (Map) form.get

            //Query form.session.createSQLQuery(getSQL(request, context, form.getCriterias(), true, true, false));
            StringBuffer buffer = new StringBuffer();
            buffer.append("SELECT ")
                    .append(setupMap.get("retrievingColumns").toString())
                    .append(" FROM ")
                    .append(setupMap.get("sqlTables").toString());
            StringBuffer condition = new StringBuffer();
            //Map setupMap = form.getSetupParam();
            //Map param = form.getSearchedParam();
            //Set joinSet = new HashSet();
            String joinTable = "";
            String strColumn = "";
            for (Object obj : param.keySet()) {
                if (((String) obj).indexOf(new String("|")) > 0) {
                    StringTokenizer moreColumns = new StringTokenizer((String) obj, new String("|"));
                    StringBuffer moreConditions = new StringBuffer();
                    while (moreColumns.hasMoreTokens()) {
                        strColumn = moreColumns.nextToken().trim();
                        if (moreConditions.length() <= 0) {
                            moreConditions.append(criteriaConverter.strCriteria("", strColumn, param.get(obj).toString().toLowerCase()));
                        } else {
                            moreConditions.append(criteriaConverter.strCriteria("or", strColumn, param.get(obj).toString().toLowerCase()));
                        }
                        //if (setupMap.get(strColumn+ "_join") != null){
                        //	joinTable += " " + setupMap.get(strColumn+ "_join").toString();
                        //}
                        if (setupMap.get(strColumn + "_join_preTable") != null) {
                            String preTable = (String) setupMap.get(strColumn + "_join_preTable");
                            if (setupMap.get(preTable) != null) {
                                if (!preTableList.contains(preTable)) {
                                    preTableList.add(preTable);
                                    joinTable += " " + setupMap.get(preTable);
                                }
                            }
                        }
                    }

                    String temp = moreConditions.toString();
                    if (setupMap.get(strColumn + "_other_condition") != null) {
                        temp = (String) setupMap.get(strColumn + "_other_condition");
                        temp = temp.replace("$filterCondition", moreConditions);
                    }
                    if (condition.length() <= 0) {
                        condition.append(" where (").append(temp).append(")");
                    } else {
                        condition.append(" and (").append(temp).append(")");
                    }
                } else { // 1 columns YYYYYYYYYYYYYYYYYYYYYYYYYYY
                    String filterCondition = criteriaConverter.strCriteria("", (String) obj, param.get(obj).toString().toLowerCase());
                    String temp = filterCondition;
                    if (setupMap.get(obj + "_other_condition") != null) {
                        temp = (String) setupMap.get(obj + "_other_condition");
                        temp = temp.replace("$filterCondition", filterCondition);
                    }
                    if (condition.length() <= 0) {
                        //condition.append(" where ").append(criteriaConverter.strCriteria("", (String)obj, param.get(obj).toString().toLowerCase()));
                        condition.append(" where ").append(temp);
                    } else {
                        condition.append(" and ").append(temp);
//                        condition.append(criteriaConverter.strCriteria("and", (String) obj, param.get(obj).toString().toLowerCase()));
                    }
                    //if (setupMap.get(obj.toString()+ "_join") != null){
                    //	joinTable += " " + setupMap.get(obj.toString()+ "_join").toString();
                    //}
                    if (setupMap.get(obj.toString() + "_join") != null) {
                        if (setupMap.get(obj.toString() + "_join_preTable") != null) {
                            String preTable = (String) setupMap.get(obj.toString() + "_join_preTable");
                            if (setupMap.get(preTable) != null) {
                                if (!preTableList.contains(preTable)) {
                                    preTableList.add(preTable);
                                    joinTable += " " + setupMap.get(preTable);
                                }
                            }
                        }
                        joinTable += " " + setupMap.get(obj.toString() + "_join").toString();
                    }

                }
                /*if (condition.length() <= 0){
					condition.append(" where ").append(criteriaConverter.strCriteria("", (String)obj, param.get(obj).toString().toLowerCase()));
				} else {
					condition.append(criteriaConverter.strCriteria("and", (String)obj, param.get(obj).toString().toLowerCase()));
				}
				if (setupMap.get(obj.toString()+ "_join") != null){
					joinTable += " " + setupMap.get(obj.toString()+ "_join").toString();
				}*/
            }

            sql = buffer.toString() + joinTable + (condition.toString() == null ? "" : condition.toString());

            // ThoTH @ 4-Mar-2013 :: Default Security Access
            Debug.printFrameworkDebug("dynamic2 sql = " + sql);


            //GROUP BY
            if (form.getDcGroupBy() != null && form.getDcGroupBy().length() > 0) {
                sql += " GROUP BY " + form.getDcGroupBy();
            }
            // ORDER
            //String sqlOrderBy = setupMap.get("sqlOrderBy")==null? "" : setupMap.get("sqlOrderBy").toString();
            //if (sqlOrderBy != null && sqlOrderBy.length() > 0) {
            //	sql += " ORDER BY " + sqlOrderBy;
            //}
            if (form.getSqlOrderBy() != null && form.getSqlOrderBy().length() > 0) {
                if (isOracleDB) {
                    String tempOrder = " ORDER BY lower(" + form.getSqlOrderBy();
                    tempOrder = tempOrder.replaceAll(" asc", " ) asc");
                    tempOrder = tempOrder.replaceAll(" desc", " ) desc");
                    sql += tempOrder;
                } else {
                    sql += " ORDER BY " + form.getSqlOrderBy();
                }
            } else {
                sql += " ORDER BY " + ((String) setupMap.get("displayFields")).split(",")[0];
            }

            Debug.printFrameworkDebug("Dynamic SQL:  " + sql);

            // PAGING
            //if (isPaging && !conn.getMetaData().getDatabaseProductName().equalsIgnoreCase("Oracle")) {
            //	sql += getDynamicPagingStatement(request, dynamicAction);
            //}
            int startNum = 1;
            pageSize = Integer.parseInt(getParameter(request, dynamicAction + "_pageSize", "10"));
            pageNo = Integer.parseInt(getParameter(request, dynamicAction + "_pageNo", "1"));
            if (form.isPaging() && !isOracleDB) {
                sql += getPagingStatement(request);
            } else if (form.isPaging() && isOracleDB) {
                startNum = ((pageNo - 1) * pageSize) + 1;
                sql = "select * "
                        + "from ( select a.*, rownum rnum "
                        + "from (" + sql + ") a "
                        + "where rownum < " + (startNum + pageSize) + " ) "
                        + "where rnum >= " + startNum;
            }
//			statement = conn.prepareStatement(sql);
//			Debug.printFrameworkDebug("Dynamic SQL = " + sql);
//			resultSet = statement.executeQuery();
//			copyTo(resultSet.getMetaData(), listColumns);
//			copyTo(resultSet, listResult);
            query = session.createSQLQuery(sql);
            if (criteriaConverter.populateCriteria) {
                for (String key : criteriaConverter.criteriaMap.keySet()) {
                    if (criteriaConverter.criteriaMap.get(key)!=null && criteriaConverter.criteriaMap.get(key).getClass().isArray()) {
                        query.setParameterList(key, (Object[])criteriaConverter.criteriaMap.get(key));
                    } else {
                        query.setParameter(key, criteriaConverter.criteriaMap.get(key));
                    }
                }
            }
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            List list = query.list();
//                        form.setResult(list);
            listResult.addAll(list);

            if (isPaging && listResult != null && listResult.size() > 0) {
                buffer.setLength(0);
                buffer.append("SELECT count(*) FROM ")
                        .append(setupMap.get("sqlTables").toString());
                sql = buffer.toString() + joinTable + (condition.toString() == null ? "" : condition.toString());
//				statement = conn.prepareStatement(sql);
//				resultSet = statement.executeQuery();
//				resultSet.next();

                query = session.createSQLQuery(sql);
                if (criteriaConverter.populateCriteria) {
                    for (String key : criteriaConverter.criteriaMap.keySet()) {
                        if (criteriaConverter.criteriaMap.get(key)!=null && criteriaConverter.criteriaMap.get(key).getClass().isArray()) {
                            query.setParameterList(key, (Object[])criteriaConverter.criteriaMap.get(key));
                        } else {
                            query.setParameter(key, criteriaConverter.criteriaMap.get(key));
                        }
                    }
                }
                Object countObj = query.uniqueResult();

                setupMap.remove(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO);
                setupMap.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO, Integer.parseInt(getParameter(request, dynamicAction + "_pageNo", "1")));

                setupMap.remove(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_SIZE);
                setupMap.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_SIZE, Integer.parseInt(getParameter(request, dynamicAction + "_pageSize", form.getText("defaultPageSize", "10"))));

                setupMap.remove(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO_OF_ROWS);
                if (countObj instanceof Integer) {
                    setupMap.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO_OF_ROWS, ((Integer) countObj).intValue());
                } else if (countObj instanceof Long) {
                    setupMap.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO_OF_ROWS, ((Long) countObj).intValue());
                } else {
                    form.setNumberOfRows(((BigInteger) countObj).intValue());
                    //added-wongkk4@15Jan2015 - Otherwise return Record 1 to null of null
                    setupMap.put(SystemConstants.DYNAMIC_ATTS.DYNAMIC_PAGE_NO_OF_ROWS, ((BigInteger) countObj).intValue());
                }
                //form.setDynamicNumberOfRows(dynamicAction, resultSet.getInt(1));
                //form.setDynamicPageSize(dynamicAction, Integer.parseInt(getParameter(request, "pageSize", form.getText("defaultPageSize", "10"))));
                //form.setDynamicPageNo(dynamicAction, Integer.parseInt(getParameter(request, "pageNo", "1")));
            }

            result = true;
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
//			if (resultSet != null) {
//				resultSet.close();
//			}
//			if (statement != null) {
//				statement.close();
//			}
//			if (conn != null){
//                            SessionFactoryImpl.closeConnection(conn);
//			}
        }
        return result;
    }

    public String getSQL(HttpServletRequest request, ServletContext context,
            Map criteriaMap, boolean paging, boolean optFilter, boolean singleEntity, boolean rowCount, boolean isOracleDB) {
        StringBuffer buffer = new StringBuffer();

        loadConfiguration(request, context);
        if (context.getAttribute(QUERY_CONFIG) == null) {
            loadConfiguration(request, context);
        }
        if (context.getAttribute(QUERY_CONFIG) != null) {
            Map pagingConfigMap = (Map) context.getAttribute(QUERY_CONFIG);

            String query = getParameter(request, QUERY, "");
            // to cater for tree structure
            if (singleEntity) {
                query = query.split("-")[0];
                query = "p" + query.substring(0, 1).toUpperCase() + query.substring(1);
            }
            if (pagingConfigMap.get(query) != null) {
                Map queryMap = (Map) pagingConfigMap.get(query);

                buffer.append("SELECT ");
                if (rowCount) {
                    buffer.append("COUNT(*)");
                } else {
                    buffer.append(queryMap.get(FIELDS));
                }
                buffer.append(" FROM ");
                buffer.append(queryMap.get(TABLES));

                String criterias = "";
                if (criteriaMap != null && !criteriaMap.isEmpty()) {
                    criterias = getCriterias(request, queryMap, criteriaMap);
                }

                String filters = (String) queryMap.get(FILTERS);
                String orders = (String) queryMap.get(ORDERS);
//				String sessionFilters = getSessionFilters(request, (String) queryMap.get(SESSION_FILTERS));

                if ((criterias.length() > 0) || (filters.length() > 0)) {
                    buffer.append(" WHERE ");
                    buffer.append(criterias);

                    // REQUEST FILTER
                    //if (criterias.length() > 0) {
                    //	buffer.append(" AND ");
                    //}
                    buffer.append(setFiltersParameters(request, filters, optFilter));
                    // SESSION FILTER
                    /*if (sessionFilters.length() > 0) {
						if ((criterias.length() > 0)
								|| (filters.length() > 0)) {
							buffer.append(" AND ");
						}
						buffer.append(sessionFilters);
					}*/
                }
                // ORDER
                if (orders != null && orders.length() > 0) {
                    if (isOracleDB) {
                        buffer.append(" ORDER BY lower(").append(orders).append(")");
                    } else {
                        buffer.append(" ORDER BY ").append(orders);
                    }
                }
                int pageSize = Integer.parseInt(getParameter(request, "pageSize", "10"));
                int pageNo = Integer.parseInt(getParameter(request, "pageNo", "1"));
                int startNum = ((pageNo - 1) * pageSize) + 1;
                if (paging && isOracleDB) {
                    buffer.insert(0, "select * from ( select a.*, rownum rnum from (")
                            .append(") a where rownum < " + (startNum + pageSize) + " ) "
                                    + "where rnum >= " + startNum);
                }
                // PAGING
                if (paging && !isOracleDB) {
                    buffer.append(getPagingStatement(request));
                }
            }
        }

        return buffer.toString();
    }

    private void loadConfiguration(HttpServletRequest request,
            ServletContext context) {
        Config config = new Config();
        /**
         * FIXME: use a constant
		 *
         */
        //final String fileHeader = "file:///";
        //String filename = context.getRealPath("/WEB-INF/config/query-config.xml");
        //filename = fileHeader+filename;
        //Map map = config.read(filename,
        //		new LookupConfigHandler());
        Map map = config.read(context.getResourceAsStream("/WEB-INF/config/query-config.xml"),
                new LookupConfigHandler());

        if (map != null) {
            context.setAttribute(QUERY_CONFIG, map);
        }
    }

    private String getParameter(HttpServletRequest request, String key,
            String defaultValue) {
        String result = defaultValue;

        if (!Validator.isEmpty(request.getParameter(key))) {
            result = request.getParameter(key);
        }

        return result;
    }

    private Integer getPageNo(BaseActionSupport form, Integer defaultValue) {
        Integer result = defaultValue;

        if (form.getPageNo() != null) {
            result = form.getPageNo();
        }

        return result;
    }

    private String getCriterias(HttpServletRequest request,
            Map queryMap, Map criterias) {
        StringBuffer buffer = new StringBuffer();
        String fields[] = new String[]{};

        if (queryMap != null && queryMap.size() > 0) {
            String filterFields = (String) queryMap.get(FILTER_FIELDS);
            if (!Validator.isEmpty(filterFields)) {
                fields = filterFields.split(",");
            }
        }

        Iterator iterator = criterias.keySet().iterator();

        String key;
        String mappedKey;
        String value;
        int index = 0;
        boolean accepted = false;

        while (iterator.hasNext()) {
            key = (String) iterator.next();

            if (fields.length > index) {
                mappedKey = fields[index];
            } else {
                mappedKey = key;
            }

            if (mappedKey != "" && criterias.containsKey(key)) {
                value = ((String) criterias.get(key)).trim();

                if (value != "") {
                    if (accepted) {
                        buffer.append(" AND ");
                    }
                    // to handle ambiguous fields with alias
                    if (mappedKey.indexOf("_@@") >= 0) {
                        // Add lookup for operator >
                        buffer.append(mappedKey.replace("_@@", "").replace("_", "."))
                                .append(mappedKey.indexOf("_@@") >= 0 ? " > " : " LIKE ")
                                .append("?");
                    } else {
                        buffer
                                //.append(mappedKey.indexOf("_@") >= 0 ? " = " : " LIKE ")
                                //.append("?");
                                .append(criteriaConverter.strCriteria("", mappedKey.replace("_@", ""), value.toLowerCase()));
                    }
                    accepted = true;
                }
            }

            index++;
        }

        return buffer.toString();
    }

    /*private String getSessionFilters(HttpServletRequest request, String sessionFilters)
    {
	Map sessionMap = ActionContext.getContext().getSession();
        StringBuffer buffer = new StringBuffer();
    	String filters[] = sessionFilters.split(",");
		Object obj = request.getSession().getAttribute(IConstants.USER_CONTAINER_KEY);

		if (obj != null)
		{
			//UserContainer container = (UserContainer) obj;
			boolean accepted = false;
			
                        for (int i = 0; i < filters.length; i++)
                        {
                                filters[i] = filters[i].trim();

                                if (accepted)
                                {
                                        buffer.append(" AND ");
                                        accepted = false;
                                }

//                                if (filters[i].toLowerCase().indexOf(ENTITY_ID.toLowerCase()) != -1)
                                if (filters[i].toLowerCase().indexOf(COMPANY_ID.toLowerCase()) != -1)
                                {
//                                        if (accepted = (container.getEntity() != null))
                                        if (accepted = (sessionMap.get(COMPANY_ID) != null))
                                        {
                                                //buffer.append(" " + filters[i] + "=" + container.getEntity().getSamentityId());
                                                buffer.append(" " + filters[i] + "=" + sessionMap.get(COMPANY_ID).toString());
                                        }
                                }
                        }
                }
    	Debug.printFrameworkDebug("getSessionFilters " + buffer.toString());
    	return buffer.toString();
    }*/
    private String getPagingStatement(HttpServletRequest request) {
        StringBuffer buffer = new StringBuffer();
        final int currentRow = (pageNo - 1)
                * pageSize;

        if (dbType.equals("mysql")) {
            buffer.append(" LIMIT " + currentRow + "," + pageSize);
        } else if (dbType.equals("sqlserver")) {
            buffer.append(" OFFSET " + currentRow + " rows FETCH NEXT " + pageSize + " rows only ");
        }
        /* Currently only MySQL is supported */
//        buffer.append(" LIMIT " + currentRow + "," + pageSize);

        // ThoTH @ 11-Jan-2013 - Support SQLServer
//                buffer.append(" OFFSET " + currentRow + " rows FETCH NEXT " + pageSize + " rows only ");
        return buffer.toString();
    }

    private String getDynamicPagingStatement(HttpServletRequest request, String dynamicAction) {
        int pageSize, pageNo;
        pageSize = Integer.parseInt(getParameter(request, dynamicAction + "_pageSize", "10"));
        pageNo = Integer.parseInt(getParameter(request, dynamicAction + "_pageNo", "1"));

        StringBuffer buffer = new StringBuffer();
        final int currentRow = (pageNo - 1)
                * pageSize;

        /* Currently only MySQL is supported */
        buffer.append(" LIMIT " + currentRow + "," + pageSize);

        // ThoTH @ 11-Jan-2013 - Support SQLServer
//                buffer.append(" OFFSET " + currentRow + " rows FETCH NEXT " + pageSize + " rows only ");
        return buffer.toString();
    }

    /*protected void setParameters(PreparedStatement statement, Map criterias)
			throws Exception {
		int i = 1;
		Collection collection = criterias.values();
		Iterator iterator = collection.iterator();
		
		Collection c = criterias.keySet();
		Iterator ite = c.iterator();
		String value, value2;

		while (iterator.hasNext()) {
			value = (String) iterator.next();
			value2 = (String) ite.next();
			if (value != "") {
				if(value2.indexOf("_@@") >= 0){
					statement.setString(i, value);
				}else if (value.indexOf("_@") >= 0) {
					statement.setString(i, value);
				} else {
					statement.setString(i, "%" + value + "%");
				}
				i++;
			}
		}
	}*/
    public void copyTo(ResultSetMetaData metaData, List columns)
            throws Exception {
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columns.add(metaData.getColumnLabel(i));
        }
    }

    public void copyTo(ResultSet resultSet, List result) throws Exception {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                Map map = new HashMap();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {

                    map.put(metaData.getColumnLabel(i).toLowerCase(), getData(resultSet, metaData,
                            i));
                }
                result.add(map);
            }
        } catch (Exception ex) {
            new LogFunction().logError(this.getClass(), "", ex);
        }
    }

    /**
     * Replace the filters value with parameters get from the request
     *
     * @param request
     * @param filters
     * @return String
     */
    private String setFiltersParameters(HttpServletRequest request, String filters, boolean optFilter) {

        String element;

        // extract the query parameters, surrounded with [] brackets
        List params = new ArrayList();
        String temp = filters;
        int start = 0;
        int end = 0;
        while ((start = temp.indexOf("[")) != -1) {
            end = temp.indexOf("]") + 1;
            params.add(temp.substring(start, end));
            temp = temp.substring(end);
        }
        String filterBy = request.getParameter(FILTER_BY);
        if (!optFilter) {
            filterBy = null; // overwrite
        }
        String[] filterValues = new String[]{};
        int idx = 0;
        if (filterBy != null) {
            filterValues = filterBy.split(",");
            // when the filter by's data got "," (comma) in their data, the lookup will take wrong value.
            // Solution is to convert the Comma to "%2C" from client side and convert back to "," before using it to do filtering. 
            for (String str : filterValues) {
                while (str.indexOf("%2C") >= 0) {
                    str = str.replace("%2C", ",");
                }
                filterValues[idx++] = str;
            }
        }

        // replace the parameters
        for (int i = 0; i < params.size(); i++) {
            element = temp = params.get(i).toString();
            if (i < filterValues.length) {
                // if LIKE expression, use wild-card match
                // if AND expression, use exact match
                if (filterValues[i].length() > 0) {
                    // replace LIKE
                    if (element.toUpperCase().indexOf("LIKE") >= 0) {
                        element = element.replaceAll("[?]", "'%" + filterValues[i] + "%'");
                    } // replace IS
                    else if (element.toUpperCase().indexOf(" IS ") >= 0) {
                        element = element.replaceAll("[?]", filterValues[i]);
                    } else {
                        if (element.toUpperCase().indexOf("IN") >= 0
                                && filterValues[i].indexOf(IConstants.Lookup.IN_DELIMITER) >= 0) {
                            filterValues[i] = filterValues[i].replaceAll("\\" + IConstants.Lookup.IN_DELIMITER, "','");
                        }

                        // EMS-631/TAG-103 - Added by Sim
                        // TODO: This checking is temporarily added to eliminate the addition of quote (' ') around the filter values
                        // Future enhancement should cater for certain scenarios which don't require ''
                        if (filterValues[i].length() > 7 && filterValues[i].substring(0, 7).equals("NOQUOTE")) {
                            element = element.replaceAll("[?]", filterValues[i].substring(7));
                        } else {
                            element = element.replaceAll("[?]", "'" + filterValues[i] + "'");
                        }
                        // EMS-631/TAG-103
                    }
                    filters = filters.replace(temp, element.substring(1, element.length() - 1));
                } else {
                    filters = filters.replace(element, "");
                }
            } else {
                filters = filters.replace(element, "");
            }
        }

        return filters;
    }

    private String getData(ResultSet resultSet, ResultSetMetaData metaData,
            int column) throws Exception {
        String result = "";
        String columnName = metaData.getColumnLabel(column);
//        Debug.printFrameworkDebug("DataRetriever: getDAta: " + columnName + " :: " + metaData.getColumnType(column));
        switch (metaData.getColumnType(column)) {
            case Types.CHAR:
                result = String.valueOf(resultSet.getString(columnName));
                break;

            case Types.NCHAR:
                result = String.valueOf(resultSet.getString(columnName));
                break;

            case Types.INTEGER:
                result = String.valueOf(resultSet.getInt(columnName));
                break;

            case Types.BIGINT:
                result = String.valueOf(resultSet.getInt(columnName));
                break;

            case Types.VARCHAR:
                result = resultSet.getString(columnName);
                break;

            case Types.NVARCHAR:
                result = resultSet.getString(columnName);
                break;

            case Types.LONGVARCHAR:
                result = resultSet.getString(columnName);
                break;

            case Types.DATE:
                result = Formatter.formatDate(resultSet.getDate(columnName));
                break;

            case Types.TIMESTAMP:
                if (getTimestampFormat() != null) {
                    result = Formatter.formatTimestamp(resultSet.getTimestamp(columnName), getTimestampFormat());
                } else {
                    result = Formatter.formatTimestamp(resultSet.getTimestamp(columnName));
                }
                break;

            case Types.DOUBLE:
                result = String.valueOf(resultSet.getDouble(columnName));
                break;
            case Types.DECIMAL:
                result = String.valueOf(resultSet.getBigDecimal(columnName));
                break;

            case Types.NUMERIC:
                if (columnName.toUpperCase().startsWith("LONG")) {
                    result = String.valueOf(resultSet.getLong(columnName));
                } else {
                    result = String.valueOf(resultSet.getDouble(columnName));
                }
                break;

            default:
                if (columnName.toUpperCase().startsWith("STR")) {
                    result = String.valueOf(resultSet.getString(columnName));
                } else if (columnName.toUpperCase().startsWith("LONG")) {
                    result = String.valueOf(resultSet.getLong(columnName));
                }
                break;
        }
        return result;
    }

    public String getTimestampFormat() {
        return timestampFormat;
    }

    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = timestampFormat;
    }
    
    public void retrieveData(Map dataMap, Map returnMap, BaseDAO dao) throws Exception{
        Map<String, String> param = (Map)dataMap.get("criteriaMap");
        String groupBy = (String)dataMap.get("groupBy");
        if (groupBy == null) {
            groupBy = "";
        } else {
            if (groupBy.toLowerCase().contains("group by")) {
                groupBy = " " + groupBy + " ";
            } else {
                groupBy = " group by " + groupBy + " ";
            }
        }
        String orderBy = (String)dataMap.get("orderBy");
        if (orderBy == null) {
            orderBy = "";
        } else {
            if (orderBy.toLowerCase().contains("order by")) {
                orderBy = " " + orderBy + " ";
            } else {
                orderBy = " order by " + orderBy + " ";
            }
        }
        String conditionStartWith = (String) dataMap.get("conditionStartWith");
        if (conditionStartWith == null) conditionStartWith = " where ";
        String strColumn = "";
        StringBuffer condition = new StringBuffer();
        if (param != null && param.keySet() != null && param.keySet().size() > 0) {
            for (String obj : param.keySet()) {
                if ((obj).indexOf(new String("|")) > 0) {
                    StringTokenizer moreColumns = new StringTokenizer((String) obj, new String("|"));
                    StringBuffer moreConditions = new StringBuffer();
                    while (moreColumns.hasMoreTokens()) {
                        strColumn = moreColumns.nextToken().trim();
                        strColumn = criteriaConverter.replaceCriteriaComma(strColumn);
                        if (moreConditions.length() <= 0) {
                            moreConditions.append(criteriaConverter.strCriteria("", strColumn, param.get(obj).toString().toLowerCase()));
                        } else {
                            moreConditions.append(criteriaConverter.strCriteria("or", strColumn, param.get(obj).toString().toLowerCase()));
                        }
                    }
                    String temp = moreConditions.toString();
                    if (condition.length() <= 0) {
                        condition.append(conditionStartWith).append(" (").append(temp).append(")");
                    } else {
                        condition.append(" and (").append(temp).append(")");
                    }
                } else { // 1 columns XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                    String filterCondition = criteriaConverter.strCriteria("", criteriaConverter.replaceCriteriaComma((String) obj), param.get(obj).toString().toLowerCase());
                    String temp = filterCondition;
                    if (condition.length() <= 0) {
                        //condition.append(" where ").append(criteriaConverter.strCriteria("", (String) obj, param.get(obj).toString().toLowerCase()));
                        condition.append(conditionStartWith).append(temp);
                    } else {
                        condition.append(" and ").append(temp);
                    }
                }
            }
        }
        
        String sql = " " +dataMap.get("sql");
        String countSql = (String) dataMap.get("countSql");
        if (countSql == null) {
            countSql = "select count(*) " + sql;
            sql = "select " + dataMap.get("columns") + " " + sql;
        }
        
        Debug.printFrameworkDebug("countSql = " + countSql + condition + groupBy);
        Long recordsTotal = dao.sqlCountRecord(countSql + condition + groupBy, criteriaConverter.criteriaMap);
        returnMap.put("recordsTotal", recordsTotal);
        returnMap.put("recordsFiltered", recordsTotal);
        
        if ((Long)returnMap.get("recordsTotal") > 0) {
            Debug.printFrameworkDebug("countSql = " + sql + condition + groupBy + orderBy);
            Debug.printFrameworkDebug("start = " + dataMap.get("start"));
            Debug.printFrameworkDebug("length = " + dataMap.get("length"));
            query = dao.getSession().createSQLQuery(sql + condition + groupBy + orderBy);
            if (criteriaConverter.populateCriteria) {
                for (String key : criteriaConverter.criteriaMap.keySet()) {
                    if (criteriaConverter.criteriaMap.get(key)!=null && criteriaConverter.criteriaMap.get(key).getClass().isArray()) {
                        query.setParameterList(key, (Object[])criteriaConverter.criteriaMap.get(key));
                    } else {
                        query.setParameter(key, criteriaConverter.criteriaMap.get(key));
                    }
                }
            }
            query.setFirstResult((Integer)dataMap.get("start"));
            query.setMaxResults((Integer)dataMap.get("length"));
            query.setResultTransformer(AliasToEntityOrderedMapResultTransformer.INSTANCE);
            returnMap.put("data", query.list());
        } else {
            returnMap.put("data", new ArrayList());
        }
    }
}
