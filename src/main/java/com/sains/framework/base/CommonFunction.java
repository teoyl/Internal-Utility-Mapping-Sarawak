package com.sains.framework.base;

import com.SysConf;
import com.google.gson.Gson;
import com.lxg.common.model.Pubcode;
import com.lxg.common.model.Rvcoddtl;
import com.lxg.common.model.Rvrcode;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.SeqModel;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.model.Application;
import com.sains.framework.model.ApplicationRights;
import com.sains.framework.model.GroupApplication;
import com.sains.framework.model.GroupApplicationRights;
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.User;
import com.sains.framework.sam.dao.ApplicationDAOImpl;
import com.sains.framework.sam.dao.UserDAOImpl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.management.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import com.sains.common.util.Validator;
import com.sains.framework.lookup.*;
import com.sains.framework.model.AuditLoginModel;
import com.sains.framework.model.ParameterModel;
import com.utimaps.model.AuditActionModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.NoSuchProviderException;
import javax.net.ssl.SSLContext;
import javax.xml.XMLConstants;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.struts2.ServletActionContext;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.mortbay.util.Password;

import org.apache.catalina.Server;
//import org.apache.catalina.ServerFactory;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.ajp.AjpProtocol;
import org.apache.coyote.ajp.AjpAprProtocol;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.joda.time.Days;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Retrieve the data for the query that is specified in the config file.
 *
 * @version 1.0
 * @author ThenSW
 */
// TODO: to change the JDBC handling with hibernate query to eliminate the hard-coding of LIMIT (use setmaxresult instead)
public class CommonFunction {

    private static Map dynamicConfigMap = null;
    private static Map dynamicRptConfigMap = null;

    public static final class SecurityConditionLocation {

        public static final String DataRetriver = "DR";
        public static final String CommonList = "CL";
        public static final String DynamicRptAction = "Rpt";
    }
    private final String DYNAMIC_CONFIG = "action";
    public static final int PK_LENGTH = 20;
    private Boolean oracleDB = Boolean.FALSE;
    private String session_userKey = "userId";

    public static ActionSupport as = new ActionSupport(); //Delvene @ 15-Apr-2013 :: To support calculateAgeWithMonth function

    public static void exceptionStackTrace(Exception e) {
        if (SystemConstants.ShowException) {
            e.printStackTrace();
        }
    }

    public boolean isOracleDB() {
//            if (oracleDB == null){
//                java.sql.Connection conn = SessionFactoryImpl.getConnection();
//                try {
//                    oracleDB = conn.getMetaData().getDatabaseProductName().equalsIgnoreCase("Oracle");
//                } catch (Exception e){
//
//                } finally {
//                    try {
//                        if (conn != null && !conn.isClosed()){
//                            conn.close();
//                        }
//                    } catch (Exception ex){}
//                }
//            }
//            if (oracleDB == null){
//                oracleDB = false;
//            }
        return oracleDB;
    }

    public Map getDynamicConfiguration(String action, HttpServletRequest request, ServletContext context)
	throws Exception {
		return getDynamicConfiguration(action, request, context, "");
	}

	public Map getDynamicConfiguration(String action, HttpServletRequest request, ServletContext context, String defaultAction)
			throws Exception {

		Map pagingConfigMap = null;
		Map queryMap = null;
		try {
			//PersistentSession session = EMSPersistentManager.instance().getSession();
			//Map criterias = (Map) form.get

			//Query form.session.createSQLQuery(getSQL(request, context, form.getCriterias(), true, true, false));
                        if (dynamicConfigMap == null) {
                            loadConfiguration(action, request, context, defaultAction);
                        }
                        if (dynamicConfigMap != null) {
                            if (defaultAction.equals("")){
                                return (Map)((Map) dynamicConfigMap).get(action);
                            } else {
                                return (Map)((Map) dynamicConfigMap).get(defaultAction);
                            }
                        }
                        
//                        if (context.getAttribute(DYNAMIC_CONFIG) != null) {
//                            if (defaultAction.equals("")){
//                                return (Map)((Map) context.getAttribute(DYNAMIC_CONFIG)).get(action);
//                            } else {
//                                return (Map)((Map) context.getAttribute(DYNAMIC_CONFIG)).get(defaultAction);
//                            }
//                        }
//                        
//			loadConfiguration(action, request, context, defaultAction);
//			pagingConfigMap = (Map) context.getAttribute(DYNAMIC_CONFIG);
//			if (defaultAction.equals("")){
//                            queryMap = (Map) pagingConfigMap.get(action);
//			} else {
//                            queryMap = (Map) pagingConfigMap.get(defaultAction);
//			}

		} catch (Exception e) {
			new LogFunction().logError(this.getClass(), "", e);
		}
		return queryMap;
	}

    public Map getDynamicEntryConfiguration(String action, HttpServletRequest request, ServletContext context, String defaultAction)
            throws Exception {

        Map pagingConfigMap = null;
        Map queryMap = null;
        try {
            //PersistentSession session = EMSPersistentManager.instance().getSession();
            //Map criterias = (Map) form.get

            //Query form.session.createSQLQuery(getSQL(request, context, form.getCriterias(), true, true, false));
            loadEntryConfiguration(action, request, context, defaultAction);
            pagingConfigMap = (Map) context.getAttribute("EntryConfig");
            if (defaultAction.equals("")) {
                queryMap = (Map) pagingConfigMap.get(action);
            } else {
                queryMap = (Map) pagingConfigMap.get(defaultAction);
            }
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }
        return queryMap;
    }

    public Map getDynamicReportConfiguration(String action, HttpServletRequest request, ServletContext context, String defaultAction)
            throws Exception {

        Map pagingConfigMap = null;
        Map queryMap = null;
        try {
            //PersistentSession session = EMSPersistentManager.instance().getSession();
            //Map criterias = (Map) form.get

            //Query form.session.createSQLQuery(getSQL(request, context, form.getCriterias(), true, true, false));
            loadReportConfiguration(action, request, context, defaultAction);
            pagingConfigMap = (Map) context.getAttribute("ReportConfig");
            if (defaultAction.equals("")) {
                queryMap = (Map) pagingConfigMap.get(action);
            } else {
                queryMap = (Map) pagingConfigMap.get(defaultAction);
            }
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }
        return queryMap;
    }

    private void loadConfiguration(String action, HttpServletRequest request,
            ServletContext context) {
        loadConfiguration(action, request, context, "");
    }

    private void loadConfiguration(String action, HttpServletRequest request,
			ServletContext context, String defaultAction) {
		Config config = new Config();
		/**
		 * FIXME: use a constant
		 **/
		Map map = null;
		//final String fileHeader = "file:///";
                final String fileHeader = "";
		if (defaultAction.equals("")) {
			//String filename = context.getRealPath("/WEB-INF/config/dynamic-config.xml");
			//filename = fileHeader+filename;
//                        java.sql.Connection conn = null;
                        try {
//                            conn = com.sains.framework.base.SessionFactoryImpl.getConnection();
                            if (oracleDB){
                                map = config.read(context.getResourceAsStream("/WEB-INF/config/dynamic-config_o.xml"),
                                            new DynamicConfigHandler(getParameter(request, action, defaultAction)));
                            } else {
                                map = config.read(context.getResourceAsStream("/WEB-INF/config/dynamic-config.xml"),
                                            new DynamicConfigHandler(getParameter(request, action, defaultAction)));
                            }
                        } catch (Exception ex){
                            new LogFunction().logError(this.getClass(), "Error Reading dynamic-config", ex);
//                        } finally {
//                            com.sains.framework.base.SessionFactoryImpl.closeConnection(conn);
                        }
		} else {
			//String filename = context.getRealPath("/WEB-INF/config/query-config.xml");
			//filename = fileHeader+filename;
                        map = config.read(context.getResourceAsStream("/WEB-INF/config/query-config.xml"),
					new LookupConfigHandler());
		}

		if (map != null) {
//                    context.setAttribute(DYNAMIC_CONFIG, map);
                    dynamicConfigMap = map;
		}
	}

    private void loadEntryConfiguration(String action, HttpServletRequest request,
            ServletContext context, String defaultAction) {
        Config config = new Config();
        /**
         * FIXME: use a constant
		 *
         */
        Map map = null;
        //final String fileHeader = "file:///";
        //java.sql.Connection conn = com.sains.framework.base.SessionFactoryImpl.getConnection();
        try {
            map = config.read(context.getResourceAsStream("/WEB-INF/config/dynamicEntry-config.xml"),
                    new DynamicConfigHandler(getParameter(request, action, defaultAction)));

        } catch (Exception ex) {

        }

        if (map != null) {
            context.setAttribute("EntryConfig", map);
        }
    }

    private void loadReportConfiguration(String action, HttpServletRequest request,
            ServletContext context, String defaultAction) {
        Config config = new Config();
        /**
         * FIXME: use a constant
		 *
         */
        Map map = null;
        //final String fileHeader = "file:///";
        //java.sql.Connection conn = com.sains.framework.base.SessionFactoryImpl.getConnection();
        try {
            map = config.read(context.getResourceAsStream("/WEB-INF/config/dynamicReport-config.xml"),
                    new DynamicConfigHandler(getParameter(request, action, defaultAction)));
//                    if (conn != null){
//                        conn.close();
//                    }
        } catch (Exception ex) {

        }

        if (map != null) {
            context.setAttribute("ReportConfig", map);
        }
    }

    private String getParameter(HttpServletRequest request, String key,
            String defaultValue) {
        String result = defaultValue;

        if (!Validator.isEmpty(request.getParameter(key))) {
            result = request.getParameter(key);
        }

        return result;
    }

    // ThoTH @ 10-Jun-2013 :: been replaced
//	public static String GetId(int intNoChar ) {
//		String strValue = "";
//		String idchars = "abcdefhjmnpqrstuvwxyz23456789ABCDEFGHJKLMNPQRSTUVWYXZ";
//		String strMathDouble = "";
//		int idlength = intNoChar;
//		for (int i=0;i<idlength;i++) {
//			strValue+=idchars.charAt((int) Math.round(Math.floor(Math.random()*idchars.length())));
//		}
//		return strValue;
//	}
    // ThoTH @ 10-Jun-2013 :: Replace original getId, to reduce the chance of duplication
    // Suggest intNoChar at least 18 :: Tested to Run 10,000 times in 1 second, get 0 duplication
    // Tested 16 char :: Save when run 100 times in 1 secod, but will get duplication if run 200 times in 1 second
    // ThenSW  @ 16-Jul-2013 :: Last Char will use Tomcat's AJP's port
    private static int runningNum = 0;
    private static String addZero(String number, int length) {
        String temp = number;
        for (int count = 0; count < length - number.length(); count++) {
            temp = "0" + temp;
        }
        return temp;
    }
    public static synchronized Long getIntId_milisec_plus_4_runningDigit() {
        String strValue = Long.toString(System.currentTimeMillis());  // I tried year 2020, will get this "1970851220498", still 13chars, with 19 infront
//            String strMathDouble = "";
        
         if (runningNum > 9999) {
            runningNum = 0;
        }
        strValue += addZero(Integer.toString(runningNum), 4);
        runningNum++;
        return new Long(strValue);
    }
    public static String getId(int intNoChar) {
        intNoChar--;
        if (SystemConstants.AJP_PORT == null) {
            constructAJP_PORT_inChar();
        }
        String idchars = "abcdefhjmnpqrstuvwxyz23456789ABCDEFGHJKLMNPQRSTUVWYXZ";
        String strValue = Long.toString(System.currentTimeMillis());  // I tried year 2020, will get this "1970851220498", still 13chars, with 19 infront
//            String strMathDouble = "";

        if (intNoChar < strValue.length()) {
            strValue = "";  // In case strNoChar less than 13
        }
        int idlength = intNoChar - strValue.length();
        for (int i = 0; i < idlength; i++) {
            strValue += idchars.charAt((int) Math.round(Math.floor(Math.random() * idchars.length())));
        }

        return strValue + SystemConstants.AJP_PORT;
    }

    // ThenSW  @ 16-Jul-2013 :: Ge the 2nd and 3rd Digit of Tomcat AJP port, and convert to one char
    // e.g. 8009 == 0, 8059 == 5, 8109 == A, 8129 == C
    public static void constructAJP_PORT_inChar() {
        if (SystemConstants.AJP_PORT == null) {
            Integer port = 0;
//                Server server = ServerFactory.getServer();
            try {
                // ThoTH @ 13-Sept-2013 :: For Tomcat 7
                MBeanServer mBeanServer = MBeanServerFactory.findMBeanServer(null).get(0);
                ObjectName name = new ObjectName("Catalina", "type", "Server");
                Server server = (Server) mBeanServer.getAttribute(name, "managedResource");
                Service[] services = server.findServices();
                for (Service service : services) {
                    for (Connector connector : service.findConnectors()) {
                        ProtocolHandler protocolHandler = connector.getProtocolHandler();
                        if (protocolHandler instanceof AjpAprProtocol
                                || protocolHandler instanceof AjpProtocol) {
                            port = Integer.parseInt(("" + connector.getPort()).substring(1, 3));
                            break;
                        }
                    }

                }
            } catch (Exception e) {
            }
            SystemConstants.AJP_PORT = numToChar(port);
        }
    }

    // ThenSW  @ 16-Jul-2013
    public static String numToChar(int port) {
        if (port < 10) {
            return "" + port;
        }
        switch (port) {
            case 10:
                return "A";
            case 11:
                return "B";
            case 12:
                return "C";
            case 13:
                return "D";
            case 14:
                return "E";
            case 15:
                return "F";
            case 16:
                return "G";
            case 17:
                return "H";
            case 18:
                return "I";
            case 19:
                return "J";
            case 20:
                return "K";
            case 21:
                return "L";
            case 22:
                return "M";
            case 23:
                return "N";
            case 24:
                return "O";
            case 25:
                return "P";
            case 26:
                return "Q";
            case 27:
                return "R";
            case 28:
                return "S";
            case 29:
                return "T";
            case 30:
                return "U";
            case 31:
                return "V";
            case 32:
                return "W";
            case 33:
                return "X";
            case 34:
                return "Y";
            case 35:
                return "Z";
        }
        return "0";
    }

    public String getSingleValue(String strTable, String strColumn, String strWhere) {
//		String strResult = "";
        Object obj = null;

        BaseDAO baseDAO = new BaseDAOImpl();
        org.hibernate.SQLQuery query = null;
        try {
            //List list = new ArrayList();
            String strSql = "";
            if (strWhere.equals("")) {
                strSql = "SELECT " + strColumn + " FROM " + strTable;
            } else {
                strSql = "SELECT " + strColumn + " FROM " + strTable + " WHERE " + strWhere;
            }
//			stmt = conn.prepareStatement(strSql);
//			rs = stmt.executeQuery();
//			if (rs.next()) {
//				// if value required is computed column
//				if (strColumn.toUpperCase().lastIndexOf(" AS ") > 0) {
//					strColumn = strColumn.substring(strColumn.toUpperCase().lastIndexOf(" AS ")+4).trim();
//			    }
//			    strResult = rs.getString(strColumn);
//			} else {
//				strResult = "";
//			}
            query = baseDAO.getSession().createSQLQuery(strSql);
            obj = query.uniqueResult();
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
            baseDAO.closeSession();
//			try {
//				rs.close();
//				stmt.close();
//				SessionFactoryImpl.closeConnection(conn);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				new LogFunction().logError(this.getClass(), "", e);
//			}
        }
        if (obj == null) {
            return "";  // ThoTH @ 17-Oct-2013
        }
        return obj.toString();
//		return strResult;
    }

    public String encData(String str) {
        String strNew = Password.obfuscate(str);
        String strSubNew = strNew.substring(4);
        return strSubNew;
    }

    public String decryptData(String str) {
        String strNew = Password.deobfuscate(str);
        return strNew;
    }

