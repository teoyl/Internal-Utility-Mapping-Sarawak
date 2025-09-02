/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sains.framework.base;

import com.PropertyGetter;
import com.lxg.common.model.Pubcode;
import com.lxg.common.model.SetupCodeModel;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.CommonComparator;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.model.DqDatasourceModel;
import com.sains.framework.model.ItemCategory;
import com.sains.framework.model.NotificationSetup;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.model.SetupSystemModel;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

/**
 *
 * @author lenovo
 */
public class CommonList {

    ActionSupport as = new ActionSupport();
    private static PropertyGetter propText = new PropertyGetter();
    public Session session = null;
    BaseDAOImpl retrivalDAO = new BaseDAOImpl();
    Map paramMap = new HashMap();

    public CommonList(Session session) {
        this.session = session;
        retrivalDAO.setSession(this.session);
    }

    public void setSession(Session session) {
        this.session = session;

    }

    public List getBuCategoryList() {
        List list = null;
//        BaseDAO<BuCategory> buCategoryDAO = new BaseDAOImpl<BuCategory>();
//        try {
//            list = new ArrayList();
//            for (BuCategory buCate : buCategoryDAO.list_order(null, new BuCategory(), "order by bu_category_name")){
//                list.add(new Options(buCate.getBu_category_id(), buCate.getBu_category_name()));
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        } finally {
//            buCategoryDAO.closeSession();
//        }
        return list;
    }

