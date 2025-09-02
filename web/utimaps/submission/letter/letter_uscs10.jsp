<%-- 
    Document   : letter_uscs10
    Created on : May 31, 2024, 9:31:00 AM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingUSCS10">
        <button class="accordion-button <s:if test='!activeAccordion.equals("2")'>collapsed</s:if> fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseUSCS10" aria-expanded="false" aria-controls="flush-collapseUSCS10">
            <i class="fas fa-tasks me-2"></i><s:text name = "utimaps.form.submission.uscs10" />
        </button>
    </h2>

    <div id="flush-collapseUSCS10" class="accordion-collapse collapse <s:if test='activeAccordion.equals("2")'>show</s:if>" aria-labelledby="flush-headingUSCS10" data-bs-parent="#accordionFlushApplication">
            <div class="accordion-body">
            <s:if test='fileID==NULL'>
                <IFRAME id="pdfviewer" src="pdfViewerSubmission?pJobId=${model.job_id}&pType=USCS10&cType=${checklistType}&output=embed"  WIDTH=100% HEIGHT=500  align="center" >
                </IFRAME>
            </s:if><s:else>
                <IFRAME id="pdfviewer" src="viewTempFileSubmission?fileID=${fileID}&output=embed"  WIDTH=100% HEIGHT=500  align="center" >
                </IFRAME>
            </s:else>
            <br><br>
            <form action="" method="post" id="USCS10Form" enctype="multipart/form-data">
                <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
                <s:hidden name="case_id" id="case_id" value="%{model.case_id}"/>
                <s:hidden name="jobId_" id="jobId_" value="%{model.job_id}"/>
                <s:hidden name="job_id" id="job_id" value="%{model.job_id}"/>
                <s:hidden name="actionName" id="actionName" value="%{actionName}"/>
                <s:hidden name="taskId_" value="%{taskId_}"/>
                <s:hidden name="wfActivityCode" value="%{wfActivityCode}"/> 
                <s:hidden name="processType" id="processType" value="150"/> 
                <s:hidden name="checklistType" id="checklistType" value="%{checklistType}"/> 
                <s:hidden name="model.wf_status" id="model.wf_status" value="%{model.wf_status}"/> 
                <s:hidden name="app_submit_by" id="app_submit_by" value="%{model.app_submit_by}"/> 
                <s:hidden name="case_ref" id="case_ref" value="%{model.case_ref}"/> 
                <s:hidden name="control_sv_flag" id="control_sv_flag" value="%{model.control_sv_flag}"/>
                <s:hidden name="comp_completed" id="comp_completed" value="%{model.comp_completed}"/>
                
                <s:if test='(!isSigned(model.job_id, "USCS10")) && model.wf_status.equals("150")'>
                    <div class="card-body p-1 ">
                        <div class="mb-3 row">
                            <label class="col-md-3 col-form-label" for="certFile"><s:text name="utimaps.form.label.retrieveDigicert" /> </label>
                            <div class="col-md-7 mb-2">
                                <s:file class="form-control" name="certFile" id="certFile" accept="application/x-pkcs12" />
                            </div>
                            <label class="col-md-3 col-form-label" for="certPassword"><s:text name="utimaps.form.label.password" /></label>
                            <div class="col-md-7 mb-2">
                                <s:password cssClass="form-control" name="certPassword" id="certPassword" value="" />
                            </div>
                        </div>
                    </div>
                </s:if>
                <div class="row">
                    <div class="col-md-12 text-end mb-3">
                        <s:if test='rightToUpdate && model.wf_status.equals("150")'>
                            <s:if test='(!isSigned(model.job_id, "USCS10")) && model.wf_status.equals("150")'>
                                <button class="btn btn-primary" id="signLetterSubmission" type="submit"><i class="fas fa-file-signature"></i></i> <s:text name = "utimaps.form.button.sign" /></button>
                            </s:if>
                            <s:if test='isSigned(model.job_id, "USCS10") && model.wf_status.equals("150")'>
                                <button class="btn btn-danger" id="revokeUSCSLetter" type="submit"><i class="fas fa-eraser"></i> <s:text name = "utimaps.form.button.recall" /></button>
                                <button class="btn btn-success" id="completeUSCS10JobSubmission" type="submit"><i class="fas fa-check"></i> <s:text name = "utimaps.form.button.complete" /></button>
                            </s:if>
                        </s:if>
                    </div>

                    <!--Sign Form Here-->
                </div>
            </form>
        </div>
    </div>
</div>

<script nonce="r4DjhKbfO5ry">
    $(document).ready(function () {
        scrollToView("flush-collapseUSCS10");
    });
    
    $('#signLetterSubmission').click(function (e) {
        e.preventDefault();
        document.getElementById("certFile").required = true;
        document.getElementById("certPassword").required = true;
        var status__ = validateForm_bshor('USCS10Form');
        if (status__) {
            var displayMessage = "<s:text name="utimaps.form.message.signLetter" />";

            bootbox.confirm({
                closeButton: false,
                message: displayMessage,
                buttons: {
                    confirm: {
                        label: 'PROCEED'
                    },
                    cancel: {
                        label: 'CANCEL'
                    }
                },
                callback: function (result) {
                    if (result) {
                        var form = $('#USCS10Form');
                        form.attr("action", "pdfSignSubmission");
                        form.submit();
                    }
                }
            });
        } else {
            showErrors(document.getElementById('USCS90Form'));
        }
    });

    $('#completeUSCS10JobSubmission').click(function (e) {
        e.preventDefault();
        var canProceed = true;
        var displayMessage = "<s:text name='utimaps.job.complete' />";

        if (canProceed) {
            bootbox.confirm({
                closeButton: false,
                message: displayMessage,
                buttons: {
                    confirm: {
                        label: 'PROCEED'
                    },
                    cancel: {
                        label: 'CANCEL'
                    }
                },
                callback: function (result) {
                    if (result) {
                        var form = $('#USCS10Form');
                        form.attr("action", "processCompleteJobSubmission");
                        form.submit();
                    }
                }
            });
        }
    });

    $('#revokeUSCSLetter').click(function (e) {
        e.preventDefault();
        var displayMessage = "<s:text name="utimaps.form.message.revokeLetter" />";

        bootbox.confirm({
            closeButton: false,
            message: displayMessage,
            buttons: {
                confirm: {
                    label: 'PROCEED'
                },
                cancel: {
                    label: 'CANCEL'
                }
            },
            callback: function (result) {
                if (result) {
                    var form = $('#USCS10Form');
                    form.attr("action", "pdfRevokeSubmission");
                    form.submit();
                }
            }
        });

    });

</script>