package com.sains.common.util;

import com.SysConf;
import java.io.*;
import com.jcraft.jsch.*;
import static com.sains.common.util.FtpsUtil.str;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ServiceFactory;
import com.sains.framework.model.DrDocRepoModel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Stream;
//import hrpay_bean.UtilBean;

public class SFTPBean extends FtpInterface {

    private ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private String FTP_CONNECT_STRING = SysConf.get("fs.url");
    private String FTP_USER_NAME = SysConf.get("fs.user");
    private String FTP_PASSWORD = SysConf.get("fs.pass");
    private int FTP_PORT = Integer.parseInt(SysConf.get("fs.port"));

    private static Boolean init = Boolean.FALSE;
    private JSch jsch;
//    private static final String append = "/home/eqpdev/sftp/";
    private static final String append = SysConf.get("fs.pathPrefix");
    private Session session = null;
    private Channel channel = null;
    private ChannelSftp channelSftp = null;

    private void connectSSH() throws Exception {
        jsch = new JSch();
        try {

//            System.out.println("----" + FTP_USER_NAME + "::" + FTP_CONNECT_STRING + "::" + FTP_PORT);
            session = jsch.getSession(FTP_USER_NAME, FTP_CONNECT_STRING, FTP_PORT);
//            session.setPassword(str(FTP_PASSWORD));
            session.setPassword((FTP_PASSWORD));
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("kex","diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256");
            session.setConfig(config);
//            session.setTimeout(20000); // if no set timeout, will try for 20 seconds if server down/not available
            session.connect(20000);
            if (session.isConnected()) {
//                System.out.println("---Session connected.");
            } else {
                Debug.printDebug("---Fail login");
            }
//            channel = session.openChannel("sftp");
//            channel.connect();
//            
//            channelSftp = (ChannelSftp) channel;
        } catch (Exception e) {
            Debug.printError("--connect ssh " + e);
            throw e;
        }

    }

    private Boolean isFromPool = Boolean.FALSE;
    private Boolean pushed = Boolean.FALSE;
    private void initSftp() throws Exception {
        if (isFromPool) return;
        Map poolMap = serviceFactory.getSFTPBeanConnectionPool().getConnection();
        if (poolMap != null) {
            isFromPool = Boolean.TRUE;
            channel = (Channel)poolMap.get("channel");
            session = (Session)poolMap.get("session");
            channelSftp = (ChannelSftp)poolMap.get("channelSftp");
//            if (!session.isConnected()) {
//                session.connect(20000);
//            }
        } else {
            if (session == null || !session.isConnected()) {
                connectSSH();
            }
            //Open the SFTP channel
            if (channel == null || channel.isClosed()) {
    //            System.out.println("open................");
    //            System.out.println("---open channel");
                channel = session.openChannel("sftp");
                channel.connect();
            }
            if (channelSftp == null || channelSftp.isClosed()) {
    //            System.out.println("---open channel sftp");
                channelSftp = (ChannelSftp) channel;
    //            channelSftp = new ChannelSftp();
            }
        }
        init = Boolean.TRUE;
    }

    public void disconnect() throws Exception {
        if (!isFromPool) {
            if (channelSftp != null) {
                channelSftp.exit();
                channelSftp = null;
            }
            if (channel != null) {
                channel.disconnect();
                channel = null;
            }
            if (session != null) {
                session.disconnect();
                session = null;
            }
        } else {
            if (session != null) {
                serviceFactory.getSFTPBeanConnectionPool().closeConnection(session, channel, channelSftp);
                session=null; channel=null; channelSftp=null;isFromPool=Boolean.FALSE;
            }
        }
        init = Boolean.FALSE;
    }

