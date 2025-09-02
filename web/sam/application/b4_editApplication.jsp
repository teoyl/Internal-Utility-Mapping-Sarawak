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

        <script language="javascript">
            function loadIcon() {
                if ($("#iconId").val().trim() === "") {
                    $("#iconDiv").html("<i/>");
                } else {
                    $("#iconLoader").load("itemChangeLoader?itemCate=AppIcon&itemValue="+encodeURIComponent($("#iconId").val()),
                        function (message) {
                        if (message === "Expired") {
                            document.location = "initLogin";
                        } else {
                            $("#iconDiv").html(message);
                        }
                    });
                }
            }
            
            function postModule() {
                if ($("#module_desc").val().trim() !== "") {
                    $("#module_code").prop("title", $("#module_desc").val().trim());
                }
            }
            
            function localValidateForm(form, operation) {
                var errors = new Array();
                validateRequired(form, errors);
                validateItems(form, errors);
                if (errors.length > 0) {
                    alert(errors.join('\n'));
                    setFocus(form);
                }
                return errors.length > 0 ? false : true;
            }


            function validateItems(form, errors) {
                if (form.app_rights_code) {
                    if (typeof (form.app_rights_code.type) == "string") {
                        validateItem(form.app_rights_code, form.app_rights_description, form.app_rights_methods, 1, errors);
                    } else {
                        var isError = false;
                        for (var i = 0; i < form.app_rights_code.length; i++) {
                            isError = validateItem(form.app_rights_code[i], form.app_rights_description[i], form.app_rights_methods[i], i + 1, errors);
                            if (isError)
                                break;
                        }
                    }
                }
            }

            function validateItem(rights_code, rights_desc, rights_methods, idx, errors) {
                //if (!selected.checked){
                if (rights_code.value == "") {
                    errors[errors.length] = formatText(messageItemized, idx,
                            formatText(messageRequired, "<s:text name='application.rightsCode' />"));
                    isError = true;
                }
                if (rights_desc.value == "") {
                    errors[errors.length] = formatText(messageItemized, idx,
                            formatText(messageRequired, "<s:text name='application.rightDesc' />"));
                    isError = true;
                }
                if (rights_methods.value == "") {
                    errors[errors.length] = formatText(messageItemized, idx,
                            formatText(messageRequired, "<s:text name='application.methodInvoked' />"));
                    isError = true;
                }
            }

            function required() {
                this.aa = new Array("application_code", "<s:text name='application.code' />");
                this.ab = new Array("application_name", "<s:text name='application.name' />");
                this.ac = new Array("action_name", "<s:text name='application.actionName' />");
                this.ad = new Array("action_class", "<s:text name='application.actionClass' />");
                this.ae = new Array("module_code", "<s:text name='application.attachedTo' />");
            }
            
            $(document).ready(function () {
                loadIcon();
            });
        </script>

        <%--<s:head />--%>
    </head>
    <body>
        <jsp:include page="/pages/base/b4_actionError.jsp"/>
        <%--div class="row">
            <div class="col-md-12">
                <h3 class="title-v3"><s:text name="application" /> <small><s:text name="actionType.add" /></small></h3>
            </div>
        </div--%>

        <form method="post" id="addApplicationFormId" action="processUpdateApplication">
            <s:hidden theme="simple" name="_deletedItem" />
            <s:hidden theme="simple" name="action" />
            <s:hidden theme="simple" name="application_id" value="%{model.application_id}"/>
            <div class="kt-portlet">
                <div class="kt-portlet__head">
                    <div class="kt-portlet__head-label">
                        <h3 class="kt-portlet__head-title">
                            <s:text name="application"/>&nbsp;<small><s:text name="button.edit"/></small>
                        </h3>
                    </div>
                </div>
                <div class="kt-portlet__body">
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="application.type"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select theme="simple" name="application_type" list="applicationTypeOption" listKey="keyData" listValue="valueData" onchange="typeChanged(this)" cssClass="rowText form-control sds-dropdown mySelectBox input-sm required"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="application.code"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="application_code" value="%{model.application_code}" cssClass="form-control required"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="application.name"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="application_name" value="%{model.application_name}" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="application.nameCode"/></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="application_name_code" value="%{model.application_name_code}" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="application.icon"/></label>
                        <div class="col-md-5">
                            <div class="input-group-append">
                                <s:textfield theme="simple" onchange="loadIcon()" name="app_icon" id="iconId" value="%{model.app_icon}" cssClass="form-control"/>
                                <span id="iconDiv" class="input-group-text" ></span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="application.actionName"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="action_name" value="%{model.action_name}" cssClass="form-control required"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="application.actionClass"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="action_class" value="%{model.action_class}" cssClass="form-control" />
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="application.systemType"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select list="systemTypeOption_" listKey="keyData" listValue="valueData" theme="simple" name="system_type" value="%{model.system_type}" cssClass="rowText form-control sds-dropdown mySelectBox"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="application.order"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="show_in_main_order" value="%{model.show_in_main_order}" cssClass="form-control" />
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="application.attachedTo"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <div class="input-group">
                                <s:hidden theme="simple" name="attached_module_id" id="attached_module_id" value="%{model.attached_module_id}"/>
                                <s:hidden theme="simple" id="module_desc" name="model.attachedModule.module_name" value="%{model.attachedModule.module_name}"/>
                                <s:textfield id="module_code" title="%{model.attachedModule.module_name}" name="model.attachedModule.module_code" value="%{model.attachedModule.module_code}" readonly="true" cssClass="form-control" required=""/>
                                <div class="input-group-append">
                                    <span class="input-group-text" onclick="lookupModal($('#module_code'), 'modalLookup?lookFor=module_code,module_id,module_name&writeTo=module_code,attached_module_id,module_desc&lookup=useSetup_Module&displayedColumns=module_code,module_name','','postModule')"><i class="fa fa-search"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="common.remarks"/></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="remark" value="%{model.remark}" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label">Application Rights</label>
                        <div class="col-md-5">
                            <!--<label>Application Rights</label><br>-->
                            <div class="row radio-group radio-group-default entryCb" style="margin-top: 5px;  margin-bottom:10px;">
                                <div class="col-md-6" style="padding-bottom: 8px !important;">
                                    <label class="kt-checkbox kt-checkbox--brand" style="display:inline;">
                                        <input type="checkbox" name="hidden" value="Y" id="hidden" <s:if test='model.hidden.equals("Y")'>checked</s:if>>
                                        <s:text name="application.hidden"/>
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-lg-6" style="padding-bottom: 8px !important;">
                                    <label class="kt-checkbox kt-checkbox--brand" style="display:inline;">
                                        <input type="checkbox" name="create_right" value="Y" id="create_right" <s:if test='model.create_right.equals("Y")'>checked</s:if>>
                                        <s:text name="application.create.right"/>
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-lg-6" style="padding-bottom: 8px !important;">
                                    <label class="kt-checkbox kt-checkbox--brand" style="display:inline;">
                                        <input type="checkbox" name="retrieve_right" value="Y" id="retrieve_right" <s:if test='model.retrieve_right.equals("Y")'>checked</s:if>>
                                        <s:text name="application.retrieve.right"/>
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-lg-6" style="padding-bottom: 8px !important;">
                                    <label class="kt-checkbox kt-checkbox--brand" style="display:inline;">
                                        <input type="checkbox" name="update_right" value="Y" id="update_right" <s:if test='model.update_right.equals("Y")'>checked</s:if>>
                                        <s:text name="application.update.right"/>
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-lg-6" style="padding-bottom: 8px !important;">
                                    <label class="kt-checkbox kt-checkbox--brand" style="display:inline;">
                                        <input type="checkbox" name="delete_right" value="Y" id="delete_right" <s:if test='model.delete_right.equals("Y")'>checked</s:if>>
                                        <s:text name="application.delete.right"/>
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-lg-6" style="padding-bottom: 8px !important;">
                                    <label class="kt-checkbox kt-checkbox--brand" style="display:inline;">
                                        <input type="checkbox" name="print_right" value="Y" id="print_right" <s:if test='model.print_right.equals("Y")'>checked</s:if>>
                                        <s:text name="application.print.right"/>
                                        <span></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>

    <!--                <div class="col-md-3">
                        <div class="form-group form-group-default required">
                            <label><s:text name="application.actionName"/></label>
                            <s:textfield theme="simple" name="action_name" value="%{model.action_name}" cssClass="form-control"/>
                        </div>
                        <div class="form-group form-group-default required">
                            <label><s:text name="application.actionClass"/></label>
                            <s:textfield theme="simple" name="action_class" value="%{model.action_class}" cssClass="form-control"/>
                        </div>
                        <div class="form-group form-group-default form-group-default-select2">
                            <label><s:text name="application.systemType"/></label>
                            <s:select list="systemTypeOption_" data-init-plugin="select2" listKey="keyData" listValue="valueData" theme="simple" name="system_type" value="%{model.system_type}" cssClass="full-width" />
                        </div>
                    </div>-->
    <!--                <div class="col-md-3">
                        <div class="form-group form-group-default">
                            <label><s:text name="application.order"/></label>
                            <s:textfield theme="simple" name="show_in_main_order" value="%{model.show_in_main_order}" cssClass="form-control"/>
                        </div>
                        <div class="form-group form-group-default required">
                            <label><s:text name="application.attachedTo"/></label>
                            <div class="input-group">
                                <s:hidden theme="simple" name="attached_module_id" value="%{model.attached_module_id}"/>
                                <input type="text" id="module_code" name="model.attachedModule.module_code" value="${model.attachedModule.module_code}" readonly class="input-sm form-control"/>
                                <div class="input-group-btn">
                                    <script language="javascript">
                                        lookup2("Search Module", "Module", "module_code,module_id,module_name,module_name", "module_code,attached_module_id,lbmodule_desc,module_desc",
                                                "useSetup_Module", "module_code,module_name", "", "");</script>
                                </div>
                            </div>
                            <s:hidden theme="simple" id="module_desc" name="model.attachedModule.module_name" value="%{model.attachedModule.module_name}"/>
                            <label class="label" id="lbmodule_desc" for="lbmodule_desc" style="margin-top:5px;color:white!important;">${model.attachedModule.module_name}</label>
                        </div>     
                        <div class="form-group form-group-default required">
                            <label><s:text name="common.remarks"/></label>
                            <s:textfield theme="simple" name="remark" value="%{model.remark}" cssClass="form-control"/>
                        </div>
                    </div>-->
    <!--                <div class="col-md-3">
                        <div class="radio-group radio-group-default">
                            <label>Application Rights</label><br>
                            <div class="checkbox check-success" style="margin-top:0px;">
                                <input type="checkbox" name="hidden" value="Y" id="hidden" <s:if test='model.hidden.equals("Y")'>checked</s:if>>
                                <label for="hidden"><s:text name="application.hidden"/></label>
                            </div>
                            <div class="checkbox check-success">
                                <input type="checkbox" name="create_right" value="Y" id="create_right" <s:if test='model.create_right.equals("Y")'>checked</s:if>>
                                <label for="create_right"><s:text name="application.create.right"/></label>
                            </div>
                            <div class="checkbox check-success">
                                <input type="checkbox" name="retrieve_right" value="Y" id="retrieve_right" <s:if test='model.retrieve_right.equals("Y")'>checked</s:if>>
                                <label for="retrieve_right"><s:text name="application.retrieve.right"/></label>
                            </div>
                            <div class="checkbox check-success">
                                <input type="checkbox" name="update_right" value="Y" id="update_right" <s:if test='model.update_right.equals("Y")'>checked</s:if>>
                                <label for="update_right"><s:text name="application.update.right"/></label>
                            </div>
                            <div class="checkbox check-success">
                                <input type="checkbox" name="delete_right" value="Y" id="delete_right" <s:if test='model.delete_right.equals("Y")'>checked</s:if>>
                                <label for="delete_right"><s:text name="application.delete.right"/></label>
                            </div>
                            <div class="checkbox check-success" style="margin-bottom:4px;">
                                <input type="checkbox" name="print_right" value="Y" id="print_right" <s:if test='model.print_right.equals("Y")'>checked</s:if>>
                                <label for="print_right"><s:text name="application.print.right"/></label>
                            </div>
                        </div>
                    </div>-->
    
                    <div class="form-group row">
                        <div class="col-md-12">
                            <h4 class="title-v3"><s:text name="application.additionalRights" /></h4>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-md-12 form-row-margin">
                            <s:if test="has_right('processAddRights')">
                                <button class="btn btn-outline-brand" type="submit" name="action:processAddRightsApplication" id="processAddRightsApplication"><i class="fa fa-plus"></i>Add Rights</button>
                            </s:if>
                            <s:if test="has_right('processDeleteRights')">
                                <button class="btn btn-outline-brand" type="submit" name="action:processDeleteRightsApplication" id="processDeleteRightsApplication" onclick="if (isCheckboxSelected(form.rights_selected)) {
                                            return confirmPermanentDelete();
                                        } else {
                                            return false;
                                        }"><i class="fa fa-trash"></i>Delete Rights</button>
                            </s:if>
                            <%--s:if test="has_right('processAddRights')">
                                <s:submit cssClass="defaultButton" theme="simple" action="processAddRightsApplication" value="Add Rights" /></s:if>
                            <s:if test="has_right('processDeleteRights')">
                                <s:submit cssClass="defaultButton" theme="simple" action="processDeleteRightsApplication" value="Delete Rights"
                                          onclick="if ( isCheckboxSelected(form.rights_selected)) {return confirmPermanentDelete();} else {return false;}"/>
                            </s:if--%>
                        </div>
                    </div>


            <s:if test="model.rightsList.size() > 0">
                <div class="form-group row">
                    <div class="col-md-12">
                        <div class="table-responsive">
                            <table class="table table-epa table-condensed table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th width="10">
                                            <div class="checkbox check-success tableCheckbox">
                                                <label class="kt-checkbox kt-checkbox--brand">
                                                    <s:if test="model.rightsList.size() > 0">
                                                        <input type="checkbox"  id="rights_select" class="selectAll" name="rights_select" onClick="toggleCheckboxByName(this,'rights_selected');">
                                                        <!--<input type="checkbox" id="rights_select" name="rights_select" onclick="toggleCheckbox(this, delRights_ids);">-->
                                                    </s:if>
                                                    <s:else>
                                                        <input type="checkbox" id="rights_select" class="selectAll" name="rights_select" disabled >
                                                    </s:else>
                                                    <!--<label for="rights_select" class="tableFormCheckbox"></label>-->
                                                    <span></span>
                                                </label>
                                            </div>
                                        </th>
                                        <th width="140"><s:text name="application.rightsCode" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                        <th width="310"><s:text name="application.rightDesc" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                        <th width="460"><s:text name="application.methodInvoked" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:iterator value="model.rightsList" status="rightsStatus" var="rightsApplication">
                                        <tr class="<s:if test="#rightsStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                            <s:hidden theme="simple" name="rightsList[%{#rightsStatus.index}].app_rights_id" value="%{#rightsApplication.app_rights_id}" />
                                            <td>
                                                <div class="checkbox check-success tableFormCheckbox">
                                                    <label class="kt-checkbox kt-checkbox--brand">
                                                        <input type="checkbox" name="rights_selected" class="checkbox_child" id="_${rightsStatus.index}" value="${rightsStatus.index}" onclick="toggleSelectAll()">
                                                        <%--<label for="_${rightsStatus.index}" class="tableFormCheckbox"></label>--%>
                                                        <span></span>
                                                    </label>
                                                </div>
                                                <%--<s:checkbox theme="simple" name="rights_selected" id="delRights_ids" fieldValue="%{#rightsStatus.index}" onclick="checkToggleCheckbox(rights_select, delRights_ids)" />--%>
                                            </td>
                                            <td width="100"><s:textfield theme="simple" name="rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#rightsApplication.app_rights_code}" cssClass="input-sm form-control"/></td>
                                            <td width="200"><s:textfield size="50" theme="simple" name="rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#rightsApplication.app_rights_description}" cssClass="input-sm form-control"/></td>
                                            <td width="200"><s:textfield size="60" theme="simple" name="rightsList[%{#rightsStatus.index}].app_rights_methods" value="%{#rightsApplication.app_rights_methods}" cssClass="input-sm form-control"/></td>
                                        </tr>
                                    </s:iterator>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </s:if>
            </div>

            <%--div class="row">
                <div class="col-md-4 rowLabel"><s:text name="application.hidden"/></div>
                <div class="col-md-6 rowText">
                    <s:checkbox theme="simple" name="hidden" fieldValue="Y" value='%{model.hidden.equals("Y")?"True":"false"}' />
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 rowLabel"><s:text name="application.create.right"/></div>
                <div class="col-md-6 rowText">
                    <s:checkbox theme="simple" name="create_right" fieldValue="Y" value='%{model.create_right.equals("Y")?"True":"false"}'/>
                </div>	
            </div>
            <div class="row">
                <div class="col-md-4 rowLabel"><s:text name="application.retrieve.right"/></div>
                <div class="col-md-6 rowText">
                    <s:checkbox theme="simple" name="retrieve_right" fieldValue="Y" value='%{model.retrieve_right.equals("Y")?"True":"false"}' />
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 rowLabel"><s:text name="application.update.right"/></div>
                <div class="col-md-6 rowText">
                    <s:checkbox theme="simple" name="update_right" fieldValue="Y"  value='%{model.update_right.equals("Y")?"True":"false"}' />
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 rowLabel"><s:text name="application.delete.right"/></div>
                <div class="col-md-6 rowText">
                    <s:checkbox theme="simple" name="delete_right" fieldValue="Y" value='%{model.delete_right.equals("Y")?"True":"false"}' />
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 rowLabel"><s:text name="application.print.right"/></div>
                <div class="col-md-6 rowText">
                    <s:checkbox theme="simple" name="print_right" fieldValue="Y" value='%{model.print_right.equals("Y")?"True":"false"}' />
                </div>
            </div--%>
            
            <div class="kt-portlet__foot">
                <div class="kt-form__actions">
                    <div class="row">
                        <div class="col-md-12 text-right">
                            <button class="btn btn-brand" type="submit" name="action:processUpdateApplication" id="processUpdateApplication" onclick="return localValidateForm(this.form, 'insert')"><i class="fa fa-save"></i><s:text name="button.save"/></button>        
                            <button class="btn btn-outline-brand" type="submit" name="action:cancelApplication" id="cancelApplication"><i class="fa fa-close"></i><s:text name="button.cancel"/></button>        
                            <%--s:submit type="submit" cssClass="defaultButton dynamic-pull mrg-lr-5" theme="simple" action="processUpdateApplication" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>
                            <s:submit type="submit" cssClass="defaultButton dynamic-pull mrg-lr-5" theme="simple" action="cancelApplication" value="Cancel"/--%>
                        </div>
                    </div>
                </div>
            </div>
                        



            <%--div class="panel panel-default ">  
                <div class="panel-body">
                    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form table borderless">
                        <tr>
                            <td align="right">
                                <s:submit type="submit" cssClass="defaultButton dynamic-pull mrg-lr-5" theme="simple" action="processUpdateApplication" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>
                                <s:submit type="submit" cssClass="defaultButton dynamic-pull mrg-lr-5" theme="simple" action="cancelApplication" value="Cancel"/>
                            </td>
                        </tr>
                    </table>
                    <table class="form table borderless">
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px"><s:text name="application.type"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"  class="form-group "><s:select theme="simple" name="application_type" list="applicationTypeOption" listKey="keyData" listValue="valueData" onchange="typeChanged(this)" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td ><s:text name="application.code"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:textfield theme="simple" name="application_code" value="%{model.application_code}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td ><s:text name="application.name"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td><s:textfield theme="simple" name="application_name" value="%{model.application_name}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td ><s:text name="application.actionName"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td><s:textfield theme="simple" name="action_name" value="%{model.action_name}" size="60" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td ><s:text name="application.actionClass"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td><s:textfield theme="simple" name="action_class" value="%{model.action_class}" size="60" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td><s:text name="application.systemType"/></td>
                            <td>:</td>
                            <td><s:select list="systemTypeOption_" listKey="keyData" listValue="valueData" theme="simple" name="system_type" value="%{model.system_type}" cssClass="input-md form-control" /></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td><s:text name="application.order"/></td>
                            <td>:</td>
                            <td><s:textfield theme="simple" name="show_in_main_order" value="%{model.show_in_main_order}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td ><s:text name="application.attachedTo"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td>
                                <div class="input-group">
                                    <s:hidden theme="simple" name="attached_module_id" value="%{model.attached_module_id}"/>
                                    <input type="text" id="module_code" name="model.attachedModule.module_code" value="${model.attachedModule.module_code}" readonly class="input-md form-control"/>
                                    <div class="input-group-btn">
                                        <script language="javascript">
                                            lookup("Search Module", "Module", "module_code,module_id,module_name,module_name", "module_code,attached_module_id,lbmodule_desc,module_desc",
                                                    "useSetup_Module", "module_code,module_name", "", "");</script>
                                            <s:hidden theme="simple" id="module_desc" name="model.attachedModule.module_name" value="%{model.attachedModule.module_name}"/>
                                        <label class="label" id="lbmodule_desc" for="lbmodule_desc">${model.attachedModule.module_name}</label>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td><s:text name="application.remark"/></td>
                            <td>:</td>
                            <td><s:textfield size="80" theme="simple" name="remark" value="%{model.remark}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td ><s:text name="application.hidden"/></td>
                            <td width="3px">:</td>
                            <td><s:checkbox theme="simple" name="hidden" fieldValue="Y" value='%{model.hidden.equals("Y")?"True":"false"}' /></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td ><s:text name="application.create.right"/></td>
                            <td width="3px">:</td>
                            <td><s:checkbox theme="simple" name="create_right" fieldValue="Y" value='%{model.create_right.equals("Y")?"True":"false"}' /></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td ><s:text name="application.retrieve.right"/></td>
                            <td width="3px">:</td>
                            <td><s:checkbox theme="simple" name="retrieve_right" fieldValue="Y" value='%{model.retrieve_right.equals("Y")?"True":"false"}' /></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td ><s:text name="application.update.right"/></td>
                            <td width="3px">:</td>
                            <td><s:checkbox theme="simple" name="update_right" fieldValue="Y" value='%{model.update_right.equals("Y")?"True":"false"}' /></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td ><s:text name="application.delete.right"/></td>
                            <td width="3px">:</td>
                            <td><s:checkbox theme="simple" name="delete_right" fieldValue="Y" value='%{model.delete_right.equals("Y")?"True":"false"}'/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td ><s:text name="application.print.right"/></td>
                            <td width="3px">:</td>
                            <td><s:checkbox theme="simple" name="print_right" fieldValue="Y" value='%{model.print_right.equals("Y")?"True":"false"}' /></td>
                        </tr>
                    </table>
                    <hr>
                    <table width="100%" class="form table borderless">
                        <tr class="CLASS_TABLE_HEADER " >
                            <td class="header_2 "><span class="imgArrowRight" /><span class="header_2Text"><s:text name="application.additionalRights" /></span></td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <s:if test="has_right('processAddRights')">
                                    <s:submit cssClass="defaultButton" theme="simple" action="processAddRightsApplication" value="Add Rights" />
                                </s:if>
                                <s:if test="has_right('processDeleteRights')">
                                    <s:submit cssClass="defaultButton" theme="simple" action="processDeleteRightsApplication" value="Delete Rights"
                                              onclick="if ( isCheckboxSelected(form.rights_selected)) {return confirmPermanentDelete();} else {return false;}"/>
                                </s:if>
                            </td>
                        </tr>
                    </table>
                    <table class="defaultTable  table borderless">
                        <tr>
                            <th width="10">
                                <s:if test="model.rightsList.size() > 0">
                                    <input type="checkbox" id="rights_select" name="rights_select" onclick="toggleCheckbox(this, delRights_ids);">
                                </s:if>
                                <s:else>
                                    <input type="checkbox" id="rights_select" name="rights_select" disabled >
                                </s:else>
                            </th>
                            <th width="140"><s:text name="application.rightsCode" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                            <th width="310"><s:text name="application.rightDesc" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                            <th width="460"><s:text name="application.methodInvoked" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                        </tr>
                        <s:iterator value="model.rightsList" status="rightsStatus" id="rightsApplication">
                            <tr class="<s:if test="#rightsStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                <s:hidden theme="simple" name="rightsList[%{#rightsStatus.index}].app_rights_id" value="%{#rightsApplication.app_rights_id}" />
                                <td><s:checkbox theme="simple" name="rights_selected" id="delRights_ids" fieldValue="%{#rightsStatus.index}" onclick="checkToggleCheckbox(rights_select, delRights_ids)"/></td>
                                <td width="100"><s:textfield theme="simple" name="rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#rightsApplication.app_rights_code}" cssClass="input-md form-control"/></td>
                                <td width="200"><s:textfield size="50" theme="simple" name="rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#rightsApplication.app_rights_description}" cssClass="input-md form-control"/></td>
                                <td width="200"><s:textfield size="60" theme="simple" name="rightsList[%{#rightsStatus.index}].app_rights_methods" value="%{#rightsApplication.app_rights_methods}" cssClass="input-md form-control"/></td>
                            </tr>
                        </s:iterator>
                    </table>
                </div--%>
            </div>
        </form>
        <div id="iconLoader" class="hidden"/>
    </body>
</html>