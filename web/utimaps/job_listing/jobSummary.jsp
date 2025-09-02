<%-- 
    Document   : jobSummary
    Created on : Jul 26, 2024, 11:08:02 AM
    Author     : Arine
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<link href="styles/sub_styles.css" rel="stylesheet" />

<style nonce = "EuTVqS192VKl">
    .historyModal {
        width: 70%;
        margin-left: 15%;
    }

    .historyModal-dialog {
        max-width: 100%;
        width: auto !important;
    }
</style>

<div class="card">
    <div class="card-body">
        <h4 class="bottom-green">Job Details (<s:property value="model.usj_no"/>)</h4>
        <hr class="divider m-0">
        <div class="row row-cols-1 row-cols-md-3 g-4 mt-1">
            <div class="col">
                <div class="card h-100">
                    <div class="card-body">
                        <div class="mb-3"> 
                            <label class="form-label fw-bold text-dark mb-0">Name of Utility Project</label>
                            <p class="fs-smaller"><s:if test="model.applicationModel.pj_name != ''"><s:property value="model.applicationModel.pj_name" /></s:if><s:else>-</s:else></p>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold text-dark mb-0">Locality</label>
                                    <p class="fs-smaller"><s:if test="model.land_desc != ''"><s:property value="model.land_desc" /></s:if><s:else>-</s:else></p>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold text-dark mb-0">Received From</label>
                                    <p class="fs-smaller"><s:if test="model.applicationModel.surveyFirmModel.company_name != 'NA'"><s:property value="model.applicationModel.surveyFirmModel.company_name" /></s:if><s:else>-</s:else></p>
                            </div>
                            <div class="row">
                                <div class="col-md-4 col-lg-4 col-sm-12">
                                    <div class="mb-3">
                                        <label class="form-label fw-bold text-dark mb-0">Date of Application</label>
                                            <p class="fs-smaller"><s:if test="model.applicationModel.case_createddate_str != ''"><s:property value="model.applicationModel.case_createddate_str" /></s:if><s:else>-</s:else></p>
                                    </div>
                                </div>
                                <div class="col-md-4 col-lg-4 col-sm-12">
                                    <div class="mb-3">
                                        <label class="form-label fw-bold text-dark mb-0">Date of Issue</label>
                                            <p class="fs-smaller"><s:if test="model.date_issue_str != ''"><s:property value="model.date_issue_str" /></s:if><s:else>-</s:else></p>
                                    </div>
                                </div>
                                <div class="col-md-4 col-lg-4 col-sm-12">
                                    <div class="mb-3">
                                        <label class="form-label fw-bold text-dark mb-0">Date of Receive</label>
                                            <p class="fs-smaller"><s:if test="model.usj_submission_date_str != ''"><s:property value="model.usj_submission_date_str" /></s:if><s:else>-</s:else></p>
                                            <!--<p class="fs-smaller"><s:if test="model.hardcopy_received_date_str != ''"><s:property value="model.hardcopy_received_date_str" /> (Hardcopy)</s:if></p>-->
                                    </div>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold text-dark mb-0">Job Status</label>
                                <p class="fs-smaller"><s:property value="model.jobHistoryList[0].status_code_str" /></p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0">Status Date</label>
                            <p class="fs-smaller"><s:property value="model.jobHistoryList[0].status_date_str" /></p>
                        </div>
                        <div class="row">
                            <div class="col-md-auto">
                                <a target="_blank" href="loadViewPageMapViewer?jobId=<s:property value="model.job_id" />" class="fs-smaller word-green fw-bold"><i class="far fa-map"></i> Map</a>
                            </div>
                            <div class="col-md-auto">
                                <a href="#" class="fs-smaller word-green fw-bold" data-bs-toggle="modal" data-bs-target="#historyModal"><i class="far fa-clock word-green"></i> Job History</a>
                            </div>
                            <div class="col-md-auto">
                                <a href="<s:property value="detailLink"/>" class="fs-smaller word-green fw-bold"><i class="far fa-file-alt"></i> More Details</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card h-100">
                    <div class="card-body">
                        <div class="mb-4">
                            <label class="form-label fw-bold text-dark mb-0">USJ Application</label><br>
                            <a href="pdfViewerUSJ?type=viewAppSummary&caseId=<s:property value="model.case_id" />" target="_blank" class="word-green"><i class="far fa-file-alt"></i> <label class="form-label word-green mb-0">Application Brief</label></a><br>
                            <i class="far fa-calendar"></i> <label class="form-label text-dark mb-0">Submitted Date: </label>&emsp;<s:property value="model.applicationModel.app_submit_date_str" /><br>
                            <i class="far fa-check-circle"></i> <label class="form-label text-dark mb-0"><a href="pdfViewerUSJ?type=checklistUPS10&caseId=<s:property value="model.case_id" />" target="_blank" class="text-dark">Checklist</a></label><br>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0">USJ Submission</label><br>
                            <a href="viewTempFileSubmission?fileID=<s:property value="model.signedUsjLetterList[0].file_id" />" target="_blank" class="word-green"><i class="far fa-file-alt"></i> <label class="form-label word-green mb-0">Job Submission</label></a><br>
                            <i class="far fa-calendar"></i> <label class="form-label text-dark mb-0">Submitted Date: </label>&emsp;<s:property value="model.usj_submission_date_str" /><br>
                            <div class="row">
                                <div class="col-md-4 col-lg-4 col-sm-12">
                                    <i class="far fa-check-circle"></i> <label class="form-label text-dark mb-0"><a href="pdfViewerUSJ?type=checklistUPS10&caseId=<s:property value="model.case_id" />" target="_blank" class="text-dark">UPS10</a></label><br>
                                </div>
                                <div class="col-md-8 col-lg-8 col-sm-12">
                                    <s:if test='model.control_sv_flag.equals("Y")'>
                                        <s:if test='(checkHasChecklist(model.job_id, "U10"))'>
                                            <a href="pdfViewerSubmission?pType=U10&pJobId=<s:property value="model.job_id" />" target="_blank" class="word-green"><i class="far fa-check-circle"></i> <label class="form-label text-dark mb-0">U10</label><br></a>
                                            <a href="pdfViewerSubmission?pType=U20&pJobId=<s:property value="model.job_id" />" target="_blank" class="word-green"><i class="far fa-check-circle"></i> <label class="form-label text-dark mb-0">U20</label><br></a>
                                        </s:if>
                                        <s:else>
                                            <i class="far fa-circle"></i> <label class="form-label text-dark mb-0">U10</label><br>
                                            <i class="far fa-circle"></i> <label class="form-label text-dark mb-0">U20</label><br>
                                        </s:else>
                                    </s:if>
                                    <s:else>
                                        <i class="far fa-circle"></i> <label class="form-label text-dark mb-0">U10 (N/A)</label><br>
                                        <s:if test='(checkHasChecklist(model.job_id, "U20"))'>
                                            <a href="pdfViewerSubmission?pType=U20&pJobId=<s:property value="model.job_id" />" target="_blank" class="word-green"><i class="far fa-check-circle"></i> <label class="form-label text-dark mb-0">U20</label><br></a>
                                        </s:if>
                                        <s:else>
                                            <i class="far fa-circle"></i> <label class="form-label text-dark mb-0">U20</label><br>
                                        </s:else>
                                    </s:else>
                                    <s:if test='(checkHasChecklist(model.job_id, "U21"))'>
                                        <a href="pdfViewerSubmission?pType=U21&pJobId=<s:property value="model.job_id" />" target="_blank" class="word-green"><i class="far fa-check-circle"></i> <label class="form-label text-dark mb-0">U21</label><br></a>
                                    </s:if>
                                    <s:else>
                                        <i class="far fa-circle"></i> <label class="form-label text-dark mb-0">U21</label><br>
                                    </s:else>
                                    <s:if test='(checkHasChecklist(model.job_id, "U30"))'>
                                        <a href="pdfViewerSubmission?pType=U30&pJobId=<s:property value="model.job_id" />" target="_blank" class="word-green"><i class="far fa-check-circle"></i> <label class="form-label text-dark mb-0">U30</label><br></a>
                                    </s:if>
                                    <s:else>
                                        <i class="far fa-circle"></i> <label class="form-label text-dark mb-0">U30</label><br>
                                    </s:else>
                                    <s:if test='(checkHasChecklist(model.job_id, "U40"))'>
                                        <a href="pdfViewerSubmission?pType=U40&pJobId=<s:property value="model.job_id" />" target="_blank" class="word-green"><i class="far fa-check-circle"></i> <label class="form-label text-dark mb-0">U40</label><br></a>
                                    </s:if>
                                    <s:else>
                                        <i class="far fa-circle"></i> <label class="form-label text-dark mb-0">U40</label><br>
                                    </s:else>
                                    <s:if test='(checkHasChecklist(model.job_id, "U50"))'>
                                        <a href="pdfViewerSubmission?pType=U50&pJobId=<s:property value="model.job_id" />" target="_blank" class="word-green"><i class="far fa-check-circle"></i> <label class="form-label text-dark mb-0">U50</label><br></a>
                                    </s:if>
                                    <s:else>
                                        <i class="far fa-circle"></i> <label class="form-label text-dark mb-0">U50</label><br>
                                    </s:else>
                                    <s:if test='(checkHasChecklist(model.job_id, "U60"))'>
                                        <a href="pdfViewerSubmission?pType=U60&pJobId=<s:property value="model.job_id" />" target="_blank" class="word-green"><i class="far fa-check-circle"></i> <label class="form-label text-dark mb-0">U60</label><br></a>
                                    </s:if>
                                    <s:else>
                                        <i class="far fa-circle"></i> <label class="form-label text-dark mb-0">U60</label><br>
                                    </s:else>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card h-100">
                    <div class="card-body">
                        <div class="mb-4">
                            <label class="form-label fw-bold text-white bg-gray p-1 pt-0 pb-0 mb-1">SURVEY FIRM</label>
                            <hr class="divider mt-0">
                            <%--<s:hidden name="usj_app" id="usj_app" value="%{model.case_id}"/>--%>
                            <div class="clearfix">
                                <label class="form-label text-dark mb-0">Application for Issuance of USJ</label><span class="form-label float-right mb-0" id="app-usj-pglabel">[0%]</span>                            
                            </div>
                            <div class="progress mb-3" width="100%">
                                <div class="progress-bar firm-progress w-0" id="app-usj" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100">
                                    <!--<span class="sr-only">70% Complete</span>-->
                                </div>
                            </div>
                            <div class="clearfix">
                                <label class="form-label text-dark mb-0">Submission of Utility Survey</label><span class="form-label float-right mb-0" id="submission-usj-pglabel">[0%]</span>
                            </div>
                            <div class="progress mb-3">
                                <div class="progress-bar firm-progress w-0" id="submission-usj" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100">
                                    <!--<span class="sr-only">70% Complete</span>-->
                                </div>
                            </div>
                        </div>
                        <div class="mb-4">
                            <label class="form-label fw-bold text-white bg-gray p-1 pt-0 pb-0 mb-1">L&S</label>
                            <hr class="divider mt-0">
                            <div class="clearfix">
                                <label class="form-label text-dark mb-0">Issuance of USJ</label><span class="form-label float-right mb-0" id="issuance-usj-pglabel">[0%]</span>
                            </div>
                            <div class="progress mb-3">
                                <div class="progress-bar ls-progress w-0" id="issuance-usj" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100">
                                    <!--<span class="sr-only">70% Complete</span>-->
                                </div>
                            </div>
                            <s:if test='model.control_sv_flag.equals("Y")'>
                                <div class="clearfix">
                                    <label class="form-label text-dark mb-0">Computation of Control Survey</label><span class="form-label float-right mb-0" id="comp-survey-pglabel">[0%]</span>  
                                </div>
                                <div class="progress mb-3">
                                    <div class="progress-bar ls-progress w-0" id="comp-survey" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100">
                                        <!--<span class="sr-only">70% Complete</span>-->
                                    </div>
                                </div>
                            </s:if>
                            <div class="clearfix">
                                <label class="form-label text-dark mb-0">Processing and Approval of Utility Survey</label><span class="form-label float-right mb-0" id="process-usj-pglabel">[0%]</span>
                            </div>
                            <div class="progress mb-3">
                                <div class="progress-bar ls-progress w-0" id="process-usj" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100">
                                    <!--<span class="sr-only">70% Complete</span>-->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row mt-3">
            <div class="accordion accordion-flush" id="accordionFlushSummary">
                <jsp:include page="jobFile.jsp"></jsp:include>
                <jsp:include page="jobPayment.jsp"></jsp:include>
                </div>
            </div>

            <div class="row mt-3">
                <div class="col">
                <jsp:include page="jobComment.jsp"></jsp:include>
                </div>
                <div class="col">
                <jsp:include page="jobActivity.jsp"></jsp:include>
                </div>
            </div>
        <jsp:include page="jobNotification.jsp"></jsp:include>
        
        <br/>
        <s:include value="../submission/pages/utility_provider_form.jsp">
            <s:param name="className_">HQ</s:param>
        </s:include >   

        <form name="form" id="FormID" class="text-end mt-3" method="post" action="#">
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <button class="btn btn-falcon-default btn-sm mb-1" type="submit" name="action:cancelSubmission" id="cancelSubmission"><i class="fas fa-times word-green"></i><span class="ms-1"><s:text name="button.back"/></span></button>    
        </form>
    </div>
    <div class="modal fade fixMarginLeft historyModal" id="historyModal" data-bs-keyboard="false" data-bs-backdrop="static" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
        <div class="modal-dialog historyModal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <div class="title">Job History</div>
                    <button class="btn-close btn btn-sm d-flex flex-center transition-base" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <jsp:include page="jobHistory.jsp"></jsp:include>
                </div>
            </div>
        </div>
    </div>
