<%@ page contentType="application/x-javascript"%>
<%@taglib uri="/struts-tags" prefix="s"%>
function lookupModal(field, lookup, desc) {
    var formId = $(field).closest("form").attr('id');
    if (formId === undefined) {
        formId = field.attr('id') + 'form';
        $(field).closest("form").attr('id', formId);
    }
    lookup += "&" + "lookupParentFormId=" + formId + "&lookupDesc=" + (desc === undefined ? "" : desc);
    $("#lookupModal").html("");
    $("#lookupModal").load(lookup,
        function (message) {
            if (message === "Expired") {
                document.location = "initLogin";
            }
            $("#lookupModal").data('width', '60%');
            $("#lookupModal").on('shown', function(){
                showHideLookupMoreField();
            });
            $('#lookupModal').modal('show');
            $("#lookupModal").on('hide', function () {
                $(field).focus();
        });
    });
}
$(document).ready(function () {
    registerDateRangePicker('drPicker');
    $('#datepicker').datepicker({
        autoclose: true,
        format: "dd/mm/yyyy"
    });
    $('.lookup_module').click(function() {
        lookupModal($('#module_code'), 'modalLookup?lookFor=module_code&writeTo=module_code&lookup=useSetup_Module&displayedColumns=module_code,module_name');
    });
    $('#submit-btn').click(function() {
        validateForm_bshor('sampleFormId');
    });
});

