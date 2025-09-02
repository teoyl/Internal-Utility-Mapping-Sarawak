package com.sains.framework.base;

import com.Decoder;
import com.SysConf;
import com.backend.SetupSchedularModel;
import com.lxg.common.model.CustCompanyModel;
import com.lxg.common.model.Pubcode;
import com.lxg.common.model.PublicUserModel;
import com.lxg.common.model.Rvcoddtl;
import com.lxg.common.model.Rvrcode;
import com.lxg.common.model.SetupCodeModel;
import com.lxg.common.model.UserCompanyModel;
import com.lxg.prepayment.model.PpBalanceModel;
import com.lxg.prepayment.model.PpTranModel;
import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.PooledDataSource;
import com.sains.common.util.ElasisSeqModel;
import com.sains.common.util.SeqModel;
import com.sains.common.util.Validator;
import com.sains.framework.model.ApiAuthenticationModel;
import com.sains.framework.model.ApiTokenModel;
import com.sains.framework.model.Application;
import com.sains.framework.model.ApplicationRights;
import com.sains.framework.model.AuditTrailModel;
import com.sains.framework.model.AuditLoginModel;
import com.sains.framework.model.AuditReportModel;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.model.AutoEmailParam;
import com.sains.framework.model.DqDatasourceModel;
import com.sains.framework.model.DqLogModel;
import com.sains.framework.model.DqQueryModel;
import com.sains.framework.model.DqTemplateFieldModel;
import com.sains.framework.model.DqTemplateModel;
import com.sains.framework.model.DrCommDocCodeModel;
import com.sains.framework.model.DrDocRepoModel;
import com.sains.framework.model.DrUserCommDocModel;
import com.sains.framework.model.EmailQueueAttachmentModel;
import com.sains.framework.model.EmailQueueModel;
import com.sains.framework.model.ErrLogModel;
import com.sains.framework.model.GroupApplication;
import com.sains.framework.model.GroupApplicationRights;
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.MoalModel;
import com.sains.framework.model.Module;
import com.sains.framework.model.NotificationSetup;
import com.sains.framework.model.RightMethod;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.model.User;
import com.sains.framework.model.ParameterModel;
import com.sains.framework.model.SetupSystemModel;
import com.sains.framework.model.TokenSessionModel;
import com.sains.framework.model.UserPreferenceModel;
import com.sains.framework.model.UserPubPriModel;
import com.sains.workflow.model.SetupSubSystemModel;
import com.sample.ChildModel;
import com.sample.ChildWithAttachmentModel;
import com.sample.DetailModel;
import com.sample.GrandChildModel;
import com.sample.MasterModel;
import com.sample.ParentModel;
import com.sample.PropertyModel;
import com.sample.UppyParentModel;
import com.sample.UppyModel;
import com.sample.model.SampleTableModel;
import com.utimaps.model.AppLocalityModel;
import com.utimaps.model.AppOwnerPModel;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.AuditActionModel;
import com.utimaps.model.CaseModel;
import com.utimaps.model.ChecklistItemSetupModel;
import com.utimaps.model.ChecklistItemModel;
import com.utimaps.model.ChecklistModel;
import com.utimaps.model.ChecklistSetupModel;
import com.utimaps.model.FileModel;
import com.utimaps.model.JobDetailModel;
import com.utimaps.model.JobStatusModel;
import com.utimaps.model.MiscellaneousPlanModel;
import com.utimaps.model.NotificationPModel;
import com.utimaps.model.PaymentItemModel;
import com.utimaps.model.PaymentModel;
import com.utimaps.model.PrecheckHistoryModel;
import com.utimaps.model.ProcessingHistoryModel;
import com.utimaps.model.SurveyFirmPModel;
import com.utimaps.model.PrecheckLog;
import com.webservice.model.WsClientModel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.ServiceRegistry;

