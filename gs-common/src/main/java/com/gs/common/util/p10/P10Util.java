package com.gs.common.util.p10;

import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.pkcs.pkcs10.CertificationRequest;
import com.gs.common.util.HexUtil;
import com.gs.common.util.StringUtil;
import com.gs.common.util.base64.Base64Util;
import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

import java.io.IOException;

/**
 * 证书请求工具类
 */
public class P10Util {

//    public static String genP10(String keyType) {
//        try {
//            // 1.证书信息
//            CertificationRequestInfo certificationRequestInfo = new CertificationRequestInfo(null,null,null);
//
//            // 2.签名算法
//            AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier();
//
//
//            // 3.签名值
////            new ASN1BitString("1".getBytes(), 1)
//
//
//            CertificationRequest request = new CertificationRequest(null, null, null);
//            DERSequence sequence = request.toDERSequence();
//
//            byte[] data = sequence.getEncoded();
//            byte[] encode = Base64Util.encode(HexUtil.byte2Hex(data));
//            return new String(encode);
//        } catch (Exception e) {
//            throw new NetGSRuntimeException("gen certificate request error");
//        }
//
//    }

}
