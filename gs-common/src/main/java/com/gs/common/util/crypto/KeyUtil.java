package com.gs.common.util.crypto;

import com.gs.common.pkcs.pkcs7.PKCS7Envelope;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class KeyUtil {

    public static byte[] attachedSign(PublicKey pubKey, PrivateKey priKey, byte[] plain, String signAlg, byte[] id) throws Exception {
        return CryptoHandle.signWithPriKey(pubKey, priKey, plain, signAlg, id);
    }

    /**
     * detached签名：包含签名值和证书
     * @param plain
     * @param cert
     * @return
     * @throws Exception
     */
    public static byte[] detachedSign(byte[] plain, X509Certificate cert) throws Exception {
        // todo 未传私钥，肯定不对
        byte[] bytes = PKCS7Envelope.makeP7(plain, cert);
        return  bytes;
    }

    /**
     * detached验签
     * @param signed
     * @param priKey
     * @return
     * @throws Exception
     */
    public static byte[] detachedVerify(byte[] signed, PrivateKey priKey) throws Exception {
        byte[] bytes = PKCS7Envelope.verifyP7(signed, priKey);
        return  bytes;
    }

    /**
     * 裸签：只含签名值
     * @param pubKey 国密时使用
     * @param priKey 私钥
     * @param plain 原文
     * @param signAlg 签名哈希算法
     * @param id 国密时使用
     * @return
     * @throws Exception
     */
    public static byte[] rawSign(PublicKey pubKey, PrivateKey priKey, byte[] plain, String signAlg, byte[] id) throws Exception {
        return  CryptoHandle.signWithPriKey(pubKey, priKey, plain, signAlg, id);
    }

    /**
     * 裸验签：需要明文、公钥和签名值
     * @param pubKey 验证签名的公钥(PublicKey bytearray )
     * @param plain 明文
     * @param signed 签名密文
     * @param signAlg 签名算法
     * @param id
     * @return
     * @throws Exception
     */
    public static boolean rawSignVerify(PublicKey pubKey, byte[] plain, byte[] signed, String signAlg, byte[] id) throws Exception {
        return  CryptoHandle.signVerifyWithPubKey(pubKey, plain, signed, signAlg, id);
    }

    public static byte[] encrypt(PublicKey pubKey, byte[] value, String encAlg) throws Exception {
        return CryptoHandle.encryptWithPubKey(pubKey, value, encAlg);
    }

    public static byte[] decrypt(PrivateKey priKey, byte[] value, String encAlg) throws Exception {
        return CryptoHandle.decryptWithPriKey(priKey, value, encAlg);
    }



    /**
     * 生成随机数
     * @param len 字节长度
     * @return
     * @throws Exception
     */
    public static byte[] genRandom(int len) {
        byte[] bs = new byte[len];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bs);
        return bs;
    }

}
