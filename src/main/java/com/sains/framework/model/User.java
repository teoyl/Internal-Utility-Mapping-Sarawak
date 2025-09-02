package com.sains.framework.model;

import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Encriptor;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.EmailQueueTrigger;
import com.sains.framework.base.ModelBase;
import com.sains.framework.sam.dao.UserDAOImpl;
import static com.sains.framework.sam.dao.UserDAOImpl.encrypt;
import java.sql.Timestamp;
import java.util.HashMap;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.Map;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import static com.sains.framework.sam.dao.UserDAOImpl.Base64PublicStr;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

@Entity
//@FilterDef(name="postSandangActive_user")
@Table(name = "t_setup_user")
@NamedQueries({
    @NamedQuery(name = "User.findByUsUserId",query = "from User where us_user_id = :us_user_id"),
    @NamedQuery(name = "User.findBySsoUid",query = "from User where sso_uid = :sso_uid"),
    @NamedQuery(name = "User.findByIdAndEmail",query = "from User where us_user_id = :us_user_id and us_email = :us_email"),
    @NamedQuery(name = "User.findByEmail",query = "from User where us_email = :us_email"),
    @NamedQuery(name = "User.findByActivationCode",query = "from User where us_activation_code = :activationCode")
})
public class User extends ModelBase implements java.io.Serializable {

    private static final long serialVersionUID = -66111L;
    private String us_id = ""; //User's PK using varchar(20)
//    private Integer us_id = null; //User's PK using Auto-Increment
    private String us_user_id = "";
    private String us_user_name = "";
    private String us_password = "";
    private String us_email = "";
    private String us_status = "N";// Y - Active, N - Not active , C - Cancelled, L - Locked
    private String us_admin = "N";
    private String us_user_type = "I";
    private String us_division = "";
    private String us_nationality;
    private String us_id_number;
    private Integer us_fail_attempt_count = 0;
    private String us_ldap = "Y";
    private String resetPassCode = null;
    private java.sql.Timestamp us_last_login_date;
    private java.sql.Timestamp us_fail_login_date;
    private java.sql.Timestamp us_pass_last_change_date;
    private String us_activation_code;
    private String sso_uid;
    private String sso_login_id;
    private String profile_attachment_id;
    private java.sql.Timestamp sso_enabled_date;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    
    public String[] updatableColumns = new String[]{"Us_id", "Us_user_id", "Us_user_name", "Us_email",
        "Us_status", "Us_admin", "Us_division", "Us_nationality", "Us_id_number", "Us_fail_attempt_count", "Us_last_login_date", "Us_fail_login_date", "Us_pass_last_change_date", "Us_ldap", "Updated_by", "Updated_date", "Us_ldap"};
    public final String[] columnLength = new String[]{"us_id:20", "us_user_id:20", "us_user_name:100", "us_email:30",
        "Us_status:1", "Us_admin:1", "Us_division:20", "Us_ldap:1"};
    private Map columnLengthMap = null;
    public User() {
        userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("us_id,us_user_id;lbl.username,us_id,us_email;lbl.email.address");
//        userDefined_insertNoDuplicateRange("us_id,us_user_id;us_id,us_email");

        //wongkk4@23Mac2015 - temporary disabled for UAT testing 
        userDefined_insertNoDuplicateRange("us_user_id;us_email");
        userDefined_duplicateFieldsRangeKeyDescription("user.id;user.emailAddress");

        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }

    /** User's PK using varchar(20) : START **/
    @Id
    @Column(name = "us_id")
    public String getUs_id() {
        return us_id;
    }

    public void setUs_id(String usId) {
        us_id = usId;
    }

    @Transient
    public String getID() {
        return getUs_id();
    }

    public void setID(String usId) {
        setUs_id(usId);
    }
    /** User's PK using varchar(20) : END **/
    
    /** User's PK using Auto-Increment : START **/
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "us_id")
//    public Integer getUs_id() {
//        return us_id;
//    }
//
//    public void setUs_id(Integer usId) {
//        us_id = usId;
//    }
//
//    @Transient
//    public String getID() {
//        if (us_id == null) {
//            return null;
//        }
//        return us_id.toString(); 
//    }
//
//    public void setID(String usId) {
//        if (Validator.isEmpty(usId)) {
//            setUs_id(null);
//        } else {
//            try {
//                this.us_id = Integer.parseInt(usId);
//            } catch (Exception e) {
//                setUs_id(null);
//            }
//        }
//    }
    /** User's PK using Auto-Increment : END **/

    @Column(name = "us_user_id")
    public String getUs_user_id() {
        return us_user_id;
    }

    public void setUs_user_id(String usUserId) {
        us_user_id = usUserId;
    }

    @Column(name = "us_user_name")
    public String getUs_user_name() {
        return us_user_name;
    }

    public void setUs_user_name(String usUserName) {
        us_user_name = usUserName;
    }

    @Column(name = "us_password")
    public String getUs_password() {
        return us_password;
    }

    public void setUs_password(String usPassword) {
        us_password = usPassword;
    }

    @Column(name = "us_email")
    public String getUs_email() {
        return us_email;
    }

    public void setUs_email(String usEmail) {
        us_email = usEmail;
    }

    @Column(name = "us_status")
    public String getUs_status() {
        return us_status;
    }

    public void setUs_status(String usStatus) {
        us_status = usStatus;
    }

    @Column(name = "us_admin")
    public String getUs_admin() {
        return us_admin;
    }

    public void setUs_admin(String usAdmin) {
        us_admin = usAdmin;
    }

    @Column(name = "us_user_type")
    public String getUs_user_type() {
        return us_user_type;
    }

    public void setUs_user_type(String us_user_type) {
        this.us_user_type = us_user_type;
    }

    @Column(name = "us_division")
    public String getUs_division() {
        return us_division;
    }

    public void setUs_division(String us_division) {
        this.us_division = us_division;
    }

