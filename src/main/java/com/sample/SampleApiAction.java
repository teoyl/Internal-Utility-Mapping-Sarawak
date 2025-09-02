package com.sample;

import com.SysConf;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.ApiUtil;
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

public class SampleApiAction extends BaseActionSupport_API{// implements ModelDriven<String> {

    private static final long serialVersionUID = -6659925652584240539L;
    private String useServiceFactory_ = "N";

    public SampleApiAction() {
        model = new String();
        setInsertUpdateSuccessCode(SystemConstants.ACTION_Status.LOAD_EDIT_PAGE);
    }

    @Override
    public String apiApp() {
        return "API_CALL";
    }

    @Override
    public Boolean validateRights() {
        return Boolean.TRUE;
    }
    
    public void tryApiCall() throws Exception {
        System.out.println("childMgmt = " + has_right_byAppRightCode("ParentAction", "childMgmt", "10"));
        System.out.println("ALL,childMgmt2 = " + has_right_byAppRightCode("ParentAction", "ALL,childMgmt2", "10"));
        System.out.println("childMgmt2 = " + has_right_byAppRightCode("ParentAction", "childMgmt2", "10"));
        System.out.println("apiKey = " + getApiKey());
        System.out.println("accessToken= " + getAccessToken());
        Map map = new HashMap();
        map.put("message", "tryApiCall triggered");
        responseCall(map);
    }

    public void method1() throws Exception {
        Map map = new HashMap();
        map.put("message", "This is method1. Over...");
        responseCall(map);
    }
    
    public void method2() throws Exception {
        Map map = new HashMap();
        map.put("message", "This is method1. Over...");
        responseCall(map);
    }
    
    
    private String abc;
    public String getAbc() {
        return abc;
    }
    public void setAbc(String abc) {
        this.abc = abc;
    }
    
}