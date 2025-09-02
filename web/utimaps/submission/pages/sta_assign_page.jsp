<%-- 
    Document   : sta_assign_page
    Created on : Mar 25, 2025, 4:52:23 PM
    Author     : yonglai
--%>

<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<script src="pages/scripts/amcharts_2.js" type="text/javascript"></script>
<script src="pages/scripts/serial.js" type="text/javascript"></script>

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
        overflow: scroll !important;
    }

    .full-screen {
        width:90% !important;
        max-width: none !important;
    }
</style>   

<div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingAssignSpatial">
        <button class="accordion-button <s:if test='!activeAccordion.equals("4")'>collapsed</s:if> fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseAssignSpatial" aria-expanded="false" aria-controls="flush-collapseAssignSpatial">
            <i class="fas fa-tasks me-2"></i><s:text name="utimaps.form.submission.staAssignSpatial" />
            
            <s:if test="model.wf_status_2 > '301'">
                <span class="position-absolute end-50px text-success">[<s:text name = "utimaps.form.label.completed" />]</span>
            </s:if>
            <s:else>
                <span class="position-absolute end-50px text-primary">[<s:text name = "utimaps.form.label.inProgress" />]</span>
            </s:else>
        </button>
    </h2>
    <div id="flush-collapseAssignSpatial" class="accordion-collapse collapse <s:if test='activeAccordion.equals("4")'>show</s:if>" aria-labelledby="flush-headingAssignSpatial" data-bs-parent="#accordionFlushApplication">
        <div class="accordion-body">
            <form action="" method="post" id="staAssignSpatialForm">
                <s:hidden name="case_id" id="case_id" value="%{model.case_id}"/>
                <s:hidden name="job_id" id="job_id" value="%{model.job_id}"/>
                <s:hidden name="actionName" id="actionName" value="%{actionName}"/>
                <s:hidden name="taskId_" value="%{taskId_}"/>
                <s:hidden name="wfActivityCode" value="%{wfActivityCode}"/> 
                <s:hidden name="model.wf_status" id="model.wf_status" value="%{model.wf_status}"/> 
                <s:hidden name="app_submit_by" id="app_submit_by" value="%{model.app_submit_by}"/> 
                <s:hidden name="case_ref" id="case_ref" value="%{model.case_ref}"/> 
                <s:hidden name="processType" id="processType" value="280"/> 
                <s:hidden name="model.wf_status_2" id="model.wf_status_2" value="%{model.wf_status_2}"/> 
                <s:hidden name="control_sv_flag" id="control_sv_flag" value="%{model.control_sv_flag}"/>
                <s:hidden name="comp_completed" id="comp_completed" value="%{model.comp_completed}"/>
                
                <div class="row">
                    <div class="col-md-12 mb-3">                   
                        <div class="col-md-12">
                            <s:if test='assigneeList.size() > 0'>
                                <div class ="col-md-12 mb-3">
                                    <div class="form-group form-group-default form-group-default-select2">
                                        <label><s:text name="TaskMgmt.manage.assignTo"/></label>
                                        <s:select list="assigneeList" cssClass="form-control full-width" data-init-plugin="select2" listKey="userId" listValue="userName" name="assignTo__" theme="simple"/>                                            
                                    </div>
                                </div>
                                <div class ="col-md-12 mb-3 text-end">
                                    <button class="btn btn-info" type="button" name="viewWorkload" id="viewWorkload"><i class="fas fa-chart-area"></i></i> <s:text name="btn.view.workload"/></button>
                                    
                                    <button class="btn btn-success" type="submit" name="completeAssignSubmission" id="completeAssignSubmission"><i class="fa fa-user"></i> <s:text name="TaskMgmt.manage.A"/></button>
                                </div>
                            </s:if>
                        </div>
                       
                    </div>
                    <div class="col-md-12">
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
                
<div id="graphInfo" class="modal fade hide-disp" tabindex="-1" data-width="100%" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog full-screen">
        <div class="modal-content">
            <div class="modal-header">
                <button class="close" data-bs-dismiss="modal" aria-label="Close"><i class="far fa-window-close"></i></button>
                <h3 class="title-v2">View Workload</h3><br>
            </div>
            <div class="modal-body myModalContent">
                <div id="chartdiv"></div>
            </div>
            <div class="modal-footer">
                <button data-bs-dismiss="modal" class="btn btn-default" aria-label="Close">Close</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
    
<script nonce="r4DjhKbfO5ry">
    $(document).ready(function () {
        scrollToView("flush-collapseAssignSpatial");
        
        $('#viewWorkload').on('click', function (e) {
            showWorkload();
        });
    });
    
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

    $('#completeAssignSubmission').click(function (e) {
        e.preventDefault();
        var displayMessage = "You are about to COMPLETE this task.";
        
        confirmationBox(displayMessage, "#staAssignSpatialForm", "processComplete2JobSubmission");
    });

</script>