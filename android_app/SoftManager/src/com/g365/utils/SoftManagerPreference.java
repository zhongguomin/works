package com.g365.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author nova
 * ���� 2013��1��5��14:58:45
 * 
 *  ����SharedPreferences��������
 */
public class SoftManagerPreference {

	private static final String PREF = "soft_pref_";

	/** ����������� */
	public static final String KEY_SOFT_UPDATE = "softupdate";
	/** ���ж�ظ��� */
	public static final String KEY_SOFT_UNINSTALL = "softuninstall";
	/** ��װ��ɾ������ */
	public static final String KEY_SOFT_INSTALLPAGES_DELETE = "softinstallpagesdelete";
	/** ��װ����װ���� */
	public static final String KEY_SOFT_INSTALLPAGES_INSTALL = "softinstallpagesinstall";
	/** �����Ҹ��� */
	public static final String KEY_SOFT_MOVE = "softmove";
	/** װ���ر�������� */
	public static final String KEY_SOFT_NECESSARY_ENTER = "softnecessaryenter";
	/** װ���ر����ش��� */
	public static final String KEY_SOFT_NECESSARY_DOWNLOAD = "softnecessarydownload";
	/** װ���ر���װ���� */
	public static final String KEY_SOFT_NECESSARY_INSTALL = "softnecessaryinstall";

	/** �û����� */
	public static final String PREF_NAME_USER_DATA = "user_data";
	/** �ϴ����ݷ��ͳɹ���ʱ�䣨long�� */
	public static final String KEY_LAST_POST_TIME = "lastposttime";

	/** �û�ʹ�����ݣ����ʹ��ʱ��㡢���� ���� ɾ�� ��װ ж�أ���String�� */
	public static final String KEY_USE_DATA = "usedata";

	private SharedPreferences sp;

	/**
	 * @param context
	 *            ������
	 * @param name
	 *            ������
	 */
	public SoftManagerPreference(Context context, String name) {
		// ��Preferences������ΪPREF+name�������������������򴴽��µ�Preferences
		sp = context.getSharedPreferences(PREF + name, 0);

	}

	/**
	 * �� int ���͵�ֵд�뵽Ĭ�ϵ�������
	 * 
	 * @param key
	 *            ����
	 * @param value
	 *            int ���͵�ֵ
	 */
	public void saveInt(String key, int value) {
		// ��sp���ڱ༭״̬
		SharedPreferences.Editor editor = sp.edit();
		// �������
		editor.putInt(key, value);
		// ����ύ
		editor.commit();
	}

	/**
	 * ��������м�����Ӧ�� int ���͵�ֵ
	 * 
	 * @param key
	 *            ����
	 * @param value
	 *            Ĭ��ֵ
	 * @return �����м�����Ӧ��ֵ����û�з���Ĭ��ֵ
	 */
	public int readInt(String key, int value) {
		return sp.getInt(key, value);

	}

