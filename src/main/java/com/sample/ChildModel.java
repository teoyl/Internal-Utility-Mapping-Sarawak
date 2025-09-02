/* ----------------------------------------------
NAME   : ChildModel.java 
CREATED BY  : Java Model Generator                   
CREATED Date:                    
UPDATED BY  :                                
UPDATED Date:                    
------------------------------------------------*/
package com.sample;

import com.sains.framework.base.CustomBaseException;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import com.sains.framework.model.DrDocRepoModel;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.Session;

@Entity
@Table(name = "t_child")
public class ChildModel extends ModelBase implements java.io.Serializable {

    private Integer child_age;
    private java.sql.Timestamp child_dob;
    private String child_id;
    private String child_name;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String parent_id;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private final String[] updatableColumns = new String[]{"Child_id", "Child_age", "Child_dob", "Child_name", "Child_file_id"};
    private final String[] columnLength = new String[]{"child_id:16", "child_name:100"};
    private Map columnLengthMap = null;

    public ChildModel() {
        
    }

    @Column(name = "child_age")
    public Integer getChild_age() {
        return child_age;
    }

    public void setChild_age(Integer child_age) {
        this.child_age = child_age;
    }
    
    @Transient
    public String getChild_age_str() {
        return child_age==null?"":Formatter.formatDecimal(child_age.toString(), Formatter.CURRENCY_PATTERN_PLAIN2);
    }

    public void setChild_age_str(String child_age) {
        try {
            this.child_age = Integer.parseInt(child_age.replaceAll(",", ""));
        } catch (Exception e){}
    }

    @Column(name = "child_dob")
    public java.sql.Timestamp getChild_dob() {
        return child_dob;
    }

    public void setChild_dob(java.sql.Timestamp child_dob) {
        this.child_dob = child_dob;
    }

    @Transient
    public String getChild_dob_str() {
        return Formatter.formatDate(child_dob, SystemConstants.DATE.dataEntryFormat);
    }

    public void setChild_dob_str(String child_dob) {
        try {
            this.child_dob = DateUtil.getTimestampFromDate(DateUtil.getDate(child_dob, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e) {
        }
    }

    @Id
    @Column(name = "child_id")
    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    @Transient
    public String getID() {
        return child_id;
    }

    public void setID(String id) {
        this.child_id = id;
    }

    @Column(name = "child_name")
    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Column(name = "created_date")
    public java.sql.Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(java.sql.Timestamp created_date) {
        this.created_date = created_date;
    }

    @Column(name = "parent_id")
    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
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
    private ParentModel parentModel;

    @ManyToOne(targetEntity = ParentModel.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false, nullable = true)
    public ParentModel getParentModel() {
        return parentModel;
    }

    public void setParentModel(ParentModel parentModel) {
        this.parentModel = parentModel;
    }
    
    private List grandChildList = new ArrayList();
    @OneToMany(targetEntity = GrandChildModel.class, fetch = FetchType.LAZY, mappedBy = "child_id")
    public List<GrandChildModel> getGrandChildList() {
        return grandChildList;
    }
    public void setGrandChildList(List<GrandChildModel> grandChildList) {
        this.grandChildList = grandChildList;
    }
    
    private String child_file_id;
    @Column(name = "child_file_id")
    public String getChild_file_id() {
        return child_file_id;
    }
    public void setChild_file_id(String child_file_id) {
        this.child_file_id = child_file_id;
    }
    
    DrDocRepoModel childDrDocModel = null;
    @OneToOne(targetEntity=DrDocRepoModel.class, optional=true, fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="dr_doc_id", name="child_file_id", insertable=false, updatable=false, nullable = true)
    public DrDocRepoModel getChildDrDocModel() {
            return childDrDocModel;
    }
    public void setChildDrDocModel(DrDocRepoModel childDrDocModel) {
            this.childDrDocModel = childDrDocModel;
    }
}
