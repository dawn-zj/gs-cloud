package com.gs.common.util.crypto;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.gs.common.define.Constants;
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
		String publicKeyString = new String(Base64Util.encode(publicKey.getEncoded()));
		String privateKeyString = new String(Base64Util.encode(privateKey.getEncoded()));
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
}
