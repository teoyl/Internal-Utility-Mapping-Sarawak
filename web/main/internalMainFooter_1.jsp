<%--
    Edited     : 1 Sept 2010 by Ivy
                 Change pt to px
--%>


<%@taglib uri="/struts-tags" prefix="s"%>
<%--<style type="text/css">
.footer_L1 {
    font-family: Arial,Helvetica,Geneva,Swiss,SunSans-Regular;
    color: white;
}

.footer_L2 {
    font-family: Arial,Helvetica,Geneva,Swiss,SunSans-Regular;
    color: #c9c9c9;
    font-size: 12px;
}
</style> commented by zhafari--%>

<table border="0" cellpadding="0" cellspacing="0" width="1000px">
    <tr>
        <td colspan="2" valign="top" align="left">
            <span class="system_name"><s:text name="system.name"/></span>
            <span class="system_version"><s:text name="systemInfo.version"/></span>
        </td>
    </tr>
    <tr>
       <td width="430px" class="copyright">
           <s:text name="systemInfo.copyRight"/>
       </td>
       <td align="right" class="best_view">
            <s:text name="systemInfo.bestView"/>
       </td>
    </tr>
</table>
