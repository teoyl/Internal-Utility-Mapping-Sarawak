<%-- 
 * Copyright © 2009 SAINS.  All rights reserved.
 * 
 * PROJECT: eKPI
 * PACKAGE/FILE NAME: Main / main.jsp
 * DESCRIPTION: Main Frame
 * 
 * DEVELOPMENT AND MODIFICATION HISTORY:
 * 
 * Name           Date        Version   Description
 * -------------------------------------------------
 * WongKK       Mar-Jun 2009  1.0.0     Development 
 * ThoTH        29-07-2009              Put Into Folder
 * 
--%>

<HTML>
<HEAD>
<META NAME="GENERATOR" Content="Microsoft Visual Studio 6.0">
<script language="javascript">
if(window !=top){top.location.href=location.href}
</script>
</HEAD>
	<frameset rows="130,*"  frameborder="0" framespacing="0" scrolling="No" noresize bordercolor=Black border=2>
    <frame src="header.jsp" marginwidth="3" marginheight="3" scrolling="No" noresize id=Top>
    <frameset cols = "170, *"  ROWS = "100%" bordercolor=Black framespacing=0 frameborder=none border=2>
      <frame src="menutree.jsp" scrolling=yes id=Left>
      <frame src="home.jsp" id=Main name=Main>
    </frameset>
  </frameset>
</HTML>