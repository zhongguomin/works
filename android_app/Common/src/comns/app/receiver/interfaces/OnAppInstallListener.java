package comns.app.receiver.interfaces;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * @类名: OnAppInstallListener
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-11-15 下午03:50:27
 * 
 * @描述: 类<code>OnAppInstallListener</code>是应用被安装的监听器</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
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
