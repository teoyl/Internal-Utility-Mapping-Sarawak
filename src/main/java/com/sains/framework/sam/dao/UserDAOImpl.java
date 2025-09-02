package com.sains.framework.sam.dao;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.Encriptor;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.LogFunction;
import com.sains.framework.model.GroupUser;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import com.sains.common.util.DateUtil;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.model.User;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.SQLQuery;

public class UserDAOImpl extends BaseDAOImpl<User> implements UserDAO {

    public User getUserByUserId(String userId, User user) {
        return (User) getSession().getNamedQuery("User.byUs_user_id").setParameter("us_user_id", userId).uniqueResult();
//        Map param = new HashMap();
//        param.put("us_user_id", userId);
//        List<User> userList = list(param, User.class, false);
//        if (userList != null && userList.size() > 0) {
//            User foundUser = userList.get(0);
//            userList = null;
//            return foundUser;
//        }
//        return null;
    }

    private String auditLoginStatus = ""; // added @26.09.2013 thensw **copy from elodgement
    public String getAuditLoginStatus() {
        return auditLoginStatus;
    }
    
    public static final String Base64PublicStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIGGxPQ+231uD6jdUt8TN6MD3oGv82teKs4mY48fRyijHd/Wj8SrbwkpQxbjGLdHI9/+SuuXB4agivsZMIVWhu8CAwEAAQ==";
    public static final String Base64PrivateStr = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAgYbE9D7bfW4PqN1S3xM3owPega/za14qziZjjx9HKKMd39aPxKtvCSlDFuMYt0cj3/5K65cHhqCK+xkwhVaG7wIDAQABAkBhJ7u4ESYGEXZBjbHJjdqfthlCYb5OfNXSx8zJ4AY6Hf7TIJ6sP5oKvqaqafsgOs0iATDYivYDq+kydw1c/TDxAiEAu1hubmIlqbWnyGLFF4DPti3ZadliMfHgX+2IslHMq9MCIQCw/h/T6R6IKknX5R5HJqqvnFfLreWPUMUPE0VQx5HS9QIgM5485wsBx422id604THriQ1+7swnYw16vdOLq14gX2MCIEARTR4dNZMek6pV4pbo1UJKOrGc2gr9tsdVN3MdOpj9AiBu7BAmpPuhVVn0HqLBYLLSJrU2blwBBqm9w1gJdJB/xg==";

