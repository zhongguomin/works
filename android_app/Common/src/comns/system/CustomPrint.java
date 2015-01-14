package comns.system;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * @类名: CustomPrint
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-11-1 下午03:45:06
 * 
 * @描述: 类<code>CustomPrint</code>是用来打印调试和 Toast的类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class CustomPrint {

	/** 打印调试信息的开关 */
	public static final boolean LOG_ON = true;

	/**
	 * 调试打印类名和信息
	 * 
	 * @param class_value
	 *            类
	 * @param log
	 *            信息
	 */
	public static void d(Class<?> class_value, String log) {

		if (LOG_ON) {
			String tag = class_value.getName();
			int index = tag.lastIndexOf('.');
			if (index != -1)
				tag = tag.substring(index + 1);
			Log.d(tag, log);
		}
	}

	/**
	 * Toast 资源 ID 指定的字符串
	 * 
	 * @param context
	 *            上下文
	 * @param resID
	 *            字符串资源 ID
	 */
	public static void show(Context context, int resID) {

		Toast.makeText(context, resID, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast 字符串
	 * 
	 * @param context
	 *            上下文
	 * @param msg
	 *            信息
	 */
	public static void show(Context context, String msg) {

		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast 自定义布局
	 * 
	 * @param context
	 *            上下文
	 * @param view
	 *            要显示的View
	 */
	public static void show(Context context, View view) {

		Toast toast = new Toast(context);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
