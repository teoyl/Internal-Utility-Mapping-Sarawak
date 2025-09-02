<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Parent Child Application</title>
        <script type="text/javascript">
            function processAddItem(entryParent, entryParentLvl, theAction) {
                $("#entryParent").val(entryParent);
                $("#entryParentLvl").val(entryParentLvl);
                submitForm("sampleFormId", theAction + "Entry");
            }
            function moreInfo(entryParent, entryParentLvl) {
                if ($("#main_" + entryParent + "-" + entryParentLvl + "-more").hasClass("hidden")) {
                    $("#main_" + entryParent + "-" + entryParentLvl + "-more").removeClass("hidden");
                    $("#main_" + entryParent + "-" + entryParentLvl + "-ico").text("Less Info");
                } else {
                    $("#main_" + entryParent + "-" + entryParentLvl + "-more").addClass("hidden")
                    $("#main_" + entryParent + "-" + entryParentLvl + "-ico").text("More Info");
                }
            }
            function deleteMe(del, entryParent, entryParentLvl) {
                if ($("#main_" + entryParent + "-" + entryParentLvl).hasClass("deleted")) {
                    del.innerHTML = '<a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.delete"/></a>';
                    $(".add" + entryParent + "-" + entryParentLvl).removeClass("hidden");
                    $("#main_" + entryParent + "-" + entryParentLvl).removeClass("deleted");
                    $("#field_" + entryParentLvl + "_markedAsDel").val("N");
                    $("#main_" + entryParent + "-" + entryParentLvl + " :input").each(function () {
                        if (!($(this).hasClass("dropdown-toggle") || $(this).attr('type') === 'hidden')) {
                            $(this).prop("readonly", false);
                        }
                    });
                    console.log(0);
                    $(".main_" + entryParent + "-" + entryParentLvl + " :input").each(function () {
                        console.log(1);
                        if (!($(this).attr('type') === 'button')) {
                            if (!($(this).hasClass("dropdown-toggle") || $(this).attr('type') === 'hidden')) {
                                $(this).prop("readonly", false);
                            } else {
                                if ($(this).prop("id").indexOf("_markedAsDel") > 0) {
                                    $(this).val("N");
                                }
                            }
                        } else {
                            $(this).prop("disabled", false);
                            $(this).closest("div").find("li").each(function (i, li) {
                                if ((li.innerHTML+"").indexOf("Undo") > 0) {
                                    li.innerHTML = '<a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.delete"/></a>';
                                }
                            });
                        }
                    });
                    $(".main_" + entryParent + "-" + entryParentLvl).each(function () {
                        $(this).removeClass("deleted");
                    });
                } else {
                    del.innerHTML = '<a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.undo"/></a>';
                    $(".add" + entryParent + "-" + entryParentLvl).addClass("hidden");
                    $("#main_" + entryParent + "-" + entryParentLvl + " :input").each(function () {
                        if (!($(this).hasClass("dropdown-toggle") || $(this).attr('type') === 'hidden')) {
                            $(this).prop("readonly", true);
                        }
                    });
                    $("#main_" + entryParent + "-" + entryParentLvl).addClass("deleted");
                    $("#field_" + entryParentLvl + "_markedAsDel").val("Y");
                    $(".main_" + entryParent + "-" + entryParentLvl + " :input").each(function () {
                        if (!($(this).attr('type') === 'button')) {
                            if (!($(this).hasClass("dropdown-toggle") || $(this).attr('type') === 'hidden')) {
                                $(this).prop("readonly", true);
                            } else {
                                if ($(this).prop("id").indexOf("_markedAsDel") > 0) {
                                    $(this).val("Y");
                                }
                            }
                        } else {
                            $(this).prop("disabled", true);
                        }
                    });
                    $(".main_" + entryParent + "-" + entryParentLvl).each(function () {
                        $(this).addClass("deleted");
                    });
                }
            }
            $(document).ready(function () {
                $('#datepicker').datepicker({
                    autoclose: true,
                    format: "dd/mm/yyyy"
                });
            });
        </script>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form name="form" id="sampleFormId" class="" method="post">
            <s:hidden id="entryParent" name="entryParent" />
            <s:hidden id="entryParentLvl" name="entryParentLvl" />
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Parent Child Application <small><s:if test='theModel.ID == null || theModel.ID.equals("")'><s:text name="actionType.add"/></s:if><s:else><s:text name="actionType.edit"/></s:else></small></h4>
                    </div>
                    <div class="panel-body">

                    <s:hidden id="field__ID" name="" /><s:hidden id="field__markedAsDel" name="" />
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label"><s:text name="Parent.parent_age" /> <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield id="field_parent_age_str" cssClass="form-control" name="theModel.parent_age_str" value="%{theModel.parent_age_str}" required="required"/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label"><s:text name="Parent.parent_address" /> <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield id="field_parent_address" cssClass="form-control" name="theModel.parent_address" value="%{theModel.parent_address}" required="required"/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label"><s:text name="Parent.parent_own_car" /> <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield id="field_parent_own_car" cssClass="form-control" name="theModel.parent_own_car" value="%{theModel.parent_own_car}" required="required"/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label"><s:text name="Parent.parent_name" /> <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield id="field_parent_name" cssClass="form-control" name="theModel.parent_name" value="%{theModel.parent_name}" required="required"/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label"><s:text name="Parent.parent_state" /> <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield id="field_parent_state" cssClass="form-control" name="theModel.parent_state" value="%{theModel.parent_state}" required="required"/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label"><s:text name="Parent.parent_gender" /> <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield id="field_parent_gender" cssClass="form-control" name="theModel.parent_gender" value="%{theModel.parent_gender}" required="required"/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label"><s:text name="Parent.parent_dob" /> <font class="asterisk">*</font></label>
                        <div class="col-md-5">
                            <s:textfield id="field_parent_dob_str" cssClass="form-control" name="theModel.parent_dob_str" value="%{theModel.parent_dob_str}" required="required"/>
                        </div>
                    </div>
                    <table class="tb0_${status_0.index}" width="100%" class="table-condensed">
                        <tr>
                            <th width="20px"><i class="1 fa fa-plus-circle" onclick="processAddItem('0', '${status_0.index}', 'processAddChild')"></i></th><th><s:text name="Child.parent_id"/></th><th><s:text name="Child.child_age"/></th><th><s:text name="Child.child_name"/></th><th><s:text name="Child.child_dob"/></th>
                        </tr>
                        <s:iterator value="theModel.childList" status="status_0" var="var_0">
                            <tr id="main_0-${status_0.index}" class="">
                                <td>
                                    <div class="btn-group">
                                        <button type="button" class="btn btn-default block-xs dropdown-toggle" data-toggle="dropdown" aria-expanded="false" aria-haspopup="true"><i class="fa fa-caret-down noMargin"></i></button>
                                        <ul class="dropdown-menu sds-dropdown-menu">
                                            <s:if test='#var_0._markedAsDel.equals("Y")'><li onclick="deleteMe(this, '0', '${status_0.index}')"><a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.delete"/></a></li></s:if><s:else><li onclick="deleteMe(this, '0', '${status_0.index}')"><a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.delete"/></a></li></s:else>
                                            <li class="2 add0-${status_0.index}" onclick="processAddItem('0', '${status_0.index}', 'processAddGrandChild')"><a href="#"><i class="fa fa-plus-square-o"></i><s:text name="button.add"/></a></li>
                                        </ul>
                                    </div>
                                </td>
                                <td>
                                    <s:hidden id="field_%{#status_0.index}_ID" name="theModel.childList[%{#status_0.index}].ID" /><s:hidden id="field_%{#status_0.index}_markedAsDel" name="theModel.childList[%{#status_0.index}]._markedAsDel" />
                                    <s:textfield id='field_0parent_id' cssClass="form-control" name="theModel.childList[%{#status_0.index}].parent_id" value="%{#var_0.parent_id}" required="required"/>
                                </td>
                                <td>
                                    <s:textfield id='field_0child_age_str' cssClass="form-control" name="theModel.childList[%{#status_0.index}].child_age_str" value="%{#var_0.child_age_str}" required="required"/>
                                </td>
                                <td>
                                    <s:textfield id='field_0child_name' cssClass="form-control" name="theModel.childList[%{#status_0.index}].child_name" value="%{#var_0.child_name}" required="required"/>
                                </td>
                                <td>
                                    <s:textfield id='field_0child_dob_str' cssClass="form-control" name="theModel.childList[%{#status_0.index}].child_dob_str" value="%{#var_0.child_dob_str}" required="required"/>
                                </td>
                            </tr>
                            <s:if test="#var_0.grandChildList.size() > 0">
                                <tr id="detailItem_0-${status_0.index}">
                                    <td></td>
                                    <td colspan="4">
                                        <table class="tb0_0_${status_0.index}_${status_0_0.index}" width="100%" class="table-condensed">
                                            <tr>
                                                <th width="20px"></th><th><s:text name="GrandChild.child_id"/></th><th><s:text name="GrandChild.gc_name"/></th>
                                            </tr>
                                            <s:iterator value="#var_0.grandChildList" status="status_0_0" var="var_0_0">
                                                <tr id="main_0_0-${status_0.index}_${status_0_0.index}" class="main_0-${status_0.index} ">
                                                    <td>
                                                        <div class="btn-group">
                                                            <button type="button" class="btn btn-default block-xs dropdown-toggle" data-toggle="dropdown" aria-expanded="false" aria-haspopup="true"><i class="fa fa-caret-down noMargin"></i></button>
                                                            <ul class="dropdown-menu sds-dropdown-menu">
                                                                <s:if test='#var_0_0._markedAsDel.equals("Y")'><li onclick="deleteMe(this, '0_0', '${status_0.index}_${status_0_0.index}')"><a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.delete"/></a></li></s:if><s:else><li onclick="deleteMe(this, '0_0', '${status_0.index}_${status_0_0.index}')"><a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.delete"/></a></li></s:else>
                                                                </ul>
                                                            </div>
                                                        </td>
                                                        <td>
                                                        <s:hidden id="field_%{#status_0.index}_%{#status_0_0.index}_ID" name="theModel.childList[%{#status_0.index}].grandChildList[%{#status_0_0.index}].ID" /><s:hidden id="field_%{#status_0.index}_%{#status_0_0.index}_markedAsDel" name="theModel.childList[%{#status_0.index}].grandChildList[%{#status_0_0.index}]._markedAsDel" />
                                                        <s:textfield id='field_0_0child_id' cssClass="form-control" name="theModel.childList[%{#status_0.index}].grandChildList[%{#status_0_0.index}].child_id" value="%{#var_0_0.child_id}" required="required"/>
                                                    </td>
                                                    <td>
                                                        <s:textfield id='field_0_0gc_name' cssClass="form-control" name="theModel.childList[%{#status_0.index}].grandChildList[%{#status_0_0.index}].gc_name" value="%{#var_0_0.gc_name}" required="required"/>
                                                    </td>
                                                </tr>
                                            </s:iterator>
                                        </table>
                                    </td>
                                </tr>
                            </s:if>
                        </s:iterator>
                    </table>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 text-right">
                    <button class="btn btn-primary" type="submit" name="action:<s:if test='theModel.ID == null || theModel.ID.equals("")'>processInsert</s:if><s:else>processUpdate</s:else><s:property value="action" escapeHtml="true"/>" id="<s:if test='theModel.ID == null || theModel.ID.equals("")'>processInsert</s:if><s:else>processUpdate</s:else><s:property value="initCapAction" escapeHtml="true"/>" onclick="return validateForm(this.form, '<s:if test='theModel.ID == null || theModel.ID.equals("")'>insert</s:if><s:else>update</s:else>')"><i class="fa fa-save"></i>Save</button>        
                    <button class="btn btn-default" type="submit" name="action:cancel<s:property value="initCapAction" escapeHtml="true"/>" id="cancel<s:property value="action" escapeHtml="true"/>"><i class="fa fa-close"></i>Cancel</button>        
                </div>
            </div>
        </form>
        <div id="itemChangeLoader" class="hidden">
        </div>
    </body>
</html>
