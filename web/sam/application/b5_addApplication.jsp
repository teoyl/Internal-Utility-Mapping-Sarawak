<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
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
        <jsp:include page="/pages/base/actionError.jsp"/>
        <%--div class="row">
            <div class="col-md-12">
                <h3 class="title-v3"><s:text name="application" /> <small><s:text name="actionType.add" /></small></h3>
            </div>
        </div--%>

        <form method="post" id="addApplicationFormId" action="processInsertApplication">
            <s:hidden theme="simple" name="action" />
            <s:hidden theme="simple" name="application_id" value="%{model.application_id}"/>
            <div class="card">
                <div class="card-header bg-light">
                    <h5><s:text name="application"/>&nbsp;<small class="fw-normal text-600"><s:text name="button.add"/></small></h5>
                </div>
                <div class="card-body">
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="application.type"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select theme="simple" name="application_type" list="applicationTypeOption" listKey="keyData" listValue="valueData" cssClass="rowText form-control form-control-sm sds-dropdown mySelectBox required"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="application.code"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="application_code" value="%{model.application_code}" cssClass="form-control form-control-sm required"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="application.name"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="application_name" value="%{model.application_name}" cssClass="form-control form-control-sm"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="application.nameCode"/></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="application_name_code" value="%{model.application_name_code}" cssClass="form-control form-control-sm"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="application.icon"/></label>
                        <div class="col-md-5">
                            <div class="input-group input-group-sm">
                                <s:textfield theme="simple" onchange="loadIcon()" name="app_icon" id="iconId" value="%{model.app_icon}" cssClass="form-control form-control-sm"/>
                                <div id="iconDiv" class="input-group-text" ></div>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="application.actionName"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="action_name" value="%{model.action_name}" cssClass="form-control form-control-sm required"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="application.actionClass"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="action_class" value="%{model.action_class}" cssClass="form-control form-control-sm" />
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="application.systemType"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select list="systemTypeOption_" listKey="keyData" listValue="valueData" theme="simple" name="system_type" value="%{model.system_type}" cssClass="rowText form-control form-control-sm sds-dropdown mySelectBox"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="application.order"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="show_in_main_order" value="%{model.show_in_main_order}" cssClass="form-control form-control-sm" />
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="application.attachedTo"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <div class="input-group input-group-sm">
                                <s:textfield id="module_code" title="%{model.attachedModule.module_name}" name="model.attachedModule.module_code" value="%{model.attachedModule.module_code}" readonly="true" cssClass="form-control form-control-sm" required=""/>
                                <s:hidden theme="simple" name="attached_module_id" id="attached_module_id" value="%{model.attached_module_id}"/>
                                <s:hidden theme="simple" id="module_desc" name="model.attachedModule.module_name" value="%{model.attachedModule.module_name}"/>
                                <button class="btn btn-secondary" type="button" onclick="lookupModal($('#module_code'), 'modalLookup?lookFor=module_code,module_id,module_name&writeTo=module_code,attached_module_id,module_desc&lookup=useSetup_Module&displayedColumns=module_code,module_name','','postModule')"><i class="fa fa-search"></i></button>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="common.remarks"/></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="remark" value="%{model.remark}" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Application Rights</label>
                        <div class="col-md-5 row">
                            <div class="col-lg-6">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="hidden" value="Y" id="hidden" <s:if test='model.hidden.equals("Y")'>checked</s:if>>
                                    <label class="form-check-label mb-0" for="hidden"><s:text name="application.hidden"/></label>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="create_right" value="Y" id="create_right" <s:if test='model.create_right.equals("Y")'>checked</s:if>>
                                    <label class="form-check-label mb-0" for="create_right"><s:text name="application.create.right"/></label>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="retrieve_right" value="Y" id="retrieve_right" <s:if test='model.retrieve_right.equals("Y")'>checked</s:if>>
                                    <label class="form-check-label mb-0" for="retrieve_right"><s:text name="application.retrieve.right"/></label>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="update_right" value="Y" id="update_right" <s:if test='model.update_right.equals("Y")'>checked</s:if>>
                                    <label class="form-check-label mb-0" for="update_right"><s:text name="application.update.right"/></label>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="delete_right" value="Y" id="delete_right" <s:if test='model.delete_right.equals("Y")'>checked</s:if>>
                                    <label class="form-check-label mb-0" for="delete_right"><s:text name="application.delete.right"/></label>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="form-check" style="margin-bottom:4px;">
                                    <input class="form-check-input" type="checkbox" name="print_right" value="Y" id="print_right" <s:if test='model.print_right.equals("Y")'>checked</s:if>>
                                    <label class="form-check-label mb-0" for="print_right"><s:text name="application.print.right"/></label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row mt-3">
                        <div class="col-md-12">
                            <h5><s:text name="application.additionalRights" /></h5>
                        </div>
                    </div>
                    <div class="row mt-1">
                        <div class="col-md-12 form-row-margin">
                            <s:if test="has_right('processAddRights')">
                                <button class="btn btn-falcon-default btn-sm" type="submit" name="action:processAddRightsApplication" id="processAddRightsApplication"><i class="fa fa-plus"></i><span class="ms-1">Add Rights</span></button>
                            </s:if>
                            <s:if test="has_right('processDeleteRights')">
                                <button class="btn btn-falcon-default btn-sm" type="submit" name="action:processDeleteRightsApplication" id="processDeleteRightsApplication" onclick="if (isCheckboxSelected(form.rights_selected)) {
                                            return confirmPermanentDelete();
                                        } else {
                                            return false;
                                        }"><i class="fa fa-trash"></i><span class="ms-1">Delete Rights</span></button>
                            </s:if>
                        </div>
                    </div>

                    <s:if test="model.rightsList.size() > 0">
                        <div class="row mt-2">
                            <div class="col">
                                <div class="table-responsive">
                                    <table class="table table-epa table-sm table-striped table-hover fs--1">
                                        <thead class="bg-light">
                                            <tr>
                                                <th style="width:1%;" class="white-space-nowrap">
                                                    <div class="form-check fs-0 d-flex align-items-center">
                                                        <s:if test="model.rightsList.size() > 0">
                                                            <input type="checkbox"  id="rights_select" class="selectAll form-check-input" name="rights_select" onClick="toggleCheckboxByName(this,'rights_selected');">
                                                            <!--<input type="checkbox" id="rights_select" name="rights_select" onclick="toggleCheckbox(this, delRights_ids);">-->
                                                        </s:if>
                                                        <s:else>
                                                            <input type="checkbox" id="rights_select" class="selectAll form-check-input" name="rights_select" disabled >
                                                        </s:else>
                                                         <label for="rights_select" class="form-check-label"></label>
                                                    </div>
                                                </th>
                                                <th style="width:20%;" class="align-middle white-space-nowrap"><s:text name="application.rightsCode" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                                <th style="width:30%;" class="align-middle white-space-nowrap"><s:text name="application.rightDesc" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                                <th class="align-middle white-space-nowrap"><s:text name="application.methodInvoked" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <s:iterator value="model.rightsList" status="rightsStatus" var="rightsApplication">
                                                <tr class="<s:if test="#rightsStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                                    <td class="white-space-nowrap">
                                                        <s:hidden theme="simple" name="rightsList[%{#rightsStatus.index}].app_rights_id" value="%{#rightsApplication.app_rights_id}" />
                                                        <div class="form-check fs-0 mb-0 d-flex align-items-center">
                                                            <input type="checkbox" name="rights_selected" class="checkbox_child form-check-input" id="_${rightsStatus.index}" value="${rightsStatus.index}" onclick="toggleSelectAll()">
                                                            <label for="_${rightsStatus.index}" class="form-check-label"></label>
                                                        </div>
                                                    </td>
                                                    <td class="white-space-nowrap"><s:textfield theme="simple" name="rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#rightsApplication.app_rights_code}" cssClass="form-control-sm form-control"/></td>
                                                    <td class="white-space-nowrap"><s:textfield size="50" theme="simple" name="rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#rightsApplication.app_rights_description}" cssClass="form-control-sm form-control"/></td>
                                                    <td class="white-space-nowrap"><s:textfield size="60" theme="simple" name="rightsList[%{#rightsStatus.index}].app_rights_methods" value="%{#rightsApplication.app_rights_methods}" cssClass="form-control-sm form-control"/></td>
                                                </tr>
                                            </s:iterator>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </s:if>
                    <div class="row">
                        <div class="col text-end">
                            <button class="btn btn-primary btn-sm" type="submit" name="action:processInsertApplication" id="processInsertApplication" onclick="return localValidateForm(this.form, 'insert')"><i class="far fa-save"></i><span class="ms-1"><s:text name="button.save"/></span></button>        
                            <button class="btn btn-falcon-default btn-sm" type="submit" name="action:cancelApplication" id="cancelApplication"><i class="fas fa-times"></i><span class="ms-1"><s:text name="button.cancel"/></span></button>   
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <div id="iconLoader" class="hidden"/>
    </body>
</html>