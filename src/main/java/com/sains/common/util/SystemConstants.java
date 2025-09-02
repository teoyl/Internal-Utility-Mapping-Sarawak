package com.sains.common.util;

import com.PropertyGetter;
import com.SysConf;
import java.io.File;
import java.util.Date;
import java.util.Map;
import org.joda.time.DateTime;

/**
 * Holds the constant variables used by the system.
 *
 */
public class SystemConstants {
    public static Map<String, String> staticVarMap = null; //to store the value to localfile and load back the value when application started
    public static final String MySessionMap = "MySessionMap";
    public static final Boolean ShowException = Boolean.TRUE;
    public static final Boolean StressTestMode = Boolean.TRUE;
    /* *************************** COMMON *************************** */
//    public static final String DATABASE_TYPE = "ORACLE";
    public static final String DATABASE_TYPE = "MSSQL";
    public static final String[] TRACE_FOLDER = new String[]{"com.mrpe.", "com.sains.", "com.impian.", "com.sample."}; //eg: new String[]{"com.sample.", "com.newFolder."} //folderName with "." at the end!!
    public static final String NEW_LINE = System.getProperty("line.separator");
//    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
//    public static final String PATH_SEPARATOR = System.getProperty("path.separator");
    public static final String TAB = System.getProperty("\t");
    public static final String COMPANY_DATA_PATH = "company.data.path";
    //public static final int PASSWORD_CHANGE_INTERVAL = 30; // in days
    public static final String MATERIAL_DIR = "material.dir";
    public static final String HRM_DIR = "hrm.dir";
    public static final String REPORT_DIR = "report.dir";
    public static final String REPORT = "report";
    public static String AJP_PORT = null;
    public static final String COMMON_GROUP = "COMMON_GROUP";
    public static final String CONVERTED_BY_GEMS = "GEMS";  // ThoTH @ 21-Feb-2016
    public static final String EQP_SYS = "EQP";
    public static final String QP_SYS = "QP";
    public static final String WF_SYSTEM_CODE = "USJ";

    public static javax.servlet.ServletContext globalServletContext = null;

          
    public static class UI_FRAMEWORK_TYPE { 
        public static final String BOOTSTRAP_5 = "B5";
        public static final String BOOTSTRAP_4 = "B4";
        public static final String BOOTSTRAP_3 = "B3";
        public static final String FRAMEWORK7 = "F7";
    }
    public static class SYSTEM_SETUP { 
        public static final String UI_FRAMEWORK = UI_FRAMEWORK_TYPE.BOOTSTRAP_5;
        public static final String JSP_PREFIX = "b5_";
//        public static final String UI_FRAMEWORK = UI_FRAMEWORK_TYPE.BOOTSTRAP_4;
//        public static final String JSP_PREFIX = "b4_";
    }
    public static final Integer ENV = 0; // 0 - dev, 1 - live
//    public static class SMS_PARAM {
//        public static final String[] SERVER_URL = new String[]{
//            "http://smstplive.sains.com.my/?<api_key>&to=<sms_phoneNo>&message=<sms_msg>",
//            "https://sms.sains.com.my/cgi-bin/api?<api_key>&to=<sms_phoneNo>&message=<sms_msg>"
//        };
//    }
    public static class SmartXChange {
        public static class SETUP {
            public String[] CONSUMER_KEY = null;
            public String[] CONSUMER_SECRET = null;
            public String[] TOKEN_URL = null;
            public String[] BASE_URL = null;
            public String applicationName = null;
            SETUP(String[] key, String[] secret, String[] tokenUrl, String[] baseUrl, String appName) {
                CONSUMER_KEY = key;
                CONSUMER_SECRET = secret;
                TOKEN_URL = tokenUrl;
                BASE_URL = baseUrl;
                applicationName = appName;
            }
        }
        public static SETUP WasteBinApplicationSetup = new SETUP(
            new String[]{
                "RgGdvo1oMnIuS1D8T2yd0qg6WDMa", //WasteBinApplication
                ""
            },
            new String[]{
                "ZkpoQUPhNG143kAh__3fdEclqgga", //WasteBinApplication
                ""
            },
            new String[]{
                "https://apitnt.sains.com.my/token",
                "https://api.sains.com.my/token"
            }, 
            new String[]{
                "https://apitnt.sains.com.my/waste_bin_replacement/v1.0",
                "https://api.sains.com.my/waste_bin_replacement/v1.0"
            }, 
                "WasteBinApplication"
        );
    }
    public static class SarawakID {
        // at index 0 - dev
        // at index 1 - live
        public static final Boolean[] ENABLE_SWKID = new Boolean[]{
            SysConf.get("swkid.enabled").equalsIgnoreCase("y"),
            Boolean.FALSE
        };
        public static final String[] PLUGIN_URL = new String[]{
            SysConf.get("swkid.PLUGIN_URL"),
            "https://sarawakid.sarawak.gov.my/web/share/swkid_plugin/?lang=en"
        };
        public static final String[] VALIDATE_LOGIN_URL = new String[]{
            SysConf.get("swkid.VALIDATE_LOGIN_URL"),
            "https://sarawakid.sarawak.gov.my/web/apiv1/login/"
        };
        //the URL for each CLIENT_ID is fixed. If need to request new client_id and secret with swkid team 
        //if your system required swkid login
        public static final String[] SYSTEM_URL = new String[]{
            SysConf.get("swkid.SYSTEM_URL"),
            "http://10.17.101.219:8080/forNewProject" //change to LIVE's URL
        };
        public static final String[] API_URL = new String[]{
            SysConf.get("swkid.API_URL"),
            "https://sarawakid.sarawak.gov.my/web/apiv1/"
        };
        public static final String[] CLIENT_ID = new String[]{
            SysConf.get("swkid.CLIENT_ID"),
            "mis_web"
        };
        public static final String[] SECRET = new String[]{
            SysConf.get("swkid.SECRET"),
            "2320003e2c077c4f1118fb552be6d331"
        };

        public static final String API_TOKEN_EXCHANGE = SysConf.get("swkid.API_TOKEN_EXCHANGE");
        public static final String API_TOKEN_USER = SysConf.get("swkid.API_TOKEN_USER");
        public static final String API_USER_GET = SysConf.get("swkid.API_USER_GET");
        public static final String API_LOGIN = SysConf.get("swkid.API_LOGIN");
    }
        
    // ThoTH @ 2-Apr-2014
    public static final class Crypto {

        public static final String PassPhrase = "This Is Impian2 Encryption By ThoTH at 2 Apr 2014";
        public static final Integer KeySize = 128;
        public static final Integer IterationCount = 1000;  // default is 10000
    }
    
    public static final class SYSTEM_TYPE {

        public static final String DEFAULT = "DEF";
        public static final String PUBLIC = "PUB";
        public static final String ESS = "ESS";
        public static final String MOBILE = "MOB";  // ThoTH @ 6-Oct-2015
        public static final String EQP = "EQP";  // ahmadni @ 29-Aug-2016
        public static final String ESPA = "ESPA";  // ahmadni @ 29-Aug-2016
    }
  
    /**
     * Country
     */
    public static final class COMM_Country {

//        public static final int MALAYSIA = 154; //Commented by Zhafari @02-Jan-2014
        public static final String MALAYSIA = "MYS"; //Added by Zhafari @02-Jan-2014 - use String instead
        public static final String MALAYSIA_SETUP_CODE = "COU-MYS"; //Added by Zhafari @02-Jan-2014 - use String instead
        public static final String MALAYSIA_PhoneCode = "60"; //Added by thensw
    }

    public static final class COMM_State {

//        public static final int SARAWAK = 13; //Commented by Zhafari @02-Jan-2014
        public static final String SARAWAK = "13"; //Added by Zhafari @02-Jan-2014 - use String instead
        public static final String OVERSEA = "98";
        public static final String SARAWAK_SETUP_CODE = "STT-13"; //Added by thensw
    }

    public static final class ACTION_Status {

        public static final String SEARCH = "search";
        public static final String LOAD_ADD_PAGE = "load_add_page";
        public static final String INSERT_FAIL = "insert_fail";
        public static final String LOAD_EDIT_PAGE = "load_edit_page";
        public static final String LOAD_EDIT_FAIL = "load_edit_fail";
        public static final String UPDATE_FAIL = "update_fail";
        public static final String DELETE_FAIL = "delete_fail";
        public static final String DELETE = "delete";
        public static final String EXPIRED = "expired";
        public static final String NOT_AUTHORISED = "not_authorised";
        public static final String FAIL = "fail";
        public static final String REFRESH_EDIT = "refresh_edit";
        public static final String LOAD_VIEW_PAGE = "load_view_page";
        public static final String SUCCESS = "success";
        public static final String SUCCESS_VERIFY = "success_verify";//sereneChye @ 4/8/2016
    }

    /**
     * Status
     */
    public static final class DYNAMIC_ATTS {

