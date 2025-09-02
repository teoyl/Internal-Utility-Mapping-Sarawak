<%-- Track Changes
  -- Author : ThoTH (by enhance the work of Sunny Saxena (code.zhandwa.com)
  -- Creation Date : 23-Feb-2012
  -- To track the changes made in form, to disallow user to nagivate away without save
  -- need to declare this JS variable in the JSP: var _formName_ = "yourFormName";
  -- Submit button onclick event need to call _submitMe_() function
  -- to exclude the field to be track, use the INPUT ID: _excludeItemIDList_ = "ID1,ID2"
  -- to remain the TrackChangeList and its oldValue after refresh Page, put this hidden field inside the form
        - <s:hidden name="trackChangesValue_" />
--%>
<script type="text/javascript" src="include/jquery.form.track.changes.js"></script>
<script type="text/javascript">
    var lbSubmit = false;
    var oldValue4TrackChanges = null;
    $(document).ready(function() {
        try {
            oldValue4TrackChanges = $("#"+_formName_).trackChanges({events: "change blur keypress keydown click", changeListVisible: false, excludeItem: _excludeItemIDList_});
        } catch (err) {
            if (err.message.toString().indexOf("_excludeItemIDList_") >= 0) {
                oldValue4TrackChanges = $("#"+_formName_).trackChanges({events: "change blur keypress keydown click", changeListVisible: false});
            }
        }


        setTrackChangesValue();  // this line must after the TrackChangesList was populated
        <%--console.log(oldvals1.get("model.signWitnessList[0].witness_name"));--%>
    });

    function _submitMe_() {
        if ($("#"+_formName_+"TrackList")[0].length == 0) {
            //stopMaskLoading();
            alert("No changes made." );
            return false;
        } else {
            lbSubmit = true;
            return true;
        }
    }

    <%--function _submitMe_(actionName) {
        alert("b");
        if ($("#"+_formName_+"TrackList")[0].length == 0) {
            alert("No changes made.");
            return false;
        } else {
            alert("000");
            setTrackChangesValue();
            lbSubmit = true;
            document.getElementById(_formName_).action = actionName;
            document.getElementById(_formName_).submit();
        }
    }--%>

    function _submitMe_(formName, actionName, alwaysSubmit) {
        console.log("YYYYYYYYYYYYYYYYYYY")
        if ($("#"+_formName_+"TrackList")[0].length == 0 && !alwaysSubmit) {
            //stopMaskLoading();
            alert("No changes made." +formName +"/" + actionName +"/"+alwaysSubmit);
            return false;
        } else {
            //startMaskLoading();  <%--May aledi called, but cater for other scenario--%>
            getTrackChangesValue();
            lbSubmit = true;
            document.getElementById(formName).action = actionName;
            document.getElementById(formName).submit();
            return false;  <%-- ThoTH @ 1-Aug-2012 :: to avoid IE button trigger 2 times --%>
        }
    }

    window.onbeforeunload = confirmExit;
    function confirmExit()  {
        if ($("#"+_formName_+"TrackList")[0].length > 0) {
            if (! lbSubmit) {
                maskLoadingUnloadStart = true;  // ThoTH @ 27-Aug-2012 :: Solved TrackChanges (cancel) coz MaskLoading non-stop
                //stopMaskLoading();  // ThoTH @ 27-Aug-2012 :: Solved TrackChanges (cancel) coz MaskLoading non-stop
                return "Your changes will be lost.";
            }
        }
    }

    function removeFromTrackChanges(_formName_, pTarget){
        var pList = $("#"+_formName_+"TrackList")[0];
        var li_RowCount = $("#"+_formName_+"TrackList")[0].length;

        // Search the List, Add if not exist
        for (var i=0; i<li_RowCount; i++) {
            if (pList.options[i].value == pTarget) {
                // Check if value is same as Original Value, remove if is same, then exit
                pList.options[i] = null;
                return false;
            }
        }
    }

    function getTrackChangesValue(){
        if (document.getElementById("trackChangesValue_") != null) {
            var pList = $("#"+_formName_+"TrackList")[0];
            var li_RowCount = $("#"+_formName_+"TrackList")[0].length;

            var vValue = "";

            // Search the List, Add if not exist
            for (var i=0; i<li_RowCount; i++) {
                vValue += "<NAME>"+pList.options[i].value+"</NAME>";  // get Name
                vValue += "<VALUE>"+oldValue4TrackChanges.get(pList.options[i].value)+"</VALUE>";  // get Old Value
            }

            document.getElementById("trackChangesValue_").value = vValue;
        }
    }

    function setTrackChangesValue(){
        if (document.getElementById("trackChangesValue_") != null) {
            var str = document.getElementById("trackChangesValue_").value;
            var pList = $("#"+_formName_+"TrackList")[0];
            var li_RowCount = $("#"+_formName_+"TrackList")[0].length;
            var tag = "<NAME>";
            var jsTagLen = tag.length;
            var jsEndTag = tag.replace(/</,"</");
            var jsPos = str.indexOf(tag);
            var jsName = "";
            var jsValue = "";

            while (str.length > 0) {
                // Get Column Name
                tag = "<NAME>";
                jsTagLen = tag.length;
                jsEndTag = tag.replace(/</,"</");
                jsPos = str.indexOf(tag) + jsTagLen;

                jsName = str.substring(jsPos, str.indexOf(jsEndTag));
                <%--console.log("111:"+jsName);--%>

                // Get Old Value
                tag = "<VALUE>";
                jsTagLen = tag.length;
                jsEndTag = tag.replace(/</,"</");
                jsPos = str.indexOf(tag) + jsTagLen;

                jsValue = str.substring(jsPos, str.indexOf(jsEndTag));

                // Set Name
                pList.options[li_RowCount] = new Option(jsName, jsName, true);
                oldValue4TrackChanges.set(jsName, jsValue);
                li_RowCount ++;
                <%--console.log(jsName + ":" + jsValue);--%>

                // Cut String
                str = str.substring(str.indexOf(jsEndTag) + jsTagLen +1);
                <%--console.log("222:"+str);--%>
            }
        }
    }

    // SAMPLE ONLY DON'T USE IT
    function addToListXXX(pList, pTarget, pOriginalValue) {
        var li_RowCount = pList.length;

        // Search the List, Add if not exist
        for (var i=0; i<li_RowCount; i++) {
            if (pList.options[i].value == pTarget) {
                // Check if value is same as Original Value, remove if is same, then exit
                pList.options[i] = null;
                return false;
            }
        }

        // Add
        pList.options[li_RowCount] = new Option(pTarget, pTarget, true);
        return true;
    }


</script>