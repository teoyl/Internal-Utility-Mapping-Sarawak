<%@ page contentType="application/x-javascript"%>
<%@taglib uri="/struts-tags" prefix="s"%>
var messageInvalidDateFormat= "<s:text name='errors.invalidDateFormat' />";
var messageInvalidInteger= "<s:text name='errors.integer' />";
var messageInvalidNumber= "<s:text name='errors.double' />";
var messageInvalidChar= "<s:text name='errors.char' />";
var messageAtLeastOneItem = "<s:text name="errors.atleastoneitem"/>";
var messageAtLeastOne = "<s:text name="errors.atleastone" />";
var messageRequired = "<s:text name="errors.required" />";
var messageItemized = "<s:text name="errors.itemized" />";
var messageInvalid = "<s:text name="errors.invalid" />";
var messageAplhanumeric = "<s:text name="errors.alphanumeric" />";
var messageMinLength = "<s:text name="errors.minlength" />";
var messageMaxLength = "<s:text name="errors.maxlength" />";
var messageValueNotMatch = "<s:text name="errors.valueNotMatch" />";

var messageEarlierThan = "<s:text name="errors.earlierthan" />";
var messageNotLaterThan = "<s:text name="errors.cannotLaterThan" />";
var messageTotalShare100 = "<s:text name="msen.totalShare100" />";
var messageLessEqualThan = "<s:text name="errors.lessEqualThan" />";
var messageMustBeSame = "<s:text name="errors.mustbesame" />";
var messageChangeToApproveWithCondition = "<s:text name="msen.mjlpbp.errorChangeToApproveWithCondition" />";
var messageNoConditionalApproval = "<s:text name="msen.mjlpbp.errorNoConditionalApproval" />";
var messageNoDecision = "<s:text name="msen.mjlpbp.errorNoDecision" />";
var messageGreaterthan = "<s:text name="errors.greaterthan" />";
var messageGreaterEqThan = "<s:text name="errors.greaterEqualThan" />";
var messageInvalidFileName = "<s:text name="errors.fileNameIsNotAlphanumeric" />";
var messageInvalidRange = "<s:text name="errors.inbetween" />";
var messageRequiredIf = "<s:text name="field.required.if" />";
var messageInvalidImage = "<s:text name="errors.InvalidImageFormat" />";
var messageAlreadySubmit = "<s:text name="messageAlreadySubmit" />";
var error404 = "<s:text name="errors.404" />";
var error404_msg1 = "<s:text name="errors.404.msg1" />";
var error404_msg2 = "<s:text name="errors.404.msg2" />";
var messageRemoveFromGroup = "<s:text name="user.confirmEemoveFromGroup" />";
var passwordInvalidCharacter = "<s:text name="errors.passwordInvalidCharacter" />";
var passwordAlphaNumeric = "<s:text name="errors.passwordAlphaNumeric" />";
var redRequired = "<s:text name="field.redRequired" />";
var messageInvalidEmail = "<s:text name="errors.email" />";
var email_lbl = "<s:text name="user.emailAddress" />";
var commonFrom = "<s:text name="common.from"/>";
var commonStart = "<s:text name="common.start"/>";
var commonTo = "<s:text name="common.to"/>";
var commonEnd = "<s:text name="common.end"/>";
var newDateRangePicker_defaultFormat = "<s:text name="newDateRangePicker_defaultFormat"/>";
var uppyInprogressMsg = "<s:text name="uppyInprogressMsg"/>";

<jsp:include page="/pages/scripts/validation.js" flush="true" />
