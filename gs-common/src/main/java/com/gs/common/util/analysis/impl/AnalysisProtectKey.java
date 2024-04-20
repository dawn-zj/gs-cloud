package com.gs.common.util.analysis.impl;

import com.alibaba.fastjson.JSON;
import com.gs.common.define.Constants;
import com.gs.common.util.FileUtil;
import com.gs.common.util.HexUtil;
import com.gs.common.util.analysis.AnalysisUtil;
import com.gs.common.util.analysis.IAnalysis;
import com.gs.common.util.base64.Base64Util;
import com.gs.common.util.crypto.OidUtil;
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
public class AnalysisProtectKey extends AnalysisUtil {

    /**
     * 解析SM2密钥对保护数据
     * @param data p7数据
     * @throws Exception
     */
    @Override
    public void analysis(byte[] data) throws Exception {
        if (data[0] != 0x30) {
            log.debug("数据首字节非0x30，进行Base64解码尝试");
            data = Base64Util.decode(data);
            log.debug("解码成功");
        }

        ASN1InputStream asn1InputStream = new ASN1InputStream(data);
        ASN1Primitive asn1Primitive;
        log.debug("开始按照《GM/T 0009-2012 SM2密码算法使用规范》规范解析");
        while ((asn1Primitive = asn1InputStream.readObject()) != null) {
            ASN1Sequence asn1Sequence = (ASN1Sequence)asn1Primitive;
            int size = asn1Sequence.size();
            log.debug("共4个部分，实际{}个部分", size);

            if (size != 4) {
                log.debug("格式有误，退出解析");
                return;
            }

            ASN1Sequence symAlg = (ASN1Sequence)asn1Sequence.getObjectAt(0);
            String oid = symAlg.getObjectAt(0).toString();
            String oidCn = OidUtil.getSignHash(oid);
            log.debug("--SEQUENCE：对称密码算法标识");
            log.debug("----OBJECT IDENTIFIER：标识 = {}({})", oid, oidCn);

            ASN1Sequence sm2Cipher = (ASN1Sequence)asn1Sequence.getObjectAt(1);
            ASN1Integer x = (ASN1Integer)sm2Cipher.getObjectAt(0);
            ASN1Integer y = (ASN1Integer)sm2Cipher.getObjectAt(1);
            ASN1OctetString hash = (ASN1OctetString)sm2Cipher.getObjectAt(2);
            ASN1OctetString cipherText = (ASN1OctetString)sm2Cipher.getObjectAt(3);
            log.debug("--SEQUENCE：对称密钥密文");
            log.debug("----INTEGER：x分量 = {}", HexUtil.byte2Hex(x.getValue().toByteArray()));
            log.debug("----INTEGER：y分量 = {}", HexUtil.byte2Hex(y.getValue().toByteArray()));
            log.debug("----INTEGER：杂凑值 = {}", HexUtil.byte2Hex(hash.getOctets()));
            log.debug("----INTEGER：密文 = {}", HexUtil.byte2Hex(cipherText.getOctets()));

            ASN1BitString sm2PublicKey = (ASN1BitString)asn1Sequence.getObjectAt(2);
            log.debug("--BIT STRING：SM2公钥 = {}", HexUtil.byte2Hex(sm2PublicKey.getBytes()));

            ASN1BitString sm2EncryptedPrivateKey = (ASN1BitString)asn1Sequence.getObjectAt(3);
            log.debug("--BIT STRING：SM2私钥密文 = {}", HexUtil.byte2Hex(sm2EncryptedPrivateKey.getBytes()));
        }

        asn1InputStream.close();
        log.debug("解析SM2密钥对保护数据结束");
    }

    public static void main(String[] args) throws Exception {
        String path = Constants.FILE_PATH + "seal/密钥对保护数据格式.key";
        AnalysisUtil analysisUtil = new AnalysisProtectKey();
        analysisUtil.analysis(FileUtil.getFile(path));
    }
}