    public List getItemCategoryList() {
        List list = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            list = new ArrayList();
            for (ItemCategory itemCate : (List<ItemCategory>) retrivalDAO.list_order(null, ItemCategory.class, "order by item_category_name")) {
                list.add(new Options(itemCate.getItem_category_id(), itemCate.getItem_category_name()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return list;
    }

    public static void addBlankOption(List list, String keyData, String valueData) {
        if (list != null) {
            list.add(0, new Options(keyData, valueData));
        }
    }

    public List getAutoTypeList() {
        List list = new ArrayList();
        list.add(new Options(SystemConstants.AutoEmail_Type.AI, as.getText("autoEmailSetup.autoType.AI")));
        list.add(new Options(SystemConstants.AutoEmail_Type.WF, as.getText("autoEmailSetup.autoType.WF")));
        return list;
    }

    public List getApproveRejectList() {
        List list = new ArrayList();
        list.add(new Options(SystemConstants.PodStatus_Type.APPROVED, as.getText("PodStatus_Type.AA")));
        list.add(new Options(SystemConstants.PodStatus_Type.REJECTED, as.getText("PodStatus_Type.AR")));
        return list;
    }

    public List getApproveRejectPendingList() {
        List list = getApproveRejectList();
        list.add(new Options(SystemConstants.PodStatus_Type.PENDING_APPROVAL, as.getText("PodStatus_Type.PA")));
        return list;
    }

//    public List getAccountType(){
//        List list = new ArrayList();
//        list.add(new Options(SystemConstants.Account_Type.BASIC, as.getText("user.userType.B").toUpperCase()));
//        list.add(new Options(SystemConstants.Account_Type.PREMIUM, as.getText("user.userType.P").toUpperCase()));
//        return list;
//    }
    public List getPreferredContactOption() {
        List list = new ArrayList();
        list.add(new Options("", as.getText("pleaseSelect")));
        list.add(new Options("M", as.getText("UserContactType.M")));
        list.add(new Options("S", as.getText("UserContactType.S")));
        list.add(new Options("B", as.getText("UserContactType.B")));
        return list;
    }

    public List getModuleOptions() {
        List list = new ArrayList();
        list.add(new Options("M", "Module"));
        list.add(new Options("S", "Sub Module"));

        return list;
    }

    public static List getLdapOptions() {
        List list = new ArrayList();
        list.add(new Options("Y", "Yes"));
        list.add(new Options("N", "No"));

        return list;
    }

    public List getLoginTypeOptions() {
        List list = new ArrayList();
        list.add(new Options(SystemConstants.PBT_LOGIN_TYPE.LDAP, "LDAP"));
        list.add(new Options(SystemConstants.PBT_LOGIN_TYPE.NEW_IC, as.getText("PR.personal.perNewICNo")));

        return list;
    }

    public List getJantinaOptions() {
        List list = new ArrayList();
        list.add(new Options("0", as.getText("PR.common.gender.0")));
        list.add(new Options("L", as.getText("PR.common.gender.L")));
        list.add(new Options("P", as.getText("PR.common.gender.P")));

        return list;
    }

    public List getGenderOptions() {
        List list = new ArrayList();
        list.add(new Options("M", as.getText("Sample.parentGender.M")));
        list.add(new Options("F", as.getText("Sample.parentGender.F")));
        list.add(new Options("O", as.getText("Sample.parentGender.O")));

        return list;
    }

    public String retrieveOptionValue(List<Options> list, String key) {
        for (Options opt : list) {
            if (opt.getKeyData().equals(key)) {
                return opt.getValueData();
            }
        }
        return "";
    }

    //Delvene @ 11-Apr-2013
    public List getActiveFlagOptions() {
        List list = new ArrayList();
        list.add(new Options("A", as.getText("common.status.A")));
        list.add(new Options("I", as.getText("common.status.I")));

        return list;
    }

    public List getPRHRActiveFlagOptions() {
        List list = new ArrayList();
        list.add(new Options("A", as.getText("common.status.A")));
        list.add(new Options("", as.getText("common.status.I")));

        return list;
    }

    public List getCountryStatusOptions() {
        List list = new ArrayList();
        list.add(new Options("1", as.getText("PR.common.countryStatus.1")));
        list.add(new Options("2", as.getText("PR.common.countryStatus.2")));
        list.add(new Options("3", as.getText("PR.common.countryStatus.3")));
        list.add(new Options("4", as.getText("PR.common.countryStatus.4")));
        list.add(new Options("5", as.getText("PR.common.countryStatus.5")));

        return list;
    }

    public List getIdentificationOptions() {
        List list = new ArrayList();
        list.add(new Options(SystemConstants.IDENTIFICATION_TYPE.NEW_IC, as.getText("PR.personal.perIdType." + SystemConstants.IDENTIFICATION_TYPE.NEW_IC)));
        list.add(new Options(SystemConstants.IDENTIFICATION_TYPE.ARMY_POLICE_IC, as.getText("PR.personal.perIdType." + SystemConstants.IDENTIFICATION_TYPE.ARMY_POLICE_IC)));
        //list.add(new Options(SystemConstants.IDENTIFICATION_TYPE.PASSPORT, as.getText("PR.personal.perIdType."+SystemConstants.IDENTIFICATION_TYPE.PASSPORT)));
        return list;
    }

    public List getIdentificationOptionsCou() { //based on country statys
        List list = new ArrayList();
        list.add(new Options(SystemConstants.IDENTIFICATION_TYPE.NEW_IC, as.getText("PR.personal.perIdType." + SystemConstants.IDENTIFICATION_TYPE.NEW_IC)));
//        list.add(new Options(SystemConstants.IDENTIFICATION_TYPE.ARMY_POLICE_IC, as.getText("PR.personal.perIdType."+SystemConstants.IDENTIFICATION_TYPE.ARMY_POLICE_IC)));
        list.add(new Options(SystemConstants.IDENTIFICATION_TYPE.PASSPORT, as.getText("PR.personal.perIdType." + SystemConstants.IDENTIFICATION_TYPE.PASSPORT)));
        return list;
    }

    public List getDurationUnitOption() {
        List list = new ArrayList();
        list.add(new Options("1", as.getText("duration.unit.1")));
        list.add(new Options("2", as.getText("duration.unit.2")));
        list.add(new Options("3", as.getText("duration.unit.3")));
        list.add(new Options("4", as.getText("duration.unit.4")));

        return list;
    }

    public List getYesNoOptions() {
        List list = new ArrayList();
        //Edited by Delvene @ 30-Aug-2013
        list.add(new Options("Y", as.getText("PR.common.comOption.Y")));
        list.add(new Options("N", as.getText("PR.common.comOption.N")));
//        list.add(new Options("Y", as.getText("yes")));
//        list.add(new Options("N", as.getText("no")));

        return list;
    }

    public List getCurrentYearDownTo2012() { //will down to year 2015 (LIVE year), not going to change the function's name
        List list = new ArrayList();
        Calendar cal = Calendar.getInstance();
        Integer year = cal.get(Calendar.YEAR);
        for (int yearIdx = year; yearIdx > 2014; yearIdx--) {
            list.add(new Options(yearIdx + "", yearIdx + ""));
        }
        return list;
    }

    //sereneChye
    public List getMonthList() {
        List list = new ArrayList();
        for (int monthIdx = 1; monthIdx < 13; monthIdx++) {
            list.add(new Options(monthIdx + "", new DateFormatSymbols().getMonths()[monthIdx - 1] + ""));
        }
        return list;
    }

    public List getModuleTypeOption() {
        List list = new ArrayList();
        list.add(new Options("M", "Module"));
        list.add(new Options("S", "Sub Module"));
        return list;
    }

    //Added by Edmund 20 Sept 2013
    public List getGeneratedYearList() {
        List list = new ArrayList();
        Calendar cal = Calendar.getInstance();
        Integer currYear = cal.get(Calendar.YEAR);
        for (int yearIndx = currYear; yearIndx != (currYear - 15); yearIndx--) {
            list.add(new Options(yearIndx + "", yearIndx + ""));
        }

        return list;
    }

    public List getPoolStatusList() {
        List list = new ArrayList();
        list.add(new Options("A", as.getText("jobpool.status.A")));
        list.add(new Options("B", as.getText("jobpool.status.B")));
        list.add(new Options("C", as.getText("jobpool.status.C")));
//        list.add(new Options("D", as.getText("jobpool.status.D"))); //combine status diserahkan semula and dalam process 
        list.add(new Options("E", as.getText("jobpool.status.E")));
        return list;
    }

    //added by sereneChye @ 16/6/2015 :: without Status 'baru'
    public List getPoolStatusList2() {
        List list = new ArrayList();
//        list.add(new Options("A", as.getText("jobpool.status.A")));
        list.add(new Options("B", as.getText("jobpool.status.B")));
        list.add(new Options("C", as.getText("jobpool.status.C")));
        list.add(new Options("D", as.getText("jobpool.status.D")));
        list.add(new Options("E", as.getText("jobpool.status.E")));
        return list;
    }

    public List getServProfConfirmStatusOptions() {
        List list = new ArrayList();
        list.add(new Options("Y", as.getText("PR.servProfile.servConfirmStatus.Y")));
        list.add(new Options("N", as.getText("PR.servProfile.servConfirmStatus.N")));
        return list;
    }

    public List getSortOption() {
        List list = new ArrayList();
        list.add(new Options("ASC", as.getText("sort.ASC")));
        list.add(new Options("DESC", as.getText("sort.DESC")));

        return list;
    }

    public List getSystemTypeOption() {
        List list = new ArrayList();
        list.add(new Options(SystemConstants.SYSTEM_TYPE.DEFAULT, "DEFAULT"));
        list.add(new Options(SystemConstants.SYSTEM_TYPE.PUBLIC, "PUBLIC"));

        return list;
    }

    //ChangMH @ 29-Aug-2014 :: For Dynamic Search TOSHR Checkbox
    public List getOwnCreationOption() {
        List list = new ArrayList();
        list.add(new Options((String) ActionContext.getContext().getSession().get("loginId"), ""));
        return list;
    }

    //ChangMH @ 11-Apr-2013 : Active flag for setup code
    public List getActiveStatusOptions() {
        List list = new ArrayList();
        list.add(new Options("Y", as.getText("setupcode.active.Y")));
        list.add(new Options("N", as.getText("setupcode.active.N")));

        return list;
    }

    // Serene @ 18-Dec-2014 :: For MixConfig
    public List getDateMonthOptionList() {

        List list = new ArrayList();
        list.add(new Options("Tugas__Kini", as.getText("jobAdmin.report.thisMonth")));
        list.add(new Options("Tugas__BulanTahun", as.getText("jobAdmin.report.userDefineMonthYear")));
        addBlankOption(list, "", as.getText("pleaseSelect"));
//             PtApplicationModel.APPLICATION_TYPE.ManageSupervisor};   //Temporary hide off
        return list;
    }

    public List getDateMonthOptionListAliran() {

        List list = new ArrayList();
        list.add(new Options("TugasAliran__Kini", as.getText("jobAdmin.report.thisMonth")));
        list.add(new Options("TugasAliran__BulanTahun", as.getText("jobAdmin.report.userDefineMonthYear")));
        addBlankOption(list, "", as.getText("pleaseSelect"));
//             PtApplicationModel.APPLICATION_TYPE.ManageSupervisor};   //Temporary hide off
        return list;
    }

    public List getYearList() {
        List list = new ArrayList();
        list.add(new Options("", as.getText("jobAdmin.report.thisMonth")));
        list.add(new Options("currentYearMonth", as.getText("jobAdmin.report.dateMonth")));
        return list;
    }

    public List getActiveEmployeeList() {
        List sqlList = null;
        List list = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        String sql = "SELECT employee_id from t_employee where active_flag = 'A'";

        try {
            sqlList = new CommonFunction().getListFromSqlWithSession(retrivalDAO.getSession(), sql, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return sqlList;
    }

//     ahmadni @ 4-Jul-2016
    public List getAccountStatus() {
        List list = new ArrayList();

        list.add(new Options(SystemConstants.USER_ACC_STATUS.ACTIVE, as.getText("active")));
        list.add(new Options(SystemConstants.USER_ACC_STATUS.INACTIVE, as.getText("inactive")));
        list.add(new Options(SystemConstants.USER_ACC_STATUS.CANCELLED, as.getText("cancelled")));
        list.add(new Options(SystemConstants.USER_ACC_STATUS.LOCKED, as.getText("locked")));

        return list;
    }

    public List getUserTypeList() {
        List list = new ArrayList();
        list.add(new Options("C", SystemConstants.QP_USER_TYPE.CO_ADMIN));
        list.add(new Options("S", SystemConstants.QP_USER_TYPE.NORMAL_STAFF));
        return list;
    }

    //use Y=Active , N=Inactive
    public List getActiveInactiveOptions() {
        List list = new ArrayList();
        //Edited by Delvene @ 30-Aug-2013
        list.add(new Options("Y", as.getText("active")));
        list.add(new Options("N", as.getText("inactive")));
//        list.add(new Options("Y", as.getText("yes")));
//        list.add(new Options("N", as.getText("no")));

        return list;
    }

    public List getYesNoEnglishOptions() {
        List list = new ArrayList();
        //Edited by Delvene @ 30-Aug-2013
        list.add(new Options("Y", as.getText("tc.Y")));
        list.add(new Options("N", as.getText("tc.N")));
//        list.add(new Options("Y", as.getText("yes")));
//        list.add(new Options("N", as.getText("no")));

        return list;
    }

    public List getYesNoNaOptions() {
        List list = new ArrayList();
        list.add(new Options("Y", as.getText("tc.Y")));
        list.add(new Options("N", as.getText("tc.N")));
        list.add(new Options("X", as.getText("tc.NA")));

        return list;
    }

    public List getGroupTypeOption() {
        List list = new ArrayList();
        list.add(new Options(SetupGroup.GROUP_TYPE.Application, as.getText("group.type." + SetupGroup.GROUP_TYPE.Application)));
        list.add(new Options(SetupGroup.GROUP_TYPE.Workflow, as.getText("group.type." + SetupGroup.GROUP_TYPE.Workflow)));

        return list;
    }

    public List getUserDetailOption() {
        List list = new ArrayList();
        list.add(new Options("URL", as.getText("Param.method.URL")));
        list.add(new Options("DB", as.getText("Param.method.DB")));
        list.add(new Options("DB", as.getText("Param.method.INT")));

        return list;
    }

    public List getDbTypeOption() {
        List list = new ArrayList();
        list.add(new Options("MySQL", as.getText("Param.DBMS.MySQL")));
        list.add(new Options("MSSQL", as.getText("Param.DBMS.MSSQL")));
        list.add(new Options("ORACLE", as.getText("Param.DBMS.ORACLE")));

        return list;
    }

    public List getPageList() {
        List list = new ArrayList();
        list.add(new Options("1", as.getText("whatPage.1")));
        list.add(new Options("2", as.getText("whatPage.2")));
        return list;
    }

    public List getDqDatasourceList() {
        List list = null;
        try {
            list = new ArrayList();
            for (DqDatasourceModel datasource : (List<DqDatasourceModel>) retrivalDAO.list_order(null, DqDatasourceModel.class, "order by ds_desc")) {
                list.add(new Options(datasource.getID(), datasource.getDs_desc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List getDivisionList() {
        List divisionList = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            divisionList = new ArrayList();
            paramMap.clear();
            paramMap.put("code_type", "DIV");
            paramMap.put("code_1", "!in 99|MD|000|00");
            for (Pubcode item : (List<Pubcode>) retrivalDAO.list_order(paramMap, Pubcode.class, true, "order by code_1")) {
                divisionList.add(new Options(item.getCode_1(), item.getCode_desc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return divisionList;
    }

    public List getEmptyOption() {
        return new ArrayList();
    }

    public static List jobTypeList = null;

    public static synchronized List getJobTypeList() {
        if (jobTypeList == null) {
            jobTypeList = new ArrayList();
            jobTypeList.add(new Options("D", propText.getPropertyText("schedular.jobType.D")));
            jobTypeList.add(new Options("W", propText.getPropertyText("schedular.jobType.W")));
            jobTypeList.add(new Options("M", propText.getPropertyText("schedular.jobType.M")));
            jobTypeList.add(new Options("Y", propText.getPropertyText("schedular.jobType.Y")));
        }

        return jobTypeList;
    }

    public synchronized List getAppStatusOptions() {
        List appStatusList = new ArrayList();
        appStatusList.add(new Options("10", propText.getPropertyText("training.appStatus.10")));
        appStatusList.add(new Options("20", propText.getPropertyText("training.appStatus.20")));
        return appStatusList;
    }
//    public List appStatusList = null;
//    public synchronized List getAppStatusOptions() {
//        if (appStatusList == null) {
//            appStatusList = new ArrayList();
//            appStatusList.add(new Options("10", propText.getPropertyText("training.appStatus.10")));
//            appStatusList.add(new Options("20", propText.getPropertyText("training.appStatus.20")));
//        }
//        
//        return appStatusList;
//    }
    public static List userStatusList = null;

    public List getUserStatusList() {
        if (userStatusList == null) {
            ActionSupport as = new ActionSupport();
            userStatusList = new ArrayList();
            userStatusList.add(new Options("Y", as.getText("common.account.status.Y")));
            userStatusList.add(new Options("N", as.getText("common.account.status.N")));
            userStatusList.add(new Options("L", as.getText("common.account.status.L")));
        }

        return userStatusList;
    }

    public List<SetupSystemModel> getSystemListRoute() {
        try {
            Map param = new HashMap();
//            param.put("main_syscode", SystemConstants.WF_SYSTEM_CODE);
            param.put("route_workflow", "Y");
            return retrivalDAO.list_order(param, SetupSystemModel.class, true, "order by system_order");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List getProcessTypeList() {
        List list = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            List<SetupCodeModel> modelList;
            Map param = new HashMap();
            param.put("code_type", "PRT");
            modelList = retrivalDAO.list_order(param, SetupCodeModel.class, true, "order by code_1");

            list = new ArrayList();
            list.add(new Options("", "Please Select"));
            if (modelList != null) {
                for (SetupCodeModel oneModel : modelList) {
                    if (!oneModel.getCode_1().equals("000")) {
                        list.add(new Options(oneModel.getCode_1(), oneModel.getCode_1() + " - " + oneModel.getCode_desc()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return list;
    }

    private List<Options> optionList = null;

    public List<Options> getStatusOption() {
        optionList = new ArrayList();
        optionList.add(new Options("Y", "Yes"));
        optionList.add(new Options("N", "No"));

        return optionList;
    }

    private List<Options> dataTypeList = null;

    public List<Options> getDataTypeList() {
        dataTypeList = new ArrayList();
        dataTypeList.add(new Options("", as.getText("pleaseSelect")));
        dataTypeList.add(new Options("CHECKBOX", "Checkbox"));
        dataTypeList.add(new Options("RADIO", "Radio Button"));
        dataTypeList.add(new Options("DATEPICKER", "Datepicker"));
        dataTypeList.add(new Options("TEXTBOX", "Textbox"));
        dataTypeList.add(new Options("LABEL", "Label"));

        List<SetupCodeModel> modelList;
        Map param = new HashMap();
        param.put("code_type", "DOC");
        modelList = retrivalDAO.list_order(param, SetupCodeModel.class, true, "order by code_1");
        if (modelList != null) {
            for (SetupCodeModel oneModel : modelList) {
                if (!oneModel.getCode_1().equals("000")) {
                    if (!Validator.isEmpty(oneModel.getCode_2())) {
                        dataTypeList.add(new Options(oneModel.getCode_2(), oneModel.getCode_2() + " - " + oneModel.getCode_desc()));
                    } else {
                        dataTypeList.add(new Options(oneModel.getCode_1(), oneModel.getCode_1() + " - " + oneModel.getCode_desc()));
                    }
                }
            }
        }

        Collections.sort(dataTypeList, new CommonComparator(new String[]{"getValueData"}));
        return dataTypeList;
    }

    public List getStateList() {
        List stateList = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            stateList = new ArrayList();
            paramMap.clear();
            paramMap.put("code_type", "LST");
            for (Pubcode item : (List<Pubcode>) retrivalDAO.list_order(paramMap, Pubcode.class, true, "order by code_1")) {
                stateList.add(new Options(item.getCode_1(), item.getCode_desc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return stateList;
    }

    //mhdaiman 9/8/22 get country prefix + country name
    public List getCountryCodeListWithDesc() {
        List list = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            list = new ArrayList();
            paramMap.clear();
            paramMap.put("code_type", "NAT");
            paramMap.put("code_1", "!in 000");
            for (Pubcode item : (List<Pubcode>) retrivalDAO.list_order(paramMap, Pubcode.class, true, "order by code_1")) {
                list.add(new Options("(+" + item.getCode_acr() + ")", "(+" + item.getCode_acr() + ") " + StringUtils.capitalize(item.getCode_desc().toLowerCase())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return list;
    }

    public List getAllDisList() {
        System.out.println("dis list x1");
        List list = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            list = new ArrayList();
            paramMap.clear();
            paramMap.put("code_type", "ADS"); //Administrative District
//            paramMap.put("code_type", "DIS");
            paramMap.put("code_1", "!in 000");
            for (Pubcode item : (List<Pubcode>) retrivalDAO.list_order(paramMap, Pubcode.class, true, "order by code_1")) {
                Map dataMap = new HashMap();
                dataMap.put("code_1", item.getCode_1());
                dataMap.put("code_2", item.getCode_2());
                dataMap.put("code_desc", item.getCode_desc());
                list.add(dataMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return list;
    }

    private List<Options> surveyTypeList = null;

    public List<Options> getSurveyTypeOption() {
        surveyTypeList = new ArrayList();
        surveyTypeList.add(new Options("USJ", "Utility Survey"));

        return surveyTypeList;
    }

    private List<Options> requestInstructionList = null;

    public List<Options> getRequestInstructionOption() {
        requestInstructionList = new ArrayList();

        paramMap.clear();
        paramMap.put("no_type", "USJ_JOB_INSTRUCTION_");
        for (NotificationSetup item : (List<NotificationSetup>) retrivalDAO.list_order(paramMap, NotificationSetup.class, true, "order by no_type")) {
            requestInstructionList.add(new Options(item.getNo_type(), item.getNo_type()));
        }

        return requestInstructionList;
    }

    public List getAllDivisionList() {
        List divisionList = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            divisionList = new ArrayList();
            divisionList.add(new Options("", "All Divisions"));
            paramMap.clear();
            paramMap.put("code_type", "DIV");
            paramMap.put("code_1", "!in 99|MD|000|00");
            for (Pubcode item : (List<Pubcode>) retrivalDAO.list_order(paramMap, Pubcode.class, true, "order by code_1")) {
                divisionList.add(new Options(item.getCode_1(), item.getCode_desc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return divisionList;
    }

    public List getClassificationList() {
        List classificationList = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            classificationList = new ArrayList();
            classificationList.add(new Options("", "Please Select"));

            paramMap.clear();
            paramMap.put("code_type", "SCC");
            for (Pubcode item : (List<Pubcode>) retrivalDAO.list_order(paramMap, Pubcode.class, true, "order by code_desc")) {
                classificationList.add(new Options(item.getCode_1(), item.getCode_desc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return classificationList;
    }

    public List getBranchList() {
        List branchList = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            branchList = new ArrayList();
            branchList.add(new Options("", "Please Select"));

            paramMap.clear();
            paramMap.put("code_type", "BRN");
            for (Pubcode item : (List<Pubcode>) retrivalDAO.list_order(paramMap, Pubcode.class, true, "order by code_desc")) {
                branchList.add(new Options(item.getCode_1(), item.getCode_desc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return branchList;
    }

    public List getQualitySurveyList() {
        List qualitySurveyList = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            qualitySurveyList = new ArrayList();
            qualitySurveyList.add(new Options("", "Please Select"));

            paramMap.clear();
            paramMap.put("code_type", "UST");
            for (Pubcode item : (List<Pubcode>) retrivalDAO.list_order(paramMap, Pubcode.class, true, "order by code_desc")) {
                qualitySurveyList.add(new Options(item.getCode_1(), item.getCode_desc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return qualitySurveyList;
    }

    public List getFirmList() {
        List firmList = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            firmList = new ArrayList();
            paramMap.clear();
            paramMap.put("code_type", "SOC");
            paramMap.put("code_1", "!in 99|MD|000|00");
            for (Pubcode item : (List<Pubcode>) retrivalDAO.list_order(paramMap, Pubcode.class, true, "order by code_1")) {
                firmList.add(new Options(item.getCode_1(), item.getCode_desc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return firmList;
    }

    public List getDivList() {
        List divList = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            divList = new ArrayList();
            paramMap.clear();
            paramMap.put("code_type", "DIV");
            paramMap.put("code_1", "!in 99|MD|000|00");
            for (Pubcode item : (List<Pubcode>) retrivalDAO.list_order(paramMap, Pubcode.class, true, "order by code_1")) {
                divList.add(new Options(item.getCode_1(), item.getCode_1()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return divList;
    }

    public List getDisList() {
        List divList = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            divList = new ArrayList();
            paramMap.clear();
            paramMap.put("code_type", "ADS"); //Administrative District
            paramMap.put("code_1", "!in 000");
            for (Pubcode item : (List<Pubcode>) retrivalDAO.list_order(paramMap, Pubcode.class, true, "order by code_1")) {
                divList.add(new Options(item.getCode_1(), item.getCode_desc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return divList;
    }

    public List getCurrentYearUpToFiveYear() { //will down to current year up to five year
        List list = new ArrayList();
        Calendar cal = Calendar.getInstance();
        Integer year = cal.get(Calendar.YEAR);

        for (int yearIdx = year; yearIdx >= 2024; yearIdx--) {
            list.add(new Options(yearIdx + "", yearIdx + ""));
        }
        return list;
    }

    public List getWorkflowStatusList() {
        List wfStatusList = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            wfStatusList = new ArrayList();
            paramMap.clear();
            paramMap.put("code_type", "JWS"); //Administrative District
            paramMap.put("code_1", "!in 000");
            for (SetupCodeModel item : (List<SetupCodeModel>) retrivalDAO.list_order(paramMap, SetupCodeModel.class, true, "order by code_1 asc")) {
                wfStatusList.add(new Options(item.getCode_1(), item.getCode_desc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return wfStatusList;
    }
    
    public List getUSJCategoryOptions() {
        List list = new ArrayList();
        list.add(new Options("Y", "With Traverse"));
        list.add(new Options("N", "Without Traverse"));
        return list;
    }
    
    public List getJobStatusReportOptions() {
        List list = new ArrayList();
        list.add(new Options("completedBySO", "Jobs completed by Survey Organisation / Utility Provider"));
        list.add(new Options("jobsByOfficer", "Show Jobs by Officer"));
        list.add(new Options("completedJob", "Number of Jobs Completed"));
        list.add(new Options("kivJob", "Number of Jobs KIV"));
        list.add(new Options("queryJob", "Number of Jobs Under Query"));
        list.add(new Options("outstandingJob", "Outstanding Jobs by Officer"));
        return list;
    }
    
    public List getKpiJobStatusOptions() {
        List list = new ArrayList();
        list.add(new Options("inProgress", "On Going"));
        list.add(new Options("completed", "Completed"));
        return list;
    }
    
    public List getKpiReportOptions() {
        List list = new ArrayList();
        list.add(new Options("division", "By Division"));
        list.add(new Options("user", "By User"));
        return list;
    }
    
    public List getWorkflowStatusList11() {
        List wfStatusList = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            wfStatusList = new ArrayList();
            paramMap.clear();
            paramMap.put("code_type", "JWS"); //Administrative District
            paramMap.put("code_1", "!in 000");
            for (SetupCodeModel item : (List<SetupCodeModel>) retrivalDAO.list_order(paramMap, SetupCodeModel.class, true, "order by code_1 asc")) {
                wfStatusList.add(new Options("'"+item.getCode_1()+"'", item.getCode_desc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return wfStatusList;
    }
    
    public List getUtilityProvidersList() {
        List wfStatusList = null;
        Boolean sessionNotSet = retrivalDAO.getSession() == null;
        try {
            wfStatusList = new ArrayList();
            paramMap.clear();
            paramMap.put("code_type", "UTP"); //Administrative District
            paramMap.put("code_1", "!in 000");
            for (SetupCodeModel item : (List<SetupCodeModel>) retrivalDAO.list_order(paramMap, SetupCodeModel.class, true, "order by code_1 asc")) {
                wfStatusList.add(new Options("'"+item.getCode_1()+"'", item.getCode_desc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sessionNotSet) {
                retrivalDAO.closeSession();
            }
        }
        return wfStatusList;
    }
}
