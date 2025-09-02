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
                $(".newItems").select2({
                    theme: 'bootstrap-5',
                    selectionCssClass: 'select2--small',
                    dropdownCssClass: 'select2--small',
                });
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
//                            $("#itemChangeItem").addClass("form-control form-control-sm sds-dropdown");
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
                initDatePicker();
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
            This is "${jspPrefix}sample1.jsp"
            <s:hidden name="addItemRefreshScroll" value="N" id="addItemRefreshScroll"/>
            <div class="card mb-3">
                <div class="card-header bg-light">
                    <h5>Page Title <small class="fw-normal text-600">Edit</small></h5>
                </div>
                <div class="card-body">
                    
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">File Uploading <font class="asterisk">*</font></label>
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
                    <div class="row mb-1">
                        <label class="col-md-4 radio-label">Radio Button 2 <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <div class="form-check form-check-inline">
                                <input value="male" name="optionyes" id="male" type="radio" required class="form-check-input">
                                <label for="male" class="form-check-label">Male</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input value="female" name="optionyes" id="female" type="radio" required class="form-check-input">
                                <label for="female" class="form-check-label">Female</label>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Select Year <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <select name="dropdown" id="dropdown" class="form-control form-control-sm sds-dropdown" required>
                                <option value="">-- Please Select ---</option>
                                <option value="2012">2012</option>
                                <option value="2013">2013</option>
                                <option value="2014">2014</option>
                                <option value="2015">2015</option>
                                <option value="2016" selected="selected">2016</option>
                            </select>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Input <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <input type="text" id='inputId' class="form-control form-control-sm" name="input" value="" required/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Email (Validation) <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <input type="text" id='emailValidateId' class="form-control form-control-sm EmailCheck" lbl="Email (Validation)" name="emailValidateId" value="" required/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 checkbox-label">Checkbox</label>
                        <div class="col-md-5">
                            <div class="form-check">
                                <input type="checkbox" value="1" id="checkbox1" required class="form-check-input">
                                <label for="checkbox1" class="form-check-label"></label>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Sample Textarea</label>
                        <div class="col-md-5 ta_div">
                            <textarea name="ta" id="ta" class="form-control form-control-sm taCount" maxlength="100"></textarea>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Lookup Module Type filter</label>
                        <div class="col-md-5">
                            <s:select list="filterModuleTypeOptions" listKey="keyData" listValue="valueData" name="module_type" id="module_type" class="form-control form-control-sm sds-dropdown"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Sample Module Lookup<font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <div class="input-group input-group-sm">
                                <input type="text" class="form-control form-control-sm" id="module_code" required>
                                <span class="input-group-text" onclick="lookupModal($('#module_code'), 
                                    'modalLookup?lookFor=module_code,module_name,module_descs&writeTo=module_code,module_id,the_module_descs&lookup=useSetup_Module&displayedColumns=module_code,module_name,module_type&filterBy=module_type', '', 'callMe::=0_1')">
                                    <i class="fa fa-search"></i>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Selected Lookup Information<font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <input type="text" class="form-control form-control-sm" id="module_id" required>
                            <label id="the_module_descs" class="form-text"></label>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Date Picker<font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <div class="input-group input-group-sm datepicker">
                                <input class="form-control form-control-sm" name="flatpickr" data-input/>
                                <span class="input-group-text input-button" data-toggle><i class="far fa-calendar-alt"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Split Date Range Picker<font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="input-group input-group-sm splitDateRangePicker" data-name="datepicker" id="datepickerFrom_dsrf">
                                        <input class="form-control form-control-sm" data-options='{"maxDate":"20/01/2021"}' id="datepickerFrom" name="datepickerFrom" value='' required data-input>
                                        <div class="input-group-text input-button" data-toggle>
                                            <i class="far fa-calendar-alt"></i>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="input-group input-group-sm" id="datepickerTo_dsrf">
                                        <input class="form-control form-control-sm" data-options='{"maxDate":"20/01/2021"}' id="datepickerTo" name="datepickerTo" value='' required data-input>
                                        <div class="input-group-text input-button" data-toggle>
                                            <i class="far fa-calendar-alt"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Date Range Picker<font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <div class="input-group input-group-sm" id="drPicker_DateRange">
                                <div class="input-group-text input-button" data-toggle>
                                    <i class="far fa-calendar-alt"></i>
                                </div>
                                <input class="form-control form-control-sm" id="drPicker" name="drPicker" value='' readonly required data-input>
                                <div class="input-group-text input-button" data-clear>
                                    <i class="fa fa-times" ></i>
                                </div>

                                <input class="form-control form-control-sm hidden" id="drPickerFrom" name="drPickerFrom" value=''>
                                <input class="form-control form-control-sm hidden" id="drPickerTo" name="drPickerTo" value=''>
                                <script type="text/javascript">
                                    $(document).ready(function() {
                                        registerDateRangePicker('drPicker');
                                    });
                                </script>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Division <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:select id="divId" list="divisionList" onchange="itemChange()" listKey="keyData" listValue="valueData" name="divId" cssClass="form-control form-control-sm sds-dropdown" value="%{divValue}"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">District <font class="asterisk">*</font></label>
                        <div class="col-md-5" id="disDiv">
                            <s:select id="disId" list="districtList" listKey="keyData" listValue="valueData" name="disId" cssClass="form-control form-control-sm sds-dropdown" value="%{disValue}"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Current Datetime</label>
                        <div class="col-md-5 col-form-label col-form-label-sm">
                            <s:property value="currentDate_str"/>
                            <button type="button" class="btn btn-sm btn-primary mb-1" name="" id="actionName" onclick="return addItem();">
                                <i class="fa fa-plus"></i> <span class="m-1">Add Item</span>
                            </button>
                            <button type="button" class="btn btn-sm btn-primary mb-1" name="" id="actionName" onclick="return addItemRefresh();">
                                <i class="fa fa-plus"></i> <span class="m-1">Add Item(Refresh)</span>
                            </button>
                            <button type="button" class="btn btn-sm btn-primary mb-1" name="" id="actionName" onclick="return doAddItemRefreshScroll();">
                                <i class="fa fa-plus"></i> <span class="m-1">Add Item(Refresh + scroll)</span>
                            </button>
                            <button type="button" class="btn btn-sm btn-primary mb-1" name="" id="actionName" onclick="window.scrollTo(0, 1000); return false;">
                                <i class="fa fa-plus"></i> <span class="m-1">scroll</span>
                            </button>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Item List</label>
                        <div class="col-md-5" id="itemListDiv">
                            <jsp:include page="/sample/b5_itemList.jsp"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm"></label>
                        <div class="col-md-5">
                            <a class="btn btn-sm btn-falcon-default mb-1" href="javascript:;"><i class="fa fa-arrow-left"></i> <span class="m-1">Back</span></a>
                            <button type="submit" class="btn btn-sm btn-primary mb-1" name="action:actionName" id="actionName" onclick="">
                                <i class="fa fa-save"></i> <span class="m-1">Save</span>
                            </button>
                            <button type="button" class="btn btn-sm btn-primary mb-1" name="" id="actionName" onclick="return callValidateForm();">
                                <i class="fa fa-save"></i> <span class="m-1">Save-js</span>
                            </button>
                            <button type="button" class="btn btn-sm btn-primary mb-1" name="" id="actionName" onclick="this.form.submit();">
                                <i class="fa fa-save"></i> <span class="m-1">Test</span>
                            </button>
                            <button type="submit" class="btn btn-sm btn-icon-only btn-danger mb-1" name="action:actionName" id="actionName" onclick="">
                                <i class="fa fa-save"></i>
                            </button>
                            <div class="dropdown">
                                <button type="button" class="btn btn-sm btn-falcon-default dropdown-toggle mb-1" data-bs-toggle="dropdown" aria-expanded="false" aria-haspopup="true"><i class="far fa-square"></i> <span class="m-1">Multi-Action</span></button>
                                <div class="dropdown-menu">
                                    <a href="#" class="dropdown-item"><i class="fa fa-save"></i>Action 1</a>
                                    <a href="#" class="dropdown-item"><i class="fa fa-save"></i>Action 2</a>
                                    <a href="#" class="dropdown-item"><i class="fa fa-save"></i>Action 3</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="card mb-3">
                <div class="card-header bg-light">
                    <h5>Listing using dynamic-config setup (<s:if test="showListing"><a href="loadSample"><font color="orange">Hide</font></a></s:if><s:else><a href="loadSample?showListing=true"><font color="orange">Show</font></a></s:else>)</h5>
                </div>
                <div class="card-body">
                    <s:if test="showListing">
                    <%--<jsp:include page="/sample/da_applicationList.jsp"/>--%>
                    <jsp:include page="/pages/base/b5_da_dynamicList.jsp"/>
                    </s:if>
                </div>
            </div>
            <div class="card mb-3">
                <div class="card-header bg-light">
                    <h5>Test load data using SmartXChange API</h5>
                </div>
                <div class="card-body">
                    <div class="row mb-1">
                        <div class="col-md-12">
                            <button type="button" class="btn btn-sm btn-primary" name="" id="actionName" onclick="loadSmartXChange('token')">
                                <i class="fa fa-binoculars"></i> <span class="m-1">Load Token</span>
                            </button>
                            <button type="button" class="btn btn-sm btn-primary" name="" id="actionName" onclick="loadSmartXChange('getData')">
                                <i class="fa fa-binoculars"></i> <span class="m-1">Load Get</span>
                            </button>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <div class="col-md-12">
                            <s:textarea cssClass="form-control form-control-sm" id="smartXChangeData"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="card mb-3" id="workflowBlock">
                <div class="card-header bg-light">
                    <h5>ROUTE Workflow</h5>
                </div>
                <div class="card-body">
                <s:property value="workflowError"/>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Workflow Action</label>
                        <div class="col-md-5" id="disDiv">
                            <s:select list="workflowActionOptions" listKey="keyData" listValue="valueData" name="workflowAction" id="workflowAction" class="form-control form-control-sm sds-dropdown"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Case ID</label>
                        <div class="col-md-5" id="disDiv">
                            <s:textfield cssClass="form-control form-control-sm" name="wfCaseId" id="wfCaseId"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Workflow/Activity Code</label>
                        <div class="col-md-5" id="disDiv">
                            <s:textarea cssClass="form-control form-control-sm" name="wfCode" id="wfCode"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm">Workflow Parameter</label>
                        <div class="col-md-5" id="disDiv">
                            <s:textarea cssClass="form-control form-control-sm" name="workflowParam" id="workflowParam"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <div class="col-md-12">
                            <button type="button" class="btn btn-sm btn-primary" name="" id="actionName" onclick="return triggerWorkflow();">
                                <i class="fa fa-binoculars"></i><span class="ms-1">Perform Workflow Action</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>            
        </form>
        <form name="form" id="form2" action="" method="post">
            <s:hidden id="loadWhat" name="loadWhat"/>
        </form>
            

    <div id="itemChangeLoader" class="hidden">
    </div>
    <div id="smartXchangeLoader" class="hidden">
    </div>
</body>
</html>