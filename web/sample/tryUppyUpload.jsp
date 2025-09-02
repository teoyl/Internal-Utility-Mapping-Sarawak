<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sample</title>
        <script src="uppy/v1.22.0/uppy.min.js"></script>
        <script type="text/javascript">
            function addFile(parentIdx) {
                $("#addFileParentIdx").val(parentIdx);
                submitForm("sampleFormId", "uppyUpload_addFile");
            }
            function deleteFile(parentIdx, childIdx) {
                $("#addFileParentIdx").val(parentIdx);
                $("#deleteFileChildIdx").val(childIdx);
                submitForm("sampleFormId", "uppyUpload_deleteFile");
            }
            function checkFileName(currentFile) {
                if (currentFile.name === '1.pdf') {
                    alert("not allow this file name");
                    return false;
                }
                return true;
            }
            $(document).ready(function () {
            });
        </script>
        <link rel="stylesheet" href="uppy/v1.22.0/uppy.min.css">
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
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form name="form" id="sampleFormId" action="loadSample" method="post">
            <div class="row">
                <div class="col-md-5">
                    <button onclick='submitForm("sampleFormId", "uppyUpload_tryUppyUpload"); return false;'>Reload</button>
                </div>
            </div>
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <s:hidden name="addFileParentIdx" id="addFileParentIdx" value=""/>
            <s:hidden name="deleteFileChildIdx" id="deleteFileChildIdx" value=""/>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Sample 1 <small>(1 Model 1 file)</small></h4>
                </div>
                <div class="panel-body">
                    
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Action</label>
                        <div class="col-md-5">
                            <button onclick='submitForm("sampleFormId", "uppyUpload_clearDirectModel");'>Clear Direct Model</button>
                            <button onclick='submitForm("sampleFormId", "uppyUpload_createDirectModel");'>Create Direct Model</button>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Direct Model ID</label>
                        <div class="col-md-5">
                            <s:property value="%{directModel.ID}"/>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label"></label>
                        <div class="col-md-5">
                            <s:hidden name="directModel.ID" id="directModel_ID" value="%{directModel.ID}"/>
                            <s:hidden id="directModel_drDocName" name="directModel.drDocRepoModel.dr_doc_name"/>
                            <s:hidden id="directModel_drDocId" name="directModel.dr_doc_id"/>
                            <s:include value="/base/uppyIncludeSingleFile_tus.jsp">
                                <%--<s:param name="uploadUrl_">upload_tus/<s:property value="fileToBeID"/>/directModel_file</s:param>--%>
                                <s:param name="uploadUrl_">upload_tus</s:param>
                                <%--<s:param name="showDeleteBtn">'N'</s:param>--%>
                                <s:param name="uppyFieldName_">directModel_file</s:param>
                                <%--<s:param name="uppyHiddenName_">directModel.drDocRepoModel.ID</s:param>--%>
                                <s:param name="drFileCode_">directModel_file</s:param>
                                <s:param name="uploadParams">uploadRecordId_=<s:property value="%{directModel.ID}"/></s:param>
                                <s:param name="theModelID"><s:property value="%{directModel.ID}"/></s:param>
                                <s:param name="uploadedFileName"><s:property value="directModel.drDocRepoModel.dr_doc_name"/></s:param>
                                <s:param name="uploadedFileId"><s:property value="directModel.drDocRepoModel.dr_doc_id"/></s:param>
                                <s:param name="theRecord_id">directModel_ID</s:param>
                                <s:param name="updateHiddenId">directModel_drDocId</s:param>
                                <s:param name="updateHiddenName">directModel_drDocName</s:param>
                                <%--<s:param name="allowedFileTypes">'image/*','application/pdf'</s:param>--%>
                                <s:param name="checkFileFn">checkFileName</s:param>
                            </s:include>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Save/Update</label>
                        <div class="col-md-5" style="min-height: 28px">
                            <button onclick='submitForm("sampleFormId", "uppyUpload_saveDirectModel");'>Save 1 Model 1 File</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Sample 2 <small>(Parent Child)</small></h4>
                </div>
                <div class="panel-body">
                    
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Add Parent</label>
                        <div class="col-md-5" style="min-height: 28px">
                            <button onclick='submitForm("sampleFormId", "uppyUpload_addParentModel"); return false;'>Add Parent</button>
                        </div>
                    </div>
                    <s:iterator value="uppyParentList" var="theParent" status="parentStatus">
                        <div class="row form-horizontal form-group">
                            <label class="col-md-4 control-label">
                                <s:hidden name="uppyParentList[%{#parentStatus.index}].uppy_parent_desc" id="parent%{#parentStatus.index}_desc" value="%{#theParent.uppy_parent_desc}"/>
                                <s:property value="#theParent.uppy_parent_desc"/>
                                <button onclick='addFile("${parentStatus.index}")'>Add Record</button>
                            </label>
                            <div class="col-md-5">
                                <div class="row">
                                    <s:hidden name="uppyParentList[%{#parentStatus.index}].ID" id="parent%{#parentStatus.index}_ID" value="%{#theParent.ID}"/>
                                    <s:iterator value="#theParent.childList" var="theChild" status="childStatus">
                                    <div class="form-horizontal form-group">
                                        <label class="col-md-4 control-label">File Uploading <font class="asterisk">*</font></label>
                                        <div class="col-md-5" style="min-height: 28px">
                                            <s:hidden name="uppyParentList[%{#parentStatus.index}].childList[%{#childStatus.index}].ID" id="parent%{#parentStatus.index}_child%{#childStatus.index}_ID" value="%{#theChild.ID}"/>
                                            <s:hidden id="drDocName_%{#parentStatus.index}_%{#childStatus.index}" name="uppyParentList[%{#parentStatus.index}].childList[%{#childStatus.index}].drDocRepoModel.dr_doc_name"/>
                                            <s:hidden id="drDocId_%{#parentStatus.index}_%{#childStatus.index}" name="uppyParentList[%{#parentStatus.index}].childList[%{#childStatus.index}].dr_doc_id"/>
                                            <s:include value="/base/uppyIncludeSingleFile.jsp">
                                                <s:param name="uploadUrl_">uploadUppy</s:param>
                                                <s:param name="uppyFieldName_">child${parentStatus.index+1}_${childStatus.index+1}</s:param>
                                                <s:param name="uppyHiddenName_">theChild.drDocRepoModel.ID</s:param>
                                                <%--<s:param name="hideArrow">Y</s:param>--%>
                                                <s:param name="drFileCode_">uppySample2_file1</s:param>
                                                <s:param name="uploadParams">uploadRecordId_=<s:property value="%{#theChild.ID}"/></s:param>
                                                <s:param name="uploadedFileName"><s:property value="#theChild.drDocRepoModel.dr_doc_name"/></s:param>
                                                <s:param name="uploadedFileId"><s:property value="#theChild.dr_doc_id"/></s:param>
                                                <s:param name="theRecord_id">parent${parentStatus.index}_child${childStatus.index}_ID</s:param>
                                                <s:param name="updateHiddenId">drDocId_${parentStatus.index}_${childStatus.index}</s:param>
                                                <s:param name="updateHiddenName">drDocName_${parentStatus.index}_${childStatus.index}</s:param>
                                            </s:include>
                                        </div>
                                        <div class="col-md-3">
                                            <button onclick='deleteFile("${parentStatus.index}", "${childStatus.index}"); return false;'>Delete Record</button>
                                        </div>
                                    </div>
                                    </s:iterator>
                                </div>
                            </div>
                        </div>
                    </s:iterator>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Save/Update</label>
                        <div class="col-md-5">
                                    <button onclick='submitForm("sampleFormId", "uppyUpload_saveParentChild");'>Save/Update Parent Child</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Sample 3 <small>(1 model with more than 1 attachment)</small></h4>
                </div>
                <div class="panel-body">
                    
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Action</label>
                        <div class="col-md-5">
                            <button onclick='submitForm("sampleFormId", "uppyUpload_clear1ModelMoreAttachment");'>Clear Record</button>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Model ID</label>
                        <div class="col-md-5">
                            <s:property value="%{moreAttachmentModel.ID}"/>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">File 1</label>
                        <div class="col-md-5">
                            <s:hidden name="moreAttachmentModel.ID" id="moreAttachmentModel_ID" value="%{moreAttachmentModel.ID}"/>
                            <s:hidden id="moreAttachmentModel_drDocName" name="moreAttachmentModel.drDocRepoModel.dr_doc_name"/>
                            <s:hidden id="moreAttachmentModel_drDocId" name="moreAttachmentModel.dr_doc_id"/>
                            <s:include value="/base/uppyIncludeSingleFile.jsp">
                                <s:param name="uploadUrl_">uploadUppy</s:param>
                                <s:param name="uppyFieldName_">moreAttachmentModel_file</s:param>
                                <s:param name="uppyHiddenName_">moreAttachmentModel.drDocRepoModel.ID</s:param>
                                <%--<s:param name="hideArrow">Y</s:param>--%>
                                <s:param name="drFileCode_">moreAttachmentModel_file</s:param>
                                <s:param name="uploadParams">uploadRecordId_=<s:property value="%{moreAttachmentModel.ID}"/></s:param>
                                <s:param name="uploadedFileName"><s:property value="moreAttachmentModel.drDocRepoModel.dr_doc_name"/></s:param>
                                <s:param name="uploadedFileId"><s:property value="moreAttachmentModel.drDocRepoModel.dr_doc_id"/></s:param>
                                <s:param name="theRecord_id">moreAttachmentModel_ID</s:param>
                                <s:param name="updateHiddenId">moreAttachmentModel_drDocId</s:param>
                                <s:param name="updateHiddenName">moreAttachmentModel_drDocName</s:param>
                            </s:include>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">File 2</label>
                        <div class="col-md-5">
                            <s:hidden id="moreAttachmentModel_drDocName2" name="moreAttachmentModel.drDocRepoModel2.dr_doc_name"/>
                            <s:hidden id="moreAttachmentModel_drDocId2" name="moreAttachmentModel.dr_doc_id_2"/>
                            <s:include value="/base/uppyIncludeSingleFile.jsp">
                                <s:param name="uploadUrl_">uploadUppy</s:param>
                                <s:param name="uppyFieldName_">moreAttachmentModel_file2</s:param>
                                <s:param name="uppyHiddenName_">moreAttachmentModel.drDocRepoModel2.ID</s:param>
                                <%--<s:param name="hideArrow">N</s:param>--%>
                                <s:param name="drFileCode_">moreAttachmentModel_file2</s:param>
                                <s:param name="uploadParams">uploadRecordId_=<s:property value="%{moreAttachmentModel.ID}"/></s:param>
                                <s:param name="uploadedFileName"><s:property value="moreAttachmentModel.drDocRepoModel2.dr_doc_name"/></s:param>
                                <s:param name="uploadedFileId"><s:property value="moreAttachmentModel.drDocRepoModel2.dr_doc_id"/></s:param>
                                <s:param name="theRecord_id">moreAttachmentModel_ID</s:param>
                                <s:param name="updateHiddenId">moreAttachmentModel_drDocId2</s:param>
                                <s:param name="updateHiddenName">moreAttachmentModel_drDocName2</s:param>
                            </s:include>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Save/Update</label>
                        <div class="col-md-5" style="min-height: 28px">
                            <button onclick='submitForm("sampleFormId", "uppyUpload_saveMoreAttachmentModel");'>Save 1 Model More Files</button>
                        </div>
                    </div>
                </div>
            </div>
            <s:if test="userRecord != null">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Sample 4 <small>Live Sample (User.java added with "profile_attachment_id")</small></h4>
                </div>
                <div class="panel-body">
                    
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Action</label>
                        <div class="col-md-5">
