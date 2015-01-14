package com.g365.download;

import java.util.HashMap;
import java.util.Map;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.g365.download.interfaces.AppDownloadProgressListener;
import com.g365.entity.AppResInit;
import com.g365.entity.ImageInfo;
import com.g365.utils.ByteHelper;
import com.g365.utils.CustomNotification;

/**
 * 
 * @author nova @ 滑动图片下载类
 * @2012年12月26日17:01:18
 * 
 */
public class ImageDownload {

	public static Map<String, ImageInfo> downMap = new HashMap<String, ImageInfo>();
	public static int id = 1;

	public static void downLoad(final Context context, final ImageInfo imageInfo) {

		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();

				int notificationId = id;
				id++;
				final CustomNotification notification = new CustomNotification(
						context, notificationId, R.drawable.stat_sys_download,
						imageInfo.apps_Name, 0, new Intent());

				AppFileDownloader appFileDownloader = new AppFileDownloader(
						context, imageInfo.apps_downloads,
						new AppDefaultStateHandler(context),
						new AppDownloadProgressListener() {

							public void onDownload(int downSize, int fileSize,
									int speed, boolean interrupt) {

								if (downSize >= fileSize) {
									notification.cancel();
									downMap.remove(imageInfo.apps_downloads);
								} else {

									// notification.update(imageInfo.apps_Name,
									// downSize * 100 / fileSize + "%" + " "
									// + ByteHelper.formatFileSize(speed),
									// 0);
									int progress = (fileSize == 0) ? 0
											: downSize * 100 / fileSize;
									RemoteViews remoteViews = new RemoteViews(
											context.getPackageName(),
											AppResInit.r_layout_common_notification_download);
									remoteViews
											.setTextViewText(
													AppResInit.r_id_notification_download_tv_title,
													imageInfo.apps_Name);
									remoteViews
											.setTextViewText(
													AppResInit.r_id_notification_download_tv_progress,
													progress + "%");
									remoteViews
											.setTextViewText(
													AppResInit.r_id_notification_download_tv_speed,
													ByteHelper
															.formatFileSize(speed)
															+ "/s");
									remoteViews
											.setProgressBar(
													AppResInit.r_id_notification_download_pb_progressbar,
													100, progress, false);
									notification
											.updateRemoteViews(
													remoteViews,
													CustomNotification.MODE_SHOW_ONGOING);

								}
							}
						});
				appFileDownloader.startDownload();
			}
		}.start();

		downMap.put(imageInfo.apps_downloads, imageInfo);
	}

//	public static RemoteViews getDownloadRemoteViews(Context context,
//			WDI adWallDownloadInfo) {
//
//		int progress = (adWallDownloadInfo.fileSize == 0) ? 0
//				: adWallDownloadInfo.curFileSize * 100
//						/ adWallDownloadInfo.fileSize;
//
//		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
//				AppResInit.r_layout_common_notification_download);
//		remoteViews.setTextViewText(
//				AppResInit.r_id_notification_download_tv_title,
//				adWallDownloadInfo.name);
//		remoteViews.setTextViewText(
//				AppResInit.r_id_notification_download_tv_progress, progress
//						+ "%");
//		remoteViews.setTextViewText(
//				AppResInit.r_id_notification_download_tv_speed,
//				ByteHelper.formatFileSize(adWallDownloadInfo.speed) + "/s");
//		remoteViews.setProgressBar(
//				AppResInit.r_id_notification_download_pb_progressbar, 100,
//				progress, false);
//
//		return remoteViews;
//	}
}
