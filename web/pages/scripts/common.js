(function ($) {
        $.fn.inputFilter = function (inputFilter) {
            return this.on("input keydown keyup mousedown mouseup select contextmenu drop", function () {
                if (inputFilter(this.value)) {
                    this.oldValue = this.value;
                    this.oldSelectionStart = this.selectionStart;
                    this.oldSelectionEnd = this.selectionEnd;
                } else if (this.hasOwnProperty("oldValue")) {
                    this.value = this.oldValue;
                    this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
                }
            });
        };
    }(jQuery));
    //Sample return : to control the input value
//        $("#inputId").inputFilter(function(value) {
//            return /^\d*$/.test(value);    // Allow digits only, using a RegExp
//        });
//        
//    return /^-?\d*$/.test(value);                // Integer
//    return /^\d*$/.test(value);                  // Integer >= 0
//    return /^\d*$/.test(value) &&                // Integer >= 0 and <= 500
//      (value === "" || parseFloat(value) <= 500);
//    return /^-?\d*[.,]?\d*$/.test(value);        // Float (accept both '.' and ',' as decimal separator)
//    return /^-?\d*[.,]?\d{0,2}$/.test(value);    // Currency (i.e. at most two decimal places)
//    return /^[a-z]*$/i.test(value);              // A-Z only
//    return /^[0-9a-f]*$/i.test(value);           // Hexadecimal

function focusFirstColumn() {
    var bFound = false;
    for (f=0; f < document.forms.length; f++) {
        for(i=0; i < document.forms[f].length; i++) {
            if (document.forms[f][i].type != "hidden") {
                if (document.forms[f][i].type != "button" && document.forms[f][i].type != "submit") {
                    if (document.forms[f][i].disabled != true) {
                        document.forms[f][i].focus();
                        bFound = true;
                    }
                }
            }
            if (bFound == true) break;
        }
        if (bFound == true) break;
    }
}
/**
 * scroll to the given id
 */
function scroll_to_ID(theID) {
    var theElement = document.getElementById(theID);
//    theElement.scrollIntoView();
    if (theElement) {
        var selectedPosX = 0;
        var selectedPosY = 0;
        while(theElement != null){
            selectedPosX += theElement.offsetLeft;
            selectedPosY += theElement.offsetTop;
            theElement = theElement.offsetParent;
        }
        setTimeout(
        function() 
        {
            window.scrollTo(selectedPosX,selectedPosY);
        }, 300);
    }
}
function scroll_to_ID_2(theID) {
    var theElement = document.getElementById(theID);
//    theElement.scrollIntoView();
    if (theElement) {
        var selectedPosX = 0;
        var selectedPosY = 0;
        while(theElement != null){
            selectedPosX += theElement.offsetLeft;
            selectedPosY += theElement.offsetTop;
            theElement = theElement.offsetParent;
        }
        setTimeout(
        function() 
        {
            selectedPosY -= 100;
            window.scrollTo(selectedPosX,selectedPosY);
        }, 300);
    }
}
//ThoTH @ 10-Jan-2014 :: For _LBA
function submitToLBA(form, methodAction) {
    var element = form.attributes['action'];
    element.value = methodAction;
//    form.action = methodAction;

    return true;
}

/**wongkk4@16Apr2014
 *Only allow user to clear textbox value
 *eg .onkeypress="return clearKey(event)"
 ***/
function clearKey(event){

    var key = event.keyCode || event.charCode;
    if( key == 8 || key == 46  || key == 9 ){//9 = tab

        return true;
    }
    return false;
}

function resetForm(theform) {
    var noOfElements = theform.elements.length;

    for (var i = 0; i < noOfElements; i++) {
        if (theform.elements[i].type != "hidden")
            clearValue(theform.elements[i]);
    }
    return false;
}

function disableForm(theform) {
    var noOfElements = theform.elements.length;

    for (var i = 0; i < noOfElements; i++) {
        if (theform.elements[i].type != "hidden") {
            disableObject(theform.elements[i]);
        }
    }
    return false;
}

function disableFormEditable(theform) {
    var noOfElements = theform.elements.length;

    for (var i = 0; i < noOfElements; i++) {
        if (theform.elements[i].type != "hidden") {
            if (theform.elements[i].type == "submit" || theform.elements[i].type == "button"){
                
            } else {
                if (theform.elements[i].id.indexOf("lu_") == 0) {
                    if (theform.elements[i].id.indexOf("lu_") == 0) {
                        theform.elements[i].innerHTML = "";
                    }
                } else {
                    theform.elements[i].disabled = true;
                }
            }
        }
    }
    var elements = document.getElementsByTagName("label");
    for (var ii = 0; ii < elements.length; ii++) {
        if (elements.item(ii).id.indexOf("lu_", 0) == 0) {
            elements.item(ii).innerHTML = "";
        }
    }
    // ThoTH @ 27-Jun-2012 : to hide Calendar Button
//    elements = document.getElementsByTagName("img");
    elements = theform.getElementsByTagName("img"); // Edited by Delvene @ 09-Sep-2014 :: To prevent calendar in other form under same document to be disabled
    for (var iii = 0; iii < elements.length; iii++) {
        if (elements.item(iii).title == "Calendar") {
            elements.item(iii).style.display = "none";
        }
    }
    return false;
}

// ThoTH @ 19-Nov-2015 :: Just reverse teh disableFormEditable, not properly tested
function enableFormEditable(theform) {
    var noOfElements = theform.elements.length;

    for (var i = 0; i < noOfElements; i++) {
        if (theform.elements[i].type != "hidden") {
            if (theform.elements[i].type == "submit" || theform.elements[i].type == "button"){
                
            } else {
//                if (theform.elements[i].id.indexOf("lu_") == 0) {
//                    if (theform.elements[i].id.indexOf("lu_") == 0) {
//                        theform.elements[i].innerHTML = "";
//                    }
//                } else {
                    theform.elements[i].disabled = false;
//                }
            }
        }
    }
//    var elements = document.getElementsByTagName("label");
//    for (var ii = 0; ii < elements.length; ii++) {
//        if (elements.item(ii).id.indexOf("lu_", 0) == 0) {
//            elements.item(ii).innerHTML = "";
//        }
//    }
    // ThoTH @ 27-Jun-2012 : to hide Calendar Button
//    elements = document.getElementsByTagName("img");
    elements = theform.getElementsByTagName("img"); // Edited by Delvene @ 09-Sep-2014 :: To prevent calendar in other form under same document to be disabled
    for (var iii = 0; iii < elements.length; iii++) {
        if (elements.item(iii).title == "Calendar") {
            elements.item(iii).style.display = "inline";
        }
    }
    return false;
}

function closePage() {
    window.close();
    return false;
}

function printPage() {
    window.print();
    return false;
}

//detect browser
IS_DOM = (document.getElementById) ? true : false;
IS_NS4 = (document.layers) ? true : false;
IS_IE  = (document.all) ? true : false;
IS_IE4 = IS_IE && !IS_DOM;
IS_Mac = (navigator.appVersion.indexOf("Mac") != -1);
IS_IE4Mac = IS_IE4 && IS_Mac;
IS_Opera  = (window.opera) ? true : false;
IS_Konqueror = (navigator.userAgent.indexOf("Konqueror")!=-1);
IS_Safari = (IS_DOM &&
    parseInt(navigator.productSub)>=20020000 &&
    navigator.vendor.indexOf("Apple Computer")!=-1);
IS_NS6 = (navigator.product=="Gecko" || IS_Safari);

//Retrieves the object of the given name.
function getObject(name) {
    return getWindowObject(this.window, name);
}

//Retrives the object of the given name and windows.
function getWindowObject(win, name) {
    if (IS_IE4) {
        return (win.document.all[name]);
    }
    else if (IS_NS6) {
        o = win.document.getElementById(name);
        if (o == null) {
            for(i=0; i<document.forms.length; i++) {
                f = document.forms[i][name];
                if (f != null) {
                    o = f;
                    break;
                }
            }
        }
        return o;
    }
    else if (IS_DOM) {
        return (win.document.getElementById(name));
    }
    else if (IS_NS4) {
        return (win.document.layers[name]);
    }
}

//Retrives the field object of the given form and field.
function getFormObject(form, field) {
    if (form.elements) {
        return form.elements[field]
    }
    else {
        return (document.forms[form].elements[field]);
    }
}

//Retrieves the value of the given object.
function getValue(obj) {
    var value = '';
    if (obj.form) {
        if (obj.type == "select-one") {
            var si = obj.selectedIndex;
            if (si >= 0) {
                value = obj.options[si].value;
            }
        }
        else if (obj.type == "select-multiple") {
            for(i=0; i<obj.length; i++) {
                if (obj[i].selected) {
                    if (value.length > 0) value += ";";
                    value += obj[i].value;
                }
            }
        }
        else if (obj.type == "radio") {
            var field = obj.form[obj.name];
            for(i=0; i<field.length; i++) {
                if (field[i].checked) {
                    value = field[i].value;
                    break;
                }
            }
        }
        else if (obj.type == "checkbox") {
            var field = obj.form[obj.name];
            for(i=0; i<field.length; i++) {
                if (field[i].checked) {
                    if (value.length > 0) value += ";";
                    value += field[i].value;
                }
            }
        }
        else if (typeof(obj.type) == "undefined") {
            if (!(isNaN(obj.length))) {
                for(i=0; i<obj.length; i++) {
                    if (obj[i].checked) {
                        if (value.length > 0) value += ";";
                        value += obj[i].value;
                    }
                }
            }
        }
        else {
            value = obj.value;
        }
    }
    else {
        value = obj.innerHTML;
    }
    return value;
}

//Sets the value of the given object.
function setValue(obj, value) {
    if (obj.form) {
        if (obj.type == "select-one") {
            for(i=0; i<obj.length; i++) {
                if (obj.options[i].value == value) {
                    obj.options[i].selected = true;
                    break;
                }
            }
        }
        else if (obj.type == "select-multiple") {
            for(i=0; i<obj.length; i++) {
                if (obj.options[i].value == value) {
                    obj.options[i].selected = true;
                }
            }
        }
        else if (obj.type == "radio" || obj.type == "checkbox") {
            var field = obj.form[obj.name];
            for(i=0; i<field.length; i++) {
                if (field[i].value == value) {
                    field[i].checked = true;
                    break;
                }
            }
        }
        else if (typeof(obj.type) == "undefined") {
            if (!(isNaN(obj.length))) {
                for(i=0; i<obj.length; i++) {
                    if (obj[i].value == value) {
                        obj[i].checked = true;
                        break;
                    }
                }
            }
        }
        else {
            obj.value = value;
        }
    }
    else {
        obj.innerHTML = value;
    }
}

