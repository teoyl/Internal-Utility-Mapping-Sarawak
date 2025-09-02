<%-- 
    Document   : userAccess_navigator
    Created on : Feb 18, 2014, 3:38:53 PM
    Author     : Delvene
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        
        <s:set name="secuLevel_Sarawak"><%=com.sains.common.util.SystemConstants.ACCESS_TYPE.Sarawak%></s:set>
        <s:set name="secuLevel_Department"><%=com.sains.common.util.SystemConstants.ACCESS_TYPE.Department%></s:set>
        <s:set name="secuLevel_Unit"><%=com.sains.common.util.SystemConstants.ACCESS_TYPE.Unit%></s:set>
        <%--<s:set name="Y"><%= //com.sains.common.util.SystemConstants.USER_ACC_STATUS.ACTIVE%></s:set>--%>
        <%--<s:set name="I"><%= //com.sains.common.util.SystemConstants.USER_ACC_STATUS.INACTIVE%></s:set>--%>
        <%--<s:set name="L"><%= //com.sains.common.util.SystemConstants.USER_ACC_STATUS.LOCKED%></s:set>--%>
        <%--<s:set name="N"><%= //com.sains.common.util.SystemConstants.USER_ACC_STATUS.NEW%></s:set>--%>
        <s:set name="userActive" value="@com.sains.common.util.SystemConstants$USER_ACC_STATUS@ACTIVE"></s:set>
        <s:set name="userInactive" value="@com.sains.common.util.SystemConstants$USER_ACC_STATUS@INACTIVE"></s:set>
        <s:set name="userLock" value="@com.sains.common.util.SystemConstants$USER_ACC_STATUS@LOCKED"></s:set>
        <s:set name="DEPAdmin"><%=com.sains.common.util.SystemConstants.USER_GROUP_CODE.DEPAdmin %></s:set>
        <s:set name="HRMAdmin"><%=com.sains.common.util.SystemConstants.USER_GROUP_CODE.HRMAdmin %></s:set>
        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />
        
        
        <%--<jsp:include page="/pr/pr_js.jsp"></jsp:include>--%>
    </head>
    <body>
        <div class="titleFramework">
            <span class="titleText"><s:text name="userAccess" /></span>
            <span class="titleActionTypeText"> | <s:text name="actionType.edit" /></span>
            <br>
        </div>
        <div class="xbox">
            <%--Navigation Steps - END--%>
<%--            <div class="step">
                <span class="header1">${currentStep_}</span>
            </div>--%>
            <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
            <table cellspacing="0" cellpadding="2" border="0" width="98%" class="master-info" style="margin: 10px;" align="center">
                <tr>
                    <td>  
                        <table >
                        <!--<table cellspacing="0" cellpadding="2" border="0" width="98%" class="master-info" style="margin: 10px;" align="center">-->
                            <s:hidden theme="simple" name="empModel.ID" />
                            <tr>
                                <td width="20px"></td>
                                <td class="label_imp" width="210px"><s:text name="PR.personal.perName"/></td>
                                <td width="10px" class="label_imp">:</td>
                                <td>
                                  <%--  <s:if test="empModel.emp_title != null && empModel.emp_title != ''">
                                        <span style="text-transform: uppercase">${empModel.emp_titleModel.code_desc}</span>
                                    </s:if> --%>
                                    ${empModel.emp_name}
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td class="label_imp"><s:text name="PR.personal.perNewICNo"/></td>
                                <td class="label_imp">:</td>
                                <td>${empModel.emp_nw_ic_no} </td>
                            </tr>
                           <tr>
                                <td></td>
                                <td class="label_imp"><s:text name="user.id"/></td>
                                <td class="label_imp">:</td>
                                <td>${model.us_user_id} </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td class="label_imp" valign="top"><s:text name="userAccess.secuGroup.dept"/></td>
                                <td class="label_imp" valign="top">:</td>
                                <td align="left"><%--${empModel.personalPostList[0].postOperation.postInfoModel.establishment_ba.topEst.department.dept_name}<br/>--%>
                                                ${empModel.personalPostActiveList[0].postOperation.establishment_bu.department.dept_name}<br>
                                                ${empModel.personalPostActiveList[0].postOperation.establishment_bu.noAndDesc}
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td class="label_imp"><s:text name="PR.account.accStatus"/></td>
                                <td class="label_imp">:</td>

                                <td align="left"><s:text name="userAccess.accStatus.%{model.us_status}"/></td>

                            </tr>
            <!--                <tr>
                                <td height="10px" class="button-container-list" colspan="4">some space</td>
                            </tr>-->
                        </table>
                    </td>
                    <td width=135x" valign="top" style="padding-right: 5px;">
                        <%--<s:if test="currStep_== '1'">
                        <s:if test='currStep_== "1"'>
                             <s:submit type="button" cssClass="defaultButton buttonUpdate" theme="simple" value="%{getText('button.update.user')}" onclick="showUserEdit('%{model.us_id}')"/>
                        </s:if>--%>
                    </td>
                </tr>
            </table>
            <jsp:include page="/sam/userAccess/${currentPage_}.jsp"></jsp:include>
        </div>
    </body>
</html>