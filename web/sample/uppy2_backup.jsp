<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="uppy/v1.15.0/uppy.min.css">
        <title>Uppy</title>
        <style>
            button .fa-ns {
                margin-right: 0px;
            }
        </style>
    </head>
    <body>
        <form method="post" id="uppyForm" name="uppyForm">
            <input type="hidden" id="file_"
        </form>
    <section class="example-one">
        <!-- Target DOM node #1 -->
        <div class="for-DragDrop">
            <div id="DragDrop-fileDiv" class="hidden"></div>
            <div class="for-ProgressBar"></div>
        </div>

        <!-- Progress bar #1 -->

    </div>
<!--    <div class="UppyForm">
        <form action="uppyUploadSample">
            <h5>Uppy was not loaded — slow connection, unsupported browser, weird JS error on a page — but the upload still works, because HTML is cool like that</h5>
            <input type="file" name="singleFile" multiple="">
            <button type="submit">Fallback Form Upload</button>
        </form>
    </div>
    <div class="UppyProgressBar"></div>
    <div class="uploaded-files">
        <h5>Uploaded files:</h5>
        <ol></ol>
    </div>-->
    <div id="drag-drop-area"></div>
    <div id="divDeleteTempFile" class="hidden"></div>
    <script src="uppy/v1.15.0/uppy.min.js"></script>
    <script>
        function removeMe(fileDiv, tempFileId, uploadId) {
            var posting = $.post("removeTempFileSample?uploadID="+tempFileId);
            posting.fail(function(jqXHR){
                if(jqXHR.status === 404) {
                    alert(error404+'\n'+error404_msg1+'\n'+error404_msg2);
                } else {
                    alert("Unexpected error occurred");
                }
            });
            posting.done(function(data) {
                $("#" + fileDiv).html("");
                $("#" + fileDiv).addClass("hidden");
                $(".for-DragDrop .uppy-Root").removeClass("hidden");
                uppyOne.removeFile(uploadId);
            });
        }
        const onUploadSuccess = (elForUploadedFiles) =>
        (file, response) => {
            if (response.body.status === 'success') {
                const uploadID = response.body.fileId;
                const fileName = file.name;
                $(".for-DragDrop .uppy-Root").addClass("hidden");
                $("#DragDrop-fileDiv").html('<button onclick="removeMe('+"'DragDrop-fileDiv"+"','"+uploadID+"', '"+file.id+"'"+'); return false;"><i class="fa-ns fa fa-trash"></i></button>&nbsp;<input type="hidden" name="tempFileId" value="'+uploadID+'"/><a href="viewTempFileSample?uploadID='+uploadID+'" target="_blank">'+fileName+'</a>');
                $("#DragDrop-fileDiv").removeClass("hidden");
//                document.querySelector(elForUploadedFiles).innerHTML +=
//                  '<li id="'+uploadID+'"><button onclick="removeMe('+"'"+uploadID+"', '"+file.id+"'"+'); return false;"><i class="fa-ns fa fa-trash"></i></button>&nbsp;<input type="hidden" name="tempFileId" value="'+uploadID+'"/><a href="viewTempFileSample?uploadID='+uploadID+'" target="_blank">'+fileName+'</a></li>'
            } else {
                alert(response.body.errMsg);
            }
            $(".for-ProgressBar").addClass("hidden");
        }
        const uppyOne = Uppy.Core({debug: true, autoProceed: true, restrictions: {
//      maxFileSize: 300000,
      maxNumberOfFiles: 1,
      minNumberOfFiles: 1//,
//      allowedFileTypes: ['image/*']
    }});
        uppyOne
                .use(Uppy.DragDrop, {target: '.example-one .for-DragDrop', height: 40})
                .use(Uppy.XHRUpload, {
                    endpoint: 'uppyUploadSample?param1=param1',
                    formData: true,
                    fieldName: 'uppyFile'
                })
                .use(Uppy.ProgressBar, {target: '.example-one .for-ProgressBar', hideAfterFinish: false})
                .on('upload-success', onUploadSuccess('.example-one .uploaded-files ol'))
                .on('upload', (data) => {
                    $(".for-ProgressBar").removeClass("hidden");
                })
                
            $(".uppy-DragDrop-arrow").addClass("hidden");
/*
        var uppy = Uppy.Core({debug: true, autoProceed: true})//{ debug: true, autoProceed: true }
                .use(Uppy.FileInput, {
                    target: '.UppyForm',
                    replaceTargetContent: true
                })
        uppy.use(Uppy.ProgressBar, {
            target: '.UppyProgressBar',
            hideAfterFinish: false
        })
        uppy.use(Uppy.XHRUpload, {
            endpoint: 'uppyUploadSample',
            formData: true,
            fieldName: 'singleFile'
        })
        uppy.on('upload-success', (file, response) => {
//    var myJSON = JSON.stringify(response);
//    alert(response.body.status);
//  const url = response.uploadURL;
            const fileName = file.name;
//alert("fileName = " + fileName);
//alert("URL = " + url);
            document.querySelector('.uploaded-files ol').innerHTML +=
                    '<li><a href="" target="_blank">' + fileName + '</a></li>';
        })
        */
//            var uppy = Uppy.Core({debug: true, autoProceed: true})//{ debug: true, autoProceed: true }
//                    .use(Uppy.Dashboard, {
//                        inline: true,
//                        target: '#drag-drop-area',
//                        metaFields: [
//                            {id: 'name', name: 'Name', placeholder: 'file name'},
//                            {id: 'caption', name: 'Caption', placeholder: 'describe what the image is about'}
//                        ]
//                    })
//                    .use(Uppy.XHRUpload, {
//                        endpoint: 'uppyUploadSample',
//                        formData: true,
//                        metaFields: ["name", "caption"],
//                        fieldName: 'singleFile'
//                    })
//
//            uppy.on('complete', (result) => {
//                //          var myJSON = JSON.stringify(result)
//                //          alert(1 + myJSON);
//                console.log('Upload complete! We’ve uploaded these files:', result.successful)
//            })
    </script>
</body>
</html>
