<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <script language="Javascript">
            function validateSelect(form){
                if (form.UserGroupToApplication_selected == "undefined"){
                    alert(messageAtLeastOneItem);
                } else {
                    if ( isCheckboxSelected(form.UserGroupToApplication_selected)) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        </script>
        <%--<s:head />--%>
    </head>
    <body>
<!--        <div class="titleFramework">
            <span class="titleText"><s:text name="userGroup" /></span>
            <span class="titleActionTypeText"> | <s:text name="actionType.search" /></span><br>
        </div>
        <div class="xbox">-->
        <div class="panel panel-default ">  
             <div class="panel-heading">
                <h3 class="panel-title"> 
                    <span class="titleText"><s:text name="userGroup" /></span>
                <span class="titleActionTypeText"> | <s:text name="actionType.search" /></span></h3>
              </div>
             <div class="panel-body">
            <table cellspacing="0" cellpadding="5" border="0" width="100%" class="form borderless table">
                <form theme="simple" action="processSearchGroupApplication" method="post">
                    <tr>
                        <td align="right">
                            <s:hidden name="action" />
                            <s:hidden name="curGroupId_" />   <%-- ThoTH @ 22-Apr-2015 --%>
                            <s:submit cssClass="defaultButton" theme="simple" value="Search"/>
                            <s:submit theme="simple" cssClass="defaultButton" action="assignToGroupApplication" value="Cancel"/>
                            <s:hidden name="searchCondition" />
                            <s:hidden name="application_id" value="%{model.application_id}" />
                            <s:hidden name="id" value="%{model.application_id}" />
                            <s:hidden name="application_code" value="%{model.application_code}"/>
                            <s:hidden name="application_name" value="%{model.application_name}"/>
                            <s:hidden name="model.create_right" value="%{model.create_right}"/>
                            <s:hidden name="model.retrieve_right" value="%{model.retrieve_right}"/>
                            <s:hidden name="model.update_right" value="%{model.update_right}"/>
                            <s:hidden name="model.delete_right" value="%{model.delete_right}"/>
                            <s:hidden name="model.print_right" value="%{model.print_right}"/>
                            <s:iterator value="model.rightsList" status="rightsStatus" var="appRights">
                                <s:hidden name="model.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                <s:hidden name="model.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                                <s:hidden name="model.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                            </s:iterator>
                            <s:iterator value="model.groupAppList" status="appStatus" var="userGroupApp">
                                <s:hidden name='model.groupAppList[%{#appStatus.index}].ID' value='%{#userGroupApp.ID}' />
                                <s:hidden name='model.groupAppList[%{#appStatus.index}].group.ID' value='%{#userGroupApp.group.ID}' />
                                <s:hidden name='model.groupAppList[%{#appStatus.index}].group.group_code' value='%{#userGroupApp.group.group_code}' />
                                <s:hidden name='model.groupAppList[%{#appStatus.index}].group.group_name' value='%{#userGroupApp.group.group_name}' />
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].create_right" id="create_ids" value='%{#userGroupApp.create_right}'/>
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].retrieve_right" id="retrieve_ids" value='%{#userGroupApp.retrieve_right}'/>
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].update_right" id="update_ids" value='%{#userGroupApp.update_right}'/>
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].delete_right" id="delete_ids" value='%{#userGroupApp.delete_right}'/>
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].print_right" id="print_ids" value='%{#userGroupApp.print_right}'/>
                                <%--<s:if test="model.rightsList.size > 0">--%>
                                    <s:iterator value="%{#userGroupApp.groupAppRights}" status="rightsStatus" var="appRights">
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.app_rights_code" value="%{#appRights.applicationRights.app_rights_code}" />
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.app_rights_description" value="%{#appRights.applicationRights.app_rights_description}" />
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].app_rights_id" value='%{#appRights.app_rights_id}' />
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].hasRight" value='%{#appRights.hasRight}'/>
                                    </s:iterator>
                                <%--</s:if>--%>
                            </s:iterator>
                        </td>
                    </tr>
                    <tr><td colspan="2"><table class="table borderless">
                                <jsp:include page="/pages/base/dynamicSearch2.jsp"/>
                            </table></td></tr>
                </form>
<%--                <s:include value="/pages/base/dynamicSearch2sortForm.jsp">
                    <s:param name="searchingParam" value="'Application'" />
                    <s:param name="functionParam" value="'processSearchApplicationUserGroup'" />
                </s:include>--%>
