/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.utimaps.model.JobDetailModel;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author yonglai
 */
public class MapViewerAction extends BaseActionSupport<JobDetailModel> implements ModelDriven<JobDetailModel>{
    
    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    public MapViewerAction() {
        model = new JobDetailModel();
    }
    
    @Override
    public JobDetailModel getModel() {
        return model;
    }
    
    public String loadMainPage() {
        setPageTitle_("");
        
        return "load_main_page";
    }
    
    /**
     *
     * @return
     */
    @Override
    public String loadEditPage() {
        try {
            setPageTitle_("Map Viewer");
            setPageSubTitle_("Edit");
            model = super.processEdit(getModel());
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionAction", "loadEditPage");
        }
        
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }
    
    @Override
    public String processInsert() {
        BaseDAO surveyjobDAO = new BaseDAOImpl();
        try {
            validateRequired();
            specificValidation("insert");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.INSERT_FAIL;
            }
            
            addActionMessage(getText("createSuccess"));

        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "MapViewerAction", "SubmissionAction", "processInsert");
            addActionMessage(getText("createFail"));
            return SystemConstants.ACTION_Status.INSERT_FAIL;
        } finally {
            surveyjobDAO.closeSession();
        }
        return returnStr;
    }
    
    @Override
    public String processUpdate() {
        try {
            
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0) {
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
            addActionMessage(getText("updateSuccess"));
        } catch (Exception e) {
            return "edit_fail";
        }

        return returnStr;
    }
    
    public String loadViewPage() {
        
        setMapParamStr(setupMapKey());
         
        return "load_view_page";
    }
    
    public String mapParamStr;
    public String getMapParamStr() {
        return mapParamStr;
    }

    public void setMapParamStr(String mapParamStr) {
        this.mapParamStr = mapParamStr;
    }
    
    public String setupMapKey() {
        BaseDAO dao = new BaseDAOImpl();
        dao.setSession(baseDAO.getSession());
        String param = "";
        
        try {
            Map sessionMap = ActionContext.getContext().getSession();
            String paramUserName = (String) sessionMap.get("loginId");
            String paramUserDivision = (String) sessionMap.get("userDivisionId");
            String jobId = "";
            
            String divAssignedList = sessionMap.get("div_assigned").toString();
            String formattedDivList = divAssignedList.replace("[", "").replace("]", "");
            formattedDivList = formattedDivList.replace(" ", "");

            String[] stringArray = formattedDivList.split(",");
            for (String str : stringArray) {
                paramUserDivision = str;
            }
            
            if(!Validator.isEmpty(request.getParameter("jobId"))) {
                jobId = request.getParameter("jobId");
            };
            
            if(Validator.isEmpty(paramUserDivision)) {
                paramUserDivision = "00";
            };
            
            param = "username='" + paramUserName + "'&hashkey='" + GetMapHashKey(paramUserName) + "'&jobid=" + jobId + "&userdiv='" + paramUserDivision + "'";
            Debug.printDebug("param - " + param);
        } catch (Exception e) {
            e.printStackTrace();
            
        }

        return Base64.getEncoder().encodeToString(param.getBytes());
    }
    
    public String GetMapHashKey(String userName) {
        try {
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
            String CurrentYear = Integer.toString(currentTimestamp.getYear() + 1900);
            String CurrentDay = String.format("%02d", currentTimestamp.getDate());
            String CurrentMonth = String.format("%02d", currentTimestamp.getMonth() + 1);
            String appKey = "^" + userName + "^UTIMAPS";
            String hashStr = CurrentYear + "^" + CurrentMonth + "^" + CurrentDay + appKey;
            Debug.printFrameworkDebug("hashStr - " + hashStr);
            String hValue = calcHmac(hashStr);
            hValue = hValue.toUpperCase();
            Debug.printFrameworkDebug("hval - " + hValue);

            return hValue;
        } catch (Exception e) {
            return "";
        }

    }

    public String calcHmac(String data) {
        try {
            String AUTH_TOKEN = SystemConstants.DOMAIN.MapViewerKey;
            byte[] key = AUTH_TOKEN.getBytes(StandardCharsets.US_ASCII);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            byte[] byteArray = data.getBytes(StandardCharsets.US_ASCII);
            byte[] resultBytes = mac.doFinal(byteArray);
            return byteArrayToHexString(resultBytes).toUpperCase();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String byteArrayToHexString(byte[] array) {
        Formatter formatter = new Formatter();
        for (byte b : array) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
