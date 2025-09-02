<%-- 
    Document   : verify_hardcopy_submission
    Created on : May 18, 2024, 5:00:01 PM
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
            <%--
            <s:if test='model.control_sv_flag.equals("Y")'>
                <jsp:include page="pages/sub_utility_survey_cs.jsp"></jsp:include>
            </s:if>
            <s:else>
                <jsp:include page="pages/sub_utility_survey.jsp"></jsp:include>
            </s:else>
            --%>
                            
            <jsp:include page="pages/hardcopy_assess.jsp"></jsp:include>
             
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

