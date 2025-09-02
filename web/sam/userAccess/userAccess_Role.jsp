<%-- 
    Document   : userAccess_System
    Created on : Feb 18, 2014, 3:53:01 PM
    Author     : user
--%>

<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-dojo-tags" prefix="sx"%>
<sx:head parseContent="true" debug="false" />
<script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
<link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
<link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />
<script type="text/javascript" src="pages/scripts/common.js"></script>
<script type="text/javascript" src="pages/scripts/validation.jsp"></script>
<script type="text/javascript" src="pages/scripts/confirmation.js"></script>
<script language="javascript">
    
    var winPodH;
    function showApp(sUgId) {
        winPodH=dhtmlmodal.open("popup", "iframe", "loadAppPageUserAccess?UgId="+sUgId, "Senarai Aplikasi", "width=603px,height=400px,resize=1,scrolling=1,center=1", "");
    }
     function showSecurity(sUgId) { 
         
        winPodH=dhtmlmodal.open("popup", "iframe", "loadSecPageUserAccess?UgId="+sUgId, "Senarai Sekuriti", "width=600px,height=400px,resize=1,scrolling=1,center=1", "");
    }
    function showWF(sUgId) { 
         
        winPodH=dhtmlmodal.open("popup", "iframe", "loadWFPageUserAccess?UgId="+sUgId, "Senarai Kumpulan Pengesah", "width=600px,height=400px,resize=1,scrolling=1,center=1", "");
    }
    
      
    function localValidateForm(form, operation) {
        var errors = new Array();
        var sAdmin = document.getElementById("ids").value;
        var sAdminChecked = document.getElementById("ids").checked;
        var bExistDeptAdmin = document.getElementById("existDeptAdmin_").value;
        var bExistCentAdmin = document.getElementById("existCentralAdmin_").value;
        
        if (sAdmin == '${DEPAdmin}' ){
            if(bExistDeptAdmin !== "true" && sAdminChecked !== true) {
                  errors[errors.length] = "Tiada Department Admin yang lain dalam sistem." ;
//                 var answer = confirm("Tiada Dept Admin yang lain dalam jabatan anda. Pastikan anda pilih pengguna yang lain menjadi dept admin.Teruskan?");
            } 
        }
        
        if (sAdmin == '${HRMAdmin}' ){
            if(bExistCentAdmin !== "true" && sAdminChecked !== true) {
                  errors[errors.length] = "Tiada Central Admin yang lain dalam sistem." ;
            } 
        }

        if (errors.length > 0) {
            alert(errors.join('\n'));
            setFocus(form);
        }
        return errors.length > 0 ? false : true;
    }
    
