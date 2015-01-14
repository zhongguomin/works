package com.g365.download;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nova ����������ǰ���й�����ص��� 
 * ���� 2013��1��21��14:27:39
 */
public class UpdateAllDownloadManager {

	public static Map<String, UpdateAllDownloader> downloadMap = new HashMap<String, UpdateAllDownloader>();

	/**
	 * ����ļ���������
	 * @param fileUrl
	 *            �ļ�������
	 * @return
	 */
	public static UpdateAllDownloader getUpdateAllDownloader(String fileUrl) {
		return downloadMap.get(fileUrl);

	}

	/**
	 * ����һ���������
	 * 
	 * @param fileUrl
	 *            �ļ�����
	 * @param updateAllDownloader
	 *            ���������
	 */
	public static void addUpdateAllDownloader(String fileUrl,
			UpdateAllDownloader updateAllDownloader) {
		downloadMap.put(fileUrl, updateAllDownloader);
	}

	public static void delUpdateAllDownloader(String fileUrl) {
		downloadMap.remove(fileUrl);
	}
}
