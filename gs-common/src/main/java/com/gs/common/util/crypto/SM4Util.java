package com.gs.common.util.crypto;

import com.gs.common.algorithm.SM4;
import com.gs.common.util.StringUtil;

/**
 * SM4对称加密工具类
 */
public class SM4Util {

    /**
     * ECB模式加密
     * @param plain 原文
     * @param key 对称密钥
     * @return
     */
    public static byte[] ecb_encrypt(byte[] plain, byte[] key) {
        byte[] encrypt = SM4.sm4_ecb_encrypt(plain, key);
        return encrypt;
    }

    /**
     * ECB模式解密
     * @param encrypted 加密过的数据
     * @param key 对称密钥
     * @return
     */
    public static byte[] ecb_decrypt(byte[] encrypted, byte[] key) {
        byte[] decrypt = SM4.sm4_ecb_decrypt(encrypted, key);
        return decrypt;
    }

    public static void main(String[] args) {
        String plain = "plain";
        byte[] symmetricKey = KeyUtil.genRandom(16);
        byte[] encrypt = ecb_encrypt(StringUtil.getBytes(plain), symmetricKey);
        byte[] decrypt = ecb_decrypt(encrypt, symmetricKey);
        System.out.println("解密后数据：" + new String(decrypt));
    }
}
