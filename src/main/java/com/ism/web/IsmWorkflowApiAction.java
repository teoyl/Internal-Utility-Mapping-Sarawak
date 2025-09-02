/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ism.web;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
//import com.mrpe.api.util.ReadResourceFiles;
//import com.mrpe.qp.application.model.QPAppBase;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SmartXChangeIntfcWfUtil;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseAction;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.LogFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author ela2sains PURPOSE: 1. TO PUSH THE NOTIFICATION TO PORTAL NOTIFICATION
 * FOR PUBLIC USER AFTER THE CHANGES OF THE APPLICATION STATUS. (IN PROCESSING,
 * COMPLETED/REJECTED).
 *
 */
public class IsmWorkflowApiAction extends BaseAction {

    //api variables
    private String sid = "";
    private String ism_rec_id = "";
    private String title_en = "";
    private String title_bm = "";
    private String message_en = "";
    private String message_bm = "";
    private String url = "";
    private String mobile_app_url = "";

    private String sRespSts = "";
    private String sRespMsg = "";
    private String sRespResult = "";
    private JSONObject oJsonData = new JSONObject();
    //private ReadResourceFiles oResrcInfo = new ReadResourceFiles();
    private String sCurrentDate = Formatter.formatDate(DateUtil.getCurrentTimestamp(), "yyyy-MM-dd HH:mm:ss");
    private String sAgencyId = SystemConstants.DOMAIN.AGENCY_ID;
    private String sAgencyName = SystemConstants.DOMAIN.AGENCY_NAME;
    private String sEditPageUrl = SystemConstants.DOMAIN.ISM_EDIT_URL_WEB;
    private String sEditPageMobileUrl = SystemConstants.DOMAIN.ISM_EDIT_URM_MOBILE;
//    private String sEditPageUrl = this.oResrcInfo.GetResrcSmartXChangeConf("ism.EditPageUrlWeb");
//    private String sEditPageMobileUrl = this.oResrcInfo.GetResrcSmartXChangeConf("ism.EditPageUrlMobile");

    public IsmWorkflowApiAction() {
    }

    //************************************************************ START OF GETTER & SETTER ************************************************************
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getIsm_rec_id() {
        return ism_rec_id;
    }

