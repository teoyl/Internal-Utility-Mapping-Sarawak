/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pb.web;

import com.SysConf;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.FileOperationUtil;
import com.sains.common.util.FtpInterface;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import com.sains.framework.base.SessionFactoryImpl;
import static com.sains.framework.base.web.UppyUploadAction.getAppCode_modelClass;
import com.sains.framework.model.DrDocRepoModel;
import com.sains.framework.model.NullModel;
import com.sains.framework.model.User;
import com.utimaps.model.FileModel;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;
import org.apache.tika.Tika;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.struts2.ServletActionContext;
import com.utimaps.web.UtimapsAction;
import java.io.IOException;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.FileOutputStream;
import com.sains.common.util.SystemConstants;
import java.nio.file.Path;
import java.nio.file.Files;

import com.utimaps.model.JobDetailModel;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 *
 * @author Aiman
 */
public class AttachmentUploadAction extends BaseActionSupport<FileModel> implements ModelDriven<FileModel> {

    public AttachmentUploadAction() {
        // change the DAO to the correct service.
        // baseDAO = new BaseDAOImpl();
        model = new FileModel();
        // set the required field for common check.
        // getRequiredParam().put("column_name1", "column.name1");
        // getRequiredParam().put("column_name2", "column.name2");
    }

    @Override // change the "String" and return value to the
    public FileModel getModel() {
        return model;
    }

    InputStream uppyFile_stream = null;
    File uppyFile;

    String uppyFileFileName;
    String drFileCode_ = null;

    public String uploadID;

    public String reqID;
    public String reqType;
    public String reqFile;

    private String uploadRecordId_ = null;
    private String uploadedFileName = null;
    private String caseId_ = null;
    private String process_ = null;

    FileModel uploadingModel = null;
    String uploadingModel_fk = null;

    private String contentType = "application/pdf";
    private String contentDisposition = "";
    private InputStream inputStream = null;
    private String tn_ = "N";

    private CommonFunction cf = new CommonFunction();

    public synchronized String uploadPath(String fileCode) throws Exception {
        String thePath = null;
        thePath = fileCode;
        return thePath;
    }

    private static Map<String, Map> appCode_modelClass_UppyUpload_appCodeSetup = null;
    private static Map<String, Class> appCode_modelClass = null;

    public static synchronized Map<String, Class> getAppCode_modelClass() {
        if (appCode_modelClass == null) {
            appCode_modelClass = new HashMap();
            appCode_modelClass_UppyUpload_appCodeSetup = new HashMap();
            for (Class regClass : SessionFactoryImpl.registeredClass) {
                try {
                    ModelBase modelBase;
                    try {
                        Class c = Class.forName(regClass.getPackage().getName() + "." + regClass.getSimpleName() + "Service");
                        modelBase = (ModelBase) c.getDeclaredConstructor(ModelBase.class, ModelBase.class).newInstance(regClass.newInstance(), null);
                    } catch (Exception e) {
                        modelBase = (ModelBase) regClass.newInstance();
                    }

                    for (String fileCode : modelBase.getUppyUpload_appCodeSetup().keySet()) {
                        appCode_modelClass.put(fileCode, regClass);
                        appCode_modelClass_UppyUpload_appCodeSetup.put(fileCode, modelBase.getUppyUpload_appCodeSetup());
                    }
                } catch (Exception e) {
                }
            }
        }
        return appCode_modelClass;
    }

