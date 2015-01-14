package comns.system;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * @类名: SystemIntent
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-11-2 上午11:03:20
 * 
 * @描述: 类<code>SystemIntent</code>是封装调用系统应用函数的类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class SystemIntent {

	/**
	 * 访问 IE 打开链接
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            链接地址
	 */
	public static void openUrl(Context context, String url) {

		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	/**
	 * 安装应用程序
	 * 
	 * @param context
	 *            上下文
	 * @param filePath
	 *            APK的路径
	 */
	public static void installApk(Context context, String filePath) {

		Uri uri = Uri.parse(filePath);
		Intent notificationIntent = new Intent(Intent.ACTION_VIEW, uri);
		notificationIntent.setData(uri);
		notificationIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		notificationIntent.setClassName("com.android.packageinstaller",
				"com.android.packageinstaller.PackageInstallerActivity");
		context.startActivity(notificationIntent);// 这种方式不能传递参数
	}

	/**
	 * 卸载应用程序
	 * 
	 * @param context
	 *            上下文
	 * @param Packname
	 *            要卸载的包名
	 */
	public static void unInstallApk(Context context, String packName) {

		try {
			Uri packageURI = Uri.parse("package:" + packName);
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
					packageURI);
			context.startActivity(uninstallIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 运行应用程序
	 * 
	 * @param context
	 *            上下文
	 * @param packName
	 *            要运行的程序包名
	 */
	public static void runApplication(Context context, String packName) {

		try {
			PackageManager pm = context.getPackageManager();
			Intent startIntent = pm.getLaunchIntentForPackage(packName);
			context.startActivity(startIntent);
		} catch (Exception e) {
			CustomAlertDialog customAlertDialog = new CustomAlertDialog(context);
			customAlertDialog.showYesNoDialog("此应用程序没有界面", null);
		}
	}

	/**
	 * 以入口方式启动包名为componentNames[0]中的名为componentNames[1]的Acitivity
	 * 
	 * @param context
	 *            上下文
	 * @param componentNames
	 *            Activity绝对路径名的字符串数组
	 */
	public static void startMainActivity(Activity context,
			String[] componentNames) {

		Intent startIntent = new Intent();
		try {
			startIntent.setComponent(new ComponentName(componentNames[0],
					componentNames[1]));
			startIntent.setAction("android.intent.action.MAIN");
			startIntent.addCategory("android.intent.category.LAUNCHER");
			startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(startIntent);
			context.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 以入口方式启动本应用的Activity
	 * 
	 * @param context
	 *            上下文
	 * @param activityName
	 *            要启动的Activity名字
	 */
	public static void startMainActivity(Activity context, String activityName) {
		startMainActivity(context, new String[] { context.getPackageName(),
				activityName });
	}

	/**
	 * 显示网络设置界面
	 * 
	 * @param context
	 *            上下文
	 */
	public static void showNetSetting(Context context) {

		Intent intent = new Intent();
		if (android.os.Build.VERSION.SDK_INT > 10) {
			intent.setAction(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		} else {
			ComponentName component = new ComponentName("com.android.settings",
					"com.android.settings.WirelessSettings");
			intent.setComponent(component);
			intent.setAction("android.intent.action.VIEW");
		}
		context.startActivity(intent);
	}
}
