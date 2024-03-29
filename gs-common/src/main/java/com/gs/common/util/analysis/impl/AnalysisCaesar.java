package com.gs.common.util.analysis.impl;

import com.gs.common.util.StringUtil;
import com.gs.common.util.analysis.AnalysisUtil;
import com.gs.common.util.seal.SealUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Zhang Juan
 * @Date 2024/3/29 16:59
 */
@Slf4j
public class AnalysisCaesar extends AnalysisUtil {

    @Override
    public void analysis(byte[] data) throws Exception {
        decryptCaesar(StringUtil.getString(data));
    }

    public void decryptCaesar(String cipherText) {
        log.debug("开始尝试凯撒密码破解");

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
            log.debug("密钥：{}(或{})，破解后原文：{}", key, key - 26, es);
        }


    }

    public static void main(String[] args) throws Exception {
        // Hello World!
        AnalysisUtil analysisUtil = new AnalysisCaesar();
        analysisUtil.analysis("Lipps Asvph!".getBytes());
    }
}
