package com.sains.common.util;

import com.PropertyGetter;
import com.SysConf;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionContext;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import com.sains.framework.base.SessionFactoryImpl;
import com.sains.framework.model.ApiTokenModel;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
// import java.io.*;
// import java.util.*;
// import java.util.regex.*;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
// import java.math.*;


/**
 * This class provides rounding methods on Double or Float objects
 * 
 * 
 */
public class ApiUtil {
    private static final String GET = "get";
    private static final String SET = "set";
    
    private static String getFieldName(Method method) {
        javax.persistence.Column columnAnnotation = method.getAnnotation(javax.persistence.Column.class);
        if (columnAnnotation != null) {
            return columnAnnotation.name();
        }
        return method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
    }
    
    public static String initCap(String s) {
        return Character.toUpperCase(s.charAt(0))+s.substring(1);
    }
    
    public static void mapToList(Map<String, Object> dataMap, List list, Class modelClass) throws Exception {
        for (String key : dataMap.keySet()) {
            if (key.equals("req.param")) continue; //req.param = other parameter values, not model's data
            for (Map<String, Object> mapObj : (List<Map>)dataMap.get(key)) {
                Object modelObj = modelClass.newInstance();
                list.add(modelObj);
                for (String innerKey : mapObj.keySet()) {
                    Method m = null;
                    if (mapObj.get(innerKey) instanceof Map) {
                        m = modelObj.getClass().getMethod(GET+initCap(innerKey));
                        Object innerObj = Class.forName(m.getGenericReturnType().getTypeName()).newInstance();
                        m = modelObj.getClass().getMethod(SET+initCap(innerKey), innerObj.getClass());
                        m.invoke(modelObj, innerObj);
                        mapToModel((Map)mapObj.get(innerKey), innerObj);
                    } else {
                        try {
                            m = modelObj.getClass().getMethod(SET+initCap(innerKey) + "_str", String.class);//if it has a _str, use this method
                            m.invoke(modelObj, mapObj.get(innerKey));
                        } catch (Exception e) {
                            m = modelObj.getClass().getMethod(SET+initCap(innerKey), String.class);
                            m.invoke(modelObj, mapObj.get(innerKey));
                        }
                    }
                }
            }
        }
    }
    public static void mapToModel(Map<String, Object> dataMap, Object model) throws Exception {
        for (String key : dataMap.keySet()) {
            if (key.equals("req.param")) continue; //req.param = other parameter values, not model's data
            Method m = model.getClass().getMethod(GET+initCap(key));
            if (m.getGenericReturnType().getTypeName().startsWith("java.util.List")) {
                String classWithPath = m.getGenericReturnType().getTypeName().substring(m.getGenericReturnType().getTypeName().indexOf("<")+1, m.getGenericReturnType().getTypeName().length()-1);
                List tempList = new ArrayList();
                for (Map<String, Object> mapObj : (List<Map>)dataMap.get(key)) {
                    Object modelObj = Class.forName(classWithPath).newInstance();
                    tempList.add(modelObj);
                    for (String innerKey : mapObj.keySet()) {
                        if (mapObj.get(innerKey) instanceof Map) {
                            m = modelObj.getClass().getMethod(GET+initCap(innerKey));
                            Object innerObj = Class.forName(m.getGenericReturnType().getTypeName()).newInstance();
                            m = modelObj.getClass().getMethod(SET+initCap(innerKey), innerObj.getClass());
                            m.invoke(modelObj, innerObj);
                            mapToModel((Map)mapObj.get(innerKey), innerObj);
                        } else {
                            try {
                                m = modelObj.getClass().getMethod(SET+initCap(innerKey) + "_str", String.class);//if it has a _str, use this method
                                m.invoke(modelObj, mapObj.get(innerKey));
                            } catch (Exception e) {
                                m = modelObj.getClass().getMethod(SET+initCap(innerKey), String.class);
                                m.invoke(modelObj, mapObj.get(innerKey));
                            }
                        }
                    }
                }
                m = model.getClass().getMethod(SET+initCap(key), List.class);
                m.invoke(model, tempList);
            } else {
                try {
                    m = model.getClass().getMethod(SET+initCap(key) + "_str", String.class);//if it has a _str, use this method
                    m.invoke(model, dataMap.get(key));
                } catch (Exception e) {
                    m = model.getClass().getMethod(SET+initCap(key), String.class);
                    m.invoke(model, dataMap.get(key));
                }
            }
        }
    }
    public static void jsonMapToModel(Map<String, Object> dataMap, Object model, String theModelName) throws Exception {
        if (!theModelName.endsWith(".")) theModelName+=".";
        for (String key : dataMap.keySet()) { //first loop, only find model's properties
            String tempKey = key;
            if (tempKey.startsWith(theModelName)) {
                tempKey = tempKey.substring(theModelName.length());
            }
            if (tempKey.contains(".")) continue; //try get model's data first
            Method m = model.getClass().getMethod(GET+initCap(tempKey));
            try {
                m = model.getClass().getMethod(SET+initCap(tempKey) + "_str", String.class);//if it has a _str, use this method
                m.invoke(model, dataMap.get(key));
            } catch (Exception e) {
                try {
                    m = model.getClass().getMethod(SET+initCap(tempKey), String.class);
                    m.invoke(model, dataMap.get(key));
                } catch (Exception e2) {
                    Debug.printDebug("Fail to set ["+ key +"] to " + model.getClass().getSimpleName());
                }
            }
        }
        for (String methods : dataMap.keySet()) { //first loop, only find model's properties
            String tempKey = methods;
            if (tempKey.startsWith(theModelName)) {
                tempKey = tempKey.substring(theModelName.length());
            }
            if (tempKey.equals("req.param")) continue; //req.param = other parameter values, not model's data
            if (!tempKey.contains(".")) continue; //no "." means model's data
//            Debug.printDebug("methods = " + methods);
            String[] methodArr = tempKey.split("\\.");
            Object currentObject = null;
            String currentObjectName = "";
            for (String field_name : methodArr) {
//                Debug.printDebug("fieldName = " + field_name);
                currentObjectName += (Validator.isEmpty(currentObjectName))?"":("."+field_name);
                Method m = null;
                String removed_squareb = field_name;
                if (field_name.contains("[")) {
                    removed_squareb = field_name.substring(0, field_name.indexOf("["));
                }
                if (currentObject == null) {
                    m = model.getClass().getMethod(GET+initCap(removed_squareb));
                } else {
                    m = currentObject.getClass().getMethod(GET+initCap(removed_squareb));
                }
                
                if (m.getGenericReturnType().getTypeName().startsWith("java.util.List")) {
//                    Debug.printDebug("is list");
                    String classWithPath = m.getGenericReturnType().getTypeName().substring(m.getGenericReturnType().getTypeName().indexOf("<")+1, m.getGenericReturnType().getTypeName().length()-1);
//                    Debug.printDebug("classWithPath = " + classWithPath);
                    List tempList = null;
                    if (currentObject == null) {
                        tempList = (List) m.invoke(model);
                    } else {
                        tempList = (List) m.invoke(currentObject);
                    }
                    if (tempList==null) {
                        tempList = new ArrayList();
                    }
                    
                    if (currentObject == null) {
                        m = model.getClass().getMethod(SET+initCap(removed_squareb), List.class);
                        m.invoke(model, tempList);
                    } else {
                        m = currentObject.getClass().getMethod(SET+initCap(removed_squareb), List.class);
                        m.invoke(currentObject, tempList);
                    }
                    
                    Integer index = Integer.parseInt(field_name.substring(field_name.indexOf("[")+1, field_name.length()-1));
                    if (index>=tempList.size()) {
                        while(index > tempList.size()-1) {
                            currentObject = Class.forName(classWithPath).newInstance();
                            tempList.add(currentObject);
                        }
                    } else {
                        currentObject = tempList.get(index);
                    }
                    //tempList.add(currentObject);
                    
                } else if (!(m.getGenericReturnType() == String.class || m.getGenericReturnType() == Long.class || 
                             m.getGenericReturnType() == Integer.class || m.getGenericReturnType() == Timestamp.class ||
                             m.getGenericReturnType() == Byte.class || m.getGenericReturnType() == java.math.BigDecimal.class || 
                             m.getGenericReturnType() == Date.class)) {
                    String classWithPath = m.getGenericReturnType().getTypeName();
                    Object tempCurrentObj = null;
                    if (currentObject == null) {
                        tempCurrentObj = m.invoke(model);
                    } else {
                        tempCurrentObj = m.invoke(currentObject);
                    }
                    
                    if (tempCurrentObj == null) {
                        tempCurrentObj = Class.forName(classWithPath).newInstance();
                        if (currentObject == null) {
                            m = model.getClass().getMethod(SET+initCap(removed_squareb), tempCurrentObj.getClass());
                            m.invoke(model, tempCurrentObj);
                        } else {
                            m = currentObject.getClass().getMethod(SET+initCap(removed_squareb), tempCurrentObj.getClass());
                            m.invoke(currentObject, tempCurrentObj);
                        }
                    }
                    currentObject = tempCurrentObj;
                } else {
                    try {
                        if (currentObject == null) {
                            m = model.getClass().getMethod(SET+initCap(field_name) + "_str", String.class);//if it has a _str, use this method
                            m.invoke(model, dataMap.get(methods));
                        } else {
                            m = currentObject.getClass().getMethod(SET+initCap(field_name) + "_str", String.class);//if it has a _str, use this method
                            m.invoke(currentObject, dataMap.get(methods));
                        }
                    } catch (Exception e) {
                        try {
                            if (currentObject == null) {
                                m = model.getClass().getMethod(SET+initCap(field_name), String.class);
                                m.invoke(model, dataMap.get(methods));
                            } else {
                                m = currentObject.getClass().getMethod(SET+initCap(field_name), String.class);
                                m.invoke(currentObject, dataMap.get(methods));
                            }
                        } catch (Exception e2) {
                            Debug.printDebug("Fail to set ["+ currentObjectName +"] to " + (currentObject == null?model:currentObject).getClass().getSimpleName());
                        }
                    }
                }
            }
        }
    }
    
