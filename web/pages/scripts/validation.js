function formatText(text) {
  returnValue = text;  
  for (i = 1; i < arguments.length; i++) {
    pattern = "\\{" + (i-1) + "\\}";
    returnValue = returnValue.replace(new RegExp(pattern), arguments[i]);
  }
  return returnValue;
}

function isEmpty(field, fieldName, errors) {
	if (field.value.trim() == '') {
		if (errors) {
			errors[errors.length] = formatText(messageRequired, fieldName);
		} else {
			alert(formatText(messageRequired, fieldName));
			if (field.focus) field.focus();
			if (field.select) field.select()
		}
		return true;
	} else {
		return false;
	}
}

function mustSelect(formElementSelectedIndex, message){
	if(formElementSelectedIndex <= 0){
		errorMsg = formatText(messageRequired, message);
		alert(errorMsg);
		//formElement.focus();
		return false;
	}
	else
		return true;
}

/*
 * function: validate a decimal or integer field
 * parameter: c - control
 *            min - minimum value, '' means ignore
 *            max - maximum value, '' means ignore
 *            len - length of field
 *            frac - the number of decimal places
 *			  errors - an array of error messages
 * return: true if integer is within range and length; false otherwise
 */
function validateNumber(field, fieldName, min, max, len, frac, errors) {
	if (!field || field.value.trim() == "") return true;

	var errormsg = frac > 0 ? messageInvalidNumber : messageInvalidInteger;
	if (isNaN(field.value)) {
      if (typeof(errors) == "undefined") {
      	field.select();
     	field.focus();
      	alert(formatText(errormsg, fieldName));
      } else {
      	errors[errors.length] = formatText(errormsg, fieldName);
      }
      return false;
    }
	if (frac <= 0 && field.value.indexOf(".") > 0) {
	  errors[errors.length] = formatText(errormsg, fieldName);
	  return false;
	}

    limit = "";
    for (i = 0; i < len-frac; i++) {
      limit = limit + "9";
    }
    if (frac > 0) {
      limit = limit + ".";
      for (i = 0; i < frac; i++) {
        limit = limit + "9";
      }
    }
    if (isNaN(parseFloat(min, 10))) min = parseFloat(limit, 10) * -1;
    if (isNaN(parseFloat(max, 10))) max = parseFloat(limit, 10);
    if ((parseFloat(field.value, 10) < min) && (limit == '' && len == '')) {
      if (typeof(errors) == "undefined") {
	    field.select();
	    field.focus();
	    alert(formatText(messageGreaterEqThan, fieldName, toNumeric(min.toString(),frac)));
	  } else {
		errors[errors.length] = formatText(messageGreaterEqThan, fieldName, toNumeric(min.toString(),frac));
	  }
	  return false;
    }
    else if ((parseFloat(field.value, 10) < min) || (parseFloat(field.value, 10) > max)) {
      if (typeof(errors) == "undefined") {
	    field.select();
	    field.focus();
	    alert(formatText(messageInvalidRange, fieldName, toNumeric(min.toString(),frac), toNumeric(max.toString(),frac)));
	  } else {
		errors[errors.length] = formatText(messageInvalidRange, fieldName, toNumeric(min.toString(),frac), toNumeric(max.toString(),frac));
	  }
	  return false;
    }
	if (field.value.indexOf(".") > 0 && (field.value.length-field.value.indexOf(".")-1 > frac)) {
	    if (typeof(errors) == "undefined") {
		  field.select();
		  field.focus();
		  alert(formatText(messageInvalid, fieldName, toNumeric(min.toString(),frac), toNumeric(max.toString(),frac)));
		} else {
		  errors[errors.length] = formatText(messageInvalid, fieldName);
		}
		return false;
    }

    if (typeof(errors) == "undefined") {
    	//field.value = toNumeric(field.value, frac);
    }
//}
  return true;
}

function toNumeric(val, frac) {
  var decimalPoint = val.indexOf('.');
  if (decimalPoint == -1) {
    intPart = val;
    decimalPart = "";
  }
  else {
    intPart = val.substr(0, decimalPoint);
    decimalPart = val.substr(decimalPoint+1);
  }
  if (frac == 0) {
  	return intPart;
  }
  // Substract the decimal part based on the input fraction
  if (decimalPart.length > frac) {
    decimalPart = decimalPart.substr(0, frac);
  }
  // Place zero as decimal points
  for (var i = decimalPart.length; i < frac; i++) {
    decimalPart = decimalPart + "0";
  }
  return intPart + "." + decimalPart;
}

function checkSpecialChar(field, fieldName) {
	var temp = field.value;
	var re = new RegExp ('\r\n', 'gi') ;
	var newstr = temp.replace(re, '') ;
//	var errorMsg = "Invalid characters found \r\rList of characters allowed:\rAlphabets\rNumbers\r .  -  &  '  ,  /  (  )  ;  :  +  #  ?  @  _"

     for (var i=0; i < newstr.length; i++)
     {
          var ch = newstr.substring(i, i+1);
          if ((ch >= "A" && ch <= "Z") || (ch>= "0" && ch <= "9") || (ch >= "a" && ch <= "z") || (ch == "\n") ||
          	 (ch == " ") || (ch == ".") || (ch == "-") || (ch == "&") || (ch == "'") || (ch == "/") || (ch == "(") ||
          	 (ch == ")") || (ch == ";") || (ch == ":") || (ch == "+") || (ch == "#") || (ch =="?") || (ch == "@") ||
          	 (ch == "_") || (ch == ",")) {
               continue;
          } else {
	          alert(formatText(messageInvalidChar, fieldName));
              field.select();
              field.focus ();
              return false;
          }
     }
     return true;
}

// this function is to check for a valid email address.
function emailCheck (field, label, errors) {	
	var emailStr = field.value.trim();
	if (emailStr.length > 0) {
		if (/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(emailStr)){
			return true;
		} else {
			errors[errors.length] = formatText(messageInvalid, label);
                        if (fieldToFocus === null) {
                            fieldToFocus = field ;
                        }
			return false;
		}
	}
	return true;
}


