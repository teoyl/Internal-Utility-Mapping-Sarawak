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
     if (!isInteger(form.file_retain_period.value))  {
        errors[errors.length] = formatText( "<s:text name='errors.digit' />", "<s:text name='file.retainPeriod' />");
   }
	if (errors.length > 0) {
		alert(errors.join('\n'));
		setFocus(form);
	}
	return errors.length > 0 ? false : true;
}
function required(){
    this.aa = new Array("file_path", "<s:text name='file.path' />");
    this.ab = new Array("file_desc", "<s:text name='file.desc' />");
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="system.name"/> - <s:text name="fileSetup.title" /> - <s:text name="actionType.edit" /></title>
<%--<s:head />--%>
</head>
<body>
    <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
	<form theme="simple" action="processInsertSF">
            <div class="titleFramework">
                <span class="titleText"><s:text name="fileSetup.title" /></span>
                <span class="titleActionTypeText"> | <s:text name="actionType.add" /></span><br>
            </div>
            <div class="xbox">
                <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
                    <tr>
                        <td align="right">
                            <s:submit type="button" cssClass="defaultButton" theme="simple" action="processInsertSF" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>
                            <s:submit type="button" cssClass="defaultButton" theme="simple" action="cancelSF" value="Cancel"/>
                        </td>
                    </tr>
                </table>
                <table>
                    <s:hidden name="action" />
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="150px" align=left><s:text name="file.path"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                        <td width="3px">:</td>
                        <td align="left"><s:textfield theme="simple" name="file_path" /></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="150px" align=left><s:text name="file.desc"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                        <td width="3px">:</td>
                        <td align="left"><s:textarea theme="simple" cols="50"  rows="3" name="file_desc" /></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="150px" align=left><s:text name="file.retainPeriod"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                        <td width="3px">:</td>
                        <td align="left"><s:textfield theme="simple" name="file_retain_period" /> </td>
                    </tr>
                </table>
            </div>
	</form>
</body>
</html>