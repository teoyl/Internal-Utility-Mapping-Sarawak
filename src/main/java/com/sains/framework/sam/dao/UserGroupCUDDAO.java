package com.sains.framework.sam.dao;

import com.sains.framework.base.BaseCUDDAO;
import com.sains.framework.model.GroupApplication;
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.base.BaseDAO;
import java.util.List;


public interface UserGroupCUDDAO extends BaseCUDDAO<SetupGroup>{
	public void create(SetupGroup obj) throws Exception;
	public void update(SetupGroup obj, List<GroupApplication> deletedGroupApplication, List<GroupUser> deletedGroupUser) throws Exception;
        public void addGroupUser(SetupGroup group, String[] userIds) throws Exception;  //added by Delvene @ 08-Jul-2013
}