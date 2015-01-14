package com.g365.download;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author nova
 * ����������ǰ�������ص���
 * ���� 2013��1��22��18:00:12
 *
 */
public class DownloadProcess {

	public static Map<String, FileDownloader> downloadMap=new HashMap<String, FileDownloader>();

	/**
	 * ����ļ���������
	 * @param fileUrl
	 * �ļ�������
	 * @return
	 */
    public static FileDownloader  getDownloadProcess(String fileUrl){
    	return downloadMap.get(fileUrl);
    }
    
     /**
      * ����һ������
      * @param fileUrl
      * �ļ�����
      * @param fileDownloader
      * ������
      */
    public static void addFileDownloader(String fileUrl,FileDownloader fileDownloader){
    	downloadMap.put(fileUrl, fileDownloader);
    }
    
    /**
     * ɾ��һ������
     * �ļ�����
     * @param fileUrl
     */
    public static void delFileDownloader(String fileUrl){
		downloadMap.remove(fileUrl);
	}

}
