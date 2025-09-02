<%-- 
    Document   : detailed_plan_checking
    Created on : Jul 7, 2024, 11:34:20 PM
    Author     : yonglai
--%>
<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingDetailedPlan">
        <button class="accordion-button <s:if test='!activeAccordion.equals("3")'>collapsed</s:if> fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseDetailedPlan" aria-expanded="false" aria-controls="flush-collapseDetailedPlan">
            <i class="fas fa-tasks me-2"></i><s:text name="utimaps.form.submission.utilityPlan" />
            
            <s:if test="model.wf_status_2 > '282'">
                <span class="position-absolute end-50px text-success">[<s:text name = "utimaps.form.label.completed" />]</span>
            </s:if>
            <s:else>
                <span class="position-absolute end-50px text-primary">[<s:text name = "utimaps.form.label.inProgress" />]</span>
            </s:else>
        </button>
    </h2>
    <div id="flush-collapseDetailedPlan" class="accordion-collapse collapse <s:if test='activeAccordion.equals("3")'>show</s:if>" aria-labelledby="flush-headingDetailedPlan" data-bs-parent="#accordionFlushApplication">
        <div class="accordion-body">
            <s:if test='rightToUpdate && model.wf_status_2.equals("280")' >
                <s:if test='(checkHasChecklist(model.job_id, "U50"))'>
                    <div id="pastChecklistDiv"><jsp:include page="/utimaps/job_listing/modalPastChecklist.jsp"></jsp:include></div>
                </s:if>
            </s:if>
            <form action="" method="post" id="ChecklistU50Form">
                <s:hidden name="case_id" id="case_id" value="%{model.case_id}"/>
                <s:hidden name="job_id" id="job_id" value="%{model.job_id}"/>
                <s:hidden name="actionName" id="actionName" value="%{actionName}"/>
                <s:hidden name="taskId_" value="%{taskId_}"/>
                <s:hidden name="wfActivityCode" value="%{wfActivityCode}"/> 
                <s:hidden name="model.wf_status" id="model.wf_status" value="%{model.wf_status}"/> 
                <s:hidden name="app_submit_by" id="app_submit_by" value="%{model.app_submit_by}"/> 
                <s:hidden name="case_ref" id="case_ref" value="%{model.case_ref}"/> 
                <s:hidden name="processType" id="processType" value="280"/> 
                <s:hidden name="model.wf_status_2" id="model.wf_status_2" value="%{model.wf_status_2}"/> 
                <s:hidden name="checklist_id" id="checklist_id" value="%{model.u50ChecklistSetupModel.checklist_id}"/> 
                <s:hidden name="model.u50ChecklistModel.check_id" id="model.u50ChecklistModel.check_id" value="%{model.u50ChecklistModel.check_id}"/> 
                <s:hidden name="model.u50ChecklistModel.case_id" id="model.u50ChecklistModel.case_id" value="%{model.job_id}"/> 
                <s:hidden name="model.u50ChecklistModel.check_type" id="model.u50ChecklistModel.check_type" value="U50"/> 
                <s:hidden name="control_sv_flag" id="control_sv_flag" value="%{model.control_sv_flag}"/>
                <s:hidden name="comp_completed" id="comp_completed" value="%{model.comp_completed}"/>
                
                <div class="row">
                    <div class="col-md-12 text-end mb-1 mt-3">
                        <s:if test='rightToUpdate && model.wf_status_2.equals("280")' >
                            <s:if test='(checkHasChecklist(model.job_id, "U50"))'>
                                <a href="#pastChecklistModal" data-toggle="modal" data-target="#pastChecklistModal" data-content-type="U50" class="btn btn-primary viewChecklistBtn mb-1" type="button"><s:text name="utimaps.form.button.viewPastChecklist"/></a>
                            </s:if>
                            <a href="pdfViewerSubmission?pType=U50&pJobId=<s:property value="model.job_id" />" target="_blank" class="btn btn-info mb-1" type="button"><s:text name="utimaps.form.button.printChecklist" /></a>
                        </s:if>
                    </div>
                    <div class="col-md-12 text-end mb-3 mt-3">
                        <s:if test='rightToUpdate && model.wf_status_2.equals("280")' >
                            <button class="btn btn-success" id="completeDetailedPlanSubmission" type="submit"><s:text name = "utimaps.form.button.accept" /></button>
                          
                            <button class="btn btn-primary" id="saveEditDetailedPlanSubmission" type="submit"><i class="fas fa-save"></i> <s:text name = "utimaps.form.button.save" /></button>
                        </s:if>
                    </div>

                    <div class="col-md-12 mb-3">
                        <div class="card mb-3">
                            <div class="card-header bg-primary-subtle">
                                <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.decisionSubQuality" /></h5>
                            </div>
                            <div class="card-body">
                                <div class="btn-group" role="group" aria-label="Completion status">
                                    <input type="radio" class="btn-check" name="model.u50ChecklistModel.check_status" id="decisionQuaSub_A" value="A"
                                        <s:if test='model.u50ChecklistModel.check_status.equals("A")'>checked</s:if> autocomplete="off">
                                    <label class="btn btn-outline-success" for="decisionQuaSub_A"><s:text name="utimaps.form.label.pass" /></label>

                                    <input type="radio" class="btn-check" name="model.u50ChecklistModel.check_status" id="decisionQuaSub_R" value="R"
                                        <s:if test='model.u50ChecklistModel.check_status.equals("R")'>checked</s:if> autocomplete="off">
                                    <label class="btn btn-outline-danger" for="decisionQuaSub_R"><s:text name="utimaps.form.label.fail" /></label>
                                </div>
                            </div>
                            <hr class="my-0" />
                            <div class="card-header bg-primary-subtle">
                                <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.commentPO" /></h5>
                            </div>
                            <div class="card-body">
                                <div class="mb-3 ">
                                    <s:textarea theme="simple" name="model.u50ChecklistModel.check_comment_oic" value="%{model.u50ChecklistModel.check_comment_oic}" cssClass="form-control" rows="10" />
                                </div>
                                <div class="row">
                                    <div class="form-floating mb-3 col-md-6">
                                        <input class="form-control" id="floatingCheckedBy_u50" name="check_by_oic" type="text" value="<s:property value='model.u50ChecklistModel.checkUser.us_user_name'/>" readonly/>
                                        <label class="floating-label" for="floatingCheckedBy_u50"><s:text name="utimaps.form.label.commentPO" /></label>
                                    </div>
                                    <div class="form-floating mb-3 col-md-6">
                                        <input class="form-control" id="floatingDateChecking_u50" name="check_date_oic_str" type="text" value="<s:property value='model.u50ChecklistModel.check_date_oic_str'/>" readonly/>
                                        <label class="floating-label" for="floatingDateChecking_u50"><s:text name="utimaps.form.label.dateChecking" /></label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-12">
                        <h6 class="pb-1 border-bottom border-bottom-lg-1 text-uppercase fw-bold fs-1" data-anchor="data-anchor"><s:text name="utimaps.checklist.label.docSubmitted" /></h6>
                        <jsp:include page="../checklist/checklist_u50.jsp"></jsp:include>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
    
