package com.g365.entity;

import java.io.Serializable;

/**
 * 
 * @author Administrator
 *  广告墙广告的实体类
 */
public class AppWallInfo implements Comparable<AppWallInfo>,Serializable {

	
	private static final long serialVersionUID = 1L;
	/** 未安装 */
	public static final int SORT_NOT_INSTALL = 0;
	/** 高版本 */
	public static final int SORT_HIGH_VERSION = 1;
	/** 低版本 */
	public static final int SORT_LOW_VERSION = 2;
	/** 已安装 */
	public static final int SORT_INSTALLED = 3;
	
	/** 广告 id */
	public int id = 0;
	/** 下载地址 */
	public String url = "";
	/** 包名 */
	public String packagename = "";
	/** 版本号 */
	public int versioncode = 0;
	
	/** 广告名字 */
	public String name = "";
	
	
	@Override
	public String toString() {
		return "AppWallInfo [id=" + id + ", url=" + url + ", packagename="
				+ packagename + ", versioncode=" + versioncode + ", name="
				+ name + "]";
	}

	public int compareTo(AppWallInfo another) {
		// TODO Auto-generated method stub
		return 0;
	}

}
