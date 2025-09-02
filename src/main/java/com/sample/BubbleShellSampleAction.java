package com.sample;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.common.util.SwkIdApi;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.web.IBubbleShell;
import com.sains.framework.model.User;
import com.sains.framework.model.UserPubPriModel;
import com.sains.framework.sam.dao.UserDAOImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.struts2.ServletActionContext;
import org.hibernate.SQLQuery;
import org.hibernate.query.NativeQuery;

public class BubbleShellSampleAction extends BaseActionSupport implements IBubbleShell{// implements ModelDriven<String> {
    private Boolean SWK_ID_LOGIN = Boolean.FALSE;
    private final Boolean SWK_ID_CREATE_AS_NEW_USER = Boolean.TRUE;
    public BubbleShellSampleAction() {
        // change the DAO to the correct service.
        //baseDAO = new BaseDAOImpl();
        model = new String();
        setInsertUpdateSuccessCode(SystemConstants.ACTION_Status.LOAD_EDIT_PAGE);
        // set the required field for common check.
        //getRequiredParam().put("column_name1", "column.name1");
        //getRequiredParam().put("column_name2", "column.name2");


    }

//    @Override // change the "String" and return value to the
//    public String getModel() {
//        return model;
//    }

    public void specificValidation(String validationType) {
    }

