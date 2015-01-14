package com.g365.entity;

import java.io.Serializable;

/**
 * 用来存入解析滑动图片实体类
 * 
 * @author nova
 * 
 */
public class ImageInfo  implements Comparable<ImageInfo>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 已下载文件大小 */
	public int curFileSize = 0;
	/** 文件总大小 */
	public int fileSize = 0;
	/** 下载速度 */
	public int speed = 0;
	/**
	 * 图片的url地址
	 */
	public String app_imageurl;
	public String apps_packagename;
	

	public String apps__sdkversion;
	/** apk图标 */
	public String apps_icon;
	/** apk名称 */
	public String apps_Name;
	/** apk简介 */
	public String apps_intro;
	
	public String apps_downloads;
	
	public String getApps_downloads() {
		return apps_downloads;
	}

	public void setApps_downloads(String apps_downloads) {
		this.apps_downloads = apps_downloads;
	}

	public String getApps_intro() {
		return apps_intro;
	}

	public void setApps_intro(String apps_intro) {
		this.apps_intro = apps_intro;
	}

	public ImageInfo() {

	}
	public String getApps_packagename() {
		return apps_packagename;
	}

	public void setApps_packagename(String apps_packagename) {
		this.apps_packagename = apps_packagename;
	}
	public String getApps_icon() {
		return apps_icon;
	}

	public void setApps_icon(String apps_icon) {
		this.apps_icon = apps_icon;
	}

	public String getApps_Name() {
		return apps_Name;
	}

	public void setApps_Name(String apps_Name) {
		this.apps_Name = apps_Name;
	}

	

	public String getApps__sdkversion() {
		return apps__sdkversion;
	}

	public void setApps__sdkversion(String apps__sdkversion) {
		this.apps__sdkversion = apps__sdkversion;
	}

	public String getApp_imageurl() {
		return app_imageurl;
	}

	public void setApp_imageurl(String app_imageurl) {
		this.app_imageurl = app_imageurl;
	}

	

	@Override
	public String toString() {
		return "ImageInfo [app_imageurl=" + app_imageurl
				+ ", apps_packagename=" + apps_packagename
				+ ", apps__sdkversion=" + apps__sdkversion + ", apps_icon="
				+ apps_icon + ", apps_Name=" + apps_Name + ", apps_intro="
				+ apps_intro + ", apps_downloads=" + apps_downloads + "]";
	}

	public int compareTo(ImageInfo another) {
		return 0;
	}

}
