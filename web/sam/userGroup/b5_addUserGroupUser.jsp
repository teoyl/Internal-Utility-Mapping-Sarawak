<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script language="Javascript">
            function validateSelect(form) {
                if (form.User_selected == "undefined") {
                    alert(messageAtLeastOneItem);
                } else {
                    if (isCheckboxSelected(form.User_selected)) {
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

        <div class="card">  
            <div class="card-header bg-light"><h5><s:text name="actionType.search"/></h5></div>
            <div class="card-body">
                <form action="processSearchUserUserGroup" method="post">
                    <s:hidden name="action" />
                    <div class="row">
                        <!--left box-->
                        <div class="col-md-8">
                            <s:include value="/pages/base/b5_dynamicSearch2.jsp"/>
                            <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
                            <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
                        </div>

                        <!--right box-->
                        <div class="col-md-4 text-end">
                            <button class="btn btn-sm btn-primary" type="submit"><i class="fa fa-search"></i> <span class="ms-1"><s:text name="actionType.search"/></span></button>
                            <s:if test='model.ug_id != ""'>
                                <button class="btn btn-sm btn-falcon-default" type="submit" name="action:addUserEditUserGroup" id="addUserEditUserGroup"><i class="fa fa-times"></i> <span class="ms-1"><s:text name="button.cancel"/></span></button>
                                <%--<s:submit cssClass="defaultButton" theme="simple" action="addUserEditUserGroup" value="Cancel"/>--%>
                            </s:if>
                            <s:else>
                                <button class="btn btn-sm btn-falcon-default" type="submit" name="action:addUserUserGroup" id="addUserUserGroup"><i class="fa fa-times"></i><s:text name="button.cancel"/></button>
                                <%--<s:submit cssClass="defaultButton" theme="simple" action="addUserUserGroup" value="Cancel"/>--%>
                            </s:else>
                        </div>
                </form>
            </div><hr/>
            <s:include value="/pages/base/dynamicSearch2sortForm.jsp">
                <s:param name="searchingParam" value="'User'" />
                <s:param name="functionParam" value="'processSearchUserUserGroup'" />
            </s:include>
            <form theme="simple" action="${model.ug_id != ''?'addUserEditUserGroup':'addUserUserGroup'}">
                <div class="row form-row-margin">
                    <div class="col-md-12">
                        <s:if test="model.ug_id != ''">
                            <button class="btn btn-sm btn-falcon-default" type="submit" name="action:addUserEditUserGroup" id="addUserEditUserGroup" onclick="return validateSelect(form)" ><i class="fa fa-plus"></i> <span class="ms-1"><s:text name="button.add.selected"/></span></button>
                            <%--s:submit cssClass="defaultButton" theme="simple" action="addUserEditUserGroup" value="Add Selected"
                                      onclick="return validateSelect(form)" /--%>
                        </s:if>
                        <s:else>
                            <button class="btn btn-sm btn-falcon-default" type="submit" name="action:addUserUserGroup" id="addUserUserGroup" onclick="return validateSelect(form)" ><i class="fa fa-plus"></i> <span class="ms-1"><s:text name="button.add.selected"/></span></button>
                            <%--s:submit cssClass="defaultButton" theme="simple" action="addUserUserGroup" value="Add Selected"
                                      onclick="return validateSelect(form)" /--%>
                        </s:else>
                    </div>
                </div>
                <div class="row mt-2">
                    <s:hidden theme="simple" name="action" />
                    <s:hidden theme="simple" name="ID" value="%{model.ug_id}"/>

                    <s:include value="/pages/base/b5_dynamicList2.jsp"/>
                </div>
            </form>
        </div>
    </div>



    <%--div class="panel panel-default ">  
        <div class="panel-heading">
            <h3 class="panel-title"> 
                <span class="titleText"><s:text name="userGroup" /></span>
                <span class="titleActionTypeText"> | <s:text name="actionType.searchUser" /></span></h3>
        </div>
        <!--<div class="titleFramework">
            <span class="titleText"><s:text name="userGroup" /></span>
            <span class="titleActionTypeText"> | <s:text name="actionType.searchUser" /></span><br>
        </div>-->
        <!--<div class="xbox">-->
        <div class="panel-body">

            <table cellspacing="0" cellpadding="5" border="0" width="100%" class="form">
                <form action="processSearchUserUserGroup" method="post">
                    <tr  >
                        <td align="right">
                            <s:hidden name="action" />
                            <s:submit cssClass="defaultButton" theme="simple" value="Search"/>
                            <s:if test='model.ug_id != ""'>
                                <s:submit cssClass="defaultButton" theme="simple" action="addUserEditUserGroup" value="Cancel"/>
                            </s:if>
                            <s:else>
                                <s:submit cssClass="defaultButton" theme="simple" action="addUserUserGroup" value="Cancel"/>
                            </s:else>
                        </td>
                    </tr>
                    <tr><td colspan="2"><table>
                                <s:include value="/pages/base/dynamicSearch2.jsp">
                                    <s:param name="searchingParam" value="'User'" />
                                    <s:param name="functionParam" value="'processSearchUserUserGroup'" />
                                </s:include>
                                <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
                                <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
                            </table></td></tr>
                </form>
            </table>
            <s:include value="/pages/base/dynamicSearch2sortForm.jsp">
                <s:param name="searchingParam" value="'User'" />
                <s:param name="functionParam" value="'processSearchUserUserGroup'" />
            </s:include>

            <table class="wwFormTable" border="0" width="100%">
                <form theme="simple" action="${model.ug_id != ''?'addUserEditUserGroup':'addUserUserGroup'}">
                    <s:hidden theme="simple" name="action" />
                    <s:hidden theme="simple" name="ID" value="%{model.ug_id}"/>
                    <tr>
                        <td colspan="2">
                            <s:if test="model.ug_id != ''">
                                <s:submit cssClass="defaultButton" theme="simple" action="addUserEditUserGroup" value="Add Selected"
                                          onclick="return validateSelect(form)" />
                            </s:if>
                            <s:else>
                                <s:submit cssClass="defaultButton" theme="simple" action="addUserUserGroup" value="Add Selected"
                                          onclick="return validateSelect(form)" />
                            </s:else>
                        </td>
                        <td></td>
                    </tr>
                    <tr>
                        <td colspan="3">
                            <s:include value="/pages/base/dynamicList2.jsp">
                                <s:param name="listingParam" value="'User'" />
                            </s:include>
                        </td>
                    </tr>
                </form>
            </table>
        </div--%>
</body>
</html>