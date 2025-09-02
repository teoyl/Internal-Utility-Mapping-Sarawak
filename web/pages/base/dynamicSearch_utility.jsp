<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="pages/scripts/validation.jsp"></script>
<script type="text/javascript" src="pages/scripts/confirmation.js"></script>
<script type="text/javascript" src="pages/scripts/common.js"></script>
<%--
<style>
    .rowText{
        margin-top:0px !important;
    }
    .checkbox {
        margin-bottom:3px !important;
    }
    .searchCheckbox label {
        margin-right: 8px!important;
        padding-right: 5px!important;
        padding-left: 22px!important;
    }
</style>
--%>
<link href="styles/sub_styles.css" rel="stylesheet" />
<s:if test='useDataTable'>
    <link href="falcon-v3.16.0/public/vendors/datatables.net-bs5/dataTables.bootstrap5.min.css" rel="stylesheet">
    <script src="falcon-v3.16.0/public/vendors/datatables.net/jquery.dataTables.min.js"></script>
    <script src="falcon-v3.16.0/public/vendors/datatables.net-bs5/dataTables.bootstrap5.min.js"></script>
    <script src="falcon-v3.16.0/public/vendors/datatables.net-fixedcolumns/dataTables.fixedColumns.min.js"></script>

    <!--    <script type="text/javascript" language="javascript" src="include/print/jquery.dataTables.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/dataTables.buttons.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/buttons.flash.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/jszip.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/pdfmake.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/vfs_fonts.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/buttons.html5.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/buttons.print.min.js"></script>-->
    <!--<link href="include/datatable/dataTables.bootstrap.min.css" rel="stylesheet"/>-->
    <!--<link href="include/datatable/jquery.dataTables.min.css" rel="stylesheet"/>-->
    <%--
    <style>
        table.dataTable tbody th, table.dataTable tbody td {
            padding: 2px 3px;
        }
        table.dataTable thead .sorting:after {
            content:'';
        }
        table.dataTable thead .sorting_asc:after {
            content:'';
        }
        table.dataTable thead .sorting_desc:after {
            content:'';
        }
        table.dataTable thead th {
          background: transparent !important;
          white-space: nowrap;
        }

/*            table.dataTable thead span.sort-icon {
          display: inline-block;
          padding-left: 5px;
          width: 16px;
          height: 16px;
        }*/

        table.dataTable thead .sorting span { 
            content: 'text that follows 2';
            background: url(images/sortingIcon.gif) left center no-repeat;
            vertical-align: middle;
            background-size: auto auto;
            padding-left: 20px;
        }
        table.dataTable thead .sorting_desc span { 
            content: 'text that follows 3';
            background: url(images/sortdes.gif) left center no-repeat;
            vertical-align: middle;
            background-size: auto auto;
            padding-left: 20px;
        }
        table.dataTable thead .sorting_asc span { 
            content: 'text that follows 2';
            background: url(images/sortasc.gif) left center no-repeat;
            vertical-align: middle;
            background-size: auto auto;
            padding-left: 20px;
        }
    </style>
    --%>
</s:if>
<s:if test='useAc_'>
    <jsp:include page="/include/jquery.autocomplete/ac.impian.jsp"></jsp:include>
