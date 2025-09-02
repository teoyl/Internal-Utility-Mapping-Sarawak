
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Akses Pengguna</title>
    </head>
    <body>
        <table class="userAccessTable" width="98%">
            <s:iterator value="groupWFList_" status="groupWorkFlowStatus" id="groupWorkFlow">
                <tr>   
                    <td class="userAccessDetailList" >
                        <s:property value="#groupWorkFlowStatus.index+1" /> .
                        ${groupWorkFlow.wfGroup.wg_name}
                    </td>
                </tr>
            </s:iterator>
        </table>
    </body>
</html>
