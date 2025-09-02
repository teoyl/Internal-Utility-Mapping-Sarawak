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
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Schedular Maintenance (Main switch is <s:property value="mainSwitchValue"/>)</h4>
                </div>
                <div class="panel-body">
                    <table width="100%" class="table table-sds table-condensed table-striped table-hover">
                        <thead>
                            <tr>
                                <th width="15%">Job Description</th>
                                <th width="15%">Job Type</th>
                                <th width="15%"><a type="button" class="btn btn-secondary" data-toggle="tooltip" data-placement="bottom" data-html="true" 
                                           title="<li>Yearly = MM-dd HH:mm (eg 01-03 14:00 for 3rd Jan 2pm)</li>
                                           <li>Monthly = dd HH:mm (eg 03 14:00 for 3rd every month 2pm)</li>
                                           <li>Day of Week = HH:mm day (eg 14:00 5, 2pm of Saturday. Sunday=1,Saturday=7</li>
                                           <li>Daily = HH:mm (eg 14:00 for everyday 2pm)</li>">
                                        Job Setup
                                    </a></th>
                                <th width="10%">Job Enabled?</th>
                                <th width="10%">Trigger Once when Tomcat started</th>
                                <th width="10%">Trigger Once After update</th>
                                <th width="10%">Next Trigger</th>
                                <th width="15%">Last Trigger</th>
                            </tr>
                        </thead>
                        <tbody>
                    <s:iterator value="timerJobMap" var="timerJob" status="timerJobStatus">
                        <tr>
                            <td>
                                <s:property value="#timerJob.jobCode"/>
                                <s:hidden name="timerJobList[%{#timerJobStatus.index}].jobCode" value='%{#timerJob.jobCode}'/>
                            </td>
                            <td><s:if test='%{#timerJob.jobRepeat.equals("F")}'>
                                    <s:text name="schedular.jobType.F"/>
                                </s:if><s:else>
                                    <s:select name="timerJobList[%{#timerJobStatus.index}].jobRepeat" value='%{#timerJob.jobRepeat}' list="jobTypeList" listKey="keyData" listValue="valueData" cssClass="form-control sds-dropdown" /></td>
                                </s:else>
                            <td>
                                <s:textfield name="timerJobList[%{#timerJobStatus.index}].jobRunEvery" value='%{#timerJob.jobRunEvery}' cssClass="form-control" />
                            </td>
                            <td>
                                <div class="col-md-5 checkbox right check-success">
                                    <input type="checkbox" name="timerJobList[${timerJobStatus.index}].jobSwitch_on" value='true' class="form-control" id="jobSwitch_on${timerJobStatus.index}" <s:if test="%{#timerJob.jobSwitch_on}">checked</s:if>/>
                                    <label for="jobSwitch_on${timerJobStatus.index}"></label>
                                </div>
                            </td>
                            <td>
                                <div class="col-md-5 checkbox right check-success">
                                    <input type="checkbox" name="timerJobList[${timerJobStatus.index}].triggerAfterRestart" value='true' class="form-control" id="triggerAfterRestart${timerJobStatus.index}" <s:if test="%{#timerJob.triggerAfterRestart}">checked</s:if>/>
                                    <label for="triggerAfterRestart${timerJobStatus.index}"></label>
                                </div>
                            </td>
                            <td>
                                <div class="col-md-5 checkbox right check-success">
                                    <input type="checkbox" name="timerJobList[${timerJobStatus.index}].alwaysTriggerOnce" value='true' class="form-control" id="alwaysTriggerOnce${timerJobStatus.index}" <s:if test="%{#timerJob.alwaysTriggerOnce}">checked</s:if>/>
                                    <label for="alwaysTriggerOnce${timerJobStatus.index}"></label>
                                </div>
                            </td>
                            <td>
                                <s:property value="#timerJob.nextTrigger"/>
                            </td>
                            <td>
                                <s:if test="#timerJob.lastTriggeredList.size() > 1">
                                    <a type="button" class="btn btn-secondary" data-toggle="tooltip" data-html="true" 
                                           title="<s:iterator value="#timerJob.lastTriggeredList" var="item"><li><s:property value="#item"/></li></s:iterator>">
                                        <s:property value="#timerJob.lastTriggered"/>
                                    </a>
                                </s:if><s:else>
                                    <label type="button" class="btn btn-secondary"><s:property value="#timerJob.lastTriggered"/></label>
                                </s:else>
                            </td>
                        </tr>
                    </s:iterator>
                        </tbody>
                    </table>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label"></label>
                        <div class="col-md-5">
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="return updateChanges('sampleFormId');">
                                <i class="fa fa-save"></i><s:text name="schedular.update"/>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
                            <div id="submitFormDiv" class="hidden"></div>
    </body>
</html>