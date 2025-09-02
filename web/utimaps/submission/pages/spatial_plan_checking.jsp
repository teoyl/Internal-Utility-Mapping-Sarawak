<%-- 
    Document   : spatial_plan_checking
    Created on : Mar 25, 2025, 4:50:32 PM
    Author     : yonglai
--%>
<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingThree">
        <button class="accordion-button <s:if test='!activeAccordion.equals("4")'>collapsed</s:if> fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseThree" aria-expanded="false" aria-controls="flush-collapseThree">
            <i class="fas fa-tasks me-2"></i><s:text name="utimaps.form.submission.spatialCheck" />
            
            <s:if test="model.wf_status_2 > '333'">
                <span class="position-absolute end-50px text-success">[<s:text name = "utimaps.form.label.completed" />]</span>
            </s:if>
            <s:else>
                <span class="position-absolute end-50px text-primary">[<s:text name = "utimaps.form.label.inProgress" />]</span>
            </s:else>
        </button>
    </h2>
    <div id="flush-collapseThree" class="accordion-collapse collapse <s:if test='activeAccordion.equals("4")'>show</s:if>" aria-labelledby="flush-headingThree" data-bs-parent="#accordionFlushApplication">
        <div class="accordion-body">
            <s:if test='rightToUpdate && model.wf_status_2 >= "310" && model.wf_status_2 <= "333"' >
                <s:if test='(checkHasChecklist(model.job_id, "U60"))'>
                    <div id="pastChecklistDiv"><jsp:include page="/utimaps/job_listing/modalPastChecklist.jsp"></jsp:include></div>
                </s:if>
            </s:if>
            <form action="" method="post" id="ChecklistU60Form">
                <s:hidden name="case_id" id="case_id" value="%{model.case_id}"/>
                <s:hidden name="job_id" id="job_id" value="%{model.job_id}"/>
                <s:hidden name="actionName" id="actionName" value="%{actionName}"/>
                <s:hidden name="taskId_" value="%{taskId_}"/>
                <s:hidden name="wfActivityCode" value="%{wfActivityCode}"/> 
                <s:hidden name="model.wf_status" id="model.wf_status" value="%{model.wf_status}"/> 
                <s:hidden name="app_submit_by" id="app_submit_by" value="%{model.app_submit_by}"/> 
                <s:hidden name="case_ref" id="case_ref" value="%{model.case_ref}"/> 
                <s:hidden name="processType" id="processType" value="310"/> 
                <s:hidden name="model.wf_status_2" id="model.wf_status_2" value="%{model.wf_status_2}"/> 
                <s:hidden name="checklist_id" id="checklist_id" value="%{model.u60ChecklistSetupModel.checklist_id}"/> 
                <s:hidden name="model.applicationModel.case_id" id="model.applicationModel.case_id" value="%{model.applicationModel.case_id}"/> 
                <s:hidden name="model.u60ChecklistModel.check_id" id="model.u60ChecklistModel.check_id" value="%{model.u60ChecklistModel.check_id}"/> 
                <s:hidden name="model.u60ChecklistModel.case_id" id="model.u60ChecklistModel.case_id" value="%{model.job_id}"/> 
                <s:hidden name="model.u60ChecklistModel.check_type" id="model.u60ChecklistModel.check_type" value="U60"/> 
                <s:hidden name="control_sv_flag" id="control_sv_flag" value="%{model.control_sv_flag}"/>
                <s:hidden name="comp_completed" id="comp_completed" value="%{model.comp_completed}"/>
                
                <div class="row">
                    <div class="col-md-12 text-end mb-1 mt-3">
                        <s:if test='rightToUpdate && model.wf_status_2 >= "310" && model.wf_status_2 <= "333"' >
                            <s:if test='(checkHasChecklist(model.job_id, "U60"))'>
                                <a href="#pastChecklistModal" data-toggle="modal" data-target="#pastChecklistModal" data-content-type="U60" class="btn btn-primary viewChecklistBtn mb-1" type="button"><s:text name="utimaps.form.button.viewPastChecklist"/></a>
                            </s:if>
                            <a href="pdfViewerSubmission?pType=U60&pJobId=<s:property value="model.job_id" />" target="_blank" class="btn btn-info mb-1" type="button"><s:text name="utimaps.form.button.printChecklist" /></a>
                        </s:if>
                    </div>
                    <div class="col-md-12 text-end mb-3 mt-3">
                        <s:if test='rightToUpdate && model.wf_status_2 >= "310" && model.wf_status_2 <= "333"' >
                            <button class="btn btn-success" id="completeSpatialCheckSubmission" type="submit"><s:text name = "utimaps.form.button.complete" /></button>
                          
                            <s:if test="model.wf_status_2.equals('320') || model.wf_status_2.equals('330')">
                                <button class="btn btn-primary" id="routeBackSubmission" type="submit"><i class="fas fa-undo-alt"></i> <s:text name = "utimaps.form.button.routeBack" /></button>
                            </s:if>

                            <button class="btn btn-primary" id="saveEditSpatialCheckSubmission" type="submit"><i class="fas fa-save"></i> <s:text name = "utimaps.form.button.save" /></button>
                        </s:if>
                    </div>

                    <div class="col-md-12 mb-3">                   
                        <div class="col-md-12 mb-3">
                            <div class="card mb-3">
                                <div class="card-header bg-primary-subtle">
                                    <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.recDocCheckSTA" /></h5>
                                </div>
                                <div class="card-body">
                                    <div class="btn-group" role="group" aria-label="Completion status">
                                        <input type="radio" class="btn-check" name="model.u60ChecklistModel.rec_status" id="decisionRec_A" value="A"
                                            <s:if test='model.u60ChecklistModel.rec_status.equals("A")'>checked</s:if>
                                            <s:if test='!model.wf_status_2.equals("310")'>disabled</s:if> autocomplete="off">
                                        <label class="btn btn-outline-success" for="decisionRec_A"><s:text name="utimaps.form.label.pass" /></label>

                                        <input type="radio" class="btn-check" name="model.u60ChecklistModel.rec_status" id="decisionRec_R" value="R"
                                            <s:if test='model.u60ChecklistModel.rec_status.equals("R")'>checked</s:if>
                                            <s:if test='!model.wf_status_2.equals("310")'>disabled</s:if> autocomplete="off">
                                        <label class="btn btn-outline-danger" for="decisionRec_R"><s:text name="utimaps.form.label.fail" /></label>
                                    </div>
                                </div>
                                <hr class="my-0" />
                                <div class="card-header bg-primary-subtle">
                                    <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.commentSTA" /></h5>
                                </div>
                                <div class="card-body">
                                    <div class="mb-3 ">
                                        <s:textarea theme="simple" name="model.u60ChecklistModel.check_comment_oic" value="%{model.u60ChecklistModel.check_comment_oic}" cssClass="form-control" rows="10" readonly="%{model.wf_status_2.equals('310') ? 'false' : 'true'}" />
                                    </div>
                                    <div class="row">
                                        <div class="form-floating mb-3 col-md-6">
                                            <input class="form-control" id="floatingCheckedBy_u60" name="check_by_oic" type="text" value="<s:property value='model.u60ChecklistModel.checkUser.us_user_name'/>" readonly/>
                                            <label class="floating-label" for="floatingCheckedBy_u60"><s:text name="utimaps.form.label.commentPO" /></label>
                                        </div>
                                        <div class="form-floating mb-3 col-md-6">
                                            <input class="form-control" id="floatingDateChecking_u60" name="check_date_oic_str" type="text" value="<s:property value='model.u60ChecklistModel.check_date_oic_str'/>" readonly/>
                                            <label class="floating-label" for="floatingDateChecking_u60"><s:text name="utimaps.form.label.dateChecking" /></label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                                
                        <div class="col-md-12 mb-3">
                            <div class="card mb-3">
                                <div class="card-header bg-primary-subtle">
                                    <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.verfiyCheckSD" /></h5>
                                </div>
                                <div class="card-body">
                                    <div class="btn-group" role="group" aria-label="Completion status">
                                        <input type="radio" class="btn-check" name="model.u60ChecklistModel.verify_status" id="decisionVerify_A" value="A"
                                            <s:if test='model.u60ChecklistModel.verify_status.equals("A")'>checked</s:if>
                                            <s:if test='!model.wf_status_2.equals("320")'>disabled</s:if> autocomplete="off">
                                        <label class="btn btn-outline-success" for="decisionVerify_A"><s:text name="utimaps.form.label.pass" /></label>

                                        <input type="radio" class="btn-check" name="model.u60ChecklistModel.verify_status" id="decisionVerify_R" value="R"
                                            <s:if test='model.u60ChecklistModel.verify_status.equals("R")'>checked</s:if>
                                            <s:if test='!model.wf_status_2.equals("320")'>disabled</s:if> autocomplete="off">
                                        <label class="btn btn-outline-danger" for="decisionVerify_R"><s:text name="utimaps.form.label.fail" /></label>
                                    </div>
                                </div>
                                <hr class="my-0" />
                                <div class="card-header bg-primary-subtle">
                                    <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.commentSD" /></h5>
                                </div>
                                <div class="card-body">
                                    <div class="mb-3 ">
                                        <s:textarea theme="simple" name="model.u60ChecklistModel.comment_verify_oic" value="%{model.u60ChecklistModel.comment_verify_oic}" cssClass="form-control" rows="10" readonly="%{model.wf_status_2.equals('320') ? 'false' : 'true'}" />
                                    </div>
                                    <div class="row">
                                        <div class="form-floating mb-3 col-md-6">
                                            <input class="form-control" id="floatingValidatedBy_u60" name="check_by_ss" type="text" value="<s:property value='model.u60ChecklistModel.verifyUser.us_user_name'/>" readonly/>
                                            <label class="floating-label" for="floatingValidatedBy_u60"><s:text name="utimaps.form.label.commentSD" /></label>
                                        </div>
                                        <div class="form-floating mb-3 col-md-6">
                                            <input class="form-control" id="floatingDateValidation_u60" name="check_date_ss_str" type="text" value="<s:property value='model.u60ChecklistModel.verify_date_oic_str'/>" readonly/>
                                            <label class="floating-label" for="floatingDateValidation_u60"><s:text name="utimaps.form.label.dateChecking" /></label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                                
                        <div class="col-md-12 mb-3">
                            <div class="card mb-3">
                                <div class="card-header bg-primary-subtle">
                                    <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.decisionSpatial" /></h5>
                                </div>
                                <div class="card-body">
                                    <div class="btn-group" role="group" aria-label="Decision spatial">
                                        <input type="radio" class="btn-check" name="model.u60ChecklistModel.check_status" id="decisionSpatial_A" value="A"
                                            <s:if test='model.u60ChecklistModel.check_status.equals("A")'>checked</s:if>
                                            <s:if test='!model.wf_status_2.equals("330")'>disabled</s:if> autocomplete="off">
                                        <label class="btn btn-outline-success" for="decisionSpatial_A"><s:text name="utimaps.form.label.pass" /></label>

                                        <input type="radio" class="btn-check" name="model.u60ChecklistModel.check_status" id="decisionSpatial_R" value="R"
                                            <s:if test='model.u60ChecklistModel.check_status.equals("R")'>checked</s:if>
                                            <s:if test='!model.wf_status_2.equals("330")'>disabled</s:if> autocomplete="off">
                                        <label class="btn btn-outline-danger" for="decisionSpatial_R"><s:text name="utimaps.form.label.fail" /></label>
                                    </div>
                                </div>
                                <hr class="my-0" />
                                <div class="card-header bg-primary-subtle">
                                    <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.commentSS" /></h5>
                                </div>
                                <div class="card-body">
                                    <div class="mb-3 ">
                                        <s:textarea theme="simple" name="model.u60ChecklistModel.comment_ss" value="%{model.u60ChecklistModel.comment_ss}" cssClass="form-control" rows="10" readonly="%{model.wf_status_2.equals('330') ? 'false' : 'true'}" />
                                    </div>
                                    <div class="row">
                                        <div class="form-floating mb-3 col-md-6">
                                            <input class="form-control" id="floatingCheckedBy_u60" name="check_by_ss" type="text" value="<s:property value='model.u60ChecklistModel.checkSSUser.us_user_name'/>" readonly/>
                                            <label class="floating-label" for="floatingCheckedBy_u60"><s:text name="utimaps.form.label.commentSS" /></label>
                                        </div>
                                        <div class="form-floating mb-3 col-md-6">
                                            <input class="form-control" id="floatingDateChecking_u60" name="check_date_ss_str" type="text" value="<s:property value='model.u60ChecklistModel.check_date_ss_str'/>" readonly/>
                                            <label class="floating-label" for="floatingDateChecking_u60"><s:text name="utimaps.form.label.dateChecking" /></label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <h6 class="pb-1 border-bottom border-bottom-lg-1 text-uppercase fw-bold fs-1" data-anchor="data-anchor"><s:text name="utimaps.checklist.label.docSubmitted" /></h6>
                        <jsp:include page="../checklist/checklist_u60.jsp"></jsp:include>
                    </div>
                    
                    <s:include value="../pages/utility_provider_form.jsp">
                        <s:param name="className_">U60</s:param>
                    </s:include >         

                </div>
            </form>
        </div>
    </div>
