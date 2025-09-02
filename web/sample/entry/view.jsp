<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Entry to be generated</title>
        <script type="text/javascript">
        </script>
    </head>
    <body>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4>Entry to be generated<small> view</small></h4>
            </div>
            <div class="panel-body">
                
<s:hidden id="field__ID" name="model.ID" /><s:hidden id="field__markedAsDel" name="model._markedAsDel" /><s:hidden id="field__hideShowMore" name="" />
<div class="row">
   <div class="col-md-6">
            <div class="form-horizontal form-group">
                <label class="col-md-4 control-label"><s:text name="Parent Address" /> </label>
                <div class="col-md-5">
                    <s:textfield id="field_parent_address" maxlength='%{model.columnLengthMap["parent_address"]}'  cssClass="form-control " name="model.parent_address" value="%{model.parent_address}" />
                </div>
            </div>
   </div>
   <div class="col-md-6">
            <div class="form-horizontal form-group">
                <label class="col-md-4 control-label"><s:text name="Parent Own Car" /> </label>
                <div class="col-md-5">
                    <s:textfield id="field_parent_own_car" maxlength='%{model.columnLengthMap["parent_own_car"]}'  cssClass="form-control " name="model.parent_own_car" value="%{model.parent_own_car}" />
                </div>
            </div>
</div>
   </div>
<div class="row">
   <div class="col-md-6">
            <div class="form-horizontal form-group">
                <label class="col-md-4 control-label"><s:text name="Parent Name" /> </label>
                <div class="col-md-5">
                    <s:textfield id="field_parent_name" maxlength='%{model.columnLengthMap["parent_name"]}'  cssClass="form-control " name="model.parent_name" value="%{model.parent_name}" />
                </div>
            </div>
   </div>
   <div class="col-md-6">
            <div class="form-horizontal form-group">
                <label class="col-md-4 control-label"><s:text name="Parent State" /> <font class="asterisk">*</font></label>
                <div class="col-md-5">
                    <s:select id="field_parent_state" list="sampleList" listKey="keyData" listValue="valueData" name="model.parent_state" cssClass="form-control sds-dropdown" value="%{model.parent_state}" required="required" />
                </div>
            </div>
</div>
   </div>
<div class="row">
   <div class="col-md-6">
            <div class="form-horizontal form-group">
                <label class="col-md-4 control-label"><s:text name="Parent Age" /> </label>
                <div class="col-md-5">
                    <s:textfield id="field_parent_age_str" maxlength='%{model.columnLengthMap["parent_age_str"]}'  cssClass="form-control Numeric" name="model.parent_age_str" value="%{model.parent_age_str}" />
                </div>
            </div>
   </div>
   <div class="col-md-6">
            <div class="form-horizontal form-group">
                <label class="col-md-4 control-label"><s:text name="Parent Gender" /> </label>
                <div class="col-md-5">
                    <s:textfield id="field_parent_gender" maxlength='%{model.columnLengthMap["parent_gender"]}'  cssClass="form-control " name="model.parent_gender" value="%{model.parent_gender}" />
                </div>
            </div>
</div>
   </div>
<div class="row">
   <div class="col-md-6">
            <div class="form-horizontal form-group">
                <label class="col-md-4 control-label"><s:text name="Parent DOB" /> </label>
                <div class="col-md-5">
                    <s:textfield id="field_parent_dob_str" maxlength='%{model.columnLengthMap["parent_dob_str"]}'  cssClass="form-control Date_Picker" name="model.parent_dob_str" value="%{model.parent_dob_str}" />
                </div>
            </div>
   </div>
</div>
                    
<table class="table-condensed tb0_${status_0.index}" width="100%">
                <tr>
                    <th width="20px"><i class="fa fa-plus-circle" title="<s:text name='TT.addChildTitle'/>" onclick="processAddItem('tTFormID', '0','${status_0.index}', 'processAddChildListtT')"></i></th><th><s:text name="Parent ID"/></th><th><s:text name="Child Name"/></th><th><s:text name="Child Age"/></th><th><s:text name="Child DOB"/></th>
                </tr>
            <s:iterator value="model.childList" status="status_0" var="var_0">
                <tr id="main_0-${status_0.index}" class="<s:if test='#var_0._markedAsDel.equals("Y")'>deleted </s:if>">
                    <td>
                        <div class="btn-group">
                            <button type="button" class="btn btn-default block-xs dropdown-toggle btn-dropdown-sds ge-more" data-toggle="dropdown" aria-expanded="false" aria-haspopup="true"><i class="fa fa-caret-down noMargin"></i></button>
                                <ul class="dropdown-menu sds-dropdown-menu">
                            <s:if test='#var_0._markedAsDel.equals("Y")'><li onclick="return deleteMe(this, '0','${status_0.index}');"><a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.undoDelete"/></a></li></s:if><s:else><li onclick="return deleteMe(this, '0','${status_0.index}');"><a href="#"><i class="fa far fa-trash-alt"></i><s:text name="button.markDelete"/></a></li></s:else>
                                    <li class="add0-${status_0.index}" onclick="processAddItem('tTFormID', '0','${status_0.index}', 'processAddGrandChildListtT')"><a href="#"><i class="fa fa-plus-square-o"></i><s:text name="button.add"/></a></li>
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
                        <th width="20px"></th><th><s:text name="Gc Name"/></th><th><s:text name="Child ID"/></th>
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
    </body>
</html>
