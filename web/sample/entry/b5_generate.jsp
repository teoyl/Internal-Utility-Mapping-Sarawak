<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="falcon-v3.16.0/public/vendors/datatables.net-bs5/dataTables.bootstrap5.min.css" rel="stylesheet">
        <link href="falcon-v3.16.0/public/vendors/datatables.net-rowReorder/rowReorder.bootstrap5.min.css" rel="stylesheet">
        <script src="falcon-v3.16.0/public/vendors/datatables.net/jquery.dataTables.min.js"></script>
        <script src="falcon-v3.16.0/public/vendors/datatables.net-bs5/dataTables.bootstrap5.min.js"> </script>
        <script src="falcon-v3.16.0/public/vendors/datatables.net-fixedcolumns/dataTables.fixedColumns.min.js"> </script>
        <script src="falcon-v3.16.0/public/vendors/datatables.net-rowReorder/dataTables.rowReorder.min.js"> </script>
        <title>Generate Entry by Model</title>
        <script type="text/javascript">
            function fieldTypeChanged(field) {
                var fieldID = $(field).prop("id");
                fieldID = fieldID.substring(0, fieldID.length - 4);
                var setupValue = document.getElementById(fieldID+"setup").value;
                if (field.value === "ta") {
                    if (setupValue==="") {
                        document.getElementById(fieldID+"setup").value = "5;3";
                    }
                } else if (field.value === "cb") {
                    if (setupValue==="") {
                        document.getElementById(fieldID+"setup").value = "Y";
                    }
                } else if (field.value === "rd") {
                    if (setupValue==="") {
                        document.getElementById(fieldID+"setup").value = "genderOptions";
                    }
                } else if (field.value === "dd") {
                    if (setupValue==="") {
                        document.getElementById(fieldID+"setup").value = "genderOptions;keyData;valueData;pleaseSelect";
                    }
                } else if (field.value === "uu") {
                    if (setupValue==="") {
                        $("#uuDiv").load("uuSetupEntry?uuColumn="+fieldID+"&uuModelName="+$(field).attr("modelName"),
                        function (message) {
                            if (message === "Expired") {
                                <%-- it you are using itemChangeLoader, it won't come here, 
                                    it only will come here it you are loading sub-item using your on Action (*access right control needed) --%>
                                document.location = "initLogin";
                            } else {
                                document.getElementById(fieldID+"setup").value = message; 
                            }
                        });
                    }
                }
            }
            function addList(theId, listName, modelName, childName) {
                $("#"+theId).load("addChildListEntry?parentModelName="+listName+"&parentModelClass="+modelName+"&selectedClass="+childName,
                    function (message) {
                        if (message === "Expired") {
                            <%-- it you are using itemChangeLoader, it won't come here, 
                                it only will come here it you are loading sub-item using your on Action (*access right control needed) --%>
                            document.location = "initLogin";
                        } else {
//                            $("#"+theId).html(message);
                            var table = $('#'+theId+"_id").DataTable({
                                dom: 't',
                                "paging": false
                            });
                            table.rowReordering();
                        }
                    });
            }
            function modelChanged() {
                if ($("#selectedClass").val() !== "") {
                    $("#modelChangeLoader").load("modelChangedEntry?selectedClass="+$("#selectedClass").val(),
                    function (message) {
                        if (message === "Expired") {
                            <%-- it you are using itemChangeLoader, it won't come here, 
                                it only will come here it you are loading sub-item using your on Action (*access right control needed) --%>
                            document.location = "initLogin";
                        } else {
                            $("#generatedContent").html(message);
                            $("#modelChangeLoader").html("");
                            setTimeout(function(){  }, 3000);
                            var table = $('#theModel_id').DataTable({
                                dom: 't',
                                "paging": false,
                                rowReorder: true
                            });
                        }
                    });
                }
            }
            
            function doView() {
                if (validateForm_bshor('generateEntryForm', 'viewEntry')) {
                    $('.allTables').each(function (i, table) {
                        $('#'+$(table).attr("id")+' > tbody  > tr').each(function (i, row) {
                            var $row = $(row);
                                $("#"+$row.attr("hiddenIdName")+"_sorting").val((i+1)*10);
//                            $("#order_"+$row.attr("id")).val((i+1)*10);
                        });
                    });
                    offLoading = true;
                    $("#generateEntryForm").attr("target","_blank");
                    $("#generateEntryForm").attr("action","viewEntry");
                    $("#generateEntryForm").submit();
                    $("#generateEntryForm").attr("target","_self");
                } else {
                    alert("Fields marked as red are required");
                }
                return false;
            }
            
            function postView() {
                $("#viewModal").data('width', '90%');
                $('#viewModal').modal('show');
            }
            
            function doGenerate() {
                offLoading = false;
                if (validateForm_bshor('generateEntryForm', 'createEntry')) {
                    $('.allTables').each(function (i, table) {
                        $('#'+$(table).attr("id")+' > tbody  > tr').each(function (i, row) {
                            var $row = $(row);
                                $("#"+$row.attr("hiddenIdName")+"_sorting").val((i+1)*10);
//                            $("#order_"+$row.attr("id")).val((i+1)*10);
                        });
                    });
                    divSubmitForm('createEntry', 'generateEntryForm', 'submitFormDiv');
                } else {
                    alert("Fields marked as red are required");
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
            function updateApplication() {
                if($("#insertApplicationRecord").is(":checked")){
                    $(".updateApplication").removeClass("hidden");
                    $("#appModuleCode").prop("required", "required");
                } else {
                    $(".updateApplication").addClass("hidden");
                    $("#appModuleCode").removeAttr("required");
                    $("#appModuleCode").removeClass("redBorder");
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
        </script>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form name="form" id="generateEntryForm" class="" method="post">
            <div class="card">
                <div class="card-header bg-light">
                    <h4>Generate Entry by model <small>v1.0</small></h4>
                </div>
                <div class="card-body">                    
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm text-md-end">Model <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select id="selectedClass" list="registeredClassList" onchange="modelChanged()" listKey="keyData" listValue="valueData" name="selectedClass" cssClass="form-control form-control-sm sds-dropdown" value="" required="required"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm text-md-end">Project directory <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <input type="text" id='filePath' class="form-control form-control-sm" name="filePath" placeholder="eg C:\Projects\appName" value="<s:property value="filePath"/>" required/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm text-md-end">Main model var name <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <input type="text" id='mainModelVarName' class="form-control form-control-sm" name="mainModelVarName" value="model" required/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm text-md-end">The Application Code <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <input type="text" id='theApplicationCode_' class="form-control form-control-sm" name="theApplicationCode_" value="<s:property value="theApplicationCode_"/>" required/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm text-md-end">The Application Name <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <input type="text" id='theApplicationName_' class="form-control form-control-sm" name="theApplicationName_" value="<s:property value="theApplicationName_"/>" required/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <div class="col-md-5 row">
                            <label class="col-md-8 col-form-label col-form-label-sm text-md-end">Create/Update Action Class </label>
                            <div class="col-md-2">
                                <div class="form-check">
                                    <input type="checkbox" value="Y" id="updateToProjectActionClass" name="updateToProjectActionClass" onclick="return notCreateStrutsXML();" onchange="updateActionClass();" class="form-check-input">
                                    <label for="updateToProjectActionClass" class="form-check-label"></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-7 row">
                            <label class="col-md-4 col-form-label col-form-label-sm text-md-end">Action Class <font class="hidden updateActionClass asterisk">*</font></label>
                            <div class="col-md-8">
                                <input class="form-control form-control-sm" id="createActionClass" name="createActionClass" placeholder="dir\ActionName.java eg: sample\sub_folder\SampleAction.java" value='<s:property value="createActionClass"/>'>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <div class="col-md-5 row">
                            <label class="col-md-8 col-form-label col-form-label-sm text-md-end">Create/Update JSP </label>
                            <div class="col-md-2">
                                <div class="form-check">
                                    <input type="checkbox" value="Y" id="updateToProjectJsp" name="updateToProjectJsp"  onclick="return notCreateStrutsXML();" onchange="updateJsp();" class="form-check-input">
                                    <label for="updateToProjectJsp" class="form-check-label"></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-7 row">
                            <label class="col-md-4 col-form-label col-form-label-sm text-md-end">jsp Dir <font class="hidden updateJsp asterisk">*</font></label>
                            <div class="col-md-8">
                                <input class="form-control form-control-sm" id="jspDir" name="jspDir" placeholder="dir\subDir" value='<s:property value="jspDir"/>'>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <div class="col-md-5 row">
                            <label class="col-md-8 col-form-label col-form-label-sm text-md-end">Create/Update Application Record </label>
                            <div class="col-md-2">
                                <div class="form-check">
                                    <input type="checkbox" value="Y" id="insertApplicationRecord" name="insertApplicationRecord" onclick="updateApplication()" <s:if test='insertApplicationRecord.equals("Y")'>checked</s:if> class="form-check-input">
                                    <label for="insertApplicationRecord" class="form-check-label"></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-7 row">
                            <label class="col-md-4 col-form-label col-form-label-sm text-md-end">Application's Module Code <font class="hidden updateApplication asterisk">*</font></label>
                            <div class="col-md-8 ">
                                <input type="text" class="form-control form-control-sm" value='<s:property value="appModuleCode"/>' id="appModuleCode" name="appModuleCode" >
                            </div>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <div class="col-md-4 row">
                            <label class="col-md-7 col-form-label col-form-label-sm text-md-end">Create/Update struts.xml </label>
                            <div class="col-md-5">
                                <div class="form-check">
                                    <input type="checkbox" value="Y" id="createStrutsXML" name="createStrutsXML" onchange="updateStruts();" <s:if test='createStrutsXML.equals("Y")'>checked</s:if> class="form-check-input">
                                    <label for="createStrutsXML" class="form-check-label"></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 row">
                            <label class="col-md-7 col-form-label col-form-label-sm text-md-end">Create/Update Dynamic-Config </label>
                            <div class="col-md-5">
                                <div class="form-check">
                                    <input type="checkbox" value="Y" id="createDynamicConfig" name="createDynamicConfig" <s:if test='createDynamicConfig.equals("Y")'>checked</s:if> class="form-check-input">
                                    <label for="createDynamicConfig" class="form-check-label"></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 row">
                            <label class="col-md-7 col-form-label col-form-label-sm text-md-end">Create/Update package.properties </label>
                            <div class="col-md-5">
                                <div class="form-check">
                                    <input type="checkbox" value="Y" id="updateToProject" name="updateToProject" class="form-check-input">
                                    <label for="updateToProject" class="form-check-label"></label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <div class="col-md-4 row">
                            <label class="col-md-7 col-form-label col-form-label-sm text-md-end">Create/Update ModelBase</label>
                            <div class="col-md-5">
                                <div class="form-check">
                                    <input type="checkbox" value="Y" id="createUpdateModelBase" name="createUpdateModelBase" class="form-check-input">
                                    <label for="createUpdateModelBase" class="form-check-label"></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 row">
                            <label class="col-md-7 col-form-label col-form-label-sm text-md-end">Put 1st Level Child to Tab</label>
                            <div class="col-md-5">
                                <div class="form-check">
                                    <input type="checkbox" value="Y" id="useTab" name="useTab" <s:if test='useTab.equals("Y")'>checked</s:if> class="form-check-input">
                                    <label for="useTab" class="form-check-label"></label>
                                </div>
                            </div>
                        </div>
                    </div>
                            <br>
                    <div id="generatedContent">
                        
                    </div>
                    <div class="row mt-3 mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm text-md-end">Action</label>
                        <div class="col-md-5 col-form-label col-form-label-sm text-md-center">
                            <button type="button" class="btn btn-sm btn-primary mb-1" name="" id="actionName" onclick="return doGenerate();">
                                <i class="fa fa-plus"></i><span class="ms-1">Generate Now</span>
                            </button>
                            <button type="button" class="btn btn-sm btn-falcon-default mb-1" name="" id="actionName" onclick="return doView();">
                                <i class="fa fa-plus"></i><span class="ms-1">View Now</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <div id="modelChangeLoader" class=""></div>
        <div id="submitFormDiv" class="hidden"></div>
        <div id="uuDiv" class="hidden"></div>
        <div id="viewModal" class="modal fade" tabindex="-1" data-bs-keyboard="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <div class="title modal-title"></div>
                        <button type="button" class="btn-close" data-dismiss="modal" aria-hidden="true">×</button>
                    </div>
                    <div class="modal-body myModalContent"></div>
                </div>
            </div>
        </div>
</body>
</html>