function passwordCheck (field, label, errors,  minlen, maxlen) {
	var passwordStr = field.value.trim();
//    1) Password Length
    if (passwordStr.length < minlen) { // 12 characters
        errors[errors.length] = formatText(messageMinLength, label, minlen);
        return false;
    } else if (passwordStr.length > maxlen) { // 16 characters
        errors[errors.length] = formatText(messageMaxLength, label, maxlen);
        return false;
    }
    
    if (!chkInvalidChar2(passwordStr)) {
//        errors[errors.length] = label + " tidak boleh mengandungi aksara tidak sah.";
        errors[errors.length] = passwordInvalidCharacter;
        return false;
    }

    if (!chkAlphaNumeric(passwordStr, "Y")) {
//        errors[errors.length] = label + " mestilah dalam format abjad angka.";
        errors[errors.length] = passwordAlphaNumeric;
        return false;
    }

	return true;
}



//function passwordCheck (field, label, errors) {
//	var passwordStr = field.value.trim();
//
////    1) Password Length
//    if (passwordStr.length < 12) { // 12 characters
//		errors[errors.length] = formatText(messageMinLength, label, 12);
//		return false;
//	}
////     // Alphanumeric 12 characters
////        var checkOK = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
////        var checkStr = passwordStr;
////        var allValid = true;
////        for (i = 0;  i < checkStr.length;  i++)
////        {
////        ch = checkStr.charAt(i);
////        for (j = 0;  j < checkOK.length;  j++)
////        if (ch == checkOK.charAt(j))
////        break;
////        if (j == checkOK.length)
////        {
////        allValid = false;
////        break;
////        }
////        }
////        if (!allValid)
////        {
////          errors[errors.length] = formatText(messageAplhanumeric, label);
////          return false;
////        }
//
//
//	return true;
//}

/*
 * function: validate key stroke - allow digit only
 * return: true if the key stroke is digit; false otherwise
 */
function integerKey() {
  var event = window.event;
  key = String.fromCharCode(event.keyCode);  
  if (isNaN(key)) {
    return false;
  }
  return true;
}

function isDecimal (s) {
	var isDecimal_re = /^\s*(\+|-)?((\d+(\.\d+)?)|(\.\d+))\s*$/;
	return String(s).search(isDecimal_re) != -1;
}

function isCurrency (s) {
	var isCurrency_re = /^\s*(\+|-)?((\d+(\.\d\d)?)|(\.\d\d))\s*$/;
	alert("Testing currency: " + isCurrency_re);
	return String(s).search(isCurrency_re) != -1;
}

/*************************
 *  Function to restrict the user from keying in 
 *  non-valid characters for currency. 
 *  This function works for IE only because of the use of the 'window' 
 *  object (e.g. window.event)
 *************************/
function currencyKey(obj, allowNegativeValue)
{
	var event = window.event;
  	key = String.fromCharCode(event.keyCode);

  	var negativePattern = /-/;
  	if(!allowNegativeValue)
  		negativePattern = /[^-]/;

	var replacePattern = /[^0-9.^-]/g;
  	if(!allowNegativeValue)
  		replacePattern = /[^0-9.]/g;

	if (!isNaN(key) || key.match(/\./) != null || key.match(negativePattern) != null) {
	    obj.value = obj.value.replace(replacePattern, "");
		var idx = obj.value.indexOf("-");	
	    if(idx == 0) {
			if (key == "-") {
	        	return false;
	      	}	    
	    } 

    	idx = obj.value.indexOf(".");
    	if (idx >= 0) {
      		if (key == ".") {
        		return false;
      		} 
      		if (((obj.value.length - idx) > 2)) {
        		obj.value = obj.value.substring(0, idx + 3);
        		return false;
      		}
    	}
    	
    	return true;
	}
	  
	return false;
}

/*
 * Validate key stroke - allow digit or dot char only
 * @handleEvent onkeypress, onblur
 * @param obj The form object
 * @param frac The fraction to control; by default is 2 if not specified; 0 to escape the control
 * @return true if the key stroke is digit and dot char; false otherwise
 */
function decimalKey(obj, frac) {
  if (!frac) frac = 2;
  var event = window.event;
  key = String.fromCharCode(event.keyCode);

  if (!isNaN(key) || key.match(/\./) != null) {
    obj.value = obj.value.replace(/[^0-9.]/g, "");

    idx = obj.value.indexOf(".");
    if (idx >= 0) {
      if (key == ".") {
        return false;
      } 
      if (frac > 0 && ((obj.value.length - idx) > frac)) {
        obj.value = obj.value.substring(0, idx + 3);
        return false;
      }
    }
    return true;
  }
  else {
    return false;
  }
}

/*
 * function: allow alphabet and numeric key code
 */
function alphaNumericKey() {
  var event = window.event;
  key = event.keyCode;
  
  if (!isAlphaKey(key) && !isNumericKey(key) || key == 32) {
    return false;
  }
  return true;
}

/*
 * function: allow alphabet, numeric, space key code
 */
function alphaNumericSpaceKey() {
  var event = window.event;
  key = event.keyCode;
  
  if (!isAlphaKey(key) && !isNumericKey(key)) {
    return false;
  }
  return true;
}

/*
 * function: allow only alphabet, space key code
 */
function alphaKey() {
  var event = window.event;
  key = event.keyCode;
  
  if (!isAlphaKey(key)) {
    return false;
  }
  return true;
}

/*
 * function: validate if key code is alphabets and space
 */
function isAlphaKey(arg) {
   return (arg > 64 && arg < 91) || (arg > 96 && arg < 123) || arg == 32;
}

/*
 * function: validate if a character is digit
 */
function isNumericKey(arg) {
  return (arg >= "0".charCodeAt(0)) && (arg <= "9".charCodeAt(0));
}

/*
 * function: remove the preceeding and trailling space
 */
