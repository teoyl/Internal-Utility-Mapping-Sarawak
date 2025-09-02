<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
    <%--<s:head />--%>
<s:if test='usePopupCalander.equals("Y")'>
<script type="text/javascript" src="include/popcalendar.js"></script>
</s:if>

<script language="javascript">
    $(document).ready(function() {
       initDatePicker(); 
    });
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
                    $("#itemChangeItem").addClass("form-control form-control-sm sds-dropdown dsrfdd");
                    $("#itemChangeItem").attr("id", "search_"+refreshItem); <%-- change the id at last line --%>
                    $("#search_"+refreshItem).val(itemValue[refreshItem]); <%-- use new id to reload select2 if it is a select2 dropdown --%>
                    var opt = {
                        theme: 'bootstrap-5'
                    };
                    if ($("#search_"+refreshItem).hasClass('form-control-sm')) {
                        opt.selectionCssClass = 'select2--small';
                        opt.dropdownCssClass = 'select2--small';
                    }
                    var options = $.extend(opt, $("#search_"+refreshItem).data('options'));
    
                    $("#search_"+refreshItem).select2(options); <%-- use new id to reload select2 if it is a select2 dropdown --%>
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

    function printRpt(printTo_) { console.log('printRpt');
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
//        $(".dsrfdd").select2();
        $(".dsrf").val("");
        setFocus(form);
    }

    function submitForm2() {
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
<jsp:include page="/pages/base/actionError.jsp"></jsp:include>
<div class="card">
    <div class="card-header bg-light">
        <h5>${searchDescription}</h5>
    </div>
    <div class="card-body">
        <div class="row">
            <!--left box-->
            <form id="searchForm" name="searchForm" action="dynamicViewPage" method="POST" onsubmit="return false;">
                <div class="row">
                    <div class="col-md-8">
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
                                <div class="row mb-1" id="${field}_row">
                                    <div class="col-md-4 col-form-label col-form-label-sm">${searchFieldsLabel[rowStatus.index]}<s:if test="isRequiredField(#field)"><font class="asterisk">*</font></s:if></div>
                                    <div class="col-md-8">
                                        <s:set var="searchfield_dd_type">${field}_dd_type</s:set>
                                        <s:set var="searchfield_dd_key">${field}_dd_key</s:set>
                                        <s:set var="searchfield_lookupSearch">${field}_lookupSearch</s:set>
                                        <s:set var="searchfield_dd">${field}_dd</s:set>
                                        <s:set var="searchfield_dd_list">${field}_dd_list</s:set>
                                        <s:set var="searchfield_itemChange">${field}_itemChange</s:set>
                                        <s:set var="searchfield_ddMultiple">${field}_ddMultiple</s:set>
                                        <s:if test='#field.startsWith("_date_")'>
                                            <s:if test='#field.endsWith("_fromTo")'>
                <!--                                                <div class="form-group form-group-default">
                                                    <s:textfield cssClass="form-control form-control-sm bootstrapDateRangeDown" theme="simple"/>
                                                    <i class="fa fa-calendar form-control form-control-sm-feedback"></i>    
                                                </div>-->
                                                <div class="input-group input-group-sm" id="search_${field.substring(6, (field.length() - 7))}_DateRange">
                                                    <div class="input-group-text input-button" data-toggle>
                                                        <i class="far fa-calendar-alt"></i>
                                                    </div>
                                                    <input class="form-control form-control-sm" id="search_${field.substring(6, (field.length() - 7))}" name="search_${field.substring(6, (field.length() - 7))}" value='<s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/> - <s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>' readonly data-input>
                                                    <div class="input-group-text input-button" data-clear>
                                                        <i class="fa fa-times" ></i>
                                                    </div>
                                                </div>
                                                <script type="text/javascript">
                                                    $(function() {registerDateRangePickerWithPreloading("search_${field.substring(6, (field.length() - 7))}");});
                                                </script>
                                                <s:hidden cssClass="dateFrom" theme="simple" id="search_%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/>
                                                <s:hidden cssClass="dateTo" theme="simple" id="search_%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>
                                            </s:if>
                                            <s:else>
                <!--                                                <div class="form-group form-group-default">
                                                    <s:textfield cssClass="form-control form-control-sm bootstrapDatePickerDown" theme="simple" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                                                    <i class="fa fa-calendar form-control form-control-sm-feedback"></i>    
                                                </div>-->
                                                <div class="input-group input-group-sm datepicker">
                                                    <div class="input-group-text input-button" data-toggle>
                                                        <i class="far fa-calendar-alt"></i>
                                                    </div>
                                                    <input type="text" class="form-control form-control-sm" id="search_${field.substring(6)}" name="search_${field.substring(6)}" value="${searchFieldsDateData.get("search_"+extractSearchFieldForDate(field))}" data-input/>
                                                </div>
                                                <%--s:textfield cssClass="datepick-impian embed input-sm" theme="simple" cssStyle="form-control form-control-sm input-sm" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/--%>
                                            </s:else>
                                        </s:if>
                                        <s:elseif test='getSearchFieldDD().get(#searchfield_dd_key) != null'>
                                            <div id="${field}Div">
                                            <s:if test='getSearchFieldDD().get(#searchfield_dd_type) != null'>
                                                <s:checkboxlist theme="simple" id="search_%{#field}" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value='%{searchFieldsData[#rowStatus.index].split(",")}' cssClass="form-control form-control-sm"/>
                                            </s:if>
                                            <s:else>
                                                <s:if test="getSearchFieldDD().get(#searchfield_dd_list).equalsIgnoreCase('setupcode')">    <%--Added by Delvene @ 29-Aug-2013--%>
                                                    <s:select theme="simple" cssClass="rowText form-control form-control-sm sds-dropdown mySelectBox input-sm" name="search_%{#field}" id="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="code_id" listValue="code_desc" value="%{searchFieldsData[#rowStatus.index]}" />
                                                </s:if>
                                                <s:else>
                                                    <s:if test='searchFieldsMap.containsKey(#searchfield_itemChange)'>
                                                        <s:select theme="simple" cssClass="dsrfdd rowText form-control form-control-sm sds-dropdown mySelectBox input-sm" name="search_%{#field}" id="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value="%{searchFieldsData[#rowStatus.index]}" onchange='itemChange(this)'/>
                                                    </s:if><s:elseif test='searchFieldsMap.containsKey(#searchfield_ddMultiple)'>
                                                        <s:select theme="simple" cssClass="rowText form-control form-control-sm sds-dropdown mySelectBox input-sm" name="search_%{#field}" id="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value="%{searchFieldsData[#rowStatus.index]}" multiple="true"/>
                                                    </s:elseif><s:else>
                                                        <s:select theme="simple" cssClass="rowText form-control form-control-sm sds-dropdown mySelectBox input-sm" name="search_%{#field}" id="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value="%{searchFieldsData[#rowStatus.index]}" />
                                                    </s:else>
                                                </s:else>
                                            </s:else>
                                            </div>
                                        </s:elseif>
                                        <s:elseif test='getSearchFieldLookup().get(#searchfield_lookupSearch) != null'>
                                            <div class="input-group input-group-sm">
                                                <s:textfield theme="simple" cssClass="form-control form-control-sm" id="search_%{#field}" name="search_%{#field}" value="%{searchFieldsDataMap[#searchfield_]}" size="30"/>
                                                <span class="input-group-text" onclick="<s:property value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>"><i class="fa fa-search"></i></span>
                                            </div>
                                            <%--<div class="form-group input-group">
                                                <s:textfield theme="simple" cssClass="form-control form-control-sm myLookupBox input-sm"  name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30"/>
                                                <span class="input-group-btn">
                                                    <script language="javascript">
                                                        <s:property escapeHtml="false" value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>
                                                    </script>
                                                </span>
                                            </div>--%>
                                            <%--s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30" cssClass="form-control form-control-sm input-sm" cssStyle="width: 292px; padding: 1px 3px 1px 3px;"/>
                                             <script language="javascript">
                                                <s:property escape="false" value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>
                                            </script--%>
                                        </s:elseif>
                                        <s:else>
                                            <input type="text" name="search_${field}" id="search_${field}" value="${searchFieldsData[rowStatus.index]}" size="30" class="rowText form-control form-control-sm input-sm"/>
                                            <%--<s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30" cssClass="rowText form-control form-control-sm input-sm" cssStyle=" width: %{getFieldStyleFormat(#field, 'searchField', '', '')}"/>--%>
                                        </s:else>
                                        <div class="form-text">${searchFieldsHelperText[field]}</div>    
                                    </div>
                                </div>
                            </s:else>
                        </s:iterator>
                        <s:iterator value="moreSearchFields" var="field" status="rowStatus">
                            <s:set var="searchfield_label">${field}_label</s:set>
                            <s:set var="searchfield_">search_${field}</s:set>
                            <s:if test='#field.startsWith("_hidden_")'> <%--Delvene @ 19-May-2015 :: Allow hidden field to be searched--%>
                                <s:hidden theme="simple" name="search_%{#field}" value='%{searchFieldsMap[#searchfield_]}' />
                            </s:if>
                            <s:else>
                                <div class="row moreField">
                                    <div class="col-md-4 col-form-label col-form-label-sm">${searchFieldsMap[searchfield_label]}</div>
                                    <div class="col-md-8">
                                        <s:set var="searchfield_dd_type">${field}_dd_type</s:set>
                                        <s:set var="searchfield_dd_key">${field}_dd_key</s:set>
                                        <s:set var="searchfield_lookupSearch">${field}_lookupSearch</s:set>
                                        <s:set var="searchfield_dd">${field}_dd</s:set>
                                        <s:set var="searchfield_dd_list">${field}_dd_list</s:set>
                                        <s:if test='#field.startsWith("_date_")'>
                                            <s:if test='#field.endsWith("_fromTo")'>
                                                <div class="input-group input-group-sm" id="search_${field.substring(6, (field.length() - 7))}_DateRange">
                                                    <div class="input-group-text input-button" data-toggle>
                                                        <i class="far fa-calendar-alt"></i>
                                                    </div>
                                                    <input class="form-control form-control-sm" id="search_${field.substring(6, (field.length() - 7))}" name="search_${field.substring(6, (field.length() - 7))}" value='<s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/> - <s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>' readonly data-input>
                                                    <div class="input-group-text input-button" data-clear>
                                                        <i class="fa fa-times"></i>
                                                    </div>
                                                </div>
                                                <script type="text/javascript">
                                                    $(function() {registerDateRangePicker("search_${field.substring(6, (field.length() - 7))}");});
                                                </script>
                                                <s:hidden cssClass="dateFrom" theme="simple" id="search_%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/>
                                                <s:hidden cssClass="dateTo" theme="simple" id="search_%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>
                                            </s:if>
                                            <s:else>
                                                <div class="input-group input-group-sm datepicker">
                                                    <div class="input-group-text input-button" data-toggle>
                                                        <i class="far fa-calendar-alt"></i>
                                                    </div>
                                                    <s:textfield cssClass="form-control form-control-sm" theme="simple" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                                                </div>
                                            </s:else>
                                        </s:if>
                                        <s:elseif test='getSearchFieldDD().get(#searchfield_dd_key) != null'>
                                            <s:if test='getSearchFieldDD().get(#searchfield_dd_type) != null'>
                                                <s:checkboxlist theme="simple" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value="%{searchFieldsData[#rowStatus.index]}" cssClass="form-control form-control-sm"/>
                                            </s:if>
                                            <s:else>
                                                <s:if test="getSearchFieldDD().get(#searchfield_dd_list).equalsIgnoreCase('setupcode')">    <%--Added by Delvene @ 29-Aug-2013--%>
                                                    <s:select theme="simple" cssClass="rowText form-control form-control-sm mySelectBox input-sm" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="code_id" listValue="code_desc" value="%{searchFieldsData[#rowStatus.index]}" />
                                                </s:if>
                                                <s:else>
                                                    <s:select theme="simple" cssClass="rowText form-control form-control-sm mySelectBox input-sm" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value="%{searchFieldsData[#rowStatus.index]}" />
                                                </s:else>
                                            </s:else>
                                        </s:elseif>
                                        <s:elseif test='getSearchFieldLookup().get(#searchfield_lookupSearch) != null'>
                                            <div class="input-group input-group-sm">
                                                <s:textfield theme="simple" cssClass="form-control form-control-sm" id="search_%{#field}" name="search_%{#field}" value="%{searchFieldsDataMap[#searchfield_]}" size="30"/>
                                                <span class="input-group-text" onclick="<s:property value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>"><i class="fa fa-search"></i></span>
                                            </div>
                                            <%--s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30" cssClass="form-control form-control-sm input-sm" cssStyle="width: 292px; padding: 1px 3px 1px 3px;"/>
                                             <script language="javascript">
                                                <s:property escape="false" value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>
                                            </script--%>
                                        </s:elseif>
                                        <s:else>
                                            <s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsMap[#searchfield_]}" size="30" cssClass="rowText form-control form-control-sm input-sm" cssStyle=" width: %{getFieldStyleFormat(#field, 'searchField', '', '')}"/>
                                        </s:else>
                                        <div class="form-text">${searchFieldsHelperText[field]}</div>    
                                    </div>
                                </div>
                            </s:else>
                        </s:iterator>
                    </div>
                    <!--right box-->
                    <div class="col-md-4 text-end">
                        <div class="dropdown d-inline">
                            <button type="submit" id="processInsertRptSams4_1_Sa" class="btn btn-sm btn-primary dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" aria-haspopup="true"><i class="fas fa-cogs"></i><span class="ms-1">Generate</span></button>
                            <div class="dropdown-menu">
                                <a class="dropdown-item" onclick="printRpt('pdf'); return submitForm2();" href="#"><i class="far fa-file-pdf"></i>&nbsp;&nbsp;PDF</a>
                                <s:if test="has_right('exportToExcel')">
                                <a class="dropdown-item" onclick="printRpt('excel'); return submitForm2(); " href="#"><i class="fa fa-file-excel-o"></i>&nbsp;&nbsp;Excel</a>
                                </s:if>
                                <s:if test="has_right('exportToWord')">
                                <a class="dropdown-item" onclick="printRpt('word'); return submitForm2(); " href="#"><i class="fa fa-file-word-o"></i>&nbsp;&nbsp;Word</a>
                                </s:if>
                            </div>
                        </div>
                        <button class="btn btn-sm btn-falcon-default reset" type="button" onclick="resetFields(this.form)"><i class="fa fa-undo"></i>&nbsp;<s:text name="button.reset"/></button>
                        <s:if test="hasMoreSearchField">
                            <button class="btn btn-default showHide" type="submit" onclick="return showHideMoreField();"><i class="showHideIcon fa <s:if test='showHideMoreField_.equals("H")'>fa-arrow-down</s:if><s:else>fa-arrow-down</s:else> search"></i><s:text name="button.more"/></button>
                            <s:hidden name="showHideMoreField_" id="showHideMoreField_"/>
                        </s:if>
                            <%--input type="submit" onclick="return getPageSize('', this.form);" value="<s:text name="button.search"/>" class="btn defaultButton" />
                            <input type="button" value="<s:text name="button.reset"/>" class="btn defaultButton" onclick="resetFields(this.form)"/--%>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<div id="dcItemChangeLoader" class="hidden"></div>
</body>
</html>