<%-- 
    Document   : jobActivity
    Created on : Aug 22, 2024, 11:28:45 AM
    Author     : Arine
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>
<div class="card mb-3">
    <div class="card-header border-bottom bg-light">
        <i class="far fa-list-alt me-2"></i><label class="form-label fw-bold text-dark mb-0">Latest Activity</label>
    </div>
    <div class="card-body scrollbar max-h-500">
        <div class="timeline-vertical history-timeline">
            <s:if test="model.jobHistoryList.size > 0">
                <s:iterator value="model.jobHistoryList" status="hStatus" var="hResult">
                    <div class="timeline-item timeline-item-end">
                        <div class="timeline-icon icon-item icon-item-lg text-primary"></div>
                        <div class="">
                            <div class="col-lg-12">
                                <div class="timeline-item-content">
                                    <!--<div class="timeline-item-card">-->
                                    <p class="mb-2 word-green">${hResult.status_code_str}</p>
                                    <p class="fs--1 mb-0">${hResult.status_date_str2}</p>
                                    <!--</div>-->
                                </div>
                            </div>
                        </div>
                    </div>
                </s:iterator>
            </s:if>
        </div>
    </div>
</div>