<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<title><s:text name="jobList.title" /></title>
<script src="pages/scripts/amcharts_2.js" type="text/javascript"></script>
<script src="pages/scripts/serial.js" type="text/javascript"></script>
<script type="text/javascript" src="include/jquery/loadingoverlay.min.js"></script>
<script type="text/javascript" src="espa/espa_js.js"></script>
<!--<script type="text/javascript" src="pages/scripts/lookup.js"></script>-->
<s:head />


<script language="javascript">
    function confirmTaskSelectJob() {
            var answer = confirm("The selected job(s) will be assigned to the officer. Do you want to proceed?");<%----%>
            var isIE = false;

            isIE = isInternetExplorer();
            return (isIE? event.returnValue = answer : answer);
        }

        function taskSelectJob() {
            if (confirmTaskSelectJob()) {
                document.getElementById("testingForm").action = "processAssignBatchRouteJobMain";
                document.getElementById("testingForm").method = "POST";
                document.getElementById("testingForm").submit();
            }
        }
    $(document).ready(function() {
        //done and added by edmund 10052019-------- start
        var hasElement = $('#assignedJobListSize').val();
        var testRet = $('#testRet').val();
        if(testRet!=""){
            var list = new Array();
            list = testRet.split(',');
            for(var i = 0 ; i<list.length; i++){
                for(var a = 0; a<hasElement;a++){
                    var getRet = $("#"+a).val();
                    if(getRet==list[i]){
                        $("#"+a).attr("checked", true);
                    }
                }
            }
        }
        //--------------------------------end ---------------
        $.LoadingOverlaySetup({
            background      : "white",
            image           : "images/preloader3.gif",
            imageAnimation  : "false",
            imageColor      : "#ffcc00",
            minSize         : 64,
            maxSize         : 64
        });
        
        
        if($('.incompleteJobCnt').val() > 0){
            $('.myTable2').DataTable({
                "paging":   true
            });
        }
        
        var pageFrom = $('.pageFrom').val();
        var selected_tab = $('.selected_tab').val();
//        alert("pageFrom=" + pageFrom);
//        alert("selected_tab=" + selected_tab);
        if(pageFrom == "welcome"){ //Unassigned job click from mainpage
            if(selected_tab == ""){
                $('.li_tab1').addClass("active");
                $('#tab1').addClass("active");
                loadUnassignedJob('SPA','SPA');
            }else{
                $('.li_'+selected_tab).addClass("active");
                $('#'+selected_tab).addClass("active");
            }
        }else{ //my job click from mainpage
            if(selected_tab == ""){
                $('.li_tab2').addClass("active");
                $('#tab2').addClass("active");
            }else{
                $('.li_'+selected_tab).addClass("active");
                $('#'+selected_tab).addClass("active");
                if(selected_tab == 'tab1'){
                    loadUnassignedJob('SPA','SPA');
                }
            }
        }
        
        // store the currently selected tab in the hash value
        $("ul.nav-tabs > li > a").on("shown.bs.tab", function(e) {
            var id = $(e.target).attr("href").substr(1);
            $('.selected_tab').val(id);
        });

        
        
    });
    
    var winPodH;
    function showUnassignedJob(pSysId) {
        winPodH=dhtmlmodal.open("popup", "iframe", "processSearchRouteJobMain?useRoute=true&istrSystemId="+pSysId+"&jobSearchAction=search&defaultPageSize=50", "Unassigned Job", "width=1000px,height=400px,resize=1,scrolling=1,center=3", "");
        //sereneC added @ 11/8/2014 to get back tab correct after load into iframe pop up.
        document.getElementById("insertJobForm").action="processInsertRouteJobMain#tab"+pSysId; 
        winPodH.onclose=function(){
            return false;
        }
    }
    
    function loadUnassignedJob(pSysId,pSelectedSystem){
        $.LoadingOverlay("show");
        var url = "processSearchRouteJobMain?useRoute=true&istrSystemId="+pSysId+"&sys_name="+pSelectedSystem+"&jobSearchAction=search&defaultPageSize=50";
        $.get(url, function(data) {
            $("#tab1").html(data);
        });
        document.getElementById("insertJobForm").action="processInsertRouteJobMain#tab"+pSysId; 
        
        $.LoadingOverlay("hide");
    }
    
    function loadContent(pSysId) {
        if (pSysId === "SPA" || pSysId === "QP") {
            $.LoadingOverlay("show");
//            document.location="loadEditPageRouteJobMain";
            $.LoadingOverlay("hide");
        } else {
            $("#loader").show();
            $.LoadingOverlay("show");
            var url = "loadJobContentRouteJobMain?useRoute=true&istrSystemId=" + pSysId;
    //        console.log(url);
            $.get(url, function(data) {
                $("#jobContentDiv").html(data);
                $.LoadingOverlay("hide");
                $("#loader").hide();
            });
        }
    }
    function loadCompleteContent(pSysId,pDuration) {
        if (pSysId === "SPA") {
            $.LoadingOverlay("show");
            document.location="loadEditPageRouteJobMain";
            $.LoadingOverlay("hide");
        } else {
           
            $("#loader").show();
            $.LoadingOverlay("show");
            var url = "loadJobContentRouteJobMain?useRoute=true&istrSystemId=" + pSysId+"&pDuration="+pDuration;
    //        console.log(url);
            $.get(url, function(data) {
                if(pDuration=="today"){
                    $("#todayDiv").html(data);
                }else{
                    $("#30dayDiv").html(data);
                }
                $("#loader").hide();
            });
             $.LoadingOverlay("hide");
        }
    }
    // ThoTH @ 23-Jul-2015
    function cancelInformed(pPlId) {
        document.getElementById("selectJobId_").value = pPlId;
        document.getElementById("insertJobForm").action="jobCancelInformedJobMain#";
        document.insertJobForm.submit();
    }
    
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
    
