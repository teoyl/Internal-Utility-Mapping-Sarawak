<%-- 
    Document   : viewChecklist
    Created on : Dec 5, 2018, 9:56:56 AM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><s:text name="utimaps.setup.checklist.title" /></title>
        
    <style nonce="EuTVqS192VKl">
        .row {
            margin-top: 15px;
            margin-bottom: 15px;
        }            
        select.input-sm, input[type="search"] {
            border-radius: 5px;
        }
        .dataTables_wrapper .dataTables_paginate .paginate_button:active {
            background: transparent;
            box-shadow: unset;
            color: unset;
        }
        .table-striped > tbody > tr:nth-of-type(2n+1) {
            background-color: #f3f2f0;
        }
        .pagination {
            /*border: 1px solid #dddddd;*/
            /*background-color: #dddddd;*/
        }
        .pagination > li > a {
            vertical-align: bottom;
            /*color: #5fa4ca;*/
            color: #333;
        }
        .pagination > .active > a {
            /*background-color: transparent;*/
            background-color: #D6D6D6;
            color: #FFF;
        }
        .pagination > .active > a:hover, .pagination> li > a:hover, .pagination> li > a:active, .pagination > .disabled > a:hover, .pagination > .active > a:focus, .pagination> li > a:focus, .pagination > .active > a:active, .pagination> li > a:active {
            /*background-color: transparent;*/
            background-color: #D6D6D6;
        }
        .pagination > .active > a:active {
            color: #FFF;
        }
        .pagination > li > a:active, .pagination > .next > a:focus {
            /*color: #5fa4ca;*/
        }
        .pagination > .disabled > a {
            background-color: transparent
        }
        .pagination > .disabled > a:focus, .pagination > .disabled > a:hover {
            background-color: transparent;
        }
        .paginate_button > a {
            /*font-weight: bold;*/
            /*font-size: larger !important;*/
        }
        .paginate_button.previous, .paginate_button.previous > a {
            min-width: 6em !important;
        }
        .paginate_button.next, .paginate_button.next > a {
            min-width: 4em !important;
        }
        .pagination > .disabled > a, .pagination > .disabled > a:hover {
            cursor: default;
        }
        table.dataTable tbody td.select-checkbox:before, table.dataTable tbody th.select-checkbox:before {
            margin-top: 0px;
        }
        table.dataTable tbody th, table.dataTable tbody td {
            padding: 10px 10px 20px;
        }
        table.dataTable tr.selected td.select-checkbox:after, table.dataTable tr.selected th.select-checkbox:after {
            margin-top: -3px;
            margin-left: -6px;
        }
        .notification-content {
            font-size: 1.2em;
            line-height: 1.5em;
        }            
        table.dataTable thead .sorting, table.dataTable thead .sorting_asc {
            background: none;
        }
        th {
            font-size: 1.2em;
        }
        .read {
            color: green;
            font-size: 2em;
        }

        .tname:hover {
            text-decoration: underline;
            cursor: pointer;
        }

        td:first-child:hover {
            cursor: pointer;
        }
        .btn-text {
            margin-left: 5px;
        }
        .alert {
            padding: 10px;
        }
        .alert-success {
            color: #155724;
            background-color: #d4edda;
            border-color: #c3e6cb;
        }
        .close {
            margin-left: 10px;
        }
    </style>
    
    <script type="text/javascript" language="javascript" src="include/print/jquery.dataTables.min.js"></script>
    <script type="text/javascript" language="javascript" src="include/print/dataTables.buttons.min.js"></script>
    <script type="text/javascript" language="javascript" src="include/datatable/dataTables.select.min.js"></script>
    <link href="include/datatable/dataTables.bootstrap.min.css" rel="stylesheet"/>
    <link href="include/datatable/select.dataTables.min.css" rel="stylesheet"/>
    <link href="include/datatable/jquery.dataTables.min.css" rel="stylesheet"/>

    <script nonce="EuTVqS192VKl">
        $(document).ready(function() {                               
            var table = $('#example').DataTable( {
                columnDefs: [ {
                   orderable: false,
                   className: 'select-checkbox',
                   targets: 0
                },
                {width: 20, targets: 0},
                {width: 500, targets: 1},
                {width: 250, targets: 2},
                {width: 200, targets: 3},
                {width: 100, targets: 4},
                {width: 100, targets: 5},
                ],
                select: {
                   style: 'multi',
                   selector:  'td:first-child'
               }
//                    order: [[ 1, 'asc' ]]
            } );
//            table.select.selector( 'td:first-child' );
            $('#checkBox').on('click', function() {
                console.log("clicked")
                if ($('#checkBox').is(':checked')) {
                    console.log("checked", table.rows())
                    table.rows({selected: true}).select();
                }
                else {
                    console.log("no checked")
                    table.rows().deselect();
                }
            });

            $('#deleteChecklistSetup').click( function (e) {
                e.preventDefault();
                var string = '';
                var ids = $.map(table.rows('.selected').data(), function (item) {
                    return item['DT_RowId'];
                });
                
                console.log(ids)

                if(ids.length === 0 ) {
                    string = '';
                } else {
                    string = ids.join(',');
                }
                
                bootbox.confirm("Are you sure want to delete?", function(result) {
                    if(result) {
                        $.ajax({
                            url: "deleteChecklistSetup",
                            method: "get",
                            data: {
                                   "id": string
                            },
                            beforeSend:function(){
                                if(ids.length === 0) {
                                    bootbox.alert("Please select at least one checklist to be deleted.");
                                }
                            },
                            success: function() {
                                ids.forEach(removeRow);
                                console.log(ids.length);
                                if(ids.length > 0) {
                                    $('.bottom-left').notify({
                                        message: {
                                            text: 'Delete Successfully.',
                                            closable: false
                                        }
                                    }).show();
                                }
                            },
                            ajaxStop: function() {
                                $(".overlay-loader").hide();
                            },
                            error: function() {
                                bootbox.alert("Error");
                            }
                        });
                    }
                });

            });

            function removeRow(value, key, map) {
                table.row("#"+value).remove().draw();
            }
        } );
    </script>
