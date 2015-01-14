package comns.file.download.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @类名: DownloadStateHandler
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-26 下午04:16:56
 * 
 * @描述: 类<code>DownloadStateHandler</code>是用来管理下载状态的接口</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public interface DownloadStateHandler {

	/**
	 * 是否新的下载文件
	 * 
	 * @param fileUrl
	 *            下载链接
	 * @return
	 */
	public boolean isNewFile(String fileUrl);

	/**
	 * 添加新的下载文件
	 * 
	 * @param fileUrl
	 *            下载链接
	 * @param fileMd5
	 *            文件的 md5值
	 * @param fileSize
	 *            文件的大小
	 * @param fileState
	 *            下载状态
	 * @return
	 */
	public boolean addNewFile(String fileUrl, String fileMd5, int fileSize,
			int fileState);

	/**
	 * 删除文件下载信息
	 * 
	 * @param fileUrl
	 *            下载链接
	 * @return
	 */
	public boolean delFile(String fileUrl);

	/**
	 * 更新文件下载状态
	 * 
	 * @param fileUrl
	 *            下载链接
	 * @param fileMd5
	 *            文件的 md5值
	 * @param fileSize
	 *            文件大小
	 * @param fileState
	 *            文件状态
	 * @return
	 */
	public boolean updateFile(String fileUrl, String fileMd5, int fileSize,
			int fileState);

	/**
	 * 获取文件大小
	 * 
	 * @param fileUrl
	 *            下载链接
	 * @return
	 */
	public int getFileSize(String fileUrl);

	/**
	 * 获取文件下载线程状态
	 * 
	 * @param fileUrl
	 *            下载链接
	 * @return
	 */
	public ArrayList<HashMap<Integer, Integer>> getThreadState(String fileUrl);

	/**
	 * 添加一条新的线程下载任务
	 * 
	 * @param fileUrl
	 *            下载链接
	 * @param threadData
	 *            线程数据
	 * @return
	 */
	public boolean addNewThreadTask(String fileUrl, int threadId,
			HashMap<Integer, Integer> threadData);

	/**
	 * 更新一条线程下载信息
	 * 
	 * @param fileUrl
	 *            下载链接
	 * @param threadData
	 *            线程数据
	 * @return
	 */
	public boolean updateThreadTask(String fileUrl, int threadId,
			HashMap<Integer, Integer> threadData);

	/**
	 * 删除文件线程下载信息
	 * 
	 * @param fileUrl
	 *            下载链接
	 * @return
	 */
	public boolean deleteThreadTask(String fileUrl);

}
