<%-- 
    Document   : newApplicationAcknowledgement.jsp
    Created on : Nov 20, 2023, 1:35:17 PM
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
                        <h4><strong>Acknowledgement</strong></h4>
                        <hr>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault">
                            <label class="form-check-label mx-3 fs-0 font-sans-serif" for="flexCheckDefault">
                                Hereby, I [Name], [IC] acknowledge the information provided in this application form, together with the supporting documents are genuine. The Land and Survey Department reserves the rights and authority to consider this application.
                            </label>
                        </div>
                        <div class="card border-info my-4 col-lg-8">
                            <div class="card-body w-100">
                                <p class="fs-1 font-sans-serif"><strong>Note:</strong></p>
                                <p class="fs-0 font-sans-serif">A digital certificate file is required to prove the authenticity of this application. </p>
                                <p class="fs-0 font-sans-serif">Accepted file format is p12.</p>
                            </div>
                        </div>
                        <div class="col-lg-12">
                            <h5></h5>
                            <form>
                                <div class="col-md-5 my-2">
                                    <label class="fs-1 col-md-12 control-label fs-1"><strong>Upload Digital Certificate</strong></label>
                                    <label class="col-md-12 control-label"><em>(Accepted file format is p12)</em></label>
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
                            <div class="row g-3 my-1 align-items-center">
                                <div class="col-3">
                                    <label for="divisionInput" class="col-form-label fs-0">Digital Certificate Password</label>
                                </div>
                                <div class="col-6">
                                    <input type="input" id="divisionInput" class="form-control" aria-describedby="divisionSelection">
                                </div>
                            </div>
                        </form>
                    </div>
                    <hr>
                </div>
            </div>

            <div class="d-flex justify-content-end mb-md-4">
                <a href="<s:property value="%{swiperList.get(0)[1]}"/>?selectStep=<s:property value="%{backStep}"/>"><button class="btn btn-secondary mx-1">< Back</button></a>
                <a href="<s:property value="%{swiperList.get(0)[1]}"/>?selectStep=<s:property value="%{swiperStep+1}"/>"><button class="btn btn-primary mx-1">Next</button></a>
            </div>
        </div>
    </body>
</html>

