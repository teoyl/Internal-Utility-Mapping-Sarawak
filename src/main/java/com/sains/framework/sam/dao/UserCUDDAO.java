package com.sains.framework.sam.dao;

import com.sains.framework.base.BaseCUDDAO;
import com.sains.framework.model.User;

public interface UserCUDDAO extends BaseCUDDAO<User>{
    public void groupDeleteUser(String[] ids, User user) throws Exception;
    public void insertActivateUser(User user) throws Exception;
}