        public static final String SEARCH_FIELD = "SearchField";
        public static final String SEARCH_FIELD_DD = "SearchField_dd";
        public static final String SEARCH_FIELD_LOOKUP = "SearchField_lookup";  //Added by Delvene @ 21-Oct-2013
        public static final String DYNAMIC_HIDDEN_FIELD = "dynamicHiddenField"; //Added by Delvene @ 21-Oct-2013
        public static final String DYNAMIC_FIELD_STYLE = "DynamicFieldStyle"; //Added by Delvene @ 15-Oct-2015
        public static final String SEARCH_LABEL = "SearchLabel";
        public static final String SEARCH_DATA = "SearchData";
        public static final String SEARCH_FIELDS_DB_NAME = "SearchFieldsDbName";
        public static final String DISPLAY_FIELD_HEADER = "DisplayFieldHeader";
        public static final String DISPLAY_FIELD = "DisplayField";
        public static final String DYNAMIC_RESULT = "DynamicResult";
        public static final String DYNAMIC_COLUMNS = "DynamicColumns";
        public static final String DYNAMIC_SEARCH_CONDITION = "DynamicSearcCondition";
        public static final String DYNAMIC_PRIMARY_KEY_COLUMN = "PrimaryKeyColumn";
        public static final String RETRIEVING_COLUMN = "RetrievingColumn";
        public static final String DYNAMIC_PAGE_NO = "pageNo";
        public static final String DYNAMIC_PAGE_SIZE = "pageSize";
        public static final String DYNAMIC_PAGE_NO_OF_ROWS = "noOfRows";
        public static final String DYNAMIC_PAGING_URL = "dynamicPagingURL";
        public static final String DYNAMIC_SEARCH_DESCRIPTION = "dynamicSearchDescription";
        public static final String DYNAMIC_SEARCH_ACTION_TYPE = "dynamicSearchActionType";
        public static final String DYNAMIC_SORT_FIELD = "dynamicSortField";
        public static final String CUSTOM_SQL = "customSQL";
        public static final String CUSTOM_COUNT_SQL = "customCountSQL";
        public static final String SPECIAL_CRITERIA = "special_criteria";
        public static final String[] AVAILABLE_RPT_FORMAT = new String[] {"pdf", "excel"};
    }

    /**
     * Action
     */
    public static final class COMM_Action {

        public static final String APPROVE = "APP";
        public static final String CLOSE = "C";
        public static final String DEACTIVATE = "IA";
        public static final String MANUALLY_CLOSE = "MC";
        public static final String RECALL = "RCL";
        public static final String RECOMMEND = "REC";
        public static final String REJECT = "RJ";
        public static final String RETURN = "RT";
        public static final String SAVE = "SV";
        public static final String SUBMIT = "SB";
        public static final String VERIFY = "VR";
        public static final String REVERSE = "RV";
        public static final String CANCELLATION = "CCL";
        public static final String COMPLETE = "CPL";
    }

    public static final class COMM_Parameter {

        public static final String LOGIN_ATTEMPTS = "P001";
        public static final String LOGIN_LOCKOUT_DURATION = "P002";
    }

    public static final class COMM_CurrencyType {

        public static final String MALAYSIA_RINGGIT = "MYR";
    }

    /**
     *
     */
    public static final class COMM_MaritalStatus {

        public static final String SINGLE = "S";
    }

    /**
     * Document Token
     */
    public static final class SAM_DocumentToken {

        public static final String YEAR_LONG = "[yyyy]";
        public static final String YEAR_SHORT = "[yy]";
        public static final String MONTH = "[mm]";
        public static final String DAY = "[dd]";
        public static final String NUMBER = "[num]";
    }

    /**
     * User Type
     */
    public static final class SAM_UserType {

        public static final String SUPER_ADMIN = "SP";
        public static final String SYSTEM_ADMIN = "SA";
        public static final String NORMAL = "NU";
    }

    /**
     * User Status
     */
    public static final class SAM_UserStatus {

        public static final String ACTIVE = "A";
        public static final String FREEZE = "F";
    }

    /**
     * Module
     */
    public static final class SAM_Module {

        public static final String ROOT_EMS = "EMS";
        public static final String SAM = "SAM";
        public static final String FINANCE = "FMS";
    }

    /**
     * Feature Type
     */
    public static final class SAM_FeatureType {

        public static final String CREATE = "C";
        public static final String SEARCH = "S";
        public static final String EDIT = "E";
        public static final String VIEW = "V";
        public static final String DELETE = "D";
        public static final String APPROVAL = "A";
        public static final String VERIFY = "VR";
        public static final String APPROVE = "APP";
        public static final String PRINT = "PR";
        public static final String RESET_PASSWORD = "RP";
        public static final String RESET_DOCUMENT = "RD";
        public static final String REVERSAL = "RV";
        public static final String CLOSE = "CL";
        public static final String REPORT = "RPT";
    }

    public static final class ReportType {

        public static final String PDF = "p";
        public static final String SpreadSheet = "x";
        public static final String MsWord = "w";
    }

    /**
     * User Authorisation Type
     */
    public static final class SAM_UserAuthorisationType {

        public static final String AMOUNT = "A";
        public static final String LEVEL = "L";
    }

    /**
     *
     */
    public static final class SAM_AuthorisationSubGroup {

        public static final String MATERIAL_TYPE = "MT";
    }

    /**
     *
     */
    public static final class SAM_AddressType {

        public static final String CORPORATE_OFFICE = "3";
        public static final String REGISTERED = "6";
    }

    /**
     * Added by TTH @ 28-Jul-2010 For Workflow General
     */
    public static final class WORKFLOW {

        public static final String Updated_By = "SYSTEM";
//        public static final String Updated_By = "WORKFLOWENGINE";
        public static final String NO_ACCESS_JOB = "no_access_job";
    }

    /**
     * Added by TTH @ 22-Oct-2010 For Backend General
     */
    public static final class BACKEND {

        public static final String DEFAULT_ID = "BACKEND"; //for defaultUpdatePropertuie
        public static final String BACKEND_CW = "BACKENDCW";
        public static final String Updated_By = "WORKFLOWENGINE";
        public static final String RecruitmentAgent = "RecruitmentAgent";
        public static final String CommonBackend = "CommonBackend";
        public static final String ADMIN_ID = "10";
    }

    /**
     * Added by ThenSW @ 19 Aug 2010 For POD History's default user
     */
    public static final class POD_history {

        public static final String DEFAULT_USER = "CUSTOMER";
    }

    /**
     * Added by ThenSW @ 19 Aug 2010 For PaymentCheck's default user
     */
//    public static final class PAYMENT_CHECK {
//
//        public static final String DEFAULT_USER = "PAYMENTCHECK";
//    }
    /**
     * Added by TTH @ 22-Mar-2013 For Workflow Criteria1 Type : To identify
     * different Workflow
     */
    public static final class WF_TYPE {

        public static final String Code_DataVerifier_PM = "CODEPOSTDV";
        public static final String Code_DataVerifier = "CODEDV";
        public static final String PR_DataVerifier = "PRDV";
        public static final String PR_CreateUser = "PRCREATEUSER";
        public static final String PR_ReportDuty = "PRReportDuty";
        public static final String PR_Disciplinary = "PRDisciplinary";
        public static final String PR_Magistrate = "PRMagistrate";
        public static final String PR_CreateUser_RM = "PRCREATEUSER_RM";
        public static final String PostNew_DataVerifier = "POSTNEWDV";
        public static final String PostNew29000_DataVerifier = "POSTNEW29000DV";
        public static final String PostEdit_DataVerifier = "POSTEDITDV";
        public static final String PostEdit29000_DataVerifier = "POSTEDIT29000DV";
        public static final String PostOperationNew_DataVerifier = "POSTOPERATIONNEWDV"; // For Post Operation - Extra Body
        public static final String PostOperationNew_GS_DataVerifier = "PostOperNewGSDV"; // ThoTH @ 26-May-2014 :: Post Operation - Gunasama
        public static final String PostOperationNew_XGS_DataVerifier = "PostOperNewXGSDV"; // ThoTH @ 26-May-2014 :: Post Operation - Bukan Gunasama
        public static final String PostOperationEdit_DataVerifier = "POSTOPERATIONEDITDV";
        public static final String PostBookBlockEdit_DataVerifier = "PostBookBlockEditDV";  // Delvene @ 02-Sep-2014 :: Penyekatan/ Penempahan Jawatan
        public static final String PostHolderEdit_DataVerifier = "POSTHOLDEREDITDV";
        public static final String PostHolder_PutusSandang_DataVerifier = "PostHolderPSDV"; // Delvene @ 12-Sep-2014 :: Putus Sandang
        public static final String PostHolder_Tamat_DataVerifier = "PostHolderTMTDV"; // Delvene @ 05-Nov-2014 :: Penamatan
        public static final String PostHolder_New_DataVerifier = "PostHolderNewDV"; // Delvene @ 05-Nov_2014 :: Pelantikan Baru
        public static final String PostHolder29000_DataVerifier = "POSTHOLDER29000DV";  //Delvene @ 03-Jan-2014
        public static final String PostNameEdit_DataVerifier = "POSTNAMEEDITDV";    //Added by Delvene @ 22-Oct-2013
        public static final String EstablishmentEditBA_DataVerifier = "EstBA_EDITDV";
        public static final String EstSectionEditBA_DataVerifier = "EstSectionBA_EDITDV";
        public static final String EstablishmentEditBU_DataVerifier = "EstBU_EDITDV";
        public static final String EstSectionEditBU_DataVerifier = "EstSectionBU_EDITDV";
//        public static final String EstablishmentArrange_DataVerifier = "EstablishmentARRANGEDV";
        public static final String ChangeScheme1_DataVerifier = "ChangeScheme1DV";
        public static final String ChangeScheme2_DataVerifier = "ChangeScheme2DV";
        public static final String Substitution_DataVerifier = "SubstitutionDV";
        public static final String DeletePost_DataVerifier = "DeletePostDV";
        public static final String TransferPost_DataVerifier = "TransferPostDV";
        public static final String TransferPostBA_DataVerifier = "TransferPostBADV";
        public static final String TransferPostBU_DataVerifier = "TransferPostBUDV";
        public static final String SchemeClass_DataVerifier = "SCHEMECLASSDV";  //Delvene @ 08-Nov-2013
        public static final String SalaryGrade_DataVerifier = "SALARYGRADEDV";  //Delvene @ 12-Nov-2013
        public static final String ServiceScheme_DataVerifier = "SERVICESCHEMEDV";  //Delvene @ 13-Nov-2013.
        public static final String PostSupervisorEdit_DataVerifier = "PostSupervisorEditDV";    //Delvene @ 07-Jan-2014
        public static final String PostBookBlock_GS_DataVerifier = "PostBookBlockGSDV";    //Delvene @ 04-Jul-2014
        public static final String PostBookBlock_XGS_DataVerifier = "PostBookBlockXGSDV";    //Delvene @ 04-Jul-2014

