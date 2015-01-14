package com.g365.entity;

import android.graphics.drawable.Drawable;

/**
 * ��ȡapk��Ϣ�ŵ�applicaton�� ��Ҫ������� �Ͱ汾 ʵ����
 */
public class AppInfo {

	/**
	 * Ӧ�ó���ͼ��
	 */
	public Drawable icon = null;

	/**
	 * Ӧ�ó�����
	 */
	public String appName = "";

	/**
	 * Ӧ�ó������
	 */
	public String packageName = "";

	/**
	 * Ӧ�ó����С
	 */

	public String appSize = "";

	/**
	 * �汾
	 */
	public String versionName = "";

	/**
	 * �汾��
	 */
	public int versionCode = 0;
    /**
     * checkbox״̬  ѡ�л���δѡ��
     * Ĭ���ǲ�ѡ��״̬
     */
	public boolean isChecked = false;

	/**
	 * apk·��
	 */
	private String filepath;
	/**
	 * apk��״̬ ���ֻ��� ������sd����
	 */
	private boolean flag = false;

	/**
	 * �汾��־
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
