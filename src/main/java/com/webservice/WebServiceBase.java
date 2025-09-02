///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.webservice;
//
///**
// *
// * @author Aiman
// */
//public class WebServiceBase {
//    
//}

package com.webservice;

import com.opensymphony.xwork2.ActionContext;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author thoth @ 5-Jul-2017
 */
public class WebServiceBase {

    @Context
    protected UriInfo uriInfo;

    @Context
    protected HttpServletRequest request;

//    protected WebServiceAuditor auditor = new WebServiceAuditor();
    protected String throwError(String errMsg) {
        JSONObject jData = new JSONObject();
        jData.put(WSConstants.Key.Status, WSConstants.Status.Failed);
        jData.put(WSConstants.Key.ErrorMsg, errMsg);
        return jData.toJSONString();
    }

    protected void logSuccess(String logTxt) {
//        auditor.audit(request.getAttribute(WSConstants.ClientId), request.getRemoteAddr(), logTxt, WebServiceAuditor.AuditStatus.Success);
    }

    protected void logFail(String logTxt) {
//        auditor.audit(request.getAttribute(WSConstants.ClientId), request.getRemoteAddr(), logTxt, WebServiceAuditor.AuditStatus.Failed);
    }
}
