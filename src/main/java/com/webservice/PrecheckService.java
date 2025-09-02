/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package com.webservice;

import com.utimaps.model.JobDetailModel;
import com.sains.common.util.Validator;
import com.webservice.util.JWTTokenNeeded;
import com.webservice.util.AuthResponse;
import com.utimaps.model.FileModel;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.InputStream;
import com.sains.framework.base.Debug;
import com.sains.common.util.FtpInterface;
import com.sains.common.util.FileOperationUtil;
import java.nio.file.Files;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pb.web.AttachmentUploadAction;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import java.nio.file.Paths;
import org.json.simple.JSONObject;
import javax.ws.rs.Path;

import java.util.List;
import com.sains.framework.base.CommonFunction;

/**
 * REST Web Service
 *
 * @author Aiman
 */
@Path("precheck")
public class PrecheckService {

    // PbUtimapsApiAction pbAction = new PbUtimapsApiAction();
    PrecheckServiceAction pcAction = new PrecheckServiceAction();
    private CommonFunction cf = new CommonFunction();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PrecheckResource
     */
    public PrecheckService() {
    }

    /**
     * Retrieves representation of an instance of
     * com.webservice.PrecheckResource
     *
     * @return an instance of java.lang.String
     */
    // @GET
    // @Produces(MediaType.APPLICATION_JSON)
    // public String getJson() {
    // //TODO return proper representation object
    // throw new UnsupportedOperationException();
    // }
    @GET
    @Path("/test")
//     @JWTTokenNeededs
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
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error generating JSON response")
                    .build();
        }
    }

    @GET
    @Path("/debug/stage_check")
    @JWTTokenNeeded
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStage(
            @QueryParam("USJ_NO") String usjNo,
            @QueryParam("DIV_NO") String divNo) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String usjSeq;
            String usjYear;

            if (usjNo != null && !Validator.isEmpty(usjNo)) {
                // Split usjNo into usjSeq and usjYear
                if (usjNo.length() == 8) {
                    usjSeq = usjNo.substring(0, 4);
                    usjYear = usjNo.substring(4);
                    System.out.println("USJ SEQ: " + usjSeq);
                    System.out.println("USJ YEAR: " + usjYear);
                } else {
                    System.out.println("Invalid USJ NO format");
                    AuthResponse errorResponse = new AuthResponse("error", "Invalid USJ No format");
                    String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                    cf.writeFile("PrecheckProgressAPILog", jsonResponse);
                    cf.writeFile("PrecheckProgressAPILog", "Conn Ended - " + usjNo);
                    return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
                }
            } else {
                System.out.println("No USJ Number");
                AuthResponse errorResponse = new AuthResponse("error", "No Survey Job Number Found");
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                cf.writeFile("PrecheckProgressAPILog", jsonResponse);
                cf.writeFile("PrecheckProgressAPILog", "Conn Ended - " + usjNo);
                return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
            }

            // Convert the response object to JSON
            AuthResponse authResponse = new AuthResponse("success", "Stage Check Connection successful");
            JobDetailModel jobDetailModel = pcAction.getJobDetail(usjSeq, usjYear, divNo);

            if (jobDetailModel != null) {

                JSONObject responseBody = new JSONObject();
                JSONObject jobDetails = new JSONObject();
                jobDetails.put("job_id", jobDetailModel.getJob_id());
                jobDetails.put("precheck_stage", jobDetailModel.getPrecheck_stage());
                jobDetails.put("notif_sent", jobDetailModel.getPrecheck_notif_sent());
                jobDetails.put("job_user", jobDetailModel.getApplicationModel().getApp_submit_by());
                responseBody.put("jobdetails", jobDetails);

                List travHistList = pcAction.getTraverseHistoryList(jobDetailModel);

                if (travHistList != null) {
                    responseBody.put("trav_history", travHistList);
                }

                authResponse.setBody(responseBody);
            } else {
                authResponse = new AuthResponse("error", "Job Model not found! ");
            }

            String jsonResponse = objectMapper.writeValueAsString(authResponse);
            // Return the JSON response
            return Response.ok(jsonResponse).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error generating JSON response")
                    .build();
        }
    }

    @POST
    @Path("/debug/stage_move")
