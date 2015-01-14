package com.g365.download;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import com.g365.utils.CustomPrint;
import com.g365.utils.NetInfoHelper;
import com.g365.utils.UrlHelper;

import android.content.Context;

/**
 * @����: DownloadThread
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-26 ����02:31:07
 * 
 * @����: ��<code>DownloadThread</code>���ļ������еĵ��������߳���</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class DownloadThread extends Thread {

	/** �ƶ���������ÿ�ζ�ȡ��С */
	private static final int READ_BYTE = 1024 * 4 * 4;// 16KBÿ������
	/** WIFIÿ�ζ�ȡ��С */
	private static final int WIFI_READ_BYTE = READ_BYTE * 30;// 480KBÿ������
	/** ��ʱʱ�� */
	private static final int OVERTIME = 30000;
	/** ��ʱ���Դ��� */
	private static final int OVERTIME_NUM = 3;

	private static final String ACCEPT = "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
			+ "application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument,"
			+ " application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, "
			+ "application/vnd.ms-powerpoint, application/msword, */*";
	private static final String USERAGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; "
			+ "Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30;"
			+ " .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)";

	/** �߳� ID */
	private int threadId = -1;
	/** ��ʼ����λ�� */
	private int startPos;
	/** ���ؿ��С */
	private int block;
	/** �����ش�С */
	private int downLen;
	/** �������� */
	private String fileUrl;
	/** �����ļ�·�� */
	private String filePath;
	/** ������ */
	private Context context;
	/** �ļ������� */
	private FileDownloader downloader;
	/** ���Ӵ��� */
	private int connectNum = 1;
	/** �����Ƿ���� */
	private boolean isFinished = false;
	/** �����Ƿ��ж� */
	private boolean isInterrupted = false;

	public DownloadThread(int threadId, int startPos, int block,
			FileDownloader downloader) {
		// TODO Auto-generated constructor stub

		this.threadId = threadId;
		this.startPos = startPos;
		this.block = block;
		this.downLen = startPos - block * threadId;
		this.fileUrl = downloader.getFileUrl();
		this.filePath = downloader.getFilePath();
		this.context = downloader.getContext();
		this.downloader = downloader;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		if (downLen < block) {// δ�������

			try {

				RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
				raf.seek(startPos);

				// connectNum <= OVERTIME_NUM &&
				while (!isFinished && !isInterrupted) {
					try {

						/* �����������;���ÿ�ζ�ȡ��С */
						int readByteSize = READ_BYTE;
						int netType = NetInfoHelper.getNetType(context);
						if (netType == NetInfoHelper.WIFI) {
							readByteSize = WIFI_READ_BYTE;
						}

						/* ������������ȷ��ʹ��Э�� */
						HttpURLConnection http = null;
						URL downUrl = new URL(fileUrl);
						if (netType == NetInfoHelper.CMWAP
								|| netType == NetInfoHelper.CT) {// �ƶ�wap�͵���
							java.net.Proxy p = new java.net.Proxy(
									java.net.Proxy.Type.HTTP,
									new InetSocketAddress(
											android.net.Proxy.getDefaultHost(),
											android.net.Proxy.getDefaultPort()));
							http = (HttpURLConnection) downUrl
									.openConnection(p);
						} else {
							http = (HttpURLConnection) downUrl.openConnection();
						}

						/* ���� HTTP�������� */
						http.setRequestMethod("GET");
						http.setRequestProperty("X-Online-Host",
								downUrl.getHost());
						http.setRequestProperty("Accept", ACCEPT);
						http.setRequestProperty("Accept-Language", "zh-CN");
						http.setRequestProperty("Referer", downUrl.toString());
						http.setRequestProperty("Charset", "UTF-8");
						http.setRequestProperty("Range", "bytes=" + startPos
								+ "-");
						http.setRequestProperty("User-Agent", USERAGENT);
						http.setRequestProperty("Connection", "Keep-Alive");
						http.setConnectTimeout(OVERTIME);// ���ó�ʱ
						http.setReadTimeout(OVERTIME);// ���ó�ʱ

						InputStream inputStream = http.getInputStream();
						int max = block > readByteSize ? readByteSize : block;
						byte[] buffer = new byte[max];

						int readNum = 0;
						while (downLen < block
								&& (readNum = inputStream.read(buffer, 0, max)) != -1
								&& !isInterrupted) {
							raf.write(buffer, 0, readNum);
							downLen += readNum;
							startPos += readNum;
							downloader.updateThreadData(threadId, startPos);
							downloader.appendFileSize(readNum);
							int spare = block - downLen;
							if (spare < max) {
								max = spare;
							}
						}
						raf.close();
						inputStream.close();

						if (!isInterrupted) {
							isFinished = true;
							interrupt();// һ���߳��Ѿ�����WaitSleepJoin
										// ״̬,���Interrupt()�Ͳ�������
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						connectNum++;
						if (connectNum >= OVERTIME_NUM) {
							isInterrupted = true;
							downloader.stopDownload();
						}
						CustomPrint.d(
								getClass(),
								"thread down error��fileName��-->"
										+ UrlHelper
												.getFileNameFromUrl(downloader
														.getFileUrl())
										+ " threadId:-->" + threadId
										+ " connectNum:-->" + connectNum);
					}
				}
			} catch (Exception e) {// �ļ���д����ֱ���ж�����
				// TODO Auto-generated catch block
				e.printStackTrace();
				downloader.stopDownload();
				isInterrupted = true;
				isFinished = false;
			}
		} else {// ���������
			isInterrupted = false;
			isFinished = true;
		}
	}

	/**
	 * @return �߳��Ƿ��������
	 */
	public boolean isFinished() {

		return isFinished;
	}

	public boolean isInterrupted() {

		return isInterrupted;
	}

	/**
	 * �����߳��ж�����
	 */
	public void setInterrupt() {

		isInterrupted = true;
	}

}
