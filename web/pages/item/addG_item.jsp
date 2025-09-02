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
<script type="text/javascript" src="pages/scripts/combo_AJAX.js"></script>
<link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
<link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />
<SCRIPT language="javascript">
function localValidateForm(form, operation) {
	var errors = new Array();
	validateRequired(form, errors);
	if (form.module_type.value == "S"){
            if (form.parent_code.value == ""){
                errors[errors.length] = formatText(messageRequired, "<s:text name='parent.module' />");
            }
        }
	if (errors.length > 0) {
		alert(errors.join('\n'));
		setFocus(form);
	}
	return errors.length > 0 ? false : true;
}

function valcheckType(form){
	if (form.module_type.value == "S"){
		return true;
	}
	return false;
}

function typeChanged(moduleType){
	if (moduleType.value == "M"){
		var form = moduleType.form;
		form.insertModule_parent_module_id.value = "";
		form.parent_code.value = "";
		lbparent_desc.innerText = "";
	}
}

function required(){
        this.aa = new Array("module_type", "<s:text name='module.type' />");
	this.ab = new Array("module_code", "<s:text name='module.code' />");
	this.ac = new Array("module_name", "<s:text name='module.name' />");
}

</SCRIPT>

<%--<s:head />--%>
</head>
<body>
	<jsp:include page="/pages/base/actionError.jsp"></jsp:include>
	<form action="processInsertModule">
            <div class="titleFramework">
                <span class="titleText"><s:text name="gItem" /></span>
                <span class="titleActionTypeText"> | <s:text name="actionType.add" /></span><br>
            </div>
            <div class="xbox">
                <s:hidden name="action" />
                <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
                    <tr>
                        <td align="right">
                            <s:submit type="button" cssClass="defaultButton" theme="simple" action="processInsertGitem" value='%{getText("button.save")}' onclick="return localValidateForm(this.form, 'insert')"/>
                            <s:submit type="button" cssClass="defaultButton" theme="simple" action="cancelGitem" value='%{getText("button.cancel")}'/>
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td class="tdLabel"><s:text name="gItem.name"/><jsp:include page="/pages/base/requiredField.jsp"/>:</td>
                        <td align="left"><s:textfield theme="simple" name="gitem_name" value="%{model.gitem_name}"/></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td class="tdLabel"><s:text name="gItem.code"/><jsp:include page="/pages/base/requiredField.jsp"/>:</td>
                        <td align="left"><s:textfield theme="simple" name="gitem_code" value="%{model.gitem_code}"/></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td class="tdLabel"><s:text name="gItem.modelNo"/><jsp:include page="/pages/base/requiredField.jsp"/>:</td>
                        <td align="left"><s:textfield theme="simple" name="gitem_modelno" value="%{model.gitem_modelno}"/></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td class="tdLabel"><s:text name="gItem.category"/>:</td>
                        <td>
                            <input type="hidden" name="item_category_id" value="${model.item_category_id}" id="item_category_id"/>
                            <input type="text" name="category_code" value="${category_code}" readonly/>
                            <script language="javascript">
                                lookup("Search Category", "ItemCategory", "item_category_code,item_category_id,item_category_name", "category_code,item_category_id,lbcategory_desc",
                                "useSetup_ItemCategory", "item_category_code,item_category_name", "");
                            </script>
                            <label class="label" id="lbcategory_desc" for="lbcategory_desc">${category_desc}</label>
                        </td>
                    </tr>
                </table>
            </div>
	</form>
</body>
</html>