package com.sains.common.util;

import com.SysConf;
import com.sains.framework.base.Debug;

public class FileOperationUtil {
    public static FtpInterface getFtpInterface() {
        Debug.printFrameworkDebug("SysConf.get(\"fileOperation\").toLowerCase() = " + SysConf.get("fileOperation").toLowerCase());
        switch (SysConf.get("fileOperation").toLowerCase()) {
            case "obs": 
                return new OBSUtil();
            case "sftp":
                return new SFTPBean();
            default: //"ftps"
                return new Sftp2Util();
        }
    }
    public static FtpInterface getFtpInterface(String type) {
        switch (type) {
            case "obs": 
                return new OBSUtil();
            case "sftp":
                return new SFTPBean();
            default: //"ftps"
                return new Sftp2Util();
        }
    }
}
