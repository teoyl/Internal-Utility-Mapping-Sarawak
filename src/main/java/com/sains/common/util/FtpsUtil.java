package com.sains.common.util;

import com.SysConf;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.model.DrDocRepoModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * A program demonstrates how to upload files from local computer to a remote
 * FTP server using Apache Commons Net API.
 * @author www.codejava.net
 */
public class FtpsUtil extends FtpInterface {
//    private static Boolean init = Boolean.FALSE;
    private FTPClient client = null; // ThoTH @ 8-May2-2014 :: DC said they cannot setup the FTPS, so hv to use FTP
//    private FTPSClient client = null;
    public Boolean testFTPConnection() throws Exception {
        initFtps();
        return client.isConnected();
    }
    private void initFtps() throws Exception {
        if (client != null && client.isConnected()) return; //isConnected() condition added by Zhafari @ 01-Apr-2014 - solve for connection closed error
        client = new FTPClient();
//        client = new FTPSClient();
//        client.setAuthValue("SSL");
//        String server = "10.17.100.39";
//        String server = "172.22.30.136";
        String server = SysConf.get("fs.url");
        String user = SysConf.get("fs.user");
        String pass = SysConf.get("fs.pass");
        int port = Integer.parseInt(SysConf.get("fs.port"));
//        String user = "thoth";
//        String pass = "tho123456";
//        String user = "administrator";
//        String pass = "sains@JSO";

        client.setConnectTimeout(3000);
        client.connect(server, port);
        client.login(user, pass);
        client.enterLocalPassiveMode();
//        client.enterLocalActiveMode();
        client.setFileType(FTP.BINARY_FILE_TYPE);
//        client.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
//        init = Boolean.TRUE;
//        client.setTrustManager(TrustManagerUtils.getValidateServerCertificateTrustManager());
//        client.setTrustManager(null);
    }
    public void disconnect() {
        try {
            if (client.isConnected()) {
                client.logout();
                client.disconnect();
            }
//            init = Boolean.FALSE;
        } catch (Exception e){}
    }

    public Boolean isFileExists(String fileDir, Boolean closeAfterUse) {//throws Exception {
        try {
            initFtps();
//            FTPFile[] ftpFile = client.listFiles("//impian2", new FTPFileFilter() {
//
//                @Override
//                public boolean accept(FTPFile ftpf) {
//                    return ftpf.getName().equals("catalina.sh");
//                }
//            });

            FTPFile[] ftpFile = client.listFiles(fileDir);
            return ftpFile.length > 0;

//            String strCurrentDir = client.printWorkingDirectory();
//            Boolean blnSuccess = client.changeWorkingDirectory(fileDir); // Use this to check whether the Dir is Exist
//            client.changeWorkingDirectory(strCurrentDir);  // go back to original
//            return blnSuccess;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
//            CommonFunction.exceptionStackTrace(e);
        } finally {
            if (closeAfterUse) disconnect();
        }
//        return Boolean.FALSE;
    }
    
    // ThoTH @ 7-Apr-2014 :: Copy from isFileExist, only main for Check whether a Dir is Exist
    public Boolean isDirExists(String fileDir, Boolean closeAfterUse) throws Exception {
        try {
            initFtps();
            String strCurrentDir = client.printWorkingDirectory();
            Boolean blnSuccess = client.changeWorkingDirectory(fileDir); // Use this to check whether the Dir is Exist
            client.changeWorkingDirectory(strCurrentDir);  // go back to original
            return blnSuccess;
        } catch (Exception e) {
            throw e;
//            e.printStackTrace();
//            CommonFunction.exceptionStackTrace(e);
        } finally {
            if (closeAfterUse) disconnect();
        }
//        return Boolean.FALSE;
    }

