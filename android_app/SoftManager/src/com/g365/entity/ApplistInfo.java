package com.g365.entity;

import java.io.Serializable;

/**
 * 用来存入解析应用推荐实体类
 * @author nova
 *
 */
public class ApplistInfo implements Comparable<ApplistInfo>, Serializable{
	
	
	private static final long serialVersionUID = 3101512804933816348L;
	/**
     * app id号
     */
	public int app_id;
	
	/**
	 * app应用版本
	 */
	public int versioncode=0;
	/**
	 * app应用名称
	 */
	public String app_Name;
	/**
	 * app应用图标
	 */
	public String app_icon;
	/**
	 * app应用评分
	 */
	public int app_score;
	/**
	 * app应用大小
	 */
	public int app_size;
	/**
	 * app应用版本
	 */
	public String app_sdkversion;
	/**
	 * app应用下载的url地址
	 */
	public String app_download;
	/**
	 * app应用介绍
	 */
	public String app_intro;
	
	/**
	 * app的包名
	 */
	public  String packagename;
	

	/** 该广告用语排序的标识 */
	public int sortValue = 0;
	
	public ApplistInfo() {
		super();
	
	}
	

	public int getVersioncode() {
		return versioncode;
	}

	public void setVersioncode(int versioncode) {
		this.versioncode = versioncode;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public int getApp_id() {
		return app_id;
	}
	public void setApp_id(int app_id) {
		this.app_id = app_id;
	}
	public String getApp_Name() {
		return app_Name;
	}
	public void setApp_Name(String app_Name) {
		this.app_Name = app_Name;
	}
	
	public String getApp_icon() {
		return app_icon;
	}

	public void setApp_icon(String app_icon) {
		this.app_icon = app_icon;
	}

	public int getApp_score() {
		return app_score;
	}
	public void setApp_score(int app_score) {
		this.app_score = app_score;
	}
	public int getApp_size() {
		return app_size;
	}
	public void setApp_size(int app_size) {
		this.app_size = app_size;
	}
	
	public String getApp_sdkversion() {
		return app_sdkversion;
	}

	public void setApp_sdkversion(String app_sdkversion) {
		this.app_sdkversion = app_sdkversion;
	}

	public String getApp_download() {
		return app_download;
	}
	public void setApp_download(String app_download) {
		this.app_download = app_download;
	}
	public String getApp_intro() {
		return app_intro;
	}
	public void setApp_intro(String app_intro) {
		this.app_intro = app_intro;
	}
	
	

	public ApplistInfo(int app_id, String app_Name, String app_icon,
			int app_score, int app_size, String app_sdkversion,
			String app_download, String app_intro) {
		super();
		this.app_id = app_id;
		this.app_Name = app_Name;
		this.app_icon = app_icon;
		this.app_score = app_score;
		this.app_size = app_size;
		this.app_sdkversion = app_sdkversion;
		this.app_download = app_download;
		this.app_intro = app_intro;
	}



	@Override
	public String toString() {
		return "ApplistInfo [app_id=" + app_id + ", versioncode=" + versioncode
				+ ", app_Name=" + app_Name + ", app_icon=" + app_icon
				+ ", app_score=" + app_score + ", app_size=" + app_size
				+ ", app_sdkversion=" + app_sdkversion + ", app_download="
				+ app_download + ", app_intro=" + app_intro + ", packagename="
				+ packagename + ", sortValue=" + sortValue + "]";
	}


	public int compareTo(ApplistInfo another) {
		return sortValue-another.sortValue;
	}
}
