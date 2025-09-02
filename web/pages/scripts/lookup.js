var win;
var lookup_dtmlwindow;
var parentFormId;
var defaultFilter = "undefined";
var rowIdx;

function openPrintWindow(report, outputFormat)
{
  var url = "../" + report + ".rpt";
  
  if (outputFormat)
  {
	url = url + "?format=" + outputFormat;
  }
  
  // Not supported
  /*
  if (window.showModalDialog)
  {
	  window.showModalDialog(url, window, "edge:sunken");
  }
  else	
  */
  {	
	var win = window.open(url, "Print", 'menubar=yes, location=no, toolbar=no, resizable=yes, scrollbars=yes, status=yes');	
	
	if (window.print)
	{
		win.print();
	}
  }	
}

function openPrintPreviewWindow(report, outputFormat)
{
  var url = "../" + report + ".rpt";
  
  if (outputFormat)
  {
	url = url + "?format=" + outputFormat;
  }
  
  openWindow(url, "Print Preview", 'menubar=yes, location=no, toolbar=no, resizable=yes, scrollbars=yes, status=yes');	
}

function lookupOrganizations(writeTo, focusOn, filterBy)
{
  var args='pages/templates/renderer/lookup_organizations.jsp?writeTo=' + writeTo;

  if (focusOn && focusOn.trim() != "")
  {
  	args += '&focusOn=' + focusOn;
  }
  if (filterBy && filterBy != '') 
  {
  	args += '&filterBy=' + filterBy;
  }
  document.write("<img id=\'img1'\ class=\'default\' style=\'cursor: pointer; cursor:hand;' src=\'images/document_lookup.gif\' alt=\'Company Lookup\' width=\'18' height=\'18' onclick=\'openWindow(\"" + args + "\", \"\")\'>");
}

function lookupOrganization(writeTo, focusOn, filterBy, rootId)
{
  var args='pages/templates/renderer/lookup_organization.jsp?writeTo=' + writeTo;

  if (focusOn)
  {
  	args += '&focusOn=' + focusOn;
  }
  if (filterBy && filterBy != '') 
  {
  	args += '&filterBy=' + filterBy;
  }
  if (filterBy && filterBy != '') 
  {
  	args += '&filterBy=' + filterBy;
  }
  if (rootId && rootId != '') 
  {
  	args += '&rootId=' + rootId;
  }
  document.write("<img id=\'img2'\ class=\'default\' style=\'cursor: pointer; cursor:hand;' src=\'images/document_lookup.gif\' alt=\'Company Structure Lookup\' width=\'18' height=\'18' onclick=\'openWindow(\"" + args + "\", \"\")\'>");
}

function lookupTree(query, writeTo, focusOn, filterBy, title, onclick, selectParent)
{
  window.listenerAttached = false;
  var args='pages/templates/renderer/lookup_tree.jsp?query=' + query + '&writeTo=' + writeTo;

  if (focusOn)
  {
  	args += '&focusOn=' + focusOn;
  }
  if (title) 
  {
  	args += '&title=' + title;
  }
  if (selectParent) 
  {
  	args += '&parent=' + selectParent;
  }
  onclick = attachOnclick(args, onclick, filterBy);
  document.write("<img id=r\'img2'\ class=\'default\' style=\'cursor: pointer; cursor:hand;' src=\'images/document_lookup.gif\' alt=\'" + title +"\' width=\'18' height=\'18' align=\'MIDDLE' onclick=\'" + onclick + "\'>");
}

