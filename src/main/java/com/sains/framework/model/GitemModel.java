/* ----------------------------------------------
   NAME   : GitemModel.java 
   CREATED BY  : Java Model Generator                   
   CREATED Date:                    
   UPDATED BY  :                                
   UPDATED Date:                    
 ------------------------------------------------*/



package com.sains.framework.model;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="G_ITEM")
public class GitemModel implements java.io.Serializable {
    private String gitem_id = "";
    private String gitem_code;
    private String gitem_modelno;
    private String gitem_name;
    private String img_name;
    private String item_category_id;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Gitem_id","Gitem_code","Gitem_modelno","Gitem_name","Img_name","Item_category_id"};


    private final String[] columnLength = new String[] {"gitem_id:16","gitem_code:30","gitem_modelno:30","gitem_name:50","img_name:30","item_category_id:16"};
    private Map columnLengthMap = null;


    @Id
    @Column(name="gitem_id")
    public String getGitem_id() {
    	return gitem_id;
    }

    public void setGitem_id(String gitem_id) {
    	this.gitem_id = gitem_id;
    }

    @Transient 
    public String getID() { 
        return gitem_id; 
    } 

    public void setID(String pk_id) {
        this.gitem_id = pk_id;
    }

    @Column(name="gitem_code")
    public String getGitem_code() {
    	return gitem_code;
    }

    public void setGitem_code(String gitem_code) {
    	this.gitem_code = gitem_code;
    }

    @Column(name="gitem_modelno")
    public String getGitem_modelno() {
    	return gitem_modelno;
    }

    public void setGitem_modelno(String gitem_modelno) {
    	this.gitem_modelno = gitem_modelno;
    }

    @Column(name="gitem_name")
    public String getGitem_name() {
    	return gitem_name;
    }

    public void setGitem_name(String gitem_name) {
    	this.gitem_name = gitem_name;
    }

    @Column(name="img_name")
    public String getImg_name() {
    	return img_name;
    }

    public void setImg_name(String img_name) {
    	this.img_name = img_name;
    }

    @Column(name="item_category_id")
    public String getItem_category_id() {
    	return item_category_id;
    }

    public void setItem_category_id(String item_category_id) {
    	this.item_category_id = item_category_id;
    }

    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String createdBy) {
        created_by = createdBy;
    }

    @Column(name = "created_date")
    public java.sql.Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(java.sql.Timestamp createdDate) {
        created_date = createdDate;
    }

    @Column(name = "updated_by")
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updatedBy) {
        updated_by = updatedBy;
    }

    @Column(name = "updated_date")
    public java.sql.Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(java.sql.Timestamp updatedDate) {
        updated_date = updatedDate;
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

    private ItemCategory itemCategory;
    @ManyToOne(targetEntity=Module.class, optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="item_category_id", insertable=false, updatable=false, nullable = true)
    public ItemCategory getItemCategory() {
            return itemCategory;
    }
    public void setItemCategory(ItemCategory parentModule) {
            this.itemCategory = parentModule;
    }
}
