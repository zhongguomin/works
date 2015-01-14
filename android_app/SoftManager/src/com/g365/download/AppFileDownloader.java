package com.g365.download;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.g365.database.SaveAppInfo;
import com.g365.download.interfaces.AppDownloadProgressListener;
import com.g365.download.interfaces.AppDownloadStateHandler;
import com.g365.entity.UpdataInfo;
import com.g365.softmanager.SoftAllUpdate;
import com.g365.softmanager.SoftUpdate;
import com.g365.utils.CustomPrint;
import com.g365.utils.FileHelper;
import com.g365.utils.UrlHelper;

/**
 * 
 * @author Administrator 是下载文件的多线程下载器
 */
public class AppFileDownloader {

	/** 下载状态：下载 */
	public static final int STATE_DOWNLOAD = 0;
	/** 下载状态：暂停 */
	public static final int STATE_PAUSE = 1;
	/** 下载状态：恢复 */
	public static final int STATE_RESUME = 2;
	/** 下载状态：完成 */
	public static final int STATE_FINISH = 3;

	/** 默认下载线程数 */
	private static final int DEFAULT_THREAD_NUM = 3;
	private static final String DEFAULT_FOLDER = "download/";
	/** 更新下载状态时间间隔 */
	private static final int DEFAULT_INTERVAL = 1000;

	/** 下载的线程 */
	private AppDownloadThread[] threads;
	/** 下载线程数 */
	private int threadNum;
	/** 线程下载信息 */
	private ArrayList<HashMap<Integer, Integer>> threadData;
	/** 每个线程下载多大 */
	private int blockSize;

	/** 一秒前下载大小 */
	private int preFileSize;
	/** 当前下载大小 */
	private int curFileSize;
	/** 文件大小 */
	private int fileSize;
	/** 下载速度 */
	private int speed;
	/** 文件保存文件夹 */
	private String fileDir;
	/** 文件名 */
	private String fileName;
	/** 文件保存路径 */
	private String filePath;
	private Context context;

	/** 下载是否中断 */
	private boolean isInterreupt = true;
	/** 文件下载链接 */
	private String fileUrl;

	/** 状态处理接口 */
	private AppDownloadStateHandler stateHandler;
	/** 下载进度接口 */
	private AppDownloadProgressListener progressListener;

	/**
	 * @param context
	 *            上下文
	 * @param fileUrl
	 *            文件下载链接
	 * @param stateHandler
	 *            下载状态处理接口
	 * @param progressListener
	 *            下载进度接口
	 */
	public AppFileDownloader(Context context, String fileUrl,
			AppDownloadStateHandler stateHandler,
			AppDownloadProgressListener progressListener) {

		this(context, fileUrl, DEFAULT_FOLDER, DEFAULT_THREAD_NUM,
				stateHandler, progressListener);
	}

	/**
	 * @param context
	 *            上下文
	 * @param fileUrl
	 *            文件下载链接
	 * @param downFolder
	 *            下载文件夹，如 "download/"，若要下载到 SD卡根目录则为""
	 * @param threadNum
	 *            下载线程数
	 * @param stateHandler
	 *            下载状态处理接口
	 * @param progressListener
	 *            下载进度接口
	 */
	public AppFileDownloader(Context context, String fileUrl,
			String downFolder, int threadNum,
			AppDownloadStateHandler stateHandler,
			AppDownloadProgressListener progressListener) {

		this.context = context;
		this.fileUrl = fileUrl;
		this.fileDir = FileHelper.SDCARD_PATH + downFolder;
		this.fileName = UrlHelper.getFileNameFromUrl(fileUrl);
		this.filePath = fileDir + fileName;
		if (threadNum <= 0) {
			threadNum = DEFAULT_THREAD_NUM;
		}
		this.threadNum = threadNum;
		this.stateHandler = stateHandler;
		this.progressListener = progressListener;
	}

