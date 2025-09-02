<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Generate Entry by Model</title>
        
        <link href="falcon-v3.16.0/public/vendors/datatables.net-bs5/dataTables.bootstrap5.min.css" rel="stylesheet">
        <script src="falcon-v3.16.0/public/vendors/datatables.net/jquery.dataTables.min.js"></script>
        <script src="falcon-v3.16.0/public/vendors/datatables.net-bs5/dataTables.bootstrap5.min.js"></script>
        <script src="falcon-v3.16.0/public/vendors/datatables.net-fixedcolumns/dataTables.fixedColumns.min.js"></script>
        
        <!--<script type="text/javascript" language="javascript" src="include/jquery/jquery-1.11.0.js"></script>-->
        <!--<script type="text/javascript" language="javascript" src="include/jquery-ui-1.11.4/jquery-ui.min.js"></script>-->
        <!--<script type="text/javascript" language="javascript" src="include/print/jquery.dataTables.min.js"></script>-->
        <!--<script type="text/javascript" language="javascript" src="include/datatable/jquery.dataTables.rowReordering.js"></script>-->
        <!--<link href="include/datatable/dataTables.bootstrap.min.css" rel="stylesheet"/>-->
        <!--<link href="include/datatable/jquery.dataTables.min.css" rel="stylesheet"/>-->
        <!--<link href="include/datatable/rowReorder.dataTables.min.css" rel="stylesheet"/>-->
        
<!--        <style>
            .form-control form-control-sm {
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
        </style>-->
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
                                rowReorder: true,
                                "paging": false,
//                                dom: "<'row mx-0'<'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-3'><'col-auto px-3'p>>"
                            });
                            
//                            table.rowReordering();   
                            toggleSelectAll('cbShowMe');
//                            alert(message);
//                            alert(message);
                        }
                    });
                }
            }
            
            function auditReport() {
                if (isCheckboxSelected(document.getElementsByClassName('cbShowMe'))) {
//                    if ($("#logUserId").val().trim() === "" && $("#logRecordId").val().trim() === "") {
//                        alert("Please enter User ID or Record ID to minimise the result");
//                        window.scrollTo(0, 0);
//                        $("#logUserId").focus();
//                    } else {
                        offLoading = true;
                        $('.templateFieldRow').each(function (i, row) {
                            var $row = $(row);
                            $("#order_"+$row.attr("id")).val((i+1)*10);
                        });
                        $('#auditForm').prop("target", "_blank");
                        submitForm('auditForm', 'generateAuditLog');
//                    }
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
                initDateRange();
            });
        </script>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form name="form" id="auditForm" class="" method="post">
            <div class="card">
                <div class="card-header bg-light">
                    <h5>Audit Log Report <small class="fw-normal text-600">v1.0</small></h5>
                </div>
                <div class="card-body">
                    <div class="row mb-2">
                        <label class="col-md-4 col-form-label col-form-label-sm text-md-end">Model <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select id="selectedClass" list="registeredClassList" onchange="modelSelected()" listKey="keyData" listValue="valueData" name="selectedClass" cssClass="form-control form-control-sm sds-dropdown" value="" required="required"/>
                        </div>
                    </div>
                    <div class="row mb-2">
                        <div class="col-md-3">
                            <div class="row mb-1">
                                <label class="col-md-4 col-form-label col-form-label-sm">User ID</label>
                                <div class="col-md-8">
                                    <input type="text" id='logUserId' class="form-control form-control-sm" name="logUserId" value=""/>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="row mb-1">
                                <label class="col-md-4 col-form-label col-form-label-sm">Record ID</label>
                                <div class="col-md-8">
                                    <input type="text" id='logRecordId' class="form-control form-control-sm" name="logRecordId" value=""/>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-5">
                            <div class="row mb-1">
                                <label class="col-md-4 col-form-label col-form-label-sm">Audit Date</label>
                                <div class="col-md-8">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="input-group input-group-sm splitDateRangePicker" data-name="logDate" id="logDateFrom_dsrf">
                                                <input class="form-control form-control-sm" id="logDateFrom" name="logDateFrom" value='' data-input />
                                                <div class="input-group-text input-button" data-toggle>
                                                    <i class="far fa-calendar-alt"></i>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="input-group input-group-sm" id="logDateTo_dsrf">
                                                <input class="form-control form-control-sm" id="logDateTo" name="logDateTo" value='' data-input />
                                                <div class="input-group-text input-button" data-toggle>
                                                    <i class="far fa-calendar-alt"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class='row mb-2'>
                        <div class="col text-center">
                            <button type='button' class='btn btn-primary btn-sm' name='generateAuditLog' id='actionName' onclick='auditReport(); return false;' >
                                <i class='fa fa-plus'></i><span class="ms-1">Generate Now</span>
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
