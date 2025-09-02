<%-- 
    Document   : sub_control_survey
    Created on : May 22, 2024, 10:00:27 AM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingSvyComp">
        <button class="accordion-button <s:if test='!activeAccordion.equals("2")'>collapsed</s:if> fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseSvyComp" aria-expanded="false" aria-controls="flush-collapseSvyComp">
            <i class="fas fa-tasks me-2"></i>Checklist On Traverse
            
            <s:if test="model.wf_status_2 > '222'">
                <span class="position-absolute end-50px text-success">[<s:text name = "utimaps.form.label.completed" />]</span>
            </s:if>
            <s:elseif test="model.wf_status_2 > '202' && model.wf_status_2 < '220'">
                <span class="position-absolute end-50px text-success">[<s:text name = "utimaps.form.label.completed" />]</span>
            </s:elseif>
            <s:else>
                <span class="position-absolute end-50px text-primary">[<s:text name = "utimaps.form.label.inProgress" />]</span>
            </s:else>
        </button>
    </h2>
    <div id="flush-collapseSvyComp" class="accordion-collapse collapse <s:if test='activeAccordion.equals("2")'>show</s:if>" aria-labelledby="flush-headingSvyComp" data-bs-parent="#accordionFlushApplication">
        <div class="accordion-body">
            <ul class="nav nav-tabs" id="surveyTab" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link <s:if test='activeTab.equals("1")'>active</s:if>" id="computation-tab" data-bs-toggle="tab" data-bs-target="#computation" type="button" role="tab" aria-controls="computation" aria-selected="false">Computation Checking</button>
                </li>
                
                <s:if test='model.wf_status_2 >= "220"'>
                <li class="nav-item" role="presentation">
                    <button class="nav-link <s:if test='activeTab.equals("2")'>active</s:if>" id="plan-tab" data-bs-toggle="tab" data-bs-target="#plan" type="button" role="tab" aria-controls="plan" aria-selected="false">Utility Control Plan Checking</button>
                </li>
                </s:if>
            </ul>
            <div class="tab-content" id="surveyTabContent">
                <div class="tab-pane <s:if test='activeTab.equals("1")'>active</s:if><s:else>fade</s:else>" id="computation" role="tabpanel" aria-labelledby="computation-tab">
                    <jsp:include page="computation_checking.jsp"></jsp:include>
                </div>
                
                <s:if test='model.wf_status_2 >= "220"'>
                <div class="tab-pane <s:if test='activeTab.equals("2")'>active</s:if><s:else>fade</s:else>" id="plan" role="tabpanel" aria-labelledby="plan-tab">
                    <jsp:include page="control_plan_checking.jsp"></jsp:include>
                </div>
                </s:if>
                        
            </div>
        </div>
    </div>
</div>
                
<script nonce="r4DjhKbfO5ry">
    $(document).ready(function () {
        scrollToView("flush-collapseSvyComp");
        
        var wfCode = $("#wfActivityCode").val();
        var wfStatus2 = $("#model_wf_status_2").val();
        
        if(wfCode === "USJ003_04_02" && wfStatus2 !== "220") {
            alertUserOnExistingJob();
        }
    });
    
    function alertUserOnExistingJob() {
        var alertMsg = "<s:text name="utimaps.alert.2JobInProgress" />";
        alertBox(alertMsg);
        
        $(".alert-msg-div").html(alertMsg);
        $(".alert-msg-div").removeClass("d-none");
    }
</script>