package com.g365.download;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author nova 
 * 是用来管理当前所有广告下载的类
 */
public class AppDownloadManager {

	public static Map<String, AppOneAdDownloader> downloadMap=new HashMap<String, AppOneAdDownloader>();

	/**
	 * 获得文件的下载器
	 * @param fileUrl
	 * 文件的链接
	 * @return
	 */
	public static AppOneAdDownloader getAppOneAdDownloader(String fileUrl){
		return downloadMap.get(fileUrl);
		
	}
	
   /**
    * 增加一个广告下载
    * @param fileUrl
    *  文件链接
    * @param appOneAdDownloader
    *  广告下载器
    */
	public static void addAppOneAdDownloader(String fileUrl,AppOneAdDownloader appOneAdDownloader){
		downloadMap.put(fileUrl, appOneAdDownloader);
	}
	
	/**
	 * 删除一个广告下载
	 * 
	 * @param fileUrl
	 *            文件链接
	 */
	public static void delAppOneAdDownloader(String fileUrl){
		downloadMap.remove(fileUrl);
	}

	/**
	 * 
	 * @return  获得下载的大小
	 */
	public static int getDownLoadSize(){
		return downloadMap.size();
		
	}
}
