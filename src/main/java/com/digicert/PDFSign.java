package com.digicert;

import digicert.validation.X509DCVerification;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.cert.Certificate;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.sains.common.util.DateUtil;
import com.sains.common.util.FtpInterface;
import com.sains.common.util.FtpsUtil;
import com.sains.common.util.OBSUtil;
import com.sains.common.util.SFTPBean;
import com.sains.common.util.Sftp2Util;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ReportGenerator;
import com.utimaps.web.IssuanceJobAction;
import com.utimaps.web.SubmissionAction;
import com.utimaps.web.UtimapsAction;
import java.awt.Point;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.apache.struts2.ServletActionContext;
import org.joda.time.DateTime;

public class PDFSign {

    //public static final String RESOURCE = "C:\\elasis\\digiCert\\dsLogo.jpg";    

    public PDFSign() {
    }

    /* Return  0 : OK
     *        -1 : Expired
     *        -2 : Revoked
     *        -3 : Not Valid Issuer
     */
    public int selfSignMode(String fileId, String filePath, String fName, File cert, String pswd, String jobId, String strDiv, String status) throws Exception {
        CommonFunction.writeFile("PDFSign", "selfSignMode " +status);
        BaseDAOImpl baseDAO = new BaseDAOImpl();
        IssuanceJobAction issuanceAction = new IssuanceJobAction();
        SubmissionAction submissionAction = new SubmissionAction();
        Integer initError= 0;
        java.util.List<String> uscsFileTypeList = Arrays.asList(UtimapsAction.WF_STATUS.ISSUE_USCS10_PENDING, UtimapsAction.WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING,
                UtimapsAction.WF_STATUS.ISSUE_USCS20_PENDING, UtimapsAction.WF_STATUS.ISSUE_USCS30_PENDING, UtimapsAction.WF_STATUS.ISSUE_USCS40_PENDING,
                UtimapsAction.WF_STATUS.ISSUE_USCS50_PENDING, UtimapsAction.WF_STATUS.ISSUE_USCS60_PENDING, UtimapsAction.WF_STATUS.ISSUE_USCS70_PENDING,
                UtimapsAction.WF_STATUS.ISSUE_USCS80_PENDING, UtimapsAction.WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING);
        try {
            //create Temp file folder to store temp sign pdf and temp stamp pdf, file will be deleted at QpApplicationModel
            String tempDir = "";
            String dsLogoPath = "";
            if (System.getProperty("os.name").startsWith("Windows")) {
                 tempDir = "C:\\lxg\\temp\\";
                 dsLogoPath = "C:\\lxg\\reportImage\\digicert\\dsLogo"+strDiv+".png";
            }else{
                 tempDir = "/home/utilitysurvey-tnt/temp/";
                 dsLogoPath = "/home/utilitysurvey-tnt/reportImage/digicert/dsLogo"+strDiv+".png";
            }
            File dir = null;
            dir = new File(tempDir);
            if (dir.exists() == false) {
                dir.mkdirs();
            }

            String fileSeperator = System.getProperty("file.separator");
            
            //create directory if not exist
            OBSUtil ftp = new OBSUtil();
            Image img = Image.getInstance(dsLogoPath);
                img.scaleAbsolute(67, 60);//new scale round (lebar,tinggi)
            if(status.equals(UtimapsAction.WF_STATUS.ISSUE_SJI_PENDING)){
                img.setAbsolutePosition(430, 90); //(left align,top)
            } else if(uscsFileTypeList.contains(status)){
                img.setAbsolutePosition(270, 235); //(left align,top)
            } else if(status.equals("DSP")){
                img.setAbsolutePosition(1110, 67); //(left align,top)
                img.scaleAbsolute(33, 30);//new scale round (lebar,tinggi)
            } else if(status.equals("MINUTE")){
                img.setAbsolutePosition(270, 225);
            } else if (status.equals("PSDSP")) {
                img.setAbsolutePosition(5, 5); //(left align,top)
                img.scaleAbsolute(0, 0);//new scale round (lebar,tinggi)
            } else{
               img.setAbsolutePosition(5, 5);
            }
            
            PdfReader reader1 = new PdfReader(ftp.getFile(filePath));
             //stamp mrpe logo
            PdfStamper stamp = new PdfStamper(reader1, new FileOutputStream(tempDir + fName.substring(0, fName.length() - 4) + "Stamped.pdf"));
            PdfContentByte under;
            int numPages = reader1.getNumberOfPages();
            int count = 0;
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = sf.format(DateUtil.getCurrentDate());
            float x, y;
            Rectangle pagesize ;
//            while (count < numPages) {
//                count++;
//                under = stamp.getUnderContent(count);
//                under.addImage(img);
//            }
            
            // 20-05_2025 : Stamping at the last page only
            under = stamp.getUnderContent(numPages);
            under.addImage(img);

            stamp.close();
            //*******create stamped file**********************************************
            String fileIdStamped;
            String filePathStamped="";
            String docStamped="";
            String fileType="";
            String fNameStamped = "";
            if(fName.contains("Stamped.pdf")) {
                fNameStamped = fName.substring(0, fName.length() - 10) + "Stamped.pdf";
            } else {
                fNameStamped = fName.substring(0, fName.length() - 4) + "Stamped.pdf";
            }
            File tempStamp = new File(tempDir +fNameStamped );
            if(status.equals(UtimapsAction.WF_STATUS.ISSUE_SJI_PENDING)){ //joveni :: 29/1/2024
                fileIdStamped = com.sains.framework.base.CommonFunction.getId(20);
                filePathStamped = UtimapsAction.FILE_PATH.USJ_LETTER_STAMPED;
                docStamped = UtimapsAction.DOC_APPLICATION.USJ_LETTER_STAMPED;
                
                // 20-05_2025 : No need to insert stamped file
//                initError = issuanceAction.uploadSignedFile(null, fileIdStamped, fNameStamped, filePathStamped, SystemConstants.FILE_TYPE.STSJL, jobId, tempStamp);
            } else if (uscsFileTypeList.contains(status)|| status.equals("DSP") || status.equals("MINUTE")){
                fileIdStamped = com.sains.framework.base.CommonFunction.getId(20);
                if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS10_PENDING)) {
                    filePathStamped = UtimapsAction.FILE_PATH.USCS10_STAMPED;
                    docStamped = UtimapsAction.DOC_APPLICATION.USCS10_STAMPED;
                    fileType = SystemConstants.FILE_TYPE.USCS10;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING)) {
                    filePathStamped = UtimapsAction.FILE_PATH.USCS10H_STAMPED;
                    docStamped = UtimapsAction.DOC_APPLICATION.USCS10H_STAMPED;
                    fileType = SystemConstants.FILE_TYPE.USCS10H;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS20_PENDING)) {
                    filePathStamped = UtimapsAction.FILE_PATH.USJCS20_STAMPED;
                    docStamped = UtimapsAction.DOC_APPLICATION.USJCS20_STAMPED;
                    fileType = SystemConstants.FILE_TYPE.USCS20;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS30_PENDING)) {
                    filePathStamped = UtimapsAction.FILE_PATH.USCS30_STAMPED;
                    docStamped = UtimapsAction.DOC_APPLICATION.USCS30_STAMPED;
                    fileType=SystemConstants.FILE_TYPE.USCS30;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS40_PENDING)) {
                    filePathStamped = UtimapsAction.FILE_PATH.USCS40_STAMPED;
                    docStamped = UtimapsAction.DOC_APPLICATION.USCS40_STAMPED;
                    fileType=SystemConstants.FILE_TYPE.USCS40;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS50_PENDING)) {
                    filePathStamped = UtimapsAction.FILE_PATH.USCS50_STAMPED;
                    docStamped = UtimapsAction.DOC_APPLICATION.USCS50_STAMPED;
                    fileType=SystemConstants.FILE_TYPE.USCS50;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS60_PENDING)) {
                    filePathStamped = UtimapsAction.FILE_PATH.USCS60_STAMPED;
                    docStamped = UtimapsAction.DOC_APPLICATION.USCS60_STAMPED;
                    fileType=SystemConstants.FILE_TYPE.USCS60;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS70_PENDING)) {
                    filePathStamped = UtimapsAction.FILE_PATH.USCS70_STAMPED;
                    docStamped = UtimapsAction.DOC_APPLICATION.USCS70_STAMPED;
                    fileType=SystemConstants.FILE_TYPE.USCS70;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS80_PENDING)) {
                    filePathStamped = UtimapsAction.FILE_PATH.USCS80_STAMPED;
                    docStamped = UtimapsAction.DOC_APPLICATION.USCS80_STAMPED;
                    fileType=SystemConstants.FILE_TYPE.USCS80;
                } else if (status.equals(UtimapsAction.WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING)) {
                    filePathStamped = UtimapsAction.FILE_PATH.USCS90_STAMPED;
                    docStamped = UtimapsAction.DOC_APPLICATION.USCS90_STAMPED;
                    fileType=SystemConstants.FILE_TYPE.USCS90;
                } else if (status.equals("DSP")) {
                    filePathStamped = UtimapsAction.FILE_PATH.SSDSP_STAMPED;
                    docStamped = UtimapsAction.DOC_APPLICATION.SSDSP_STAMPED;
                    fileType=SystemConstants.FILE_TYPE.SSDSP;
                } else if (status.equals("MINUTE")) {
                    filePathStamped = UtimapsAction.FILE_PATH.MINUTE_STAMPED;
                    docStamped = UtimapsAction.DOC_APPLICATION.MINUTE_STAMPED;
                    fileType=SystemConstants.FILE_TYPE.MINUTE;
                }
                // 20-05_2025 : No need to insert stamped file
