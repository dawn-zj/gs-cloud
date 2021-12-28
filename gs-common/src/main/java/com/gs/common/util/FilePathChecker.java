package com.gs.common.util;

import java.util.ArrayList;
import java.util.List;

import com.gs.common.define.Constants;
import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.resource.ErrCode;

/**
 * 文件路径校验工具
 *
 */
public class FilePathChecker {
	private static List<String> list = new ArrayList<String>();

	static {
		list.add(Constants.PHOTO_SUFFIX);
		list.add(Constants.PDF_SUFFIX);
		list.add(Constants.ZIP_SUFFIX);
		list.add(Constants.LOG_SUFFIX);
		list.add(Constants.TXT_SUFFIX);
		list.add(Constants.CONF_SUFFIX);
		list.add(Constants.CER_SUFFIX);
		list.add(Constants.PFX_SUFFIX);

		// 日志文件后缀
		for (int i = 0; i < 10; i++)
			list.add(String.valueOf(i));
	}

	public static void checkFileSuffix(String filePath) {
		// 校验文件名后缀
		boolean checkSuffix = false;
		List<String> suffixList = FilePathChecker.list;
		for (int i = 0; i < suffixList.size(); i++) {
			if (filePath.endsWith(suffixList.get(i))) {
				checkSuffix = true;
				break;
			}
		}

		if (!checkSuffix)
			throw new NetGSRuntimeException("file suffix is invalid, file path is " + filePath);
	}
}
