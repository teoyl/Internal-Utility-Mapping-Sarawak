<%-- 
    Document   : application_new
    Created on : Nov 20, 2023, 1:34:50 PM
    Author     : Aiman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
                    <h4><strong>Project Information</strong></h4>
                    <hr>
                    <div class="card border-info my-4">
                        <div class="card-body w-100">
                            <p class="fs-1 font-sans-serif"><strong>Note:</strong></p>
                            <p class="fs-0 font-sans-serif">This application must be completed clearly and accurately. All documents mentioned in Supporting Document section are required to be attached. All incomplete, unclear, inaccurate and without supporting documents applications will be rejected. </p>
                        </div>
                    </div>
                    <form>
                         <h5><strong>Project</strong></h5>
                        
                        <div class="row g-3 my-1 align-items-center">
                            <div class="col-3">
                              <label for="divisionInput" class="col-form-label">Division</label>
                            </div>
                            <div class="col-6">
                              <input type="input" id="divisionInput" class="form-control" aria-describedby="divisionSelection">
                            </div>
                        </div>
                        
                        <hr class="divider">
                        <h5><strong>Locality</strong></h5>
                        
                        <div class="row g-3 my-1 align-items-center">
                            <div class="col-3">
                              <label for="divisionInput" class="col-form-label">Land District</label>
                            </div>
                            <div class="col-6">
                              <input type="input" id="landDistrict" class="form-control" aria-describedby="divisionSelection">
                            </div>
                        </div>
                        <div class="row g-3 my-1 align-items-center">
                            <div class="col-3">
                              <label for="divisionInput" class="col-form-label">Locality/Land Description</label>
                            </div>
                            <div class="col-6">
                              <input type="input" id="landDesc" class="form-control" aria-describedby="divisionSelection">
                            </div>
                        </div>
                        
                        <hr class="divider">
                        <h5><strong>Contact Details</strong></h5>
                        
                        <div class="row g-3 my-1 align-items-center">
                            <div class="col-6">
                              <label for="divisionInput" class="col-form-label">Survey Organization</label>
                            </div>
                        </div>
                        <div class="row g-3 my-1 align-items-center">
                            <div class="col-3">
                              <label for="oicInput" class="col-form-label">Officer In Charge (OIC)</label>
                            </div>
                            <div class="col-6">
                              <input type="input" id="oicInput" class="form-control" aria-describedby="oicInput">
                            </div>
                        </div>
                        <div class="row g-3 my-1 align-items-center">
                            <div class="col-3">
                              <label for="oicEmailInput" class="col-form-label">OIC Email</label>
                            </div>
                            <div class="col-6">
                              <input type="email" id="oicEmailInput" class="form-control" aria-describedby="oicEmailInput">
                            </div>
                        </div>
                        <div class="row g-3 my-1 align-items-center">
                            <div class="col-3">
                              <label for="oicEmailInput" class="col-form-label">OIC Contact No.</label>
                            </div>
                            <div class="col-6">
                              <input type="input" id="oicEmailInput" class="form-control" aria-describedby="oicEmailInput">
                            </div>
                        </div>
                        <div class="row g-3 my-1 align-items-center">
                            <div class="col-3">
                              <label for="oicEmailInput" class="col-form-label">Organization Fax No. </label>
                            </div>
                            <div class="col-6">
                              <input type="input" id="oicEmailInput" class="form-control" aria-describedby="oicEmailInput">
                            </div>
                        </div>
                    </form>
                    <hr>
                </div>
            </div>
            <div class="d-flex justify-content-end mb-md-4">
                <!--<a href="<s:property value="%{swiperList.get(0)[1]}"/>?selectStep=<s:property value="%{backStep}"/>"><button class="btn btn-secondary mx-1">< Back</button></a>-->
                <a href="<s:property value="%{swiperList.get(0)[1]}"/>?selectStep=<s:property value="%{swiperStep+1}"/>"><button class="btn btn-primary mx-1">Next</button></a>
            </div>
        </div>
    </body>
</html>
