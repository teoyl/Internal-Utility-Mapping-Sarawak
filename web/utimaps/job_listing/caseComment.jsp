<%-- 
    Document   : caseComment
    Created on : Apr 3, 2025, 4:31:42 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>
<div class="card mb-3">
    <div class="card-header border-bottom bg-light">
        <i class="far fa-comment-alt me-2"></i><label class="form-label fw-bold text-dark mb-0">Comments</label>
    </div>
    <div class="card-body scrollbar max-h-500">
        
    <s:if test='model.checklistModel.check_comment_oic != null'>
        <div class="row ">
            <div class="col-md-auto text-end" > <i class="fas fa-users"></i></div>
            <div class="col p-0 me-2">
                <span class="word-green"><s:property value="model.checklistModel.checkUser.us_user_name" /></span>
                <span class="float-right fs-smaller"><s:property value="model.checklistModel.check_date_oic_str" /></span>
                <div class="clearfix"></div>
                <p class="text-dark"><s:property value="model.checklistModel.check_comment_oic" escapeHtml="false"/></p>
            </div>
        </div>
    </s:if>
    </div>
</div>