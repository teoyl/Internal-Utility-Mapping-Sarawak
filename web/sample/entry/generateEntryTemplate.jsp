<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ReplaceApplicationTitle</title>
        _replace_uppy_js_
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
        _replace_uppy_css_
        _replace_uppy_style_
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form name="form" id="__formId__FormID" class="" method="post" action="loadAddPage__initCapAction__">
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <s:hidden id="entryParent" name="entryParent" />
            <s:hidden id="activeTab" name="activeTab" />
            <s:hidden id="entryParentLvl" name="entryParentLvl" />
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>ReplaceApplicationTitle <small><s:if test='theModel.ID == null || theModel.ID.equals("")'><s:text name="actionType.add"/></s:if><s:else><s:text name="actionType.edit"/></s:else></small></h4>
                </div>
                <div class="panel-body">
                    //ReplaceContentHere
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 text-right">
                    <button class="btn btn-primary" type="submit" name="saveButton" id="<s:if test='theModel.ID == null || theModel.ID.equals("")'>processInsert</s:if><s:else>processUpdate</s:else>__initCapAction__" onclick="return submitForm_bshor(this.form.id, '<s:if test='theModel.ID == null || theModel.ID.equals("")'>processInsert</s:if><s:else>processUpdate</s:else>__initCapAction__');"><i class="fa fa-save"></i>Save</button>        
                    <button class="btn btn-default" type="button" name="cancelButton" id="cancel__initCapAction__" onclick="return submitForm(this.form.id, 'cancel__initCapAction__');"><i class="fa fa-close"></i>Cancel</button>        
                </div>
            </div>
        </form>
        <div id="itemChangeLoader" class="hidden">
        </div>
    </body>
</html>
