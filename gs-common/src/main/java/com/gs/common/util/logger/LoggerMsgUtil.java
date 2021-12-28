package com.gs.common.util.logger;

import java.util.Hashtable;
import com.gs.common.define.Constants;

/**
 * 将日志类型转为中文描述
 */
public class LoggerMsgUtil {
	// 存储中文描述
	private static Hashtable<String, String> container = new Hashtable<String, String>();

	static {
		container.put(Constants.LOG_OPTYPE_SYSUSER_LOGIN, "登录");
	}

	public static String getLoggerMsg(String logOptype) {
		String ManageLoggerMsg = container.get(logOptype);
		if (ManageLoggerMsg == null) {
			return "没有对应的操作描述";
		} else {
			return ManageLoggerMsg;
		}
	}
}
