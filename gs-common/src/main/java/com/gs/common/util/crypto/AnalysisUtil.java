package com.gs.common.util.crypto;

/**
 * @Author Zhang Juan
 * @Date 2024/1/15 9:50
 */
public class AnalysisUtil {
    /**
     * 凯撒密码破解
     */
    public static void decryptCaesar(String cipherText) {
        System.out.println(String.format("开始尝试凯撒密码破解"));

        // 对照码 String codeRefer="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int key = 1; key <= 25; key++) {
            String es = "";
            for (int i = 0; i < cipherText.length(); i++) {
                char c = cipherText.charAt(i);
                // 是小写字母
                if (c >= 'a' && c <= 'z') {
                    // 移动key%26位
                    c += key % 26;
                    // 向左超界
                    if (c < 'a') {
                        c += 26;
                    }
                    // 向右超界
                    if (c > 'z') {
                        c -= 26;
                    }
                } else if (c >= 'A' && c <= 'Z') {
                    // 是大写字母
                    c += key % 26;
                    if (c < 'A') {
                        c += 26;
                    }
                    if (c > 'Z') {
                        c -= 26;
                    }
                }
                es += c;
            }
            System.out.println(String.format("密钥：%s(或%s)，破解后原文：%s", key, key - 26, es));
        }


    }

    public static void main(String[] args) {
        // Hello World!
        decryptCaesar("Lipps Asvph!");
    }
}
