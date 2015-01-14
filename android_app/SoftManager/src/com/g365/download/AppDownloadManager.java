package com.g365.download;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author nova 
 * ����������ǰ���й�����ص���
 */
public class AppDownloadManager {

	public static Map<String, AppOneAdDownloader> downloadMap=new HashMap<String, AppOneAdDownloader>();

	/**
	 * ����ļ���������
	 * @param fileUrl
	 * �ļ�������
	 * @return
	 */
	public static AppOneAdDownloader getAppOneAdDownloader(String fileUrl){
		return downloadMap.get(fileUrl);
		
	}
	
   /**
    * ����һ���������
    * @param fileUrl
    *  �ļ�����
    * @param appOneAdDownloader
    *  ���������
    */
	public static void addAppOneAdDownloader(String fileUrl,AppOneAdDownloader appOneAdDownloader){
		downloadMap.put(fileUrl, appOneAdDownloader);
	}
	
	/**
	 * ɾ��һ���������
	 * 
	 * @param fileUrl
	 *            �ļ�����
	 */
	public static void delAppOneAdDownloader(String fileUrl){
		downloadMap.remove(fileUrl);
	}

	/**
	 * 
	 * @return  ������صĴ�С
	 */
	public static int getDownLoadSize(){
		return downloadMap.size();
		
	}
}