    public static void listToMap(Map<String, Map> theSetup, List dataList, Map dataMap) throws Exception {
        List tempList = new ArrayList();
        dataMap.put("theList", tempList);
        for (Object obj : dataList) {
            Map tempDataMap = new HashMap();
            tempList.add(tempDataMap);
            modelToMap(theSetup, obj, tempDataMap);
        }
    }
    public static void modelToMap(Map<String, Map> theSetup, Object model, Map dataMap) throws Exception {
        for (Method method : model.getClass().getDeclaredMethods()) {
            if (method.getParameterCount()==0 && method.getName().startsWith("get") && !(method.getName().endsWith("_str") || method.getName().equals("getID")) ) {
                if (method.getGenericReturnType() == String.class || method.getGenericReturnType() == Long.class || 
                    method.getGenericReturnType() == Integer.class || method.getGenericReturnType() == Timestamp.class ||
                    method.getGenericReturnType() == Byte.class || method.getGenericReturnType() == java.math.BigDecimal.class) {
                    try {
                        Method method2 = model.getClass().getMethod(method.getName() + "_str");
                        Object obj = method2.invoke(model);
                        if (obj != null) {
                            dataMap.put(getFieldName(method), obj);
                        }
                    } catch (Exception e) {
                        Object obj = method.invoke(model);
                        if (obj != null) {
                            dataMap.put(getFieldName(method), obj);
                        }
                    }
                }
            }
        }
        for (String key : theSetup.keySet()) {
            if (key.endsWith("LIST")) continue;
            Method m = model.getClass().getMethod(GET+key);
            if (m.getGenericReturnType().getTypeName().startsWith("java.util.List")) {
                List list = (List)m.invoke(model);
                List dataList = new ArrayList();
                dataMap.put(key, dataList);
                for (Object obj : list) {
                    Map newDataMap = new HashMap();
                    dataList.add(newDataMap);
                    modelToMap(theSetup.get(key), obj, newDataMap);
                }
            } else {
                if (m.invoke(model) != null) {
                    Map newDataMap = new HashMap();
                    dataMap.put(key, newDataMap);
                    modelToMap(theSetup.get(key), m.invoke(model), newDataMap);
                }
            }
        }
    }
    
