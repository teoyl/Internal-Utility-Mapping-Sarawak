<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Entity Entry</title>

<link rel="stylesheet" href="include/jquery.treeview/jquery.treeview.css" />
<link rel="stylesheet" href="include/jquery.treeview/screen.css" />

<script type="text/javascript" src="include/jquery.js"></script>
<script src="include/jquery.treeview/jquery.cookie.js" type="text/javascript"></script>
<script src="include/jquery.treeview/jquery.treeview.js" type="text/javascript"></script>
<script type="text/javascript" language="javascript" src="pages/scripts/lookup.js"></script>


<style type="text/css">
    treeview ul {
    background-color: white;
    margin-top: 4px;
    }
</style>
<script type="text/javascript">
    $(function() {
        $("#tree").treeview({
            collapsed: true,
            animated: "medium",
            control:"#sidetreecontrol",
            persist: "location"
        });
    })
<!--


function selectNode(<s:property value="lookForParam"/>) {
    parent.rowIdx = "<%= request.getParameter("rowIdx")%>";
    var lookFor = "<%= request.getParameter("lookFor")%>";
    var writeTo = "<%= request.getParameter("writeTo")%>";
    var focusOn = "<%= request.getParameter("focusOn")%>";
    <s:property value="updateParentString"/>
    parent.lookup_dtmlwindow.hide();
}

function updateParentForm(writeToValue, data){
    data = data.replace("$backslash", "\\");
    var parentFormId= "<%= request.getParameter("lookupParentFormId")%>";
    if (window.parent.document.getElementById(parentFormId)[writeToValue]) {
        window.parent.document.getElementById(parentFormId)[writeToValue].value = data;
    } else {
        window.parent.document.getElementById(writeToValue).innerHTML = data;
    }
}
-->

</script>


</head>
<body>

${entityTree}

<form name="formMain" id="formMain" method="post">
	<input type="hidden" id ="en_id" value="${tree_en_id}" />
	<input type="hidden" id ="en_parent_id" value="-" />
	<input type="hidden" name="hdnSetActionD" id="hdnSetActionD" value="" />
	<input type="hidden" name="hdnSetActionN" id="hdnSetActionN" value="" />
</form>

</body>
</html>