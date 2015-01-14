package com.g365.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author nova
 * 日期 2013年1月5日14:58:45
 * 
 *  采用SharedPreferences保存数据
 */
public class SoftManagerPreference {

	private static final String PREF = "soft_pref_";

	/** 软件升级个数 */
	public static final String KEY_SOFT_UPDATE = "softupdate";
	/** 软件卸载个数 */
	public static final String KEY_SOFT_UNINSTALL = "softuninstall";
	/** 安装包删除个数 */
	public static final String KEY_SOFT_INSTALLPAGES_DELETE = "softinstallpagesdelete";
	/** 安装包安装个数 */
	public static final String KEY_SOFT_INSTALLPAGES_INSTALL = "softinstallpagesinstall";
	/** 软件搬家个数 */
	public static final String KEY_SOFT_MOVE = "softmove";
	/** 装机必备进入次数 */
	public static final String KEY_SOFT_NECESSARY_ENTER = "softnecessaryenter";
	/** 装机必备下载次数 */
	public static final String KEY_SOFT_NECESSARY_DOWNLOAD = "softnecessarydownload";
	/** 装机必备安装次数 */
	public static final String KEY_SOFT_NECESSARY_INSTALL = "softnecessaryinstall";

	/** 用户数据 */
	public static final String PREF_NAME_USER_DATA = "user_data";
	/** 上次数据发送成功的时间（long） */
	public static final String KEY_LAST_POST_TIME = "lastposttime";

	/** 用户使用数据（多个使用时间点、升级 下载 删除 安装 卸载）（String） */
	public static final String KEY_USE_DATA = "usedata";

	private SharedPreferences sp;

	/**
	 * @param context
	 *            上下文
	 * @param name
	 *            设置名
	 */
	public SoftManagerPreference(Context context, String name) {
		// 打开Preferences，名称为PREF+name，如果存在则打开它，否则创建新的Preferences
		sp = context.getSharedPreferences(PREF + name, 0);

	}

	/**
	 * 将 int 类型的值写入到默认的设置中
	 * 
	 * @param key
	 *            键名
	 * @param value
	 *            int 类型的值
	 */
	public void saveInt(String key, int value) {
		// 让sp处于编辑状态
		SharedPreferences.Editor editor = sp.edit();
		// 存放数据
		editor.putInt(key, value);
		// 完成提交
		editor.commit();
	}

	/**
	 * 获得设置中键名对应的 int 类型的值
	 * 
	 * @param key
	 *            键名
	 * @param value
	 *            默认值
	 * @return 设置中键名对应的值，若没有返回默认值
	 */
	public int readInt(String key, int value) {
		return sp.getInt(key, value);

	}

