package com.sains.common.util;

import java.util.StringTokenizer;

import com.opensymphony.xwork2.ActionSupport;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.text.StringEscapeUtils;
import org.hibernate.query.NativeQuery;

public class CriteriaConverter {

    private String wildSearchChar = "%";
    //private String wildSearch = new ActionSupport().getText("search.wildSearch", "N");
    private String wildSearch = "Y";
//    private boolean isOracle = Boolean.FALSE;
    public String dbType = null;
    private boolean isOracle = Boolean.TRUE;//set true to set lower case column value
    private boolean noChange = false;
    private boolean noChangeLower = false;
    private boolean wildCardFront = false;
    private boolean wildCardBack = false;
    private boolean globalIsDate = Boolean.FALSE;
    private boolean noChangeDate = Boolean.FALSE;
    public Map<String, Object> criteriaMap = null;
    public Boolean populateCriteria = Boolean.FALSE;
    public CriteriaConverter() {
    }

    ;//default constructor

    public CriteriaConverter(Boolean populateCriteria) {
        this.populateCriteria = populateCriteria;
        if (this.populateCriteria) {
            criteriaMap = new HashMap();
        }
    }
    public CriteriaConverter(String isWildSearch) {
        if (isWildSearch.equalsIgnoreCase("Y")) {
            wildSearch = "Y";
        } else {
            wildSearch = "N";
        }
    }

    public void setIsOracleDB() {
        isOracle = true;
    }

