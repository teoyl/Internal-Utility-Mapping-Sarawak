<%@taglib uri="/struts-tags" prefix="s"%>
<table class="form table borderless" border="0" cellpadding="1" cellspacing="1" width="100%">
    <s:iterator value="searchFields" var="field" status="rowStatus">
        <tr>
            <td <%--class="tdLabel"--%> align="right" width="230" valign="middle"><label class="col-lg-2 control-label"><s:property value="%{searchFieldsLabel[#rowStatus.index]}"/></label>  </td>
            <td align="left"><s:set var="searchfield_dd_key">${field}_dd_key</s:set><s:set var="searchfield_lookupSearch">${field}_lookupSearch</s:set><s:set var="searchfield_dd">${field}_dd</s:set>
                <s:set var="searchfield_dd_list">${field}_dd_list</s:set>  <%--Added by Delvene @ 29-Aug-2013--%>
                <s:set var="searchfield_dd_type">${field}_dd_type</s:set>  <%--Added by Delvene @ 21-Apr-2014--%>
                <s:if test='#field.startsWith("_date_")'>
                    <s:if test='#field.endsWith("_fromTo")'>
                        <s:textfield cssClass="datepick-impian embed input-md" theme="simple" id="%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/>&nbsp;<s:text name="report_to"/>
                        <s:textfield cssClass="datepick-impian embed input-md" theme="simple" id="%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>
                    </s:if>
                    <s:else>
                        <s:textfield cssClass="datepick-impian embed input-md" theme="simple" id="search_%{#field.substring(6)}" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                    </s:else>
                </s:if>
                <s:elseif test='getSearchFieldDD().get(#searchfield_dd_key) != null'>
                    <s:if test='getSearchFieldDD().get(#searchfield_dd_type) != null'>  <%--Added by Delvene @ 21-Apr-2014--%>
                        <s:checkboxlist theme="simple" id="search_%{#field}" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value='%{getData(#field)}' />
                    </s:if>
                    <s:else>
                        <s:if test="getSearchFieldDD().get(#searchfield_dd_list).equalsIgnoreCase('setupcode')">    <%--Added by Delvene @ 29-Aug-2013--%>
                            <s:select onfocus="clearNavi()" theme="simple" cssStyle="width: 322px" id="search_%{#field}" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="code_id" listValue="code_desc" value='%{getData(#field)}' />
                        </s:if>
                        <s:else>
                            <s:select onfocus="clearNavi()" theme="simple" cssStyle="width: 322px" id="search_%{#field}" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value='%{getData(#field)}' />
                    </s:else>
                    </s:else>
                </s:elseif>
                <s:else>
                    <s:textfield onfocus="clearNavi()" theme="simple" id="search_%{#field}" name="search_%{#field}" value="%{getData(#field)}" size="30" cssStyle="width: 322px; padding: 1px 3px 1px 3px;"/>
                </s:else>
            </td>
        </tr>
    </s:iterator>
    <s:if test='showPageSize'>
        <tr>
            <td align="right" width="230" valign="middle"><label class="col-lg-2 control-label"><s:text name="paging.recordPerPage"/></label></td>
            <td align="left">
                <s:select theme="simple" id="pageSize" name="pageSize" list="pageSizeOption" listKey="keyData" listValue="valueData" value="%{pageSize}"/>
            </td>
        </tr>
    </s:if>
    <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
</table>