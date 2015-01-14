package comns.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import comns.bytes.ByteHelper;
import comns.system.CustomPrint;

/**
 * @����: UrlHelper
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-23 ����03:55:58
 * 
 * @����: ��<code>UrlHelper</code>���ṩ������������غ������࣬������ȡָ�����ȵ�����ַ����ͽ���base64����</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class UrlHelper {

	/** ��ʱ */
	public static int OVERTIME = 30000;

	/**
	 * �Ӹ������ӻ�ȡ������
	 * 
	 * @param urlStr
	 *            ָ��������
	 * @return ������
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
	 * �Ӹ������ӻ�ȡ�ı���δ��ȡ������ null��
	 * 
	 * @param urlStr
	 *            ָ��������
	 * @return
	 */
	public static String getTextFromUrl(String urlStr) {

		InputStream is = getInputStreamFromUrl(urlStr);

		return ByteHelper.inStream2Str(is, "utf-8");
	}

	/**
	 * �� URL�е� HTMLת���ַ����滻�ɶ�Ӧ�ķ���
	 * 
	 * @param urlStr
	 *            Ҫ����� URL
	 * @return �������ַ���
	 */
	public static String replaceUrl(String urlStr) {

		return ByteHelper.replaceHtmlString(urlStr);

	}

	/**
	 * �������ӻ���ļ���
	 * 
	 * @param urlStr
	 *            �ļ�����
	 * @return �ļ���
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
	 * ���ļ����ӻ�ȡ�ļ���С
	 * 
	 * @param urlStr
	 *            �ļ�����
	 * @return �ļ���С
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
			conn.setConnectTimeout(OVERTIME);// ���ó�ʱ
			conn.setReadTimeout(OVERTIME);// ���ó�ʱ
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
	 * �ж�ĳ�����Ƿ����
	 * 
	 * @param urlStr
	 *            ���ӵ�ַ
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
