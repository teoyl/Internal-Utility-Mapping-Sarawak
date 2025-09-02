<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title"><s:text name="DqTemplate.appName"/> <small class="fw-normal text-600"><s:text name="DqTemplate.columns"/></small></h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
            <div class="row">
        <s:iterator value="columnList" var="theColumn" status="colStatus">
                <div class="col-lg-4">${colStatus.index+1}.&nbsp;<s:property value="%{#theColumn.keyData}"/></div>
        </s:iterator>
            </div>
        </div>
    </div>
</div>
