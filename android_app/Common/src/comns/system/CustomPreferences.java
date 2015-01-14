package comns.system;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @����: CustomPreferences
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-11-1 ����03:44:22
 * 
 * @����: ��<code>CustomPreferences</code>�ǹ���������ص���</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class CustomPreferences {

	private static final String PREF = "my_pref_";

	private SharedPreferences sp;

	/**
	 * @param context
	 *            ������
	 * @param name
	 *            ������
	 */
	public CustomPreferences(Context context, String name) {

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

		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, value);
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
}
