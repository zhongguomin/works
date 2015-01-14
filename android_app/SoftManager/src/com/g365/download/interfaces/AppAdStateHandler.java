package com.g365.download.interfaces;

import java.util.ArrayList;

import com.g365.entity.AppWallDownloadInfo;

/**
 * 
 * @author nova
 * @日期 2012年12月20日17:57:18
 * 是用来管理广告文件状态的接口
 */
public interface AppAdStateHandler {
	
	/**
	 * 根据包名和版本号判断是否新的文件
	 * 
	 * @param packName
	 *            包名
	 * @param versionCode
	 *            版本号
	 * @return
	 */
	public boolean isNewFile(String packName, int versionCode);
	
	/**
	 * 添加新的下载文件
	 * @param fileUrl
	 *            文件链接
	 * @param packName
	 *            包名
	 * @param versionCode
	 *            版本号
	 * @param fileState
	 *            下载状态
	 */
	public boolean addNewFile(String fileUrl,  String packName,
			int versionCode, int appid, int fileState);

	/**
	 * 根据包名和版本号删除文件信息
	 * @param packName
	 *          包名
	 * @param versionCode
	 *           版本号
	 * @return
	 */
	public boolean delFile(String packName,int versionCode);
	
	/**
	 * 更新文件下载状态
	 * @param fileUrl
	 *         文件链接
	 * @param packName
	 *         包名
	 * @param versionCode
	 *         版本号
	 * @param fileState
	 *         文件状态
	 * @return
	 */
	public boolean updateFile(String fileUrl,String packName,
			int versionCode,int appid, int fileState);
	
	/**
	 * 根据包名和版本号更行文件状态
	 * @param packName 
	 *        包名
	 * @param versionCode
	 *        版本号
	 * @param fileState
	 *        要更新的状态
	 * @return
	 */
	public boolean updateState(String packName, int versionCode, int fileState);
	

	/**
	 * 根据包名和版本号获取文件的信息
	 * @param packName
	 *        包名
	 * @param versionCode
	 *        版本号
	 * @return
	 */
	public AppWallDownloadInfo getAppWallDownloadInfo(String packName,
			int versionCode);
	
	/**
	 * 根据包名获取文件的信息列表
	 * @param packName
	 *        包名
	 * @return
	 */
	public ArrayList<AppWallDownloadInfo> getAppWallDownloadInfos(String packName);
  
   /**
    * 获取文件状态
    * @param fileUrl
    *        文件链接
    * @return
    */
   public int getFileSate(String fileUrl);

}
