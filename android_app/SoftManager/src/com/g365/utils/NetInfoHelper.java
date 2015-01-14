package com.g365.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetInfoHelper {

	/** 没有任何连接 */
	public static final int NULLNET = 0;
	/** CMWAP */
	public static final int CMWAP = 1;
	/** CMNET */
	public static final int CMNET = 2;
	/** WIFI */
	public static final int WIFI = 3;
	/** 电信 */
	public static final int CT = 4;
	/** 移动和联通网关 */
	public static final String CMCC_PROXY = "10.0.0.172";
	/** 电信网关 */
	public static final String CT_PROXY = "10.0.0.200";

	/**
	 * 获取当前网络类型
	 * 
	 * @param context
	 *            上下文
	 * @return 代表当前网络类型的数字
	 */
	public static int getNetType(Context context) {

		if (context == null) {
			return CMNET;
		}

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();// 此处输出当前可用网络
		if (info == null || !info.isAvailable())
			return NULLNET;
		else if (info.getType() == ConnectivityManager.TYPE_WIFI)
			return WIFI;
		else {
			String host = android.net.Proxy.getDefaultHost();// 代理
			if (host != null) {
				/* 代理模式 */
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
	 * 判断网络是否可用
	 * 
	 * @param ctx
	 *            上下文
	 * @return 当前网络是否可用
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
