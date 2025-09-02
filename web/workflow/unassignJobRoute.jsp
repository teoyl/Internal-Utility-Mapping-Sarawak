<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>

<script language="javascript">
    $(document).ready(function() {
        $('#resultCasea').DataTable();
        
        $(".getJobButton").click(function(e) {
            e.preventDefault();
            selectJob($(this).data("id"));
        });
        
        $(".getAllJobButton").click(function(e) {
            e.preventDefault();
            selectMultipleJob();
        });
    });

    function confirmSelectJob(str) {
        bootbox.confirm({
            closeButton: false,
            message: "The selected job will be assigned to you. Do you want to proceed ?",
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
                    parent.document.getElementById("selectJobId_").value = str;
                    parent.document.insertJobForm.submit();
                }
            }
        });
    }

    function selectJob(pPlId) {
        $('.pPlId').val(pPlId);
        bootbox.confirm({
            closeButton: false,
            message: "The selected job will be assigned to you. Do you want to proceed ?",
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
                    parent.document.getElementById("selectJobId_").value = pPlId;
                    parent.document.insertJobForm.submit();
                }
            }
        });
    }

    function selectMultipleJob() {
        var str = "";
        var i = 0;
        $("input:checkbox").each(function () {
            if ($(this).is(':checked')) {
                if (str.length > 0)
                    str += "^_^";
                str += $(this).val();
                i++
            }
        });
        if (str == "") {
            bootbox.alert({
                closeButton: false,
                message: "No Job selected."
            });
            
            return null;
        }
        confirmSelectJob(str);
    }

    //sereneChye@ 9/7/2015
    function resetFields(form) {
        var noOfElements = form.elements.length;
        for (var i = 0; i < noOfElements; i++) {
            if (!(form.elements[i].type === "hidden"
                    || form.elements[i].type === "submit"
                    || form.elements[i].type === "button")) {
                clearValue(form.elements[i]);
            }
        }
////        document.unassignjobMainForm.action = "processSearchRouteJobMain?useRoute=true&istrSystemId=" +${istrSystemId} + "&jobSearchAction=search&defaultPageSize=50";
    }

    <%-- ThoTH @ 20-Nov-2015 --%>
    function submitMyForm() {
        document.getElementById("unassignjobMainForm").submit();
    }

    function localValidateForm(form, operation) {
        var errors = new Array();
        validateRequired(form, errors);
        if (errors.length > 0) {
            alert(errors.join('\n'));
            setFocus(form);
        }
        return errors.length > 0 ? false : true;
    }
    function required() {
    }
</script>

<br>

<form method="post" id="unassignjobMainForm" name="unassignjobMainForm" class=""  action="processSearchRouteJobMain?useRoute=true&istrSystemId=${istrSystemId}&jobSearchAction=search&defaultPageSize=0">
    <s:hidden cssClass="listJobtobeGrab" value="%{listJobtobeGrab.size()}"/>
    <s:hidden cssClass="pPlId"/>
    <s:if test="listJobtobeGrab.size() > 0">
    <div class="row">
        <div class="col-md-12 mt-3">
            <button class="btn btn-primary getAllJobButton" type="submit"><i class="fas fa-hand-paper"></i> <s:text name="jobList.getAllJob" /></button>
        </div>
    </div> <br/>
    </s:if>
       
    <table id="resultCasea" class="table table-striped dataTable" >
        <thead>
            <tr>
                <th class="col-1"><s:text name="jobList.jobNo" /></th>
                <th class="col-1"><s:text name="jobList.jobDate" /></th>
                <th class="col-1"> <s:text name="jobList.from" /></th>
                <th class="col-4 "><s:text name="jobList.appDetail" /></th>
                <th class="col-2"><s:text name="jobList.jobItem" /></th>
                <th class="col-2"><s:text name="jobList.jobDetail" /></th>
                <th class="col-1">&nbsp;</th>
            </tr>
        </thead>
        <tbody>
            <s:if test='listJobtobeGrab.size() > 0'>
                <s:iterator value="listJobtobeGrab" status="localStatus" var="localList">
                <tr class="<s:if test="#localStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                    <td align="center" class="jobLabel">
                        <s:checkbox theme="simple" name="selected" id="ids" fieldValue="%{#localList.get('task_id')}" />
                    </td>
                    <td class="jobLabel">
                        <s:text name="date_default_datetime"><s:param value="%{#localList.get('assigned_date')}"/></s:text>
                    </td>
                    <td>
                        <s:property escapeHtml="true" value="%{#localList.get('previousTaskDoer')}"/>
                    </td>
                    <td>
                        <s:property escapeHtml="true" value="%{#localList.get('case_ref')}"/>
                    </td>
                    <td class="jobLabel">
                        <s:property escapeHtml="true" value="%{#localList.get('wf_name')}"/>
                    </td>
                    <td class="jobLabel">
                        <s:property escapeHtml="true" value="%{#localList.get('task_description')}"/>
                    </td>
                    <td class="jobLabel">
                        <button class="btn btn-primary getJobButton" data-id="${localList.get('task_id')}"><i class="fa fa-hand-paper" aria-hidden="true"></i>&nbsp;<s:text name="jobList.getJob" /></button>
                    </td>
                </tr>
                </s:iterator>
            </s:if>
            <s:else>
                
            </s:else>
        </tbody>
    </table>
</form>  