<%@taglib uri="/struts-tags" prefix="s"%>
<s:set var="searchParam">${param.searchingParam}</s:set>
<s:set var="fParam">${param.functionParam}</s:set>
    <script language="javascript">
        function getSearch2PageSize(fieldType) {
    <s:if test='showPageSize'>
            document.getElementById('${searchParam}_pageSize').value = document.getElementById('search2PageSize').value;
    </s:if>
            return true;
        }

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
</script>
<%--Uncommented by Delvene @ 21-Oct-2013--%>
<s:iterator value="getDynamicHiddenFields(list_param)" var="hiddenField" status="hiddenRowStatus">
    <input type='hidden' <s:property escapeHtml="true" value="%{#hiddenField}"/> />
</s:iterator>
<%--Uncommented by Delvene @ 21-Oct-2013 - END--%>
<%--<s:iterator value="getDynamicSearchField(list_param)" var="field" status="rowStatus">
    <tr>
        <td class="tdLabel" align="right" width="150"><s:property value="%{getDynamicSearchLabel(list_param)[#rowStatus.index]}"/></td>
        <td align="left" width="50">
            <s:textfield theme="simple" name="search_%{#field}" value="%{getDynamicSearchData(list_param)[#rowStatus.index]}" size="30"/>
        </td>
    </tr>
</s:iterator>--%>

<s:iterator value="getDynamicSearchField(list_param)" var="field" status="rowStatus">
    <s:if test='#field.startsWith("_hidden_")'> <%--Delvene @ 19-May-2015 :: Allow hidden field to be searched--%>
        <s:hidden theme="simple" name="search_%{#field}" value="%{getDynamicSearchData(list_param)[#rowStatus.index]}" />
    </s:if>
    <s:else>
        <div class="form-group row">
            <div class="col-3 col-form-label"><s:property value="%{getDynamicSearchLabel(list_param)[#rowStatus.index]}"/></div>
            <div class="col-9">
                <s:set var="searchfield_dd_type">${field}_dd_type</s:set><s:set var="searchfield_dd_key">${field}_dd_key</s:set><s:set var="searchfield_lookupSearch">${field}_lookupSearch</s:set><s:set var="searchfield_dd">${field}_dd</s:set>
                <s:set var="searchfield_dd_list">${field}_dd_list</s:set>  <%--Added by Delvene @ 29-Aug-2013--%>
                <s:set var="ddMap" value="getDynamicSearchFieldDD(list_param)"></s:set>
                <s:set var="lookupMap" value="getDynamicSearchFieldLookup(list_param)"></s:set>
                <s:if test='#field.startsWith("_date_")'>
                    <s:if test='#field.endsWith("_fromTo")'>
                        <s:textfield readonly="true" theme="simple" name="search_%{#field.substring(6, (#field.length() - 7))}From" size="26" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}' cssClass="input-md form-control"/>
                        <img alt="" src="images/calendar.gif" id="imgCalTo1" style="" onclick="popUpCalendar(this, document.getElementById('search_<s:property value='%{extractSearchFieldForDate(#field)}'/>From'), '<s:text name="date_default_date_popup" />')"   title="Calendar" align="absmiddle" height="18" width="18">&nbsp;<s:text name="report_to"/>
                        <s:textfield readonly="true" theme="simple" name="search_%{#field.substring(6, (#field.length() - 7))}To" size="26" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}' cssClass="input-md form-control"/>
                        <img alt="" src="images/calendar.gif" id="imgCalTo1" style="" onclick="popUpCalendar(this, document.getElementById('search_<s:property value='%{extractSearchFieldForDate(#field)}'/>To'), '<s:text name="date_default_date_popup" />')"   title="Calendar" align="absmiddle" height="18" width="18">
                    </s:if>
                    <s:else>
                        <s:textfield readonly="true" theme="simple" name="search_%{#field.substring(6)}" size="26" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}' cssClass="input-md form-control"/>
                        <img alt="" src="images/calendar.gif" id="imgCalTo1" style="" onclick="popUpCalendar(this, document.getElementById('search_<s:property value='%{extractSearchFieldForDate(#field)}'/>'), '<s:text name="date_default_date_popup" />')"   title="Calendar" align="absmiddle" height="18" width="18">
                    </s:else>
                </s:if>
                <s:elseif test='#ddMap.get(#searchfield_dd_key) != null'>
                    <s:if test='#ddMap.get(#searchfield_dd_type) != null'>
                        <s:checkboxlist theme="simple" name="search_%{#field}" list="#ddMap.get(#searchfield_dd)" listKey="keyData" listValue="valueData" value="%{getDynamicSearchData(list_param)[#rowStatus.index]}" />
                    </s:if>
                    <s:else>
                        <s:if test="#ddMap.get(#searchfield_dd_list).equalsIgnoreCase('setupcode')">
                            <s:select theme="simple"  cssClass="input-md form-control"  name="search_%{#field}" list="#ddMap.get(#searchfield_dd)" listKey="code_id" listValue="code_desc" value="%{getDynamicSearchData(list_param)[#rowStatus.index]}" />
                        </s:if>
                        <s:else>
                            <s:select theme="simple" cssClass="input-md form-control" name="search_%{#field}" list="#ddMap.get(#searchfield_dd)" listKey="keyData" listValue="valueData" value="%{getDynamicSearchData(list_param)[#rowStatus.index]}" />
                        </s:else>
                    </s:else>
                </s:elseif>
                <s:elseif test='#lookupMap.get(#searchfield_lookupSearch) != null'>
                    <s:textfield theme="simple" name="search_%{#field}" value="%{getDynamicSearchData(list_param)[#rowStatus.index]}" size="30" cssStyle="padding: 1px 3px 1px 3px; %{getFieldStyleFormat(#field, 'searchField', '')" cssClass="input-md form-control"/>
                    <script language="javascript">
                        <s:property escapeHtml="false" value="%{getDynamicSearchFieldLookup(list_param).get(#searchfield_lookupSearch)}"/>
                    </script>
                </s:elseif>
                <s:else>
                    <s:textfield theme="simple" name="search_%{#field}" value="%{getDynamicSearchData(list_param)[#rowStatus.index]}" size="30" cssStyle="padding: 1px 3px 1px 3px; %{getFieldStyleFormat(#field, 'searchField', '')" cssClass="input-md form-control input-xs"/>
                </s:else>
                ${searchFieldsHelperText[field]}
            </div>
        </div>
    </s:else>
</s:iterator>
