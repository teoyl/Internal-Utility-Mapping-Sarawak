<%@taglib uri="/struts-tags" prefix="s"%>
    <div class="row">
        <div class="col-md-12">
            <s:iterator value="hiddenFields" var="hiddenField" status="hiddenRowStatus">
                <input type='hidden' <s:property escapeHtml="true" value="%{#hiddenField}"/> />
            </s:iterator>
            <s:hidden theme="simple" name="pageSize" id="searchFormPageSize"/>
            <s:hidden theme="simple" name="action" />
            <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
            <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
            <s:iterator value="searchFields" var="field" status="rowStatus">
                <s:set var="searchfield_label">${field}_label</s:set>
                <s:set var="searchfield_data">search_${field}</s:set>
                <s:if test='#field.startsWith("_hidden_")'> <%--Delvene @ 19-May-2015 :: Allow hidden field to be searched--%>
                    <s:hidden theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" />
                    <s:hidden theme="simple" name="search_%{#field}2" value='%{searchFieldsMap.get("search_"+#field)}' />
                </s:if>
                <s:else>
                    <div class="row">
                        <div class="col-3 col-form-label">${searchFieldsLabel[rowStatus.index]}</div>
                        <div class="col-9">
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
                                    <div class='input-group' id='kt_daterangepicker_2a'>
                                        <input class="form-control" id="search_${field.substring(6, (field.length() - 7))}" name="search_${field.substring(6, (field.length() - 7))}" value='<s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/> - <s:property value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>' readonly>
                                        <div class="input-group-append">
                                                <span class="input-group-text"><i class="fa fa-calendar-check-o"></i></span>
                                        </div>
                                    </div>
                                    <s:hidden cssClass="dateFrom" theme="simple" id="%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/>
                                    <s:hidden cssClass="dateTo" theme="simple" id="%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>
                                    <%--<div class="form-group form-group-default">
                                        <s:textfield cssClass="form-control bootstrapDateRangeDown" theme="simple"/>
                                        <i class="fa fa-calendar form-control-feedback"></i>    
                                    </div>
                                    --%>
                                </s:if>
                                <s:else>
                                    <div class="form-group input-group date">
                                        <!--<div class="input-group-addon">
                                            <i class="fa fa-calendar"></i>
                                        </div>-->
                                        <s:textfield cssClass="form-control lookup_date" theme="simple" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                                        <div class="input-group-append">
                                            <span class="input-group-text">
                                                <i class="fa fa-calendar-check-o"></i>
                                            </span>
                                        </div>
                                    </div>
                                    <%--<div class="form-group form-group-default">
                                        <s:textfield cssClass="form-control bootstrapDatePickerDown" theme="simple" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                                        <i class="fa fa-calendar form-control-feedback"></i>    
                                    </div>--%>
                                    <%--s:textfield cssClass="datepick-impian embed input-sm" theme="simple" cssStyle="form-control input-sm" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/--%>
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
                                <div class="form-group input-group">
                                    <s:textfield theme="simple" cssClass="form-control myLookupBox input-sm"  name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30"/>
                                    <span class="input-group-btn">
                                        <script language="javascript">
                                            <s:property escapeHtml="false" value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>
                                        </script>
                                    </span>
                                </div>
                                <%--s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30" cssClass="form-control input-sm" cssStyle="width: 292px; padding: 1px 3px 1px 3px;"/>
                                 <script language="javascript">
                                    <s:property escape="false" value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>
                                </script--%>
                            </s:elseif>
                            <s:else>
                                <s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30" cssClass="rowText form-control input-sm" cssStyle=" width: %{getFieldStyleFormat(#field, 'searchField', '', '')}"/>
                                <font class="labelText">${searchFieldsHelperText[field]}</font>    
                            </s:else>
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
                    <div class="row lookupMoreField">
                        <div class="col-3 col-form-label">${searchFieldsMap[searchfield_label]}</div>
                        <div class="col-9">
                            <s:set var="searchfield_dd_type">${field}_dd_type</s:set>
                            <s:set var="searchfield_dd_key">${field}_dd_key</s:set>
                            <s:set var="searchfield_lookupSearch">${field}_lookupSearch</s:set>
                            <s:set var="searchfield_dd">${field}_dd</s:set>
                            <s:set var="searchfield_dd_list">${field}_dd_list</s:set>
                            <s:if test='#field.startsWith("_date_")'>
                                <s:if test='#field.endsWith("_fromTo")'>
                                    <div class="form-group form-group-default">
                                        <s:textfield cssClass="form-control bootstrapDateRangeDown" theme="simple"/>
                                        <i class="fa fa-calendar form-control-feedback"></i>    
                                    </div>
                                    <s:hidden cssClass="dateFrom" theme="simple" id="%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/>
                                    <s:hidden cssClass="dateTo" theme="simple" id="%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>
                                </s:if>
                                <s:else>
                                    <div class="form-group form-group-default">
                                        <s:textfield cssClass="form-control bootstrapDatePickerDown" theme="simple" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                                        <i class="fa fa-calendar form-control-feedback"></i>    
                                    </div>
                                    <%--s:textfield cssClass="datepick-impian embed input-sm" theme="simple" cssStyle="form-control input-sm" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/--%>
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
                                <div class="form-group input-group">
                                    <s:textfield theme="simple" cssClass="form-control myLookupBox input-sm"  name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30"/>
                                    <span class="input-group-btn">
                                        <script language="javascript">
                                            <s:property escapeHtml="false" value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>
                                        </script>
                                    </span>
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
    </div>
    <div class="row">
        <div class="col-md-12 text-center">
            <button class="btn btn-brand" type="button" onclick="divSubmitForm('mSearchLookup', 'mSearchLookup', 'lookupModal');"><i class="fa fa-search"></i>&nbsp;<s:text name="button.search"/></button>
            <button class="btn btn-outline-brand reset" type="button" onclick="resetFields(this.form)"><i class="fa fa-undo"></i>&nbsp;<s:text name="button.reset"/></button>
            <s:if test="hasMoreSearchField">
                <button class="btn btn-brand showHide" type="button" onclick="return showHideLookupMoreField();"><i class="showHideIcon la <s:if test='showHideLookupMoreField_.equals("H")'>la-arrow-down</s:if><s:else>la-arrow-down</s:else> search"></i><s:text name="button.more"/></button>
            </s:if>
                <%--input type="submit" onclick="return getPageSize('', this.form);" value="<s:text name="button.search"/>" class="btn defaultButton" />
                <input type="button" value="<s:text name="button.reset"/>" class="btn defaultButton" onclick="resetFields(this.form)"/--%>
        </div>
    </div>
    <jsp:include page="/pages/base/b4_actionError.jsp"></jsp:include>
    <script src="include/assets/js/pages/crud/forms/widgets/bootstrap-datepicker.js" type="text/javascript"></script>
    <script src="include/assets/js/pages/crud/forms/widgets/bootstrap-daterangepicker.js" type="text/javascript"></script>