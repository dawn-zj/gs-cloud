package com.gs.common.util.logger;

import org.apache.log4j.Logger;

public abstract class AbstractLogger {
	/**
	 * log4j对象
	 */
	protected Logger logger = null;

	// private static String logFilterPattern=ConfigUtil.getInstance().getLogFilterPattern();
	/**
	 * 
	 */
	public AbstractLogger() {
		super();
	}

	public abstract void Log(String msg);

}
