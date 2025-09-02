<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
    <%--<s:head />--%>
<script type="text/javascript" src="pages/scripts/validation.jsp"></script>
<script type="text/javascript" src="pages/scripts/confirmation.js"></script>
<script type="text/javascript" src="pages/scripts/common.js"></script>
<s:if test='usePopupCalander.equals("Y")'>
<script type="text/javascript" src="include/popcalendar.js"></script>
</s:if>
<s:if test='getSearchFieldLookup().size() > 0'>
    <script type="text/javascript" src="pages/scripts/lookup.js"></script>
    <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
    <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
    <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
    <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />
</s:if>

<script language="javascript">
    var itemValue = {};
    function itemChange(refreshItem, triggerBy, icCode) {
        $("#dcItemChangeLoader").load("itemChangeLoader?itemCate="+icCode+"&itemValue="+$("#search_"+triggerBy).val(),
            function (message) {
                if (message === "Expired") {
                    <%-- it you are using itemChangeLoader, it won't come here, 
                        it only will come here it you are loading sub-item using your on Action (*access right control needed) --%>
                    document.location = "initLogin";
                } else {
                    $("#"+refreshItem+"Div").html(message);
                    $("#itemChangeLoader").html(""); <%-- clear the loader's content --%>
                    $("#itemChangeItem").attr("name", "search_"+refreshItem);
                    $("#itemChangeItem").addClass("form-control sds-dropdown dsrfdd");
                    $("#itemChangeItem").attr("id", "search_"+refreshItem); <%-- change the id at last line --%>
                    $("#search_"+refreshItem).val(itemValue[refreshItem]); <%-- use new id to reload select2 if it is a select2 dropdown --%>
                    $("#search_"+refreshItem).select2(); <%-- use new id to reload select2 if it is a select2 dropdown --%>
                    itemValue[refreshItem] = "";
                }
            });
    }
    function localValidateForm(form) {
	var errors = new Array();
	validateRequired(form, errors);
        <s:property escapeHtml="false" value="pageRequiredWhen"/>
	<%--<s:property escape="false" value="localValidateFormJavascript"/>--%>
	if (errors.length > 0) {
            alert(errors.join('\n'));
            setFocus(form);
	}
	return errors.length > 0 ? false : true;
    }

    function printRpt(printTo_) {
        document.getElementById('searchForm')['printTo_'].value = printTo_;
    }
        
    function resetFields(form) {
        var noOfElements = form.elements.length;
        for (var i = 0; i < noOfElements; i++) {
            if (!(form.elements[i].type == "hidden"
                || form.elements[i].type == "submit"
                || form.elements[i].type == "button")) {
                clearValue(form.elements[i]);
            }
        }
        $(".dsrfdd").val("");
        $(".dsrfdd").select2();
        $(".dsrf").val("");
        setFocus(form);
    }

    function submitForm() {
        if (localValidateForm(document.searchForm)){
            if (document.getElementById("printTo_").value==='pdf') {
                document.searchForm.target = "_blank";
            } else {
                document.searchForm.target = "_self";
            }
            document.searchForm.submit();
            return true;
        } else {
            return false;
        }
    }
    

    //added by wongkk4@05Jan15
    <s:property escapeHtml="false" value="pageJavascript"/>

    function required(){
        <s:property escapeHtml="false" value="pageRequired"/>
    }
    <s:if test='usePopupCalander.equals("Y")'>
    InitCalendar2("images/",false);
    </s:if>