String.prototype.trim = function() {
  return( this.replace(/^\s*([\s\S]*\S+)\s*$|^\s*$/,'$1') );
}


/**
 * Validate mandatory fields.
 * Usage:
 *   function required() { 
 *     this.aa = new Array("<field name>", "<lable name>", <overwrite message: true/false/1/0>);
 *     this.ab = new Array("<field name>", "<lable name>");
 *   }
 */
var fieldToFocus = null;
function validateRequired(form, errors, focus) {
  fieldToFocus = null;
  var isValid = true;
  var focusField = null;
  //var i = 0;
  var i = typeof(errors) != "undefined" ? errors.length : 0;
  var fields = new Array();
  var oRequired = new required();
  if (!focus) focus = new Array();
  
  for (x in oRequired) {
    var field = form[oRequired[x][0]];
    //if (typeof(field) == "undefined") continue;
    if (typeof(field) == "undefined"){
        field = document.getElementById(oRequired[x][0]);
        if (typeof(field) == "undefined") continue;
    }
    var label = oRequired[x][1];
    var ownMsg = (typeof(oRequired[x][2]) == "undefined" || oRequired[x][2] == false || oRequired[x][2] == 0) ? false : true;

    if (validateFieldRequired(field) == false) {
	  isValid = false;
      if (typeof(errors) == "undefined" || errors.length == 0) {
		focusField = (typeof(field.type) != "undefined") ? field : field[0];
	  }
	  if (typeof(errors) == "undefined") {
	  	fields[i++] = (ownMsg)? label : formatText(messageRequired, label);
                if (fieldToFocus === null) {
                    fieldToFocus = field;
                }
	  	break;
	  } else {
	  	errors[i++] = (ownMsg)? label : formatText(messageRequired, label);
                if (fieldToFocus === null) {
                    fieldToFocus = field;
                }
	  	if (focus.length == 0 && focusField) {
	  		focus[0] = focusField;
	  	}
	  }
  	}
  }
  $(form).find(".EmailCheck").each(function () {
    if ($(this).attr("lbl") !== "" && $(this).attr("lbl") !== "undefined") {
        emailCheck (this, $(this).attr("lbl"), errors)
    } else {
        emailCheck (this, "Email", errors)
    }
  });
  if (typeof(errors) == "undefined") {
	if (fields.length > 0) {
	  focusField.focus();
	  alert(fields.join('\n'));
    }
  }
  if (focusField) {
      scroll_to_ID($(focusField).prop("id"));
  }
  return isValid;
  //return errors;
}

function validateLookupRequired(form, errors, focus) {
  var isValid = true;
  var focusField = null;
  //var i = 0;
  var i = typeof(errors) !== "undefined" ? errors.length : 0;
  var fields = new Array();
  var oRequired = new lookupRequired();
  if (!focus) focus = new Array();

  for (x in oRequired) {
    var field = form[oRequired[x][0]];
    if (typeof(field) === "undefined") continue;
    var label = oRequired[x][1];
    var ownMsg = (typeof(oRequired[x][2]) === "undefined" || oRequired[x][2] === false || oRequired[x][2] === 0) ? false : true;

    if (validateFieldRequired(field) === false) {
	  isValid = false;
      if (typeof(errors) === "undefined" || errors.length === 0) {
		focusField = (typeof(field.type) !== "undefined") ? field : field[0];
	  }
	  if (typeof(errors) === "undefined") {
	  	fields[i++] = (ownMsg)? label : formatText(messageRequired, label);
	  	break;
	  } else {
	  	errors[i++] = (ownMsg)? label  : formatText(messageRequired, label);
	  	if (focus.length === 0 && focusField) {
	  		focus[0] = focusField;
	  	}
	  }
  	}
  }
  if (typeof(errors) !== "undefined") {
	if (errors.length > 0) {
            if (focus != "nofocus"){
                focusField.focus();
            }
    }
  }
  return isValid;
  //return errors;
}

/**
 * Validate whether the given field is empty.
 * @return true(not empty)/false(empty)
 */
function validateFieldRequired(field) {
  var isValid = true;
  if (field.type == 'text' ||
      field.type == 'textarea' ||
      field.type == 'file' ||
      field.type == 'select-one' ||
      field.type == 'radio' ||
      field.type == 'checkbox' ||
      field.type == 'password') {

    var value = '';
    // get field's value
    if (field.type == "select-one") {
      var si = field.selectedIndex;
      if (si >= 0) {
        value = field.options[si].value;
      }
    } else if (field.type == "checkbox" || field.type == 'radio') {
      if (field.checked) value = field.value;
    } else {
      value = field.value;
    }
    
    if (trim(value).length == 0) {
      isValid = false;
    }
  }
  else if (typeof(field.type) == "undefined") {
    if (!(isNaN(field.length))) {
      checked = false;
      for(j=0; j<field.length; j++) {
        checked = validateFieldRequired(field[j]);
        if (checked) break;
      }
      if (checked == false) {
        isValid = false;
      }
    }
  }
  return isValid;
}

/**
 * Validate at least 1 of the given fields is required.
 * Usage:
 *   function minRequired() { 
 *     this.aa = new Array("<Field Name>", "<Own Message>");
 *     this.ab = new Array("<Field Name>");
 *   }
 */ 
function validateMinRequired(form, errors) {
  var isValid = false;
  //var i = 0;
  var i = typeof(errors) != "undefined" ? errors.length : 0;
  var ownMsg;
  var oMinRequired = new minRequired();
  for(var x in oMinRequired) {
    var field = form[oMinRequired[x][0]];
    if (typeof(oMinRequired[x][1]) != "undefined") {
      ownMsg = oMinRequired[x][1];
    }

    if (validateFieldRequired(field) == true) {
      isValid = true;
      break;
    }
  }
  if (isValid == false) {
  	if (typeof(errors) == "undefined") {
    	alert((ownMsg != null && ownMsg.length > 0) ? ownMsg : messageAtLeastOne);
    } else {
    	errors[i++] = (ownMsg != null && ownMsg.length > 0) ? ownMsg : messageAtLeastOne;
    }
  }
  return isValid;
}