    public String strCriteria(String operator, String columnName, String criteria) {
        if (columnName.indexOf(SystemConstants.CRITERIA.NESTEDSQL) >= 0)  return " " + operator + " " + criteria;  // ThoTH @ 24-Jan-2014 :: For custom Criteria, e.g. and (column_1 = 'abc' and column_2 = 'abc') or (column_1 = 'abc' and column_3 = 'abc')
        //System.out.println("column name " +columnName);
        noChange = Boolean.FALSE;
        if (columnName.startsWith(SystemConstants.CRITERIA.NO_CHANGE_LOWER)){
            noChangeLower = Boolean.TRUE;
            noChange = Boolean.TRUE;
            columnName = columnName.substring(SystemConstants.CRITERIA.NO_CHANGE_LOWER.length());
        } else if (columnName.startsWith(SystemConstants.CRITERIA.NO_CHANGE)){
            noChange = Boolean.TRUE;
            columnName = columnName.substring(SystemConstants.CRITERIA.NO_CHANGE.length());
        }

        if (columnName.startsWith(SystemConstants.CRITERIA.WILD_CARD_FRONT)){
            wildCardFront = Boolean.TRUE;
            columnName = columnName.substring(SystemConstants.CRITERIA.WILD_CARD_FRONT.length());
        } else if (columnName.startsWith(SystemConstants.CRITERIA.WILD_CARD_BACK)){
            wildCardBack = Boolean.TRUE;
            columnName = columnName.substring(SystemConstants.CRITERIA.WILD_CARD_BACK.length());
        }

        if (columnName.startsWith(SystemConstants.CRITERIA.DATE_CRITERIA)){
            Debug.printDebug("date criteria and criteria "+criteria);
//            noChange = Boolean.TRUE;
            noChangeDate = Boolean.TRUE;
            columnName = columnName.substring(SystemConstants.CRITERIA.DATE_CRITERIA.length());
            globalIsDate = Boolean.TRUE;
            String tempDateCriteria = dateCriteria(operator, columnName, criteria);
            globalIsDate = Boolean.FALSE;
            noChangeDate = Boolean.FALSE;
//            noChange = Boolean.FALSE;
            return  tempDateCriteria;
        }

        //if (isOracle) {
            if (noChangeLower) {
                columnName = "lower(rtrim(ltrim(" + columnName + ")))";
                criteria = criteria.toLowerCase();
            } else if (noChange) {
                columnName = "rtrim(ltrim(" + columnName + "))";
            } else if (noChangeDate) {
               
            } else {
                columnName = "lower(rtrim(ltrim(" + columnName + ")))";
                criteria = criteria.toLowerCase();
            }
       // }
        //String wildSearch = new ActionSupport().getText("search.wildSearch", "N");
        if (criteria == null) {
            return "";
        }

        if (!globalIsDate) {
            if (!populateCriteria) {
                criteria = convertSQLChar(criteria);
            }
        }

        //if (noChange) return " " + operator + " " + columnName + " = '" + criteria + "'";


        if (criteria.indexOf(",") >= 0 || criteria.indexOf("<>") >= 0) {
            if (noChange) {
                return moreCriteria(operator, columnName, criteria, "N", true, false);
            } else {
                return moreCriteria(operator, columnName, criteria, wildSearch, true, false);
            }
        } else if (criteria.indexOf(",") >= 0 || criteria.indexOf("!in") >= 0 || criteria.indexOf("!IN") >= 0) {
            if (noChange) {
                return moreCriteria(operator, columnName, criteria, "N", true, false);
            } else {
                return moreCriteria(operator, columnName, criteria, wildSearch, true, false);
            }
        }

        String temp = "";
        if (noChange) {
            temp = greaterSmallerCriteria(criteria, "N", true, false);
        } else {
            temp = greaterSmallerCriteria(criteria, wildSearch, true, false);
        }
        //System.out.println("temp="+temp);
        if (!(temp.equals(""))) {
            //System.out.println("return="+operator + " " + columnName + " " + temp);
            return " " + operator + " " + columnName + " " + temp;
        }
        //System.out.println("criteria="+criteria);
        if (criteria.trim().equalsIgnoreCase("is null") || criteria.trim().equalsIgnoreCase("is not null")){
            if (criteria.trim().equalsIgnoreCase("is not null")) {
                return " " + operator + " (" + columnName + " is not null and "+columnName+" != '')";
            }
            return " " + operator + " " + columnName + " " + criteria;
        }

        if (!noChange) {
            if (wildSearch.equals("Y")) {
                if (criteria.indexOf("\"") >= 0) {
                    criteria = criteria.replaceAll("\"", "");
                } else {
                    if (wildCardFront) {
                        criteria = "%" + criteria;
                    } else if (wildCardBack) {
                        criteria = criteria + '%';
                    } else {
                        criteria = "%" + criteria + "%";
                    }
                }
            }
        }

        if (criteria.indexOf(wildSearchChar) >= 0) {
            if (populateCriteria) {
                criteriaMap.put("C"+criteriaMap.size(), criteria);
                return " " + operator + " " + columnName + " like " + ":C"+ (criteriaMap.size()-1) + " " + addCollate() + " ";
            }
            return " " + operator + " " + columnName + " like '" + criteria + "'";
        }
        if (globalIsDate) {
            if (populateCriteria) {
                criteriaMap.put("C"+criteriaMap.size(), criteria);
                return " " + operator + " " + columnName + " = " + ":C"+ (criteriaMap.size()-1) + " " + addCollate() + " ";
            }
            return " " + operator + " " + columnName + " = " + criteria;
        }
        if (populateCriteria) {
            criteriaMap.put("C"+criteriaMap.size(), criteria);
            return " " + operator + " " + columnName + " = " + ":C"+ (criteriaMap.size()-1) + " " + addCollate() + " ";
        }
        return " " + operator + " " + columnName + " = '" + criteria + "'";
    }

