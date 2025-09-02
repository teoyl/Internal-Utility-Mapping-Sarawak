package com.sains.framework.base.web;

import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSession;

public class SessionListener implements HttpSessionListener {

//    public static Set<HttpSession> activeSessionSet = new HashSet();
    public static Map<String, HttpSession> activeSessionMap = new HashMap();
    public static Map<String, String> expiredFromMap = new HashMap();

    public static void listActiveSession() {
        for (String key : activeSessionMap.keySet()) {
            HttpSession session = activeSessionMap.get(key);
            Debug.printFrameworkDebug("SessionId: " + session.getId() + ", loginId = " + session.getAttribute("loginId")+ ", loginIp = " + session.getAttribute("loginIp"));
        }
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        //do nothing.
        Debug.printFrameworkDebug("session created : " + se.getSession().getId());
        // HttpSession session = se.getSession();
        // session.setAttribute("test", "abc" + activeSessions);
        // session.setAttribute("test2", "XXX");
        // System.out.println("created session.id = " + session.getId());
        // System.out.println("activeSessions = " + activeSessions);
        // String sessionId=session.getId();
        // String kullanici_adi=(String)session.getAttribute("sifre");
        // activeSessions++;
        //
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Debug.printFrameworkDebug("Expired sessionDestroyed ");
        HttpSession session = se.getSession();
        
        Debug.printFrameworkDebug("session id = " + session.getId());
        Debug.printFrameworkDebug("session.loginId = " + session.getAttribute("loginId"));
        if (session.getAttribute("from_site")!=null && !session.getAttribute("from_site").equals("UTC")) {
            expiredFromMap.put(session.getId(), (String)session.getAttribute("from_site"));
        }
//        if (session.getAttribute("loginId") != null && !Validator.isEmpty(session.getAttribute("loginId").toString())) {
//            activeSessionMap.remove((String)session.getAttribute("loginId"));
//            Debug.printFrameworkDebug("Expired sessionDestroyed IP" + session.getAttribute("loginIp").toString());
//            if (session.getAttribute("login_user_type").toString().equals(SystemConstants.SYSTEM_TYPE.PUBLIC)) {
//                new CommonFunction().auditLogin(SystemConstants.AUDIT_LOGIN.ACTION.TIMEOUT, SystemConstants.AUDIT_LOGIN.TYPE.PUBLIC, SystemConstants.AUDIT_LOGIN.STATUS.SUCCESS, session.getAttribute("loginId").toString(),session.getAttribute("loginIp").toString());
//            } else {
//                new CommonFunction().auditLogin(SystemConstants.AUDIT_LOGIN.ACTION.TIMEOUT, SystemConstants.AUDIT_LOGIN.TYPE.INTERNAL, SystemConstants.AUDIT_LOGIN.STATUS.SUCCESS, session.getAttribute("loginId").toString(),session.getAttribute("loginIp").toString());
//            }
//        }else{
//            Debug.printFrameworkDebug("all empty");
//        }
    }
}
