<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="DqTemplate.appName"/></title>
        <script type="text/javascript">
            function showColumns() {
//                $("#columnsDiv").html("");
                loadUrl = "showColumnsDqTemplate?tmpl_ds_id_str="+$("#tmpl_ds_id").val();
                $("#columnsDiv").load(loadUrl, 
                        function (message) {
                            if (message === "Expired") {
                                document.location = "initLogin";
                            }
                            $("#columnsDiv").data('width', '60%');
                            $('#columnsDiv').modal('show');
//                            $("#columnsDiv").on('hide', function () {
//                                $(field).focus();
//                            });
                        });
            }
            $(document).ready(function () {
            });
        </script>
    </head>
    <body>
        <jsp:include page="/pages/base/b4_actionError.jsp"/>
        <form id="dqTemplateFormId" method="post" action="processUpdateDqTemplate">
            <s:hidden name="model.ID" value="%{model.ID}"/>
            <div class="kt-portlet">
                <div class="kt-portlet__head">
                    <div class="kt-portlet__head-label">
                        <h3 class="kt-portlet__head-title">
                            <s:text name="DqTemplate.appName"/> <small><s:text name="actionType.edit"/></small>
                        </h3>
                    </div>
                </div>
                <div class="kt-portlet__body">
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="DqTemplate.selectDs"/> <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select cssClass="form-control sds-dropdown" theme="simple" list="commList.DqDatasourceList" listKey="keyData" listValue="valueData" name="tmpl_ds_id" id="tmpl_ds_id" value="%{model.tmpl_ds_id}"/>
                        </div>
                        <div class="col-md-3">
                            <button type="button" class="btn btn-brand" name="" id="actionName" onclick="showColumns(); return false;">
                                <i class="fa fa-database"></i><s:text name="DqTemplate.showCols"/>
                            </button>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="DqTemplate.tmpl_name"/> <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield cssClass="form-control" name="tmpl_name" value="%{model.tmpl_name}" required="required"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label"><s:text name="DqTemplate.tmpl_desc"/> <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield cssClass="form-control" name="tmpl_desc" value="%{model.tmpl_desc}" required="required"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label">Condition</label>
                        <div class="col-md-5">
                            <s:textarea name="tmpl_cond" rows="5" cssClass="form-control" value="%{model.tmpl_cond}" />
                        </div>
                    </div>
                </div>
                <div class="kt-portlet__foot">
                    <div class="kt-form__actions">
                        <div class="form-group row">
                            <div class="col-md-12">
                                <button type="submit" class="btn btn-brand" name="action:edit_step2DqTemplate" id="actionName" onclick="">
                                    <i class="fa fa-toggle-right"></i><s:text name="DqTemplate.next"/>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <s:iterator value="model.templateFieldList" var="field" status="fieldStatus">
                <s:hidden value="%{#field.ID}" name="model.templateFieldList[%{#fieldStatus.index}].ID" id="field%{#fieldStatus.index}_ID"/>
                <s:hidden value="%{#field.fld_showMe}" name="model.templateFieldList[%{#fieldStatus.index}].fld_showMe" id="field%{#fieldStatus.index}_showMe"/>
                <s:hidden value="%{#field.fld_order}" name="model.templateFieldList[%{#fieldStatus.index}].fld_order" id="field%{#fieldStatus.index}_order"/>
                <s:hidden value="%{#field.fld_col}" name="model.templateFieldList[%{#fieldStatus.index}].fld_col" id="field%{#fieldStatus.index}_col"/>
                <s:hidden value="%{#field.fld_lbl}" name="model.templateFieldList[%{#fieldStatus.index}].fld_lbl" id="field%{#fieldStatus.index}_lbl"/>
                <s:hidden value="%{#field.fld_helperText}" name="model.templateFieldList[%{#fieldStatus.index}].fld_helperText" id="field%{#fieldStatus.index}_helper"/>
            </s:iterator>
        </form>
    <div class="modal fade" id="columnsDiv" tabindex="-1" role="dialog" aria-labelledby="columnsDivLabel"></div>
    </body>
</html>