    public void setIsm_rec_id(String ism_rec_id) {
        this.ism_rec_id = ism_rec_id;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getTitle_bm() {
        return title_bm;
    }

    public void setTitle_bm(String title_bm) {
        this.title_bm = title_bm;
    }

    public String getMessage_en() {
        return message_en;
    }

    public void setMessage_en(String message_en) {
        this.message_en = message_en;
    }

    public String getMessage_bm() {
        return message_bm;
    }

    public void setMessage_bm(String message_bm) {
        this.message_bm = message_bm;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMobile_app_url() {
        return mobile_app_url;
    }

    public void setMobile_app_url(String mobile_app_url) {
        this.mobile_app_url = mobile_app_url;
    }

    public String getsRespSts() {
        return sRespSts;
    }

    public void setsRespSts(String sRespSts) {
        this.sRespSts = sRespSts;
    }

    public String getsRespMsg() {
        return sRespMsg;
    }

    public void setsRespMsg(String sRespMsg) {
        this.sRespMsg = sRespMsg;
    }

    public JSONObject getoJsonData() {
        return oJsonData;
    }

    public void setoJsonData(JSONObject oJsonData) {
        this.oJsonData = oJsonData;
    }

    public String getsRespResult() {
        return sRespResult;
    }

    public void setsRespResult(String sRespResult) {
        this.sRespResult = sRespResult;
    }

    //************************************************************ END OF GETTER & SETTER ************************************************************
    public String processSXC_ISMWorkflow_Get_Task(String sCaseId, String sBy_ActivityCode, String sActionDate) throws Exception {
        try {
            SmartXChangeIntfcWfUtil sxcWfUtil = new SmartXChangeIntfcWfUtil();

            List<NameValuePair> urlParameters = new ArrayList();
            urlParameters.add(new BasicNameValuePair("params", "caseId::=" + sCaseId));
            urlParameters.add(new BasicNameValuePair("params", "by_activityCode::=" + sBy_ActivityCode));
            urlParameters.add(new BasicNameValuePair("params", "wf_system::=ISM"));
            urlParameters.add(new BasicNameValuePair("params", "actionDate::=" + sActionDate));//yyyy-MM-dd HH:mm:ss

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Get_Task() >>> urlParameters: " + urlParameters, null);

            String rtn = sxcWfUtil.callSXCSendDataPostParamAdv("ISM", "/workflow/get_task", urlParameters, "", 0);
            Map<String, Object> returnMap = new Gson().fromJson(rtn, Map.class);

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Get_Task() >>> returnMap: " + returnMap, null);

            if (returnMap.get("status") != null && returnMap.get("status").equals("success")) {
                //Do SUCCESS processing
                sRespResult = (String) returnMap.get("status");
                sRespSts = "success";
            } else {
                //Do FAIL processing 
                sRespSts = "error";
            }
        } catch (Exception e) {
            sRespSts = "error";
            throw (e);
        }
        return sRespSts;
    }

    public String processSXC_ISMWorkflow_Add_SubProcess(String sSubProcCode, String sActionDate, String sIsmRecId,
            String sSubProcTitleEn, String sSubProcTitleBm, String sIsmMsgEn, String sIsmMsgBm) throws Exception {
        try {
            SmartXChangeIntfcWfUtil sxcWfUtil = new SmartXChangeIntfcWfUtil();

            List<NameValuePair> urlParameters = new ArrayList();
            urlParameters.add(new BasicNameValuePair("params", "subprocess_code::=sp_" + sSubProcCode));
            urlParameters.add(new BasicNameValuePair("params", "actionDate::=" + sActionDate)); //yyyy-MM-dd HH:mm:ss
            urlParameters.add(new BasicNameValuePair("params", "ism_rec_id::=" + sIsmRecId));
            urlParameters.add(new BasicNameValuePair("params", "ism_message_en::=" + sIsmMsgEn));
            urlParameters.add(new BasicNameValuePair("params", "ism_message_bm::=" + sIsmMsgBm));
            urlParameters.add(new BasicNameValuePair("params", "title_en::=" + sSubProcTitleEn));
            urlParameters.add(new BasicNameValuePair("params", "title_bm::=" + sSubProcTitleBm));

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Add_SubProcess >>> urlParameters: " + urlParameters, null);

            String rtn = sxcWfUtil.callSXCSendDataPostParamAdv("ISM", "/workflow/add_subprocess", urlParameters, "", 0);
            Map<String, Object> returnMap = new Gson().fromJson(rtn, Map.class);

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Add_SubProcess >>> returnMap: " + returnMap, null);

            if (returnMap.get("status") != null && returnMap.get("status").equals("success")) {
                //Do SUCCESS processing
                sRespResult = (String) returnMap.get("status");
                sRespSts = "success";
            } else {
                //Do FAIL processing 
                String sMessage = (String) returnMap.get("message");
                sRespSts = "error::" + sMessage;

            }

        } catch (Exception e) {
            sRespSts = "error::";
            throw (e);
        }
        return sRespSts;
    }

    public String processSXC_ISMWorkflow_Complete_Task(String sCaseId, String sActivityCode, String sCompleteVal,
            String sDescEn, String sDescBm, String sRemarkEn, String sRemarkBm, String sActionDate) throws Exception {

        try {
            SmartXChangeIntfcWfUtil sxcWfUtil = new SmartXChangeIntfcWfUtil();

            List<NameValuePair> urlParameters = new ArrayList();
            urlParameters.add(new BasicNameValuePair("params", "caseId::=" + sCaseId)); //rec_id
            //urlParameters.add(new BasicNameValuePair("params", "coid::="+sCorpId)); //this one is SarawakIdCorporate
            urlParameters.add(new BasicNameValuePair("params", "activityCode::=" + sActivityCode));
            urlParameters.add(new BasicNameValuePair("params", "_completeValue::=" + sCompleteVal));
            urlParameters.add(new BasicNameValuePair("params", "desc_en::=" + sDescEn));//not mandatory
            urlParameters.add(new BasicNameValuePair("params", "desc_bm::=" + sDescBm));//not mandatory
            urlParameters.add(new BasicNameValuePair("params", "remark_en::=" + sRemarkEn));//not mandatory
            urlParameters.add(new BasicNameValuePair("params", "remark_bm::=" + sRemarkBm));//not mandatory
            urlParameters.add(new BasicNameValuePair("params", "actionDate::=" + sActionDate));//yyyy-MM-dd HH:mm:ss

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Complete_Task >>> urlParameters: " + urlParameters, null);

            String rtn = sxcWfUtil.callSXCSendDataPostParamAdv("ISM", "/workflow/complete_task", urlParameters, "", 0);
            Map<String, Object> returnMap = new Gson().fromJson(rtn, Map.class);

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Complete_Task >>> returnMap: " + returnMap, null);

            if (returnMap.get("status") != null && returnMap.get("status").equals("success")) {
                //Do SUCCESS processing
                sRespResult = (String) returnMap.get("status");
                sRespSts = "success";
            } else {
                //Do FAIL processing 
                String sMessage = (String) returnMap.get("message");
                sRespSts = "error::" + sMessage;
            }
        } catch (Exception e) {
            sRespSts = "error";
            throw (e);
        }
        return sRespSts;
    }

    public String processSXC_ISMWorkflow_Update_Progress(String sIsmRecId, String sStatusTobeUpdate, String sCustRemarksEn, String sCustRemarksBm) throws Exception {
        //THIS IS TO UPDATE APPLICATION STATUS REMARKS IN MY WORKSPACE
        try {
            SmartXChangeIntfcWfUtil sxcWfUtil = new SmartXChangeIntfcWfUtil();

            List<NameValuePair> urlParameters = new ArrayList();
            urlParameters.add(new BasicNameValuePair("params", "rec_id::=" + sIsmRecId));
            urlParameters.add(new BasicNameValuePair("params", "status::=" + sStatusTobeUpdate)); //@7.11.2024. Checked with David. This is the Application Status. in_progress meaning the application are still in progress.
            urlParameters.add(new BasicNameValuePair("params", "agency_id::=" + sAgencyId));
            urlParameters.add(new BasicNameValuePair("params", "remark_en::=" + sCustRemarksEn));
            urlParameters.add(new BasicNameValuePair("params", "remark_bm::=" + sCustRemarksBm));

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Update_Application >>> urlParameters: " + urlParameters, null);
            Debug.printDebug("processSXC_ISMWorkflow_Update_Application >>> urlParameters: " + urlParameters);

            String rtn = sxcWfUtil.callSXCSendDataPostParamAdv("ISM", "/workflow/update_application", urlParameters, "", 0);
            Map<String, Object> returnMap = new Gson().fromJson(rtn, Map.class);

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Update_Application >>> returnMap: " + returnMap, null);

            if (returnMap.get("status") != null && returnMap.get("status").equals("success")) {
                //Do SUCCESS processing
                sRespResult = (String) returnMap.get("status");
                sRespSts = "success";
            } else {
                //Do FAIL processing 
                String sMessage = (String) returnMap.get("message");
                sRespSts = "error::" + sMessage;
            }
        } catch (Exception e) {
            sRespSts = "error::";
            throw (e);
        }
        return sRespSts;
    }

    public String processSXC_ISMWorkflow_Update_Application(String sIsmRecId, String sStatusTobeUpdate, String strPayStatus, String strPayUrl, String sCustRemarksEn, String sCustRemarksBm) throws Exception {
        /*
        pay_status: 
        pending_ext - external payment
        pending_int - internal payment
        done - payment done
        
        Remark:
        1. external payment will be open payment gateway in new tab such as using bank payment gateway
        2. internal payment will be open payment gateway in iframe, such as swkpay or open payment
         */

        //THIS IS TO UPDATE APPLICATION STATUS REMARKS IN MY WORKSPACE
        try {
            SmartXChangeIntfcWfUtil sxcWfUtil = new SmartXChangeIntfcWfUtil();

            List<NameValuePair> urlParameters = new ArrayList();
            urlParameters.add(new BasicNameValuePair("params", "rec_id::=" + sIsmRecId));
            urlParameters.add(new BasicNameValuePair("params", "status::=" + sStatusTobeUpdate)); //@7.11.2024. Checked with David. This is the Application Status. in_progress meaning the application are still in progress.
            urlParameters.add(new BasicNameValuePair("params", "pay_status::=" + strPayStatus));
            //wongkk@20SEP2024 -Disabled
//            if(!strPayStatus.equals(QPAppBase.ISM_PAY_STATUS.DONE)){
            urlParameters.add(new BasicNameValuePair("params", "agency_id::=" + sAgencyId));
            urlParameters.add(new BasicNameValuePair("params", "pay_url::=" + strPayUrl));
            urlParameters.add(new BasicNameValuePair("params", "remark_en::=" + sCustRemarksEn));
            urlParameters.add(new BasicNameValuePair("params", "remark_bm::=" + sCustRemarksBm));
            urlParameters.add(new BasicNameValuePair("params", "datetime::=" + sCurrentDate));

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Update_Application >>> urlParameters: " + urlParameters, null);

            String rtn = sxcWfUtil.callSXCSendDataPostParamAdv("ISM", "/workflow/update_application", urlParameters, "", 0);
            Map<String, Object> returnMap = new Gson().fromJson(rtn, Map.class);

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Update_Application >>> returnMap: " + returnMap, null);

            if (returnMap.get("status") != null && returnMap.get("status").equals("success")) {
                //Do SUCCESS processing
                sRespResult = (String) returnMap.get("status");
                sRespSts = "success";
            } else {
                //Do FAIL processing 
                String sMessage = (String) returnMap.get("message");
                sRespSts = "error::" + sMessage;
            }
        } catch (Exception e) {
            sRespSts = "error::";
            throw (e);
        }
        return sRespSts;
    }

    public String processSXC_ISMWorkflow_Notification_Insert(String sSrwkId, String sIsmRecId, String sTitleEn, String sTitleBm,
            String sMsgEn, String sMsgBm, String sCaseId, String sIsmServiceId, String sRedirect, String sIsPayment) throws Exception {

        try {
            String sEditUrl = sEditPageUrl + "?m=w&t=[[swkid_token]]&l=[[lang]]" + "&svcId=" + sIsmServiceId + "&agency_Id=" + sAgencyId + "&id=" + sCaseId + sRedirect;
            String sEditMobileUrl = sEditPageMobileUrl + "?m=m&t=[[swkid_token]]&l=[[lang]]" + "&svcId=" + sIsmServiceId + "&agency_Id=" + sAgencyId + "&id=" + sCaseId + sRedirect;

            //eg: https://ismonline.tnt.sarawak.gov.my/ism/projects/eqp/survey/?id=1649985177080MYFj5h0
//            String sPayUrl = QPAppBase.ISM_PAYMENT.PAY_URL+"?id="+sCaseId;
            //wongkk@20SEP2024 -Disabled
//            String sPayUrl = SystemConstants.DOMAIN.ISM_EQP_URL+"survey/?id="+sCaseId;
            SmartXChangeIntfcWfUtil sxcWfUtil = new SmartXChangeIntfcWfUtil();

            List<NameValuePair> urlParameters = new ArrayList();
            urlParameters.add(new BasicNameValuePair("params", "sid::=" + sSrwkId));
            urlParameters.add(new BasicNameValuePair("params", "ism_rec_id::=" + sIsmRecId));
            urlParameters.add(new BasicNameValuePair("params", "title_en::=" + sTitleEn));
            urlParameters.add(new BasicNameValuePair("params", "message_en::=" + sMsgEn));
            urlParameters.add(new BasicNameValuePair("params", "title_bm::=" + sTitleBm));
            urlParameters.add(new BasicNameValuePair("params", "message_bm::=" + sMsgBm));
            //wongkk@20SEP2024 -Disabled
//            if(!Validator.isEmpty(sIsPayment) && sIsPayment.equals("Y")){
//                urlParameters.add(new BasicNameValuePair("params", "url::=" + sPayUrl));
//                urlParameters.add(new BasicNameValuePair("params", "mobile_app_url::=" + sPayUrl));
//            }else{
            urlParameters.add(new BasicNameValuePair("params", "url::=" + sEditUrl));
            urlParameters.add(new BasicNameValuePair("params", "mobile_app_url::=" + sEditMobileUrl));
//            }

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Notification_Insert >>> urlParameters: " + urlParameters, null);

            String rtn = sxcWfUtil.callSXCSendDataPostParamAdv("ISM", "/workflow/notification_insert", urlParameters, "", 0);
            Map<String, Object> returnMap = new Gson().fromJson(rtn, Map.class);

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Notification_Insert >>> returnMap: " + returnMap, null);

            if (returnMap.get("status") != null && returnMap.get("status").equals("success")) {
                //Do SUCCESS processing
                sRespResult = (String) returnMap.get("status");
                LogManager.getLogger().debug("processSXC_ISMWorkflow_Notification_Insert >>> sRespResult :" + sRespResult);
                sRespSts = "success";

            } else {
                //Do FAIL processing 
                sRespSts = "error";

            }

        } catch (Exception e) {
            sRespSts = "error";
            throw (e);
        }
        return sRespSts;
    }

    public String processSXC_ISMWorkflow_SaveAsDraft_Workflow(String sWorkflowCode, String sLoginId, String sCaseId, String sCaseRefNo, String sWorkflow, String sIsmServiceId, String sRedirect, String sCorpId) throws Exception {
        try {
            //Map sessionMap = ActionContext.getContext().getSession();

            String sEditUrl = sEditPageUrl + "?m=w&t=[[swkid_token]]&l=[[lang]]" + "&svcId=" + sIsmServiceId + "&agency_Id=" + sAgencyId + "&id=" + sCaseId + sRedirect;
            String sEditMobileUrl = sEditPageMobileUrl + "?m=m&t=[[swkid_token]]&l=[[lang]]" + "&svcId=" + sIsmServiceId + "&agency_Id=" + sAgencyId + "&id=" + sCaseId + sRedirect;

            SmartXChangeIntfcWfUtil sxcWfUtil = new SmartXChangeIntfcWfUtil();

            List<NameValuePair> urlParameters = new ArrayList();
            urlParameters.add(new BasicNameValuePair("params", "wf_code::=" + sWorkflowCode));
            urlParameters.add(new BasicNameValuePair("params", "status::=draft"));
            urlParameters.add(new BasicNameValuePair("params", "triggerBy::=" + sLoginId)); ///user sarawakid
            urlParameters.add(new BasicNameValuePair("params", "systemCode::=ISM"));
            urlParameters.add(new BasicNameValuePair("params", "form_id::=" + sCaseId)); //this one is record id
            urlParameters.add(new BasicNameValuePair("params", "case_id::=" + sCaseRefNo)); //this one is case reference no
            urlParameters.add(new BasicNameValuePair("params", "coid::=" + sCorpId)); //this one is SarawakIdCorporate
            urlParameters.add(new BasicNameValuePair("params", "sid::=" + sLoginId));
            urlParameters.add(new BasicNameValuePair("params", "service_id::=" + sIsmServiceId));
            urlParameters.add(new BasicNameValuePair("params", "agency_id::=" + sAgencyId));
            urlParameters.add(new BasicNameValuePair("params", "agency_name::=" + sAgencyName));
            urlParameters.add(new BasicNameValuePair("params", "url::=" + sEditUrl));
            urlParameters.add(new BasicNameValuePair("params", "mobile_app_url::=" + sEditMobileUrl));
            urlParameters.add(new BasicNameValuePair("params", "datetime::=" + sCurrentDate));
            urlParameters.add(new BasicNameValuePair("params", "workflow::=" + sWorkflow));

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_SaveAsDraft_Workflow >>> urlParameters: " + urlParameters, null);
            new LogFunction().printDebug("processSXC_ISMWorkflow_SaveAsDraft_Workflow >>> urlParameters: " + urlParameters);

            String rtn = sxcWfUtil.callSXCSendDataPostParamAdv("ISM", "/workflow/save_as_draft", urlParameters, "", 0);
            Map<String, Object> returnMap = new Gson().fromJson(rtn, Map.class);

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_SaveAsDraft_Workflow >>> returnMap: " + returnMap, null);
            new LogFunction().printDebug("processSXC_ISMWorkflow_SaveAsDraft_Workflow >>> returnMap: " + returnMap);

            if (returnMap.get("status") != null && returnMap.get("status").equals("success")) {
                //Do SUCCESS processing
                sRespResult = (String) returnMap.get("status");
                String ism_rec_id = (String) ((Map) returnMap.get("data")).get("rec_id");
                sRespSts = "success::" + ism_rec_id;

            } else {
                //Do FAIL processing 
                sRespSts = "error";
                String error_msg = (String) returnMap.get("error_msg");
                throw new CustomBaseException(error_msg);
            }

        } catch (Exception e) {
            sRespSts = "error";
            throw (e);
        }
        return sRespSts;
    }

    public String processSXC_ISMWorkflow_DeleteDraft_Workflow(String sIsmRecId) throws Exception {
        try {
            SmartXChangeIntfcWfUtil sxcWfUtil = new SmartXChangeIntfcWfUtil();

            List<NameValuePair> urlParameters = new ArrayList();
            urlParameters.add(new BasicNameValuePair("params", "ism_rec_id::=" + sIsmRecId));

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_DeleteDraft_Workflow >>> urlParameters: " + urlParameters, null);

            String rtn = sxcWfUtil.callSXCSendDataPostParamAdv("ISM", "/workflow/delete_draft", urlParameters, "", 0);
            Map<String, Object> returnMap = new Gson().fromJson(rtn, Map.class);

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_DeleteDraft_Workflow >>> returnMap: " + returnMap, null);

            if (returnMap.get("status") != null && returnMap.get("status").equals("success")) {
                sRespSts = "success";

            } else {
                //Do FAIL processing 
                sRespSts = "error";
                String error_msg = (String) returnMap.get("error_msg");
                throw new CustomBaseException(error_msg);
            }
        } catch (Exception e) {
            sRespSts = "error";
            throw (e);
        }
        return sRespSts;
    }

    public String processSXC_ISMWorkflow_Start_Workflow(String sWorkflowCode, String sLoginId, String sCaseId, String sCaseRefNo, String sIsmRecId, String sIsmServiceId, String sRedirect, String sCorpId) throws Exception {
        try {
            //String sEditUrl = sEditPageUrl + "?m=w&t=[[swkid_token]]&l=[[lang]]" + "&svcId=" + sIsmServiceId + "&agency_Id=" + sAgencyId + sRedirect;
            //String sEditMobileUrl = sEditPageMobileUrl + "?m=m&t=[[swkid_token]]&l=[[lang]]"  + "&svcId=" + sIsmServiceId + "&agency_Id=" + sAgencyId + sRedirect;
            //brianc@2025-07-01 :: add id param to cater for termination of storage operator
            String sEditUrl = sEditPageUrl + "?m=w&t=[[swkid_token]]&l=[[lang]]" + "&svcId=" + sIsmServiceId + "&agency_Id=" + sAgencyId + "&id=" + sCaseId + sRedirect;
            String sEditMobileUrl = sEditPageMobileUrl + "?m=m&t=[[swkid_token]]&l=[[lang]]" + "&svcId=" + sIsmServiceId + "&agency_Id=" + sAgencyId + "&id=" + sCaseId + sRedirect;

            SmartXChangeIntfcWfUtil sxcWfUtil = new SmartXChangeIntfcWfUtil();

            List<NameValuePair> urlParameters = new ArrayList();
            urlParameters.add(new BasicNameValuePair("params", "wf_code::=" + sWorkflowCode));
            urlParameters.add(new BasicNameValuePair("params", "triggerBy::=" + sLoginId)); ///user sarawakid
            urlParameters.add(new BasicNameValuePair("params", "systemCode::=ISM"));
            urlParameters.add(new BasicNameValuePair("params", "caseId::=" + sCaseId)); //this one is record id
            urlParameters.add(new BasicNameValuePair("params", "case_ref::=" + sCaseRefNo)); //this one is case reference no
            urlParameters.add(new BasicNameValuePair("params", "coid::=" + sCorpId)); //this one is SarawakIdCorporate
            urlParameters.add(new BasicNameValuePair("params", "sid::=" + sLoginId));
            urlParameters.add(new BasicNameValuePair("params", "service_id::=" + sIsmServiceId));
            urlParameters.add(new BasicNameValuePair("params", "agency_id::=" + sAgencyId));
            urlParameters.add(new BasicNameValuePair("params", "agency_name::=" + sAgencyName));
            urlParameters.add(new BasicNameValuePair("params", "url::=" + sEditUrl));
            urlParameters.add(new BasicNameValuePair("params", "mobile_app_url::=" + sEditMobileUrl));
            if (!Validator.isEmpty(sIsmRecId)) { //brianc@2025-06-17 :: cater for application directly submit without draft case status
                urlParameters.add(new BasicNameValuePair("params", "ism_rec_id::=" + sIsmRecId));
            }
            urlParameters.add(new BasicNameValuePair("params", "actionDate::=" + sCurrentDate));//yyyy-MM-dd HH:mm:ss

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Start_Workflow >>> urlParameters: " + urlParameters, null);

            String rtn = sxcWfUtil.callSXCSendDataPostParamAdv("ISM", "/workflow/start", urlParameters, "", 0);
            Map<String, Object> returnMap = new Gson().fromJson(rtn, Map.class);

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Start_Workflow >>> returnMap: " + returnMap, null);

            if (returnMap.get("status") != null && returnMap.get("status").equals("success")) {
                //Do SUCCESS processing
                sRespResult = (String) returnMap.get("status");
                //sRespSts = "success";
                if (!Validator.isEmpty(sIsmRecId)) { //brianc@2025-06-17 :: cater for application directly submit without draft case status
                    sRespSts = "success";
                } else {
                    String ism_rec_id = (String) ((Map) returnMap.get("data")).get("ism_rec_id");
                    sRespSts = "success::" + ism_rec_id;
                }
            } else {
                //Do FAIL processing 
                sRespSts = "error";
            }

        } catch (Exception e) {
            sRespSts = "error";
            throw (e);
        }
        return sRespSts;
    }

    public String processSXC_ISMWorkflow_Insert_Payment_History(String sToken, String sLoginId, String sCorpId, String sBilRefNo, String sBillAmount, String sBilType,
            String sPaymentChannel, String sPayTransNo, String sPaymentId, String sApplicantName, String sStatus, String sReceiptUrl,
            String sOtherDetails, String sPaymentRemark) throws Exception {
        //Remark: Checked with David. This is called when public making the payment. Only call one time per one application. After that should call /workflow/payment_hist_update.
        try {
            SmartXChangeIntfcWfUtil sxcWfUtil = new SmartXChangeIntfcWfUtil();
            List<NameValuePair> urlParameters = new ArrayList();
            urlParameters.add(new BasicNameValuePair("params", "token::=" + sToken));
            urlParameters.add(new BasicNameValuePair("params", "sid::=" + sLoginId)); //sarawakid
            urlParameters.add(new BasicNameValuePair("params", "coid::=" + sCorpId)); //sarawakid corporate
            urlParameters.add(new BasicNameValuePair("params", "agency::=" + sAgencyName));
            urlParameters.add(new BasicNameValuePair("params", "agency_bm::=" + sAgencyName));
            urlParameters.add(new BasicNameValuePair("params", "bill_type::=" + sBilType));
            urlParameters.add(new BasicNameValuePair("params", "bill_type_bm::=" + sBilType));
            urlParameters.add(new BasicNameValuePair("params", "bill_ref_no::=" + sBilRefNo));
            urlParameters.add(new BasicNameValuePair("params", "bill_amt::=" + sBillAmount));
            urlParameters.add(new BasicNameValuePair("params", "pymt_channel::=" + sPaymentChannel));
            urlParameters.add(new BasicNameValuePair("params", "pymt_datetime::=" + sCurrentDate));
            urlParameters.add(new BasicNameValuePair("params", "pymt_trans_no::=" + sPayTransNo));
            urlParameters.add(new BasicNameValuePair("params", "object_id::=" + sPaymentId));
            urlParameters.add(new BasicNameValuePair("params", "applicant_name::=" + sApplicantName));
            urlParameters.add(new BasicNameValuePair("params", "status::=" + sStatus));
            urlParameters.add(new BasicNameValuePair("params", "receipt_url::=" + sReceiptUrl)); //not mandatory
            urlParameters.add(new BasicNameValuePair("params", "other_payment_details::=" + sOtherDetails)); //not mandatory
            urlParameters.add(new BasicNameValuePair("params", "pymt_remark::=" + sPaymentRemark)); //not mandatory

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Insert_Payment_History >>> urlParameters: " + urlParameters, null);

            String rtn = sxcWfUtil.callSXCSendDataPostParamAdv("ISM", "/workflow/payment_hist_insert", urlParameters, "", 0);
            Map<String, Object> returnMap = new Gson().fromJson(rtn, Map.class);

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Insert_Payment_History >>> returnMap: " + returnMap, null);

            if (returnMap.get("status") != null && returnMap.get("status").equals("SUCCESS")) {
                //Do SUCCESS processing
                sRespResult = (String) returnMap.get("status");
                String id = (String) returnMap.get("id");
                sRespSts = "success::" + id;
            } else {
                //Do FAIL processing 
                String sMessage = (String) returnMap.get("message");
                sRespSts = "error::" + sMessage;
            }

        } catch (Exception e) {
            sRespSts = "error::";
            throw (e);
        }
        return sRespSts;
    }

    public String processSXC_ISMWorkflow_Update_Payment_History(String sToken, String sPaymentId, String sPayTransNo, String sStatus,
            String sReceiptUrl, String sPaymentRemark) throws Exception {
        try {
            SmartXChangeIntfcWfUtil sxcWfUtil = new SmartXChangeIntfcWfUtil();
            List<NameValuePair> urlParameters = new ArrayList();
            urlParameters.add(new BasicNameValuePair("params", "token::=" + sToken));
            urlParameters.add(new BasicNameValuePair("params", "rec_id::=" + sPaymentId)); //id from payment_hist_insert
            urlParameters.add(new BasicNameValuePair("params", "pymt_trans_no::=" + sPayTransNo));
            urlParameters.add(new BasicNameValuePair("params", "status::=" + sStatus));
            urlParameters.add(new BasicNameValuePair("params", "receipt_url::=" + sReceiptUrl)); //not mandatory
            urlParameters.add(new BasicNameValuePair("params", "pymt_remark::=" + sPaymentRemark)); //not mandatory

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Update_Payment_History >>> urlParameters: " + urlParameters, null);

            String rtn = sxcWfUtil.callSXCSendDataPostParamAdv("ISM", "/workflow/payment_hist_update", urlParameters, "", 0);
            Map<String, Object> returnMap = new Gson().fromJson(rtn, Map.class);

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Update_Payment_History >>> returnMap: " + returnMap, null);

            if (returnMap.get("status") != null && returnMap.get("status").equals("success")) {
                //Do SUCCESS processing
                sRespResult = (String) returnMap.get("status");
                sRespSts = "success";
            } else {
                //Do FAIL processing 
                String sMessage = (String) returnMap.get("message");
                sRespSts = "error::" + sMessage;
            }
        } catch (Exception e) {
            sRespSts = "error::";
            throw (e);
        }
        return sRespSts;
    }

    //This is an operation to query existing application(s).
    public String processSXC_ISMWorkflow_Select(String sToken, String sLoginId, String sServiceId, String sCaseId, String sFormId, String sDatetime, String sFrom) throws Exception {
        try {
            SmartXChangeIntfcWfUtil sxcWfUtil = new SmartXChangeIntfcWfUtil();

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("sid", sLoginId));
            urlParameters.add(new BasicNameValuePair("service_id", sServiceId));
            urlParameters.add(new BasicNameValuePair("case_id", sCaseId));
            urlParameters.add(new BasicNameValuePair("form_id", sFormId));
            urlParameters.add(new BasicNameValuePair("datetime", sDatetime));

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Select >>> urlParameters: " + urlParameters, null);

            String rtn = sxcWfUtil.callSXCSendDataPostParamAdv("ISM", "/application/select", urlParameters, "", 0);

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Select >>> returnResult: " + rtn, null);

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(rtn);
            JSONObject jo = (JSONObject) obj;
            String status = jo.get("status").toString();
            String app_status = "", app_workflow = "", app_url = "", app_mobile_app_url = "", app_pay_url = "", app_pay_status = "", app_service_id = "";

            if (!Validator.isEmpty(status) && status.equals("success")) {
                JSONArray jsonArray = (JSONArray) jo.get("data");
                for (int j = 0; j < jsonArray.size(); j++) {
                    JSONObject dataObj = (JSONObject) jsonArray.get(j);
                    app_status = dataObj.get("app_status").toString();
                    app_workflow = dataObj.get("app_workflow").toString();
                    app_url = dataObj.get("app_url").toString();
                    app_mobile_app_url = dataObj.get("app_mobile_app_url").toString();
                    if (dataObj.get("app_pay_url") != null) {
                        app_pay_url = dataObj.get("app_pay_url").toString();
                    }
                    if (dataObj.get("app_pay_status") != null) {
                        app_pay_status = dataObj.get("app_pay_status").toString();
                    }
                    app_service_id = dataObj.get("app_service_id").toString();

                    new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Select >>> app_status: " + app_status, null);

                    if (!Validator.isEmpty(sFrom) && sFrom.equals("backend")) {
                        sRespSts = dataObj.toJSONString();
                    } else {
                        sRespSts = "success::" + app_status;
                    }
                }
//                sRespSts = "success::"+app_status+"::"+app_workflow+"::"+app_url+"::"+app_mobile_app_url+"::"+app_pay_url+"::"+app_pay_status+"::"+app_service_id;
            } else {
                //Do FAIL processing 
                sRespSts = "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            sRespSts = "error";
            throw (e);
        }
        return sRespSts;
    }

    public String processSXC_ISMWorkflow_Chrono_Select(String sIsmRecId) throws Exception {
        try {
            SmartXChangeIntfcWfUtil sxcWfUtil = new SmartXChangeIntfcWfUtil();

            List<NameValuePair> urlParameters = new ArrayList();
            urlParameters.add(new BasicNameValuePair("params", "ism_rec_id::=" + sIsmRecId));

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Chrono_Select >>> urlParameters: " + urlParameters, null);

            String rtn = sxcWfUtil.callSXCSendDataPostParamAdv("ISM", "/workflow/chrono_select", urlParameters, "", 0);
            Map<String, Object> returnMap = new Gson().fromJson(rtn, Map.class);

            new LogFunction().logDebug(this.getClass(), "processSXC_ISMWorkflow_Chrono_Select >>> returnMap: " + returnMap, null);

            if (returnMap.get("status") != null && returnMap.get("status").equals("success")) {
                //Do SUCCESS processing
                sRespResult = (String) returnMap.get("status");
                sRespSts = returnMap.get("data").toString();
            } else {
                //Do FAIL processing 
                sRespSts = "error";
            }
        } catch (Exception e) {
            sRespSts = "error";
            throw (e);
        }
        return sRespSts;
    }
}
