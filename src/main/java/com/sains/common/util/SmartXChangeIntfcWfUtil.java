/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sains.common.util;

//import com.mrpe.api.util.ReadResourceFiles;
import com.sains.framework.base.LogFunction;
import java.io.*;
import java.util.HashMap;
import java.util.Base64;
import java.net.*;
import javax.net.ssl.*;
import java.security.cert.*;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import org.apache.http.NameValuePair;
import org.apache.logging.log4j.LogManager;
import org.json.simple.*;
import org.json.simple.parser.*;
/**
 *
 * @author ela2sains
 */
public class SmartXChangeIntfcWfUtil {

//    private static String pstrAccessToken = "";
//    private static long plExpiryTimestamp = 0;
//    private String sSxcApplicationName = "";
    private static Map<String, Map> accessTokenMap = new HashMap();
    //private ReadResourceFiles oResrcInfo = new ReadResourceFiles();
    
    public SmartXChangeIntfcWfUtil() {
        
    }
    
    public SmartXChangeIntfcWfUtil(HashMap<String,String> hmReqHeader) {
        //this is for Advance API with additional header use
        this.setHmReqHeader(hmReqHeader);
    }
    
    //Return access token for API invocation
    public String getAccessToken(String apiSystem) {
        if (accessTokenMap.get(apiSystem) == null || (System.currentTimeMillis() / 1000) >= (Long)accessTokenMap.get(apiSystem).get("plExpiryTimestamp")) {	//Expired
            if (genAccessToken(apiSystem)) {
                return (String)accessTokenMap.get(apiSystem).get("access_token");
            }
            else {
                return null;
            }
        }
        else {
            return (String)accessTokenMap.get(apiSystem).get("access_token");
        }
    }
    
