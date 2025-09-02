package com.sains.common.util;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;


public class SubGroup {
	Session session;
	
//	public String getSubGroup(String tableName, String parentName, String subName, String retrieveCode, String code){
//    	String returnStr = "";
//    	String subCcgCode = "";
//    	String sqlQuery =
//    		"select "+ retrieveCode +" from " + tableName + " where active = 1 and "+ subName +" in " +
//    		"(select "+ parentName +" from " + tableName + " where active = 1 and " + retrieveCode + " = ?)";
//    	try {
//	    	java.sql.PreparedStatement stmt = session.connection().prepareStatement(sqlQuery);
//	    	stmt.setString(1, code);
//			java.sql.ResultSet rs = stmt.executeQuery();
//			if (rs!= null){
//				while (rs.next()){
//					subCcgCode = rs.getString(retrieveCode);
//					returnStr = returnStr + ",'"+ subCcgCode +"'";
//					returnStr += getSubGroup(tableName, parentName, subName, retrieveCode, subCcgCode);
//				}
//			}
//    	} catch (Exception e) {
//    		return "";
//    	}
//
//    	return returnStr;
//    }
//
//	public List<String> getSubGroupInList(String tableName, String parentName, String subName, String retrieveCode, String code){
//		List<String> rtnList = new ArrayList<String>();
//    	//String returnStr = "";
//    	String subCcgCode = "";
//    	String sqlQuery =
//    		"select "+ retrieveCode +" from " + tableName + " where active = 1 and "+ subName +" in " +
//    		"(select "+ parentName +" from " + tableName + " where active = 1 and " + retrieveCode + " = ?)";
//    	try {
//	    	java.sql.PreparedStatement stmt = session.connection().prepareStatement(sqlQuery);
//	    	stmt.setString(1, code);
//			java.sql.ResultSet rs = stmt.executeQuery();
//			if (rs!= null){
//				while (rs.next()){
//					subCcgCode = rs.getString(retrieveCode);
//					rtnList.add(subCcgCode);
//					rtnList.addAll(getSubGroupInList(tableName, parentName, subName, retrieveCode, subCcgCode));
//				}
//			}
//    	} catch (Exception e) {
//    		return null;
//    	}
//
//    	return rtnList;
//    }
}