/**
 * Validate field requirement depeneds on another field.
 * Usage:
 *   function requiredIf() { 
 *     this.aa = new Array("<field name>", "<field value>", "<operator>", "<required name>", "<required label>", <overwrite message: true/false/1/0>);
 *     this.ab = new Array("<field1 name>", "<field1 value>", "==", "<required4 name>", "<required4 label>");
 *     this.ac = new Array("<field2 name>", "<field2 value>", "!=", "<required5 name>", "<required5 label>", true);
 *   } 
 */
function validateRequiredIf(form, errors) {
  var isValid = true;
  var focusField = null;
  var i = typeof(errors) != "undefined" ? errors.length : 0;
  var fields = new Array();
  var oRequiredIf = new requiredIf();
  for(var x in oRequiredIf) {
    field    = form[oRequiredIf[x][0]];
    value    = oRequiredIf[x][1];
    operator = oRequiredIf[x][2];
    fieldr   = form[oRequiredIf[x][3]];
    label    = oRequiredIf[x][4];
    ownMsg   = (typeof(oRequiredIf[x][5]) == "undefined" || oRequiredIf[x][5] == false || oRequiredIf[x][5] == 0) ? false : true;

	if (!field) continue;
    if (field.type == 'select-one') {
      var fieldValue = field.value;

      if(fieldValue == '') //added to handle the case where field value is empty
      {
	  	fieldValue = "''";       
      }
      
	  var evalStatement = fieldValue + operator + value;
	  var evalResult = eval(evalStatement);
      
      if (evalResult) {      
        if (validateFieldRequired(fieldr) == false) {
          if (i == 0) {
            focusField = (typeof(fieldr.type) != "undefined") ? fieldr : fieldr[0];
          }
          if (typeof(errors) == "undefined") {
			fields[i++] = (ownMsg)? label : formatText(messageRequiredIf, label);
          } else {
			errors[i++] = (ownMsg)? label : formatText(messageRequiredIf, label);
          }
          isValid = false;
          //break; // added to show 1 message at a time.
        }                	
      }
    }
    else if (typeof(field.type) == "undefined") {
     
      if (!(isNaN(field.length))) {
        for(j=0; j<field.length; j++) {
          if (field[j].checked && field[j].value == value) {
            if (validateFieldRequired(fieldr) == false) {
              if (i == 0) {
                focusField = (typeof(fieldr.type) != "undefined") ? fieldr : fieldr[0];
              }
	          if (typeof(errors) == "undefined") {
				fields[i++] = (ownMsg)? label : formatText(messageRequiredIf, label);
	          } else {
				errors[i++] = (ownMsg)? label : formatText(messageRequiredIf, label);
	          }
              isValid = false;
            }
            //break;                	
          }
        }
      }
    }
    else if (field.type == 'text' || field.type == 'textarea' || field.type == 'password') {
      //FIX for the string entry
      if (field.value.length > 0 && eval("'" + field.value + "'" + operator + value)) {
        if (validateFieldRequired(fieldr) == false) {
          if (i == 0) {
            focusField = (typeof(fieldr.type) != "undefined") ? fieldr : fieldr[0];
          }
	      if (typeof(errors) == "undefined") {
			fields[i++] = (ownMsg)? label : formatText(messageRequiredIf, label);
	      } else {
			errors[i++] = (ownMsg)? label : formatText(messageRequiredIf, label);
	      }
          isValid = false;
          //break; // added to show 1 message at a time.
        }
      }
    }
  }
  if (typeof(errors) == "undefined") {
  	if (fields.length > 0) {
	  focusField.focus();
	  alert(fields.join('\n'));
	}
  }
  return isValid;
}

// Trim whitespace from left and right sides of s.
function trim(s) {
  return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
}

/**
 * Validate the pattern of the field
 * Usage:
 *   function mask() { 
 *     this.aa = new Array("<field name>", "<field label>", "<pattern>", "<message>", "<additional label>");
 *     this.ab = new Array("<field1 name>", "<field1 value>", "<pattern1>", "<message>");
 *     this.ac = new Array("<field2 name>", "<field2 value>", "<pattern2>", "<message>");
 *   } 
 */
function validateMask(form, errors) {
	var isValid = true
	var focusField = null
	//var i = 0;
	var i = typeof(errors) != "undefined" ? errors.length : 0
	var fields = new Array()
	var oMasked = new mask()
	for (x in oMasked) {
		field = form[oMasked[x][0]]
		label = oMasked[x][1]
		pattern = oMasked[x][2]
		msg = oMasked[x][3]
		label2 = oMasked[x][4]
		if (!(typeof(field) == "undefined" || field.type == "undefined") && 
			((field.type == 'text' || 
			 field.type == 'textarea' ||
			 field.type == 'password') && 
 			(field.value.length > 0))) {
                        
			if (!field.value.match(pattern)) {
				if (typeof(errors) == "undefined") {
					if (i == 0) {
						focusField = (typeof(field) != "undefined" || field.type != "undefined") ? field : field[0]					
					}
					if (label2 == "undefined") {
						fields[i++] = formatText(msg, label)
					} else fields[i++] = formatText(msg, label, label2)
					
				} else {
					if (label2 == "undefined") {
						errors[i++] = formatText(msg, label)
					} else errors[i++] = formatText(msg, label, label2)
				}
				isValid = false
				//break
			}
		}
	}    
	if (typeof(errors) == "undefined") {
		if (fields.length > 0) {
			focusField.focus()
			focusField.select()
			alert(fields.join('\n'))
		}
	}
	return isValid
}

/*
 * function: validate a field length
 * parameter: minlen - minimum length, '' means ignore
 *            maxlen - maximum length, '' means ignore
 *            //args0     - custom error message [optional]
 *			  errors
 * return: true if input is within length range; false otherwise
 */