    //Generate access token by invoking token endpoint, used by getAccessToken only
    private boolean genAccessToken(String apiSystem) {
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
//            System.out.println("this.sSxcApplicationName:"+apiSystem);
//            String strClientId = oResrcInfo.GetResrcSmartXChangeConf("sxc."+apiSystem+".CONSUMER_KEY");  //Example from Development Demo app - Use your own Client ID
//            String strClientSecret = oResrcInfo.GetResrcSmartXChangeConf("sxc."+apiSystem+".CONSUMER_SECRET");	//Example from Development Demo app - Use your own Client Secret
//            String strApiInvoke = oResrcInfo.GetResrcSmartXChangeConf("sxc."+apiSystem+".URL_GENERATE_ACCESS_TOKEN");
            String strClientId = SystemConstants.DOMAIN.ISM_CONSUMER_KEY;  
            String strClientSecret = SystemConstants.DOMAIN.ISM_CONSUMER_SECRET;	
            String strApiInvoke = SystemConstants.DOMAIN.ISM_URL_GENERATE_ACCESS_TOKEN;

            LogManager.getLogger().debug("###ISM genAccessToken strClientId:"+strClientId);
            LogManager.getLogger().debug("###ISM genAccessToken strClientSecret:"+strClientSecret);
            LogManager.getLogger().debug("###ISM genAccessToken strApiInvoke:"+strApiInvoke);
            
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

            //System.out.println("API Result: "+result.toString("UTF-8"));

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(result.toString());
            Object jsonElement;
            jsonElement = jsonObject.get("expires_in");
            if (jsonElement != null) {
                Map map = new HashMap();
                map.put("plExpiryTimestamp", (Long)(System.currentTimeMillis() / 1000) + Long.parseLong(jsonElement.toString()));
                map.put("access_token", jsonObject.get("access_token"));
                accessTokenMap.put(apiSystem, map);
            }

            //String resScope = "";
            //String resTokenType = "";
            //resScope = jsonObject.get("scope").toString();
            //resTokenType = jsonObject.get("token_type").toString();
            //System.out.println(resScope);
            //System.out.println(resTokenType);

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
    public String callSXCReadDataGet(String apiSystem, String strSXCApiUrl, String strAid)throws Exception{
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

//            String strApiEndpoint = strSXCApiUrl;
            //String strApiContext = "/msg";
            //String strApiParam = "/" + strUid;
            //String strApiInvoke = strApiEndpoint + strApiContext + strApiParam;
//            String strApiInvoke = oResrcInfo.GetResrcSmartXChangeConf("sxc."+apiSystem+".URL_GENERATE_ACCESS_TOKEN");
            String strApiInvoke = SystemConstants.DOMAIN.ISM_URL_GENERATE_ACCESS_TOKEN;
            URL obj = new URL(strApiInvoke);
            conn = (HttpsURLConnection) obj.openConnection();			  
            conn.setSSLSocketFactory(sc.getSocketFactory());

            String strAccessToken = getAccessToken(apiSystem);
            System.out.println("strAccessToken: "+strAccessToken);
            
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
                    System.out.println("API Result: "+result.toString("UTF-8"));

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
    public String callSXCSendDataPost(String apiSystem, String strSXCApiUrl, String sJsonData, String strAid)throws Exception{
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

//            String strApiEndpoint = strSXCApiUrl;
            //String strApiContext = "/msg";
            //String strApiParam = "/" + strUid;
            //String strApiInvoke = strApiEndpoint + strApiContext + strApiParam;
//            String strApiInvoke = oResrcInfo.GetResrcSmartXChangeConf("sxc."+apiSystem+".CONTEXT") + strSXCApiUrl;
            String strApiInvoke = SystemConstants.DOMAIN.ISM_URL_CONTEXT+ strSXCApiUrl;
                    
            URL obj = new URL(strApiInvoke);
            conn = (HttpsURLConnection) obj.openConnection();			  
            conn.setSSLSocketFactory(sc.getSocketFactory());

            String strAccessToken = getAccessToken(apiSystem);
            System.out.println("strAccessToken: "+strAccessToken);
            
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
                
                System.out.println("JsonData => "+sJsonData.toString());
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
                    System.out.println("API Result: "+result.toString("UTF-8"));

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
    
    //POST: CALL SMARTXCHANGE API
    public String callSXCSendDataDelete(String apiSystem, String strSXCApiUrl, String sJsonData, String strAid)throws Exception{
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

//            String strApiEndpoint = strSXCApiUrl;
            //String strApiContext = "/msg";
            //String strApiParam = "/" + strUid;
            //String strApiInvoke = strApiEndpoint + strApiContext + strApiParam;
//            String strApiInvoke = oResrcInfo.GetResrcSmartXChangeConf("sxc."+apiSystem+".URL_GENERATE_ACCESS_TOKEN");
            String strApiInvoke = SystemConstants.DOMAIN.ISM_URL_GENERATE_ACCESS_TOKEN;
                    
            URL obj = new URL(strApiInvoke);
            conn = (HttpsURLConnection) obj.openConnection();			  
            conn.setSSLSocketFactory(sc.getSocketFactory());

            String strAccessToken = getAccessToken(apiSystem);
            System.out.println("strAccessToken: "+strAccessToken);
            
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
                
                System.out.println("JsonData => "+sJsonData.toString());
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
                    System.out.println("API Result: "+result.toString("UTF-8"));

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
    
    
    //GET: CALL SMARTXCHANGE API
    private HashMap<String, String> hmReqHeader = new HashMap<String, String>();
    public HashMap<String, String> getHmReqHeader() {
        return hmReqHeader;
    }
    public void setHmReqHeader(HashMap<String, String> hmReqHeader) {
        this.hmReqHeader = hmReqHeader;
    }
    
    public String callSXCReadDataGetAdv(String apiSystem, String strSXCApiUrl, String strAid, int iAddXtraHeader)throws Exception{
        //boolean bSuccess = false;
        HttpsURLConnection conn = null;
        DataOutputStream dos = null;
        InputStream is = null;
        BufferedReader br = null;
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

//            String strApiEndpoint = strSXCApiUrl;
            //String strApiContext = "/msg";
            //String strApiParam = "/" + strUid;
            //String strApiInvoke = strApiEndpoint + strApiContext + strApiParam;
//            String strApiInvoke = oResrcInfo.GetResrcSmartXChangeConf("sxc."+apiSystem+".URL_GENERATE_ACCESS_TOKEN");
            String strApiInvoke = SystemConstants.DOMAIN.ISM_URL_GENERATE_ACCESS_TOKEN;
                    
            URL obj = new URL(strApiInvoke);
            conn = (HttpsURLConnection) obj.openConnection();			  
            conn.setSSLSocketFactory(sc.getSocketFactory());
            System.out.println("strApiInvoke URL:"+strApiInvoke);
            
            String strAccessToken = getAccessToken(apiSystem);
            System.out.println("strAccessToken: "+strAccessToken);
            
            if(strAccessToken != null && !strAccessToken.equals("")){
                
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
                    System.out.println("API Result: "+result.toString("UTF-8"));

                }
                else if (responseCode == 400) {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    String strCurrentLine;
                    String strErrMsg = "";
                    while ((strCurrentLine = br.readLine()) != null) {
                        strErrMsg += strCurrentLine;
                    }
                    System.out.println("error:"+strErrMsg);
                    br.close();
                    throw new Exception("sXc error:"+responseCode+": "+strErrMsg);
                } //end of responseCode
                else{
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    String strCurrentLine;
                    String strErrMsg = "";
                    while ((strCurrentLine = br.readLine()) != null) {
                        strErrMsg += strCurrentLine;
                    }
                    System.out.println("error:"+strErrMsg);
                    br.close();
                    throw new Exception("general error:"+responseCode+":"+strErrMsg);
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
    public String callSXCSendDataPostJSONAdv(String apiSystem, String strSXCApiUrl, String sJsonData, String strAid, int iAddXtraHeader)throws Exception{
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

//            String strApiEndpoint = strSXCApiUrl;
            //String strApiContext = "/msg";
            //String strApiParam = "/" + strUid;
            //String strApiInvoke = strApiEndpoint + strApiContext + strApiParam;
//            String strApiInvoke = strApiEndpoint;
//            String strApiInvoke = oResrcInfo.GetResrcSmartXChangeConf("sxc."+apiSystem+".CONTEXT") + strSXCApiUrl;        
            String strApiInvoke = SystemConstants.DOMAIN.ISM_URL_CONTEXT + strSXCApiUrl;        
            URL obj = new URL(strApiInvoke);
            conn = (HttpsURLConnection) obj.openConnection();			  
            conn.setSSLSocketFactory(sc.getSocketFactory());

            String strAccessToken = getAccessToken(apiSystem);
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
                conn.setRequestProperty("Content-Type", "application/json"); // We send our data in JSON format
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                
                System.out.println("JsonData => "+sJsonData.toString());
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
                    System.out.println("API Result: "+result.toString("UTF-8"));

                } else if (responseCode == 400) {
                    is = conn.getInputStream();   

                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                    }

                    overall = result.toString("UTF-8");
                    System.out.println("API Result: "+result.toString("UTF-8"));
                    throw new Exception("sXc error:"+responseCode);
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
    
    public String callSXCSendDataPostParamAdv(String apiSystem, String strSXCApiUrl, List<NameValuePair> urlParameters, String strAid, int iAddXtraHeader) throws Exception {
        System.out.println("CALL SXC API");
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
//            System.out.println("strSXCApiUrl:"+strSXCApiUrl);
//            String strApiInvoke = oResrcInfo.GetResrcSmartXChangeConf("sxc."+apiSystem+".CONTEXT") + strSXCApiUrl;
            String strApiInvoke = SystemConstants.DOMAIN.ISM_URL_CONTEXT + strSXCApiUrl;
                    
            LogManager.getLogger().debug("###ISM API url:"+strApiInvoke);
            new LogFunction().printDebug("###ISM API url:"+strApiInvoke);
            URL obj = new URL(strApiInvoke);
            conn = (HttpsURLConnection) obj.openConnection();			  
            conn.setSSLSocketFactory(sc.getSocketFactory());

            String strAccessToken = getAccessToken(apiSystem);
            LogManager.getLogger().debug("###ISM API strAccessToken:"+strAccessToken);
            LogManager.getLogger().debug("==================================================================");
            new LogFunction().printDebug("###ISM API strAccessToken:"+strAccessToken);
            
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
                    new LogFunction().printDebug("###200 RECEIVED");
                    is = conn.getInputStream();   

                    ByteArrayOutputStream result1 = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1) {
                        result1.write(buffer, 0, length);
                    }

                    overall = result1.toString("UTF-8");
//                    LogManager.getLogger().debug("callSXCSendDataPostParamAdv >>>  API Result: "+result1.toString("UTF-8"));
                    new LogFunction().printDebug("callSXCSendDataPostParamAdv >>>  API Result: "+result1.toString("UTF-8"));


                } else if (responseCode == 400) {
                    new LogFunction().printDebug("###400RECEIVED");
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    String strCurrentLine;
                    String strErrMsg = "";
                    while ((strCurrentLine = br.readLine()) != null) {
                        strErrMsg += strCurrentLine;
                    }
                    LogManager.getLogger().debug("###ISM callSXCSendDataPostParamAdv >>>  error:"+strErrMsg);
                    new LogFunction().printDebug("###ISM callSXCSendDataPostParamAdv >>>  error:"+strErrMsg);
                    br.close();
                    throw new Exception("###ISM sXc error:"+responseCode+": "+strErrMsg);
                
                }  else if (responseCode == 401) {
                    LogManager.getLogger().debug("###ISM callSXCSendDataPostParamAdv >>>  == inside 401, regenerating token and repost");
                    new LogFunction().printDebug("###ISM callSXCSendDataPostParamAdv >>>  == inside 401, regenerating token and repost");
                    if (genAccessToken(apiSystem)) {
                        LogManager.getLogger().debug("###ISM callSXCSendDataPostParamAdv >>>  == success genAccessToken");
                        LogManager.getLogger().debug("###ISM callSXCSendDataPostParamAdv >>>  == repost starting");
                        overall = repostSXCSendDataPostParamAdv(apiSystem, strSXCApiUrl, urlParameters, strAid, iAddXtraHeader);
                    }
                    
                }  else{
                    is = conn.getInputStream();   

                    ByteArrayOutputStream result1 = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1) {
                        result1.write(buffer, 0, length);
                    }
                    overall = result1.toString("UTF-8");
                    LogManager.getLogger().debug("callSXCSendDataPostParamAdv >>>  API Error Result: "+result1.toString("UTF-8"));
                    new LogFunction().printDebug("callSXCSendDataPostParamAdv >>>  API Error Result: "+result1.toString("UTF-8"));
                    new LogFunction().printDebug("callSXCSendDataPostParamAdv >>>  API Error Response: "+responseCode);
                    throw new Exception("general error:"+responseCode);
                        //strData = responseCode+"";
                }
                conn.getInputStream().close();
                conn.disconnect();	
            }else{
                new LogFunction().printDebug("No token found");
                throw new Exception ("No Token Found!");
            }
        }catch(Exception e){
            System.out.println("[SXC EXCEPTION] -> " + e.toString());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        finally { 
            conn.disconnect();
        }

        return overall;
        
    }
    
    public String repostSXCSendDataPostParamAdv(String apiSystem, String strSXCApiUrl, List<NameValuePair> urlParameters, String strAid, int iAddXtraHeader) throws Exception {
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
//            String strApiInvoke = oResrcInfo.GetResrcSmartXChangeConf("sxc."+apiSystem+".CONTEXT") + strSXCApiUrl;
            String strApiInvoke = SystemConstants.DOMAIN.ISM_URL_CONTEXT+ strSXCApiUrl;
                    
            LogManager.getLogger().debug("###ISM repostSXCSendDataPostParamAdv >>> strApiInvoke URL:"+strApiInvoke);
            URL obj = new URL(strApiInvoke);
            conn = (HttpsURLConnection) obj.openConnection();			  
            conn.setSSLSocketFactory(sc.getSocketFactory());

            String strAccessToken = getAccessToken(apiSystem);
            LogManager.getLogger().debug("###ISM repostSXCSendDataPostParamAdv >>> strAccessToken: "+strAccessToken);
            
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
                    LogManager.getLogger().debug("###ISM API Result: "+result1.toString("UTF-8"));

                } else if (responseCode == 400) {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    String strCurrentLine;
                    String strErrMsg = "";
                    while ((strCurrentLine = br.readLine()) != null) {
                        //System.out.println("strCurrentLine:"+strCurrentLine);
                        strErrMsg += strCurrentLine;
                    }
                    LogManager.getLogger().debug("###ISM error:"+strErrMsg);
                    br.close();
                    throw new Exception("###ISM sXc error:"+responseCode+": "+strErrMsg);
                    
                } else if (responseCode == 401) {
                    LogManager.getLogger().debug("###ISM repostSXCSendDataPostParamAdv >>> == inside 401, regenerating token and repost");
                    if (genAccessToken(apiSystem)) {
                        LogManager.getLogger().debug("###ISM repostSXCSendDataPostParamAdv >>> == success genAccessToken");
                        LogManager.getLogger().debug("###ISM repostSXCSendDataPostParamAdv >>> == will not repost");
                        //repostSXCSendDataPostParamAdv(apiSystem, strSXCApiUrl, urlParameters, strAid, iAddXtraHeader);
                    }
                } else{
                    is = conn.getInputStream();   

                    ByteArrayOutputStream result1 = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1) {
                        result1.write(buffer, 0, length);
                    }

                    overall = result1.toString("UTF-8");
                    LogManager.getLogger().debug("###ISM repostSXCSendDataPostParamAdv >>> API Error Result: "+result1.toString("UTF-8"));
                    throw new Exception("general error:"+responseCode);
                        //strData = responseCode+"";
                }
                conn.getInputStream().close();
                conn.disconnect();	
            }else{
                throw new Exception ("No Token Found!");
            }
        }catch(Exception e){
//            e.printStackTrace();
//            throw new Exception(e.getMessage());
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
//        System.out.println("result param:"+result.toString());
        return result.toString();
    }
    
    //POST: CALL SMARTXCHANGE API
    public String callSXCSendDataDeleteAdv(String apiSystem, String strSXCApiUrl, String sJsonData, String strAid, int iAddXtraHeader)throws Exception{
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

            String strAccessToken = getAccessToken(apiSystem);
            System.out.println("###ISM strAccessToken: "+strAccessToken);
            
            if(strAccessToken != null && !strAccessToken.equals("")){
                
                // Add headers
                if(iAddXtraHeader == 1){
                    if(!strAid.equals("")){
                        conn.setRequestProperty("X-App-Token", strAid);
                    }
                    //append header
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
                conn.setRequestProperty("Content-Type", "application/json"); // We send our data in JSON format
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("DELETE");
                
                System.out.println("JsonData => "+sJsonData.toString());
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
                    System.out.println("API Result: "+result.toString("UTF-8"));

                } else if (responseCode == 400) {
                    throw new Exception("sXc error:"+responseCode);
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
    public boolean sendNotification(String apiSystem, String strAid, String strUid, String strId, String strTitle, String strMsg)throws Exception{
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

            String strAccessToken = getAccessToken(apiSystem);
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

                System.out.println("API Result: "+result.toString("UTF-8"));

                bSuccess = true;
            } //end of responseCode
            else if (responseCode == 400) {
                    System.out.println("Detected 400");				
            }
            else {
                    bSuccess = false;
                    strData = responseCode+"";
                    System.out.println("Others: " + strData);
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
}