</s:if>
<script language="javascript">
    function setAbp() {
        var errors = new Array();
        validateRequired(document.getElementById("search2DynamicFormId"), errors);
        if (errors.length > 0) {
            alert(errors.join('\n'));
            setFocus(document.getElementById("search2DynamicFormId"));
            return false;
        } else {
            $("form#search2DynamicFormId input[type=text]").each(function () {
                if ($(this).attr("name").startsWith("search_abp_")) {
                    $("#lf_" + $(this).attr("name")).val($(this).val());
                }
            });
            return true;
        }
    }
    function submitDcForm(recordId, submitAction) {
        $("#submit_id").val(recordId);
        divSubmitForm(submitAction, "dcForm", 'dcSubmitDiv');
    }
    function submitSearch(formId, formAction) {
        var errors = new Array();
        validateRequired(document.getElementById(formId), errors);
        if (errors.length > 0) {
            alert(errors.join('\n'));
            document.getElementById(formId).focus();
            return false;
        } else {
            $("form#" + formId).attr("action", formAction);
            $("form#" + formId).submit();
            return true;
        }
    }
    var itemValue = {};
    function itemChange(refreshItem, triggerBy, icCode) {
        $("#dcItemChangeLoader").load("itemChangeLoader?itemCate=" + icCode + "&itemValue=" + $("#search_" + triggerBy).val(),
                function (message) {
                    if (message === "Expired") {
    <%-- it you are using itemChangeLoader, it won't come here, 
        it only will come here it you are loading sub-item using your on Action (*access right control needed) --%>
                        document.location = "initLogin";
                    } else {
                        $("#" + refreshItem + "Div").html(message);
                        $("#itemChangeLoader").html("");
                        <%-- clear the loader's content --%>
                        $("#itemChangeItem").attr("name", "search_" + refreshItem);
                        $("#itemChangeItem").addClass("form-control form-control-sm sds-dropdown dsrfdd");
                        $("#itemChangeItem").attr("id", "search_" + refreshItem);
                        <%-- change the id at last line --%>
                        $("#search_" + refreshItem).val(itemValue[refreshItem]);
                        <%-- use new id to reload select2 if it is a select2 dropdown --%>
                        $("#search_" + refreshItem).select2();
                        <%-- use new id to reload select2 if it is a select2 dropdown --%>
                        itemValue[refreshItem] = "";
                    }
                });
    }
    <s:if test="has_right('dynamicSearchRpt')">
    function printRpt(printTo) {
        document.getElementById('search2DynamicFormId')['printTo'].value = printTo;
        divSubmitForm('dynamicSearchRpt', 'search2DynamicFormId', 'printRptDiv');
    }
    </s:if>
    <s:if test='getSearchFieldLookup().size() > 0 || isMixConfig'>
    function lookupModal(field, lookup, desc) {
        var formId = $(field).closest("form").attr('id');
        if (formId === undefined) {
            formId = field.attr('id') + 'form';
            $(field).closest("form").attr('id', formId);
        }
        lookup += "&" + "lookupParentFormId=" + formId + "&lookupDesc=" + (desc === undefined ? "" : desc);
        $("#lookupModal").html("");
        $("#lookupModal").load(lookup,
                function (message) {
                    if (message === "Expired") {
                        document.location = "initLogin";
                    }
                    $("#lookupModal").data('width', '60%');
                    $('#lookupModal').modal('show');
                    $("#lookupModal").on('hide', function () {
                        $(field).focus();
                    });
                });
    }
    </s:if>
    function resetFields(form) {
        var noOfElements = form.elements.length;
        for (var i = 0; i < noOfElements; i++) {
            if (!(form.elements[i].type == "hidden"
                    || form.elements[i].type == "submit"
                    || form.elements[i].type == "button")) {
                clearValue(form.elements[i]);
            }
            if (form.elements[i].id.indexOf("_hidden_") != -1) {
    <%--Delvene @ 19-May-2015 :: Reset searchable hidden field--%>
                clearValue(form.elements[i]);
            }
        }
        $(".dsrfdd").val("");
        $(".dsrfdd").select2();
        $(".dsrf").val("");
        setFocus(form);
    }
    function getPageSize(fieldType, form) {
    <s:if test='showPageSize && !(useDataTable)'>
        document.getElementById('searchFormPageSize' + fieldType).value = document.getElementById('mainPageSize').value;
        if (fieldType == "_mc") {
            form.searchFormPageSize_mc.value = document.getElementById('mainPageSize').value;
        }
    </s:if>
        return true;
    }

    <%--<s:if test="hasMoreSearchField">--%>
    function showHideMoreField() {
        if ($('.ttt').hasClass("hidden")) {
            $('.ttt').removeClass("hidden")
//                $('#showHideMoreField_').val("S");
//                $('#sortShowHideMoreField_').val("S");
//                $('.showHideIcon').removeClass("fa-arrow-down");
//                $('.showHideIcon').addClass("fa-arrow-up");
        } else {
            $('.ttt').addClass("hidden");
//                $('#showHideMoreField_').val("H");
//                $('#sortShowHideMoreField_').val("H");
//                $('.showHideIcon').addClass("fa-arrow-down");
//                $('.showHideIcon').removeClass("fa-arrow-up");
        }
        return false;
    }
    <%--</s:if>--%>

    function preLookupInfo() {
        if (document.getElementById("search_dept_id").value === null || document.getElementById("search_dept_id").value === "") {
            alert("Sila pilih Organisasi Aktiviti - Agensi.");
            return false;
        } else {
            return true;
        }
    }

    function preLookupOper() {
        if (document.getElementById("search_dept_id").value === null || document.getElementById("search_dept_id").value === "") {
            alert("Sila pilih Organisasi Operasi - Agensi.");
            return false;
        } else {
            return true;
        }
    }
    <s:if test='isMixConfig'>
    function getSearchCode() {
        document.getElementById('addFormSearchCode').value = document.getElementById('mainSearchCode').value;
        return true;
    }
    function searchCodeChange(fieldData) {
        <s:iterator value="mcf_actionList" var="mcfAction" status="mcfActionStatus">
        document.getElementById('${mcfAction}table').style.display = 'none';
        document.getElementById('${mcfAction}table').style.height = '0px';
        document.getElementById('${mcfAction}addDelete').style.display = 'none';
        document.getElementById('${mcfAction}addDelete').style.height = '0px';
        </s:iterator>
        document.getElementById(fieldData.split("::")[0] + "table").style.display = 'block';
        document.getElementById(fieldData.split("::")[0] + "table").style.height = 'auto';
        document.getElementById(fieldData.split("::")[0] + "addDelete").style.display = 'block';
        document.getElementById(fieldData.split("::")[0] + "addDelete").style.height = 'auto';
        document.getElementById(fieldData.split("::")[0] + "searchCode").value = fieldData;
    }

    // Added by Delvene @ 28-Aug-2014 :: Overwrite the lookup function in lookup.js to support dynamic form id according to MixConfig action (mcfAction)
    // Added a new parameter :: mcfAction
    function lookup(title, query, lookFor, writeTo, lookupType, displayedColumns, focusOn, onclick, filterBy, mcfAction) {
        var parentFormId = mcfAction + "search2DynamicFormId";
        window.listenerAttached = false;
        var args = 'query=' + query + "&lookFor=" + lookFor + "&writeTo=" + writeTo;
        var writeTo_Array = writeTo.split(",");
        var firstField = writeTo_Array[0];
        if (filterBy)
        {
            args += '&filterBy=' + filterBy;
        }
        if (lookupType)
        {
            args += '&lookup=' + lookupType;
        }
        if (displayedColumns)
        {
            args += '&displayedColumns=' + displayedColumns;
        }
        if (focusOn)
        {
            args += '&focusOn=' + focusOn;
        }
        if (title)
        {
            args += '&title=' + title;
        } else {
            title = "Lookup";
        }
        args = "processLookup?" + args;
        args += "&lookupParentFormId=" + parentFormId;
//        onclick = attachLookupOnclick(query, args, onclick, filterBy, title);
        onclick = attachLookupOnclick(query, args, onclick, filterBy, title, '450', '700');// sereneChye@27/10/2014 add Height, Width 
        document.write("<img id=\'lu_" + firstField + "\' style=\'cursor: pointer; cursor:hand;' src=\'images/document_lookup.gif\' alt=\'" + title + "\' width=\'18' height=\'18' align=\'absmiddle' onclick=\'" + onclick + "\'>");
    }


    // Added by Delvene @ 28-Aug-2014 :: Overwrite the lookup function in lookup.js to support dynamic form id according to MixConfig action (mcfAction) - END
    </s:if>
    $(document).ready(function () {
    <s:iterator value="searchFields" var="field" status="rowStatus">
        <s:set var="searchfield_itemChange">${field}_itemChange</s:set>
        <s:set var="searchfield_dd_type">${field}_dd_type</s:set>
        <s:set var="searchfield_dd_key">${field}_dd_key</s:set>
        <s:set var="searchfield_dd_list">${field}_dd_list</s:set>
        <s:if test='getSearchFieldDD().get(#searchfield_dd_key) != null'>
            <s:if test='getSearchFieldDD().get(#searchfield_dd_type) != null'>
            </s:if>
            <s:else>
                <s:if test="getSearchFieldDD().get(#searchfield_dd_list).equalsIgnoreCase('setupcode')">
    <%--Added by Delvene @ 29-Aug-2013--%>
                </s:if>
                <s:else>
                    <s:if test='searchFieldsMap.containsKey(#searchfield_itemChange)'>
                itemValue["<s:property value='%{searchFieldsMap.get(#field+"_refreshItem")}'/>"] = "<s:property value='%{searchFieldsDataMap.get("search_"+searchFieldsMap.get(#field+"_refreshItem"))}'/>";
                itemChange(<s:property value='%{searchFieldsMap.get(#field+"_itemChange")}'/>);
                    </s:if>
                </s:else>
            </s:else>
        </s:if>
    </s:iterator>
    <s:if test='useDataTable'>
//        var table = $('#dataTableId').DataTable({
//            "order": [],
//            "columnDefs": [ {
//              "targets"  : 'no-sort',
//              "orderable": false,
//            }],
//            "oLanguage": {
//                "sSearch": "<s:text name="filterResult"/>:"
//            }
//        });
//        table.columns().iterator( 'column', function (ctx, idx) {
//            $( table.column(idx).header() ).append('<span class="sort-icon"/>');
//        } );
    </s:if>
    <s:if test='usePopupCalander.equals("Y")'>
                $('.datepickerCls').flatpickr({
                    dateFormat: newDateRangePicker_defaultFormat,
                });
    </s:if>
                initDateRange();
    <s:if test="hasMoreSearchField">
        <s:if test='showHideMoreField_.equals("H")'>
                showHideMoreField();
        </s:if><s:else>
                $('.showHideIcon').removeClass("fa-arrow-down");
                $('.showHideIcon').addClass("fa-arrow-up");
        </s:else>
    </s:if>
    <%--<s:if test='usePopupCalander.equals("Y")'>
    var dateFrom = $('.dateFrom').val();
    if (dateFrom != "") {
        $('.bootstrapDateRangeDown').val($('.dateFrom').val() + '  -  ' + $('.dateTo').val());
    }

        $(".reset").click(function() {
            $('input:checkbox').removeAttr('checked');
            $(".mySelectBox option[selected]").removeAttr("selected");
            $('.myLookupBox').attr("value", "");
            $('.myInputBox').attr("value", "");
            $('.bootstrapDateRangePickerDown').attr("value", "");
            $('.bootstrapDateRangePickerUp').attr("value", "");
            $('.bootstrapDatePickerDown').attr("value", "");
            $('.bootstrapDatePickerUp').attr("value", "");
            $('.dateFrom').attr("value", "");
            $('.dateTo').attr("value", "");
        });



        initDatePicker();
        <s:iterator value="searchFields_with_dateFromTo" var="popCalField" status="popCalFieldStatus">
        $('#${popCalField}From').datepick('option', {onSelect: function(dateText, instance) {
                prepareEndDate(dateText, '${popCalField}')
            }});
        </s:iterator>
        </s:if>--%>

            });
    <%--<s:if test='usePopupCalander.equals("Y")'>
        <s:if test="searchFields_with_dateFromTo != null && searchFields_with_dateFromTo.size > 0">
    function prepareEndDate(date, dateTo_id) {
        $('#' + dateTo_id + 'To').datepick('option', {minDate: $('#' + dateTo_id + 'From').val()}).focus();
    }
        </s:if>
    </s:if>--%>
            function required() {
    <s:property escapeHtml="false" value="pageRequired"/>
            }
