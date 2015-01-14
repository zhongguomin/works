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
 * @author Administrator �������ļ��Ķ��߳�������
 */
public class AppFileDownloader {

	/** ����״̬������ */
	public static final int STATE_DOWNLOAD = 0;
	/** ����״̬����ͣ */
	public static final int STATE_PAUSE = 1;
	/** ����״̬���ָ� */
	public static final int STATE_RESUME = 2;
	/** ����״̬����� */
	public static final int STATE_FINISH = 3;

	/** Ĭ�������߳��� */
	private static final int DEFAULT_THREAD_NUM = 3;
	private static final String DEFAULT_FOLDER = "download/";
	/** ��������״̬ʱ���� */
	private static final int DEFAULT_INTERVAL = 1000;

	/** ���ص��߳� */
	private AppDownloadThread[] threads;
	/** �����߳��� */
	private int threadNum;
	/** �߳�������Ϣ */
	private ArrayList<HashMap<Integer, Integer>> threadData;
	/** ÿ���߳����ض�� */
	private int blockSize;

	/** һ��ǰ���ش�С */
	private int preFileSize;
	/** ��ǰ���ش�С */
	private int curFileSize;
	/** �ļ���С */
	private int fileSize;
	/** �����ٶ� */
	private int speed;
	/** �ļ������ļ��� */
	private String fileDir;
	/** �ļ��� */
	private String fileName;
	/** �ļ�����·�� */
	private String filePath;
	private Context context;

	/** �����Ƿ��ж� */
	private boolean isInterreupt = true;
	/** �ļ��������� */
	private String fileUrl;

	/** ״̬����ӿ� */
	private AppDownloadStateHandler stateHandler;
	/** ���ؽ��Ƚӿ� */
	private AppDownloadProgressListener progressListener;

	/**
	 * @param context
	 *            ������
	 * @param fileUrl
	 *            �ļ���������
	 * @param stateHandler
	 *            ����״̬����ӿ�
	 * @param progressListener
	 *            ���ؽ��Ƚӿ�
	 */
	public AppFileDownloader(Context context, String fileUrl,
			AppDownloadStateHandler stateHandler,
			AppDownloadProgressListener progressListener) {

		this(context, fileUrl, DEFAULT_FOLDER, DEFAULT_THREAD_NUM,
				stateHandler, progressListener);
	}

	/**
	 * @param context
	 *            ������
	 * @param fileUrl
	 *            �ļ���������
	 * @param downFolder
	 *            �����ļ��У��� "download/"����Ҫ���ص� SD����Ŀ¼��Ϊ""
	 * @param threadNum
	 *            �����߳���
	 * @param stateHandler
	 *            ����״̬����ӿ�
	 * @param progressListener
	 *            ���ؽ��Ƚӿ�
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
	 * ��ȡ�ļ���Ĭ�ϱ���·��
	 * 
	 * @param fileUrl
	 *            �ļ�����
	 * @return
	 */
	public static String getDefaultPath(String fileUrl) {

		return FileHelper.SDCARD_PATH + DEFAULT_FOLDER+ UrlHelper.getFileNameFromUrl(fileUrl);
	}

	/**
	 * ��ʼ���������ݲ�����
	 */
	public boolean startDownload() {

		if (isInterreupt) {// ����δ����
			isInterreupt = false; // �����Ƿ��ж�
		} else {// �����ѽ���
			return false;
		}

		File fileFolder = new File(fileDir);

		if (!fileFolder.exists()) {// �����ļ���
			fileFolder.mkdirs();
		}
		File file = new File(filePath);
		if (!file.exists()) {// �ļ������������¼
			stateHandler.delFile(fileUrl);
			stateHandler.deleteThreadTask(fileUrl);
		}

		/** ����ļ���С */
		if (stateHandler.isNewFile(fileUrl)) {
			fileSize = UrlHelper.getFileSizeFromUrl(fileUrl);
			if (fileSize == 0) {// ������ȡʧ��
				CustomPrint.d(getClass(), fileName + "��get fileSize error");
				stopDownload();
				return false;
			}
			stateHandler.addNewFile(fileUrl, fileSize, STATE_DOWNLOAD);
			CustomPrint.d(getClass(), fileName + "��add new file");
		} else {
			fileSize = stateHandler.getFileSize(fileUrl);
		}

		/** ��ʼ���߳����� */
		threads = new AppDownloadThread[threadNum];
		// ÿ���߳����ؿ�Ĵ�С
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
			CustomPrint.d(getClass(), fileName + "��add new threads task");
		}
		CustomPrint.d(getClass(), fileName + "��init download ok��"
				+ "fileSize--->" + fileSize + " threadNum--->" + threadNum
				+ " blockSize--->" + blockSize);