        // ## Recruitment
        public static final String Recruitment_G17 = "RM_G17";
        public static final String Recruitment_N27 = "RM_N27";
        public static final String Recruitment_X27 = "RM_X27";

        // ## TOS
        public static final String TOS_PANS = "TOS_PANS";
        public static final String TOS_STATUTORY_BODY = "TOS_SBODY";
        public static final String TOS_RECALL = "TOS_RECALL";
        public static final String TOS_ENDORSE = "TOS_ENDORSE";
        public static final String TOS_DataVerifier = "TOSDV";

        // ## Harta
        public static final String HARTA_DataVerifier = "HartaDV";
        public static final String HARTA_UPSM_DataVerifier = "HartaDV_UPSM";
        public static final String HARTA_Income_DataVerifier = "HartaIncomeDV";

        // ## Leave
//        public static final String LMS_3Posts = "LMS_3Posts"; // no more using
        public static final String LMS_CutiRehat = "LMS_CutiRehat";
        public static final String LMS_GHS = "LMS_GoldHandShake";

//        ## ACNL
        public static final String ACNL_DV = "ACNLDV";

        // ## Tatatertib
        public static final String TATA_Case_New = "TATA_New";
        public static final String TATA_Appeal = "TATA_Appeal";
        public static final String TATA_Cancel = "TATA_Cancel"; // AiMin @ 8-Sept-2015
        public static final String TATA_Warta = "TATA_Warta"; //ahmadni @ 9-Nov-2015
//        public static final String TATA_Case_Decision = "TATA_Decision";

        // ## Latihan 
        public static final String Training_42HoursLog_DataVerifier = "TrainingLogDV";
        public static final String Training_KIU_DataVerifier = "TrainingKIUDV";
        public static final String Training_CandidateList_DataVerifier = "TrainingCandidateDV";
        public static final String Training_ShortListed_DataVerifier = "TrainingShortListDV";

        // ## CPP 
        public static final String CPP_DataVerifier = "CPPDV";
        public static final String CPP_MasterList_Verifier = "CPPMV";

        //#eQP
        public static final String QpAppNew_DataVerifier = "QPAPP_NEWDV";
        public static final String QpAppRenew_DataVerifier = "QPAPP_RENEWDV";
        public static final String QpAppEdit_DataVerifier = "QPAPP_EDITDV";
        public static final String QpAppEdit_PO = "WFA001PO_VER";
        public static final String QPGazette_DataVerifier = "QPGAZETTE_VER";
        public static final String QpAppRenewCert_DataVerifier = "QPAPP_RENEWCERTDV";
    }

    /**
     * Added by TTH @ 29-Jun-2010 For Workflow Entity Rules Condition
     */
    public static final class WF_Entity_Rules {

//        public static final String TYPE = "type";
        public static final String CRITERIA1 = "criteria1";
        public static final String QP_APP_STATUS = "qp_app_status"; //ahmadni @ 20-Jul-2016
        public static final String QP_GAZETTE_STATUS = "qp_gaz_status"; //ahmadni @ 20-Jul-2016
        public static final String QP_APP_TYPE = "qp_app_type"; //ahmadni @ 23-Feb-2017
        public static final String QP_MEM_STATUS = "qp_mem_status"; //ahmadni @ 23-Feb-2017
    }

    /**
     * Added by TTH @ 27-Jul-2010 For Automail Setup- Auto Type Drop Down Code
     */
    public static final class AutoEmail_Type {

        public static final String AI = "AI";
        public static final String WF = "WF";
    }

    /**
     * Added by THENSW @ 12-Aug-2010 POD Status
     */
    public static final class PodStatus_Type {

        public static final String PENDING_APPROVAL = "PA";
        public static final String PENDING_PAYMENT = "PP";
        public static final String PAYMENT_IN_PROGRESS = "PI";
        public static final String PAYMENT_FAILED = "PF";
        public static final String PAYMENT_COMPLETED = "PC";
        public static final String REJECTED = "AR";
        public static final String PRODUCT_READY = "PR";
        public static final String DOWNLOADED_COLLECTED = "DC";
        public static final String APPROVED = "AA";
        public static final String NOT_APPLICABLE = "NA";
        public static final String EXPIRED = "EX"; //to be used in t_payment's status only.
    }

    /**
     * Added by Ivy @ 2-Sept-2010 For Public User Setup- Account Type Drop Down
     * Code
     */
    public static final class Account_Type {
//        public static final String BASIC = "B";
//        public static final String PREMIUM = "P";

        public static final String PUBLIC = "P";
        public static final String INTERNAL = "I";
    }

    /**
     * Added by ThenSW @ 19 Aug 2010
     */
    public static final class DOMAIN {
        public static final String domain = "https://utilitysurvey-tnt.sarawak.gov.my/";
        public static final String domain_esub = "https://utilitysurvey-tnt.sarawak.gov.my/esub/";
        public static final String domain_gis_tnt = "https://utilitysurvey-tnt.sarawak.gov.my/utimaps/";
        public static final String MapViewerKey = "utimaps2023";
        
        public static final String email_feedback = "support@system.gov.my";
        public static final String[] SERVER_URL = new String[]{
            SysConf.get("server_url"),
            SysConf.get("server_url")
        };
        public static final String[] PORTAL_URL = new String[]{
            SysConf.get("portal_url"),
            SysConf.get("portal_url")
        };
        public static final String[] OBS_END_POINT = new String[]{
            "https://obs.dsancloud.com",
            "https://obs.dsancloud.com"//change to live's end point
        };
        public static final String[] OBS_BUCKET_NAME = new String[]{
            "utimaps_tnt",
            "utimaps_tnt"//change to live's bucket name
        };
        public static final String[] OBS_ACCESS_KEY = new String[]{
            "99SBRW8NB8EXVW1VQLRO",
            "99SBRW8NB8EXVW1VQLRO"//change to live's ak
        };
        public static final String[] OBS_SECRET_KEY = new String[]{
            "CNcFuvlQ2UwWdvGd94vS6a2O6A0QjdngxNO8GznQ",
            "CNcFuvlQ2UwWdvGd94vS6a2O6A0QjdngxNO8GznQ"//change to live's sk
        };
        
        // EBPP LXGTOL SETTINGS TNT
        public static final String ebppHashKey = "2014";  
        public static final String ebppMerchantId = "LXGTOL";
        public static final String ebppBillerId = "LNS";
        public static final String ebppSrvId = "PY1";
        public static final String ebppEnquiryUrl = "https://uat.paymentgalaxy.com/eComPmtGateway/PaymentStatusEnquiry";
        
        public static final String netParamKey = "utimaps2023";
        
//        public static final String KeyStorePath = "C:\\ks\\"; //localhost
        public static final String KeyStorePath = "/home/utilitysurvey-tnt/ks/"; //tnt
        public static final String CryptoSignCert_Alias = "utimaps_api";
        public static final String CryptoSignCert_Password = "UTIMAPS@2024";
        public static final String CryptoSignCert_KeyStore = "utimaps_key.jks";

        //START Development
        public static final String loadContextDomainName = "http://localhost:8081/eSPA/internal"; //thensw:my port is 8084
        public static final String domainName = "http://localhost:8084/eSPA";
        public static final String fileSeperator = "//";//10.17.100.22
        public static final String logPath = "c:\\sdilog\\eSPA\\";

        //eLASIS Server
//        public static final String elasisServer ="http://localhost:8080/eLasis";
        public static final String elasisServer ="http://10.17.101.226:8086/eLasis";
        
