<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <!--<script type="text/javascript" src="include/inforLoader.js"></script>-->
        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <!--<script type="text/javascript" src="pages/scripts/lookup.js"></script>-->
        <script type="text/javascript" src="pages/scripts/controls.js"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
<!--        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
        <script type="text/javascript" src="pages/scripts/combo_AJAX.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />-->
        <SCRIPT language="javascript">
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
        <jsp:include page="/pages/base/b4_actionError.jsp"/>
        <%--div class="row">
            <div class="col-md-12">
                <h3 class="title-v3"><s:text name="module" /> <small><s:text name="actionType.add" /></small></h3>
            </div>
        </div--%>
        <form id="moduleFormId" action="processUpdateModule" method="post">
            <s:hidden name="action" />
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <s:hidden name="module_id" value="%{model.module_id}"/>
            <div class="kt-portlet">
                <div class="kt-portlet__head">
                    <div class="kt-portlet__head-label">
                        <h3 class="kt-portlet__head-title">
                            <s:text name="module"/>&nbsp;<small><s:text name="button.edit"/></small>
                        </h3> 
                    </div>
                </div>
                <div class="kt-portlet__body">
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="module.type"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select name="module_type" value="%{model.module_type}" theme="simple" list="moduleOptions" listKey="keyData" listValue="valueData" onchange="typeChanged(this)" cssClass="form-control sds-dropdown"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="module.code"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="module_code" value="%{model.module_code}" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="module.name"/><font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="module_name" value="%{model.module_name}" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="module.nameCode"/></label>
                        <div class="col-md-5">
                            <s:textfield theme="simple" name="module_name_code" value="%{model.module_name_code}" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="module.icon"/></label>
                        <div class="col-md-5">
                            <div class="input-group-append">
                                <s:textfield theme="simple" onchange="loadIcon();" name="module_icon" id="iconId" value="%{model.module_icon}" cssClass="form-control"/>
                                <span id="iconDiv" class="input-group-text" ></span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="parent.module"/><font class="parentCodeReq asterisk">*</font></label>
                        <div class="col-md-5">
                            <%--<input type="hidden" name="parent_module_id" value="${model.parent_module_id}" id="parent_module_id"/>--%>
                            <s:hidden name="parent_module_id" id="parent_module_id" value="%{model.parent_module_id}"/>
                            <div class="input-group">
                                <!--<input type="text" readonly id="parent_code" name="parent_code" class="form-control" value="${parent_code}"/>-->
                                <s:textfield id="parent_code" name="parent_code" value="%{parent_code}" readonly="true" cssClass="form-control" required=""/>
                                <div class="input-group-append">
                                    <span class="input-group-text" onclick="if (valcheckType()) {lookupModal($('#module_code'), 'modalLookup?lookFor=module_id,module_code&writeTo=parent_module_id,parent_code&lookup=useSetup_Module&displayedColumns=module_code,module_name')}"><i class="fa fa-search"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
