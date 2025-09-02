/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pb.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.LogFunction;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author yonglai
 */
public class UtilityApplication extends BaseActionSupport {
    
    private JSONArray newSubmissionList = new JSONArray();
    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
    Map sessionMap = new HashMap();
    
    public UtilityApplication() {
        model = new String();
    }
    
    public CommonFunction cf = new CommonFunction();
    
    @Override
    public String processInsert() {
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    @Override
    public String processUpdate() {
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    @Override
    public String delete() {
        return SUCCESS;
    }
    
    public String loadDashboard() {
        
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("Title", "Total Active Cadastral Submission");
        jsonObj.put("Total", "5");
        jsonObj.put("New", "4");
        jsonObj.put("Re-submission", "1");
        newSubmissionList = new JSONArray();
        newSubmissionList.add(0, jsonObj);
//        newSubmissionList.add(1, "");
        System.out.println("newSubmissionList " + newSubmissionList);
        
        setPageTitle_("Home");
        
        return "load_dashboard";
    }

    public JSONArray getNewSubmissionList() {
        return newSubmissionList;
    }

    public void setNewSubmissionList(JSONArray newSubmissionList) {
        this.newSubmissionList = newSubmissionList;
    }
    
    public void getAllSubmissionList() throws IOException {
        Map jsonMap = new HashMap();
        int code = 500;
        String status = "Failed";
        String message = "Internal Server Error";
        
         try {
            HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            response.setContentType("application/json");
            
            String strResponse = "";
            JSONObject jsonObj = new JSONObject();
//            String url = SystemConstants.DOMAIN.domain_utimaps + "get-all-lc-list"; //getAllLicenseList
//            ArrayList<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//            String hashKey = new CommonFunction().getHashKeyVal("PbUTiMAPS", SystemConstants.DOMAIN.PbUTiMAPSHashKey, "", (String) ActionContext.getContext().getSession().get("loginId"), "EIS");
//            urlParameters.add(new BasicNameValuePair("hValue", hashKey));
//            urlParameters.add(new BasicNameValuePair("pbUser", (String) ActionContext.getContext().getSession().get("loginId")));
//            urlParameters.add(new BasicNameValuePair("pbUserId", (String) ActionContext.getContext().getSession().get("id_profile")));
//            strResponse = cf.callPublicApi(url, urlParameters);
//            jsonObj = (JSONObject) new JSONParser().parse(strResponse);

//            String sStatus = jsonObj.get("res_message").toString();
//            cf.writeFile("LicenceLog", "getAllLicenceList*******retrieve case jsonObj :: " + jsonObj);

//            if (sStatus.equals("Success")) {
//                JSONObject jsonData = (JSONObject) jsonObj.get("data");
//                if (jsonData.containsKey("licenseList")) {
//                    jsonMap.put("Cases", jsonData.get("licenseList"));
//                }
//            }
            
            code = 200;
            status = "Success";
            message = "Retrieve submission information successfully.";
            
        } catch (Exception e) {
//            cf.writeFile("LicenceLog", "getAllLicenceList*******failed retrieve case :: " + e);
//            new LogFunction().logError(this.getClass(), " *** Failed to retrieve license info. *** ", e);
             e.printStackTrace();
        } finally {
        }
         
        jsonMap.put("code", code);
        jsonMap.put("status", status);
        jsonMap.put("message", message);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        response.getWriter().append(gson.toJson(jsonMap));
        response.flushBuffer();
    }
}
