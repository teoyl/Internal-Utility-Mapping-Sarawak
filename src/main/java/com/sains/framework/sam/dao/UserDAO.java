package com.sains.framework.sam.dao;

import com.sains.framework.model.User;
import com.sains.framework.base.BaseDAO;


public interface UserDAO extends UserCUDDAO, BaseDAO<User>{
    public User processLogin(String userId, String password,String systemType_, String loginFrom) throws Exception;
    public String getAuditLoginStatus(); // thensw@26.09.2013 **copied from elodgement
}