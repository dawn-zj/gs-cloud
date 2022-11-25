package com.gs.common.util.cert;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import com.gs.common.define.Constants;
import com.gs.common.util.FileUtil;
import com.gs.common.util.HexUtil;
import com.gs.common.util.base64.Base64Util;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Hex;

public class CertUtil {

	static {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

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

	/**
	 * 解析p7b证书链
	 * @param certChainData
	 * @return
	 * @throws Exception
	 */
	public static List<X509Certificate> parseCertChain(byte[] certChainData) throws Exception {
		List<X509Certificate> list = new ArrayList<>();

		CMSSignedData cmsSignedData = new CMSSignedData(certChainData);
		Store<X509CertificateHolder> store = cmsSignedData.getCertificates();
		Collection<X509CertificateHolder> certificates = store.getMatches(null);

		for (X509CertificateHolder certHolder : certificates) {
			X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
			list.add(cert);
		}
		return list;
	}

	/**
	 * 制作p7b证书链
	 * @param certChain
	 * @return
	 * @throws Exception
	 */
	public static byte[] genCertChain(List<X509Certificate> certChain) throws Exception {
		JcaCertStore jcaCertStore = new JcaCertStore(certChain);

		CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
		gen.addCertificates(jcaCertStore);

		CMSProcessableByteArray msg = new CMSProcessableByteArray("".getBytes());
		CMSSignedData signedData = gen.generate(msg);
		return signedData.getEncoded();

	}

	public static void main(String[] args) throws Exception {
//		byte[] file = FileUtil.getFile(Constants.FILE_PATH + "ca.cer");
//		X509Certificate x509Certificate = getX509Certificate(file);
//		System.out.println(x509Certificate.getSubjectDN());

//		byte[] file = FileUtil.getFile("E:\\zj\\file\\cert\\p7b\\1.p7b");
//		CertUtil.parseCertChain(Base64Util.decode(file));

		ArrayList<X509Certificate> list = new ArrayList<>();
		list.add(CertUtil.getX509Certificate(FileUtil.getFile("E:\\Idea\\gs\\gs-cloud\\gs-cloud\\file\\out\\3B53A48943.cer")));

		byte[] bytes = genCertChain(list);
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "p7b/1.p7b", bytes);

	}
}
