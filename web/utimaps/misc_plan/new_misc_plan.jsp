<%-- 
    Document   : new_misc_plan
    Created on : May 2, 2025, 2:44:00 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<link href="styles/sub_styles.css" rel="stylesheet" />

<div class="container-fluid">
    <div class="row">
        <div class="accordion accordion-flush" id="accordionFlushApplication">
            <div class="accordion-item">
                <h2 class="accordion-header" id="flush-headingMiscPlan">
                    <button class="accordion-button collapsed fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseMiscPlan" aria-expanded="false" aria-controls="flush-collapseMiscPlan">
                        <i class="fas fa-tasks me-2"></i><s:text name = "utimaps.miscPlan.title" />
                    </button>
                </h2>

                <div id="flush-collapseMiscPlan" class="accordion-collapse collapse show" aria-labelledby="flush-headingMiscPlan" data-bs-parent="#accordionMiscPlan">
                    <div class="accordion-body">
                        <form action="" method="post" id="newMiscPlan">
                            <s:hidden name="id" id="id" value="%{model.id}"/>
                            <s:hidden name="misc_plan_id" id="misc_plan_id" value="%{model.misc_plan_id}"/>
                            <s:hidden name="actionName" id="actionName" value="%{actionName}"/>
                            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
                            <s:hidden name="userDiv_" value="%{userDiv_}"/>
                            <s:hidden name="created_div" value="%{model.created_div}"/>
                            <s:hidden name="survey_job_no" value="%{model.survey_job_no}"/>

                            <div class="row">
                                <div class="col-md-12 text-end mb-3">
                                    <button class="btn btn-secondary" type="submit" name="action:cancelMiscPlan" id="cancelMiscPlan"><i class="fa fa-times"></i> <s:text name="button.cancel" /></button>
                                    <s:if test='%{!userDiv_.equals("00")}'>
                                        <button class="btn btn-primary" id="saveNewMiscPlan" type="submit"><i class="fas fa-save"></i> <s:text name = "utimaps.form.button.save" /></button>
                                    </s:if>
                                </div>

                                <div class="col-md-9 col-xs-12 mb-1">
                                    <div class="row">
                                        <s:if test='pageSubTitle_.equals("Add")'>
                                            <label class="col-md-3 col-form-label fs-smaller" for="division"><s:text name="utimaps.form.miscPlan.division" /><font class="asterisk">*</font></label>
                                            <div class="col-md-8">
                                                <s:if test='isHQUser'>
                                                    <s:select 
                                                        name="model.selectedDivision"
                                                        value="userDiv_"
                                                        list="_DivisionList"
                                                        listKey="keyData"
                                                        listValue="valueData"                    
                                                        cssClass="form-select"
                                                     />
                                                </s:if>
                                                <s:else>
                                                    <s:hidden class="form-control" name="model.selectedDivision" value="%{userDiv_}" readonly="true" />
                                                    <s:textfield class="form-control" name="userStrDiv_" value="%{getStrTrnDiv(userDiv_)}" readonly="true" />
                                                </s:else>
                                                <div class="mb-3 row"></div>
                                            </div>
                                        </s:if>

                                        <label class="col-md-3 col-form-label fs-smaller" for="plan_no"><s:text name="utimaps.form.miscPlan.planNo" /></label>
                                        <div class="col-md-8 text-danger fw-bold">
                                            <s:hidden name="model.plan_no" value="%{model.plan_no}"/>
                                            <s:property value="%{model.plan_no}"/>
                                            <div class="mb-3 row"></div>
                                        </div>

                                        <label class="col-md-3 col-form-label fs-smaller">
                                            <label class="col-form-label ">&nbsp;</label><br />
                                            <s:text name="utimaps.form.miscPlan.sjNo" />
                                        </label>
                                        <div class="col-md-8">
                                            <div class="row g-3 align-items-center">
                                                <div class="col-auto">
                                                    <label class="col-form-label ">&nbsp;</label><br />
                                                    <label class="col-form-label"><s:text name="utimaps.form.issuance.usj" /> / </label>
                                                </div>
                                                <div class="col-2 text-center">
                                                    <label class="col-form-label "><small><s:text name="utimaps.form.miscPlan.div" /></small></label>
                                                    <s:textfield class="form-control" name = "model.usj_div" value = "%{model.usj_div}" maxlength="4" data-inputmask="'mask': '9999'" readonly="%{noRightUpdate}" required ="required" />
                                                </div>
                                                <div class="col-auto">
                                                    <label class="col-form-label ">&nbsp;</label><br />
                                                    <label class="col-form-label"> / </label>
                                                </div>
                                                <div class="col-3 text-center">
                                                    <label class="col-form-label "><small><s:text name="utimaps.form.miscPlan.seq" /></small></label>
                                                    <s:textfield class="form-control" name = "model.usj_seq" value = "%{model.usj_seq}" maxlength="4" data-inputmask="'mask': '9999'" readonly="%{noRightUpdate}" required ="required" />
                                                </div>
                                                <div class="col-auto">
                                                    <label class="col-form-label ">&nbsp;</label><br />
                                                    <label class="col-form-label"> / </label>
                                                </div>
                                                <div class="col-auto text-center">
                                                    <label class="col-form-label "><small><s:text name="utimaps.form.miscPlan.year" /></small></label>
                                                    <s:select 
                                                        name="model.usj_year"
                                                        value="%{model.usj_year}"
                                                        list="USJYearList"
                                                        listKey="keyData"
                                                        listValue="valueData"                    
                                                        cssClass="form-select"
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

                                        <label class="col-md-3 col-form-label fs-smaller" for="model_plan_title"><s:text name="utimaps.form.miscPlan.planTitle" /></label>
                                        <div class="col-md-8">
                                            <s:textarea theme="simple" name="model.plan_title" value="%{model.plan_title}" cssClass="form-control" rows="6" maxlength="256" />
                                            <div class="mb-3 row"></div>
                                        </div>

                                        <label class="col-md-3 col-form-label fs-smaller" for="model_file_ref"><s:text name="utimaps.form.miscPlan.fileRef" /></label>
                                        <div class="col-md-8">
                                            <s:textfield cssClass="form-control" name="model.file_ref" value="%{model.file_ref}" maxlength="100" />
                                            <div class="mb-3 row"></div>
                                        </div>
                                        <label class="col-md-3 col-form-label fs-smaller" for="model_field_book"><s:text name="utimaps.form.miscPlan.fieldBook" /></label>
                                        <div class="col-md-8">
                                            <s:textfield cssClass="form-control" name="model.field_book" value="%{model.field_book}" maxlength="100" />
                                            <div class="mb-3 row"></div>
                                        </div>

                                        <label class="col-md-3 col-form-label fs-smaller" for="model_sheet_ref"><s:text name="utimaps.form.miscPlan.sheetRef" /></label>
                                        <div class="col-md-8">
                                            <s:textfield cssClass="form-control" name="model.sheet_ref" value="%{model.sheet_ref}" maxlength="100" />
                                            <div class="mb-3 row"></div>
                                        </div>
                                            
                                        <s:if test='pageSubTitle_.equals("Edit")'>
                                            <label class="col-md-3 col-form-label fs-smaller" for="model_sheet_ref"><s:text name="utimaps.form.miscPlan.atachment" /></label>
                                            <div class="col-md-8">
                                                <ol>
                                                <s:iterator value = "model.attachmentList" status="supportFileStatus" var="supportFile">
                                                   <li>
                                                   <s:include value="/base/uppyIncludeSingleFile_mp.jsp">
                                                        <s:param name="uploadUrl_">uploadInternalAttachment</s:param>
                                                        <s:param name="uppyFieldName_">a<s:property value="%{#supportFile.file_id}" /></s:param>
                                                        <s:param name="uppyHiddenName_">uppySample2FileId</s:param>
                                                        <s:param name="drFileCode_">MP</s:param>
                                                        <s:param name="hideArrow">Y</s:param>
                                                        <s:param name="uppyFileList" value="testList"/>
                                                        <s:param name="uploadParams">drFileCode_=MP&drAppCode_=MP&uploadRecordId_=<s:property value="%{model.ID}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                                                        <s:param name="uploadedFileName"><s:property value="%{#supportFile.original_file_name}"/></s:param>
                                                        <s:param name="uploadedExt"><s:property value="%{#supportFile.file_ext}"/></s:param>
                                                        <s:param name="uploadedDate"><s:property value="%{#supportFile.created_date_str}"/></s:param>
                                                        <s:param name="uploadedFileId"><s:property value="%{#supportFile.file_id}"/></s:param>
                                                        <s:param name="allowedFileTypes">'.pdf', '.zip'</s:param>
                                                        <s:param name="theRecord_id"><s:property value="%{#supportFile.file_id}"/></s:param>
                                                    </s:include>
                                                   </li>
                                                </s:iterator>
                                                </ol> 

                                                <div class="mb-3 row">
                                                    <s:include value="/base/uppyIncludeMultipleFile_mp.jsp">
                                                        <s:param name="uploadUrl_">uploadInternalAttachment</s:param>
                                                        <s:param name="uppyFieldName_">multi1</s:param>
                                                        <s:param name="uppyHiddenName_">multipleFileDrDocIds</s:param>
                                                        <s:param name="hideArrow">Y</s:param>
                                                        <s:param name="drFileCode_">MP</s:param>
                                                        <s:param name="maxNumberOfFiles">3</s:param>
                                                        <s:param name="uploadParams">drFileCode_=MP&drAppCode_=MP&uploadRecordId_=<s:property value="%{model.ID}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                                                        <s:param name="uppyFileListName">childList</s:param>
                                                        <s:param name="uploadedFileName"><s:property value="uploadedFile_fileName"/></s:param>
                                                        <s:param name="uploadedFileId"><s:property value="uploadedFile_fileId"/></s:param>
                                                        <s:param name="theRecordId"><s:property value="yourParentModel.ID"/></s:param>
                                                        <s:param name="allowedFileTypes">'image/*','application/pdf'</s:param>
                                                        <s:param name="allowedFileTypesDesc"><s:text name="imagesAndPDF"/></s:param>
                                                        <%--<s:param name="delBtnMarginTop">6px</s:param>--%>
                                                    </s:include>
                                                </div>
                                            </div>
                                        </s:if>
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
    $('#saveNewMiscPlan').click(function (e) {
        e.preventDefault();
        var displayMessage = "<s:text name="utimaps.form.message.save"/>";
        
        var miscPlanID = $("#misc_plan_id").val();
        
        console.log($("#model_plan_title").val())
        
        if (miscPlanID === "") {
            confirmationBox(displayMessage, "#newMiscPlan", "processInsertMiscPlan");
        } else {
            confirmationBox(displayMessage, "#newMiscPlan", "processUpdateMiscPlan");
        }
        
    });
</script>