<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>

<%@taglib uri="/struts-tags" prefix="s"%>
<s:set name="ctx" value="%{pageContext.request.contextPath}"/>



<html>
    <head><s:set id="systemWelcome_"><s:text name="system.welcome"/></s:set>
        <title><s:text name="system.name"/>:
            <decorator:title default='${systemWelcome_} !'/>
        </title>
        <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
        <!--<link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico?" type="image/x-icon" />-->
        <script type="text/javascript" src="pages/scripts/focusColumn.js"></script>
        <s:if test='#session.login_user_type.equalsIgnoreCase("PUBLIC")'>
            <style type="text/css">
            @import url(styles/msen_style.css);
            </style>
        </s:if>
        <s:else>
            <style type="text/css">
            @import url(styles/style_internal.css);
            </style>
        </s:else>
        <style type="text/css">

           
            <%--html, body{height: 100%; width: 990px; border: 0; padding: 0; margin: 0; background: #F1EADA;}--%>
            #spacer{
                display: block;
                min-height: 88%;
                height: 88%;
                width: 1px;
                padding: 0;
                margin: 0;
                border: 0;
                background: #F1EADA; /* same as body bg */
                float: right;
            }

            #bodycontent {
                width: 990px;
                margin: 0;
                padding: 0;
                padding-top: 120px;
                border: 0;
                float: left;
                margin-right: -1px; /* this is the key to avoid the 1px jog caused by spacer */
                background: #F1EADA;
            }

            #bodyarea{
                margin: 0px;
                background: #F1EADA;
                padding: 0;
                width: 990px;
                float: left;
            }

            #footer{
                display: block;
                clear:both;
                width: 990px;
                margin-left: 5px;
                padding: 0;
                left: 10px;
                background: #616161;
                text-align: left;
            }

            #footer p{background: #ddd;}

            #header{
                position: absolute;
                top: 1px;
                left: 5px;
                width: 990px;
                height: 95px;
                background: #f2f2f2;
                color: white;
                z-index: 50;
            }

            #menu{
                position: absolute;
                top: 0;
                left: 5px;
                width: 990px;
                height: 30px;
                padding-top: 96px;
                /*background-image: url("../images/menu_bg.gif") ;*/
                /*background-repeat: repeat-x;*/
                /*background: #f2f2f2;*/
                /*border: solid black;*/
            }

        </style>

        <decorator:head />
    </head>
    <body bgcolor="#F1EADA" >
        <table style="height: 100%" width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">
            
            <s:if test='#session.login_user_type.equalsIgnoreCase("public")'>
                <tr>
                    <td align="center" height="100px" id="banner_imp">
                    <page:applyDecorator page="/main/publicMainHeader.jsp" name="panel1" /></td>
                </tr>
                <tr height="50px" >
                <td align="center" id="menubar_imp_public"><page:applyDecorator page="/main/publicMenu.jsp" name="panel1" /></td>
                </tr>
            </s:if>
            <s:elseif test='#session.login_user_type.equalsIgnoreCase("internal")'>
                <tr>
                <%--<td align="center" id="banner_imp"><page:applyDecorator page="/main/internalMainHeader_1.jsp" name="panel1" /></td>--%>
                <td align="center" height="100px" id="banner_imp">
                    <%--<s:property value="#session.login_user_type"/>--%>
                    <page:applyDecorator page="/main/internalMainHeader.jsp" name="panel1" /></td>
            </tr>
                <tr>
                    <td align="center" id="menubar_imp"><page:applyDecorator page="/main/internalMenu.jsp" name="panel1" /></td>
                </tr>
            </s:elseif>
            <tr>
                <td align="center" height="1"><table width="1000px" border="0" cellpadding="0" cellspacing="0"><tr><td>
                        <decorator:body /></td></tr></table></td>
            </tr>
            <tr>
                <td height="auto">&nbsp;</td>
            </tr>
            <tr>
                <td align="center" id="footer_imp"><page:applyDecorator page="/main/internalMainFooter.jsp" name="panel1" /></td>
            </tr>
        </table>
        <%--<div id="spacer"></div>
        <div id="bodycontent">
            <div id="bodyarea">
                <decorator:body />
                <br/>
            </div>
        </div>
        <div style="clear: both;"></div><!-- to clear the floats - might not be required -->
        <div id="header"><page:applyDecorator page="/main/internalMainHeader.jsp" name="panel1" /></div>
        <div id="menu"><page:applyDecorator page="/main/internalMenu.jsp" name="panel1" /> </div>
        <div id="footer"><page:applyDecorator page="/main/internalMainFooter.jsp" name="panel1" /></div>--%>


    </body>
    <script type="text/javascript" language="javascript">
        focusFirstColumn();
    </script>
</html>

