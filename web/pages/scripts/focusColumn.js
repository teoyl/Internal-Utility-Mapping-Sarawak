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
    if (theElement) {
        var selectedPosX = 0;
        var selectedPosY = 0;
        while(theElement != null){
            selectedPosX += theElement.offsetLeft;
            selectedPosY += theElement.offsetTop;
            theElement = theElement.offsetParent;
        }
        window.scrollTo(selectedPosX,selectedPosY);
    }
}