</script>
<form method="post" id="prForm" class="prForm" action="processUpdateUserAccess" >
    <s:hidden theme="simple" name="action" />
    <s:hidden theme="simple" name="id" />
    <s:hidden theme="simple" name="model.ID" />
    <s:hidden theme="simple" name="model.emp_id" />
    <s:hidden theme="simple" name="currStep_" />
    <s:hidden theme="simple" name="model.UpdateSystemID" />
    <s:hidden theme="simple" name="existDeptAdmin_"/>
    <s:hidden theme="simple" name="existCentralAdmin_"/>
    <s:hidden theme="simple" name="model.differentDept_" />
    <s:hidden theme="simple" name="model.IsSains_Admin_" />
    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
        <tr>
            <td class="button-container"> 
                <div class="step">
                    <span class="header1">${currentStep_}</span>
                </div>
            </td>
            <td align="right" class="button-container">
                <s:submit type="submit" cssClass="defaultButton" theme="simple" action="prevStepUserAccess" value="%{getText('button.back')}"/>
                <%--<s:submit type="submit" cssClass="defaultButton buttonSave" theme="simple" action="processUpdateUserAccess" value="%{getText('button.save')}" title="" onclick="return confirmUpdate();"/>--%>
                <%--<s:submit type="submit" cssClass="defaultButton buttonSave" theme="simple" action="processUpdateUserAccess"  value="%{getText('button.save')}" title="" onclick="return confirmUpdateStatus();"/>--%>
                <s:submit type="submit" cssClass="defaultButton buttonSave" theme="simple" action="processUpdateUserAccess"  value="%{getText('button.save')}" title="" onclick="return localValidateForm(this.form, 'update')"/>
                <%--<s:submit type="submit" cssClass="defaultButton buttonSave" theme="simple" action="processUpdateUserAccess" value="%{getText('button.save')}" title=""/>--%>
                <%--<s:submit type="submit" cssClass="defaultButton buttonSave" theme="simple" action="processUpdateUserAccess" value="%{getText('button.save')}" title=""/>--%>
                <s:submit type="submit" cssClass="defaultButton buttonBackToList" theme="simple" action="cancelUserAccess" value="" title="%{getText('button.back')}"/>
                
            </td>
        </tr>
    </table>

    <table cellspacing="0" cellpadding="3" width="98%" border="0" id="userAccess_crumb" style="margin: 10px;">
        <tr class="sub_header_bg2"  ><th ><s:text name="userAccess.module"/></th><th width="40%"><s:text name="userAccess.module.app"/></th><th width="40%"><s:text name="userAccess.module.wf"/></th></tr>
        <s:iterator value="systemList_" id="system" status="systemStatus">
            <tr>
                <td valign="top" style="padding-bottom: 5px;padding-left: 5px;font-weight: bold;">${system.system_name}</td>
                <td valign="top">
                    <table cellpadding="0" cellspacing="0" width="100%" border="0" style="border: 0px;">
                        <s:iterator value="%{#system.setupGroupAppList}" id="groupApp" status="groupAppStatus">
                                <tr>
                                <td width="20px" valign="top"><s:checkbox theme="simple" name="selected" id="ids" fieldValue="%{#groupApp.ID}" value='%{#groupApp.hasAssign.equals("Y")?"True":"false"}' /></td>
                                <%--<td>${groupApp.group_name} --- ${groupApp.hasAssign}</td>--%>
                                <td>${groupApp.group_name}</td>
                                <td style="text-align: right;" width="25px"><a class="appButton" href="" onclick="showApp('${groupApp.ug_id}');return false;"><img src="images/action_icon/app_icon.jpg" title="<s:text name='userAccess.application'/>"/><%--<s:text name="userAccess.application"/>--%></a></td>
                                <td style="text-align: right;" width="25px"><a class="secButton" href="" onclick="showSecurity('${groupApp.ug_id}');return false;"><img src="images/action_icon/sec_icon.jpg" title="<s:text name='userAccess.security'/>"/><%--<s:text name="userAccess.security"/>--%></a></td>
                            </tr>
                        </s:iterator>
                    </table>
                </td>
                <td valign="top">
                    <table cellpadding="0" cellspacing="0" width="100%" border="0" style="border: 0px;">
                        <s:iterator value="%{#system.SetupGroupWFList}" id="groupWF" status="groupWFStatus">
                            <tr>
                                <td width="20px" valign="top"><s:checkbox theme="simple" name="selected" id="ids" fieldValue="%{#groupWF.ID}" value='%{#groupWF.hasAssign.equals("Y")?"True":"false"}' /></td>
                                <%--<td>${groupWF.group_name} --- ${groupWF.hasAssign}</td>--%>
                                <td >${groupWF.group_name}</td><%----%>
                                <td style="text-align: right;" width="25px"><a class="appButton" href="" onclick="showWF('${groupWF.ug_id}');return false;"><img src="images/action_icon/app_icon.jpg" title="<s:text name='userAccess.application'/>"/><%--<s:text name="userAccess.application"/>--%></a></td>
                                <td style="text-align: right;" width="25px"><a class="secButton" href="" onclick="showSecurity('${groupWF.ug_id}');return false;"><img src="images/action_icon/sec_icon.jpg" title="<s:text name='userAccess.security'/>"/><%--<s:text name="userAccess.security"/>--%></a></td>
                            </tr>
                        </s:iterator>
                    </table>
                </td>
            </tr>
        </s:iterator>
        <%--${currentViewModel_}---%>
    </table>
</form>
