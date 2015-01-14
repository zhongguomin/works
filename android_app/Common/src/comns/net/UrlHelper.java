package comns.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import comns.bytes.ByteHelper;
import comns.system.CustomPrint;

/**
 * @类名: UrlHelper
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-23 下午03:55:58
 * 
 * @描述: 类<code>UrlHelper</code>是提供了网络链接相关函数的类，包括获取指定长度的随机字符串和进行base64编码</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class UrlHelper {

	/** 超时 */
	public static int OVERTIME = 30000;

	/**
	 * 从给定链接获取输入流
	 * 
	 * @param urlStr
	 *            指定的链接
	 * @return 输入流
	 */
	public static InputStream getInputStreamFromUrl(String urlStr) {

		InputStream inputStream = null;

		try {
			URL url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			inputStream = connection.getInputStream();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return inputStream;
	}

	/**
	 * 从给定链接获取文本（未获取到返回 null）
	 * 
	 * @param urlStr
	 *            指定的链接
	 * @return
	 */
	public static String getTextFromUrl(String urlStr) {

		InputStream is = getInputStreamFromUrl(urlStr);

		return ByteHelper.inStream2Str(is, "utf-8");
	}

	/**
	 * 将 URL中的 HTML转义字符串替换成对应的符号
	 * 
	 * @param urlStr
	 *            要处理的 URL
	 * @return 处理后的字符串
	 */
	public static String replaceUrl(String urlStr) {

		return ByteHelper.replaceHtmlString(urlStr);

	}

	/**
	 * 根据链接获得文件名
	 * 
	 * @param urlStr
	 *            文件链接
	 * @return 文件名
	 */
	public static String getFileNameFromUrl(String urlStr) {

		if (urlStr == null || urlStr.length() <= 0) {
			return urlStr;
		}

		String fileName = "";

		int startPos = urlStr.lastIndexOf("/") + 1;
		fileName = urlStr.substring(startPos);

		return ByteHelper.replaceSpecialString(fileName);
	}

	/**
	 * 从文件链接获取文件大小
	 * 
	 * @param urlStr
	 *            文件链接
	 * @return 文件大小
	 */
	public static int getFileSizeFromUrl(String urlStr) {

		int fileSize = 0;

		try {

			URL url = new URL(urlStr);
			System.setProperty("sun.net.client.defaultConnectTimeout", OVERTIME
					+ "");
			System.setProperty("sun.net.client.defaultReadTimeout", OVERTIME
					+ "");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(OVERTIME);// 设置超时
			conn.setReadTimeout(OVERTIME);// 设置超时
			conn.setRequestMethod("GET");
			conn.setRequestProperty(
					"Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Referer", urlStr);
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();

			if (conn.getResponseCode() == 200) {
				fileSize = conn.getContentLength();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileSize;
	}

	/**
	 * 判断某链接是否可用
	 * 
	 * @param urlStr
	 *            链接地址
	 * @return
	 */
	public static boolean isUrlAvailable(String urlStr) {

		boolean isAvailable = false;

		InputStream is = null;
		try {
			URL url = new URL(urlStr);
			URLConnection urlConn = url.openConnection();
			is = urlConn.getInputStream();
		} catch (Exception e) {
			// TODO: handle exception
			isAvailable = false;
		}
		if (is != null) {
			isAvailable = true;
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		} else {
			isAvailable = false;
		}

		CustomPrint.d(UrlHelper.class, "isUrlAvailable:" + isAvailable);

		return isAvailable;
	}
}
