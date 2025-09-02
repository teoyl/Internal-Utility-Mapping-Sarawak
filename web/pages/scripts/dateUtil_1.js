// ===================================================================
// Author: Matt Kruse <matt@mattkruse.com>
// WWW: http://www.mattkruse.com/
//
// NOTICE: You may use this code for any purpose, commercial or
// private, without any further permission from the author. You may
// remove this notice from your final code if you wish, however it is
// appreciated by the author if at least my web site address is kept.
//
// You may *NOT* re-distribute this code in any way except through its
// use. That means, you can include it in your product, or your web
// site, or any other form where the code is actually being used. You
// may not put the plain javascript up on your site for download or
// include it in your javascript libraries for download. 
// If you wish to share this code with others, please just point them
// to the URL instead.
// Please DO NOT link directly to my .js files from your site. Copy
// the files to your server and use them there. Thank you.
// ===================================================================

// HISTORY
// ------------------------------------------------------------------
// May 17, 2003: Fixed bug in parseDate() for dates <1970
// March 11, 2003: Added parseDate() function
// March 11, 2003: Added "NNN" formatting option. Doesn't match up
//                 perfectly with SimpleDateFormat formats, but 
//                 backwards-compatability was required.

// ------------------------------------------------------------------
// These functions use the same 'format' strings as the 
// java.text.SimpleDateFormat class, with minor exceptions.
// The format string consists of the following abbreviations:
// 
// Field        | Full Form          | Short Form
// -------------+--------------------+-----------------------
// Year         | yyyy (4 digits)    | yy (2 digits), y (2 or 4 digits)
// Month        | MMM (name or abbr.)| MM (2 digits), M (1 or 2 digits)
//              | NNN (abbr.)        |
// Day of Month | dd (2 digits)      | d (1 or 2 digits)
// Day of Week  | EE (name)          | E (abbr)
// Hour (1-12)  | hh (2 digits)      | h (1 or 2 digits)
// Hour (0-23)  | HH (2 digits)      | H (1 or 2 digits)
// Hour (0-11)  | KK (2 digits)      | K (1 or 2 digits)
// Hour (1-24)  | kk (2 digits)      | k (1 or 2 digits)
// Minute       | mm (2 digits)      | m (1 or 2 digits)
// Second       | ss (2 digits)      | s (1 or 2 digits)
// AM/PM        | a                  |
//
// NOTE THE DIFFERENCE BETWEEN MM and mm! Month=MM, not mm!
// Examples:
//  "MMM d, y" matches: January 01, 2000
//                      Dec 1, 1900
//                      Nov 20, 00
//  "M/d/yy"   matches: 01/20/00
//                      9/2/00
//  "MMM dd, yyyy hh:mm:ssa" matches: "January 01, 2000 12:30:45AM"
// ------------------------------------------------------------------

var MONTH_NAMES=new Array('January','February','March','April','May','June','July','August','September','October','November','December','Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec');
var DAY_NAMES=new Array('Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sun','Mon','Tue','Wed','Thu','Fri','Sat');
function LZ(x) {return(x<0||x>9?"":"0")+x}

// ------------------------------------------------------------------
// isDate ( date_string, format_string )
// Returns true if date string matches format of format string and
// is a valid date. Else returns false.
// It is recommended that you trim whitespace around the value before
// passing it to this function, as whitespace is NOT ignored!
// ------------------------------------------------------------------
function isDate(val,format) {
	var date=getDateFromFormat(val,format);
	if (date===0) { 
            return false; 
        }
	return true;
}

// -------------------------------------------------------------------
// compareDates(date1,date1format,date2,date2format)
//   Compare two date strings to see which is greater.
//   Returns:
//   1 if date1 is greater than date2
//   0 if date2 is greater than date1 of if they are the same
//  -1 if either of the dates is in an invalid format
// -------------------------------------------------------------------
function compareDates(date1,dateformat1,date2,dateformat2) {
	var d1=getDateFromFormat(date1,dateformat1);
	var d2=getDateFromFormat(date2,dateformat2);
	if (d1==0 || d2==0) {
		return -1;
		}
	else if (d1 > d2) {
		return 1;
		}
	return 0;
	}

