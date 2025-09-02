package com.sains.framework.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.ModelBase;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author johnnuk
 */
@Entity
@Table(name = "t_api_authentication")
@NamedQueries({
    @NamedQuery(name = "ApiAuthenticationModel.findBy_apiKey_apiSecret", query = "SELECT a FROM ApiAuthenticationModel a WHERE a.api_key = :apiKey and a.api_secret = :apiSecret")})
public class ApiAuthenticationModel extends ModelBase implements java.io.Serializable {

    private String api_auth_id;
    private String api_user_id;
    private String api_app;
    private String api_app_description;
    private String api_key;
    private String api_secret;
    private String created_by;
    private Timestamp created_date;
    private String updated_by;
    private Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Api_auth_id","Api_user_id","Api_app","Api_app_description","Api_key","Api_secret"};


    private final String[] columnLength = new String[] {"api_user_id:65","api_app:50","api_app_description:200","api_key:100","api_secret:100"};
    
    private Map columnLengthMap = null;

    
    public ApiAuthenticationModel() {
    }

    public ApiAuthenticationModel(String api_auth_id) {
        this.api_auth_id = api_auth_id;
    }

    @Id
    @Column(name = "api_auth_id")
    public String getApi_auth_id() {
        return api_auth_id;
    }

    public void setApi_auth_id(String api_auth_id) {
        this.api_auth_id = api_auth_id;
    }

    @Transient 
    public String getID() { 
        if (api_auth_id == null) {
            return null;
        }
        return api_auth_id; 
    } 

    public void setID(String pk_id) {
        this.api_auth_id = pk_id;
    }

    @Column(name = "api_user_id")
    public String getApi_user_id() {
        return api_user_id;
    }

    public void setApi_user_id(String api_user_id) {
        this.api_user_id = api_user_id;
    }
    
    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }

    @Column(name = "api_app")
    public String getApi_app() {
        return api_app;
    }

    public void setApi_app(String api_app) {
        this.api_app = api_app;
    }

    @Column(name = "api_app_description")
    public String getApi_app_description() {
        return api_app_description;
    }

    public void setApi_app_description(String api_app_description) {
        this.api_app_description = api_app_description;
    }

    @Column(name = "api_key")
    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    @Column(name = "api_secret")
    public String getApi_secret() {
        return api_secret;
    }

    public void setApi_secret(String api_secret) {
        this.api_secret = api_secret;
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

    
}
