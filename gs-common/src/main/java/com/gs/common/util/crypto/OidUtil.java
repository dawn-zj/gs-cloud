package com.gs.common.util.crypto;

import java.util.Hashtable;

public class OidUtil {
    public static Hashtable algorithms = new Hashtable();
    public static Hashtable oids = new Hashtable();

    static {
        algorithms.put("MD2WITHRSAENCRYPTION", "1.2.840.113549.1.1.2");
        algorithms.put("MD2WITHRSA", "1.2.840.113549.1.1.2");
        algorithms.put("MD5WITHRSAENCRYPTION", "1.2.840.113549.1.1.4");
        algorithms.put("MD5WITHRSA", "1.2.840.113549.1.1.4");
        algorithms.put("RSAWITHMD5", "1.2.840.113549.1.1.4");
        algorithms.put("SHA1WITHRSAENCRYPTION", "1.2.840.113549.1.1.5");
        algorithms.put("SHA1WITHRSA", "1.2.840.113549.1.1.5");
        algorithms.put("SHA256WITHRSA", "1.2.840.113549.1.1.11");
        algorithms.put("RSAWITHSHA1", "1.2.840.113549.1.1.5");
        algorithms.put("RIPEMD160WITHRSAENCRYPTION", "1.3.36.3.3.1.2");
        algorithms.put("RIPEMD160WITHRSA", "1.3.36.3.3.1.2");
        algorithms.put("SHA1WITHDSA", "1.2.840.10040.4.3");
        algorithms.put("DSAWITHSHA1", "1.2.840.10040.4.3");
        algorithms.put("SHA1WITHECDSA", "1.2.840.10045.4.1");
        algorithms.put("ECDSAWITHSHA1", "1.2.840.10045.4.1");
        algorithms.put("SM3WITHSM2", "1.2.156.10197.1.501");
        algorithms.put("SHA1WITHSM2", "1.2.156.197.1.502");
        algorithms.put("SHA256WITHSM2", "1.2.156.197.1.503");
        oids.put("1.2.840.113549.1.1.5", "SHA1WITHRSA");
        oids.put("1.2.840.113549.1.1.4", "MD5WITHRSA");
        oids.put("1.2.840.113549.1.1.2", "MD2WITHRSA");
        oids.put("1.2.840.10040.4.3", "DSAWITHSHA1");
        oids.put("1.2.840.113549.1.1.11", "SHA256WITHRSA");
        oids.put("1.2.156.197.1.501", "SM3WITHSM2");
        oids.put("1.2.156.10197.1.501", "SM3WITHSM2");
        oids.put("1.2.156.197.1.502", "SHA1WITHSM2");
        oids.put("1.2.156.197.1.503", "SHA256WITHSM2");
    }

    public static final String OID_MD2 = "1.2.840.113549.2.2";

    public static final String OID_MD5 = "1.2.840.113549.2.5";

    public static final String OID_SHA1 = "1.3.14.3.2.26";

    public static final String OID_SHA224 = "2.16.840.1.101.3.4.2.4";

    public static final String OID_SHA256 = "2.16.840.1.101.3.4.2.1";

    public static final String OID_SHA384 = "2.16.840.1.101.3.4.2.2";

    public static final String OID_SHA512 = "2.16.840.1.101.3.4.2.3";

    public static final String OID_SM3 = "1.2.156.10197.1.401";

    public static final String OID_RSA = "1.2.840.113549.1.1.1";

    public static final String OID_SM2 = "1.2.156.10197.1.301.1";

    public static final String OID_SM1 = "1.2.156.10197.1.102";

    public static final String OID_MD2withRSA = "1.2.840.113549.1.1.2";

    public static final String OID_MD5withRSA = "1.2.840.113549.1.1.4";

    public static final String OID_SHA1withRSA = "1.2.840.113549.1.1.5";

    public static final String OID_SHA256withRSA = "1.2.840.113549.1.1.11";

    public static final String OID_SM3withSM2 = "1.2.156.10197.1.501";

}
