package com.g365.entity;

import java.io.Serializable;

/**
 * 
 * @author Administrator
 *  ���ǽ����ʵ����
 */
public class AppWallInfo implements Comparable<AppWallInfo>,Serializable {

	
	private static final long serialVersionUID = 1L;
	/** δ��װ */
	public static final int SORT_NOT_INSTALL = 0;
	/** �߰汾 */
	public static final int SORT_HIGH_VERSION = 1;
	/** �Ͱ汾 */
	public static final int SORT_LOW_VERSION = 2;
	/** �Ѱ�װ */
	public static final int SORT_INSTALLED = 3;
	
	/** ��� id */
	public int id = 0;
	/** ���ص�ַ */
	public String url = "";
	/** ���� */
	public String packagename = "";
	/** �汾�� */
	public int versioncode = 0;
	
	/** ������� */
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
