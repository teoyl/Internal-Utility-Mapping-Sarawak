package com.sains.framework.model;

import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "item_category")
public class ItemCategory implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String item_category_id;
    private String item_category_name = "";
    private String item_category_code = "";
    private ItemCategory parentCategory;
    private List<ItemCategory> childCategory;
    //private List<Module> attachedApplication;
    private String parent_item_category_id;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private final String[] updatableColumns = new String[]{"Item_category_id", "Item_category_name", "Item_category_code", "Parent_item_category_id", "Updated_by", "Updated_date"};

    @Id
    @Column(name = "item_category_id")
    public String getItem_category_id() {
        return item_category_id;
    }

    public void setItem_category_id(String item_category_id) {
        this.item_category_id = item_category_id;
    }

    @Transient
    public String getID() {
        return item_category_id;
    }

    public void setID(String item_category_id) {
        setItem_category_id(item_category_id);
    }

    @Column(name = "item_category_name")
    public String getItem_category_name() {
        return item_category_name;
    }

    public void setItem_category_name(String item_category_name) {
        this.item_category_name = item_category_name;
    }

    @Column(name = "item_category_code")
    public String getItem_category_code() {
        return item_category_code;
    }

    public void setItem_category_code(String item_category_code) {
        this.item_category_code = item_category_code;
    }

    @Column(name = "parent_item_category_id")
    public String getParent_item_category_id() {
        return parent_item_category_id;
    }

    public void setParent_item_category_id(String parent_item_category_id) {
        this.parent_item_category_id = parent_item_category_id;
    }


    @ManyToOne(targetEntity = ItemCategory.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_item_category_id", insertable = false, updatable = false, nullable = true)
    public ItemCategory getParentCategory() {
        return parentCategory;
    }



    public void setParentCategory(ItemCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    @OneToMany(targetEntity = ItemCategory.class, fetch = FetchType.LAZY, mappedBy = "parent_item_category_id")
    public List<ItemCategory> getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(List<ItemCategory> childModule) {
        this.childCategory = childModule;
    }

//    @OneToMany(targetEntity = Application.class, fetch = FetchType.LAZY, mappedBy = "attached_module_id")
//    public List<Module> getAttachedApplication() {
//        return attachedApplication;
//    }
//
//    public void setAttachedApplication(List<Module> attachedApplication) {
//        this.attachedApplication = attachedApplication;
//    }

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
}
