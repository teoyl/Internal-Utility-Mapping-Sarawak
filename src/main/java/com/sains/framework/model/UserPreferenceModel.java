package com.sains.framework.model;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.sains.framework.base.ModelBase;

@Entity
@Table(name = "t_user_preference")
public class UserPreferenceModel extends ModelBase implements java.io.Serializable {

    private String user_pref_id;
    private String us_id;
    private String pref_code;
    private String pref_value;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private final String[] updatableColumns = new String[]{"User_pref_id","Pref_value"};
    private final String[] columnLength = new String[]{"us_id:16", "pref_code:20", "pref_value:100", "user_pref_id:16"};
    private Map columnLengthMap = null;

    public UserPreferenceModel() {
        
    }

    @Column(name = "us_id")
    public String getUs_id() {
        return us_id;
    }

    public void setUs_id(String us_id) {
        this.us_id = us_id;
    }

    @Transient
    public String getID() {
        return user_pref_id;
    }

    public void setID(String id) {
        this.user_pref_id = id;
    }

    @Column(name = "pref_code")
    public String getPref_code() {
        return pref_code;
    }

    public void setPref_code(String pref_code) {
        this.pref_code = pref_code;
    }

    @Column(name = "pref_value")
    public String getPref_value() {
        return pref_value;
    }

    public void setPref_value(String pref_value) {
        this.pref_value = pref_value;
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
    @Column(name = "user_pref_id")
    public String getUser_pref_id() {
        return user_pref_id;
    }

    public void setUser_pref_id(String user_pref_id) {
        this.user_pref_id = user_pref_id;
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
