<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<!-- Target DOM node #1 -->
<span id="${param.uppyFieldName_}deleteFileDiv" class="hidden"></span>
<span id="${param.uppyFieldName_}DragDrop-fileDiv" class="hidden"></span>
<div id="${param.uppyFieldName_}divDeleteTempFile" class="hidden"></div>
<div class="row" style="width: 100%;">
<div class="${param.uppyFieldName_}for-DragDrop" style="width: 100%;">
    <div class="${param.uppyFieldName_}for-ProgressBar" style="width: 100%;"></div>
</div>
</div>
<div id="${param.uppyFieldName_}drag-drop-area"></div>
<script>
    <s:set var="hideArrow">${param.hideArrow}</s:set>
    <s:set var="delBtnMarginTop">${param.delBtnMarginTop}</s:set>
    <s:set var="delBtnMarginTop2">${delBtnMarginTop}+""</s:set>
    <s:set var="uploadedFileName">${param.uploadedFileName}</s:set>
    <s:set var="uploadedFileName2">${param.uploadedFileName}+""</s:set>
    <s:set var="uploadedFileId">${param.uploadedFileId}</s:set>
    <s:set var="theRecordId">${param.theRecordId}</s:set>
    <s:set var="theRecordId2">${param.theRecordId}+""</s:set>
    <s:set var="allowedFileTypes">${param.allowedFileTypes}</s:set>
    <s:set var="allowedFileTypes2">${param.allowedFileTypes}+""</s:set>
    <s:if test='#allowedFileTypes2.contains("@") && #allowedFileTypes2.contains(".")'>
        <s:set var="allowedFileTypes" value=""/>
    </s:if>
        
    <s:set var="allowedFileTypesDesc">${param.allowedFileTypesDesc}</s:set>
    <s:set var="allowedFileTypesDesc2">${param.allowedFileTypesDesc}+""</s:set>
    <s:if test='#allowedFileTypesDesc2.contains("@") && #allowedFileTypesDesc2.contains(".")'>
        <s:set var="allowedFileTypesDesc" value=""/>
    </s:if>
        
    <s:if test='#theRecordId2.contains("@") && #theRecordId2.contains(".")'>
        <s:set var="theRecordId" value=""/>
    </s:if>
        
    <s:if test='#uploadedFileName2.contains("@") && #uploadedFileName2.contains(".")'>
        <s:set var="uploadedFileName" value=""/>
        <s:set var="uploadedFileId" value=""/>
    </s:if>
        
    <s:if test='#delBtnMarginTop2.contains("@") && #delBtnMarginTop2.contains(".")'>
        <s:set var="delBtnMarginTop" value='"6px"'/>
    </s:if>
    function removeMe${param.uppyFieldName_}(fileDiv, tempFileId, uploadId) {
        app.request.post("removeTempFileUppy", { uploadID:tempFileId }, function (data) {
            $("#${param.uppyFieldName_}" + fileDiv).html("");
            $("#${param.uppyFieldName_}" + fileDiv).addClass("hidden");
            $(".${param.uppyFieldName_}for-DragDrop .uppy-Root").removeClass("hidden");
            $(".${param.uppyFieldName_}im").html("");
            uppy_${param.uppyFieldName_}.removeFile(uploadId);
        });
        
//        $.ajax({
//            method: "POST",
//            url: "removeTempFileSample?uploadID=" + tempFileId
//        })
//        .done(function( msg ) {
//            alert( "Data Saved: " + msg );
//        });
//        var posting = $.post("removeTempFileSample?uploadID=" + tempFileId);
//        posting.fail(function (jqXHR) {
//            if (jqXHR.status === 404) {
//                alert(error404 + '\n' + error404_msg1 + '\n' + error404_msg2);
//            } else {
//                alert("Unexpected error occurred");
//            }
//        });
//        posting.done(function (data) {
//            alert(2);
//            $("#${param.uppyFieldName_}" + fileDiv).html("");
//            $("#${param.uppyFieldName_}" + fileDiv).addClass("hidden");
//            $(".${param.uppyFieldName_}for-DragDrop .uppy-Root").removeClass("hidden");
//            $(".${param.uppyFieldName_}im").html("");
//            uppy_${param.uppyFieldName_}.removeFile(uploadId);
//        });
    }
    const onUploadSuccess${param.uppyFieldName_} = (elForUploadedFiles) =>
        (file, response) => {
            if (response.body.status === 'success') {
                const uploadID = response.body.fileId;
                const fileName = file.name;
                $(".${param.uppyFieldName_}for-DragDrop .uppy-Root").addClass("hidden");
                $("#${param.uppyFieldName_}DragDrop-fileDiv").html('<input type="hidden" id="${param.uppyHiddenName_}" name="${param.uppyHiddenName_}" value="' + uploadID + '"/><a onclick="downloadPage(\'viewTempFileUppy?uploadID=' + uploadID + '\')">' + fileName + '</a>');
                $(".${param.uppyFieldName_}im").html('<a onclick="removeMe${param.uppyFieldName_}(' + "'DragDrop-fileDiv" + "','" + uploadID + "', '" + file.id + "'" + '); return false;"><i class="icon f7-icons">trash</i></a>');
                
                $("#${param.uppyFieldName_}DragDrop-fileDiv").removeClass("hidden");
//                document.querySelector(elForUploadedFiles).innerHTML +=
//                  '<li id="'+uploadID+'"><button onclick="removeMe('+"'"+uploadID+"', '"+file.id+"'"+'); return false;"><i class="fa-ns fa fa-trash"></i></button>&nbsp;<input type="hidden" name="tempFileId" value="'+uploadID+'"/><a href="viewTempFileSample?uploadID='+uploadID+'" target="_blank">'+fileName+'</a></li>'
            } else {
                uppy_${param.uppyFieldName_}.removeFile(file.id);
                alert(response.body.errMsg);
            }
            $(".${param.uppyFieldName_}for-ProgressBar").addClass("hidden");
        }
    const uppy_${param.uppyFieldName_} = Uppy.Core({debug: true, autoProceed: true, restrictions: {
//      maxFileSize: 300000,
            maxNumberOfFiles: 1,
            minNumberOfFiles: 1<s:if test='!(#allowedFileTypes.equals(""))'>,
            allowedFileTypes: [${param.allowedFileTypes}]
            </s:if>
        }});
        
    uppy_${param.uppyFieldName_}
            .use(Uppy.DragDrop, {target: '.${param.uppyFieldName_}for-DragDrop', height: 28})
            .use(Uppy.XHRUpload, {
                endpoint: '${param.uploadUrl_}?${param.uploadParams}',
                formData: true,
                fieldName: 'uppyFile',
                timeout:0
            })
            .use(Uppy.StatusBar, {target: '.${param.uppyFieldName_}for-ProgressBar', hideAfterFinish: false, showProgressDetails: true})
            .on('upload-success', onUploadSuccess${param.uppyFieldName_}(''))
            .on('upload', (data) => {
                $(".${param.uppyFieldName_}for-ProgressBar").removeClass("hidden");
            })
            <s:if test='#hideArrow.equals("Y")'>
                $(".${param.uppyFieldName_}for-DragDrop .uppy-DragDrop-container .uppy-DragDrop-inner .uppy-DragDrop-arrow").addClass("hidden");
            </s:if>
            <s:if test='!(#uploadedFileId==null || #uploadedFileId.equals(""))'>
                $(".${param.uppyFieldName_}for-DragDrop .uppy-Root").addClass("hidden");
                $("#${param.uppyFieldName_}DragDrop-fileDiv").html('<button class="button" style="margin-top:${delBtnMarginTop}" onclick="removeMe${param.uppyFieldName_}(' + "'DragDrop-fileDiv" + "','${param.uploadedFileId}', ''" + '); return false;"><i class="fa-ns fa fa-trash"></i></button>&nbsp;<input type="hidden" id="${param.uppyHiddenName_}" name="${param.uppyHiddenName_}" value="${param.uploadedFileId}"/><a href="viewTempFileUppy?uploadID=${param.uploadedFileId}" target="_blank">${param.uploadedFileName}</a>');
                $("#${param.uppyFieldName_}DragDrop-fileDiv").removeClass("hidden");
            </s:if>
//            $(".${param.uppyFieldName_}for-DragDrop .uppy-DragDrop-container .uppy-DragDrop-inner .uppy-DragDrop-label")
//    $(".uppy-DragDrop-arrow").addClass("hidden");
</script>