</script>




<div class="card">
    <s:if test='isMixConfig'>
        <div class="card-header bg-light">                
            <h5>${searchDescription}</h5>
        </div>
        <div class="card-body">
            <div class="row">
                <s:iterator value="mcf_actionList" var="mcfAction" status="mcfActionStatus">
                    <form action="search2Dynamic" name="${mcfAction}search2DynamicFormId" id="${mcfAction}search2DynamicFormId" method="POST">
                        <!--left box-->
                        <div class="col-md-8">
                            <s:if test="#mcfActionStatus.index == 0 ">
                                <div class="row">
                                    <label class="col-md-4 col-form-label col-form-label-sm">Please Select</label>
                                    <div class="col-md-8 rowText"><s:select onchange="searchCodeChange(this.value)" theme="simple" cssStyle="form-control form-control-sm" id="mainSearchCode" cssClass="form-control form-control-sm mySelectBox" name="searchCode" list='mixedConfig_map.get("mainSearchCodeDD").get("searchCode_dd")' listKey="keyData" listValue="valueData" value='%{searchCode}' /></div>
                                </div>
                            </s:if>
                            <div id="${mcfAction}table">
                                <s:iterator value='%{mixedConfig_map.get(#mcfAction+"hiddenFields")}' var="hiddenField" status="hiddenRowStatus">
                                    <input type='hidden' <s:property escapeHtml="true" value="%{#hiddenField}"/> />
                                </s:iterator>
                                <s:hidden theme="simple" name="pageSize" id="searchFormPageSize_mc" escapeJavaScript="true" />
                                <s:hidden theme="simple" name="action" escapeJavaScript="true" />
                                <s:hidden theme="simple" name="dynamicSortBy" value='%{mixedConfig_map.get(#mcfAction+"dynamicSortBy")}' escapeJavaScript="true" />
                                <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" escapeJavaScript="true" />
                                <s:hidden theme="simple" id="%{mcfAction}searchCode" name="searchCode" value="%{searchCode}" escapeJavaScript="true" />

                                <s:iterator value='%{mixedConfig_map.get(#mcfAction+"searchFields")}' var="field" status="rowStatus">
                                    <s:if test='#field.startsWith("_hidden_")'> <%--Delvene @ 19-May-2015 :: Allow hidden field to be searched--%>
                                        <s:hidden theme="simple" name="search_%{#field}" value='%{searchFieldsDataMap.get("search_"+#field)}' escapeJavaScript="true" />
                                    </s:if>
                                    <s:else>
                                        <div class="row">
                                            <label class="col-md-4 col-form-label col-form-label-sm"><s:property value='%{mixedConfig_map.get(#mcfAction+"searchFieldsLabel")[#rowStatus.index]}'/></label>
                                            <div class="col-md-8">
                                                <s:set var="searchfield_dd_type">${field}_dd_type</s:set>
                                                <s:set var="searchfield_dd_key">${field}_dd_key</s:set>
                                                <s:set var="searchfield_lookupSearch">${field}_lookupSearch</s:set>
                                                <s:set var="searchfield_dd">${field}_dd</s:set>
                                                <s:set var="searchfield_dd_list">${field}_dd_list</s:set>
                                                <s:if test='#field.startsWith("_date_")'>
                                                    <s:if test='#field.endsWith("_fromTo")'>
                                                        <%--s:textfield cssClass="datepick-impian embed  " theme="simple" cssStyle="width: 130px; padding: 1px 3px 1px 3px;" id="%{#mcfAction}_%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/><s:text name="report_to"/>&nbsp;&nbsp;&nbsp;
                                                        <s:textfield cssClass="datepick-impian embed " theme="simple" cssStyle="width: 130px; padding: 1px 3px 1px 3px;" id="%{#mcfAction}_%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/--%>
                                                        <div class="form-group form-group-default">
                                                            <s:textfield cssClass="form-control form-control-sm bootstrapDateRangeDown" theme="simple"/>
                                                            <i class="fa fa-calendar form-control-feedback"></i>    
                                                        </div>
                                                        <s:hidden cssClass="dateFrom" theme="simple" id="%{#mcfAction}_%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}' escapeJavaScript="true" />
                                                        <s:hidden cssClass="dateTo" theme="simple" id="%{#mcfAction}_%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}' escapeJavaScript="true" />
                                                    </s:if>
                                                    <s:else>
                                                        <div class="form-group form-group-default">
                                                            <s:textfield cssClass="form-control form-control-sm bootstrapDatePickerDown" theme="simple" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                                                            <i class="fa fa-calendar form-control-feedback"></i>    
                                                        </div>
                                                        <%--<s:textfield cssClass="datepick-impian embed " theme="simple" cssStyle="width: 322px" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>--%>
                                                    </s:else>
                                                </s:if>
                                                <s:elseif test='mixedConfig_map.get(#mcfAction+"searchFieldDD").get(#searchfield_dd_key) != null'>
                                                    <s:if test='mixedConfig_map.get(#mcfAction+"searchFieldDD").get(#searchfield_dd_type) != null'>
                                                        <s:checkboxlist theme="simple" name="search_%{#field}" list='mixedConfig_map.get(#mcfAction+"searchFieldDD").get(#searchfield_dd)' listKey="keyData" listValue="valueData" value='%{searchFieldsDataMap.get("search_"+#field).split(",")}' />
                                                    </s:if>
                                                    <s:else>
                                                        <s:if test='mixedConfig_map.get(#mcfAction+"searchFieldDD").get(#searchfield_dd_list).equalsIgnoreCase("setupcode")'>    <%--Added by Delvene @ 29-Aug-2013--%>
                                                            <s:select theme="simple" cssClass="rowText form-control form-control-sm mySelectBox " name="search_%{#field}" list='mixedConfig_map.get(#mcfAction+"searchFieldDD").get(#searchfield_dd)' listKey="code_id" listValue="code_desc" value='%{searchFieldsDataMap.get("search_"+#field)}' />
                                                        </s:if>
                                                        <s:else>
                                                            <s:select theme="simple" cssClass="rowText form-control form-control-sm mySelectBox " name="search_%{#field}" list='mixedConfig_map.get(#mcfAction+"searchFieldDD").get(#searchfield_dd)' listKey="keyData" listValue="valueData" value='%{searchFieldsDataMap.get("search_"+#field)}' />
                                                        </s:else>
                                                    </s:else>
                                                </s:elseif>
                                                <s:elseif test='mixedConfig_map.get(#mcfAction+"searchFieldLookup").get(#searchfield_lookupSearch) != null'>
                                                    <div class="has-feedback">
                                                        <s:textfield theme="simple" id="inputValidation"  name="search_%{#field}" value='%{searchFieldsDataMap.get("search_"+#field)}' size="30" readonly="true" cssClass="rowText form-control form-control-sm"/>
                                                        <a href="#" onclick="" class="glyphicon glyphicon-search search-icon"></a>
                                                        <script language="javascript">
                                                            <s:property escapeHtml="false" value='%{mixedConfig_map.get(#mcfAction+"searchFieldLookup").get(#searchfield_lookupSearch)}'/>
                                                        </script>
                                                    </div>
                                                    <%--s:textfield theme="simple" name="search_%{#field}" value='%{searchFieldsDataMap.get("search_"+#field)}' size="30" readonly="true" cssClass="rowText form-control form-control-sm"/>
                                                    <script language="javascript">
                                                        <s:property escape="false" value='%{mixedConfig_map.get(#mcfAction+"searchFieldLookup").get(#searchfield_lookupSearch)}'/>
                                                    </script--%>
                                                </s:elseif>
                                                <s:else>
                                                    <s:textfield theme="simple" name="search_%{#field}" value='%{searchFieldsDataMap.get("search_"+#field)}' cssClass="rowText form-control form-control-sm"/>
                                                </s:else>
                                                <div class="form-text">${searchFieldsHelperText[field]} </div>    
                                            </div>
                                        </div>
                                    </s:else>
                                </s:iterator>
                                <div class="col-md-4 text-right">
                                    <s:if test="#mcfActionStatus.index == 0">
                                        <button class="btn btn-primary" type="submit" onclick="return getPageSize('_mc', this.form);"><i class="fa fa-search"></i>&nbsp;<s:text name="button.search"/></button>
                                        <button class="btn btn-falcon-default reset" type="button" onclick="resetFields(this.form)"><i class="fa fa-undo"></i>&nbsp;<s:text name="button.reset"/></button>
                                    </s:if></div>
                            </div>
                        </div>
                        <!--right box-->
                        <div class="col-md-4 text-right">
                            <s:if test="#mcfActionStatus.index == 0">
                                <button class="btn btn-primary" type="submit" onclick="return getPageSize('_mc', this.form);"><i class="fa fa-search"></i>&nbsp;<s:text name="button.search"/></button>
                                <button class="btn btn-falcon-default reset" type="button" onclick="resetFields(this.form)"><i class="fa fa-undo"></i>&nbsp;<s:text name="button.reset"/></button>
                            </s:if>
                            <s:if test="hasMoreSearchField">
                                <!--<button class="btn btn-primary" type="submit" onclick="return getPageSize('_mc', this.form);"><i class="fa fa-  search"></i><s:text name="button.more"/></button>-->
                            </s:if>
                            <%--input type="submit" onclick="return getPageSize('_mc', this.form);" value="<s:text name="button.search"/>" class="defaultButton"/>
                            <input type="button" value="<s:text name="button.reset"/>" class="defaultButton" onclick="resetFields(this.form)"/--%>
                        </div>
                    </form>
                </s:iterator>
            </div>
        </s:if>
        <s:else>
            <form action="search2Dynamic" id="search2DynamicFormId" method="POST" >
                <div class="card-header bg-light">                    
                    <div class="row flex-between-center">                            
                        <div class="col-6 col-sm-auto d-flex align-items-center pe-0">
                            <h5 class="">${searchDescription}</h5>
                        </div>
                        <div class="col-6 col-sm-auto ms-auto text-end ps-0">
                            <s:if test='hideAddButton.equals("N")'>
                                <s:if test="has_right('loadAddPage')">
                                    <button class="btn btn-falcon-default btn-sm ms-2" type="button" onclick="submitForm('search2DynamicFormId', '${addPageURL}')" name="action:${addPageURL}" id="${addPageURL}"><i class="fa fa-plus"></i><span class="d-none d-sm-inline-block ms-1"><s:text name="button.add"/></span></button>
                                            <%--<s:submit theme="simple" action="%{addPageURL}" value="%{getText('button.add')}" cssClass="defaultButton" />--%>
                                        </s:if>
                                    </s:if>
                                    <s:if test='hideDeleteButton.equals("N")'>
                                        <s:if test="has_right('delete')">
                                    <button class="btn btn-falcon-default btn-sm ms-2" type="button" name="action:${deleteURL}" id="${deleteURL}" onclick="if (isCheckboxSelected(document.getElementById('listForm').selected)) {
                                                if (confirmDelete()) {
                                                    $('#listForm').attr('action', '${deleteURL}');
                                                    document.getElementById('listForm').submit();
                                                } else {
                                                    return false;
                                                }
                                            } else {
                                                return false;
                                            }"><i class="fa far fa-trash-alt"></i><span class="d-none d-sm-inline-block ms-1"><s:text name="button.delete"/></span></button>
                                            <%--s:submit theme="simple" action="%{deleteURL}" value="%{getText('button.delete')}" cssClass="defaultButton"
                                                      onclick="if ( isCheckboxSelected(form.selected)) {return confirmDelete();} else {return false};"/--%>
                                        </s:if>
                                    </s:if>
                                    <s:if test="additionalButton.size() > 0">
                                        <s:iterator value="additionalButton" var="addButton" status="addButtonStatus">
                                            <s:if test="additionalButtonChecked[#addButtonStatus.index]">
                                        <button class="btn btn-falcon-default btn-sm ms-2" type="button" onclick="
                                                if (isCheckboxSelected(document.getElementById('listForm').selected)) {
                                                    if (setAbp()) {
                                                <s:if test='additionalButtonMap.containsKey(#addButton+"offLoading")'>
                                                        offLoading = true;
                                                </s:if>
                                                        submitForm('listForm', '<s:property value='additionalButtonAction[#addButtonStatus.index]'/>');
                                                <s:if test='additionalButtonMap.containsKey(#addButton+"offLoading")'>
                                                        offLoading = false;
                                                </s:if>
                                                    }
                                                }" name="action:<s:property value='additionalButtonAction[#addButtonStatus.index]'/>" id="<s:property value='additionalButtonAction[#addButtonStatus.index]'/>"><i class="<s:property value='additionalButtonIcon[#addButtonStatus.index]'/>"></i><span class="d-none d-sm-inline-block ms-1"><s:property value='additionalButtonLabel[#addButtonStatus.index]'/></span></button>
                                            </s:if><s:else>
                                                <s:if test='additionalButtonMap.containsKey(#addButton+"divSubmitForm")'>
                                            <button class="btn btn-falcon-default btn-sm ms-2" type="button" onclick="divSubmitForm('<s:property value='additionalButtonAction[#addButtonStatus.index]'/>', 'listForm', 'dcSubmitDiv')" name="action:<s:property value='additionalButtonAction[#addButtonStatus.index]'/>" id="<s:property value='additionalButtonAction[#addButtonStatus.index]'/>"><i class="<s:property value='additionalButtonIcon[#addButtonStatus.index]'/>"></i><span class="d-none d-sm-inline-block ms-1"><s:property value='additionalButtonLabel[#addButtonStatus.index]'/></span></button>
                                                </s:if><s:else>
                                            <button class="btn btn-falcon-default btn-sm ms-2" type="button" onclick="submitForm('search2DynamicFormId', '<s:property value='additionalButtonAction[#addButtonStatus.index]'/>')" name="action:<s:property value='additionalButtonAction[#addButtonStatus.index]'/>" id="<s:property value='additionalButtonAction[#addButtonStatus.index]'/>"><i class="<s:property value='additionalButtonIcon[#addButtonStatus.index]'/>"></i><span class="d-none d-sm-inline-block ms-1"><s:property value='additionalButtonLabel[#addButtonStatus.index]'/></span></button>
                                                </s:else>
                                            </s:else>
                                        </s:iterator>
                                    </s:if>
                        </div>
                    </div>                            
                </div>
                <div class="card-body">
                    <div class="row pb-3">
                        <!--left box-->
                        <div class="col-md-8">
                            <div class="ttt">
                                <s:iterator value="hiddenFields" var="hiddenField" status="hiddenRowStatus">
                                    <input type='hidden' <s:property escapeHtml="true" value="%{#hiddenField}"/> />
                                </s:iterator>
                                <s:hidden theme="simple" name="pageSize" id="searchFormPageSize" escapeJavaScript="true" />
                                <s:hidden theme="simple" name="printTo" id="printTo" escapeJavaScript="true" />
                                <s:hidden theme="simple" name="action" escapeJavaScript="true" />
                                <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" escapeJavaScript="true" />
                                <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" escapeJavaScript="true" />
                                <div class="row mb-1">
                                    <label class="col-md-4 col-form-label col-form-label-sm">Survey Job No.</label>
                                    <div class="col-md-8">
                                        <div id="survey_job">
                                            <div class="row">
                                                <div class="col-md-4 p-0">
                                                    <s:select theme="simple" cssClass="dsrfdd rowText form-control form-control-sm sds-dropdown mySelectBox " name="search_cs_usj_div" id="search_cs_usj_div" list="getSearchDDList('cs_usj_div_dd')" listKey="keyData" listValue="valueData" value='%{searchFieldsDataMap.get("search_cs_usj_div")}' />
                                                </div>    
                                                <!--<div class="col-md-4 p-0"><input type="text" id="search_cs_usj_div" maxlength='2' class="rowText form-control form-control-sm" name="search_cs_usj_div" value="<s:property value='%{searchFieldsDataMap.get("search_cs_usj_div")}'/>" /></div>-->    
                                                <div class="col-md-4 p-0"><input type="text" id="search_cs_usj_seq" maxlength='6' class="rowText form-control form-control-sm" name="search_cs_usj_seq" placeholder="Sequence" value="<s:property value='%{searchFieldsDataMap.get("search_cs_usj_seq")}'/>" /></div>    
                                                <div class="col-md-4 p-0"><input type="text" id="search_cs_usj_year" maxlength='4' class="rowText form-control form-control-sm" name="search_cs_usj_year" placeholder="Year" value="<s:property value='%{searchFieldsDataMap.get("search_cs_usj_year")}'/>" /></div>    
                                            </div>
                                        </div>
                                        <div class="form-text"></div>  
                                    </div>
                                </div>
                                <div class="row mb-1">
                                    <label class="col-md-4 col-form-label col-form-label-sm">Survey Plan No.</label>
                                    <div class="col-md-8">
                                        <div id="survey_plan">
                                            <div class="row">
                                                <div class="col-md-4 p-0"><input type="text" id="usj" maxlength='6' class="rowText form-control form-control-sm" name="usj" value="USJ" disabled /></div>    
                                                <div class="col-md-4 p-0">
                                                    <s:select theme="simple" cssClass="dsrfdd rowText form-control form-control-sm sds-dropdown mySelectBox " name="search_cs_plan_div" id="search_cs_plan_div" list="getSearchDDList('cs_plan_div_dd')" listKey="keyData" listValue="valueData" value='%{searchFieldsDataMap.get("search_cs_plan_div")}' />
                                                </div>    
                                                <div class="col-md-4 p-0"><input type="text" id="search_cs_plan_no" maxlength='6' class="rowText form-control form-control-sm" name="search_cs_plan_no" placeholder="Sequence" value="<s:property value='%{searchFieldsDataMap.get("search_cs_plan_no")}'/>" /></div>    
                                            </div>
                                        </div>
                                        <div class="form-text"></div>  
                                    </div>
                                </div>

                                <s:iterator value="searchFields" var="field" status="rowStatus">
                                    <s:if test='!#field.startsWith("cs_")'> 
                                        <s:set var="searchfield_label">${field}_label</s:set>
                                        <s:set var="searchfield_">search_${field}</s:set>
                                        <s:if test='#field.startsWith("_hidden_")'> <%--Delvene @ 19-May-2015 :: Allow hidden field to be searched--%>
                                            <%--<s:hidden theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" />--%>
                                            <s:hidden theme="simple" name="search_%{#field}" value='%{searchFieldsDataMap.get("search_"+#field)}' escapeJavaScript="true" />
                                            <s:hidden theme="simple" name="search_%{#field}2" value='%{searchFieldsMap.get("search_"+#field)}' escapeJavaScript="true" />
                                        </s:if>
                                        <s:else>
                                            <div class="row mb-1">
                                                <label class="col-md-4 col-form-label col-form-label-sm">${searchFieldsLabel[rowStatus.index]}<s:if test='isRequiredField(#field)'><jsp:include page="/pages/base/requiredField.jsp"/></s:if></label>
                                                    <div class="col-md-8 p-0">
                                                    <s:set var="searchfield_dd_type">${field}_dd_type</s:set>
                                                    <s:set var="searchfield_dd_key">${field}_dd_key</s:set>
                                                    <s:set var="searchfield_lookupSearch">${field}_lookupSearch</s:set>
                                                    <s:set var="searchfield_dd">${field}_dd</s:set>
                                                    <s:set var="searchfield_dd_list">${field}_dd_list</s:set>
                                                    <s:set var="searchfield_itemChange">${field}_itemChange</s:set>
                                                    <s:if test='#field.startsWith("_date_")'>
                                                        <s:if test='#field.endsWith("_fromTo")'>
                                                            <s:set var="searchfield_splitDate"><s:property value="%{#field.substring(6, (#field.length() - 7))}"/>_splitDate</s:set>
                                                            <s:if test='searchFieldsMap.containsKey(#searchfield_splitDate)'>
                                                                <div class="row">
                                                                    <div class="col-md-6">
                                                                        <div class="input-group input-group-sm splitDateRangePicker" data-name="search_<s:property value='%{#field.substring(6, (#field.length() - 7))}'/>" id="search_<s:property value='%{#field.substring(6, (#field.length() - 7))}'/>From_dsrf">
                                                                            <input type="text" class="form-control form-control-sm" id="search_<s:property value='%{#field.substring(6, (#field.length() - 7))}'/>From" name="search_<s:property value='%{#field.substring(6, (#field.length() - 7))}'/>From" value='<s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/>' data-input/>
                                                                            <%--<input class="form-control form-control-sm DateRange SameDate" id="datepickerFrom" name="datepickerFrom" value='' required>--%>
                                                                            <div class="input-group-text input-button" data-toggle>
                                                                                <i class="fa fa-calendar"></i>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-md-6">
                                                                        <div class="input-group input-group-sm" id="search_<s:property value='%{#field.substring(6, (#field.length() - 7))}'/>To_dsrf">
                                                                            <%--<input class="form-control form-control-sm DateRange SameDate" id="datepickerTo" name="datepickerTo" value='' required>--%>
                                                                            <input type="text" class="form-control form-control-sm" id="search_<s:property value='%{#field.substring(6, (#field.length() - 7))}'/>To" name="search_<s:property value='%{#field.substring(6, (#field.length() - 7))}'/>To" value='<s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>' data-input/>
                                                                            <div class="input-group-text input-button" data-toggle>
                                                                                <i class="fa fa-calendar"></i>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </s:if><s:else>
                                                                <!--                                                <div class="form-group form-group-default">
                                                                <s:textfield cssClass="form-control form-control-sm bootstrapDateRangeDown" theme="simple"/>
                                                                <i class="fa fa-calendar form-control-feedback"></i>    
                                                            </div>-->
                                                                <div class="input-group input-group-sm" id="search_${field.substring(6, (field.length() - 7))}_DateRange">
                                                                    <a class="input-group-text input-button" data-toggle>
                                                                        <i class="fa fa-calendar"></i>
                                                                    </a>
                                                                    <!--<input class="form-control form-control-sm" id="search_${field.substring(6, (field.length() - 7))}" name="search_${field.substring(6, (field.length() - 7))}" value='<s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/> - <s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>' readonly data-input>-->
                                                                    
                                                                    <s:textfield
                                                                        cssClass="form-control form-control-sm"
                                                                        id="search_%{#field.substring(6, #field.length() - 7)}"
                                                                        name="search_%{#field.substring(6, #field.length() - 7)}"
                                                                        value="%{searchFieldsDateData.get('search_'+extractSearchFieldForDate(#field)+'From') + ' - ' + searchFieldsDateData.get('search_'+extractSearchFieldForDate(#field)+'To')}"
                                                                        readonly="true"
                                                                        data-input="true"
                                                                        escapeHtml="true"
                                                                    />
                                                                    <a class="input-group-text input-button" data-clear>
                                                                        <i class="fa fa-times"></i>
                                                                    </a>
                                                                </div>
                                                                <script type="text/javascript">
                                                                    $(function () {
                                                                        registerDateRangePicker("search_${field.substring(6, (field.length() - 7))}");
                                                                    });
                                                                </script>
                                                                <s:hidden cssClass="dsrf dateFrom" theme="simple" id="search_%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}' escapeJavaScript="true" />
                                                                <s:hidden cssClass="dsrf dateTo" theme="simple" id="search_%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}' escapeJavaScript="true" />
                                                            </s:else>
                                                        </s:if>
                                                        <s:else>
                                                            <!--                                                <div class="form-group form-group-default">
                                                            <s:textfield cssClass="form-control form-control-sm bootstrapDatePickerDown" theme="simple" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                                                            <i class="fa fa-calendar form-control-feedback"></i>    
                                                        </div>-->
                                                            <div class="input-group input-group-sm datepicker">
                                                                <div class="input-group-text input-button" data-toggle>
                                                                    <i class="fa fa-calendar"></i>
                                                                </div>
                                                                <input type="text" class="form-control form-control-sm datepickerCls" theme="simple" name="search_<s:property value='%{#field.substring(6)}'/>" value='<s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>' data-input/>
                                                            </div>
                                                            <%--s:textfield cssClass="datepick-impian embed " theme="simple" cssStyle="form-control form-control-sm" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/--%>
                                                        </s:else>
                                                    </s:if>
                                                    <s:elseif test='getSearchFieldDD().get(#searchfield_dd_key) != null'>
                                                        <s:if test='getSearchFieldDD().get(#searchfield_dd_type) != null'>
                                                            <s:if test='getSearchFieldDD().get(#searchfield_dd_type).equals("radio")'>
                                                                <div class="radio radio-inline radio-success">
                                                                    <s:radio list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" theme="simple" name="search_%{#field}" value='%{searchFieldsDataMap.get("search_"+#field).split(",")}'/>
                                                                </div>
                                                            </s:if><s:else>
                                                                <div class="checkbox searchCheckbox check-success">
                                                                    <s:checkboxlist theme="simple" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value='%{searchFieldsDataMap.get("search_"+#field).split(",")}' cssClass="form-control form-control-sm"/>
                                                                </div>
                                                            </s:else>
                                                        </s:if>
                                                        <s:else>
                                                            <div id="${field}Div">
                                                                <s:if test="getSearchFieldDD().get(#searchfield_dd_list).equalsIgnoreCase('setupcode')">    <%--Added by Delvene @ 29-Aug-2013--%>
                                                                    <s:select theme="simple" cssClass="dsrfdd rowText form-control form-control-sm sds-dropdown mySelectBox " name="search_%{#field}" id="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="code_id" listValue="code_desc" value='%{searchFieldsDataMap.get("search_"+#field)}' />
                                                                </s:if>
                                                                <s:else>
                                                                    <s:if test='searchFieldsMap.containsKey(#searchfield_itemChange)'>
                                                                        <s:select theme="simple" cssClass="dsrfdd rowText form-control form-control-sm sds-dropdown mySelectBox " name="search_%{#field}" id="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value='%{searchFieldsDataMap.get("search_"+#field)}' onchange='itemChange(%{searchFieldsMap.get(#field+"_itemChange")})'/>
                                                                    </s:if><s:else>
                                                                        <s:select theme="simple" cssClass="dsrfdd rowText form-control form-control-sm sds-dropdown mySelectBox " name="search_%{#field}" id="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value='%{searchFieldsDataMap.get("search_"+#field)}' />
                                                                    </s:else>
                                                                </s:else>
                                                            </div>
                                                        </s:else>
                                                    </s:elseif>
                                                    <s:elseif test='getSearchFieldLookup().get(#searchfield_lookupSearch) != null'>
                                                        <div class="input-group">
                                                            <s:textfield theme="simple" cssClass="form-control form-control-sm" id="search_%{#field}" name="search_%{#field}" value='%{searchFieldsDataMap.get("search_"+#field)}' size="30"/>
                                                            <span class="input-group-addon" onclick="<s:property value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>"><i class="fa fa-search"></i></span>
                                                        </div>
                                                        <%--<div class="form-group input-group">
                                                            <s:textfield theme="simple" cssClass="form-control form-control-sm myLookupBox "  name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30"/>
                                                            <span class="input-group-btn">
                                                                <script language="javascript">
                                                                    <s:property escapeHtml="false" value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>
                                                                </script>
                                                            </span>
                                                        </div>--%>
                                                        <%--s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30" cssClass="form-control form-control-sm" cssStyle="width: 292px; padding: 1px 3px 1px 3px;"/>
                                                         <script language="javascript">
                                                            <s:property escape="false" value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>
                                                        </script--%>
                                                    </s:elseif>
                                                    <s:else>
                                                        <div id="${field}Div">
                                                            <input type="text" name="search_${field}" value="<s:property value='%{searchFieldsDataMap.get("search_"+#field)}'/>" size="30" class="rowText form-control form-control-sm"/>
                                                        </div>
                                                        <%--<s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30" cssClass="rowText form-control form-control-sm" cssStyle=" width: %{getFieldStyleFormat(#field, 'searchField', '', '')}"/>--%>
                                                    </s:else>
                                                    <div class="form-text">${searchFieldsHelperText[field]}</div>    
                                                </div>
                                            </div>
                                        </s:else>
                                    </s:if>
                                </s:iterator>
                                <s:iterator value="moreSearchFields" var="field" status="rowStatus">
                                    <s:set var="searchfield_label">${field}_label</s:set>
                                    <s:set var="searchfield_">search_${field}</s:set>
                                    <s:if test='#field.startsWith("_hidden_")'> <%--Delvene @ 19-May-2015 :: Allow hidden field to be searched--%>
                                        <s:hidden theme="simple" name="search_%{#field}" value='%{searchFieldsMap[#searchfield_]}' escapeJavaScript="true" />
                                    </s:if>
                                    <s:else>
                                        <div class="row mb-1 moreField">
                                            <label class="col-md-4 col-form-label col-form-label-sm">${searchFieldsMap[searchfield_label]}</label>
                                            <div class="col-md-8">
                                                <s:set var="searchfield_dd_type">${field}_dd_type</s:set>
                                                <s:set var="searchfield_dd_key">${field}_dd_key</s:set>
                                                <s:set var="searchfield_lookupSearch">${field}_lookupSearch</s:set>
                                                <s:set var="searchfield_dd">${field}_dd</s:set>
                                                <s:set var="searchfield_dd_list">${field}_dd_list</s:set>
                                                <s:if test='#field.startsWith("_date_")'>
                                                    <s:if test='#field.endsWith("_fromTo")'>
                                                        <div class="input-group input-group-sm" id="search_${field.substring(6, (field.length() - 7))}_DateRange">
                                                            <a class="input-group-text input-button" data-toggle>
                                                                <i class="fa fa-calendar"></i>
                                                            </a>
                                                            <input class="form-control form-control-sm" id="search_${field.substring(6, (field.length() - 7))}" name="search_${field.substring(6, (field.length() - 7))}" value='<s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/> - <s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>' readonly data-input/>
                                                            <a class="input-group-text input-button" data-clear>
                                                                <i class="fa fa-times"></i>
                                                            </a>
                                                        </div>                                                    
                                                        <script type="text/javascript">
                                                                    $(function () {
                                                                        registerDateRangePicker("search_${field.substring(6, (field.length() - 7))}");
                                                                    });
                                                        </script>
                                                        <s:hidden cssClass="dateFrom" theme="simple" id="search_%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}' escapeJavaScript="true" />
                                                        <s:hidden cssClass="dateTo" theme="simple" id="search_%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}' escapeJavaScript="true" />
                                                    </s:if>
                                                    <s:else>
                                                        <div class="input-group input-group-sm datepicker">
                                                            <div class="input-group-text input-button" data-toggle>
                                                                <i class="fa fa-calendar"></i>
                                                            </div>
                                                            <input type="text" class="form-control form-control-sm datepickerCls" theme="simple" name="search_<s:property value='%{#field.substring(6)}'/>" value='<s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>' data-input/>
                                                        </div>
                                                    </s:else>
                                                </s:if>
                                                <s:elseif test='getSearchFieldDD().get(#searchfield_dd_key) != null'>
                                                    <s:if test='getSearchFieldDD().get(#searchfield_dd_type) != null'>
                                                        <s:if test='getSearchFieldDD().get(#searchfield_dd_type).equals("radio")'>
                                                            <div class="radio radio-inline radio-success">
                                                                <s:radio list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" theme="simple" name="search_%{#field}" value='%{searchFieldsDataMap.get("search_"+#field).split(",")}'/>
                                                            </div>
                                                        </s:if><s:else>
                                                            <div class="checkbox searchCheckbox check-success">
                                                                <s:checkboxlist theme="simple" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value='%{searchFieldsDataMap.get("search_"+#field).split(",")}' cssClass="form-control form-control-sm"/>
                                                            </div>
                                                        </s:else>
                                                    </s:if>
                                                    <s:else>
                                                        <s:if test="getSearchFieldDD().get(#searchfield_dd_list).equalsIgnoreCase('setupcode')">    <%--Added by Delvene @ 29-Aug-2013--%>
                                                            <s:select theme="simple" cssClass="dsrfdd rowText form-control form-control-sm mySelectBox " name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="code_id" listValue="code_desc" value='%{searchFieldsDataMap.get("search_"+#field)}' />
                                                        </s:if>
                                                        <s:else>
                                                            <s:select theme="simple" cssClass="dsrfdd rowText form-control form-control-sm mySelectBox " name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value='%{searchFieldsDataMap.get("search_"+#field)}' />
                                                        </s:else>
                                                    </s:else>
                                                </s:elseif>
                                                <s:elseif test='getSearchFieldLookup().get(#searchfield_lookupSearch) != null'>
                                                    <div class="input-group">
                                                        <s:textfield theme="simple" cssClass="form-control form-control-sm" id="search_%{#field}" name="search_%{#field}" value="%{searchFieldsDataMap[#searchfield_]}" size="30"/>
                                                        <span class="input-group-addon" onclick="<s:property value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>"><i class="fa fa-search"></i></span>
                                                    </div>
                                                    <%--s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30" cssClass="form-control form-control-sm" cssStyle="width: 292px; padding: 1px 3px 1px 3px;"/>
                                                     <script language="javascript">
                                                        <s:property escape="false" value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>
                                                    </script--%>
                                                </s:elseif>
                                                <s:else>
                                                    <s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsMap[#searchfield_]}" size="30" cssClass="rowText form-control form-control-sm" cssStyle=" width: %{getFieldStyleFormat(#field, 'searchField', '', '')}"/>
                                                </s:else>
                                                <div class="form-text">${searchFieldsHelperText[field]}</div>    
                                            </div>
                                        </div>
                                    </s:else>
                                </s:iterator>
                            </div>    
                        </div>
                        <!--right box-->
                        <div class="col-md-4 text-end">
                            <%--<s:if test="hasMoreSearchField">--%>
                            <button class="no-bg-btn showHide btn-sm mb-1" type="submit" onclick="return showHideMoreField();"><i class="showHideIcon fa fa-search search search-icon"></i></button>
                                <%--<s:hidden name="showHideMoreField_" id="showHideMoreField_"/>--%>
                                <%--</s:if>--%>
                        </div>
                    </div>
                    <div class="row pb-3">
                        <div class="col-md-8 text-end">
                            <button class="btn btn-sm mb-1 ttt green-btn text-white" type="submit" onclick="getPageSize('', this.form);
                                    return submitSearch('search2DynamicFormId', 'search2Dynamic');"><span class="ms-1"><s:text name="button.search"/></span></button>
                            <button class="btn btn-falcon-default reset btn-sm mb-1 ttt" type="button" onclick="resetFields(this.form)"><i class="fa fa-undo word-green"></i><span class="ms-1"><s:text name="button.reset"/></span></button>
                        </div>
                        <div class="col-md-4">
                            <s:if test='showPageSize && !(useDataTable)'>
                                <div class="row mb-1">
                                    <label class="col-md-9 col-form-label col-form-label-sm text-end"><s:text name="paging.recordPerPage"/>:</label>
                                    <div class="col-md-3">
                                        <s:select theme="simple" id="mainPageSize" name="mainPageSize" list="pageSizeOption" listKey="keyData" listValue="valueData" value="%{pageSize}" cssClass="dsrf form-control form-control-sm sds-dropdown"/>
                                    </div>
                                </div>
                            </s:if>
                        </div>
                    </div>
            </form>
        </s:else>
        <jsp:include page="actionError.jsp"></jsp:include>
            <form theme="simple" action="search2Dynamic" id="sortForm" method="POST">
            <s:hidden name="showHideMoreField_" id="sortShowHideMoreField_" escapeJavaScript="true" />
            <s:hidden theme="simple" name="pageSize" value="%{pageSize}" escapeJavaScript="true" />
            <s:hidden theme="simple" name="searchCode" value="%{searchCode}" escapeJavaScript="true" />
            <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" escapeJavaScript="true" />
            <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" escapeJavaScript="true" />
            <s:hidden theme="simple" name="action" />
            <s:if test="result.size() > 0">
                <s:hidden theme="simple" name="listSize" value="1" escapeJavaScript="true" />
            </s:if>
            <s:else>
                <s:hidden theme="simple" name="listSize" value="0" escapeJavaScript="true" />
            </s:else>
            <s:iterator value="searchFields" var="field" status="rowStatus">
                <s:if test='#field.startsWith("_date_")'>
                    <s:if test='#field.endsWith("_fromTo")'>
                        <s:hidden id="sort_search_%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}' escapeJavaScript="true" />
                        <s:hidden id="sort_search_%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}' escapeJavaScript="true" />
                    </s:if>
                    <s:else>
                        <s:hidden id="sort_search_%{#field.substring(6)}" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}' escapeJavaScript="true" />
                    </s:else>
                </s:if><s:else>
                    <s:hidden id="sort_search_%{#field}" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" escapeJavaScript="true" />
                </s:else>
            </s:iterator>
            <s:iterator value="moreSearchFields" var="field" status="rowStatus">
                <s:set var="searchfield_">search_${field}</s:set>
                <s:if test='#field.startsWith("_date_")'>
                    <s:if test='#field.endsWith("_fromTo")'>
                        <s:hidden id="sort_search_%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}' escapeJavaScript="true" />
                        <s:hidden id="sort_search_%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}' escapeJavaScript="true" />
                    </s:if>
                    <s:else>
                        <s:hidden id="sort_search_%{#field.substring(6)}" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}' escapeJavaScript="true" />
                    </s:else>
                </s:if><s:else>
                    <s:hidden id="sort_search_%{#field}" name="search_%{#field}" value="%{searchFieldsMap[#searchfield_]}" escapeJavaScript="true" />
                </s:else>
            </s:iterator>
        </form>


        <s:if test='isMixConfig'>
            <form action="dynamicAction" method="POST" class="prForm">
                <s:if test="result.size() > 0">
                    <!--<hr>-->
                    <div class="row">
                        <div class="col-md-4">
                            <s:hidden theme="simple" id="addFormSearchCode" name="searchCode" escapeJavaScript="true" />
                            <s:hidden theme="simple" name="action" escapeJavaScript="true" />
                            <s:iterator value="mcf_actionList" var="mcfAction" status="mcfActionStatus">
                                <div id="${mcfAction}addDelete" >
                                    <s:if test='!("Y".equals(mixedConfig_map.get(#mcfAction+"hideAddButton"))) || !("Y".equals(mixedConfig_map.get(#mcfAction+"hideDeleteButton")))'>
                                        <s:if test='!"Y".equals(mixedConfig_map.get(#mcfAction+"hideAddButton"))'>
                                            <s:if test="has_right2('DynamicAction','loadAddPage',#mcfAction)">
                                                <button class="btn btn-falcon-default" type="submit" name="action:<s:property value='%{mixedConfig_map.get(#mcfAction+"addPageURL")}'/>" id="<s:property value='%{mixedConfig_map.get(#mcfAction+"addPageURL")}'/>"><i class="fa fa-plus"></i><s:text name="button.add"/></button>
                                                    <%--<s:submit theme="simple" id="addButton" action='%{mixedConfig_map.get(#mcfAction+"addPageURL")}' value="%{getText('button.add')}" cssClass="defaultButton" onclick="return getSearchCode();"/>--%>
                                                </s:if>
                                            </s:if>
                                            <s:if test='!"Y".equals(mixedConfig_map.get(#mcfAction+"hideDeleteButton"))'>
                                                <s:if test="has_right2('DynamicAction','delete',#mcfAction)">
                                                <button class="btn btn-falcon-default" type="submit" name="action:<s:property value='%{mixedConfig_map.get(#mcfAction+"deleteURL")}'/>" 
                                                        id="<s:property value='%{mixedConfig_map.get(#mcfAction+"deleteURL")}'/>" 
                                                        onclick="if (isCheckboxSelected(document.getElementsByName('selected'))) {
                                                                    return confirmDelete();
                                                                } else {
                                                                    return false
                                                                }
                                                                ;"><i class="fa far fa-trash-alt"></i><s:text name="button.delete"/></button>    
                                                    <%--s:submit theme="simple" id="deleteButton" action='%{mixedConfig_map.get(#mcfAction+"deleteURL")}' value="%{getText('button.delete')}" cssClass="defaultButton"
                                                                  onclick="if ( isCheckboxSelected(document.getElementsByName('selected'))) {return confirmDelete();} else {return false};"/--%>
                                                </s:if>
                                            </s:if>
                                        </s:if>
                                </div>
                            </s:iterator>
                        </div>
                        <div class="col-md-8 text-right">
                            <s:if test='showPageSize && useDataTable'>
                                <div class="form-group" style="padding-top:0px;padding-bottom:0px;float:right;">
                                    <div class="input-group" style="width: 180px;"><span class="input-group-addon" style="background-color:white;color:black;border:none;">Records in page</span>
                                        <s:select theme="simple" id="mainPageSize" name="mainPageSize" list="pageSizeOption" listKey="keyData" listValue="valueData" value="%{pageSize}" cssClass="form-control form-control-sm"/>
                                    </div>
                                </div>

                                <%--label><s:text name="paging.recordPerPage"/>:</label>&nbsp;<s:select theme="simple" id="mainPageSize" name="mainPageSize" list="pageSizeOption" listKey="keyData" listValue="valueData" value="%{pageSize}" cssClass="form-control form-control-sm"/--%>
                            </s:if>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <jsp:include page="${listPage}.jsp"/>
                        </div>
                    </div>
                </s:if>
            </form>
        </s:if>
        <s:else>
            <form action="dynamicAction" method="POST" id="listForm">
                <s:iterator value="searchFields" var="field" status="rowStatus">
                    <s:if test='#field.startsWith("abp_")'>
                        <s:hidden theme="simple" name="search_%{#field}" id="lf_search_%{#field}" escapeJavaScript="true" />
                    </s:if>
                </s:iterator>
                <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
                <s:hidden theme="simple" name="action" />
                <%--<s:if test="result.size() > 0">--%>
                <!--<hr>-->
                <%--<div class="row">
                    <div class="col-md-8">
                        <s:if test='hideAddButton.equals("N")'>
                            <s:if test="has_right('loadAddPage')">
                                <button class="btn btn-falcon-default" type="submit" name="action:${addPageURL}" id="${addPageURL}"><i class="fa fa-plus"></i><s:text name="button.add"/></button>
                                </s:if>
                            </s:if>
                            <s:if test='hideDeleteButton.equals("N")'>
                                <s:if test="has_right('delete')">
                                <button class="btn btn-falcon-default" type="submit" name="action:${deleteURL}" id="${deleteURL}" onclick="if (isCheckboxSelected(form.selected)) {
                                            return confirmDelete();
                                        } else {
                                            return false
                                        }
                                        ;"><i class="fa far fa-trash-alt"></i><s:text name="button.delete"/></button>
                                </s:if>
                            </s:if>
                    </div>
                    <div class="col-md-4">
                        <s:if test='showPageSize'>
                            <table style="float:right;">
                                <tr>
                                    <td><label><s:text name="paging.recordPerPage"/>:&nbsp;</label></td>
                                    <td><s:select theme="simple" id="mainPageSize" name="mainPageSize" list="pageSizeOption" listKey="keyData" listValue="valueData" value="%{pageSize}" cssClass="form-control form-control-sm"/></td>
                                </tr>
                            </table>
                        </s:if>
                    </div>
                </div>--%>
                <div class="row">
                    <div class="col-md-12">
                        <jsp:include page="${listPage}.jsp"/>
                    </div>
                </div>     
            </form>
        </s:else>
        <s:if test='isMixConfig'>
            <script language="javascript">
                searchCodeChange(document.getElementById("mainSearchCode").value);
            </script>
        </s:if>

    </div><%--/panel-body--%>
</div><%--/panel panel-default--%><br><br>
<div id='printRptDiv' class='d-none'></div>
<div id="dcItemChangeLoader" class="d-none"></div>
<form id="dcForm" method="post">
    <input type="hidden" id="submit_id" name="id">
</form>
<div id="dcSubmitDiv">
</div>