//Reset object value to default.
function resetValue(obj) {
    if (obj.form) {
        if (obj.type == "select-one" || obj.type == "select-multiple") {
            for(i=0; i<obj.length; i++) {
                obj.options[i].selected = false;
                if (obj.options[i].defaultSelected) {
                    obj.options[i].selected = true;
                }
            }
        }
        else if (obj.type == "radio" || obj.type == "checkbox") {
            var field = obj.form[obj.name];
            for(i=0; i<field.length; i++) {
                field[i].checked = false;
                if (field[i].defaultChecked) {
                    field[i].checked = true;
                    if (obj.type == "radio") break;
                }
            }
        }
        else {
            obj.value = obj.defaultValue;
        }
    }
    else if (!isNaN(obj.length)) {
        for(i=0; i<obj.length; i++) {
            obj[i].checked = false;
            if (obj[i].defaultChecked) {
                obj[i].checked = true;
                if (obj[i].type == "radio") break;
            }
        }
    }
}

function clearValue(obj) {
    if (obj.name == 'mainPageSize') {
        return;
    }
    if (obj.form) {
        if (obj.type == "select-one" || obj.type == "select-multiple") {
            obj.options.selectedIndex = 0;
        }
        else if (obj.type == "radio" || obj.type == "checkbox") {
            var field = obj.form[obj.name];
            if (typeof(field.length) == "undefined") {
                field.checked = false;
            }
            else {
                for(i=0; i<field.length; i++) {
                    field[i].checked = false;
                }
            }
        }
        else {
            obj.value = "";
        }
    }
    else if (!isNaN(obj.length)) {
        for(i=0; i<obj.length; i++) {
            obj[i].checked = false;
        }
    }
    else {
        obj.innerHTML == "";
    }
}

//Disable form object.
function disableObject(obj) {
    if (obj.form) {
        if (obj.type == "radio" || obj.type == "checkbox") {
            var field = obj.form[obj.name];
            for(i=0; i<field.length; i++) {
                field[i].disabled = true;
            }
        }
        else {
            obj.disabled = true;
        }
    }
    else if (!isNaN(obj.length)) {
        for(i=0; i<obj.length; i++) {
            obj[i].disabled = true;
        }
    }
}

//Enable form object.
function enableObject(obj) {
    if (obj.form) {
        if (obj.type == "radio" || obj.type == "checkbox") {
            var field = obj.form[obj.name];
            for(i=0; i<field.length; i++) {
                field[i].disabled = false;
            }
        }
        else {
            obj.disabled = false;
        }
    }
    else if (!isNaN(obj.length)) {
        for(i=0; i<obj.length; i++) {
            obj[i].disabled = false;
        }
    }
}

//Sets object to hidden.
function hideObject(obj) {
    if (IS_IE) {
        obj.style.display = "none";
    } else {
        obj.style.visibility = "hidden";
        obj.style.position = "absolute";
    }
}

//Sets object to visible.
function showObject(obj) {
    if (IS_IE) {
        obj.style.display = "inline";
    } else {
        obj.style.position = "static";
        obj.style.visibility = "visible";
    }
}

//open popup window
function openWin(url, width, height) {
    var top = (screen.height) ? (screen.height - height)/2 : 0;
    var left = (screen.width) ? (screen.width - width)/2 : 0;
    return window.open(url, 'win', 'width=' + width + ',height=' + height + ',top=' + top + ',left=' + left + ',resizable=1,scrollbars=1');
}

//close popup window
function closeWin(win) {
    win.close();
}

//set string to lower case.
function toLowerCase(field) {
    field.value = field.value.toLowerCase();
}

//set string to upper case.
function toUpperCase(field) {
    field.value = field.value.toUpperCase();
}

// Sets the maximum length of the field.
// @handleEvent onkeypress, onblur
function maxLength(field, len) {
    if (field.value.length > len) {
        field.value = field.value.substring(0, len);
        return false;
    }
    return true;
}

/*
 * To select or deselect all the item of the multilist.
 * @param arg0 a checkbox
 * @param arg1 a multilist that to be set
 */
function toggleCheckbox(arg0, arg1) {
    if (typeof(arg1) != "undefined") {
        if (typeof(arg1.type) == "string") {
            if (!arg1.disabled) {
                arg1.checked = arg0.checked;
            }
        } else {
            for (var i=0; i<arg1.length; i++) {
                if (!arg1[i].disabled) {
                    arg1[i].checked = arg0.checked;
                }
            }
        }
    }
}

/*
 * To select or deselect the toggle selection checkbox
 * @param arg0 the toggle checkbox
 * @param arg1 the multilist that toggle the checkbox
 */
function checkToggleCheckbox(obj0, obj1) {
    if (obj1) {
        if (typeof(obj1.type) == "string") {
            obj0.checked = obj1.checked
            obj0.disabled = obj1.disabled
        } else {
            var same = true;
            var disableCnt = 0;
            for (var i=0; i < obj1.length; i++) {
                if (i < obj1.length-1) {
                    if (obj1[i].checked != obj1[i+1].checked) {
                        same = false;
                        break;
                    }
                }
                if (obj1[i].disabled) disableCnt++;
            }
            if (same) {
                obj0.checked = obj1[0].checked
            } else {
                obj0.checked = false;
            }
            if (disableCnt == obj1.length) {
                obj0.disabled = true;
            }
        }
    }
}

/*
 * To set the focus to the first non-hidden element of the given form
 * @param theform The form object
 */
function setFocus(theform) {
    var noOfElements = theform.elements.length;

    for (var i = 0; i < noOfElements; i++) {
        if (theform.elements[i].type != "hidden"
            && !theform.elements[i].disabled) {
            if (theform.elements[i].type == 'text') {
                theform.elements[i].select();
            }
            theform.elements[i].focus();
            break;
        }
    }
}

Array.prototype.inArray = function (value) {
    var i;
    for (i=0; i < this.length; i++) {
        if (this[i] == value) {
            return true;
        }
    }
    return false;
};

// Array.indexOf( value, begin, strict ) - Return index of the first element that matches value
Array.prototype.indexOf = function(v, b, s) {
    for( var i = +b || 0, l = this.length; i < l; i++ ) {
        if( this[i]===v || s && this[i]==v ) {
            return i;
        }
    }
    return -1;
};

Array.prototype.add = function(n) {
    for (var i = 0; i < n.length; i++) {
        this[this.length] = n[i];
    }
}

/*
 * Control the behaviour and look of the images and checkboxes of the approval actions on the header
 */
function ctrlApproval(form, objs) {
    var objs = ["approve","recall", "reject", "return", "verify"];
    var images = ["document_{0}_16.gif", "document_{0}_16_disable.gif"];
    for (var i=0; i < objs.length; i++) {
        var list = document.all[objs[i]+'Ids'];
        var chk = document.all[objs[i]+'chk'];
        var img = document.all[objs[i]+'Button'];

        if ((list != "undefined")
            && (typeof(chk) != "undefined")) {

            chk.disabled = true;
            img.disabled = true;
            img.src = img.src.substring(0,img.src.lastIndexOf("/") + 1) + formatText(images[1], objs[i]);
            if (typeof(list.type) == "string") { // one element only
                isDisabled = list.disabled;
            }
            else {
                for (var j=0; j<list.length; j++) {
                    if (!list[j].disabled) {
                        chk.disabled = false;
                        img.disabled = false;
                        img.src = img.src.substring(0,img.src.lastIndexOf("/") + 1) + formatText(images[0], objs[i]);
                        break;
                    }
                }
            }
        }
    }
}

// customised function
// control the action of changing page size options
function goSearchList(form) {
    form.submit();
}

//From eKPI
//from DD-MM-YYYY to YYYY-MM-DD
function fnCvtDBDate(str) {
    var jsNew = "";
    if (str != "") {
        var jsDay   = str.substring(0,2);
        var jsMonth = str.substring(3,5);
        var jsYear  = str.substring(6,10);

        var jsNew = jsYear + "-" + jsMonth + "-" + jsDay;
        return jsNew;
    } else {
        return jsNew;
    }
}

/*
To control user input, only allow digit and decimal
checkNumberDec(obj, event, maxNumber, maxDecimal )
@param obj = object
@param event = keypress event
@param maxNumber = Max no. of Digits before Decimal Point
@param maxDecimal = Max no. of Decimal Points
return true or false

how to call
onkeypress="return checkNumberDec(this,event,5,2);

*/
function checkNumberDec(obj, event, maxNumber, maxDecimal ){
    var liDot1;
    var lsSub;
    var sel, rng, r2, li=-1;
    var keynum;
    var liStart=0;
    var liEnd=0;
    var lsItem;
    var lsMinus;

    if(window.event) // IE
        {
            if (event.ctrlKey && (event.keyCode==86 || event.keyCode==118) ||
                (event.ctrlKey && (event.keyCode==69 || event.keyCode==99)) 
                || (event.which==9)){ //wongkk4@11Oct2017 - allow Tab(9)){
                return true;
            }
        }
        else if(event.which) // Netscape/Firefox/Opera
        {
            if ((event.ctrlKey && (event.which==86 || event.which==118)) ||
                (event.ctrlKey && (event.which==69 || event.which==99)) 
                || (event.which==9)){ //wongkk4@11Oct2017 - allow Tab(9)
                return true;
            }
        }

    //get cursor position in textbox for FireFox
    if(typeof obj.selectionStart=="number") {
        liStart=obj.selectionStart;
        liEnd =obj.selectionEnd;
    }
    //get cursor position in textbox for ie 6
    else if(document.selection && obj.createTextRange) {

        sel=document.selection;
        if(sel){
            r2=sel.createRange();
            rng=obj.createTextRange();
            rng.setEndPoint("EndToStart", r2);
            liStart = rng.text.length;
            liEnd = liStart + r2.text.length;
        //alert("Text="+liStart+"end"+liEnd);
        }
    }
    //  else {
    //  obj.onkeyup=null;
    //  obj.onclick=null;
    //  }

    //get keycode
    //alert(isNumberKey(event) );
    //alert( event.which );
    var valid = false;
    if (maxDecimal > 0) {
        valid = isNumberKey(event);
    } else {
        valid = isNumberKey(event, 'N', 'N');
    }

    if (valid){
        //check max digit before and after .
        if(window.event) // IE
        {
            keynum = event.keyCode;
        }
        else if(event.which) // Netscape/Firefox/Opera
        {
            keynum = event.which;
        }

        //no checking if user press backspace or delete
//        if (!(keynum >=48 && keynum <=57) && !(keynum==46)&& !(keynum==45)){
        if ((keynum==8) || (keynum==46)){
            return true;
        }

        lsItem = obj.value;
        //check for minus(-)
        liMinus = lsItem.indexOf('-');
        //invalid format - more than 1 '-'
        if (liMinus >= 0 && keynum==45){
            //alert("More than 1 Decimal Point detected");
            return false;
        }
        //invalid format '-' only can be at 0
        if (liStart > 0 && keynum==45){
            //alert("More than 1 Decimal Point detected");
            return false;
        }

        //alert("Start "+liStart +" End "+liEnd);

        liDot1 = lsItem.indexOf('.');
        //invalid format - more than 1 '.'
        if (liDot1 >= 0 && keynum==46){
            //alert("More than 1 Decimal Point detected");
            return false;
        }
        if (liStart != liEnd){
            //alert("liStart "+liStart+" liEnd "+ liEnd);
            return true;
        }
        //decimal point already exist
        if (liDot1 >= 0 ){
            lsSub = lsItem.substring(liDot1 +1);
            //check no. of digits after decimal
            if (lsSub.length >=maxDecimal && liStart > liDot1 ){
                //alert("Maximum 2 Decimal Points");
                return false;

            }
            //check no of digits before decimal
            lsSub = lsItem.substring(0, liDot1);

            if (lsSub.length >= maxNumber && liStart <= liDot1 ){
                //allow user to enter 9 digits & minus(-)
                if(liMinus == 0 && lsSub.length == maxNumber ){
                    return true;
                }else{
                    //allow user to enter minus(-) when reach maximum digits
                    if((keynum==45)){
                        return true;
                    }else{
                        return false;
                    }
                }
            }else{
        //alert("Decimal"+liStart +"vs" +liDot1);
        }



        }else{
            if(lsItem.length >= maxNumber && !(keynum==46)){
                //allow user to enter maximum digits & minus(-)
                if(liMinus == 0 && lsItem.length == maxNumber ){
                    return true;
                }else{
                    //allow user to enter minus(-) when reach maximum digits
                    if((keynum==45)){
                        return true;
                    }else{
                        return false;
                    }
                }
            }// if(lsItem.length >= maxNumber ){
            else if(keynum==46){
                //alert("liStart "+ liStart + " lsItem.length "+ lsItem.length);
                //not allow user to enter . when decemal point is more than max
                if( (lsItem.length - liStart) > maxDecimal ){
                    return false;
                }
            }
        }

    }else{
        return false;
    }
    return true;
}

