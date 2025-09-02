<%-- 
    Document   : submission_checklist
    Created on : May 21, 2024, 4:26:59 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingOne">
        <button class="accordion-button <s:if test='!wf_step.equals("1") || !wf_step.equals("2")'>collapsed</s:if> fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseOne" aria-expanded="false" aria-controls="flush-collapseOne">
            <i class="fas fa-tasks me-2"></i><s:text name="utimaps.form.application.subChecklist" />

            <s:if test="model.wf_status > '103'">
                <s:if test="model.getUPS10List().size() > 0">
                    <span class="ms-2 text-primary">[<s:text name="utimaps.form.submission.ups10" /> <s:text name="utimaps.form.application.ready" />]</span>
                </s:if>
                <s:elseif test="model.applicationModel.getUPS10List().size() > 0">
                    <span class="ms-2 text-primary">[<s:text name="utimaps.form.submission.ups10" /> <s:text name="utimaps.form.application.ready" />]</span>
                </s:elseif>
                <span class="position-absolute end-50px text-success">[<s:text name = "utimaps.form.label.completed" />]</span>
            </s:if>
            <s:else>
                <span class="position-absolute end-50px text-primary">[<s:text name = "utimaps.form.label.inProgress" />]</span>
            </s:else>
        </button>
    </h2>

    <div id="flush-collapseOne" class="accordion-collapse collapse <s:if test='wf_step.equals("1") || wf_step.equals("2")'>show</s:if>" aria-labelledby="flush-headingOne" data-bs-parent="#accordionFlushApplication">
        <div class="accordion-body">
            <form action="" method="post" id="ApplicationUSJForm">
                <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
                <s:hidden name="case_id" id="case_id" value="%{model.case_id}"/>
                <s:hidden name="taskId_" value="%{taskId_}"/>
                <s:hidden name="wfActivityCode" value="%{wfActivityCode}"/> 
                <s:hidden name="model.wf_status" id="model.wf_status" value="%{model.wf_status}"/> 
                <s:hidden name="checklist_id" id="checklist_id" value="%{model.checklistSetupCaseModel.checklist_id}"/> 
                <s:hidden name="app_submit_by" id="app_submit_by" value="%{model.app_submit_by}"/> 
                <s:hidden name="case_ref" id="case_ref" value="%{model.case_ref}"/> 
                <s:hidden name="internal_case" id="internal_case" value="%{model.internal_case}"/> 
                <s:hidden name="model.checklistModel.check_id" id="model.checklistModel.check_id" value="%{model.checklistModel.check_id}"/> 
                <s:hidden name="model.checklistModel.case_id" id="model.checklistModel.case_id" value="%{model.case_id}"/> 
                <s:hidden name="model.checklistModel.check_type" id="model.checklistModel.case_id" value="APP01"/> 
                <s:hidden name="model.paymentModel.payment_id" id="model.paymentModel.payment_id" value="%{model.paymentModel.payment_id}"/> 

            <div class="row">
                <div class="col-md-12 text-end mb-3">
                    <s:if test="rightToUpdate">
                        <button class="btn btn-primary" id="completeApplicationUSJ" type="submit"><i class="fas fa-check"></i> <s:text name = "utimaps.form.button.complete" /></button>
                        <s:if test='(checkHasChecklist(model.case_id, "UPS10"))'>
                            <a href="#pastChecklistModal" data-toggle="modal" data-target="#pastChecklistModal" data-content-type="UPS10" class="btn btn-primary viewChecklistBtn" type="button"><s:text name="utimaps.form.button.viewPastChecklist"/></a>
                        </s:if>
                        <s:if test="model.wf_status.equals('103')">
                            <button class="btn btn-primary" id="routeBackUSJ" type="submit"><i class="fas fa-undo-alt"></i> <s:text name = "utimaps.form.button.routeBack" /></button>
                        </s:if>
                        <a href="pdfViewerUSJ?type=checklistUPS10&caseId=<s:property value="model.case_id" />" target="_blank" class="btn btn-info" type="button"><i class="far fa-file-alt"></i> <s:text name = "utimaps.form.button.printChecklist" /></a>
                        <button class="btn btn-primary" id="saveEditApplicationUSJ" type="submit"><i class="fas fa-save"></i> <s:text name = "utimaps.form.button.save" /></button>
                    </s:if>
                </div>

                <div class="col-md-12 mb-3">
                    <div class="card mb-3">
                        <div class="card-body">
                            <h6 class="mb-0"><label class="form-label"><s:text name = "utimaps.form.label.decision" /></label></h6>
                            <div class="form-check form-check-inline mb-0">
                                <input class="form-check-input" type="radio" name="model.checklistModel.check_status" id="checkStatus_A" value="A" <s:if test='model.checklistModel.check_status.equals("A")'>checked</s:if> <s:if test="!rightToUpdate">disabled</s:if> />
                                <label class="form-check-label" for="checkStatus_A"><s:text name = "utimaps.form.label.accept" /></label>
                            </div>
                            <div class="form-check form-check-inline mb-0">
                                <input class="form-check-input" type="radio" name="model.checklistModel.check_status" id="checkStatus_R" value="R" <s:if test='model.checklistModel.check_status.equals("R")'>checked</s:if> <s:if test="!rightToUpdate">disabled</s:if>  />
                                <label class="form-check-label" for="checkStatus_R"><s:text name = "utimaps.form.label.reject" /></label>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-12 mb-3">
                    <h6><label class="form-label"><s:text name = "utimaps.form.label.commentSubmission" /></label></h6>
                    <div class="mb-3 ">
                        <s:textarea theme="simple" name="model.checklistModel.check_comment_oic" value="%{model.checklistModel.check_comment_oic}" cssClass="form-control" rows="10" readonly="%{!rightToUpdate}" />
                    </div>
                    <div class="row">
                        <div class="form-floating mb-3 col-md-6">
                            <input class="form-control" id="floatingCheckedBy" name="check_by_oic" type="text" value="<s:property value='checker_name'/>" readonly/>
                            <label class="floating-label" for="floatingCheckedBy"><s:text name = "utimaps.form.label.checkedBy" /></label>
                        </div>
                        <div class="form-floating mb-3 col-md-6">
                            <input class="form-control" id="floatingDateChecking" name="check_date_oic_str" type="text" value="<s:property value='model.checklistModel.check_date_oic_str'/>" readonly/>
                            <label class="floating-label" for="floatingDateChecking"><s:text name = "utimaps.form.label.dateChecking" /></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-floating mb-3 col-md-6">
                            <input class="form-control" id="floatingVerifiedBy" name="verify_by_oic" type="text" value="<s:property value='verifier_name'/>" readonly/>
                            <label class="floating-label" for="floatingVerifiedBy"><s:text name = "utimaps.form.label.verifyBy" /></label>
                        </div>
                        <div class="form-floating mb-3 col-md-6">
                            <input class="form-control" id="floatingDateVerify" name="verify_date_oic_str" type="text" value="<s:property value='model.checklistModel.verify_date_oic_str'/>" readonly/>
                            <label class="floating-label" for="floatingDateVerify"><s:text name = "utimaps.form.label.dateVerify" /></label>
                        </div>
                    </div>
                </div>

                 <div class="col-md-12">
                     <h6 class="pb-1 border-bottom border-bottom-lg-1 text-uppercase fw-bold fs-1" data-anchor="data-anchor"><s:text name ="utimaps.checklist.label.docSubmitted" /></h6>
                     <div class="table-responsive scrollbar">
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
                                 <s:if test="model.checklistSetupCaseModel.checklistItemList.size() > 0">
                                 <s:iterator value="model.checklistSetupCaseModel.checklistItemList" status="listStatus" var="checklistItem">
                                    <tr class="<s:if test="#listStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                        <s:hidden name="model.checklistSetupCaseModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].cl_id" value="%{#checklistItem.checklistResultList[0].cl_id}" />
                                        <s:hidden name="model.checklistSetupCaseModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].case_id" value="%{model.case_id}" />
                                        <s:hidden name="model.checklistSetupCaseModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].check_id" value="%{model.checklistModel.check_id}" />
                                        <s:hidden name="model.checklistSetupCaseModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].ci_id" value="%{#checklistItem.ci_id}" />
                                        <td colspan="2" class="col-md-3">
                                            <div class="d-block">
                                                <s:hidden name="model.checklistSetupCaseModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].ci_desc" value="%{#checklistItem.ci_desc}" />
                                                <h6 class="mb-0 fw-bold"><s:property value="%{#checklistItem.ci_sequence}" />. <s:property value="%{#checklistItem.ci_desc}" /></h6>
                                                <p class="mb-0"><small><s:property value="%{#checklistItem.ci_notes}" /></small></p>
                                                <b><small><s:text name ="utimaps.checklist.label.lastCheckedDate" />: <s:property value = "%{#checklistItem.checklistResultList[0].updated_date_str}" /></small></b>
                                            </div>
                                            <div class="d-flex align-items-center mt-1">
                                                <div class="col-md-3">
                                                    <div class="ms-2 circle-div text-uppercase">
                                                        <s:if test="#checklistItem.checklistFileModelList[0].file_ext != ''"><s:property value="%{#checklistItem.checklistFileModelList[0].file_ext}" /></s:if><s:else>-</s:else>
                                                    </div>
                                                </div>
                                                <div class="col-md-9">
                                                    <s:if test='#checklistItem.ci_datatype.equals("SUP")'>
                                                        <s:iterator value = "#checklistItem.checklistFileModelList" status="fileStatus" var="checklistFile">
                                                            <s:hidden name="model.checklistSetupCaseModel.checklistItemList[%{#listStatus.index}].checklistFileModelList[0].file_id" value="%{#checklistItem.checklistFileModelList[0].file_id}" />
                                                            <p class="checklist-file ">
                                                                <a class="color-file-<s:property value="%{#checklistFile.file_status}" />" href="viewTempFileAttachment?uploadID=<s:property value="%{#checklistFile.file_id}" />" target="_blank">
                                                                    <s:property value="%{#checklistFile.file_name}" />
                                                                </a>
                                                            </p>
                                                            <span class="checklist-file-date"><s:property value="%{#checklistFile.created_date_str}" /></span>
                                                            <span class="checklist-file-size mb-3"><s:property value="%{#checklistFile.file_size}" /></span>
                                                            <span class="checklist-file-size mb-3 fw-bold color-file-<s:property value="%{#checklistFile.file_status}" />">
                                                                <s:if test ='%{#checklistFile.file_status.equals("Y")}'><s:text name="utimaps.fileStatus.verified" /></s:if>
                                                                <s:elseif test ='%{#checklistFile.file_status.equals("N")}'><s:text name="utimaps.fileStatus.rejected" /></s:elseif>
                                                                <s:elseif test ='%{#checklistFile.file_status.equals("P")}'><s:text name="utimaps.fileStatus.new" /></s:elseif>
                                                            </span>
                                                        </s:iterator>
                                                    </s:if>
                                                    <s:else>
                                                        <s:hidden name="model.checklistSetupCaseModel.checklistItemList[%{#listStatus.index}].checklistFileModelList[0].file_id" value="%{#checklistItem.checklistFileModelList[0].file_id}" />
                                                        <p class="checklist-file ">
                                                            <a class="color-file-<s:property value="%{#checklistItem.checklistFileModelList[0].file_status}" />" href="viewTempFileAttachment?uploadID=<s:property value="%{#checklistItem.checklistFileModelList[0].file_id}" />" target="_blank">
                                                                <s:property value="%{#checklistItem.checklistFileModelList[0].file_name}" />
                                                            </a>
                                                        </p> 
                                                        <span class="checklist-file-date"><s:property value="%{#checklistItem.checklistFileModelList[0].created_date_str}" /></span>
                                                        <span class="checklist-file-size mb-3"><s:property value="%{#checklistItem.checklistFileModelList[0].file_size}" /></span>
                                                        <span class="checklist-file-size mb-3 fw-bold color-file-<s:property value="%{#checklistItem.checklistFileModelList[0].file_status}" />">
                                                            <s:if test ='%{#checklistItem.checklistFileModelList[0].file_status.equals("Y")}'><s:text name="utimaps.fileStatus.verified" /></s:if>
                                                            <s:elseif test ='%{#checklistItem.checklistFileModelList[0].file_status.equals("N")}'><s:text name="utimaps.fileStatus.rejected" /></s:elseif>
                                                            <s:elseif test ='%{#checklistItem.checklistFileModelList[0].file_status.equals("P")}'><s:text name="utimaps.fileStatus.new" /></s:elseif>
                                                        </span>
                                                    </s:else>
                                                </div>
                                            </div>
                                        </td>
                                        <td class="col-md-2">
                                            <div class="form-check">
                                                    <input class="ups10ChecklistForm form-check-input" id="cl_result_c_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.checklistSetupCaseModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="C" <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("C")}'>Checked</s:if> <s:if test="!rightToUpdate">disabled</s:if> />
                                                <label class="form-check-label" for="cl_result_c_<s:property value='%{#checklistItem.id}' />"><s:text name="utimaps.checklist.label.correct" /></label>
                                            </div>
                                            <div class="form-check">
                                                <input class="ups10ChecklistForm form-check-input" id="cl_result_i_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.checklistSetupCaseModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="E" <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("E")}'>Checked</s:if> <s:if test="!rightToUpdate">disabled</s:if> />
                                                <label class="form-check-label" for="cl_result_i_<s:property value='%{#checklistItem.id}' />"><s:text name="utimaps.checklist.label.incorrect" /></label>
                                            </div>
                                            <div class="form-check">
                                                <input class="ups10ChecklistForm form-check-input" id="cl_result_n_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.checklistSetupCaseModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="N" <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("N")}'>Checked</s:if> <s:if test="!rightToUpdate">disabled</s:if> />
                                                <label class="form-check-label" for="cl_result_n_<s:property value='%{#checklistItem.id}' />"><s:text name="utimaps.checklist.label.notapplicable" /></label>
                                            </div>
                                        </td>
                                        <td class="col-md-5">
                                            <s:textarea theme="simple" name="model.checklistSetupCaseModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].cl_remarks" value="%{#checklistItem.checklistResultList[0].cl_remarks}" cssClass="form-control" rows="6" maxlength="1000" readonly="%{!rightToUpdate}" />
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
                                                        <s:param name="uploadParams">drAppCode_=uppySample2&uploadRecordId_=<s:property value="%{model.case_id}"/>&ciId=<s:property value="%{#checklistItem.ci_id}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                                                        <s:param name="uploadedFileName"><s:property value="%{#supportFile.original_file_name}"/></s:param>
                                                        <s:param name="uploadedExt"><s:property value="%{#supportFile.file_ext}"/></s:param>
                                                        <s:param name="uploadedDate"><s:property value="%{#supportFile.created_date_str}"/></s:param>
                                                        <s:param name="uploadedFileId"><s:property value="%{#supportFile.file_id}"/></s:param>
                                                        <s:param name="allowedFileTypes">'.pdf', '.zip'</s:param>
                                                        <s:param name="theRecord_id"><s:property value="%{#supportFile.file_id}"/></s:param>
                                                        <s:param name="ci_id"><s:property value="%{#checklistItem.ci_id}"/></s:param>
                                                        <s:param name="checklist_type">UPS10</s:param>
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
                                                    <s:param name="uploadParams">drAppCode_=uppySample2&uploadRecordId_=<s:property value="%{model.case_id}"/>&ciId=<s:property value="%{#checklistItem.ci_id}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                                                    <s:param name="uploadedFileName"></s:param>
                                                    <s:param name="uploadedExt"></s:param>
                                                    <s:param name="uploadedDate"></s:param>
                                                    <s:param name="uploadedFileId"></s:param>
                                                    <s:param name="allowedFileTypes">'.pdf', '.zip'</s:param>
                                                    <s:param name="theRecord_id"></s:param>
                                                    <s:param name="checklist_type">UPS10</s:param>
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
            </div>
            </form>
        </div>
    </div>
</div>

<div id="pastChecklistDiv"><jsp:include page="/utimaps/job_listing/modalPastChecklist.jsp"></jsp:include></div>
                                 
<script nonce="r4DjhKbfO5ry">
    $( document ).ready(function() {
        initEditor("model.checklistModel.check_comment_oic", 4000);
        
        checklistDecisionButton("checklistModel","completeApplicationUSJ");
        
        checkChecklistResult("ups10ChecklistForm","checkStatus_A","checkStatus_R","checklistModel","completeApplicationUSJ");
    });

    $('input[name="model.checklistModel.check_status"]').on("change", function(e) {
        e.preventDefault();
        checklistDecisionButton("checklistModel","completeApplicationUSJ");
    });
    
    $('#saveEditApplicationUSJ').click( function (e) {
        e.preventDefault();
        var displayMessage = "<s:text name="utimaps.job.save"/>";
        
        confirmationBox(displayMessage, "#ApplicationUSJForm", "processUpdateApplicationUSJ");
    });
    
    $('#routeBackUSJ').click( function (e) {
        e.preventDefault();
        var displayMessage = "<s:text name="utimaps.job.routeBack"/>";
        
        confirmationBox(displayMessage, "#ApplicationUSJForm", "processRouteBackApplicationUSJ");
    });
    
    $('#completeApplicationUSJ').click( function (e) {
        e.preventDefault();
        var rejectMessage = "<s:text name="utimaps.job.complete"/>";
        var acceptMessage = "<s:text name="utimaps.job.complete"/>";
        var canProceed = true;
        var isCommentFilled = true;
        var decision = $('input[name="model.checklistModel.check_status"]:checked').val();
        var internalCase = $("#internal_case").val();
        console.log(internalCase)
        var poComment = CKEDITOR.instances.model_checklistModel_check_comment_oic.getData();
        var checkRemarks = checkChecklistResultRemarks("ups10ChecklistForm","checkStatus_A","checkStatus_R","checklistModel","completeApplicationUSJ");
        
        if(checkRemarks > 0) {
            <s:if test="model.wf_status.equals('103')">
                if(internalCase != "" && internalCase == "Y") {
                    rejectMessage = "<s:text name="utimaps.form.message.rejectAppInternal" />";
                    acceptMessage = "<s:text name="utimaps.form.message.acceptAppInternal" />";
                } else {
                    rejectMessage = "<s:text name="utimaps.form.message.rejectApplication" />";
                    acceptMessage = "<s:text name="utimaps.form.message.acceptApplication" />";
                }

                if(decision === undefined || decision === '') {
                    canProceed = false;
                    alertBox("<s:text name="utimaps.form.message.tickDecision" />");
                } else {
                    if(decision === "R") {
                        console.log(poComment)
                        if(poComment.length <= 0) {
                            canProceed = false;
                            var displayMessage = "<s:text name="utimaps.form.message.fillCommentPO"/>";
                            isCommentFilled = false;
                        } else {
                            var displayMessage = rejectMessage;
                        }
                    } else {
                        var displayMessage = acceptMessage;
                    }
                }
            </s:if>
            <s:else>
                rejectMessage = "<s:text name="utimaps.job.complete"/>";
                acceptMessage = "<s:text name="utimaps.job.complete"/>";
                
                if(decision === undefined || decision === '') {
                    canProceed = false;
                    alertBox("<s:text name="utimaps.form.message.tickDecision" />");
                } else {
                    if(decision === "R") {
                        if(poComment.length <= 0) {
                            canProceed = false;
                            var displayMessage = "<s:text name="utimaps.form.message.fillCommentPO"/>";
                            isCommentFilled = false;
                        } else {
                            var displayMessage = "<s:text name="utimaps.form.message.completeTask"/>";
                        }
                    } else {
                        var displayMessage = "<s:text name="utimaps.form.message.completeTask"/>";
                    }
                }
            </s:else>

            if(canProceed) {
//                var displayMessage = decision === "A" ? acceptMessage : rejectMessage;

                if(checkAllTicked("ups10ChecklistForm")) {
                    confirmationBox(displayMessage, "#ApplicationUSJForm", "processCompleteApplicationUSJ");
                }
            } else {
                alertBox(displayMessage);
            }
        }
    });
    
    $('.viewChecklistBtn').on('click', function (e) {
        e.stopPropagation();
        e.stopImmediatePropagation();
        e.preventDefault();
        var contentType = $(this).data('content-type');
        divSubmitForm("viewPastChecklistUSJ?caseId=<s:property value="%{model.case_id}"/>&checklist=" + contentType, "pastChecklistForm", "pastChecklistDiv", "viewChecklist");
    });
    function closeChecklistModal() {
        $('#pastChecklistModal').modal('hide');
    }
    function viewChecklist() {
        $('#pastChecklistModal').modal('show');
    }
</script>