<%-- ThoTH @ 23-Apr-2015 :: Don't know why need repeat this, so comment first --%>
                <%--<form id="sortForm" action="processSearchGroupApplication" method="post">
                    <s:iterator value="getDynamicSearchField(#searchParam)" var="field" status="rowStatus">
                        <s:hidden theme="simple" name="search_%{#field}" value="%{getDynamicSearchData(#searchParam)[#rowStatus.index]}" />
                    </s:iterator>
                    <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
                    <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
                    <s:hidden theme="simple" name="action"/>
                    <s:if test='getDynamicResult("UserGroupToApplication").size() > 0'>
                        <s:hidden theme="simple" name="listSize" value="1"/>
                    </s:if>
                    <s:else>
                        <s:hidden theme="simple" name="listSize" value="0"/>
                    </s:else>
                    <s:hidden name="action" />
                            <s:hidden name="curGroupId_" />   
                            <s:hidden name="searchCondition" />
                            <s:hidden name="application_id" value="%{model.application_id}" />
                            <s:hidden name="id" value="%{model.application_id}" />
                            <s:hidden name="application_code" value="%{model.application_code}"/>
                            <s:hidden name="application_name" value="%{model.application_name}"/>
                            <s:hidden name="model.create_right" value="%{model.create_right}"/>
                            <s:hidden name="model.retrieve_right" value="%{model.retrieve_right}"/>
                            <s:hidden name="model.update_right" value="%{model.update_right}"/>
                            <s:hidden name="model.delete_right" value="%{model.delete_right}"/>
                            <s:hidden name="model.print_right" value="%{model.print_right}"/>
                            <s:iterator value="model.groupAppList" status="appStatus" id="userGroupApp">
                                <s:hidden name='model.groupAppList[%{#appStatus.index}].ID' value='%{#userGroupApp.ID}' />
                                <s:hidden name='model.groupAppList[%{#appStatus.index}].group.ID' value='%{#userGroupApp.group.ID}' />
                                <s:hidden name='model.groupAppList[%{#appStatus.index}].group.group_code' value='%{#userGroupApp.group.group_code}' />
                                <s:hidden name='model.groupAppList[%{#appStatus.index}].group.group_name' value='%{#userGroupApp.group.group_name}' />
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].create_right" id="create_ids" value='%{#userGroupApp.create_right}'/>
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].retrieve_right" id="retrieve_ids" value='%{#userGroupApp.retrieve_right}'/>
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].update_right" id="update_ids" value='%{#userGroupApp.update_right}'/>
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].delete_right" id="delete_ids" value='%{#userGroupApp.delete_right}'/>
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].print_right" id="print_ids" value='%{#userGroupApp.print_right}'/>
                                <s:if test="model.rightsList.size > 0">
                                    <s:iterator value="model.rightsList" status="rightsStatus" id="appRights">
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.app_rights_code" value="%{#appRights.app_rights_code}" />
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.app_rights_description" value="%{#appRights.app_rights_description}" />
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.ID" value="%{#appRights.ID}" />
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.gaRights_id" value='%{#appRights.gaRights_id}' />
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.gaRightChecked" value='%{#appRights.gaRightChecked}'/>
                                    </s:iterator>
                                </s:if>
                            </s:iterator>
                </form>--%>
            </table>
            <table class="wwFormTable table borderless" border="0" width="100%">
                <form theme="simple" action='addGroupEditApplication' method="post">
                    <tr>
                        <td colspan="2">
                            <s:submit theme="simple" cssClass="defaultButton" action="addGroupEditApplication" value="Add Selected"
                                      onclick="return validateSelect(form)" />
                        </td>
                        <td></td>
                    </tr>
                    <tr>
                        <td colspan="3">
                            <jsp:include page="/pages/base/dynamicList2.jsp"/>
                            <s:hidden name="action" />
                            <s:hidden name="curGroupId_" />   <%-- ThoTH @ 22-Apr-2015 --%>
                            <s:hidden name="searchCondition" />
                            <s:hidden name="model.application_id" value="%{model.application_id}" />
                            <s:hidden name="id" value="%{model.application_id}" />
                            <s:hidden name="model.application_code" value="%{model.application_code}"/>
                            <s:hidden name="model.application_name" value="%{model.application_name}"/>
                            <s:hidden name="model.create_right" value="%{model.create_right}"/>
                            <s:hidden name="model.retrieve_right" value="%{model.retrieve_right}"/>
                            <s:hidden name="model.update_right" value="%{model.update_right}"/>
                            <s:hidden name="model.delete_right" value="%{model.delete_right}"/>
                            <s:hidden name="model.print_right" value="%{model.print_right}"/>
                            <s:iterator value="model.rightsList" status="rightsStatus" var="appRights">
                                <s:hidden name="model.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                <s:hidden name="model.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                                <s:hidden name="model.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                            </s:iterator>
                            <s:iterator value="model.groupAppList" status="appStatus" var="userGroupApp">
                                <s:hidden name='model.groupAppList[%{#appStatus.index}].ID' value='%{#userGroupApp.ID}' />
                                <s:hidden name='model.groupAppList[%{#appStatus.index}].group.ID' value='%{#userGroupApp.group.ID}' />
                                <s:hidden name='model.groupAppList[%{#appStatus.index}].group.group_code' value='%{#userGroupApp.group.group_code}' />
                                <s:hidden name='model.groupAppList[%{#appStatus.index}].group.group_name' value='%{#userGroupApp.group.group_name}' />
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].create_right" id="create_ids" value='%{#userGroupApp.create_right}'/>
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].retrieve_right" id="retrieve_ids" value='%{#userGroupApp.retrieve_right}'/>
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].update_right" id="update_ids" value='%{#userGroupApp.update_right}'/>
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].delete_right" id="delete_ids" value='%{#userGroupApp.delete_right}'/>
                                <s:hidden name="model.groupAppList[%{#appStatus.index}].print_right" id="print_ids" value='%{#userGroupApp.print_right}'/>
                                <%--<s:if test="model.rightsList.size > 0">--%>
                                     <s:iterator value="%{#userGroupApp.groupAppRights}" status="rightsStatus" var="appRights">
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.app_rights_code" value="%{#appRights.applicationRights.app_rights_code}" />
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.app_rights_description" value="%{#appRights.applicationRights.app_rights_description}" />
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].app_rights_id" value='%{#appRights.app_rights_id}' />
                                        <s:hidden name="model.groupAppList[%{#appStatus.index}].groupAppRights[%{#rightsStatus.index}].hasRight" value='%{#appRights.hasRight}'/>
                                    </s:iterator>
                                <%--</s:if>--%>
                            </s:iterator>
                        </td>
                    </tr>
                </form>
            </table>
        </div>
    </body>
</html>