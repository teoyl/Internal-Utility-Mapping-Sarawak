<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sample</title>
        <script src="uppy/v1.27.0/uppy.min.js"></script>
        <link href="uppy/v1.27.0/uppy.min.css" rel="stylesheet">
        <script type="text/javascript">
//            function uppy_init(){
//	const ProgressBar = Uppy.ProgressBar
//	const XHRUpload = Uppy.XHRUpload
//	const Tus = Uppy.Tus
//
//		var v_id = '#uppyModalOpener1';
//	var v_inline = 0;
//	var v_max_file_size = 8000000;
//	var v_max_files = 2;
//	var v_allow_file_type = null;
//	var v_upload_url = 'http://localhost:8080/forNewProject/upload';
//
//                const uppy0 = Uppy.Core({
//                    debug: false,
//                    autoProceed: false,
//                    allowMultipleUploads: true,
//                    restrictions: {
//                        maxFileSize: 8000000,
//                        maxNumberOfFiles: 2,
//                        minNumberOfFiles: null,
//                        allowedFileTypes: null
//                    },
//                })
//                        .use(Uppy.Dashboard, {trigger: v_id, showRemoveButtonAfterComplete: true, inline: 0})
//                        .use(ProgressBar, {target: Uppy.Dashboard})
//                        .use(XHRUpload, {fieldName: 'upload_file', method: 'post', formData: true, endpoint: v_upload_url})
//            }
//            uppy_init();
        </script>
        <style>
            button .fa-ns {
                margin-right: 0px;
            }
            .uppy-DragDrop-label {
                font-size: 0.9em;
                max-width: 100%;
            }
            .uppy-DragDrop-arrow{
                width:0px;
                height:0px;
                margin-bottom:0px;
            }
            .uppy-DragDrop-inner{
                padding: 0px;
            }
        </style>
    </head>
      <body>
    <div id="drag-drop-area"></div>

    <script>
      var uppy = new Uppy.Core()
        .use(Uppy.Dashboard, {
          inline: true,
          target: '#drag-drop-area'
        })
        .use(Uppy.Tus, {endpoint: 'TusServlet'})

      uppy.on('complete', (result) => {
        console.log('Upload complete! We’ve uploaded these files:', result.successful)
      })
    </script>
  </body>
</html>
