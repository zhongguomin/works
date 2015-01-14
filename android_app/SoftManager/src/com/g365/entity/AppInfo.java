package com.g365.entity;

import android.graphics.drawable.Drawable;

/**
 * 读取apk信息放到applicaton表 主要存入包名 和版本 实体类
 */
public class AppInfo {

	/**
	 * 应用程序图标
	 */
	public Drawable icon = null;

	/**
	 * 应用程序名
	 */
	public String appName = "";

	/**
	 * 应用程序包名
	 */
	public String packageName = "";

	/**
	 * 应用程序大小
	 */

	public String appSize = "";

	/**
	 * 版本
	 */
	public String versionName = "";

	/**
	 * 版本号
	 */
	public int versionCode = 0;
    /**
     * checkbox状态  选中或者未选中
     * 默认是不选中状态
     */
	public boolean isChecked = false;

	/**
	 * apk路径
	 */
	private String filepath;
	/**
	 * apk的状态 在手机上 还是在sd卡上
	 */
	private boolean flag = false;

	/**
	 * 版本标志
	 */
	private int versionflag;

	public int getVersionflag() {
		return versionflag;
	}

	public void setVersionflag(int versionflag) {
		this.versionflag = versionflag;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public AppInfo() {
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppSize() {
		return appSize;
	}

	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public AppInfo(Drawable icon, String appName, String packageName,
			String appSize, String versionName, int versionCode) {

		this.icon = icon;
		this.appName = appName;
		this.packageName = packageName;
		this.appSize = appSize;
		this.versionName = versionName;
		this.versionCode = versionCode;
	}

	public AppInfo(String packageName, int versionCode) {

		this.packageName = packageName;
		this.versionCode = versionCode;

	}

}