    public User processLogin(String userId, String password, String systemType_, String loginUsing) throws Exception {
        Debug.printFrameworkDebug("---processlogin");
        Map sessionMap = ActionContext.getContext().getSession(); // remove....
        java.sql.Timestamp timeBefore = DateUtil.getCurrentTimestamp();
        //BaseDAO baseDAO = new BaseDAOImpl();
        User user = null;
//        user = (User) baseDAO.getModelByCode("us_user_id",userId,new User());
        if (loginUsing.equals("IC")) {
            user = (User) getSession().getNamedQuery("User.findByUsIdNumber").setString("us_id_number", userId).uniqueResult();
        } else if (loginUsing.equals("EMAIL")) {
            user = (User) getSession().getNamedQuery("User.findByUsEmail").setString("us_email", userId).uniqueResult();
            if (user.getUs_nationality() != null && user.getUs_nationality().equals(SystemConstants.SETUP_CODE_HARDCODE.COU_Malaysia)) {
                throw new CustomBaseException(new ActionSupport().getText("errors.malaysian.login.email"));
            }
        } else if (loginUsing.equals("USERID")) {
            //if not doing mapping contains "sSysssoUserObjId", then use swkId's pk to find user
            if ((!sessionMap.containsKey("map_sso_uid") && !sessionMap.containsKey("bs_map_sso_uid")) && sessionMap.containsKey("sSysssoUserObjId")) {
                user = (User) getSession().getNamedQuery("User.findBySsoUid").setParameter("sso_uid", sessionMap.get("sSysssoUserObjId")).uniqueResult();
            } else {
                Debug.printFrameworkDebug("userId " + userId);
                user = (User) getSession().getNamedQuery("User.findByUsUserId").setString("us_user_id", userId).uniqueResult();
            }
            
            Debug.printFrameworkDebug("user " + user);
        }
//        user = (User)getSession().getNamedQuery("User.findByUsUserId").setString("us_user_id", userId).uniqueResult();
        Boolean isValid = Boolean.FALSE;
//        try {
        auditLoginStatus = SystemConstants.AUDIT_LOGIN.STATUS.FAIL;  // Default to Failed
        if (user == null) {
            return null;
        }
        if (user.getUs_ldap() == null) {
            return null;
        }
            
//        if (!(sessionMap.containsKey("map_sso_uid")) && !Validator.isEmpty((String) sessionMap.get("sesAccessToken")) && !Validator.isEmpty((String) sessionMap.get("sSysUserObjId")) && sessionMap.get("sSysUserObjId").equals(userId)) {
        if (sessionMap.containsKey("sesAccessToken") && sessionMap.get("sesAccessToken").equals("_fim2Login_")) { //sesAccessToken = _fim2Login_ is set at LoginAction.fim2Verify
            isValid = true;
        } else if ((!(sessionMap.containsKey("map_sso_uid")) && !(sessionMap.containsKey("bs_map_sso_uid")) ) && !Validator.isEmpty((String) sessionMap.get("sesAccessToken"))) {
            // logined thru sarawak id, bypass fw login
            isValid = true;
	} else if ((!(sessionMap.containsKey("map_sso_uid")) && !(sessionMap.containsKey("bs_map_sso_uid")) ) && user.getUs_ldap().equals("S")) {
		throw new CustomBaseException("errors.useSwkID");
        }else if(sessionMap.containsKey("map_api_call")){
            Debug.printFrameworkDebug("*****************************************");
        isValid = true;
//            Debug.printFrameworkDebug("SarawakID LOGIN...");
//
//            try {
//                SwkIdApi swkIdApi = new SwkIdApi();
//                String loginBy = "sarawakid";
//                String strOAuthAccessToken = swkIdApi.login(userId, password, loginBy);
//                if (!Validator.isEmpty(strOAuthAccessToken)) {
//                    sessionMap.put("sesAccessToken", strOAuthAccessToken);
//                    isValid = true;
//                    
//                    // get user info from sarawak id
//                    Map usr_info = swkIdApi.token_user(strOAuthAccessToken);
//                    if (usr_info.containsKey("usr_short_name")) { // sarawak id
//                        sessionMap.put("sSysUserObjId", usr_info.get("usr_short_name"));
//                    }
//                }
//            } catch (CustomBaseException e) {
//                throw e;
//            }
        } else if (user.getUs_ldap().equals("Y")) {
            Debug.printFrameworkDebug("LDAP LOGIN...");
            isValid = checkUserLdap(userId, password);
        } else {
            Debug.printFrameworkDebug("NON-LDAP Login...");
            Debug.printFrameworkDebug("NON-LDAP Login...");
//            Debug.printFrameworkDebug("systemType_ " + systemType_);

            //Disable public user login using internal screen - ahmadni @2-Sept-2016
//                if (password.equals("KInpn7t69Easvx6fPHIHEA==")) {  // << remove this line after testing
//                    isValid = Boolean.TRUE;                          // << remove this line after testing
//                    auditLoginStatus = SystemConstants.AUDIT_LOGIN.STATUS.SUCCESS; // << remove this line after testing
//                } else {                                             // << remove this line after testing
            if (!Validator.isEmpty(user.getUs_password())) { // password not empty
//                byte[] publicKey = Base64.decodeBase64(base64PublicStr);
                byte[] privateKey = Base64.decodeBase64(Base64PrivateStr);
//                String encodeStr = Base64.encodeBase64String(encrypt(publicKey, "password".getBytes()));
//                if (user.getUs_password().equalsIgnoreCase(Encriptor.encode(password))) { // if password match
//Debug.printFrameworkDebug("2. Base64.encodeBase64String(decrypt(privateKey, Base64.decodeBase64(user.getUs_password()))) = " + new String(decrypt(privateKey, Base64.decodeBase64(encodeStr))));
                if (new String(decrypt(privateKey, Base64.decodeBase64(user.getUs_password()))).equalsIgnoreCase(password)) { // if password match
                    Debug.printFrameworkDebug("---password match here ---");
                    if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.ACTIVE)) { // if ACTIVE account
                        Debug.printFrameworkDebug("1");
                    } else if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.INACTIVE)) { // if INACTIVE account
                        Debug.printFrameworkDebug("2");
                        throw new CustomBaseException(new ActionSupport().getText("account.error.inactive"));
                    } else if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.CANCELLED)) { // if CANCELLED account
                        Debug.printFrameworkDebug("3");
                        throw new CustomBaseException(new ActionSupport().getText("account.error.cancelled"));
                    } else if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.LOCKED)) { // if LOCKED account
                        Debug.printFrameworkDebug("4");
                        throw new CustomBaseException(new ActionSupport().getText("account.error.locked"));
                    }

                    isValid = Boolean.TRUE;
                } else { // if password mismatch
                    Debug.printFrameworkDebug("mismatch");
                    if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.ACTIVE)) {
                        checkFailLoginAttempts(user);
                    } else {
                        if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.INACTIVE)) { // if INACTIVE account
                            throw new CustomBaseException(new ActionSupport().getText("account.error.inactive"));
                        } else if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.CANCELLED)) { // if CANCELLED account
                            throw new CustomBaseException(new ActionSupport().getText("account.error.cancelled"));
                        } else if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.LOCKED)) { // if LOCKED account
                            throw new CustomBaseException(new ActionSupport().getText("account.error.locked"));
                        }
                    }
                }
            } else { // password is empty
                Debug.printFrameworkDebug("dataEroror" + new ActionSupport().getText("errors.dataError"));
                throw new CustomBaseException(new ActionSupport().getText("errors.dataError"));
            }