    // ThoTH @ 24-Feb-2014
    public Boolean createDirIfNotExists(String fileDir, Boolean closeAfterUse) throws Exception {
        try {
            initFtps();
            if (! isDirExists(fileDir, Boolean.FALSE)) {
                if (! client.makeDirectory(fileDir)) {
                    // ThoTH @ 4-Apr-2014 :: Support Sub-Directories
                    int intOccur = 0;
                    int intPost = CommonFunction.nthOccurrence(fileDir, "\\", intOccur);
                    String strTemp = fileDir.substring(0, intPost);
                    while (intPost >=0  && intOccur < 10) {  // intOccur < 10 is to avoid endless Loop
                        // Check Directory here
                        if (! isDirExists(strTemp, Boolean.FALSE)) {
                            // Make Directory
                            if (! client.makeDirectory(strTemp)) throw new CustomBaseException("Create Directory Failed...");
                        }

                        intOccur ++;
                        intPost = CommonFunction.nthOccurrence(fileDir, "\\", intOccur);
                        if (intPost >= 0)   strTemp = fileDir.substring(0, intPost);
                    }
                    Debug.printDebug("strTemp == " + strTemp);
                }

                return Boolean.FALSE;
            }

            return Boolean.TRUE;

        } catch (Exception e) {
            throw e;
//            e.printStackTrace();
        } finally {
            if (closeAfterUse) disconnect();
        }
//        return Boolean.FALSE;
    }

    public InputStream getFile(String fileDir) throws Exception {
        InputStream inStream = null;
        try {
            initFtps();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            client.retrieveFile(fileDir, out);
            inStream = new ByteArrayInputStream(out.toByteArray());
            out.close();
        } catch (Exception e) {
//            CommonFunction.exceptionStackTrace(e);
            throw e;
        } finally {
            disconnect();
        }
        return inStream;
    }
    public InputStream getFile_noAppend(String fileDir) throws Exception {
        InputStream inStream = null;
        try {
            initFtps();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            client.retrieveFile(fileDir, out);
            inStream = new ByteArrayInputStream(out.toByteArray());
            out.close();
        } catch (Exception e) {
//            CommonFunction.exceptionStackTrace(e);
            throw e;
        } finally {
            disconnect();
        }
        return inStream;
    }