// ------------------------------------------------------------------
// formatDate (date_object, format)
// Returns a date in the output format specified.
// The format string uses the same abbreviations as in getDateFromFormat()
// ------------------------------------------------------------------
function formatDate(date,format) {
	format=format+"";
	var result="";
	var i_format=0;
	var c="";
	var token="";
	var y=date.getYear()+"";
	var M=date.getMonth()+1;
	var d=date.getDate();
	var E=date.getDay();
	var H=date.getHours();
	var m=date.getMinutes();
	var s=date.getSeconds();
	var yyyy,yy,MMM,MM,dd,hh,h,mm,ss,ampm,HH,H,KK,K,kk,k;
	// Convert real date parts into formatted versions
	var value=new Object();
	if (y.length < 4) {y=""+(y-0+1900);}
	value["y"]=""+y;
	value["yyyy"]=y;
	value["yy"]=y.substring(2,4);
	value["M"]=M;
	value["MM"]=LZ(M);
	value["MMM"]=MONTH_NAMES[M-1];
	value["NNN"]=MONTH_NAMES[M+11];
	value["d"]=d;
	value["dd"]=LZ(d);
	value["E"]=DAY_NAMES[E+7];
	value["EE"]=DAY_NAMES[E];
	value["H"]=H;
	value["HH"]=LZ(H);
	if (H==0){value["h"]=12;}
	else if (H>12){value["h"]=H-12;}
	else {value["h"]=H;}
	value["hh"]=LZ(value["h"]);
	if (H>11){value["K"]=H-12;} else {value["K"]=H;}
	value["k"]=H+1;
	value["KK"]=LZ(value["K"]);
	value["kk"]=LZ(value["k"]);
	if (H > 11) { value["a"]="PM"; }
	else { value["a"]="AM"; }
	value["m"]=m;
	value["mm"]=LZ(m);
	value["s"]=s;
	value["ss"]=LZ(s);
	while (i_format < format.length) {
		c=format.charAt(i_format);
		token="";
		while ((format.charAt(i_format)==c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
			}
		if (value[token] != null) { result=result + value[token]; }
		else { result=result + token; }
		}
	return result;
	}
	
// ------------------------------------------------------------------
// Utility functions for parsing in getDateFromFormat()
// ------------------------------------------------------------------
function _isInteger(val) {
	var digits="1234567890";
	for (var i=0; i < val.length; i++) {
		if (digits.indexOf(val.charAt(i))==-1) { return false; }
		}
	return true;
	}
function _getInt(str,i,minlength,maxlength) {
	for (var x=maxlength; x>=minlength; x--) {
		var token=str.substring(i,i+x);
		if (token.length < minlength) { return null; }
		if (_isInteger(token)) { return token; }
		}
	return null;
	}
	
// ------------------------------------------------------------------
// getDateFromFormat( date_string , format_string )
//
// This function takes a date string and a format string. It matches
// If the date string matches the format string, it returns the 
// getTime() of the date. If it does not match, it returns 0.
// ------------------------------------------------------------------
function getDateFromFormat(val,format) {
	val=val+"";
	format=format+"";
	var i_val=0;
	var i_format=0;
	var c="";
	var token="";
	var token2="";
	var x,y;
	var now=new Date();
	var year=now.getYear();
	var month=now.getMonth()+1;
	var date=1;
	var hh=now.getHours();
	var mm=now.getMinutes();
	var ss=now.getSeconds();
	var ampm="";
	
	while (i_format < format.length) {
		// Get next token from format string
		c=format.charAt(i_format);
		token="";
		while ((format.charAt(i_format)===c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
			}
		// Extract contents of value based on format token
		if (token==="yyyy" || token==="yy" || token==="y") {
			if (token==="yyyy") { x=4;y=4; }
			if (token==="yy")   { x=2;y=2; }
			if (token==="y")    { x=2;y=4; }
			year=_getInt(val,i_val,x,y);
			if (year===null) { return 0; }
			i_val += year.length;
			if (year.length===2) {
				if (year > 70) { year=1900+(year-0); }
				else { year=2000+(year-0); }
				}
			}
		else if (token==="MMM"||token==="NNN"){
			month=0;
			for (var i=0; i<MONTH_NAMES.length; i++) {
				var month_name=MONTH_NAMES[i];
				if (val.substring(i_val,i_val+month_name.length).toLowerCase()===month_name.toLowerCase()) {
					if (token==="MMM"||(token==="NNN"&&i>11)) {
						month=i+1;
						if (month>12) { month -= 12; }
						i_val += month_name.length;
						break;
						}
					}
				}
			if ((month < 1)||(month>12)){return 0;}
			}
		else if (token==="EE"||token==="E"){
			for (var i=0; i<DAY_NAMES.length; i++) {
				var day_name=DAY_NAMES[i];
				if (val.substring(i_val,i_val+day_name.length).toLowerCase()===day_name.toLowerCase()) {
					i_val += day_name.length;
					break;
					}
				}
			}
		else if (token==="MM"||token==="M") {
			month=_getInt(val,i_val,token.length,2);
			if(month===null||(month<1)||(month>12)){return 0;}
			i_val+=month.length;}
		else if (token==="dd"||token==="d") {
			date=_getInt(val,i_val,token.length,2);
			if(date===null||(date<1)||(date>31)){return 0;}
			i_val+=date.length;}
		else if (token==="hh"||token==="h") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh===null||(hh<1)||(hh>12)){return 0;}
			i_val+=hh.length;}
		else if (token==="HH"||token==="H") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<0)||(hh>23)){return 0;}
			i_val+=hh.length;}
		else if (token==="KK"||token==="K") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<0)||(hh>11)){return 0;}
			i_val+=hh.length;}
		else if (token==="kk"||token==="k") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh===null||(hh<1)||(hh>24)){return 0;}
			i_val+=hh.length;hh--;}
		else if (token==="mm"||token==="m") {
			mm=_getInt(val,i_val,token.length,2);
			if(mm===null||(mm<0)||(mm>59)){return 0;}
			i_val+=mm.length;}
		else if (token==="ss"||token==="s") {
			ss=_getInt(val,i_val,token.length,2);
			if(ss===null||(ss<0)||(ss>59)){return 0;}
			i_val+=ss.length;}
		else if (token==="a") {
			if (val.substring(i_val,i_val+2).toLowerCase()==="am") {ampm="AM";}
			else if (val.substring(i_val,i_val+2).toLowerCase()==="pm") {ampm="PM";}
			else {return 0;}
			i_val+=2;}
		else {
			if (val.substring(i_val,i_val+token.length)!==token) {return 0;}
			else {i_val+=token.length;}
                }
            }
	// If there are any trailing characters left in the value, it doesn't match
	if (i_val !== val.length) { return 0; }
	// Is date valid for month?
	if (month===2) {
		// Check for leap year
		if ( ( (year%4===0)&&(year%100 !== 0) ) || (year%400===0) ) { // leap year
			if (date > 29){ return 0; }
			}
		else { if (date > 28) { return 0; } }
		}
	if ((month===4)||(month===6)||(month===9)||(month===11)) {
		if (date > 30) { return 0; }
		}
	// Correct hours value
	if (hh<12 && ampm==="PM") { hh=hh-0+12; }
	else if (hh>11 && ampm==="AM") { hh-=12; }
	var newdate=new Date(year,month-1,date,hh,mm,ss);
	return newdate.getTime();
	}

