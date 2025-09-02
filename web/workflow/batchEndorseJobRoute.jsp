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

        <script language="javascript">
            $(document).ready(function() {
                window.history.pushState("", "", "loadBatchEndorseRouteJobMain");  // Only browser which support HTML5 works
            });

            var winPodH;
            function showUnassignedJob(pSysId) {
                //        winPodH=dhtmlmodal.open("popup", "iframe", "showUnassignedJobJobMain?istrSystemId="+pSysId, "Tugasan Belum Diserah", "width=900px,height=400px,resize=1,scrolling=1,center=1", "");
                 winPodH = dhtmlmodal.open("popup", "iframe", "processSearchRouteJobMain?istrSystemId=" + pSysId + "&jobSearchAction=search&defaultPageSize=50", "Tugasan Belum Diserah", "width=900px,height=500px,resize=1,scrolling=1,center=3", "");
                 ////        winPodH=dhtmlmodal.open("popup", "iframe", "loadUnassignJobSearchPageJobMain?istrSystemId="+pSysId, "Tugasan Belum Diserah", "width=1000px,height=500px,resize=1,scrolling=1,center=1", "");
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
        </script>

        <%--sereneCHye@ 4/8/2014 :: added to support tab--%>
        <!--<script type="text/javascript" src="include/jquery_11.js"></script>-->
        <script type="text/javascript" src="include/simple_tab/simple_tab.js"></script>
        <link rel="stylesheet" href="include/simple_tab/simple_tab.css" />
    </head>
    <body >
        <form action="" id="batchEndorseForm" name="batchEndorseForm" method="post" class="prForm" enctype="multipart/form-data">
            <%--<s:set name="eQP" value="@com.sains.common.util.SystemConstants$SYSTEM_ID@EQP"></s:set>--%>      
            <s:set name="eQP" ><%=com.sains.common.util.SystemConstants.QP_SYS%></s:set>      
            <s:set name="draftVerified" value="@com.sains.common.util.SystemConstants$CERT_STATUS@DRAFT_VERIFIED"></s:set>            
            <s:set name="renewDraftVerified" value="@com.sains.common.util.SystemConstants$CERT_STATUS@RENEW_DRAFT_VERIFIED"></s:set>            
            <s:set name="renew" value="@com.mrpe.qp.application.model.QPAppBase$APP_TYPE@RENEW"></s:set>            
            <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
            <div id="sysId_" style="display: none">${sysId_}</div>
            <!--<div class="titleJobSummary col-xs-12" ><s:text name="jobList.title"/> </div>-->

            <!--div class="panel panel-default ">
            <div class="panel-heading ">
              <h3 class="panel-title"> 
                  <span class="titleText"><s:text name="jobBatch.summary"/>
                  </span>
              </h3>
            </div>
            <div class="panel-body bg-grey"-->
            <div  class=" jobSummary"> 

                <div class="row">
                    <div class="col-md-12">
                        <h3 class="title-v3"><s:text name="qp.job.count" /></h3>
                        <div class="row form-row-margin">
                            <!--<div class="col-md-6"><s:text name="qp.job.pending" /><small class="label pull-right bg-yellow"><s:property value="getListEndorsedPending_total().get('total')"/></small></div>-->
                            <div class="col-md-6"><s:text name="qp.job.pending" /><small class="label pull-right bg-yellow">${endorsePending}</small></div>
<!--                        </div>
                        <div class="row form-row-margin">-->
                            <!--<div class="col-md-6"><s:text name="qp.job.endorsed" /><small class="label pull-right bg-green"><s:property value="getListEndorsed_total().get('total')"/></small></div>-->
                            <div class="col-md-6"><s:text name="qp.job.endorsed" /><small class="label pull-right bg-green">${endorse}</small></div>
                        </div>
                    </div>
                    <!--<div class="col-md-1"></div>-->
<!--                    <div class="col-md-5">
                        <h3 class="title-v3"><s:text name="qp.app.type" /></h3>
                        <div class="row form-row-margin">
                            <div class="col-md-12"><s:text name="qp.app.type.N" /><small class="label pull-right bg-green"><s:property value="getListNewJobProcessed_total().get('total')"/></small></div>
                        </div>
                        <div class="row form-row-margin">
                            <div class="col-md-12"><s:text name="qp.app.type.R" /><small class="label pull-right bg-yellow"><s:property value="getListRenewJobProcessed_total().get('total')"/></small></div>
                        </div>
                    </div>-->
                    <!--<div class="col-md-1"></div>-->
                </div><br/><br/>      

                <!--Format By Ivy @ 5/1/2017-->
                <%--table border="0" width="100%" >
                     <tr>
                         <td>
                             <strong><s:text name="qp.job.count" /></strong>
                             <table border="0" class="">
                                 <tr>
                                     <td><s:text name="qp.job.pending" /> </td>
                                     <td><s:property value="getListEndorsedPending_total().get('total')"/> </td>
                                 </tr>
                                  <tr>
                                     <td><s:text name="qp.job.endorsed" /> </td>
                                     <td><s:property value="getListEndorsed_total().get('total')"/></td>
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
                </table--%>
                <!--            <table border="0" width="100%" >
                                 <tr>
                                    <td width="15%"><s:text name="qp.job.count" /></td>                
                                    <td width="15%"><s:text name="qp.job.pending" /></td>
                                    <td align="left"><s:property value="getListEndorsedPending_total().get('total')"/></div></td>                
                                    <td width="15%"><s:text name="qp.job.endorsed" /></td>
                                    <td align="left"><s:property value="getListEndorsed_total().get('total')"/></td>                
                                    <td width="15%"></td>
                                    <td align="left"></div></td>
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
            </div>

            <s:if test="listJobtobeGrab.size() <= 0">
                <div class="alert alert-danger col-xs-12"><%--You are not been assigned to any Workflow.--%>Tiada Aliran Kerja untuk anda.</div>
            </s:if>
            <s:else>
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
                <s:if test="genListBySystem(#eQP) > 0">
                    <div id="pCVerify" >
                        <s:if test='isFrom.equals("HOP")'>
                            <button class="btn btn-primary" type="submit" name="action:processVerifyEndorseHOPRouteJobMain" id="processVerifyEndorseHOPRouteJobMain" onclick="return submitQpAppForm(this.form, 'processVerifyEndorseHOPQpApp', '');"><i class="fa fa-check"></i><s:text name="qp.button.complete"/></button>
                                <%--<s:submit  type="submit" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="processVerifyEndorseHOPJobMain" value="%{getText('qp.button.complete')}" onclick="return submitQpAppForm(this.form, 'processVerifyEndorseHOPQpApp', '');"/>--%>
                            </s:if><s:else>
                            <button class="btn btn-primary" type="submit" name="action:processVerifyEndorseSUTRouteJobMain" id="processVerifyEndorseSUTRouteJobMain" onclick="return submitQpAppForm(this.form, 'processVerifyEndorseSUTQpApp', '');"><i class="fa fa-check"></i><s:text name="qp.button.complete"/></button>
                                <%--<s:submit  type="submit" cssClass="defaultButton dynamic-pull btn mrg-lr-5" theme="simple" action="processVerifyEndorseSUTJobMain" value="%{getText('qp.button.complete')}" onclick="return submitQpAppForm(this.form, 'processVerifyEndorseSUTQpApp', '');"/>--%>                                
                            </s:else>
                    </div>
                </s:if>
                <br/>
                <div class="col-lg-12" style="padding:0px;">
                    <div class="table-responsive tableBorder">
                        <table class="table table-espa table-condensed table-striped table-hover" cellspacing="0" cellpadding="0" width="100%">
                            <thead>
                                <tr class="jobHeader_1">
                                    <th class="jobHeaderLabel space_left_5"> <input type="checkbox" id="select_all" name="select_all" onclick="toggleCheckbox(this, Sign_selected);" /></th>
                                    <th class="jobHeaderLabel"><s:text name="jobList.jobNo" /></th>
                                    <th class="jobHeaderLabel" width="150px"><s:text name="jobList.jobDate" /></th>
                                    <!--<td class="jobHeaderLabel" width="150px"><s:text name="qp.kpi.date" /></td>-->
                                    <th class="jobHeaderLabel" width="150px"><s:text name="jobList.jobDueDate" /></th>

                                    <th class="jobHeaderLabel"><s:text name="qp.application.summary" /></th>
                                    <!--<td class="jobHeaderLabel"><s:text name="jobList.jobDetail" /></td>-->
                                    <th class="jobHeaderLabel"> <s:text name="qp.card.section" /></th>
                                    <th class="jobHeaderLabel"> <s:text name="qp.approval.date" /></th>
                                    <th class="jobHeaderLabel"> <s:text name="qp.status" /></th>
                                </tr>
                            </thead>
                            <tbody>
                                <s:if test="genListBySystem(#eQP) > 0">
                                    <s:iterator value="listJobBySystem" status="jobProgressStatus" id="iteratorJobBySystem">
                                        <tr class="<s:if test="#jobProgressStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                                <td class="jobLabel space_left_5">
                                                <s:if test='#iteratorJobBySystem.get("regstatus").equals(#draftVerified) || #iteratorJobBySystem.get("regstatus").equals(#renewDraftVerified)'>
                                                    <s:checkbox theme="simple" name="Sign_selected" id="ids" fieldValue="%{#iteratorJobBySystem.get('registerid')}" onclick="checkToggleCheckbox(select_all, ids);"/>                                    
                                                </s:if>
                                                <s:else>
                                                    <s:checkbox theme="simple" name="Sign_selected" id="ids" disabled="true" onclick="checkToggleCheckbox(select_all, ids);"/>                                    
                                                </s:else>
                                            </td>
                                            <td class="jobLabel">${jobProgressStatus.index + 1}</td>
                                            <!--ASSIGN DATE-->
                                            <td></td>
                                            <!--DUE DATE-->
                                            <td class="jobLabel">
                                                <s:if test="#iteratorJobBySystem.get('duedate') != null">
                                                    <s:text name="date_default_datetime"><s:param value="%{#iteratorJobBySystem.get('duedate')}"/></s:text>
                                                </s:if>    
                                            </td>
                                        <!--APPLICATION SUMMARY-->
                                            <td id="jobLabel" class="jobLabel">
                                                <s:text name="qp.app.type.%{#iteratorJobBySystem.get('apptype')}"/> - 
                                                <s:text name="getSetupCodeDesc(#iteratorJobBySystem.get('category'))"/> - 
                                                <s:text name="getSetupCodeDesc(#iteratorJobBySystem.get('profession'))"/>                                            
                                                <s:if test="#iteratorJobBySystem.get('apptype').equals(#renew)">
                                                    <br><s:text name="qp.app.name"/>: <s:property escape="false" value="%{#iteratorJobBySystem.get('username')}"/>
                                                    <s:text name="getRenewInfo(#iteratorJobBySystem.get('podid'))"/>                                                
                                                </s:if>
                                           </td>
                                        <%--JOB DESC commented by Ivy--%>
                                        <%--<td class="jobLabel" style="word-wrap: break-word">
                                            <s:text name="qp.app.name"/>: <s:property escape="false" value="%{#iteratorJobBySystem.get('username')}"/>
                                        </td>--%>
                                        <!--CARD INFORMATION Rearrange by Ivy-->
                                        <td class="jobLabel">
                                            <s:if test='#iteratorJobBySystem.get("apptype").equals("N")'>
                                                <table align="left" border="0" width="100%">
                                                    <tr><td width="40%"><s:text name="qp.registration.no" /></td>
                                                    <td>
                                                       : <s:property escape="false" value="%{#iteratorJobBySystem.get('cardregnumber')}"/>
                                                    </td></tr>
                                                    <%--Commented by ahmadni - 12-Jul-2017--%>
                                                    <%--<tr><td> <s:text name="qp.app.reg.cate" /></td>
                                                        <td>
                                                         : <s:property escape="false" value="%{#iteratorJobBySystem.get('cardpart')}"/>
                                                        </td></tr>--%>
                                                    <tr><td> <s:text name="qp.certificate.holder" /></td>
                                                    <td>
                                                        : <s:property escape="false" value="%{#iteratorJobBySystem.get('cardusername')}"/>
                                                        </td></tr>
                                                    <tr><td> <s:text name="user.ic.no" /></td>
                                                    <td>
                                                        : <s:property escape="false" value="%{#iteratorJobBySystem.get('cardicno')}"/>
                                                        </td>
                                                    </tr>
                                                    <tr valign="top"><td> <s:text name="qp.app.postal.address" /></td>
                                                    <td>
                                                        :  <s:property escape="false" value="%{#iteratorJobBySystem.get('cardaddr1')}"/><br/>
                                                            <s:property escape="false" value="%{#iteratorJobBySystem.get('cardaddr2')}"/><br/>
                                                            <s:property escape="false" value="%{#iteratorJobBySystem.get('cardaddr3')}"/><br/>
                                                            <s:property escape="false" value="%{#iteratorJobBySystem.get('cardaddr4')}"/><br/>
                                                        </td></tr>
                                                    <tr><td> <s:text name="qp.app.category_profession" /></td>
                                                    <td>
                                                        :
                                                        <s:property escape="false" value="%{#iteratorJobBySystem.get('cardpart')}"/> -  
                                                        <s:property escape="false" value="%{#iteratorJobBySystem.get('cardprofession')}"/>
                                                        </td></tr>
                                                </table>
                                             
                                            </s:if><s:else>
                                                <i>No Card Information</i>
                                            </s:else>
                                        </td>
                                        <!--APPROVAL DATE-->
                                        <td class="jobLabel">
                                            <s:if test='#iteratorJobBySystem.get("approvaldate") != null || !#iteratorJobBySystem.get("approvaldate").equals("") '>
                                                <s:text name="date_default_datetime"><s:param value="%{#iteratorJobBySystem.get('approvaldate')}"/></s:text>                                                          
                                            </s:if>
                                        </td>
                                            
                                        <!--APP STATUS-->
                                        <td class="jobLabel">
                                            <s:text name="qp.register.status.%{#iteratorJobBySystem.get('regstatus')}"/>                                    
                                        </td>
                                        </tr>
                                    </s:iterator>
                                </s:if>
                                <s:else>
                                    <tr class="errortxt"><td colspan="8"><s:text name="jobList.jobNone" /></td></tr>
                                    </s:else>
                            </tbody>
                        </table>
                    </div>
                    <br>
                    <s:if test="genListBySystem(#eQP) > 0"><br/>
                        <div class="row">
                            <div class="col-md-12">
                                <h3 class="title-v3"><s:text name="Endorse QP Certificates" /></h3>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group form-group-default">
                                    <label><s:text name="qp.select.digicert" /></label>
                                    <s:file theme="simple" name="certFile"/>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group form-group-default">
                                    <label><s:text name="qp.enter.pwd" /></label>
                                    <input class="form-control" id="dcPass" name="dcPass" type="password" theme="simple" />
                                </div>
                            </div>
                            <div class="col-md-4">
                                <s:if test='isFrom.equals("HOP")'>
                                    <button class="btn btn-primary" type="submit" name="action:signBatchCertHOPRouteJobMain" id="signBatchCertHOPJobMain" onclick="if (isCheckboxSelected(form.Sign_selected)) {return check();} else {return false;}"><i class="fa fa-pencil"></i>Sign</button>
                                    <%--<s:submit cssClass="defaultButton" theme="simple" name="sign" value="Sign" action="signBatchCertHOPJobMain">                                                 
                                              onclick="if (isCheckboxSelected(form.Sign_selected)) {return check();} else {return false;}"/--%>
                                </s:if><s:else>
                                <button class="btn btn-primary" type="submit" name="action:signBatchCertSUTRouteJobMain" id="signBatchCertSUTJobMain" onclick="if (isCheckboxSelected(form.Sign_selected)) {return check();} else {return false;}"><i class="fa fa-pencil"></i>Sign</button>
                                  <%--<s:submit cssClass="defaultButton" theme="simple" name="sign" value="Sign" action="signBatchCertSUTJobMain"--%>                                               
                                              <!--onclick="if (isCheckboxSelected(form.Sign_selected)) {return check();} else {return false;}"/>-->

                                </s:else>
                            </div>
                        </div>

                        <%--div class="sub_header_bg" ><s:text name="Endorse QP Certificates" /></div>
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
                                    <s:if test='isFrom.equals("HOP")'>
                                        <s:submit cssClass="defaultButton" theme="simple" name="sign" value="Sign" action="signBatchCertHOPJobMain"                                                 
                                                  onclick="if (isCheckboxSelected(form.Sign_selected)) {return check();} else {return false;}"/>
                                    </s:if><s:else>
                                        <s:submit cssClass="defaultButton" theme="simple" name="sign" value="Sign" action="signBatchCertSUTJobMain"                                                 
                                                  onclick="if (isCheckboxSelected(form.Sign_selected)) {return check();} else {return false;}"/>

                                    </s:else>
                                </td>
                            </tr>   

                        </table--%>
                    </s:if>
                </div>

            </s:else>
            <!--/div-->
        </div>   <br/> <br/>      
       
    </form>
</body>
</html>