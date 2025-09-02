<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script type="text/javascript">
            function updateChanges() {
                if (validateForm_bshor('schedularFormId')) {
                    submitForm('schedularFormId', 'processUpdateSysSchedular');
                } else {
                    alert("<s:text name="field.redRequired"/>");
                }
                return false;
            }
            $(document).ready(function () {
                $(function () {
                    $('[data-toggle="tooltip"]').tooltip()
                })
            });
        </script>
    </head>
    <body>
        <form name="schedularForm" id="schedularFormId" method="post" action="processUpdateSysSchedular">
            <div class="card">
                <div class="card-header bg-light">
                    <h5>Schedular Maintenance (Main switch is <s:property value="mainSwitchValue"/>)</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive scrollbar mb-3">
                        <table width="100%" class="table table-sds table-sm fs--1 table-striped table-hover">
                            <thead class="bg-200 text-900">
                                <tr>
                                    <th width="15%" class="align-middle">Job Description</th>
                                    <th width="15%" class="align-middle">Job Type</th>
                                    <th width="15%" class="align-middle"><a type="button" class="link-info" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-html="true" 
                                               title="<li>Yearly = MM-dd HH:mm (eg 01-03 14:00 for 3rd Jan 2pm)</li>
                                               <li>Monthly = dd HH:mm (eg 03 14:00 for 3rd every month 2pm)</li>
                                               <li>Day of Week = HH:mm day (eg 14:00 5, 2pm of Saturday. Sunday=1,Saturday=7</li>
                                               <li>Daily = HH:mm (eg 14:00 for everyday 2pm)</li>">
                                            Job Setup
                                        </a></th>
                                    <th width="10%" class="align-middle">Job Enabled?</th>
                                    <th width="10%" class="align-middle">Trigger Once when Tomcat started</th>
                                    <th width="10%" class="align-middle">Trigger Once After update</th>
                                    <th width="10%" class="align-middle">Next Trigger</th>
                                    <th width="15%" class="align-middle">Last Trigger</th>
                                </tr>
                            </thead>
                            <tbody>
                        <s:iterator value="timerJobMap" var="timerJob" status="timerJobStatus">
                            <tr>
                                <td class="align-middle">
                                    <s:property value="#timerJob.jobCode"/>
                                    <s:hidden name="timerJobList[%{#timerJobStatus.index}].jobCode" value='%{#timerJob.jobCode}'/>
                                </td>
                                <td class="align-middle"><s:if test='%{#timerJob.jobRepeat.equals("F")}'>
                                        <s:text name="schedular.jobType.F"/>
                                    </s:if><s:else>
                                        <s:select name="timerJobList[%{#timerJobStatus.index}].jobRepeat" value='%{#timerJob.jobRepeat}' list="jobTypeList" listKey="keyData" listValue="valueData" cssClass="form-control form-control-sm sds-dropdown" /></td>
                                    </s:else>
                                <td class="align-middle">
                                    <s:textfield name="timerJobList[%{#timerJobStatus.index}].jobRunEvery" value='%{#timerJob.jobRunEvery}' cssClass="form-control form-control-sm" />
                                </td>
                                <td class="align-middle">
                                    <div class="form-check fs-0 mb-0">
                                        <input type="checkbox" name="timerJobList[${timerJobStatus.index}].jobSwitch_on" value='true' class="form-check-input" id="jobSwitch_on${timerJobStatus.index}" <s:if test="%{#timerJob.jobSwitch_on}">checked</s:if>/>
                                        <label for="jobSwitch_on${timerJobStatus.index}" class="form-check-label"></label>
                                    </div>
                                </td>
                                <td class="align-middle">
                                    <div class="form-check fs-0 mb-0">
                                        <input type="checkbox" name="timerJobList[${timerJobStatus.index}].triggerAfterRestart" value='true' class="form-check-input" id="triggerAfterRestart${timerJobStatus.index}" <s:if test="%{#timerJob.triggerAfterRestart}">checked</s:if>/>
                                        <label for="triggerAfterRestart${timerJobStatus.index}" class="form-check-label"></label>
                                    </div>
                                </td>
                                <td class="align-middle">
                                    <div class="form-check fs-0 mb-0">
                                        <input type="checkbox" name="timerJobList[${timerJobStatus.index}].alwaysTriggerOnce" value='true' class="form-check-input" id="alwaysTriggerOnce${timerJobStatus.index}" <s:if test="%{#timerJob.alwaysTriggerOnce}">checked</s:if>/>
                                        <label for="alwaysTriggerOnce${timerJobStatus.index}" class="form-check-label"></label>
                                    </div>
                                </td>
                                <td class="align-middle">
                                    <s:property value="#timerJob.nextTrigger"/>
                                </td>
                                <td class="align-middle">
                                    <s:if test="#timerJob.lastTriggeredList.size() > 1">
                                        <a type="button" class="btn btn-sm btn-falcon-default" data-bs-toggle="tooltip" data-bs-html="true" 
                                               title="<s:iterator value="#timerJob.lastTriggeredList" var="item"><li><s:property value="#item"/></li></s:iterator>">
                                            <s:property value="#timerJob.lastTriggered"/>
                                        </a>
                                    </s:if>
                                    <s:elseif test='!#timerJob.lastTriggered.empty()'>
                                        <label type="button" class="btn btn-sm btn-falcon-default"><s:property value="#timerJob.lastTriggered"/></label>
                                    </s:elseif>
                                </td>
                            </tr>
                        </s:iterator>
                            </tbody>
                        </table>
                    </div>
                    <div class="row">
                        <div class="col text-center">
                            <button type="button" class="btn btn-sm btn-primary" name="" id="actionName" onclick="return updateChanges('sampleFormId');">
                                <i class="fa fa-save"></i> <span class="ms-1"><s:text name="schedular.update"/></span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <div id="submitFormDiv" class="hidden"></div>
    </body>
</html>