    public String dateCriteria(String operator, String columnName, String criteria) {
        if (!populateCriteria) {
            if (!criteria.startsWith("'")) {
                criteria = "'"+criteria+"'";
            }
        }
        String temp = null;
        //if (isOracle) {
            //columnName = "lower(trim(" + columnName + "))";
        //}
        if (criteria == null) {
            return "";
        }
//        if (!isOracle) {
//            criteria = convertSQLChar(criteria);
//        }
        if (criteria.indexOf(",") >= 0 || criteria.indexOf("<>") >= 0) {
//            if (!isOracle) {
//                temp = moreCriteria(operator, columnName, criteria, "N", true, false);
//            } else {
//                temp = moreCriteria(operator, columnName, criteria, "N", false, false);
//            }
            temp = moreCriteria(operator, columnName, criteria, "N", false, true);
            if (temp != null && temp.length() > 0){
                temp = replaceCriteriaComma(temp);
            }
            return temp;
        }

//        criteria = replaceCriteriaComma(criteria);
        if ((criteria.indexOf(">") == 0) || (criteria.indexOf("<") == 0) || (criteria.indexOf("<=") == 0) || (criteria.indexOf(">=") == 0)) {
            temp = moreCriteria(operator, columnName, criteria, "N", false, true);
//            String tempWildSearch = wildSearch;
//            wildSearch = "N";
//            temp = strCriteria("", columnName, criteria);
//            temp = replaceCriteriaComma(temp);
//            wildSearch = tempWildSearch;
            return temp;
            //return " " + operator + " " + columnName + " " + criteria;
        }
        
//        if (!isOracle){
//            criteria = addSingleQuote(criteria);
//        }
        if (populateCriteria) {
            if (criteria.equalsIgnoreCase("is null") || criteria.equalsIgnoreCase("is empty")) {
                return " " + operator + " " + columnName + " is null ";
            }
            criteriaMap.put("C"+criteriaMap.size(), criteria);
            return " " + operator + " " + columnName + " = " + ":C"+ (criteriaMap.size()-1) + " " + addCollate() + " ";
        }
        return " " + operator + " " + columnName + " = " + criteria;
    }

    public String numericCriteria(String operator, String columnName, String criteria) {
        if (isOracle) {
            columnName = "lower(rtrim(ltrim(" + columnName + ")))";
        }
        if (criteria == null) {
            return "";
        }
        String wildSearch = "N";
        String temp = "";

        if (criteria.indexOf(",") >= 0 || criteria.indexOf("<>") >= 0) {
            return moreCriteria(operator, columnName, criteria, wildSearch, false, false);
        }

        temp = greaterSmallerCriteria(criteria, wildSearch, true, false);
        if (!(temp.equals(""))) {
            return " " + operator + " " + columnName + " " + temp;
        }

        if (populateCriteria) {
            try {
                new Long(criteria);
                criteriaMap.put("C"+criteriaMap.size(), new Long(criteria));
            } catch (Exception e) {
                new Double(criteria);
                criteriaMap.put("C"+criteriaMap.size(), new Double(criteria));
            }
            return " " + operator + " " + columnName + " = " + ":C"+ (criteriaMap.size()-1) + " " + addCollate() + " ";
        }
        return " " + operator + " " + columnName + " = " + criteria;
    }

    public String convertSQLChar(String str) {
        str = str.replaceAll("'", "''");
        str = str.replaceAll("\\\\", "\\\\\\\\");
        return str.trim();
    }

