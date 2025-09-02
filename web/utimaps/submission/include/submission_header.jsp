<%-- 
    Document   : submission_header
    Created on : May 19, 2024, 9:12:13 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="card mb-3 border-secondary">
    <div class="card-body p-0">
        <div class="row m-0">
            <div class="col-lg-3 border-end-lg border-4 border-success border-bottom border-bottom-lg-0 rounded pt-3 pb-3 pb-lg-0 bg-green">
                <div class="mb-3">
                    <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.usj_no" /></label>
                    <p class="text-danger fw-bold fs-larger">
                    <s:if test="model.usj_no.length != ''"><s:property value="model.usj_no" /></s:if><s:else>-</s:else>
                    </p>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.app_ref" /></label>
                    <p class=""><s:if test="model.case_ref != ''"><s:property value="model.case_ref" /></s:if><s:else>-</s:else></p>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.div" /></label>
                    <p class="text-uppercase"><s:if test="model.division_name != ''"><s:property value="model.division_name" /></s:if><s:else>-</s:else></p>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.receivedFrom" /></label>
                    <p><s:if test="model.applicationModel.surveyFirmModel.company_name != ''"><s:property value="model.applicationModel.surveyFirmModel.company_name" /></s:if><s:else>-</s:else></p>
                </div>
            </div>
            <div class="col-lg-9 border-bottom border-bottom-lg-0 pt-3 pb-3 pb-lg-0 m-auto px-5">
                    <h4 class="word-green text-uppercase fw-bold mb-3"><s:text name="utimaps.form.submission.title" /></h4>
                <div class="row">
                    <div class="col-md-6 col-lg-6 col-sm-12">
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.projectName" /></label>
                            <p class="fs-smaller"><s:if test="model.applicationModel.pj_name != ''"><s:property value="model.applicationModel.pj_name" /></s:if><s:else>-</s:else></p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.locality" /></label>
                            <p class="fs-smaller"><s:if test="model.land_desc != ''"><s:property value="model.land_desc" /></s:if><s:else>-</s:else></p>
                        </div>
                        <div class="mb-3">
                            <s:if test='model.control_sv_flag.equals("Y")'>
                                <i class="fas fa-check-circle"></i> <label class="form-label fw-bold text-dark mb-0"><s:text name="utimaps.form.submission.includeTraverse" /></label>
                            </s:if>
                        </div>
                    </div>
                    <div class="col-md-6 col-lg-6 col-sm-12">
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.dateApplication" /></label>
                            <p class="fs-smaller"><s:if test="model.applicationModel.case_createddate_str != ''"><s:property value="model.applicationModel.case_createddate_str" /></s:if><s:else>-</s:else></p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0"><stext name="utimaps.form.application.dateIssue" /></label>
                            <p class="fs-smaller"><s:if test="model.date_issue_str != ''"><s:property value="model.date_issue_str" /></s:if><s:else>-</s:else></p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0"><s:text name="utimaps.form.submission.dateReceive" /></label>
                            <p class="fs-smaller"><s:if test="model.usj_submission_date_str != ''"><s:property value="model.usj_submission_date_str" /></s:if><s:else>-</s:else></p>
                            <p class="fs-smaller"><s:if test="model.hardcopy_received_date_str != ''"><s:property value="model.hardcopy_received_date_str" /> (<s:text name="utimaps.checklist.hardcopy" />)</s:if></p>
                        </div>
                        <div class="mb-3 text-right">
                            <!--<a href="#" target="_blank"><p class="word-green text-right"><i class="fas fa-file"></i> View Job Submission</p></a>-->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>