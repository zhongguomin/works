package com.g365.download;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.g365.download.interfaces.DownloadProgressListener;
import com.g365.utils.CustomPrint;
import com.g365.utils.FileHelper;
import com.g365.utils.UrlHelper;
import com.lllfy.newad.core.util.HashFile;

import comns.file.download.interfaces.DownloadStateHandler;

/**
 * @����: FileDownloader
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-26 ����02:31:45
 * 
 * @����: ��<code>FileDownloader</code> �������ļ��Ķ��߳�������</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class FileDownloader {

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
	private DownloadThread[] threads;
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
	/** �ļ��� md5ֵ */
	private String fileMd5;
	private Context context;

	/** �����Ƿ��ж� */
	private boolean isInterreupt = true;
	/** �ļ��������� */
	private String fileUrl;
	/** ״̬����ӿ� */
	private DownloadStateHandler stateHandler;
	/** ���ؽ��Ƚӿ� */
	private DownloadProgressListener progressListener;

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
	public FileDownloader(Context context, String fileUrl,
			DownloadStateHandler stateHandler,
			DownloadProgressListener progressListener) {

		this(context, fileUrl, null, stateHandler, progressListener);
	}

	/**
	 * @param context
	 *            ������
	 * @param fileUrl
	 *            �ļ���������
	 * @param fileMd5
	 *            �ļ��� md5ֵ����Ϊnull
	 * @param stateHandler
	 *            ����״̬����ӿ�
	 * @param progressListener
	 *            ���ؽ��Ƚӿ�
	 */
	public FileDownloader(Context context, String fileUrl, String fileMd5,
			DownloadStateHandler stateHandler,
			DownloadProgressListener progressListener) {

		this(context, fileUrl, fileMd5, DEFAULT_FOLDER, DEFAULT_THREAD_NUM,
				stateHandler, progressListener);
	}

	/**
	 * @param context
	 *            ������
	 * @param fileUrl
	 *            �ļ���������
	 * @param fileMd5
	 *            �ļ��� md5ֵ����Ϊnull
	 * @param downFolder
	 *            �����ļ��У��� "download/"����Ҫ���ص� SD����Ŀ¼��Ϊ""
	 * @param threadNum
	 *            �����߳���
	 * @param stateHandler
	 *            ����״̬����ӿ�
	 * @param progressListener
	 *            ���ؽ��Ƚӿ�
	 */
	public FileDownloader(Context context, String fileUrl, String fileMd5,
			String downFolder, int threadNum,
			DownloadStateHandler stateHandler,
			DownloadProgressListener progressListener) {

		this.context = context;
		this.fileUrl = fileUrl;
		if (fileMd5 == null) {
			this.fileMd5 = "";
		} else {
			this.fileMd5 = fileMd5;
		}
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

		return FileHelper.SDCARD_PATH + DEFAULT_FOLDER
				+ UrlHelper.getFileNameFromUrl(fileUrl);
	}

	/**
	 * ��ʼ���������ݲ�����
	 */
	public void startDownload() {

		new Thread() {
			public void run() {

				if (isInterreupt) {// ����δ����
					isInterreupt = false;// �����Ƿ��ж�
				} else {// �����ѽ���
					return;
				}
				File fileFolder = new File(fileDir);
				if (!fileFolder.exists()) {// �����ļ���
					fileFolder.mkdirs();
					CustomPrint.d(getClass(), fileName + "��make dirs");
				}
				File file = new File(filePath);
				if (!file.exists()) {// �ļ������������¼
					stateHandler.delFile(fileUrl);
					stateHandler.deleteThreadTask(fileUrl);
				}

				/* ����ļ���С */
				if (stateHandler.isNewFile(fileUrl)) {
					CustomPrint.d(getClass(), fileName
							+ "��start get fileSize from url");
					fileSize = UrlHelper.getFileSizeFromUrl(fileUrl);
					if (fileSize == 0) {// ������ȡʧ��
						CustomPrint.d(getClass(), fileName
								+ "��get fileSize error");
						stopDownload();
						return;
					}
					stateHandler.addNewFile(fileUrl, fileMd5, fileSize,
							STATE_DOWNLOAD);
					CustomPrint.d(getClass(), fileName + "��add new file");
				} else {
					fileSize = stateHandler.getFileSize(fileUrl);
					CustomPrint.d(getClass(), fileName
							+ "��get fileSize from database");
				}
				/* ��ʼ���߳����� */
				threads = new DownloadThread[threadNum];
				blockSize = fileSize / threadNum
						+ (fileSize % threadNum == 0 ? 0 : 1);// ÿ���߳����ؿ�Ĵ�С
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
					CustomPrint.d(getClass(), fileName
							+ "��add new threads task");
				}

				CustomPrint.d(getClass(), fileName + "��init download ok��"
						+ "fileSize--->" + fileSize + " threadNum--->"
						+ threadNum + " blockSize--->" + blockSize);

				download();
			};
		}.start();
	}

	/**
	 * ��ʼ��������
	 */
	private void download() {

		try {

			CustomPrint.d(getClass(), fileName + "��start download");
			stateHandler.updateFile(fileUrl, fileMd5, fileSize, STATE_PAUSE);

			curFileSize = 0;
			int finishNum = 0;// �ɹ������߳���
			int threadBlockSize = 0;// ÿ���߳�ʵ����Ҫ���صĴ�С
			int spareSize = 0;// ����Ҫ���صĴ�С
			for (int i = 0; i < threadNum; i++) {

				if (!isInterreupt) {

					int downLen = threadData.get(i).get(i) - blockSize * i;
					curFileSize += downLen;
					spareSize = fileSize - blockSize * i;
					threadBlockSize = (spareSize < blockSize) ? spareSize
							: blockSize;

					if (downLen < threadBlockSize) {
						threads[i] = new DownloadThread(i, threadData.get(i)
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

				boolean isFinished = false;// �����Ƿ����
				while (!isFinished && !isInterreupt) {

					preFileSize = curFileSize;
					Thread.sleep(DEFAULT_INTERVAL);
					speed = curFileSize - preFileSize;
					isFinished = true;
					for (int i = 0; i < threadNum; i++) {
						if (threads[i] != null && !threads[i].isFinished()) {// �����߳�δ������������
							isFinished = false;
							break;
						}
					}
					for (int i = 0; i < threadNum; i++) {
						if (threads[i] != null && threads[i].isInterrupted()) {// ���̱߳��ж����ж�
							isInterreupt = true;
							break;
						}
					}
					if (progressListener != null) {
						if (curFileSize < fileSize && isFinished) {// �����쳣�ж�
							progressListener.onDownload(curFileSize, fileSize,
									speed, true);
						} else {
							if (curFileSize >= fileSize && isFinished) {// �������
								if (fileMd5.equals("")) {// ��ȡ�ļ� md5
									fileMd5 = HashFile.getHash(filePath,
											HashFile.HASH_TYPE_MD5);
									if (fileMd5 == null) {// ��ȡʧ��
										fileMd5 = "";
									}
								}
								stateHandler.updateFile(fileUrl, fileMd5,
										fileSize, STATE_FINISH);
								CustomPrint.d(getClass(), fileName
										+ "��download finish");
								progressListener.onDownload(curFileSize,
										fileSize, speed, false);
								isInterreupt = true;// �������
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
				if (fileMd5.equals("")) {// ��ȡ�ļ� md5
					fileMd5 = HashFile
							.getHash(filePath, HashFile.HASH_TYPE_MD5);
					if (fileMd5 == null) {// ��ȡʧ��
						fileMd5 = "";
					}
				}
				if (progressListener != null) {
					progressListener.onDownload(curFileSize, fileSize, speed,
							false);
				}
				stateHandler.updateFile(fileUrl, fileMd5, fileSize,
						STATE_FINISH);
				CustomPrint.d(getClass(), fileName + "��download has finished");
				isInterreupt = true;// �������
				
			//	context.sendBroadcast(new Intent("broadCast"));
				
			}

		} catch (Exception e) {
			// TODO: handle exception
			stopDownload();
			e.printStackTrace();
		}
	}

	/**
	 * �ж�����
	 */
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
		stateHandler.updateFile(fileUrl, fileMd5, fileSize, STATE_RESUME);
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
