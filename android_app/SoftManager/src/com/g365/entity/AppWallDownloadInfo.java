package com.g365.entity;

public class AppWallDownloadInfo  extends ApplistInfo{

	
	private static final long serialVersionUID = 1L;
	/** 已下载文件大小 */
	public int curFileSize = 0;
	/** 文件总大小 */
	public int fileSize = 0;
	/** 下载速度 */
	public int speed = 0;
	/** 下载状态 */
	public int state = 0;
	/** 下载来源（推送或者广告墙） */
	public int from = 0;
	
	@Override
	public String toString() {
		return "AppWallDownloadInfo [curFileSize=" + curFileSize
				+ ", fileSize=" + fileSize + ", speed=" + speed + ", state="
				+ state + ", from=" + from + "]";
	}
	
	

}
