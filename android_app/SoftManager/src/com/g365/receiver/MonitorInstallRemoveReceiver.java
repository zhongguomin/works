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
 * @日期 2013年2月27日14:19:45 @ 描述 是监听广告被安装和卸载的接收器
 * 
 */
public class MonitorInstallRemoveReceiver extends BroadcastReceiver {

	private AdInstallListener adInstallListener;
	private AdRemoveListener adRemoveListener;
	/** 读取apk信息放到applicaton表 主要存入包名 和版本 实体类 */
	ArrayList<AppInfo> applist = new ArrayList<AppInfo>();

	private AppInfo tmpInfo = new AppInfo();

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		if (action != null) {
			adInstallListener = new AdInstallListener();
			adRemoveListener = new AdRemoveListener();

			/* 获取应用的包名* */
			String packName = intent.getDataString();
			PackageManager packageManager = context.getPackageManager();
			if (packName == null) {

				int uid = intent.getExtras().getInt(
						AppInstallRemoveReceiver.DATA_KEY_UID);
				packName = packageManager.getNameForUid(uid);
			}
			packName = packName.replace("package:", "");

			/* 获取整个应用包的信息* */
			PackageInfo packageInfo = null;
			try {
				packageInfo = packageManager.getPackageInfo(packName,
						PackageManager.GET_ACTIVITIES);
				//获取包名
				tmpInfo.packageName=packageInfo.packageName;
				//获取版本号
				tmpInfo.versionCode=packageInfo.versionCode;
			} catch (NameNotFoundException e) {
				e.printStackTrace();

			}
			if (action.equals(AppInstallRemoveReceiver.ACTION_PACKAGE_ADDED)) {// 安装和替换

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
					.equals(AppInstallRemoveReceiver.ACTION_PACKAGE_REMOVED)) {// 卸载

				SaveAppInfo saveAppInfo = new SaveAppInfo(context);
				saveAppInfo.deleteIfUninstall(packName);
				adRemoveListener.onRemove(context, packName);
			}
		}
	}

}
