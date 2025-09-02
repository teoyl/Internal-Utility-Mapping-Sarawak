<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <script language="Javascript">
            function validateSelect(form) {
                if (form.Application_selected == "undefined") {
                    alert(messageAtLeastOneItem);
                } else {
                    if (isCheckboxSelected(form.Application_selected)) {
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
                    <span class="titleActionTypeText"> | <s:text name="actionType.searchApplication" /></span><br>
                </div>
                <div class="xbox">-->
        <div class="panel panel-default">  
            <div class="panel-heading"><h4><s:text name="actionType.search"/>&nbsp;<s:property value='getDynamicSearchDescription("Application")'/></h4></div>
            <div class="panel-body">
                <form theme="simple" action="processSearchApplicationUserGroup" method="post">
                    <s:hidden name="action" />
                    <s:hidden name="searchCondition" />
                    <s:hidden name="ug_id" value="%{model.ug_id}" />
                    <s:hidden name="id" value="%{model.ug_id}" />
                    <s:hidden name="group_code" value="%{model.group_code}"/>
                    <s:hidden name="group_name" value="%{model.group_name}"/>
                    <s:hidden name="group_type" value="%{model.group_type}"/>
                    <s:iterator value="model.groupApplication" status="appStatus" var="userGroupApp">
                        <s:hidden name='groupApplication[%{#appStatus.index}].ID' value='%{#userGroupApp.ID}' />
                        <s:hidden name='groupApplication[%{#appStatus.index}].application.ID' value='%{application.ID}' />
                        <s:hidden name='groupApplication[%{#appStatus.index}].application.application_code' value='%{application.application_code}' />
                        <s:hidden name='groupApplication[%{#appStatus.index}].application.application_name' value='%{application.application_name}' />
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.create_right" value="%{application.create_right}"/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].create_right" id="create_ids" value='%{create_right}'/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.retrieve_right" value="%{application.retrieve_right}"/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].retrieve_right" id="retrieve_ids" value='%{retrieve_right}'/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.update_right" value="%{application.update_right}"/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].update_right" id="update_ids" value='%{update_right}'/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.delete_right" value="%{application.delete_right}"/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].delete_right" id="delete_ids" value='%{delete_right}'/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.print_right" value="%{application.print_right}"/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].print_right" id="print_ids" value='%{print_right}'/>
                        <s:if test="application.rightsList.size > 0">
                            <s:iterator value="application.rightsList" status="rightsStatus" var="appRights">
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRights_id" value='%{#appRights.gaRights_id}' />
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRightChecked" value='%{#appRights.gaRightChecked}'/>
                            </s:iterator>
                        </s:if>
                    </s:iterator>
                    <div class="row">
                        <div class="col-md-8">
                            <jsp:include page="/pages/base/dynamicSearch2.jsp"/>
                        </div>
                        <div class="col-md-4 text-right">
                            <%--<s:submit cssClass="defaultButton" theme="simple" value="Search"/>--%>
                            <button class="btn btn-primary" type="submit"><i class="fa fa-search"></i>Search</button>

                            <s:if test='model.ug_id != ""'>
                                <%--<s:submit theme="simple" cssClass="defaultButton" action="addApplicationEditUserGroup" value="Cancel"/>--%>
                                <button class="btn btn-default" type="submit" name="action:addApplicationEditUserGroup" id="addApplicationEditUserGroup"><i class="fa fa-close"></i>Cancel</button>

                            </s:if>
                            <s:else>
                                <%--<s:submit theme="simple" cssClass="defaultButton" action="addApplicationUserGroup" value="Cancel"/>--%>
                                <button class="btn btn-default" type="submit" name="action:addApplicationUserGroup" id="addApplicationUserGroup"><i class="fa fa-close"></i>Cancel</button>

                            </s:else>
                        </div>
                    </div>
                </form>

                <form id="sortForm" action="processSearchApplicationUserGroup" method="post">
                    <s:iterator value="getDynamicSearchField(#searchParam)" var="field" status="rowStatus">
                        <s:hidden theme="simple" name="search_%{#field}" value="%{getDynamicSearchData(#searchParam)[#rowStatus.index]}" />
                    </s:iterator>
                    <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
                    <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
                    <s:hidden theme="simple" name="action"/>
                    <s:if test='getDynamicResult("Application").size() > 0'>
                        <s:hidden theme="simple" name="listSize" value="1"/>
                    </s:if>
                    <s:else>
                        <s:hidden theme="simple" name="listSize" value="0"/>
                    </s:else>
                    <s:hidden name="searchCondition" />
                    <s:hidden name="ug_id" value="%{model.ug_id}" />
                    <s:hidden name="id" value="%{model.ug_id}" />
                    <s:hidden name="group_code" value="%{model.group_code}"/>
                    <s:hidden name="group_name" value="%{model.group_name}"/>
                    <s:hidden name="group_type" value="%{model.group_type}"/>
                    <s:iterator value="model.groupApplication" status="appStatus" var="userGroupApp">
                        <s:hidden name='groupApplication[%{#appStatus.index}].ID' value='%{#userGroupApp.ID}' />
                        <s:hidden name='groupApplication[%{#appStatus.index}].application.ID' value='%{application.ID}' />
                        <s:hidden name='groupApplication[%{#appStatus.index}].application.application_code' value='%{application.application_code}' />
                        <s:hidden name='groupApplication[%{#appStatus.index}].application.application_name' value='%{application.application_name}' />
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.create_right" value="%{application.create_right}"/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].create_right" id="create_ids" value='%{create_right}'/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.retrieve_right" value="%{application.retrieve_right}"/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].retrieve_right" id="retrieve_ids" value='%{retrieve_right}'/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.update_right" value="%{application.update_right}"/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].update_right" id="update_ids" value='%{update_right}'/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.delete_right" value="%{application.delete_right}"/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].delete_right" id="delete_ids" value='%{delete_right}'/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.print_right" value="%{application.print_right}"/>
                        <s:hidden name="groupApplication[%{#appStatus.index}].print_right" id="print_ids" value='%{print_right}'/>
                        <s:if test="application.rightsList.size > 0">
                            <s:iterator value="application.rightsList" status="rightsStatus" var="appRights">
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRights_id" value='%{#appRights.gaRights_id}' />
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRightChecked" value='%{#appRights.gaRightChecked}'/>
                            </s:iterator>
                        </s:if>
                    </s:iterator>
                </form>     <br/>       



                <form theme="simple" action='${model.ug_id != ""?"addApplicationEditUserGroup":"addApplicationUserGroup"}' method="post">
                    <s:hidden name="action" />
                    <s:hidden name="searchCondition" />                    
                    <div class="row form-row-margin">
                        <div class="col-md-12">
                            <s:if test='model.ug_id != ""'>
                                <button class="btn btn-default" type="submit" name="action:addApplicationEditUserGroup" id="addApplicationEditUserGroup" onclick="return validateSelect(form)" ><i class="fa fa-plus"></i><s:text name="button.add.selected"/></button>
                                <%--s:submit theme="simple" cssClass="defaultButton" action="addApplicationEditUserGroup" value="Add Selected"
                                                                  onclick="return validateSelect(form)" /--%>
                            </s:if>
                            <s:else>
                                <button class="btn btn-default" type="submit" name="action:addApplicationUserGroup" id="addApplicationUserGroup" onclick="return validateSelect(form)" ><i class="fa fa-plus"></i><s:text name="button.add.selected"/></button>
                                <%--s:submit theme="simple" cssClass="defaultButton" action="addApplicationUserGroup" value="Add Selected"
                                                                      onclick="return validateSelect(form)" /--%>
                            </s:else>
                        </div>
                    </div>    
                    <div class="row">
                        <div class="col-md-12">
                            <s:include value="/pages/base/dynamicList2.jsp"/>
                            <s:hidden name="searchCondition" />
                            <s:hidden name="ug_id" value="%{model.ug_id}" />
                            <s:hidden name="id" value="%{model.ug_id}" />
                            <s:hidden name="group_code" value="%{model.group_code}"/>
                            <s:hidden name="group_name" value="%{model.group_name}"/>
                            <s:hidden name="group_type" value="%{model.group_type}"/>
                            <s:iterator value="model.groupApplication" status="appStatus" var="userGroupApp">
                                <s:hidden name='groupApplication[%{#appStatus.index}].ID' value='%{#userGroupApp.ID}' />
                                <s:hidden name='groupApplication[%{#appStatus.index}].application.ID' value='%{application.ID}' />
                                <s:hidden name='groupApplication[%{#appStatus.index}].application.application_code' value='%{application.application_code}' />
                                <s:hidden name='groupApplication[%{#appStatus.index}].application.application_name' value='%{application.application_name}' />
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.create_right" value="%{application.create_right}"/>
                                <s:hidden name="groupApplication[%{#appStatus.index}].create_right" id="create_ids" value='%{create_right}'/>
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.retrieve_right" value="%{application.retrieve_right}"/>
                                <s:hidden name="groupApplication[%{#appStatus.index}].retrieve_right" id="retrieve_ids" value='%{retrieve_right}'/>
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.update_right" value="%{application.update_right}"/>
                                <s:hidden name="groupApplication[%{#appStatus.index}].update_right" id="update_ids" value='%{update_right}'/>
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.delete_right" value="%{application.delete_right}"/>
                                <s:hidden name="groupApplication[%{#appStatus.index}].delete_right" id="delete_ids" value='%{delete_right}'/>
                                <s:hidden name="groupApplication[%{#appStatus.index}].application.print_right" value="%{application.print_right}"/>
                                <s:hidden name="groupApplication[%{#appStatus.index}].print_right" id="print_ids" value='%{print_right}'/>
                                <s:if test="application.rightsList.size > 0">
                                    <s:iterator value="application.rightsList" status="rightsStatus" var="appRights">
                                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRights_id" id="ga_rights_ids" value='%{#appRights.gaRights_id}' />
                                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRightChecked" id="rights_ids" value='%{#appRights.gaRightChecked}'/>
                                    </s:iterator>
                                </s:if>
                            </s:iterator>
                        </div>
                    </div>
                </form>



                <%--table cellspacing="0" cellpadding="5" border="0" width="100%" class="form table borderless">
                    <form theme="simple" action="processSearchApplicationUserGroup" method="post">
                        <tr>
                            <td align="right">
                                <s:hidden name="action" />
                                <s:submit cssClass="defaultButton" theme="simple" value="Search"/>
                                <s:if test='model.ug_id != ""'>
                                    <s:submit theme="simple" cssClass="defaultButton" action="addApplicationEditUserGroup" value="Cancel"/>
                                </s:if>
                                <s:else>
                                    <s:submit theme="simple" cssClass="defaultButton" action="addApplicationUserGroup" value="Cancel"/>
                                </s:else>
                                <s:hidden name="searchCondition" />
                                <s:hidden name="ug_id" value="%{model.ug_id}" />
                                <s:hidden name="id" value="%{model.ug_id}" />
                                <s:hidden name="group_code" value="%{model.group_code}"/>
                                <s:hidden name="group_name" value="%{model.group_name}"/>
                                <s:hidden name="group_type" value="%{model.group_type}"/>
                                <s:hidden name="dept_id" value="%{model.dept_id}"/>
                                <s:hidden name="system_id" value="%{model.system_id}"/>
                                <s:iterator value="model.groupApplication" status="appStatus" id="userGroupApp">
                                    <s:hidden name='groupApplication[%{#appStatus.index}].ID' value='%{#userGroupApp.ID}' />
                                    <s:hidden name='groupApplication[%{#appStatus.index}].application.ID' value='%{application.ID}' />
                                    <s:hidden name='groupApplication[%{#appStatus.index}].application.application_code' value='%{application.application_code}' />
                                    <s:hidden name='groupApplication[%{#appStatus.index}].application.application_name' value='%{application.application_name}' />
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.create_right" value="%{application.create_right}"/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].create_right" id="create_ids" value='%{create_right}'/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.retrieve_right" value="%{application.retrieve_right}"/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].retrieve_right" id="retrieve_ids" value='%{retrieve_right}'/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.update_right" value="%{application.update_right}"/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].update_right" id="update_ids" value='%{update_right}'/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.delete_right" value="%{application.delete_right}"/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].delete_right" id="delete_ids" value='%{delete_right}'/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.print_right" value="%{application.print_right}"/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].print_right" id="print_ids" value='%{print_right}'/>
                                    <s:if test="application.rightsList.size > 0">
                                        <s:iterator value="application.rightsList" status="rightsStatus" id="appRights">
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRights_id" value='%{#appRights.gaRights_id}' />
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRightChecked" value='%{#appRights.gaRightChecked}'/>
                                        </s:iterator>
                                    </s:if>
                                </s:iterator>
                            </td>
                        </tr>
                        <tr><td colspan="2"><table class="table borderless">
                                    <s:include value="/pages/base/dynamicSearch2.jsp">
                                        <s:param name="searchingParam" value="'Application'" />
                                        <s:param name="functionParam" value="'processSearchApplicationUserGroup'" />
                                    </s:include>
                                </table></td></tr>
                    </form>
                    <!--                <s:include value="/pages/base/dynamicSearch2sortForm.jsp">
                                        <s:param name="searchingParam" value="'Application'" />
                                        <s:param name="functionParam" value="'processSearchApplicationUserGroup'" />
                                    </s:include>-->
                    <form id="sortForm" action="processSearchApplicationUserGroup" method="post">
                        <s:iterator value="getDynamicSearchField(#searchParam)" var="field" status="rowStatus">
                            <s:hidden theme="simple" name="search_%{#field}" value="%{getDynamicSearchData(#searchParam)[#rowStatus.index]}" />
                        </s:iterator>
                        <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
                        <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
                        <s:hidden theme="simple" name="action"/>
                        <s:if test='getDynamicResult("Application").size() > 0'>
                            <s:hidden theme="simple" name="listSize" value="1"/>
                        </s:if>
                        <s:else>
                            <s:hidden theme="simple" name="listSize" value="0"/>
                        </s:else>
                        <s:hidden name="searchCondition" />
                        <s:hidden name="ug_id" value="%{model.ug_id}" />
                        <s:hidden name="id" value="%{model.ug_id}" />
                        <s:hidden name="group_code" value="%{model.group_code}"/>
                        <s:hidden name="group_name" value="%{model.group_name}"/>
                        <s:hidden name="group_type" value="%{model.group_type}"/>
                        <s:hidden name="dept_id" value="%{model.dept_id}"/>
                        <s:hidden name="system_id" value="%{model.system_id}"/>
                        <s:iterator value="model.groupApplication" status="appStatus" id="userGroupApp">
                            <s:hidden name='groupApplication[%{#appStatus.index}].ID' value='%{#userGroupApp.ID}' />
                            <s:hidden name='groupApplication[%{#appStatus.index}].application.ID' value='%{application.ID}' />
                            <s:hidden name='groupApplication[%{#appStatus.index}].application.application_code' value='%{application.application_code}' />
                            <s:hidden name='groupApplication[%{#appStatus.index}].application.application_name' value='%{application.application_name}' />
                            <s:hidden name="groupApplication[%{#appStatus.index}].application.create_right" value="%{application.create_right}"/>
                            <s:hidden name="groupApplication[%{#appStatus.index}].create_right" id="create_ids" value='%{create_right}'/>
                            <s:hidden name="groupApplication[%{#appStatus.index}].application.retrieve_right" value="%{application.retrieve_right}"/>
                            <s:hidden name="groupApplication[%{#appStatus.index}].retrieve_right" id="retrieve_ids" value='%{retrieve_right}'/>
                            <s:hidden name="groupApplication[%{#appStatus.index}].application.update_right" value="%{application.update_right}"/>
                            <s:hidden name="groupApplication[%{#appStatus.index}].update_right" id="update_ids" value='%{update_right}'/>
                            <s:hidden name="groupApplication[%{#appStatus.index}].application.delete_right" value="%{application.delete_right}"/>
                            <s:hidden name="groupApplication[%{#appStatus.index}].delete_right" id="delete_ids" value='%{delete_right}'/>
                            <s:hidden name="groupApplication[%{#appStatus.index}].application.print_right" value="%{application.print_right}"/>
                            <s:hidden name="groupApplication[%{#appStatus.index}].print_right" id="print_ids" value='%{print_right}'/>
                            <s:if test="application.rightsList.size > 0">
                                <s:iterator value="application.rightsList" status="rightsStatus" id="appRights">
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRights_id" value='%{#appRights.gaRights_id}' />
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRightChecked" value='%{#appRights.gaRightChecked}'/>
                                </s:iterator>
                            </s:if>
                        </s:iterator>
                    </form>
                </table->





                <table class="wwFormTable table borderless" border="0" width="100%">
                    <form theme="simple" action='${model.ug_id != ""?"addApplicationEditUserGroup":"addApplicationUserGroup"}' method="post">
                        <s:hidden theme="simple" name="action" />
                        <tr>
                            <td colspan="2">
                                <s:if test='model.ug_id != ""'>
                                    <s:submit theme="simple" cssClass="defaultButton" action="addApplicationEditUserGroup" value="Add Selected"
                                              onclick="return validateSelect(form)" />
                                </s:if>
                                <s:else>
                                    <s:submit theme="simple" cssClass="defaultButton" action="addApplicationUserGroup" value="Add Selected"
                                              onclick="return validateSelect(form)" />
                                </s:else>
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <s:include value="/pages/base/dynamicList2.jsp">
                                    <s:param name="listingParam" value="'Application'" />
                                </s:include>
                                <s:hidden name="searchCondition" />
                                <s:hidden name="ug_id" value="%{model.ug_id}" />
                                <s:hidden name="id" value="%{model.ug_id}" />
                                <s:hidden name="group_code" value="%{model.group_code}"/>
                                <s:hidden name="group_name" value="%{model.group_name}"/>
                                <s:hidden name="group_type" value="%{model.group_type}"/>
                                <s:hidden name="dept_id" value="%{model.dept_id}"/>
                                <s:hidden name="system_id" value="%{model.system_id}"/>
                                <s:iterator value="model.groupApplication" status="appStatus" id="userGroupApp">
                                    <s:hidden name='groupApplication[%{#appStatus.index}].ID' value='%{#userGroupApp.ID}' />
                                    <s:hidden name='groupApplication[%{#appStatus.index}].application.ID' value='%{application.ID}' />
                                    <s:hidden name='groupApplication[%{#appStatus.index}].application.application_code' value='%{application.application_code}' />
                                    <s:hidden name='groupApplication[%{#appStatus.index}].application.application_name' value='%{application.application_name}' />
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.create_right" value="%{application.create_right}"/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].create_right" id="create_ids" value='%{create_right}'/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.retrieve_right" value="%{application.retrieve_right}"/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].retrieve_right" id="retrieve_ids" value='%{retrieve_right}'/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.update_right" value="%{application.update_right}"/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].update_right" id="update_ids" value='%{update_right}'/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.delete_right" value="%{application.delete_right}"/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].delete_right" id="delete_ids" value='%{delete_right}'/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.print_right" value="%{application.print_right}"/>
                                    <s:hidden name="groupApplication[%{#appStatus.index}].print_right" id="print_ids" value='%{print_right}'/>
                                    <s:if test="application.rightsList.size > 0">
                                        <s:iterator value="application.rightsList" status="rightsStatus" id="appRights">
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRights_id" id="ga_rights_ids" value='%{#appRights.gaRights_id}' />
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRightChecked" id="rights_ids" value='%{#appRights.gaRightChecked}'/>
                                        </s:iterator>
                                    </s:if>
                                </s:iterator>
                            </td>
                        </tr>
                    </form>
                </table--%>
            </div>
        </div>
    </body>
</html>