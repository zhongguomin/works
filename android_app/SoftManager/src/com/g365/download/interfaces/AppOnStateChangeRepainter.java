package com.g365.download.interfaces;

import android.content.Intent;

/**
 * ��Ӧ��״̬�ı���ػ�ؼ��Ľӿ�
 * @author nova
 * ���� 2012��12��21��11:52:39
 */
public interface AppOnStateChangeRepainter {

	/**
	 * �ػ�ӿ�
	 * 
	 * @param view
	 *            Ҫ�ػ�Ŀؼ�
	 * @param appWallDownloadInfo
	 *            �ػ�����
	 */
	public void repaint(Intent intent);
}
