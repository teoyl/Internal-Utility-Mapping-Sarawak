<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/lookup.js"></script>
        <script type="text/javascript" src="pages/scripts/controls.js"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
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
        <jsp:include page="/pages/base/b4_actionError.jsp"/>
        <%--div class="row">
            <div class="col-md-4">
                <h3 class="title-v3"><s:text name="user.Internal" /> <small><s:text name="actionType.edit" /></small></h3>
            </div>
        </div--%>

        <form theme="simple" action="processUpdateUser" method="post">
            <s:hidden name="action" />
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <s:hidden name="us_id" value="%{model.us_id}"/>

            <div class=" kt-portlet">
                <div class="kt-portlet__head">
                    <div class="kt-portlet__head-label">
                        <h3 class="kt-portlet__head-title">
                            <s:text name="user"/>&nbsp;<small><s:text name="button.edit"/></small>
                        </h3> 
                    </div>
                </div>
                <div class="kt-portlet__body">
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="user.id"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="us_user_id" value="%{model.us_user_id}" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="user.name"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="us_user_name" value="%{model.us_user_name}" size="60" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="user.emailAddress"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="us_email" value="%{model.us_email}" size="60" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="user.ldap"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select cssClass="form-control sds-dropdown" name="us_ldap" theme="simple" list="ldapList" listKey="keyData" listValue="valueData" value="%{model.us_ldap}"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="user.status"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select cssClass="form-control sds-dropdown" name="us_status" theme="simple" list="statusList" listKey="keyData" listValue="valueData" value="%{model.us_status}"/>
                        </div>
                    </div>
                </div>
                <div class="kt-portlet__foot">
                    <div class="kt-form__actions">
                        <div class="row">
                            <div class="col-md-12 text-right">
                                <button class="btn btn-brand" type="submit" name="action:processUpdateUser" id="processUpdateUser" onclick="return localValidateForm(this.form, 'update')"><i class="fa fa-save"></i>Save</button>     
                                <button class="btn btn-outline-brand" type="submit" name="action:cancelUser" id="cancelUser"><i class="fa fa-close"></i>Cancel</button>     
                                <%--s:submit type="button" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="processUpdateUser" value="Save" onclick="return localValidateForm(this.form, 'update')"/>
                                <s:submit type="button" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="cancelUser" value="Cancel"/--%>
                            </div>
                        </div>     
                    </div>  
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
    </body>
</html>