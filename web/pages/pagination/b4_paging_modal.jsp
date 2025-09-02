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
                        <s:if test='#firstPage != null'>&nbsp;<a id="fpId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL"/>&pageNo=<s:property escapeJavaScript="true" value="%{firstPage"/>&action=<s:property escapeJavaScript="true" value="%{action"/>'><s:text name="paging.first"/></a></s:if>
                        <s:if test='#previousPage != null'>&nbsp;<a id="ppId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL"/>&pageNo=<s:property escapeJavaScript="true" value="%{previousPage"/>&action=<s:property escapeJavaScript="true" value="%{action"/>'><s:text name="paging.previous"/></a></s:if>
                        <s:iterator
                                value='#pageItem.get("items")' var="item">
                                <s:if test="#item == pageNo">
                                                &nbsp;[<s:property escapeJavaScript="true" value="%{item"/>]
                                        </s:if>
                                <s:if test="#item != pageNo">
                                        &nbsp;<a href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL"/>&pageNo=<s:property escapeJavaScript="true" value="%{item"/>&action=<s:property escapeJavaScript="true" value="%{action"/>'><s:property escapeJavaScript="true" value="%{item"/></a>
                                </s:if>
                        </s:iterator>
                        <s:if test='#nextPage != null'>&nbsp;<a id="npId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL"/>&pageNo=<s:property escapeJavaScript="true" value="%{nextPage"/>&action=<s:property escapeJavaScript="true" value="%{action"/>'><s:text name="paging.next"/></a></s:if>
                        <s:if test='#lastPage != null'>&nbsp;<a id="lpId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL"/>&pageNo=<s:property escapeJavaScript="true" value="%{lastPage"/>&action=<s:property escapeJavaScript="true" value="%{action"/>'><s:text name="paging.last"/></a></s:if>
                </td>
        </tr></table>--%>


<%--nav aria-label="Page navigation">
    <ul class="pagination " style="width:100%;">

        <s:set var="pageItem" value="pagingItem" />
        <s:set var="firstPage" value="pagingItem.get('First')"/>
        <s:set var="previousPage" value="pagingItem.get('Previous')"/>
        <s:set var="nextPage" value="pagingItem.get('Next')"/>
        <s:set var="lastPage" value="pagingItem.get('Last')"/>

        <s:if test='#firstPage != null'><li></s:if><s:else><li class="disabled"></s:else><a id="fpId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL"/>&pageNo=<s:property escapeJavaScript="true" value="%{firstPage"/>&action=<s:property escapeJavaScript="true" value="%{action"/>'  ><s:text name="paging.first"/></a></li>
        <s:if test='#previousPage != null'><li></s:if><s:else><li class="disabled"></s:else><a id="ppId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL"/>&pageNo=<s:property escapeJavaScript="true" value="%{previousPage"/>&action=<s:property escapeJavaScript="true" value="%{action"/>' aria-label="Previous"><s:text name="paging.previous"/></a></li>
            <s:iterator value='#pageItem.get("items")' var="item">
                <s:if test="#item == pageNo"><li class="active"><a href="#"> &nbsp;<s:property escapeJavaScript="true" value="%{item"/></a></li></s:if>
                <s:if test="#item != pageNo">
                <li>&nbsp;<a href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL"/>&pageNo=<s:property escapeJavaScript="true" value="%{item"/>&action=<s:property escapeJavaScript="true" value="%{action"/>'><s:property escapeJavaScript="true" value="%{item"/></a></li>
                </s:if>
            </s:iterator>
            <s:if test='#nextPage != null'><li></s:if><s:else><li class="disabled"></s:else><a id="npId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL"/>&pageNo=<s:property escapeJavaScript="true" value="%{nextPage"/>&action=<s:property escapeJavaScript="true" value="%{action"/>' aria-label="Next"><s:text name="paging.next"/></a></li>
        <s:if test='#lastPage != null'><li></s:if><s:else><li class="disabled"></s:else><a id="lpId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL"/>&pageNo=<s:property escapeJavaScript="true" value="%{lastPage"/>&action=<s:property escapeJavaScript="true" value="%{action"/>'><s:text name="paging.last"/></a>
            <li class="pull-right">
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
        </li>
    </ul>
</nav--%>

<div class="row">
    <div class="col-xs-8 col-sm-7 col-md-6 col-lg-6 kt-pagination kt-pagination--brand" id="actions-box">  
        
        <ul class="kt-pagination__links">
            <s:set var="pageItem" value="pagingItem" />
            <s:set var="firstPage" value="pagingItem.get('First')"/>
            <s:set var="previousPage" value="pagingItem.get('Previous')"/>
            <s:set var="nextPage" value="pagingItem.get('Next')"/>
            <s:set var="lastPage" value="pagingItem.get('Last')"/>
            <li>&nbsp;</li>
            <s:if test='#firstPage != null'>
                <li class="kt-pagination__link--first">
                    <a id="fpId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL}"/>&pageNo=<s:property escapeJavaScript="true" value="%{firstPage}"/>&action=<s:property escapeJavaScript="true" value="%{action}"/>')"><i class="fa fa-angle-double-left kt-font-brand"></i></a>
                </li>
            </s:if>
            <s:if test='#previousPage != null'>
                <li class="kt-pagination__link--next">
                    <a id="ppId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL}"/>&pageNo=<s:property escapeJavaScript="true" value="%{previousPage}"/>&action=<s:property escapeJavaScript="true" value="%{action}"/>')" aria-label="Previous"><i class="fa fa-angle-left kt-font-brand"></i></a>
                </li>
            </s:if>
            <s:iterator value='#pageItem.get("items")' var="item">
                <s:if test="#item == pageNo">
                    <li class="kt-pagination__link--active" aria-current="page"><a href="#"> &nbsp;<s:property escapeJavaScript="true" value="%{item}"/></a></li>
                    </s:if>
                    <s:if test="#item != pageNo">
                    <li>&nbsp;<a href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL}"/>&pageNo=<s:property escapeJavaScript="true" value="%{item}"/>&action=<s:property escapeJavaScript="true" value="%{action}"/>')"><s:property escapeJavaScript="true" value="%{item}"/></a></li>
                    </s:if>
                </s:iterator>
                <s:if test='#nextPage != null'>
                <li class="kt-pagination__link--prev">
                    <a id="npId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL}"/>&pageNo=<s:property escapeJavaScript="true" value="%{nextPage}"/>&action=<s:property escapeJavaScript="true" value="%{action}"/>')" aria-label="Next"><i class="fa fa-angle-right kt-font-brand"></i></a>
                </li>
            </s:if>
            <s:if test='#lastPage != null'>
                <li class="kt-pagination__link--last">
                    <a id="lpId" href='#' onclick="gotoPage('<s:property escapeJavaScript="true" value="%{pagingURL}"/>&pageNo=<s:property escapeJavaScript="true" value="%{lastPage}"/>&action=<s:property escapeJavaScript="true" value="%{action}"/>')"><i class="fa fa-angle-double-right kt-font-brand"></i>
                    </a>
                </li>
            </s:if>
        </ul>     
    </div>

    <div class="col-xs-4 col-sm-5 col-md-6 col-lg-6 text-right kt-pagination__toolbar">
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