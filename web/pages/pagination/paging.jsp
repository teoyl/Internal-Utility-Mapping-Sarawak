<%@taglib uri="/struts-tags" prefix="s"%>
<s:head />
<div class="row">
    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-3">
        <s:if test="(pageNo * pageSize) >= numberOfRows && numberOfRows > 0">
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
    </div>
    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-9 text-end" id="actions-box">  
        <ul class="pagination justify-content-end">
            <s:set var="pageItem" value="pagingItem" />
            <s:set var="firstPage" value="pagingItem.get('First')"/>
            <s:set var="previousPage" value="pagingItem.get('Previous')"/>
            <s:set var="nextPage" value="pagingItem.get('Next')"/>
            <s:set var="lastPage" value="pagingItem.get('Last')"/>

            <s:if test='#firstPage != null'>
                <li class="page-item">
                    <a id="fpId" class="page-link" href="<s:property value="%{pagingURL}" escapeHtml="true" escapeJavaScript="true"/>&pageNo=<s:property value="%{firstPage}"/>&action=<s:property value="%{action}"/>"><s:text name="paging.first"/></a>
                </li>
            </s:if>
            <s:if test='#previousPage != null'>
                <li class="page-item">
                    <a id="ppId" class="page-link" href="<s:property value="%{pagingURL}" escapeHtml="true" escapeJavaScript="true"/>&pageNo=<s:property value="%{previousPage}"/>&action=<s:property value="%{action}"/>" aria-label="Previous"><s:text name="paging.previous"/></a>
                </li>
            </s:if>
            <s:iterator value='#pageItem.get("items")' var="item">
                <s:if test="#item == pageNo">
                    <li class="active page-item"><a class="page-link" href="#"><s:property value="%{item}"/></a></li>
                    </s:if>
                    <s:if test="#item != pageNo">
                    <li class="page-item"><a class="page-link" href="<s:property value="%{pagingURL}" escapeHtml="true" escapeJavaScript="true"/>&pageNo=<s:property value="%{item}"/>&action=<s:property value="%{action}"/>"><s:property value="%{item}"/></a></li>
                    </s:if>
                </s:iterator>
                <s:if test='#nextPage != null'>
                <li class="page-item">
                    <a id="npId" class="page-link" href="<s:property value="%{pagingURL}" escapeHtml="true" escapeJavaScript="true"/>&pageNo=<s:property value="%{nextPage}"/>&action=<s:property value="%{action}"/>" aria-label="Next"><s:text name="paging.next"/></a>
                </li>
            </s:if>
            <s:if test='#lastPage != null'>
                <li class="page-item">
                    <a id="lpId" class="page-link" href="<s:property value="%{pagingURL}" escapeHtml="true" escapeJavaScript="true"/>&pageNo=<s:property value="%{lastPage}"/>&action=<s:property value="%{action}"/>"><s:text name="paging.last"/>
                    </a>
                </li>
            </s:if>
        </ul>     
    </div>
</div>