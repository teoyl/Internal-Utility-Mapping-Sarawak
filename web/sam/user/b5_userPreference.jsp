<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
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
        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
            <form action="updatePreference" id="updatePreferenceFormId" method="post">
            <s:hidden name="action"/>
            <div class="card">
                <div class="card-header bg-light">
                    <h5><s:property value="%{modelList[0].system_descs}" /> <small class="fw-normal text-600">[<s:text name="button.edit"/>]</small></h5>
                </div>
                <div class="card-body">
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
                                <<div class="mb-3"></div>
                                </s:elseif><s:else><%--input or label--%>
                                    <s:if test='#paramItem.field_indicator.equals("input")'>
                                        <s:if test='#paramItem.paramType.equalsIgnoreCase("select")'>
                                            <div class="row mb-1">
                                                <label class="col-md-4 col-form-label col-form-label-sm"><s:property value="#paramItem.parameter_descs"/><s:if test='#paramItem.field_indicator.equalsIgnoreCase("input") && #paramItem.req_field.equalsIgnoreCase("Y")'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></label>
                                                <div class="col-md-6">
                                                <s:select cssClass="form-control form-control-sm sds-dropdown" name="modelList[%{#theIndex}].parameter_value" id="modelList%{#theIndex}_parameter_value" value="%{#paramItem.parameter_value}" theme="simple" list="#paramItem.radioSelectList" listKey="keyData" listValue="valueData"/>
                                                </div>
                                            </div>
                                            <s:if test='#paramItem.numericJavascript != null && !#paramItem.numericJavascript.equals("")'>
                                            <script language="javascript">
                                                <%--<s:property escape="false" value="#paramItem.numericJavascript"/>--%>
                                                <s:property escapeHtml="false" value='#paramItem.lookUp'/>

                                            </script>   
                                        </s:if>
                                    </s:if><s:elseif test='#paramItem.paramType.equalsIgnoreCase("radio")'>
                                            <div class="row mb-1">
                                                <label class="col-md-4 col-form-label col-form-label-sm"><s:property value="#paramItem.parameter_descs"/><s:if test='#paramItem.field_indicator.equalsIgnoreCase("input") && #paramItem.req_field.equalsIgnoreCase("Y")'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></label>
                                                <div class="col-md-6 form-check">
                                                   <s:radio name="modelList[%{#theIndex}].parameter_value" id="modelList%{#theIndex}_parameter_value" value="%{#paramItem.parameter_value}" theme="simple" list="#paramItem.radioSelectList" listKey="keyData" listValue="valueData"/>
                                                </div>
                                            </div>
                                    </s:elseif><s:else>
                                        <s:if test='#paramItem.numericJavascript == null || #paramItem.numericJavascript.equals("")'>
                                            <div class="row mb-1">
                                                <label class="col-md-4 col-form-label col-form-label-sm"><s:property value="#paramItem.parameter_descs"/><s:if test='#paramItem.field_indicator.equalsIgnoreCase("input") && #paramItem.req_field.equalsIgnoreCase("Y")'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></label>
                                                    <div class="col-md-6">
                                                    <s:textfield cssClass="form-control form-control-sm" theme="simple" id="modelList%{#theIndex}_parameter_value" size="%{#paramItem.textFieldWidth}" name="modelList[%{#theIndex}].parameter_value" value="%{#paramItem.parameter_value}"/>
                                                </div>
                                            </div>
                                        </s:if><s:else>
                                            <div class="row mb-1">
                                                <label class="col-md-4 col-form-label col-form-label-sm"><s:property value="#paramItem.parameter_descs"/><s:if test='#paramItem.field_indicator.equalsIgnoreCase("input") && #paramItem.req_field.equalsIgnoreCase("Y")'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></label>
                                                    <div class="col-md-6">
                                                        <input class="form-control form-control-sm" <s:property escapeHtml="false" value="#paramItem.numericJavascript"/> type="text" id="modelList${theIndex}_parameter_value" size="${paramItem.textFieldWidth}" name="modelList[${theIndex}].parameter_value" value="${paramItem.parameter_value}"/>
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
                                
                    <div class="row mt-3">
                        <div class="col-md-12 text-end">
                            <s:submit cssClass="btn btn-sm btn-primary" theme="simple" action="updatePreference" value="Save" onclick="return localValidateForm(this.form);"/>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>