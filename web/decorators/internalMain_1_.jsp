<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:set name="ctx" value="%{pageContext.request.contextPath}"/>
<%
String strLocale = "";
String strAction = (String) session.getAttribute("curAction");
String queryString = request.getAttribute("action").toString();
//if(session.getAttribute("WW_TRANS_I18N_LOCALE") == null){
//    strLocale = "EN";
//}else{
    strLocale = session.getAttribute("WW_TRANS_I18N_LOCALE").toString();
//}
System.out.println("strLocale at internalMain_1 " + strLocale);
%>
<html>
    <head><s:set id="systemWelcome_"><s:text name="system.welcome"/></s:set>
    <title><s:text name="system.name"/>:
        <decorator:title default='${systemWelcome_} !'/>
    </title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <!--<link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico?" type="image/x-icon" />-->
    <script type="text/javascript" src="pages/scripts/focusColumn.js"></script>
    <link rel="stylesheet" type="text/css" href="include/menu-superfish/css/superfish_1.css" media="screen"/>
    <script type="text/javascript" src="include/jquery.js"></script>
    <script type="text/javascript" src="include/menu-superfish/js/hoverIntent.js"></script>
    <script type="text/javascript" src="include/menu-superfish/js/superfish.js"></script>
    <script type="text/javascript" src="include/menu-superfish/js/supersubs.js"></script>


    <script type="text/javascript">

    // initialise plugins
    $(document).ready(function(){
        $("ul.sf-menu").supersubs({
            minWidth:    12,   // minimum width of sub-menus in em units
            maxWidth:    27,   // maximum width of sub-menus in em units
            extraWidth:  1     // extra width can ensure lines don't sometimes turn over
                               // due to slight rounding differences and font-family
        }).superfish();  // call supersubs first, then superfish, so that subs are
                         // not display:none when measuring. Call before initialising
                         // containing tabs for same reason.
    });

    </script>
        <style type="text/css">
            @import url(styles/msen_style_internal_ess.css);
            #spacer{
                display: block;
                min-height: 88%;
                height: 88%;
                width: 1px;
                padding: 0;
                margin: 0;
                border: 0;
                background: #EEEEEE; /* same as body bg */
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
                background: #EEEEEE;
            }

            #bodyarea{
                margin: 0px;
                background: #EEEEEE;
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
                background: #EEEEEE;
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
            .clLogout {
                color: #FFFFFF;
                text-decoration:none;
                font-weight: normal;
            }
            .clLogout:hover {
                font-weight: bold;
                text-decoration:none;
            }
             .headertext {
                font-family: Arial,Helvetica,Geneva,Swiss,SunSans-Regular;
                font-weight: bold;
                font-size : 13px;
                color: #FBC313;
            }

            /*IMPIAN CSS*/

            
        </style>

        <decorator:head />
    </head>
    <body>
        <table style="height: 100%" width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
            <tr>
                <%--<td align="center" id="banner_imp"><page:applyDecorator page="/main/internalMainHeader_1.jsp" name="panel1" /></td>--%>
                <td align="center" height="100px" id="banner_imp"><page:applyDecorator page="/main/internalMainHeader_1.jsp" name="panel1" /></td>
            </tr>
            <tr>
                <td align="center" id="menubar_imp"><page:applyDecorator page="/main/internalMenu_1.jsp" name="panel1" /></td>
            </tr>
            <tr>
                <td align="center" height="1"><table width="1000px" border="0" cellpadding="0" cellspacing="0"><tr><td>
                        <decorator:body /></td></tr></table></td>
            </tr>
            <tr>
                <td height="auto">&nbsp;</td>
            </tr>
            <tr>
                <td align="center" id="footer_imp"><page:applyDecorator page="/main/internalMainFooter_1.jsp" name="panel1" /></td>
            </tr>
        <%--<div id="header"><page:applyDecorator page="/main/internalMainHeader.jsp" name="panel1" /></div>--%>
        <%--<div id="menu"><page:applyDecorator page="/main/internalMenu.jsp" name="panel1" /> </div>--%>
        <%--<div id="footer"><page:applyDecorator page="/main/internalMainFooter.jsp" name="panel1" /></div>--%>
        
        </table>
        <%--<div style="clear: both;"></div><!-- to clear the floats - might not be required -->--%>
    </body>
    <script type="text/javascript" language="javascript">
        focusFirstColumn();
    </script>
</html>