</head>
<body>
    <div class="x_panel">
    <div class="row">
        <div class="col-md-12 col-xs-12">
            
            <a href="loadAddPageChecklistSetup"><button class="btn btn-sm btn-falcon-default" data-toggle="tooltip" title=""><i class="fa fa-plus"></i><span class="btn-text">Add</span></button></a>
            <button class="btn btn-sm btn-falcon-default" type="submit" data-toggle="tooltip" title="" name="deleteChecklist" id="deleteChecklistSetup"><i class="fa fa-trash"></i><span class="btn-text">Delete</span></button>

            <table id="example" class="table table-striped" cellspacing="0" width="100%">
                <thead>
                    <tr>
                        <th class="select-checkbox"><div><input id="checkBox" type="checkbox"></div></th>
                        <th><s:text name="utimaps.setup.checklist.name" /></th>
                        <th><s:text name="utimaps.setup.checklist.processType" /></th>
                        <th><s:text name="utimaps.setup.checklist.status" /></th>
                        <th><s:text name="utimaps.setup.checklist.createdBy" /></th>
                        <th><s:text name="utimaps.setup.checklist.createdDate" /></th>
                    </tr>
                </thead>
                <tbody>
                    <s:iterator value="listResult" var="notificationList" status="fieldStatus">
                        <tr id="<s:property value="%{#notificationList.checklist_id}" />">
                            <td></td>
                            <td class="tname"><a href="loadEditPageChecklistSetup.action?id=<s:property value='%{#notificationList.checklist_id}'/> "><s:property value="%{#notificationList.checklist_name}" /></a></td>
                            <td><s:property value="%{#notificationList.process_type}" /></td>
                            <td><s:text name="checklist.status.%{#notificationList.checklist_status}"/></td>
                            <td><s:property value="%{#notificationList.created_by}" /></td>
                            <td><s:property value="%{#notificationList.created_date_only_str}" /></td>
                        </tr>
                    </s:iterator>
                </tbody>
            </table>
        </div>
        <div class='notifications bottom-left'></div>
    </div> 
    </div>
</body>