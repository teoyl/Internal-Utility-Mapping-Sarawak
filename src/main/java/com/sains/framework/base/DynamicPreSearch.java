package com.sains.framework.base;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.CriteriaConverter;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.web.DynamicAction;
import com.sains.framework.lookup.LookupAction;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

public class DynamicPreSearch{
    public static int count = 0;
    public String preLookupSearchModule(LookupAction lookupAction, Map param) throws Exception{
//        param.put(SystemConstants.DYNAMIC_ATTS.SPECIAL_CRITERIA, "t_setup_module.module_icon like '%fa%'");
        return ActionSupport.SUCCESS;
    }
    public String lookupModule_access(LookupAction lookupAction, Map param) throws Exception{
//        if (1==1) {
//            throw new CustomBaseException("cannot do this lookup");
//        }
        return ActionSupport.SUCCESS;
    }
    public String preSearchModule(DynamicAction dynamicAction, Map param) throws Exception{
        //sample
//        param.put(SystemConstants.DYNAMIC_ATTS.CUSTOM_SQL, "select t_setup_module.module_id, case when (t_setup_module.module_type = 'S') then 'Sub Module' else 'Module' end as strmoduletype, t_setup_module.module_code, t_setup_module.module_name, t_setup_module.module_type from t_setup_module");
        //throw new DynamicPreSearchException("errors.atleastone", "1");
        //throw new DynamicPreSearchException("errors.systemException");
        return ActionSupport.SUCCESS;
    }

    // ThoTH @ 17-May-2013 :: Default Security Access :: Copy & Modified from DataRetriever
//    private String appendSecurityCondition() throws Exception {
//        String strSql = "";
//        Integer intSecuAL  = (Integer) ActionContext.getContext().getSession().get("secuAL");
//        String strSecuDeptBU = (String) ActionContext.getContext().getSession().get("secuDeptBU");
//
//        String strOperator = " and ";
////        if (condition == null || condition.indexOf("where") < 0) {
////            strOperator = " where ";
////        } else {
////            strOperator = " and ";
////        }
//
//        if (intSecuAL == 8) { // Exclude SAINS only
//            strSql = strOperator + "exists (select su.emp_id from t_setup_user su, t_employee emp2 " +
//                                   "         where su.emp_id = emp2.employee_id and su.us_accesslevel <= 8) ";
//
//        } else if (intSecuAL < 8) {  // View all in saeme Department
//            strSql = strOperator + "bu.est_dept_id = '"+strSecuDeptBU+"' " ;
//        }
//
//        return strSql;
//    }
    
