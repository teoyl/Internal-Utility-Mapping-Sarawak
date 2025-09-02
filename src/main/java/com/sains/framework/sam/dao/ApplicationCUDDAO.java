package com.sains.framework.sam.dao;

import com.sains.framework.base.BaseCUDDAO;
import com.sains.framework.model.Application;
import com.sains.framework.model.GroupApplication;

public interface ApplicationCUDDAO extends BaseCUDDAO<Application>{
    public Application updateGroup(Application application) throws Exception;
    public void deleteGroup(String[] ids, GroupApplication groupApp) throws Exception;
}
