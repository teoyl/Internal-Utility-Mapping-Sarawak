<%-- 
    Document   : userAccess_step1
    Created on : Feb 18, 2014, 3:53:01 PM
    Author     : Delvene
--%>

<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-dojo-tags" prefix="sx"%>
<sx:head parseContent="true" debug="false" />

<form method="post" id="prForm" class="prForm" action="processUpdateUserAccess" >
    <s:hidden theme="simple" name="action" />
    <s:hidden theme="simple" name="id" />
    <s:hidden theme="simple" name="model.ID" />
    <s:hidden theme="simple" name="model.emp_id" />
    <s:hidden theme="simple" name="currStep_" />

    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
        <tr>
            <td align="right" class="button-container">
                <s:submit type="submit" cssClass="defaultButton buttonNextStep" theme="simple" action="nextStepUserAccess" value="" title="%{getText('mohon.button.next')}"/>
                <s:submit type="submit" cssClass="defaultButton buttonBackToList" theme="simple" action="cancelUserAccess" value="" title="%{getText('button.back')}"/>
            </td>
        </tr>
    </table>

    <table cellpadding="0" cellspacing="0" width="100%">
        <tr valign="top">
            <%--Pelantikan Penyandang--%>
            <td>
                <table cellpadding="3" cellspacing="0" width="100%">
                    <tr class="sub_header_bg">
                        <td></td>
                        <td colspan="4"><s:text name="userAccess.userBA"/></td>
                    </tr>
                    <tr>
                        <td height="10px"><%--some space--%></td>
                    </tr>
                    <tr valign="top">
                        <td width="20px"></td>
                        <td class="label_imp" width="200px"><s:text name="postinfo.postStandard"/></td>
                        <td width="10px" class="label_imp">:</td>
                        <td align="left">
                            ${currentViewModel_.postLantikActiveList[0].postInfo.postName.post_name}<br>
                            ${currentViewModel_.postLantikActiveList[0].postInfo.post_id}<br>
                            ${currentViewModel_.postLantikActiveList[0].postInfo.establishment_ba.department.dept_name}<br>
                            ${currentViewModel_.postLantikActiveList[0].postInfo.establishment_ba.noAndDesc}<br>
                            <s:text name="postinfo.appointStatus.%{currentViewModel_.postLantikActiveList[0].postInfo.post_appoint_status}"/>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td class="label_imp"><s:text name="postLantik.appointReason"/></td>
                        <td class="label_imp">:</td>
                        <td>
                            <s:if test="currentViewModel_.postLantikActiveList[0].appointed_reason_id != null && currentViewModel_.postLantikActiveList[0].appointed_reason_id != ''">
                                ${currentViewModel_.postLantikActiveList[0].appointed_reason_idModel.code_desc}
                            </s:if>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td class="label_imp"><s:text name="postLantik.appointType"/></td>
                        <td class="label_imp">:</td>
                        <td>
                            <s:if test="currentViewModel_.postLantikActiveList[0].appoint_type_id != null && currentViewModel_.postLantikActiveList[0].appoint_type_id != ''">
                                ${currentViewModel_.postLantikActiveList[0].appoint_type_idModel.code_desc}
                            </s:if>
                        </td>
                    </tr>
                    <tr valign="top">
                        <td></td>
                        <td class="label_imp"><s:text name="postLantik.startDateKuasa"/></td>
                        <td class="label_imp">:</td>
                        <td>${currentViewModel_.postLantikActiveList[0].appointed_date_str}</td>
                    </tr>
                    <tr valign="top">
                        <td></td>
                        <td class="label_imp"><s:text name="postLantik.estVacantDate"/></td>
                        <td class="label_imp">:</td>
                        <td>${currentViewModel_.postLantikActiveList[0].est_vacant_date_str}</td>
                    </tr>
                    <tr valign="top">
                        <td></td>
                        <td class="label_imp"><s:text name="postLantik.endDateKuasa"/></td>
                        <td class="label_imp">:</td>
                        <td>${currentViewModel_.postLantikActiveList[0].end_date_str}</td>
                    </tr>
                    <tr valign="top">
                        <td></td>
                        <td class="label_imp"><s:text name="postLantik.postSalaryGrade"/></td>
                        <td class="label_imp">:</td>
                        <td>${currentViewModel_.postLantikActiveList[0].postInfo.post_grade_text}</td>
                        <td></td>
                    </tr>
                    <tr valign="top">
                        <td></td>
                        <td class="label_imp"><s:text name="postLantik.appointGred"/></td>
                        <td class="label_imp">:</td>
                        <td>
                            <s:if test="currentViewModel_.postLantikActiveList[0].post_appointed_gred != null && currentViewModel_.postLantikActiveList[0].post_appointed_gred != ''">
                                ${currentViewModel_.postLantikActiveList[0].salary_gradeModel.sal_grade_code}
                            </s:if>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td class="label_imp"><s:text name="postLantik.serviceProfileScheme"/></td>
                        <td class="label_imp">:</td>
                        <td>${currentViewModel_.postLantikActiveList[0].service_profile_scheme_str}</td>
                    </tr>
                    <tr>
                        <td></td>
                        <td class="label_imp"><s:text name="PR.servProfile.servGroup"/></td>
                        <td class="label_imp">:</td>
                        <td><%--${postLantik.service_profile_scheme_str}--%></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td class="label_imp"><s:text name="postLantik.active"/></td>
                        <td class="label_imp">:</td>
                        <td>${currentViewModel_.postLantikActiveList[0].active_flag_str}</td>
                    </tr>
                    <tr valign="top">
                        <td></td>
                        <td class="label_imp"><s:text name="postLantik.remark"/></td>
                        <td class="label_imp">:</td>
                        <td>${currentViewModel_.postLantikActiveList[0].remark}</td>
                    </tr>
                    <tr>
                        <td height="10px"><%--some space--%></td>
                    </tr>
                </table>
            </td>
            <%--Pelantikan Penyandang End--%>

            <%--Penempatan Penyandang--%>
            <s:if test="currentViewModel_.personalPostActiveList.size() > 0">
                <td style="border-left: 1px solid #AAAAAA" width="50%">
                    <table cellpadding="3" cellspacing="0" width="100%">
                        <tr class="sub_header_bg">
                            <td></td>
                            <td colspan="3"><s:text name="userAccess.userBU"/></td>
                        </tr>
                        <tr>
                            <td height="10px"><%--some space--%></td>
                        </tr>
                        <tr valign="top">
                            <td width="20px"></td>
                            <td class="label_imp" width="200px"><s:text name="postOper.postStandard"/></td>
                            <td width="10px" class="label_imp">:</td>
                            <td align="left">
                                ${currentViewModel_.personalPostActiveList[0].postOperation.postName.post_name}<br>
                                ${currentViewModel_.personalPostActiveList[0].postOperation.establishment_bu.department.dept_name}<br>
                                ${currentViewModel_.personalPostActiveList[0].postOperation.establishment_bu.noAndDesc}<br>
                                <s:text name="postinfo.vacantStatus.%{currentViewModel_.personalPostActiveList[0].postOperation.post_status}"/>
                            </td>
                        </tr>
                        <tr valign="top">
                            <td></td>
                            <td class="label_imp"><s:text name="postSandang.jobDesc"/></td>
                            <td class="label_imp">:</td>
                            <td>${currentViewModel_.personalPostActiveList[0].job_description}</td>
                        </tr>
                        <tr>
                            <td></td>
                            <td class="label_imp"><s:text name="postSandang.startDateKuasa"/></td>
                            <td class="label_imp">:</td>
                            <td>${currentViewModel_.personalPostActiveList[0].appointed_date_str}</td>
                        </tr>
                        <tr>
                            <td></td>
                            <td class="label_imp"><s:text name="postSandang.endDateKuasa"/></td>
                            <td class="label_imp">:</td>
                            <td>${currentViewModel_.personalPostActiveList[0].end_date_str}</td>
                        </tr>
                        <tr>
                            <td></td>
                            <td class="label_imp"><s:text name="postSandang.heldAgainst"/></td>
                            <td class="label_imp">:</td>
                            <td>${currentViewModel_.personalPostActiveList[0].held_against_flag_str}</td>
                        </tr>
                        <s:if test='currentViewModel_.personalPostActiveList[0].held_against_flag.equals("Y")'>
                            <tr>
                                <td></td>
                                <td class="label_imp"><s:text name="postSandang.heldAgainst.reason"/></td>
                                <td class="label_imp">:</td>
                                <td>
                                    <s:if test="currentViewModel_.personalPostActiveList[0].held_against_id != null && currentViewModel_.personalPostActiveList[0].held_against_id != ''">
                                        ${currentViewModel_.personalPostActiveList[0].held_against_idModel.code_desc}
                                    </s:if>
                                </td>
                            </tr>
                        </s:if>
                        <s:if test='currentViewModel_.personalPostActiveList[0].held_against_flag.equals("Y")'>
                            <tr>
                                <td></td>
                                <td class="label_imp"><s:text name="postSandang.appointGred"/></td>
                                <td class="label_imp">:</td>
                                <td>
                                    <s:if test="currentViewModel_.personalPostActiveList[0].post_appointed_gred != null && currentViewModel_.personalPostActiveList[0].post_appointed_gred != ''">
                                        ${currentViewModel_.personalPostActiveList[0].salary_gradeModel.sal_grade_code}
                                    </s:if>
                                </td>
                            </tr>
                        </s:if>
                            <tr valign="top">
                            <td></td>
                            <td class="label_imp"><s:text name="postSandang.remark"/></td>
                            <td class="label_imp">:</td>
                            <td>${currentViewModel_.personalPostActiveList[0].remark}</td>
                        </tr>
                        <tr>
                            <td height="10px"><%--some space--%></td>
                        </tr>
                    </table>
                </td>
            </s:if>
            <%--Penempatan Penyandang End--%>
        </tr>
    </table>
</form>
