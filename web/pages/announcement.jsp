<%-- 
    Document   : announcement
    Created on : Jun 26, 2014, 2:32:57 PM
    Author     : Delvene
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<!--serenechye @ 1/8/2014 :: support page header -->
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>

<script type="text/javascript" src="<s:url value="/include/CurrentDateTime.js"/>"></script>
<html>
    <head>
        <style type="text/css">
             @import url(styles/impian_style_internal_annoucement.css);
        </style>
    </head>
    <body>
        <table style="height: 100%" width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
            <form id="loginForm" theme="simple" action="cancelLogin" method="post" enctype="multipart/form-data">
<!--                <tr>
                    <td align="center" height="90px" id="banner_imp"><jsp:include page="/main/internalMainHeader_1.jsp" /></td>
                </tr>-->
                  <%--Added by Serene @ 30-July-2014 :: To apply background of header- END--%>
                <td align="center" height="90px" id="banner_imp" >
                    <s:if test='systemType_.equals("ESS")'>
                        <page:applyDecorator page="/main/internalMainHeader_1.jsp" name="panel1" />
                    </s:if>
                    <s:else>
                         <page:applyDecorator page="/main/internalMainHeader.jsp" name="panel1" />
                    </s:else>
                </td>
<!--                <tr>
                    <td align="center" id="menubar_imp" class="title" height="35px"><h2><s:text name="announcement" /></h2>
                    <div id="dateDiv"></div>
                    </td>
                </tr>-->
                <tr>
                    <td style="border-top: 3px solid #A2C4C9;">&nbsp;</td>
                </tr>
                <tr>
                    <td height="15px"><%--some space--%></td>
                </tr>
                <tr>
                    <td align="center" valign="top" height="585px">
                        <table width="1000px" border="0" cellpadding="2" cellspacing="2"  id="announceArchive">
                            <s:hidden theme="simple" name="archive_" />
                            <tr>
                                <td colspan="2" id="announceHeader"><s:text name="announcement" /></td>
                            </tr>
<!--                            <tr>
                                <td height="10px"><%--some space--%></td>
                            </tr>-->
                            <tr id="announceSubHeader">
                                <td  class="announceTitle"><s:property value="%{announceModel_.announce_title}"/> </td>
                               <td id="announceHeader" class="announceContinue" style="font-size: 8pt"><a href="javascript: window.history.go(-1)"> Kembali </a></td>
                            </tr>
                            <tr>
                                <td class="label_imp"><s:property value="%{announceModel_.announce_prepare_date_str}"/></td>
                                <td align="right" class="announceContinue">
                                    <%--<a href="javascript: window.history.go(-1)">Kembali</a>
                                    comment by sereneChye@1/8/2014 :: Back to the previous page so that will go back to the system come from.--%>
                                    <%--<a href="cancelLogin?archive_=${archive_}">Kembali</a>--%>
                                </td>
                            </tr>
<!--                            <tr>
                                <td height="10px"><%--some space--%></td>
                            </tr>-->
                            <tr>
                                <td colspan="2" class="announceContent"><s:property value="%{announceModel_.announce_content}"/></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td height="auto">&nbsp;</td>
                </tr>
                <tr>
                    <td align="center" id="footer_imp"><jsp:include page="/main/internalMainFooter_1.jsp" /></td>
                </tr>
            </form>
        </table>
    </body>
</html>