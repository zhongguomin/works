package com.g365.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UrlHelper {

	/** 超时 */
	public static int OVERTIME = 30000;
	/**
	 * 从给定链接获取输入流  发送get请求
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
			e.printStackTrace();
		}

		return inputStream;
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
	 * 从给定链接获取文本
	 * 
	 * @param urlStr
	 *            指定的链接
	 * @return
	 */
	public static String getTextFromUrl(String urlStr) {

		String text = null;

		InputStream is = getInputStreamFromUrl(urlStr);

		if (is != null) {

			try {
				BufferedInputStream bis = new BufferedInputStream(is);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int position = 0;
				while ((position = bis.read()) != -1) {
					baos.write((byte) position);
					text = baos.toString();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return text;
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
	 * 发生get请求获取输入流
	 * @param urlStr 地址
	 * @return
	 */
	public static String getInputStreamString(String urlStr) {
		InputStream inputStream = null;
		try {
			
			URL url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			inputStream = connection.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = inputStream.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			byte[] data = baos.toByteArray();
			inputStream.close();
			connection.disconnect();
			String videoUrl = new String(data);
			return videoUrl;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
