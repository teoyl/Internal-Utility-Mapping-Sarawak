<%@taglib uri="/struts-tags" prefix="s"%>
	<s:set var="pageParam">${param.searchingParam}</s:set>
	<s:set var="urlParam" value="getDynamicPagingURL(#pageParam)" />
	<s:set var="dynamicSearchParam" value="getDynamicSearchCondition(#pageParam)" />
	<%--<table class="tablePaging" width="100%"><tr>
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
					<a href='${urlParam}?${pageParam}_pageNo=${item}&action=${action}${dynamicSearchParam}' >${item}</a>
				</s:if>
			</s:iterator>
		</td>
	</tr></table>--%>
    
    <!--begin: Pagination-->
    <div class="kt-pagination  kt-pagination--brand">
        <ul class="kt-pagination__links">
            <s:set var="pageItem" value="getDynamicPagingItem(#pageParam)" />
            <s:set var="firstPage" value="getDynamicPagingItem(#pageParam).get('First')"/>
            <s:set var="previousPage" value="getDynamicPagingItem(#pageParam).get('Previous')"/>
            <s:set var="nextPage" value="getDynamicPagingItem(#pageParam).get('Next')"/>
            <s:set var="lastPage" value="getDynamicPagingItem(#pageParam).get('Last')"/>

            <s:if test='#firstPage != null'>
                <li class="kt-pagination__link--first">
                    <a id="fpId" href='${urlParam}?${pageParam}_pageNo=${firstPage}&action=${action}${dynamicSearchParam}'><i class="fa fa-angle-double-left kt-font-brand"></i></a>
                </li>
            </s:if>
            <s:if test='#previousPage != null'>
                <li class="kt-pagination__link--next">
                    <a id="ppId" href='${urlParam}?${pageParam}_pageNo=${previousPage}&action=${action}${dynamicSearchParam}' aria-label="Previous"><i class="fa fa-angle-left kt-font-brand"></i></a>
                </li>
            </s:if>
            <s:iterator value="getDynamicPagingItem(#pageParam).items" var="item">
                <s:if test="#item == getDynamicPageNo(#pageParam)">
                    <li class="kt-pagination__link--active" aria-current="page"><a href="#"> &nbsp;${item}</a></li>
                </s:if>
                <s:if test="#item != getDynamicPageNo(#pageParam)">
                    <li><a href='${urlParam}?${pageParam}_pageNo=${item}&action=${action}${dynamicSearchParam}' >${item}</a></li>
                </s:if>
            </s:iterator>
            <s:if test='#nextPage != null'>
                <li class="kt-pagination__link--prev">
                    <a id="npId" href='${urlParam}?${pageParam}_pageNo=${nextPage}&action=${action}${dynamicSearchParam}' aria-label="Next"><i class="fa fa-angle-right kt-font-brand"></i></a>
                </li>   
            </s:if>
            <s:if test='#lastPage != null'>
                <li class="kt-pagination__link--last">
                    <a id="lpId" href='${urlParam}?${pageParam}_pageNo=${lastPage}&action=${action}${dynamicSearchParam}'><i class="fa fa-angle-double-right kt-font-brand"></i>
                    </a>
                </li>
            </s:if>
        </ul>
        <div class="kt-pagination__toolbar">
            <span class="pagination__desc">
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
            </span>
        </div>
    </div>
    <!--end: Pagination-->

