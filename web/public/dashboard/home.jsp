<%-- 
    Document   : home
    Created on : Nov 3, 2023, 10:34:31 AM
    Author     : yonglai
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<style>
    .sync-dashboard:hover {
        text-decoration: none;
        cursor: pointer;
    }
    .flex-box {
        display: flex;
        padding: 0 10px;
    }
    .submission-card {
        border: 1px solid #ccc;
        border-radius: 10px;
        padding: 10px 10px 0 15px;
        width: 100%;
        color: white !important;
    }
    .submission-card h1 {
        color: white !important;
    }
    .submission-card-title {
        /*padding: 0 5px;*/
    }
    .submission-card-title p {
        display: inline-block;
        width: 72%;
        margin-bottom: 10px;
    }
    .submission-card-title h1 {
        display: inline-block;
        width: 25%;
        text-align: right;
    }
    .submission-card-footer {
        border-top: 1px solid #ffffff9e;
    }
    .submission-card-footer-content {
        padding-right: 5px;
    }
    .submission-card-footer-content p {
        display: inline-flex;
        width: 100%;
        margin-bottom: 5px;
        align-items: center;
        color: #ffffff9e;
        font-size: 10.5pt;
    }
    .margin-bt-15 {
        margin-bottom: 15px;
    }
    
    .total-number {
        flex: 1;
        text-align: right;
        font-weight: 500;
        font-size: 19.5pt;
        /*font-size: 2.0736rem;*/
        color: #ffffff;
    }
    
    .blue-violet {
        background-color: #8159bd;
    }
    
    .han-blue {
        background-color: #556cc2;
    }

    .deep-saffron {
        background-color: #f19b37;
    }
    
</style>

<jsp:include page="/pages/base/actionError.jsp"/>

<!--Submission Card-->
<div class="row">
    <div>
        <p class="text-end"><a class="sync-dashboard"><span class="fas fa-sync"></span> Refresh Dashboard</a></p>
    </div>
</div>
<div class="row">
    <div class="col-md-3 margin-bt-15 flex-box">
        <div class="submission-card blue-violet">
            <div class="submission-card-title">
                <p>Total Active <br> Cadastral Submission</p>
                <h1 id="total-cad-sub">0</h1>
            </div>
            <div class="submission-card-footer">
                <div class="col-12 submission-card-footer-content">
                    <p>New Submission <span class="total-number" id="new-cad-sub">0</span></p>
                </div>
                <div class="col-12 submission-card-footer-content">
                    <p>Re-Submission <span class="total-number" id="re-cad-sub">0</span></p>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-3 margin-bt-15 flex-box">
        <div class="submission-card han-blue">
            <div class="submission-card-title">
                <p>Total Active <br> Utility Submission</p>
                <h1 id="total-ut-sub">0</h1>
            </div>
            <div class="submission-card-footer">
                <div class="col-12 submission-card-footer-content">
                    <p>New Submission <span class="total-number" id="new-ut-sub">0</span></p>
                </div>
                <div class="col-12 submission-card-footer-content">
                    <p>Re-Submission <span class="total-number" id="re-ut-sub">0</span></p>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-3 margin-bt-15 flex-box">
        <div class="submission-card deep-saffron">
            <div class="submission-card-title">
                <p>Total Utility Application</p>
                <h1 id="total-ut-app">0</h1>
            </div>
            <div class="submission-card-footer">
                <div class="col-12 submission-card-footer-content">
                    <p>New Submission <span class="total-number" id="new-ut-app">0</span></p>
                </div>
                <div class="col-12 submission-card-footer-content">
                    <p>Re-Submission <span class="total-number" id="re-ut-app">0</span></p>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-3 margin-bt-15 flex-box">
        <div class="submission-card blue-violet">
            <div class="submission-card-title">
                <p>Total <br> Notifications</p>
                <h1 id="total-not">0</h1>
            </div>
            <div class="submission-card-footer">
                <div class="col-12 submission-card-footer-content">
                    <p>Unread <span class="total-number" id="unread-not">0</span></p>
                </div>
            </div>
        </div>
    </div>
</div>

<!--Network Connection Status-->
<div class="row">
    <div></div>
</div>
        

<script>
    $(document).ready(function() {
        
        loadDashboardInfo();
        
        $(".sync-dashboard").on('click', function(e) {
            e.preventDefault();
            clearDashboardInfo();
            loadDashboardInfo();
            
        });
    });
    
    function loadDashboardInfo() {
        
        $.ajax({
            type: "POST",
            url: 'getAllSubmissionListApp',
            dataType: "json",
            data: {},
            success: function (response) {
                console.log("response",response);
                $("#total-cad-sub").html("5");
                $("#new-cad-sub").html("4");
                $("#re-cad-sub").html("1");
                $("#total-ut-sub").html("3");
                $("#new-ut-sub").html("1");
                $("#re-ut-sub").html("2");
                $("#total-ut-app").html("5");
                $("#new-ut-app").html("2");
                $("#re-ut-app").html("3");
                $("#total-not").html("98");
                $("#unread-not").html("3");
            }
        });
        
    }
    
    function clearDashboardInfo() {
        
        $("#total-cad-sub").html("0");
        $("#new-cad-sub").html("0");
        $("#re-cad-sub").html("0");
        $("#total-ut-sub").html("0");
        $("#new-ut-sub").html("0");
        $("#re-ut-sub").html("0");
        $("#total-ut-app").html("0");
        $("#new-ut-app").html("0");
        $("#re-ut-app").html("0");
        $("#total-not").html("0");
        $("#unread-not").html("0");

    }
</script>