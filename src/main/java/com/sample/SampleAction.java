package com.sample;

import com.SysConf;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.HttpsUrlUtil;
import com.sains.common.util.OBSUtil;
import com.sains.common.util.Options;
import com.sains.common.util.SFTPBean;
import com.sains.common.util.SmartXChangeUtil;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseActionSupport_API;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CommonList;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.LogFunction;
import com.sains.framework.base.SessionFactoryImpl;
import com.sains.framework.base.web.DynamicAction;
import com.sains.framework.lookup.DataRetriever;
import com.sains.framework.lookup.ItemChangeAction;
import com.sains.framework.model.DrDocRepoModel;
import com.sains.framework.model.User;
import com.sains.workflow.util.RouteUtil;
//import com.sains.workflow.util.RouteUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.activation.MimetypesFileTypeMap;
import javax.net.ssl.SSLContext;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.WordUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.struts2.ServletActionContext;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SampleAction extends BaseActionSupport{// implements ModelDriven<String> {

    private static final long serialVersionUID = -6659925652584240539L;
    private String useServiceFactory_ = "N";

    public String getUseServiceFactory_() {
        return useServiceFactory_;
    }

    public void setUseServiceFactory_(String useServiceFactory_) {
        this.useServiceFactory_ = useServiceFactory_;
    }


    public SampleAction() {
        // change the DAO to the correct service.
        //baseDAO = new BaseDAOImpl();
        model = new String();
        setInsertUpdateSuccessCode(SystemConstants.ACTION_Status.LOAD_EDIT_PAGE);
        // set the required field for common check.
        //getRequiredParam().put("column_name1", "column.name1");
        //getRequiredParam().put("column_name2", "column.name2");


    }

    public List filterModuleTypeOptions = null;
    public List getFilterModuleTypeOptions() {
        if (filterModuleTypeOptions == null) {
            filterModuleTypeOptions = new ArrayList();
            filterModuleTypeOptions.add(new Options("","No filter - Show all module and submodule"));
            filterModuleTypeOptions.add(new Options("S","Submodule only"));
            filterModuleTypeOptions.add(new Options("M","None Submodule only"));
        }
        return filterModuleTypeOptions;
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

    private String deleteMasterId = null;
    public String getDeleteMasterId() {
        return deleteMasterId;
    }
    public void setDeleteMasterId(String deleteMasterId) {
        this.deleteMasterId = deleteMasterId;
    }
    
    public String deleteMasterDetail() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        request.setAttribute("ignoreCsrfCheck", "true");
        try {
            baseDAO.beginBatchTransaction();
            DetailModel detail = new DetailModel();
            detail.setID("5");
            getDaoService_().auditDeleteWithSession(baseDAO.getSession(), detail, "10", true);
            baseDAO.commitBatchTransaction();
        } catch (Exception e) {
            baseDAO.rollbackBatchTransaction();
        }
        return SUCCESS;
    }
    public String updateMasterDetail() {
        try {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            request.setAttribute("ignoreCsrfCheck", "true");
            //Insert Master - Detail
            BaseDAO dao = baseDAO;
            MasterModel master = (MasterModel)dao.getModelById(7, MasterModel.class);
            master.setMaster_name("777");
            master.setupMyChildList("getDetailList", DetailModel.class, "detailDeleted", Boolean.FALSE);
//            master.setupMyChildList("getDetail3List", DetailModel.class, "detailDeleted", Boolean.FALSE);
            master.getDetailList().get(0).setDetail_desc("777.7");
            getDaoService_().update(master);
            //delete master detail

        } catch (Exception e) {
        }
        return SUCCESS;
    }
    public String insertMasterDetail() {
        try {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            request.setAttribute("ignoreCsrfCheck", "true");
            //Insert Master - Detail
            MasterModel master = new MasterModel();
            master.setMaster_name("UUUU");
            master.setupMyChildList("getDetailList", DetailModel.class, "detailDeleted", Boolean.FALSE);
//            master.setupMyChildList("getDetail4List", DetailModel.class, "detailDeleted", Boolean.FALSE);
//            master.setupMyChildList("getDetail3List", DetailModel.class, "detailDeleted", Boolean.FALSE);
            DetailModel detail = new DetailModel();
            detail.setDetail_desc("ZZZZZ");
            master.getDetailList().add(detail);
            getDaoService_().insert(master);
            
            //delete master detail

        } catch (Exception e) {
        }
        return SUCCESS;
    }
    
    private Integer intData = null;
    public Integer getIntData() {
        return intData;
    }
    public void setIntData(Integer intData) {
        this.intData = intData;
    }
    public void setIntData_str(String intData) {
        try {
            this.intData = Integer.parseInt(intData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJlWYMr8uyoREQZIZat4cKWEOcoiTBdbJiUqgZgCWcw9qka7i2z2FiLu1IPE5LdYDGjKqSQdnc8b5t8MZ5E1YtcCAwEAAQ==";
    
    DynamicAction da = null;
    public DynamicAction getDa() {
        return da;
    }
    
    private Map ismDocApi(String apiType, Map<String, String> paramMap) throws Exception {
        Debug.printFrameworkDebug("trigger push Doc Info to ISM");
        //HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        //HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
//        String sRetStatus="";
        JSONObject oJSonStr = null;
        try {
            
            //SAMPLE RETURN MSG 
            //{"status":"success","message":"Record have been successfully insert.","data":{"rec_id":"eUVmZFlnTkN6RG9JOVlBalFrcm9tcHdNMlhNTitPMFdyTmhIUW1nV0orTT0=","ref_no":"Ref2020000002"}}
                
            String sToken = "6ec848f9c65b89d7a1375429dc210d78";
            String sUrl = null;
            if (apiType.equals("push_doc")) {
                sUrl = "https://servicetraining.sarawak.gov.my/web/web/api/document/insert";
            } else if (apiType.equals("delete_doc")) {
                sUrl = "https://servicetraining.sarawak.gov.my/web/web/api/document/delete";
            } else if (apiType.equals("doc_info")) {
                sUrl = "https://servicetraining.sarawak.gov.my/web/web/api/document/get";
            } else if (apiType.equals("comm_doc_info")) {
                sUrl = "https://servicetraining.sarawak.gov.my/web/web/api/document_user";
            }
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("token", sToken));
            for (String key : paramMap.keySet()) {
                urlParameters.add(new BasicNameValuePair(key, paramMap.get(key)));
            }
//            urlParameters.add(new BasicNameValuePair("datetime", strCurrDateTime));
//            urlParameters.add(new BasicNameValuePair("status", status));

            String strJsonString = HttpsUrlUtil.https_selfSignCallUrlPost(sUrl,urlParameters);

            //if need to keep the return json string. Comment for now
            oJSonStr = (JSONObject) new JSONParser().parse(strJsonString);
            if (oJSonStr.get("status")==null || !((String)oJSonStr.get("status")).equals("success")) {
                throw new CustomBaseException((String)oJSonStr.get("message"));
            }
            System.out.println("returned = " + oJSonStr);
//            sRetStatus = oJSonStr.toJSONString();
                
            
        } catch (BaseException be) {
            throw be;
        } catch (Exception e) {
            System.out.println("e:"+e.getMessage());
            if (e.getMessage().contains("No route to host")) {
                throw new CustomBaseException(e.getMessage());
            }
            new LogFunction().logError(this.getClass(), "", e);
            throw new CustomBaseException("Unknown exception");
        }
        return oJSonStr;
//        return sRetStatus;
    }
    
    public String template() throws Exception {
        return "template";
    }
    public String load() throws Exception {
//        java.util.Date aDate = DateUtil.getDate("2020-12-28", "yyyy-MM-dd");
//        System.out.println("YYYY = " + Formatter.formatDate(aDate, "dd/MM/YYYY"));
//        System.out.println("yyyy = " + Formatter.formatDate(aDate, "dd/MM/yyyy"));
//        SFTPBean bean = new SFTPBean();
//        SFTPBean.fileCount = 0L;
//        SFTPBean.fileSize  = 0L;
//        bean.list(SystemConstants.DOMAIN.ftps_path);
//        bean.disconnect();
//        System.out.println("total file     = " + SFTPBean.fileCount);
//        System.out.println("file size(MB)  = " + SFTPBean.fileSize/1024/1024);
//        com.sains.workflow.util.RouteUtil ru = new com.sains.workflow.util.RouteUtil();
//        ru.startPembinaanMasjidWorkflow("1585625103477FbnHu90", "Public_1_IC", "openpayment_tnt",
//                 "309", "LA20200331_000001", "111", "Majlis Perbandaran Sibu", 
//                 "https://ss-intsrq.tnt.sarawak.gov.my/srq/ssoTriggerSsoAccess?srcId=ISM&svcId=1581908432324H9FFpq0&slacd=SLA223&fmId=1585793321098cfVNsr0", 
//                 "K1UrSXFFL3VhL3IxN1FxbGJJYjc5em5TUVkzbGh4SEhMd29VWG5kaDRPST0=");
//        Map mailParam = new HashMap();
//        mailParam.put(EmailQueueTrigger.MAIL_TO, "Then Sze Wee<thensw@sains.com.my>");
//        mailParam.put("language", "bm");
//        mailParam.put(EmailQueueTrigger.SMS_TO, "0168692323, 1234567890");
//        EmailQueueTrigger emailQueue = new EmailQueueTrigger();
//        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//        request.setAttribute("isFromBackend", "true");
//        emailQueue.sendEMail(mailParam, "PendingGroup");
//        request.removeAttribute("isFromBackend");

//        System.out.println(URLEncoder.encode("<p>Dear Sir/Madam, <p>\n" +
//"Job Pending at group", "UTF-8"));
//        System.out.println("");
//        System.out.println("");
//        System.out.println(URLDecoder.decode("%3Cp%3EDear+Sir%2FMadam%2C+%3Cp%3E%0AJob+Pending+at+group", "UTF-8"));

//        List param = new ArrayList();
//        param.add(new BasicNameValuePair("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTQ1MgV29ya3NwYWNlIiwiYXVkIjoiRUxBMiIsImV4cCI6IjIwMjAtMDUtMDUgMTc6MTE6MDgiLCJjbGllbnQiOiJlbGEyIiwidXNlciI6IjE2MDEyIiwiZ2VuX29uIjoxNTg4NjY4MDY4LjQ5NTgxOCwicmFuZCI6ImxtY3AwIn0.FF9Iy0RRDS_6hRLI2huZvGopT1ejWkN53RuMliP7bZQ"));
//        System.out.println("api = " + HttpsUrlUtil.https_selfSignCallUrlPost("https://ss-intsrq.tnt.sarawak.gov.my/srq/loadScsWasteBinReplcmntTaskList_ScsWrkspcAccess", param));
        
        /*
        Map paramMap = new HashMap();
        paramMap.put("sid", "1234567890"); //swkid/IC
        paramMap.put("doc_code", "Passport"); //left empty if it is not common doc
        paramMap.put("dr_doc_name", "passport.pdf");
        paramMap.put("dr_doc_path", "COMM/Passport");
        paramMap.put("dr_doc_type", "pdf");
        Map returnMap = ismDocApi("push_doc", paramMap);
        
        //dr_doc_id the key generate by ISM API for you to load doc infor
        returnMap.get("dr_doc_id");
        //store file to ism common ftp using dr_doc_path + dr_doc_id:
        // COMM/Passport +"/"+ {dr_doc_id}
        paramMap.put("dr_doc_id", "20200429151233vmN7xtES");//20200429150102vTAF4vCE
        System.out.println("doc_info(20200429151233vmN7xtES) = " + ismDocApi("doc_info", paramMap));

//        paramMap.put("dr_doc_id", "20200429151233vmN7xtES"); 
//        System.out.println("delete_doc(20200429151233vmN7xtES) = " + ismDocApi("delete_doc", paramMap));
        
        paramMap.clear();
        paramMap.put("sid", "1234567890");
        paramMap.put("doc_code", "Passport");
//        paramMap.put("doc_code", "IC");
        returnMap = ismDocApi("comm_doc_info", paramMap);
        List<Map<String, String>> dataList = (List) paramMap.get("data");
        System.out.println("dr_doc_path = " + dataList.get(0).get("dr_doc_path"));
        */
        baseDAO.getModelById("10", com.sains.framework.model.User.class);
        if (showListing) {
            da = new DynamicAction(baseDAO, Boolean.TRUE);
//            da.setAction("Application");//search_application_code
            da.setAction("Module");//search_application_code
            Map manualParam = new HashMap();
            manualParam.put("search_module_code", "a");
            da.setDynamicSortBy("module_code");
            da.setDynamicSortOrder("A");
            da.setManualParamMap(manualParam);
            da.search2();
        }
//        System.out.println("intData " + intData);
        return SUCCESS;
    }
    
    public static Set<String> apiCallSet = new HashSet();
    
    String apiUrl = null;
    public String getApiUrl() {
        return apiUrl;
    }
    
    public String callApi1() throws Exception {
        String callKey = CommonFunction.getId(25);
        String secret = StringEscapeUtils.escapeHtml4(CommonFunction.encrypt(publicKeyStr, "123"));
        apiUrl = "http://localhost:8080/forNewProject/apiCall1Sample?doWhat=insert&callerKey="+callKey
                +"&caller=training3&secretData="+secret;
        Map jsonMap = callUrl(apiUrl);
        System.out.println("API return : " + jsonMap);
        return "API_CALL";
    }
    public String callApi2() throws Exception {
        String callKey = CommonFunction.getId(25);
        apiCallSet.add(callKey); /// keep the call api key (if using LB, should save the call key to DB)
        String secret = StringEscapeUtils.escapeHtml4(CommonFunction.encrypt(publicKeyStr, "123"));
        apiUrl = "http://localhost:8080/forNewProject/apiCall2Sample?doWhat=insert&callerKey="+callKey
                +"&caller=training3&secretData="+secret;
        Map jsonMap = callUrl(apiUrl);
        System.out.println("API return : " + jsonMap);
//        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
//        response.setHeader("Content-Security-Policy", "default-src 'self' 'unsafe-inline' 'unsafe-eval'; script-src 'self' ");
        return "API_CALL";
    }
    
    public String callApi3() throws Exception {
        String callKey = CommonFunction.getId(25);
        String secret = StringEscapeUtils.escapeHtml4(CommonFunction.encrypt(publicKeyStr, "123"));
        apiUrl = "http://localhost:8080/forNewProject/apiCall2Sample?doWhat=insert&callerKey="+callKey
                +"&caller=training3&secretData="+secret;
        Map jsonMap = callUrl(apiUrl);
        System.out.println("API return : " + jsonMap);
//        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
//        response.setHeader("Content-Security-Policy", "default-src 'self' 'unsafe-inline' 'unsafe-eval'; script-src 'self' ");
        return "API_CALL";
    }
    
    String apiReturnStr = null;
    public String getApiReturnStr() {
        return apiReturnStr;
    }
    
    public Map callUrl(String theUrl) throws Exception {
        URL url = new URL(theUrl);  
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(20000);  // 20 seconds
        StringBuilder sb = new StringBuilder();
        InputStreamReader stream = new InputStreamReader(conn.getInputStream());
        BufferedReader rd = new BufferedReader(stream);
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line).append('\n');
        }
        Map map = new Gson().fromJson(sb.toString() , new TypeToken<HashMap<String, Object>>() {}.getType() );
        apiReturnStr = map.toString();
        return map;
    }
    
    private String validateCallKey = null;
    public String getValidateCallKey() {
        return validateCallKey;
    }
    public void setValidateCallKey(String validateCallKey) {
        this.validateCallKey = validateCallKey;
    }
    
    public String validateMyCall() throws Exception {
        Map jsonMap = new HashMap();
        jsonMap.put("status", "fail");
        jsonMap.put("msg", "No, i don't call u");
        if (!Validator.isEmpty(validateCallKey)) {
            if (apiCallSet.remove(validateCallKey)) {
                jsonMap.put("status", "success");
                jsonMap.put("msg", "yes, i called your API");
            }
        }
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.setContentType("application/json");
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
        
        return null;
    }
    
    private String secretData = null;
    public String getSecretData() {
        return secretData;
    }
    public void setSecretData(String secretData) {
        secretData=secretData.replaceAll(" ", "+");
        this.secretData = secretData;
    }

    private String caller = null;
    public String getCaller() {
        return caller;
    }
    public void setCaller(String caller) {
        this.caller = caller;
    }

    private String callerKey = null;
    public String getCallerKey() {
        return callerKey;
    }
    public void setCallerKey(String callerKey) {
        this.callerKey = callerKey;
    }

    private String doWhat = null;
    public String getDoWhat() {
        return doWhat;
    }
    public void setDoWhat(String doWhat) {
        this.doWhat = doWhat;
    }
    
    private final String base64PrivateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAmVZgyvy7KhERBkhlq3hwpYQ5yiJMF1smJSqBmAJZzD2qRruLbPYWIu7Ug8Tkt1gMaMqpJB2dzxvm3wxnkTVi1wIDAQABAkBbpXbgm8E52U11ldu+apPZOXhYybr00rOvzwH52p3VJa6ySD+68WNpTJJeFFZ1C4VU1IS01djxUZIq2TsZTOWRAiEA5Ylawg3oiwedROqmBVvwrI5r/gp2MYwfDAaJKG9dxBsCIQCrBAxLSqDak0X7ZG0FmCwOAwJcxMC0QBh6TIYVWmNv9QIgE+d0Qk2Yc63mWlqKqP5PAXecL7xSgXtoGc/bRvFZTS8CIG2E0CK/jjesLRsLe04j38KxZqfmUMqeaY6Yxx2RnzCpAiEAhxx1kNCDZU6uZH5ReL6/i+yAy5+sgr7Wvr0lh3Whr2Q=";
    public String apiCall1() throws Exception {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Map jsonMap = new HashMap();
        String secret = CommonFunction.decrypt(base64PrivateKey, secretData);
        if (secret.equals("123")) {
            jsonMap.put("status", "success");
            jsonMap.put("actionDone", doWhat);
            jsonMap.put("yourSecret", secret);
        } else {
            jsonMap.put("status", "fail");
            jsonMap.put("msg", "invalid secret key");
        }
        response.setContentType("application/json");
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
        return null;
    }
    public String apiCall2() throws Exception {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Map jsonMap = new HashMap();
        String secret = CommonFunction.decrypt(base64PrivateKey, secretData);
        if (secret.equals("123")) {
            Map<String, String> validationMap = validateCaller();
            if (validationMap.get("status").equalsIgnoreCase("success")) {
                jsonMap.put("status", "success");
                jsonMap.put("actionDone", "validated you called me, i had done " + doWhat);
                jsonMap.put("yourSecret", CommonFunction.decrypt(base64PrivateKey, secretData));
            } 
            else {
                jsonMap.put("status", "fail");
                jsonMap.put("msg", "Don't play play, I know what you did last summer");
            }
        } else {
            jsonMap.put("status", "fail");
            jsonMap.put("msg", "invalid secret key");
        }
        response.setContentType("application/json");
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
        return null;
    }
    
    public Map validateCaller() throws Exception {
        String theUrl = "http://localhost:8080/forNewProject/validateMyCallSample?validateCallKey="+StringEscapeUtils.escapeHtml4(callerKey);
                
        URL url = new URL(theUrl);  
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(20000);  // 20 seconds
        StringBuilder sb = new StringBuilder();
        InputStreamReader stream = new InputStreamReader(conn.getInputStream());
        BufferedReader rd = new BufferedReader(stream);
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line).append('\n');
        }