</script>
</head>
<body>
<jsp:include page="/pages/base/b4_actionError.jsp"></jsp:include>
<div class="kt-portlet">
    <div class="kt-portlet__head">
        <div class="kt-portlet__head-label">
            <h3 class="kt-portlet__head-title">
                ${searchDescription}
            </h3>
        </div>
    </div>
    <div class="kt-portlet__body">
        <div class="row">
            <!--left box-->
            <div class="col-lg-8">
                <form id="searchForm" name="searchForm" action="dynamicViewPage" method="POST" onsubmit="return false;">
                <s:iterator value="hiddenFields" var="hiddenField" status="hiddenRowStatus">
                    <input type='hidden' <s:property escapeHtml="true" value="%{#hiddenField}"/> />
                </s:iterator>
                <input type='hidden' name="printTo_" id="printTo_"/>
                <s:hidden theme="simple" name="rptCode" />
                <s:iterator value="searchFields" var="field" status="rowStatus">
                    <s:set var="searchfield_label">${field}_label</s:set>
                    <s:set var="searchfield_">search_${field}</s:set>
                    <s:if test='#field.startsWith("_hidden_")'> <%--Delvene @ 19-May-2015 :: Allow hidden field to be searched--%>
                        <s:hidden theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" />
                        <s:hidden theme="simple" name="search_%{#field}2" value='%{searchFieldsMap.get("search_"+#field)}' />
                    </s:if>
                    <s:else>
                        <div class="form-group row">
                            <div class="col-3 col-form-label">${searchFieldsLabel[rowStatus.index]}<s:if test="isRequiredField(#field)"><font class="asterisk">*</font></s:if></div>
                            <div class="col-9">
                                <s:set var="searchfield_dd_type">${field}_dd_type</s:set>
                                <s:set var="searchfield_dd_key">${field}_dd_key</s:set>
                                <s:set var="searchfield_lookupSearch">${field}_lookupSearch</s:set>
                                <s:set var="searchfield_dd">${field}_dd</s:set>
                                <s:set var="searchfield_dd_list">${field}_dd_list</s:set>
                                <s:set var="searchfield_itemChange">${field}_itemChange</s:set>
                                <s:if test='#field.startsWith("_date_")'>
                                    <s:if test='#field.endsWith("_fromTo")'>
                                        <!--<div class="form-group form-group-default">
                                            <s:textfield cssClass="form-control bootstrapDateRangeDown" theme="simple"/>
                                            <i class="fa fa-calendar form-control-feedback"></i>    
                                        </div>-->
                                        <%--<div class="input-group date">
                                            <div class="input-group-addon">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                            <input class="form-control" id="search_${field.substring(6, (field.length() - 7))}" name="search_${field.substring(6, (field.length() - 7))}" value='<s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/> - <s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>' readonly>
                                            <div class="input-group-addon">
                                                <i class="fa fa-times" id="clearsearch_${field.substring(6, (field.length() - 7))}" ></i>
                                            </div>
                                        </div>
                                        <script type="text/javascript">
                                            $(function() {registerDateRangePicker("search_${field.substring(6, (field.length() - 7))}");});
                                        </script>--%>
                                        <div class='input-group' id='kt_daterangepicker_2'>
                                            <input type='text' class="form-control" placeholder="Select date range" id="search_${field.substring(6, (field.length() - 7))}" name="search_${field.substring(6, (field.length() - 7))}" value='<s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/> - <s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>' readonly />
                                            <div class="input-group-append">
                                                    <span class="input-group-text"><i class="fa fa-calendar-check-o"></i></span>
                                            </div>
                                        </div>
                                        <s:hidden cssClass="dateFrom" theme="simple" id="search_%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/>
                                        <s:hidden cssClass="dateTo" theme="simple" id="search_%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>
                                    </s:if>
                                    <s:else>
                                        <!-- <div class="form-group form-group-default">
                                            <s:textfield cssClass="form-control bootstrapDatePickerDown" theme="simple" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                                            <i class="fa fa-calendar form-control-feedback"></i>    
                                        </div>-->
                                        <!--<div class="input-group date">
                                            <div class="input-group-addon">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                            <s:textfield cssClass="form-control datepickerCls" theme="simple" id="search_%{#field.substring(6)}" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                                        </div>-->
                                        <div class="input-group date">
                                            <s:textfield cssClass="form-control" theme="simple" placeholder="" id="kt_datepicker_2" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                                            <!--<input type="text" class="form-control" readonly placeholder="Select date" id="kt_datepicker_2" />-->
                                            <div class="input-group-append">
                                                <span class="input-group-text">
                                                    <i class="fa fa-calendar-check-o"></i>
                                                </span>
                                            </div>
                                        </div>
                                        <%--s:textfield cssClass="datepick-impian embed input-sm" theme="simple" cssStyle="form-control input-sm" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/--%>
                                    </s:else>
                                </s:if>
                                <s:elseif test='getSearchFieldDD().get(#searchfield_dd_key) != null'>
                                    <div id="${field}Div">
                                    <s:if test='getSearchFieldDD().get(#searchfield_dd_type) != null'>
                                        <s:checkboxlist theme="simple" id="search_%{#field}" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value='%{searchFieldsData[#rowStatus.index].split(",")}' cssClass="form-control"/>
                                    </s:if>
                                    <s:else>
                                        <s:if test="getSearchFieldDD().get(#searchfield_dd_list).equalsIgnoreCase('setupcode')">    <%--Added by Delvene @ 29-Aug-2013--%>
                                            <s:select theme="simple" cssClass="rowText form-control sds-dropdown mySelectBox input-sm" name="search_%{#field}" id="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="code_id" listValue="code_desc" value="%{searchFieldsData[#rowStatus.index]}" />
                                        </s:if>
                                        <s:else>
                                            <s:if test='searchFieldsMap.containsKey(#searchfield_itemChange)'>
                                                <s:select theme="simple" cssClass="dsrfdd rowText form-control sds-dropdown mySelectBox input-sm" name="search_%{#field}" id="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value="%{searchFieldsData[#rowStatus.index]}" onchange='itemChange(%{searchFieldsMap.get(#field+"_itemChange")})'/>
                                            </s:if><s:else>
                                                <s:select theme="simple" cssClass="rowText form-control sds-dropdown mySelectBox input-sm" name="search_%{#field}" id="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value="%{searchFieldsData[#rowStatus.index]}" />
                                            </s:else>
                                        </s:else>
                                    </s:else>
                                    </div>
                                </s:elseif>
                                <s:elseif test='getSearchFieldLookup().get(#searchfield_lookupSearch) != null'>
                                    <div class="input-group">
                                        <s:textfield theme="simple" cssClass="form-control" id="search_%{#field}" name="search_%{#field}" value="%{searchFieldsDataMap[#searchfield_]}" size="30"/>
                                        <span class="input-group-addon" onclick="<s:property value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>"><i class="fa fa-search"></i></span>
                                    </div>
                                    <%--<div class="form-group input-group">
                                        <s:textfield theme="simple" cssClass="form-control myLookupBox input-sm"  name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30"/>
                                        <span class="input-group-btn">
                                            <script language="javascript">
                                                <s:property escapeHtml="false" value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>
                                            </script>
                                        </span>
                                    </div>--%>
                                    <%--s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30" cssClass="form-control input-sm" cssStyle="width: 292px; padding: 1px 3px 1px 3px;"/>
                                     <script language="javascript">
                                        <s:property escape="false" value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>
                                    </script--%>
                                </s:elseif>
                                <s:else>
                                    <input type="text" name="search_${field}" id="search_${field}" value="${searchFieldsData[rowStatus.index]}" size="30" class="rowText form-control input-sm"/>
                                    <%--<s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30" cssClass="rowText form-control input-sm" cssStyle=" width: %{getFieldStyleFormat(#field, 'searchField', '', '')}"/>--%>
                                </s:else>
                                <font class="labelText">${searchFieldsHelperText[field]}</font>    
                            </div>
                        </div>
                    </s:else>
                </s:iterator>
                <div class="input-daterange" id="kt_datepicker_5">
                    <div class="form-group row">
                        <div class="col-3 col-form-label">From Date<font class="asterisk">*</font></div>
                        <div class="col-9">
                            <div class="input-group date">
                                <input type="text" class="form-control" name="start" />
                                <span class="input-group-append">
                                    <span class="input-group-text"><i class="fa fa-calendar-check-o"></i></span>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-3 col-form-label">To Date<font class="asterisk">*</font></div>
                        <div class="col-9">
                            <div class="input-group date">
                                <input type="text" class="form-control" name="end" />
                                <span class="input-group-append">
                                    <span class="input-group-text"><i class="fa fa-calendar-check-o"></i></span>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
                <s:iterator value="moreSearchFields" var="field" status="rowStatus">
                    <s:set var="searchfield_label">${field}_label</s:set>
                    <s:set var="searchfield_">search_${field}</s:set>
                    <s:if test='#field.startsWith("_hidden_")'> <%--Delvene @ 19-May-2015 :: Allow hidden field to be searched--%>
                        <s:hidden theme="simple" name="search_%{#field}" value='%{searchFieldsMap[#searchfield_]}' />
                    </s:if>
                    <s:else>
                        <div class="row moreField">
                            <div class="col-md-4 rowLabel">${searchFieldsMap[searchfield_label]}</div>
                            <div class="col-md-8">
                                <s:set var="searchfield_dd_type">${field}_dd_type</s:set>
                                <s:set var="searchfield_dd_key">${field}_dd_key</s:set>
                                <s:set var="searchfield_lookupSearch">${field}_lookupSearch</s:set>
                                <s:set var="searchfield_dd">${field}_dd</s:set>
                                <s:set var="searchfield_dd_list">${field}_dd_list</s:set>
                                <s:if test='#field.startsWith("_date_")'>
                                    <s:if test='#field.endsWith("_fromTo")'>
                                        <%--<div class="input-group date">
                                            <div class="input-group-addon">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                            <input class="form-control" id="search_${field.substring(6, (field.length() - 7))}" name="search_${field.substring(6, (field.length() - 7))}" value='<s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/> - <s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>' readonly>
                                            <div class="input-group-addon">
                                                <i class="fa fa-times" id="clearsearch_${field.substring(6, (field.length() - 7))}" ></i>
                                            </div>
                                        </div>
                                        <script type="text/javascript">
                                            $(function() {registerDateRangePicker("search_${field.substring(6, (field.length() - 7))}");});
                                        </script>--%>
                                        <div class='input-group' id='kt_daterangepicker_2'>
                                            <input type='text' class="form-control" placeholder="Select date range" id="search_${field.substring(6, (field.length() - 7))}" name="search_${field.substring(6, (field.length() - 7))}" value='<s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/> - <s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>' readonly />
                                            <div class="input-group-append">
                                                <span class="input-group-text"><i class="fa fa-calendar-check-o"></i></span>
                                            </div>
                                        </div>
                                        <s:hidden cssClass="dateFrom" theme="simple" id="search_%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/>
                                        <s:hidden cssClass="dateTo" theme="simple" id="search_%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>
                                    </s:if>
                                    <s:else>
                                        <%--<div class="input-group date">
                                            <div class="input-group-addon">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                            <s:textfield cssClass="form-control datepickerCls" theme="simple" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                                        </div>--%>
                                        <div class="input-group date">
                                            <s:textfield cssClass="form-control" placeholder="Select date" id="kt_datepicker_2" theme="simple" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                                            <!--<input type="text" class="form-control" readonly placeholder="Select date" id="kt_datepicker_2" />-->
                                            <div class="input-group-append">
                                                <span class="input-group-text">
                                                    <i class="fa fa-calendar-check-o"></i>
                                                </span>
                                            </div>
                                        </div>
                                    </s:else>
                                </s:if>
                                <s:elseif test='getSearchFieldDD().get(#searchfield_dd_key) != null'>
                                    <s:if test='getSearchFieldDD().get(#searchfield_dd_type) != null'>
                                        <s:checkboxlist theme="simple" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value="%{searchFieldsData[#rowStatus.index]}" cssClass="form-control"/>
                                    </s:if>
                                    <s:else>
                                        <s:if test="getSearchFieldDD().get(#searchfield_dd_list).equalsIgnoreCase('setupcode')">    <%--Added by Delvene @ 29-Aug-2013--%>
                                            <s:select theme="simple" cssClass="rowText form-control mySelectBox input-sm" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="code_id" listValue="code_desc" value="%{searchFieldsData[#rowStatus.index]}" />
                                        </s:if>
                                        <s:else>
                                            <s:select theme="simple" cssClass="rowText form-control mySelectBox input-sm" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value="%{searchFieldsData[#rowStatus.index]}" />
                                        </s:else>
                                    </s:else>
                                </s:elseif>
                                <s:elseif test='getSearchFieldLookup().get(#searchfield_lookupSearch) != null'>
                                    <div class="input-group">
                                        <s:textfield theme="simple" cssClass="form-control" id="search_%{#field}" name="search_%{#field}" value="%{searchFieldsDataMap[#searchfield_]}" size="30"/>
                                        <span class="input-group-addon" onclick="<s:property value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>"><i class="fa fa-search"></i></span>
                                    </div>
                                    <%--s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30" cssClass="form-control input-sm" cssStyle="width: 292px; padding: 1px 3px 1px 3px;"/>
                                     <script language="javascript">
                                        <s:property escape="false" value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>
                                    </script--%>
                                </s:elseif>
                                <s:else>
                                    <s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsMap[#searchfield_]}" size="30" cssClass="rowText form-control input-sm" cssStyle=" width: %{getFieldStyleFormat(#field, 'searchField', '', '')}"/>
                                </s:else>
                                <font class="labelText">${searchFieldsHelperText[field]}</font>    
                            </div>
                        </div>
                    </s:else>
                </s:iterator>
            </div>
            <!--right box-->
            <div class="col-lg-4 text-right">
                <%--<div class="btn-group">
                    <button type="submit" id="processInsertRptSams4_1_Sa" class="btn btn-primary block-xs dropdown-toggle" data-toggle="dropdown" aria-expanded="false" aria-haspopup="true"><i class="fa fa-gears"></i>Generate<i class="fa fa-caret-down"></i></button>
                    <ul class="dropdown-menu sds-dropdown-menu">
                        <li><a class="btn fa fa-file-pdf-o" onclick="printRpt('pdf'); return submitForm();" href="#">&nbsp;&nbsp;PDF</a></li>
                        <s:if test="has_right('exportToExcel')">
                        <li><a class="btn fa fa-file-excel-o" onclick="printRpt('excel'); return submitForm(); " href="#">&nbsp;&nbsp;Excel</a></li>
                        </s:if>
                        <s:if test="has_right('exportToWord')">
                        <li><a class="btn fa fa-file-word-o" onclick="printRpt('word'); return submitForm(); " href="#">&nbsp;&nbsp;Word</a></li>
                        </s:if>
                    </ul>
                </div>--%>
                <button type="submit" id="processInsertRptSams4_1_Sa" class="btn btn-brand" data-toggle="dropdown" aria-expanded="false" aria-haspopup="true">
                    <i class="fa fa-gears"></i>Generate&nbsp;&nbsp;<i style="font-size:13px" class="fa fa-caret-down"></i>
                </button>
                <div class="dropdown-menu dropdown-menu-right sds-dropdown-menu" x-placement="bottom-end" style="position: absolute; will-change: transform; top: 0px; left: 0px; transform: translate3d(-58px, 38px, 0px);min-width:10rem;padding:0;">
                    <ul class="kt-nav">
                        <li class="kt-nav__item"><a class="kt-nav__link" onclick="printRpt('pdf'); return submitForm();" href="#"><i class="kt-nav__link-icon fa fa-file-pdf-o"></i><span class="kt-nav__link-text">PDF</span></a></li>
                        <s:if test="has_right('exportToExcel')">
                        <li class="kt-nav__item"><a class="kt-nav__link" onclick="printRpt('excel'); return submitForm();" href="#"><i class="kt-nav__link-icon fa fa-file-excel-o"></i><span class="kt-nav__link-text">Excel</span></a></li>
                        </s:if>
                        <s:if test="has_right('exportToWord')">
                        <li class="kt-nav__item"><a class="kt-nav__link" onclick="printRpt('word'); return submitForm();" href="#"><i class="kt-nav__link-icon fa fa-file-word-o"></i><span class="kt-nav__link-text">Word</span></a></li>
                        </s:if>
                    </ul>
                </div>
                <button class="btn btn-outline-brand reset" type="button" onclick="resetFields(this.form)"><i class="fa fa-undo"></i>&nbsp;<s:text name="button.reset"/></button>
                <s:if test="hasMoreSearchField">
                    <button class="btn btn-outline-brand showHide" type="submit" onclick="return showHideMoreField();"><i class="showHideIcon la <s:if test='showHideMoreField_.equals("H")'>la-arrow-down</s:if><s:else>la-arrow-down</s:else> search"></i><s:text name="button.more"/></button>
                    <s:hidden name="showHideMoreField_" id="showHideMoreField_"/>
                </s:if>
                    <%--input type="submit" onclick="return getPageSize('', this.form);" value="<s:text name="button.search"/>" class="btn defaultButton" />
                    <input type="button" value="<s:text name="button.reset"/>" class="btn defaultButton" onclick="resetFields(this.form)"/--%>
            </div>
            </form>
        </div>
    </div>
</div>
<div id="dcItemChangeLoader" class="hidden"></div>
</body>
</html>