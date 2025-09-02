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
                if (form.UserGroup_selected == "undefined") {
                    alert(messageAtLeastOneItem);
                } else {
                    if (isCheckboxSelected(form.UserGroup_selected)) {
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
                    <span class="titleText">Public User Group</span>
                    <span class="titleActionTypeText"> | <s:text name="actionType.search" /></span><br>
                </div>
                <div class="xbox">-->
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4 class="panel-title">Search</h4>
            </div>
            <div class="panel-body">

                <form action="processSearchUGPublicUser_" >
                    <s:hidden name="action" />
                    <div class="row">
                        <div class="col-md-8">
                            <s:include value="/pages/base/dynamicSearch2.jsp">
                                <s:param name="searchingParam" value="'UserGroup'" />
                                <s:param name="functionParam" value="'processSearchUGPublicUsr'" />
                            </s:include>
                            <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
                            <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />   
                        </div>
                        <div class="col-md-4 text-right">
                            <%--<s:submit cssClass="defaultButton btn mrg-lr-5" theme="simple" value="Search"/>--%>
                            <%--<s:submit cssClass="defaultButton btn mrg-lr-5" theme="simple" action="addGroupCancelPublicUser_" value="Cancel"/>--%>
                            <button class="btn btn-primary" type="submit"><i class="fa fa-search"></i>Search</button>
                            <button class="btn btn-default" type="submit" name="action:addGroupCancelPublicUser_" id="addGroupCancelPublicUser_"><i class="fa fa-close"></i>Cancel</button>
                        </div>
                    </div>
                </form>
                <s:include value="/pages/base/dynamicSearch2sortForm.jsp">
                    <s:param name="searchingParam" value="'UserGroup'" />
                    <s:param name="functionParam" value="'processSearchUGPublicUsr'" />
                </s:include>

                <hr>
                <form action="">
                    <s:hidden theme="simple" name="action" />
                    <div class="row form-row-margin">
                        <div class="col-md-12">
                            <button class="btn btn-default" type="submit" name="action:addGroupPublicUser_" id="addGroupPublicUser_" onclick="return validateSelect(form)" ><i class="fa fa-plus"></i>Add Selected</button>
                            <%--s:submit cssClass="defaultButton btn mrg-lr-5" theme="simple" action="addGroupPublicUser_" value="Add Selected"
                                      onclick="return validateSelect(form)" /--%>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <s:include value="/pages/base/dynamicList2.jsp">
                                <s:param name="listingParam" value="'UserGroup'" />
                            </s:include>
                        </div>
                    </div>
                </form>
            </div>
        </div>





        <%--div class="panel panel-default ">
            <div class="panel-heading ">
                <h3 class="panel-title"> 
                    <span class="titleText">Public User Group</span>
                    <span class="titleActionTypeText">
                        |<s:text name="actionType.search" />
                    </span>
                </h3>
            </div>
            <div class="panel-body">
                <table cellspacing="0" cellpadding="5" border="0" width="100%" class="table borderless">
                    <!--<form action="processSearchUGPublicUsr" >-->
                    <form action="processSearchUGPublicUser_" >
                        <tr>
                            <td width="30%" align="right">
                                <s:hidden name="action" />
                                <s:submit cssClass="defaultButton btn mrg-lr-5" theme="simple" value="Search"/>
                                <!--<s:submit cssClass="defaultButton btn mrg-lr-5" theme="simple" action="addGroupCancelPublicUsr" value="Cancel"/>-->
                                <s:submit cssClass="defaultButton btn mrg-lr-5" theme="simple" action="addGroupCancelPublicUser_" value="Cancel"/>
                            </td>
                        </tr>
                        <tr><td colspan="2"><table class="table borderless">
                                    <s:include value="/pages/base/dynamicSearch2.jsp">
                                        <s:param name="searchingParam" value="'UserGroup'" />
                                        <s:param name="functionParam" value="'processSearchUGPublicUsr'" />
                                    </s:include>
                                    <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
                                    <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />                
                                </table></td></tr>
                    </form>
                </table>
                <s:include value="/pages/base/dynamicSearch2sortForm.jsp">
                    <s:param name="searchingParam" value="'UserGroup'" />
                    <s:param name="functionParam" value="'processSearchUGPublicUsr'" />
                </s:include>

                <table class="wwFormTable table borderless" border="0" width="100%">
                    <form action="">
                        <s:hidden theme="simple" name="action" />
                        <tr><td><hr/></td></tr>
                        <tr>
                            <td colspan="3">
                                <!--<s:submit cssClass="defaultButton btn mrg-lr-5" theme="simple" action="addGroupPublicUsr" value="Add Selected"-->
                                <s:submit cssClass="defaultButton btn mrg-lr-5" theme="simple" action="addGroupPublicUser_" value="Add Selected"
                                          onclick="return validateSelect(form)" />
                            </td>
                        </tr>                
                        <tr>
                            <td colspan="3">
                                <div >
                                    <s:include value="/pages/base/dynamicList2.jsp">
                                        <s:param name="listingParam" value="'UserGroup'" />
                                    </s:include>
                                </div>
                            </td>
                        </tr>
                    </form>
                </table>
            </div>
        </div--%>
    </body>
</html>