package comns.file.download;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import android.content.Context;

import comns.net.NetInfoHelper;
import comns.net.UrlHelper;
import comns.system.CustomPrint;

/**
 * @类名: DownloadThread
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-26 下午02:31:07
 * 
 * @描述: 类<code>DownloadThread</code>是文件下载中的单个下载线程类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class DownloadThread extends Thread {

	/** 移动数据流量每次读取大小 */
	private static final int READ_BYTE = 1024 * 4 * 4;// 16KB每次下载
	/** WIFI每次读取大小 */
	private static final int WIFI_READ_BYTE = READ_BYTE * 30;// 480KB每次下载
	/** 超时时间 */
	private static final int OVERTIME = 30000;
	/** 超时重试次数 */
	private static final int OVERTIME_NUM = 3;

	private static final String ACCEPT = "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
			+ "application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument,"
			+ " application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, "
			+ "application/vnd.ms-powerpoint, application/msword, */*";
	private static final String USERAGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; "
			+ "Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30;"
			+ " .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)";

	/** 线程 ID */
	private int threadId = -1;
	/** 起始下载位置 */
	private int startPos;
	/** 下载块大小 */
	private int block;
	/** 已下载大小 */
	private int downLen;
	/** 下载链接 */
	private String fileUrl;
	/** 保存文件路径 */
	private String filePath;
	/** 上下文 */
	private Context context;
	/** 文件下载器 */
	private FileDownloader downloader;
	/** 连接次数 */
	private int connectNum = 1;
	/** 下载是否完成 */
	private boolean isFinished = false;
	/** 下载是否中断 */
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

		if (downLen < block) {// 未下载完成

			try {

				RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
				raf.seek(startPos);

				// connectNum <= OVERTIME_NUM &&
				while (!isFinished && !isInterrupted) {
					try {

						/* 根据网络类型决定每次读取大小 */
						int readByteSize = READ_BYTE;
						int netType = NetInfoHelper.getNetType(context);
						if (netType == NetInfoHelper.WIFI) {
							readByteSize = WIFI_READ_BYTE;
						}

						/* 根据网络类型确定使用协议 */
						HttpURLConnection http = null;
						URL downUrl = new URL(fileUrl);
						if (netType == NetInfoHelper.CMWAP
								|| netType == NetInfoHelper.CT) {// 移动wap和电信
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

						/* 设置 HTTP请求数据 */
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
						http.setConnectTimeout(OVERTIME);// 设置超时
						http.setReadTimeout(OVERTIME);// 设置超时

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
							interrupt();// 一旦线程已经进入WaitSleepJoin
										// 状态,这个Interrupt()就不管用了
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
								"thread down error：fileName：-->"
										+ UrlHelper
												.getFileNameFromUrl(downloader
														.getFileUrl())
										+ " threadId:-->" + threadId
										+ " connectNum:-->" + connectNum);
					}
				}
			} catch (Exception e) {// 文件读写错误，直接中断下载
				// TODO Auto-generated catch block
				e.printStackTrace();
				downloader.stopDownload();
				isInterrupted = true;
				isFinished = false;
			}
		} else {// 已下载完成
			isInterrupted = false;
			isFinished = true;
		}
	}

	/**
	 * @return 线程是否下载完成
	 */
	public boolean isFinished() {

		return isFinished;
	}

	public boolean isInterrupted() {

		return isInterrupted;
	}

	/**
	 * 设置线程中断下载
	 */
	public void setInterrupt() {

		isInterrupted = true;
	}

}
