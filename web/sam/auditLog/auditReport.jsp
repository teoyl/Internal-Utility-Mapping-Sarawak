<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Audit Report</title>
        <script type="text/javascript">
            function modelSelected() {
                offLoading = false;
                if ($("#selectedClass").val() !== "") {
                    $("#modelChangeLoader").load("modelSelectedEntry?selectedClass="+$("#selectedClass").val(),
                    function (message) {
                        if (message === "Expired") {
                            <%-- it you are using itemChangeLoader, it won't come here, 
                                it only will come here it you are loading sub-item using your on Action (*access right control needed) --%>
                            document.location = "initLogin";
                        } else {
                            $("#generatedContent").html(message);
//                            alert(message);
                        }
                    });
                }
            }
            
            function auditReport() {
                offLoading = true;
                $('#auditForm').prop("target", "_blank");
                submitForm('auditForm', 'auditReportEntry');
                return false;
            }
        </script>
        <style>
            .td_matched {
                background-color: lightgreen;
            }
            .td_notMatched {
                background-color: lightgrey;
            }
            .td_current {
                background-color: #AFEABB;
            }
            table {
                display: block;
                overflow-x: auto;
                white-space: nowrap;
            }
            .UPDATED {
                color: blue;
            }
            .INSERT {
                color: #2BA42B;
            }
            .DELETED {
                color: RED;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form name="form" id="auditForm" class="" method="post">
            <div class="card">
                <div class="card-header bg-light">
                    <h5>Audit Log Report <small class="fw-normal text-600">v1.0</small></h5>
                </div>
                <div class="card-body">
                    <s:iterator value="auditTrailMap.keySet" var="auditTrailKey" status="auditMapStatus">
                        <div>
                            Record ID: <s:property value="auditTrailMap.get(#auditTrailKey).get(0).record_id"/>
                        </div>
                        <div class="table-responsive">
                        <table id="table_<s:property value="auditTrailMap.get(#auditTrailKey).get(0).record_id"/>" class="table table-sds table-sm fs--1 table-bordered" width="100%">
                            <thead class="bg-200 text-900">
                            <tr>
                                <th class="align-middle white-space-nowrap">
                                    Audit Date
                                </th>
                                <th class="align-middle white-space-nowrap">
                                    Status
                                </th>
                                <th class="align-middle white-space-nowrap">
                                    Action By
                                </th>
                                <s:iterator value="formattedSortedDisplayColList" var="displayCol">
                                <th class="align-middle white-space-nowrap">${displayCol}</th>
                                </s:iterator>
                            </tr>
                            </thead>
                            <tbody>
                            <s:iterator value="auditTrailMap.get(#auditTrailKey)" var="auditRecord" status="auditStatus">
                                <tr class="td_<s:property value="#auditRecord.matchClass"/>">
                                <td class="align-middle white-space-nowrap">
                                    <s:if test="#auditRecord.date_time == null">
                                        Current
                                    </s:if><s:else>
                                        ${auditRecord.date_time_str}
                                    </s:else>
                                </td>
                                <td class="align-middle white-space-nowrap <s:if test="#auditRecord.audit_action != null">${auditRecord.audit_action}</s:if>">
                                    <s:if test="#auditRecord.audit_action == null">
                                        -
                                    </s:if><s:else>
                                        ${auditRecord.audit_action}
                                    </s:else>
                                </td>
                                <td class="align-middle white-space-nowrap">
                                    ${auditRecord.user_id}
                                </td>
                                <s:iterator value="sortedDisplayColList" var="displayCol">
                                    <td class="align-middle white-space-nowrap">
                                        <s:set var="displayCol2" value="#displayCol"/>
                                        <s:if test='#displayCol.endsWith("_str")'><s:set var="displayCol2" value="#displayCol.substring(0, #displayCol.length() - 4)"></s:set></s:if>
                                    <s:if test='#auditRecord.audit_action != null && #auditRecord.audit_action.equals("DELETED")'>
                                        ${auditRecord.getOldData(displayCol2)}
                                    </s:if><s:else>
                                        <s:set var="isMatched"><s:property value="displayCol2"/>__matched</s:set>
                                        <font color="<s:if test='#auditRecord.getNewData(#isMatched)'>red</s:if>">
                                        ${auditRecord.getNewData(displayCol2)}
                                        </font>
                                    </s:else>
                                    
                                </td>
                                </s:iterator>
                            </tr>
                        </s:iterator>
                            </tbody>
                        </table>
                        </div>
                    </s:iterator>
                </div>
            </div>
        </form>
</body>
</html>
