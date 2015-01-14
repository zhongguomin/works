package com.g365.utils;

import java.io.File;

import android.os.Environment;

/**
 * 
 * @author nova
 * 文件相关操作的类
 */
public class FileHelper {

	/** SDCARD路径，以 File.separator结尾 */
	public static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + File.separator;

	/**
	 * @return SDCARD 是否正常
	 */
	public static boolean sdCardIsOk() {

		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 截取文件名带后缀。
	 * 
	 * @param url
	 *            远程文件地址
	 * @return 失败返回 null
	 * */
	public static String subFileName1(String url) {
		if (url != null) {
			int start = url.lastIndexOf("/");
			if (start < url.length()) { // 开始指针比结束指针小，才不抛错误。
				return url.substring(start + 1).trim();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
