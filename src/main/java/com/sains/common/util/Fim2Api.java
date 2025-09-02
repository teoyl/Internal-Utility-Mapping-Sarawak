package com.sains.common.util;

import com.PropertyGetter;
import com.SysConf;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.LogFunction;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Fim2Api {

    public static Map verifyFimLogin(HttpServletRequest request, Boolean updateUserInfo) throws Exception {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
        urlParameters.add(new BasicNameValuePair("client_id", "test-TOwAo45r52fO3A9gD4IbMrmkbAkIr4RU-apps.sains.com.my"));
        urlParameters.add(new BasicNameValuePair("client_secret", "CRrxSFPRzLSywvcxrFlSYKqcJdEvbbbLDAiuohEesONjjUNnOgNVclXFzqEknYSg"));
        urlParameters.add(new BasicNameValuePair("redirect_uri", "http://thensw.sains.com.my:8080/forNewProject/fim2VerifyLogin"));
        urlParameters.add(new BasicNameValuePair("code", request.getParameter("code")));
        Map validateFimMap = CommonFunction.getMapFromJson(HttpsUrlUtil.https_selfSignCallUrlPost(SysConf.get("fim.TOKEN_URL"), urlParameters));
        if (request.getParameter("state") != null && request.getParameter("state").equals(((SessionMap)ActionContext.getContext().getSession()).get("sesStateCode"))) {
            if (updateUserInfo) {
                //Map userMap = CommonFunction.getMapFromJson(HttpsUrlUtil.https_selfSignCallUrlPost_bearerToken(SysConf.get("fim.USERINFO_URL"), (String)validateFimMap.get("access_token")));
            }
            String[] chunks = ((String)validateFimMap.get("id_token")).split("\\.");
            java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();

            
            
            String payload = new String(decoder.decode(chunks[1]));
            ((SessionMap)ActionContext.getContext().getSession()).put("sesAccessToken", "_fim2Login_");
            validateFimMap.put("status", "success");
            validateFimMap.put("fimLoginUserId", CommonFunction.getMapFromJson(payload).get("sub"));
        } else {
            validateFimMap.put("status", "fail");
            validateFimMap.put("errMsg", "fim.invalidFimState");
        }
        return validateFimMap;
    }
    
}
