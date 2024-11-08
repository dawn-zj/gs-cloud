package com.gs.common.pkcs.pkcs7;

import com.alibaba.fastjson.JSON;
import com.gs.common.define.Constants;
import com.gs.common.entity.crypto.SequenceEnvelop;
import com.gs.common.util.ConfigUtil;
import com.gs.common.util.FileUtil;
import com.gs.common.util.cert.CertUtil;
import com.gs.common.util.crypto.KeyUtil;
import com.gs.common.util.crypto.OidUtil;
import com.gs.common.util.pkcs.KeyStoreUtil;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

public class PKCS7Envelope {

    /**
     * cms制作数字信封，默认AES对称算法
     * @param content 要发送的内容
     * @param cert 接收方公钥证书
     * @return
     * @throws Exception
     */
    public static byte[] makeP7(byte[] content, X509Certificate cert) throws Exception {
        // 信封
        SequenceEnvelop seqEnv = new SequenceEnvelop();
        seqEnv.setVersion(new DERInteger(0));
        seqEnv.setContent(content, cert);
        ASN1Encodable sequence = seqEnv.toASN1Structure();

        // 创建Sequence
        ASN1EncodableVector p7Encodable = new ASN1EncodableVector();
        p7Encodable.add(new DERObjectIdentifier(PKCSObjectIdentifiers.envelopedData.toString()));
        p7Encodable.add(new DERTaggedObject(true, 0, sequence));
        DERSequence p7 = new DERSequence(p7Encodable);

        // 写入DERSequence数据
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DEROutputStream derOutputStream = new DEROutputStream(outputStream);
        derOutputStream.writeObject(p7);
        derOutputStream.flush();
        byte[] bs = outputStream.toByteArray();
        try {
            outputStream.close();
        } catch (IOException ex) {
        }
        return bs;

    }

    /**
     * cms解析数字信封
     * @param envData 数字信封数据
     * @param privateKey 接收者私钥
     * @return
     * @throws Exception
     */
    public static byte[] verifyP7(byte[] envData, PrivateKey privateKey) throws Exception {
        ASN1Sequence sequence = getSequenceEnvelop(envData);

        DERSet set = (DERSet)sequence.getObjectAt(1);
        ASN1Sequence contentSequence = (ASN1Sequence)sequence.getObjectAt(2);

        // 2. 接收者信息
        ASN1Sequence certSequence = (ASN1Sequence)set.getObjectAt(0);
        // 非对称加密算法
        ASN1Sequence algSequence = (ASN1Sequence)certSequence.getObjectAt(2);
        ASN1ObjectIdentifier algObj = (ASN1ObjectIdentifier)algSequence.getObjectAt(0);
        String algOid = algObj.toString();
        String signAlg = OidUtil.getSignHash(algOid);
        System.out.println(signAlg);

        // 非对称加密密文
        ASN1OctetString encObj = (ASN1OctetString)certSequence.getObjectAt(3);
        byte[] encData = encObj.getOctets();

        // 非对称解密，得到对称密钥
        byte[] symKeyData = KeyUtil.decrypt(privateKey, encData, signAlg);


        // 3. 加密内容
        // 对称加密算法
        ASN1Sequence symAlgSequence = (ASN1Sequence)contentSequence.getObjectAt(1);
        ASN1ObjectIdentifier symAlgObj = (ASN1ObjectIdentifier)symAlgSequence.getObjectAt(0);
        String symAlgOid = symAlgObj.toString();
        String symAlg = OidUtil.getSignHash(symAlgOid);
        System.out.println(symAlg);

        // 对称加密密文
        DERTaggedObject symEncObj = (DERTaggedObject)contentSequence.getObjectAt(2);
        byte[] symEncData = symEncObj.getEncoded();

        // 对称解密，得到原文
        SecretKeySpec keySpec = new SecretKeySpec(symKeyData, "AES");

        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, keySpec);
        return c.doFinal(symEncData);

    }

    private static ASN1Sequence getSequenceEnvelop(byte[] envData) throws Exception {
        ASN1InputStream asn1InputStream = new ASN1InputStream(envData);
        ASN1Sequence obj = (ASN1Sequence) asn1InputStream.readObject();

        DERTaggedObject contextObj = (DERTaggedObject)obj.getObjectAt(1);
        ASN1Sequence sequence = (ASN1Sequence)contextObj.getObject();
        return sequence;
    }

    public static void main(String[] args) throws Exception {
        // 必须执行此行，读取oid文件
        ConfigUtil.getInstance().addToOidUtil();

        String password = "11111111";
        String pfxPath = Constants.FILE_PATH + "/key/rsa/server.pfx";
        String pfxCertPath = Constants.FILE_PATH + "/key/rsa/server.cer";

        byte[] file = FileUtil.getFile(pfxCertPath);
        X509Certificate x509Certificate = CertUtil.getX509Certificate(file);
        byte[] bytes = PKCS7Envelope.makeP7("123".getBytes(), x509Certificate);
        FileUtil.storeFile("F:/1.asn1", bytes);

        PrivateKey privateKey = KeyStoreUtil.loadKey(password, Constants.PFX_SUFFIX, FileUtil.getFile(pfxPath));
        byte[] plain = verifyP7(bytes, privateKey);
        System.out.println(new String(plain));
    }
}
