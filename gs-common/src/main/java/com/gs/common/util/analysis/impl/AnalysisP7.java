package com.gs.common.util.analysis.impl;

import com.alibaba.fastjson.JSON;
import com.gs.common.define.Constants;
import com.gs.common.util.FileUtil;
import com.gs.common.util.HexUtil;
import com.gs.common.util.analysis.AnalysisUtil;
import com.gs.common.util.base64.Base64Util;
import com.gs.common.util.crypto.OidUtil;
import com.gs.common.util.seal.SealUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.cms.EncryptedContentInfo;
import org.bouncycastle.asn1.cms.EnvelopedData;
import org.bouncycastle.asn1.pkcs.ContentInfo;
import org.bouncycastle.asn1.pkcs.SignedData;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

import java.util.Enumeration;

/**
 * @Author Zhang Juan
 * @Date 2024/3/29 16:59
 */
@Slf4j
public class AnalysisP7 extends AnalysisUtil {

    /**
     * 解析P7结构
     * @param data p7数据
     * @throws Exception
     */
    @Override
    public void analysis(byte[] data) throws Exception {
        analysisP7(data);
    }

    public void analysisP7(byte[] data) throws Exception {
        if (data[0] != 0x30) {
            log.debug("p7数据首字节非0x30，进行Base64解码尝试");
            data = Base64Util.decode(data);
            log.debug("解码成功");
        }

        ASN1InputStream asn1InputStream = new ASN1InputStream(data);
        ASN1Primitive asn1Primitive;
        log.debug("开始按照《PKCS#7:加密消息语法标准》规范解析");
        while ((asn1Primitive = asn1InputStream.readObject()) != null) {
            ASN1Sequence asn1Sequence = (ASN1Sequence)asn1Primitive;
            int size = asn1Sequence.size();
            log.debug("共{}个部分", size);

            ASN1ObjectIdentifier obj0 = (ASN1ObjectIdentifier)asn1Sequence.getObjectAt(0);
            String oid = obj0.toString();
            String oidCn = OidUtil.getSignHash(oid);
            log.debug("--OBJECT IDENTIFIER：内容类型标识 = {}({})", oid, oidCn);

            if (size == 2) {
                DERTaggedObject obj1 = (DERTaggedObject)asn1Sequence.getObjectAt(1);
                ASN1Sequence sequence = (ASN1Sequence)obj1.getObject();
                if (Constants.PKCS7_SIGNED_DATA.equals(oidCn)) {
                    analysisP7SignedData(sequence);
                } else if (Constants.PKCS7_ENVELOPED_DATA.equals(oidCn)) {
                    analysisP7EnvelopedData(sequence);
                }

            }
        }


        asn1InputStream.close();
        log.debug("解析P7结束");
    }

    private void analysisP7SignedData(ASN1Sequence sequence) throws Exception {
        log.debug("--Context-SEQUENCE：签名包，共{}部分", sequence.size());

        SignedData signedData = new SignedData(sequence);
        ASN1Integer version = signedData.getVersion();
        ASN1Set digestAlgorithms = signedData.getDigestAlgorithms();
        ContentInfo contentInfo = signedData.getContentInfo();
        ASN1Set certificates = signedData.getCertificates();
        ASN1Set crLs = signedData.getCRLs();
        ASN1Set signerInfos = signedData.getSignerInfos();

        // 1. ==============================================版本,这一标准中版本应该为1
        log.debug("----INTEGER：版本(这一标准中版本应该为1) = {}", version.getValue());

        // 2. ==============================================摘要算法
        ASN1Sequence hashSequence = (ASN1Sequence)digestAlgorithms.getObjectAt(0);
        String hashOid = hashSequence.getObjectAt(0).toString();
        log.debug("----SET：摘要算法 = {}({})", hashOid, OidUtil.getSignHash(hashOid));

        // 3. ==============================================被签名内容
        log.debug("----SEQUENCE：被签名内容 = {}", JSON.toJSON(contentInfo));

        // 4. ==============================================签名证书(可选项)
        log.debug("----Context：签名证书(可选项) = {}", certificates);

        // 5. ==============================================证书吊销列表(可选项)
        log.debug("----Context：证书吊销列表(可选项) = {}", crLs);

        // 6. ==============================================签名信息
        log.debug("----SET-SEQUENCE：签名信息");

        ASN1Sequence signSequence = (ASN1Sequence)signerInfos.getObjectAt(0);
        log.debug("------all = {}", signSequence);

        log.debug("------INTEGER：版本 = {}", signSequence.getObjectAt(0));

        ASN1Sequence certSequence = (ASN1Sequence)signSequence.getObjectAt(1);
        ASN1Integer serialNumber = (ASN1Integer)certSequence.getObjectAt(1);
        log.debug("------SEQUENCE:签名者证书信息", certSequence);
        log.debug("--------INTEGER:证书序列号 = {}", HexUtil.byte2Hex(serialNumber.getValue().toByteArray()));

        ASN1Sequence obj2 = (ASN1Sequence)signSequence.getObjectAt(2);
        String hashOid2 = obj2.getObjectAt(0).toString();
        log.debug("------SEQUENCE：摘要算法");
        log.debug("--------OBJECT IDENTIFIER：摘要算法 = {}({})", hashOid2, OidUtil.getSignHash(hashOid2));

        ASN1Sequence obj3 = (ASN1Sequence)signSequence.getObjectAt(3);
        String signOid = obj3.getObjectAt(0).toString();
        log.debug("------SEQUENCE：签名算法 = {}({})", signOid, OidUtil.getSignHash(signOid));

        log.debug("------OCTET STRING:签名值 = {}", signSequence.getObjectAt(4));
    }

