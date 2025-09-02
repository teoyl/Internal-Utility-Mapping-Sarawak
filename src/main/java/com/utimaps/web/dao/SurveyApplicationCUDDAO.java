/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web.dao;

import com.sains.framework.base.BaseCUDDAO;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.JobDetailModel;

/**
 *
 * @author yonglai
 */
public interface SurveyApplicationCUDDAO extends BaseCUDDAO<ApplicationPModel>{
    public ApplicationPModel updateApplication(ApplicationPModel model) throws Exception;
    public String completeApplication(ApplicationPModel model) throws Exception;
    public String deleteUPS10Letter(ApplicationPModel model) throws Exception;
    public String clearApplicationChecklist(ApplicationPModel model) throws Exception;
}
