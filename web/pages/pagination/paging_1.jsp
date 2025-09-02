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
                        <s:set name="pageItem" value="pagingItem" />
                        <s:set name="firstPage" value="pagingItem.get('First')"/>
                        <s:set name="previousPage" value="pagingItem.get('Previous')"/>
                        <s:set name="nextPage" value="pagingItem.get('Next')"/>
                        <s:set name="lastPage" value="pagingItem.get('Last')"/>
                        <s:if test='#firstPage != null'>&nbsp;<a id="fpId" href='${pagingURL}&pageNo=${firstPage}&action=${action}'><s:text name="paging.first"/></a></s:if>
                        <s:if test='#previousPage != null'>&nbsp;<a id="ppId" href='${pagingURL}&pageNo=${previousPage}&action=${action}'><s:text name="paging.previous"/></a></s:if>
                        <s:iterator
                                value='#pageItem.get("items")' var="item">
                                <s:if test="#item == pageNo">
                                                &nbsp;[${item}]
                                        </s:if>
                                <s:if test="#item != pageNo">
                                        &nbsp;<a href='${pagingURL}&pageNo=${item}&action=${action}'>${item}</a>
                                </s:if>
                        </s:iterator>
                        <s:if test='#nextPage != null'>&nbsp;<a id="npId" href='${pagingURL}&pageNo=${nextPage}&action=${action}'><s:text name="paging.next"/></a></s:if>
                        <s:if test='#lastPage != null'>&nbsp;<a id="lpId" href='${pagingURL}&pageNo=${lastPage}&action=${action}'><s:text name="paging.last"/></a></s:if>
                </td>
        </tr></table>--%>


<%--nav aria-label="Page navigation">
    <ul class="pagination " style="width:100%;">

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
    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-9" id="actions-box">  
        <ul class="pagination">
            <s:set name="pageItem" value="pagingItem" />
            <s:set name="firstPage" value="pagingItem.get('First')"/>
            <s:set name="previousPage" value="pagingItem.get('Previous')"/>
            <s:set name="nextPage" value="pagingItem.get('Next')"/>
            <s:set name="lastPage" value="pagingItem.get('Last')"/>

            <s:if test='#firstPage != null'>
                <li>
                    <a id="fpId" href='${pagingURL}&pageNo=${firstPage}&action=${action}'><s:text name="paging.first"/></a>
                </li>
            </s:if>
            <s:if test='#previousPage != null'>
                <li>
                    <a id="ppId" href='${pagingURL}&pageNo=${previousPage}&action=${action}' aria-label="Previous"><s:text name="paging.previous"/></a>
                </li>
            </s:if>
            <s:iterator value='#pageItem.get("items")' var="item">
                <s:if test="#item == pageNo">
                    <li class="active"><a href="#"> &nbsp;${item}</a></li>
                    </s:if>
                    <s:if test="#item != pageNo">
                    <li>&nbsp;<a href='${pagingURL}&pageNo=${item}&action=${action}'>${item}</a></li>
                    </s:if>
                </s:iterator>
                <s:if test='#nextPage != null'>
                <li>
                    <a id="npId" href='${pagingURL}&pageNo=${nextPage}&action=${action}' aria-label="Next"><s:text name="paging.next"/></a>
                </li>
            </s:if>
            <s:if test='#lastPage != null'>
                <li>
                    <a id="lpId" href='${pagingURL}&pageNo=${lastPage}&action=${action}'><s:text name="paging.last"/>
                    </a>
                </li>
            </s:if>
        </ul>     
    </div>

    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-3  text-right">
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