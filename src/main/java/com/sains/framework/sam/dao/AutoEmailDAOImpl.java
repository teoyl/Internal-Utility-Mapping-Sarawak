package com.sains.framework.sam.dao;

import com.sains.common.util.SystemConstants;
import com.sains.framework.base.LogFunction;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.model.AutoEmailParam;


public class AutoEmailDAOImpl extends BaseDAOImpl<AutoEmail> implements AutoEmailDAO{

	public AutoEmail getAutoEmailSetup(String actionName, String methodName, String actionStatus, AutoEmail autoEmail){
		Map param = new HashMap();
		param.put("action_name", actionName);
                param.put("method_name", methodName);
                param.put("action_status", actionStatus);
                param.put("auto_type", SystemConstants.AutoEmail_Type.AI);
		List moduleList = list(param, AutoEmail.class, false);
		if (moduleList != null && moduleList.size() > 0){
			return (AutoEmail)moduleList.get(0);
		}
		return null;
	}
	
	public AutoEmail getAutoEmailByCode(String code, AutoEmail model) {
        Map param = new HashMap();
        param.put("code", code);
        List list = list(param, AutoEmail.class, false);
        if (list != null && list.size() > 0) {
            return (AutoEmail) list.get(0);
        }
        return null;
    }

        @Override
	public synchronized void create(AutoEmail autoEmail) throws Exception{
            Session session = getSession();
            Transaction tx = session.beginTransaction();
            try {
                session.saveOrUpdate(autoEmail);
                for (AutoEmailParam emailParam : autoEmail.getAutoEmailParam()){
                    emailParam.setAuto_email_id(autoEmail.getAuto_email_id());
                    session.saveOrUpdate(emailParam);
                }
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
	}

	@Override
	public synchronized void update(AutoEmail autoEmail, List<AutoEmailParam> deletedList) throws Exception{
            Session session = getSession();
            Transaction tx = session.beginTransaction();
            try {
                AutoEmail dbAutoEmail = new BaseDAOImpl<AutoEmail>().setUpdateProperties(autoEmail, session);
                if (dbAutoEmail != null){
                    dbAutoEmail.setAutoEmailParam(autoEmail.getAutoEmailParam());
                    autoEmail = dbAutoEmail;
                }
                session.saveOrUpdate(autoEmail);
                for (AutoEmailParam emailParam : autoEmail.getAutoEmailParam()){
                    //emailParam.setAuto_email_id(autoEmail.getAuto_email_id());
                    session.saveOrUpdate(emailParam);
                }
                for (AutoEmailParam emailParam : deletedList){
                    session.delete(emailParam);
                }
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
	}

	public synchronized void delete(String[] deletedIds) throws Exception{
            Session session = getSession();
            Transaction tx = session.beginTransaction();
            try {
                for (String strAutoEmailId : deletedIds){
                    AutoEmail autoEmail = new AutoEmail();
                    autoEmail = (AutoEmail) session.get(autoEmail.getClass(), strAutoEmailId);
                    for (AutoEmailParam emailParam : autoEmail.getAutoEmailParam()){
                        session.delete(emailParam);
                    }
                    session.delete(autoEmail);
                }
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
	}

}
