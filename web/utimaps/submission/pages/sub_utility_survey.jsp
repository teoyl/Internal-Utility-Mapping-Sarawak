<%-- 
    Document   : sub_utility_survey
    Created on : May 22, 2024, 10:00:15 AM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingOne">
        <button class="accordion-button <s:if test='!activeAccordion.equals("1")'>collapsed</s:if> fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseOne" aria-expanded="false" aria-controls="flush-collapseOne">
            <i class="fas fa-tasks me-2"></i><s:text name = "utimaps.form.submission.utilityChecklist" />

            <s:if test="model.wf_status > '142'">
                <span class="position-absolute end-50px text-success">[<s:text name = "utimaps.form.label.completed" />]</span>
            </s:if>
            <s:else>
                <span class="position-absolute end-50px text-primary">[<s:text name = "utimaps.form.label.inProgress" />]</span>
            </s:else>
        </button>
    </h2>

    <div id="flush-collapseOne" class="accordion-collapse collapse <s:if test='activeAccordion.equals("1")'>show</s:if>" aria-labelledby="flush-headingOne" data-bs-parent="#accordionUtitliyChecklist">
        <div class="accordion-body">
            <s:if test='(checkHasChecklist(model.job_id, "U20"))'>
                <div id="pastChecklistDiv"><jsp:include page="/utimaps/job_listing/modalPastChecklist.jsp"></jsp:include></div>
            </s:if>
            <form action="" method="post" id="ChecklistU20Form">
                <s:hidden name="case_id" id="case_id" value="%{model.case_id}"/>
                <s:hidden name="job_id" id="job_id" value="%{model.job_id}"/>
                <s:hidden name="actionName" id="actionName" value="%{actionName}"/>
                <s:hidden name="taskId_" value="%{taskId_}"/>
                <s:hidden name="wfActivityCode" value="%{wfActivityCode}"/> 
                <s:hidden name="model.wf_status" id="model.wf_status" value="%{model.wf_status}"/> 
                <s:hidden name="app_submit_by" id="app_submit_by" value="%{model.app_submit_by}"/> 
                <s:hidden name="case_ref" id="case_ref" value="%{model.case_ref}"/> 
                <s:hidden name="processType" id="processType" value="130"/> 
                <s:hidden name="checklist_id" id="checklist_id" value="%{model.u20ChecklistSetupModel.checklist_id}"/> 
                <s:hidden name="model.u20ChecklistModel.check_id" id="model.u20ChecklistModel.check_id" value="%{model.u20ChecklistModel.check_id}"/> 
                <s:hidden name="model.u20ChecklistModel.case_id" id="model.u20ChecklistModel.case_id" value="%{model.job_id}"/> 
                <s:hidden name="model.u20ChecklistModel.check_type" id="model.u20ChecklistModel.check_type" value="U20"/> 
                <s:hidden name="control_sv_flag" id="control_sv_flag" value="%{model.control_sv_flag}"/>
                <s:hidden name="comp_completed" id="comp_completed" value="%{model.comp_completed}"/>
                <s:hidden name="model.applicationModel.case_id" id="model.applicationModel.case_id"/> 
                
                <div class="row">
                    <div class="col-md-12 text-end mb-1">
                        <s:if test='rightToUpdate && model.wf_status.equals("140") || model.wf_status.equals("143")'>
                            <s:if test='(checkHasChecklist(model.job_id, "U20"))'>
                                <a href="#pastChecklistModal" data-toggle="modal" data-target="#pastChecklistModal" data-content-type="U20" class="btn btn-primary viewChecklistBtn mb-1" type="button"><s:text name="utimaps.form.button.viewPastChecklist"/></a>
                            </s:if>
                            <a href="pdfViewerSubmission?pType=U20&pJobId=<s:property value="model.job_id" />" target="_blank" class="btn btn-primary mb-1" type="button"><s:text name="utimaps.form.button.printChecklist" /></a>
                        </s:if>
                    </div>
                    <div class="col-md-12 text-end mb-3">
                        <s:if test='rightToUpdate && model.wf_status.equals("140") || model.wf_status.equals("143")'>
                            <button class="btn btn-success" id="completeJobSubmission" type="submit"><s:text name = "utimaps.form.button.accept" /></button>
                            
                            <s:if test="model.wf_status.equals('143')">
                                <button class="btn btn-primary" id="routeBackSubmission" type="submit"><i class="fas fa-undo-alt"></i> <s:text name = "utimaps.form.button.routeBack" /></button>
                            </s:if>
                            
                            <button class="btn btn-primary" id="saveEditJobSubmission" type="submit"><i class="fas fa-save"></i> <s:text name = "utimaps.form.button.save" /></button>
                        </s:if>
                    </div>
                    
                    <div class="col-md-12 mb-3">
                        <div class="card mb-3">
                            <div class="card-header bg-primary-subtle">
                                <h5 class="mb-0 fw-bold"><s:text name = "utimaps.form.label.recDocCheck" /></h5>
                            </div>
                            <div class="card-body">
                                <div class="btn-group" role="group" aria-label="Recommendation status">
                                    <input type="radio" class="btn-check" name="model.u20ChecklistModel.rec_status" id="recStatus_A" value="A" 
                                        <s:if test='model.u20ChecklistModel.rec_status.equals("A")'>checked</s:if> 
                                        <s:if test='!model.wf_status.equals("140")'>disabled</s:if> autocomplete="off">
                                    <label class="btn btn-outline-success" for="recStatus_A"><s:text name="utimaps.form.label.accept" /></label>

                                    <input type="radio" class="btn-check" name="model.u20ChecklistModel.rec_status" id="recStatus_R" value="R" 
                                        <s:if test='model.u20ChecklistModel.rec_status.equals("R")'>checked</s:if> 
                                        <s:if test='!model.wf_status.equals("140")'>disabled</s:if> autocomplete="off">
                                    <label class="btn btn-outline-danger" for="recStatus_R"><s:text name="utimaps.form.label.reject" /></label>
                                </div>
                            </div>
                            <hr class="my-0" />
                            <div class="card-header bg-primary-subtle">
                                <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.commentPO" /></h5>
                            </div>
                            <div class="card-body">
                                <div class="mb-3 ">
                                    <s:textarea theme="simple" name="model.u20ChecklistModel.check_comment_oic" value="%{model.u20ChecklistModel.check_comment_oic}" cssClass="form-control" rows="10" readonly="%{model.wf_status.equals('140') ? 'false' : 'true'}" />
                                </div>
                                <div class="row">
                                    <div class="form-floating mb-3 col-md-6">
                                        <input class="form-control" id="floatingCheckedBy" name="check_by_oic" type="text" value="<s:property value='model.u20ChecklistModel.user.us_user_name'/>" readonly/>
                                        <label class="floating-label" for="floatingCheckedBy"><s:text name = "utimaps.form.label.commentPO" /></label>
                                    </div>
                                    <div class="form-floating mb-3 col-md-6">
                                        <input class="form-control" id="floatingDateChecking" name="check_date_oic_str" type="text" value="<s:property value='model.u20ChecklistModel.check_date_oic_str'/>" readonly/>
                                        <label class="floating-label" for="floatingDateChecking"><s:text name = "utimaps.form.label.dateChecking" /></label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                                    
                    <div class="col-md-12 mb-3">
                        <div class="card mb-3">
                            <div class="card-header bg-primary-subtle">
                                <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.decisionDocCheck" /></h5>
                            </div>
                            <div class="card-body">
                                <div class="btn-group" role="group" aria-label="Recommendation status">
                                    <input type="radio" class="btn-check" name="model.u20ChecklistModel.check_status" id="checkStatus_A" value="A" 
                                        <s:if test='model.u20ChecklistModel.check_status.equals("A")'>checked</s:if> 
                                        <s:if test='!model.wf_status.equals("143")'>disabled</s:if>  autocomplete="off">
                                    <label class="btn btn-outline-success" for="checkStatus_A"><s:text name="utimaps.form.label.accept" /></label>

                                    <input type="radio" class="btn-check" name="model.u20ChecklistModel.check_status" id="checkStatus_R" value="R" 
                                        <s:if test='model.u20ChecklistModel.check_status.equals("R")'>checked</s:if> 
                                        <s:if test='!model.wf_status.equals("143")'>disabled</s:if> autocomplete="off">
                                    <label class="btn btn-outline-danger" for="checkStatus_R"><s:text name="utimaps.form.label.reject" /></label>
                                </div>
                            </div>
                            <div class="card-header bg-primary-subtle">
                                <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.commentSS" /></h5>
                            </div>
                            <div class="card-body">
                                <div class="mb-3 ">
                                    <s:textarea theme="simple" name="model.u20ChecklistModel.comment_ss" value="%{model.u20ChecklistModel.comment_ss}" cssClass="form-control" rows="10" readonly="%{model.wf_status.equals('143') ? 'false' : 'true'}" />
                                </div>
                                <div class="row">
                                    <div class="form-floating mb-3 col-md-6">
                                        <input class="form-control" id="floatingVerifiedBy" name="verify_by_oic" type="text" value="<s:property value='model.u20ChecklistModel.verifyUser.us_user_name'/>" readonly/>
                                        <label class="floating-label" for="floatingVerifiedBy"><s:text name = "utimaps.form.label.commentSS" /></label>
                                    </div>
                                    <div class="form-floating mb-3 col-md-6">
                                        <input class="form-control" id="floatingDateVerification" name="verify_date_oic_str" type="text" value="<s:property value='model.u20ChecklistModel.verify_date_oic_str'/>" readonly/>
                                        <label class="floating-label" for="floatingDateVerification"><s:text name = "utimaps.form.label.dateChecking" /></label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-12" id="">
                        <jsp:include page="../checklist/checklist_u20.jsp"></jsp:include>
                    </div>
                    
                    <s:include value="../pages/utility_provider_form.jsp">
                        <s:param name="className_">U10</s:param>
                    </s:include >   
                </div>
            </form>
        </div>
    </div>
