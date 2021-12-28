package com.gs.common.util;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpClientUtil {

	/**
	 * post请求
	 * @param url
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, String message) throws Exception {
		String result = null;
		try {

			if (url.startsWith("https")) {
				result = httpsPost(url, message);

			} else if (url.startsWith("http")) {
				result = httpPost(url, message);
			} else {
				throw new Exception("url error, " + url);
			}
		} catch (Exception e) {
			throw new Exception("http request error, " + e.getMessage());

		}
		return result;
	}

	/**
	 * http协议-post
	 * @param url
	 * @param message
	 * @return
	 * @throws Exception
	 */
	private static String httpPost(String url, String message) throws Exception {
		HttpURLConnection conn = null;
		String result = null;
		try {
			// 获取HttpURLConnection对象
			URL uRL = new URL(url);
			conn = (HttpURLConnection) uRL.openConnection();

			// 设置属性
			conn.setRequestMethod("POST");// 请求方式
			conn.setReadTimeout(30000);// 超时时间 30秒
			conn.setConnectTimeout(10000);
			conn.setDoOutput(true);
			conn.setDoInput(true);

			// 设置http请求头
			conn.setRequestProperty("Connection", "keep-alive"); // http1.1
			conn.setRequestProperty("Content-type", "application/json;charset=utf-8");

			// 把提交的数据以输出流的形式提交到服务器
			OutputStream os = conn.getOutputStream();
			os.write(message.getBytes("utf-8"));
			os.flush();
			os.close();

			// 通过响应码来判断是否连接成功
			if (conn.getResponseCode() == 200) {
				// 获得服务器返回的字节流
				InputStream is = conn.getInputStream();

				// 内存输出流,适合数据量比较小的字符串 和 图片
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = is.read(buf)) != -1) {
					baos.write(buf, 0, len);
				}
				is.close();
				// 可使用 toByteArray() 和 toString() 获取数据。
				result = baos.toString("utf-8");
			} else {
				throw new Exception("response code error, " + conn.getResponseCode() + ", url:" + url);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (conn != null)
				conn.disconnect();
		}

		return result;
	}

	private static String httpsPost(String url, String message) throws Exception {
		HttpsURLConnection conn = null;
		String result = null;
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());

			// 获取HttpsURLConnection对象
			URL console = new URL(url);
			conn = (HttpsURLConnection) console.openConnection();

			// 设置属性
			conn.setRequestMethod("POST");
			conn.setReadTimeout(30000);
			conn.setConnectTimeout(10000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());

			// 设置http请求头
			conn.setRequestProperty("Connection", "keep-alive"); // http1.1
			conn.setRequestProperty("Content-type", "application/json;charset=utf-8");

			// 把提交的数据以输出流的形式提交到服务器
			OutputStream os = conn.getOutputStream();
			os.write(message.getBytes("utf-8"));
			os.flush();
			os.close();
			// 通过响应码来判断是否连接成功
			if (conn.getResponseCode() == 200) {
				// 获得服务器返回的字节流
				InputStream is = conn.getInputStream();

				// 内存输出流,适合数据量比较小的字符串 和 图片
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = is.read(buf)) != -1) {
					baos.write(buf, 0, len);
				}
				is.close();
				// 可使用 toByteArray() 和 toString() 获取数据。
				result = baos.toString("utf-8");
			} else {
				throw new Exception("response code error, " + conn.getResponseCode() + ", url:" + url);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return result;
	}



	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
}