<%--                            <button onclick='submitForm("sampleFormId", "uppyUpload_clearDirectModel");'>Clear Direct Model</button>
                            <button onclick='submitForm("sampleFormId", "uppyUpload_createDirectModel");'>Create Direct Model</button>--%>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">User ID</label>
                        <div class="col-md-5">
                            <s:property value="%{userRecord.us_user_id}"/>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Profile Image</label>
                        <div class="col-md-5">
                            <s:hidden name="userRecord.ID" id="userRecord_ID" value="%{userRecord.ID}"/>
                            <s:hidden id="userRecord_profileDocName" name="userRecord.drDocRepoModel.dr_doc_name"/>
                            <s:hidden id="userRecord_profile_attachment_id" name="userRecord.profile_attachment_id"/>
                            <s:include value="/base/uppyIncludeSingleFile.jsp">
                                <s:param name="uploadUrl_">uploadUppy</s:param>
                                <s:param name="uppyFieldName_">userProfileAttachment</s:param>
                                <s:param name="uppyHiddenName_">userRecord.drDocRepoModel.ID</s:param>
                                <s:param name="drFileCode_">userRecord_file</s:param>
                                <s:param name="uploadParams">uploadRecordId_=<s:property value="%{userRecord.ID}"/></s:param>
                                <s:param name="uploadedFileName"><s:property value="userRecord.drDocRepoModel.dr_doc_name"/></s:param>
                                <s:param name="uploadedFileId"><s:property value="userRecord.drDocRepoModel.dr_doc_id"/></s:param>
                                <s:param name="theRecord_id">userRecord_ID</s:param>
                                <s:param name="updateHiddenId">userRecord_profile_attachment_id</s:param>
                                <s:param name="updateHiddenName">userRecord_profileDocName</s:param>
                            </s:include>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Save/Update</label>
                        <div class="col-md-5" style="min-height: 28px">
                            <button onclick='submitForm("sampleFormId", "uppyUpload_saveUserProfileAttachment");'>Save User Profile Picture</button>
                        </div>
                    </div>
                </div>
            </div>
            </s:if>
        </form>
    </body>
</html>
