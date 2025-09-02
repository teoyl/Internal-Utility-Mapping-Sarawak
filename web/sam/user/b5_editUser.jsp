<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script language="javascript">
            function confirmRemove(groupCode) {
                var answer = confirm(formatText(messageRemoveFromGroup, groupCode));
                var isIE = false;

                isIE = isInternetExplorer();
                return (isIE? event.returnValue = answer : answer);
            }
            
            function removeFromGroup(groupCode, gu_id) {
                if (confirmRemove(groupCode)) {
                    $("#remove_gu_id").val(gu_id);
                    submitForm_bshor("updateUserForm", "processRemoveUser")
                }
                return false;
            }
            
            function sendEmail() {
                divSubmitForm("processResetPassword?ajax=true&resetUserId="+$("#us_id").val(), "resetPasswordForm", "sendEmailDiv");
            }
            
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
                this.ac = new Array("us_password", "<s:text name='user.password' />");
                this.ad = new Array("us_email", "<s:text name='user.emailAddress' />");
            }
            
            $(document).ready(function() {                
                
            });
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="system.name"/> - <s:text name="user.Internal" /> - <s:text name="actionType.edit" /></title>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form theme="simple" action="processUpdateUser" method="post" id="updateUserForm">
            <s:hidden name="action" />
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <s:hidden id="us_id" name="us_id" value="%{model.us_id}"/>
            <div class="card mb-3">
                <div class="card-header bg-light">
                    <h5><s:text name="user"/>&nbsp;<small class="fw-normal text-600"><s:text name="button.edit"/></small></h5>
                </div>
                <div class="card-body">
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="user.id"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="us_user_id" value="%{model.us_user_id}" cssClass="form-control form-control-sm"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="user.name"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="us_user_name" value="%{model.us_user_name}" size="60" cssClass="form-control form-control-sm"/>
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
                            <s:select cssClass="form-control form-control-sm sds-dropdown" name="us_ldap" theme="simple" list="ldapList" listKey="keyData" listValue="valueData" value="%{model.us_ldap}"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="user.status"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select cssClass="form-control form-control-sm sds-dropdown" name="us_status" theme="simple" list="statusList" listKey="keyData" listValue="valueData" value="%{model.us_status}"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="forgotPassword"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <button class="btn btn-sm btn-primary" type="button" name="action:processUpdateUser" id="processUpdateUser" onclick="sendEmail();"><i class="fas fa-share"></i> <span class="ms-1"><s:text name="button.sendEmail"/></span></button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="card mb-3">
                <div class="card-header bg-light">
                    <h5><s:text name="userGroup"/>&nbsp;<small class="fw-normal text-600"><s:text name="button.edit"/></small></h5>
                </div>
                <s:hidden name="remove_gu_id" id="remove_gu_id"/>
                <div class="card-body">
                    <table width="100%" class="table table-sm table-striped fs--1">
                        <tr class="bg-200 text-900">
                            <th><s:text name="group.code"/></th>
                            <th><s:text name="group.name"/></th>
                            <th><s:text name="user.removeFromGroup"/></th>
                        </tr>
                    <s:iterator value="model.groupUserList" var="groupUser" status="guStatus">
                        <tr>
                            <td>
                                <s:hidden name="model.groupUserList[%{#guStatus.index}].ID" value="%{#groupUser.ID}"/>
                                <s:hidden name="model.groupUserList[%{#guStatus.index}].userGroup.group_code" value="%{#groupUser.userGroup.group_code}"/>
                                <s:hidden name="model.groupUserList[%{#guStatus.index}].userGroup.group_name" value="%{#groupUser.userGroup.group_name}"/>
                                <s:property value="#groupUser.userGroup.group_code"/>
                            </td>
                            <td><s:property value="#groupUser.userGroup.group_name"/></td>
                            <td><button class="btn btn-sm btn-primary" type="button" name="action:processUpdateUser" id="processUpdateUser" onclick="removeFromGroup('<s:property value="#groupUser.userGroup.group_name" escapeJavaScript="true"/>', '<s:property value="#groupUser.ID" escapeJavaScript="true"/>')"><i class="fa fa-trash"></i> <span class="ms-1"><s:text name="button.remove"/></span></button> </td>
                        </tr>
                    </s:iterator>
                    </table>
                </div>
            </div>
                        
            <div class="card mb-3">
                <div class="card-body">
                    <div class="row">
                        <div class="col text-end">
                            <button class="btn btn-sm btn-primary" type="submit" name="action:processUpdateUser" id="processUpdateUser" onclick="return localValidateForm(this.form, 'update')"><i class="fa fa-save"></i> <span class="ms-1">Save</span></button>     
                            <button class="btn btn-sm btn-falcon-default" type="submit" name="action:cancelUser" id="cancelUser"><i class="fa fa-times"></i><span class="ms-1">Cancel</span></button>     
                        </div>
                    </div>      
                </div>
            </div>
        </form>
        <form action="processResetPassword" method="post" id="resetPasswordForm"/>
        <div id="sendEmailDiv" class="hidden"/>
    </body>
</html>