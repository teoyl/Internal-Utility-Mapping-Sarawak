 /* ----------------------------------------------
NAME   : ParentModel.java 
CREATED BY  : Java Model Generator                   
CREATED Date:                    
UPDATED BY  :                                
UPDATED Date:                    
------------------------------------------------*/
package com.sains.framework.model;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.sains.framework.base.ModelBase;
import org.hibernate.Session;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name = "t_setup_user_key")
@NamedQueries({
    @NamedQuery(name = "UserKey.findByUsId",query = "from UserPubPriModel where us_id = :us_id"),
    @NamedQuery(name = "UserKey.findByUsId_deviceId",query = "from UserPubPriModel where us_id = :us_id and device_id = :device_id")
})
public class UserPubPriModel extends ModelBase implements java.io.Serializable {

    private String created_by;
    private java.sql.Timestamp created_date;
    private String us_id;
    private String device_id;
    private String pub_key;
    private String user_key_id;
    private String pri_key;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private final String[] updatableColumns = new String[]{"User_key_id", "Us_id","Pub_key", "Pri_key", "Device_id"};
    private final String[] columnLength = new String[]{"pub_key:1", "user_key_id:16", "pri_key:100"};
    private Map columnLengthMap = null;

    public UserPubPriModel() {
        showNotOverrideMethod = Boolean.TRUE;
        userDefined_autoValidation(Boolean.TRUE);
        userDefined_insertNoDuplicate("us_id;Duplicated UserID");
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

    @Column(name = "us_id")
    public String getUs_id() {
        return us_id;
    }

    public void setUs_id(String us_id) {
        this.us_id = us_id;
    }
    
    @Column(name = "device_id")
    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    @Column(name = "pub_key")
    public String getPub_key() {
        return pub_key;
    }

    public void setPub_key(String pub_key) {
        this.pub_key = pub_key;
    }

    @Id
    @Column(name = "user_key_id")
    public String getUser_key_id() {
        return user_key_id;
    }

    public void setUser_key_id(String user_key_id) {
        this.user_key_id = user_key_id;
    }

    @Transient
    public String getID() {
        return user_key_id;
    }

    public void setID(String id) {
        this.user_key_id = id;
    }

    @Column(name = "pri_key")
    public String getPri_key() {
        return pri_key;
    }

    public void setPri_key(String pri_key) {
        this.pri_key = pri_key;
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
    @Override
    public void preDelete(Session session, Object deletingObject) throws Exception {
    }
    
    @Override
    public void manualOperation(org.hibernate.Session session) throws Exception {
        
    }
    
    @Override
    public void preInsert(Session session) throws Exception {
        
    }
//    @Override
//    public void preUpdate(Session session, Object updatingModel) throws Exception {
//    }
}