/* added @18.7.2012 ai min*/
function numeralsOnly(evt) {
    evt = (evt) ? evt : event;
    var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode :
        ((evt.which) ? evt.which : 0));
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        alert("Nombor sahaja yang diterima.");
        return false;
    }
    return true;
}


/**
 * Clear Forms or Inputs
 *
 * How to call from page:
 * $('form').clearForm()   --> Form(s)
 * $(':input').clearForm() --> Input(s)
 *
 * NOTE:::
 * If just want to reset the form back to initial state, use this instead:
 * $("form")[0].reset();
 *
 * copied from internet by : thoth 27/05/2009
 */
/*
jQuery.fn.clearForm = function() {
  return this.each(function() {
    var type = this.type, tag = this.tagName.toLowerCase();
    if (tag == 'form')
      return $(':input',this).clearForm();
    if (type == 'text' || type == 'password' || tag == 'textarea')
      this.value = '';
    else if (type == 'checkbox' || type == 'radio')
      this.checked = false;
    else if (tag == 'select')
      this.selectedIndex = -1;
  });
};
*/
/**
 * Checks/unchecks all tables
 *
 * @param   string   the form name
 * @param   boolean  whether to check or to uncheck the element
 *
 * @return  boolean  always true
 */
function setCheckboxes(the_form, do_check)
{
    var elts      = (typeof(document.forms[the_form].elements['selected_db[]']) != 'undefined')
    ? document.forms[the_form].elements['selected_db[]']
    : (typeof(document.forms[the_form].elements['selected_tbl[]']) != 'undefined')
    ? document.forms[the_form].elements['selected_tbl[]']
    : document.forms[the_form].elements['selected_fld[]']
    ? document.forms[the_form].elements['select']
    : document.forms[the_form].elements['select'];

    var elts_cnt  = (typeof(elts.length) != 'undefined')
    ? elts.length
    : 0;

    var lbStat;

    if (elts_cnt) {
        for (var i = 0; i < elts_cnt; i++) {
            lbStat = elts[i].disabled;
            if (!lbStat) {
                elts[i].checked = do_check;
            }
        } // end for
    } else {
        lbStat = elts.disabled;
        if (!lbStat) {
            elts.checked = do_check;
        }
    } // end if... else

    return true;
} // end of the 'setCheckboxes()' function

/**
 * Generate a Random ID based on length specified by user
 *
 * @param   integer   id length
 *
 * @return  string  generated id
 *
 * created by : WongKK 20/03/2009
 */
function generateID(intlength)
{
    var idchars = "abcdefhjmnpqrstuvwxyz23456789ABCDEFGHJKLMNPQRSTUVWYXZ.,:";
    var idlength = intlength;
    var id = '';

    for (i=0;i<idlength;i++)
    {
        id+=idchars.charAt(Math.floor(Math.random()*idchars.length))
    }


    return id;
}

// Common Function::
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

// Common Function:: Return the value inside the tag
function parse_xml(str, tag){
    var jsTagLen = tag.length;
    var jsEndTag = tag.replace(/</,"</");
    var jsPos = str.indexOf(tag) + jsTagLen;

    var jsResult = str.substring(jsPos, str.indexOf(jsEndTag));

    return jsResult;
}

// Only allow to enter numbers ('.' and '-' if not set is allow, else Y/y = allow)
function isNumberKey(e, allowDot, allowMinus) {
    var keynum;
    var keychar;
    var numcheck;

    if (allowDot === undefined) {
        allowDot = true;
    } else {
        if (allowDot === "Y" || allowDot === "y") {
            allowDot = true;
        } else {
            allowDot = false;
        }
    }

    if (allowMinus === undefined) {
        allowMinus = true;
    } else {
        if (allowMinus === "Y" || allowMinus === "y") {
            allowMinus = true;
        } else {
            allowMinus = false;
        }
    }

    if(window.event) // IE
    {
        keynum = e.keyCode;
    }
    else if(e.which) // Netscape/Firefox/Opera
    {
        keynum = e.which;
    }

    //only allow to enter number and . and '-'
    if ((allowMinus && (keynum==109 || keynum===173) ) || (allowDot && (keynum==110||keynum==190)) || (keynum >=96 && keynum <=105) || (keynum >=48 && keynum <=57) || (keynum==46)||(keynum==13) || (keynum==8) || keynum==undefined || (keynum==45) ){
        return true;
    }else{

        return false;
    }
}

function checkEnterKey(event)
{
    var code = event.keyCode;
    if (code == 13)  
    {
        return false;
    }   
}

//vldt Login ID/User Name ======================================================
function vldtLoginId(strString, strFieldLabel, fldFocus, strMinChar, strMaxChar, strNull) {

    if (!chkNull(strString, strNull)) {
        alert(strFieldLabel + " must be filled.");
        fldFocus.focus();
        return false;
    }

    if (strString != "") {
        if (!chkStringRange(strString, strMinChar, strMaxChar)) {
            alert(strFieldLabel + " should not exceed " + strMaxChar + " characters or less than " + strMinChar + " characters.");
            fldFocus.focus();
            return false;
        }

        if (!chkInvalidChar1(strString)) {
            //    alert("You have entered an invalid symbol \""+sChar+"\". Please re-enter password.");
            alert(strFieldLabel + " should not contain invalid characters.");
            fldFocus.focus();
            return false;
        }
    }



    return true;
}

//vldt Password ================================================================
function vldtPassword(strString, strFieldLabel, fldFocus, strMinChar, strMaxChar, strNull, strAlphaNumeric) {

    if (!chkNull(strString, strNull)) {
//        alert(strFieldLabel + " must be filled.");
        alert(strFieldLabel + " wajib diisi.");
        fldFocus.focus();
        return false;
    }

    if (strString != "") {
        if (!chkStringRange(strString, strMinChar, strMaxChar)) {
            alert(strFieldLabel + " should not exceed " + strMaxChar + " characters or less than " + strMinChar + " characters.");
//            alert(strFieldLabel + " tidak boleh melibihi " + strMaxChar + " aksara atau kurang daripada " + strMinChar + " aksara.");
            fldFocus.focus();
            return false;
        }

        //if (!chkInvalidChar1(strString)) {  //## Commented by TTH, becos some user's password consists of char like $, @
        if (!chkInvalidChar2(strString)) {
            alert(strFieldLabel + " should not contain invalid characters.");
//            alert(strFieldLabel + " tidak boleh mengandungi aksara tidak sah.");
            fldFocus.focus();
            return false;
        }

        if (!chkAlphaNumeric(strString, strAlphaNumeric)) {
            alert(strFieldLabel + " must be in alphanumeric.");
//            alert(strFieldLabel + " mestilah dalam format abjad angka.");
            fldFocus.focus();
            return false;
        }
    }

    return true;
}

//vldt String ==================================================================
function vldtString(strString, strFieldLabel, fldFocus, strMinChar, strMaxChar, strNull) {

    if (!chkNull(strString, strNull)) {
        alert(strFieldLabel + " must be filled.");
        if (fldFocus != "") {
            fldFocus.focus();
        }
        return false;
    }

    if (strString != "") {
        if (!chkStringRange(strString, strMinChar, strMaxChar)) {
            alert(strFieldLabel + " should not exceed " + strMaxChar + " characters or less than " + strMinChar + " characters.");
            if (fldFocus != "") {
                fldFocus.focus();
            }
            return false;
        }

        if (!chkInvalidChar2(strString)) {
            alert(strFieldLabel + " should not contain invalid characters.");
            if (fldFocus != "") {
                fldFocus.focus();
            }
            return false;
        }
    }

    return true;
}

//vldt Qty1 ====================================================================
function vldtQty1(strString, strFieldLabel, fldFocus, strMaxChar, strStartChar, strEndChar, strNull) {

    if (!chkNull(strString, strNull)) {
        alert(strFieldLabel + " must be filled.");
        fldFocus.focus();
        return false;
    }

    if (strString != "") {
        if (!chkMaxLength(strString, strMaxChar)) {
            alert(strFieldLabel + " should not exceed " + strMaxChar + " characters.");
            fldFocus.focus();
            return false;
        }

        if (!chkNoRange(strString, strStartChar, strEndChar)) {
            alert(strFieldLabel + " should be in " + strStartChar + " - " + strEndChar + " range.");
            fldFocus.focus();
            return false;
        }

        if (!chkInvalidChar3(strString)) {
            alert(strFieldLabel + " should not contain invalid characters.");
            fldFocus.focus();
            return false;
        }
    }

    return true;
}

