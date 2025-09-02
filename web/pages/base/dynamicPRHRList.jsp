<%--
    Document   : dynamicRpttListPR
    Created on : Jul 29, 2013, 8:16:55 AM
    Author     : Delvene
--%>

<%@taglib uri="/struts-tags" prefix="s"%>

<%int tableCol = 0;%>
<table class="defaultTable" border="0" cellpadding="1" cellspacing="1" width="100%" >
    <!-- display the columns -->
    <tr class="CLASS_TABLE_HEADER">
        <s:if test='hideDeleteButton.equals("N")'>
            <s:if test="has_right('delete')">
            <th width="1%">
                <s:if test="result.size() > 0">
                    <input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, ids);">
                </s:if>
                <s:else>
                    <input type="checkbox" id="cbselect" name="cbselect" disabled >
                </s:else>
            </th></s:if>
        </s:if>
        <%tableCol = 0;%>
        <s:iterator value="displayFieldsHeader" status="columnStatus" var="column">
            <%tableCol ++;%>
            <s:set name="disp" value="displayFields[#columnStatus.index]"/>
            <th style="<s:property value='%{getFieldStyleFormat(#disp, "header", "", #column)}'/>">
                <s:if test="isSortingField(#disp) == true">
                    <a class="thLink" href="javascript:sortField('${disp}')">${column}</a>
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
                </s:if>
                <s:else>${column}</s:else>
            </th>
        </s:iterator>
    </tr>

    <!-- display the data -->
    <%--<s:if test="columns.size() > 0">--%>
        <s:set name="inactiveStatus"><%=com.sains.common.util.SystemConstants.COMM_ActiveStatus.INACTIVE%></s:set>
        <s:set name="notModifiedValue_main"><%=com.impian.pr.model.PRBase.PR_STATUS.MAIN.NotModified%></s:set>
        <s:set name="notModifiedText"><s:text name="PR.MAIN_STATUS.%{#notModifiedValue_main}"/></s:set>
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
            <s:iterator value="result" status="resultStatus" id="r">

                <tr class="
                <s:iterator value="displayFields" id="displayCol2">
                    <%--<s:if test='getFormattedField(#displayCol2, #resultStatus.index).equals("Aktif")'>--%>
                        <s:if test="#resultStatus.odd == true ">odd</s:if><s:else>even</s:else>
                    <%--</s:if>
                    <s:elseif test='getFormattedField(#displayCol2, #resultStatus.index).equals("Tidak Aktif")'>
                        inactive
                    </s:elseif>
                    <s:if test='!getFormattedField("pr_main_status", #resultStatus.index).equals(#notModifiedText)'>
                        pendingData
                    </s:if>--%>
                </s:iterator>
                ">
                    <s:if test='hideDeleteButton.equals("N")'><s:if test="has_right('delete')">
                        <td width="1%">
                            <s:checkbox theme="simple" name="selected" id="ids" fieldValue="%{getResultPrimaryKey(#resultStatus.index)}" onclick="checkToggleCheckbox(cbselect, ids)"/><%firstIndex++;%>
                        </td>
                    </s:if></s:if>
                    <s:iterator value="displayFields" id="displayCol">
                        
                        <td style="padding-left: 4px; padding-right: 4px; <s:property value='%{getFieldStyleFormat(#displayCol, "detail", #r.get(#displayCol), #r)}'/>" 
                            id="${displayCol}" style="word-wrap: break-word"
                            <s:if test="#displayCol.equals('pr_main_status')">
                                <s:if test="#r.get(#displayCol) > 1">
                                    class="pendingData"
                                </s:if>
                            </s:if><s:elseif test="#displayCol.equals('active_flag')">
                                <s:if test="#r.get(#displayCol).toString().equals(#inactiveStatus)">
                                    class="inactive"
                                </s:if>
                            </s:elseif>
                        > <%--end of <td ..... > open tag--%>
                            <s:if test='getCheckEditLink(#displayCol).equals("true")'>
                                <s:url id="editLink" action="%{editPageURL}">
                                    <s:param name="id" value="%{getResultPrimaryKey(#resultStatus.index)}"></s:param>
                                    <s:param name="action" value="%{action}"></s:param>
                                </s:url>
                                <s:if test="#r.get(#displayCol) instanceof java.sql.Timestamp">
                                    <s:a href="%{editLink}" style="${editLinkStyle}">
                                        <s:text name="date_default_datetime"><s:param value="%{#r.get(#displayCol)}" /></s:text>
                                    </s:a>
                                </s:if>
                                <s:else>
                                    <s:a href="%{editLink}" style="${editLinkStyle}"><s:property value="%{#r.get(#displayCol)}"/></s:a>
                                </s:else>
                            </s:if><s:elseif test='getCheckEditLink(#displayCol+"_otherLink").equals("true")'>
                                <s:url id="displayFieldLink" action='%{getDisplayFieldLink(#displayCol+"_otherLink", #r)}'></s:url>
                                <s:set var="lDisplayStyle" value='%{getDisplayFieldLink(#displayCol+"_style", #r)}'/>
                                <s:if test="#r.get(#displayCol) instanceof java.sql.Timestamp">
                                    <s:a href="%{displayFieldLink}" style="${lDisplayStyle}">
                                        <s:text name="date_default_datetime"><s:param value="%{#r.get(#displayCol)}" /></s:text>
                                    </s:a>
                                </s:if><s:else>
                                    <s:a href="%{displayFieldLink}" style="${lDisplayStyle}"><s:property value="%{#r.get(#displayCol)}"/></s:a>
                                </s:else>
                            </s:elseif><s:else>
                                <s:if test='getCheckFormatField(#displayCol).equals("true")'>
                                    <s:property value="%{getFormattedField(#displayCol, #resultStatus.index)}"/>
                                </s:if>
                                <s:else>
                                    <s:if test="#r.get(#displayCol) instanceof java.sql.Timestamp">
                                        <s:text name="date_default_datetime"><s:param value="%{#r.get(#displayCol)}" /></s:text>
                                    </s:if>
                                    <s:else>
                                        <s:property value="%{#r.get(#displayCol)}"/>
                                    </s:else>
                                </s:else>
                            </s:else>
                        </td>
                    </s:iterator>
                    <s:iterator value="hiddenColumns" id="hiddenCol">
                    <span id="${hiddenCol}"><input type="hidden"  value="%{#r.get(#hiddenCol)}" /></span>
                    </s:iterator>
            </tr>
        </s:iterator>
    </s:if>
<%--</s:if>--%>
<%-- if no results --%>
<s:if test="result.size() <= 0 && searched">
    <tr><td class="remark" colspan="<%= tableCol +1 %>"><s:text name="common.noRecordFound" /></td></tr>
</s:if>
<%-- pagination --%>
<s:if test="result.size() > 0 && searched">
    <tr><td colspan="<%=tableCol + 1%>">
            <jsp:include page="../pagination/paging.jsp"></jsp:include>
        </td></tr>
    </s:if>
</table>
