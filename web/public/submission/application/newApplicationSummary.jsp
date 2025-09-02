<%-- 
    Document   : newApplicationSummary
    Created on : Nov 21, 2023, 3:55:21 PM
    Author     : Aiman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="include/newApplicationSummary.css" rel="stylesheet"/>
        <link href="include/public_swiper.css" rel="stylesheet"/>
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

                <div class="card-group my-2">
                    <div class="col-lg-3 col-md-12 col-sm-12 my-sm-2">
                        <div class="card text-white card-custom d-flex flex-row h-100">
                            <div class="card-body border-end">
                                <!--<p class="card-text"><small class="text-body-primary">Last updated 3 mins ago</small></p>-->
                                <p class="card-label">Ref. No.</p>
                                <h5 class="card-title text-white my-2">
                                    UPIS/000100/2024
                                </h5>

                                <p class="card-label">USJ. No.</p>
                                <h5 class="card-title text-white my-2">
                                    -
                                </h5>

                                <p class="card-label">Survey Type</p>
                                <h5 class="card-title text-white my-2">
                                    @
                                </h5>

                                <p class="card-label">Division</p>
                                <h5 class="card-title text-white my-2">
                                    @
                                </h5>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-3 col-md-12 col-sm-12 my-sm-2">

                        <div class="card text-white card-custom d-flex flex-row h-100">
                            <div class="card-body">
                                <p class="card-label">Application Submission Date</p>
                                <h5 class="card-title text-white my-2">
                                    @
                                </h5>

                                <p class="card-label">Submission Job Issuance Date</p>
                                <h5 class="card-title text-white my-2">
                                    @
                                </h5>
                            </div>
                        </div>

                    </div>

                    <div class="card col-lg-6 col-md-12 col-sm-12 my-sm-2">
                        <div class="card-body d-flex flex-column align-self-start h-100">
                            <p class="card-label">Record Status</p>
                            <s:if test="unfit == true">
                                <h5 class="card-title my-2">
                                    <i class="fa fa-times-circle text-danger"></i> UNFIT
                                </h5>
                            </s:if>
                            <s:else>
                                <h5 class="card-title my-2">
                                    <i class="fa fa-check-circle text-success"></i> APPROVED
                                </h5>
                            </s:else>
                            <p class="card-label">Description</p>
                            <h5 class="card-title my-2">
                                @
                            </h5>
                            <p class="card-label">Locality</p>
                            <h5 class="card-title my-2">
                                @
                            </h5>

                            <div class="mt-auto flex-row align-self-end">
                                <s:if test="!unfit"> 
                                    <a class="mx-1" href="#">View Offer Letter</a>
                                </s:if>
                                <a class="mx-1" href="#">View Application</a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-12 my-1">
                    <s:if test="unfit">
                        <div class="font-sans-serif fs-0 border border-custom bg-white p-x1 rounded border-2">Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident.</div>
                    </s:if>

                    <div class="mt-2 d-flex flex-row justify-content-between">
                        <button class="btn-clear mt-2 fs-1 text-black" type="button" data-bs-toggle="collapse" data-bs-target="#suppDoc" aria-expanded="false" aria-controls="suppDoc">Supporting Documents
                            <s:if test="unfit == true">
                                <i class="fa fa-exclamation-triangle text-warning" aria-hidden="true"></i>
                            </s:if>
                        </button>
                        <button class="btn btn-falcon-default ms-sm-2 mt-2" type="button" data-bs-toggle="collapse" data-bs-target="#suppDoc" aria-expanded="false" aria-controls="suppDoc"><i class="fa fa-caret-down" aria-hidden="true"></i></button>
                    </div>
                    <hr>
                    <div class="collapse" id="suppDoc">
                        <s:iterator value="appDocList" var="docSet" status="docSetStatus">
                            <div class ="row">
                                <div class="col-lg-5 mx-2 col-md-11 border-end border-2 d-flex flex-row">
                                    <div class="col-lg-1 col-md-11 d-flex flex-column align-items-center mt-lg-2">
                                        <s:if test="%{#docSet.docUnfit}"> 
                                            <i class="fa fa-times-circle text-danger"></i>
                                        </s:if>
                                        <s:else>
                                            <i class="fa fa-check-circle text-success"></i> 
                                        </s:else>
                                    </div>
                                    <div class="font-sans-serif col-lg-11 col-md-11 flex-column align-items-center justify-content-center">
                                        <p class='mb-2 mx-1 fw-medium text-black'><s:property value="%{#docSetStatus.index+1}"/>. <s:property value="%{#docSet.description}" /></p>
                                        <a class="ml-1" href='#'>Document</a>
                                    </div>
                                </div>
                                <div class="col-lg-6 col-md-12 mx-2">
                                    <p class='font-sans-serif fw-medium fst-italic text-black'>Agency's Remarks</p>
                                    <p class='font-sans-serif mb-2'><s:property value="%{#docSet.agency_remarks}" /></p>
                                </div>
                            </div>
                            <hr>
                        </s:iterator>
                    </div>
                </div>

                <div class="col-lg-12 my-2">
                    <div class="d-flex flex-row justify-content-between">
                        <button class="btn-clear mt-2 fs-1 text-black" type="button" data-bs-toggle="collapse" data-bs-target="#history" aria-expanded="false" aria-controls="history">History</button>
                        <button class="btn btn-falcon-default ms-sm-2 mt-2" type="button" data-bs-toggle="collapse" data-bs-target="#history" aria-expanded="false" aria-controls="history"><i class="fa fa-caret-down" aria-hidden="true"></i></button>
                    </div>
                    <hr>
                    <div class="collapse" id="history">
                        <%--<jsp:include page="../vertical_swiper.jsp"></jsp:include>--%>
                        <jsp:include page="../history_horizontal.jsp"></jsp:include>
                    </div>
                </div>

            </div>
        </div>
    </body>
</html>
