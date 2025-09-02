<%-- 
    Document   : fileUpload
    Created on : Aug 4, 2010, 8:20:02 AM
    Author     : lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>File Upload</title>
        <script type="text/javascript" language="javascript">
            function checkForm(theForm){
                if(!theForm.userFile.value.match(/\.(pdf)$/i)){
                    alert("Only PDF format is allowed.");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <s:if test="actionErrors.size > 0"><s:actionerror/></s:if>
        <form theme="simple" action="uploadFile" method="post" enctype="multipart/form-data" >
            <div class="titleFramework">
                <span class="titleText"><s:text name="uploadFile" /></span>
            </div>
            <div class="xbox">
                <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
                    <tr>
                        <td align="right">
                            <s:submit theme="simple" type="button" cssClass="defaultButton" value="Upload" align="center" onclick="return checkForm(this.form)"/>
                            <s:submit type="button" cssClass="defaultButton" theme="simple" action="cancelUpload" value='%{getText("button.cancel")}'/>
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td><s:text name="selectUploadFile"/></td>
                        <td>
                            <s:file size="60" theme="simple" name="userFile"/>
                            <s:hidden theme="simple" name="dType" value="%{dType}" />
                            <s:hidden theme="simple" name="dCode" value="%{dCode}" />
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </body>
</html>
