package com.g365.receiver.interfaces;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * 
 * @author nova
 * @ ���� 2013��2��27��14:47:43
 * @ ��Ӧ�ó����滻�ļ�����
 *
 */
public interface OnAppReplaceListener {
	/**
	 * Ӧ�ó����滻
	 * 
	 * @param context
	 *            ������
	 * @param packageInfo
	 *            ��Ӧ�õ� PackageInfo
	 */
	public void onReplace(Context context, PackageInfo packageInfo);
}
