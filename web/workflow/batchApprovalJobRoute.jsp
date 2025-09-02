<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <title><s:text name="jobList.title" /></title>
        <s:head />

        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />
        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="include/characterRemain.js"></script>
        <script language="javascript">
            $(document).ready(function() {
                window.history.pushState("", "", "loadBatchApprovalRouteJobMain");  // Only browser which support HTML5 works
            });

            var winPodH;
            function showUnassignedJob(pSysId) {
                //        winPodH=dhtmlmodal.open("popup", "iframe", "showUnassignedJobRouteJobMain?istrSystemId="+pSysId, "Tugasan Belum Diserah", "width=900px,height=400px,resize=1,scrolling=1,center=1", "");
                winPodH = dhtmlmodal.open("popup", "iframe", "processSearchRouteJobMain?istrSystemId=" + pSysId + "&jobSearchAction=search&defaultPageSize=50", "Tugasan Belum Diserah", "width=900px,height=500px,resize=1,scrolling=1,center=3", "");
                //        winPodH=dhtmlmodal.open("popup", "iframe", "loadUnassignJobSearchPageRouteJobMain?istrSystemId="+pSysId, "Tugasan Belum Diserah", "width=1000px,height=500px,resize=1,scrolling=1,center=1", "");
                //sereneC added @ 11/8/2014 to get back tab correct after load into iframe pop up.
                document.getElementById("insertJobForm").action = "processInsertRouteJobMain#tab" + pSysId;
                //        document.getElementById("insertJobForm").action="processInsertJob/Main#";
                winPodH.onclose = function() {
                    return false;
                }
            }

            // ThoTH @ 23-Jul-2015
            function cancelInformed(pPlId) {
                document.getElementById("selectJobId_").value = pPlId;
                document.getElementById("insertJobForm").action = "jobCancelInformedRouteJobMain#";
                document.insertJobForm.submit();
            }

            function check() {

                if (!checkForm())
                    return false;
                if (!confirmSign())
                    return false;

                return true;
            }

            function confirmSign() {
                var answer = confirm("You are about to SIGN this document. Do you want to proceed?");
                var isIE = false;

                isIE = isInternetExplorer();
                return (isIE ? event.returnValue = answer : answer);
            }

            function checkForm(theForm) {

                if (isNull(document.getElementById("certFile").value)) {
                    alert("Please select your DigiCert.");
                    return false;
                }
                if (!document.getElementById("certFile").value.match(/\.(p12|pfx)$/i)) {
                    alert("Only p12 or pfx format is allowed.");
                    return false;
                }
                if (isNull(document.getElementById("dcPass").value)) {
                    alert("Please enter your Password.");
                    return false;
                } else
                    return true;
            }

            function checkApprove() {
                var listSize = document.getElementById("listSize").value;
                //alert("list size " +listSize);
                var approved = "";

                for (var x = 0; x < listSize; x++) {
                    //        for (var x = 0, length = listSize; x < length; x++) {
                    //alert("x = "+x)
                    var radios = document.getElementsByName("decisionSelect[" + x + "]");
                    for (var i = 0; i < radios.length; i++) {
                        //            for (var i = 0, length = radios.length; i < length; i++) {
                        if (radios[i].checked) {
                            //alert(radios[i].value);              
                            if (document.getElementById("app_decision_remark_" + x + "_").value === "") {
                                document.getElementById("app_decision_remark_" + x + "_").value = "";
                            }
                            if (approved === "") {
                                approved = document.getElementById("id_" + x + "_").value + "_" + radios[i].value + "_" + document.getElementById("app_decision_remark_" + x + "_").value;
                            } else {
                                approved += "," +
                                        document.getElementById("id_" + x + "_").value + "_" + radios[i].value + "_" + document.getElementById("app_decision_remark_" + x + "_").value;
                            }
                        }
                    }
                }
                document.getElementById("selectedApp").value = approved;
                //alert("final " +document.getElementById("selectedApp").value)
            }
        </script>

        <%--sereneCHye@ 4/8/2014 :: added to support tab--%>
        <!--<script type="text/javascript" src="include/jquery_11.js"></script>-->
        <script type="text/javascript" src="include/simple_tab/simple_tab.js"></script>
        <link rel="stylesheet" href="include/simple_tab/simple_tab.css" />
    </head>
    <body >
        <form action="" id="batchApprovalForm" name="batchApprovalForm" method="post" class="prForm" enctype="multipart/form-data">
            <s:set name="eQP" value="@com.sains.common.util.SystemConstants$SYSTEM_ID@EQP"></s:set>      
            <s:set name="approvalVerification" value="@com.mrpe.qp.application.model.QPAppBase$APP_STATUS@APPROVAL_VERIFICATION"></s:set>   
            <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
            <div id="sysId_" style="display: none">${sysId_}</div>
            <%--<s:text name="jobList.title"/>--%>
            <!--div class="panel panel-default ">
                <div class="panel-heading ">
                    <h3 class="panel-title"> 
                        <span class="titleText"><s:text name="jobBatch.summary"/>
                        </span>
                    </h3>
                </div>
                <div class="panel-body bg-grey"-->

            <div class="row">
                <div class="col-md-12">
                    <h3 class="title-v3"><s:text name="qp.job.count" /></h3>
                    <div class="row form-row-margin">
                        <div class="col-md-4"><s:text name="qp.job.pending" /><small class="label pull-right bg-yellow">${decisionPending}</small></div>
