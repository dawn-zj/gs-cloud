package com.gs.common.util.crypto;

import com.gs.common.define.Constants;

import java.security.Signature;
import java.util.Hashtable;

public class OidUtil {
    public static Hashtable algorithms = new Hashtable();
    public static Hashtable oids = new Hashtable();

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

    public static String getSignAlg(String signHashAlg) throws Exception {
        if (Constants.SHA1_RSA.equalsIgnoreCase(signHashAlg)
                || Constants.SHA256_RSA.equalsIgnoreCase(signHashAlg)) {
            return Constants.RSA;
        } else if (Constants.SM3_SM2.equalsIgnoreCase(signHashAlg)) {
            return Constants.SM2;
        } else {
            throw new Exception("unknown " + signHashAlg);
        }
    }

    public static String getSignAlgOid(String signHashAlg) throws Exception {
        if (Constants.SHA1_RSA.equalsIgnoreCase(signHashAlg)
                || Constants.SHA256_RSA.equalsIgnoreCase(signHashAlg)) {
            return getOid(Constants.RSA);
        } else if (Constants.SM3_SM2.equalsIgnoreCase(signHashAlg)) {
            return getOid(Constants.SM2);
        } else {
            throw new Exception("unknown " + signHashAlg);
        }
    }

}
