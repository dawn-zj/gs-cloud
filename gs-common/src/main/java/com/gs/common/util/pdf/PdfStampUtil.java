package com.gs.common.util.pdf;

import com.alibaba.fastjson.JSONObject;
import com.gs.common.define.Constants;
import com.gs.common.entity.pdf.StampInfo;
import com.gs.common.entity.pdf.StampVerify;
import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.util.StringUtil;
import com.gs.common.util.cert.CertUtil;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * 参考文章：https://blog.csdn.net/tomatocc/article/details/80762507
 * @author Administator
 */
public class PdfStampUtil {

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * PDF添加图片并签名
     * @param pdfData pdf数据
     * @param photoData 图片数据
     * @param pageNumber 页码
     * @param x x坐标，单位像素
     * @param y y坐标，单位像素
     * @param chain 证书链
     * @param privateKey 私钥
     * @param hashAlg 摘要算法
     * @return
     * @throws Exception
     */
    public byte[] sign(byte[] pdfData, byte[] photoData, int pageNumber, float x, float y,
                       Certificate[] chain, PrivateKey privateKey, String hashAlg) throws Exception {
        PdfReader reader = new PdfReader(pdfData);

        /*
         * 创建签章工具PdfStamper，
         * 第二个参数是输出流，签完的文件放在这个输出流，我们获取
         * 最后一个boolean参数是否允许被追加签名
         * false的话，pdf文件只允许被签名一次，多次签名，最后一次有效
         * true的话，pdf可以被追加签名，验签工具可以识别出每次签名之后文档是否被修改
         */
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfStamper stamper = PdfStamper.createSignature(reader, outputStream, '\0', null, true);

        // 1.设置PdfSignatureAppearance
        PdfSignatureAppearance sap = stamper.getSignatureAppearance();
        // 1.1设置图章图片
        Image image = Image.getInstance(photoData);
        sap.setSignatureGraphic(image);
        // 1.2设置图章的显示方式，这里是GRAPHIC只显示图章（还有其他的模式，可以图章和签名描述一同显示），不设置默认是展示描述
        sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
        // 1.3设置图章位置，页码，签名域名称，多次追加签名的时候，签名域名称不能一样 图片大小受表单域大小影响（过小导致压缩）
        // 签名的坐标，是图章相对于pdf页面的位置坐标，原点为pdf页面左下角
        // 四个参数单位均是像素，分别是，图章左下角x，图章左下角y，图章右上角x，图章右上角y。
        // 图片单位调整，像素改为磅pt：像素 / 72 = ? / dpi
        float imageWidth = image.getWidth() * 72f / Constants.DPI;
        float imageHeight = image.getHeight() * 72f / Constants.DPI;
        float ux = x + imageWidth;
        float uy = y + imageHeight;
        sap.setVisibleSignature(new Rectangle(x, y, ux, uy), pageNumber, StringUtil.genDigitRandom(10));

        // 2.摘要算法接口，会根据hashAlg匹配至相应摘要类，进行hash
        ExternalDigest interfaceDigest = new BouncyCastleDigest();
        // 3.签名算法
        ExternalSignature externalSignature = new PrivateKeySignature(privateKey, hashAlg, null);
        // 签章
        MakeSignature.signDetached(sap, interfaceDigest, externalSignature, chain,
                null, null, null, 0, MakeSignature.CryptoStandard.CADES);

        stamper.close();
        reader.close();
        return outputStream.toByteArray();

    }

    /**
     * PDF验签名
     * @param pdfData
     * @return
     * @throws Exception
     */
    public StampVerify verifySign(byte[] pdfData) throws Exception {
        StampVerify object = new StampVerify();

        PdfReader reader = new PdfReader(pdfData);
        AcroFields fields = reader.getAcroFields();
        ArrayList<String> names = fields.getSignatureNames();
        boolean result = true;
        if (names.size() == 0) {
            result = false;
        }

        List<StampInfo> signList = new ArrayList<>();
        for (int i = 0, size = names.size(); i < size; i++) {
            String signName = (String) names.get(i);
            PdfDictionary dictionary = fields.getSignatureDictionary(signName);

            PdfName sub = dictionary.getAsName(PdfName.SUBFILTER);
            if (PdfName.ETSI_CADES_DETACHED.equals(sub)
                    || PdfName.ADBE_PKCS7_DETACHED.equals(sub)
                    || PdfName.ADBE_X509_RSA_SHA1.equals(sub)) {
                PdfPKCS7 pkcs7 = fields.verifySignature(signName);

                X509Certificate x509Certificate = pkcs7.getSigningCertificate();

                boolean verify = pkcs7.verify();
                if (!verify) {
                    result = false;
                }

                StampInfo signObj = new StampInfo();
                signObj.setResult(verify);
                signObj.setCertDn(x509Certificate.getSubjectDN().getName());
                signObj.setCertNotBefore(x509Certificate.getNotBefore().getTime());
                signObj.setCertNotAfter(x509Certificate.getNotAfter().getTime());
                signObj.setSignTime(pkcs7.getSignDate().getTimeInMillis());
                signObj.setSignSubFilter(pkcs7.getFilterSubtype().toString());
                signObj.setSignHashAlg(pkcs7.getDigestAlgorithm());
                signObj.setSignLocation(fields.getFieldPositions(signName));
                signList.add(signObj);
            } else {
                throw new NetGSRuntimeException("暂不支持的SubFilter类型：" + sub);
            }
        }

        object.setResult(result);
        object.setSignList(signList);
        return object;
    }

