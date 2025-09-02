<%-- 
    Document   : control_plan_checking
    Created on : May 06, 2024, 11:03:24 AM
    Author     : arine
--%>

<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<s:if test='rightToUpdate && model.wf_status_2.equals("220")' >
    <s:if test='(checkHasChecklist(model.job_id, "U40"))'>
        <div id="pastChecklistDiv"><jsp:include page="/utimaps/job_listing/modalPastChecklist.jsp"></jsp:include></div>     
    </s:if>
</s:if>
                
<form action="" method="post" id="ChecklistU40Form">
    <s:hidden name="case_id" id="case_id" value="%{model.case_id}"/>
    <s:hidden name="job_id" id="job_id" value="%{model.job_id}"/>
    <s:hidden name="actionName" id="actionName" value="%{actionName}"/>
    <s:hidden name="taskId_" value="%{taskId_}"/>
    <s:hidden name="wfActivityCode" value="%{wfActivityCode}"/> 
    <s:hidden name="model.wf_status" id="model.wf_status" value="%{model.wf_status}"/> 
    <s:hidden name="app_submit_by" id="app_submit_by" value="%{model.app_submit_by}"/> 
    <s:hidden name="case_ref" id="case_ref" value="%{model.case_ref}"/> 
    <s:hidden name="processType" id="processType" value="220"/> 
    <s:hidden name="model.wf_status_2" id="model.wf_status_2" value="%{model.wf_status_2}"/> 
    <s:hidden name="checklist_id" id="checklist_id" value="%{model.u40ChecklistSetupModel.checklist_id}"/> 
    <s:hidden name="model.u40ChecklistModel.check_id" id="model.u40ChecklistModel.check_id" value="%{model.u40ChecklistModel.check_id}"/> 
    <s:hidden name="model.u40ChecklistModel.case_id" id="model.u40ChecklistModel.case_id" value="%{model.job_id}"/> 
    <s:hidden name="model.u40ChecklistModel.check_type" id="model.u40ChecklistModel.check_type" value="U40"/> 
    <s:hidden name="control_sv_flag" id="control_sv_flag" value="%{model.control_sv_flag}"/>
    <s:hidden name="comp_completed" id="comp_completed" value="%{model.comp_completed}"/>
                    
    <div class="row">
        <div class="col-md-12 text-end mb-1 mt-3">
            <s:if test='rightToUpdate && model.wf_status_2.equals("220")' >
                <s:if test='(checkHasChecklist(model.job_id, "U40"))'>
                    <a href="#pastChecklistModal" data-toggle="modal" data-target="#pastChecklistModal" data-content-type="U40" class="btn btn-primary viewChecklistBtn mb-1" type="button"><s:text name="utimaps.form.button.viewPastChecklist"/></a>
                </s:if>
                <a href="pdfViewerSubmission?pType=U40&pJobId=<s:property value="model.job_id" />" target="_blank" class="btn btn-info mb-1" type="button"><s:text name="utimaps.form.button.printChecklist" /></a>
            </s:if>
        </div>
        <div class="col-md-12 text-end mb-3 mt-3">
            <s:if test='rightToUpdate && model.wf_status_2.equals("220")' >
                <button class="btn btn-success" id="completePlanJobSubmission" type="submit"><s:text name = "utimaps.form.button.accept" /></button>

                <button class="btn btn-primary" id="saveEditPlanJobSubmission" type="submit"><i class="fas fa-save"></i> <s:text name = "utimaps.form.button.save" /></button>
            </s:if>
        </div>
        <div class="col-md-12 mb-3">                   
            <div class="col-md-12 mb-3">
    <div class="card mb-3">
        <div class="card-header bg-primary-subtle">
            <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.decisionSubQuality" /></h5>
        </div>
        <div class="card-body">
            <div class="btn-group" role="group" aria-label="Completion status">
                <input type="radio" class="btn-check" name="model.u40ChecklistModel.check_status" id="decisionSub_A" value="A"
                    <s:if test='model.u40ChecklistModel.check_status.equals("A")'>checked</s:if> autocomplete="off">
                <label class="btn btn-outline-success" for="decisionSub_A"><s:text name="utimaps.form.label.pass" /></label>

                <input type="radio" class="btn-check" name="model.u40ChecklistModel.check_status" id="decisionSub_R" value="R"
                    <s:if test='model.u40ChecklistModel.check_status.equals("R")'>checked</s:if> autocomplete="off">
                <label class="btn btn-outline-danger" for="decisionSub_R"><s:text name="utimaps.form.label.fail" /></label>
            </div>
        </div>
        <hr class="my-0" />
        <div class="card-header bg-primary-subtle">
            <h5 class="mb-0 fw-bold"><s:text name="utimaps.form.label.commentPO" /></h5>
        </div>
        <div class="card-body">
            <div class="mb-3 ">
                <s:textarea theme="simple" name="model.u40ChecklistModel.check_comment_oic" value="%{model.u40ChecklistModel.check_comment_oic}" cssClass="form-control" rows="10" />
            </div>
            <div class="row">
                <div class="form-floating mb-3 col-md-6">
                    <input class="form-control" id="floatingCheckedBy_u40" name="check_by_oic" type="text" value="<s:property value='model.u40ChecklistModel.checkUser.us_user_name'/>" readonly/>
                    <label class="floating-label" for="floatingCheckedBy_u40"><s:text name="utimaps.form.label.commentPO" /></label>
                </div>
                <div class="form-floating mb-3 col-md-6">
                    <input class="form-control" id="floatingDateChecking_u40" name="check_date_oic_str" type="text" value="<s:property value='model.u40ChecklistModel.check_date_oic_str'/>" readonly/>
                    <label class="floating-label" for="floatingDateChecking_u40"><s:text name="utimaps.form.label.dateChecking" /></label>
                </div>
            </div>
        </div>
    </div>
