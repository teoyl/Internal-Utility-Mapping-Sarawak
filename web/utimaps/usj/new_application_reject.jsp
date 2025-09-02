<%-- 
    Document   : new_application_verify
    Created on : Apr 18, 2024, 4:55:14 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>
    
<style nonce="EuTVqS192VKl">
    .floating-label {
        margin-left: 5px;
    }

    .circle-div {
        height: 50px;
        width: 50px;
        border-radius: 50%;
        border: 2px solid #3d80cc;
        justify-content: center;
        display: flex;
        align-items: center;
        color: #3d80cc;
    }

    .checklist-file {
        margin: 0;
        color: blue;
    }

    .checklist-file-date, .checklist-file-size {
        display: block;
        color: grey;
        font-size: smaller;
    }

    .checklist-supporting {
        display: block;
        font-size: smaller;
    }
    .card {
        border: 1px solid #d1d1d1;
    }
    .bg-green {
        background-color: var(--bg-green);
    }
    .word-green {
        color: #009999;
    }
    .fs-smaller {
        font-size: smaller;
    }
    .fs-larger {
        font-size: larger;
    }
    .btn-search {
        height: 98%;
    }
    .btn-search:first-child:active, .btn-search:hover{
        color: #748194 !important;
    }
    .tox-tinymce {
        border: 1px solid #d1d1d1 !important;
    }

    /* uppy override custom */
    .uppy-DragDrop-arrow {
        width: 20px !important;
        height: 20px !important;
        margin-bottom: 0 !important;
    }

    button .uppy-Root,
   .uppy-u-reset,
   .uppy-DragDrop-container,
   .uppy-DragDrop--isDragDropSupported,
   .uppy-DragDrop-inner {
       min-height: 3rem !important;
       max-height: 100% !important;
   }

   .uppy-DragDrop-container,
   .uppy-DragDrop--isDragDropSupported,
    button .no-upload
   {
       background-color: #009999 !important;
       border: none !important;
       margin-bottom: 0.3em !important;
   }

   .uppy-DragDrop--uploaded {
       background-color: #F2F2F2 !important;
       border: none !important;
       margin-bottom: 0.3em !important;

       display: flex !important;
       flex-direction: row !important;
       justify-content: center !important;
       align-items: center !important;

       min-height: 3rem !important;
       max-height: 100% !important;

       font-size: 1.15em;
   }

    button .uppy-DragDrop-inner {
       padding: 0 !important;
       font-size: 12px !important;
       color: white !important;
       font-weight: bold;

       display: flex;
       flex: row;
       align-items: center;
   }

    button a .uppy-DragDrop-inner  {
       color: white !important;
       font-weight: bold;
    }

    .uppy-c-icon {
       margin-right: 0.5rem;
    }

    .svg-upload-icon {
       width: 15px !important;
       height: 15px  !important;
       margin-right: 5px !important;
   }
</style>
                                         

