package com.g365.download;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.g365.download.interfaces.AppDownloadProgressListener;
import com.g365.download.interfaces.AppOnStateChangeRepainter;
import com.g365.entity.UpdataInfo;
import com.g365.utils.AppInfoHelper;
import com.g365.utils.CustomPrint;
import com.g365.utils.FileHelper;
import com.g365.utils.UpdateAllStateChangeReceiver;
import com.g365.utils.UrlHelper;
import comns.system.SystemIntent;

/**
 * 
 * @author nova ȫ������ ���� 2013��1��21��14:02:46
 * 
 */
public class UpdateAllDownloader {

	/** ���ؽ��ȸı�㲥�� Action */
	public static final String ACTION_UPDATEALL_DOWNLOAD_CHANCE = "action_updateall_download_change";

	/** ���ؽ��ȵ� UpdataInfo�ֶ� */
	public static final String DATA_KEY_ONEADWALL_DOWNLOAD_INFO = "updateInfos";

	private Context context;

	private UpdataInfo updateInfos;

	private AppFileDownloader downloader;

	/** �ļ������ļ��� */
	private String fileDir;
	/** �ļ��� */
	private String fileName;
	/** �ļ�����·�� */
	private String filePath;

	/** �ļ��������� */
	private String fileUrl;
	private AppOnStateChangeRepainter repainter;

	public static synchronized UpdateAllDownloader getInstance(Context context,
			UpdataInfo updateInfos) {

		UpdateAllDownloader updateAllDownloader = UpdateAllDownloadManager
				.getUpdateAllDownloader(updateInfos.url);
		if (updateAllDownloader == null) {
			updateAllDownloader = new UpdateAllDownloader(context, updateInfos);
			UpdateAllDownloadManager.addUpdateAllDownloader(updateInfos.url,
					updateAllDownloader);

		}
		return updateAllDownloader;
	}

	private UpdateAllDownloader(Context context, UpdataInfo updateInfos) {
		this.context = context;
		this.updateInfos = updateInfos;
		this.fileDir = FileHelper.SDCARD_PATH;
		this.fileName = UrlHelper.getFileNameFromUrl(fileUrl);
		this.filePath = fileDir + fileName;
	}

	/**
	 * ���ж��ļ��Ƿ�������������������Լ��ģ� ��������ʾ��װ������������������
	 */
	public void startDownload() {
		// ������߳�
		new Thread() {
			public void run() {
				final String filePath = AppFileDownloader
						.getDefaultPath(updateInfos.url);
				// �ļ�����
				if (AppInfoHelper.isApKFileOk(context, filePath)) {
					File file = new File(filePath);
					AppDefaultStateHandler stateHandler = new AppDefaultStateHandler(
							context);
					if (stateHandler.isNewFile(updateInfos.url)) {
						stateHandler.addNewFile(updateInfos.url,
								(int) file.length(),
								AppFileDownloader.STATE_FINISH);
					} else {
						stateHandler.updateFileState(updateInfos.url,
								AppFileDownloader.STATE_FINISH);
					}

					AppAdDefaultStateHandler aStateHandler = new AppAdDefaultStateHandler(
							context);

					if (aStateHandler.isNewFile(updateInfos.packagename,
							updateInfos.versioncode)) {

						aStateHandler.addNewFile(updateInfos.url,
								updateInfos.packagename,
								updateInfos.versioncode, updateInfos.app_id,
								AppAdDefaultStateHandler.STATE_FINISH);
					} else {
						aStateHandler.updateFile(updateInfos.url,
								updateInfos.packagename,
								updateInfos.versioncode, updateInfos.app_id,
								AppAdDefaultStateHandler.STATE_FINISH);
					}
					UpdateAllStateChangeReceiver.sendStateChange(context,
							UpdateAllStateChangeReceiver
									.getUpdataInfo(updateInfos,
											AppFileDownloader.STATE_FINISH));
					UpdateAllDownloadManager
							.delUpdateAllDownloader(updateInfos.url);

					// SystemIntent.installApk(context, filePath);

					Log.v("jiao333", "cccccccccccccccccccccccccccccccccccccc");

					// SaveAppInfo saveAppInfo = new SaveAppInfo(context);
					// if(SoftAllUpdate.currentTaskNum <
					// saveAppInfo.getScrollData().size()){
					// UpdataInfo updataInfo =
					// saveAppInfo.getScrollData().get(SoftAllUpdate.currentTaskNum);
					// UpdateAllDownloader.getInstance(context,
					// updataInfo).startDownload();
					// SoftAllUpdate.currentTaskNum++;
					//
					// if(SoftAllUpdate.currentTaskNum ==
					// saveAppInfo.getScrollData().size()){
					// SoftAllUpdate.currentTaskNum = 0;
					// }
					// }

				} else {
					prepareDownload();
				}
			};
		}.start();

	}

	/**
	 * ��ʼ��������Դ��ʼ����
	 */

