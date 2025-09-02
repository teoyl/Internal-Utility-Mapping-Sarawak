<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" language="javascript" src="include/print/jquery.dataTables.min.js"></script>
<script type="text/javascript" language="javascript" src="include/print/dataTables.buttons.min.js"></script>
<script type="text/javascript" language="javascript" src="include/print/buttons.flash.min.js"></script>
<script type="text/javascript" language="javascript" src="include/print/jszip.min.js"></script>
<script type="text/javascript" language="javascript" src="include/print/pdfmake.min.js"></script>
<script type="text/javascript" language="javascript" src="include/print/vfs_fonts.js"></script>
<script type="text/javascript" language="javascript" src="include/print/buttons.html5.min.js"></script>
<script type="text/javascript" language="javascript" src="include/print/buttons.print.min.js"></script>
<script language="javascript">
$(document).ready(function() {
    $('#dynamicListTable').dataTable( {
        dom: 'Bti',
        buttons: [
            <s:if test='printTo.equals("pdf")'>
            {
                extend: '<s:property value="%{printTo}"/>',
                title: '<s:text name="system.name"/>\n <s:text name="common.reportListing"><s:param><s:property value="%{searchDescription}" escapeJavaScript="true"/></s:param></s:text>',
                customize: function(doc) {
                    doc.styles.title = {
                      fontSize: '20',
                      alignment: 'center'
                    }   
                }
            }
            </s:if><s:else>
            {
                extend: '<s:property value="%{printTo}"/>',
                title: '<s:text name="system.name"/>\n <s:property value="%{searchDescription}" escapeJavaScript="true"/> Listing'
            }
            </s:else>
        ]
    });
//    $(".dt-buttons").addClass("hidden");
    $('.buttons-<s:property value="%{printTo}"/>').click();
});
</script>
<jsp:include page="${listPage}.jsp"/>
