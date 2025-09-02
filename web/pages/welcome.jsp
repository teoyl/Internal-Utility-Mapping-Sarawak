<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>

<script nonce="r4DjhKbfO5ry" src="pages/echart/echarts.min.js"></script>
<script src="include/custom_dashboard.js"></script>

<script nonce="r4DjhKbfO5ry">
    $(document).ready(function() {
        $.ajax({
            type: "POST",
            url: 'getTaskJobMapRouteJobMain',
            dataType: "json",
            data: {},
            success: function(response) {
//                console.log(response);
                $(".unassigned_job").html(response["poolCount"]);
                $(".in_progress").html(response["inProgressCount"]);
                $(".completed_today").html(response["completedToday"]);
                $(".completed_30").html(response["totalCompleted7Days"]);
            }
        });
    });
    
</script>

<style nonce="EuTVqS192VKl">
    #submissionChart, #appplicationChart {
        min-height: 320px;
    }
</style>

<div class="col-lg-12 col-xxl-12">
    <div class="card mb-3">
        <div class="card-header d-flex flex-between-center py-2 border-bottom">
            <div class="col-lg-6 border-bottom border-bottom-lg-0 pb-12 pb-lg-0">
                <h5><s:text name="utimaps.dashboard.job.title" /></h5>
            </div>
            <div class="col-lg-6 border-bottom border-bottom-lg-0 pb-12 pb-lg-0 text-end">
                <s:if test='userDivisionId.equals("00")'>
                    <a class="btn btn-falcon-default" href="dynamicAction?action=USJ_JobAdminRoute&retrieve=y"><i class="fas fa-list"></i> <s:text name="utimaps.dashboard.job.jobAdmin" /></a>
                </s:if>
                <s:else>
                    <a class="btn btn-falcon-default" href="dynamicAction?action=USJ_JobAdminRouteDiv&retrieve=y"><i class="fas fa-list"></i> <s:text name="utimaps.dashboard.job.jobAdmin" /></a>
                </s:else>
            </div>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-lg-3 border-end-lg border-bottom border-bottom-lg-0 pb-3 pb-lg-0 align-self-center">
                    <a class="link-underline-light" href="loadEditPageRouteJobMain?selected_tab=tab1">
                    <div class="d-flex justify-content-center ">
                        <p class="font-sans-serif lh-1 mb-1 pe-2">
                            <span class="fs-4 unassigned_job">0</span>
                            <s:text name="utimaps.dashboard.job.unassigned" />
                        </p>
                    </div>
                    </a>
                </div>
                <div class="col-lg-3 border-end-lg border-bottom border-bottom-lg-0 py-3 py-lg-0 align-self-center">
                    <a class="link-underline-light" href="loadEditPageRouteJobMain?selected_tab=tab2">
                    <div class="d-flex justify-content-center ">
                        <p class="font-sans-serif lh-1 mb-1 pe-2">
                            <span class="fs-4 in_progress">0</span>
                            <s:text name="utimaps.dashboard.job.inProgress" />
                        </p>
                    </div>
                    </a>
                </div>
                <div class="col-lg-3 border-end-lg border-bottom border-bottom-lg-0 py-3 py-lg-0 align-self-center">
                    <a class="link-underline-light" href="loadEditPageRouteJobMain?selected_tab=tab3">
                    <div class="d-flex justify-content-center ">
                        <p class="font-sans-serif lh-1 mb-1 pe-2">
                            <span class="fs-4 completed_today">0</span>
                            <s:text name="utimaps.dashboard.job.completedToday" />
                        </p>
                    </div>
                    </a>
                </div>
                <div class="col-lg-3 pt-3 pt-lg-0 align-self-center">
                    <a class="link-underline-light" href="loadEditPageRouteJobMain?selected_tab=tab4">
                    <div class="d-flex justify-content-center ">
                        <p class="font-sans-serif lh-1 mb-1 pe-2">
                            <span class="fs-4 completed_30">0</span>
                            <s:text name="utimaps.dashboard.job.completed30" />
                        </p>
                    </div>
                    </a>
                </div>
            </div>
        </div>
    </div>
    <div class="col">
        <div class="row g-3">
            <div class="col-md-6">
                <div class="card h-100">
                    <div class="card-header py-2 text-center fw-bold">
                        <s:text name="utimaps.dashboard.job.application" />
                    </div>
                    <div class="card-body">
                        <div class="row flex-between-center g-0">
                            <div class="col-md-12 col-xxl-12 mb-xxl-1">
                                <div class="position-relative">
                                    <div id="appplicationChart" class="appplicationChart" data-echart-responsive="true"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
<!--            <div class="col-md-3">
                <div class="card h-100">
                    <div class="card-header py-2 border-bottom text-center">
                        COMPUTATION OF CONTROL SURVEY
                    </div>
                    <div class="card-body d-flex flex-column justify-content-center">
                        <div class="row flex-between-center">
                            <div class="col d-md-flex d-lg-block flex-between-center">
                                <div class="d-flex justify-content-center ">
                                    <h1 class="mb-md-0 mb-lg-2">0</h1>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>-->

            <div class="col-md-6">
                <div class="card h-100">
                    <div class="card-header py-2 text-center fw-bold">
                        <s:text name="utimaps.dashboard.job.submission" />
                    </div>
                    <div class="card-body">
                        <div class="row flex-between-center">
                            <div class="col-md-12 col-xxl-12 mb-xxl-1">
                                <div class="position-relative">
                                    <div id="submissionChart" class="submissionChart" data-echart-responsive="true"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
<!--            <div class="col-md-3">
                <div class="card h-100">
                    <div class="card-header py-2 border-bottom text-center">
                        UNDER QUERY
                    </div>
                    <div class="card-body d-flex flex-column justify-content-center">
                        <div class="row flex-between-center">
                            <div class="col d-md-flex d-lg-block flex-between-center">
                                <div class="d-flex justify-content-center ">
                                    <h1 class="mb-md-0 mb-lg-2">3</h1>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>-->
        </div>
    </div>
</div>
