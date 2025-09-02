package com.sains.framework.sam.dao;

import com.sains.framework.sam.dao.ApplicationCUDDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import com.sains.framework.model.Application;
import com.sains.framework.model.ApplicationRights;
import com.sains.framework.model.GroupApplication;
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.Module;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.base.BaseDAO;


public interface ApplicationDAO extends ApplicationCUDDAO, BaseDAO<Application>{
	public Application getApplicationByCode(String applicationCode, Application application);
        public Application getApplicationByActionClass(String actionClass, Application application);
        public List getAdminApplications();
}