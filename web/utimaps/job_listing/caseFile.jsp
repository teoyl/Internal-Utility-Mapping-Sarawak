<%-- 
    Document   : caseFile
    Created on : Apr 3, 2025, 3:56:56 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>
<div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingFiles">
        <button class="accordion-button collapsed fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseFiles" aria-expanded="false" aria-controls="flush-collapseFiles">
            <i class="far fa-file-alt me-2 fs-2"></i>FILES 
        </button>
    </h2>
    <div id="flush-collapseFiles" class="accordion-collapse collapse" aria-labelledby="flush-headingFiles" data-bs-parent="#accordionFlushFiles">
        <div class="accordion-body">
            <div class="row g-0">
                <div class="col-md-3 file-menu">
                    <div class="navbar-collapse" id="navbarVerticalCollapse">
                        <div class="navbar-vertical-content scrollbar">
                            <ul class="navbar-nav flex-column mb-3" id="navbarVerticalNav">
                                <li class='nav-item'>
                                    <a class='nav-link' onclick="viewFile(''); return false;" role='button'>
                                        <div class='d-flex align-items-center'>
                                            <span class='nav-link-icon'><i class="fas fa-folder fs-2"></i></span>
                                            <span class='nav-link-text ps-1'>All</span>
                                        </div>
                                    </a>
                                </li>
                                <li class='nav-item '>   
                                    <a class='nav-link dropdown-indicator' href='#APP01' role='button' data-bs-toggle='collapse' aria-expanded='false' aria-controls='APP01'>      
                                        <div class="d-flex align-items-center">
                                            <span class='nav-link-icon'><i class="fas fa-folder fs-2"></i></span>
                                            <span class="nav-link-text ps-1">Application for Issuance of USJ</span>
                                        </div>   
                                    </a>      
                                    <ul class='nav collapse' id='APP01'>    
                                        <li class='nav-item'>
                                            <a class='file-submenu' onclick="viewFile('APP01_SF'); return false;">
                                                <div class="d-flex align-items-center" >
                                                    <span class='nav-link-icon' id="APP01_SF"><i class="fas fa-folder fs-2"></i></span>
                                                    <span class="nav-link-text ps-1">Application</span></div>
                                            </a>
                                        </li>    
                                        <li class='nav-item'>
                                            <a class='file-submenu' onclick="viewFile('APP01_LS'); return false;">
                                                <div class="d-flex align-items-center" >
                                                    <span class='nav-link-icon' id="APP01_LS"><i class="fas fa-folder fs-2"></i></span>
                                                    <span class="nav-link-text ps-1">L&AMP;S</span>
                                                </div>
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                                
                                <li class='nav-item'>
                                    <a class='nav-link' onclick="viewFile('Approval_LS'); return false;" role='button'>
                                        <div class='d-flex align-items-center' >
                                            <span class='nav-link-icon' id="Approval_LS"><i class="fas fa-folder fs-2"></i></span>
                                            <span class='nav-link-text ps-1'>L&AMP;S Processing &AMP; Approval</span>
                                        </div>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-9">
                    <div class="table-responsive scrollbar max-h-500">
                        <table class="table table-sds table-sm fs--1 table-hover overflow-hidden" width="100%" id="fileTable">
                            <thead class="text-900">
                                <tr class="bottom-green-2">
                                    <th width="1%" class="align-middle">
                                        No.
                                    </th>
                                    <th class="align-middle white-space-nowrap" >
                                        File Group
                                    </th>
                                    <th class="align-middle white-space-nowrap" >
                                    </th>
                                    <th class="align-middle white-space-nowrap">
                                        File Description
                                    </th>
                                    <th class="align-middle white-space-nowrap">
                                        Status
                                    </th>
                                    <th class="align-middle white-space-nowrap">
                                        Date Uploaded
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <s:iterator value="fileList" status="fStatus" var="fResult">
                                    <tr class="bottom-green-1">
                                        <td class="text-middle align-middle" width="1%"></td>
                                        <td class="align-middle" >
                                            ${fResult.FILE_GROUP}
                                        </td>
                                        <td class="align-middle p-0 text-right" >
                                            <i class="far fa-file-alt"></i>
                                        </td>
                                        <td class="align-middle">
                                            ${fResult.FILE_DESC}<br>
                                            <a href="viewTempFileSubmission?fileID=${fResult.FILE_ID}" target="_blank">${fResult.ORIGINAL_FILE_NAME}</a>
                                        </td>
                                        <td class="align-middle fs-2 word-green">
                                            <s:if test='#fResult.STATUS.equals("C")'><i class="far fa-check-square"></i></s:if>
                                            <s:elseif test='#fResult.STATUS.equals("E")'><i class="far fa-check-times"></i></s:elseif>
                                            <s:elseif test='#fResult.STATUS.equals("N")'>N/A</s:elseif>

                                            </td>
                                            <td class="align-middle">
                                            ${fResult.CREATED_DATE}
                                        </td>
                                    </tr>
                                </s:iterator>
                            </tbody>
                        </table>
                    </div>   
                </div>
            </div>
            <!--</div>-->
        </div>
    </div>
</div>
<script nonce="r4DjhKbfO5ry">
    var table;
    $(document).ready(function () {
        table = $('#fileTable').DataTable({
            columnDefs: [{
                    targets: [1],
                    visible: false,
                    searchable: true
                }], ordering: false,
            paging: false,
            info: false, dom: 'lrt'
        });

        table.on('order.dt search.dt', function () {
            let i = 1;
            table.cells(null, 0, {search: 'applied', order: 'applied'})
                .every(function (cell) {
                    this.data(i++);
                });
        }).draw();
    });
    
    function viewFile(fileId) {
        table.column(1).search(fileId).draw();
        $(".open-folder").find('i').remove();
        $(".open-folder").html($('<i/>', {class: 'fas fa-folder fs-2'}));
        $(".open-folder").removeClass("open-folder");
        var x = $('#' + fileId);
        x.addClass("open-folder");
        x.find('i').remove();
        x.html($('<i/>', {class: 'fas fa-folder-open fs-2'}));
    }
</script>