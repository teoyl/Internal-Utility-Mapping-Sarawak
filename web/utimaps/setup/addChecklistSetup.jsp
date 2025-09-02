<%-- 
    Document   : addChecklistSetup
    Created on : Dec 7, 2018, 3:29:37 PM
    Author     : User
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="utimaps.setup.checklist.addChecklist" /></title>
        <link rel="stylesheet" href="include/jquery-ui-1.11.4/jquery-ui.min.css" />
        <script nonce="r4DjhKbfO5ry">
            function required(){
                this.aa = new Array("checklist_name", "<s:text name='checklist.name' />");
                this.ab = new Array("process_type", "<s:text name='checklist.processType' />");
                this.ae = new Array("case_type", "<s:text name='checklist.caseType' />");
                console.log(this.ad);
            }
        </script>
        
        <style nonce="EuTVqS192VKl">
            .input-group {
                display: flex;
            }
            .input-group-btn {
                position: absolute;
                right: 50px;
                z-index: 99;
            }
            .input-group-btn:last-child > .btn, .input-group-btn:last-child > .btn-group {
                margin-top: -4px;
                height: 35px;
            }
            .form-group span {
                float: none;
                display: block;
            }
            .fa-calendar {
                font-size: large;
            }
            .btn .fa {
                margin-left: 10px;
            }
            .form-group-default.form-group-default-select2 > label {
                z-index: 2;
            }
            select.full-width + .select2-container {
                width: 100% !important;
            }
            textarea {
                margin-top: 5px;
            }
            .number-input {
                text-align: center;
            }
            table select {
                height: 32px;
                width: inherit;
                padding: 0 10px;
            }
            .drop-note {
                cursor: pointer;
                padding-left: 5px;
            }
            .drop-note:hover {
                color: #0080FF;
            }
        </style>
    </head>
    <body>
        <div class="x_panel">
            <jsp:include page="/pages/base/actionError.jsp"/>

            <form method="post" id="addChecklistFormId" action="processInsertChecklistSetup">
                <s:hidden theme="simple" name="action" />
                <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
                <div class="row">
                    <div class="col-md-5">
                        <label class="form-label" for="checklist_name"><s:text name="checklist.name"/></label>
                        <div class="input-group mb-3">
                            <s:textfield theme="simple" name="checklist_name" value="%{model.checklist_name}" cssClass="form-control"/>
                        </div>

                        <label class="form-label" for="process_type"><s:text name="checklist.processType"/></label>
                        <div class="input-group mb-3">
                            <s:select 
                                name="process_type"
                                value="%{model.process_type}"
                                list="processTypeList"
                                listKey="keyData"
                                listValue="valueData"                    
                                cssClass="form-select"
                             />
                        </div>

                        <label class="form-label" for="checklist_status"><s:text name="checklist.status"/></label>
                        <div class="input-group mb-3">
                            <s:select 
                                    name="checklist_status"
                                    value="%{model.checklist_status}"
                                    theme="simple" 
                                    list="statusOptionList"
                                    listKey="keyData"
                                    listValue="valueData"
                                    cssClass="form-select"
                                 />
                        </div>

                    </div>
                    <div class="col-md-12 text-end">
                        <button class="btn btn-primary" type="submit" name="processInsertChecklistSetup" id="processInsertUserGroup"  onclick="return validateForm(this.form, 'insert')"><i class="fa fa-save"></i> <s:text name="button.save" /></button>        
                        <button class="btn btn-default" type="submit" name="action:cancelChecklistSetup" id="cancelUserGroup"><i class="fa fa-times"></i> <s:text name="button.cancel" /></button>
                    </div>    
                </div>

                <div class="row">
                    <div class="col-md-12">
                        <h3 class="title-v3"><s:text name="checklist.items" /></h3>
                    </div>
                </div>
                    <div class="row">
                    <div class="col-md-12 form-row-margin">
                        <s:if test="has_right('processAddListItem')">
                            <button class="btn btn-default" type="submit" name="action:processAddListItemChecklistSetup" id="processAddListItemChecklistSetup"><i class="fa fa-plus"></i> <s:text name="utimaps.setup.checklist.addItems" /></button>
                        </s:if>
                        <s:if test="has_right('processDeleteChecklistItems')">
                            <button class="btn btn-default" type="submit" name="action:processDeleteChecklistItemsChecklistSetup" id="processDeleteChecklistItemsChecklistSetup" onclick="if (isCheckboxSelected(form.list_selected)) {
                                    return confirmDelete();
                                } else {
                                    return false;
                                }"><i class="fa fa-trash"></i> <s:text name="utimaps.setup.checklist.deleteItems" /></button>
                        </s:if>
                    </div>
                </div>    

                <s:if test="model.checklistItemList.size() > 0">        
                    <div class="row">
                        <div class="col-md-12">
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th width="1%" align="center">
                                                <s:if test="model.checklistItemList.size() > 0">
                                                    <input type="checkbox" id="list_select" name="list_select" onclick="toggleCheckbox(this, delList_ids);">
                                                </s:if>
                                                <s:else>
                                                    <input type="checkbox" id="list_select" name="list_select" disabled >
                                                </s:else>
                                            </th>
                                            <th width="600"><s:text name="checklist.desc" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                            <th width="100"><s:text name="checklist.status" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                            <th width="100"><s:text name="checklist.sequence" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                            <th width="200"><s:text name="checklist.dataType" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <s:iterator value="model.checklistItemList" status="listStatus" var="checklistItem">
                                            <tr class="<s:if test="#listStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                                <s:hidden theme="simple" name="checklistItemList[%{#listStatus.index}].ci_id" value="%{#checklistItem.ci_id}" />
                                                <td align="center">
                                                    <s:checkbox theme="simple" name="list_selected" id="delList_ids" fieldValue="%{#listStatus.index}" onclick="checkToggleCheckbox(list_select, delList_ids)"/>
                                                </td>
                                                <td width="600">
                                                    <s:textfield theme="simple" name="checklistItemList[%{#listStatus.index}].ci_desc" value="%{#checklistItem.ci_desc}" cssClass="input-sm form-control" />
                                                    <s:textarea id="textarea_%{#listItem.ci_id}" theme="simple" name="checklistItemList[%{#listStatus.index}].ci_notes" value="%{#checklistItem.ci_notes}" rows="5" cssClass="input-sm form-control" placeholder="Write Down Notes Here" />                                                
                                                </td>
                                                <td width="90">
                                                    <s:select 
                                                        name="checklistItemList[%{#listStatus.index}].ci_status"
                                                        data-init-plugin="select2" 
                                                        value="%{#checklistItem.ci_status}"
                                                        theme="simple" 
                                                        list="statusOptionList"
                                                        listKey="keyData"
                                                        listValue="valueData"
                                                        id="dataType_%{#listStatus.index}"
                                                        cssClass="full-width dataType_select"
                                                     />
                                                </td>
                                                <td width="70">
                                                    <s:textfield theme="simple" name="checklistItemList[%{#listStatus.index}].ci_sequence" value="%{#checklistItem.ci_sequence}" cssClass="input-sm number-input form-control" />
                                                </td>
                                                <td width="200">
                                                    <s:hidden theme="simple" name="checklistItemList[%{#listStatus.index}].ci_datatype" value="%{#checklistItem.ci_datatype}" cssClass="input-sm form-control" id="textbox_dataType_%{#listStatus.index}" />
                                                    <s:select 
                                                        name="checklistItemList[%{#listStatus.index}].ci_dataType"
                                                        data-init-plugin="select2" 
                                                        value="%{#checklistItem.ci_datatype}"
                                                        theme="simple" 
                                                        list="dataTypeList"
                                                        listKey="keyData"
                                                        listValue="valueData"
                                                        id="dataType_%{#listStatus.index}"
                                                        cssClass="full-width dataType_select"
                                                     />
                                                </td>
                                                <%int index = 0;%>
                                            </tr>
                                        </s:iterator>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </s:if>

                <script nonce="r4DjhKbfO5ry">
                    // Restricts input for each element in the set of matched elements to the given inputFilter.
                    (function($) {
                        $.fn.inputFilter = function(inputFilter) {
                            return this.on("input keydown keyup mousedown mouseup select contextmenu drop", function() {
                                if (inputFilter(this.value)) {
                                    this.oldValue = this.value;
                                    this.oldSelectionStart = this.selectionStart;
                                    this.oldSelectionEnd = this.selectionEnd;
                                } else if (this.hasOwnProperty("oldValue")) {
                                    this.value = this.oldValue;
                                this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
                                }
                            });
                        };
                    }(jQuery));

                    $( document ).ready(function() {
                        $('.dataType_select').on('change', function() {
                            textbox_id = '#textbox_' + $(this).attr('id');
                            $(textbox_id).attr('value', this.value);
                        })

                        $(".number-input").inputFilter(function(value) {
                            return /^\d*$/.test(value); 
                        });

                        $(".drop-note").on('click', function() {
                            var id = $(this).attr("data-id");
                            $("#textarea_" + id).css("display", "initial");
                            $(this).css("display", "none");
                        });
                    });
                </script>            
            </form>
        </div>
    </body>
</html>
