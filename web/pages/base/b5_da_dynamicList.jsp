<%@taglib uri="/struts-tags" prefix="s"%>
<s:if test='da.useDataTable'>
    <link href="falcon-v3.16.0/public/vendors/datatables.net-bs5/dataTables.bootstrap5.min.css" rel="stylesheet">
    <script src="falcon-v3.16.0/public/vendors/datatables.net/jquery.dataTables.min.js"></script>
    <script src="falcon-v3.16.0/public/vendors/datatables.net-bs5/dataTables.bootstrap5.min.js"> </script>
    <script src="falcon-v3.16.0/public/vendors/datatables.net-fixedcolumns/dataTables.fixedColumns.min.js"> </script>
    
    <script language="javascript">
        $(document).ready(function() {
            var table = $('#dataTableId').DataTable({
                "order": [],
                "columnDefs": [ {
                  "targets"  : 'no-sort',
                  "orderable": false,
                }],
                "oLanguage": {
                    "sSearch": "<s:text name="filterResult"/>:"
                }
            });
            table.columns().iterator( 'column', function (ctx, idx) {
                $( table.column(idx).header() ).append('<span class="sort-icon"/>');
            } );
        });
    </script>
</s:if>
<%int tableCol = 0;%>
<div class="table-responsive">
    <table <s:if test="da.isGenerateDynamicRpt">id="dynamicListTable"</s:if><s:elseif test="da.useDataTable">id="dataTableId"</s:elseif> class="table table-sds table-sm fs--1 table-striped table-hover" width="100%">
        <thead class="bg-200 text-900">
            <!-- display the columns -->
            <tr>
                <s:if test='da.hideDeleteButton.equals("N")'>
                    <%--<s:if test="has_right('delete')">--%>
                    <s:if test="da.has_right2('DynamicAction','delete',da.action)">
                        <th width="1%" class="align-middle white-space-nowrap <s:if test="da.useDataTable">no-sort</s:if>">
                            <div class="form-check fs-0 mb-0">
                                <s:if test="da.result.size() > 0">
                                    <input type="checkbox"  id="cbselect" class="selectAll form-check-input" name="cbselect" onClick="toggleCheckboxByName(this,'selected');">
                                    <%--<input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, ids);">--%>
                                </s:if>
                                <s:else>
                                    <input type="checkbox" id="cbselect" class="selectAll form-check-input" name="cbselect" disabled >
                                </s:else>
                                <label for="cbselect" class="form-check-label"></label>
                            </div>
                        </th>
                    </s:if>
                </s:if>
                <%tableCol = 0;%>
                <s:iterator value="da.displayFieldsHeader" status="columnStatus" var="column">
                    <%tableCol++;%>
                    <s:set var="disp" value="da.displayFields[#columnStatus.index]"/>
                    <th class="align-middle white-space-nowrap <s:if test="da.useDataTable && !(da.isSortingField(#disp))">no-sort</s:if><s:else>sort</s:else>" style="<s:property value='%{da.getFieldStyleFormat(#disp, "header", "", #column)}' escapeHtml="true"/>" >
                        <s:if test="da.isSortingField(#disp) && !da.manualParam">
                            <s:if test="!(da.useDataTable)">
                                <a class="thLink" href="javascript:sortField('${disp}')"><s:property value="%{#column}" escapeHtml="true"/></a>
                                <s:if test="(da.dynamicSortBy == #disp) && (da.result.size() > 0)">
                                    <s:if test='da.dynamicSortOrder == "D"'>
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
                            <s:if test="!(da.useDataTable) && !da.manualParam">
                                <a class="thLink" href="javascript:sortField('${disp}')"><s:property value="%{#column}" escapeHtml="true"/></a>
                                <s:if test="(da.dynamicSortBy == #disp) && (da.result.size() > 0)">
                                    <s:if test='da.dynamicSortOrder == "D"'>
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
            <s:if test="da.result.size() > 0">
                <%
                    String[] cssClass = {"value", "valueB"};
                    String[] cssCenterClass = {"valueCenter", "valueCenterB"};
                    String pageNo = request.getParameter("pageNo");
                    if (com.sains.common.util.Validator.isEmpty(pageNo)) {
                        pageNo = "1";
                    }
                    int row = 0, firstIndex = (Integer.parseInt(pageNo) - 1) * 10 + 1;
                %>
                <s:iterator value="da.result" status="resultStatus" var="r">
                    <tr>
                        <s:if test='da.hideDeleteButton.equals("N")'><s:if test="da.has_right2('DynamicAction','delete',da.action)">
                                <td class="align-middle white-space-nowrap <s:if test="#resultStatus.odd == true ">odd</s:if><s:else>even</s:else>" width="1%">
                                    <div class="form-check fs-0 mb-0">
                                            <input type="checkbox" name="selected" class="checkbox_child form-check-input" id="_${resultStatus.index}" value="<s:property value="%{da.getResultPrimaryKey(#resultStatus.index)}" escapeHtml="true"/>" onclick="toggleSelectAll()">
                                        <label for="_${resultStatus.index}" class="form-check-label"></label>
                                        <%--<s:checkbox theme="simple" name="selected" cssClass="checkbox_child" id="%{#resultStatus.index}" fieldValue="%{getResultPrimaryKey(#resultStatus.index)}" onclick="toggleSelectAll();"/>--%>
                                    </div>
                                                
                                    <%--<s:checkbox theme="simple" name="selected" id="ids" fieldValue="%{getResultPrimaryKey(#resultStatus.index)}" onclick="checkToggleCheckbox(cbselect, ids)"/>--%>
                                    <%firstIndex++;%>
                                </td>
                            </s:if></s:if>
                        <s:iterator value="da.displayFields" var="displayCol">
                            <td class="align-middle white-space-nowrap" id="${displayCol}" style="word-wrap: break-word">
                                <s:if test='da.getCheckEditLink(#displayCol).equals("true")'>
                                    <s:set var="theTarget" value="%{da.getOpenLinkAtNewPage(#displayCol)}"/>
                                    <s:if test='da.isMixConfig'>
                                        <s:url var="editLink" action="%{da.editPageURL}">
                                            <s:param name="id" value="%{da.getResultPrimaryKey(#resultStatus.index)}"></s:param>
                                            <s:param name="action" value="%{da.action}"></s:param>
                                            <s:param name="searchCode" value="%{da.searchCode}"></s:param>
                                        </s:url>
                                    </s:if><s:else>
                                        <s:url var="editLink" action="%{da.editPageURL}">
                                            <s:param name="id" value="%{da.getResultPrimaryKey(#resultStatus.index)}"></s:param>
                                            <s:param name="action" value="%{da.action}"></s:param>
                                        </s:url>
                                    </s:else>
                                    <s:if test="#r.get(#displayCol) instanceof java.sql.Timestamp">
                                        <s:a href="%{editLink}" cssStyle="%{da.editLinkStyle}" target="%{theTarget}">
                                            <div style="width: 100%"><s:text name="date_default_datetime"><s:param value="%{#r.get(#displayCol)}" /></s:text></div>
                                        </s:a>
                                    </s:if>
                                    <s:else>
                                        <s:a href="%{editLink}" cssStyle="%{da.editLinkStyle}" target="%{theTarget}"><div style="width: 100%"><s:property value="%{#r.get(#displayCol)}"/></div></s:a>
                                    </s:else>
                                </s:if><s:elseif test='da.getCheckEditLink(#displayCol+"_otherLink").equals("true")'>
                                    <s:set var="theTarget" value="%{da.getOpenLinkAtNewPage(#displayCol)}"/>
                                    <s:url var="displayFieldLink" action='%{da.getDisplayFieldLink(#displayCol+"_otherLink", #r)}'></s:url>
                                    <s:set var="lDisplayStyle" value='%{da.getDisplayFieldLink(#displayCol+"_style", #r)}'/>
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
                                    <s:if test='da.getCheckFormatField(#displayCol).equals("true")'>
                                        <s:property escapeHtml="true" value="%{da.getFormattedField(#displayCol, #resultStatus.index)}"/>
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
                        <s:iterator value="da.hiddenColumns" var="hiddenCol">
                    <span id="${hiddenCol}"><input type="hidden"  value="%{#r.get(#hiddenCol)}" /></span>
                    </s:iterator>
                </tr>
            </s:iterator>
        </s:if>
        <s:else>
            <tr><td class="remark" colspan="<%= tableCol + 1%>"><center><s:text name="common.noRecordFound" /></center></td></tr>
        </s:else>
        <%--</s:if>--%>
        <%-- if no results --%>
        <%--<s:if test="result.size() <= 0 && searched">
            <tr><td class="remark" colspan="<%= tableCol + 1%>"><s:text name="common.noRecordFound" /></td></tr>
            </s:if>--%>
        </tbody>
    </table>
</div>

<%-- pagination --%>
<s:if test="da.result.size() > 0 && da.searched && !(da.useDataTable)">
    <jsp:include page="../pagination/da_paging.jsp"></jsp:include>
</s:if>
