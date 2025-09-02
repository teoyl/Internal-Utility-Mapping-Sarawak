<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<style>
    .uppy-StatusBar-actionBtn--retry {
        display: none !important;
    }
</style>
    
<!-- Target DOM node #1 -->
<div class="${param.uppyFieldName_}for-DragDrop">
    <div id="${param.uppyFieldName_}DragDrop-fileDiv" class="hidden"></div>
</div>
<div class="${param.uppyFieldName_}for-ProgressBar"></div>
<div id="${param.uppyFieldName_}drag-drop-area"></div>
<div id="${param.uppyFieldName_}divDeleteTempFile" class="hidden"></div>
<script>
    <s:set var="theModelID">${param.theModelID}</s:set>
    <s:set var="theModelID2">${param.theModelID}@</s:set>
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
    <s:set var="uploadedFileId2">${param.uploadedFileId}@</s:set>
    <s:set var="theRecord_id">${param.theRecord_id}</s:set>
    <s:set var="theRecord_id2">${param.theRecord_id}@</s:set> 
    <s:set var="allowedFileTypes">${param.allowedFileTypes}</s:set>
    <s:set var="allowedFileTypes2">${param.allowedFileTypes}@</s:set>
    <s:if test='#allowedFileTypes2.equals("@")'>
        <s:set var="allowedFileTypes"></s:set>
    </s:if>
    <s:set var="updateHiddenId">${param.updateHiddenId}</s:set>
    <s:set var="updateHiddenId2">${param.updateHiddenId}@</s:set>
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
    <s:if test='#displayAsThumbnail2.contains("@")'>
        <s:set var="displayAsThumbnail">N</s:set>
    </s:if> 
        //uploadedFileId - ${uploadedFileId}
        //uploadedFileId2 - ${uploadedFileId2}
        //drFileCode_ - ${drFileCode_}
    <s:if test='#uploadedFileName2.equals("@")'>
        <s:set var="uploadedFileName" value=""/>
        <s:set var="uploadedFileId"></s:set>
    </s:if>
    <s:if test='#uploadedFileId2.equals("@")'>
        <s:set var="uploadedFileId"><s:property value="fileToBeID"/></s:set>
    </s:if>
    <s:if test='#theModelID2.equals("@")'>
        <s:set var="theModelID">-</s:set>
    </s:if>
    <s:if test='#uploadUrl_2.equals("@")'>
        //aaaaaaaaaaaaaaaaaaaaaaaaaaaa
        <%--<s:set var="uploadUrl_">http://localhost:8080/forNewProject/upload_tusUppy</s:set>--%>
        <%--<s:set var="uploadUrl_">TusServlet</s:set>--%>
        <s:set var="uploadUrl_">upload_tus/${uploadedFileId}/${drFileCode_}/${theModelID}</s:set>
    </s:if><s:else>
        //bbbbbbbbbbbbbbbbbbbb
        <s:if test='#theRecord_id==null || theRecord_id.equals("")'>
            //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa
        </s:if>
        <s:set var="uploadUrl_">${uploadUrl_}/${uploadedFileId}/${drFileCode_}/${theModelID}</s:set>
    </s:else>
    <s:if test='#delBtnMarginTop2.contains("@")'>
        <s:set var="delBtnMarginTop" value='"6px"'/>
    </s:if>
    var isUploading = false;
    function removeMe${param.uppyFieldName_}(fileDiv, tempFileId, uploadId) {
        var posting = $.post("removeTempFileUppy?drFileCode_=${drFileCode_}&uploadID=" + tempFileId + "&uploadRecordId_="+$("#${theRecord_id}").val());
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
//        (result) => {
//            console.log('Upload complete! We’ve uploaded these files:', result.successful[0].data.name)
//            console.log('result is:', result.successful)
//            console.log('result is:', result.successful[0].id)
//        }
        (result) => {
            if (result.successful.length > 0) {
                const uploadID = '${uploadedFileId}';
                const fileName = result.successful[0].data.name;
                const fileId = result.successful[0].id;
                <s:if test='( !(#updateHiddenId == null || #updateHiddenId.equals("")) )'>
                    $("#${updateHiddenId}").val(uploadID);
                </s:if>
                <s:if test='( !(#updateHiddenName == null || #updateHiddenName.equals("")) )'>
                    $("#${updateHiddenName}").val(fileName);
                </s:if>
                $(".${param.uppyFieldName_}for-DragDrop .uppy-Root").addClass("hidden");
                <s:if test='(#displayAsThumbnail.equals("Y"))'>
                    $("#${param.uppyFieldName_}DragDrop-fileDiv").html('<button style="margin-top:${delBtnMarginTop}" onclick="removeMe${param.uppyFieldName_}(' + "'DragDrop-fileDiv" + "','" + uploadID + "', '" + fileId + "'" + '); return false;"><i class="fa-ns fa fa-trash"></i></button>&nbsp;<a href="viewTempFileUppy?uploadID=' + uploadID + '" target="_blank">' + fileName + '</a><img src="viewTempFileUppy?tn_=Y&uploadID='+uploadID+'"/>');
                </s:if><s:else>
                    $("#${param.uppyFieldName_}DragDrop-fileDiv").html('<button style="margin-top:${delBtnMarginTop}" onclick="removeMe${param.uppyFieldName_}(' + "'DragDrop-fileDiv" + "','" + uploadID + "', '" + fileId + "'" + '); return false;"><i class="fa-ns fa fa-trash"></i></button>&nbsp;<a href="viewTempFileUppy?uploadID=' + uploadID + '" target="_blank">' + fileName + '</a>');                        
                </s:else>
                    
                $("#${param.uppyFieldName_}DragDrop-fileDiv").removeClass("hidden");
//                document.querySelector(elForUploadedFiles).innerHTML +=
//                  '<li id="'+uploadID+'"><button onclick="removeMe('+"'"+uploadID+"', '"+fileId+"'"+'); return false;"><i class="fa-ns fa fa-trash"></i></button>&nbsp;<input type="hidden" name="tempFileId" value="'+uploadID+'"/><a href="viewTempFileUppy?uploadID='+uploadID+'" target="_blank">'+fileName+'</a></li>'
            } else {
                uppy_${param.uppyFieldName_}.removeFile(fileId);
                alert("Fail to upload the file");
            }
            $(".${param.uppyFieldName_}for-ProgressBar").addClass("hidden");
            uppyUploadingCount--;
            isUploading = false;
        }
    
    var uppy_${param.uppyFieldName_} = Uppy.Core({debug: true, autoProceed: true, restrictions: {
//      maxFileSize: 300000,
            maxNumberOfFiles: 1,
            minNumberOfFiles: 1<s:if test='!(#allowedFileTypes.equals(""))'>,
            allowedFileTypes: [${param.allowedFileTypes}]
            </s:if>
    },
        onBeforeFileAdded: (currentFile, files) => {
        <s:if test='( !(#checkFileFn == null || #checkFileFn.equals("")) )'>
            return ${checkFileFn}(currentFile);
        </s:if>
    }});
        
    uppy_${param.uppyFieldName_}
            .use(Uppy.Tus, {
                endpoint: '${uploadUrl_}?drFileCode_=${drFileCode_}&${param.uploadParams}&antiCsrf=<s:property value="%{#session.antiCsrf}"/>',
//                endpoint: '${uploadUrl_}',
                method:'post',
                retryDelays: [0, 1000, 3000, 5000]
            })
            .use(Uppy.DragDrop, {target: '.${param.uppyFieldName_}for-DragDrop', height: 28})
//            .use(Uppy.XHRUpload, {
//                method:'post',
//                endpoint: '${uploadUrl_}?drFileCode_=${drFileCode_}&${param.uploadParams}&antiCsrf=<s:property value="%{#session.antiCsrf}"/>',
//                endpoint: '${uploadUrl_}',
//                formData: true,
//                fieldName: 'uppyFile'
//            })
            .use(Uppy.StatusBar, {target: '.${param.uppyFieldName_}for-ProgressBar', hideAfterFinish: false, showProgressDetails: true})
            .on('complete', onUploadSuccess${param.uppyFieldName_}(''))
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
            })