function dLookup(divId, lookupFieldId, title, query, lookFor, writeTo, lookupType, displayedColumns, focusOn, onclick, filterBy, wHeight, wWidth) {
  var lookupField = document.getElementById(lookupFieldId);
  var args='query=' + query + "&lookFor=" + lookFor + "&writeTo=" + writeTo;
  var writeTo_Array = writeTo.split(",");
  var firstField = writeTo_Array[0];
  if (filterBy)
  {
    args += '&filterBy=' + filterBy;
  }
  if (lookupType)
  {
    args += '&lookup=' + lookupType;
  } 
  if (displayedColumns)
  {
    args += '&displayedColumns=' + displayedColumns;
  } 
  if (focusOn)
  {
  	args += '&focusOn=' + focusOn;
  }
  if (title) 
  {
  	args += '&lookupTitle=' + encodeURI(title);
  }
  else {
  	title = "Lookup";
  }
  args = "performLookup?" + args;
  //currently, search2DynamicFormId, dynamicFormId(for dynamicAdd & dynamicEdit form's id) are the id for dynamic population.
  var idElement = lookupField.form.attributes['id'];
  //lookupField.form.setAttribute('id', actionElement.value + "FormId");
  parentFormId = idElement.value;// + "FormId";
  if (parentFormId === "") {
    alert("Developer Info: Please give an id for your FORM");
  }
  args += "&lookupParentFormId=" + parentFormId;
  args += "&popDivId=" + divId;
    if (!wHeight) {
        wHeight = 450;
    }
    if (!wWidth) {
        wWidth = 700;
    }
  args = attachFilterBy(args, filterBy);
//  alert(args);
//  
  $("#"+divId).load(args,
    function(message) {
        if (message === "Expired") {
            document.location = "initLogin";
        }
        $("#"+divId).find('.modal-dialog').css({
            width:'100%'
        });
        $("#"+divId).data('width', '70%');
        $('#'+divId).modal('show'); 
    });
    
//  attachLookupOnclick(query, args, onclick, filterBy, title, wHeight, wWidth);
}
function lookup(title, query, lookFor, writeTo, lookupType, displayedColumns, focusOn, onclick, filterBy, wHeight, wWidth)
{
  window.listenerAttached = false;
  var args='query=' + query + "&lookFor=" + lookFor + "&writeTo=" + writeTo;
  var writeTo_Array = writeTo.split(",");
  var firstField = writeTo_Array[0];
  if (filterBy)
  {
    args += '&filterBy=' + filterBy;
  }
  if (lookupType)
  {
    args += '&lookup=' + lookupType;
  } 
  if (displayedColumns)
  {
    args += '&displayedColumns=' + displayedColumns;
  } 
  if (focusOn)
  {
  	args += '&focusOn=' + focusOn;
  }
  if (title) 
  {
  	args += '&title=' + title;
  }
  else {
  	title = "Lookup";
  }
  args = "processLookup?" + args;
  //currently, search2DynamicFormId, dynamicFormId(for dynamicAdd & dynamicEdit form's id) are the id for dynamic population.
    if (!document.getElementById("search2DynamicFormId")) {
        if (!document.getElementById("dynamicFormId")) {
            document.write("<label id='lu_" + query + "_id\'>");
            var theParentForm = document.getElementById("lu_"+ query+"_id").parentNode;
            var counting = 0;
            while (true){
                counting++;
                if (counting > 50) {
                    break;
                }
                if (theParentForm.tagName == "FORM") {
                    var formId_value = "";
                    if (!theParentForm.attributes['id']) {
                        var actionElement = theParentForm.attributes['action'];
                        theParentForm.setAttribute('id', actionElement.value + "FormId");
                        parentFormId = actionElement.value + "FormId";
                        break;
                    }
                    var element = theParentForm.attributes['id'];
                    formId_value = element.value;
                    parentFormId = formId_value;
                    break;
                } else {
                    theParentForm = theParentForm.parentNode;
                }
            }
    } else {
        parentFormId = "dynamicFormId";
    }
  } else {parentFormId = "search2DynamicFormId";}
  if (parentFormId == "") {
    alert("Developer Info: Please give an id for your FORM");
  }
  args += "&lookupParentFormId=" + parentFormId;
    if (!wHeight) {
        wHeight = 450;
    }
    if (!wWidth) {
        wWidth = 700;
    }
  onclick = attachLookupOnclick(query, args, onclick, filterBy, title, wHeight, wWidth);
//  document.write("<img id=\'lu_"+ firstField + "\' style=\'cursor: pointer; cursor:hand;' src=\'images/document_lookup.gif\' alt=\'" + title +"\' width=\'18' height=\'18' align=\'absmiddle' onclick=\'" + onclick + "\'>");
//  document.write("<span class=\"input-group-btn\"><button type=\"button\" style=\"padding-top:9px;padding-bottom:9px;padding-left:15px;padding-right:5px\" class=\"btn btn-default lookupButton\" onclick=\'" + onclick + "\'><i class=\"fa fa-search\"></i></button></span>");
  document.write("<div class=\"input-group-addon\" ><i class=\"fa fa-search\" onclick=\'" + onclick + "\'></i></div>")
}

