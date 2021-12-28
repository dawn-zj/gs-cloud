package com.gs.common.util.logger;

import com.gs.common.define.Constants;
import com.gs.common.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

public class LoggerConfig {
	/**
	 * 日志路径
	 */
	private static String logPath = Constants.LOG_PATH;

	/**
	 * 写入日志文件配置
	 */
	private static List<String> toFileList;

	/**
	 * 写入数据库配置
	 */
	private static List<String> toDBList;

	/**
	 * 文件大小
	 */
	private static int fileSize;

	/**
	 * 文件数量
	 */
	private static int fileNumber;


	public static String getLogPath() {
		return logPath;
	}

	/**
	 * 初始化日志参数，系统启动时初始化一次即可
	 */
	public synchronized static void init() {
		toFileList = new ArrayList<String>();
		toDBList = new ArrayList<String>();

		ConfigUtil config = ConfigUtil.getInstance();
		String toFile = config.getLogToFile();
		String toDB = config.getLogToDB();

		String[] toFiles = toFile.split(",");
		for (String string : toFiles)
			toFileList.add(string);

		String[] toDBs = toDB.split(",");
		for (String string : toDBs)
			toDBList.add(string);


		fileSize = Integer.parseInt(ConfigUtil.getInstance().getLogFileSize());
		fileNumber = Integer.parseInt(ConfigUtil.getInstance().getLogFileNum());

	}

	/**
	 * 日志文件配置是否包含指定日志类型
	 *
	 * @param logMode 日志类型：错误日志、调试日志等等
	 * @return boolean
	 */
	public synchronized static boolean fileIsContains(String logMode) {
		return toFileList.contains(logMode);
	}

}
