/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web.dao;

import com.sains.framework.base.BaseDAO;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.JobDetailModel;

/**
 *
 * @author yonglai
 */
public interface IssuanceJobDAO extends IssuanceJobCUDDAO, BaseDAO<JobDetailModel> {
    
}