//added by amywyp 19-10-2017 --use for lookup at data entry forms
function lookup2(title, query, lookFor, writeTo, lookupType, displayedColumns, focusOn, onclick, filterBy, wHeight, wWidth)
{
  window.listenerAttached = false;
  var args='query=' + query + "&lookFor=" + lookFor + "&writeTo=" + writeTo;
  var writeTo_Array = writeTo.split(",");
  var firstField = writeTo_Array[0];
  if (filterBy)
  {
    args += '&filterBy=' + filterBy;
  }
  if (lookupType)
  {
    args += '&lookup=' + lookupType;
  } 
  if (displayedColumns)
  {
    args += '&displayedColumns=' + displayedColumns;
  } 
  if (focusOn)
  {
  	args += '&focusOn=' + focusOn;
  }
  if (title) 
  {
  	args += '&title=' + title;
  }
  else {
  	title = "Lookup";
  }
  args = "processLookup?" + args;
  //currently, search2DynamicFormId, dynamicFormId(for dynamicAdd & dynamicEdit form's id) are the id for dynamic population.
    if (!document.getElementById("search2DynamicFormId")) {
        if (!document.getElementById("dynamicFormId")) {
            document.write("<label id='lu_" + query + "_id\'>");
            var theParentForm = document.getElementById("lu_"+ query+"_id").parentNode;
            var counting = 0;
            while (true){
                counting++;
                if (counting > 50) {
                    break;
                }
                if (theParentForm.tagName == "FORM") {
                    var formId_value = "";
                    if (!theParentForm.attributes['id']) {
                        var actionElement = theParentForm.attributes['action'];
                        theParentForm.setAttribute('id', actionElement.value + "FormId");
                        parentFormId = actionElement.value + "FormId";
                        break;
                    }
                    var element = theParentForm.attributes['id'];
                    formId_value = element.value;
                    parentFormId = formId_value;
                    break;
                } else {
                    theParentForm = theParentForm.parentNode;
                }
            }
    } else {
        parentFormId = "dynamicFormId";
    }
  } else {parentFormId = "search2DynamicFormId";}
  if (parentFormId == "") {
    alert("Developer Info: Please give an id for your FORM");
  }
  args += "&lookupParentFormId=" + parentFormId;
    if (!wHeight) {
        wHeight = 450;
    }
    if (!wWidth) {
        wWidth = 700;
    }
  onclick = attachLookupOnclick(query, args, onclick, filterBy, title, wHeight, wWidth);
//  document.write("<img id=\'lu_"+ firstField + "\' style=\'cursor: pointer; cursor:hand;' src=\'images/document_lookup.gif\' alt=\'" + title +"\' width=\'18' height=\'18' align=\'absmiddle' onclick=\'" + onclick + "\'>");
//  document.write("<span class=\"input-group-btn\"><button type=\"button\" style=\"padding-top:9px;padding-bottom:9px;padding-left:15px;padding-right:5px\" class=\"btn btn-default lookupButton\" onclick=\'" + onclick + "\'><i class=\"fa fa-search\"></i></button></span>");
  document.write("<div style=\"cursor:pointer;\"><i class=\"fa fa-search text-green\" onclick=\'" + onclick + "\' style=\"cursor:pointer;margin-top:-5px;color:white;\"></i></div>")
}

