<%-- 
    Document   : internal_swiper
    Created on : Apr 8, 2024, 11:03:24 AM
    Author     : yonglai
--%>

<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<link href="include/public_swiper.css" rel="stylesheet"/>

<div class="card mb-3 border-secondary z-n1">
    <div class="card-body p-0">
        <!-- Part 1 Swiper -->
        <div class="my-lg-3 m-md-3">
            <div class="d-flex justify-content-center">
                <s:iterator value="swiperList" var="swiper" status="swiperStatus">
                    <s:set var="swiperStep" value="%{swiperStep}" />
                    <div class="bullets flex-column align-items-center text-center p-lg-4 p-md-3">
                        <s:if test="%{#swiperStatus.index+1 < #swiperStep}">
                            <s:if test="%{#swiperStatus.index + 1 != swiperList.size()}">
                                <button class="w-3rem progressBullet linedBullet m-auto h-3rem border border-3 rounded-circle completed-bullet"><s:property value="%{#swiperStatus.index+1}"/></button>
                            </s:if>
                            <s:else>
                                <button class="w-3rem progressBullet m-auto h-3rem border border-3 rounded-circle inactive-bullet"><s:property value="%{#swiperStatus.index+1}"/></button>
                            </s:else>
                        </s:if>
                        <s:elseif test="%{#swiperStatus.index+1 == #swiperStep}">
                            <s:if test="%{#swiperStatus.index + 1 != swiperList.size()}">
                                <button class="w-3rem progressBullet darklinedBullet m-auto h-3rem border border-3 rounded-circle bg-white fw-bold dark__text-white"><s:property value="%{#swiperStatus.index+1}"/></button>
                            </s:if>
                            <s:else>
                                <button class="w-3rem progressBullet m-auto h-3rem border border-3 rounded-circle bg-white fw-bold dark__text-white"><s:property value="%{#swiperStatus.index+1}"/></button>
                            </s:else>
                        </s:elseif>
                        <s:else>
                            <s:if test="%{#swiperStatus.index + 1 != swiperList.size()}">
                                <button class="w-3rem progressBullet darklinedBullet m-auto h-3rem border border-3 rounded-circle inactive-bullet"><s:property value="%{#swiperStatus.index+1}"/></button>
                            </s:if>
                            <s:else>
                                <button class="w-3rem progressBullet m-auto h-3rem border border-3 rounded-circle inactive-bullet"><s:property value="%{#swiperStatus.index+1}"/></button>
                            </s:else>
                        </s:else>
                        <div class="mt-3 font-sans-serif text-wrap"><strong>${swiper[0]}</strong></div>
                    </div>
                </s:iterator>

            </div>
            <div class="swiper-button-prev"></div>
            <div class="swiper-button-next"></div>
        </div>

    </div>
</div>

