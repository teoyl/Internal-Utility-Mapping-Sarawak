/* 
 * Copyright � 2007 SAINS.  All rights reserved.
 * Wisma Teo Say Ho, Lot 369, Blk 10, KCLD, Jln Tun Ahmad Zaidi Adruce, 93150, Kch, Swak, Msia.
 * 
 * PROJECT: JSP.jpr
 * PACKAGE/FILE NAME: Bean_ICTU/ClsMail.java
 * DESCRIPTION: Mail class.
 * 
 * DEVELOPMENT AND MODIFICATION HISTORY:
 * 
 * Name         Date        Version   Description
 * ----------------------------------------------
 * Tan Ann Nie  11/06/2007  1.0.0     Development
 * 
 */

package com.sains.common.util;

import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.model.DrDocRepoModel;
import com.sains.framework.model.EmailQueueAttachmentModel;
import java.io.File;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import java.util.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import org.apache.commons.io.IOUtils;

public class ClsMail {
  public ClsMail() { }
  
  //info abt user and mail servers
  private String strUserName;
  private String strPassword;
  private String strOutMailServer;
  private String strInMailServer;
  private boolean blLoginSuccess = false;
  private String strSendFrom; //added by TANAN 11-03-2004
  
  //data representing the user's session
  private java.util.Properties properties;
  private javax.mail.Session session;
  
  //data representing the mail store, folders and message being read
  private javax.mail.Store store;
  private javax.mail.Folder folder;
  private javax.mail.Message[] messages;
  private javax.mail.internet.MimeMessage currentMessage;
  private javax.mail.Transport transport;
  
  //data representing message abt to be sent
  private javax.mail.internet.MimeMessage newMessage;
  private String to;
  private String cc;
  private String bcc;
  private String subject;
  private String body;

  //wongkk
  //private String attfilename;
//  private List attfilename = null;
  private List<File> attTempFileList = null;  // YongLai @ 20-06-2024 : Copied from SSTS's Code : use TempFile to avoid 2 ppl attach file with same name at the same time.
  private List<String> attFileNameList = null;  // YongLai @ 20-06-2024 : Copied from SSTS's Code




  
  private String mailType;  // added by TTH @ 11-1-2010: T=Text; H=HTML;

  public void login() throws NoSuchProviderException, MessagingException {
    //start session
    properties = System.getProperties();
    session = Session.getInstance(properties, null);
    
    //connect to store
    store = session.getStore("imap"); //throws NoSuchProviderException
    store.connect(strInMailServer, strUserName, strPassword); //throws MessagingException
    this.setLoginSuccess(true); //used by jsp the page
  }
  
  public void openInbox() throws MessagingException {
    //open the inbox
    folder = store.getFolder("inbox");
    folder.open(Folder.READ_ONLY); //throws messaging exception.
    
    //get and list the messages
    messages = folder.getMessages();
  }
  
  public void readMsg(String s) throws MessagingException, NumberFormatException {
    int intMsgNo = Integer.parseInt(s);
    currentMessage = (MimeMessage)this.messages[intMsgNo];
  }

  public void signout() throws MessagingException {
    folder.close(false);
    store.close();
  }
  
