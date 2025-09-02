<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <link rel="icon" href="<s:text name="system.icon"/>" type="image/x-icon"/>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style>
            * {box-sizing: border-box;}

            body { 
                margin: 0;
                font-family: Arial, Helvetica, sans-serif;
            }

            .header {
                overflow: hidden;
                background-color: #f1f1f1;
                padding: 0px 20px;
            }

            .header a {
                float: left;
                color: black;
                text-align: center;
                padding: 20px 10px;
                text-decoration: none;
                font-size: 18px; 
                line-height: 25px;
                border-radius: 4px;
            }

            .header a.logo {
                font-size: 25px;
                font-weight: bold;
            }

            .header a:hover {
                background-color: #ddd;
                color: black;
            }

            .header a.active {
                background-color: dodgerblue;
                color: white;
            }

            .header-right {
                float: right;
                padding: 20px 10px;
            }

            @media screen and (max-width: 500px) {
                .header a {
                    float: none;
                    display: block;
                    text-align: left;
                }

                .header-right {
                    float: none;
                }
            }
        </style>
        <script type="text/javascript">
            /**
             * Comment
             */
            function showHideError(button) {
                if (button.value == "<s:text name="errors.showError"/>") {
                    button.value = "<s:text name="errors.hideError"/>"
                    document.getElementById("errorDetail_").style.display = "block";
                } else {
                    button.value = "<s:text name="errors.showError"/>"
                    document.getElementById("errorDetail_").style.display = "none";
                }
            }
        </script>
    </head>
    <body>
        <div class="header">
            <a href=""><img src="<s:text name="system.logo"/>" height="60px"/></a>
            <!--<h1><s:text name="systemInfo.systemName"/></h1>-->
        </div>

        <div style="padding-left:20px">
            <h2><s:text name="errors.unexpectedError"/></h2>
            <div><input type="button" onclick="showHideError(this)" value="<s:text name="errors.showError"/>"/></div>
            <div id="errorDetail_" style="display: none">
                <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
            </div>
        </div>
    </body>
</html>