package demo;

import com.gs.common.define.Constants;
import com.gs.common.util.FileUtil;
import com.gs.common.util.HexUtil;
import com.gs.common.util.cert.CertUtil;
import com.gs.common.util.crypto.KeyUtil;
import com.gs.common.util.pkcs.KeyStoreUtil;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

public class ITextTest {

    @Test
    public void digestAlgorithms() {
        // 各种hash写法，转成itext认的写法
        System.out.println(DigestAlgorithms.getDigest(DigestAlgorithms.getAllowedDigests("SHA-1")));
        System.out.println(DigestAlgorithms.getDigest(DigestAlgorithms.getAllowedDigests("sha-1")));
        System.out.println(DigestAlgorithms.getDigest(DigestAlgorithms.getAllowedDigests("SHA1")));
        System.out.println(DigestAlgorithms.getDigest("1.3.14.3.2.26"));
    }

    @Test
    public void externalSignature() throws Exception {
        String password = "11111111";
        String pfxPath = Constants.FILE_PATH + "/key/rsa/rsapfx3des-sha1.pfx";

        // 读取keystore ，获得私钥
        PrivateKey pk = KeyStoreUtil.loadKey(password, Constants.PFX_SUFFIX, FileUtil.getFile(pfxPath));

        // 使用itext的ExternalSignature签名
        ExternalSignature signature = new PrivateKeySignature(pk, "sha1", null);
        System.out.println("摘要算法：" + signature.getHashAlgorithm());
        System.out.println("签名算法：" + signature.getEncryptionAlgorithm());
        byte[] signed = signature.sign("123".getBytes());

        // 验签
        byte[] file = FileUtil.getFile(Constants.FILE_PATH + "/key/rsa/rsapfx3des-sha1.cer");
        X509Certificate x509Certificate = CertUtil.getX509Certificate(file);
        boolean result = KeyUtil.rawSignVerify(x509Certificate.getPublicKey(), "123".getBytes(), signed, Constants.SHA1_RSA, "1".getBytes());
        System.out.println("itext签名验签完成，验签结果：" + result);
    }

    @Test
    public void getStampFromPDF() throws Exception {
        String pdfStampPath = "F:/kms-rsa签章.pdf";

        byte[] pdfData = FileUtil.getFile(pdfStampPath);

        PdfReader reader = null;
        try {
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
                if (lenHex.equals("81")) { //308110xx
                    lenHex = hexContents.substring(4, 6);
                    int i = Integer.parseInt(lenHex, 16);
                    hexContents = hexContents.substring(0, (i + 3) * 2);
                } else if (lenHex.equals("82")) { //30821020xx
                    lenHex = hexContents.substring(4, 8);
                    int i = Integer.parseInt(lenHex, 16);
                    hexContents = hexContents.substring(0, (i + 4) * 2);
                } else {
                    int i = Integer.parseInt(lenHex, 16);
                    hexContents = hexContents.substring(0, (i + 2) * 2);
                }

                FileUtil.storeFile("f:/temp/kms-rsa签章-" + name + ".stamp", HexUtil.hex2Byte(hexContents));
            }


        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (Exception e) {
            }
        }
    }

}
