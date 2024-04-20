package com.gs.common.util.crypto;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import com.gs.common.define.Constants;
import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.util.FileUtil;
import com.gs.common.util.StringUtil;
import com.gs.common.util.base64.Base64Util;

public class RSAUtil {

	static {
		// Security.addProvider(); // 当使用第三方provider，并使用getInstance(String type, String provider)方式时，需要注册provider
	}

	public static void main(String[] args) throws Exception {
		KeyPair kayPair = genRSAKeyPair(Constants.RSA_KEY_SIZE_1024);
		RSAPublicKey publicKey = (RSAPublicKey)kayPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey)kayPair.getPrivate();
		//得到base64编码的公钥/私钥字符串
		String publicKeyString = Base64Util.encode(publicKey.getEncoded());
		String privateKeyString = Base64Util.encode(privateKey.getEncoded());
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "rsa/pubKey.txt", publicKeyString.getBytes());
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "rsa/priKey.txt", privateKeyString.getBytes());
		System.out.println("RSA密钥对存储成功！");
	}

	/**
	 * 生成RSA密钥对
	 * @param keySize 密钥长度
	 * @return KeyPair PKCS8格式密钥对
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static KeyPair genRSAKeyPair(int keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
		// 1.获取实例:provider是啥
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		// 2.初始化长度
		kpg.initialize(keySize);
		// 3.生成密钥对，PKCS8格式，java平台适用
		KeyPair keyPair = kpg.generateKeyPair();
		return keyPair;
	}

	/**
	 * 根据私钥派生公钥
	 * @param priKeyB64
	 * @return
	 * @throws Exception
	 */
	public static PublicKey genPublicKeyByPrivateKey(String priKeyB64) throws Exception {
		// 解析私钥
		byte[] decode = Base64Util.decode(priKeyB64);
		PrivateKey privateKey = generateP8PrivateKey(decode);

		// 获取私钥参数，从私钥中可以提取出公钥，只需要取出n和e即可
		// 私钥文件的内容解码后，包含了公钥和私钥的所有参数，包括n、e、d、p、q、d mod (p-1)、d mod (q-1)和q^-1 mod p。
		// n = modulus，e = publicExponent，d = privateExponent，p = prime1，q = prime2
		// d mod (p-1) = exponent1, d mod (q-1) = exponent2, q^-1 mod p = coefficient
		RSAPrivateCrtKey rsaPrivateKey = (RSAPrivateCrtKey)privateKey;
		BigInteger modulus = rsaPrivateKey.getModulus();
		BigInteger publicExponent = rsaPrivateKey.getPublicExponent();

		// 生成公钥参数
		RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(rsaPublicKeySpec);
		return publicKey;
	}

	/**
	 * 检测公私钥是否匹配
	 * @param pubKeyB64
	 * @param priKeyB64
	 * @return
	 * @throws Exception
	 */
	public static boolean checkKeyPair(String pubKeyB64, String priKeyB64) throws Exception {
		PublicKey publicKey = genPublicKeyByPrivateKey(priKeyB64);

		byte[] rsaPublicKeyEncoded = publicKey.getEncoded();
		if (Arrays.equals(rsaPublicKeyEncoded, Base64Util.decode(pubKeyB64))) {
			return true;
		}

		return false;
	}

	/**
	 * pkcs8: -----BEGIN PRIVATE KEY-----	-----END PRIVATE KEY-----
	 * @param privateKeyData
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey generateP8PrivateKey(byte[] privateKeyData) throws Exception {
		/*
		 * pkcs1: -----BEGIN RSA PRIVATE KEY-----	-----END RSA PRIVATE KEY-----
		 * pkcs8: -----BEGIN PRIVATE KEY-----	-----END PRIVATE KEY-----
		 */

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyData);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(keySpec);
	}

	public static PublicKey generateP8PublicKey(byte[] publicKeyData) throws Exception {
		X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(publicKeyData);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(encPubKeySpec);
	}

	//TODO RSA 加密、解密


	private static byte[] getHashMagic(String hash) {
		// see https://www.ietf.org/rfc/rfc3110.txt
		// RSA/SHA1 SIG Resource Records
		byte result[];
		switch (hash) {
			case Constants.SHA1:
				result = new byte[] { 0x30, 0x1f, 0x30, 0x07, 0x06, 0x05, 0x2b, 0x0e, 0x03, 0x02, 0x1a, 0x04, 0x14 };
				break;
			case Constants.SHA224:
				result = new byte[] { 0x30, 0x2b, 0x30, 0x0b, 0x06, 0x09, 0x60, (byte) 0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x04, 0x04, 0x1c };
				break;
			case Constants.SHA256:
				result = new byte[] { 0x30, 0x2f, 0x30, 0x0b, 0x06, 0x09, 0x60, (byte) 0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x01, 0x04, 0x20 };
				break;
			case Constants.SHA384:
				result = new byte[] { 0x30, 0x3f, 0x30, 0x0b, 0x06, 0x09, 0x60, (byte) 0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x02, 0x04, 0x30 };
				break;
			case Constants.SHA512:
				result = new byte[] { 0x30, 0x4f, 0x30, 0x0b, 0x06, 0x09, 0x60, (byte) 0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x03, 0x04, 0x40 };
				break;
//			case ripemd128:
//				result = new byte[] { 0x30, 0x1b, 0x30, 0x07, 0x06, 0x05, 0x2b, 0x24, 0x03, 0x02, 0x02, 0x04, 0x10 };
//				break;
//			case ripemd160:
//				result = new byte[] { 0x30, 0x1f, 0x30, 0x07, 0x06, 0x05, 0x2b, 0x24, 0x03, 0x02, 0x01, 0x04, 0x14 };
//				break;
			// case ripemd256: result = new byte[]
			// { 0x30, 0x2b, 0x30, 0x07, 0x06, 0x05, 0x2b, 0x24
			// , 0x03, 0x02, 0x03, 0x04, 0x20 };
			// break;
			default:
				throw new NetGSRuntimeException("Hash algorithm " + hash + " not supported for signing.");
		}

		return result;
	}

	/**
	 * 将hash值转为PKCS1规范的结构
	 * @param hash hash算法
	 * @param hashData hash值
	 * @return der编码后的hash值
	 * @throws Exception
	 */
	public static byte[] genDerHash(String hash, byte[] hashData) throws Exception {
		ByteArrayOutputStream digestInfoValueBuf = new ByteArrayOutputStream();
		digestInfoValueBuf.write(getHashMagic(hash));
		digestInfoValueBuf.write(hashData);
		byte[] derHashData = digestInfoValueBuf.toByteArray();
		return derHashData;
	}

	public static String getPemPubKey(String base64) {
		return getPemPubKey(base64, true);
	}

	public static String getPemPubKey(String base64, boolean withBeginEnd) {
		String str = "-----BEGIN PUBLIC KEY-----\n";
		String key = StringUtil.formatB64(base64);
		if (withBeginEnd) {
			str += key;
			str += "-----END PUBLIC KEY-----\n";
		}
		return str;
	}

	public static String getPemPriKey(String base64) {
		return getPemPriKey(base64, true, 8);
	}

	/**
	 * 获取pem格式的私钥
	 * @param base64 私钥base64
	 * @param withBeginEnd 是否带头尾
	 * @param keyType 密钥类型：PKCS1、PKCS8
	 * @return
	 */
	public static String getPemPriKey(String base64, boolean withBeginEnd, int keyType) {
		String pkcs1Begin = "-----BEGIN RSA PRIVATE KEY-----\n";
		String pkcs1End = "-----END RSA PRIVATE KEY-----\n";

		String pkcs8Begin = "-----BEGIN PRIVATE KEY-----\n";
		String pkcs8End = "-----END PRIVATE KEY-----\n";

		String str = "";
		String key = StringUtil.formatB64(base64);
		if (withBeginEnd) {
			switch (keyType) {
				case 1:
					str += pkcs1Begin;
					str += key;
					str += pkcs1End;
					break;
				case 8:
				default:
					str += pkcs8Begin;
					str += key;
					str += pkcs8End;
					break;
			}
		}
		return str;
	}

	public static String clearPemPubKey(String base64) {
		String pkcsBegin = "-----BEGIN PUBLIC KEY-----";
		String pkcsEnd = "-----END PUBLIC KEY-----";

		base64 = base64.replace(pkcsBegin, "")
				.replace(pkcsEnd, "")
				.replace("\n", "");

		return base64;
	}

	public static String clearPemPriKey(String base64) {
		String pkcs1Begin = "-----BEGIN RSA PRIVATE KEY-----";
		String pkcs1End = "-----END RSA PRIVATE KEY-----";

		String pkcs8Begin = "-----BEGIN PRIVATE KEY-----";
		String pkcs8End = "-----END PRIVATE KEY-----";

		base64 = base64.replace(pkcs1Begin, "")
				.replace(pkcs1End, "")
				.replace(pkcs8Begin, "")
				.replace(pkcs8End, "")
				.replace("\n", "");

		return base64;
	}

}
