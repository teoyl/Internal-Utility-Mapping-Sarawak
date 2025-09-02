/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webservice.model;

import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.ModelBase;

/**
 *
 * @author Aiman 25/07/2024
 * 
 * Following KuehLK eMinds API Sample
 * 
 */
@Entity
@Table(name = "t_ws_client")
public class WsClientModel {

    private String client_id = "";
    private String client_login;
    private String client_pwd;
    private String client_name;
    private String active_flag = "Y";
    private String subs = "";  // ThoTH @ 31-May-2019
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
        
    public WsClientModel() {
    }

    @Id
    @Column(name="client_id")
    public String getClient_id() {return client_id;}
    public void setClient_id(String client_id) {this.client_id = client_id;}

    @Transient 
    public String getID() { return client_id; } 
    public void setID(String pk_id) {this.client_id = pk_id;}

    @Column(name="client_login")
    public String getClient_login() {return client_login;}
    public void setClient_login(String client_login) {this.client_login = client_login;}

    @Column(name="client_pwd")
    public String getClient_pwd() {return client_pwd;}
    public void setClient_pwd(String client_pwd) {this.client_pwd = client_pwd;}

    @Column(name="client_name")
    public String getClient_name() {return client_name;}
    public void setClient_name(String client_name) {this.client_name = client_name;}
    
    @Column(name="created_by")
    public String getCreated_by() {return created_by;}
    public void setCreated_by(String created_by) {this.created_by = created_by;}

    @Column(name="created_date")
    public java.sql.Timestamp getCreated_date() {return created_date;}
    public void setCreated_date(java.sql.Timestamp created_date) {this.created_date = created_date;}
    
    @Transient
    public String getCreated_date_str() {
        return Formatter.formatDate(created_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setCreated_date_str(String created_date) {
        try {
            this.created_date = DateUtil.getTimestampFromDate(DateUtil.getDate(created_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

    @Column(name="updated_by")
    public String getUpdated_by() {return updated_by;}
    public void setUpdated_by(String updated_by) {this.updated_by = updated_by;}

    @Column(name="updated_date")
    public java.sql.Timestamp getUpdated_date() {return updated_date;}
    public void setUpdated_date(java.sql.Timestamp updated_date) {this.updated_date = updated_date;}

    @Transient
    public String getUpdated_date_str() {
        return Formatter.formatDate(updated_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setUpdated_date_str(String updated_date) {
        try {
            this.updated_date = DateUtil.getTimestampFromDate(DateUtil.getDate(updated_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

}
