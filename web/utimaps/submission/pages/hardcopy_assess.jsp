<%-- 
    Document   : hardcopy_assess
    Created on : May 22, 2024, 1:02:25 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingHardcopy">
        <button class="accordion-button <s:if test='!activeAccordion.equals("1")'>collapsed</s:if> fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseHardcopy" aria-expanded="false" aria-controls="flush-collapseHardcopy">
            <i class="fas fa-tasks me-2"></i><s:text name="utimaps.form.submission.hardcopySubmission" />
            
            <s:if test='model.wf_status_2 > "165"'>
                <span class="position-absolute end-50px text-success">[<s:text name = "utimaps.form.label.completed" />]</span>
            </s:if>
            <s:else>
                <span class="position-absolute end-50px text-primary">[<s:text name = "utimaps.form.label.inProgress" />]</span>
            </s:else>
        </button>
    </h2>
    <div id="flush-collapseHardcopy" class="accordion-collapse collapse <s:if test='activeAccordion.equals("1")'>show</s:if>" aria-labelledby="flush-headingHardcopy" data-bs-parent="#accordionFlushApplication">
        <div class="accordion-body">
            <div class="row">
            <s:if test='model.wf_status_2.equals("161")'>
                <div class="col-lg-12 mb-3 text-center">
                    <form action="startHardcopyAssessmentSubmission" method="post" id="startHardcopyAssessmentForm">
                        <s:hidden name="job_id" id="job_id" value="%{model.job_id}"/>
                        <s:hidden name="jobId" id="jobId" value="%{model.job_id}"/>
                        <s:hidden name="case_id" id="case_id" value="%{model.case_id}"/>
                        <s:hidden name="actionName" id="actionName" value="%{actionName}"/>
                        <s:hidden name="taskId_" value="%{taskId_}"/>
                        <s:hidden name="wfActivityCode" value="%{wfActivityCode}"/> 
                        <s:hidden name="model.wf_status_2" id="model.wf_status_2" value="%{model.wf_status_2}"/>
                        <s:hidden name="control_sv_flag" id="control_sv_flag" value="%{model.control_sv_flag}"/>
                        <s:hidden name="comp_completed" id="comp_completed" value="%{model.comp_completed}"/>
                
                        <button class="btn btn-primary" id="startHardcopyAssessmentSubmission" type="submit"><s:text name="utimaps.form.label.hardcopy.startJob" /></button>
                    </form>
                </div>
            </s:if>
            <s:else>
                 <s:if test='(checkHasChecklist(model.job_id, "U21"))'>
                    <div id="pastChecklistDiv"><jsp:include page="/utimaps/job_listing/modalPastChecklist.jsp"></jsp:include></div>
                </s:if>
                <form action="processUpdate2JobSubmission" method="post" id="saveEditHardcopyForm">
                    <s:hidden name="job_id" id="job_id" value="%{model.job_id}"/>
                    <s:hidden name="jobId" id="jobId" value="%{model.job_id}"/>
                    <s:hidden name="case_id" id="case_id" value="%{model.case_id}"/>
                    <s:hidden name="actionName" id="actionName" value="%{actionName}"/>
                    <s:hidden name="taskId_" value="%{taskId_}"/>
                    <s:hidden name="wfActivityCode" value="%{wfActivityCode}"/> 
                    <s:hidden name="model.wf_status_2" id="model.wf_status_2" value="%{model.wf_status_2}"/> 
                    <s:hidden name="processType" id="processType" value="162"/>
                    <s:hidden name="checklist_id" id="checklist_id" value="%{model.u21ChecklistModel.checklist_id}"/> 
                    <s:hidden name="model.u21ChecklistModel.check_id" id="model.u21ChecklistModel.check_id" value="%{model.u21ChecklistModel.check_id}"/> 
                    <s:hidden name="model.u21ChecklistModel.case_id" id="model.u21ChecklistModel.case_id" value="%{model.job_id}"/> 
                    <s:hidden name="model.u21ChecklistModel.check_type" id="model.u21ChecklistModel.check_type" value="U21"/> 
                    <s:hidden name="control_sv_flag" id="control_sv_flag" value="%{model.control_sv_flag}"/>
                    <s:hidden name="comp_completed" id="comp_completed" value="%{model.comp_completed}"/>

                    <div class="col-md-12 text-end mb-1">
                        <s:if test='rightToUpdate && model.wf_status_2.startsWith("16")'>
                            <s:if test='(checkHasChecklist(model.job_id, "U21"))'>
                                <a href="#pastChecklistModal" data-toggle="modal" data-target="#pastChecklistModal" data-content-type="U21" class="btn btn-primary viewChecklistBtn mb-1" type="button"><s:text name="utimaps.form.button.viewPastChecklist"/></a>
                            </s:if>
                            <a href="pdfViewerSubmission?pType=U21&pJobId=<s:property value="model.job_id" />" target="_blank" class="btn btn-info mb-1" type="button"><s:text name="utimaps.form.button.printChecklist" /></a>
                        </s:if>
                    </div>
                    <div class="col-md-12 text-end mb-3">
                        <s:if test='rightToUpdate && model.wf_status_2.startsWith("16")'>
                            <button class="btn btn-success" id="completeHardcopySubmission" type="submit"><s:text name = "utimaps.form.button.accept" /></button>
                            <button class="btn btn-primary" id="saveEditHardcopySubmission" type="submit"><i class="fas fa-save"></i> <s:text name = "utimaps.form.button.save" /></button>
                        </s:if>
                    </div>

                    <div class="col-md-12 mb-3">
                        <div class="card mb-3">
                            <div class="card-header bg-primary-subtle">
                                <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.decisionHardcopy" /></h5>
                            </div>
                            <div class="card-body">
                                <div class="btn-group" role="group" aria-label="Decision status">
                                    <input class="btn-check" type="radio" name="model.u21ChecklistModel.check_status" id="checkHardcopyStatus_A" value="A" 
                                       <s:if test='model.u21ChecklistModel.check_status.equals("A")'>checked</s:if>/>
                                    <label class="btn btn-outline-success" for="checkHardcopyStatus_A"><s:text name = "utimaps.form.label.accept" /></label>
                                    
                                    <input class="btn-check" type="radio" name="model.u21ChecklistModel.check_status" id="checkHardcopyStatus_R" value="R" 
                                           <s:if test='model.u21ChecklistModel.check_status.equals("R")'>checked</s:if> />
                                    <label class="btn btn-outline-danger" for="checkHardcopyStatus_R"><s:text name = "utimaps.form.label.reject" /></label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-12 mb-3 hardcopy-received-date-card">
                        <div class="card">
                            <div class="card-body">
                                <label class="col-md-3 col-form-label" for="model_hardcopy_received_date_str"><s:text name="utimaps.form.label.hardcopyRcvdDate"/></label>
                                <div class="col-md-6">
                                    <div class="input-group">
                                        <s:textfield theme="simple" class="form-control date_picker" name = "model.hardcopy_received_date_str" value = "%{model.hardcopy_received_date_str}" autocomplete="off" readonly="%{noRightUpdate}" disabled="%{noRightUpdate}"/>
                                        <span class="input-group-append">
                                            <button class="btn btn-search btn-outline-secondary bg-white border border-start-0" type="button">
                                                <i class="fas fa-calendar-alt"></i>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                                        
                    <div class="col-md-12" id="">
                        <h6 class="pb-1 border-bottom border-bottom-lg-1 text-uppercase fw-bold fs-1" data-anchor="data-anchor"><s:text name="utimaps.checklist.label.docSubmitted" /></h6>
                        
                        <h5 class="p-1 border-bottom border-bottom-lg-1 bg-secondary text-white mb-0"><s:text name="utimaps.checklist.hardcopy" /></h5>
                        <div class="table-responsive scrollbar" >
                            <table class="table table-hover table-striped overflow-hidden " id="checklist_table">
                                <thead>
                                    <tr>
                                        <th scope="col" class="col-md-0 fs-smaller"><s:text name ="utimaps.checklist.label.no" /></th>
                                        <th scope="col" class="col-md-3 fs-smaller"><s:text name ="utimaps.checklist.label.fileDesc" /></th>
                                        <th scope="col" class="col-md-2 fs-smaller"><s:text name ="utimaps.checklist.label.docCheck" /></th>
                                        <th scope="col" class="col-md-5 fs-smaller"><s:text name ="utimaps.checklist.label.processingRemarks" /></th>
                                        <th scope="col" class="col-md-2 fs-smaller">
                                            <span data-bs-toggle="tooltip" data-bs-placement="right" title="<s:text name ="utimaps.checklist.label.supportDocInfo" />">
                                                <s:text name ="utimaps.checklist.label.supportDoc" />
                                                <i class="fas fa-info-circle h4 mb-0"></i>
                                            </span>
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:if test="model.u11ChecklistSetupModel.checklistItemList.size() > 0">
                                        <tr>
                                            <td class="table-primary" colspan="5"><strong><s:text name="utimaps.checklist.hardcopyPart1" /></strong></td>
                                        </tr>
                                        <s:iterator value="model.u11ChecklistSetupModel.checklistItemList" status="listStatus" var="checklistItem">
                                            <tr class="<s:if test="#listStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                                <s:hidden name="model.u11ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].cl_id" value="%{#checklistItem.checklistResultList[0].cl_id}" />
                                                <s:hidden name="model.u11ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].case_id" value="%{model.job_id}" />
                                                <s:hidden name="model.u11ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].check_id" value="%{model.u21ChecklistModel.check_id}" />
                                                <s:hidden name="model.u11ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].ci_id" value="%{#checklistItem.ci_id}" />
                                                <td colspan="2" class="col-md-3">
                                                    <div class="d-block">
                                                        <s:hidden name="model.u11ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].ci_desc" value="%{#checklistItem.ci_desc}" />
                                                        <h6 class="mb-0 fw-bold"><s:property value="%{#checklistItem.ci_sequence}" />. <s:property value="%{#checklistItem.ci_desc}" /></h6>
                                                        <p class="mb-0"><small><pre class="ms-3 cd-notes"><s:property value="%{#checklistItem.ci_notes}" /></pre></small></p>
                                                        <b><small>Last checked date: <s:property value = "%{#checklistItem.checklistResultList[0].updated_date_str}" /></small></b>
                                                    </div>
                                                    <div class="d-flex align-items-center mt-3">
                                                        <div class="col-md-3">
                                                            <div class="ms-2 circle-div text-uppercase">
                                                                <s:if test="#checklistItem.checklistFileModelList2[0].file_ext != ''"><s:property value="%{#checklistItem.checklistFileModelList2[0].file_ext}" /></s:if><s:else>-</s:else>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-9">
                                                            <p class="checklist-file text-truncate max-w-200">
                                                                <a class="color-file-<s:property value="%{#checklistItem.checklistFileModelList2[0].file_status}" />" href="viewTempFileAttachment?uploadID=<s:property value="%{#checklistItem.checklistFileModelList2[0].file_id}" />" target="_blank">
                                                                    <s:property value="%{#checklistItem.checklistFileModelList2[0].file_name}" />
                                                                </a>
                                                            </p>
                                                            <div class="d-block">
                                                                <p class="mb-0">
                                                                    <small>
                                                                        <pre class="cd-notes"><s:property value="%{#checklistItem.ci_notes}" /></pre>
                                                                    </small>
                                                                </p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td class="col-md-2">
                                                    <div class="form-check">
                                                        <input class="hardcopyForm form-check-input" id="cl_result_c_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.u11ChecklistSetupModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="C" <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("C")}'>Checked</s:if> />
                                                        <label class="form-check-label" for="cl_result_c_<s:property value='%{#checklistItem.id}' />">Complete/Correct</label>
                                                    </div>
                                                    <div class="form-check">
                                                        <input class="hardcopyForm form-check-input" id="cl_result_i_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.u11ChecklistSetupModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="E" <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("E")}'>Checked</s:if> />
                                                        <label class="form-check-label" for="cl_result_i_<s:property value='%{#checklistItem.id}' />">Incomplete/Incorrect</label>
                                                    </div>
                                                    <div class="form-check">
                                                        <input class="hardcopyForm form-check-input" id="cl_result_n_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.u11ChecklistSetupModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="N" 
                                                            <s:if test='model.control_sv_flag.equals("Y")'>
                                                                <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("N")}'>Checked</s:if> 
                                                            </s:if>
                                                            <s:else>
                                                                <s:if test='%{#checklistItem.checklistResultList.size() > 0}'>
                                                                    <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("N")}'>Checked</s:if> 
                                                                </s:if>
                                                                <s:else>Checked</s:else>
                                                            </s:else>
                                                            />
                                                        <label class="form-check-label" for="cl_result_n_<s:property value='%{#checklistItem.id}' />"><s:text name="utimaps.checklist.label.notapplicable" /></label>
                                                    </div>
                                                </td>
                                                <td class="col-md-5">
                                                    <s:textarea theme="simple" name="model.u11ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].cl_remarks" value="%{#checklistItem.checklistResultList[0].cl_remarks}" cssClass="form-control" rows="5" maxlength="1000"/>
                                                </td>

                                                <td class="col-md-2">
                                                    <s:if test="#checklistItem.checklistResultList[0].checklistSupportFileList.size() > 0">
                                                        <s:iterator value = "#checklistItem.checklistResultList[0].checklistSupportFileList" status="supportFileStatus" var="supportFile">
                                                            <!--<p class="mb-0"><a href="viewTempFileUppy?uploadID=<s:property value="%{#supportFile.file_id}" />" target="_blank"><s:property value="%{#supportFile.original_file_name}" /></a></p>-->
                                                            <s:hidden id="drDocName" name="%{#supportFile.dr_doc_name}" value="%{#supportFile.dr_doc_name}"/>
                                                            <s:include value="/base/uppyIncludeSingleFile_utimaps.jsp">
                                                                <s:param name="uploadUrl_">uploadInternalAttachment</s:param>
                                                                <s:param name="uppyFieldName_"><s:property value="%{#checklistItem.ci_datatype}" /></s:param>
                                                                <s:param name="uppyHiddenName_">uppySample2FileId</s:param>
                                                                <s:param name="drFileCode_">CM</s:param>
                                                                <s:param name="hideArrow">Y</s:param>
                                                                <s:param name="uppyFileList" value="testList"/>
                                                                <s:param name="uploadParams">drAppCode_=uppySample2&uploadRecordId_=<s:property value="%{model.job_id}"/>&ciId=<s:property value="%{#checklistItem.ci_id}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                                                                <s:param name="uploadedFileName"><s:property value="%{#supportFile.original_file_name}"/></s:param>
                                                                <s:param name="uploadedExt"><s:property value="%{#supportFile.file_ext}"/></s:param>
                                                                <s:param name="uploadedDate"><s:property value="%{#supportFile.created_date_str}"/></s:param>
                                                                <s:param name="uploadedFileId"><s:property value="%{#supportFile.file_id}"/></s:param>
                                                                <s:param name="allowedFileTypes">'.pdf', '.zip'</s:param>
                                                                <s:param name="theRecord_id"><s:property value="%{#supportFile.file_id}"/></s:param>
                                                                <s:param name="ci_id"><s:property value="%{#checklistItem.ci_id}"/></s:param>
                                                                <s:param name="checklist_type">U21</s:param>
                                                            </s:include>
                                                        </s:iterator>
                                                    </s:if>
                                                    <s:else>
                                                        <s:include value="/base/uppyIncludeSingleFile_utimaps.jsp">
                                                            <s:param name="uploadUrl_">uploadInternalAttachment</s:param>
                                                            <s:param name="uppyFieldName_"><s:property value="%{#checklistItem.ci_datatype}" /></s:param>
                                                            <s:param name="uppyHiddenName_">uppySample2FileId</s:param>
                                                            <s:param name="drFileCode_">CM</s:param>
                                                            <s:param name="hideArrow">Y</s:param>
                                                            <s:param name="uppyFileList" value="testList"/>
                                                            <s:param name="uploadParams">drAppCode_=uppySample2&uploadRecordId_=<s:property value="%{model.job_id}"/>&ciId=<s:property value="%{#checklistItem.ci_id}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                                                            <s:param name="uploadedFileName"></s:param>
                                                            <s:param name="uploadedExt"></s:param>
                                                            <s:param name="uploadedDate"></s:param>
                                                            <s:param name="uploadedFileId"></s:param>
                                                            <s:param name="allowedFileTypes">'.pdf', '.zip'</s:param>
                                                            <s:param name="theRecord_id"></s:param>
                                                            <s:param name="ci_id"><s:property value="%{#checklistItem.ci_id}"/></s:param>
                                                            <s:param name="checklist_type">U21</s:param>
                                                        </s:include>
                                                    </s:else>
                                                </td>
                                            </tr>
                                        </s:iterator>
                                    </s:if>
                                            
                                    <s:if test="model.u21ChecklistSetupModel.checklistItemList.size() > 0">
                                        <tr>
                                            <td class="table-primary" colspan="5"><strong><s:text name="utimaps.checklist.hardcopyPart2" /></strong></td>
                                        </tr>
                                        <s:iterator value="model.u21ChecklistSetupModel.checklistItemList" status="listStatus" var="checklistItem">
                                            <tr class="<s:if test="#listStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                                <s:hidden name="model.u21ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].cl_id" value="%{#checklistItem.checklistResultList[0].cl_id}" />
                                                <s:hidden name="model.u21ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].case_id" value="%{model.job_id}" />
                                                <s:hidden name="model.u21ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].check_id" value="%{model.u21ChecklistModel.check_id}" />
                                                <s:hidden name="model.u21ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].ci_id" value="%{#checklistItem.ci_id}" />
                                                <td colspan="2" class="col-md-3">
                                                    <div class="d-block">
                                                        <s:hidden name="model.u21ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].ci_desc" value="%{#checklistItem.ci_desc}" />
                                                        <h6 class="mb-0 fw-bold"><s:property value="%{#checklistItem.ci_sequence}" />. <s:property value="%{#checklistItem.ci_desc}" /></h6>
                                                        <p class="mb-0"><small><pre class="ms-3 cd-notes"><s:property value="%{#checklistItem.ci_notes}" /></pre></small></p>
                                                        <b><small><s:text name ="utimaps.checklist.label.lastCheckedDate" />: <s:property value = "%{#checklistItem.checklistResultList[0].updated_date_str}" /></small></b>
                                                    </div>
                                                    <div class="d-flex align-items-center mt-3">
                                                        <div class="col-md-3">
                                                            <div class="ms-2 circle-div text-uppercase">
                                                                <s:if test="#checklistItem.checklistFileModelList2[0].file_ext != ''"><s:property value="%{#checklistItem.checklistFileModelList2[0].file_ext}" /></s:if><s:else>-</s:else>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-9">
                                                            <p class="checklist-file text-truncate max-w-200">
                                                                <a class="color-file-<s:property value="%{#checklistItem.checklistFileModelList2[0].file_status}" />" href="viewTempFileAttachment?uploadID=<s:property value="%{#checklistItem.checklistFileModelList2[0].file_id}" />" target="_blank">
                                                                    <s:property value="%{#checklistItem.checklistFileModelList2[0].file_name}" />
                                                                </a>
                                                            </p>
                                                            <div class="d-block">
                                                                <p class="mb-0">
                                                                    <small>
                                                                        <pre class="cd-notes"><s:property value="%{#checklistItem.ci_notes}" /></pre>
                                                                    </small>
                                                                </p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td class="col-md-2">
                                                    <div class="form-check">
                                                        <input class="hardcopyForm form-check-input" id="cl_result_c_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.u21ChecklistSetupModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="C" <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("C")}'>Checked</s:if> />
                                                        <label class="form-check-label" for="cl_result_c_<s:property value='%{#checklistItem.id}' />"><s:text name="utimaps.checklist.label.correct" /></label>
                                                    </div>
                                                    <div class="form-check">
                                                        <input class="hardcopyForm form-check-input" id="cl_result_i_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.u21ChecklistSetupModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="E" <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("E")}'>Checked</s:if> />
                                                        <label class="form-check-label" for="cl_result_i_<s:property value='%{#checklistItem.id}' />"><s:text name="utimaps.checklist.label.incorrect" /></label>
                                                    </div>
                                                    <div class="form-check">
                                                        <input class="hardcopyForm form-check-input" id="cl_result_n_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.u21ChecklistSetupModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="N" <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("N")}'>Checked</s:if> />
                                                        <label class="form-check-label" for="cl_result_n_<s:property value='%{#checklistItem.id}' />"><s:text name="utimaps.checklist.label.notapplicable" /></label>
                                                    </div>
                                                </td>
                                                <td class="col-md-5">
                                                    <s:textarea theme="simple" name="model.u21ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].cl_remarks" value="%{#checklistItem.checklistResultList[0].cl_remarks}" cssClass="form-control" rows="5" maxlength="1000"/>
                                                </td>

                                                <td class="col-md-2">
                                                    <s:if test="#checklistItem.checklistResultList[0].checklistSupportFileList.size() > 0">
                                                        <s:iterator value = "#checklistItem.checklistResultList[0].checklistSupportFileList" status="supportFileStatus" var="supportFile">
                                                            <s:hidden id="drDocName" name="%{#supportFile.dr_doc_name}" value="%{#supportFile.dr_doc_name}"/>
                                                            <s:include value="/base/uppyIncludeSingleFile_utimaps.jsp">
                                                                <s:param name="uploadUrl_">uploadInternalAttachment</s:param>
                                                                <s:param name="uppyFieldName_"><s:property value="%{#checklistItem.ci_datatype}" /></s:param>
                                                                <s:param name="uppyHiddenName_">uppySample2FileId</s:param>
                                                                <s:param name="drFileCode_">CM</s:param>
                                                                <s:param name="hideArrow">Y</s:param>
                                                                <s:param name="uppyFileList" value="testList"/>
                                                                <s:param name="uploadParams">drAppCode_=uppySample2&uploadRecordId_=<s:property value="%{model.job_id}"/>&ciId=<s:property value="%{#checklistItem.ci_id}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                                                                <s:param name="uploadedFileName"><s:property value="%{#supportFile.original_file_name}"/></s:param>
                                                                <s:param name="uploadedExt"><s:property value="%{#supportFile.file_ext}"/></s:param>
                                                                <s:param name="uploadedDate"><s:property value="%{#supportFile.created_date_str}"/></s:param>
                                                                <s:param name="uploadedFileId"><s:property value="%{#supportFile.file_id}"/></s:param>
                                                                <s:param name="allowedFileTypes">'.pdf', '.zip'</s:param>
                                                                <s:param name="theRecord_id"><s:property value="%{#supportFile.file_id}"/></s:param>
                                                                <s:param name="ci_id"><s:property value="%{#checklistItem.ci_id}"/></s:param>
                                                                <s:param name="checklist_type">U21</s:param>
                                                            </s:include>
                                                        </s:iterator>
                                                    </s:if>
                                                    <s:else>
                                                        <s:include value="/base/uppyIncludeSingleFile_utimaps.jsp">
                                                            <s:param name="uploadUrl_">uploadInternalAttachment</s:param>
                                                            <s:param name="uppyFieldName_"><s:property value="%{#checklistItem.ci_datatype}" /></s:param>
                                                            <s:param name="uppyHiddenName_">uppySample2FileId</s:param>
                                                            <s:param name="drFileCode_">CM</s:param>
                                                            <s:param name="hideArrow">Y</s:param>
                                                            <s:param name="uppyFileList" value="testList"/>
                                                            <s:param name="uploadParams">drAppCode_=uppySample2&uploadRecordId_=<s:property value="%{model.job_id}"/>&ciId=<s:property value="%{#checklistItem.ci_id}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                                                            <s:param name="uploadedFileName"></s:param>
                                                            <s:param name="uploadedExt"></s:param>
                                                            <s:param name="uploadedDate"></s:param>
                                                            <s:param name="uploadedFileId"></s:param>
                                                            <s:param name="allowedFileTypes">'.pdf', '.zip'</s:param>
                                                            <s:param name="theRecord_id"></s:param>
                                                            <s:param name="ci_id"><s:property value="%{#checklistItem.ci_id}"/></s:param>
                                                            <s:param name="checklist_type">U21</s:param>
                                                        </s:include>
                                                    </s:else>
                                                </td>
                                            </tr>
                                        </s:iterator>
                                    </s:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </form>
            </s:else>
                
            </div>
        </div>
    </div>
</div>

<script nonce="r4DjhKbfO5ry">
    $(document).ready(function () {
        scrollToView("flush-collapseHardcopy");
        
        checklistDecisionButton("u21ChecklistModel","completeHardcopySubmission");
        
        checkChecklistResult("hardcopyForm","checkHardcopyStatus_A","checkHardcopyStatus_R","u21ChecklistModel","completeHardcopySubmission");
        
        saveShortcut("#saveEditHardcopyForm","processUpdate2JobSubmission");
        
        $("#startHardcopyAssessmentSubmission").on("click", function(e) {
            e.preventDefault();
            bootbox.confirm({
                closeButton: false,
                message: "<s:text name="utimaps.form.message.startAssessment" />", 
                buttons: {
                    confirm: {
                        label: 'CONFIRM'
                    },
                    cancel: {
                        label: 'CANCEL'
                    }
                },
                callback: function(result) {
                    if (result) {
                        var form = $('#startHardcopyAssessmentForm');
                        form.submit();
                    }
                }
            });
        });
        
        $(".date_picker").each(function () {
            $(this).datepicker({
                enableOnReadonly: true,
                autoclose: true,
                format: "dd MM yyyy"
            });
        });
        
        //change complete button based on user decision
        $('input[name="model.u21ChecklistModel.check_status"]').on("change", function(e) {
            e.preventDefault();
            checklistDecisionButton("u21ChecklistModel","completeHardcopySubmission");
            
            var decision = $('input[name="model.u21ChecklistModel.check_status"]:checked').val();
            
//            if(decision === 'R') {
//                $('input[name="model.hardcopy_received_date_str"]').val("");
//            }
        });
        
        $('#saveEditHardcopySubmission').click(function (e) {
            e.preventDefault();
            var displayMessage = "<s:text name="utimaps.form.message.save" />";
        
            confirmationBox(displayMessage, "#saveEditHardcopyForm", "processUpdate2JobSubmission");
        });
        
        $('#completeHardcopySubmission').click(function (e) {
            e.preventDefault();
            var canProceed = true;
            var decision = $('input[name="model.u21ChecklistModel.check_status"]:checked').val();
            var rejectMessage = "<s:text name="utimaps.form.message.reject" />";
            var acceptMessage = "<s:text name="utimaps.form.message.accept" />";
            
            var checkRemarks = checkChecklistResultRemarks("hardcopyForm","checkStatus_A","checkStatus_R","u21ChecklistModel","processComplete2JobSubmission");
            
            if(checkRemarks > 0) {
                if(decision === undefined || decision === null) {
                    canProceed = false;
                    bootbox.alert({
                        closeButton: false,
                        message: "<s:text name="hardcopyCheckSave" />"
                    });
                }

                var displayMessage = decision === "A" ? acceptMessage : rejectMessage;

                if(canProceed) {

                    if(checkAllTicked("hardcopyForm")) {
                        confirmationBox(displayMessage, "#saveEditHardcopyForm", "processComplete2JobSubmission");
                    }
                }
            }
        });
    });   
</script>    
        