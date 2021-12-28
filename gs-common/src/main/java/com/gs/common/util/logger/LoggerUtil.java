package com.gs.common.util.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LoggerUtil {
	private static DebugLogger debugLog;
	private static ErrorLogger errorLog;

	/**
	 * 记录调试日志
	 *
	 * @param
	 */
	private static Object objLockDebug = new Object();

	private static Object objLockError = new Object();


	public static void debugLog(String msg) {
		//开启debug
		if (true) {
			// 判断debugLog对象是否存在，不存在则创建
			synchronized (objLockDebug) {
				if (debugLog == null) {
					LoggerRes res = new LoggerRes();
					res.setLogFile(LoggerConfig.getLogPath() + "debug.log");
					res.setLoggerName("debug");

					try {
						debugLog = new DebugLogger(res);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
			if (debugLog != null) {
				debugLog.Log(msg);
			}
		}

	}

	public static void errorLog(String msg) {
		//开启错误日志
		if (true) {
			// 判断debugLog对象是否存在，不存在则创建
			synchronized (objLockError) {
				if (errorLog == null) {
					LoggerRes res = new LoggerRes();
					res.setLogFile(LoggerConfig.getLogPath() + "error.log");
					res.setLoggerName("error");

					try {
						errorLog = new ErrorLogger(res);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
			if (errorLog != null) {
				errorLog.Log(msg);
			}
		}

	}

	public static void errorLog(String msg, Throwable tr) {
		if (tr != null) {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			PrintStream out = new PrintStream(bao);
			tr.printStackTrace(out);
			errorLog(msg + "\r\n" + tr.getMessage() + "\r\n" + bao.toString());
		} else {
			errorLog(msg);
		}
	}
}