	private void prepareDownload() {
		if (downloader == null) {
			UpdateAllStateChangeReceiver.sendStateChange(context,
					UpdateAllStateChangeReceiver.getUpdataInfo(updateInfos,
							AppFileDownloader.STATE_PAUSE));
			AppDefaultStateHandler stateHandler = new AppDefaultStateHandler(
					context);
			if (stateHandler.isNewFile(updateInfos.url)) {
				stateHandler.addNewFile(updateInfos.url,
						UrlHelper.getFileSizeFromUrl(updateInfos.url),
						AppFileDownloader.STATE_PAUSE);
			} else {
				stateHandler.updateFileState(updateInfos.url,
						AppFileDownloader.STATE_PAUSE);
			}

			downloader = new AppFileDownloader(context, updateInfos.url,
					new AppDefaultStateHandler(context),
					new AppDownloadProgressListener() {
						public void onDownload(int downSize, int fileSize,
								int speed, boolean interrupt) {

							UpdataInfo sendInfo = getUpdataInfo(updateInfos,
									downSize, fileSize, speed);
							if (!interrupt) {
								if (downSize >= fileSize) {
									UpdateAllStateChangeReceiver
											.sendStateChange(
													context,
													UpdateAllStateChangeReceiver
															.getUpdataInfo(
																	updateInfos,
																	AppFileDownloader.STATE_FINISH));
									UpdateAllDownloadManager
											.delUpdateAllDownloader(updateInfos.url);
									AppAdDefaultStateHandler stateHandler = new AppAdDefaultStateHandler(
											context);
									if (stateHandler.isNewFile(
											updateInfos.packagename,
											updateInfos.versioncode)) {
										stateHandler
												.addNewFile(
														updateInfos.url,
														updateInfos.packagename,
														updateInfos.versioncode,
														updateInfos.app_id,
														AppAdDefaultStateHandler.STATE_FINISH);
									} else {
										stateHandler
												.updateFile(
														updateInfos.url,
														updateInfos.packagename,
														updateInfos.versioncode,
														updateInfos.app_id,
														AppAdDefaultStateHandler.STATE_FINISH);
									}

									SystemIntent
											.installApk(
													context,
													AppFileDownloader
															.getDefaultPath(updateInfos.url));
									/* ��ӿ��ύ���� */
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
														"adwall download post��"
																+ (result == 1 ? "success"
																		: "fail"));
									} catch (Exception e) {
										e.printStackTrace();
									}

								}
								sendUpdataInfo(context, sendInfo);
							} else {
								UpdateAllStateChangeReceiver.sendStateChange(
										context,
										UpdateAllStateChangeReceiver
												.getUpdataInfo(
														updateInfos,
														AppFileDownloader.STATE_RESUME));
							}
						}
					});
		}
		download();
	}

	/**
	 * ��ʼ����
	 */
	private void download() {
		new Thread() {

			public void run() {
				UpdateAllStateChangeReceiver.sendStateChange(context,
						UpdateAllStateChangeReceiver.getUpdataInfo(updateInfos,
								AppFileDownloader.STATE_PAUSE));
				downloader.startDownload();
			}
		}.start();

	}

	/**
	 * ֹͣ����
	 */
	public void stopDownload() {

		if (downloader != null) {
			downloader.stopDownload();
		}

		UpdateAllStateChangeReceiver.sendStateChange(context,
				UpdateAllStateChangeReceiver.getUpdataInfo(updateInfos,
						AppFileDownloader.STATE_RESUME));
	}

	/**
	 * 
	 * @return �Ƿ���������
	 */
	public boolean isDownloading() {
		if (downloader != null) {
			return downloader.isDownloading();
		}
		return false;

	}

	/**
	 * ��ȡ���������Ϣ��
	 * 
	 * @param updataInfos
	 * @param curFileSize
	 *            �����ش�С
	 * @param fileSize
	 *            �ļ��ܴ�С
	 * @param speed
	 *            �����ٶ�
	 * @return
	 */
	public static UpdataInfo getUpdataInfo(UpdataInfo updataInfos,
			int curFileSize, int fileSize, int speed) {
		updataInfos.curFileSize = curFileSize;
		updataInfos.fileSize = fileSize;
		updataInfos.speed = speed;
		return updataInfos;

	}

	/**
	 * ���ؽ��ȹ㲥
	 * 
	 * @param context
	 * @param updataInfos
	 */
	public static void sendUpdataInfo(Context context, UpdataInfo updataInfos) {
		Intent intent = new Intent(ACTION_UPDATEALL_DOWNLOAD_CHANCE);
		intent.putExtra(DATA_KEY_ONEADWALL_DOWNLOAD_INFO, updataInfos);
		context.sendBroadcast(intent);
	}

	/**
	 * @return ��ȡ�ļ�����·��
	 */
	public String getFilePath() {
		return this.filePath;
	}

}
