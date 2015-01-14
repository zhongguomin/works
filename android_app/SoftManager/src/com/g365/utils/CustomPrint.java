package com.g365.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * @����: CustomPrint
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-11-1 ����03:45:06
 * 
 * @����: ��<code>CustomPrint</code>��������ӡ���Ժ� Toast����</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class CustomPrint {

	/** ��ӡ������Ϣ�Ŀ��� */
	public static final boolean LOG_ON = true;

	/**
	 * ���Դ�ӡ��������Ϣ
	 * 
	 * @param class_value
	 *            ��
	 * @param log
	 *            ��Ϣ
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
	 * Toast ��Դ ID ָ�����ַ���
	 * 
	 * @param context
	 *            ������
	 * @param resID
	 *            �ַ�����Դ ID
	 */
	public static void show(Context context, int resID) {

		Toast.makeText(context, resID, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast �ַ���
	 * 
	 * @param context
	 *            ������
	 * @param msg
	 *            ��Ϣ
	 */
	public static void show(Context context, String msg) {

		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
