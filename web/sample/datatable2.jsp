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
            var table, pageLength='', sortField=0, sortFieldOrder="", initCount;
            function gotoPage(page) {
                var info = table.page.info();
                $("#dt_pageNo").val(page);
                $("#dt_pageSize").val(info.length);
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
//                    "bInfo": false,
                    "searching": false,
//                    "order": [[sortField, sortFieldOrder]],
//                    for sorting the input field
//                    "columnDefs": [
//                        {"orderDataType": "dom-input", "targets": 0}
////                        ,{"orderDataType": "dom-input", "targets": 1}
//                    ],
                    
                    <s:property value="%{dt_columns}"/>
                    <s:property value="%{dt_defaultOrder}"/>
                    "columnDefs": [ 
                        {
                        "targets": 0,
                        "orderable": false,
                        "render": function ( data, type, row, meta ) {
                            info = table.page.info();
                            return (info.page * info.length) + (meta.row+1)+'.';
                        }},
                        {
                        "targets": 1,
                        "render": function (data, type, row, meta ) {
                            var sanitized = $.fn.dataTable.render.text().display(row['href']);
                            return '<a href="'+sanitized+'">'+data+'</a>';
                        }
                      } ],
                    "processing": true,
                    "serverSide": true,
                    "ajax": "datatable_ajaxSample",
//                    "deferLoading": 25,
                    "length" : pageLength
                });
                initCount = 0;
//                table.on('length.dt', function () {
//                    gotoPage(1);
//                });
//                table.on('page.dt', function () {
//                    var info = table.page.info();
//                    $("#dt_pageNo").val(info.page + 1);
//                    $("#dt_pageSize").val(info.length);
//                    var order = table.order();
//                    if (order[0]) {
//                        $("#dt_order").val(order[0][1]);
//                        $("#dt_order_by").val(order[0][0]);
//                    }
////                    api.paging=false; //so that paging button no effect
//                    divSubmitForm("dtLoadSample", "dtFormId", "dtDiv");
//                });
                
                table.columns().iterator('column', function (ctx, idx) {
                    $(table.column(idx).header()).append('&nbsp;<span class="sort-icon"/>');
                });
            }
            $(document).ready(function () {
                pageLength =  <s:property value="dt_pageSize"/>;
                prepareDatatable("example");
            });
        </script>
        <style>
            tfoot input {
                width: 100%;
                padding: 3px;
                box-sizing: border-box;
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
                        <th>First name</th>
                        <th>Last name</th>
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
<%--                    <s:iterator value="recordList" var="rec" status="recStatus">
                        <tr>
                            <td><s:property value="%{getDt_rowIdx(#recStatus.index+1)}"/></td>
                            <td>First Name ${recStatus.index+1}</td>
                            <td>Last Name ${recStatus.index+1}</td>
                        </tr>
                    </s:iterator>--%>
                </tbody>
            </table>
        </div>
        <form id="dtFormId">
            <input type="hidden" name="dt_pageSize" id="dt_pageSize"/>
            <input type="hidden" name="dt_pageNo" id="dt_pageNo"/>
            <input type="hidden" name="dt_order_by" id="dt_order_by"/>
            <input type="hidden" name="dt_order" id="dt_order"/>
        </form>
    </body>
</html>
