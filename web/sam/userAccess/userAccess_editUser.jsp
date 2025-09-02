
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Kemaskini Pengguna</title>
        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <script>
                function localValidateForm(form, operation) {
                var errors = new Array();
                    validateRequired(form, errors);
                    if (errors.length > 0) {
                            alert(errors.join('\n'));
                            setFocus(form);
                    }
                    return errors.length > 0 ? false : true;
            }
            function required(){
                this.ab = new Array("us_user_id", "<s:text name='user.id' />");
            }
            
            <s:set name="male"><%=com.impian.pr.model.EmployeeModel.EMPLOYEE_SEX.Lelaki%></s:set>
            
//            function refreshEdit(form, operation) {
//                if (localValidateForm(form, operation)) {
//                    parent.document.getElementById("prForm").action = "processUpdateUserUserAccess";
////                    parent.document.getElementById("prForm").submit();
//                    parent.document.getElementById("prForm").submit();
//                } else return false;
//            }
        </script>
    </head>
    <body><div class="titleFramework">
                <span class="titleText"><s:text name="user.accountProfile" /></span>
                <span class="titleActionTypeText"> | <s:text name="actionType.edit" /></span><br>
            </div>
            <div class="xbox">
        <table class="userAccessTable" width="1000px">
            <!--<form theme="simple" action="processUpdateUserUserAccess">-->
            <form theme="simple" method="post" action="" >
                <s:hidden theme="simple" name="action" />
                <s:hidden theme="simple" name="id" value="%{model.ID}"/>
                <s:hidden theme="simple" name="model.ID" />
                <s:hidden theme="simple" name="model.emp_id" />
                <s:hidden theme="simple" name="currStep_" />
                <s:hidden theme="simple" name="model._operation" />
                <s:hidden theme="simple" name="usId" />
               <%--<s:hidden theme="simple" name="us_ldap" />--%>
            <tr><td> 
                <table>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="150px" align=left><s:text name="user.name"/></td>
                        <td>:</td>
                        <td align="left">${model.us_user_name}</td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="150px" align=left><s:text name="user.id"/><s:text name="required.field"/></td>
                        <td width="3px">:</td>
                        <td align="left"><s:textfield theme="simple" name="us_user_id" value="%{model.us_user_id}"/></td>
                    </tr>

<%--                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="150px" align=left><s:text name="user.ldap"/><s:text name="required.field"/></td>
                        <td>:</td>
                        <td align="left"><s:select name="us_ldap" theme="simple" list="ldapList" listKey="keyData" listValue="valueData" value="%{model.us_ldap}"/></td>
                    </tr>--%>
                    <tr>
                        <td height="12px"><!---------some space----></td>
                        <td height="12px"><!---------some space----></td>
                        <td height="12px"><!---------some space----></td>
                        <td height="12px"><!---------some space----></td>
                    </tr>
<!--                    <tr>
                        <td width="">&nbsp;</td>
                        <td width="" align=left></td>
                        <td >&nbsp;</td>
                        <td><%--<s:submit type="button" cssClass="defaultButton buttonSave" theme="simple" action="processUpdateUserUserAccess" value="%{getText('button.update')}" onclick="return localValidateForm(this.form, 'update')"/></td>--%>
                    </tr>-->
                </table>
                </td>
                <td valign="top" align="right" style="padding-right: 10px;padding-top: 5px;">
                    <s:submit type="button" cssClass="defaultButton buttonSave" theme="simple" action="processUpdateUserUserAccess" value="%{getText('button.update')}" onclick="return localValidateForm(this.form, 'update')"/>&nbsp;
                    <s:submit type="submit" cssClass="defaultButton" theme="simple" action="prevStepUserAccess" value="%{getText('button.back')}"/></td>
                <%--<s:submit type="button" cssClass="defaultButton buttonSave" theme="simple" value="%{getText('button.update')}"  onclick="return refreshEdit(this.form, 'update')"/></td>--%>
            </tr>

           </form>
            
        </table></div>
    </body>
</html>
