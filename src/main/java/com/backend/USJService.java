/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend;

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.EmailTrigger;
import com.sains.framework.base.LogFunction;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.model.User;
import com.sains.framework.sam.dao.AutoEmailDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
//import com.sains.workflow.web.RouteJobMainAction;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
//import org.json.simple.JSONArray;

//import org.apache.http.impl.client.HttpClientBuilder;

public class USJService extends BaseActionSupport<String> implements ModelDriven<String> {

//    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    private static final long serialVersionUID = -6659925652584240539L;
    private InputStream inputStream = null;
    Map paramMap = new HashMap();
    private String userId;
    private String userObjectId;
    private String currentPage_;
    public String paramId= "", paramFrom, paramStruts;
    private Map mailParam = new HashMap();
    private String contentDisposition;
    private String contentType = "";
    private List moduleList = new ArrayList();

    public String loadFrameTest() {
        return null;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getParamFrom() {
        return paramFrom;
    }

    public void setParamFrom(String paramFrom) {
        this.paramFrom = paramFrom;
    }

    public String getParamStruts() {
        return paramStruts;
    }

    public void setParamStruts(String paramStruts) {
        this.paramStruts = paramStruts;
    }



    public String HMAC_SHA256(String secret, String message) {
        byte[] hash = null;
        try {
            
            Mac hasher = Mac.getInstance("HmacSHA256");
            hasher.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));

            hash = hasher.doFinal(message.getBytes());

            return DatatypeConverter.printHexBinary(hash);
            
        } catch (Exception e) {

        }
        
        return DatatypeConverter.printBase64Binary(hash);
    }

    User userModel = new User();

    public User getUserModel() {
        return userModel;
    }

    public void setUserModel(User userModel) {
        this.userModel = userModel;
    }

    private String publicKey, paramHash;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getParamHash() {
        return paramHash;
    }

    public void setParamHash(String paramHash) {
        this.paramHash = paramHash;
    }
    
    public String actionMsg;

    public String getActionMsg() {
        return actionMsg;
    }

    public void setActionMsg(String actionMsg) {
        this.actionMsg = actionMsg;
    }

    //added by edmund 14062019 - epay now to enquire payment info from espa but to be initiate by espa every 20 mins
    public String ePayNowEnquiry(String biller, String svc, String biller_ref_no, String acct_no, String amt_paid, String merchant_id) throws UnsupportedEncodingException {
        try {
//            BaseDAOImpl dao = new BaseDAOImpl();
            String url = SystemConstants.DOMAIN.ebppEnquiryUrl;
//            String url = "";
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            String trans_type = "STATUS ENQUIRY";
//            acct_no = "lawyera"; //testing

            //---edmund code here end --- 14052019
            // add header
            post.setHeader("User-Agent", USER_AGENT);
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("trans_type", trans_type)); //SPA_19000224
            urlParameters.add(new BasicNameValuePair("biller", biller));
            urlParameters.add(new BasicNameValuePair("svc", svc));
            urlParameters.add(new BasicNameValuePair("biller_ref_no", biller_ref_no));
            urlParameters.add(new BasicNameValuePair("acct_no", acct_no));
            urlParameters.add(new BasicNameValuePair("amt_paid", amt_paid));
            urlParameters.add(new BasicNameValuePair("merchant_id", merchant_id));

            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = client.execute(post);
            String result = "";
            if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
                result = EntityUtils.toString(response.getEntity());
                //document to read XML response
                DocumentBuilderFactory factoryBuilder = DocumentBuilderFactory.newInstance();
                factoryBuilder.setIgnoringComments(true);
                factoryBuilder.setIgnoringElementContentWhitespace(true);
                DocumentBuilder db = factoryBuilder.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(result));
                Document doc = db.parse(is);
                System.out.println("trans_type == " + doc.getElementsByTagName("trans_type").item(0).getTextContent());
                System.out.println("biller_ref_no == " + doc.getElementsByTagName("biller_ref_no").item(0).getTextContent());
                System.out.println("acct_no == " + doc.getElementsByTagName("acct_no").item(0).getTextContent());
                System.out.println("amt_paid == " + doc.getElementsByTagName("amt_paid").item(0).getTextContent());
//                System.out.println("merchant_id == " + doc.getElementsByTagName("merchant_id").item(0).getTextContent());
                System.out.println("pmt status == " + doc.getElementsByTagName("pmt_status").item(0).getTextContent());
                System.out.println("pmt_channel == " + doc.getElementsByTagName("pmt_channel").item(0).getTextContent());
                System.out.println("pmt_mode == " + doc.getElementsByTagName("pmt_mode").item(0).getTextContent());
                System.out.println("pbm_resp_code == " + doc.getElementsByTagName("pbm_resp_code").item(0).getTextContent());
                System.out.println("pbm_resp_message == " + doc.getElementsByTagName("pbm_resp_message").item(0).getTextContent());
                System.out.println("pbm_receipt == " + doc.getElementsByTagName("pbm_receipt").item(0).getTextContent());
                System.out.println("pbm_process_date == " + doc.getElementsByTagName("pmt_processed_date").item(0).getTextContent());
                System.out.println("pmt_processed_time == " + doc.getElementsByTagName("pmt_processed_time").item(0).getTextContent());
//                System.out.println("status_date== " + doc.getElementsByTagName("status_date").item(0).getTextContent());
                //need to get all return code value, if not successful what to do next, item(0) - only 1 record                 
                String pmtStatus = "";
                String pbmReceipt = "";
                String statusDate = "";
                pmtStatus = doc.getElementsByTagName("pmt_status").item(0).getTextContent();
                pbmReceipt = doc.getElementsByTagName("pbm_receipt").item(0).getTextContent();
                statusDate = doc.getElementsByTagName("pmt_processed_date").item(0).getTextContent() +" " +doc.getElementsByTagName("pmt_processed_time").item(0).getTextContent();
                actionMsg = pmtStatus + "," + pbmReceipt + "," + statusDate;
            }

            System.out.println("actionMsg == " + actionMsg);
        } catch (Exception e) {
            System.out.println("request epaynow error ");
            e.printStackTrace();
        }
        return actionMsg;
    }
    
    @Override
    public String getModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        

}