</div>
        </div>
        <div class="col-md-12">
            <h6 class="pb-1 border-bottom border-bottom-lg-1 text-uppercase fw-bold fs-1" data-anchor="data-anchor"><s:text name="utimaps.checklist.label.docSubmitted" /></h6>
            <jsp:include page="../checklist/checklist_u40.jsp"></jsp:include>
            </div>
        </div>
    </form>

    <script nonce="r4DjhKbfO5ry">
        $(document).ready(function () {
            initEditor("model.u40ChecklistModel.check_comment_oic", 4000);

            checklistDecisionButton("u40ChecklistModel", "completePlanJobSubmission");

            checkChecklistResult("u40checklistform", "decisionSub_A", "decisionSub_R", "u40ChecklistModel", "completePlanJobSubmission");
        });

        $('input[name="model.u40ChecklistModel.check_status"]').on("change", function (e) {
            e.preventDefault();
            checklistDecisionButton("u40ChecklistModel", "completePlanJobSubmission");
        });

        $('#saveEditPlanJobSubmission').click(function (e) {
            e.preventDefault();
            var displayMessage = "<s:text name="utimaps.form.message.save"/>";

            confirmationBox(displayMessage, "#ChecklistU40Form", "processUpdate2JobSubmission");
        });

        $('#completePlanJobSubmission').click(function (e) {
            e.preventDefault();

            var rejectMessage = "<s:text name="utimaps.form.message.completeTask"/>";
            var acceptMessage = "<s:text name="utimaps.form.message.completeTask"/>";

            var POComment = CKEDITOR.instances.model_u40ChecklistModel_check_comment_oic.getData();
            var isCommentFilled = true;
            var decision = $('input[name="model.u40ChecklistModel.check_status"]:checked').val();

            var checkRemarks = checkChecklistResultRemarks("u40checklistform", "checkStatus_A", "checkStatus_R", "u40ChecklistModel", "completePlanJobSubmission");

            if (checkRemarks > 0) {
                if (decision === "A") {
                    var displayMessage = decision === "A" ? acceptMessage : rejectMessage;
                } else {
                    if (POComment.length <= 0) {
                        var displayMessage = "<s:text name="utimaps.form.message.fillCommentPO"/>";
                        isCommentFilled = false;
                    } else {
                        var displayMessage = decision === "A" ? acceptMessage : rejectMessage;
                    }
                }

                if (isCommentFilled) {
                    if (checkAllTicked("u40checklistform")) {
                        confirmationBox(displayMessage, "#ChecklistU40Form", "processComplete2JobSubmission");
                    }
                } else {
                    alertBox(displayMessage);
                }
            }
        });

</script>