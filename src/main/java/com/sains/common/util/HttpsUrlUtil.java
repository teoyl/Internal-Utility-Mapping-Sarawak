/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sains.common.util;

import com.sains.framework.base.Debug;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author ela2sains
 */
public class HttpsUrlUtil {

    public HttpsUrlUtil() {
    }
    
    public static String https_selfSignCallUrlGet(String strUrl) throws Exception {
        String result;
        String overall="";
        CloseableHttpResponse resp = null;
        try {
            try {
                SSLContext sslContext = new SSLContextBuilder()
                        .loadTrustMaterial(null, (certificate, authType) -> true).build();
                org.apache.http.impl.client.CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
                        .build();
                HttpGet httpGet = new HttpGet(strUrl);
                resp = httpClient.execute(httpGet);
                
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(resp.getEntity().getContent()));
                while ((result = br.readLine()) != null){
                    overall = overall + "\n" + result;
                }
//                Debug.printFrameworkDebug("https selfSign Get = " + overall);
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                throw e;
            }
            //addActionMessage("https_selfSign() no error");
        } catch (Exception e) {
            throw e;
        } finally {
            resp.close();
        }
        return overall;
    }
    
    public static String https_selfSignCallUrlPostJson(String strUrl, StringEntity entityJsonData) throws Exception {
        String result;
        String strApiResult = "";
        String overall="";
        CloseableHttpResponse resp = null;
        try {
            try {
                Debug.printFrameworkDebug("httpsApiUrl => "+strUrl);
                Debug.printFrameworkDebug("entityJsonData => "+entityJsonData);

                SSLContext sslContext = new SSLContextBuilder()
                        .loadTrustMaterial(null, (certificate, authType) -> true).build();
                org.apache.http.impl.client.CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
                        .build();
    //            HttpGet httpGet = new HttpGet(strPaymentHistApiUrl);
                
                HttpPost httpPost = new HttpPost(strUrl);
                httpPost.setEntity(entityJsonData);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
    
                resp = httpClient.execute(httpPost);

                BufferedReader br = new BufferedReader(
                    new InputStreamReader(resp.getEntity().getContent()));
                while ((strApiResult = br.readLine()) != null){
                    overall = overall + "\n" + strApiResult;
                    Debug.printFrameworkDebug("https selfSign Post = " + strApiResult);

                }
    //            System.out.println("overall = " + overall);
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            resp.close();
        }
        return overall;
    }
    
    public static String https_selfSignCallUrlPost(String strUrl, List<NameValuePair> urlParameters) throws Exception {
        String result;
        String strApiResult = "";
        String overall="";
        CloseableHttpResponse resp = null;
        try {
            try {
                Debug.printFrameworkDebug("httpsApiUrl => "+strUrl);
                Debug.printFrameworkDebug("urlParameters => "+urlParameters);

                SSLContext sslContext = new SSLContextBuilder()
                        .loadTrustMaterial(null, (certificate, authType) -> true).build();
                org.apache.http.impl.client.CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
                        .build();
    //            HttpGet httpGet = new HttpGet(strPaymentHistApiUrl);
                HttpPost httpPost = new HttpPost(strUrl);
                httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
                resp = httpClient.execute(httpPost);

                BufferedReader br = new BufferedReader(
                    new InputStreamReader(resp.getEntity().getContent()));
                while ((strApiResult = br.readLine()) != null){
                    overall = overall + "\n" + strApiResult;
                    Debug.printFrameworkDebug("https selfSign Post = " + strApiResult);

                }
    //            System.out.println("overall = " + overall);
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            resp.close();
        }
        return overall;
    }
    
    public static String https_selfSignCallUrlPost_bearerToken(String strUrl, String bearerToken) throws Exception {
        String result;
        String strApiResult = "";
        String overall="";
        CloseableHttpResponse resp = null;
        try {
            try {
                Debug.printFrameworkDebug("httpsApiUrl => "+strUrl);

                SSLContext sslContext = new SSLContextBuilder()
                        .loadTrustMaterial(null, (certificate, authType) -> true).build();
                org.apache.http.impl.client.CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
                        .build();
    //            HttpGet httpGet = new HttpGet(strPaymentHistApiUrl);
                HttpPost httpPost = new HttpPost(strUrl);
                httpPost.setHeader("Authorization", "Bearer " + bearerToken);
                httpPost.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
                httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken);
                resp = httpClient.execute(httpPost);

                BufferedReader br = new BufferedReader(
                    new InputStreamReader(resp.getEntity().getContent()));
                while ((strApiResult = br.readLine()) != null){
                    overall = overall + "\n" + strApiResult;
                    Debug.printFrameworkDebug("https selfSign Post = " + strApiResult);

                }
    //            System.out.println("overall = " + overall);
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            resp.close();
        }
        return overall;
    }
                         
    public static String https_selfSignCallUrlPost_withHeader(String strUrl, List<NameValuePair> urlParameters, Map<String, String> header) throws Exception {
        String result;
        String strApiResult = "";
        String overall="";
        CloseableHttpResponse resp = null;
        try {
            try {
                Debug.printFrameworkDebug("httpsApiUrl => "+strUrl);
                Debug.printFrameworkDebug("urlParameters => "+urlParameters);

                SSLContext sslContext = new SSLContextBuilder()
                        .loadTrustMaterial(null, (certificate, authType) -> true).build();
                org.apache.http.impl.client.CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
                        .build();
    //            HttpGet httpGet = new HttpGet(strPaymentHistApiUrl);
                HttpPost httpPost = new HttpPost(strUrl);
                if (urlParameters != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
                }
                
                if (header!=null) {
                    for (String key : header.keySet()) {
                        httpPost.addHeader(key, header.get(key));
                    }
                }
                
                resp = httpClient.execute(httpPost);

                BufferedReader br = new BufferedReader(
                    new InputStreamReader(resp.getEntity().getContent()));
                while ((strApiResult = br.readLine()) != null){
                    overall = overall + "\n" + strApiResult;
                    Debug.printFrameworkDebug("https selfSign Post = " + strApiResult);

                }
    //            System.out.println("overall = " + overall);
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            resp.close();
        }
        return overall;
    }
    
    public static String https_selfSignCallUrlPostJSON_withHeader(String strUrl, StringEntity entityJsonData, Map<String, String> header) throws Exception {
        String result;
        String strApiResult = "";
        String overall="";
        CloseableHttpResponse resp = null;
        try {
            try {
                Debug.printFrameworkDebug("httpsApiUrl => "+strUrl);
                Debug.printFrameworkDebug("entityJsonData => "+entityJsonData);

                SSLContext sslContext = new SSLContextBuilder()
                        .loadTrustMaterial(null, (certificate, authType) -> true).build();
                org.apache.http.impl.client.CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
                        .build();
    //            HttpGet httpGet = new HttpGet(strPaymentHistApiUrl);
                HttpPost httpPost = new HttpPost(strUrl);
                httpPost.setEntity(entityJsonData);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                
                if (header!=null) {
                    for (String key : header.keySet()) {
                        httpPost.addHeader(key, header.get(key));
                    }
                }
                
                resp = httpClient.execute(httpPost);

                BufferedReader br = new BufferedReader(
                    new InputStreamReader(resp.getEntity().getContent()));
                while ((strApiResult = br.readLine()) != null){
                    overall = overall + "\n" + strApiResult;
                    Debug.printFrameworkDebug("https selfSign Post = " + strApiResult);

                }
                System.out.println("overall = " + overall);
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            resp.close();
        }
        return overall;
    }
}
