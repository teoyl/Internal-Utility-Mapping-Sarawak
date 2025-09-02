<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<script>
        $(document).ready(function () {
              $('#historicalTable').DataTable({
                  "searching": true,
              });

         });
</script>
<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <div class="title"><s:property value="%{moreInfoTitle}"/></div>
            <button type="button" class="close btnCloseAlert" onclick="closeMoreInfo()">&times;</button>
        </div>
        <div class="modal-body">
            <jsp:include page="${moreInfoJsp}"/>
        </div>
    </div>
</div>