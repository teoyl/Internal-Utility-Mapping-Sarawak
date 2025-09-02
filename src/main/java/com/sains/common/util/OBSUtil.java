package com.sains.common.util;

import com.SysConf;
import java.io.*;
import com.jcraft.jsch.*;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.exception.ObsException;
import com.obs.services.model.AbortMultipartUploadRequest;
import com.obs.services.model.CompleteMultipartUploadRequest;
import com.obs.services.model.DeleteObjectsRequest;
import com.obs.services.model.DeleteObjectsResult;
import com.obs.services.model.DeleteObjectsResult.DeleteObjectResult;
import com.obs.services.model.InitiateMultipartUploadRequest;
import com.obs.services.model.InitiateMultipartUploadResult;
import com.obs.services.model.KeyAndVersion;
import com.obs.services.model.ListMultipartUploadsRequest;
import com.obs.services.model.ListVersionsRequest;
import com.obs.services.model.ListVersionsResult;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.PartEtag;
import com.obs.services.model.RenameObjectRequest;
import com.obs.services.model.RenameObjectResult;
import com.obs.services.model.UploadPartRequest;
import com.obs.services.model.UploadPartResult;
import com.obs.services.model.VersionOrDeleteMarker;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.model.DrDocRepoModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
//import hrpay_bean.UtilBean;

public class OBSUtil extends FtpInterface {

//    private static final String append = "/home/eqpdev/sftp/";
    private static final String append = SysConf.get("pathPrefix");
    private Long fileSizeToUseMultipart = 100 * 1024 * 1024L; //100MB to use multipart upload
    private Long multipartSize = 10 * 1024 * 1024L; //size of each multipart (default to 10M)
    private ObsClient obsClient = null;
    private static final String endPoint = SysConf.get("obs.endPoint");
    private static final String ak = SysConf.get("obs.ak");
    private static final String sk = SysConf.get("obs.sk");
    private static String bucketName = SysConf.get("obs.bucketName");
    
    //for testing speed bt FTP Server and OBS
//    private static final String ak = "Z1X4YZLNUEOBNCHNJOVI";
//    private static final String sk = "aEh12b2b01V1DSCi6VA5X42LxvfWglKzZyhFX4rn";
//    private static String bucketName = "obs-test-20200805";
    private void initOBSClient() {
        if (obsClient == null) {
            ObsConfiguration config = new ObsConfiguration();
            config.setSocketTimeout(30000);
            config.setConnectionTimeout(10000);
            config.setEndPoint(endPoint);
            obsClient = new ObsClient(ak, sk, config);
        }
    }
    public Boolean isFileExists_noAppend(String fileDir, Boolean closeSftp) throws Exception {
        try {
            initOBSClient();
//            System.out.println("isFileExist = " + append + fileDir);

            ObjectMetadata metadata = obsClient.getObjectMetadata(bucketName, fileDir);
            return Boolean.TRUE;
        } catch (Exception e) {
//            CommonFunction.exceptionStackTrace(e);
        } finally {
            if (closeSftp) {
                disconnect();
            }
        }
        return Boolean.FALSE;
    }
    public Boolean isFileExists(String fileDir, Boolean closeSftp) throws Exception {
        try {
            initOBSClient();
//            System.out.println("isFileExist = " + append + fileDir);
            ObjectMetadata metadata = obsClient.getObjectMetadata(bucketName, append + fileDir);
            return Boolean.TRUE;
        } catch (Exception e) {
//            CommonFunction.exceptionStackTrace(e);
        } finally {
            if (closeSftp) {
                disconnect();
            }
        }
        return Boolean.FALSE;
    }

