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
            .uppy-DragDrop-label {
                font-size: 0.9em;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function () {
//                $.get( "https://ss-intmobile.tnt.sarawak.gov.my/mobile/getCartInformationSrmsCart?sid=openpayment_tnt", function( data ) {
                $.post( "https://10.17.100.73:8049/smarttourism/getHighlightsAPI?token=a", function( data ) {
//                $( ".result" ).html( data );
                 alert(JSON.stringify(data));
                 });
            });
            
        </script>
    </head>
    <body>
        <form id="uppy2Form" action="uppy2SaveApplicationSample" method="post">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Page Title <small>Edit</small></h4>
                </div>
                <div class="row form-horizontal form-group">
                    <label class="col-md-4 control-label">File Uploading <font class="asterisk">*</font></label>
                    <div class="col-md-5" style="height: 33px">
                        <s:include value="/base/uppyIncludeSingleFile.jsp">
                            <s:param name="uploadUrl_">uppyUpload</s:param>
                            <s:param name="uppyFieldName_">file1</s:param>
                            <s:param name="uppyHiddenName_">uppySample2FileId</s:param>
                            <s:param name="hideArrow">Y</s:param>
                            <s:param name="uppyFileList" value="testList"/>
                            <s:param name="uploadParams">drAppCode_=uppySample2&uploadRecordId_=<s:property value="%{yourModel.ID}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                            <s:param name="uploadedFileName"><s:property value="uploadedFile_fileName"/></s:param>
                            <s:param name="uploadedFileId"><s:property value="uploadedFile_fileId"/></s:param>
                            <s:param name="theRecordId"><s:property value="yourModel.ID"/></s:param>
                            <%--<s:param name="delBtnMarginTop">6px</s:param>--%>
                        </s:include>
                    </div>
                </div>
                <div class="row form-horizontal form-group">
                    <label class="col-md-4 control-label">Description of file <font class="asterisk">*</font></label>
                    <div class="col-md-5">
                        <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
                        <s:hidden name="yourModel.ID" value="%{yourModel.ID}"/>
                        <input type="text" id='descOfFile' class="form-control" name="yourModel.uppy_file_desc" value="<s:property value="yourModel.uppy_file_desc"/>" required/>
                    </div>
                </div>
                <div class="row form-horizontal form-group">
                    <label class="col-md-4 control-label"></label>
                    <div class="col-md-5">
                        <button type="button" class="btn btn-primary" name="" id="actionName" onclick="return submitForm_bshor('uppy2Form');">
                            <i class="fa fa-save"></i>Save Uppy2 Form
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
