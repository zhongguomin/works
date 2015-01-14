package comns.system;

import android.content.Context;

/**
 * @类名: Pixels
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2013-2-22 上午10:14:28
 * 
 * @描述: 类<code>Pixels</code>是dip和px间相互转换的类</p>
 * 
 *      Copyright 2013。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class Pixels {

	/**
	 * 将 dip 转换成 px
	 * 
	 * @param context
	 *            上下文
	 * @param dipValue
	 *            dip 值
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将 px 转换成 dip
	 * 
	 * @param context
	 *            上下文
	 * @param pxValue
	 *            px 值
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
