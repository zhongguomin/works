package comns.system;

import comns.phone.PhoneInfoHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;

/**
 * @类名: CustomAlertDialog
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2013-2-22 上午11:23:03
 * 
 * @描述: 类<code>CustomAlertDialog</code>是提供几种系统默认常用类型对话框的类</p>
 * 
 *      Copyright 2013。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class CustomAlertDialog {

	private Context context;

	public CustomAlertDialog(Context context) {

		this.context = context;
	}

	/**
	 * 显示“确定”、“取消”对话框
	 * 
	 * @param message
	 *            提示内容
	 * @param listener
	 *            点击“确定”后的监听器
	 */
	public void showYesNoDialog(String message,
			DialogInterface.OnClickListener listener) {
		new AlertDialog.Builder(context).setMessage(message)
				.setPositiveButton("确定", listener)
				.setNegativeButton("取消", null).create().show();
	}

	/**
	 * 显示多选对话框
	 * 
	 * @param title
	 *            标题
	 * @param strArray
	 *            多选框每项的描述
	 * @param bData
	 *            初始化每项是否选中的布尔数组
	 * @param listener1
	 *            每项选择和取消选择的监听器
	 * @param listener2
	 *            点击“确定”后的监听器
	 */
	public void showMultiChooseDialog(String title, String[] strArray,
			boolean[] bData,
			DialogInterface.OnMultiChoiceClickListener listener1,
			DialogInterface.OnClickListener listener2) {
		new AlertDialog.Builder(context).setTitle(title)
				.setMultiChoiceItems(strArray, bData, listener1)
				.setPositiveButton("确定", listener2)
				.setNegativeButton("取消", null).show();
	}

	/**
	 * 显示单选对话框
	 * 
	 * @param title
	 *            标题
	 * @param strArray
	 *            单选框每项的描述
	 * @param listener1
	 *            每项选择的监听器
	 * @param listener2
	 *            点击“确定”后的监听器
	 */
	public void showSingleChooseDialog(String title, String[] strArray,
			DialogInterface.OnClickListener listener1,
			DialogInterface.OnClickListener listener2) {

		new AlertDialog.Builder(context).setTitle(title)
				.setSingleChoiceItems(strArray, 0, listener1)
				.setPositiveButton("确定", listener2)
				.setNegativeButton("取消", null).show();

	}

	/**
	 * 显示自定义对话框
	 * 
	 * @param context
	 *            上下文
	 * @param view
	 *            自定义视图
	 */
	public static void showCustomDialog(Context context, View view) {

		Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);
		dialog.show();
	}
	
	/**
	 * 设置对话框为全屏（在Dialog.setContentView()之后调用）
	 * 
	 * @param context
	 *            上下文
	 * @param dialog
	 *            要设置的对话框
	 */
	public static void setDialogFullScreen(Activity context, Dialog dialog) {

		LayoutParams lp = dialog.getWindow().getAttributes();
		int[] resolution = PhoneInfoHelper.getResolution(context);
		Rect rect = new Rect();
		View view = context.getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		lp.width = resolution[0];
		lp.height = resolution[1] - rect.top;
	}
}