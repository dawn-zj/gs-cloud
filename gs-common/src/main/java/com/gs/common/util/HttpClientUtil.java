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
	 * get请求
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static byte[] get(String url) throws Exception {
		return get(url, "");
	}

	/**
	 * get请求
	 * @param url
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static byte[] get(String url, String message) throws Exception {
		byte[] result = null;
		try {
			if (url.startsWith("https")) {
				result = httpsSend(url, "GET", message);

			} else if (url.startsWith("http")) {
				result = httpSend(url,"GET", message);
			} else {
				throw new Exception("url error, " + url);
			}
		} catch (Exception e) {
			throw new Exception("request error, " + e.getMessage());
		}
		return result;
	}

	/**
	 * post请求
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static byte[] post(String url) throws Exception {
		return post(url, "");
	}

	/**
	 * post请求
	 * @param url
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static byte[] post(String url, String message) throws Exception {
		byte[] result = null;
		try {
			if (url.startsWith("https")) {
				result = httpsSend(url, "POST", message);
			} else if (url.startsWith("http")) {
				result = httpSend(url,"POST", message);
			} else {
				throw new Exception("url error, " + url);
			}
		} catch (Exception e) {
			throw new Exception("request error, " + e.getMessage());

		}
		return result;
	}

	/**
	 *
	 * @param url url
	 * @param methodType get/post
	 * @param isSendMessage 发送消息
	 * @return byte[]
	 * @throws Exception
	 */
	private static byte[] httpSend(String url, String methodType, String message) throws Exception {
		HttpURLConnection connection = null;
		byte[] result = null;
		try {
			URL urlInstance = new URL(url);
			connection = (HttpURLConnection) urlInstance.openConnection();
			// 设置属性
			connection.setRequestMethod(methodType);
			connection.setReadTimeout(30000);
			connection.setConnectTimeout(10000);
			connection.setDoOutput(true);
			connection.setDoInput(true);

			// 设置http请求头
			connection.setRequestProperty("Connection", "keep-alive"); // http1.1
			connection.setRequestProperty("Content-type", "application/json;charset=utf-8");
			connection.setRequestProperty("Content-Length", "0");

			// 发送消息
			if (StringUtil.isNotBlank(message)) {
				// 把提交的数据以输出流的形式提交到服务器
				OutputStream os = connection.getOutputStream();
				os.write(message.getBytes("utf-8"));
			}

			// 通过响应码来判断是否连接成功
			if (connection.getResponseCode() == 200) {
				// 获得服务器返回的字节流
				InputStream is = connection.getInputStream();

				// 内存输出流,适合数据量比较小的字符串 和 图片
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = is.read(buf)) != -1) {
					baos.write(buf, 0, len);
				}
				is.close();
				// 可使用 toByteArray() 和 toString() 获取数据。
				result = baos.toByteArray();
			} else {
				throw new Exception("response code error, " + connection.getResponseCode());
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if (connection != null)
				connection.disconnect();
		}

		return result;
	}

	/**
	 *
	 * @param url url
	 * @param methodType get/post
	 * @param isSendMessage 发送消息
	 * @return byte[]
	 * @throws Exception
	 */
	private static byte[] httpsSend(String url, String methodType, String message) throws Exception {
		HttpsURLConnection connection = null;
		byte[] result = null;
		try {

			URL urlInstance = new URL(url);
			connection = (HttpsURLConnection) urlInstance.openConnection();
			// 设置属性
			connection.setRequestMethod(methodType);
			connection.setReadTimeout(30000);
			connection.setConnectTimeout(10000);
			connection.setDoOutput(true);
			connection.setDoInput(true);

			// 设置http请求头
			connection.setRequestProperty("Connection", "keep-alive"); // http1.1
			connection.setRequestProperty("Content-type", "application/json;charset=utf-8");
			connection.setRequestProperty("Content-Length", "0");

			// https
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			connection.setSSLSocketFactory(sc.getSocketFactory());

			// 发送消息
			if (StringUtil.isNotBlank(message)) {
				// 把提交的数据以输出流的形式提交到服务器
				OutputStream os = connection.getOutputStream();
				os.write(message.getBytes("utf-8"));
			}

			// 通过响应码来判断是否连接成功
			if (connection.getResponseCode() == 200) {
				// 获得服务器返回的字节流
				InputStream is = connection.getInputStream();

				// 内存输出流,适合数据量比较小的字符串 和 图片
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = is.read(buf)) != -1) {
					baos.write(buf, 0, len);
				}
				is.close();
				// 可使用 toByteArray() 和 toString() 获取数据。
				result = baos.toByteArray();
			} else {
				throw new Exception("response code error, " + connection.getResponseCode());
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if (connection != null)
				connection.disconnect();
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

	public static void main(String[] args) throws Exception {
		String url = "https://www.baidu.com";
		byte[] bytes = get(url);
		System.out.println(StringUtil.getString(bytes));
	}
}
