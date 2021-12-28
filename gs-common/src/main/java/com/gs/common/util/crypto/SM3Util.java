package com.gs.common.util.crypto;

import com.gs.common.util.StringUtil;
import com.gs.common.algorithm.SM3;

import java.util.Arrays;

/**
 * SM3摘要算法工具类
 */
public class SM3Util {

    /**
     * 摘要
     * @param plainData 原文
     * @return
     */
    public static byte[] hash(byte[] plainData) {
        SM3 sm3 = new SM3();
        sm3.update(plainData);
        byte[] digest = new byte[32];
        sm3.digest(digest);
        return digest;
    }

    /**
     * 验证摘要
     * @param plainData 原文
     * @param hashData 摘要
     * @return
     */
    public static boolean verify(byte[] plainData, byte[] hashData) {
        byte[] hash = hash(plainData);
        return Arrays.equals(hash, hashData);
    }

    public static void main(String[] args) {
        String plain = "plain";
        byte[] hashData = hash(StringUtil.getBytes(plain));
        boolean verify = verify(StringUtil.getBytes(plain), hashData);
        System.out.println(verify);
    }
}
