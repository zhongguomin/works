package com.g365.download;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author nova
 * 是用来管理当前所有下载的类
 * 日期 2013年1月22日18:00:12
 *
 */
public class DownloadProcess {

	public static Map<String, FileDownloader> downloadMap=new HashMap<String, FileDownloader>();

	/**
	 * 获得文件的下载器
	 * @param fileUrl
	 * 文件的链接
	 * @return
	 */
    public static FileDownloader  getDownloadProcess(String fileUrl){
    	return downloadMap.get(fileUrl);
    }
    
     /**
      * 增加一个下载
      * @param fileUrl
      * 文件链接
      * @param fileDownloader
      * 下载器
      */
    public static void addFileDownloader(String fileUrl,FileDownloader fileDownloader){
    	downloadMap.put(fileUrl, fileDownloader);
    }
    
    /**
     * 删除一个下载
     * 文件链接
     * @param fileUrl
     */
    public static void delFileDownloader(String fileUrl){
		downloadMap.remove(fileUrl);
	}

}
