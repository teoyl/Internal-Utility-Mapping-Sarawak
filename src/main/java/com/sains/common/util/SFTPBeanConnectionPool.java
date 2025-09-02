package com.sains.common.util;

import com.SysConf;
import java.io.*;
import com.jcraft.jsch.*;
import static com.sains.common.util.FtpsUtil.str;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Stream;
//import hrpay_bean.UtilBean;

public class SFTPBeanConnectionPool {

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
            session.setPassword(str(FTP_PASSWORD));
//            session.setPassword((FTP_PASSWORD));
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("kex","diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256");
            session.setConfig(config);
//            session.setTimeout(20000); // if no set timeout, will try for 20 seconds if server down/not available
            session.connect(20000);
//            Thread.sleep(1000);
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
    private synchronized Map initSftp() throws Exception {
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
        Map map = new HashMap();
        map.put("session", session);
        map.put("channel", channel);
        map.put("channelSftp", channelSftp);
        session = null;
        channel = null;
        channelSftp = null;
        return map;
    }

//    public synchronized void disconnect() throws Exception {
//        pushConnection(session, channel, channelSftp);
//    }
    private static final int MaxConn = 10;
    private static int totalConn = 0;
    private static Stack<Map> connectionPool;
    public synchronized Map getConnection() throws Exception {
        if (connectionPool == null) {
            connectionPool = new Stack();
        }
//        System.out.println("getConnectionPool.size = " + connectionPool.size());
        if (connectionPool.size() > 0) {
            Map map = (Map)connectionPool.pop();
            if (!((ChannelSftp)map.get("channelSftp")).isConnected()) { //if the connection broken
                map = initSftp();
            }
            return map;
        } else {
            if (totalConn < MaxConn) { 
                totalConn++;
                return initSftp();
            }
        }
        return null;
    }
    
    public synchronized void closeConnection(Session session, Channel channel, ChannelSftp channelSftp) {
        if (connectionPool == null) {
            connectionPool = new Stack();
        }
//        System.out.println("pushConnectionPool.size = " + connectionPool.size());
        Map map = new HashMap();
        map.put("session", session);
        map.put("channel", channel);
        map.put("channelSftp", channelSftp);
        connectionPool.push(map);
    }
}
