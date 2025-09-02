<%@taglib uri="/struts-tags" prefix="s"%>

<%int tableCol = 0;%>
<%int ctr = 0;%>
<div class="table-responsive">
    <table <s:if test="isGenerateDynamicRpt">id="dynamicListTable"</s:if><s:elseif test="useDataTable">id="kt_table_1"</s:elseif> class="table table-sds table-condensed table-striped table-hover" width="100%">
        <thead>
            <!-- display the columns -->
            <tr>
                <s:if test='hideDeleteButton.equals("N")'>
                    <%--<s:if test="has_right('delete')">--%>
                    <s:if test="has_right2('DynamicAction','delete',action)">
                        <s:if test="useDataTable">
                            <th>
<!--                                <div class="checkbox check-success">
                                    <label class="kt-checkbox kt-checkbox--brand">
                                        <s:if test="result.size() > 0">
                                            <input type="checkbox"  id="cbselect" class="selectAll" name="cbselect" onClick="toggleCheckboxByName(this,'selected');">
                                            <%--<input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, ids);">--%>
                                        </s:if>
                                        <s:else>
                                            <input type="checkbox" id="cbselect" class="selectAll" name="cbselect" disabled >
                                        </s:else>
                                        <span></span>
                                    </label>
                                </div>-->
                            </th>
                        </s:if>
                        <s:else>
                            <th width="1%">
                                <div class="checkbox check-success">
                                    <label class="kt-checkbox kt-checkbox--brand">
                                        <s:if test="result.size() > 0">
                                            <input type="checkbox"  id="cbselect" class="selectAll" name="cbselect" onClick="toggleCheckboxByName(this,'selected');">
                                            <%--<input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, ids);">--%>
                                        </s:if>
                                        <s:else>
                                            <input type="checkbox" id="cbselect" class="selectAll" name="cbselect" disabled >
                                        </s:else>
                                        <span></span>
                                    </label>
                                </div>
                            </th>
                        </s:else>
                    </s:if>
                </s:if>
                <%tableCol = 0;%>
                <s:iterator value="displayFieldsHeader" status="columnStatus" var="column">
                    <%tableCol++;%>
                    <s:set var="disp" value="displayFields[#columnStatus.index]"/>
                    <th <s:if test="useDataTable && !(isSortingField(#disp))">class="no-sort"</s:if> style="<s:property value='%{getFieldStyleFormat(#disp, "header", "", #column)}' escapeHtml="true"/>" >
                        <s:if test="isSortingField(#disp)">
                            <s:if test="!(useDataTable)">
                                <a class="thLink" href="javascript:sortField('${disp}')"><s:property value="%{#column}" escapeHtml="true"/></a>
                                <s:if test="(dynamicSortBy == #disp) && (result.size() > 0)">
                                    <s:if test='dynamicSortOrder == "D"'>
                                        <Img src='images/sortdes.gif' border='0'/>
                                    </s:if>
                                    <s:else>
                                        <Img src='images/sortasc.gif' border='0'/>
                                    </s:else>
                                </s:if><s:else>
                                    &nbsp;<Img alt="sorting" src='images/sortingIcon.gif' border='0'/>
                                </s:else>
                            </s:if><s:else>
                                <s:property value="%{#column}" escapeHtml="true"/>&nbsp;
                            </s:else>
                        </s:if>
                        <s:else>
                            <%--<s:property value="%{#column}"/>--%>
                            <%--to enable sorting for custom sorting field at dynamic config - ahmadni  @ 14-Feb-2017 --%>
                            <s:if test="!(useDataTable)">
                                <a class="thLink" href="javascript:sortField('${disp}')"><s:property value="%{#column}" escapeHtml="true"/></a>
                                <s:if test="(dynamicSortBy == #disp) && (result.size() > 0)">
                                    <s:if test='dynamicSortOrder == "D"'>
                                        <Img src='images/sortdes.gif' border='0'/>
                                    </s:if>
                                    <s:else>
                                        <Img src='images/sortasc.gif' border='0'/>
                                    </s:else>
                                </s:if><s:else>
                                    &nbsp;<Img alt="sorting" src='images/sortingIcon.gif' border='0'/>
                                </s:else>
                            </s:if><s:else>
                                <s:property value="%{#column}" escapeHtml="true"/>
                            </s:else>
                        </s:else>
                    </th>
                </s:iterator>
            </tr>
        </thead>

        <tbody>
            <!-- display the data -->
            <%--<s:if test="columns.size() > 0">--%>
            <s:if test="result.size() > 0">
                <%
                    String[] cssClass = {"value", "valueB"};
                    String[] cssCenterClass = {"valueCenter", "valueCenterB"};
                    String pageNo = request.getParameter("pageNo");
                    if (com.sains.common.util.Validator.isEmpty(pageNo)) {
                        pageNo = "1";
                    }
                    int row = 0, firstIndex = (Integer.parseInt(pageNo) - 1) * 10 + 1;
                %>
                <s:iterator value="result" status="resultStatus" var="r">
                    <%ctr++;%>
                    <tr>
                        <s:if test='hideDeleteButton.equals("N")'><s:if test="has_right2('DynamicAction','delete',action)">
                            <s:if test="useDataTable">
                                <td class="<s:if test="#resultStatus.odd == true ">odd</s:if><s:else>even</s:else>" width="1%">
                                    <div class="checkbox check-success">
                                        <label class="kt-checkbox kt-checkbox--brand">
                                            <input type="checkbox" name="selected" class="checkbox_child" id="_${resultStatus.index}" value="<s:property value="%{getResultPrimaryKey(#resultStatus.index)}" escapeHtml="true"/>" onclick="toggleSelectAll()">
                                        <%--<label for="_${resultStatus.index}"></label>--%>
                                        <%--<s:checkbox theme="simple" name="selected" cssClass="checkbox_child" id="%{#resultStatus.index}" fieldValue="%{getResultPrimaryKey(#resultStatus.index)}" onclick="toggleSelectAll();"/>--%>
                                            <span></span>
                                        </label>
                                    </div>

                                    <%--<s:checkbox theme="simple" name="selected" id="ids" fieldValue="%{getResultPrimaryKey(#resultStatus.index)}" onclick="checkToggleCheckbox(cbselect, ids)"/>--%>
                                    <%firstIndex++;%>
                                </td>
                            </s:if>
                            <s:else>
                                <td class="<s:if test="#resultStatus.odd == true ">odd</s:if><s:else>even</s:else>" width="1%">
                                    <div class="checkbox check-success">
                                        <label class="kt-checkbox kt-checkbox--brand">
                                            <input type="checkbox" name="selected" class="checkbox_child" id="_${resultStatus.index}" value="<s:property value="%{getResultPrimaryKey(#resultStatus.index)}" escapeHtml="true"/>" onclick="toggleSelectAll()">
                                        <%--<label for="_${resultStatus.index}"></label>--%>
                                        <%--<s:checkbox theme="simple" name="selected" cssClass="checkbox_child" id="%{#resultStatus.index}" fieldValue="%{getResultPrimaryKey(#resultStatus.index)}" onclick="toggleSelectAll();"/>--%>
                                            <span></span>
                                        </label>
                                    </div>
                                                
                                    <%--<s:checkbox theme="simple" name="selected" id="ids" fieldValue="%{getResultPrimaryKey(#resultStatus.index)}" onclick="checkToggleCheckbox(cbselect, ids)"/>--%>
                                    <%firstIndex++;%>
                                </td>
                            </s:else>
                        </s:if></s:if>
                        <s:iterator value="displayFields" var="displayCol">
                            <td id="${displayCol}" style="word-wrap: break-word">
                                <s:if test='getCheckEditLink(#displayCol).equals("true")'>
                                    <s:set var="theTarget" value="%{getOpenLinkAtNewPage(#displayCol)}"/>
                                    <s:if test='isMixConfig'>
                                        <s:url var="editLink" action="%{editPageURL}">
                                            <s:param name="id" value="%{getResultPrimaryKey(#resultStatus.index)}"></s:param>
                                            <s:param name="action" value="%{action}"></s:param>
                                            <s:param name="searchCode" value="%{searchCode}"></s:param>
                                        </s:url>
                                    </s:if><s:else>
                                        <s:url var="editLink" action="%{editPageURL}">
                                            <s:param name="id" value="%{getResultPrimaryKey(#resultStatus.index)}"></s:param>
                                            <s:param name="action" value="%{action}"></s:param>
                                        </s:url>
                                    </s:else>
                                    <s:if test="#r.get(#displayCol) instanceof java.sql.Timestamp">
                                        <s:a href="%{editLink}" cssStyle="%{editLinkStyle}" target="%{theTarget}">
                                            <div style="width: 100%"><s:text name="date_default_datetime"><s:param value="%{#r.get(#displayCol)}" /></s:text></div>
                                        </s:a>
                                    </s:if>
                                    <s:else>
                                        <s:a href="%{editLink}" cssStyle="%{editLinkStyle}" target="%{theTarget}"><div style="width: 100%"><s:property value="%{#r.get(#displayCol)}"/></div></s:a>
                                    </s:else>
                                </s:if><s:elseif test='getCheckEditLink(#displayCol+"_otherLink").equals("true")'>
                                    <s:set var="theTarget" value="%{getOpenLinkAtNewPage(#displayCol)}"/>
                                    <s:url var="displayFieldLink" action='%{getDisplayFieldLink(#displayCol+"_otherLink", #r)}'></s:url>
                                    <s:set var="lDisplayStyle" value='%{getDisplayFieldLink(#displayCol+"_style", #r)}'/>
                                    <s:if test="#r.get(#displayCol) instanceof java.sql.Timestamp">
                                        <s:a href="%{displayFieldLink}" cssStyle="%{lDisplayStyle}" target="%{theTarget}">
                                            <div style="width: 100%">
                                                <s:text name="date_default_datetime"><s:param value="%{#r.get(#displayCol)}" /></s:text>
                                                </div>
                                        </s:a>
                                    </s:if><s:else>
                                        <s:a href="%{displayFieldLink}" cssStyle="%{lDisplayStyle}" target="%{theTarget}"><div style="width: 100%"><s:property value="%{#r.get(#displayCol)}" escapeHtml="true"/></div></s:a>
                                    </s:else>
                                </s:elseif><s:else>
                                    <s:if test='getCheckFormatField(#displayCol).equals("true")'>
                                        <s:property escapeHtml="true" value="%{getFormattedField(#displayCol, #resultStatus.index)}"/>
                                    </s:if>
                                    <s:else>
                                        <s:if test="#r.get(#displayCol) instanceof java.sql.Timestamp">
                                            <s:text name="date_default_datetime"><s:param value="%{#r.get(#displayCol)}" /></s:text>
                                        </s:if><s:elseif test="#r.get(#displayCol) instanceof java.util.Date">
                                            <s:text name="date_default_date"><s:param value="%{#r.get(#displayCol)}" /></s:text>
                                        </s:elseif>
                                        <s:else>
                                            <s:property escapeHtml="true" value="%{#r.get(#displayCol)}"/>
                                        </s:else>
                                    </s:else>
                                </s:else>
                            </td>
                        </s:iterator>
                        <s:iterator value="hiddenColumns" var="hiddenCol">
                    <span id="${hiddenCol}"><input type="hidden"  value="%{#r.get(#hiddenCol)}" /></span>
                    </s:iterator>
                </tr>
            </s:iterator>
        </s:if>
        <s:elseif test="!(useDataTable)">
            <tr><td class="remark" colspan="<%= tableCol + 1%>"><center><s:text name="common.noRecordFound" /></center></td></tr>
        </s:elseif>
        <%--</s:if>--%>
        <%-- if no results --%>
        <%--<s:if test="result.size() <= 0 && searched">
            <tr><td class="remark" colspan="<%= tableCol + 1%>"><s:text name="common.noRecordFound" /></td></tr>
            </s:if>--%>
        </tbody>
    </table>
</div>

<%-- pagination --%>
<s:if test="result.size() > 0 && searched && !(useDataTable)">
    <jsp:include page="../pagination/b4_paging.jsp"></jsp:include>
</s:if>