//                initError = submissionAction.uploadSignedFile(null, fileIdStamped, fNameStamped, filePathStamped, fileType, jobId, tempStamp, docStamped);
            }
            if(initError!=0){
                return initError;
            }
            PdfReader reader = null;
            FileOutputStream fout = null;
            reader = new PdfReader(tempDir + fName.substring(0, fName.length() - 4) + "Stamped.pdf");  // This Temp PDF will be removed in QPApplicationModel.updateSign
            
            //*******create signed file**********************************************
            if(fName.contains("Signed.pdf")) {
                fout = new FileOutputStream(tempDir + fName.substring(0, fName.length() - 10) + "Signed.pdf");
            } else {
                fout = new FileOutputStream(tempDir + fName.substring(0, fName.length() - 4) + "Signed.pdf");
            }
            PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
            PdfSignatureAppearance sap = stp.getSignatureAppearance();
            KeyStore ks = KeyStore.getInstance("pkcs12");
            try {
                if(cert==null){
                    CommonFunction.writeFile("PDFSign", "no valid cert");
                    return -4;
                }
                ks.load(new FileInputStream(cert), pswd.toCharArray());
            } catch (IOException ioe) {
                return -4;
            }
            String alias = (String) ks.aliases().nextElement();
            // DigiCert Verification
            X509Certificate xcert = (X509Certificate) ks.getCertificate(alias);
            X509DCVerification dcCheck = new X509DCVerification(xcert);

            
