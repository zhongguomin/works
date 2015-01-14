package com.g365.receiver.interfaces;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * 
 * @author nova
 * @ 日期 2013年2月27日14:26:40
 * @ 是应用被安装的监听器
 *
 */
public interface OnAppInstallListener {

	/**
	 * 应用被安装
	 * 
	 * @param context
	 *            上下文
	 * @param packageInfo
	 *            该应用的 PackageInfo
	 */
	public void onInstall(Context context, PackageInfo packageInfo);
}
