<%-- 
    Document   : checklist_u50
    Created on : Jul 7, 2024, 11:25:54 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>


<h5 class="p-1 border-bottom border-bottom-lg-1 bg-secondary text-white mb-0"><s:text name="utimaps.checklist.u50" /></h5>
<div class="table-responsive scrollbar" >
    <table class="table table-hover table-striped overflow-hidden " id="checklist_table">
        <thead>
            <tr>
                <th scope="col" class="col-md-0 fs-smaller"><s:text name ="utimaps.checklist.label.no" /></th>
                <th scope="col" class="col-md-3 fs-smaller"><s:text name ="utimaps.checklist.label.fileDesc" /></th>
                <th scope="col" class="col-md-2 fs-smaller"><s:text name ="utimaps.checklist.label.docCheck" /></th>
                <th scope="col" class="col-md-5 fs-smaller"><s:text name ="utimaps.checklist.label.processingRemarks" /></th>
                <th scope="col" class="col-md-2 fs-smaller">
                    <span data-bs-toggle="tooltip" data-bs-placement="right" title="<s:text name ="utimaps.checklist.label.supportDocInfo" />">
                        <s:text name ="utimaps.checklist.label.supportDoc" />
                        <i class="fas fa-info-circle h4 mb-0"></i>
                    </span>
                </th>
            </tr>
        </thead>
        <tbody>
            <s:if test="model.u50ChecklistSetupModel.checklistItemList.size() > 0">
                <s:iterator value="model.u50ChecklistSetupModel.checklistItemList" status="listStatus" var="checklistItem">
                    <tr class="<s:if test="#listStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                        <s:hidden name="model.u50ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].cl_id" value="%{#checklistItem.checklistResultList[0].cl_id}" />
                        <s:hidden name="model.u50ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].case_id" value="%{model.job_id}" />
                        <s:hidden name="model.u50ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].check_id" value="%{model.u50ChecklistSetupModel.check_id}" />
                        <s:hidden name="model.u50ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].ci_id" value="%{#checklistItem.ci_id}" />
                        <td colspan="2" class="col-md-3">
                            <div class="d-block">
                                <s:hidden name="model.u50ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].ci_desc" value="%{#checklistItem.ci_desc}" />
                                <h6 class="mb-0 fw-bold"><s:property value="%{#checklistItem.ci_sequence}" />. <s:property value="%{#checklistItem.ci_desc}" /></h6>
                                <p class="mb-0"><small><pre class="ms-3 cd-notes"><s:property value="%{#checklistItem.ci_notes}" /></pre></small></p>
                                <b><small><s:text name ="utimaps.checklist.label.lastCheckedDate" />: <s:property value = "%{#checklistItem.checklistResultList[0].updated_date_str}" /></small></b>
                            </div>
                            <div class="d-flex align-items-center mt-1">
                                <div class="col-md-3">
                                </div>
                                <div class="col-md-9">
                                    <p class="checklist-file text-truncate max-w-150">
                                        <s:hidden name="model.u50ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistFileModelList[0].file_id" value="%{#checklistItem.checklistFileModelList[0].file_id}" />
                                        <a class="color-file-<s:property value="%{#checklistItem.checklistFileModelList[0].file_status}" />" href="viewTempFileAttachment?uploadID=<s:property value="%{#checklistItem.checklistFileModelList[0].file_id}" />" target="_blank">
                                            <s:property value="%{#checklistItem.checklistFileModelList[0].file_name}" />
                                        </a>
                                    </p>
                                    <span class="checklist-file-date text-truncate max-w-150"><s:property value="%{#checklistItem.checklistFileModelList[0].created_date_str}" /></span>
                                    <span class="checklist-file-size mb-3"><s:property value="%{#checklistItem.checklistFileModelList[0].file_size}" /></span>
                                </div>
                            </div>
                        </td>
                        <td class="col-md-2">
                            <div class="form-check">
                                <input class="u50checklistform form-check-input" id="cl_result_c_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.u50ChecklistSetupModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="C" <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("C")}'>Checked</s:if> />
                                <label class="form-check-label" for="cl_result_c_<s:property value='%{#checklistItem.id}' />"><s:text name="utimaps.checklist.label.correct" /></label>
                            </div>
                            <div class="form-check">
                                <input class="u50checklistform form-check-input" id="cl_result_e_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.u50ChecklistSetupModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="E" <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("E")}'>Checked</s:if> />
                                <label class="form-check-label" for="cl_result_e_<s:property value='%{#checklistItem.id}' />"><s:text name="utimaps.checklist.label.incorrect" /></label>
                            </div>
                            <div class="form-check">
                                <input class="u50checklistform form-check-input" id="cl_result_n_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.u50ChecklistSetupModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="N" <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("N")}'>Checked</s:if> />
                                <label class="form-check-label" for="cl_result_n_<s:property value='%{#checklistItem.id}' />"><s:text name="utimaps.checklist.label.notapplicable" /></label>
                            </div>
                        </td>
                        <td class="col-md-5">
                            <s:textarea theme="simple" name="model.u50ChecklistSetupModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].cl_remarks" value="%{#checklistItem.checklistResultList[0].cl_remarks}" cssClass="form-control" rows="5" maxlength="1000"/>
                        </td>

                        <td class="col-md-2">
                            <s:if test="#checklistItem.checklistResultList[0].checklistSupportFileList.size() > 0">
                                <s:iterator value = "#checklistItem.checklistResultList[0].checklistSupportFileList" status="supportFileStatus" var="supportFile">
                                    <s:hidden id="drDocName" name="%{#supportFile.dr_doc_name}" value="%{#supportFile.dr_doc_name}"/>
                                    <s:include value="/base/uppyIncludeSingleFile_utimaps.jsp">
                                        <s:param name="uploadUrl_">uploadInternalAttachment</s:param>
                                        <s:param name="uppyFieldName_">CI<s:property value="%{#checklistItem.ci_id}" /></s:param>
                                        <s:param name="uppyHiddenName_">uppySample2FileId</s:param>
                                        <s:param name="drFileCode_">CM</s:param>
                                        <s:param name="hideArrow">Y</s:param>
                                        <s:param name="uppyFileList" value="testList"/>
                                        <s:param name="uploadParams">drAppCode_=uppySample2&uploadRecordId_=<s:property value="%{model.job_id}"/>&ciId=<s:property value="%{#checklistItem.ci_id}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                                        <s:param name="uploadedFileName"><s:property value="%{#supportFile.original_file_name}"/></s:param>
                                        <s:param name="uploadedExt"><s:property value="%{#supportFile.file_ext}"/></s:param>
                                        <s:param name="uploadedDate"><s:property value="%{#supportFile.created_date_str}"/></s:param>
                                        <s:param name="uploadedFileId"><s:property value="%{#supportFile.file_id}"/></s:param>
                                        <s:param name="allowedFileTypes">'.pdf', '.zip'</s:param>
                                        <s:param name="theRecord_id"><s:property value="%{#supportFile.file_id}"/></s:param>
                                        <s:param name="ci_id"><s:property value="%{#checklistItem.ci_id}"/></s:param>
                                        <s:param name="checklist_type">U50</s:param>
                                    </s:include>
                                </s:iterator>
                            </s:if>
                            <s:else>
                                <s:include value="/base/uppyIncludeSingleFile_utimaps.jsp">
                                    <s:param name="uploadUrl_">uploadInternalAttachment</s:param>
                                    <s:param name="uppyFieldName_">CI<s:property value="%{#checklistItem.ci_id}" /></s:param>
                                    <s:param name="uppyHiddenName_">uppySample2FileId</s:param>
                                    <s:param name="drFileCode_">CM</s:param>
                                    <s:param name="hideArrow">Y</s:param>
                                    <s:param name="uppyFileList" value="testList"/>
                                    <s:param name="uploadParams">drAppCode_=uppySample2&uploadRecordId_=<s:property value="%{model.job_id}"/>&ciId=<s:property value="%{#checklistItem.ci_id}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                                    <s:param name="uploadedFileName"></s:param>
                                    <s:param name="uploadedExt"></s:param>
                                    <s:param name="uploadedDate"></s:param>
                                    <s:param name="uploadedFileId"></s:param>
                                    <s:param name="allowedFileTypes">'.pdf', '.zip'</s:param>
                                    <s:param name="theRecord_id"></s:param>
                                    <s:param name="ci_id"><s:property value="%{#checklistItem.ci_id}"/></s:param>
                                    <s:param name="checklist_type">U50</s:param>
                                </s:include>
                            </s:else>
                        </td>
                    </tr>
                </s:iterator>
            </s:if>
        </tbody>
    </table>
</div>