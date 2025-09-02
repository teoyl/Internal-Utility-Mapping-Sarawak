<%-- 
    Document   : b4_jobInProgress
    Created on : Oct 5, 2021, 4:15:55 PM
    Author     : seren
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<div class="table-responsive">
    <!--<table class="table table-condensed table-striped myTable2" cellspacing="0" cellpadding="0" width="100%">-->
    <table id="pendingTaskTable" class="table table-striped " >
        <thead>
            <%--<tr>
                <td colspan="9">
                    <a href="loadEditPageRouteJobMain?ltype=grp" class="btn btn-default"><i class="fa fa-users"></i> Assign Multiple Jobs</a>
                </td>
            </tr>--%>
            <tr>
                <th width="2%"><s:text name="jobList.jobNo" /></th>
                <th width="5%"><s:text name="jobList.jobDate" /></th>
                <th width="5%"> <s:text name="jobList.from" /></th>
                <!--<th width="10%"><s:text name="jobList.jobDueDate" /></th>-->
                <th width="25%">Application Details</th>
                <th width="20%"><s:text name="jobList.jobItem" /></th>
                <th width="7%"><s:text name="jobpool.job.history"/></th>
                <th width="10%"><s:text name="jobList.jobDetail" /></th>
                <!--<th width="10%"><s:text name="jobList.jobStatus" /></th>-->
            </tr>
        </thead>
        <tbody>
        <br>
        <s:if test="genListBySystem(selected_sys_id) > 0">
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
                    <td class="jobLabel" width="10%">
                        <s:text name="date_default_datetime"><s:param value="%{#iteratorJobBySystem.get('grabbed_date')}"/></s:text>
                        </td>
                        <td class="jobLabel">
                        <!--${iteratorJobBySystem.get('previousTaskDoer')}-->
                        ${iteratorJobBySystem.get('previousTaskDoerName')}
                    </td>
                    <!--                                                    <td class="jobLabel" width="10%">
                    <s:if test="#iteratorJobBySystem.get('due_date') != null">                                    
                        <s:text name="date_default_datetime"><s:param value="%{#iteratorJobBySystem.get('due_date')}"/></s:text>
                        <s:if test="#iteratorJobBySystem.get('today') > #iteratorJobBySystem.get('due_date')">
                            <button type="button" class="btn btn-default btn-xs"><span class="time" style="color: red;" >Due</span></button>
                        </s:if>
                    </s:if>   
                </td>-->
                    <td class="jobLabel content-wrap" width="25%">
                        <%--<s:a href="#" title="Case Details" onclick="$('#caseDetail_%{#jobProgressStatus.index}').data('width', '90%'); $('#caseDetail_%{#jobProgressStatus.index}').modal('show');">--%>
                        <s:property escapeHtml="false" value="%{#iteratorJobBySystem.get('case_ref')}"/>
                        <%--</s:a>--%>
                    </td>
                    <td id="jobLabel" class="jobLabel" width="20%">
                        <s:if test='#iteratorJobBySystem.get("jpextra1").equals("RJ")'><span style="font-weight: bold; color: red">[Rejected]</span> </s:if>  
                        <s:if test='#iteratorJobBySystem.get("task_status").equals("F")'>  
                            <s:property value="%{#iteratorJobBySystem.get('wf_name')}"/>: <s:property value="%{#iteratorJobBySystem.get('task_description')}"/>
                        </s:if>
                        <s:else>
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
                                    <s:if test="#actionUrlStatus.index > 0"><br></s:if>
                                    <s:else>
                                        <s:if test='%{#iteratorJobBySystem.get("wfJobRemark") != null && !#iteratorJobBySystem.get("wfJobRemark").equals("")}'>
                                            <i title="<s:property escapeHtml="false" value="%{#iteratorJobBySystem.get('wfJobRemark')}"/>" class="fa fa-2x fa-commenting text-green" style="font-size:18px"></i><br>
                                        </s:if>
                                    </s:else>
                                    <a href="<s:property value="%{#actionUrl.get('actionUrl')}"/>">
                                        <s:property value="%{#actionUrl.get('actionDesc')}"/>
                                        <a><br>
                                        </s:iterator>
                                    </s:else>
                                </s:else>
                                </td>
                                <td class="jobLabel content-wrap" width="7%">
                                    <!--<a href="javascript:;" onclick="loadHistory('loadUserTaskHistoryRouteJobMain?cid=<s:property value="%{#iteratorJobBySystem.get('caseId')}"/>')"  title="History"><i class="fa fa-clock-o" style="margin-left:10px;font-size:14px;"></i></a>-->
                                    <!--<br>-->
                                    <a href="loadDiagramPageRouteJobMain?id=<s:property value="%{#iteratorJobBySystem.get('task_id')}"/>" target="_blank" >Diagram</a>
                                </td>
                                <td class="jobLabel content-wrap" width="23%">
                                    <s:property value="%{#iteratorJobBySystem.get('wf_name')}"/>: <s:property escapeHtml="false" value="%{#iteratorJobBySystem.get('task_description')}"/>
                                </td>
                                <!--                                                    <td class="jobLabel">
                                <%--<s:property value="getJobStatusDesc(#iteratorJobBySystem.get('task_status'))"/>--%>
                                <%--<s:if test='#iteratorJobBySystem.get("task_status").equals("F")'>--%>  
                                <%--<s:submit type="button" cssClass="defaultButton" theme="simple" value="%{getText('jobList.informed')}" onclick="cancelInformed('%{#iteratorJobBySystem.get('task_id')}')" />--%>
                                <%--</s:if>--%>
                            </td>-->

                                </tr>
                            </s:iterator>
                        </s:if>
                        <s:else>
                            <tr class="errortxt"><td colspan="9" class="text-center"><s:text name="jobList.jobNone" /></td></tr>
                            <tr><td colspan="9">&nbsp;</td></tr>
                        </s:else>    
                        </tbody>
                        </table>
                        </div>