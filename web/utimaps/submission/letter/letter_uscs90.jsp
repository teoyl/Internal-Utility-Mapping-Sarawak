<%-- 
    Document   : letter_USCS90
    Created on : Jun 7, 2024, 8:23:44 AM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>
<div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingUSCS90">
        <button class="accordion-button <s:if test='!activeAccordion.equals("5")'>collapsed</s:if> fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseUSCS90" aria-expanded="false" aria-controls="flush-collapseUSCS90">
            <i class="fas fa-tasks me-2"></i><s:text name = "utimaps.form.submission.uscs90" />
        </button>
    </h2>

    <div id="flush-collapseUSCS90" class="accordion-collapse collapse <s:if test='activeAccordion.equals("5")'>show</s:if>" aria-labelledby="flush-headingUSCS90" data-bs-parent="#accordionUtitliyChecklist">
        <div class="accordion-body">
            <form action="" method="post" id="USCS90Form" enctype="multipart/form-data">
                <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
                <s:hidden name="case_id" id="case_id" value="%{model.case_id}"/>
                <s:hidden name="jobId_" id="jobId_" value="%{model.job_id}"/>
                <s:hidden name="job_id" id="job_id" value="%{model.job_id}"/>
                <s:hidden name="actionName" id="actionName" value="%{actionName}"/>
                <s:hidden name="taskId_" value="%{taskId_}"/>
                <s:hidden name="wfActivityCode" value="%{wfActivityCode}"/> 
                <s:hidden name="processType" id="processType" value="350"/> 
                <s:hidden name="model.wf_status" id="model.wf_status" value="%{model.wf_status}"/> 
                <s:hidden name="model.wf_status_2" id="model.wf_status_2" value="%{model.wf_status_2}"/> 
                <s:hidden name="app_submit_by" id="app_submit_by" value="%{model.app_submit_by}"/> 
                <s:hidden name="case_ref" id="case_ref" value="%{model.case_ref}"/>
                <s:hidden name="control_sv_flag" id="control_sv_flag" value="%{model.control_sv_flag}"/>
                <s:hidden name="comp_completed" id="comp_completed" value="%{model.comp_completed}"/>
                
                <div class="row">
                    <div class="form-floating mb-3 col-md-6">
                        <s:if test='(!isSigned(model.job_id, "SSDSP") || !isSigned(model.job_id, "USCS90") || !isSigned(model.job_id, "MINUTE")) && model.wf_status_2.equals("350")'>
                            <div class="card-body p-1 ">
                                <div class="mb-3 row">
                                    <label class="col-md-3 col-form-label" for="certFile"><s:text name="utimaps.form.label.retrieveDigicert" /> </label>
                                    <div class="col-md-7 mb-2">
                                        <s:file class="form-control" name="certFile" id="certFile" accept="application/x-pkcs12"/>
                                    </div>
                                    <label class="col-md-3 col-form-label" for="certPassword"><s:text name="utimaps.form.label.password" /></label>
                                    <div class="col-md-7 mb-2">
                                        <s:password cssClass="form-control" name="certPassword" id="certPassword" value="" />
                                    </div>
                                </div>
                            </div>
                        </s:if>
                    </div>
                    <s:if test="rightToUpdate">
                        <div class="form-floating mb-3 col-md-3 signDoc">
                            <s:if test='(!isSigned(model.job_id, "SSDSP") || !isSigned(model.job_id, "USCS90") || !isSigned(model.job_id, "MINUTE")) && model.wf_status_2.equals("350")'>
                                <button class="btn btn-primary" id="signLetterSubmission" type="submit"><i class="fas fa-file-signature"></i></i> <s:text name = "utimaps.form.button.sign" /></button><br>
                                <div class="clearfix">
                                    <input type="checkbox" name="checkbox_selected_" class="checkbox_child" id="sign_drawing" value="DSP" <s:if test='isSigned(model.job_id, "SSDSP")'>disabled</s:if><s:else>checked</s:else>>
                                    <label for="sign_drawing"><s:text name="utimaps.uscs90.drawing" /></label><br>
                                </div>
                                <div class="clearfix">
                                    <input type="checkbox" name="checkbox_selected_" class="checkbox_child" id="sign_letter" value="USCS90" <s:if test='isSigned(model.job_id, "USCS90")'>disabled</s:if><s:else>checked</s:else>>
                                    <label for="sign_letter"><s:text name="utimaps.uscs90.letter" /></label><br>  
                                </div>
                                <div class="clearfix">
                                    <input type="checkbox" name="checkbox_selected_" class="checkbox_child" id="sign_minute" value="MINUTE" <s:if test='isSigned(model.job_id, "MINUTE")'>disabled</s:if><s:else>checked</s:else>>
                                    <label for="sign_minute"><s:text name="utimaps.uscs90.minute" /></label><br>
                                </div>
                            </s:if>
                        </div>
                        <div class="form-floating mb-3 col-md-3 revokeDoc">
                            <s:if test='(isSigned(model.job_id, "SSDSP") || isSigned(model.job_id, "USCS90") || isSigned(model.job_id, "MINUTE")) && model.wf_status_2.equals("350")'>
                                <button class="btn btn-danger" id="revokeUSCSLetter" type="submit"><i class="fas fa-eraser"></i> <s:text name = "utimaps.form.button.recall" /></button><br>
                                <div class="clearfix">
                                    <input type="checkbox" name="revoke_checkbox_selected_" class="checkbox_child" id="revoke_drawing" value="SSDSP" <s:if test='isSigned(model.job_id, "SSDSP")'>checked</s:if><s:else>disabled</s:else>>
                                    <label for="revoke_drawing"><s:text name="utimaps.uscs90.drawing" /></label><br>
                                </div>
                                <div class="clearfix">
                                    <input type="checkbox" name="revoke_checkbox_selected_" class="checkbox_child" id="revoke_letter" value="USCS90" <s:if test='isSigned(model.job_id, "USCS90")'>checked</s:if><s:else>disabled</s:else>>
                                    <label for="revoke_letter"><s:text name="utimaps.uscs90.letter" /></label><br>
                                </div>
                                <div class="clearfix">
                                    <input type="checkbox" name="revoke_checkbox_selected_" class="checkbox_child" id="revoke_minute" value="MINUTE" <s:if test='isSigned(model.job_id, "MINUTE")'>checked</s:if><s:else>disabled</s:else>>
                                    <label for="revoke_minute"><s:text name="utimaps.uscs90.minute" /></label><br>
                                </div>
                            </s:if>
                        </div>
                    </s:if>
                    <ul class="nav nav-tabs" id="USCS90Tab" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link active" id="drawing-tab" data-bs-toggle="tab" data-bs-target="#drawing" type="button" role="tab" aria-controls="drawing" aria-selected="true">2.1 <s:text name="utimaps.uscs90.drawing" /></button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="letter-tab" data-bs-toggle="tab" data-bs-target="#letter" type="button" role="tab" aria-controls="letter" aria-selected="false">2.2 <s:text name="utimaps.uscs90.letter" /></button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="minute-tab" data-bs-toggle="tab" data-bs-target="#minute" type="button" role="tab" aria-controls="minute" aria-selected="false">2.3 <s:text name="utimaps.uscs90.minute" /></button>
                        </li>
                    </ul>
                    <div class="tab-content mt-3 mb-3" id="USCS90TabContent">
                        <div class="tab-pane fade show active" id="drawing" role="tabpanel" aria-labelledby="drawing-tab">
                            <div class="card mb-3">
                                <div class="row g-0">
                                    <div class="col-md-1">
                                        <ul class="list-group list-group-flush dsp-list rounded-start">
                                            <s:iterator value="dspList" status="dsplistStatus" var="dspItem">
                                                <li class="list-group-item text-middle p-3 <s:if test="#dsplistStatus.index==0">viewed</s:if>" id="dsp_<s:property value="%{#dspItem.file_id}"/>" onclick="viewDSP('${ dspItem.file_id}'); return false;">
                                                    <i class="fas fa-regular fa-file"></i><br>
                                                    ${ dsplistStatus.index+1}
                                                </li>
                                            </s:iterator>
                                        </ul>
                                    </div>
                                    <div class="col-md-11">
                                        <IFRAME class="rounded-end" id="pdfviewer_dsp" src="viewTempFileSubmission?fileID=${dspList[0].file_id}&output=embed"  WIDTH=100% HEIGHT=500  align="center" >
                                        </IFRAME>
                                    </div>
                                </div>
                            </div>

                        </div>
                        <div class="tab-pane fade" id="letter" role="tabpanel" aria-labelledby="letter-tab">
                            <s:if test='fileID==NULL'>
                                <IFRAME id="pdfviewer" src="pdfViewerSubmission?pJobId=${model.job_id}&pType=USCS90&output=embed"  WIDTH=100% HEIGHT=500  align="center" >
                                </IFRAME>
                            </s:if><s:else>
                                <IFRAME id="pdfviewer" src="viewTempFileSubmission?fileID=${fileID}&output=embed"  WIDTH=100% HEIGHT=500  align="center" >
                                </IFRAME>
                            </s:else>           
                        </div>
                        <div class="tab-pane fade" id="minute" role="tabpanel" aria-labelledby="minute-tab">
                            <IFRAME id="pdfviewer" src="pdfViewerSubmission?pJobId=${model.job_id}&pType=MINUTE&output=embed"  WIDTH=100% HEIGHT=500  align="center" >
                            </IFRAME>       
                        </div>
                    </div>
                    <br><br>
                    <div class="row">
                        <div class="col-md-12 text-end mb-3">
                            <s:if test='rightToUpdate && allDSPSigned && isSigned(model.job_id, "USCS90") && isSigned(model.job_id, "MINUTE")'>
                                <button class="btn btn-success" id="completeUSCS90Submission" type="submit"><i class="fas fa-check"></i> <s:text name = "utimaps.form.button.complete" /></button>
                            </s:if>
                        </div>

                        <!--Sign Form Here-->
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script nonce="r4DjhKbfO5ry">
    $(document).ready(function () {
        scrollToView("flush-collapseUSCS90");
    });
    
   function viewDSP(fileId) {
        $('.dsp-list li').removeClass("viewed");
        $('#dsp_'+fileId).addClass("viewed");
        $('#pdfviewer_dsp').attr('src', 'viewTempFileSubmission?fileID='+fileId+'&output=embed')
    } 
    $('#signLetterSubmission').click(function (e) {
        e.preventDefault();
        document.getElementById("certFile").required = true;
        document.getElementById("certPassword").required = true;
        var status__ = validateForm_bshor('USCS90Form');
        var letterUSCS = false;
        
        if (status__) {
            var checkboxesChecked = [];
            $('.signDoc input[type="checkbox"]:checked').each(function () {
                if($(this).next().text() === "<s:text name="utimaps.uscs90.letter" />") {
                    letterUSCS = true;
                }
                checkboxesChecked.push($(this).next().text());
            });
            if(checkboxesChecked.length>0) {
                var displayMessage = "<s:text name="utimaps.form.message.signLetter" />"+" ".concat(checkboxesChecked.join(", "), ".");

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
                        var jobId = $("#job_id").val();
                        var antiCsrf = $("#antiCsrf").val();
                        //added to update approval date first before sign
                        if(letterUSCS) {
                            $.ajax({
                                type: "POST",
                                url: "updateApprovalDateSubmission",
                                dataType: "json",
                                data: {jobId: jobId, antiCsrf: antiCsrf},
                                success: function(response) {
                                    var form = $('#USCS90Form');
                                    form.attr("action", "pdfSignSubmission");
                                    form.submit();
                                }
                            });	
                        } else {
                            var form = $('#USCS90Form');
                            form.attr("action", "pdfSignSubmission");
                            form.submit();
                        }
                    }
                }
            });
        } else {
            bootbox.alert({
                closeButton: false,
                message: "<s:text name="utimaps.form.message.signAtLeast1" />"
            });
        }
        } else {
            showErrors(document.getElementById('USCS90Form'));
        }
    });

    $('#completeUSCS90Submission').click(function (e) {
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
                        var form = $('#USCS90Form');
                        form.attr("action", "processComplete2JobSubmission");
                        form.submit();
                    }
                }
            });
        }
    });

    $('#revokeUSCSLetter').click(function (e) {
        e.preventDefault();
        var checkboxesChecked = [];
        $('.revokeDoc input[type="checkbox"]:checked').each(function () {
            checkboxesChecked.push($(this).next().text());
        });
        if(checkboxesChecked.length>0) {
        var displayMessage = "<s:text name="utimaps.form.message.revokeLetter" />".concat(checkboxesChecked.join(", "), ".");

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
                    var form = $('#USCS90Form');
                    form.attr("action", "pdfRevokeSubmission");
                    form.submit();
                }
            }
        });
    } else 
        bootbox.alert({
                closeButton: false,
                message: "<s:text name="utimaps.form.message.revokeAtLeast1" />"
            });
    });

</script>