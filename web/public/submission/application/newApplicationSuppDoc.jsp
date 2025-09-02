<%-- 
    Document   : newApplicationSuppDoc
    Created on : Nov 20, 2023, 4:52:02 PM
    Author     : Aiman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="uppy/v1.27.0/uppy.min.js"></script>
        <link rel="stylesheet" href="uppy/v1.27.0/uppy.min.css">
        <title>JSP Page</title>
    </head>
    <body>
        <div class="overflow-hidden p-lg-3">
            <div class="row align-items-center">
                <!--                <div class="col-lg-6"><img class="img-fluid" src="falcon-v3.16.0/public/assets/img/icons/spot-illustrations/21.png" alt="" /></div>
                                <div class="col-lg-6 ps-lg-4 my-5 text-center text-lg-start">
                                    <h3 class="text-primary">Edit me! (welcome.jsp)</h3>
                                    <p class="lead">Create Something Beautiful.</p><a class="btn btn-falcon-primary" href="falcon-v3.16.0/public/documentation/getting-started.html">Getting started</a>
                                </div>-->

                <div class="col-lg-12">
                    <jsp:include page="../public_swiper.jsp"></jsp:include>
                        <h4><strong>Supporting Documents</strong></h4>
                        <hr>
                        <div class="card border-info my-4">
                            <div class="card-body w-100">
                                <p class="fs-1 font-sans-serif" ><strong>Note:</strong></p>
                                <p class="fs-0 font-sans-serif">Maximum upload file size is 25MB</p>
                            </div>
                        </div>
                        <form>
                            <div class="row g-3 my-1 d-flex flex-column">
                                <label class="fs-0 font-sans-serif col-md-6 control-label"><strong>1. Letter of Authorisation /  Appointment (LOA) by Utility Provider / Agencies.</strong></label>
                                <div class="col-md-6">
                                    <s:include value="/base/uppyIncludeSingleFile.jsp">
                                        <s:param name="uploadUrl_">uppyUpload</s:param>
                                        <s:param name="uppyFieldName_">loa</s:param>
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
                            
                            <div class="row g-3 my-1 d-flex flex-column">
                                <label class="fs-0 font-sans-serif col-md-6 control-label"><strong>2. Letter of Commencement (LOC) of Survey by Land Surveyor Board.</strong></label>
                                <div class="col-md-6">
                                    <s:include value="/base/uppyIncludeSingleFile.jsp">
                                        <s:param name="uploadUrl_">uppyUpload</s:param>
                                        <s:param name="uppyFieldName_">loc</s:param>
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

                            <div class="row g-3 my-1 d-flex flex-column">
                                <label class="fs-0 font-sans-serif col-md-6 control-label"><strong>3. Plan showing site of Utility Survey.</strong></label>
                                <div class="col-md-6">
                                    <s:include value="/base/uppyIncludeSingleFile.jsp">
                                        <s:param name="uploadUrl_">uppyUpload</s:param>
                                        <s:param name="uppyFieldName_">sitePlan</s:param>
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

                            <div class="row g-3 my-1 d-flex flex-column">
                                <label class="fs-0 font-sans-serif col-md-6 control-label"><strong>4. Letter of Sitting Approval or Approved Plan on the laying of utility, if any.</strong></label>
                                <div class="col-md-6" style="height: 33px">
                                    <s:include value="/base/uppyIncludeSingleFile.jsp">
                                        <s:param name="uploadUrl_">uppyUpload</s:param>
                                        <s:param name="uppyFieldName_">letter</s:param>
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

                            <div class="row g-3 my-4 d-flex flex-column"> 
                                <label class="fs-0 font-sans-serif col-md-6 control-label"><strong>5. Certificate of Competency / Qualification of registered technician.</strong></label>
                                <div class="col-md-6" >
                                    <s:include value="/base/uppyIncludeSingleFile.jsp">
                                        <s:param name="uploadUrl_">uppyUpload</s:param>
                                        <s:param name="uppyFieldName_">certificate</s:param>
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
                        </div>
                    </form>
                    <hr>
                </div>
                <div class="d-flex justify-content-end mb-md-4">
                    <a href="<s:property value="%{swiperList.get(0)[1]}"/>?selectStep=<s:property value="%{backStep}"/>"><button class="btn btn-secondary mx-1">< Back</button></a>
                    <a href="<s:property value="%{swiperList.get(0)[1]}"/>?selectStep=<s:property value="%{swiperStep+1}"/>"><button class="btn btn-primary mx-1">Next</button></a>
                </div>
            </div>
        </div>
    </body>
</html>

