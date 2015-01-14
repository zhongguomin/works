package com.g365.download.interfaces;

/**
 * @类名: DownloadProgressListener
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-26 下午02:32:48
 * 
 * @描述: 类<code>DownloadProgressListener</code>是文件下载数据接口</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public interface DownloadProgressListener {

	/**
	 * @param downSize
	 *            已下载大小
	 * @param fileSize
	 *            文件大小
	 * @param speed
	 *            下载速度
	 * @param interrupt
	 *            是否中断
	 */
	public void onDownload(int downSize, int fileSize, int speed,
			boolean interrupt);
}
