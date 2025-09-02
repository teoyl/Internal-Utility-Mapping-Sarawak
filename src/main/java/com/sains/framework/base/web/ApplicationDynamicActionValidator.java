package com.sains.framework.base.web;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.sains.common.util.Validator;
import com.sains.common.util.DateUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.opensymphony.xwork2.ActionSupport;

import com.sains.framework.sam.dao.ModuleDAO;
import com.sains.framework.sam.dao.ModuleDAOImpl;
import com.sains.framework.lookup.DataRetriever;
import com.sains.framework.model.Module;
import com.sains.framework.base.CommonFunction;


public class ApplicationDynamicActionValidator extends DynamicActionValidator{
	private final String SUCCESS = "success";
	private final String INSERT = "insert";
	private final String UPDATE = "update";
	private final String DELETE = "delete";
	private String validationType = "";
	//private ActionSupport dynamicActionSupport;
	public void commonValidation(){
		//do verification for required fields
	}
	
	public String validateInsert(){
		validationType = INSERT;
		
		commonValidation();
		return SUCCESS;
	}
	
	public String validateUpdate(){
		validationType = UPDATE;
		
		commonValidation();
		return SUCCESS;
	}
	
	public String validateDelete(){
		validationType = DELETE;
		
		
		return SUCCESS;
	}
}
