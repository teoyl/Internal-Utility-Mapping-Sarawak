<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <%--<s:head />--%>
        <s:set name="propGetter" value="new com.PropertyGetter()" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <form method="post" action="processTermsConditions" name="myform">
            <jsp:include page="/pages/base/actionError.jsp"></jsp:include>

            <table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
                <s:hidden theme="simple" name="strTc_version"/>
                <s:hidden theme="simple" name="_operation"/>
                <s:hidden id="user_type" name="user_type" />
                
                <!--Formatting by IvyL @ 2Nov2916-->
                <tr><td align="left" style="border-bottom: 0px solid #000;">
                        <p style="font-size: 14px; font-weight: bold; padding: 5px;"><s:text name="lbl.terms.and.conditions"/></p>
                        <!--<p style="font-size: 12px; padding: 5px;"><s:text name="tc.ver"/>${strTc_version}</p>--> 
                        <span style="font-size: 12px; padding: 5px;"><s:text name="tc.ver"/>${strTc_version}</span> 
                        <hr/>
                        <table width="100%" cellspacing="0" cellpadding="0" border="0">
                            <tr><td><s:text name="strTc_clause" /></td></tr>
                        </table>
                    </td></tr>
                <tr><td><br/></td></tr>
                <%--<tr><td style="text-align: justify;"><p>You agree to the terms and conditions outlined in this Terms and Conditions of use Agreement (Agreement) with respect to our site (the Site). This Agreement constitutes the entire and only agreement between us and you, and supersedes all prior or contemporaneous agreements, representations, warranties and understandings with respect to the Site, the content, free product samples or freebie offers or services provided by or listed on the Site, and the subject matter of this Agreement. This Agreement may be amended by us at any time and at any frequency without specific notice to you. The latest Agreement will be posted on the Site, and you should review this Agreement prior to using the Site.</p></td></tr>--%>
                <s:if test="!actionErrors.size() > 0">
                    <tr><td align="center">
                            <s:submit type="button" cssClass="btn btn-default" theme="simple" action="initLoginSPA" value="%{getText('btn.disagree')}"/>&nbsp;
                            <s:submit type="button" cssClass="btn btn-primary" theme="simple" action="processTermsConditionsRegistration" value="%{getText('btn.agree')}"/>
                            <div class="row">
                    </div>
                        </td></tr>
                </s:if>
            </table>
        </form>
    </body>
</html>
