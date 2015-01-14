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
 * @日期 2013年2月27日14:49:10
 * @是应用程序安装、替换和删除的监听器，只能动态注册使用
 * 
 */
public class AppInstallRemoveReceiver extends BroadcastReceiver {

	/** 安装新应用的 ACTION */
	public static final String ACTION_PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED";
	/** 替换应用的 ACTION */
	public static final String ACITON_PACKAGE_REPLACED = "android.intent.action.PACKAGE_REPLACED";
	/** 卸载应用的 ACTION */
	public static final String ACTION_PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED";
	/** 应用 UID的键 */
	public static final String DATA_KEY_UID = "android.intent.extra.UID";

	/** 应用被安装的监听器 */
	private OnAppInstallListener installListener;
	/** 应用被替换的监听器 */
	private OnAppReplaceListener replaceListener;
	/** 应用被卸载的监听器 */
	private OnAppRemoveListener removeListener;

	/**
	 * @param installAndRepalceListener
	 *            应用被安装和替换的监听器，不需要可指定为 null
	 * @param removeListener
	 *            应用被卸载的监听器，不需要可指定为 null
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
			/** 获取应用的包名 */
			String packName = intent.getDataString();
			PackageManager packageManager = context.getPackageManager();

			if (packName == null) {
				int uid = intent.getExtras().getInt(DATA_KEY_UID);
				packName = packageManager.getNameForUid(uid);
			}
			packName = packName.replace("package:", "");

			/** 获取整个应用包的信息 */
			PackageInfo packageInfo = null;
			try {
				packageInfo = packageManager.getPackageInfo(packName,
						PackageManager.GET_ACTIVITIES);

			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			if (action.equals(ACTION_PACKAGE_ADDED)) {// 安装
				if (installListener != null) {
					installListener.onInstall(context, packageInfo);

				}

			} else if (action.equals(ACITON_PACKAGE_REPLACED)) {// 替换
				if (replaceListener != null) {
					replaceListener.onReplace(context, packageInfo);
				}
			} else if (action.equals(ACTION_PACKAGE_REMOVED)) {// 卸载
				if (removeListener != null) {
					removeListener.onRemove(context, packName);
				}

			}

		}
	}

}
