<%-- 
    Document   : modalPastChecklist
    Created on : Sep 6, 2024, 9:30:36 AM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<style nonce = "EuTVqS192VKl">
    #pastChecklistModal {
        height: inherit;
    }
</style>

<div id="pastChecklistModal" class="modal fade" tabindex="-1" data-width="" data-height="" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <div class="title">Checklist Version</div>
                <button type="button" class="close btnCloseAlert" onclick="closeChecklistModal()">&times;</button>
            </div>
            <div class="modal-body p-0">
                <form id="pastChecklistForm" action="" name="pastChecklistForm" method="POST">
                    <ul class="list-group">
                        <s:if test="fileList.isEmpty()">
                            <li class="list-group-item">No record found</li>
                        </s:if>
                        <s:else>
                            <s:iterator value="fileList" status="fStatus" var="fResult">
                                <li class="list-group-item"><a href="viewTempFileSubmission?fileID=${fResult.file_id}" target="_blank">${fResult.created_date_str}</a></li>
                            </s:iterator>
                        </s:else>
                    </ul>
                </form>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!--end responsive--> 