</div>

<script nonce="r4DjhKbfO5ry">
    var range = function (start, end, step) {
        var range = [];
        var typeofStart = typeof start;
        var typeofEnd = typeof end;
        typeof step == "undefined" && (step = 1);
        if (end < start) {
            step = -step;
        }
        if (typeofStart == "number") {
            while (step > 0 ? end >= start : end <= start) {
                range.push(start);
                start += step;
            }
        }
        return range;
    };

    var appUsjArr = ["001", "002", "003", "004", "005", "006", "007", "008"];
    var appUsjStatus = '<s:property value="%{model.applicationModel.app_status}"/>';
    var appUsj = Math.round((appUsjArr.indexOf(appUsjStatus) + 1) / (appUsjArr.length) * 100);
    document.getElementById("app-usj-pglabel").innerHTML = "[" + appUsj + "%]";
    document.getElementById("app-usj").style.width = appUsj + "%";
    var submissionUsjArr = ["008", "009", "010", "011", "012", "013", "014", "015", "016", "017", "020"];
    var submissionUsjStatus = '<s:property value="%{model.usj_status}"/>';
    var submissionUsj = Math.round((submissionUsjArr.indexOf(submissionUsjStatus) + 1) / (submissionUsjArr.length) * 100);
    document.getElementById("submission-usj-pglabel").innerHTML = "[" + submissionUsj + "%]";
    document.getElementById("submission-usj").style.width = submissionUsj + "%";

    var wfStatus = <s:property value="model.jobHistoryList[0].status_code" />;
    console.log(wfStatus);
    if (wfStatus < 116) {
        var wfStatusArr = range(101, 115, 1);
        console.log(wfStatusArr);
        var submissionUsj = Math.round((wfStatusArr.indexOf(wfStatus) + 1) / (wfStatusArr.length) * 100);
        document.getElementById("issuance-usj-pglabel").innerHTML = "[" + submissionUsj + "%]";
        document.getElementById("issuance-usj").style.width = submissionUsj + "%";
    } else if (wfStatus > 115) {
        document.getElementById("issuance-usj-pglabel").innerHTML = "[100%]";
        document.getElementById("issuance-usj").style.width = "100%";
        var wfStatusArr = [];
        if ('<s:property value="model.control_sv_flag" />' == "Y") {
            if (wfStatus > 271) {
                document.getElementById("comp-survey-pglabel").innerHTML = "[100%]";
                document.getElementById("comp-survey").style.width = "100%";
                wfStatusArr = [280, 281, 282, 290, 291, 300, 301, 310];
                var submissionUsj = Math.round((wfStatusArr.indexOf(wfStatus) + 1) / (wfStatusArr.length) * 100);
                document.getElementById("process-usj-pglabel").innerHTML = "[" + submissionUsj + "%]";
                document.getElementById("process-usj").style.width = submissionUsj + "%";
            } else {
                //U10 & U20
                wfStatusArr = [130, 131, 132, 133, 134, 135, 136, 140, 141, 142, 143, 144, 145, 146];
                //uscs10 &20
                wfStatusArr.push(150, 151, 152, 153);
                //hardcopy, query uscs10, uscs30
                wfStatusArr.push(160, 161, 162, 163, 164, 170, 171, 172, 173, 174, 182, 183, 184);
                //U30, uscs40,u40, uscs50, query uscs40,uscs60, uscs70
                wfStatusArr.push(200, 201, 202, 210, 211, 212, 213, 214, 220, 221, 222, 230, 231, 232, 233, 234, 250, 251, 270, 271);
                var submissionUsj = Math.round((wfStatusArr.indexOf(wfStatus) + 1) / (wfStatusArr.length) * 100);
                document.getElementById("comp-survey-pglabel").innerHTML = "[" + submissionUsj + "%]";
                document.getElementById("comp-survey").style.width = submissionUsj + "%";
            }
        } else {
            //u20, uscs10, uscs20, hardcopy, uscs30
            wfStatusArr = [140, 141, 142, 143, 144, 145, 146, 150, 151, 170, 171, 172, 173, 174, 152, 153, 160, 161, 162, 163, 164, 182, 183, 184];
            wfStatusArr.push(280, 281, 282, 290, 291, 300, 301, 310);
            var submissionUsj = Math.round((wfStatusArr.indexOf(wfStatus) + 1) / (wfStatusArr.length) * 100);
            document.getElementById("process-usj-pglabel").innerHTML = "[" + submissionUsj + "%]";
            document.getElementById("process-usj").style.width = submissionUsj + "%";
        }
    }
</script>
