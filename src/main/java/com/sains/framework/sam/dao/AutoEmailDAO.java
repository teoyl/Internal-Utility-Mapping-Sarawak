package com.sains.framework.sam.dao;

import java.util.List;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.model.AutoEmailParam;
import com.sains.framework.base.BaseDAO;

public interface AutoEmailDAO extends AutoEmailCUDDAO, BaseDAO<AutoEmail>{
    public AutoEmail getAutoEmailSetup(String actionName, String methodName, String actionStatus, AutoEmail autoEmail);
    public AutoEmail getAutoEmailByCode(String code, AutoEmail model);
}
