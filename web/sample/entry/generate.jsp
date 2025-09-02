<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" language="javascript" src="include/jquery/jquery-1.11.0.js"></script>
        <script type="text/javascript" language="javascript" src="include/jquery-ui-1.11.4/jquery-ui.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/jquery.dataTables.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/datatable/jquery.dataTables.rowReordering.js"></script>
        <link href="include/datatable/dataTables.bootstrap.min.css" rel="stylesheet"/>
        <link href="include/datatable/jquery.dataTables.min.css" rel="stylesheet"/>
        <title>Generate Entry by Model</title>
        <style>
            table.dataTable tbody td {
                padding: 0px 0px !important;
            }
        </style>
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
                                "paging": false
                            });
                            table.rowReordering();   
//                            $(".up,.down").click(function(){
//                                var $element = this;
//                                var row = $($element).parents("tr:first");
//                                alert($(this).closest('tr').prop("rowIndex"));
//                                var y = window.scrollY;
//                                var x = window.scrollX;
//                                if($(this).is('.up')){
//                                   row.insertBefore(row.prev());
//                                } 
//                                else{
//                                   row.insertAfter(row.next());
//                                }
//                                setTimeout(function(){ window.scrollTo(x, y); }, 400);
//                                
//                            });
//                            alert(message);
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
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Generate Entry by model <small>v1.0</small></h4>
                </div>
                <div class="panel-body">
                    
                    <div class="form-horizontal form-group row">
                        <label class="col-md-4 control-label">Model <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select id="selectedClass" list="registeredClassList" onchange="modelChanged()" listKey="keyData" listValue="valueData" name="selectedClass" cssClass="form-control sds-dropdown" value="" required="required"/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group row">
                        <label class="col-md-4 control-label">Project directory <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <input type="text" id='filePath' class="form-control" name="filePath" placeholder="eg C:\Projects\appName" value="<s:property value="filePath"/>" required/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group row">
                        <label class="col-md-4 control-label">Main model var name <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <input type="text" id='mainModelVarName' class="form-control" name="mainModelVarName" value="model" required/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group row">
                        <label class="col-md-4 control-label">The Application Code <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <input type="text" id='theApplicationCode_' class="form-control" name="theApplicationCode_" value="<s:property value="theApplicationCode_"/>" required/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group row">
                        <label class="col-md-4 control-label">The Application Name <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <input type="text" id='theApplicationName_' class="form-control" name="theApplicationName_" value="<s:property value="theApplicationName_"/>" required/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-5 form-horizontal form-group">
                            <label class="col-md-8 checkbox-label">Create/Update Action Class </label>
                            <div class="col-md-2 checkbox right check-success">
                                <input type="checkbox" value="Y" id="updateToProjectActionClass" name="updateToProjectActionClass" onclick="return notCreateStrutsXML();" onchange="updateActionClass();">
                                <label for="updateToProjectActionClass"></label>
                            </div>
                        </div>
                        <div class="col-md-7 form-horizontal form-group">
                            <label class="col-md-4 checkbox-label">Action Class <font class="hidden updateActionClass asterisk">*</font></label>
                            <div class="col-md-8">
                                <input class="form-control" id="createActionClass" name="createActionClass" placeholder="dir\ActionName.java eg: sample\sub_folder\SampleAction.java" value='<s:property value="createActionClass"/>'>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-5 form-horizontal form-group">
                            <label class="col-md-8 checkbox-label">Create/Update JSP </label>
                            <div class="col-md-2 checkbox right check-success">
                                <input type="checkbox" value="Y" id="updateToProjectJsp" name="updateToProjectJsp"  onclick="return notCreateStrutsXML();" onchange="updateJsp();">
                                <label for="updateToProjectJsp"></label>
                            </div>
                        </div>
                        <div class="col-md-7 form-horizontal form-group">
                            <label class="col-md-4 checkbox-label">jsp Dir <font class="hidden updateJsp asterisk">*</font></label>
                            <div class="col-md-8">
                                <input class="form-control" id="jspDir" name="jspDir" placeholder="dir\subDir" value='<s:property value="jspDir"/>'>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-5 form-horizontal form-group">
                            <label class="col-md-8 checkbox-label">Create/Update Application Record </label>
                            <div class="col-md-2 checkbox right check-success">
                                <input type="checkbox" value="Y" id="insertApplicationRecord" name="insertApplicationRecord" onclick="updateApplication()" <s:if test='insertApplicationRecord.equals("Y")'>checked</s:if> >
                                <label for="insertApplicationRecord"></label>
                            </div>
                        </div>
                        <div class="col-md-7 form-horizontal form-group">
                            <label class="col-md-4 checkbox-label">Application's Module Code <font class="hidden updateApplication asterisk">*</font></label>
                            <div class="col-md-8 ">
                                <input type="text" class="form-control" value='<s:property value="appModuleCode"/>' id="appModuleCode" name="appModuleCode" >
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 form-horizontal form-group">
                            <label class="col-md-7 checkbox-label">Create/Update struts.xml </label>
                            <div class="col-md-5 checkbox right check-success">
                                <input type="checkbox" value="Y" id="createStrutsXML" name="createStrutsXML" onchange="updateStruts();" <s:if test='createStrutsXML.equals("Y")'>checked</s:if>>
                                <label for="createStrutsXML"></label>
                            </div>
                        </div>
                        <div class="col-md-4 form-horizontal form-group">
                            <label class="col-md-7 checkbox-label">Create/Update Dynamic-Config </label>
                            <div class="col-md-5 checkbox right check-success">
                                <input type="checkbox" value="Y" id="createDynamicConfig" name="createDynamicConfig" <s:if test='createDynamicConfig.equals("Y")'>checked</s:if>>
                                <label for="createDynamicConfig"></label>
                            </div>
                        </div>
                        <div class="col-md-4 form-horizontal form-group">
                            <label class="col-md-7 checkbox-label">Create/Update package.properties </label>
                            <div class="col-md-5 checkbox right check-success">
                                <input type="checkbox" value="Y" id="updateToProject" name="updateToProject">
                                <label for="updateToProject"></label>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 form-horizontal form-group">
                            <label class="col-md-7 checkbox-label">Create/Update ModelBase</label>
                            <div class="col-md-5 checkbox right check-success">
                                <input type="checkbox" value="Y" id="createUpdateModelBase" name="createUpdateModelBase">
                                <label for="createUpdateModelBase"></label>
                            </div>
                        </div>
                        <div class="col-md-4 form-horizontal form-group">
                            <label class="col-md-7 checkbox-label">Put 1st Level Child to Tab</label>
                            <div class="col-md-5 checkbox right check-success">
                                <input type="checkbox" value="Y" id="useTab" name="useTab" <s:if test='useTab.equals("Y")'>checked</s:if> >
                                <label for="useTab"></label>
                            </div>
                        </div>
                    </div>
                            <br>
                    <div id="generatedContent">
                        
                    </div>
                    <div class="form-horizontal form-group row">
                        <label class="col-md-4 control-label">Action</label>
                        <div class="col-md-5 control-label">
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="return doGenerate();">
                                <i class="fa fa-plus"></i>Generate Now
                            </button>
                            <button type="button" class="btn" name="" id="actionName" onclick="return doView();">
                                <i class="fa fa-plus"></i>View Now
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <div id="modelChangeLoader" class=""></div>
        <div id="submitFormDiv" class="hidden"></div>
        <div id="uuDiv" class="hidden"></div>
        <div id="viewModal" class="modal fade" tabindex="-1" data-width="" data-height="" style="display: none;" data-keyboard="true">
            <div class="">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close btnc" data-dismiss="modal" aria-hidden="true">×</button>
                        <div class="title"></div>
                    </div>
                    <div class="modal-body myModalContent"></div>
                </div>
            </div>
        </div>
</body>
</html>