function lookupH(title, query, lookFor, writeTo, lookupType, displayedColumns, focusOn, onclick, filterBy, wHeight, wWidth)
{
  window.listenerAttached = false;
  var args='query=' + query + "&lookFor=" + lookFor + "&writeTo=" + writeTo;

  if (filterBy)
  {
    args += '&filterBy=' + filterBy;
  }
  if (lookupType)
  {
    args += '&lookup=' + lookupType;
  }
  if (displayedColumns)
  {
    args += '&displayedColumns=' + displayedColumns;
  }
  if (focusOn)
  {
  	args += '&focusOn=' + focusOn;
  }
  if (title)
  {
  	args += '&title=' + title;
  }
  else {
  	title = "Lookup";
  }
  args = "hierarchyLookup?" + args;
  //currently, search2DynamicFormId, dynamicFormId(for dynamicAdd & dynamicEdit form's id) are the id for dynamic population.
    if (!document.getElementById("search2DynamicFormId")) {
    if (!document.getElementById("dynamicFormId")) {
            document.write("<label id='lu_" + query + "_id\'>");
            var theParentForm = document.getElementById("lu_"+ query+"_id").parentNode;
            var counting = 0;
            while (true){
                counting++;
                if (counting > 50) {
                    break;
                }
                if (theParentForm.tagName == "FORM") {
//                    if (!theParentForm.id) {
//                        theParentForm.id = theParentForm.action + "FormId";
//                    }
//                    parentFormId = theParentForm.id;
//                    break;
                    var formId_value = "";
                    if (!theParentForm.attributes['id']) {
                        var actionElement = theParentForm.attributes['action'];
                        theParentForm.setAttribute('id', actionElement.value + "FormId");
                        parentFormId = actionElement.value + "FormId";
                        break;
                    }
                    var element = theParentForm.attributes['id'];
                    formId_value = element.value;
                    parentFormId = formId_value;
                    break;
                } else {
                    theParentForm = theParentForm.parentNode;
                }
            }
    } else {
        parentFormId = "dynamicFormId";
    }
  } else {parentFormId = "search2DynamicFormId";}
  if (parentFormId == "") {
    alert("Developer Info: Please give an id for your FORM");
  }
  args += "&lookupParentFormId=" + parentFormId;
  if (!wHeight) {
    wHeight = 450;
}
if (!wWidth) {
    wWidth = 700;
}
  if (onclick) {
     if (onclick.indexOf(" ") > 0) {
         rowIdx = onclick.substring(onclick.indexOf(" ")+1);
         onclick = onclick.substring(0, onclick.indexOf(" "));
         args+="&rowIdx="+rowIdx;
         onclick = attachLookupOnclick(query, args, onclick, filterBy, title, wHeight, wWidth);
     } else {
         onclick = attachLookupOnclick(query, args, onclick, filterBy, title, wHeight, wWidth);
     }
  }
  //onclick = attachLookupOnclick(query, args, onclick, filterBy, title);
//  document.write("<img id=\'lu_"+ query + "\' style=\'cursor: pointer; cursor:hand;' src=\'images/document_lookup.gif\' alt=\'" + title +"\' width=\'18' height=\'18' align=\'absmiddle' onclick=\'" + onclick + "\'>");
  document.write("<span class=\"input-group-btn\"><button type=\"button\" style=\"padding-top:9px;padding-bottom:9px;padding-left:15px;padding-right:5px\" class=\"btn btn-default lookupButton\" onclick=\'" + onclick + "\'><i class=\"fa fa-search\"></i></button></span>");
}

//added by amywyp 19-10-2017 --use for lookup at data entry forms
function lookupH2(title, query, lookFor, writeTo, lookupType, displayedColumns, focusOn, onclick, filterBy, wHeight, wWidth)
{
  window.listenerAttached = false;
  var args='query=' + query + "&lookFor=" + lookFor + "&writeTo=" + writeTo;

  if (filterBy)
  {
    args += '&filterBy=' + filterBy;
  }
  if (lookupType)
  {
    args += '&lookup=' + lookupType;
  }
  if (displayedColumns)
  {
    args += '&displayedColumns=' + displayedColumns;
  }
  if (focusOn)
  {
  	args += '&focusOn=' + focusOn;
  }
  if (title)
  {
  	args += '&title=' + title;
  }
  else {
  	title = "Lookup";
  }
  args = "hierarchyLookup?" + args;
  //currently, search2DynamicFormId, dynamicFormId(for dynamicAdd & dynamicEdit form's id) are the id for dynamic population.
    if (!document.getElementById("search2DynamicFormId")) {
    if (!document.getElementById("dynamicFormId")) {
            document.write("<label id='lu_" + query + "_id\'>");
            var theParentForm = document.getElementById("lu_"+ query+"_id").parentNode;
            var counting = 0;
            while (true){
                counting++;
                if (counting > 50) {
                    break;
                }
                if (theParentForm.tagName == "FORM") {
//                    if (!theParentForm.id) {
//                        theParentForm.id = theParentForm.action + "FormId";
//                    }
//                    parentFormId = theParentForm.id;
//                    break;
                    var formId_value = "";
                    if (!theParentForm.attributes['id']) {
                        var actionElement = theParentForm.attributes['action'];
                        theParentForm.setAttribute('id', actionElement.value + "FormId");
                        parentFormId = actionElement.value + "FormId";
                        break;
                    }
                    var element = theParentForm.attributes['id'];
                    formId_value = element.value;
                    parentFormId = formId_value;
                    break;
                } else {
                    theParentForm = theParentForm.parentNode;
                }
            }
    } else {
        parentFormId = "dynamicFormId";
    }
  } else {parentFormId = "search2DynamicFormId";}
  if (parentFormId == "") {
    alert("Developer Info: Please give an id for your FORM");
  }
  args += "&lookupParentFormId=" + parentFormId;
  if (!wHeight) {
    wHeight = 450;
}
if (!wWidth) {
    wWidth = 700;
}
  if (onclick) {
     if (onclick.indexOf(" ") > 0) {
         rowIdx = onclick.substring(onclick.indexOf(" ")+1);
         onclick = onclick.substring(0, onclick.indexOf(" "));
         args+="&rowIdx="+rowIdx;
         onclick = attachLookupOnclick(query, args, onclick, filterBy, title, wHeight, wWidth);
     } else {
         onclick = attachLookupOnclick(query, args, onclick, filterBy, title, wHeight, wWidth);
     }
  }
  //onclick = attachLookupOnclick(query, args, onclick, filterBy, title);
//  document.write("<img id=\'lu_"+ query + "\' style=\'cursor: pointer; cursor:hand;' src=\'images/document_lookup.gif\' alt=\'" + title +"\' width=\'18' height=\'18' align=\'absmiddle' onclick=\'" + onclick + "\'>");
document.write("<div style=\"cursor:pointer;\"><i class=\"fa fa-search text-green\" onclick=\'" + onclick + "\' style=\"cursor:pointer;margin-top:-5px;color:white;\"></i></div>")
}

