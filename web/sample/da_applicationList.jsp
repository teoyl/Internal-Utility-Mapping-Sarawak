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
                        <s:if test="da.isSortingField(#disp)">
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
                        <td>
                            <s:hidden name="application_id" value='%{#r.get("application_id")}'/>
                            <s:textfield name="application_code" id="application_code" value='%{#r.get("application_code")}'/>
                        </td>
                        <td>
                            <s:textfield name="application_name" id="application_name" value='%{#r.get("application_name")}'/>
                        </td>
                        <td>
                            <s:hidden name="application_type" value='%{#r.get("application_type")}'/>
                            <s:text name='application.type.%{#r.get("application_type")}'/>
                        </td>
                        <td>
                            <s:hidden name="module_name" value='%{#r.get("module_name")}'/>
                            <s:property value='%{#r.get("module_name")}'/>
                        </td>
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
    <jsp:include page="/pages/pagination/da_paging.jsp"></jsp:include>
</s:if>