//                } // << remove this line after testing
//                if (user != null && user.getUs_password().equalsIgnoreCase(password)) {
//                    isValid = true;
//                }
        }
//            Debug.printFrameworkDebug("Valid: " + isValid);
        if (isValid) {
            BaseDAO updateUserDAO = new BaseDAOImpl();
            try {
                    //Debug.printFrameworkDebug("--update last login date here");
                SQLQuery query = null;
                if(sessionMap.containsKey("map_sso_uid") || sessionMap.containsKey("bs_map_sso_uid")) {
                    query = updateUserDAO.getSession().createSQLQuery("update t_setup_user set us_last_login_date = :lastLoginDate, us_fail_attempt_count='0', sso_uid = :ssoUid, sso_login_id = :ssoLoginId, sso_enabled_date = :enabledDate where us_id = :usId");
                    java.sql.Timestamp ts = DateUtil.getCurrentTimestamp();
                    query.setParameter("lastLoginDate", ts);
                    query.setParameter("usId", user.getID());
                    //save the SSO's information
                    query.setParameter("ssoUid", sessionMap.get("sSysssoUserObjId"));
                    query.setParameter("ssoLoginId", sessionMap.get("sSysUserObjId"));
                    query.setParameter("enabledDate", ts);
                } else {
                    query = updateUserDAO.getSession().createSQLQuery("update t_setup_user set us_last_login_date = :lastLoginDate, us_fail_attempt_count='0' where us_id = :usId");
                    query.setParameter("lastLoginDate", DateUtil.getCurrentTimestamp());
                    query.setParameter("usId", user.getID());
                }

                updateUserDAO.beginBatchTransaction();
                query.executeUpdate();
                updateUserDAO.commitBatchTransaction();
            } catch (Exception e) {
                updateUserDAO.rollbackBatchTransaction();
            } finally {
                updateUserDAO.closeSession();
            }
            auditLoginStatus = SystemConstants.AUDIT_LOGIN.STATUS.SUCCESS; // S-Success
            return user;
        }