    private String moreCriteria(String operator, String columnName, String criteria, String wildSearch, boolean isStringCriteria, boolean isDate) {
        StringTokenizer st = new StringTokenizer(criteria, ",");
        StringTokenizer stNotIn;
        String temp = "";
        String tempNotIn = "";
        String likeSearch = "";
        String inSearch = "";
        String inSearchIsNull = "";
        String returnCriteria = "";
        String glSearch = ""; //greater or smaller search
        String btSearch = ""; //between search
        String notInSearch = ""; //not in search
//        if (isOracle) {
//            columnName = "lower(trim(" + columnName + "))";
//        }
        String oriColName = columnName;
        if (isOracle && columnName.indexOf("ltrim(") < 0 ) {
            if (isDate || noChangeDate) {
                //do nothing
            } else if (noChange) {
                columnName = "rtrim(ltrim(" + columnName + "))";
            } else {
                columnName = "lower(rtrim(ltrim(" + columnName + ")))";
            }
        }
        while (st.hasMoreTokens()) {
            temp = st.nextToken().trim();
            if ((temp.indexOf("!in") == 0) || (temp.indexOf("!IN") == 0)) {
                String tempNotLike = "";
                if (temp.indexOf("!in") == 0) {
                    temp = temp.replaceFirst("!in", "");
                } else {
                    temp = temp.replaceFirst("!IN", "");
                }
                if (temp.indexOf(new String("|")) > 0) {
                    stNotIn = new StringTokenizer(temp, new String("|"));
                    while (stNotIn.hasMoreTokens()) {
                        String notInString = stNotIn.nextToken().trim();
                        if (notInString.contains(wildSearchChar)) {
                            if (tempNotLike.equals("")) {
                                tempNotLike += notInString;
                            } else {
                                if (populateCriteria) {
                                    tempNotLike += "," + notInString;
                                } else {
                                    tempNotLike += "','" + notInString;
                                }
                            }
                        } else {
                            if (tempNotIn.equals("")) {
                                tempNotIn += notInString;
                            } else {
                                if (populateCriteria) {
                                    tempNotIn += "," + notInString;
                                } else {
                                    tempNotIn += "','" + notInString;
                                }
                            }
                        }
                    }
                } else {
                    tempNotIn = temp.trim();
                }
                if (notInSearch.equals("")) {
                    if (!tempNotLike.equals("")) {
                        if (populateCriteria) {
                            criteriaMap.put("C"+criteriaMap.size(), tempNotIn.split(","));
                            criteriaMap.put("C"+criteriaMap.size(), tempNotLike);
                            notInSearch += "("+columnName + " not in (" + (":C"+(criteriaMap.size()-2) ) + " " + addCollate() + " ) or "+columnName + " not like (" + (":C"+(criteriaMap.size()-1) ) + " " + addCollate() + " ))  ";
                        } else {
                            notInSearch += "("+columnName + " not in ('" + tempNotIn + "') or "+columnName + " not like ('" + tempNotLike + "'))";
                        }
                    } else {
                        if (populateCriteria) {
                            criteriaMap.put("C"+criteriaMap.size(), tempNotIn.split(","));
                            notInSearch += columnName + " not in (" + (":C"+(criteriaMap.size()-1) ) + " " + addCollate() + " ) ";
                        } else {
                            notInSearch += columnName + " not in ('" + tempNotIn + "')";
                        }
                    }
                } else {
                    if (!tempNotLike.equals("")) {
                        if (populateCriteria) {
                            criteriaMap.put("C"+criteriaMap.size(), tempNotIn.split(","));
                            criteriaMap.put("C"+criteriaMap.size(), tempNotLike);
                            notInSearch += " or ("+columnName + " not in (" + (":C"+(criteriaMap.size()-2) ) + " " + addCollate() + ") or "+columnName + " not like (" + (":C"+(criteriaMap.size()-1) ) + " " + addCollate() + "))";
                        } else {
                            notInSearch += " or ("+columnName + " not in ('" + tempNotIn + "') or "+columnName + " not like ('" + tempNotLike + "'))";
                        }
                    } else {
                        if (populateCriteria) {
                            criteriaMap.put("C"+criteriaMap.size(), tempNotIn.split(","));
                            notInSearch += " or " + columnName + " not in (" + (":C"+(criteriaMap.size()-1) ) + " " + addCollate() + ")";
                        } else {
                            notInSearch += " or " + columnName + " not in ('" + tempNotIn + "')";
                        }
                    }
                }
            } else if ((temp.indexOf("<>") == 0)) {
                if (btSearch.equals("")) {
                    btSearch += columnName + " " + betweenCriteria(temp, isStringCriteria, isDate);
                } else {
                    btSearch += " or " + columnName + " " + betweenCriteria(temp, isStringCriteria, isDate);
                }
            } else if ((temp.indexOf("<") == 0) || (temp.indexOf(">") == 0) || (temp.indexOf("<=") == 0) || (temp.indexOf(">=") == 0)) { //populate ">" or "<" search criteria
                if (glSearch.equals("")) {
//					System.out.println(3);
                    glSearch += columnName + " " + greaterSmallerCriteria(temp, wildSearch, isStringCriteria, isDate);
                } else {
//					System.out.println(4);
                    glSearch += " and " + columnName + " " + greaterSmallerCriteria(temp, wildSearch, isStringCriteria, isDate);
                }
            } else if (temp.indexOf(wildSearchChar) >= 0) { //populate "like" Search criteria
                if (likeSearch.equals("")) {
//					System.out.println(1);
                    if (populateCriteria) {
                        criteriaMap.put("C"+criteriaMap.size(), temp);
                        likeSearch = " " + columnName + " like :C" + (criteriaMap.size()-1) + " " + addCollate() + " ";
                    } else {
                        likeSearch = " " + columnName + " like '" + temp + "'";
                    }
                } else {
//					System.out.println(2);
                    if (populateCriteria) {
                        criteriaMap.put("C"+criteriaMap.size(), temp);
                        likeSearch += " or " + columnName + " like :C" + (criteriaMap.size()-1) + " " + addCollate() + " ";
                    } else {
                        likeSearch += " or " + columnName + " like '" + temp + "'";
                    }
                }
            } else { // populate "in" Search criteria
                if (wildSearch.equals("Y")) { // change to wildcard search if wild search is set to "Y"
                    String like_or_equal;
                    if (temp.indexOf("\"") >= 0) {
                        temp = temp.replaceAll("\"", "");
                        if (criteria.indexOf(wildSearchChar) >= 0) {
                            like_or_equal = " like ";
                        } else {
                            like_or_equal = " = ";
                        }
                    } else {
                        if (temp.trim().equalsIgnoreCase("is null") || temp.trim().equalsIgnoreCase("is not null")) {
                            like_or_equal = " ";
                        } else {
                            like_or_equal = " like ";
                            temp = "%" + temp + "%";
                        }
                    }
                    if (likeSearch.equals("")) {
//						System.out.println(1);
                        if (like_or_equal.equals(" ")) {
                            likeSearch = " " + oriColName + temp;
                        } else {
                            if (populateCriteria) {
                                criteriaMap.put("C"+criteriaMap.size(), temp);
                                likeSearch = " " + columnName + like_or_equal +" :C" + (criteriaMap.size()-1) + " " + addCollate() + " ";
                            } else {
                                likeSearch = " " + columnName + like_or_equal +"'" + temp + "'";
                            }
                        }
                    } else {
//						System.out.println(2);
                        if (like_or_equal.equals(" ")) {
                            likeSearch += " or " + oriColName + temp;
                        } else {
                            if (populateCriteria) {
                                criteriaMap.put("C"+criteriaMap.size(), temp);
                                likeSearch += " or " + columnName + like_or_equal + " :C" + (criteriaMap.size()-1) + " " + addCollate() + " ";
                            } else {
                                likeSearch += " or " + columnName + like_or_equal + "'" + temp + "'";
                            }
                        }
                    }
                    
                } else {
                    if (temp.trim().equalsIgnoreCase("is null") || temp.trim().equalsIgnoreCase("is not null")) {
                        if (inSearchIsNull.equals("")) {
                            if (temp.trim().equalsIgnoreCase("is not null")) {
                                inSearchIsNull = " (" + columnName + " is not null and "+columnName+" != '')";
                            } else {
                                inSearchIsNull = " " + columnName + " " +temp.trim();
                            }
                        } else {
                            if (temp.trim().equalsIgnoreCase("is not null")) {
                                inSearchIsNull += " (" + columnName + " is not null and "+columnName+" != '')";
                            } else {
                                inSearchIsNull += " or " + columnName + " " +temp.trim();
                            }
                        }
                    } else {
                        if (inSearch.equals("")) {
//						System.out.println(5);
                            inSearch += "'" + temp + "'";
                        } else {
//						System.out.println(6);
                            inSearch += ", '" + temp + "'";
                        }
                    }
                }
            }
        }
        if (glSearch.equals("")) {
            glSearch = btSearch;
        } else if (!btSearch.equals("")) {
            glSearch += ") or ( " + btSearch;
        }

        if (!notInSearch.equals("")) {
            if (glSearch.equals("")) {
                glSearch = notInSearch;
            } else {
                glSearch += ") or ( " + notInSearch;
            }
        }
        if (inSearch.equals("")) {
            if (glSearch.equals("")) {
                returnCriteria = " " + operator + " (" + likeSearch + " "+ (inSearchIsNull.equals("")?"":"or"+inSearchIsNull) +")";
                if (likeSearch.equals("")){
                    returnCriteria = inSearchIsNull;
                }
            } else if (likeSearch.equals("")) {
                returnCriteria = " " + operator + " ( " + glSearch + " "+ (inSearchIsNull.equals("")?"":"or"+inSearchIsNull) +")";
                if (glSearch.equals("")){
                    returnCriteria = inSearchIsNull;
                }
            } else {
                returnCriteria = " " + operator + " (" + likeSearch + " or (" + glSearch + ") "+ (inSearchIsNull.equals("")?"":"or"+inSearchIsNull) +")";
                if (likeSearch.equals("") && glSearch.equals("")){
                    returnCriteria = inSearchIsNull;
                }
            }
        } else {
            if (likeSearch.equals("")) {
                if (glSearch.equals("")) {
                    returnCriteria = " " + operator + " (" + columnName + " in (" + inSearch + ") "+ (inSearchIsNull.equals("")?"":"or"+inSearchIsNull) +")";
                    if (inSearch.equals("")){
                        returnCriteria = inSearchIsNull;
                    }
                } else {
                    returnCriteria = " " + operator + " (" + columnName + " in (" + inSearch + ") or (" + glSearch + ") "+ (inSearchIsNull.equals("")?"":"or"+inSearchIsNull) +" )";
                    if (inSearch.equals("") && glSearch.equals("")){
                        returnCriteria = inSearchIsNull;
                    }
                }
            } else {
                if (glSearch.equals("")) {
                    returnCriteria = " " + operator + " (" + columnName + " in (" + inSearch + ") or (" + likeSearch + ") "+ (inSearchIsNull.equals("")?"":"or"+inSearchIsNull) +" )";
                    if (inSearch.equals("") && likeSearch.equals("")){
                        returnCriteria = inSearchIsNull;
                    }
                } else {
                    returnCriteria = " " + operator + " (" + columnName + " in (" + inSearch + ") or (" + likeSearch + ") or (" + glSearch + ") "+ (inSearchIsNull.equals("")?"":"or"+inSearchIsNull) +" )";
                    if (inSearch.equals("") && likeSearch.equals("") && glSearch.equals("")){
                        returnCriteria = inSearchIsNull;
                    }
                }
            }
        }
        if (returnCriteria.equals("")){
            //System.out.println("in here?  " + operator + " (" + inSearchIsNull + ") ");
            return " " + operator + " (" + inSearchIsNull + ") ";
        }
        return returnCriteria;
    }

