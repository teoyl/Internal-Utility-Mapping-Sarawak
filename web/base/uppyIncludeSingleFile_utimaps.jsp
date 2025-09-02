<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<style nonce="EuTVqS192VKl">
    .uppy-StatusBar-actionBtn--retry {
        display: none !important;
    }
</style>
    
<!-- Target DOM node #1 -->
<div class="${param.uppyFieldName_}for-DragDrop">
    <div id="${param.uppyFieldName_}DragDrop-fileDiv" class="d-flex hidden"></div>
</div>
<div class="${param.uppyFieldName_}for-ProgressBar"></div>
<div id="${param.uppyFieldName_}drag-drop-area"></div>
<div id="${param.uppyFieldName_}divDeleteTempFile" class="hidden"></div>
<script nonce="r4DjhKbfO5ry">
    <s:set var="drFileCode_">${param.drFileCode_}</s:set>
    <%--<s:set var="hideArrow">${param.hideArrow}</s:set>--%>
    <s:set var="showDeleteBtn">${param.showDeleteBtn}</s:set>
    <s:set var="showDeleteBtn2">${param.showDeleteBtn}@</s:set>
    <s:set var="uploadUrl_">${param.uploadUrl_}</s:set>
    <s:set var="uploadUrl_2">${param.uploadUrl_}@</s:set>
    <s:set var="checkFileFn">${param.checkFileFn}</s:set>
    <s:set var="checkFileFn2">${param.checkFileFn}@</s:set>
    <s:set var="delBtnMarginTop">${param.delBtnMarginTop}</s:set>
    <s:set var="delBtnMarginTop2">${delBtnMarginTop}@</s:set>
    <s:set var="uploadedFileName">${param.uploadedFileName}</s:set>
    <s:set var="uploadedFileName2">${param.uploadedFileName}@</s:set>
    <s:set var="uploadedFileId">${param.uploadedFileId}</s:set>
    <s:set var="ci_id">${param.ci_id}</s:set>
    <s:set var="theRecord_id">${param.theRecord_id}</s:set>
    <s:set var="theRecord_id2">${param.theRecord_id}@</s:set> 
    <s:set var="allowedFileTypes">${param.allowedFileTypes}</s:set>
    <s:set var="allowedFileTypes2">${param.allowedFileTypes}@</s:set>
    <s:if test='#allowedFileTypes2.equals("@")'>
        <s:set var="allowedFileTypes"></s:set>
    </s:if>
    <s:set var="updateHiddenId">${param.updateHiddenId}</s:set>
    <s:set var="updateHiddenId2">${param.updateHiddenId}@</s:set>
    <s:if test='#uploadUrl_2.equals("@")'>
        <s:set var="uploadUrl_">uploadUppy</s:set>
    </s:if>
    <s:if test='#checkFileFn2.equals("@")'>
        <s:set var="checkFileFn"></s:set>
    </s:if>
    <s:if test='#updateHiddenId2.equals("@")'>
        <s:set var="updateHiddenId"></s:set>
    </s:if>
    <s:if test='#showDeleteBtn2.equals("@")'>
        <s:set var="showDeleteBtn">Y</s:set>
    </s:if>
    <s:set var="updateHiddenName">${param.updateHiddenName}</s:set>
    <s:set var="updateHiddenName2">${param.updateHiddenName}@</s:set>
    <s:if test='#updateHiddenName2.equals("@")'>
        <s:set var="updateHiddenName"></s:set>
    </s:if>
    <s:set var="displayAsThumbnail">${param.displayAsThumbnail}</s:set>
    <s:set var="displayAsThumbnail2">${param.displayAsThumbnail}@</s:set>
    
        
    <s:set var="allowedFileTypesDesc">${param.allowedFileTypesDesc}</s:set>
    <s:set var="allowedFileTypesDesc2">${param.allowedFileTypesDesc}@</s:set>
    <s:if test='#allowedFileTypesDesc2.equals("@")'>
        <s:set var="allowedFileTypesDesc"></s:set>
    </s:if>
        
    <s:if test='#theRecord_id2.equals("@")'>
        <s:set var="theRecord_id"></s:set>
    </s:if> 
        
    <s:if test='#uploadedFileName2.equals("@")'>
        <s:set var="uploadedFileName" value=""/>
        <s:set var="uploadedFileId"></s:set>
    </s:if>
    <s:if test='#delBtnMarginTop2.contains("@")'>
        <s:set var="delBtnMarginTop" value='"6px"'/>
    </s:if>

    <s:set var="checklist_type">${param.checklist_type}</s:set>
        
    var isUploading = false;
    var theRecord_id = '${theRecord_id}';
    function removeMe${param.uppyFieldName_}(fileDiv, tempFileId, uploadId) {
//        var posting = $.post("removeTempFileAttachment?drFileCode_=${drFileCode_}&uploadID=" + tempFileId + "&uploadRecordId_=" + "${theRecord_id}");
        var posting = $.post("removeSupportingFileAttachment?drFileCode_=${drFileCode_}&uploadID=" + tempFileId + "&uploadRecordId_=" + "${theRecord_id}");
        console.log(posting)
        
        posting.fail(function (jqXHR) {
            if (jqXHR.status === 404) {
                alert(error404 + '\n' + error404_msg1 + '\n' + error404_msg2);
            } else {
                alert("Unexpected error occurred");
            }
            uppyUploadingCount--;
            isUploading = false;
        });
        
        posting.done(function (data) {
            $("#${param.uppyFieldName_}" + fileDiv).html("");
            $("#${param.uppyFieldName_}" + fileDiv).addClass("hidden");
            $(".${param.uppyFieldName_}for-DragDrop .uppy-Root").removeClass("hidden");
            uppy_${param.uppyFieldName_}.removeFile(uploadId);
            <s:if test='( !(#updateHiddenId == null || #updateHiddenId.equals("")) )'>
                $("#${updateHiddenId}").val("");
            </s:if>
            <s:if test='( !(#updateHiddenName == null || #updateHiddenName.equals("")) )'>
                $("#${updateHiddenName}").val("");
            </s:if>
        });
    }
    var onUploadSuccess${param.uppyFieldName_} = (elForUploadedFiles) =>
        (file, response) => {
            if (response.body.status === 'success') {
                const uploadID = response.body.fileId;
                const fileName = file.name;
                <s:if test='( !(#updateHiddenId == null || #updateHiddenId.equals("")) )'>
                    $("#${updateHiddenId}").val(uploadID);
                </s:if>
                <s:if test='( !(#updateHiddenName == null || #updateHiddenName.equals("")) )'>
                    $("#${updateHiddenName}").val(fileName);
                </s:if>
                $(".${param.uppyFieldName_}for-DragDrop .uppy-Root").addClass("hidden");
                <s:if test='(#displayAsThumbnail.equals("Y"))'>
                    $("#${param.uppyFieldName_}DragDrop-fileDiv").html('<button class="d-inline-block" onclick="removeMe${param.uppyFieldName_}(' + "'DragDrop-fileDiv" + "','" + uploadID + "', '" + file.id + "'" + '); return false;"><i class="fa-ns fa fa-trash"></i></button>&nbsp;<a class="d-inline-block text-truncate max-w-150" href="viewTempFileAttachment?uploadID=' + uploadID + '" target="_blank">' + fileName + '</a><img src="viewTempFileAttachment?tn_=Y&uploadID='+uploadID+'"/>');
                </s:if><s:else>
                    $("#${param.uppyFieldName_}DragDrop-fileDiv").html('<button class="d-inline-block" onclick="removeMe${param.uppyFieldName_}(' + "'DragDrop-fileDiv" + "','" + uploadID + "', '" + file.id + "'" + '); return false;"><i class="fa-ns fa fa-trash"></i></button>&nbsp;<a class="d-inline-block text-truncate max-w-150" href="viewTempFileAttachment?uploadID=' + uploadID + '" target="_blank">' + fileName + '</a>');                        
                </s:else>
                    
                $("#${param.uppyFieldName_}DragDrop-fileDiv").removeClass("hidden");
            } else {
                uppy_${param.uppyFieldName_}.removeFile(file.id);
                bootbox.alert({
                    closeButton: false,
                    message: response.body.errMsg
                });
            }
            
            $(".${param.uppyFieldName_}for-ProgressBar").addClass("hidden");
            uppyUploadingCount--;
            isUploading = false;
        }
    
    var uppy_${param.uppyFieldName_} = Uppy.Core({debug: true, autoProceed: true, restrictions: {
            maxFileSize: 26214400,
            minFileSize: 1,
            maxNumberOfFiles: 1,
            minNumberOfFiles: 1
            <s:if test='!(#allowedFileTypes.equals(""))'>,
            allowedFileTypes: [${param.allowedFileTypes}]
            </s:if>
    },
        onBeforeFileAdded: (currentFile, files) => {
        <s:if test='( !(#checkFileFn == null || #checkFileFn.equals("")) )'>
            return ${checkFileFn}(currentFile);
        </s:if>
    }});
        
    <s:if test="%{rightToUpdate && checklist_step.contains(#checklist_type)}" >
    uppy_${param.uppyFieldName_}
        .use(Uppy.DragDrop, {target: '.${param.uppyFieldName_}for-DragDrop', height: 28})
        .use(Uppy.XHRUpload, {
            endpoint: '${uploadUrl_}?drFileCode_=${drFileCode_}&${param.uploadParams}&antiCsrf=<s:property value="%{#session.antiCsrf}"/>',
            formData: true,
            fieldName: 'uppyFile'
        })
        .use(Uppy.StatusBar, {target: '.${param.uppyFieldName_}for-ProgressBar', hideAfterFinish: false, showProgressDetails: true})
        .on('file-removed', (file, reason) => {
                    if (isUploading) {
                uppyUploadingCount--;
            }
            isUploading = false;
        })
        .on('restriction-failed', (file, error) => {
            alertBox(error);
//                    alert(`File type not allowed!`);
        })
        .on('upload-error', (file, error, response) => {alert(error);})
        .on('upload-success', onUploadSuccess${param.uppyFieldName_}(''))
        .on('upload', (data) => {
            uppyUploadingCount++;
            isUploading = true;
            $(".${param.uppyFieldName_}for-ProgressBar").removeClass("hidden");
        })
        .on('file-removed', (file, reason) => {
            if (isUploading) {
                uppyUploadingCount--;
            }
            isUploading = false;
        });
    </s:if>

    $(document).ready(function () {
        <s:if test='!(#uploadedFileId==null || #uploadedFileId.equals(""))'>
            $(".${param.uppyFieldName_}for-DragDrop .uppy-Root").addClass("hidden");
            <s:if test="%{rightToUpdate && checklist_step.contains(#checklist_type)}" >
                <s:if test='(#displayAsThumbnail.equals("Y"))'>
                    $("#${param.uppyFieldName_}DragDrop-fileDiv").html('<button class="d-inline-block" onclick="removeMe${param.uppyFieldName_}(' + "'DragDrop-fileDiv" + "','${param.uploadedFileId}', ''" + '); return false;"><i class="fa-ns fa fa-trash"></i></button>&nbsp;<a class="d-inline-block text-truncate max-w-150" href="viewTempFileAttachment?uploadID=${param.uploadedFileId}" target="_blank">${param.uploadedFileName}</a><img src="viewTempFileAttachment?tn_=Y&uploadID=${param.uploadedFileId}"/>');
                </s:if><s:else>
                    <s:if test='(#showDeleteBtn.equals("Y"))'>
                        $("#${param.uppyFieldName_}DragDrop-fileDiv").html('<button class="d-inline-block" onclick="removeMe${param.uppyFieldName_}(' + "'DragDrop-fileDiv" + "','${param.uploadedFileId}', ''" + '); return false;"><i class="fa-ns fa fa-trash"></i></button>&nbsp;<a class="d-inline-block text-truncate max-w-150" href="viewTempFileAttachment?uploadID=${param.uploadedFileId}" target="_blank">${param.uploadedFileName}</a>');
                    </s:if><s:else>
                        $("#${param.uppyFieldName_}DragDrop-fileDiv").html('&nbsp;<a class="d-inline-block text-truncate max-w-150" href="viewTempFileAttachment?uploadID=${param.uploadedFileId}" target="_blank">${param.uploadedFileName}</a>');
                    </s:else>
                </s:else>
                $("#${param.uppyFieldName_}DragDrop-fileDiv").removeClass("hidden");
            </s:if>
            <s:else>
                <s:if test='(#displayAsThumbnail.equals("Y"))'>
                    $("#${param.uppyFieldName_}DragDrop-fileDiv").html('<a class="d-inline-block text-truncate max-w-150" href="viewTempFileAttachment?uploadID=${param.uploadedFileId}" target="_blank">${param.uploadedFileName}</a><img src="viewTempFileAttachment?tn_=Y&uploadID=${param.uploadedFileId}"/>');
                </s:if><s:else>
                    <s:if test='(#showDeleteBtn.equals("Y"))'>
                        $("#${param.uppyFieldName_}DragDrop-fileDiv").html('<a class="d-inline-block text-truncate max-w-150" href="viewTempFileAttachment?uploadID=${param.uploadedFileId}" target="_blank">${param.uploadedFileName}</a>');
                    </s:if><s:else>
                        $("#${param.uppyFieldName_}DragDrop-fileDiv").html('&nbsp;<a class="d-inline-block text-truncate max-w-150" href="viewTempFileAttachment?uploadID=${param.uploadedFileId}" target="_blank">${param.uploadedFileName}</a>');
                    </s:else>
                </s:else>
                $("#${param.uppyFieldName_}DragDrop-fileDiv").removeClass("hidden");
            </s:else>
        </s:if>
    });
</script>
<%--<s:property value = "rightToUpdate" />
<s:property value = '%{checklist_step.contains(#checklist_type)}' />
<s:property value = '%{checklist_step}' />
<s:property value = '#checklist_type' />
<s:property value = "%{rightToUpdate && checklist_step.contains(#checklist_type)}" />--%>
   