	/**
	 * �� String ���͵�ֵд�뵽Ĭ�ϵ�������
	 * 
	 * @param key
	 *            ����
	 * @param value
	 *            String ���͵�ֵ
	 */
	public void saveString(String key, String value) {

		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * ��������м�����Ӧ�� String ���͵�ֵ
	 * 
	 * @param key
	 *            ����
	 * @param value
	 *            Ĭ��ֵ
	 * @return �����м�����Ӧ��ֵ����û�з���Ĭ��ֵ
	 */
	public String readString(String key, String value) {

		return sp.getString(key, value);
	}

	/**
	 * �� boolean ���͵�ֵд�뵽Ĭ�ϵ�������
	 * 
	 * @param key
	 *            ����
	 * @param value
	 *            boolean ���͵�ֵ
	 */
	public void saveBoolean(String key, boolean value) {

		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * ��������м�����Ӧ�� boolean ���͵�ֵ
	 * 
	 * @param key
	 *            ����
	 * @param value
	 *            Ĭ��ֵ
	 * @return �����м�����Ӧ��ֵ����û�з���Ĭ��ֵ
	 */
	public boolean readBoolean(String key, boolean value) {

		return sp.getBoolean(key, value);
	}

	/**
	 * �� long ���͵�ֵд�뵽Ĭ�ϵ�������
	 * 
	 * @param key
	 *            ����
	 * @param value
	 *            long ���͵�ֵ
	 */
	public void saveLong(String key, long value) {

		SharedPreferences.Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * ��������м�����Ӧ�� long ���͵�ֵ
	 * 
	 * @param key
	 *            ����
	 * @param value
	 *            Ĭ��ֵ
	 * @return �����м�����Ӧ��ֵ����û�з���Ĭ��ֵ
	 */
	public long readLong(String key, long value) {

		return sp.getLong(key, value);
	}

	/**
	 * �� float ���͵�ֵд�뵽Ĭ�ϵ�������
	 * 
	 * @param key
	 *            ����
	 * @param value
	 *            float ���͵�ֵ
	 */
	public void saveFloat(String key, float value) {

		SharedPreferences.Editor editor = sp.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	/**
	 * ��������м�����Ӧ�� float ���͵�ֵ
	 * 
	 * @param key
	 *            ����
	 * @param value
	 *            Ĭ��ֵ
	 * @return �����м�����Ӧ��ֵ����û�з���Ĭ��ֵ
	 */
	public float readFloat(String key, float value) {

		return sp.getFloat(key, value);
	}

	/**
	 * ������������ĸ���
	 * @param context
	 *        ������
	 * @param sum
	 *      ��������
	 */
	public static void saveSoftUpdate(Context context, int sum ) {
		SharedPreferences sp1 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp1.edit();
		editor.putInt(KEY_SOFT_UPDATE, sum);
		editor.commit();

	}

	/**
	 * �������ж�صĸ���
	 * @param context
	 *        ������
	 * @param sum
	 *      ж�ظ���
	 */
	public static void saveSoftUninstall(Context context,int sum) {
		SharedPreferences sp2 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp2.edit();
		editor.putInt(KEY_SOFT_UNINSTALL, sum);
		editor.commit();
	}

	/**
	 * ���氲װ��ɾ���ĸ���
	 * @param context
	 *        ������
	 * @param sum
	 *      ��װ��ɾ���ĸ���
	 */
	public static void saveSoftInstalldelete(Context context,int sum) {
		SharedPreferences sp3 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp3.edit();
		editor.putInt(KEY_SOFT_INSTALLPAGES_DELETE, sum);
		editor.commit();
	}

	/**
	 * ���氲װ����װ�ĸ���
	 * @param context
	 *        ������
	 * @param sum
	 *      ��װ����װ�ĸ���
	 */
	public static void saveSoftInstallPages(Context context,int sum) {
		SharedPreferences sp4 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp4.edit();
		editor.putInt(KEY_SOFT_INSTALLPAGES_INSTALL, sum);
		editor.commit();
	}

	/**
	 * ���������ҵĸ���
	 * @param context
	 *        ������
	 * @param sum
	 *      ��������
	 */
	public static void saveSoftMove(Context context,int sum) {
		SharedPreferences sp5 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp5.edit();
		editor.putInt(KEY_SOFT_MOVE, sum);
		editor.commit();
	}

	/**
	 * ����װ���ر��������
	 * @param context
	 *        ������
	 * @param sum
	 *      װ���ر��������
	 */
	public static void saveSoftNeccessaryEnter(Context context,int sum) {
		SharedPreferences sp6 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp6.edit();
		editor.putInt(KEY_SOFT_NECESSARY_ENTER, sum);
		editor.commit();
	}
   
	/**
	 * ����װ���ر����ش���
	 * @param context
	 *        ������
	 * @param sum
	 *      װ���ر����ش���
	 */
	public static void saveSoftNeccessaryDownload(Context context,int sum) {
		SharedPreferences sp7 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp7.edit();
		editor.putInt(KEY_SOFT_NECESSARY_DOWNLOAD, sum);
		editor.commit();
	}
	/**
	 * ����װ���ر���װ����
	 * @param context
	 *        ������
	 * @param sum
	 *        ��װ����
	 */
	public static void saveSoftNeccessaryInstall(Context context,int sum) {
		SharedPreferences sp8 = context.getSharedPreferences(PREF
				+ KEY_USE_DATA, 0);
		SharedPreferences.Editor editor = sp8.edit();
		editor.putInt(KEY_SOFT_NECESSARY_INSTALL, sum);
		editor.commit();
	}

}