//            if (dcCheck.isExpired()) {
//                Debug.printDebug("dcCheck.isExpired() " + dcCheck.isExpired());
//                return -1;
//            }

            //Commented after migrate TomCat server : 17-Oct-2017
//            if (dcCheck.isRevoked()) {
//                return -2;
//            }
            
            //@13.3.2013 New Cert Got Problem. Temporary disabled.
//            if (!dcCheck.isValidIssuer()) {
//                return -3;
//            }

            // Start Signing
            PrivateKey key = (PrivateKey) ks.getKey(alias, pswd.toCharArray());
            Certificate[] chain = (Certificate[]) ks.getCertificateChain(alias);
            sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);

            if(uscsFileTypeList.contains(status)){
                sap.setVisibleSignature(new Rectangle(210, 290, 480, 200), reader.getNumberOfPages(), null);
            } else if (status.equals("PSDSP")){
                    sap.setVisibleSignature(new Rectangle(1062, 178, 1180, 143), reader.getNumberOfPages(), null); //(x, width, y, height) <- NEW BEST ONE
            } else if (status.equals("DSP")){
                sap.setVisibleSignature(new Rectangle(1075, 100, 1168, 60), reader.getNumberOfPages(), null); 
            } else if (status.equals("MINUTE")){
                sap.setVisibleSignature(new Rectangle(200, 280, 500, 200), reader.getNumberOfPages(), null);
            } else {
                sap.setVisibleSignature(new Rectangle(372, 125, 600, 80), reader.getNumberOfPages(), null); 
            }

            stp.close();

            //remove signed
            String fileIdSigned="" ;
            String filePathSigned="" ;
            String docSigned="" ;
            String fNameSigned = "";
            
            if(fName.contains("Signed.pdf")) {
                fNameSigned =  fName.substring(0, fName.length() - 10) + "Signed.pdf";
            } else {
                fNameSigned =  fName.substring(0, fName.length() - 4) + "Signed.pdf";
            }
            File tempSigned = new File(tempDir + fNameSigned);
            if(status.equals(UtimapsAction.WF_STATUS.ISSUE_SJI_PENDING)){
                fileIdSigned= com.sains.framework.base.CommonFunction.getId(20);
                filePathSigned = UtimapsAction.FILE_PATH.USJ_LETTER_SIGNED;
                docSigned = UtimapsAction.DOC_APPLICATION.USJ_LETTER_SIGNED;
                issuanceAction.uploadSignedFile(null, fileIdSigned, fNameSigned, filePathSigned, SystemConstants.FILE_TYPE.SISJL, jobId, tempSigned);

            } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS10_PENDING) || status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING) ||
                status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS20_PENDING) || status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS30_PENDING) || 
                status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS40_PENDING) || status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS50_PENDING) || 
                status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS60_PENDING) || status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS70_PENDING)|| 
                status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS80_PENDING) || status.equals(UtimapsAction.WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING) || 
                status.equals("DSP") || status.equals("MINUTE") || status.equals("PSDSP")) {
                fileIdSigned= com.sains.framework.base.CommonFunction.getId(20);
                if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS10_PENDING)) {
                    filePathSigned = UtimapsAction.FILE_PATH.USCS10_SIGNED;
                    docSigned = UtimapsAction.DOC_APPLICATION.USCS10_SIGNED;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS10_HARDCOPY_PENDING)) {
                    filePathSigned = UtimapsAction.FILE_PATH.USCS10H_SIGNED;
                    docSigned = UtimapsAction.DOC_APPLICATION.USCS10H_SIGNED;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS20_PENDING)) {
                    filePathSigned = UtimapsAction.FILE_PATH.USCS20_SIGNED;
                    docSigned = UtimapsAction.DOC_APPLICATION.USCS20_SIGNED;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS30_PENDING)) {
                    filePathSigned = UtimapsAction.FILE_PATH.USCS30_SIGNED;
                    docSigned = UtimapsAction.DOC_APPLICATION.USCS30_SIGNED;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS40_PENDING)) {
                    filePathSigned = UtimapsAction.FILE_PATH.USCS40_SIGNED;
                    docSigned = UtimapsAction.DOC_APPLICATION.USCS40_SIGNED;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS50_PENDING)) {
                    filePathSigned = UtimapsAction.FILE_PATH.USCS50_SIGNED;
                    docSigned = UtimapsAction.DOC_APPLICATION.USCS50_SIGNED;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS60_PENDING)) {
                    filePathSigned = UtimapsAction.FILE_PATH.USCS60_SIGNED;
                    docSigned = UtimapsAction.DOC_APPLICATION.USCS60_SIGNED;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS70_PENDING)) {
                    filePathSigned = UtimapsAction.FILE_PATH.USCS70_SIGNED;
                    docSigned = UtimapsAction.DOC_APPLICATION.USCS70_SIGNED;
                } else if (status.equals(UtimapsAction.WF_STATUS.ISSUE_USCS80_PENDING)) {
                    filePathSigned = UtimapsAction.FILE_PATH.USCS80_SIGNED;
                    docSigned = UtimapsAction.DOC_APPLICATION.USCS80_SIGNED;
                } else if (status.equals(UtimapsAction.WF_STATUS.ENDORSE_USP_ISSUE_USCS90_PENDING)) {
                    filePathSigned = UtimapsAction.FILE_PATH.USCS90_SIGNED;
                    docSigned = UtimapsAction.DOC_APPLICATION.USCS90_SIGNED;
                } else if (status.equals("DSP")) {
                    filePathSigned = UtimapsAction.FILE_PATH.SSDSP_SIGNED;
                    docSigned = UtimapsAction.DOC_APPLICATION.SSDSP_SIGNED;
                } else if (status.equals("MINUTE")) {
                    filePathSigned = UtimapsAction.FILE_PATH.MINUTE_SIGNED;
                    docSigned = UtimapsAction.DOC_APPLICATION.MINUTE_SIGNED;
                }  else if (status.equals("PSDSP")) {
                    fileType = SystemConstants.FILE_TYPE.PSDSP;
                    filePathSigned = UtimapsAction.FILE_PATH.PS_DSP_SIGNED;
                    docSigned = UtimapsAction.DOC_APPLICATION.PS_DSP_SIGNED;
                } 
                initError = submissionAction.uploadSignedFile(null, fileIdSigned, fNameSigned, filePathSigned, fileType, jobId, tempSigned, docSigned);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "PDFSign", "PDFSign", "selfSignMode");
            return -9;
        } finally {
            baseDAO.closeSession();
            issuanceAction.closeSession();
        }
        return 0;
    }

    public static byte[] streamToByteArray(InputStream stream) throws Exception {
        if (stream == null) {
            return null;
        } else {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            byte buffer[] = new byte[1024];
            int c = 0;
            while ((c = stream.read(buffer)) > 0) {
                byteArray.write(buffer, 0, c);
            }
            byteArray.flush();
            return byteArray.toByteArray();
        }
    }
    
    //FTP Setting   
//    public void closeFTP() {
//        try {            
//            if (System.getProperty("os.name").startsWith("Windows")) {
//                new FtpsUtil().disconnect();
//            } else {
////                new Sftp2Util().disconnectSftp();
//                new SFTPBean().disconnectSftp();
//            }
//        } catch (Exception e) {
//        }
//    } 
    
    public FtpInterface ftps = getFtps();

    public FtpInterface getFtps() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            ftps = new FtpsUtil();            
        }else{
//            ftps = new Sftp2Util();
            ftps = new SFTPBean();
        }
        return ftps;
    }

    public void setFtps(FtpInterface ftps) {
        this.ftps = ftps;
    }
}
