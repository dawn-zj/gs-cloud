package com.gs.common.pkcs.pkcs7;

import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;

public class PKCS7Envelope {

    /**
     * cms制作数字信封
     * @param signed
     * @param cert
     * @return
     * @throws Exception
     */
    public static byte[] makeP7(byte[] signed, X509Certificate cert) throws Exception {
        CMSTypedData signedData = new CMSProcessableByteArray(signed);

        CMSEnvelopedDataGenerator edGen = new CMSEnvelopedDataGenerator();
        edGen.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(cert).setProvider(new BouncyCastleProvider()));

        CMSEnvelopedData ed = edGen.generate(signedData, new JceCMSContentEncryptorBuilder(PKCSObjectIdentifiers.rc4).setProvider(new BouncyCastleProvider()).build());

        byte[] result = ed.getEncoded();
        return result;
    }

    /**
     * cms验证p7
     * @param signed
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] verifyP7(byte[] signed, PrivateKey privateKey) throws Exception {
        CMSEnvelopedData ed = new CMSEnvelopedData(signed);

        RecipientInformationStore recipients = ed.getRecipientInfos();

        Collection<RecipientInformation> c = recipients.getRecipients();
        Iterator<RecipientInformation> it = c.iterator();

        byte[] result = null;
        if (it.hasNext()) {
            RecipientInformation recipient = (RecipientInformation) it.next();

            result = recipient.getContent(new JceKeyTransEnvelopedRecipient(privateKey).setProvider(new BouncyCastleProvider()));
        }

        return result;
    }
}
