/* ----------------------------------------------
NAME   : ChildModel.java 
CREATED BY  : Java Model Generator                   
CREATED Date:                    
UPDATED BY  :                                
UPDATED Date:                    
------------------------------------------------*/
package com.sample;

import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.BaseException;
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
import com.sains.framework.base.ModelBase;
import org.hibernate.Session;

@Entity
@Table(name = "t_grand_child")
public class GrandChildModel extends ModelBase implements java.io.Serializable {

    private String grandchild_id;
    private String child_id;
    private String gc_name;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private final String[] updatableColumns = new String[]{"Grandchild_id", "Gc_name"};
    private final String[] columnLength = new String[]{"child_id:16", "gc_name:100", "grandchild_id:16"};
    private Map columnLengthMap = null;

    public GrandChildModel() {
        userDefined_autoValidation(Boolean.TRUE);
//        userDefined_insertNoDuplicate("Gc_name;Grand Child Name");
        userDefined_insertNoDuplicateRange("child_id,gc_name");
        userDefined_duplicateFieldsRangeKeyDescription("GrandChild.gc_name");
        //userDefined_validateRecursive(Boolean.TRUE);
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }

    @Column(name = "child_id")
    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    @Transient
    public String getID() {
        return grandchild_id;
    }

    public void setID(String id) {
        this.grandchild_id = id;
    }

    @Column(name = "gc_name")
    public String getGc_name() {
        return gc_name;
    }

    public void setGc_name(String gc_name) {
        this.gc_name = gc_name;
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

    @Id
    @Column(name = "grandchild_id")
    public String getGrandchild_id() {
        return grandchild_id;
    }

    public void setGrandchild_id(String grandchild_id) {
        this.grandchild_id = grandchild_id;
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

}
