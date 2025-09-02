/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.utimaps.model.JobDetailModel;
import com.utimaps.model.MiscellaneousPlanModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Filter;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.query.ParameterMetadata;

/**
 *
 * @author yonglai
 */
public class MiscellaneousPlanAction extends BaseActionSupport<MiscellaneousPlanModel> implements ModelDriven<MiscellaneousPlanModel>{
    
    private Map<String, Object> jsonMap = new HashMap();
    HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    
    public MiscellaneousPlanAction() {
        model = new MiscellaneousPlanModel();
    }
    
    @Override
    public MiscellaneousPlanModel getModel() {
        return model;
    }
    
    public String loadMainPage() {
        setPageTitle_("");
        
        return "load_main_page";
    }
    
    @Override
    public String loadAddPage() {
        Debug.printDebug("loadAddPage");
        try {
            setPageTitle_("Miscellaneous Plan");
            setPageSubTitle_("Add");

            setUserDiv_();
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "MiscellaneousPlanAction", "MiscellaneousPlanAction", "loadAddPage");
        }
        
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }
    
    @Override
    public String loadEditPage() {
        Debug.printDebug("loadEditPage");
        try {
            BaseDAO dao = new BaseDAOImpl();
            dao.setSession(baseDAO.getSession());
            dao.getSession().enableFilter("statusFilter");
            setPageTitle_("USJ Miscellaneous Plan");
            setPageSubTitle_("Edit");
            model = super.processEdit(getModel());
            setUserDiv_();
            
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "MiscellaneousPlanAction", "MiscellaneousPlanAction", "loadEditPage");
        }
        
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }
    
    public String processInsert() {
        BaseDAO baseDAO = new BaseDAOImpl();
        try {
            validateRequired();
            specificValidation("insert");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.INSERT_FAIL;
            }
            
            serviceFactory.getMiscPlanService().insert(getModel());
            id = model.getID();
            addActionMessage(getText("createSuccess"));

        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "MiscellaneousPlanAction", "FieldQueryAction", "processInsert");
            addActionMessage(getText("createFail"));
            return SystemConstants.ACTION_Status.INSERT_FAIL;
        } finally {
            baseDAO.closeSession();
        }
        return returnStr;
    }
    
    public String processUpdate() {
        try {
            
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }

            id = model.getID();
            serviceFactory.getMiscPlanService().update(model);
            addActionMessage(getText("updateSuccess"));
            
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "FieldQueryAction", "FieldQueryAction", "processUpdate");
            return "edit_fail";
        }

        return returnStr;
    }
    
    public String loadSearchPage() {
        Debug.printDebug("loadSearchPage");
        try {
            setPageTitle_("USJ Miscellaneous Plan");
            setPageSubTitle_("Search");
            
            search();
            
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "MiscellaneousPlanAction", "MiscellaneousPlanAction", "loadSearchPage");
        }
        
        return "load_search_page";
    }
    
    private String usjDiv_ = "";
    public String getUsjDiv_() {
        return usjDiv_;
    }

    public void setUsjDiv_(String usjDiv_) {
        this.usjDiv_ = usjDiv_;
    }
    
    
    public String search() {
        Debug.printDebug("search");
        BaseDAO baseDAO = new BaseDAOImpl();
        try {
            setPageTitle_("USJ Miscellaneous Plan");
            setPageSubTitle_("Search");
            
            setUserDiv_();
            
            String plan_no = Validator.isEmpty(request.getParameter("plan_no")) ? "" : request.getParameter("plan_no");
            String usjDiv = Validator.isEmpty(usjDiv_) ? "" : usjDiv_;
            String usjSeq = Validator.isEmpty(request.getParameter("usj_seq")) ? "" : request.getParameter("usj_seq");
            String usjYear = Validator.isEmpty(request.getParameter("usj_year")) ? "" : request.getParameter("usj_year");
            String planTitle = Validator.isEmpty(request.getParameter("title")) ? "" : request.getParameter("title");
            System.out.println("userDiv_ " + userDiv_);
            Map param = new HashMap();
            param.clear();
            if (!Validator.isEmpty(plan_no))
                param.put("plan_no", plan_no);
            if (!Validator.isEmpty(usjDiv))
                param.put("usj_div", usjDiv);
            if (!Validator.isEmpty(usjSeq))
                param.put("usj_seq", usjSeq);
            if (!Validator.isEmpty(usjYear))
                param.put("usj_year", usjYear);
            if (!Validator.isEmpty(planTitle))
                param.put("plan_title", planTitle);
            if (!Validator.isEmpty(userDiv_)) {
                if(!userDiv_.equals("00")) {
                    param.put("created_div", userDiv_);
                }
            }
            
            miscPlanList = baseDAO.list_order(param, MiscellaneousPlanModel.class, "order by created_date desc");
            System.out.println("miscPlanList " + miscPlanList);
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "MiscellaneousPlanAction", "MiscellaneousPlanAction", "search");
        } finally {
            baseDAO.closeSession();
        }
        
        return "load_search_page";
    }
    
    public List<Options> getUSJYearList() {
        return commList.getCurrentYearUpToFiveYear();
    }
    
    public List<Options> get_DivisionList() {
        return commList.getAllDivisionList();
    }
    
    private boolean isHQUser = false;
    public boolean isIsHQUser() {
        return isHQUser;
    }

    public void setIsHQUser(boolean isHQUser) {
        this.isHQUser = isHQUser;
    }
    
    private String userDiv_;
    public String getUserDiv_() {
        return userDiv_;
    }

    public void setUserDiv_(String userDiv_) {
        this.userDiv_ = userDiv_;
    }
    
    private String id = "";
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public List<MiscellaneousPlanModel> miscPlanList = new ArrayList();
    public List<MiscellaneousPlanModel> getMiscPlanList() {
        return miscPlanList;
    }

    public void setMiscPlanList(List<MiscellaneousPlanModel> miscPlanList) {
        this.miscPlanList = miscPlanList;
    }
    
    public void setUserDiv_() {
        Map sessionMap = ActionContext.getContext().getSession();
        String divAssignedList = sessionMap.get("div_assigned").toString();
        String formattedDivList = divAssignedList.replace("[", "").replace("]", "");
        formattedDivList = formattedDivList.replace(" ", "").replace("]", "");

        String[] stringArray = formattedDivList.split(",");
        List<String> divList = new ArrayList<>();
        for (String str : stringArray) {
            divList.add(str);
        }

        if(divList.contains("00")) 
            isHQUser = true;

        if(!divList.isEmpty()) {
            if(divList.size() > 1) {
                isHQUser = true;
            } else {
                userDiv_ = divList.get(0);
            }
        }
    }
    
    private String strTrnDiv;
    public String getStrTrnDiv(String strDiv) {
            if(Validator.isEmpty(strDiv)){
                strTrnDiv = "-";
            }else{
                switch(strDiv) {
                    case "01":
                        strTrnDiv = "Kuching";
                        break;
                    case "02":
                        strTrnDiv = "Sri Aman";
                        break;
                    case "03":
                        strTrnDiv = "Sibu";
                        break;
                    case "04":
                        strTrnDiv = "Miri";
                        break; 
                    case "05":
                        strTrnDiv = "Limbang";
                        break;
                    case "06":
                        strTrnDiv = "Sarikei";
                        break;
                    case "07":
                        strTrnDiv = "Kapit";
                        break;
                    case "08":
                        strTrnDiv = "Kota Samarahan";
                        break;
                    case "09":
                        strTrnDiv = "Bintulu";
                        break;
                    case "10":
                        strTrnDiv = "Mukah";
                        break;
                    case "11":
                        strTrnDiv = "Betong";
                        break;
                    case "12":
                        strTrnDiv = "Serian";
                        break;    
                    default:
                        break; 
                }
            }
        return strTrnDiv;
    }
    
    public String cancel() {
        return "cancel";
    }
}
