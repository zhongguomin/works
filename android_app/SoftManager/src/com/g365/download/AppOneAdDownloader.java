package com.g365.download;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.g365.download.interfaces.AppDownloadProgressListener;
import com.g365.entity.AppResInit;
import com.g365.entity.AppWallDownloadInfo;
import com.g365.utils.AppInfoHelper;
import com.g365.utils.AppStateChangeReceiver;
import com.g365.utils.ByteHelper;
import com.g365.utils.CustomNotification;
import com.g365.utils.CustomPrint;
import com.g365.utils.SystemHelper;
import com.g365.utils.UrlHelper;
import comns.system.SystemIntent;

/**
 * 
 * @author Administrator 广告墙单个广告的下载类
 * 
 */
public class AppOneAdDownloader {

	/** 下载进度改变广播的 Action */
	public static final String ACTION_AD_DOWNLOAD_CHANCE = "action_ad_download_change";

	/** 发送广告下载状态的 AdWallDownloadInfo字段 */
	public static final String DATA_KEY_ONEADWALL_DOWNLOAD_INFO = "appwalldownloadinfo";

	private Context context;
	private AppWallDownloadInfo appWallDownloadInfo;
	private AppFileDownloader downloader;

	private CustomNotification notification;

	public static synchronized AppOneAdDownloader getInstance(Context context,
			AppWallDownloadInfo appWallDownloadInfo) {

		AppOneAdDownloader appOneAdDownloader = AppDownloadManager
				.getAppOneAdDownloader(appWallDownloadInfo.app_download);

		if (appOneAdDownloader == null) {
			appOneAdDownloader = new AppOneAdDownloader(context,
					appWallDownloadInfo);
			AppDownloadManager.addAppOneAdDownloader(
					appWallDownloadInfo.app_download, appOneAdDownloader);

		}

		return appOneAdDownloader;

	}

	AppOneAdDownloader(Context context,
			AppWallDownloadInfo appWallDownloadInfo) {
		this.context = context;
		this.appWallDownloadInfo = appWallDownloadInfo;
		AppResInit.initNotificationDownloadRes();
	}

	/**
	 * 先判断文件是否存在且完整且是我们自己的，若是则提示安装，若不是则重新下载
	 */
	public void startDownload() {

		//放入多线程
		new Thread() {
			public void run() {
				final String filePath = AppFileDownloader
						.getDefaultPath(appWallDownloadInfo.app_download);
				// 文件完整
				if (AppInfoHelper.isApKFileOk(context, filePath)) {

					File file = new File(filePath);
					AppDefaultStateHandler stateHandler = new AppDefaultStateHandler(
							context);

					if (stateHandler
							.isNewFile(appWallDownloadInfo.app_download)) {
						stateHandler.addNewFile(
								appWallDownloadInfo.app_download,
								(int) file.length(),
								AppFileDownloader.STATE_FINISH);
					} else {
						stateHandler.updateFileState(
								appWallDownloadInfo.app_download,
								AppFileDownloader.STATE_FINISH);
					}

					AppAdDefaultStateHandler aStateHandler = new AppAdDefaultStateHandler(
							context);

					if (aStateHandler.isNewFile(
							appWallDownloadInfo.packagename,
							appWallDownloadInfo.versioncode)) {

						aStateHandler.addNewFile(
								appWallDownloadInfo.app_download,
								appWallDownloadInfo.packagename,
								appWallDownloadInfo.versioncode,
								appWallDownloadInfo.app_id,
								AppAdDefaultStateHandler.STATE_FINISH);
					} else {
						aStateHandler.updateFile(
								appWallDownloadInfo.app_download,
								appWallDownloadInfo.packagename,
								appWallDownloadInfo.versioncode,
								appWallDownloadInfo.app_id,
								AppAdDefaultStateHandler.STATE_FINISH);
					}

					AppStateChangeReceiver.sendStateChange(context,
							AppStateChangeReceiver.getAppWallStateInfo(
									appWallDownloadInfo,
									AppFileDownloader.STATE_FINISH));
					AppDownloadManager
							.delAppOneAdDownloader(appWallDownloadInfo.app_download);
					SystemIntent.installApk(context, filePath);

				} else {
					prepareDownload();
				}
			};
		}.start();

	}

