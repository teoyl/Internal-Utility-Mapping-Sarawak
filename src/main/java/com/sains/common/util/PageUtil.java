package com.sains.common.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class PageUtil {
	public static List getPagingItem(Integer pageNo, Integer pageSize, Integer numberOfRows){
		List pageList = new ArrayList();
		int totalPage = DoubleUtil.roundUp(numberOfRows.doubleValue() / pageSize);
		int startPage = 1;
		
		if (new Double(pageNo) % pageSize == 0){
			startPage = (((pageNo / pageSize)-1) * pageSize) + 1;
		} else {
			startPage = ((pageNo / pageSize) * pageSize) + 1;
		}
		if (startPage <= 0) startPage = 1;
		
		if (startPage == totalPage) return pageList;
		
		for (int pageIdx = 0; pageIdx < 10; pageIdx++){
			if (startPage > totalPage){
				break;
			}
			pageList.add(startPage++);
		}
		
		return pageList;
	}
	
	public static Map getPagingItem(Integer pageNo, Integer pageSize, Integer numberOfRows, int intPageList){
       
		//for testing:
		//numberOfRows = 543;
		//pageNo = 10;
		Map paramMap = new HashMap();
		List pageList = new ArrayList();
		int totalPage = numberOfRows / pageSize;
		if (numberOfRows % pageSize > 0)  totalPage ++ ;
		int startPage = 1;
		int endPage = 0;



		int appendPage = intPageList/2;
		startPage = pageNo - appendPage;
		if (startPage < 1) startPage=1;
		if (startPage + intPageList - 1 > totalPage) {
			endPage = totalPage;
			startPage = endPage - intPageList + 1;
		} else {
			endPage = startPage + intPageList - 1;
		}
		
		if (startPage < 1) startPage = 1;
			
		if (startPage == endPage) return paramMap;
		
		for (int pageIdx = startPage; pageIdx <= endPage; pageIdx++){
			if (pageIdx > totalPage){
				break;
			}
			pageList.add(pageIdx);
		}
		
		if (pageNo > 1) {
			paramMap.put("First", "1");
			paramMap.put("Previous", "" + (pageNo - 1));
		}
		if (pageNo < endPage) {
			paramMap.put("Next", "" + (pageNo + 1));
			paramMap.put("Last", "" + totalPage);
		}
		paramMap.put("items", pageList);
		
		return paramMap;
	}
}