  public void sendMsg() throws AddressException, SendFailedException, MessagingException {
    properties = System.getProperties();
    properties.put("mail.smtp.host", strOutMailServer);
    properties.put("mail.smtp.localhost", strOutMailServer);  // ThoTH @ 1-Apr-2015 :: For Live server
    session = Session.getInstance(properties, null);

    //fill in the message headers
    newMessage = new MimeMessage(session);
    //String strFrom = new String(strUserName + "@" + strInMailServer.substring(5));
    String strFrom = new String(strSendFrom); //added by TANAN 11-03-2004
    newMessage.setFrom(new InternetAddress(strFrom));
    newMessage.setSubject(subject);


    //added wongkk @ 10/08/2010, to allow single attachment
    // create and fill the first message part
    MimeBodyPart mbp1 = new MimeBodyPart();
    // Added by TTH @ 11-1-2010, to cater HTML content
    if (mailType.equals("H")) {
      mbp1.setContent(body, "text/html");
      //newMessage.setContent(body, "text/html");
    } else {
      mbp1.setText(body);
      //newMessage.setText(body);
    }
    // create the Multipart and add email body to it
    Multipart mp = new MimeMultipart();
    mp.addBodyPart(mbp1);

    //attachment included
//    if(attfilename != null && attfilename.size() > 0){
//        //int liCount = 0;
//        for (int i=0;i< attfilename.size();i++){
//            // create the second message part
//            MimeBodyPart mbp2 = new MimeBodyPart();
//            // attach the file to the message
//            FileDataSource fds = new FileDataSource(attfilename.get(i).toString() );
//            mbp2.setDataHandler(new DataHandler(fds));
//            mbp2.setFileName(fds.getName());
//            //add email attachement(s) to it
//            mp.addBodyPart(mbp2);
//        }
//    }

    // YongLai @ 20-06-2024 : Copied from SSTS's Code
    if(attFileNameList != null && attFileNameList.size() > 0){
        //int liCount = 0;
//        for (String s : attFileNameList){
        for (int i=0;i< attFileNameList.size();i++){
            // create the second message part
            MimeBodyPart mbp2 = new MimeBodyPart();
            // attach the file to the message
            FileDataSource fds = new FileDataSource(attTempFileList.get(i));
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(attFileNameList.get(i));  // ThoTH @ 13-Dec-2018
//            mbp2.setFileName(fds.getName());
            //add email attachement(s) to it
            mp.addBodyPart(mbp2);
        }
    }
    
    newMessage.setContent(mp);



    Address[] toAddresses = InternetAddress.parse(to);
    newMessage.setRecipients(Message.RecipientType.TO, toAddresses);

    Address[] ccAddresses = InternetAddress.parse(cc);
    newMessage.setRecipients(Message.RecipientType.CC, ccAddresses);

    Address[] bccAddresses = InternetAddress.parse(bcc);
    newMessage.setRecipients(Message.RecipientType.BCC, bccAddresses);

    //connect to the transport (if required, insert your strUserName and strPassword)
    Transport transport = session.getTransport("smtp");
    transport.connect(strOutMailServer, "", "");

    //send the message
    transport.sendMessage(newMessage, newMessage.getAllRecipients());
    transport.close();
  }
  public void sendMail() throws Exception {
    properties = System.getProperties();
    properties.put("mail.smtp.host", strOutMailServer);
    properties.put("mail.smtp.localhost", strOutMailServer);  // ThoTH @ 1-Apr-2015 :: For Live server
    session = Session.getInstance(properties, null);

    //fill in the message headers
    newMessage = new MimeMessage(session);
    //String strFrom = new String(strUserName + "@" + strInMailServer.substring(5));
    String strFrom = new String(strSendFrom); //added by TANAN 11-03-2004
    newMessage.setFrom(new InternetAddress(strFrom));
    newMessage.setSubject(subject);


    //added wongkk @ 10/08/2010, to allow single attachment
    // create and fill the first message part
    MimeBodyPart mbp1 = new MimeBodyPart();
    // Added by TTH @ 11-1-2010, to cater HTML content
    if (mailType.equals("H")) {
      mbp1.setContent(body, "text/html");
      //newMessage.setContent(body, "text/html");
    } else {
      mbp1.setText(body);
      //newMessage.setText(body);
    }
    // create the Multipart and add email body to it
    Multipart mp = new MimeMultipart();
    mp.addBodyPart(mbp1);

    //attachment included
//    if(attfilename != null && attfilename.size() > 0){
//        SFTPBean sftp = new SFTPBean();
//        for (EmailQueueAttachmentModel attc : (List<EmailQueueAttachmentModel>)attfilename) {
////            InternetHeaders headers = new InternetHeaders();
////            headers.addHeader("Content-Type", attc.getDrDocRepoModel().getMime_type());
////            MimeBodyPart mbp2 = new MimeBodyPart(headers, IOUtils.toByteArray(sftp.getFile(attc.getDrDocRepoModel().getDr_doc_path())));
//////            MimeBodyPart mbp2 = new MimeBodyPart();
//////            mbp2.setContent(sftp.getFile(attc.getDrDocRepoModel().getDr_doc_path()), attc.getDrDocRepoModel().getMime_type());
//////            MimeBodyPart mbp2 = new MimeBodyPart(sftp.getFile(attc.getDrDocRepoModel().getDr_doc_path()));
////            mbp2.setFileName(attc.getDrDocRepoModel().getDr_doc_name());
////            mp.addBodyPart(mbp2);
//
//            MimeBodyPart attachment= new MimeBodyPart();
//            ByteArrayDataSource ds = new ByteArrayDataSource(sftp.getFile(attc.getDrDocRepoModel().getDr_doc_path()), attc.getDrDocRepoModel().getMime_type()); 
//            attachment.setDataHandler(new DataHandler(ds));
//            attachment.setFileName(attc.getDrDocRepoModel().getDr_doc_name());
//            mp.addBodyPart(attachment);
//        }
//    }

    newMessage.setContent(mp);



    Address[] toAddresses = InternetAddress.parse(to);
    newMessage.setRecipients(Message.RecipientType.TO, toAddresses);

    Address[] ccAddresses = InternetAddress.parse(cc);
    newMessage.setRecipients(Message.RecipientType.CC, ccAddresses);

    Address[] bccAddresses = InternetAddress.parse(bcc);
    newMessage.setRecipients(Message.RecipientType.BCC, bccAddresses);

    //connect to the transport (if required, insert your strUserName and strPassword)
    Transport transport = session.getTransport("smtp");
    transport.connect(strOutMailServer, "", "");

    //send the message
    transport.sendMessage(newMessage, newMessage.getAllRecipients());
    transport.close();
  }
  