	/**
	 * 获取文件的默认保存路径
	 * 
	 * @param fileUrl
	 *            文件链接
	 * @return
	 */
	public static String getDefaultPath(String fileUrl) {

		return FileHelper.SDCARD_PATH + DEFAULT_FOLDER+ UrlHelper.getFileNameFromUrl(fileUrl);
	}

	/**
	 * 初始化下载数据并下载
	 */
	public boolean startDownload() {

		if (isInterreupt) {// 下载未进行
			isInterreupt = false; // 下载是否中断
		} else {// 下载已进行
			return false;
		}

		File fileFolder = new File(fileDir);

		if (!fileFolder.exists()) {// 创建文件夹
			fileFolder.mkdirs();
		}
		File file = new File(filePath);
		if (!file.exists()) {// 文件不存在清除记录
			stateHandler.delFile(fileUrl);
			stateHandler.deleteThreadTask(fileUrl);
		}

		/** 获得文件大小 */
		if (stateHandler.isNewFile(fileUrl)) {
			fileSize = UrlHelper.getFileSizeFromUrl(fileUrl);
			if (fileSize == 0) {// 联网获取失败
				CustomPrint.d(getClass(), fileName + "：get fileSize error");
				stopDownload();
				return false;
			}
			stateHandler.addNewFile(fileUrl, fileSize, STATE_DOWNLOAD);
			CustomPrint.d(getClass(), fileName + "：add new file");
		} else {
			fileSize = stateHandler.getFileSize(fileUrl);
		}

		/** 初始化线程数据 */
		threads = new AppDownloadThread[threadNum];
		// 每个线程下载块的大小
		blockSize = fileSize / threadNum + (fileSize % threadNum == 0 ? 0 : 1);
		threadData = stateHandler.getThreadState(fileUrl);
		if (threadNum != threadData.size()) {
			stateHandler.deleteThreadTask(fileUrl);
			threadData.clear();

			for (int i = 0; i < threadNum; i++) {
				HashMap<Integer, Integer> tempMap = new HashMap<Integer, Integer>();
				tempMap.put(i, blockSize * i);
				threadData.add(tempMap);
				stateHandler.addNewThreadTask(fileUrl, i, tempMap);

			}
			CustomPrint.d(getClass(), fileName + "：add new threads task");
		}
		CustomPrint.d(getClass(), fileName + "：init download ok："
				+ "fileSize--->" + fileSize + " threadNum--->" + threadNum
				+ " blockSize--->" + blockSize);

		download();
		return true;

	}

