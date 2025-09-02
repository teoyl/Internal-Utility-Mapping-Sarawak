<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title><s:property value="pageTitle_"/>
        </title>        
        <s:head />

        <script src="pages/scripts/amcharts_2.js" type="text/javascript"></script>
        <script src="pages/scripts/serial.js" type="text/javascript"></script>
        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />

        <script language="javascript">
            $(document).ready(function () {
                
            });
            var chart;
            var chartData = [
            ${jobStatistic}

            ];
            AmCharts.ready(function () {
            // SERIALL CHART
            chart = new AmCharts.AmSerialChart();
            chart.dataProvider = chartData;
            chart.categoryField = "Officer";
            chart.plotAreaBorderAlpha = 0.2;
            chart.rotate = true;
            // AXES
            // Category
            var categoryAxis = chart.categoryAxis;
            categoryAxis.gridAlpha = 0.1;
            categoryAxis.axisAlpha = 0;
            categoryAxis.gridPosition = "start";
            // value
            var valueAxis = new AmCharts.ValueAxis();
            valueAxis.stackType = "regular";
            valueAxis.gridAlpha = 0.1;
            valueAxis.axisAlpha = 0;
            valueAxis.precision = 0; // set interval precision                                
            chart.addValueAxis(valueAxis);
            // GRAPHS
            // firstgraph
            var graph = new AmCharts.AmGraph();
            graph.title = "Completed";
            graph.labelText = "[[value]]";
            graph.valueField = "Completed";
            graph.type = "column";
            graph.lineAlpha = 0;
            graph.fillAlphas = 1;
            graph.lineColor = "#23db5f";
            graph.balloonText = "<b><span style='color:#bbff99'>[[title]]</b></span><br><span style='font-size:14px'>[[category]]: <b>[[value]]</b></span>";
            chart.addGraph(graph);
            // second graph
            graph = new AmCharts.AmGraph();
            graph.title = "In Progress";
            graph.labelText = "[[value]]";
            graph.valueField = "In Progress";
            graph.type = "column";
            graph.lineAlpha = 0;
            graph.fillAlphas = 1;
            graph.lineColor = "#2397ef";
            graph.balloonText = "<b><span style='color:#2a8000'>[[title]]</b></span><br><span style='font-size:14px'>[[category]]: <b>[[value]]</b></span>";
            chart.addGraph(graph);
            // third graph
            graph = new AmCharts.AmGraph();
            graph.title = "Outstanding";
            graph.labelText = "[[value]]";
            graph.valueField = "Outstanding";
            graph.type = "column";
            graph.lineAlpha = 0;
            graph.fillAlphas = 1;
            graph.lineColor = "#e65c00";
            graph.balloonText = "<b><span style='color:#e65c00'>[[title]]</b></span><br><span style='font-size:14px'>[[category]]: <b>[[value]]</b></span>";
            chart.addGraph(graph);
            // LEGEND
            var legend = new AmCharts.AmLegend();
            legend.position = "top";
            legend.borderAlpha = 0.3;
            legend.horizontalGap = 10;
            legend.switchType = "v";
            chart.addLegend(legend);
            chart.creditsPosition = "top-right";
            // WRITE
            chart.write("chartdiv");
            setDepth();
            });
            // Make chart 2D/3D
            function setDepth() {
            if (document.getElementById("rb1").checked) {
            chart.depth3D = 0;
            chart.angle = 0;
            } else {
            chart.depth3D = 20;
            chart.angle = 30;
            }
            chart.validateNow();
            }

            function doAssignment(completeValueIdx) {
            var answer = confirm("The selected job(s) will be assigned to the officer. Do you want to proceed?");

            if(answer == true){
            var assignmentStr = $("#assignNextWorkerCompleteValue_" + completeValueIdx).val() + ";";
            $.each($(".activity_id_" + completeValueIdx), function(completeTaskIndex, affordDiv) {
            if (completeTaskIndex > 0) {
            assignmentStr += ",";
            }
            assignmentStr += $("#activity_id_" + completeValueIdx + "_" + completeTaskIndex).val() + "::" + $("#assignee_" + completeValueIdx + "_" + completeTaskIndex).val() + "::" + $("#activity_code_" + completeValueIdx + "_" + completeTaskIndex).val() + "::" + $("#wfJobRemark_" + completeValueIdx + "_" + completeTaskIndex).val() ;
            <%--<s:if test="isRA_job">
                assignmentStr += $("#activity_id_" + completeValueIdx + "_" + completeTaskIndex).val() + "::" + $("#assignee_" + completeValueIdx + "_" + completeTaskIndex).val() + "::" + $("#activity_code_" + completeValueIdx + "_" + completeTaskIndex).val() + "::" + $("#wfJobRemark_" + completeValueIdx + "_" + completeTaskIndex).val() ;
            </s:if><s:else>
                assignmentStr += $("#activity_id_" + completeValueIdx + "_" + completeTaskIndex).val() + "::" + $("#assignee_" + completeValueIdx + "_" + completeTaskIndex).val() + "::" + $("#activity_code_" + completeValueIdx + "_" + completeTaskIndex).val();
            </s:else>--%>
            <%--$(".phase"+thePhase+"shortageBalance"+affordableID+"_"+shortageIndex).val($(".phase"+thePhase+"shortageUnit"+affordableID+"_"+shortageIndex).val());
            $(".lblphase"+thePhase+"shortageBalance"+affordableID+"_"+shortageIndex).text($(".phase"+thePhase+"shortageUnit"+affordableID+"_"+shortageIndex).val());--%>
            });
            $("#assignmentSelected").val(assignmentStr);
            return true;
            }else{
             return false;
            }
            }
            function cancelInformed(pPlId) {
            document.getElementById("selectJobId_").value = pPlId;
            document.getElementById("insertJobForm").action = "jobCancelInformedJobMain#";
            document.insertJobForm.submit();
            }
        </script>

        <script type="text/javascript" src="include/simple_tab/simple_tab.js"></script>
        <link rel="stylesheet" href="include/simple_tab/simple_tab.css" />
    </head>
    <body>
        <!--<div class="col-md-12 col-sm-12 col-xs-12" style=" background-color: #ffffff;">-->

        
        <form id="jobForm" name="jobForm" action="doAssignRouteJobMain" method="POST">
            <s:hidden theme="simple" name="assignmentSelected" id="assignmentSelected" value=""/>
            <s:hidden theme="simple" name="task_id__" value='%{currentTaskID_}'/>
            <s:hidden theme="simple" name="action" value="JobMainRoute"/>
            <s:hidden theme="simple" name="queryId_" cssClass="queryId_" value="%{queryId_}"/>
            <s:hidden theme="simple" name="strAppId" cssClass="strAppId" value="%{strAppId}"/>

                <div class="row form-row-margin">
                    <div class="col-md-12 text-right">
                        <button class="btn btn-default block-xs buttonBackToList" type="submit" name="action:loadEditPageRouteJobMain" id="loadEditPageRouteJobMain"><i class="fa fa-arrow-left"></i>Back</button>
                    </div>
                </div>
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="x_panel">
                        <div class="x_title">
                            <h3> 
                                <span class="titleText"><s:property value="pageTitle_"/></span>
                            </h3>
                        </div>
                    
                        <div class="x_content">
                            <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
                            <div class=" form-group form-group-default">
                                <label class="control-label col-md-3 col-sm-3 col-xs-12">Current Task</label>
                                <div class="col-md-9 col-sm-9 col-xs-12">${currentTaskDesc_}</div>
                            </div>
                            
                                <s:iterator value="nextCompleteTaskList" var="nextCompleteTask" status="nextCompleteTaskStatus">
                                    <s:hidden theme="simple" cssClass="completeValue_%{#nextCompleteTaskStatus.index}" id="assignNextWorkerCompleteValue_%{#nextCompleteTaskStatus.index}" name="assignNextWorkerCompleteValue_%{#nextCompleteTaskStatus.index}" value='%{#nextCompleteTask.get("nc")}'/>
                                    <s:if test='nextCompleteTaskList.size() == 1 && #nextCompleteTask.get("nextTaskList").size() == 1'>
                                        <s:iterator value='#nextCompleteTask.get("nextTaskList")' var="nextTask" status="nextTaskStatus">
                                            <div class=" form-group form-group-default">
                                                <label class="control-label col-md-3 col-sm-3 col-xs-12"><s:text name="TaskMgmt.manage.nextTask"/> </label>
                                                <div class="col-md-9 col-sm-6 col-xs-12"><s:property value='%{#nextTask.get("task_description")}'/></div>
                                            </div>
                                            <div class ="form-group form-group-default">
                                                <label class="control-label col-md-3 col-sm-3 col-xs-12">Assign To</label>
                                                <div class ="col-md-9">
                                                    <s:hidden theme="simple" cssClass="activity_id_%{#nextCompleteTaskStatus.index}" id="activity_id_%{#nextCompleteTaskStatus.index}_%{#nextTaskStatus.index}" name="activity_id__" value='%{#nextTask.get("activity_id")}'/>
                                                    <s:hidden theme="simple" cssClass="activity_code_%{#nextCompleteTaskStatus.index}" id="activity_code_%{#nextCompleteTaskStatus.index}_%{#nextTaskStatus.index}" name="activity_code__" value='%{#nextTask.get("activity_code")}'/>
                                                    <div class="form-group form-group-default form-group-default-select2">
                                                        
                                                        <%--<s:select cssClass="assignee_%{#nextCompleteTaskStatus.index}" id="assignee_%{#nextCompleteTaskStatus.index}_%{#nextTaskStatus.index}" list='%{#nextTask.get("assigneeList")}' listKey="userId" listValue="userName" name="assignTo__" theme="simple" value='%{#nextTask.get("assigneeList_value")}'/>--%>
                                                        <s:select id="assignee_%{#nextCompleteTaskStatus.index}_%{#nextTaskStatus.index}" list='%{#nextTask.get("assigneeList")}' cssClass="assignee_%{#nextCompleteTaskStatus.index} full-width" data-init-plugin="select2" listKey="userId" listValue="userName" name="assignTo__"/>                                            
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group form-group-default">
                                                <label class="control-label col-md-3 col-sm-3 col-xs-12">Job Remark</label>
                                                <div class="col-md-9 col-sm-6 col-xs-12"><input class="form-control" id="wfJobRemark_${nextCompleteTaskStatus.index}_${nextTaskStatus.index}" name="wfJobRemark_${nextCompleteTaskStatus.index}_${nextTaskStatus.index}" type="text" value="${searchProposal_title}" maxlength="250"></div>
                                            </div>
                                            <div class ="form-group form-group-default">
                                                <label class="control-label col-md-3 col-sm-3 col-xs-12"></label>
                                                <div class ="col-md-9 col-sm-6 col-xs-12">
                                                        <%--<s:submit type="button" class="btn btn-primary" action="doAssignRouteJobMain" onclick="return doAssignment(%{#nextCompleteTaskStatus.index});" value='%{#nextCompleteTask.get("ncDesc")}'/>--%>
                                                        <button class="btn btn-primary" type="submit" name="action:doAssignRouteJobMain" id="doAssignRouteJobMain"  onclick="return doAssignment(${nextCompleteTaskStatus.index});" value='${nextCompleteTask.get("ncDesc")}'><i class="fa fa-user"></i>Assign</button>
                                                        <!--<button class="btn btn-primary" type="submit" name="doAssignRouteJobMain" id="doAssignRouteJobMain"  onclick="return doAssignment(${nextCompleteTaskStatus.index});" value='${nextCompleteTask.get("ncDesc")}'><i class="fa fa-user"></i>Assign</button>-->
                                                </div>
                                            </div>
                                            <!--                                    </td>
                                                                            </tr>-->
                                        </s:iterator>
                                        <!--</table>-->
                                    </s:if><s:else>
                                        <table>
                                            <tr>
                                                <td ><s:text name="TaskMgmt.manage.nextTask"/></td>
                                                <td width="2%">:</td>
                                                <td>
                                                    <s:submit type="button" theme="simple" action="doAssignRouteJobMain" onclick="return doAssignment(%{#nextCompleteTaskStatus.index});" value='%{#nextCompleteTask.get("ncDesc")}'/>
                                                </td>
                                            </tr>
                                            <s:iterator value='#nextCompleteTask.get("nextTaskList")' var="nextTask" status="nextTaskStatus">
                                                <tr>
                                                    <td >
                                                        <s:property value='%{#nextTask.get("task_description")}'/>&nbsp;&nbsp;
                                                        <s:hidden theme="simple" cssClass="activity_id_%{#nextCompleteTaskStatus.index}" id="activity_id_%{#nextCompleteTaskStatus.index}_%{#nextTaskStatus.index}" name="activity_id__" value='%{#nextTask.get("activity_id")}'/>
                                                        <s:hidden theme="simple" cssClass="activity_code_%{#nextCompleteTaskStatus.index}" id="activity_code_%{#nextCompleteTaskStatus.index}_%{#nextTaskStatus.index}" name="activity_code__" value='%{#nextTask.get("activity_code")}'/>
                                                    </td>
                                                    <td width="2%">:</td>
                                                    <td>
                                                        <s:text name="TaskMgmt.manage.assignTo"/>&nbsp;<s:select cssClass="assignee_%{#nextCompleteTaskStatus.index}" id="assignee_%{#nextCompleteTaskStatus.index}_%{#nextTaskStatus.index}" list='%{#nextTask.get("assigneeList")}' listKey="userId" listValue="userName" name="assignTo__" theme="simple"/>
                                                    </td>
                                                </tr>
                                            </s:iterator>
                                        </table>
                                    </s:else>
                                </s:iterator>
                        </div>    
                    </div>        
                </div>
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="x_panel">
                        <div class="x_title">
                            <h3> 
                                <span class="titleText">${groupDescTitle}</span>
                            </h3>
                        </div>
                    
                        <div class="x_content">
                            <div id="chartdiv" style="width: 100%; height: <s:property value="graphHeight"/>px;"></div>
                            <div style="margin-left:40px;">
                                <input type="radio" name="group" id="rb1" onclick="setDepth()">2D
                                <input type="radio" checked="true" name="group" id="rb2" onclick="setDepth()">3D
                            </div>
                        </div>    
                    </div> 
                </div>   
                            
<!--            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"> 
                        <span class="titleText">
                            <%--<s:text name="chart.workload.title" />--%>
                            
                        </span>                        
                    </h3>
                </div>
                <div class="panel-body">
                    
                </div>
            </div>-->
        </form>
       <!--</div>-->             
    </body>
</html>