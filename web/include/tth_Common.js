/*
COMMON JAVASCRIPT
- submitEnter(myfield,e)
- GetXmlHttpObject
- isEmpty(aValue, aName)
- isEmptyCustomMsg(aValue, aMsg)
- isEmptyRow(aValue, aName, aRow)
- pad(str, len, pad, dir)
- genSqlNotInList(pForm, pName)
- parse_xml(str, tag)
- searchList(pList, pTarget)
- addToList(pList, pTarget, pOriginalValue)
- removeFromList(pList, pTarget)
- processBack(pList)
- TrackRowChanged(pForm, pList)
- confirmExit()
*/
var ibSubmit = false;

function submitEnter(myfield,e) {
  var keycode;
  if (window.event) keycode = window.event.keyCode;
  else if (e) keycode = e.which;
  else return true;
  
  if (keycode == 13) {
     submitForm("S");
     return false;
  } else {
     return true;
  }
}

function GetXmlHttpObject(){
  var xmlHttp=null;
  try
    {
    // Firefox, Opera 8.0+, Safari
    xmlHttp=new XMLHttpRequest();
    }
  catch (e)
    {
    // Internet Explorer
    try
      {
      xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
      }
    catch (e)
      {
      xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
    }
  return xmlHttp;
}

function isEmpty(aValue, aName) {
  var re = /\s/g; //Match any white space including space, tab, form-feed, etc. 
  RegExp.multiline = true; // IE support
  var str = aValue.replace(re, "");
  if (str.length == 0) {
    alert(aName + " must be filled.");
    return true;
  } else {return false;}
} 

function isEmptyCustomMsg(aValue, aMsg) {
  var re = /\s/g; //Match any white space including space, tab, form-feed, etc. 
  RegExp.multiline = true; // IE support
  var str = aValue.replace(re, "");
  if (str.length == 0) {
    alert(aMsg);
    return true;
  } else {return false;}
}

function isEmptyRow(aValue, aName, aRow) {
  var re = /\s/g; //Match any white space including space, tab, form-feed, etc. 
  RegExp.multiline = true; // IE support
  var str = aValue.replace(re, "");
  if (str.length == 0) {
    alert("Row " + aRow + ": " + aName + " must be filled.");
    return true;
  } else {return false;}
} 

 
function pad(str, len, pad, dir) { 
  var STR_PAD_LEFT  = 1;
  var STR_PAD_RIGHT = 2;
  var STR_PAD_BOTH  = 3;

	if (typeof(len) == "undefined") { var len = 0; }
	if (typeof(pad) == "undefined") { var pad = ' '; }
	if (typeof(dir) == "undefined") { var dir = STR_PAD_RIGHT; }
 
	if (len + 1 >= str.length) { 
		switch (dir){ 
			case STR_PAD_LEFT:
				str = Array(len + 1 - str.length).join(pad) + str;
			break;
 
			case STR_PAD_BOTH:
				var right = Math.ceil((padlen = len - str.length) / 2);
				var left = padlen - right;
				str = Array(left+1).join(pad) + str + Array(right+1).join(pad);
			break;
 
			default:
				str = str + Array(len + 1 - str.length).join(pad);
			break; 
		} // switch 
	} 
	return str; 
}

function genSqlNotInList(pForm, pName, phdnRowCount, phdnChanged) {
/*
Generate a SQL NOT IN / IN clause, e.g. 'thoth', 'ali', 'ah kao'
- pName related column must follow this format "abc#1", where pName = "abc"
- Hidden field, hdnRowCount, must exist in the pForm
- Hidden field, hdnChanged#?, must exist in the pForm, row with value "NEWDEL" will be excluded
- Return empty string if not record found
*/
  if (phdnRowCount == null) {phdnRowCount = "hdnRowCount";}
  if (phdnChanged == null) {phdnChanged = "hdnChanged";}

  var lsList = "";
  var lsValue, lsStatus;
  var liRowCount = document.forms(pForm).elements(phdnRowCount).value;
  
  for (var i=1; i<=liRowCount; i++) {
    lsValue = encodeURIComponent(document.forms(pForm).elements(pName+"#"+i).value);
    if (lsValue != null) {
      lsStatus = document.forms(pForm).elements(phdnChanged+"#"+i).value;
      if (lsStatus != "NEWDEL") {
        lsList += "'"+lsValue+"',";
      }
    }
  }  
  
  var intLen = lsList.length;
  if (intLen > 0) {
    lsList = lsList.substring(0, intLen-1);
  }
  
  return lsList;
}

// Common Function:: Return the value inside the tag
function parse_xml(str, tag) {
  var jsTagLen = tag.length;
  var jsEndTag = tag.replace(/</,"</");
  var jsPos = str.indexOf(tag) + jsTagLen;
  
  var jsResult = str.substring(jsPos, str.indexOf(jsEndTag)); 
  
  return jsResult;
}

function searchList(pList, pTarget) {
  var li_count = pList.length;

  for (var i=0; i<li_count; i++ ){
    ls_column = pList.options[i].value;
    if (ls_column == pTarget) {
      return true;
    }
  }
  return false;
}

function addToList(pList, pTarget, pOriginalValue) {
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

function removeFromList(pList, pTarget) {
  var a = pList.length -1;
  while (a >= 0) {  
    if (pList.options[a].value == pTarget) { 
      pList.options[a] = null; 
    }
    a --;
  }
}

function processBack(pList) {
  var jsAnswer = confirm("Are you sure you want to navigate away from this page?\n\nYour changes will be lost.\n\nPress OK to continue, or Cancel to stay on the current page.");
  return jsAnswer;
}

function TrackRowChanged(pForm, pList) {
/*
Must use together with jquery.form.track.changes.js
Parameter Received e.g. document.formMain  ; document.formMain.formMainTrackList
Following Hidden column must be exist in the pForm:
  - hdnChanged#?   ** For every Row, indicate whether Row Changed, Y (Changed) : N (No, Default)
  
Column name must be numbered followed by #, e.g. columnA#1
*/
  var lsCName;  
  var i, liCount, liPos, liRow;
    
  liCount = pList.length;  
    
  for (i=0; i<liCount; i++) {  
    lsCName = pList.options[i].value;
    liPos   = lsCName.indexOf('#');
    lsCName = lsCName.substring(liPos +1); 
    liRow   = parseInt(lsCName);
    if (liRow > 0) {
      pForm.elements("hdnChanged#"+liRow).value = "Y";
    }
  }  
}

function confirmExit() {
  if (document.formMain.formMainTrackList.length > 0) {  
    if (! ibSubmit) {    
      return "Your changes will be lost."; 
    } 
  }
}