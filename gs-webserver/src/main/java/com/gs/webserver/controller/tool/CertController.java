package com.gs.webserver.controller.tool;

import com.gs.common.entity.KeyStoreIo;
import com.gs.common.util.HexUtil;
import com.gs.common.util.base64.Base64Util;
import com.gs.common.util.cert.CertUtil;
import com.gs.common.util.crypto.OidUtil;
import com.gs.common.util.p10.P10Util;
import com.gs.webserver.entity.to.response.CommonResTo;
import com.gs.webserver.entity.to.response.ResponseTo;
import com.gs.webserver.entity.to.response.cert.CertResTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.security.cert.X509Certificate;

/**
 * 证书
 * @Author Zhang Juan
 * @Date 2024/3/22 10:44
 */
@RestController
@RequestMapping("/tool/cert")
public class CertController {

    /**
     * x509证书解析
     * @param file 证书
     * @throws Exception
     */
    @PostMapping("/getX509Cert")
    public ResponseTo<CertResTo.CertTo> certTest(@RequestParam("file") MultipartFile file) throws Exception {
        X509Certificate x509Certificate = CertUtil.getX509Certificate(file.getBytes());
        CertResTo.CertTo certResTo = new CertResTo.CertTo();
        certResTo.setDn(x509Certificate.getSubjectDN().toString());
        certResTo.setIssueDn(x509Certificate.getIssuerDN().toString());
        certResTo.setSn(HexUtil.byte2Hex(x509Certificate.getSerialNumber().toByteArray()).toUpperCase());
        certResTo.setNotBefore(x509Certificate.getNotBefore().getTime());
        certResTo.setNotAfter(x509Certificate.getNotAfter().getTime());
        certResTo.setKeyUsage(x509Certificate.getKeyUsage());
        return ResponseTo.success(certResTo);
    }

    /**
     * 生成证书请求
     * @param alg 签名算法 |RSA
     * @param subjectDn 证书主题 |CN=Test
     * @param type 输出类型，1:Base64,2:PEM/Base64 |1
     * @return
     * @throws Exception
     */
    @PostMapping("/genP10")
    public ResponseTo<CommonResTo> genP10(String alg, String subjectDn, int type) throws  Exception {
        KeyStoreIo keyStoreIo = P10Util.genP10(".pri", alg, subjectDn);
        String p10 = keyStoreIo.getP10();

        CommonResTo commonResTo = new CommonResTo();
        switch (type) {
            case 1:
                break;
            case 2:
            default:
                p10 = P10Util.getPemP10(p10, true);
                break;
        }
        commonResTo.setResult(p10);
        return ResponseTo.success(commonResTo);
    }

    /**
     * 解析证书请求
     * @param p10B64 证书请求
     * @return
     * @throws Exception
     */
    @PostMapping("/verifyP10")
    public ResponseTo<CertResTo.CertRequestTo> verifyP10(String p10B64) throws  Exception {
        byte[] p10Data = Base64Util.decode(p10B64);
        boolean verifyP10 = P10Util.verifyP10(p10Data);
        byte[] pubKeyData = P10Util.getPubKeyFormP10(p10Data);

        CertResTo.CertRequestTo certRequestTo = new CertResTo.CertRequestTo();
        certRequestTo.setResult(verifyP10);
        certRequestTo.setPublicKeyB64(Base64Util.encode(pubKeyData));

        return ResponseTo.success(certRequestTo);
    }

    /**
     * 获取算法的oid值
     * @param alg 算法 |RSA
     * @return
     * @throws Exception
     */
    @PostMapping("/getOid")
    public ResponseTo<CommonResTo> getOid(@NotNull String alg) throws  Exception {
        alg = alg.trim().toUpperCase().replace("WITH", "with");
        String oid = OidUtil.getOid(alg);

        CommonResTo commonResTo = new CommonResTo();
        commonResTo.setResult(oid);
        return ResponseTo.success(commonResTo);
    }

    /**
     * 获取算法名
     * @param oid 算法oid |1.2.840.113549.1.1.1
     * @return
     * @throws Exception
     */
    @PostMapping("/getAlg")
    public ResponseTo<CommonResTo> getAlg(@NotNull String oid) throws  Exception {
        String alg = OidUtil.getSignHash(oid.trim());

        CommonResTo commonResTo = new CommonResTo();
        commonResTo.setResult(alg);
        return ResponseTo.success(commonResTo);
    }


}
