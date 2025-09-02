<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
    <style>
        .bg-blue{
            background-color:#5AA0CA;
        }
    </style>
<title><s:text name="jobList.title" /></title>
<s:head />
<!--DataTable-->
<!--<script src="include/datatable/jquery.dataTables.min.js"></script>-->
<!--<script src="include/datatable/dataTables.bootstrap.min.js"></script>-->
<!--<link href="include/datatable/jquery.dataTables.min.css" rel="stylesheet"/>-->
<!--<link href="include/datatable/dataTables.bootstrap.min.css" rel="stylesheet"/>-->
<!--<script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>-->
<!--<script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>-->
<!--<link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />-->
<!--<link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />-->
<!--<script type="text/javascript" src="include/jquery/loadingoverlay.min.js"></script>-->
<script type="text/javascript" src="workflow/route.js"></script>

<script language="javascript">
    $(document).ready(function() {
        //$.LoadingOverlaySetup({
//            background      : "white",
//            image           : "images/preloader3.gif",
//            imageAnimation  : "false",
//            imageColor      : "#ffcc00",
//            minSize         : 64,
//            maxSize         : 64
//        });
        
        if($('.incompleteJobCnt').val() > 0){
            $('.myTable2').dataTable({
                "columnDefs" : [{"targets":1, "type":"date"}]
//                "paging":   true
            });
        }
        
        var pageFrom = $('.pageFrom').val();
        var selected_tab = $('.selected_tab').val();
        if(pageFrom == "welcome"){ //Unassigned job click from mainpage
            if(selected_tab == ""){ 
               $('.li_tab1').addClass("active");
                $('#tab1').addClass("active");
                loadUnassignedJob('<s:property escapeHtml="true" value="selected_sys_id"/>','<s:property escapeHtml="true" value="selected_sys_id"/>');
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
                    loadUnassignedJob('<s:property escapeHtml="true" value="selected_sys_id"/>','<s:property escapeHtml="true" value="selected_sys_id"/>');
                }
            }
        }
        
        
//        $('#myTab a').click(function(e) {
//            e.preventDefault();
//            $(this).tab('show');
//        });

        // store the currently selected tab in the hash value
        $("ul.nav-tabs > li > a").on("shown.bs.tab", function(e) {
            var id = $(e.target).attr("href").substr(1);
            $('.selected_tab').val(id);
        });

        // on load of the page: switch to the currently selected tab
//        var hash = window.location.hash;
//        console.log("hash :"+hash);
//        $('#myTab a[href="' + hash + '"]').tab('show');
        
        
    });
    
    var winPodH;
    function showUnassignedJob(pSysId) {
//        console.log('pSysId!!' + pSysId)
//        winPodH=dhtmlmodal.open("popup", "iframe", "showUnassignedJobJobMain?istrSystemId="+pSysId, "Tugasan Belum Diserah", "width=900px,height=400px,resize=1,scrolling=1,center=1", "");
//        winPodH=dhtmlmodal.open("popup", "iframe", "processSearchRouteJobMain?useRoute=true&istrSystemId="+pSysId+"&jobSearchAction=search&defaultPageSize=50", "Unassigned Job", "width=900px,height=400px,resize=1,scrolling=1,center=3", "");
//        winPodH=dhtmlmodal.open("popup", "iframe", "processSearchRouteJobMain?useRoute=true&istrSystemId=QP&jobSearchAction=search&defaultPageSize=50", "Unassigned Job", "width=900px,height=400px,resize=1,scrolling=1,center=3", "");
        winPodH=dhtmlmodal.open("popup", "iframe", "processSearchRouteJobMain?useRoute=true&istrSystemId="+pSysId+"&jobSearchAction=search&defaultPageSize=50", "Unassigned Job", "width=1000px,height=400px,resize=1,scrolling=1,center=3", "");
//        winPodH=dhtmlmodal.open("popup", "iframe", "loadUnassignJobSearchPageJobMain?istrSystemId="+pSysId, "Tugasan Belum Diserah", "width=1000px,height=500px,resize=1,scrolling=1,center=1", "");
        //sereneC added @ 11/8/2014 to get back tab correct after load into iframe pop up.
        document.getElementById("insertJobForm").action="processInsertRouteJobMain#tab"+pSysId; 
//        document.getElementById("insertJobForm").action="processInsertJob/Main#";
        winPodH.onclose=function(){
            return false;
        }
    }
    
    function loadUnassignedJob(pSysId,pSelectedSystem){
        //$.LoadingOverlay("show");
        var url = "processSearchRouteJobMain?useRoute=true&istrSystemId="+pSysId+"&sys_name="+pSelectedSystem+"&jobSearchAction=search&defaultPageSize=50";
        $.get(url, function(data) {
            $("#tab1").html(data);
        });
        document.getElementById("insertJobForm").action="processInsertRouteJobMain#tab"+pSysId; 
        
        //$.LoadingOverlay("hide");
    }
    
    function loadContent(pSysId) {
        if (pSysId === "<s:property escapeHtml="true" value="selected_sys_id"/>") {
            //$.LoadingOverlay("show");
//            document.location="loadEditPageRouteJobMain";
            //$.LoadingOverlay("hide");
        } else {
            //$("#loader").show();
            //$.LoadingOverlay("show");
            var url = "loadJobContentRouteJobMain?useRoute=true&istrSystemId=" + pSysId;
    //        console.log(url);
            $.get(url, function(data) {
                $("#jobContentDiv").html(data);
                //$.LoadingOverlay("hide");
                //$("#loader").hide();
            });
        }
    }
    function loadCompleteContent(pSysId,pDuration) {
        if (pSysId === "<s:property escapeHtml="true" value="selected_sys_id"/>") {
            //$.LoadingOverlay("show");
            document.location="loadEditPageRouteJobMain";
            //$.LoadingOverlay("hide");
        } else {
            //$("#loader").show();
            //$.LoadingOverlay("show");
            var url = "loadJobContentRouteJobMain?useRoute=true&istrSystemId=" + pSysId+"&pDuration="+pDuration+"&selected_sys_id=<s:property value='selected_sys_id'/>";
//            console.log(url);
            $.get(url, function(data) {
                if(pDuration=="today"){
//                    console.log(data);
                    $("#todayDiv").html(data);
                }else{
                    $("#30dayDiv").html(data);
                }
                //$("#loader").hide();
            });
             //$.LoadingOverlay("hide");
        }
    }
    // ThoTH @ 23-Jul-2015
    function cancelInformed(pPlId) {
        document.getElementById("selectJobId_").value = pPlId;
        document.getElementById("insertJobForm").action="jobCancelInformedJobMain#";
        document.insertJobForm.submit();
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
    left: 280!important;
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
    <s:hidden name="selected_sys_id" value="%{selected_sys_id}"/>
    <s:hidden name="listJobBySystem" cssClass="listJobBySystem" value="%{listJobBySystem.size()}"/>
        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
        <div id="sysId_" style="display: none">${sysId_}</div>
        
        <s:if test='getCalcTotalSystem() > 1'>
        <div>
            <s:iterator value="userSubsystemList" var="theSubSystem" status="subSystemStatus">
                <s:if test='#theSubSystem.ID.equals(selected_sys_id)'>
                    <a href="loadEditPageRouteJobMain?sys_name=${theSubSystem.ID}" class="btn btn-primary">${theSubSystem.system_name}</a>
                </s:if><s:else>
                    <a href="loadEditPageRouteJobMain?sys_name=${theSubSystem.ID}" class="btn btn-default">${theSubSystem.system_name}</a>
                </s:else>
            </s:iterator>
        </div>
<%--            <a href="loadEditPageRouteJobMain?sys_name=QP" class="eqpBtn">
               <div class="leftBtn2 side-btn">
                    <div class="side-btn-content">eQP</div>
                </div>
            </a> --%>
        </s:if>
 
        
          
        <%--<s:if test="listJobtobeGrab.size() <= 0">--%>
        <s:if test="listJobtobeGrab.size() == -10">
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
                        <%--<s:iterator value="listJobtobeGrab" status="jobPoolStatus" var="iteratorJobPool">--%>
                            <s:hidden name="incompleteJobCnt" cssClass="incompleteJobCnt" value="%{inProgressCount}"/>
                            <li role="presentation" class="li_tab1">
                                <a href="#tab1" onclick="loadUnassignedJob('<s:property value="%{selected_sys_id}"/>','<s:property escapeHtml="true" value="%{selected_system}"/>')" aria-controls="tab1" role="tab" data-toggle="tab">
                                   Unassigned Job<small class="label pull-right bg-blue"><s:property value="%{poolCount}"/></small>
                                </a>
                            </li>
                            <li role="presentation" class="li_tab2">
                                <a href="#tab2" onclick="loadContent('<s:property value="%{selected_sys_id}"/>')" aria-controls="tab2" role="tab" data-toggle="tab">
                                   In Progress<small class="label pull-right bg-blue"><s:property value="%{inProgressCount}"/></small>
                                </a>
                            </li>
                        <%--</s:iterator>--%>
                        <li role="presentation" class="li_tab3">
                            <a href="#tab3" onclick="loadCompleteContent('COMPLETED','today')" aria-controls="tab3" role="tab" data-toggle="tab">
                                Completed Today<small class="label pull-right bg-blue"><s:property value="completedToday"/></small>
                            </a>
                        </li>
                        <!--2019.01.09 Edited by IvyL-->
                         <li role="presentation" class="li_tab4">
                            <a href="#tab4" onclick="loadCompleteContent('COMPLETED','30day')" aria-controls="tab4" role="tab" data-toggle="tab">
                                Completed Past 30 Days<small class="label pull-right bg-blue"><s:property value="%{totalCompleted7Days}"/></small>
                            </a>
                        </li>
<!--                        <li role="presentation">
                            <a href="#tab4" onclick="loadCompleteContent('COMPLETED','7day')" aria-controls="tab4" role="tab" data-toggle="tab">
                                Completed Past 7 Days<small class="label pull-right bg-green"><s:property value="%{totalCompleted7Days}"/></small>
                            </a>
                        </li>-->
                    </ul>
                    <div class="tab-content"  id="jobContentDiv">
                        <div role="tabpanel" class="tab-pane fade in" id="tab1">
                            
                        </div>
                        <div role="tabpanel" class="tab-pane fade in" id="tab2">
                            <div class="table-responsive">
                                <table class="table table-condensed table-striped myTable2" cellspacing="0" cellpadding="0" width="100%">
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
                                            <th width="25%">Application Details.</th>
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
                                                        ${iteratorJobBySystem.get('previousTaskDoer')}
                                                    </td>
<!--                                                    <td class="jobLabel" width="10%">
                                                        <s:if test="#iteratorJobBySystem.get('due_date') != null">                                    
                                                            <s:text name="date_default_datetime"><s:param value="%{#iteratorJobBySystem.get('due_date')}"/></s:text>
                                                            <s:if test="#iteratorJobBySystem.get('today') > #iteratorJobBySystem.get('due_date')">
                                                                <button type="button" class="btn btn-default btn-xs"><span class="time" style="color: red;" >Due</span></button>
                                                            </s:if>
                                                        </s:if>   
                                                    </td>-->
                                                    <td class="jobLabel" style="word-wrap: break-word" width="25%">
                                                        <%--<s:a href="#" title="Case Details" onclick="$('#caseDetail_%{#jobProgressStatus.index}').data('width', '90%'); $('#caseDetail_%{#jobProgressStatus.index}').modal('show');">--%>
                                                            <s:property escapeHtml="false" value="%{#iteratorJobBySystem.get('sta_no')}"/>
                                                        <%--</s:a>--%>
                                                    </td>
                                                    <td id="jobLabel" class="jobLabel" width="20%">
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
                                                    <td class="jobLabel" style="word-wrap: break-word" width="7%">
                                                        <!--<a href="javascript:;" onclick="loadHistory('loadUserTaskHistoryRouteJobMain?cid=<s:property value="%{#iteratorJobBySystem.get('caseId')}"/>')"  title="History"><i class="fa fa-clock-o" style="margin-left:10px;font-size:14px;"></i></a>-->
                                                        <!--<br>-->
                                                        <a href="loadDiagramPageRouteJobMain?id=<s:property value="%{#iteratorJobBySystem.get('task_id')}"/>" target="_blank" >Diagram</a>
                                                    </td>
                                                    <td class="jobLabel" style="word-wrap: break-word" width="23%">
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
        

            
            <%--<div  class="col-lg-2 " style="padding:0px;">
                  <ul class="tabs" style="padding-left: 0; margin: 0;">
                    <s:iterator value="listJobtobeGrab" status="jobPoolStatus" id="iteratorJobPool">
                        <li class="<s:if test="#jobPoolStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                            <a href="" onclick="loadContent('<s:property value="%{#iteratorJobPool.get('wf_subsystem')}"/>')" class="">
                                <div class="space" style="padding-top:10px;"><h5><strong><s:property value="%{#iteratorJobPool.get('systemname')}"/></strong> </dih5></div>
                                <div class="space" <s:if test="#iteratorJobPool.get('longcount') > 0">style="color:#DD4B39;"</s:if>>
                                    <s:text name="jobList.jobUnassign" /> : 
                                    <small class="label bg-yellow pull-right" style="font-style:normal;"><s:property value="%{#iteratorJobPool.get('longcount')}"/></small>
                                </div >
                                <div class="space" >
                                    <s:text name="jobList.jobNotComplete" /> :
                                    <small class="label bg-red pull-right"  style="font-style:normal;"><s:property value="%{#iteratorJobPool.get('ipcount')}"/></small>
                                </div >
                            </a>
                        </li>
                    </s:iterator>
                     <!--Completed Job Added by IvyL-->
                       <li class="">
                           <a href="" onclick="loadContent('COMPLETED')" class="">
                               <div class="space" style="padding-top:10px;"><h5><strong><s:text name="jobList.jobComplete" /></strong></h5></div>
                                <div class="space">
                                    <s:text name="jobList.jobToday" /> :
                                    <strong><s:property value="completedToday"/></strong>
                                   <!--<small class="label bg-white pull-right"  style="font-style:normal;"><s:property value="getListJobProcessed_total().get('longtoday')"/></small>-->
                                </div >
                                <div class="space" >
                                    <s:text name="jobList.job7DaysAgo" /> : 
                                    <strong><s:property value="completed7days"/></strong>
                                     <!--<small class="label bg-white pull-right"  style="font-style:normal;"><s:property value="getListJobProcessed_total().get('longpast7')"/></small>-->
                                </div >
                            </a>
                        </li>
                </ul> 
            </div>--%>
                                
                                
            <%--detail--%>
           <%-- <div class="col-lg-10 tableBorder" id="jobContentDiv">
                <s:if test="#iteratorJobPool.get('longcount') > 0"> 
                      <div style="padding:5px;">
                            <div id="sysId_" style="display: none">${iteratorJobPool.get('wf_subsystem')}</div>
                            <button class="btn btn-primary" type="submit"   onclick="showUnassignedJob('${iteratorJobPool.get('wf_subsystem')}');" ><i class="fa fa-send-o"></i><s:text name="jobList.jobGet"/></button>
                            <!--<s:submit type="button" cssClass="btn btn-primary fa-plus" theme="simple" value="%{getText('jobList.jobGet')}" onclick="showUnassignedJob('%{#iteratorJobPool.get('wf_subsystem')}')"/>-->
                    <!--<s:submit type="button" cssClass="defaultButton" theme="simple" value="%{getText('jobList.jobGet')}" action="loadUnassignJobSearchPageJobMain"/>-->
                    </div> 
                </s:if> 
                <s:else>
                      <div style="padding:5px;margin-top: 10px;">
                        &nbsp;
                    </div>
                </s:else>
                 <div class="table-responsive">
                    <table class="  table table-espa table-condensed table-striped" cellspacing="0" cellpadding="0" width="100%">
                        <thead>
                            <tr class="jobHeader_1">
                                <th class="  space_left_5"><s:text name="jobList.jobNo" /></th>
                                <th class=" " width="100px"><s:text name="jobList.jobDate" /></th>
                                <th class=" " width="100px"><s:text name="jobList.jobDueDate" /></th>
                                <th class=" ">eCase Ref.</th>
                                <th class=" "><s:text name="jobList.jobItem" /></th>
                                <th class=" "><s:text name="jobList.jobDetail" /></th>
                                <th class=" "> <s:text name="jobList.jobStatus" /></th>
                                <th class=" "> <s:text name="jobList.diagram" /></th>
                            </tr>
                        </thead>
                        <tbody>
                    
                    <s:if test="genListBySystem(#iteratorJobPool.get('wf_subsystem')) > 0">
                        <s:iterator value="listJobBySystem" status="jobProgressStatus" id="iteratorJobBySystem">
                            <!--<tr class="<s:if test="#jobProgressStatus.odd == true ">odd</s:if><s:else>even</s:else>">-->
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
                                    <!--<s:property value="%{#iteratorJobBySystem.get('grabbed_date')}"/>-->
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
                                    <s:a href="#" title="Case Details" onclick="$('#caseDetail_%{#jobProgressStatus.index}').data('width', '90%'); $('#caseDetail_%{#jobProgressStatus.index}').modal('show');">
                                        <s:property value="%{#iteratorJobBySystem.get('eCase_ref')}"/>
                                    </s:a>
                                </td>
                                <td id="jobLabel" class="jobLabel">
                                    <s:if test='#iteratorJobBySystem.get("jpextra1").equals("RJ")'><span style="font-weight: bold; color: red">[Rejected]</span> </s:if>  <!--ThoTH @ 14-Mar-2014-->
                                    <!-- ThoTH @ 16-Apr-2014 :: skip jobDetail.jsp -->
                                    <!-- <s:a href="loadEditPageJobDetail?istrJpId=%{#iteratorJobBySystem.get('task_id')}" title="Open Job"> -->
                                    <s:if test='#iteratorJobBySystem.get("task_status").equals("F")'>  <!-- 23-Jul-2015 -->
                                        <s:property value="%{#iteratorJobBySystem.get('wf_name')}"/>: <s:property value="%{#iteratorJobBySystem.get('task_description')}"/>
                                    </s:if><s:else>
                                        <s:if test="#iteratorJobBySystem.get('today') = #iteratorJobBySystem.get('due_date')">
                                            <a style="color:#DD4B39" href="<s:property value="%{#iteratorJobBySystem.get('actionUrl')}"/>" title="View Job" <s:if test='#iteratorJobBySystem.get("actionDesc").equals("View Case")'>target="_blank"</s:if>>
                                                <s:property value="%{#iteratorJobBySystem.get('actionDesc')}"/>
                                            </a>
                                        </s:if>
                                        <s:elseif test="#iteratorJobBySystem.get('today') > #iteratorJobBySystem.get('due_date')">
                                            <a style="color:#DD4B39" href="<s:property value="%{#iteratorJobBySystem.get('actionUrl')}"/>" title="View Job" <s:if test='#iteratorJobBySystem.get("actionDesc").equals("View Case")'>target="_blank"</s:if>>
                                                <s:property value="%{#iteratorJobBySystem.get('actionDesc')}"/>
                                            </a>
                                        </s:elseif>
                                        <s:else>
                                            <s:iterator value="#iteratorJobBySystem.get('taskAction')" status="actionUrlStatus" id="actionUrl">
                                                <s:if test="#actionUrlStatus.index > 0"><br></s:if>
                                                <a href="<s:property value="%{#actionUrl.get('actionUrl')}"/>" title="View Job" <s:if test='#actionUrl.get("actionDesc").equals("View Case")'>target="_blank"</s:if>>
                                                    <s:property value="%{#actionUrl.get('actionDesc')}"/>
                                                </a>
                                            </s:iterator>
                                        </s:else>
                                        <!--<s:a href="%{#iteratorJobBySystem.get('actionUrl')}" title="View Job">
                                            <s:property value="%{#iteratorJobBySystem.get('wf_name')}"/>: <s:property value="%{#iteratorJobBySystem.get('actionDesc')}"/>
                                        </s:a>-->
                                    </s:else>
                                </td>
                                <td class="jobLabel" style="word-wrap: break-word">
                                    <s:property value="%{#iteratorJobBySystem.get('wf_name')}"/>: <s:property escape="false" value="%{#iteratorJobBySystem.get('task_description')}"/>
                                </td>
                                <td class="jobLabel">
                                    <s:property value="getJobStatusDesc(#iteratorJobBySystem.get('task_status'))"/>
                                    <s:if test='#iteratorJobBySystem.get("task_status").equals("F")'>  <!-- 23-Jul-2015 -->
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
                        <tr class="errortxt"><td colspan="8" class="text-center"><s:text name="jobList.jobNone" /></td></tr>
                        <tr><td>&nbsp;</td></tr>
                    </s:else>    
                    </tbody>
                </table>
            </div>
        </div>  --%>       
        <s:if test="genListBySystem(#iteratorJobPool.get('wf_subsystem')) > 0">
            <s:iterator value="listJobBySystem" status="jobProgressStatus" var="iteratorJobBySystem">
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
                                                        <s:iterator value="#iteratorJobBySystem.get('applicationInProgressModel').afcLotsList" status="afcLotsStatus" var="afcLotsModel">
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
<!--        <div  class="jobSummary bg-grey"> 
            <table border="0" width="100%" >
                <tr><td>&nbsp;</td></tr>
                <tr>
                    <td colspan="15%"><s:text name="jobList.jobComplete" /></td>
                </tr>
                <tr>
                    <td width="15%"><s:text name="jobList.jobToday" /></td>
                    <td align="left" width="75%"><s:property value="getListJobProcessed_total().get('longtoday')"/></td>
                </tr>
                <tr>
                    <td width="15%"><s:text name="jobList.job7DaysAgo" /> </td>
                    <td align="left" width="75%"><s:property value="getListJobProcessed_total().get('longpast7')"/></div></td>
                </tr>
                <tr><td>&nbsp;</td></tr>
            </table>
        </div>-->
        <form id="insertJobForm" name="insertJobForm" action="processInsertRouteJobMain" method="POST">
        <%--<form id="insertJobForm" name="insertJobForm" action="processInsertJobMain">--%>
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