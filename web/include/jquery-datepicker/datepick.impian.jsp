<%@taglib uri="/struts-tags" prefix="s"%>
<link href="include/jquery-datepicker/jquery.datepick_impian.css" rel="stylesheet">
<style>
    .embed + img { position: relative; left: -18px; top: 0px; }
    .embed_2 + img { position: relative; left: 320px; top: -18px; }
</style>
<!--<script src="include/jquery_11.js"></script>-->
<!--<script src="include/jquery-datepicker/jquery.validate.min.js"></script>-->
<script src="include/jquery-datepicker/jquery.plugin.js"></script>
<script src="include/jquery-datepicker/jquery.mousewheel.js"></script>
<script src="include/jquery-datepicker/jquery.datepick.js"></script>
<script src="include/jquery-datepicker/jquery.datepick.ext.min.js"></script>
<script src="include/jquery-datepicker/jquery.datepick.validation.js"></script>
<script src="pages/scripts/date_mattkruse.js"></script>
<script>
    function initDatePicker() {
        $('.datepick-impian').each(function(i, obj) {
            $(this).datepick();
            $(this).change(function(){
                if (!($(this).val() === "")) {
                    validateDate($(this));
                }
            });
        });
    }
    function enableDate() {
        $('.datepick-impian').each(function(i, obj) {
            $(this).datepick('enable');
        });
    }
    function disableDate() {
        $('.datepick-impian').each(function(i, obj) {
            $(this).datepick('disable');
        });
    }
    function validateDate(pObj) {
        var lDate = $(pObj).val();
        if (lDate.length == 8) {  // append / if lengh only 8, e.g. 01012014
            lDate = lDate.substring(0,2) + '/' + lDate.substring(2,4) + '/' + lDate.substring(4,8);
        } else {
            lDate = lDate.replace(/\./g,'/');  // replace all . to /  e.g. 01.01.2014
            lDate = lDate.replace(/\-/g,'/');  // replace all - to /  e.g. 01-01-2014 
        }
        $(pObj).val(lDate);
        var bValid = isDate(lDate,'<s:text name="date_default_date_dr"/>');
        if(! bValid) {
            alert(formatText(messageInvalidDateFormat, lDate));
            $(pObj).val(null);
            $(pObj).focus();
        }
        return bValid;
    }    
</script>
<script src="include/jquery-datepicker/jquery.datepick-ms.js"></script>