/*
 * Prompt for user's confirmation upon invoking delete button
 */
 
 function isInternetExplorer()
{
	if (navigator.appVersion.match('MSIE 7'))
	{
		return true;
	}
	else
	{
		return false;
	}
}

function confirmDelete(question) {
        if (question === undefined) {
            question = "You are about to DELETE this record. Do you want to proceed?";
        }
	var answer = confirm(question);
//	var answer = confirm("Adakah anda pasti untuk hapus rekod yang dipilih?");
	var isIE = false;
	
	isIE = isInternetExplorer();
	return (isIE? event.returnValue = answer : answer);
	
}


function confirmDeleteCase(question) {
        if (question === undefined) {
            question = "You are about to DELETE THIS APPLICATION. Do you want to proceed?";
        }
	var answer = confirm(question);
//	var answer = confirm("Adakah anda pasti untuk hapus rekod yang dipilih?");
	var isIE = false;
	
	isIE = isInternetExplorer();
	return (isIE? event.returnValue = answer : answer);
	
}

function confirmPermanentDelete() {
	var answer = confirm("You are about to DELETE this record. Do you want to proceed?");
//	var answer = confirm("Adakah anda pasti untuk hapus rekod yang dipilih secara KEKAL?");
	var isIE = false;

	isIE = isInternetExplorer();
	return (isIE? event.returnValue = answer : answer);

}

//function confirmPermanentDelete2() {
//	var answer = confirm("Unsaved data will be lost. Are you sure you want to PERMANENTLY delete the record(s)?");
////	var answer = confirm("Adakah anda pasti untuk hapus rekod yang dipilih secara KEKAL?");
//	var isIE = false;
//
//	isIE = isInternetExplorer();
//	return (isIE? event.returnValue = answer : answer);
//
//}
/*added by amywyp 16-05-2018*/
function confirmPermanentDelete2() {
        $("#confirmDiv").find('.myModalContent').html("Unsaved data will be lost. Do you want to proceed?");
        $('#confirmDiv').modal('show');
}
/*
 * Prompt for user's confirmation upon invoking back button
 */
function confirmBack() {
	var answer = confirm("Unsaved data will be lost. Do you want to proceed?");
	var isIE = false;

	isIE = isInternetExplorer();
	return (isIE? event.returnValue = answer : answer);
}
/*
 * Prompt for user's confirmation upon processing any kind of approval.
 */
function confirmApproval(action) {
//	var answer = confirm("Are you sure you want to " + action + "?");
/*added by IvyL 18-08-2018*/
        var answer = confirm("You are about to " + action + ". Do you want to proceed?");
	var isIE = false;

	isIE = isInternetExplorer();
	return (isIE? event.returnValue = answer : answer);
}
/*
 * Prompt for user's confirmation upon making critical changes
 */
function confirmAbort() {
	var answer = confirm("Unsaved data will be lost. Do you want to proceed?");
	return answer;
}

/* Added by THOTH @ 12 Aug 2010
 * Prompt for user's confirmation upon invoking reject button
 */
function confirmComplete(formId) {

        var answer = confirm("You are about to COMPLETE this job. Do you want to proceed?");
	var isIE = false;

	isIE = isInternetExplorer();
        var complete = false;
        complete = (isIE? event.returnValue = answer : answer);
        if (formId) {
            if (complete) {
                document.getElementById(formId).submit();
                return false;
            }
        }
	return complete;
}

/* Added by THOTH @ 20 Aug 2010
 * Prompt for user's confirmation upon invoking terminate button
 */
function confirmTerminate() {
	var answer = confirm("You are about to TERMINATE this job. Do you want to proceed?");
	var isIE = false;

	isIE = isInternetExplorer();
	return (isIE? event.returnValue = answer : answer);
}

/*
 * Prompt for user's confirmation upon invoking reject button
 */
function confirmReject() {
	var answer = confirm("You are about to REJECT this job. Do you want to proceed?");
//	var answer = confirm("Adakah anda pasti untuk menolak?");
	return answer;
}

//Added by Zhafari @ 22 Aug 2013 - START

/*
 * Prompt for user's confirmation upon form submission
 */
function confirmSubmitVer() {
//	var answer = confirm("Anda akan menghantar rekod ini untuk semakan. Teruskan?");
//	var answer = confirm("Anda akan menghantar rekod ini untuk pengesahan. Teruskan?");
	var answer = confirm("You are about to COMPLETE this job. Do you want to proceed ?");
	var isIE = false;

	isIE = isInternetExplorer();
        console.log(isIE);
	return (isIE? event.returnValue = answer : answer);
}

function confirmSubmitVerDisclaimer(disclaimer) {
//	var answer = confirm("Anda akan menghantar rekod ini untuk semakan. Teruskan?");
	var answer = confirm(disclaimer);
	var isIE = false;

	isIE = isInternetExplorer();
	return (isIE? event.returnValue = answer : answer);
}

/*
 * Prompt for user's confirmation upon form submission after got rejected
 */
function confirmReSubmitVer() {
//	var answer = confirm("Anda akan menghantar rekod ini untuk semakan semula. Teruskan?");
	var answer = confirm("Anda akan menghantar rekod ini untuk pengesahan semula. Teruskan?");
	var isIE = false;

	isIE = isInternetExplorer();
	return (isIE? event.returnValue = answer : answer);
}

/*
 * Prompt for user's confirmation upon record approval in verification
 */
function confirmApprovedVer(message) {
//    var answer = confirm("You are about to approve this record.\nDo you want to proceed?");
    var answer;
    if (message) {
        answer = confirm(message);
    } else {
        answer = confirm("Anda akan mengesah rekod ini. \nTeruskan?");
    }
    
    var isIE = false;

    isIE = isInternetExplorer();
    return (isIE? event.returnValue = answer : answer);
}

function confirmRejectVer() {
//    var answer = confirm("Anda akan menolak rekod ini untuk semakan. \nTeruskan?");
    var answer = confirm("You are about to REJECT this application. Do you want to proceed ?");
    var isIE = false;

    isIE = isInternetExplorer();
    return (isIE? event.returnValue = answer : answer);
}

//ahmadni @ 21-Jul-2017
function confirmReturnVer() {
    var answer = confirm("You are about to RETURN this application to PA. Do you want to proceed ?");
    var isIE = false;

    isIE = isInternetExplorer();
    return (isIE? event.returnValue = answer : answer);
}

//ahmadni @ 21-Jul-2017
function confirmReturnVer(ver) {
    var answer = "";
    if (ver === "PO"){
         answer = confirm("You are about to RETURN this application to PA. Do you want to proceed ?");
    }else if (ver === "HOD") {
         answer = confirm("You are about to RETURN this application to PO. Do you want to proceed ?");
    }else {
        answer = confirm("You are about to RETURN this application to Applicant. Do you want to proceed ?");
    }
    var isIE = false;

    isIE = isInternetExplorer();
    return (isIE? event.returnValue = answer : answer);
}