    private String greaterSmallerCriteria(String criteria, String wildSearch, boolean isStrCriteria, boolean isDate) {
        /// do checking for wildSearch!
        if (criteria.indexOf(">=") == 0) {
            criteria = criteria.replaceFirst(">=", "");
            //-- removed by thensw: no need % if it is a greater smaller condition.
//            if (wildSearch.equals("Y")) {
//                criteria = criteria.trim() + "%";
//            }
            if (isDate) {
                if(CommonFunction.verifyDate(criteria).equals("dd/MM/yyyy")){
                    criteria = ddMMyyyy_to_yyyyMMdd(criteria);
                }
            }
            if (isStrCriteria) {
                if (!globalIsDate) {
                    criteria = addSingleQuote(criteria);
                }
            }
			criteria = StringEscapeUtils.escapeHtml4(criteria);
            if (populateCriteria) {
                if (isStrCriteria || isDate) {
                    criteriaMap.put("C"+criteriaMap.size(), criteria.trim());
                } else {
                    criteriaMap.put("C"+criteriaMap.size(), new Integer(criteria.trim()));
                }
                return ">= :C" + (criteriaMap.size()-1) + " " + addCollate() + " ";
            } else {
                return ">= " + criteria.trim();
            }
        } else if (criteria.indexOf("<=") == 0) {
            criteria = criteria.replaceFirst("<=", "");
            //-- removed by thensw: no need % if it is a greater smaller condition.
//            if (wildSearch.equals("Y")) {
//                criteria = criteria.trim() + "%";
//            }
            if (isDate) {
                if(CommonFunction.verifyDate(criteria).equals("dd/MM/yyyy")){
                    criteria = ddMMyyyy_to_yyyyMMdd(criteria);
                }
            }
            if (isStrCriteria) {
                if (!globalIsDate) {
                    criteria = addSingleQuote(criteria);
                }
            }
			criteria = StringEscapeUtils.escapeHtml4(criteria);
            if (populateCriteria) {
                if (isStrCriteria || isDate) {
                    criteriaMap.put("C"+criteriaMap.size(), criteria.trim());
                } else {
                    criteriaMap.put("C"+criteriaMap.size(), new Integer(criteria.trim()));
                }
                return "<= :C" + (criteriaMap.size()-1) + " " + addCollate() + " ";
            } else {
                return "<= " + criteria.trim();
            }
        } else if (criteria.indexOf(">") == 0) {
            criteria = criteria.replaceFirst(">", "");
            //-- removed by thensw: no need % if it is a greater smaller condition.
//            if (wildSearch.equals("Y")) {
//                criteria = criteria.trim() + "%";
//            }
            if (isDate) {
                if(CommonFunction.verifyDate(criteria).equals("dd/MM/yyyy")){
                    criteria = ddMMyyyy_to_yyyyMMdd(criteria);
                }
            }
            if (isStrCriteria) {
                if (!globalIsDate) {
                    criteria = addSingleQuote(criteria);
                }
            }
			criteria = StringEscapeUtils.escapeHtml4(criteria);
            if (populateCriteria) {
                if (isStrCriteria || isDate) {
                    criteriaMap.put("C"+criteriaMap.size(), criteria.trim());
                } else {
                    criteriaMap.put("C"+criteriaMap.size(), new Integer(criteria.trim()));
                }
//                criteriaMap.put("C"+criteriaMap.size(), criteria.trim());
                return "> :C" + (criteriaMap.size()-1) + " " + addCollate() + " ";
            } else {
                return "> " + criteria.trim();
            }
        } else if (criteria.indexOf("<") == 0) {
            criteria = criteria.replaceFirst("<", "");
            //-- removed by thensw: no need % if it is a greater smaller condition.
//            if (wildSearch.equals("Y")) {
//                criteria = criteria.trim() + "%";
//            }
            if (isDate) {
                if(CommonFunction.verifyDate(criteria).equals("dd/MM/yyyy")){
                    criteria = ddMMyyyy_to_yyyyMMdd(criteria);
                }
            }
            if (isStrCriteria) {
                if (!globalIsDate) {
                    criteria = addSingleQuote(criteria);
                }
            }
			criteria = StringEscapeUtils.escapeHtml4(criteria);
            if (populateCriteria) {
                if (isStrCriteria || isDate) {
                    criteriaMap.put("C"+criteriaMap.size(), criteria.trim());
                } else {
                    criteriaMap.put("C"+criteriaMap.size(), new Integer(criteria.trim()));
                }
//                criteriaMap.put("C"+criteriaMap.size(), criteria.trim());
                return "< :C" + (criteriaMap.size()-1) + " " + addCollate() + " ";
            } else {
                return "< " + criteria.trim();
            }
        }
        
        return "";
    }

