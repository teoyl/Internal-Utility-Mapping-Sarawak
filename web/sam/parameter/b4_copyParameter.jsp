<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
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
        validateRequired(form, errors);
        if (errors.length > 0) {
            alert(errors.join('\n'));
            setFocus(form);
        }
        return errors.length > 0 ? false : true;
    }
    function required(){
        this.a1 = new Array("systemCode_", "<s:text name='Param.system_code' />");
        this.a2 = new Array("systemDesc_", "<s:text name='Param.system_descs' />");
            <s:property escapeHtml="false" value="pageRequired_"/>
        
    }
</script>
        <title><s:text name="system.name"/> - <s:text name="button.add"/></title>
<s:head />
</head>
<body>
    <jsp:include page="/pages/base/b4_actionError.jsp"></jsp:include>
	<form action="processUpdateParameter" id="processUpdateParameterFormId" method="post">
            <s:hidden name="action"/>
            <div class="kt-portlet">
                <div class="kt-portlet__head">
                    <div class="kt-portlet__head-label">
                        <h3 class="kt-portlet__head-title">
                            <span class="titleText"><s:text name="Param.applicationName" /></span>
                            <span class="titleActionTypeText"> | <s:text name="button.add"/></span><br>
                        </h3>
                    </div>
                    <div class="kt-portlet__head-toolbar">
                        <s:submit type="button" cssClass="btn btn-outline-brand" theme="simple" action="processInsertParameter" value="Save" onclick="return localValidateForm(this.form);"/>&nbsp;
                        <s:submit type="button" cssClass="btn btn-outline-brand" theme="simple" action="cancelParameter" value="Cancel"/>
                    </div>
                </div>  
                <%--<table><s:iterator value="modelList" status="paramItemStatus" id="paramItem">
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td align="left"><s:property value="#paramItem.parameter_descs"/></td>
                        <td align="left">:</td>
                        <td align="left"><s:hidden theme="simple" name="modelList[%{#paramItemStatus.index}].ID" value="%{#paramItem.ID}"/>
                            <s:if test='#paramItem.paramType.equalsIgnoreCase("select")'>
                                <s:select name="modelList[%{#paramItemStatus.index}].parameter_value" value="%{#paramItem.parameter_value}" theme="simple" list="#paramItem.radioSelectList" listKey="keyData" listValue="valueData"/>
                            </s:if><s:elseif test='#paramItem.paramType.equalsIgnoreCase("radio")'>
                                <s:radio name="modelList[%{#paramItemStatus.index}].parameter_value" value="%{#paramItem.parameter_value}" theme="simple" list="#paramItem.radioSelectList" listKey="keyData" listValue="valueData"/>
                            </s:elseif><s:else>
                                <s:textfield theme="simple" size="%{#paramItem.textFieldWidth}" name="modelList[%{#paramItemStatus.index}].parameter_value" value="%{#paramItem.parameter_value}"/>
                            </s:else>
                        </td>
                    </tr></s:iterator>
                </table>--%>
                <%--<table width="100%">
                    <tr><td>--%>
                <div class="kt-portlet__body">
                    <s:set var="theIndex">-1</s:set>
                    <s:iterator value="modelGroupList" status="paramItemStatus" var="groupList">
                    <table>
                        <s:if test="#theIndex == -1">
                            <tr>
                                <td colspan="4">&nbsp;<u><b>System Information</b></u></td>
                            </tr>
                            <tr>
                                <td width="20px">&nbsp;</td>
                                <td align="left"><s:text name="Param.system_code"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                                <td align="left">:</td>
                                <td><s:textfield theme="simple" id="systemCode_" name="systemCode_" value="%{systemCode_}"/></td>
                            </tr>
                            <tr>
                                <td width="20px">&nbsp;</td>
                                <td align="left"><s:text name="Param.system_descs"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                                <td align="left">:</td>
                                <td><s:textfield theme="simple" id="systemDesc_" name="systemDesc_" value="%{systemDesc_}"/></td>
                            </tr>
                        </s:if>
                        <s:iterator value="groupList" status="modelStatus" var="paramItem"><s:set var="theIndex">${theIndex + 1}</s:set>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].ID" value="%{#paramItem.ID}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].system_code" value="%{#paramItem.system_code}"/>
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
                                                <s:property escapeHtml="false" value='#paramItem.lookUp'/>
                                                    
                                                </script>   
                                            </s:if>
                                        </s:if><s:elseif test='#paramItem.paramType.equalsIgnoreCase("radio")'>
                                            <s:radio name="modelList[%{#theIndex}].parameter_value" id="modelList%{#theIndex}_parameter_value" value="%{#paramItem.parameter_value}" theme="simple" list="#paramItem.radioSelectList" listKey="keyData" listValue="valueData"/>
                                        </s:elseif><s:else>
                                            <s:if test='#paramItem.numericJavascript == null || #paramItem.numericJavascript.equals("")'>
                                                <s:textfield theme="simple" id="modelList%{#theIndex}_parameter_value" size="%{#paramItem.textFieldWidth}" name="modelList[%{#theIndex}].parameter_value" value="%{#paramItem.parameter_value}"/>
                                            </s:if><s:else>
                                                <input <s:property escapeHtml="false" value="#paramItem.numericJavascript"/> type="text" id="modelList${theIndex}_parameter_value" size="${paramItem.textFieldWidth}" name="modelList[${theIndex}].parameter_value" value="${paramItem.parameter_value}"/>
                                            </s:else>
                                        </s:else>
                                    </s:if><s:else>
                                        ${paramItem.field_input_label}
                                    </s:else>
                                    </td>
                                </tr>
                            </s:else>
                    <%--<tr>
                        <td width="20px">&nbsp;</td>
                        <td align="left"><s:property value="#paramItem.parameter_descs"/></td>
                        <td align="left">:</td>
                        <td align="left"><s:hidden theme="simple" name="modelList[%{#paramItemStatus.index}].ID" value="%{#paramItem.ID}"/>
                            <s:if test='#paramItem.paramType.equalsIgnoreCase("select")'>
                                <s:select name="modelList[%{#paramItemStatus.index}].parameter_value" value="%{#paramItem.parameter_value}" theme="simple" list="#paramItem.radioSelectList" listKey="keyData" listValue="valueData"/>
                            </s:if><s:elseif test='#paramItem.paramType.equalsIgnoreCase("radio")'>
                                <s:radio name="modelList[%{#paramItemStatus.index}].parameter_value" value="%{#paramItem.parameter_value}" theme="simple" list="#paramItem.radioSelectList" listKey="keyData" listValue="valueData"/>
                            </s:elseif><s:else>
                                <s:textfield theme="simple" size="%{#paramItem.textFieldWidth}" name="modelList[%{#paramItemStatus.index}].parameter_value" value="%{#paramItem.parameter_value}"/>
                            </s:else>
                        </td>
                    </tr>--%></s:iterator>
                    </table>
                    </s:iterator>
                   <%-- </td>
                                    </tr>
                </table>--%>
                </div><br>
            </div>
	</form>
</body>
</html>