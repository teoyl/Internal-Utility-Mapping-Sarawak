<%-- 
    Document   : new_application_verify
    Created on : Mar 20, 2024, 4:55:14 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>
    
<link href="styles/app_styles.css" rel="stylesheet" />

<div class="container-fluid">
    <jsp:include page="internal_swiper.jsp"></jsp:include>

    <jsp:include page="application_header.jsp"></jsp:include>
    
    <div class="row">
        <div class="accordion accordion-flush" id="accordionFlushApplication">
            <jsp:include page="submission_checklist.jsp"></jsp:include>

            <s:if test='model.wf_status > "108" && !model.applicationModel.internal_case.equals("Y")'>
                <jsp:include page="payment_list.jsp"></jsp:include>
            </s:if>
        </div>
    </div>
</div>
