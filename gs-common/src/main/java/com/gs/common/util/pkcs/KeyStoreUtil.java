package com.gs.common.util.pkcs;

import com.gs.common.define.Constants;
import com.gs.common.exception.BaseException;
import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.util.FileUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Enumeration;

/**
 * @author Administator
 * @date 2021-11-04 15:44
 * @Description
 */
public class KeyStoreUtil {
    /**
     * 加载私钥
     * @param keyPwd 密码
     * @param keyMode 密钥类型
     * @param keyData 密钥数据
     * @return
     * @throws Exception
     */
    public static PrivateKey loadKey(String keyPwd, String keyMode, byte[] keyData) throws Exception {
        KeyStore ks = null;
        PrivateKey priKey = null;

        if (Constants.PFX_SUFFIX.equals(keyMode)) {
            ks = KeyStore.getInstance("PKCS12");
            ks.load(new ByteArrayInputStream(keyData), keyPwd.toCharArray());
        } else if (Constants.JKS_SUFFIX.equals(keyMode)) {
            ks = KeyStore.getInstance("JKS");
            ks.load(new ByteArrayInputStream(keyData), keyPwd.toCharArray());
        } else {
            throw new NetGSRuntimeException("Not support key mode: " + keyMode);
        }

        Enumeration en = ks.aliases();
        while (en.hasMoreElements()) {
            String alias = (String) en.nextElement();
            priKey = (PrivateKey) ks.getKey(alias, keyPwd.toCharArray());
            if (priKey != null)
                break;
        }

        if (priKey == null)
            throw new BaseException("密钥在文件中不存在");

        return priKey;
    }

    /**
     * jks转pfx
     * @param jksPath
     * @param pfxPath
     * @param keyPwd jks密码，pfx也使用此密码
     * @throws Exception
     */
    public static void jks2pfx(String jksPath, String pfxPath, String keyPwd) throws Exception {
        jks2pfx(jksPath, pfxPath, keyPwd, keyPwd);
    }

    /**
     *
     * jks转pfx
     * @param jksPath
     * @param pfxPath
     * @param jksKeyPwd jks密码
     * @param pfxKeyPwd pfx密码
     * @throws Exception
     */
    public static void jks2pfx(String jksPath, String pfxPath, String jksKeyPwd, String pfxKeyPwd) throws Exception {
        char[] jksPwd = jksKeyPwd.toCharArray();

        // 加载jks
        KeyStore inputKeyStore = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream(jksPath);
        inputKeyStore.load(fis, jksPwd);
        fis.close();

        // 查找相关信息，写入pfx
        char[] pfxPwd = pfxKeyPwd.toCharArray();
        KeyStore outputKeyStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
        outputKeyStore.load(null, pfxPwd);

        Enumeration enums = inputKeyStore.aliases();
        while (enums.hasMoreElements()) {
            String keyAlias = (String) enums.nextElement();
            System.out.println("alias=[" + keyAlias + "]");

            if (inputKeyStore.isKeyEntry(keyAlias)) {
                Key key = inputKeyStore.getKey(keyAlias, jksPwd);
                Certificate[] certChain = inputKeyStore.getCertificateChain(keyAlias);
                outputKeyStore.setKeyEntry(keyAlias, key, jksPwd, certChain);
            }

        }

        FileOutputStream out = new FileOutputStream(pfxPath);
        outputKeyStore.store(out, pfxPwd);

        out.close();
    }

    /**
     * 从pfx中解析证书
     * @param keyPwd
     * @param pfxData
     * @return
     * @throws Exception
     */
    public static byte[] getCertFromPfx(String keyPwd, byte[] pfxData) throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new ByteArrayInputStream(pfxData), keyPwd.toCharArray());

        Enumeration en = ks.aliases();
        while (en.hasMoreElements()) {
            String alias = (String) en.nextElement();
            PrivateKey priKey = (RSAPrivateKey) ks.getKey(alias, keyPwd.toCharArray());
            if (priKey != null) {
                X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
                return x509.getEncoded();
            }
        }
        throw new BaseException("密钥在文件中不存在");
    }

    public static void main(String[] args) throws Exception {
        String password = "11111111";
        String pfxPath = Constants.FILE_PATH + "/key/rsa/rsapfx3des-sha1.pfx";
        PrivateKey pk = KeyStoreUtil.loadKey(password, Constants.PFX_SUFFIX, FileUtil.getFile(pfxPath));
        System.out.println("签名算法：" + pk.getAlgorithm());
    }
}
