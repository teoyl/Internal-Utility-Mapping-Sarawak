package com.sains.framework.base.web;

import com.SysConf;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.sains.framework.model.NullModel;
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
import com.sains.framework.model.DrDocRepoModel;
import com.sains.framework.model.User;
import com.sample.ChildWithAttachmentModel;
import com.sample.UppyModel;
import com.sample.UppyParentModel;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.upload.UploadInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.struts2.ServletActionContext;
//import org.apache.tika.Tika;
//import org.apache.tika.config.TikaConfig;
//import org.apache.tika.io.TikaInputStream;
//import org.apache.tika.metadata.Metadata;
//import org.apache.tika.mime.MediaType;
//import org.apache.tika.mime.MimeType;

public class UppyUploadAction extends BaseActionSupport<NullModel> implements ModelDriven<NullModel> {

    public UppyUploadAction() {
        // change the DAO to the correct service.
        //baseDAO = new BaseDAOImpl();
        model = new NullModel();
        // set the required field for common check.
        //getRequiredParam().put("column_name1", "column.name1");
        //getRequiredParam().put("column_name2", "column.name2");
    }
    
//    private static Map<String, String> pathMap = null;
    ModelBase uploadingModel = null;
    String uploadingModel_fk = null;
    public synchronized String uploadPath(String fileCode) throws Exception{
        String thePath = null;
        System.out.println("getAppCode_modelClass() = " + getAppCode_modelClass());
        if (getAppCode_modelClass().containsKey(fileCode)) {
            System.out.println("uploadRecordId_ = " + uploadRecordId_);
            BaseDAO dao = baseDAO;
            if (!Validator.isEmpty(uploadRecordId_)) {
                if (uploadingModel == null) {
                    uploadingModel = (ModelBase) dao.getModelById(uploadRecordId_, appCode_modelClass.get(fileCode));
                }
                Map<String, String> map = appCode_modelClass_UppyUpload_appCodeSetup.get(fileCode);
                if (map.containsKey(fileCode)) {
                    uploadingModel_fk = map.get(fileCode).split(";")[0];
                    thePath = map.get(fileCode).split(";")[1];
                }
            }
        }
        if (thePath == null) {
            Debug.printFrameworkInfo("--------------------- Cannot find setup path for fileCode = ["+fileCode+"] ---------------------");
            thePath = fileCode;
        }
        return thePath;
    }

    @Override // change the "String" and return value to the
    public NullModel getModel() {
        return model;
    }

    public void specificValidation(String validationType) {
    }

    @Override
    public String processInsert() {
        addActionError("Not supported");
        return "pageError";
    }

    public String loadAddPage() {
        addActionError("Not supported");
        return "pageError";
    }

    //retrieve data for editing.
    public String loadEditPage() {
        addActionError("Not supported");
        return "pageError";
    }

    public String processUpdate() {
        addActionError("Not supported");
        return "pageError";
    }

    public String delete() {
        addActionError("Not supported");
        return "pageError";
    }
    
    private String contentType = "application/pdf";
    private String contentDisposition = "";
    private InputStream inputStream = null;
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
    
    private String uploadedFile_fileId;
    public String getUploadedFile_fileId() {
        return uploadedFile_fileId;
    }
    public void setUploadedFile_fileId(String uploadedFile_fileId) {
        this.uploadedFile_fileId = uploadedFile_fileId;
    }
    
    private String uploadedFile_fileName;
    public String getUploadedFile_fileName() {
        return uploadedFile_fileName;
    }
    public void setUploadedFile_fileName(String uploadedFile_fileName) {
        this.uploadedFile_fileName = uploadedFile_fileName;
    }
    
    InputStream uppyFile_stream = null;
    
    File uppyFile;
    public File getUppyFile() {
        return uppyFile;
    }
    public void setUppyFile(File uppyFile) {
        this.uppyFile = uppyFile;
    }
    
    String uppyFileFileName;
    public String getUppyFileFileName() {
        return uppyFileFileName;
    }
    public void setUppyFileFileName(String uppyFileFileName) {
        this.uppyFileFileName = uppyFileFileName;
    }
    
    public String uploadID;
    public String getUploadID() {
        return uploadID;
    }
    public void setUploadID(String uploadID) {
        this.uploadID = uploadID;
    }
    
    private String uploadRecordId_ = null;
    public String getUploadRecordId_() {
        return uploadRecordId_;
    }
    public void setUploadRecordId_(String uploadRecordId_) {
        this.uploadRecordId_ = uploadRecordId_;
    }
    
    String drFileCode_ = null;
    public String getDrFileCode_() {
        return drFileCode_;
    }
    public void setDrFileCode_(String drFileCode_) {
        this.drFileCode_ = drFileCode_;
    }
    
    private UppyModel yourModel = null;
    public UppyModel getYourModel() {
        return yourModel;
    }
    public void setYourModel(UppyModel yourModel) {
        this.yourModel = yourModel;
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
                        Class c = Class.forName(regClass.getPackage().getName()+"."+regClass.getSimpleName()+"Service");
                        modelBase = (ModelBase) c.getDeclaredConstructor(ModelBase.class, ModelBase.class).newInstance(regClass.newInstance(), null);
                    } catch (Exception e) {
                        modelBase = (ModelBase)regClass.newInstance();
                    }
                    
