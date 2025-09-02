<%-- 
    Document   : caseSummary
    Created on : Apr 2, 2025, 4:17:53 PM
    Author     : yonglai
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
        <h4 class="bottom-green"><s:text name="utimaps.appSummary.caseDetails" /> (UAP/<s:property value="model.case_seq"/>/<s:property value="model.case_year"/>)</h4>
        <hr class="divider m-0">
        <div class="row row-cols-1 row-cols-md-3 g-4 mt-1">
            <div class="col">
                <div class="card h-100">
                    <div class="card-body">
                        <div class="mb-3"> 
                            <label class="form-label fw-bold text-dark mb-0"><s:text name="utimaps.appSummary.nameProject" /></label>
                            <p class="fs-smaller"><s:if test="model.pj_name != ''"><s:property value="model.pj_name" /></s:if><s:else>-</s:else></p>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold text-dark mb-0"><s:text name="utimaps.appSummary.locality" /></label>
                                    <p class="fs-smaller"><s:if test="model.land_desc != ''"><s:property value="model.land_desc" /></s:if><s:else>-</s:else></p>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold text-dark mb-0"><s:text name="utimaps.appSummary.receivedFrom" /></label>
                                    <p class="fs-smaller"><s:if test="model.surveyFirmModel.company_name != 'NA'"><s:property value="model.surveyFirmModel.company_name" /></s:if><s:else>-</s:else></p>
                            </div>
                            <div class="row">
                                <div class="col-md-4 col-lg-4 col-sm-12">
                                    <div class="mb-3">
                                        <label class="form-label fw-bold text-dark mb-0"><s:text name="utimaps.appSummary.dateApplication" /></label>
                                        <p class="fs-smaller"><s:if test="model.case_createddate_str != ''"><s:property value="model.case_createddate_str" /></s:if><s:else>-</s:else></p>
                                    </div>
                                </div>
                                <div class="col-md-4 col-lg-4 col-sm-12">
                                    <div class="mb-3">
                                        <label class="form-label fw-bold text-dark mb-0"><s:text name="utimaps.appSummary.dateIssue" /></label>
                                        <p class="fs-smaller"><s:if test="model.jobDetailModel.date_issue_str != ''"><s:property value="model.jobDetailModel.date_issue_str" /></s:if><s:else>-</s:else></p>
                                    </div>
                                </div>
                                <div class="col-md-4 col-lg-4 col-sm-12">
                                    <div class="mb-3">
                                        <label class="form-label fw-bold text-dark mb-0"><s:text name="utimaps.appSummary.dateReceive" /></label>
                                        <p class="fs-smaller"><s:if test="model.app_submit_date_str != ''"><s:property value="model.app_submit_date_str" /></s:if><s:else>-</s:else></p>
                                    </div>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold text-dark mb-0"><s:text name="utimaps.appSummary.caseStatus" /></label>
                                <p class="fs-smaller"><s:property value="model.wf_status_str" /></p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0"><s:text name="utimaps.appSummary.statusDate" /></label>
                            <p class="fs-smaller">
                                <s:property value="model.jobHistoryList[0].status_date_str" />
                            </p>
                        </div>
                        <div class="row">
                            <div class="col-md-auto">
                                <a href="#" class="fs-smaller word-green fw-bold" data-bs-toggle="modal" data-bs-target="#historyModal"><i class="far fa-clock word-green"></i> <s:text name="utimaps.appSummary.caseHistory" /> </a>
                            </div>
                            <div class="col-md-auto">
                                <a href="<s:property value="detailLink"/>" class="fs-smaller word-green fw-bold"><i class="far fa-file-alt"></i> <s:text name="utimaps.appSummary.moreDetails" /> </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card h-100">
                    <div class="card-body">
                        <div class="mb-4">
                            <label class="form-label fw-bold text-dark mb-0"><s:text name="utimaps.appSummary.appTitle" /></label><br>
                            <a href="pdfViewerUSJ?type=viewAppSummary&caseId=<s:property value="model.case_id" />" target="_blank" class="word-green"><i class="far fa-file-alt"></i> <label class="form-label word-green mb-0"><s:text name="utimaps.appSummary.appBrief" /></label></a><br>
                            <i class="far fa-calendar"></i> <label class="form-label text-dark mb-0"><s:text name="utimaps.appSummary.submittedDate" /> </label>&emsp;<s:property value="model.applicationModel.app_submit_date_str" /><br>
                            <i class="far fa-check-circle"></i> <label class="form-label text-dark mb-0"><a href="pdfViewerUSJ?type=checklistUPS10&caseId=<s:property value="model.case_id" />" target="_blank" class="text-dark"><s:text name="utimaps.appSummary.checklist" /></a></label><br>
                        </div>
                        <s:if test='model.job_id != null'>
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0"><s:text name="utimaps.appSummary.subTitle" /></label><br>
                            <a href="loadViewJobSubmission?id=<s:property value="model.job_id" />" target="_blank" class="word-green"><i class="far fa-file-alt"></i> <label class="form-label word-green mb-0"><s:text name="utimaps.appSummary.jobSubmission" /></label></a>
                            <br>
                        </div>
                        </s:if>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card h-100">
                    <div class="card-body">
                        <div class="mb-4">
                            <label class="form-label fw-bold text-white bg-gray p-1 pt-0 pb-0 mb-1"><s:text name="utimaps.appSummary.surveyFirm" /></label>
                            <hr class="divider mt-0">
                            <div class="clearfix">
                                <label class="form-label text-dark mb-0"><s:text name="utimaps.appSummary.applicationUSJ" /></label><span class="form-label float-right mb-0" id="app-usj-pglabel">[0%]</span>                            
                            </div>
                            <div class="progress mb-3" width="100%">
                                <div class="progress-bar firm-progress w-0" id="app-usj" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100">
                                </div>
                            </div>
                        </div>
                        <div class="mb-4">
                            <label class="form-label fw-bold text-white bg-gray p-1 pt-0 pb-0 mb-1"><s:text name="utimaps.appSummary.lns" /></label>
                            <hr class="divider mt-0">
                            <div class="clearfix">
                                <label class="form-label text-dark mb-0"><s:text name="utimaps.appSummary.issuanceUSJ" /></label><span class="form-label float-right mb-0" id="issuance-usj-pglabel">[0%]</span>
                            </div>
                            <div class="progress mb-3">
                                <div class="progress-bar ls-progress w-0" id="issuance-usj" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row mt-3">
            <div class="accordion accordion-flush" id="accordionFlushSummary">
                <jsp:include page="caseFile.jsp"></jsp:include>
                <jsp:include page="jobPayment.jsp"></jsp:include>
                </div>
            </div>

            <div class="row mt-3">
                <div class="col">
                <jsp:include page="caseComment.jsp"></jsp:include>
                </div>
                <div class="col">
                <jsp:include page="jobActivity.jsp"></jsp:include>
                </div>
            </div>
        <jsp:include page="jobNotification.jsp"></jsp:include>
        <form name="form" id="FormID" class="text-end mt-3" method="post" action="#">
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <button class="btn btn-falcon-default btn-sm mb-1" type="submit" name="action:cancelSubmission" id="cancelSubmission"><i class="fas fa-times word-green"></i><span class="ms-1"><s:text name="button.back"/></span></button>    
        </form>
    </div>
    <div class="modal fade fixMarginLeft historyModal" id="historyModal" data-bs-keyboard="false" data-bs-backdrop="static" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
        <div class="modal-dialog historyModal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <div class="title"><s:text name="utimaps.appSummary.jobHistory" /></div>
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
    var appUsjStatus = '<s:property value="%{model.app_status}"/>';
    var appUsj = Math.round((appUsjArr.indexOf(appUsjStatus) + 1) / (appUsjArr.length) * 100);
    document.getElementById("app-usj-pglabel").innerHTML = "[" + appUsj + "%]";
    document.getElementById("app-usj").style.width = appUsj + "%";
    var issuanceUsjArr = ["100", "101", "109", "111", "113"];
    var issuanceUsjStatus = '<s:property value="%{model.wf_status}"/>';
    var issuanceUsj = Math.round((issuanceUsjArr.indexOf(issuanceUsjStatus) + 1) / (issuanceUsjArr.length) * 100);
    console.log(issuanceUsj)
    document.getElementById("issuance-usj-pglabel").innerHTML = "[" + issuanceUsj + "%]";
    document.getElementById("issuance-usj").style.width = issuanceUsj + "%";
</script>
