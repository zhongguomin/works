package com.g365.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.g365.entity.AppPage;
import com.g365.entity.AppWallDownloadInfo;
import com.g365.entity.Feedback;
import com.g365.entity.ImageInfo;
import com.g365.entity.UpdataInfo;

public class PullUtils {

	/**
	 * ������� ͨ��pull��ʽ������̨���ؽ����ݱ����ڷ�װ����
	 */
	public static List<UpdataInfo> parseUpdate(String returnText) {
		List<UpdataInfo> updates = null;
		try {
			XmlPullParser pullParser = Xml.newPullParser();
			InputStream inputStream = new ByteArrayInputStream(
					returnText.getBytes());
			pullParser.setInput(inputStream, "UTF-8");
			// ������һ���¼�
			int event = pullParser.getEventType();
			UpdataInfo updataInfo = null;
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				// �жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼�
				case XmlPullParser.START_DOCUMENT:
					// ��ʼ������
					updates = new ArrayList<UpdataInfo>();
					break;
				// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�
				case XmlPullParser.START_TAG:

					if ("ApkItemInfo".equals(pullParser.getName())) {
						updataInfo = new UpdataInfo();
						updataInfo.setUid(Integer.parseInt(pullParser
								.getAttributeValue(0)));
						updataInfo.setType(pullParser.getAttributeValue(1));
						updataInfo.setTid(Integer.parseInt(pullParser
								.getAttributeValue(2)));
						updataInfo.setIconurl(pullParser.getAttributeValue(3));
						updataInfo.setName(pullParser.getAttributeValue(4));
						updataInfo.setPackname(pullParser.getAttributeValue(5));
						updataInfo.setVersion(pullParser.getAttributeValue(6));
						updataInfo.setVersioncode(Integer.parseInt(pullParser
								.getAttributeValue(7)));
						updataInfo.setMd5hash(pullParser.getAttributeValue(8));
						updataInfo.setStar(Integer.parseInt(pullParser
								.getAttributeValue(9)));
						updataInfo.setSize(Integer.parseInt(pullParser
								.getAttributeValue(10)));
						updataInfo.setUrl(pullParser.getAttributeValue(11));
						updataInfo.setLastdate(Integer.parseInt(pullParser
								.getAttributeValue(12)));
					}

					break;
				case XmlPullParser.END_TAG:
					if ("ApkItemInfo".equals(pullParser.getName())) {
						updates.add(updataInfo);
						updataInfo = null;
					}
					break;
				}
				event = pullParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("-------�������������غ������---------" + updates);
		return updates;

	}