<!--            <div class="row">
                <div class="col-md-3">
                    <div class="form-group form-group-default form-group-default-select2 required">
                        <label></label>
                        
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="form-group form-group-default required">
                        <label></label>
                        
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="form-group form-group-default required">
                        <label><s:text name="module.name"/></label>
                        <s:textfield theme="simple" name="module_name" value="%{model.module_name}" cssClass="form-control"/>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="form-group form-group-default required">
                        <label><s:text name="parent.module"/></label>
                        <div class="input-group">
                            <input type="hidden" name="parent_module_id" value="${model.parent_module_id}" id="parent_module_id"/>
                            <input type="text" id="parent_code" name="parent_code" class="form-control" value="${parent_code}" onchange="loadDescs('useSetup_Module', 'module_code', this, 'module_id, module_code, module_name, module_name', 'parent_module_id, parent_code, lbparent_desc, parent_desc', '2', '', '', 'module_code,module_name_as_name');"/>
                            <div class="input-group-btn">
                                <s:if test="1 != 1">
                                    <script language="javascript">
                                        lookup2("Search Module", "Module", "module_code,module_id,module_name", "parent_code,parent_module_id,lbparent_desc",
                                                "useSetup_Module", "module_code,module_name", "", "valcheckType()", "module_name_as_name, module_code");
                                    </script>Sample
                                </s:if>
                                <script language="javascript">
                                    lookup2("Search Module", "Module", "module_code,module_id,module_name", "parent_code,parent_module_id,lbparent_desc",
                                            "useSetup_Module", "module_code,module_name,module_type", "", "valcheckType()");
                                </script>
                            </div>
                        </div>
                        <label class="label" id="lbparent_desc" for="lbparent_desc" style="margin-top:5px;color:white!important;">${parent_desc}</label>
                        <input type="hidden" id="parent_desc" name="parent_desc" value="${parent_desc}" cssClass="input-md form-control"/>
                    </div>
                </div>
            </div>
            <br><br>  -->
                <div class="kt-portlet__foot">
                    <div class="kt-form__actions"
                        <div class="row">
                            <div class=text-right">
                                <button class="btn btn-brand" type="submit" name="action:processUpdateModule" id="processUpdateModule" onclick="return localValidateForm(this.form, 'insert')"><i class="fa fa-save"></i>Save</button>        
                                <button class="btn btn-outline-brand" type="submit" name="action:cancelModule" id="cancelModule"><i class="fa fa-close"></i>Cancel</button>        
                                <%--s:submit type="button" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="processUpdateModule" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>
                                <s:submit type="button" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="cancelModule" value="Cancel"/--%>
                            </div>
                        </div>
                    </div>
                </div>
                            


            <%--div class="panel panel-default ">
                <div class="panel-heading ">
                    <h3 class="panel-title"> 
                        <span class="titleText"><s:text name="module" /></span>
                        <span class="titleActionTypeText"> | <s:text name="actionType.add" /></span>
                    </h3>
                </div>
                <div class="panel-body">
                    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form table borderless">
                        <tr>
                            <td>
                                <s:submit type="button" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="processUpdateModule" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>
                                <s:submit type="button" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="cancelModule" value="Cancel"/>
                            </td>
                        </tr>
                    </table>
                    <table class="table borderless">
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td class="tdLabel" width="150px"><s:text name="module.type"/><jsp:include page="/pages/base/requiredField.jsp"/>:</td>
                            <td align="left"><s:select name="module_type" value="%{model.module_type}" theme="simple" list="moduleOptions" listKey="keyData" listValue="valueData" onchange="typeChanged(this)" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td class="tdLabel"><s:text name="module.code"/><jsp:include page="/pages/base/requiredField.jsp"/>:</td>
                            <td align="left"><s:textfield theme="simple" name="module_code" value="%{model.module_code}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td class="tdLabel"><s:text name="module.name"/><jsp:include page="/pages/base/requiredField.jsp"/>:</td>
                            <td align="left"><s:if test="1 != 1"><s:textfield theme="simple" name="module_name" value="%{model.module_name}" onchange="triggerLookup('lu_module_name', this, 'module_name_as_name');"/></s:if>
                                <s:textfield theme="simple" name="module_name" value="%{model.module_name}" cssClass="input-md form-control"/>
                            </td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td class="tdLabel"><s:text name="parent.module"/>:</td>
                            <td>
                                <div class="input-group col-lg-2">
                                    <input type="hidden" name="parent_module_id" value="${model.parent_module_id}" id="parent_module_id"/>
                                    <input type="text" id="parent_code" name="parent_code" class="input-md form-control" value="${parent_code}" onchange="loadDescs('useSetup_Module', 'module_code', this, 'module_id, module_code, module_name, module_name', 'parent_module_id, parent_code, lbparent_desc, parent_desc', '2', '', '', 'module_code,module_name_as_name');"/>
                                    <!--<input type="text" id="parent_code" name="parent_code" value="${parent_code}" onchange="loadDescs('useSetup_Module;ne;nc;ssc:1', 'module_code', this, 'module_id, module_code, module_name, module_name', 'parent_module_id, parent_code, lbparent_desc, parent_desc', '2');"/>-->
                                    <div class="input-group-btn ">
                                        <s:if test="1 != 1"><script language="javascript">
                                            lookup("Search Module", "Module", "module_code,module_id,module_name", "parent_code,parent_module_id,lbparent_desc",
                                                    "useSetup_Module", "module_code,module_name", "", "valcheckType()", "module_name_as_name, module_code");
                                            </script><!--Sample--></s:if>
                                            <script language="javascript">
                                                lookup("Search Module", "Module", "module_code,module_id,module_name", "parent_code,parent_module_id,lbparent_desc",
                                                        "useSetup_Module", "module_code,module_name,module_type", "", "valcheckType()");
                                            </script>
                                            <span >
                                                <label class="label" id="lbparent_desc" for="lbparent_desc">${parent_desc}</label>
                                            <input type="hidden" id="parent_desc" name="parent_desc" value="${parent_desc}" cssClass="input-md form-control"/>
                                        </span>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div--%>
        </form>
        <script type="text/javascript" language="javascript">
            var mt = document.getElementById("module_type");
            typeChanged(mt);
//            if (mt.value == "M") {
//                mt.form.parent_code.readOnly = true;
//            } else {
//                mt.form.parent_code.readOnly = false;
//            }
        </script>
        <div id="iconLoader" class="hidden"/>
    </body>
</html>