package com.gs.webserver.controller.tool;

import com.gs.common.define.Constants;
import com.gs.common.pkcs.pkcs7.PKCS7Envelope;
import com.gs.common.util.FileUtil;
import com.gs.common.util.StringUtil;
import com.gs.common.util.base64.Base64Util;
import com.gs.common.util.cert.CertUtil;
import com.gs.common.util.crypto.KeyUtil;
import com.gs.common.util.crypto.RSAUtil;
import com.gs.common.util.crypto.SM2Util;
import com.gs.common.util.pkcs.KeyStoreUtil;
import com.gs.webserver.entity.to.response.CommonResTo;
import com.gs.webserver.entity.to.response.ResponseTo;
import com.gs.webserver.entity.to.response.key.KeyResTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
 * 密钥
 * @Author Zhang Juan
 * @Date 2024/3/22 11:18
 */
@RestController
@RequestMapping("/tool/key")
public class KeyController {

    /**
     * 生成RSA密钥对，PKCS#8格式
     * @param keySize 密钥长度 | 1024 | 2048
     * @return
     * @throws Exception
     */
    @PostMapping("/genRsaKeyPair")
    public ResponseTo<KeyResTo> genRsaKeyPairTest(@NotNull int keySize) throws Exception {
        KeyPair kayPair = RSAUtil.genRSAKeyPair(keySize);
        KeyResTo keyResTo = genKeyResTo(kayPair);
        return ResponseTo.success(keyResTo);
    }

    /**
     * 生成RSA密钥对，PKCS#8格式,以PEM/Base64格式输出
     * @param keySize 密钥长度 | 1024 | 2048
     * @return
     * @throws Exception
     */
    @PostMapping("/genRsaKeyPairPem")
    public ResponseTo<KeyResTo> genRsaKeyPairPemTest(@NotNull int keySize) throws Exception {
        KeyPair kayPair = RSAUtil.genRSAKeyPair(keySize);
        KeyResTo keyResTo = genKeyResTo(kayPair, true);
        return ResponseTo.success(keyResTo);
    }

    /**
     * 校验RSA密钥对
     * @param pubKeyB64 公钥base64
     * @param priKeyB64 私钥base64
     * @return
     * @throws Exception
     */
    @PostMapping("/rsaCheckKeyPair")
    public ResponseTo<Boolean> rsaCheckKeyPairTest(@NotNull String pubKeyB64, @NotNull String priKeyB64) throws Exception {
        boolean checkKeyPair = RSAUtil.checkKeyPair(pubKeyB64, priKeyB64);
        return ResponseTo.success(checkKeyPair);
    }

    /**
     * 从私钥中提取公钥
     * @param pubKeyB64 公钥base64
     * @param priKeyB64 私钥base64
     * @return
     * @throws Exception
     */
    @PostMapping("/genRSAPubKeyByPriKey")
    public ResponseTo<String> genRSAPubKeyByPriKeyTest(@NotNull String priKeyB64) throws Exception {
        PublicKey publicKey = RSAUtil.genPublicKeyByPrivateKey(priKeyB64);
        String pubKeyB64 = Base64Util.encode(publicKey.getEncoded());
        return ResponseTo.success(pubKeyB64);
    }

    /**
     * base64格式公钥转pem格式
     * @param pubKeyB64 公钥base64
     * @return
     * @throws Exception
     */
    @PostMapping("/pubKeyB64ToPem")
    public ResponseTo<String> pubKeyB64ToPemTest(@NotNull String pubKeyB64) throws Exception {
        String pemPubKey = RSAUtil.getPemPubKey(pubKeyB64);
        return ResponseTo.success(pemPubKey);
    }

    /**
     * base64格式私钥转pem格式
     * @param priKeyB64
     * @param keyType 密钥类型，1:PKCS#1, 8:PKCS#8 |8
     * @return
     * @throws Exception
     */
    @PostMapping("/priKeyB64ToPem")
    public ResponseTo<String> priKeyB64ToPemTest(@NotNull String priKeyB64, @NotNull int keyType) throws Exception {
        String pemPriKey = RSAUtil.getPemPriKey(priKeyB64,true, keyType);
        return ResponseTo.success(pemPriKey);
    }

