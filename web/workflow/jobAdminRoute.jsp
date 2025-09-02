<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Process Job</title>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <script type="text/javascript" src="pages/scripts/controls.js"></script>
        <link href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" rel="stylesheet"></link>
        <link href="include/dhtmlwindow/modalfiles/modal.css" rel="stylesheet"></link>
        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
        <script src="pages/scripts/amcharts_2.js" type="text/javascript"></script>
        <script src="pages/scripts/serial.js" type="text/javascript"></script>
        <!--<link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" type="text/css" />
        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" type="text/css" />
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>-->
        <style type="text/css">
            a.tooltip_nobs {outline:none; }
            a.tooltip_nobs strong {line-height:30px;}
            a.tooltip_nobs:hover {text-decoration:none;} 
            a.tooltip_nobs span {
                z-index:10;display:none; padding:14px 20px;
                margin-top:40px; margin-left:0px;
                width:300px; line-height:16px;
            }
            a.tooltip_nobs:hover span{
                display:inline; position:absolute; color:#111;
                border:1px solid #DCA; background:#fffAF0;}
            .callout {z-index:20;position:absolute;top:30px;border:0;left:-12px;}

            a.tooltip_nobs span
            {
                border-radius:4px;
                box-shadow: 5px 5px 8px #CCC;
                /*opacity: 0.8;*/
            }
        </style>
        <SCRIPT language="javascript">
            $(document).ready(function() {                
//                $('#graphInfo').modal('hide');
                  $("#assignTo__").select2();
            });
            
            function setData(p1, p2, p3) {
                document.getElementById("pPlId").value = p1;

                if (p3 == 'NEW') {
                    document.getElementById("assignTo").value = document.getElementById("assignTo_new").value;
                    submitForm(p3)
                } else if (p3 == 'REASSIGN') {
                    document.getElementById("assignTo").value = document.getElementById("assignTo_reassign").value;
                    submitForm(p3)
                } else {
                    document.getElementById("pJpId").value = p2;
                }
            }

            
        //function openAssignTo(p1, p2) {
        //    document.getElementById("pJpId").value = p2;
        //
        //    divwin=dhtmlwindow.open('divbox', 'div', p1, 'Serah Semula', 'width=400px,height=100px,resize=1,scrolling=1,center=1"',"");
        //    return false;
        //       
        //}

            
            function showWorkload() {
                $('#graphInfo').modal('show').on('shown.bs.modal', function () {
                LoadChart();
              });
                $('#graphInfo').modal('show');
            }
            
            function graphClose(){
                $('#graphInfo').modal('hide');
            }
            function LoadChart(){
                var chart;
                var chartData = [                    
                    ${jobStatistic}

                ];
//                AmCharts.ready(function () {
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
                    chart.depth3D = 20;
                    chart.angle = 30;
                    // WRITE
                    chart.write("chartdiv");                
//                });
            }
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
            
            function openAssignTo(p1, p2) {
                document.getElementById("pJpId").value = p2;
                winPodH = dhtmlmodal.open("divbox", "div", p1, "Reassign", "width=400px,height=100px,resize=1,scrolling=0,center=1", "");
                winPodH.onclose = function() {
                    return false;
                }
            }

            function submitForm(p1) {
                document.getElementById("pType").value = p1;
                document.jobForm.submit();
            }
        </SCRIPT>
        <style>           
            .form-group span {
                float: none;
                display: block;
            }
            .form-group-default.form-group-default-select2 > label {
                z-index: 2;
            }
            select.full-width + .select2-container {
                width: 100% !important;
            }           
        </style>            
    </head>
    <body>
        <form id="jobForm" name="jobForm" action="processUpdateRouteJobAdmin" method="POST">
            <s:hidden theme="simple" name="action" value="JobAdminRoute"/>
            <s:hidden name="pl_id" theme="simple" value="%{model.pl_id}" />
            
            <div class="panel panel-default ">  
                <div class="panel-heading">
                    <h3 class="panel-title"> 
                        <div class="row">
                            <!--<div class="col-md-2 col-lg-2"><s:text name="TaskMgmt.manage.eCaseRef"/></div>-->
                            <div class="col-md-2 col-lg-2"><s:text name="sta.staNo"/></div>
                            <div class="col-md-10 col-lg-10 text-bold"><s:property value='%{jobAdminMap.get("case_ref")}'/></div><br>
                        </div>
                       <%-- <span class="titleText"><s:text name="jobadmin" /></span>
                        <s:submit type="submit" cssClass="defaultButton buttonBackToList" theme="simple" action="cancelRouteJobAdmin" value='%{getText("button.back")}' cssStyle=" float: right"/>--%>
                    </h3>
                </div>
                <div class="panel-body">
                    <jsp:include page="/pages/base/actionError.jsp"></jsp:include>

                        <div class="row">
                            <div class="col-md-3">
                                <div class="form-group form-group-default viewText">
                                    <label><s:text name="jobadmin.systemname" /></label>
                                    <s:property value='%{jobAdminMap.get("wf_subsystem")}'/>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group form-group-default viewText">
                                    <label><s:text name="jobadmin.poolstatus" /></label>
                                    <s:text name='TaskMgmt.status.%{jobAdminMap.get("task_status")}' />
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group form-group-default viewText">
                                    <label><s:text name="jobadmin.poolassigndate" /></label>
                                    <s:text name="date_default_datetime"><s:param value='%{jobAdminMap.get("created_date")}' /></s:text>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group form-group-default viewText">
                                    <label>Assigned To</label>
                                    <s:if test='jobAdminMap.get("task_status").equals("20")'>
                                        <s:if test='jobAdminMap.get("taskDoer") == null'>
                                            -
                                        </s:if>
                                        <s:else>
                                            <s:property value='%{jobAdminMap.get("taskDoer_name")}'/>
                                        </s:else>
                                    </s:if>
                                    <s:elseif test='jobAdminMap.get("task_status").equals("40")'>
                                        <s:property value='%{jobAdminMap.get("taskDoer")}'/>
                                    </s:elseif>
                                    <s:else></s:else>
                                </div>
                            </div>
                        </div>      
                        <div class="row">
                            <div class="col-md-3">
                                <div class="form-group form-group-default viewText">
                                    <label><s:text name="jobadmin.caseDesc" /></label>
                                    <s:property value='%{jobAdminMap.get("case_desc")}'/>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group form-group-default viewText">
                                    <label><s:text name="common.division" /></label>
                                    <s:property value='%{jobAdminMap.get("case_div")}'/>
                                </div>
                            </div>
                            <div class="col-md-6">
<%--                                <div class="form-group form-group-default viewText">
                                    <label><s:text name="common.land.district" /></label>
                                    <s:property value='%{jobAdminMap.get("case_dis")}'/>
                                </div>--%>
                            </div>    
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="form-group form-group-default viewText">
                                    <label>Job Description</label>
                                    <s:property value='%{jobAdminMap.get("wf_name") + " : " + jobAdminMap.get("task_description")}'/>
                                </div>
                            </div>
                        </div>

                        <br><br><h3 class="title-v3"><s:text name="TaskMgmt.manage.taskAssignment"/></h3>
                        <div class="row">
                            <s:if test='jobAdminMap.get("task_status").equals("20")'>
                                <div class="col-md-3">
                                    <s:if test='assigneeList.size() > 0'>
                                        <div class ="row">
                                            <div class ="col-md-10">
                                                <div class="form-group form-group-default form-group-default-select2">
                                                    <label><s:text name="TaskMgmt.manage.reassignTo"/></label>
                                                    <s:select list="assigneeList" cssClass="form-control full-width" data-init-plugin="select2" listKey="userId" listValue="userName" name="assignTo__" theme="simple"/>                                            
                                                </div>
                                            </div>
                                            <div class ="col-md-2">
                                                <div>
                                                    <!--<button class="btn btn-primary" type="submit" name="action:assignRouteJobAdmin" id="viewWorkload"><i class="fa fa-area-chart"></i><s:text name="btn.view.workload"/></button>-->
                                                    <button class="btn btn-primary" type="button" name="viewWorkload" id="viewWorkload" onclick="showWorkload();"><i class="fa fa-area-chart"></i><s:text name="btn.view.workload"/></button>
                                                </div>
                                            </div>
                                        </div>
                                    </s:if>
                                    <s:else>
                                        <div class="form-group form-group-default viewText">
                                            <label><s:text name="TaskMgmt.manage.reassignTo"/></label>
                                            No other user in this group.
                                        </div>
                                    </s:else>
                                </div>
                            </s:if>
                            <s:elseif test='jobAdminMap.get("task_status").equals("10")'>
                                <div class="col-md-3">
                                    <div class="form-group form-group-default form-group-default-select2">
                                        <label><s:text name="TaskMgmt.manage.assignTo"/></label>
                                        <s:select list="assigneeList" cssClass="form-control full-width" data-init-plugin="select2" listKey="userId" listValue="userName" name="assignTo__" theme="simple"/>
                                    </div>
                                </div>
                            </s:elseif> 
                        </div>


                        <br><br><h3 class="title-v3">Task Flows</h3>
                        <div aria-labelledby="dLabel" style="background-color:white;width:100%;">
                            <div style="margin-left:10px;margin-right:10px;">
                                <ul class="timeline">
                                    <s:iterator value='jobAdminMap.get("progressList")' var="progress" status="progressStatus">
                                         <li>
                                            <div class="timeline-badge timeline-badge-default"></div>
                                            <div class="timeline-panel">
                                                <div class="timeline-heading">
                                                    <h4 class="timeline-title"><a href="#" class="tooltip_nobs"><s:property escapeHtml="false" value="%{#progress}"/></a></h4>
                                                </div>
                                                <div class="timeline-body">
                                                    <p></p>
                                                </div>
                                            </div>
                                        </li>
                                    </s:iterator>
                                </ul>
                            </div>
                        </div><!--end dropdown-menu-->

    <%--                    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
                            <tr><td>&nbsp;</td></tr>
                            <tr>
                                <td width="30%" align="right">
                                    <s:submit type="submit" cssClass="defaultButton buttonBackToList" theme="simple" action="cancelRouteJobAdmin" value='%{getText("button.back")}'/>
                                </td>

                            </tr>
                        </table>>
                        <table border="0" width="100%" cellpadding="2px" class="table borderless form" >
                            <tr>
                                <td width="20px">&nbsp;</td>
                                <td width="170px"><label class="control-label"><s:text name="jobadmin.systemname" /></label></td>
                                <td width="10px">:</td>
                                <td><s:property value='%{jobAdminMap.get("wf_subsystem")}'/></td>
                            </tr>
                            <tr valign="top">
                                <td>&nbsp;</td>
                                <td><label class="control-label"><s:text name="TaskMgmt.manage.eCaseRef"/></label></td>
                                <td>:</td>
                                <td style="word-wrap: break-word"><s:property value='%{jobAdminMap.get("case_ref")}'/></td>
                            </tr>
                            <tr valign="top">
                                <td>&nbsp;</td>
                                <td><label class="control-label">Case Description</label></td>
                                <td>:</td>
                                <td style="word-wrap: break-word"><s:property value='%{jobAdminMap.get("case_desc")}'/></td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td><label class="control-label">Job Description</label></td>
                                <td>:</td>
                                <td><s:property value='%{jobAdminMap.get("wf_name") + " : " + jobAdminMap.get("task_description")}'/></td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td><label class="control-label"><s:text name="jobadmin.poolassigndate" /></label></td>
                                <td>:</td>
                                <td><s:text name="date_default_datetime"><s:param value='%{jobAdminMap.get("created_date")}' /></s:text></td>
                            </tr>

                            <!--comment by sereneChye @ 2/2/2013--> 
                            <tr>
                                <td>&nbsp;</td>
                                <td><label class="control-label"><s:text name="jobadmin.poolstatus" /></label></td>
                                <td>:</td>
                                <td><s:text name='TaskMgmt.status.%{jobAdminMap.get("task_status")}' /></td>
                            </tr>
                            <s:if test='jobAdminMap.get("task_status").equals("20")'>
                                <tr>
                                    <td>&nbsp;</td>
                                    <td><label class="control-label">Assigned To</label></td>
                                    <td>:</td>
                                    <td><s:property value='%{jobAdminMap.get("taskDoer")}'/></td>
                                </tr>
                            </s:if>
                            <tr><td colspan="3"/><td>
                                <s:if test='jobAdminMap.get("task_status").equals("20")'>
                                        <fieldset><!--<legend><s:radio onchange="taskActionChange(this.value)" id="taskActionOption" name="taskAction" list="reassignCompleteOption" listKey="keyData" listValue="valueData"/></legend>-->
                                            <legend><s:text name="TaskMgmt.manage.taskAssignment"/></legend>
                                            <table>
                                                <tr>
                                                    <td>
                                                        <div id="reassignID">
                                                            <table>
                                                                <tr>
                                                                    <s:if test='assigneeList.size() > 0'>
                                                                    <td><s:text name="TaskMgmt.manage.reassignTo"/></td>
                                                                    <td>:</td>
                                                                    <td>
                                                                        <!--<s:textfield name="model.reassignTo__" theme="simple"/>-->
                                                                        <s:select list="assigneeList" listKey="userId" listValue="userName" name="assignTo__" theme="simple"/>
                                                                        <s:submit type="submit" theme="simple" action="assignRouteJobAdmin" value='%{getText("TaskMgmt.manage.R")}'/>
                                                                    </td>
                                                                    </s:if><s:else>
                                                                        <td>No other user in this group!</td>
                                                                    </s:else>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                        <!--<div id="completeID">
                                                            <table>
                                                                <tr>
                                                                    <td><s:text name="TaskMgmt.completedValue"/></td>
                                                                    <td>:</td>
                                                                    <td>
                                                                        <s:textfield name="model.completeValue__" theme="simple" value="Y"/>
                                                                        <s:submit type="submit" theme="simple" action="assignRouteJobAdmin" value='%{getText("button.update")}'/>
                                                                    </td>
                                                                </tr>
                                                                <s:if test="!model.taskActivity.outputParamList.isEmpty">
                                                                    <tr>
                                                                        <td colspan="3">Output Parameter</td>
                                                                    </tr>
                                                                    <s:iterator value="model.taskActivity.outputParamList" id="outParam" status="outParamStatus">
                                                                    <tr>
                                                                        <td><s:property value="#outParam.param_description"/>-(<s:property value="#outParam.param_name"/>)
                                                                            <s:if test='#outParam.required.equals("R") || #outParam.required.equals("Y")'>
                                                                                <jsp:include page="/pages/base/requiredField.jsp"/>
                                                                            </s:if>
                                                                        </td>
                                                                        <td>:</td>
                                                                        <td><s:textfield name="__%{#outParam.param_name}" theme="simple" value="%{getOutputParamData(#outParam.param_name)}"/></td>
                                                                    </tr>
                                                                    </s:iterator>
                                                                </s:if>
                                                            </table>
                                                        </div>-->
                                                    </td>
                                                </tr>
                                            </table>
                                        </fieldset>
                                    </s:if><s:elseif test='jobAdminMap.get("task_status").equals("10")'>
                                        <fieldset> <legend><s:text name="TaskMgmt.manage.taskAssignment"/></legend>
                                            <table>
                                                <tr>
                                                    <td width="15%"><s:text name="TaskMgmt.manage.assignTo"/></td>
                                                    <td width="1%">:</td>
                                                    <td>
                                                        <!--<s:textfield name="model.assignTo__" theme="simple" value=""/>-->
                                                        <s:select list="assigneeList" listKey="userId" listValue="userName" name="assignTo__" theme="simple"/>
                                                        <s:submit type="submit" theme="simple" action="assignRouteJobAdmin" value='%{getText("TaskMgmt.manage.A")}'/>
                                                    </td>
                                                </tr>
                                            </table>
                                        </fieldset>
                                    </s:elseif>
                                </td></tr>
                        </table>

                        <br/>
                        <table border="0" width="100%" cellpadding="2px" class="table borderless form" >
                            <tr><td>
                                <fieldset> <legend>Task Flows</legend>
                                    <s:iterator value='jobAdminMap.get("progressList")' id="progress" status="progressStatus">
                                        <s:if test="#progressStatus.index > 0"> &rarr; </s:if> <a href="#" class="tooltip_nobs"><s:property escape="false" value="%{#progress}"/></a> 
                                    </s:iterator>
                                </fieldset>
                                </td>
                            </tr>
                        </table>--%>

                        <s:hidden name="id" value="%{id}" />
                        <s:hidden name="pPlId" value="" />
                        <s:hidden name="pJpId" value="" />
                        <s:hidden name="pType" value="" />
                        <s:hidden name="assignTo" value="" />


                        <div id="divAssignToNew" style="display:none;">
                            <p style="padding: 10px">
                                <s:if test='assigneeList.size() > 0'>
                                    <s:text name="jobadmin.assignto" />: <s:select id="assignTo_new" name="assignTo_new" theme="simple" list="assigneeList" listKey="userId" listValue="userName" value="" />
                                    <s:submit theme="simple" cssClass="defaultButton" action="" onclick="setData('%{model.pl_id}','','NEW')" value='%{getText("jobadmin.btn.assign")}' />
                                </s:if>
                                <s:else>
                                    <font class="errortxt"><s:text name="jobadmin.noAssignee" /></font>
                                    </s:else>
                            </p>
                        </div>

                        <div id="divAssignToReassign" style="display:none;">
                            <p style="padding: 10px;">
                                <s:if test='assigneeList.size() > 0'>
                                    <s:text name="jobadmin.assignto" />: <s:select id="assignTo_reassign" name="assignTo_reassign" theme="simple" list="assigneeList" listKey="userId" listValue="userName" value=""/>
                                    <span style="float:right; margin-top: -5px;"><s:submit theme="simple" cssClass="defaultButton" action="" onclick="setData('%{model.pl_id}','','REASSIGN')" value='%{getText("jobadmin.btn.assign")}' /></span>
                                </s:if>
                                <s:else>
                                    <font class="errortxt"><s:text name="jobadmin.noAssignee" /></font>
                                    </s:else>
                            </p>
                        </div>


                </div>
            </div>
            <div class="row">
                <div class="col-md-12 text-right">
                    <s:if test='jobAdminMap.get("task_status").equals("20") || jobAdminMap.get("task_status").equals("10")'>
                        <button class="btn btn-primary" type="submit" name="action:assignRouteJobAdmin" id="assignRouteJobAdmin"><i class="fa fa-user"></i><s:text name="TaskMgmt.manage.A"/></button>
                    </s:if>
                    <button class="btn btn-default" type="submit" name="action:cancelRouteJobAdmin" id="cancelRouteJobAdmin"><i class="fa fa-arrow-left"></i>Back</button>
                </div>
            </div><br><br>

        </form>
        <!--added by ed 06112018-->
<!--        <div class="graphInfo">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="panel-title">
                        Info Graph
                    </div>
                </div>
                <div class="panel-body">
                    display graph here
                    <button onclick="graphClose()">Close</button>
                </div>
            </div>
        </div>-->
        <div id="graphInfo" class="modal fade" tabindex="-1" data-width="60%"  style="display: none;"  data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h3 class="title-v2">View Workload</h3><br>
                    </div>
                    <div class="modal-body myModalContent">
                        <div id="chartdiv" style="width: 100%; height: 400px;"></div>
<!--                        <div style="margin-left:40px;">
                            <input type="radio" checked="true" name="group" id="rb1" onclick="setDepth()">2D
                            <input type="radio" name="group" id="rb2" onclick="setDepth()">3D
                        </div>-->
                        
                    </div>
                    <div class="modal-footer">
                        <button type="button" data-dismiss="modal" class="btn btn-default">Close</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div>
    </body>
</html>