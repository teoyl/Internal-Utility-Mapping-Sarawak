<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/lookup.js"></script>
        <script type="text/javascript" src="pages/scripts/controls.js"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <%--text editor js--%>
        <script language="Javascript" src="pages/scripts/jquery-1.3.2.min.js" type="text/javascript"></script>
        <script language="Javascript" src="pages/scripts/htmlbox.colors.js" type="text/javascript"></script>
        <script language="Javascript" src="pages/scripts/htmlbox.styles.js" type="text/javascript"></script>
        <script language="Javascript" src="pages/scripts/htmlbox.syntax.js" type="text/javascript"></script>
        <script language="Javascript" src="pages/scripts/xhtml.js" type="text/javascript"></script>

        <script language="Javascript" src="pages/scripts/htmlbox.full.js" type="text/javascript"></script>
        <%--POPUP--%>
        <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" type="text/css" />
        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" type="text/css" />
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>

        <script language="javascript">
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
                this.aa = new Array("system_id", "<s:text name='tc.system' />");
                this.ab = new Array("tc_version", "<s:text name='tc.ver' />");
                this.ac = new Array("tc_tnc_path", "<s:text name='tc.path' />");
                //this.ac = new Array("tc_clause", "<s:text name='tc.clause' />");
            }

            function previewTemplate() {
                var lsData = document.getElementById("tc_clause").value;
                lsData = lsData.replace(/param>/g, 'b>');
                lsData = "<p>" + lsData + "</p>";
//                document.getElementById("divPreview").innerHTML = lsData;
//                divwin = dhtmlwindow.open('divbox', 'div', 'divPreview', 'Preview', 'width=500px,height=450px,left=100px,top=30px,resize=1,scrolling=1');
                $("#previewModalDiv").find('.myModalContent').html(lsData);
                $('#previewModalDiv').modal('show');
                return false;
            }

            function setLatestVer() {
                if (confirmLatestVersion() == true) {
                    document.getElementById("latestVerFlag").innerHTML = 'Yes';
                    document.getElementById("latestVerFlag_").value = "Y";
                }
            }
            function confirmLatestVersion() {
                var answer = confirm("You are about to set this Terms & Conditions as the latest version. Do you want to proceed?");
                return answer;
            }
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="system.name"/> - <s:text name="termsConditions" /> - <s:text name="actionType.add" /></title>
        <%--<s:head />--%>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
            <form action="processInsertTermsConds" id="tcId" name="tcForm" method="post" enctype="multipart/form-data">
            <s:hidden name="action" />
            <s:hidden theme="simple" name="tc_id" value="%{model.tc_id}" />
            <s:hidden theme="simple" name="tc_clause"/>
            <s:hidden theme="simple" name="tc_latest"/>    

            <div class="row">
                <div class="col-md-6">
                    <div class="form-group form-group-default form-group-default-select2 required">
                        <label><s:text name="tc.system"/></label>
                        <s:select cssClass="full-width"  data-init-plugin="select2" theme="simple" name="system_id" list="systemList" listKey="keyData" listValue="valueData" value="%{model.system_id}"/>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group form-group-default required">
                        <label><s:text name="tc.ver"/></label>
                        <s:textfield theme="simple" size="5" name="tc_version" maxLength='%{model.columnLengthMap["tc_version"]}' value="%{model.tc_version}" cssClass="form-control"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <s:if test="model.tc_tnc_path != ''">
                    <div class="col-md-6">
                        <div class="form-group form-group-default required">
                            <label><s:text name="tc.path"/> <a href="javascript:;" onclick="previewTemplate()"><i class="fa fa-eye"></i></a></label>
                            <s:hidden name="tc_tnc_path" value="%{model.tc_tnc_path}"/>
                            <s:url action="downloadTermsCondsFile" id="fileDownload" namespace="/"></s:url>
                            <s:a href="%{fileDownload}?fileName=%{model.tc_tnc_path}"><s:property value="%{model.tc_tnc_path}"/></s:a>
                                <%--br/>
                                <input type="button" id="btnPreview" class="defaultButton" value="Preview" onclick="previewTemplate()" /--%>
                            </div>
                        </div>
                </s:if>
                <s:else>
                    <div class="col-md-6">
                        <div class="form-group form-group-default required">
                            <label><s:text name="tc.path"/></label>
                            <s:file theme="simple" name="fileUpload" size="40"/>
                        </div>
                    </div>
                </s:else>
                <div class="col-md-6">
                    <div class="form-group form-group-default">
                        <label><s:text name="tc.latest"/></label>
                        <s:hidden name="latestVerFlag_"/>
                        <s:if test="latestVerFlag_ != ''">
                            <span id="latestVerFlag">
                                <s:if test='latestVerFlag_.equals("Y")'><s:text name="tc.%{latestVerFlag_}"/></s:if> 
                                <s:if test='latestVerFlag_.equals("N")'><input type="button" value="Set as latest version" onclick="setLatestVer()" /></s:if>
                                </span>
                        </s:if>
                        <s:else>
                            <span id="latestVerFlag">
                                <input type="button" value="Set as latest version" onclick="setLatestVer()" />
                            </span>
                        </s:else>
                    </div>
                </div>
            </div><br/><br/>
            <div class="row">
                <div class="col-md-12 text-right">
                    <s:if test='model.ID == null || model.ID.equals("")'>
                        <button class="btn btn-primary" type="submit" name="action:processInsertTermsConds" id="processInsertTermsConds"  onclick="return localValidateForm(this.form, 'insert')"><i class="fa fa-save"></i>Save</button>
                        <%--<s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processInsertTermsConds" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>--%>
                    </s:if>
                    <s:else>
                        <button class="btn btn-primary" type="submit" name="action:processUpdateTermsConds" id="processUpdateTermsConds" onclick="return localValidateForm(this.form, 'update')"><i class="fa fa-save"></i>Save</button>
                        <%--<s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processUpdateTermsConds" value="Update" onclick="return localValidateForm(this.form, 'update')"/>--%>
                    </s:else>
                    <button class="btn btn-default" type="submit" name="action:cancelTermsConds" id="cancelTermsConds"><i class="fa fa-close"></i>Cancel</button>
                    <%--<s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="cancelTermsConds" value="Cancel"/>--%>

                </div>
            </div>





            <!--            <div class="titleFramework">
                            <span class="titleText"><s:text name="termsConditions" /></span>
                            <span class="titleActionTypeText"> | <s:text name="actionType.add" /></span><br>
                        </div>
                        <div class="xbox">-->
            <%--div class="panel panel-default ">
                <div class="panel-heading ">
                    <h3 class="panel-title"> 
                        <span class="titleText"><s:text name="termsConditions" /></span>
                        <span class="titleActionTypeText"> | <s:text name="actionType.add" /></span>
                    </h3>
                </div>
                <div class="panel-body">
                    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form table borderless">
                        <tr>
                            <td align="right">
                                <s:if test='model.ID == null || model.ID.equals("")'>
                                    <s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processInsertTermsConds" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>
                                </s:if><s:else>
                                    <s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processUpdateTermsConds" value="Update" onclick="return localValidateForm(this.form, 'update')"/>
                                </s:else>
                                <s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="cancelTermsConds" value="Cancel"/>
                            </td>
                        </tr>
                    </table>
                    <table class="table borderless">


                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="250px" align=left><s:text name="tc.system"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td> 
                            <td><s:select cssClass="field input-md form-control" theme="simple" name="system_id" list="systemList" listKey="keyData" listValue="valueData" value="%{model.system_id}"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td align=left><s:text name="tc.ver"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:textfield theme="simple" size="5" name="tc_version" maxLength='%{model.columnLengthMap["tc_version"]}' value="%{model.tc_version}" cssClass="input-md form-control"/></td>
                        </tr>
                        <!--<tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left valign="top"><s:text name="tc.clause"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px" valign="top">:</td>
                            <td align="left">
                                <s:textarea theme="simple" cols="100"  rows="25" id="tc_clause" name="tc_clause" maxLength='%{model.columnLengthMap["tc_clause"]}' value="%{model.tc_clause}"/>
                                <br><br/>
                                <input type="button" id="btnPreview" value="Preview" onclick="previewTemplate()" />
                            </td>
                        </tr>-->
                        <s:if test="model.tc_tnc_path != ''">
                            <tr>
                                <td width="20px">&nbsp;</td>
                                <td align=left valign="top"><s:text name="tc.path"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                                <td width="3px" valign="top">:</td>
                                <td align="left">
                                    <s:hidden name="tc_tnc_path" value="%{model.tc_tnc_path}"/>
                                    <s:url action="downloadTermsCondsFile" id="fileDownload" namespace="/"></s:url>
                                    <s:a href="%{fileDownload}?fileName=%{model.tc_tnc_path}"><s:property value="%{model.tc_tnc_path}"/></s:a>
                                        <br/>
                                        <input type="button" id="btnPreview" class="defaultButton" value="Preview" onclick="previewTemplate()" />
                                    </td>
                                </tr>
                        </s:if>
                        <s:else>
                            <tr>
                                <td width="20px">&nbsp;</td>
                                <td align=left valign="top"><s:text name="tc.path"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                                <td width="3px" valign="top">:</td>
                                <td align="left">
                                    <s:file theme="simple" name="fileUpload" size="40"/>
                                    <br><br/>
                                    <!--                            <input type="button" id="btnPreview" value="Preview" onclick="previewTemplate()" />-->
                                </td>
                            </tr>
                        </s:else>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td align=left><s:text name="tc.latest"/></td>
                            <td width="3px">:</td>
                            <td>
                                <s:hidden name="latestVerFlag_"/>
                                <s:if test="latestVerFlag_ != ''">
                                    <span id="latestVerFlag">
                                        <s:if test='latestVerFlag_.equals("Y")'><s:text name="tc.%{latestVerFlag_}"/></s:if> 
                                        <s:if test='latestVerFlag_.equals("N")'><input type="button" value="Set as latest version" onclick="setLatestVer()" /></s:if>
                                        </span>
                                </s:if><s:else>
                                    <span id="latestVerFlag"><input type="button" value="Set as latest version" onclick="setLatestVer()" /></span>
                                    </s:else>
                            </td>
                        </tr>
                        <!--text editor-->
                        <script type="text/javascript">
                            $("#ha").css("height", "100%").css("width", "100%").htmlbox({
                                toolbars: [
                                    [
                                        // Cut, Copy, Paste
                                        "separator", "cut", "copy", "paste",
                                        // Undo, Redo
                                        "separator", "undo", "redo",
                                        // Bold, Italic, Underline, Strikethrough, Sup, Sub
                                        "separator", "bold", "italic", "underline", "strike", "sup", "sub",
                                        // Left, Right, Center, Justify
                                        "separator", "justify", "left", "center", "right",
                                        // Ordered List, Unordered List, Indent, Outdent
                                        "separator", "ol", "ul", "indent", "outdent",
                                        // Hyperlink, Remove Hyperlink, Image
                                        "separator", "link", "unlink", "image"

                                    ],
                                    [// Show code
                                        "separator", "code",
                                        // Formats, Font size, Font family, Font color, Font, Background
                                        "separator", "formats", "fontsize", "fontfamily",
                                        "separator", "fontcolor", "highlight",
                                    ],
                                ],
                                skin: "blue"
                            });
                        </script>                          
                    </table>
                </div>
            </div--%>


            <script type="text/javascript">
                $("#ha").css("height", "100%").css("width", "100%").htmlbox({
                    toolbars: [
                        [
                            // Cut, Copy, Paste
                            "separator", "cut", "copy", "paste",
                            // Undo, Redo
                            "separator", "undo", "redo",
                            // Bold, Italic, Underline, Strikethrough, Sup, Sub
                            "separator", "bold", "italic", "underline", "strike", "sup", "sub",
                            // Left, Right, Center, Justify
                            "separator", "justify", "left", "center", "right",
                            // Ordered List, Unordered List, Indent, Outdent
                            "separator", "ol", "ul", "indent", "outdent",
                            // Hyperlink, Remove Hyperlink, Image
                            "separator", "link", "unlink", "image"

                        ],
                        [// Show code
                            "separator", "code",
                            // Formats, Font size, Font family, Font color, Font, Background
                            "separator", "formats", "fontsize", "fontfamily",
                            "separator", "fontcolor", "highlight",
                        ],
                    ],
                    skin: "blue"
                });
            </script>   
        </form>
        <%--div id="divPreview" style="display:none;">
            &nbsp;
        </div--%>

        <!--Preview Modal-->
        <div id="previewModalDiv" class="modal fade" tabindex="-1" data-width="60%" data-height="" style="display: none;"  data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h3 class="title-v2">Preview</h3><br>
                    </div>
                    <div class="modal-body myModalContent"></div>
                    <div class="modal-footer">
                        <button type="button" data-dismiss="modal" class="btn btn-default">Close</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!--end responsive-->
    </body>
</html>