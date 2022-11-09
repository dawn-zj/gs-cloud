package com.gs.common.util.crypto;

import com.gs.common.define.Constants;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class CryptoHandle {
    /**
     * 公钥加密
     * @param pubKey
     * @param value
     * @param encAlg
     * @return
     * @throws Exception
     */
    protected static byte[] encryptWithPubKey(PublicKey pubKey, byte[] value, String encAlg) throws Exception {
        if (pubKey == null)
            throw new Exception("pubKey is null");

        switch (encAlg) {
            case Constants.RSA:
                Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                c.init(Cipher.ENCRYPT_MODE, pubKey);
                return c.doFinal(value);
            case Constants.SM2:
                return SM2Util.encrypt(value, pubKey);
                // throw new Exception("encrypt alg don't support, alg is: " + encAlg);
            default:
                throw new Exception("encrypt alg unknown, alg is: " + encAlg);
        }
    }

    /**
     * 私钥解密
     * @param priKey
     * @param value
     * @param encAlg
     * @return
     * @throws Exception
     */
    protected static byte[] decryptWithPriKey(PrivateKey priKey, byte[] value, String encAlg) throws Exception {
        if (priKey == null)
            throw new Exception("priKey is null");

        switch (encAlg) {
            case Constants.RSA:
                Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                c.init(Cipher.DECRYPT_MODE, priKey);
                return c.doFinal(value);
            case Constants.SM2:
                return SM2Util.decrypt(value, priKey);
            default:
                throw new Exception("decrypt alg unknown, alg is: " + encAlg);
        }
    }

    /**
     * 对明文进行签名
     * @param pubKey 国密时使用
     * @param priKey 私钥
     * @param plain 原文
     * @param signAlg 签名哈希算法
     * @param id 国密时使用
     * @return
     * @throws Exception
     */
    protected static byte[] signWithPriKey(PublicKey pubKey, PrivateKey priKey, byte[] plain, String signAlg, byte[] id) throws Exception {
        if (priKey == null)
            throw new Exception("priKey is null");

        switch (signAlg) {
            case Constants.SHA1_RSA:
                Signature sig = Signature.getInstance(Constants.SHA1_RSA);
                sig.initSign(priKey);
                sig.update(plain);
                return sig.sign();
            case Constants.SHA256_RSA:
                Signature sig256 = Signature.getInstance(Constants.SHA256_RSA);
                sig256.initSign(priKey);
                sig256.update(plain);
                return sig256.sign();
            case Constants.SM3_SM2:
                return SM2Util.signByPrivateKey(plain, priKey);
            default:
                throw new Exception("sign alg unknown, alg is: " + signAlg);
        }
    }

    /**
     * 对明文摘要进行签名
     * @param pubKey
     * @param priKey
     * @param hashData
     * @param signAlg
     * @param id
     * @return
     * @throws Exception
     */
    public byte[] signHashWithPriKey(PublicKey pubKey, PrivateKey priKey, byte[] hashData, String signAlg, byte[] id) throws Exception {
        return null;
    }

    /**
     * 对签名进行验证
     * @param pubKey 验证签名的公钥(PublicKey bytearray )
     * @param plain 明文
     * @param signed 签名密文
     * @param signAlg 签名算法
     * @param id
     * @return 验证是否通过
     * @throws Exception
     */
    protected static boolean signVerifyWithPubKey(PublicKey pubKey, byte[] plain, byte[] signed, String signAlg, byte[] id) throws Exception {
        if (pubKey == null)
            throw new Exception("pubKey is null");

        switch (signAlg) {
            case Constants.SHA1_RSA:
                Signature rsa = Signature.getInstance(Constants.SHA1_RSA);
                rsa.initVerify(pubKey);
                rsa.update(plain);
                return rsa.verify(signed);
            case Constants.SHA256_RSA:
                Signature rsa256 = Signature.getInstance(Constants.SHA256_RSA);
                rsa256.initVerify(pubKey);
                rsa256.update(plain);
                return rsa256.verify(signed);
            case Constants.SM3_SM2:
                return SM2Util.verifyByPublicKey(plain, pubKey, signed);
            default:
                throw new Exception("sign alg unknown, alg is: " + signAlg);
        }
    }

}
