<%@taglib uri="/struts-tags" prefix="s"%>
<s:set name="searchParam">${param.searchingParam}</s:set>
<s:set name="fParam">${param.functionParam}</s:set>

<form id="sortForm" theme="simple" action="${fParam}" >
    <s:iterator value="getDynamicSearchField(#searchParam)" var="field" status="rowStatus">
        <s:hidden theme="simple" name="search_%{#field}" value="%{getDynamicSearchData(#searchParam)[#rowStatus.index]}" />
    </s:iterator>
    <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
    <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
    <s:hidden theme="simple" name="action"/>
    <s:if test="getDynamicResult(#searchParam).size() > 0">
        <s:hidden theme="simple" name="listSize" value="1"/>
    </s:if>
    <s:else>
        <s:hidden theme="simple" name="listSize" value="0"/>
    </s:else>
</form>