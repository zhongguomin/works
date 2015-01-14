package comns.net;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;

import comns.phone.PhoneInfoHelper;
import comns.system.CustomPrint;

public class ApnManager {

	/** ����APN */
	public static final Uri APN_URI_TABLES = Uri
			.parse("content://telephony/carriers");
	/** ��ǰѡ��APN */
	public static final Uri APN_URI_PREFER = Uri
			.parse("content://telephony/carriers/preferapn");
	public static final Uri APN_URI_CURRENT = Uri
			.parse("content://telephony/carriers/current");
	private static String[] projection = { "_id", "name", "apn", "proxy",
			"port", "user", "password", "server", "mmsc", "mmsproxy",
			"mmsport", "mcc", "mnc", "authtype", "type", "numeric", "current" };

	/**
	 * �ж�APN�Ƿ���޸�
	 * 
	 * @param context
	 *            ������
	 * @return
	 */
	public static boolean isApnAccessable(Context context) {

		boolean accessable = context.getPackageManager().checkPermission(
				"android.permission.WRITE_APN_SETTINGS",
				context.getPackageName()) == PackageManager.PERMISSION_GRANTED;

		CustomPrint.d(ApnManager.class, "isApnAccessable:" + accessable);

		return accessable;
	}

	/**
	 * ��ȡ����APN�б�
	 * 
	 * @param context
	 *            ������
	 * @return
	 */
	public static ArrayList<ApnInfo> getApnList(Context context) {

		ArrayList<ApnInfo> apnList = new ArrayList<ApnInfo>();

		Cursor cursor = context.getContentResolver().query(APN_URI_TABLES,
				projection, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {

			do {
				ApnInfo mApnInfo = new ApnManager.ApnInfo();
				mApnInfo.id = cursor.getInt(cursor.getColumnIndex("_id"));
				mApnInfo.name = cursor.getString(cursor.getColumnIndex("name"));
				mApnInfo.apn = cursor.getString(cursor.getColumnIndex("apn"));
				mApnInfo.proxy = cursor.getString(cursor
						.getColumnIndex("proxy"));
				mApnInfo.port = cursor.getString(cursor.getColumnIndex("port"));
				mApnInfo.user = cursor.getString(cursor.getColumnIndex("user"));
				mApnInfo.password = cursor.getString(cursor
						.getColumnIndex("password"));
				mApnInfo.server = cursor.getString(cursor
						.getColumnIndex("server"));
				mApnInfo.mmsc = cursor.getString(cursor.getColumnIndex("mmsc"));
				mApnInfo.mmsproxy = cursor.getString(cursor
						.getColumnIndex("mmsproxy"));
				mApnInfo.mmsport = cursor.getString(cursor
						.getColumnIndex("mmsport"));
				mApnInfo.mcc = cursor.getString(cursor.getColumnIndex("mcc"));
				mApnInfo.mnc = cursor.getString(cursor.getColumnIndex("mnc"));
				mApnInfo.authtype = cursor.getString(cursor
						.getColumnIndex("authtype"));
				mApnInfo.type = cursor.getString(cursor.getColumnIndex("type"));
				mApnInfo.numeric = cursor.getString(cursor
						.getColumnIndex("numeric"));
				mApnInfo.current = cursor.getString(cursor
						.getColumnIndex("current"));
				apnList.add(mApnInfo);
			} while (cursor.moveToNext());

			cursor.close();
		}

		return apnList;
	}

	/**
	 * ��ȡ���п��õ�APN�б�
	 * 
	 * @param context
	 *            ������
	 * @return
	 */
	public static ArrayList<ApnInfo> getAvailableApnList(Context context) {

		ArrayList<ApnInfo> apnList = new ArrayList<ApnInfo>();

		Cursor cursor = context.getContentResolver().query(APN_URI_TABLES,
				projection, "current is not null", null, null);
		if (cursor != null && cursor.moveToFirst()) {

			do {
				ApnInfo mApnInfo = new ApnManager.ApnInfo();
				mApnInfo.id = cursor.getInt(cursor.getColumnIndex("_id"));
				mApnInfo.name = cursor.getString(cursor.getColumnIndex("name"));
				mApnInfo.apn = cursor.getString(cursor.getColumnIndex("apn"));
				mApnInfo.proxy = cursor.getString(cursor
						.getColumnIndex("proxy"));
				mApnInfo.port = cursor.getString(cursor.getColumnIndex("port"));
				mApnInfo.user = cursor.getString(cursor.getColumnIndex("user"));
				mApnInfo.password = cursor.getString(cursor
						.getColumnIndex("password"));
				mApnInfo.server = cursor.getString(cursor
						.getColumnIndex("server"));
				mApnInfo.mmsc = cursor.getString(cursor.getColumnIndex("mmsc"));
				mApnInfo.mmsproxy = cursor.getString(cursor
						.getColumnIndex("mmsproxy"));
				mApnInfo.mmsport = cursor.getString(cursor
						.getColumnIndex("mmsport"));
				mApnInfo.mcc = cursor.getString(cursor.getColumnIndex("mcc"));
				mApnInfo.mnc = cursor.getString(cursor.getColumnIndex("mnc"));
				mApnInfo.authtype = cursor.getString(cursor
						.getColumnIndex("authtype"));
				mApnInfo.type = cursor.getString(cursor.getColumnIndex("type"));
				mApnInfo.numeric = cursor.getString(cursor
						.getColumnIndex("numeric"));
				mApnInfo.current = cursor.getString(cursor
						.getColumnIndex("current"));
				apnList.add(mApnInfo);
			} while (cursor.moveToNext());

			cursor.close();
		}

		return apnList;
	}

	/**
	 * ��ȡ��ǰѡ���APN
	 * 
	 * @param context
	 *            ������
	 * @return û�з���null
	 */
	public static ApnInfo getPreferApn(Context context) {

		ApnInfo mApnInfo = null;

		Cursor cursor = context.getContentResolver().query(APN_URI_PREFER,
				projection, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			mApnInfo = new ApnInfo();
			mApnInfo.id = cursor.getInt(cursor.getColumnIndex("_id"));
			mApnInfo.name = cursor.getString(cursor.getColumnIndex("name"));
			mApnInfo.apn = cursor.getString(cursor.getColumnIndex("apn"));
			mApnInfo.proxy = cursor.getString(cursor.getColumnIndex("proxy"));
			mApnInfo.port = cursor.getString(cursor.getColumnIndex("port"));
			mApnInfo.user = cursor.getString(cursor.getColumnIndex("user"));
			mApnInfo.password = cursor.getString(cursor
					.getColumnIndex("password"));
			mApnInfo.server = cursor.getString(cursor.getColumnIndex("server"));
			mApnInfo.mmsc = cursor.getString(cursor.getColumnIndex("mmsc"));
			mApnInfo.mmsproxy = cursor.getString(cursor
					.getColumnIndex("mmsproxy"));
			mApnInfo.mmsport = cursor.getString(cursor
					.getColumnIndex("mmsport"));
			mApnInfo.mcc = cursor.getString(cursor.getColumnIndex("mcc"));
			mApnInfo.mnc = cursor.getString(cursor.getColumnIndex("mnc"));
			mApnInfo.authtype = cursor.getString(cursor
					.getColumnIndex("authtype"));
			mApnInfo.type = cursor.getString(cursor.getColumnIndex("type"));
			mApnInfo.numeric = cursor.getString(cursor
					.getColumnIndex("numeric"));
			mApnInfo.current = cursor.getString(cursor
					.getColumnIndex("current"));
			cursor.close();
		}

		return mApnInfo;
	}

	/**
	 * ���һ���µ�APN
	 * 
	 * @param newApn
	 *            �µ�APN
	 * @return �ɹ�������ID��ʧ�ܷ���-1
	 */
	public static int addApn(Context context, ApnInfo newApn) {

		int apnId = -1;

		try {
			/* ����ApnInfo���ContentValues��Ϊnull����� */
			ContentValues mContentValues = new ContentValues();
			String temp = newApn.name;
			if (temp != null)
				mContentValues.put("name", temp);
			temp = newApn.apn;
			if (temp != null)
				mContentValues.put("apn", temp);
			temp = newApn.proxy;
			if (temp != null)
				mContentValues.put("proxy", temp);
			temp = newApn.port;
			if (temp != null)
				mContentValues.put("port", temp);
			temp = newApn.user;
			if (temp != null)
				mContentValues.put("user", temp);
			temp = newApn.password;
			if (temp != null)
				mContentValues.put("password", temp);
			temp = newApn.server;
			if (temp != null)
				mContentValues.put("server", temp);
			temp = newApn.mmsc;
			if (temp != null)
				mContentValues.put("mmsc", temp);
			temp = newApn.mmsproxy;
			if (temp != null)
				mContentValues.put("mmsproxy", temp);
			temp = newApn.mmsport;
			if (temp != null)
				mContentValues.put("mmsport", temp);
			temp = newApn.mcc;
			if (temp != null)
				mContentValues.put("mcc", temp);
			temp = newApn.mnc;
			if (temp != null)
				mContentValues.put("mnc", temp);
			temp = newApn.authtype;
			if (temp != null)
				mContentValues.put("authtype", temp);
			temp = newApn.type;
			if (temp != null)
				mContentValues.put("type", temp);
			temp = newApn.numeric;
			if (temp != null)
				mContentValues.put("numeric", temp);
			temp = newApn.current;
			if (temp != null)
				mContentValues.put("current", temp);

			Uri newUri = context.getContentResolver().insert(APN_URI_TABLES,
					mContentValues);
			if (newUri != null) {
				Cursor mCursor = context.getContentResolver().query(newUri,
						projection, null, null, null);
				if (mCursor != null && mCursor.moveToFirst()) {
					apnId = mCursor.getInt(mCursor.getColumnIndex("_id"));
					mCursor.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return apnId;

	}

	/**
	 * ����IDɾ��APN
	 * 
	 * @param context
	 *            ������
	 * @param id
	 *            Ҫɾ����ID
	 * @return
	 */
	public static boolean delApnById(Context context, int id) {

		int counts = 0;

		counts = context.getContentResolver().delete(APN_URI_TABLES, "_id=?",
				new String[] { id + "" });

		return counts > 0;
	}

	/**
	 * ����ID��ȡAPN��Ϣ
	 * 
	 * @param context
	 *            ������
	 * @param id
	 * @return û�з���null
	 */
	public static ApnInfo getApnInfoById(Context context, int id) {

		ApnInfo mApnInfo = null;

		Cursor cursor = context.getContentResolver().query(APN_URI_TABLES,
				projection, "_id=?", new String[] { id + "" }, null);
		if (cursor != null && cursor.moveToFirst()) {
			mApnInfo = new ApnInfo();
			mApnInfo.id = cursor.getInt(cursor.getColumnIndex("_id"));
			mApnInfo.name = cursor.getString(cursor.getColumnIndex("name"));
			mApnInfo.apn = cursor.getString(cursor.getColumnIndex("apn"));
			mApnInfo.proxy = cursor.getString(cursor.getColumnIndex("proxy"));
			mApnInfo.port = cursor.getString(cursor.getColumnIndex("port"));
			mApnInfo.user = cursor.getString(cursor.getColumnIndex("user"));
			mApnInfo.password = cursor.getString(cursor
					.getColumnIndex("password"));
			mApnInfo.server = cursor.getString(cursor.getColumnIndex("server"));
			mApnInfo.mmsc = cursor.getString(cursor.getColumnIndex("mmsc"));
			mApnInfo.mmsproxy = cursor.getString(cursor
					.getColumnIndex("mmsproxy"));
			mApnInfo.mmsport = cursor.getString(cursor
					.getColumnIndex("mmsport"));
			mApnInfo.mcc = cursor.getString(cursor.getColumnIndex("mcc"));
			mApnInfo.mnc = cursor.getString(cursor.getColumnIndex("mnc"));
			mApnInfo.authtype = cursor.getString(cursor
					.getColumnIndex("authtype"));
			mApnInfo.type = cursor.getString(cursor.getColumnIndex("type"));
			mApnInfo.numeric = cursor.getString(cursor
					.getColumnIndex("numeric"));
			mApnInfo.current = cursor.getString(cursor
					.getColumnIndex("current"));
			cursor.close();
		}

		return mApnInfo;
	}

	/**
	 * ��ȡCMWap��ID
	 * 
	 * @param context
	 *            ������
	 * @return ������ڷ���ID�������ڷ���-1
	 */
	public static int getCMWapApnId(Context context) {

		int apnId = -1;

		Cursor cursor = context.getContentResolver().query(APN_URI_TABLES,
				projection, "apn=? and proxy=?",
				new String[] { "cmwap", "10.0.0.172" }, null);
		if (cursor != null && cursor.moveToFirst()) {
			apnId = cursor.getInt(cursor.getColumnIndex("_id"));
			cursor.close();
		}

		return apnId;
	}

	/**
	 * ����APNΪ��ǰAPN
	 * 
	 * @param context
	 *            ������
	 * @param id
	 *            Ҫ����APN��ID
	 */
	public static boolean setApn(Context context, int id) {

		int counts = 0;

		try {
			ContentValues mContentValues = new ContentValues();
			mContentValues.put("apn_id", id + "");
			counts = context.getContentResolver().update(APN_URI_PREFER,
					mContentValues, null, null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return counts > 0;
	}

	/**
	 * ����CMWap��APN��Ϣ
	 * 
	 * @return
	 */
	public static ApnInfo createCMWapApnInfo(Context context) {

		ApnInfo mApnInfo = new ApnInfo();
		mApnInfo.name = "�й��ƶ�CMWap";
		mApnInfo.apn = "cmwap";
		mApnInfo.proxy = "10.0.0.172";
		mApnInfo.port = "80";
		mApnInfo.server = "http://wap.monternet.com";
		mApnInfo.type = "default";
		mApnInfo.current = "1";
		String imsi = PhoneInfoHelper.getImsi(context);
		if (imsi != null) {
			mApnInfo.mcc = imsi.substring(0, 3);
			mApnInfo.mnc = imsi.substring(3, 5);
			mApnInfo.numeric = imsi.substring(0, 5);
		}

		return mApnInfo;
	}

	/**
	 * @ClassName: ApnInfo
	 * 
	 * @author: chellychi
	 * 
	 * @version: V1.0
	 * 
	 * @Date: 2013-8-29 ����5:47:35
	 * 
	 * @Description: ��<code>ApnInfo</code>��Apn��Ϣ���ʵ��</p>
	 * 
	 *               Copyright 2013�� All rights reserved.
	 * 
	 *               You have the permissions to modify.
	 * 
	 */
	public static class ApnInfo {

		/** id */
		public int id;
		/** �������� */
		public String name;
		/** APN���� */
		public String apn;
		/** ���� */
		public String proxy;
		/** �˿� */
		public String port;
		/** �û��� */
		public String user;
		/** ���� */
		public String password;
		/** ������ */
		public String server;
		/** �������� */
		public String mmsc;
		/** ���Ŵ��� */
		public String mmsproxy;
		/** ���Ŷ˿� */
		public String mmsport;
		/** ���ʴ��� */
		public String mcc;
		/** �ƶ����� */
		public String mnc;
		/** �����֤���� */
		public String authtype;
		/** APN���� */
		public String type;
		public String numeric;
		/** �Ƿ�ǰ����APN */
		public String current;

		@Override
		public String toString() {
			return "ApnInfo [id=" + id + ", name=" + name + ", apn=" + apn
					+ ", proxy=" + proxy + ", port=" + port + ", user=" + user
					+ ", password=" + password + ", server=" + server
					+ ", mmsc=" + mmsc + ", mmsproxy=" + mmsproxy
					+ ", mmsport=" + mmsport + ", mcc=" + mcc + ", mnc=" + mnc
					+ ", authtype=" + authtype + ", type=" + type
					+ ", numeric=" + numeric + ", current=" + current + "]";
		}

	}

}
