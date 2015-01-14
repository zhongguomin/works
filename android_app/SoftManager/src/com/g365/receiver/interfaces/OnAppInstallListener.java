package com.g365.receiver.interfaces;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * 
 * @author nova
 * @ ���� 2013��2��27��14:26:40
 * @ ��Ӧ�ñ���װ�ļ�����
 *
 */
public interface OnAppInstallListener {

	/**
	 * Ӧ�ñ���װ
	 * 
	 * @param context
	 *            ������
	 * @param packageInfo
	 *            ��Ӧ�õ� PackageInfo
	 */
	public void onInstall(Context context, PackageInfo packageInfo);
}
