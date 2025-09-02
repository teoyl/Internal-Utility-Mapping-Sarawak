<%-- 
    Document   : announcement_archive
    Created on : Jun 30, 2014, 3:10:26 PM
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
            <form id="loginForm" theme="simple" action="processloginLogin" method="post" onsubmit="return false;">
                <%--<tr>
                    <td align="center" height="90px" id="banner_imp"><jsp:include page="/main/internalMainHeader_1.jsp" /></td>
                </tr>--%>
                 <%--Added by Serene @ 30-July-2014 :: To apply background of header- END--%>
               <td align="center" height="90px" id="banner_imp" >
                 <s:if test='systemType_.equals("ESS")'>
                        <page:applyDecorator page="/main/internalMainHeader_1.jsp" name="panel1" />
                    </s:if>
                    <s:else>
                         <page:applyDecorator page="/main/internalMainHeader.jsp" name="panel1" />
                    </s:else>
                </td>
              <!--<tr>
                    <td align="center" id="menubar_imp" class="title" height="35px"><h2><s:text name="announcement.archive" /></h2>
                    <div id="dateDiv"></div>
                    </td>
                </tr>  -->
               <tr>
                    <td style="border-top: 3px solid #a2c4c9;">&nbsp;</td>
                </tr>
                
                <tr>
                    <td height="15px"><%--some space--%></td>
                </tr>
                <tr>
                    <td align="center" valign="top" height="585px">
                        <table width="1000px" border="0" cellpadding="2" cellspacing="0" id="announceArchive">
                            <tr>
                                <td id="announceHeader">
                                   <s:text name="announcement.archive" />
                                </td>
                                <td id="announceHeader" class="announceContinue" style="font-size: 8pt">&nbsp;</td>
                            <!--<td id="announceHeader" class="announceContinue" style="font-size: 8pt"><a href="cancelLogin">Kembali</a></td>-->
                            </tr>
                        </table>
                        <table width="1000px" border="0" cellpadding="2" cellspacing="0" id="announceArchive">
                            <tr id="announceSubHeader">
                                <td colspan="2"><s:text name="announcement.announceDate" /></td>
                                <td><s:text name="announcement.title" /></td>
                                <!-- Comment by sereneChye @1/8/2014 :: To direct the link to page previously.
                                    <td id="announceHeader" class="announceContinue" style="font-size: 8pt"><a href="cancelLogin">Kembali</a></td>-->
                                    <td id="announceHeader" class="announceContinue" style="font-size: 8pt">
                                        <a href="javascript: window.history.go(-1)">Kembali</a>
                                    </td>
                            </tr>
                            <s:if test='systemType_.equals("ESS")'>
                            <s:set name="archiveYear" value="announceListESS[0].created_year_str" />
                            <tr>
                                <td align="left" class="label_imp" width="100px">${archiveYear}</td>
                                <td width="90px" align=""></td>
                                <td></td>
                            </tr>
                            <s:iterator value="announceListESS" var="archive" status="archiveStatus">
                                <s:if test="!#archive.announce_prepare_year_str.equals(#archiveYear)">
                                    <tr>
                                        <td height="10px"><%--some space--%></td>
                                    </tr>
                                    <tr>
                                        <td align="left" class="label_imp">${archive.announce_prepare_year_str}</td>
                                        <td></td>
                                    </tr>
                                </s:if>
                                <tr>
                                    <td align="right">${archive.announce_prepare_date_str}</td>
                                    <td></td>
                                    <td class="archiveLink"><a href="loadAnnouncementPageLogin?archive_=Y&itemId_=${archive.ID}">${archive.announce_title}</a></td>
                                </tr>
                            </s:iterator>
                           
                            </s:if>
                            <s:else>
                            <s:set name="archiveYear" value="announceList[0].created_year_str" />
                            <tr>
                                <td align="left" class="label_imp" width="100px">${archiveYear}</td>
                                <td width="90px" align=""></td>
                                <td></td>
                            </tr>
                            <s:iterator value="announceList" var="archive" status="archiveStatus">
                                <s:if test="!#archive.announce_prepare_year_str.equals(#archiveYear)">
                                    <tr>
                                        <td height="10px"><%--some space--%></td>
                                    </tr>
                                    <tr>
                                        <td align="left" class="label_imp">${archive.announce_prepare_year_str}</td>
                                        <td></td>
                                    </tr>
                                </s:if>
                                <tr>
                                    <td align="right">${archive.announce_prepare_date_str}</td>
                                    <td></td>
                                    <td class="archiveLink"><a href="loadAnnouncementPageLogin?archive_=Y&itemId_=${archive.ID}">${archive.announce_title}</a></td>
                                </tr>
                            </s:iterator>
                            </s:else>
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