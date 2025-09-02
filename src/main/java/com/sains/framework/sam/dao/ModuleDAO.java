package com.sains.framework.sam.dao;

import com.sains.framework.model.Module;
import com.sains.framework.base.BaseDAO;

public interface ModuleDAO extends BaseDAO<Module>{
    public Module getModuleByCode(String moduleCode, Module module);
}
