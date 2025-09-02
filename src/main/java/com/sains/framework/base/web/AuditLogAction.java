package com.sains.framework.base.web;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.CriteriaConverter;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.CommonList;
import com.sains.framework.base.ModelBase;
import com.sains.framework.base.SessionFactoryImpl;
import com.sains.framework.model.AuditTrailModel;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.WordUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.query.Query;

public class AuditLogAction extends BaseActionSupport{// implements ModelDriven<String> {

    private static final long serialVersionUID = -6659925652584240539L;
    private String useServiceFactory_ = "N";

    public String getUseServiceFactory_() {
        return useServiceFactory_;
    }

    public void setUseServiceFactory_(String useServiceFactory_) {
        this.useServiceFactory_ = useServiceFactory_;
    }


    public AuditLogAction() {
        // change the DAO to the correct service.
        //baseDAO = new BaseDAOImpl();
        model = new String();
        setInsertUpdateSuccessCode(SystemConstants.ACTION_Status.LOAD_EDIT_PAGE);
        // set the required field for common check.
        //getRequiredParam().put("column_name1", "column.name1");
        //getRequiredParam().put("column_name2", "column.name2");


    }

    public void specificValidation(String validationType) {
    }

    public String processInsert() {
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    public String processUpdate() {
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String delete() {
        return SUCCESS;
    }
    
    HttpServletRequest globalRequest = null;
    String javaImportModels = null;
    
    private String removeGet(String methodName) {
        String firstChar = methodName.substring(3, 4);
        methodName = methodName.substring(4);
        return firstChar.toLowerCase()+methodName;
    }
    
    public static List registeredClassList = null;
    public List getRegisteredClassList() {
        if (registeredClassList == null) {
            registeredClassList = new ArrayList();
            if (SessionFactoryImpl.registeredClass == null) {
                baseDAO.getSession();
            }
            for (Object obj : SessionFactoryImpl.registeredClass) {
                registeredClassList.add(new Options(((Class)obj).getSimpleName(), ((Class)obj).getSimpleName()));
            }
            CommonList.addBlankOption(registeredClassList, "", getText("pleaseSelect"));
        }
        return registeredClassList;
    }
    
    private String selectedClass = null;
    public String getSelectedClass() {
        return selectedClass;
    }
    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }
    
    private Class getModelClass() {
        if (SessionFactoryImpl.registeredClass == null) {
            baseDAO.getSession();
        }
        for (Object obj : SessionFactoryImpl.registeredClass) {
            if (((Class)obj).getSimpleName().equals(selectedClass)) {
                return (Class)obj;
            }
        }
        return null;
    }
    
    private String parentModelClass = "theModel";
    public String getParentModelClass() {
        return parentModelClass;
    }
    public void setParentModelClass(String parentModelClass) {
        this.parentModelClass = parentModelClass;
    }
    
    private String parentModelName = "theModel";
    public String getParentModelName() {
        return parentModelName;
    }
    public void setParentModelName(String parentModelName) {
        this.parentModelName = parentModelName;
    }

    private String mainModelVarName = "model";
    public String getMainModelVarName() {
        return mainModelVarName;
    }
    public void setMainModelVarName(String mainModelVarName) {
        this.mainModelVarName = mainModelVarName;
    }
    
    private String[] entryModels = null;
    public String[] getEntryModels() {
        return entryModels;
    }
    public void setEntryModels(String[] entryModels) {
        this.entryModels = entryModels;
    }
    
    //** Generate Audit Query Page from Model : START **//
    public String load() {
        return "load";
    }
    
    private List columnList = null;
    public List getColumnList() {
        return columnList;
    }
    public void setColumnList(List columnList) {
        this.columnList = columnList;
    }
    
    public String modelSelected() {
        if (columnList == null) columnList = new ArrayList();
        List<Options> oneToManyList = new ArrayList();
        Class modelClass = getModelClass();
        String html = "";
        Integer idx = -1;
        for(Method method : modelClass.getDeclaredMethods()){
            if (!method.getName().startsWith("get")) continue;
            Annotation[] annotations = method.getAnnotations();
            Boolean isPK = Boolean.FALSE;
            String name = null;
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i].annotationType().equals(Id.class)) {
                    isPK = Boolean.TRUE;
                }
                if (annotations[i].annotationType().equals(Column.class)) {
                    name = ((Column)annotations[i]).name();
                }
                if (annotations[i].annotationType().equals(OneToMany.class)) {
                    oneToManyList.add(new Options(parentModelName+"."+removeGet(method.getName()), ((OneToMany)annotations[i]).targetEntity().getSimpleName()));
                }
            }
            if (name != null) {
                idx++;
                String labelName = name;
                
                if (name.equalsIgnoreCase("created_date") || name.equalsIgnoreCase("created_by") ||
                    name.equalsIgnoreCase("updated_date") || name.equalsIgnoreCase("updated_by")) {
                    continue; //do not generate the input fields for these columns
                }
                if (method.getGenericReturnType().toString().equals("class java.lang.String")) {
                } else if (method.getGenericReturnType().toString().equals("class java.lang.Double")) {
                    name += "_str";
                } else if (method.getGenericReturnType().toString().equals("class java.lang.Long")) {
                    name += "_str";
                } else if (method.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                    name += "_str";
                } else if (method.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
                    name += "_str";
                }
                if (isPK) {
//                    html =  "<div class='form-horizontal form-group'>\n"+
//                            "   <label class='col-md-4 control-label'>"+ WordUtils.capitalize(labelName.replace("_", " ")).replace(" Id", " ID") +"</label>\n"+
//                            "   <div class='col-md-5'>\n"+
//                            "      <input type='text' id='"+name+"' class='form-control' name='"+name+"' value='' />\n"+
//                            "   </div>\n"+
//                            "</div>\n" + html;
                    html =  "<tr id=\"row-"+(idx+1)+"\" class=\"templateFieldRow\"><td>"+(idx+1)+"</td>\n"+
                            "<td>\n"+
                            "    <div class=\"checkbox check-success\">\n"+
                            "        <input class=\"cbShowMe\" name=\""+name+"_showMe\" type=\"checkbox\" id=\"_"+idx+"\" onclick=\"toggleSelectAll('cbShowMe')\"/>\n"+
                            "        <label for=\"_"+idx+"\"></label>\n"+
                            "    </div>\n"+
                            "</td>\n"+
                            "<td>"+WordUtils.capitalize(labelName.replace("_", " ")).replace(" Id", " ID")+"</td>\n"+
                            "<td>\n"+
                            "   <input type='text' id='"+name+"' class='form-control' name='"+name+"' value='' />\n"+
                            "   <input type='hidden' id='order row-"+(idx+1)+"' class='form-control' name='"+name+"_fld_order' value='' />\n"+
                            "</td></tr>\n" + html;
                } else {
//                    html +=  "<div class='form-horizontal form-group'>\n"+
//                            "   <label class='col-md-4 control-label'>"+ WordUtils.capitalize(labelName.replace("_", " ")).replace(" Id", " ID") +"</label>\n"+
//                            "   <div class='col-md-5'>\n"+
//                            "      <input type='text' id='"+name+"' class='form-control' name='"+name+"' value='' />\n"+
//                            "   </div>\n"+
//                            "</div>";
                    html += "<tr id=\"row-"+(idx+1)+"\" class=\"templateFieldRow\"><td>"+(idx+1)+"</td>\n"+
                            "<td>\n"+
                            "    <div class=\"checkbox check-success\">\n"+
                            "        <input class=\"cbShowMe\" name=\""+name+"_showMe\" type=\"checkbox\" id=\"_"+idx+"\" onclick=\"toggleSelectAll('cbShowMe')\"/>\n"+
                            "        <label for=\"_"+idx+"\"></label>\n"+
                            "    </div>\n"+
                            "</td>\n"+
                            "<td>"+WordUtils.capitalize(labelName.replace("_", " ")).replace(" Id", " ID")+"</td>\n"+
                            "<td>\n"+
                            "   <input type='text' id='"+name+"' class='form-control' name='"+name+"' value='' />\n"+
                            "   <input type='hidden' id='order row-"+(idx+1)+"' class='form-control' name='"+name+"_fld_order' value='' />\n"+
                            "</td></tr>";
                }
            }
        }
        
        try {
            HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            response.setContentType("text/html; charset=UTF-8");
            
            /*
            
            <div class="table-responsive">
                            <table id="templateFieldTable" class="table table-sds table-condensed table-striped table-hover" width="100%">
                            <thead>
                                <tr>
                                    <th><s:text name="common.no"/></th>
                                    <th><s:text name="AuditLog.showMe"/><br>
                                        <div class="checkbox check-success">
                                            <input type="checkbox"  id="cbselect" class="selectAll" name="cbselect" onClick="toggleCheckboxByClassName(this,'cbShowMe');">
                                            <label for="cbselect"></label>
                                        </div>
                                    </th>
                                    <th><s:text name="AuditLog.fieldName"/></th>
                                    <th><s:text name="AuditLog.fieldFilter"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <s:iterator value="model.templateFieldList" var="field" status="fieldStatus">
                                    <tr id="row-idx+1}" class="templateFieldRow">
                                        <td>idx+1</td>
                                        <td>
                                            <div class="checkbox check-success">
                                                <input class="cbShowMe" type="checkbox" <s:if test="#field.fld_showMe_boo">checked</s:if> value="Y" name="model.templateFieldList[idx}].fld_showMe" id="_idx}" onclick="toggleSelectAll('cbShowMe')"/>
                                                <label for="_idx}"></label>
                                            </div>
                                    </div>
                                        </td>
                                        <td><s:textfield readonly="true" value="%{#field.fld_col}" name="model.templateFieldList[%{#fieldStatus.index}].fld_col" cssClass="form-control"/></td>
                                        <td>
                                            <s:hidden id="order_row-%{#fieldStatus.index+1}" value="%{#field.fld_order}" name="model.templateFieldList[%{#fieldStatus.index}].fld_order" cssClass="form-control"/>
                                            <s:hidden value="%{#field.ID}" name="model.templateFieldList[%{#fieldStatus.index}].ID"/>
                                            <s:textfield value="%{#field.fld_lbl}" name="model.templateFieldList[%{#fieldStatus.index}].fld_lbl" cssClass="form-control"/>
                                        </td>
                                    </tr>
                                </s:iterator>
                            </tbody>
                            </table>
                        </div> 
            
            */
            html = "<div class=\"table-responsive\">\n"+
                   "   <table id=\"templateFieldTable\" class=\"table table-sds table-condensed table-striped table-hover\" width=\"100%\">\n"+
                   "      <thead>\n"+
                   "         <tr>\n"+
                   "            <th>"+getText("common.no")+"</th>\n"+
                   "            <th>"+getText("AuditLog.showMe")+"<br>\n"+
                   "               <div class=\"checkbox check-success\">\n"+
                   "                  <input type=\"checkbox\" id=\"cbselect\" class=\"selectAll\" name=\"cbselect\" onClick=\"toggleCheckboxByClassName(this,'cbShowMe');\">\n"+
                   "                  <label for=\"cbselect\"></label>\n"+
                   "               </div>\n"+
                   "            </th>\n"+
                   "            <th>"+getText("AuditLog.fieldName")+"</th>\n"+
                   "            <th>"+getText("AuditLog.fieldFilter")+"</th>\n"+
                   "         </tr>\n"+
                   "      </thead>\n"+
                   "      <tbody>\n" + html + "\n"+
                   "      </tbody>\n"+
                   "   </table>\n"+
                   "</div>" ;
            response.getWriter().append("<div class='row'><hr></div>"+html+
                    "<div class='row'>\n" +
"                        <div class=\"col-md-2 col-md-offset-5\">\n" +
"                            <button type='button' class='btn btn-primary' name='auditReportEntry' id='actionName' onclick='auditReport(); return false;' >\n" +
"                                <i class='fa fa-plus'></i>Generate Now\n" +
"                            </button>\n" +
"                        </div>\n" +
"                    </div>");
//            response.getWriter().append("<div class='row'><hr></div>"+html+"<br>"+
//            "<div class='form-horizontal form-group'>\n"+
//            "   <label class='col-md-4 control-label'>Action</label>\n"+
//            "      <div class='col-md-5 control-label'>\n"+
//            "         <button type='button' class='btn btn-primary' name='auditReportEntry' id='actionName' onclick='auditReport(); return false;' >\n"+
//            "      <i class='fa fa-plus'></i>Generate Now\n"+
//            "         </button>\n"+
//            "   </div>\n"+
//            "</div>");
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (!oneToManyList.isEmpty()) {
//        }
        return null;
    }
    
    private List<AuditTrailModel> auditList = null;
    public List<AuditTrailModel> getAuditList() {
        return auditList;
    }
    public void setAuditList(List<AuditTrailModel> auditList) {
        this.auditList = auditList;
    }
    
    private Map<String, List> auditTrailMap = null;
    public Map<String, List> getAuditTrailMap() {
        return auditTrailMap;
    }
    public void setAuditTrailMap(Map<String, List> auditTrailMap) {
        this.auditTrailMap = auditTrailMap;
    }
    
    
    private List<String> displayColList = null;
    public List<String> getDisplayColList() {
        if (displayColList == null) {
            displayColList = new ArrayList();
        }
        return displayColList;
    }
    public void setDisplayColList(List<String> displayColList) {
        this.displayColList = displayColList;
    }
    
    public Boolean sorted=Boolean.FALSE;
    public List<String> getSortedDisplayColList() {
        if (!sorted) {
            Collections.sort(getDisplayColList());
            sorted = Boolean.TRUE;
        }
        return getDisplayColList();
    }
    
    private List formattedList = null;
    public List<String> getFormattedSortedDisplayColList() {
        if (formattedList == null) {
            formattedList = new ArrayList();
            getSortedDisplayColList().forEach((col) -> {
                formattedList.add(WordUtils.capitalize(col.replace("_", " ")).replace(" Id", " ID"));
            });
        }
        return formattedList;
    }
    
    private String logDateFrom;
    public String getLogDateFrom() {
        return logDateFrom;
    }
    public void setLogDateFrom(String logDateFrom) {
        this.logDateFrom = logDateFrom;
    }

    private String logDateTo;
    public String getLogDateTo() {
        return logDateTo;
    }
    public void setLogDateTo(String logDateTo) {
        this.logDateTo = logDateTo;
    }
    
    private String logUserId;
    public String getLogUserId() {
        return logUserId;
    }
    public void setLogUserId(String logUserId) {
        this.logUserId = logUserId;
    }

    private String logRecordId;
    public String getLogRecordId() {
        return logRecordId;
    }
    public void setLogRecordId(String logRecordId) {
        this.logRecordId = logRecordId;
    }
    
    public String generate() {
        auditTrailMap = new TreeMap();
        try {
            globalRequest = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            Class modelClass = getModelClass();
            String html = "";
            List<Options> oneToManyList = new ArrayList();
            Table table = (Table)modelClass.getAnnotation(Table.class);
//            auditList = baseDAO.getSession().getNamedQuery("AuditTrailModel.findBy_tableName").setParameter("tableName", table.name()).list();
            String stmt = "select _self from AuditTrailModel _self where table_name = :tableName ";
            CriteriaConverter cc = new CriteriaConverter(Boolean.TRUE);
            if (Validator.notEmpty(logUserId)) {
                stmt += cc.strCriteria("and", "user_id", logUserId.toLowerCase());
            }
            if (Validator.notEmpty(logRecordId)) {
                stmt += cc.strCriteria("and", "record_id", logRecordId.toLowerCase());
            }
            if (Validator.notEmpty(logDateFrom)) {
                String dateFormat = getText("date_default_date_dr");
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(DateUtil.getDate(logDateFrom, dateFormat));
                } catch (Exception e) {
                    try {
                        dateFormat = "dd-MM-yyyy";
                        cal.setTime(DateUtil.getDate(logDateFrom, dateFormat));
                    } catch (Exception e2) {
                        dateFormat = "dd.MM.yyyy";
                        cal.setTime(DateUtil.getDate(logDateFrom, dateFormat));
                    }
                }
                stmt += "and date_time >= :dateFrom ";
                cc.criteriaMap.put("dateFrom", cal.getTime());
            }
            if (Validator.notEmpty(logDateTo)) {
                String dateFormat = getText("date_default_date_dr");
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(DateUtil.getDate(logDateTo, dateFormat));
                } catch (Exception e) {
                    try {
                        dateFormat = "dd-MM-yyyy";
                        cal.setTime(DateUtil.getDate(logDateTo, dateFormat));
                    } catch (Exception e2) {
                        dateFormat = "dd.MM.yyyy";
                        cal.setTime(DateUtil.getDate(logDateTo, dateFormat));
                    }
                }
                cal.add(Calendar.DAY_OF_MONTH, 1);
                stmt += "and date_time < :dateTo ";
                cc.criteriaMap.put("dateTo", cal.getTime());
            }
            stmt += " order by record_id, date_time desc";
            Query query = baseDAO.getSession().createQuery(stmt);
            for (String key : cc.criteriaMap.keySet()) {
                if (cc.criteriaMap.get(key)!=null && cc.criteriaMap.get(key).getClass().isArray()) {
                    query.setParameterList(key, (Object[])cc.criteriaMap.get(key));
                } else {
                    query.setParameter(key, cc.criteriaMap.get(key));
                }
            }
            query.setParameter("tableName", table.name());
            auditList = query.list();
            System.out.println("auditList.size = " + auditList.size());

            List list = null;
            Boolean addedDisplayColumn = Boolean.FALSE;
            for (AuditTrailModel auditTrail : auditList) {
                if (auditTrailMap.containsKey(auditTrail.getRecord_id()))  {
                    list = auditTrailMap.get(auditTrail.getRecord_id());
                } else {
                    list = new ArrayList();
                    auditTrailMap.put(auditTrail.getRecord_id(), list);
                }
                list.add(auditTrail);
                for(Method method : modelClass.getDeclaredMethods()){
                    if (!method.getName().startsWith("get")) continue;
                    Annotation[] annotations = method.getAnnotations();
                    Boolean isPK = Boolean.FALSE;
                    String name = null;
                    for (int i = 0; i < annotations.length; i++) {
                        if (annotations[i].annotationType().equals(Id.class)) {
                            isPK = Boolean.TRUE;
                        }
                        if (annotations[i].annotationType().equals(Column.class)) {
                            name = ((Column)annotations[i]).name();
                        }
                        if (annotations[i].annotationType().equals(OneToMany.class)) {
                            oneToManyList.add(new Options(parentModelName+"."+removeGet(method.getName()), ((OneToMany)annotations[i]).targetEntity().getSimpleName()));
                        }
                    }
                    if (name != null) {
                        if (name.equalsIgnoreCase("created_date") || name.equalsIgnoreCase("created_by") ||
                            name.equalsIgnoreCase("updated_date") || name.equalsIgnoreCase("updated_by")) {
                            continue; //do not generate the input fields for these columns
                        }
                        if (method.getGenericReturnType().toString().equals("class java.lang.String")) {
                        } else if (method.getGenericReturnType().toString().equals("class java.lang.Double")) {
                            name += "_str";
                        } else if (method.getGenericReturnType().toString().equals("class java.lang.Long")) {
                            name += "_str";
                        } else if (method.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                            name += "_str";
                        } else if (method.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
                            name += "_str";
                        }
                        if (Validator.notEmpty(globalRequest.getParameter(name+"_showMe"))) {//user selected to show this column
                            if (!addedDisplayColumn) {
                                getDisplayColList().add(name);
                            }
                            if (Validator.notEmpty(globalRequest.getParameter(name))) {//with filtering criteria
                                for (String criteria : globalRequest.getParameter(name).split(",")) {
                                    criteria = criteria.trim();
                                    if (criteria.contains("%")) {
                                        Pattern pattern = Pattern.compile(criteria.toLowerCase().replaceAll("%", ".*"), Pattern.DOTALL);
                                        if (pattern.matcher( ((String)auditTrail.getNewData(name)).toLowerCase()).matches()) {
                                            auditTrail.setMatchCriteria(Boolean.TRUE);
                                            auditTrail.newDataMap().put(name+"__matched", Boolean.TRUE);
                                        }
                                    } else if (criteria.startsWith(">=") || criteria.startsWith(">") || criteria.startsWith("<=") || criteria.startsWith("<")) {
                                        Boolean found = null;
                                        if (criteria.startsWith(">=")) {
                                            found = (criteria.substring(2).trim().compareToIgnoreCase((String)auditTrail.getNewData(name))>=0);
                                        } else if (criteria.startsWith(">")) {
                                            found = (criteria.substring(1).trim().compareToIgnoreCase((String)auditTrail.getNewData(name))>0);
                                        } else if (criteria.startsWith("<=")) {
                                            found = (criteria.substring(2).trim().compareToIgnoreCase((String)auditTrail.getNewData(name))<=0);
                                        } else {
                                            found = (criteria.substring(1).trim().compareToIgnoreCase((String)auditTrail.getNewData(name))<0);
                                        }
                                        if (found) {
                                            auditTrail.setMatchCriteria(Boolean.TRUE);
                                            auditTrail.newDataMap().put(name+"__matched", Boolean.TRUE);
                                        }
                                    } else {//equals
                                        if (criteria.trim().equalsIgnoreCase((String)auditTrail.getNewData(name))) {
                                            auditTrail.setMatchCriteria(Boolean.TRUE);
                                            auditTrail.newDataMap().put(name+"__matched", Boolean.TRUE);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                addedDisplayColumn = Boolean.TRUE;
            }
            for (String key : auditTrailMap.keySet()) {
                AuditTrailModel currentRecordModel = new AuditTrailModel();
                AuditTrailModel auditTrail = (AuditTrailModel)auditTrailMap.get(key).get(0);
                currentRecordModel.setRecord_id(auditTrail.getRecord_id());
                ModelBase currentModelInstance = null;
                try {
                    currentModelInstance = (ModelBase)baseDAO.getModelById(auditTrail.getRecord_id(), modelClass);
                } catch (Exception e) {
                    e.printStackTrace();
                    currentModelInstance = (ModelBase)baseDAO.getModelById(new Integer(auditTrail.getRecord_id()), modelClass);
                }
                Map map = new HashMap();
                for (String name : getDisplayColList()) {
                    try {
                        String putName = name;
                        if (putName.endsWith("_str")) {
                            putName = name.substring(0, name.length() - 4);
                        }
                        map.put(putName, getMethodValueFromObject(currentModelInstance, "get"+name.substring(0, 1).toUpperCase() + name.substring(1)));
                    } catch (Exception e) {
//                        System.out.println("\"get\"+name.substring(0, 1).toUpperCase() + name.substring(1) = " + "get"+name.substring(0, 1).toUpperCase() + name.substring(1));
                        //e.printStackTrace();
                    }
                }
                try {
                    currentRecordModel.setUser_id((String)getMethodValueFromObject(currentModelInstance, "getUpdated_by"));
                    currentRecordModel.setNewDataMap(map);
                    auditTrailMap.get(key).add(0, currentRecordModel);
                } catch (Exception e) {
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for (String key : auditTrailMap.keySet()) {
//            for (AuditTrailModel auditModel : (List<AuditTrailModel>) auditTrailMap.get(key)) {
//                System.out.println("auditModel.newDataMap = " + auditModel.newDataMap());
//                System.out.println("auditModel.oldDataMap = " + auditModel.oldDataMap());
//            }
//        }
        return "generate";
    }
    //** Generate Audit Query Page from Model : END **//
}