        //for conn testing to elasis
        public static final String elasisDbUrl = "jdbc:oracle:thin:@10.17.101.162:1622:elasisuat";
        public static final String elasisDbLogin = "eland";
        public static final String elasisDbPwd = "eland4elasisuat";
        
        //END Development

        // ***** COMMON ***** //
        public static final String war_version = "2015.04.01.01 (Distributable :: No Libs)";
        public static final String webServer = "TomCat";
        public static final String dbServer = "MSSQL";
//        public static final String email_SAINSCC = "callcentre@sains.com.my";
        public static final String email_SAINSCC = "zhafarii@sains.com.my";
        private static final String prFilePath = "pr\\files\\";
        public static final String prDocPath = "pr\\";
        public static final String recruitDocPath = "recruit\\";
        public static final String tosAttachmentPath = "tos\\";
        public static final String tosTempPath = "tos\\temp\\";
        public static final String hartaAttachmentPath = "harta\\attc\\";
        public static final String hartaPdfPath = "harta\\pdf\\";
        public static final String trainingDocPath = "training\\";
        public static final String cppAttachmentPath = "cpp\\attachment\\";

        public static final String writeFilePath_linux = "/home/eqpkpps/logs/";
        public static final String writeFilePath_window = "C:\\sdilog\\espa\\";
        public static final String profileImagePath = "eQP" + DOMAIN.fileSeperator + "profile" + DOMAIN.fileSeperator + "images" + DOMAIN.fileSeperator;//sereneC @ 4/7/2016
        public static final String registrationPath = "eQP" + DOMAIN.fileSeperator + "registration" + DOMAIN.fileSeperator;
        public static final String termsCondsPath = "termsConds" + DOMAIN.fileSeperator;
        public static final String qpAppEduTempPath = "eQP" + DOMAIN.fileSeperator + "application" + DOMAIN.fileSeperator + "education" + DOMAIN.fileSeperator + "temp" + DOMAIN.fileSeperator;//sereneC @ 4/7/2016
        public static final String qpAppPath = "eQP" + DOMAIN.fileSeperator + "application" + DOMAIN.fileSeperator;
        public static final String qpProfilePath = "eQP" + DOMAIN.fileSeperator + "profile" + DOMAIN.fileSeperator;
        public static final String qpRegisterPath = "eQP" + DOMAIN.fileSeperator + "QPRegister" + DOMAIN.fileSeperator;
        public static final String setupSignaturePath = "eQP" + DOMAIN.fileSeperator + "setup" + DOMAIN.fileSeperator + "signature" + DOMAIN.fileSeperator;
        public static final String cardSetupPath = "eQP" + DOMAIN.fileSeperator + "setup" + DOMAIN.fileSeperator + "card" + DOMAIN.fileSeperator;
        public static final String qpCertMRPEChop = "eQP" + DOMAIN.fileSeperator + "QPRegister" + DOMAIN.fileSeperator + "image" + DOMAIN.fileSeperator;
        public static final String qpGazettePath = "eQP" + DOMAIN.fileSeperator + "QPGazette" + DOMAIN.fileSeperator;
        public static final String qpSignSetupPath = "eQP" + DOMAIN.fileSeperator + "setup" + DOMAIN.fileSeperator + "reportImage" + DOMAIN.fileSeperator;
        public static final String qpTempPath = "eQP" + DOMAIN.fileSeperator + "temp" + DOMAIN.fileSeperator;
        public static final String userManual = "eQP" + DOMAIN.fileSeperator + "setup" + DOMAIN.fileSeperator + "userManual" + DOMAIN.fileSeperator;
        public static final String ldapServer = "ldap://ldap.sarawak.gov.my:389";

        public static final String version = "V1.0";// ahmadni @ 11-Jul-2017

        public static final String PbUTiMAPSHashKey = "PublicUTIMAPS$2023@LSD";
        public static final String domain_utimaps = "http://localhost:8080/utimaps-internal/"; //MUST POINT TO TRUNK -local
        
        // sarawakpay TNT
        public static final String sarawakPayPreOrderH5Url = "https://xservice.sains.com.my/xservice/H5PaymentAction.preOrder.do"; //H5
        public static final String sarawakPayCashierH5Url = "https://xservice.sains.com.my/xservice/H5PaymentAction.cashier.do"; //H5
        public static final String sarawakPayQueryOrderH5Url = "https://xservice.sains.com.my/xservice/H5PaymentAction.queryOrder.do"; //H5
        public static final String sarawakPayOrderRefundH5Url = "https://xservice.sains.com.my/xservice/H5PaymentAction.queryOrder.do"; //H5
        public static final String sarawakPayOrderCancellationH5Url = "https://xservice.sains.com.my/xservice/H5PaymentAction.closeOrder.do"; //H5
        public static final String sarawakPayReconciliationDownloadH5Url = "https://xservice.sains.com.my/statement/BillDownloadAction.downloadbill.do"; //H5
        public static final String sarawakPayPreOrderQRUrl = "https://xservice.sains.com.my/xservice/QRCodePaymentAction.preOrder.do"; //QR
        public static final String sarawakPayQueryOrderQRUrl = "https://xservice.sains.com.my/xservice/QRCodePaymentAction.queryOrder.do"; //QR
        public static final String sarawakPayOrderRefundQRUrl = "https://xservice.sains.com.my/xservice/QRCodePaymentAction.orderRefund.do"; //QR
        public static final String sarawakPayRefundOrderQueryQRUrl = "https://xservice.sains.com.my/xservice/QRCodePaymentAction.refundOrderQuery.do"; //QR
        public static final String sarawakPayOrderCancellationQRUrl = "https://xservice.sains.com.my/xservice/QRCodePaymentAction.closeOrder.do"; //QR
        public static final String sarawakPayReconciliationDownloadQRUrl = "https://xservice.sains.com.my/statement/BillDownloadAction.downloadbill.do"; //QR

        public static final String ISM_EDIT_URL_WEB = "[[FORM_URL]]/";
        public static final String ISM_EDIT_URM_MOBILE = "[[FORM_URL]]/";
	public static final String ISM_CONSUMER_KEY = SysConf.get("ism.consumer.key");
        public static final String ISM_CONSUMER_SECRET = SysConf.get("ism.consumer.secret");
        public static final String ISM_URL_GENERATE_ACCESS_TOKEN = SysConf.get("ism.token.url");
        public static final String AGENCY_ID = SysConf.get("ism.agency.id");
        public static final String AGENCY_NAME = SysConf.get("ism.agency.name");
    	public static final String ISM_URL_CONTEXT = SysConf.get("ism.url.context");
    }

    public static String getProfileImagePath() {
//       if (DOMAIN.isLinuxSftp) {
//           return DOMAIN.profileImagePath_linux;
//       }
        return DOMAIN.profileImagePath;
    }

    public static String getPrAttachmentPath() {
//       if (DOMAIN.isLinuxSftp) {
//           return DOMAIN.prAttachmentPath_linux;
//       }
        return DOMAIN.profileImagePath;
    }

