<%@taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    $(document).ready(function () {
        sortField = '<s:property value="%{dt_order_by}"/>';
        sortFieldOrder = '<s:property value="%{dt_order}"/>';
        prepareDatatable("example");
        $("#example").find(".sds-dropdown").select2();
    });
</script>
<table id="example" class="display" style="width:100%">
    <thead>
        <tr>
            <th width="1%">No.</th>
            <th>First name</th>
            <th>Last name</th>
        </tr>
    </thead>
    <tbody>
<%--        <s:iterator value="recordList" var="rec" status="recStatus">
            <tr><td><s:property value="%{getDt_rowIdx(#recStatus.index+1)}"/></td>
                <td>First name <s:property value="%{#rec}"/></td><td>Last name <s:property value="%{#rec}"/></td></tr>
        </s:iterator> --%>
        <s:iterator value="recordList" var="rec" status="recStatus">
            <tr>
                <td><s:property value="%{getDt_rowIdx(#recStatus.index+1)}"/></td>
                <td><s:textfield name="col_1" cssClass="form-control" value='%{#rec.col_1}'/></td>
                <td><s:textfield name="col_2" cssClass="form-control" value="%{#rec.col_2}"/></td>
            </tr>
        </s:iterator>
    </tbody>
</table>
<div class="dataTables_wrapper">
    <jsp:include page="/pages/pagination/paging_dt.jsp"></jsp:include>
</div>

