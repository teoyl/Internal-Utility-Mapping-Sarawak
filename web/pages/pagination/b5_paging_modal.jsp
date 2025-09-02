<%@taglib uri="/struts-tags" prefix="s"%>

<div class="row">
    <div class="col-xs-8 col-sm-7 col-md-6 col-lg-6" id="actions-box">  
        <ul class="pagination pagination-sm justify-content-start">
            <s:set var="pageItem" value="pagingItem" />
            <s:set var="firstPage" value="pagingItem.get('First')"/>
            <s:set var="previousPage" value="pagingItem.get('Previous')"/>
            <s:set var="nextPage" value="pagingItem.get('Next')"/>
            <s:set var="lastPage" value="pagingItem.get('Last')"/>

            <s:if test='#firstPage != null'>
                <li class="page-item">
                    <a class="page-link text-500" id="fpId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL}"/>&pageNo=<s:property escapeJavaScript="true" value="%{firstPage}"/>&action=<s:property escapeJavaScript="true" value="%{action}"/>')"><s:text name="paging.first"/></a>
                </li>
            </s:if>
            <s:if test='#previousPage != null'>
                <li class="page-item">
                    <a class="page-link text-500" id="ppId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL}"/>&pageNo=<s:property escapeJavaScript="true" value="%{previousPage}"/>&action=<s:property escapeJavaScript="true" value="%{action}"/>')" aria-label="Previous"><s:text name="paging.previous"/></a>
                </li>
            </s:if>
            <s:iterator value='#pageItem.get("items")' var="item">
                <s:if test="#item == pageNo">
                    <li class="page-item active"><a class="page-link text-white" href="#"><s:property escapeJavaScript="true" value="%{item}"/></a></li>
                    </s:if>
                    <s:if test="#item != pageNo">
                    <li class="page-item"><a class="page-link text-500" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL}"/>&pageNo=<s:property escapeJavaScript="true" value="%{item}"/>&action=<s:property escapeJavaScript="true" value="%{action}"/>')"><s:property escapeJavaScript="true" value="%{item}"/></a></li>
                    </s:if>
                </s:iterator>
                <s:if test='#nextPage != null'>
                <li class="page-item">
                    <a class="page-link text-500" id="npId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL}"/>&pageNo=<s:property escapeJavaScript="true" value="%{nextPage}"/>&action=<s:property escapeJavaScript="true" value="%{action}"/>')" aria-label="Next"><s:text name="paging.next"/></a>
                </li>
            </s:if>
            <s:if test='#lastPage != null'>
                <li class="page-item">
                    <a class="page-link text-500" id="lpId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL}"/>&pageNo=<s:property escapeJavaScript="true" value="%{lastPage}"/>&action=<s:property escapeJavaScript="true" value="%{action}"/>')"><s:text name="paging.last"/>
                    </a>
                </li>
            </s:if>
        </ul>     
    </div>

    <div class="col-xs-4 col-sm-5 col-md-6 col-lg-6 text-end">
        <p class="fs--1">
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
        </p>
    </div>
</div>
            <script>
                function gotoPage(action) {
                    action += "&bs=1";
                $("#lookupModal").load(action,
                    function(message) {
                        if (message === "Expired") {
                            document.location = "initLogin";
                        }
                    });
                }
            </script>