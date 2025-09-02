<%--
    Document   : dynamicRpttListPR
    Created on : Jul 29, 2013, 8:16:55 AM
    Author     : Delvene
--%>

<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
<link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
<link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />
<script language="javascript">
    var winPodH;
    var reportLink;

    function openSelectTab(url) {
        reportLink = url;
        winPodH=dhtmlmodal.open("popup", "iframe", "populateMenuListPRHR", "Pilih Laporan Untuk Dijana", "width=400px,height=200px,center=1,resize=1,scrolling=1", "");

        winPodH.onclose=function(){
            return false;
        }
    }

    function openReport(selectedTab) {
        var reportURL = reportLink + "&search_pSelectedTab=" + selectedTab;
        window.open(reportURL);
    }
</script>

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
            <th style="<s:property value='%{getFieldStyleFormat(#disp)}'/>">
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
                <tr>
                    <%--<s:if test='hideDeleteButton.equals("N")'><s:if test="has_right('delete')">
                        <td class="<s:if test="#resultStatus.odd == true ">odd</s:if><s:else>even</s:else>" width="1%">
                            <s:checkbox theme="simple" name="selected" id="ids" fieldValue="%{getResultPrimaryKey(#resultStatus.index)}" onclick="checkToggleCheckbox(cbselect, ids)"/><%firstIndex++;%>
                        </td>
                    </s:if></s:if>--%>
                    <s:iterator value="displayFields" id="displayCol">
                        <td style="padding-left: 4px; padding-right: 4px; <s:property value='%{getFieldStyleFormat(#displayCol)}'/>" class="<s:if test="#resultStatus.odd == true ">odd</s:if><s:else>even</s:else>" id="${displayCol}" style="word-wrap: break-word">
                            <s:if test='getCheckEditLink(#displayCol).equals("true")'>
                                <s:url id="reportLink" action="%{editPageURL}" escapeAmp="false">
                                    <s:param name="search_pEmpID" value="%{getResultPrimaryKey(#resultStatus.index)}"></s:param>
                                    <s:param name="rptCode" value="%{action}"></s:param>
                                </s:url>
                                <%--<input type="button" class="defaultButton" value="Jana" onclick="window.open('${reportLink}')" />--%>
                                <input type="button" class="defaultButton" value="Jana" onclick="return openSelectTab('${reportLink}');" />
                                <%--<a href="dynamicViewPage?search_pEmpID=K0273732&rptCode=${prReport.reportCode}" target="_blank">Jana</a>--%>
                                <%--<s:submit theme="simple" action="%{reportLink}" value="%{getText('button.generate')}" cssClass="defaultButton" />--%>
                            </s:if>
                            <s:else>
                                <s:if test='getCheckFormatField(#displayCol).equals("true")'>
                                    <s:property value="%{getFormattedField(#displayCol, #resultStatus.index)}"/>
                                </s:if>
                                <s:else>
                                    <s:property value="%{#r.get(#displayCol)}"/>
                                </s:else>
                            </s:else>
                        </td>
                    </s:iterator>
                    <s:iterator value="hiddenColumns" id="hiddenCol">
                    <%--<span id="${hiddenCol}"><input type="hidden"  value="%{#r.get(#hiddenCol)}" /></span>--%> <%--commented by Zhafari - 22 Aug 2013 - span cannot be directly inside <tr>--%>
                    <td id="${hiddenCol}"><input type="hidden"  value="%{#r.get(#hiddenCol)}" /></td>
                    </s:iterator>
                </tr>
        </s:iterator>
    </s:if>
<%--</s:if>--%>
<%-- if no results --%>
<s:if test="result.size() <= 0 && searched">
    <tr><td class="remark" colspan="<%= tableCol +1 %>"><s:text name="errors.noResultFound" /></td></tr>
</s:if>
<%-- pagination --%>
<s:if test="result.size() > 0 && searched">
    <tr>
        <td colspan="<%=tableCol + 1%>">
            <jsp:include page="../pagination/paging.jsp"></jsp:include>
        </td>
    </tr>
</s:if>
</table>
