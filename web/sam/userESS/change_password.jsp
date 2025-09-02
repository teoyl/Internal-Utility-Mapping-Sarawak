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
<script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
<script language="javascript">
function localValidateForm(form, operation) {

        var errors = new Array();
	validateRequired(form, errors);

        <%--ICTU GUIDE: PASSWORD ATLEAST 12 CHARACTERS--%>
        passwordCheck(form._strNew_us_password, "Kata Laluan Baru", errors,12,16);
        if ( trim(form._strNew_us_password.value) != trim(form._strComfirm_new_us_password.value) ){
             errors[errors.length] = "<s:text name='accountActivate.error.comfirmPasswordX'/>";
        }
        <%--ICTU GUIDE: PASSWORD CANNOT BE SAME AS USER ID--%>
        if ( trim(form.us_user_id.value).toUpperCase() == trim(form._strNew_us_password.value).toUpperCase() ) {
             errors[errors.length] = "<s:text name='accountActivate.errors.passwordSameAsUserID'/>";
        }
        
        if (errors.length > 0) {
		alert(errors.join('\n'));
		setFocus(form);
	}
	return errors.length > 0 ? false : true;

}
function required(){
    this.aa = new Array("us_user_id", "<s:text name='user.id' />");
    this.ac = new Array("us_password", "<s:text name='user.password' />");
    this.ad = new Array("_strNew_us_password", "<s:text name='user.newPassword' />");
    this.ae = new Array("_strComfirm_new_us_password", "<s:text name='accountActivate.confirmNewPassword' />");
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="system.shortname"/> - <s:text name="user.Internal" /> - <s:text name="actionType.edit" /></title>
<%--<s:head />--%>
</head>
<body>
    <form method="post" theme="simple" id="userForm" action="processChangePasswordUserESS">
            <div class="titleFramework">
                <span class="titleText"><s:text name="changePassword" /></span>
                <span class="titleActionTypeText"> | <s:text name="actionType.edit" /></span><br>
            </div>
            <s:hidden name="us_id" value="%{model.us_id}"/>
            <div class="xbox">
                <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
                    <tr>
                        <td align="right">
                            <s:submit type="button" cssClass="defaultButton buttonSave" theme="simple" action="processChangePasswordUserESS" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>
                            <s:submit type="button" cssClass="defaultButton buttonCancel" theme="simple" action="cancelUserESS" value="Cancel"/>
                        </td>
                    </tr>
                </table>
                <table>
                    <s:hidden name="action" />
                    <tr>
                        <td colspan="4"><jsp:include page="/pages/base/actionError.jsp"></jsp:include> </td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="250px" align=left><s:text name="user.id"/></td>
                        <td width="3px">:</td>
                        <td align="left"><s:hidden name="us_user_id" value="%{model.us_user_id}"/><s:property value="%{model.us_user_id}"/></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="150px" align=left><s:text name="user.currentPassword"/><s:text name="required.field"/></td>
                        <td>:</td>
                        <td align="left"><s:password cssClass="requiredField" theme="simple" name="us_password" /></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="150px" align=left><s:text name="user.newPassword"/><s:text name="required.field"/></td>
                        <td>:</td>
                        <td align="left"><s:password cssClass="requiredField" theme="simple" name="_strNew_us_password" onchange="vldtPassword(this.value, '%{getText('user.newPassword')}', this, '12', '16', 'Y','Y')" /></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="150px" align=left><s:text name="accountActivate.confirmNewPassword"/><s:text name="required.field"/></td>
                        <td>:</td>
                        <td align="left"><s:password cssClass="requiredField" theme="simple" name="_strComfirm_new_us_password" /></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="150px" align=left>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td align="left">&nbsp;</td>
                    </tr>
                </table>
            </div>
	</form>
</body>
</html>