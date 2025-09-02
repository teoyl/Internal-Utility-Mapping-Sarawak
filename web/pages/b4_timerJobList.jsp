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
        </script>
    </head>
    <body>
        <form name="schedularForm" id="schedularFormId" method="post" action="processUpdateSysSchedular">
            <div class="kt-portlet">
                <div class="kt-portlet__head">
                    <div class="kt-portlet__head-label">
                        <h3 class="kt-portlet__head-title">
                            <s:text name="schecularMaintenance.appName"/>
                        </h3>
                    </div>
                </div>
                <div class="kt-portlet__body">
                    <table width="100%" class="table table-sds table-condensed table-striped table-hover">
                        <thead>
                            <tr>
                                <th width="20%">Job Description</th>
                                <th width="20%">Job Type</th>
                                <th width="20%">Job Setup</th>
                                <th width="10%">Job Enabled?</th>
                                <th width="10%">Trigger Once when Tomcat started</th>
                                <th width="10%">Trigger Once After update</th>
                                <th width="10%">Next Trigger</th>
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
                                <label class="kt-checkbox kt-checkbox--brand" style="padding-top:20px">
                                    <input type="checkbox" name="timerJobList[${timerJobStatus.index}].jobSwitch_on" value='true' class="form-control" id="jobSwitch_on${timerJobStatus.index}" <s:if test="%{#timerJob.jobSwitch_on}">checked</s:if>/>
                                    <span></span>
                                </label>
                            </td>
                            <td>
                                <label class="kt-checkbox kt-checkbox--brand" style="padding-top:20px">
                                    <input type="checkbox" name="timerJobList[${timerJobStatus.index}].triggerAfterRestart" value='true' class="form-control" id="triggerAfterRestart${timerJobStatus.index}" <s:if test="%{#timerJob.triggerAfterRestart}">checked</s:if>/>
                                    <span></span>
                                </label>
<%--                                <div class="col-md-5 checkbox right check-success">
                                    <input type="checkbox" name="timerJobList[${timerJobStatus.index}].triggerAfterRestart" value='true' class="form-control" id="triggerAfterRestart${timerJobStatus.index}" <s:if test="%{#timerJob.triggerAfterRestart}">checked</s:if>/>
                                    <label for="triggerAfterRestart${timerJobStatus.index}"></label>
                                </div>--%>
                            </td>
                            <td>
                                <label class="kt-checkbox kt-checkbox--brand" style="padding-top:20px">
                                    <input type="checkbox" name="timerJobList[${timerJobStatus.index}].alwaysTriggerOnce" value='true' class="form-control" id="alwaysTriggerOnce${timerJobStatus.index}" <s:if test="%{#timerJob.alwaysTriggerOnce}">checked</s:if>/>
                                    <span></span>
                                </label>
                            </td>
                            <td>
                                <s:property value="#timerJob.nextTrigger"/>
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