//            $(".${param.uppyFieldName_}for-DragDrop .uppy-DragDrop-container .uppy-DragDrop-inner .uppy-DragDrop-label")
//    $(".uppy-DragDrop-arrow").addClass("hidden");
    $(document).ready(function () {
        <s:if test='!(#uploadedFileId2.equals("@")) && !(#uploadedFileId==null || #uploadedFileId.equals(""))'>
            $(".${param.uppyFieldName_}for-DragDrop .uppy-Root").addClass("hidden");
            <s:if test='(#displayAsThumbnail.equals("Y"))'>
                $("#${param.uppyFieldName_}DragDrop-fileDiv").html('<button style="margin-top:${delBtnMarginTop}" onclick="removeMe${param.uppyFieldName_}(' + "'DragDrop-fileDiv" + "','${param.uploadedFileId}', ''" + '); return false;"><i class="fa-ns fa fa-trash"></i></button>&nbsp;<a href="viewTempFileUppy?uploadID=${param.uploadedFileId}" target="_blank">${param.uploadedFileName}</a><img src="viewTempFileUppy?tn_=Y&uploadID=${param.uploadedFileId}"/>');
            </s:if><s:else>
                <s:if test='(#showDeleteBtn.equals("Y"))'>
                    $("#${param.uppyFieldName_}DragDrop-fileDiv").html('<button style="margin-top:${delBtnMarginTop}" onclick="removeMe${param.uppyFieldName_}(' + "'DragDrop-fileDiv" + "','${param.uploadedFileId}', ''" + '); return false;"><i class="fa-ns fa fa-trash"></i></button>&nbsp;<a href="viewTempFileUppy?uploadID=${param.uploadedFileId}" target="_blank">${param.uploadedFileName}</a>');
                </s:if><s:else>
                    $("#${param.uppyFieldName_}DragDrop-fileDiv").html('&nbsp;<a href="viewTempFileUppy?uploadID=${param.uploadedFileId}" target="_blank">${param.uploadedFileName}</a>');
                </s:else>
            </s:else>
            $("#${param.uppyFieldName_}DragDrop-fileDiv").removeClass("hidden");
        </s:if>
    });
</script>
