package com.sains.framework.base;

import org.hibernate.Session;

public interface BaseCUDDAO<T>{
    public void groupDelete(String[] ids, T model) throws Exception;
    public void delete(String id, T model) throws Exception;
    public void deleteModel(T model) throws Exception;
    public void deleteModelWithPrePost(T model) throws Exception;
    public void manualUpdate(T model) throws Exception;
    public void manualUpdateWithSession(org.hibernate.Session session, T model) throws Exception;
    public void insert(T obj) throws Exception;
    public void insertWithSession(Session pSession, T obj) throws Exception;
    public void frameworkInsert(T obj) throws Exception;
    public T update(T obj) throws Exception;
    public T updateWithSession(Session pSession, T obj) throws Exception;
    public T frameworkUpdate(T obj) throws Exception;
    public void directUpdate(T model) throws Exception;
    public void directUpdateWithSession(org.hibernate.Session session, T model) throws Exception;
    public void auditDeleteWithSession(org.hibernate.Session session, Object model, Object strUpdatedBy, boolean deleteModel) throws Exception;
    public Object frameworkDelete(String[] ids, Class modelClass) throws Exception;

}
