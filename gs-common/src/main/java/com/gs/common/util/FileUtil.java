package com.gs.common.util;

import com.gs.common.define.Constants;
import com.gs.common.exception.NetGSRuntimeException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileUtil {

	/**
	 * 获取当前系统相对路径
	 * @return
	 */
	public static String getContextPath() {
		String filePath = System.getProperty("user.dir");
		return filePath;
	}

	/**
	 * 校验指定文件路径的文件是否存在
	 *
	 * @param filePath
	 *            文件路径
	 * @return boolean
	 */
	public static boolean checkPath(String filePath) {
		if (StringUtil.isBlank(filePath)) {
			throw new NetGSRuntimeException("path invaild, path is " + filePath);
		}
		File file = new File(filePath);
		// if (file.isFile()) {
		// 	FilePathChecker.checkFileSuffix(filePath);
		// }
		return file.exists();
	}

	/**
	 * 根据文件路径创建绝对路径（目标文件夹）
	 *
	 * @param filePath
	 *            文件路径
	 * @throws Exception
	 */
	public static void createDir(String filePath) {
		String dirPath = filePath.substring(0, filePath.lastIndexOf(Constants.SPLIT_DIR));

		File file = new File(dirPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 删除文件
	 *
	 * @param filePath
	 *            文件路径
	 * @throws Exception
	 */
	public static void deleteFile(String filePath) {
		checkPath(filePath, false);

		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 删除目录
	 *
	 * @param dirPath
	 *            目录路径
	 * @throws Exception
	 */
	public static void deleteDir(String dirPath) {
		checkPath(dirPath, false);

		File file = new File(dirPath);
		File[] fs = file.listFiles();
		if (fs == null) {
			return;
		}
		for (File f : fs) {
			if (f.isFile()) {
				deleteFile(f.getPath());
			}
			if (f.isDirectory()) {
				deleteDir(f.getPath());
			}
		}

		deleteFile(dirPath);
	}

	/**
	 * 删除空目录
	 *
	 * @param dirPath
	 *            目录路径
	 * @throws Exception
	 */
	public static void deleteEmptyDir(String dirPath) {
		checkPath(dirPath, false);

		File file = new File(dirPath);
		File[] fs = file.listFiles();
		if (fs == null || fs.length == 0) {
			deleteFile(dirPath);
		} else {
			return;
		}
	}

	/**
	 * 删除目录
	 *
	 * @param dirPath
	 *            目录路径
	 * @param excludePath
	 *            排除目录或文件的路径
	 */
	public static void deleteDir(String dirPath, String excludePath) {
		checkPath(dirPath, false);

		File excludeFile = new File(excludePath);
		File dirFile = new File(dirPath);

		File[] fs = dirFile.listFiles();
		if (fs == null) {
			return;
		}
		for (File f : fs) {
			if (!f.getAbsolutePath().equals(excludeFile.getAbsolutePath())) {
				if (f.isDirectory()) {
					deleteDir(f.getPath(), excludePath);
				}
				f.delete();
			}
		}
	}

	/**
	 * 删除文件及所在目录
	 *
	 * @param filePath
	 *            文件路径
	 */
	public static void deleteFileAndDir(String filePath) {
		checkPath(filePath, false);

		File file = new File(filePath);
		deleteDir(file.getParent());
	}

	/**
	 * 列出目录下文件名称
	 *
	 * @param dirPath
	 *            目录路径
	 * @return String[] 返回目录下的文件名
	 * @throws Exception
	 */
	public static String[] listFileName(String dirPath) {
		checkPath(dirPath, true);

		File file = new File(dirPath);
		if (!file.isDirectory()) {
			throw new NetGSRuntimeException("dir path is not a dir, dir path is " + dirPath);
		}
		return file.list();
	}

	/**
	 * 获取文件数据，先校验文件路径
	 *
	 * @param filePath
	 *            文件路径
	 * @return byte[] 文件的字节数组
	 * @throws Exception
	 */
	public static byte[] getFile(String filePath) {
//		checkPath(filePath, true);

		File f = new File(filePath);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			byte[] bs = new byte[fis.available()];
			fis.read(bs);

			return bs;
		} catch (Exception e) {
			throw new NetGSRuntimeException(
					"get file data error, " + e.getMessage() + " file path is " + filePath);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 获取文件数据，不校验文件路径
	 *
	 * @param filePath
	 *            文件路径
	 * @return byte[] 文件的字节数组
	 * @throws Exception
	 */
	public static byte[] getFileNoCheck(String filePath) {
		File f = new File(filePath);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			byte[] bs = new byte[fis.available()];
			fis.read(bs);

			return bs;
		} catch (Exception e) {
			throw new NetGSRuntimeException("get file data error, " + e.getMessage() + " file path is " + filePath);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 文件中是否含有指定内容
	 *
	 * @param filePath
	 *            文件路径
	 * @return data 指定的内容
	 * @throws Exception
	 */
	public static boolean contain(String filePath, byte[] data) {
		if (!checkPath(filePath))
			return false;

		File f = new File(filePath);

		byte[] bs = new byte[20];
		int len = -1;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			while ((len = fis.read(bs)) != -1) {
				if (Arrays.equals(bs, data))
					return true;
			}
			return false;
		} catch (Exception e) {
			throw new NetGSRuntimeException(
					"get file data error, " + e.getMessage() + " file path is " + filePath);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 文件中是否含有指定内容(证书作废时间在签章时间之前返回true)
	 *
	 * @param filePath
	 *            文件路径
	 * @param data
	 *            指定内容
	 * @param time
	 *            签章时间
	 * @return
	 */
	public static boolean containInDate(String filePath, byte[] data, long time) {
		if (!checkPath(filePath))
			return false;

		int len = -1;
		byte[] allData = new byte[28];
		byte[] snData = new byte[20];
		byte[] timeData = new byte[8];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			while ((len = fis.read(allData)) != -1) {
				System.arraycopy(allData, 0, snData, 0, snData.length);
				if (Arrays.equals(snData, data)) {
					System.arraycopy(allData, snData.length, timeData, 0, timeData.length);
					if (HexUtil.byte2Long(timeData) <= time)
						return true;
				}
			}
			return false;
		} catch (Exception e) {
			throw new NetGSRuntimeException(
					"get file data error, " + e.getMessage() + " file path is " + filePath);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 获取文件后缀拓展名
	 *
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static String getFileSuffix(String fileName) {
		if (fileName == null) {
			return "";
		}
		int index = fileName.lastIndexOf(".");
		if (index == -1) {
			return "";
		}
		return fileName.substring(index);
	}

	/**
	 * 获取图片数据 (数据、宽、高)
	 *
	 * @param filePath
	 *            图片路径
	 * @return List
	 * @throws Exception
	 */
	public static List<Object> getImagePro(String filePath) {
		byte[] bs = getFile(filePath);

		List<Object> list = new ArrayList<Object>();
		BufferedImage sourceImg;
		try {
			sourceImg = ImageIO.read(new ByteArrayInputStream(bs));
			list.add(bs);
			list.add(sourceImg.getWidth());
			list.add(sourceImg.getHeight());
		} catch (Exception e) {
			throw new NetGSRuntimeException(
					"get image data error, " + e.getMessage() + " file path is " + filePath);
		}

		return list;
	}

	/**
	 * 获取图片数据 (数据、宽、高)
	 *
	 * @param bs
	 *            图片数据
	 * @return List
	 * @throws Exception
	 */
	public static List<Object> getImagePro(byte[] bs) {
		List<Object> list = new ArrayList<Object>();
		BufferedImage sourceImg;
		try {
			sourceImg = ImageIO.read(new ByteArrayInputStream(bs));
			list.add(bs);
			list.add(sourceImg.getWidth());
			list.add(sourceImg.getHeight());
		} catch (Exception e) {
			throw new NetGSRuntimeException("get image data error, " + e.getMessage());
		}

		return list;
	}

	/**
	 * 保存文件（覆盖）
	 *
	 * @param filePath
	 *            目标路径
	 * @param data
	 *            文件数据
	 * @throws Exception
	 */
	public static String storeFile(String filePath, byte[] data) {
		createDir(filePath);

		File f = new File(filePath);
		if (!f.exists())
			try {
				f.createNewFile();
			} catch (Exception e) {
				throw new NetGSRuntimeException(
						"create file error, " + e.getMessage() + " file path is " + filePath);
			}

		if (data != null) {
			FileOutputStream fous = null;
			try {
				fous = new FileOutputStream(f);
				fous.write(data);
				fous.flush();
			} catch (Exception e) {
				throw new NetGSRuntimeException(
						"store file error, " + e.getMessage() + " file path is " + filePath);
			} finally {
				try {
					if (fous != null)
						fous.close();
				} catch (Exception e) {
				}
			}
		}
		return filePath;
	}

	/**
	 * 保存文件（追加）
	 *
	 * @param filePath
	 *            目标路径
	 * @param data
	 *            文件数据
	 * @param append
	 *            boolean类型的是否追加
	 * @return String 文件路径
	 * @throws Exception
	 */
	public static String storeFile(String filePath, byte[] data, boolean append) {
		createDir(filePath);

		File f = new File(filePath);
		if (!f.exists())
			try {
				f.createNewFile();
			} catch (Exception e) {
				throw new NetGSRuntimeException(
						"create file error, " + e.getMessage() + " file path is " + filePath);
			}

		if (data != null) {
			FileOutputStream fous = null;
			try {
				fous = new FileOutputStream(f, append);
				fous.write(data);
				fous.flush();
			} catch (Exception e) {
				throw new NetGSRuntimeException(
						"store file error, " + e.getMessage() + " file path is " + filePath);
			} finally {
				try {
					if (fous != null)
						fous.close();
				} catch (Exception e) {
				}
			}
		}
		return filePath;
	}

	/**
	 * 产生图片
	 *
	 * @param filePath
	 *            图片存储目标路径
	 * @param content
	 *            图片内容
	 */
	public static void createImage(String filePath, String content) {
		File file = new File(filePath);
		try {
			Font font = new Font("Serif", Font.BOLD, 10);
			BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = (Graphics2D) bi.getGraphics();
			g2.setBackground(Color.WHITE);
			g2.clearRect(0, 0, 100, 100);
			g2.setPaint(Color.BLACK);

			FontRenderContext context = g2.getFontRenderContext();
			Rectangle2D bounds = font.getStringBounds(content, context);
			double x = (100 - bounds.getWidth()) / 2;
			double y = (100 - bounds.getHeight()) / 2;
			double ascent = -bounds.getY();
			double baseY = y + ascent;

			g2.drawString(content, (int) x, (int) baseY);
			ImageIO.write(bi, "jpg", file);
		} catch (Exception e) {
			throw new NetGSRuntimeException(
					"create image file error, " + e.getMessage() + " file path is " + filePath);
		}
	}

	/**
	 * 产生文件名
	 *
	 * @return
	 */
	public static String getFileName() {
		byte[] bs = new byte[20];
		SecureRandom sr = new SecureRandom();
		sr.nextBytes(bs);
		return HexUtil.byte2Hex(bs);
	}

	/**
	 * 获取文件总行数
	 *
	 * @param filePath
	 *            文件路径
	 * @return 行数
	 * @throws Exception
	 */
	public static int getFileLineNum(String filePath) {
		checkPath(filePath, true);

		File file = new File(filePath);
		long fileLength = file.length();
		int lineNo = 0;
		LineNumberReader lnr = null;
		try {
			lnr = new LineNumberReader(new FileReader(file));
			if (lnr != null) {
				lnr.skip(fileLength);
				lineNo = lnr.getLineNumber();
			}
		} catch (Exception e) {
			throw new NetGSRuntimeException(
					"get file line number error, " + e.getMessage() + " file path is " + filePath);
		} finally {
			try {
				if (lnr != null)
					lnr.close();
			} catch (IOException ee) {
			}
		}
		return lineNo;
	}

	/**
	 * 读取文件指定行
	 *
	 * @param filePath
	 *            文件路径
	 * @param begin
	 *            开始行
	 * @param end
	 *            结束行
	 * @return String
	 */
	public static String getSpecifyLine(String filePath, int begin, int end) {
		checkPath(filePath, true);

		// File file = new File(filePath);
		if (begin <= 0)
			begin = 0;

		LineNumberReader lnr = null;
		String lines = null;
		StringBuffer ret = new StringBuffer();
		try {
			lnr = new LineNumberReader(new FileReader(filePath));
			int lineNum = 1;
			lines = lnr.readLine();
			while (lines != null) {
				lineNum++;
				if (lineNum >= begin)
					ret.append(lines).append("<br>");

				if (lineNum > end)
					break;

				lines = lnr.readLine();
			}
		} catch (Exception e) {
			throw new NetGSRuntimeException(
					"get specify line number error, " + e.getMessage() + " file path is " + filePath);
		} finally {
			try {
				if (lnr != null)
					lnr.close();
			} catch (IOException e) {
			}
		}
		return ret.toString();
	}

	/**
	 * 复制目录
	 *
	 * @param srcDir
	 *            原目录路径
	 * @param desDir
	 *            目标目录路径
	 */
	public static void copyDir(String srcDir, String desDir) {
		checkPath(srcDir, true);
		checkPath(desDir, false);

		File srcFile = new File(srcDir);
		File desFile = new File(desDir);

		if (!desFile.exists())
			desFile.mkdirs();

		File[] fs = srcFile.listFiles();
		for (File f : fs) {
			if (f.isFile()) {
				byte[] bs = getFile(f.getPath());
				storeFile(desDir + Constants.SPLIT_DIR + f.getName(), bs);
			}
			if (f.isDirectory())
				copyDir(f.getPath(), desDir + Constants.SPLIT_DIR + f.getName());
		}
	}

	public static void copyDirNoCheck(String srcDir, String desDir) {

		File srcFile = new File(srcDir);
		File desFile = new File(desDir);

		if (!desFile.exists())
			desFile.mkdirs();

		File[] fs = srcFile.listFiles();
		for (File f : fs) {
			if (f.isFile()) {
				byte[] bs = getFileNoCheck(f.getPath());
				storeFile(desDir + Constants.SPLIT_DIR + f.getName(), bs);
			}
			if (f.isDirectory())
				copyDirNoCheck(f.getPath(), desDir + Constants.SPLIT_DIR + f.getName());
		}
	}

	/**
	 * 将信息记录文件中
	 *
	 * @param filePath
	 *            文件路径
	 * @param newLog
	 *            信息
	 */
	public static void appendLog(String filePath, String newLog) {
		File f = new File(filePath);
		Scanner sc = null;
		PrintWriter pw = null;
		try {
			if (!f.exists()) {
				File parentDir = new File(f.getParent());
				if (!parentDir.exists())// 如果�?在目录不存在,则新�?.
					parentDir.mkdirs();

				f.createNewFile();
			}

			sc = new Scanner(f);
			StringBuilder sb = new StringBuilder();
			// 先读出旧文件内容,并暂存sb�?;
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine());
				sb.append("\r\n");// 换行符作为间�?,扫描器读不出�?,因此要自己添�?.
			}
			sc.close();

			pw = new PrintWriter(new FileWriter(f), true);
			pw.println(sb.toString());// ,写入旧文件内�?.
			pw.println(newLog);// 写入新日�?.
			pw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 校验文件路径
	 *
	 * @param filePath
	 */
	private static void checkPath(String filePath, boolean checkExist) {
		if (StringUtil.isBlank(filePath))
			throw new NetGSRuntimeException("path invaild, path is " + filePath);

		File file = new File(filePath);
		if (file.isFile())
			FilePathChecker.checkFileSuffix(filePath);

		if (checkExist && !file.exists())
			throw new NetGSRuntimeException("path not exist, path is " + filePath);
	}

	public static void main(String[] args) {
		System.out.println(FileUtil.getContextPath());
	}

}
