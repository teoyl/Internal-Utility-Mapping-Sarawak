/*
 * This Variable added by TTH @ 28-June-2010
 * To stopy the AutoHeight Dynamically
 * */
var stopAutoHeight = "";

function doIframe(){
	o = document.getElementsByTagName('iframe');
	for(i=0;i<o.length;i++){
		if (/\bautoHeight\b/.test(o[i].className)){
			if (stopAutoHeight.indexOf(o[i].id) >= 0) continue;
			setHeight(o[i]);
			addEvent(o[i],'load', doIframe);
		}
	}
	
	stopAutoHeight = "";  // Added by TTH @ 28-June-2010
}

function setHeight(e){
	if(e.contentDocument){
		e.height = e.contentDocument.body.offsetHeight + 35;
	} else {
		e.height = e.contentWindow.document.body.scrollHeight;
	}
}

function addEvent(obj, evType, fn){
	if(obj.addEventListener)
	{
	obj.addEventListener(evType, fn,false);
	return true;
	} else if (obj.attachEvent){
	var r = obj.attachEvent("on"+evType, fn);
	return r;
	} else {
	return false;
	}
}

if (document.getElementById && document.createTextNode){
 addEvent(window,'load', doIframe);	
}