//    @Column(name="us_nationality")
    @Transient
    public String getUs_nationality() {
    	return us_nationality;
    }

    public void setUs_nationality(String us_nationality) {
    	this.us_nationality = us_nationality;
    }
    
//    @Column(name="us_id_number")
    @Transient
    public String getUs_id_number() {
        return us_id_number;
    }

    public void setUs_id_number(String us_id_number) {
        this.us_id_number = us_id_number;
    }
    
//    @Column(name = "us_fail_attempt_count")
    @Transient
    public Integer getUs_fail_attempt_count() {
        return us_fail_attempt_count;
    }

    public void setUs_fail_attempt_count(Integer us_fail_attempt_count) {
        this.us_fail_attempt_count = us_fail_attempt_count;
    }
    
    @Column(name = "us_ldap")
    public String getUs_ldap() {
        return us_ldap;
    }

    public void setUs_ldap(String us_ldap) {
        this.us_ldap = us_ldap;
    }

    @Column(name = "resetPassCode")
    public String getResetPassCode() {
        return resetPassCode;
    }

    public void setResetPassCode(String resetPassCode) {
        this.resetPassCode = resetPassCode;
    }

//    @Column(name = "us_last_login_date")
    @Transient
    public java.sql.Timestamp getUs_last_login_date() {
        return us_last_login_date;
    }

    public void setUs_last_login_date(java.sql.Timestamp usLastLoginDate) {
        us_last_login_date = usLastLoginDate;
    }
//    
//    @Column(name = "us_fail_login_date")
    @Transient
    public java.sql.Timestamp getUs_fail_login_date() {
        return us_fail_login_date;
    }

    public void setUs_fail_login_date(java.sql.Timestamp usLastLoginDate) {
        us_fail_login_date = usLastLoginDate;
    }
