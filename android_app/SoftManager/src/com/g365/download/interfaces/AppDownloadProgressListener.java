package com.g365.download.interfaces;

/**
 * ���ļ��������ݽӿ�
 * @author nova
 *
 */
public interface AppDownloadProgressListener {

	/**
	 * 
	 * @param downSize
	 *         �����ش�С
	 * @param fileSize
	 *         �ļ���С
	 * @param speed
	 *         �����ٶ�
	 * @param interrupt
	 *         �Ƿ��ж�
	 */
	public void onDownload(int downSize,int fileSize,int speed,boolean interrupt);
}
