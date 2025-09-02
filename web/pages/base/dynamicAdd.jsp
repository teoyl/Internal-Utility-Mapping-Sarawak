<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<s:if test="loadDescsUsed"><%@taglib uri="/struts-dojo-tags" prefix="sx"%></s:if>
<html>
<head><s:if test="loadDescsUsed">
<sx:head parseContent="true" debug="false" />
<script type="text/javascript" src="include/inforLoader.js"></script></s:if>
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
<s:if test="popupCalanderUsed"><script type="text/javascript" src="include/popcalendar.js"></script></s:if>
<SCRIPT language="javascript">
function localValidateForm(form, operation) {
	var errors = new Array();

	validateRequired(form, errors);

	<s:property escape="false" value="localValidateFormJavascript"/>

	if (errors.length > 0) {
            alert(errors.join('\n'));
            setFocus(form);
	}
	return errors.length > 0 ? false : true;
}

<s:property escape="false" value="pageJavascript"/>

function required(){
    <s:property escape="false" value="addPageRequired"/>
}
<s:if test="popupCalanderUsed">InitCalendar2("images/",false);</s:if>
</SCRIPT>

<%--<s:head />--%>
</head>
<body>
	<jsp:include page="/pages/base/actionError.jsp"></jsp:include>
        <form  theme="simple" id="dynamicFormId" action="processInsertDynamic" method="post">
            <div class="titleFramework">
                <span class="titleText"><s:property value="pageTitle" /></span>
                <span class="titleActionTypeText"> | <s:text name="actionType.add" /></span><br>
            </div>
                <div class="xbox">
		<s:hidden name="action"/>
		<s:hidden name="objId" value="%{getFieldData('objId')}"/>
                <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
                    <tr>
                        <td><jsp:include page="/pages/base/denoteRequired.jsp" /></td>
                        <td align="right">
                            <s:submit type="button" cssClass="defaultButton" theme="simple" action="processInsertDynamic" value='%{getText("button.save")}' onclick="return localValidateForm(this.form, 'insert')"/>
                            <s:submit type="button" cssClass="defaultButton" theme="simple" action="cancelDynamic" value='%{getText("button.cancel")}'/>
                        </td>
                    </tr>
                </table>
                <table><s:iterator value="pageLabels" var="field" status="rowStatus">
                    <tr><s:set name="itemText" value="%{pageFields[#rowStatus.index]}"/>
                        <td width="20px">&nbsp;</td>
                        <td width="${labelWidth}px" align="left">${pageLabelsDesc[rowStatus.index]}<s:if test='isRequiredField("Add", #field)'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></td>
                        <td width="3px">:</td>
                        <td align="left">
                            <s:if test='#itemText.startsWith("select:")'>
                                <s:select theme="simple" name="%{getNameOfColumn(#field)}" list="getPageDDList(#field)" listKey="keyData" listValue="valueData" value="%{getFieldData(#itemText)}" onchange='%{getFieldJavaScript(#itemText+"_onchange")}'/>
                            </s:if>
                            <s:else>
                                <s:property escape="false" value="%{#itemText}"/>
                            </s:else>
                        </td>
                    </tr>
                </s:iterator></table>
            </div>
	</form>
        <s:if test="pageLoadedJavascript != null">
            <script language="javascript" type="text/javascript">
            <s:property escape="false" value="pageLoadedJavascript"/>
            </script>
        </s:if>
</body>
</html>