<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script language="javascript">
            function localValidateForm(form, operation) {
                var errors = new Array();
                validateRequired(form, errors);
                if (errors.length > 0) {
                    alert(errors.join('\n'));
                    setFocus(form);
                }
                return errors.length > 0 ? false : true;
            }
            function required() {
                this.aa = new Array("us_user_id", "<s:text name='user.id' />");
                this.ab = new Array("us_user_name", "<s:text name='user.name' />");
                if (document.getElementById("us_ldap").value === "N"){
                    this.ac = new Array("us_password", "<s:text name='user.password' />");
                    this.ad = new Array("us_email", "<s:text name='user.emailAddress' />");
                }else{
                    this.ac = new Array("us_email", "<s:text name='user.emailAddress' />");
                }
            }
            
            function noPassword(){
                if (document.getElementById("us_ldap").value === "N"){
                  $('.password').addClass('required');
                }else{
                  $('.password').removeClass('required');

                }
            }
            
            $(document).ready(function() {
                if (document.getElementById("us_ldap").value === "N"){
                  $('.password').addClass('required');
                }else{
                  $('.password').removeClass('required');
                }
            });
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="system.name"/> - <s:text name="user.Internal" /> - <s:text name="actionType.add" /></title>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form theme="simple" action="processInsertUser" class="prForm" method="post">
            <s:hidden name="action" />
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <div class="card">
                <div class="card-header bg-light">
                    <h5><s:text name="user"/>&nbsp;<small class="fw-normal text-600"><s:text name="button.add"/></small></h5>
                </div>
                <div class="card-body">
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="user.id"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="us_user_id" cssClass="form-control form-control-sm"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="user.name"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="us_user_name" size="60" cssClass="form-control form-control-sm"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="user.emailAddress"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="us_email" value="%{model.us_email}" size="60" cssClass="form-control form-control-sm"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="user.ldap"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select cssClass="form-control form-control-sm sds-dropdown" name="us_ldap" theme="simple" list="ldapList" listKey="keyData" listValue="valueData" value="%{model.us_ldap}" onchange="noPassword()"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="user.password"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:password theme="simple" name="us_password" cssClass="form-control form-control-sm" />
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="user.status"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select cssClass="form-control form-control-sm sds-dropdown" name="us_status" theme="simple" list="statusList" listKey="keyData" listValue="valueData" value="%{model.us_status}"/>
                        </div>
                    </div>
                    <div class="row mt-3">
                        <div class="col text-end">
                            <button class="btn btn-sm btn-primary" type="submit" name="action:processInsertUser" id="processInsertUser" onclick="return localValidateForm(this.form, 'insert')"><i class="fa fa-save"></i> <span class="ms-1"><s:text name="button.save"/></span></button>     
                            <button class="btn btn-sm btn-falcon-default" type="submit" name="action:cancelUser" id="cancelUser"><i class="fa fa-times"></i><span class="ms-1"><s:text name="button.cancel"/></span></button> 
                        </div>
                    </div>
                        
                </div>
            </div>
        </form>
    </body>
</html>