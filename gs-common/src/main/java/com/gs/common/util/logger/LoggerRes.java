package com.gs.common.util.logger;

public class LoggerRes {
	private String LogFile = "log/acces.log";
	private String LoggerName = null;

	private int MaxBackupIndex = 10;

	private String MaxFileSize = "10MB";


	public String getLogFile() {
		return LogFile;
	}

	public void setLogFile(String logFile) {
		LogFile = logFile;
	}

	public String getLoggerName() {
		return LoggerName;
	}

	public void setLoggerName(String loggerName) {
		LoggerName = loggerName;
	}

	public int getMaxBackupIndex() {
		return MaxBackupIndex;
	}

	public void setMaxBackupIndex(int maxBackupIndex) {
		MaxBackupIndex = maxBackupIndex;
	}

	public String getMaxFileSize() {
		return MaxFileSize;
	}

	public void setMaxFileSize(String maxFileSize) {
		MaxFileSize = maxFileSize;
	}
}