function validateLength(field, fieldName, minlen, maxlen, errors) {
	if (!field || 
		!(field.type == "text" 
			|| field.type == "textarea")) return true;
	var value = field.value.trim();
	if (value == '') return true;

   	//errMsg  = (args0 != null) ? args0 : messageMinLength;
   	var errormsg = ''
	var len = ''
	if (minlen == '' && maxlen == '') {
		return true
	} else {
		maxlen = maxlen == '' ? value.length : maxlen
		minlen = minlen == '' ? value.length : minlen
		if (value.length < minlen) {
			errormsg = messageMinLength
			len = minlen
		} else if (value.length > maxlen) {
			errormsg = messageMaxLength
			len = maxlen
		}
		if (errormsg != '') {
			if (typeof(errors) == "undefined") {
				field.focus()
				field.select()
				alert(formatText(errormsg, fieldName, maxlen))
			} else {
				errors[errors.length] = formatText(errormsg, fieldName, len)
			}
			return false
		} else {
			return true
		}
		
	}
}

/*
 * function: validate whether first field is greater than second field
 * only applied to Integer and Float
 * Usage:
 *   function greaterThan() { 
 *     this.aa = new Array("<field name>", "<field label>");
 *     this.ab = new Array("<field1 name>", "<field1 label>");
 *   } 
 * return: true if input 1 greater than input 2; false otherwise
 */
function validateGreater(form, errors) {
	var isValid = false;
	var focusField = null;
	var i = 0;
	var fields = new Array();
	var fieldVals = new Array();
	oGreaterThan = new greaterThan();
	for (var x in oGreaterThan) {
		var field = form[oGreaterThan[x][0]];
		if ((field.type == 'text' ||
			field.type == 'textarea') &&
			(field.value.length > 0)) {
			fieldVals[i] = field;
			fields[i] = oGreaterThan[x][1];
			i++;
		}
		if (i == 2) break;
	}
	if (fieldVals.length == 2) {
		var field1 = fieldVals[0];
		var field2 = fieldVals[1];
		if (parseFloat(field1.value) > parseFloat(field2.value)) {
			isValid = true;
		}
	}
	if (fieldVals.length == 2 && !isValid) {
		if (typeof(errors) == "undefined") {
			fieldVals[0].focus();
			fieldVals[0].select();
			alert(formatText(messageGreaterThan, fields[0], fields[1]))
		} else {
			errors[errors.length] = formatText(messageGreaterThan, fields[0], fields[1]);
		} 
	}
	return isValid;
}

/*
 * function: validate whether first field equals second field
 * return: true if input 1 equals input 2; false otherwise
 */
function validateEquals(field1, fieldName1, field2, fieldName2, errors) {
	var arg1 = field1.value.trim();
	var arg2 = field2.value.trim();
	if (arg1 == '' && arg2 == '') {
		return false
	}
 	if (arg1 != "" && arg2 != "" && arg1 != arg2) {
		if (typeof(errors) == "undefined") {
	 		field1.focus();
	 		field1.select();
	 		alert(formatText(messageMustBeSame, fieldName1, fieldName2))
	 	}
	 	else {
	 		errors[errors.length] = formatText(messageMustBeSame, fieldName1, fieldName2);
	 	}
	 	return false
 	} 
 	return true
}

/*
 * Function : Compare two date strings to see which is greater.
 * return false if date is invalid or fromDate is greater than toDate
 */
function compareDate(fromDay, 
					 fromMonth, 
					 fromYear, 
					 toDay, 
					 toMonth, 
					 toYear,
					 fromDateLabel,
					 toDateLabel,
					 errors,
					 operator) {
	var now=new Date();
	var hh=now.getHours();
	var mm=now.getMinutes();
	var ss=now.getSeconds();

	if (operator == null || typeof(operator) == "undefined" || operator == '') {
		operator = '>'
	}

	var newDate1=new Date(fromYear,fromMonth-1,fromDay,hh,mm,ss);
	var newDate2=new Date(toYear,toMonth-1,toDay,hh,mm,ss);
	var d1=newDate1.getTime();
	var d2=newDate2.getTime();

	if (d1==0) {
		errors[errors.length] = formatText(messageInvalid, fromDateLabel);
		return false;
	} else if (d2==0) {
		errors[errors.length] = formatText(messageInvalid, toDateLabel);
		return false;
	} else if (eval(d1 + operator + d2)) {
//		var msg = messageEarlierThan;   //commented by Delvene @ 14-Jun-2013 :: Operator ">" should mean d1 later than d2.
		var msg = messageNotLaterThan;
		if (operator == '==') {
		} else if (operator == '!=') {

		} else if (operator == '<') {
//			msg = messageNotLaterThan   //commented by Delvene @ 14-Jun-2013 :: Operator "<" should mean d1 earlier than d2.
			msg = messageEarlierThan
		}
		errors[errors.length] = formatText(msg, fromDateLabel, toDateLabel);
		return false;
	}
	return true;
}

/**
 * Function : Convert the date to dd, mm(0-11), and yyyy format and pass to compareDate for checking
   In JavaScript, date is in mm/dd/yyyy format  
 * return false if date is invalid or fromDate is greater than toDate
 */
function convertAndCompareDate(fromDate, toDate, errors) {
	var fromDay = fromDate.substring(0, 2)
	var fromMonth = (fromDate.substring(3, 5) - 1)
	var fromYear = fromDate.substring(6)
	var toDay = toDate.substring(0, 2)
	var toMonth = (toDate.substring(3, 5) - 1)
	var toYear = toDate.substring(6)
	return compareDate(fromDay, fromMonth, fromYear, toDay, toMonth, toYear, "Date To", "Date From", errors);
}

/**
 * Added By Delvene @ 14-Jun-2013 :: To include fromDateLabel, toDateLabel and operator
 * Function : Convert the date to dd, mm(0-11), and yyyy format and pass to compareDate for checking
   In JavaScript, date is in mm/dd/yyyy format
 * return false if date is invalid or comparison fails according to operator
 */
