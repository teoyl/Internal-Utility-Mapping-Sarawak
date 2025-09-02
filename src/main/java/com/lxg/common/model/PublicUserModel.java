/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lxg.common.model;

import com.sains.framework.base.ModelBase;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 *
 * @author seren
 */
@Entity
    @Table(name = "t_setup_user_p")
    public class PublicUserModel extends ModelBase implements java.io.Serializable {
    private String us_id;
    private String us_user_id;
    private String us_password;
    private String us_id_type;
    private String us_id_number;
    private String us_user_name;
    private String us_nationality = "MY";
    private String us_preferred_contact;
    private String us_email;
    private String us_hp_number;
    private String us_profession;
    private String us_mailing_address1;
    private String us_mailing_address2;
    private String us_mailing_address3;
    private String us_mailing_postcode;
    private String us_mailing_city;
    private String us_mailing_state;
    private String us_status = "N";
    private String us_admin = "N";
    private java.sql.Timestamp us_last_login_date;
    private java.sql.Timestamp us_effective_date;
    private java.sql.Timestamp us_cancel_date;
    private java.sql.Timestamp created_date;
    private String created_by;
    private java.sql.Timestamp updated_date;
    private String updated_by;
    private String us_user_type = "";
    private String us_verify_by;
    private java.sql.Timestamp us_verify_date;
    private Integer us_fail_attempt_count;
    private java.sql.Timestamp us_fail_login_date;
    private String us_division;
    private String us_upgrade;
    private String us_internal = "N";
    private java.sql.Timestamp us_upgrade_date;
    private String us_upgrade_by;
    private String us_hp_country_code = "60";
    private java.sql.Timestamp us_expiry_date;
    private String us_internal_section;
    private java.sql.Timestamp us_email_date;
    private String us_email_by;
    private String us_tc_accept_version;
    private String pubkey;
    private java.sql.Timestamp us_password_expiry;
    private String us_internal_storefront;
    private String user_type_el = "C";
    private String fim_id;
//    private String us_identity_type;
    private String fim_user_id;
    private String ldap_user_id;
    public String[] updatableColumns = new String[]{"Us_Id", "Us_User_Id", "Us_Password", "Us_Id_Type", "Us_Id_Number", "Us_User_Name", "Us_Nationality", "Us_Preferred_Contact", "Us_Email", "Us_Hp_Number", "Us_Profession", "Us_Mailing_Address1", "Us_Mailing_Address2", "Us_Mailing_Address3", "Us_Mailing_Postcode", "Us_Mailing_City", "Us_Mailing_State", "Us_Status", "Us_Admin", "Us_Last_Login_Date", "Us_Effective_Date", "Us_Cancel_Date", "Created_Date", "Created_By", "Updated_Date", "Updated_By", "Us_User_Type", "Us_Verify_By", "Us_Verify_Date", "Us_Fail_Attempt_Count", "Us_Fail_Login_Date", "Us_Division", "Us_Upgrade", "Us_Internal", "Us_Upgrade_Date", "Us_Upgrade_By", "Us_Hp_Country_Code", "Us_Expiry_Date", "Us_Internal_Section", "Us_Email_Date", "Us_Email_By", "Us_Tc_Accept_Version", "Pubkey", "Us_Password_Expiry", "Us_Internal_Storefront", "User_Type_El"};

    private final String[] columnLength = new String[]{"us_id:16", "us_user_id:50", "us_password:200", "us_id_type:3", "us_id_number:16", "us_user_name:100", "us_nationality:3", "us_preferred_contact:3", "us_email:50", "us_hp_number:20", "us_profession:100", "us_mailing_address1:50", "us_mailing_address2:50", "us_mailing_address3:30", "us_mailing_postcode:5", "us_mailing_city:20", "us_mailing_state:3", "us_status:1", "us_admin:1", "us_last_login_date:7", "us_effective_date:7", "us_cancel_date:7", "created_date:7", "created_by:20", "updated_date:7", "updated_by:20", "us_user_type:1", "us_verify_by:16", "us_verify_date:7", "us_fail_attempt_count:22", "us_fail_login_date:7", "us_division:3", "us_upgrade:1", "us_internal:1", "us_upgrade_date:7", "us_upgrade_by:20", "us_hp_country_code:15", "us_expiry_date:7", "us_internal_section:3", "us_email_date:7", "us_email_by:20", "us_tc_accept_version:5", "pubkey:16", "us_password_expiry:7", "us_internal_storefront:1", "user_type_el:1"};

    @Id
    @Column(name = "us_id")  
    public String getUs_id() {
        return us_id;
    }

    public void setUs_id(String us_id) {
        this.us_id = us_id;
    }
    @Transient
    public String getID() {
        return us_id;
    }
    
    public void setID(String id) {
        this.us_id = id;
    }


    @Column(name = "us_user_id")
    public String getUs_user_id() {
        return us_user_id;
    }

    public void setUs_user_id(String us_user_id) {
        this.us_user_id = us_user_id;
    }

    @Column(name = "us_password")
    public String getUs_password() {
        return us_password;
    }

    public void setUs_password(String us_password) {
        this.us_password = us_password;
    }

    @Column(name = "us_id_type")
    public String getUs_id_type() {
        return us_id_type;
    }

    public void setUs_id_type(String us_id_type) {
        this.us_id_type = us_id_type;
    }

    @Column(name = "us_id_number")
    public String getUs_id_number() {
        return us_id_number;
    }

    public void setUs_id_number(String us_id_number) {
        this.us_id_number = us_id_number;
    }

    @Column(name = "us_user_name")
    public String getUs_user_name() {
        return us_user_name;
    }

    public void setUs_user_name(String us_user_name) {
        this.us_user_name = us_user_name;
    }

    @Column(name = "us_nationality")
    public String getUs_nationality() {
        return us_nationality;
    }

    public void setUs_nationality(String us_nationality) {
        this.us_nationality = us_nationality;
    }

    @Column(name = "us_preferred_contact")
    public String getUs_preferred_contact() {
        return us_preferred_contact;
    }

    public void setUs_preferred_contact(String us_preferred_contact) {
        this.us_preferred_contact = us_preferred_contact;
    }

    @Column(name = "us_email")
    public String getUs_email() {
        return us_email;
    }

    public void setUs_email(String us_email) {
        this.us_email = us_email;
    }

    @Column(name = "us_hp_number")
    public String getUs_hp_number() {
        return us_hp_number;
    }

    public void setUs_hp_number(String us_hp_number) {
        this.us_hp_number = us_hp_number;
    }

    @Column(name = "us_profession")
    public String getUs_profession() {
        return us_profession;
    }

    public void setUs_profession(String us_profession) {
        this.us_profession = us_profession;
    }

    @Column(name = "us_mailing_address1")
    public String getUs_mailing_address1() {
        return us_mailing_address1;
    }

    public void setUs_mailing_address1(String us_mailing_address1) {
        this.us_mailing_address1 = us_mailing_address1;
    }

    @Column(name = "us_mailing_address2")
    public String getUs_mailing_address2() {
        return us_mailing_address2;
    }

    public void setUs_mailing_address2(String us_mailing_address2) {
        this.us_mailing_address2 = us_mailing_address2;
    }

    @Column(name = "us_mailing_address3")
    public String getUs_mailing_address3() {
        return us_mailing_address3;
    }

    public void setUs_mailing_address3(String us_mailing_address3) {
        this.us_mailing_address3 = us_mailing_address3;
    }

    @Column(name = "us_mailing_postcode")
    public String getUs_mailing_postcode() {
        return us_mailing_postcode;
    }

    public void setUs_mailing_postcode(String us_mailing_postcode) {
        this.us_mailing_postcode = us_mailing_postcode;
    }

    @Column(name = "us_mailing_city")
    public String getUs_mailing_city() {
        return us_mailing_city;
    }

    public void setUs_mailing_city(String us_mailing_city) {
        this.us_mailing_city = us_mailing_city;
    }

    @Column(name = "us_mailing_state")
    public String getUs_mailing_state() {
        return us_mailing_state;
    }

    public void setUs_mailing_state(String us_mailing_state) {
        this.us_mailing_state = us_mailing_state;
    }

    @Column(name = "us_status")
    public String getUs_status() {
        return us_status;
    }

    public void setUs_status(String us_status) {
        this.us_status = us_status;
    }

    @Column(name = "us_admin")
    public String getUs_admin() {
        return us_admin;
    }

    public void setUs_admin(String us_admin) {
        this.us_admin = us_admin;
    }

    @Column(name = "us_last_login_date")
    public Timestamp getUs_last_login_date() {
        return us_last_login_date;
    }

    public void setUs_last_login_date(Timestamp us_last_login_date) {
        this.us_last_login_date = us_last_login_date;
    }

    @Column(name = "us_effective_date")
    public Timestamp getUs_effective_date() {
        return us_effective_date;
    }

    public void setUs_effective_date(Timestamp us_effective_date) {
        this.us_effective_date = us_effective_date;
    }

    @Column(name = "us_cancel_date")
    public Timestamp getUs_cancel_date() {
        return us_cancel_date;
    }

    public void setUs_cancel_date(Timestamp us_cancel_date) {
        this.us_cancel_date = us_cancel_date;
    }

    @Column(name = "created_date")
    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Column(name = "updated_date")
    public Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Timestamp updated_date) {
        this.updated_date = updated_date;
    }

    @Column(name = "updated_by")

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    @Column(name = "us_user_type")
    public String getUs_user_type() {
        return us_user_type;
    }

    public void setUs_user_type(String us_user_type) {
        this.us_user_type = us_user_type;
    }

    @Column(name = "us_verify_by")
    public String getUs_verify_by() {
        return us_verify_by;
    }

    public void setUs_verify_by(String us_verify_by) {
        this.us_verify_by = us_verify_by;
    }

    @Column(name = "us_verify_date")
    public Timestamp getUs_verify_date() {
        return us_verify_date;
    }

    public void setUs_verify_date(Timestamp us_verify_date) {
        this.us_verify_date = us_verify_date;
    }

    @Column(name = "us_fail_attempt_count")
    public Integer getUs_fail_attempt_count() {
        return us_fail_attempt_count;
    }

    public void setUs_fail_attempt_count(Integer us_fail_attempt_count) {
        this.us_fail_attempt_count = us_fail_attempt_count;
    }

    @Column(name = "us_fail_login_date")
    public Timestamp getUs_fail_login_date() {
        return us_fail_login_date;
    }

    public void setUs_fail_login_date(Timestamp us_fail_login_date) {
        this.us_fail_login_date = us_fail_login_date;
    }

    @Column(name = "us_division")
    public String getUs_division() {
        return us_division;
    }

    public void setUs_division(String us_division) {
        this.us_division = us_division;
    }

    @Column(name = "us_upgrade")
    public String getUs_upgrade() {
        return us_upgrade;
    }

    public void setUs_upgrade(String us_upgrade) {
        this.us_upgrade = us_upgrade;
    }

    @Column(name = "us_internal")
    public String getUs_internal() {
        return us_internal;
    }

    public void setUs_internal(String us_internal) {
        this.us_internal = us_internal;
    }

    @Column(name = "us_upgrade_date")
    public Timestamp getUs_upgrade_date() {
        return us_upgrade_date;
    }

    public void setUs_upgrade_date(Timestamp us_upgrade_date) {
        this.us_upgrade_date = us_upgrade_date;
    }

    @Column(name = "us_upgrade_by")
    public String getUs_upgrade_by() {
        return us_upgrade_by;
    }

    public void setUs_upgrade_by(String us_upgrade_by) {
        this.us_upgrade_by = us_upgrade_by;
    }

    @Column(name = "us_hp_country_code")
    public String getUs_hp_country_code() {
        return us_hp_country_code;
    }

    public void setUs_hp_country_code(String us_hp_country_code) {
        this.us_hp_country_code = us_hp_country_code;
    }

    @Column(name = "us_expiry_date")
    public Timestamp getUs_expiry_date() {
        return us_expiry_date;
    }

    public void setUs_expiry_date(Timestamp us_expiry_date) {
        this.us_expiry_date = us_expiry_date;
    }

    @Column(name = "us_internal_section")
    public String getUs_internal_section() {
        return us_internal_section;
    }

    public void setUs_internal_section(String us_internal_section) {
        this.us_internal_section = us_internal_section;
    }

    @Column(name = "us_email_date")
    public Timestamp getUs_email_date() {
        return us_email_date;
    }

    public void setUs_email_date(Timestamp us_email_date) {
        this.us_email_date = us_email_date;
    }

    @Column(name = "us_email_by")
    public String getUs_email_by() {
        return us_email_by;
    }

    public void setUs_email_by(String us_email_by) {
        this.us_email_by = us_email_by;
    }

    @Column(name = "us_tc_accept_version")
    public String getUs_tc_accept_version() {
        return us_tc_accept_version;
    }

    public void setUs_tc_accept_version(String us_tc_accept_version) {
        this.us_tc_accept_version = us_tc_accept_version;
    }

    @Column(name = "pubkey")
    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }

    @Column(name = "us_password_expiry")
    public Timestamp getUs_password_expiry() {
        return us_password_expiry;
    }

    public void setUs_password_expiry(Timestamp us_password_expiry) {
        this.us_password_expiry = us_password_expiry;
    }

    @Column(name = "us_internal_storefront")
    public String getUs_internal_storefront() {
        return us_internal_storefront;
    }

    public void setUs_internal_storefront(String us_internal_storefront) {
        this.us_internal_storefront = us_internal_storefront;
    }

    @Column(name = "user_type_el")
    public String getUser_type_el() {
        return user_type_el;
    }

    public void setUser_type_el(String user_type_el) {
        this.user_type_el = user_type_el;
    }

    @Column(name = "fim_id")
    public String getFim_id() {
        return fim_id;
    }

    public void setFim_id(String fim_id) {
        this.fim_id = fim_id;
    }

//    @Column(name = "us_identity_type")
//    public String getUs_identity_type() {
//        return us_identity_type;
//    }
//
//    public void setUs_identity_type(String us_identity_type) {
//        this.us_identity_type = us_identity_type;
//    }

    @Column(name = "fim_user_id")
    public String getFim_user_id() {
        return fim_user_id;
    }

    public void setFim_user_id(String fim_user_id) {
        this.fim_user_id = fim_user_id;
    }

    @Column(name = "ldap_user_id")
    public String getLdap_user_id() {
	return ldap_user_id;
    }

    public void setLdap_user_id(String ldap_user_id) {
	this.ldap_user_id = ldap_user_id;
    }
    
    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }
    
    private List userCompanyList = new ArrayList();
    @OneToMany(targetEntity = UserCompanyModel.class, fetch = FetchType.LAZY, mappedBy = "us_id")
    public List<UserCompanyModel> getUserCompanyList() {
        return userCompanyList;
    }

    public void setUserCompanyList(List<UserCompanyModel> userCompanyList) {
        this.userCompanyList = userCompanyList;
    }

} 
