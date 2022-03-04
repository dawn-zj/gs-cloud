package com.gs.common.pkcs.pkcs10;

import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

/**
 * 证书请求类
 */
public class CertificationRequest {
    protected CertificationRequestInfo reqInfo = null;
    protected AlgorithmIdentifier sigAlgId = null;
    protected ASN1BitString signature = null;

    protected CertificationRequest() {
    }

    public CertificationRequest(CertificationRequestInfo requestInfo, AlgorithmIdentifier algorithm, ASN1BitString signature) {
        this.reqInfo = requestInfo;
        this.sigAlgId = algorithm;
        this.signature = signature;
    }

    public DERSequence toDERSequence() {
        // 创建ASN1EncodableVector, 存放sequence的数据
        ASN1EncodableVector encodable = new ASN1EncodableVector();
        encodable.add(reqInfo);
        encodable.add(sigAlgId);
        encodable.add(signature);

        // ASN1EncodableVector封装为DERSequence
        DERSequence derSequence = new DERSequence(encodable);
        return derSequence;
    }
}
