package com.sains.framework.base.web;

import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.CommonFunction;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSession;

public class SessionTimeoutListener implements HttpSessionListener {
    public static Set<HttpSession> activeSessionSet = new HashSet();
    public static void listActiveSession() {
        for (HttpSession session : activeSessionSet) {
            System.out.println("SessionId: " + session.getId() + ", loginId = " + session.getAttribute("loginId"));            
        }
    }
     @Override
     public void sessionCreated(HttpSessionEvent se) {
         //do nothing.
//         System.out.println("session created 88888888888888888888888888888888888888888888888");
//          HttpSession session = se.getSession();
//          session.setAttribute("test", "abc" + activeSessions);
//          session.setAttribute("test2", "XXX");
//          System.out.println("created session.id = " + session.getId());
//          System.out.println("activeSessions = " + activeSessions);
//          String sessionId=session.getId();
//          String kullanici_adi=(String)session.getAttribute("sifre");
//          activeSessions++;
//          
     }

     @Override
     public void sessionDestroyed(HttpSessionEvent se) {
         HttpSession session = se.getSession();
         if (session.getAttribute("loginId") != null && !Validator.isEmpty(session.getAttribute("loginId").toString())) {
             activeSessionSet.remove(session);
             if (session.getAttribute("loginSystemType_").toString().equals(SystemConstants.SYSTEM_TYPE.ESS)) {
                 new CommonFunction().auditLogin(SystemConstants.AUDIT_LOGIN.ACTION.TIMEOUT, SystemConstants.AUDIT_LOGIN.TYPE.ESS, SystemConstants.AUDIT_LOGIN.STATUS.SUCCESS, session.getAttribute("loginId").toString());
             } else {
                 new CommonFunction().auditLogin(SystemConstants.AUDIT_LOGIN.ACTION.TIMEOUT, SystemConstants.AUDIT_LOGIN.TYPE.INTERNAL, SystemConstants.AUDIT_LOGIN.STATUS.SUCCESS, session.getAttribute("loginId").toString());
             }
         }
     }
}