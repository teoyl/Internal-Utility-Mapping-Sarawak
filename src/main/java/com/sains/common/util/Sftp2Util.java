package com.sains.common.util;

import com.SysConf;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import static com.sains.common.util.FtpsUtil.str;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.model.DrDocRepoModel;
import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Sftp2Util extends FtpInterface {

    private Boolean init = Boolean.FALSE;
    private SshClient ssh = null;
    private SftpClient client;
    private static final String append = SysConf.get("pathPrefix");
//    private static final String append = "/home/eqpdev/sftp/";
    Channel channel = null;
    ChannelSftp channelSftp = null;

    private void connectSSH() throws Exception {
        ssh = new SshClient();
//        String host = "sftp.eqp-kpps.sarawak.gov.my"; //LIVE
        //Planning War Room
//        String host = "172.19.107.13";
//        String user = "espa";
//        String pass = "password";
//        Integer port=22;
        
        //Development
        String host = SysConf.get("fs.url");
        String user = SysConf.get("fs.user");
        String pass = SysConf.get("fs.pass");
        int port = Integer.parseInt(SysConf.get("fs.port"));
        
       //Training Server 
//       String host = "10.17.101.4";
//       String user = "root";
//       String pass = "ASDF1234";
//       Integer port=22;
        
//        String host = "172.22.51.202"; 
        ssh.connect(host, port, new IgnoreHostKeyVerification());
        //Authenticate
        PasswordAuthenticationClient passwordAuthenticationClient = new PasswordAuthenticationClient();
        passwordAuthenticationClient.setUsername(user);//LIVE
        passwordAuthenticationClient.setPassword(str(pass));//LIVE
//        passwordAuthenticationClient.setPassword(pass);//TRAINING SERVER
        int result = ssh.authenticate(passwordAuthenticationClient);
        if (result != AuthenticationProtocolState.COMPLETE) {
            Debug.printDebug("SFTP Login to " + host + ":" + port + " failed");
        } else {
            //System.out.println("SFTP Login success");
        }

    }

    private void initSftp() throws Exception {
        if (ssh == null || !ssh.isConnected()) {
            connectSSH();
        }
        //Open the SFTP channel
        if (client == null || client.isClosed()) {
//            System.out.println("open................");
            client = ssh.openSftpClient();
            
            
        }
        init = Boolean.TRUE;
    }

    public void disconnect() throws Exception {
        if (client != null) {
            client.quit();
            client = null;
        }
        if (ssh != null) {
            ssh.disconnect();
            ssh = null;
        }
        init = Boolean.FALSE;
    }

    public Boolean isFileExists(String fileDir, Boolean closeSftp) throws Exception {
        try {
            initSftp();
//            System.out.println("isFileExist = " + append + fileDir);
            return client.get(append + fileDir).isFile();
            
        } catch (Exception e) {
//            CommonFunction.exceptionStackTrace(e);
        } finally {
            if (closeSftp) {
                disconnect();
            }
        }
        return Boolean.FALSE;
    }

    public InputStream getFile_noClose(String fileDir) throws Exception {

        //System.out.println("---Get File fileDir "+append + fileDir);
        InputStream inStream = null;
        try {
            initSftp();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            client.get(append + fileDir, out);
            inStream = new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
        }
        return inStream;
    }
    public InputStream getFile(String fileDir) throws Exception {

        //System.out.println("---Get File fileDir "+append + fileDir);
        InputStream inStream = null;
        try {
            initSftp();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            client.get(append + fileDir, out);
            inStream = new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return inStream;
    }
    public InputStream getFile_noAppend(String fileDir) throws Exception {

        //System.out.println("---Get File fileDir "+append + fileDir);
        InputStream inStream = null;
        try {
            initSftp();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            client.get(fileDir, out);
            inStream = new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
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
            //System.out.println("---Create File fileDir "+append +fileDir);
            client.put(new FileInputStream(file), append + fileDir);
            return Boolean.TRUE;
        } catch (Exception e) {
            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return Boolean.FALSE;
    }

    public Boolean createFile(String fileDir, InputStream file, String fileName, String[] extWhiteList) throws Exception {
        checkExtension(fileName, extWhiteList);
        return createFile(fileDir, file);
    }
    public Boolean createFile(String fileDir, InputStream file) throws Exception {
        try {
            initSftp();
            //System.out.println("---Create File fileDir "+append +fileDir);
            //client.storeFile(fileDir, file);
            client.put(file, append + fileDir);
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
            initSftp();
            if (client.get(append + fileDir).isFile()) {
                client.rm(append + fileDir);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            Debug.printError("Delete File " + e.getMessage());
            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return Boolean.FALSE;
    }
    
    public Boolean deleteFolder(String fileDir) throws Exception {
        try {
            initSftp();
            if (client.get(append + fileDir).isDirectory()) {
                client.rm(append + fileDir, Boolean.TRUE, Boolean.TRUE);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            Debug.printError("Delete Folder " + e.getMessage());
            CommonFunction.exceptionStackTrace(e);
        } finally {
            disconnect();
        }
        return Boolean.FALSE;
    }

    public Boolean renameFile(String fileDir, String newFileDir) throws Exception {
        try {
            initSftp();
            client.rename(append + fileDir, newFileDir);
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
//            System.out.println("Folder Size " + client.get(append + fileDir).getSize());
            return client.get(append + fileDir).isDirectory();
        } catch (Exception e) {
//            System.out.println("Error " + e.getMessage() );
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
            
            //System.out.println("---createdirifnotexist = " + append + fileDir);
            initSftp();
            if (!isDirExists( fileDir, Boolean.FALSE)) {
                client.mkdirs(append + fileDir);
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
                client.rm(append + targetFileDir.trim());
            } else {
                Debug.printDebug("---file not exist");
            }
//               List<SftpFile> sftpFileList = client.ls(targetFileDir);
//               System.out.println("sftp file list size = "+sftpFileList.size());
//               if (sftpFileList.size() > 0) {
//                   client.rm(targetFileDir);
//               }
//           } catch (Exception e) {CommonFunction.exceptionStackTrace(e);}

            client.rename(append + fileDir.trim(), append + targetFileDir.trim());
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
    public Boolean copyFile_noClose(String fileDir, String targetFileDir) throws Exception {
//        System.out.println("-------------------------------------------");
//        System.out.println("1. doing copyFile:: fileDir = " + append + fileDir.trim());
//        System.out.println("2. doing copyFile:: targetFileDir = " + append + targetFileDir.trim());
//        System.out.println("-------------------------------------------");
        try {
            initSftp();
//           try {
            if (isFileExists(targetFileDir.trim(), Boolean.FALSE)) {
                client.rm(append + targetFileDir.trim());
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
            
            client.put(getFile_noClose(fileDir), append + targetFileDir);
//            client.put(new FileInputStream(file), fileDir);
            return Boolean.TRUE;
        } catch (Exception e) {
//            CommonFunction.exceptionStackTrace(e);
            throw e;
        } finally {
            
        }
//        return Boolean.FALSE;
    }

    public Boolean makeDirectory(String fileDir) {
        try {
            client.mkdir(fileDir);
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**wongkk@28Nov2017
     * @param fileDir - path for File or Directory
     * @param force = true delete files in the folder and folder itself only
     * @param recurse = true delete everything including sub folder under the folder
     * deleteFileOrDir(folderFilePath,false,false) - Delete file
     * deleteFileOrDir(folderFilePath,true,false) - Delete folder and files(if any)
     * deleteFileOrDir(folderFilePath,true,true) - Delete folder and files and sub folder
    */
    public Boolean deleteFileOrDir(String fileDir ,boolean force, boolean recurse)throws Exception {
        try {
            initSftp();
            client.rm(append + fileDir,force,recurse);
           
        } catch (Exception e) {
//            System.out.println(fileDir + e.getMessage());
            return Boolean.FALSE;
        } finally {
            disconnect();
        }
        return Boolean.TRUE;
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