    public static Object getMethodModel(String apiMethod) throws Exception {
        if (apiServer == null) {
            populateApiServer();
        }
        String setup = getApiSetup(apiMethod);
        setup = setup.substring(0, setup.indexOf("."));
        for (Class theClass : SessionFactoryImpl.registeredClass) {
            if (theClass.getSimpleName().equals(setup)) {
                return theClass.newInstance();
            }
        }
        return null;
    }
    
    public static Map getSetupMap(String apiMethod) throws Exception {
        Map currentMap = null;
        Map previousMap = null;
        Map<String, Map> setupMap = new HashMap();
        String setup = getApiSetup(apiMethod);
        Debug.printDebug("111 setup = " + setup);
        setup = setup.substring(setup.indexOf(".")+1);
        Debug.printDebug("222 setup = " + setup);
        for (String setupString : setup.split(",")) {
            Debug.printDebug("setupString = " + setupString);
            currentMap = setupMap;
            for (String method : setupString.split("\\.")) {
                Debug.printDebug("method = " + method);
                if (currentMap.containsKey(method)) {
                    previousMap = currentMap;
                    currentMap = (Map)previousMap.get(method);
                } else {
                    previousMap = currentMap;
                    currentMap = new HashMap();
                }
                Debug.printDebug("put method = "+method+", current map is null? " + (currentMap==null));
                previousMap.put(method, currentMap);
            }
            Debug.printDebug("setupMap = " + setupMap);
        }
        return setupMap;
    }
    
