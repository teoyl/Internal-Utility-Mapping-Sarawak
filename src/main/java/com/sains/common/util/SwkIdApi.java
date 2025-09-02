package com.sains.common.util;

import com.PropertyGetter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
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
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SwkIdApi {

    private final static String SWKID_URL = SystemConstants.SarawakID.API_URL[SystemConstants.ENV];
    private final static String CLIENT_ID = SystemConstants.SarawakID.CLIENT_ID[SystemConstants.ENV];
    private final static String SECRET = SystemConstants.SarawakID.SECRET[SystemConstants.ENV];
    private final static List OMIT_SECRET = Arrays.asList(new String[]{SystemConstants.SarawakID.API_TOKEN_USER});
    private String api_name = "";

    public static JSONObject loadSwkIdApiData(String profile_type, String user_name, String language,  String searchParam) throws Exception {
//        state,division,religion,race,district,subdistrict,country,foreigner_purpose_visit,gender,city
        String theUrl = "https://sarawakid-tnt.sarawak.gov.my/web/apiv1/profile_get/"; //i hardcode the url of profile_get
//        Gson gson = new GsonBuilder()
//        .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        JSONObject jsonData = null;
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
//                Map map = new HashMap();
//                map.put("params", params);
            arguments.add(new BasicNameValuePair("client_id", SystemConstants.SarawakID.CLIENT_ID[SystemConstants.ENV]));
            arguments.add(new BasicNameValuePair("secret", SystemConstants.SarawakID.SECRET[SystemConstants.ENV]));
            arguments.add(new BasicNameValuePair("profile_type", profile_type));
            arguments.add(new BasicNameValuePair("lang", language));
            arguments.add(new BasicNameValuePair("user_name", user_name));
            
            httpPost.setEntity(new UrlEncodedFormEntity(arguments));
            CloseableHttpResponse resp = httpClient.execute(httpPost);
            String result;
            String overall="";
            BufferedReader br = new BufferedReader(
                new InputStreamReader(resp.getEntity().getContent()));
            while ((result = br.readLine()) != null){
                overall = overall + "\n" + result;
            }
//                System.out.println("overall = " + overall);
            resp.close();
            JSONParser parser = new JSONParser();
            jsonData = (JSONObject) parser.parse(overall);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw e;
        }
        return jsonData;
    }
    
    public static JSONObject validateSwkIdLogin(String userId, String pswd) throws Exception {
        String theUrl = SystemConstants.SarawakID.VALIDATE_LOGIN_URL[SystemConstants.ENV];
//        Gson gson = new GsonBuilder()
//        .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        JSONObject jsonData = null;
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
//                Map map = new HashMap();
//                map.put("params", params);
            arguments.add(new BasicNameValuePair("client_id", SystemConstants.SarawakID.CLIENT_ID[SystemConstants.ENV]));
            arguments.add(new BasicNameValuePair("secret", SystemConstants.SarawakID.SECRET[SystemConstants.ENV]));
            arguments.add(new BasicNameValuePair("usr_short_name", userId));
            arguments.add(new BasicNameValuePair("usr_password", pswd));
            arguments.add(new BasicNameValuePair("login_by", "sarawakid"));
            httpPost.setEntity(new UrlEncodedFormEntity(arguments));
            CloseableHttpResponse resp = httpClient.execute(httpPost);
            String result;
            String overall="";
            BufferedReader br = new BufferedReader(
                new InputStreamReader(resp.getEntity().getContent()));
            while ((result = br.readLine()) != null){
                overall = overall + "\n" + result;
            }
//                System.out.println("overall = " + overall);
            resp.close();
            JSONParser parser = new JSONParser();
            jsonData = (JSONObject) parser.parse(overall);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw e;
        }
        return jsonData;
    }
    private JSONObject callApi(Map<String, Object> param) throws CustomBaseException {
        try {
//            if (SystemConstants.ENV == 0) { // development, bypass ssl
//                new LogFunction().logInfo(this.getClass(), "Dev: bypass ssl");
//                // Create a trust manager that does not validate certificate chains
//                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                        return null;
//                    }
//
//                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                    }
//
//                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                    }
//                }
//                };
//
//                // Install the all-trusting trust manager
//                SSLContext sc = SSLContext.getInstance("SSL");
//                sc.init(null, trustAllCerts, new java.security.SecureRandom());
//                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//
//                // Create all-trusting host name verifier
//                HostnameVerifier allHostsValid = new HostnameVerifier() {
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                };
//
//                // Install the all-trusting host verifier
//                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//            } // end bypass ssl

            String api_url = SWKID_URL + api_name;
            URL url = new URL(api_url);
            Debug.printFrameworkDebug("URL: " + api_url);

            if (!OMIT_SECRET.contains(api_name)) {
                param.put("client_id", CLIENT_ID);
                param.put("secret", SECRET);
            }

            // populate post data
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> p : param.entrySet()) {
                if (postData.length() != 0) {
                    postData.append('&');
                }
                postData.append(URLEncoder.encode(p.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(p.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            Debug.printFrameworkDebug("Data: " + postData);
            JSONObject jsonData = null;
            if (SystemConstants.ENV == 0) {
                SSLContext sslContext = new SSLContextBuilder()
                            .loadTrustMaterial(null, (certificate, authType) -> true).build();
                    org.apache.http.impl.client.CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
                            .setSSLHostnameVerifier(new NoopHostnameVerifier())
                            .build();

                HttpPost httpPost = new HttpPost(api_url);
                ArrayList<NameValuePair> postParameters;
                postParameters = new ArrayList<NameValuePair>();
                for (String postParams : postData.toString().split("&")) {
                    String[] paramArr = postParams.split("=");
                    postParameters.add(new BasicNameValuePair(paramArr[0], paramArr[1]));
                }

                httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
                CloseableHttpResponse resp = httpClient.execute(httpPost);
                String result;
                StringBuilder buffer = new StringBuilder();
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(resp.getEntity().getContent()));
                while ((result = br.readLine()) != null){
                    buffer.append(result+"\r\n");
                }
                String strJsonString = buffer.toString();
                Debug.printFrameworkDebug("res: " + strJsonString);
                JSONParser parser = new JSONParser();
                if (!Validator.isEmpty(strJsonString)) {
                    JSONObject jsonRes = (JSONObject) parser.parse(strJsonString);
                    if (jsonRes.get("transaction_status").toString().equals("1")) {
                        jsonData = jsonRes;
                        if (jsonRes.containsKey("data")) {
                            String strDataString = jsonRes.get("data").toString();
                            jsonData = (JSONObject) parser.parse(strDataString);
                        }
                    }
                }
            } else {
                // execute request
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postDataBytes);
                conn.setReadTimeout(60000);
                conn.setConnectTimeout(60000);
                // get result
                StringBuilder buffer = new StringBuilder();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                int read;
                char[] chars = new char[1024];
                while ((read = in.read(chars)) != -1) {
                    buffer.append(chars, 0, read);
                }

                String strJsonString = buffer.toString();
                Debug.printFrameworkDebug("res: " + strJsonString);
                JSONParser parser = new JSONParser();
                if (!Validator.isEmpty(strJsonString)) {
                    JSONObject jsonRes = (JSONObject) parser.parse(strJsonString);
                    if (jsonRes.get("transaction_status").toString().equals("1")) {
                        jsonData = jsonRes;
                        if (jsonRes.containsKey("data")) {
                            String strDataString = jsonRes.get("data").toString();
                            jsonData = (JSONObject) parser.parse(strDataString);
                        }
                    }
                }
                in.close();
            }
            return jsonData;
        } catch (NoRouteToHostException | UnknownHostException e) {
            new LogFunction().logError(this.getClass(), "", e);
            throw new CustomBaseException(new PropertyGetter().getPropertyText("error.connect.sarawakid"));
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }

        return null;
    }

    /**
     * This API returns a list of user profiles from Sarawak ID and MyIdentity.
     *
     * @return
     */
    public Map user_get(String userId) throws CustomBaseException {
        api_name = SystemConstants.SarawakID.API_USER_GET;

        Map param = new HashMap();
        param.put("dataset_from", "sid");
        param.put("user_name", userId);
        param.put("sarawak_id", userId);

        JSONObject json = callApi(param);
        if (json != null) {
            if (json.containsKey("Sarawak ID")) {
                if (!json.get("Sarawak ID").toString().equals("Record not found")) {
                    ObjectMapper mapper = new ObjectMapper();
                    List<Map<String, Object>> userList = null;
                    try {
                        userList = mapper.readValue(json.get("Sarawak ID").toString(), new TypeReference<List<Map<String, Object>>>() {
                        });
                    } catch (IOException ex) {
                    }
                    if (userList != null && userList.size() > 0) {
                        return userList.get(0);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Request for a token by passing the code given by Sarawak ID. This token
     * is tied to the user who are currently login on Sarawak ID. This token can
     * be used on token_user API to retrieve the user profile.
     *
     * @return
     */
    public String token_exchange(String state, String code) throws CustomBaseException {
        api_name = SystemConstants.SarawakID.API_TOKEN_EXCHANGE;

        String access_token = "";
        Map param = new HashMap();
        param.put("grant_type", "authorization_code");
        param.put("state", state);
        param.put("code", code);

        JSONObject json = callApi(param);
        if (json != null) {
            if (json.containsKey("access_token")) {
                return json.get("access_token").toString();
            }
        }
        return access_token;
    }

    /**
     * Request for a token by passing a valid user login credentials. This token
     * is tied to the user, and can be used on token_user API to retrieve the
     * user profile.
     *
     * @return
     */
    public String login(String userId, String password, String loginBy) throws CustomBaseException {
        api_name = SystemConstants.SarawakID.API_LOGIN;

        String access_token = "";
        Map param = new HashMap();
        param.put("usr_short_name", userId);
        param.put("usr_password", password);
        param.put("login_by", loginBy);

        JSONObject json = callApi(param);
        if (json != null) {
            if (json.containsKey("access_token")) {
                return json.get("access_token").toString();
            }
        }
        return access_token;
    }

    /**
     * This API returns the user profile which was tied to the access token
     * passed.
     *
     * @return
     */
    public Map token_user(String access_token) throws CustomBaseException {
        api_name = SystemConstants.SarawakID.API_TOKEN_USER;

        Map param = new HashMap();
        param.put("access_token", access_token);

        JSONObject json = callApi(param);
        if (json != null) {
            Map map = new Gson().fromJson(json.toString(), new TypeToken<HashMap<String, Object>>() {
            }.getType());

            return map;
        }

        return null;
    }
}
