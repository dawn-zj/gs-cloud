package demo;

import com.gs.common.define.Constants;
import com.gs.common.util.FileUtil;
import com.gs.common.util.cert.CertUtil;
import com.gs.common.util.crypto.KeyUtil;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import org.junit.Test;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

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
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(pfxPath), password.toCharArray());
        String alias = ks.aliases().nextElement();
        PrivateKey pk = (PrivateKey) ks.getKey(alias, password.toCharArray());

        // 使用itext的ExternalSignature签名
        ExternalSignature signature = new PrivateKeySignature(pk, "sha1", null);
        System.out.println("摘要算法：" + signature.getHashAlgorithm());
        System.out.println("签名算法：" + signature.getEncryptionAlgorithm());
        byte[] signed = signature.sign("123".getBytes());

        // 验签
        byte[] file = FileUtil.getFile(Constants.FILE_PATH + "/key/rsa/rsapfx3des-sha1.cer");
        X509Certificate x509Certificate = CertUtil.getX509Certificate(file);
        boolean result = KeyUtil.rawSignVerify(x509Certificate.getPublicKey(), "123".getBytes(), signed, -1, Constants.SHA1_RSA, "1".getBytes());
        System.out.println("itext签名验签完成，验签结果：" + result);
    }

}