//                return sb.toString();
        return new Gson().fromJson(sb.toString() , new TypeToken<HashMap<String, Object>>() {}.getType() );
    }
    
    public List getSampleList() {
        List list = new ArrayList();
        list.add(new Options("1", "1"));
        list.add(new Options("2", "2"));
        list.add(new Options("3", "3"));
        list.add(new Options("4", "4"));
        return list;
    }
    public List getDivisionList() {
        List list = new ArrayList();
        list.add(new Options("", getText("pleaseSelect")));
        list.add(new Options("1", "Kuching"));
        list.add(new Options("2", "Sri Aman"));
        list.add(new Options("3", "Sibu"));
        return list;
    }
    public List getDistrictList() throws Exception {
        ItemChangeAction icAction = new ItemChangeAction();
        try {
            icAction.setItemCate("DivDis");
            icAction.setItemValue(getDivValue());
//            icAction.setOriValue(getDisValue());
            icAction.itemChange();
        } finally {
            icAction.closeSession();
        }
        return icAction.getItemChangeList();
    }
    
    public String getDivValue(){
        return "1";
    }
    public String getDisValue(){
        return "2";
    }
    
    //**workflow sample : START**//
//    public String startLeaveApplication() {
//        RouteUtil routeUtil = new RouteUtil();
//        try {
//            Map returnMap = routeUtil.startLeaveApplication("12", "lian", "01", "N");
//            System.out.println("workflow return = " + returnMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return SUCCESS;
//    }
//    public String supervisorApprove() {
//        RouteUtil routeUtil = new RouteUtil();
//        try {
//            Map returnMap = routeUtil.completeTaskByActivityCode("12", "LeaveApp_001", "_completeValue::=Y", "status::=A");
//            System.out.println("approval process return = " + returnMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return SUCCESS;
//    }
    //**workflow sample : END**//
    
    public List<Options> itemList = new ArrayList();
    public List<Options> getItemList() {
        return itemList;
    }
    public void setItemList(List<Options> itemList) {
        this.itemList = itemList;
    }
    
    public String getCurrentDate_str() {
        return Formatter.formatDate(DateUtil.getCurrentTimestamp(), "EEEE, dd MMMM yyyy h:m:s a");
    }
    
    private Integer addItemRefreshScroll = -1;

    public Integer getAddItemRefreshScroll() {
        return addItemRefreshScroll;
    }
    
    public String addItemRefreshScrollTo() {
        getItemList().add(new Options("Item "+(getItemList().size() + 1), "") );
        addItemRefreshScroll = getItemList().size() - 1;
        return SUCCESS;
    }
    public String addItemRefresh() {
        getItemList().add(new Options("Item "+(getItemList().size() + 1), "") );
        return SUCCESS;
    }
    public String addItem() {
        noDecoPage = "/sample/itemList.jsp";
        for (Options op : getItemList()) {
            System.out.println("op.getKeyData = " + op.getKeyData());
        }
        getItemList().add(new Options("Item "+(getItemList().size() + 1), "") );
        return "addItem";
    }
    
    private String noDecoPage = null;
    public String getNoDecoPage() {
        return noDecoPage;
    }
    
    public String testQuery() {
        SQLQuery query = baseDAO.getSession().createSQLQuery("select application_name, Application_code from t_setup_application");
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List<Map<String, Object>> list = query.list();
        for (String key : list.get(0).keySet()) {
            System.out.println("key = " + key);
        }
//        query.setResultTransformer(ToListResultTransformer.INSTANCE);
//        for (Object obj : query.list()) {
//            System.out.println("obj = " + obj.getClass());
//            System.out.println("string = " + obj.getClass().toGenericString());
//            System.out.println("1 = " + ((List)obj).get(0) )  ;
////            Object[] objArr = (Object[])obj;
////            System.out.println("objArr[0] = " + objArr[0]);
////            System.out.println("objArr[0].class = " + objArr[0].getClass());
//        }
        
        return SUCCESS;
    }
    
    public String https_selfSign()  {
//        String theUrl = "https://servicetraining.sarawak.gov.my/web/web/api/payment_history/?agency=MAJLIS+PERBANDARAN+PADAWAN&bill_type=Assessment+Bill&bill_ref_no=2234539&bill_amt=37.20&sid=A123456&pymt_channel=epaynow&pymt_datetime=2005-08-12+11:30:05&pymt_trans_no=U19BJYY0100011&object_id=1574150327537CXZSpn0&applicant_name=Test&token=08e277d4a428cf0e7e1e7a7ae9d87297";
//        String theUrl = "https://10.18.4.65/projects/appsuite/branches/dash/api/request_token?client_id=sifbas&secret=ssds7654x4z56iqmcal3ssdszqkc9t160820193d&ldap_id=anthonyckk";
//        String theUrl = "https://www.google.com";

        String theUrl = "http://localhost/workflow/WfService_getWorkflowEstimation";
        Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List params = new ArrayList();
        
        params.add("wf_system::=ISM"); 
        params.add("by_caseId::=1585625103477FbnHu90"); 
//        params.add("data1::=data1");
//        params.add("data2::=data2");
//        params.add("data3::=data3");
        try {
            try {
                SSLContext sslContext = new SSLContextBuilder()
                        .loadTrustMaterial(null, (certificate, authType) -> true).build();
                org.apache.http.impl.client.CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
                        .build();
//                HttpGet httpGet = new HttpGet(theUrl);
//                CloseableHttpResponse resp = httpClient.execute(httpGet);
                HttpPost httpPost= new HttpPost(theUrl);
                List<NameValuePair> arguments = new ArrayList();
                Map map = new HashMap();
                map.put("params", params);
                arguments.add(new BasicNameValuePair("jsonData", gson.toJson(map)));
                httpPost.setEntity(new UrlEncodedFormEntity(arguments));
                CloseableHttpResponse resp = httpClient.execute(httpPost);
                String result;
                String overall="";
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(resp.getEntity().getContent()));
                while ((result = br.readLine()) != null){
                    overall = overall + "\n" + result;
                }
                System.out.println("overall = " + overall);
                resp.close();
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                throw e;
            }
            addActionMessage("https_selfSign() no error");
        } catch (Exception e) {
            addActionError("https_selfSign() error");
            e.printStackTrace();
        }
        return SUCCESS;
    }
    
    public String noOfBit;
    public Integer getNoOfBit_int() {
        try {
            return Integer.parseInt(getNoOfBit());
        } catch (Exception e) {
            return 512;
        }
    }
    public String getNoOfBit() {
        return noOfBit;
    }
    public void setNoOfBit(String noOfBit) {
        this.noOfBit = noOfBit;
    }
    
    public String newKeyPair(){
        try {
            KeyPair keyPair = CommonFunction.generateKeyPair(getNoOfBit_int());
            r = "rmsg;New Key generated,publicKey;"+Base64.encodeBase64String(keyPair.getPublic().getEncoded())+",privateKey;"+Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
        } catch (Exception e) {
            r = "rmsg;Fail to generate new key pair";
        }
        return "defaultDivSubmitForm";
    }
    public String encryption() {
        return "encryption";
    }
    
    //** Generate Entry Page from Model : START **//
    private String theApplicationCode_ = null;
    public String getTheApplicationCode_() {
        return theApplicationCode_;
    }
    public void setTheApplicationCode_(String theApplicationCode_) {
        this.theApplicationCode_ = theApplicationCode_;
    }
    
    private String filePath = null;
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    private static final String packagePath = "src\\main\\resource\\";
    private Map<String, Map> selectedClassMap = null;
    private Map<String, String> selectClassModelMap = new HashMap();
    HttpServletRequest globalRequest = null;
    public String create() {
        globalRequest = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        System.out.println("selectedClass = " + selectedClass);
        selectedClassMap = new TreeMap();
        Map allMap = new HashMap();
        selectClassModelMap = new HashMap();
//        entryModels = new String[]{"model;ParentModel", "model.getList1;List1Model", "model.getList2;List2Model", "model.getList1.getList1_1;List1_1Model"};
        System.out.println("sout filePath =  " + filePath);
        if (filePath == null) {
            filePath = "C:\\Projects\\forNewProject\\";
        } 
        if (!filePath.endsWith("\\")) {
            filePath += "\\";
        }
        
        for (String selectedClassSetup : entryModels) {
            int count = 0;
            String mapKey = null;
            String[] selectedClassSetupArr = selectedClassSetup.split(";");
            Map currentMap = null;
            for (String setup : selectedClassSetupArr[0].split("\\.")) {
                if (count++ == 0) {
                    mapKey = setup;
                    if (!allMap.containsKey(setup)) {
                        currentMap = new TreeMap();
                        selectedClassMap.put(setup, currentMap);
                        selectClassModelMap.put(setup, selectedClassSetupArr[1]);
                        allMap.put(setup, currentMap);
                    } else {
                        currentMap = (Map)allMap.get(setup);
                    }
                } else {
                    if (!currentMap.containsKey(mapKey+"."+setup)) {
                        selectClassModelMap.put(mapKey+"."+setup, selectedClassSetupArr[1]);
                        currentMap.put(mapKey+"."+setup, new TreeMap());
                        allMap.put(mapKey+"."+setup, new TreeMap());
                    } else {
                        currentMap = (Map)currentMap.get(mapKey+"."+setup);
                    }
                    mapKey += "."+setup;
                }
            }
        }
        System.out.println("selectedClassMap = " + selectedClassMap);
        System.out.println("selectClassModelMap = " + selectClassModelMap);
        StringBuilder packageStr = new StringBuilder();
        String newLine = "\r\n";
        String addedStartEnd = "#Added @"+ Formatter.formatDate(DateUtil.getCurrentDate(), "yyyyMMMdd") +" : "+ theApplicationCode_ +" : ";
        packageStr.append(newLine).append(newLine).append(addedStartEnd).append("START").append(newLine);
//        writeFile(filePath+packagePath, "package.properties", addedStartEnd+"START");
        for (String key : selectedClassMap.keySet()) {
            Map map = selectedClassMap.get(key);
            String classModel = selectClassModelMap.get(key);
            System.out.println("");
        }
        htmlBuilder = new StringBuilder();
        htmlBuilder_th = new StringBuilder();
        loopSelectedClassMap(selectedClassMap, 0, null);
        
//        writeFile(filePath+packagePath, "package.properties", addedStartEnd+"END");
        return "generate";
    }
    
    private StringBuilder htmlBuilder_th = null;
    private StringBuilder htmlBuilder = null;
    private StringBuilder finalHtmlBuilder = null;
    public StringBuilder getHtmlBuilder() {
        return htmlBuilder;
    }
    public void setHtmlBuilder(StringBuilder htmlBuilder) {
        this.htmlBuilder = htmlBuilder;
    }
    
    
    private void loopSelectedClassMap(Map<String, Map> loopingMap, Integer level, String levelSetup) {
        Integer subCount = -1;
        System.out.println("starting.levelSetup = " + levelSetup);
        System.out.println("loopingMap.size() = " + loopingMap.size());
        String nextLevel = null;
        Integer td_count = 0;
        for (String key : loopingMap.keySet()) {
            String hiddenPK = "<s:hidden id=\""+key+".ID\" name=\""+key+".ID\" />";
            Boolean startBuild = Boolean.FALSE;
            subCount++;
            if (levelSetup ==null) {
                nextLevel = "";
            } else {
                if (Validator.isEmpty(levelSetup)) {
                    nextLevel = ""+subCount;
                } else {
                    nextLevel = levelSetup+"_"+subCount;
                }
            }
            System.out.println("nextLevel = " + nextLevel);
            selectedClass = selectClassModelMap.get(key);
            Class modelClass = getModelClass();
            for(Method method : modelClass.getDeclaredMethods()){
                if (!method.getName().startsWith("get")) continue;
                Annotation[] annotations = method.getAnnotations();
                Boolean isPK = Boolean.FALSE;
                String name = null;
                for (int i = 0; i < annotations.length; i++) {
                    if (annotations[i].annotationType().equals(Id.class)) {
                        isPK = Boolean.TRUE;
                    }
                    if (annotations[i].annotationType().equals(Column.class)) {
                        name = ((Column)annotations[i]).name();
                    }
                }
                if (name != null) {
                    String labelName = name;

                    if (name.equalsIgnoreCase("created_date") || name.equalsIgnoreCase("created_by") ||
                        name.equalsIgnoreCase("updated_date") || name.equalsIgnoreCase("updated_by")) {
                        continue; //do not generate the input fields for these columns
                    }
                    if (method.getGenericReturnType().toString().equals("class java.lang.String")) {
                    } else if (method.getGenericReturnType().toString().equals("class java.lang.Double")) {
                        name += "_str";
                    } else if (method.getGenericReturnType().toString().equals("class java.lang.Long")) {
                        name += "_str";
                    } else if (method.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                        name += "_str";
                    } else if (method.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
                        name += "_str";
                    }
                    if (!isPK) {
                        System.out.println("levelSetup =  "+nextLevel+ ", " +key+"."+name+"_packageName"+ " = " + globalRequest.getParameter(key+"."+name+"_packageName"));
                        if (Validator.isEmpty(nextLevel)) { //first level (model/theModel)
                            if (!startBuild) {
                                startBuild = Boolean.TRUE;
                            }
                            if (hiddenPK != null) {
                                htmlBuilder.append("\r\n").append(hiddenPK);
                                hiddenPK = null;
                            }
                            if (globalRequest.getParameter(key+"."+name+"_type").equals("tf")) {
                                appendTextField(key+"."+name, key+"."+lastFieldName(name), Boolean.TRUE);
                            }
                        } else { //not main level
                            if (!startBuild) {
                                System.out.println("not main - not start build");
                                startBuild = Boolean.TRUE;
                                htmlBuilder.append("<tr>\n");
                            } else {
                                System.out.println("not main - start build");
                                hiddenPK = null;
                            }
                            htmlBuilder_th.append("<th><s:text name=\"").append(globalRequest.getParameter(key+"."+name+"_packageName")).append("\"/></th>");
                            if (globalRequest.getParameter(key+"."+name+"_type").equals("tf")) {
                                td_count++;
                                appendTextField_td(key+"."+name, "#var_"+nextLevel+"."+lastFieldName(name), Boolean.TRUE, hiddenPK);
                            }
                        }
                    }
                }
            }
            if (finalHtmlBuilder == null) {
                finalHtmlBuilder = new StringBuilder();
            }
            if (!Validator.isEmpty(nextLevel)) {
                finalHtmlBuilder.append("<table><tr>\r\n").append(htmlBuilder_th).append("</tr>").append("\r\n").append(htmlBuilder).append("\r\n").append("</tr></table>\r\n");
            } else {
                finalHtmlBuilder.append(htmlBuilder).append("\r\n");
            }
            finalHtmlBuilder.append("\r\n\r\n");
            htmlBuilder.setLength(0);
            htmlBuilder_th.setLength(0);
            loopSelectedClassMap(loopingMap.get(key), level+1, nextLevel);
        }
        System.out.println("finalHtmlBuilder = " + finalHtmlBuilder);
    }
    
    private String removeGet(String methodName) {
        String firstChar = methodName.substring(3, 4);
        methodName = methodName.substring(4);
        return firstChar.toLowerCase()+methodName;
    }
    private String lastFieldName(String fullName) {
        return fullName.substring(fullName.lastIndexOf(".")+1);
    }
    
    private void appendTextField_td(String fieldName, String valueVar, Boolean req, String hiddenPK) {
//        String label = globalRequest.getParameter(fieldName+"_packageName");
        htmlBuilder.append(
"                    <td>\n"+
(hiddenPK==null?"":"                       "+hiddenPK+"\n")+
"                       <s:textfield id='"+fieldName+"' cssClass=\"form-control\" name=\""+fieldName+"\" value=\"%{"+valueVar+"}\" "+(req?"required=\"required\"":"")+"/>\n" +
"                    </td>\r\n");
    }
    private void appendTextField(String fieldName, String valueVar, Boolean req) {
        String label = globalRequest.getParameter(fieldName+"_packageName");
        htmlBuilder.append(
"                    <div class=\"form-horizontal form-group\">\n" +
"                        <label class=\"col-md-4 control-label\"><s:text name=\""+label+"\" /> "+ (req?"<font class=\"asterisk\">*</font></label>":"") +"\n" +
"                        <div class=\"col-md-5\">\n" +
"                            <s:textfield id=\""+fieldName+"\" cssClass=\"form-control\" name=\""+fieldName+"\" value=\"%{"+valueVar+"}\" "+(req?"required=\"required\"":"")+"/>\n" +
"                        </div>\n" +
"                    </div>\r\n");
    }
    
    public String generate() {
        return "generate";
    }
    public static List registeredClassList = null;
    public List getRegisteredClassList() {
        if (registeredClassList == null) {
            registeredClassList = new ArrayList();
            if (SessionFactoryImpl.registeredClass == null) {
                baseDAO.getSession();
            }
            for (Object obj : SessionFactoryImpl.registeredClass) {
                registeredClassList.add(new Options(((Class)obj).getSimpleName(), ((Class)obj).getSimpleName()));
            }
            CommonList.addBlankOption(registeredClassList, "", getText("pleaseSelect"));
        }
        return registeredClassList;
    }
    
    private String selectedClass = null;
    public String getSelectedClass() {
        return selectedClass;
    }
    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }
    
    private Class getModelClass() {
        if (SessionFactoryImpl.registeredClass == null) {
            baseDAO.getSession();
        }
        for (Object obj : SessionFactoryImpl.registeredClass) {
            if (((Class)obj).getSimpleName().equals(selectedClass)) {
                return (Class)obj;
            }
        }
        return null;
    }
    public Integer childLevel = 1;
    public Integer getChildLevel() {
        return childLevel;
    }
    public void setChildLevel(Integer childLevel) {
        this.childLevel = childLevel;
    }
    
    private String parentModelClass = "theModel";
    public String getParentModelClass() {
        return parentModelClass;
    }
    public void setParentModelClass(String parentModelClass) {
        this.parentModelClass = parentModelClass;
    }
    
    private String parentModelName = "theModel";
    public String getParentModelName() {
        return parentModelName;
    }
    public void setParentModelName(String parentModelName) {
        this.parentModelName = parentModelName;
    }

    private String mainModelVarName = "model";
    public String getMainModelVarName() {
        return mainModelVarName;
    }
    public void setMainModelVarName(String mainModelVarName) {
        this.mainModelVarName = mainModelVarName;
    }
    
    
    private String childModelName = null;
    public String getChildModelName() {
        return childModelName;
    }
    public void setChildModelName(String childModelName) {
        this.childModelName = childModelName;
    }
    
    public String addChildList() throws Exception {
        modelChanged();
        return null;
    }
    
    private String[] entryModels = null;
    public String[] getEntryModels() {
        return entryModels;
    }
    public void setEntryModels(String[] entryModels) {
        this.entryModels = entryModels;
    }
    
    private String whiteSpace = null;
    private String extraTd = null;
    private String extraTh = null;
    private Integer colSpan = 4;
    public String modelChanged() throws Exception {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Class modelClass = getModelClass();
//        if (parentModelName.equals("theModel") && !Validator.isEmpty(mainModelVarName)) {
//            parentModelName = mainModelVarName;
//        }
        if (parentModelName == null) parentModelName = "theModel";
        if (parentModelName.split("\\.").length > 0) {
            for (String split : parentModelName.split("\\.")) {
                if(extraTd ==null) {
                    extraTd = "";
                    extraTh = "";
                    whiteSpace = "";
                } else {
                    colSpan++;
                    extraTd += "<td/>";
                    extraTh += "<th width='20px'/>";
                    whiteSpace += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                }
            }
        }
        String html = "<input type=\"hidden\" name=\"entryModels\" value=\""+parentModelName+";"+modelClass.getSimpleName()+"\">";
        List<Options> oneToManyList = new ArrayList();
        for(Method method : modelClass.getDeclaredMethods()){
            if (!method.getName().startsWith("get")) continue;
            Annotation[] annotations = method.getAnnotations();
            Boolean isPK = Boolean.FALSE;
            String name = null;
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i].annotationType().equals(Id.class)) {
                    isPK = Boolean.TRUE;
                }
                if (annotations[i].annotationType().equals(Column.class)) {
                    name = ((Column)annotations[i]).name();
                }
                if (annotations[i].annotationType().equals(OneToMany.class)) {
                    oneToManyList.add(new Options(parentModelName+"."+removeGet(method.getName()), ((OneToMany)annotations[i]).targetEntity().getSimpleName()));
                }
            }
            if (name != null) {
                String labelName = name;
                
                if (name.equalsIgnoreCase("created_date") || name.equalsIgnoreCase("created_by") ||
                    name.equalsIgnoreCase("updated_date") || name.equalsIgnoreCase("updated_by")) {
                    continue; //do not generate the input fields for these columns
                }
                if (method.getGenericReturnType().toString().equals("class java.lang.String")) {
                } else if (method.getGenericReturnType().toString().equals("class java.lang.Double")) {
                    name += "_str";
                } else if (method.getGenericReturnType().toString().equals("class java.lang.Long")) {
                    name += "_str";
                } else if (method.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                    name += "_str";
                } else if (method.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
                    name += "_str";
                }
                if (isPK) {
                    html = getHtmlInput("ID", parentModelName+".ID", modelClass.getSimpleName(), Boolean.TRUE) + html;
                } else {
                    html += getHtmlInput(labelName, parentModelName+"."+name, modelClass.getSimpleName(), Boolean.FALSE);
                }
            }
        }
        if (!oneToManyList.isEmpty()) {
            for (Options option : oneToManyList) {
                html += "<tr>" + 
                        "   <td id=\""+option.getValueData()+"\" colspan='"+(colSpan)+"'>" +
                        "       "+whiteSpace+"<input type=\"button\" onclick=\"addList('"+ option.getKeyData() +"', '"+modelClass.getSimpleName()+"', '"+option.getValueData()+"')\" value='Add "+ option.getValueData() +" List'>\r\n" + 
                        "   </td>" +
                        "</tr>";
//                html += "<div id=\""+option.getValueData()+"\" class=\"form-horizontal form-group\">\r\n" +
//                        "   <label class=\"col-md-4 control-label\">"+option.getValueData()+"</label>\r\n" +
//                        "   <div class=\"col-md-5\">\r\n" +
//                        "       <input type=\"button\" onclick=\"addList('"+ option.getKeyData() +"', '"+modelClass.getSimpleName()+"', '"+option.getValueData()+"')\" value='Add List'>\r\n" +
//                        "   </div>\r\n" +
//                        "</div>\r\n";
            }
        }
        html = "<table width=\"100%\">\r\n" + 
               "<tr>"+extraTh+"<th>Package Text</th><th>Package Label</th><th>Field Type</th><th>Field Setup</th></tr>" + 
                html + "\r\n</table>\r\n";
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().append(html);
        response.flushBuffer();
        return null;
    }
    private String getHtmlInput(String labelName, String inputName, String modelName, Boolean isHidden) {
        modelName = modelName.replace("Model", "");
        if (labelName==null) {
            labelName = inputName;
        }
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String tfChecked="", ddChecked="", cbChecked="", rdChecked="";
        String tfSelected="", ddSelected="", cbSelected="", rdSelected="", igSelected="", hdSelected="";
        if (request.getParameter(inputName+"_type") == null) {
            tfChecked="checked=\"checked\"";
            tfSelected="selected";
        } else {
            if (request.getParameter(inputName+"_type").equalsIgnoreCase("dd")) {
                ddChecked="checked";
                ddSelected="selected";
            } else if (request.getParameter(inputName+"_type").equalsIgnoreCase("cb")) {
                cbChecked="checked";
                cbSelected="selected";
            } else if (request.getParameter(inputName+"_type").equalsIgnoreCase("rd")) {
                rdChecked="checked";
                rdSelected="selected";
            } else if (request.getParameter(inputName+"_type").equalsIgnoreCase("ig")) {
                igSelected="selected";
            } else if (request.getParameter(inputName+"_type").equalsIgnoreCase("hd")) {
                hdSelected="selected";
            }
        }
        String packageLabel= WordUtils.capitalize(labelName.replace("_", " "));
        return  "<tr>\r\n" + extraTd + "\r\n" + 
                "   <td><input type=\"text\" name=\""+inputName+"_packageName\" class=\"form-control\" value=\""+modelName+"."+labelName+"\"></td>\r\n" + 
                "   <td><input type=\"text\" name=\""+inputName+"_label\" class=\"form-control\" value=\""+packageLabel+"\"></td>\r\n" + 
                "   <td>\r\n" + 
                "       <select name=\""+ inputName +"_type\" id=\""+ inputName +"_type\" class=\"form-control\">\r\n" +
                "           <option "+tfSelected+" value=\"tf\">Textfield</option>\r\n" + 
                "           <option "+ddSelected+" value=\"dd\">DropDown</option>\r\n" + 
                "           <option "+cbSelected+" value=\"cb\">Checkbox</option>\r\n" + 
                "           <option "+rdSelected+" value=\"rd\">Radio</option>\r\n" + 
                "           <option "+hdSelected+" value=\"hd\">Hidden</option>\r\n" + 
                "           <option "+igSelected+" value=\"ig\">Ignore This</option>\r\n" + 
                "   </td>\r\n" + 
                "   <td><input type=\"text\" name=\""+inputName+"_setup\" class=\"form-control\" value=\"\"></td>\r\n" + 
                "</tr>";
//                "<tr><td colspan=\"4\" id=\""+inputName+"_id\">" +
//                " <div class=\"form-horizontal form-group\">\n" +
//"                        <label class=\"col-md-4 control-label\">Input <font class=\"asterisk\">*</font></label>\n" +
//"                        <div class=\"col-md-5\">\n" +
//"                            <input type=\"text\" id='inputId' class=\"form-control\" name=\"input\" value=\"\" required/>\n" +
//"                        </div>\n" +
//"                    </div>"
//                + "<div class=\"form-horizontal form-group\">\n" +
//"                        <label class=\"col-md-4 control-label\">Input <font class=\"asterisk\">*</font></label>\n" +
//"                        <div class=\"col-md-5\">\n" +
//"                            <input type=\"text\" id='inputId' class=\"form-control\" name=\"input\" value=\"\" required/>\n" +
//"                        </div>\n" +
//"                    </div>" + 
//                "</td></tr>";
        
//                return "<div class=\"form-horizontal form-group\">\r\n" +
//"   <label class=\"col-md-4 control-label\">"+modelName+"."+labelName+"</label>\r\n" +
//"   <div class=\"col-md-5\">\r\n" +
//"       Label\r\n" +
//"       <input type=\"text\" name=\""+inputName+"_label\" value=\""+packageLabel+"\">\r\n" +
//"       Setup\r\n" +
//"       <input type=\"text\" name=\""+inputName+"_setup\" value=\"\">\r\n" +
//"       <div class=\"col-md-5 radio radio-inline radio-success\">\n" +
//"           <input "+tfChecked+" value=\"tf\" name=\""+inputName+"_type\" id=\""+inputName+"tf\" type=\"radio\">\n" +
//"           <label for=\""+inputName+"tf\">Textfield</label>\n" +
//"           <input "+ddChecked+" value=\"dd\" name=\""+inputName+"_type\" id=\""+inputName+"dd\" type=\"radio\">\n" +
//"           <label for=\""+inputName+"dd\">Dropdown</label>\n" +
//"           <input "+cbChecked+" value=\"cb\" name=\""+inputName+"_type\" id=\""+inputName+"cb\" type=\"radio\">\n" +
//"           <label for=\""+inputName+"cb\">Checkbox</label>\n" +
//"           <input "+rdChecked+" value=\"rd\" name=\""+inputName+"_type\" id=\""+inputName+"rd\" type=\"radio\">\n" +
//"           <label for=\""+inputName+"rd\">Radio</label>\n" +
//"           <input value=\"ig\" name=\""+inputName+"_type\" id=\""+inputName+"ig\" type=\"radio\">\n" +
//"           <label for=\""+inputName+"ig\">Ignore</label>\n" +
//"       </div>\r\n" +
//"   </div>\r\n" +
//"</div>\r\n";
                
//        return "<div class=\"form-horizontal form-group\">\r\n" +
//"   <label class=\"col-md-4 control-label\"><s:text name=\""+modelName+"."+inputName+"\"/></label>\r\n" +
//"   <div class=\"col-md-5\">\r\n" +
//"       <s:"+(isHidden?"hidden":"textfield")+" name=\""+inputName+"\" cssClass=\"form-control\" value=\"%{"+inputName+"}\"/>\r\n" +
//    "       </div>\r\n" +
//"</div>\r\n";
    }
    public static void writeFile(String filePath, String fileName, String data) {
        try {
            String enterChar = "\r\n";
            //File dir = new File(logPath + fileName + ".log");
            File dir = new File(filePath);

            //create directory if not exist
            if (dir.exists() == false) {
                dir.mkdirs();
                dir.createNewFile();
                enterChar = "";
            }
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(filePath + fileName, true));
            writer.write(enterChar + data);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private ParentModel theModel = null;
    public ParentModel getTheModel() {
        return theModel;
    }
    public String viewGenerated() {
        theModel = new ParentModel();
        ChildModel child = new ChildModel();
        child.getGrandChildList().add(new GrandChildModel());
        theModel.getChildList().add(child);
        return "viewGenerated";
    }
    //** Generate Entry Page from Model : END **//
    //** Test pulse : Start **/
    public String pulseIframeContent() throws Exception {
        return "pulseIframeContent";
    }
    public String pulse() throws Exception {
        return "pulse";
    }
    public void receivePulse() throws Exception {
        Map session = ActionContext.getContext().getSession();
//        System.out.println("session = "+ session);
        String logined = (session.get("logined")!=null)?"true":"false";
        System.out.println("is login... = " + logined);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.getWriter().append(logined);
        response.setContentType("text/plain");
        response.flushBuffer();
    }
    //** Test pulse : End **/
    
    private Boolean showListing = Boolean.FALSE;
    public Boolean getShowListing() {
        return showListing;
    }
    public void setShowListing(Boolean showListing) {
        this.showListing = showListing;
    }
    
    //** Datatable : with input + lazy load :: Start **//
    private List recordList = null;
    public List getRecordList() {
        return recordList;
    }
    
    public String datatable_ajax() throws Exception{
        printParam();
        dt_columnsArr = new String[] {"data:'rowIdx', orderable: 'false'", "col_2", "col_4"};
        dt_columnsIdxArr = new String[] {"2", "3", "5"};
        Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        response.setContentType("application/json");
        Map jsonMap = new HashMap();
        jsonMap.put("draw", Integer.parseInt(request.getParameter("draw"))+1 ) ;
        Map dataMap = new HashMap();
        populateDtServerSideInfo(dataMap, getDt_pageSize(), 1);
        dataMap.put("sql", "select the_pk, '' as rowIdx, col_1, col_2, col_4 from t_datatable_test where col_1 = 'asas'");
        dataMap.put("countSql", "select count(*) from t_datatable_test where col_1 = 'asas'");
//        dataMap.put("groupBy", "groupByColumns here...");
        dataMap.put("orderBy", dt_columnsIdxArr[(Integer)dataMap.get("orderIdx")] + " " + dataMap.get("orderDir"));
        
        new DataRetriever().retrieveData(dataMap, jsonMap, baseDAO);
        
        //this is sample if want to add additional column/data to result : START
        for (Map map : (List<Map>)jsonMap.get("data")) {
            map.put("href", "loadEditPageApplication?id=2&action=Application");
        }
        //this is sample if want to add additional column/data to result : END
        
        response.getWriter().append(gson.toJson(jsonMap));
        response.flushBuffer();
        return null;
    }
    
    public String datatableAjaxInput() throws Exception{
        dt_columnsArr = new String[] {"data:'rowIdx', orderable: false", "col_2", "col_4"};
        dt_columnsIdxArr = new String[] {"2", "3", "5"};
        dt_defaultOrder = "order: [[1, 'asc']],";
        return "datatable_ajax_input";
    }
    public String datatable2() throws Exception{
        try {
            dt_columnsArr = new String[] {"data:'rowIdx', orderable: false", "col_2", "col_4"};
            dt_columnsIdxArr = new String[] {"2", "3", "5"};
            dt_defaultOrder = "order: [[1, 'asc']],";
        } catch (Exception e) {
        }
        return "datatable2";
    }
    
    public String datatable() throws Exception{
        Map paramMap = new HashMap();
        //dt_columnsIdxArr = index of datatable column map to sorting field, "5" can be replace to column
        //name or some sql syntax
        dt_columnsIdxArr = new String[] {"5", "5", "5"}; //first column = No. 
        populateDtServerSideInfo(paramMap, getDt_pageSize(), 1);
        paramMap.put("sql", "select the_pk, '' as rowIdx, col_1, col_2, col_4 from t_datatable_test");
        paramMap.put("countSql", "select count(*) from t_datatable_test");
//        dataMap.put("groupBy", "groupByColumns here...");
        paramMap.put("orderBy", dt_columnsIdxArr[(Integer)paramMap.get("orderIdx")] + " " + paramMap.get("orderDir"));
        Map returnMap = new HashMap();
        
        //this is sample if want to add additional column/data to result : START
//        for (Map map : (List<Map>)paramMap.get("data")) {
//            map.put("href", "loadEditPageApplication?id=2&action=Application");
//        }
        //this is sample if want to add additional column/data to result : END

        new DataRetriever().retrieveData(paramMap, returnMap, baseDAO);
        
        // must set the total records of the list if using this way to populate datatable
        setNumberOfRows( ((Long)returnMap.get("recordsTotal")).intValue() ); 
        recordList = (List)returnMap.get("data");
        return "datatable"; //return page (head + body) with datatable prepopulated
    }
    
    public Integer getDt_rowIdx(Integer currentIdx) {
        return (getDt_pageNo()-1) * getDt_pageSize() + currentIdx;
    }
    
    public String dtLoad() throws Exception {
        printParam();
        Map paramMap = new HashMap();
        dt_columnsIdxArr = new String[] {"5", "5", "5"};
        populateDtServerSideInfo(paramMap, getDt_pageSize(), 1);
        paramMap.put("sql", "select the_pk, '' as rowIdx, col_1, col_2, col_4 from t_datatable_test");
        paramMap.put("countSql", "select count(*) from t_datatable_test");
//        dataMap.put("groupBy", "groupByColumns here...");
        paramMap.put("orderBy", dt_columnsIdxArr[(Integer)paramMap.get("orderIdx")] + " " + paramMap.get("orderDir"));

        //this is sample if want to add additional column/data to result : START
        for (Map map : (List<Map>)paramMap.get("data")) {
            map.put("href", "loadEditPageApplication?id=2&action=Application");
        }
        //this is sample if want to add additional column/data to result : END
        
        Map returnMap = new HashMap();
        new DataRetriever().retrieveData(paramMap, returnMap, baseDAO);
        setNumberOfRows( ((Long)returnMap.get("recordsTotal")).intValue() ); // << must set the total records of the list
        recordList = (List)returnMap.get("data");
        return "datatable_dtLoad"; //return datatable prepopulated only
    }
    //** Datatable : with input + lazy load :: END **//
    
    //** Display Image From FTP Server :: START **//
    private InputStream sampleImage = null;
    public InputStream getSampleImage() {
        return sampleImage;
    }
    public String ftpImage() throws Exception {
        sampleImage = new SFTPBean().getFile("sampleImage/Verification.png");
        return "ftpImage";
    }
    //** Display Image From FTP Server :: END **//
    
    //** Test Upload file : START **//
    private static int uploadCount = 0;
    private static Boolean keepLooping = Boolean.TRUE;
    private File binaryFile;
    public File getBinaryFile() {
        return binaryFile;
    }
    public void setBinaryFile(File binaryFile) {
        System.out.println("in set binary file");
        this.binaryFile = binaryFile;
    }
    
    private File binaryFileFileName;
    public File getBinaryFileFileName() {
        return binaryFileFileName;
    }
    public void setBinaryFileFileName(File binaryFileFileName) {
        this.binaryFileFileName = binaryFileFileName;
    }
    
    
    public String uploadConcurrent() throws Exception {
        Integer current = ++uploadCount;
//        System.out.println("uploading file " + current);
//        System.out.println("binaryFile.size = " + binaryFile.length());
//        System.out.println("binaryFileFileName = " + binaryFileFileName);
//        InputStream inStream = new FileInputStream(binaryFile);
//        while (keepLooping) 
//if (uploadCount < 3) {
//            java.lang.Thread.sleep(10000);
//}
//        }
//        System.out.println("start running " + current);
        SFTPBean ftp = new SFTPBean();
//        OBSUtil ftp = new OBSUtil();
        try {
            String filePath = "testConcurrentUpload";
//            System.out.println("start uploading to OBS file ("+current+") at " + new Timestamp(System.currentTimeMillis()));
            ftp.createFile(filePath+"/"+current, binaryFile);
//            System.out.println("complete uploading to OBS file ("+current+") at " + new Timestamp(System.currentTimeMillis()));
//            if (ftp.isDirExists(SystemConstants.DOMAIN.ftps_path + filePath, Boolean.FALSE)) {
//                System.out.println("exists");
//                
//                ftp.createFile(filePath+"/"+current, inStream);
//            } else {
//                System.out.println("not exists");
//                ftp.createDirIfNotExists(filePath, Boolean.FALSE);
//            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ftp.disconnect();
        }
        
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.setContentType("application/json");
        response.getWriter().append("done start running of " + current);
        response.flushBuffer();
        return null;
    }
    
    public static String fileName = "C:\\Users\\User\\Desktop\\framework\\OBS\\obs-upload";
    public static synchronized String getFileName() {
        if (fileName.equals("C:\\Users\\User\\Desktop\\framework\\OBS\\obs-upload")) {
            fileName = "C:\\Users\\user\\desktop\\SAINS-Fun-Run-2019_Reg-Form.pdf";
            return "C:\\Users\\User\\Desktop\\framework\\OBS\\obs-upload";
        }
        return fileName;
    }
    public void triggerLoop() throws Exception {
        HttpEntity entity = MultipartEntityBuilder.create()
                       .addPart("binaryFile", new FileBody(new File("C:\\Users\\user\\desktop\\catalina.pdf")))
                       .addPart("binaryFileFileName", new StringBody("SAINS-Fun-Run-2019_Reg-Form.pdf", ContentType.APPLICATION_FORM_URLENCODED))
                       .build();

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i=1; i<=50; i++) {
            executorService.execute(new Runnable()
            {
                @Override
                public void run() 
                {
                    HttpPost request = new HttpPost("http://localhost:8080/forNewProject/uploadConcurrentSample");
                    request.setEntity(entity);

                    HttpClient client = HttpClientBuilder.create().build();
                //    HttpResponse response = client.execute(request);
                    try {
                        client.execute(request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    public void triggerLoopOBS() throws Exception {
        HttpEntity entity = MultipartEntityBuilder.create()
                       .addPart("binaryFile", new FileBody(new File("C:\\Users\\user\\desktop\\catalina.pdf")))
                       .addPart("binaryFileFileName", new StringBody("SAINS-Fun-Run-2019_Reg-Form.pdf", ContentType.APPLICATION_FORM_URLENCODED))
                       .build();

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i=1; i<=50; i++) {
            executorService.execute(new Runnable()
            {
                @Override
                public void run() 
                {
                    HttpPost request = new HttpPost("http://localhost:8080/forNewProject/uploadConcurrentSample");
                    request.setEntity(entity);

                    HttpClient client = HttpClientBuilder.create().build();
                //    HttpResponse response = client.execute(request);
                    try {
                        client.execute(request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    
    public void uploadFile() throws Exception {
        SFTPBean ftp = new SFTPBean();
//        InputStream inStream = new FileInputStream(new File("C:\\Users\\user\\desktop\\SAINS-Fun-Run-2019_Reg-Form.pdf"));
        InputStream inStream = new FileInputStream(new File("C:\\Users\\User\\Desktop\\framework\\OBS\\obs-upload"));
        try {
            String fileId = CommonFunction.getId(20);
            String filePath = "testConcurrentUpload";
            if (ftp.isDirExists(SysConf.get("pathPrefix") + filePath, Boolean.FALSE)) {
                ftp.createFile(filePath+"/"+fileId, inStream, fileId+".pdf", SFTPBean.ExtensionWhiteList.DOC);
                ftp.insertToFileDirectory(filePath+"/"+fileId, fileId+".pdf", ServletActionContext.getServletContext().getMimeType(uppyFileFileName), fileId, baseDAO, "sampleTestUpload", Boolean.FALSE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ftp.disconnect();
            inStream.close();
        }
    }
    //** Test Upload file : END **//
    public String loadPDF() throws Exception {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        FileInputStream ficheroInput = new FileInputStream("C:\\Users\\user\\desktop\\SAINS-Fun-Run-2019_Reg-Form.pdf");
        int tamanoInput = ficheroInput.available();
        byte[] datosPDF = new byte[tamanoInput];
        ficheroInput.read(datosPDF, 0, tamanoInput);

//        response.setHeader("Content-disposition", "attachment; filename=\"Sample.pdf\"");
        response.setHeader("Content-disposition", "inline;filename='Sample.pdf'");
        response.setContentType("application/pdf");
        response.setContentLength(tamanoInput);
        response.getOutputStream().write(datosPDF);

        response.getOutputStream().flush();
        response.getOutputStream().close();

        ficheroInput.close();
        return null;
    }
    private String contentType = "application/pdf";
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
    
    public String uppy() {
        return "uppy";
    }
    
    private String uploadedFile_fileId;
    public String getUploadedFile_fileId() {
        return uploadedFile_fileId;
    }
    public void setUploadedFile_fileId(String uploadedFile_fileId) {
        this.uploadedFile_fileId = uploadedFile_fileId;
    }
    
    private String uploadedFile_fileName;
    public String getUploadedFile_fileName() {
        return uploadedFile_fileName;
    }
    public void setUploadedFile_fileName(String uploadedFile_fileName) {
        this.uploadedFile_fileName = uploadedFile_fileName;
    }
    
    
    //** UPPY file Upload: START **//
    UppyParentModel yourParentModel;
    public UppyParentModel getYourParentModel() {
        return yourParentModel;
    }
    public void setYourParentModel(UppyParentModel yourParentModel) {
        this.yourParentModel = yourParentModel;
    }
    
    public String uppy3() { //Multiple file upload
        yourParentModel = (UppyParentModel) baseDAO.getSession().getNamedQuery("UppyParentModel.findBy_createdBy")
                .setParameter("createdBy", ActionContext.getContext().getSession().get("loginId"))
                .uniqueResult();
        if (yourParentModel == null) {
            System.out.println("uppy3() yourParentModel is null");
            yourParentModel = new UppyParentModel();
            List drDocList = baseDAO.getSession().getNamedQuery("DocRepo.findBy_drDocApp_createdUser")
                .setParameter("createdBy", ActionContext.getContext().getSession().get("loginId"))
                .setParameter("drDocApp", "uppySample3")
                .list();
            getUppyFileMap().put("childList", drDocList);
            
//            UppyModel uppy1 = new UppyModel();
//            uppy1.setUppy_file_desc("first file");
//            uppy1.setDr_doc_id("abc123");
//            yourParentModel.getChildList().add(uppy1);
//            UppyModel uppy2 = new UppyModel();
//            uppy2.setUppy_file_desc("2nd file");
//            yourParentModel.getChildList().add(uppy2);
        } else {
            System.out.println("yourParentModel is not null :child.size = " + yourParentModel.getChildList().size());
            List list = new ArrayList();
            for (UppyModel uppy : yourParentModel.getChildList()) {
                list.add(uppy.getDrDocRepoModel());
            }
            getUppyFileMap().put("childList", list);
        }
        //if you have multiple file input, put the list to uppyFileMap
        return "uppy3";
    }
    public String uppy2() { 
        yourModel = (UppyModel) baseDAO.getSession().getNamedQuery("UppyModel.findBy_createdBy")
                .setParameter("createdBy", ActionContext.getContext().getSession().get("loginId"))
                .uniqueResult();
        DrDocRepoModel drDoc = null;
        if (yourModel != null) {
            drDoc = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id")
                .setParameter("dr_doc_id", yourModel.getDr_doc_id())
                .uniqueResult();
        } else {
            drDoc = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_drDocApp_createdUser")
                .setParameter("drDocApp", "uppySample2").setParameter("createdBy", ActionContext.getContext().getSession().get("loginId"))
                .uniqueResult();
        }
        if (drDoc != null) {
            uploadedFile_fileId = drDoc.getDr_doc_id();
            uploadedFile_fileName = drDoc.getDr_doc_name();
        }
        return "uppy2";
    }
    
    File uppyFile;
    public File getUppyFile() {
        return uppyFile;
    }
    public void setUppyFile(File uppyFile) {
        this.uppyFile = uppyFile;
    }
    
    String uppyFileFileName;
    public String getUppyFileFileName() {
        return uppyFileFileName;
    }
    public void setUppyFileFileName(String uppyFileFileName) {
        this.uppyFileFileName = uppyFileFileName;
    }
    
    
    public String uploadID;
    public String getUploadID() {
        return uploadID;
    }
    public void setUploadID(String uploadID) {
        this.uploadID = uploadID;
    }
    
    final static MimetypesFileTypeMap FILE_TYPE_MAP = new MimetypesFileTypeMap();
    
    
    String drAppCode_ = null;
    public String getDrAppCode_() {
        return drAppCode_;
    }
    public void setDrAppCode_(String drAppCode_) {
        this.drAppCode_ = drAppCode_;
    }
    
    private String uploadRecordId_ = null;
    public String getUploadRecordId_() {
        return uploadRecordId_;
    }
    public void setUploadRecordId_(String uploadRecordId_) {
        this.uploadRecordId_ = uploadRecordId_;
    }
    
    private Integer deleteFileChildIdx = null;
    public Integer getDeleteFileChildIdx() {
        return deleteFileChildIdx;
    }
    public void setDeleteFileChildIdx(Integer deleteFileChildIdx) {
        this.deleteFileChildIdx = deleteFileChildIdx;
    }
    
    private Integer addFileParentIdx = null;
    public Integer getAddFileParentIdx() {
        return addFileParentIdx;
    }
    public void setAddFileParentIdx(Integer addFileParentId) {
        try {
            this.addFileParentIdx = addFileParentId;
        } catch (Exception e) {
        }
    }
    
    private List<UppyParentModel> uppyParentList = null;
    public List<UppyParentModel> getUppyParentList() {
        if (uppyParentList == null) uppyParentList = new ArrayList();
        return uppyParentList;
    }
    public void setUppyParentList(List uppyParentList) {
        this.uppyParentList = uppyParentList;
    }
    
    private User userRecord = null;
    public User getUserRecord() {
        return userRecord;
    }
    public void setUserRecord(User userRecord) {
        this.userRecord = userRecord;
    }
    
    private String getFieldName(Method method) {
        javax.persistence.Column columnAnnotation = method.getAnnotation(javax.persistence.Column.class);
        if (columnAnnotation != null) {
            return columnAnnotation.name();
        }
        return method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
    }
    
    private static Gson gson = new Gson();
    public String tryUppyUpload() throws Exception {
//        OBSUtil obs = new OBSUtil();
//        try {
////            System.out.println("TestAction testDelete/meeting/Subfolder/LICENSE.pdf = " + obs.isFileExists("testDelete/meeting/Subfolder/LICENSE.pdf", true ));
//	    for (String key : obs.listFolder("testDelete/", false)) {
//                System.out.println("key = "+ key);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            obs.disconnect();
//        }
        BaseDAO dao = baseDAO;
        uppyParentList = dao.list(UppyParentModel.class);
        directModel = (UppyModel) baseDAO.getSession().getNamedQuery("UppyModel.findBy_uppyFileDesc").setParameter("uppyFileDesc", "_tryUppyUpload_").uniqueResult();
        moreAttachmentModel = (ChildWithAttachmentModel) baseDAO.getSession().getNamedQuery("ChildWithAttachmentModel.findBy_uppyFileDesc").setParameter("uppyFileDesc", "_moreAttachment_").uniqueResult();
        if (moreAttachmentModel == null) {
            moreAttachmentModel = new ChildWithAttachmentModel();
        }
        userRecord = (User) dao.getModelById((String)ActionContext.getContext().getSession().get("userId"), User.class);
        return "tryUppyUpload";
    }
    public String tryTus() throws Exception {
        return "tryTus";
    }
    
    public String saveParentChild() throws Exception {
        BaseDAO dao = baseDAO;
        for (UppyParentModel parentModel : getUppyParentList()) {
            if (Validator.isEmpty(parentModel.getID())) {
                dao.insert(parentModel);
            } else {
                System.out.println("parentModel.getID =  " + parentModel.getID());
                for (UppyModel uppy : parentModel.getChildList()) {
                    System.out.println("uppy.getDr_doc_id = " + uppy.getDr_doc_id());
                }
                dao.update(parentModel);
            }
        }
        addActionMessage("Updated");
        return tryUppyUpload();
    }
    
    public String addParentModel() throws Exception {
        UppyParentModel parent = new UppyParentModel();
        parent.setUppy_parent_desc("Parent created at " + Formatter.formatDate(DateUtil.getCurrentDate(), "dd MMM yyyy"));
        BaseDAO dao = baseDAO;
        dao.insert(parent);
        
        getUppyParentList().add(parent);
        return "tryUppyUpload";
    }
    public String addFile() throws Exception {
        if (addFileParentIdx != null) {
            try {
                UppyParentModel parent = uppyParentList.get(addFileParentIdx);
                parent.getChildList().add(new UppyModel());
            } catch (Exception e) {
            }
        }
        return "tryUppyUpload";
    }
    String tJunModel;

    public String gettJunModel() {
        return tJunModel;
    }

    public void settJunModel(String tJunModel) {
        this.tJunModel = tJunModel;
    }
    
    public String deleteFile() throws Exception {
        if (addFileParentIdx != null) {
            try {
                UppyParentModel parent = uppyParentList.get(addFileParentIdx);
                if (parent != null) {
                    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                    request.setAttribute("ignoreCsrfCheck", "true");
                    UppyModel child = parent.getChildList().get(deleteFileChildIdx);
                    baseDAO.delete(child.getID(), child);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tryUppyUpload();
    }
    
    UppyModel directModel = null;
    public UppyModel getDirectModel() {
        return directModel;
    }
    public void setDirectModel(UppyModel directModel) {
        this.directModel = directModel;
    }
    
    ChildWithAttachmentModel moreAttachmentModel = null;
    public ChildWithAttachmentModel getMoreAttachmentModel() {
        return moreAttachmentModel;
    }
    public void setMoreAttachmentModel(ChildWithAttachmentModel moreAttachmentModel) {
        this.moreAttachmentModel = moreAttachmentModel;
    }
    
    
    public String createDirectModel() throws Exception {
        UppyModel toDelete = (UppyModel) baseDAO.getSession().getNamedQuery("UppyModel.findBy_uppyFileDesc").setParameter("uppyFileDesc", "_tryUppyUpload_").uniqueResult();
        if (toDelete == null) {
            directModel = new UppyModel();
            directModel.setUppy_file_desc("_tryUppyUpload_");
            baseDAO.insert(directModel);
        }
        return tryUppyUpload();
    }
    public String clearDirectModel() throws Exception {
        UppyModel toDelete = (UppyModel) baseDAO.getSession().getNamedQuery("UppyModel.findBy_uppyFileDesc").setParameter("uppyFileDesc", "_tryUppyUpload_").uniqueResult();
        try {
            if (toDelete != null) {
                baseDAO.beginBatchTransaction();
                baseDAO.deleteModelWithPrePost(toDelete);
                baseDAO.commitBatchTransaction();
            } else {
                directModel = toDelete;
            }
        } catch (Exception e) {
            
            baseDAO.rollbackBatchTransaction();
        } finally {
            toDelete.printOperationList();
        }
        return tryUppyUpload();
    }
    public String clear1ModelMoreAttachment() throws Exception {
        ChildWithAttachmentModel toDelete = (ChildWithAttachmentModel) baseDAO.getSession().getNamedQuery("ChildWithAttachmentModel.findBy_uppyFileDesc").setParameter("uppyFileDesc", "_moreAttachment_").uniqueResult();
        try {
            if (toDelete != null) {
                baseDAO.beginBatchTransaction();
                baseDAO.deleteModelWithPrePost(toDelete);
                baseDAO.commitBatchTransaction();
            } else {
                moreAttachmentModel = toDelete;
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseDAO.rollbackBatchTransaction();
        }
        return tryUppyUpload();
    }
    
    public String saveDirectModel() throws Exception {
        if (directModel != null) {
            directModel.set_operation("directModel");
            directModel.setUppy_file_desc("_tryUppyUpload_");
            if (Validator.isEmpty(directModel.getID())) {
                baseDAO.insert(directModel);
            } else {
                baseDAO.update(directModel);
            }
        }
        return tryUppyUpload();
    }
    public String saveMoreAttachmentModel() throws Exception {
        if (moreAttachmentModel != null) {
            moreAttachmentModel.set_operation("moreAttachmentModel");
            moreAttachmentModel.setUppy_file_desc("_moreAttachment_");
            if (Validator.isEmpty(moreAttachmentModel.getID())) {
                baseDAO.insert(moreAttachmentModel);
            } else {
                baseDAO.update(moreAttachmentModel);
            }
        }
        return tryUppyUpload();
    }
    public String uppyUpload() throws Exception {
        System.out.println("nimeType = " + ServletActionContext.getServletContext().getMimeType(uppyFileFileName));
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Map jsonMap = new HashMap();
        response.setContentType("application/json");
        SFTPBean ftp = new SFTPBean();
        InputStream inStream = new FileInputStream(uppyFile);
        String newDrDocId = CommonFunction.getId(20);
        try {
            if (Validator.isEmpty(uploadRecordId_)) {
                String sftpUploadPath = "uppyTempFolder";
                ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
                if (ftp.isDirExists(SysConf.get("pathPrefix") + sftpUploadPath, Boolean.FALSE)) {
                    ftp.createFile(sftpUploadPath+"/"+newDrDocId, inStream);//, uppyFileFileName, SFTPBean.ExtensionWhiteList.IMAGE_and_PDF);
                    ftp.insertToFileDirectory(sftpUploadPath+"/"+newDrDocId, uppyFileFileName, ServletActionContext.getServletContext().getMimeType(uppyFileFileName), newDrDocId, baseDAO, drAppCode_, Boolean.TRUE);
    //                ftp.insertToFileDirectory(filePath, filePath, fileId, baseDAO);
                }
            } else {
                String sftpUploadPath = null;
                if (drAppCode_.equals("uppySample2")) {
                    sftpUploadPath = "uppySample2ActualFolder";
                } else if (drAppCode_.equals("uppySample3")) {
                    sftpUploadPath = "uppyMultipleActualFolder";
                }
                System.out.println("uploadRecordId_ = " + uploadRecordId_);
                ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
                if (ftp.isDirExists(SysConf.get("pathPrefix") + sftpUploadPath, Boolean.FALSE)) {
                    ftp.createFile(sftpUploadPath+"/"+uploadRecordId_+"/"+newDrDocId, inStream);//, uppyFileFileName, SFTPBean.ExtensionWhiteList.IMAGE_and_PDF);
                    ftp.insertToFileDirectory(sftpUploadPath+"/"+uploadRecordId_+"/"+newDrDocId, uppyFileFileName, ServletActionContext.getServletContext().getMimeType(uppyFileFileName), newDrDocId, baseDAO, drAppCode_, Boolean.FALSE);
                    if (drAppCode_.equals("uppySample2")) {
                        yourModel = (UppyModel) baseDAO.getModelById(uploadRecordId_, UppyModel.class);
                    } else if (drAppCode_.equals("uppySample3")) {
                        yourModel = new UppyModel();
                        yourModel.setUppy_parent_id(uploadRecordId_);
                    }
                    yourModel.setDr_doc_id(newDrDocId);
                    try {
                        baseDAO.beginBatchTransaction();
                        if (drAppCode_.equals("uppySample2")) {
                            baseDAO.getSession().update(yourModel);
                        } else if (drAppCode_.equals("uppySample3")) {
                           baseDAO.insert(yourModel);
                        }
                        baseDAO.commitBatchTransaction();
                    } catch (Exception e) {
                        baseDAO.rollbackBatchTransaction();
                        try {
                            ftp.deleteFile(sftpUploadPath+"/"+newDrDocId); //try to delete if the file is uploaded but record not updated.
                        } catch (Exception delEx) {
                        }
                        throw e;
                    }
                }
            }
            jsonMap.put("status", "success");
            jsonMap.put("fileId", newDrDocId);
            
        } catch (BaseException be) {
            be.printStackTrace();
            jsonMap.put("status", "fail");
            jsonMap.put("errMsg", be.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("status", "fail");
            jsonMap.put("errMsg", getText("errors.unknownError_plsTryAgain"));
        } finally {
            ftp.disconnect();
        }
        
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
        return null;
    }
    
    public void removeTempFile() throws Exception {
        DrDocRepoModel docRepo = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id").setParameter("dr_doc_id", uploadID).uniqueResult();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Map jsonMap = new HashMap();
        response.setContentType("application/json");
        try {
            if (new SFTPBean().deleteFile(docRepo.getDr_doc_path())) {
                baseDAO.beginBatchTransaction();
                baseDAO.getSession().delete(docRepo);
                baseDAO.commitBatchTransaction();
                jsonMap.put("status", "success");
            } else {
                jsonMap.put("status", "fail");
            }
        } catch (Exception e) {
            jsonMap.put("status", "fail");
        }
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
    }
    public String viewTempFile() throws Exception {
        DrDocRepoModel docRepo = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id").setParameter("dr_doc_id", uploadID).uniqueResult();
        inputStream = new SFTPBean().getFile(docRepo.getDr_doc_path());
//        contentDisposition = "attachment;filename="+docRepo.getDr_doc_name();
        contentDisposition = "filename=\""+StringEscapeUtils.escapeHtml4(docRepo.getDr_doc_name())+"\"";
        contentType = FILE_TYPE_MAP.getContentType(docRepo.getDr_doc_name());
        if (contentType.startsWith("application/")) {
            contentType = "application/" + docRepo.getDr_doc_name().substring(docRepo.getDr_doc_name().lastIndexOf(".")+1);
        }
        System.out.println("contentType = " + contentType);
//        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
//        response.setContentType(contentType);
//        response.setContentLength((int) inputStream.available());
//         
//        // forces download
//        String headerKey = "Content-Disposition";
////        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
//        response.setHeader(headerKey, contentDisposition);
//         
//        // obtains response's output stream
//        OutputStream outStream = response.getOutputStream();
//         
//        byte[] buffer = new byte[4096];
//        int bytesRead = -1;
//         
//        while ((bytesRead = inputStream.read(buffer)) != -1) {
//            outStream.write(buffer, 0, bytesRead);
//        }
//         
//        inputStream.close();
//        outStream.close(); 
//        response.flushBuffer();
//        return null;
        return "fileDownload";
    }
    
    public String uppySaveApplication() throws Exception {
        return "uppy";
    }
    
    private String descOfFile;
    public String getDescOfFile() {
        return descOfFile;
    }
    public void setDescOfFile(String descOfFile) {
        this.descOfFile = descOfFile;
    }
    
    private String uppySample2FileId;
    public String getUppySample2FileId() {
        return uppySample2FileId;
    }
    public void setUppySample2FileId(String uppySample2FileId) {
        this.uppySample2FileId = uppySample2FileId;
    }
    
    private UppyModel yourModel = null;
    public UppyModel getYourModel() {
        return yourModel;
    }
    public void setYourModel(UppyModel yourModel) {
        this.yourModel = yourModel;
    }
    
    public String uppy2SaveApplication() throws Exception {
        System.out.println("yourModel.desc = " + yourModel.getUppy_file_desc());
        if (Validator.isEmpty(yourModel.getID())) {//new record (insert)
            
            yourModel.set_uppyUploadFile_drDocPath("uppySample2ActualFolder");
            yourModel.set_uppyUploadFile_drDocId(uppySample2FileId);
            yourModel.setDr_doc_id(uppySample2FileId);
            baseDAO.insert(yourModel);
        } else {//existing record (update)
            baseDAO.update(yourModel); //only update the updatable columns (in this case, the uppy_file_desc)
        }
        return uppy2();
    }
    
    private List<String> multipleFileDrDocIds = null;
    public List<String> getMultipleFileDrDocIds() {
        return multipleFileDrDocIds;
    }
    public void setMultipleFileDrDocIds(List<String> multipleFileDrDocIds) {
        this.multipleFileDrDocIds = multipleFileDrDocIds;
    }
    
    public String uppy3SaveApplication() throws Exception {
        System.out.println("yourParentModel.desc = " + yourParentModel.getUppy_parent_desc());
        if (Validator.isEmpty(yourParentModel.getID())) {//new record (insert)
            System.out.println("insert ---");
            if (multipleFileDrDocIds !=null) {
                for (String drDocId : multipleFileDrDocIds) {
                    DrDocRepoModel docRepo = (DrDocRepoModel)baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id_notTemp").setParameter("dr_doc_id", drDocId).uniqueResult();
                    if (docRepo == null) {

                        UppyModel uppyModel = new UppyModel();
                        uppyModel.setAppendRecordID(Boolean.FALSE);
                        uppyModel.setDr_doc_id(drDocId);
                        uppyModel.set_uppyUploadFile_drDocPath("uppyMultipleActualFolder");
                        uppyModel.set_uppyUploadFile_drDocId(drDocId);
                        yourParentModel.getChildList().add(uppyModel);
                    }
                }
            }
            baseDAO.insert(yourParentModel);
        } else {//existing record (update)
            System.out.println("update ---");
            baseDAO.update(yourParentModel); //only update the updatable columns (in this case, the uppy_file_desc)
        }
        return uppy3();
    }
    //** UPPY file Upload: END **//
    //** SmartXChange : START **//
    private String loadWhat = null;
    public String getLoadWhat() {
        return loadWhat;
    }
    public void setLoadWhat(String loadWhat) {
        this.loadWhat = loadWhat;
    }
    
    public void loadSmartXChange() throws Exception {
        try {
            Map jsonMap = new HashMap();
            SmartXChangeUtil sxcUtil = new SmartXChangeUtil(SystemConstants.SmartXChange.WasteBinApplicationSetup);
            if (loadWhat.equals("token")) {
                jsonMap.put("token", sxcUtil.getAccessToken());
            } else if (loadWhat.equals("getData")) {
                String strUrl="https://apitnt.sains.com.my/waste_bin_replacement/v1.0/applications?cclCode=B2&sSvcId=1581908432324H9FFpq0&sAFrmId=1597216627416fbrEN90";
                String strJsonString = sxcUtil.callSXCReadDataGet(strUrl,"");
                jsonMap.put("getData", strJsonString);
                Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.S").create();
//                .setDateFormat("yyyy-MM-dd HH:mm:ss.S").create();
                Map map = gson.fromJson(strJsonString, Map.class);
                System.out.println("map.get(received_date) = " + ((Map)((Map)(map.get("data"))).get("form_info")).get("received_date").getClass());
                System.out.println("aaa = " + DateUtil.getDate((String)((Map)((Map)(map.get("data"))).get("form_info")).get("received_date"), "yyyy-MM-dd HH:mm:ss.S"));
            }
            HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            response.setContentType("application/json");
            response.getWriter().append(new Gson().toJson(jsonMap));
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //** SmartXChange : END **//
    public String requestMethod() {
        return "requestMethod";
    }
    
    public String fileToBeID = null;
    public String getFileToBeID() {
        if (fileToBeID == null) {
            fileToBeID = CommonFunction.getId(20);
        }
        return fileToBeID;
    }
    public void setFileToBeID(String fileToBeID) {
        this.fileToBeID = fileToBeID;
    }
    
    private String wfCaseId = null;
    public String getWfCaseId() {
        return wfCaseId;
    }
    public void setWfCaseId(String wfCaseId) {
        this.wfCaseId = wfCaseId;
    }
    
    private String wfCode = null;
    public String getWfCode() {
        return wfCode;
    }
    public void setWfCode(String wfCode) {
        this.wfCode = wfCode;
    }
    
    private String workflowParam = null;
    public String getWorkflowParam() {
        return workflowParam;
    }
    public void setWorkflowParam(String workflowParam) {
        this.workflowParam = workflowParam;
    }
    
    private String workflowAction = null;
    public String getWorkflowAction() {
        return workflowAction;
    }
    public void setWorkflowAction(String workflowAction) {
        this.workflowAction = workflowAction;
    }
    
    private String workflowError = null;
    public String getWorkflowError() {
        return workflowError;
    }
    public void setWorkflowError(String workflowError) {
        this.workflowError = workflowError;
    }
    
    private List workflowActionOptions = null;
    public List getWorkflowActionOptions() {
        if (workflowActionOptions == null) {
            workflowActionOptions = new ArrayList();
            workflowActionOptions.add(new Options("startWorkflowInstance", "Start Workflow"));
            workflowActionOptions.add(new Options("grabTask", "Grab Task"));
            workflowActionOptions.add(new Options("completeTask", "Complete Task"));
        }
        return workflowActionOptions;
    }
    
    public String doWorkflowAction() throws Exception {
        RouteUtil routeUtil = new RouteUtil();
        List param = new ArrayList();
        Map returnMap = null;
        if (workflowAction.equals("startWorkflowInstance")) {
            param.add("wf_code::=" + wfCode);
            param.add("caseId::=" + wfCaseId);
            populateParam(param);
            System.out.println("starting workflow : " + wfCode);
            workflowError = routeUtil.workflowRESTCall("startWorkflowInstance", param);
        } else if (workflowAction.equals("grabTask")) {
            param.add("caseId::=" + wfCaseId);
            populateParam(param);
            workflowError = routeUtil.workflowRESTCall("grabTask", param);
        } else if (workflowAction.equals("completeTask")) {
            param.add("caseId::=" + wfCaseId);
            param.add("activityCode::=" + wfCode);
            populateParam(param);
            workflowError = routeUtil.workflowRESTCall("completeTask", param);
        }
        return load();
    }
    
    private void populateParam(List param) {
        if (!Validator.isEmpty(workflowParam)) {
            for (String paramSetup : workflowParam.split(";;")) {
                String[] setupArr = paramSetup.split("::=");
                param.add(setupArr[0]+"::="+setupArr[1]);
            }
        }
    }
}