                    for (String fileCode : modelBase.getUppyUpload_appCodeSetup().keySet()) {
                        appCode_modelClass.put(fileCode, regClass);
                        appCode_modelClass_UppyUpload_appCodeSetup.put(fileCode, modelBase.getUppyUpload_appCodeSetup());
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        }
        return appCode_modelClass;
    }
    
    private void updateUpdatingModel(String value) throws Exception {
        System.out.println("set = " + uploadingModel_fk.substring(0, 1).toUpperCase() + uploadingModel_fk.substring(1));
        Method m = uploadingModel.getClass().getMethod("set" + uploadingModel_fk.substring(0, 1).toUpperCase() + uploadingModel_fk.substring(1), String.class);
        m.invoke(uploadingModel, value);
    }

    public String tusUploadData = null;
    
    public String fileToBeID = null;
    public String getFileToBeID() {
        return CommonFunction.getId(20);
    }

    public String tus_modelID;
    
    public static Map tusUploadMap = new HashMap();
    public void upload_tus() throws Exception {
        Map tusMap = new HashMap();
        System.out.println("drFileCode_ = " + drFileCode_);
        Map jsonMap = new HashMap();
        String newDrDocId = CommonFunction.getId(20);
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        TusFileUploadService tusUpload = null;
        try {
//            String uploadURI = request.getRequestURI();
//            String uploadURI = "/forNewProject/upload_tus";
            String uploadURI = null;
            int slashCount = StringUtils.countMatches(request.getRequestURI(), "/");
            String[] urlArray = request.getRequestURI().split("/");
//            if (slashCount == 2) {
//                uploadURI += "?drFileCode_="+URLEncoder.encode(drFileCode_, "UTF-8")+"&uploadRecordId_="+URLEncoder.encode(uploadRecordId_, "UTF-8");
//            }
System.out.println("---------------------------- arr 1 ----------------------------");
for (String arr : urlArray) {
    System.out.println("arr = " + arr);
}
System.out.println("---------------------------- arr 2 ----------------------------");
            if (urlArray.length >= 7) { 
                request.getRequestURI().lastIndexOf("/");
                uploadURI = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
            } else {
                uploadURI = request.getRequestURI();
            }
            System.out.println("1. response.code = " + uploadURI);
            System.out.println("1. request.method = " + request.getMethod());
            System.out.println("request.getRequestURI() = " + request.getRequestURI());
            System.out.println("request.getRequestURL() = " + request.getRequestURL());
            tusUpload = new TusFileUploadService()
                .withStoragePath("C:\\tus")
                .withDownloadFeature()
                .withThreadLocalCache(true)
                .withUploadURI(uploadURI);
            tusUpload.process(request, response);
    
            try {
                InputStream is1 = tusUpload.getUploadedBytes(request.getRequestURI());
                if (is1 != null) {
                    System.out.println("is1.size = " + is1.available());
                }
            } catch (Exception e) {
            }
            
            UploadInfo uploadInfo = null;
            try {
              uploadInfo = tusUpload.getUploadInfo(request.getRequestURI());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("uploadInfo == null >> " + (uploadInfo==null) );
            if (uploadInfo != null && !uploadInfo.isUploadInProgress()) {
                uppyFileFileName = uploadInfo.getFileName();
                uppyFile_stream = tusUpload.getUploadedBytes(request.getRequestURI());
                System.out.println("uploadInfo.getId() = " + uploadInfo.getId());
                tus_modelID = urlArray[5];
                drFileCode_ = urlArray[4];
                fileToBeID = urlArray[3];
                uppyFile = new File("C:\\tus\\uploads\\"+uploadInfo.getId()+"\\data");
                    upload_for_TUS();
//                try (InputStream is = tusUpload.getUploadedBytes(request.getRequestURI())) {
////                    Path output = this.uploadDirectory.resolve(uploadInfo.getFileName());
////                    Files.copy(is, output, StandardCopyOption.REPLACE_EXISTING);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
            System.out.println("2. response.code = " + response.getStatus());
//            response.setStatus(200);
//            UploadInfo info = tusUpload.getUploadInfo(uploadID);
//            System.out.println("file name = " + info.getFileName());
                    
            jsonMap.put("status", "success");
            jsonMap.put("fileId", newDrDocId);
//            response.setContentType("application/json");
            
//        } catch (BaseException be) {
//            jsonMap.put("status", "fail");
//            jsonMap.put("errMsg", be.getMessage());
        } catch (Exception e) {
            System.out.println("Exception at tus");
            e.printStackTrace();
            jsonMap.put("status", "fail");
            jsonMap.put("errMsg", getText("errors.unknownError_plsTryAgain"));
        } finally {
            ftp.disconnect();
            try {
                tusUpload.deleteUpload(request.getRequestURI());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("response.toString() = " + response.toString());
//        response.getWriter().append(new Gson().toJson(jsonMap));
//        response.setStatus(200);
//        response.flushBuffer();
        
//        return null;
    }
    
    public String upload_for_TUS() throws Exception {
        BaseDAO dao = baseDAO;
        
        String fileMimeType = ServletActionContext.getServletContext().getMimeType(uppyFileFileName);
//        Tika tika = new Tika();  
//        String type = tika.detect(uppyFile);  
//        System.out.println("tika detect file type : " + type);  
//        
//        TikaConfig config = TikaConfig.getDefaultConfig();
//        MediaType mediaType = config.getMimeRepository().detect(TikaInputStream
//                                                    .get(uppyFile), new Metadata());
////        MediaType mediaType = config.getMimeRepository().detect(new BufferedInputStream(inStream), new Metadata());
//        MimeType tika_mimeType = config.getMimeRepository().forName(mediaType.toString());
//        System.out.println("mediaType.getType() = " + mediaType.getType());
//        System.out.println("mediaType.getSubType() = " + mediaType.getSubtype());
//        System.out.println("tika_mimeType.getName() = " + tika_mimeType.getName());
//        System.out.println("tika_mimeType.toString() = " + tika_mimeType.toString());
//        System.out.println("tika_mimeType.getDescription() = " + tika_mimeType.getDescription());
//        System.out.println("tika_mimeType.getExtension() = " + tika_mimeType.getExtension());
//        
//        System.out.println("-------------------------------------------------------------");
//        TikaConfig config = new TikaConfig();
//        MediaType mediaType2 = config.getDetector()
//                                        .detect(TikaInputStream
//                                                    .get(uppyFile_stream), new Metadata());
//        MimeType tika_mimeType = config.getMimeRepository().forName(mediaType2.toString());
//        System.out.println("mediaType2.getType() = " + mediaType2.getType());
//        System.out.println("mediaType2.subType = " + mediaType2.getSubtype());
//        System.out.println("tika_mimeType.getName() = " + tika_mimeType.getName());
//        System.out.println("tika_mimeType.toString() = " + tika_mimeType.toString());
//        System.out.println("tika_mimeType.getDescription() = " + tika_mimeType.getDescription());
//        System.out.println("tika_mimeType.getExtension() = " + tika_mimeType.getExtension());
//        System.out.println("tika_mimeType.getAcronym() = " + tika_mimeType.getAcronym());
//        System.out.println("tika_mimeType.getUniformTypeIdentifier() = " + tika_mimeType.getUniformTypeIdentifier());
//        
//        System.out.println("");
//        System.out.println("-------------------------------------------------------------");
//        MediaType mediaType3 = tika.getDetector().detect(
//        TikaInputStream.get(uppyFile_stream), new Metadata());
//        tika_mimeType = config.getMimeRepository().forName(mediaType3.toString());
//        System.out.println("mediaType3.subType = " + mediaType3.getSubtype());
//        System.out.println("tika_mimeType.getName() = " + tika_mimeType.getName());
//        System.out.println("tika_mimeType.toString() = " + tika_mimeType.toString());
//        System.out.println("tika_mimeType.getDescription() = " + tika_mimeType.getDescription());
//        System.out.println("tika_mimeType.getExtension() = " + tika_mimeType.getExtension());
//        System.out.println("tika_mimeType.getAcronym() = " + tika_mimeType.getAcronym());
//        System.out.println("tika_mimeType.getUniformTypeIdentifier() = " + tika_mimeType.getUniformTypeIdentifier());
//        System.out.println("");
//        System.out.println("-------------------------------------------------------------");
        
//        TusFileUploadService
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Map jsonMap = new HashMap();
        response.setContentType("application/json");
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        
        String newDrDocId = CommonFunction.getId(20);
        try {
            if (tus_modelID != null && !tus_modelID.equals("-")) {
                System.out.println("tus_modelID = " + tus_modelID);
                System.out.println("drFileCode_ = " + drFileCode_);
                System.out.println("appCode_modelClass.get(drFileCode_) = " + appCode_modelClass.get(drFileCode_));
                uploadingModel = (ModelBase) dao.getModelById(tus_modelID, appCode_modelClass.get(drFileCode_));
                if (uploadingModel != null) {
                    uploadRecordId_ = fileToBeID;
                    newDrDocId = fileToBeID;
                } else {
                    System.out.println(" uploading model is null ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            checkFile_checkAccess();
            if (Validator.isEmpty(uploadRecordId_)) {
                newDrDocId = fileToBeID; //replace the newDrDocId to fileToBeID;
                String sftpUploadPath = "uppyTempFolder";
                ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
                if (ftp.isDirExists(SysConf.get("pathPrefix") + sftpUploadPath, Boolean.FALSE)) {
//                    ftp.createFile(sftpUploadPath+"/"+newDrDocId, inStream);//, uppyFileFileName, SFTPBean.ExtensionWhiteList.IMAGE_and_PDF);
                    ftp.createFile(sftpUploadPath+"/"+newDrDocId, uppyFile);
                    ftp.insertToFileDirectory(sftpUploadPath+"/"+newDrDocId, uppyFileFileName, fileMimeType, newDrDocId, baseDAO, drFileCode_, Boolean.TRUE);
//                    ftp.createThumbnail(sftpUploadPath+"/"+newDrDocId, uppyFileFileName, fileMimeType, new FileInputStream(uppyFile));
    //                ftp.insertToFileDirectory(filePath, filePath, fileId, baseDAO);
                }
            } else {
                String sftpUploadPath = null;
                System.out.println("drFileCode_ = " + drFileCode_);
//                if (drFileCode_.startsWith("uppySample2")) {
//                    sftpUploadPath = "uppySample2ActualFolder";
//                } else if (drFileCode_.equals("uppySample3")) {
//                    sftpUploadPath = "uppyMultipleActualFolder";
//                } else if (drFileCode_.equals("directModel_file")) {
//                    sftpUploadPath = "uppyDirectModel_actualFolder";
//                } else {
//                    sftpUploadPath = drFileCode_;
//                }
                sftpUploadPath = uploadPath(drFileCode_);
                ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
                if (ftp.isDirExists(SysConf.get("pathPrefix") + sftpUploadPath, Boolean.FALSE)) {
                    ftp.createDirIfNotExists(sftpUploadPath+"/"+uploadRecordId_, Boolean.FALSE);
//                    ftp.createFile(sftpUploadPath+"/"+uploadRecordId_+"/"+newDrDocId, inStream);//, uppyFileFileName, SFTPBean.ExtensionWhiteList.IMAGE_and_PDF);
                    ftp.createFile(sftpUploadPath+"/"+uploadRecordId_+"/"+newDrDocId, uppyFile);// use File as parameter
                    ftp.insertToFileDirectory(sftpUploadPath+"/"+uploadRecordId_+"/"+newDrDocId, uppyFileFileName, fileMimeType, newDrDocId, baseDAO, drFileCode_, Boolean.FALSE);
//                    ftp.createThumbnail(sftpUploadPath+"/"+uploadRecordId_+"/"+newDrDocId, uppyFileFileName, fileMimeType, new FileInputStream(uppyFile));
                    if (uploadingModel != null) {
                        try {
                            System.out.println("===========================================================");
                            System.out.println("=====-- uploading model not null, help to update fk --=====");
                            System.out.println("===========================================================");
                            System.out.println("uploadingModel_fk = " + uploadingModel_fk);
                            updateUpdatingModel(newDrDocId);
                            dao.beginBatchTransaction();
                            dao.getSession().update(uploadingModel);
                            dao.commitBatchTransaction();
                        } catch (Exception e) {
                            dao.rollbackBatchTransaction();
                            try {
                                ftp.deleteFile(sftpUploadPath+"/"+newDrDocId); //try to delete if the file is uploaded but record not updated.
                            } catch (Exception delEx) {
                            }
                            throw e;
                        }
                    } else if (drFileCode_.equals("userRecord_file")) { //this fileCode is not defined/set in model/modelService's constructor.
                        System.out.println("in drFileCode_ = userRecord_file");
                        User yourModel = (User) dao.getModelById(uploadRecordId_, User.class);
                        yourModel.setProfile_attachment_id(newDrDocId);
                        try {
                            dao.beginBatchTransaction();
                            dao.getSession().update(yourModel);
                            dao.commitBatchTransaction();
                        } catch (Exception e) {
                            dao.rollbackBatchTransaction();
                            try {
                                ftp.deleteFile(sftpUploadPath+"/"+newDrDocId); //try to delete if the file is uploaded but record not updated.
                            } catch (Exception delEx) {
                            }
                            throw e;
                        }
//                    } else if (drFileCode_.startsWith("moreAttachmentModel_")) {
//                        System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
//                        System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
//                        System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
//                        //normally the yourModel should be UppyModel
//                        ChildWithAttachmentModel yourModel = (ChildWithAttachmentModel) dao.getModelById(uploadRecordId_, ChildWithAttachmentModel.class);
//                        if (drFileCode_.endsWith("file2")) {
//                            yourModel.setDr_doc_id_2(newDrDocId);
//                        } else {
//                            yourModel.setDr_doc_id(newDrDocId);
//                        }
//                        try {
//                            dao.beginBatchTransaction();
//                            dao.getSession().update(yourModel);
//                            dao.commitBatchTransaction();
//                        } catch (Exception e) {
//                            dao.rollbackBatchTransaction();
//                            try {
//                                ftp.deleteFile(sftpUploadPath+"/"+newDrDocId); //try to delete if the file is uploaded but record not updated.
//                            } catch (Exception delEx) {
//                            }
//                            throw e;
//                        }
//                    } else if (drFileCode_.startsWith("uppySample2")) {
//                        System.out.println("---------------------------------");
//                        System.out.println("-----starts with uppySample2-----");
//                        System.out.println("---------------------------------");
//                        yourModel = (UppyModel) dao.getModelById(uploadRecordId_, UppyModel.class);
//                        if (drFileCode_.equals("uppySample2_file1")) {
//                            yourModel.setDr_doc_id(newDrDocId);
//                        } else {
//                            yourModel.setDr_doc_id_2(newDrDocId);
//                        }
//                    } else if (drFileCode_.equals("uppySample3")) {
//                        yourModel = new UppyModel();
//                        yourModel.setUppy_parent_id(uploadRecordId_);
//                        yourModel.setDr_doc_id(newDrDocId);
//                    } else if (drFileCode_.equals("directModel_file")) {
//                        yourModel = (UppyModel) dao.getModelById(uploadRecordId_, UppyModel.class);
//                        yourModel.setDr_doc_id(newDrDocId);
                    }
//                    if (yourModel != null) {
//                        try {
//                            dao.beginBatchTransaction();
//                            if (drFileCode_.startsWith("uppySample2")) {
//                                dao.getSession().update(yourModel);
//                            } else if (drFileCode_.equals("uppySample3")) {
//                                //may need to check maximum files allow here... 
//                                dao.insert(yourModel);
//                            }
//                            dao.commitBatchTransaction();
//                        } catch (Exception e) {
//                            dao.rollbackBatchTransaction();
//                            try {
//                                ftp.deleteFile(sftpUploadPath+"/"+newDrDocId); //try to delete if the file is uploaded but record not updated.
//                            } catch (Exception delEx) {
//                            }
//                            throw e;
//                        }
//                    }
                }
            }
            jsonMap.put("status", "success");
            jsonMap.put("fileId", newDrDocId);
            
        } catch (BaseException be) {
            jsonMap.put("status", "fail");
            jsonMap.put("errMsg", be.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("status", "fail");
            jsonMap.put("errMsg", getText("errors.unknownError_plsTryAgain"));
        } finally {
            ftp.disconnect();
        }
        
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
        return null;
    }
    
    public String upload() throws Exception {
        BaseDAO dao = baseDAO;
        String fileMimeType = ServletActionContext.getServletContext().getMimeType(uppyFileFileName);
        try {

            System.out.println("MimetypesFileTypeMap.getDefaultFileTypeMap() = " + MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(uppyFile));
            System.out.println("mimeType = " + fileMimeType);
            
        } catch (Exception e) {
        }
        
//        Path path = new File(uppyFile.getAbsoluteFile()).toPath();
//        Path path = uppyFile.toPath();
//        String mimeType = Files.probeContentType(path);
//        System.out.println("mimeType using path = " + mimeType);
//        
//        MimetypesFileTypeMap fullPath = new MimetypesFileTypeMap("C:\\Program Files\\Java\\jdk1.8.0_51\\lib\\mime.types");
//        System.out.println("mimeType3 = " + fullPath.getContentType(uppyFile));
//        
//        path = new File(uppyFile.getAbsolutePath()).toPath();
//        mimeType = Files.probeContentType(path);
//        System.out.println("mimeType using new file + getAbsolutePath = " + mimeType);
//        
//        URLConnection connection = uppyFile.toURL().openConnection();
//        System.out.println("mimeType4 = " + connection.getContentType());
//        
        InputStream inStream = null;
        if (uppyFile_stream == null) {
            inStream = new FileInputStream(uppyFile);
        } else {
            inStream = uppyFile_stream;
        }
//        String urlConn_guessMimeType = URLConnection.guessContentTypeFromStream(inStream);
//        System.out.println("urlConn_guessMimeType = " + urlConn_guessMimeType);
        
//        Tika tika = new Tika();  
//        String type = tika.detect(uppyFile);  
//        System.out.println("tika detect file type : " + type);  
//        
//        TikaConfig config = TikaConfig.getDefaultConfig();
//        MediaType mediaType = config.getMimeRepository().detect(TikaInputStream
//                                                    .get(uppyFile), new Metadata());
////        MediaType mediaType = config.getMimeRepository().detect(new BufferedInputStream(inStream), new Metadata());
//        MimeType tika_mimeType = config.getMimeRepository().forName(mediaType.toString());
//        System.out.println("mediaType.getType() = " + mediaType.getType());
//        System.out.println("mediaType.getSubType() = " + mediaType.getSubtype());
//        System.out.println("tika_mimeType.getName() = " + tika_mimeType.getName());
//        System.out.println("tika_mimeType.toString() = " + tika_mimeType.toString());
//        System.out.println("tika_mimeType.getDescription() = " + tika_mimeType.getDescription());
//        System.out.println("tika_mimeType.getExtension() = " + tika_mimeType.getExtension());
//        
//        System.out.println("-------------------------------------------------------------");
//        config = new TikaConfig();
//        MediaType mediaType2 = config.getDetector()
//                                        .detect(TikaInputStream
//                                                    .get(inStream), new Metadata());
//        tika_mimeType = config.getMimeRepository().forName(mediaType2.toString());
//        System.out.println("mediaType2.getType() = " + mediaType2.getType());
//        System.out.println("mediaType2.subType = " + mediaType2.getSubtype());
//        System.out.println("tika_mimeType.getName() = " + tika_mimeType.getName());
//        System.out.println("tika_mimeType.toString() = " + tika_mimeType.toString());
//        System.out.println("tika_mimeType.getDescription() = " + tika_mimeType.getDescription());
//        System.out.println("tika_mimeType.getExtension() = " + tika_mimeType.getExtension());
//        System.out.println("tika_mimeType.getAcronym() = " + tika_mimeType.getAcronym());
//        System.out.println("tika_mimeType.getUniformTypeIdentifier() = " + tika_mimeType.getUniformTypeIdentifier());
//        
//        System.out.println("");
//        System.out.println("-------------------------------------------------------------");
//        MediaType mediaType3 = tika.getDetector().detect(
//        TikaInputStream.get(inStream), new Metadata());
//        tika_mimeType = config.getMimeRepository().forName(mediaType3.toString());
//        System.out.println("mediaType3.subType = " + mediaType3.getSubtype());
//        System.out.println("tika_mimeType.getName() = " + tika_mimeType.getName());
//        System.out.println("tika_mimeType.toString() = " + tika_mimeType.toString());
//        System.out.println("tika_mimeType.getDescription() = " + tika_mimeType.getDescription());
//        System.out.println("tika_mimeType.getExtension() = " + tika_mimeType.getExtension());
//        System.out.println("tika_mimeType.getAcronym() = " + tika_mimeType.getAcronym());
//        System.out.println("tika_mimeType.getUniformTypeIdentifier() = " + tika_mimeType.getUniformTypeIdentifier());
//        System.out.println("");
//        System.out.println("-------------------------------------------------------------");
        
//        TusFileUploadService
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Map jsonMap = new HashMap();
        response.setContentType("application/json");
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        
        String newDrDocId = CommonFunction.getId(20);
        
        try {
            checkFile_checkAccess();
            if (Validator.isEmpty(uploadRecordId_)) {
                String sftpUploadPath = "uppyTempFolder";
                ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
                if (ftp.isDirExists(SysConf.get("pathPrefix") + sftpUploadPath, Boolean.FALSE)) {
//                    ftp.createFile(sftpUploadPath+"/"+newDrDocId, inStream);//, uppyFileFileName, SFTPBean.ExtensionWhiteList.IMAGE_and_PDF);
                    ftp.createFile(sftpUploadPath+"/"+newDrDocId, uppyFile);
                    ftp.insertToFileDirectory(sftpUploadPath+"/"+newDrDocId, uppyFileFileName, fileMimeType, newDrDocId, baseDAO, drFileCode_, Boolean.TRUE);
//                    ftp.createThumbnail(sftpUploadPath+"/"+newDrDocId, uppyFileFileName, fileMimeType, new FileInputStream(uppyFile));
    //                ftp.insertToFileDirectory(filePath, filePath, fileId, baseDAO);
                }
            } else {
                String sftpUploadPath = null;
                System.out.println("drFileCode_ = " + drFileCode_);
//                if (drFileCode_.startsWith("uppySample2")) {
//                    sftpUploadPath = "uppySample2ActualFolder";
//                } else if (drFileCode_.equals("uppySample3")) {
//                    sftpUploadPath = "uppyMultipleActualFolder";
//                } else if (drFileCode_.equals("directModel_file")) {
//                    sftpUploadPath = "uppyDirectModel_actualFolder";
//                } else {
//                    sftpUploadPath = drFileCode_;
//                }
                sftpUploadPath = uploadPath(drFileCode_);
                ftp.createDirIfNotExists(sftpUploadPath, Boolean.FALSE);
                if (ftp.isDirExists(SysConf.get("pathPrefix") + sftpUploadPath, Boolean.FALSE)) {
                    ftp.createDirIfNotExists(sftpUploadPath+"/"+uploadRecordId_, Boolean.FALSE);
//                    ftp.createFile(sftpUploadPath+"/"+uploadRecordId_+"/"+newDrDocId, inStream);//, uppyFileFileName, SFTPBean.ExtensionWhiteList.IMAGE_and_PDF);
                    ftp.createFile(sftpUploadPath+"/"+uploadRecordId_+"/"+newDrDocId, uppyFile);// use File as parameter
                    ftp.insertToFileDirectory(sftpUploadPath+"/"+uploadRecordId_+"/"+newDrDocId, uppyFileFileName, fileMimeType, newDrDocId, baseDAO, drFileCode_, Boolean.FALSE);
//                    ftp.createThumbnail(sftpUploadPath+"/"+uploadRecordId_+"/"+newDrDocId, uppyFileFileName, fileMimeType, new FileInputStream(uppyFile));
                    if (uploadingModel != null) {
                        try {
                            System.out.println("===========================================================");
                            System.out.println("=====-- uploading model not null, help to update fk --=====");
                            System.out.println("===========================================================");
                            System.out.println("uploadingModel_fk = " + uploadingModel_fk);
                            updateUpdatingModel(newDrDocId);
                            dao.beginBatchTransaction();
                            dao.getSession().update(uploadingModel);
                            dao.commitBatchTransaction();
                        } catch (Exception e) {
                            dao.rollbackBatchTransaction();
                            try {
                                ftp.deleteFile(sftpUploadPath+"/"+newDrDocId); //try to delete if the file is uploaded but record not updated.
                            } catch (Exception delEx) {
                            }
                            throw e;
                        }
                    } else if (drFileCode_.equals("userRecord_file")) { //this fileCode is not defined/set in model/modelService's constructor.
                        System.out.println("in drFileCode_ = userRecord_file");
                        User yourModel = (User) dao.getModelById(uploadRecordId_, User.class);
                        yourModel.setProfile_attachment_id(newDrDocId);
                        try {
                            dao.beginBatchTransaction();
                            dao.getSession().update(yourModel);
                            dao.commitBatchTransaction();
                        } catch (Exception e) {
                            dao.rollbackBatchTransaction();
                            try {
                                ftp.deleteFile(sftpUploadPath+"/"+newDrDocId); //try to delete if the file is uploaded but record not updated.
                            } catch (Exception delEx) {
                            }
                            throw e;
                        }
//                    } else if (drFileCode_.startsWith("moreAttachmentModel_")) {
//                        System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
//                        System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
//                        System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
//                        //normally the yourModel should be UppyModel
//                        ChildWithAttachmentModel yourModel = (ChildWithAttachmentModel) dao.getModelById(uploadRecordId_, ChildWithAttachmentModel.class);
//                        if (drFileCode_.endsWith("file2")) {
//                            yourModel.setDr_doc_id_2(newDrDocId);
//                        } else {
//                            yourModel.setDr_doc_id(newDrDocId);
//                        }
//                        try {
//                            dao.beginBatchTransaction();
//                            dao.getSession().update(yourModel);
//                            dao.commitBatchTransaction();
//                        } catch (Exception e) {
//                            dao.rollbackBatchTransaction();
//                            try {
//                                ftp.deleteFile(sftpUploadPath+"/"+newDrDocId); //try to delete if the file is uploaded but record not updated.
//                            } catch (Exception delEx) {
//                            }
//                            throw e;
//                        }
//                    } else if (drFileCode_.startsWith("uppySample2")) {
//                        System.out.println("---------------------------------");
//                        System.out.println("-----starts with uppySample2-----");
//                        System.out.println("---------------------------------");
//                        yourModel = (UppyModel) dao.getModelById(uploadRecordId_, UppyModel.class);
//                        if (drFileCode_.equals("uppySample2_file1")) {
//                            yourModel.setDr_doc_id(newDrDocId);
//                        } else {
//                            yourModel.setDr_doc_id_2(newDrDocId);
//                        }
//                    } else if (drFileCode_.equals("uppySample3")) {
//                        yourModel = new UppyModel();
//                        yourModel.setUppy_parent_id(uploadRecordId_);
//                        yourModel.setDr_doc_id(newDrDocId);
//                    } else if (drFileCode_.equals("directModel_file")) {
//                        yourModel = (UppyModel) dao.getModelById(uploadRecordId_, UppyModel.class);
//                        yourModel.setDr_doc_id(newDrDocId);
                    }
//                    if (yourModel != null) {
//                        try {
//                            dao.beginBatchTransaction();
//                            if (drFileCode_.startsWith("uppySample2")) {
//                                dao.getSession().update(yourModel);
//                            } else if (drFileCode_.equals("uppySample3")) {
//                                //may need to check maximum files allow here... 
//                                dao.insert(yourModel);
//                            }
//                            dao.commitBatchTransaction();
//                        } catch (Exception e) {
//                            dao.rollbackBatchTransaction();
//                            try {
//                                ftp.deleteFile(sftpUploadPath+"/"+newDrDocId); //try to delete if the file is uploaded but record not updated.
//                            } catch (Exception delEx) {
//                            }
//                            throw e;
//                        }
//                    }
                }
            }
            jsonMap.put("status", "success");
            jsonMap.put("fileId", newDrDocId);
            
        } catch (BaseException be) {
            jsonMap.put("status", "fail");
            jsonMap.put("errMsg", be.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("status", "fail");
            jsonMap.put("errMsg", getText("errors.unknownError_plsTryAgain"));
        } finally {
            ftp.disconnect();
        }
        
        response.getWriter().append(new Gson().toJson(jsonMap));
        response.flushBuffer();
        return null;
    }
    
    private void checkFile_checkAccess() throws Exception {
        if (drFileCode_.equals("userRecord_file")) {
            if (uppyFileFileName.endsWith(".pdf")) {
                throw new CustomBaseException("PDF not allowed");
            }
            BaseDAO dao = baseDAO;
            Map session = ActionContext.getContext().getSession();
            User user = (User) dao.getModelById((String)session.get("userId"), User.class);
            if (user != null && !user.getUs_user_id().equals("admin")) {
                throw new CustomBaseException("Only admin can update user profile");
            }
        }
    }

    public void removeTempFile() throws Exception {
        System.out.println("uploadID = " + uploadID);
        DrDocRepoModel docRepo = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id").setParameter("dr_doc_id", uploadID).uniqueResult();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        Map jsonMap = new HashMap();
        response.setContentType("application/json");
        try {
//            if (new SFTPBean().deleteFile(docRepo.getDr_doc_path())) {
            if (FileOperationUtil.getFtpInterface().deleteFile(docRepo.getDr_doc_path())) {
                BaseDAO dao = baseDAO;
                dao.beginBatchTransaction();
                uploadPath(drFileCode_);
                if (uploadingModel != null && !Validator.isEmpty(uploadRecordId_)) {
                    try {
                        System.out.println("delete using model's setup (uploadingModel not null)");
                        updateUpdatingModel(null);
//                        dao.beginBatchTransaction();
                        dao.getSession().update(uploadingModel);
//                        dao.commitBatchTransaction();
                    } catch (Exception e) {
//                        dao.rollbackBatchTransaction();
                        throw e;
                    }
//                } else if (drFileCode_.equals("directModel_file")) {
//                    yourModel = (UppyModel) dao.getSession().getNamedQuery("UppyModel.findBy_drDocId").setParameter("drDocId", uploadID).uniqueResult();
//                    if (yourModel != null) {
//                        yourModel.setDr_doc_id(null);
//                        dao.getSession().update(yourModel);
//                    }
//                } else if (drFileCode_.startsWith("uppySample2")) {
//                    yourModel = (UppyModel) dao.getSession().getNamedQuery("UppyModel.findBy_drDocId").setParameter("drDocId", uploadID).uniqueResult();
//                    if (yourModel != null) {
//                        if (drFileCode_.equals("uppySample2_file1")) {
//                            yourModel.setDr_doc_id(null);
//                        } else {
//                            yourModel.setDr_doc_id_2(null);
//                        }
//                        dao.getSession().update(yourModel);
//                    }
                }
                System.out.println("111, docRepo.ID = " + docRepo.getID());
                dao.getSession().delete(docRepo);
                System.out.println("222");
                dao.commitBatchTransaction();
                System.out.println("333");
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
    
    private String tn_ = "N";
    public String getTn_() {
        return tn_;
    }
    public void setTn_(String tn_) {
        this.tn_ = tn_;
    }
    
    final static MimetypesFileTypeMap FILE_TYPE_MAP = new MimetypesFileTypeMap();
    
    public String viewTempFile() throws Exception {
        System.out.println("uploadID = " + uploadID);
        DrDocRepoModel docRepo = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id").setParameter("dr_doc_id", uploadID).uniqueResult();
        FtpInterface ftp = FileOperationUtil.getFtpInterface();
        if (!Validator.isEmpty(tn_) && tn_.equals("Y")) {
            try {
                inputStream = ftp.getFile(docRepo.getDr_doc_path()+ftp.ThumbnailSurfix);
            } catch (Exception e) {
                inputStream = ftp.getFile(docRepo.getDr_doc_path());
//                inputStream = ftp.createThumbnail(docRepo.getDr_doc_path()+ftp.ThumbnailSurfix, docRepo.getDr_doc_name(), ServletActionContext.getServletContext().getMimeType(docRepo.getDr_doc_name()), inputStream);
                System.out.println("size = " + inputStream.available());
                inputStream = ftp.getFile(docRepo.getDr_doc_path()+ftp.ThumbnailSurfix);
            }
        } else {
            inputStream = ftp.getFile(docRepo.getDr_doc_path());
        }
//        contentDisposition = "attachment;filename="+docRepo.getDr_doc_name();
        contentDisposition = "filename=\""+StringEscapeUtils.escapeHtml4(docRepo.getDr_doc_name())+"\"";
        contentType = docRepo.getMime_type();
        if (Validator.isEmpty(docRepo.getMime_type())) {
            contentType = FILE_TYPE_MAP.getContentType(docRepo.getDr_doc_name());
            if (contentType.startsWith("application/")) {
                contentType = "application/" + docRepo.getDr_doc_name().substring(docRepo.getDr_doc_name().lastIndexOf(".")+1);
            }
        }
        System.out.println("contentType = " + contentType);
//        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
//        response.setContentType(contentType);
//        response.setContentLength((int) inputStream.available());
//         
//        // forces download
//        String headerKey = "Content-Disposition";
////        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
//        response.setHeader(headerKey, contentDisposition);
//         
//        // obtains response's output stream
//        OutputStream outStream = response.getOutputStream();
//         
//        byte[] buffer = new byte[4096];
//        int bytesRead = -1;
//         
//        while ((bytesRead = inputStream.read(buffer)) != -1) {
//            outStream.write(buffer, 0, bytesRead);
//        }
//         
//        inputStream.close();
//        outStream.close(); 
//        response.flushBuffer();
//        return null;
        return "fileDownload";
    }
    
    public String singleFile() {
        yourModel = (UppyModel) baseDAO.getSession().getNamedQuery("UppyModel.findBy_createdBy")
                .setParameter("createdBy", ActionContext.getContext().getSession().get("loginId"))
                .uniqueResult();
        DrDocRepoModel drDoc = null;
        if (yourModel != null) {
            drDoc = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id")
                .setParameter("dr_doc_id", yourModel.getDr_doc_id())
                .uniqueResult();
        } else {
            //retrieve 1st file
            drDoc = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_drDocApp_createdUser")
                .setParameter("drDocApp", "uppySample2_file1").setParameter("createdBy", ActionContext.getContext().getSession().get("loginId"))
                .uniqueResult();
            yourModel = new UppyModel();
            if (drDoc != null) {
                System.out.println("DrModel is not null, ID=" + drDoc.getID());
                yourModel.setDrDocRepoModel(drDoc);
                yourModel.setDr_doc_id(drDoc.getID());
            }
            //retrieve 2nd file
            drDoc = (DrDocRepoModel) baseDAO.getSession().getNamedQuery("DocRepo.findBy_drDocApp_createdUser")
                .setParameter("drDocApp", "uppySample2_file2").setParameter("createdBy", ActionContext.getContext().getSession().get("loginId"))
                .uniqueResult();
            if (drDoc != null) {
                System.out.println("DrModel2 is not null, ID=" + drDoc.getID());
                yourModel.setDrDocRepoModel2(drDoc);
                yourModel.setDr_doc_id_2(drDoc.getID());
            }
        }
        if (drDoc != null) {
            uploadedFile_fileId = drDoc.getDr_doc_id();
            uploadedFile_fileName = drDoc.getDr_doc_name();
        }
        return "singleFile";
    }
    
    private String uppySampleSingleFileId;
    public String getUppySampleSingleFileId() {
        return uppySampleSingleFileId;
    }
    public void setUppySampleSingleFileId(String uppySampleSingleFileId) {
        this.uppySampleSingleFileId = uppySampleSingleFileId;
    }
    
    
    public String saveSingleFile() throws Exception {
        System.out.println("yourModel.desc = " + yourModel.getUppy_file_desc());
        BaseDAO dao = baseDAO;
        if (Validator.isEmpty(yourModel.getID())) {//new record (insert)
            yourModel.set_uppyUploadFile_drDocPath("uppySample2ActualFolder");
            yourModel.set_uppyUploadFile_drDocId(yourModel.getDr_doc_id());
            if (yourModel.getDr_doc_id_2() != null) {
                if (yourModel.get_uppyUploadFile_drDocId() == null) {
                    yourModel.set_uppyUploadFile_drDocId(yourModel.getDr_doc_id_2());
                } else {
                    yourModel.set_uppyUploadFile_drDocId(yourModel.get_uppyUploadFile_drDocId() +","+ yourModel.getDr_doc_id_2());
                }
            }
            dao.insert(yourModel);
        } else {//existing record (update)
            dao.update(yourModel); //only update the updatable columns (in this case, the uppy_file_desc)
        }
        return singleFile();
    }
    
    UppyParentModel yourParentModel;
    public UppyParentModel getYourParentModel() {
        return yourParentModel;
    }
    public void setYourParentModel(UppyParentModel yourParentModel) {
        this.yourParentModel = yourParentModel;
    }
    
    public String multiFile() { //Multiple file upload
        yourParentModel = (UppyParentModel) baseDAO.getSession().getNamedQuery("UppyParentModel.findBy_createdBy")
                .setParameter("createdBy", ActionContext.getContext().getSession().get("loginId"))
                .uniqueResult();
        if (yourParentModel == null) {
            System.out.println("multiFile() yourParentModel is null");
            yourParentModel = new UppyParentModel();
            List drDocList = baseDAO.getSession().getNamedQuery("DocRepo.findBy_drDocApp_createdUser")
                .setParameter("createdBy", ActionContext.getContext().getSession().get("loginId"))
                .setParameter("drDocApp", "uppySample3")
                .list();
            getUppyFileMap().put("childList", drDocList);
            
//            UppyModel uppy1 = new UppyModel();
//            uppy1.setUppy_file_desc("first file");
//            uppy1.setDr_doc_id("abc123");
//            yourParentModel.getChildList().add(uppy1);
//            UppyModel uppy2 = new UppyModel();
//            uppy2.setUppy_file_desc("2nd file");
//            yourParentModel.getChildList().add(uppy2);
        } else {
            System.out.println("yourParentModel is not null :child.size = " + yourParentModel.getChildList().size());
            List list = new ArrayList();
            for (UppyModel uppy : yourParentModel.getChildList()) {
                list.add(uppy.getDrDocRepoModel());
            }
            getUppyFileMap().put("childList", list);
        }
        //if you have multiple file input, put the list to uppyFileMap
        return "multiFile";
    }
    
    private List<String> multipleFileDrDocIds = null;
    public List<String> getMultipleFileDrDocIds() {
        return multipleFileDrDocIds;
    }
    public void setMultipleFileDrDocIds(List<String> multipleFileDrDocIds) {
        this.multipleFileDrDocIds = multipleFileDrDocIds;
    }
    
    public String saveMultiFile() throws Exception {
        System.out.println("yourParentModel.desc = " + yourParentModel.getUppy_parent_desc());
        BaseDAO dao = baseDAO;
        if (Validator.isEmpty(yourParentModel.getID())) {//new record (insert)
            System.out.println("insert ---");
            if (multipleFileDrDocIds !=null) {
                for (String drDocId : multipleFileDrDocIds) {
                    DrDocRepoModel docRepo = (DrDocRepoModel)baseDAO.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id_notTemp").setParameter("dr_doc_id", drDocId).uniqueResult();
                    if (docRepo == null) {

                        UppyModel uppyModel = new UppyModel();
                        uppyModel.setAppendRecordID(Boolean.FALSE);
                        uppyModel.setDr_doc_id(drDocId);
                        uppyModel.set_uppyUploadFile_drDocPath("uppyMultipleActualFolder");
                        uppyModel.set_uppyUploadFile_drDocId(drDocId);
                        yourParentModel.getChildList().add(uppyModel);
                    }
                }
            }
            dao.insert(yourParentModel);
        } else {//existing record (update)
            System.out.println("update ---");
            dao.update(yourParentModel); //only update the updatable columns (in this case, the uppy_file_desc)
        }
        return multiFile();
    }
}
