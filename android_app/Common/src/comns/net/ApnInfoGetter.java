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
 * @Date: 2013-8-19 ����9:24:30
 * 
 * @Description: ��<code>ApnInfoGetter</code>�ǻ�ȡ��ǰAPN��Ϣ����</p>
 * 
 *               Copyright 2013�� All rights reserved.
 * 
 *               You have the permissions to modify.
 * 
 */
public class ApnInfoGetter {

	public static final Uri PREFERRED_APN_URI;

	private String mApn; // ���������

	private String mPort; // �˿ں�

	private String mProxy; // ���������

	private boolean mUseWap; // �Ƿ�����ʹ��WAP

	static {
		PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn"); // ȡ�õ�ǰ���õ�APN
	}

	public ApnInfoGetter(Context context) {
		checkNetworkType(context);
		Log.d(ApnInfoGetter.class.getSimpleName(), "APN:" + getApn() + " PROXY:"
				+ getProxy() + " PROXYPORT:" + getProxyPort() + " USEWAP:"
				+ isWapNetwork());
	}

	/**
	 * ��õ�ǰ���õ�APN��ز���
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

			// �й��ƶ�WAP���ã�APN��CMWAP������10.0.0.172���˿ڣ�80
			// �й���ͨWAP���ã�APN��UNIWAP������10.0.0.172���˿ڣ�80
			// �й���ͨWAP���ã�3G����APN��3GWAP������10.0.0.172���˿ڣ�80
			if (apn.equals("CMWAP") || apn.equals("UNIWAP")
					|| (apn.equals("3GWAP"))) {
				if (this.mProxy.indexOf("10.0.0.172") != -1) {
					this.mUseWap = true;
				}
			}

			// �й�����WAP���ã�APN(���߽��������)��CTWAP������10.0.0.200���˿ڣ�80
			if (apn.equals("CTWAP")) {
				if (this.mProxy.indexOf("10.0.0.200") != -1) {
					this.mUseWap = true;
				}
			}

			cursor.close();
		}

	}

	/**
	 * ��⵱ǰʹ�õ�����������WIFI����WAP
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