    private synchronized static void populateApiServer() {
        if (apiServer == null) {
            apiServer = new HashMap();
            apiMethodSetup = new HashMap();
            defaultApiServer = SysConf.get("defaultApiServer");
            if (!defaultApiServer.endsWith("/")) {
                defaultApiServer += "/";
            }
            if (!Validator.isEmpty(SysConf.get("apiMethodServer"))) {
                for (String methodServer : SysConf.get("apiMethodServer").split(";")) {
                    String[] methodServerArr = methodServer.split("::");
                    apiServer.put(methodServerArr[0], methodServerArr[1].endsWith("/")?methodServerArr[1]:(methodServerArr[1]+"/") );
                }
            }
            for (String methodSetup : SysConf.get("apiMethodSetup").split(";")) {
                if (methodSetup.contains("::")) {
                    String[] methodSetupArr = methodSetup.split("::");
                    apiMethodSetup.put(methodSetupArr[0], methodSetupArr[1]);
                } else {
                    String theSetup = SysConf.get("apiSetup."+methodSetup);
                    if (!("apiSetup."+methodSetup).equals(theSetup)) {
                        apiMethodSetup.put(methodSetup, SysConf.get("apiSetup."+methodSetup));
                    } 
                }
            }
        }
    }
    private static Map<String, String> apiMethodSetup = null;
    private static Map<String, String> apiServer = null;
    private static String defaultApiServer = null;
    private static String getApiSetup(String apiMethod) throws Exception {
        if (apiServer == null) {
            populateApiServer();
        }
        String serverUrl = null;
        if (apiMethodSetup.containsKey(apiMethod)) {
            serverUrl = apiMethodSetup.get(apiMethod);
        } else {
            throw new CustomBaseException("apiMethodSetup ["+apiMethod+"] not found");
        }
        return serverUrl;
    }
    private static String getApiUrl(String apiMethod) {
        if (apiServer == null) {
            populateApiServer();
        }
        String serverUrl = null;
        if (apiServer.containsKey(apiMethod)) {
            serverUrl = apiServer.get(apiMethod);
        } else {
            serverUrl = defaultApiServer;
        }
        return serverUrl+apiMethod;
    }
    
