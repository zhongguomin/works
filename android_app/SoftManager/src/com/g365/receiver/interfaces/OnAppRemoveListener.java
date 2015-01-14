package com.g365.receiver.interfaces;

import android.content.Context;

/**
 * 
 * @author nova
 * @ 日期 2013年2月27日14:34:48
 * @ 是应用被卸载的监听器
 *
 */
public interface OnAppRemoveListener {

	/**
	 * 应用被删除
	 * 
	 * @param context
	 *            上下文
	 * @param packName
	 *            包名
	 */
	public void onRemove(Context context, String packName);
}
