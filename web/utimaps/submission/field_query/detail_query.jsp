<%-- 
    Document   : detail_query
    Created on : Sep 18, 2024, 2:22:29 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="utimaps.query.title" /></title>
    </head>
    <body>
        <form id="jobForm" name="jobForm" action="initWorkflowFieldQuery" method="POST">
            <s:hidden name="id" value="%{model.job_id}"/>
            <s:hidden name="job_id" value="%{model.job_id}"/>
            <div class="row">
                <div class="col-sm-12 mb-5">
                    <div class="card">
                        <div class="card-header bg-primary">
                            <h5 class="card-title"><s:text name="jobadmin.caseInfo" /></h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-4">
                                    <p class="fw-bold mb-0"><s:text name ="TaskMgmt.manage.eCaseRef" /></p>
                                    <p><s:property value ='model.case_ref' /></p>
                                </div>
                                <s:if test='!model.usj_no.equals("")'>
                                    <div class="col-md-4">
                                        <p class="fw-bold mb-0"><s:text name ="TaskMgmt.manage.USJNo" /></p>
                                        <p><s:property value ='model.usj_no' /></p>
                                    </div>
                                </s:if>
                                <div class="col-md-4">
                                    <p class="fw-bold mb-0"><s:text name ="jobadmin.poolstatus" /></p>
                                    <p><s:property value='model.wf_status_2_str' /></p>
                                </div>
                                <div class="col-md-4">
                                    <p class="fw-bold mb-0"><s:text name="jobadmin.jobdesc" /></p>
                                    <p><s:property value='model.land_desc'/></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-12 mb-5">
                    <div class="card">
                        <div class="card-header bg-primary">
                            <h5 class="card-title"><s:text name="TaskMgmt.manage.assignProcessingOfficer" /></h5>
                        </div>
                        <div class="card-body">
                            <div class="col-md-12">
                                <s:if test='model.wf_status_2.equals("204") || model.wf_status_2.equals("205") || model.wf_status_2.equals("220")'>
                                    <s:if test='assigneeList.size() > 0'>
                                        <div class ="col-md-12 mb-3">
                                            <div class="form-group form-group-default form-group-default-select2">
                                                <label><s:text name="TaskMgmt.manage.assignTo"/></label>
                                                <s:select list="assigneeList" cssClass="form-control full-width" data-init-plugin="select2" listKey="userId" listValue="userName" name="assignTo__" theme="simple"/>                                            
                                            </div>
                                        </div>
                                        <div class ="col-md-12 mb-3"> 
                                            <button class="btn btn-success" type="submit" name="action:initWorkflowFieldQuery" id="initWorkflowFieldQuery"><i class="fa fa-user"></i> <s:text name="TaskMgmt.manage.A"/></button>
                                            <button class="btn btn-default" type="submit" name="action:cancelFieldQuery" id="cancelFieldQuery"><i class="fa fa-arrow-left"></i> <s:text name="button.back" /></button>
                                        </div>
                                    </s:if>
                                    <s:else>
                                        <div class="form-group form-group-default viewText">
                                            <label><s:text name="TaskMgmt.manage.assignTo"/></label>
                                            <s:text name="TaskMgmt.msg.noUser" />
                                        </div>
                                    </s:else>
                                </s:if>
                                <s:else>
                                    <b><s:text name="TaskMgmt.msg.taskInit" /></b>
                                </s:else>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        
        <script nonce="r4DjhKbfO5ry">
            $(document).ready(function() {                
                $("#initWorkflowFieldQuery").on('click', function(e) {
                    e.preventDefault();
                    var reassignTo = $("#assignTo__").val();
                    var jobId = $("#job_id").val();
                    var id = $("#id").val();

                    if(reassignTo === "") {
                        bootbox.alert({
                            closeButton: false,
                            message: "<s:text name="TaskMgmt.msg.pleaseSelect" />"
                        });
                    } else {
//                        $('#loadingModal').modal('show');
                        $.ajax({
                            type: "POST",
                            url: 'checkWorkflowBeforeFieldQuery',
                            dataType: "json",
                            data: {id: id, job_id: jobId},
                            success: function(response) {
                                console.log(response);
//                                $('#loadingModal').modal('hide');
                                var status = response['status'];
                                var confirmationMessage = "<s:text name="TaskMgmt.msg.confirmAssign" />";
                                
                                if(status === "Y") {
                                    confirmationMessage = response['message'];
                                } else {
                                    confirmationMessage = "<s:text name="TaskMgmt.msg.confirmAssign" />";
                                }
                                    
                                bootbox.confirm({
                                    closeButton: false,
                                    message: confirmationMessage,
                                    buttons: {
                                        confirm: {
                                            label: 'PROCEED'
                                        },
                                        cancel: {
                                            label: 'CANCEL'
                                        }
                                    },
                                    callback: function (result) {
                                        if (result) {
                                            $('#loadingModal').modal('show');
                                            $.ajax({
                                                type: "POST",
                                                url: 'initWorkflowFieldQuery',
                                                dataType: "json",
                                                data: {id: id, job_id: jobId, assignTo__: reassignTo},
                                                success: function(response) {
                                                    console.log(response);
                                                    $('#loadingModal').modal('hide');
                                                    bootbox.alert({
                                                        closeButton: false,
                                                        message: response['message'],
                                                        callback: function () { 
                                                            location.reload(true);
                                                        } 
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }

                });
            });
        </script>                        
    </body>
</html>