    private void analysisP7EnvelopedData(ASN1Sequence sequence) throws Exception {
        log.debug("--Context-SEQUENCE：信封包，共{}部分", sequence.size());

        EnvelopedData envelopedData = new EnvelopedData(sequence);
        ASN1Integer version = envelopedData.getVersion();
        ASN1Set recipientInfos = envelopedData.getRecipientInfos();
        EncryptedContentInfo encryptedContentInfo = envelopedData.getEncryptedContentInfo();

        // 1. ==============================================版本,这一标准中版本应该为0
        log.debug("----INTEGER：版本(这一标准中版本应该为0) = {}", version.getValue());

        // 2. ==============================================每个接收者信息的集合
        int size = recipientInfos.size();
        log.debug("----SET：接收者，共{}个", size);

        Enumeration secEnum = recipientInfos.getObjects();
        while (secEnum.hasMoreElements()) {
            ASN1Sequence seqObj = (ASN1Sequence) secEnum.nextElement();

            ASN1Integer certVersion = (ASN1Integer)seqObj.getObjectAt(0);
            ASN1Sequence cert = (ASN1Sequence)seqObj.getObjectAt(1);
            ASN1Sequence encAlg = (ASN1Sequence)seqObj.getObjectAt(2);
            ASN1OctetString objectAt3 = (ASN1OctetString)seqObj.getObjectAt(3);

            ASN1Integer serialNumber = (ASN1Integer)cert.getObjectAt(1);
            String encAlgOid = encAlg.getObjectAt(0).toString();
            log.debug("------SEQUENCE：接收者信息");
            log.debug("--------INTEGER：版本 = {}", certVersion.toString());
            log.debug("--------SEQUENCE-INTEGER：证书序列号 = {}", HexUtil.byte2Hex(serialNumber.getValue().toByteArray()));
            log.debug("--------SEQUENCE-OBJECT IDENTIFIER：非对称加密算法 = {}({})", encAlgOid, OidUtil.getSignHash(encAlgOid));
            log.debug("--------OCTET STRING:对称密钥的非对称加密值 = {}", objectAt3);
        }

        // 3. ==============================================加了密的内容信息
        log.debug("----SEQUENCE：加了密的内容信息");
        String contentTypeOid = encryptedContentInfo.getContentType().toString();
        AlgorithmIdentifier contentEncryptionAlgorithm = encryptedContentInfo.getContentEncryptionAlgorithm();
        String algorithm = contentEncryptionAlgorithm.getAlgorithm().toString();
        log.debug("------SEQUENCE：内容类型 = {}({})", contentTypeOid, OidUtil.getSignHash(contentTypeOid));
        log.debug("------SEQUENCE：对称算法 = {}({})", algorithm, OidUtil.getSignHash(algorithm));
        log.debug("------SEQUENCE：内容的对称加密值 = {}", encryptedContentInfo.getEncryptedContent());
    }

    public static void main(String[] args) throws Exception {
        String path = Constants.FILE_PATH + "seal/数字信封.p7";
        AnalysisUtil analysisUtil = new AnalysisP7();
        analysisUtil.analysis(FileUtil.getFile(path));

        path = Constants.FILE_PATH + "seal/电子签章.p7";
        analysisUtil.analysis(FileUtil.getFile(path));
    }
}
