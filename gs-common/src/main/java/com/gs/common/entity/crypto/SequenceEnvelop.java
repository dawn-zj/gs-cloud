package com.gs.common.entity.crypto;

import com.gs.common.define.Constants;
import com.gs.common.util.ConfigUtil;
import com.gs.common.util.crypto.KeyUtil;
import com.gs.common.util.crypto.OidUtil;
import com.gs.common.util.crypto.RSAUtil;
import com.gs.common.util.crypto.SM2Util;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.yaml.snakeyaml.scanner.Constant;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Locale;

public class SequenceEnvelop implements ASN1Encodable {
    /**
     * 版本(这一标准中版本应该为0)
     */
    private DERInteger version;
    /**
     *
     */
    private DERSet recipientInfos;
    /**
     * 加密内容
     */
    private DERSequence content;

    private String symAlg = "AES";
    private String symAlgOid = "2.16.840.1.101.3.4.1.2"; // AES128_CBC

    public SequenceEnvelop() {

    }

    public SequenceEnvelop(DERSet recipientInfos, ASN1EncodableVector content) {
        this(0, recipientInfos, content);
    }

    public SequenceEnvelop(int version, DERSet recipientInfos, ASN1EncodableVector content) {
        this.version = new DERInteger(version);
        this.recipientInfos =recipientInfos;
        this.content = new DERSequence(content);
    }

    public ASN1Encodable toASN1Structure() {
        return new DERSequence(new ASN1Encodable[]{version, recipientInfos, content});
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return null;
    }

    public void setVersion(DERInteger version) {
        this.version = version;
    }

    public void setContent(byte[] content, X509Certificate cert) throws Exception {
        // 生成对称密钥，默认AES算法对称加密
        byte[] symKeyData = null;
        SecretKey key = null;
        byte[] symEncData = null;
        if ("SM4".equalsIgnoreCase(symAlg)) {
            // todo 优化点：待支持SM4
        } else {
            symKeyData = new byte[16];
            SecureRandom ivRandom = new SecureRandom();
            ivRandom.nextBytes(symKeyData);
            key = new SecretKeySpec(symKeyData, symAlg);

            // 此处不指定provider，使用java默认的加密模式，例如AES的是AES/ECB/PKCS5padding
            Cipher c = Cipher.getInstance(symAlg);
            c.init(Cipher.ENCRYPT_MODE, key);
            symEncData = c.doFinal(content);// 加密消息
        }

        // 对称算法sequence
        DERObjectIdentifier objectIdentifier = new DERObjectIdentifier(symAlgOid);
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(objectIdentifier, null);

        ASN1EncodableVector seqEncodable = new ASN1EncodableVector();
        // 标识：1.2.840.113549.1.7.1
        seqEncodable.add(new DERObjectIdentifier(PKCSObjectIdentifiers.data.toString()));
        seqEncodable.add(algorithmIdentifier);
        seqEncodable.add(new DERTaggedObject(false, 0, new DEROctetString(symEncData)));

        this.content = new DERSequence(seqEncodable);

        // 制作接收者信息，用证书公钥加密对称密钥
        setRecipientInfos(cert, symKeyData);
    }

    private void setRecipientInfos(X509Certificate cert, byte[] symKeyData) throws Exception {
        // 1：版本
        DERInteger version = new DERInteger(0);

        // 2：DN和SN
        IssuerAndSerialNumber issuerAndSerialNumber = new IssuerAndSerialNumber((X509Principal) cert.getIssuerDN(), new DERInteger(cert.getSerialNumber()));

        // 3：证书加密算法标识
        String sigHashAlg = cert.getSigAlgName();
        String sigAlgOID = OidUtil.getSignAlgOid(sigHashAlg);

        DERObjectIdentifier objectIdentifier = new DERObjectIdentifier(sigAlgOID);
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(objectIdentifier, null);

        // 4：证书公钥加密对称密钥
        byte[] encryptData = null;
        String signHash = OidUtil.getSignHash(sigAlgOID);
        if (Constants.RSA.equals(signHash)) {
            encryptData = KeyUtil.encrypt(cert.getPublicKey(), symKeyData, signHash);
        } else {
            throw new Exception("not support " + signHash);
        }
        DEROctetString encDer = new DEROctetString(encryptData);

        ASN1EncodableVector asn1Vector = new ASN1EncodableVector();
        asn1Vector.add(version);
        asn1Vector.add(issuerAndSerialNumber.toASN1Object());
        asn1Vector.add(algorithmIdentifier);
        asn1Vector.add(encDer);

        DERSequence sequence = new DERSequence(asn1Vector);

        // todo 优化点：目前只支持一个接收人
        DERSet set = new DERSet(sequence);
        this.recipientInfos = set;
    }
}

