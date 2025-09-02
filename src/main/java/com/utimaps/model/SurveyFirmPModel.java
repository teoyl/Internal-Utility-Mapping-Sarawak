/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.model;

import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.ModelBase;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "US_SURVEY_FIRM_P")
public class SurveyFirmPModel extends ModelBase implements java.io.Serializable{
    private String so_app_id;
    private String case_id;
    private String firm_soc;
    private String firm_oic;
    private String firm_email;
    private String firm_country_code;
    private String firm_contact_num;
    private String firm_fax_country_code;
    private String firm_fax_num;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{};
    
    public String[] columnLength = new String[]{};

    @Id
    @Column(name = "so_app_id")
    public String getSo_app_id() {
        return so_app_id;
    }

    public void setSo_app_id(String so_app_id) {
        this.so_app_id = so_app_id;
    }

    @Column(name = "case_id")
    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    @Column(name = "firm_soc")
    public String getFirm_soc() {
        return firm_soc;
    }

    public void setFirm_soc(String firm_soc) {
        this.firm_soc = firm_soc;
    }

    @Column(name = "firm_oic")
    public String getFirm_oic() {
        return firm_oic;
    }

    public void setFirm_oic(String firm_oic) {
        this.firm_oic = firm_oic;
    }

    @Column(name = "firm_email")
    public String getFirm_email() {
        return firm_email;
    }

    public void setFirm_email(String firm_email) {
        this.firm_email = firm_email;
    }

    @Column(name = "firm_country_code")
    public String getFirm_country_code() {
        return firm_country_code;
    }

    public void setFirm_country_code(String firm_country_code) {
        this.firm_country_code = firm_country_code;
    }

    @Column(name = "firm_contact_num")
    public String getFirm_contact_num() {
        return firm_contact_num;
    }

    public void setFirm_contact_num(String firm_contact_num) {
        this.firm_contact_num = firm_contact_num;
    }

    @Column(name = "firm_fax_country_code")
    public String getFirm_fax_country_code() {
        return firm_fax_country_code;
    }

    public void setFirm_fax_country_code(String firm_fax_country_code) {
        this.firm_fax_country_code = firm_fax_country_code;
    }

    @Column(name = "firm_fax_num")
    public String getFirm_fax_num() {
        return firm_fax_num;
    }

    public void setFirm_fax_num(String firm_fax_num) {
        this.firm_fax_num = firm_fax_num;
    }

    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Column(name = "created_date")
    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    @Column(name = "updated_by")
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    @Column(name = "updated_date")
    public Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Timestamp updated_date) {
        this.updated_date = updated_date;
    }
    
    @Transient
    public String[] getColumnLength() {
        return columnLength;
    }
    
    public void setColumnLength(String[] columnLength) {
        this.columnLength = columnLength;
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
    
    private String company_name = "";
    @Transient
    public String getCompany_name() {
        
        CommonFunction cf = new CommonFunction();
        company_name = cf.getPubcodeDesc("SOC", firm_soc);
//        System.out.println("company_name " + company_name);
        return company_name;
    }
    
    
    
}