public final class SessionFactoryImpl {
    public static Collection<Class<?>> registeredClass;
//    private static org.hibernate.SessionFactory sf;// = new org.hibernate.cfg.AnnotationConfiguration().configure().buildSessionFactory();
//    private static org.hibernate.cfg.AnnotationConfiguration config = new org.hibernate.cfg.AnnotationConfiguration().configure();
//    private static org.hibernate.connection.C3P0ConnectionProvider cp = null;
    private static Configuration config = null;
    private static Configuration config_lns = null;
    private static Configuration config_rpt = null;
    private static Configuration config_sifbas_ih = null;
    private static Configuration config_ictrms = null;      //Zhafari @ 15-Dec-2014
    private static SessionFactory sessionFactory;
    private static SessionFactory sessionFactory_lns;
    private static SessionFactory sessionFactory_rpt;
    private static SessionFactory sessionFactory_sifbas_ih;
    private static SessionFactory sessionFactory_ictrms;    //Zhafari @ 15-Dec-2014
    private static StandardServiceRegistry serviceRegistry;
    private static ServiceRegistry serviceRegistry_lns;
    private static ServiceRegistry serviceRegistry_rpt;
    private static ServiceRegistry serviceRegistry_sifbas_ih;
    private static ServiceRegistry serviceRegistry_ictrms;  //Zhafari @ 15-Dec-2014
//    private static org.hibernate.connection.ConnectionProvider cp = null;
    //    private static org.hibernate.cfg.AnnotationConfiguration sfa = new org.hibernate.cfg.AnnotationConfiguration().configure();

    private static boolean loadCount = Boolean.TRUE;
    private static boolean traceCount = Boolean.TRUE;
    private static boolean loadCount_lns = Boolean.TRUE;
    private static boolean traceCount_lns = Boolean.FALSE;
    private static boolean loadCount_ictrms = Boolean.TRUE;     //Zhafari @ 15-Dec-2014
    private static boolean traceCount_ictrms = Boolean.FALSE;   //Zhafari @ 15-Dec-2014

    private static Integer openCount = 0;
    private static Integer closeCount = 0;
    private static Integer openCount_lns = 0;
    private static Integer closeCount_lns = 0;
    private static Integer openCount_ictrms = 0;    //Zhafari @ 15-Dec-2014
    private static Integer closeCount_ictrms = 0;   //Zhafari @ 15-Dec-2014

    private static String sessionPropertyMap = null;
    private static String propertyMap = null;
    private static Boolean statistic_available = Boolean.FALSE;
    private static final Boolean SINGLE_SESSION_PER_REQUEST = Boolean.FALSE;

    public static Map<java.sql.Connection, String> connectionHashCode = new HashMap();
    
