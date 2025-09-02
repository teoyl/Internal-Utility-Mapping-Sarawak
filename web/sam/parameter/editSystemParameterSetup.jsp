<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-dojo-tags" prefix="sx"%>
<html>
<head>
<sx:head parseContent="true" debug="false" />
<script type="text/javascript" src="include/inforLoader.js"></script>
<script type="text/javascript" src="pages/scripts/common.js"></script>
<script type="text/javascript" src="pages/scripts/validation.jsp"></script>
<script type="text/javascript" src="pages/scripts/lookup.js"></script>
<script type="text/javascript" src="pages/scripts/controls.js"></script>
<script type="text/javascript" src="pages/scripts/confirmation.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
<script type="text/javascript" src="pages/scripts/combo_AJAX.js"></script>
<script type="text/javascript" src="include/popcalendar.js"></script>
<script type="text/javascript" src="pages/scripts/dateUtil.js"></script>
<link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
<link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />
<script language="javascript">
    function localValidateForm(form) {
    var errors = new Array();
        <s:if test='pageRequired_ != null && !pageRequired_.equals("")'>
        validateRequired(form, errors);
        </s:if>
        if (errors.length > 0) {
            alert(errors.join('\n'));
            setFocus(form);
        }
        return errors.length > 0 ? false : true;
    }
    function required(){
        <s:property escape="false" value="pageRequired_"/>
    }
</script>
<title><s:text name="system.name"/> - <s:text name="SystemParameterSetup.applicationName" /> - <s:property value="%{modelList[0].system_descs}" /></title>
<%--<s:head />--%>
</head>
<body>
    <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
	<form action="processUpdateSystemParameterSetup" id="processUpdateSystemParameterSetupFormId" method="post">
            <s:hidden name="action"/>
<!--            <table width="100%">
                <tr>
                    <td>
                        <div class="titleFramework">
                            <span class="titleText"><s:text name="SystemParameterSetup.applicationName" /></span>
                            <span class="titleActionTypeText"> | <s:property value="%{modelList[0].system_descs}" /></span><br>
                        </div>
                    </td>
                    <td align="right" style="padding-right: 8px">
                        <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
                            <tr>
                                <td align="right">
                                    <s:submit type="button" cssClass="defaultButton" theme="simple" action="processUpdateSystemParameterSetup" value="Save" onclick="return localValidateForm(this.form);"/>
                                    <input type="button" class="defaultButton" value='<s:text name="reset"/>' onclick="if (confirmReset())location.href='loadEditPageSystemParameterSetup?id=${modelList[0].system_code}&action=${action}'" />
                                    <s:submit type="button" cssClass="defaultButton" theme="simple" action="cancelSystemParameterSetup" value="Cancel"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>-->
            <div class="titleFramework">
                <span class="titleText"><s:text name="SystemParameterSetup.applicationName" /></span>
                <span class="titleActionTypeText"> | <s:property value="%{modelList[0].system_descs}" /></span><br>
            </div>
            <div class="xbox">
                <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
                    <tr>
                        <td align="right">
                            <s:submit type="button" cssClass="defaultButton" theme="simple" action="processUpdateSystemParameterSetup" value="Save" onclick="return localValidateForm(this.form);"/>
                            <input type="button" class="defaultButton" value='<s:text name="reset"/>' onclick="if (confirmReset())location.href='loadEditPageSystemParameterSetup?id=${modelList[0].system_code}&action=${action}'" />
                            <s:submit type="button" cssClass="defaultButton" theme="simple" action="cancelSystemParameterSetup" value="Cancel"/>
                        </td>
                    </tr>
                </table>
                <s:set name="theIndex">-1</s:set>
                    <s:iterator value="modelGroupList" status="paramItemStatus" id="groupList">
                    <table>
                        <s:iterator value="groupList" status="modelStatus" id="paramItem"><s:set name="theIndex">${theIndex + 1}</s:set>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].ID" value="%{#paramItem.ID}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].field_indicator" value="%{#paramItem.field_indicator}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].field_input_label" value="%{#paramItem.field_input_label}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].textFieldWidth" value="%{#paramItem.textFieldWidth}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].req_field" value="%{#paramItem.req_field}"/>
                            <s:if test='#paramItem.field_indicator.equals("header")'>
                                <tr>
                                    <td colspan="4">&nbsp;<u><b>${paramItem.parameter_descs}</b></u></td>
                                </tr>
                            </s:if><s:elseif test='#paramItem.field_indicator.equals("spacer")'>
                                <tr><td height="3px" colspan="4"></td></tr>
                            </s:elseif><s:else><%--input or label--%>
                                <tr>
                                    <td width="20px">&nbsp;</td>
                                    <s:if test='#paramItem.descs_width != null || #paramItem.descs_width.equals("")'>
                                        <td align="left" width="${paramItem.descs_width}px"><s:property value="#paramItem.parameter_descs"/><s:if test='#paramItem.field_indicator.equalsIgnoreCase("input") && #paramItem.req_field.equalsIgnoreCase("Y")'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></td>
                                    </s:if><s:else>
                                        <td align="left">${paramItem.parameter_descs}<s:if test='#paramItem.field_indicator.equalsIgnoreCase("input") && #paramItem.req_field.equalsIgnoreCase("Y")'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></td>
                                    </s:else>
                                    <td align="left">:</td>
                                    <td>
                                    <s:if test='#paramItem.field_indicator.equals("input")'>
                                        <s:if test='#paramItem.paramType.equalsIgnoreCase("select")'>
                                            <s:select name="modelList[%{#theIndex}].parameter_value" id="modelList%{#theIndex}_parameter_value" value="%{#paramItem.parameter_value}" theme="simple" list="#paramItem.radioSelectList" listKey="keyData" listValue="valueData"/>
                                            <s:if test='#paramItem.numericJavascript != null && !#paramItem.numericJavascript.equals("")'>
                                                <script language="javascript">
                                                <%--<s:property escape="false" value="#paramItem.numericJavascript"/>--%>
                                                <s:property escape="false" value='#paramItem.lookUp'/>
                                                    
                                                </script>   
                                            </s:if>
                                        </s:if><s:elseif test='#paramItem.paramType.equalsIgnoreCase("radio")'>
                                            <s:radio name="modelList[%{#theIndex}].parameter_value" id="modelList%{#theIndex}_parameter_value" value="%{#paramItem.parameter_value}" theme="simple" list="#paramItem.radioSelectList" listKey="keyData" listValue="valueData"/>
                                        </s:elseif><s:else>
                                            <s:if test='#paramItem.numericJavascript == null || #paramItem.numericJavascript.equals("")'>
                                                <s:textfield theme="simple" id="modelList%{#theIndex}_parameter_value" size="%{#paramItem.textFieldWidth}" maxlength="%{#paramItem.textFieldLength}" name="modelList[%{#theIndex}].parameter_value" value="%{#paramItem.parameter_value}"/>
                                            </s:if><s:else>
                                                <input <s:property escape="false" value="#paramItem.numericJavascript"/> type="text" id="modelList${theIndex}_parameter_value" size="${paramItem.textFieldWidth}" name="modelList[${theIndex}].parameter_value" value="${paramItem.parameter_value}"/>
                                            </s:else>
                                        </s:else>
                                    </s:if>
                                    <%--<s:else>--%>
                                        <!--${paramItem.field_input_label}-->
                                    <%--</s:else>--%>
                                    ${paramItem.field_input_label}
                                    </td>
                                </tr>
                            </s:else>
                        </s:iterator>
                    </table>
                    </s:iterator>
            </div>
	</form>
</body>
</html>