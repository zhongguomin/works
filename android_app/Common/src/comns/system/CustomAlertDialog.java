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
 * @����: CustomAlertDialog
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2013-2-22 ����11:23:03
 * 
 * @����: ��<code>CustomAlertDialog</code>���ṩ����ϵͳĬ�ϳ������ͶԻ������</p>
 * 
 *      Copyright 2013�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class CustomAlertDialog {

	private Context context;

	public CustomAlertDialog(Context context) {

		this.context = context;
	}

	/**
	 * ��ʾ��ȷ��������ȡ�����Ի���
	 * 
	 * @param message
	 *            ��ʾ����
	 * @param listener
	 *            �����ȷ������ļ�����
	 */
	public void showYesNoDialog(String message,
			DialogInterface.OnClickListener listener) {
		new AlertDialog.Builder(context).setMessage(message)
				.setPositiveButton("ȷ��", listener)
				.setNegativeButton("ȡ��", null).create().show();
	}

	/**
	 * ��ʾ��ѡ�Ի���
	 * 
	 * @param title
	 *            ����
	 * @param strArray
	 *            ��ѡ��ÿ�������
	 * @param bData
	 *            ��ʼ��ÿ���Ƿ�ѡ�еĲ�������
	 * @param listener1
	 *            ÿ��ѡ���ȡ��ѡ��ļ�����
	 * @param listener2
	 *            �����ȷ������ļ�����
	 */
	public void showMultiChooseDialog(String title, String[] strArray,
			boolean[] bData,
			DialogInterface.OnMultiChoiceClickListener listener1,
			DialogInterface.OnClickListener listener2) {
		new AlertDialog.Builder(context).setTitle(title)
				.setMultiChoiceItems(strArray, bData, listener1)
				.setPositiveButton("ȷ��", listener2)
				.setNegativeButton("ȡ��", null).show();
	}

	/**
	 * ��ʾ��ѡ�Ի���
	 * 
	 * @param title
	 *            ����
	 * @param strArray
	 *            ��ѡ��ÿ�������
	 * @param listener1
	 *            ÿ��ѡ��ļ�����
	 * @param listener2
	 *            �����ȷ������ļ�����
	 */
	public void showSingleChooseDialog(String title, String[] strArray,
			DialogInterface.OnClickListener listener1,
			DialogInterface.OnClickListener listener2) {

		new AlertDialog.Builder(context).setTitle(title)
				.setSingleChoiceItems(strArray, 0, listener1)
				.setPositiveButton("ȷ��", listener2)
				.setNegativeButton("ȡ��", null).show();

	}

	/**
	 * ��ʾ�Զ���Ի���
	 * 
	 * @param context
	 *            ������
	 * @param view
	 *            �Զ�����ͼ
	 */
	public static void showCustomDialog(Context context, View view) {

		Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);
		dialog.show();
	}
	
	/**
	 * ���öԻ���Ϊȫ������Dialog.setContentView()֮����ã�
	 * 
	 * @param context
	 *            ������
	 * @param dialog
	 *            Ҫ���õĶԻ���
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