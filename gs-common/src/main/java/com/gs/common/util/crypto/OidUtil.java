package com.gs.common.util.crypto;

import java.util.Hashtable;

public class OidUtil {
    public static Hashtable algorithms = new Hashtable();
    public static Hashtable oids = new Hashtable();

    // 参考链接：https://blog.csdn.net/A_Lonely_Smile/article/details/121273134
    static {
        algorithms.put("SHA1", "1.3.14.3.2.26");
        algorithms.put("SHA256", "2.16.840.1.101.3.4.2.1");
        algorithms.put("MD2withRSA", "1.2.840.113549.1.1.2");
        algorithms.put("MD4withRSA", "1.2.840.113549.1.1.3");
        algorithms.put("MD5withRSA", "1.2.840.113549.1.1.4");
        algorithms.put("SHA1withRSA", "1.2.840.113549.1.1.5");
        algorithms.put("SHA256withRSA", "1.2.840.113549.1.1.11");
        algorithms.put("SHA384withRSA", "1.2.840.113549.1.1.12");
        algorithms.put("SHA512withRSA", "1.2.840.113549.1.1.13");
        algorithms.put("SHA224withRSA", "1.2.840.113549.1.1.14");
        algorithms.put("SM6", "1.2.156.197.1.101");
        algorithms.put("SM1", "1.2.156.197.1.102");
        algorithms.put("SF33", "1.2.156.197.1.103");
        algorithms.put("SM4", "1.2.156.197.1.104");
        algorithms.put("SM7", "1.2.156.197.1.105");
        algorithms.put("SM8", "1.2.156.197.1.106");
        algorithms.put("SM5", "1.2.156.197.1.201");
        algorithms.put("SM2", "1.2.156.197.1.301");
        algorithms.put("SM2", "1.2.156.197.1.301.1");
        algorithms.put("SM2", "1.2.156.197.1.301.2");
        algorithms.put("SM2", "1.2.156.197.1.301.3");
        algorithms.put("SM9", "1.2.156.197.1.302");
        algorithms.put("SM9", "1.2.156.197.1.302.1");
        algorithms.put("SM9", "1.2.156.197.1.302.2");
        algorithms.put("SM9", "1.2.156.197.1.302.3");
        algorithms.put("RSA", "1.2.156.197.1.310");
        algorithms.put("SM3", "1.2.156.197.1.401");
        algorithms.put("SM3", "1.2.156.197.1.401.1");
        algorithms.put("SM3", "1.2.156.197.1.401.2");
        algorithms.put("SHA1", "1.2.156.197.1.410");
        algorithms.put("SHA1", "1.2.156.197.1.410.1");
        algorithms.put("SHA1", "1.2.156.197.1.410.2");
        algorithms.put("SHA256", "1.2.156.197.1.411");
        algorithms.put("SHA256", "1.2.156.197.1.411.1");
        algorithms.put("SHA256", "1.2.156.197.1.411.2");
        algorithms.put("SM3withSM2", "1.2.156.197.1.501");
        algorithms.put("SHA1withSM2", "1.2.156.197.1.502");
        algorithms.put("SHA256withSM2", "1.2.156.197.1.503");
        algorithms.put("SM3withRSA", "1.2.156.197.1.504");
        algorithms.put("SHA1withRSA", "1.2.156.197.1.505");
        algorithms.put("SHA256withRSA", "1.2.156.197.1.506");
        algorithms.put("SM6", "1.2.156.10197.1.101");
        algorithms.put("SM1", "1.2.156.10197.1.102");
        algorithms.put("SF33", "1.2.156.10197.1.103");
        algorithms.put("SM4", "1.2.156.10197.1.104");
        algorithms.put("SM7", "1.2.156.10197.1.105");
        algorithms.put("SM8", "1.2.156.10197.1.106");
        algorithms.put("SM5", "1.2.156.10197.1.201");
        algorithms.put("SM2", "1.2.156.10197.1.301");
        algorithms.put("SM2", "1.2.156.10197.1.301.1");
        algorithms.put("SM2", "1.2.156.10197.1.301.2");
        algorithms.put("SM2", "1.2.156.10197.1.301.3");
        algorithms.put("SM9", "1.2.156.10197.1.302");
        algorithms.put("SM9", "1.2.156.10197.1.302.1");
        algorithms.put("SM9", "1.2.156.10197.1.302.2");
        algorithms.put("SM9", "1.2.156.10197.1.302.3");
        algorithms.put("RSA", "1.2.156.10197.1.310");
        algorithms.put("SM3", "1.2.156.10197.1.401");
        algorithms.put("SM3", "1.2.156.10197.1.401.1");
        algorithms.put("SM3", "1.2.156.10197.1.401.2");
        algorithms.put("SHA1", "1.2.156.10197.1.410");
        algorithms.put("SHA1", "1.2.156.10197.1.410.1");
        algorithms.put("SHA1", "1.2.156.10197.1.410.2");
        algorithms.put("SHA256", "1.2.156.10197.1.411");
        algorithms.put("SHA256", "1.2.156.10197.1.411.1");
        algorithms.put("SHA256", "1.2.156.10197.1.411.2");
        algorithms.put("SM3withSM2", "1.2.156.10197.1.501");
        algorithms.put("SHA1withSM2", "1.2.156.10197.1.502");
        algorithms.put("SHA256withSM2", "1.2.156.10197.1.503");
        algorithms.put("SM3withRSA", "1.2.156.10197.1.504");
        algorithms.put("SHA1withRSA", "1.2.156.10197.1.505");
        algorithms.put("SHA256withRSA", "1.2.156.10197.1.506");
        algorithms.put("PKCS#7DATA", "1.2.156.10197.6.1.4.2.1");
        algorithms.put("PKCS#7SIGNEDDATA", "1.2.156.10197.6.1.4.2.2");
        algorithms.put("PKCS#7ENVELOPEDDATA", "1.2.156.10197.6.1.4.2.3");
        algorithms.put("RSA", "1.2.840.113549.1.1.1");
        algorithms.put("DSA", "1.2.840.10040.4.1");


        oids.put("1.3.14.3.2.26", "SHA1");
        oids.put("2.16.840.1.101.3.4.2.1", "SHA256");
        oids.put("1.2.840.113549.1.1.2", "MD2withRSA");
        oids.put("1.2.840.113549.1.1.3", "MD4withRSA");
        oids.put("1.2.840.113549.1.1.4", "MD5withRSA");
        oids.put("1.2.840.113549.1.1.5", "SHA1withRSA");
        oids.put("1.2.840.113549.1.1.11", "SHA256withRSA");
        oids.put("1.2.840.113549.1.1.12", "SHA384withRSA");
        oids.put("1.2.840.113549.1.1.13", "SHA512withRSA");
        oids.put("1.2.840.113549.1.1.14", "SHA224withRSA");
        oids.put("1.2.156.197.1.101", "SM6");
        oids.put("1.2.156.197.1.102", "SM1");
        oids.put("1.2.156.197.1.103", "SF33");
        oids.put("1.2.156.197.1.104", "SM4");
        oids.put("1.2.156.197.1.105", "SM7");
        oids.put("1.2.156.197.1.106", "SM8");
        oids.put("1.2.156.197.1.201", "SM5");
        oids.put("1.2.156.197.1.301", "SM2");
        oids.put("1.2.156.197.1.301.1", "SM2");
        oids.put("1.2.156.197.1.301.2", "SM2");
        oids.put("1.2.156.197.1.301.3", "SM2");
        oids.put("1.2.156.197.1.302", "SM9");
        oids.put("1.2.156.197.1.302.1", "SM9");
        oids.put("1.2.156.197.1.302.2", "SM9");
        oids.put("1.2.156.197.1.302.3", "SM9");
        oids.put("1.2.156.197.1.310", "RSA");
        oids.put("1.2.156.197.1.401", "SM3");
        oids.put("1.2.156.197.1.401.1", "SM3");
        oids.put("1.2.156.197.1.401.2", "SM3");
        oids.put("1.2.156.197.1.410", "SHA1");
        oids.put("1.2.156.197.1.410.1", "SHA1");
        oids.put("1.2.156.197.1.410.2", "SHA1");
        oids.put("1.2.156.197.1.411", "SHA256");
        oids.put("1.2.156.197.1.411.1", "SHA256");
        oids.put("1.2.156.197.1.411.2", "SHA256");
        oids.put("1.2.156.197.1.501", "SM3withSM2");
        oids.put("1.2.156.197.1.502", "SHA1withSM2");
        oids.put("1.2.156.197.1.503", "SHA256withSM2");
        oids.put("1.2.156.197.1.504", "SM3withRSA");
        oids.put("1.2.156.197.1.505", "SHA1withRSA");
        oids.put("1.2.156.197.1.506", "SHA256withRSA");
        oids.put("1.2.156.10197.1.101", "SM6");
        oids.put("1.2.156.10197.1.102", "SM1");
        oids.put("1.2.156.10197.1.103", "SF33");
        oids.put("1.2.156.10197.1.104", "SM4");
        oids.put("1.2.156.10197.1.105", "SM7");
        oids.put("1.2.156.10197.1.106", "SM8");
        oids.put("1.2.156.10197.1.201", "SM5");
        oids.put("1.2.156.10197.1.301", "SM2");
        oids.put("1.2.156.10197.1.301.1", "SM2");
        oids.put("1.2.156.10197.1.301.2", "SM2");
        oids.put("1.2.156.10197.1.301.3", "SM2");
        oids.put("1.2.156.10197.1.302", "SM9");
        oids.put("1.2.156.10197.1.302.1", "SM9");
        oids.put("1.2.156.10197.1.302.2", "SM9");
        oids.put("1.2.156.10197.1.302.3", "SM9");
        oids.put("1.2.156.10197.1.310", "RSA");
        oids.put("1.2.156.10197.1.401", "SM3");
        oids.put("1.2.156.10197.1.401.1", "SM3");
        oids.put("1.2.156.10197.1.401.2", "SM3");
        oids.put("1.2.156.10197.1.410", "SHA1");
        oids.put("1.2.156.10197.1.410.1", "SHA1");
        oids.put("1.2.156.10197.1.410.2", "SHA1");
        oids.put("1.2.156.10197.1.411", "SHA256");
        oids.put("1.2.156.10197.1.411.1", "SHA256");
        oids.put("1.2.156.10197.1.411.2", "SHA256");
        oids.put("1.2.156.10197.1.501", "SM3withSM2");
        oids.put("1.2.156.10197.1.502", "SHA1withSM2");
        oids.put("1.2.156.10197.1.503", "SHA256withSM2");
        oids.put("1.2.156.10197.1.504", "SM3withRSA");
        oids.put("1.2.156.10197.1.505", "SHA1withRSA");
        oids.put("1.2.156.10197.1.506", "SHA256withRSA");
        oids.put("1.2.156.10197.6.1.4.2.1", "PKCS#7DATA");
        oids.put("1.2.156.10197.6.1.4.2.2", "PKCS#7SIGNEDDATA");
        oids.put("1.2.156.10197.6.1.4.2.3", "PKCS#7ENVELOPEDDATA");
        oids.put("1.2.840.113549.1.1.1", "RSA");
        oids.put("1.2.840.10040.4.1", "DSA");

        // 对称加密
        oids.put("2.16.840.1.101.3.4.1.2", "AES128_CBC");

        // pkcs7 对象标识符
        oids.put("1.2.840.113549.1.7.1", "data");
        oids.put("1.2.840.113549.1.7.2", "signedData");
        oids.put("1.2.840.113549.1.7.3", "envelopedData");
        oids.put("1.2.840.113549.1.7.4", "signedAndEnvelopedData");
        oids.put("1.2.840.113549.1.7.5", "digestedData");
        oids.put("1.2.840.113549.1.7.6", "encryptedData");

        // 证书
        oids.put("2.5.4.3", "CN");
        oids.put("2.5.4.4", "Surname");
        oids.put("2.5.4.6", "C");
        oids.put("2.5.4.7", "Locality");
        oids.put("2.5.4.8", "State");
        oids.put("2.5.4.9", "StreetAddress");
        oids.put("2.5.4.10", "O");
        oids.put("2.5.4.10", "OU");

    }

    public static String getSignHash(String oid) {
        Object obj = oids.get(oid);
        if (obj != null) {
            return obj.toString();
        }
        return "";
    }

    public static String getOid(String signHash) {
        Object obj = algorithms.get(signHash);
        if (obj != null) {
            return obj.toString();
        }
        return "";
    }

}
