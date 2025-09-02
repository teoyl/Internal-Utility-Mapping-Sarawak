<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="row flex-center min-vh-100 py-6">
    <div class="col-sm-10 col-md-8 col-lg-8 col-xl-6 col-xxl-5"><a class="d-flex flex-center mb-4" href="<s:url value="/"/>"><img class="me-2" src="<s:text name="system.icon"/>" alt="" width="58" /><span class="font-sans-serif fw-bolder fs-5 d-inline-block"><s:text name="system.name"/></span></a>
        <div class="card">
            <div class="card-body p-4 p-sm-5">                 
                <jsp:include page="/pages/base/actionError.jsp" />
                <h5 class="text-center"><s:text name="button.resetPwd" /></h5>
                <p class="text-center"><s:text name="lbl.resetPassword.setNewPassword"/></p>
                <form class="mt-3" action="activateNewPassword" method="post">
                    <div class="row mb-3">
                        <label class="col-4 col-form-label"><s:text name="user.id"/></label>
                        <div class="col-8">
                            <input type="text" readonly class="form-control-plaintext" value="<s:property value="model.us_user_id"/>">
                            <s:hidden theme="simple" name="us_id" value="%{model.us_id}"/>
                            <s:hidden theme="simple" name="us_user_id" value="%{model.us_user_id}"/>
                            <s:hidden theme="simple" name="activationCode" value="%{activationCode}"/>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <label class="col-4 col-form-label"><s:text name="user.newPassword"/><jsp:include page="/pages/base/requiredField.jsp"/></label>
                        <div class="col-8">
                            <s:textfield style="display:none" theme="simple" name="v1" cssClass="input-md form-control"/>
                            <s:password cssClass="requiredField input-md form-control" theme="simple" name="model.us_password" id="us_password" value="" size="50"/>
                            <p class="form-text"><s:text name="lbl.password.note"/></p>
                        </div>                        
                    </div>
                    <div class="row mb-3">
                        <label class="col-4 col-form-label"><s:text name="user.confirmPassword"/><jsp:include page="/pages/base/requiredField.jsp"/></label>
                        <div class="col-8">
                            <s:password cssClass="requiredField input-md form-control" theme="simple" name="confirmPassword" id="confirmPassword" value="" size="50"/>
                        </div>                  
                    </div>
                        
                    <button class="btn btn-primary d-block w-100 mt-3" type="submit" name="resetPassword" id="resetPassword" onclick="return localValidateForm(this.form);"><s:text name="button.resetPwd"/></button>
                </form>
                
                <div class="mt-3 text-center">
                    <a class="fs--1 text-600" href="<s:url value="/"/>"><s:text name="system.toMain"/> <span class="d-inline-block ms-1">&rarr;</span></a>
                </div>
            </div>
        </div>
    </div>
</div>
            
<script type="text/javascript">
    function localValidateForm(form, operation) {
        var errors = new Array();

        validateRequired(form, errors);

    <%--ICTU GUIDE: PASSWORD ATLEAST 12 CHARACTERS--%>
            passwordCheck(form.us_password, "Password", errors,8,16);
            if ( trim(form.confirmPassword.value) != trim(form.us_password.value) ){
                errors[errors.length] = "<s:text name='errors.passwordNotMatch'/>";
            }
    <%--ICTU GUIDE: PASSWORD CANNOT BE SAME AS USER ID--%>
            if ( trim(form.us_user_id.value).toUpperCase() == trim(form.us_password.value).toUpperCase() ) {
                errors[errors.length] = "<s:text name='errors.passwordSameAsUserID'/>";
            }

            if (errors.length > 0) {
                alert(errors.join('\n'));
                setFocus(form);
            }
            return errors.length > 0 ? false : true;
        }
        function required(){
            this.aa = new Array("us_password", "<s:text name="user.newPassword"/>");
            this.ab = new Array("confirmPassword", "<s:text name="user.confirmPassword"/>");
        }
</script>