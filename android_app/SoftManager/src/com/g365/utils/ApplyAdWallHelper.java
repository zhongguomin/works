package com.g365.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;

import com.g365.entity.AppWallDownloadInfo;
import com.g365.entity.ImageInfo;
import com.g365.softmanager.AdWall;
import com.g365.softmanager.SlideAdWall;
import com.lllfy.newad.core.AdHelper;

/**
 * ������������ǽ������
 * 
 * @author nova ���� 2012��12��27��14:19:16
 * 
 */
public class ApplyAdWallHelper {

	/** �����ǽ��洫�͵� AdWallDetailInfo���ֶ� */
	public static final String DATA_KEY_AD_SLIDEWALL_INFO = "ImageInfo";
	public static final String DATA_KEY_AD_WALL_INFO = "AppWallDownloadInfo";

	/** ���ǽ�����ļ��� */
	private static final String DEFAULT_FODLER = "Cache/FileCache/";

	private Context context;

	/** �����ļ����ļ���·�� */
	private String fileDir;
	/** �ļ�·�� */
	private String filePath;

	private OnApplyListener listener;

	/** ��־���������Ϸ ��� type=1 type=2 ��Ϸ */
	private int type;

	private static int pageNow = 1;

	/**
	 * param context ������
	 * 
	 * @param listener
	 *            ���ݻص�������
	 */

	public ApplyAdWallHelper(Context context, OnApplyListener listener, int type) {
		this.context = context; // ��ʼ��
		this.fileDir = FileHelper.SDCARD_PATH + DEFAULT_FODLER;
		this.filePath = fileDir + (type == 1 ? "soft" : "game");
		this.listener = listener;
		this.type = type;
	}

