<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <SCRIPT language="javascript">
            function callMe(data) {
                for (i = 0; i < data.split(";").length; i++) {
                    alert(data.split(";")[i]);
                } 
            }
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
            
            function localValidateForm(form, operation) {
                var errors = new Array();
                validateRequired(form, errors);
                if (form.module_type.value == "S") {
                    if (form.parent_code.value == "") {
                        errors[errors.length] = formatText(messageRequired, "<s:text name='parent.module' />");
                    }
                }
                if (errors.length > 0) {
                    alert(errors.join('\n'));
                    setFocus(form);
                }
                return errors.length > 0 ? false : true;
            }

            function valcheckType() {
                form = document.getElementById("moduleFormId");
                if (form.module_type.value === "S") {
                    return true;
                }
                return false;
            }

            function typeChanged(moduleType) {
                var form = moduleType.form;
                if (moduleType.value === "M") {
                    form.parent_module_id.value = "";
                    form.parent_code.value = "";
                    $(".parentCodeReq").addClass("hidden");
                } else {
                    $(".parentCodeReq").removeClass("hidden");
                }
            }

            function required() {
                this.aa = new Array("module_type", "<s:text name='module.type' />");
                this.ab = new Array("module_code", "<s:text name='module.code' />");
                this.ac = new Array("module_name", "<s:text name='module.name' />");
            }

            $(document).ready(function () {
                loadIcon();
            });
        </SCRIPT>

        <%--<s:head />--%>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form id="moduleFormId" action="processUpdateModule" method="post">
            <s:hidden name="action" />
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <s:hidden name="module_id" value="%{model.module_id}"/>
            <div class="card">
                <div class="card-header bg-light">
                    <h5><s:text name="module"/>&nbsp;<small class="fw-normal text-600"><s:text name="button.edit"/></small></h5>
                </div>
                <div class="card-body">
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="module.type"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select name="module_type" value="%{model.module_type}" theme="simple" list="moduleOptions" listKey="keyData" listValue="valueData" onchange="typeChanged(this)" cssClass="form-control form-control-sm sds-dropdown"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="module.code"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="module_code" value="%{model.module_code}" cssClass="form-control form-control-sm"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="module.name"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="module_name" value="%{model.module_name}" cssClass="form-control form-control-sm"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="module.nameCode"/></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="module_name_code" value="%{model.module_name_code}" cssClass="form-control form-control-sm"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="module.icon"/></label>
                        <div class="col-md-5">
                            <div class="input-group input-group-sm">
                                <s:textfield theme="simple" onchange="loadIcon();" name="module_icon" id="iconId" value="%{model.module_icon}" cssClass="form-control form-control-sm"/>
                                <div id="iconDiv" class="input-group-text" ></div>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="parent.module"/><font class="parentCodeReq asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:hidden name="parent_module_id" id="parent_module_id" value="%{model.parent_module_id}"/>
                            <div class="input-group input-group-sm">
                                <s:textfield id="parent_code" name="parent_code" value="%{parent_code}" readonly="true" cssClass="form-control form-control-sm" required=""/>
                                <span class="input-group-text" onclick="if (valcheckType()) {lookupModal($('#moduleFormId').find('#module_code'), 'modalLookup?lookFor=module_id,module_code&writeTo=parent_module_id,parent_code&lookup=useSetup_Module&displayedColumns=module_code,module_name&filterBy=iconId_as_iconCode', '', 'callMe::=abc;xyz')}"><i class="fa fa-search"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="row mt-3">
                        <div class="col text-end">
                            <button class="btn btn-sm btn-primary" type="submit" name="action:processUpdateModule" id="processUpdateModule" onclick="return localValidateForm(this.form, 'insert')"><i class="fa fa-save"></i> <span class="ms-1"><s:text name="button.save"/></span></button>        
                            <button class="btn btn-sm btn-falcon-default" type="submit" name="action:cancelModule" id="cancelModule"><i class="fa fa-times"></i><span class="ms-1"><s:text name="button.cancel"/></span></button> 
                        </div>
                    </div>            
                </div>
            </div>                    
        </form>
        <script type="text/javascript" language="javascript">
            var mt = document.getElementById("module_type");
            typeChanged(mt);
        </script>
        <div id="iconLoader" class="hidden"/>
    </body>
</html>