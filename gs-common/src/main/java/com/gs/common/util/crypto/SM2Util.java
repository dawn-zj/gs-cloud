package com.gs.common.util.crypto;

import com.gs.common.define.Constants;
import com.gs.common.util.FileUtil;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SM2Util {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 生成密钥对
     * @return 密钥对
     * @throws Exception
     */
    public static KeyPair genKeyPair() throws Exception {
        // 1.获取实例:provider是啥
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
        // 2.初始化长度
        ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
        kpg.initialize(sm2Spec);
        // 3.生成密钥对，PKCS8格式，java平台适用
        KeyPair keyPair = kpg.generateKeyPair();
        return keyPair;
    }

    /**
     * 将Base64转码的公钥串，转化为公钥对象
     *
     * @param publicKey
     * @return 公钥对象
     */
    public static PublicKey createPublicKey(String publicKey) {
        PublicKey publickey = null;
        try {
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance("EC", new BouncyCastleProvider());
            publickey = keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publickey;
    }

    /**
     * 将Base64转码的私钥串，转化为私钥对象
     *
     * @param privateKey
     * @return
     */
    public static PrivateKey createPrivateKey(String privateKey) {
        PrivateKey publicKey = null;
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance("EC", new BouncyCastleProvider());
            publicKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static byte[] encrypt(byte[] data, PublicKey publicKey) {
        ECPublicKeyParameters localECPublicKeyParameters = null;

        if (publicKey instanceof BCECPublicKey) {
            BCECPublicKey localECPublicKey = (BCECPublicKey) publicKey;
            ECParameterSpec localECParameterSpec = localECPublicKey.getParameters();
            ECDomainParameters localECDomainParameters = new ECDomainParameters(localECParameterSpec.getCurve(),
                    localECParameterSpec.getG(), localECParameterSpec.getN());
            localECPublicKeyParameters = new ECPublicKeyParameters(localECPublicKey.getQ(), localECDomainParameters);
        }
        SM2Engine localSM2Engine = new SM2Engine();
        localSM2Engine.init(true, new ParametersWithRandom(localECPublicKeyParameters, new SecureRandom()));
        byte[] arrayOfByte2;
        try {
            arrayOfByte2 = localSM2Engine.processBlock(data, 0, data.length);
            return arrayOfByte2;
        } catch (InvalidCipherTextException e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据privateKey对加密数据encodeData，使用SM2解密
     *
     * @param encodeData
     * @param privateKey
     * @return
     */
    public static byte[] decrypt(byte[] encodeData, PrivateKey privateKey) {
        SM2Engine localSM2Engine = new SM2Engine();
        BCECPrivateKey sm2PriK = (BCECPrivateKey) privateKey;
        ECParameterSpec localECParameterSpec = sm2PriK.getParameters();
        ECDomainParameters localECDomainParameters = new ECDomainParameters(localECParameterSpec.getCurve(),
                localECParameterSpec.getG(), localECParameterSpec.getN());
        ECPrivateKeyParameters localECPrivateKeyParameters = new ECPrivateKeyParameters(sm2PriK.getD(),
                localECDomainParameters);
        localSM2Engine.init(false, localECPrivateKeyParameters);
        try {
            byte[] arrayOfByte3 = localSM2Engine.processBlock(encodeData, 0, encodeData.length);
            return arrayOfByte3;
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥签名
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] signByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
        // Signature sig = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), BouncyCastleProvider.PROVIDER_NAME);
        Signature sig = Signature.getInstance(OidUtil.OID_SM3withSM2, BouncyCastleProvider.PROVIDER_NAME);
        sig.initSign(privateKey);
        sig.update(data);
        byte[] ret = sig.sign();
        return ret;
    }

    /**
     * 公钥验签
     *
     * @param data
     * @param publicKey
     * @param signature
     * @return
     * @throws Exception
     */
    public static boolean verifyByPublicKey(byte[] data, PublicKey publicKey, byte[] signature) throws Exception {
        Signature sig = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), BouncyCastleProvider.PROVIDER_NAME);
        sig.initVerify(publicKey);
        sig.update(data);
        boolean ret = sig.verify(signature);
        return ret;
    }

    public static void main(String[] args) throws Exception {
        // KeyPair kayPair = genKeyPair();
        // PublicKey publicKey = (PublicKey)kayPair.getPublic();
        // PrivateKey privateKey = (PrivateKey)kayPair.getPrivate();
        // //得到base64编码的公钥/私钥字符串
        // String publicKeyString = Base64Util.encode(publicKey.getEncoded());
        // String privateKeyString = Base64Util.encode(privateKey.getEncoded());
        // FileUtil.storeFile(Constants.FILE_OUT_PATH + "sm2/pubKey.txt", publicKeyString.getBytes());
        // FileUtil.storeFile(Constants.FILE_OUT_PATH + "sm2/priKey.txt", privateKeyString.getBytes());
        // System.out.println("SM2密钥对存储成功！");

        String str = "123456";

        String pubKey = new String(FileUtil.getFile(Constants.FILE_OUT_PATH + "sm2/pubKey.txt"));
        String priKey = new String(FileUtil.getFile(Constants.FILE_OUT_PATH + "sm2/priKey.txt"));

        PublicKey publicKey = SM2Util.createPublicKey(pubKey);
        PrivateKey privateKey = SM2Util.createPrivateKey(priKey);

        byte[] encrypt = SM2Util.encrypt(str.getBytes(), publicKey);
        String encryptBase64Str = Base64.getEncoder().encodeToString(encrypt);
        System.out.println("加密数据：" + encryptBase64Str);

        byte[] decode = Base64.getDecoder().decode(encryptBase64Str);
        byte[] decrypt = SM2Util.decrypt(decode, privateKey);
        System.out.println("解密数据：" + new String(decrypt));

        byte[] sign = SM2Util.signByPrivateKey(str.getBytes(), privateKey);
        System.out.println("数据签名：" + Base64.getEncoder().encodeToString(sign));

        boolean b = SM2Util.verifyByPublicKey(str.getBytes(), publicKey, sign);
        System.out.println("数据验签：" + b);

    }
}
