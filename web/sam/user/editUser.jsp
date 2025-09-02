di<%@ page language="java" contentType="text/html; charset=UTF-8"
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
        <%--<s:head />--%>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <%--div class="row">
            <div class="col-md-4">
                <h3 class="title-v3"><s:text name="user.Internal" /> <small><s:text name="actionType.edit" /></small></h3>
            </div>
        </div--%>

        <form theme="simple" action="processUpdateUser" method="post" id="updateUserForm">
            <s:hidden name="action" />
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <s:hidden id="us_id" name="us_id" value="%{model.us_id}"/>


            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4><s:text name="user"/>&nbsp;<small><s:text name="button.edit"/></small></h4>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <label class="col-md-4 control-label"><s:text name="user.id"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="us_user_id" value="%{model.us_user_id}" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label"><s:text name="user.name"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="us_user_name" value="%{model.us_user_name}" size="60" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label"><s:text name="user.emailAddress"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="us_email" value="%{model.us_email}" size="60" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label"><s:text name="user.ldap"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select cssClass="form-control sds-dropdown" name="us_ldap" theme="simple" list="ldapList" listKey="keyData" listValue="valueData" value="%{model.us_ldap}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label"><s:text name="user.status"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select cssClass="form-control sds-dropdown" name="us_status" theme="simple" list="statusList" listKey="keyData" listValue="valueData" value="%{model.us_status}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label"><s:text name="forgotPassword"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <button class="btn btn-primary" type="button" name="action:processUpdateUser" id="processUpdateUser" onclick="sendEmail();"><i class="fa fa-mail-forward"></i><s:text name="button.sendEmail"/></button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4><s:text name="userGroup"/>&nbsp;<small><s:text name="button.edit"/></small></h4>
                </div>
                <s:hidden name="remove_gu_id" id="remove_gu_id"/>
                <div class="panel-body">
                    <table width="100%">
                        <tr>
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
                            <td><button class="btn btn-primary" type="button" name="action:processUpdateUser" id="processUpdateUser" onclick="removeFromGroup('<s:property value="#groupUser.userGroup.group_name" escapeJavaScript="true"/>', '<s:property value="#groupUser.ID" escapeJavaScript="true"/>')"><i class="fa fa-trash"></i><s:text name="button.remove"/></button> </td>
                        </tr>
                    </s:iterator>
                    </table>
                </div>
            </div>
                        
            <div class="row">
                <div class="col-md-12 text-right">
                    <button class="btn btn-primary" type="submit" name="action:processUpdateUser" id="processUpdateUser" onclick="return localValidateForm(this.form, 'update')"><i class="fa fa-save"></i>Save</button>     
                    <button class="btn btn-default" type="submit" name="action:cancelUser" id="cancelUser"><i class="fa fa-close"></i>Cancel</button>     
                    <%--s:submit type="button" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="processUpdateUser" value="Save" onclick="return localValidateForm(this.form, 'update')"/>
                    <s:submit type="button" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="cancelUser" value="Cancel"/--%>
                </div>
            </div>         

            <%--div class="panel panel-default ">
                <div class="panel-heading ">
                    <h3 class="panel-title"> 
                        <span class="titleText"><s:text name="user.Internal" /></span>
                        <span class="titleActionTypeText"> | <s:text name="actionType.edit" /></span>
                    </h3>
                </div>
                <div class="panel-body">
                    <s:hidden name="action" />
                    <s:hidden name="us_id" value="%{model.us_id}"/>
                    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form table borderless">
                        <tr>
                            <td>
                                <s:submit type="button" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="processUpdateUser" value="Save" onclick="return localValidateForm(this.form, 'update')"/>
                                <s:submit type="button" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="cancelUser" value="Cancel"/>
                            </td>
                        </tr>
                    </table>
                    <table class="table borderless">
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="user.id"/><s:text name="required.field"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:textfield theme="simple" name="us_user_id" value="%{model.us_user_id}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="user.name"/><s:text name="required.field"/></td>
                            <td>:</td>
                            <td align="left"><s:textfield theme="simple" name="us_user_name" value="%{model.us_user_name}" size="60" cssClass="input-md form-control"/></td>
                        </tr>
                        <!--<tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="user.division"/></td>
                            <td>:</td>
                            <td align="left">&nbsp;</td>
                            <td align="left"><s:select name="us_division" theme="simple" list="divisionList" listKey="code_1" listValue="code_desc" value="%{model.us_division}"/></td>
                        </tr>-->
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="user.role"/></td>
                            <td>:</td>                        
                            <td align="left"><s:select name="role_id" theme="simple" list="roleList" listKey="code_id" listValue="code_desc" value="%{model.role_id}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="user.ldap"/><s:text name="required.field"/></td>
                            <td>:</td>
                            <td align="left"><s:select name="us_ldap" theme="simple" list="ldapList" listKey="keyData" listValue="valueData" value="%{model.us_ldap}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="user.status"/><s:text name="required.field"/></td>
                            <td>:</td>
                            <td align="left"><s:select name="us_status" theme="simple" list="statusList" listKey="keyData" listValue="valueData" value="%{model.us_status}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="user.emailAddress"/><s:text name="required.field"/></td>
                            <td>:</td>
                            <td align="left"><s:textfield theme="simple" name="us_email" value="%{model.us_email}" size="60" cssClass="input-md form-control"/></td>
                        </tr>
                    </table>
                </div>
            </div--%>
        </form>
        <form action="processResetPassword" method="post" id="resetPasswordForm"/>
        <div id="sendEmailDiv" class="hidden"/>
    </body>
</html>