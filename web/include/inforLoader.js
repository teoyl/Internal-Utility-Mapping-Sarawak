/**
 * init the div
 */
//function init_retriever() {
document.write('<div style="display:none" id="loading_screen"><h1>Loading...</h1><h3>Please Wait...</h3></div>');
document.write('<div style="display:none" executeScripts="true" dojoType="struts:BindDiv" id="dataRetriverDetails" formId="dataRetriver_form" href="dataRetriever" listenTopics="load_detail" showError="true" showLoading="Loading..." parseContent="true"></div>')
document.write('<form id="dataRetriver_form"><input type=hidden name="lookupParentFormId" value=""><input type=hidden name="_loadingConfig" value=""><input type=hidden name="_searchField" value=""><input type=hidden name="_searchFieldData" value=""><input type=hidden name="_searchFieldId" value=""><input type=hidden name="_lookFor" value=""><input type=hidden name="_writeTo" value=""><input type=hidden name="_focusOn" value=""><input type=hidden name="_onTrigger" value=""><input type=hidden name="filterBy" value=""><input type=hidden name="filter" value=""></form>')
//document.write('<img id="indicator" src="/images/ajax-loader.gif" style="display:none"/>');

    djConfig.searchIds.push("dataRetriverDetails");

//}
var loadingImagePath = '<img src="/images/ajax-loader.gif"/>';
String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}

//loadingType mean the type of loading screen/icon to be used.
//eg, 1. ('1' or null or '') = not showing,
//    2. ('2' or 'icon') = show loading icon in the first lb*** field
//    3. ('3' or 'hold') = show a loading screen that stop user from continueing editing until loading completed (Not done yet)
function loadDescs(loadingConfig, searchField, inputField, lookFor, writeTo, loadingIndicator, onTrigger, focusOn, filterBy){
    var proceedLoad = false;
    if (onTrigger) {
        proceedLoad = this[onTrigger].apply(this, Array.prototype.slice.call(arguments, 1));
    } else {
        proceedLoad = true;
    }
    if (proceedLoad) {
        if (inputField.value == "") { //clear the writeTo fields
            var writeToArray = writeTo.split(",");
            for (var i = 0; i < writeToArray.length; i++) {
                var writeToItem = new String(writeToArray[i]);
                if (writeToItem.trim().substring(0, 2) == "lb") {
                    document.getElementById(writeToItem.trim()).innerHTML = "";
                } else {
                    document.getElementById(writeToItem.trim()).value = "";
                }
            }
        } else { //load the description from server
            var retrieverForm = document.getElementById("dataRetriver_form");
            retrieverForm._loadingConfig.value = loadingConfig;
            retrieverForm._searchField.value = searchField;
            retrieverForm._searchFieldData.value = inputField.value;
            retrieverForm._searchFieldId.value = inputField.id;
            retrieverForm.lookupParentFormId.value = inputField.form.id;
            retrieverForm._lookFor.value = lookFor;
            retrieverForm._writeTo.value = writeTo;
            if (onTrigger) {retrieverForm._onTrigger.value = onTrigger;}
            if (focusOn) {retrieverForm._focusOn.value = focusOn;}
            if (filterBy) {
                retrieverForm.filterBy.value = filterBy;
                var filterArray = filterBy.split(",");
                var value, obj;
                var filtersValue = "";
                for (var i = 0; i < filterArray.length; i++) {
                    filterArray[i] = filterArray[i].trim();
                    //obj = document.all[filterArray[i]];
                    if (filterArray[i].indexOf("_as_") > 0) {
                        filterArray[i] = filterArray[i].substring(0, filterArray[i].indexOf("_as_"));
                    }
                    //obj = window.parent.document.getElementById(parentFormId)[filterArray[i]];
                    obj = inputField.form[filterArray[i]];

                    if ((obj.name)) {
//                        value = escape(obj.value);
//                        value = value.replace('+','%2b'); // manually escape
//                        filtersValue += value;
                        filtersValue += obj.value;
                    } else {
                        filtersValue += "";
                    }
                    if (i < (filterArray.length-1)) {
                        filtersValue += ",,,"
                    }
                }
                retrieverForm.filter.value = filtersValue;
            } else {
                retrieverForm.filter.value = "";
                retrieverForm.filterBy.value = "";
            }
            if ((loadingIndicator) && loadingIndicator!='') {
                if (loadingIndicator == 'icon' || loadingIndicator == '2') {
                    var writeToArray = writeTo.split(",");
                    for (var i = 0; i < writeToArray.length; i++) {
                        var writeToItem = new String(writeToArray[i]);
                        if (writeToItem.trim().substring(0, 2) == "lb") {
                            document.getElementById(writeToItem.trim()).innerHTML = loadingImagePath;
                            break;
                        }
                    }
                }
            }
            dojo.event.topic.publish('load_detail');
        }
    }
}