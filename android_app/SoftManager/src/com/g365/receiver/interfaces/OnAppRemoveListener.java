package com.g365.receiver.interfaces;

import android.content.Context;

/**
 * 
 * @author nova
 * @ ���� 2013��2��27��14:34:48
 * @ ��Ӧ�ñ�ж�صļ�����
 *
 */
public interface OnAppRemoveListener {

	/**
	 * Ӧ�ñ�ɾ��
	 * 
	 * @param context
	 *            ������
	 * @param packName
	 *            ����
	 */
	public void onRemove(Context context, String packName);
}
