package com.gs.common.util.crypto;

import com.gs.common.define.Constants;
import com.gs.common.entity.crypto.SM2PublicKey;
import com.gs.common.exception.BaseException;
import com.gs.common.util.FileUtil;
import com.gs.common.util.HexUtil;
import org.bouncycastle.asn1.*;
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
        // 1.获取实例
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
        // 2.指定曲线参数名称
        ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
        kpg.initialize(sm2Spec);
        // 3.生成密钥对,BC库使用的公钥=64个字节+1个字节（04标志位），BC库使用的私钥=32个字节
        KeyPair keyPair = kpg.generateKeyPair();
        return keyPair;
    }

    /**
     * 将Base64转码的公钥串，转化为公钥对象
     *
     * @param publicKey Base64格式的公钥串
     * @return 公钥对象
     */
    public static PublicKey createPublicKey(String publicKey) {
        PublicKey publickey = createPublicKey(Base64.getDecoder().decode(publicKey));
        return publickey;
    }

    public static PublicKey createPublicKey(byte[] publicKeyData) {
        PublicKey publickey = null;
        try {
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyData);
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
     * @param privateKey Base64格式的私钥串
     * @return
     */
    public static PrivateKey createPrivateKey(String privateKey) {
        PrivateKey publicKey = createPrivateKey(Base64.getDecoder().decode(privateKey));
        return publicKey;
    }

    public static PrivateKey createPrivateKey(byte[] privateKeyData) {
        PrivateKey publicKey = null;
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyData);
            KeyFactory keyFactory = KeyFactory.getInstance("EC", new BouncyCastleProvider());
            publicKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    /**
     * SM2加密
     * @param data 待加密的数据
     * @param publicKey 公钥
     * @return 密文，格式：C1||C2||C3，以04开始
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey) {
        // SM2的密文有两种排列方式0-C1C2C3；1-C1C3C2，《0003规范》标准排列方式是1，但是BC库的实现是0
        // todo 要想支持其他模式，需要自行扩展，参考https://blog.csdn.net/seeyouagen/article/details/115727307
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
     * SM2解密
     *
     * @param encodeData C1||C2||C3格式密文，C1以04开始
     * @param privateKey 私钥
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
     *
     * @param data 原文
     * @param publicKey 公钥
     * @return der编码的加密结果，符合0009规范
     */
    public static DERSequence encryptWith0009(byte[] data, PublicKey publicKey) throws Exception {
        byte[] encDataNoDer = encrypt(data, publicKey);
        DERSequence encData = genSM2EncryptedWith0009(encDataNoDer, 0);
        return encData;
    }

    /**
     *
     * @param encData der编码的密文，符合0009规范
     * @param publicKey 公钥
     * @return 解密结果
     */
    public static byte[] decryptWith0009(byte[] encData, PrivateKey privateKey) throws Exception {
        byte[] encDataNoDer = getSM2DecryptedBy0009(encData, 0, true);
        // todo 偶尔出错
        byte[] data = decrypt(encDataNoDer, privateKey);
        return data;
    }

    /**
     * 将指定加密模式的密文，转为0009规范密文
     * @param data 密文
     * @param cipherMode 密文的加密模式，0:C1||C2||C3, 1:C1||C3||C2
     * @return 0009规范密文，der编码
     * @throws Exception
     */
    public static DERSequence genSM2EncryptedWith0009(byte[] data, int cipherMode) throws Exception {
        // 兼容C1公钥部分带04
        byte[] encData = new byte[data.length];
        if (data[0] == 0x04) {
            encData = new byte[data.length - 1];
            System.arraycopy(data, 1, encData, 0, data.length - 1);
        } else {
            System.arraycopy(data, 0, encData, 0, data.length);
        }

        // 拆分成0009规范所需的值
        byte[] kxData = new byte[32];
        byte[] kyData = new byte[32];
        byte[] hashData = new byte[32];
        byte[] cipherData = new byte[encData.length - 96]; // 减去上面3部分，剩下的全是密文

        System.arraycopy(encData, 0, kxData, 0, kxData.length);
        System.arraycopy(encData, kxData.length, kyData, 0, kyData.length);
        if (cipherMode == 0) {// C1||C2||C3格式密文
            System.arraycopy(encData, kxData.length + kyData.length, cipherData, 0, cipherData.length);
            System.arraycopy(encData, kxData.length + kyData.length + cipherData.length, hashData, 0, hashData.length);
        } else if (cipherMode == 1) {// C1||C3||C2格式密文
            System.arraycopy(encData, kxData.length + kyData.length, hashData, 0, hashData.length);
            System.arraycopy(encData, kxData.length + kyData.length + hashData.length, cipherData, 0, cipherData.length);
        } else {
            throw new BaseException("unknown cipherMode");
        }

        // 组装0009规范的asn1格式
        return genSM2EncryptedWith0009(kxData, kyData, hashData, cipherData);
    }

    /**
     * 将各部分数剧组装成der编码的0009规范密文
     * @param kxData x分量
     * @param kyData y分量
     * @param hashData hash值
     * @param cipherData 密文
     * @return 0009规范密文
     * @throws Exception
     */
    private static DERSequence genSM2EncryptedWith0009(byte[] kxData, byte[] kyData, byte[] hashData, byte[] cipherData) throws Exception {
        // 组装0009规范的asn1格式
        DERInteger kx,ky;
        DEROctetString derHash = new DEROctetString(hashData);
        DEROctetString derCipher = new DEROctetString(cipherData);

        // 针对Integer类型的xy分量，如果数字的第1bit为1，通常会在编码前补上一个0字节(byte)，也就是0x00
        if ((kxData[0] & 0x80) == 0x80) {
            byte[] kxDataTmp = new byte[kxData.length + 1];
            kxDataTmp[0] = 0;
            System.arraycopy(kxData, 0, kxDataTmp, 1, kxData.length);
            kx = new DERInteger(kxDataTmp);
        } else {
            kx = new DERInteger(kxData);
        }

        if ((kyData[0] & 0x80) == 0x80) {
            byte[] kyDataTmp = new byte[kyData.length + 1];
            kyDataTmp[0] = 0;
            System.arraycopy(kyData, 0, kyDataTmp, 1, kyData.length);
            ky = new DERInteger(kyDataTmp);
        } else {
            ky = new DERInteger(kyData);
        }

        ASN1EncodableVector derVec = new ASN1EncodableVector();
        derVec.add(kx);
        derVec.add(ky);
        derVec.add(derHash);
        derVec.add(derCipher);
        DERSequence seq = new DERSequence(derVec);

        return seq;
    }

    /**
     * 将0009规范的密文，按指定的加密模式拼接输出
     * @param encData 0009格式的密文
     * @param cipherMode 0:C1||C2||C3, 1:C1||C3||C2
     * @param c1with04 C1部分是否以04开始
     * @return 指定模式的密文
     * @throws Exception
     */
    public static byte[] getSM2DecryptedBy0009(byte[] encData, int cipherMode, boolean c1with04) throws Exception {
        try {
            ASN1InputStream asn1InputStream = new ASN1InputStream(encData);
            ASN1Primitive asn1Primitive = asn1InputStream.readObject();
            ASN1Sequence obj = (ASN1Sequence) asn1Primitive;

            // 获取4部分的值
            ASN1Integer x_obj = (ASN1Integer)obj.getObjectAt(0);
            byte[] kxData = x_obj.getValue().toByteArray();

            ASN1Integer y_obj = (ASN1Integer)obj.getObjectAt(1);
            byte[] kyData = y_obj.getValue().toByteArray();

            DEROctetString hash_obj = (DEROctetString)obj.getObjectAt(2);
            byte[] hashData = hash_obj.getOctets();

            DEROctetString cipher_obj = (DEROctetString)obj.getObjectAt(3);
            byte[] cipherData = cipher_obj.getOctets();

            byte[] encData_no_der = new byte[32 + 32 + 32 + cipherData.length];
            // 若xy分量发生了补位，就截掉
            if (kxData.length == 33 && kxData[0] == 0) {
                System.arraycopy(kxData, 1, encData_no_der, 0, 32);
            } else if (kxData.length == 32) {
                System.arraycopy(kxData, 0, encData_no_der, 0, kxData.length);
            } else {
                throw new BaseException("x分量长度为：" + kxData.length + ", 无法处理");
            }

            if (kyData.length == 33 && kyData[0] == 0) {
                System.arraycopy(kyData, 1, encData_no_der, kxData.length, 32);
            } else if (kyData.length == 32) {
                System.arraycopy(kyData, 0, encData_no_der, kxData.length, kyData.length);
            } else {
                throw new BaseException("y分量长度为：" + kxData.length + ", 无法处理");
            }

            // 转换为指定模式的密文
            if (cipherMode == 0) { // 转为C1||C2||C3格式
                System.arraycopy(cipherData, 0, encData_no_der, 64, cipherData.length);
                System.arraycopy(hashData, 0, encData_no_der, 64 + cipherData.length, hashData.length);
            } else if (cipherMode == 1) {// 转为C1||C3||C2格式
                System.arraycopy(hashData, 0, encData_no_der, 64, hashData.length);
                System.arraycopy(cipherData, 0, encData_no_der, 64 + hashData.length, cipherData.length);
            } else {
                throw new BaseException("unknown cipherMode");
            }

            // 按情况给C1部分增加04标识
            byte[] all = new byte[encData_no_der.length];
            if (c1with04) {
                all = new byte[encData_no_der.length + 1];
                all[0] = 04;
                System.arraycopy(encData_no_der, 0, all, 1, encData_no_der.length);
            } else {
                System.arraycopy(encData_no_der, 0, all, 0, encData_no_der.length);
            }
            return all;

        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    /**
     * 生成SM2密钥对保护数据格式
     * @param publicKey 外部公钥
     * @param symmAlg 对称加密算法
     * @return
     * @throws Exception
     */
    public static DERSequence genSM2ProtectWith0009(PublicKey publicKey, String symmAlg, PrivateKey privateKey) throws Exception {
        // 1. 生成SM2密钥对
        KeyPair keyPair = genKeyPair();
        byte[] priKeyData = keyPair.getPrivate().getEncoded();

        byte[] pubKeyData = keyPair.getPublic().getEncoded();
        SM2PublicKey sm2PublicKey = new SM2PublicKey(pubKeyData);
        DERBitString derSM2PublicKey = new DERBitString(sm2PublicKey.getEncoded4ex());

        // 2. 生成对称密钥
        byte[] symmetricKey = null;
        byte[] priKeyEncData = null;
        ASN1ObjectIdentifier derSymmAlg = null;
        DERSequence derSM2Cipher = null;
        switch (symmAlg) {
            case "SM4":
            default:
                symmAlg = "SM4";
                // 2.1 将SM2私钥进行对称加密
                symmetricKey = KeyUtil.genRandom(16);
                priKeyEncData = SM4Util.ecb_encrypt(priKeyData, symmetricKey);

                // 2.2 将对称密钥用外部公钥进行SM2加密
                derSM2Cipher = encryptWith0009(symmetricKey, publicKey);
                decryptWith0009(derSM2Cipher.getEncoded(), privateKey);

        }
        derSymmAlg = new ASN1ObjectIdentifier(OidUtil.getOid(symmAlg));
        ASN1EncodableVector derAlgVec = new ASN1EncodableVector();
        derAlgVec.add(derSymmAlg);
        DERSequence algSequence = new DERSequence(derAlgVec);

        DERBitString derBitString = new DERBitString(priKeyEncData);

        // 组装0009规范的asn1格式
        ASN1EncodableVector derVec = new ASN1EncodableVector();
        derVec.add(algSequence);
        derVec.add(derSM2Cipher);
        derVec.add(derSM2PublicKey);
        derVec.add(derBitString);
        DERSequence seq = new DERSequence(derVec);

        return seq;
    }

    public static KeyPair parseSM2ProtectWith0009(PrivateKey privateKey, DERSequence sequence) throws Exception {
        ASN1Sequence algSequence = (ASN1Sequence) sequence.getObjectAt(0);
        ASN1Sequence symEncryptedKey = (ASN1Sequence) sequence.getObjectAt(1);
        ASN1BitString sm2PublicKey = (ASN1BitString) sequence.getObjectAt(2);
        ASN1BitString sm2EncryptedPrivateKey = (ASN1BitString) sequence.getObjectAt(3);

        // 1.SM2解密，解出对称密钥
        byte[] symKey = SM2Util.decryptWith0009(symEncryptedKey.getEncoded(), privateKey);

        // 2.对称加密算法
        String algOid = algSequence.getObjectAt(0).toString();
        System.out.println(algOid);
        String signHash = OidUtil.getSignHash(algOid);

        byte[] sm2PriKey = null;
        switch (signHash) {
            case "SM4":
            default:
                sm2PriKey = SM4Util.ecb_decrypt(sm2EncryptedPrivateKey.getEncoded(), symKey);
        }

        byte[] bytes = sm2PublicKey.getBytes();
        System.out.println(sm2PublicKey.toString());
        System.out.println(HexUtil.byte2Hex(bytes));
        // 转为 BCECPublicKey BCECPrivateKey
        PublicKey pubKey = createPublicKey(bytes);
        PrivateKey priKey = createPrivateKey(sm2PriKey);
        KeyPair keyPair = new KeyPair(pubKey, priKey);
        return keyPair;
    }

    /**
     * 私钥签名
     *
     * @param data 原文
     * @param privateKey 私钥
     * @return 签名值
     * @throws Exception
     */
    public static byte[] signByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
        // Signature sig = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), BouncyCastleProvider.PROVIDER_NAME);
        Signature sig = Signature.getInstance(OidUtil.getOid(Constants.SM3_SM2), BouncyCastleProvider.PROVIDER_NAME);
        sig.initSign(privateKey);
        sig.update(data);
        byte[] ret = sig.sign();
        return ret;
    }

    /**
     * 公钥验签
     *
     * @param data 原文
     * @param publicKey 公钥
     * @param signature 签名值
     * @return 验签结果
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
        String encryptHex = HexUtil.byte2Hex(encrypt);
        // 示例：040938b0c9c04a0c399dad46ba9d30780eca9921e438265206c6081af6c05499ff2d89ad022516198548175e892d57fe9de7e95483c4377f18bf8f276a7004834ec13f0fcb7c94291ad75b7b97d7e24cb04cdb1538e18d7324e3b542e291affb0e3840f3412891
        System.out.println("加密数据：" + encryptHex);

        byte[] decode = HexUtil.hex2Byte(encryptHex);
        byte[] decrypt = SM2Util.decrypt(decode, privateKey);
        System.out.println("解密数据：" + new String(decrypt));

        byte[] sign = SM2Util.signByPrivateKey(str.getBytes(), privateKey);
        System.out.println("数据签名：" + Base64.getEncoder().encodeToString(sign));

        boolean b = SM2Util.verifyByPublicKey(str.getBytes(), publicKey, sign);
        System.out.println("数据验签：" + b);

    }
}
