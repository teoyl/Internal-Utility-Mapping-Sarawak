<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="DqRptGen.appName"/></title>
        <link href="falcon-v3.16.0/public/vendors/datatables.net-bs5/dataTables.bootstrap5.min.css" rel="stylesheet">
        <script src="falcon-v3.16.0/public/vendors/datatables.net/jquery.dataTables.min.js"></script>
        <script src="falcon-v3.16.0/public/vendors/datatables.net-bs5/dataTables.bootstrap5.min.js"> </script>
        <script src="falcon-v3.16.0/public/vendors/datatables.net-fixedcolumns/dataTables.fixedColumns.min.js"> </script>
        <script type="text/javascript">
            $(document).ready(function () {
//                $.fn.dataTable.moment( 'DD MMM YYYY HH:mm:ss' );
//                $.fn.dataTable.moment( 'DD MMM YYYY' );
                var table = $('#dataTableId').DataTable({
                pageLength: 25,
                "columnDefs": [ {
                    "searchable": false,
                    "orderable": false,
                    "targets": 0
                } ],
                buttons: [
                        {
                            extend: 'pdf',
                            title: '<s:property value="%{model.tmpl_name}" escapeJavaScript="true"/>',
                            footer: true,
                            customize: function(doc) {
                                doc.styles.title = {
                                  fontSize: '20',
                                  alignment: 'center'
                                }   
                            },
                            exportOptions: {
                                stripNewlines: false,
                                trim: false
                            }
                        },
                        {
                            extend: 'csv',
                            footer: true,
                            title: '<s:property value="%{model.tmpl_name}" escapeJavaScript="true"/>'
                        },
                        {
                            extend: 'excel',
                            footer: true,
                            title: '<s:property value="%{model.tmpl_name_escapeSQ}" escapeJavaScript="true"/>',
                            exportOptions: {
                                stripNewlines: false,
                                trim: false
                            }
                        }
                ],
                dom: 'Blrtip',
                "order": [],
                "oLanguage": {
                    "sSearch": "<s:text name="filterResult"/>:"
                }
            });
            table.columns().iterator( 'column', function (ctx, idx) {
                $( table.column(idx).header() ).append('<span class="sort-icon"/>');
            } );
            table.on( 'order.dt search.dt', function () {
                table.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                    cell.innerHTML = i+1;
                    table.cell(cell).invalidate('dom');
                } );
            } ).draw();
            $(".dt-buttons").prepend("<span>Save as ");
            $(".dt-buttons").append("</span><span class='pull-right'>"+$("#dataTableId_length").html()+"</span>");
            $(".dt-buttons").find('select').first().attr("name", "dataTableId_length_new")
            $(".dt-buttons").find('select').first().change(function() {
                $('[name="dataTableId_length"]').val($(this).val());
                $('[name="dataTableId_length"]').change();
            });
            $('[name="dataTableId_length_new"]').val($('[name="dataTableId_length"]').val());
            $("#dataTableId_length").addClass("hidden");
            $(".buttons-pdf").html('<i class="fa fa-file-pdf-o">&nbsp;&nbsp;PDF</i>')
            $(".buttons-excel").html('<i class="fa fa-file-excel-o">&nbsp;&nbsp;Excel</i>')
            $(".buttons-csv").html('<i class="fa fa-file-text-o">&nbsp;&nbsp;CSV</i>')
            });
        </script>
        <style>
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
        <form target="_blank" id="dqRptGenFormId" method="post" action="generateDqRptDqRptGen">
            <s:hidden name="model.ID" value="%{model.ID}"/>
            <s:hidden name="model.tmpl_name" value="%{model.tmpl_name}"/>
            <div class="card">
                <div class="card-header bg-light">
                    <h5><s:property value="%{model.tmpl_name}"/> <small class="fw-normal text-600"><s:text name="actionType.generator"/></small></h5>
                </div>
                <div class="card-body">
                    <table id="dataTableId_" class="table table-sds table-sm fs--1 table-striped table-hover" width="100%">
                        <thead>
                            <tr>
                            <th class="align-middle white-space-nowrap">No.</th>
                        <s:iterator value="searchFieldList" status="colStatus" var="col">
                            <th class="align-middle white-space-nowrap"><s:property value="%{#col.fld_lbl}"/>&nbsp;</th>
                        </s:iterator>
                            </tr>
                        </thead>
                        <tbody>
                    <s:iterator value="resultList" status="resultStatus" var="r">
                        <tr>
                            <td class="align-middle white-space-nowrap"><s:property value="#resultStatus.count"/></td>
                            <s:iterator value="displayFieldList" var="key">
                                <td class="align-middle white-space-nowrap">
                                    <s:if test="#r.get(#key.fld_col) instanceof java.sql.Timestamp">
                                        <s:text name="date_default_datetime"><s:param value="%{#r.get(#key.fld_col)}" /></s:text>
                                    </s:if><s:elseif test="#r.get(#key.fld_col) instanceof java.util.Date">
                                        <s:text name="date_default_date"><s:param value="%{#r.get(#key.fld_col)}" /></s:text>
                                    </s:elseif><s:else>
                                        <s:property value="%{#r.get(#key.fld_col)}"/>
                                    </s:else>
                                </td>    
                            </s:iterator>
                        </tr>
                    </s:iterator>
                        </tbody>
                        <tfoot>
                    <s:iterator value="criteriaList" status="criteriaStatus" var="c">
                        <tr align="left">
                            <th class="align-middle white-space-nowrap"></th>
                            <s:iterator value="displayFieldList" var="key">
                                <th class="align-middle white-space-nowrap"><s:property value="%{#c.get(#key.fld_col)}"/>
                                </th>    
                            </s:iterator>
                        </tr>
                    </s:iterator>
                        </tfoot>
                    </table>
<%--                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label"></label>
                        <div class="col-md-5">
                            <button type="submit" class="btn btn-primary" name="action:saveCriteriaDqRptGen" id="actionName" onclick="">
                                <i class="fa fa-gears"></i><s:text name="DqRptGen.saveCriteria"/>
                            </button>
                        </div>
                    </div>--%>
                </div>
            </div>
        </form>
    </body>
</html>