    /**
     * RSA加密
     * @param plain 原文
     * @param publicKeyB64 公钥
     * @return 加密值
     * @throws Exception
     */
    @PostMapping("/rsaEncrypt")
    public ResponseTo<CommonResTo> rsaEncryptTest(@NotNull String plain, @NotNull String publicKeyB64) throws Exception {
        // 加载RSA公钥
        PublicKey publicKey = RSAUtil.generateP8PublicKey(Base64Util.decode(publicKeyB64));
        byte[] encrypt = KeyUtil.encrypt(publicKey, plain.getBytes(), Constants.RSA);

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(encrypt));
        return ResponseTo.success(commonTo);
    }

    /**
     * RSA解密
     * @param encryptB64 待解密数据
     * @param privateKeyB64 私钥
     * @return 原文
     * @throws Exception
     */
    @PostMapping("/rsaDecrypt")
    public ResponseTo<CommonResTo> rsaDecryptTest(@NotNull String encryptB64, @NotNull String privateKeyB64) throws Exception {
        byte[] encrypt = Base64Util.decode(encryptB64);

        // 加载RSA私钥
        PrivateKey privateKey = RSAUtil.generateP8PrivateKey(Base64Util.decode(privateKeyB64));
        byte[] decrypt = KeyUtil.decrypt(privateKey, encrypt, Constants.RSA);

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(StringUtil.getString(decrypt));
        return ResponseTo.success(commonTo);
    }

    /**
     * RSA裸签名
     * @param plain 待签原文
     * @param privateKeyB64 私钥
     * @param signHashAlg 签名摘要算法 | SHA1WITHRSA | SHA256WITHRSA
     * @return
     * @throws Exception
     */
    @PostMapping("/rsaRawSign")
    public ResponseTo<CommonResTo> rsaRawSignTest(@NotNull String plain, @NotNull String privateKeyB64, @NotNull String signHashAlg) throws Exception {
        // 加载RSA私钥
        PrivateKey privateKey = RSAUtil.generateP8PrivateKey(Base64Util.decode(privateKeyB64));
        byte[] signed = KeyUtil.rawSign(null, privateKey, plain.getBytes(),  signHashAlg, null);

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(signed));
        return ResponseTo.success(commonTo);
    }

    /**
     * RSA验裸签
     * @param plain 原文
     * @param signB64 裸签值
     * @param publicKeyB64 公钥
     * @param signHashAlg 签名摘要算法 | SHA1WITHRSA | SHA256WITHRSA
     * @return 验签结果
     * @throws Exception
     */
    @PostMapping("/rsaRawVerify")
    public ResponseTo<Boolean> rsaRawVerifyTest(@NotNull String plain, @NotNull String signB64,
                                                @NotNull String publicKeyB64, @NotNull String signHashAlg) throws Exception {
        byte[] signed = Base64Util.decode(signB64);

        // 加载RSA公钥
        PublicKey publicKey = RSAUtil.generateP8PublicKey(Base64Util.decode(publicKeyB64));
        boolean result = KeyUtil.rawSignVerify(publicKey, plain.getBytes(), signed, signHashAlg, null);

        return ResponseTo.success(result);
    }

    /**
     * RSA detached签名
     * @param certFile
     * @param plain
     * @return
     * @throws Exception
     */
    @PostMapping("/rsaDetachedSign")
    public ResponseTo<CommonResTo> detachedSignTest(@RequestParam("certFile") MultipartFile certFile, @NotNull String plain) throws Exception {
        X509Certificate cert = CertUtil.getX509Certificate(certFile.getBytes());
        // 签名
        byte[] signed = KeyUtil.detachedSign(plain.getBytes(), cert);

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(signed));
        return ResponseTo.success(commonTo);
    }

    /**
     * RSA 验detached签名
     * @param certFile
     * @param plain
     * @return
     * @throws Exception
     */
    @PostMapping("/rsaDetachedVerify")
    public ResponseTo<CommonResTo> detachedVerifyTest(@RequestParam("pfxFile") MultipartFile pfxFile, String password,
                                 @NotNull String signB64) throws Exception {
        byte[] signed = Base64Util.decode(signB64);
        // 验签
        PrivateKey privateKey = KeyStoreUtil.loadKey(password, Constants.PFX_SUFFIX, pfxFile.getBytes());
        byte[] data = KeyUtil.detachedVerify(signed, privateKey);

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(StringUtil.getString(data));
        return ResponseTo.success(commonTo);
    }

    /**
     * 生成SM2密钥对
     * @return
     * @throws Exception
     */
    @PostMapping("/genSM2KeyPair")
    public ResponseTo<KeyResTo> genSM2KeyPairTest() throws Exception {
        KeyPair kayPair = SM2Util.genKeyPair();
        KeyResTo keyResTo = genKeyResTo(kayPair);
        return ResponseTo.success(keyResTo);
    }

    /**
     * SM2加密(GM/T 0003)
     * @param plain 原文
     * @param publicKeyB64 公钥
     * @return 加密值
     * @throws Exception
     */
    @PostMapping("/sm2Encrypt")
    public ResponseTo<CommonResTo> encryptWithBC(@NotNull String plain, @NotNull String publicKeyB64) throws Exception {
        // bc库sm2加密
        PublicKey publicKey = SM2Util.createPublicKey(publicKeyB64);
        byte[] bytes = SM2Util.encrypt(plain.getBytes(), publicKey);

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(bytes));
        return ResponseTo.success(commonTo);

    }

    /**
     * SM2解密(GM/T 0003)
     * @param encryptB64 加密值
     * @param privateKeyB64 私钥
     * @return 原文
     * @throws Exception
     */
    @PostMapping("/sm2Decrypt")
    public ResponseTo<CommonResTo> decryptWithBC(@NotNull String encryptB64, @NotNull String privateKeyB64) throws Exception {
        byte[] encrypt = Base64Util.decode(encryptB64);
        // bc库sm2解密
        PrivateKey privateKey = SM2Util.createPrivateKey(privateKeyB64);
        byte[] decrypt = SM2Util.decrypt(encrypt, privateKey);

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(StringUtil.getString(decrypt));
        return ResponseTo.success(commonTo);
    }

    /**
     * SM2加密(GM/T 0009)
     * @param plain 原文
     * @param publicKeyB64 公钥
     * @return 加密值
     * @throws Exception
     */
    @PostMapping("/sm2Encrypt0009")
    public ResponseTo<CommonResTo> encryptWith0009(@NotNull String plain, @NotNull String publicKeyB64) throws Exception {
        // bc库sm2加密
        PublicKey publicKey = SM2Util.createPublicKey(publicKeyB64);
        byte[] bytes = SM2Util.encryptWith0009(plain.getBytes(), publicKey).getEncoded();

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(bytes));
        return ResponseTo.success(commonTo);

    }

    /**
     * SM2解密(GM/T 0009)
     * @param encryptB64 加密值
     * @param privateKeyB64 私钥
     * @return 原文
     * @throws Exception
     */
    @PostMapping("/sm2Decrypt0009")
    public ResponseTo<CommonResTo> decryptWith0009(@NotNull String encryptB64, @NotNull String privateKeyB64) throws Exception {
        byte[] encrypt = Base64Util.decode(encryptB64);
        // bc库sm2解密
        PrivateKey privateKey = SM2Util.createPrivateKey(privateKeyB64);
        byte[] decrypt = SM2Util.decryptWith0009(encrypt, privateKey);

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(StringUtil.getString(decrypt));
        return ResponseTo.success(commonTo);
    }

    /**
     * SM2 0003->0009
     * @param encB64 GM/T 0003的加密值
     * @param cipherMode GM/T 0003的密文格式，0:C1C2C3，1:C1C3C2  | 0 | 1
     * @return
     * @throws Exception
     */
    @PostMapping("/sm20003To0009")
    public ResponseTo<CommonResTo> sm20003To0009(@NotNull String encB64, @NotNull int cipherMode) throws Exception {
        byte[] encDataNoDer = Base64Util.decode(encB64);
        byte[] encData = SM2Util.genSM2EncryptedWith0009(encDataNoDer, cipherMode).getEncoded();

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(encData));
        return ResponseTo.success(commonTo);
    }

    /**
     * SM2 0009->0003
     * @param encB64 GM/T 0009的加密值
     * @param cipherMode GM/T 0003的密文格式 | 0 | 1
     * @param c1with04 C1部分是否带04 | true
     * @return
     * @throws Exception
     */
    @PostMapping("/sm20009To0003")
    public ResponseTo<CommonResTo> sm20009To0003(@NotNull String encB64, @NotNull int cipherMode, @NotNull boolean c1with04) throws Exception {
        byte[] encDataDer = Base64Util.decode(encB64);
        byte[] encData = SM2Util.getSM2DecryptedBy0009(encDataDer, cipherMode, c1with04);

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(encData));
        return ResponseTo.success(commonTo);
    }

    /**
     * 制作数字信封
     * @param pfxFile pfx文件
     * @param password pfx文件密码
     * @param plain 原文
     * @return
     * @throws Exception
     */
    @PostMapping("/makeEnvelop")
    public ResponseTo<CommonResTo> makeEnvelop(@RequestParam("pfxFile") MultipartFile pfxFile, String password,
                                               @NotNull String plain) throws  Exception {
        byte[] certData = KeyStoreUtil.getCertFromPfx(password, pfxFile.getBytes());
        X509Certificate cert = CertUtil.getX509Certificate(certData);
        // 制作数字信封
        byte[] bytes = PKCS7Envelope.makeP7(plain.getBytes(), cert);

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(bytes));
        return ResponseTo.success(commonTo);
    }

    /**
     * 解数字信封
     * @param pfxFile pfx文件
     * @param password pfx文件密码
     * @param envB64 信封数据
     * @return
     * @throws Exception
     */
    @PostMapping("/parseEnvelop")
    public ResponseTo<CommonResTo> parseEnvelop(@RequestParam("pfxFile") MultipartFile pfxFile, String password,
                                                @NotNull String envB64) throws Exception {
        byte[] envData = Base64Util.decode(envB64);
        // 解数字信封
        PrivateKey privateKey = KeyStoreUtil.loadKey(password, Constants.PFX_SUFFIX, pfxFile.getBytes());
        byte[] bytes = PKCS7Envelope.verifyP7(envData, privateKey);

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(StringUtil.getString(bytes));
        return ResponseTo.success(commonTo);
    }

    private KeyResTo genKeyResTo(KeyPair kayPair) {
        return genKeyResTo(kayPair, false);
    }

    private KeyResTo genKeyResTo(KeyPair kayPair, boolean isPem) {
        PublicKey publicKey = kayPair.getPublic();
        PrivateKey privateKey = kayPair.getPrivate();
        //得到base64编码的公钥/私钥字符串
        String publicKeyString = Base64Util.encode(publicKey.getEncoded());
        String privateKeyString = Base64Util.encode(privateKey.getEncoded());

        if (isPem) {
            // RFC1421，一种基于 base64 的编码格式，带头尾注释且每64个为一行
            publicKeyString = RSAUtil.getPemPubKey(publicKeyString);
            privateKeyString = RSAUtil.getPemPriKey(privateKeyString);
        }

        KeyResTo keyResTo = new KeyResTo();
        keyResTo.setPublicKeyB64(publicKeyString);
        keyResTo.setPrivateKeyB64(privateKeyString);
        return keyResTo;

    }

    /**
     * 生成SM2密钥对保护数据
     * @param publicKeyB64 签名公钥
     * @return SM2加密密钥对的保护数据
     * @throws Exception
     */
//    @PostMapping("/genSM2ProtectKeyPair")
//    public ResponseTo<CommonResTo> genSM2ProtectKeyPairTest(@NotNull String publicKeyB64) throws Exception {
//        PublicKey publicKey = SM2Util.createPublicKey(publicKeyB64);
//        DERSequence seq = SM2Util.genSM2ProtectWith0009(publicKey, "SM4");
//
//        CommonResTo commonTo = new CommonResTo();
//        commonTo.setResult(Base64Util.encode(seq.getEncoded()));
//        return ResponseTo.success(commonTo);
//    }

    /**
     * 解析SM2密钥对保护数据
     * @return
     * @throws Exception
     */
    @PostMapping("/genSM2ProtectKeyPair")
    public ResponseTo<KeyResTo> parseSM2ProtectKeyPairTest(@NotNull String privateKeyB64) throws Exception {
        KeyPair kayPair = SM2Util.genKeyPair();
        KeyResTo keyResTo = genKeyResTo(kayPair);
        return ResponseTo.success(keyResTo);
    }
}
