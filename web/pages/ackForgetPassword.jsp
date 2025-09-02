<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<div class="row flex-center min-vh-100 py-6 text-center">
    <div class="col-sm-10 col-md-8 col-lg-6 col-xl-5 col-xxl-4"><a class="d-flex flex-center mb-4" href="<s:url value="/"/>"><img class="me-2" src="<s:text name="system.icon"/>" alt="" width="58" /><span class="font-sans-serif fw-bolder fs-5 d-inline-block"><s:text name="system.name"/></span></a>
        <div class="card">
            <div class="card-body p-4 p-sm-5"> 
                <h5 class="mb-0"><s:text name="forgotPassword" /></h5>
                <p class="mt-3"><s:text name="lbl.resetPassword.checkMail"/></p>
                <a class="btn btn-primary d-block w-100 mt-3" href="<s:url value="/"/>"><s:text name="system.toMain"/></a>
            </div>
        </div>
    </div>
</div>