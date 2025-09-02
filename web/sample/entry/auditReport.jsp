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
                background-color: #79C789;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form name="form" id="auditForm" class="" method="post">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Audit Log Report <small>v1.0</small></h4>
                </div>
                <div class="panel-body">
                    <s:iterator value="auditTrailMap.keySet" var="auditTrailKey" status="auditMapStatus">
                        <div>
                            Record ID: <s:property value="auditTrailMap.get(#auditTrailKey).get(0).record_id"/>
                        </div>
                        <table class="table table-sds table-condensed " width="100%">
                            <tr>
                                <th>
                                    Audit Date
                                </th>
                                <th>
                                    Status
                                </th>
                                <th>
                                    Action By
                                </th>
                                <s:iterator value="sortedDisplayColList" var="displayCol">
                                <th>${displayCol}</th>
                                </s:iterator>
                            </tr>
                            <s:iterator value="auditTrailMap.get(#auditTrailKey)" var="auditRecord" status="auditStatus">
                                <tr class="td_<s:property value="#auditRecord.matchClass"/>">
                                <td>
                                    <s:if test="#auditRecord.date_time == null">
                                        Current
                                    </s:if><s:else>
                                        ${auditRecord.date_time_str}
                                    </s:else>
                                </td>
                                <td>
                                    <s:if test="#auditRecord.audit_action == null">
                                        -
                                    </s:if><s:else>
                                        ${auditRecord.audit_action}
                                    </s:else>
                                </td>
                                <td>
                                    ${auditRecord.user_id}
                                </td>
                                <s:iterator value="sortedDisplayColList" var="displayCol">
                                <td>
                                    <s:set var="isMatched"><s:property value="displayCol"/>__matched</s:set>
                                    <font color="<s:if test='#auditRecord.getNewData(#isMatched)'>red</s:if>">
                                    ${auditRecord.getNewData(displayCol)}
                                    </font>
                                </td>
                                </s:iterator>
                            </tr>
                        </s:iterator>
                        </table>
                    </s:iterator>
                </div>
            </div>
        </form>
</body>
</html>
