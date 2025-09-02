<%-- 
    Document   : userAccess_step1
    Created on : Feb 18, 2014, 3:53:01 PM
    Author     : Delvene
--%>


<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-dojo-tags" prefix="sx"%>
<sx:head parseContent="true" debug="false" />

<script type="text/javascript" src="pages/scripts/common.js"></script>
<script type="text/javascript" src="pages/scripts/validation.jsp"></script>
<script type="text/javascript" src="pages/scripts/confirmation.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
<link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
<link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />

<s:set name="userActive" value="@com.sains.common.util.SystemConstants$USER_ACC_STATUS@ACTIVE"></s:set>
<s:set name="userInactive" value="@com.sains.common.util.SystemConstants$USER_ACC_STATUS@INACTIVE"></s:set>
<s:set name="userLock" value="@com.sains.common.util.SystemConstants$USER_ACC_STATUS@LOCKED"></s:set>
<s:set name="DEPAdmin" value="@com.sains.common.util.SystemConstants$USER_GROUP_CODE@DEPAdmin"></s:set>
<s:set name="HRMAdmin" value="@com.sains.common.util.SystemConstants$USER_GROUP_CODE@HRMAdmin"></s:set>


<script language="javascript">
    
    var winPodH;
    function showApp(sUgId) {
        winPodH=dhtmlmodal.open("popup", "iframe", "loadAppPageUserAccess?UgId="+sUgId, "Senarai Menu/Fungsi diberi Akses", "width=603px,height=400px,resize=1,scrolling=1,center=1", "");
    }
     function showSecurity(sUgId) { 
         
        winPodH=dhtmlmodal.open("popup", "iframe", "loadSecPageUserAccess?UgId="+sUgId, "Senarai Sekuriti", "width=600px,height=400px,resize=1,scrolling=1,center=1", "");
    }
    function showWF(sUgId) { 
         
        winPodH=dhtmlmodal.open("popup", "iframe", "loadWFPageUserAccess?UgId="+sUgId, "Senarai Kumpulan Pengesah", "width=600px,height=400px,resize=1,scrolling=1,center=1", "");
    }

    function showUserEdit(strUsId) {
        winPodH=dhtmlmodal.open("popup", "iframe", "loadUserEditUserAccess?usId="+strUsId, "Kemaskini pengguna", "width=600px,height=200px,resize=1,scrolling=1,center=1", "");
    }
    
    function confirmUpdateStatus(sStatus){
        var sdeptAdmin = document.getElementById("ug_id").value;
        var bExistDeptAdmin = document.getElementById("existDeptAdmin_").value;
            if(sStatus == '${userActive}'){
                var answer = confirm("Anda akan mengaktifkan pengguna ini. Teruskan?");
//            } else if (sStatus == "L") {
            } else if (sStatus == '${userLock}') {
               
//                if (sdeptAdmin == "DEP_Admin" && bExistDeptxAdmin == false) {
                if (sdeptAdmin == '${DEPAdmin}' && bExistDeptAdmin == "false") {
                    var answer = confirm("Anda akan menguncikan pengguna ini. Tiada Dept Admin yang lain dalam jabatan anda. Teruskan?");
                } else if (sdeptAdmin == '${HRMAdmin}' && bExistDeptAdmin == "false") {
                    var answer = confirm("Anda akan menguncikan pengguna ini. Tiada Central Admin yang lain dalam Sistem. Teruskan?");
                } else {
                    var answer = confirm("Anda akan menguncikan pengguna ini. Teruskan?");
                }

            } else{
//                if (sdeptAdmin == "DEP_Admin" && bExistDeptAdmin == false) {
                if (sdeptAdmin == '${DEPAdmin}' && bExistDeptAdmin == "false"){
                  
                    var answer = confirm("Anda akan menyahaktifkan pengguna ini. Tiada Dept Admin yang lain dalam jabatan anda. Teruskan?");
                } else if (sdeptAdmin == '${HRMAdmin}' && bExistDeptAdmin == "false") {
                    var answer = confirm("Anda akan menyahaktifkan pengguna ini. Tiada Central Admin yang lain dalam Sistem. Teruskan?");
                } else {
                    var answer = confirm("Anda akan menyahaktifkan pengguna ini. Teruskan?");
                }
            }

            var isIE = false;
            isIE = isInternetExplorer();
            return (isIE ? event.returnValue = answer : answer);
            
            
        }
</script>



