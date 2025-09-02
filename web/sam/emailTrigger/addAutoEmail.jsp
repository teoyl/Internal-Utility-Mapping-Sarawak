<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <script type="text/javascript" src="pages/scripts/lookup.js"></script>
        <script type="text/javascript" src="pages/scripts/controls.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
        <script type="text/javascript" src="pages/scripts/combo_AJAX.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />

        <SCRIPT language="javascript">
            function localValidateForm(form, operation) {
                var errors = new Array();

                validateRequired(form, errors);
                // sample to validate items.
                validateItems(form, errors);
                if (errors.length > 0) {
                    alert(errors.join('\n'));
                    setFocus(form);
                }
                return errors.length > 0 ? false : true;
            }

            function validateItems(form, errors) {
                if (form.param_name) {
                    if (typeof (form.param_name.type) == "string") {
                        validateItem(form.param_name, form.retrieve_from, 1, errors);
                    } else {
                        var isError = false;
                        for (var i = 0; i < form.param_name.length; i++) {
                            isError = validateItem(form.param_name[i], form.retrieve_from[i], i + 1, errors);
                            if (isError)
                                break;
                        }
                    }
                }
            }

            function validateItem(param_name, retrieve_from, idx, errors) {
                //if (!selected.checked){
                if (param_name.value == "") {
                    errors[errors.length] = formatText(messageItemized, idx,
                            formatText(messageRequired, "<s:text name='autoEmailSetup.paramName' />"));
                    isError = true;
                }
                if (retrieve_from.value == "") {
                    errors[errors.length] = formatText(messageItemized, idx,
                            formatText(messageRequired, "<s:text name='autoEmailSetup.paramFrom' />"));
                    isError = true;
                }
            }

            function required() {
                this.aa = new Array("code", "<s:text name='autoEmailSetup.code' />");
                this.ab = new Array("descs", "<s:text name='autoEmailSetup.descs' />");
                this.ac = new Array("action_name", "<s:text name='autoEmailSetup.action_name' />");
                this.ad = new Array("method_name", "<s:text name='autoEmailSetup.method_name' />");
                this.ae = new Array("action_status", "<s:text name='autoEmailSetup.action_status' />");
                this.af = new Array("mail_to", "<s:text name='autoEmailSetup.mail_to' />");
                this.ag = new Array("notification_code", "<s:text name='autoEmailSetup.notification' />");
            }
        </SCRIPT>

        <%--<s:head />--%>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <%--div class="row">
            <div class="col-md-4">
                <h3 class="title-v3"><s:text name="autoEmailSetup.title" /> <small><s:text name="actionType.add" /></small></h3>
            </div>
        </div--%>

        <form theme="simple" action="processInsertAutoEmail">
            <s:hidden theme="simple" name="action" />
            <div class="row">
                <!--left box-->
                <div class="col-md-9">
                    <div class="row">
                        <div class="col-md-4">
                            <div class="form-group form-group-default required">
                                <label><s:text name="autoEmailSetup.code"/></label>
                                <s:textfield theme="simple" name="code" value="%{model.code}" cssClass="form-control" maxlength="20"/>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group form-group-default required">
                                <label><s:text name="autoEmailSetup.descs"/></label>
                                <s:textfield theme="simple" name="descs" value="%{model.descs}" cssClass="form-control" />
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group form-group-select">
                                <label><s:text name="autoEmailSetup.type"/></label>
                                <s:select theme="simple" name="auto_type" list="autoTypeList" listKey="keyData" listValue="valueData" value="%{model.auto_type}" cssClass="form-control sds-dropdown"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4">
                            <div class="form-group form-group-default required">
                                <label><s:text name="autoEmailSetup.action_name"/></label>
                                <s:textfield theme="simple" name="action_name" value="%{model.action_name}" cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group form-group-default required">
                                <label><s:text name="autoEmailSetup.method_name"/></label>
                                <s:textfield theme="simple" name="method_name" value="%{model.method_name}" cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group form-group-default required">
                                <label><s:text name="autoEmailSetup.action_status"/></label>
                                <s:textfield theme="simple" name="action_status" value="%{model.action_status}" cssClass="form-control"/>
                            </div>
                        </div>
                    </div>        
                    <div class="row">
                        <div class="col-md-4">
                            <div class="form-group form-group-default required">
                                <label><s:text name="autoEmailSetup.mail_to"/></label>
                                <s:textfield theme="simple" name="mail_to" value="%{model.mail_to}"  cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group form-group-default">
                                <label><s:text name="autoEmailSetup.mail_ccto"/></label>
                                <s:textfield theme="simple" name="mail_ccto" value="%{model.mail_ccto}" cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group form-group-default">
                                <label><s:text name="autoEmailSetup.mail_bccto"/></label>
                                <s:textfield theme="simple" name="mail_bccto" value="%{model.mail_bccto}" size="60" cssClass="form-control"/>
                            </div>
                        </div>
                    </div>    
                    <div class="row">
                        <div class="col-md-4">
                            <div class="radio-group radio-group-default">
                                <label><s:text name="autoEmailSetup.enable"/></label><br>
                                <div class="checkbox checkbox-inline check-success" style="margin-top:0px;margin-bottom:4px;">
                                    <input name="enable" value='%{model.enable.equals("Y")?"True":"false"}' id="enable" type="checkbox" <s:if test='model.enable.equals("Y")'>checked</s:if>>
                                    <label for="enable"></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group form-group-default required">
                                <label><s:text name="autoEmailSetup.notification"/></label>
                                <div class="input-group">
                                    <s:hidden theme="simple" name="no_id" id="no_id" value="%{model.no_id}"/>
                                    <s:textfield theme="simple" id="notification_code" name="model.notificationSetup.no_type" value="%{model.notificationSetup.no_type}" cssClass="form-control" readonly="true"/>
                                    <span class="input-group-addon" onclick="lookupModal($('#notification_code'), 'modalLookup?lookFor=no_id,no_type&writeTo=no_id,notification_code&lookup=useSetup_SetupNotification&displayedColumns=no_type,no_desc','')"><i class="fa fa-search"></i></span>
                                </div>
                            </div> 
                        </div>
                        <div class="col-md-4">
                        </div>
                    </div>            
                </div>
                <!--right box-->
                <div class="col-md-3 text-right">
                    <button class="btn btn-primary" type="submit" name="action:processInsertAutoEmail" id="processInsertAutoEmail" onclick="return localValidateForm(this.form, 'insert')"><i class="fa fa-save"></i>Save</button>     
                    <button class="btn btn-default" type="submit" name="action:cancelAutoEmail" id="cancelAutoEmail"><i class="fa fa-close"></i>Cancel</button>     
                    <%--s:submit type="submit" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="processInsertAutoEmail" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>
                    <s:submit type="submit" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="cancelAutoEmail" value="Cancel"/--%>
                </div>
            </div><br><br>


            <div class="row">
                <div class="col-md-12">
                    <h3 class="title-v3"><s:text name="autoEmailSetup.param" /></h3>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 form-row-margin">
                    <button class="btn btn-default" type="submit" name="action:processAddParamAutoEmail" id="processAddParamAutoEmail"><i class="fa fa-plus"></i>Add Parameter</button>
                    <button class="btn btn-default" type="submit" name="action:processDeleteParamAutoEmail" id="processDeleteParamAutoEmail" onclick="if (isCheckboxSelected(form.param_selected)) {
                                return confirmDelete();
                            } else {
                                return false;
                            }"><i class="fa far fa-trash-alt"></i>Delete Parameter</button>
                    <%--s:submit cssClass="defaultButton" theme="simple" action="processAddParamAutoEmail" value="Add Parameter" />
                    <s:submit cssClass="defaultButton" theme="simple" action="processDeleteParamAutoEmail" value="Delete Parameter"
                              onclick="if ( isCheckboxSelected(form.param_selected)) {return confirmDelete();} else {return false;}"/--%>
                </div>
            </div>
            <s:if test="emailParamList.size() > 0">
                <div class="row">
                    <div class="col-md-12">
                        <div class="table-responsive">
                            <table class="table table-sds table-condensed table-striped table-hover" cellspacing="1" cellpadding="1"  width="100%">
                                <thead>
                                    <tr>
                                        <th width="10">
                                            <div class="checkbox check-success tableFormCheckbox">
                                                <s:if test="emailParamList.size() > 0">
                                                    <input type="checkbox"  id="param_select" class="selectAll" name="param_select" onClick="toggleCheckboxByName(this,'param_selected');">
                                                    <!--<input type="checkbox" id="param_select" name="param_select" onclick="toggleCheckbox(this, delParam_ids);">-->
                                                </s:if>
                                                <s:else>
                                                    <input type="checkbox" id="param_select" class="selectAll" name="param_select" disabled >
                                                </s:else>
                                                <label for="param_select" class="tableFormCheckbox"></label>
                                            </div>
                                        </th>
                                        <th width="310"><s:text name="autoEmailSetup.paramName" /></th>
                                        <th width="460"><s:text name="autoEmailSetup.paramFrom" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:iterator value="emailParamList" status="paramStatus" var="paramAutoEmail">
                                        <tr class="<s:if test="#paramStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                            <s:hidden theme="simple" name="param_id" value="%{#paramAutoEmail.param_id}" />
                                            <td>
                                                <div class="checkbox check-success tableFormCheckbox">
                                                    <input type="checkbox" name="param_selected" class="checkbox_child" id="${paramStatus.index}" value="${paramStatus.index}" onclick="toggleSelectAll()">
                                                    <label for="${paramStatus.index}" class="tableFormCheckbox"></label>
                                                </div>
                                                <%--<s:checkbox theme="simple" name="param_selected" id="delParam_ids" fieldValue="%{#paramStatus.index}" onclick="checkToggleCheckbox(param_select, delParam_ids)"/>--%>
                                            </td>
                                            <td width="310"><s:textfield cssClass="input-sm form-control" theme="simple" name="param_name" value="%{#paramAutoEmail.param_name}"/></td>
                                            <td width="460"><s:textfield cssClass="input-sm form-control" theme="simple" name="retrieve_from" value="%{#paramAutoEmail.retrieve_from}"/></td>
                                        </tr>
                                    </s:iterator>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </s:if>


            <%--div class="panel panel-default ">
                <div class="panel-heading ">
                    <h3 class="panel-title"> 
                        <span class="titleText"><s:text name="autoEmailSetup.title" /></span>
                        <span class="titleActionTypeText"> | <s:text name="actionType.add" /></span>
                    </h3>
                </div>
                <div class="panel-body">
                    <s:hidden theme="simple" name="action" />
                    <s:hidden theme="simple" name="application_id" value="%{model.application_id}"/>
                    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form table borderless">
                        <tr>
                            <td align="right">
                                <s:submit type="submit" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="processInsertAutoEmail" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>
                                <s:submit type="submit" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="cancelAutoEmail" value="Cancel"/>
                            </td>
                        </tr>
                    </table>
                    <table class="table borderless">
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="autoEmailSetup.code"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:textfield theme="simple" name="code" value="%{model.code}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="autoEmailSetup.descs"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td>:</td>
                            <td align="left"><s:textfield theme="simple" name="descs" value="%{model.descs}" size="60" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="autoEmailSetup.type"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td>:</td>
                            <td><s:select theme="simple" name="auto_type" list="autoTypeList" listKey="keyData" listValue="valueData" value="%{model.auto_type}" cssClass="input-md form-control" /></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="autoEmailSetup.action_name"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td>:</td>
                            <td><s:textfield theme="simple" name="action_name" value="%{model.action_name}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="autoEmailSetup.method_name"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td>:</td>
                            <td><s:textfield theme="simple" name="method_name" value="%{model.method_name}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="autoEmailSetup.action_status"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td>:</td>
                            <td><s:textfield theme="simple" name="action_status" value="%{model.action_status}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="autoEmailSetup.mail_to"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td>:</td>
                            <td><s:textfield theme="simple" name="mail_to" value="%{model.mail_to}" size="60" cssClass="input-md form-control" /></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="autoEmailSetup.mail_ccto"/></td>
                            <td>:</td>
                            <td><s:textfield theme="simple" name="mail_ccto" value="%{model.mail_ccto}" size="60" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="autoEmailSetup.mail_bccto"/></td>
                            <td>:</td>
                            <td><s:textfield theme="simple" name="mail_bccto" value="%{model.mail_bccto}" size="60" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="autoEmailSetup.enable"/></td>
                            <td>:</td>
                            <td><s:checkbox theme="simple" name="enable" fieldValue="Y" value='%{model.enable.equals("Y")?"True":"false"}' /></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="autoEmailSetup.notification"/></td>
                            <td>:</td>
                            <td><div class="input-group">
                                    <s:hidden theme="simple" name="no_id" value="%{model.no_id}"/>
                                    <input type="text" name="notification_code" value="${notification_code}" readonly class="input-md form-control"/>
                                    <div class="input-group-btn">
                                        <script language="javascript">
                                            lookup("Search Notification", "SetupNotification", "no_id,no_type", "no_id,notification_code",
                                                    "useSetup_SetupNotification", "no_type, no_desc", "", "");
                                        </script>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                    <hr>
                    <table width="100%" class="form table borderless">
                        <tr class="CLASS_TABLE_HEADER" >
                            <td class="header_2"><span class="imgArrowRight" /><span class="header_2Text"><s:text name="autoEmailSetup.param" /></span></td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <s:submit cssClass="defaultButton" theme="simple" action="processAddParamAutoEmail" value="Add Parameter" />
                                <s:submit cssClass="defaultButton" theme="simple" action="processDeleteParamAutoEmail" value="Delete Parameter"
                                          onclick="if ( isCheckboxSelected(form.param_selected)) {return confirmDelete();} else {return false;}"/>
                            </td>
                        </tr>
                    </table>
                    <table class="defaultTable" cellspacing="1" cellpadding="1"  width="100%">
                        <tr>
                            <th width="10">
                                <s:if test="emailParamList.size() > 0">
                                    <input type="checkbox" id="param_select" name="param_select" onclick="toggleCheckbox(this, delParam_ids);">
                                </s:if>
                                <s:else>
                                    <input type="checkbox" id="param_select" name="param_select" disabled >
                                </s:else>
                            </th>
                            <th width="310"><s:text name="autoEmailSetup.paramName" /></th>
                            <th width="460"><s:text name="autoEmailSetup.paramFrom" /></th>
                        </tr>
                        <s:iterator value="emailParamList" status="paramStatus" id="paramAutoEmail">
                            <tr class="<s:if test="#paramStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                <s:hidden theme="simple" name="param_id" value="%{#paramAutoEmail.param_id}" />
                                <td><s:checkbox theme="simple" name="param_selected" id="delParam_ids" fieldValue="%{#paramStatus.index}" onclick="checkToggleCheckbox(param_select, delParam_ids)"/></td>
                                <td width="310"><s:textfield theme="simple" name="param_name" value="%{#paramAutoEmail.param_name}"/></td>
                                <td width="460"><s:textfield size="50" theme="simple" name="retrieve_from" value="%{#paramAutoEmail.retrieve_from}" cssClass="input-md form-control"/></td>
                            </tr>
                        </s:iterator>
                    </table>
                </div>
            </div--%>
        </form><br><br>
    </body>
</html>