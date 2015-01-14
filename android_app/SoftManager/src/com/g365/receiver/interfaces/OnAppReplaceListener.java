package com.g365.receiver.interfaces;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * 
 * @author nova
 * @ 日期 2013年2月27日14:47:43
 * @ 是应用程序被替换的监听器
 *
 */
public interface OnAppReplaceListener {
	/**
	 * 应用程序被替换
	 * 
	 * @param context
	 *            上下文
	 * @param packageInfo
	 *            该应用的 PackageInfo
	 */
	public void onReplace(Context context, PackageInfo packageInfo);
}
