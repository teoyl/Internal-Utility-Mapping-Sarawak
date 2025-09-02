<%-- 
    Document   : init_field_query
    Created on : Sep 18, 2024, 11:14:43 AM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <form action="jobSearchFieldQuery" method="post" id="fieldQuerySearchForm">
                <div class="row me-0 mb-3">
                    <label class="col-sm-3 col-form-label"><s:text name="utimaps.form.hardcopy_search.sjNo" /></label>
                        <div class="col-3 p-0">
                            <s:select 
                                name="division"
                                value="userDiv_"
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
                    <label for="survey_org" class="col-sm-3 col-form-label"><s:text name="utimaps.form.hardcopy_search.surveyOrg" /></label>
                    <div class="col-sm-9 p-0">
                        <s:textfield class="form-control" name = "survey_org" />
                    </div>
                </div>
                <div class="row me-0 mb-3">
                    <label for="location" class="col-sm-3 col-form-label"><s:text name="utimaps.form.hardcopy_search.location" /></label>
                    <div class="col-sm-9 p-0">
                        <s:textfield class="form-control" name = "location" />
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
                                <th scope="col" class="col-md-2 fs-smaller"><s:text name ="utimaps.table.hardcopy_search.caseRef" /></th>
                                <th scope="col" class="col-md-2 fs-smaller"><s:text name ="utimaps.table.hardcopy_search.sjNo" /></th>
                                <th scope="col" class="col-md-3 fs-smaller"><s:text name ="utimaps.table.hardcopy_search.surveyOrg" /></th>
                                <th scope="col" class="col-md-2 fs-smaller"><s:text name ="utimaps.table.hardcopy_search.location" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <s:if test="surveyJobList.size() > 0">
                                <s:iterator value="surveyJobList" status="listStatus" var="surveyJobItem">
                                    <tr class="<s:if test="#listStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                        <td class="col-md-1">
                                            <p class="mb-0"><s:property value='%{#listStatus.index+1}' /></p>
                                        </td>
                                        <td class="col-md-2">
                                            <a href="loadDetailPageFieldQuery?id=<s:property value="%{#surveyJobItem.JOB_ID}" />"><p id="<s:property value="%{#surveyJobItem.CASE_REF}" />" class="mb-0"><s:property value="%{#surveyJobItem.CASE_REF}" /></p></a>
                                        </td>
                                        <td class="col-md-2">
                                            <a href="loadDetailPageFieldQuery?id=<s:property value="%{#surveyJobItem.JOB_ID}" />"><p id="<s:property value="%{#surveyJobItem.JOB_ID}" />" class="mb-0"><s:property value="%{#surveyJobItem.USJ_NO}" /></p></a>
                                        </td>
                                        <td class="col-md-3">
                                            <p class="mb-0"><s:property value="%{#surveyJobItem.CO_NAME}" /></p>
                                        </td>
                                        <td class="col-md-2">
                                            <p class="mb-0"><s:property value="%{#surveyJobItem.LAND_DESC}" /></p>
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
            $('#fieldQuerySearchForm').trigger("reset");
        });

        $('#btnSubmit').click(function(e){
            e.preventDefault();
            $('#fieldQuerySearchForm').submit();
        });
    });
</script>