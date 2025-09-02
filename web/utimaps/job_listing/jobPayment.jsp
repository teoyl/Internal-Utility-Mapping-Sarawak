<%-- 
    Document   : jobPayment
    Created on : Aug 9, 2024, 10:50:38 AM
    Author     : Arine
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>
<div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingPayment">
        <button class="accordion-button collapsed fw-bold text-uppercase" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapsePayment" aria-expanded="false" aria-controls="flush-collapsePayment">
            <i class="far fa-file-alt me-2 fs-2"></i>Payment
        </button>
    </h2>
    <div id="flush-collapsePayment" class="accordion-collapse collapse" aria-labelledby="flush-headingPayment" data-bs-parent="#accordionFlushPayment">
        <div class="accordion-body">
            <div class="card mb-3">
                <div class="card-header p-3 border-bottom bg-light">
                    <h6 class="mb-0"><label class="form-label">Application Fee</label></h6>
                    <s:if test="model.paymentModel != null">
                        <s:if test="model.paymentModel.payment_status = 'PC'">
                            <p class="mb-0 fw-bolder">Payment 1: PAID on <s:property value = "model.paymentModel.payment_date_str" /> via Receipt No. <s:property value = "model.paymentModel.pay_ref_no" />(RM<s:property value = "model.paymentModel.paymentAmount" />)</p>
                        </s:if>
                        <s:else>
                            <p class="mb-0 fw-bolder">Payment Pending</p>
                        </s:else>
                    </s:if>
                    <s:elseif test="model.applicationModel.paymentModel != null">
                        <s:if test="model.applicationModel.paymentModel.payment_status = 'PC'">
                            <p class="mb-0 fw-bolder">Payment 1: PAID on <s:property value = "model.paymentModel.payment_date_str" /> via Receipt No. <s:property value = "model.paymentModel.pay_ref_no" />(RM<s:property value = "model.paymentModel.paymentAmount" />)</p>
                        </s:if>
                        <s:else>
                            <p class="mb-0 fw-bolder">Payment Pending</p>
                        </s:else>
                    </s:elseif>
                </div>
                <div class="card-body p-1 ">
                    <div class="table-responsive scrollbar">
                        <table class="table table-hover table-striped overflow-hidden ">
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
                                                <s:if test="#paymentItem.rvr_code_str =''">
                                                    <p><s:property value="%{#paymentItem.rvr_code_str}" /></p>
                                                </s:if>
                                                <s:else>
                                                    <p>Fees for Application for Utility Survey Job </p>
                                                </s:else>
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
                                                <s:if test="#paymentItem.rvr_code_str =''">
                                                    <p><s:property value="%{#paymentItem.rvr_code_str}" /></p>
                                                </s:if>
                                                <s:else>
                                                    <p>NOMINAL FEE FOR ISSUANCE OF SURVEY JOB </p>
                                                </s:else>
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
                                    <td colspan="3" class="text-end"></td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>