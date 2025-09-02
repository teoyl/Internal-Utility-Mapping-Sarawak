<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script type="text/javascript" src="include/inforLoader.js"></script>
        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/lookup.js"></script>
        <script type="text/javascript" src="pages/scripts/controls.js"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
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
            function required() {
            <s:property escapeHtml="false" value="pageRequired_"/>
            }
        </script>
        <title><s:text name="system.name"/> - <s:property value="%{modelList[0].system_descs}" /></title>
        <s:head />
    </head>
    <body>
        <jsp:include page="/pages/base/b4_actionError.jsp"></jsp:include>
            <form action="updatePreference" id="updatePreferenceFormId" method="post">
            <s:hidden name="action"/>
            <div class="kt-portlet">
                <div class="kt-portlet__head">
                    <div class="kt-portlet__head-label">
                        <h3 class="kt-portlet__head-title">
                            <s:property value="%{modelList[0].system_descs}" /> <small>[<s:text name="button.edit"/>]</small>
                        </h3>
                    </div>
                </div>
                <div class="kt-portlet__body">
                    <s:set var="theIndex">-1</s:set>
                    <s:iterator value="modelGroupList" status="paramItemStatus" var="groupList">
                        <s:iterator value="groupList" status="modelStatus" var="paramItem"><s:set var="theIndex">${theIndex + 1}</s:set>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].ID" value="%{#paramItem.ID}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].system_code" value="%{#paramItem.system_code}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].parameter_code" value="%{#paramItem.parameter_code}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].field_indicator" value="%{#paramItem.field_indicator}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].field_input_label" value="%{#paramItem.field_input_label}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].textFieldWidth" value="%{#paramItem.textFieldWidth}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].req_field" value="%{#paramItem.req_field}"/>
                            <s:if test='#paramItem.field_indicator.equals("header")'>
                                <div class="row ">
                                    <div class="col-md-12">
                                        <h3>
                                            <u><s:property value="#paramItem.parameter_descs"/></u>
                                        </h3>
                                    </div>
                                </div>
                            </s:if><s:elseif test='#paramItem.field_indicator.equals("spacer")'>
                                <tr><td height="3px" colspan="4"></td></tr>
                                </s:elseif><s:else><%--input or label--%>
                                    <s:if test='#paramItem.field_indicator.equals("input")'>
                                        <s:if test='#paramItem.paramType.equalsIgnoreCase("select")'>
                                            <div class="row form-group">
                                                <label class="col-md-4 control-label sub"><s:property value="#paramItem.parameter_descs"/><s:if test='#paramItem.field_indicator.equalsIgnoreCase("input") && #paramItem.req_field.equalsIgnoreCase("Y")'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></label>
                                                <div class="col-md-6">
                                                <s:select cssClass="form-control sds-dropdown" name="modelList[%{#theIndex}].parameter_value" id="modelList%{#theIndex}_parameter_value" value="%{#paramItem.parameter_value}" theme="simple" list="#paramItem.radioSelectList" listKey="keyData" listValue="valueData"/>
                                                </div>
                                            </div>
                                            <s:if test='#paramItem.numericJavascript != null && !#paramItem.numericJavascript.equals("")'>
                                            <script language="javascript">
                                                <%--<s:property escape="false" value="#paramItem.numericJavascript"/>--%>
                                                <s:property escapeHtml="false" value='#paramItem.lookUp'/>

                                            </script>   
                                        </s:if>
                                    </s:if><s:elseif test='#paramItem.paramType.equalsIgnoreCase("radio")'>
                                            <div class="row form-group">
                                                <label class="col-md-4 radio-label sub"><s:property value="#paramItem.parameter_descs"/><s:if test='#paramItem.field_indicator.equalsIgnoreCase("input") && #paramItem.req_field.equalsIgnoreCase("Y")'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></label>
                                                <div class="col-md-6 radio radio-inline radio-success">
                                                   <s:radio name="modelList[%{#theIndex}].parameter_value" id="modelList%{#theIndex}_parameter_value" value="%{#paramItem.parameter_value}" theme="simple" list="#paramItem.radioSelectList" listKey="keyData" listValue="valueData"/>
                                                </div>
                                            </div>
                                    </s:elseif><s:else>
                                        <s:if test='#paramItem.numericJavascript == null || #paramItem.numericJavascript.equals("")'>
                                            <div class="row form-group">
                                                <label class="col-md-4 control-label sub"><s:property value="#paramItem.parameter_descs"/><s:if test='#paramItem.field_indicator.equalsIgnoreCase("input") && #paramItem.req_field.equalsIgnoreCase("Y")'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></label>
                                                    <div class="col-md-6">
                                                    <s:textfield cssClass="form-control" theme="simple" id="modelList%{#theIndex}_parameter_value" size="%{#paramItem.textFieldWidth}" name="modelList[%{#theIndex}].parameter_value" value="%{#paramItem.parameter_value}"/>
                                                </div>
                                            </div>
                                        </s:if><s:else>
                                            <div class="row form-group">
                                                <label class="col-md-4 control-label sub"><s:property value="#paramItem.parameter_descs"/><s:if test='#paramItem.field_indicator.equalsIgnoreCase("input") && #paramItem.req_field.equalsIgnoreCase("Y")'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></label>
                                                    <div class="col-md-6">
                                                        <input class="form-control" <s:property escapeHtml="false" value="#paramItem.numericJavascript"/> type="text" id="modelList${theIndex}_parameter_value" size="${paramItem.textFieldWidth}" name="modelList[${theIndex}].parameter_value" value="${paramItem.parameter_value}"/>
                                                </div>
                                            </div>
                                        </s:else>
                                    </s:else>
                                </s:if><s:else>
                                    ${paramItem.field_input_label}
                                </s:else>
                            </s:else>
                        </s:iterator>
                    </s:iterator>
                </div>
                <div class="kt-portlet__foot">
                    <div class="kt-form__actions">
                        <div class="row form-row-margin">
                            <div class="col-md-12 text-right">
                                <s:submit cssClass="btn btn-brand" theme="simple" action="updatePreference" value="Save" onclick="return localValidateForm(this.form);"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>    
        </form>
    </body>
</html>