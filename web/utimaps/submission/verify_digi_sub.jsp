<%-- 
    Document   : verify_digi_sub
    Created on : Mar 20, 2024, 4:55:14 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<link href="styles/sub_styles.css" rel="stylesheet" />

<div class="container-fluid">
    <jsp:include page="include/submission_swiper.jsp"></jsp:include>
    <jsp:include page="include/submission_header.jsp"></jsp:include>

        <div class="row">
            <div class="accordion accordion-flush" id="accordionFlushApplication">
            <s:if test='model.control_sv_flag.equals("Y")'>
                <s:if test='model.comp_completed.equals("Y")'>
                    <jsp:include page="pages/sub_utility_survey.jsp"></jsp:include>
                </s:if>
                <s:else>
                    <jsp:include page="pages/sub_utility_survey_cs.jsp"></jsp:include>
                </s:else>
            </s:if>
            <s:else>
                <jsp:include page="pages/sub_utility_survey.jsp"></jsp:include>
            </s:else>
        </div>
    </div>
</div>

<script nonce="r4DjhKbfO5ry">
    $('.viewChecklistBtn').on('click', function (e) {
        e.stopPropagation();
        e.stopImmediatePropagation();
        e.preventDefault();
        var contentType = $(this).data('content-type');
        divSubmitForm("viewPastChecklistSubmission?jobId=<s:property value="%{model.job_id}"/>&checklist=" + contentType, "pastChecklistForm", "pastChecklistDiv", "viewChecklist");
    });
    function closeChecklistModal() {
        $('#pastChecklistModal').modal('hide');
    }
    function viewChecklist() {
        $('#pastChecklistModal').modal('show');
    }
</script>
