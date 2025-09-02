<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<script language="javascript">
$(document).ready(function() {
    if($('.completeJobCount').val() > 0){
        $('#myTable${strDurationValue}').dataTable({
        });
    }
});
</script>

<s:hidden name="completeJobCount" cssClass="completeJobCount" value="%{listJobBySystem.size()}"/>

<s:if test='istrSystemId.equals("COMPLETED")'> 
    <div class="table-responsive" >
        <table class="table table-striped  "  id="myTable${strDurationValue}"  >
            <thead>
                <tr class="jobHeader_1">
                    <th class="space_left_5"><s:text name="jobList.jobNo" /></th>
                    <th class="" width="145px"><s:text name="jobList.jobDate" /></th>
                    <th width="25%">Application Details.</th>
                    <th class=""><s:text name="jobList.jobDetail" /></th>
                    <th class=""> <s:text name="jobList.jobCompletionDate" /></th>
                    <th class=""> <s:text name="jobList.diagram" /></th>
                </tr>
            </thead>
            <tbody>
                <br>
                <s:if test="listJobBySystem.size() > 0">
                    <s:iterator value="listJobBySystem" status="jobProgressStatus" var="iteratorJobBySystem">
                        <tr class="
                            <s:if test="#iteratorJobBySystem.get('today') = #iteratorJobBySystem.get('due_date')">
                                due
                            </s:if><s:elseif test="#iteratorJobBySystem.get('today') > #iteratorJobBySystem.get('due_date')">
                                lapse
                            </s:elseif><s:else>
                                <s:if test="#jobPoolStatus.odd == true ">odd</s:if><s:else>even</s:else>
                            </s:else>
                            ">
                            <td class="jobLabel space_left_5">${jobProgressStatus.index + 1}</td>
                            <td class="jobLabel">
                                <s:text name="date_default_datetime"><s:param value="%{#iteratorJobBySystem.get('grabbed_date')}"/></s:text>
                            </td>
                            <td class="jobLabel" style="word-wrap: break-word">
                                <s:property escapeHtml="false" value="%{#iteratorJobBySystem.get('case_ref')}"/>
                            </td>
                            <td class="jobLabel" style="word-wrap: break-word">
                                <s:if test='%{#iteratorJobBySystem.get("wfJobRemark") != null && !#iteratorJobBySystem.get("wfJobRemark").equals("")}'>
                                    <s:a href="#" title='%{#iteratorJobBySystem.get("wfJobRemark")}'>
                                        <s:property value="%{#iteratorJobBySystem.get('wf_name')}"/>: <s:property escapeHtml="false" value="%{#iteratorJobBySystem.get('task_description')}"/>
                                    </s:a>
                                </s:if><s:else>
                                    <s:property value="%{#iteratorJobBySystem.get('wf_name')}"/>: <s:property escapeHtml="false" value="%{#iteratorJobBySystem.get('task_description')}"/>
                                </s:else>
                            </td>
                            <td class="jobLabel">
                                <s:if test="#iteratorJobBySystem.get('completion_date') != null">                                    
                                    <s:text name="date_default_datetime"><s:param value="%{#iteratorJobBySystem.get('completion_date')}"/></s:text>
                                </s:if>   
                            </td>
                            <td class="jobLabel">
                                <a href="loadDiagramPageRouteJobMain?id=<s:property value="%{#iteratorJobBySystem.get('task_id')}"/>" target="_blank" >Show</a>
                            </td>
                        </tr>
                    </s:iterator>
                </s:if>
                <s:else>
                    <tr class="errortxt text-center"><td colspan="8"><s:text name="jobList.jobNone" /></td></tr>
                </s:else>    
            </tbody>
        </table>
    </div>
</s:if>
<s:else>
<div class="table-responsive">
    <table class="table  condensed table-striped" id="myTable"  >
        <thead>
            <tr class="jobHeader_1">
                <th class="space_left_5">!COMPLETED<s:text name="jobList.jobNo" /></th>
                <th class="" width="145px"><s:text name="jobList.jobDate" /></th>
                <th class="" width="145px"><s:text name="jobList.jobDueDate" /></th>
                <th class="">Application Details.</th>
                <th class=""><s:text name="jobList.jobItem" /></th>
                <th class=""><s:text name="jobList.jobDetail" /></th>
                <th class=""> <s:text name="jobList.jobStatus" /></th>
                <th class=""> <s:text name="jobList.diagram" /></th>
            </tr>
        </thead>
        <tbody>
            <s:if test="genListBySystem(istrSystemId) > 0">
                <s:iterator value="listJobBySystem" status="jobProgressStatus" var="iteratorJobBySystem">
                    <%--<tr class="<s:if test="#jobProgressStatus.odd == true ">odd</s:if><s:else>even</s:else>">--%>
                    <tr class="
                        <s:if test="#iteratorJobBySystem.get('today') = #iteratorJobBySystem.get('due_date')">
                            due
                        </s:if><s:elseif test="#iteratorJobBySystem.get('today') > #iteratorJobBySystem.get('due_date')">
                            lapse
                        </s:elseif><s:else>
                            <s:if test="#jobPoolStatus.odd == true ">odd</s:if><s:else>even</s:else>
                        </s:else>
                        ">
                        <td class="jobLabel space_left_5">${jobProgressStatus.index + 1}</td>
                        <td class="jobLabel">
                            <%--<s:property value="%{#iteratorJobBySystem.get('grabbed_date')}"/>--%>
                            <s:text name="date_default_datetime"><s:param value="%{#iteratorJobBySystem.get('grabbed_date')}"/></s:text>
                            </td>
                            <td class="jobLabel">
                            <s:if test="#iteratorJobBySystem.get('due_date') != null">                                    
                                <s:text name="date_default_datetime"><s:param value="%{#iteratorJobBySystem.get('due_date')}"/></s:text>
                                <s:if test="#iteratorJobBySystem.get('today') > #iteratorJobBySystem.get('due_date')">
                                    <button type="button" class="btn btn-default btn-xs"><span class="time" style="color: red;" >Due</span></button>
                                </s:if>
                            </s:if>   
                        </td>
                        <td class="jobLabel" style="word-wrap: break-word">
                            <s:property value="%{#iteratorJobBySystem.get('eCase_ref')}"/>
                        </td>
                        <td id="jobLabel" class="jobLabel">
                            <s:if test='#iteratorJobBySystem.get("jpextra1").equals("RJ")'><span style="font-weight: bold; color: red">[Rejected]</span> </s:if>  <%--ThoTH @ 14-Mar-2014--%>
                            <%-- ThoTH @ 16-Apr-2014 :: skip jobDetail.jsp --%>
                            <%-- <s:a href="loadEditPageJobDetail?istrJpId=%{#iteratorJobBySystem.get('task_id')}" title="Open Job"> --%>
                            <s:if test='#iteratorJobBySystem.get("task_status").equals("F")'>  <%-- 23-Jul-2015 --%>
                                <s:if test='%{#iteratorJobBySystem.get("wfJobRemark") != null && !#iteratorJobBySystem.get("wfJobRemark").equals("")}'>
                                    <s:a href="#" title='%{#iteratorJobBySystem.get("wfJobRemark")}'>
                                        <s:property value="%{#iteratorJobBySystem.get('wf_name')}"/>: <s:property value="%{#iteratorJobBySystem.get('task_description')}"/>
                                    </s:a>
                                </s:if><s:else>
                                    <s:property value="%{#iteratorJobBySystem.get('wf_name')}"/>: <s:property value="%{#iteratorJobBySystem.get('task_description')}"/>
                                </s:else>
                            </s:if><s:else>
                                <s:if test="#iteratorJobBySystem.get('today') = #iteratorJobBySystem.get('due_date')  ">
                                    <s:a style="color:#DD4B39" href="%{#iteratorJobBySystem.get('actionUrl')}" title="View Job">
                                        <s:property value="%{#iteratorJobBySystem.get('actionDesc')}"/>
                                    </s:a>
                                </s:if>
                                <s:elseif test="#iteratorJobBySystem.get('today') > #iteratorJobBySystem.get('due_date')">
                                    <s:a style="color:#DD4B39" href="%{#iteratorJobBySystem.get('actionUrl')}" title="View Job">
                                        <s:property value="%{#iteratorJobBySystem.get('actionDesc')}"/>
                                    </s:a>
                                </s:elseif>
                                <s:else>
                                    <s:iterator value="#iteratorJobBySystem.get('taskAction')" status="actionUrlStatus" var="actionUrl">
                                        <s:a href="%{#actionUrl.get('actionUrl')}" title="View Job">
                                            <s:property value="%{#actionUrl.get('actionDesc')}"/>
                                        </s:a>
                                    </s:iterator>
                                </s:else>
                                <%--<s:a href="%{#iteratorJobBySystem.get('actionUrl')}" title="View Job">
                                    <s:property value="%{#iteratorJobBySystem.get('wf_name')}"/>: <s:property value="%{#iteratorJobBySystem.get('actionDesc')}"/>
                                </s:a>--%>
                            </s:else>
                        </td>
                        <td class="jobLabel" style="word-wrap: break-word">
                            <s:property value="%{#iteratorJobBySystem.get('wf_name')}"/>: <s:property escapeHtml="false" value="%{#iteratorJobBySystem.get('task_description')}"/>
                        </td>
                        <td class="jobLabel">
                            <s:property value="getJobStatusDesc(#iteratorJobBySystem.get('task_status'))"/>
                            <s:if test='#iteratorJobBySystem.get("task_status").equals("F")'>  <%-- 23-Jul-2015 --%>
                                <s:submit type="button" cssClass="defaultButton" theme="simple" value="%{getText('jobList.informed')}" onclick="cancelInformed('%{#iteratorJobBySystem.get('task_id')}')" />
                            </s:if>
                        </td>
                        <td class="jobLabel">
                            <a href="loadDiagramPageRouteJobMain?id=<s:property value="%{#iteratorJobBySystem.get('task_id')}"/>" target="_blank" >Show</a>
                        </td>
                    </tr>
                </s:iterator>
            </s:if>
            <s:else>
                <tr class="errortxt"><td colspan="8"><s:text name="jobList.jobNone" /></td></tr>
                <tr><td colspan="8">&nbsp;</td></tr>
            </s:else>    
        </tbody>
    </table>
</div>
</s:else>
