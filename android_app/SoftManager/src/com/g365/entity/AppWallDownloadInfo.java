package com.g365.entity;

public class AppWallDownloadInfo  extends ApplistInfo{

	
	private static final long serialVersionUID = 1L;
	/** �������ļ���С */
	public int curFileSize = 0;
	/** �ļ��ܴ�С */
	public int fileSize = 0;
	/** �����ٶ� */
	public int speed = 0;
	/** ����״̬ */
	public int state = 0;
	/** ������Դ�����ͻ��߹��ǽ�� */
	public int from = 0;
	
	@Override
	public String toString() {
		return "AppWallDownloadInfo [curFileSize=" + curFileSize
				+ ", fileSize=" + fileSize + ", speed=" + speed + ", state="
				+ state + ", from=" + from + "]";
	}
	
	

}