<script nonce="r4DjhKbfO5ry">
    $(document).ready(function () {
        scrollToView("flush-collapseDetailedPlan");
        
        initEditor("model.u50ChecklistModel.check_comment_oic", 4000);

        checklistDecisionButton("u50ChecklistModel","completeDetailedPlanSubmission");
        
        checkChecklistResult("u50checklistform","decisionQuaSub_A","decisionQuaSub_R","u50ChecklistModel","completeDetailedPlanSubmission");
    });

    $('input[name="model.u50ChecklistModel.check_status"]').on("change", function(e) {
        e.preventDefault();
        checklistDecisionButton("u50ChecklistModel","completeDetailedPlanSubmission");
    });
    
    $('#saveEditDetailedPlanSubmission').click(function (e) {
        e.preventDefault();
        var displayMessage = "Are you sure want to save?";
        
        confirmationBox(displayMessage, "#ChecklistU50Form", "processUpdate2JobSubmission");
    });

    $('#completeDetailedPlanSubmission').click(function (e) {
        e.preventDefault();
        var rejectMessage = "You are about to COMPLETE this task.";
        var acceptMessage = "You are about to COMPLETE this task.";
        
        var poComment = CKEDITOR.instances.model_u50ChecklistModel_check_comment_oic.getData();
        var isCommentFilled = true;

        var decision = $('input[name="model.u50ChecklistModel.check_status"]:checked').val();
        var displayMessage = decision === "A" ? acceptMessage : rejectMessage;
        
        var checkRemarks = checkChecklistResultRemarks("u50checklistform","checkStatus_A","checkStatus_R","u50ChecklistModel","completeDetailedPlanSubmission");
        
        if(checkRemarks > 0) {
            if(decision === "A") {
                var displayMessage = decision === "A" ? acceptMessage : rejectMessage;
            } else {
                if(poComment.length <= 0) {
                    var displayMessage = "<s:text name="utimaps.form.message.fillCommentPO"/>" ;
                    isCommentFilled = false;
                } else {
                    var displayMessage = decision === "A" ? acceptMessage : rejectMessage;
                }
            } 

            if(isCommentFilled) {
                if(checkAllTicked("u50checklistform")) {
                    confirmationBox(displayMessage, "#ChecklistU50Form", "processComplete2JobSubmission");
                }
            } else {
                alertBox(displayMessage);
            }
        }
    });

</script>