package com.sains.framework.base;

import com.sains.common.util.SystemConstants;

public abstract class BaseMobileActionSupport<T> extends BaseAction<T> {
    private static final long serialVersionUID = -1577947567038101585L;
    protected ServiceFactory serviceFactory = ServiceFactory.getInstance();
    protected T model ;
    private String id = null;
    
    public BaseMobileActionSupport() {
        super();
        systemType_ = SystemConstants.SYSTEM_TYPE.MOBILE;
    }
    public BaseMobileActionSupport(org.hibernate.Session session) {
        super(session);
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
}