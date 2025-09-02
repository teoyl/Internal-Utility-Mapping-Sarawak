<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
                
<div class="row flex-center min-vh-100 py-6 text-center">
    <div class="col-sm-10 col-md-8 col-lg-6 col-xl-5 col-xxl-4"><a class="d-flex flex-center mb-4" href="<s:url value="/"/>"><img class="me-2" src="<s:text name="system.icon"/>" alt="" width="58" /><span class="font-sans-serif fw-bolder fs-5 d-inline-block"><s:text name="system.name"/></span></a>
        <div class="card">
            <div class="card-body p-4 p-sm-5">
                <jsp:include page="/pages/base/actionError.jsp"></jsp:include>    
                <h5 class="mb-0"><s:text name="forgotPassword" /></h5><small><s:text name="lbl.resetPassword.note"/></small>

                <form class="mt-4" id="resetForm" action="processResetPassword" method="post">
                    <div class="mb-3">
                        <input class="input-md form-control" type="text" name="resetUserId" id="resetUserId" value="" size="50" placeholder="<s:text name="user.id"/>" />
                    </div>            
                    <div class="mb-3">
                        <input class="input-md form-control" type="email" name="resetEmail" id="resetEmail" value="" size="50" placeholder="<s:text name="user.emailAddress"/>" />
                    </div>
                    <div class="mb-3"></div>
                    <button class="btn btn-primary d-block w-100 mt-3" type="button" name="" id="actionName" onclick="return localValidateForm(this.form);"><i class="far fa-save"></i> <s:text name="button.submit"/></button>
                </form>
                <!--<a class="fs--1 text-600" href="#!">I can't recover my account using this page<span class="d-inline-block ms-1">&rarr;</span></a>-->
            </div>
        </div>
    </div>
</div>
        
<script language="javascript">
    function localValidateForm(form) {
        if ($("#resetUserId").val().trim()==="" && $("#resetEmail").val().trim()==="") {
            alert("<s:text name="lbl.resetPassword.required"/>");
            setFocus(form);
            return false;
        } else {
            form.submit();
        }
    }
</script>