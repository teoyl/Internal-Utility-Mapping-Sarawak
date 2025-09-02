package com.sains.framework.model;

import com.sains.common.util.Options;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonList;
import com.sains.framework.base.ModelBase;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name = "t_system_parameter")
@NamedQueries({
    @NamedQuery(name = "Parameter.getByParameter_code",query = "from ParameterModel where system_code = :system_code and parameter_code = :parameter_code"),
    @NamedQuery(name = "Parameter.getByParameter_code_allSys",query = "from ParameterModel where parameter_code = :parameter_code")
})
public class ParameterModel extends ModelBase implements java.io.Serializable {

    private String parameter_id;
    private String system_code;
    private String system_descs;
    private String parameter_code;
    private String parameter_descs;
    private String parameter_value;
    private String value_explaination;
    private String parameter_setup;
    private String group_code;
    private Integer group_item_order;
    private String field_indicator;
    private String field_input_label;
    private String numericJavascript;
    private String descs_width;
    private String req_field;
    private String created_by;
    private String wf_system;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    public String[] updatableColumns = new String[]{"Parameter_id","parameter_value"};
    private final String[] columnLength = new String[]{"system_code:50", "parameter_code:50", "parameter_value:20", "parameter_setup:100"};
    private Map columnLengthMap = null;

    public ParameterModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }

    @Id
//    @GeneratedValue
    @Column(name = "parameter_id")
    public String getParameter_id() {
        return parameter_id;
    }

    public void setParameter_id(String parameter_id) {
        this.parameter_id = parameter_id;
    }

    @Transient
    public String getID() {
        return parameter_id;
    }

    public void setID(String pk_id) {
        this.parameter_id = pk_id;
    }

    @Column(name = "system_code")
    public String getSystem_code() {
        return system_code;
    }

    public void setSystem_code(String system_code) {
        this.system_code = system_code;
    }

    @Column(name = "system_descs")
    public String getSystem_descs() {
        return system_descs;
    }

    public void setSystem_descs(String system_descs) {
        this.system_descs = system_descs;
    }

    @Column(name = "parameter_code")
    public String getParameter_code() {
        return parameter_code;
    }

    public void setParameter_code(String parameter_code) {
        this.parameter_code = parameter_code;
    }

    @Column(name = "parameter_descs")
    public String getParameter_descs() {
        return parameter_descs;
    }

    public void setParameter_descs(String parameter_descs) {
        this.parameter_descs = parameter_descs;
    }

    @Column(name = "parameter_value")
    public String getParameter_value() {
        return parameter_value;
    }

    public void setParameter_value(String parameter_value) {
        this.parameter_value = parameter_value;
    }

    @Column(name = "value_explaination")
    public String getValue_explaination() {
        return value_explaination;
    }

    public void setValue_explaination(String value_explaination) {
        this.value_explaination = value_explaination;
    }

    @Column(name = "parameter_setup")
    public String getParameter_setup() {
        return parameter_setup;
    }

    public void setParameter_setup(String parameter_setup) {
        this.parameter_setup = parameter_setup;
    }

    @Column(name = "created_date")
    public java.sql.Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(java.sql.Timestamp created_date) {
        this.created_date = created_date;
    }

    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Column(name = "updated_by")
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    @Column(name = "updated_date")
    public java.sql.Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(java.sql.Timestamp updated_date) {
        this.updated_date = updated_date;
    }

    @Column(name="wf_system")
    public String getWf_system() {
    	return wf_system;
    }

    public void setWf_system(String wf_system) {
    	this.wf_system = wf_system;
    }

    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }

    @Transient
    public Map getColumnLengthMap() {
        if (columnLengthMap == null) {
            columnLengthMap = new HashMap();
            String[] strA = null;

            for (String str : columnLength) {
                strA = str.split(":");
                columnLengthMap.put(strA[0], strA[1]);
            }
        }

        return columnLengthMap;
    }

    @Transient
    public String getParamType(){
        String[] setupArr = parameter_setup.split(";");
        BaseDAO dao = new BaseDAOImpl();
        try {
            if (setupArr[0].equalsIgnoreCase("textfield")) {
                if (setupArr.length > 1) {
                    textFieldWidth = setupArr[1].trim();
                }
                return setupArr[0];
            } else if (setupArr[0].equalsIgnoreCase("select") || setupArr[0].equalsIgnoreCase("radio")) {
                radioSelectList = new ArrayList();
                if (setupArr[1].equalsIgnoreCase("CL")) {
                    CommonList commonList = new CommonList(dao.getSession());
                    try {
                        Method m = commonList.getClass().getMethod(setupArr[2]);
                        radioSelectList = (List) m.invoke(commonList);
                    } catch (Exception e){}
                } else { //if not CL (CommonList) then must be Manual
                    String[] dataItemArr;
                    StringTokenizer manualListTokenizer = new StringTokenizer(setupArr[2], new String("|"));
                    while (manualListTokenizer.hasMoreTokens()) {
                        String token = manualListTokenizer.nextToken().trim();
                        dataItemArr = token.split("__");
                        radioSelectList.add(new Options(dataItemArr[0], dataItemArr[1]));
                    }
                }
            }
        } finally {
            dao.closeSession();
        }
        return setupArr[0];
    }

    private List radioSelectList = null;
    @Transient
    public List getRadioSelectList() {
        return radioSelectList;
    }

    private String textFieldWidth = "100";
    @Transient
    public String getTextFieldWidth() {
        return textFieldWidth;
    }

    public String getField_indicator() {
        return field_indicator;
    }

    public void setField_indicator(String field_indicator) {
        this.field_indicator = field_indicator;
    }

    public String getField_input_label() {
        return field_input_label;
    }

    public void setField_input_label(String field_input_label) {
        this.field_input_label = field_input_label;
    }

    public String getGroup_code() {
        return group_code;
    }

    public void setGroup_code(String group_code) {
        this.group_code = group_code;
    }

    public Integer getGroup_item_order() {
        return group_item_order;
    }

    public void setGroup_item_order(Integer group_item_order) {
        this.group_item_order = group_item_order;
    }

    public String getNumericJavascript() {
        return numericJavascript;
    }

    public void setNumericJavascript(String numericJavascript) {
        this.numericJavascript = numericJavascript;
    }

    public String getDescs_width() {
        return descs_width;
    }

    public void setDescs_width(String descs_width) {
        this.descs_width = descs_width;
    }

    public String getReq_field() {
        return req_field;
    }

    public void setReq_field(String req_field) {
        this.req_field = req_field;
    }

    private String lookUp = "";
    
    @Transient
    public String getLookUp() {
        return lookUp;
    }
    //populate column name in JSP to be replace by PopUp
    public void setLookUp(String fieldName) {
        this.lookUp = numericJavascript.replaceAll("column", fieldName);
    }



    /* ******** Write your code after this line ****** */
}