	/**
	 * 初始化下载资源开始下载
	 */
	private void prepareDownload() {

		if (downloader == null) {

			AppStateChangeReceiver
					.sendStateChange(context, AppStateChangeReceiver
							.getAppWallStateInfo(appWallDownloadInfo,
									AppFileDownloader.STATE_PAUSE));
			AppDefaultStateHandler stateHandler = new AppDefaultStateHandler(
					context);

			if (stateHandler.isNewFile(appWallDownloadInfo.app_download)) {
				stateHandler
						.addNewFile(
								appWallDownloadInfo.app_download,
								UrlHelper
										.getFileSizeFromUrl(appWallDownloadInfo.app_download),
								AppFileDownloader.STATE_PAUSE);

			} else {
				stateHandler.updateFileState(appWallDownloadInfo.app_download,
						AppFileDownloader.STATE_PAUSE);
			}

			downloader = new AppFileDownloader(context,
					appWallDownloadInfo.app_download,
					new AppDefaultStateHandler(context),
					new AppDownloadProgressListener() {

						public void onDownload(int downSize, int fileSize,
								int speed, boolean interrupt) {

							AppWallDownloadInfo sendInfo = getAppWallDownloadInfo(
									appWallDownloadInfo, downSize, fileSize,
									speed);

							if (!interrupt) {
								if (downSize >= fileSize) {
									AppStateChangeReceiver.sendStateChange(
											context,
											AppStateChangeReceiver
													.getAppWallStateInfo(
															appWallDownloadInfo,
															AppFileDownloader.STATE_FINISH));

									AppDownloadManager
											.delAppOneAdDownloader(appWallDownloadInfo.app_download);

									if (notification != null) {
										notification.cancel();
									}

									AppAdDefaultStateHandler stateHandler = new AppAdDefaultStateHandler(
											context);

									if (stateHandler.isNewFile(
											appWallDownloadInfo.packagename,
											appWallDownloadInfo.versioncode)) {
										stateHandler
												.addNewFile(
														appWallDownloadInfo.app_download,
														appWallDownloadInfo.packagename,
														appWallDownloadInfo.versioncode,
														appWallDownloadInfo.app_id,
														AppAdDefaultStateHandler.STATE_FINISH);
									} else {
										stateHandler
												.updateFile(
														appWallDownloadInfo.app_download,
														appWallDownloadInfo.packagename,
														appWallDownloadInfo.versioncode,
														appWallDownloadInfo.app_id,
														AppAdDefaultStateHandler.STATE_FINISH);
									}

									SystemIntent
											.installApk(
													context,
													AppFileDownloader
															.getDefaultPath(appWallDownloadInfo.app_download));
									/* 向接口提交数据 */
									// http://cp.g365.cn/app_download.php?userid=15&type=1&aid=2
									try {

										String[] info = AppInfoHelper
												.getInitInfo(context);
										// http://cp.g365.cn/app_download.php?userid=1&type=1&aid=1
										// http://cp.g365.cn/app_download.php?userid=15&type=1&aid=2
										String urlStr = "http://cp.g365.cn/app_download.php?userid=1&type=1&aid=1";

										int result = Integer.parseInt(UrlHelper
												.getTextFromUrl(urlStr));

										CustomPrint
												.d(getClass(),
														"adwall download post："
																+ (result == 1 ? "success"
																		: "fail"));
									} catch (Exception e) {
										e.printStackTrace();
									}

								} else {

									if (notification != null) {
										notification
												.updateRemoteViews(
														getDownloadRemoteViews(
																context,
																sendInfo),
														CustomNotification.MODE_SHOW_ONGOING);
									}
								}

								sendAppWallDownloadInfo(context, sendInfo);
							} else {
								AppStateChangeReceiver
										.sendStateChange(
												context,
												AppStateChangeReceiver
														.getAppWallStateInfo(
																appWallDownloadInfo,
																AppFileDownloader.STATE_RESUME));

								if (notification != null) {
									notification.cancel();
								}
							}
						}
					});
		}

		if (notification == null) {
			notification = new CustomNotification(context,
					appWallDownloadInfo.app_id,
					android.R.drawable.stat_sys_download,
					appWallDownloadInfo.app_Name,
					SystemHelper.MODE_PI_ACTIVITY, new Intent());
		}

		notification.updateRemoteViews(
				getDownloadRemoteViews(context, appWallDownloadInfo),
				CustomNotification.MODE_SHOW_ONGOING);
		download();
	}