  //added by TANAN 11-03-2004
  public void setSendFrom(String s) { strSendFrom = s; }
  public String getSendFrom() { return strSendFrom; }

  public void setUserName(String s) { strUserName = s; }
  public String getUserName() { return strUserName; }

  public void setPassword(String s) { strPassword = s; }
  public String getPassword() { return strPassword; }

  public void setOutMailServer(String s) { strOutMailServer = s; }
  public String getOutMailServer() { return strOutMailServer; }

  public void setInMailServer(String s) { strInMailServer = s; }
  public String getInMailServer() { return strInMailServer; }

  public void setLoginSuccess(boolean b) { blLoginSuccess = b; }
  public boolean getLoginSuccess() { return blLoginSuccess; }

  public void setMessages(Message[] msg) { messages = msg; }
  public Message[] getMessages() { return messages; }

  public void setCurrentMessage(MimeMessage msg) { currentMessage = msg; }
  public MimeMessage getCurrentMessage() { return currentMessage; }

  public void setNewMessage(MimeMessage msg) { newMessage = msg; }
  public MimeMessage getNewMessage() { return newMessage; }

  public void setTo(String s) { to = s; }
  public String getTo() { return to; }

  public void setCc(String s) { cc = s; }
  public String getCc() { return cc; }

  public void setBcc(String s) { bcc = s; }
  public String getBcc() { return bcc; }

  public void setSubject(String s) { subject = s; }
  public String getSubject() { return subject; }

  public void setBody(String s) { body = s; }
  public String getBody() { return body; }
  
  public void setMailType(String s) {mailType = s;}


//  public List getAttfilename() {
//        return attfilename;
//  }
//
//  public void setAttfilename(List attfilename) {
//    this.attfilename = attfilename;
//  }
  
  public List<File> getAttTempFileList() {return attTempFileList;}
  public void setAttTempFileList(List<File> attTempFileList) {this.attTempFileList = attTempFileList;}

  public List<String> getAttFileNameList() {return attFileNameList;}
  public void setAttFileNameList(List<String> attFileNameList) {this.attFileNameList = attFileNameList;}
}
