package com.sains.framework.sam.dao;

import com.sains.framework.base.BaseCUDDAO;
import java.util.List;
import com.sains.framework.model.AutoEmail;
import com.sains.framework.model.AutoEmailParam;
import com.sains.framework.base.BaseDAO;

public interface AutoEmailCUDDAO extends BaseCUDDAO<AutoEmail>{
    public void create(AutoEmail application) throws Exception;
    public void update(AutoEmail application, List<AutoEmailParam> deletedList) throws Exception;
    public void delete(String[] deletedIds) throws Exception;
}
