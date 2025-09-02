<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<!-- Target DOM node #1 -->
<div class="row">
<div id="${param.uppyFieldName_}DragDrop-fileDiv" class="hidden" style="width: 100%">
    <ol></ol>
</div>
</div>
<div class="${param.uppyFieldName_}for-DragDrop" style="width: 100%;">
    <div class="${param.uppyFieldName_}for-ProgressBar" style="width: 100%;"></div>
</div>
<div id="${param.uppyFieldName_}drag-drop-area"></div>
<div id="${param.uppyFieldName_}divDeleteTempFile" class="hidden"></div>
<script>
    <s:set var="uppyFileKey">${param.uppyFileListName}</s:set>
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
    function removeMe${param.uppyFieldName_}(theBtn, uploadId, theRecord_id ) {
        var posting = $.post("removeSupportingFileAttachment?drFileCode_=${drFileCode_}&uploadID=" + uploadId + "&uploadRecordId_=" + "${theRecord_id}");
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
//            $("#${param.uppyFieldName_}" + theBtn).html("");
//            $("#${param.uppyFieldName_}" + theBtn).addClass("hidden");
//            $(".${param.uppyFieldName_}for-DragDrop .uppy-Root").removeClass("hidden");

        console.log($("#${param.uppyFieldName_}" + theBtn).closest('li'))
            $("#${param.uppyFieldName_}" + theBtn).closest('li').remove();
            uppy_${param.uppyFieldName_}.removeFile(theRecord_id);
            <s:if test='( !(#updateHiddenId == null || #updateHiddenId.equals("")) )'>
                $("#${updateHiddenId}").val("");
            </s:if>
            <s:if test='( !(#updateHiddenName == null || #updateHiddenName.equals("")) )'>
                $("#${updateHiddenName}").val("");
            </s:if>
        });
    }
    
    const onUploadSuccess${param.uppyFieldName_} = (elForUploadedFiles) =>
        (file, response) => {
            if (response.body.status === 'success') {
                const uploadID = response.body.fileId;
                const fileName = file.name;
                $("#${param.uppyFieldName_}DragDrop-fileDiv ol")
//                        .html($("#${param.uppyFieldName_}DragDrop-fileDiv ol").html() + 
//                    '<li><div class="item-content"><div class="item-inner">'+
//                    '<input type="hidden" id="${param.uppyHiddenName_}" name="${param.uppyHiddenName_}" value="' + uploadID + '"/><a href="viewTempFileUppy?uploadID=' + uploadID + '" target="_blank">' + fileName + '</a>'+
//                    '</div><div class="item-media"><a onclick="removeMe${param.uppyFieldName_}(' + "this" + ",'" + uploadID + "', '" + file.id + "'" + '); return false;"><i class="fa-ns fa fa-trash"></i>aaa</a></div></div></li>');
        .html('<li><button class="d-inline-block" onclick="removeMe${param.uppyFieldName_}(' + "'DragDrop-fileDiv" + "','"+uploadID+"', ''" + '); return false;"><i class="fa-ns fa fa-trash"></i></button>&nbsp;<a class="d-inline-block text-truncate" href="viewTempFileAttachment?uploadID=${param.uploadedFileId}" target="_blank">'+fileName+'</a></li>');

                $("#${param.uppyFieldName_}DragDrop-fileDiv").removeClass("hidden");
            } else {
                uppy_${param.uppyFieldName_}.removeFile(file.id);
                alert(response.body.errMsg);
            }
            $(".${param.uppyFieldName_}for-ProgressBar").addClass("hidden");
            
        }
        
        const uppy_${param.uppyFieldName_} = Uppy.Core({debug: true, autoProceed: true, restrictions: {
//            maxFileSize: 300000,
            maxNumberOfFiles: ${param.maxNumberOfFiles},
            minNumberOfFiles: 1
            <s:if test='!(#allowedFileTypes.equals(""))'>,
            allowedFileTypes: [${param.allowedFileTypes}]
            </s:if>
        },
        onBeforeFileAdded: (currentFile, files) => {
            var canAdd = true;
            Object.keys(files).forEach(fileID => {
                if (files[fileID].name === currentFile.name) {
                    canAdd = false;
                }
            })
            return canAdd;
        }
    });
       