//vldt Qty2 ====================================================================
function vldtQty2(strString1, strString2, strFieldLabel1, strFieldLabel2, fldFocus) {

    if (strString1 != "" && strString2 != "") {
        if (parseInt(strString1) > parseInt(strString2)) {
            alert(strFieldLabel1 + " should not exceed " + strFieldLabel2 + ".");
            fldFocus.focus();
            return false;
        }
    }

    return true;
}

//vldt Phone No ================================================================
function vldtPhoneNo(strString, strFieldLabel, fldFocus, strMinChar, strMaxChar, strNull) {

    if (!chkNull(strString, strNull)) {
        alert(strFieldLabel + " must be filled.");
        fldFocus.focus();
        return false;
    }

    if (strString != "") {
        if (!chkStringRange(strString, strMinChar, strMaxChar)) {
//            alert(strFieldLabel + " should not exceed " + strMaxChar + " characters or less than " + strMinChar + " characters.");
            alert(strFieldLabel + " tidak patut melebihi " + strMaxChar + " aksara atau kurang daripada " + strMinChar + " aksara.");
            fldFocus.focus();
            return false;
        }

        if (!chkInvalidChar4(strString)) {
//            alert(strFieldLabel + " should not contain invalid characters.");
            alert(strFieldLabel + " tidak patut mengandungi aksara tidak sah.");
            fldFocus.focus();
            return false;
        }
    }

    return true;
}

//vldt Email ===================================================================
function vldtEmail(strString, strFieldLabel, fldFocus, strMinChar, strMaxChar, strNull) {

    if (!chkNull(strString, strNull)) {
        alert(strFieldLabel + " must be filled.");
        fldFocus.focus();
        return false;
    }

    if (strString != "") {
        if (!chkStringRange(strString, strMinChar, strMaxChar)) {
//            alert(strFieldLabel + " should not exceed " + strMaxChar + " characters or less than " + strMinChar + " characters.");
            alert(strFieldLabel + " tidak patut melebihi " + strMaxChar + " aksara atau kurang daripada " + strMinChar + " aksara.");
            fldFocus.focus();
            return false;
        }

        if (!chkInvalidChar2(strString)) {
//            alert(strFieldLabel + " should not contain invalid characters.");
            alert(strFieldLabel + " tidak patut mengandungi aksara tidak sah.");
            fldFocus.focus();
            return false;
        }

        if (!chkInvalidChar5(strString)) {
//            alert("Invalid " + strFieldLabel + " format.");
            alert("Format " + strFieldLabel + " tidak sah.");
            fldFocus.focus();
            return false;
        }
    }

    return true;
}

//vldt Checkbox ================================================================
function vldtCheckbox(strString, strFieldLabel, strNull) {

    if (!chkNull(strString, strNull)) {
        alert(strFieldLabel + " must be filled.");
        return false;
    }

    return true;
}

//vldt Radio ===================================================================
function vldtRadio(strString, strFieldLabel, strNull) {

    if (!chkNull(strString, strNull)) {
        alert(strFieldLabel + " must be filled.");
        return false;
    }

    return true;
}

//vldt Select ==================================================================
function vldtSelect(strString, strFieldLabel, fldFocus, strNull) {

    if (!chkNull(strString, strNull)) {
        alert(strFieldLabel + " must be filled.");
        fldFocus.focus();
        return false;
    }

    return true;
}

//vldt New IC ==================================================================
function vldtNewIC1(strString, strFieldLabel, fldFocus, strNull) {
    if (!chkNull(strString, strNull)) {
//        alert(strFieldLabel + " must be changed.");
        alert(strFieldLabel + " harus ditukar.");
        fldFocus.focus();
        return false;
    }

    if (strString != "") {
        if (!(IsValidNewIC(strString, strFieldLabel))) {
            fldFocus.focus();
            return false;
        }
    }

      //commented by Delvene @ 16-Aug-2013 :: MAMPU DDSA for New Ic No is 12 char. No "-" is needed.
//    fldFocus.value = FormatNewIC(strString); //format YYMMDDXXXXXX to YYMMDD-XX-XXXX
    return true;
}

//vldt New IC ==================================================================
//extract New IC to DOB, Gender, State
function vldtNewIC2(strString, strFieldLabel, fldFocus, strStringExtract1, strStringExtract2, strNull) {

    if (!chkNull(strString, strNull)) {
//        alert(strFieldLabel + " must be filled.");
        alert(strFieldLabel + " harus diisi.");
        fldFocus.focus();
        return false;
    }

    if (strString != "") {
        if (!(IsValidNewIC(strString, strFieldLabel))) {
            fldFocus.focus();
        } else {
            fldFocus.value = FormatNewIC(strString); //format YYMMDDXXXXXX to YYMMDD-XX-XXXX
            SetRadioBox(strStringExtract1, GetGenderFromNewIC(strString));
            strStringExtract2.value = GetDOBFromNewIC(strString, "-");
        }
    }
}

function SplitNewICToArray(strNewIC) {
    var vArrayNewIC = strNewIC.split("-");
    if (vArrayNewIC.length == 1) {
        if (strNewIC.length == 12) {
            vArrayNewIC[0] = strNewIC.substring(0,6);
            vArrayNewIC[1] = strNewIC.substring(6,8);
            vArrayNewIC[2] = strNewIC.substring(8,12);
        }
    }
    return vArrayNewIC;
}

function FormatNewIC(strNewIC) {
    var vArrayNewIC = SplitNewICToArray(strNewIC);
    if (vArrayNewIC.length != 3) {
        return strNewIC;
    } else {
        return vArrayNewIC[0] + "-" + vArrayNewIC[1] + "-" + vArrayNewIC[2];
    }
}

function IsValidNewIC(strNewIC, strFieldLabel) {
    var vArrayNewIC = SplitNewICToArray(strNewIC);
    if (vArrayNewIC.length != 3) {
//        alert(strFieldLabel + " tidak sah.  \n" + strFieldLabel + " sepatutnya dalam format YYMMDD-XX-XXXX."); //commented by Zhafari 18-Dec-2013
//        alert(strFieldLabel + " tidak sah.  \n" + strFieldLabel + " hendaklah dalam format YYMMDDXXXXXX."); //Zhafari 18-Dec-2013 without "-"
        alert(strFieldLabel + " is invalid.  \n" + strFieldLabel + " must be in YYMMDDXXXXXX format."); 
//        alert(strFieldLabel + " must be in [YYMMDD-XX-XXXX] format.");
        return false;
    }

    if (isNaN(vArrayNewIC[0])) {
//        alert(strFieldLabel + " tidak sah.  \n" + strFieldLabel + " sepatutnya dalam format YYMMDD-XX-XXXX."); //commented by Zhafari 18-Dec-2013
//        alert(strFieldLabel + " tidak sah.  \n" + strFieldLabel + " hendaklah dalam format YYMMDDXXXXXX."); //Zhafari 18-Dec-2013 without "-"
        alert(strFieldLabel + " is invalid.  \n" + strFieldLabel + " must be in YYMMDDXXXXXX format."); 
//        alert(strFieldLabel + " must be in [YYMMDD-XX-XXXX] format.");
        return false;
    }
    if (isNaN(vArrayNewIC[1])) {
//        alert(strFieldLabel + " tidak sah.  \n" + strFieldLabel + " sepatutnya dalam format YYMMDD-XX-XXXX."); //commented by Zhafari 18-Dec-2013
//        alert(strFieldLabel + " tidak sah.  \n" + strFieldLabel + " hendaklah dalam format YYMMDDXXXXXX."); //Zhafari 18-Dec-2013 without "-"
        alert(strFieldLabel + " is invalid.  \n" + strFieldLabel + " must be in YYMMDDXXXXXX format."); 
//        alert(strFieldLabel + " must be in [YYMMDD-XX-XXXX] format.");
        return false;
    }
    if (isNaN(vArrayNewIC[2])) {
//        alert(strFieldLabel + " tidak sah.  \n" + strFieldLabel + " sepatutnya dalam format YYMMDD-XX-XXXX."); //commented by Zhafari 18-Dec-2013
//        alert(strFieldLabel + " tidak sah.  \n" + strFieldLabel + " hendaklah dalam format YYMMDDXXXXXX."); //Zhafari 18-Dec-2013 without "-"
        alert(strFieldLabel + " is invalid.  \n" + strFieldLabel + " must be in YYMMDDXXXXXX format."); 
//        alert(strFieldLabel + " must be in [YYMMDD-XX-XXXX] format.");
        return false;
    }

    if (vArrayNewIC[0].length != 6) {
//        alert("Panjang " + strFieldLabel + " tidak betul.  \n" + strFieldLabel + " sepatutnya dalam format YYMMDD-XX-XXXX."); //commented by Zhafari 18-Dec-2013
//        alert("Panjang " + strFieldLabel + " tidak betul.  \n" + strFieldLabel + " sepatutnya dalam format YYMMDDXXXXXX."); //Zhafari 18-Dec-2013 without "-"
        alert(strFieldLabel + " length is incorrect.  \n" + strFieldLabel + " must be in YYMMDDXXXXXX format..");
//        alert(strFieldLabel + " must be in [YYMMDD-XX-XXXX] format.");
        return false;
    }
    if (vArrayNewIC[1].length != 2) {
//        alert("Panjang " + strFieldLabel + " tidak betul.  \n" + strFieldLabel + " sepatutnya dalam format YYMMDD-XX-XXXX."); //commented by Zhafari 18-Dec-2013
//        alert("Panjang " + strFieldLabel + " tidak betul.  \n" + strFieldLabel + " sepatutnya dalam format YYMMDDXXXXXX."); //Zhafari 18-Dec-2013 without "-"
        alert(strFieldLabel + " length is incorrect.  \n" + strFieldLabel + " must be in YYMMDDXXXXXX format..");
//        alert(strFieldLabel + " must be in [YYMMDD-XX-XXXX] format.");
        return false;
    }
    if (vArrayNewIC[2].length != 4) {
//        alert("Panjang " + strFieldLabel + " tidak betul.  \n" + strFieldLabel + " sepatutnya dalam format YYMMDD-XX-XXXX."); //commented by Zhafari 18-Dec-2013
//        alert("Panjang " + strFieldLabel + " tidak betul.  \n" + strFieldLabel + " sepatutnya dalam format YYMMDDXXXXXX."); //Zhafari 18-Dec-2013 without "-"
        alert(strFieldLabel + " length is incorrect.  \n" + strFieldLabel + " must be in YYMMDDXXXXXX format..");
//        alert(strFieldLabel + " must be in [YYMMDD-XX-XXXX] format.");
        return false;
    }

    var vDay = parseInt(vArrayNewIC[0].substring(4,6), 10);
    var vMonth = parseInt(vArrayNewIC[0].substring(2,4), 10);
    var v2DigitsYear = parseInt(vArrayNewIC[0].substring(0,2), 10);

    v2DigitsYear = (v2DigitsYear == 0) ? "00" : v2DigitsYear;
    var vFullYear = (parseInt(v2DigitsYear) < 10) ? ("20" + v2DigitsYear) : ("19" + v2DigitsYear);
    if (vDay <= 0) {
//        alert(strFieldLabel + " - Tarikh hari tidak sah.");
        alert(strFieldLabel + " - Invalid Day.");
//        alert(strFieldLabel + " must be in [YYMMDD-XX-XXXX] format.");
        return false;
    }
    if (vDay > 31) {
//        alert(strFieldLabel + " - Tarikh hari tidak sah.");
        alert(strFieldLabel + " - Invalid Day.");
//        alert(strFieldLabel + " must be in [YYMMDD-XX-XXXX] format.");
        return false;
    }
    if (vMonth <= 0) {
//        alert(strFieldLabel + " - Tarikh bulan tidak sah.");
        alert(strFieldLabel + " - Invalid Month.");
//        alert(strFieldLabel + " must be in [YYMMDD-XX-XXXX] format.");
        return false;
    }
    if (vMonth > 12) {
//        alert(strFieldLabel + " - Tarikh bulan tidak sah.");
        alert(strFieldLabel + " - Invalid Month.");
//        alert(strFieldLabel + " must be in [YYMMDD-XX-XXXX] format.");
        return false;
    }
    if ((vMonth == 4 || vMonth == 6 || vMonth == 9 || vMonth == 11) && vDay > 30 ) {
//        alert(strFieldLabel + " - Tarikh hari tidak sah.");
        alert(strFieldLabel + " - Invalid Day.");
//        alert(strFieldLabel + " must be in [YYMMDD-XX-XXXX] format.");
        return false;
    }
    if (vMonth == 2) {
        if (!IsLeapYear(vFullYear) && (vDay > 28)) {
//            alert(strFieldLabel + " - Tarikh hari tidak sah.");
            alert(strFieldLabel + " - Invalid Day.");
//            alert(strFieldLabel + " must be in [YYMMDD-XX-XXXX] format.");
            return false;
        }
        if (IsLeapYear(vFullYear) && (vDay > 29)) {
//            alert(strFieldLabel + " - Tarikh hari tidak sah.");
            alert(strFieldLabel + " - Invalid Day.");
//            alert(strFieldLabel + " must be in [YYMMDD-XX-XXXX] format.");
            return false;
        }
    }
    return true;
}