    public Boolean isFileExists_noAppend(String fileDir, Boolean closeSftp) throws Exception {
        try {
            initSftp();
//            System.out.println("isFileExist = " + append + fileDir);

            channelSftp.get(fileDir);
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
            initSftp();
//            System.out.println("isFileExist = " + append + fileDir);

            channelSftp.get(append + fileDir);
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
            initSftp();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            channelSftp.cd(append + fileDir);            
            channelSftp.get(append + fileDir, out);
            inStream = new ByteArrayInputStream(out.toByteArray());
            
//            Path p = Paths.get(append + fileDir);
//            String fileName = p.getFileName().toString();
//            Path path = p.getParent().toAbsolutePath();
//            System.out.println("path "+path);
//            System.out.println("filename = "+fileName);
            
//            channelSftp.cd(path.toString());
//            inStream = channelSftp.get(fileName);
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
            initSftp();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            channelSftp.cd(append + fileDir);            
            channelSftp.get(fileDir, out);
            inStream = new ByteArrayInputStream(out.toByteArray());
            
//            Path p = Paths.get(append + fileDir);
//            String fileName = p.getFileName().toString();
//            Path path = p.getParent().toAbsolutePath();
//            System.out.println("path "+path);
//            System.out.println("filename = "+fileName);
            
//            channelSftp.cd(path.toString());
//            inStream = channelSftp.get(fileName);
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
            initSftp();            
            Debug.printDebug("---Create File fileDir "+append +fileDir);
            channelSftp.put(new FileInputStream(file), append + fileDir);
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
            initSftp();
//           try {
            String toDeletePath = docRepo.getDr_doc_path();
            System.out.println("toDeletePath ");
            copyFile_noClose(docRepo.getDr_doc_path(), newPath);
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
            initSftp();
//           try {
            copyFile_noClose(docRepo.getDr_doc_path(), newPath);
            deleteFile(docRepo.getDr_doc_path());
            if (!Validator.isEmpty(ThumbnailSurfix)) {
                if (isFileExists_noAppend(docRepo.getDr_doc_path()+ThumbnailSurfix, Boolean.FALSE)){
                    copyFile_noClose(docRepo.getDr_doc_path()+ThumbnailSurfix, newPath+ThumbnailSurfix);
                    deleteFile(docRepo.getDr_doc_path()+ThumbnailSurfix);
                }
            }
            docRepo.setDr_doc_temp("N");
            docRepo.setDr_doc_path(newPath);
            session.save(docRepo);
        } catch (Exception e) {
            throw e;
        } finally {
            disconnect();
        }
    }
    public void insertToFileDirectory(String filePath, String fileName, String mime_type, String fileId, BaseDAO dao, String docApplication, Boolean isTemp) throws Exception {
        DrDocRepoModel docRepo = new DrDocRepoModel();
        docRepo.defaultAddProperties();
        docRepo.setDr_doc_id(fileId);
        docRepo.setDr_doc_name(fileName);
        docRepo.setMime_type(mime_type);
        docRepo.setDr_doc_path(filePath);
        System.out.println("docApplication = " + docApplication);
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
            deleteFile(filePath);
            throw new CustomBaseException("DB Error : " + e.getCause());
        }
    }
    
    public Boolean createFile(String fileDir, InputStream file, String fileName, String[] extWhiteList) throws Exception {
        checkExtension(fileName, extWhiteList);
        return createFile(fileDir, file);
    }
    public Boolean createFile(String fileDir, InputStream file) throws Exception {
        try {
            initSftp();
//            System.out.println("---Create File fileDir "+append +fileDir);
            //client.storeFile(fileDir, file);
            channelSftp.put(file, append + fileDir);
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
            initSftp();
//            if (isFileExists(append + fileDir, Boolean.FALSE)){
            if (isFileExists_noAppend(fileDir, Boolean.FALSE)){
                Debug.printInfo("file exist");
                if (!Validator.isEmpty(ThumbnailSurfix)) {
                    if (isFileExists_noAppend(fileDir+ThumbnailSurfix, Boolean.FALSE)){
                        channelSftp.rm(fileDir+ThumbnailSurfix);
                    }
                }
                channelSftp.rm(fileDir);
                return Boolean.TRUE;
                
            }else{
                Debug.printDebug("---File not exist = "+fileDir);
            }
//            System.out.println("file avaialble" + channelSftp.get(append + fileDir).available());
//            if (channelSftp.get(append + fileDir).available() > 0) {
//                channelSftp.rm(append + fileDir);
//            }
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return Boolean.FALSE;
    }
    public Boolean deleteFile(String fileDir) throws Exception {
        try {
            initSftp();
//            if (isFileExists(append + fileDir, Boolean.FALSE)){
            if (isFileExists(fileDir, Boolean.FALSE)){
                Debug.printInfo("file exist");
                channelSftp.rm(append + fileDir);
                if (!Validator.isEmpty(ThumbnailSurfix)) {
                    if (isFileExists_noAppend(append + fileDir+ThumbnailSurfix, Boolean.FALSE)){
                        channelSftp.rm(append + fileDir+ThumbnailSurfix);
                    }
                }
                return Boolean.TRUE;
                
            }else{
                Debug.printDebug("---File not exist = "+append+fileDir);
            }
//            System.out.println("file avaialble" + channelSftp.get(append + fileDir).available());
//            if (channelSftp.get(append + fileDir).available() > 0) {
//                channelSftp.rm(append + fileDir);
//            }
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return Boolean.FALSE;
    }
    public Boolean deleteFolder(String fileDir) throws Exception {
        try {
            initSftp();
            if (isFileExists(fileDir, Boolean.FALSE)){
                //System.out.println("file exist");
                channelSftp.rmdir(append + fileDir);
                return Boolean.TRUE;
                
            }else{
                Debug.printDebug("---File not exist = "+append+fileDir);
            }
//            System.out.println("file avaialble" + channelSftp.get(append + fileDir).available());
//            if (channelSftp.get(append + fileDir).available() > 0) {
//                channelSftp.rm(append + fileDir);
//            }
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return Boolean.FALSE;
    }

    public Boolean renameFile(String fileDir, String newFileDir) throws Exception {
        try {
            initSftp();
            channelSftp.rename(append + fileDir, newFileDir);
            return Boolean.TRUE;
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return Boolean.FALSE;
    }

    public Boolean isDirExists(String fileDir, Boolean closeSftp) throws Exception {
        try {
            initSftp();
            SftpATTRS attrs = channelSftp.lstat(fileDir);
            
            if (attrs != null){
                //System.out.println("dir exist");
                return Boolean.TRUE;
            }
//            if (channelSftp.get(append + fileDir).available() > 1) {
//                return Boolean.TRUE;
//            }
        } catch (Exception e) {
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
            Debug.printDebug("---createdirifnotexist = " + append + fileDir);
            initSftp();
            String appendFileDir = append + fileDir;
            Path p = Paths.get(appendFileDir);
            String fileName = p.getFileName().toString();
            Path path = p.getParent().toAbsolutePath();
//            System.out.println("path "+path);
//            System.out.println("filename = "+fileName);
            //appendFileDir = path.toString();
//            System.out.println("--file path " +appendFileDir);
            
            
            if (!isDirExists(appendFileDir, Boolean.FALSE)) {
//                System.out.println("---create dir here");                
                channelSftp.mkdir(appendFileDir);
//                channelSftp.cd(appendFileDir);
//                String[] arrDirectory = appendFileDir.split("/");
//                String strCurDir = "";
//                boolean isFirst = true;
//                for (int a = 0; a < arrDirectory.length; a++) {
//                    if (!arrDirectory[a].equals("")) {
//                        strCurDir = isFirst ? "/" + arrDirectory[a] : arrDirectory[a];
//                        try {
//                            channelSftp.cd(strCurDir);  //System.out.println("Checking if "+strCurDir+" folder exist.");
//                        } catch (SftpException e) {
//                            // unable to access the folder means the folder not exist, so create it
//                            channelSftp.mkdir(strCurDir); //System.out.println(strCurDir+" folder do not exist, create it.");
//                            channelSftp.cd(strCurDir);    //System.out.println("Opening "+strCurDir+" folder");
//                        }
//                        isFirst = false;
//                    }
//                }
                //System.out.println("---folder created");
//                if (! makeDirectory(fileDir)) {
//                    // ThoTH @ 4-Apr-2014 :: Support Sub-Directories
//                    int intOccur = 0;
//                    int intPost = CommonFunction.nthOccurrence(fileDir, File.separator, intOccur);
//                    String strTemp = fileDir.substring(0, intPost);
//                    while (intPost >=0  && intOccur < 10) {  // intOccur < 10 is to avoid endless Loop
//                        // Check Directory here
//                        if (! isDirExists(strTemp, Boolean.FALSE)) {
//                            // Make Directory
//                            if (! makeDirectory(strTemp)) throw new CustomBaseException("Create Directory Failed...");
//                        }
//
//                        intOccur ++;
//                        intPost = CommonFunction.nthOccurrence(fileDir, "\\", intOccur);
//                        if (intPost >= 0)   strTemp = fileDir.substring(0, intPost);
//                    }
//
//                }

                return Boolean.FALSE;
            }

            return Boolean.TRUE;

        } catch (Exception e) {
            throw e;
//            e.printStackTrace();
        } finally {
            if (closeAfterUse) {
                disconnect();
            }
        }
//        return Boolean.FALSE;
    }

    public Boolean copyFile(String fileDir, String targetFileDir) throws Exception {
//        System.out.println("-------------------------------------------");
//        System.out.println("1. doing copyFile:: fileDir = " + append + fileDir.trim());
//        System.out.println("2. doing copyFile:: targetFileDir = " + append + targetFileDir.trim());
//        System.out.println("-------------------------------------------");
        try {
            initSftp();
//           try {
            if (isFileExists(targetFileDir.trim(), Boolean.FALSE)) {
                channelSftp.rm(append + targetFileDir.trim());
            } else {
                Debug.printDebug("---file not exist");
            }
//               List<SftpFile> sftpFileList = client.ls(targetFileDir);
//               System.out.println("sftp file list size = "+sftpFileList.size());
//               if (sftpFileList.size() > 0) {
//                   client.rm(targetFileDir);
//               }
//           } catch (Exception e) {CommonFunction.exceptionStackTrace(e);}

            channelSftp.rename(append + fileDir.trim(), append + targetFileDir.trim());
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

        InputStream inStream = null;
        try {
            initSftp();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            channelSftp.get(append + fileDir, out);
            inStream = new ByteArrayInputStream(out.toByteArray());
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
            initSftp();
//           try {
            if (isFileExists(targetFileDir.trim(), Boolean.FALSE)) {
                channelSftp.rm(append + targetFileDir.trim());
            } else {
//                System.out.println("---file not exist");
            }
//               List<SftpFile> sftpFileList = client.ls(targetFileDir);
//               System.out.println("sftp file list size = "+sftpFileList.size());
//               if (sftpFileList.size() > 0) {
//                   client.rm(targetFileDir);
//               }
//           } catch (Exception e) {CommonFunction.exceptionStackTrace(e);}
            Debug.printDebug("aaaaaaaaa:"+getFile_noClose(fileDir));
            Debug.printDebug("bbbbbbb :"+append + targetFileDir);
            
            channelSftp.put(getFile_noClose(fileDir), append + targetFileDir);
//            client.put(new FileInputStream(file), fileDir);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
//            CommonFunction.exceptionStackTrace(e);
            throw e;
        } finally {
            
        }
//        return Boolean.FALSE;
    }
    
    public void tryToConnect() {
        try {
//            System.out.println("start : " + DateUtil.getCurrentTimestamp());
            initSftp();
//            System.out.println("end : " + DateUtil.getCurrentTimestamp());
        } catch (Exception e) {
        }
    }
    public Boolean isConnected() {
        try {
            return session.isConnected();
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
    public static Long fileCount = 0L;
    public static Long fileSize = 0L;
    public void list(String path) throws Exception {
        initSftp();
        Vector<ChannelSftp.LsEntry> vector = channelSftp.ls(path);
        int size = vector.size();
        if (size == 1) { //is a file
//            channelSftp.rm(path);
            ChannelSftp.LsEntry entry = vector.get(0);
            fileCount++;
            fileSize = fileSize + entry.getAttrs().getSize();
//            System.out.println("File : " + path + ", size="+entry.getAttrs().getSize());
        } else if (size == 2) { //empty dir
//            channelSftp.rmdir(path);
//            System.out.println("Empty Dir : " + path);
        } else {
            String fileName = null;
            for (ChannelSftp.LsEntry en : vector) {
                fileName = en.getFilename();
                if (!".".equals(fileName) && !"..".equals(fileName)) {
                    list(path + (path.endsWith("/")?"":"/") + fileName);
                }
            }
        }
    }
}
