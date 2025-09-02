<%-- 
    Document   : jobComment
    Created on : Aug 22, 2024, 11:26:22 AM
    Author     : Arine
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>
<div class="card mb-3">
    <div class="card-header border-bottom bg-light">
        <i class="far fa-comment-alt me-2"></i><label class="form-label fw-bold text-dark mb-0">Comments</label>
    </div>
    <div class="card-body scrollbar max-h-500">
        <s:if test='model.u50ChecklistModel.check_comment_oic != null'>
        <div class="row ">
            <div class="col-md-auto text-end U50" > <i class="fas fa-users"></i></div>
            <div class="col p-0 me-2">
                <s:if test='model.u50ChecklistModel.comment_ss != null'>
                    <div class="clearfix">
                        <span class="word-green"><s:property value="model.u50ChecklistModel.checkSSUser.us_user_name" /></span>
                        <span class="float-right fs-smaller"><s:property value="model.u50ChecklistModel.check_date_ss_str" /></span>
                        <div class="clearfix"></div>
                        <p class="text-dark"><s:property value="model.u50ChecklistModel.comment_ss" escapeHtml="false"/></p>
                    </div>
                </s:if>
                <div class="clearfix">
                    <span class="word-green"><s:property value="model.u50ChecklistModel.checkUser.us_user_name" /></span>
                    <span class="float-right fs-smaller"><s:property value="model.u50ChecklistModel.check_date_oic_str" /></span>
                    <div class="clearfix"></div>
                    <p class="text-dark"><s:property value="model.u50ChecklistModel.check_comment_oic" escapeHtml="false"/></p>
                </div>
            </div>
        </div>
    </s:if>
    <s:if test='model.u40ChecklistModel.check_comment_oic != null'>
        <div class="row ">
            <div class="col-md-auto text-end U40" > <i class="fas fa-users"></i></div>
            <div class="col p-0 me-2">
                <s:if test='model.u40ChecklistModel.comment_ss != null'>
                    <div class="clearfix">
                        <span class="word-green"><s:property value="model.u40ChecklistModel.checkSSUser.us_user_name" /></span>
                        <span class="float-right fs-smaller"><s:property value="model.u40ChecklistModel.check_date_ss_str" /></span>
                        <div class="clearfix"></div>
                        <p class="text-dark"><s:property value="model.u40ChecklistModel.comment_ss" escapeHtml="false"/></p>
                    </div>
                </s:if>
                <div class="clearfix">
                    <span class="word-green"><s:property value="model.u40ChecklistModel.checkUser.us_user_name" /></span>
                    <span class="float-right fs-smaller"><s:property value="model.u40ChecklistModel.check_date_oic_str" /></span>
                    <div class="clearfix"></div>
                    <p class="text-dark"><s:property value="model.u40ChecklistModel.check_comment_oic" escapeHtml="false"/></p>
                </div>
            </div>
        </div>
    </s:if>
    <s:if test='model.u30ChecklistModel.check_comment_oic != null'>
        <div class="row ">
            <div class="col-md-auto text-end U30" > <i class="fas fa-users"></i></div>
            <div class="col p-0 me-2">
                <s:if test='model.u30ChecklistModel.comment_ss != null'>
                    <div class="clearfix">
                        <span class="word-green"><s:property value="model.u30ChecklistModel.checkSSUser.us_user_name" /></span>
                        <span class="float-right fs-smaller"><s:property value="model.u30ChecklistModel.check_date_ss_str" /></span>
                        <div class="clearfix"></div>
                        <p class="text-dark"><s:property value="model.u30ChecklistModel.comment_ss" escapeHtml="false"/></p>
                    </div>
                </s:if>
                <div class="clearfix">
                    <span class="word-green"><s:property value="model.u30ChecklistModel.checkUser.us_user_name" /></span>
                    <span class="float-right fs-smaller"><s:property value="model.u30ChecklistModel.check_date_oic_str" /></span>
                    <div class="clearfix"></div>
                    <p class="text-dark"><s:property value="model.u30ChecklistModel.check_comment_oic" escapeHtml="false"/></p>
                </div>
            </div>
        </div>
    </s:if>
    <s:if test='model.u20ChecklistModel.check_comment_oic != null'>
        <div class="row ">
            <div class="col-md-auto text-end U20" > <i class="fas fa-users"></i></div>
            <div class="col p-0 me-2">
                <s:if test='model.u20ChecklistModel.comment_ss != null'>
                    <div class="clearfix">
                        <span class="word-green"><s:property value="model.u20ChecklistModel.checkSSUser.us_user_name" /></span>
                        <span class="float-right fs-smaller"><s:property value="model.u20ChecklistModel.check_date_ss_str" /></span>
                        <div class="clearfix"></div>
                        <p class="text-dark"><s:property value="model.u20ChecklistModel.comment_ss" escapeHtml="false"/></p>
                    </div>
                </s:if>
                <div class="clearfix">
                    <span class="word-green"><s:property value="model.u20ChecklistModel.checkUser.us_user_name" /></span>
                    <span class="float-right fs-smaller"><s:property value="model.u20ChecklistModel.check_date_oic_str" /></span>
                    <div class="clearfix"></div>
                    <p class="text-dark"><s:property value="model.u20ChecklistModel.check_comment_oic" escapeHtml="false"/></p>
                </div>
            </div>
        </div>
    </s:if>
    <s:if test='model.u10ChecklistModel.check_comment_oic != null'>
        <div class="row ">
            <div class="col-md-auto text-end U10" > <i class="fas fa-users"></i></div>
            <div class="col p-0 me-2">
                <s:if test='model.u10ChecklistModel.comment_ss != null'>
                    <div class="clearfix">
                        <span class="word-green"><s:property value="model.u10ChecklistModel.checkSSUser.us_user_name" /></span>
                        <span class="float-right fs-smaller"><s:property value="model.u10ChecklistModel.check_date_ss_str" /></span>
                        <div class="clearfix"></div>
                        <p class="text-dark"><s:property value="model.u10ChecklistModel.comment_ss" escapeHtml="false"/></p>
                    </div>
                </s:if>
                <div class="clearfix">
                    <span class="word-green"><s:property value="model.u10ChecklistModel.checkUser.us_user_name" /></span>
                    <span class="float-right fs-smaller"><s:property value="model.u10ChecklistModel.check_date_oic_str" /></span>
                    <div class="clearfix"></div>
                    <p class="text-dark"><s:property value="model.u10ChecklistModel.check_comment_oic" escapeHtml="false"/></p>
                </div>
            </div>
        </div>
    </s:if>
    <s:if test='model.u21ChecklistModel.check_comment_oic != null'>
        <div class="row ">
            <div class="col-md-auto text-end U21" > <i class="fas fa-users"></i></div>
            <div class="col p-0 me-2">
                <s:if test='model.hardChecklistModel.comment_ss != null'>
                    <div class="clearfix">
                        <span class="word-green"><s:property value="model.u21ChecklistModel.checkSSUser.us_user_name" /></span>
                        <span class="float-right fs-smaller"><s:property value="model.u21ChecklistModel.check_date_ss_str" /></span>
                        <div class="clearfix"></div>
                        <p class="text-dark"><s:property value="model.u21ChecklistModel.comment_ss" escapeHtml="false"/></p>
                    </div>
                </s:if>
                <div class="clearfix">
                    <span class="word-green"><s:property value="model.hardChecklistModel.checkUser.us_user_name" /></span>
                    <span class="float-right fs-smaller"><s:property value="model.hardChecklistModel.check_date_oic_str" /></span>
                    <div class="clearfix"></div>
                    <p class="text-dark"><s:property value="model.hardChecklistModel.check_comment_oic" escapeHtml="false"/></p>
                </div>
            </div>
        </div>
    </s:if>
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