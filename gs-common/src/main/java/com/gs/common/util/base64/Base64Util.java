package com.gs.common.util.base64;

import com.gs.common.util.StringUtil;
import com.gs.common.util.crypto.RSAUtil;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Base64Util {
	public static void main(String[] args) {
		//编码
//		String str1 = "son";
//		byte[] b1 = encode(str1);
//		System.out.println(new String(b1));

		//解码
		String str2 = "c29u";
		byte[] b2 = decode(str2);
		System.out.println(new String(b2));
	}

	/**
	 * 将指定字符串进行base64编码
	 *
	 * @param str
	 *            字符串
	 * @return 已被编码的byte[]
	 */
	public static String encode(String str) {
		Encoder enc = Base64.getEncoder();
		return StringUtil.getString(enc.encode(str.getBytes()));
	}

	/**
	 * 将指定字节数组进行base64编码
	 *
	 * @param data
	 *            字节数组
	 * @return 已被编码的byte[]
	 */
	// public static byte[] encode(byte[] data) {
	// 	Encoder enc = Base64.getEncoder();
	// 	return enc.encode(data);
	// }

	public static String encode(byte[] data) {
		Encoder enc = Base64.getEncoder();
		return StringUtil.getString(enc.encode(data));
	}

	/**
	 * 将被编码的字符串进行base64解码
	 *
	 * @param str
	 *            字符串
	 * @return 已解码的byte[]
	 */
	public static byte[] decode(String str) {
		str = RSAUtil.clearPemPubKey(str);
		str = RSAUtil.clearPemPriKey(str);
		Decoder dec = Base64.getMimeDecoder();
		return dec.decode(str.getBytes());
	}

	/**
	 * 将被编码的字节数组进行base64解码
	 *
	 * @param data
	 *            被编码的字节数组
	 * @return 已解码的byte[]
	 */
	public static byte[] decode(byte[] data) {
		Decoder dec = Base64.getDecoder();
		return dec.decode(data);
	}
}
