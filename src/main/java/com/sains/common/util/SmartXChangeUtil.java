/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sains.common.util;

import com.sains.framework.base.Debug;
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Base64;
import java.net.*;
import javax.net.ssl.*;
import java.security.cert.*;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.json.simple.*;
import org.json.simple.parser.*;
/**
 *
 * @author ela2sains
 */
public class SmartXChangeUtil {

    private static String pstrAccessToken = "";
    private static long plExpiryTimestamp = 0;
    
    private String sSxcApplicationName = "";
    private SystemConstants.SmartXChange.SETUP smartXChangeSetup = null;
    
    public SmartXChangeUtil(SystemConstants.SmartXChange.SETUP setupParam) {
        sSxcApplicationName = setupParam.applicationName;
        smartXChangeSetup = setupParam;
    }
    
    //Return access token for API invocation
    public String getAccessToken() {
        if ((System.currentTimeMillis() / 1000) >= plExpiryTimestamp) {	//Expired
            if (genAccessToken()) {
                return pstrAccessToken;
            }
            else {
                return null;
            }
        }
        else {
            return pstrAccessToken;
        }
    }
    
    //Generate access token by invoking token endpoint, used by getAccessToken only
    private boolean genAccessToken() {
        StringBuffer sbResponse = new StringBuffer();    
        boolean bSuccess = false;
        String strData = "";
        String strConnectUrl = "";
        HttpsURLConnection conn = null;
        DataOutputStream dos = null;
        InputStream is = null;

        try{
           // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLSv1.2"); //pass in TLSv1.2
            sc.init(null, trustAllCerts, new java.security.SecureRandom());	      

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);	

//            String strClientId = "CrWrC7u7Qv0SRmrLYf1aAMOo6j4a";	//Example from Development Demo app - Use your own Client ID
//            String strClientSecret = "Odxg9dF4BQnOW2sfXcjvx3FT7REa";	//Example from Development Demo app - Use your own Client Secret
//            String strApiInvoke = "https://apidev.sains.com.my/token";
            Debug.printFrameworkDebug("this.sSxcApplicationName:"+this.sSxcApplicationName);
//            String strClientId = oResrcInfo.getResrcSmartXChangeConf(this.sSxcApplicationName+".CONSUMER_KEY");  //Example from Development Demo app - Use your own Client ID
//            String strClientSecret = oResrcInfo.getResrcSmartXChangeConf(this.sSxcApplicationName+".CONSUMER_SECRET");	//Example from Development Demo app - Use your own Client Secret
//            String strApiInvoke = oResrcInfo.getResrcSmartXChangeConf(this.sSxcApplicationName+".URL_GENERATE_ACCESS_TOKEN");
            String strClientId = smartXChangeSetup.CONSUMER_KEY[SystemConstants.ENV];  //Example from Development Demo app - Use your own Client ID
            String strClientSecret = smartXChangeSetup.CONSUMER_SECRET[SystemConstants.ENV];	//Example from Development Demo app - Use your own Client Secret
            String strApiInvoke = smartXChangeSetup.TOKEN_URL[SystemConstants.ENV];

            Debug.printFrameworkDebug("genAccessToken strClientId:"+strClientId);
            Debug.printFrameworkDebug("genAccessToken strClientSecret:"+strClientSecret);
            Debug.printFrameworkDebug("genAccessToken strApiInvoke:"+strApiInvoke);
            
            URL obj = new URL(strApiInvoke);
            conn = (HttpsURLConnection) obj.openConnection();			  
            conn.setSSLSocketFactory(sc.getSocketFactory());

            conn.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((strClientId + ":" + strClientSecret).getBytes()));
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("grant_type", "client_credentials");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            strData = "grant_type=client_credentials";

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(strData);
            dos.flush();

            is = conn.getInputStream();   

            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            //Debug.printFrameworkDebug("API Result: "+result.toString("UTF-8"));

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(result.toString());
            Object jsonElement;

            jsonElement = jsonObject.get("access_token");
            if (jsonElement != null) {
                    pstrAccessToken = jsonElement.toString();
            }
            jsonElement = jsonObject.get("expires_in");
            if (jsonElement != null) {
                    plExpiryTimestamp = (System.currentTimeMillis() / 1000) + Long.parseLong(jsonElement.toString());
            }

