/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package com.webservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sains.common.util.DateUtil;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ServiceFactory;
import com.webservice.util.AuthResponse;
import com.webservice.util.JWTTokenNeeded;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 *
 * @author Aiman
 */
@Path("notifyCompJobProgress")
public class CompjobService {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CompjobService
     */
    public CompjobService() {
    }
    
    @GET
    @Path("/test")
//    @JWTTokenNeeded
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson() {
        try {
            // Create the response object
            AuthResponse authResponse = new AuthResponse("success", "Precheck Service Connection successful");

            // Convert the response object to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(authResponse);

            // Return the JSON response
            return Response.ok(jsonResponse).build();
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "CompjobService", "CompjobService", "getJson");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error generating JSON response").build();
        }
    }
    
    @GET
    @Path("/test_auth")
    @JWTTokenNeeded
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthJson() {
        try {
            // Create the response object
            AuthResponse authResponse = new AuthResponse("success", "Precheck Service Authentication successful");

            // Convert the response object to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(authResponse);

            // Return the JSON response
            return Response.ok(jsonResponse).build();
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "CompjobService", "CompjobService", "getAuthJson");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error generating JSON response").build();
        }
    }

    /**
     * Retrieves representation of an instance of com.webservice.CompjobService
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of CompjobService
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    
    @POST
    @JWTTokenNeeded
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response notifyCompJob(
            @FormParam("usj_no") String usjNo,
            @FormParam("div_no") String divNo,
            @FormParam("job_action") String jobAction,
            @FormParam("status_date") String statusDate) {

        try {
            CommonFunction.writeFile("CompjobService", "CompjobService" + " >>> " + "notifyCompJob" + " :: " + "=============== Calling notifyCompJob =============== ");
            CommonFunction.writeFile("CompjobService", "CompjobService" + " >>> " + "notifyCompJob" + " :: " + "USJ NO " + usjNo + " || DIV NO " + divNo + " || job_action " + jobAction + " || status_date " + statusDate);
            
            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            Map updateProgressResult = new HashMap();
            updateProgressResult = serviceFactory.getSubmissionJobService().updateJobProgress(usjNo, divNo, jobAction, statusDate);

            CommonFunction.writeFile("CompjobService", "CompjobService" + " >>> " + "notifyCompJob" + " :: " + "updateProgressResult " + updateProgressResult);
            JSONObject jsonObject = new JSONObject(updateProgressResult);
            
            AuthResponse authResponse = new AuthResponse("success", "Request successful");
            authResponse.setResult((String) updateProgressResult.get("result"));
            authResponse.setBody(jsonObject);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(authResponse);

            return Response.ok(jsonResponse).build();

        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "CompjobService", "CompjobService", "notifyCompJob");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Update failed").build();
        } finally {
            CommonFunction.writeFile("CompjobService", "CompjobService" + " >>> " + "notifyCompJob" + " :: " + "=============== Calling notifyCompJob end =============== ");
        }
    }
}