//Added By Delvene @ 14-Jun-2013 :: Validate New IC Without Alert (To be used on localValidateForm)
function IsValidNewICOnSubmit(strNewIC) {
    var vArrayNewIC = SplitNewICToArray(strNewIC);
    if (vArrayNewIC.length != 3) {
        return false;
    }

    if (isNaN(vArrayNewIC[0])) {
        return false;
    }
    if (isNaN(vArrayNewIC[1])) {
        return false;
    }
    if (isNaN(vArrayNewIC[2])) {
        return false;
    }
    
    if (vArrayNewIC[0].length != 6) {
        return false;
    }
    if (vArrayNewIC[1].length != 2) {
        return false;
    }
    if (vArrayNewIC[2].length != 4) {
        return false;
    }

    var vDay = parseInt(vArrayNewIC[0].substring(4,6), 10);
    var vMonth = parseInt(vArrayNewIC[0].substring(2,4), 10);
    var v2DigitsYear = parseInt(vArrayNewIC[0].substring(0,2), 10);

    v2DigitsYear = (v2DigitsYear == 0) ? "00" : v2DigitsYear;
    var vFullYear = (parseInt(v2DigitsYear) < 10) ? ("20" + v2DigitsYear) : ("19" + v2DigitsYear);

    if (vDay <= 0) {
        return false;
    }
    if (vDay > 31) {
        return false;
    }
    if (vMonth <= 0) {
        return false;
    }
    if (vMonth > 12) {
        return false;
    }
    if ((vMonth == 4 || vMonth == 6 || vMonth == 9 || vMonth == 11) && vDay > 30 ) {
        return false;
    }
    if (vMonth == 2) {
        if (!IsLeapYear(vFullYear) && (vDay > 28)) {
            return false;
        }
        if (IsLeapYear(vFullYear) && (vDay > 29)) {
            return false;
        }
    }
    return true;
}

function IsLeapYear(strYear) {
    if (strYear % 100 == 0) {
        if (strYear % 400 == 0) {
            return true;
        }
    } else {
        if ((strYear % 4) == 0) {
            return true;
        }
    }
    return false;
}

function SetRadioBox(strCheck, strGender) {
    if (strGender == 'Male') {
        strCheck[0].checked = true;
    } else {
        strCheck[1].checked = true;
    }
}

function GetDOBFromNewIC(strNewIC, strDOBSeparator) {
    var vArrayNewIC = SplitNewICToArray(strNewIC);

    if (vArrayNewIC.length != 3) {
        return "";
    } else {
        if (!IsValidNewIC(strNewIC)) {
            return "";
        } else {
            var vDay = vArrayNewIC[0].substring(4,6);
            var vMonth = vArrayNewIC[0].substring(2,4);
            var v2DigitsYear = vArrayNewIC[0].substring(0,2);
            var v2DigitsCurrentYear = parseInt(new Date().getFullYear().toString().substring(2,4)); //added by Zhafari @ 19-Jun-2014 - use current year's YY instead of constant 10
            var vFullYear = (parseInt(v2DigitsYear) <= v2DigitsCurrentYear) ? ("20" + v2DigitsYear) : ("19" + v2DigitsYear); //added by Zhafari @ 19-Jun-2014 - use current year's YY instead of constant 10
            //var vFullYear = (parseInt(v2DigitsYear) < 10) ? ("20" + v2DigitsYear) : ("19" + v2DigitsYear); //Commented by Zhafari @ 19-Jun-2014 - use current year's YY instead of constant 10
            //return vFullYear + strDOBSeparator + vMonth + strDOBSeparator + vDay; //YYYY-MM-DD
            return vDay + strDOBSeparator + vMonth + strDOBSeparator + vFullYear; //DD-MM-YYYY
        }
    }
}

function GetDOBDayFromNewIC(strNewIC, strDOBSeparator) {
    var vArrayNewIC = SplitNewICToArray(strNewIC);

    if (vArrayNewIC.length != 3) {
        return "";
    } else {
        if (!IsValidNewIC(strNewIC)) {
            return "";
        } else {
            var vDay = vArrayNewIC[0].substring(4,6);
            return vDay;
        }
    }
}

function GetDobMonthFromNewIC(strNewIC, strDOBSeparator) {
    var vArrayNewIC = SplitNewICToArray(strNewIC);

    if (vArrayNewIC.length != 3) {
        return "";
    } else {
        if (!IsValidNewIC(strNewIC)) {
            return "";
        } else {
            var vMonth = vArrayNewIC[0].substring(2,4);
            return vMonth;
        }
    }
}

function GetDobYearFromNewIC(strNewIC, strDOBSeparator, strYearFormat) {
    var vArrayNewIC = SplitNewICToArray(strNewIC);

    if (vArrayNewIC.length != 3) {
        return "";
    } else {
        if (!IsValidNewIC(strNewIC)) {
            return "";
        } else {
            var v2DigitsYear = vArrayNewIC[0].substring(0,2);
            var vFullYear = (parseInt(v2DigitsYear) < 10) ? ("20" + v2DigitsYear) : ("19" + v2DigitsYear);
            return (strYearFormat == "YY") ? v2DigitsYear : vFullYear;
        }
    }
}

/** Modified by IvyL 29th August 2011
 */

function GetGenderFromNewIC(strGender) {
    var i = strGender.length - 1;
    var arr = strGender.charAt(i);
    var vGender = arr % 2;

    if (vGender.toString() == "0") {
        return "F";
    } else if (vGender.toString() == "1") {
        return "M";
    } else {
        return "";
    }
}

//vldt Price ===================================================================
function vldtPrice(strString, strFieldLabel, fldFocus, strMinChar, strMaxChar, strDigit, strPrecision, strNull) {

    if (!chkNull(strString, strNull)) {
        alert(strFieldLabel + " must be filled.");
        fldFocus.focus();
        return false;
    }

    if (strString != "") {
        if (!chkStringRange(strString, strMinChar, strMaxChar)) {
            alert(strFieldLabel + " should not exceed " + strMaxChar + " characters or less than " + strMinChar + " characters.");
            fldFocus.focus();
            return false;
        }

        if (!chkInvalidChar6(strString)) {
            alert(strFieldLabel + " should not contain invalid characters.");
            fldFocus.focus();
            return false;
        }

        //set price to 2 decimal pts
        pos = (strString.indexOf(".")) + 1;
        len = strString.length;
        if (pos == 0) {
            if (len > parseInt(strDigit)) {
                alert(strFieldLabel + " should not exceed " + strDigit + " digit dan " + strPrecision + " titik perpuluhan.");
                fldFocus.focus();
                return false;
            }
        } else {
            if (pos > (parseInt(strDigit) + 1)) {
                alert(strFieldLabel + " should not exceed " + strDigit + " digit dan " + strPrecision + " titik perpuluhan.");
                fldFocus.focus();
                return false;
            } else {
                diff = len - pos;
                if (diff > parseInt(strPrecision)) {
                    alert(strFieldLabel + " should not exceed " + strPrecision + " titik perpuluhan.");
                    fldFocus.focus();
                    return false;
                }
            }
        }
    }

    return true;
}

//vldt Postcode ================================================================
function vldtPostcode(strString, strFieldLabel, fldFocus, strMinChar, strMaxChar, strNull) {

    if (!chkNull(strString, strNull)) {
        alert(strFieldLabel + " must be filled.");
        fldFocus.focus();
        return false;
    }

    if (strString != "") {
        if (!chkStringRange(strString, strMinChar, strMaxChar)) {
            alert(strFieldLabel + " should not exceed " + strMaxChar + " characters or less than " + strMinChar + " character(s).");
            fldFocus.focus();
            return false;
        }

        if (!chkInvalidChar3(strString)) {
            alert(strFieldLabel + " should not contain invalid characters.");
            fldFocus.focus();
            return false;
        }
    }

    return true;
}

