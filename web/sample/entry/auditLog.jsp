<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Generate Entry by Model</title>
        <script type="text/javascript" language="javascript" src="include/jquery/jquery-1.11.0.js"></script>
        <script type="text/javascript" language="javascript" src="include/jquery-ui-1.11.4/jquery-ui.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/jquery.dataTables.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/datatable/jquery.dataTables.rowReordering.js"></script>
        <link href="include/datatable/dataTables.bootstrap.min.css" rel="stylesheet"/>
        <link href="include/datatable/jquery.dataTables.min.css" rel="stylesheet"/>
        <!--Date and Daterange picker-->
        <link href="include/datepicker/datepicker3.css" rel="stylesheet"/>
        <link href="include/daterangepicker/daterangepicker.css" rel="stylesheet"/>
        <script type="text/javascript" language="javascript" src="include/datepicker/bootstrap-datepicker3.js"></script>
        <script type="text/javascript" language="javascript" src="include/daterangepicker/moment.js"></script>
        <script type="text/javascript" language="javascript" src="include/daterangepicker/daterangepicker.js"></script>
        <style>
            .form-control {
                margin-bottom: 1px;
            }
            table.dataTable tbody th, table.dataTable tbody td {
                padding: 1px 1px;
            }
            table.dataTable tbody th, table.dataTable tbody td .checkbox{
                padding-top: 4px;
                padding-left: 2px;
            }
            hr {
                margin-bottom: 1px;
            }
            .input-group.date .input-group-addon i {
                height: 14px;
            }
        </style>
        <script type="text/javascript">
            function addList(theId, listName, modelName, childName) {
                $("#"+theId).load("addChildListEntry?parentModelName="+listName+"&parentModelClass="+modelName+"&selectedClass="+childName,
                    function (message) {
                        if (message === "Expired") {
                            <%-- it you are using itemChangeLoader, it won't come here, 
                                it only will come here it you are loading sub-item using your on Action (*access right control needed) --%>
                            document.location = "initLogin";
                        } else {
                            $("#"+theId).html(message);
                        }
                    });
            }
            function modelSelected() {
                offLoading = false;
                if ($("#selectedClass").val() !== "") {
                    $("#modelChangeLoader").load("modelSelectedEntry?selectedClass="+$("#selectedClass").val(),
                    function (message) {
                        if (message === "Expired") {
                            <%-- it you are using itemChangeLoader, it won't come here, 
                                it only will come here it you are loading sub-item using your on Action (*access right control needed) --%>
                            document.location = "initLogin";
                        } else {
                            $("#generatedContent").html(message);
                            var table = $('#templateFieldTable').DataTable({
                                dom: 't',
                                "paging": false
                            });
                            table.rowReordering();   
                            toggleSelectAll('cbShowMe');
//                            alert(message);
//                            alert(message);
                        }
                    });
                }
            }
            
            function auditReport() {
                if (isCheckboxSelected(document.getElementsByClassName('cbShowMe'))) {
                    if ($("#logUserId").val().trim() === "" && $("#logRecordId").val().trim() === "") {
                        alert("Please User ID or Record ID to minimise the result");
                        window.scrollTo(0, 0);
                        $("#logUserId").focus();
                    } else {
                        offLoading = true;
                        $('.templateFieldRow').each(function (i, row) {
                            var $row = $(row);
                            $("#order_"+$row.attr("id")).val((i+1)*10);
                        });
                        $('#auditForm').prop("target", "_blank");
                        submitForm('auditForm', 'auditReportEntry');
                    }
                }
                return false;
            }
            
            function updateActionClass() {
                if($("#updateToProjectActionClass").is(":checked")){
                    $(".updateActionClass").removeClass("hidden");
                    $("#createActionClass").prop("required", "required");
                } else {
                    if(!$("#createStrutsXML").is(":checked")){
                        $(".updateActionClass").addClass("hidden");
                        $("#createActionClass").removeAttr("required");
                        $("#createActionClass").removeClass("redBorder");
                    }
                }
            }
            function updateJsp() {
                if($("#updateToProjectJsp").is(":checked")){
                    $(".updateJsp").removeClass("hidden");
                    $("#jspDir").prop("required", "required");
                } else {
                    if(!$("#createStrutsXML").is(":checked")){
                        $(".updateJsp").addClass("hidden");
                        $("#jspDir").removeAttr("required");
                        $("#jspDir").removeClass("redBorder");
                    }
                }
            }
            function updateStruts() {
                if($("#createStrutsXML").is(":checked")){
                    $(".updateJsp").removeClass("hidden");
                    $(".updateActionClass").removeClass("hidden");
                    $("#jspDir").prop("required", "required");
                    $("#createActionClass").prop("required", "required");
                } else {
                    if(!$("#updateToProjectActionClass").is(":checked")){
                        $(".updateActionClass").addClass("hidden");
                        $("#createActionClass").removeAttr("required");
                        $("#createActionClass").removeClass("redBorder");
                    }
                    if(!$("#updateToProjectJsp").is(":checked")){
                        $(".updateJsp").addClass("hidden");
                        $("#jspDir").removeAttr("required");
                        $("#jspDir").removeClass("redBorder");
                    }
                }
            }
            function notCreateStrutsXML() {
                return !$("#createStrutsXML").is(":checked");
            }
            $(document).ready(function () {
                initDateCheck();
            });
        </script>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form name="form" id="auditForm" class="" method="post">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Audit Log Report <small>v1.0</small></h4>
                </div>
                <div class="panel-body">
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Model <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select id="selectedClass" list="registeredClassList" onchange="modelSelected()" listKey="keyData" listValue="valueData" name="selectedClass" cssClass="form-control sds-dropdown" value="" required="required"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-3">
                            <div class="row form-horizontal form-group">
                                <label class="col-md-4 control-label">User ID</label>
                                <div class="col-md-8">
                                    <input type="text" id='logUserId' class="form-control" name="logUserId" value=""/>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="row form-horizontal form-group">
                                <label class="col-md-4 control-label">Record ID</label>
                                <div class="col-md-8">
                                    <input type="text" id='logRecordId' class="form-control" name="logRecordId" value=""/>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-5">
                            <div class="row form-horizontal form-group">
                                <label class="col-md-4 control-label">Audit Date</label>
                                <div class="col-md-8">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="input-group date">
                                                <input class="form-control DateCheck SameDate" id="datepickerFrom" name="logDateFrom" value=''>
                                                <div class="input-group-addon">
                                                    <i class="fa fa-calendar"></i>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="input-group date">
                                                <input class="form-control DateCheck SameDate" id="datepickerTo" name="logDateTo" value=''>
                                                <div class="input-group-addon">
                                                    <i class="fa fa-calendar"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class='row'>
                        <div class="col-md-2 col-md-offset-5">
                            <button type='button' class='btn btn-primary' name='auditReportEntry' id='actionName' onclick='auditReport(); return false;' >
                                <i class='fa fa-plus'></i>Generate Now
                            </button>
                        </div>
                    </div>
                    <div id="generatedContent">
                        
                    </div>
                </div>
            </div>
        </form>
        <div id="modelChangeLoader" class="hidden"></div>
        <div id="submitFormDiv" class="hidden"></div>
</body>
</html>
