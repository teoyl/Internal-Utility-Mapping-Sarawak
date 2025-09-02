<%-- 
    Document   : payment_list
    Created on : May 21, 2024, 4:28:58 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingTwo">
        <button class="accordion-button <s:if test='!wf_step.equals("3")'>collapsed</s:if> fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseTwo" aria-expanded="false" aria-controls="flush-collapseTwo">
            <i class="fas fa-tasks me-2"></i><s:text name="utimaps.payment.label.payment" />

            <s:if test="model.wf_status > '109'">
                <span class="position-absolute end-50px text-success">[<s:text name="utimaps.form.label.paymentPaid" />]</span>
            </s:if>
            <s:else>
                <span class="position-absolute end-50px text-primary">[<s:text name="utimaps.form.label.paymentPending" />]</span>
            </s:else>
        </button>
    </h2>
    <div id="flush-collapseTwo" class="accordion-collapse collapse <s:if test='wf_step.equals("3")'>show</s:if>" aria-labelledby="flush-headingTwo" data-bs-parent="#accordionFlushApplication">
        <div class="accordion-body">
            <s:if test="model.wf_status < '111'">
                <div class="col-md-12 text-end mb-3">
                    <a href="loadEditPageRouteJobMain"><button class="btn btn-primary" id="backJobListing" type="button"><s:text name = "utimaps.form.button.backListing" /></button></a>
                </div>
            </s:if> 

            <div class="card mb-3">
                 <div class="card-header p-3 border-bottom bg-light">
                     <h6 class="mb-0"><label class="form-label"><s:text name="utimaps.payment.label.applicationFee" /></label></h6>
                    <s:if test="model.paymentModel != null">
                        <s:if test="model.paymentModel.payment_status = 'PC'">
                        <p class="mb-0 fw-bolder"><s:text name="utimaps.payment.label.payment" /> 1: <s:text name="utimaps.payment.label.paidOn" /> <s:property value = "model.paymentModel.payment_date_str" /> <s:text name="utimaps.payment.label.viaReceiptNo" /> <s:property value = "model.paymentModel.pay_ref_no" />(RM<s:property value = "model.paymentModel.paymentAmount" />)</p>
                        </s:if>
                        <s:else>
                            <p class="mb-0 fw-bolder"><s:text name="utimaps.payment.label.paymentPending" /></p>
                        </s:else>
                    </s:if>
                    <s:elseif test="model.applicationModel.paymentModel != null">
                        <s:if test="model.applicationModel.paymentModel.payment_status = 'PC'">
                           <p class="mb-0 fw-bolder"><s:text name="utimaps.payment.label.payment" /> 1: <s:text name="utimaps.payment.label.paidOn" /> <s:property value = "model.paymentModel.payment_date_str" /> <s:text name="utimaps.payment.label.viaReceiptNo" /> <s:property value = "model.paymentModel.pay_ref_no" />(RM<s:property value = "model.paymentModel.paymentAmount" />)</p>
                        </s:if>
                        <s:else>
                           <p class="mb-0 fw-bolder"><s:text name="utimaps.payment.label.paymentPending" /></p>
                        </s:else>
                    </s:elseif>
                  </div>
                <div class="card-body p-1 ">
                    <div class="table-responsive scrollbar">
                        <table class="table table-bordered table-hover table-striped overflow-hidden ">
                            <thead>
                                <tr>
                                    <th scope="col" class="col-md-2 fw-bold"><s:text name="utimaps.payment.label.code" /></th>
                                    <th scope="col" class="col-md-7 fw-bold"><s:text name="utimaps.payment.label.item" /></th>
                                    <th scope="col" class="col-md-3 fw-bold"><s:text name="utimaps.payment.label.amount" /></th>
                                </tr>
                            </thead>
                            <tbody>
                                <s:if test="model.paymentModel != null">
                                    <s:iterator value="model.paymentModel.paymentItemList" status="paylistStatus" var="paymentItem">
                                        <tr class="<s:if test="#paylistStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                           <td class="" class="col-md-2">
                                               <p><s:property value="%{#paymentItem.rvr_code}" /></p>
                                           </td>
                                           <td class="" class="col-md-3">
                                                <p><s:property value="%{#paymentItem.rvr_code_str}" /></p>
                                           </td>

                                           <td class="" class="col-md-3">
                                               <p><s:property value="%{#paymentItem.itemamount}" /></p>
                                           </td>
                                       </tr>
                                    </s:iterator>
                                </s:if>
                                <s:elseif test="model.applicationModel.paymentModel != null">
                                    <s:iterator value="model.applicationModel.paymentModel.paymentItemList" status="paylistStatus" var="paymentItem">
                                        <tr class="<s:if test="#paylistStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                           <td class="" class="col-md-2">
                                               <p><s:property value="%{#paymentItem.rvr_code}" /></p>
                                           </td>
                                           <td class="" class="col-md-3">
                                               <p><s:property value="%{#paymentItem.rvr_code_str}" /></p>
                                           </td>

                                           <td class="" class="col-md-3">
                                               <p><s:property value="%{#paymentItem.itemamount}" /></p>
                                           </td>
                                       </tr>
                                    </s:iterator>
                                </s:elseif>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="2" class="text-end"><b><s:text name="utimaps.payment.label.total" /></b></td>
                                    <td>
                                        <s:if test="model.paymentModel != null">
                                            <s:property value ="model.paymentModel.paymentAmount" />
                                        </s:if>
                                        <s:elseif test="model.applicationModel.paymentModel != null">
                                            <s:property value ="model.applicationModel.paymentModel.paymentAmount" />
                                        </s:elseif>
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>