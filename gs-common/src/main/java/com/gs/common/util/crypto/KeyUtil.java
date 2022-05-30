package com.gs.common.util.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class KeyUtil {

    public static byte[] attachedSign(PublicKey pubKey, PrivateKey priKey, byte[] plain, int hsmId, String signAlg, byte[] id) throws Exception {
        return CryptoHandle.signWithPriKey(pubKey, priKey, plain, hsmId, signAlg, id);
    }

    public static byte[] detachedSign(PublicKey pubKey, PrivateKey priKey, byte[] plain, int hsmId, String signAlg, byte[] id) throws Exception {
        return  CryptoHandle.signWithPriKey(pubKey, priKey, plain, hsmId, signAlg, id);
    }

    /**
     * 裸签
     * @param pubKey 国密时使用
     * @param priKey 私钥
     * @param plain 原文
     * @param hsmId 加密卡时使用
     * @param signAlg 签名哈希算法
     * @param id 国密时使用
     * @return
     * @throws Exception
     */
    public static byte[] rawSign(PublicKey pubKey, PrivateKey priKey, byte[] plain, int hsmId, String signAlg, byte[] id) throws Exception {
        return  CryptoHandle.signWithPriKey(pubKey, priKey, plain, hsmId, signAlg, id);
    }

    /**
     * 裸验签
     * @param pubKey 验证签名的公钥(PublicKey bytearray )
     * @param plain 明文
     * @param signed 签名密文
     * @param hsmId 加密卡id
     * @param signAlg 签名算法
     * @param id
     * @return
     * @throws Exception
     */
    public static boolean rawSignVerify(PublicKey pubKey, byte[] plain, byte[] signed, int hsmId, String signAlg, byte[] id) throws Exception {
        return  CryptoHandle.signVerifyWithPubKey(pubKey, plain, signed, hsmId, signAlg, id);
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