<!--                    </div>
                    <div class="row form-row-margin">-->
    <div class="col-md-4"><s:text name="qp.job.approved" /><small class="label pull-right bg-green">${decisionApprove}</small></div>
<!--                    </div>
                    <div class="row form-row-margin">-->
                        <div class="col-md-4"><s:text name="qp.job.rejected" /><small class="label pull-right bg-red">${decisionReject}</small></div>
                    </div>
                </div>
                <!--<div class="col-md-1"></div>-->
<!--                <div class="col-md-5">
                    <h3 class="title-v3"><s:text name="qp.app.type" /></h3>
                    <div class="row form-row-margin">
                        <div class="col-md-12"><s:text name="qp.app.type.N" /><small class="label pull-right bg-green"><s:property value="getListNewJobProcessed_total().get('total')"/></small></div>
                    </div>
                    <div class="row form-row-margin">
                        <div class="col-md-12"><s:text name="qp.app.type.R" /><small class="label pull-right bg-yellow"><s:property value="getListNewJobProcessed_total().get('total')"/></small></div>
                    </div>
                </div>-->
                <!--<div class="col-md-1"></div>-->
            </div><br/><br/>


            <%--div  class=" jobSummary"> 
                <!--Format By Ivy @ 5/1/2017-->
                <table border="0" width="100%" >
                    <tr>
                        <td>
                            <strong><s:text name="qp.job.count" /></strong>
                            <table border="0" class="">
                                <tr>
                                    <td><s:text name="qp.job.pending" /> </td>
                                    <td><s:property value="getListDecisionPending_total().get('total')"/></td>
                                </tr>
                                <tr>
                                    <td><s:text name="qp.job.approved" /> </td>
                                    <td><s:property value="getListDecisionApproved_total().get('total')"/></td>
                                </tr>
                                <tr>
                                    <td><s:text name="qp.job.rejected" /> </td>
                                    <td><s:property value="getListDecisionRejected_total().get('total')"/></td>
                                </tr>
                            </table>
                        </td>
                        <td>
                            <strong><s:text name="qp.app.type" /></strong>
                            <table border="0" class="">
                                <tr>
                                    <td><s:text name="qp.app.type.N" /> </td>
                                    <td><s:property value="getListNewJobProcessed_total().get('total')"/></td>
                                </tr>
                                <tr>
                                    <td><s:text name="qp.app.type.R" /> </td>
                                    <td><s:property value="getListRenewJobProcessed_total().get('total')"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>

                <!--            <table border="0" width="100%" >
                                 <tr>
                                    <td width="15%"><s:text name="qp.job.count" /></td>                
                                    <td width="15%"><s:text name="qp.job.pending" /></td>
                                    <td align="left"><s:property value="getListDecisionPending_total().get('total')"/></td>                
                                    <td width="15%"><s:text name="qp.job.approved" /></td>
                                    <td align="left"><s:property value="getListDecisionApproved_total().get('total')"/></td>                
                                    <td width="15%"><s:text name="qp.job.rejected" /></td>
                                    <td align="left"><s:property value="getListDecisionRejected_total().get('total')"/></td>
                                </tr>
                                <tr>
                                    <td><s:text name="qp.app.type" /></td>                
                                    <td><s:text name="qp.app.type.N" /></td>
                                    <td><s:property value="getListNewJobProcessed_total().get('total')"/></td> 
                                    <td><s:text name="qp.app.type.R" /></td>
                                    <td><s:property value="getListRenewJobProcessed_total().get('total')"/></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                            </table>-->
            </div--%>
            <%--<s:if test="listJobtobeGrab.size() <= 0">--%>
                <!--<div class="errortxt col-xs-12"><%--You are not been assigned to any Workflow.--%>Tiada Aliran Kerja untuk anda.</div>-->
            <%--</s:if>--%>
            <%--<s:else>--%>
                <%--tab--%>

                <%--<div  class="col-lg-2 " style="padding:0px;margin-top: 40px;">
                      <ul class="tabs" style="padding-left: 0; margin: 0;">
                        <s:iterator value="listJobtobeGrab" status="jobPoolStatus" id="iteratorJobPool">
                            <li class="<s:if test="#jobPoolStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                <a href="#tab<s:property value="%{#iteratorJobPool.get('systemid')}"/>" class="">
                                    <div class="space" style="padding-top:10px;"><strong><s:property value="%{#iteratorJobPool.get('systemname')}"/></strong> </div>
                                    <div class="space" <s:if test="#iteratorJobPool.get('longcount') > 0">style="color:red;"</s:if>>
                                        <s:text name="jobList.jobUnassign" /> : <strong><s:property value="%{#iteratorJobPool.get('longcount')}"/></strong>
                                    </div >
                                    <div class="space" >
                                        <s:text name="jobList.jobNotComplete" /> : <strong><s:property value="%{#iteratorJobPool.get('ipcount')}"/></strong>
                                    </div >
                                </a>
                            </li>
                        </s:iterator>
                    </ul> 
                </div>--%>
                <%--detail--%>
                <s:if test="listJobtobeGrab.size() > 0">
                    <div id="pCVerify">
                        <button class="btn btn-primary" type="submit" name="action:updateBatchApproveRouteJobMain" id="updateBatchApproveRouteJobMain" onclick="checkApprove()" ><i class="fa fa-save"></i><s:text name="qp.button.save"/></button>
                        <button class="btn btn-default" type="submit" name="action:processVerifyApprovalRouteJobMain" id="processVerifyApprovalRouteJobMain" onclick="return checkApprove(),submitQpAppForm(this.form, 'processVerifyRouteQpApp', '');"><i class="fa fa-check"></i><s:text name="qp.button.complete"/></button>
                        <%--s:submit  type="submit" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="updateBatchApproveRouteJobMain" value="%{getText('qp.button.save')}" onclick="checkApprove()"  />
                        <s:submit  type="submit" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="processVerifyApprovalRouteJobMain" value="%{getText('qp.button.complete')}" onclick="return checkApprove(),submitQpAppForm(this.form, 'processVerifyQpApp', '');"/--%>
                    </div>
                </s:if>
                <br>
                <s:textfield  name="selectedApp" value="%{selectedApp}" style="display:none"/>
                <div class="table-responsive tableBorder">
                    <table class="jobListTable table table-espa table-condensed table-striped table-hover" cellspacing="0" cellpadding="0" width="100%">
                        <thead>
                            <tr class="jobHeader_1">
                                <th class="jobHeaderLabel space_left_5"> No.</th>
                                <th class="jobHeaderLabel"><s:text name="qp.app.name" /></th>
                                    <%--<td class="jobHeaderLabel" width="150px"><s:text name="qp.app.education" /></td>
                                    <td class="jobHeaderLabel" width="150px"><s:text name="qp.app.membership" /></td>
                                    <td class="jobHeaderLabel"><s:text name="qp.app.experience" /></td>--%>
                                <th class="jobHeaderLabel"><s:text name="qp.app.category_profession" /></th>
                                <th class="jobHeaderLabel"><s:text name="qp.app.secretary.recommendation" /></th>
                                <th class="jobHeaderLabel"><s:text name="qp.app.secretary.decision" /></th>
                                <th class="jobHeaderLabel"><s:text name="qp.register.remark" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <s:hidden name="listSize" value="%{applicationList.size()}"/>
                            <s:if test="applicationList.size() > 0">
                                <%--<s:iterator value="listJobBySystem" status="jobProgressStatus" id="iteratorJobBySystem">--%>
                                <s:iterator value="applicationList" status="appStatus" id="appList">                            
                                    <s:hidden name="id[%{#appStatus.index}]" value="%{#appList.app_id}"/>
                                    <tr>
                                        <%--<td class="jobLabel space_left_5">
                                        <s:if test='#iteratorJobBySystem.get("regstatus").equals(#draftVerified)'>
                                            <s:checkbox theme="simple" name="Sign_selected" id="ids" fieldValue="%{#iteratorJobBySystem.get('registerid')}" onclick="checkToggleCheckbox(select_all, ids);"/>                                    
                                        </s:if>
                                        <s:else>
                                            <s:checkbox theme="simple" name="Sign_selected" id="ids" disabled="true" onclick="checkToggleCheckbox(select_all, ids);"/>                                    
                                        </s:else>
                                    </td>--%>
                                        <td class="jobLabel">${appStatus.index + 1}</td>
                                        <!--APPLICANT NAME-->
                                        <td class="jobLabel">                                    
                                            <a title="View Application Form" href='loadEditPageQpApp?id=<s:property value="%{#appList.app_id}"/>&action=QPApp' target="_blank"><s:property value="%{#appList.pb_userModel.us_user_name}"/></a>
                                        </td>
                                        <!--ACADEMIC QUALIFICATION-->
                                        <%--<td class="jobLabel">
                                            <s:iterator value="#appList.QpEducationList" status="eduStatus" id="eduList">
                                                <s:property value="%{#eduList.edu_categoryModel.code_desc}"/> - 
                                                <s:property value="%{#eduList.edu_year}"/> - 
                                                <s:property value="%{#eduList.edu_institutModel.code_desc}"/> - 
                                            </s:iterator>
                                            
                                        </td>--%>
                                        <!--MEMBERSHIP OF PROFESSIONAL BODIES-->
                                        <%--<td id="jobLabel" class="jobLabel">
                                            <s:iterator value="#appList.QpMembershipList" status="memStatus" id="memList">
                                                <s:property value="%{#memList.mem_issue_autModel.code_desc}"/> #( 
                                                <s:property value="%{#memList.mem_no}"/> )                                       
                                            </s:iterator>
                                        </td>--%>
                                        <!--WORKING EXPERIENCES-->
                                        <%--<td class="jobLabel" style="word-wrap: break-word">
                                            <s:iterator value="#appList.QpExperienceList" status="expStatus" id="expList">
                                                <s:property value="%{#expList.exp_start_month}"/> <s:property value="%{#expList.exp_start_month}"/> 
                                                to
                                                <s:property value="%{#expList.exp_end_month}"/> <s:property value="%{#expList.exp_end_month}"/> 
                                                - 
                                                <s:property value="%{#expList.exp_employer}"/> )                                       
                                            </s:iterator>
                                        </td>--%>
                                        <!--REGISTRATION QUALIFICATION-->
                                        <td class="jobLabel">
                                            <s:property value="%{#appList.app_categoryModel.code_desc}"/> -                                            
                                            <s:property value="%{#appList.app_professionModel.code_desc}"/>                                           
                                        </td>
                                        <!--SECRETARY RECOMMENDED-->
                                        <td class="jobLabel">
                                            <u><b><s:text name="PO"/></b></u><br />
                                            <b><s:text name="common.status"/>: </b><s:text name="qp.recommended.%{app_recomm_status}"/> <br />    
                                            <b><s:text name="qp.app.check.remark"/>: </b><s:property value="app_recomm_remark"/> <br />    
                                            <br/>
                                            <u><b><s:text name="HOP"/></b></u><br />
                                            <b><s:text name="common.status"/>: </b><s:text name="qp.recommended.%{app_ver_status}"/> <br />   
                                            <b><s:text name="qp.app.check.remark"/>: </b><s:property value="app_ver_remark"/> <br />    
                                        </td>
                                        <!--SECRETARY DECISION-->
                                        <td class="jobLabel">
                                            <div class="radio radio-inline radio-success sutApproveRadio">
                                                <s:radio theme="simple" list="decisionList" listKey="keyData" listValue="valueData" name="decisionSelect[%{#appStatus.index}]" value="%{#appList.app_decision_status}" id="decisionSelect"  cssClass="requiredField"/>                                 
                                            </div>                   
                                        </td>
                                        <!--REMARK-->
                                        <td class="jobLabel">
                                            <%--<s:textfield theme="simple" name="app_decision_remark[%{#appStatus.index}]" value="%{#appList.app_decision_remark}"/>--%>                                    
                                            <div style="width: 100%; display: inline-block">
                                                <s:textarea cssClass="form-control" theme="simple" maxlength="2000" cols="30" rows="5" name="app_decision_remark[%{#appStatus.index}]" value="%{#appList.app_decision_remark}"/>                                    
                                            </div>
                                        </td>
                                    </tr>
                                </s:iterator>
                            </s:if>
                            <s:else>
                                <tr class="errortxt"><td colspan="5"><s:text name="jobList.jobNone" /></td></tr>
                            </s:else>
                        </tbody>
                    </table>
                    <br>
                    <%--            <div class="sub_header_bg" ><s:text name="Endorsed QP Certificate" /></div>
                                <table border="0" width="100%" cellpadding="2px" class="datatable table borderless">	
                                    
                                        <tr>
                                            <td width="10px"></td>
                                            <td width="170px"><s:text name="qp.select.digicert" /></td>
                                            <td width="5px">:</td>
                                            <td width="650px"><s:file theme="simple" name="certFile"/></td>	
                                        </tr>
                                        <tr>
                                            <td></td>
                                            <td><s:text name="qp.enter.pwd" /></td>
                                            <td>:</td>
                                            <td><input id="dcPass" name="dcPass" type="password" theme="simple" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                <s:submit cssClass="defaultButton" theme="simple" name="sign" value="Sign"  action="signBatchCertJobMain" 
                                                          onclick="if (isCheckboxSelected(form.Sign_selected)) {return check();} else {return false;}"
                                                          />
                                            </td>
                                        </tr>   
                                    
                                </table>
                                </div>--%>

                <%--</s:else>--%>
            </div>
            <!--/div-->
        </div>
    </form>
    <script type="javaascript">
        checkApprove();
    </script>
</body>
</html>