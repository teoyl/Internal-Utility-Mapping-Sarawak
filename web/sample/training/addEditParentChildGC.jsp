<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="ParentChildGC.appName"/></title>
        <script type="text/javascript">
            function cbReadOnly(field) {
                if ($(field).prop("readonly")) {
                    return false;
                }
                return true;
            }
            function processAddItem(formId, entryParent, entryParentLvl, theAction) {
                $("#entryParent").val(entryParent);
                $("#entryParentLvl").val(entryParentLvl);
                submitForm(formId, theAction);
            }
            function moreInfo(entryParent, entryParentLvl) {
                if ($("#main_" + entryParent + "-" + entryParentLvl + "-more").hasClass("hidden")) {
                    $("#main_" + entryParent + "-" + entryParentLvl + "-more").removeClass("hidden");
                    $("#main_" + entryParent + "-" + entryParentLvl + "-ico").text("Less Info");
                    $("#field_" + entryParentLvl + "_hideShowMore").val("S");
                    $("#main_" + entryParent + "-" + entryParentLvl + "-more").find( "select" ).each(function() {
                        $(this).select2();
                    });
                } else {
                    $("#main_" + entryParent + "-" + entryParentLvl + "-more").addClass("hidden")
                    $("#main_" + entryParent + "-" + entryParentLvl + "-ico").text("More Info");
                    $("#field_" + entryParentLvl + "_hideShowMore").val("H");
                }
                return false;
            }
            function deleteMe(del, entryParent, entryParentLvl) {
                if ($("#main_" + entryParent + "-" + entryParentLvl).hasClass("deleted")) {
                    del.innerHTML = '<a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.markDelete"/></a>';
                    $(".add" + entryParent + "-" + entryParentLvl).removeClass("hidden");
                    $("#main_" + entryParent + "-" + entryParentLvl).removeClass("deleted");
                    $("#field_" + entryParentLvl + "_markedAsDel").val("N");
                    $("#main_" + entryParent + "-" + entryParentLvl + " :input").each(function () {
                        if (!($(this).hasClass("dropdown-toggle") || $(this).attr('type') === 'hidden')) {
                            if($(this).is("select")) {
                                $(this).prop("disabled", false);
                            } else {
                                $(this).prop("readonly", false);
                            }
                        }
                    });
                    $(".main_" + entryParent + "-" + entryParentLvl + " :input").each(function () {
                        if (!($(this).attr('type') === 'button')) {
                            if (!($(this).hasClass("dropdown-toggle") || $(this).attr('type') === 'hidden')) {
                                if($(this).is("select")) {
                                    $(this).prop("disabled", false);
                                } else {
                                    $(this).prop("readonly", false);
                                }
                            } else {
                                if ($(this).prop("id").indexOf("_markedAsDel") > 0) {
                                    $(this).val("N");
                                }
                            }
                        } else {
                            $(this).prop("disabled", false);
                        }
                    });
                    $(".main_" + entryParent + "-" + entryParentLvl).each(function () {
                        $(this).find("li").each(function(i, li) {
                            if ((li.innerHTML+"").indexOf('far fa-trash-alt') > 0) {
                                li.innerHTML = '<a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.markDelete"/></a>';
                            }
                        });
                        $(this).removeClass("deleted");
                    });
                } else {
                    del.innerHTML = '<a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.undoDelete"/></a>';
                    $(".add" + entryParent + "-" + entryParentLvl).addClass("hidden");
                    $("#main_" + entryParent + "-" + entryParentLvl).find(".redBorder").removeClass("redBorder");
                    $("#main_" + entryParent + "-" + entryParentLvl + " :input").each(function () {
                        if (!($(this).hasClass("dropdown-toggle") || $(this).attr('type') === 'hidden')) {
                            if($(this).is("select")) {
                                $(this).prop("disabled", true);
                            } else {
                                $(this).prop("readonly", true);
                            }
                        }
                    });
                    $("#main_" + entryParent + "-" + entryParentLvl).addClass("deleted");
                    $("#field_" + entryParentLvl + "_markedAsDel").val("Y");
                    $(".main_" + entryParent + "-" + entryParentLvl).find(".redBorder").removeClass("redBorder");
                    $(".main_" + entryParent + "-" + entryParentLvl + " :input").each(function () {
                        if (!($(this).attr('type') === 'button')) {
                            if (!($(this).hasClass("dropdown-toggle") || $(this).attr('type') === 'hidden')) {
                                if($(this).is("select")) {
                                    $(this).prop("disabled", true);
                                } else {
                                    $(this).prop("readonly", true);
                                }
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
                return false;
            }
            $(document).ready(function () {
                $(".tabToggle").click(function() {
                    $("#activeTab").val($(this).prop("id").replaceAll("tab_menu", ""));
                });
                $('.table-responsive').on('show.bs.dropdown', function () {
                    $('.table-responsive').css( "overflow", "inherit" );
                });
                $('.table-responsive').on('hide.bs.dropdown', function () {
                    $('.table-responsive').css( "overflow", "auto");
                });
                $(".deleted").each(function () {
                    if ($(this).prop("id")) {
                        $("."+$(this).prop("id")+"-other").find("input").each(function(i, theInput) {
                            $(this).prop("readonly", true);
                            $(this).addClass("markDeleted");
                        });
                    }
                    $(this).find("input").each(function(i, theInput) {
                        if (!($(this).attr('type') === 'button')) {
                            if (!($(this).hasClass("dropdown-toggle") || $(this).attr('type') === 'hidden')) {
                                $(this).prop("readonly", true);
                                $(this).addClass("markDeleted");
                            } else {
                                if ($(this).prop("id").indexOf("_markedAsDel") > 0) {
                                    $(this).val("Y");
                                }
                            }
                        } else {
                            $(this).prop("disabled", true);
                            $(this).addClass("markDeleted");
                        }
                    });
                });
                $(".Numeric").each(function () {
                    $(this).inputFilter(function(value) {
                        return /^\d*$/.test(value);    // Allow digits only, using a RegExp
                    });
                });
                $(".Float").each(function () {
                    $(this).inputFilter(function(value) {
                        return /^-?\d*[.,]?\d{0,2}$/.test(value); 
                    });
                });
                $(".Date_Picker").each(function () {
                    $(this).datepicker({
                        autoclose: true,
                        format: "<s:text name="date_default_date_datepicker"/>"
                    });
                });
                
            });
        </script>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form name="form" id="ParentChildGCFormID" class="" method="post" action="loadAddPageParentChildGC">
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <s:hidden id="entryParent" name="entryParent" />
            <s:hidden id="activeTab" name="activeTab" />
            <s:hidden id="entryParentLvl" name="entryParentLvl" />
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4><s:text name="ParentChildGC.appName"/> <small><s:if test='model.ID == null || model.ID.equals("")'><s:text name="actionType.add"/></s:if><s:else><s:text name="actionType.edit"/></s:else></small></h4>
                </div>
                <div class="panel-body">
                    
<s:hidden id="field__ID" name="model.ID" /><s:hidden id="field__markedAsDel" name="model._markedAsDel" /><s:hidden id="field__hideShowMore" name="" />
<div class="row">
   <div class="col-md-6">
            <div class="form-horizontal form-group">
                <label class="col-md-5 control-label"><s:text name="Parent.parent_address" /> </label>
                <div class="col-md-7">
                    <s:textfield id="field_parent_address" maxlength='%{model.columnLengthMap["parent_address"]}'  cssClass="form-control " name="model.parent_address" value="%{model.parent_address}" />
                </div>
            </div>
   </div>
   <div class="col-md-6">
            <div class="form-horizontal form-group">
                <label class="col-md-5 control-label"><s:text name="Parent.parent_own_car" /> </label>
                <div class="col-md-7">
                    <s:textfield id="field_parent_own_car" maxlength='%{model.columnLengthMap["parent_own_car"]}'  cssClass="form-control " name="model.parent_own_car" value="%{model.parent_own_car}" />
                </div>
            </div>
</div>
   </div>
<div class="row">
   <div class="col-md-6">
            <div class="form-horizontal form-group">
                <label class="col-md-5 control-label"><s:text name="Parent.parent_name" /> </label>
                <div class="col-md-7">
                    <s:textfield id="field_parent_name" maxlength='%{model.columnLengthMap["parent_name"]}'  cssClass="form-control " name="model.parent_name" value="%{model.parent_name}" />
                </div>
            </div>
   </div>
   <div class="col-md-6">
            <div class="form-horizontal form-group">
                <label class="col-md-5 control-label"><s:text name="Parent.parent_state" /> </label>
                <div class="col-md-7">
                    <s:textfield id="field_parent_state" maxlength='%{model.columnLengthMap["parent_state"]}'  cssClass="form-control " name="model.parent_state" value="%{model.parent_state}" />
                </div>
            </div>
</div>
   </div>
<div class="row">
   <div class="col-md-6">
            <div class="form-horizontal form-group">
                <label class="col-md-5 control-label"><s:text name="Parent.parent_age" /> </label>
                <div class="col-md-7">
                    <s:textfield id="field_parent_age_str" maxlength='%{model.columnLengthMap["parent_age_str"]}'  cssClass="form-control Numeric" name="model.parent_age_str" value="%{model.parent_age_str}" />
                </div>
            </div>
   </div>
   <div class="col-md-6">
            <div class="form-horizontal form-group">
                <label class="col-md-5 control-label"><s:text name="Parent.parent_gender" /> </label>
                <div class="col-md-7">
                    <s:textfield id="field_parent_gender" maxlength='%{model.columnLengthMap["parent_gender"]}'  cssClass="form-control " name="model.parent_gender" value="%{model.parent_gender}" />
                </div>
            </div>
</div>
   </div>
<div class="row">
   <div class="col-md-6">
            <div class="form-horizontal form-group">
                <label class="col-md-5 control-label"><s:text name="Parent.parent_dob" /> </label>
                <div class="col-md-7">
                    <s:textfield id="field_parent_dob_str" maxlength='%{model.columnLengthMap["parent_dob_str"]}'  cssClass="form-control Date_Picker" name="model.parent_dob_str" value="%{model.parent_dob_str}" />
                </div>
            </div>
   </div>
</div>
                    
<table class="table-condensed tb0_${status_0.index}" width="100%">
                <tr>
                    <th width="20px"><i class="fa fa-plus-circle" title="<s:text name='ParentChildGC.addChildTitle'/>" onclick="processAddItem('ParentChildGCFormID', '0','${status_0.index}', 'processAddChildListParentChildGC')"></i></th><th><s:text name="Child.parent_id"/></th><th><s:text name="Child.child_name"/></th><th><s:text name="Child.child_age"/></th><th><s:text name="Child.child_dob"/></th>
                </tr>
            <s:iterator value="model.childList" status="status_0" var="var_0">
                <tr id="main_0-${status_0.index}" class="<s:if test='#var_0._markedAsDel.equals("Y")'>deleted </s:if>">
                    <td>
                        <div class="btn-group">
                            <button type="button" class="btn btn-default block-xs dropdown-toggle btn-dropdown-sds ge-more" data-toggle="dropdown" aria-expanded="false" aria-haspopup="true"><i class="fa fa-caret-down noMargin"></i></button>
                                <ul class="dropdown-menu sds-dropdown-menu">
                            <s:if test='#var_0._markedAsDel.equals("Y")'><li onclick="return deleteMe(this, '0','${status_0.index}');"><a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.undoDelete"/></a></li></s:if><s:else><li onclick="return deleteMe(this, '0','${status_0.index}');"><a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.markDelete"/></a></li></s:else>
                                    <li class="add0-${status_0.index}" onclick="processAddItem('ParentChildGCFormID', '0','${status_0.index}', 'processAddGrandChildListParentChildGC')"><a href="#"><i class="fa fa-plus-square-o"></i><s:text name="button.add"/></a></li>
                                </ul>
                        </div>
                    </td>
                    <td>
                       <s:hidden id="field_%{#status_0.index}_ID" name="model.childList[%{#status_0.index}].ID" /><s:hidden id="field_%{#status_0.index}_markedAsDel" name="model.childList[%{#status_0.index}]._markedAsDel" /><s:hidden id="field_%{#status_0.index}_hideShowMore" name="model.childList[%{#status_0.index}]._hideShowMore" />
                       <s:textfield id='model_childList%{#status_0.index}_parent_id' maxlength='%{model.childList.columnLengthMap["parent_id"]}'  cssClass="form-control " name="model.childList[%{#status_0.index}].parent_id" value="%{#var_0.parent_id}" />
                    </td>
                    <td>
                       <s:textfield id='model_childList%{#status_0.index}_child_name' maxlength='%{model.childList.columnLengthMap["child_name"]}'  cssClass="form-control " name="model.childList[%{#status_0.index}].child_name" value="%{#var_0.child_name}" />
                    </td>
                    <td>
                       <s:textfield id='model_childList%{#status_0.index}_child_age_str' maxlength='%{model.childList.columnLengthMap["child_age_str"]}'  cssClass="form-control Numeric" name="model.childList[%{#status_0.index}].child_age_str" value="%{#var_0.child_age_str}" />
                    </td>
                    <td>
                       <s:textfield id='model_childList%{#status_0.index}_child_dob_str' maxlength='%{model.childList.columnLengthMap["child_dob_str"]}'  cssClass="form-control Date_Picker" name="model.childList[%{#status_0.index}].child_dob_str" value="%{#var_0.child_dob_str}" />
                    </td>
                </tr>
<s:if test="#var_0.grandChildList.size() > 0">
                <tr id="detailItem_0-${status_0.index}">
                    <td></td>
                    <td colspan="4">
                        
<table class="table-condensed tb0_0_${status_0.index}_${status_0_0.index}" width="100%">
                    <tr>
                        <th width="20px"></th><th><s:text name="GrandChild.gc_name"/></th><th><s:text name="GrandChild.child_id"/></th>
                    </tr>
                <s:iterator value="#var_0.grandChildList" status="status_0_0" var="var_0_0">
                    <tr id="main_0_0-${status_0.index}_${status_0_0.index}" class="<s:if test='#var_0_0._markedAsDel.equals("Y")'>deleted </s:if>main_0-${status_0.index} ">
                        <td>
                            <div class="btn-group">
                                <button type="button" class="<s:if test='#var_0._markedAsDel.equals("Y")'>disabled </s:if>btn btn-default block-xs dropdown-toggle btn-dropdown-sds ge-more" data-toggle="dropdown" aria-expanded="false" aria-haspopup="true"><i class="fa fa-caret-down noMargin"></i></button>
                                <ul class="dropdown-menu sds-dropdown-menu">
                                <s:if test='#var_0_0._markedAsDel.equals("Y")'><li onclick="return deleteMe(this, '0_0','${status_0.index}_${status_0_0.index}');"><a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.undoDelete"/></a></li></s:if><s:else><li onclick="return deleteMe(this, '0_0','${status_0.index}_${status_0_0.index}');"><a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.markDelete"/></a></li></s:else>
                                </ul>
                            </div>
                        </td>
                    <td>
                       <s:hidden id="field_%{#status_0.index}_%{#status_0_0.index}_ID" name="model.childList[%{#status_0.index}].grandChildList[%{#status_0_0.index}].ID" /><s:hidden id="field_%{#status_0.index}_%{#status_0_0.index}_markedAsDel" name="model.childList[%{#status_0.index}].grandChildList[%{#status_0_0.index}]._markedAsDel" /><s:hidden id="field_%{#status_0.index}_%{#status_0_0.index}_hideShowMore" name="model.childList[%{#status_0.index}].grandChildList[%{#status_0_0.index}]._hideShowMore" />
                       <s:textfield id='model_childList%{#status_0.index}_grandChildList%{#status_0_0.index}_gc_name' maxlength='%{model.childList.grandChildList.columnLengthMap["gc_name"]}'  cssClass="form-control " name="model.childList[%{#status_0.index}].grandChildList[%{#status_0_0.index}].gc_name" value="%{#var_0_0.gc_name}" />
                    </td>
                    <td>
                       <s:textfield id='model_childList%{#status_0.index}_grandChildList%{#status_0_0.index}_child_id' maxlength='%{model.childList.grandChildList.columnLengthMap["child_id"]}'  cssClass="form-control " name="model.childList[%{#status_0.index}].grandChildList[%{#status_0_0.index}].child_id" value="%{#var_0_0.child_id}" />
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
                    <button class="btn btn-primary" type="submit" name="saveButton" id="<s:if test='model.ID == null || model.ID.equals("")'>processInsert</s:if><s:else>processUpdate</s:else>ParentChildGC" onclick="return submitForm_bshor(this.form.id, '<s:if test='model.ID == null || model.ID.equals("")'>processInsert</s:if><s:else>processUpdate</s:else><s:property value="action" escapeHtml="true"/>');"><i class="fa fa-save"></i>Save</button>        
                    <button class="btn btn-default" type="button" name="cancelButton" id="cancel<s:property value="action" escapeHtml="true"/>" onclick="return submitForm(this.form.id, 'cancelParentChildGC');"><i class="fa fa-close"></i>Cancel</button>        
                </div>
            </div>
        </form>
        <div id="itemChangeLoader" class="hidden">
        </div>
    </body>
</html>