function convertAndCompareDate2(fromDate, toDate, fromDateLabel, toDateLabel, errors, operator) {
        var fromDay = fromDate.substring(0, 2)
	var fromMonth = (fromDate.substring(3, 5) - 1)
	var fromYear = fromDate.substring(6)
	var toDay = toDate.substring(0, 2)
	var toMonth = (toDate.substring(3, 5) - 1)
	var toYear = toDate.substring(6)
	return compareDate(fromDay, fromMonth, fromYear, toDay, toMonth, toYear, fromDateLabel, toDateLabel, errors, operator);
}

function convertAndCompareMonthYear(fromDate, toDate, errors) {
	var fromDay = "01";
	var fromMonth = (fromDate.substring(0, 2) - 1)
	var fromYear = fromDate.substring(3)
	var toDay = "01";
	var toMonth = (toDate.substring(0, 2) - 1)
	var toYear = toDate.substring(3)
	
	return compareDate(fromDay, fromMonth, fromYear, toDay, toMonth, toYear, "Date To", "Date From", errors);
}

function isCheckboxSelected_byClass(className, label, errors) {
    return isCheckboxSelected(document.getElementsByClassName(className), label, errors);
}
function isCheckboxSelected(obj, label, errors) {
   
	if (typeof(obj) == "undefined") {
		return false;
	}
	var errormsg;
	if (label == null) {
		errormsg = messageAtLeastOneItem;
	} else {
		errormsg = messageAtLeastOne;
	}
	var isValid = false;
	if (typeof(obj.type) == "string") {
		if (!obj.disabled && obj.checked) {
			isValid = true;
		}
	} else {
		for (var i=0; i<obj.length; i++) {
			if (!obj[i].disabled && obj[i].checked) {
				isValid = true;
				break;
			}
		}
	}
         console.log('************1');
         console.log(errormsg);
	if (!isValid) {
		if (typeof(errors) == "undefined") {
                        $("#alertDiv").find('.title').html('<h3 class="title-alert">Alert</h3>');
                        $("#alertDiv").find('.myModalContent').html(formatText(errormsg, label));
                        $('#alertDiv').modal('show');
//			alert(formatText(errormsg, label));
console.log(errormsg + "a");
		} else {
			errors[errors.length] = formatText(errormsg, label);
		}
	}
        console.log(errors+'************')
	return isValid;
}