    public String prePostQuery(DynamicAction dynamicAction, Map param) throws Exception {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
         String strSql = "SELECT "+
                            "emp.employee_id, emp.emp_name, emp.emp_nw_ic_no, "+
                            "postInfo.post_info_id, postOper.post_oper_id, "+
                            "case when(lantik.lantik_id is null) then 'Tiada Jawatan' else dept.dept_shortname + ':' + postName_ba.post_name + ' [' + postInfo.post_id + ']' end as ba_post, "+
//                            "case when(pp.pp_id is null) then 'Tiada Jawatan' else dept_bu.dept_shortname + ':' + postName_bu.post_name + ' [' + postOper.post_id + ']' end as bu_post "+
                            "case when(pp.pp_id is null) then 'Tiada Sandangan' else dept_bu.dept_shortname + ':' + postName_bu.post_name + ' [' + bu.est_no + ']' end as bu_post "+
                            "FROM t_employee emp "+
                            "left outer join t_pt_post_lantik lantik on emp.employee_id = lantik.employee_id "+
                            "left outer join t_pt_post_info postInfo on lantik.post_info_id = postInfo.post_info_id "+
                            "left outer join t_pt_post_name postName_ba on postInfo.post_name_id = postName_ba.post_name_id "+
                            "left outer join t_pt_establishment ba on postInfo.est_id = ba.est_id "+
                            "left outer join t_pt_department dept on dept.dept_id = ba.est_dept_id "+
                            "left outer join t_cm_personal_post pp on emp.employee_id = pp.employee_id "+
                            "left outer join t_pt_post_operation postOper on pp.post_oper_id = postOper.post_oper_id "+
                            "left outer join t_pt_post_name postName_bu on postOper.post_name_id = postName_bu.post_name_id "+
                            "left outer join t_pt_establishment bu on postOper.est_id = bu.est_id "+
                            "left outer join t_pt_department dept_bu on dept_bu.dept_id = bu.est_dept_id  "+ 
                            "where emp.emp_type = 'PAN'";
         String strSqlCount = "";

         String strSearch = "";
         String strSearchPostId = null;
         String strSearchPostOperId = null;

         CriteriaConverter criteriaConverter = new CriteriaConverter();
         int index = 0;
         for (String str : (List<String>)dynamicAction.getSearchFields()) {
             String data = request.getParameter("search_" + str);
             if (!Validator.isEmpty(data)) {
                 String dbName = (String)dynamicAction.getSearchFieldsDbName().get(index);
                 strSearch += criteriaConverter.strCriteria("and", dbName, data);
             }
             index++;
         }
         //thensw@ 14 Nov 2013 :: This is to populate the searchCondition for CUSTOM_SQL & CUSTOM_COUNT_SQL after cancel/kembali is click
         // reason: if search is clicked, it will find the search criteria using request.getParameter("search"+... which when cancel is clicked...
         //         the search criteria is restore back from session and without "search_" in front.
         if (strSearch.equals("")) {
             index = 0;
             for (Object key : param.keySet()) {
                 if (!Validator.isEmpty(param.get(key)+"")) {
                     for (String str : (List<String>)dynamicAction.getSearchFields()) {
                         if (str.equals(key+"")) {
                             String dbName = (String)dynamicAction.getSearchFieldsDbName().get(index);
                             strSearch += criteriaConverter.strCriteria("and", dbName, param.get(key)+"");
                         }
                         index++;
                     }
                }
             }
         }
         //--thensw: no need security control.
//         strSearch += appendSecurityCondition();

         // Convert first and to where
         // Commented by ThoTH @ 2-Apr-2015, since original sql already got where
//         if (strSearch.indexOf("and") > 0) {
//             strSearch = strSearch.replaceFirst("and", "WHERE");
//         }

         strSql += strSearch;
         
         strSearchPostId = request.getParameter("search_post_id");
         strSearchPostOperId = request.getParameter("search_oper_post_id");
         if (Validator.isEmpty(strSearchPostId) && !Validator.isEmpty(strSearchPostOperId)) {
             strSearchPostOperId = criteriaConverter.strCriteria("and", "postOper.post_id", strSearchPostOperId);
             strSql += " union " +
                     "select " +
                     "null as employee_id, null as emp_name, null as emp_nw_ic_no, null as post_info_id, postOper.post_oper_id, " +
                     "null as ba_post, " +
                     "dept_bu.dept_shortname + ':' + postName_bu.post_name + ' [' + postOper.post_id + ']' as bu_post " +
                     "from t_pt_post_operation postOper " +
                     "left outer join t_pt_post_name postName_bu on postOper.post_name_id = postName_bu.post_name_id " +
                     "left outer join t_pt_establishment bu on postOper.est_id = bu.est_id " +
                     "left outer join t_pt_department dept_bu on dept_bu.dept_id = bu.est_dept_id " +
                     "where not exists (select '' from t_cm_personal_post pp where pp.post_oper_id = postOper.post_oper_id) "
                     + strSearchPostOperId;
         } else if (Validator.isEmpty(strSearchPostOperId) && !Validator.isEmpty(strSearchPostId)) {
             strSearchPostId = criteriaConverter.strCriteria("and", "postInfo.post_id", strSearchPostId);
             strSql += " union " +
                     "select " +
                     "null as employee_id, " +
                     "null as emp_name, " +
                     "null as emp_nw_ic_no, " +
                     "postInfo.post_info_id, " +
                     "null as post_oper_id, " +
                     "dept.dept_shortname + ':' + postName_ba.post_name + ' [' + postInfo.post_id + ']' as ba_post, " +
                     "null as bu_post " +
                     "from t_pt_post_info postInfo " +
                     "left outer join t_pt_post_name postName_ba on postInfo.post_name_id = postName_ba.post_name_id " +
                     "left outer join t_pt_establishment ba on postInfo.est_id = ba.est_id " +
                     "left outer join t_pt_department dept on dept.dept_id = ba.est_dept_id " +
                     "where not exists (select '' from t_pt_post_lantik lantik where lantik.post_info_id = postInfo.post_info_id) "
                     + strSearchPostId;
         }

         strSqlCount = "SELECT COUNT(*) FROM (" + strSql + ") main";

         param.put(SystemConstants.DYNAMIC_ATTS.CUSTOM_SQL, strSql);
         param.put(SystemConstants.DYNAMIC_ATTS.CUSTOM_COUNT_SQL, strSqlCount) ;
         return ActionSupport.SUCCESS;
    }

//    public String preManageRM(DynamicAction dynamicAction, Map param) throws Exception {
//         HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//
//         String data = request.getParameter("search_filter_type");
//         System.out.println("data: " + data);
//         if (data.equals(RmApplicationModel.Filter_Type.PendingCandidate)) {
//             param.remove("re.re_status");
//             param.put(SystemConstants.CRITERIA.NO_CHANGE+"re.re_status", RmApplicationModel.RECRUIT_STATUS.D1_eRecruitment_Pending_Post+","+RmApplicationModel.RECRUIT_STATUS.D2_eRecruitment_Posted);
//         }
//
//        return ActionSupport.SUCCESS;
//    }
    
