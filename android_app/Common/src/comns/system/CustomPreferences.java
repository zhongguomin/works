package comns.system;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @类名: CustomPreferences
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-11-1 下午03:44:22
 * 
 * @描述: 类<code>CustomPreferences</code>是管理设置相关的类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class CustomPreferences {

	private static final String PREF = "my_pref_";

	private SharedPreferences sp;

	/**
	 * @param context
	 *            上下文
	 * @param name
	 *            设置名
	 */
	public CustomPreferences(Context context, String name) {

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

		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, value);
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
}
