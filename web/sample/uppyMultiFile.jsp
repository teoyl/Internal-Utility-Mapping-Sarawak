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
        <form id="uppy3Form" action="saveMultiFileUppy" method="post">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Multiple File Upload <small>Sample</small><s:property value="yourParentModel.ID"/></h4>
                </div>
                <div class="row form-horizontal form-group">
                    <label class="col-md-4 control-label">File Uploading <font class="asterisk">*</font></label>
                    <div class="col-md-5" style="min-height: 33px">
                        <s:include value="/base/uppyIncludeMultipleFile.jsp">
                            <s:param name="uploadUrl_">uploadUppy</s:param>
                            <s:param name="uppyFieldName_">file1</s:param>
                            <s:param name="uppyHiddenName_">multipleFileDrDocIds</s:param>
                            <s:param name="hideArrow">Y</s:param>
                            <s:param name="maxNumberOfFiles">3</s:param>
                            <s:param name="uploadParams">drAppCode_=uppySample3&uploadRecordId_=<s:property value="%{yourParentModel.ID}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
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
                <div class="row form-horizontal form-group">
                    <label class="col-md-4 control-label">Description of file <font class="asterisk">*</font></label>
                    <div class="col-md-5">
                        <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
                        <s:hidden name="yourParentModel.ID" value="%{yourParentModel.ID}"/>
                        <input type="text" id='descOfFile' class="form-control" name="yourParentModel.uppy_parent_desc" value="<s:property value="yourParentModel.uppy_parent_desc"/>" required/>
                    </div>
                </div>
                <div class="row form-horizontal form-group">
                    <label class="col-md-4 control-label"></label>
                    <div class="col-md-5">
                        <button type="button" class="btn btn-primary" name="" id="actionName" onclick="return submitForm_bshor('uppy3Form');">
                            <i class="fa fa-save"></i>Save Muti-file Form
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
