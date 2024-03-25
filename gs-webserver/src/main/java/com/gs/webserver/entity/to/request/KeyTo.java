package com.gs.webserver.entity.to.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author Zhang Juan
 * @Date 2024/3/25 13:38
 */
public class KeyTo {
    @Data
    public static class GenRsaKeyPairReqTo {

        /**
         * 密钥长度
         * @mock 2048
         */
        @NotNull
        private int keySize;

    }

    @Data
    public static class EncryptReqTo {

        /**
         * 原文
         */
        @NotNull
        private String plain;

        /**
         * 公钥
         */
        @NotNull
        private String publicKeyB64;

    }

    @Data
    public static class DecryptReqTo {

        /**
         * 密文
         */
        @NotNull
        private String encryptB64;

        /**
         * 私钥
         */
        @NotNull
        private String privateKeyB64;

    }

    @Data
    public static class RawSignReqTo {

        /**
         * 原文
         */
        @NotNull
        private String plain;

        /**
         * 私钥
         */
        @NotNull
        private String privateKeyB64;

        /**
         * 签名摘要算法
         */
        @NotNull
        private String signHashAlg;
    }

    @Data
    public static class RawVerifyReqTo {
        /**
         * 原文
         */
        @NotNull
        private String plain;

        /**
         * 签名值
         */
        @NotNull
        private String signB64;

        /**
         * 公钥
         */
        @NotNull
        private String publicKeyB64;

        /**
         * 签名摘要算法
         */
        @NotNull
        private String signHashAlg;
    }
}
