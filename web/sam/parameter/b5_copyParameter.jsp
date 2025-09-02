<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<sx:head parseContent="true" debug="false" />
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
    <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
	<form action="processUpdateParameter" id="processUpdateParameterFormId" method="post">
        <div class="card">
            <s:hidden name="action"/>
            <div class="card-header bg-light">
                <div class="row flex-between-center">
                    <div class="col-6 col-sm-auto d-flex align-items-center pe-0">
                        <h5><s:text name="Param.applicationName"/>&nbsp;<small class="fw-normal text-600"><s:text name="button.add"/></small></h5>
                    </div>
                    <div class="col-6 col-sm-auto ms-auto text-end ps-0">
                        <s:submit type="button" cssClass="btn btn-sm btn-primary" theme="simple" action="processInsertParameter" value="Save" onclick="return localValidateForm(this.form);"/>
                        <s:submit type="button" cssClass="btn btn-sm btn-falcon-default" theme="simple" action="cancelParameter" value="Cancel"/>
                    </div>
                </div>
            </div>
            <div class="card-body">
                <s:set var="theIndex">-1</s:set>
                    <s:iterator value="modelGroupList" status="paramItemStatus" var="groupList">
                        <s:if test="#theIndex == -1">
                            <div class="row mt-3">
                                <div class="col-md-12">
                                    <h5>
                                        <u>System Information</u>
                                    </h5>
                                </div>
                            </div>
                            <div class="row mb-1">
                                <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="Param.system_code"/><jsp:include page="/pages/base/requiredField.jsp"/></label>
                                <div class="col-md-6">
                                    <s:textfield theme="simple" id="systemCode_" name="systemCode_" value="%{systemCode_}" class="form-control form-control-sm"/>
                                </div>
                            </div>
                            <div class="row mb-1">
                                <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="Param.system_descs"/><jsp:include page="/pages/base/requiredField.jsp"/></label>
                                <div class="col-md-6">
                                    <s:textfield theme="simple" id="systemDesc_" name="systemDesc_" value="%{systemDesc_}" class="form-control form-control-sm"/>
                                </div>
                            </div>
                        </s:if>
                        <s:iterator value="groupList" status="modelStatus" var="paramItem"><s:set var="theIndex">${theIndex + 1}</s:set>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].ID" value="%{#paramItem.ID}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].system_code" value="%{#paramItem.system_code}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].field_indicator" value="%{#paramItem.field_indicator}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].field_input_label" value="%{#paramItem.field_input_label}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].textFieldWidth" value="%{#paramItem.textFieldWidth}"/>
                            <s:hidden theme="simple" name="modelList[%{#theIndex}].req_field" value="%{#paramItem.req_field}"/>
                            <s:if test='#paramItem.field_indicator.equals("header")'>                                
                                <div class="row mt-3">
                                    <div class="col-md-12">
                                        <h5>
                                            <u>${paramItem.parameter_descs}</u>
                                        </h5>
                                    </div>
                                </div>
                            </s:if><s:elseif test='#paramItem.field_indicator.equals("spacer")'>
                                <div class="mb-3"></div>
                            </s:elseif><s:else><%--input or label--%>
                                    <s:if test='#paramItem.field_indicator.equals("input")'>
                                        <s:if test='#paramItem.paramType.equalsIgnoreCase("select")'>
                                            <div class="row mb-1">
                                                <label class="col-md-4 col-form-label col-form-label-sm sub"><s:property value="#paramItem.parameter_descs"/><s:if test='#paramItem.field_indicator.equalsIgnoreCase("input") && #paramItem.req_field.equalsIgnoreCase("Y")'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></label>
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
                                                <label class="col-md-4 col-form-label col-form-label-sm sub"><s:property value="#paramItem.parameter_descs"/><s:if test='#paramItem.field_indicator.equalsIgnoreCase("input") && #paramItem.req_field.equalsIgnoreCase("Y")'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></label>
                                                <div class="col-md-6 form-check">
                                                   <s:radio name="modelList[%{#theIndex}].parameter_value" id="modelList%{#theIndex}_parameter_value" value="%{#paramItem.parameter_value}" theme="simple" list="#paramItem.radioSelectList" listKey="keyData" listValue="valueData"/>
                                                </div>
                                            </div>
                                    </s:elseif><s:else>
                                        <s:if test='#paramItem.numericJavascript == null || #paramItem.numericJavascript.equals("")'>
                                            <div class="row mb-1">
                                                <label class="col-md-4 col-form-label col-form-label-sm sub"><s:property value="#paramItem.parameter_descs"/><s:if test='#paramItem.field_indicator.equalsIgnoreCase("input") && #paramItem.req_field.equalsIgnoreCase("Y")'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></label>
                                                    <div class="col-md-6">
                                                    <s:textfield cssClass="form-control form-control-sm" theme="simple" id="modelList%{#theIndex}_parameter_value" maxlength="%{#paramItem.textFieldWidth}" name="modelList[%{#theIndex}].parameter_value" value="%{#paramItem.parameter_value}"/>
                                                </div>
                                            </div>
                                        </s:if><s:else>
                                            <div class="row mb-1">
                                                <label class="col-md-4 col-form-label col-form-label-sm sub"><s:property value="#paramItem.parameter_descs"/><s:if test='#paramItem.field_indicator.equalsIgnoreCase("input") && #paramItem.req_field.equalsIgnoreCase("Y")'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></label>
                                                    <div class="col-md-6">
                                                    <s:if test='#paramItem.numericJavascript.startsWith("byClass_")'>
                                                        <input class="form-control form-control-sm <s:property escapeHtml="false" value="#paramItem.numericJavascript.substring(8)"/>" type="text" id="modelList${theIndex}_parameter_value" maxlength="${paramItem.textFieldWidth}" name="modelList[${theIndex}].parameter_value" value="${paramItem.parameter_value}"/>
                                                    </s:if><s:else>
                                                        <input class="form-control form-control-sm" <s:property escapeHtml="false" value="#paramItem.numericJavascript"/> type="text" id="modelList${theIndex}_parameter_value" maxlength="${paramItem.textFieldWidth}" name="modelList[${theIndex}].parameter_value" value="${paramItem.parameter_value}"/>
                                                    </s:else>
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
        </div>
	</form>
</body>
</html>