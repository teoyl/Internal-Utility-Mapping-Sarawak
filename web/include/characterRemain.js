/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function() {
    // Get all textareas that have a "maxlength" property.
    $("textarea[maxlength]").each(function() {

        // Store the jQuery object to be more efficient...
        var $textarea = $(this);

        // Store the maxlength and value of the field
        var maxlength = $textarea.attr("maxlength");
        // Add a DIV to display remaining characters to user
        $textarea.after($("<div>").addClass("charsRemaining"));

        // Bind the trimming behavior to the "keyup" & "blur" events (to handle mouse-based paste)
        $textarea.on("keyup blur", function(event) {
            // Fix OS-specific line-returns to do an accurate count
//            var val = $textarea.val().replace(/\r\n|\r|\n/g, "\r\n").slice(0, maxlength);
//            $textarea.val(val);
            var textRemain = maxlength - $textarea.val().length;
            if (textRemain==0 ){
                // Display updated count to user
                 $textarea.next(".charsRemaining").html("<span class='charsRemainingAlert'>"+ textRemain +" character left</span>");
            }
            else{
                $textarea.next(".charsRemaining").html(textRemain + " character(s) left");
            }
        }).trigger("blur");

    });
});

function characterRemain(){
     // Get all textareas that have a "maxlength" property.
    $("textarea[maxlength]").each(function() {

        // Store the jQuery object to be more efficient...
        var $textarea = $(this);

        // Store the maxlength and value of the field
        var maxlength = $textarea.attr("maxlength");
        
       
        // Add a DIV to display remaining characters to user
        $textarea.after($("<div style='font-style: italic;'>").addClass("charsRemainingPopup"));
        $(".charsRemaining").remove();
        // Bind the trimming behavior to the "keyup" & "blur" events (to handle mouse-based paste)
        $textarea.on("keyup blur", function(event) {
            // Fix OS-specific line-returns to do an accurate count
//            var val = $textarea.val().replace(/\r\n|\r|\n/g, "\r\n").slice(0, maxlength);
          // alert('val :' + val.length);
//            $textarea.val(val);
            var textRemain = maxlength - $textarea.val().length;
            if (textRemain==0 ){
                // Display updated count to user
                 $textarea.next(".charsRemainingPopup").html("<span class='charsRemainingAlert'>"+ textRemain +" character left</span>");
            }
            else{
                $textarea.next(".charsRemainingPopup").html(textRemain + " character(s) left");
            }
        }).trigger("blur");

    });
};