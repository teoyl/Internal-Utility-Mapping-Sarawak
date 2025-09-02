<%@taglib uri="/struts-tags" prefix="s"%>
	<s:set var="pageParam">${param.searchingParam}</s:set>
	<s:set var="urlParam" value="getDynamicPagingURL(#pageParam)" />
	<s:set var="dynamicSearchParam" value="getDynamicSearchCondition(#pageParam)" />
	<table class="tablePaging" width="100%"><tr>
		<td align="left">
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
						<s:param value="getDynamicNumberOfRows(#pageParam)" />
					</s:text>
				</s:if>
				<s:else>
					<s:text name="paging.record">
						<s:param value="(getDynamicPageNo(#pageParam) - 1) * getDynamicPageSize(#pageParam) + 1" />
						<s:param value="getDynamicPageNo(#pageParam) * getDynamicPageSize(#pageParam)" />
						<s:param value="getDynamicNumberOfRows(#pageParam)" />
					</s:text>
				</s:else>
			</s:if>
		</td>
		<td align="right">
			<s:iterator value="getDynamicPagingItem(#pageParam).items" var="item">
				<s:if test="#item == getDynamicPageNo(#pageParam)">
					[${item}]
				</s:if>
				
				<s:if test="#item != getDynamicPageNo(#pageParam)">
                                        <a href='<s:property value="%{#urlParam}" escapeHtml="true" escapeJavaScript="true"/>?${pageParam}_pageNo=${item}&action=<s:property value="%{action}" escapeHtml="true" escapeJavaScript="true"/><s:property value="%{#dynamicSearchParam}" escapeHtml="true" escapeJavaScript="true"/>' >${item}</a>
				</s:if>
			</s:iterator>
		</td>
	</tr></table>
