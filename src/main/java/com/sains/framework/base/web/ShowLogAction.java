/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sains.framework.base.web;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.LogFunction;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author ThoTH @ 8-Apr-2014
 */
public class ShowLogAction extends BaseActionSupport {

    @Override
    public String processInsert() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String processUpdate() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public class LogFileModel {
        private String fileName = "";
        private Long fileSize;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Long getFileSize() {
            return fileSize;
        }
        
        public String getFileSize_kb() {
            return new DecimalFormat("###,##0").format(fileSize);
        }

        public void setFileSize(Long fileSize) {
            this.fileSize = fileSize;
        }
    }
    
    
    private List<LogFileModel> fileList = null;
    private String contentType = "";
    private InputStream inputStream = null;
    private String contentDisposition = "";
    
    
    public String loadEditPage() {
        setPageTitle_("Log File Console");
        setPageSubTitle_("Manage");
        LogFileModel logFile = null;
        fileList = new ArrayList();

        File folder = new File(SystemConstants.DOMAIN.logPath);
        File[] listOfFiles = folder.listFiles();
       
        if(listOfFiles != null){
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    logFile = new LogFileModel();
                    logFile.setFileName(listOfFiles[i].getName());
                    logFile.setFileSize(listOfFiles[i].length());
                    fileList.add(logFile);
                }
            }
        }
        
        return SUCCESS;
    }
    
    public String downloadFile() {
        try {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);

            String fileName = request.getParameter("fileName_");

            // Get Directory
            String filePath = SystemConstants.DOMAIN.logPath + SystemConstants.DOMAIN.fileSeperator;

            setContentType("application/" + CommonFunction.getFile_extension(fileName));
            setContentDisposition("filename=\"" + fileName + "\"");
            String outFileName = filePath + fileName;

            setInputStream(new FileInputStream(outFileName));
            
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
            addActionError("File not found.");
            return "";
        }

        return "download_file";
    }
    

    public List<LogFileModel> getFileList() throws Exception {
        return fileList;
    }

    public void setFileList(List<LogFileModel> fileList) {
        this.fileList = fileList;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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
    
    
}