	/**
	 * �ȶ�ȡ�����ļ��������Ƚ��Ƿ���Ҫ�ṩ�µ����ݣ� ������Ҫ���µ���������д�뻺�� ���
	 */
	public void showInfoList() {
		new Thread() {

			public void run() {
				try {
					Map<String, Object> map = new HashMap<String, Object>();
					map = getLocalLists();

					if (listener != null) {
						listener.readLocalCacheAppInfo(map);
					}

					/**
					 * ��������Ƿ��
					 */
					if (NetInfoHelper.isNetworkAvailable(context)) {

						/** �������������ֽ������Ա����� */
						InputStream inputStream = getInputStreamFromUrl(type);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte[] buf = new byte[1024];
						int len = -1;
						while ((len = inputStream.read(buf)) != -1) {
							baos.write(buf, 0, len);
						}

						byte[] data = baos.toByteArray();
						inputStream.close();
						// �������ȡ���б�
						Map<String, Object> map2 = new HashMap<String, Object>();
						map2 = PullUtils.getApplistinfo(new String(data));

						if (listener != null) {
							listener.readOnlineNewDataAppInfo(map2);
						}

						if (FileHelper.sdCardIsOk()) {

							File fileDir = new File(
									ApplyAdWallHelper.this.fileDir);
							System.out.println("~~~~~~~~~~~~~~~~"+ApplyAdWallHelper.this.fileDir);
							if (!fileDir.exists()) {
								fileDir.mkdirs();
							}
							File file = new File(
									ApplyAdWallHelper.this.filePath);
							System.out.println("~~~~~~~~~~~~~~~~"+ApplyAdWallHelper.this.filePath);
							if (!file.exists()) {
								file.createNewFile();
							}

							InputStream fileInputStream = new ByteArrayInputStream(
									data);
							FileOutputStream fos = new FileOutputStream(file);
							byte[] buffer = new byte[1024];
							int readNum = -1;
							while ((readNum = fileInputStream.read(buffer)) != -1) {
								fos.write(buffer, 0, readNum);
							}
							fos.close();
							fileInputStream.close();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * �ȶ�ȡ�����ļ��������Ƚ��Ƿ���Ҫ�ṩ�µ����ݣ� ������Ҫ���µ���������д�뻺�� ��Ϸ
	 */
	public void showInfoList1() {
		new Thread() {

			public void run() {
				try {
					Map<String, Object> map = new HashMap<String, Object>();
					map = getLocalLists();

					if (listener != null) {
						listener.readLocalCacheAppInfo(map);
					}

					/**
					 * ��������Ƿ��
					 */
					if (NetInfoHelper.isNetworkAvailable(context)) {

						/** �������������ֽ������Ա����� */
						InputStream inputStream = getInputStreamFromUrl1(type);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte[] buf = new byte[1024];
						int len = -1;
						while ((len = inputStream.read(buf)) != -1) {
							baos.write(buf, 0, len);
						}

						byte[] data = baos.toByteArray();
						inputStream.close();
						// �������ȡ���б�
						Map<String, Object> map2 = new HashMap<String, Object>();
						map2 = PullUtils.getApplistinfo(new String(data));

						if (listener != null) {
							listener.readOnlineNewDataAppInfo(map2);
						}

						if (FileHelper.sdCardIsOk()) {

							File fileDir = new File(
									ApplyAdWallHelper.this.fileDir);
							System.out.println("~~~~~~~~~~~~~~~~"+ApplyAdWallHelper.this.fileDir);
							if (!fileDir.exists()) {
								fileDir.mkdirs();
							}
							File file = new File(
									ApplyAdWallHelper.this.filePath);
							System.out.println("~~~~~~~~~~~~~~~~"+ApplyAdWallHelper.this.filePath);
							if (!file.exists()) {
								file.createNewFile();
							}

							InputStream fileInputStream = new ByteArrayInputStream(
									data);
							FileOutputStream fos = new FileOutputStream(file);
							byte[] buffer = new byte[1024];
							int readNum = -1;
							while ((readNum = fileInputStream.read(buffer)) != -1) {
								fos.write(buffer, 0, readNum);
							}
							fos.close();
							fileInputStream.close();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * ��ʾ�����ϸ��Ϣ����
	 * 
	 * @param context
	 *            ������
	 * @param AdWallDownloadInfo
	 *            ���ǽ�����Ϣ
	 */
	public static void showAdDetail(Context context,
			AppWallDownloadInfo appWallDownloadInfo) {

		Intent intent = new Intent(context, AdWall.class);
		intent.putExtra(DATA_KEY_AD_WALL_INFO, appWallDownloadInfo);
		context.startActivity(intent);
	}

	public static void showSlideAdDetail(Context context, ImageInfo imageInfo) {

		Intent intent = new Intent(context, SlideAdWall.class);
		intent.putExtra(DATA_KEY_AD_SLIDEWALL_INFO, imageInfo);
		context.startActivity(intent);
	}

	/**
	 * ������ȡ���ǽ�б�������� type 1 ��� 2 ��Ϸ
	 * 
	 * @return
	 */
	private InputStream getInputStreamFromUrl(int pageNow) {
		// String kk="http://cp.g365.cn/app.php?userid=1&type=1&pageno=1";
		// String kk="http://cp.g365.cn/app.php?userid=1&type=2&pageno=1";
		String urlStr = "http://cp.g365.cn/app.php?userid="
				+ AdHelper.getUserId(context) + "&type=1" + "&pageno="
				+ pageNow;
		pageNow++;
		return UrlHelper.getInputStreamFromUrl(urlStr);
	}

	/**
	 * ������ȡ���ǽ�б�������� type 1 ��� 2 ��Ϸ
	 * 
	 * @return
	 */
	private InputStream getInputStreamFromUrl1(int pageNow) {
		// String kk="http://cp.g365.cn/app.php?userid=1&type=1&pageno=1";
		// String kk="http://cp.g365.cn/app.php?userid=1&type=1&pageno=2";
		// String kk="http://cp.g365.cn/app.php?userid=1&type=2&pageno=1";
		String urlStr = "http://cp.g365.cn/app.php?userid="
				+ AdHelper.getUserId(context) + "&type=2" + "&pageno="
				+ pageNow;
		pageNow++;
		return UrlHelper.getInputStreamFromUrl(urlStr);
	}

	/**
	 * �ӱ��ػ����ļ���ȡ
	 */

	private Map<String, Object> getLocalLists() {
		Map<String, Object> map = new HashMap<String, Object>();
		// �Ƽ���apklist
		List<AppWallDownloadInfo> applistInfoList = new ArrayList<AppWallDownloadInfo>();
		// ������ͼƬlist
		List<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();
		/** ����map ���� */
		map.put("a1", imageInfoList);
		map.put("b1", applistInfoList);

		// �ж�sd���Ƿ�����
		if (FileHelper.sdCardIsOk()) {
			try {
				File fileDir = new File(this.fileDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				File file = new File(this.filePath);
				if (!file.exists()) {
					file.createNewFile();
				} else {

					FileInputStream fis = new FileInputStream(file);
					BufferedInputStream bis = new BufferedInputStream(fis);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int readNum = -1;
					while ((readNum = bis.read(buffer)) != -1) {
						baos.write(buffer, 0, readNum);
					}
					byte[] data = baos.toByteArray();
					map = PullUtils.getApplistinfo(new String(data));
					bis.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	/** @����: ��ȡ����б��Ļص������� */
	public interface OnApplyListener {
		/**
		 * ��ȡ���ػ������
		 */
		public void readLocalCacheAppInfo(Map<String, Object> maplocal);

		/**
		 * ��ȡ�������������
		 */
		public void readOnlineNewDataAppInfo(Map<String, Object> mapOnline);

	}
}