<s:if test='%{!userDiv_.equals("00")}'>
    uppy_${param.uppyFieldName_}
        .use(Uppy.DragDrop, {target: '.${param.uppyFieldName_}for-DragDrop', height: 28})
        .use(Uppy.XHRUpload, {
            endpoint: '${param.uploadUrl_}?${param.uploadParams}',
            formData: true,
            fieldName: 'uppyFile',
            timeout:0
        })
        .use(Uppy.StatusBar, {target: '.${param.uppyFieldName_}for-ProgressBar', hideAfterFinish: true, showProgressDetails: false})
        .on('upload-success', onUploadSuccess${param.uppyFieldName_}(''))
        .on('complete', (result) => {
            console.log('Upload finished!');
            window.location.reload();

            if (result.failed.length > 0) {
                console.warn('Some files failed to upload:');
                result.failed.forEach((file) => {
                  console.error(file.name, ':', file.error);
                });
            }

            if (result.successful.length > 0) {
                console.log('Successfully uploaded files:', result.successful);
                // You can now do something with the successfully uploaded files,
                // like redirecting the user or updating a database.
            }
        })
        .on('upload', (data) => {
            $(".${param.uppyFieldName_}for-ProgressBar").removeClass("hidden");
        })
        .on('restriction-failed', (file, error) => {
            if ((error+"").includes("Error: You can only upload:")) {
                <s:if test='!(#allowedFileTypesDesc.equals(""))'>
                    alert(error);
                </s:if><s:else>
                    alert("${param.allowedFileTypesDesc}");
                </s:else>
            } else if ((error+"").endsWith(" files")) {
                alert("<s:text name="reachFileLimit"><s:param>${param.maxNumberOfFiles}</s:param></s:text>");
            } else if ((error+"").endsWith(" files")) {
                alert("["+file.name + "] <s:text name="uppyFileExists"/>");
            } else if ((error+"").startsWith("Error: Cannot add the file because")) {
                alert("["+file.name + "] <s:text name="uppyFileExists"/>");
            } else {
                alert(error);
            }
         })

        <s:if test='#hideArrow.equals("Y")'>
            $(".${param.uppyFieldName_}for-DragDrop .uppy-DragDrop-container .uppy-DragDrop-inner .uppy-DragDrop-arrow").addClass("hidden");
        </s:if>
        <s:if test='uppyFileMap.get(#uppyFileKey).size() > 0'>
            $("#${param.uppyFieldName_}DragDrop-fileDiv").removeClass("hidden");
            uppy_${param.uppyFieldName_}.setOptions({ autoProceed: false });
            <s:iterator value="uppyFileMap.get(#uppyFileKey)" var="theFile">
                uppy_${param.uppyFieldName_}.addFile({
                name: '<s:property value="#theFile.dr_doc_name"/>', // file name
                type: '<s:property value="#theFile.mime_type"/>', // file type
                data: '', // file blob
                meta: {
                  // optional, store the directory path of a file so Uppy can tell identical files in different directories apart
//                          relativePath: webkitFileSystemEntry.relativePath,
                  drDocId: '<s:property value="#theFile.dr_doc_id"/>',
                },
//                        source: 'Local', // optional, determines the source of the file, for example, Instagram
                isRemote: true // optional, set to true if actual file is not in the browser, but on some remote server, for example, when using companion in combination with Instagram
              });
            </s:iterator>
            uppy_${param.uppyFieldName_}.getFiles().forEach(file => {
                $("#${param.uppyFieldName_}DragDrop-fileDiv ol").html($("#${param.uppyFieldName_}DragDrop-fileDiv ol").html() + 
                    '<li><div class="item-content"><div class="item-inner">'+
                    '<input type="hidden" id="${param.uppyHiddenName_}" name="${param.uppyHiddenName_}" value="' + file.meta.drDocId + '"/><a href="viewTempFileUppy?uploadID=' + file.meta.drDocId + '" target="_blank">' + file.name + '</a>'+
                    '</div><div class="item-media"><a onclick="removeMe${param.uppyFieldName_}(' + "this" + ",'" + file.meta.drDocId + "', '" + file.id + "'" + '); return false;"><i class="fa-ns fa fa-trash"></i></a></div></div></li>');

                uppy_${param.uppyFieldName_}.setFileState(file.id, { 
                    progress: { uploadComplete: true, uploadStarted: true } 
                });
            });
            $(".${param.uppyFieldName_}for-ProgressBar .uppy-Root").addClass("hidden");
            uppy_${param.uppyFieldName_}.setOptions({ autoProceed: true });
        </s:if>
</s:if>            
</script>