    private String betweenCriteria(String criteria, boolean isStrCriteria, boolean isDate) {
        int count = 0;
        String temp = "";
        String rtnCriteria = "invalid between condition";
        criteria = criteria.replaceFirst("<>", "");
        StringTokenizer st = new StringTokenizer(criteria, ":");
        while (st.hasMoreTokens()) {
            count++;
            temp = st.nextToken().trim();
            if (isDate) {
                temp = ddMMyyyy_to_yyyyMMdd(temp);
            }
            if (isStrCriteria) {
                temp = addSingleQuote(temp);
            }

            if (count == 1) {
                if (populateCriteria) {
                    criteriaMap.put("C"+criteriaMap.size(), temp);
                    rtnCriteria = " :C" + (criteriaMap.size()-1) + " " + addCollate() + " ";
                } else {
                    rtnCriteria = temp;
                }
                /*if (isDate){
                criteria = ddMMyyyy_to_yyyyMMdd(criteria);
                }
                if (isStrCriteria){
                criteria = addSingleQuote(criteria);
                }*/
            } else if (count == 2) {
                /*if (isStrCriteria){
                rtnCriteria = " between " +criteria+ " and " + addSingleQuote(temp);
                } else {
                rtnCriteria = " between " +criteria+ " and " + temp;
                }*/
                if (populateCriteria) {
                    criteriaMap.put("C"+criteriaMap.size(), temp);
                    rtnCriteria = " between :C" + (criteriaMap.size()-2) + " " + addCollate() + " and :C" + (criteriaMap.size()-1) + " " + addCollate() + " ";
                } else {
                    rtnCriteria = " between " + criteria + " and " + temp;
                }
                break;
            }
        }
        return rtnCriteria;
    }

