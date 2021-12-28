package com.gs.common.util.cert;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import com.gs.common.define.Constants;
import com.gs.common.util.FileUtil;
import com.gs.common.util.base64.Base64Util;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CertUtil {

	public static X509Certificate getX509Certificate(byte[] certData) throws Exception {
		ByteArrayInputStream in = null;
		// 判断是否为der编码证书
		if (certData[0] == 0x30) {
			int tl = ((int) (certData[1] & 0xff)) - 128;
			if (tl > 0) {
				byte[] ltmp = new byte[tl];
				System.arraycopy(certData, 2, ltmp, 0, tl);
				int length = new BigInteger(ltmp).intValue();
				if ((length > 0) && (length == (certData.length - 2 - tl))) {
					in = new ByteArrayInputStream(certData);
				} else {
					throw new CertificateException("Illegal length: " + length);
				}
			} else {
				throw new CertificateException("Illegal code: 30 " + ((certData[1] & 0xff)));
			}
		} else {
			String head = "-----BEGIN CERTIFICATE-----";
			String tail = "-----END CERTIFICATE-----";
			String b64Cert = new String(certData);
			if (b64Cert.indexOf(head) > -1) {
				b64Cert = b64Cert.replaceFirst(head, "").replaceFirst(tail, "");
			}
			byte[] certTmp = Base64Util.decode(b64Cert.trim());
			in = new ByteArrayInputStream(certTmp);
		}
		// CertificateFactory cf = CertificateFactory.getInstance("X.509FX", "INFOSEC");
		// CertificateFactory cf = CertificateFactory.getInstance("X.509"); //java自带security，暂只支持RSA证书
		CertificateFactory cf = CertificateFactory.getInstance("X.509", new BouncyCastleProvider()); //使用第三方BouncyCastle作为提供者，支持RSA和国密证书
		return (X509Certificate) cf.generateCertificate(in);
	}

	public static void getAllProvider() {
		Provider[] providers = Security.getProviders();
		for (int i = 0; i < providers.length; i++) {
			System.out.println("" + (i + 1) + ":" + providers[i]);
			for (Enumeration<?> e = providers[i].keys(); e.hasMoreElements();) {
				System.out.println("\t" + e.nextElement());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		byte[] file = FileUtil.getFile(Constants.FILE_PATH + "ca.cer");
		X509Certificate x509Certificate = getX509Certificate(file);
		System.out.println(x509Certificate.getSubjectDN());
	}
}
