<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sample Datatable</title>
        <script type="text/javascript" language="javascript" src="include/daterangepicker/moment.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/jquery.dataTables.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/dataTables.buttons.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/buttons.flash.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/jszip.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/pdfmake.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/vfs_fonts.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/datetime-moment.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/buttons.html5.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/buttons.print.min.js"></script>
        <link href="include/datatable/dataTables.bootstrap.min.css" rel="stylesheet"/>
        <link href="include/datatable/jquery.dataTables.min.css" rel="stylesheet"/>
        <script type="text/javascript">
            var table, pageLength='', sortField=0, sortFieldOrder="";
            function gotoPage(page) {
                var info = table.page.info();
                $("#dt_pageNo").val(page);
                $("#dt_pageSize").val(info.length); <%-- length --%>
                $("#dt_start").val(((page-1) * info.length) ); <%-- start --%>
                
                pageLength = info.length;
                var order = table.order();
                if (order[0]) {
                    $("#dt_order").val(order[0][1]);
                    $("#dt_order_by").val(order[0][0]);
                }
//                    api.paging=false; //so that paging button no effect
                divSubmitForm("dtLoadSample", "dtFormId", "dtDiv");
            }
            function prepareDatatable(tableId) {
                table = $('#'+tableId).DataTable({
//                    "paging":false,
//                    "ordering": false,
                    "bInfo": false,
                    "searching": false,
                    "order": [[sortField, sortFieldOrder]],
                    "columnDefs": [ 
                        {
                            "targets": 0,
                            "orderable": false
                        }
                    ],
//                    "columnDefs": [
//                        {"orderDataType": "dom-input", "targets": 0}
////                        ,{"orderDataType": "dom-input", "targets": 1}
//                    ],
//                    "processing": true,
//                    "serverSide": true,
//                    "ajax": "datatableSample",
//                    "deferLoading": 25,
                    "pageLength": pageLength==''?10:pageLength
//                    "length" : 25
                });

                table.on('length.dt', function () {
                    gotoPage(1);
                });
                table.on('page.dt', function () {
                    var info = table.page.info();
                    $("#dt_pageNo").val(info.page + 1);
                    $("#dt_pageSize").val(info.length);
                    var order = table.order();
                    if (order[0]) {
                        $("#dt_order").val(order[0][1]);
                        $("#dt_order_by").val(order[0][0]);
                    }
//                    api.paging=false; //so that paging button no effect
                    divSubmitForm("dtLoadSample", "dtFormId", "dtDiv");
                });
                $("#"+tableId+"_paginate").addClass("hidden");
                
                table.on('order', function () {
                    gotoPage(1);
                });
                
                table.columns().iterator('column', function (ctx, idx) {
                    $(table.column(idx).header()).append('&nbsp;<span class="sort-icon"/>');
                });
                $.fn.dataTable.ext.order['dom-input'] = function (settings, col) {
                    return this.api().column(col, {order: 'index'}).nodes().map(function (td, i) {
                        if ($(td).find('input,select').length > 0) {
                            return $(td).find('input,select').val(); //change to find by class = "sortMe"
                        } else {
                            return $(td).html();
//                            return $('input,select', td).val();
                        }
                    });
                }
            }
            $(document).ready(function () {
                sortField = '<s:property value="%{dt_order_by}"/>';
                sortFieldOrder = '<s:property value="%{dt_order}"/>';
                prepareDatatable("example");
            });
        </script>
        <style>
            .dataTables_paginate {
                float: right;
                text-align: right;
                padding-top: 0em !important;
            }
            table.dataTable tbody td {
                padding: 6px 6px 0px 6px;
            }
            table.dataTable tfoot th, table.dataTable tfoot td {
                border-top: 0px;
                white-space: pre;
            }
            .pull-right {
                margin-right: 20px;
            }
            .dt-button {
                padding: 5px 5px 5px 5px;
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

            /*            table.dataTable thead .sorting_asc_disabled span { 
                            background: url('http://cdn.datatables.net/plug-ins/3cfcc339e89/integration/bootstrap/images/sort_asc_disabled.png') no-repeat center right; }
                        table.dataTable thead .sorting_desc_disabled span { background: url('http://cdn.datatables.net/plug-ins/3cfcc339e89/integration/bootstrap/images/sort_desc_disabled.png') no-repeat center right; }*/
        </style>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <div id="dtDiv">
            <table id="example" class="display" style="width:100%">
                <thead>
                    <tr>
                        <th width="1%">No.</th>
                        <th>Column 1</th>
                        <th>Column 2</th>
                    </tr>
                </thead>
                <tbody>
<!--                                        <tr><td data-order=""><s:select cssClass="form-control sds-dropdown" list="sampleList" listKey="keyData" listValue="valueData"/></td><td>Last name 1</td></tr>
                    <tr><td data-order=""><input type="text" class="form-control" name="field1r2"/></td><td>Last name 2</td></tr>
                    <tr><td data-order=""><input type="text" class="form-control" name="field1r3"/></td><td>Last name 3</td></tr>
                    <tr><td data-order=""><input type="text" class="form-control" name="field1r4"/></td><td>Last name 4</td></tr>
                    <tr><td data-order=""><input type="text" class="form-control" name="field1r5"/></td><td>Last name 5</td></tr>
                    <tr><td data-order=""><input type="text" class="form-control" name="field1r1"/></td><td>Last name 1</td></tr>
                    <tr><td data-order=""><input type="text" class="form-control" name="field1r2"/></td><td>Last name 2</td></tr>
                    <tr><td data-order=""><input type="text" class="form-control" name="field1r3"/></td><td>Last name 3</td></tr>
                    <tr><td data-order=""><input type="text" class="form-control" name="field1r4"/></td><td>Last name 4</td></tr>
                    <tr><td data-order=""><input type="text" class="form-control" name="field1r5"/></td><td>Last name 5</td></tr>-->
<%--                    <s:iterator value="recordList" var="rec" status="recStatus">
                        <tr><td><s:property value="%{getDt_rowIdx(#recStatus.index+1)}"/></td>
                            <td>First name <s:property value="%{#rec}"/></td><td>Last name <s:property value="%{#rec}"/></td></tr>
                    </s:iterator>--%>
                    <s:iterator value="recordList" var="rec" status="recStatus">
                        <tr>
                            <td><s:property value="%{getDt_rowIdx(#recStatus.index+1)}"/></td>
                            <td><s:textfield name="col_1" cssClass="form-control" value='%{#rec.col_1}'/></td>
                            <td><s:textfield name="col_2" cssClass="form-control" value="%{#rec.col_2}"/></td>
                        </tr>
                    </s:iterator>
                </tbody>
            </table>
            <div class="dataTables_wrapper">
                <jsp:include page="/pages/pagination/paging_dt.jsp"></jsp:include>
            </div>
        </div>
        <form id="dtFormId">
            <input type="hidden" name="length" id="dt_pageSize"/>
            <input type="hidden" name="dt_pageNo" id="dt_pageNo"/>
            <input type="hidden" name="order[0][column]" id="dt_order_by"/>
            <input type="hidden" name="order[0][dir]" id="dt_order"/>
            <input type="hidden" name="start" id="dt_start"/>
        </form>
    </body>
</html>
