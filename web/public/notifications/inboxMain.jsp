<%-- 
    Document   : inboxMain
    Created on : Nov 9, 2023, 9:58:34 AM
    Author     : yonglai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<style>
    .notes-label {
        color: var(--spanish-grey-color);
        font-size:12px;
        margin: 0;
    }
    .select-wrap {
        border: 1px solid var(--silver-color);
        border-radius: 4px;
        padding: 0 5px;
        width: 180px;
        background-color: var(--white-color);
        position: relative;
    }
    .select-wrap label{
        font-size: 12px;
        color: var(--silver-color);
        padding: 0 5px;
        position: absolute;
        top: 5px;
    }

    select{
        background-color: var(--white-color);
        border:0px;
        height:50px;
        padding-top: 20px;
        font-size: 14px;
        width: 100%;
    }

    select:focus-visible {
        outline: transparent 0;
    }
    
    .filter-div {
        padding: 0 10px;
    }
    
    .filter-button {
        font-size: 14px;
        color: var(--black-color);
        padding: 15px 25px;
        border-radius: 5px;
        background: gainsboro;
    }
    
    thead {
        border-bottom: 1px solid #000;
        border-top: 1px solid #000;
        font-size: 14px;
        font-weight: 600;
        color: var(--black-color);
    }
    
    
    @media (min-width: 768px) { 
        table {
            table-layout: fixed;
        }
    }

    .not-title {
        color: var(--azure-color);
        font-size: 14px;
        font-weight: 600;
    }
    .not-from {
        color: var(--black-color);
        font-size: 13px;
        font-weight: 500;
    }
    .not-sender {
        color: var(--spanish-grey-color);
        font-size: 13px;
        font-weight: 400;
    }
    .not-content {
        color: var(--granite-grey-color);
        font-size: 14px;
        font-weight: 400;
    }
    .not-date {
        color: var(--spanish-grey-color);
        font-size: 14px;
        font-weight: 400;
    }
    .no-margin {
        margin: 0;
    }
    .dot-icon {
        color: var(--azure-color);
        font-size: 33px;
    }

</style>


<jsp:include page="/pages/base/actionError.jsp"/>

<div class="row">
    <div class="col-md-6 d-flex align-items-end">
        <p class="notes-label">1,200 notifications, 3 unread</p>
    </div>
    <div class="col-md-6 ">
        <div class="select-wrap float-end">
            <label>Sort By</label>
            <select class="" name="sortBy">
                <option value="date">Notification Date</option>
                <option value="subject">Subject</option>
            </select>
        </div>
        <div class="float-end filter-div">
            <button type="button" class="btn btn-light filter-button" data-bs-toggle="modal" data-bs-target="#staticBackdrop">
                <span class="fas fa-sliders-h"></span>  Filters
            </button>
        </div>
    </div>
</div>
<br/>

<div class="row">
    <div class="table-responsive-md">
        <table class="table ">
            <thead>
              <tr>
                <th class="w-5" scope="col"></th>
                <th class="w-75" scope="col">Subject</th>
                <th class="w-20" scope="col">Notification Date</th>
              </tr>
            </thead>
            <tbody>
                <tr>
                    <td scope="row"><span class="dot-icon">&#x2022;</span></td>
                    <td>
                        <p class="no-margin not-title text-truncate">[USCS10] Notification of resubmission for USJ ID/0001/2024 located at Utility Survey Works and mapp ...</p>
                        <p class="no-margin not-from">From: <span class="not-sender">Superintendent of Land and Survey</span></p>
                        <p class="no-margin not-content">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labo ...</p>
                    </td>
                    <td>
                        <p class="not-date">04/08/2023 16:37</p>
                    </td>
                </tr>
                <tr>
                    <td scope="row"></td>
                    <td>
                        <p class="no-margin not-title text-truncate">[USCS10] Notification of resubmission for USJ ID/0001/2024 located at Utility Survey Works and mapp ...</p>
                        <p class="no-margin not-from">From: <span class="not-sender">Superintendent of Land and Survey</span></p>
                        <p class="no-margin not-content">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labo ...</p>
                    </td>
                    <td>
                        <p class="not-date">04/08/2023 16:37</p>
                    </td>
                </tr>
                <tr>
                    <td scope="row"></td>
                    <td>
                        <p class="no-margin not-title text-truncate">[USCS10] Notification of resubmission for USJ ID/0001/2024 located at Utility Survey Works and mapp ...</p>
                        <p class="no-margin not-from">From: <span class="not-sender">Superintendent of Land and Survey</span></p>
                        <p class="no-margin not-content">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labo ...</p>
                    </td>
                    <td>
                        <p class="not-date">04/08/2023 16:37</p>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</div>

<!--Filter Modal-->
<div class="modal fade" id="staticBackdrop" data-backdrop="static" data-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="staticBackdropLabel">Filter Notifications</h4>
                <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body mt-0 pt-0">
                <div class="mt-3">
                    <div class="p-2 rounded checkbox-form">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault-1">
                            <label class="unread form-check-label" for="flexCheckDefault-1">Unread</label>
                        </div>     
                    </div>

                    <div class="p-2 rounded checkbox-form">
                        <div class="form-check">
                          <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault-2">
                          <label class="read form-check-label" for="flexCheckDefault-2">Read</label>
                        </div>     
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        $(".messageList").select2();

    });
</script>