//vldt Tel, Fax ================================================================
function vldtTelFax(strString, strFieldLabel, fldFocus, strMinChar, strMaxChar, strNull) {

    if (!chkNull(strString, strNull)) {
        alert(strFieldLabel + " must be filled.");
        fldFocus.focus();
        return false;
    }

    if (strString != "") {
        if (!chkStringRange(strString, strMinChar, strMaxChar)) {
            alert(strFieldLabel + " should not exceed " + strMaxChar + " characters or less than " + strMinChar + " character(s).");
            fldFocus.focus();
            return false;
        }

        if (!chkInvalidChar4(strString)) {
            alert(strFieldLabel + " should not contain invalid characters.");
            fldFocus.focus();
            return false;
        }
    }

    return true;
}

//vldt File Name ===============================================================
function vldtFileName(strString, strFieldLabel, fldFocus, strMinChar, strMaxChar, strNull) {

    if (!chkNull(strString, strNull)) {
        alert(strFieldLabel + " must be filled.");
        fldFocus.focus();
        return false;
    }

    if (strString != "") {
        strStringFileName = strString.substr(strString.lastIndexOf("\\") + 1);

        if (!chkStringRange(strStringFileName, strMinChar, strMaxChar)) {
            alert(strFieldLabel + " should not exceed " + strMaxChar + " characters or less than " + strMinChar + " characters.");
            fldFocus.focus();
            return false;
        }

        if (!chkInvalidChar7(strStringFileName)) {
            alert(strFieldLabel + " should not contain invalid characters.");
            fldFocus.focus();
            return false;
        }
    }

    return true;
}

/*---------------------------- General Functions ----------------------------*/
//check null
function isNull(strString) {
    if (strString == "" || strString == null) {
        return true;
    }
    return false;
}

//check empty
function chkNull(strString, strNull) {
    if (strNull.toUpperCase() == "N") {
        if (strString == "") {
            return false;
        }
    }
    return true;
}

//check minimum length of string
function chkMinLength(strString, strMinChar) {
    if (strString.length < strMinChar) {
        return false;
    }
    return true;
}

//check maximum length of string
function chkMaxLength(strString, strMinChar) {
    if (strString.length > strMinChar) {
        return false;
    }
    return true;
}

//check range of string
function chkStringRange(strString, strMinChar, strMaxChar) {
    if (strString.length < strMinChar || strString.length > strMaxChar) {
        return false;
    }
    return true;
}

//check range of number
function chkNoRange(strString, strStartChar, strEndChar) {
    if (parseInt(strString) < parseInt(strStartChar) || parseInt(strString) > parseInt(strEndChar)) {
        return false;
    }
    return true;
}

//check alpha numberic
function chkAlphaNumeric(strString, strAlphaNumeric) {
    if (strAlphaNumeric.toUpperCase() == "Y") {
        if (!(IsAlphanumeric(strString))) {
            return false;
        }
    }
    return true;
}

//check invalid character, allow a-z, A-Z, 0-9 and underscore only.
function chkInvalidChar1(strString) {
    //cannot start or end with an underscore
    var vLegalChars = /^[a-zA-Z0-9]\w*[a-zA-Z0-9]$/
    if (!(vLegalChars.test(strString))) {
        return false;
    }
    return true;
}

//check invalid character, allow common characters only
function chkInvalidChar2(strString) {
    var vValidChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 ,<.>//?;:\'\"\r\n[]\\!@#$%&*()-_=+";
    for (var i=0; i<strString.length; i++) {
        if (vValidChars.indexOf(strString.charAt(i)) == -1) {
            return false;
        }
    }
    return true;
}

//check invalid character, allow numbers only
function chkInvalidChar3(strString) {
    if (isNaN(strString)) {
        return false;
    }
    return true;
}

//check invalid character, allow numbers, spaces, brackets, dashes and pluses only
function chkInvalidChar4(strString) {
    var vValidChars = "1234567890 -()+";
    for (var i=0; i<strString.length; i++) {
        if (vValidChars.indexOf(strString.charAt(i)) == -1) {
            return false;
        }
    }
    return true;
}

//check invalid email format
function chkInvalidChar5(strString) {
    var vFilter = /^[^@]+@[^@.]+\.[^@]*\w\w$/
    if (!(vFilter.test(strString))) {
        return false;
    }
    return true;
}

//check invalid character, allow numbers and dots only
function chkInvalidChar6(strString) {
    var vValidChars = "1234567890.";
    for (var i=0; i<strString.length; i++) {
        if (vValidChars.indexOf(strString.charAt(i)) == -1) {
            return false;
        }
    }
    return true;
}

//check invalid character, for file name
function chkInvalidChar7(strString) {
    var vValidChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 ,.;\'[]!@$%()-_=+";
    for (var i=0; i<strString.length; i++) {
        if (vValidChars.indexOf(strString.charAt(i)) == -1) {
            return false;
        }
    }
    return true;
}

//is alpha numeric or not
function IsAlphanumeric(strString) {
    var iCounter;
    var vChar;
    var vAllowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
    var vAllowedNo = "0123456789";
    var bCharExist = false;
    var bNoExist = false;

    for (iCounter=0; iCounter<strString.length; iCounter++) {
        vChar = strString.substr(iCounter, 1);
        vChar = vChar.toUpperCase();
        if (vAllowedChars.indexOf(vChar) != -1) bCharExist = true;
        if (vAllowedNo.indexOf(vChar) != -1) bNoExist = true;
    }

    if (bCharExist && bNoExist) {
        return true;
    } else {
        return false;
    }
}

//trim non-numeric values, allow numbers only
function FilterChar1(field) {
    var vResult = new String();
    var vNum = "0123456789";
    var vChar = field.value.split(""); // create array
    for (i=0; i<vChar.length; i++) {
        if (vNum.indexOf(vChar[i]) != -1) vResult += vChar[i];
    }
    if (field.value != vResult) field.value = vResult;
}

//trim non-numeric values, allow numbers, dashes, brackets, pluses and spaces only
function FilterChar2(field) {
    var vResult = new String();
    var vNum = "0123456789 -()+";
    var vChar = field.value.split(""); // create array
    for (i=0; i<vChar.length; i++) {
        if (vNum.indexOf(vChar[i]) != -1) vResult += vChar[i];
    }
    if (field.value != vResult) field.value = vResult;
}

//trim non-numeric values, allow numbers and dashes only
function FilterChar3(field) {
    var vResult = new String();
    var vNum = "0123456789-";
    var vChar = field.value.split(""); // create array
    for (i=0; i<vChar.length; i++) {
        if (vNum.indexOf(vChar[i]) != -1) vResult += vChar[i];
    }
    if (field.value != vResult) field.value = vResult;
}

//trim non-numeric values, allow numbers and dots only
function FilterChar4(field) {
    var vResult = new String();
    var vNum = "0123456789.";
    var vChar = field.value.split(""); // create array
    for (i=0; i<vChar.length; i++) {
        if (vNum.indexOf(vChar[i]) != -1) vResult += vChar[i];
    }
    if (field.value != vResult) field.value = vResult;
}

//trim spaces before and after
function TrimString(strString) {
    while (strString.substring(0,1) == ' ' || strString.substring(0,1) == '\r' || strString.substring(0,1) == '\n' || strString.substring(0,1) == '\t') {
        strString = strString.substring(1, strString.length);
    }
    while (strString.substring(strString.length-1, strString.length) == ' ' || strString.substring(0,1) == '\r' || strString.substring(0,1) == '\n' || strString.substring(0,1) == '\t') {
        strString = strString.substring(0,strString.length-1);
    }
    return strString;
}

//popup window
function OpenWindow(URL, WinName, Width, Height, Settings) {
    WinName = (WinName == "") ? "NewWindow" : WinName;
    Width = (Width == "") ? (screen.Width - 200) : Width;
    Height = (Height == "") ? (screen.Height - 200) : Height;
    LeftPos = (screen.Width) ? (screen.Width - Width)/2 : 0;
    TopPos = (screen.Height) ? (screen.Height - Height - 25)/2 : 0;
    if (Settings == "0") {
        Settings = 'height=' + Height + ', width=' + Width + ', top=' + TopPos + ', left=' + LeftPos;
        Settings += ', scrollbars=1, resizable=0, status=1, menubar=0';
    } else {
        Settings = 'height=' + Height + ', width=' + Width + ', top=' + TopPos + ', left=' + LeftPos;
        Settings += ', scrollbars=1, resizable=1, status=1, menubar=0';
    }
    win = window.open(URL, WinName, Settings);
    win.focus();
}

//popup full screen
function FullScreen(URL) {
    //  window.open(URL, "", "fullscreen, scrollbars");
    window.open(URL, "", "width=2300,height=2300");
}

//check to allow numeric onkeypress event, added by tantk1 on 24/3/2008
function isNumberOnly(evt){
    var charCode = (evt.which) ? evt.which : evt.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

    return true;
}

var mikExp = /[\\0\\1\\2\\3\\4\\5\\6\\7\\8\\9]/;
function dodacheck(val) {
    var strPass = val.value;
    var offset = 0;
    if (strPass==""){
        val.value=0;
    }else{
        while (offset < strPass.length) {
            var lchar = strPass.charAt(offset);
            if (lchar.search(mikExp) != 0) {
                // split it in the middle
                var lside = strPass.substring(0, offset);
                var rside = strPass.substring(offset + 1);
                strPass = lside + rside;
            } else {
                offset++;
            }
        };
        val.value = strPass;
    }
}

//**Get the row index e.g. PsMeasure-3 will return 3
//**Created by Stanley @ 5-March-2010
function GetRowIndex(RowId) {
    var liPos;

    liPos = RowId.indexOf('-');

    return parseInt(RowId.substring(liPos +1));
}

function sortField(sortByName){
    var form = document.getElementById("sortForm");
    if (form.listSize.value <= 0){
        form.dynamicSortOrder.value = "A";
    }
    if (form.dynamicSortBy.value == sortByName){
        if (form.listSize.value > 0){
            if (form.dynamicSortOrder.value == "A"){
                form.dynamicSortOrder.value = "D";
            } else {
                form.dynamicSortOrder.value = "A";
            }
        }
    } else {
        form.dynamicSortBy.value = sortByName;
    }
    form.submit();
}