            //String resScope = "";
            //String resTokenType = "";
            //resScope = jsonObject.get("scope").toString();
            //resTokenType = jsonObject.get("token_type").toString();
            //Debug.printFrameworkDebug(resScope);
            //Debug.printFrameworkDebug(resTokenType);

            int responseCode = conn.getResponseCode();
            if(responseCode == 200){

                    bSuccess = true;
            } //end of responseCode
            else{
                    bSuccess = false;
                    strData = responseCode+"";
            }
            conn.disconnect();
        }catch(Exception e){
            e.printStackTrace();
            //throw new Exception(e.getMessage());
            return false;
        }
        finally { 
            conn.disconnect();
        }
        return true;
    }

    
    //GET: CALL SMARTXCHANGE API
    public String callSXCReadDataGet(String strSXCApiUrl, String strAid)throws Exception{
        //boolean bSuccess = false;
        HttpsURLConnection conn = null;
        DataOutputStream dos = null;
        InputStream is = null;
        String overall = "";
        String apistatus = "";
        String apimessage = "";
        
        JSONObject oJSData = new JSONObject();
        try{
           // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLSv1.2"); //pass in TLSv1.2
            sc.init(null, trustAllCerts, new java.security.SecureRandom());	      

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);	

            String strApiEndpoint = strSXCApiUrl;
            //String strApiContext = "/msg";
            //String strApiParam = "/" + strUid;
            //String strApiInvoke = strApiEndpoint + strApiContext + strApiParam;
            String strApiInvoke = strApiEndpoint;
                    
            URL obj = new URL(strApiInvoke);
            conn = (HttpsURLConnection) obj.openConnection();			  
            conn.setSSLSocketFactory(sc.getSocketFactory());

            String strAccessToken = getAccessToken();
            Debug.printFrameworkDebug("strAccessToken: "+strAccessToken);
            
            if(strAccessToken != null && !strAccessToken.equals("")){
                
                if(!strAid.equals("")){
                    conn.setRequestProperty("X-App-Token", strAid);
                }
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + strAccessToken);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    is = conn.getInputStream();   

                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                    }

                    overall = result.toString("UTF-8");
                    Debug.printFrameworkDebug("API Result: "+result.toString("UTF-8"));

                }
                else if (responseCode == 400) {
                    throw new Exception("error:"+responseCode);
                } //end of responseCode
                else{
                    throw new Exception("general error:"+responseCode);
                        //strData = responseCode+"";
                }
                conn.disconnect();	
            }else{
                throw new Exception ("No Token Found!");
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        finally { 
            conn.disconnect();
        }
        
        return overall;
    }

    //POST: CALL SMARTXCHANGE API
    public String callSXCSendDataPost(String strSXCApiUrl, String sJsonData, String strAid)throws Exception{
        //boolean bSuccess = false;
        HttpsURLConnection conn = null;
        DataOutputStream dos = null;
        InputStream is = null;
        String overall = "";
        String strData = "";
        
        JSONObject oJSData = new JSONObject();
        try{
           // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLSv1.2"); //pass in TLSv1.2
            sc.init(null, trustAllCerts, new java.security.SecureRandom());	      

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);	

            String strApiEndpoint = strSXCApiUrl;
            //String strApiContext = "/msg";
            //String strApiParam = "/" + strUid;
            //String strApiInvoke = strApiEndpoint + strApiContext + strApiParam;
            String strApiInvoke = strApiEndpoint;
                    
            URL obj = new URL(strApiInvoke);
            conn = (HttpsURLConnection) obj.openConnection();			  
            conn.setSSLSocketFactory(sc.getSocketFactory());

            String strAccessToken = getAccessToken();
            Debug.printFrameworkDebug("strAccessToken: "+strAccessToken);
            
            if(strAccessToken != null && !strAccessToken.equals("")){
                
                // Add headers
                if(!strAid.equals("")){
                    conn.setRequestProperty("X-App-Token", strAid);
                }
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + strAccessToken);
                conn.setRequestProperty("Content-Type", "application/json"); // We send our data in JSON format
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                
                Debug.printFrameworkDebug("JsonData => "+sJsonData.toString());
                //SEND DATA
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(sJsonData.toString());
                dos.flush();

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    is = conn.getInputStream();   

                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                    }

                    overall = result.toString("UTF-8");
                    Debug.printFrameworkDebug("API Result: "+result.toString("UTF-8"));

                } else if (responseCode == 400) {
                    throw new Exception("error:"+responseCode);
                }  else{
                    throw new Exception("general error:"+responseCode);
                        //strData = responseCode+"";
                }
                conn.getInputStream().close();
                conn.disconnect();	
            }else{
                throw new Exception ("No Token Found!");
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        finally { 
            conn.disconnect();
        }
        
        return overall;
    }
    
    public String callSXCSendDataPostParamAdv(String apiUrl, String strSXCApiUrl, List<NameValuePair> urlParameters, String strAid, int iAddXtraHeader) throws Exception {
        String result;
        String strApiResult = "";
        String overall="";
        HttpsURLConnection conn = null;
        InputStream is = null;
        BufferedReader br = null;
        try{
           // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLSv1.2"); //pass in TLSv1.2
            sc.init(null, trustAllCerts, new java.security.SecureRandom());	      

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);	

            //String strApiEndpoint = strSXCApiUrl;
            //String strApiContext = "/msg";
            //String strApiParam = "/" + strUid;
            //String strApiInvoke = strApiEndpoint + strApiContext + strApiParam;
            String strApiInvoke = smartXChangeSetup.BASE_URL[SystemConstants.ENV];
                    
            System.out.println("strApiInvoke URL:"+strApiInvoke);
            URL obj = new URL(strApiInvoke);
            conn = (HttpsURLConnection) obj.openConnection();			  
            conn.setSSLSocketFactory(sc.getSocketFactory());

            String strAccessToken = getAccessToken();
            System.out.println("strAccessToken: "+strAccessToken);
            
            if(strAccessToken != null && !strAccessToken.equals("")){
                
                // Add headers
                if(!strAid.equals("")){
                    conn.setRequestProperty("X-App-Token", strAid);
                }
                //append header
                if(iAddXtraHeader == 1){
                    if(hmReqHeader == null){
                        throw new Exception ("Additional Request Header is required");
                    }else{
                        for (String i : hmReqHeader.keySet()) {
                            conn.setRequestProperty(i, hmReqHeader.get(i));    
                        }
                    }
                }
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + strAccessToken);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                
                //SEND x-www-form-urlencoded
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(urlParameters));
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    is = conn.getInputStream();   

                    ByteArrayOutputStream result1 = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1) {
                        result1.write(buffer, 0, length);
                    }

                    overall = result1.toString("UTF-8");
                    System.out.println("API Result: "+result1.toString("UTF-8"));

                } else if (responseCode == 400) {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    String strCurrentLine;
                    String strErrMsg = "";
                    while ((strCurrentLine = br.readLine()) != null) {
                        strErrMsg += strCurrentLine;
                    }
                    System.out.println("error:"+strErrMsg);
                    br.close();
                    throw new Exception("sXc error:"+responseCode+": "+strErrMsg);
                                        
                }  else{
                    is = conn.getInputStream();   

                    ByteArrayOutputStream result1 = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1) {
                        result1.write(buffer, 0, length);
                    }

                    overall = result1.toString("UTF-8");
                    System.out.println("API Error Result: "+result1.toString("UTF-8"));
                    throw new Exception("general error:"+responseCode+"::"+result1.toString("UTF-8"));
                        //strData = responseCode+"";
                }
                conn.getInputStream().close();
                conn.disconnect();	
            }else{
                throw new Exception ("No Token Found!");
            }
        }catch(Exception e){
            e.printStackTrace();
            throw (e);
        }
        finally { 
            conn.disconnect();
        }

        return overall;
        
    }
    public String callSXCSendDataPostParamAdv2(String apiUrl, List<NameValuePair> urlParameters, String strAid, int iAddXtraHeader) throws Exception {
        String result;
        String strApiResult = "";
        String overall="";
        HttpsURLConnection conn = null;
        InputStream is = null;
        BufferedReader br = null;
        try{
           // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLSv1.2"); //pass in TLSv1.2
            sc.init(null, trustAllCerts, new java.security.SecureRandom());	      

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);	

            //String strApiEndpoint = strSXCApiUrl;
            //String strApiContext = "/msg";
            //String strApiParam = "/" + strUid;
            //String strApiInvoke = strApiEndpoint + strApiContext + strApiParam;
            String strApiInvoke = apiUrl;
                    
            System.out.println("strApiInvoke URL:"+strApiInvoke);
            URL obj = new URL(strApiInvoke);
            conn = (HttpsURLConnection) obj.openConnection();			  
            conn.setSSLSocketFactory(sc.getSocketFactory());

            
                
                // Add headers
                if(!strAid.equals("")){
                    conn.setRequestProperty("X-App-Token", strAid);
                }
                //append header
                if(iAddXtraHeader == 1){
                    if(hmReqHeader == null){
                        throw new Exception ("Additional Request Header is required");
                    }else{
                        for (String i : hmReqHeader.keySet()) {
                            conn.setRequestProperty(i, hmReqHeader.get(i));    
                        }
                    }
                }
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // We send our data in JSON format
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                
                //SEND x-www-form-urlencoded
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(urlParameters));
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    is = conn.getInputStream();   

                    ByteArrayOutputStream result1 = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1) {
                        result1.write(buffer, 0, length);
                    }

                    overall = result1.toString("UTF-8");
                    System.out.println("API Result: "+result1.toString("UTF-8"));

                } else if (responseCode == 400) {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    String strCurrentLine;
                    String strErrMsg = "";
                    while ((strCurrentLine = br.readLine()) != null) {
                        strErrMsg += strCurrentLine;
                    }
                    System.out.println("error:"+strErrMsg);
                    br.close();
                    throw new Exception("sXc error:"+responseCode+": "+strErrMsg);
                                        
                }  else{
                    is = conn.getInputStream();   

                    ByteArrayOutputStream result1 = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1) {
                        result1.write(buffer, 0, length);
                    }

                    overall = result1.toString("UTF-8");
                    System.out.println("API Error Result: "+result1.toString("UTF-8"));
                    throw new Exception("general error:"+responseCode+"::"+result1.toString("UTF-8"));
                        //strData = responseCode+"";
                }
                conn.getInputStream().close();
                conn.disconnect();	
        }catch(Exception e){
            e.printStackTrace();
            throw (e);
        }
        finally { 
            conn.disconnect();
        }

        return overall;
        
    }
    
    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        System.out.println("result param:"+result.toString());
        return result.toString();
    }
    
    private HashMap<String, String> hmReqHeader = new HashMap<String, String>();
    public HashMap<String, String> getHmReqHeader() {
        return hmReqHeader;
    }
    public void setHmReqHeader(HashMap<String, String> hmReqHeader) {
        this.hmReqHeader = hmReqHeader;
    }
    
    //POST: CALL SMARTXCHANGE API
    public String callSXCSendDataDelete(String strSXCApiUrl, String sJsonData, String strAid)throws Exception{
        //boolean bSuccess = false;
        HttpsURLConnection conn = null;
        DataOutputStream dos = null;
        InputStream is = null;
        String overall = "";
        String strData = "";
        
        JSONObject oJSData = new JSONObject();
        try{
           // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLSv1.2"); //pass in TLSv1.2
            sc.init(null, trustAllCerts, new java.security.SecureRandom());	      

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);	

            String strApiEndpoint = strSXCApiUrl;
            //String strApiContext = "/msg";
            //String strApiParam = "/" + strUid;
            //String strApiInvoke = strApiEndpoint + strApiContext + strApiParam;
            String strApiInvoke = strApiEndpoint;
                    
            URL obj = new URL(strApiInvoke);
            conn = (HttpsURLConnection) obj.openConnection();			  
            conn.setSSLSocketFactory(sc.getSocketFactory());

            String strAccessToken = getAccessToken();
            Debug.printFrameworkDebug("strAccessToken: "+strAccessToken);
            
            if(strAccessToken != null && !strAccessToken.equals("")){
                
                // Add headers
                if(!strAid.equals("")){
                    conn.setRequestProperty("X-App-Token", strAid);
                }
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + strAccessToken);
                conn.setRequestProperty("Content-Type", "application/json"); // We send our data in JSON format
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("DELETE");
                
                Debug.printFrameworkDebug("JsonData => "+sJsonData.toString());
                //SEND DATA
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(sJsonData.toString());
                dos.flush();

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    is = conn.getInputStream();   

                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                    }

                    overall = result.toString("UTF-8");
                    Debug.printFrameworkDebug("API Result: "+result.toString("UTF-8"));

                } else if (responseCode == 400) {
                    throw new Exception("error:"+responseCode);
                }  else{
                    throw new Exception("general error:"+responseCode);
                        //strData = responseCode+"";
                }
                conn.getInputStream().close();
                conn.disconnect();	
            }else{
                throw new Exception ("No Token Found!");
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        finally { 
            conn.disconnect();
        }
        
        return overall;
    }
    
    //SEND NOTIFICATION TO CYBIX
    public boolean sendNotification(String strAid, String strUid, String strId, String strTitle, String strMsg)throws Exception{
        boolean bSuccess = false;
        String strData = "";
        HttpsURLConnection conn = null;
        DataOutputStream dos = null;
        InputStream is = null;

        try{
           // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLSv1.2"); //pass in TLSv1.2
            sc.init(null, trustAllCerts, new java.security.SecureRandom());	      

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                            return true;
                    }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);	

            String strApiEndpoint = "https://apidev.sains.com.my/cyBixAppNotification/v1.0";
            String strApiContext = "/msg";
            String strApiParam = "/" + strUid;
            String strApiInvoke = strApiEndpoint + strApiContext + strApiParam;

            URL obj = new URL(strApiInvoke);
            conn = (HttpsURLConnection) obj.openConnection();			  
            conn.setSSLSocketFactory(sc.getSocketFactory());

            String strAccessToken = getAccessToken();
            conn.setRequestProperty("X-App-Token", strAid);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + strAccessToken);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            strData = "id=" + strId + "&title=" + strTitle + "&msg=" + strMsg;

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(strData);
            dos.flush();

            int responseCode = conn.getResponseCode();
            if (responseCode == 201) {
                is = conn.getInputStream();   

                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                }

                Debug.printFrameworkDebug("API Result: "+result.toString("UTF-8"));

                bSuccess = true;
            } //end of responseCode
            else if (responseCode == 400) {
                    Debug.printFrameworkDebug("Detected 400");				
            }
            else {
                    bSuccess = false;
                    strData = responseCode+"";
                    Debug.printFrameworkDebug("Others: " + strData);
            }
            conn.disconnect();
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        finally { 
            conn.disconnect();
        }
        return bSuccess;
    }

    

    //Compress the data to to save bandwidth (may need this in future)
    //can use like following code:
    //START: Compress the data to save bandwidth
    //      byte[] compressedData = compress(data.toString());
    private static byte[] compress(final String str) throws IOException {
        if (str == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
        gzip.write(str.getBytes("UTF-8"));
        gzip.close();
        return outputStream.toByteArray();
    }
    
    public String getsSxcApplicationName() {
        return sSxcApplicationName;
    }

    public void setsSxcApplicationName(String sSxcApplicationName) {
        this.sSxcApplicationName = sSxcApplicationName;
    }

    
    private static class DefaultTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        public X509Certificate[] getAcceptedIssuers() {
                return null;
        }
    }

    /*public static void main(String args[]) throws Exception {
        String strAid = "lNkbhUbLfbUr2fJjjqpI8SY";	//Travel Request
        String strUid = "hilarwky";
        String strId = "101";
        String strTitle = "Travel Request";
        String strMsg = "Hello World from Java";

        //readMsg(strAid, strUid);
        //sendNotification(strAid, strUid, strId, strTitle, strMsg);
    }*/
    
    public SystemConstants.SmartXChange.SETUP getSmartXChangeSetup() {
        return smartXChangeSetup;
    }

    public void setSmartXChangeSetup(SystemConstants.SmartXChange.SETUP aoResrcInfo) {
        smartXChangeSetup = aoResrcInfo;
    }
}
