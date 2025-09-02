/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lxg.prepayment.model;
/**
 *
 * @author joveni.h
 */
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.ModelBase;

@Entity
@Table(name="PP_BALANCE")
public class PpBalanceModel extends ModelBase implements java.io.Serializable {
    private String bal_id = "";
    private String us_id;
    private String co_id;
    private String bal_type;
    private Double bal_amount;
    private String bal_status;
    private Integer hibernate_version;
    private java.sql.Timestamp created_date;
    private String created_by;
    private java.sql.Timestamp updated_date;
    private String updated_by;

    public String[] updatableColumns = new String[] {"Bal_id","Bal_amount","Bal_status"};

 
    private final String[] columnLength = new String[] {"bal_id:16","us_id:16","co_id:16","bal_type:1","bal_status:3"};
    private Map columnLengthMap = null;

//    public PpBalanceModel() {
//        //userDefined_autoValidation(Boolean.TRUE);
//        //userDefined_insertNoDuplicate("module_code;module.code");
//        //userDefined_validateRecursive(Boolean.TRUE);
//        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
//    }


    @Id
    @Column(name="bal_id")
    public String getBal_id() {
    	return bal_id;
    }

    public void setBal_id(String bal_id) {
    	this.bal_id = bal_id;
    }

    @Transient 
    public String getID() { 
        return bal_id; 
    } 

    public void setID(String pk_id) {
        this.bal_id = pk_id;
    }

    @Column(name="us_id")
    public String getUs_id() {
    	return us_id;
    }

    public void setUs_id(String us_id) {
    	this.us_id = us_id;
    }

    @Column(name="co_id")
    public String getCo_id() {
    	return co_id;
    }

    public void setCo_id(String co_id) {
    	this.co_id = co_id;
    }

    @Column(name="bal_type")
    public String getBal_type() {
    	return bal_type;
    }

    public void setBal_type(String bal_type) {
    	this.bal_type = bal_type;
    }

    @Column(name="bal_amount")
    public Double getBal_amount() {
    	return bal_amount;
    }

    public void setBal_amount(Double bal_amount) {
    	this.bal_amount = bal_amount;
    }

    @Column(name="bal_status")
    public String getBal_status() {
    	return bal_status;
    }

    public void setBal_status(String bal_status) {
    	this.bal_status = bal_status;
    }

    @Version
    @Column(name="hibernate_version")
    public Integer getHibernate_version() {
    	return hibernate_version;
    }

    public void setHibernate_version(Integer hibernate_version) {
    	this.hibernate_version = hibernate_version;
    }

    @Column(name="created_date")
    public java.sql.Timestamp getCreated_date() {
    	return created_date;
    }

    public void setCreated_date(java.sql.Timestamp created_date) {
    	this.created_date = created_date;
    }

    @Transient
    public String getCreated_date_str() {
        return Formatter.formatDate(created_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setCreated_date_str(String created_date) {
        try {
            this.created_date = DateUtil.getTimestampFromDate(DateUtil.getDate(created_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

    @Column(name="created_by")
    public String getCreated_by() {
    	return created_by;
    }

    public void setCreated_by(String created_by) {
    	this.created_by = created_by;
    }

    @Column(name="updated_date")
    public java.sql.Timestamp getUpdated_date() {
    	return updated_date;
    }

    public void setUpdated_date(java.sql.Timestamp updated_date) {
    	this.updated_date = updated_date;
    }

    @Transient
    public String getUpdated_date_str() {
        return Formatter.formatDate(updated_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setUpdated_date_str(String updated_date) {
        try {
            this.updated_date = DateUtil.getTimestampFromDate(DateUtil.getDate(updated_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

    @Column(name="updated_by")
    public String getUpdated_by() {
    	return updated_by;
    }

    public void setUpdated_by(String updated_by) {
    	this.updated_by = updated_by;
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

    /* ******** Write your code after this line ****** */
    private List<PpTranModel> tranList = new ArrayList();
    @OneToMany(targetEntity = PpTranModel.class, fetch = FetchType.LAZY, mappedBy = "bal_id")
    public List<PpTranModel> getTranList() {
        return tranList;
    }

    public void setTranList(List<PpTranModel> tranList) {
        this.tranList = tranList;
    }

//    private PublicUser publicUser = new PublicUser();
//    @OneToOne(targetEntity = PublicUser.class, optional = true, fetch = FetchType.LAZY)
//    @JoinColumn(name = "us_id", insertable = false, updatable = false, nullable = true)
//    public PublicUser getPublicUser() {
//        return publicUser;
//    }
//
//    public void setPublicUser(PublicUser publicUser) {
//        this.publicUser = publicUser;
//    }

    
    
}
