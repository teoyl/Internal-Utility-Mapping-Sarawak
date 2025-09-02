package com.sains.framework.sam.dao;

import com.sains.framework.model.GroupApplication;
import com.sains.framework.model.GroupUser;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.base.BaseDAO;
import java.util.List;


public interface UserGroupDAO extends UserGroupCUDDAO, BaseDAO<SetupGroup>{
	public SetupGroup getUserGroupByCode(String groupCode, SetupGroup userGroup);
        public void addGroupUser(SetupGroup group, String[] userIds) throws Exception;
        public void deleteGroupUser(SetupGroup group, String[] userIds) throws Exception;
}