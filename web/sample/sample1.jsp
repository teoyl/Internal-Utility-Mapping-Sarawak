<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>File = sample1.jsp</title>
        <script src="uppy/v1.15.0/uppy.min.js"></script>
        <script type="text/javascript">
            var uploadingCount = 0;
            function callValidateForm() {
                if (!validateForm_bshor('sampleFormId')) {
                    showErrors(document.getElementById('sampleFormId'));
                }
                $("#sampleFormId .DateRange").each(function(){
                    if ($(this).attr("id").endsWith("From")) {
                        if ($("#"+$(this).attr("id").substring(0, $(this).attr("id").length - 4)+"To") !== undefined) {
                            alert('found to = ' + $(this).attr("id").substring(0, $(this).attr("id").length - 4)+"To");
                        }
                    }
                });
            }
            function loadSmartXChange(loadWhat) {
                $("#loadWhat").val(loadWhat);
                divSubmitForm("loadSmartXChangeSample", "form2", "smartXchangeLoader", "setSmartXChangeData");
            }
            function setSmartXChangeData() {
                $("#smartXChangeData").val($("#smartXchangeLoader").html());
            }
//            function callMe() {
//                alert('callMe triggered');
//            }
            function callMe(idx) {
                alert('callMe triggered, idx = ' + idx);
            }
            function newItemsSelect2() {
                $(".newItems").select2();
            }
            function doAddItemRefreshScroll() {
                $("#addItemRefreshScroll").val("Y");
                submitForm("sampleFormId", "addItemRefreshScrollToSample");
            }
            function addItemRefresh() {
                submitForm("sampleFormId", "addItemRefreshSample");
            }
            function addItem() {
                divSubmitForm("addItemSample", "sampleFormId", "itemListDiv", "newItemsSelect2");
            }
            function itemChange() {
                $("#itemChangeLoader").load("itemChangeLoader?itemCate=Image&itemValue="+$("#divId").val(),
//                $("#itemChangeLoader").load("itemChangeLoader?itemCate=DivDis&itemValue="+$("#divId").val(),
                    function (message) {
                        if (message === "Expired") {
                            <%-- it you are using itemChangeLoader, it won't come here, 
                                it only will come here it you are loading sub-item using your on Action (*access right control needed) --%>
                            document.location = "initLogin";
                        } else {
                            $("#disDiv").html(message);
//                            $("#itemChangeLoader").html(""); <%-- clear the loader's content --%>
//                            $("#itemChangeItem").attr("name", "disId");
//                            $("#itemChangeItem").addClass("form-control sds-dropdown");
//                            $("#itemChangeItem").attr("id", "disId"); <%-- change the id at last line --%>
//                            $("#disId").select2(); <%-- use new id to reload select2 if it is a select2 dropdown --%>
                        }
                    });
            }
            
            $(document).ready(function () {
            <s:if test="workflowError != null">
                    scroll_to_ID("workflowBlock");
            </s:if>
                <s:if test='addItemRefreshScroll >= 0'>
                        scroll_to_ID("divId<s:property value="addItemRefreshScroll"/>");
                </s:if>
                initDateRange();
                /*
                $('#form2').on('submit', function(e) {
//                    e.preventDefault();
                    $.ajax({
                        url : $(this).attr('action')//,
//                        xhrFields: { withCredentials: true }//,
//                        success: function (data) {
//                            $("#form_output").html(data);
//                        },
//                        error: function (jXHR, textStatus, errorThrown) {
//                            
//                        }
                    });
                });
                $('#form2').attr("action", "http://10.17.101.219:8080/forNewProject/itemChangeLoader?itemCate=DivDis&itemValue=1");
                $('#form2').submit();*/
//                divSubmitForm("", "sampleFormId", "itemChangeLoader");
//                $("#itemChangeLoader").load("http://10.17.101.219:8080/forNewProject/itemChangeLoader?itemCate=DivDis&itemValue=");
//                $("#alertDiv").find('.title').html('<h3 class="title-alert">Alert</h3>');
//                $("#alertDiv").find('.myModalContent').html("sample message");
//                $('#alertDiv').modal('show');

//                itemChange();
                /*
                $('.DateRange').datepicker({
                    autoclose: true,
                    format: "dd/mm/yyyy",
                }).on('changeDate', function(){
                    if ($(this).attr("id").endsWith("From") || $(this).attr("id").endsWith("Start")) {
                        var ph1, ph2, toOrEnd, fromLength;
                        if ($(this).attr("id").endsWith("From")) {
                            ph1 = '<s:text name="common.from"/>';
                            ph2 = '<s:text name="common.to"/>';
                            toOrEnd = 'To';
                            fromLength = 4;
                        } else {
                            ph1 = '<s:text name="common.start"/>';
                            ph2 = '<s:text name="common.end"/>';
                            toOrEnd = 'End';
                            fromLength = 5;
                        }
                        var to = $(this).attr("id").substring(0, $(this).attr("id").length - fromLength)+toOrEnd;
                        alert("attr placeholder = " + $(this).attr("placeholder"));
                        alert("prop placeholder = " + $(this).prop("placeholder"));
                        if ($("#"+to) !== undefined) {
                            var startDate = $(this).val();
                            startDate = startDate.substring(3, 5) +"/"+ startDate.substring(0, 2) + "/" + startDate.substring(6);
                            $('#'+to).datepicker('setStartDate', new Date(startDate));
                        }
                    } else if ($(this).attr("id").endsWith("To")) {
                        var from = $(this).attr("id").substring(0, $(this).attr("id").length - 2)+"From";
                        if ($("#"+from) !== undefined) {
                            var endDate = $(this).val();
                            endDate = endDate.substring(3, 5) +"/"+ endDate.substring(0, 2) + "/" + endDate.substring(6);
                            $('#'+from).datepicker('setEndDate', new Date(endDate));
                        }
                    }
                    // set the "toDate" start to not be later than "fromDate" ends:
                }); */
                $("#inputId").inputFilter(function(value) {
//                    return /^\d*$/.test(value);    // Allow digits only, using a RegExp
                    return /^-?\d*[.,]?\d{0,1}$/.test(value)&&(value === "" || parseFloat(value) <= 500); 
                });
            });
            function triggerWorkflow() {
                var workflowAction = $("#workflowAction").val();
                var error = false;
                if (workflowAction === "grab_task" || workflowAction === "complete_task") {
                    if ($("#wfActivityCode").val().trim() === "") {
                        alert("Activity Code is required");
                        error = true;
                    }
                }
                if (!error) {
                    submitForm("sampleFormId", "doWorkflowActionSample");
                } else {
                    return false;
                }
            }
        </script>
        <link rel="stylesheet" href="uppy/v1.15.0/uppy.min.css">
        <style>
            button .fa-ns {
                margin-right: 0px;
            }
            .uppy-DragDrop-label {
                font-size: 0.9em;
                max-width: 100%;
            }
            .uppy-DragDrop-arrow{
                width:0px;
                height:0px;
                margin-bottom:0px;
            }
            .uppy-DragDrop-inner{
                padding: 0px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form name="form" id="sampleFormId" action="loadSample" method="post">
            This is "sample1.jsp"
            <s:hidden name="addItemRefreshScroll" value="N" id="addItemRefreshScroll"/>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Page Title <small>Edit</small></h4>
                </div>
                <div class="panel-body">
                    
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">File Uploading <font class="asterisk">*</font></label>
                        <div class="col-md-5" style="min-height: 28px">
                            <s:include value="/base/uppyIncludeSingleFile.jsp">
                                <s:param name="uploadUrl_">uploadUppy</s:param>
                                <s:param name="uppyFieldName_">file1</s:param>
                                <s:param name="uppyHiddenName_">yourModel.drDocRepoModel.ID</s:param>
                                <s:param name="hideArrow">Y</s:param>
                                <s:param name="uppyFileList" value="testList"/>
                                <s:param name="drAppCode_">uppySample2_file1</s:param>
                                <s:param name="uploadParams">uploadRecordId_=<s:property value="%{yourModel.ID}"/>&antiCsrf=<s:property value="%{#session.antiCsrf}"/></s:param>
                                <s:param name="uploadedFileName"><s:property value="yourModel.drDocRepoModel.dr_doc_name"/></s:param>
                                <s:param name="uploadedFileId"><s:property value="yourModel.drDocRepoModel.ID"/></s:param>
                                <s:param name="theRecordId"><s:property value="yourModel.ID"/></s:param>
                                <s:param name="displayAsThumbnail">N</s:param>
                                <%--<s:param name="delBtnMarginTop">6px</s:param>--%>
                            </s:include>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 radio-label">Radio Button 2 <font class="asterisk">*</font></label>
                        <div class="col-md-5 radio radio-inline radio-success">
                            <input value="male" name="optionyes" id="male" type="radio" required>
                            <label for="male">Male</label>
                            <input value="female" name="optionyes" id="female" type="radio" required>
                            <label for="female">Female</label>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Select Year <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <select name="dropdown" id="dropdown" class="form-control sds-dropdown" required>
                                <option value="">-- Please Select ---</option>
                                <option value="2012">2012</option>
                                <option value="2013">2013</option>
                                <option value="2014">2014</option>
                                <option value="2015">2015</option>
                                <option value="2016" selected="selected">2016</option>
                            </select>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Input <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <input type="text" id='inputId' class="form-control" name="input" value="" required/>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Email (Validation) <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <input type="text" id='emailValidateId' class="form-control EmailCheck" lbl="Email (Validation)" name="emailValidateId" value="" required/>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 checkbox-label">Checkbox</label>
                        <div class="col-md-5 checkbox right check-success">
                            <input type="checkbox" value="1" id="checkbox1" required>
                            <label for="checkbox1"></label>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Sample Textarea</label>
                        <div class="col-md-5 ta_div">
                            <textarea name="ta" id="ta" class="form-control taCount" maxlength="100"></textarea>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Lookup Module Type filter</label>
                        <div class="col-md-5">
                            <s:select list="filterModuleTypeOptions" listKey="keyData" listValue="valueData" name="module_type" id="module_type" class="form-control sds-dropdown"/>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Sample Module Lookup<font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <div class="input-group">
                                <input type="text" class="form-control" id="module_code" required>
                                <span class="input-group-addon" onclick="lookupModal($('#module_code'), 
                                    'modalLookup?lookFor=module_code,module_name,module_descs&writeTo=module_code,module_id,the_module_descs&lookup=useSetup_Module&displayedColumns=module_code,module_name,module_type&filterBy=module_type', '', 'callMe::=0_1')">
                                    <i class="fa fa-search"></i>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Selected Lookup Information<font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <input type="text" class="form-control" id="module_id" required>
                            <label id="the_module_descs"></label>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Date Picker<font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="input-group date">
                                        <input class="form-control DateRange SameDate" id="datepickerStart" name="datepickerFrom" value='' required>
                                        <div class="input-group-addon">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="input-group date">
                                        <input class="form-control DateRange SameDate" limit="20/01/2021" id="datepickerEnd" name="datepickerTo" value='' required>
                                        <div class="input-group-addon">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Date Range Picker<font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <div class="input-group date">
                                <div class="input-group-addon">
                                    <i class="fa fa-calendar"></i>
                                </div>
                                <input class="form-control" id="drPicker" name="drPicker" value='' readonly required>
                                <div class="input-group-addon">
                                    <i class="fa fa-times" id="cleardrPicker" ></i>
                                </div>

                                <input class="form-control hidden" id="drPickerFrom" name="drPickerFrom" value=''>
                                <input class="form-control hidden" id="drPickerTo" name="drPickerTo" value=''>
                                <script type="text/javascript">
                                    registerDateRangePicker('drPicker');
                                </script>
                            </div>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Division <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select id="divId" list="divisionList" onchange="itemChange()" listKey="keyData" listValue="valueData" name="divId" cssClass="form-control sds-dropdown" value="%{divValue}"/>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">District <font class="asterisk">*</font></label>
                        <div class="col-md-5" id="disDiv">
                            <s:select id="disId" list="districtList" listKey="keyData" listValue="valueData" name="disId" cssClass="form-control sds-dropdown" value="%{disValue}"/>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Current Datetime</label>
                        <div class="col-md-5 control-label">
                            <s:property value="currentDate_str"/>
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="return addItem();">
                                <i class="fa fa-plus"></i>Add Item
                            </button>
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="return addItemRefresh();">
                                <i class="fa fa-plus"></i>Add Item(Refresh)
                            </button>
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="return doAddItemRefreshScroll();">
                                <i class="fa fa-plus"></i>Add Item(Refresh + scroll)
                            </button>
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="window.scrollTo(0, 1000); return false;">
                                <i class="fa fa-plus"></i>scroll
                            </button>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Item List</label>
                        <div class="col-md-5" id="itemListDiv">
                            <jsp:include page="/sample/itemList.jsp"/>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label"></label>
                        <div class="col-md-5">
                            <a class="btn btn-default" href="javascript:;"><i class="fa fa-arrow-left"></i>Back </a>
                            <button type="submit" class="btn btn-primary" name="action:actionName" id="actionName" onclick="">
                                <i class="fa fa-save"></i>Save 
                            </button>
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="return callValidateForm();">
                                <i class="fa fa-save"></i>Save-js
                            </button>
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="this.form.submit();">
                                <i class="fa fa-save"></i>Test
                            </button>
                            <button type="submit" class="btn btn-icon-only btn-danger" name="action:actionName" id="actionName" onclick="">
                                <i class="fa fa-save"></i>
                            </button>
                            <div class="btn-group">
                                <button type="button" class="btn btn-default   block-xs dropdown-toggle" data-toggle="dropdown" aria-expanded="false" aria-haspopup="true"><i class="fa fa-square-o"></i> Multi-Action<i class="fa fa-caret-down"></i></button>
                                <ul class="dropdown-menu sds-dropdown-menu">
                                    <li><a href="#"><i class="fa fa-save"></i>Action 1</a></li>
                                    <li><a href="#"><i class="fa fa-save"></i>Action 2</a></li>
                                    <li><a href="#"><i class="fa fa-save"></i>Action 3</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Listing using dynamic-config setup (<s:if test="showListing"><a href="loadSample"><font color="orange">Hide</font></a></s:if><s:else><a href="loadSample?showListing=true"><font color="orange">Show</font></a></s:else>)</h4>
                </div>
                <div class="panel-body">
                    <s:if test="showListing">
                    <%--<jsp:include page="/sample/da_applicationList.jsp"/>--%>
                    <jsp:include page="/pages/base/da_dynamicList.jsp"/>
                    </s:if>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Test load data using SmartXChange API</h4>
                </div>
                <div class="panel-body">
                    <div class="row form-horizontal form-group">
                        <div class="col-md-12">
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="loadSmartXChange('token')">
                                <i class="fa fa-binoculars"></i>Load Token
                            </button>
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="loadSmartXChange('getData')">
                                <i class="fa fa-binoculars"></i>Load Get
                            </button>
                        </div>
                        <div class="col-md-12">
                            <s:textarea cssClass="form-control" id="smartXChangeData"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-default" id="workflowBlock">
                <div class="panel-heading">
                    <h4>ROUTE Workflow</h4>
                </div>
                <s:property value="workflowError"/>
                <div class="panel-body">
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Workflow Action</label>
                        <div class="col-md-5" id="disDiv">
                            <s:select list="workflowActionOptions" listKey="keyData" listValue="valueData" name="workflowAction" id="workflowAction" class="form-control sds-dropdown"/>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Case ID</label>
                        <div class="col-md-5" id="disDiv">
                            <s:textfield cssClass="form-control" name="wfCaseId" id="wfCaseId"/>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Workflow/Activity Code</label>
                        <div class="col-md-5" id="disDiv">
                            <s:textarea cssClass="form-control" name="wfCode" id="wfCode"/>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label">Workflow Parameter</label>
                        <div class="col-md-5" id="disDiv">
                            <s:textarea cssClass="form-control" name="workflowParam" id="workflowParam"/>
                        </div>
                    </div>
                    <div>
                        <div class="col-md-12">
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="return triggerWorkflow();">
                                <i class="fa fa-binoculars"></i>Perform Workflow Action
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Using Theme <small>bshor</small></h4>
                </div>
                <div class="panel-body">
                    <s:textfield name="text_bshor" cssClass="form-control" theme="bshor" label="Textfield" required="required"/>
                    <s:checkbox name="checkbox_bshor" theme="bshor" label="Checkbox" required="required"/>
                    <s:radio list="sampleList" theme="bshor" listKey="keyData" listValue="valueData" name="radio_bshor" label="Radio" required="required"/>
                    <s:select list="sampleList" theme="bshor" listKey="keyData" listValue="valueData" name="select_bshor" id="select_bshor" label="Select" cssClass="form-control sds-dropdown" required="required"/>
                    <s:textarea rows="3" name="textarea" cssClass="form-control" theme="bshor" label="Textarea" required="required"/>
                </div>
            </div>
            
