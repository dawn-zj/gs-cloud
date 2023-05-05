package com.gs.common.define;

public class Constants {

	public static final String VERISON = "version1.0"; // 系统版本号统一出处
	public static final String UTF_8 = "UTF-8"; // 系统编码
	public static final String SPLIT_1 = ","; // 分割符1
	public static final String SPLIT_2 = ";"; // 分割符2
	public static final String SPLIT_DIR = "/"; // 目录分割符
	public static final String SPOT = ".";
	public static final String DEFAULT_STRING = "";

	// 密钥
	public static final String PFX_SUFFIX = ".pfx"; // 密钥后缀
	public static final String JKS_SUFFIX = ".jks"; // 密钥后缀
	// 文件
	public static final String PHOTO_SUFFIX = ".png"; // 图片后缀
	public static final String PDF_SUFFIX = ".pdf"; // pdf后缀
	public static final String ZIP_SUFFIX = ".zip"; // 压缩文件后缀
	public static final String LOG_SUFFIX = ".log"; // log文件后缀
	public static final String TXT_SUFFIX = ".txt"; // txt文件后缀
	public static final String CONF_SUFFIX = ".properties"; // properties文件后缀
	public static final String CER_SUFFIX = ".cer"; // cert文件后缀
	public static final String P7B_SUFFIX = ".p7b"; // cert文件后缀

	// 项目资源
	public static final String CONTEXT_PATH = "E:/Idea/GS/gs-cloud";// 项目目录相对路径
	public static final String FILE_PATH = CONTEXT_PATH + "/file/";// 项目内测文件相对路径
	public static final String FILE_OUT_PATH = CONTEXT_PATH + "/file/out/";// 项目内测输出文件相对路径

	public static final String ROOT_PATH = "/opt/zj/gs/";
	public static final String PHOTO_PATH = ROOT_PATH + "photo/";
	public static final String CONF_PATH = ROOT_PATH + "conf/";
	public static final String LOG_PATH = ROOT_PATH + "log/";
	public static final String TMP_PATH = ROOT_PATH + "tmp/";
	public static final String PDF_PATH = ROOT_PATH + "pdf/";
	public static final String PDF_TEMPLATE_PATH = ROOT_PATH + "pdfTemplate/";

	public static final String NATIVELIB_PATH = "appserver/nativelib"; //libsigar-amd64-linux.so

	public static final int RSA_KEY_SIZE_1024 = 1024;

	public static final long LENGTH_5MB = 5 * 1024 * 1024L; // 5MB

	// 图片分辨率，分辨率越高，图片越小越清晰
	public static final int DPI = 96;
	public static final int PHOTO_UNIT_CM = 1; // 图片单位厘米
	public static final int PHOTO_UNIT_PX = 2; // 图片单位像素

	// 日志类型
	public static final String LOG_OPTYPE_SYSUSER_LOGIN = "LOG_OPTYPE_SYSUSER_LOGIN";
	// 邮件
	public static final String Email_Account = "867096367@qq.com";//3385528945
	public static final String Email_Authz_Password = "wxpqpjeklcsvbgag";//zj19970818 授权码

	// 算法标识
	public static final String EC = "EC";
	public static final String SM2 = "SM2";
	public static final String SM3 = "SM3";
	public static final String SM4 = "SM4";
	public static final String SM3_SM2 = "SM3WITHSM2";
	public static final String RSA = "RSA";
	public static final String SHA1 = "SHA1";
	public static final String SHA256 = "SHA256";
	public static final String SHA512 = "SHA512";
	public static final String SHA1_RSA = "SHA1WITHRSA";
	public static final String SHA256_RSA = "SHA256WITHRSA";

}
