<%-- 
    Document   : search_misc_plan
    Created on : May 2, 2025, 4:10:56 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <form action="searchMiscPlan" method="post" id="searchMiscPlanForm">
                <div class="row me-0 mb-3">
                    <label for="plan_no" class="col-sm-3 col-form-label"><s:text name="utimaps.search.miscPlan.planNo" /></label>
                    <div class="col-sm-9 p-0">
                        <s:textfield class="form-control" name = "plan_no" />
                    </div>
                </div>
                <div class="row me-0 mb-3">
                    <label class="col-sm-3 col-form-label"><s:text name="utimaps.search.miscPlan.sjNo" /></label>
                        <div class="col-3 p-0">
                            <s:select 
                                name="usjDiv_"
                                list="_DivisionList"
                                listKey="keyData"
                                listValue="valueData"                    
                                cssClass="form-select"
                             />
                        </div>
                        <div class="col-3 p-0">
                            <s:textfield class="form-control" name = "usj_seq" placeholder="Sequence" maxlength="4" />
                        </div>
                        <div class="col-3 p-0">
                            <s:textfield class="form-control" name = "usj_year" placeholder="Year" maxlength="4" />
                        </div>
                </div>
                <div class="row me-0 mb-3">
                    <label for="title" class="col-sm-3 col-form-label"><s:text name="utimaps.search.miscPlan.title" /></label>
                    <div class="col-sm-9 p-0">
                        <s:textfield class="form-control" name = "title" />
                    </div>
                </div>
                <div class="row me-0 mb-3 text-end">
                    <div class="col-12 p-0">
                        <button type="submit" class="btn btn-primary" id="btnSubmit"><s:text name="utimaps.form.hardcopy_search.search" /></button>
                        <button type="button" class="btn btn-secondary" id="btnReset"><s:text name="utimaps.form.hardcopy_search.reset" /></button>
                    </div>
                </div>
            </form>
        </div>

        <div class="col-md-12">
            <div class="card">
                <div class="card-header">
                    <h4 class="border-bottom"><s:text name="utimaps.job.listing" /></h4>
                </div>
                <div class="card-body">
                    <table class="table table-hover table-striped overflow-hidden text-center" id="surveyjob_searchTable">
                        <thead>
                            <tr>
                                <th scope="col" class="col-md-1 fs-smaller"><s:text name ="common.no" /></th>
                                <th scope="col" class="col-md-2 fs-smaller"><s:text name ="utimaps.table.miscPlan.planNo" /></th>
                                <th scope="col" class="col-md-2 fs-smaller"><s:text name ="utimaps.table.miscPlan.sjNo" /></th>
                                <th scope="col" class="col-md-2 fs-smaller"><s:text name ="utimaps.table.miscPlan.title" /></th>
                                <th scope="col" class="col-md-3 fs-smaller"><s:text name ="utimaps.table.miscPlan.fileRef" /></th>
                                <th scope="col" class="col-md-2 fs-smaller"><s:text name ="utimaps.table.miscPlan.fieldBook" /></th>
                                <th scope="col" class="col-md-2 fs-smaller"><s:text name ="utimaps.table.miscPlan.sheetRef" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <s:if test="miscPlanList.size() > 0">
                                <s:iterator value="miscPlanList" status="listStatus" var="miscPlanItem">
                                    <tr class="<s:if test="#listStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                        <td class="col-md-1">
                                            <p class="mb-0"><s:property value='%{#listStatus.index+1}' /></p>
                                        </td>
                                        <td class="col-md-2">
                                            <a href="loadEditPageMiscPlan?id=<s:property value="%{#miscPlanItem.MISC_PLAN_ID}" />"><p id="<s:property value="%{#miscPlanItem.PLAN_NO}" />" class="mb-0"><s:property value="%{#miscPlanItem.PLAN_NO}" /></p></a>
                                        </td>
                                        <td class="col-md-2">
                                            <a href="loadEditPageMiscPlan?id=<s:property value="%{#miscPlanItem.MISC_PLAN_ID}" />"><p id="<s:property value="%{#miscPlanItem.SURVEY_JOB_NO}" />" class="mb-0">USJ/<s:property value="%{#miscPlanItem.USJ_DIV}" />/<s:property value="%{#miscPlanItem.USJ_SEQ}" />/<s:property value="%{#miscPlanItem.USJ_YEAR}" /></p></a>
                                        </td>
                                        <td class="col-md-2">
                                            <p class="mb-0"><s:property value="%{#miscPlanItem.PLAN_TITLE}" /></p>
                                        </td>
                                        <td class="col-md-3">
                                            <p class="mb-0"><s:property value="%{#miscPlanItem.FILE_REF}" /></p>
                                        </td>
                                        <td class="col-md-2">
                                            <p class="mb-0"><s:property value="%{#miscPlanItem.FIELD_BOOK}" /></p>
                                        </td>
                                        <td class="col-md-2">
                                            <p class="mb-0"><s:property value="%{#miscPlanItem.SHEET_REF}" /></p>
                                        </td>
                                    </tr>
                                </s:iterator>
                            </s:if>
                            <s:else>

                            </s:else>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


<script nonce="EuTVqS192VKl">
    $(document).ready(function() {
        var table = $('#surveyjob_searchTable').DataTable({
            columnDefs: [ 
            {width: 10, targets: 0},
            {width: 100, targets: 1},
            {width: 100, targets: 2},
            {width: 200, targets: 3},
            {width: 200, targets: 4}
            ]
        });
        
        $('#btnReset').click(function(e){
            e.preventDefault();
            $('#searchMiscPlanForm').trigger("reset");
            $('#usjDiv_').val('');
        });

        $('#btnSubmit').click(function(e){
            e.preventDefault();
            $('#searchMiscPlanForm').submit();
        });
    });
</script>