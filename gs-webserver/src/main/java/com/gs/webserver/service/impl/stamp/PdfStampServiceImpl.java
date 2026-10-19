package com.gs.webserver.service.impl.stamp;

import com.gs.common.define.Constants;
import com.gs.common.entity.pdf.StampVerify;
import com.gs.common.exception.BaseException;
import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.util.FileUtil;
import com.gs.common.util.HexUtil;
import com.gs.common.util.base64.Base64Util;
import com.gs.common.util.pdf.PdfStampUtil;
import com.gs.webserver.entity.to.request.StampTo;
import com.gs.webserver.entity.to.response.pdf.PdfStampVerifyResTo;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administator
 */
@Service
public class PdfStampServiceImpl extends StampServiceImpl {

    @Override
    public byte[] stamp(byte[] pdfData, String pfxFilePath, String password, List<StampTo> list) throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(pfxFilePath), password.toCharArray());
        String alias = ks.aliases().nextElement();
        PrivateKey pk = (PrivateKey) ks.getKey(alias, password.toCharArray());
        // 得到证书链
        Certificate[] chain = ks.getCertificateChain(alias);

        //签章: PDF的RSA签章 = 图片+签名，没有印章和签章结构
        byte[] signedData = null;

        PdfStampUtil pdfUtil = new PdfStampUtil();
        for (StampTo info: list ) {
            String photoBase64 = info.getSealInfo().getBase64();
            byte[] photoData = Base64Util.decode(photoBase64);
            // todo 摘要算法从证书中取
            signedData = pdfUtil.sign(pdfData, photoData, info.getPageNum(),info.getX(), info.getY(), chain, pk, DigestAlgorithms.SHA1);
            pdfData = signedData;
        }
        return signedData;
    }

    @Override
    public PdfStampVerifyResTo verify(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String fileType = FileUtil.getFileSuffix(filename);
            if (!Constants.PDF_SUFFIX.equals(fileType)) {
                throw new BaseException("文件不是PDF类型");
            }

            PdfStampUtil pdfUtil = new PdfStampUtil();
            StampVerify verify = pdfUtil.verifySign(file.getBytes());

            PdfStampVerifyResTo to = new PdfStampVerifyResTo();
            to.setResult(verify.isResult());
            to.setSignList(verify.getSignList());
            return to;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new NetGSRuntimeException("PDF验签章发生错误", e);
        }
    }

    @Override
    public Map<String, String> getStampFromPdf(MultipartFile file) throws Exception {
        Map<String, String> map = new HashMap<>();
        PdfReader reader = null;
        try {
            byte[] pdfData = file.getBytes();
            reader = new PdfReader(pdfData);
            AcroFields af = reader.getAcroFields();

            ArrayList<String> names = af.getSignatureNames();
            // 获取每一个签名域的 Contents
            for (String name : names) {
                PdfDictionary dictionary = af.getSignatureDictionary(name);
                byte[] bytes = dictionary.getAsString(PdfName.CONTENTS).getBytes();
                String hexContents = HexUtil.byte2Hex(bytes);

                // 判断签名值长度
                String lenHex = hexContents.substring(2, 4);
                if ("81".equals(lenHex)) {
                    //308110xx
                    lenHex = hexContents.substring(4, 6);
                    int i = Integer.parseInt(lenHex, 16);
                    hexContents = hexContents.substring(0, (i + 3) * 2);
                } else if ("82".equals(lenHex)) {
                    //30821020xx
                    lenHex = hexContents.substring(4, 8);
                    int i = Integer.parseInt(lenHex, 16);
                    hexContents = hexContents.substring(0, (i + 4) * 2);
                } else {
                    int i = Integer.parseInt(lenHex, 16);
                    hexContents = hexContents.substring(0, (i + 2) * 2);
                }

                map.put(name, Base64Util.encode(HexUtil.hex2Byte(hexContents)));
            }
            return map;

        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) {
        byte[] file = FileUtil.getFile("E:/1.txt");
        FileUtil.storeFile("E:/1.asn1", HexUtil.hex2Byte(new String(file)));
    }
}
