package com.g365.download.interfaces;

import java.util.ArrayList;

import com.g365.entity.AppWallDownloadInfo;

/**
 * 
 * @author nova
 * @���� 2012��12��20��17:57:18
 * �������������ļ�״̬�Ľӿ�
 */
public interface AppAdStateHandler {
	
	/**
	 * ���ݰ����Ͱ汾���ж��Ƿ��µ��ļ�
	 * 
	 * @param packName
	 *            ����
	 * @param versionCode
	 *            �汾��
	 * @return
	 */
	public boolean isNewFile(String packName, int versionCode);
	
	/**
	 * ����µ������ļ�
	 * @param fileUrl
	 *            �ļ�����
	 * @param packName
	 *            ����
	 * @param versionCode
	 *            �汾��
	 * @param fileState
	 *            ����״̬
	 */
	public boolean addNewFile(String fileUrl,  String packName,
			int versionCode, int appid, int fileState);

	/**
	 * ���ݰ����Ͱ汾��ɾ���ļ���Ϣ
	 * @param packName
	 *          ����
	 * @param versionCode
	 *           �汾��
	 * @return
	 */
	public boolean delFile(String packName,int versionCode);
	
	/**
	 * �����ļ�����״̬
	 * @param fileUrl
	 *         �ļ�����
	 * @param packName
	 *         ����
	 * @param versionCode
	 *         �汾��
	 * @param fileState
	 *         �ļ�״̬
	 * @return
	 */
	public boolean updateFile(String fileUrl,String packName,
			int versionCode,int appid, int fileState);
	
	/**
	 * ���ݰ����Ͱ汾�Ÿ����ļ�״̬
	 * @param packName 
	 *        ����
	 * @param versionCode
	 *        �汾��
	 * @param fileState
	 *        Ҫ���µ�״̬
	 * @return
	 */
	public boolean updateState(String packName, int versionCode, int fileState);
	

	/**
	 * ���ݰ����Ͱ汾�Ż�ȡ�ļ�����Ϣ
	 * @param packName
	 *        ����
	 * @param versionCode
	 *        �汾��
	 * @return
	 */
	public AppWallDownloadInfo getAppWallDownloadInfo(String packName,
			int versionCode);
	
	/**
	 * ���ݰ�����ȡ�ļ�����Ϣ�б�
	 * @param packName
	 *        ����
	 * @return
	 */
	public ArrayList<AppWallDownloadInfo> getAppWallDownloadInfos(String packName);
  
   /**
    * ��ȡ�ļ�״̬
    * @param fileUrl
    *        �ļ�����
    * @return
    */
   public int getFileSate(String fileUrl);

}
