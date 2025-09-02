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
            });
            
        </script>
    </head>
    <body>
        <form id="uppy2Form" action="saveSingleFileUppy" method="post">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Single File Upload <small>Sample</small></h4>
                </div>
                <div class="row form-horizontal form-group">
                    <label class="col-md-3 control-label">File Uploading <font class="asterisk">*</font></label>
                    <div class="col-md-3" style="min-height: 33px">
                        <s:include value="/base/uppyIncludeSingleFile.jsp">
                            <s:param name="uploadUrl_">uploadUppy</s:param>
                            <s:param name="uppyFieldName_">file1</s:param>
                            <s:param name="uppyHiddenName_">yourModel.drDocRepoModel.ID</s:param>
                            <s:param name="hideArrow">Y</s:param>
                            <s:param name="uppyFileList" value="testList"/>
                            <s:param name="drAppCode_">uppySample2_file1</s:param>
                            <s:param name="uploadParams">uploadRecordId_=<s:property value="%{yourModel.ID}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                            <s:param name="uploadedFileName"><s:property value="yourModel.drDocRepoModel.dr_doc_name"/></s:param>
                            <s:param name="uploadedFileId"><s:property value="yourModel.drDocRepoModel.ID"/></s:param>
                            <s:param name="theRecordId"><s:property value="yourModel.ID"/></s:param>
                            <s:param name="displayAsThumbnail">Y</s:param>
                            <%--<s:param name="delBtnMarginTop">6px</s:param>--%>
                        </s:include>
                    </div>
                    <div class="col-md-3" style="min-height: 33px">
                        <s:include value="/base/uppyIncludeSingleFile.jsp">
                            <s:param name="uploadUrl_">uploadUppy</s:param>
                            <s:param name="uppyFieldName_">file2</s:param>
                            <s:param name="uppyHiddenName_">yourModel.drDocRepoModel2.ID</s:param>
                            <s:param name="hideArrow">Y</s:param>
                            <s:param name="uppyFileList" value="testList"/>
                            <s:param name="drAppCode_">uppySample2_file2</s:param>
                            <s:param name="uploadParams">uploadRecordId_=<s:property value="%{yourModel.ID}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                            <s:param name="uploadedFileName"><s:property value="yourModel.drDocRepoModel2.dr_doc_name"/></s:param>
                            <s:param name="uploadedFileId"><s:property value="yourModel.drDocRepoModel2.ID"/></s:param>
                            <s:param name="theRecordId"><s:property value="yourModel.ID"/></s:param>
                            <s:param name="displayAsThumbnail">Y</s:param>
                            <%--<s:param name="delBtnMarginTop">6px</s:param>--%>
                        </s:include>
                    </div>
                </div>
                <div class="row form-horizontal form-group">
                    <label class="col-md-3 control-label">Description of file <font class="asterisk">*</font></label>
                    <div class="col-md-6">
                        <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
                        <s:hidden name="yourModel.ID" value="%{yourModel.ID}"/>
                        <input type="text" id='descOfFile' class="form-control" name="yourModel.uppy_file_desc" value="<s:property value="yourModel.uppy_file_desc"/>" required/>
                    </div>
                </div>
                <div class="row form-horizontal form-group">
                    <label class="col-md-3 control-label"></label>
                    <div class="col-md-6">
                        <button type="button" class="btn btn-primary" name="" id="actionName" onclick="return submitForm_bshor('uppy2Form');">
                            <i class="fa fa-save"></i>Save Single File Form
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