	/**
	 * 将 String 类型的值写入到默认的设置中
	 * 
	 * @param key
	 *            键名
	 * @param value
	 *            String 类型的值
	 */
	public void saveString(String key, String value) {

		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 获得设置中键名对应的 String 类型的值
	 * 
	 * @param key
	 *            键名
	 * @param value
	 *            默认值
	 * @return 设置中键名对应的值，若没有返回默认值
	 */
	public String readString(String key, String value) {

		return sp.getString(key, value);
	}

	/**
	 * 将 boolean 类型的值写入到默认的设置中
	 * 
	 * @param key
	 *            键名
	 * @param value
	 *            boolean 类型的值
	 */
	public void saveBoolean(String key, boolean value) {

		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 获得设置中键名对应的 boolean 类型的值
	 * 
	 * @param key
	 *            键名
	 * @param value
	 *            默认值
	 * @return 设置中键名对应的值，若没有返回默认值
	 */
	public boolean readBoolean(String key, boolean value) {

		return sp.getBoolean(key, value);
	}

	/**
	 * 将 long 类型的值写入到默认的设置中
	 * 
	 * @param key
	 *            键名
	 * @param value
	 *            long 类型的值
	 */
	public void saveLong(String key, long value) {

		SharedPreferences.Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * 获得设置中键名对应的 long 类型的值
	 * 
	 * @param key
	 *            键名
	 * @param value
	 *            默认值
	 * @return 设置中键名对应的值，若没有返回默认值
	 */
	public long readLong(String key, long value) {

		return sp.getLong(key, value);
	}

	/**
	 * 将 float 类型的值写入到默认的设置中
	 * 
	 * @param key
	 *            键名
	 * @param value
	 *            float 类型的值
	 */
	public void saveFloat(String key, float value) {

		SharedPreferences.Editor editor = sp.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	/**
	 * 获得设置中键名对应的 float 类型的值
	 * 
	 * @param key
	 *            键名
	 * @param value
	 *            默认值
	 * @return 设置中键名对应的值，若没有返回默认值
	 */
	public float readFloat(String key, float value) {

		return sp.getFloat(key, value);
	}

	/**
	 * 保存软件升级的个数
	 * @param context
	 *        上下文
	 * @param sum
	 *      升级个数
	 */
	public static void saveSoftUpdate(Context context, int sum ) {
		SharedPreferences sp1 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp1.edit();
		editor.putInt(KEY_SOFT_UPDATE, sum);
		editor.commit();

	}

	/**
	 * 保存软件卸载的个数
	 * @param context
	 *        上下文
	 * @param sum
	 *      卸载个数
	 */
	public static void saveSoftUninstall(Context context,int sum) {
		SharedPreferences sp2 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp2.edit();
		editor.putInt(KEY_SOFT_UNINSTALL, sum);
		editor.commit();
	}

	/**
	 * 保存安装包删除的个数
	 * @param context
	 *        上下文
	 * @param sum
	 *      安装包删除的个数
	 */
	public static void saveSoftInstalldelete(Context context,int sum) {
		SharedPreferences sp3 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp3.edit();
		editor.putInt(KEY_SOFT_INSTALLPAGES_DELETE, sum);
		editor.commit();
	}

	/**
	 * 保存安装包安装的个数
	 * @param context
	 *        上下文
	 * @param sum
	 *      安装包安装的个数
	 */
	public static void saveSoftInstallPages(Context context,int sum) {
		SharedPreferences sp4 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp4.edit();
		editor.putInt(KEY_SOFT_INSTALLPAGES_INSTALL, sum);
		editor.commit();
	}

	/**
	 * 保存软件搬家的个数
	 * @param context
	 *        上下文
	 * @param sum
	 *      升级个数
	 */
	public static void saveSoftMove(Context context,int sum) {
		SharedPreferences sp5 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp5.edit();
		editor.putInt(KEY_SOFT_MOVE, sum);
		editor.commit();
	}

	/**
	 * 保存装机必备进入次数
	 * @param context
	 *        上下文
	 * @param sum
	 *      装机必备进入次数
	 */
	public static void saveSoftNeccessaryEnter(Context context,int sum) {
		SharedPreferences sp6 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp6.edit();
		editor.putInt(KEY_SOFT_NECESSARY_ENTER, sum);
		editor.commit();
	}
   
	/**
	 * 保存装机必备下载次数
	 * @param context
	 *        上下文
	 * @param sum
	 *      装机必备下载次数
	 */
	public static void saveSoftNeccessaryDownload(Context context,int sum) {
		SharedPreferences sp7 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp7.edit();
		editor.putInt(KEY_SOFT_NECESSARY_DOWNLOAD, sum);
		editor.commit();
	}
	/**
	 * 保存装机必备安装次数
	 * @param context
	 *        上下文
	 * @param sum
	 *        安装个数
	 */
	public static void saveSoftNeccessaryInstall(Context context,int sum) {
		SharedPreferences sp8 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp8.edit();
		editor.putInt(KEY_SOFT_NECESSARY_INSTALL, sum);
		editor.commit();
	}

}
