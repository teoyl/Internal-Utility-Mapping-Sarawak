<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
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
            <div class="card">
                <div class="card-header bg-light">
                    <h5><s:text name="autoEmailSetup.title"/> <small class="fw-normal text-600"><s:text name="button.add"/></small></h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <!--left box-->
                        <div class="col-md-9">
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="mb-3 required">
                                        <label class="form-label"><s:text name="autoEmailSetup.code"/></label>
                                        <s:textfield theme="simple" name="code" value="%{model.code}" cssClass="form-control form-control-sm" maxlength="20"/>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="mb-3 required">
                                        <label class="form-label"><s:text name="autoEmailSetup.descs"/></label>
                                        <s:textfield theme="simple" name="descs" value="%{model.descs}" cssClass="form-control form-control-sm" />
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="mb-3">
                                        <label class="form-label"><s:text name="autoEmailSetup.type"/></label>
                                        <s:select theme="simple" name="auto_type" list="autoTypeList" listKey="keyData" listValue="valueData" value="%{model.auto_type}" cssClass="form-control form-control-sm sds-dropdown"/>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="mb-3 required">
                                        <label class="form-label"><s:text name="autoEmailSetup.action_name"/></label>
                                        <s:textfield theme="simple" name="action_name" value="%{model.action_name}" cssClass="form-control form-control-sm"/>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="mb-3 required">
                                        <label class="form-label"><s:text name="autoEmailSetup.method_name"/></label>
                                        <s:textfield theme="simple" name="method_name" value="%{model.method_name}" cssClass="form-control form-control-sm"/>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="mb-3 required">
                                        <label class="form-label"><s:text name="autoEmailSetup.action_status"/></label>
                                        <s:textfield theme="simple" name="action_status" value="%{model.action_status}" cssClass="form-control form-control-sm"/>
                                    </div>
                                </div>
                            </div>        
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="mb-3 required">
                                        <label class="form-label"><s:text name="autoEmailSetup.mail_to"/></label>
                                        <s:textfield theme="simple" name="mail_to" value="%{model.mail_to}"  cssClass="form-control form-control-sm"/>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="mb-3">
                                        <label class="form-label"><s:text name="autoEmailSetup.mail_ccto"/></label>
                                        <s:textfield theme="simple" name="mail_ccto" value="%{model.mail_ccto}" cssClass="form-control form-control-sm"/>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="mb-3">
                                        <label class="form-label"><s:text name="autoEmailSetup.mail_bccto"/></label>
                                        <s:textfield theme="simple" name="mail_bccto" value="%{model.mail_bccto}" size="60" cssClass="form-control form-control-sm"/>
                                    </div>
                                </div>
                            </div>    
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="mb-3">
                                        <label class="form-label"><s:text name="autoEmailSetup.enable"/></label><br>
                                        <div class="form-check">
                                            <input name="enable" value='%{model.enable.equals("Y")?"True":"false"}' id="enable" type="checkbox" <s:if test='model.enable.equals("Y")'>checked</s:if> class="form-check-input">
                                            <label for="enable" class="form-check-label"></label>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="mb-3 required">
                                        <label class="form-label"><s:text name="autoEmailSetup.notification"/></label>
                                        <div class="input-group input-group-sm">
                                            <s:textfield theme="simple" id="notification_code" name="model.notificationSetup.no_type" value="%{model.notificationSetup.no_type}" cssClass="form-control form-control-sm" readonly="true"/>
                                            <s:hidden theme="simple" name="no_id" id="no_id" value="%{model.no_id}"/>
                                            <span class="input-group-text" onclick="lookupModal($('#notification_code'), 'modalLookup?lookFor=no_id,no_type&writeTo=no_id,notification_code&lookup=useSetup_SetupNotification&displayedColumns=no_type,no_desc','')"><i class="fa fa-search"></i></span>
                                        </div>
                                    </div> 
                                </div>
                                <div class="col-md-4">
                                </div>
                            </div>            
                        </div>
                        <!--right box-->
                        <div class="col-md-3 text-end">
                            <button class="btn btn-primary btn-sm mb-1" type="submit" name="action:processInsertAutoEmail" id="processInsertAutoEmail" onclick="return localValidateForm(this.form, 'insert')"><i class="fa fa-save"></i><span class="ms-1">Save</span></button>     
                            <button class="btn btn-falcon-default btn-sm mb-1" type="submit" name="action:cancelAutoEmail" id="cancelAutoEmail"><i class="fa fa-times"></i><span class="ms-1">Cancel</span></button>     
                            <%--s:submit type="submit" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="processInsertAutoEmail" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>
                            <s:submit type="submit" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="cancelAutoEmail" value="Cancel"/--%>
                        </div>
                    </div><br><br>


                    <div class="row">
                        <div class="col">
                            <h5><s:text name="autoEmailSetup.param" /></h5>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">
                            <button class="btn btn-falcon-default btn-sm" type="submit" name="action:processAddParamAutoEmail" id="processAddParamAutoEmail"><i class="fa fa-plus"></i><span class="ms-1">Add Parameter</span></button>
                            <button class="btn btn-falcon-default btn-sm" type="submit" name="action:processDeleteParamAutoEmail" id="processDeleteParamAutoEmail" onclick="if (isCheckboxSelected(form.param_selected)) {
                                        return confirmDelete();
                                    } else {
                                        return false;
                                    }"><i class="fa far fa-trash-alt"></i><span class="ms-1">Delete Parameter</span></button>
                            <%--s:submit cssClass="defaultButton" theme="simple" action="processAddParamAutoEmail" value="Add Parameter" />
                            <s:submit cssClass="defaultButton" theme="simple" action="processDeleteParamAutoEmail" value="Delete Parameter"
                                      onclick="if ( isCheckboxSelected(form.param_selected)) {return confirmDelete();} else {return false;}"/--%>
                        </div>
                    </div>
                    <s:if test="emailParamList.size() > 0">
                        <div class="row mt-2">
                            <div class="col">
                                <div class="table-responsive">
                                    <table class="table table-sds table-sm fs--1 table-striped table-hover" cellspacing="1" cellpadding="1"  width="100%">
                                        <thead class="bg-200 text-900">
                                            <tr>
                                                <th style="width:1%;" class="align-middle white-space-nowrap">
                                                    <div class="form-check fs-0 d-flex align-items-center">
                                                        <s:if test="emailParamList.size() > 0">
                                                            <input type="checkbox"  id="param_select" class="selectAll form-check-input" name="param_select" onClick="toggleCheckboxByName(this,'param_selected');">
                                                            <!--<input type="checkbox" id="param_select" name="param_select" onclick="toggleCheckbox(this, delParam_ids);">-->
                                                        </s:if>
                                                        <s:else>
                                                            <input type="checkbox" id="param_select" class="selectAll form-check-input" name="param_select" disabled >
                                                        </s:else>
                                                        <label for="param_select" class="form-check-label"></label>
                                                    </div>
                                                </th>
                                                <th width="310" class="align-middle white-space-nowrap"><s:text name="autoEmailSetup.paramName" /></th>
                                                <th width="460" class="align-middle white-space-nowrap"><s:text name="autoEmailSetup.paramFrom" /></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <s:iterator value="emailParamList" status="paramStatus" var="paramAutoEmail">
                                                <tr class="<s:if test="#paramStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                                    <td class="align-middle white-space-nowrap">
                                                        <s:hidden theme="simple" name="param_id" value="%{#paramAutoEmail.param_id}" />
                                                        <div class="form-check fs-0 d-flex align-items-center">
                                                            <input type="checkbox" name="param_selected" class="checkbox_child form-check-input" id="${paramStatus.index}" value="${paramStatus.index}" onclick="toggleSelectAll()">
                                                            <label for="${paramStatus.index}" class="form-check-label"></label>
                                                        </div>
                                                        <%--<s:checkbox theme="simple" name="param_selected" id="delParam_ids" fieldValue="%{#paramStatus.index}" onclick="checkToggleCheckbox(param_select, delParam_ids)"/>--%>
                                                    </td>
                                                    <td width="310" class="align-middle white-space-nowrap"><s:textfield cssClass="input-sm form-control form-control-sm" theme="simple" name="param_name" value="%{#paramAutoEmail.param_name}"/></td>
                                                    <td width="460" class="align-middle white-space-nowrap"><s:textfield cssClass="input-sm form-control form-control-sm" theme="simple" name="retrieve_from" value="%{#paramAutoEmail.retrieve_from}"/></td>
                                                </tr>
                                            </s:iterator>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </s:if>
                </div>
            </div>
        </form>
    </body>
</html>