		download();
		return true;

	}

	/**
	 * ��ʼ��������
	 */
	private void download() {

		try {
			CustomPrint.d(getClass(), fileName + "��start download");
			stateHandler.updateFile(fileUrl, fileSize, STATE_PAUSE);

			curFileSize = 0;
			int finishNum = 0;// �ɹ������߳���
			int threadBlockSize = 0; // ÿ���߳�ʵ����Ҫ���صĴ�С
			int spareSize = 0; // ����Ҫ���صĴ�С

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
					} else {// ����Ҫ�������߳�
						finishNum++;
						threads[i] = null;
						CustomPrint.d(getClass(), fileName + "��thread[" + i
								+ "] is finished");
					}
				}
			}

			if (finishNum < threadNum) {
				/** �����Ƿ���� */
				boolean isFinished = false;

				while (!isFinished && !isInterreupt) {
					preFileSize = curFileSize;
					Thread.sleep(DEFAULT_INTERVAL);
					speed = curFileSize - preFileSize;
					isFinished = true;

					for (int i = 0; i < threadNum; i++) {
						// �����߳�δ������������
						if (threads[i] != null && !threads[i].isFinished()) {
							isFinished = false;
							break;
						}
					}
					for (int i = 0; i < threadNum; i++) {
						// ���̱߳��ж����ж�
						if (threads[i] != null && threads[i].isInterrupted()) {
							isInterreupt = true;
							break;
						}
					}
					if (progressListener != null) {
						// �����쳣�ж�
						if (curFileSize < fileSize && isFinished) {
							progressListener.onDownload(curFileSize, fileSize,
									spareSize, true);
						} else {
							if (curFileSize >= fileSize && isFinished) {
								stateHandler.updateFile(fileUrl, fileSize,
										STATE_FINISH);
								CustomPrint.d(getClass(), fileName
										+ "��download finish");
								progressListener.onDownload(curFileSize,
										fileSize, speed, false);
								isInterreupt = true;// �������
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
										+ "��download process��" + curFileSize
										+ "/" + fileSize);
							}
						}
					}
				}
			} else {// �������
				if (progressListener != null) {
					progressListener.onDownload(curFileSize, fileSize, speed,
							false);
				}
				stateHandler.updateFile(fileUrl, fileSize, STATE_FINISH);
				CustomPrint.d(getClass(), fileName + "��download has finished");
				isInterreupt = true;// �������
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
		CustomPrint.d(getClass(), fileName + "��stop download");
	}

	/**
	 * �ж��Ƿ���������
	 * 
	 * @return
	 */
	public boolean isDownloading() {
		return !isInterreupt;

	}

	/**
	 * @return ��ȡ�ļ���������
	 */
	public String getFileUrl() {
		return this.fileUrl;
	}

	/**
	 * @return ��ȡ�ļ�����·��
	 */
	public String getFilePath() {
		return this.filePath;
	}

	/**
	 * @return ��ȡ������
	 */
	public Context getContext() {
		return this.context;
	}

	/**
	 * ����������ļ���С
	 * 
	 * @param size
	 *            ��С����
	 */
	protected synchronized void appendFileSize(int size) {
		curFileSize += size;
	}

	/**
	 * �����߳�������Ϣ
	 * 
	 * @param threadId
	 *            �̱߳��
	 * @param downPos
	 *            ����λ��
	 */
	protected synchronized void updateThreadData(int threadId, int downPos) {
		threadData.get(threadId).put(threadId, downPos);
		stateHandler.updateThreadTask(fileUrl, threadId,
				threadData.get(threadId));
	}
}