<div class="container-fluid">
    <jsp:include page="internal_swiper.jsp"></jsp:include>


    <div class="card mb-3 border-secondary">
        <div class="card-body p-0">
            <div class="row m-0">
                <div class="col-lg-5 border-end-lg border-4 border-success border-bottom border-bottom-lg-0 rounded pt-3 pb-3 pb-lg-0 bg-green">
                    <div class="row">
                        <div class="col-md-6 col-lg-6 col-sm-12">
                            <div class="mb-3">
                                <label class="form-label fw-bold text-dark mb-0">USJ No.</label>
                                <p>
                                    <s:if test="model.usj_no.length != ''"><s:property value="model.usj_no" /></s:if><s:else>-</s:else>
                                </p>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold text-dark mb-0">Application Ref.</label>
                                <p class="text-danger fw-bold fs-larger"><s:if test="model.case_ref != ''"><s:property value="model.case_ref" /></s:if><s:else>-</s:else></p>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold text-dark mb-0">Division</label>
                                <p><s:if test="model.division_name != ''"><s:property value="model.division_name" /></s:if><s:else>-</s:else></p>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold text-dark mb-0">Received From</label>
                                <p><s:if test="model.surveyFirmModel.company_name != ''"><s:property value="model.surveyFirmModel.company_name" /></s:if><s:else>-</s:else></p>
                            </div>
                        </div>
                        <div class="col-md-6 col-lg-6 col-sm-12">
                            <div class="mb-3">
                                <label class="form-label fw-bold text-dark mb-0">Date of Issue</label>
                                <p><s:if test="model.case_ref != ''"><s:property value="model.case_ref" /></s:if><s:else>-</s:else></p>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold text-dark mb-0">Date of Application</label>
                                <p><s:if test="model.case_createddate_str != ''"><s:property value="model.case_createddate_str" /></s:if><s:else>-</s:else></p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-7 border-bottom border-bottom-lg-0 pt-3 pb-3 pb-lg-0 m-auto px-5">
                    <h4 class="word-green text-uppercase fw-bold mb-3">Application for Utility Survey Job</h4>
                    <div class="mb-3">
                        <label class="form-label fw-bold text-dark mb-0">Name of Utility Project</label>
                        <p class="fs-smaller"><s:if test="model.pj_name != ''"><s:property value="model.pj_name" /></s:if><s:else>-</s:else></p>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold text-dark mb-0">Locality</label>
                        <p class="fs-smaller"><s:if test="model.land_desc != ''"><s:property value="model.land_desc" /></s:if><s:else>-</s:else></p>
                    </div>

                    <p class="word-green"><i class="fas fa-file"></i> View Application</p>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="accordion accordion-flush" id="accordionFlushApplication">
            <div class="accordion-item">
                <h2 class="accordion-header" id="flush-headingOne">
                    <button class="accordion-button collapsed fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseOne" aria-expanded="false" aria-controls="flush-collapseOne">
                        <i class="fas fa-tasks me-2"></i>Submission Checklist

                        <span class="position-absolute end-50px text-danger">[Reject]</span>
                        
                    </button>
                </h2>
            
                <div id="flush-collapseOne" class="accordion-collapse collapse show" aria-labelledby="flush-headingOne" data-bs-parent="#accordionFlushApplication">
                    <div class="accordion-body">
                        <form action="" method="post" id="ApplicationUSJForm">
                            <s:hidden name="case_id" id="case_id" value="%{model.case_id}"/>
                            <s:hidden name="taskId_" value="%{taskId_}"/>
                            <s:hidden name="wfActivityCode" value="%{wfActivityCode}"/> 
                            <s:hidden name="model.wf_status" id="model.wf_status" value="%{model.wf_status}"/> 
                            <s:hidden name="checklist_id" id="checklist_id" value="%{model.checklistSetupCaseModel.checklist_id}"/> 
                            <s:hidden name="model.checklistModel.check_id" id="model.checklistModel.check_id" value="%{model.checklistModel.check_id}"/> 
                            <s:hidden name="model.checklistModel.case_id" id="model.checklistModel.case_id" value="%{model.case_id}"/> 
                            <s:hidden name="model.checklistModel.check_type" id="model.checklistModel.case_id" value="APP01"/> 

                        <div class="row">
                            <div class="col-md-12 text-end mb-3">
                                <button class="btn btn-primary" type="button">Print Checklist</button>
                                <button class="btn btn-primary" id="rejectApplicationUSJ" type="submit"><i class="fas fa-check"></i> Complete</button>
                                <s:if test="rightToUpdate">
                                    <button class="btn btn-primary" id="saveEditApplicationUSJ" type="submit"><i class="fas fa-save"></i> Save</button>
                                </s:if>
                            </div>

                            <div class="col-md-12 mb-3">
                                <div class="card mb-3">
                                    <div class="card-body">
                                        <h6 class="mb-0"><label class="form-label">Decision</label></h6>
                                        <div class="form-check form-check-inline mb-0">
                                            <input class="form-check-input" type="radio" name="checklistModel.check_status" id="checkStatus_A" value="A" <s:if test='model.checklistModel.check_status.equals("A")'>checked</s:if> />
                                            <label class="form-check-label" for="checkStatus_A">ACCEPT</label>
                                        </div>
                                        <div class="form-check form-check-inline mb-0">
                                            <input class="form-check-input" type="radio" name="checklistModel.check_status" id="checkStatus_R" value="R" <s:if test='model.checklistModel.check_status.equals("R")'>checked</s:if> />
                                            <label class="form-check-label" for="checkStatus_R">REJECT</label>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-12 mb-3">
                                <h6><label class="form-label">Comments on Submission</label></h6>
                                <div class="mb-3 ">
                                    <s:textarea theme="simple" name="model.checklistModel.check_comment_oic" value="%{model.checklistModel.check_comment_oic}" cssClass="form-control" rows="10" />
                                </div>
                                <div class="row">
                                    <div class="form-floating mb-3 col-md-6">
                                        <input class="form-control" id="floatingCheckedBy" name="checklistModel.check_by_oic" type="text" value="<s:property value='checker_name'/>" readonly/>
                                        <label class="floating-label" for="floatingCheckedBy">Checked By</label>
                                    </div>
                                    <div class="form-floating mb-3 col-md-6">
                                        <input class="form-control" id="floatingDateChecking" name="checklistModel.check_date_oic_str" type="text" value="<s:property value='model.checklistModel.check_date_oic_str'/>" readonly/>
                                        <label class="floating-label" for="floatingDateChecking">Date of Checking</label>
                                    </div>
                                </div>
                            </div>

                             <div class="col-md-12">
                                 <h6 class="pb-1 border-bottom border-bottom-lg-1 text-uppercase fw-bold fs-1" data-anchor="data-anchor">Document Submitted</h6>
                                 <div class="table-responsive scrollbar">
                                     <table class="table table-hover table-striped overflow-hidden " id="checklist_table">
                                         <thead>
                                             <tr>
                                                 <th scope="col" class="col-md-0 fs-smaller">No.</th>
                                                 <th scope="col" class="col-md-3 fs-smaller">File Description</th>
                                                 <th scope="col" class="col-md-2 fs-smaller">Document Checking</th>
                                                 <th scope="col" class="col-md-3 fs-smaller">Processing Remarks</th>
                                                 <th scope="col" class="col-md-3 fs-smaller">Supporting Documents</th>
                                             </tr>
                                         </thead>
                                         <tbody>
                                             <s:if test="model.checklistSetupCaseModel.checklistItemList.size() > 0">
                                             <s:iterator value="model.checklistSetupCaseModel.checklistItemList" status="listStatus" var="checklistItem">
                                                <tr class="<s:if test="#listStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                                    <s:hidden name="model.checklistSetupCaseModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].cl_id" value="%{#checklistItem.checklistResultList[0].cl_id}" />
                                                    <s:hidden name="model.checklistSetupCaseModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].case_id" value="%{model.case_id}" />
                                                    <s:hidden name="model.checklistSetupCaseModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].check_id" value="%{model.checklistModel.check_id}" />
                                                    <s:hidden name="model.checklistSetupCaseModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].ci_id" value="%{#checklistItem.ci_id}" />
                                                    <td class="" colspan="2" class="col-md-4">
                                                        <div class="d-block">
                                                            <h6 class="mb-0 fw-bold"><s:property value="%{#checklistItem.ci_sequence}" />. <s:property value="%{#checklistItem.ci_desc}" /></h6>
                                                            <p class="mb-0"><small><s:property value="%{#checklistItem.ci_notes}" /></small></p>
                                                        </div>
                                                        <div class="d-flex align-items-center mt-1">
                                                            <div class="col-md-3">
                                                                <div class="ms-2 circle-div text-uppercase">
                                                                    <s:if test="#checklistItem.checklistFileModelList[0].file_ext != ''"><s:property value="%{#checklistItem.checklistFileModelList[0].file_ext}" /></s:if><s:else>-</s:else>
                                                                </div>
                                                            </div>
                                                            <div class="col-md-9">
                                                                <s:iterator value = "#checklistItem.checklistFileModelList" status="fileStatus" var="checklistFile">
                                                                    <p class="checklist-file">
                                                                        <a href="viewTempFileAttachment?uploadID=<s:property value="%{#checklistFile.file_id}" />" target="_blank">
                                                                            <s:property value="%{#checklistFile.file_name}" />
                                                                        </a>
                                                                    </p>
                                                                    <span class="checklist-file-date"><s:property value="%{#checklistFile.created_date_str}" /></span>
                                                                    <span class="checklist-file-size mb-3"><s:property value="%{#checklistFile.file_size}" /></span>
                                                                </s:iterator>
                                                                <%--<p class="checklist-file">
                                                                    <s:property value="%{#checklistItem.checklistResultList[0].ciFileModel.file_name}" />
                                                                </p>
                                                                <span class="checklist-file-date"><s:property value="%{#checklistItem.checklistResultList[0].ciFileModel.created_date_str}" /></span>
                                                                <span class="checklist-file-size mb-3"><s:property value="%{#checklistItem.checklistResultList[0].ciFileModel.file_size}" /></span>
                                                                <span class="checklist-supporting">Supporting Attachment(s):</span>
                                                                <span class="">
                                                                    <s:iterator value = "#checklistItem.checklistResultList[0].checklistSupportFileList" status="supportFileStatus" var="supportFile">
                                                                        <p class="mb-0"><a href="viewTempFileAttachment?uploadID=<s:property value="%{#supportFile.file_id}" />" target="_blank"><s:property value="%{#supportFile.original_file_name}" /></a></p>
                                                                    </s:iterator>
                                                                </span>
                                                                --%>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td class="" class="col-md-2">
                                                        <div class="form-check">
                                                            <input class="form-check-input" id="cl_result_c_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.checklistSetupCaseModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="C" <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("C")}'>Checked</s:if> />
                                                            <label class="form-check-label" for="cl_result_c_<s:property value='%{#checklistItem.id}' />">Complete/Correct</label>
                                                        </div>
                                                        <div class="form-check">
                                                            <input class="form-check-input" id="cl_result_i_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.checklistSetupCaseModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="I" <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("I")}'>Checked</s:if> />
                                                            <label class="form-check-label" for="cl_result_i_<s:property value='%{#checklistItem.id}' />">Incomplete/Incorrect</label>
                                                        </div>
                                                        <div class="form-check">
                                                            <input class="form-check-input" id="cl_result_n_<s:property value='%{#checklistItem.id}' />" type="radio" name="model.checklistSetupCaseModel.checklistItemList[<s:property value='%{#listStatus.index}' />].checklistResultList[0].cl_result" value="N" <s:if test='%{#checklistItem.checklistResultList[0].cl_result.equals("N")}'>Checked</s:if> />
                                                            <label class="form-check-label" for="cl_result_n_<s:property value='%{#checklistItem.id}' />">Not Applicable</label>
                                                        </div>
                                                    </td>
                                                    <td class="" class="col-md-3">
                                                        <%--<s:textarea theme="simple" name="model.checklistSetupCaseModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].cl_remarks" value="%{#checklistItem.checklistResultList[0].cl_remarks}" cssClass="form-control" rows="5" maxlength="1000"/>--%>
                                                        <s:textarea theme="simple" name="model.checklistSetupCaseModel.checklistItemList[%{#listStatus.index}].checklistResultList[0].cl_remarks" value="%{#checklistItem.checklistResultList[0].cl_remarks}" cssClass="form-control" rows="5" maxlength="1000"/>
                                                    </td>
                                                    
                                                    <td class="" class="col-md-2">
                                                        <s:if test="#checklistItem.checklistResultList[0].checklistSupportFileList.size() > 0">
                                                            <s:iterator value = "#checklistItem.checklistResultList[0].checklistSupportFileList" status="supportFileStatus" var="supportFile">
                                                                <!--<p class="mb-0"><a href="viewTempFileAttachment?uploadID=<s:property value="%{#supportFile.file_id}" />" target="_blank"><s:property value="%{#supportFile.original_file_name}" /></a></p>-->
                                                                <s:hidden id="drDocName" name="%{#supportFile.dr_doc_name}" value="%{#supportFile.dr_doc_name}"/>
                                                                <s:include value="/base/uppyIncludeSingleFile_utimaps.jsp">
                                                                    <s:param name="uploadUrl_">uploadInternalAttachment</s:param>
                                                                    <s:param name="uppyFieldName_"><s:property value="%{#checklistItem.ci_datatype}" /></s:param>
                                                                    <s:param name="uppyHiddenName_">uppySample2FileId</s:param>
                                                                    <s:param name="drFileCode_">CM</s:param>
                                                                    <s:param name="hideArrow">Y</s:param>
                                                                    <s:param name="uppyFileList" value="testList"/>
                                                                    <s:param name="uploadParams">drAppCode_=uppySample2&uploadRecordId_=<s:property value="%{model.case_id}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                                                                    <s:param name="uploadedFileName"><s:property value="%{#supportFile.original_file_name}"/></s:param>
                                                                    <s:param name="uploadedExt"><s:property value="%{#supportFile.file_ext}"/></s:param>
                                                                    <s:param name="uploadedDate"><s:property value="%{#supportFile.created_date_str}"/></s:param>
                                                                    <s:param name="uploadedFileId"><s:property value="%{#supportFile.file_id}"/></s:param>
                                                                    <s:param name="allowedFileTypes">'.pdf', '.zip'</s:param>
                                                                    <s:param name="theRecord_id"><s:property value="%{#supportFile.file_id}"/></s:param>
                                                                    <%--<s:param name="delBtnMarginTop">6px</s:param>--%>
                                                                </s:include>
                                                            </s:iterator>
                                                        </s:if>
                                                        <s:else>
                                                            <s:include value="/base/uppyIncludeSingleFile_utimaps.jsp">
                                                                <s:param name="uploadUrl_">uploadInternalAttachment</s:param>
                                                                <s:param name="uppyFieldName_"><s:property value="%{#checklistItem.ci_datatype}" /></s:param>
                                                                <s:param name="uppyHiddenName_">uppySample2FileId</s:param>
                                                                <s:param name="drFileCode_">CM</s:param>
                                                                <s:param name="hideArrow">Y</s:param>
                                                                <s:param name="uppyFileList" value="testList"/>
                                                                <s:param name="uploadParams">drAppCode_=uppySample2&uploadRecordId_=<s:property value="%{model.case_id}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                                                                <s:param name="uploadedFileName"></s:param>
                                                                <s:param name="uploadedExt"></s:param>
                                                                <s:param name="uploadedDate"></s:param>
                                                                <s:param name="uploadedFileId"></s:param>
                                                                <s:param name="allowedFileTypes">'.pdf', '.zip'</s:param>
                                                                <s:param name="theRecord_id"></s:param>
                                                                <%--<s:param name="delBtnMarginTop">6px</s:param>--%>
                                                            </s:include>
                                                        </s:else>
                                                    </td>
                                                </tr>
                                            </s:iterator>
                                           </s:if>
                                         </tbody>
                                     </table>
                                 </div>
                            </div>
                        </div>
                        </form>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
                                                
<script nonce="r4DjhKbfO5ry">
    $( document ).ready(function() {
        CKEDITOR.replace('model.checklistModel.check_comment_oic', {
            contentsCss: "body {font-size: 15px;font-family: 'Times New Roman', Times, serif;}",
            extraPlugins: 'wordcount',
            wordcount: {
                countSpacesAsChars : true,
                showCharCount: true,
                showWordCount: false,
                showParagraphs: false,
//              maxCharCount: 100
                maxCharCount: 4000
            }
             });
    });

    $('#saveEditApplicationUSJ').click( function (e) {
        e.preventDefault();
        var string = '';

        bootbox.confirm("Are you sure want to save?", function(result) {
            if(result) {
                var form = $('#ApplicationUSJForm');
                form.attr("action","processUpdateApplicationUSJ");
                form.submit();
            }
        });

    });
    
    $('#rejectApplicationUSJ').click( function (e) {
        e.preventDefault();
        var string = '';

        bootbox.confirm("Are you sure want to complete?", function(result) {
            if(result) {
                var form = $('#ApplicationUSJForm');
                form.attr("action","processRejectApplicationUSJ");
                form.submit();
            }
        });

    });

</script>