function sortField2(sortByName, refreshDivId, sortFormId){
    if(sortFormId === undefined) {
        sortFormId = 'sortForm';
    }
    var form = document.getElementById(sortFormId);
    if (form.listSize.value <= 0){
        form.dynamicSortOrder.value = "A";
    }
    if (form.dynamicSortBy.value == sortByName){
        if (form.listSize.value > 0){
            if (form.dynamicSortOrder.value == "A"){
                form.dynamicSortOrder.value = "D";
            } else {
                form.dynamicSortOrder.value = "A";
            }
        }
    } else {
        form.dynamicSortBy.value = sortByName;
    }
    // Stop form from submitting normally
    // Get some values from elements on the page:
//        var tds = $(this).find('input, textarea');
    divSubmit(sortFormId, refreshDivId);
//    var $form = $("#sortForm"),
//        url = $form.attr( "action" );
//    // Send the data using post
//    var posting = $.post( url, $('#sortForm').serialize() );
//    // Put the results in a div
//    posting.done(function( data ) {
////          var content = $( data ).find( "#content" );
//      $( "#"+refreshDivId ).empty().append( data );
//    });
}

function goPage(pageNo, paramDivId){
    if (!paramDivId) {
        paramDivId = refreshingDivId;
    }
    var form = document.getElementById("sortForm");
    form.pageNo.value = pageNo;
    divSubmit("sortForm", paramDivId);
}
function divSubmit(formId, refreshDivId) {
    var $form = $("#"+formId),
    url = $form.attr( "action" );
    // Send the data using post
    var posting = $.post( url, $("#"+formId).serialize() );
    // Put the results in a div
    posting.done(function( data ) {
//          var content = $( data ).find( "#content" );
        $( "#"+refreshDivId ).empty().append( data );
        $( "#"+refreshDivId ).find('.modal-dialog').css({
            width:'100%'
        });
        $('.full-width').select2();
        $(".select2-selection").on("focus", function() {
            $(this).parent().parent().prev().select2("open");                        
        }); 
    });
}


/*-------------------------- End General Functions --------------------------*/

// added by etys on 13/6/2011
function checkNumberOnly(obj, event, maxNumber, maxDecimal ){
    var liDot1;
    var lsSub;
    var sel, rng, r2, li=-1;
    var keynum;
    var liStart=0;
    var liEnd=0;
    var lsItem;
    var lsMinus;

    //get cursor position in textbox for FireFox
    if(typeof obj.selectionStart=="number") {
        liStart=obj.selectionStart;
        liEnd =obj.selectionEnd;
    }
    //get cursor position in textbox for ie 6
    else if(document.selection && obj.createTextRange) {
        sel=document.selection;
        if(sel){
            r2=sel.createRange();
            rng=obj.createTextRange();
            rng.setEndPoint("EndToStart", r2);
            liStart = rng.text.length;
            liEnd = liStart + r2.text.length;
        }
    }

    //get keycode
    if (isNumberKey(event)){
        //check max digit before and after .
        if(window.event) // IE
        {
            keynum = event.keyCode;
        }
        else if(event.which) // Netscape/Firefox/Opera
        {
            keynum = event.which;
        }

        //no checking if user press backspace or delete
        if (!(keynum >=48 && keynum <=57) && !(keynum==46)&& !(keynum==45)){
            return true;
        }

        lsItem = obj.value;

        //check for minus(-)
        liMinus = lsItem.indexOf('-');
        //invalid format - more than 1 '-'
        if (liMinus >= 0 && keynum==45){
            return false;
        }
        //invalid format '-' only can be at 0
        if (liStart > 0 && keynum==45){
            return false;
        }
        // added by etys on 13/6/2011
        if (keynum==45) {
            return false;
        }

        liDot1 = lsItem.indexOf('.');
        //invalid format - more than 1 '.'
        if (liDot1 >= 0 && keynum==46){
            return false;
        }
        if (liStart != liEnd){
            return true;
        }
        // added by etys on 13/6/2011
        if(keynum==46){
            return false;
        }

        //decimal point already exist
        if (liDot1 >= 0 ){
            lsSub = lsItem.substring(liDot1 +1);
            //check no. of digits after decimal
            if (lsSub.length >=maxDecimal && liStart > liDot1 ){
                return false;

            }
            //check no of digits before decimal
            lsSub = lsItem.substring(0, liDot1);

            if (lsSub.length >= maxNumber && liStart <= liDot1 ){
                //allow user to enter 9 digits & minus(-)
                if(liMinus == 0 && lsSub.length == maxNumber ){
                    return true;
                }else{
                    //allow user to enter minus(-) when reach maximum digits
                    if((keynum==45)){
                        return true;
                    }else{
                        return false;
                    }
                }
            }else{
        }

        }else{
            if(lsItem.length >= maxNumber && !(keynum==46)){
                //allow user to enter maximum digits & minus(-)
                if(liMinus == 0 && lsItem.length == maxNumber ){
                    return true;
                }else{
                    //allow user to enter minus(-) when reach maximum digits
                    if((keynum==45)){
                        return true;
                    }else{
                        return false;
                    }
                }
            }
            else if(keynum==46){
                //not allow user to enter . when decimal point is more than max
                if( (lsItem.length - liStart) > maxDecimal ){
                    return false;
                }
            }
        }

    }else{
        return false;
    }
}

function commaSeparated(nStr)
{
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}

/**
 * To track whether Enter key is pressed
 * Added by TTH @ 25-Jul-2011
 */
function isEnterKeyPressed(event) {
//    alert(window.event.keyCode);
//    alert(event.which);
    if(window.event) { // IE
        if (event.keyCode==13 ){
            return true;
        }
    } else if(event.which) {// Netscape/Firefox/Opera
        if (event.which==13 ){
            event.which = null;
            return true;
        }
    }
    return false;
}

function noUploadingInprogress() {
    if (uppyValidateInprogress) {
        if (uppyUploadingCount > 0) {
            if (uppyShowInprogressMsg) {
                alert(uppyInprogressMsg);
            }
            uppyHasInprogress = true;
            return false;
        } else {
            uppyHasInprogress = false;
            return true;
        }
    } else {
        uppyHasInprogress = false;
        return true;
    }
}
/**
 * To Submit Form when Form onSubmit is false;
 * Added by TTH @ 26-Jul-2011
 */
function submitForm(formId, formAction) {
//    startMaskLoading();
    if (noUploadingInprogress()) {
        $("form#"+formId).attr("action", formAction);
        $("form#"+formId).submit();
    }
    return false;
}

function actionSubmit(field, actionName, formId) {
    var $form;
    if (formId) {
        $form = $("#"+formId);
    } else {
        $form = $(field).closest('form');
//        $form = field.form;
    }
    $form.attr( "action", actionName );
    $form.submit();
}
/**
 * Use this function to set the default Button being triggered when certain field get focus (onfocus)
 * or default back the form's original default button when field lost focus(onblur)
 */
function defaultButton(field, strAction){
    for (i = 0; i < document.forms.length; i++) {
        if (field.form[i].type == "submit") {
            var submitButton = field.form[i];
            if (document.all) { //IE8
                var element = field.form.attributes['action'];
                element.value = strAction;
                submitButton.name = 'action:' + strAction;
            } else {
                field.form.action = strAction;
                submitButton.name = 'action:' + strAction;
            }
            break;
        }
    }
}

/**
 * to get the form's action
 */
function getFormAction(form) {
    if (document.all) { //IE
        var element = form.attributes['action'];
        return element.value;
    } else {
        var arr = form.action.split("/");
        return arr[arr.length - 1];
    }
}

