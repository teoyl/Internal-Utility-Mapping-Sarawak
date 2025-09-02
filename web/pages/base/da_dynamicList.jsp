<%@taglib uri="/struts-tags" prefix="s"%>
<s:if test='da.useDataTable'>
    <script type="text/javascript" language="javascript" src="include/print/jquery.dataTables.min.js"></script>
    <script type="text/javascript" language="javascript" src="include/print/dataTables.buttons.min.js"></script>
    <script type="text/javascript" language="javascript" src="include/print/buttons.flash.min.js"></script>
    <script type="text/javascript" language="javascript" src="include/print/jszip.min.js"></script>
    <script type="text/javascript" language="javascript" src="include/print/pdfmake.min.js"></script>
    <script type="text/javascript" language="javascript" src="include/print/vfs_fonts.js"></script>
    <script type="text/javascript" language="javascript" src="include/print/buttons.html5.min.js"></script>
    <script type="text/javascript" language="javascript" src="include/print/buttons.print.min.js"></script>
    <link href="include/datatable/dataTables.bootstrap.min.css" rel="stylesheet"/>
    <link href="include/datatable/jquery.dataTables.min.css" rel="stylesheet"/>
    <style>
        table.dataTable tbody th, table.dataTable tbody td {
            padding: 2px 3px;
        }
        table.dataTable thead .sorting:after {
            content:'';
        }
        table.dataTable thead .sorting_asc:after {
            content:'';
        }
        table.dataTable thead .sorting_desc:after {
            content:'';
        }
        table.dataTable thead th {
          background: transparent !important;
          white-space: nowrap;
        }

/*            table.dataTable thead span.sort-icon {
          display: inline-block;
          padding-left: 5px;
          width: 16px;
          height: 16px;
        }*/

        table.dataTable thead .sorting span { 
            content: 'text that follows 2';
            background: url(images/sortingIcon.gif) left center no-repeat;
            vertical-align: middle;
            background-size: auto auto;
            padding-left: 20px;
        }
        table.dataTable thead .sorting_desc span { 
            content: 'text that follows 3';
            background: url(images/sortdes.gif) left center no-repeat;
            vertical-align: middle;
            background-size: auto auto;
            padding-left: 20px;
        }
        table.dataTable thead .sorting_asc span { 
            content: 'text that follows 2';
            background: url(images/sortasc.gif) left center no-repeat;
            vertical-align: middle;
            background-size: auto auto;
            padding-left: 20px;
        }
    </style>
    
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
    <table <s:if test="da.isGenerateDynamicRpt">id="dynamicListTable"</s:if><s:elseif test="da.useDataTable">id="dataTableId"</s:elseif> class="table table-sds table-condensed table-striped table-hover" width="100%">
        <thead>
            <!-- display the columns -->
            <tr>
                <s:if test='da.hideDeleteButton.equals("N")'>
                    <%--<s:if test="has_right('delete')">--%>
                    <s:if test="da.has_right2('DynamicAction','delete',da.action)">
                        <th width="1%" <s:if test="da.useDataTable">class="no-sort"</s:if>>
                            <div class="checkbox check-success">
                                <s:if test="da.result.size() > 0">
                                    <input type="checkbox"  id="cbselect" class="selectAll" name="cbselect" onClick="toggleCheckboxByName(this,'selected');">
                                    <%--<input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, ids);">--%>
                                </s:if>
                                <s:else>
                                    <input type="checkbox" id="cbselect" class="selectAll" name="cbselect" disabled >
                                </s:else>
                                <label for="cbselect"></label>
                            </div>
                        </th>
                    </s:if>
                </s:if>
                <%tableCol = 0;%>
                <s:iterator value="da.displayFieldsHeader" status="columnStatus" var="column">
                    <%tableCol++;%>
                    <s:set var="disp" value="da.displayFields[#columnStatus.index]"/>
                    <th <s:if test="da.useDataTable && !(da.isSortingField(#disp))">class="no-sort"</s:if> style="<s:property value='%{da.getFieldStyleFormat(#disp, "header", "", #column)}' escapeHtml="true"/>" >
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
                                <td class="<s:if test="#resultStatus.odd == true ">odd</s:if><s:else>even</s:else>" width="1%">
                                    <div class="checkbox check-success">
                                            <input type="checkbox" name="selected" class="checkbox_child" id="_${resultStatus.index}" value="<s:property value="%{da.getResultPrimaryKey(#resultStatus.index)}" escapeHtml="true"/>" onclick="toggleSelectAll()">
                                        <label for="_${resultStatus.index}"></label>
                                        <%--<s:checkbox theme="simple" name="selected" cssClass="checkbox_child" id="%{#resultStatus.index}" fieldValue="%{getResultPrimaryKey(#resultStatus.index)}" onclick="toggleSelectAll();"/>--%>
                                    </div>
                                                
                                    <%--<s:checkbox theme="simple" name="selected" id="ids" fieldValue="%{getResultPrimaryKey(#resultStatus.index)}" onclick="checkToggleCheckbox(cbselect, ids)"/>--%>
                                    <%firstIndex++;%>
                                </td>
                            </s:if></s:if>
                        <s:iterator value="da.displayFields" var="displayCol">
                            <td id="${displayCol}" style="word-wrap: break-word">
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
