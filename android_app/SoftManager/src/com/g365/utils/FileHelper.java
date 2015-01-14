package com.g365.utils;

import java.io.File;

import android.os.Environment;

/**
 * 
 * @author nova
 * �ļ���ز�������
 */
public class FileHelper {

	/** SDCARD·������ File.separator��β */
	public static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + File.separator;

	/**
	 * @return SDCARD �Ƿ�����
	 */
	public static boolean sdCardIsOk() {

		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * ��ȡ�ļ�������׺��
	 * 
	 * @param url
	 *            Զ���ļ���ַ
	 * @return ʧ�ܷ��� null
	 * */
	public static String subFileName1(String url) {
		if (url != null) {
			int start = url.lastIndexOf("/");
			if (start < url.length()) { // ��ʼָ��Ƚ���ָ��С���Ų��״���
				return url.substring(start + 1).trim();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
