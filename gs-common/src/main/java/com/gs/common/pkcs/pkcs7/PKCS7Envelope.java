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
     * @param content 要发送的内容
     * @param cert 接收方公钥证书
     * @return
     * @throws Exception
     */
    public static byte[] makeP7(byte[] content, X509Certificate cert) throws Exception {
        CMSTypedData signedData = new CMSProcessableByteArray(content);

        CMSEnvelopedDataGenerator edGen = new CMSEnvelopedDataGenerator();
        edGen.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(cert).setProvider(new BouncyCastleProvider()));

        CMSEnvelopedData ed = edGen.generate(signedData, new JceCMSContentEncryptorBuilder(PKCSObjectIdentifiers.rc4).setProvider(new BouncyCastleProvider()).build());

        byte[] result = ed.getEncoded();
        return result;
    }

    /**
     * cms解析数字信封
     * @param envData 数字信封数据
     * @param privateKey 接收者私钥
     * @return
     * @throws Exception
     */
    public static byte[] verifyP7(byte[] envData, PrivateKey privateKey) throws Exception {
        CMSEnvelopedData ed = new CMSEnvelopedData(envData);

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
