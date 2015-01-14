package com.g365.receiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.g365.database.SaveAppInfo;
import com.g365.entity.AdInstallListener;
import com.g365.entity.AdRemoveListener;
import com.g365.entity.AppInfo;

/**
 * 
 * @author nova
 * @���� 2013��2��27��14:19:45 @ ���� �Ǽ�����汻��װ��ж�صĽ�����
 * 
 */
public class MonitorInstallRemoveReceiver extends BroadcastReceiver {

	private AdInstallListener adInstallListener;
	private AdRemoveListener adRemoveListener;
	/** ��ȡapk��Ϣ�ŵ�applicaton�� ��Ҫ������� �Ͱ汾 ʵ���� */
	ArrayList<AppInfo> applist = new ArrayList<AppInfo>();

	private AppInfo tmpInfo = new AppInfo();

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		if (action != null) {
			adInstallListener = new AdInstallListener();
			adRemoveListener = new AdRemoveListener();

			/* ��ȡӦ�õİ���* */
			String packName = intent.getDataString();
			PackageManager packageManager = context.getPackageManager();
			if (packName == null) {

				int uid = intent.getExtras().getInt(
						AppInstallRemoveReceiver.DATA_KEY_UID);
				packName = packageManager.getNameForUid(uid);
			}
			packName = packName.replace("package:", "");

			/* ��ȡ����Ӧ�ð�����Ϣ* */
			PackageInfo packageInfo = null;
			try {
				packageInfo = packageManager.getPackageInfo(packName,
						PackageManager.GET_ACTIVITIES);
				//��ȡ����
				tmpInfo.packageName=packageInfo.packageName;
				//��ȡ�汾��
				tmpInfo.versionCode=packageInfo.versionCode;
			} catch (NameNotFoundException e) {
				e.printStackTrace();

			}
			if (action.equals(AppInstallRemoveReceiver.ACTION_PACKAGE_ADDED)) {// ��װ���滻

				SaveAppInfo saveAppInfo = new SaveAppInfo(context);
				saveAppInfo.deleteFormPackageName(packName);

				applist = saveAppInfo.getScrollDatato();
				if (!applist.contains(tmpInfo.getPackageName())) {
					saveAppInfo.savePackageVersion(tmpInfo);
				} else {
					saveAppInfo.updateVersionCode(tmpInfo);
				}
				adInstallListener.onInstall(context, packageInfo);
			} else if (action
					.equals(AppInstallRemoveReceiver.ACTION_PACKAGE_REMOVED)) {// ж��

				SaveAppInfo saveAppInfo = new SaveAppInfo(context);
				saveAppInfo.deleteIfUninstall(packName);
				adRemoveListener.onRemove(context, packName);
			}
		}
	}

}
