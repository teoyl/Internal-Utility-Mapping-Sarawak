/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web.dao;

import com.sains.framework.base.BaseCUDDAO;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.JobDetailModel;
import java.util.Map;

/**
 *
 * @author yonglai
 */
public interface SubmissionJobCUDDAO extends BaseCUDDAO<JobDetailModel>{
    public JobDetailModel updateApplication(JobDetailModel model) throws Exception;
    public JobDetailModel updateSubmission(JobDetailModel model) throws Exception;
    public String completeApplication(JobDetailModel model) throws Exception;
    public String deleteUSCSLetter(JobDetailModel model, String letterType, String letterSubType) throws Exception;
    public Integer signUSCSLetter(JobDetailModel model, String processType, String letterType) throws Exception;
    public Integer insertSignedUSCSLetter(JobDetailModel model, String fileId, String filePath, String fileName, String contentType, String fileType, String fileSubType) throws Exception;
    public String revokeUSCSLetter(JobDetailModel model, String fileType) throws Exception;
    public String clearSubmissionChecklist(JobDetailModel model) throws Exception ;
    public String clearRejectedChecklist(JobDetailModel model) throws Exception ;
    public String clearAllSubmissionChecklist(JobDetailModel model) throws Exception ;
    public Integer updateApprovalDate(JobDetailModel model) throws Exception ;
    public Map updateJobProgress(String usjNo, String divNo, String jobAction, String statusDate) throws Exception;
}
