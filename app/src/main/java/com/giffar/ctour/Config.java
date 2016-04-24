/**
 * Configuration For Application
 * @author egiadtya
 * 27 October 2014
 **/
package com.giffar.ctour;

import android.os.Environment;


public class Config {

	/**
	 * Set Application Name
	 * 
	 * @return String
	 **/
	public static String APP_NAME = "ZOUK";

	/**
	 * Set folder name for store cache data
	 * 
	 * @return String
	 **/
	public static String CACHE_FOLDER = APP_NAME + "_data";

	/**
	 * Set preference name on application
	 * 
	 * @return String
	 **/
	public static String PREFERENCE_NAME = APP_NAME + "_preference";

	/**
	 * Set database name if application use SQL Lite Database
	 * 
	 * @return String
	 **/
	public static String DATABASE_NAME = APP_NAME + "_DB.sqlite";

	/**
	 * Set Api key if webservice need authentication with key
	 * 
	 * @return String
	 **/
	public static String API_KEY = "";

	/**
	 * Set URL server name if application access webservice
	 * 
	 * @return String
	 **/
	public static String SERVER_ADDRESS;
	public static String HTTP = "http://";
	private static String DEVELOPMENT_URL ="";
	private static String PRODUCTION_URL = "";


	public static boolean isDevelopment;

	public static String getURL() {
		if (isDevelopment) {
			return HTTP + DEVELOPMENT_URL;
		} else {
			return HTTP + PRODUCTION_URL;
		}
	}
	
	public static String getAPIUrl(){
		return getURL() + "api";
	}
	
	public enum MODE {
		DEVELOPMENT, PRODUCTION
	}

	public static void setMode(MODE mode) {
		switch (mode) {
		case DEVELOPMENT:
			isDevelopment = true;
			break;
		case PRODUCTION:
			isDevelopment = false;
			break;
		}
	}
	
	public static final String FILES_PATH = Environment.getExternalStorageDirectory() + "/Android/data/com.ebizu.zouk/Files";
	
}
