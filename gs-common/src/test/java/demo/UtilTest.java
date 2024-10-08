package demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.define.Constants;
import com.gs.common.entity.*;
import com.gs.common.entity.pdf.StampVerify;
import com.gs.common.pkcs.pkcs7.PKCS7Envelope;
import com.gs.common.util.*;
import com.gs.common.util.base64.Base64Util;
import com.gs.common.util.cert.CertUtil;
import com.gs.common.util.crypto.KeyUtil;
import com.gs.common.util.crypto.RSAUtil;
import com.gs.common.util.crypto.SM2Util;
import com.gs.common.util.date.DateUtil;
import com.gs.common.util.p10.P10Util;
import com.gs.common.util.pdf.PdfStampUtil;
import com.gs.common.util.pdf.PdfUtil;
import com.gs.common.util.pdf.RemovePdfStampUtil;
import com.gs.common.util.pkcs.KeyStoreUtil;
import com.gs.common.util.seal.SealUtil;
import com.gs.common.util.seal.StampUtil;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import org.bouncycastle.asn1.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UtilTest {
	private String password = "11111111";
	private String pfxPath = Constants.FILE_PATH + "/key/rsa/server.pfx";
	private String pfxCertPath = Constants.FILE_PATH + "/key/rsa/server.cer";

	@Test
	public void genDerASN1() throws Exception {
		// 创建ASN1EncodableVector, 存放sequence的数据
		ASN1EncodableVector encodable = new ASN1EncodableVector();
		encodable.add(new DERInteger(10)); //10对应的hex为0a
		encodable.add(new DERUTF8String("string"));

		// ASN1EncodableVector封装为DERSequence
		DERSequence derSequence = new DERSequence(encodable);

		// 写入DERSequence数据
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DEROutputStream derOutputStream = new DEROutputStream(outputStream);
		derOutputStream.writeObject(derSequence);
		derOutputStream.flush();

		// 从字节流中获取最终数据
		byte[] result = outputStream.toByteArray();
		FileUtil.storeFile(Constants.FILE_OUT_PATH  + "sequence.asn1", result);
		System.out.println("DER编码生成完毕：" + HexUtil.format(HexUtil.byte2Hex(result))); // 300b02010a0c06737472696e67

		/*
		 * 30 0b 02 01 0a 0c 06 73 74 72 69 6e 67
		 * TLV
		 * 30 代表ox30 sequence
		 * 0b 代表sequence长度为11，值为02 01 0a 0c 06 73 74 72 69 6e 67
		 *
		 * 02 代表ox02 integer
		 * 01 代表integer长度为1，值为0a，转为十进制为10
		 *
		 * 0c 代表ox0c utf8 string
		 * 06 代表utf8 string长度为6，值为73 74 72 69 6e 67
		 * */

	}

	@Test
	public void parseDerASN1() throws Exception {
		byte[] file = FileUtil.getFile(Constants.FILE_OUT_PATH + "sequence.asn1");
		ASN1InputStream asn1InputStream = new ASN1InputStream(file);
		ASN1Primitive asn1Primitive = asn1InputStream.readObject();
		ASN1Sequence obj = (ASN1Sequence) asn1Primitive;

		ASN1Integer a_obj = (ASN1Integer)obj.getObjectAt(0);
		byte[] a = a_obj.getValue().toByteArray();
		System.out.println(HexUtil.byte2Hex(a));

	}
	/**
	 * 制作图片验证码
	 *
	 * @throws Exception
	 */
	@Test
	public void genAuthCodeTest() throws Exception {
		String path = Constants.FILE_OUT_PATH + "authCodeImage.jpg";
//		byte[] bytes = ImageUtil.genAuthCodeImage(180, 50);
		byte[] bytes = ImageUtil.genAuthCodeImage(180, 50, 4, 100);
		FileUtil.storeFile(path, bytes);
		System.out.println("制作完成");
	}

	/**
	 * 图片去底色
	 */
	@Test
	public void transferAlphaTest() {
		// transferAlpha内部用画板重绘图片，对于网络图片可能会失真
		byte[] photoData = ImageUtil.transferAlpha(Constants.FILE_PATH + "gs.png");
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "gs_去底色.png", photoData);
		System.out.println("图片处理完毕！");
	}

	/**
	 * 去白色背景或指定RGB
	 */
	@Test
	public void cleanBGColorTest() throws Exception {
		// cleanBGColor内部针对指定RGB进行处理，适用于大部分图片
//		byte[] photoData = ImageUtil.cleanBGColor(FileUtil.getFile(Constants.FILE_PATH + "bg_white.png"));
		byte[] photoData = ImageUtil.cleanBGColor(FileUtil.getFile("C:/Users/Administator/Desktop/PDFImage.png"),
				71, 112, 76);

		FileUtil.storeFile(Constants.FILE_OUT_PATH + "bg_white_去底色.png", photoData);
		System.out.println("图片处理完毕！");
	}

	/**
	 * 制作二维码
	 */
	@Test
	public void genBarcodeImageTest() {
		byte[] bytes = ImageUtil.genBarcodeImage("https://www.baidu.com");
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "barcodeImage.jpg", bytes);
		System.out.println("制作完成");
	}

	/**
	 * base64编解码
	 */
	@Test
	public void base64Test() {
		//编码
		String str1 = "son";
		String str = Base64Util.encode(str1);
		System.out.println(str);

		//解码
		String str2 = "c29u";
		byte[] b2 = Base64Util.decode(str2);
		System.out.println(new String(b2));
	}

	/**
	 * 获取pdf模板文本域
	 *
	 * @throws Exception
	 */
	@Test
	public void getPdfFieldsTest() throws Exception {
		List list = PdfUtil.getTemplateFields(FileUtil.getFile(Constants.FILE_PATH + "req_con.pdf"));
		System.out.println(list);
	}

	/**
	 * 根据pdf模板生成pdf
	 *
	 * @throws Exception
	 */
	@Test
	public void genPdfByTemplateTest() throws Exception {
		byte[] pdfTemplateData = FileUtil.getFile(Constants.FILE_PATH + "req_con.pdf");

		Properties properties = new Properties();
		properties.setProperty("cert", "CN=Test");
		properties.setProperty("businessCodeCn", "新办");
		properties.setProperty("name", "张女士");
		properties.setProperty("phone", "15712345678");
		properties.setProperty("idType", "身份证");
		properties.setProperty("idNum", "411328****");
		properties.setProperty("other", "无");

		properties.setProperty("ag_name", "张女士");
		properties.setProperty("ag_sex", "女");
		properties.setProperty("ag_idNum", "411328****");
		properties.setProperty("ag_idType", "身份证");
		properties.setProperty("ag_phone", "15712345678");
		properties.setProperty("ag_telPhone", "0377-66666666");
		properties.setProperty("ag_email", "867096367@qq.com");
		properties.setProperty("ag_other", "无");

		properties.setProperty("req_time", DateUtil.getDateTime());
		properties.setProperty("remark", "无");

		byte[] pdfData = PdfUtil.genPdfByTemplate(pdfTemplateData, properties);
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "个人信息.pdf", pdfData);
	}

	/**
	 * pdf插入图片
	 *
	 * @throws Exception
	 */
	@Test
	public void pdfAddImageTest() throws Exception {
		byte[] pdfData = FileUtil.getFile(Constants.FILE_PATH + "req_con.pdf");
		byte[] photoData = FileUtil.getFile(Constants.FILE_PATH + "gs.png");
		// 可指定图片大小和单位，1为厘米，2位像素
		// byte[] addImagePdf = PdfUtil.pdfAddImage(pdfData, photoData, 1, 100, 100, 100, 100, 2);
		byte[] addImagePdf = PdfUtil.pdfAddImage(pdfData, photoData, 1, 100, 100, 4.3f, 4.3f, 1);
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "个人信息_addImage2.pdf", addImagePdf);
		System.out.println("ok");
	}

	/**
	 * 压缩解压缩
	 *
	 * @throws Exception
	 */
	@Test
	public void zipTest() throws Exception {
		String zipPath = Constants.FILE_OUT_PATH + "file.zip";
		String unzipPath = Constants.FILE_OUT_PATH + "file_unzip";
		String filePath = Constants.FILE_PATH;
		ZipUtil.zipDir(filePath, zipPath, "");
		ZipUtil.unZip(zipPath, unzipPath, "");
		System.out.println("ok");
	}

	/**
	 * 执行命令/脚本
	 * @throws Exception
	 */
	@Test
	public void execTest() throws Exception {
		String ipconfig = ExecSh.exec("ipconfig");
		System.out.println(ipconfig);
	}

	/**
	 * 十六进制转换
	 * @throws Exception
	 */
	@Test
	public void hexTest() throws Exception {
		long time = DateUtil.getCurrentTime();
		System.out.println(time);
		System.out.println(HexUtil.byte2Hex(HexUtil.long2Byte(time)));
		System.out.println(HexUtil.byte2Long(HexUtil.long2Byte(time)));
	}

	/**
	 * 网卡MAC十六进制信息
	 * @throws Exception
	 */
	@Test
	public void getHostMacTest() throws Exception {
		System.out.println(NetWorkUtil.getHostMac("eth0"));
	}

	@Test
	public void getNetWorkListTest() throws Exception {
		// windows测试linux时，进入方法改成windows路径再测
		System.out.println(JSON.toJSONString(NetWorkUtil.getNetworkList()));
	}

	/**
	 * 浮点数运算
	 */
	@Test
	public void arithTest() {
		System.out.println(Arith.round(1.6456, 2));
	}

	/**
	 * 系统监控信息
	 * @throws Exception
	 */
	@Test
	public void monitorTest() throws Exception {
		ServerInfo systemInfo = NetWorkUtil.systemInfo();
		System.out.println(JSON.toJSONString(systemInfo));

		Memory memory = NetWorkUtil.memoryInfo();
		System.out.println(JSON.toJSONString(memory));

		Cpu cpu = NetWorkUtil.cpuInfo();
		System.out.println(JSON.toJSONString(cpu));

		List<Disk> disks = NetWorkUtil.diskInfo();
		System.out.println(JSON.toJSONString(disks));
	}

	/**
	 * 系统提供者
	 */
	@Test
	public void providerTest() {
		// CertUtil.getAllProvider();
		System.out.println(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME));
	}

	/**
	 * x509证书解析
	 */
	@Test
	public void certTest() throws Exception {
		byte[] file = FileUtil.getFile(Constants.FILE_PATH + "ca.cer");
		X509Certificate x509Certificate = CertUtil.getX509Certificate(file);
		System.out.println(x509Certificate.getSubjectDN());
	}

	/**
	 * 解析p7b证书链
	 * @throws Exception
	 */
	@Test
	public void parseCertChainTest() throws Exception {
		byte[] file = FileUtil.getFile("E:/zj/file/cert/p7b/1.p7b");
		List<X509Certificate> list = CertUtil.parseCertChain(Base64Util.decode(file));
		for (X509Certificate cert : list) {
			BigInteger serialNumber = cert.getSerialNumber();
			String sn = HexUtil.byte2Hex(serialNumber.toByteArray()).toUpperCase();
			FileUtil.storeFile(Constants.FILE_OUT_PATH + sn + ".cer", cert.getEncoded());
		}
	}

	/**
	 * 制作p7b证书链
	 * @throws Exception
	 */
	@Test
	public void genCertChainTest() throws Exception {
		ArrayList<X509Certificate> list = new ArrayList<>();
		list.add(CertUtil.getX509Certificate(FileUtil.getFile("F:/infosec/cert/CA61gm/ca61gm.cer")));
		list.add(CertUtil.getX509Certificate(FileUtil.getFile("F:/infosec/cert/CA61gm/SM2_cert.cer")));

		byte[] bytes = CertUtil.genCertChain(list);
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "p7b/1.p7b", bytes);
	}

	/**
	 * 生成RSA密钥对
	 * @throws Exception
	 */
	@Test
	public void genRsaKeyPairTest() throws Exception {
		KeyPair kayPair = RSAUtil.genRSAKeyPair(Constants.RSA_KEY_SIZE_1024);
		PublicKey publicKey = kayPair.getPublic();
		PrivateKey privateKey = kayPair.getPrivate();
		//得到base64编码的公钥/私钥字符串
		String publicKeyString = Base64Util.encode(publicKey.getEncoded());
		String privateKeyString = Base64Util.encode(privateKey.getEncoded());

		boolean checkKeyPair = RSAUtil.checkKeyPair(publicKeyString, privateKeyString);
		System.out.println("公私钥是否匹配：" + checkKeyPair);

		publicKeyString = RSAUtil.getPemPubKey(publicKeyString);
		privateKeyString = RSAUtil.getPemPriKey(privateKeyString);
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "rsa/pubKey.txt", publicKeyString.getBytes());
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "rsa/priKey.txt", privateKeyString.getBytes());
		System.out.println("RSA密钥对存储成功！");
	}

	/**
	 * 生成SM2密钥对
	 * @throws Exception
	 */
	@Test
	public void genSM2KeyPairTest() throws Exception {
		KeyPair keyPair = SM2Util.genKeyPair();
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		//得到base64编码的公钥/私钥字符串
		String publicKeyString = Base64Util.encode(publicKey.getEncoded());
		String privateKeyString = Base64Util.encode(privateKey.getEncoded());

		FileUtil.storeFile(Constants.FILE_OUT_PATH + "sm2/pubKey.txt", publicKeyString.getBytes());
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "sm2/priKey.txt", privateKeyString.getBytes());
		System.out.println("SM2密钥对存储成功！");

		// 运算测试
		byte[] plain = "123456".getBytes();
		byte[] encrypt = SM2Util.encrypt(plain, keyPair.getPublic());
		byte[] decrypt = SM2Util.decrypt(encrypt, keyPair.getPrivate());
		System.out.println("运算测试的原文：" + StringUtil.getString(decrypt));
	}

	/**
	 * 生成SM2密钥对保护数据
	 * @throws Exception
	 */
	@Test
	public void genSM2KeyPairTest2() throws Exception {
		// 制作
		byte[] pubKeyData = FileUtil.getFile(Constants.FILE_OUT_PATH + "sm2/pubKey.txt");
		PublicKey publicKey = SM2Util.createPublicKey(StringUtil.getString(pubKeyData));
		byte[] priKeyData = FileUtil.getFile(Constants.FILE_OUT_PATH + "sm2/priKey.txt");
		PrivateKey privateKey = SM2Util.createPrivateKey(StringUtil.getString(priKeyData));
		DERSequence sequence = SM2Util.genSM2ProtectWith0009(publicKey, "SM4", privateKey);

		FileUtil.storeFile(Constants.FILE_OUT_PATH + "sm2/protect.asn1", sequence.getEncoded());
		System.out.println("SM2密钥对保护数据存储成功！");

		// 解析
//		byte[] priKeyData = FileUtil.getFile(Constants.FILE_OUT_PATH + "sm2/priKey.txt");
//		PrivateKey privateKey = SM2Util.createPrivateKey(StringUtil.getString(priKeyData));
//		KeyPair keyPair = SM2Util.parseSM2ProtectWith0009(privateKey, sequence);

		// 运算测试
//		byte[] plain = "123456".getBytes();
//		byte[] encrypt = SM2Util.encrypt(plain, keyPair.getPublic());
//		byte[] decrypt = SM2Util.decrypt(encrypt, keyPair.getPrivate());
//		System.out.println("运算测试的原文：" + StringUtil.getString(decrypt));
	}

	@Test
	public void clearPemTest() throws Exception {
		byte[] file = FileUtil.getFile(Constants.FILE_OUT_PATH + "rsa/pubKey.txt");
		String string = StringUtil.getString(file);
		System.out.println(string);
		System.out.println(RSAUtil.clearPemPubKey(string));

		byte[] file2 = FileUtil.getFile(Constants.FILE_OUT_PATH + "rsa/priKey.txt");
		String string2 = StringUtil.getString(file2);
		System.out.println(string2);
		System.out.println(RSAUtil.clearPemPriKey(string2));
		System.out.println("解析成功！");
	}

	/**
	 * RSA非对称加解密
	 * @throws Exception
	 */
	@Test
	public void rsaEncryptTest() throws Exception {
		String plain = "plain";
		System.out.println("加密开始，原文数据：" + plain);
		// 加载RSA公钥
		byte[] pubKeyData = FileUtil.getFile(Constants.FILE_OUT_PATH + "rsa/pubKey.txt");
		PublicKey publicKey = RSAUtil.generateP8PublicKey(Base64Util.decode(pubKeyData));
		byte[] encrypt = KeyUtil.encrypt(publicKey, plain.getBytes(), Constants.RSA);

		// 加载RSA私钥
		byte[] priKeyData = FileUtil.getFile(Constants.FILE_OUT_PATH + "rsa/priKey.txt");
		PrivateKey privateKey = RSAUtil.generateP8PrivateKey(Base64Util.decode(priKeyData));
		byte[] decrypt = KeyUtil.decrypt(privateKey, encrypt, Constants.RSA);
		System.out.println("解密完成，解密数据：" + new String(decrypt));
	}

	/**
	 * RSA裸签名验签
	 * @throws Exception
	 */
	@Test
	public void rsaRawSignTest1() throws Exception {
		String plain = "plain";
		System.out.println("签名开始，原文数据：" + plain);
		// 加载RSA私钥
		byte[] priKeyData = FileUtil.getFile(Constants.FILE_OUT_PATH + "rsa/priKey.txt");
		PrivateKey privateKey = RSAUtil.generateP8PrivateKey(Base64Util.decode(priKeyData));
		byte[] signed = KeyUtil.rawSign(null, privateKey, plain.getBytes(),  Constants.SHA256_RSA, "1".getBytes());

		// 加载RSA公钥
		byte[] pubKeyData = FileUtil.getFile(Constants.FILE_OUT_PATH + "rsa/pubKey.txt");
		PublicKey publicKey = RSAUtil.generateP8PublicKey(Base64Util.decode(pubKeyData));
		boolean result = KeyUtil.rawSignVerify(publicKey, plain.getBytes(), signed, Constants.SHA256_RSA, "1".getBytes());
		System.out.println("验签完成，验签结果：" + result);
	}

	@Test
	public void rsaRawSignTest2() throws Exception {
		String plain = "plain";
		System.out.println("签名开始，原文数据：" + plain);

		PrivateKey privateKey = KeyStoreUtil.loadKey(password, Constants.PFX_SUFFIX, FileUtil.getFile(pfxPath));
		System.out.println("签名算法：" + privateKey.getAlgorithm());
		// 使用sha256做摘要
		byte[] signed = KeyUtil.rawSign(null, privateKey, plain.getBytes(), Constants.SHA256_RSA, "1".getBytes());
		FileUtil.storeFile(Constants.FILE_PATH + "/key/rsa/rsa_sha256_rawSigned.txt", Base64Util.encode(signed).getBytes());

		byte[] file = FileUtil.getFile(pfxCertPath);
		X509Certificate x509Certificate = CertUtil.getX509Certificate(file);
		PublicKey publicKey = x509Certificate.getPublicKey();
		boolean result = KeyUtil.rawSignVerify(publicKey, plain.getBytes(), signed, Constants.SHA256_RSA, "1".getBytes());
		System.out.println("验签完成，验签结果：" + result);
	}

	@Test
	public void detachedSignTest() throws Exception {
		String plain = "plain";
		System.out.println("签名开始，原文数据：" + plain);

		byte[] file = FileUtil.getFile(pfxCertPath);
		X509Certificate cert = CertUtil.getX509Certificate(file);
		// 签名
		byte[] signed = KeyUtil.detachedSign(plain.getBytes(), cert);
		FileUtil.storeFile(Constants.FILE_PATH + "/key/rsa/rsa_sha256_detachedSigned.txt", signed);

		// 验签
		PrivateKey privateKey = KeyStoreUtil.loadKey(password, Constants.PFX_SUFFIX, FileUtil.getFile(pfxPath));
		byte[] data = KeyUtil.detachedVerify(signed, privateKey);

		System.out.println("验签完成，解析原文：" + StringUtil.getString(data));
	}

	/**
	 * 加载私钥
	 * @throws Exception
	 */
	@Test
	public void loadKeyTest() throws Exception {
		PrivateKey pk = KeyStoreUtil.loadKey(password, Constants.PFX_SUFFIX, FileUtil.getFile(pfxPath));
		System.out.println("签名算法：" + pk.getAlgorithm());
	}

	/**
	 * pdf签章
	 * @throws Exception
	 */
	@Test
	public void pdfStampTest() throws Exception {
		PdfStampUtil pdfUtil = new PdfStampUtil();
		// 读取keystore ，获得私钥
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(pfxPath), password.toCharArray());
		String alias = ks.aliases().nextElement();
		PrivateKey pk = (PrivateKey) ks.getKey(alias, password.toCharArray());
		// 得到证书链
		Certificate[] chain = ks.getCertificateChain(alias);

		//签章: PDF的RSA签章 = 图片+签名，没有印章和签章结构
		byte[] pdfData = FileUtil.getFile(Constants.FILE_PATH + "2页.pdf");
		byte[] photoData = FileUtil.getFile(Constants.FILE_PATH + "999.png");
		byte[] signedData = pdfUtil.sign(pdfData, photoData,1,100, 100, chain, pk, DigestAlgorithms.SHA1);
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "stamp.pdf", signedData);
		System.out.println("签章成功，文件存储路径为：" + Constants.FILE_OUT_PATH + "stamp.pdf");
	}

	/**
	 * pdf验章
	 * @throws Exception
	 */
	@Test
	public void pdfVerifyTest() throws Exception {
		byte[] pdfData = FileUtil.getFile(Constants.FILE_OUT_PATH + "stamp.pdf");
		PdfStampUtil pdfUtil = new PdfStampUtil();
		StampVerify verify = pdfUtil.verifySign(pdfData);
		System.out.println("验签结果：" + JSON.toJSONString(verify));
	}

	/**
	 * pdf撤章
	 * @throws Exception
	 */
	@Test
	public void pdfStampRemoveTest() throws Exception {
		String pdfStamp = "f:/temp/stamp1.pdf";
		byte[] data = RemovePdfStampUtil.removeStamp(FileUtil.getFile(pdfStamp));
		FileUtil.storeFile("f:/temp/stamp_remove.pdf", data);
		System.out.println("撤章成功");
	}

	@Test
	public void der2base64() {
		String file = Constants.FILE_PATH + "/key/rsa/rsapfx3des-sha1.cer";
		byte[] data = FileUtil.getFile(file);
		FileUtil.storeFile(Constants.FILE_PATH + "/key/rsa/rsapfx3des-sha1_b64.cer", Base64Util.encode(data).getBytes());
	}

	@Test
	public void jks2pfx() throws  Exception {
		String jksPath = Constants.FILE_PATH + "/key/rsa/server.jks";
		String pfxPath = Constants.FILE_PATH + "/key/rsa/server.pfx";
//		KeyStoreUtil.jks2pfx(jksPath, pfxPath, password);
		KeyStoreUtil.jks2pfx(jksPath, pfxPath, "68683556", "11111111");
	}


	@Test
	public void getCertFromPfx() throws  Exception {
		byte[] cert = KeyStoreUtil.getCertFromPfx(password, FileUtil.getFile(pfxPath));
		FileUtil.storeFile(Constants.FILE_PATH + "/key/rsa/zj.cer", cert);

	}

	@Test
	public void readJsonTest() {
		String jsonPath = "F:/infosec/za/v8/自测/case.json";

		byte[] file = FileUtil.getFile(jsonPath);
		Object jsonObject = JSON.parse(file);
		JSONArray array = (JSONArray)jsonObject;
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = (JSONObject)array.get(i);
			System.out.println(obj.get("id"));
		}
	}

	@Test
	public void genP10() throws  Exception {
		String p10Path = Constants.FILE_OUT_PATH + "p10.p10";
		String priKeyPath = Constants.FILE_OUT_PATH + "p10.pri";

		KeyStoreIo keyStoreIo = P10Util.genP10(".pri", "RSA", "CN=Test,O=GS,C=CN");
		FileUtil.storeFile(p10Path, keyStoreIo.getP10().getBytes());
		FileUtil.storeFile(priKeyPath, keyStoreIo.getPriKeyData());
		System.out.println("p10和私钥存储完成");

		String pemP10 = P10Util.getPemP10(keyStoreIo.getP10(), true);
		System.out.println(pemP10);

		byte[] p10Data = Base64Util.decode(FileUtil.getFile(p10Path));
		byte[] pubKeyFormP10 = P10Util.getPubKeyFormP10(p10Data);
		System.out.println(RSAUtil.getPemPubKey(Base64Util.encode(pubKeyFormP10), true));

		boolean verifyP10 = P10Util.verifyP10(p10Data);
		System.out.println("验证p10: " + verifyP10);

		// 从p10中读取公钥做一次加解密测试
		String plain = "plain";
		System.out.println("加密开始，原文数据：" + plain);
		// 加载RSA公钥
		byte[] pubKeyData = P10Util.getPubKeyFormP10(p10Data);
		PublicKey publicKey = RSAUtil.generateP8PublicKey(pubKeyData);
		byte[] encrypt = KeyUtil.encrypt(publicKey, plain.getBytes(), Constants.RSA);

		// 加载RSA私钥
		byte[] priKeyData = FileUtil.getFile(priKeyPath);
		PrivateKey privateKey = RSAUtil.generateP8PrivateKey(priKeyData);
		byte[] decrypt = KeyUtil.decrypt(privateKey, encrypt, Constants.RSA);
		System.out.println("解密完成，解密数据：" + new String(decrypt));
	}

	@Test
	public void makeEnvelop() throws  Exception {
		String plain = "plain";
		byte[] certData = KeyStoreUtil.getCertFromPfx(password, FileUtil.getFile(pfxPath));
		X509Certificate cert = CertUtil.getX509Certificate(certData);
		// 制作数字信封
		byte[] bytes = PKCS7Envelope.makeP7(plain.getBytes(), cert);
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "envelop.asn1", bytes);
		System.out.println("数字信封内容：" + plain + "，制作完成");
	}

	@Test
	public void parseEnvelop() throws  Exception {
		byte[] envData = FileUtil.getFile("E:/Idea/NetSeal/v6/netseal/netseal-app-demo/file/envelop/envelop.asn1");
		// 解数字信封
		PrivateKey privateKey = KeyStoreUtil.loadKey(password, Constants.PFX_SUFFIX, FileUtil.getFile(pfxPath));
		byte[] bytes = PKCS7Envelope.verifyP7(envData, privateKey);
		System.out.println("数字信封内容：" + new String(bytes));
	}

	@Test
	public void encryptWithBC() throws  Exception {
		String str = "123456";

		// bc库sm2加密
		String pubKey = new String(FileUtil.getFile(Constants.FILE_OUT_PATH + "sm2/pubKey.txt"));
		PublicKey publicKey = SM2Util.createPublicKey(pubKey);
		byte[] bytes = SM2Util.encrypt(str.getBytes(), publicKey);
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "sm2/enc_bc.txt", bytes);

		// bc库sm2解密
		String priKey = new String(FileUtil.getFile(Constants.FILE_OUT_PATH + "sm2/priKey.txt"));
		PrivateKey privateKey = SM2Util.createPrivateKey(priKey);
		byte[] decrypt = SM2Util.decrypt(bytes, privateKey);
		System.out.println("解密出原文：" + new String(decrypt));

	}

	@Test
	public void encryptWith0009() throws  Exception {
		String str = "123456";

		// 0009规范加密
		String pubKey = new String(FileUtil.getFile(Constants.FILE_OUT_PATH + "sm2/pubKey.txt"));
		PublicKey publicKey = SM2Util.createPublicKey(pubKey);
		byte[] bytes = SM2Util.encryptWith0009(str.getBytes(), publicKey).getEncoded();
		FileUtil.storeFile(Constants.FILE_OUT_PATH + "sm2/enc."+DateUtil.getCurrentTime()+".asn1", bytes);

		// 0009规范解密
		String priKey = new String(FileUtil.getFile(Constants.FILE_OUT_PATH + "sm2/priKey.txt"));
		PrivateKey privateKey = SM2Util.createPrivateKey(priKey);
		byte[] decrypt = SM2Util.decryptWith0009(bytes, privateKey);
		System.out.println("解密出原文：" + new String(decrypt));
	}

	@Test
	public void analysisAsn1() throws  Exception {
		String path1 = Constants.FILE_PATH + "seal/电子印章_GM0031.seal";
		SealUtil.analysisSeal(FileUtil.getFile(path1));
		String path2 = Constants.FILE_PATH + "seal/电子印章_GB38540.seal";
		SealUtil.analysisSeal(FileUtil.getFile(path2));

		String path3 = Constants.FILE_PATH + "seal/电子签章_GM0031.asn1";
		StampUtil.analysisStamp(FileUtil.getFile(path3));
        String path4 = Constants.FILE_PATH + "seal/电子签章_GB38540.asn1";
        StampUtil.analysisStamp(FileUtil.getFile(path4));


	}

}
