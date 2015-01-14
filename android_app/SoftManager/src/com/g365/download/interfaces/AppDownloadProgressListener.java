package com.g365.download.interfaces;

/**
 * 是文件下载数据接口
 * @author nova
 *
 */
public interface AppDownloadProgressListener {

	/**
	 * 
	 * @param downSize
	 *         已下载大小
	 * @param fileSize
	 *         文件大小
	 * @param speed
	 *         下载速度
	 * @param interrupt
	 *         是否中断
	 */
	public void onDownload(int downSize,int fileSize,int speed,boolean interrupt);
}