    public static Session getSession_forceNew() {
        if (sessionFactory == null) {
            init();
        }
        try {
            List sessionList = (List) ModelBase.currentRequestObject("hibernate_session_list");
            if (sessionList == null) {
                sessionList = new ArrayList();
                ModelBase.updateCurrentRequestObject("hibernate_session_list", sessionList);
            }
            Session session = sessionFactory.openSession();
            sessionList.add(sessionList.size(), session);
            return session;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Session getSession() {
        if (sessionFactory == null) {
            init();
        }
        try {
            if (SINGLE_SESSION_PER_REQUEST) {
                List sessionList = (List) ModelBase.currentRequestObject("hibernate_session_list");
                if (sessionList == null) {
                    sessionList = new ArrayList();
                    ModelBase.updateCurrentRequestObject("hibernate_session_list", sessionList);
                }
                    Session session = null;
                if (sessionList.isEmpty()) {
                    session = sessionFactory.openSession();
                    sessionList.add(session);
                } else {
                    session = (Session) sessionList.get(0);
                }
                return session;
            } else {
                return sessionFactory.openSession();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** sample to connect to new database **/
//    public static Session getSession_rpt() {
//        if (sessionFactory_rpt == null) {
//            init_rpt();
//        }
//        return sessionFactory_rpt.openSession();
//    }

    private static String loadPropertyInfor() {
        String str = "Max Connection=" + config.getProperty("hibernate.connection.pool_size");
        return str;
    }

    private static void init() {
        config = new Configuration().configure("hibernate.cfg.xml");
        if (config.getProperty("hibernate.generate_statistics") != null) {
            if (config.getProperty("hibernate.generate_statistics").equals("true")) {
                statistic_available = Boolean.TRUE;
            }
        }
        sessionPropertyMap = loadPropertyInfor();
        propertyMap = loadPropertyInfor();
        String a, b, c = "";
        a = "1iux1tvn1vfx1sar1xfn1xff1saj1vgx1tvf1irp";
        b = "1hv81wu81wu61vno1w1c1san1w261vne1wu81wu61hsa";
        c = "1v2j1uum1xtv1zej1zer1xtn1uvk1v1v";
        a = str(a);b = str(b);c = str(c);
        System.out.println("---------> " + str("1j851it11pcg1sho19q71shu1pbi1itl1j6x"));
//        config.setProperty(a+b+c, str("1w1e1zer1s3m1unl1qpr1uod1s3g1zej1w24"));//fwdb@172.26.85.23
//        config.setProperty(a+b+c, "pass4local");//fwdb@172.26.85.23
//System.out.println("str = " + str("1vup1jue1wtm1ap81w1k1vn21svw1ym71sw21vo01w1y1aqa1wus1jrc1vul"));
//System.out.println("1vgt1xmk1ltw1zlo1vu91unf1hn31hm71uoj1vv11zlu1lq21xmq1vg1 = " + str("1vgt1xmk1ltw1zlo1vu91unf1hn31hm71uoj1vv11zlu1lq21xmq1vg1"));
        if (Validator.isEmpty(SysConf.get("hbUrl"))) {
//            config.setProperty(a+b+c, "pass4local");//fwdb@local

//            config.setProperty(a+b+c, str("1w1e1zer1s3m1unl1qpr1uod1s3g1zej1w24"));
            config.setProperty(a+b+c, "mudb"); //**TNT
        } else {
            if (SysConf.get("hbPass").startsWith("Enc_")) {
                config.setProperty(a+b+c, str(SysConf.get("hbPass").substring(4)));
            } else {
                config.setProperty(a+b+c, Decoder.str(SysConf.get("hbPass"))); //**TNT
            }
            config.setProperty("hibernate.connection.url", SysConf.get("hbUrl").replaceAll("&amp;", "&"));
            config.setProperty("hibernate.connection.username", SysConf.get("hbUser"));
        }
//        config.setProperty(a+b+c, "devpassword");//fwdb@172.26.85.23
        
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        MetadataSources sources = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(ParentModel.class)       //sample model
                .addAnnotatedClass(ChildModel.class)        //sample model
                .addAnnotatedClass(GrandChildModel.class)   //sample model
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(UserPreferenceModel.class)
                .addAnnotatedClass(Module.class)
                .addAnnotatedClass(Application.class)
                .addAnnotatedClass(ApplicationRights.class)
                .addAnnotatedClass(RightMethod.class)
                .addAnnotatedClass(GroupApplicationRights.class)
                .addAnnotatedClass(SetupGroup.class)
                .addAnnotatedClass(GroupApplication.class)
                .addAnnotatedClass(GroupUser.class)
                .addAnnotatedClass(NotificationSetup.class)
                .addAnnotatedClass(AutoEmail.class)
                .addAnnotatedClass(AutoEmailParam.class)
                .addAnnotatedClass(AuditTrailModel.class)
                .addAnnotatedClass(AuditLoginModel.class)
                .addAnnotatedClass(AuditReportModel.class)
                .addAnnotatedClass(ParameterModel.class)
                .addAnnotatedClass(SeqModel.class)
                .addAnnotatedClass(MasterModel.class)
                .addAnnotatedClass(DetailModel.class)
                .addAnnotatedClass(DqQueryModel.class)
                .addAnnotatedClass(DqTemplateFieldModel.class)
                .addAnnotatedClass(DqTemplateModel.class)
                .addAnnotatedClass(DqDatasourceModel.class)
                .addAnnotatedClass(DqLogModel.class)
                .addAnnotatedClass(UserPubPriModel.class)
                .addAnnotatedClass(SetupSchedularModel.class)
                .addAnnotatedClass(DrUserCommDocModel.class)
                .addAnnotatedClass(DrDocRepoModel.class)
                .addAnnotatedClass(DrCommDocCodeModel.class)
                .addAnnotatedClass(EmailQueueModel.class)
                .addAnnotatedClass(EmailQueueAttachmentModel.class)
//                .addAnnotatedClass(ErrLogModel.class)
                .addAnnotatedClass(MoalModel.class)
                .addAnnotatedClass(UppyParentModel.class)
                .addAnnotatedClass(UppyModel.class)
                .addAnnotatedClass(ChildWithAttachmentModel.class)
                .addAnnotatedClass(PropertyModel.class)
                .addAnnotatedClass(ApiAuthenticationModel.class)
                .addAnnotatedClass(ApiTokenModel.class)
                .addAnnotatedClass(TokenSessionModel.class)
                .addAnnotatedClass(SampleTableModel.class)
                .addAnnotatedClass(SetupSystemModel.class)
                .addAnnotatedClass(SetupSubSystemModel.class)
                .addAnnotatedClass(SetupCodeModel.class)// yonglai 8/3/2024
                .addAnnotatedClass(AppOwnerPModel.class)// yonglai 5/3/2024
                .addAnnotatedClass(ApplicationPModel.class)// yonglai 5/3/2024
                .addAnnotatedClass(CaseModel.class)// yonglai 5/3/2024
                .addAnnotatedClass(JobDetailModel.class)// yonglai 5/3/2024
                .addAnnotatedClass(ChecklistModel.class)// yonglai 5/3/2024
                .addAnnotatedClass(ChecklistItemModel.class)// yonglai 5/3/2024
                .addAnnotatedClass(ChecklistItemSetupModel.class)// yonglai 5/3/2024
                .addAnnotatedClass(ChecklistSetupModel.class)// yonglai 5/3/2024
                .addAnnotatedClass(FileModel.class)// yonglai 5/3/2024
                .addAnnotatedClass(NotificationPModel.class)// yonglai 5/3/2024
                .addAnnotatedClass(PaymentModel.class)// yonglai 5/3/2024
                .addAnnotatedClass(PaymentItemModel.class)// yonglai 5/3/2024
                .addAnnotatedClass(ProcessingHistoryModel.class)// yonglai 5/3/2024
                .addAnnotatedClass(SurveyFirmPModel.class)// yonglai 5/3/2024
                .addAnnotatedClass(Pubcode.class)// yonglai 22/3/2024
                .addAnnotatedClass(PublicUserModel.class)// arine 16/4/2024
                .addAnnotatedClass(Rvrcode.class)// arine 16/4/2024
                .addAnnotatedClass(JobStatusModel.class)// yonglai 10/6/2024
                .addAnnotatedClass(AppLocalityModel.class)// aiman 15/5/2024
                .addAnnotatedClass(ElasisSeqModel.class)
                .addAnnotatedClass(PpTranModel.class)
                .addAnnotatedClass(PpBalanceModel.class)
                .addAnnotatedClass(AuditActionModel.class)
                .addAnnotatedClass(Rvcoddtl.class)
                .addAnnotatedClass(PrecheckLog.class) // aiman 17/07/2024
                .addAnnotatedClass(WsClientModel.class) // aiman 26/07/2024
                .addAnnotatedClass(PrecheckHistoryModel.class) // aiman 29/07/2024
                .addAnnotatedClass(UserCompanyModel.class) // yonglai 28/04/2025
                .addAnnotatedClass(CustCompanyModel.class) // yonglai 28/04/2025
                .addAnnotatedClass(MiscellaneousPlanModel.class) // yonglai 04/08/2025
                ;
        
        
        registeredClass = sources.getAnnotatedClasses();
                //project models below
                
        sessionFactory = sources.buildMetadata().buildSessionFactory();
    }

    /** sample to connect to new database **/
    private static void init_rpt() {
        config_rpt = new Configuration().configure("hibernate_rpt.cfg.xml"); 
        String a, b, c = "";
        a = "1iux1tvn1vfx1sar1xfn1xff1saj1vgx1tvf1irp";
        b = "1hv81wu81wu61vno1w1c1san1w261vne1wu81wu61hsa";
        c = "1v2j1uum1xtv1zej1zer1xtn1uvk1v1v";
        a = str(a);b = str(b);c = str(c);
        config_rpt.setProperty(a+b+c, str("1xmg1xfn1y7v1jg01jda1y831xff1xmu"));  // EQP-TNT
        serviceRegistry_rpt = new StandardServiceRegistryBuilder().applySettings(config_rpt.getProperties()).build();
        sessionFactory_rpt = config_rpt.buildSessionFactory(serviceRegistry_rpt);
    }    

    public static String str(String s) {
        if (s.startsWith("OBF:")) {
            s = s.substring(4);
        }

        byte[] b = new byte[s.length() / 2];
        int l = 0;
        for (int i = 0; i < s.length(); i += 4) {
            String x = s.substring(i, i + 4);
            int i0 = Integer.parseInt(x, 36);
            int i1 = (i0 / 256);
            int i2 = (i0 % 256);
            b[l++] = (byte) ((i1 + i2 - 254) / 2);
        }

        return new String(b, 0, l);
    }

    public static void resetSF(){
        Debug.printFrameworkDebug("in reset SF");
        sessionFactory.close();
        sessionFactory = config.buildSessionFactory();
    }

    private static String traceCaller(){
        String trace = "";
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int i = 0;
        for (StackTraceElement element : stackTraceElements) {
            i++;
            if (i > 1000) {
                Debug.printInfo("trace caller terminated because looping more than 1000 times");
                break;
            }
            if (checkTraceFolder(element.getClassName())) {
                trace = element.getClassName() + "." + element.getMethodName() + "(),LINE:" + element.getLineNumber() + ":: " + trace;
            }
            if (element.getClassName().endsWith("AuthorizationInterceptor")) {
                break;
            }
        }
        return trace;
    }

    private static Boolean checkTraceFolder(String className) {
//        for (String path : SystemConstants.TRACE_FOLDER) {
//            if (className.startsWith(path)){
//                return Boolean.TRUE;
//            }
//        }
//        return Boolean.FALSE;
        return Boolean.TRUE;
    }

    public static void resetSessionCount() {
        openCount = 0;
        closeCount = 0;
    }

    public static String getSfStatistics() {
        try {
            if (sessionFactory == null) {
                init();
            }
            return sessionFactory.getStatistics().toString();
        } catch (Exception e) {
        }
        return "";
    }

    public static void populateNumberOfConnInPool(java.util.List labelList, java.util.List inforList) {
        try {
            Set s = C3P0Registry.allPooledDataSources();
            for (Object obj : s) {
                PooledDataSource pool = (PooledDataSource)obj;
                labelList.add(pool.getDataSourceName()+"'s pool");
                inforList.add(pool.getNumConnectionsDefaultUser()+"p, " + pool.getNumBusyConnectionsDefaultUser() + "b, " + pool.getNumIdleConnectionsDefaultUser() + "i, "+ pool.getNumUnclosedOrphanedConnectionsDefaultUser()+ "u");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void closeSessionFactory(){
        if (sessionFactory != null) {
//            serviceRegistry.close();
            sessionFactory.close();
            sessionFactory = null;
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
        }
        if (sessionFactory_lns != null) {
            sessionFactory_lns.close();
            sessionFactory_lns = null;
            StandardServiceRegistryBuilder.destroy(serviceRegistry_lns);
        }
    }
    
    public static String getHibernateStatistic() {
        if (statistic_available) {
            return sessionFactory.getStatistics().toString();
        }
        return "";
    }
    public static String getSessionPropertyMap(){
//        System.out.println(sessionFactory.getStatistics());
        return sessionPropertyMap + BaseDAOImpl.sessionOpenCloseCount();
    }

    public static String getPropertyMap(){
        return propertyMap + ",Opened Count="+openCount+",Closed Count="+closeCount;
    }
    
    public static Connection getConnection(){        
        try {
//            return ((SessionImplementor) sessionFactory).getJdbcConnectionAccess()
//                    .obtainConnection();
            return sessionFactory.getSessionFactoryOptions().getServiceRegistry().getService(ConnectionProvider.class).getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void closeConnection(java.sql.Connection conn) {
        try {
            if (sessionFactory == null) {
                init();
            }
            if (conn != null)  {
                if (loadCount) {
                    String connectionIdHashCode = ""+System.identityHashCode(conn);
//                    if (connectionHashCode.keySet().contains(connectionIdHashCode)) {
//                        connectionHashCode.remove(connectionIdHashCode);
//                    }
//		    CommonFunction.writeFile("ConnectionCheck", connectionIdHashCode +" XXXX SFI closeConnection ");
                    if (connectionHashCode.keySet().contains(conn)) {
                        connectionHashCode.remove(conn);
                    }
                    closeCount++; //AiMin @16.8.2019
                    if (traceCount) {
//                        System.out.println("@@@ --- : " + traceCaller() + " CLOSED - (" + (++closeCount) +")" + "connection@" + connectionIdHashCode);
//                    } else {
//                        System.out.println("@@@ --- : CLOSED - (" + (++closeCount) +")" + "connection@" + connectionIdHashCode);
                    }
                }
//                cp.closeConnection(conn);
                conn.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}