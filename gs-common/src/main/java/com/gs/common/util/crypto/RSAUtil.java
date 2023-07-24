package com.gs.common.util.crypto;

import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.gs.common.define.Constants;
import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.util.FileUtil;
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
		/*
		 * pkcs1: -----BEGIN RSA PRIVATE KEY-----	-----END RSA PRIVATE KEY-----
		 * pkcs8: -----BEGIN PRIVATE KEY-----	-----END PRIVATE KEY-----
		 */

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

}
