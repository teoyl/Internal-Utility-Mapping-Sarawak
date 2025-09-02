<%-- 
    Document   : letter_uscs30
    Created on : Jun 7, 2024, 8:23:44 AM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingUSCS60">
        <button class="accordion-button <s:if test='!activeAccordion.equals("3")'>collapsed</s:if> fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseUSCS60" aria-expanded="false" aria-controls="flush-collapseUSCS60">
            <i class="fas fa-tasks me-2"></i><s:text name = "utimaps.form.submission.uscs50" />
        </button>
    </h2>

    <div id="flush-collapseUSCS60" class="accordion-collapse collapse <s:if test='activeAccordion.equals("3")'>show</s:if>" aria-labelledby="flush-headingUSCS60" data-bs-parent="#accordionUtitliyChecklist">
            <div class="accordion-body">
            <s:if test='fileID==NULL'>
                <IFRAME id="pdfviewer" src="pdfViewerSubmission?pJobId=${model.job_id}&pType=USCS50&output=embed"  WIDTH=100% HEIGHT=500  align="center" >
                </IFRAME>
            </s:if><s:else>
                <IFRAME id="pdfviewer" src="viewTempFileSubmission?fileID=${fileID}&output=embed"  WIDTH=100% HEIGHT=500  align="center" >
                </IFRAME>
            </s:else>
            <br><br>
            <form action="" method="post" id="USCS50Form" enctype="multipart/form-data">
                <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
                <s:hidden name="case_id" id="case_id" value="%{model.case_id}"/>
                <s:hidden name="jobId_" id="jobId_" value="%{model.job_id}"/>
                <s:hidden name="job_id" id="job_id" value="%{model.job_id}"/>
                <s:hidden name="actionName" id="actionName" value="%{actionName}"/>
                <s:hidden name="taskId_" value="%{taskId_}"/>
                <s:hidden name="wfActivityCode" value="%{wfActivityCode}"/> 
                <s:hidden name="processType" id="processType" value="232"/> 
                <s:hidden name="model.wf_status" id="model.wf_status" value="%{model.wf_status}"/> 
                <s:hidden name="model.wf_status_2" id="model.wf_status_2" value="%{model.wf_status_2}"/> 
                <s:hidden name="app_submit_by" id="app_submit_by" value="%{model.app_submit_by}"/> 
                <s:hidden name="case_ref" id="case_ref" value="%{model.case_ref}"/>
                <s:hidden name="control_sv_flag" id="control_sv_flag" value="%{model.control_sv_flag}"/>
                <s:hidden name="comp_completed" id="comp_completed" value="%{model.comp_completed}"/>
                
                <s:if test='!isSigned(model.job_id, "USCS50") && model.wf_status_2.equals("232")'>
                    <div class="card-body p-1 ">
                        <div class="mb-3 row">
                            <label class="col-md-3 col-form-label" for="certFile"><s:text name="utimaps.form.label.retrieveDigicert" /> </label>
                            <div class="col-md-7 mb-2">
                                <s:file class="form-control" name="certFile" id="certFile" accept="application/x-pkcs12" />
                            </div>
                            <label class="col-md-3 col-form-label" for="certPassword"><s:text name="utimaps.form.label.retrieveDigicert" /></label>
                            <div class="col-md-7 mb-2">
                                <s:password cssClass="form-control" name="certPassword" id="certPassword" value="" />
                            </div>
                        </div>
                    </div>
                </s:if>
                <div class="row">
                    <div class="col-md-12 text-end mb-3">
                        <s:if test="rightToUpdate">
                            <s:if test='(!isSigned(model.job_id, "USCS50")) && model.wf_status_2.equals("232")'>
                                <button class="btn btn-primary" id="signLetterSubmission" type="submit"><i class="fas fa-file-signature"></i></i> <s:text name = "utimaps.form.button.sign" /></button>
                            </s:if>
                            <s:if test='isSigned(model.job_id, "USCS50") && model.wf_status_2.equals("232")'>
                                <button class="btn btn-danger" id="revokeUSCSLetter" type="submit"><i class="fas fa-eraser"></i> <s:text name = "utimaps.form.button.recall" /></button>
                                <button class="btn btn-success" id="completeUSCS50Submission" type="submit"><i class="fas fa-check"></i> <s:text name = "utimaps.form.button.complete" /></button>
                            </s:if>
                            <s:if test='model.wf_status_2.equals("230")'>
                                <button class="btn btn-success" id="completeUSCS50Submission" type="submit"><i class="fas fa-check"></i> <s:text name = "utimaps.form.button.complete" /></button>
                            </s:if>
                        </s:if>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script nonce="r4DjhKbfO5ry">
    $(document).ready(function () {
        scrollToView("flush-collapseUSCS50");
    });
    
    $('#signLetterSubmission').click(function (e) {
        e.preventDefault();
        document.getElementById("certFile").required = true;
        document.getElementById("certPassword").required = true;
        var status__ = validateForm_bshor('USCS50Form');
        if (status__) {
            var displayMessage = "<s:text name="utimaps.form.message.signLetter" />";

            confirmationBox(displayMessage, "#USCS50Form", "pdfSignSubmission");
        } else {
            showErrors(document.getElementById('USCS90Form'));
        }
    });

    $('#completeUSCS50Submission').click(function (e) {
        e.preventDefault();
        var displayMessage = "<s:text name='utimaps.job.complete' />";

        confirmationBox(displayMessage, "#USCS50Form", "processComplete2JobSubmission");
    });

    $('#revokeUSCSLetter').click(function (e) {
        e.preventDefault();
        var displayMessage = "<s:text name="utimaps.form.message.revokeLetter" />";

        confirmationBox(displayMessage, "#USCS50Form", "pdfRevokeSubmission");
    });

</script>