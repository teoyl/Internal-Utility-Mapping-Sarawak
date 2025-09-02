package com.sains.common.util;


/**
 * Holds the constant variables used by the user interface.
 *
 * @version $Revision: 14271 $
 * @author  $Author: Then Sze Wee $
 */
public interface IConstants {

	/*
	 * Global
	 */
	public static final String USER_CONTAINER_KEY = "USERCONTAINER";
	public static final String CURRENT_YEAR_KEY = "YEAR";
	public static final String CURRENT_DATE_KEY = "DATE";
	public static final int PASSWORD_EXPIRY_DAYS = 30;
	public static final String USERNAME = "USERNAME";
	public static final String COMM_ACCESS_LOG_ID = "ACCESSLOGID";

	/**
	 * Paging Constants
	 */
	public static final class Paging {
		//public static final String DEFAULT_PAGE_SIZE = "paging.default.pageSize";
		public static final String DEFAULT_PAGE_SIZE = "10";
		public static final String PAGE_SIZE_OPTION = "paging.pageSizeOptions";
	    public static final String NUMBER_OF_PAGE_SHOWN = "paging.noOfPageShown";
	    public static final String PAGE_NUMBER = "pageNo";
	}

	/**
	 * Query name for lookup.
	 *
	 */
	public static final class Lookup {
		/* Configuration */
		public static final String PAGE_SIZE = "pageSize";
		public static final String IN_DELIMITER = "#@";

		/* Common */
        public static final String EXCHANGE_RATE = "exchangeRate";

	}
	
}
