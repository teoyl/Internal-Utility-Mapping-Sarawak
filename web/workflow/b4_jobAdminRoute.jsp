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
        <style type="text/css" nonce="EuTVqS192VKl">
            a.tooltip_nobs {outline:none; }
            a.tooltip_nobs strong {line-height:30px;}
            a.tooltip_nobs:hover {text-decoration:none;} 
            a.tooltip_nobs span {
                z-index:10;display:none; padding:14px 20px;
                /*margin-top:40px; margin-left:0px;*/
                margin-top:-60px; margin-left:10px;
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
        <SCRIPT language="javascript" nonce="r4DjhKbfO5ry">
            $(document).ready(function () {
//                $('#graphInfo').modal('hide');
                $("#assignTo__").select2();
                
                $('#viewWorkload').on('click', function (e) {
                    showWorkload();
                });
                
                $('#assignToNewBtn').on('click', function (e) {
                    setData('%{model.pl_id}','','NEW');
                });
                
                $('#assignToReassignBtn').on('click', function (e) {
                    setData('%{model.pl_id}','','REASSIGN');
                });
                
                $('#assignToReassignBtn').on('click', function (e) {
                    setData('%{model.pl_id}','','REASSIGN');
                });
                
                
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

            function graphClose() {
                $('#graphInfo').modal('hide');
            }
            function LoadChart() {
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
                winPodH.onclose = function () {
                    return false;
                }
            }

            function submitForm(p1) {
                document.getElementById("pType").value = p1;
                document.jobForm.submit();
            }
        </SCRIPT>
        <style nonce="EuTVqS192VKl">           
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
            
            .assignDiv{
               padding: 10px ;
            }
            
            .assignBtn{
               float:right; margin-top: -5px;
            }
            
            #chartdiv{
                width: 100%;
                height: 400px;
            }
            
        </style>            
    </head>
    <body>
        <form id="jobForm" name="jobForm" action="processUpdateRouteJobAdmin" method="POST">
            <s:hidden theme="simple" name="action" value="RouteJobAdmin" escapeHtml="true" escapeJavaScript="true"/>
            <s:hidden name="pl_id" theme="simple" value="%{model.pl_id}" />
            <div class="row">
                <div class="col-md-4">
                    <!--begin:: Job detail  -->
                    <div class="kt-portlet kt-portlet--height-fluid">
                        <div class="kt-portlet__head">
                            <div class="kt-portlet__head-label">
                                <h3 class="kt-portlet__head-title">
                                    Job Detail
                                </h3>
                            </div>
                        </div>
                        <div class="kt-portlet__body">
                            <div class="kt-widget3">
                                <div class="kt-widget3__item">
                                    <div class="kt-widget3__header">
                                        <!--<i class="fa fa-circle-thin  " aria-hidden="true"></i>-->
                                        <div class="kt-widget3__user-img"><i class="fa fa-check-circle  " aria-hidden="true"></i></div>
                                        <div class="kt-widget3__info  kt-font-bold"><s:text name="tol.case_ref"/></div>
                                        <span class="kt-widget3__status kt-font-info">&nbsp;</span>	
                                    </div>
                                    <div class="kt-widget3__body">
                                        <p class="kt-widget3__text"> 
                                            <s:property value='%{jobAdminMap.get("case_ref")}'/>
                                        </p>	
                                    </div>
                                </div>
                            </div>
                            <div class="kt-widget3">
                                <div class="kt-widget3__item">
                                    <div class="kt-widget3__header">
                                        <div class="kt-widget3__user-img"><i class="fa fa-check-circle  " aria-hidden="true"></i></div>
                                        <div class="kt-widget3__info  kt-font-bold"><s:text name="jobadmin.systemname" /></div>
                                        <span class="kt-widget3__status kt-font-info">&nbsp;</span>	
                                    </div>
                                    <div class="kt-widget3__body">
                                        <p class="kt-widget3__text"> 
                                            <s:property value='%{jobAdminMap.get("wf_subsystem")}'/>
                                        </p>	
                                    </div>
                                </div>
                            </div>
                            <div class="kt-widget3">
                                <div class="kt-widget3__item">
                                    <div class="kt-widget3__header">
                                        <div class="kt-widget3__user-img"><i class="fa fa-check-circle  " aria-hidden="true"></i></div>
                                        <div class="kt-widget3__info  kt-font-bold"><s:text name="jobadmin.poolstatus" /></div>
                                        <span class="kt-widget3__status kt-font-info">&nbsp;</span>	
                                    </div>
                                    <div class="kt-widget3__body">
                                        <p class="kt-widget3__text"> 
                                            <s:text name='TaskMgmt.status.%{jobAdminMap.get("task_status")}' />
                                        </p>	
                                    </div>
                                </div>
                            </div>
                            <div class="kt-widget3">
                                <div class="kt-widget3__item">
                                    <div class="kt-widget3__header">
                                        <div class="kt-widget3__user-img"><i class="fa fa-check-circle  " aria-hidden="true"></i></div>
                                        <div class="kt-widget3__info  kt-font-bold"><s:text name="jobadmin.poolassigndate"/></div>
                                        <span class="kt-widget3__status kt-font-info">&nbsp;</span>	
                                    </div>
                                    <div class="kt-widget3__body">
                                        <p class="kt-widget3__text"> 
                                            <s:property value='%{jobAdminMap.get("created_date")}'/>
                                        </p>	
                                    </div>
                                </div>
                            </div>
                            <div class="kt-widget3">
                                <div class="kt-widget3__item">
                                    <div class="kt-widget3__header">
                                        <div class="kt-widget3__user-img"><i class="fa fa-check-circle  " aria-hidden="true"></i></div>
                                        <div class="kt-widget3__info  kt-font-bold">Assign To</div>
                                        <span class="kt-widget3__status kt-font-info">&nbsp;</span>	
                                    </div>
                                    <div class="kt-widget3__body">
                                        <p class="kt-widget3__text"> 
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
                                            </p>	
                                        </div>
                                    </div>
                                </div>
                                <div class="kt-widget3">
                                    <div class="kt-widget3__item">
                                        <div class="kt-widget3__header">
                                            <!--<i class="fa fa-circle-thin  " aria-hidden="true"></i>-->
                                            <div class="kt-widget3__user-img"><i class="fa fa-check-circle  " aria-hidden="true"></i></div>
                                            <div class="kt-widget3__info  kt-font-bold"><s:text name="jobadmin.caseDesc"/></div>
                                        <span class="kt-widget3__status kt-font-info">&nbsp;</span>	
                                    </div>
                                    <div class="kt-widget3__body">
                                        <p class="kt-widget3__text"> 
                                            <s:property value='%{jobAdminMap.get("case_desc")}'/>
                                        </p>	
                                    </div>
                                </div>
                            </div>
                            <div class="kt-widget3">
                                <div class="kt-widget3__item">
                                    <div class="kt-widget3__header">
                                        <!--<i class="fa fa-circle-thin  " aria-hidden="true"></i>-->
                                        <div class="kt-widget3__user-img"><i class="fa fa-check-circle  " aria-hidden="true"></i></div>
                                        <div class="kt-widget3__info  kt-font-bold"><s:text name="common.division"/></div>
                                        <span class="kt-widget3__status kt-font-info">&nbsp;</span>	
                                    </div>
                                    <div class="kt-widget3__body">
                                        <p class="kt-widget3__text"> 
                                            <s:property value='%{jobAdminMap.get("case_div")}'/>
                                        </p>	
                                    </div>
                                </div>
<!--                                <div class="kt-widget3">
                                    <div class="kt-widget3__item">
                                        <div class="kt-widget3__header">
                                            <div class="kt-widget3__user-img"><i class="fa fa-check-circle  " aria-hidden="true"></i></div>
                                            <div class="kt-widget3__info  kt-font-bold"><s:text name="common.land.district"/></div>
                                            <span class="kt-widget3__status kt-font-info">&nbsp;</span>	
                                        </div>
                                        <div class="kt-widget3__body">
                                            <p class="kt-widget3__text"> 
                                                <s:property value='%{jobAdminMap.get("case_dis")}'/>
                                            </p>
                                        </div>
                                    </div>
                                </div>-->
                                <div class="kt-widget3">
                                    <div class="kt-widget3__item">
                                        <div class="kt-widget3__header">
                                            <!--<i class="fa fa-circle-thin  " aria-hidden="true"></i>-->
                                            <div class="kt-widget3__user-img"><i class="fa fa-check-circle  " aria-hidden="true"></i></div>
                                            <div class="kt-widget3__info  kt-font-bold">Job Description</div>
                                            <span class="kt-widget3__status kt-font-info">&nbsp;</span>	
                                        </div>
                                        <div class="kt-widget3__body">
                                            <p class="kt-widget3__text"> 
                                                <s:property value='%{jobAdminMap.get("wf_name") + " : " + jobAdminMap.get("task_description")}'/>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--end:: Job detail -->	


                <div class="col-md-4">
         
                    <!--Begin::Portlet-->
                    <div class="kt-portlet kt-portlet--height-fluid">
                        <div class="kt-portlet__head">
                            <div class="kt-portlet__head-label">
                                <h3 class="kt-portlet__head-title">
                                    Task Flows
                                </h3>
                            </div>
                        </div>
                        <div class="kt-portlet__body">
                            <!--Begin::Timeline 3 -->
                            <div class="kt-timeline-v2">
                                <div class="kt-timeline-v2__items  kt-padding-top-25 kt-padding-bottom-30">
                                    <s:iterator value='jobAdminMap.get("progressList")' var="progress" status="progressStatus">
                                        <div class="kt-timeline-v2__item">
                                            <span class="kt-timeline-v2__item-time"></span>
                                            <div class="kt-timeline-v2__item-cricle">
                                                <i class="fa fa-genderless kt-font-danger"></i>
                                            </div>
                                            <div class="kt-timeline-v2__item-text  kt-padding-top-5">
                                                <a href="#" class="tooltip_nobs"><s:property escapeHtml="false" value="%{#progress}"/></a>                                           	                                	               
                                            </div>
                                        </div>
                                    </s:iterator>
                                </div>
                            </div>
                            <!--End::Timeline 3 -->
                        </div>
                    </div>
                    <!--End::Portlet-->	
                </div>	
       <!--begin:: job adminstartion-->
                <div class="col-md-4">
                    <div class="kt-portlet kt-portlet--height-fluid">
                        <div class="kt-portlet__head">
                            <div class="kt-portlet__head-label">
                                <h3 class="kt-portlet__head-title">
                                    Job Administration 
                                </h3>
                            </div>
                        </div>
                        <div class="kt-portlet__body">
                            <s:if test='jobAdminMap.get("task_status").equals("20")'>
                                <div class="col-md-12">
                                    <s:if test='assigneeList.size() > 0'>
                                        <!--<div class ="row">-->
                                        <div class ="col-md-12">
                                            <div class="form-group form-group-default form-group-default-select2">
                                                <label><s:text name="TaskMgmt.manage.reassignTo"/></label>
                                                <s:select list="assigneeList" cssClass="form-control full-width" data-init-plugin="select2" listKey="userId" listValue="userName" name="assignTo__" theme="simple"/>                                            
                                            </div>
                                        </div>
                                        <div class ="col-md-12"> 
                                            <div class="form-group form-group-default ">
                                            <!--<button class="btn btn-primary" type="submit" name="action:assignRouteJobAdmin" id="viewWorkload"><i class="fa fa-area-chart"></i><s:text name="btn.view.workload"/></button>-->
                                            <button class="btn btn-primary" type="button" name="viewWorkload" id="viewWorkload"><i class="fa fa-area-chart"></i><s:text name="btn.view.workload"/></button>
                                             </div>
                                        </div>
                                        <div class ="col-md-12">
                                            <div class="form-group form-group-default">
                                                <s:if test='jobAdminMap.get("task_status").equals("20") || jobAdminMap.get("task_status").equals("10")'>
                                                    <button class="btn btn-primary" type="submit" name="action:assignRouteJobAdmin" id="assignJobAdminRoute"><i class="fa fa-user"></i><s:text name="TaskMgmt.manage.A"/></button>
                                                </s:if>
                                            </div>
                                        </div>
                                        <!--</div>-->
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
                                <div class="col-md-12">
                                    <div class="form-group form-group-default form-group-default-select2">
                                        <label><s:text name="TaskMgmt.manage.assignTo"/></label>
                                        <s:select list="assigneeList" cssClass="form-control full-width" data-init-plugin="select2" listKey="userId" listValue="userName" name="assignTo__" theme="simple"/>
                                    </div>
                                </div>
                            </s:elseif>
                        </div>

                        <div class="kt-portlet__foot">
                            <div class="kt-form__actions">
                                <div class="row">
                                    <div class="col-md-12 text-right">
                                        <%--<s:if test='jobAdminMap.get("task_status").equals("20") || jobAdminMap.get("task_status").equals("10")'>--%>
                                            <!--<button class="btn btn-primary" type="submit" name="action:assignRouteJobAdmin" id="assignJobAdminRoute"><i class="fa fa-user"></i><s:text name="TaskMgmt.manage.A"/></button>-->
                                        <%--</s:if>--%>
                                        <button class="btn btn-default" type="submit" name="action:cancelRouteJobAdmin" id="cancelJobAdminRoute"><i class="fa fa-arrow-left"></i>Back</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div> 
                </div>   
                <!--end:: job adminstartion-->  
            </div>    
            <s:hidden name="id" value="%{id}" />
            <s:hidden name="pPlId" value="" />
            <s:hidden name="pJpId" value="" />
            <s:hidden name="pType" value="" />
            <s:hidden name="assignTo" value="" />


            <div id="divAssignToNew" class="hide-disp">
                <p class="assignDiv" >
                    <s:if test='assigneeList.size() > 0'>
                        <s:text name="jobadmin.assignto" />: <s:select id="assignTo_new" name="assignTo_new" theme="simple" list="assigneeList" listKey="userId" listValue="userName" value="" />
                        <s:submit theme="simple" cssClass="defaultButton" id="assignToNewBtn" action="" value='%{getText("jobadmin.btn.assign")}' />
                    </s:if>
                    <s:else>
                        <font class="errortxt"><s:text name="jobadmin.noAssignee" /></font>
                        </s:else>
                </p>
            </div>

            <div id="divAssignToReassign" class="hide-disp">
                <p class="assignDiv">
                    <s:if test='assigneeList.size() > 0'>
                        <s:text name="jobadmin.assignto" />: <s:select id="assignTo_reassign" name="assignTo_reassign" theme="simple" list="assigneeList" listKey="userId" listValue="userName" value=""/>
                        <span class="assignBtn"><s:submit theme="simple" cssClass="defaultButton" action="" id="assignToReassignBtn" value='%{getText("jobadmin.btn.assign")}' /></span>
                    </s:if>
                    <s:else>
                        <font class="errortxt"><s:text name="jobadmin.noAssignee" /></font>
                        </s:else>
                </p>
            </div>
        </form>
        <div id="graphInfo" class="modal fade hide-disp" tabindex="-1" data-width="60%" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h3 class="title-v2">View Workload</h3><br>
                    </div>
                    <div class="modal-body myModalContent">
                        <div id="chartdiv"></div>
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