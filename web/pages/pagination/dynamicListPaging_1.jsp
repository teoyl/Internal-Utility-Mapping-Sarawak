<%@taglib uri="/struts-tags" prefix="s"%>
	<s:set name="pageParam">${param.searchingParam}</s:set>
	<s:set name="urlParam" value="getDynamicPagingURL(#pageParam)" />
	<s:set name="dynamicSearchParam" value="getDynamicSearchCondition(#pageParam)" />
<%--	<table class="tablePaging" width="100%"><tr>
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
                    <s:set name="pageItem" value="getDynamicPagingItem(#pageParam)" />
                    <s:set name="firstPage" value="getDynamicPagingItem(#pageParam).get('First')"/>
                    <s:set name="previousPage" value="getDynamicPagingItem(#pageParam).get('Previous')"/>
                    <s:set name="nextPage" value="getDynamicPagingItem(#pageParam).get('Next')"/>
                    <s:set name="lastPage" value="getDynamicPagingItem(#pageParam).get('Last')"/>
                    <s:if test='#firstPage != null'>&nbsp;<a href='${urlParam}?${pageParam}_pageNo=${firstPage}&${pageParam}_pageSize=${pageSize}&action=${action}${dynamicSearchParam}'>First</a></s:if>
                    <s:if test='#previousPage != null'>&nbsp;<a href='${urlParam}?${pageParam}_pageNo=${previousPage}&${pageParam}_pageSize=${pageSize}&action=${action}${dynamicSearchParam}'>Previous</a></s:if>
                    <s:iterator value='getDynamicPagingItem(#pageParam).get("items")' var="item">
                            <s:if test="#item == getDynamicPageNo(#pageParam)">
                                    [${item}]
                            </s:if>

                            <s:if test="#item != getDynamicPageNo(#pageParam)">
                                    <a href='${urlParam}?${pageParam}_pageNo=${item}&${pageParam}_pageSize=${pageSize}&action=${action}${dynamicSearchParam}' >${item}</a>
                            </s:if>
                    </s:iterator>
                    <s:if test='#nextPage != null'>&nbsp;<a href='${urlParam}?${pageParam}_pageNo=${nextPage}&${pageParam}_pageSize=${pageSize}&action=${action}${dynamicSearchParam}'>Next</a></s:if>
                    <s:if test='#lastPage != null'>&nbsp;<a href='${urlParam}?${pageParam}_pageNo=${lastPage}&${pageParam}_pageSize=${pageSize}&action=${action}${dynamicSearchParam}'>Last</a></s:if>
		</td>
	</tr></table>--%>
<div class="row">
    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-9" id="actions-box">  
        <ul class="pagination">
            <s:set name="pageItem" value="getDynamicPagingItem(#pageParam)" />
            <s:set name="firstPage" value="getDynamicPagingItem(#pageParam).get('First')"/>
            <s:set name="previousPage" value="getDynamicPagingItem(#pageParam).get('Previous')"/>
            <s:set name="nextPage" value="getDynamicPagingItem(#pageParam).get('Next')"/>
            <s:set name="lastPage" value="getDynamicPagingItem(#pageParam).get('Last')"/>
            
            <s:if test='#firstPage != null'>
                <li>
                    <a href='${urlParam}?${pageParam}_pageNo=${firstPage}&${pageParam}_pageSize=${pageSize}&action=${action}${dynamicSearchParam}'><s:text name="paging.first"/></a>
                </li>
            </s:if>
            <s:if test='#previousPage != null'>
                <li>
                    <a href='${urlParam}?${pageParam}_pageNo=${previousPage}&${pageParam}_pageSize=${pageSize}&action=${action}${dynamicSearchParam}'><s:text name="paging.previous"/></a>
                </li>
            </s:if>
            <s:iterator value='getDynamicPagingItem(#pageParam).get("items")' var="item">
                <s:if test="#item == getDynamicPageNo(#pageParam)">
                    <li class="active"><a href="#"> &nbsp;${item}</a></li>
                    </s:if>
                    <s:if test="#item != getDynamicPageNo(#pageParam)">
                    <li>&nbsp;<a href='${urlParam}?${pageParam}_pageNo=${item}&${pageParam}_pageSize=${pageSize}&action=${action}${dynamicSearchParam}' >${item}</a></li>
                    </s:if>
                </s:iterator>
                <s:if test='#nextPage != null'>
                <li>
                    <a href='${urlParam}?${pageParam}_pageNo=${nextPage}&${pageParam}_pageSize=${pageSize}&action=${action}${dynamicSearchParam}'><s:text name="paging.next"/></a>
                </li>
            </s:if>
            <s:if test='#lastPage != null'>
                <li>
                    <a href='${urlParam}?${pageParam}_pageNo=${lastPage}&${pageParam}_pageSize=${pageSize}&action=${action}${dynamicSearchParam}'><s:text name="paging.last"/></a>
                </li>
            </s:if>
        </ul>     
    </div>

    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-3  text-right">
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
    </div>
</div>

<%--
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
    </li>
        <s:set name="pageItem" value="pagingItem" />
        <s:set name="firstPage" value="pagingItem.get('First')"/>
        <s:set name="previousPage" value="pagingItem.get('Previous')"/>
        <s:set name="nextPage" value="pagingItem.get('Next')"/>
        <s:set name="lastPage" value="pagingItem.get('Last')"/>
        
        <s:if test='#firstPage != null'><li></s:if><s:else><li class="disabled"></s:else><a id="fpId" href='${pagingURL}&pageNo=${firstPage}&action=${action}'  ><s:text name="paging.first"/></a></li>
        <s:if test='#previousPage != null'><li></s:if><s:else><li class="disabled"></s:else><a id="ppId" href='${pagingURL}&pageNo=${previousPage}&action=${action}' aria-label="Previous"><s:text name="paging.previous"/></a></li>
        <s:iterator value='#pageItem.get("items")' var="item">
            <s:if test="#item == pageNo"><li class="active"><a href="#"> &nbsp;${item}</a></li></s:if>
            <s:if test="#item != pageNo">
                <li>&nbsp;<a href='${pagingURL}&pageNo=${item}&action=${action}'>${item}</a></li>
            </s:if>
        </s:iterator>
        <s:if test='#nextPage != null'><li></s:if><s:else><li class="disabled"></s:else><a id="npId" href='${pagingURL}&pageNo=${nextPage}&action=${action}' aria-label="Next"><s:text name="paging.next"/></a></li>
        <s:if test='#lastPage != null'><li></s:if><s:else><li class="disabled"></s:else><a id="lpId" href='${pagingURL}&pageNo=${lastPage}&action=${action}'><s:text name="paging.last"/></a>
  </ul>
</nav>
--%>