package com.gs.common.util.pkcs;

import com.gs.common.define.Constants;
import com.gs.common.util.FileUtil;
import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.security.PrivateKey;

import static org.junit.jupiter.api.Assertions.*;

class KeyStoreUtilTest {

    @ParameterizedTest
    @CsvSource({
            "11111111, .pfx, /key/rsa/rsapfx3des-sha1.pfx"
            , "11111111, .jks, /key/rsa/zj.jks"
            , "11111111, .pri, /key/netseal.pri"
            , "11111111, .pri, /key/NetSealEnc.pri"
    })
    void loadKey(String keyPwd, String keyMode, String path) throws Exception {
        String pfxPath = Constants.FILE_PATH + path;
        PrivateKey pk = KeyStoreUtil.loadKey(keyPwd, keyMode, FileUtil.getFile(pfxPath));
        System.out.println("签名算法：" + pk.getAlgorithm());
        assertNotNull(pk.getAlgorithm());
    }

    @ParameterizedTest
    @CsvSource({
            "/key/rsa/zj.jks, /key/rsa/zj.pfx, 11111111"
    })
    void jks2pfx(String jksPath, String pfxPath, String password) throws Exception {
        jksPath = Constants.FILE_PATH + jksPath;
        pfxPath = Constants.FILE_PATH + pfxPath;
        KeyStoreUtil.jks2pfx(jksPath, pfxPath, password);
    }

    @ParameterizedTest
    @CsvSource({
            "/key/rsa/rsapfx3des-sha1.pfx, 11111111"
    })
    void getCertFromPfx(String pfxPath, String password) throws Exception {
        pfxPath = Constants.FILE_PATH + pfxPath;
        byte[] cert = KeyStoreUtil.getCertFromPfx(password, FileUtil.getFile(pfxPath));
        Assert.assertNotNull(cert);
    }
}