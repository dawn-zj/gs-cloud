package com.gs.webserver.controller.tool;

import com.gs.common.util.HexUtil;
import com.gs.common.util.cert.CertUtil;
import com.gs.webserver.entity.to.response.ResponseTo;
import com.gs.webserver.entity.to.response.cert.CertResTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseTo<CertResTo> certTest(@RequestParam("file") MultipartFile file) throws Exception {
        X509Certificate x509Certificate = CertUtil.getX509Certificate(file.getBytes());
        CertResTo certResTo = new CertResTo();
        certResTo.setDn(x509Certificate.getSubjectDN().toString());
        certResTo.setIssueDn(x509Certificate.getIssuerDN().toString());
        certResTo.setSn(HexUtil.byte2Hex(x509Certificate.getSerialNumber().toByteArray()).toUpperCase());
        certResTo.setNotBefore(x509Certificate.getNotBefore().getTime());
        certResTo.setNotAfter(x509Certificate.getNotAfter().getTime());
        certResTo.setKeyUsage(x509Certificate.getKeyUsage());
        return ResponseTo.success(certResTo);
    }
}