//    
//    @Column(name = "us_pass_last_change_date")
    @Transient
    public Timestamp getUs_pass_last_change_date() {
        return us_pass_last_change_date;
    }

    public void setUs_pass_last_change_date(Timestamp us_pass_last_change_date) {
        this.us_pass_last_change_date = us_pass_last_change_date;
    }
    
    @Column(name = "us_activation_code")
    public String getUs_activation_code() {
        return us_activation_code;
    }

    public void setUs_activation_code(String us_activation_code) {
        this.us_activation_code = us_activation_code;
    }

    @Column(name = "sso_uid")
    public String getSso_uid() {
        return sso_uid;
    }

    public void setSso_uid(String sso_uid) {
        this.sso_uid = sso_uid;
    }

    @Column(name = "sso_login_id")
    public String getSso_login_id() {
        return sso_login_id;
    }

    public void setSso_login_id(String sso_login_id) {
        this.sso_login_id = sso_login_id;
    }

    @Column(name = "sso_enabled_date")
    public Timestamp getSso_enabled_date() {
        return sso_enabled_date;
    }

    public void setSso_enabled_date(Timestamp sso_enabled_date) {
        this.sso_enabled_date = sso_enabled_date;
    }
    
    @Transient
    public String getSso_enabled_date_str() {
        return Formatter.formatDate(sso_enabled_date, SystemConstants.DATE.dataEntryFormat);
    }

    public void setSso_enabled_date_str(String sso_enabled_date) {
        try {
            this.sso_enabled_date = DateUtil.getTimestampFromDate(DateUtil.getDate(sso_enabled_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e) {
        }
    }
    
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
    public java.util.Date getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(java.sql.Timestamp updatedDate) {
        updated_date = updatedDate;
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

    private List groupUserList;
    @OneToMany(targetEntity = GroupUser.class, fetch = FetchType.LAZY, mappedBy = "us_id")
    public List<GroupUser> getGroupUserList() {
        return groupUserList;
    }

    public void setGroupUserList(List groupUserList) {
        this.groupUserList = groupUserList;
    }

    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }

    @Override
    public void preInsert(org.hibernate.Session session) throws Exception {
        Map sessionMap = ActionContext.getContext().getSession();

        try {
            BaseDAOImpl dao = new BaseDAOImpl();
            dao.setSession(session);
            if(get_operation().equalsIgnoreCase("SWKID")){
                setUs_status("Y");
                setUs_admin("N");
                setUs_ldap("S");
                setUs_user_type("P"); //Public
                setCreated_by(getUs_user_id());
            } else if (get_operation().equalsIgnoreCase("PUBLIC")){
                setUs_user_type("P"); //Public
                setUs_admin("N");
                setUs_ldap("N");
                setUs_status("P"); //P - Pending Approval
                setCreated_by(getUs_user_id());
            } else {
                setUs_admin("N");
//                setUs_password("password"); // to be removed
                setUs_ldap("N"); // to be set to 'Y'
//                setUs_status("Y");
            }
            // encript the password here.
            if (!Validator.isEmpty(getUs_password())) {
                byte[] publicKey = Base64.decodeBase64(Base64PublicStr);
                setUs_password(Base64.encodeBase64String(encrypt(publicKey, getUs_password().getBytes())));
            }
             setUs_user_id(getUs_user_id().toLowerCase());
             session.save(this);
            //return super.insert(getModel());
        } catch (Exception e) {
            throw e;
            //return SystemConstants.ACTION_Status.INSERT_FAIL;
        } finally {
            //closeSession();
        }
    }


    @Override
    public void postInsert(org.hibernate.Session session) throws Exception {
        try {
            Debug.printInfo("post Update User");
            BaseDAOImpl dao = new BaseDAOImpl();
            dao.setSession(session);
            //insert new created user into public group
            if(get_operation().equalsIgnoreCase("PUBLIC")){
                SetupGroup group = null;
                group = (SetupGroup)dao.getObjectByCode("group_code", "publicUser", new SetupGroup());
                if(group == null){
                    throw new CustomBaseException("error.user.groupNotFound");
                }
                GroupUser gu = new GroupUser();
                dao.setSession(session);
                gu.setUs_id(getUs_id());
                gu.setUg_id(group.getUg_id());
                gu.defaultAddProperties();
                dao.getSession().save(gu);

            }
        } catch (Exception e) {
            Debug.printError("postInsert error " + e.getMessage());
            throw e;
            //return SystemConstants.ACTION_Status.INSERT_FAIL;
        } finally {
            //closeSession();
        }


    }

    @Override
    public void preUpdate(org.hibernate.Session session, Object dataEntryModel) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(session);
//        System.out.println("Pre Update");
//        System.out.println(((User)dataEntryModel).getCo_id());
//        System.out.println("user pre update get_operation()" + ((User)dataEntryModel).get_operation());
    }
    
    private List userPreferenceList = new ArrayList();
    @OneToMany(targetEntity = UserPreferenceModel.class, fetch = FetchType.LAZY, mappedBy = "us_id")
    public List<UserPreferenceModel> getUserPreferenceList() {
        return userPreferenceList;
    }
    public void setUserPreferenceList(List userPreferenceList) {
        this.userPreferenceList = userPreferenceList;
    }

    @Override
    public void manualOperation(Session session) throws Exception {
        System.out.println("in user.manualOperation");
        if (get_operation().equals("generateActivationCode")) {
            System.out.println("operation = generateActivationCode");
            BaseDAOImpl dao = new BaseDAOImpl();
            dao.setSession(session);
            try {
                String resetCode = getID() +"_"+Formatter.formatDate(DateUtil.getCurrentTimestamp(), Formatter.DATE_PATTERN + " " + Formatter.TIME_PATTERN);
                resetCode = Base64.encodeBase64String(UserDAOImpl.encrypt(Base64.decodeBase64(UserDAOImpl.Base64PublicStr), resetCode.getBytes()));
                session.evict(this);//remove this User object from updating session.
                session.createNativeQuery("update t_setup_user set us_activation_code = :activationCode where us_id = :us_id ")
                        .setParameter("activationCode", resetCode)
                        .setParameter("us_id", getID())
                        .executeUpdate();
                EmailQueueTrigger eqTrigger = new EmailQueueTrigger();
                Map mailParam = new HashMap();
                mailParam.put(EmailQueueTrigger.MAIL_TO, getUs_user_name() + "<"+getUs_email()+">");
                mailParam.put("userId", getUs_user_id());
                mailParam.put("userName", getUs_user_name());
                mailParam.put("activationCode", URLEncoder.encode(resetCode, StandardCharsets.UTF_8.toString()));
                NotificationSetup notification = (NotificationSetup)session.getNamedQuery("Notification.findByNoType")
                        .setParameter("noType", "Reset Password").uniqueResult();
                eqTrigger.sendEMail(mailParam, notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Column(name = "profile_attachment_id")
    public String getProfile_attachment_id() {
        return profile_attachment_id;
    }
    public void setProfile_attachment_id(String profile_attachment_id) {
        this.profile_attachment_id = profile_attachment_id;
    }
    
    DrDocRepoModel drDocRepoModel = null;
    @OneToOne(targetEntity=DrDocRepoModel.class, optional=true, fetch=FetchType.LAZY)
    @JoinColumn(referencedColumnName="dr_doc_id", name="profile_attachment_id", insertable=false, updatable=false, nullable = true)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    public DrDocRepoModel getDrDocRepoModel() {
            return drDocRepoModel;
    }
    public void setDrDocRepoModel(DrDocRepoModel drDocRepoModel) {
            this.drDocRepoModel = drDocRepoModel;
    }
}
