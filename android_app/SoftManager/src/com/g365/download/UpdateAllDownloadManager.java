package com.g365.download;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nova 是用来管理当前所有广告下载的类 
 * 日期 2013年1月21日14:27:39
 */
public class UpdateAllDownloadManager {

	public static Map<String, UpdateAllDownloader> downloadMap = new HashMap<String, UpdateAllDownloader>();

	/**
	 * 获得文件的下载器
	 * @param fileUrl
	 *            文件的链接
	 * @return
	 */
	public static UpdateAllDownloader getUpdateAllDownloader(String fileUrl) {
		return downloadMap.get(fileUrl);

	}

	/**
	 * 增加一个广告下载
	 * 
	 * @param fileUrl
	 *            文件链接
	 * @param updateAllDownloader
	 *            广告下载器
	 */
	public static void addUpdateAllDownloader(String fileUrl,
			UpdateAllDownloader updateAllDownloader) {
		downloadMap.put(fileUrl, updateAllDownloader);
	}

	public static void delUpdateAllDownloader(String fileUrl) {
		downloadMap.remove(fileUrl);
	}
}
