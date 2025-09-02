<%@taglib uri="/struts-tags" prefix="s"%>
<s:head />
<%--<div class="row">
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
    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-9 text-right" id="actions-box">  
        <ul class="pagination">
            <s:set var="pageItem" value="pagingItem" />
            <s:set var="firstPage" value="pagingItem.get('First')"/>
            <s:set var="previousPage" value="pagingItem.get('Previous')"/>
            <s:set var="nextPage" value="pagingItem.get('Next')"/>
            <s:set var="lastPage" value="pagingItem.get('Last')"/>

            <s:if test='#firstPage != null'>
                <li>
                    <a id="fpId" href='<s:property value="%{pagingURL}" escapeJavaScript="true"/>&pageNo=<s:property value="%{firstPage}"/>&action=<s:property value="%{action}"/>'><s:text name="paging.first"/></a>
                </li>
            </s:if>
            <s:if test='#previousPage != null'>
                <li>
                    <a id="ppId" href='<s:property value="%{pagingURL}" escapeJavaScript="true"/>&pageNo=<s:property value="%{previousPage}"/>&action=<s:property value="%{action}"/>' aria-label="Previous"><s:text name="paging.previous"/></a>
                </li>
            </s:if>
            <s:iterator value='#pageItem.get("items")' var="item">
                <s:if test="#item == pageNo">
                    <li class="active"><a href="#"> &nbsp;<s:property value="%{item}"/></a></li>
                    </s:if>
                    <s:if test="#item != pageNo">
                    <li>&nbsp;<a href='<s:property value="%{pagingURL}" escapeJavaScript="true"/>&pageNo=<s:property value="%{item}"/>&action=<s:property value="%{action}"/>'><s:property value="%{item}"/></a></li>
                    </s:if>
                </s:iterator>
                <s:if test='#nextPage != null'>
                <li>
                    <a id="npId" href='<s:property value="%{pagingURL}" escapeJavaScript="true"/>&pageNo=<s:property value="%{nextPage}"/>&action=<s:property value="%{action}"/>' aria-label="Next"><s:text name="paging.next"/></a>
                </li>
            </s:if>
            <s:if test='#lastPage != null'>
                <li>
                    <a id="lpId" href='<s:property value="%{pagingURL}" escapeJavaScript="true"/>&pageNo=<s:property value="%{lastPage}"/>&action=<s:property value="%{action}"/>'><s:text name="paging.last"/>
                    </a>
                </li>
            </s:if>
        </ul>     
    </div>
</div>--%>

    <!--begin: Pagination-->
    <div class="kt-pagination  kt-pagination--brand">
        <ul class="kt-pagination__links">
            <s:set var="pageItem" value="pagingItem" />
            <s:set var="firstPage" value="pagingItem.get('First')"/>
            <s:set var="previousPage" value="pagingItem.get('Previous')"/>
            <s:set var="nextPage" value="pagingItem.get('Next')"/>
            <s:set var="lastPage" value="pagingItem.get('Last')"/>

            <s:if test='#firstPage != null'>
                <li class="kt-pagination__link--first">
                    <a id="fpId" href='${pagingURL}&pageNo=${firstPage}&action=${action}'><i class="fa fa-angle-double-left kt-font-brand"></i></a>
                </li>
            </s:if>
            <s:if test='#previousPage != null'>
                <li class="kt-pagination__link--next">
                    <a id="ppId" href='${pagingURL}&pageNo=${previousPage}&action=${action}' aria-label="Previous"><i class="fa fa-angle-left kt-font-brand"></i></a>
                </li>
            </s:if>
            <s:iterator value='#pageItem.get("items")' var="item">
                <s:if test="#item == pageNo">
                <li class="kt-pagination__link--active" aria-current="page"><a href="#"> &nbsp;${item}</a></li>
                </s:if>
                <s:if test="#item != pageNo">
                <li><a href='${pagingURL}&pageNo=${item}&action=${action}'>${item}</a></li>
                </s:if>
            </s:iterator>
            <s:if test='#nextPage != null'>
                <li class="kt-pagination__link--prev">
                    <a id="npId" href='${pagingURL}&pageNo=${nextPage}&action=${action}' aria-label="Next"><i class="fa fa-angle-right kt-font-brand"></i></a>
                </li>   
            </s:if>
            <s:if test='#lastPage != null'>
                <li class="kt-pagination__link--last">
                    <a id="lpId" href='${pagingURL}&pageNo=${lastPage}&action=${action}'><i class="fa fa-angle-double-right kt-font-brand"></i>
                    </a>
                </li>
            </s:if>
        </ul>
        <div class="kt-pagination__toolbar">
            <span class="pagination__desc">
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
            </span>
        </div>
    </div>
    <!--end: Pagination-->