	/**
	 * 开始下载
	 */
	private void download() {
		new Thread() {

			public void run() {

				AppStateChangeReceiver.sendStateChange(context,
						AppStateChangeReceiver.getAppWallStateInfo(
								appWallDownloadInfo,
								AppFileDownloader.STATE_PAUSE));
				downloader.startDownload();
			}
		}.start();

	}

	/**
	 * 停止下载
	 */
	public void stopDownload() {

		if (downloader != null) {
			downloader.stopDownload();
		}

		if (notification != null) {
			notification.cancel();
		}
		AppStateChangeReceiver.sendStateChange(context, AppStateChangeReceiver
				.getAppWallStateInfo(appWallDownloadInfo,
						AppFileDownloader.STATE_RESUME));
	}

	/**
	 * 
	 * @return 是否正在下载
	 */
	public boolean isDownloading() {

		if (downloader != null) {
			return downloader.isDownloading();
		}
		return false;

	}

	/**
	 * 获取广告下载信息类
	 * 
	 * @param appWallDownloadInfo
	 * @param curFileSize
	 *            已下载大小
	 * @param fileSize
	 *            文件总大小
	 * @param speed
	 *            下载速度
	 * @return
	 */
	public static AppWallDownloadInfo getAppWallDownloadInfo(
			AppWallDownloadInfo appWallDownloadInfo, int curFileSize,
			int fileSize, int speed) {
		appWallDownloadInfo.curFileSize = curFileSize;
		appWallDownloadInfo.fileSize = fileSize;
		appWallDownloadInfo.speed = speed;
		return appWallDownloadInfo;

	}

	public static void sendAppWallDownloadInfo(Context context,
			AppWallDownloadInfo appWallDownloadInfo) {
		Intent intent = new Intent(ACTION_AD_DOWNLOAD_CHANCE);
		intent.putExtra(DATA_KEY_ONEADWALL_DOWNLOAD_INFO, appWallDownloadInfo);
		context.sendBroadcast(intent);
	}

	/**
	 * 获取广告墙广告下载的通知栏视图
	 * 
	 * @param context
	 *            上下文
	 * @param appWallDownloadInfo
	 *            广告墙下载信息
	 * @return
	 */
	public static RemoteViews getDownloadRemoteViews(Context context,
			AppWallDownloadInfo appWallDownloadInfo) {

		int progress = (appWallDownloadInfo.fileSize == 0) ? 0
				: appWallDownloadInfo.curFileSize * 100
						/ appWallDownloadInfo.fileSize;

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				AppResInit.r_layout_common_notification_download);
		remoteViews.setTextViewText(
				AppResInit.r_id_notification_download_tv_title,
				appWallDownloadInfo.app_Name);
		remoteViews.setTextViewText(
				AppResInit.r_id_notification_download_tv_progress, progress
						+ "%");
		remoteViews.setTextViewText(
				AppResInit.r_id_notification_download_tv_speed,
				ByteHelper.formatFileSize(appWallDownloadInfo.speed) + "/s");
		remoteViews.setProgressBar(
				AppResInit.r_id_notification_download_pb_progressbar, 100,
				progress, false);
		return remoteViews;

	}

}