    public String processInsert() {
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    public String processUpdate() {
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String delete() {
        return SUCCESS;
    }

    //** BubbleShell : START **//
    public String getServerUrl() {
        return "https://droute.sains.com.my/bubbleShell";
//        return "http://10.17.101.219:8080/forNewProject/";
//        return "http://192.168.0.131:8080/forNewProject/";
    }
    public String redirect() {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        return "redirect";
    }
    private Boolean isActivateFingerPrint = Boolean.FALSE;
    public Boolean getIsActivateFingerPrint() {
        return isActivateFingerPrint;
    }
    
    private String fplPublicKey = null; //fpl = finger print login
    public String getFplPublicKey() {
        return fplPublicKey;
    }
    
    public String bsDeviceId = null;
    public String getBsDeviceId() {
        return bsDeviceId;
    }
    public void setBsDeviceId(String bsDeviceId) {
        this.bsDeviceId = bsDeviceId;
    }
    
    public String activateFingerPrint() {
        Map currentSession = ActionContext.getContext().getSession();
        BaseDAO dao = baseDAO;
        try {
            dao.beginBatchTransaction();
            UserPubPriModel dbUserPubPri = null;
            if (bsDeviceId == null) {
                dbUserPubPri = (UserPubPriModel)dao.getSession()
                        .getNamedQuery("UserKey.findByUsId")
                        .setParameter("us_id", currentSession.get("us_id"))
                        .uniqueResult();
            } else {
                dbUserPubPri = (UserPubPriModel)dao.getSession()
                        .getNamedQuery("UserKey.findByUsId_deviceId")
                        .setParameter("us_id", currentSession.get("us_id"))
                        .setParameter("device_id", bsDeviceId)
                        .uniqueResult();
            }
            if (dbUserPubPri != null) {
                KeyPair generateKeyPair = CommonFunction.generateKeyPair(1024);
                byte[] publicKey = generateKeyPair.getPublic().getEncoded();
                fplPublicKey = Base64.encodeBase64String(publicKey);
                byte[] privateKey = generateKeyPair.getPrivate().getEncoded();
                String base64Private = Base64.encodeBase64String(privateKey);
                dbUserPubPri.setPub_key(fplPublicKey);
                dbUserPubPri.setPri_key(base64Private);
                dao.getSession().update(dbUserPubPri);
                dao.commitBatchTransaction();
            } else {
                UserPubPriModel pubPrivateKey = new UserPubPriModel();
                pubPrivateKey.defaultAddProperties();
                pubPrivateKey.setUs_id((String)currentSession.get("us_id"));
                pubPrivateKey.setID(CommonFunction.getId(20));
                KeyPair generateKeyPair = CommonFunction.generateKeyPair(1024);
                byte[] publicKey = generateKeyPair.getPublic().getEncoded();
                fplPublicKey = Base64.encodeBase64String(publicKey);
                byte[] privateKey = generateKeyPair.getPrivate().getEncoded();
                String base64Private = Base64.encodeBase64String(privateKey);
                pubPrivateKey.setPub_key(fplPublicKey);
                pubPrivateKey.setPri_key(base64Private);
                bsDeviceId = CommonFunction.getId(30);
                pubPrivateKey.setDevice_id(bsDeviceId);
                dao.getSession().save(pubPrivateKey);
                dao.commitBatchTransaction();
            }
            isActivateFingerPrint = Boolean.TRUE;
        } catch (Exception e) {
            addActionError(getText("bubbleShell.failToActivateFingerPrint"));
            dao.rollbackBatchTransaction();
        }

        return SUCCESS;
    }
    public String logout() {
        logoutBs();
        return "goLoginPage";
    }
    public String init() {
        return "init";
    }
    
    private String bsToken = null;
    public String getBsToken() {
        return bsToken;
    }
    public void setBsToken(String bsToken) {
        this.bsToken = bsToken;
    }
    
    private String testId = null;
    public String getTestId() {
        return testId;
    }
    public void setTestId(String testId) {
        this.testId = testId;
    }
    
    public String landing() {
        if (isLogined()) {
            return SUCCESS;
        }
        return "goLoginPage";
    }
    public String openPage1() {
        Debug.printDebug("testId = " + testId);
        Debug.printDebug("bsToken = " + bsToken);
        return "page1";
    }
    private String bsFPL_device_id = null;
    public void setBsFPL_device_id(String bsFPL_device_id) {
        this.bsFPL_device_id = bsFPL_device_id;
    }
    
    private String bsFPL_secret = null;
    public void setBsFPL_secret(String bsFPL_secret) {
        this.bsFPL_secret = bsFPL_secret;
    }
    
    UppyParentModel yourParentModel;
    public UppyParentModel getYourParentModel() {
        return yourParentModel;
    }
    public void setYourParentModel(UppyParentModel yourParentModel) {
        this.yourParentModel = yourParentModel;
    }
    public String doLogin() {
        if (!isLogined()) {
            return validateLogin();
        }
        yourParentModel = (UppyParentModel) baseDAO.getSession().getNamedQuery("UppyParentModel.findBy_createdBy")
                .setParameter("createdBy", ActionContext.getContext().getSession().get("loginId"))
                .uniqueResult();
        if (yourParentModel == null) {
            yourParentModel = new UppyParentModel();
            yourParentModel.setChildList(baseDAO.getSession().getNamedQuery("DocRepo.findBy_drDocApp_createdUser")
                .setParameter("createdBy", ActionContext.getContext().getSession().get("loginId"))
                .setParameter("drDocApp", "uppySample3")
                .list());
            System.out.println("ActionContext.getContext().getSession().get(\"loginId\") = " + ActionContext.getContext().getSession().get("loginId"));
            System.out.println("yourParentModel child.size = " + yourParentModel.getChildList().size());
//            UppyModel uppy1 = new UppyModel();
//            uppy1.setUppy_file_desc("first file");
//            uppy1.setDr_doc_id("abc123");
//            yourParentModel.getChildList().add(uppy1);
//            UppyModel uppy2 = new UppyModel();
//            uppy2.setUppy_file_desc("2nd file");
//            yourParentModel.getChildList().add(uppy2);
        } else {
            System.out.println("yourParentModel is not null :child.size = " + yourParentModel.getChildList().size());
        }
        //if you have multiple file input, put the list to uppyFileMap
        getUppyFileMap().put("childList", yourParentModel.getChildList());
        return SUCCESS;
    }
    
    public String noLogin() {
        return SUCCESS;
    }
    
    public String validateLogin() {
        if (Validator.isEmpty(getBsUserId())) {
            addActionError("User ID is required");
        } else {
            UserDAOImpl userDAO = new UserDAOImpl();
            userDAO.setSession(baseDAO.getSession());
            try {
                User user = null;
                Map currentSession = ActionContext.getContext().getSession();
                if (!Validator.isEmpty(bsFPL_secret)) {
                    user = (User)baseDAO.getSession()
                            .getNamedQuery("User.findByUsUserId")
                            .setParameter("us_user_id", getBsUserId()).uniqueResult();
                    Debug.printDebug("bsFPL_device_id = " + bsFPL_device_id);
                    UserPubPriModel dbUserPubPri = (UserPubPriModel)baseDAO.getSession()
                        .getNamedQuery("UserKey.findByUsId_deviceId")
                        .setParameter("us_id", user.getID())
                        .setParameter("device_id", bsFPL_device_id)
                        .uniqueResult();
                    Debug.printDebug("bsFPL_secret = " + bsFPL_secret);        
                    Debug.printDebug("dbUserPubPri.getPri_key = " + dbUserPubPri.getPri_key());
                    String secret = new String(CommonFunction.decrypt(dbUserPubPri.getPri_key(), bsFPL_secret));
                    if (!secret.equals(currentSession.get("fpl_token"))) {
                        user = null;
                    }
//                    bsFPL_secret = UserDAOImpl.
                } else {
                    if (SWK_ID_LOGIN) {
                        SwkIdApi swkIdApi = new SwkIdApi();
                        Map map = SwkIdApi.validateSwkIdLogin(getBsUserId(), getBsPswd());
                        if (map.containsKey("transaction_status") && map.get("transaction_status").toString().equals("1") ) {
                            String access_token = (String)((Map)map.get("data")).get("access_token");
                            currentSession.put("sesAccessToken", access_token);
                            Map usr_info = swkIdApi.token_user(access_token);
                            if (usr_info.containsKey("usr_ldap_id")) { // SwkID-PK
                                currentSession.put("sSysssoUserObjId", usr_info.get("usr_ldap_id"));
                                if (usr_info.get("login_by") != null && usr_info.get("login_by").equals("sarawaknet")) {
        //                            // sarawaknet id
                                    currentSession.put("sSysUserObjId", usr_info.get("usr_ldap_sarawaknet_id"));
                                } else {
        //                            // sarawakid
                                    currentSession.put("sSysUserObjId", usr_info.get("usr_short_name"));
                                }
                                String userId = (String) usr_info.get("usr_ldap_id");
                                user = (User) baseDAO.getSession().getNamedQuery("User.findBySsoUid").setParameter("sso_uid", userId).uniqueResult();
                                if (user == null) { //not matching swk_id user found
                                    if (SWK_ID_CREATE_AS_NEW_USER) {
                                        ////////////////////////////////////////////////////////
                                        //       ADD CHECKING BEFORE INSERT USER TO DB        //
                                        //       Throw Exception if validation fail           //
                                        ////////////////////////////////////////////////////////
                                        user = new User();
                                        user.setUs_user_id((String)usr_info.get("usr_new_ic_no")); //use new IC
//                                        user.setUs_user_id((String)usr_info.get("usr_short_name")); //use swkid_loginId
                                        user.setUs_user_name((String)usr_info.get("usr_full_name"));
                                        user.setUs_password(usr_info.get("usr_new_ic_no") + getBsPswd().substring(0, 4));
                                        user.setUs_email((String)usr_info.get("usr_email"));
                                        user.setUs_last_login_date(DateUtil.getCurrentTimestamp());
                                        user.setSso_uid((String)currentSession.get("sSysssoUserObjId"));
                                        user.setSso_login_id((String)currentSession.get("sSysUserObjId"));
                                        user.setSso_enabled_date(DateUtil.getCurrentTimestamp());
                                        user.setUs_status("Y");
                                        user.set_operation("SWKID");
                                        userDAO.insert(user);
                                    } else {
                                        currentSession.put("bs_map_sso_uid", "bs_map_sso_uid"); //to mark this login need to do mapping with SSO ID
                                        addActionMessage(getText("errors.needToMapSwkId"));
                                        return "bs_map_sso_uid";
                                    }
                                } else {
                                    //if reach here, it means the swkid login success and mapped with your local user.
                                    
                                    //-- Update Last Login Date : START --//
                                    BaseDAO updateUserDAO = new BaseDAOImpl();
                                    try {
                                        NativeQuery query = null;
                                        query = updateUserDAO.getSession().createNativeQuery("update t_setup_user set us_last_login_date = :lastLoginDate, us_fail_attempt_count='0' where us_id = :usId");
                                        query.setParameter("lastLoginDate", DateUtil.getCurrentTimestamp());
                                        query.setParameter("usId", user.getID());

                                        updateUserDAO.beginBatchTransaction();
                                        query.executeUpdate();
                                        updateUserDAO.commitBatchTransaction();
                                    } catch (Exception e) {
                                        updateUserDAO.rollbackBatchTransaction();
                                    } finally {
                                        updateUserDAO.closeSession();
                                    }
                                    //-- Update Last Login Date : END --//
                                    
                                    ////////////////////////////////////////////////////////
                                    //       add any extra codes if needed here??         //
                                    ////////////////////////////////////////////////////////
                                }
                            }
                        }
                    } else {
                        user = userDAO.processLogin(getBsUserId(), getBsPswd(), SystemConstants.SYSTEM_TYPE.DEFAULT, "USERID");
                    }
                }
                if (user != null) {
                    loginBs(user);
                } else {
                    addActionError("Invalid User Id or password");
                    return "goLoginPage";
                }
            } catch (CustomBaseException be) {
                be.printStackTrace();
                addActionError(be.getMessage());
                return "goLoginPage";
            } catch (Exception e) {
                e.printStackTrace();
                addActionError("Fail to login");
                return "goLoginPage";
            }
            return SUCCESS;
        }
        return "goLoginPage";
    }
    
    private String actualUrl = null;
    public String getActualUrl() {
        return actualUrl;
    }
    public void setActualUrl(String actualUrl) {
        this.actualUrl = actualUrl;
    }
    
    public String getFpl_token() {
        Map currentSession = ActionContext.getContext().getSession();
        currentSession.put("fpl_token", CommonFunction.getId(25));
        return (String)currentSession.get("fpl_token");
    }
    
    public String processMapSSO() throws Exception {
        try {
            SWK_ID_LOGIN = Boolean.FALSE;
            validateLogin();
            Map sessionMap = ActionContext.getContext().getSession();
            if (sessionMap.get("logined") != null) {
                sessionMap.remove("bs_map_sso_uid");
                addActionMessage(getTextProvider().getText("swkId.mapped", new String[]{(String)sessionMap.get("loginId"), (String)sessionMap.get("userName")}));
            } else {
                addActionError("errors.mappedSwkIdFail");
                return "bs_map_sso_uid";
            }
        } catch (Exception e) {
            return "bs_map_sso_uid";
        }
        return SUCCESS;
    }
    
    public String loadPDF2() throws Exception {
//        File downloadFile = new File("C:\\Users\\user\\desktop\\SAINS-Fun-Run-2019_Reg-Form.pdf");
        File downloadFile = new File("C:\\Users\\user\\desktop\\SPA_19000046.pdf");
        inputStream = new FileInputStream(downloadFile);
        contentDisposition = "attachment; filename=\"SPA_19000046.pdf\"";
        contentLength = downloadFile.length();
//        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
//        response.setContentType("application/pdf");
//        
//        response.getWriter().append("done start running of " + current);
//        response.flushBuffer();
        return "pdf";
    }
    
    public String loadPDF() throws Exception {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        FileInputStream ficheroInput = new FileInputStream("C:\\Users\\user\\desktop\\SPA_19000046.pdf");
//        FileInputStream ficheroInput = inputStream;
        int tamanoInput = ficheroInput.available();
        byte[] datosPDF = new byte[tamanoInput];
        ficheroInput.read(datosPDF, 0, tamanoInput);

//        response.setHeader("Content-disposition", "filename=\"Sample.pdf\"");
        response.setHeader("Content-disposition", "attachment; filename=\"SPA_19000046.pdf\"");
        response.setContentType("application/pdf");
        response.setContentLength(tamanoInput);
        response.getOutputStream().write(datosPDF);

        response.getOutputStream().flush();
//        response.getOutputStream().close();
        
//        ficheroInput.close();
        return null;
    }
    private Long contentLength = 0L;
    private String contentType = "application/pdf";
//    private String contentType = "application/pdf";
    private String contentDisposition = "";
    private InputStream inputStream = null;
    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }
    
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    //** BubbleShell : END **//

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    
}
