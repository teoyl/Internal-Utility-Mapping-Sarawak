<%-- 
    Document   : jobNotification
    Created on : Aug 22, 2024, 11:30:13 AM
    Author     : Arine
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>
<div class="row">
    <div class="col" >
        <div class="card">
            <div class="card-header border-bottom bg-light">
                <i class="far fa-envelope me-2"></i><label class="form-label fw-bold text-dark mb-0">System Notifications</label>
            </div>
            <div class="card-body scrollbar max-h-500">
                <table id="tableNotifList" class="table table-sm table-dashboard no-wrap mb-4 fs--1 w-100">
                    <thead class="table-header text-align-center font-sans-serif">
                        <tr>
                            <th class="w-10" scope="col">No.</th>
                            <th class="w-5" scope="col"></th>
                            <th class="w-70" scope="col">Notification Message</th>
                            <th class="w-20" scope="col">Notification Date</th>
                        </tr>
                    </thead>
                    <tbody class="table-list-content font-sans-serif">
                         <s:if test="%{notifList.size() > 0}">
                            <s:iterator value="notifList" var="notice" status="noticeStatus">
                                 <tr>
                                    <td class="notif-no"><p class="my-2">${noticeStatus.index+1}</p></td>
                                    <td class="not-status"><p id="read-${noticeStatus.index+1}" class="my-2"></p></td>
                                    <td class="notif_subject">
                                        <p class="my-2 not-title text-truncate msg_subject cursor-pointer" data-id="${noticeStatus.index+1}" data-bs-toggle="modal" data-bs-target="#staticBackdrop_${noticeStatus.index+1}">${notice.message_subject}</p>
                                        <p class="my-2 not-from">From: <span class="not-sender">
                                                <s:if test="#notice.messageSenderModel != null">
                                                    ${notice.messageSenderModel.us_user_name}
                                                </s:if>
                                                <s:else>
                                                    ${notice.message_sender}
                                                </s:else>
                                            </span></p>
                                        <button class="btn btn-clear not-title" type="button" data-bs-toggle="modal" data-bs-target="#staticBackdrop_${noticeStatus.index+1}">View Message</button>
                                        <div class="modal fade" id="staticBackdrop_${noticeStatus.index+1}" data-bs-keyboard="false" data-bs-backdrop="static" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                                            <div class="modal-dialog modal-lg mt-6" role="document">
                                                <div class="modal-content border-0">
                                                    <div class="position-absolute top-0 end-0 mt-3 me-3 z-1">
                                                        <button class="btn-close btn btn-sm btn-circle d-flex flex-center transition-base" data-bs-dismiss="modal" aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body p-0">
                                                        <div class="bg-light rounded-top-3 py-3 ps-4 pe-6">
                                                            <h5 class="mb-1" id="staticBackdropLabel">${notice.message_subject}</h5>
                                                            <div class="p-4">
                                                                <div class="row">
                                                                    <div class="col-lg-12 mb-4">
                                                                        <s:if test="#notice.fileIdList.length > 0">
                                                                            <s:iterator value="#notice.fileIdList" var="fileId" status="fileIdStatus">
                                                                                <p class="mb-1"><a href="<s:text name="domain.internal" />viewTempFileAttachment?uploadID=<s:property value="%{#fileId}" />" target="_blank">Download Attachment <s:property value="#fileIdStatus.index + 1" /></a></p>
                                                                            </s:iterator>
                                                                        </s:if>
                                                                    </div>
                                                                    <div class="col-lg-12 mb-4">${notice.message_content}</div>
                                                                    <div class="modal-footer">
                                                                        <button class="btn btn-secondary" type="button" data-bs-dismiss="modal">Close</button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                    <td class="notif-date">
                                        <p class="my-2">${notice.message_status_date_str}</p>
                                    </td>
                                </tr>
                            </s:iterator>
                        </s:if>
                        <s:else>
                        </s:else>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>