package com.gs.common.entity.crypto;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x509.X509Name;

import java.math.BigInteger;

public class IssuerAndSerialNumber implements ASN1Encodable {
    X509Name name;
    DERInteger certSerialNumber;

    public static IssuerAndSerialNumber getInstance(Object obj) {
        if (obj instanceof IssuerAndSerialNumber) {
            return (IssuerAndSerialNumber)obj;
        } else if (obj instanceof ASN1Sequence) {
            return new IssuerAndSerialNumber((ASN1Sequence)obj);
        } else {
            throw new IllegalArgumentException("unknown object in factory: " + obj.getClass().getName());
        }
    }

    public IssuerAndSerialNumber(ASN1Sequence seq) {
        this.name = X509Name.getInstance(seq.getObjectAt(0));
        this.certSerialNumber = (DERInteger)seq.getObjectAt(1);
    }

    public IssuerAndSerialNumber(X509Name name, BigInteger certSerialNumber) {
        this.name = name;
        this.certSerialNumber = new DERInteger(certSerialNumber);
    }

    public IssuerAndSerialNumber(X509Name name, DERInteger certSerialNumber) {
        this.name = name;
        this.certSerialNumber = certSerialNumber;
    }

    public X509Name getName() {
        return this.name;
    }

    public DERInteger getCertificateSerialNumber() {
        return this.certSerialNumber;
    }

    public ASN1Encodable toASN1Object() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.name);
        v.add(this.certSerialNumber);
        return new DERSequence(v);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return null;
    }
}
