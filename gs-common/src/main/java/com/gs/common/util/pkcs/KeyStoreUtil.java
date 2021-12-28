package com.gs.common.util.pkcs;

import com.gs.common.define.Constants;
import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.util.FileUtil;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
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
            throw new NetGSRuntimeException("密钥在文件中不存在");

        return priKey;
    }

    public static void main(String[] args) throws Exception {
        String password = "11111111";
        String pfxPath = Constants.FILE_PATH + "/key/rsa/rsapfx3des-sha1.pfx";
        PrivateKey pk = KeyStoreUtil.loadKey(password, Constants.PFX_SUFFIX, FileUtil.getFile(pfxPath));
        System.out.println("签名算法：" + pk.getAlgorithm());
    }
}
