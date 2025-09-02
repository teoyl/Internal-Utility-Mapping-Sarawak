<%@taglib uri="/struts-tags" prefix="s"%>
<!--<link href="include/jquery-datepicker/jquery.ac_impian.css" rel="stylesheet">-->
<style>
    .autocomplete-suggestions { border: 1px solid #999; background: #FFF; overflow: auto; }
    .autocomplete-suggestion { padding: 2px 5px; white-space: nowrap; overflow: hidden; }
    .autocomplete-selected { background: #F0F0F0; }
    .autocomplete-suggestions strong { font-weight: normal; color: #3399FF; }
</style>
<script type="text/javascript" src="include/jquery.autocomplete/jquery.autocomplete.js"></script>
<script>
     <s:if test="acSetupList.size > 0">
        <s:iterator value="acSetupList" id="acSetup" status="asSetupStatus">
            var acData${asSetupStatus.index} = [${acSetup.data}];

            $(document).ready(function() {
                if (!($('#${acSetup.inputId}').attr("placeholder")) ) {
                    <s:if test="#acSetup.placeHolder != null">
                            $('#${acSetup.inputId}').attr("placeholder", "<s:text name="%{#acSetup.placeHolder}"/>");
                    </s:if><s:else>
                        $('#${acSetup.inputId}').attr("placeholder", "<s:text name="ac.plsKeyIn"/>");
                    </s:else>
                }
                $('#${acSetup.inputId}').autocomplete({
                    lookup: acData${asSetupStatus.index},
                    showNoSuggestionNotice: 'true',
                    onSelect: function (suggestion) {
                        writeSuggestion($(this).attr("id"), suggestion, "${acSetup.lookFor}", "${acSetup.writeTo}");
                    }
                });
                <s:if test="#acSetup.clearSuggestion">
                $('#${acSetup.inputId}').change({writeTo: "${acSetup.writeTo}"},  function(event) {
                    clearSuggestion(acData${asSetupStatus.index}, $(this).val(), event.data.writeTo);
                });
                </s:if>
            });
        </s:iterator>
        function clearSuggestion(dataSource, theValue, param) {
            if (theValue !== "") {
                var foundSuggestion = false;
                for (i = 0; i < dataSource.length; i++) {
                    if ( (dataSource[i].value) === theValue ) {
                          foundSuggestion = true; break;
                    }
                }
                if (!foundSuggestion) {theValue = "";}
            }
            if (theValue === "") {
                writeToArr = param.split(",");
                for (i = 0; i < writeToArr.length; i++) {
                    if (writeToArr[i].startsWith("lb")) {
                        $('#'+writeToArr[i]).text("");
                    } else {
                        $('#'+writeToArr[i]).val("");
                    }
                }
            }
        }
        function writeSuggestion(objectId, suggestion, lookFor, writeTo) {
            writeToArr = writeTo.split(",");
            lookForArr = lookFor.split(",");
            for (i = 0; i < writeToArr.length; i++) {
                if (writeToArr[i].startsWith("lb")) {
                    $('#'+writeToArr[i]).text(suggestion[lookForArr[i]]);
                } else {
                    $('#'+writeToArr[i]).val(suggestion[lookForArr[i]]);
                }
            }
        }
    </s:if>
</script>