    public Boolean copyFile(String fileDir, String targetFileDir) throws Exception {
        try {
            initFtps();
//            System.out.println("-------------------------------------------");
//            System.out.println("1. doing copyFile:: fileDir = " + fileDir);
//            System.out.println("2. doing copyFile:: targetFileDir = " + targetFileDir);
//            System.out.println("-------------------------------------------");
            FTPFile[] ftpFile = client.listFiles(targetFileDir);
            if (ftpFile.length > 0) {
                client.deleteFile(targetFileDir);
            }
            client.rename(fileDir, targetFileDir);
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

    public Boolean createFile(String fileDir, File file, String fileName, String[] extWhiteList) throws Exception {
        checkExtension(fileName, extWhiteList);
        return createFile(fileDir, file);
    }
    public Boolean createFile(String fileDir, File file) throws Exception {
        try {
            initFtps();
            client.storeFile(fileDir, new FileInputStream(file));
//            client.put(new FileInputStream(file), fileDir);
            return Boolean.TRUE;
        } catch (Exception e) {
//           CommonFunction.exceptionStackTrace(e);
            throw e;
        } finally {
            disconnect();
        }
//        return Boolean.FALSE;
    }

    public Boolean createFile(String fileDir, InputStream file, String fileName, String[] extWhiteList) throws Exception {
        checkExtension(fileName, extWhiteList);
        return createFile(fileDir, file);
    }
    public Boolean createFile(String fileDir, InputStream file) throws Exception {
        try {
            initFtps();
            client.storeFile(fileDir, file);
            return Boolean.TRUE;
        } catch (Exception e) {
//            CommonFunction.exceptionStackTrace(e);
            throw e;
        } finally {
            disconnect();
        }
//        return Boolean.FALSE;
    }

    public Boolean deleteFile(String fileDir) throws Exception {
        try {
            initFtps();
            client.deleteFile(fileDir); //Zhafari @ 04-Mar-2014 - this line was missing, maybe accidentally removed
            return Boolean.TRUE;
        } catch (Exception e) {
//            CommonFunction.exceptionStackTrace(e);
            throw e;
        } finally {
            disconnect();
        }
//        return Boolean.FALSE;
    }
    
    public Boolean deleteFolder(String fileDir) throws Exception {
        try {
            initFtps();
//            client.deleteFile(fileDir); //Zhafari @ 04-Mar-2014 - this line was missing, maybe accidentally removed
            client.removeDirectory(fileDir);
            return Boolean.TRUE;
        } catch (Exception e) {
//            CommonFunction.exceptionStackTrace(e);
            throw e;
        } finally {
            disconnect();
        }
//        return Boolean.FALSE;
    }
    
    // ThoTH @ 20-Jun-2014
    public Boolean deleteDirectory(String dirPath) {
        try {
            initFtps();
            client.removeDirectory(dirPath); 
            return Boolean.TRUE;
        } catch (Exception e) {
            Debug.printError("!!! Directory Not Exist !!!");  // Purposely don't throw back
//            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return Boolean.FALSE;
    }

    public Boolean renameFile(String fileDir, String newFileDir) throws Exception {
        try {
            initFtps();
//            System.out.println("fileDir : " + fileDir);
//            System.out.println("newFileDir : " + newFileDir);
//            client.enterLocalActiveMode();
            client.setFileType(FTP.ASCII_FILE_TYPE);
            client.rename(fileDir, newFileDir);
            return Boolean.TRUE;
        } catch (Exception e) {
//            e.printStackTrace();
//            CommonFunction.exceptionStackTrace(e);
            throw e;
        } finally {
            disconnect();
        }
//        return Boolean.FALSE;
    }
    
    // ThoTH @ 8-May-2014 :: Copied from SessionFactoryImpl
    // Ahmadni @ 10-Sept-2015 :: Change private to public for cpp report use
    public static String str(String s)
    {
        if (s.startsWith("OBF:"))
            s=s.substring(4);

        byte[] b=new byte[s.length()/2];
        int l=0;
        for (int i=0;i<s.length();i+=4)
        {
            String x=s.substring(i,i+4);
            int i0 = Integer.parseInt(x,36);
            int i1=(i0/256);
            int i2=(i0%256);
            b[l++]=(byte)((i1+i2-254)/2);
        }

        return new String(b,0,l);
    }

//    public void retrieveFile() throws Exception {
//        try {
//
//            initFtps();
//            // APPROACH #1: using retrieveFile(String, OutputStream)
//            String remoteFile1 = "dsLogo.jpg";
//            File downloadFile1 = new File("C:/data/dsLogo.jpg");
//            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
//            boolean success = client.retrieveFile(remoteFile1, outputStream1);
//            outputStream1.close();
//
//            if (success) {
//                System.out.println("File #1 has been downloaded successfully.");
//            }
//
//            // APPROACH #2: using InputStream retrieveFileStream(String)
//            String remoteFile2 = "dsLogo.jpg";
//            File downloadFile2 = new File("C:/data/dsLogo_stream.jpg");
//            OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
//            InputStream inputStream = client.retrieveFileStream(remoteFile2);
//            byte[] bytesArray = new byte[4096];
//            int bytesRead = -1;
//            while ((bytesRead = inputStream.read(bytesArray)) != -1) {
//                outputStream2.write(bytesArray, 0, bytesRead);
//            }
//
//            success = client.completePendingCommand();
//            if (success) {
//                System.out.println("File #2 has been downloaded successfully.");
//            }
//            outputStream2.close();
//            inputStream.close();
//
//        } catch (IOException ex) {
//            System.out.println("Error: " + ex.getMessage());
//            ex.printStackTrace();
//        } finally {
//            disconnect();
//        }
//    }
    
    public OutputStream getOutFile(String fileDir) throws Exception {
        OutputStream inStream = null;
        try {
            initFtps();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            client.retrieveFile(fileDir, out);
            inStream = new ByteArrayOutputStream();
            out.close();
        } catch (Exception e) {
//            CommonFunction.exceptionStackTrace(e);
            throw e;
        } finally {
            disconnect();
        }
        return inStream;
    }

    @Override
    public void insertToFileDirectory(String filePath, String fileName, String mime_type, String fileId, BaseDAO dao, String docApplication, Boolean isTemp) throws Exception {
        DrDocRepoModel docRepo = new DrDocRepoModel();
        docRepo.defaultAddProperties();
        docRepo.setDr_doc_id(fileId);
        docRepo.setDr_doc_name(fileName);
        docRepo.setMime_type(mime_type);
        docRepo.setDr_doc_path(filePath);
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
            dao.rollbackBatchTransaction();
        }
    }
    
    @Override
    public void moveTempFile(String newPath, String drDocId, org.hibernate.Session session) throws Exception {
        throw new Exception("Not implemented yet");
    }
}