    private static Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.S").create();
    public static String apiCall2(String apiMethod, Object modelObject, Map param) throws Exception {
        Map dataMap = new HashMap();
        modelToMap(getSetupMap(apiMethod), modelObject, dataMap);
        if (param != null) {
            dataMap.put("req.param", param);
        }
        Debug.printDebug("dataMap in apiCall = " + dataMap);
        Debug.printDebug("======================================================");
        Debug.printDebug("getApiUrl(apiMethod) = " + getApiUrl(apiMethod));
//        return HttpsUrlUtil.https_selfSignCallUrlPostJson(getApiUrl(apiMethod)+"?testParameter=123", new StringEntity(gson.toJson(dataMap)));
        return HttpsUrlUtil.https_selfSignCallUrlPostJson(getApiUrl(apiMethod), new StringEntity(gson.toJson(dataMap)));
    }
    public static String apiCall2(String apiMethod, List modelList, Map param) throws Exception {
        Map dataMap = new HashMap();
        listToMap(getSetupMap(apiMethod), modelList, dataMap);
        Debug.printDebug("dataMap in apiCall = " + dataMap);
        Debug.printDebug("======================================================");
        Debug.printDebug("getApiUrl(apiMethod) = " + getApiUrl(apiMethod));
        return HttpsUrlUtil.https_selfSignCallUrlPostJson(getApiUrl(apiMethod), new StringEntity(gson.toJson(dataMap)));
    }
    
    //API Server:: Start
    public static Boolean EnableApiAuthentication = Boolean.TRUE;
    public static Map<String, Map> apiAuthMap = new HashMap();
    public static Map<String, Map> apiRefreshMap = new HashMap();
    public final static String authorizationData_headerOrParamOrBoth = "B";//H=Header, Param=P, B=Both
    //API Server:: END
        
    //API Consumer:: Start
    public static Map<String, Map> tokenMap = new HashMap();
    
    public static String getToken_aquila(String apiApp) throws Exception {
        Map authMap = tokenMap.get(apiApp);
        if (authMap != null) {
            Long currentTime = DateUtil.getCurrentTimestamp().getTime();
            //5 min before expired, use refreshToken to regen token. Can consider to increate the time if the API 
            if ((currentTime+(1000*60*(5)) ) >= Long.parseLong(authMap.get("expires_in").toString()) ){
                if (currentTime >= Long.parseLong(authMap.get("expires_in").toString())) {
                    return genToken_aquila(apiApp, SysConf.get(apiApp+".Api.Consumer.key"), SysConf.get(apiApp+".Api.Consumer.secret"));
                }
                return refreshToken_aquila(apiApp, authMap);
            } else {
                return (String) authMap.get("accessToken");
            }
        } else {
            return genToken_aquila(apiApp, SysConf.get(apiApp+".Api.Consumer.key"), SysConf.get(apiApp+".Api.Consumer.secret"));
        }
    }
    
    private static String refreshToken_aquila(String apiApp, Map authMap){
        Map headerMap = new HashMap();
        headerMap.put("Authorization", "Bearer " + authMap.get("refreshToken"));
        try {
            String urlRtnString = HttpsUrlUtil.https_selfSignCallUrlPost_withHeader(SysConf.get(apiApp+".Api.Consumer.refreshUrl"), null, headerMap);
            headerMap.clear();
            headerMap = gson.fromJson(urlRtnString, Map.class);
            if (headerMap.containsKey("status") && "success".equalsIgnoreCase((String)headerMap.get("status"))) {
                tokenMap.put(apiApp, (Map)headerMap.get("data"));
                return (String) ((Map)headerMap.get("data")).get("accessToken");
            }
        } catch (Exception e) {
            tokenMap.remove(apiApp);
        }
        return null;
    }
    
