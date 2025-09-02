<%@taglib uri="/struts-tags" prefix="s"%>
<script nonce="r4DjhKbfO5ry">
    $(document).ready(function () {
        $('#historicalTable').DataTable();
    });
</script>
<div class="table-responsive">
    <table class="table table-sds table-condensed table-striped table-hover" width="100%" id="historicalTable">
        <thead>
            <tr>
                <th width="2%">#</th>
                <th class="">Assigned Date</th>
                <th class="">Job Description</th>
                <th class="">Assigned From</th>
                <th class="">Assigned To</th>
            </tr>
        </thead>
        <tbody>
            <s:if test="model.jobHistoryList.size > 0">
                <s:iterator value="model.jobHistoryList" status="hStatus" var="hResult">
                    <tr>
                        <td>${hStatus.index + 1}</td>
                        <td> ${hResult.status_date_str}  </td>
                        <td> ${hResult.status_code_str}  </td>
                        <td> ${hResult.created_by_str}  </td>
                        <td> ${hResult.status_by_str} </td>
                    </tr>
                </s:iterator>
            </s:if><s:else>
                <tr class="errortxt"><td colspan="6" class="text-center">No records</td></tr>
                <tr><td colspan="6">&nbsp;</td></tr>
            </s:else>    
        </tbody>
    </table>
</div>