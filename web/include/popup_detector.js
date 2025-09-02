// ThoTH @ 12-Sept-2012
var winPopupDetector = null;
function test_popup(pButtonToHide) {
    var popResult = true;
    if(winPopupDetector == null || typeof(winPopupDetector) == "undefined") {
        //    if(winPopupDetector == null || typeof(winPopupDetector) == "undefined" || winPopupDetector.location.href == 'about:blank') {
        popResult = false;
    } else {
        var myBrowser = detectBrowserVersion();
        if (myBrowser.indexOf("chrome") >= 0) {
//            winPopupDetector.onload = function () {
//                alert (winPopupDetector.screenX );
                if (winPopupDetector.screenX  != 0) {
                    popResult = false;
                } else 
//            }
            if (winPopupDetector.location.href == 'about:blank') {
                popResult = false;
            }
        }
        winPopupDetector.close();
    }
    if (! popResult) {
        try {            
            $('#' + pButtonToHide).attr("style","display:none");            
            $('#spPopupDetector').attr("style","display:inline");
        } catch (err) {}
    }
}

function myTimer() {
   return true;
}

function detect_popup(pButtonToHide) {
    winPopupDetector = window.open('include/popupDetector.jsp', 'Popup Detector', '');
    setTimeout("test_popup('"+pButtonToHide+"')",250);
}


