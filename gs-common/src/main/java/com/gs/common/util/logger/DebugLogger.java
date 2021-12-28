package com.gs.common.util.logger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class DebugLogger extends AbstractLogger {

	public DebugLogger(LoggerRes res) throws Exception {
		createDebuglogger(res);
	}

	@Override
	public void Log(String msg) {
		logger.info(msg);
	}

	private void createDebuglogger(LoggerRes loggerRes) throws Exception {

		logger = Logger.getLogger(loggerRes.getLoggerName());
		logger.removeAllAppenders();

		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss S} [%t] - %m%n");

		RollingFileAppender da = null;
		try {
			da = new RollingFileAppender(layout, loggerRes.getLogFile());
			da.setMaxBackupIndex(loggerRes.getMaxBackupIndex());
			da.setMaxFileSize(loggerRes.getMaxFileSize());
			logger.addAppender(da);
			logger.setLevel(Level.INFO);
		} catch (Exception e) {
			throw e;
		}
	}
}
