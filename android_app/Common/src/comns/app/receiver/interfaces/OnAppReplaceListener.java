package comns.app.receiver.interfaces;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * @类名: OnAppReplaceListener
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-11-22 上午10:24:48
 * 
 * @描述: 类<code>OnAppReplaceListener</code>是应用程序被替换的监听器</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
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
