<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
    
    <title><s:text name="jobList.title" /></title>
    <s:head />
    <script language="javascript" nonce="r4DjhKbfO5ry">
        $(document).ready(function() {
            $('.unassigned-tab').on('click', function (e) {
                loadUnassignedJob('<s:property escapeHtml="true" escapeJavaScript="true" value="%{selected_sys_id}"/>','<s:property escapeHtml="true" escapeJavaScript="true" value="%{selected_system}"/>');
            });

            $('.in-progress-tab').on('click', function (e) {
                loadContent('<s:property escapeHtml="true" escapeJavaScript="true" value="%{selected_sys_id}"/>');
            });

            //preload content upon document ready
            loadCompleteContent('COMPLETED','today');
            loadCompleteContent('COMPLETED','30day');
            
            $('.completed-today-tab').on('click', function (e) {
                loadCompleteContent('COMPLETED','today');
            });

            $('.completed-30-tab').on('click', function (e) {
                loadCompleteContent('COMPLETED','30day');
            });

            if($('.incompleteJobCnt').val() > 0){
                $('.myTable2').DataTable({
                    "columnDefs" : [{"targets":1, "type":"date"}]
                });
            }
            $('#unassignedTaskTable').DataTable();
            $('#pendingTaskTable').DataTable();
            var pageFrom = $('.pageFrom').val();
            var selected_tab = "#" + $('.selected_tab').val();
            if(pageFrom == "welcome"){ //Unassigned job click from mainpage
                if(selected_tab == ""){ 
                   $('.li_tab1').addClass("active");
                    $('#tab1').addClass("active");
                    loadUnassignedJob('<s:property escapeHtml="true" escapeJavaScript="true" value="selected_sys_id"/>','<s:property escapeHtml="true" escapeJavaScript="true" value="selected_sys_id"/>');
                }else{
                    $('.li_'+selected_tab).addClass("active");
                    $('#'+selected_tab).addClass("active");
                }
            }else{ //my job click from mainpage
                if(selected_tab === ""){
                    $('.li_tab2').addClass("active");

                    $('#tab2').addClass("active");
                     $('.selected_tab').val("#tab2");
                }else{
                    selected_tab= selected_tab.substring(1);//serene @7/10/2021
                    $('.li_'+selected_tab).addClass("active");
                    $('.a_'+selected_tab).addClass("active");
                    $('#'+selected_tab).addClass("active");
                    $('#'+selected_tab).addClass("show");//serene @7/10/2021
                    if(selected_tab === 'tab1'){
                        loadUnassignedJob('<s:property escapeHtml="true" escapeJavaScript="true" value="selected_sys_id"/>','<s:property escapeHtml="true" escapeJavaScript="true" value="selected_sys_id"/>');
                    }else{
                        loadContent('<s:property value="%{selected_sys_id}"/>');

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
            var url = "processSearchRouteJobMain?useRoute=true&istrSystemId="+pSysId+"&sys_name="+pSelectedSystem+"&jobSearchAction=search&defaultPageSize=50";
            $.get(url, function(data) {
                $("#tab1").html(data);
            });
            document.getElementById("insertJobForm").action="processInsertRouteJobMain#tab"+pSysId; 

        }

        function loadContent(pSysId) {
            if (pSysId === "<s:property escapeHtml="true" escapeJavaScript="true" value="selected_sys_id"/>") {
            } else {
                var url = "loadJobContentRouteJobMain?useRoute=true&istrSystemId=" + pSysId;
                $.get(url, function(data) {
                    $("#jobContentDiv").html(data);
                });
            }
        }
        function loadCompleteContent(pSysId,pDuration) {
            if (pSysId === "<s:property escapeHtml="true" escapeJavaScript="true" value="selected_sys_id"/>") {
                document.location="loadEditPageRouteJobMain";
            } else {
                var url = "loadJobContentRouteJobMain?useRoute=true&istrSystemId=" + pSysId+"&pDuration="+pDuration+"&selected_sys_id=<s:property escapeHtml="true" value='selected_sys_id'/>";
                $.get(url, function(data) {
                    if(pDuration=="today"){
                        $("#todayDiv").html(data);
                    }else{
                        $("#30dayDiv").html(data);
                    }
                });
            }
        }
        // ThoTH @ 23-Jul-2015
        function cancelInformed(pPlId) {
            document.getElementById("selectJobId_").value = pPlId;
            document.getElementById("insertJobForm").action="jobCancelInformedJobMain#";
            document.insertJobForm.submit();
        }


    </script>
    <link rel="stylesheet" href="include/route/styles.css" />
</head>
<body>

<div id="myhash"></div>
<style nonce="EuTVqS192VKl">
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
<s:hidden name="selected_tab" cssClass="selected_tab" />
<s:hidden name="pageFrom" cssClass="pageFrom" value="%{pageFrom}"/>
<s:hidden name="selected_system" cssClass="selected_system" value="%{selected_system}"/>
<s:hidden name="selected_sys_id" value="%{selected_sys_id}"/>
<s:hidden name="listJobBySystem" cssClass="listJobBySystem" value="%{listJobBySystem.size()}"/>
        
<div id="sysId_" class="hide-disp">${sysId_}</div>
<div class="card mb-3">
    <div class="card-header border-bottom">
        <div class="col-auto align-self-center">
            <h3 class="mb-0">
                <s:text name="jobList.title"/> 
            </h3>
        </div>
    </div>
    <div class="card-body pt-0">
        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
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
        </s:if>
        <br/><br/>
          
        <s:if test="listJobtobeGrab.size() == -10">
            <div class="row">
                <div class="errortxt col-xs-12"><%--You are not been assigned to any Workflow.--%>There is NO job for you at this moment.</div>
            </div>
        </s:if>
        <s:else>
            <ul class="nav nav-tabs nav-justified" role="tablist" id="myTab">
                <s:hidden name="incompleteJobCnt" cssClass="incompleteJobCnt" value="%{inProgressCount}"/>
                <li role="presentation" class="li_tab1 nav-item">
                    <a href="#tab1" aria-controls="tab1" role="tab" data-bs-toggle="tab" class="nav-link a_tab1 unassigned-tab" id="li_tab1">
                        Unassigned Job &nbsp;&nbsp;<span class="badge badge-info fw-bold "><s:property value="%{poolCount}"/> </span> 
                    </a>
                </li>
                <li role="presentation" class=" li_tab2 nav-item">
                    <a href="#tab2" aria-controls="tab2" role="tab" data-bs-toggle="tab" class="nav-link  a_tab2 in-progress-tab"   id="li_tab2">
                        In Progress&nbsp;&nbsp;<span class="badge badge-info fw-bold "><s:property value="%{inProgressCount}"/> </span> 
                    </a>
                </li>
                <li role="presentation" class="li_tab3 nav-item">
                    <a href="#tab3" aria-controls="tab3" role="tab" data-bs-toggle="tab" class="nav-link a_tab3 completed-today-tab" id="li_tab3">
                        Completed Today&nbsp;&nbsp;<span class="badge badge-info fw-bold "><s:property value="%{completedToday}"/> </span> 
                    </a>
                </li>
                <!--2019.01.09 Edited by IvyL-->
                 <li role="presentation" class="li_tab4 nav-item">
                    <a href="#tab4" aria-controls="tab4" role="tab" data-bs-toggle="tab" class="nav-link a_tab4 completed-30-tab" id="li_tab4">
                        Completed (30 Days) &nbsp;&nbsp;<span class="badge badge-info fw-bold "><s:property value="%{totalCompleted7Days}"/> </span>
                    </a>
                </li>
            </ul>
            <div class="tab-content"  id="jobContentDiv">
                <div role="tabpanel" class="tab-pane fade in" id="tab1">
                </div>
                <div role="tabpanel" class="tab-pane fade in" id="tab2">
                    <jsp:include page="b5_jobInProgress.jsp"></jsp:include> 
                </div>
                <div role="tabpanel" class="tab-pane fade in" id="tab3">
                    <div id="todayDiv"></div>
                </div>
                <div role="tabpanel" class="tab-pane fade in" id="tab4">
                    <div id="30dayDiv"></div>
                </div>                
            </div>
        </s:else>

        <form id="insertJobForm" name="insertJobForm" action="processInsertRouteJobMain" method="POST">
            <s:hidden theme="simple" name="selectJobId_" value=""/>
            <s:hidden theme="simple" name="pod_remarks" value=""/>
            <!--sereneChye @ 24/9/2014-->
            <s:hidden theme="simple" id="sysId_" name="sysId_" value=""/>
        </form>
        <div id="submissionHistory" class="modal fade hide-disp" tabindex="-1" data-keyboard="false" data-backdrop="static">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header"></div>
                    <div class="modal-body"><button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                    </div>
                </div>
            </div>
        </div>
                        
    </div>
</div>
</body>
</html>