package com.gs.common.util.logger;

import com.gs.common.define.Constants;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LoggerFileUtil {

	private static String path = Constants.LOG_PATH;

	public static File[] logFile(final String logType) {
		File fileDir = new File(path);

		File[] fs = fileDir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.getName().startsWith(logType);
			}
		});

		if(fs!=null){
			List<File> list = Arrays.asList(fs);
			Collections.sort(list, new Comparator<File>() {
				public int compare(File file, File newFile) {
					if (file.lastModified() < newFile.lastModified()) {
						return 1;
					} else if (file.lastModified() == newFile.lastModified()) {
						return 0;
					} else {
						return -1;
					}

				}
			});
			fs=list.toArray(new File[list.size()]);
		}else{
			fs = new File[0];
		}
		return fs;
	}
}