    public static String getWriteFilePath() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            return DOMAIN.writeFilePath_window;
        }
        return DOMAIN.writeFilePath_linux;
    }

    public static final class CRITERIA {

        public static final String NO_CHANGE = "_no_change";
        public static final String NO_CHANGE_LOWER = "_no_change_lower";
        public static final String DATE_CRITERIA = "_date_";
        public static final String WILD_CARD_FRONT = "_wildCardFront";
        public static final String WILD_CARD_BACK = "_wildCardBack";
        public static final String NESTEDSQL = "NESTEDSQL";
        public static final String CUSTOM = "_custom_";
        //public static final String NO_CHANGE = "";
    }

    public static final class DATE {

        public static final String dataEntryFormat = new PropertyGetter().getPropertyText("date_default_date_dr");
        public static final String dataEntryFormat2 = new PropertyGetter().getPropertyText("date_default_date_dr2");
        public static final String dataEntryFormat3 = new PropertyGetter().getPropertyText("date_default_date_dr3");
        public static final String dataEntryFormat4 = new PropertyGetter().getPropertyText("date_default_date_dr4");
        public static final String dataEntryFormat5 = new PropertyGetter().getPropertyText("date_default_date_dr5");
        public static final String dataTimeEntryFormat = new PropertyGetter().getPropertyText("date_default_datetime_dr");
        public static final String dataTimeAmPmEntryFormat = new PropertyGetter().getPropertyText("date_default_datetime_dr2");
        public static final String activityLogDateTimeFormat = new PropertyGetter().getPropertyText("date_activityLog_datetime");
        public static final String timeFormat = new PropertyGetter().getPropertyText("date_default_time");
    }

    public static final class QUERY {

        public static final String JOIN = "_join_";
        public static final String INNER_JOIN = "inner join ";
        public static final String OUTER_JOIN = "outer join ";
        public static final String LEFT_JOIN = "left join ";
    }

    public static final class METHOD {

        public static final String SET_UPDATED_BY = "setUpdated_by";
        public static final String GET_ID = "getID";
        public static final String SET_ID = "setID";
    }

    public static final class HQL {

        public static final String JOIN = "_join_";
        public static final String DEFAULT_CONDITION = "_defaultCond_";
    }

    public static final class COMM_OPERATION {

        public static final String INSERT = "insert";
        public static final String UPDATE = "update";
        public static final String APPROVE = "approve";
        public static final String REJECT = "reject";
        public static final String DELETE = "delete";
        public static final String UNDO_ESS = "undo";
        public static final String UNDO_VERIFIER = "undo_verifier";
        public static final String ACTIVATE_ACCOUNT = "activate_account";  
        public static final String FORGOT_PASSWORD = "forgot_password";
        public static final String RESET_PASSWORD = "reset_password";
    }

    public static final class USER_ACC_STATUS { // added @2.12.2011 ai min

        public static final String ACTIVE = "Y";
        public static final String INACTIVE = "I";
        public static final String CANCELLED = "C";
        public static final String LOCKED = "L"; // added @1.2.2012 ai min
        public static final String NEW = "N"; // added by ChangMH @ 27/10/2014 :: newly added user(PBT/BBN) by HRM user
        public static final String NONLDAP = "X"; // added by sereneC @ 31/07/2015 
    }

    // added by sereneC @ 7.11.2014 
    public static final class USER_GROUP_CODE {

        public static final String HRMAdmin = "HRM_Central"; // added @7.11.2014 sereneC
        public static final String HRMMaster = "HRM_Master"; // added @7.11.2014 sereneC
        public static final String DEPAdmin = "DEP_Admin";
        public static final String JOBDoer = "JobDoer";
        public static final String HRMGlobalCode = "HRM_GlobalCode";
        public static final String HRMPMCode = "HRM_PM_Code";
        public static final String WFHRMGlobalCode = "WFHRM_GlobalCode";
        public static final String WFHRMPMCode = "WFHRM_PM_Code";
        public static final String SUKROLE = "SUK01";
        public static final String SelfService = "SelfService";
        public static final String Dept_HRDO = "DEP_HRDO";
        public static final String POGroup = "WFA001PO_VER";//ahmadni @ 14-Feb-2017

    }

    // added by sereneC @ 7.11.2014  
    public static final class USER_GROUP_LEVEL {

        public static final String FIRSTLEVELADMIN = "4";
        public static final String SECONDLEVELADMIN = "3";
        public static final String THIRDLEVELADMIN = "2";
        public static final String NORMALUSER = "1";
    }

    public static final class USER_GROUP_TYPE {

        public static final String WORKFLOW = "W";
        public static final String APPLICATION = "A";

    }

    public static final class AUDIT_LOGIN { // added @26.09.2013 thensw **copy from elodgement

        public static final class TYPE {

            public static final String INTERNAL = "I";
            public static final String ESS = "E";
            public static final String MOBILE = "M";
            public static final String PUBLIC = "P";
            public static final String EQP = "Q";
        }

        public static final class STATUS {

            public static final String SUCCESS = "S";
            public static final String FAIL = "F";
            public static final String UNDER_ACTING = "A";
        }

        // ThoTH @ 24-Apr-2014
        public static final class ACTION {

            public static final String LOGIN = "I";
            public static final String LOGOUT = "O";
            public static final String TIMEOUT = "T";
        }
    }

    //serene @ 11/12/2014
    public static final class AUDIT_LOG {

        public static final class ACTION {

            public static final String DELETE = "DEL";
            public static final String UPDATE = "UPD";
        }
    }

    public static final class BACKENDSERVICENAME {

        public static final String CALLWORKFLOW = "CALL_WORKFLOW";
        public static final String PAYMENTCHECK = "PAYMENTCHECK";
    }

    public static final class DefaultValue {

        public static final String DivisionHQ = "016";
    }

    /*
     * Zhafari
     * 02-Jan-2014
     * SetupCodeModel code_1
     */
    public static final class COMM_SetupCode {

        public static final String OTHER = "99";
        public static final String NO_INFO = "00";
    }

    public static final class COMM_ActiveStatus {

        public static final String ACTIVE = "A";
        public static final String INACTIVE = "I";
    }
    //END - Zhafari

    /**
     * Created by ThoTH @ 7-Jan-2014
     *
     * Discussed with SW, mainly for PR module, in order to improve the System
     * performance, we allowed Hardcode. However, all the Hardcode ID must be in
     * this section, and those code shall follow proper format, e.g. ABC-001, so
     * can be added back if accidentally deleted.
     *
     * Later in the Setup Code Maintenance, these codes shall be be deleted, no
     * matter there is recording pointing to these codes or not.
     */
    public static final class SETUP_CODE_HARDCODE {

        // ServiceHitotry
        public static final class ServHistory {

            public static final String PR_MelaporDiri = "HID-PR-001";
            public static final String Tata_001 = "HID-TATA-001";
            public static final String Tata_002 = "HID-TATA-002"; // AiMin @ 21-Sept-2015
            public static final String PR_Anugerah = "HID-002";
            public static final String PR_Kursus = "HID-009";
            public static final String PM_Pelantikan = "HID-007";
            public static final String PR_MelaporDiri_TukarTempat = "HID-PR-002";
            public static final String PM_KenaikanPangkat = "HID-001";
//            public static final String PM_TukarButiran = "HID-xx";
//            public static final String PM_LantikanSemula = "HID-xxx";
//            public static final String PM_Pinjaman = "HID-xxx";
            public static final String PM_Memangku = "HID-013";
            public static final String PM_TukarTempatTugas = "HID-048";
//            public static final String PM_Kontrak = "HID-xxx";
            public static final String PM_JalanTugas = "HID-014";
            public static final String PM_TukarLantik = "HID-040";
            public static final String PM_Penamatan = "HID-024";
            public static final String Lain_Lain = "HID-099";

            // Leave
            public static final String Leave_TanpaRekod = "HID-LVT-TR";
            public static final String Leave_SeparuhGaji = "HID-LVT-GS";
            public static final String Leave_TanpaGaji = "HID-LVT-GT";
            public static final String Leave_Haji = "HID-LVT-HJ";
            public static final String Leave_Belajar = "HID-LVT-LJ";
            public static final String Leave_SakitTBK = "HID-LVT-SP";
            public static final String Leave_Sakit = "HID-LVT-SK";
            public static final String Leave_Bersalin = "HID-LVT-BS";
            public static final String Leave_GHS = "HID-LVT-GC";

            // ACNL
            public static final String ACNL_Acting = "HID-013";  // ID already there, dun want kacau
            public static final String ACNL_Covering = "HID-014";  // ID already there, dun want kacau
            public static final String ACNL_Acting_Cancel = "HID-ACNL-003";
            public static final String ACNL_Covering_Cancel = "HID-ACNL-004";
        }

        public static final class Non_Setup_Code {

            public static final String Department_SPANS_BU = "SPANS_BU";
            public static final String Department_B_NS = "B.NS";    // Delvene @ 27-Mar-2015 :: For 29000 Default Dept
            public static final String Department_SUK_BU = "SUK_BU";    // ChangMH @ 07-Apr-2015 :: For TOS filter ministry_id 
            public static final String Department_JKM = "JKM";    // ChangMH @ 21-Sep-2015 :: For TOS filter ministry_id 

            public static final String PPBIL142008 = "PPBIL-14/2008";  // ThoTH @ 27-Oct-2015 :: Latest Pekeliling used by Leave

        }

        //ADT
        public static final String ADT_Tetap = "ADT-01";
        public static final String ADT_Menyurat = "ADT-02";
        public static final String ADT_Semasa = "ADT-03";

        //HIT code_1
        public static final String HIT_MelaporDiri = "HIT-019";
//        public static final String HID_MelaporDiri = "HID-019";  // Commented by ThoTH @ 21-Nov-2014

        //EAP
        public static final String EAP_Awam = "EAP-5"; //Zhafari @ 19-Nov-2014

        public static final String APT_Tetap = "APT-001"; //Delvene @ 15-May-2014
        public static final String APT_Kontrak = "APT-002"; //Delvene @ 21-Jan-2014
        public static final String APT_Pinjaman = "APT-005";
        public static final String APT_Sangkutan = "APT-007";   //Delvene @ 21-Jan-2014
        public static final String APT_Memangku = "APT-020"; //Delvene @ 15-May-2014
        public static final String APT_LantikSementara = "APT-021"; //Delvene @ 15-May-2014
        public static final String COU_Malaysia = "COU-MYS";
        public static final String DSC_Kemalangan = "DSC-02";
        public static final String PPT_Antarabangsa = "PPT-01";
        public static final String ACN_KWSP = "ACN-7001";
        //LCT
        public static final String LCT_Lesen_Memandu = "LCT-01";
        public static final String LCT_Lesen_Penerbangan = "LCT-02";
        public static final String LCT_Lesen_Malim = "LCT-03";
        // MRS
        public static final String MRS_Belum_kahwin = "MRS-1";
        public static final String MRS_Kahwin = "MRS-2";
        // RLS
        public static final String RLS_Isteri = "RLS-01";
        public static final String RLS_Suami = "RLS-02";
        public static final String RLS_Ibu = "RLS-03";
        public static final String RLS_Bapa = "RLS-04";
        public static final String RLS_Anak_Kandung = "RLS-05";
        public static final String RLS_Anak_Tiri = "RLS-06";
        public static final String RLS_Anak_Angkat = "RLS-07";
        public static final String Anak_Tidak_Sah_Taraf = "RLS-08";
        public static final String RLS_Nenek = "RLS-09";
        public static final String RLS_Datuk = "RLS-10";
        public static final String RLS_Bekas_Isteri = "RLS-13";
        public static final String RLS_Bekas_Suami = "RLS-14";
        //EDU
        public static final String EDU_Stpm = "EDU-13";
        public static final String EDU_Spm = "EDU-14";
        public static final String EDU_Pmr = "EDU-15";
        public static final String EDU_Sekolah_Rendah = "EDU-16";
        public static final String EDU_Taman_Asuhan = "EDU-17";
        public static final String EDU_Sekolah_Menengah = "EDU-21";
        public static final String EDU_Sekolah_Khas = "EDU-22";
        //STT
        public static final String STT_No_Info = "STT-00";
        public static final String STT_Sarawak = "STT-13";
        public static final String STT_Luar_Negara = "STT-98";
        public static final String STT_Sabah = "STT-12";            //ChangMh @ 17-Mar-2014
        public static final String STT_Labuan = "STT-15";           //ChangMh @ 17-Mar-2014
        public static final String STT_Lain_lain = "STT-99";        //ChangMh @ 19-Mar-2014
        //TIT
        public static final String TIT = "TIT";
        public static final String TIT_Encik = "TIT-L001";
        public static final String TIT_Cik = "TIT-P019";
        public static final String TIT_Puan = "TIT-P020";

        //AWD
        public static final String AWD = "AWD";

        //TWN
        public static final String TWN_No_Info = "TWN-0000";
        public static final String TWN_Other = "TWN-9999";
        public static final String TWN_Kuching = "TWN-1320";

        //PTY :: Delvene @ 30-Apr-2014
        public static final String PTY_Tetap = "PTY-001";
        public static final String PTY_JBC = "PTY-002";
        public static final String PTY_29000 = "29000"; // ThoTH @ 24-Nov-2015

        //PTC :: Delvene @ 22-Aug-2014
        public static final String PTC_Terbuka = "PTC-06";

        //REL :: Delvene @ 14-Aug-2014 
        public static final String REL_LainLain = "REL-99";

        //SKC
        public static final String SKC_SKM = "SKC-01";
        public static final String SKC_CIDB = "SKC-02";
        public static final String SKC_Other = "SKC-99";

        //LVT
        public static final String LVT_REHAT = "LVT-RH";
        public static final String LVT_REHAT_LANJUTAN = "LVT-RL";
        public static final String LVT_SEPARUH_GAJI = "LVT-GS";
        public static final String LVT_TANPA_GAJI = "LVT-GT";
        public static final String LVT_TANPA_REKOD = "LVT-TR";
        public static final String LVT_Pembatalan = "LVT-RB";
        public static final String LVT_PemanggilanBalik = "LVT-RC";
        public static final String LVT_ISTERI_BERSALIN = "LVT-BI";
        public static final String LVT_BERSALIN = "LVT-BS";
        public static final String LVT_SAKIT = "LVT-SK";
        public static final String LVT_SAKIT_GAJI_PENUH = "LVT-SP";  // aka SAKIT TBK
        public static final String LVT_HAJI = "LVT-HJ";
        public static final String LVT_MATI_AHLI_KELUARGA = "LVT-KA";
        public static final String LVT_BELAJAR = "LVT-LJ";
        public static final String LVT_KUARANTINA = "LVT-KU";
        public static final String LVT_KECEDERAAN = "LVT-CD";

        public static final String LVT_GHS = "LVT-GC";

        //LAP - ThoTH
        public static final String LAP_SUP = "LAP-SUP";
        public static final String LAP_LC = "LAP-LC";
        public static final String LAP_HOD = "LAP-HOD";
        public static final String LAP_SUK = "LAP-SUK";
        public static final String LAP_SPANS = "LAP-SPANS";

        // DSP - ThoTH
        public static final String DSP_TangguhGaji = "DSP-04"; //cater for new punishment requirement from spans @ ahmadni 24-Aug-2015 
        public static final String DSP_TurunGaji = "DSP-05";
        public static final String DSP_TurunPangkat = "DSP-06";
        public static final String DSP_BuangKerja = "DSP-07"; // AiMin @ 09-Oct-2015

        //POJ
        public static final String POJ_Constitued = "POJ-001";
        public static final String POJ_Designated = "POJ-002";
        public static final String POJ_Statutory = "POJ-003";
        public static final String POJ_Oridinary = "POJ-004";

        //NGJ :: Gelaran
        public static final String GELARAN_Prefix = "NGJ";
        public static final String Pengarah = "NGJ-KJ1";
        public static final String Residen = "NGJ-KJ2";
        public static final String SUT = "NGJ-KJ3";
        public static final String DO = "NGJ-DO";
        public static final String SAO = "NGJ-SAO";

        //COR :: Jenis Organisasi
        public static final String COR_PublicSector = "COR-01";

        //USER ACCESS
        public static final String SAINS = "SAINS";

        //DIV :: Bahagian=
//        public static final String DIV_NA = "DIV-014"; 
        public static final String DIV_Prefix = "DIV";
//        public static final String DIV_Kuching = "DIV-001"; 

        //KTP :: Kategori Tujuan Perjalanan *These 4 category need to submit a report after applicant return from the travelling*
        public static final String KTP_Bengkel = "KTP003";
        public static final String KTP_Persidangan = "KTP004";
        public static final String KTP_Seminar = "KTP005";
        public static final String KTP_Kursus = "KTP006";

        //APR :: Sebab Pelantikan *These 2 type allow staff to sandang blocked/booked post
        public static final String APR_PelantikanBaru = "APR-001";
        public static final String APR_Memangku = "APR-011";
        public static final String APR_Acting = "APR-014";
        public static final String APR_TukaranTempatTugas = "APR-012";

        //JIK
        public static final String JIK_Kursus = "JIK1";
//        public static final String JIK_KIK1 = "JIK13"; 
//        public static final String JIK_KIK2 = "JIK14"; 
//        public static final String JIK_KIU = "JIK15"; 
    }

    // ThOTH @ 24-Jan-2014
    public static final class PARAM_NAME {

        public static final String RM_USE_TYPE3 = "RM_USE_TYPE3";
        public static final String CODE_HIDE = "CODE_HIDE";
        public static final String USER_GROUP_HIDE = "USER_GROUP_HIDE";  //Added by ChangMH @ 04-Feb-2015 :: Hide the JobDoer group at user access group list 
        public static final String CODE_ACR_SHOW = "CODE_ACR_SHOW";  //Added by ChangMH @ 27-Apr-2015 :: Code : show code_acr field at data entry screen
    }

    // ThOTH @ 14-Feb-2014
    public static final class ACCESS_TYPE {

        public static final String SAINS = "9";
        public static final String Sarawak = "8";
        public static final String Ministry = "7";
        public static final String Department = "6";
        public static final String Unit = "5";
        public static final String Unit_selectedOnly = "4";
    }

    //Delvene @ 21-Feb-2014
    public static final class DEPT_CODE {

        public static final String DEPT_JKM = "JKM";    //HRM
        public static final String COMMON = "COMMON";    //ThoTH @ 23-Sep-2014 :: For New Security
    }

    //ChangMh @ 28-Mar-2014
    public static String getTosAttachmentPath() {
        return DOMAIN.tosAttachmentPath;
    }

    public static final class SYSTEM_PARAM_SETUP {

        public static final class SAMPLE {

            public static final String SampleInput1 = "SampleInput1";
        }

        public static final class BACKEND {

            public static final String AUTO_BACKEND = "AUTO_BACKEND";
            public static final String KEY_BACKEND = "KEY_BACKEND";
        }

        public static final class RM {

            public static final String RM_OL_RETURN = "RM_OL_RETURN";
            public static final String RM_SHOW_RETIRE = "RM_SHOW_RETIRE";
            public static final String RM_USE_TYPE3 = "RM_USE_TYPE3";
        }
    }

    //Zhafari @ 17-Jul-2014
    public static final class IDENTIFICATION_TYPE { //for t_employee.emp_id_type

        public static final String NEW_IC = "IC";           //New IC
        public static final String ARMY_POLICE_IC = "AP";   //New IC
        public static final String PASSPORT = "PP";         //Passoprt ID
    }

    //Zhafari @ 23-Sep-2014
    public static final class PBT_LOGIN_TYPE { //for t_employee.pbt_login_type

        public static final String NEW_IC = "ICNO";
        public static final String LDAP = "LDAP";
    }

    public static String getHartaAttachmentPath() {
        return DOMAIN.hartaAttachmentPath;
    }

    public static String getHartaPdfPath() {
        return DOMAIN.hartaPdfPath;
    }

    //ChangMh @ 04-Aug-2014
    public static final String SAINS_CALL_CENTER = "http://callcentre.sains.com.my";
    public static final String SAINS_FEEDBACK = "http://feedback.sains.com.my";

    public static final class RedirectPattern { //for t_employee.emp_id_type

        public static final String OPEN = "<redirectUrl_>";
        public static final String CLOSE = "</redirectUrl_>";
        public static final String ALIVE = "<I_AM_ALIVE>";
    }

    //ChangMH @ 30-Sep-2014 : for t_employee.emp_type
    public static final class EMP_TYPE {

        public static final String PAN = "PAN";
        public static final String PBT_BBN = "PBT/BBN";
    }

    //Zhafari @ 14-Oct-2014 : for t_employee.emp_type
    public static final class GROUP_CODE {

        public static final String ESS = "SelfService";
    }

    //ChangMH @ 11-Nov-2014 
    public static final class ACTIVE_FLAG {

        public static final String ACTIVE = "Y";
        public static final String INACTIVE = "N";
    }

    //Zhafari @ 03-Dec-2014
    public static final class WEB_SERVICE_METHOD {

        public static final String POST = "POST";
        public static final String GET = "GET";
    }

    // ThoTH @ 26-Dec-2014
    public static final class NAVIGATION {

        public static final String INPOOL = "___IN_POOL___";
    }

    // ChangMH @ 13-Jan-2015
    public static final class SETUP_CODE_ACTION {

        public static final String GLOBAL = "GlobalSetupCode ";
        public static final String PM = "PostSetupCode ";
    }

    //ahmadni @ 28-Jul-2015
    public static String getCPPAttachmentPath() {
        return DOMAIN.cppAttachmentPath;
    }

    //ahmadni @ 1-Oct-2015
    public static final class ACCESS_MODE {

        public static final String NORMAL = "NORMAL";
        public static final String READONLY = "READONLY";

    }

    public static final class SYSTEM_NAME {

        public static final String EQP = "EQP"; //ahmadni @ 21-June-2016
        public static final String ESPA = "ESPA"; //ahmadni @ 21-June-2016
    }

    public static final class USERSUBSCRIPTION {

        public static final class LIST {

            public static final String APPLYEQP = "APPEQP";
            public static final String APPLYESPA = "APPSPA";
        }

        public static final class STATUS {

            public static final String APPROVED = "AA";
            public static final String REJECTED = "AR";
            public static final String PENDING = "PA";

        }
    }

    //bernard @29-JUN-2016 : My Profile Module
    public static final class MY_PROFILE {

        public static final String PERSONAL_INFORMATION = "PI";
        public static final String CORPORATE_PROFILE = "CP";
    }

    //ahmadni @ 29-june-2016 : Group ID
    public static final class GROUP_ID {

        public static final String QP_ADMIN = "QP_Admin";
        public static final String QP_STAFF = "QP_Staff";
        public static final String QP_COMMON = "QP_Common";
        public static final String QP_COMMON2 = "1507260128992hYem7x0";//eqp with espa application - ahmadni @ 26-Apr-2018
        public static final String SPA_PUBLIC = "SPA_Public";
    }

    //ahmadni @ 29-june-2016 : Group TYPE
    public static final class GROUP_TYPE {

        public static final String APPLICATION = "A";
        public static final String WORKFLOW = "W";
    }

    //ahmadni @ 29-june-2016 : Group TYPE
    public static final class GROUP_OPTION {

        public static final String PARENT = "P";
        public static final String CHILD = "C";
        public static final String DEFAULT = "D";
    }

    //serene@29-JUN-2016 : My Profile Module
    public static final class REG_QUALIFICATION_PART {

        public static final String PART_ONE = "P1";
        public static final String PART_TWO = "P2";
        public static final String PART_ONE_TWO = "P3";
    }

    public static final class REG_QUALIFICATION {

        public static final String ARCHITECT = "ARC";
        public static final String ENGINEER = "ENG";
        public static final String LAND_SURVEYOR = "LS";
        public static final String TOWN_PLANNER = "TP";
        public static final String VALUER = "VAL";
        public static final String SEVEN_YEAR_EXP = "EXP";
    }

    //ahmadni @ 29-june-2016 
    public static final class SYSTEM {

        public static final String QP = "2";
    }

    //ahmadni @ 29-june-2016 : For user account added from Public Company
    public static final class SIGNUP {

        public static final String SECRET_KEY = "P775YBYWLFQAd9PE";
    }

    //ahmadni @ 12-Jul-2016 : For QP User account type
    public static final class QP_USER_TYPE {

        public static final String CO_ADMIN = "Company Admin";
        public static final String NORMAL_STAFF = "Normal Staff";
    }

    //ahmadni @ 29-june-2016 
    public static final class SYSTEM_CODE {

        public static final String QP = "QP";
        public static final String UTIMAPS = "UTiMAPS";
        public static final String LXG_MAIN = "LXG_MAIN";
    }

    public static final class CHECK_STATUS_CODE {

        public static final String C = "Complete / Correct";
        public static final String I = "Incomplete / Incorrect";
    }

    public static final class RECOMM_STATUS_CODE {

        public static final String R = "Recommend";
        public static final String N = "Not Recommend";
    }

    public static final class DECISION_STATUS_CODE {

        public static final String A = "Approve";
        public static final String N = "Not Approve";
    }

    public static final class DOC_STATUS_CODE {

        public static final String COMPLETE = "101";
        public static final String INCORRECT = "102";
        public static final String NOT_APPLICABLE = "103";
    }

    public static final class QP_EMAIL {
          public static final String QP_APPROVED = "QPAccountActivate";
          public static final String QP_APPROVED_FROM_COMPANY = "QPAccountActivate2";
          public static final String QP_APPROVED_FROM_SIGNUP = "QPAccountActivate3"; //ahmadni @ 5-Apr-2017
          public static final String QP_ACC_REJECTED = "QPAccountRejected";
          public static final String QP_PAYMENT_NEW = "QPUpdatePaymentNew";
          public static final String QP_PAYMENT_RENEW = "QPUpdatePaymentRenew";
          public static final String QP_APP_REJECTED = "QPRejectApplication";
          public static final String QP_APP_RETURN = "QPReturnApplication";//serene @ 22/12/2016
          public static final String QP_CERT_ENDORSED = "QPCertEndorsed";
          public static final String QP_PAYMENT_EXPIRED = "QPPaymentExpired";//ahmadni @ 19-Jan-2017
          public static final String QP_RENEWAL_REMINDER = "QPRenewalReminder";//ahmadni @ 19-Jan-2017
          public static final String QP_PAYMENT_REMINDER = "QPPaymentReminder";//ahmadni @ 19-Jan-2017
          public static final String QP_CARD_PRINTED = "QPCardPrinted";//ahmadni @ 23-Jan-2017
          public static final String QP_RENEW_CERT_RETURN = "QPRenewCertReturn";//ahmadni @ 23-Jan-2018
          
      }
    
    public static String getTermCondsPath() {
//       if (DOMAIN.isLinuxSftp) {
//           return DOMAIN.profileImagePath_linux;
//       }
        return DOMAIN.termsCondsPath;
    }

    public static String getQPRegisterPath() {
//       if (DOMAIN.isLinuxSftp) {
//           return DOMAIN.profileImagePath_linux;
//       }
        return DOMAIN.qpRegisterPath;
    }


    /*
     Public:    
     - Endorsed : After SUT Endorsed (Valid)    
     - Invalid : Expired    
     Internal Use:
     - Draft : For PO Verification
     - Deceased : Cert Holder Deceased
     - Suspended : Cert Holder suspended by SPA
     */
    public static final class CERT_STATUS {
        public static final String DRAFT = "DT"; //Draft 
        public static final String DRAFT_CHECKED = "DC"; //Draft - Checking by PO 
        public static final String DRAFT_VERIFIED = "DV"; //Draft - Verify by PO 
        public static final String RENEW_DRAFT = "RT"; //Renew Draft 
        public static final String RENEW_CHECKED = "RC"; //Renew Draft - Checking by PO
        public static final String RENEW_DRAFT_VERIFIED = "RV"; //Renew Draft - Verify by PO 
        public static final String ENDORSED = "EN";

        //SET BY BACKEND RUN EVERY 1ST JANUARY
        public static final String EXPIRED = "EX";
        public static final String VALID = "VL";

        //SET BY USER
        public static final String BANKRUPT = "BK";
        public static final String SUSPENDED = "SU";
        public static final String DECEASED = "XX";

    }

    public static final class SMSCODE {

        public static final String MAIN_SWITCH = "MS";  // Global Setting: ON: Can Send / OFF: Cannot
        public static final String URL = "URL";
        public static final String FIRST_REMINDER = "RM1";
        public static final String LAST_REMINDER = "RM9";
    }

    public static final class POST2RVS {

        public static final String PAYMENT_TYPE = "C";
        public static final String TRANSACTION_CODE = "250"; // Amended on 16th November 2011
        public static final String KCH_DIV = "01";
    }

    public static final class PAYMENT_METHOD {
        public static final String EBP = "EBP";
        public static final String JVP = "JVP"; //changed by ai min 19/10/2011
        public static final String SPY = "SPY"; //bernard@25-APR-2019 : For Sarawak Pay
        public static final String CSH = "CSH"; //@4.4.2019. Remark: To cater RLL Counter Payment.
        public static final String LNS = "LNS"; //added by Mike 02-08-2020 To cater LNS counter
    }

    public static final class SYSTEM_ID {

        public static final String LXG_MAIN ="C001";  
        public static final String LXG_LAS ="L001";  
        public static final String USJ ="U001";  
    }

    public static String getSetupSignaturePath() {
        return DOMAIN.setupSignaturePath;
    }

    public static String getCardSetupPath() {
        return DOMAIN.cardSetupPath;
    }

    public static final class WF_HARDCODE {

        public static final String SUT_APPROVAL = "A001SUT01";  // ahmadni @ 13-Oct-2016
        public static final String SUT_ENDORSE = "A001SUT02";  // ahmadni @ 13-Oct-2016
        public static final String HOP_ENDORSE = "A001HOP03";  // ahmadni @ 25-Feb-2016
        public static final String SUT_ENDORSE_GAZETTE = "A002SUT01"; // ahmadni @ 6-Dec-2016
    }

    public static String getQPCertMRPEChop() {
        return DOMAIN.qpCertMRPEChop;
    }

    public static String getQPGazettePath() {
        return DOMAIN.qpGazettePath;
    }

    //ahmadni @ 3-Jan-2017
    public static final class JOB_STATUS {

        public static final String NEW = "A";
        public static final String ACTIVE = "B";
    }

    public static String getQPSignSetupPath() {
        return DOMAIN.qpSignSetupPath;
    }

    public static String getQPTempPath() {
        return DOMAIN.qpTempPath;
    }

    public static String getUserManualPath() {
        return DOMAIN.userManual;
    }

    public static final class POLICYSETUP {

        public static final String PWD_EXPIRY = "PasswordExpiry";
        public static final String LAND_SEARCH = "LandSearchFilter";
    }
    
    public static final class FILE_TYPE {
        
        public static final String UPS10 = "UPS10";
        public static final String USJL = "USJL";
        public static final String SISJL = "SISJL";
        public static final String STSJL = "STSJL";
        public static final String USCS10 = "USCS10";
        public static final String USCS10H = "USCS10H";
        public static final String USCS20 = "USCS20";
        public static final String USCS30 = "USCS30";
        public static final String USCS40 = "USCS40";
        public static final String USCS50 = "USCS50";
        public static final String USCS60 = "USCS60";
        public static final String USCS70 = "USCS70";
        public static final String USCS80 = "USCS80";
        public static final String USCS90 = "USCS90";
        public static final String DSP = "DSP";
        public static final String SSDSP = "SSDSP";
        public static final String MINUTE = "MINUTE";
        public static final String DRO = "DRO";
        
        public static final String U10 = "U10";
        public static final String U20 = "U20";
        public static final String U21 = "U21";
        public static final String U30 = "U30";
        public static final String U40 = "U40";
        public static final String U50 = "U50";
        public static final String U60 = "U60";
        
        // for Public side signed Digital Survey Plan 16.07.24
        public static final String PSDSP = "PSDSP";
        
        // Traverse Precheck Reports 26.09.2024
        public static final String TRAVERSE_PC = "TRAV";
    }
    
    public static final class FILE_STATUS {
        public static final String PENDING = "P";
        public static final String YES = "Y";
        public static final String NO = "N";
        public static final String SOFT_DELETED = "D";
    }
    
    //AiMin @4.9.2019. 
     public static final class PAYMENT_OPTION {
        public static final String EBPP = "EBPP";
        public static final String INDIVIDUAL_DEPOSIT_ACC = "I";
        public static final String COMPANY_DEPOSIT_ACC = "C";
        public static final String OFFICIAL_USE = "OU"; //Remark: ELASIS/2017/29.
        public static final String SARAWAK_PAY = "SPY"; //Remark: ELASIS CASHLESS
}
    //AiMin @4.9.2019. 
     public static final class PAYMENT_OPTION_DESC {
         public static final String INDIVIDUAL_BAL_ACC = "Individual Prepayment Account"; //ahmadni 10/10/11 - change balance to prepayment
         public static final String COMPANY_BAL_ACC = "Company Prepayment Account"; //ahmadni 10/10/11
         public static final String DIRECT_DEBIT = "Direct Debit"; //added by ahmadni 13/10/2011
    }
    
    public static final class PAYMENT_CATEGORY{
         public static final String EIS = "EIS";
         public static final String UTIMAPS = "UTIMAPS";
     }
    
     /**
     * Added by AiMin @29 Jul 2019
     * SarawakPay Merchant Key Path
     */
    public static final class SPAY_MERCHANT_KEY {
	//**LIVE**
//        public static final String FILE_PATH = "/home/lxgeis/sarawakpay/";//live
        public static final String FILE_PATH = "/home/utilitysurvey-tnt/sarawakpay/";//tnt
//        public static final String FILE_PATH = "C:\\SarawakPay_RSAKey\\"; // aimin's pc
    } 
    
    /**
     * Added by AiMin @29 Jul 2019
     * SarawakPay Merchant ID for eLasis
     */
    public static final class SPAY_MERCHANT_ID {
	//**LIVE**
//        public static final String ID = "M100006411"; //live
        public static final String ID = "M100000980"; //dev
    }
    
    public static final class SPAY_ORDER_STATUS {
        public static final String Order_Pending = "0";
        public static final String Order_Paid = "1";
        public static final String Failed = "2";
        public static final String Order_Closed = "4";
    }

    public static final class SPAY_RESPONSE_CODE {
        public static final String Order_Not_Exist = "TPB003";
    }
    
    public static final class COUNTER_PAYMENT_CHECK {
        public static final String DEFAULT_USER = "COUNTERPAYCHECK";
    }
           /**
     * Added by ai min @ 12.7.2011
     * For PrepaymentCheck
     */
    public static final class PREPAYMENT_CHECK {

        public static final String DEFAULT_ID = "RVS";
        public static final class BALANCE_STATUS {
            public static final String ACTIVE = "ACT";
            public static final String CANCELLED = "CCL";
            public static final String FREEZED = "FRZ";
        }

        public static final class STAGING_TYPE {
            public static final String FREEZED = "FRZ";
            public static final String TOPUP_CASH = "CSH";
            public static final String TOPUP_CHEQUE = "CHQ";
            public static final String REFUND = "RFD";
            public static final String REVERSED = "REV";
        }

        public static final class TRANS_TYPE {
            public static final String CASH = "CSH";
            public static final String CHEQUE = "CHQ";
            public static final String REFUND = "RFD";
            public static final String STOREFRONT_PYMNT = "SPY";
            public static final String ELODGEMENT_PYMNT = "EPY";
            public static final String RLL_PYMNT = "RLL";
            public static final String USJ_PYMNT = "USJ";
            public static final String MOBILE_PYMNT = "MPY"; //AiMin @28.10.2020
        }

        public static final class STAGING_STATUS {
            public static final String ACTIVE = "A";
            public static final String PENDING = "P";
            public static final String CANCELLED = "C";
            public static final String IN_PROGRESS = "I";
        }
        
        public static final String ACC_TYPE_INDIVIDUAL = "I";
        public static final String ACC_TYPE_COMPANY = "C";
        public static final String ACC_NEW = "NEW";
        public static final String ACC_CURRENT = "CURRENT";
        public static final String DEFAULT_USER = "PREPAYMENTCHECK";

        public static final class TRANSACTION {
            public static final String STAGING = "Staging";
            public static final String STOREFRONT_PYMNT = "StorefrontPayment";
            public static final String EL_PYMNT = "ELPayment";  // thoth
            public static final String TOPUP_EBPP = "TopUpEBPP";
            public static final String TOPUP_SPY = "TopUpSPY"; // Mike 5.9.2020 :: eLASIS Cashless
            public static final String ACC_MAINTENANCE = "AccountMaintenance";
            public static final String RLL_PYMNT = "RLLPayment"; //AiMin @24.11.2017
            public static final String USJ_PYMNT = "USJPayment"; 
            public static final String MOBILE_PYMNT = "MobilePayment"; //AiMin @3.2.2020
        }

        public static final class PAYMENT_OPT {
            public static final String RVS = "RVS";
        }
    }
    
    public static final class TIME_UNIT {
        public static final String times = "times";
        public static final String days = "days";
        public static final String years = "years";
        public static final String seconds = "seconds";
        public static final String minutes = "minutes";
        public static final String hours = "hours";
    }
     
    //Land and Survey Board Email
    public static final String email_landsurveyboard = "";
    
    //Resubmission due period
    public static final int daysDue = 7;
    
    public static final class RVS_TRAN_CODE {
        public static final String RVS_TRANCODE = "308";
        public static final String RVS_SUBCODE = "007";
    }
    
    public static final class SCS_JP3_PUBCODE {
        public static final String ASSIGN_COMP = "200";
        public static final String JOB_REGISTERED = "260";
        public static final String COMPUTATION_APPROVED = "151";
        public static final String ASSIGN_PLAN_EXAMINATION = "204";

    }
}
