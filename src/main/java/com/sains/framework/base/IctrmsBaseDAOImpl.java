/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sains.framework.base;

import org.hibernate.Session;

/**
 *
 * @author Zhafari
 */
public class IctrmsBaseDAOImpl extends BaseDAOImpl {
    @Override
    public Session getSession() {
        if (session == null || !session.isOpen()) {
//            session = SessionFactoryImpl.getSession_ictrms();
            if (loadCount) {
                openCount++;
//                System.out.println("openCount = " + openCount);
                String sessionHashCode = ""+System.identityHashCode(session);
                String traceStr = null;
                if (traceCount) {
                    traceStr = traceCaller();
                    //System.out.println(">>> +++ : " + traceCaller() + " OPENED - (" + (++openCount) +")");
                } else {
                    traceStr = ">>> +++ : " + this.getClass().getName() + " OPENED";
                    //System.out.println(">>> +++ : " + this.getClass().getName() + " OPENED - (" + (openCount) +")");
                }
                sessionIdHashCode.put(sessionHashCode, traceStr);
            }
        }
        return session;
    }

    public Object getObjectById(Integer id, Object model) {
        try {
            model = getSession().get(model.getClass(), id);
        } catch (Exception e) {
            checkException(e);
            new LogFunction().logError(this.getClass(), "", e);
        }
        return model;
    }
}
