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
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.ModelBase;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name="t_api_token")
@NamedQueries({
    @NamedQuery(name = "ApiTokenModel.findBy_accessToken",query = "from ApiTokenModel where access_token = :accessToken"),
    @NamedQuery(name = "ApiTokenModel.findBy_refreshToken",query = "from ApiTokenModel where refresh_token = :refreshToken")
})
public class ApiTokenModel extends ModelBase implements java.io.Serializable {
    private String api_token_id = "";
    private String access_token;
    private String api_auth_id;
    private String apiapp;
    private String created_by;
    private java.sql.Timestamp created_date;
    private Long created_long;
    private Long expires_in;
    private String refresh_token;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private String userid;
    private String active_flag;

    private final String[] updatableColumns = new String[] {"Api_token_id","Access_token","Api_auth_id","Apiapp","Created_long","Expires_in","Refresh_token","Userid"};

    private final String[] columnLength = new String[] {"api_token_id:20","access_token:30","api_auth_id:20","apiapp:50","refresh_token:30","userid:20"};
    private Map columnLengthMap = null;

    public ApiTokenModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="api_token_id")
    public String getApi_token_id() {
    	return api_token_id;
    }

    public void setApi_token_id(String api_token_id) {
    	this.api_token_id = api_token_id;
    }

    @Transient 
    public String getID() { 
        return api_token_id; 
    } 

    public void setID(String pk_id) {
        this.api_token_id = pk_id;
    }

    @Column(name="access_token")
    public String getAccess_token() {
    	return access_token;
    }

    public void setAccess_token(String access_token) {
    	this.access_token = access_token;
    }

    @Column(name="api_auth_id")
    public String getApi_auth_id() {
    	return api_auth_id;
    }

    public void setApi_auth_id(String api_auth_id) {
    	this.api_auth_id = api_auth_id;
    }

    @Column(name="apiapp")
    public String getApiapp() {
    	return apiapp;
    }

    public void setApiapp(String apiapp) {
    	this.apiapp = apiapp;
    }

    @Column(name="created_by")
    public String getCreated_by() {
    	return created_by;
    }

    public void setCreated_by(String created_by) {
    	this.created_by = created_by;
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

    @Column(name="created_long")
    public Long getCreated_long() {
    	return created_long;
    }

    public void setCreated_long(Long created_long) {
    	this.created_long = created_long;
    }

    @Transient
    public String getCreated_long_str() {
        return created_long==null?"":Formatter.formatDecimal(created_long.toString(), Formatter.CURRENCY_PATTERN_PLAIN2);
    }

    public void setCreated_long_str(String created_long) {
        try {
            this.created_long = Long.parseLong(created_long.replaceAll(",", ""));
        } catch (Exception e){}
    }

    @Column(name="expires_in")
    public Long getExpires_in() {
    	return expires_in;
    }

    public void setExpires_in(Long expires_in) {
    	this.expires_in = expires_in;
    }

    @Transient
    public String getExpires_in_str() {
        return expires_in==null?"":expires_in.toString();
//        return expires_in==null?"":Formatter.formatDecimal(expires_in.toString(), Formatter.CURRENCY_PATTERN_PLAIN2);
    }

    public void setExpires_in_str(String expires_in) {
        try {
            this.expires_in = Long.parseLong(expires_in.replaceAll(",", ""));
        } catch (Exception e){}
    }

    @Column(name="refresh_token")
    public String getRefresh_token() {
    	return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
    	this.refresh_token = refresh_token;
    }

    @Column(name="updated_by")
    public String getUpdated_by() {
    	return updated_by;
    }

    public void setUpdated_by(String updated_by) {
    	this.updated_by = updated_by;
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

    @Column(name="userid")
    public String getUserid() {
    	return userid;
    }

    public void setUserid(String userid) {
    	this.userid = userid;
    }
    
    @Column(name="active_flag")
    public String getActive_flag() {
        return active_flag;
    }

    public void setActive_flag(String active_flag) {
        this.active_flag = active_flag;
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