    private String addSingleQuote(String criteria) {
        if (populateCriteria) {
            return criteria.trim();
        } 
        return "'" + criteria.trim() + "'";
    }

    private String ddMMyyyy_to_yyyyMMdd(String criteria) {
        criteria = criteria.trim();
        String tempSeperator = "/";
        int slashPos = criteria.indexOf(tempSeperator);
        if (slashPos > 0) {
            criteria = criteria.replaceAll("//", "-");
        }
        slashPos = criteria.indexOf("-");
        if (slashPos == 4) {
            return criteria;
        }
        tempSeperator = "-";
        String yyyyMMdd = "-" + criteria.substring(0, slashPos);
        yyyyMMdd = "-" + criteria.substring(slashPos + 1, criteria.indexOf(tempSeperator, slashPos + 1)) + yyyyMMdd;
        slashPos = criteria.indexOf(tempSeperator, slashPos + 1);
        yyyyMMdd = criteria.substring(slashPos + 1) + yyyyMMdd;

        return yyyyMMdd;
    }

    public void setWildSearch(String wildSearch) {
        this.wildSearch = wildSearch;
    }

    public String replaceCriteriaComma(String theCriteria) {
        return theCriteria.replace("_comma_", ",");
    }
    /*public void main(String[] arg) {
    //System.out.println(ddMMyyyy_to_yyyyMMdd("05/16/2009"));
    System.out.println(CriteriaConverter.dateCriteria("and", "strColumnName", "'--delete from abc"));
    System.out.println(CriteriaConverter.strCriteria("and", "strColumnName", "5, 750%", "N"));
    //System.out.println(CriteriaConverter.numericCriteria("and", "strColumnName", ">=25, <=30, 40, 45"));
    //		System.out.println(CriteriaConverter.numericCriteria("", "numericColumnName", ">25, <40", "%"));
    }*/
    
    public String addCollate() {
//        if (dbType == null) dbType = "mysql";
//        if (dbType.equals("mysql")) {
//            return "COLLATE utf8_unicode_ci";
//        }
        return"";
    }
    
    public void setCriteriaValue(org.hibernate.query.NativeQuery query) {
        if (populateCriteria) {
            for (String key : criteriaMap.keySet()) {
                if (criteriaMap.get(key)!=null && criteriaMap.get(key).getClass().isArray()) {
                    query.setParameterList(key, (Object[])criteriaMap.get(key));
                } else {
                    query.setParameter(key, criteriaMap.get(key));
                }
            }
        }
    }
}

	