<form method="post" id="prForm" class="prForm" action="processUpdateUserAccess" >
    <s:hidden theme="simple" name="action" />
    <s:hidden theme="simple" name="id" />
    <s:hidden theme="simple" name="model.ID" />
    <s:hidden theme="simple" name="model.emp_id" />
    <s:hidden theme="simple" name="currStep_" />
    <s:hidden theme="simple" name="existDeptAdmin_" value="%{existDeptAdmin_}"/>
    <s:hidden theme="simple" name="existCentralAdmin_" value="%{existCentralAdmin_}"/>
    <table cellspacing="0" cellpadding="2" border="0" width="99%" class="form">
        <tr class="button-container">
            <td> 
                <div class="step">
                    <span class="header1">${currentStep_} : </span>
                </div>
            </td>
            <td align="right" >
                <s:if test='existGroup_'>
                    <s:submit type="submit" cssClass="defaultButton buttonUpdate" theme="simple" action="loadUserEditUserAccess" value="%{getText('button.update.user')}" title="" />
                </s:if>
                <s:if test='model.us_status.equals(#userActive)'>
                <%--<s:if test="model.us_status.equals('Y')">--%>
                    <%--<s:hidden name="us_status" value="I"/>--%>
                    <s:submit type="submit" cssClass="defaultButton buttonGo" theme="simple" action="nextStepUserAccess" value="%{getText('button.update')}" title=""/>
                    <s:submit type="submit" cssClass="defaultButton buttonUpdate" theme="simple" action="processInactiveUserAccess" onclick="return confirmUpdateStatus('I');" value="%{getText('userAccess.accStatus.I')}" title=""/>
                    <%--<s:submit type="submit" cssClass="defaultButton buttonUpdate" theme="simple" action="processInactiveUserAccess" onclick="return confirmUpdateStatus('I');" value="%{getText('userAccess.accStatus.I')}" title=""/>--%>
                    <s:submit type="submit" cssClass="defaultButton buttonUpdate" theme="simple" action="processLockUserAccess" onclick="return confirmUpdateStatus('L');" value="%{getText('userAccess.accStatus.L')}" title=""/>
                </s:if>
                <s:elseif test="model.us_status.equals(#userInactive)">
                     <s:submit type="submit" cssClass="defaultButton buttonGo" theme="simple" action="nextStepUserAccess"  value="%{getText('button.update')}" title=""/>
                     <s:submit type="submit" cssClass="defaultButton buttonUpdate" theme="simple" action="processActiveUserAccess" onclick="return confirmUpdateStatus('Y');" value="%{getText('userAccess.accStatus.Y')}"  title=""/>
                </s:elseif>
                <%--<s:elseif test="model.us_status.equals('N')">
                    <s:submit type="submit" cssClass="defaultButton buttonUpdate" theme="simple" action="loadNewUserAccess" value="%{getText('button.update')}" title=""/>
                </s:elseif>--%>
                <s:elseif test="model.us_status.equals(#userLock)">
                    <%--<s:hidden name="us_status" value="Y"/>--%>
                    <s:submit type="submit" cssClass="defaultButton buttonGo" theme="simple" action="nextStepUserAccess" value="%{getText('button.update')}" title=""/>
                    <s:submit type="submit" cssClass="defaultButton buttonUpdate" theme="simple" action="processActiveUserAccess" onclick="return confirmUpdateStatus('Y');" value="%{getText('userAccess.accStatus.Y')}" title=""/>
                </s:elseif>
                <s:submit type="submit" cssClass="defaultButton buttonBackToList" theme="simple" action="cancelUserAccess" value="" title="%{getText('button.back')}"/>
            </td>
        </tr>
    </table>
         
            <!--<table cellspacing="1" cellpadding="1" width="100%" border="0" class="defaultTable" >-->
       <table cellspacing="0" cellpadding="3" width="98%" border="0" id="userAccess_crumb" style="margin: 10px;">
        <tr valign="top" class="sub_header_bg2"  >
            <th width="20%"><s:text name="userAccess.module"/></th>
            <th width="40%"><s:text name="userAccess.module.app"/></th>
            <th width="40%"><s:text name="userAccess.module.wf"/></th>
        </tr>
        <s:iterator value="systemList_" id="system" status="systemStatus">
            <tr>
                <td valign="top" style="padding-bottom: 5px;padding-left: 5px; font-weight: bold;">${system.system_name}</td>
                <td valign="top">
                    <s:iterator value="%{#system.setupGroupAppList}" id="groupApp" status="groupAppStatus">
                       
                        <table width="100%">
                            <tr><td style="padding-bottom: 5px;padding-left: 5px">${groupApp.group_name}
                                    <s:hidden name="ug_id" id="ug_id" value="%{#groupApp.ug_id}"/>
                                </td>
                                <td style="text-align: right;" width="25px"><a class="appButton" href="" onclick="showApp('${groupApp.ug_id}');return false;"><img src="images/action_icon/app_icon.jpg" title="<s:text name='userAccess.application'/>"/><%--<s:text name="userAccess.application"/>--%></a></td>
                                <td style="text-align: right;" width="25px"><a class="secButton" href="" onclick="showSecurity('${groupApp.ug_id}');return false;"><img src="images/action_icon/sec_icon.jpg" title="<s:text name='userAccess.security'/>"/><%--<s:text name="userAccess.security"/>--%></a></td>
                            </tr>
                        </table>
                    </s:iterator>
                </td>
                <td valign="top" > 
                    <s:iterator value="%{#system.setupGroupWFList}" id="groupWF" status="groupWFStatus">
                        <s:hidden name="ug_id" id="ug_id"  value="%{groupWF.ug_id}"/>
                        <table width="100%" valign="top">
                            <tr valign="top"><td style="padding-bottom: 5px;padding-left: 5px" >${groupWF.group_name}</td>
                                <td style="text-align: right;" width="25px"> <a class="supportButton" href="" onclick="showWF('${groupWF.ug_id}');return false;"><img src="images/action_icon/app_icon.jpg" title="<s:text name='userAccess.application'/>"/></a></td>
                                <td style="text-align: right;" width="25px"><a class="supportButton" href="" onclick="showSecurity('${groupWF.ug_id}');return false;"><img src="images/action_icon/sec_icon.jpg" title="<s:text name='userAccess.security'/>"/></a></td>
                            </tr>
                        </table>
                    </s:iterator>
                </td>
            </tr>
        </s:iterator>
       <%--${currentViewModel_}--%>
    </table>
</form>