    public InputStream getFile(String fileDir) throws Exception {

        Debug.printDebug("---Get File fileDir "+append + fileDir);
        InputStream inStream = null;
        try {
            initOBSClient();
//            channelSftp.cd(append + fileDir);            
            inStream = obsClient.getObject(bucketName, append + fileDir).getObjectContent();
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
            Debug.printError("--error getting file :: "+append + fileDir);
        } finally {
            disconnect();
        }
        return inStream;
    }
    public InputStream getFile_noAppend(String fileDir) throws Exception {

        Debug.printDebug("---Get File fileDir "+ fileDir);
        InputStream inStream = null;
        try {
            initOBSClient();
            inStream = obsClient.getObject(bucketName, fileDir).getObjectContent();
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
            Debug.printError("--error getting file :: "+ fileDir);
        } finally {
            disconnect();
        }
        return inStream;
    }
    
    public Boolean createFile(String fileDir, File file, String fileName, String[] extWhiteList) throws Exception {
        checkExtension(fileName, extWhiteList);
        return createFile(fileDir, file);
    }
    public Boolean createFile(String fileDir, File file) throws Exception {
        try {
            initOBSClient();            
            Debug.printDebug("---Create File fileDir "+append +fileDir);
            obsUploadFile(file, cleanXML_fileName(append + fileDir));
            return Boolean.TRUE;
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return Boolean.FALSE;
    }

    public void moveTempFile(String newPath, String drDocId, BaseDAO dao) throws Exception {
        DrDocRepoModel docRepo = (DrDocRepoModel) dao.getSession().getNamedQuery("DocRepo.findBy_dr_doc_id").setParameter("dr_doc_id", drDocId).uniqueResult();
        try {
            initOBSClient();
//           try {
            String toDeletePath = docRepo.getDr_doc_path();
            System.out.println("toDeletePath ");
            copyFile_noClose(cleanXML_fileName(docRepo.getDr_doc_path()), cleanXML_fileName(newPath));
            deleteFile(toDeletePath);
            try {
                dao.beginBatchTransaction();
                docRepo.setDr_doc_temp("N");
                docRepo.setDr_doc_path(newPath);
                dao.getSession().save(docRepo);
                dao.commitBatchTransaction();
            } catch (Exception e) {
                dao.rollbackBatchTransaction();
            }
//            client.put(new FileInputStream(file), fileDir);
        } catch (Exception e) {
//            CommonFunction.exceptionStackTrace(e);
            throw e;
        } finally {
            
        }
    }
    public void moveTempFile(String newPath, String drDocId, org.hibernate.Session session) throws Exception {
        DrDocRepoModel docRepo = (DrDocRepoModel) session.getNamedQuery("DocRepo.findBy_dr_doc_id").setParameter("dr_doc_id", drDocId).uniqueResult();
        try {
            initOBSClient();
//           try {
System.out.println("444 newPath = " + newPath);
            copyFile_noClose(cleanXML_fileName(docRepo.getDr_doc_path()), cleanXML_fileName(newPath));
            deleteFile(docRepo.getDr_doc_path());
            docRepo.setDr_doc_temp("N");
            docRepo.setDr_doc_path(cleanXML_fileName(newPath));
            session.save(docRepo);
        } catch (Exception e) {
            throw e;
        } finally {
            disconnect();
        }
    }
    
    @Override
    public void insertToFileDirectory(String filePath, String fileName, String mime_type, String fileId, BaseDAO dao, String docApplication, Boolean isTemp) throws Exception {
        DrDocRepoModel docRepo = new DrDocRepoModel();
        docRepo.defaultAddProperties();
        docRepo.setDr_doc_id(fileId);
        docRepo.setDr_doc_name(fileName);
        docRepo.setMime_type(mime_type);
        docRepo.setDr_doc_path(cleanXML_fileName(filePath));
        docRepo.setDr_doc_application(docApplication);
        if (isTemp) {
            docRepo.setDr_doc_temp("Y");
        }
        if (fileName.contains(".")) {
            docRepo.setDr_doc_type(fileName.substring(fileName.lastIndexOf(".")+1));
        } else {
            throw new CustomBaseException("Unknown file extension");
        }
        try {
            dao.beginBatchTransaction();
            dao.getSession().save(docRepo);
            dao.commitBatchTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollbackBatchTransaction();
            deleteFile(cleanXML_fileName(filePath));
            throw new CustomBaseException("DB Error : " + e.getCause());
        }
    }
    
    private String cleanXML_fileName(String oriFileName) {
        return oriFileName.replaceAll("[\\000]*", "");
    }
    
    public Boolean createFile(String fileDir, InputStream file, String fileName, String[] extWhiteList) throws Exception {
        checkExtension(fileName, extWhiteList);
        return createFile(fileDir, file);
    }
    public Boolean createFile(String fileDir, InputStream file) throws Exception {
        try {
            initOBSClient();
//            System.out.println("---OBS Create File fileDir "+append +fileDir);
            //client.storeFile(fileDir, file);
            
            obsUploadFile(file, cleanXML_fileName(append + fileDir));
            return Boolean.TRUE;
        } catch (Exception e) {
            Debug.printError("### SFTP Error: File creation fail : "+append+fileDir);
            deleteFile(fileDir);
//            CommonFunction.exceptionStackTrace(e);
            throw e;
        } finally {
            disconnect();
        }
//        return Boolean.FALSE;
    }    
    
    public Boolean deleteFile_noAppend(String fileDir) throws Exception {
        try {
            initOBSClient();
            obsClient.deleteObject(bucketName, cleanXML_fileName(fileDir));
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return Boolean.FALSE;
    }
    public Boolean deleteFile(String fileDir) throws Exception {
        try {
            initOBSClient();
            obsClient.deleteObject(bucketName, cleanXML_fileName(append + fileDir));
            return Boolean.TRUE;
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return Boolean.FALSE;
    }
    public List<String> listFolder(String fileDir, Boolean includeSubfolder) throws Exception {
        List returnList = new ArrayList();
        try {
            initOBSClient();
            ListVersionsRequest request = new ListVersionsRequest (bucketName, 50);
//            ListVersionsRequest request = new ListVersionsRequest (bucketName);
            String prefix = append + fileDir;
            request.setPrefix(prefix);
            Integer prefixLength = prefix.length();
            ListVersionsResult result = obsClient.listVersions(request);
            for(VersionOrDeleteMarker v : result.getVersions()){
                if (v.getKey().equals(prefix)) continue;
                String noPrefixName = v.getKey().substring(prefixLength);
                if (!includeSubfolder){
                    if (!noPrefixName.contains("/")) {
                        returnList.add(v.getKey());
                    }
                } else {
                    returnList.add(v.getKey());
                }
    //            System.out.println("\t" + v.getKey());
    //            System.out.println("\t" + v.getOwner());
    //            System.out.println("\t" + v.isDeleteMarker());

            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return returnList;
    }
    public Boolean deleteFolder(String fileDir) throws Exception {
        try {
            initOBSClient();
            obsClient.deleteObject(bucketName, cleanXML_fileName(append + fileDir));
            ListVersionsRequest request = new ListVersionsRequest (bucketName);
            request.setPrefix(cleanXML_fileName(append + fileDir));
            ListVersionsResult result = obsClient.listVersions(request);
            
            KeyAndVersion[] kvs = new KeyAndVersion[result.getVersions().length];
            Integer index=0;
            for(VersionOrDeleteMarker v : result.getVersions()){
                kvs[index++] = new KeyAndVersion(v.getKey());
            }
            
            DeleteObjectsRequest delRequest = new DeleteObjectsRequest();
            delRequest.setBucketName(bucketName);
            delRequest.setQuiet(false);
            
            delRequest.setKeyAndVersions(kvs);
            
//            System.out.println("Delete results:");
            obsClient.deleteObjects(delRequest);
//            DeleteObjectsResult deleteObjectsResult = obsClient.deleteObjects(delRequest);
//            for (DeleteObjectResult object : deleteObjectsResult.getDeletedObjectResults())
//            {
//                System.out.println("\t" + object);
//            }
            
            return Boolean.TRUE;
                
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return Boolean.FALSE;
    }

    public Boolean renameFile(String fileDir, String newFileDir) throws Exception {
        try {
            initOBSClient();
            RenameObjectRequest request = new RenameObjectRequest();
            request.setBucketName(bucketName);
            request.setObjectKey(cleanXML_fileName(fileDir));
            request.setNewObjectKey(cleanXML_fileName(newFileDir));
            RenameObjectResult result = obsClient.renameObject(request);
            return Boolean.TRUE;
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return Boolean.FALSE;
    }

    public Boolean isDirExists(String fileDir, Boolean closeSftp) throws Exception {
        if (!fileDir.endsWith("/"))
            fileDir += "/"; 
        Debug.printFrameworkDebug("append + fileDir= " + fileDir);
        try {
            initOBSClient();
            ObjectMetadata metadata = obsClient.getObjectMetadata(bucketName, fileDir);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
//            CommonFunction.exceptionStackTrace(e);
        } finally {
            if (closeSftp) {
                disconnect();
            }
        }
        return Boolean.FALSE;
    }

    public Boolean createDirIfNotExists(String fileDir, Boolean closeAfterUse) throws Exception {
        try {
            Debug.printDebug("---createdirifnotexist = " + cleanXML_fileName(append + fileDir));
            initOBSClient();
            if (!fileDir.endsWith("/")) {
                fileDir += "/";
            }
            createFile(cleanXML_fileName(fileDir), new ByteArrayInputStream(new byte[0]));
            return Boolean.TRUE;

        } catch (Exception e) {
            throw e;
//            e.printStackTrace();
        } finally {
            if (closeAfterUse) {
                disconnect();
            }
        }
    }

    public Boolean copyFile(String fileDir, String targetFileDir) throws Exception {
//        System.out.println("-------------------------------------------");
//        System.out.println("1. doing copyFile:: fileDir = " + append + fileDir.trim());
//        System.out.println("2. doing copyFile:: targetFileDir = " + append + targetFileDir.trim());
//        System.out.println("-------------------------------------------");
        try {
            initOBSClient();
//           try {
            obsClient.copyObject(bucketName, cleanXML_fileName(append + fileDir.trim()), bucketName, cleanXML_fileName(append + targetFileDir.trim()));
//            client.put(new FileInputStream(file), fileDir);
            return Boolean.TRUE;
        } catch (Exception e) {
//            CommonFunction.exceptionStackTrace(e);
            throw e;
        } finally {
            disconnect();
        }
//        return Boolean.FALSE;
    }
    
    //copy from sftp2util - ahmadni @ 20-Sept-2018
    public InputStream getFile_noClose(String fileDir) throws Exception {

//        System.out.println("---getFile_noClose fileDir "+append + fileDir);
        InputStream inStream = null;
        try {
            initOBSClient();
            inStream = obsClient.getObject(bucketName, cleanXML_fileName(append + fileDir)).getObjectContent();
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
        }
        return inStream;
    }
    
    public Boolean copyFile_noClose(String fileDir, String targetFileDir) throws Exception {
//        System.out.println("-------------------------------------------");
//        System.out.println("1. doing copyFile:: fileDir = " + append + fileDir.trim());
//        System.out.println("2. doing copyFile:: targetFileDir = " + append + targetFileDir.trim());
//        System.out.println("-------------------------------------------");
        try {
            initOBSClient();
            obsClient.copyObject(bucketName, cleanXML_fileName(append + fileDir), bucketName, cleanXML_fileName(append + targetFileDir));
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            
        }
//        return Boolean.FALSE;
    }
    
    @Override
    public void disconnect() throws Exception {
        if (obsClient != null) {
            try {
                obsClient.close();
                obsClient = null;
            } catch (IOException e) {
            }
        }
    }
    
    private void obsUploadFile(Object fileOrStream, String path_name) {
        try {
            initOBSClient();
            if (fileOrStream instanceof File) {
                File theFile = (File)fileOrStream;
                if (theFile.length() >= fileSizeToUseMultipart) {
                    doMultipartUpload(theFile, path_name);
                } else {
                    normalUpload(theFile, path_name);
                }
            } else {
                InputStream stream = (InputStream) fileOrStream;
                normalUpload(fileOrStream, path_name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void doMultipartUpload(File theFile, String fileDir) throws Exception {
//        System.out.println("doing multipart uploading");
        // Create an instance of ObsClient.
        final ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        // Initialize the thread pool.
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        final File largeFile = theFile;

        // Initialize the multipart upload.
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, fileDir);
        InitiateMultipartUploadResult result = obsClient.initiateMultipartUpload(request);

        final String uploadId = result.getUploadId();
//        System.out.println("\t"+ uploadId + "\n");

        long fileSize = largeFile.length();

        // Calculate the number of parts need to be uploaded.
        long partCount = fileSize % multipartSize == 0 ? fileSize / multipartSize : fileSize / multipartSize + 1;

        final List<PartEtag> partEtags = Collections.synchronizedList(new ArrayList<PartEtag>());

        // Start uploading parts concurrently.
        for (int i = 0; i < partCount; i++)
        {
//            System.out.println("upload...");
            // Start position of parts in the file
            final long offset = i * multipartSize;
            // Part size
            final long currPartSize = (i + 1 == partCount) ? fileSize - offset : multipartSize;
            // Part number
            final int partNumber = i + 1;
            executorService.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    UploadPartRequest uploadPartRequest = new UploadPartRequest();
                    uploadPartRequest.setBucketName(bucketName);
                    uploadPartRequest.setObjectKey(fileDir);
                    uploadPartRequest.setUploadId(uploadId);
                    uploadPartRequest.setFile(largeFile);
                    uploadPartRequest.setPartSize(currPartSize);
                    uploadPartRequest.setOffset(offset);
                    uploadPartRequest.setPartNumber(partNumber);

                    UploadPartResult uploadPartResult;
                    try
                    {
                        uploadPartResult = obsClient.uploadPart(uploadPartRequest);
//                        System.out.println("Part#" + partNumber + " done\n");
                        partEtags.add(new PartEtag(uploadPartResult.getEtag(), uploadPartResult.getPartNumber()));
                    }
                    catch (ObsException e)
                    {   
                        try { //try to do reupload of part (not sure is the correct way or not) Retry#1
                            try {
                                Thread.currentThread().sleep(1000); //wait 1 second before retry
                            } catch (Exception e1) {}
                            uploadPartResult = obsClient.uploadPart(uploadPartRequest);
                            partEtags.add(new PartEtag(uploadPartResult.getEtag(), uploadPartResult.getPartNumber()));
                        } catch (ObsException obsEx2) {
                            try { //try to do reupload of part (not sure is the correct way or not) Retry#2
                                try {
                                    Thread.currentThread().sleep(1000); //wait 1 second before retry
                                } catch (Exception e1) {}
                                uploadPartResult = obsClient.uploadPart(uploadPartRequest);
                                partEtags.add(new PartEtag(uploadPartResult.getEtag(), uploadPartResult.getPartNumber()));
                            } catch (ObsException obsEx3) {
                                obsEx3.printStackTrace();
                            }
                        }
                    }
                }
            });
        }

        // Wait until the upload is complete.
        executorService.shutdown();
        while (!executorService.isTerminated())
        {
            try
            {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        // Combine parts.
        try {
            CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, fileDir, uploadId, partEtags);
            ListMultipartUploadsRequest listMultipartUploadRequest = new ListMultipartUploadsRequest(bucketName);
//            System.out.println("size of multipart upload = " + obsClient.listMultipartUploads(listMultipartUploadRequest).getMultipartTaskList().size());
            obsClient.completeMultipartUpload(completeMultipartUploadRequest);
        } catch (ObsException e) {
            AbortMultipartUploadRequest abortMultipartUploadRequest = new AbortMultipartUploadRequest(bucketName, fileDir, uploadId);
            obsClient.abortMultipartUpload(abortMultipartUploadRequest);
        } 
    }
    
    private void normalUpload(Object fileOrStream, String fileDir) {
        initOBSClient();
        if (fileOrStream instanceof File) {
            obsClient.putObject(bucketName, fileDir, (File)fileOrStream);
        } else {
            obsClient.putObject(bucketName, fileDir, (InputStream)fileOrStream);
        }
    }
}
