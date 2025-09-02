<%@taglib uri="/struts-tags" prefix="s"%>
<%--<s:head />--%>
<%--	<table class="tablePaging" width="100%"><tr>
		<td class="noBorderTd" align="left">
                        <s:if test="(pageNo * pageSize) >= numberOfRows">
				<s:if test="(pageNo - 1) * pageSize + 1 == numberOfRows">
					<s:text name="paging.record2">
						<s:param value="numberOfRows" />
						<s:param value="numberOfRows" />
					</s:text>
				</s:if>
				<s:else>
					<s:text name="paging.record">
						<s:param value="(pageNo - 1) * pageSize + 1" />
						<s:param value="numberOfRows" />
						<s:param value="numberOfRows" />
					</s:text>
				</s:else>
			</s:if>
			<s:if test="pageNo * pageSize < numberOfRows">
				<s:if test="pageNo == 1">
					<s:text name="paging.record">
						<s:param value="pageNo" />
						<s:param value="pageNo * pageSize" />
						<s:param value="numberOfRows" />
					</s:text>
				</s:if>
				<s:else>
					<s:text name="paging.record">
						<s:param value="(pageNo - 1) * pageSize + 1" />
						<s:param value="pageNo * pageSize" />
						<s:param value="numberOfRows" />
					</s:text>
				</s:else>
			</s:if>
		</td>
		<td align="right">
			<s:set var="pageItem" value="pagingItem" />
			<s:set var="firstPage" value="pagingItem.get('First')"/>
			<s:set var="previousPage" value="pagingItem.get('Previous')"/>
			<s:set var="nextPage" value="pagingItem.get('Next')"/>
			<s:set var="lastPage" value="pagingItem.get('Last')"/>
			<s:if test='#firstPage != null'>&nbsp;<a href="_SYNprocessUpdateDBA?sqlstmt=<s:property value="sqlstmt" escapeHtml="true"/>&pageNo=${firstPage}&action=${action}"><s:text name="paging.first"/></a></s:if>
			<s:if test='#previousPage != null'>&nbsp;<a href="_SYNprocessUpdateDBA?sqlstmt=<s:property value="sqlstmt" escapeHtml="true"/>&pageNo=${previousPage}&action=${action}"><s:text name="paging.previous"/></a></s:if>
			<s:iterator
				value='#pageItem.get("items")' var="item">
				<s:if test="#item == pageNo">
						&nbsp;[${item}]
					</s:if>
				<s:if test="#item != pageNo">
                                    &nbsp;<a href="_SYNprocessUpdateDBA?sqlstmt=<s:property value="sqlstmt" escapeHtml="true"/>&pageNo=${item}&action=${action}">${item}</a>
				</s:if>
			</s:iterator>
                        <s:if test='#nextPage != null'>&nbsp;<a href="_SYNprocessUpdateDBA?sqlstmt=<s:property value="sqlstmt" escapeHtml="true"/>&pageNo=${nextPage}&action=${action}"><s:text name="paging.next"/></a></s:if>
                        <s:if test='#lastPage != null'>&nbsp;<a href="_SYNprocessUpdateDBA?sqlstmt=<s:property value="sqlstmt" escapeHtml="true"/>&pageNo=${lastPage}&action=${action}"><s:text name="paging.last"/></a></s:if>
		</td>
	</tr></table>
--%>
<nav aria-label="Page navigation">
  <ul class="pagination">
    <li>
        <s:if test="(getDynamicPageNo(#pageParam) * getDynamicPageSize(#pageParam)) >= getDynamicNumberOfRows(#pageParam)">
            <s:if test="(getDynamicPageNo(#pageParam) - 1) * getDynamicPageSize(#pageParam) + 1 == getDynamicNumberOfRows(#pageParam)">
                    <s:text name="paging.record2">
                            <s:param value="getDynamicNumberOfRows(#pageParam)" />
                            <s:param value="getDynamicNumberOfRows(#pageParam)" />
                    </s:text>
            </s:if>
            <s:else>
                    <s:text name="paging.record">
                            <s:param value="(getDynamicPageNo(#pageParam) - 1) * getDynamicPageSize(#pageParam) + 1" />
                            <s:param value="getDynamicNumberOfRows(#pageParam)" />
                            <s:param value="getDynamicNumberOfRows(#pageParam)" />
                    </s:text>
            </s:else>
    </s:if>
    <s:if test="getDynamicPageNo(#pageParam) * getDynamicPageSize(#pageParam) < getDynamicNumberOfRows(#pageParam)">
        <s:if test="pageNo == 1">
                <s:text name="paging.record">
                        <s:param value="getDynamicPageNo(#pageParam)" />
                        <s:param value="getDynamicPageNo(#pageParam) * getDynamicPageSize(#pageParam)" />
                        <s:param value="getDynamicNumberOfRows(#pageParam)" />c
                </s:text>
        </s:if>
        <s:else>
                <s:text name="paging.record">
                        <s:param value="(getDynamicPageNo(#pageParam) - 1) * getDynamicPageSize(#pageParam) + 1" />
                        <s:param value="getDynamicPageNo(#pageParam) * getDynamicPageSize(#pageParam)" />
                        <s:param value="getDynamicNumberOfRows(#pageParam)" />d
                </s:text>
        </s:else>
    </s:if>
    </li>
        <s:set var="pageItem" value="pagingItem" />
        <s:set var="firstPage" value="pagingItem.get('First')"/>
        <s:set var="previousPage" value="pagingItem.get('Previous')"/>
        <s:set var="nextPage" value="pagingItem.get('Next')"/>
        <s:set var="lastPage" value="pagingItem.get('Last')"/>
        
        <s:if test='#firstPage != null'><li></s:if><s:else><li class="disabled"></s:else><a id="fpId" href='${pagingURL}&pageNo=${firstPage}&action=${action}'  ><s:text name="paging.first"/></a></li>
        <s:if test='#previousPage != null'><li></s:if><s:else><li class="disabled"></s:else><a id="ppId" href='${pagingURL}&pageNo=${previousPage}&pageSize=<s:property value="pageSize" escapeHtml="true" escapeJavaScript="true"/>&action=${action}' aria-label="Previous"><s:text name="paging.previous"/></a></li>
        <s:iterator value='#pageItem.get("items")' var="item">
            <s:if test="#item == pageNo"><li class="active"><a href="#"> &nbsp;${item}</a></li></s:if>
            <s:if test="#item != pageNo">
            <li>&nbsp;<a href='${pagingURL}&pageNo=${item}&pageSize=<s:property value="pageSize" escapeHtml="true" escapeJavaScript="true"/>&action=${action}'>${item}</a></li>
            </s:if>
        </s:iterator>
        <s:if test='#nextPage != null'><li></s:if><s:else><li class="disabled"></s:else><a id="npId" href='${pagingURL}&pageNo=${nextPage}&pageSize=<s:property value="pageSize" escapeHtml="true" escapeJavaScript="true"/>&action=${action}' aria-label="Next"><s:text name="paging.next"/></a></li>
        <s:if test='#lastPage != null'><li></s:if><s:else><li class="disabled"></s:else><a id="lpId" href='${pagingURL}&pageNo=${lastPage}&pageSize=<s:property value="pageSize" escapeHtml="true" escapeJavaScript="true"/>&action=${action}'><s:text name="paging.last"/></a>
  </ul>
</nav>