<%--            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Show Image <small>ftp</small></h4>
                    <div class="panel-body">
                        <img src=" <s:url action='ftpImageSample' />" />
                    </div>
                </div>
            </div>--%>
        </form>
        <form name="form" id="form2" action="" method="post">
            <s:hidden id="loadWhat" name="loadWhat"/>
        </form>
            

    <div id="itemChangeLoader" class="hidden">
    </div>
    <div id="smartXchangeLoader" class="hidden">
    </div>
<%-- <form id="sampleForm" action="loadSample" method="post">
     <div class="panel panel-default">
         <div class="panel-heading">
             <h5>PAGE TITLE</h5>
         </div>
         <div class="panel-body">
             <div class="row clearfix form">
                 <div class="col-md-3">
                     <div class="form-group form-float">
                         <div class="form-line">
                             <input type="text" class="form-control" maxlength="10" minlength="3" required>
                             <label class="form-label">Label 1 <font class="asterisk">*</font></label>
                         </div>
                     </div>
                 </div>
                 <div class="col-md-3 select-dropdown">
                     <label class="select-label">Label 2</label>
                     <select class="form-control show-tick" data-live-search="true">
                         <option value="">--Please Select--</option>
                         <option value="A">A</option>
                         <option value="B">B</option>
                         <option value="c">C</option>
                     </select>
                 </div>
                 <div class="col-md-3 select-dropdown">
                     <label class="select-label">Label 3</label>
                     <select class="form-control show-tick" data-live-search="true">
                         <option value="">--Please Select--</option>
                         <option value="A">A</option>
                         <option value="B">B</option>
                         <option value="C">C</option>
                     </select>
                 </div>
                 <div class="col-md-3">
                     <div class="form-group form-float">
                         <div class="form-line">
                             <input type="text" class="form-control" maxlength="10" minlength="3" required>
                             <label class="form-label">Label 4</label>
                         </div>
                     </div>
                 </div>
                 <s:textfield oclass="col-lg-4 col-md-4 col-sm-4 col-xs-6" iclass="form-group form-float" name="test1" cssClass="form-control" theme="bootstrap" label="Label 1"/>
                 <s:select oclass="col-lg-4 col-md-4 col-sm-4 col-xs-6" list="sampleList" listKey="keyData" listValue="valueData" data-live-search="true" cssClass="form-control show-tick" theme="bootstrap"/>
             </div>
             <div class="row clearfix form">
                 <div class="col-md-3">
                     <div class="form-group form-float">
                         <div class="form-line">
                             <input type="text" class="form-control" maxlength="10" minlength="3" required>
                             <label class="form-label">Label 5</label>
                         </div>
                     </div>
                 </div>
                 <div class="col-md-3 select-dropdown">
                     <label class="select-label">Label 6</label>
                     <select class="form-control show-tick" data-live-search="true">
                         <option value="">--Please Select--</option>
                         <option value="A">A</option>
                         <option value="B">B</option>
                         <option value="c">C</option>
                     </select>
                 </div>
                 <div class="col-md-3 select-dropdown">
                     <label class="select-label">Label 7</label>
                     <select class="form-control show-tick" data-live-search="true">
                         <option value="">--Please Select--</option>
                         <option value="A">A</option>
                         <option value="B">B</option>
                         <option value="C">C</option>
                     </select>
                 </div>
                 <div class="col-md-3">
                     <div class="form-group form-float">
                         <div class="form-line">
                             <input type="text" class="form-control" maxlength="10" minlength="3" required>
                             <label class="form-label">Label 8</label>
                         </div>
                     </div>
                 </div>
            </div>
         </div>
     </div>
 </form>--%>
</body>
</html>
case