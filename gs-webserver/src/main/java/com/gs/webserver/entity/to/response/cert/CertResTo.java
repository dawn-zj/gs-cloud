package com.gs.webserver.entity.to.response.cert;

import lombok.Data;

/**
 * @author Administator
 */
@Data
public class CertResTo {

    @Data
    public static class CertTo {
        /**
         * 证书序列号
         */
        private String sn;
        /**
         * 证书主题
         */
        private String dn;
        /**
         * 证书通用名称
         */
        private String cn;
        /**
         * 证书颁发者主题
         */
        private String issueDn;
        /**
         * 证书颁发者通用名称
         */
        private String issueCn;
        /**
         * 密钥用法
         */
        private boolean[] keyUsage;
        /**
         * 证书起始日期
         */
        private Long notBefore;
        /**
         * 证书结束日期
         */
        private Long notAfter;
        /**
         * 证书授权密钥标识符
         */
        private String keyId;
        /**
         * 证书颁发者密钥标识
         */
        private String keyIdIssue;
    }

    @Data
    public static class CertRequestTo {
        /**
         * 验证结果
         */
        private boolean result;
        /**
         * 公钥
         */
        private String publicKeyB64;
    }

}