// ------------------------------------------------------------------
// parseDate( date_string [, prefer_euro_format] )
//
// This function takes a date string and tries to match it to a
// number of possible date formats to get the value. It will try to
// match against the following international formats, in this order:
// y-M-d   MMM d, y   MMM d,y   y-MMM-d   d-MMM-y  MMM d
// M/d/y   M-d-y      M.d.y     MMM-d     M/d      M-d
// d/M/y   d-M-y      d.M.y     d-MMM     d/M      d-M
// A second argument may be passed to instruct the method to search
// for formats like d/M/y (european format) before M/d/y (American).
// Returns a Date object or null if no patterns match.
// ------------------------------------------------------------------
function parseDate(val) {
	var preferEuro=(arguments.length===2)?arguments[1]:false;
	generalFormats=new Array('y-M-d','MMM d, y','MMM d,y','y-MMM-d','d-MMM-y','MMM d');
	monthFirst=new Array('M/d/y','M-d-y','M.d.y','MMM-d','M/d','M-d');
	dateFirst =new Array('d/M/y','d-M-y','d.M.y','d-MMM','d/M','d-M');
	var checkList=new Array('generalFormats',preferEuro?'dateFirst':'monthFirst',preferEuro?'monthFirst':'dateFirst');
	var d=null;
	for (var i=0; i<checkList.length; i++) {
		var l=window[checkList[i]];
		for (var j=0; j<l.length; j++) {
			d=getDateFromFormat(val,l[j]);
			if (d!==0) { return new Date(d); }
			}
		}
	return null;
}