function isInteger(s) {
	var i;
    for (i = 0; i < s.length; i++) {   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag) {
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++)  {   
        // Check that current character isn't whitespace.
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function validatePhone(field, label, errors){
	// non-digit characters which are allowed in phone numbers
//	var phoneNumberDelimiters = "-";
	var phoneNumberDelimiters = "-+";   //Edited by Delvene @ 14-Jun-2013 :: Allow '+' symbol as well. E.g. +6512345678
	// Minimum no of digits in an international phone no.
	var minDigitsInIPhoneNumber = 8;

	if (field.value.trim() == "") return true;
	s=stripCharsInBag(field.value,phoneNumberDelimiters);
	if(!(isInteger(s) && s.length >= minDigitsInIPhoneNumber)){
		errors[errors.length] = formatText(messageInvalid, label);
	}
	return false;
	
}

function isValidImageFileFormat(fileName, label, errors) {
	if (!fileName) return true;
	var	fileTypes = new Array ('.jpg','.jpeg');
	
	var dots = fileName.split(".")
	//get the part AFTER the LAST period.
	var fileType = "." + dots[dots.length-1];
	
	if (fileTypes.join(".").indexOf(fileType.toLowerCase()) != -1){	
		return true;
	}
	else {
		if (typeof(errors) == "undefined") {
			alert(formatText(messageInvalidImage, label));
		} else {
			errors[errors.length] = formatText(messageInvalidImage, label);
		}
		return false;
	}	
}

/*
 * Limit the obj (textarea) to a maximum length
 * Usage: onkeypress="return checkMaxlength(textarea, 100)"
 * IE compatible only
 */
function checkMaxlength(obj, maxlen, event) {
	if (!event) event = window.event;
	// FIXME: for mozilla, enter key's length is 1
	//return (obj.value.length+((event.keyCode == 13) ? 2 : 1)) <= maxlen;
	if (obj.value.length >= (maxlen-1)) {
		obj.value = obj.value.substring(0,maxlen-1);
		return false;
	}
	return true;
}


function checkResubmit(chkpoint) {
	if (chkpoint != "") {
		alert(messageAlreadySubmit);
		return false;
	}
	return true;
}

function validateForm(form, operation) {
	var errors = new Array();
	
	validateRequired(form, errors);		
	
	if (errors.length > 0) {
		alert(errors.join('\n'));
		setFocus(form);
	}
	return errors.length > 0 ? false : true;
}

function showErrors(form) {
    var errors = new Array();
    if (fieldError && emailErrors) {
        emailErrors[emailErrors.length] = redRequired;
        errors = emailErrors;
    } else {
        emailErrors[errors.length] = redRequired;
    }
    if (errors.length > 0) {
//        alert(errors.join('\n'));
        bootbox.alert({
            closeButton: false,
            message: errors.join('\n'),
        });
        if (fieldToFocus != null) {
            fieldToFocus.focus();
            scroll_to_ID($(fieldToFocus).attr("id"));
        }
        setFocus(form);
//        alert("in show errors");
    }
}
    
function dateCheck(field, lbl) {
    var fromDate = field.value.trim();
    var fromDay = fromDate.substring(0, 2);
    var fromMonth = (fromDate.substring(3, 5) - 1);
    var fromYear = fromDate.substring(6);
    alert("fromDay = " + fromDay);
    alert("fromMonth = " + fromMonth);
    alert("fromYear = " + fromYear);
}

function emailCheck_jq (field, lbl) {	
    var emailStr = field.value.trim();
    if (emailStr.length > 0) {
        if (/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(emailStr)){
                return true;
        } else {
            if (fieldToFocus === null) {
                fieldToFocus = field ;
            }
            if (lbl === "" || lbl === undefined) {
                lbl = email_lbl;
            }
            emailErrors[emailErrors.length] = formatText(messageInvalidEmail, lbl);
            return false;
        }
    }
    return true;
}

function submitForm_bshor(formId, formAction) {
    if (validateForm_bshor(formId) && emailErrors.length <= 0) {
        if (formAction) {
            $("form#"+formId).attr("action", formAction);
        }
        $("form#"+formId).submit();
    } else {
        showErrors(document.getElementById(formId));
        return false;
    }
}
var emailErrors = null;
var fieldError;
function validateForm_bshor(formId) {
    emailErrors = new Array();
    fieldError = null;
    var noError = true;
    var radioName = new Array();
    $("form#"+formId+" :input").each(function(){
        var input = $(this);
        var thePN = $(this.parentNode);
        if ($(this).hasClass("EmailCheck")) {
            emailCheck_jq (this, $(this).attr("lbl"));
        }
        if (!$(input).prop('disabled') && !$(input).prop('readonly') && $(input).attr("required") === "required") {
            if(!$(input).is("select")) {
                if ($(input).attr("type") === "checkbox") {
                    ($(input).closest("div").find("label")).first().removeClass("redRequired");
                    if (!$(input).prop('checked') ) {
                        noError = false; fieldError=true;
                        ($(input).closest("div").find("label")).first().addClass("redRequired");
                    }
                } else if ($(input).attr("type") === "radio") {
                        if ($(input).closest("div").find("label").length > 0) {
                            ($(input).closest("div").find("label")).removeClass("redRequired");
                            if ($("input[name='"+$(input).attr("name")+"']:checked").val() === undefined ) {
                                noError = false; fieldError=true;
                                ($(input).closest("div").find("label")).addClass("redRequired");
                                radioOnChange_clearBox(input);
                            }
                        } else {
                            $(input).removeClass("redBorder");
                            if ($("input[name='"+$(input).attr("name")+"']:checked").val() === "") {
                                noError = false; fieldError=true;
                                $(input).addClass("redBorder");
                                radioOnChange_clearBox(input);
                            }
                        }
                } else {
                    if ($(input).closest("div").hasClass("input-group")) {
                        $(input).closest("div").removeClass("redBorder");
                        if ($(input).val() === "") {
                            noError = false; fieldError=true;
                            $(input).closest("div").addClass("redBorder");
                            inputOnChange_clearBox(input);
                        }
                    } else {
                        $(input).removeClass("redBorder");
                        if ($(input).val() === "") {
                            noError = false; fieldError=true;
                            $(input).addClass("redBorder");
                            inputOnChange_clearBox(input);
                        }
                    }
                }
            } else { //select2?
                var theSelect2 = $(thePN).find('.select2-selection--single');
                if ($(theSelect2).hasClass("select2-selection--single")) {
                    $(theSelect2).removeClass("redBorder");
                    if ($(input).val() === "") {
                        noError = false; fieldError=true;
                        $(thePN).find('.select2-selection--single').addClass("redBorder");
                        selectOnChange_clearBox(input);
                    }
                }
            }
        }
    });
    return noError;
}

function inputOnChange_clearBox(field) {
    $(field).change(function() {
        if ($(this).closest("div").hasClass("input-group")) {
            if ($(this).val() !== "") {
                $(this).closest("div").removeClass("redBorder");
            }
        } else {
            if ($(this).val() !== "") {
                $(this).removeClass("redBorder")
            }
        }
    });
}
function radioOnChange_clearBox(field) {
    $(field).change(function() {
        var radioName = new Array();
        if (radioName.indexOf($(this).attr("name")) < 0) {
            radioName[radioName.length] = $(this).attr("name");
            if ($(this).closest("div").find("label").length > 0) {
                if ($("input[name='"+$(this).attr("name")+"']:checked").val() !== undefined ) {
                    ($(this).closest("div").find("label")).removeClass("redRequired");
                }
            } else {
                if ($("input[name='"+$(this).attr("name")+"']:checked").val() !== "") {
                    $(this).removeClass("redBorder");
                }
            }
        }
    });
}
function selectOnChange_clearBox(field) {
    $(field).change(function() {
        if ($(this).val() !== "") {
            var thePN = $(this.parentNode);
            var theSelect2 = $(thePN).find('.select2-selection--single');
            if ($(theSelect2).hasClass("select2-selection--single")) {
                $(theSelect2).removeClass("redBorder");
            }
        }
    });
}

function fromDateSetting(field) {
    if ($(field).attr("id").endsWith("From") || $(field).attr("id").endsWith("Start")) {
        var fromLength, toOrEnd;
        if ($(field).attr("id").endsWith("From")) {
            fromLength = 4;
            toOrEnd = "To";
        } else {
            fromLength = 5;
            toOrEnd = "End";
        }
        var to = $(field).attr("id").substring(0, $(field).attr("id").length - fromLength) + toOrEnd;
        if ($("#" + to) !== undefined) {
            var startDate;
            var date;
            if ($(field).val().trim() !== "") {
                startDate = $(field).val();
                startDate = startDate.substring(3, 5) + "/" + startDate.substring(0, 2) + "/" + startDate.substring(6);
                date = new Date(startDate);
            } else {
                if ($(field).attr("limit")!== undefined) {
                    startDate = $(field).attr("limit");
                    startDate = startDate.substring(3, 5) + "/" + startDate.substring(0, 2) + "/" + startDate.substring(6);
                    date = new Date(startDate);
                }
            }
            if ($(field).attr("limit")!== undefined) {
                var limitDate = $(field).val();
                limitDate = limitDate.substring(3, 5) + "/" + limitDate.substring(0, 2) + "/" + limitDate.substring(6);
                var dLimitDate = new Date(limitDate);
                if (!$("#" + to).hasClass("SameDate")) {
                    date.setDate(date.getDate() + 1);
                }
                if (dLimitDate > date) {
                    $('#' + to).datepicker('setStartDate', dLimitDate);
                } else {
                    $('#' + to).datepicker('setStartDate', date);
                }
            } else {
                if (date !== undefined) {
                    if (!$("#" + to).hasClass("SameDate")) {
                        date.setDate(date.getDate() + 1);
                    }
                    $('#' + to).datepicker('setStartDate', date);
                } else {
                    $('#' + to).datepicker('setStartDate', '');
                }
            }
        }
    }
}

function toDateSetting(field) {
    var toLength, fromOrStart;
    if ($(field).attr("id").endsWith("To")) {
        toLength = 2;
        fromOrStart = "From";
    } else {
        toLength = 3;
        fromOrStart = "Start";
    }
    var from = $(field).attr("id").substring(0, $(field).attr("id").length - toLength) + fromOrStart;
    if ($("#" + from) !== undefined) {
        var startDate;
        var date;
        if ($(field).val().trim() !== "") {
            startDate = $(field).val();
            startDate = startDate.substring(3, 5) + "/" + startDate.substring(0, 2) + "/" + startDate.substring(6);
            date = new Date(startDate);
        } else {
            if ($(field).attr("limit")!== undefined) {
                startDate = $(field).attr("limit");
                startDate = startDate.substring(3, 5) + "/" + startDate.substring(0, 2) + "/" + startDate.substring(6);
                date = new Date(startDate);
            }
        }
        if ($(field).attr("limit")!== undefined) {
            var limitDate = $(field).val();
            limitDate = limitDate.substring(3, 5) + "/" + limitDate.substring(0, 2) + "/" + limitDate.substring(6);
            var dLimitDate = new Date(limitDate);
            if (!$("#" + from).hasClass("SameDate")) {
                date.setDate(date.getDate() - 1);
            }
            if (dLimitDate < date) {
                $('#' + from).datepicker('setEndDate', dLimitDate);
            } else {
                $('#' + from).datepicker('setEndDate', date);
            }
        } else {
            if (date !== undefined) {
                if (!$("#" + from).hasClass("SameDate")) {
                    date.setDate(date.getDate() - 1);
                }
                $('#' + from).datepicker('setEndDate', date);
            } else {
                $('#' + from).datepicker('setEndDate', '');
            }
        }
    }
}

function initDatePicker() {
    $('.datepicker').each(function() {        
        var opt = {
            wrap: true,
            dateFormat: newDateRangePicker_defaultFormat,
        }; 
        if ($(this).find('input[data-input]').data('options')) {
            opt = $.extend(opt, $(this).find('input[data-input]').data('options'));
        }
        
        $(this).flatpickr(opt);
    });
}

/* date range split into 2 inputs */
function initDateRange() {
    $('.splitDateRangePicker').each(function() {
        var pickerName = $(this).data('name');
        $('#' + pickerName + 'From').attr('placeholder', 'From');
        $('#' + pickerName + 'To').attr('placeholder', 'To');
        
        var dsrf_opt = {
            wrap: true,
            dateFormat: newDateRangePicker_defaultFormat,
        }; 
        var dsrf_opt2 = {
            wrap: true,
            dateFormat: newDateRangePicker_defaultFormat,
        };
        
        if ($('#' + pickerName + 'From_dsrf').data('options')) {
            dsrf_opt = $.extend(dsrf_opt, $('#' + pickerName + 'From_dsrf input[data-input]').data('options'));
        }
        
        if ($('#' + pickerName + 'To_dsrf input[data-input]').data('options')) {
            dsrf_opt2 = $.extend(dsrf_opt2, $('#' + pickerName + 'To_dsrf input[data-input]').data('options'));
        }
        
        var pickerFrom = $('#' + pickerName + 'From_dsrf').flatpickr(dsrf_opt);
        var pickerTo = $('#' + pickerName + 'To_dsrf').flatpickr(dsrf_opt2);
        
        pickerFrom.set('onChange', function(selectedDates, dateStr, instance) {
            pickerTo.set('minDate', selectedDates[0]);
        });
    });
    
    
//    $('.DateRange').each(function () {
//        if ($(this).attr("id").endsWith("From") || $(this).attr("id").endsWith("Start")) {
//            var ph1, ph2, toOrEnd, fromLength;
//            if ($(this).attr("id").endsWith("From")) {
//                ph1 = commonFrom;
//                ph2 = commonTo;
//                toOrEnd = 'To';
//                fromLength = 4;
//            } else {
//                ph1 = commonStart;
//                ph2 = commonEnd;
//                toOrEnd = 'End';
//                fromLength = 5;
//            }
//            var to = $(this).attr("id").substring(0, $(this).attr("id").length - fromLength) + toOrEnd;
//            if ($(this).attr("placeholder") == undefined || $(this).attr("placeholder") == "") {
//                $(this).attr("placeholder", ph1);
//            }
//            var compareDate = $(this).attr("limit");
////            compareDate = compareDate.substring(3, 5) + "/" + compareDate.substring(0, 2) + "/" + compareDate.substring(6);
//            $(this).datepicker('setStartDate', compareDate);
//            if ($("#" + to) !== undefined) {
//                if ($("#" + to).attr("placeholder") == undefined || $("#" + to).attr("placeholder") == "") {
//                    $("#" + to).attr("placeholder", ph2);
//                }
//                var startDate = $(this).val();
//                startDate = startDate.substring(3, 5) + "/" + startDate.substring(0, 2) + "/" + startDate.substring(6);
//                $('#' + to).datepicker('setStartDate', new Date(startDate));
//                compareDate = $('#' + to).attr("limit");
//                $('#' + to).datepicker('setEndDate', compareDate);
//            }
//            fromDateSetting($(this));
//        } else {
//            toDateSetting($(this));
//        }
//        $(this).datepicker().on('changeDate', function () {
//            if ($(this).attr("id").endsWith("From") || $(this).attr("id").endsWith("Start")) {
//                fromDateSetting($(this));
//            } else {
//                toDateSetting($(this));
//            }
//        });
//    });
}
