package com.gs.common.util.p10;

import com.gs.common.define.Constants;
import com.gs.common.entity.KeyStoreIo;
import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.util.base64.Base64Util;
import com.gs.common.util.crypto.KeyUtil;
import com.gs.common.util.crypto.OidUtil;
import com.gs.common.util.crypto.RSAUtil;
import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import javax.security.auth.x500.X500Principal;
import java.security.KeyPair;
import java.security.PublicKey;

/**
 * 证书请求工具类
 */
public class P10Util {

    /**
     * 生成证书请求
     *
     * @param keyType   密钥类型，为了兼容软硬
     * @param alg       密钥算法
     * @param subjectDn 证书主题
     * @return
     */
    public static KeyStoreIo genP10(String keyType, String alg, String subjectDn) {
        try {
            switch (keyType) {
                // 暂以pri标记为软签名
                case ".pri":
                    // 生成密钥对
                    KeyPair keyPair = null;
                    if (Constants.RSA.equals(alg)) {
                        keyPair = RSAUtil.genRSAKeyPair(Constants.RSA_KEY_SIZE_1024);
                    } else {
                        throw new NetGSRuntimeException("not support alg: " + alg);
                    }
                    // 生成p10
                    String p10 = genP10(keyPair, subjectDn);
                    // 封装起来返回
                    KeyStoreIo keyStore = new KeyStoreIo(p10, keyPair.getPrivate().getEncoded());
                    return keyStore;
                default:
                    return null;
            }
        } catch (Exception e) {
            throw new NetGSRuntimeException("gen certificate request error", e);
        }
    }

    /**
     * 生成证书请求
     *
     * @param kayPair   密钥对
     * @param subjectDn 证书主题
     * @return
     */
    private static String genP10(KeyPair kayPair, String subjectDn) {
        try {
            JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder("SHA256withRSA");
            ContentSigner signer = signerBuilder.build(kayPair.getPrivate());

            X500Principal x509Name = new X500Principal(subjectDn);
            JcaPKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(x509Name, kayPair.getPublic());
            PKCS10CertificationRequest certificationRequest = builder.build(signer);

            byte[] data = certificationRequest.getEncoded();
            String encode = Base64Util.encode(data);
            return encode;
        } catch (Exception e) {
            throw new NetGSRuntimeException("gen certificate request error", e);
        }
    }

    /**
     * 从证书请求中提取公钥
     * @param p10Data
     * @return
     */
    public static byte[] getPubKeyFormP10(byte[] p10Data) {
        try {
            PKCS10CertificationRequest p10 = new PKCS10CertificationRequest(p10Data);
            byte[] pubKey = p10.getSubjectPublicKeyInfo().getEncoded();
            return pubKey;
        } catch (Exception e) {
            throw new NetGSRuntimeException("get pubKey form P10 error", e);
        }
    }

    /**
     * 验证证书请求文件
     * @param p10Data 证书请求文件数据
     * @return
     */
    public static boolean verifyP10(byte[] p10Data) {
        try {
            // 将p10文件数据转为PKCS10CertificationRequest
            PKCS10CertificationRequest p10 = new PKCS10CertificationRequest(p10Data);

            // 获取公钥
            byte[] pubKey = p10.getSubjectPublicKeyInfo().getEncoded();
            PublicKey publicKey = RSAUtil.generateP8PublicKey(pubKey);

            // 获取算法
            String algorithmOid = p10.getSignatureAlgorithm().getAlgorithm().toString();
            String algorithm = (String) OidUtil.oids.get(algorithmOid);

            // 获取签名值
            byte[] signature = p10.getSignature();

            // 获取CertificationRequest的CertificationRequestInfo，即原文
            byte[] encoded = p10.getEncoded();
            CertificationRequest request = CertificationRequest.getInstance(encoded);
            byte[] plain = request.getCertificationRequestInfo().getEncoded();

            // 裸签验证
            boolean result = KeyUtil.rawSignVerify(publicKey, plain, signature, algorithm, null);
            return result;
        } catch (Exception e) {
            throw new NetGSRuntimeException("verify P10 error", e);
        }
    }

}
