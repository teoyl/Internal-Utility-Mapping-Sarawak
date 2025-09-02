package com.sains.framework.sam.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.model.Module;


public class ModuleDAOImpl extends BaseDAOImpl<Module> implements ModuleDAO {
    public Module getModuleByCode(String moduleCode, Module module) {
        Map param = new HashMap();
        param.put("module_code", moduleCode);
        List moduleList = list(param, Module.class, false);
        if (moduleList != null && moduleList.size() > 0) {
            return (Module) moduleList.get(0);
        }
        return null;
    }
}