    /**
     * todo 未完待续，不好实现
     * @param pdfData
     * @param photoData
     * @param pageNumber
     * @param x
     * @param y
     * @param certData
     * @param hashAlg
     * @return
     * @throws Exception
     */
    public byte[] stampWithoutSign(byte[] pdfData, byte[] photoData, int pageNumber, float x, float y,
                                   byte[] certData, String hashAlg) throws Exception {
        PdfReader reader = new PdfReader(pdfData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfStamper stamper = PdfStamper.createSignature(reader, outputStream, '\0', null, true);

        // 1.设置PdfSignatureAppearance
        PdfSignatureAppearance sap = stamper.getSignatureAppearance();
        Image image = Image.getInstance(photoData);
        sap.setSignatureGraphic(image);
        sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);

        float imageWidth = image.getWidth() * 72f / Constants.DPI;
        float imageHeight = image.getHeight() * 72f / Constants.DPI;
        float ux = x + imageWidth;
        float uy = y + imageHeight;
        sap.setVisibleSignature(new Rectangle(x, y, ux, uy), pageNumber, StringUtil.genDigitRandom(10));

        X509Certificate x509Certificate = CertUtil.getX509Certificate(certData);
        sap.setCertificate(x509Certificate);

        // // 2.摘要算法接口
        // ExternalDigest digest = new BouncyCastleDigest();
        // // 3.签名算法
        // ExternalSignature signature = new PrivateKeySignature(privateKey, hashAlg, null);
        // // 签章
        // MakeSignature.signDetached(sap, digest, signature, chain,
        //         null, null, null, 0, MakeSignature.CryptoStandard.CADES);

        stamper.close();
        reader.close();
        return outputStream.toByteArray();

    }

    public byte[] data2sign(byte[] toSignData, PrivateKey privateKey, String hashAlg) throws Exception {
        ExternalSignature signature = new PrivateKeySignature(privateKey, hashAlg, null);
        byte[] signed = signature.sign(toSignData);
        return signed;
    }

    public byte[] pdfStampMerge(byte[] toSignData, byte[] signed) throws Exception {
       // todo 将待签原文和签名值合并
        return null;
    }


//    private byte[] parsePdfPlain(byte[] pdfData, PdfArray byteRange) throws Exception {
//        if (byteRange.size() != 4) {
//            // todo 异常
//        }
//
//        // [0, 283, 805, 37684]
//        int start1 = byteRange.getAsNumber(0).intValue();
//        int length1 = byteRange.getAsNumber(1).intValue();
//        int start2 = byteRange.getAsNumber(2).intValue();
//        int length2 = byteRange.getAsNumber(3).intValue();
//        byte[] plain = new byte[length1 + length2];
//        System.out.println(byteRange.toString());
//        System.arraycopy(pdfData, start1, plain, 0, length1);
//        System.arraycopy(pdfData, start2, plain, length1, length2);
//
//        FileUtil.storeFile("F:/temp/stamp-2-plain.pdf", plain);
//        return plain;
//    }
//
//    private byte[] parsePdfSigned(byte[] pdfData, PdfArray byteRange) throws Exception {
//        if (byteRange.size() != 4) {
//            // todo 异常
//        }
//
//        // [0, 283, 805, 37684]
//        int length1 = byteRange.getAsNumber(1).intValue();
//        int start2 = byteRange.getAsNumber(2).intValue();
//        byte[] signed = new byte[start2 - length1];
//        System.out.println(byteRange.toString());
//        System.arraycopy(pdfData, length1, signed, 0, start2 - length1);
//
//        return signed;
//
//    }
//
//    private X509Certificate parsePdfCert(byte[] pdfStampData, PdfString certStr) throws Exception {
//        X509CertParser x509CertParser = new X509CertParser();
//        x509CertParser.engineInit(new ByteArrayInputStream(certStr.getBytes()));
//        Collection collection = x509CertParser.engineReadAll();
//        X509Certificate certificate = (X509Certificate) collection.iterator().next();
//
//        System.out.println("dn: " + certificate.getSubjectDN());
//        System.out.println("alg: " + certificate.getSigAlgName());
//        return certificate;
//    }

}