    // =============================================================================================================================
    public String upload() throws Exception {
//        Debug.printDebug("=============================================================");
//        Debug.printDebug("upload file ***** " + new java.sql.Timestamp(System.currentTimeMillis()));
//        Debug.printDebug(uploadRecordId_);
        BaseDAO dao = baseDAO;

        // usage of Tika is to check legitimacy of file uploaded (preventing file extension changes/content tampering)
        Tika tika = new Tika();
        String contentMime = tika.detect(uppyFile);
        System.out.println("genuine mimetype - " + contentMime);

        String fileMimeType = ServletActionContext.getServletContext().getMimeType(uppyFileFileName);
//        Debug.printDebug("Uppy Upload Action mimeType = " + fileMimeType);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Map jsonMap = new HashMap();
        response.setContentType("application/json");
        // 05/07/2024 include doc and docx uploading capability
        List<String> mimeTypeList = Arrays.asList("image/jpeg", "image/png", "application/zip", "application/pdf",
                "image/vnd.dxf", "application/x-zip", "text/plain", "text/csv", "image/tiff", "text/x-cps",
                "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.google-earth.kml+xml", "application/vnd.google-earth.kmz"); // filter upload type

//        Debug.printDebug("mimeType - " + fileMimeType);
        if (contentMime != null) {
            switch (fileMimeType) {
                case "text/x-cps":
                    analyzeCpsFile(uppyFile);

                    Boolean isAscii = isAsciiCompatible(uppyFile);
                    if (!isAscii) {
                        jsonMap.put("status", "fail");
                        jsonMap.put("errMsg", "Upload Failed: The .cps file contains non-ASCII characters. Please ensure the file contains only ASCII text.");
                        
                        response.getWriter().append(new Gson().toJson(jsonMap));
                        response.flushBuffer();
                        return null;
                    }
                    break;
                case "text/csv":
                    break;
                default:
                    if (!fileMimeType.equals(contentMime)) {
                        jsonMap.put("status", "fail");
                        jsonMap.put("errMsg", "Upload Failed: File extension does not match its content. Please upload a valid file.");

                        response.getWriter().append(new Gson().toJson(jsonMap));
                        response.flushBuffer();
                        return null;
                    }

                    break;
            }
        }

        if (mimeTypeList.contains(fileMimeType)) {
            FtpInterface ftp = FileOperationUtil.getFtpInterface();
            String strValue = Long.toString(System.currentTimeMillis()).substring(0, 10);

            if (Validator.isEmpty(uploadedFileName)) { //joveni @ 21/6/2022 :: check if null
                uploadedFileName = drFileCode_.concat("_").concat(strValue).concat(uppyFileFileName.substring(uppyFileFileName.lastIndexOf(".")));
            } else {
                uploadedFileName = uploadedFileName.concat("_").concat(strValue).concat(uppyFileFileName.substring(uppyFileFileName.lastIndexOf(".")));
            }

//            Debug.printDebug("upload file " + uploadedFileName);
//            Debug.printDebug("upload file " + uppyFile);
            InputStream inStream = new FileInputStream(uppyFile);
            String newDrDocId = CommonFunction.getId(20);

            try {
                String sftpUploadPath = null;
                sftpUploadPath = uploadPath(drFileCode_);
                ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
                if (ftp.isDirExists(SysConf.get("pathPrefix") + sftpUploadPath, Boolean.FALSE)) {
                    ftp.createDirIfNotExists(sftpUploadPath + "/" + uploadRecordId_, Boolean.FALSE);
                    ftp.createFile(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId, uppyFile);// use File as parameter
                    ftp.insertToFileDirectory(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId, uppyFileFileName, fileMimeType, newDrDocId, baseDAO, drFileCode_, Boolean.FALSE);

                    if (fileMimeType.equals("application/zip") || fileMimeType.equals("application/x-zip")) {
                        // Call the new function to unzip and upload contents
                        if (drFileCode_.equals("DSP")) {
                            String unzipStatus = unzipAndUploadContents(drFileCode_, uppyFile, uploadRecordId_, dao,
                                    jsonMap);

                            if (!unzipStatus.equals("pass")) {
                                jsonMap.put("status", "fail");

                                switch (unzipStatus) {
                                    case "fail_name_format":
                                        jsonMap.put("errMsg",
                                                "File name format is invalid. Please ensure the Digital Survey Plans are named with format given in instructions.");
                                        break;
                                    case "fail_folder":
                                        jsonMap.put("errMsg",
                                                "A folder was found in the .zip file, please ensure Digital Survey Plans are not kept in a folder within the .zip file.");
                                        break;
                                    case "fail_non_accepted_file":
                                        jsonMap.put("errMsg",
                                                "A file with an invalid format was found. Please ensure the .zip file contains only PDF Digital Survey Plans.");
                                        break;
                                    default:
                                        jsonMap.put("errMsg", "Unable to upload .zip file, please try again later.");
                                        break;
                                }

                                response.setContentType("application/json");
                                response.getWriter().append(new Gson().toJson(jsonMap));
                                response.flushBuffer();

                                try {
                                    ftp.deleteFile(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId);
                                } catch (Exception delEx) {
                                    delEx.printStackTrace();
                                }

                                return null;
                            } else {

                            }
                        } else if (drFileCode_.equals("DSD")) {
                            // String unzipStatus = unzipAndUploadContents(drFileCode_, uppyFile,
                            // uploadRecordId_, dao, jsonMap);
                            String zipStatus = zipFileCheck(drFileCode_, uppyFile, uploadRecordId_, dao, jsonMap);

                            if (!zipStatus.equals("pass")) {
                                jsonMap.put("status", "fail");

                                if (zipStatus.equals("fail_folder")) {
                                    jsonMap.put("errMsg",
                                            "A folder was found in the .zip file, please ensure Utility Survey Data files are not kept in a folder within the .zip file.");
                                } else if (zipStatus.equals("fail_non_accepted_file")) {
                                    jsonMap.put("errMsg",
                                            "A file with an invalid format was found. Please ensure the .zip file contains only valid digital survey data files.");
                                } else if (zipStatus.contains("fail_missing_file_")) {
                                    String missingFiles = zipStatus.substring("fail_missing_file_".length());

                                    String[] missingFileTypes = missingFiles.substring(1, missingFiles.length() - 1).split(", ");
                                    StringBuilder formattedMissingFiles = new StringBuilder();
                                    for (int i = 0; i < missingFileTypes.length; i++) {
                                        formattedMissingFiles.append("\".")
                                                .append(missingFileTypes[i])
                                                .append("\"");
                                        if (i < missingFileTypes.length - 1) {
                                            formattedMissingFiles.append(", ");
                                        }
                                    }
                                    missingFiles = formattedMissingFiles.toString();
                                    Debug.printDebug("missingFiles - " + missingFiles);

                                    jsonMap.put("errMsg", "Unable to upload .zip file due to missing required data file types: ["
                                            + missingFiles
                                            + "]\n\nPlease check and include a complete set of digital survey data files.");
                                } else {
                                    jsonMap.put("errMsg", "Unable to upload .zip file, please try again later.");
                                }

                                response.setContentType("application/json");
                                response.getWriter().append(new Gson().toJson(jsonMap));
                                response.flushBuffer();

                                try {
                                    ftp.deleteFile(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId);
                                } catch (Exception delEx) {
                                    delEx.printStackTrace();
                                }

                                return null;
                            } else {

                            }

                        }
                    }

                    if (uploadingModel != null) {
                        try {
                            updateUpdatingModel(newDrDocId); // joveni @ 21/6/2022 :: commented
                            dao.beginBatchTransaction();
                            dao.getSession().update(uploadingModel);
                            dao.commitBatchTransaction();
                        } catch (Exception e) {
                            dao.rollbackBatchTransaction();
                            try {
                                ftp.deleteFile(sftpUploadPath + "/" + newDrDocId); // try to delete if the file is
                                // uploaded but record not updated.
                            } catch (Exception delEx) {
                            }
                            throw e;
                        }
                    } else {
                        uploadingModel = new FileModel();
                        uploadingModel.setFile_id(newDrDocId);
                        uploadingModel.setFile_type(drFileCode_);
                        uploadingModel.setFile_name(uploadedFileName);
                        uploadingModel.setOriginal_file_name(uppyFileFileName);
                        uploadingModel.setFile_ext(uppyFileFileName.substring(uppyFileFileName.lastIndexOf(".") + 1));
                        uploadingModel.setCase_id(uploadRecordId_);
                        uploadingModel.setFile_path(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId);
                        uploadingModel.setUsj_no("-");
                        uploadingModel.setTotal_files(1);
                        uploadingModel.setTotal_gislayer(0);
                        uploadingModel.setTotal_features(0);
                        uploadingModel.setFile_status("P");
                        uploadingModel.setOriginal_file_name(uppyFileFileName);

                        String setupCheckListId = cf.getSingleValueWithSession(baseDAO.getSession(),
                                "t_setup_checklist", "checklist_id",
                                "process_type ='" + process_ + "' AND checklist_status = 'Y'");
                        String checkItemId = cf.getSingleValueWithSession(baseDAO.getSession(),
                                "t_setup_checklist_item", "ci_id",
                                "ci_datatype = '" + drFileCode_ + "' AND checklist_id = '" + setupCheckListId + "'");
                        uploadingModel.setCi_id(checkItemId);
                        uploadingModel.defaultAddProperties();

                        try {
                            dao.beginBatchTransaction();

                            // [PRECHECK] RELATED CODE
                            if (drFileCode_.equals("DSD")) {
                                JobDetailModel dsdJobModel = (JobDetailModel) dao.getModelByCode("job_id",
                                        uploadRecordId_, new JobDetailModel());

                                if (dsdJobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_PRECHECK)) {
                                    dsdJobModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_UTIL);
                                } else {
                                    if (dsdJobModel.getPrecheck_stage() == null) {
                                        dsdJobModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.NEW);
                                    } else if (!dsdJobModel.getPrecheck_stage().equals(UtimapsAction.PRECHECK_STAGE.UTIL_RERUN_PENDING)) {
                                        dsdJobModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.NEW);
                                    }
                                }

                                dao.getSession().update(dsdJobModel);
                            } else if (drFileCode_.equals("FBL") || drFileCode_.equals("RSO") || drFileCode_.equals("PS3")) {
                                JobDetailModel trvJobModel = (JobDetailModel) dao.getModelByCode("job_id",
                                        uploadRecordId_, new JobDetailModel());

                                if (trvJobModel.getUsj_status().equals(UtimapsAction.PB_STATUS.SUBMISSION_QUERY_USCS40)) {
                                    trvJobModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.QUERY_TRAV);
                                } else {
                                    trvJobModel.setPrecheck_stage(UtimapsAction.PRECHECK_STAGE.NEW);
                                }

                                dao.getSession().update(trvJobModel);
                            }

                            dao.getSession().save(uploadingModel);
                            dao.commitBatchTransaction();
                        } catch (Exception e) {
                            dao.rollbackBatchTransaction();
                            try {
                                ftp.deleteFile(sftpUploadPath + "/" + newDrDocId); // try to delete if the file is
                                // uploaded but record not updated.
                            } catch (Exception delEx) {
                            }
                            throw e;
                        }
                    }
                }

                jsonMap.put("status", "success");
                jsonMap.put("fileId", newDrDocId);
                jsonMap.put("fileName", uploadedFileName);

            } catch (BaseException be) {
                be.printStackTrace();
                jsonMap.put("status", "fail");
                jsonMap.put("errMsg", be.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                jsonMap.put("status", "fail");
                jsonMap.put("errMsg", getText("errors.unknownError_plsTryAgain"));
            } finally {
                dao.closeSession();
                ftp.disconnect();
            }

        } else {
            jsonMap.put("status", "fail");
            jsonMap.put("errMsg", "Upload Failed: Unacceptable file type");
        }

        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
        return null;
    }

    public String apiUploadStatic(InputStream fileInputStream, String fileFolder, String fileName) throws Exception {
        Debug.printDebug("API UPLOADING STATIC FILE");
        BaseDAO dao = baseDAO;
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        String strValue = Long.toString(System.currentTimeMillis()).substring(0, 10);

        try {
            uploadedFileName = drFileCode_.concat("_").concat(strValue).concat(uppyFileFileName.substring(uppyFileFileName.lastIndexOf(".")));
            uppyFile = new File(System.getProperty("java.io.tmpdir"), uploadedFileName);

            try (FileOutputStream fos = new FileOutputStream(uppyFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            String newDrDocId = CommonFunction.getId(20);

            try {
                String sftpUploadPath = null;
                String fileMimeType = "text/plain";
                sftpUploadPath = "Static_Files";
                ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
                if (ftp.isDirExists(SysConf.get("pathPrefix") + sftpUploadPath, Boolean.FALSE)) {
                    ftp.createDirIfNotExists(sftpUploadPath + "/" + fileFolder, Boolean.FALSE);
                    ftp.createFile(sftpUploadPath + "/" + fileFolder + "/" + newDrDocId, uppyFile);// use File as parameter
                    ftp.insertToFileDirectory(sftpUploadPath + "/" + fileFolder + "/" + newDrDocId, uppyFileFileName, fileMimeType, newDrDocId, baseDAO, drFileCode_, Boolean.FALSE);
                }

                uploadingModel = new FileModel();
                uploadingModel.setFile_id(newDrDocId);
                uploadingModel.setFile_type(drFileCode_);
                uploadingModel.setFile_name(uploadedFileName);
                uploadingModel.setOriginal_file_name(uppyFileFileName);
                uploadingModel.setFile_ext(uppyFileFileName.substring(uppyFileFileName.lastIndexOf(".") + 1));
                uploadingModel.setCase_id("-");
                uploadingModel.setFile_path(sftpUploadPath + "/" + fileFolder + "/" + newDrDocId);
                uploadingModel.setUsj_no("-");
                uploadingModel.setTotal_files(1);
                uploadingModel.setTotal_gislayer(0);
                uploadingModel.setTotal_features(0);
                uploadingModel.setFile_status("P");
                uploadingModel.setOriginal_file_name(uppyFileFileName);
                uploadingModel.defaultAddProperties();

                try {
                    dao.beginBatchTransaction();
                    dao.getSession().save(uploadingModel);
                    dao.commitBatchTransaction();
                } catch (Exception e) {
                    Debug.printDebug("EXCEPTION DAO");
                    e.printStackTrace();
                    dao.rollbackBatchTransaction();
                    try {
                        ftp.deleteFile(sftpUploadPath + "/" + fileFolder + "/" + newDrDocId); // try to delete if the file is
                        // uploaded but record not updated.
                    } catch (Exception delEx) {
                    }
                    throw e;
                }
//                }

            } catch (Exception e) {
                Debug.printDebug("EXCEPTION");
                e.printStackTrace();
                return "failed";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.closeSession();
            
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return "success";
    }

    public String apiUpload(String usjSeq, String usjYear, String divNo, InputStream fileInputStream, FormDataContentDisposition fileDetail) throws Exception {
        Debug.printDebug("API UPLOADING");
        BaseDAO dao = baseDAO;
        // TODO: Implement file upload logic using the provided InputStream and FormDataContentDisposition
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        String strValue = Long.toString(System.currentTimeMillis()).substring(0, 10);

        JobDetailModel detModel = (JobDetailModel) dao.getModelByCode("usj_seq,usj_year,usj_div", usjSeq + "," + usjYear + "," + divNo, new JobDetailModel());

        if (detModel == null) {
            return "fail_no_job";
        }

        uploadRecordId_ = detModel.getJob_id();

//        if (Validator.isEmpty(uploadedFileName)) { //joveni @ 21/6/2022 :: check if null
//        } else {
//            uploadedFileName = uploadedFileName.concat("_").concat(strValue).concat(uppyFileFileName.substring(uppyFileFileName.lastIndexOf(".")));
//        }
        uploadedFileName = drFileCode_.concat("_").concat(strValue).concat(uppyFileFileName.substring(uppyFileFileName.lastIndexOf(".")));

//        File tempFile = null;
        try {

            // check if there is existing files as the name 
            Map outputMap = new HashMap();
//                    psDspMap.put("file_status", "P");
            outputMap.put("file_type", drFileCode_);
            outputMap.put("case_id", uploadRecordId_);
            List<FileModel> outputModelList = dao.list_order(outputMap, FileModel.class, "");

            if (!outputModelList.isEmpty()) {
                for (FileModel file : outputModelList) {
//                    AttachmentUploadAction uploadAction = new AttachmentUploadAction();
//                    setUploadID(psdsp.getFile_id());
                    String deleteResult = removeTempFileRtnStrById(file.getFile_id());
                    Debug.printDebug("delete output - " + deleteResult);
                }
            }

            // Create a file with the uploadedFileName
            uppyFile = new File(System.getProperty("java.io.tmpdir"), uploadedFileName);

            // Write the InputStream content to the file
            try (FileOutputStream fos = new FileOutputStream(uppyFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            String newDrDocId = CommonFunction.getId(20);

            try {
                String sftpUploadPath = null;
                // String fileMimeType = ServletActionContext.getServletContext().getMimeType(uppyFileFileName);
                // Hardcode fileMimeType to be txt for now
                String fileMimeType = "text/plain";
                sftpUploadPath = uploadPath(drFileCode_);
                ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
                if (ftp.isDirExists(SysConf.get("pathPrefix") + sftpUploadPath, Boolean.FALSE)) {
                    ftp.createDirIfNotExists(sftpUploadPath + "/" + uploadRecordId_, Boolean.FALSE);
                    ftp.createFile(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId, uppyFile);// use File as parameter
                    ftp.insertToFileDirectory(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId, uppyFileFileName, fileMimeType, newDrDocId, baseDAO, drFileCode_, Boolean.FALSE);
                }

//                if (uploadingModel != null) {
//                    try {
//                        updateUpdatingModel(newDrDocId); // joveni @ 21/6/2022 :: commented
//                        dao.beginBatchTransaction();
//                        dao.getSession().update(uploadingModel);
//                        dao.commitBatchTransaction();
//                    } catch (Exception e) {
//                        dao.rollbackBatchTransaction();
//                        try {
//                            ftp.deleteFile(sftpUploadPath + "/" + newDrDocId); // try to delete if the file is
//                            // uploaded but record not updated.
//                        } catch (Exception delEx) {
//                        }
//                        throw e;
//                    }
//                } else {
                uploadingModel = new FileModel();
                uploadingModel.setFile_id(newDrDocId);
                uploadingModel.setFile_type(drFileCode_);
                uploadingModel.setFile_name(uploadedFileName);
                uploadingModel.setOriginal_file_name(uppyFileFileName);
                uploadingModel.setFile_ext(uppyFileFileName.substring(uppyFileFileName.lastIndexOf(".") + 1));
                uploadingModel.setCase_id(uploadRecordId_);
                uploadingModel.setFile_path(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId);
                uploadingModel.setUsj_no("-");
                uploadingModel.setTotal_files(1);
                uploadingModel.setTotal_gislayer(0);
                uploadingModel.setTotal_features(0);
                uploadingModel.setFile_status("P");
                uploadingModel.setOriginal_file_name(uppyFileFileName);
                uploadingModel.defaultAddProperties();

                try {
                    dao.beginBatchTransaction();
                    dao.getSession().save(uploadingModel);
                    dao.commitBatchTransaction();
                } catch (Exception e) {
                    Debug.printDebug("EXCEPTION DAO");
                    e.printStackTrace();
                    dao.rollbackBatchTransaction();
                    try {
                        ftp.deleteFile(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId); // try to delete if the file is
                        // uploaded but record not updated.
                    } catch (Exception delEx) {
                    }
                    throw e;
                }
//                }

            } catch (Exception e) {
                Debug.printDebug("EXCEPTION");
                e.printStackTrace();
                return "failed";
            }

            // Now tempFile is a File object containing the content from fileInputStream
            // You can use tempFile for further processing
            // TODO: Add your file processing logic here
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception appropriately
        } finally {
            // Close the input stream
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Note: We're not deleting the file here as it might be needed for further processing
            // Make sure to handle file deletion appropriately in your main logic
        }

        return "success";
    }

    public String zipFileCheck(String fileCode, File zipFile, String uploadRecordId_, BaseDAO dao,
            Map<String, Object> jsonMap) throws Exception {
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        boolean allFilesValid = true;
        boolean containsFolder = false;
        boolean containsNonAcceptedFile = false;
        boolean containsMissingFile = false;

        // make an arraylist of the required files for the fileCode
        List<String> requiredFiles = new ArrayList<String>();
        if (fileCode.equals("DSD")) {
            requiredFiles.add("dbf");
            requiredFiles.add("shp");
            requiredFiles.add("shx");
            requiredFiles.add("prj");
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                if (zipEntry.isDirectory() || zipEntry.getName().contains("/")) {
                    containsFolder = true;
                    allFilesValid = false;
                    break;
                } else if (!isAcceptableFile(zipEntry.getName(), fileCode)) {
                    containsNonAcceptedFile = true;
                    allFilesValid = false;
                    break;
                } else {
                    if (fileCode.equals("DSD")) {
                        String ext = zipEntry.getName().substring(zipEntry.getName().lastIndexOf(".") + 1).toLowerCase();
                        Debug.printDebug("removing -- " + ext);
                        requiredFiles.remove(ext);
                    }
                }
                zipEntry = zis.getNextEntry();
            }
        }

        Debug.printDebug("remaining files - " + requiredFiles.toString());

        if (fileCode.equals("DSD")) {
            if (requiredFiles.size() > 0) {
                containsMissingFile = true;
                allFilesValid = false;
            }
        }

        if (!allFilesValid) {
            if (containsFolder) {
                return "fail_folder";
            } else if (containsNonAcceptedFile) {
                return "fail_non_accepted_file";
            } else if (containsMissingFile) {
                return "fail_missing_file_" + requiredFiles.toString();
            }
        }

        return "pass";
    }

    public String unzipAndUploadContents(String fileCode, File zipFile, String uploadRecordId_, BaseDAO dao,
            Map<String, Object> jsonMap) throws Exception {
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        boolean allFilesValid = true;
        boolean containsFolder = false;
        boolean containsNonAcceptedFile = false;

        String zipCheckRes = zipFileCheck(fileCode, zipFile, uploadRecordId_, dao, jsonMap);

        if (!zipCheckRes.equals("pass")) {
            return zipCheckRes;
        }

        // If all files are valid, proceed with uploading
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                if (!zipEntry.isDirectory() && !zipEntry.getName().contains("/")
                        && isAcceptableFile(zipEntry.getName(), fileCode)) {
                    File tempFile = File.createTempFile("upload", null);
                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }

                    String strValue = Long.toString(System.currentTimeMillis()).substring(0, 10);
                    String uploadedFileName = zipEntry.getName();
                    if (Validator.isEmpty(uploadedFileName)) {
                        uploadedFileName = drFileCode_.concat("_").concat(strValue)
                                .concat(zipEntry.getName().substring(zipEntry.getName().lastIndexOf(".")));
                    }

                    String newDrDocId = CommonFunction.getId(20);
                    String sftpUploadPath = uploadPath(drFileCode_);
                    ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
                    ftp.createDirIfNotExists(sftpUploadPath + "/" + uploadRecordId_, Boolean.FALSE);
                    ftp.createFile(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId, tempFile);
                    ftp.insertToFileDirectory(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId,
                            zipEntry.getName(),
                            ServletActionContext.getServletContext().getMimeType(zipEntry.getName()), newDrDocId, dao,
                            drFileCode_, Boolean.FALSE);

                    FileModel uploadingModel = new FileModel();
                    uploadingModel.setFile_id(newDrDocId);
                    uploadingModel.setFile_type(drFileCode_);
                    uploadingModel.setFile_name(uploadedFileName);
                    uploadingModel.setOriginal_file_name(zipEntry.getName());
                    uploadingModel.setFile_ext(zipEntry.getName().substring(zipEntry.getName().lastIndexOf(".") + 1));
                    uploadingModel.setCase_id(uploadRecordId_);
                    uploadingModel.setFile_path(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId);
                    uploadingModel.setUsj_no("-");
                    uploadingModel.setTotal_files(1);
                    uploadingModel.setTotal_gislayer(0);
                    uploadingModel.setTotal_features(0);
                    uploadingModel.setFile_status("P");

                    String setupCheckListId = cf.getSingleValueWithSession(dao.getSession(), "t_setup_checklist",
                            "checklist_id", "process_type ='" + process_ + "' AND checklist_status = 'Y'");
                    String checkItemId = cf.getSingleValueWithSession(dao.getSession(), "t_setup_checklist_item",
                            "ci_id",
                            "ci_datatype = '" + drFileCode_ + "' AND checklist_id = '" + setupCheckListId + "'");
                    uploadingModel.setCi_id(checkItemId);
                    uploadingModel.defaultAddProperties();

                    try {
                        dao.beginBatchTransaction();
                        dao.getSession().save(uploadingModel);
                        dao.commitBatchTransaction();
                    } catch (Exception e) {
                        dao.rollbackBatchTransaction();
                        try {
                            ftp.deleteFile(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId);
                        } catch (Exception delEx) {
                            // Handle deletion exception if necessary
                        }
                        throw e;
                    }
                }
                zipEntry = zis.getNextEntry();
            }
        } catch (Exception e) {
            jsonMap.put("status", "fail");
            jsonMap.put("errMsg", e.getMessage());
            return "fail";
        } finally {
            ftp.disconnect();
        }

        return "pass";
    }

    private boolean isAcceptableFile(String fileName, String fileCode) {
        List<String> acceptableExtensions = Arrays.asList("pdf");

        if (fileCode.equals("DSP")) {
            acceptableExtensions = Arrays.asList("pdf");
        } else if (fileCode.equals("DSD")) {
            acceptableExtensions = Arrays.asList("shp", "xml", "cpg", "dbf", "prj", "sbn", "sbx", "shx", "shp.xml");
        } else {
            acceptableExtensions = Arrays.asList("jpeg", "jpg", "png", "pdf", "dxf", "txt", "csv", "tiff", "cps", "doc",
                    "docx");
        }

        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        Debug.printDebug("checking " + fileExtension + "-- result --> " + acceptableExtensions.contains(fileExtension));
        return acceptableExtensions.contains(fileExtension);
    }

    final static MimetypesFileTypeMap FILE_TYPE_MAP = new MimetypesFileTypeMap();

    public String viewTempFile() throws Exception {
        // Debug.printDebug("uploadID = " + uploadID);
        DrDocRepoModel docRepo = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id").setParameter("dr_doc_id", uploadID).uniqueResult();
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        if (!Validator.isEmpty(tn_) && tn_.equals("Y")) {
            try {
                inputStream = ftp.getFile(docRepo.getDr_doc_path() + ftp.ThumbnailSurfix);
            } catch (Exception e) {
                inputStream = ftp.getFile(docRepo.getDr_doc_path());
                // Debug.printDebug("size = " + inputStream.available());
                inputStream = ftp.getFile(docRepo.getDr_doc_path() + ftp.ThumbnailSurfix);
            }
        } else {
            inputStream = ftp.getFile(docRepo.getDr_doc_path());
        }
        contentDisposition = "attachment;filename=\"" + StringEscapeUtils.escapeHtml4(docRepo.getDr_doc_name()) + "\"";
        contentType = docRepo.getMime_type();
        if (Validator.isEmpty(docRepo.getMime_type())) {
            contentType = FILE_TYPE_MAP.getContentType(docRepo.getDr_doc_name());
            if (contentType.startsWith("application/")) {
                contentType = "application/" + docRepo.getDr_doc_name().substring(docRepo.getDr_doc_name().lastIndexOf(".") + 1);
            }
        }
//        Debug.printDebug("contentDisp = " + contentDisposition);
//        Debug.printDebug("contentType = " + contentType);
        return "fileDownload";
    }
    // ========================================================================================================================================

    public String viewTempFileById() throws Exception {

        Debug.printDebug(reqID + " - " + reqType + " - " + reqFile);
        BaseDAO dao = baseDAO;

        if (reqType.equals("job")) {
            JobDetailModel detModel = (JobDetailModel) dao.getModelByCode("case_id", reqID, new JobDetailModel());
            // JobDetailModel reqJobModel = (JobDetailModel)
            // baseDAO.getModelByCode("case_id", reqID, new JobDetailModel());
            reqID = detModel.getJob_id();
        }

        FileModel reqFileModel = new FileModel();
        if (reqFile.equals("USCS90")) {
            // System.out.println("signed uscs90?");
            reqFileModel = (FileModel) baseDAO.getModelByCode("case_id,file_type,description", reqID + "," + reqFile + "," + reqFile + "_SIGNED", new FileModel());
        } else {
            reqFileModel = (FileModel) baseDAO.getModelByCode("case_id,file_type", reqID + "," + reqFile, new FileModel());
        }

        uploadID = reqFileModel.getFile_id();

        // Debug.printDebug("uploadID = " + uploadID);
        DrDocRepoModel docRepo = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id").setParameter("dr_doc_id", uploadID).uniqueResult();
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        if (!Validator.isEmpty(tn_) && tn_.equals("Y")) {
            try {
                inputStream = ftp.getFile(docRepo.getDr_doc_path() + ftp.ThumbnailSurfix);
            } catch (Exception e) {
                inputStream = ftp.getFile(docRepo.getDr_doc_path());
                // Debug.printDebug("size = " + inputStream.available());
                inputStream = ftp.getFile(docRepo.getDr_doc_path() + ftp.ThumbnailSurfix);
            }
        } else {
            inputStream = ftp.getFile(docRepo.getDr_doc_path());
        }
        contentDisposition = "attachment;filename=\"" + StringEscapeUtils.escapeHtml4(docRepo.getDr_doc_name()) + "\"";
        contentType = docRepo.getMime_type();
        if (Validator.isEmpty(docRepo.getMime_type())) {
            contentType = FILE_TYPE_MAP.getContentType(docRepo.getDr_doc_name());
            if (contentType.startsWith("application/")) {
                contentType = "application/" + docRepo.getDr_doc_name().substring(docRepo.getDr_doc_name().lastIndexOf(".") + 1);
            }
        }
        // Debug.printDebug("contentDisp = " + contentDisposition);
        // Debug.printDebug("contentType = " + contentType);
        return "fileDownload";
    }

    // ========================================================================================================================================
    public File downloadTempFile() throws Exception {
        Debug.printDebug("uploadID = " + uploadID);
        FtpInterface ftp = FileOperationUtil.getFtpInterface();

        // Retrieve the document metadata
        DrDocRepoModel docRepo = (DrDocRepoModel) baseDAO.getSession()
                .getNamedQuery("DocRepo.findBy_dr_doc_id")
                .setParameter("dr_doc_id", uploadID)
                .uniqueResult();

        InputStream inputStream = null;
        File tempFile = null;

        if (!Validator.isEmpty(tn_) && tn_.equals("Y")) {
            try {
                inputStream = ftp.getFile(docRepo.getDr_doc_path() + ftp.ThumbnailSurfix);
            } catch (Exception e) {
                inputStream = ftp.getFile(docRepo.getDr_doc_path());
                Debug.printDebug("size = " + inputStream.available());
                inputStream = ftp.getFile(docRepo.getDr_doc_path() + ftp.ThumbnailSurfix);
            }
        } else {
            inputStream = ftp.getFile(docRepo.getDr_doc_path());
        }

        // Create a temporary file
        String fileExtension = getFileExtension(docRepo.getDr_doc_name());
        tempFile = File.createTempFile("tempFile", getFileExtension(docRepo.getDr_doc_name()));

        // Write the InputStream to the temporary file
        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return tempFile;
    }

    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        return index == -1 ? "" : fileName.substring(index);
    }

    public String viewTempFile2() throws Exception {
//        Debug.printDebug("uploadID = " + uploadID);
        DrDocRepoModel docRepo = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id").setParameter("dr_doc_id", uploadID).uniqueResult();
        FtpInterface ftp = FileOperationUtil.getFtpInterface();

        if (docRepo != null) {
            if (!Validator.isEmpty(tn_) && tn_.equals("Y")) {
                try {
                    inputStream = ftp.getFile(docRepo.getDr_doc_path() + ftp.ThumbnailSurfix);
                } catch (Exception e) {
                    inputStream = ftp.getFile(docRepo.getDr_doc_path());
//                    Debug.printDebug("size = " + inputStream.available());
                    inputStream = ftp.getFile(docRepo.getDr_doc_path() + ftp.ThumbnailSurfix);
                }
            } else {
                //            Debug.printDebug("doc path:: " + docRepo.getDr_doc_path());
                if (drFileCode_.equals("OfferLetterSigned") || drFileCode_.equals("LicenseSigned")) {
                    inputStream = ftp.getFile(docRepo.getDr_doc_path() + "Signed." + docRepo.getDr_doc_type());
                } else {
                    inputStream = ftp.getFile(docRepo.getDr_doc_path());
                }
            }
            contentDisposition = "filename=\"" + StringEscapeUtils.escapeHtml4(docRepo.getDr_doc_name()) + "\"";
            contentType = docRepo.getMime_type();
            if (Validator.isEmpty(docRepo.getMime_type())) {
                contentType = FILE_TYPE_MAP.getContentType(docRepo.getDr_doc_name());
                if (contentType.startsWith("application/")) {
                    contentType = "application/" + docRepo.getDr_doc_name().substring(docRepo.getDr_doc_name().lastIndexOf(".") + 1);
                }
            }
        } else {
//            Debug.printDebug("file not found");
            contentType = "application/pdf";
        }
//        Debug.printDebug("contentType = " + contentType);
        return "fileDownload";
    }

    public void removeTempFile() throws Exception {
//        Debug.printDebug("uploadID = " + uploadID);
        DrDocRepoModel docRepo = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id").setParameter("dr_doc_id", uploadID).uniqueResult();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Map jsonMap = new HashMap();
        response.setContentType("application/json");
        try {
            BaseDAO dao = baseDAO;
            uploadingModel = (FileModel) baseDAO.getSession().getNamedQuery("FileModel.findBy_fileId").setParameter("file_id", uploadID).uniqueResult();
            uploadingModel_fk = "dr_doc_id";
            dao.beginBatchTransaction();

            if (SystemConstants.FILE_STATUS.NO.equals(uploadingModel.getFile_status())
                    || SystemConstants.FILE_STATUS.YES.equals(uploadingModel.getFile_status())) {
                Debug.printDebug("soft delete file");

                if (uploadingModel.getFile_type().equals("DSP")) {
                    Map dspMap = new HashMap();

                    // remove all signed digital plans (revoke)
                    dspMap.put("file_type", "PSDSP");
                    dspMap.put("case_id", uploadingModel.getCase_id());
                    List<FileModel> psDspModelList = dao.list_order(dspMap, FileModel.class, "");

                    if (!psDspModelList.isEmpty()) {
                        for (FileModel psdsp : psDspModelList) {
                            DrDocRepoModel psdDSpRepo = (DrDocRepoModel) baseDAO.getSession()
                                    .getNamedQuery("DocRepo.findBy_dr_doc_id")
                                    .setParameter("dr_doc_id", psdsp.getFile_id()).uniqueResult();

                            if (FileOperationUtil.getFtpInterface().deleteFile(psdDSpRepo.getDr_doc_path())) {
                                dao.getSession().delete(psdsp);
                            }
                        }
                    }

                    // delete other pdf dsps if main uploaded file is a zip
                    // 4/9 - need to include deleting any other DSP with file_status = "P" to avoid
                    // duplicates - added after deleted param id file
                    // follow principle of one Pending file at a time
                    if (uploadingModel.getFile_ext().equals("zip")) {
                        // Debug.printDebug("delete other pdf dsps if main uploaded file is a zip");
                        Map pdfDspmap = new HashMap();
                        pdfDspmap.put("file_type", "DSP");
                        pdfDspmap.put("case_id", uploadingModel.getCase_id());
                        pdfDspmap.put("file_ext", "pdf");
                        pdfDspmap.put("file_status", "P");
                        List<FileModel> pdfDspModelList = dao.list_order(pdfDspmap, FileModel.class, "");

                        if (!pdfDspModelList.isEmpty()) {
                            for (FileModel pdfDsp : pdfDspModelList) {
                                DrDocRepoModel psdDSpRepo = (DrDocRepoModel) baseDAO.getSession()
                                        .getNamedQuery("DocRepo.findBy_dr_doc_id")
                                        .setParameter("dr_doc_id", pdfDsp.getFile_id()).uniqueResult();

                                if (FileOperationUtil.getFtpInterface().deleteFile(psdDSpRepo.getDr_doc_path())) {
                                    dao.getSession().delete(pdfDsp);
                                }
                            }
                        }
                    }

                }

                uploadingModel.setFile_status(SystemConstants.FILE_STATUS.SOFT_DELETED);
                dao.getSession().update(uploadingModel);
                dao.commitBatchTransaction();
                jsonMap.put("status", "success");
            } else {
                Debug.printDebug("hard delete file");
                if (FileOperationUtil.getFtpInterface().deleteFile(docRepo.getDr_doc_path())) {
                    if (uploadingModel.getFile_type().equals("DSP")) {
                        Map dspMap = new HashMap();
                        // remove all signed digital plans (revoke)
                        dspMap.put("file_type", "PSDSP");
                        dspMap.put("case_id", uploadingModel.getCase_id());
                        List<FileModel> psDspModelList = dao.list_order(dspMap, FileModel.class, "");

                        if (!psDspModelList.isEmpty()) {
                            for (FileModel psdsp : psDspModelList) {
                                DrDocRepoModel psdDSpRepo = (DrDocRepoModel) baseDAO.getSession()
                                        .getNamedQuery("DocRepo.findBy_dr_doc_id")
                                        .setParameter("dr_doc_id", psdsp.getFile_id()).uniqueResult();

                                if (FileOperationUtil.getFtpInterface().deleteFile(psdDSpRepo.getDr_doc_path())) {
                                    dao.getSession().delete(psdsp);
                                }
                            }
                        }

                        // delete other pdf dsps if main uploaded file is a zip
                        // 4/9 - need to include deleting any other DSP with file_status = "P" to avoid
                        // duplicates - added after deleted param id file
                        // follow principle of one Pending file at a time
                        if (uploadingModel.getFile_ext().equals("zip")) {
                            // Debug.printDebug("delete other pdf dsps if main uploaded file is a zip");
                            Map pdfDspmap = new HashMap();
                            pdfDspmap.put("file_type", "DSP");
                            pdfDspmap.put("case_id", uploadingModel.getCase_id());
                            pdfDspmap.put("file_ext", "pdf");
                            pdfDspmap.put("file_status", "P");
                            List<FileModel> pdfDspModelList = dao.list_order(pdfDspmap, FileModel.class, "");

                            if (!pdfDspModelList.isEmpty()) {
                                for (FileModel pdfDsp : pdfDspModelList) {
                                    DrDocRepoModel psdDSpRepo = (DrDocRepoModel) baseDAO.getSession()
                                            .getNamedQuery("DocRepo.findBy_dr_doc_id")
                                            .setParameter("dr_doc_id", pdfDsp.getFile_id()).uniqueResult();

                                    if (FileOperationUtil.getFtpInterface().deleteFile(psdDSpRepo.getDr_doc_path())) {
                                        dao.getSession().delete(pdfDsp);
                                    }
                                }
                            }
                        }

                    }

                    // for resubmisssion purposes - if file status is N then soft delete - else just
                    // delete entirely
                    if (SystemConstants.FILE_STATUS.PENDING.equals(uploadingModel.getFile_status())
                            || uploadingModel.getFile_status() == null) {
                        try {
                            // checking any other duplicate "P" status DSP files
                            if (uploadingModel.getFile_type().equals("DSP")) {
                                Map dupeDspmap = new HashMap();
                                dupeDspmap.put("file_type", "DSP");
                                dupeDspmap.put("case_id", uploadingModel.getCase_id());
                                dupeDspmap.put("file_status", "P");
                                List<FileModel> dupeDspModelList = dao.list_order(dupeDspmap, FileModel.class, "");
                                if (!dupeDspModelList.isEmpty()) {
                                    for (FileModel dupeDsp : dupeDspModelList) {
                                        DrDocRepoModel dupeDSpRepo = (DrDocRepoModel) baseDAO.getSession()
                                                .getNamedQuery("DocRepo.findBy_dr_doc_id")
                                                .setParameter("dr_doc_id", dupeDsp.getFile_id()).uniqueResult();

                                        if (FileOperationUtil.getFtpInterface().deleteFile(dupeDSpRepo.getDr_doc_path())) {
                                            dao.getSession().delete(dupeDsp);
                                        }
                                    }
                                }
                            }

                            dao.getSession().delete(uploadingModel);
                            dao.getSession().delete(docRepo);
                            dao.commitBatchTransaction();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            dao.rollbackBatchTransaction();
                            throw ex;
                        }
                    }
//                    } else if (SystemConstants.FILE_STATUS.NO.equals(uploadingModel.getFile_status())
//                            || SystemConstants.FILE_STATUS.YES.equals(uploadingModel.getFile_status())) {
//                        // Debug.printDebug("soft deleting prev file");
//                        uploadingModel.setFile_status(SystemConstants.FILE_STATUS.SOFT_DELETED);
//                        dao.getSession().update(uploadingModel);
//                        dao.commitBatchTransaction();
//                    }

                    jsonMap.put("status", "success");
                } else {
                    jsonMap.put("status", "fail");
                }
            }
        } catch (Exception e) {
            jsonMap.put("status", "fail");
            e.printStackTrace();
        }
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
    }

    public String removeTempFileRtnStr() throws Exception {
        String returnStr = "failed";
        Debug.printDebug("uploadID = " + uploadID);
        DrDocRepoModel docRepo = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id")
                .setParameter("dr_doc_id", uploadID).uniqueResult();
        Map jsonMap = new HashMap();

        try {
            if (FileOperationUtil.getFtpInterface().deleteFile(docRepo.getDr_doc_path())) {
                BaseDAO dao = baseDAO;
                dao.beginBatchTransaction();
                uploadingModel = (FileModel) baseDAO.getSession().getNamedQuery("FileModel.findBy_fileId")
                        .setParameter("file_id", uploadID).uniqueResult();
                uploadingModel_fk = "dr_doc_id";

                if (uploadingModel.getFile_type().equals("DSP")) {
                    Map dspMap = new HashMap();

                    // remove all signed digital plans (revoke)
                    dspMap.put("file_type", "PSDSP");
                    dspMap.put("case_id", uploadingModel.getCase_id());
                    List<FileModel> psDspModelList = dao.list_order(dspMap, FileModel.class, "");

                    if (!psDspModelList.isEmpty()) {
                        for (FileModel psdsp : psDspModelList) {
                            DrDocRepoModel psdDSpRepo = (DrDocRepoModel) baseDAO.getSession()
                                    .getNamedQuery("DocRepo.findBy_dr_doc_id")
                                    .setParameter("dr_doc_id", psdsp.getFile_id()).uniqueResult();

                            if (FileOperationUtil.getFtpInterface().deleteFile(psdDSpRepo.getDr_doc_path())) {
                                dao.getSession().delete(psdsp);
                            }
                        }
                    }

                    // delete other pdf dsps if main uploaded file is a zip
                    if (uploadingModel.getFile_ext().equals("zip")) {
                        Debug.printDebug("delete other pdf dsps if main uploaded file is a zip");
                        Map pdfDspmap = new HashMap();
                        pdfDspmap.put("file_type", "DSP");
                        pdfDspmap.put("case_id", uploadingModel.getCase_id());
                        pdfDspmap.put("file_ext", "pdf");
                        pdfDspmap.put("file_status", "P");
                        List<FileModel> pdfDspModelList = dao.list_order(pdfDspmap, FileModel.class, "");

                        if (!pdfDspModelList.isEmpty()) {
                            for (FileModel pdfDsp : pdfDspModelList) {
                                DrDocRepoModel psdDSpRepo = (DrDocRepoModel) baseDAO.getSession()
                                        .getNamedQuery("DocRepo.findBy_dr_doc_id")
                                        .setParameter("dr_doc_id", pdfDsp.getFile_id()).uniqueResult();

                                if (FileOperationUtil.getFtpInterface().deleteFile(psdDSpRepo.getDr_doc_path())) {
                                    dao.getSession().delete(pdfDsp);
                                }
                            }
                        }
                    }

                }

                // for resubmisssion purposes - if file status is N then soft delete - else just
                // delete entirely
                if (SystemConstants.FILE_STATUS.PENDING.equals(uploadingModel.getFile_status())
                        || uploadingModel.getFile_status() == null) {
                    try {
                        dao.getSession().delete(uploadingModel);
                        dao.getSession().delete(docRepo);
                        dao.commitBatchTransaction();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        dao.rollbackBatchTransaction();
                        return returnStr;
                    }
                } else if (SystemConstants.FILE_STATUS.NO.equals(uploadingModel.getFile_status())) {
                    Debug.printDebug("soft deleting prev file");
                    uploadingModel.setFile_status(SystemConstants.FILE_STATUS.SOFT_DELETED);
                    dao.getSession().update(uploadingModel);
                    dao.commitBatchTransaction();
                } else {
                    if (uploadingModel.getFile_type().equals("PSDSP")) {
                        try {
                            dao.getSession().delete(uploadingModel);
                            dao.getSession().delete(docRepo);
                            dao.commitBatchTransaction();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            dao.rollbackBatchTransaction();
                            return returnStr;
                        }
                    }
                }

                returnStr = "success";
            } else {
                jsonMap.put("status", "fail");
            }
        } catch (Exception e) {
            jsonMap.put("status", "fail");
            e.printStackTrace();
        }
        return returnStr;
    }

    public String removeTempFileRtnStrById(String uploadID) throws Exception {
        String returnStr = "failed";
        Debug.printDebug("uploadID = " + uploadID);
        DrDocRepoModel docRepo = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id")
                .setParameter("dr_doc_id", uploadID).uniqueResult();
        Map jsonMap = new HashMap();

        try {
            if (FileOperationUtil.getFtpInterface().deleteFile(docRepo.getDr_doc_path())) {
                BaseDAO dao = baseDAO;
                dao.beginBatchTransaction();
                uploadingModel = (FileModel) baseDAO.getSession().getNamedQuery("FileModel.findBy_fileId")
                        .setParameter("file_id", uploadID).uniqueResult();
                uploadingModel_fk = "dr_doc_id";

                if (uploadingModel.getFile_type().equals("DSP")) {
                    Map dspMap = new HashMap();

                    // remove all signed digital plans (revoke)
                    dspMap.put("file_type", "PSDSP");
                    dspMap.put("case_id", uploadingModel.getCase_id());
                    List<FileModel> psDspModelList = dao.list_order(dspMap, FileModel.class, "");

                    if (!psDspModelList.isEmpty()) {
                        for (FileModel psdsp : psDspModelList) {
                            DrDocRepoModel psdDSpRepo = (DrDocRepoModel) baseDAO.getSession()
                                    .getNamedQuery("DocRepo.findBy_dr_doc_id")
                                    .setParameter("dr_doc_id", psdsp.getFile_id()).uniqueResult();

                            if (FileOperationUtil.getFtpInterface().deleteFile(psdDSpRepo.getDr_doc_path())) {
                                dao.getSession().delete(psdsp);
                            }
                        }
                    }

                    // delete other pdf dsps if main uploaded file is a zip
                    if (uploadingModel.getFile_ext().equals("zip")) {
                        Debug.printDebug("delete other pdf dsps if main uploaded file is a zip");
                        Map pdfDspmap = new HashMap();
                        pdfDspmap.put("file_type", "DSP");
                        pdfDspmap.put("case_id", uploadingModel.getCase_id());
                        pdfDspmap.put("file_ext", "pdf");
                        pdfDspmap.put("file_status", "P");
                        List<FileModel> pdfDspModelList = dao.list_order(pdfDspmap, FileModel.class, "");

                        if (!pdfDspModelList.isEmpty()) {
                            for (FileModel pdfDsp : pdfDspModelList) {
                                DrDocRepoModel psdDSpRepo = (DrDocRepoModel) baseDAO.getSession()
                                        .getNamedQuery("DocRepo.findBy_dr_doc_id")
                                        .setParameter("dr_doc_id", pdfDsp.getFile_id()).uniqueResult();

                                if (FileOperationUtil.getFtpInterface().deleteFile(psdDSpRepo.getDr_doc_path())) {
                                    dao.getSession().delete(pdfDsp);
                                }
                            }
                        }
                    }

                }

                // for resubmisssion purposes - if file status is N then soft delete - else just
                // delete entirely
                if (SystemConstants.FILE_STATUS.PENDING.equals(uploadingModel.getFile_status())
                        || uploadingModel.getFile_status() == null) {
                    try {
                        dao.getSession().delete(uploadingModel);
                        dao.getSession().delete(docRepo);
                        dao.commitBatchTransaction();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        dao.rollbackBatchTransaction();
                        return returnStr;
                    }
                } else if (SystemConstants.FILE_STATUS.NO.equals(uploadingModel.getFile_status())) {
                    Debug.printDebug("soft deleting prev file");
                    uploadingModel.setFile_status(SystemConstants.FILE_STATUS.SOFT_DELETED);
                    dao.getSession().update(uploadingModel);
                    dao.commitBatchTransaction();
                } else {
                    if (uploadingModel.getFile_type().equals("PSDSP")) {
                        try {
                            dao.getSession().delete(uploadingModel);
                            dao.getSession().delete(docRepo);
                            dao.commitBatchTransaction();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            dao.rollbackBatchTransaction();
                            return returnStr;
                        }
                    }
                }

                returnStr = "success";
            } else {
                jsonMap.put("status", "fail");
            }
        } catch (Exception e) {
            jsonMap.put("status", "fail");
            e.printStackTrace();
        }
        return returnStr;
    }

    // ========================================================================================================================================
    public void copyFiles(String process, String file_code, FileModel oriModel) throws Exception {
        BaseDAO dao = baseDAO;
        System.out.println("copy files - " + file_code);
        // copy SJI
        String newDrDocId = CommonFunction.getId(20);
        FileModel copyModel = new FileModel();
        copyModel.setFile_id(newDrDocId);
        copyModel.setFile_type(file_code);
        copyModel.setTotal_files(1);
        copyModel.setTotal_gislayer(0);
        copyModel.setTotal_features(0);
        copyModel.setUsj_no("-");
        copyModel.setFile_ext(oriModel.getFile_ext());
        copyModel.setFile_name(oriModel.getFile_name());
        copyModel.setCase_id(oriModel.getCase_id());
        copyModel.setOriginal_file_name(oriModel.getOriginal_file_name());
        copyModel.setFile_status("P");
        copyModel.setFile_path(oriModel.getFile_path());
        String setupCheckListId = cf.getSingleValueWithSession(dao.getSession(), "t_setup_checklist", "checklist_id", "process_type ='" + process + "' AND checklist_status = 'Y'");
        String checkItemId = cf.getSingleValueWithSession(dao.getSession(), "t_setup_checklist_item", "ci_id", "ci_datatype = '" + file_code + "' AND checklist_id = '" + setupCheckListId + "'");
        System.out.println("checklist id - " + setupCheckListId);
        System.out.println("checkitem id - " + checkItemId);
        copyModel.setCi_id(checkItemId);
        copyModel.defaultAddProperties();

        try {
            dao.beginBatchTransaction();
            dao.getSession().save(copyModel);

            //copy dr doc repo now
            DrDocRepoModel oriDrDocModel = (DrDocRepoModel) dao.getModelByCode("dr_doc_id", oriModel.getFile_id(), new DrDocRepoModel());

            if (oriDrDocModel != null) {
                DrDocRepoModel newDocModel = new DrDocRepoModel();
                newDocModel.defaultAddProperties();
                newDocModel.setDr_doc_id(newDrDocId);
                newDocModel.setDr_doc_name(oriDrDocModel.getDr_doc_name());
                newDocModel.setMime_type(oriDrDocModel.getMime_type());
                newDocModel.setDr_doc_path(oriDrDocModel.getDr_doc_path());
                newDocModel.setDr_doc_application(oriDrDocModel.getDr_doc_application());
                dao.getSession().save(newDocModel);
            }

            dao.commitBatchTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollbackBatchTransaction();
            throw e;
        }
    }

    // ========================================================================================================================================
    private void checkFile_checkAccess() throws Exception {
        if (drFileCode_.equals("userRecord_file")) {
            if (uppyFileFileName.endsWith(".pdf")) {
                throw new CustomBaseException("PDF not allowed");
            }
            BaseDAO dao = baseDAO;
            Map session = ActionContext.getContext().getSession();
            User user = (User) dao.getModelById((String) session.get("userId"), User.class);
            if (user != null && !user.getUs_user_id().equals("admin")) {
                throw new CustomBaseException("Only admin can update user profile");
            }
        }
    }

    private void updateUpdatingModel(String value) throws Exception {
        Debug.printDebug("set = " + uploadingModel_fk.substring(0, 1).toUpperCase() + uploadingModel_fk.substring(1));
        Method m = uploadingModel.getClass().getMethod("set" + uploadingModel_fk.substring(0, 1).toUpperCase() + uploadingModel_fk.substring(1), String.class);
        m.invoke(uploadingModel, value);
    }

    public File getUppyFile() {
        return uppyFile;
    }

    public void setUppyFile(File uppyFile) {
        this.uppyFile = uppyFile;
    }

    public String getUppyFileFileName() {
        return uppyFileFileName;
    }

    public void setUppyFileFileName(String uppyFileFileName) {
        this.uppyFileFileName = uppyFileFileName;
    }

    public String getDrFileCode_() {
        return drFileCode_;
    }

    public void setDrFileCode_(String drFileCode_) {
        this.drFileCode_ = drFileCode_;
    }

    public String getUploadID() {
        return uploadID;
    }

    public void setUploadID(String uploadID) {
        this.uploadID = uploadID;
    }

    public String getUploadRecordId_() {
        return uploadRecordId_;
    }

    public void setUploadRecordId_(String uploadRecordId_) {
        this.uploadRecordId_ = uploadRecordId_;
    }

    public String getUploadedFileName() {
        return uploadedFileName;
    }

    public void setUploadedFileName(String uploadedFileName) {
        this.uploadedFileName = uploadedFileName;
    }

    public String getCaseId_() {
        return caseId_;
    }

    public void setCaseId_(String caseId_) {
        this.caseId_ = caseId_;
    }

    public String getTn_() {
        return tn_;
    }

    public void setTn_(String tn_) {
        this.tn_ = tn_;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String uploadInternal() throws Exception {
        Debug.printDebug("=============================================================");
        Debug.printDebug("upload internal file ***** " + new java.sql.Timestamp(System.currentTimeMillis()));
        Debug.printDebug(uppyFileFileName);
        Debug.printDebug("drFileCode_ " + drFileCode_);
        BaseDAO dao = baseDAO;
        String fileMimeType = ServletActionContext.getServletContext().getMimeType(uppyFileFileName);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Map jsonMap = new HashMap();
        response.setContentType("application/json");
        List<String> mimeTypeList = Arrays.asList("image/jpeg", "image/png", "application/zip", "application/pdf"); //filter upload type
        boolean canProceed = true;
        if (uppyFileFileName.length() > 50) {
            canProceed = false;
            jsonMap.put("status", "fail");
            jsonMap.put("errMsg", "Upload Failed: File name exceeds maximum 50 characters allowed.");
        }

        if (canProceed) {
            if (mimeTypeList.contains(fileMimeType)) {
                FtpInterface ftp = FileOperationUtil.getFtpInterface();
                String strValue = Long.toString(System.currentTimeMillis()).substring(0, 10);

                if (Validator.isEmpty(uploadedFileName)) { //joveni @ 21/6/2022 :: check if null
                    uploadedFileName = drFileCode_.concat("_").concat(strValue).concat(uppyFileFileName.substring(uppyFileFileName.lastIndexOf(".")));
                } else {
                    uploadedFileName = uploadedFileName.concat("_").concat(strValue).concat(uppyFileFileName.substring(uppyFileFileName.lastIndexOf(".")));
                }

                InputStream inStream = new FileInputStream(uppyFile);
                String newDrDocId = CommonFunction.getId(20);

                try {
                    String sftpUploadPath = null;
                    sftpUploadPath = uploadPath(drFileCode_);
                    ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
                    if (ftp.isDirExists(SysConf.get("pathPrefix") + sftpUploadPath, Boolean.FALSE)) {
                        ftp.createDirIfNotExists(sftpUploadPath + "/" + uploadRecordId_, Boolean.FALSE);
                        ftp.createFile(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId, uppyFile);// use File as parameter
                        ftp.insertToFileDirectory(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId, uppyFileFileName, fileMimeType, newDrDocId, baseDAO, drFileCode_, Boolean.FALSE);

                        if (uploadingModel != null) {
                            try {
                                updateUpdatingModel(newDrDocId);
                                dao.beginBatchTransaction();
                                dao.getSession().update(uploadingModel);
                                dao.commitBatchTransaction();
                            } catch (Exception e) {
                                dao.rollbackBatchTransaction();
                                try {
                                    ftp.deleteFile(sftpUploadPath + "/" + newDrDocId); //try to delete if the file is uploaded but record not updated.
                                } catch (Exception delEx) {
                                }
                                throw e;
                            }
                        } else {
                            // Debug.printDebug("drFileCode_ " + drFileCode_);
                            // Debug.printDebug("uploadedFileName " + uploadedFileName);
                            // Debug.printDebug("uppyFileFileName " + uppyFileFileName);
                            // Debug.printDebug("uploadRecordId_ " + uploadRecordId_);
                            // Debug.printDebug("ciId @@## " + ciId);
                            uploadingModel = new FileModel();
                            uploadingModel.setFile_id(newDrDocId);
                            uploadingModel.setFile_type(drFileCode_);
                            uploadingModel.setFile_name(uploadedFileName);
                            uploadingModel.setOriginal_file_name(uppyFileFileName);
                            uploadingModel.setFile_ext(uppyFileFileName.substring(uppyFileFileName.lastIndexOf(".") + 1));
                            uploadingModel.setCase_id(uploadRecordId_);
                            uploadingModel.setCi_id(ciId);
                            uploadingModel.setFile_path(sftpUploadPath + "/" + uploadRecordId_ + "/" + newDrDocId);
                            uploadingModel.setUsj_no("-");
                            uploadingModel.setTotal_files(1);
                            uploadingModel.setTotal_gislayer(0);
                            uploadingModel.setTotal_features(0);
                            uploadingModel.setFile_status("Y");
                            uploadingModel.defaultAddProperties();

                            try {
                                dao.beginBatchTransaction();
                                dao.insert(uploadingModel);
                                dao.commitBatchTransaction();
                            } catch (Exception e) {
                                dao.rollbackBatchTransaction();
                                try {
                                    ftp.deleteFile(sftpUploadPath + "/" + newDrDocId); //try to delete if the file is uploaded but record not updated.
                                } catch (Exception delEx) {
                                }
                                throw e;
                            }
                        }
                    }

                    jsonMap.put("status", "success");
                    jsonMap.put("fileId", newDrDocId);
                    jsonMap.put("fileName", uploadedFileName);

                } catch (BaseException be) {
                    be.printStackTrace();
                    jsonMap.put("status", "fail");
                    jsonMap.put("errMsg", be.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    jsonMap.put("status", "fail");
                    jsonMap.put("errMsg", getText("errors.unknownError_plsTryAgain"));
                } finally {
                    ftp.disconnect();
                }

            } else {
                jsonMap.put("status", "fail");
                jsonMap.put("errMsg", "Upload Failed: Unacceptable file type");
            }
        }

        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
        return null;
    }

    public String getProcess_() {
        return process_;
    }

    public void setProcess_(String process_) {
        this.process_ = process_;
    }

    private String ciId;

    public String getCiId() {
        return ciId;
    }

    public void setCiId(String ciId) {
        this.ciId = ciId;
    }

    public void removeSupportingFile() throws Exception {
        Debug.printDebug("uploadID = " + uploadID);
        DrDocRepoModel docRepo = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id").setParameter("dr_doc_id", uploadID).uniqueResult();
        System.out.println("docRepo " + docRepo);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Map jsonMap = new HashMap();
        response.setContentType("application/json");
        try {
            if (FileOperationUtil.getFtpInterface().deleteFile(docRepo.getDr_doc_path())) {
                BaseDAO dao = baseDAO;
                dao.beginBatchTransaction();
                uploadingModel = (FileModel) baseDAO.getSession().getNamedQuery("FileModel.findBy_fileId").setParameter("file_id", uploadID).uniqueResult();
                uploadingModel_fk = "dr_doc_id";

                // soft delete
                uploadingModel.setFile_status(SystemConstants.FILE_STATUS.SOFT_DELETED);
                dao.getSession().update(uploadingModel);
                dao.commitBatchTransaction();

                jsonMap.put("status", "success");
            } else {
                jsonMap.put("status", "fail");
            }
        } catch (Exception e) {
            jsonMap.put("status", "fail");
            e.printStackTrace();
        }
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getReqFile() {
        return reqFile;
    }

    public void setReqFile(String reqFile) {
        this.reqFile = reqFile;
    }

    private Boolean isAsciiCompatible(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] header = new byte[18]; // Read first 18 bytes to check header
            int bytesRead = fis.read(header);

            if (header[0] != 'C' || header[1] != 'H') {
                return false;
            }

            if (header[11] != 'J' || header[12] != 'o' || header[13] != 'b' ||
                header[14] != 'I' || header[15] != 'n' || header[16] != 'f' || header[17] != 'o') {
                return false;
            }
            
            // byte[] buffer = new byte[8192];
            // int bytesRead;
            // while ((bytesRead = is.read(buffer)) != -1) {
            //     for (int i = 0; i < bytesRead; i++) {
            //         // In ASCII, valid bytes are 0-127
            //         // if ((buffer[i] & 0xFF) > 127) {
            //         //     return false;
            //         // }
            //         if (bytesRead < 18) {
            //             return false; // File too short to be valid CPS
            //         }
            //     }
            // }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void analyzeCpsFile(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            // Store first 50 bytes for magic byte analysis
            byte[] firstBytes = new byte[50];
            int firstBytesRead = fis.read(firstBytes, 0, 50);
            
            // Print the initial byte sequence
            System.out.println("=============== CPS FILE HEADER ANALYSIS ===============");
            System.out.println("First " + firstBytesRead + " bytes:");
            for (int i = 0; i < firstBytesRead; i++) {
                int b = firstBytes[i] & 0xFF;
                String representation = "";
                if (b >= 32 && b <= 126) {
                    representation = " ('" + (char)b + "')";
                } else if (b < 32) {
                    // Add special handling for control characters
                    switch (b) {
                        case 0: representation = " (NUL)"; break;
                        case 9: representation = " (TAB)"; break;
                        case 10: representation = " (LF)"; break;
                        case 13: representation = " (CR)"; break;
                        default: representation = " (control)";
                    }
                }
                System.out.printf("Position %2d: Byte %3d%s\n", i, b, representation);
            }
            
            // Start a new stream for full analysis
            try (FileInputStream fullStream = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                
                // Track byte frequency and other characteristics
                Map<Integer, Integer> byteFrequency = new HashMap<>();
                final int[] totalBytesArray = {0};
                int lineCount = 0;
                int nonAsciiCount = 0;
                
                while ((bytesRead = fullStream.read(buffer)) != -1) {
                    for (int i = 0; i < bytesRead; i++) {
                        int b = buffer[i] & 0xFF;
                        totalBytesArray[0]++;
                        
                        // Count byte frequency
                        byteFrequency.put(b, byteFrequency.getOrDefault(b, 0) + 1);
                        
                        // Track line breaks
                        if (b == 10) lineCount++;
                        
                        // Track non-ASCII bytes
                        if (b > 127) nonAsciiCount++;
                    }
                }
                
                final int totalBytes = totalBytesArray[0];
                
                // Continue with your existing analysis printouts...
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