//        } catch (CustomBaseException cbe){
//            rollbackBatchTransaction();
//            new LogFunction().logError(this.getClass(), "", cbe);
//            throw cbe;
//        } catch (Exception e){
//            rollbackBatchTransaction();
//            new LogFunction().logError(this.getClass(), "", e);
//        }
        return null;
    }

    // Added by TTH @ 28-Sept-2010, Copy from eKPI ldap.jsp
    //public boolean checkUserLdap(HttpServletRequest request, ServletContext application, String userid, String password) throws java.io.IOException {
    private boolean checkUserLdap(String userid, String password) throws java.io.IOException {
        //kpi_servlet.Logger logger = new kpi_servlet.Logger(request);

//        Debug.printFrameworkDebug("in checkUserLdap...");

//        String url = "ldap://ldap.sarawaknet.gov.my:389";
//        new ldap
//        String url = "ldap://ldap.sarawak.gov.my:389"; //Live
        String url = "172.26.80.14:389"; //TNT @ DC
        Hashtable environment = new Hashtable();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, url);

        if (password.equals("")) {
            Debug.printDebug("Ldap userid: " + userid + "  \t exc: no userid or password apply\n\n");
            //logger.setPath(2);
            //logger.WriteLogFile("Ldap userid: "+userid+"  \t exc: no userid or password apply\n\n");
            return false;
        } else {
            try {
                DirContext ctx = new InitialDirContext(environment);
                if (ctx == null) {
                    Debug.printDebug("Ldap userid: " + userid + "  \t exc: DirContext ctx == null\n\n");
                    //logger.setPath(2);
                    //logger.WriteLogFile("Ldap userid: "+userid+"  \t exc: DirContext ctx == null\n\n");
                    return false;
                }

                SearchControls constraints = new SearchControls();
                constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
                //search sarawaknet subtree for uid
                NamingEnumeration results = ctx.search("o=SarawakNet", "uid=" + userid, constraints);

                if ((results != null) && results.hasMore()) {	//can be found
                    SearchResult si = (SearchResult) results.next();	//retrieve results
                    environment.put(Context.SECURITY_AUTHENTICATION, "simple");
                    environment.put(Context.SECURITY_PRINCIPAL, si.getName() + ",o=SarawakNet");
                    environment.put(Context.SECURITY_CREDENTIALS, password);

                    DirContext dirctx = new InitialDirContext(environment);
                    if (dirctx == null) {
                        Debug.printDebug("Ldap userid: " + userid + "  \t exc: DirContext dirctx == null\n\n");
                        //logger.setPath(1);
                        //logger.WriteLogFile("Ldap userid: "+userid+"  \t exc: DirContext dirctx == null\n\n");
                        return false;
                    }
//        String[] attrOLDIC = {"oldicnumber"};
//	  		String[] attrNEWIC = {"newicnumber"};
//        String[] attrSN = {"sn"};
//        String[] attrMAIL = {"mail"};
//
//       	//Get the attributes requested
// 	    	Attributes oldicn = dirctx.getAttributes(si.getName()+",o=SarawakNet", attrOLDIC);
// 	    	Attributes newicn = dirctx.getAttributes(si.getName()+",o=SarawakNet", attrNEWIC);
//        Attributes name = dirctx.getAttributes(si.getName()+", o=SarawakNet", attrSN);
//        Attributes email = dirctx.getAttributes(si.getName()+", o=SarawakNet", attrMAIL);
//
//        oldicnumber = (String)oldicn.get("oldicnumber").get();
//        newicnumber = (String)newicn.get("newicnumber").get();
//        sn = (String)name.get("sn").get();
//        mail = (String)email.get("mail").get();
//
//        request.getSession(true).setAttribute("strOldIc", oldicnumber);
//        request.getSession(true).setAttribute("strNewIc", newicnumber);
////        request.getSession(true).setAttribute("sn", sn);
////        request.getSession(true).setAttribute("mail", mail);
////        request.getSession(true).setAttribute("strLoginid", userid);
//
//    		if (oldicnumber.equals("null") && newicnumber.equals("null")) {
//        Debug.printFrameworkDebug("Ldap userid: "+userid+"  \t exc: password-protected attribute not found, consider as invalid credentials\n\n");
//        //logger.setPath(2);
//        //logger.WriteLogFile("Ldap userid: "+userid+"  \t exc: password-protected attribute not found, consider as invalid credentials\n\n");
//        return false;	//if password-protected attribute not found, consider as invalid credentials
//        }
                } else {
                    Debug.printDebug("Ldap userid: " + userid + "  \t exc: username not found\n\n");
                    //logger.setPath(2);
                    //logger.WriteLogFile("Ldap userid: "+userid+"  \t exc: username not found\n\n");
                    return false;		//username not found
                }
            } catch (Exception e) {
                if (e.toString().indexOf("Invalid Credentials") >= 0) {
                    //Debug.printFrameworkDebug("exe: Invalid Credentials");
                    Debug.printDebug("Ldap userid: " + userid + "  \t Error: Invalid Credentials\n\n");
                    //logger.setPath(2);
                    //logger.WriteLogFile("Ldap userid: "+userid+"  \t Error: Invalid Credentials\n\n");
                    return false;
                } else if (e.toString().indexOf("No Such Object") >= 0) {
                    //Debug.printFrameworkDebug("exe: No Such Object");
                    Debug.printDebug("Ldap userid: " + userid + "  \t exc:" + e.toString());
                    //Debug.printFrameworkDebug("Ldap userid: "+userid+"  \t exc: No Such Object\n\n");
                    //logger.setPath(2);
                    //logger.WriteLogFile("Ldap userid: "+userid+"  \t exc: No Such Object\n\n");
                    return false;
                } else {	//unknown exception
                    //Debug.printFrameworkDebug("exe: " + e);
                    //System.err.println(e);
                    Debug.printDebug("Ldap userid: " + userid + "  \t exc: " + e + "(unknown exception)\n\n");
                    //logger.setPath(2);
                    //logger.WriteLogFile("Ldap userid: "+userid+"  \t exc: " + e + "(unknown exception)\n\n");
                    return false;
                }
            }
            //logger.setPath(2);
            //logger.WriteLogFile("Ldap userid: "+userid+"  \t Ldap login success\n\n");
            return true;
        } //end if password = null
    }


    public synchronized void groupDeleteUser(String[] ids, User user) throws Exception {
        beginBatchTransaction();
        try {
            String strUpdatedBy = (String) ActionContext.getContext().getSession().get("loginId");  // For Audit

            for (String deleteId : ids) {
                user = (User) getSession().get(user.getClass(), deleteId);

                // For Audit
                user.setUpdated_by(strUpdatedBy);

                getSession().update(user);
                getSession().flush();
                // For Audit - End

                if (user.getGroupUserList() != null){
                    for (GroupUser groupUser : user.getGroupUserList()){
                        getSession().delete(groupUser);
                    }
                }
//                if (user.getWfGroupUserList() != null){
//                    for (WorkflowGroupUserModel wfGroupUser : user.getWfGroupUserList()){
//                        getSession().delete(wfGroupUser);
//                    }
//                }
                getSession().delete(user);
            }
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }

    //Added by Delvene @ 12-Jul-2013 :: To group inser new user and add into user group under 1 transaction
    @Override
    public synchronized void insertActivateUser(User user) throws Exception {
        BaseDAO baseDAO = new BaseDAOImpl();
        baseDAO.setSession(getSession());

        try {
            beginBatchTransaction();
            user.defaultAddProperties();
            getSession().save(user);

            //set newly created user into ESS user group
            SetupGroup userGroup = (SetupGroup)baseDAO.getModelByCode("group_code", "SelfService", new SetupGroup());
            GroupUser gu = new GroupUser();
            gu.setUs_id(user.getUs_id());
            gu.setUg_id(userGroup.getID());
            gu.defaultAddProperties();
            getSession().save(gu);

            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }
    
    public synchronized void checkFailLoginAttempts(User user) throws Exception {
        BaseDAO baseDAO = new BaseDAOImpl();
        baseDAO.setSession(getSession());

        User updateUser = (User) getSession().getNamedQuery("User.findByUsUserId").setString("us_user_id", user.getUs_user_id()).uniqueResult();

        int failCount = 0; // disable account after 3 failed attempts
        if (updateUser.getUs_fail_attempt_count() != null) {
            failCount = updateUser.getUs_fail_attempt_count();
        }

        if (failCount >= 3) {
            throw new CustomBaseException(new ActionSupport().getText("account.error.locked"));
        } else {
            failCount++;
            user.setUs_fail_login_date(DateUtil.getCurrentTimestamp()); // set fail login date
            if (failCount == 3) {
//                        String strCurrentDateTime = DateUtil.getCurrentTimestamp().toString();
                String strActivationCode = new CommonFunction().encData(updateUser.getID()+user.getUs_fail_login_date());
                updateUser.setUs_activation_code(strActivationCode);
                updateUser.setUs_status(SystemConstants.USER_ACC_STATUS.LOCKED); // set user to LOCKED if failCount=3
            }

            updateUser.setUs_fail_attempt_count(failCount); // set fail attempt count
//                user.updatableColumns = new String[]{"Us_id", "Us_status", "Us_fail_attempt_count"};
            beginBatchTransaction();
            getSession().update(updateUser);
            commitBatchTransaction();

            if (failCount == 3) {
                throw new CustomBaseException(new ActionSupport().getText("account.error.locked"));
            } else {
//                    throw new CustomBaseException(new ActionSupport().getText("errors.invalidLogin"));
            }
        }

    }
    
    public synchronized void activateUser(String userId, String password, String strOperation) throws Exception {
        BaseDAO baseDAO = new BaseDAOImpl();
        baseDAO.setSession(getSession());
        User pum = new User();
        User user = null;
        //ahmadni @ 13-Mar-2017 - replace ic number to retrieve from db
        String newUserId = userId.replace("-", "");
//        Debug.printFrameworkDebug("newUserId " +newUserId);
        user = (User) getSession().getNamedQuery("User.findByUsUserId").setParameter("us_user_id", newUserId).uniqueResult();
        user.set_operation(strOperation);
        Boolean isValid = Boolean.FALSE;

        try {
            beginBatchTransaction();
            user.defaultUpdateProperties();

            if (user != null) {
                if (user.getUs_ldap() != null) {
                    if (user.getUs_ldap().equals("Y")) {
                        Debug.printDebug("LDAP User cannot change password here");
                    } else {
//                        Debug.printFrameworkDebug("non ldap");
                        if (!Validator.isEmpty(user.get_operation())) {
                            if (user.get_operation().equals(SystemConstants.COMM_OPERATION.RESET_PASSWORD)) {
                                if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.ACTIVE)) {
                                    if (!Validator.isEmpty(password)) { // password not empty
                                        byte[] publicKey = Base64.decodeBase64(Base64PublicStr);
                                        user.setUs_password(Base64.encodeBase64String(encrypt(publicKey, password.getBytes())));
                                        user.setUs_pass_last_change_date(DateUtil.getCurrentTimestamp());
                                    } else { // password is empty
                                        throw new CustomBaseException(new ActionSupport().getText("errors.dataError"));
                                    }
                                }
//                                pum.logActivity(new ActionSupport().getText("app.logActivity.change.password", new String[]{ActionContext.getContext().getSession().get("userName").toString()}), baseDAO.getSession(), SystemConstants.SYSTEM_ID.EQP, Boolean.FALSE);
                            } else if (user.get_operation().equals(SystemConstants.COMM_OPERATION.ACTIVATE_ACCOUNT)) {
                                if (user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.INACTIVE) || user.getUs_status().equals(SystemConstants.USER_ACC_STATUS.LOCKED)) {

                                    if (!Validator.isEmpty(password)) { // password not empty
                                        byte[] publicKey = Base64.decodeBase64(Base64PublicStr);
                                        user.setUs_password(Base64.encodeBase64String(encrypt(publicKey, password.getBytes())));
                                        user.setUs_status(SystemConstants.USER_ACC_STATUS.ACTIVE);
                                        user.setUs_fail_attempt_count(0);
                                        user.setUs_pass_last_change_date(DateUtil.getCurrentTimestamp());
                                    } else { // password is empty
                                        throw new CustomBaseException(new ActionSupport().getText("errors.dataError"));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            user = setUpdateProperties(user, getSession());
            getSession().update(user);
            commitBatchTransaction();

        } catch (Exception e) {
            rollbackBatchTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }
    
    public synchronized void updateActivationCode(User user) throws Exception {
        try {
            beginBatchTransaction();
            user.defaultUpdateProperties();

            user = setUpdateProperties(user, getSession());
            getSession().update(user);
            commitBatchTransaction();

        } catch (Exception e) {
            rollbackBatchTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }
    
    private static final String ALGORITHM = "RSA";
    public static byte[] decrypt(byte[] privateKey, byte[] inputData)
            throws Exception {

        PrivateKey key = KeyFactory.getInstance(ALGORITHM)
                .generatePrivate(new PKCS8EncodedKeySpec(privateKey));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decryptedBytes = cipher.doFinal(inputData);

        return decryptedBytes;
    }
    
    public static byte[] encrypt(byte[] publicKey, byte[] inputData)
            throws Exception {

        PublicKey key = KeyFactory.getInstance(ALGORITHM)
                .generatePublic(new X509EncodedKeySpec(publicKey));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(inputData);

        return encryptedBytes;
    }
}