function openLookupWindow(args, filterBy)
{
  args = attachFilterBy(args, filterBy);
  //alert("args: " + args);
  openWindow(args, "lookup");
}

function openWindow(url, name, options)
{
  var parameters = 'left=400, top=200, width=500, height=450, menubar=no, location=no, resizable=no, scrollbars=yes, status=no, toolbar=no';

  if (options)
  {
    parameters = options;
  }
  
  // Not supported
  /*
    if (window.showModalDialog)
	{
	  window.showModalDialog(url, window, "edge:sunken");
	}
	else	
  */
	{	
		if (!window.listenerAttached) 
		{
			window.listenerAttached = true;
			attachPopupWindowListener();
		}

		hidePopupWindow();

		win = window.open(url, name, parameters);

		win.focus();	    

		if (!win.opener) 
		{
			win.opener = self;
		}	
	}
}

function attachPopupWindowListener() 
{
	if (document.layers) 
	{
		document.captureEvents(Event.MOUSEUP);
	}
	
	window.popupWindowOldEventListener = document.onmouseup;
	
	if (window.popupWindowOldEventListener != null) {
		document.onmouseup = new Function("window.popupWindowOldEventListener(); hidePopupWindow();");
	}
	else {
		document.onmouseup = hidePopupWindow;
	}
}

function hidePopupWindow() 
{	
	if (win && !win.closed) 
	{
		win.close();
		win = null;
	}
}

function printWindow()
{
  if (window.print)
  {
    window.print();
  }
  else  
  {
    var obj = '<OBJECT ID="webBrowser" WIDTH=0 HEIGHT=0 CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></OBJECT>';

    document.body.insertAdjacentHTML('beforeEnd', obj);

    webBrowser.ExecWB(6, 2);
    webBrowser.outerHTML = "";
  }
}

function changeQuery(elementId, value) {
	var element = document.all("lu_" + elementId);
	if (element != "undefined") {
		var script = element.onclick.toString();
		script = script.substring(script.indexOf("{")+1, script.lastIndexOf("}"))
		script = script.replace(script.substring(script.indexOf("query="), script.indexOf("&look")), "query=" + value);
		var func = new Function(script);
		element.onclick = func;
	}
}