</div>
    
<script nonce="r4DjhKbfO5ry">
    $(document).ready(function () {
        initEditor("model.u60ChecklistModel.check_comment_oic", 4000);
        initEditor("model.u60ChecklistModel.comment_verify_oic", 4000);
        initEditor("model.u60ChecklistModel.comment_ss", 4000);

        <s:if test= 'model.wf_status_2 == "330"'>
            checklistDecisionButton("u60ChecklistModel","completeSpatialCheckSubmission");
            checkChecklistResult("u60checklistform","decisionSpatial_A","decisionSpatial_R","u60ChecklistModel","completeSpatialCheckSubmission");
        </s:if>
    });

    $('input[name="model.u60ChecklistModel.check_status"]').on("change", function(e) {
        e.preventDefault();
        checklistDecisionButton("u60ChecklistModel","completeSpatialCheckSubmission");
    });
    
    $('#saveEditSpatialCheckSubmission').click(function (e) {
        e.preventDefault();
        var displayMessage = "Are you sure want to save?";
        
        confirmationBox(displayMessage, "#ChecklistU60Form", "processUpdate2JobSubmission");
    });
    
    $('#routeBackSubmission').click( function (e) {
        e.preventDefault();
        var displayMessage = "<s:text name="utimaps.job.routeBack"/>";
        
        confirmationBox(displayMessage, "#ChecklistU60Form", "processRouteBackJobSubmission");
    });

    $('#completeSpatialCheckSubmission').click(function (e) {
        e.preventDefault();
        var rejectMessage = "You are about to COMPLETE this task.";
        var acceptMessage = "You are about to COMPLETE this task.";
        
        var poComment = CKEDITOR.instances.model_u60ChecklistModel_check_comment_oic.getData();
        var sdComment = CKEDITOR.instances.model_u60ChecklistModel_comment_verify_oic.getData();
        var ssComment = CKEDITOR.instances.model_u60ChecklistModel_comment_ss.getData();
        var isCommentFilled = true;

        var decision = $('input[name="model.u60ChecklistModel.check_status"]:checked').val();
        var currentStatus = $("#model_wf_status_2").val();
        var displayMessage = decision === "A" ? acceptMessage : rejectMessage;
        
        var checkRemarks = checkChecklistResultRemarks("u60checklistform","checkStatus_A","checkStatus_R","u60ChecklistModel","completeSpatialCheckSubmission");
        
        if(checkRemarks > 0) {
            if(decision === "A") {
                var displayMessage = decision === "A" ? acceptMessage : rejectMessage;
            } else {
                var commentLength = 0;
                var displayMessage = "";
                if(currentStatus === "310") {
                    commentLength = poComment.length;
                    displayMessage = "<s:text name="utimaps.form.message.fillCommentPO"/>" ;
                } else if(currentStatus === "320") {
                    displayMessage = "<s:text name="utimaps.form.message.fillCommentSD"/>" ;
                    commentLength = sdComment.length;
                } else if(currentStatus === "330") {
                    displayMessage = "<s:text name="utimaps.form.message.fillCommentSS"/>" ;
                    commentLength = ssComment.length;
                }
                
                if(commentLength <= 0) {
                    isCommentFilled = false;
                } else {
                    var displayMessage = decision === "A" ? acceptMessage : rejectMessage;
                }
            } 

            if(isCommentFilled) {
                if(checkAllTicked("u60checklistform")) {
                    confirmationBox(displayMessage, "#ChecklistU60Form", "processComplete2JobSubmission");
                }
            } else {
                alertBox(displayMessage);
            }
        }
    });

</script>