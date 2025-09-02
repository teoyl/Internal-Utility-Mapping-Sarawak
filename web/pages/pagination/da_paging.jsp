<%@taglib uri="/struts-tags" prefix="s"%>
<s:head />
<div class="row">
    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-3">
        <s:if test="(da.pageNo * da.pageSize) >= da.numberOfRows && da.numberOfRows > 0">
            <s:if test="(da.pageNo - 1) * da.pageSize + 1 == da.numberOfRows">
                <s:text name="paging.record2">
                    <s:param value="da.numberOfRows" />
                    <s:param value="da.numberOfRows" />
                </s:text>
            </s:if>
            <s:else>
                <s:text name="paging.record">
                    <s:param value="(da.pageNo - 1) * da.pageSize + 1" />
                    <s:param value="da.numberOfRows" />
                    <s:param value="da.numberOfRows" />
                </s:text>
            </s:else>
        </s:if>
        <s:if test="da.pageNo * da.pageSize < da.numberOfRows">
            <s:if test="da.pageNo == 1">
                <s:text name="paging.record">
                    <s:param value="da.pageNo" />
                    <s:param value="da.pageNo * da.pageSize" />
                    <s:param value="da.numberOfRows" />
                </s:text>
            </s:if>
            <s:else>
                <s:text name="paging.record">
                    <s:param value="(da.pageNo - 1) * da.pageSize + 1" />
                    <s:param value="da.pageNo * da.pageSize" />
                    <s:param value="da.numberOfRows" />
                </s:text>
            </s:else>
        </s:if>
    </div>
    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-9 text-right" id="actions-box">  
        <ul class="pagination">
            <s:set var="pageItem" value="da.pagingItem" />
            <s:set var="firstPage" value="da.pagingItem.get('First')"/>
            <s:set var="previousPage" value="da.pagingItem.get('Previous')"/>
            <s:set var="nextPage" value="da.pagingItem.get('Next')"/>
            <s:set var="lastPage" value="da.pagingItem.get('Last')"/>

            <s:if test='#firstPage != null'>
                <li>
                    <a id="fpId" href='<s:property value="%{da.pagingURL}" escapeJavaScript="true"/>&pageNo=<s:property value="%{firstPage}"/>&action=<s:property value="%{da.action}"/>'><s:text name="paging.first"/></a>
                </li>
            </s:if>
            <s:if test='#previousPage != null'>
                <li>
                    <a id="ppId" href='<s:property value="%{da.pagingURL}" escapeJavaScript="true"/>&pageNo=<s:property value="%{previousPage}"/>&action=<s:property value="%{da.action}"/>' aria-label="Previous"><s:text name="paging.previous"/></a>
                </li>
            </s:if>
            <s:iterator value='#pageItem.get("items")' var="item">
                <s:if test="#item == pageNo">
                    <li class="active"><a href="#"><s:property value="%{item}"/></a></li>
                    </s:if>
                    <s:if test="#item != pageNo">
                    <li><a href='<s:property value="%{da.pagingURL}" escapeJavaScript="true"/>&pageNo=<s:property value="%{item}"/>&action=<s:property value="%{da.action}"/>'><s:property value="%{item}"/></a></li>
                    </s:if>
                </s:iterator>
                <s:if test='#nextPage != null'>
                <li>
                    <a id="npId" href='<s:property value="%{da.pagingURL}" escapeJavaScript="true"/>&pageNo=<s:property value="%{nextPage}"/>&action=<s:property value="%{da.action}"/>' aria-label="Next"><s:text name="paging.next"/></a>
                </li>
            </s:if>
            <s:if test='#lastPage != null'>
                <li>
                    <a id="lpId" href='<s:property value="%{da.pagingURL}" escapeJavaScript="true"/>&pageNo=<s:property value="%{lastPage}"/>&action=<s:property value="%{da.action}"/>'><s:text name="paging.last"/>
                    </a>
                </li>
            </s:if>
        </ul>     
    </div>
</div>