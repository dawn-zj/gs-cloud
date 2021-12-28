package com.gs.common.util.pkcs;

import com.gs.common.define.Constants;
import com.gs.common.util.FileUtil;
import com.gs.common.util.HexUtil;
import com.gs.common.util.crypto.KeyUtil;
import com.gs.common.util.crypto.SM4Util;
import org.bouncycastle.asn1.*;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

/**
 * 数字信封工具类
 */
public class PKCS7EnvelopeUtil {
	/**
	 * 制作数字信封
	 *
	 * @param plain     原文
	 * @param publicKey 公钥
	 * @param symAlg    对称算法
	 */
	public static void makeP7(byte[] plain, PublicKey publicKey, String symAlg) throws Exception {
		// 使用对方公钥加密，保证只有对方能解
		// 1.生成对称密钥
		boolean isSM4 = false;
		byte[] symData = null;
		switch (symAlg) {
			case Constants.SM4:
				isSM4 = true;
				symData = KeyUtil.genRandom(16);

			default:
				isSM4 = true;
				symAlg = Constants.SM4;
				symData = KeyUtil.genRandom(16);
		}

		// 2.使用对称密钥加密原文plain
		byte[] encryptData = null;
		if (isSM4) {
			encryptData = SM4Util.ecb_encrypt(plain, symData);
			if (encryptData == null)
				throw new Exception("SM4 Encrypt Error");
		} else {
			// todo SM1/AES/DES/RC4
		}

		// 3.使用公钥加密，encryptData


		// 4.构建ASN1格式，PKCS7规范

	}

	public static void makeP7(byte[] plain, Certificate cert) {

	}

	public static void parseP7(byte[] envData, PrivateKey privateKey) {

	}

}