    private static String genToken_aquila(String apiApp, String key, String secret) throws Exception {
        Map headerMap = new HashMap();
        headerMap.put("Authorization", "Basic "+ key+":"+secret);
        String urlRtnString = HttpsUrlUtil.https_selfSignCallUrlPost_withHeader(SysConf.get(apiApp+".Api.Consumer.tokenUrl"), null, headerMap);
        headerMap.clear();
        headerMap = gson.fromJson(urlRtnString, Map.class);
        Debug.printDebug("headerMap = " + headerMap);
        if (headerMap.containsKey("status") && "success".equalsIgnoreCase((String)headerMap.get("status"))) {
            tokenMap.put(apiApp, (Map)headerMap.get("data"));
            return (String) ((Map)headerMap.get("data")).get("accessToken");
        } else {
            throw new CustomBaseException((String)headerMap.get("message"));
//                return (String) headerMap.get("message");
        }
    }
    
    public static Map callApi_aquila(String apiApp, String apiUrl, List<NameValuePair> urlParameters) throws Exception {
        Map headerMap = new HashMap();
        try {
            headerMap.put("Authorization", "Bearer " + getToken_aquila(apiApp));
        } catch (BaseException be) {
            throw be;
        }
        try {
            String urlRtnString = HttpsUrlUtil.https_selfSignCallUrlPost_withHeader(apiUrl, urlParameters, headerMap);
            return gson.fromJson(urlRtnString, Map.class);
        } catch (Exception e) {
            
        }
        return null;
    }
    public static Map callApi_aquila(String apiApp, String apiUrl, Map<String, String> paramMap) throws Exception {
          Debug.printDebug("callApi_aquila ");
          Debug.printDebug("callApi_aquila " + paramMap);
        Map headerMap = new HashMap();
        try {
            headerMap.put("Authorization", "Bearer " + getToken_aquila(apiApp));
        } catch (BaseException be) {
            throw be;
        }
        try {
            List<NameValuePair> urlParameters = new ArrayList();
            for (String key : paramMap.keySet()) {
                urlParameters.add(new BasicNameValuePair(key, paramMap.get(key)));
            }
           
            String urlRtnString = HttpsUrlUtil.https_selfSignCallUrlPost_withHeader(apiUrl, urlParameters, headerMap);
            return gson.fromJson(urlRtnString, Map.class);
        } catch (Exception e) {
            
        }
        return null;
    }
    public static Map getApiByAccessToken(String accessToken, BaseDAO dao) {
        Debug.printDebug("accessToken " + accessToken);
        ApiTokenModel apiTokenModel = (ApiTokenModel) dao.getSession().getNamedQuery("ApiTokenModel.findBy_accessToken").setParameter("accessToken", accessToken).uniqueResult();   
        Debug.printDebug("apiTokenModel :" +apiTokenModel);
        
        if(apiTokenModel == null){
            Debug.printDebug("null apiTokenModel");
            return null;
        } 
        
        return populateToMap(apiTokenModel);
    }
    public static Map getApiByRefreshToken(String refreshToken, BaseDAO dao) {
        ApiTokenModel apiTokenModel = (ApiTokenModel) dao.getSession().getNamedQuery("ApiTokenModel.findBy_refreshToken").setParameter("refreshToken", refreshToken).uniqueResult();
        return populateToMap(apiTokenModel);
    }
    
    private static Map populateToMap(ApiTokenModel tokenModel) {
        Map map = new HashMap();
        map.put("accessToken", tokenModel.getAccess_token());
        map.put("refreshToken", tokenModel.getRefresh_token());
        map.put("calendar", tokenModel.getCreated_long());
        Debug.printDebug("String.valueOf(tokenModel.getExpires_in()) = " + String.valueOf(tokenModel.getExpires_in()));
        map.put("expires_in", String.valueOf(tokenModel.getExpires_in()));
//        map.put("expires_in", tokenModel.getExpires_in_str());
        map.put("authId", tokenModel.getApi_auth_id());
        map.put("apiApp", tokenModel.getApiapp());
        return map;
    }
}
