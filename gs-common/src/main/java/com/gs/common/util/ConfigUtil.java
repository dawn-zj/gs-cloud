package com.gs.common.util;

import com.gs.common.define.Constants;
import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.resource.ErrCode;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 配置类
 */
public class ConfigUtil {
	/**
	 * 实例
	 */
	private static ConfigUtil instance;
	private Configuration properties;
	private FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
	private String confPath = Constants.CONF_PATH + "config.properties";

	/**
	 * 获取ConfigUtil实例
	 *
	 * @return
	 */
	public static synchronized ConfigUtil getInstance() {
		if (instance == null) {
			ConfigUtil config = new ConfigUtil();
			try {
				config.load();
			} catch (Exception e) {
				e.printStackTrace();
				throw new NetGSRuntimeException(ErrCode.LOAD_CONF_ERROR, e.getMessage());
			}
			instance = config;
		}
		return instance;
	}

	/**
	 * 加载配置文件
	 */
	private synchronized void load() {
		try {
			// 读取属性文件 .properties
			Parameters params = new Parameters();
			builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class);
//			builder.configure(params.properties().setFileName(confPath).setListDelimiterHandler(new DefaultListDelimiterHandler(',')));
			builder.configure(params.properties().setFileName(confPath));
			properties = builder.getConfiguration();
		} catch (Exception e) {
			throw new NetGSRuntimeException(ErrCode.LOAD_CONF_ERROR, e.getMessage());
		}
	}

	/**
	 * 重新加载配置文件
	 */
	public synchronized void reload() {
		try {
			instance.load();
		} catch (Exception e) {
			throw new NetGSRuntimeException(ErrCode.RELOAD_CONF_ERROR, e.getMessage());
		}
	}

	/**
	 * 超时时间
	 *
	 * @return
	 */
	public Long getTimeoutTime() {
		return properties.getLong("timeout.time", 1800000L);
	}

	/**
	 * 日志文件配置
	 *
	 * @return
	 */
	public String getLogToFile() {
		List<Object> list = properties.getList("log.toFile");
		String logToFile = StringUtils.join(list.toArray(), Constants.SPLIT_1);
		return logToFile;
	}

	/**
	 * 日志数据库配置
	 *
	 * @return
	 */
	public String getLogToDB() {
		List<Object> list = properties.getList("log.toDB");
		String logToDB = StringUtils.join(list.toArray(), Constants.SPLIT_1);
		return logToDB;
	}

	/**
	 * 日志文件大小
	 *
	 * @return
	 */
	public String getLogFileSize() {
		return properties.getString("log.file_size", Constants.DEFAULT_STRING);
	}

	/**
	 * 日志文件个数
	 *
	 * @return
	 */
	public String getLogFileNum() {
		return properties.getString("log.file_num", Constants.DEFAULT_STRING);
	}

	public String getScheduleTask() {
		return properties.getString("schedule.task", Constants.DEFAULT_STRING);
	}

	public static void main(String[] args) {
		Long time = ConfigUtil.getInstance().getTimeoutTime();
		System.out.println(time);

		String task = ConfigUtil.getInstance().getScheduleTask();
		System.out.println(task);
	}
}
