package com.sains.framework.base.web;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.DateUtil;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.AliasToEntityOrderedMapResultTransformer;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.Debug;
import com.sains.framework.model.NullModel;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

public class DbAction extends BaseActionSupport<NullModel> implements ModelDriven<NullModel> {

    private static final long serialVersionUID = -6659925652584240545L;
    private String sqlstmt = null;
    private String userAction = null;
    private String connectionStatus = "-";
    private static BaseDAOImpl dbaDAO = null;
    
    public String getSqlstmt() {
        return sqlstmt;
    }
    public void setSqlstmt(String sqlstmt) {
        this.sqlstmt = sqlstmt;
    }

    public String getUserAction() {
        return userAction;
    }
    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
    
    public DbAction() {
        // change the DAO to the correct service.
        //baseDAO = new BaseDAOImpl();
        model = new NullModel();
        // set the required field for common check.
        //getRequiredParam().put("column_name1", "column.name1");
        //getRequiredParam().put("column_name2", "column.name2");


    }

    @Override // change the "String" and return value to the
    public NullModel getModel() {
        return model;
    }

    public void specificValidation(String validationType) {
    }

    public String processInsert() {
        return loadEditPage();
    }

    public String loadAddPage() {
        return loadEditPage();
    }

    //retrieve data for editing.
    public String loadEditPage() {
        
//        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//        Map session = ActionContext.getContext().getSession();
//        if (session.get("lock") == null) {
//            String code = null;
//            if (request.getParameter("lock") == null) {
//                code = DateUtil.getCurrentTimestamp_nano().toString();
//                code = code.substring(code.length() - 6);
//                addActionError("Page Not Found... : ErrorCode ("+ code +")");
//                session.put("dba", code);
//                return "pageNotFound";
//            } else { //checking for the lock
//                code = (String)session.get("dba");
//                if (code == null) {
//                    code = DateUtil.getCurrentTimestamp_nano().toString();
//                    code = code.substring(code.length() - 6);
//                    addActionError("Page Not Found... : ErrorCode ("+ code +")");
//                    session.put("dba", code);
//                    return "pageNotFound";
//                } else {
//                    code.replaceFirst("^0+(?!$)", "");
//                    if (!request.getParameter("lock").equals( (9876543-Integer.parseInt(code))+"" )) {
//                        code = DateUtil.getCurrentTimestamp_nano().toString();
//                        code = code.substring(code.length() - 6);
//                        addActionError("Page Not Found... : ErrorCode ("+ code +")");
//                        session.put("dba", code);
//                        return "pageNotFound";
//                    } else {
//                        session.put("lock", DateUtil.getCurrentTimestamp());
//                    }
//                }
//            }
//        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String processUpdate() {
        String loadEditPageStr = loadEditPage();
        if (!loadEditPageStr.equals(SystemConstants.ACTION_Status.LOAD_EDIT_PAGE)) {
            return loadEditPageStr;
        }
        if (userAction != null && userAction.equals("commit")) {
            try {
                dbaDAO.commitBatchTransaction();
            } catch (Exception e) {
                addActionError(e.getCause().toString());
                return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
            }
            connectionStatus = "** transaction committed **";
        } else if (userAction != null && userAction.equals("rollback")) {
            connectionStatus = "** transaction rollback **";
            dbaDAO.rollbackBatchTransaction();
        } else if (userAction != null && userAction.equals("closeConnection")) {
            if (dbaDAO != null) {
                dbaDAO.closeSession();
            }
        } else {
            try {
                if (dbaDAO == null) {
                    dbaDAO = new BaseDAOImpl();
                }
                if (sqlstmt.endsWith(";")) {
                    sqlstmt = sqlstmt.replaceAll(";", "");
                }
                if (Validator.isEmpty(sqlstmt)) {
                    addActionError("Empty sql...");
                } else {
                    SQLQuery query = null;
                    if (sqlstmt != null) {
                        sqlstmt = sqlstmt.replaceAll("ZXZ","%");
                    }
                    if (sqlstmt.trim().startsWith("select ") || sqlstmt.trim().startsWith("_no_change ")) {
                        String tempStmt = null;
                        String paging = "";
                        setPagingURL("processUpdateDBA?sqlstmt="+sqlstmt);
                        if (sqlstmt.trim().startsWith("_no_change ")) {
                            tempStmt = sqlstmt.substring(10);
                            if (tempStmt.indexOf("rows FETCH NEXT") < 0) { //no paging statement.
                                paging += " OFFSET " + ( getPageNo()==1?0:((getPageNo()-1)*getPageSize()) ) + " rows FETCH NEXT " + getPageSize() + " rows only";
                            }
                            query = dbaDAO.getSession().createSQLQuery(tempStmt);
    //                        System.out.println(sqlstmt);
                        } else {
                           // tempStmt = "select Total_ = COUNT(*) OVER(), " + sqlstmt.substring(7);
                            tempStmt = "select " + sqlstmt.substring(7);
                            if (tempStmt.indexOf("order by") < 0) {
                                paging = " order by 1 ";
                            }
                            if (tempStmt.indexOf("rows FETCH NEXT") < 0) { //no paging statement.
                                paging += " OFFSET " + ( getPageNo()==1?0:((getPageNo()-1)*getPageSize()) ) + " rows FETCH NEXT " + getPageSize() + " rows only";
                            }
                            query = dbaDAO.getSession().createSQLQuery(tempStmt + paging);
                            Debug.printDebug("" + tempStmt + paging);
                        }
                        query.setResultTransformer(AliasToEntityOrderedMapResultTransformer.INSTANCE);
                        setResult(query.list());
                        if (getResult() != null && getResult().size() > 0) {
                            try {
                                setNumberOfRows( Integer.parseInt( ((Map)getResult().get(0)).get("Total_").toString()) );
                            } catch (Exception e) {
                            }
                        }
                        if (connectionStatus.indexOf("** begin transaction **") < 0) {
                            connectionStatus = "** opened **";
                        }
                    } else {
//                        addActionError("not allow...");
                        query = dbaDAO.getSession().createSQLQuery(sqlstmt);
                        System.out.println("sqlstmt " + sqlstmt);
                        try {
                            dbaDAO.beginBatchTransaction();
                            connectionStatus = "** begin transaction **";
                            query.executeUpdate();
                        } catch (Exception e) {
                            dbaDAO.rollbackBatchTransaction();
                            throw e;
                        }
                    }
                    if (sqlstmt != null) {
                        sqlstmt = sqlstmt.replaceAll("%", "ZXZ");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                addActionError(e.getCause().toString());
            } finally {
                dbaDAO.closeSession();
            }
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String delete() {
        return loadEditPage();
    }
    
    @Override
    public Integer getPageSize() {
        if (super.getPageSize() == null) {
            return 50;
        }
        return super.getPageSize();
    }
    
    @Override
    public Integer getPageNo() {
        if (super.getPageNo() == null) {
            return 1;
        }
        return super.getPageNo();
    }
    
    private Integer currentRow = null;
    public Integer getCurrentRow() {
        if (currentRow == null) {
            return 1;
        }
        return currentRow;
    }
    public void setCurrentRow(Integer currentRow) {
        this.currentRow = currentRow;
    }
    
}
