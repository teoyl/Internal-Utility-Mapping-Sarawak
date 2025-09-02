<%-- 
    Document   : internalMenu
    Created on : Jul 30, 2010, 11:30:29 AM
    Author     : Administrator
    Edited     : 1 Sept 2010 by Ivy
                 Change pt to px
--%>

<%@taglib uri="/struts-tags" prefix="s"%>
<%--<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title>eLASIS- Internal Menu</title>--%>

    <%--<link rel="stylesheet" type="text/css" href="include/menu-superfish/css/superfish.css" media="screen">
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

    </script>--%>
    <%--<style type="text/css">
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
    </style>--%>
<%--</head>
<body>--%>
    <%
	//String treeList = (String) session.getAttribute("menuList");
	//if (treeList == null) treeList = "";
        // Zhafari @ 16-Apr-2013 - tile menu
        //String tileList = (String) session.getAttribute("menuTile");
        //if (tileList == null) tileList = "";
        // Zhafari @ 16-Apr-2013 - END

        // ThoTH @ 20-May-2013
        String treeList_ESS = (String) session.getAttribute("menuList_ESS");
	if (treeList_ESS == null) treeList_ESS = "";
    %>
    <%--<s:set name="systemTypeValue"><%=com.sains.common.util.SystemConstants.SYSTEM_TYPE.DEFAULT%></s:set>--%>
    <table align="center" border="0" cellpadding="0" cellspacing="0" width="1000px">
        <tr>
            <td valign="top" align="left" height="50px">
                <div style="height: 50px;">
                    <ul id='navigationTree' class='sf-menu' >
                        <li class="current">
                            <div class="rootmenu">
                                <a style="border: 0px; color: white;"  href='javascript:void(0)'><%--Your Menu--%>Menu Anda</a>
                            </div>
                            <ul style="margin-top: 7px;">
                                <%--<s:if test='systemType_.equals(#systemTypeValue)'>
                                    <%=treeList%>
                                </s:if>
                                <s:else>--%>
                                    <%=treeList_ESS%>
                                <%--</s:else>--%>
                            </ul>
                        </li>
                    </ul>
                </div>
            </td>
            <td valign="top" align="right" height="50px" id="session_name_imp">
                
                    <s:property value="#session.userName"/>
                
                <%--<div style="width: 100%; height: 23px; margin-top: 0; padding-top: 4px; background-image: url(include/menu-superfish/images/bar_bg.gif); background-color: #616161; ">
                    <font class="headertext" style="padding-left: 10px;">
                        <b><s:property value="#session.userName"/></b>
                        <a title="Logout" href="processlogoutLogout" class="clLogout">&nbsp;[Logout]</a>
                    </font>
                </div>--%>
            </td>
            <td valign="top" align="right" width="50px">
                <a href="processlogoutLogout" title="<s:text name="lbl.logout"/>"><img src="images/imp_logout.png" alt="<s:text name="lbl.logout"/>"/></a>
            </td>
        </tr>
    </table>
            <%--<div id="menu-tile-container">
                <table id="menu-tile-table" align="center" border="0" cellpadding="0" cellspacing="3" >
                            <%=tileList%>
                </table>
            </div>--%>

    

<%--</body>
</html>--%>


