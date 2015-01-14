package com.g365.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * @类名: SystemHelper
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-26 下午10:39:27
 * 
 * @描述: 类<code>SystemHelper</code>是封装了一些和系统相关的常用的函数的类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class SystemHelper {

	/** getPendingIntent</P>获取一个启动Acitivity 的 PendingIntent */
	public static final int MODE_PI_ACTIVITY = 0;
	/** getPendingIntent</P>获取一个启动Service 的 PendingIntent */
	public static final int MODE_PI_SERVICE = 1;
	/** getPendingIntent</P>获取一个广播 的 PendingIntent */
	public static final int MODE_PI_BROADCAST = 2;

	/**
	 * 获取一个用户指定的 PendingIntent
	 * 
	 * @param context
	 *            上下文
	 * @param id
	 *            PendingIntent的唯一性 ID
	 * @param mode
	 *            点击后执行的操作类型（0:Acitivity;1:Service;2:Broadcast）
	 * @param intent
	 *            点击后执行的 Intent
	 * @return
	 */
	public static PendingIntent getPendingIntent(Context context, int id,
			int mode, Intent intent) {

		PendingIntent pendingIntent;

		switch (mode) {// 根据模式获取对应的 PendingIntent
		case MODE_PI_SERVICE:
			pendingIntent = PendingIntent.getService(context, id, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case MODE_PI_BROADCAST:
			pendingIntent = PendingIntent.getBroadcast(context, id, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		default:
			pendingIntent = PendingIntent.getActivity(context, id, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		}

		return pendingIntent;
	}
}