</script>

<!--<script type="text/javascript" src="include/simple_tab/simple_tab.js"></script>-->
<!--<link rel="stylesheet" href="include/simple_tab/simple_tab.css" />-->
</head>
<body>

<div id="myhash"></div>
<style>
.leftBtn1{
    padding: 0px 50px;
    background-color: #24A259;
    top:300px!important;
    display:block!important;
    left: 0!important;
}

.leftBtn2{
    padding: 0px 50px;
    background-color: black;
    top:440px!important;
    display:block!important;
    left: 0!important;
}
.side-btn {
    color:white;
    display:none;
    position:fixed;
    z-index:100000;
    cursor:pointer;
     -ms-transform-origin: 0 0;
    -webkit-transform-origin: 0 0;
    -o-transform-origin: 0 0;
    -khtml-transform-origin: 0 0;
    transform-origin: 0 0;
    -ms-transform: rotate(-90deg);
    -webkit-transform: rotate(-90deg);
    -o-transform: rotate(-90deg);
    -khtml-transform: rotate(-90deg);
    transform: rotate(-90deg);
}

.side-btn-content {
    color: #FFFFFF!important;
    font-size: 18px;
    white-space: nowrap;
    text-align: center;
}
.side-btn-content a {
    color: white;
}
</style>
    <s:hidden name="selected_tab" cssClass="selected_tab"/>
    <s:hidden name="pageFrom" cssClass="pageFrom" value="%{pageFrom}"/>
    <s:hidden name="selected_system" cssClass="selected_system" value="%{selected_system}"/>
    <s:hidden name="listJobBySystem" cssClass="listJobBySystem" value="%{listJobBySystem.size()}"/>
        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
        <div id="sysId_" style="display: none">${sysId_}</div>
        
        <s:if test='getCalcTotalSystem() > 1'>
            <a href="loadEditPageRouteJobMain?sys_name=SPA" class="espaBtn">
                <div class="leftBtn1 side-btn">
                   <div class="side-btn-content">eSPA</div>
               </div>
            </a> 
            <a href="loadEditPageRouteJobMain?sys_name=QP" class="eqpBtn">
               <div class="leftBtn2 side-btn">
                    <div class="side-btn-content">eQP</div>
                </div>
            </a> 
        </s:if>
 
        
          
        <s:if test="listJobtobeGrab.size() <= 0">
            <div class="row">
                <div class="errortxt col-xs-12"><%--You are not been assigned to any Workflow.--%>There is NO job for you at this moment.</div>
            </div>
        </s:if>
        <s:else>
        <%--tab--%>
        <div class="row row-tabs" role="tabpanel">
            <div class="col-xs-12">
                <div class="tabs-top tabs-justified-top">
                    <ul class="nav nav-tabs nav-justified" role="tablist" id="myTab">
                        <s:iterator value="listJobtobeGrab" status="jobPoolStatus" id="iteratorJobPool">
                            <s:hidden name="incompleteJobCnt" cssClass="incompleteJobCnt" value="%{#iteratorJobPool.get('ipcount')}"/>
                            <li role="presentation" class="li_tab1">
                                <a href="#tab1" onclick="loadUnassignedJob('<s:property value="%{#iteratorJobPool.get('wf_subsystem')}"/>','<s:property value="%{selected_system}"/>')" aria-controls="tab1" role="tab" data-toggle="tab">
                                   Unassigned Job<small class="label pull-right bg-green"><s:property value="%{#iteratorJobPool.get('longcount')}"/></small>
                                </a>
                            </li>
                            <li role="presentation" class="li_tab2">
                                <a href="#tab2" onclick="loadContent('<s:property value="%{#iteratorJobPool.get('wf_subsystem')}"/>')" aria-controls="tab2" role="tab" data-toggle="tab">
                                   In Progress<small class="label pull-right bg-green"><s:property value="%{#iteratorJobPool.get('ipcount')}"/></small>
                                </a>
                            </li>
                        </s:iterator>
                        <li role="presentation" class="li_tab3">
                            <a href="#tab3" onclick="loadCompleteContent('COMPLETED','today')" aria-controls="tab3" role="tab" data-toggle="tab">
                                Completed Today<small class="label pull-right bg-green"><s:property value="completedToday"/></small>
                            </a>
                        </li>
                        <!--2019.01.09 Edited by IvyL-->
                         <li role="presentation" class="li_tab4">
                            <a href="#tab4" onclick="loadCompleteContent('COMPLETED','30day')" aria-controls="tab4" role="tab" data-toggle="tab">
                                Completed Past 30 Days<small class="label pull-right bg-green"><s:property value="%{totalCompleted7Days}"/></small>
                            </a>
                        </li>
                    </ul>
                    <div class="tab-content"  id="jobContentDiv">
                        <div role="tabpanel" class="tab-pane fade in" id="tab1">
                            
                        </div>
                        <div role="tabpanel" class="tab-pane fade in" id="tab2">
                            <form action="processAssignBatchRouteJobMain" method="POST" id="testingForm" name="testingForm">
                            <div class="table-responsive">
                                <table class="table table-espa table-condensed table-striped myTable2" cellspacing="0" cellpadding="0" width="100%">
                                    <thead>
                                        <tr>
                                            <td colspan="10">
                                                <!--<button class="btn btn-default"><a href="loadEditPageRouteJobMain"><i class="fa fa-user" style="margin-left:10px;font-size:14px;"> Assign Single Job</i></a></button>-->
                                                <a href="loadEditPageRouteJobMain" class="btn btn-default"><i class="fa fa-user"></i> Assign Single Job</a>
                                                <s:hidden theme="simple" cssClass="" id="testRet" name="testRet" value='%{testRet}'/>
                                                <s:hidden name="assignedJobListSize" value="%{assignedJobListSize}" id="assignedJobListSize"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th width="1%">
                                                <input type="checkbox" name="job_select" class="input_" id="job_select" onClick="toggleCheckboxByName(this, 'job_selected');">
                                            </th>
                                            <th width="1%"><s:text name="jobList.jobNo" /></th>
                                            <th width="10%"><s:text name="jobList.jobDate" /></th>
                                            <th width="5%"> <s:text name="jobList.from" /></th>
                                            <th width="12%"><s:text name="jobList.jobDueDate" /></th>
                                            <th width="13%">eCase Ref.</th>
                                            <th width="17%"><s:text name="jobList.jobItem" /></th>
                                            <th width="7%"><s:text name="jobpool.job.history"/></th>
                                            <th width="24%"><s:text name="jobList.jobDetail" /></th>
                                            <th width="10%"><s:text name="jobList.jobStatus" /></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <br>
                                        <s:if test="genListBySystem(#iteratorJobPool.get('wf_subsystem')) > 0">
                                            <s:iterator value="listJobBySystem" status="jobProgressStatus" id="iteratorJobBySystem">
                                                <tr class="
                                                    <s:if test="#iteratorJobBySystem.get('today') = #iteratorJobBySystem.get('due_date')">
                                                    due
                                                    </s:if><s:elseif test="#iteratorJobBySystem.get('today') > #iteratorJobBySystem.get('due_date')">
                                                    lapse
                                                    </s:elseif><s:else>
                                                        <s:if test="#jobPoolStatus.odd == true ">odd</s:if><s:else>even</s:else>
                                                    </s:else>
                                                    ">
                                                    <td class="" width="1%">                                                        
                                                        <!--<input type="checkbox" name="job_selected" class="checkbox_child form-control input_" id="${jobProgressStatus.index}" value="${iteratorJobBySystem.get('task_id')}::${iteratorJobBySystem.get('caseId')}" onclick="toggleSelectAll()">-->
                                                        <input type="checkbox" name="job_selected" class="input_" id="${jobProgressStatus.index}" value="${iteratorJobBySystem.get('task_id')}::${iteratorJobBySystem.get('caseId')}" onclick="toggleSelectAll()">
                                                                    <label for="${jobProgressStatus.index}" class="tableFormCheckbox"></label>
                                                    </td>
                                                    <td class="jobLabel space_left_5">${jobProgressStatus.index + 1}</td>
                                                    <td class="jobLabel" width="10%">
                                                        <s:text name="date_default_datetime"><s:param value="%{#iteratorJobBySystem.get('grabbed_date')}"/></s:text>
                                                    </td>
                                                    <td class="jobLabel">
                                                         ${iteratorJobBySystem.get('previousTaskDoer')}
                                                    </td>
                                                    <td class="jobLabel" width="12%">
                                                        <s:if test="#iteratorJobBySystem.get('due_date') != null">                                    
                                                            <s:text name="date_default_datetime"><s:param value="%{#iteratorJobBySystem.get('due_date')}"/></s:text>
                                                            <s:if test="#iteratorJobBySystem.get('today') > #iteratorJobBySystem.get('due_date')">
                                                                <button type="button" class="btn btn-default btn-xs"><span class="time" style="color: red;" >Due</span></button>
                                                            </s:if>
                                                        </s:if>   
                                                    </td>
                                                    <td class="jobLabel" style="word-wrap: break-word" width="13%">
                                                        <s:a href="#" title="Case Details" onclick="$('#caseDetail_%{#jobProgressStatus.index}').data('width', '90%'); $('#caseDetail_%{#jobProgressStatus.index}').modal('show');">
                                                            <s:property value="%{#iteratorJobBySystem.get('eCase_ref')}"/>
                                                        </s:a>
                                                    </td>
                                                    
                                                    <td id="jobLabel" class="jobLabel" width="17%">
                                                        <s:if test='#iteratorJobBySystem.get("jpextra1").equals("RJ")'><span style="font-weight: bold; color: red">[Rejected]</span> </s:if>  
                                                        <s:if test='#iteratorJobBySystem.get("task_status").equals("F")'>  
                                                           <s:property value="%{#iteratorJobBySystem.get('wf_name')}"/>: <s:property value="%{#iteratorJobBySystem.get('task_description')}"/>
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
                                                                <s:iterator value="#iteratorJobBySystem.get('taskAction')" status="actionUrlStatus" id="actionUrl">
                                                                    <%--<s:if test='%{#actionUrl.get("actionDesc").equals("Assign Processing Officer") || #actionUrl.get("actionDesc").equals("Return Job")}'>--%>
                                                                        <s:if test="#actionUrlStatus.index > 0"><br></s:if>
                                                                        <s:else>
                                                                            <s:if test='%{#iteratorJobBySystem.get("wfJobRemark") != null && !#iteratorJobBySystem.get("wfJobRemark").equals("")}'>
                                                                                 <i title="<s:property escape="false" value="%{#iteratorJobBySystem.get('wfJobRemark')}"/>" class="fa fa-2x fa-commenting text-green" style="font-size:18px"></i><br>
                                                                            </s:if>
                                                                        </s:else>
                                                                        <a href="<s:property value="%{#actionUrl.get('actionUrl')}"/>" target="_blank" >
                                                                            <s:property value="%{#actionUrl.get('actionDesc')}"/>
                                                                        <a><br>
                                                                    <%--</s:if>--%>
                                                                </s:iterator>
                                                            </s:else>
                                                        </s:else>
                                                    </td>
                                                    <td class="jobLabel" style="word-wrap: break-word" width="7%">
                                                        <a href="javascript:;" onclick="loadHistory('loadUserTaskHistoryRouteJobMain?cid=<s:property value="%{#iteratorJobBySystem.get('caseId')}"/>')"  title="History"><i class="fa fa-clock-o" style="margin-left:10px;font-size:14px;"></i></a>
                                                        <br>
                                                        <a href="loadDiagramPageRouteJobMain?id=<s:property value="%{#iteratorJobBySystem.get('task_id')}"/>" target="_blank" >Show</a>
                                                    </td>
                                                    <td class="jobLabel" style="word-wrap: break-word" width="24%">
                                                        <s:property value="%{#iteratorJobBySystem.get('wf_name')}"/>: <s:property escape="false" value="%{#iteratorJobBySystem.get('task_description')}"/>
                                                    </td>
                                                    <td class="jobLabel">
                                                        <s:property value="getJobStatusDesc(#iteratorJobBySystem.get('task_status'))"/>
                                                        <s:if test='#iteratorJobBySystem.get("task_status").equals("F")'>  
                                                            <s:submit type="button" cssClass="defaultButton" theme="simple" value="%{getText('jobList.informed')}" onclick="cancelInformed('%{#iteratorJobBySystem.get('task_id')}')" />
                                                        </s:if>
                                                    </td>
                                                    
                                                </tr>
                                            </s:iterator>
                                        </s:if>
                                        <s:else>
                                            <tr class="errortxt"><td colspan="10" class="text-center"><s:text name="jobList.jobNone" /></td></tr>
                                            <tr><td>&nbsp;</td></tr>
                                        </s:else>    
                                    </tbody>
                                </table><br/>
                            </div>
                            <s:iterator value="batchOfficerTaskList" id="nextCompleteTask" status="nextCompleteTaskStatus">
                                <s:if test='batchOfficerTaskList.size() > 0'>
                                        <%--<s:iterator value='#nextCompleteTask.get("nextTaskList")' id="nextTask" status="nextTaskStatus">--%>
                                            <div class="panel panel-default ">  
                                            <div class="panel-heading">
                                                <h3 class="panel-title"> 
                                                    <span class="titleText">Batch Assignment Officer List</span>
                                                </h3>
                                            </div>
                                            <div class="panel-body">
                                                <div class ="row">
                                                <div class ="col-md-12">
                                                    <%--<s:hidden theme="simple" cssClass="activity_id_%{#nextCompleteTaskStatus.index}" id="activity_id_%{#nextCompleteTaskStatus.index}_%{#nextTaskStatus.index}" name="activity_id__" value='%{#nextTask.get("activity_id")}'/>--%>
                                                    
                                                    <div class="form-group form-group-default form-group-default-select2">
                                                        <label>Assign To</label>
                                                        <%--<s:select id="assignee_%{#nextCompleteTaskStatus.index}_%{#nextTaskStatus.index}" list='%{#nextTask.get("nextTaskList")}' cssClass="assignee_%{#nextCompleteTaskStatus.index}  full-width" data-init-plugin="select2" listKey="userId" listValue="userName" name="assignTo__"/>--%>                                            
                                                        <s:select id="assignee_%{#nextCompleteTaskStatus.index}" list='%{#nextCompleteTask.get("nextTaskList")}' cssClass="assignee_%{#nextCompleteTaskStatus.index}  full-width" data-init-plugin="select2" listKey="userId" listValue="userName" name="assignTo__"/>                                            
                                                    </div>
                                                </div>
                                                <div class="col-md-12">
                                                        <div class="form-group form-group-default">
                                                            <label>Job Remark</label>
                                                            <input class="form-control proposal_title" id="batchJobRemark" name="batchJobRemark" type="text" value="" maxlength="250">
                                                        </div>
                                                </div>
                                                <div class ="row">  
                                                    <!--<button class="btn btn-default block-xs" type="submit" name="action:processAssignBatchJobRouteMain" id="processAssignBatchJobRouteMain" onclick="taskSelectJob('${iteratorJobBySystem.get('caseId')}');"><i class="fa fa-trash"></i>Assign-->
                                                    <button class="btn btn-primary block-xs" type="button" name="testbuton" id="testbuton" onclick="if (isCheckboxSelected(form.job_selected)) {
                                                return taskSelectJob();
                                            } else {
                                                return false;
                                            }"><i class="fa fa-user"></i>Assign
                                                    </button>
                                                    
                                                </div>  
                                            </div>
                                            
                                            
                                            <div class="row">
                                                <br/>
                                            </div>
                                            <div class="row">
                                                <div class="panel panel-default">
                                                    <div class="panel-heading">
                                                        <h3 class="panel-title"> 
                                                            <span class="titleText">
                                                                <%--<s:text name="chart.workload.title" />--%>
                                                                ${groupDescTitle}
                                                            </span>                        
                                                        </h3>
                                                    </div>
                                                    <div class="panel-body">
                                                        <div id="chartdiv" style="width: 100%; height: <s:property value="graphHeight"/>px;"></div>
                                                        <div style="margin-left:40px;">
                                                            <input type="radio" checked="true" name="group" id="rb1" onclick="setDepth()">2D
                                                            <input type="radio" name="group" id="rb2" onclick="setDepth()">3D
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            </div>  
                                            </div>
                                        <%--</s:iterator>--%>
                                    </s:if>
                                </s:iterator>
                                        </form>
                        </div>
                        <div role="tabpanel" class="tab-pane fade in" id="tab3">
                            <div id="todayDiv"></div>
                        </div>
                        <div role="tabpanel" class="tab-pane fade in" id="tab4">
                            <div id="30dayDiv"></div>
                        </div>                
                                        
                    </div>
                </div>
            </div>
        </div><br><br>
               
        <s:if test="genListBySystem(#iteratorJobPool.get('wf_subsystem')) > 0">
            <s:iterator value="listJobBySystem" status="jobProgressStatus" id="iteratorJobBySystem">
                <div id="caseDetail_${jobProgressStatus.index}" class="modal fade" tabindex="-1" style="display: none;">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button><br>
                            </div>
                            <div class="modal-body">
                                <table class="table table-condensed table-striped table-hover" id="myTable" width="100%">
                                    <thead>
                                        <tr>
                                            <th width="2%"></th>
                                            <th width="7%">Division</th>
                                            <th width="10%">File Ref. No.</th>
                                            <th width="16%">Applicant</th>
                                            <th width="33%">Land Information</th>
                                            <th width="10%">Nature of Application</th>
                                            <th width="12%">Status</th>
                                            <th width="8%"><s:text name="mf.prepare.create.date"/></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <s:if test='applicationInProgressModel.processing_agency.equals("GOV-SD-114")'>
                                                    <img src="images/lns_logo.jpg" width="15px" height="15px"/><!--for lns cases only-->
                                                </s:if>
                                                <s:else>
                                                    <img src="images/login_logo.png" width="15px" height="15px"/><!--for ministry cases only-->
                                                </s:else>
                                            </td>
                                            <td><s:property value="applicationInProgressModel.strDivName"/></td>
                                            <td>
                                                <a href="loadEditPage${applicationInProgressModel.strStrutsAction}?id=${applicationInProgressModel.app_id}" target="_blank"><s:property value="applicationInProgressModel.digital_reference_display"/></a>
                                            </td>
                                            <td class="text-upper">
                                                <s:iterator value="#iteratorJobBySystem.get('applicationInProgressModel').dcApplicantList" status="applicantListStatus" var="applicantListValue">
                                                    <s:if test="#applicantListValue.app_id == #iteratorJobBySystem.get('applicationInProgressModel').app_id">
                                                        <s:if test="#applicantListValue.ind_id != null">
                                                            <s:if test='#applicantListValue.custIndividual.ind_name != "" && #applicantListValue.custIndividual.ind_name != null'>
                                                                <!--For siting no need show applicant name-->
                                                                <s:if test='#iteratorJobBySystem.get("applicationInProgressModel").application_type != "CMC-SP-101"'>
                                                                    <s:if test="#applicantListValue.custCompany.co_app_type.equals('ACS-GOV-01') || 
                                                                          #applicantListValue.custCompany.co_app_type.equals('ACS-GOV-02')  || 
                                                                          #applicantListValue.custCompany.co_app_type.equals('ACS-GOV-03')  || 
                                                                          #applicantListValue.custCompany.co_app_type.equals('ACS-GOV-04')  || 
                                                                          #applicantListValue.custCompany.co_app_type.equals('ACS-GOV-05') ">
                                                                    </s:if><s:else>
                                                                        ${applicantListValue.custIndividual.ind_name}<br>                                                                                
                                                                    </s:else>
                                                                </s:if>
                                                            </s:if>
                                                        </s:if>
                                                        <s:if test="#applicantListValue.co_id != null">
                                                            ${applicantListValue.custCompany.co_name}<br>
                                                        </s:if>
                                                    </s:if>
                                                </s:iterator>
                                            </td>   
                                            <td>
                                                <s:if test="#iteratorJobBySystem.get('applicationInProgressModel').afcLotsList.size > 0">
                                                    <ul style="margin-left:12px;">
                                                        <s:iterator value="#iteratorJobBySystem.get('applicationInProgressModel').afcLotsList" status="afcLotsStatus" id="afcLotsModel">
                                                            <li>
                                                                <s:property value="%{#afcLotsModel.land_desc}"/>
                                                                (<s:property value="%{#afcLotsModel.affected_area}"/>
                                                                <s:property value="%{#afcLotsModel.affected_area_type}"/>)
                                                            </li>
                                                        </s:iterator>
                                                    </ul>
                                                </s:if>
                                                <s:else>
                                                    -
                                                </s:else>
                                            </td>
                                            <td class="text-upper">
                                                <s:property value="applicationInProgressModel.sp_project_name"/>
                                            </td>
                                            <td>${applicationInProgressModel.strStatusName}</td>
                                            <td>${applicationInProgressModel.created_datetime_str}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </s:iterator>
        </s:if>
        </s:else>
        <form id="insertJobForm" name="insertJobForm" action="processInsertRouteJobMain" method="POST">
            <s:hidden theme="simple" name="selectJobId_" value=""/>
            <s:hidden theme="simple" name="pod_remarks" value=""/>
            <!--sereneChye @ 24/9/2014-->
            <s:hidden theme="simple" id="sysId_" name="sysId_" value=""/>
        </form>
        
    <!--</div>-->
    <!--SubmissionStatus History Modal -added by edmund 16/4/2019-->
    <div id="submissionHistory" class="modal fade" tabindex="-1" style="display: none;" data-keyboard="false" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header"></div>
                <div class="modal-body"><button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                </div>
            </div>
        </div>
    </div>
    </body>
</html>