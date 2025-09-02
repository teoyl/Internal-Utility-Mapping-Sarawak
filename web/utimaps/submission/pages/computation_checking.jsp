<%--
    Document   : computation_checking
    Created on : May 06, 2024, 11:03:24 AM
    Author     : arine
--%>

<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<s:if test='rightToUpdate && model.wf_status_2.equals("200") || model.wf_status_2.equals("203")' >
    <s:if test='(checkHasChecklist(model.job_id, "U30"))'>
        <div id="pastChecklistDiv"><jsp:include page="/utimaps/job_listing/modalPastChecklist.jsp"></jsp:include></div>     
    </s:if>
</s:if>
                
<form action="" method="post" id="ChecklistU30Form">
    <s:hidden name="case_id" id="case_id" value="%{model.case_id}"/>
    <s:hidden name="job_id" id="job_id" value="%{model.job_id}"/>
    <s:hidden name="actionName" id="actionName" value="%{actionName}"/>
    <s:hidden name="taskId_" value="%{taskId_}"/>
    <s:hidden name="wfActivityCode" value="%{wfActivityCode}"/> 
    <s:hidden name="model.wf_status" id="model.wf_status" value="%{model.wf_status}"/> 
    <s:hidden name="app_submit_by" id="app_submit_by" value="%{model.app_submit_by}"/> 
    <s:hidden name="case_ref" id="case_ref" value="%{model.case_ref}"/> 
    <s:hidden name="processType" id="processType" value="200"/> 
    <s:hidden name="model.wf_status_2" value="%{model.wf_status_2}"/> 
    <s:hidden name="checklist_id" id="checklist_id" value="%{model.u30ChecklistSetupModel.checklist_id}"/> 
    <s:hidden name="model.u30ChecklistModel.check_id" id="model.u30ChecklistModel.check_id" value="%{model.u30ChecklistModel.check_id}"/> 
    <s:hidden name="model.u30ChecklistModel.case_id" id="model.u30ChecklistModel.case_id" value="%{model.job_id}"/> 
    <s:hidden name="model.u30ChecklistModel.check_type" id="model.u30ChecklistModel.check_type" value="U30"/> 
    <s:hidden name="control_sv_flag" id="control_sv_flag" value="%{model.control_sv_flag}"/>
    <s:hidden name="comp_completed" id="comp_completed" value="%{model.comp_completed}"/>

    <div class="row">
        <div class="col-md-12 text-end mb-1 mt-3">
            <s:if test='rightToUpdate && model.wf_status_2.equals("200") || model.wf_status_2.equals("203")' >
                <s:if test='(checkHasChecklist(model.job_id, "U30"))'>
                    <a href="#pastChecklistModal" data-toggle="modal" data-target="#pastChecklistModal" data-content-type="U30" class="btn btn-primary viewChecklistBtn mb-1" type="button"><s:text name="utimaps.form.button.viewPastChecklist"/></a>
                </s:if>
                <a href="pdfViewerSubmission?pType=U30&pJobId=<s:property value="model.job_id" />" target="_blank" class="btn btn-info mb-1" type="button"><s:text name="utimaps.form.button.printChecklist" /></a>
            </s:if>
        </div>
        <div class="col-md-12 text-end mb-3 mt-3">
            <s:if test='rightToUpdate && model.wf_status_2.equals("200") || model.wf_status_2.equals("203")' >
                <button class="btn btn-success" id="completeJobCompSubmission" type="submit"><s:text name = "utimaps.form.button.accept" /></button>

                <button class="btn btn-primary" id="saveEditJobCompSubmission" type="submit"><i class="fas fa-save"></i> <s:text name = "utimaps.form.button.save" /></button>
            </s:if>
        </div>

        <div class="col-md-12 mb-3">
            <div class="card mb-3">
                <div class="card-header bg-primary-subtle">
                    <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.recDocCheckTA" /></h5>
                </div>
                <div class="card-body">
                    <div class="btn-group" role="group" aria-label="Completion status">
                        <input type="radio" class="btn-check" name="model.u30ChecklistModel.rec_status" id="compcheck_A" value="A"
                            <s:if test='model.u30ChecklistModel.rec_status.equals("A")'>checked</s:if>
                            <s:if test='!model.wf_status_2.equals("200")'>disabled</s:if> autocomplete="off">
                        <label class="btn btn-outline-success" for="compcheck_A"><s:text name="utimaps.form.label.pass" /></label>

                        <input type="radio" class="btn-check" name="model.u30ChecklistModel.rec_status" id="compcheck_R" value="R"
                            <s:if test='model.u30ChecklistModel.rec_status.equals("R")'>checked</s:if>
                            <s:if test='!model.wf_status_2.equals("200")'>disabled</s:if> autocomplete="off">
                        <label class="btn btn-outline-danger" for="compcheck_R"><s:text name="utimaps.form.label.fail" /></label>
                    </div>
                </div>
                <hr class="my-0" />
                <div class="card-header bg-primary-subtle">
                    <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.commentTA" /> (<s:text name="utimaps.form.label.surveyComp" />)</h5>
                </div>
                <div class="card-body">
                    <div class="mb-3 ">
                        <s:textarea theme="simple" name="model.u30ChecklistModel.comment_ss" value="%{model.u30ChecklistModel.comment_ss}" cssClass="form-control" rows="10" readonly="%{model.wf_status_2.equals('200') ? 'false' : 'true'}" />
                    </div>
                    <div class="row">
                        <div class="form-floating mb-3 col-md-6">
                            <input class="form-control" id="floatingCheckedByTA_u30" name="check_by_ss" type="text" value="<s:property value='model.u30ChecklistModel.checkSSUser.us_user_name'/>" readonly/>
                            <label class="floating-label" for="floatingCheckedByTA_u30"><s:text name="utimaps.form.label.commentTA" /></label>
                        </div>
                        <div class="form-floating mb-3 col-md-6">
                            <input class="form-control" id="floatingDateCheckingTA_u30" name="check_date_ss_str" type="text" value="<s:property value='model.u30ChecklistModel.check_date_ss_str'/>" readonly/>
                            <label class="floating-label" for="floatingDateCheckingTA_u30"><s:text name="utimaps.form.label.dateChecking" /></label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
                
        <div class="col-md-12 mb-3">
            <div class="card mb-3">
                <div class="card-header bg-primary-subtle">
                    <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.decisionCompCheck" /></h5>
                </div>
                <div class="card-body">
                    <div class="btn-group" role="group" aria-label="Completion status">
                        <input type="radio" class="btn-check" name="model.u30ChecklistModel.check_status" id="compDecision_A" value="A"
                            <s:if test='model.u30ChecklistModel.check_status.equals("A")'>checked</s:if>
                            <s:if test='!model.wf_status_2.equals("203")'>disabled</s:if> autocomplete="off">
                        <label class="btn btn-outline-success" for="compDecision_A"><s:text name="utimaps.form.label.pass" /></label>

                        <input type="radio" class="btn-check" name="model.u30ChecklistModel.check_status" id="compDecision_R" value="R"
                            <s:if test='model.u30ChecklistModel.check_status.equals("R")'>checked</s:if>
                            <s:if test='!model.wf_status_2.equals("203")'>disabled</s:if> autocomplete="off">
                        <label class="btn btn-outline-danger" for="compDecision_R"><s:text name="utimaps.form.label.fail" /></label>
                    </div>
                </div>
                <hr class="my-0" />
                <div class="card-header bg-primary-subtle">
                    <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.commentSTA" /> (<s:text name="utimaps.form.label.surveyComp" />)</h5>
                </div>
                <div class="card-body">
                    <div class="mb-3 ">
                        <s:textarea theme="simple" name="model.u30ChecklistModel.check_comment_oic" value="%{model.u30ChecklistModel.check_comment_oic}" cssClass="form-control" rows="10" readonly="%{model.wf_status_2.equals('203') ? 'false' : 'true'}" />
                    </div>
                    <div class="row">
                        <div class="form-floating mb-3 col-md-6">
                            <input class="form-control" id="floatingCheckedByAS_u30" name="check_by_oic" type="text" value="<s:property value='model.u30ChecklistModel.checkUser.us_user_name'/>" readonly/>
                            <label class="floating-label" for="floatingCheckedByAS_u30"><s:text name="utimaps.form.label.commentSTA" /></label>
                        </div>
                        <div class="form-floating mb-3 col-md-6">
                            <input class="form-control" id="floatingDateCheckingAS_u30" name="check_date_oic_str" type="text" value="<s:property value='model.u30ChecklistModel.check_date_oic_str'/>" readonly/>
                            <label class="floating-label" for="floatingDateCheckingAS_u30"><s:text name="utimaps.form.label.dateChecking" /></label>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-12">
            <h6 class="pb-1 border-bottom border-bottom-lg-1 text-uppercase fw-bold fs-1" data-anchor="data-anchor"><s:text name="utimaps.checklist.label.docSubmitted" /></h6>
            <jsp:include page="../checklist/checklist_u30.jsp"></jsp:include>
            </div>
        </div>
    </form>

    <script nonce="r4DjhKbfO5ry">
        $(document).ready(function () {
            initEditor("model.u30ChecklistModel.check_comment_oic", 4000);
            initEditor("model.u30ChecklistModel.comment_ss", 4000);

            checklistDecisionButton("u30ChecklistModel", "completeJobCompSubmission");

            checkChecklistResult("u30checklistform", "compcheck_A", "compcheck_R", "u30ChecklistModel", "completeJobCompSubmission");
        });

        //change complete button based on user decision
        $('input[name="model.u30ChecklistModel.check_status"]').on("change", function (e) {
            e.preventDefault();
            checklistDecisionButton("u30ChecklistModel", "completeJobCompSubmission");

        });

        $('#saveEditJobCompSubmission').click(function (e) {
            e.preventDefault();
            var displayMessage = "<s:text name="utimaps.form.message.save"/>";

            confirmationBox(displayMessage, "#ChecklistU30Form", "processUpdate2JobSubmission");
        });

        $('#completeJobCompSubmission').click(function (e) {
            e.preventDefault();

            var rejectMessage = "<s:text name="utimaps.form.message.completeTask"/>";
            var acceptMessage = "<s:text name="utimaps.form.message.completeTask"/>";
            
            var wfStatus = $("#model_wf_status_2").val();

            var taComment = CKEDITOR.instances.model_u30ChecklistModel_comment_ss.getData();
            var asComment = CKEDITOR.instances.model_u30ChecklistModel_check_comment_oic.getData();
            var isCommentFilled = true;
            var decision = $('input[name="model.u30ChecklistModel.check_status"]:checked').val();
            var commentLength = (wfStatus === "203") ? asComment.length : taComment.length;
            var commentUser = (wfStatus === "203") ? "AS" : "TA";

            var checkRemarks = checkChecklistResultRemarks("u30checklistform", "checkStatus_A", "checkStatus_R", "u30ChecklistModel", "completeJobCompSubmission");

            if (checkRemarks > 0) {
                if (decision === "A") {
                    var displayMessage = decision === "A" ? acceptMessage : rejectMessage;
                } else {
                    if (commentLength <= 0) {
                        var displayMessage = "<s:text name="utimaps.form.message.fillCommentPO"/>" + " " + commentUser;
                        isCommentFilled = false;
                    } else {
                        var displayMessage = decision === "A" ? acceptMessage : rejectMessage;
                    }
                }

                if (isCommentFilled) {
                    if (checkAllTicked("u30checklistform")) {
                        confirmationBox(displayMessage, "#ChecklistU30Form", "processComplete2JobSubmission");
                    }
                } else {
                    alertBox(displayMessage);
                }
            }
        });

</script>

