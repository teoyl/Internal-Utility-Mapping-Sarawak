/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend;

import com.sains.framework.base.BaseAction;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.LogFunction;
import com.sains.framework.base.ServiceFactory;
import com.sains.framework.base.SessionFactoryImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LRBase extends BaseAction {
//    protected Session session = null;
    protected String workingDiv = "01";
    protected PreparedStatement statement = null;
    protected ResultSet resultSet = null;
    protected java.sql.Connection conn = null;
    protected java.sql.Connection conn_new = null;

    protected List param_ = new ArrayList();
    protected String countColumn_ = "count(*)";
    protected StringBuffer dataColumn_ = new StringBuffer();
    protected StringBuffer sqlStmt_ = new StringBuffer();

    private ServiceFactory serviceFactory = ServiceFactory.getInstance();

    protected BaseDAO retrieverDAO = new BaseDAOImpl();
    
    public org.hibernate.SQLQuery query = null;

//    private boolean loadCount = Boolean.TRUE;
//    private boolean traceCount = Boolean.FALSE;
//
//    private static Integer openCount = 0;
//    private static Integer closeCount = 0;

    // Created by ThoTH @ 13-Sep-2011
    public void getConnection() {
        if(conn == null){
//            conn = getSession().connection();
            conn = SessionFactoryImpl.getConnection();
        }
    }
    
    //AiMin @29.11.2017
    public java.sql.Connection getConnection_new() {
        if(conn_new == null){
//            System.out.println("conn_new is null");
            conn_new = SessionFactoryImpl.getConnection();
//            System.out.println("conn new is set!!!!");
        }
        return conn_new;
    }


    // Created by ThoTH @ 13-Sep-2011
    // ThoTH @ 12-Dec-2013 :: Issue Rollback, to free the cursor
    public void closeAll() {
        try {statement.close();} catch (Exception e4) {}
        try {resultSet.close();} catch (Exception e2){}
//            try {conn.close();}      catch (Exception e3){}
//        if (loadCount) {
//            if (traceCount) {
//                System.out.print("@@@ --- : " + traceCaller() + " CLOSED - (" + (++closeCount) +")");
//            } else {
//                System.out.print("@@@ --- : " + this.getClass().getName() + " CLOSED - (" + (++closeCount) +")");
//            }
//            System.out.println("connection@"+System.identityHashCode(conn));
//        }
        try {
            retrieverDAO.closeSession();
            SessionFactoryImpl.closeConnection(conn);
            conn = null;
        }      catch (Exception e3){}
//        try {closeSession(); conn = null;}      catch (Exception e3){}
        // Clean
        param_.clear();
        dataColumn_.setLength(0);
        sqlStmt_.setLength(0);
    }
    
    //AiMin @29.11.2017
    public void closeAll_new() {
        try {
            SessionFactoryImpl.closeConnection(conn_new);
            conn_new = null;
        }      catch (Exception e3){}
    }

    // Created by ThoTH @ 1-Aug-2013
    public void closeAllExcludeConn() {
        try {statement.close();} catch (Exception e4) {}
        try {resultSet.close();} catch (Exception e2){}
//        try {
//            retrieverDAO.closeSession();
//            SessionFactoryImpl.closeConnection(conn);
//            conn = null;
//        }      catch (Exception e3){}
//        try {closeSession(); conn = null;}      catch (Exception e3){}
        cleanSql();
    }

    // ThoTH @ 13-Jan-2012
    public void cleanSql() {
        param_.clear();
        dataColumn_.setLength(0);
        sqlStmt_.setLength(0);
    }
    
    /**
     * Mainly for Insert/Update TRS Table which don't have PK
     *
     * @param param
     * @param sqlStmt
     * @throws Exception
     *
     * Created by ThoTH @ 12-Sep-2011
     */
    public static Integer tthCount = 0;
    public void sqlUpdate(List param, String sqlStmt) throws Exception{
        try {
            //System.out.println("tthCount: " + ++tthCount );
          getConnection();
//            if(conn == null){
//                conn = getSession().connection();
//            }
            statement = conn.prepareStatement(sqlStmt);
            setParameter(param, statement);

//            System.out.println("sql:"+sqlStmt); 

            statement.executeUpdate();
        } catch (Exception e) {
	    new LogFunction().logError(this.getClass(),"param "+ param + " " +sqlStmt.toString() ,e);
            CommonFunction.writeFile("Post2RVS", "[ERROR] >>> Post2RVS Running with Exception :: " +e.getMessage()+"param "+ param + " " +sqlStmt.toString());
            throw e;
        } finally {
            closeAllExcludeConn();
        }
    }
    
    public void setParameter(List param, PreparedStatement stmt) throws Exception{
        int i = 1;
        String paramType = "";
        for (Object obj : param){
            if (obj == null) {
                stmt.setNull(i++, 12);  // added by ThoTH @ 14-Sep-2011, to cater for Null Parameter
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



}