    //added by wongkk4 8/7/2010
    public static String getRunningSeq(String strType, String strYear, String prefix, String postfix) {
        BaseDAO<SeqModel> seqDAO = new BaseDAOImpl();
        DecimalFormat sixDigits = new DecimalFormat("000000");
        //String strNewSeq = "";
        long llSeq = 0;
        try {
            SeqModel seq = seqDAO.getModelById(strType + strYear, SeqModel.class);
            if (seq == null) {
                //System.out.println("NewSeq");
                llSeq = 1;
                seq = new SeqModel();
                seq.setSeq_id(strType + strYear);
                seq.setSeq_last_no(llSeq);

                seqDAO.insert(seq);
            } else {
                //System.out.println("Before seq = " + seq.getSeq_last_no() );
                llSeq = seq.getSeq_last_no() + 1;
                seq.setSeq_last_no(llSeq);
                //try{
                //seqDAO.update(seq);
                seqDAO.directUpdate(seq);
                //}catch(Exception ex){
                //}
            }
            // System.out.println("After seq = " + llSeq );
            //aeDAO.update(ae);
        } catch (Exception e) {
        } finally {
            seqDAO.closeSession();
        }
        return prefix + sixDigits.format(llSeq) + postfix;
    }

    public static void writeFile(String fileName, String data) {
        String logPath = "C:\\utimaps\\log\\";
        
        //14.08.2024::log at server
        if (System.getProperty("os.name").startsWith("Windows")) {
            logPath = "C:\\utimaps\\log\\";
        } else {
            logPath = "/home/utilitysurvey-tnt/apache-tomcat-8.5.37/utimaps-logs/";
        }
        
        try {
            String enterChar = "\r\n";
            //File dir = new File(logPath + fileName + ".log");
            File dir = new File(logPath);

            //create directory if not exist
            if (dir.exists() == false) {
                dir.mkdirs();
                dir.createNewFile();
                enterChar = "";
            }

//            if (!dir.exists()){
//                dir.createNewFile();
//                enterChar = "";
//            }
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(logPath + fileName + ".log", true));
            writer.write(enterChar + Formatter.formatDate(DateUtil.getCurrentTimestamp(), "yyyy-MM-dd hh:mm:ss") + " >> " + data);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To log the Backend Latest RunTime - to be read by Backend Checker, and
     * inform Developers if Backend is not running
     *
     * @param backendName
     *
     * Created by ThoTH @ 16-Nov-2011
     */
    public static void backend_checkIn(String backendName) {
        String logPath = "C:\\msen\\BackendChecker\\";
        try {
            File dir = new File(logPath);

            //create directory if not exist
            if (dir.exists() == false) {
                dir.mkdirs();
                dir.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(logPath + backendName + "_cur.log", false));
            writer.write(Formatter.formatDate(DateUtil.getCurrentTimestamp(), "yyyy-MM-dd hh:mm:ss"));
            writer.close();
        } catch (Exception e) {
//            e.printStackTrace();
            new LogFunction().logError(CommonFunction.class, "", e);
        }
    }

    private static String convertStreamToString(InputStream is) throws IOException {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

//    public boolean validatePublicRight(String actionClassName, String method, Map sessionMap, String paramAction){
//        session_userKey = "userId";
//        return validateRight(actionClassName, method, sessionMap, paramAction);
//    }
    public boolean validateRight(String actionClassName, String method, Map sessionMap, String paramAction, Session hibernateSession) {
        session_userKey = "userId";
        String byPassAction = new ActionSupport().getText("bypass.action");
        StringTokenizer stActions = new StringTokenizer(byPassAction, ",");
        UserDAOImpl userDAO = new UserDAOImpl();
        userDAO.setSession(hibernateSession);
        ApplicationDAOImpl applicationDAO = new ApplicationDAOImpl();
        applicationDAO.setSession(hibernateSession);
        User user = new User();
        while (stActions.hasMoreTokens()) {
            String temp = stActions.nextToken();
            if (!(temp.equals("DynamicAction"))) {
                if (actionClassName.equals(temp)) {
                    return true;
                }
            }
        }

        if (sessionMap.get(session_userKey) == null) {
            return false;
        }

        try {
            if (actionClassName.equals("DynamicAction")) {
//                System.out.println("DA------------------");
                Map param = new HashMap();
                //param.put("us_user_id", userId);
//                if (session_userKey.startsWith("p_")) {
//                    //user = userDAO.getModelById(userId, user);
//                } else {
//                    user = userDAO.getModelById(userId, user);
//                }
                //wongkk4@26Dec2013
                if (ActionContext.getContext().getSession().get("userId") instanceof Integer) {
                    user = userDAO.getModelById((Integer)sessionMap.get("userId"), User.class);
                } else {
                    user = userDAO.getModelById((String)sessionMap.get("userId"), User.class);
                }
                if (user != null) {
//                    System.out.println("Common Function validate right 111111111" + user.getGroupUserList().size());
//                    System.out.println("method = " + method);
                    for (GroupUser gu : user.getGroupUserList()) {
//                        System.out.println("gu.id = " + gu.getID());
                        for (GroupApplication groupApp : gu.getUserGroup().getGroupApplication()) {
//                            System.out.println("Common Function validate right 2222222222" + groupApp.getApplication().getApplication_code());
                            if (groupApp.getApplication().getApplication_code().equals(paramAction)) {
//                                System.out.println("Common Function validate right 333333");
                                //if user is administrator & the Application is a System Application then allow to
                                //proceed without the need to check access right.
                                if (groupApp.getApplication().getSystem_app().equalsIgnoreCase("Y")
                                        && user.getUs_admin().equalsIgnoreCase("Y")) {
                                    return true;
                                }

                                if (method.equals("search2")) {
                                    if (!groupApp.getApplication().getRetrieve_right().equalsIgnoreCase("Y") || !groupApp.getRetrieve_right().equalsIgnoreCase("Y")) {
                                        return false;
                                    }
                                }
                                if (method.equals("loadAddPage") || method.equals("processInsert")) {
//                                    System.out.println("------------------------- load add page");
                                    if (!groupApp.getApplication().getCreate_right().equalsIgnoreCase("Y") || !groupApp.getCreate_right().equalsIgnoreCase("Y")) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                }
                                if (method.equals("delete")) {
                                    if (!groupApp.getApplication().getDelete_right().equalsIgnoreCase("Y") || !groupApp.getDelete_right().equalsIgnoreCase("Y")) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                }
                                if (method.equals("dynamicUpdate") || method.equals("loadEditPage") || method.equals("processUpdate")) { // to edit, must have retrieve + update right
                                    if (!groupApp.getApplication().getRetrieve_right().equalsIgnoreCase("Y") || !groupApp.getRetrieve_right().equalsIgnoreCase("Y")) {
                                        return false;
                                    } else if (!groupApp.getApplication().getUpdate_right().equalsIgnoreCase("Y") || !groupApp.getUpdate_right().equalsIgnoreCase("Y")) {
                                        return true;
                                    } else {
                                        return true;
                                    }
                                }
                                //wongkk4@4Apr2014 for Additional Button
                                for (ApplicationRights appRights : groupApp.getApplication().getRightsList()) {
                                    StringTokenizer st = new StringTokenizer(appRights.getApp_rights_methods(), ",");
                                    while (st.hasMoreTokens()) {
                                        String temp = st.nextToken().trim();
//                                            System.out.println("validateRight temp = " + temp);
//                                            System.out.println("validateRight method = " + method);
                                        if (method.equals(temp)) {
                                            if (paramAction.equals(groupApp.getApplication().getApplication_code())) {

                                                //Found same method name, check for access rights
                                                for (GroupApplicationRights gaRights : groupApp.getGroupAppRights()) {
                                                    //new SessionFactoryImpl().getSession().refresh(gaRights);
                                                    if (gaRights.getApp_rights_id().equals(appRights.getApp_rights_id())) {
                                                        if (gaRights.getHasRight().equals("Y")) {
//                                                                System.out.println("44444444444444");
                                                            return true;
                                                        } else {
                                                            break;
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                    if (method.equals("populateDynamicActionSetup")) {
//                        System.out.println("ppppppppppppppppppp");
                        param.clear();
                        param.put("application_code", paramAction);
                        List<Application> applicationList = applicationDAO.list(param, Application.class);
                        if (applicationList != null && applicationList.size() > 0) {
                            //if user is administrator & the Application is a System Application then allow to
                            //proceed without the need to check access right.
                            if (applicationList.get(0).getSystem_app().equalsIgnoreCase("Y")
                                    && user.getUs_admin().equalsIgnoreCase("Y")) {
                                applicationDAO.getSession().evict(applicationList.get(0));
                                return true;
                            }
                            // else not allow to access because not assigned to.
                            return false;
                        } else {
                            return false;
                        }
                    }
                }
            } else {
                //no need to check authorisation if the method is cancel
                if (method.equalsIgnoreCase("cancel")) {
                    return true;
                }

                //param.put("us_user_id", userId);
                if (ActionContext.getContext().getSession().get("userId") instanceof Integer) {
                    user = userDAO.getModelById((Integer)sessionMap.get("userId"), User.class);
                } else {
                    user = userDAO.getModelById((String)sessionMap.get("userId"), User.class);
                }
//                System.out.println("userId " + userId);
                if (user != null) {
//                    System.out.println("user.getGroupUserList().size() " + user.getGroupUserList().size());
                    for (GroupUser gu : user.getGroupUserList()) {
//                        System.out.println("gu.getUserGroup().getGroupApplication().size() " + gu.getUserGroup().getGroupApplication().size());
                        for (GroupApplication groupApp : gu.getUserGroup().getGroupApplication()) {
//                            System.out.println("i :" + i);

//                            System.out.println("CF groupApp.getApplication().getApplication_name() " + groupApp.getApplication().getApplication_name());
//                            System.out.println("CF groupApp.getApplication().getAction_class() " + groupApp.getApplication().getAction_class());
//                            System.out.println("CF actionClassName " + actionClassName);
//                            System.out.println("groupApp.getGroupAppRights().size() " );
//                            System.out.println("groupApp.getGroupAppRights().size() " + groupApp.getGroupAppRights().size());
                            if (groupApp.getApplication().getAction_class().equals(actionClassName)) {
                                //if user is administrator & the Application is a System Application then allow to
                                //proceed without the need to check access right.
                                if (groupApp.getApplication().getSystem_app().equalsIgnoreCase("Y")
                                        && user.getUs_admin().equalsIgnoreCase("Y")) {
                                    return true;
                                }
                                //start the authorisation check below.
                                if (method.equals("loadAddPage") || method.equals("processInsert")) {
                                    if (!groupApp.getApplication().getCreate_right().equalsIgnoreCase("Y") || !groupApp.getCreate_right().equalsIgnoreCase("Y")) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                } else if (method.equals("dynamicUpdate") || method.equals("loadEditPage") || method.equals("processUpdate")) {
                                    if (!groupApp.getApplication().getUpdate_right().equalsIgnoreCase("Y")
                                            || !groupApp.getUpdate_right().equalsIgnoreCase("Y")) {
                                        if (method.equals("loadEditPage")) {
                                            return false;
                                        } else {
                                            return false;
                                        }
                                    } else {
                                        return true;
                                    }
                                } else if (method.equals("delete")) {
                                    if (!groupApp.getApplication().getDelete_right().equalsIgnoreCase("Y") || !groupApp.getDelete_right().equalsIgnoreCase("Y")) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                } else if (method.equals("processPrint")) {
                                    if (!groupApp.getApplication().getPrint_right().equalsIgnoreCase("Y") || !groupApp.getPrint_right().equalsIgnoreCase("Y")) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                } else { // check for additional access rights
//                                    System.out.println("55555 = " + groupApp.getApplication().getRightsList().size());
//                                    for (ApplicationRights appRights : groupApp.getApplication().getRightsList()) {
////                                        appRights.getGroupApplicationRightsList().size();
//                                        System.out.println(" test Size");
//                                        System.out.println("appRights.getRightMethodList().size() " + appRights.getRightMethodList().size());
//                                        System.out.println("appRights.getApp_rights_methods() " + appRights.getApp_rights_methods());
//                                        StringTokenizer st = new StringTokenizer(appRights.getApp_rights_methods(), ",");
//                                        while (st.hasMoreTokens()) {
//                                            String temp = st.nextToken().trim();
//                                            System.out.println("Common Function validateRight temp = " + temp);
//                                            System.out.println("Common Function  validateRight method = " + method);
//                                            if (method.equals(temp)) {
//                                                System.out.println("groupApp.getUg_app_id()" + groupApp.getUg_app_id());
//                                                try {
//                                                System.out.println("wongkk groupApp.getGroupAppRights().size() " + groupApp.getGroupAppRights().size());
//                                                //Found same method name, check for access rights
//                                                for (GroupApplicationRights gaRights : groupApp.getGroupAppRights()) {
//                                                    System.out.println("gaRights.getApp_rights_id() " + gaRights.getApp_rights_id());
//                                                    System.out.println("appRights.getApp_rights_id() " + appRights.getApp_rights_id());
//                                                    //new SessionFactoryImpl().getSession().refresh(gaRights);
//                                                    if (gaRights.getApp_rights_id().equals(appRights.getApp_rights_id())) {
//                                                        if (gaRights.getHasRight().equals("Y")) {
//                                                            return true;
//                                                        } else {
//                                                            break;
//                                                        }
//                                                    }
//                                                }
//
//                                                } catch (Exception e) {
//                                                    System.out.println("Wongkk Error " + e.getMessage());
//                                                }
//                                            }
//                                        }
//                                    }//for
                                    //wongkk4@9Apr2014 - can have more than 1 application using the same action
                                    // return false;
                                    for (ApplicationRights appRights : groupApp.getApplication().getRightsList()) {
//                                        System.out.println("appRights.getApp_rights_methods() " + appRights.getApp_rights_methods());
                                        StringTokenizer st = new StringTokenizer(appRights.getApp_rights_methods(), ",");
                                        while (st.hasMoreTokens()) {
                                            String temp = st.nextToken().trim();
//                                            System.out.println("Common Function validateRight temp = " + temp);
//                                            System.out.println("Common Function  validateRight method = " + method);
                                            if (method.equals(temp)) {
                                                //Found same method name, check for access rights
                                                for (GroupApplicationRights gaRights : groupApp.getGroupAppRights()) {
                                                    //new SessionFactoryImpl().getSession().refresh(gaRights);
                                                    if (gaRights.getApp_rights_id().equals(appRights.getApp_rights_id())) {
                                                        if (gaRights.getHasRight().equals("Y")) {
                                                            //System.out.println("33333333333333");
                                                            return true;
                                                        } else {
                                                            break;
                                                        }
                                                    }
                                                }

                                                //}
                                            }
                                        }
                                    }
                                }
                            }//if (groupApp.getApplication().getAction_class().equals(actionClassName)) {
                        }//for
                    }
                    if (applicationDAO.getApplicationByActionClass(actionClassName, new Application()).getSystem_app().equalsIgnoreCase("Y")
                            && user.getUs_admin().equalsIgnoreCase("Y")) {
                        return true;
                    }
                    return false;
                } else {
                }
            }
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
//        } finally {
//            userDAO.closeSession();
//            applicationDAO.closeSession();
        }
        return false;
    }

    public boolean validateRight_new(String actionClassName, String method, Map sessionMap, String paramAction, Session hibernateSession) {
        session_userKey = "userId";

        String byPassAction = new ActionSupport().getText("bypass.action");
        StringTokenizer stActions = new StringTokenizer(byPassAction, ",");
        UserDAOImpl userDAO = new UserDAOImpl();
        userDAO.setSession(hibernateSession);
        BaseDAO retrievingDAO = new BaseDAOImpl();
        retrievingDAO.setSession(hibernateSession);
        ApplicationDAOImpl applicationDAO = new ApplicationDAOImpl();
        applicationDAO.setSession(hibernateSession);
        User user = new User();
        while (stActions.hasMoreTokens()) {
            String temp = stActions.nextToken();
            if (!(temp.equals("DynamicAction"))) {
                if (actionClassName.equals(temp)) {
                    return true;
                }
            }
        }

        if (sessionMap.get(session_userKey) == null) {
            return false;
        }

        try {
            if (actionClassName.equals("DynamicAction")) {
                Map param = new HashMap();
                //param.put("us_user_id", userId);
                if (session_userKey.startsWith("p_")) {
                    //user = userDAO.getModelById(userId, user);
                } else {
                    if (ActionContext.getContext().getSession().get("userId") instanceof Integer) {
                        user = userDAO.getModelById((Integer)sessionMap.get("userId"), User.class);
                    } else {
                        user = userDAO.getModelById((String)sessionMap.get("userId"), User.class);
                    }
                }
                if (user != null) {
                    String rightCondition = null;
                    if (method.equals("search2") || method.equals("loadEditPage") || method.equals("populateDynamicActionSetup")) {
                        rightCondition = "application.retrieve_right = 'Y' and group_app.retrieve_right = 'Y'";
                    } else if (method.equals("loadAddPage") || method.equals("processInsert")) {
                        rightCondition = "application.create_right = 'Y' and group_app.create_right = 'Y'";
                    } else if (method.equals("delete")) {
                        rightCondition = "application.delete_right = 'Y' and group_app.delete_right = 'Y'";
                    } else if (method.equals("dynamicUpdate") || method.equals("processUpdate")) {
                        rightCondition = "application.update_right = 'Y' and group_app.update_right = 'Y'";
                    }
                    String rightSql = "select count(*) from t_setup_group "
                            + "inner join t_setup_group_user group_user on t_setup_group.ug_id = group_user.ug_id "
                            + "inner join t_setup_user on t_setup_user.us_id = group_user.us_id "
                            + "inner join t_setup_group_app group_app on t_setup_group.ug_id = group_app.ug_id "
                            + "inner join t_setup_application application on application.application_id = group_app.application_id "
                            + "where (application_code = :paramAction and t_setup_user.us_id = '" + user.getID() + "' "
                            + "and " + rightCondition + ")";
                    Map rightParam = new HashMap();
                    rightParam.put("paramAction", paramAction);
                    if (retrievingDAO.sqlCountRecord(rightSql, rightParam) > 0) { // rights Found
                        return true;
                    } else {
                        return false;
                    }

                }
            } else {
                //no need to check authorisation if the method is cancel
                if (method.equalsIgnoreCase("cancel")) {
                    return true;
                }

                //param.put("us_user_id", userId);
                if (ActionContext.getContext().getSession().get("userId") instanceof Integer) {
                    user = userDAO.getModelById((Integer)sessionMap.get("userId"), User.class);
                } else {
                    user = userDAO.getModelById((String)sessionMap.get("userId"), User.class);
                }
                if (user != null) {
                    String rightCondition = null;
                    if (method.equals("search2") || method.equals("loadEditPage") || method.equals("populateDynamicActionSetup")) {
                        rightCondition = "application.retrieve_right = 'Y' and group_app.retrieve_right = 'Y'";
                    } else if (method.equals("loadAddPage") || method.equals("processInsert")) {
                        rightCondition = "application.create_right = 'Y' and group_app.create_right = 'Y'";
                    } else if (method.equals("delete")) {
                        rightCondition = "application.delete_right = 'Y' and group_app.delete_right = 'Y'";
                    } else if (method.equals("dynamicUpdate") || method.equals("processUpdate")) {
                        rightCondition = "application.update_right = 'Y' and group_app.update_right = 'Y'";
                    }
                    String rightSql = null;
                    if (rightCondition == null) { //not standard rights, need to check against additional rights
                        rightSql = "select count(*) from t_setup_group "
                                + "inner join t_setup_group_user group_user on t_setup_group.ug_id = group_user.ug_id "
                                + "inner join t_setup_user on t_setup_user.us_id = group_user.us_id "
                                + "inner join t_setup_group_app group_app on t_setup_group.ug_id = group_app.ug_id "
                                + "inner join t_setup_group_app_right gapp_right on gapp_right.ug_app_id = group_app.ug_app_id and gapp_right.hasright='Y' "
                                + "inner join t_setup_application application on application.application_id = group_app.application_id "
                                + "inner join t_setup_app_right app_right on application.application_id = app_right.application_id "
                                + "where (action_class = :paramAction and t_setup_user.us_id = '" + user.getID() + "' "
                                + "and (app_rights_methods = '" + method + "' || "
                                + "instr(app_rights_methods, '" + method + ",') > 0 || "
                                + "instr(app_rights_methods, ', " + method + ",') > 0 || "
                                + "instr(app_rights_methods, ', " + method + "') > 0 || "
                                + "instr(app_rights_methods, '," + method + ",') > 0 || "
                                + "instr(app_rights_methods, '," + method + "') > 0))";
                    } else { // standard rights
                        rightSql = "select count(*) from t_setup_group "
                                + "inner join t_setup_group_user group_user on t_setup_group.ug_id = group_user.ug_id "
                                + "inner join t_setup_user on t_setup_user.us_id = group_user.us_id "
                                + "inner join t_setup_group_app group_app on t_setup_group.ug_id = group_app.ug_id "
                                + "inner join t_setup_application application on application.application_id = group_app.application_id "
                                + "where (action_class = :paramAction and t_setup_user.us_id = '" + user.getID() + "' "
                                + "and " + rightCondition + ")";
                    }
                    Map rightParam = new HashMap();
                    rightParam.put("paramAction", actionClassName);
                    Debug.printFrameworkDebug(rightSql);
                    if (retrievingDAO.sqlCountRecord(rightSql, rightParam) > 0) { // rights Found
                        Debug.printFrameworkDebug("CF in here 1... new access right checking allow to invoke!!!");
                        return true;
                    } else {
                        Application app = new Application();
                        Map param = new HashMap();
                        param.put("action_class", actionClassName);
                        List list = retrievingDAO.list(param, Application.class);
                        String appName = "Unknown Application Code :" + actionClassName;
                        if (list != null) {
                            app = (Application) list.get(0);
                            if (app.getSystem_app().equalsIgnoreCase("Y") && user.getUs_admin().equalsIgnoreCase("Y")) {
                                Debug.printFrameworkDebug("CF in here 2... SYSTEM APP: new access right checking allow to invoke!!!");
                                return true;
                            }
                            appName = app.getApplication_name();
                        }
                        return false;
                    }
                } else {
                }
            }
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }
        return false;
    }

    public static Boolean validateRight_by_actionClass_appRightCode(String actionClassName, String appRightCode, Object userId, BaseDAO baseDAO) throws Exception {
        try {
            System.out.println("appRightCode = " + appRightCode);
            String rightSql = "select count(*) from t_setup_group "
                            + "inner join t_setup_group_user group_user on t_setup_group.ug_id = group_user.ug_id "
                            + "inner join t_setup_user on t_setup_user.us_id = group_user.us_id "
                            + "inner join t_setup_group_app group_app on t_setup_group.ug_id = group_app.ug_id "
                            + "inner join t_setup_group_app_right gapp_right on gapp_right.ug_app_id = group_app.ug_app_id and gapp_right.hasright='Y' "
                            + "inner join t_setup_app_right app_right on app_right.app_rights_id = gapp_right.app_rights_id "
                            + "inner join t_setup_application application on application.application_id = group_app.application_id "
                            + "where (action_class = :paramAction and t_setup_user.us_id = :usId "
                            + "and app_right.app_rights_code in (:appRightCode))";
            Map param = new HashMap();
            param.put("paramAction", actionClassName);
            param.put("appRightCode", appRightCode.split(","));
            param.put("usId", userId);
            return baseDAO.sqlCountRecord(rightSql, param) > 0;
        } catch (Exception e) {
        }
        return Boolean.FALSE;
    }
    public static Object getRightList_byActionAndAppRightCode(String actionClassName, String appRightCode, Map sessionMap, Session hibernateSession, String type) {
        List<ApplicationRights> list = null;
        try {
            UserDAOImpl userDAO = new UserDAOImpl();
            userDAO.setSession(hibernateSession);
            User user = null;
            if (ActionContext.getContext().getSession().get("userId") instanceof Integer) {
                user = userDAO.getModelById((Integer)sessionMap.get("userId"), User.class);
            } else {
                user = userDAO.getModelById((String)sessionMap.get("userId"), User.class);
            }
            if (user != null) {
                String rightSql = "select distinct app_right.* from t_setup_group "
                        + "inner join t_setup_group_user group_user on t_setup_group.ug_id = group_user.ug_id "
                        + "inner join t_setup_user on t_setup_user.us_id = group_user.us_id "
                        + "inner join t_setup_group_app group_app on t_setup_group.ug_id = group_app.ug_id "
                        + "inner join t_setup_group_app_right gapp_right on gapp_right.ug_app_id = group_app.ug_app_id and gapp_right.hasright='Y' "
                        + "inner join t_setup_app_right app_right on app_right.app_rights_id = gapp_right.app_rights_id "
                        + "inner join t_setup_application application on application.application_id = group_app.application_id "
                        + "where (action_class = :paramAction and t_setup_user.us_id = '" + user.getID() + "' "
                        + "and app_right.app_rights_code in (" + appRightCode + "))";
                SQLQuery query = hibernateSession.createSQLQuery(rightSql);
                query.setParameter("paramAction", actionClassName);
                query.addEntity(ApplicationRights.class);
                list = query.list();
                if (type.equals("Map")) {
                    Map map = new HashMap();
                    for (ApplicationRights appRight : list) {
                        map.put(appRight.getApp_rights_code(), appRight);
                    }
                    return map;
                }
            } else {
                //User by right should not be null...
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get from SNT- For PayBill
    public static String getNumber(String str) {
        char ch1;
        String strResult = "";

        try {
            for (int i = 0; i < str.length(); i++) {
                ch1 = str.charAt(i);
                if (Character.isDigit(ch1)) {
                    strResult += ch1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            strResult = "ERROR";
        }

        return strResult;
    }

    /**
     * Replace Special Character to HTML Code for & to %26 -- = to %3D //Please
     * add accordingly if u find any Character that need to be replaced.
     */
    public static String replaceToHTMLCode(String str) {
        return str.replace("&", "%26").replace("=", "%3D");
    }

    /* Validate Uploaded File Extension, throw Exception when is not valid format
     * Created by WongKK
     * Modified by ThoTH @ 26-Jul-2012
     */
    private Map<String, String> uploadFileBlockExtMap = null;  // ThoTH @ 26-Jul-2012

    public boolean validFileUploadExtension(String fileName, String allowExtension) throws Exception {
        String defaultAllowExt = "PNG,GIF,JPG,JPEG,TIF,BMP,PPT,PDF,PSD,XLS,XLSX,SWF,DOC,DOCX,ODT,ODC,ODP,ODG,MPP";
        if (uploadFileBlockExtMap == null) {  // Save processing power when multiple file uploaded same time
            uploadFileBlockExtMap = new HashMap();
            uploadFileBlockExtMap.put(".ade", "Microsoft Access project extension");
            uploadFileBlockExtMap.put(".adp", "Microsoft Access project");
            uploadFileBlockExtMap.put(".app", "Application file");
            uploadFileBlockExtMap.put(".asa", "ASP declarations file");
            uploadFileBlockExtMap.put(".ashx", "ASP.NET Web handler file. Web handlers are software modules that handle raw HTTP requests received by ASP.NET");
            uploadFileBlockExtMap.put(".asmx", "AASP.NET Web Services source file");
            uploadFileBlockExtMap.put(".asp", "Active Server Pages");
            uploadFileBlockExtMap.put(".bas", "Microsoft Visual Basic class module");
            uploadFileBlockExtMap.put(".bat", "Batch file");
            uploadFileBlockExtMap.put(".cdx", "Compound index");
            uploadFileBlockExtMap.put(".cer", "Certificate file");
            uploadFileBlockExtMap.put(".cgi", "cgi");
            uploadFileBlockExtMap.put(".chm", "Compiled HTML Help file");
            uploadFileBlockExtMap.put(".class", "Java class file");
            uploadFileBlockExtMap.put(".cmd", "Microsoft Windows NT command script");
            uploadFileBlockExtMap.put(".com", "Microsoft MS-DOS program");
            uploadFileBlockExtMap.put(".config", "Configuration file");
            uploadFileBlockExtMap.put(".cpl", "Control Panel extension");
            uploadFileBlockExtMap.put(".crt", "Security certificate");
            uploadFileBlockExtMap.put(".csh", "Script file");
            uploadFileBlockExtMap.put(".dll", "Windows dynamic link library");
            uploadFileBlockExtMap.put(".exe", "Program");
            uploadFileBlockExtMap.put(".fxp", "Microsoft Visual FoxPro compiled program");
            uploadFileBlockExtMap.put(".hlp", "CreatedHelp file");
            uploadFileBlockExtMap.put(".hta", "CreatedHTML program");
            uploadFileBlockExtMap.put(".htm", "");
            uploadFileBlockExtMap.put(".html", "");
            uploadFileBlockExtMap.put(".htr", "Script file");
            uploadFileBlockExtMap.put(".htw", "HTML document");
            uploadFileBlockExtMap.put(".ida", "Internet Information Services file");
            uploadFileBlockExtMap.put(".idc", "Internet database connector file");
            uploadFileBlockExtMap.put(".idq", "Internet data query file");
            uploadFileBlockExtMap.put(".ins", "Internet Naming Service");
            uploadFileBlockExtMap.put(".isp", "Internet Communication settings");
            uploadFileBlockExtMap.put(".its", "Internet Document Set file");
            uploadFileBlockExtMap.put(".jar", "");
            uploadFileBlockExtMap.put(".jse", "JScript Encoded script file");
            uploadFileBlockExtMap.put(".jsp", "");
            uploadFileBlockExtMap.put(".ksh", "Korn Shell script file");
            uploadFileBlockExtMap.put(".lnk", "Shortcut");
            uploadFileBlockExtMap.put(".mad", "Shortcut");
            uploadFileBlockExtMap.put(".maf", "Shortcut");
            uploadFileBlockExtMap.put(".mag", "Shortcut");
            uploadFileBlockExtMap.put(".mam", "Shortcut");
            uploadFileBlockExtMap.put(".maq", "Shortcut");
            uploadFileBlockExtMap.put(".mar", "Shortcut");
            uploadFileBlockExtMap.put(".mas", "Microsoft Access stored procedure");
            uploadFileBlockExtMap.put(".mat", "Shortcut");
            uploadFileBlockExtMap.put(".mau", "Shortcut");
            uploadFileBlockExtMap.put(".mav", "Shortcut");
            uploadFileBlockExtMap.put(".maw", "Shortcut");
            uploadFileBlockExtMap.put(".mda", "Microsoft Access add-in program");
            uploadFileBlockExtMap.put(".mdb", "Microsoft Access program");
            uploadFileBlockExtMap.put(".mde", "Microsoft Access MDE database");
            uploadFileBlockExtMap.put(".mdt", "Microsoft Access data file");
            uploadFileBlockExtMap.put(".mdw", "Microsoft Access workgroup");
            uploadFileBlockExtMap.put(".mdz", "Microsoft Access wizard program");
            uploadFileBlockExtMap.put(".msc", "Microsoft Common Console document");
            uploadFileBlockExtMap.put(".msh", "Microsoft Agent script helper");
            uploadFileBlockExtMap.put(".msh1", "Microsoft Agent script helper");
            uploadFileBlockExtMap.put(".msh1xml", "Microsoft Agent script helper");
            uploadFileBlockExtMap.put(".msh2", "Microsoft Agent script helper");
            uploadFileBlockExtMap.put(".msh2xml", "Microsoft Agent script helper");
            uploadFileBlockExtMap.put(".mshxml", "Microsoft Agent script helper");
            uploadFileBlockExtMap.put(".msi", "Microsoft Windows Installer package");
            uploadFileBlockExtMap.put(".msp", "Windows Installer patch package file");
            uploadFileBlockExtMap.put(".mst", "Visual Test source files");
            uploadFileBlockExtMap.put(".ops", "Microsoft Office profile settings file");
            uploadFileBlockExtMap.put(".pcd", "Photo CD image or Microsoft Visual Test compiled script");
            uploadFileBlockExtMap.put(".php", "");
            uploadFileBlockExtMap.put(".php3", "");
            uploadFileBlockExtMap.put(".php4", "");
            uploadFileBlockExtMap.put(".phtml", "");
            uploadFileBlockExtMap.put(".pif", "Shortcut to MS-DOS program");
            uploadFileBlockExtMap.put(".pl", "");
            uploadFileBlockExtMap.put(".prf", "System file");
            uploadFileBlockExtMap.put(".prg", "Program source file");
            uploadFileBlockExtMap.put(".printer", "Printer file");
            uploadFileBlockExtMap.put(".pst", "Microsoft Outlook personal folder file");
            uploadFileBlockExtMap.put(".py", "");
            uploadFileBlockExtMap.put(".reg", "Registration entries");
            uploadFileBlockExtMap.put(".rem", "ACT! database maintenance file");
            uploadFileBlockExtMap.put(".scf", "Windows Explorer command file");
            uploadFileBlockExtMap.put(".scr", "Screen saver");
            uploadFileBlockExtMap.put(".sct", "Script file");
            uploadFileBlockExtMap.put(".sh", "");
            uploadFileBlockExtMap.put(".shb", "Windows shortcut");
            uploadFileBlockExtMap.put(".shs", "Shell Scrap object");
            uploadFileBlockExtMap.put(".shtm", "HTML file that contains server side directives");
            uploadFileBlockExtMap.put(".shtml", "HTML file that contains server side directives");
            uploadFileBlockExtMap.put(".soap", "Simple Object Access Protocol file");
            uploadFileBlockExtMap.put(".stm", "HTML file that contains server side directives");
            uploadFileBlockExtMap.put(".url", "Uniform Resource Locator (Internet shortcut)");
            uploadFileBlockExtMap.put(".vb", "Microsoft Visual Basic Scripting Edition (Visual Basic Scripting Edition (VBScript)) file");
            uploadFileBlockExtMap.put(".vbe", "VBScript Encoded Script file");
            uploadFileBlockExtMap.put(".vbs", "VBScript file");
            uploadFileBlockExtMap.put(".ws", "Windows Script file");
            uploadFileBlockExtMap.put(".wsc", "Windows Script Component");
            uploadFileBlockExtMap.put(".wsf", "Windows Script file");
            uploadFileBlockExtMap.put(".wsh", "Windows Script Host settings file");
        }
        try {
            // Check Block Extension and To avoid double Extension like IamVirus.exe.jpg
            fileName = fileName.toLowerCase();
            for (String blockExt : uploadFileBlockExtMap.keySet()) {
                if (fileName.indexOf(blockExt) > 0) {
                    throw new CustomBaseException("errors.invalidFileUploadExtension", fileName, blockExt.substring(1).toUpperCase());
                }
            }

            // Check Allowed Extension
            if (Validator.isEmpty(allowExtension)) {
                allowExtension = defaultAllowExt;
            } else {
                allowExtension = allowExtension.toUpperCase();
            }

            int fileExtIdx = fileName.lastIndexOf(".");
            int fileNLength = fileName.length();
            String fileExtension = fileName.substring(fileExtIdx + 1, fileNLength).toUpperCase();

            if (allowExtension.indexOf(fileExtension) == -1) {
                throw new CustomBaseException("errors.invalidFileUploadExtension", fileName, fileExtension);
            }
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    //Delvene @ 15-Apr-2013 :: Calculate Age with Month (Edited @ 05-Jun-2013 :: Manual calculation replaced with Joda DateTime)
    public String getAgeWithMonth(java.sql.Timestamp emp_dob) {
        DateTime today = DateTime.now();
        DateTime dob = new DateTime(emp_dob);
        String strAgeWithMonth = "";

        if (emp_dob != null) {
            Period age = new Period(dob, today);
            strAgeWithMonth = as.getText("PR.common.ageCount", new String[]{Integer.toString(age.getYears()), Integer.toString(age.getMonths())});
        }

        return strAgeWithMonth;
    }
    //Delvene @ 15-Apr-2013 :: Calculate Age with Month (Edited @ 05-Jun-2013 :: Manual calculation replaced with Joda DateTime) - END

    //Delvene @ 10-Jun-2013 :: Calculate Age with Month between two dates
    public String getAgeBtwDates(java.sql.Timestamp early_dt, java.sql.Timestamp later_dt) {
        DateTime earlyDate = new DateTime(early_dt);
        DateTime laterDate = new DateTime(later_dt);
        String strAgeBtwDates = "";

        if (early_dt != null && later_dt != null) {
            Period age = new Period(earlyDate, laterDate);
            strAgeBtwDates = as.getText("PR.common.ageCount", new String[]{Integer.toString(age.getYears()), Integer.toString(age.getMonths())});
        }

        return strAgeBtwDates;
    }

    //Zhafari @ 20-Aug-2013 :: Calculate days between two dates
    public Double getDaysBtwDates(java.sql.Timestamp early_dt, java.sql.Timestamp later_dt) {
        DateTime earlyDate = new DateTime(early_dt);
        DateTime laterDate = new DateTime(later_dt);
        Days d = Days.daysBetween(earlyDate, laterDate);
        int days = d.getDays() + 1; //+1 because early_dt is included as day 1
        return (double) days;
    }
    //Zhafari @ 20-Aug-2013 :: Calculate days between two dates - END

    //Thensw @ 08-Oct-2013 :: move security condition from DR to here, for reuse purpose
    // ThoTH @ 4-Mar-2013 :: Default Security Access
    // ThoTH @ 26-Jun-2013 :: Use impianSecurityType to replace sqlTable
    // ThoTH @ 6-Aug-2013 :: Only use BU for Security, since someon's BA can under a Department which is not Exist in Real. e.g. Pentadbiran Am
//    public static String appendSecurityCondition(String securityType, String condition, String conditionLocation) throws Exception {
//        String strSql = "";
//        Integer intSecuAL  = (Integer) ActionContext.getContext().getSession().get("secuAL");
//        String strSecuBA = (String) ActionContext.getContext().getSession().get("secuBA");
//        String strSecuDeptBA = (String) ActionContext.getContext().getSession().get("secuDeptBA");
//        String strSecuDivBA = (String) ActionContext.getContext().getSession().get("secuDivBA");
//        String strSecuBU = (String) ActionContext.getContext().getSession().get("secuBU");
//        String strSecuDeptBU = (String) ActionContext.getContext().getSession().get("secuDeptBU");
//        String strSecuDivBU = (String) ActionContext.getContext().getSession().get("secuDivBU");
////        System.out.println("appendSecurityCondition: securityType: " + securityType + " >>>> intSecuAL:"+intSecuAL );
//
//        String strOperator = "";
//        if (conditionLocation.equals(SecurityConditionLocation.DataRetriver)) {
//            if (condition == null || condition.indexOf("where") < 0) {
//                strOperator = " where ";
//            } else {
//                strOperator = " and ";
//            }
//        }
//
//        if (securityType.equalsIgnoreCase("SetupUser")) {
////        if (sqlTable.equalsIgnoreCase("t_setup_user")) {
//            if (intSecuAL == 8) {
//                strSql = strOperator + "exists (select su.us_id from t_setup_user su    where 8 >= t_setup_user.us_accesslevel) ";
//
//            } else if (intSecuAL < 8) {
//                strSql = strOperator + "exists (  select us_id from t_setup_user su, t_cm_personal_post bu, t_pt_post_operation oper, t_pt_establishment est   " +
//                       "                  where su.emp_id = bu.employee_id and bu.post_oper_id = oper.post_oper_id" +
//                       "                    and oper.est_id = est.est_id     " +
//                       "                    and est.est_dept_id = '"+strSecuDeptBU+"' " +
//                       "                    and t_setup_user.us_id = su.us_id)" ;
//                // Commented by ThoTH @ 25-Apr-2013 :: No more using t_pt_post_lokasi
////                strSql = strOperator + "exists (  select us_id from t_setup_user su, t_pt_post_lokasi bu, t_pt_establishment est   " +
////                       "                  where su.emp_id = bu.emp_id and bu.est_id_bu = est.est_id     " +
////                       "                    and est.est_dept_id = '"+strSecuDept+"' " +
////                       "                    and t_setup_user.us_id = su.us_id)" ;
//            }
//
//        } else if (securityType.equalsIgnoreCase("SetupUserGroup")) {
////        } else if (sqlTable.equalsIgnoreCase("t_setup_group userGroup")) {
//            if (intSecuAL == 8) {
//                strSql = strOperator + "not isnull(dept_id, '') = '' ";
//
//            } else if (intSecuAL < 8) {
//                strSql = strOperator + "dept_id = '"+strSecuDeptBU+"' ";
//            }
//
//        } else if (securityType.equalsIgnoreCase("MOHONAN")) {
//            if (intSecuAL < 8) {
//                strSql = strOperator + "((app.dept_id = '"+strSecuDeptBU+"' and app.app_type = '"+PtApplicationModel.APPLICATION_TYPE.NewPostUnderWarrant+"') OR (app.dept_id = '"+strSecuDeptBU+"' and app.app_type != '"+PtApplicationModel.APPLICATION_TYPE.NewPostUnderWarrant+"')) ";
////                strSql = strOperator + "((app.dept_id = '"+strSecuDeptBA+"' and app.app_type = '"+PtApplicationModel.APPLICATION_TYPE.NewPostUnderWarrant+"') OR (app.dept_id = '"+strSecuDeptBU+"' and app.app_type != '"+PtApplicationModel.APPLICATION_TYPE.NewPostUnderWarrant+"')) ";
//            }
//        } else if (securityType.equalsIgnoreCase("PostInfo")) {
//            if (intSecuAL == 7) {  // HQ
//                strSql = strOperator + "exists (select innerBA.est_dept_id from t_pt_establishment innerBA " +
//                        "                        where innerBA.est_id = post.est_id " +
//                        "                          and innerBA.est_dept_id = '"+strSecuDeptBU+"')" ;
////                        "                          and innerBA.est_dept_id = '"+strSecuDeptBA+"')" ;
//            } else if (intSecuAL == 6) { // Station
//                strSql = strOperator + "exists (select innerBA.est_dept_id from t_pt_establishment innerBA " +
//                        "                        where innerBA.est_id = post.est_id " +
//                        "                          and innerBA.est_dept_id = '"+strSecuDeptBU+"' " +
//                        "                          and innerBA.est_id = '"+strSecuBU+"') " ;
////                        "                          and innerBA.est_dept_id = '"+strSecuDeptBA+"' " +
////                        "                          and innerBA.est_id = '"+strSecuBA+"') " ;
//
//            } else if (intSecuAL == 5) { // Division
//                strSql = strOperator + "exists (select innerBA.est_dept_id from t_pt_establishment innerBA inner join t_pt_station innerStt " +
//                        "                   on innerBA.est_station_id = innerStt.station_id " +
//                        "                where innerBA.est_id = post.est_id" +
//                        "                  and innerBA.est_dept_id = '"+strSecuDeptBU+"' and innerStt.station_div_id = '"+strSecuDivBU+"') " ;
////                        "                  and innerBA.est_dept_id = '"+strSecuDeptBA+"' and innerStt.station_div_id = '"+strSecuDivBA+"') " ;
//            }
//        } else if (securityType.equalsIgnoreCase("PostOperation")) {
//            if (intSecuAL == 7) {  // HQ
//                strSql = strOperator + "exists (select innerBU.est_dept_id from t_pt_establishment innerBU " +
//                        "                        where innerBU.est_id = post.est_id " +
//                        "                          and innerBU.est_dept_id = '"+strSecuDeptBU+"')" ;
//            } else if (intSecuAL == 6) { // Station
//                strSql = strOperator + "exists (select innerBU.est_dept_id from t_pt_establishment innerBU " +
//                        "                        where innerBU.est_id = post.est_id " +
//                        "                          and innerBU.est_dept_id = '"+strSecuDeptBU+"' " +
//                        "                          and innerBU.est_id = '"+strSecuBU+"') " ;
//
//            } else if (intSecuAL == 5) { // Division
//                strSql = strOperator + "exists (select innerBU.est_dept_id from t_pt_establishment innerBU inner join t_pt_station innerStt " +
//                        "                   on innerBU.est_station_id = innerStt.station_id " +
//                        "                where innerBU.est_id = post.est_id" +
//                        "                  and innerBU.est_dept_id = '"+strSecuDeptBU+"' and innerStt.station_div_id = '"+strSecuDivBU+"') " ;
//            }
//        } else if (securityType.equalsIgnoreCase("PostHolder")) {
//            if (intSecuAL == 7) {  // HQ
//                strSql = strOperator + "exists (select innerBU.est_dept_id from t_pt_establishment innerBU " +
//                        "                        where innerBU.est_id = bu.est_id " +
//                        "                          and innerBU.est_dept_id = '"+strSecuDeptBU+"')" ;
//            } else if (intSecuAL == 6) { // Station
//                strSql = strOperator + "exists (select innerBU.est_dept_id from t_pt_establishment innerBU " +
//                        "                        where innerBU.est_id = bu.est_id " +
//                        "                          and innerBU.est_dept_id = '"+strSecuDeptBU+"' " +
//                        "                          and innerBU.est_id = '"+strSecuBU+"') " ;
//
//            } else if (intSecuAL == 5) { // Division
//                strSql = strOperator + "exists (select innerBU.est_dept_id from t_pt_establishment innerBU inner join t_pt_station innerStt " +
//                        "                   on innerBU.est_station_id = innerStt.station_id " +
//                        "                where innerBU.est_id = bu.est_id" +
//                        "                  and innerBU.est_dept_id = '"+strSecuDeptBU+"' and innerStt.station_div_id = '"+strSecuDivBU+"') " ;
//            }
//        }
//
//        return strSql;
//    }
    private static Map<String, List<String>> byPassMap = null;
    private static Boolean debugMode = Boolean.TRUE;
//    public boolean validateRight_sql(String actionClassName, String method, Map sessionMap, String paramAction, Session hibernateSession) throws Exception {
//        //System.out.println("in checking access right....");
////        System.out.println("validate right");
//        //session_userKey = "userId";
//        //wongkk4@26Dec2013
//        if(sessionMap.get("login_user_type") == SystemConstants.SF_SYSTEM_TYPE.PUBLIC){
//            session_userKey = "userId";
//        }else{
//            session_userKey = "userId";
//        }
//
//        String notAuthorisedCode = "not_authorised";
//        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//        String errorMessage = null;
////        String byPassAction = new ActionSupport().getText("bypass.action");
////        StringTokenizer stActions = new StringTokenizer(byPassAction, ",");
//        UserDAOImpl userDAO = new UserDAOImpl();
//        userDAO.setSession(hibernateSession);
//        BaseDAO retrievingDAO = new BaseDAOImpl();
//        retrievingDAO.setSession(hibernateSession);
//        ApplicationDAOImpl applicationDAO = new ApplicationDAOImpl();
//        applicationDAO.setSession(hibernateSession);
//        User user = new User();
//
//        if (byPassMap == null) {
//            byPassMap = new HashMap();
//            List includeAllList = new ArrayList();
//            includeAllList.add("-all-");
//            String byPassAction = new ActionSupport().getText("bypass.action");
//
//            if (!Validator.isEmpty(byPassAction)) {
//                String[] stActions = byPassAction.split(",");
//                for (String action : stActions) {
//                    if (action.indexOf(";") > 0) {
//                        List list = new ArrayList();
//                        String[] splitedStr = action.split(";");
//                        if (splitedStr[1].indexOf(":") > 0) {
//                            String[] actions = splitedStr[1].split(":");
//                            list.add("-" + actions[0].trim() + "-");
//                            StringTokenizer st = new StringTokenizer(actions[1], new String("|"));
//                            while (st.hasMoreTokens()) {
//                                list.add(st.nextToken().trim());
//                            }
//                            byPassMap.put(splitedStr[0], list); // by pass all methods
//                        } else {
//                            byPassMap.put(splitedStr[0], includeAllList); // by pass all methods
//                        }
//                    } else {
//                        byPassMap.put(action, includeAllList); // by pass all methods
//                    }
//                }
//            }
//        }
//
//        String hc = null;
//        String query = null;
//        List<String> tempList = null;
//        if (actionClassName.equals("LookupAction")){
//            tempList = byPassMap.get(request.getParameter("LAN_"));
//        } else {
//            tempList = byPassMap.get(actionClassName);
//        }
//        if (tempList != null) {
//            if (actionClassName.equals("RegistrationAction")) {
//                return Boolean.TRUE;
//            }
//
//            if (tempList.get(0).equals("-all-")
//                    || (tempList.get(0).equals("-include-") && tempList.contains(method))
//                    || (tempList.get(0).equals("-exclude-") && !tempList.contains(method))) {
//                return Boolean.TRUE;
//            }
//        }
//
//        if (actionClassName.equals("LookupAction")){
//            Boolean isLookup = Boolean.FALSE;
//            String publicStr = "";
//            if (method.equals("retrieveData")){
//                return Boolean.TRUE;
//            }
//            isLookup = Boolean.TRUE;
//            actionClassName = request.getParameter("LAN_"); //lookup Action Name
//            hc = request.getParameter("teaC_"); //lookup Action Name
//            query = request.getParameter("query");
//            if (!Validator.isEmpty(hc) && !"undefined".equals(hc)) {
//                if (hc.equals(new LookupAction().getHc(actionClassName, query))) {
//                    retrievingDAO.setSession(userDAO.getSession());
//                    String sql = "select count(*) from t_setup_group_app"+ publicStr +" " +
//                        "inner join t_setup_application"+ publicStr +" on " +
//                        "t_setup_group_app"+ publicStr +".application_id = t_setup_application"+ publicStr +".application_id " +
//                        "and t_setup_group_app"+ publicStr +".retrieve_right = 'Y' and action_class='"+ actionClassName +"' " +
//                        "inner join t_setup_group"+ publicStr +" on t_setup_group"+ publicStr +".ug_id = t_setup_group_app"+ publicStr +".ug_id " +
//                        "inner join t_setup_group_user"+ publicStr +" on t_setup_group"+ publicStr +".ug_id = t_setup_group_user"+ publicStr +".ug_id " +
//                        "and t_setup_group_user"+ publicStr +".us_id = '"+ sessionMap.get(session_userKey) +"'";
//                    Long count = retrievingDAO.sqlCountRecord(sql);
//                    if (count > 0){
//                        return Boolean.TRUE;
//                    }
//                    sessionMap.put("not_authorised", "You are not allow to perform this lookup");
//                    sessionMap.put("isLookup", "yes");
//                    return Boolean.FALSE;
//                } else {
//                    sessionMap.put("isLookup", "yes");
//                    sessionMap.put("not_authorised", "You are not allow to perform this lookup");
//                    return Boolean.FALSE;
//                }
//            } else {
//                if (debugMode) {
//                    System.out.println("################################");
//                    System.out.println("# No check lookup access right #");
//                    System.out.println("################################");
//                }
//                return Boolean.TRUE;
//            }
//        }
//
////        while (stActions.hasMoreTokens()) {
////            String temp = stActions.nextToken();
////            if (!(temp.equals("DynamicAction"))) {
////                if (actionClassName.equals(temp)) {
////                    return true;
////                }
////            }
////        }
//
////        if (sessionMap.get(session_userKey) == null) {
////            return false;
////        }
//
//        try {
//            //System.out.println("Common Function Dynamic Action");
//            if (actionClassName.equals("DynamicAction")) {
//                Map param = new HashMap();
//                String userId = (String) sessionMap.get(session_userKey);
//                //param.put("us_user_id", userId);
//                //System.out.println("Common Function Dynamic Action userId "+ userId);
//                //System.out.println("method " + method);
//                //System.out.println("session_userKey " + session_userKey);
//                if (session_userKey.startsWith("p_")) {
//                    user = userDAO.getModelById(userId, user);
//                } else {
//                    user = userDAO.getModelById(userId, user);
//                }
//                if (user != null) {
//                    String rightCondition = null;
//                    if (method.equals("search2") || method.equals("loadEditPage") || method.equals("populateDynamicActionSetup")) {
//                        rightCondition = "application.retrieve_right = 'Y' and group_app.retrieve_right = 'Y'";
//                        errorMessage = "You are not authorised to SEARCH RECORD in ";
//                    } else if (method.equals("loadAddPage") || method.equals("processInsert")) {
//                        rightCondition = "application.create_right = 'Y' and group_app.create_right = 'Y'";
//                        errorMessage = "You are not authorised to CREATE RECORD in ";
//                    } else if (method.equals("delete")) {
//                        rightCondition = "application.delete_right = 'Y' and group_app.delete_right = 'Y'";
//                        errorMessage = "You are not authorised to DELETE RECORD in ";
//                    } else if (method.equals("processUpdate")) {
//                        rightCondition = "application.update_right = 'Y' and group_app.update_right = 'Y'";
//                        errorMessage = "You are not authorised to UPDATE RECORD in ";
//                    }
//                    String rightSql = "select count(*) from t_setup_group " +
//                        "inner join t_setup_group_user group_user on t_setup_group.ug_id = group_user.ug_id " +
//                        "inner join t_setup_user on t_setup_user.us_id = group_user.us_id " +
//                        "inner join t_setup_group_app group_app on t_setup_group.ug_id = group_app.ug_id " +
//                        "inner join t_setup_application application on application.application_id = group_app.application_id " +
//                        "where (application_code = '"+ paramAction.replaceAll("'", "''") +"' and t_setup_user.us_id = '"+ user.getID() +"' " +
//                        "and "+ rightCondition +")";
//                    //System.out.println("Common Function = " + rightSql);
//                    if (retrievingDAO.sqlCountRecord(rightSql) > 0) { // rights Found
//                        return true;
//                    }
////                    else if(sessionMap.get("login_user_type") == SystemConstants.SF_SYSTEM_TYPE.PUBLIC){
////                        return true;
////                    }
//                    else {
//                        Application app = (Application) retrievingDAO.getModelByCode("application_code", paramAction, new Application());
//                        sessionMap.remove(notAuthorisedCode);
//                        sessionMap.put(notAuthorisedCode, errorMessage + app.getAction_name());
//                        retrievingDAO.getSession().evict(app);
//                        return false;
//                    }
//                }
//            } else {
//                //no need to check authorisation if the method is cancel
//                if (method.equalsIgnoreCase("cancel")) {
//                    return true;
//                }
//
//                String userId = (String) sessionMap.get("userId");
//                //param.put("us_user_id", userId);
//                user = userDAO.getModelById(userId, user);
//                if (user != null) {
//                    String rightCondition = null;
//                            if (method.equals("search2") || method.equals("loadEditPage") || method.equals("populateDynamicActionSetup")) {
//                                errorMessage = "You are not authorised to SEARCH/RETRIEVE RECORD in ";
//                                rightCondition = "application.retrieve_right = 'Y' and group_app.retrieve_right = 'Y'";
//                            } else if (method.equals("loadAddPage") || method.equals("processInsert")) {
//                                errorMessage = "You are not authorised to CREATE RECORD in ";
//                                rightCondition = "application.create_right = 'Y' and group_app.create_right = 'Y'";
//                            } else if (method.equals("delete")) {
//                                errorMessage = "You are not authorised to DELETE RECORD in ";
//                                rightCondition = "application.delete_right = 'Y' and group_app.delete_right = 'Y'";
//                            } else if (method.equals("processUpdate")) {
//                                errorMessage = "You are not authorised to UPDATE RECORD in ";
//                                rightCondition = "application.update_right = 'Y' and group_app.update_right = 'Y'";
//                            }
//                            String rightSql = null;
//                            if (rightCondition == null) { //not standard rights, need to check against additional rights
//                                errorMessage = "You are not authorised to perform "+ method +" in ";
//                                rightSql = "select count(*) from t_setup_group " +
//                                    "inner join t_setup_group_user group_user on t_setup_group.ug_id = group_user.ug_id " +
//                                    "inner join t_setup_user on t_setup_user.us_id = group_user.us_id " +
//                                    "inner join t_setup_group_app group_app on t_setup_group.ug_id = group_app.ug_id " +
//                                    "inner join t_setup_application application on application.application_id = group_app.application_id " +
//                                    "inner join t_setup_app_right app_right on application.application_id = app_right.application_id " +
//                                    "inner join t_setup_app_right_method right_method on right_method.app_rights_id = app_right.app_rights_id " +
//                                    "where (action_class = '"+ actionClassName.replaceAll("'", "''") +"' and t_setup_user.us_id = '"+ user.getID() +"' " +
//                                    "and (method_str = '"+ method.replaceAll("'", "''") +"'))";
//                            } else { // standard rights
//                                rightSql = "select count(*) from t_setup_group " +
//                                    "inner join t_setup_group_user group_user on t_setup_group.ug_id = group_user.ug_id " +
//                                    "inner join t_setup_user on t_setup_user.us_id = group_user.us_id " +
//                                    "inner join t_setup_group_app group_app on t_setup_group.ug_id = group_app.ug_id " +
//                                    "inner join t_setup_application application on application.application_id = group_app.application_id " +
//                                    "where (action_class = '"+ actionClassName.replaceAll("'", "''") +"' and t_setup_user.us_id = '"+ user.getID() +"' " +
//                                    "and "+ rightCondition +")";
//                            }
////                            System.out.println("Common Function wongkk4 =" + rightSql);
//                            if (retrievingDAO.sqlCountRecord(rightSql) > 0) { // rights Found
////                                System.out.println("CF in here 1... new access right checking allow to invoke!!!");
//                                return true;
//                            } else {
//
////                                System.out.println("CF in here 2... no access right checking allow to invoke!!!");
//                                Application app = new Application();
//                                Map param = new HashMap();
//                                param.put("action_class", actionClassName);
//                                List list = retrievingDAO.list(param, app);
//                                String appName = "Unknown Action Class :" + actionClassName;
//                                if (list != null){
//                                    app = (Application) list.get(0);
//                                    if (app.getSystem_app().equalsIgnoreCase("Y") && user.getUs_admin().equalsIgnoreCase("Y")){
////                                        System.out.println("CF in here 2... SYSTEM APP: new access right checking allow to invoke!!!");
//                                        return true;
//                                    }
//                                    appName = app.getApplication_name();
//                                }
////                                System.out.println("CF appName " + appName);
//                                sessionMap.remove(notAuthorisedCode);
//                                if (appName.startsWith("Unknown Action Class")) {
//                                    sessionMap.put(notAuthorisedCode, "Not a stardard method and not setup in application's additional right. Access Denied.");
//                                } else {
//                                    sessionMap.put(notAuthorisedCode, errorMessage + appName);
//                                }
//                                return false;
//                            }
//                } else {
//                }
//            }
//        } catch (Exception e) {
//            new LogFunction().logError(this.getClass(), "", e);
//            sessionMap.put(notAuthorisedCode, "Not a stardard method and not setup in application's additional right. Access Denied.");
//        }
//        sessionMap.remove(notAuthorisedCode);
//        sessionMap.put(notAuthorisedCode, "Unknown Access");
//
//        return false;
//    }
//   protected void auditLogin(String loginType, String loginStatus, String loginId, BaseDAO basedao) {
////        BaseDAO<AuditLoginModel> basedao = new BaseDAOImpl();
//        try {
//            AuditLoginModel auditLogin = new AuditLoginModel();
//
//            auditLogin.setID(CommonFunction.getId(20));
//            auditLogin.setLogintype(loginType);
//            auditLogin.setLoginstatus(loginStatus);
//            auditLogin.setLoginid(loginId);
//            auditLogin.setLogintime(DateUtil.getCurrentTimestamp());
//
//            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//            auditLogin.setLoginip((String)request.getRemoteAddr());
//
//            //basedao.insert(auditLogin);
//            auditLogin.setID(com.sains.framework.base.CommonFunction.getId(CommonFunction.PK_LENGTH));
//            basedao.beginBatchTransaction();
//            basedao.getSession().save(auditLogin);
//            basedao.commitBatchTransaction();
//        } catch (Exception e) {
//            basedao.rollbackBatchTransaction();
//            new LogFunction().logError(this.getClass(), "", e);
////        } finally {
////            basedao.closeSession();
//        }
//    }

    public void auditLogin(String loginAction, String loginType, String loginStatus, String loginId) {
//    protected void auditLogin(String loginType, String loginStatus, String loginId, BaseDAO basedao) {
System.out.println("login logout..." + loginId);
        // Not need log if the loginId is empty
        BaseDAO<AuditLoginModel> basedao = new BaseDAOImpl();
        try {
            if (loginAction.equals(SystemConstants.AUDIT_LOGIN.ACTION.LOGOUT)) {
                loginId = (String) ActionContext.getContext().getSession().get("loginId");
                if (Validator.isEmpty(loginId)) {
                    return;
                }
            }

            AuditLoginModel auditLogin = new AuditLoginModel();

//            auditLogin.setID(CommonFunction.getId(20));
            auditLogin.setLoginaction(loginAction);
            auditLogin.setLogintype(loginType);
            auditLogin.setLoginstatus(loginStatus);
            auditLogin.setLoginid(loginId);
            auditLogin.setLogintime(DateUtil.getCurrentTimestamp());
            auditLogin.setSystem_id("ESUB");

            //ThOTH @ 17-Feb-2014  :: is client behind something?
            String ipAddress = null;
            try {
                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                ipAddress = request.getHeader("X-FORWARDED-FOR");
                if (ipAddress == null) {
                    ipAddress = request.getRemoteAddr();
                }
            } catch (Exception e) {
            }
            auditLogin.setLoginip(ipAddress);
//            auditLogin.setLoginip((String)request.getRemoteAddr());

            //basedao.insert(auditLogin);
            if (!ModelBase.isIntegerPk(auditLogin.getClass())) {
                auditLogin.setID(getId(CommonFunction.PK_LENGTH));
            }
            basedao.beginBatchTransaction();
            basedao.getSession().save(auditLogin);

            basedao.commitBatchTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            basedao.rollbackBatchTransaction();
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
            basedao.closeSession();
        }
    }

    public List getListFromSqlWithSession(org.hibernate.Session session, String strSql, Map<Integer, String> param) throws Exception {
        List listResult = new ArrayList();

        org.hibernate.SQLQuery query = null;
        try {
            query = session.createSQLQuery(strSql);
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            if (param != null) {  // ThoTH @ 21-Mar-2014
                for (int i = 1; i <= param.size(); i++) {
                    query.setString(i, param.get(i).toString());
                }
            }
            listResult = query.list();
        } catch (Exception e) {
            throw e;
        }
        return listResult;
    }

    public void auditLogin(String loginAction, String loginType, String loginStatus, String loginId, String loginIp) {
// protected void auditLogin(String loginType, String loginStatus, String loginId, BaseDAO basedao) {
        Debug.printFrameworkDebug("CF Audit Login =" + loginId);
// Not need log if the loginId is empty
        //System.out.println("bf sessionMap ");
        //Map sessionMap = ActionContext.getContext().getSession();
        //System.out.println("af sessionMap ");
        BaseDAO<AuditLoginModel> basedao = new BaseDAOImpl();
        try {
            Debug.printFrameworkDebug("loginAction " + loginAction);
            if (loginAction.equals(SystemConstants.AUDIT_LOGIN.ACTION.LOGOUT)) {
                loginId = (String) ActionContext.getContext().getSession().get("loginId");
                Debug.printFrameworkDebug("loginId 2" + loginId);
                if (Validator.isEmpty(loginId)) {
                    return;
                }
            }

            AuditLoginModel auditLogin = new AuditLoginModel();

            auditLogin.setID(CommonFunction.getId(20));
            auditLogin.setLoginaction(loginAction);
            auditLogin.setLogintype(loginType);
            auditLogin.setLoginstatus(loginStatus);
            auditLogin.setLoginid(loginId);
            auditLogin.setLogintime(DateUtil.getCurrentTimestamp());

//ThOTH @ 17-Feb-2014 :: is client behind something?
            String ipAddress = null;
            try {
                HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
                ipAddress = request.getHeader("X-FORWARDED-FOR");
                if (ipAddress == null) {
                    ipAddress = request.getRemoteAddr();
                }
                if (ipAddress == null) {
                    ipAddress = loginIp;
                }

            } catch (Exception e) {
                Debug.printError("getIp" + e.getMessage());
                ipAddress = loginIp;
//                System.out.println("ipAddress error " + (String) ActionContext.getContext().getSession().get("loginIp").toString());
            }
            auditLogin.setLoginip(ipAddress);
// auditLogin.setLoginip((String)request.getRemoteAddr());

//basedao.insert(auditLogin);
            auditLogin.setID(com.sains.framework.base.CommonFunction.getId(CommonFunction.PK_LENGTH));
            basedao.beginBatchTransaction();
            basedao.getSession().save(auditLogin);
            basedao.commitBatchTransaction();
        } catch (Exception e) {
            basedao.rollbackBatchTransaction();
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
            basedao.closeSession();
        }
    }

    public boolean validateRight_sql(String actionClassName, String method, Map sessionMap, String paramAction, Session hibernateSession) throws Exception {
        return validateRight_sql(actionClassName, method, sessionMap, paramAction, hibernateSession, Boolean.FALSE);
    }

    public boolean validateRight_sql(String actionClassName, String method, Map sessionMap, String paramAction, Session hibernateSession, Boolean checkIsViewer) throws Exception {
        Debug.printFrameworkDebug("paramAction = " + paramAction + ", method = " + method);
        if (paramAction != null && paramAction.indexOf("__") > 0) {
            paramAction = paramAction.substring(0, paramAction.indexOf("__"));
        }
//        System.out.println("validate right");
        session_userKey = "userId";
        String notAuthorisedCode = "not_authorised";
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String errorMessage = null;

        String hc = null;
        String query = null;
        UserDAOImpl userDAO = new UserDAOImpl();
        Boolean closeTheSession = Boolean.FALSE;
        if (hibernateSession == null) {
            closeTheSession = Boolean.TRUE;
            hibernateSession = userDAO.getSession();
        }
        userDAO.setSession(hibernateSession);
        BaseDAO retrievingDAO = new BaseDAOImpl();
        retrievingDAO.setSession(hibernateSession);
        ApplicationDAOImpl applicationDAO = new ApplicationDAOImpl();
        applicationDAO.setSession(hibernateSession);
        User user = null;
        try {
            String usingActionOrClass = null;
            if (actionClassName.equals("LookupAction")) {
                Boolean isLookup = Boolean.FALSE;
                String publicStr = "";
                if (method.equals("retrieveData")) {
                    return Boolean.TRUE;
                }
                isLookup = Boolean.TRUE;
                actionClassName = request.getParameter("LAN_"); //lookup Action Name
                hc = request.getParameter("teaC_"); //lookup Action Name
                query = request.getParameter("query");
                if (!Validator.isEmpty(hc) && !"undefined".equals(hc)) {
                    if (hc.equals(new LookupAction().getHc(actionClassName, query))) {
                        String sql = "select count(*) from t_setup_group_app" + publicStr + " "
                                + "inner join t_setup_application" + publicStr + " on "
                                + "t_setup_group_app" + publicStr + ".application_id = t_setup_application" + publicStr + ".application_id "
                                + "and t_setup_group_app" + publicStr + ".retrieve_right = 'Y' and action_class='" + actionClassName + "' "
                                + "inner join t_setup_group" + publicStr + " on t_setup_group" + publicStr + ".ug_id = t_setup_group_app" + publicStr + ".ug_id "
                                + "inner join t_setup_group_user" + publicStr + " on t_setup_group" + publicStr + ".ug_id = t_setup_group_user" + publicStr + ".ug_id "
                                + "and t_setup_group_user" + publicStr + ".us_id = '" + sessionMap.get(session_userKey) + "'";
                        System.out.println("sql = " + sql);
                        Long count = retrievingDAO.sqlCountRecord(sql, null);
                        if (count > 0) {
                            return Boolean.TRUE;
                        }
                        request.setAttribute("not_authorised", "You are not allow to perform this lookup");
                        request.setAttribute("isLookup", "yes");
                        return Boolean.FALSE;
                    } else {
                        request.setAttribute("not_authorised", "You are not allow to perform this lookup");
                        request.setAttribute("isLookup", "yes");
                        return Boolean.FALSE;
                    }
                } else {
                    if (debugMode) {
                        Debug.printFrameworkDebug("################################");
                        Debug.printFrameworkDebug("# No check lookup access right #");
                        Debug.printFrameworkDebug("################################");
                    }
                    return Boolean.TRUE;
                }
            }

            //        while (stActions.hasMoreTokens()) {
            //            String temp = stActions.nextToken();
            //            if (!(temp.equals("DynamicAction"))) {
            //                if (actionClassName.equals(temp)) {
            //                    return true;
            //                }
            //            }
            //        }
            //        if (sessionMap.get(session_userKey) == null) {
            //            return false;
            //        }
            try {
                if (actionClassName.equals("DynamicAction") || (actionClassName.equals("DynamicRptAction") && !method.equals("exportToExcel"))) {
                    Map param = new HashMap();
                    
                    //param.put("us_user_id", userId);
                    if (session_userKey.startsWith("p_")) {
                        //user = userDAO.getModelById(userId, user);
                    } else {
                        if (sessionMap.get("userId") instanceof Integer) {
                            user = userDAO.getModelById((Integer)sessionMap.get("userId"), User.class);
                        } else {
                            user = userDAO.getModelById((String)sessionMap.get("userId"), User.class);
                        }
                    }
                    if (user != null) {
                        String rightCondition = null;
                        if (method.equals("search2") || method.equals("loadEditPage") || method.equals("loadViewPage") || method.equals("moreInfo") || method.equals("populateDynamicActionSetup")) {
                            rightCondition = "application.retrieve_right = 'Y' and group_app.retrieve_right = 'Y'";
                            errorMessage = "You are not authorised to SEARCH RECORD in ";
                        } else if (method.equals("loadAddPage") || method.equals("processInsert")) {
                            rightCondition = "application.create_right = 'Y' and group_app.create_right = 'Y'";
                            errorMessage = "You are not authorised to CREATE RECORD in ";
                        } else if (method.equals("delete")) {
                            rightCondition = "application.delete_right = 'Y' and group_app.delete_right = 'Y'";
                            errorMessage = "You are not authorised to DELETE RECORD in ";
                        } else if (method.equals("dynamicUpdate") || method.equals("processUpdate")) {
                            rightCondition = "application.update_right = 'Y' and group_app.update_right = 'Y'";
                            errorMessage = "You are not authorised to UPDATE RECORD in ";
                        } else if ((actionClassName.equals("DynamicRptAction") || actionClassName.equals("DynamicAction")) && (method.equals("dynamicViewPage") || method.equals("generateDynamicRpt") || method.equals("dynamicSearchRpt") || method.equals("loadSearchPage"))) {
                            errorMessage = "No Print-Right in ";
                            rightCondition = "application.print_right = 'Y' and group_app.print_right = 'Y'";
                        }
                        String rightSql = "select count(*) from t_setup_group "
                                + "inner join t_setup_group_user group_user on t_setup_group.ug_id = group_user.ug_id "
                                + "inner join t_setup_user on t_setup_user.us_id = group_user.us_id "
                                + "inner join t_setup_group_app group_app on t_setup_group.ug_id = group_app.ug_id "
                                + "inner join t_setup_application application on application.application_id = group_app.application_id "
                                + "where (application_code = :paramAction and t_setup_user.us_id = '" + user.getID() + "' "
                                + (rightCondition == null ? "" : ("and " + rightCondition)) + ")";
                        Map rightParam = new HashMap();
                        rightParam.put("paramAction", paramAction);
                        usingActionOrClass = paramAction;
//                        System.out.println("rightSQL1 = " + rightSql);
                        if (retrievingDAO.sqlCountRecord(rightSql, rightParam) > 0) { // rights Found
//                                if (checkIsViewer && getCUD_count(retrievingDAO, "application_code", paramAction.replaceAll("'", "''"), user.getID()) == 0) {
//                                    request.setAttribute("isViewer", Boolean.TRUE);
//                                }
                            return Boolean.TRUE;
                        } else {
                            try {
                                Application app = (Application) retrievingDAO.getModelByCode("application_code", paramAction, new Application());
                                if (app == null) {
                                    request.setAttribute(notAuthorisedCode, "Invalid "+ (actionClassName.equals("DynamicRptAction")?"rptCode":"action") +" ["+ paramAction +"]");
                                } else {
                                    request.setAttribute(notAuthorisedCode, errorMessage + app.getAction_name());
                                }
                            } catch (Exception e) {
                                Debug.printError("Invalid application_code [" + paramAction + "]");
                                throw e;
                            }
                            return false;
                        }
                    }
                } else {
                    //no need to check authorisation if the method is cancel
                    if (sessionMap.get("userId") instanceof Integer) {
                        user = userDAO.getModelById((Integer)sessionMap.get("userId"), User.class);
                    } else {
                        user = userDAO.getModelById((String)sessionMap.get("userId"), User.class);
                    }

                    if (method.equalsIgnoreCase("cancel")) {
//                        if (checkIsViewer && getCUD_count(retrievingDAO, "action_class", actionClassName.replaceAll("'", "''"), user.getID()) == 0) {
//                            request.setAttribute("isViewer", Boolean.TRUE);
//                        }
                        return Boolean.TRUE;
                    }

                    if (user != null) {
                        String rightCondition = null;
                        if (method.equals("moreInfo") || method.equals("search2") || method.equals("loadEditPage") || method.equals("loadViewPage") || method.equals("populateDynamicActionSetup") || method.equals("processSearch")) {
                            errorMessage = "You are not authorised to SEARCH/RETRIEVE RECORD in ";
                            rightCondition = "application.retrieve_right = 'Y' and group_app.retrieve_right = 'Y'";
                        } else if (method.equals("loadAddPage") || method.equals("processInsert")) {
                            errorMessage = "You are not authorised to CREATE RECORD in ";
                            rightCondition = "application.create_right = 'Y' and group_app.create_right = 'Y'";
                        } else if (method.equals("delete")) {
                            errorMessage = "You are not authorised to DELETE RECORD in ";
                            rightCondition = "application.delete_right = 'Y' and group_app.delete_right = 'Y'";
                        } else if (method.equals("dynamicUpdate") || method.equals("processUpdate") || method.equals("cancelChanges") || method.equals("submitVerify")) {
                            errorMessage = "You are not authorised to UPDATE RECORD in ";
                            rightCondition = "application.update_right = 'Y' and group_app.update_right = 'Y'";
                        }
                        String rightSql = null;
                        Map rightParam = new HashMap();
                        if (rightCondition == null) { //not standard rights, need to check against additional rights
                            errorMessage = "You are not authorised to perform " + method + " in ";
                            if (method.equals("exportToExcel")) {
                                rightSql = "select count(*) from t_setup_group "
                                        + "inner join t_setup_group_user group_user on t_setup_group.ug_id = group_user.ug_id "
                                        + "inner join t_setup_user on t_setup_user.us_id = group_user.us_id "
                                        + "inner join t_setup_group_app group_app on t_setup_group.ug_id = group_app.ug_id "
                                        + "inner join t_setup_group_app_right group_app_right on group_app_right.ug_app_id = group_app.ug_app_id and group_app_right.hasright = 'Y' "
                                        + "inner join t_setup_application application on application.application_id = group_app.application_id "
                                        + "inner join t_setup_app_right app_right on app_right.APP_RIGHTS_ID = group_app_right.APP_RIGHTS_ID "
                                        + "inner join t_setup_app_right_method right_method on right_method.app_rights_id = app_right.app_rights_id "
                                        + "where (application_code = :paramAction and t_setup_user.us_id = '" + user.getID() + "' "
                                        + "and (method_str = '" + method.replaceAll("'", "''") + "'))";
                                        rightParam.put("paramAction", paramAction);
                            } else {
                                rightSql = "select count(*) from t_setup_group "
                                        + "inner join t_setup_group_user group_user on t_setup_group.ug_id = group_user.ug_id "
                                        + "inner join t_setup_user on t_setup_user.us_id = group_user.us_id "
                                        + "inner join t_setup_group_app group_app on t_setup_group.ug_id = group_app.ug_id "
                                        + "inner join t_setup_group_app_right group_app_right on group_app_right.ug_app_id = group_app.ug_app_id and group_app_right.hasright = 'Y' "
                                        + "inner join t_setup_application application on application.application_id = group_app.application_id "
                                        + "inner join t_setup_app_right app_right on app_right.APP_RIGHTS_ID = group_app_right.APP_RIGHTS_ID "
                                        + "inner join t_setup_app_right_method right_method on right_method.app_rights_id = app_right.app_rights_id "
                                        + "where (action_class = :paramAction and t_setup_user.us_id = '" + user.getID() + "' "
                                        + "and (method_str = '" + method.replaceAll("'", "''") + "'))";
                                rightParam.put("paramAction", actionClassName);
                            }
                        } else { // standard rights
                            rightSql = "select count(*) from t_setup_group "
                                    + "inner join t_setup_group_user group_user on t_setup_group.ug_id = group_user.ug_id "
                                    + "inner join t_setup_user on t_setup_user.us_id = group_user.us_id "
                                    + "inner join t_setup_group_app group_app on t_setup_group.ug_id = group_app.ug_id "
                                    + "inner join t_setup_application application on application.application_id = group_app.application_id "
                                    + "where (action_class = :paramAction and t_setup_user.us_id = '" + user.getID() + "' "
                                    + "and " + rightCondition + ")";
                            rightParam.put("paramAction", actionClassName);
                        }
                        usingActionOrClass = (String)rightParam.get("paramAction");
//                        Debug.printFrameworkDebug("rightSql2 = " + rightSql);
//                        Debug.printFrameworkDebug("rightParam = " + rightParam);
                        if (retrievingDAO.sqlCountRecord(rightSql, rightParam) > 0) { // rights Found
//                            System.out.println(actionClassName + ">>" + user.getID());
                            //                                System.out.println("CF in here 1... new access right checking allow to invoke!!!");
//                                    if (checkIsViewer && getCUD_count(retrievingDAO, "action_class", actionClassName.replaceAll("'", "''"), user.getID()) == 0) {
//                                        request.setAttribute("isViewer", Boolean.TRUE);
//                                    }
                            return Boolean.TRUE;
                        } else {
                            //                                System.out.println(actionClassName + ": rightSql used=" + rightSql);
                            Application app = null;
                            Map param = new HashMap();
                            Debug.printFrameworkDebug("actionClassName ===== " + actionClassName);
                            param.put("action_class", actionClassName);
                            List list = retrievingDAO.list(param, Application.class);
                            String appName = "Unknown Action Class :" + actionClassName;
                            if (list != null && list.size() > 0) {
                                app = (Application) list.get(0);
                                if (app.getSystem_app().equalsIgnoreCase("Y") && user.getUs_admin().equalsIgnoreCase("Y")) {
//                                            if (checkIsViewer && getCUD_count(retrievingDAO, "action_class", actionClassName.replaceAll("'", "''"), user.getID()) == 0) {
//                                                request.setAttribute("isViewer", Boolean.TRUE);
//                                            }
                                    //                                        System.out.println("CF in here 2... SYSTEM APP: new access right checking allow to invoke!!!");
                                    return Boolean.TRUE;
                                }
                                appName = app.getApplication_name();
                            }
                            if (appName.startsWith("Unknown Action Class")) {
                                request.setAttribute(notAuthorisedCode, "Not a stardard method and not setup in application's additional right. Access Denied.");
                            } else {
                                request.setAttribute(notAuthorisedCode, errorMessage + appName);
                            }
                            Debug.printFrameworkDebug("FALSE 3");
                            return false;
                        }
                    } else {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute(notAuthorisedCode, "Fail to validate access right for ["+usingActionOrClass+"]");
                return false;
//                throw e;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (closeTheSession) {
                userDAO.closeSession();
            }
        }
        sessionMap.remove(notAuthorisedCode);
        sessionMap.put(notAuthorisedCode, "Unknown Access");
        return false;
    }

    public static String insertNoLock(String sql) {
        Integer index = null;
        Integer space_insertIdx = null;
        Integer blacket_insertIdx = null;
        sql = sql.replaceAll("(?i) from ", " FROM ");
        sql = sql.replaceAll("(?i) join ", " JOIN ");
        StringBuilder strBuilder = new StringBuilder(sql);
        while (index == null || index >= 0) {
            if (index == null) {
                index = -1;
            }
            index = strBuilder.indexOf(" FROM ", index + 1);
            if (index > 0) {
                if (strBuilder.indexOf(" FROM n ", index - 7) == index) {
                    continue;
                }
                if (strBuilder.indexOf(" FROM n)", index - 7) == index) {
                    continue;
                }
                if (strBuilder.indexOf(" FROM (", index - 7) == index) {
                    continue;
                }
                space_insertIdx = strBuilder.indexOf(" ", index + 7);
                if (space_insertIdx < 0) {
                    //strBuilder.append(" (nolock)");
                    continue;
                }
                blacket_insertIdx = strBuilder.indexOf(")", index + 7);
                if (blacket_insertIdx < space_insertIdx && blacket_insertIdx > 0) {
                    //strBuilder.insert(blacket_insertIdx, " (nolock) ", 0, 10);
                } else {
                    //strBuilder.insert(space_insertIdx, " (nolock) ", 0, 10);
                }
            }
        }
        index = null;
        while (index == null || index >= 0) {
            if (index == null) {
                index = -1;
            }
            index = strBuilder.indexOf(" JOIN ", index + 1);
            if (index > 0) { //join come first
                if (strBuilder.indexOf(" JOIN (", index - 7) == index || strBuilder.indexOf(" JOIN  (", index - 7) == index) {
                    continue;
                }
                space_insertIdx = strBuilder.indexOf(" ", index + 7);
                if (space_insertIdx < 0) {
                    //strBuilder.append(" (nolock)");
                    continue;
                }
                //strBuilder.insert(space_insertIdx, " (nolock) ", 0, 10);
            }
        }
        return strBuilder.toString();
    }

    // ThoTH @ 26-Nov-2013
    public String getSingleValueWithSession(org.hibernate.Session session, String strTable, String strColumn, String strWhere) throws Exception {
        Object obj = null;

        org.hibernate.SQLQuery query = null;
        try {
            String strSql = "";
            if (strTable.equals("")) {  // ThoTH @ 25-Feb-2014 :: to cater pass in Whole SQL
                strSql = strColumn;
            } else {
                if (strWhere.equals("")) {
                    strSql = "SELECT " + strColumn + " FROM " + strTable;
                } else {
                    strSql = "SELECT " + strColumn + " FROM " + strTable + " WHERE " + strWhere;
                }
            }
//            Debug.printFrameworkDebug("strSql " + strSql);
            query = session.createSQLQuery(strSql);
            obj = query.uniqueResult();
        } catch (Exception e) {
//            new LogFunction().logError(this.getClass(), "", e);
            throw e;
        }
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public List getHoliday(org.hibernate.Session session, String startDate, String endDate) {
        String sql = "select cal_date from t_setup_calendar "
                + "where cal_date between cast(:startDate as timestamp) and cast(:endDate as timestamp) and "
                //+ "(extract(dow from cal_date) between 1 and 5) and is_holiday = 'N' " this one is to get None Holiday
                + "((extract(dow from cal_date)=0 or extract(dow from cal_date)=6) or is_holiday = 'Y') ";
        Debug.printFrameworkDebug("SQL = " + sql + " :: startDate = " + startDate + ", endDate = " + endDate);

        return session.createSQLQuery(sql).setString("startDate", startDate).setString("endDate", endDate).
                setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).list();
    }

    public java.sql.Timestamp getJobNextWorkingDay(org.hibernate.Session session, java.sql.Timestamp jobDate, String jobDuration) throws Exception {

        String sql = " select s.a as work_day "
                + " from ( "
                + //                     " select cast(s.a as timestamp), row_number() over() as rnum from generate_series(now() + interval '1 day', now() + interval '365 days', '1 day') s(a) "+
                " select cast(s.a as timestamp), row_number() over() as rnum from generate_series(cast('" + jobDate + "' as timestamp) + interval '1 day', cast('" + jobDate + "' as timestamp) + interval '365 days', '1 day') s(a) "
                + " inner join t_setup_calendar on cast(cal_date as date) = cast(s.a as date) "
                + " where extract(dow from s.a) between 1 and 5 "
                + " and is_holiday = 'N' "
                + " ) s "
                + " where s.rnum = '" + jobDuration + "' ";

//        String sql = "select min(s.a) as work_day "
//                + " from (select cast(s.a as timestamp) from generate_series(cast('2016-Jan-01' as timestamp), '2050-Dec-31', '1 day') s(a) "
//                + " where extract(dow from s.a) between 1 and 5 except "
//                + " select cal_date from t_setup_calendar where is_holiday='Y' ) s "
//                + " where s.a - interval '"+jobDuration+"' day > '"+jobDate+"' ";
//        String sql = "select min(s.a) as work_day "
//                + " from (select s.a::date from generate_series('2016-Jan-01'::date, '2050-Dec-31', '1 day') s(a) "
//                + " where extract(dow from s.a) between 1 and 5 except "
//                + " select cal_date from t_setup_calendar where is_holiday='Y' ) s "
//                + " where s.a - interval '"+jobDuration+"' day > '"+jobDate+"' ";
        //System.out.println("sql next working day = "+sql);
        sql = insertNoLock(sql);
        return DateUtil.getTimestampFromDate(DateUtil.getDate(getSingleValueWithSession(session, "", sql, ""), "yyyy-MM-dd HH:mm:ss"));
    }

    public static String getHexId(int intNoChar) {
        intNoChar--;
        String idchars = "0123456789ABCDEF";
        String strValue = "";

        for (int i = 0; i <= intNoChar; i++) {
            strValue += idchars.charAt((int) Math.round(Math.floor(Math.random() * idchars.length())));
        }

        return strValue;
    }

    public static String censoredEmail(String strEmail) {
        boolean blnMy = false;
        if (strEmail.contains(".my")) {
            strEmail = strEmail.substring(0, strEmail.length() - 3);
            blnMy = true;
        }
        String strArray[] = strEmail.split("");
        String strCensored = "";
        int intSeq = 0;
        int intAfterAlias = -1;
        for (String a : strArray) {
            if (intSeq > 0) {
                if (intSeq == 1 || a.equals(".")) {
                    strCensored += a;
                } else if (a.equals("@")) {
                    intAfterAlias = intSeq + 1;
                    strCensored += a;
                } else if (intAfterAlias == intSeq) {
                    strCensored += a;
                } else {
                    strCensored += "*";
                }
            }
            intSeq++;
        }
        if (blnMy) {
            strCensored += ".my";
        }
        return strCensored;
    }

    public static Map getDynamicConfigMap() {
        return dynamicConfigMap;
    }
    
    public static int nthOccurrence(String str, String c, int n) {
        int pos = str.indexOf(c, 0);
        while (n-- > 0 && pos != -1) {
            pos = str.indexOf(c, pos+1);
        }
        return pos;
    }
    
    public static String getFile_extension(String strFileName) throws Exception {
        int fileExtIdx = strFileName.lastIndexOf(".");
        int fileNLength = strFileName.length();
        return strFileName.substring(fileExtIdx, fileNLength);
    }
    
    public static synchronized String getUserIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");  
        if (ipAddress == null) {  
            Debug.printFrameworkDebug("cannot get IP from X-FORWARDED-FOR");
            ipAddress = request.getRemoteAddr();  
        }
        //some how the ipAddress will contains port and more than 1 IP eg: 115.164.222.219:42823, 172.18.65.31
        //call removePort() to remove the port.
        ipAddress = removePort(ipAddress);
        Debug.printFrameworkDebug("final IP = " + ipAddress);
        return ipAddress;
    }
    private static String removePort(String ipAddress) {
        while (ipAddress.contains(":")) {
            int idx = ipAddress.indexOf(":");
            int commaIdx = ipAddress.indexOf(",", idx);
            if (commaIdx > 0) {
                ipAddress = ipAddress.substring(0, idx) + ipAddress.substring(commaIdx);
            } else {
                ipAddress = ipAddress.substring(0, idx);
            }
        }
        return ipAddress;
    }
    public static synchronized String getUserAgent(HttpServletRequest request) {
        String  browserDetails  =   request.getHeader("User-Agent");
        String  userAgent       =   browserDetails;
        String  user            =   userAgent.toLowerCase();

        String os = "";
        String browser = "";

        //=================OS=======================
         if (userAgent.toLowerCase().indexOf("windows") >= 0 )
         {
             os = "Windows";
         } else if(userAgent.toLowerCase().indexOf("mac") >= 0)
         {
             os = "Mac";
         } else if(userAgent.toLowerCase().indexOf("x11") >= 0)
         {
             os = "Unix";
         } else if(userAgent.toLowerCase().indexOf("android") >= 0)
         {
             os = "Android";
         } else if(userAgent.toLowerCase().indexOf("iphone") >= 0)
         {
             os = "IPhone";
         }else{
             os = "UnKnown, More-Info: "+userAgent;
         }
         //===============Browser===========================
        if (user.contains("msie"))
        {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera"))
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            else if(user.contains("opr"))
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
        } else if (user.contains("chrome"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
        {
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
            browser = "Netscape-?";

        } else if (user.contains("firefox"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv"))
        {
            browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
        } else
        {
            browser = "UnKnown, More-Info: "+userAgent;
        }
        
        return os+"::"+browser;
    }
    
    public static String getSystemParam(Session session, String systemCode, String paramCode) {
        ParameterModel paramModel = (ParameterModel) session.getNamedQuery("Parameter.getByParameter_code")
        .setParameter("system_code", systemCode)
        .setParameter("parameter_code", paramCode)
        .uniqueResult();
        return paramModel.getParameter_value();
    }
    
    public static KeyPair generateKeyPair()
            throws NoSuchAlgorithmException, NoSuchProviderException {
        return generateKeyPair(ENCRYPTION_BIT);
    }
    public static KeyPair generateKeyPair(Integer keyBit)
            throws NoSuchAlgorithmException, NoSuchProviderException {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");//RSA

//        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        SecureRandom random = SecureRandom.getInstanceStrong();

        // 512 is keysize
        keyGen.initialize(keyBit, random);

        KeyPair generateKeyPair = keyGen.generateKeyPair();
        return generateKeyPair;
    }
    
//    public static void generateKeyPayToSession(Map session) throws Exception{
//        KeyPair generateKeyPair = generateKeyPair();
//
//        byte[] publicKey = generateKeyPair.getPublic().getEncoded();
//        byte[] privateKey = generateKeyPair.getPrivate().getEncoded();
//
//        String base64Public = org.apache.commons.codec.binary.Base64.encodeBase64String(publicKey);
//        String base64Private = org.apache.commons.codec.binary.Base64.encodeBase64String(privateKey);
//
//        session.put("pubkey", base64Public);
//        session.put("prikey", base64Private);
//    }

    public static Map jsVar = new HashMap();
    private static final String ALGORITHM = "RSA";
    private static final Integer ENCRYPTION_BIT = 512;

    public static String encrypt(String base64PublicKey, String inputData)
            throws Exception {

        PublicKey key = KeyFactory.getInstance(ALGORITHM)
                .generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(base64PublicKey)));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(inputData.getBytes());

        return Base64.encodeBase64String(encryptedBytes);
    }

    public static String decrypt(String base64PrivateKey, String base64String)
            throws Exception {

        PrivateKey key = KeyFactory.getInstance(ALGORITHM)
                .generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(base64PrivateKey)));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(base64String));

        return new String(decryptedBytes);
    }
    // For EBPP Paybill
    public static String getHash(String strOrderNo, String strMerchantID, String strAmt, String strRespCode)
            throws ParseException {
        strOrderNo = getNumber(strOrderNo);
        strMerchantID = SystemConstants.DOMAIN.ebppHashKey;
        
//        strMerchantID = "2013";  // Override

        BigDecimal dbOrderNo = new BigDecimal(strOrderNo);
        BigDecimal dbMID = new BigDecimal(strMerchantID);
        BigDecimal dbAmt = new BigDecimal(strAmt);
        BigDecimal dbResp = new BigDecimal(strRespCode);

        BigDecimal hash1 = dbOrderNo.multiply(dbAmt);
        BigDecimal hash2 = dbMID.multiply(dbAmt);
        BigDecimal hash3 = dbMID.multiply(dbOrderNo);
        BigDecimal t1 = hash1.add(hash2).add(hash3);
        BigDecimal t2 = dbMID.add(dbResp);
        BigDecimal total = t1.divide(t2, 8, BigDecimal.ROUND_UP);

        String strHash = total.toString();

        if (strHash.indexOf(".") != -1) {
            strHash = strHash.substring(0, strHash.indexOf(".") + 7);
        } else {
            strHash = strHash + ".000000";
        }
        System.out.println("strHash " +strHash);
        return TruncateChar(strHash);
    }

    // For EBPP Paybill
    public static String TruncateChar(String str) {
        if (str.indexOf(",") != -1) {
            String newStr = str.substring(0, str.indexOf(",")) + "" + TruncateChar(str.substring(str.indexOf(",") + 1,
                    str.length()));

            return newStr;
        } else {
            return str;
        }

    }
    
    /**
     * @param criteria Input in date format. Eg 2020/03/31
     * @return return String of dateformat. return "" is not found common
     * dateformat
     */
    public static String verifyDate(String criteria) {
        if (verifyDateformat(criteria, "dd/MM/yyyy")) {
            return "dd/MM/yyyy"; // common 
        } else if (verifyDateformat(criteria, "dd-MM-yyyy")) {
            return "dd-MM-yyyy"; // india
        } else if (verifyDateformat(criteria, "yyyy-MM-dd")) {
            return "yyyy-MM-dd"; // common 
        } else if (verifyDateformat(criteria, "yyyy/MM/dd")) {
            return "yyyy/MM/dd"; // african
        } else if (verifyDateformat(criteria, "MM/dd/yyyy")) {
            return "MM/dd/yyyy"; // american
        } else {
            return "";
        }
    }

    /**
     *
     * @param criteria Input as in date format
     * @param format Date format. To check criteria is in current date format.
     * @return return true is criteria and format is match. return ffalse if
     * not.
     */
    public static Boolean verifyDateformat(String criteria, String format) {
        try {
            new SimpleDateFormat(format).parse(criteria);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static Object getMethodObjectFromObject(Object model, String method) throws Exception {
        Boolean isDate = Boolean.FALSE;
        if (method.indexOf("|") > 0){
            method = method.substring(method.indexOf("|") + 1);
        }
        if (method.indexOf("_date_") >= 0) {
            method = method.substring(method.indexOf("_date_") + 6);
            isDate = Boolean.TRUE;
        }

        StringTokenizer st = new StringTokenizer(method, ".");
        String token = "";
        Object obj = null;
        Method m;
        while (st.hasMoreTokens()) {
            token = st.nextToken().trim();
            if (token.startsWith("_self")) {
                //token = token.substring(6);
                continue;
            }
            if (!token.startsWith("get")) {
                token = "get" + WordUtils.capitalize(token);
            }
            if (obj == null) {
                m = model.getClass().getMethod(token);
                obj = m.invoke(model);
            } else {
                m = obj.getClass().getMethod(token);
                obj = m.invoke(obj);
            }
        }
        if (obj != null && isDate) {
            return obj.toString().substring(0, 10);
        }
        return obj;
    }
    
    public static void loadStaticVar() {
        try {
            File f1 = new File(SysConf.get("staticVarFilePath"));
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            ArrayList<String> lines = new ArrayList<String>();
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] varArr = line.split("=");
                switch (varArr[0]) {
                    case "EMAIL_Queue" :
                        if (SystemConstants.staticVarMap.containsKey(varArr[0])) {
                            SystemConstants.staticVarMap.put("EMAIL_Queue", varArr[1]); 
                        }
                        break;
                    case "SMS_Queue" :
                        if (SystemConstants.staticVarMap.containsKey(varArr[0])) {
                            SystemConstants.staticVarMap.put("SMS_Queue", varArr[1]); 
                        }
                        break;
                    case "triggerTimer" :
                        if (SystemConstants.staticVarMap.containsKey(varArr[0])) {
                            SystemConstants.staticVarMap.put("triggerTimer", varArr[1]);
                        }
                        break;
                }
            }
            fr.close();
            br.close();
        } catch (Exception e) {
            
        }
    }
    public static void saveStaticVar() {
        try {
            File f1 = new File(SysConf.get("staticVarFilePath"));
            FileWriter fw = new FileWriter(f1);
            BufferedWriter out = new BufferedWriter(fw);
            for (String key : SystemConstants.staticVarMap.keySet()) {
                out.write(key+"="+ SystemConstants.staticVarMap.get(key) +"\r\n");
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            
        }
    }
    
    private static Map commThreadVar = new HashMap();
    public static Map<String, Object> currentProcessMap() {
        Map currentProcessMap = null;
        try {
            HttpServletRequest currentRequest = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            if (currentRequest.getAttribute("currentProcessMap") != null) {
                return (Map)currentRequest.getAttribute("currentProcessMap");
            } else {
                currentProcessMap = new HashMap();
                currentRequest.setAttribute("currentProcessMap", currentProcessMap);
            }
        } catch (Exception e) {
            Thread currThread = Thread.currentThread();
            Map threadMap = (Map)commThreadVar.get(currThread.getId());
            if (threadMap == null) {
                threadMap = new HashMap();
                currentProcessMap = new HashMap();
                threadMap.put("currentProcessMap", currentProcessMap);
                commThreadVar.put(currThread.getId(), threadMap);
            } else {
                currentProcessMap = (Map)threadMap.get("currentProcessMap");
                if (currentProcessMap == null) {
                    currentProcessMap = new HashMap();
                    threadMap.put("currentProcessMap", currentProcessMap);
                }
            }
        }
        return currentProcessMap;
    }
    
    public static Map getMapFromJson(String jsonString) {
        return new Gson().fromJson(jsonString, Map.class);
    }
    
    public String getPubcodeDesc(String strCodeType, String strCode1) { //serene @ 21/3/2019: remove static
//    public static String getPubcodeDesc(String strCodeType, String strCode1) {
        BaseDAO<Pubcode> pubcodeDAO = new BaseDAOImpl();
        String strCodeDesc = "NA";
        try {
            Pubcode pubcode = pubcodeDAO.getModelByCode("code_type,code_1", strCodeType + "," + strCode1, new Pubcode());
            if (pubcode != null) {
                strCodeDesc = pubcode.getCode_desc();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            pubcodeDAO.closeSession();
        }
        return strCodeDesc;
    }
    
    public String doFormattedDateCount(Map dynamicConfigResultMap) {
        try {
            return "- " + dynamicConfigResultMap.get("application_order") + " -";
        } catch (Exception e) {
        }
        return "";
    }
    
    public static Map actionPackageMap = null;
    public static synchronized Map readStrutsXmlAsMap() {
        if (actionPackageMap != null) {
            return actionPackageMap;
        }
        actionPackageMap = new HashMap();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            // parse XML file
            
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setEntityResolver(new EntityResolver() {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    if (systemId.contains(".dtd")) {
                        return new InputSource(new StringReader(""));
                    } else {
                        return null;
                    }
                }
            });
            Document doc = db.parse(new File(SysConf.get("struts.xml.path")));

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            // get <action>
//            NodeList list = doc.getElementsByTagName("action");
            NodeList list = doc.getElementsByTagName("package");

            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);
                Element nodeElement = (Element) node;
                if (!nodeElement.getAttribute("name").equals("default")) {
                    continue;
                }
                NodeList actionNodeList =  nodeElement.getElementsByTagName("action");
                for (int actionIdx = 0; actionIdx < actionNodeList.getLength(); actionIdx++) {
                    Node actionNode = actionNodeList.item(actionIdx);
                    Element actionElement = (Element) actionNode;
                    String actionClass = actionElement.getAttribute("class");
                    actionPackageMap.put(actionClass.substring(actionClass.lastIndexOf(".")+1), actionClass);
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String getHashKeyVal(String strSystemName, String systemHashKey, String apiUrl, String strParam, String strFrom) {
        Calendar cal = Calendar.getInstance();
        String strYear = Integer.toString(cal.get(Calendar.YEAR));
        String strMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String strDay = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));

        String strFieldsValue = strYear.concat("^").concat(StringUtils.leftPad(strMonth, 2, "00")).concat("^").concat(StringUtils.leftPad(strDay, 2, "00")).concat("^").concat(strParam).concat("^").concat(strFrom);
        new LogFunction().logInfo(this.getClass(), "getHashKeyVal >>> System: " + strSystemName + " strFieldsValue: " + strFieldsValue);
        return HMAC_SHA256(systemHashKey, strFieldsValue);
    }
    
    public String HMAC_SHA256(String secret, String message) {

        byte[] hash = null;
        try {
            //new
            Mac hasher = Mac.getInstance("HmacSHA256");
            hasher.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
            hash = hasher.doFinal(message.getBytes());

            return DatatypeConverter.printHexBinary(hash);

        } catch (Exception e) {
        }
        return DatatypeConverter.printBase64Binary(hash);
    }
    
    public String callPublicApi(String url, ArrayList<NameValuePair> urlParameters) throws Exception {
//        System.out.println("call public API 1");
        String strResponse = "";
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();

        } catch (KeyManagementException ex) {
            new LogFunction().logError(this.getClass(), "callPublicApi1",ex);
//            Logger.getLogger(EisApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
        org.apache.http.impl.client.CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
            .setSSLHostnameVerifier(new NoopHostnameVerifier())
            .build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        CloseableHttpResponse resp = httpClient.execute(httpPost);
        BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
        String responseString = "";
        while ((responseString = br.readLine()) != null) {
            strResponse = strResponse + responseString;
        }
//        System.out.println("callapi1:: " + strResponse);
        return strResponse;
    }
    
    public String callPublicApi2(String apiUrl, String strParams) throws Exception{
        SSLContext sslContext = null;
        String strResponse = "";
        new LogFunction().logInfo(this.getClass(), " apiUrl : " + apiUrl);
        try {
            sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (certificate, authType) -> true).build();
        } catch (KeyManagementException ex) {
              new LogFunction().logError(this.getClass(), "callPublicApi2",ex);
//            Logger.getLogger(TsApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
        org.apache.http.impl.client.CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
           .setSSLHostnameVerifier(new NoopHostnameVerifier())
            .build();
//        System.out.println("strParams "+strParams);
        HttpPost httpPost = new HttpPost(apiUrl);
        StringEntity strEntity = new StringEntity(strParams);
            httpPost.setEntity(strEntity);
        httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            
        CloseableHttpResponse resp = httpClient.execute(httpPost);
//        System.out.println("testing 123");
        BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
        String responseString = ""; 
        while ((responseString = br.readLine()) != null) {
            strResponse = strResponse + responseString;
        }
        new LogFunction().logInfo(this.getClass(), "CF ->"+strResponse  );
//        System.out.println("STRrESPONSE :"+strResponse);
        return strResponse;
                
    }
    
    public String getHashCashTran(String billRefNo) throws ParseException {
        
//        podId = getNumber(podId); 
//        int billNo = Integer.parseInt(podId);        
//        int i = billNo * 105 / 5;
        System.out.println("billRefNo " +billRefNo);
        BigDecimal billRefNo_ = new BigDecimal(getNumber(billRefNo));
        BigDecimal code = new BigDecimal("105");
        BigDecimal code2 = new BigDecimal("5");
        BigDecimal i = billRefNo_.multiply(code);
        BigDecimal finalStr = i.divide(code2);
        System.out.println("elasis finalStr " +finalStr);
        
        return String.valueOf(finalStr);
        
    }
    
    public String getRvrcodeDesc(String strTracode) { //stephnieia@14.8.2022
        BaseDAO<Rvrcode> rvrcodeDAO = new BaseDAOImpl();
        String strTracodeDesc = "NA";
        try {
            Rvrcode rvrcode = rvrcodeDAO.getModelByCode("trancode", strTracode, new Rvrcode());
            if (rvrcode != null) {
                strTracodeDesc = rvrcode.getDescription();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            rvrcodeDAO.closeSession();
        }
        return strTracodeDesc;
    }
    
    public String getRvrSubcodeDesc(String strTrancode, String strSubcode) { 
        BaseDAO<Rvcoddtl> rvcoddtlDAO = new BaseDAOImpl();
        String strTracodeDesc = "NA";
        try {
            Rvcoddtl rvcoddtl = rvcoddtlDAO.getModelByCode("trancode,subcode", strTrancode+","+strSubcode, new Rvcoddtl());
            if (rvcoddtl != null) {
                strTracodeDesc = rvcoddtl.getDescription();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            rvcoddtlDAO.closeSession();
        }
        return strTracodeDesc;
    }
    
                  /*
    AiMin @29.7.2019
    Remark: For Mobile Lasis Integrate with SarawakPay App
    */
    public String readMLasisPrivateKey(){
        try {
            // strip of header, footer, newlines, whitespaces
          //Remark: Dev used.  
            String privateKey = 
                    FileUtils.readFileToString(
                            new File(SystemConstants.SPAY_MERCHANT_KEY.FILE_PATH + "lasis_mobile_spa.key"), "utf-8"); 
           
            //Remark: Liv used.
//            String privateKey = 
//                    FileUtils.readFileToString(
//                            new File(SystemConstants.SPAY_MERCHANT_KEY.FILE_PATH + "lasis_mobile_spa.key"), "utf-8"); 
           
//            Remark: Liv used. **LIVE**
//            String privateKey = 
//                    FileUtils.readFileToString(
//                            new File(SystemConstants.SPAY_MERCHANT_KEY.FILE_PATH + "midlleware_private.key"), "utf-8"); 
	

            privateKey = privateKey
                    .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            System.out.println("readMLasisPrivateKey privateKey : " + privateKey);
            return privateKey;
        } catch (Exception e) {
            System.out.println("readPrivateKey Exception.");
        }
       
        return "";
    }
    
     /*
    AiMin @29.7.2019
    Remark: For Mobile Lasis Integrate with SarawakPay App
    */
    public String readPublicKey() {
        try {
            //Remark: Dev used.
            String publicKey = 
                    FileUtils.readFileToString(
//                            new File(SystemConstants.SPAY_MERCHANT_KEY.FILE_PATH + "sarawakpay_public_key.pem"), "utf-8"); 
                            new File(SystemConstants.SPAY_MERCHANT_KEY.FILE_PATH + "spayweb_dev_pub.pem"), "utf-8"); 
            
           //Remark: Live used. **LIVE**
//            String publicKey = 
//                    FileUtils.readFileToString(
//                            new File(SystemConstants.SPAY_MERCHANT_KEY.FILE_PATH + "sp_prod_web_public_key.pem"), "utf-8");  
          
            // strip of header, footer, newlines, whitespaces
            publicKey = publicKey
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

             
            System.out.println("readPublicKey publicKey : " + publicKey);
            return publicKey;
            
        } catch (IOException io) {
            System.out.println("readPublicKey IOException");
        } catch (Exception e) {
            System.out.println("readPublicKey Exception");
        }

        return "";
    }
    
    public void auditAction(String caseId, Session session, String actionBy, String actionRemark) {

        BaseDAO<AuditActionModel> basedao = new BaseDAOImpl();
        basedao.setSession(session);
        try {
            AuditActionModel auditAction = new AuditActionModel();

            auditAction.setCase_id(caseId);
            auditAction.setAction_by(actionBy);
            auditAction.setAction_remarks(actionRemark);
            auditAction.setAction_id(getId(CommonFunction.PK_LENGTH));
            auditAction.defaultAddProperties();
            
            basedao.getSession().save(auditAction);
        } catch (Exception e) {
            e.printStackTrace();
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
        }
    }
    
    public void auditAction2(String caseId, Session session, String actionBy, String actionRemark) {

        BaseDAO<AuditActionModel> basedao = new BaseDAOImpl();
        basedao.setSession(session);
        try {
            AuditActionModel auditAction = new AuditActionModel();

            auditAction.setCase_id(caseId);
            auditAction.setAction_by(actionBy);
            auditAction.setAction_remarks(actionRemark);
            auditAction.setAction_id(getId(CommonFunction.PK_LENGTH));
            auditAction.defaultAddProperties();
            
            basedao.beginBatchTransaction();
            basedao.getSession().save(auditAction);

            basedao.commitBatchTransaction();
        } catch (Exception e) {
            basedao.rollbackBatchTransaction();
            e.printStackTrace();
            new LogFunction().logError(this.getClass(), "", e);
        } finally {
            basedao.closeSession();
        }
    }
    
    public long find_different_days(String join_date, String leave_date, String type)   
    {   
        // Create an instance of the SimpleDateFormat class  
        SimpleDateFormat obj = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");   
        long difference = 0;
        // In the try block, we will try to find the difference  
        try {   
            // Use parse method to get date object of both dates  
            Date date1 = obj.parse(join_date);   
            Date date2 = obj.parse(leave_date);   
               long time_difference = date2.getTime() - date1.getTime();  
            // Calucalte time difference in milliseconds   
            if(type.equals(SystemConstants.TIME_UNIT.times)){
                difference =time_difference; 
            }
            if(type.equals(SystemConstants.TIME_UNIT.days)){
                difference =  (time_difference / (1000*60*60*24)) % 365;   
            }
            if(type.equals(SystemConstants.TIME_UNIT.years)){
                difference =  (time_difference / (1000l*60*60*24*365)); 
            }
            if(type.equals(SystemConstants.TIME_UNIT.seconds)){
                difference =  (time_difference / 1000)% 60;   
            }
            if(type.equals(SystemConstants.TIME_UNIT.minutes)){
                difference = (time_difference / (1000*60)) % 60;   
            }
            if(type.equals(SystemConstants.TIME_UNIT.hours)){
                difference = (time_difference / (1000*60*60)) % 24;   
            }
        }   
        // Catch parse exception   
        catch (ParseException excep) {   
            excep.printStackTrace();   
        }   
        return difference;
    } 
    
    public String getUserName(String strUserId) { //serene @ 21/3/2019: remove static
//    public static String getPubcodeDesc(String strCodeType, String strCode1) {
        BaseDAO<User> userDAO = new BaseDAOImpl();
        String strUserName = "NA";
        try {
            User user = userDAO.getModelByCode("us_user_id,rownum", strUserId+",1", new User());
            if (user != null) {
//                System.out.println("strCodeDesc ");
                strUserName = user.getUs_user_name();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            userDAO.closeSession();
        }
        return strUserName;
    }
    
    public static String prefixZeros(String value, int len, boolean prefixAlphabet, String prefixAl) {
        char[] t = new char[len];
        int l = value.length();
        int k = len - l;
        for (int i = 0; i < k; i++) {
            t[i] = '0';
        }
        value.getChars(0, l, t, k);
        
        if(prefixAlphabet){
            return prefixAl + new String(t);
        }else{
            return new String(t);
        }        
    }
    
    public static void writeLogFile(StackTraceElement[] stktrace, String logFile, String className, String actionName) {
        
        try {
            for (StackTraceElement stktrace1 : stktrace) {
                CommonFunction.writeFile(logFile, className + " >>> " + actionName + " :: at " + stktrace1.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
