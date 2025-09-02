<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@taglib uri="/struts-tags" prefix="s"%>
<s:set name="ctx" value="%{pageContext.request.contextPath}"/>

<title><s:set id="systemName_"><s:text name="system.name"/></s:set>
<decorator:title default="${systemName_}"/>
</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<%--<style type="text/css">
	@import url(style.css);
</style>--%>
<decorator:head/>
  <script type="text/javascript" src="pages/scripts/common.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/pages/scripts/validation.js"></script>
  <link href="styles/general.css" rel="stylesheet" type="text/css" />
  <link href="styles/common.css" rel="stylesheet" type="text/css" />

  <body style="margin: 0" bgcolor="#fd9602" >
		<center>
            <table border="0" cellpadding="0" cellspacing="0" width="990" bgcolor="white">
			<tr>
                <td align="center" colspan="3">
                    <page:applyDecorator page="/publicmain/mainHeader.jsp" name="panel1" />
				</td>
			</tr>
			<tr>
                <td width="10"><img height="10" width="10" src="images/div.gif" border="0" alt=""/></td>
                <td align="center" align="left">
					<decorator:body />
				</td>
                 <td width="10"><img height="10" width="10" src="images/div.gif" border="0" alt=""/></td>
			</tr>
			<tr>
                <td colspan="3"><img height="10" width="10" src="images/div.gif" border="0" alt=""/></td>
			</tr>
		</table>
        <page:applyDecorator page="/publicmain/publicFooter.jsp" name="panel1" />
	</center
</body>