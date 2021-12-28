package com.gs.common.util.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import com.gs.common.util.StringUtil;

public class MD5Util {

	/**
	 * md5加密：对数据进行MD5摘要运算
	 * @param b
	 * @return 摘要结果的十六进制字符串(小写)
	 */
	public static String md5Sign(byte[] b) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(b);
			// 摘要：16字节
			byte[] hash = md.digest();
			// byte转string:即byte——>int——>string
			StringBuffer outStrBuf = new StringBuffer(32);
			for (int i = 0; i < hash.length; i++) {
				int v = hash[i] & 0xFF;//和十六进制0xFF与运算，保持计算机二进制补码的一致性
				//将int转成十六进制的字符,若int<16，则先补0
				if (v < 16) {
					outStrBuf.append('0');
				}
				outStrBuf.append(Integer.toString(v, 16).toLowerCase());
			}
			return outStrBuf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new String(b);
		}
	}


	public static boolean md5VerifySign(byte[] plain, String sign) {
		String mySign = md5Sign(plain);
		if(mySign.equals(sign.toLowerCase())) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		String str = "son";
		//签名
		String sign = md5Sign(str.getBytes());
		//验签
		boolean isSigned = md5VerifySign(str.getBytes(), sign);
		System.out.println(isSigned);
	}
}
