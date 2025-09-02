/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pb.web;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.LogFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author yonglai
 */
public class UtilityMessageAction extends BaseActionSupport {
    
    public UtilityMessageAction() {
        model = new String();
    }
    
    @Override
    public String processInsert() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String processUpdate() {
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    @Override
    public String delete() {
        return SUCCESS;
    }
    
    
    @Override
    public String loadEditPage() {
        try {
//            String strResponse = "";
//            JSONObject jsonObj = new JSONObject();
//            String url = SystemConstants.DOMAIN.domain_eis + "get-single-eis-mymessage";
//            ArrayList<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//            String hashKey = new CommonFunction().getHashKeyVal("PbEIS", SystemConstants.DOMAIN.PbEISHashKey, "", (String) ActionContext.getContext().getSession().get("loginId"), "EIS");
//            urlParameters.add(new BasicNameValuePair("hValue", hashKey));
//            urlParameters.add(new BasicNameValuePair("pbUser", (String) ActionContext.getContext().getSession().get("loginId")));
//            urlParameters.add(new BasicNameValuePair("pbUserId", (String) ActionContext.getContext().getSession().get("userId")));
//            urlParameters.add(new BasicNameValuePair("msg_recipient", (String) ActionContext.getContext().getSession().get("loginId")));
//            urlParameters.add(new BasicNameValuePair("mid", mid));
//
//            strResponse = callPublicApi(url, urlParameters);
//            
//            jsonObj = (JSONObject) new JSONParser().parse(strResponse);
//            System.out.println("jsonObj " + jsonObj);
//            String responseMessage = jsonObj.get("res_message").toString();
//            System.out.println("responseMessage " + responseMessage);
//            
//            if (responseMessage.equals("Success")) {
//                JSONObject jsonData = (JSONObject) jsonObj.get("data");
//                System.out.println("jsonData " + jsonData);
//                String[] dataFields2 = new String[]{"id_msg","case_id","no_id","msg_subject","msg_content","msg_status","msg_status_date","msg_recipient","is_formal_offer","submission_ref_no"};
//                EisMyMessageModel eisMyMsg = new EisMyMessageModel();
//
//                setModelData(jsonData, dataFields2, eisMyMessage);
//            }
//            
//            countDeletedMessageList();
//            countNewMessageList();
//            
//            curPage="viewMessage";
        } catch (Exception e) {
            System.out.println("ezception in loadEditPage " + e);
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
        }
        return "load_list_page";
    }
    
    public String loadListPage() {
        Map sessionMap = ActionContext.getContext().getSession();
        setPageTitle_("Notifications");
        
        try {

//            String strResponse = "";
//            JSONObject jsonObj = new JSONObject();
//            String url = SystemConstants.DOMAIN.domain_eis + "get-eis-mymessage"; //getEisMymessage
//            ArrayList<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//            String hashKey = new CommonFunction().getHashKeyVal("PbEIS", SystemConstants.DOMAIN.PbEISHashKey, "", (String) ActionContext.getContext().getSession().get("loginId"), "EIS");
//            urlParameters.add(new BasicNameValuePair("hValue", hashKey));
//            urlParameters.add(new BasicNameValuePair("pbUser", (String) ActionContext.getContext().getSession().get("loginId")));
//            urlParameters.add(new BasicNameValuePair("pbUserId", (String) ActionContext.getContext().getSession().get("id_profile")));
//            urlParameters.add(new BasicNameValuePair("msg_recipient", (String) ActionContext.getContext().getSession().get("loginId")));
//            urlParameters.add(new BasicNameValuePair("msg_status", "N,R"));
//
//            strResponse = callPublicApi(url, urlParameters);
//            
//            jsonObj = (JSONObject) new JSONParser().parse(strResponse);
//            cf.writeFile("MessageLog", "getEisMymessage*******response message from api :: " + jsonObj);
//            String responseMessage = jsonObj.get("res_message").toString();
//            
//            if (responseMessage.equals("Success")) {
//                JSONObject jsonData = (JSONObject) jsonObj.get("data");
//                JSONArray my_msg = (JSONArray) jsonData.get("my_msg");
//                String[] dataFields2 = new String[]{"id_msg","case_id","no_id","msg_subject","msg_content","msg_status","msg_status_date","msg_recipient","is_formal_offer","submission_ref_no"};
//                
//                if(my_msg.size() > 0) {
//                    for (int x = 0; x < my_msg.size(); x++) {
//                        EisMyMessageModel eisMyMsg = new EisMyMessageModel();
//                        JSONObject jsTrn = (JSONObject) my_msg.get(x);
//                        setModelData(jsTrn, dataFields2, eisMyMsg);
//                        messageList.add(x, eisMyMsg);
//                    }
//                }
//            }
//            
//            thisPage = true;

        messageList.add(0, "First Message");
        } catch (Exception e) {
            System.out.println("Exception in loadListMessage " + e);
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
        }
        return "load_list_page";
    }
    
    private List<String> messageList = new ArrayList();
    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }
}
