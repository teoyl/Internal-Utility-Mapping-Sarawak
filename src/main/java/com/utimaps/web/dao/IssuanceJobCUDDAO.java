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
public interface IssuanceJobCUDDAO extends BaseCUDDAO<JobDetailModel>{
    public JobDetailModel updateIssuance(JobDetailModel model) throws Exception;
    public String completeIssuance(JobDetailModel model) throws Exception;
    public String deleteUSJFile(JobDetailModel model) throws Exception;
    public Integer signUSJLetter(JobDetailModel model) throws Exception;
    public String revokeUSJLetter(JobDetailModel model) throws Exception;
    public Integer insertSignedUSJLetter(JobDetailModel model, String fileId, String filePath, String fileName, String contentType, String fileType) throws Exception;
}
