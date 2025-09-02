<%-- 
    Document   : internalMenu
    Created on : Jul 30, 2010, 11:30:29 AM
    Author     : Administrator
    Edited     : 1 Sept 2010 by Ivy
                 Change pt to px
--%>

<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title><s:text name="system.name"/>:- Internal Menu</title>

    <link rel="stylesheet" type="text/css" href="include/menu-superfish/css/superfish.css" media="screen">
    <script type="text/javascript" src="include/jquery.js"></script>
    <script type="text/javascript" src="include/menu-superfish/js/hoverIntent.js"></script>
    <script type="text/javascript" src="include/menu-superfish/js/superfish.js"></script>
    <script type="text/javascript" src="include/menu-superfish/js/supersubs.js">e</script>
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
        .clLogout {
            color: #FFFFFF;
            text-decoration:none;
            font-weight: normal;
        }
        .clLogout:hover {
            font-weight: bold;
        }
         .headertext {
            font-family: Arial,Helvetica,Geneva,Swiss,SunSans-Regular;
            font-weight: bold;
            font-size : 13px;
            color: #FBC313;
        }
    </style>
</head>
<body>
    <%
	String strLocale = "";
        if(request.getAttribute("request_locale") == null){
            strLocale = "EN";
        }

        //System.out.println("internal Menu " + strLocale);
        //System.out.println("java.util.Locale.getDefault().getLanguage() = " + java.util.Locale.getDefault().getLanguage());
        //System.out.println("WW_TRANS_I18N_LOCALE = " + session.getAttribute("WW_TRANS_I18N_LOCALE"));
        //System.out.println( "Local = "+ request.getSession().getAttribute("LOCALE_KEY").toString());
        if(session.getAttribute("WW_TRANS_I18N_LOCALE") == null){
            strLocale = "EN";
        }else{
            strLocale = session.getAttribute("WW_TRANS_I18N_LOCALE").toString();
        }
	String treeList = "";
        if(strLocale.equalsIgnoreCase("BM")){
            treeList = (String) session.getAttribute("menuList_bm");
        }else{
            treeList = (String) session.getAttribute("menuList");

        }
	if (treeList == null) treeList = "";

        // ThoTH @ 20-May-2013
        //String treeList_ESS = (String) session.getAttribute("menuList_ESS");
	//if (treeList_ESS == null) treeList_ESS = "";
    %>
    <s:set name="systemTypeValue"><%=com.sains.common.util.SystemConstants.SYSTEM_TYPE.DEFAULT%></s:set>
    <%--old UI starts here, commented by Zhafari - 28 May 2013--%>
    <%--<div style=" width: 150px; color: white;  background-color: white; margin: 0">
        <ul id='navigationTree' class='sf-menu' >
            <li class='current'><a href='javascript:void(0)'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Your Menu&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
                <ul>
                    <s:if test='systemType_.equals(#systemTypeValue)'>
                        <%=treeList%>
                    </s:if>
                    <s:else>
                        <%=treeList_ESS%>
                    </s:else>
                </ul>
            </li>
        </ul>
    </div>
   <div style="padding-left: 12px; float: left; padding-top: 6px; width: 828px; height: 21px; margin-top: 0; background-image: url(include/menu-superfish/images/bar_bg.gif); background-color: #616161; ">
        <font class="headertext">
        <b><s:property value="#session.userName"/></b>
        <a title="Logout" href="processlogoutLogout" class="clLogout">&nbsp;[Logout]</a>
        </font>
   </div>--%>
    <%--new UI starts here, added by Zhafari - 28 May 2013--%>
   <table align="center" border="0" cellpadding="0" cellspacing="0" width="1000px">
        <tr>
            

                <td valign="top" align="left" height="50px">
                    <div style="height: 50px;">
                        <ul id='navigationTree' class='sf-menu' >
                            <li class="current">
                                <div class="rootmenu">
                                    <a style="border: 0px; color: white;"  href='javascript:void(0)'><%--Your Menu--%><s:text name="common.menu"/></a>
                                </div>
                                <ul style="margin-top: 7px;">
                                    <%=treeList%>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </td>
            
            <td valign="top" align="right" height="50px" id="session_name_imp">
                <s:property value="#session.userName"/>
            </td>
            <td valign="top" align="right" width="50px">
                <a href="processlogoutLogout" title="<s:text name="common.logout"/>"><img src="images/imp_logout.png" alt="<s:text name="common.logout"/>"/></a>
            </td>
        </tr>
    </table>
</body>
</html>


