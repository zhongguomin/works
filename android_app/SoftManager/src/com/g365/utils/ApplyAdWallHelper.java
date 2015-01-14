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
 * 是用来管理广告墙广告的类
 * 
 * @author nova 日期 2012年12月27日14:19:16
 * 
 */
public class ApplyAdWallHelper {

	/** 将广告墙广告传送到 AdWallDetailInfo的字段 */
	public static final String DATA_KEY_AD_SLIDEWALL_INFO = "ImageInfo";
	public static final String DATA_KEY_AD_WALL_INFO = "AppWallDownloadInfo";

	/** 广告墙缓存文件夹 */
	private static final String DEFAULT_FODLER = "Cache/FileCache/";

	private Context context;

	/** 保存文件的文件夹路径 */
	private String fileDir;
	/** 文件路径 */
	private String filePath;

	private OnApplyListener listener;

	/** 标志软件还是游戏 软件 type=1 type=2 游戏 */
	private int type;

	private static int pageNow = 1;

	/**
	 * param context 上下文
	 * 
	 * @param listener
	 *            数据回调监听器
	 */

	public ApplyAdWallHelper(Context context, OnApplyListener listener, int type) {
		this.context = context; // 初始化
		this.fileDir = FileHelper.SDCARD_PATH + DEFAULT_FODLER;
		this.filePath = fileDir + (type == 1 ? "soft" : "game");
		this.listener = listener;
		this.type = type;
	}

	/**
	 * 先读取缓存文件再联网比较是否需要提供新的数据， 并把需要更新的数据重新写入缓存 软件
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
					 * 检查网络是否打开
					 */
					if (NetInfoHelper.isNetworkAvailable(context)) {

						/** 复制输入流到字节数组以便重用 */
						InputStream inputStream = getInputStreamFromUrl(type);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte[] buf = new byte[1024];
						int len = -1;
						while ((len = inputStream.read(buf)) != -1) {
							baos.write(buf, 0, len);
						}

						byte[] data = baos.toByteArray();
						inputStream.close();
						// 从网络获取的列表
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
	 * 先读取缓存文件再联网比较是否需要提供新的数据， 并把需要更新的数据重新写入缓存 游戏
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
					 * 检查网络是否打开
					 */
					if (NetInfoHelper.isNetworkAvailable(context)) {

						/** 复制输入流到字节数组以便重用 */
						InputStream inputStream = getInputStreamFromUrl1(type);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte[] buf = new byte[1024];
						int len = -1;
						while ((len = inputStream.read(buf)) != -1) {
							baos.write(buf, 0, len);
						}

						byte[] data = baos.toByteArray();
						inputStream.close();
						// 从网络获取的列表
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
	 * 显示广告详细信息界面
	 * 
	 * @param context
	 *            上下文
	 * @param AdWallDownloadInfo
	 *            广告墙广告信息
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
	 * 联网获取广告墙列表的输入流 type 1 软件 2 游戏
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
	 * 联网获取广告墙列表的输入流 type 1 软件 2 游戏
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
	 * 从本地缓存文件读取
	 */

	private Map<String, Object> getLocalLists() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 推荐的apklist
		List<AppWallDownloadInfo> applistInfoList = new ArrayList<AppWallDownloadInfo>();
		// 滑动的图片list
		List<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();
		/** 放入map 集合 */
		map.put("a1", imageInfoList);
		map.put("b1", applistInfoList);

		// 判断sd卡是否正常
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

	/** @描述: 获取广告列表后的回调监听器 */
	public interface OnApplyListener {
		/**
		 * 读取本地缓存完成
		 */
		public void readLocalCacheAppInfo(Map<String, Object> maplocal);

		/**
		 * 读取网络新数据完成
		 */
		public void readOnlineNewDataAppInfo(Map<String, Object> mapOnline);

	}
}
