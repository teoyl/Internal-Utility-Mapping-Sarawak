<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<link href="falcon-v3.16.0/public/vendors/datatables.net-bs5/dataTables.bootstrap5.min.css" rel="stylesheet">
<link href="falcon-v3.16.0/public/vendors/datatables.net-rowReorder/rowReorder.bootstrap5.min.css" rel="stylesheet">
<script src="falcon-v3.16.0/public/vendors/datatables.net/jquery.dataTables.min.js"></script>
<script src="falcon-v3.16.0/public/vendors/datatables.net-bs5/dataTables.bootstrap5.min.js"> </script>
<script src="falcon-v3.16.0/public/vendors/datatables.net-fixedcolumns/dataTables.fixedColumns.min.js"> </script>
<script src="falcon-v3.16.0/public/vendors/datatables.net-rowReorder/dataTables.rowReorder.min.js"> </script>

<script type="text/javascript">
    function submitTemplateForm(formAction) {
        $('.templateFieldRow').each(function (i, row) {
            var $row = $(row);
            $("#order_"+$row.attr("id")).val((i+1)*10);
        });
        submitForm('dqTemplateForm', formAction);
    }

    $(document).ready(function () {
        var table = $('#templateFieldTable').DataTable({
            dom: 't',
            "paging": false
        });
        table.rowReordering();   
        toggleSelectAll('cbShowMe');
    });
</script>

<jsp:include page="/pages/base/actionError.jsp"/>
<form id="dqTemplateForm" method="post" action="processUpdateDqTemplate">
    <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
    <s:hidden name="model.ID" value="%{model.ID}"/>
    <s:hidden name="tmpl_ds_id" value="%{model.tmpl_ds_id}"/>
    <s:hidden name="tmpl_name" value="%{model.tmpl_name}" />
    <s:hidden name="tmpl_desc" value="%{model.tmpl_desc}"/>
    <s:textarea name="tmpl_cond" rows="5" cssClass="hidden" theme="simple"/>
    <div class="card">
        <div class="card-header bg-light">
            <h5><s:text name="DqTemplate.appName"/> <small class="fw-normal text-600"><s:text name="actionType.edit"/></small></h5>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="table-responsive">
                    <table id="templateFieldTable" class="table table-sds table-sm fs--1 table-striped table-hover" width="100%">
                    <thead class="bg-200 text-900">
                        <tr>
                            <th style="width:1%;" class="align-middle white-space-nowrap sort"><s:text name="common.no"/></th>
                            <th style="width:1%;" class="align-middle white-space-nowrap no-sort"><s:text name="DqTemplate.showMe"/><br>
                                <div class="form-check fs-0">
                                    <input type="checkbox"  id="cbselect" class="selectAll form-check-input" name="cbselect" onClick="toggleCheckboxByClassName(this,'cbShowMe');">
                                    <label for="cbselect" class="form-check-label"></label>
                                </div>
                            </th>
                            <th class="align-middle white-space-nowrap no-sort"><s:text name="DqTemplate.fieldName"/></th>
                            <th class="align-middle white-space-nowrap no-sort"><s:text name="DqTemplate.fieldLabel"/></th>
                            <th class="align-middle white-space-nowrap no-sort"><s:text name="DqTemplate.fieldHelperText"/></th>
                            <!--<th><s:text name="DqTemplate.fieldSetup"/></th>--> 
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator value="model.templateFieldList" var="field" status="fieldStatus">
                            <tr id="row-${fieldStatus.index+1}" class="templateFieldRow">
                                <td class="align-middle white-space-nowrap">${fieldStatus.index+1}</td>
                                <td class="align-middle white-space-nowrap">
                                    <div class="checkbox check-success">
                                        <input class="cbShowMe" type="checkbox" <s:if test="#field.fld_showMe_boo">checked</s:if> value="Y" name="model.templateFieldList[${fieldStatus.index}].fld_showMe" id="_${fieldStatus.index}" onclick="toggleSelectAll('cbShowMe')"/>
                                        <label for="_${fieldStatus.index}"></label>
                                    </div>
                                <%--<s:checkbox theme="simple" name="selected" cssClass="checkbox_child" id="%{#resultStatus.index}" fieldValue="%{getResultPrimaryKey(#resultStatus.index)}" onclick="toggleSelectAll();"/>--%>
                                </td>
                                <td class="align-middle white-space-nowrap"><s:textfield readonly="true" value="%{#field.fld_col}" name="model.templateFieldList[%{#fieldStatus.index}].fld_col" cssClass="form-control form-control-sm"/></td>
                                <td class="align-middle white-space-nowrap">
                                    <s:hidden id="order_row-%{#fieldStatus.index+1}" value="%{#field.fld_order}" name="model.templateFieldList[%{#fieldStatus.index}].fld_order" cssClass="form-control form-control-sm"/>
                                    <s:hidden value="%{#field.ID}" name="model.templateFieldList[%{#fieldStatus.index}].ID"/>
                                    <s:textfield value="%{#field.fld_lbl}" name="model.templateFieldList[%{#fieldStatus.index}].fld_lbl" cssClass="form-control form-control-sm"/>
                                </td>
                                <td class="align-middle white-space-nowrap"><s:textfield value="%{#field.fld_helperText}" name="model.templateFieldList[%{#fieldStatus.index}].fld_helperText" cssClass="form-control form-control-sm"/></td>
                            </tr>
                        </s:iterator>
                    </tbody>
                    </table>
                </div> 
            </div>
            <div class="row mb-1">
                <label class="col-md-4 col-form-label col-form-label-sm"></label>
                <div class="col-md-5">
                    <button type="button" class="btn btn-sm btn-primary" name="action:goEditPageDqTemplate" id="actionName" onclick="submitTemplateForm('goEditPageDqTemplate')">
                        <i class="far fa-caret-square-left"></i><span class="ms-1"><s:text name="DqTemplate.previous"/></span>
                    </button>
                    <button type="button" class="btn btn-sm btn-primary" name="action:processUpdateDqTemplate" id="actionName" onclick="submitTemplateForm('processUpdateDqTemplate')">
                        <i class="fa fa-save"></i><span class="ms-1"><s:text name="button.save"/></span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</form>
                    
<div class="modal fade" id="columnsDiv" tabindex="-1" role="dialog" aria-labelledby="columnsDivLabel"></div>