function attachFilterBy(args, filterBy) {
    if (defaultFilter != "undefined") {
        if (filterBy && filterBy != '' && filterBy != "undefined") {
            filterBy += ",firstFilter_"+defaultFilter;
        } else {
            filterBy = "firstFilter_"+defaultFilter;
        }
        args+='&filterBy='+filterBy;
    }

  if (filterBy && filterBy != '' && filterBy != "undefined") {
  	args += '&filter=';
	var filterArray = filterBy.split(",");
	var value, obj;
	for (var i = 0; i < filterArray.length; i++) {
	  filterArray[i] = filterArray[i].trim();
          //obj = document.all[filterArray[i]];
          if (filterArray[i].indexOf("_as_") > 0) {
              filterArray[i] = filterArray[i].substring(0, filterArray[i].indexOf("_as_"));
          }
          if (filterArray[i].indexOf("firstFilter_") == 0) {
              obj = window.parent.document.getElementById(parentFormId)[filterArray[i].substring(12)];
          } else {
              obj = window.parent.document.getElementById(parentFormId)[filterArray[i]];
          }

	  if ((obj.id || obj.name)) {
                if (obj.value.substring(0, 1) == "?") {
                    value = obj.value.substring(1);
                } else {
                    value = obj.value;
                }
		  //value = escape(obj.value);
                  value = escape(value);
		  value = value.replace('+','%2b'); // manually escape
		  args += value;
	  } else {
	  	args += "";
	  }
	  if (i < (filterArray.length-1)) {
		args += ",,,"
	  }
	}
  }
  defaultFilter = "undefined";
  return args;
}
function divAttachFilterBy(args, filterBy) {
    if (defaultFilter != "undefined") {
        if (filterBy && filterBy != '' && filterBy != "undefined") {
            filterBy += ",firstFilter_"+defaultFilter;
        } else {
            filterBy = "firstFilter_"+defaultFilter;
        }
        args+='&filterBy='+filterBy;
    }

  if (filterBy && filterBy != '' && filterBy != "undefined") {
  	args += '&filter=';
	var filterArray = filterBy.split(",");
	var value, obj;
	for (var i = 0; i < filterArray.length; i++) {
	  filterArray[i] = filterArray[i].trim();
          //obj = document.all[filterArray[i]];
          if (filterArray[i].indexOf("_as_") > 0) {
              filterArray[i] = filterArray[i].substring(0, filterArray[i].indexOf("_as_"));
          }
          if (filterArray[i].indexOf("firstFilter_") == 0) {
              obj = document.getElementById(parentFormId)[filterArray[i].substring(12)];
          } else {
              obj = document.getElementById(parentFormId)[filterArray[i]];
          }

	  if ((obj.id || obj.name)) {
                if (obj.value.substring(0, 1) == "?") {
                    value = obj.value.substring(1);
                } else {
                    value = obj.value;
                }
		  //value = escape(obj.value);
                  value = escape(value);
		  value = value.replace('+','%2b'); // manually escape
		  args += value;
	  } else {
	  	args += "";
	  }
	  if (i < (filterArray.length-1)) {
		args += ",,,"
	  }
	}
  }
  defaultFilter = "undefined";
  return args;
}

function attachLookupOnclick(id, args, onclick, filterBy, title, wHeight, wWidth) {
	return (onclick && onclick != '') 
    ? ("if (" + onclick + ") { args = attachFilterBy(\""+args+"\",\""+ filterBy +"\"); " + " lookup_dtmlwindow=dhtmlmodal.open(\""+ id +"\", \"iframe\", args, \""+ title +"\", \"width="+wWidth+"px,height="+wHeight+"px,resize=1,scrolling=1,center=1\");} else {return false;}")
    : ("lookup_dtmlwindow=dhtmlmodal.open(\""+ id +"\", \"iframe\", \"" +args+ "\", \""+ title +"\", \"width="+wWidth+"px,height="+wHeight+"px,resize=1,scrolling=1,center=1\")");
}


function attachOnclick(args, onclick, filterBy) {
	return (onclick && onclick != '') 
	  				? ("if (" + onclick + ") {openLookupWindow(\"" + args + "\",\"" + (filterBy ? filterBy : '') + "\")} else {return false;}") 
	  				: ("openLookupWindow(\"" + args + "\",\"" + (filterBy ? filterBy : '') + "\")");
  //return (onclick && onclick != '') 
  //				? ("if (" + onclick + ") {openLookupWindow(\"" + args + "\",\"" + (filterBy ? filterBy : '') + "\")} else {return false;}") 
  //				: ("openLookupWindow(\"" + args + "\",\"" + (filterBy ? filterBy : '') + "\")");
}

/**
 * trigger the lookup button
 */
function triggerLookup(imageId, field, defaultSearching) {
    if (field.value.substring(0, 1) == "?") {
        defaultFilter = defaultSearching
        var imageLink = document.getElementById(imageId);
        if(document.dispatchEvent) { // W3C
            var oEvent = document.createEvent( "MouseEvents" );
            oEvent.initMouseEvent("click", true, true,window, 1, 1, 1, 1, 1, false, false, false, false, 0, imageLink);
            imageLink.dispatchEvent( oEvent );
        } else {
            imageLink.fireEvent("onclick");
        }
        field.value = "";
        return true;
    } else {
        return false;
    }
//    alert(imageLink);
//    imageLink.click();
}