/** ThoTH */
function detectBrowserVersion(){
    var userAgent = navigator.userAgent.toLowerCase();
    //    $.browser.chrome = /chrome/.test(navigator.userAgent.toLowerCase());//commented by ahmadni @ 13-Jun-2017 
    var version = 0;
    var myBrowser = "";

    // Is this a version of IE?
//    if($.browser.msie){//commented by ahmadni @ 13-Jun-2017 
    if(/*@cc_on!@*/false || !!document.documentMode){
        userAgent = $.browser.version;
        myBrowser = "ie:" + userAgent;
        userAgent = userAgent.substring(0,userAgent.indexOf('.'));
        version = userAgent;
    }

    // Is this a version of Chrome?
//    if($.browser.chrome){//commented by ahmadni @ 13-Jun-2017 
    if(!!window.chrome && !!window.chrome.webstore){
        userAgent = userAgent.substring(userAgent.indexOf('chrome/') +7);
        userAgent = userAgent.substring(0,userAgent.indexOf('.'));
        myBrowser = "chrome:" + userAgent;
        version = userAgent;
        // If it is chrome then jQuery thinks it's safari so we have to tell it it isn't
//        $.browser.safari = false; //commented by ahmadni @ 13-Jun-2017 (Cause error at Chrome)
    }

    // Is this a version of Safari?
//    if($.browser.safari){//commented by ahmadni @ 13-Jun-2017 
    if(navigator.userAgent.toLowerCase().indexOf('safari') != -1){
        userAgent = userAgent.substring(userAgent.indexOf('safari/') +7);
        myBrowser = "safari:" + userAgent;
        userAgent = userAgent.substring(0,userAgent.indexOf('.'));
        version = userAgent;
    }

    // Is this a version of Mozilla?
//    if($.browser.mozilla){//commented by ahmadni @ 13-Jun-2017 
    if(typeof InstallTrigger !== 'undefined'){
        //Is it Firefox?
        if(navigator.userAgent.toLowerCase().indexOf('firefox') != -1){
            userAgent = userAgent.substring(userAgent.indexOf('firefox/') +8);
            myBrowser = "firefox:" + userAgent;
            userAgent = userAgent.substring(0,userAgent.indexOf('.'));
            version = userAgent;
        }
        // If not then it must be another Mozilla
        else{
        }
    }

    // Is this a version of Opera?
//    if($.browser.opera){//commented by ahmadni @ 13-Jun-2017 
    if((!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0){
        userAgent = userAgent.substring(userAgent.indexOf('version/') +8);
        userAgent = userAgent.substring(0,userAgent.indexOf('.'));
        myBrowser = "opera:" + userAgent;
        version = userAgent;
    }
    return myBrowser;
}


/**
 * Added by ChangMH @ 20-Aug-2014 :: Check the balance charactor that can be enter in.
 */
function textCounter(field, countfield, maxlimit){
    if (field.value.length > maxlimit) // if too long...trim it!
        field.value = field.value.substring(0, maxlimit);
        // otherwise, update 'characters left' counter
    else 
        countfield.value = maxlimit - field.value.length;
}

/**
 * Added by ChangMH @ 28-Aug-2014 :: Format the currency to ###,###,###.00.
 */
function formatCurrency(n) {
    return n.toFixed(2).replace(/./g, function(c, i, a) {
        return i > 0 && c !== "." && (a.length - i) % 3 === 0 ? "," + c : c;
    });
}

/**
 * Added by ahmadni @ 10-Nov-2016 :: Remove Dash from IC
 */
function removeDashIC(strNewIC) {
    var vArrayNewIC = strNewIC.split("-");    
    vArrayNewIC = strNewIC.substring(0,6)+strNewIC.substring(7,9)+strNewIC.substring(10,15);    
    return vArrayNewIC;
}

function checkFullStop(e)
{   
//            alert("checkFullStop" );
    var keynum;
    var keychar;
    var numcheck;

    if(window.e) // IE
    {
        keynum = e.keyCode;
    }
    else if(e.which) // Netscape/Firefox/Opera
    {
        keynum = e.which;
    }
//            alert("keynum full stop" + keynum);
    //not allow to enter '.' 
    if ((keynum===190)||(keynum===110)||(keynum===46)){
        return false;
    }else{
        return true;
    }

}



//added by amywyp @ 05-09-2018 ---use to select/deselect all child checkboxes
function toggleCheckboxByName(source, name) {
    checkboxes = document.getElementsByName(name);
    for (var i = 0, n = checkboxes.length; i < n; i++) {
        if (!checkboxes[i].disabled) {
            checkboxes[i].checked = source.checked;
        }
    }
}
function toggleCheckboxByClassName(source,name) {
  checkboxes = document.getElementsByClassName(name);
  for(var i=0, n=checkboxes.length;i<n;i++) {
        if (!checkboxes[i].disabled) {
            checkboxes[i].checked = source.checked;
        }
  }
}

//added by amywyp @ 05-09-2018--to toggle SelectAll checkbox based on checked child checkbox
function toggleSelectAll(cbClass){
    if (cbClass === undefined) {
        cbClass = 'checkbox_child';
    }
    if ($('.'+cbClass+':checked').length === $('.'+cbClass+'').length ){
        $(".selectAll").prop('checked', true);
    }else{
        $(".selectAll").prop('checked', false);
    }
}

function toggleSelectAll_ml(parentCheckbox, childCheckbox){ //multiLevel
    if ($('.'+childCheckbox+':checked').length == $('.'+childCheckbox).length ){
        $("."+parentCheckbox).prop('checked', true);
    }else{
        $("."+parentCheckbox).prop('checked', false);
    }
}

//alert modal
function vldtPassword2(strString, strFieldLabel, fldFocus, strMinChar, strMaxChar, strNull, strAlphaNumeric) {

    if (!chkNull(strString, strNull)) {
//        alert(strFieldLabel + " must be filled.");
        alert(strFieldLabel + " wajib diisi.");
        fldFocus.focus();
        return false;
    }

    if (strString != "") {
        if (!chkStringRange(strString, strMinChar, strMaxChar)) {
//            alert(strFieldLabel + " should not exceed " + strMaxChar + " characters or less than " + strMinChar + " characters.");
////            alert(strFieldLabel + " tidak boleh melibihi " + strMaxChar + " aksara atau kurang daripada " + strMinChar + " aksara.");
//            fldFocus.focus();
//            return false;
            var msgAlert = strFieldLabel + " should not exceed " + strMaxChar + " characters or less than " + strMinChar + " characters.";
            if(msgAlert !== ""){    
                $("#alertDiv").find('.title').html('<h3 class="title-alert">Alert</h3>');
                $("#alertDiv").find('.myModalContent').html(msgAlert);
                $('#alertDiv').modal('show');
                fldFocus.focus();
                return false;
            }
        }

        //if (!chkInvalidChar1(strString)) {  //## Commented by TTH, becos some user's password consists of char like $, @
        if (!chkInvalidChar2(strString)) {
//            alert(strFieldLabel + " should not contain invalid characters.");
////            alert(strFieldLabel + " tidak boleh mengandungi aksara tidak sah.");
//            fldFocus.focus();
//            return false;
            var msgAlert = strFieldLabel + " should not contain invalid characters.";
            if(msgAlert !== ""){    
                $("#alertDiv").find('.title').html('<h3 class="title-alert">Alert</h3>');
                $("#alertDiv").find('.myModalContent').html(msgAlert);
                $('#alertDiv').modal('show');
                fldFocus.focus();
                return false;
            }
        }

        if (!chkAlphaNumeric(strString, strAlphaNumeric)) {
//            alert(strFieldLabel + " must be in alphanumeric.");
////            alert(strFieldLabel + " mestilah dalam format abjad angka.");
//            fldFocus.focus();
//            return false;
            var msgAlert = strFieldLabel + " must be in alphanumeric.";
            if(msgAlert !== ""){    
                $("#alertDiv").find('.title').html('<h3 class="title-alert">Alert</h3>');
                $("#alertDiv").find('.myModalContent').html(msgAlert);
                $('#alertDiv').modal('show');
                fldFocus.focus();
                return false;
            }
        }
    }

    return true;
}

function divSubmitForm(submitAction, formId, itemDiv, postEvent) {
    var message = "";
    if (document.getElementById("r")) {
        $("#r").remove();
    }
    var posting = $.post(submitAction, $("#" + formId).serialize());
    posting.fail(function(jqXHR){
        if(jqXHR.status === 404) {
            alert(error404+'\n'+error404_msg1+'\n'+error404_msg2);
        } else {
            alert("Unexpected error occurred");
        }
    });
    posting.done(function(data) {
        try {
            data.startsWith("error:")
        } catch(err) {
            data = JSON.stringify(data);
        }
        if (data.startsWith("error:")) {
            alert(data.substring(6));
        } else {
            $("#" + itemDiv).html(data);
            if (document.getElementById("r")) {
                var rItem = $("#r").val().split(",");
                for (var i = 0; i < rItem.length; i++) {
                    if (rItem[i].indexOf(";") >= 0) {
                        if (rItem[i].split(";")[0] === 'rmsg') {
                            message = rItem[i].split(";")[1];
                        } else {
                            $("#" + rItem[i].split(";")[0]).val(rItem[i].split(";")[1]);
                        }
                    } else {
                        $("#" + rItem[i]).html($("#" + rItem[i] + "_n").html());
                    }
                }
            }
        }
        if (message !== "") {
            alert(message);
        }
        if (postEvent) {
            window[postEvent]();
        }
    });
}

var lookupSelected;
function lookupModal(field, lookup, desc, postEvent) {
    lookupSelected = false;
    console.log("attr= "+$(field).attr("style"));
    console.log("prop= "+$(field).prop("style"));
    console.log($(field).closest("form"));
    var formId = $(field).closest("form").attr('id');
    console.log("field.id = " + field.attr("id"));
    if (formId === undefined) {
        formId = field.attr('id')+'form';
        $(field).closest("form").attr('id', formId);
    }
    console.log("formId = " + formId);
    var filterByIdx = lookup.indexOf("&filterBy");
    if (filterByIdx > 0) {
        var filterBy = lookup.substring(filterByIdx+10);
        var filterArray = filterBy.split(",");
	var value, obj;
        var args="&filter=";
	for (var i = 0; i < filterArray.length; i++) {
	  filterArray[i] = filterArray[i].trim();
            console.log("1. filterArray["+i+"] = " + filterArray[i]);
          //obj = document.all[filterArray[i]];
          if (filterArray[i].indexOf("_as_") > 0) {
              filterArray[i] = filterArray[i].substring(0, filterArray[i].indexOf("_as_"));
          }
            console.log("2 .filterArray["+i+"] = " + filterArray[i]);
          if (filterArray[i].indexOf("firstFilter_") === 0) {
              obj = document.getElementById(formId)[filterArray[i].substring(12)];
          } else {
              obj = document.getElementById(formId)[filterArray[i]];
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
        lookup += args;
    }
    lookup += "&" + "lookupParentFormId="+formId+ "&lookupDesc="+(desc===undefined?"":desc);
    $("#lookupModal").load(lookup,
        function(message) {
            if (message === "Expired") {
                document.location = "initLogin";
            }
            $("#lookupModal").data('width', '60%');
            $('#lookupModal').modal('show');
            $("#lookupModal").on('hidden.bs.modal', function() {
                $(this).off('hidden.bs.modal');
                if (lookupSelected) {
                if (postEvent !== undefined) {
                    if (postEvent.includes("::=")) {
                        var idx = postEvent.indexOf("::=");
                        window[postEvent.substring(0, idx)](postEvent.substring(idx+3));
                    } else {
                        window[postEvent]();
                    }
                }
                }
                $(field).focus();
                $("#lookupModal").html("");
            });
        });
}

function constructTaCount(taClass) {
    var defaultClass = "taCount";
    if (taClass) {
        defaultClass = taClass;
    }
    $("."+defaultClass ).each(function( index ) {
        var ta = document.getElementById($(this).prop("id"));
        var width = ta.clientWidth;
        var height = ta.clientHeight;
        $( '<span class="label label-default" id="'+$(this).prop("id")+'_count"></span>' ).insertAfter( $( this ) );
        var _count_id = $(this).prop("id")+"_count";
        var _id = $(this).prop("id");
        $(this).on('blur', function() {
            if ($("."+_id+"_div").length) {
                $("."+_id+"_div").css('padding-bottom', oriPadding);
            }
            if ($("#"+_id+"_div").length) {
                $("#"+_id+"_div").css('padding-bottom', oriPadding);
            }
//            $("#"+_id+"_div").css('padding-bottom', "0px");
            $("#"+_count_id).html("");
        });
        $(this).on("mouseup", function(){
            if($(this).prop("clientWidth") !== width || $(this).prop("clientHeight") !== height){
                $(this).focus();
                $(this).prop("selectionStart", $(this).val().length);
                $(this).prop("selectionEnd", $(this).val().length);
//                            ta.selectionStart = $(this).val().length;
//                            ta.selectionEnd = $(this).val().length;
            }
            width = ta.clientWidth;
            height = ta.clientHeight;
        });
        var oriPadding = '';
        $(this).inputFilter(function(value) {
//            var position = $("#"+_id+"_div").position();
//                $("#"+_count_id).css({top: position.top + $("#"+_id).height()+10, left: position.left, position:'absolute'});
            if (value.length > $("#"+_id).attr("maxlength")) {
                $("#"+_id).val(value.substring(0, $("#"+_id).attr("maxlength")));
                return false;
            } else {
                    if (oriPadding === '') {
                        oriPadding = $("."+_id+"_div").css('padding-bottom');
                    }
                    if ($("."+_id+"_div").length) {
                        $("."+_id+"_div").css('padding-bottom', "5px");
                    }
                    if ($("#"+_id+"_div").length) {
                        $("#"+_id+"_div").css('padding-bottom', "5px");
                    }
                $("#"+_count_id).html(value.length + "/" + $("#"+_id).attr("maxlength"));
                return true;
            }
//                        return /^\d*$/.test(value)&&(value === "" || parseFloat(value) <= 100000);
        });
//            console.log( index + ": " + $( this ).prop("id") );
    });
}