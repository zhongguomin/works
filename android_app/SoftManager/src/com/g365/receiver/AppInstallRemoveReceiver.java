package com.g365.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.g365.receiver.interfaces.OnAppInstallListener;
import com.g365.receiver.interfaces.OnAppRemoveListener;
import com.g365.receiver.interfaces.OnAppReplaceListener;

/**
 * 
 * @author nova
 * @���� 2013��2��27��14:49:10
 * @��Ӧ�ó���װ���滻��ɾ���ļ�������ֻ�ܶ�̬ע��ʹ��
 * 
 */
public class AppInstallRemoveReceiver extends BroadcastReceiver {

	/** ��װ��Ӧ�õ� ACTION */
	public static final String ACTION_PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED";
	/** �滻Ӧ�õ� ACTION */
	public static final String ACITON_PACKAGE_REPLACED = "android.intent.action.PACKAGE_REPLACED";
	/** ж��Ӧ�õ� ACTION */
	public static final String ACTION_PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED";
	/** Ӧ�� UID�ļ� */
	public static final String DATA_KEY_UID = "android.intent.extra.UID";

	/** Ӧ�ñ���װ�ļ����� */
	private OnAppInstallListener installListener;
	/** Ӧ�ñ��滻�ļ����� */
	private OnAppReplaceListener replaceListener;
	/** Ӧ�ñ�ж�صļ����� */
	private OnAppRemoveListener removeListener;

	/**
	 * @param installAndRepalceListener
	 *            Ӧ�ñ���װ���滻�ļ�����������Ҫ��ָ��Ϊ null
	 * @param removeListener
	 *            Ӧ�ñ�ж�صļ�����������Ҫ��ָ��Ϊ null
	 */
	public AppInstallRemoveReceiver(OnAppInstallListener installListener,
			OnAppReplaceListener replaceListener,
			OnAppRemoveListener removeListener) {
		this.installListener = installListener;
		this.replaceListener = replaceListener;
		this.removeListener = removeListener;

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (action != null) {
			/** ��ȡӦ�õİ��� */
			String packName = intent.getDataString();
			PackageManager packageManager = context.getPackageManager();

			if (packName == null) {
				int uid = intent.getExtras().getInt(DATA_KEY_UID);
				packName = packageManager.getNameForUid(uid);
			}
			packName = packName.replace("package:", "");

			/** ��ȡ����Ӧ�ð�����Ϣ */
			PackageInfo packageInfo = null;
			try {
				packageInfo = packageManager.getPackageInfo(packName,
						PackageManager.GET_ACTIVITIES);

			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			if (action.equals(ACTION_PACKAGE_ADDED)) {// ��װ
				if (installListener != null) {
					installListener.onInstall(context, packageInfo);

				}

			} else if (action.equals(ACITON_PACKAGE_REPLACED)) {// �滻
				if (replaceListener != null) {
					replaceListener.onReplace(context, packageInfo);
				}
			} else if (action.equals(ACTION_PACKAGE_REMOVED)) {// ж��
				if (removeListener != null) {
					removeListener.onRemove(context, packName);
				}

			}

		}
	}

}
