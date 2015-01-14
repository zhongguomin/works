package comns.app.receiver.interfaces;

import android.content.Context;

/**
 * @类名: OnAppRemoveListener
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-11-15 下午03:50:01
 * 
 * @描述: 类<code>OnAppRemoveListener</code>是应用被卸载的监听器</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
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
