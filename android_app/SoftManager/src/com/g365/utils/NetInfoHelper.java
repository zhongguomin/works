package com.g365.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetInfoHelper {

	/** û���κ����� */
	public static final int NULLNET = 0;
	/** CMWAP */
	public static final int CMWAP = 1;
	/** CMNET */
	public static final int CMNET = 2;
	/** WIFI */
	public static final int WIFI = 3;
	/** ���� */
	public static final int CT = 4;
	/** �ƶ�����ͨ���� */
	public static final String CMCC_PROXY = "10.0.0.172";
	/** �������� */
	public static final String CT_PROXY = "10.0.0.200";

	/**
	 * ��ȡ��ǰ��������
	 * 
	 * @param context
	 *            ������
	 * @return ����ǰ�������͵�����
	 */
	public static int getNetType(Context context) {

		if (context == null) {
			return CMNET;
		}

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();// �˴������ǰ��������
		if (info == null || !info.isAvailable())
			return NULLNET;
		else if (info.getType() == ConnectivityManager.TYPE_WIFI)
			return WIFI;
		else {
			String host = android.net.Proxy.getDefaultHost();// ����
			if (host != null) {
				/* ����ģʽ */
				if (host.indexOf(CMCC_PROXY) != -1)
					return CMWAP;
				else if (host.indexOf(CT_PROXY) != -1) {
					return CT;
				}
				return NULLNET;
			} else {
				return CMNET;
			}
		}
	}

	/**
	 * �ж������Ƿ����
	 * 
	 * @param ctx
	 *            ������
	 * @return ��ǰ�����Ƿ����
	 */
	public static boolean isNetworkAvailable(Context context) {

		boolean available = false;

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cm != null) {
			try {
				NetworkInfo info = cm.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					available = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return available;
	}

}