	/**
	 * 开始下载数据
	 */
	private void download() {

		try {
			CustomPrint.d(getClass(), fileName + "：start download");
			stateHandler.updateFile(fileUrl, fileSize, STATE_PAUSE);

			curFileSize = 0;
			int finishNum = 0;// 成功下载线程数
			int threadBlockSize = 0; // 每个线程实际需要下载的大小
			int spareSize = 0; // 还需要下载的大小

			for (int i = 0; i < threadNum; i++) {

				if (!isInterreupt) {

					int downLen = threadData.get(i).get(i) - blockSize * i;
					curFileSize += downLen;
					spareSize = fileSize - blockSize * i;
					threadBlockSize = (spareSize < blockSize) ? spareSize
							: blockSize;

					if (downLen < threadBlockSize) {
						threads[i] = new AppDownloadThread(i, threadData.get(i)
								.get(i), blockSize, this);
						threads[i].setPriority(Thread.MAX_PRIORITY);
						threads[i].start();
					} else {// 不需要再启动线程
						finishNum++;
						threads[i] = null;
						CustomPrint.d(getClass(), fileName + "：thread[" + i
								+ "] is finished");
					}
				}
			}

			if (finishNum < threadNum) {
				/** 下载是否完成 */
				boolean isFinished = false;

				while (!isFinished && !isInterreupt) {
					preFileSize = curFileSize;
					Thread.sleep(DEFAULT_INTERVAL);
					speed = curFileSize - preFileSize;
					isFinished = true;

					for (int i = 0; i < threadNum; i++) {
						// 还有线程未完成任务则继续
						if (threads[i] != null && !threads[i].isFinished()) {
							isFinished = false;
							break;
						}
					}
					for (int i = 0; i < threadNum; i++) {
						// 有线程被中断则中断
						if (threads[i] != null && threads[i].isInterrupted()) {
							isInterreupt = true;
							break;
						}
					}
					if (progressListener != null) {
						// 下载异常中断
						if (curFileSize < fileSize && isFinished) {
							progressListener.onDownload(curFileSize, fileSize,
									spareSize, true);
						} else {
							if (curFileSize >= fileSize && isFinished) {
								stateHandler.updateFile(fileUrl, fileSize,
										STATE_FINISH);
								CustomPrint.d(getClass(), fileName
										+ "：download finish");
								progressListener.onDownload(curFileSize,
										fileSize, speed, false);
								isInterreupt = true;// 下载完成
								Log.v("jiao333", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
								if(SoftUpdate.updateState == SoftUpdate.SOFT_UPDATE_STOP){
									SaveAppInfo saveAppInfo = new SaveAppInfo(context);
									if(SoftAllUpdate.currentTaskNum <= saveAppInfo.getScrollData().size()){
										UpdataInfo updataInfo = saveAppInfo.getScrollData().get(SoftAllUpdate.currentTaskNum);
										UpdateAllDownloader.getInstance(context, updataInfo).startDownload();
										SoftAllUpdate.currentTaskNum++;
									}
								}
							} else {
								progressListener.onDownload(curFileSize,
										fileSize, speed, isInterreupt);
								CustomPrint.d(getClass(), fileName
										+ "：download process：" + curFileSize
										+ "/" + fileSize);
							}
						}
					}
				}
			} else {// 下载完成
				if (progressListener != null) {
					progressListener.onDownload(curFileSize, fileSize, speed,
							false);
				}
				stateHandler.updateFile(fileUrl, fileSize, STATE_FINISH);
				CustomPrint.d(getClass(), fileName + "：download has finished");
				isInterreupt = true;// 下载完成
				Log.v("jiao333", "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
			}
		} catch (Exception e) {
			stopDownload();
			e.printStackTrace();
		}
	}

	public synchronized void stopDownload() {
		isInterreupt = true;
		if (threads != null) {
			for (int i = 0; i < threadNum; i++) {
				if (threads[i] != null) {
					threads[i].setInterrupt();
				}
			}
		}

		if (progressListener != null) {
			progressListener.onDownload(curFileSize, fileSize, speed,
					isInterreupt);
		}
		stateHandler.updateFile(fileUrl, fileSize, STATE_RESUME);
		CustomPrint.d(getClass(), fileName + "：stop download");
	}

	/**
	 * 判断是否正在下载
	 * 
	 * @return
	 */
	public boolean isDownloading() {
		return !isInterreupt;

	}

	/**
	 * @return 获取文件下载链接
	 */
	public String getFileUrl() {
		return this.fileUrl;
	}

	/**
	 * @return 获取文件保存路径
	 */
	public String getFilePath() {
		return this.filePath;
	}

	/**
	 * @return 获取上下文
	 */
	public Context getContext() {
		return this.context;
	}

	/**
	 * 添加已下载文件大小
	 * 
	 * @param size
	 *            大小增量
	 */
	protected synchronized void appendFileSize(int size) {
		curFileSize += size;
	}

	/**
	 * 更新线程下载信息
	 * 
	 * @param threadId
	 *            线程编号
	 * @param downPos
	 *            下载位置
	 */
	protected synchronized void updateThreadData(int threadId, int downPos) {
		threadData.get(threadId).put(threadId, downPos);
		stateHandler.updateThreadTask(fileUrl, threadId,
				threadData.get(threadId));
	}
}
