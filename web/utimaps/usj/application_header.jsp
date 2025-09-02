<%-- 
    Document   : application_header
    Created on : May 21, 2024, 4:24:16 PM
    Author     : yonglai
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="card mb-3 border-secondary">
    <div class="card-body p-0">
        <div class="row m-0">
            <div class="col-lg-5 border-end-lg border-4 border-success border-bottom border-bottom-lg-0 rounded pt-3 pb-3 pb-lg-0 bg-green">
                <div class="row">
                    <div class="col-md-6 col-lg-6 col-sm-12">
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.usj_no" /></label>
                            <p>
                                <s:if test="model.usj_no.length != ''"><s:property value="model.usj_no" /></s:if><s:else>-</s:else>
                            </p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.app_ref" /></label>
                            <p class="text-danger fw-bold fs-larger"><s:if test="model.case_ref != ''"><s:property value="model.case_ref" /></s:if><s:else>-</s:else></p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.div" /></label>
                            <p><s:if test="model.division_name != ''"><s:property value="model.division_name" /></s:if><s:else>-</s:else></p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.receivedFrom" /></label>
                            <p>
                                <s:if test="model.surveyFirmModel.company_name != 'NA'"><s:property value="model.surveyFirmModel.company_name" /></s:if>
                                <s:elseif test="model.applicationModel.surveyFirmModel.company_name != 'NA'"><s:property value="model.applicationModel.surveyFirmModel.company_name" /></s:elseif>
                                <s:else>-</s:else>
                            </p>
                        </div>
                        <s:if test='model.applicationModel.internal_case.equals("Y") || model.internal_case.equals("Y")'>
                            <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0">Internal Survey Job by L&S**</label>
                            </div>
                        </s:if>
                    </div>
                    <div class="col-md-6 col-lg-6 col-sm-12">
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.dateIssue" /></label>
                            <p>-</p>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.dateApplication" /></label>
                            <p><s:if test="model.case_createddate_str != ''"><s:property value="model.case_createddate_str" /></s:if><s:else>-</s:else></p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-7 border-bottom border-bottom-lg-0 pt-3 pb-3 pb-lg-0 m-auto px-5">
                <h4 class="word-green text-uppercase fw-bold mb-3"><s:text name = "utimaps.form.application.title" /></h4>
                <div class="mb-3">
                    <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.projectName" /></label>
                    <p class="fs-smaller">
                        <s:if test="model.pj_name != ''"><s:property value="model.pj_name" /></s:if>
                        <s:elseif test="model.applicationModel.pj_name != ''"><s:property value="model.applicationModel.pj_name" /></s:elseif>
                        <s:else>-</s:else>
                    </p>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-bold text-dark mb-0"><s:text name = "utimaps.form.application.locality" /></label>
                    <p class="fs-smaller"><s:if test="model.land_desc != ''"><s:property value="model.land_desc" /></s:if><s:else>-</s:else></p>
                </div>

                <div class="mb-3">
                    <p class="word-green"><a href="pdfViewerUSJ?type=viewAppSummary&caseId=<s:property value="model.case_id" />" target="_blank"><i class="fas fa-file"></i> <s:text name = "utimaps.form.button.viewApp" /></a></p>
                    <s:if test="model.applicationModel.getUPS10List().size() > 0">
                        <p class="word-green"><a href="viewTempFileUSJ?fileID=<s:property value = "model.applicationModel.getUPS10List()[0].file_id" />" target="_blank"><i class="fas fa-file"></i> <s:text name = "utimaps.form.button.downloadUPS10" /></a></p>
                    </s:if>
                    <s:elseif test="model.getUPS10List().size() > 0">
                        <p class="word-green"><a href="viewTempFileUSJ?fileID=<s:property value = "model.getUPS10List()[0].file_id" />" target="_blank"><i class="fas fa-file"></i> <s:text name = "utimaps.form.button.downloadUPS10" /></a></p>
                    </s:elseif>
                    <s:else></s:else>
                </div>
            </div>
        </div>
    </div>
</div>