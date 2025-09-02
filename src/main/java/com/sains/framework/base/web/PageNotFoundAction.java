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
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public class PageNotFoundAction extends BaseActionSupport<NullModel> implements ModelDriven<NullModel> {

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
    
    public PageNotFoundAction() {
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
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Map session = ActionContext.getContext().getSession();
        if (session.get("userId") == null) {
            return "expired";
        } else {
            if (!session.get("loginId").equals("admin")) {
                String code = DateUtil.getCurrentTimestamp_nano().toString();
                code = code.substring(code.length() - 6);
                addActionError("Page Not Found... : ErrorCode ("+ code +")");
                return "pageNotFound";
            }
        }
        
        if (session.get("lock") == null) {
            String code = null;
            if (request.getParameter("lock") == null) {
                code = DateUtil.getCurrentTimestamp_nano().toString();
                code = code.substring(code.length() - 6);
                addActionError("Page Not Found... : ErrorCode ("+ code +")");
                session.put("pnf", code);
                return "pageNotFound";
            } else { //checking for the lock
                code = (String)session.get("pnf");
                if (code == null) {
                    code = DateUtil.getCurrentTimestamp_nano().toString();
                    code = code.substring(code.length() - 6);
                    addActionError("Page Not Found... : ErrorCode ("+ code +")");
                    session.put("pnf", code);
                    return "pageNotFound";
                } else {
                    code.replaceFirst("^0+(?!$)", "");
                    if (!request.getParameter("lock").equals( (9876543-Integer.parseInt(code))+"" )) {
                        code = DateUtil.getCurrentTimestamp_nano().toString();
                        code = code.substring(code.length() - 6);
                        addActionError("Page Not Found... : ErrorCode ("+ code +")");
                        session.put("pnf", code);
                        return "pageNotFound";
                    } else {
                        session.put("lock", DateUtil.getCurrentTimestamp());
                    }
                }
            }
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String processUpdate() {
        Map session = ActionContext.getContext().getSession();
        if (session.get("loginId") == null) {
            return "expired";
        } else {
            if (!session.get("loginId").equals("admin")) {
                String code = DateUtil.getCurrentTimestamp_nano().toString();
                code = code.substring(code.length() - 6);
                return "pageNotFound";
            }
        }
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
                sqlstmt = sqlstmt.replaceAll(";", ""); //so that cannot run multiple sql
                
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
                        setPagingURL("processUpdatePNF?sqlstmt="+sqlstmt);
                        if (sqlstmt.trim().startsWith("_no_change ")) {
                            tempStmt = sqlstmt.substring(10);
                            if (tempStmt.indexOf(" LIMIT ") < 0) { //no paging statement.
//                            if (tempStmt.indexOf("rows FETCH NEXT") < 0) { //no paging statement.
//                                paging += " OFFSET " + ( getPageNo()==1?0:((getPageNo()-1)*getPageSize()) ) + " rows FETCH NEXT " + getPageSize() + " rows only";
                                  paging += " LIMIT " + ( getPageNo()==1?0:((getPageNo()-1)*getPageSize()) ) + "," + getPageSize();
                            }
                            query = dbaDAO.getSession().createSQLQuery(tempStmt);
//                            System.out.println(sqlstmt);
                        } else {
                           // tempStmt = "select Total_ = COUNT(*) OVER(), " + sqlstmt.substring(7);
//                            tempStmt = "select COUNT(*) OVER() as Total_, " + sqlstmt.substring(7);
                            tempStmt = sqlstmt;
                            if (tempStmt.indexOf("order by") < 0) {
                                paging = " order by 2 ";
                            }
                            if (tempStmt.indexOf(" LIMIT ") < 0) { //no paging statement.
//                            if (tempStmt.indexOf("rows FETCH NEXT") < 0) { //no paging statement.
//                                paging += " OFFSET " + ( getPageNo()==1?0:((getPageNo()-1)*getPageSize()) ) + " rows FETCH NEXT " + getPageSize() + " rows only";
                                paging += " LIMIT " + ( getPageNo()==1?0:((getPageNo()-1)*getPageSize()) ) + "," + getPageSize();
                            }
                            query = dbaDAO.getSession().createSQLQuery(tempStmt + paging);
                            Debug.printDebug("" + tempStmt + paging);
                        }
//                        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
                        query.setResultTransformer(AliasToEntityOrderedMapResultTransformer.INSTANCE);
                        setResult(query.list());
                        if (getResult() != null && getResult().size() > 0) {
                            try {
//                                setNumberOfRows( Integer.parseInt( ((Map)getResult().get(0)).get("Total_").toString()) );
                                setNumberOfRows( Integer.parseInt( dbaDAO.sqlCountRecord("select count(*) from ("+sqlstmt+") a", null).toString()) );
                            } catch (Exception e) {
                            }
                        }
                        if (connectionStatus.indexOf("** begin transaction **") < 0) {
                            connectionStatus = "** opened **";
                        }
                    } else if (sqlstmt.trim().startsWith("create view") || sqlstmt.trim().startsWith("drop view")) {
                        query = dbaDAO.getSession().createSQLQuery(sqlstmt);
                        try {
                            dbaDAO.beginBatchTransaction();
                            query.executeUpdate();
                            dbaDAO.commitBatchTransaction();
                        } catch (Exception e) {
                            dbaDAO.rollbackBatchTransaction();
                        }
                    } else {
                        addActionError("not allow...");
//                        query = dbaDAO.getSession().createSQLQuery(sqlstmt);
//                        try {
//                            dbaDAO.beginBatchTransaction();
//                            connectionStatus = "** begin transaction **";
//                            query.executeUpdate();
//                        } catch (Exception e) {
//                            dbaDAO.rollbackBatchTransaction();
//                            throw e;
//                        }
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
