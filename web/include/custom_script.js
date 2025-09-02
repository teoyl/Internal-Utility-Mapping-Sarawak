/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global bootbox, CKEDITOR */

function checkChecklistResult(formName, decisionP, decisionR, checklistName, completeBtnName) {
    $('.'+formName+':radio').on("click", function() {
        var checkResult = "pass";

        $('.'+formName+':radio').each(function() {
            if($('input[name="'+$(this).attr('name')+'"]:checked').val() === "E") {
                checkResult = "failed"; 
            }
        });
        
        if(checkResult === "pass") {
            $('#'+decisionP).prop("checked", true);
        } else if(checkResult === "failed") {
            $('#'+decisionR).prop("checked", true);
        }
        
        checklistDecisionButton(checklistName, completeBtnName);
    });

}

//change complete button based on user decision
function checklistDecisionButton(checklistName, completeBtnName) {
    var decision = $('input[name="model.'+checklistName+'.check_status"]:checked').val();
    
    $("#"+completeBtnName).removeClass("btn-primary");
    $("#"+completeBtnName).removeClass("btn-danger");
    $("#"+completeBtnName).removeClass("btn-success");

    if(decision === 'A') {
        $("#"+completeBtnName).addClass("btn-success");
        $("#"+completeBtnName).text('Accept Submission');
    } else if (decision === 'R') {
        $("#"+completeBtnName).addClass("btn-danger");
        $("#"+completeBtnName).text('Query PS');

    } else {
        $("#"+completeBtnName).addClass("btn-success");
        $("#"+completeBtnName).text('Complete');
    }
}

//Check if all checklist items had been ticked
function checkAllTicked(formName) {
    var names = {};
    var fillAll = true;
    $('.'+formName+':radio').each(function() {
        names[$(this).attr('name')] = true;
    });

    var count = 0;
    $.each(names, function() { 
        count++;
    });

    if ($('.'+formName+':radio:checked').length === count) {
        fillAll = true;
    } else {
        fillAll = false;
        bootbox.alert({
            closeButton: false,
            message: "Please tick all on the Document Checking section in the Checklist."
        });
    }

    return fillAll;
}

function initEditor(textareaName, maxChar, height = 250) {
    
    CKEDITOR.replace(textareaName, {
        contentsCss: "body {font-size: 15px;font-family: 'Poppins', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol';}",
        extraPlugins: 'wordcount,enterkey,link',
        wordcount: {
            countSpacesAsChars: true,
            showCharCount: true,
            showWordCount: false,
            showParagraphs: false,
            maxCharCount: maxChar
        },
        height: height
    });
}

function confirmationBox(msg, formName, actionName) {
    bootbox.confirm({
        closeButton: false,
        message: msg,
        buttons: {
            confirm: {
                label: 'PROCEED'
            },
            cancel: {
                label: 'CANCEL'
            }
        },
        callback: function (result) {
            if (result) {
                var form = $(formName);
                form.attr("action", actionName);
                form.submit();
            }
        }
    });
}

function alertBox(msg) {
    bootbox.alert({
        closeButton: false,
        message: msg
    });
}

function checkChecklistResultRemarks(formName, decisionP, decisionR, checklistName, completeBtnName) {
    var incompleteChecklistSet = new Set(); // Use a set to avoid duplicates
    var alertMessageList = [];
    var result = 1;
    
    $('.'+formName+':radio').each(function() {
        if($('input[name="'+$(this).attr('name')+'"]:checked').val() === "E") {
            var checklistItemName = $(this).attr('name');

            // Use regular expression to match the desired part
            var regex = /^(.*)\.\w+$/;
            var match = checklistItemName.match(regex);

            if (match) {
              const extractedString = match[1];
              incompleteChecklistSet.add(extractedString); // Add to the set
            } else {
            }
        }
    });

    if (incompleteChecklistSet.length === 0) {
    } else {
        for (const extractedString of incompleteChecklistSet) {
            var clRemarks = extractedString + ".cl_remarks";
            var ciDesc = extractedString + ".ci_desc";
            var clRemarksContent = $('textarea[name="'+clRemarks+'"]').val();
            var ciDescStr = $('input[name="'+ciDesc+'"]').val();
            
            if(clRemarksContent.length === 0) {
                var alertMsg = "Please enter processing remarks for " + ciDescStr;
                alertMessageList.push(ciDescStr);
            }
        }
    }
    
    if(alertMessageList.length > 0) {
        result = 0;
        var messageContent = "Please enter and save the processing remarks for <br>";
        alertMessageList.forEach(function(element, index) {
            messageContent += index+1 + ". " + element + "<br>";
        });
        
        bootbox.alert({
            closeButton: false,
            message: messageContent
        });
    }

    return result;
}

function saveShortcut(checklistName, actionButton) {
    $(document).keydown(function(event) {
        if (event.ctrlKey && event.keyCode === 83) { // Ctrl+S
            event.preventDefault(); // Prevent default browser action

            var displayMessage = "Are you sure want to save?";

            confirmationBox(displayMessage, checklistName, actionButton);
        }
    });
}

function scrollToView(id) {
    const element = document.getElementById(id);
    element.scrollIntoView();
}