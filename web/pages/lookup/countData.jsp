<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%--<s:head />--%>
    <script type="text/javascript" language="javascript">
        function updateFields() {
            multipleRecs_ = false;
            <s:if test="result != null">
                recCount_ = <s:property value="result.size"/>;
            </s:if><s:else>
                recCount_ = 0
            </s:else>
            if (postEvent_) {
                if (postEvent_ != "") {
                    parent[postEvent_].apply(this, Array.prototype.slice.call(arguments, 1));
                }
            }
        }
    </script>
</head>
<body>
    <script type="text/javascript" language="javascript">
        updateFields();
    </script>
</body>
</html>