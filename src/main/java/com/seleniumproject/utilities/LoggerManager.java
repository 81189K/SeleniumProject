package com.seleniumproject.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoggerManager {
	
	/***
	 * returns a logger instance for provided class
	 */
	public static Logger getLogger(Class<?> className) {
		return LogManager.getRootLogger();
	}
}