// ------------------------------------------------------------------
//<!-- Begin
// Added by Max to format the date keyed in
var isNav4 = false, isNav5 = false, isIE4 = false
var strSeperator = "/"; 

var vDateType = 3; // Global value for type of date format

var vYearType = 4; //Set to 2 or 4 for number of digits in the year for Netscape
var vYearLength = 2; // Set to 4 if you want to force the user to enter 4 digits for the year before validating.
var err = 0; // Set the error code to a default of zero
if(navigator.appName === "Netscape") {
	if (navigator.appVersion < "5") {
		isNav4 = true;
		isNav5 = false;
	}
	else if (navigator.appVersion > "4") {
		isNav4 = false;
		isNav5 = true;
   	}
}
else {
	isIE4 = true;
}

// vDateName = object name
// vDateValue = value in the field being checked
// e = event
// dateCheck 
// True  = Verify that the vDateValue is a valid date
// False = Format values being entered into vDateValue only
// vDateType
// 1 = mm/dd/yyyy
// 2 = yyyy/mm/dd
// 3 = dd/mm/yyyy
function dateKey(vDate, vDateType, dateCheck,event) {
	var vDateValue = vDate.value;
	// get symbol for date pattern/ type
	if (vDateType === "MM/dd/yyyy") {
		vDateType = '1';
	} else if (vDateType === "yyyy/MM/dd") {
		vDateType = '2';
	} else if (vDateType === "dd/MM/yyyy") {
		vDateType = '3';
	} else if (vDateType === "MM/yyyy") {
		vDateType = '4';
	} else {
		alert("Invalid Date Pattern");
		vDate.value = "";
		return false;
	}
	//Enter a tilde sign for the first number and you can check the variable information.
	if (vDateValue === "~") {
		//alert("AppVersion = "+navigator.appVersion+" \nNav. 4 Version = "+isNav4+" \nNav. 5 Version = "+isNav5+" \nIE Version = "+isIE4+" \nYear Type = "+vYearType+" \nDate Type = "+vDateType+" \nSeparator = "+strSeperator);
		vDate.value = "";
		vDate.focus();
		return true;
	}
	var whichCode;
	if (window.event)
		whichCode = window.event.keyCode;
	else
		whichCode = event.which;

	// Check to see if a seperator is already present.
	// bypass the date if a seperator is present and the length greater than 8
	if (vDateValue.length > 8 && isNav4) {
		if ((vDateValue.indexOf("-") >= 1) || (vDateValue.indexOf("/") >= 1))
			return true;
	}
	//Eliminate all the ASCII codes that are not valid
	if (isNaN(String.fromCharCode(whichCode))&&whichCode.length > 0) {
		//if (isNav4) {
			vDate.value = "";
			vDate.focus();
			vDate.select();
   		//}
		//return false;
	}
	//Ignore the Netscape value for backspace. IE has no value
	//if (whichCode == 8) return false;

	//Create numeric string values for 0123456789/
	//The codes provided include both keyboard and keypad values
	var strCheck = '47,48,49,50,51,52,53,54,55,56,57,58,59,95,96,97,98,99,100,101,102,103,104,105';
	if (strCheck.indexOf(whichCode) != -1) {
		/*if (isNav4) {
			if (((vDateValue.length < 6 && dateCheck) || (vDateValue.length == 7 && dateCheck)) && (vDateValue.length >=1)) {
				alert("Invalid Date\nPlease Re-Enter");
				vDate.value = "";
				vDate.focus();
				vDate.select();
				return false;
			}
			if (vDateValue.length == 6 && dateCheck) {
				var mDay = vDate.value.substr(2,2);
				var mMonth = vDate.value.substr(0,2);
				var mYear = vDate.value.substr(4,4)
				//Turn a two digit year into a 4 digit year
				if (mYear.length == 2 && vYearType == 4) {
					var mToday = new Date();
					//If the year is greater than 30 years from now use 19, otherwise use 20
					var checkYear = mToday.getFullYear() + 30; 
					var mCheckYear = '20' + mYear;
					if (mCheckYear >= checkYear)
						mYear = '19' + mYear;
					else
						mYear = '20' + mYear;
				}
				var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
				if (!dateValid(vDateValueCheck)) {
					alert("Invalid Date\nPlease Re-Enter");
					vDate.value = "";
					vDate.focus();
					vDate.select();
					return false;
				}
				return true;
			}
			else {
				// Reformat the date for validation and set date type to a 1
				if (vDateValue.length >= 8  && dateCheck) {
					if (vDateType == 1) { // mmddyyyy 
						var mDay = vDate.value.substr(2,2);
						var mMonth = vDate.value.substr(0,2);
						var mYear = vDate.value.substr(4,4)
						vDate.value = mMonth+strSeperator+mDay+strSeperator+mYear;
					}
					if (vDateType == 2) { // yyyymmdd
						var mYear = vDate.value.substr(0,4)
						var mMonth = vDate.value.substr(4,2);
						var mDay = vDate.value.substr(6,2);
						vDate.value = mYear+strSeperator+mMonth+strSeperator+mDay;
					}
					if (vDateType == 3) { // ddmmyyyy
						var mMonth = vDate.value.substr(2,2);
						var mDay = vDate.value.substr(0,2);
						var mYear = vDate.value.substr(4,4)
						vDate.value = mDay+strSeperator+mMonth+strSeperator+mYear;
					}
					if (vDateType == 4) { // mmyyyy
						var mMonth = vDate.value.substr(0,2);
						//var mDay = vDate.value.substr(0,2);
						var mYear = vDate.value.substr(2,4)
						vDate.value = mMonth+strSeperator+mYear;
					}
					//Create a temporary variable for storing the DateType and change
					//the DateType to a 1 for validation.
					var vDateTypeTemp = vDateType;
					var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
					if (vDateType == 4){
						vDateValueCheck = mMonth+strSeperator+"01"+strSeperator+mYear;
					}
					
					vDateType = 1;
					var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
					if (!dateValid(vDateValueCheck)) {
						alert("Invalid Date\nPlease Re-Enter");
						vDateType = vDateTypeTemp;
						vDate.value = "";
						vDate.focus();
						vDate.select();
						return false;
					}
					vDateType = vDateTypeTemp;
					return true;
				}
				else {
					if (((vDateValue.length < 8 && dateCheck) || (vDateValue.length == 9 && dateCheck)) && (vDateValue.length >=1)) {
						alert("Invalid Date\nPlease Re-Enter");
						vDate.value = "";
						vDate.focus();
						vDate.select();
						return false;
		         	}
				} // date length = 8
			} // date length = 6
		} // isNav4 
		else {*/
			// Non isNav Check
			if (((vDateValue.length < 8 && dateCheck) || (vDateValue.length == 9 && dateCheck)) && (vDateValue.length >=1)) {
				alert("Invalid Date\nPlease Re-Enter");
				vDate.value = "";
				vDate.focus();
				return true;
			}
			// Reformat date to format that can be validated. mm/dd/yyyy
			if (vDateValue.length >= 8 && dateCheck) {
				// Additional date formats can be entered here and parsed out to
				// a valid date format that the validation routine will recognize.
				if (vDateType == 1) { // mm/dd/yyyy
					var mMonth = vDate.value.substr(0,2);
					var mDay = vDate.value.substr(3,2);
					var mYear = vDate.value.substr(6,4)
				}
				if (vDateType == 2)	{ // yyyy/mm/dd
					var mYear = vDate.value.substr(0,4)
					var mMonth = vDate.value.substr(5,2);
					var mDay = vDate.value.substr(8,2);
				}
				if (vDateType == 3)	{ // dd/mm/yyyy
					var mDay = vDate.value.substr(0,2);
					var mMonth = vDate.value.substr(3,2);
					var mYear = vDate.value.substr(6,4)
				}
				if (vDateType == 4) { // mmyyyy
					alert(vDate.value);
					var mMonth = vDate.value.substr(0,2);
					//var mDay = vDate.value.substr(0,2);
					var mYear = vDate.value.substr(2,4)
					vDate.value = mMonth+strSeperator+mYear;
				}
				if (vYearLength == 4) {
					if (mYear.length < 4) {
						alert("Invalid Date\nPlease Re-Enter");
						vDate.value = "";
						vDate.focus();
						return true;
			   		}
				}
				// Create temp. variable for storing the current vDateType
				var vDateTypeTemp = vDateType;
				
				// Store reformatted date to new variable for validation.
				var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
				if (vDateType == 4) { // mmyyyy
					vDateValueCheck = mMonth+strSeperator+"01"+strSeperator+mYear;
				}
				
				// Change vDateType to a 1 for standard date format for validation
				// Type will be changed back when validation is completed.
				vDateType = 1;
				// Store reformatted date to new variable for validation.
				var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
				if (mYear.length == 2 && vYearType == 4 && dateCheck) {
					//Turn a two digit year into a 4 digit year
					var mToday = new Date();
					//If the year is greater than 30 years from now use 19, otherwise use 20
					var checkYear = mToday.getFullYear() + 30; 
					var mCheckYear = '20' + mYear;
					if (mCheckYear >= checkYear)
						mYear = '19' + mYear;
					else
						mYear = '20' + mYear;
					vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
					// Store the new value back to the field.  This function will
					// not work with date type of 2 since the year is entered first.
					if (vDateTypeTemp == 1) // mm/dd/yyyy
						vDate.value = mMonth+strSeperator+mDay+strSeperator+mYear;
					if (vDateTypeTemp == 3) // dd/mm/yyyy
						vDate.value = mDay+strSeperator+mMonth+strSeperator+mYear;
					if (vDateTypeTemp == 4) // mm/yyyy
						vDate.value = mMonth+strSeperator+mYear;
				} 
				if (!dateValid(vDateValueCheck)) {
					alert("Invalid Date\nPlease Re-Enter");
					vDateType = vDateTypeTemp;
					vDate.value = "";
					vDate.focus();
					return true;
				}
				vDateType = vDateTypeTemp;
				return true;
			} // date length >= 8
			else {
				if (vDateType == 1) {
					if (vDateValue.length == 2) {
						vDate.value = vDateValue+strSeperator;
					}
					if (vDateValue.length == 5) {
						vDate.value = vDateValue+strSeperator;
			   		}
				}
				if (vDateType == 2) {
					if (vDateValue.length == 4) {
						vDate.value = vDateValue+strSeperator;
					}
					if (vDateValue.length == 7) {
						vDate.value = vDateValue+strSeperator;
					}
				} 
				if (vDateType == 3) {
					if (vDateValue.length == 2) {
						vDate.value = vDateValue+strSeperator;
					}
					if (vDateValue.length == 5) {
						vDate.value = vDateValue+strSeperator;
			   		}
				}
				if (vDateType == 4) {
					if (vDateValue.length == 2) {
						vDate.value = vDateValue+strSeperator;
					}
				}
				return true;
			}
			if (vDateValue.length == 10 && dateCheck) {
				if (!dateValid(vDate)) {
					// Un-comment the next line of code for debugging the dateValid() function error messages
					alert("Invalid Date\nPlease Re-Enter");
					vDate.focus();
					vDate.select();
				}
			}
			return false;
		}
	//}
	else { // end str check
		// If the value is not in the string return the string minus the last
		// key entered.
		/*if (isNav4) {
			vDate.value = "";
			vDate.focus();
			vDate.select();
			return false;
		}
		else {*/
			return false;
		//}
	}
}

