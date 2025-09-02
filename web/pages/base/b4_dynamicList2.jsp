<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="pages/scripts/common.js"></script>
<%int tableCol = 0;%>
    <div class="table-responsive">
        <table class="table table-sds table-condensed table-striped table-hover " border="0" cellpadding="1" cellspacing="1" width="100%">
            <!-- display the columns -->
            <thead>
                <tr class="CLASS_TABLE_HEADER" width="2%">
                <th width="1%">
                    <div class="checkbox check-success tableListingCheckbox">
                        <s:if test="getDynamicResult(list_param).size() > 0">
                            <label class="kt-checkbox kt-checkbox--brand">
                                <input type="checkbox"  id="cbselect" class="selectAll" name="cbselect" onClick="toggleCheckboxByName(this,'${list_param}_selected');">
                                <%--<input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, ids);">--%>
                                <span></span>
                            </label>
                        </s:if>
                        <s:else>
                            <label class="kt-checkbox kt-checkbox--brand">
                                <input type="checkbox" id="cbselect" class="selectAll" name="cbselect" disabled >
                                <span></span>
                            </label>
                        </s:else>
                        <!--<label for="cbselect" class="tableListingCheckbox"></label>-->
                    </div>
                    <%--<s:if test="getDynamicResult(list_param).size() > 0">
                        <input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, ${list_param}_ids);">
                    </s:if>
                    <s:else>
                        <input type="checkbox" id="cbselect" name="cbselect" disabled >
                    </s:else>--%>
                </th>
                <%tableCol = 0;%>
                <s:iterator value="getDynamicDisplayFieldHeader(list_param)" status="columnStatus" var="column">
                    <%tableCol++;%>
                    <s:set var="disp" value="getDynamicDisplayField(list_param)[#columnStatus.index]"/>
                    <th style="">
                        <s:if test="isDynamicSortingField(list_param, #disp) == true"><a class="thLink" style="text-decoration: none;" href="javascript:sortField('${disp}')">${column}
                                <s:if test="(dynamicSortBy == #disp) && (getDynamicResult(list_param).size() > 0)">
                                    <s:if test='dynamicSortOrder == "D"'>
                                        <Img src='images/sortdes.gif' border='0'/>
                                    </s:if>
                                    <s:else>
                                        <Img src='images/sortasc.gif' border='0'/>
                                    </s:else>
                                </s:if>
                            </a></s:if>
                        <s:else>${column}</s:else>
                        </th>
                </s:iterator>
                <!-- <s:iterator value="getDynamicDisplayFieldHeader(list_param)" status="columnStatus" var="column">
                                        <th>
                    ${column}
                                        </th>
                </s:iterator> -->
            </tr>
        </thead>
        <tbody>
            <!-- display the data -->
            <%--<s:if test="getDynamicColumns(list_param).size() > 0">--%>
            <s:if test="getDynamicResult(list_param).size() > 0">
                <%
                    String[] cssClass = {"value", "valueB"};
                    String[] cssCenterClass = {"valueCenter", "valueCenterB"};
                    String pageNo = request.getParameter("pageNo");
                    if (com.sains.common.util.Validator.isEmpty(pageNo)) {
                        pageNo = "1";
                    }
                    int row = 0, firstIndex = (Integer.parseInt(pageNo) - 1) * 10 + 1;
                %>
                <s:iterator value="getDynamicResult(list_param)" status="resultStatus" var="r">
                    <tr>
                        <td class="<s:if test="#resultStatus.odd == true ">odd</s:if><s:else>even</s:else>" width="1%">
                            <div class="checkbox check-success tableListingCheckbox">
                                <label class="kt-checkbox kt-checkbox--brand">
                                    <input type="checkbox" name="${list_param}_selected" class="checkbox_child" id="${list_param}_ids_${resultStatus.index}" value="<s:property value="%{getDynamicResultPrimaryKey(list_param,#resultStatus.index)}"/>" onclick="toggleSelectAll()">
                                    <span></span>
                                </label>
                                <%--<label for="${list_param}_ids_${resultStatus.index}" class="tableListingCheckbox"></label>--%>
                            </div>
                            <%--<s:checkbox theme="simple" name="%{list_param}_selected" id="%{list_param}_ids" fieldValue="%{getDynamicResultPrimaryKey(list_param, #resultStatus.index)}" onclick="checkToggleCheckbox(cbselect, %{list_param}_ids)"/><%firstIndex++;%>--%>
                        </td>
                        <s:iterator value="getDynamicDisplayField(list_param)" var="displayCol">
                            <td class="<s:if test="#resultStatus.odd == true ">odd</s:if><s:else>even</s:else>" id="${displayCol}" style="word-wrap: break-word; <s:property value='%{getFieldStyleFormat(#displayCol, "detail", #r.get(#displayCol), #r)}'/>">
                                <s:if test='getCheckEditLink(#displayCol).equals("true")'>
                                    <s:url var="editLink" action="processEdit%{action}">
                                        <s:param name="id" value="%{getDynamicResultPrimaryKey(list_param, #resultStatus.index)}"></s:param>
                                        <s:param name="action" value="%{action}"></s:param>
                                    </s:url>
                                    <%--<s:a href="%{editLink}"><s:property value="%{#r.get(#displayCol)}"/></s:a>--%>
                                    <s:if test="#r.get(#displayCol) instanceof java.sql.Timestamp">
                                        <s:a href="%{editLink}" cssStyle="%{editLinkStyle}">
                                            <s:text name="date_default_datetime"><s:param value="%{#r.get(#displayCol)}" /></s:text>
                                        </s:a>
                                    </s:if>
                                    <s:else>
                                        <s:a href="%{editLink}" cssStyle="%{editLinkStyle}"><s:property value="%{#r.get(#displayCol)}"/></s:a>
                                    </s:else>
                                </s:if>
                                <s:else>
                                    <s:if test='getCheckFormatField(#displayCol).equals("true")'>
                                        <s:property value="%{getFormattedField_dynamic(list_param, #displayCol, #resultStatus.index)}"/>
                                    </s:if>
                                    <s:else>
                                        <s:if test="#r.get(#displayCol) instanceof java.sql.Timestamp">
                                            <s:text name="date_default_datetime"><s:param value="%{#r.get(#displayCol)}" /></s:text>
                                        </s:if>
                                        <s:else>
                                            <s:property value="%{#r.get(#displayCol)}" escapeHtml="false"/>
                                        </s:else>
                                    </s:else>
                                </s:else>
                            </td>
                        </s:iterator>
                        <s:iterator value="hiddenColumns" var="hiddenCol">
                    <span id="${hiddenCol}"><input type="hidden"  value="%{#r.get(#hiddenCol)}"/></span>
                    </s:iterator>
                </tr>
            </s:iterator>
        </s:if>
        <%--</s:if>--%>
        <%-- if no results --%>
        <s:if test="getDynamicResult(list_param).size() <= 0">
            <tr><td align="center" class="remark" colspan="<%=tableCol + 1%>"><s:text name="common.noRecordFound" /></td></tr>
            </s:if>

        </tbody>
    </table>
</div>

<%-- pagination --%>
<s:if test="getDynamicResult(list_param).size() > 0">
    <tr><td colspan="<%=tableCol + 1%>">
            <s:include value="../pagination/b4_dynamicListPaging.jsp">
                <s:param name="searchingParam" value="%{list_param}" />
                <s:param name="dynamicPageNo" value="%{getDynamicPageNo(list_param)}"></s:param>
            </s:include>
        </td></tr>
</s:if>