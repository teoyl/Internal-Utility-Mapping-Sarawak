<%-- 
    Document   : issue_usj_instruction
    Created on : May 21, 2024, 4:30:28 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingThree">
        <button class="accordion-button <s:if test='!wf_step.equals("4")'>collapsed</s:if> fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseThree" aria-expanded="false" aria-controls="flush-collapseThree">
            <i class="fas fa-tasks me-2"></i><s:text name="utimaps.form.issuance.title" />
        </button>
    </h2>
    <div id="flush-collapseThree" class="accordion-collapse collapse <s:if test='wf_step.equals("4") || wf_step.equals("5")'>show</s:if>" aria-labelledby="flush-headingThree" data-bs-parent="#accordionFlushApplication">
        <div class="accordion-body">
            <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
            <form action="" method="post" id="IssuanceJobForm" enctype="multipart/form-data">
                <s:hidden name="case_id" id="case_id" value="%{model.case_id}"/>
                <s:hidden name="job_id" id="job_id" value="%{model.job_id}"/>
                <s:hidden name="jobId_" id="jobId_" value="%{model.job_id}"/>
                <s:hidden name="taskId_" value="%{taskId_}"/>
                <s:hidden name="wfActivityCode" value="%{wfActivityCode}"/> 
                <s:hidden name="model.wf_status" id="model.wf_status" value="%{model.wf_status}"/> 
                <s:hidden name="usj_div" id="usj_div" value="%{model.usj_div}"/>
                <s:hidden name="usj_no" id="usj_no" value="%{model.usj_no}"/>
                <s:hidden name="model.applicationModel.app_submit_by" value="%{model.applicationModel.app_submit_by}"/>

                <div class="row">
                    <div class="col-md-12 text-end mb-3">

                        <s:if test="model.wf_status > '112'">
                            <s:if test="model.getSignedUsjLetterList().size() > 0">
                                <button class="btn btn-danger" type="submit" name="action:pdfRevokeIssuance" id="revokeUSJLetter"><i class="fas fa-times"></i> <s:text name="utimaps.form.button.recall" /></button>
                            </s:if>
                            <s:elseif test="model.getUsjLetterList().size() > 0">
                                <a href="pdfViewerIssuance?type=AppCheck_USJ&caseId=<s:property value="model.case_id" />" target="_blank" class="btn btn-primary" type="button"><s:text name="utimaps.form.button.preview" /></a>
                                <button class="btn btn-info" id="generateUSJLetter"><i class="fas fa-download"></i> <s:text name="utimaps.form.issuance.generateUSJ" /></button>
                                <button class="btn btn-primary" id="processUpdateIssuance"><i class="fas fa-save"></i> <s:text name="utimaps.form.button.save" /></button>
                                <button class="btn btn-primary" id="routeBackJobIssuance" type="submit"><i class="fas fa-undo"></i> <s:text name="utimaps.form.button.routeBack" /></button>
                            </s:elseif>
                            <s:else>
                                <a href="pdfViewerIssuance?type=AppCheck_USJ&caseId=<s:property value="model.case_id" />" target="_blank" class="btn btn-primary" type="button"><s:text name="utimaps.form.button.preview" /></a>
                                <button class="btn btn-info" id="generateUSJLetter"><i class="fas fa-download"></i> <s:text name="utimaps.form.issuance.generateUSJ" /></button>
                                <button class="btn btn-primary" id="processUpdateIssuance"><i class="fas fa-save"></i> <s:text name="utimaps.form.button.save" /></button>
                                <button class="btn btn-primary" id="routeBackJobIssuance" type="submit"><i class="fas fa-undo"></i> <s:text name="utimaps.form.button.routeBack" /></button>
                            </s:else>
                        </s:if>
                        <s:else>
                            <button class="btn btn-primary" id="processUpdateIssuance"><i class="fas fa-save"></i> <s:text name="utimaps.form.button.save" /></button>
                            <button class="btn btn-success" id="completeJobIssuance" type="submit"><i class="fas fa-check"></i> <s:text name="utimaps.form.button.complete" /></button>
                        </s:else>

                        <s:if test="model.wf_status > '112'">
                            <s:if test="model.getSignedUsjLetterList().size() > 0">
                            <button class="btn btn-success" id="completeJobIssuance" type="submit"><i class="fas fa-check"></i> <s:text name="utimaps.form.button.complete" /></button>
                            </s:if>
                        </s:if>
                    </div>
                </div>

                <s:if test="model.wf_status > '112'">
                    <s:if test="model.getSignedUsjLetterList().size() > 0">
                    </s:if>
                    <s:elseif test="model.getUsjLetterList().size() > 0">
                        <div class="card-header p-3 border-bottom bg-light">
                            <h4 class="mb-0"><s:text name="utimaps.form.issuance.signUSJ" /></h4>
                        </div>
                        <div class="card-body p-1 ">
                            <div class="mb-3 row">
                                <label class="col-md-3 col-form-label" for="certFile"><s:text name="utimaps.form.label.retrieveDigicert" /> </label>
                                <div class="col-md-7 mb-2">
                                    <s:file class="form-control" name="certFile" accept="application/x-pkcs12" />
                                </div>
                                <label class="col-md-3 col-form-label" for="certPassword"><s:text name="utimaps.form.label.password" /></label>
                                <div class="col-md-7 mb-2">
                                    <s:password cssClass="form-control" name="certPassword" value="" />
                                </div>
                                <div class="col-md-10 text-end">
                                    <button class="btn btn-success" id="signUSJLetter" type="submit"><s:text name="utimaps.form.button.sign" /></button>
                                </div>
                            </div>
                        </div>
                        <hr/>
                    </s:elseif>
                    <s:else>
                        <div class="card">
                            <div class="card-body">
                                <p class="fw-bold"><s:text name="utimaps.form.issuance.notes" /></p>
                            </div>
                        </div>
                    </s:else>
                </s:if>
                <br>

                <div class="mb-3 row">
                    <div class="col-md-8">
                        <div class="row">
                            <label class="col-md-4 col-form-label fs-smaller">
                                <label class="col-form-label ">&nbsp;</label><br />
                                <s:text name="utimaps.form.issuance.sjNo" /><font class="asterisk">*</font></label>
                            <div class="col-md-8">
                                <div class="row g-3 align-items-center">
                                    <div class="col-auto">
                                        <label class="col-form-label ">&nbsp;</label><br />
                                        <label class="col-form-label"><s:text name="utimaps.form.issuance.usj" /> / <s:property value ="model.application_div_display"/> / </label>
                                    </div>
                                    <div class="col-3 text-center">
                                        <label class="col-form-label "><small><s:text name="utimaps.form.issuance.seq" /></small></label>
                                        <s:textfield class="form-control" name = "model.usj_seq" value = "%{model.usj_seq}" maxlength="4" data-inputmask="'mask': '9999'" readonly="%{noRightUpdate}" required ="required" />
                                    </div>
                                    <div class="col-auto">
                                        <label class="col-form-label ">&nbsp;</label><br />
                                        <label class="col-form-label"> / </label>
                                    </div>
                                    <div class="col-auto text-center">
                                        <label class="col-form-label "><small><s:text name="utimaps.form.issuance.year" /></small></label>
                                        <s:select 
                                            name="model.usj_year"
                                            value="%{model.usj_year}"
                                            list="USJYearList"
                                            listKey="keyData"
                                            listValue="valueData"                    
                                            cssClass="form-select"
                                            readonly = "%{noRightUpdate}"
                                            required="required"
                                         />
                                    </div>
                                    <div class="col-auto">
                                        <label class="col-form-label ">&nbsp;</label><br />
                                        <span id="sampleUSJ" class="form-text"><s:text name="utimaps.form.issuance.usjExp" /></span>
                                    </div>
                                </div>

                                <div class="mb-3 row"></div>
                            </div>

                            <label class="col-md-4 col-form-label fs-smaller" for="model_sj_firm"><s:text name="utimaps.form.issuance.classification" /> <font class="asterisk">*</font></label>
                            <div class="col-md-8">
                                <s:hidden name="model.usj_classification" value="%{model.usj_classification}" />
                                <s:select 
                                    name="model.usj_classification"
                                    value="%{model.usj_classification}"
                                    list="classificationList"
                                    listKey="keyData"
                                    listValue="valueData"                    
                                    cssClass="form-select"
                                    disabled="true"
                                 />
                                <div class="mb-3 row"></div>
                            </div>

                            <label class="col-md-4 col-form-label fs-smaller" for="model_sj_firm"><s:text name="utimaps.form.issuance.surveyFirm" /></label>
                            <div class="col-md-8">
                                <s:property value="%{model.applicationModel.surveyFirmModel.company_name}" />
                                <div class="mb-3 row"></div>
                            </div>

                            <label class="col-md-4 col-form-label fs-smaller" for="model_case_ref"><s:text name="utimaps.form.issuance.fileRef" /></label>
                            <div class="col-md-8">
                                <div class="input-group">
                                    <s:textfield cssClass="form-control" name="model.case_ref" value="%{model.case_ref}" readonly="true" />
                                </div>
                                <div class="mb-3 row"></div>
                            </div>

                            <label class="col-md-4 col-form-label fs-smaller" for="model_sj_firm"><s:text name="utimaps.form.issuance.jobDesc" /></label>
                            <div class="col-md-8">
                                <s:textarea theme="simple" name="model.applicationModel.pj_name" value="%{model.applicationModel.pj_name}" cssClass="form-control" rows="6" readonly="true" />
                                <div class="mb-3 row"></div>
                            </div>

                            <label class="col-md-4 col-form-label fs-smaller" for="model_usj_job_type"><s:text name="utimaps.form.issuance.requestor" /></label>
                            <div class="col-md-8">
                                <s:select 
                                    name="model.requestor_branch"
                                    value="%{model.requestor_branch}"
                                    list="branchList"
                                    listKey="keyData"
                                    listValue="valueData"                    
                                    cssClass="form-select"
                                 />
                                <div class="mb-3 row"></div>
                            </div>

                            <%-- Survey Duration to be removed as requested during external preview --%>
                            <%--
                            <label class="col-md-4 col-form-label fs-smaller" for="model_land_desc"><s:text name="utimaps.form.issuance.surveyDate" /> <font class="asterisk">*</font></label>
                            <div class="col-md-8">
                                <div class="input-group input-group-sm" id="survey_date_DateRange">
                                    <div class="input-group-text input-button" data-toggle>
                                        <i class="far fa-calendar-alt"></i>
                                    </div>

                                    <s:hidden cssClass="form-control form-control-sm" id="survey_date" name="model.survey_date_str" value="" readonly="true" required="true" data-input="true" />
                                    <div class="input-group-text input-button" data-clear>
                                        <i class="fa fa-times" ></i>
                                    </div>

                                    <s:hidden cssClass="form-control form-control-sm" id="survey_dateFrom" name="model.survey_date_start_str" value="%{model.survey_date_start_str}" />
                                    <s:hidden cssClass="form-control form-control-sm" id="survey_dateTo" name="model.survey_date_end_str" value="%{model.survey_date_end_str}" />
                                </div>
                                <div class="mb-3 row"></div>
                            </div>
                            --%>

                            <label class="col-md-4 col-form-label fs-smaller" for="model_qual_level"><s:text name="utimaps.form.issuance.qualLevel" /></label>
                            <div class="col-md-8">
                                <s:property value="%{model.qual_level_str}" />
                                <div class="mb-3 row"></div>
                            </div>
                                
                            <label class="col-md-4 col-form-label fs-smaller" for="model_qual_level"><s:text name="utimaps.form.issuance.surveyPlanNo" /></label>
                            <div class="col-md-8">
                                <div class="input-group fw-bold">
                                    <s:hidden name="model.plan_no" value="%{model.plan_no}"/>
                                    <s:if test="model.plan_no != null">
                                        USP-<s:property value = "model.usj_div" />-<s:property value = "model.plan_no" />
                                    </s:if>
                                    <s:else>
                                        -
                                    </s:else>
                                </div>
                                <div class="mb-3 row"></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 text-center ">
                        
                        <s:if test="model.getSignedUsjLetterList().size() > 0">
                            <div class="pt-3">
                                <i class="far fa-file-pdf fa-6x mb-3"></i>
                                <p class="mb-0"><button class="btn btn-link downloadUSJLetter fs-smaller fw-bold" id="downloadUSJLetter" value="<s:property value = "model.getSignedUsjLetterList()[0].file_id" />"><s:text name="utimaps.form.label.download" /> <s:property value = "model.getSignedUsjLetterList()[0].file_name" /></button> </p>
                                <p class="mb-0 fs-smaller"><s:text name="utimaps.form.issuance.isseuDate" /><br> <s:property value = "model.getSignedUsjLetterList()[0].created_date_str" /></p>
                            </div>
                        </s:if>
                        <s:elseif test="model.getUsjLetterList().size() > 0">
                            <div class="pt-3">
                                <i class="far fa-file-pdf fa-6x mb-3"></i>
                                <p class="mb-0"><button class="btn btn-link downloadUSJLetter fs-smaller fw-bold" id="downloadUSJLetter" value="<s:property value = "model.getUsjLetterList()[0].file_id" />"><s:text name="utimaps.form.label.download" />  <s:property value = "model.getUsjLetterList()[0].file_name" /></button> </p>
                            </div>
                        </s:elseif>
                        <s:else>
                        </s:else>
                    </div>
                    <div class="col-md-12">
                        <label class="col-md-12 col-form-label fs-smaller" for="model_usj_request"><s:text name="utimaps.form.issuance.detailRequest" /> &nbsp;
                        <s:select 
                            name="instructionType"
                            value=""
                            list="requestInstructionList"
                            listKey="keyData"
                            listValue="valueData"                    
                            cssClass="form-select d-inline w-50"
                         />
                        </label>
                        <div class="col-md-12 min-vh-50 mb-3 ">
                            <s:textarea theme="simple" name="model.usj_request" value="%{model.usj_request}" cssClass="form-control" rows="15" readonly="%{noRightUpdate}" />
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="include/input-mask/jquery.inputmask.js"></script>
<script nonce="r4DjhKbfO5ry">
    $( document ).ready(function() {
        const numericField = document.getElementById('model_usj_seq');

        numericField.addEventListener('input', (event) => {
            numericField.value = numericField.value.replace(/[^0-9.]/g, '');
        });
        
        initEditor("model.usj_request", 4000, 350);

//        registerDateRangePicker('survey_date');
        
        $("#instructionType").on("change", function(e) {
            var instructionType = $(this).val();
            
            $.ajax({
                type: "POST",
                url: 'getInstructionTemplateIssuance',
                dataType: "json",
                data: {instruction_type: instructionType},
                success: function(response) {
                    bootbox.confirm({
                        closeButton: false,
                        message: "<s:text name="utimaps.form.message.replaceTemplate" />",
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
                                $('#model_usj_request').val(response.template);
                            }
                        }
                    });
                } 
            });
        });
        
        $('#processUpdateIssuance').click( function (e) {
            e.preventDefault();
            var string = '';

            var usjSeq = $("#model_usj_seq").val();
            var usjYear = $("#model_usj_year").val();
            var usjDiv = $("#usj_div").val();
            var jobId = $("#job_id").val();
//            var surveyDate = $("#survey_date").val();
//            var surveyDateFrom = $("#survey_dateFrom").val();
//            var surveyDateTo = $("#survey_dateTo").val();
            var canProceed = true;
            var displayMessage = "Are you sure want to save?";

//            if(surveyDateFrom === "" || surveyDateTo === "" || usjSeq === "" || usjYear === "") {
            if(usjSeq === "" || usjYear === "") {
                canProceed = false;
                alertBox("Survey Job Number must be filled.");
            }

            if(canProceed) {
                $('#loadingModal').modal('show');
                $.ajax({
                    type: "POST",
                    url: 'checkSurveyJobNoIssuance',
                    dataType: "json",
                    data: {usj_seq: usjSeq, usj_year: usjYear, usj_div: usjDiv, job_id: jobId},
                    success: function(response) {
                        
                        setTimeout(function() {
                            $('body').removeAttr("style");
                            $('#loadingModal').hide();
                            $('#loadingModal').removeClass("show");
                            $('#loadingModal').css("display: none");
                            $('body').removeClass('modal-open');
                            $('.modal-backdrop').remove();

                            if(response['status'] === false) {
                                // 05-03-2025 :: To bypass this case as requested by LNS 
                                var sjNoToExcluded = "00422025";
                                if(usjSeq + usjYear === sjNoToExcluded) {
                                    canProceed = true;
                                } else {
                                    canProceed = false;
                                    var msg = response['message'];
                                    alertBox(msg);
                                }
                            }

                            if(canProceed) {
                                confirmationBox(displayMessage, "#IssuanceJobForm", "processUpdateJobIssuance");
                            }
                        }, 2000);
                    }
                });
            }
        });
    
        $('#completeJobIssuance').click( function (e) {
            e.preventDefault();
            var canProceed = true;
            var displayMessage = "<s:text name="utimaps.form.message.completeTask" />";
            
            var usjSeq = $("#model_usj_seq").val();
            var usjYear = $("#model_usj_year").val();
            var usjNo = $("#usj_no").val();
            var alertMsg = "";
            console.log(usjNo)
//            var surveyDateFrom = $("#survey_dateFrom").val();
//            var surveyDateTo = $("#survey_dateTo").val();
            
//            if(usjSeq === "" || usjYear === "" || surveyDateFrom === "" || surveyDateTo === "") {
            if(usjSeq === "" || usjYear === "" || usjNo === "") {
                if(usjNo === "") {
                    alertMsg = "<s:text name="utimaps.form.message.saveBeforeComplete" />";
                } else {
                    alertMsg = "<s:text name="utimaps.form.message.fillInRequired" />";
                }
                canProceed = false;
            }

            if(canProceed) {
                confirmationBox(displayMessage, "#IssuanceJobForm", "processCompleteJobIssuance");
            } else {
                alertBox(alertMsg);
            } 
        });

        $('#routeBackJobIssuance').click( function (e) {
            e.preventDefault();
            var displayMessage = "<s:text name="utimaps.form.message.routeBack" />";

            confirmationBox(displayMessage, "#IssuanceJobForm", "processRouteBackJobIssuance");
        });

        $('#signUSJLetter').click( function (e) {
            e.preventDefault();
            
            document.getElementById("certFile").required = true;
            document.getElementById("certPassword").required = true;
            
            var status__ = validateForm_bshor('IssuanceJobForm');
            
        var displayMessage = "<s:text name="utimaps.form.message.signLetter" />";

            if (status__) {
                confirmationBox(displayMessage, "#IssuanceJobForm", "pdfSignIssuance");
            }
        });

        $('#revokeUSJLetter').click( function (e) {
            e.preventDefault();
            var displayMessage = "<s:text name="utimaps.form.message.revokeLetter" />";

            confirmationBox(displayMessage, "#IssuanceJobForm", "pdfRevokeIssuance");
        });

        $("#generateUSJLetter").on("click", function(e) {
            e.preventDefault();

            bootbox.confirm({
                closeButton: false,
                message: "<s:text name="utimaps.form.message.generateLetter" />", 
                buttons: {
                    confirm: {
                        label: 'PROCEED'
                    },
                    cancel: {
                        label: 'CANCEL'
                    }
                },
                callback: function(result) {
                    if(result) {
                        $('#loadingModal').modal('show');
                        $.ajax({
                            type: "POST",
                            url: 'generateLetterIssuance',
                            dataType: "json",
                            data: {caseId: "<s:property value="model.case_id" />", jobId: "<s:property value="model.job_id" />", antiCsrf: "<s:property value="%{#session.antiCsrf}"/>"},
                            success: function(response) {
                                if(response["status"] === "Y") {
                                    $('#loadingModal').modal('hide');
                                    window.open("viewTempFileIssuance?fileID=" + response["fileId"],"_blank");
                                    location.reload();
                                }
                            }
                        });
                    }
                }
            });
        });

        $(".downloadUSJLetter").on("click", function(e) {
            e.preventDefault();
            var fileId =  $(this).val();
            
            window.open("viewTempFileIssuance?fileID=" + fileId,"_blank");
        });
    
    });
</script>