function dateValid(objName) {
	var strDate;
	var strDateArray;
	var strDay;
	var strMonth;
	var strYear;
	var intday;
	var intMonth;
	var intYear;
	var booFound = false;
	var datefield = objName;
	var strSeparatorArray = new Array("-"," ","/",".");
	var intElementNr;
	// var err = 0;
	var strMonthArray = new Array(12);
	strMonthArray[0] = "Jan";
	strMonthArray[1] = "Feb";
	strMonthArray[2] = "Mar";
	strMonthArray[3] = "Apr";
	strMonthArray[4] = "May";
	strMonthArray[5] = "Jun";
	strMonthArray[6] = "Jul";
	strMonthArray[7] = "Aug";
	strMonthArray[8] = "Sep";
	strMonthArray[9] = "Oct";
	strMonthArray[10] = "Nov";
	strMonthArray[11] = "Dec";
	//strDate = datefield.value;
	strDate = objName;
	if (strDate.length < 1) {
		return true;
	}
	for (intElementNr = 0; intElementNr < strSeparatorArray.length; intElementNr++) {
		if (strDate.indexOf(strSeparatorArray[intElementNr]) != -1) {
			strDateArray = strDate.split(strSeparatorArray[intElementNr]);
			if (strDateArray.length != 3) {
				err = 1;
				return false;
			}
			else {
				strDay = strDateArray[0];
				strMonth = strDateArray[1];
				strYear = strDateArray[2];
			}
			booFound = true;
		}
	}
	if (booFound == false) {
		if (strDate.length>5) {
			strDay = strDate.substr(0, 2);
			strMonth = strDate.substr(2, 2);
			strYear = strDate.substr(4);
		}
	}
	//Adjustment for short years entered
	if (strYear.length == 2) {
		strYear = '20' + strYear;
	}
	strTemp = strDay;
	strDay = strMonth;
	strMonth = strTemp;
	intday = parseInt(strDay, 10);
	if (isNaN(intday)) {
		err = 2;
		return false;
	}
	intMonth = parseInt(strMonth, 10);
	if (isNaN(intMonth)) {
		for (i = 0;i<12;i++) {
			if (strMonth.toUpperCase() == strMonthArray[i].toUpperCase()) {
				intMonth = i+1;
				strMonth = strMonthArray[i];
				i = 12;
	   		}
		}
		if (isNaN(intMonth)) {
			err = 3;
			return false;
   		}
	}
	intYear = parseInt(strYear, 10);
	if (isNaN(intYear)) {
		err = 4;
		return false;
	}
	if (intMonth>12 || intMonth<1) {
		err = 5;
		return false;
	}
	if ((intMonth == 1 || intMonth == 3 || intMonth == 5 || intMonth == 7 || intMonth == 8 || intMonth == 10 || intMonth == 12) && (intday > 31 || intday < 1)) {
		err = 6;
		return false;
	}
	if ((intMonth == 4 || intMonth == 6 || intMonth == 9 || intMonth == 11) && (intday > 30 || intday < 1)) {
		err = 7;
		return false;
	}
	if (intMonth == 2) {
		if (intday < 1) {
			err = 8;
			return false;
		}
		if (leapYear(intYear) == true) {
			if (intday > 29) {
				err = 9;
				return false;
	   		}
		}
		else {
			if (intday > 28) {
				err = 10;
				return false;
			}
		}
	}
	return true;
}

function leapYear(intYear) {
	if (intYear % 100 == 0) {
		if (intYear % 400 == 0) { return true; }
	}
	else {
		if ((intYear % 4) == 0) { return true; }
	}
	return false;
}
//  End -->

/* 
 * validate date for the given pattern
 * d - date object
 * pattern - date format
 */
function valDate(d, pattern, label) {
	d.value = d.value.trim();
	if (d.value != "" && !d.readOnly) {
		if (!isDate(d.value, pattern)) {
			alert(formatText(messageInvalidDateFormat, label ? label : "Date", pattern));
			d.value = "";
			d.focus();
			return false;
		}
	}
	return true;
}

// compute new date from the given date for the given number of days
function computeDate(val, days, dateFormat) {
	var MilliSecondsBase = 86400000;
	// get raw date value
	var fromDate = getDateFromFormat(val,dateFormat);
	var newDate = new Date(fromDate+(MilliSecondsBase*(days)));
	return formatDate(newDate,dateFormat);
}

function validateDate(field, format) {
    if (!isDate(field.value, format)) {
        document.getElementById(field.id).focus();
        alert("invalid date format");
        return false;
    }
}