//    @JWTTokenNeeded
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFiles(
            @FormDataParam("USJ_NO") String usjNo,
            @FormDataParam("DIV_NO") String divNo,
            @FormDataParam("ORI_STAGE") String oriStage,
            @FormDataParam("NEW_STAGE") String newStage) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String usjSeq;
            String usjYear;

            if (usjNo != null && !Validator.isEmpty(usjNo)) {
                // Split usjNo into usjSeq and usjYear
                if (usjNo.length() == 8) {
                    usjSeq = usjNo.substring(0, 4);
                    usjYear = usjNo.substring(4);
                    System.out.println("USJ SEQ: " + usjSeq);
                    System.out.println("USJ YEAR: " + usjYear);
                } else {
                    System.out.println("Invalid USJ NO format");
                    AuthResponse errorResponse = new AuthResponse("error", "Invalid USJ No format");
                    String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                    cf.writeFile("PrecheckProgressAPILog", jsonResponse);
                    cf.writeFile("PrecheckProgressAPILog", "Conn Ended - " + usjNo);
                    return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
                }
            } else {
                System.out.println("No USJ Number");
                AuthResponse errorResponse = new AuthResponse("error", "No Survey Job Number Found");
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                cf.writeFile("PrecheckProgressAPILog", jsonResponse);
                cf.writeFile("PrecheckProgressAPILog", "Conn Ended - " + usjNo);
                return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
            }

            // Convert the response object to JSON
            AuthResponse authResponse = new AuthResponse("success", "Stage Check Connection successful");
            JobDetailModel jobDetailModel = pcAction.getJobDetail(usjSeq, usjYear, divNo);

            if (jobDetailModel != null) {

                if (!jobDetailModel.getPrecheck_stage().equals(oriStage)) {
                    AuthResponse errorResponse = new AuthResponse("error", "Param stage doesn't match with current stage!");
                    String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                    return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
                }

                JSONObject responseBody = new JSONObject();
                JSONObject jobDetails = new JSONObject();
                jobDetails.put("job_id", jobDetailModel.getJob_id());
                jobDetails.put("old_precheck_stage", jobDetailModel.getPrecheck_stage());

                jobDetailModel = pcAction.updateManualPrecheckStage(jobDetailModel, newStage);

                jobDetails.put("new_precheck_stage", jobDetailModel.getPrecheck_stage());
                responseBody.put("jobdetails", jobDetails);
                authResponse.setBody(responseBody);
            } else {
                authResponse = new AuthResponse("error", "Job Model not found! ");
            }

            String jsonResponse = objectMapper.writeValueAsString(authResponse);
            // Return the JSON response
            return Response.ok(jsonResponse).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("File upload failed").build();
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
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error generating JSON response")
                    .build();
        }
    }

    @GET
    @Path("/status")
    @JWTTokenNeeded
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatus(
            @QueryParam("USJ_NO") String usjNo,
            @QueryParam("DIV_NO") String divNo) {

        try {
            // Create the response object
            AuthResponse authResponse = new AuthResponse("success", "get status successful");

            // Create a JSON object for job details
            JSONObject jobDetails = new JSONObject();
            // jobDetails.put("job_id", 12345);
            jobDetails.put("job_status", "processing");
            jobDetails.put("job_description", "dummy job description.");

            // Set the job details in the response body
            JSONObject responseBody = new JSONObject();
            responseBody.put("status", "active");
            responseBody.put("jobdetails", jobDetails);

            authResponse.setBody(responseBody);

            // Convert the response object to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(authResponse);

            // Return the JSON response
            return Response.ok(jsonResponse).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error generating JSON response")
                    .build();
        }
    }

    /**
     * PUT method for updating or creating an instance of PrecheckResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    @POST
    @Path("/download")
    @JWTTokenNeeded
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postPcJob(
            @FormParam("JOB_ID") String jobId,
            // @FormParam("usjNo") String usjNo,
            // @FormParam("divNo") String divNo,
            @FormParam("FILE_TYPE") String fileType) {

        File file = null;
        String mimeType = "application/octet-stream";

        // Generate or retrieve the file based on fileType
        System.out.println("ftype - " + fileType);

        switch (fileType) {
            case "SJI":
            case "FBL":
            case "PS3":
            case "RSO":
                file = pcAction.getFileById(jobId, fileType);
                break;
            default:
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid file type from request").build();
        }

        if (file != null && file.exists()) {
            try {
                mimeType = Files.probeContentType(file.toPath());
                if (mimeType == null) {
                    mimeType = "application/octet-stream"; // Default MIME type if undetermined
                }

                System.out.println("mime - " + mimeType);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return Response.ok(file)
                    .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                    .type(mimeType)
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("File not found").build();
        }
    }

    @POST
    @Path("/init")
    @JWTTokenNeeded
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFiles(
            @FormDataParam("usj_no") String usjNo,
            @FormDataParam("div_no") String divNo,
            @FormDataParam("Survey_Job") InputStream RSOInputStream,
            @FormDataParam("Survey_Job") FormDataContentDisposition RSODetail,
            @FormDataParam("PS3") InputStream PS3InputStream,
            @FormDataParam("PS3") FormDataContentDisposition PS3Detail,
            @FormDataParam("FBL") InputStream FBLInputStream,
            @FormDataParam("FBL") FormDataContentDisposition FBLDetail) {

        try {

            System.out.println("USJ NO " + usjNo);
            System.out.println("DIV NO " + divNo);

            System.out.println("RSO NAME " + RSODetail.getFileName());
            System.out.println("PS3 NAME " + PS3Detail.getFileName());
            System.out.println("FBL NAME " + FBLDetail.getFileName());

            AuthResponse authResponse = new AuthResponse("success", "Request successful");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(authResponse);

            System.out.println("Starting delay...");
            for (int i = 1; i <= 7; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println("Delaying... " + i + " second(s)");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Delay ended.");

            return Response.ok(jsonResponse).build();

            // return Response.ok("success").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("File upload failed").build();
        }
    }

    @POST
    @Path("/progress")
    @JWTTokenNeeded
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendProgressMessage(
            @FormDataParam("usj_no") String usjNo,
            @FormDataParam("div_no") String divNo,
            @FormDataParam("progress_signal") String progressSignal,
            @FormDataParam("progress_step") String progressStep,
            @FormDataParam("progress_msg") String progressMsg,
            @FormDataParam("progress_date") String progressDate) {

        try {
            cf.writeFile("PrecheckProgressAPILog", "========== Conn Initiated - " + usjNo + " - " + divNo + " ==========");
            System.out.println("USJ NO: " + usjNo);
            System.out.println("DIV NO: " + divNo);
            System.out.println("Progress Step: " + progressStep);
            System.out.println("Progress Message: " + progressMsg);
            System.out.println("Progress Date: " + progressDate);

            String usjSeq = "";
            String usjYear = "";
            ObjectMapper objectMapper = new ObjectMapper();

            if (usjNo != null && !Validator.isEmpty(usjNo)) {
                // Split usjNo into usjSeq and usjYear
                if (usjNo.length() == 8) {
                    usjSeq = usjNo.substring(0, 4);
                    usjYear = usjNo.substring(4);
                    System.out.println("USJ SEQ: " + usjSeq);
                    System.out.println("USJ YEAR: " + usjYear);
                } else {
                    System.out.println("Invalid USJ NO format");
                    AuthResponse errorResponse = new AuthResponse("error", "Invalid USJ No format");
                    String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                    cf.writeFile("PrecheckProgressAPILog", jsonResponse);
                    cf.writeFile("PrecheckProgressAPILog", "Conn Ended - " + usjNo);
                    return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
                }
            } else {
                System.out.println("No USJ Number");
                AuthResponse errorResponse = new AuthResponse("error", "No Survey Job Number Found");
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                cf.writeFile("PrecheckProgressAPILog", jsonResponse);
                cf.writeFile("PrecheckProgressAPILog", "Conn Ended - " + usjNo);
                return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
            }

            int progressStepInt = 1;
            if (progressStep != null && !Validator.isEmpty(progressStep)) {
                progressStepInt = Integer.parseInt(progressStep);
            }

            String statusProg = pcAction.insertTraverseHistory(usjSeq, usjYear, divNo, progressMsg, progressDate, progressSignal, progressStepInt);

            if (statusProg.equals("success")) {
                Debug.printDebug("Successfully Inserted Traverse Progress");
            } else {
                switch (statusProg) {
                    case "fail_no_job":
                        System.out.println("No Job Found");
                        AuthResponse errorResponse = new AuthResponse("error", "No Survey Job with the provided No. was found.");
                        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                        cf.writeFile("PrecheckProgressAPILog", jsonResponse);
                        cf.writeFile("PrecheckProgressAPILog", "Conn Ended - " + usjNo);
                        return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
                }
            }

            // TODO: Add logic to process and store the progress message
            AuthResponse authResponse = new AuthResponse("success", "Progress message received");

            String jsonResponse = objectMapper.writeValueAsString(authResponse);

            cf.writeFile("PrecheckProgressAPILog", jsonResponse);
            cf.writeFile("PrecheckProgressAPILog", "Conn Ended - " + usjNo);
            return Response.ok(jsonResponse).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to process progress message").build();
        }
    }

    @POST
    @Path("/debug/upload_static")
    @JWTTokenNeeded
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("fileType") String fileType) {

        try {
            cf.writeFile("FileUploadAPILog", "=====CALL===== File Upload ==========");
            System.out.println("File Type: " + fileType);

            if (fileInputStream == null || fileDetail == null) {
                AuthResponse errorResponse = new AuthResponse("error", "No file uploaded");
                String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);
                cf.writeFile("FileUploadAPILog", jsonResponse);
                cf.writeFile("FileUploadAPILog", "=====RETURN===== File Upload Failed ==========");
                return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
            }

            AttachmentUploadAction attachmentUploadAction = new AttachmentUploadAction();
            attachmentUploadAction.setDrFileCode_(fileType);
            attachmentUploadAction.setUppyFileFileName(fileDetail.getFileName());
            String uploadResult = attachmentUploadAction.apiUploadStatic(fileInputStream, fileType, fileDetail.getFileName());

            AuthResponse successResponse = new AuthResponse("error", "File failed to upload.");
            String jsonResponse = new ObjectMapper().writeValueAsString(successResponse);

            if (uploadResult.equals("success")) {
                String resMsg = "uploaded " + fileType + " into OBS";
                Debug.printDebug(resMsg);
                successResponse = new AuthResponse("success", "File uploaded successfully.");
                successResponse.setResult(resMsg);
                jsonResponse = new ObjectMapper().writeValueAsString(successResponse);
            }

            cf.writeFile("FileUploadAPILog", jsonResponse);
            cf.writeFile("FileUploadAPILog", "=====RETURN===== File Upload Success ==========");
            return Response.ok(jsonResponse).build();

        } catch (Exception e) {
            e.printStackTrace();
            cf.writeFile("FileUploadAPILog", "Error: " + e.getMessage());
            cf.writeFile("FileUploadAPILog", "=====RETURN===== File Upload Failed ==========");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to upload file").build();
        }
    }

    @POST
    @Path("/output")
    @JWTTokenNeeded
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadOutput(
            @FormDataParam("usj_no") String usjNo,
            @FormDataParam("div_no") String divNo,
            @FormDataParam("Status") InputStream StatusInputStream,
            @FormDataParam("Status") FormDataContentDisposition StatusDetail,
            @FormDataParam("Error") InputStream ErrorInputStream,
            @FormDataParam("Error") FormDataContentDisposition ErrorDetail,
            @FormDataParam("Report_O") InputStream ReportOInputStream,
            @FormDataParam("Report_O") FormDataContentDisposition ReportODetail,
            @FormDataParam("Report_N") InputStream ReportNInputStream,
            @FormDataParam("Report_N") FormDataContentDisposition ReportNDetail,
            @FormDataParam("Report_RoRLn") InputStream ReportRoRLNInputStream,
            @FormDataParam("Report_RoRLn") FormDataContentDisposition ReportRoRLNDetail,
            @FormDataParam("Report_GeneralCheck") InputStream ReportGeneralCheckInputStream,
            @FormDataParam("Report_GeneralCheck") FormDataContentDisposition ReportGeneralCheckDetail) {

        try {
            cf.writeFile("PrecheckOutputAPILog", "=====CALL===== " + usjNo + " - " + divNo + " ==========");
            System.out.println("USJ NO " + usjNo);
            System.out.println("DIV NO " + divNo);
            String usjSeq = "";
            String usjYear = "";

            ObjectMapper objectMapper = new ObjectMapper();

            if (usjNo != null && !Validator.isEmpty(usjNo)) {
                // Split usjNo into usjSeq and usjYear
                if (usjNo.length() == 8) {
                    usjSeq = usjNo.substring(0, 4);
                    usjYear = usjNo.substring(4);
                    System.out.println("USJ SEQ: " + usjSeq);
                    System.out.println("USJ YEAR: " + usjYear);
                } else {
                    System.out.println("Invalid USJ NO format");
                    AuthResponse errorResponse = new AuthResponse("error", "Invalid USJ No format");
                    String jsonResponse = objectMapper.writeValueAsString(errorResponse);

                    cf.writeFile("PrecheckOutputAPILog", jsonResponse);
                    cf.writeFile("PrecheckOutputAPILog", "=====RETURN===== " + usjNo + " - " + divNo + " ==========");

                    return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
                }
            } else {
                System.out.println("No USJ Number");
                AuthResponse errorResponse = new AuthResponse("error", "No Survey Job Number Found");
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);

                cf.writeFile("PrecheckOutputAPILog", jsonResponse);
                cf.writeFile("PrecheckOutputAPILog", "=====RETURN===== " + usjNo + " - " + divNo + " ==========");

                return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
            }

//            System.out.println("RSO NAME " + RSODetail.getFileName());
//            System.out.println("PS3 NAME " + PS3Detail.getFileName());
//            System.out.println("FBL NAME " + FBLDetail.getFileName());
            // System.out.println("RESULT NAME " + StatusDetail.getFileName());
            // System.out.println("ERROR NAME " + ErrorDetail.getFileName());
            // Helper method to check if a file was actually sent
            // Create a JSON object to store file reception status
            JSONObject fileStatus = new JSONObject();
            JSONObject body = new JSONObject();
            fileStatus.put("Error", isFileUploaded(ErrorDetail, ErrorInputStream) ? "Received" : "Not Received");
            fileStatus.put("Status", isFileUploaded(StatusDetail, StatusInputStream) ? "Received" : "Not Received");
            fileStatus.put("Report_O", isFileUploaded(ReportODetail, ReportOInputStream) ? "Received" : "Not Received");
            fileStatus.put("Report_N", isFileUploaded(ReportNDetail, ReportNInputStream) ? "Received" : "Not Received");
            fileStatus.put("Report_RoRLN", isFileUploaded(ReportRoRLNDetail, ReportRoRLNInputStream) ? "Received" : "Not Received");
            fileStatus.put("Report_GeneralCheck", isFileUploaded(ReportGeneralCheckDetail, ReportGeneralCheckInputStream) ? "Received" : "Not Received");

            // Create a JSON object to store file reception status
            // JSONObject fileStatus = new JSONObject();
            // fileStatus.put("Status", StatusDetail != null ? "Received" : "Not Received");
            // fileStatus.put("Error", ErrorDetail != null ? "Received" : "Not Received");
            // fileStatus.put("Report_O", ReportODetail != null ? "Received" : "Not Received");
            // fileStatus.put("Report_N", ReportNDetail != null ? "Received" : "Not Received");
            // fileStatus.put("Report_RoRLN", ReportRoRLNDetail != null ? "Received" : "Not Received");
            // fileStatus.put("Report_GeneralCheck", ReportGeneralCheckDetail != null ? "Received" : "Not Received");
            body.put("filesReceived", fileStatus);
            cf.writeFile("PrecheckOutputAPILog", fileStatus.toString());

            AttachmentUploadAction attachmentUploadAction = new AttachmentUploadAction();

            if (isFileUploaded(StatusDetail, StatusInputStream)) {
                System.out.println("status not null?");
                // upload the status file with directUploa()
                attachmentUploadAction.setDrFileCode_("STA");
                attachmentUploadAction.setUppyFileFileName(StatusDetail.getFileName());
                String statusUpload = attachmentUploadAction.apiUpload(usjSeq, usjYear, divNo, StatusInputStream, StatusDetail);

                if (statusUpload.equals("success")) {
                    Debug.printDebug("uploaded STA into OBS");
                } else {
                    switch (statusUpload) {
                        case "fail_no_job":
                            System.out.println("No Job Found");
                            AuthResponse errorResponse = new AuthResponse("error", "No Survey Job with the provided No. was found.");
                            String jsonResponse = objectMapper.writeValueAsString(errorResponse);

                            cf.writeFile("PrecheckOutputAPILog", jsonResponse);
                            cf.writeFile("PrecheckOutputAPILog", "=====RETURN===== " + usjNo + " - " + divNo + " ==========");

                            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
                    }
                }
            }

            if (isFileUploaded(ErrorDetail, ErrorInputStream)) {
                attachmentUploadAction.setDrFileCode_("ERR");
                attachmentUploadAction.setUppyFileFileName(ErrorDetail.getFileName());
                String errorUpload = attachmentUploadAction.apiUpload(usjSeq, usjYear, divNo, ErrorInputStream, ErrorDetail);

                if (errorUpload.equals("success")) {
                    Debug.printDebug("uploaded ERR into OBS");
                }
            }

            if (isFileUploaded(ReportODetail, ReportOInputStream)) {
                attachmentUploadAction.setDrFileCode_("RPO");
                attachmentUploadAction.setUppyFileFileName(ReportODetail.getFileName());
                String reportOUpload = attachmentUploadAction.apiUpload(usjSeq, usjYear, divNo, ReportOInputStream, ReportODetail);

                if (reportOUpload.equals("success")) {
                    Debug.printDebug("uploaded RPO into OBS");
                }
            }

            if (isFileUploaded(ReportNDetail, ReportNInputStream)) {
                attachmentUploadAction.setDrFileCode_("RPN");
                attachmentUploadAction.setUppyFileFileName(ReportNDetail.getFileName());
                String reportNUpload = attachmentUploadAction.apiUpload(usjSeq, usjYear, divNo, ReportNInputStream, ReportNDetail);

                if (reportNUpload.equals("success")) {
                    Debug.printDebug("uploaded RPN into OBS");
                }
            }

            if (isFileUploaded(ReportRoRLNDetail, ReportRoRLNInputStream)) {
                attachmentUploadAction.setDrFileCode_("RPR");
                attachmentUploadAction.setUppyFileFileName(ReportRoRLNDetail.getFileName());
                String reportRoRLNUpload = attachmentUploadAction.apiUpload(usjSeq, usjYear, divNo, ReportRoRLNInputStream, ReportRoRLNDetail);

                if (reportRoRLNUpload.equals("success")) {
                    Debug.printDebug("uploaded RPR into OBS");
                }
            }

            if (isFileUploaded(ReportGeneralCheckDetail, ReportGeneralCheckInputStream)) {
                attachmentUploadAction.setDrFileCode_("RPG");
                attachmentUploadAction.setUppyFileFileName(ReportGeneralCheckDetail.getFileName());
                String reportGeneralCheckUpload = attachmentUploadAction.apiUpload(usjSeq, usjYear, divNo, ReportGeneralCheckInputStream, ReportGeneralCheckDetail);

                if (reportGeneralCheckUpload.equals("success")) {
                    Debug.printDebug("uploaded RPG into OBS");
                }
            }

            // if all goes well
            cf.writeFile("PrecheckOutputAPILog", "========================================\n");
            AuthResponse authResponse = new AuthResponse("success", "Request successful");
            authResponse.setBody(body);
            String jsonResponse = objectMapper.writeValueAsString(authResponse);

            cf.writeFile("PrecheckOutputAPILog", jsonResponse);
            cf.writeFile("PrecheckOutputAPILog", "=====RETURN OK ===== " + usjNo + " - " + divNo + " ==========");

            return Response.ok(jsonResponse).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("File upload failed").build();
        }
    }

    boolean isFileUploaded(FormDataContentDisposition fileDetail, InputStream fileInputStream) {
        return fileDetail != null && fileInputStream != null && fileDetail.getFileName() != null && !fileDetail.getFileName().isEmpty();

    }

    // 23092024
    // a post endpoint for the purpsose of sending output files back to Java side
    // from PCS API
    // @POST
    // @Path("/batch")
    //// @JWTTokenNeeded
    // @Consumes(MediaType.MULTIPART_FORM_DATA)
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response uploadBatchFiles(
    // @FormDataParam("usj_no") String usjNo,
    // @FormDataParam("div_no") String divNo,
    // // @FormDataParam("file_attachment") List<FormDataContentDisposition>
    // fileDetails,
    // // @FormDataParam("file_attachment") List<InputStream> fileInputs) {
    // @FormDataParam("file_attachment") FormDataContentDisposition fileDetails,
    // @FormDataParam("file_attachment") InputStream fileInputs) {
    //
    // try {
    //
    // if (fileDetails == null || fileInputs == null) {
    // return Response.status(Response.Status.BAD_REQUEST).entity("Missing file form
    // data parameters").build();
    // }
    //
    // System.out.println("USJ NO " + usjNo);
    // System.out.println("DIV NO " + divNo);
    // System.out.println("file detail NO " + fileDetails.getFileName());
    //
    //// for (int i = 0; i < fileDetails.size(); i++) {
    //// FormDataContentDisposition detail = fileDetails.get(i);
    //// InputStream inputStream = fileInputs.get(i);
    //// System.out.println("File Name: " + detail.getFileName());
    // // Process each file InputStream as needed.
    // // Example: save the inputStream to a file
    //// }
    // AuthResponse authResponse = new AuthResponse("success", "Request
    // successful");
    //
    // ObjectMapper objectMapper = new ObjectMapper();
    // String jsonResponse = objectMapper.writeValueAsString(authResponse);
    //
    // return Response.ok(jsonResponse).build();
    //
    //// return Response.ok("success").build();
    // } catch (Exception e) {
    // e.printStackTrace();
    // return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("File
    // upload failed").build();
    // }
    // }
    @POST
    @Path("/batch")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadBatchFiles(FormDataMultiPart multiPart) {

        try {
            String usjNo = multiPart.getField("usj_no").getValue();
            String divNo = multiPart.getField("div_no").getValue();

            System.out.println("USJ NO " + usjNo);
            System.out.println("DIV NO " + divNo);
//            System.out.println("file detail NO " + fileDetails.getFileName());

            List<FormDataBodyPart> fileParts = multiPart.getFields("file_attachment");

            for (FormDataBodyPart filePart : fileParts) {
                FormDataContentDisposition fileDetail = filePart.getFormDataContentDisposition();
                InputStream fileInputStream = filePart.getValueAs(InputStream.class);
                System.out.println("File Name In API endpoint : " + fileDetail.getFileName());
            }

            AuthResponse authResponse = new AuthResponse("success", "All commands executed successfully");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(authResponse);

            return Response.ok(jsonResponse).build();

//            return Response.ok("success").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("File upload failed").build();
        }
    }
}
