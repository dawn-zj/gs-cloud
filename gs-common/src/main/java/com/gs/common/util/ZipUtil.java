package com.gs.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.gs.common.define.Constants;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class ZipUtil {

	public static void main(String[] args) {
		String zipPath = Constants.FILE_OUT_PATH + "file.zip";
		String unzipPath = Constants.FILE_OUT_PATH + "file_unzip";
		String filePath = Constants.FILE_PATH;
		zipDir(filePath, zipPath, "");
		unZip(zipPath, unzipPath, "");
		System.out.println("ok");
	}

	/**
	 * 压缩文件
	 *
	 * @param filePath 文件路径
	 * @param zipPath 压缩后存放路径
	 * @param pwd 压缩密码
	 * @throws Exception
	 */
	public static void zipFile(String filePath, String zipPath, String pwd) {
		try {
			ZipFile zipFile = new ZipFile(zipPath);
			ArrayList<File> fileList = new ArrayList<File>();
			setFileToList(filePath, fileList);

			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

			if (StringUtil.isNotBlank(pwd)) {
				parameters.setEncryptFiles(true);
				parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
				parameters.setPassword(pwd);
			}
			zipFile.addFiles(fileList, parameters);
		} catch (ZipException e) {
		}
	}

	/**
	 * 压缩文件夹
	 *
	 * @param dirPath 文件夹路径
	 * @param zipPath 压缩后存放路径
	 * @param pwd 压缩密码
	 */
	public static void zipDir(String dirPath, String zipPath, String pwd) {
		try {
			ZipFile zipFile = new ZipFile(zipPath);

			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

			if (StringUtil.isNotBlank(pwd)) {
				parameters.setEncryptFiles(true);
				parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
				parameters.setPassword(pwd);
			}
			File df = new File(dirPath);
			zipFile.addFolder(df, parameters);
		} catch (ZipException e) {
		}
	}

	/**
	 * 递归
	 *
	 * @param filePath
	 * @param fileList
	 */
	private static void setFileToList(String filePath, ArrayList<File> fileList) {
		File file = new File(filePath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files)
				setFileToList(f.getPath(), fileList);
		} else
			fileList.add(file);
	}

	/**
	 * 解压
	 *
	 * @param zipPath 压缩文件路径
	 * @param filePath 解压后存放路径
	 * @param pwd 解压密码
	 * @throws Exception
	 */
	public static void unZip(String zipPath, String filePath, String pwd) {
		try {
			ZipFile zipFile = new ZipFile(zipPath);
			zipFile.setFileNameCharset("UTF8");
			if (StringUtil.isBlank(pwd)) {
				zipFile.extractAll(filePath);
			} else {
				if (zipFile.isEncrypted())
					zipFile.setPassword(pwd);

				List<?> fileHeaderList = zipFile.getFileHeaders();
				for (Object fileHeader : fileHeaderList)
					zipFile.extractFile((FileHeader) fileHeader, filePath);
			}
		} catch (ZipException e) {
		}
	}
}
