<%-- 
    Document   : api_page
    Created on : Sep 6, 2024, 3:30:16 PM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome to API Page</title>
    </head>
    <body>
        <form id="checkSTADetailsCaseSS" action="" name="checkSTADetailsCaseSS" class="prForm" method="post" enctype="multipart/form-data">
            <div class="row">
                <div class="col-sm-12 mb-5">
                    <div class="card mb-5">
                        <div class="card-header bg-primary">
                            <h5 class="card-title">Here for Traverse Cases</h5>
                        </div>
                        <div class="card-body">
                            <div class="row mb-3">
                                <label class="col-md-3 col-form-label fs-smaller">Survey Job No.</label>
                                <div class="col-md-4">
                                    <s:textfield class="form-control" name = "usj_no" value = "" />
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-md-3 col-form-label fs-smaller">Job Id</label>
                                <div class="col-md-4">
                                    <s:textfield class="form-control" name = "job_id" value = "" />
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-md-3 col-form-label fs-smaller">Task to start</label>
                                <div class="col-md-4">
                                    <s:select 
                                        name="task_selected"
                                        value=""
                                        list="taskListForApiList"
                                        listKey="keyData"
                                        listValue="valueData"                    
                                        cssClass="form-select"
                                     />
                                </div>
                            </div>
                            <div class="col-md-12 mb-3">
                                <button class="btn btn-success" id="submitApi" type="submit">Submit</button>
                            </div>
                        </div>
                    </div>
                                
                    <div class="card">
                        <div class="card-header bg-primary">
                            <h5 class="card-title">Here for Non-Traverse Cases</h5>
                        </div>
                        <div class="card-body">
                            <div class="row mb-3">
                                <label class="col-md-3 col-form-label fs-smaller">Survey Job No.</label>
                                <div class="col-md-4">
                                    <s:textfield class="form-control" name = "usj_no2" value = "" />
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-md-3 col-form-label fs-smaller">Job Id</label>
                                <div class="col-md-4">
                                    <s:textfield class="form-control" name = "job_id2" value = "" />
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-md-3 col-form-label fs-smaller">Task to start</label>
                                <div class="col-md-4">
                                    <s:select 
                                        name="task_selected2"
                                        value=""
                                        list="taskListForApiList2"
                                        listKey="keyData"
                                        listValue="valueData"                    
                                        cssClass="form-select"
                                     />
                                </div>
                            </div>
                            <div class="col-md-12 mb-3">
                                <button class="btn btn-success" id="submitApi2" type="submit">Submit</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
                                
        <script nonce="r4DjhKbfO5ry">
            $( document ).ready(function() {
                $('#submitApi').click( function (e) {
                    e.preventDefault();
                    $(".waiting-msg").show();
                    var usj_no = $("#usj_no").val();
                    var job_id = $("#job_id").val();
                    var task_selected = $("#task_selected").val();
                    console.log(usj_no)
                    console.log(task_selected)
                    
                    bootbox.confirm({
                        message: "Confirm to submit?", 
                        closeButton: false,
                        buttons: {
                            confirm: {
                                label: 'Yes'
                            },
                            cancel: {
                                label: 'No'
                            }
                        },
                        callback: function(result) {
                            if(result) {     
                                $.ajax({
                                    type: "POST",
                                    url: "submit-submission2-test",
                                    data: { "usj_no" : usj_no, "jobId" : job_id, "wf_id" : task_selected},
                                    success: function (response) {
                                        console.log(response)
                                        
                                        $(".waiting-msg").hide();
                                        bootbox.alert({
                                            closeButton: false,
                                            message: response['submit_message']
                                        });
                                    }
                                });
                            } else {
                                $(".waiting-msg").hide();
                            }
                        }
                    });
                });
            
                $('#submitApi2').click( function (e) {
                    e.preventDefault();
                    $(".waiting-msg").show();
                    var usj_no = $("#usj_no2").val();
                    var job_id = $("#job_id2").val();
                    var task_selected = $("#task_selected2").val();
                    console.log(usj_no)
                    console.log(task_selected)
                    
                    bootbox.confirm({
                        message: "Confirm to submit?", 
                        closeButton: false,
                        buttons: {
                            confirm: {
                                label: 'Yes'
                            },
                            cancel: {
                                label: 'No'
                            }
                        },
                        callback: function(result) {
                            if(result) {     
                                $.ajax({
                                    type: "POST",
                                    url: "submit-submission3-test",
                                    data: { "usj_no" : usj_no, "jobId" : job_id, "wf_id" : task_selected},
                                    success: function (response) {
                                        console.log(response)
                                        
                                        $(".waiting-msg").hide();
                                        bootbox.alert({
                                            closeButton: false,
                                            message: response['submit_message']
                                        });
                                    }
                                });
                            } else {
                                $(".waiting-msg").hide();
                            }
                        }
                    });
            
                });
            });
        </script>
    </body>
</html>