</div>
                
<script nonce="r4DjhKbfO5ry">
    $(document).ready(function () {
        initEditor("model.u20ChecklistModel.check_comment_oic", 4000);
        
        initEditor("model.u20ChecklistModel.comment_ss", 4000);
       
        checklistDecisionButton("u20ChecklistModel","completeJobSubmission");
       
        checkChecklistResult("u20checklistform","checkStatus_A","checkStatus_R","u20ChecklistModel","completeJobSubmission");
    });
    
    //change complete button based on user decision
    $('input[name="model.u20ChecklistModel.check_status"]').on("change", function(e) {
        e.preventDefault();
        checklistDecisionButton("u20ChecklistModel","completeJobSubmission");

    });

    $('#saveEditJobSubmission').click(function (e) {
        e.preventDefault();
        var displayMessage = "Are you sure want to save?";
        
        confirmationBox(displayMessage, "#ChecklistU20Form", "processUpdateJobSubmission");
    });
    
    $('#routeBackSubmission').click( function (e) {
        e.preventDefault();
        var displayMessage = "<s:text name="utimaps.job.routeBack"/>";
        
        confirmationBox(displayMessage, "#ChecklistU20Form", "processRouteBackJobSubmission");
    });

    $('#completeJobSubmission').click(function (e) {
        e.preventDefault();
        
        var poComment = CKEDITOR.instances.model_u20ChecklistModel_check_comment_oic.getData();
        var ssComment = CKEDITOR.instances.model_u20ChecklistModel_comment_ss.getData();
        var isCommentFilled = true;
        var decision = $('input[name="model.u20ChecklistModel.check_status"]:checked').val();
        
        var checkRemarks = checkChecklistResultRemarks("u20checklistform","checkStatus_A","checkStatus_R","u20ChecklistModel","completeJobSubmission");
        
        if(checkRemarks > 0) {
            <s:if test='model.wf_status.equals("140")'>
                if(decision === "R") {
                    if(poComment.length <= 0) {
                        var displayMessage = "<s:text name="utimaps.form.message.fillCommentPO"/>";
                        isCommentFilled = false;
                    } else {
                        var displayMessage = "<s:text name="utimaps.form.message.completeTask"/>";
                    }
                } else {
                        var displayMessage = "<s:text name="utimaps.form.message.completeTask"/>";
                }

            </s:if>
            <s:else>
                if(decision === "R") {
                    if(ssComment.length <= 0) {
                        var displayMessage = "<s:text name="utimaps.form.message.fillCommentSS"/>";
                        isCommentFilled = false;
                    } else {
                        var rejectMessage = "<s:text name="utimaps.form.message.reject"/>";
                        var acceptMessage = "<s:text name="utimaps.form.message.accept"/>";

                        var displayMessage = decision === "A" ? acceptMessage : rejectMessage;
                    }
                } else {
                    var rejectMessage = "<s:text name="utimaps.form.message.reject"/>";
                    var acceptMessage = "<s:text name="utimaps.form.message.accept"/>";

                    var displayMessage = decision === "A" ? acceptMessage : rejectMessage;
                }

            </s:else>

            if(isCommentFilled) {
                if(checkAllTicked("u20checklistform")) {
                    confirmationBox(displayMessage, "#ChecklistU20Form", "processCompleteJobSubmission");
                }
            } else {
                alertBox(displayMessage);
            }
        }
    });
</script>