	/**
	 * Ӧ���Ƽ�pull����
	 * 
	 * @param imageurl
	 * @return
	 */
	public static Map<String, Object> getApplistinfo(String imageStr) {

		System.out.println("cp:"
				+ imageStr.substring(
						imageStr.indexOf("<cp>") + "<cp>".length(),
						imageStr.indexOf("</cp>")));
		System.out.println("ap:"
				+ imageStr.substring(
						imageStr.indexOf("<ap>") + "<ap>".length(),
						imageStr.indexOf("</ap>")));
		System.out.println("total:"
				+ imageStr.substring(
						imageStr.indexOf("<total>") + "<total>".length(),
						imageStr.indexOf("</total>")));

		Map<String, Object> map = new HashMap<String, Object>();
		// �Ƽ���apklist
		List<AppWallDownloadInfo> applistInfoList = new ArrayList<AppWallDownloadInfo>();
		// ������ͼƬlist
		List<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();

		// ҳ��
		// List<AppPage> appPages=new ArrayList<AppPage>();
		AppPage appPage = new AppPage();
		appPage.setCurrentPage(Integer.parseInt(imageStr.substring(
				imageStr.indexOf("<cp>") + "<cp>".length(),
				imageStr.indexOf("</cp>"))));
		appPage.setAllPage(Integer.parseInt(imageStr.substring(
				imageStr.indexOf("<ap>") + "<ap>".length(),
				imageStr.indexOf("</ap>"))));
		appPage.setTotalSum(Integer.parseInt(imageStr.substring(
				imageStr.indexOf("<total>") + "<total>".length(),
				imageStr.indexOf("</total>"))));
		System.out.println("-------------------------"
				+ appPage.getCurrentPage());

		/**
		 * �����ǩ
		 */
		String tag = "";
		try {
			XmlPullParser parser = Xml.newPullParser();
			InputStream inputStream = new ByteArrayInputStream(
					imageStr.getBytes());
			parser.setInput(inputStream, "UTF-8");
			ImageInfo imageInfo = null;
			AppWallDownloadInfo appWallDownloadInfo = null;
			// ������һ���¼�
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				// �жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼�
				case XmlPullParser.START_DOCUMENT:
					// ��ʼ������
					applistInfoList = new ArrayList<AppWallDownloadInfo>();
					imageInfoList = new ArrayList<ImageInfo>();
					// appPages=new ArrayList<AppPage>();

					break;
				case XmlPullParser.START_TAG:

					// ͼƬ��ǩ
					if ("switching".equals(parser.getName())) {
						tag = "switching";
					}
					if ("list".equals(parser.getName())) {
						tag = "list";
					}
					if ("slide".equals(parser.getName())) {
						imageInfo = new ImageInfo();

					}
					if ("image".equals(parser.getName())) {
						String image = parser.nextText();
						imageInfo.setApp_imageurl(image);
					}

					// app ��ǩ
					if ("app".equals(parser.getName())) {
						appWallDownloadInfo = new AppWallDownloadInfo();

					}
					if ("id".equals(parser.getName())) {
						int ids = Integer.parseInt(parser.nextText());
						appWallDownloadInfo.setApp_id(ids);
					}
					if ("name".equals(parser.getName())) {
						if ("list".equals(tag)) {
							String name = parser.nextText();
							appWallDownloadInfo.setApp_Name(name);
						} else if ("switching".equals(tag)) {
							String names = parser.nextText();
							imageInfo.setApps_Name(names);
						}
					}

					if ("download".equals(parser.getName())) {
						if ("list".equals(tag)) {
							String download = parser.nextText();
							appWallDownloadInfo.setApp_download(download);

						} else if ("switching".equals(tag)) {
							String downloads = parser.nextText();
							imageInfo.setApps_downloads(downloads);

						}
					}

					if ("sdkversion".equals(parser.getName())) {
						if ("list".equals(tag)) {
							String sdkversion = parser.nextText();
							appWallDownloadInfo.setApp_sdkversion(sdkversion);
						} else if ("switching".equals(tag)) {
							String sdkversion1 = parser.nextText();
							imageInfo.setApps__sdkversion(sdkversion1);
						}
					}
					if ("intro".equals(parser.getName())) {
						if ("list".equals(tag)) {
							String intro = parser.nextText();
							appWallDownloadInfo.setApp_intro(intro);
						} else if ("switching".equals(tag)) {
							String intros = parser.nextText();
							imageInfo.setApps_intro(intros);
						}
					}
					if ("icon".equals(parser.getName())) {
						if ("list".equals(tag)) {
							String icon = parser.nextText();
							appWallDownloadInfo.setApp_icon(icon);
						} else if ("switching".equals(tag)) {
							String icons = parser.nextText();
							imageInfo.setApps_icon(icons);
						}

					}

					if ("score".equals(parser.getName())) {
						int scores = Integer.parseInt(parser.nextText());
						appWallDownloadInfo.setApp_score(scores);
					}
					if ("size".equals(parser.getName())) {
						int sizes = Integer.parseInt(parser.nextText());
						appWallDownloadInfo.setApp_size(sizes);

					}
					if ("packname".equals(parser.getName())) {
						String packnames = parser.nextText();
						appWallDownloadInfo.setPackagename(packnames);
					}
					if ("versioncode".equals(parser.getName())) {
						int versioncodes = Integer.parseInt(parser.nextText());
						appWallDownloadInfo.setVersioncode(versioncodes);
					}
					break;
				case XmlPullParser.END_TAG:
					/** ����ͼƬ */
					if ("slide".equals(parser.getName())) {
						imageInfoList.add(imageInfo);
						imageInfo = null;
					}

					/** app��Ϣ */
					if ("app".equals(parser.getName())) {
						applistInfoList.add(appWallDownloadInfo);
						appWallDownloadInfo = null;
					}
					break;

				}
				event = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/** ����map ���� */
		map.put("a1", imageInfoList);
		map.put("b1", applistInfoList);
		map.put("c1", appPage);

		System.out.println("�������������---" + map.get("c1"));
		return map;

	}

	/**
	 * �û�����������
	 * 
	 * @param uri
	 * @return
	 */
	public static List<Feedback> getLastVideos(String uri) {
		InputStream inStream = null;
		try {
			// String title = new String(uri.getBytes("UTF-8"));
			String str = new String(uri.getBytes("UTF-8"), "ISO-8859-1");
			URL url = new URL(str);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			inStream = conn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return parseXML(inStream);
	}
	
	
	
	public static String getLastVideos1(String uri) {
		InputStream inStream = null;
		String str=null;
		try {
			// String title = new String(uri.getBytes("UTF-8"));
			str = new String(uri.getBytes("UTF-8"), "ISO-8859-1");
			URL url = new URL(str);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			inStream = conn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return str;
	}

	/**
	 * ͨ��pull��ʽ������̨���ؽ����ݱ����ڷ�װ����
	 * 
	 * @param inStream
	 * @return
	 */
	private static List<Feedback> parseXML(InputStream inStream) {
		List<Feedback> videos = null;
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(inStream, "UTF-8");
			int event = parser.getEventType();
			Feedback feedback = null;
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					videos = new ArrayList<Feedback>();
					break;

				case XmlPullParser.START_TAG:

					if ("re".equals(parser.getName())) {
						feedback = new Feedback();
						feedback.setName(parser.nextText());
					}
					break;

				case XmlPullParser.END_TAG:

					if ("Resource".equals(parser.getName())) {
						videos.add(feedback);
						feedback = null;
					}
					break;
				}
				event = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return videos;
	}
}
