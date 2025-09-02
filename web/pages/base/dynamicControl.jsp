<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<title>${searchDescription} - <s:text name="actionType.manage" /></title>
<%--<s:head />--%>
<s:if test='jsInclude != null'>
    <script language="JavaScript" type="text/javascript" src="<s:property value="%{jsInclude}"/>"></script>
</s:if>
    <style>
        .fixMarginLeft {
            margin-left: <s:property value="moreInfoMarginLeft"/> !important;
        }
    </style>
<script>
    function moreInfoLoaded() {
    <s:if test='moreInfoType.equals("Auto")'>
//        $('#moreInfoDiv').find(".btnClass").addClass("hidden");
//        $('#moreInfoDiv').find("input,select,radio,textarea").prop( "disabled", true );
//        $('#moreInfoDiv').find(".relogin").prop( "disabled", false );
//        $('#moreInfoDiv').find(".panel").removeClass("panel");
    </s:if>
    }
    function openMoreInfo2(dcAction,id) {
        $("#moreInfoDiv").html("");
        $("#moreInfoDiv").load("moreInfo"+dcAction+"?action=UtilityJobDiv&id="+id,
            function(message) {
                if (message === "Expired") {
                    document.location = "initLogin";
                }
                $("#moreInfoDiv").data('width', '60%');
                $('#moreInfoDiv').modal('show');
                $("#moreInfoDiv").on('hide', function() {
                });
            });
//    divSubmitForm("moreInfo"+dcAction+"?id="+id, "sortForm","moreInfoDiv","newAnak");
}
function closeMoreInfo(){
    $('#moreInfoDiv').modal('hide');
    }

function openMoreInfo(dcAction,id) {
        $("#moreInfoDiv").load("moreInfo"+dcAction+"?action=USJ_UtilityJobDiv&id="+id,
        function(message) {
            if (message === "Expired") {
                document.location = "initLogin";
            }
            $("#moreInfoDiv").css('width', '<s:property value="moreInfoWidth"/>');
            $("#moreInfoDiv .modal-dialog").css('max-width', '100%');
//            $("#moreInfoDiv .modal-dialog").css('max-width', '<s:property value="moreInfoWidth"/>');
            $("#moreInfoDiv .modal-dialog").css('width', 'auto');
            
            $('#moreInfoDiv').modal('show');
            
            moreInfoLoaded();
            $("#moreInfoDiv").on('shown.bs.modal', function () {
                $("#moreInfoDiv").css('margin-left', '15%');
            });
            $("#moreInfoDiv").on('hidden.bs.modal', function() {
                <s:if test="moreInfoReloadOnClose">
                if (reloadOnClose) {
                    $("#search2DynamicFormId").submit();
                }
                </s:if>
//                $(this).off('hidden.bs.modal');
//                if (lookupSelected) {
//                if (postEvent !== undefined) {
//                    if (postEvent.includes("::=")) {
//                        var idx = postEvent.indexOf("::=");
//                        window[postEvent.substring(0, idx)](postEvent.substring(idx+3));
//                    } else {
//                        window[postEvent]();
//                    }
//                }
//                }
//                $(field).focus();
//                $("#moreInfoDiv").html("");
            });
        });
    }
</script>
</head>
<body>
<%--<jsp:include page="actionError.jsp"></jsp:include>--%> <%--commented by Delvene @ 09-Jul-2013--%>
<%--div class="panel panel-default ">
  <div class="panel-heading ">
    <h3 class="panel-title"> 
        <span class="titleText">${searchDescription}</span>
    <span class="titleActionTypeText"> | <s:text name="actionType.manage" /></span></h3>
  </div>
  <div class="panel-body"--%>
    <jsp:include page="${searchPage}.jsp"></jsp:include>
  <%--/div>
</div--%>


  
<%--<div class="titleFramework">
    <span class="titleText">${searchDescription}</span>
    <span class="titleActionTypeText"> | <s:text name="actionType.manage" /></span><br>
</div>--%>
<!--<div class="xbox">-->
    <%--<jsp:include page="${searchPage}.jsp"></jsp:include>--%>
<!--</div>-->
</body>
</html>