    //ahmadni @ 4-Jul-2016
    public String preSearchUserAccount(DynamicAction dynamicAction, Map param) throws Exception {
        Map sessionMap = ActionContext.getContext().getSession();
        param.put("t_user_company.co_id", sessionMap.get("co_id"));
        return ActionSupport.SUCCESS;
    }

    //ahmadni @ 4-Jul-2016
    public String preSearchUserGroup(DynamicAction dynamicAction, Map param) throws Exception {
        Map sessionMap = ActionContext.getContext().getSession();
        param.put("userGroup.co_id", sessionMap.get("co_id"));
        return ActionSupport.SUCCESS;
    }
    
    //sereneC @ 26-Jul-2016
    public String preSearchUser(DynamicAction dynamicAction, Map param) throws Exception {
        Map sessionMap = ActionContext.getContext().getSession();
        param.put("t_setup_user.us_id", sessionMap.get("userId"));//ahmadni @ 6-Sept-2017
//        param.put("t_setup_user_p.us_id", sessionMap.get("userId"));
        return ActionSupport.SUCCESS;
    }
    
    public static String getDefaultValue(String field, String setup, Map searchFieldsDateData) {
        String rtn = null;
        setup = setup.substring(5);
        if (setup.equals("1doy")) { // 1st day of year
            rtn = "01/01/" + Calendar.getInstance().get(Calendar.YEAR);
            searchFieldsDateData.put("search_"+field, rtn);
        } else if (setup.equals("1dom")) { // 1st day of month
            Calendar cal = Calendar.getInstance();
            rtn = "01/";
            if (cal.get(Calendar.MONTH) < 9) {
                rtn += "0";
            }
            rtn += (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
            searchFieldsDateData.put("search_"+field, rtn);
        } else if (setup.equals("today")) { //today
            Calendar cal = Calendar.getInstance();
            if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
                rtn += "0";
            }
            rtn += cal.get(Calendar.DAY_OF_MONTH) + "/";
            if (cal.get(Calendar.MONTH) < 9) {
                rtn += "0";
            }
            rtn += (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
            searchFieldsDateData.put("search_"+field, rtn);
        } else if (setup.equals("myValue1")) { //my own function
            
        }
        return rtn;
    }
    
    public String preRoundRobinSetup(DynamicAction dynamicAction, Map param) {
        
        try {
//            System.out.println("preRoundRobinSetup");
            Map sessionMap = ActionContext.getContext().getSession();
            String strUserDiv = "";

            String divAssignedList = sessionMap.get("div_assigned").toString();
            String formattedDivList = divAssignedList.replace("[", "").replace("]", "");
            formattedDivList = formattedDivList.replace(" ", "");

            String[] stringArray = formattedDivList.split(",");
            List<String> divList = new ArrayList<>();
            for (String str : stringArray) {
                divList.add(str);
            }

            String strDefaultWhere = "";
            Map<String, String> setupMap = dynamicAction.getSetupMap();

            //Not HQ User
            if(!strUserDiv.equals("00")){
                setupMap = dynamicAction.getSetupMap();
                //check to avoid developer from overwrite the original value if any
                strDefaultWhere = setupMap.get("defaultSearchValue").toString();
                if (setupMap.get("defaultSearchValue") != null && !setupMap.get("defaultSearchValue").equals("")){
                    strDefaultWhere = "userGroup.system_id = 'U001' AND (";
                    for (int i = 0; i < divList.size(); i++) {
                        if (i == 0) {
                            strDefaultWhere += " userGroup.group_code like '%_"+divList.get(i)+"' ";
                        } else {
                            strDefaultWhere += " OR userGroup.group_code like '%_"+divList.get(i)+"' ";
                        }
                    }

                    strDefaultWhere += ")";
                    Debug.printFrameworkDebug("strDefaultWhere " + strDefaultWhere);
                }else{
                    strDefaultWhere = "userGroup.group_code like '%_"+strUserDiv+"' ";
                }
                setupMap.put("defaultSearchValue",strDefaultWhere);
                dynamicAction.setSetupMap(setupMap);
    //            strDefaultWhere = dynamicAction.getDefaultSearchValue();
    //            strDefaultWhere +="  and userGroup.group_code like '%_"+strUserDiv+"' ";

                  //wongkk4@26Apr2020- not working, need to set defaultSearchValue  in dynamicAction.getSetupMap()
    //            dynamicAction.setDefaultSearchValue(strDefaultWhere);
            }
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        return ActionSupport.SUCCESS;
    }
}

