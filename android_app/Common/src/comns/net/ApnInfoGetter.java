package comns.net;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

/**
 * @ClassName: ApnInfoGetter
 * 
 * @author: chellychi
 * 
 * @version: V1.0
 * 
 * @Date: 2013-8-19 下午9:24:30
 * 
 * @Description: 类<code>ApnInfoGetter</code>是获取当前APN信息的类</p>
 * 
 *               Copyright 2013。 All rights reserved.
 * 
 *               You have the permissions to modify.
 * 
 */
public class ApnInfoGetter {

	public static final Uri PREFERRED_APN_URI;

	private String mApn; // 接入点名称

	private String mPort; // 端口号

	private String mProxy; // 代理服务器

	private boolean mUseWap; // 是否正在使用WAP

	static {
		PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn"); // 取得当前设置的APN
	}

	public ApnInfoGetter(Context context) {
		checkNetworkType(context);
		Log.d(ApnInfoGetter.class.getSimpleName(), "APN:" + getApn() + " PROXY:"
				+ getProxy() + " PROXYPORT:" + getProxyPort() + " USEWAP:"
				+ isWapNetwork());
	}

	/**
	 * 获得当前设置的APN相关参数
	 * 
	 * @param context
	 */
	private void checkApn(Context context) {
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = PREFERRED_APN_URI;
		String[] apnInfo = new String[3];
		apnInfo[0] = "apn";
		apnInfo[1] = "proxy";
		apnInfo[2] = "port";

		Cursor cursor = contentResolver.query(uri, apnInfo, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			this.mApn = cursor.getString(cursor.getColumnIndex("apn"));
			this.mProxy = cursor.getString(cursor.getColumnIndex("proxy"));
			this.mPort = cursor.getString(cursor.getColumnIndex("port"));

			String apn = this.mApn.toUpperCase();

			// 中国移动WAP设置：APN：CMWAP；代理：10.0.0.172；端口：80
			// 中国联通WAP设置：APN：UNIWAP；代理：10.0.0.172；端口：80
			// 中国联通WAP设置（3G）：APN：3GWAP；代理：10.0.0.172；端口：80
			if (apn.equals("CMWAP") || apn.equals("UNIWAP")
					|| (apn.equals("3GWAP"))) {
				if (this.mProxy.indexOf("10.0.0.172") != -1) {
					this.mUseWap = true;
				}
			}

			// 中国电信WAP设置：APN(或者接入点名称)：CTWAP；代理：10.0.0.200；端口：80
			if (apn.equals("CTWAP")) {
				if (this.mProxy.indexOf("10.0.0.200") != -1) {
					this.mUseWap = true;
				}
			}

			cursor.close();
		}

	}

	/**
	 * 检测当前使用的网络类型是WIFI还是WAP
	 * 
	 * @param context
	 */
	private void checkNetworkType(Context context) {
		NetworkInfo networkInfo = ((ConnectivityManager) context
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (networkInfo != null) {
			if (!"wifi".equals(networkInfo.getTypeName().toLowerCase())) {
				checkApn(context);
				return;
			}
			this.mUseWap = false;
		}

	}

	public String getApn() {
		return this.mApn;
	}

	public String getProxy() {
		return this.mProxy;
	}

	public String getProxyPort() {
		return this.mPort;
	}

	public boolean isWapNetwork() {
		return this.mUseWap;
	}

	public static void setCmwapApn(Context context) {

	}

}
